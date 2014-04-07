/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlgFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.FrontAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.PgenDistanceDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.PgenDistanceDlg.DistanceDisplayProperties;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.SigmetAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.SigmetCommAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TrackAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TrackExtrapPointInfoDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.CcfpAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.VaaCloudDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Track;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.CcfpInfo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.ConvSigmet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*     */ import gov.noaa.nws.ncep.viz.common.LocatorUtil;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*     */ 
/*     */ public class PgenMultiPointDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   private TrackExtrapPointInfoDlg trackExtrapPointInfoDlg;
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/*  94 */     super.deactivateTool();
/*     */ 
/*  96 */     if (this.trackExtrapPointInfoDlg != null) {
/*  97 */       this.trackExtrapPointInfoDlg.close();
/*     */     }
/*  99 */     if ((this.mouseHandler instanceof PgenMultiPointDrawingHandler)) {
/* 100 */       PgenMultiPointDrawingHandler mph = (PgenMultiPointDrawingHandler)this.mouseHandler;
/* 101 */       if (mph != null) mph.clearPoints();
/*     */     }
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 111 */     if (this.mouseHandler == null)
/*     */     {
/* 113 */       this.mouseHandler = new PgenMultiPointDrawingHandler();
/*     */     }
/*     */ 
/* 117 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   private void displayTrackExtrapPointInfoDlg(TrackAttrDlg trackAttrDlgObject, Track trackObject)
/*     */   {
/* 524 */     if (trackAttrDlgObject == null)
/* 525 */       return;
/* 526 */     TrackExtrapPointInfoDlg extrapPointInfoDlg = trackAttrDlgObject.getTrackExtrapPointInfoDlg();
/* 527 */     if (extrapPointInfoDlg != null) {
/* 528 */       extrapPointInfoDlg.close();
/*     */     } else {
/* 530 */       extrapPointInfoDlg = (TrackExtrapPointInfoDlg)AttrDlgFactory.createAttrDlg("TRACK_EXTRA_POINTS_INFO", 
/* 531 */         this.pgenType, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/* 532 */       this.trackExtrapPointInfoDlg = extrapPointInfoDlg;
/* 533 */       trackAttrDlgObject.setTrackExtrapPointInfoDlg(extrapPointInfoDlg);
/*     */     }
/*     */ 
/* 540 */     extrapPointInfoDlg.setBlockOnOpen(false);
/* 541 */     extrapPointInfoDlg.open();
/*     */ 
/* 543 */     extrapPointInfoDlg.setTrack(trackObject, trackAttrDlgObject.getUnitComboSelectedIndex(), 
/* 544 */       trackAttrDlgObject.getRoundComboSelectedIndex(), trackAttrDlgObject.getRoundDirComboSelectedIndex());
/*     */ 
/* 547 */     extrapPointInfoDlg.setBlockOnOpen(true);
/*     */   }
/*     */ 
/*     */   public class PgenMultiPointDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/* 132 */     protected ArrayList<Coordinate> points = new ArrayList();
/*     */     protected AbstractDrawableComponent elem;
/* 143 */     protected DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/* 148 */     boolean ccfpTxtFlag = false;
/*     */ 
/* 150 */     private GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/* 151 */     private PgenDistanceDlg.DistanceDisplayProperties distProps = PgenDistanceDlg.getInstance(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()).getDistanceProperties();
/*     */ 
/*     */     public PgenMultiPointDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 161 */       if (!PgenMultiPointDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 164 */       Coordinate loc = PgenMultiPointDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 165 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 167 */       if (button == 1) {
/* 168 */         if ("SIGMET".equalsIgnoreCase(PgenMultiPointDrawingTool.this.pgenCategory)) return handleSigmetMouseDown(loc);
/*     */ 
/* 170 */         this.points.add(loc);
/*     */ 
/* 172 */         if (isTrackElement(getDrawableType(PgenMultiPointDrawingTool.this.pgenType))) {
/* 173 */           if (this.points.size() == 1) {
/* 174 */             if (((PgenMultiPointDrawingTool.this.attrDlg instanceof TrackAttrDlg)) && 
/* 175 */               (((TrackAttrDlg)PgenMultiPointDrawingTool.this.attrDlg).getFrameTimeButton().getSelection())) {
/* 176 */               String ftime = PgenUtil.getCurrentFrameTime();
/* 177 */               if (((ftime != null ? 1 : 0) & (ftime.trim().length() > 0 ? 1 : 0)) != 0) {
/* 178 */                 ((TrackAttrDlg)PgenMultiPointDrawingTool.this.attrDlg).getFirstTimeText().setText(ftime);
/* 179 */                 ((TrackAttrDlg)PgenMultiPointDrawingTool.this.attrDlg).getSecondTimeText().setText("");
/*     */               }
/*     */             }
/*     */           }
/* 183 */           else if ((this.points.size() == 2) && 
/* 184 */             ((PgenMultiPointDrawingTool.this.attrDlg instanceof TrackAttrDlg)) && 
/* 185 */             (((TrackAttrDlg)PgenMultiPointDrawingTool.this.attrDlg).getFrameTimeButton().getSelection())) {
/* 186 */             Calendar cal = PgenUtil.getCurrentFrameCalendar();
/* 187 */             String interval = ((TrackAttrDlg)PgenMultiPointDrawingTool.this.attrDlg).getIntervalTimeString();
/* 188 */             Calendar secondCal = PgenUtil.getNextCalendar(cal, interval);
/* 189 */             String stime = PgenUtil.getFrameTime(secondCal);
/* 190 */             if ((stime != null) && (stime.trim().length() > 0)) {
/* 191 */               ((TrackAttrDlg)PgenMultiPointDrawingTool.this.attrDlg).getSecondTimeText().setText(stime);
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 197 */         return true;
/*     */       }
/*     */ 
/* 200 */       if (button == 3)
/*     */       {
/* 202 */         if (this.points.size() == 0)
/*     */         {
/* 204 */           closeAttrDlg(PgenMultiPointDrawingTool.this.attrDlg, PgenMultiPointDrawingTool.this.pgenType);
/* 205 */           PgenMultiPointDrawingTool.this.attrDlg = null;
/* 206 */           PgenUtil.setSelectingMode();
/*     */         }
/* 209 */         else if (this.points.size() < 2)
/*     */         {
/* 211 */           PgenMultiPointDrawingTool.this.drawingLayer.removeGhostLine();
/* 212 */           this.points.clear();
/*     */ 
/* 214 */           PgenMultiPointDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */         else
/*     */         {
/* 219 */           DrawableType drawableType = getDrawableType(PgenMultiPointDrawingTool.this.pgenType);
/*     */ 
/* 226 */           this.elem = this.def.create(drawableType, PgenMultiPointDrawingTool.this.attrDlg, 
/* 227 */             PgenMultiPointDrawingTool.this.pgenCategory, PgenMultiPointDrawingTool.this.pgenType, this.points, PgenMultiPointDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 229 */           PgenMultiPointDrawingTool.this.attrDlg.setDrawableElement((DrawableElement)this.elem);
/* 230 */           AttrSettings.getInstance().setSettings((DrawableElement)this.elem);
/*     */ 
/* 232 */           if ("CCFP_SIGMET".equals(PgenMultiPointDrawingTool.this.pgenType)) {
/* 233 */             this.ccfpTxtFlag = true;
/* 234 */             return true;
/*     */           }
/*     */ 
/* 237 */           if ((this.elem != null) && (this.elem.getPgenCategory().equalsIgnoreCase("Front")) && 
/* 238 */             (((FrontAttrDlg)PgenMultiPointDrawingTool.this.attrDlg).labelEnabled()))
/*     */           {
/* 240 */             DECollection dec = new DECollection("labeledFront");
/* 241 */             dec.setPgenCategory(PgenMultiPointDrawingTool.this.pgenCategory);
/* 242 */             dec.setPgenType(PgenMultiPointDrawingTool.this.pgenType);
/* 243 */             dec.addElement(this.elem);
/* 244 */             PgenMultiPointDrawingTool.this.drawingLayer.addElement(dec);
/*     */ 
/* 246 */             PgenUtil.setDrawingTextMode(true, ((FrontAttrDlg)PgenMultiPointDrawingTool.this.attrDlg).useFrontColor(), "", dec);
/* 247 */             this.elem = null;
/*     */           }
/*     */           else {
/* 250 */             PgenMultiPointDrawingTool.this.drawingLayer.addElement(this.elem);
/*     */           }
/*     */ 
/* 254 */           if (isTrackElement(drawableType)) {
/* 255 */             PgenMultiPointDrawingTool.this.displayTrackExtrapPointInfoDlg((TrackAttrDlg)PgenMultiPointDrawingTool.this.attrDlg, (Track)this.elem);
/*     */           }
/*     */ 
/* 258 */           PgenMultiPointDrawingTool.this.drawingLayer.removeGhostLine();
/*     */ 
/* 260 */           if (!this.ccfpTxtFlag) {
/* 261 */             this.points.clear();
/*     */           }
/* 263 */           PgenMultiPointDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 267 */         return true;
/*     */       }
/*     */ 
/* 270 */       if (button == 2)
/*     */       {
/* 272 */         return true;
/*     */       }
/*     */ 
/* 277 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int aX, int aY, int button)
/*     */     {
/* 285 */       if ((!PgenMultiPointDrawingTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 286 */       return true;
/*     */     }
/*     */ 
/*     */     private boolean handleSigmetMouseDown(Coordinate loc)
/*     */     {
/* 291 */       if (("CCFP_SIGMET".equals(PgenMultiPointDrawingTool.this.pgenType)) && (this.ccfpTxtFlag)) return handleCcfpMouseDown(loc);
/*     */ 
/* 293 */       this.points.add(loc);
/*     */ 
/* 295 */       if ((getSigmetLineType(PgenMultiPointDrawingTool.this.attrDlg).contains("Text")) || ("Isolated".equalsIgnoreCase(getSigmetLineType(PgenMultiPointDrawingTool.this.attrDlg))))
/*     */       {
/* 301 */         this.elem = this.def.create(getDrawableType(PgenMultiPointDrawingTool.this.pgenType), PgenMultiPointDrawingTool.this.attrDlg, 
/* 302 */           PgenMultiPointDrawingTool.this.pgenCategory, PgenMultiPointDrawingTool.this.pgenType, this.points, PgenMultiPointDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 304 */         PgenMultiPointDrawingTool.this.attrDlg.setDrawableElement((DrawableElement)this.elem);
/* 305 */         AttrSettings.getInstance().setSettings((DrawableElement)this.elem);
/*     */ 
/* 307 */         PgenMultiPointDrawingTool.this.drawingLayer.addElement(this.elem);
/* 308 */         PgenMultiPointDrawingTool.this.drawingLayer.removeGhostLine();
/* 309 */         this.points.clear();
/* 310 */         PgenMultiPointDrawingTool.this.mapEditor.refresh();
/*     */       }
/* 312 */       return true;
/*     */     }
/*     */ 
/*     */     private DrawableType getDrawableType(String pgenTypeString) {
/* 316 */       if ("STORM_TRACK".equalsIgnoreCase(pgenTypeString))
/* 317 */         return DrawableType.TRACK;
/* 318 */       if (pgenTypeString.equalsIgnoreCase("jet"))
/* 319 */         return DrawableType.JET;
/* 320 */       if ("INTL_SIGMET".equalsIgnoreCase(pgenTypeString))
/* 321 */         return DrawableType.SIGMET;
/* 322 */       if (("CONV_SIGMET".equalsIgnoreCase(pgenTypeString)) || 
/* 323 */         ("NCON_SIGMET".equalsIgnoreCase(pgenTypeString)) || 
/* 324 */         ("AIRM_SIGMET".equalsIgnoreCase(pgenTypeString)) || 
/* 325 */         ("OUTL_SIGMET".equalsIgnoreCase(pgenTypeString)) || 
/* 326 */         ("CCFP_SIGMET".equalsIgnoreCase(pgenTypeString)))
/* 327 */         return DrawableType.CONV_SIGMET;
/* 328 */       if ("VACL_SIGMET".equalsIgnoreCase(pgenTypeString))
/* 329 */         return DrawableType.VAA_CLOUD;
/* 330 */       return DrawableType.LINE;
/*     */     }
/*     */ 
/*     */     private String getSigmetLineType(AttrDlg attrDlg)
/*     */     {
/* 335 */       if (PgenMultiPointDrawingTool.this.pgenType.equalsIgnoreCase("INTL_SIGMET")) {
/* 336 */         return ((SigmetAttrDlg)attrDlg).getLineType();
/*     */       }
/*     */ 
/* 339 */       if (PgenMultiPointDrawingTool.this.pgenType.equalsIgnoreCase("CCFP_SIGMET")) {
/* 340 */         return ((CcfpAttrDlg)attrDlg).getLineType();
/*     */       }
/* 342 */       if (PgenMultiPointDrawingTool.this.pgenType.equalsIgnoreCase("VACL_SIGMET"))
/* 343 */         return ((VaaCloudDlg)attrDlg).getLineType();
/* 344 */       return ((SigmetCommAttrDlg)attrDlg).getLineType();
/*     */     }
/*     */ 
/*     */     private boolean isTrackElement(DrawableType drawableType)
/*     */     {
/* 349 */       if (drawableType == DrawableType.TRACK)
/* 350 */         return true;
/* 351 */       return false;
/*     */     }
/*     */ 
/*     */     private void closeAttrDlg(AttrDlg attrDlgObject, String pgenTypeString) {
/* 355 */       if (attrDlgObject == null)
/* 356 */         return;
/* 357 */       if (DrawableType.TRACK == getDrawableType(pgenTypeString)) {
/* 358 */         TrackAttrDlg tempTrackAttrDlg = (TrackAttrDlg)attrDlgObject;
/* 359 */         closeTrackExtrapPointInfoDlg(tempTrackAttrDlg.getTrackExtrapPointInfoDlg());
/* 360 */         tempTrackAttrDlg = null;
/*     */       }
/* 362 */       attrDlgObject.close();
/*     */     }
/*     */ 
/*     */     private void closeTrackExtrapPointInfoDlg(TrackExtrapPointInfoDlg dlgObject) {
/* 366 */       if (dlgObject != null)
/* 367 */         dlgObject.close();
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 378 */       if (!PgenMultiPointDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 381 */       Coordinate loc = PgenMultiPointDrawingTool.this.mapEditor.translateClick(x, y);
/* 382 */       if (loc == null) return false;
/*     */ 
/* 384 */       DECollection ghost = new DECollection();
/*     */ 
/* 386 */       if ("SIGMET".equalsIgnoreCase(PgenMultiPointDrawingTool.this.pgenCategory)) return handleSigmetMouseMove(loc);
/*     */ 
/* 389 */       AbstractDrawableComponent ghostline = this.def.create(DrawableType.LINE, PgenMultiPointDrawingTool.this.attrDlg, 
/* 390 */         PgenMultiPointDrawingTool.this.pgenCategory, PgenMultiPointDrawingTool.this.pgenType, this.points, PgenMultiPointDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 392 */       if ((this.points != null) && (this.points.size() >= 1))
/*     */       {
/* 394 */         ArrayList ghostPts = new ArrayList(this.points);
/* 395 */         ghostPts.add(loc);
/* 396 */         Line ln = (Line)ghostline;
/* 397 */         ln.setLinePoints(new ArrayList(ghostPts));
/* 398 */         ghost.add(ghostline);
/*     */ 
/* 403 */         if (this.distProps.displayDistance) {
/* 404 */           this.gc.setStartingGeographicPoint(loc.x, loc.y);
/* 405 */           this.gc.setDestinationGeographicPoint(((Coordinate)this.points.get(0)).x, ((Coordinate)this.points.get(0)).y);
/*     */ 
/* 407 */           double azimuth = this.gc.getAzimuth();
/* 408 */           if (azimuth < 0.0D) azimuth += 360.0D;
/* 409 */           double distanceInMeter = this.gc.getOrthodromicDistance();
/* 410 */           String distdir = createDistanceString(distanceInMeter, azimuth, this.distProps);
/* 411 */           ghost.add(new gov.noaa.nws.ncep.ui.pgen.elements.Text(null, "Courier", 18.0F, IText.TextJustification.LEFT_JUSTIFY, 
/* 412 */             loc, 0.0D, IText.TextRotation.SCREEN_RELATIVE, new String[] { distdir }, 
/* 413 */             IText.FontStyle.BOLD, Color.YELLOW, 4, 6, true, IText.DisplayType.NORMAL, "TEXT", "General Text"));
/*     */         }
/*     */ 
/* 416 */         PgenMultiPointDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 417 */         PgenMultiPointDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 421 */       return false;
/*     */     }
/*     */ 
/*     */     private String createDistanceString(double distanceInMeter, double dir, PgenDistanceDlg.DistanceDisplayProperties distProps)
/*     */     {
/* 431 */       StringBuilder sb = new StringBuilder();
/* 432 */       String distVal = LocatorUtil.distanceDisplay(distanceInMeter, 1, distProps.distanceUnits.toUpperCase());
/*     */ 
/* 434 */       sb.append(distVal);
/*     */ 
/* 436 */       sb.append(' ');
/*     */ 
/* 438 */       if (distProps.directionUnits.equalsIgnoreCase("16-pt")) {
/* 439 */         sb.append(LocatorUtil.ConvertTO16PointDir(dir));
/*     */       }
/*     */       else {
/* 442 */         sb.append(String.valueOf((int)dir));
/* 443 */         sb.append("deg");
/*     */       }
/*     */ 
/* 446 */       return sb.toString();
/*     */     }
/*     */ 
/*     */     private boolean handleSigmetMouseMove(Coordinate loc)
/*     */     {
/* 451 */       if (this.ccfpTxtFlag) return handleCcfpMouseMove(loc);
/*     */ 
/* 453 */       AbstractDrawableComponent ghost = this.def.create(getDrawableType(PgenMultiPointDrawingTool.this.pgenType), PgenMultiPointDrawingTool.this.attrDlg, 
/* 454 */         PgenMultiPointDrawingTool.this.pgenCategory, PgenMultiPointDrawingTool.this.pgenType, this.points, PgenMultiPointDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 456 */       if (("Isolated".equalsIgnoreCase(getSigmetLineType(PgenMultiPointDrawingTool.this.attrDlg))) || 
/* 457 */         (getSigmetLineType(PgenMultiPointDrawingTool.this.attrDlg).contains("Text")) || (
/* 457 */         (this.points != null) && (this.points.size() >= 1)))
/*     */       {
/* 459 */         ArrayList ghostPts = new ArrayList(this.points);
/* 460 */         ghostPts.add(loc);
/* 461 */         MultiPointElement ln = (MultiPointElement)ghost;
/* 462 */         ln.setLinePoints(new ArrayList(ghostPts));
/*     */ 
/* 464 */         PgenMultiPointDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 465 */         PgenMultiPointDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 468 */       return false;
/*     */     }
/*     */ 
/*     */     public void clearPoints() {
/* 472 */       this.points.clear();
/*     */     }
/*     */ 
/*     */     private boolean handleCcfpMouseMove(Coordinate loc)
/*     */     {
/* 477 */       ConvSigmet ccfp = (ConvSigmet)this.def.create(getDrawableType(PgenMultiPointDrawingTool.this.pgenType), PgenMultiPointDrawingTool.this.attrDlg, 
/* 478 */         PgenMultiPointDrawingTool.this.pgenCategory, PgenMultiPointDrawingTool.this.pgenType, this.points, PgenMultiPointDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 480 */       ccfp.setEditableAttrFromLine("CCFP_SIGMET");
/*     */ 
/* 482 */       double[] ad = CcfpInfo.getCcfpTxtAziDir(loc, ccfp);
/* 483 */       if (ad == null) return false;
/*     */ 
/* 485 */       ccfp.setEditableAttrFreeText(ad[0] + ":::" + ad[1]);
/*     */ 
/* 487 */       PgenMultiPointDrawingTool.this.drawingLayer.setGhostLine(ccfp);
/*     */ 
/* 489 */       PgenMultiPointDrawingTool.this.mapEditor.refresh();
/* 490 */       return false;
/*     */     }
/*     */ 
/*     */     private boolean handleCcfpMouseDown(Coordinate loc)
/*     */     {
/* 495 */       ((ConvSigmet)this.elem).setEditableAttrFromLine("CCFP_SIGMET");
/*     */ 
/* 497 */       double[] ad = CcfpInfo.getCcfpTxtAziDir(loc, (Sigmet)this.elem);
/* 498 */       if (ad == null) return false;
/*     */ 
/* 500 */       ((ConvSigmet)this.elem).setEditableAttrFreeText(ad[0] + ":::" + ad[1]);
/*     */ 
/* 502 */       this.elem = this.def.create(getDrawableType(PgenMultiPointDrawingTool.this.pgenType), PgenMultiPointDrawingTool.this.attrDlg, 
/* 503 */         PgenMultiPointDrawingTool.this.pgenCategory, PgenMultiPointDrawingTool.this.pgenType, this.points, PgenMultiPointDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 505 */       double[] ad2 = CcfpInfo.getCcfpTxtAziDir(loc, (Sigmet)this.elem);
/* 506 */       if (ad2 == null) return true;
/*     */ 
/* 508 */       ((ConvSigmet)this.elem).setEditableAttrFreeText(ad2[0] + ":::" + ad2[1]);
/* 509 */       ((ConvSigmet)this.elem).setEditableAttrFromLine("CCFP_SIGMET");
/*     */ 
/* 511 */       PgenMultiPointDrawingTool.this.drawingLayer.addElement(this.elem);
/* 512 */       PgenMultiPointDrawingTool.this.drawingLayer.removeGhostLine();
/* 513 */       this.points.clear();
/* 514 */       PgenMultiPointDrawingTool.this.mapEditor.refresh();
/*     */ 
/* 516 */       this.ccfpTxtFlag = false;
/* 517 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenMultiPointDrawingTool
 * JD-Core Version:    0.6.2
 */