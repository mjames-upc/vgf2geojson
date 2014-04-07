/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ITrack;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector.VectorType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Cloud;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Turbulence;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.ITcm;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.TcmFcst;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Ccfp;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.ConvSigmet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.ICcfp;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.ISigmet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.IVaaCloud;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VolcanoAshCloud;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.TCAElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DrawableElementFactory
/*     */ {
/*     */   public AbstractDrawableComponent create(DrawableType typeName, IAttribute attr, String pgenCategory, String pgenType, ArrayList<Coordinate> locations, AbstractDrawableComponent parent)
/*     */   {
/*  77 */     AbstractDrawableComponent de = null;
/*     */ 
/*  79 */     switch (typeName)
/*     */     {
/*     */     case ANY:
/*  82 */       de = new Line();
/*  83 */       Line ln = (Line)de;
/*  84 */       ln.setLinePoints(locations);
/*  85 */       break;
/*     */     case ARC:
/*  88 */       de = new Symbol();
/*  89 */       Symbol sbl = (Symbol)de;
/*  90 */       sbl.setLocation((Coordinate)locations.get(0));
/*  91 */       break;
/*     */     case AVN_TEXT:
/*  94 */       de = new KinkLine();
/*  95 */       break;
/*     */     case COMBO_SYMBOL:
/*  98 */       de = new Text();
/*  99 */       Text txt = (Text)de;
/* 100 */       txt.setLocation((Coordinate)locations.get(0));
/* 101 */       break;
/*     */     case CONTOURS:
/* 104 */       de = new AvnText();
/* 105 */       AvnText avntxt = (AvnText)de;
/* 106 */       avntxt.setLocation((Coordinate)locations.get(0));
/* 107 */       break;
/*     */     case CONV_SIGMET:
/* 110 */       de = new MidCloudText();
/* 111 */       MidCloudText cldtxt = (MidCloudText)de;
/* 112 */       cldtxt.setLocation((Coordinate)locations.get(0));
/* 113 */       break;
/*     */     case GFA:
/* 116 */       de = new Arc();
/* 117 */       Arc arc = (Arc)de;
/* 118 */       arc.setLinePoints(locations);
/* 119 */       break;
/*     */     case KINKLINE:
/* 122 */       de = new Vector();
/* 123 */       Vector vec = (Vector)de;
/* 124 */       vec.setLocation((Coordinate)locations.get(0));
/*     */ 
/* 126 */       vec.setDirectionOnly(false);
/*     */ 
/* 128 */       if (pgenType.equalsIgnoreCase("Arrow")) {
/* 129 */         vec.setVectorType(IVector.VectorType.ARROW);
/*     */       }
/* 131 */       else if (pgenType.equalsIgnoreCase("Barb")) {
/* 132 */         vec.setVectorType(IVector.VectorType.WIND_BARB);
/*     */       }
/* 134 */       else if (pgenType.equalsIgnoreCase("Directional")) {
/* 135 */         vec.setVectorType(IVector.VectorType.ARROW);
/* 136 */         vec.setDirectionOnly(true);
/*     */       }
/* 138 */       else if (pgenType.equalsIgnoreCase("Hash")) {
/* 139 */         vec.setVectorType(IVector.VectorType.HASH_MARK);
/*     */       }
/*     */       else {
/* 142 */         vec.setVectorType(IVector.VectorType.ARROW);
/*     */       }
/*     */ 
/* 145 */       break;
/*     */     case JET:
/* 148 */       de = new Track();
/* 149 */       Track track = (Track)de;
/*     */ 
/* 151 */       if ((attr instanceof ITrack)) {
/* 152 */         track.initializeTrackByTrackAttrDlgAndLocationList((ITrack)attr, locations);
/*     */       }
/*     */ 
/* 155 */       break;
/*     */     case MID_CLOUD_TEXT:
/* 158 */       de = new ComboSymbol();
/* 159 */       ComboSymbol combo = (ComboSymbol)de;
/* 160 */       combo.setLocation((Coordinate)locations.get(0));
/* 161 */       break;
/*     */     case SIGMET:
/* 164 */       de = new Jet(attr, locations);
/* 165 */       break;
/*     */     case SPENES:
/* 167 */       de = new TCAElement();
/* 168 */       break;
/*     */     case TCM_FCST:
/* 171 */       de = new Contours();
/* 172 */       break;
/*     */     case TEXT:
/* 175 */       de = new Sigmet();
/* 176 */       Sigmet sgm = (Sigmet)de;
/* 177 */       sgm.setLinePoints(locations);
/* 178 */       if ((attr instanceof ISigmet)) {
/* 179 */         sgm.setType(((ISigmet)attr).getLineType());
/* 180 */         sgm.setWidth(((ISigmet)attr).getWidth());
/*     */       }
/* 182 */       break;
/*     */     case TRACK:
/* 185 */       de = new ConvSigmet();
/* 186 */       ConvSigmet csgm = (ConvSigmet)de;
/* 187 */       csgm.setLinePoints(locations);
/*     */ 
/* 189 */       if ((pgenType.equals("CCFP_SIGMET")) && ((attr instanceof ICcfp)))
/*     */       {
/* 191 */         ((ICcfp)attr).copyEditableAttrToAbstractSigmet(csgm);
/*     */       }
/*     */       else
/*     */       {
/* 195 */         csgm.setType(((ISigmet)attr).getLineType());
/* 196 */         csgm.setWidth(((ISigmet)attr).getWidth());
/* 197 */       }break;
/*     */     case VAA:
/* 200 */       de = new Volcano();
/* 201 */       Volcano volc = (Volcano)de;
/* 202 */       volc.setLinePoints(locations);
/* 203 */       break;
/*     */     case VAA_CLOUD:
/* 206 */       de = new VolcanoAshCloud();
/* 207 */       VolcanoAshCloud vCloud = (VolcanoAshCloud)de;
/* 208 */       vCloud.setLinePoints(locations);
/* 209 */       if ((attr instanceof IVaaCloud)) {
/* 210 */         vCloud.setType(((IVaaCloud)attr).getLineType());
/* 211 */         vCloud.setWidth(((IVaaCloud)attr).getWidth());
/* 212 */         vCloud.setEditableAttrFreeText(((IVaaCloud)attr).getFhrFlDirSpdTxt());
/*     */       }
/* 214 */       break;
/*     */     case SYMBOL:
/* 217 */       de = new Gfa(attr, locations);
/* 218 */       break;
/*     */     case VECTOR:
/* 221 */       de = new TcmFcst((Coordinate)locations.get(0), ((ITcm)attr).getFcstHr(), ((ITcm)attr).getWindRadius());
/* 222 */       break;
/*     */     case WATCH_BOX:
/* 225 */       de = new Spenes();
/* 226 */       ((Spenes)de).setLinePoints(locations);
/* 227 */       break;
/*     */     case LINE:
/*     */     case TCA:
/*     */     }
/*     */ 
/* 237 */     de.setPgenCategory(pgenCategory);
/* 238 */     de.setPgenType(pgenType);
/* 239 */     de.setParent(parent);
/*     */ 
/* 241 */     if ((de != null) && ((de instanceof DrawableElement)) && 
/* 242 */       (attr != null)) {
/* 243 */       ((DrawableElement)de).update(attr);
/*     */     }
/*     */ 
/* 250 */     return de;
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent create(DrawableType typeName, IAttribute attr, String pgenCategory, String pgenType, Coordinate location, AbstractDrawableComponent parent)
/*     */   {
/* 257 */     ArrayList locations = new ArrayList();
/* 258 */     locations.add(location);
/*     */ 
/* 260 */     return create(typeName, attr, pgenCategory, pgenType, locations, parent);
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent create(DrawableType typeName, IAttribute attr, String pgenCategory, String pgenType, Coordinate[] points, AbstractDrawableComponent parent)
/*     */   {
/* 268 */     ArrayList locations = new ArrayList();
/*     */ 
/* 270 */     for (int ii = 0; ii < points.length; ii++) {
/* 271 */       locations.add(points[ii]);
/*     */     }
/*     */ 
/* 274 */     return create(typeName, attr, pgenCategory, pgenType, locations, parent);
/*     */   }
/*     */ 
/*     */   public Outlook createOutlook(String otlkType, AbstractDrawableComponent child, DECollection dec, Outlook otlk)
/*     */   {
/* 295 */     Outlook newOtlk = null;
/* 296 */     if (otlk == null) {
/* 297 */       newOtlk = new Outlook("Outlook");
/* 298 */       newOtlk.setOutlookType(otlkType);
/* 299 */       newOtlk.setPgenCategory("MET");
/* 300 */       newOtlk.setPgenType(otlkType);
/*     */     }
/*     */     else {
/* 303 */       newOtlk = otlk.copy();
/*     */     }
/*     */ 
/* 306 */     if ((dec != null) && (child != null)) {
/* 307 */       dec.add(child);
/* 308 */       newOtlk.add(dec);
/*     */     }
/*     */ 
/* 311 */     return newOtlk;
/*     */   }
/*     */ 
/*     */   public DECollection createWatchBox(String pgenCategory, String pgenType, WatchBox.WatchShape ws, Coordinate pt0, Coordinate pt1, ArrayList<Station> anchorsInPoly, IAttribute attr)
/*     */   {
/* 331 */     ArrayList watchPts = null;
/* 332 */     Station anchor1 = WatchBox.getNearestAnchorPt(pt0, anchorsInPoly);
/* 333 */     Station anchor2 = WatchBox.getNearestAnchorPt(pt1, anchorsInPoly);
/*     */ 
/* 335 */     if ((anchor1 != null) && (anchor2 != null))
/*     */     {
/* 337 */       watchPts = WatchBox.generateWatchBoxPts(ws, 96560.3984375D, 
/* 338 */         WatchBox.snapOnAnchor(anchor1, pt0), 
/* 339 */         WatchBox.snapOnAnchor(anchor2, pt1));
/*     */     }
/*     */ 
/* 346 */     if (watchPts != null)
/*     */     {
/* 349 */       WatchBox watchBox = new WatchBox();
/*     */ 
/* 351 */       watchBox.setLinePoints(watchPts);
/* 352 */       watchBox.setAnchors(anchor1, anchor2);
/* 353 */       watchBox.update(attr);
/*     */ 
/* 356 */       watchBox.setPgenCategory(pgenCategory);
/* 357 */       watchBox.setPgenType(pgenType);
/*     */ 
/* 360 */       DECollection dec = new DECollection("Watch");
/* 361 */       dec.setPgenType("WatchBox");
/* 362 */       dec.setPgenCategory("MET");
/* 363 */       dec.add(watchBox);
/*     */ 
/* 365 */       return dec;
/*     */     }
/*     */ 
/* 368 */     return null;
/*     */   }
/*     */ 
/*     */   public LabeledLine createLabeledLine(String pgenCat, String pgentype, IAttribute attrDlg, List<Coordinate> points, LabeledLine ll, DECollection parent)
/*     */   {
/* 389 */     Line ln = new Line();
/* 390 */     ln.update(attrDlg);
/* 391 */     ln.setLinePoints(points);
/* 392 */     ln.setPgenCategory("Lines");
/*     */ 
/* 394 */     if (pgentype.equalsIgnoreCase("Cloud"))
/*     */     {
/* 396 */       ln.setPgenType("SCALLOPED");
/* 397 */       if ((ll == null) || (!(ll instanceof Cloud))) {
/* 398 */         ll = new Cloud("Cloud");
/* 399 */         ll.setPgenCategory(pgenCat);
/* 400 */         ll.setPgenType(pgentype);
/* 401 */         ll.setParent(parent);
/*     */       }
/*     */     }
/* 404 */     else if (pgentype.equalsIgnoreCase("Turbulence"))
/*     */     {
/* 406 */       ln.setPgenType("LINE_DASHED_4");
/*     */ 
/* 408 */       if ((ll == null) || (!(ll instanceof Turbulence))) {
/* 409 */         ll = new Turbulence("Turbulence");
/* 410 */         ll.setPgenCategory(pgenCat);
/* 411 */         ll.setPgenType(pgentype);
/* 412 */         ll.setParent(parent);
/*     */       }
/* 414 */     } else if ("CCFP_SIGMET".equalsIgnoreCase(pgentype))
/*     */     {
/* 416 */       Sigmet sig = new Sigmet();
/*     */ 
/* 418 */       if ((attrDlg instanceof ICcfp)) {
/* 419 */         sig.setType(((ICcfp)attrDlg).getCcfpLineType());
/*     */ 
/* 421 */         ln.setPgenType(((ICcfp)attrDlg).getCcfpLineType());
/* 422 */         ln.setClosed(Boolean.valueOf(((ICcfp)attrDlg).isAreaType()));
/* 423 */         ln.setFilled(Boolean.valueOf(((ICcfp)attrDlg).isAreaType()));
/* 424 */         if (!((ICcfp)attrDlg).isAreaType())
/* 425 */           ln.setLineWidth(3.0F);
/*     */         else {
/* 427 */           ln.setLineWidth(2.0F);
/*     */         }
/*     */       }
/* 430 */       if ((ll == null) || (!(ll instanceof Ccfp))) {
/* 431 */         ll = new Ccfp("CCFP_SIGMET");
/* 432 */         ll.setPgenCategory(pgenCat);
/* 433 */         ll.setPgenType(pgentype);
/* 434 */         ll.setParent(parent);
/*     */       }
/* 436 */       ((Ccfp)ll).setSigmet(sig);
/* 437 */       ((Ccfp)ll).setAreaLine(ln);
/* 438 */       ((Ccfp)ll).setAttributes(attrDlg);
/*     */     }
/*     */ 
/* 441 */     ll.addLine(ln);
/* 442 */     return ll;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory
 * JD-Core Version:    0.6.2
 */