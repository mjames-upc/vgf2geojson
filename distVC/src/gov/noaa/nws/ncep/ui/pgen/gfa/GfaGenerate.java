/*     */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.SerializationUtil;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LinearRing;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ 
/*     */ public class GfaGenerate
/*     */ {
/*     */   private static final String TEXT_TYPE = "TEXT";
/*  73 */   private GeometryFactory gf = new GeometryFactory();
/*     */   private static final String ISSUE_TYPE_FROM_OUTLOOK = "ISSUE_TYPE_FROM_OUTLOOK";
/*     */   private Transformer transformer;
/*  80 */   private String XSLT_FILE = PgenStaticDataProvider.getProvider()
/*  80 */     .getFileAbsolutePath(
/*  81 */     PgenStaticDataProvider.getProvider()
/*  82 */     .getPgenLocalizationRoot() + 
/*  83 */     "xslt" + 
/*  84 */     File.separator + 
/*  85 */     "airmet" + 
/*  86 */     File.separator + 
/*  87 */     "gfa_product.xsl");
/*     */ 
/*     */   public StringBuilder generate(List<Gfa> all, List<String> areas, List<String> categories, String dataURI)
/*     */     throws IOException, JAXBException
/*     */   {
/* 103 */     StringBuilder sb = new StringBuilder();
/* 104 */     StringBuilder temp = new StringBuilder();
/* 105 */     String cycle = PgenCycleTool.pad(PgenCycleTool.getCycleHour());
/*     */ 
/* 107 */     List adjusted = new ArrayList();
/*     */ 
/* 112 */     for (Gfa g : all) {
/* 113 */       Gfa sg = createAdjacentGfa(g);
/*     */ 
/* 115 */       adjusted.add(g);
/*     */ 
/* 117 */       if (sg != null) {
/* 118 */         adjusted.add(sg);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 125 */     trackOtlkIssueTypeToAirmet(adjusted);
/*     */     Iterator localIterator2;
/* 131 */     for (??? = categories.iterator(); ???.hasNext(); 
/* 132 */       localIterator2.hasNext())
/*     */     {
/* 131 */       String category = (String)???.next();
/* 132 */       localIterator2 = areas.iterator(); continue; String area = (String)localIterator2.next();
/*     */ 
/* 134 */       List ret = filterSelected(adjusted, area, category);
/*     */ 
/* 137 */       for (Gfa de : ret) {
/* 138 */         if ((!de.isSnapshot()) && 
/* 139 */           (de.getAttribute("ISSUE_TIME") == null)) {
/* 140 */           GfaRules.assignIssueTime(de);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 145 */       String fileName = "AIRMET_" + category + "_" + area + "_" + 
/* 146 */         cycle + ".txt";
/*     */ 
/* 150 */       gov.noaa.nws.ncep.ui.pgen.elements.Product p = new gov.noaa.nws.ncep.ui.pgen.elements.Product();
/* 151 */       gov.noaa.nws.ncep.ui.pgen.elements.Layer l = new gov.noaa.nws.ncep.ui.pgen.elements.Layer();
/* 152 */       p.addLayer(l);
/*     */ 
/* 157 */       String fzlvlRange = findFreezingRange(all, ret, area, category);
/*     */ 
/* 164 */       for (Gfa gss : ret) {
/* 165 */         Gfa gfaCopy = gss.copy();
/* 166 */         String otlkIssueType = Gfa.nvl(gfaCopy.getGfaValue("ISSUE_TYPE_FROM_OUTLOOK"));
/* 167 */         if (!otlkIssueType.equals("NRML")) {
/* 168 */           gfaCopy.setGfaIssueType(otlkIssueType);
/*     */         }
/*     */ 
/* 171 */         if (fzlvlRange != null) {
/* 172 */           gfaCopy.setGfaValue("FZL RANGE", fzlvlRange);
/*     */         }
/* 174 */         l.add(gfaCopy);
/*     */       }
/*     */ 
/* 178 */       List pList = new ArrayList();
/* 179 */       pList.add(p);
/*     */ 
/* 181 */       Products products = ProductConverter.convert(pList);
/*     */ 
/* 185 */       if (ret.size() == 0) {
/* 186 */         addNullGfa(products, category, fzlvlRange);
/*     */       }
/*     */ 
/* 189 */       String xml = SerializationUtil.marshalToXml(products);
/*     */ 
/* 191 */       if ((sb.length() > 0) && (!sb.toString().endsWith("\n\n"))) {
/* 192 */         sb.append("\n\n");
/*     */       }
/*     */ 
/* 195 */       String prdXml = generateProduct(xml, category, area).trim();
/* 196 */       temp.append(prdXml);
/*     */ 
/* 199 */       storeProduct(temp, fileName, dataURI);
/*     */ 
/* 201 */       sb.append(temp);
/* 202 */       temp.setLength(0);
/*     */     }
/*     */ 
/* 206 */     return sb;
/*     */   }
/*     */ 
/*     */   private void storeProduct(StringBuilder temp, String fileName, String dataURI)
/*     */   {
/*     */     try
/*     */     {
/* 213 */       StorageUtils.storeDerivedProduct(dataURI, fileName, "TEXT", 
/* 214 */         temp.toString());
/*     */     } catch (PgenStorageException e) {
/* 216 */       StorageUtils.showError(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void saveToFile(StringBuilder sb, String fileName)
/*     */     throws IOException
/*     */   {
/* 223 */     FileTools.writeFile(fileName, sb.toString());
/*     */   }
/*     */ 
/*     */   private static List<Gfa> filterSelected(List<Gfa> all, String area, String category)
/*     */   {
/* 238 */     ArrayList ret = new ArrayList();
/*     */ 
/* 240 */     int ii = 0;
/* 241 */     for (Gfa g : all)
/*     */     {
/* 243 */       GfaInfo.HazardCategory c = GfaInfo.getHazardCategory(g
/* 244 */         .getGfaHazard());
/* 245 */       boolean inCategory = category.equals(c.toString());
/*     */ 
/* 247 */       String[] s = Gfa.nvl(g.getGfaArea()).split("-");
/* 248 */       boolean inArea = Gfa.nvl(s[0]).contains(area);
/*     */ 
/* 250 */       if ((inArea) && (inCategory)) {
/* 251 */         ret.add(g);
/*     */       }
/*     */ 
/* 254 */       ii++;
/*     */     }
/*     */ 
/* 257 */     return ret;
/*     */   }
/*     */ 
/*     */   public String generateProduct(String prdxml, String category, String area)
/*     */   {
/* 262 */     String xml1 = prdxml.replaceAll("TURB-HI", "TURB");
/* 263 */     String xml = xml1.replaceAll("TURB-LO", "TURB");
/*     */ 
/* 265 */     String res = "";
/*     */     try
/*     */     {
/* 268 */       StreamSource xmlSource = new StreamSource(new StringReader(xml));
/* 269 */       if (this.transformer == null) {
/* 270 */         TransformerFactory tFactory = TransformerFactory.newInstance();
/* 271 */         Source xsltSource = new StreamSource(this.XSLT_FILE);
/* 272 */         this.transformer = tFactory.newTransformer(xsltSource);
/*     */       }
/*     */ 
/* 275 */       StreamResult result = new StreamResult(new StringWriter());
/* 276 */       this.transformer.reset();
/* 277 */       this.transformer.setParameter("categories", category);
/* 278 */       this.transformer.setParameter("areas", area);
/* 279 */       this.transformer.transform(xmlSource, result);
/*     */ 
/* 281 */       res = result.getWriter().toString().trim();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 285 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 290 */     if (res.contains("FRZLVL...")) {
/* 291 */       res = wrapFrzl(res);
/*     */     }
/*     */ 
/* 294 */     return res;
/*     */   }
/*     */ 
/*     */   private static Gfa createAdjacentGfa(Gfa g)
/*     */   {
/* 308 */     Gfa secondg = null;
/*     */ 
/* 310 */     String[] s = Gfa.nvl(g.getGfaArea()).split("-");
/*     */ 
/* 312 */     if (s.length > 1) {
/* 313 */       String sname = new String(s[1].trim() + "-" + s[0].trim());
/*     */ 
/* 316 */       secondg = g.copy();
/* 317 */       secondg.setGfaArea(sname);
/* 318 */       GfaRules.reorderStateList(secondg);
/*     */     }
/*     */ 
/* 321 */     return secondg;
/*     */   }
/*     */ 
/*     */   private String wrapFrzl(String frzl)
/*     */   {
/* 332 */     StringBuffer sb = new StringBuffer(frzl);
/*     */ 
/* 334 */     int startIdx = sb.indexOf("\n", sb.indexOf("FRZLVL...")) + 1;
/* 335 */     int endIdx = sb.indexOf("....", startIdx);
/*     */ 
/* 337 */     int ii = startIdx;
/* 338 */     int ii64 = 0;
/*     */     do
/*     */     {
/* 341 */       ii64 = ii + 64;
/* 342 */       int newline = sb.indexOf("\n", ii);
/*     */ 
/* 344 */       if (newline <= ii64)
/* 345 */         ii = newline + 1;
/*     */       else
/* 347 */         for (int jj = ii64; jj >= ii; jj--)
/*     */         {
/* 350 */           if ((sb.charAt(jj) == ' ') || (sb.charAt(jj) == '-')) {
/* 351 */             sb.insert(jj + 1, "\n      ");
/* 352 */             ii = jj + 2;
/* 353 */             break;
/*     */           }
/*     */         }
/*     */     }
/* 340 */     while (
/* 358 */       ii64 < endIdx);
/*     */ 
/* 360 */     return new String(sb);
/*     */   }
/*     */ 
/*     */   private void addNullGfa(Products prds, String category, String fzlRange)
/*     */   {
/* 372 */     gov.noaa.nws.ncep.ui.pgen.file.Gfa fgfa = new gov.noaa.nws.ncep.ui.pgen.file.Gfa();
/*     */ 
/* 374 */     fgfa.setPgenCategory("MET");
/* 375 */     fgfa.setPgenType("GFA");
/*     */ 
/* 379 */     fgfa.setHazard(setFirstHazardType(category));
/* 380 */     fgfa.setFcstHr("0-6");
/*     */ 
/* 383 */     if (fzlRange != null) {
/* 384 */       fgfa.setHazard("FZLVL");
/* 385 */       fgfa.setFzlRange(fzlRange);
/*     */     }
/*     */ 
/* 389 */     SimpleDateFormat sdf = new SimpleDateFormat("ddHHmm");
/*     */ 
/* 391 */     Calendar cal = Calendar.getInstance();
/* 392 */     String timeStr = AirmetCycleInfo.getIssueTime();
/*     */ 
/* 394 */     int hour = Integer.parseInt(timeStr.substring(0, 2));
/* 395 */     int min = Integer.parseInt(timeStr.substring(2));
/*     */ 
/* 397 */     cal.set(11, hour);
/* 398 */     cal.set(12, min);
/* 399 */     cal.set(13, 0);
/*     */ 
/* 401 */     fgfa.setIssueTime(sdf.format(cal.getTime()));
/*     */ 
/* 403 */     cal = AirmetCycleInfo.getUntilTime();
/* 404 */     fgfa.setUntilTime(sdf.format(cal.getTime()));
/*     */ 
/* 407 */     ((gov.noaa.nws.ncep.ui.pgen.file.Layer)((gov.noaa.nws.ncep.ui.pgen.file.Product)prds.getProduct().get(0)).getLayer().get(0)).getDrawableElement()
/* 408 */       .getGfa().add(fgfa);
/*     */   }
/*     */ 
/*     */   private String setFirstHazardType(String category)
/*     */   {
/* 417 */     String type = "NONE";
/*     */ 
/* 419 */     if (category.equals(GfaInfo.HazardCategory.SIERRA.toString()))
/* 420 */       type = new String("IFR");
/* 421 */     else if (category.equals(GfaInfo.HazardCategory.TANGO.toString()))
/* 422 */       type = new String("TURB");
/* 423 */     else if (category.equals(GfaInfo.HazardCategory.ZULU.toString())) {
/* 424 */       type = new String("ICE");
/*     */     }
/*     */ 
/* 427 */     return type;
/*     */   }
/*     */ 
/*     */   private String findFreezingRange(List<Gfa> all, List<Gfa> selected, String area, String cat)
/*     */   {
/* 437 */     if (!cat.equalsIgnoreCase("ZULU")) {
/* 438 */       return null;
/*     */     }
/*     */ 
/* 442 */     String fzlRange = null;
/* 443 */     String topStr = null;
/* 444 */     String botStr = null;
/*     */ 
/* 450 */     if ((selected == null) || (selected.size() == 0)) {
/* 451 */       int[] extRange = findExernalFzlvlRange(all, area);
/* 452 */       if (extRange[0] != -1) {
/* 453 */         topStr = padding(extRange[0]);
/* 454 */         botStr = padding(extRange[1]);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 467 */       int[] existingRange = findExistingFzlRange(all, selected, area);
/* 468 */       int[] mfzlRange = findMfzlvlRange(all, selected, area);
/*     */ 
/* 470 */       if (existingRange[0] == -1) {
/* 471 */         existingRange = findFzlvlLevelRange(all, selected, area);
/*     */       }
/*     */ 
/* 474 */       if ((existingRange[0] != -1) || (mfzlRange[0] != -1)) {
/* 475 */         topStr = padding(Math.max(existingRange[0], mfzlRange[0]));
/*     */       }
/*     */ 
/* 478 */       if ((existingRange[1] != 9999) || (mfzlRange[1] != 9999)) {
/* 479 */         botStr = padding(Math.min(existingRange[1], mfzlRange[1]));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 484 */     if ((topStr != null) && (botStr != null)) {
/* 485 */       fzlRange = area + ";" + topStr + ";" + botStr;
/*     */     }
/*     */ 
/* 488 */     return fzlRange;
/*     */   }
/*     */ 
/*     */   private int[] findExistingFzlRange(List<Gfa> all, List<Gfa> selected, String area)
/*     */   {
/* 500 */     int[] topBot = { -1, 9999 };
/*     */ 
/* 502 */     for (Gfa elem : selected)
/*     */     {
/* 504 */       String gtype = elem.getGfaHazard();
/*     */ 
/* 506 */       if ((gtype.equalsIgnoreCase("FZLVL")) && (!elem.isSnapshot()))
/*     */       {
/* 510 */         String range1 = elem.getGfaValue("FZL RANGE");
/* 511 */         if (range1 != null) {
/* 512 */           String[] rangeInfo = range1.split(";");
/* 513 */           if ((rangeInfo.length >= 3) && (rangeInfo[0].equalsIgnoreCase(area))) {
/* 514 */             int top1 = -1;
/* 515 */             int bot1 = -1;
/*     */             try
/*     */             {
/* 518 */               top1 = Integer.parseInt(rangeInfo[1]);
/*     */             }
/*     */             catch (Exception e) {
/* 521 */               e.printStackTrace();
/*     */             }
/*     */ 
/* 524 */             if (rangeInfo[2].equalsIgnoreCase("SFC"))
/* 525 */               bot1 = 0;
/*     */             else {
/*     */               try
/*     */               {
/* 529 */                 bot1 = Integer.parseInt(rangeInfo[2]);
/*     */               }
/*     */               catch (Exception e) {
/* 532 */                 e.printStackTrace();
/*     */               }
/*     */             }
/*     */ 
/* 536 */             if ((top1 >= 0) && (bot1 >= 0)) {
/* 537 */               topBot[0] = Math.max(topBot[0], top1);
/* 538 */               topBot[1] = Math.min(topBot[1], bot1);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 544 */     return topBot;
/*     */   }
/*     */ 
/*     */   private int[] findMfzlvlRange(List<Gfa> all, List<Gfa> selected, String area)
/*     */   {
/* 553 */     int[] topBot = { -1, 9999 };
/*     */ 
/* 555 */     for (Gfa elem : selected)
/*     */     {
/* 557 */       String gtype = elem.getGfaHazard();
/*     */ 
/* 559 */       if ((gtype.equalsIgnoreCase("M_FZLVL")) && (!elem.isSnapshot()))
/*     */       {
/* 563 */         int top2 = -1;
/* 564 */         int bot2 = -1;
/*     */ 
/* 566 */         if (elem.getGfaTop() != null) {
/*     */           try {
/* 568 */             top2 = Integer.parseInt(elem.getGfaTop());
/*     */           }
/*     */           catch (Exception e) {
/* 571 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */ 
/* 575 */         String botStr = elem.getGfaBottom();
/* 576 */         if (botStr != null) {
/* 577 */           if (botStr.equalsIgnoreCase("SFC"))
/* 578 */             bot2 = 0;
/*     */           else {
/*     */             try
/*     */             {
/* 582 */               bot2 = Integer.parseInt(elem.getGfaBottom());
/*     */             }
/*     */             catch (Exception e) {
/* 585 */               e.printStackTrace();
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 590 */         if ((top2 >= 0) && (bot2 >= 0)) {
/* 591 */           topBot[0] = Math.max(topBot[0], top2);
/* 592 */           topBot[1] = Math.min(topBot[1], bot2);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 597 */     return topBot;
/*     */   }
/*     */ 
/*     */   private int[] findFzlvlLevelRange(List<Gfa> all, List<Gfa> selected, String area)
/*     */   {
/* 607 */     int[] topBot = { -1, 9999 };
/* 608 */     for (Gfa elem : selected)
/*     */     {
/* 610 */       String gtype = elem.getGfaHazard();
/*     */ 
/* 612 */       if ((gtype.equalsIgnoreCase("FZLVL")) && (!elem.isSnapshot()))
/*     */       {
/* 616 */         int top2 = -1;
/* 617 */         int bot2 = -1;
/*     */ 
/* 619 */         String levelStr = elem.getGfaValue("Level");
/* 620 */         int lvl = -1;
/*     */ 
/* 622 */         if (levelStr != null)
/*     */         {
/* 624 */           if (levelStr.equalsIgnoreCase("SFC")) {
/* 625 */             top2 = 40;
/* 626 */             bot2 = 0;
/*     */           }
/*     */           else {
/*     */             try {
/* 630 */               lvl = Integer.parseInt(levelStr);
/*     */             }
/*     */             catch (Exception e) {
/* 633 */               e.printStackTrace();
/*     */             }
/*     */ 
/* 636 */             if ((lvl >= 0) && (lvl <= 40)) {
/* 637 */               top2 = 40;
/* 638 */               bot2 = 0;
/*     */             }
/*     */             else {
/* 641 */               top2 = lvl + 40;
/* 642 */               bot2 = lvl - 40;
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 648 */         if ((top2 >= 0) && (bot2 >= 0)) {
/* 649 */           topBot[0] = Math.max(topBot[0], top2);
/* 650 */           topBot[1] = Math.min(topBot[1], bot2);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 655 */     return topBot;
/*     */   }
/*     */ 
/*     */   private int[] findExernalFzlvlRange(List<Gfa> all, String area)
/*     */   {
/* 674 */     int[] topBot = { -1, 9999 };
/*     */ 
/* 676 */     HashMap areaBnds = GfaClip.getInstance().getFaAreaBounds();
/*     */ 
/* 678 */     Coordinate center = this.gf.createLinearRing(((Geometry)areaBnds.get(area)).getCoordinates()).getCentroid().getCoordinate();
/*     */ 
/* 680 */     Gfa closestGfa = null;
/* 681 */     double minDist = 1.7976931348623157E+308D;
/*     */ 
/* 683 */     for (Gfa elem : all)
/*     */     {
/* 685 */       String gtype = elem.getGfaHazard();
/*     */ 
/* 687 */       if (gtype.equalsIgnoreCase("FZLVL"))
/*     */       {
/* 692 */         double dist = distance(elem, center);
/* 693 */         if (dist < minDist) {
/* 694 */           minDist = dist;
/* 695 */           closestGfa = elem;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 700 */     if (closestGfa != null) {
/* 701 */       boolean isLeft = GfaSnap.getInstance().atLeft(center, closestGfa.getLinePoints(), 
/* 702 */         closestGfa.isClosedLine().booleanValue(), 0.0D);
/*     */ 
/* 704 */       String levelStr = closestGfa.getGfaValue("Level");
/* 705 */       int lvl = -1;
/*     */ 
/* 707 */       if (levelStr != null)
/*     */       {
/* 709 */         if (levelStr.equalsIgnoreCase("SFC"))
/* 710 */           lvl = 0;
/*     */         else {
/*     */           try
/*     */           {
/* 714 */             lvl = Integer.parseInt(levelStr);
/*     */           }
/*     */           catch (Exception e) {
/* 717 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 722 */       if (lvl >= 0) {
/* 723 */         if (!isLeft) {
/* 724 */           topBot[0] = (lvl + 40);
/* 725 */           topBot[1] = lvl;
/*     */         }
/*     */         else {
/* 728 */           topBot[0] = lvl;
/* 729 */           topBot[1] = (lvl - 40);
/* 730 */           if (topBot[1] < 0) topBot[1] = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 735 */     return topBot;
/*     */   }
/*     */ 
/*     */   private String padding(int value)
/*     */   {
/* 744 */     StringBuilder str = new StringBuilder();
/* 745 */     if (value <= 0) {
/* 746 */       str.append("SFC");
/*     */     }
/* 749 */     else if (value < 100) {
/* 750 */       str.append("0" + value);
/*     */     }
/*     */     else {
/* 753 */       str.append(value);
/*     */     }
/*     */ 
/* 757 */     return str.toString();
/*     */   }
/*     */ 
/*     */   private double distance(Gfa gfa, Coordinate loc)
/*     */   {
/* 766 */     double dist = 1.7976931348623157E+308D;
/* 767 */     double minDist = 1.7976931348623157E+308D;
/*     */ 
/* 769 */     Object[] pts = gfa.getPoints().toArray();
/*     */ 
/* 771 */     for (int ii = 0; ii < pts.length; ii++)
/*     */     {
/* 773 */       if (ii == pts.length - 1) {
/* 774 */         if (!gfa.isClosedLine().booleanValue()) break;
/* 775 */         dist = PgenResource.distanceFromLineSegment(loc, 
/* 776 */           (Coordinate)pts[ii], (Coordinate)pts[0]);
/*     */       }
/*     */       else
/*     */       {
/* 783 */         dist = PgenResource.distanceFromLineSegment(loc, (Coordinate)pts[ii], 
/* 784 */           (Coordinate)pts[(ii + 1)]);
/*     */       }
/*     */ 
/* 787 */       if (dist < minDist) {
/* 788 */         minDist = dist;
/*     */       }
/*     */     }
/*     */ 
/* 792 */     return minDist;
/*     */   }
/*     */ 
/*     */   private static void trackOtlkIssueTypeToAirmet(List<Gfa> all)
/*     */   {
/* 809 */     for (Gfa gg : all) {
/* 810 */       gg.setGfaValue("ISSUE_TYPE_FROM_OUTLOOK", "NRML");
/* 811 */       if ((gg.isAirmet()) && ("NRML".equalsIgnoreCase(gg.getGfaIssueType()))) {
/* 812 */         String akey = gg.getGfaHazard() + gg.getGfaTag() + gg.getGfaDesk();
/* 813 */         for (Gfa gotlk : all)
/* 814 */           if ((gotlk.isOutlook()) && (!"NRML".equalsIgnoreCase(gotlk.getGfaIssueType()))) {
/* 815 */             String okey = gotlk.getGfaHazard() + gotlk.getGfaTag() + gotlk.getGfaDesk();
/* 816 */             if (okey.equals(akey)) {
/* 817 */               gg.setGfaValue("ISSUE_TYPE_FROM_OUTLOOK", gotlk.getGfaIssueType());
/* 818 */               break;
/*     */             }
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaGenerate
 * JD-Core Version:    0.6.2
 */