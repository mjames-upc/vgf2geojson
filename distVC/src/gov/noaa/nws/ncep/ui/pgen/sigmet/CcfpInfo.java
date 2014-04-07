/*     */ package gov.noaa.nws.ncep.ui.pgen.sigmet;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringReader;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ 
/*     */ public class CcfpInfo
/*     */ {
/*     */   public static final String PGEN_TYPE_CCFP = "CCFP_SIGMET";
/*     */   public static final String TEXT_SEPERATOR = ":::";
/*  81 */   private static final ArrayList<AbstractDrawableComponent> sigs = new ArrayList();
/*     */ 
/*  86 */   public static final Map<String, Double> DIR_AZI_MAP = getDirAziMap();
/*     */ 
/*     */   private static Map<String, Double> getDirAziMap()
/*     */   {
/*  94 */     Map map = new HashMap();
/*     */ 
/*  96 */     map.put("N", Double.valueOf(0.0D));
/*  97 */     map.put("NNE", Double.valueOf(22.5D));
/*  98 */     map.put("NE", Double.valueOf(45.0D));
/*  99 */     map.put("ENE", Double.valueOf(67.5D));
/*     */ 
/* 101 */     map.put("E", Double.valueOf(90.0D));
/* 102 */     map.put("ESE", Double.valueOf(112.5D));
/* 103 */     map.put("SE", Double.valueOf(135.0D));
/* 104 */     map.put("SSE", Double.valueOf(157.5D));
/*     */ 
/* 106 */     map.put("S", Double.valueOf(180.0D));
/* 107 */     map.put("SSW", Double.valueOf(-157.5D));
/* 108 */     map.put("SW", Double.valueOf(-135.0D));
/* 109 */     map.put("WSW", Double.valueOf(-112.5D));
/*     */ 
/* 111 */     map.put("W", Double.valueOf(-90.0D));
/* 112 */     map.put("WNW", Double.valueOf(-67.5D));
/* 113 */     map.put("NW", Double.valueOf(-45.0D));
/* 114 */     map.put("NNW", Double.valueOf(-22.5D));
/*     */ 
/* 116 */     return map;
/*     */   }
/*     */ 
/*     */   public static String convertXml2Txt(String xml, String xltFileName)
/*     */   {
/* 130 */     String res = "";
/*     */ 
/* 132 */     Source xmlSource = new StreamSource(new StringReader(xml));
/* 133 */     Source xsltSource = new StreamSource(xltFileName);
/*     */ 
/* 135 */     TransformerFactory transFact = TransformerFactory.newInstance();
/*     */     try
/*     */     {
/* 138 */       Transformer trans = transFact.newTransformer(xsltSource);
/* 139 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 140 */       trans.transform(xmlSource, new StreamResult(baos));
/*     */ 
/* 142 */       res = new String(baos.toByteArray());
/*     */     } catch (Exception e) {
/* 144 */       System.out.println("Error: File is corrupt");
/*     */     }
/*     */ 
/* 147 */     return res;
/*     */   }
/*     */ 
/*     */   public static String saveCcfpXmlFile(String it, String vt)
/*     */   {
/* 161 */     Products filePrds = getCcfpFilePrds(it, vt);
/* 162 */     String fileName = PgenUtil.getPgenActivityTextProdPath() + 
/* 163 */       File.separator + getCcfpFileName();
/*     */ 
/* 165 */     FileTools.write(fileName, filePrds);
/*     */ 
/* 168 */     sigs.clear();
/*     */ 
/* 170 */     return fileName;
/*     */   }
/*     */ 
/*     */   public static String storeCcfpXmlFile(Product prod)
/*     */   {
/* 184 */     String dataURI = null;
/*     */ 
/* 186 */     String label = getCcfpFileName();
/* 187 */     prod.setOutputFile(label);
/*     */     try
/*     */     {
/* 191 */       dataURI = StorageUtils.storeProduct(prod);
/*     */     } catch (PgenStorageException e) {
/* 193 */       StorageUtils.showError(e);
/*     */     }
/*     */ 
/* 197 */     sigs.clear();
/*     */ 
/* 199 */     return dataURI;
/*     */   }
/*     */ 
/*     */   public static Products getCcfpFilePrds(String it, String vt)
/*     */   {
/* 214 */     PgenResource rsc = PgenSession.getInstance().getPgenResource();
/* 215 */     ArrayList prds = (ArrayList)rsc.getProducts();
/*     */ 
/* 217 */     Products filePrds = ProductConverter.convert(getCcfpPrds(prds, it, vt));
/*     */ 
/* 219 */     return filePrds;
/*     */   }
/*     */ 
/*     */   public static ArrayList<Product> getCcfpPrds(ArrayList<Product> prds, String it, String vt)
/*     */   {
/* 235 */     ArrayList list = new ArrayList();
/*     */     Iterator localIterator2;
/* 237 */     for (Iterator localIterator1 = prds.iterator(); localIterator1.hasNext(); 
/* 238 */       localIterator2.hasNext())
/*     */     {
/* 237 */       Product p = (Product)localIterator1.next();
/* 238 */       localIterator2 = p.getLayers().iterator(); continue; Layer l = (Layer)localIterator2.next();
/* 239 */       for (AbstractDrawableComponent adc : l.getDrawables()) {
/* 240 */         if ("CCFP_SIGMET".equalsIgnoreCase(adc.getPgenType())) {
/* 241 */           Ccfp c = (Ccfp)adc;
/* 242 */           Sigmet s = c.getSigmet();
/* 243 */           s.setEditableAttrStartTime(it);
/* 244 */           s.setEditableAttrEndTime(vt);
/* 245 */           c.setCollectionNameWithSigmet(s);
/* 246 */           sigs.add((Sigmet)((Ccfp)adc).getSigmet().copy());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 252 */     setIssueValidTimes(sigs, it, vt);
/*     */ 
/* 254 */     Layer l = new Layer();
/* 255 */     l.add(sigs);
/* 256 */     Product p = new Product();
/* 257 */     p.addLayer(l);
/* 258 */     list.add(p);
/*     */ 
/* 260 */     return list;
/*     */   }
/*     */ 
/*     */   public static Product getCcfpPrds(String it, String vt)
/*     */   {
/* 274 */     PgenResource rsc = PgenSession.getInstance().getPgenResource();
/* 275 */     List prds = rsc.getProducts();
/* 276 */     ArrayList sigmets = new ArrayList();
/*     */     Iterator localIterator2;
/* 278 */     for (Iterator localIterator1 = prds.iterator(); localIterator1.hasNext(); 
/* 279 */       localIterator2.hasNext())
/*     */     {
/* 278 */       Product p = (Product)localIterator1.next();
/* 279 */       localIterator2 = p.getLayers().iterator(); continue; Layer l = (Layer)localIterator2.next();
/* 280 */       for (AbstractDrawableComponent adc : l.getDrawables()) {
/* 281 */         if ("CCFP_SIGMET".equalsIgnoreCase(adc.getPgenType())) {
/* 282 */           Ccfp c = (Ccfp)adc;
/* 283 */           Sigmet s = c.getSigmet();
/* 284 */           s.setEditableAttrStartTime(it);
/* 285 */           s.setEditableAttrEndTime(vt);
/* 286 */           c.setCollectionNameWithSigmet(s);
/* 287 */           sigmets.add((Sigmet)((Ccfp)adc).getSigmet().copy());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 293 */     setIssueValidTimes(sigmets, it, vt);
/*     */ 
/* 295 */     Layer l = new Layer();
/* 296 */     l.add(sigmets);
/* 297 */     Product p = new Product();
/* 298 */     p.addLayer(l);
/* 299 */     p.setType("CCFP_SIGMET");
/*     */ 
/* 301 */     return p;
/*     */   }
/*     */ 
/*     */   private static void setIssueValidTimes(ArrayList<AbstractDrawableComponent> sigs, String it, String vt)
/*     */   {
/* 316 */     if (sigs == null) {
/* 317 */       return;
/*     */     }
/* 319 */     for (AbstractDrawableComponent adc : sigs)
/*     */     {
/* 321 */       ((Sigmet)adc).setEditableAttrStartTime(it);
/* 322 */       ((Sigmet)adc).setEditableAttrEndTime(vt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getCcfpFileName()
/*     */   {
/* 334 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
/* 335 */     sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
/*     */ 
/* 337 */     return sdf.format(new Date()) + "_CCFP.xml";
/*     */   }
/*     */ 
/*     */   public static String[] getCcftTxt(Sigmet sig)
/*     */   {
/* 355 */     return getCcfpTxt2(sig);
/*     */   }
/*     */ 
/*     */   public static double getDirPts2(String dir, double x, double y)
/*     */   {
/* 360 */     Double deg = (Double)DIR_AZI_MAP.get(dir);
/*     */ 
/* 362 */     double r2d = 57.295779500000002D;
/*     */ 
/* 364 */     if (deg == null) {
/* 365 */       return y;
/*     */     }
/* 367 */     double d = deg.doubleValue() / r2d;
/*     */ 
/* 369 */     return x * Math.sin(d) + y * Math.cos(d);
/*     */   }
/*     */ 
/*     */   public static Coordinate getDirMostCoor(String dir, Coordinate[] coors, IMapDescriptor md)
/*     */   {
/* 374 */     if (coors == null) {
/* 375 */       return null;
/*     */     }
/* 377 */     TreeMap tm = new TreeMap();
/*     */ 
/* 379 */     for (Coordinate c : coors)
/*     */     {
/* 381 */       tm.put(Double.valueOf(getDirPts2(dir, c.x, c.y)), c);
/*     */     }
/*     */ 
/* 386 */     return (Coordinate)tm.get(tm.lastKey());
/*     */   }
/*     */ 
/*     */   public static ArrayList<Coordinate> getDirMostPts(String dir, Coordinate[] coors, IMapDescriptor md)
/*     */   {
/* 391 */     GeodeticCalculator gc = new GeodeticCalculator(md.getCRS());
/* 392 */     Coordinate c1 = getDirMostCoor(dir, coors, md);
/*     */ 
/* 395 */     Point2D pts = null;
/*     */     try {
/* 397 */       gc.setStartingGeographicPoint(c1.x, c1.y);
/* 398 */       gc.setDirection(((Double)DIR_AZI_MAP.get(dir)).doubleValue(), 129640.0D);
/*     */ 
/* 400 */       pts = gc.getDestinationGeographicPoint();
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 404 */     ArrayList list = new ArrayList();
/* 405 */     if (pts == null) {
/* 406 */       return list;
/*     */     }
/* 408 */     list.add(c1);
/* 409 */     list.add(new Coordinate(pts.getX(), pts.getY()));
/*     */ 
/* 411 */     return list;
/*     */   }
/*     */ 
/*     */   public static Coordinate getDirMostTxt(String dir, Coordinate[] coors, IMapDescriptor md)
/*     */   {
/* 428 */     GeodeticCalculator gcTxt = new GeodeticCalculator(md.getCRS());
/* 429 */     Coordinate c1 = getDirMostCoor(dir, coors, md);
/*     */ 
/* 431 */     gcTxt.setStartingGeographicPoint(c1.x, c1.y);
/* 432 */     gcTxt.setDirection(((Double)DIR_AZI_MAP.get(dir)).doubleValue(), 166680.0D);
/* 433 */     Point2D spdTxt = gcTxt.getDestinationGeographicPoint();
/*     */ 
/* 435 */     return new Coordinate(spdTxt.getX(), spdTxt.getY());
/*     */   }
/*     */ 
/*     */   public static Coordinate getSigCentroid(Sigmet sig)
/*     */   {
/* 447 */     if (sig.getLinePoints().length < 2) {
/* 448 */       return null;
/*     */     }
/*     */ 
/* 451 */     GeometryFactory factory = new GeometryFactory();
/* 452 */     LineString g = factory.createLineString(sig.getLinePoints());
/* 453 */     Point p = g.getCentroid();
/*     */ 
/* 455 */     return p.getCoordinate();
/*     */   }
/*     */ 
/*     */   public static boolean isCrossingLon180(Sigmet sig)
/*     */   {
/* 466 */     if (sig.getLinePoints().length < 2) {
/* 467 */       return false;
/*     */     }
/*     */ 
/* 470 */     GeometryFactory factory = new GeometryFactory();
/* 471 */     LineString g = factory.createLineString(sig.getLinePoints());
/*     */ 
/* 473 */     GeometryFactory f2 = new GeometryFactory();
/* 474 */     LineString lon180 = f2.createLineString(new Coordinate[] { 
/* 475 */       new Coordinate(180.0D, 90.0D), new Coordinate(180.0D, -90.0D) });
/*     */ 
/* 477 */     GeometryFactory f3 = new GeometryFactory();
/* 478 */     LineString lon180x = f3.createLineString(new Coordinate[] { 
/* 479 */       new Coordinate(-180.0D, 90.0D), new Coordinate(-180.0D, -90.0D) });
/*     */ 
/* 481 */     return (g.crosses(lon180)) || (g.crosses(lon180x));
/*     */   }
/*     */ 
/*     */   public static double[] getCcfpTxtAziDir(Coordinate ctxt, Sigmet sig)
/*     */   {
/* 495 */     if (ctxt == null) {
/* 496 */       return null;
/*     */     }
/* 498 */     Coordinate c = getSigCentroid(sig);
/*     */ 
/* 500 */     if (c == null) {
/* 501 */       return null;
/*     */     }
/* 503 */     double[] d = new double[2];
/*     */ 
/* 505 */     GeodeticCalculator gc = new GeodeticCalculator(
/* 506 */       PgenSession.getInstance().getPgenResource().getCoordinateReferenceSystem());
/*     */     try
/*     */     {
/* 509 */       gc.setStartingGeographicPoint(c.x, c.y);
/* 510 */       gc.setDestinationGeographicPoint(ctxt.x, ctxt.y);
/* 511 */       d[0] = gc.getAzimuth();
/* 512 */       d[1] = gc.getOrthodromicDistance();
/*     */     } catch (Exception e) {
/* 514 */       System.out.println("___________ Exception: " + e.getMessage());
/*     */     }
/*     */ 
/* 519 */     saveArrwTxtPts(ctxt, c, sig);
/*     */ 
/* 521 */     return d;
/*     */   }
/*     */ 
/*     */   public static boolean isPtsInArea(AbstractSigmet sig, Coordinate pts)
/*     */   {
/* 534 */     if ((sig == null) || (sig.getLinePoints() == null) || 
/* 535 */       (sig.getLinePoints().length < 3) || (pts == null)) {
/* 536 */       return false;
/*     */     }
/*     */ 
/* 539 */     Coordinate[] c = new Coordinate[sig.getLinePoints().length + 1];
/* 540 */     c = (Coordinate[])Arrays.copyOf(sig.getLinePoints(), c.length);
/* 541 */     c[(c.length - 1)] = c[0];
/*     */ 
/* 543 */     GeometryFactory f = new GeometryFactory();
/*     */ 
/* 545 */     return f.createPolygon(f.createLinearRing(c), null).contains(
/* 546 */       new GeometryFactory().createPoint(pts));
/*     */   }
/*     */ 
/*     */   public static Coordinate getSigCentroid2(Sigmet sig, IMapDescriptor md)
/*     */   {
/* 557 */     if ((sig == null) || (sig.getLinePoints() == null) || 
/* 558 */       (sig.getLinePoints().length < 2)) {
/* 559 */       return null;
/*     */     }
/* 561 */     Coordinate[] sigCoors = sig.getLinePoints();
/* 562 */     double[][] list = PgenUtil.latlonToPixel(sigCoors, md);
/*     */ 
/* 564 */     double x = 0.0D; double y = 0.0D;
/*     */ 
/* 566 */     for (double[] dd : list) {
/* 567 */       x += dd[0];
/* 568 */       y += dd[1];
/*     */     }
/*     */ 
/* 571 */     double avgX = x / sigCoors.length;
/* 572 */     double avgY = y / sigCoors.length;
/*     */ 
/* 574 */     double[] ptw = md.pixelToWorld(new double[] { avgX, avgY });
/*     */ 
/* 576 */     return new Coordinate(ptw[0], ptw[1]);
/*     */   }
/*     */ 
/*     */   public static boolean isPtsInArea2(AbstractSigmet sig, Coordinate pts, IMapDescriptor md)
/*     */   {
/* 591 */     if ((sig == null) || (sig.getLinePoints() == null) || 
/* 592 */       (sig.getLinePoints().length < 3) || (pts == null)) {
/* 593 */       return false;
/*     */     }
/*     */ 
/* 596 */     Coordinate[] sigCoors = sig.getLinePoints();
/* 597 */     double[][] list = PgenUtil.latlonToPixel(sigCoors, md);
/*     */ 
/* 599 */     TreeSet xset = new TreeSet(); TreeSet yset = new TreeSet();
/*     */ 
/* 601 */     for (double[] dd : list) {
/* 602 */       xset.add(Double.valueOf(dd[0]));
/* 603 */       yset.add(Double.valueOf(dd[1]));
/*     */     }
/*     */ 
/* 606 */     double[][] dpts = PgenUtil.latlonToPixel(new Coordinate[] { pts }, md);
/*     */ 
/* 608 */     return (dpts[0][0] >= ((Double)xset.first()).doubleValue()) && (dpts[0][0] <= ((Double)xset.last()).doubleValue()) && 
/* 609 */       (dpts[0][1] >= ((Double)yset.first()).doubleValue()) && (
/* 609 */       dpts[0][1] <= ((Double)yset.last()).doubleValue());
/*     */   }
/*     */ 
/*     */   private static void saveArrwTxtPts(Coordinate cTxt, Coordinate cArrw, Sigmet sig)
/*     */   {
/*     */     try
/*     */     {
/* 628 */       sig.setEditableAttrLevelInfo2(cArrw.x);
/* 629 */       sig.setEditableAttrLevelInfo1(cArrw.y);
/*     */ 
/* 632 */       sig.setEditableAttrLevelText2(cTxt.x);
/* 633 */       sig.setEditableAttrLevelText1(cTxt.y);
/*     */     }
/*     */     catch (Exception e) {
/* 636 */       System.out.println("___________ Exception: " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void calAziDist4TxtArrw(Sigmet sigmet)
/*     */   {
/* 649 */     if (sigmet == null) {
/* 650 */       return;
/*     */     }
/* 652 */     String tLon = sigmet.getEditableAttrLevelText2();
/* 653 */     String tLat = sigmet.getEditableAttrLevelText1();
/* 654 */     if ((tLon == null) || (tLat == null) || (tLon.isEmpty()) || (tLat.isEmpty())) {
/* 655 */       return;
/*     */     }
/* 657 */     double[] ad = null;
/*     */     try
/*     */     {
/* 661 */       double dLon = Double.parseDouble(tLon);
/* 662 */       double dLat = Double.parseDouble(tLat);
/*     */ 
/* 664 */       ad = getCcfpTxtAziDir(new Coordinate(dLon, dLat), sigmet);
/*     */     }
/*     */     catch (Throwable t) {
/* 667 */       System.out.println("___________ Error: " + t.getMessage());
/*     */     }
/*     */ 
/* 670 */     if (ad == null) {
/* 671 */       return;
/*     */     }
/* 673 */     sigmet.setEditableAttrFreeText(ad[0] + ":::" + ad[1]);
/* 674 */     sigmet.setEditableAttrFromLine("CCFP_SIGMET");
/*     */   }
/*     */ 
/*     */   public static boolean isTxtArrwExst(Sigmet sigmet)
/*     */   {
/* 686 */     if (sigmet == null) {
/* 687 */       return false;
/*     */     }
/* 689 */     String fline = sigmet.getEditableAttrFromLine();
/* 690 */     String ftext = sigmet.getEditableAttrFreeText();
/*     */ 
/* 692 */     if ((fline != null) && (!fline.isEmpty()) && (ftext != null) && 
/* 693 */       (!ftext.isEmpty()))
/*     */     {
/* 695 */       if (("CCFP_SIGMET".equalsIgnoreCase(fline.trim())) && 
/* 696 */         (ftext.contains(":::"))) {
/* 697 */         return true;
/*     */       }
/*     */     }
/* 700 */     return false;
/*     */   }
/*     */ 
/*     */   public static String[] getCcfpTxt2(Sigmet sig)
/*     */   {
/* 705 */     String tops = sig.getEditableAttrPhenom2();
/* 706 */     String gwth = sig.getEditableAttrPhenomLon();
/*     */ 
/* 708 */     if (tops.contains("-"))
/* 709 */       tops = " " + tops.substring(tops.indexOf("-") + 1);
/* 710 */     else if (tops.contains("+")) {
/* 711 */       tops = " " + tops.replace("+", "");
/*     */     }
/* 713 */     if (gwth.contains("+"))
/* 714 */       tops = tops + " " + gwth;
/*     */     else {
/* 716 */       tops = tops + "   ";
/*     */     }
/* 718 */     return new String[] { tops };
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.CcfpInfo
 * JD-Core Version:    0.6.2
 */