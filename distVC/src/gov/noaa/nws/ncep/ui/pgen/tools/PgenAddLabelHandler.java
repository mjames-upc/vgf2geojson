/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.CloudAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.MidLevelCloudAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TurbAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAvnText.AviationTextType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AvnText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Label;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Ccfp;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.CcfpInfo;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class PgenAddLabelHandler extends InputHandlerDefaultImpl
/*     */ {
/*     */   private AbstractEditor mapEditor;
/*     */   private PgenResource drawingLayer;
/*     */   private ILabeledLine prevTool;
/*     */   private AttrDlg dlg;
/*     */   private Line lineSelected;
/*     */   private Label ghostLabel;
/*     */   private List<Coordinate> pts;
/*     */ 
/*     */   public PgenAddLabelHandler(AbstractEditor mapEditor, PgenResource drawingLayer, ILabeledLine prevTool, AttrDlg dlg)
/*     */   {
/*  92 */     this.mapEditor = mapEditor;
/*  93 */     this.drawingLayer = drawingLayer;
/*  94 */     this.prevTool = prevTool;
/*  95 */     this.dlg = dlg;
/*  96 */     this.pts = new ArrayList();
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/* 108 */     if (!this.drawingLayer.isEditable()) return false;
/*     */ 
/* 111 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/* 112 */     if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 114 */     if (button == 1)
/*     */     {
/* 116 */       if (this.lineSelected != null)
/*     */       {
/* 118 */         this.pts.add(loc);
/*     */ 
/* 120 */         if ((!this.prevTool.getLabeledLine().getName().contains("CCFP_SIGMET")) && 
/* 121 */           (inPoly(loc, this.lineSelected))) {
/* 122 */           addLabel(loc, (LabeledLine)this.lineSelected.getParent());
/* 123 */           this.pts.clear();
/*     */         }
/*     */       }
/*     */       else {
/* 127 */         LabeledLine ll = this.prevTool.getLabeledLine();
/* 128 */         AbstractDrawableComponent nearestComp = this.drawingLayer.getNearestComponent(loc);
/*     */ 
/* 130 */         if ((nearestComp != null) && (nearestComp.getPgenType().equalsIgnoreCase(ll.getPgenType())))
/*     */         {
/* 132 */           Iterator it = ((LabeledLine)nearestComp).getComponentIterator();
/* 133 */           double minDist = 1.7976931348623157E+308D;
/*     */ 
/* 135 */           while (it.hasNext()) {
/* 136 */             AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 137 */             if ((adc instanceof Line)) {
/* 138 */               Line ln = (Line)adc;
/* 139 */               double dist = 1.7976931348623157E+308D;
/*     */ 
/* 141 */               Object[] pts = adc.getPoints().toArray();
/*     */ 
/* 143 */               for (int ii = 0; ii < pts.length; ii++)
/*     */               {
/* 145 */                 if (ii == pts.length - 1) {
/* 146 */                   if (!ln.isClosedLine().booleanValue()) break;
/* 147 */                   dist = PgenResource.distanceFromLineSegment(loc, (Coordinate)pts[ii], (Coordinate)pts[0]);
/*     */                 }
/*     */                 else
/*     */                 {
/* 154 */                   dist = PgenResource.distanceFromLineSegment(loc, (Coordinate)pts[ii], (Coordinate)pts[(ii + 1)]);
/*     */                 }
/*     */ 
/* 157 */                 if (dist < minDist)
/*     */                 {
/* 159 */                   minDist = dist;
/* 160 */                   this.lineSelected = ln;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 170 */       return true;
/*     */     }
/*     */ 
/* 173 */     if (button == 3)
/*     */     {
/* 175 */       if (this.ghostLabel != null)
/*     */       {
/* 177 */         if (this.prevTool.getLabeledLine().getName().contains("CCFP_SIGMET")) {
/* 178 */           addCcfpLabel(loc, this.prevTool.getLabeledLine());
/* 179 */           cleanUp();
/* 180 */           return true;
/*     */         }
/*     */ 
/* 184 */         if (!this.pts.isEmpty())
/*     */         {
/* 186 */           addLabel((Coordinate)this.pts.get(this.pts.size() - 1), (LabeledLine)this.lineSelected.getParent());
/*     */         }
/*     */         else
/*     */         {
/* 190 */           this.lineSelected = null;
/* 191 */           this.ghostLabel = null;
/* 192 */           this.drawingLayer.removeGhostLine();
/* 193 */           this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 196 */         this.pts.clear();
/*     */       }
/*     */       else
/*     */       {
/* 200 */         this.lineSelected = null;
/* 201 */         this.drawingLayer.removeGhostLine();
/* 202 */         this.mapEditor.refresh();
/*     */ 
/* 204 */         this.prevTool.resetMouseHandler();
/* 205 */         this.dlg.resetLabeledLineBtns();
/*     */       }
/*     */ 
/* 208 */       return true;
/*     */     }
/*     */ 
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */   private void addLabel(Coordinate loc, LabeledLine ll)
/*     */   {
/* 221 */     LabeledLine newll = ll.copy();
/* 222 */     Label lbl = createLabel(loc, newll, this.lineSelected);
/* 223 */     newll.addLabel(lbl);
/*     */ 
/* 226 */     LabeledLine mergedLine = null;
/* 227 */     mergedLine = PgenUtil.mergeLabels(newll, lbl, lbl.getSpe().getLocation(), this.mapEditor, this.drawingLayer);
/*     */ 
/* 230 */     if (ll.getName().equalsIgnoreCase("Cloud")) {
/* 231 */       AttrSettings.getInstance().setSettings(lbl.getSpe());
/*     */     }
/*     */ 
/* 234 */     if (mergedLine != null)
/*     */     {
/* 236 */       ArrayList old = new ArrayList();
/* 237 */       old.add(mergedLine);
/* 238 */       old.add(ll);
/*     */ 
/* 240 */       ArrayList newLines = new ArrayList();
/* 241 */       newLines.add(newll);
/*     */ 
/* 243 */       this.drawingLayer.replaceElements(old, newLines);
/*     */     }
/*     */     else {
/* 246 */       this.drawingLayer.replaceElement(ll, newll);
/*     */     }
/*     */ 
/* 249 */     this.prevTool.setLabeledLine(newll);
/*     */ 
/* 251 */     this.lineSelected = null;
/*     */ 
/* 253 */     if (this.drawingLayer.getSelectedDE() != null) {
/* 254 */       resetSelected(newll);
/*     */     }
/*     */ 
/* 257 */     this.ghostLabel = null;
/* 258 */     this.drawingLayer.removeGhostLine();
/* 259 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   public boolean handleMouseMove(int x, int y)
/*     */   {
/* 271 */     if (!this.drawingLayer.isEditable()) return false;
/*     */ 
/* 273 */     LabeledLine l = this.prevTool.getLabeledLine();
/* 274 */     if ((l != null) && ((l instanceof Ccfp))) {
/* 275 */       return handleCcfpMouseMove(x, y);
/*     */     }
/* 277 */     if (this.lineSelected != null)
/*     */     {
/* 279 */       Coordinate loc = this.mapEditor.translateClick(x, y);
/* 280 */       if (loc == null) return false;
/*     */ 
/* 283 */       LabeledLine ll = (LabeledLine)this.lineSelected.getParent();
/* 284 */       if (ll == null) return false;
/*     */ 
/* 287 */       this.ghostLabel = createLabel(loc, ll, this.lineSelected);
/* 288 */       this.drawingLayer.setGhostLine(this.ghostLabel);
/* 289 */       this.mapEditor.refresh();
/*     */     }
/*     */ 
/* 292 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */   {
/* 298 */     if ((!this.drawingLayer.isEditable()) || (this.shiftDown)) return false;
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */   private Label createLabel(Coordinate loc, LabeledLine ll, Line ln)
/*     */   {
/* 308 */     if (ll == null) return null;
/*     */ 
/* 311 */     Label lbl = null;
/* 312 */     SinglePointElement spe = null;
/*     */ 
/* 315 */     if (ll.getName().equalsIgnoreCase("Cloud")) {
/* 316 */       DrawableElementFactory def = new DrawableElementFactory();
/* 317 */       MidCloudText mtxt = (MidCloudText)def.create(DrawableType.MID_CLOUD_TEXT, ((CloudAttrDlg)this.dlg).getLabelDlg(), 
/* 318 */         "Text", "MID_LEVEL_CLOUD", loc, this.drawingLayer.getActiveLayer());
/*     */ 
/* 320 */       mtxt.setTwoColumns(false);
/*     */ 
/* 322 */       spe = mtxt;
/*     */ 
/* 324 */       lbl = new Label(mtxt);
/* 325 */       lbl.setParent(ll);
/*     */     }
/* 374 */     else if (ll.getName().equalsIgnoreCase("Turbulence")) {
/* 375 */       AvnText avntxt = new AvnText();
/* 376 */       spe = avntxt;
/*     */ 
/* 378 */       avntxt.setLocation(loc);
/* 379 */       avntxt.setParent(lbl);
/* 380 */       avntxt.setPgenCategory("Text");
/* 381 */       avntxt.setPgenType("AVIATION_TEXT");
/* 382 */       avntxt.setAvnTextType(IAvnText.AviationTextType.HIGH_LEVEL_TURBULENCE);
/*     */ 
/* 384 */       avntxt.setSymbolPatternName(((TurbAttrDlg)this.dlg).getSymbolPatternName());
/* 385 */       avntxt.setMask(Boolean.valueOf(true));
/* 386 */       avntxt.setDisplayType(IText.DisplayType.NORMAL);
/* 387 */       avntxt.setFontSize(14.0F);
/* 388 */       avntxt.setFontName("Courier");
/* 389 */       avntxt.setStyle(IText.FontStyle.REGULAR);
/* 390 */       avntxt.setJustification(IText.TextJustification.CENTER);
/* 391 */       avntxt.setText(new String[] { "" });
/*     */ 
/* 393 */       avntxt.setColors(this.dlg.getColors());
/* 394 */       avntxt.setTopValue(((TurbAttrDlg)this.dlg).getTopValue());
/* 395 */       avntxt.setBottomValue(((TurbAttrDlg)this.dlg).getBottomValue());
/*     */ 
/* 397 */       lbl = new Label(avntxt);
/* 398 */       lbl.setParent(ll);
/*     */     }
/* 400 */     else if (ll.getName().contains("CCFP_SIGMET")) {
/* 401 */       Text t = new Text();
/* 402 */       spe = t;
/* 403 */       lbl = new Label(t);
/* 404 */       lbl.setParent(ll);
/* 405 */       setCcfpText(loc, ll, ln, t, lbl);
/* 406 */       return lbl;
/*     */     }
/*     */ 
/* 409 */     if (spe != null)
/*     */     {
/* 411 */       int[] xpoints = new int[ln.getPoints().size()];
/* 412 */       int[] ypoints = new int[ln.getPoints().size()];
/* 413 */       for (int ii = 0; ii < ln.getPoints().size(); ii++) {
/* 414 */         double[] pt = this.mapEditor.translateInverseClick((Coordinate)ln.getPoints().get(ii));
/* 415 */         xpoints[ii] = ((int)pt[0]);
/* 416 */         ypoints[ii] = ((int)pt[1]);
/*     */       }
/*     */ 
/* 419 */       java.awt.Polygon poly = new java.awt.Polygon(xpoints, ypoints, ln.getPoints().size());
/* 420 */       double[] txtLoc = this.mapEditor.translateInverseClick(spe.getLocation());
/*     */ 
/* 422 */       if (!poly.contains(txtLoc[0], txtLoc[1]))
/*     */       {
/* 425 */         ArrayList locs = new ArrayList();
/* 426 */         locs.add(spe.getLocation());
/*     */ 
/* 430 */         if (!this.pts.isEmpty()) {
/* 431 */           int idx = this.pts.size() - 1;
/* 432 */           if (locs.get(0) == this.pts.get(idx)) {
/* 433 */             idx--;
/*     */           }
/* 435 */           for (int ii = idx; ii >= 0; ii--) {
/* 436 */             locs.add((Coordinate)this.pts.get(ii));
/*     */           }
/*     */         }
/*     */ 
/* 440 */         Coordinate scnCenter = getScreenCentroid(ln);
/* 441 */         locs.add(this.mapEditor.translateClick(scnCenter.x, scnCenter.y));
/*     */ 
/* 443 */         Color[] arrowColors = new Color[2];
/* 444 */         if (ll.getName().equalsIgnoreCase("Cloud")) {
/* 445 */           arrowColors = ((CloudAttrDlg)this.dlg).getLabelDlg().getColors();
/*     */         }
/*     */         else {
/* 448 */           arrowColors = this.dlg.getColors();
/*     */         }
/*     */ 
/* 451 */         Line arrowLn = new Line(null, arrowColors, 1.0F, 1.0D, 
/* 452 */           false, false, locs, 0, FillPatternList.FillPattern.SOLID, "Lines", "POINTED_ARROW");
/*     */ 
/* 454 */         lbl.addArrow(arrowLn);
/*     */       }
/*     */     }
/*     */ 
/* 458 */     return lbl;
/*     */   }
/*     */ 
/*     */   private void resetSelected(LabeledLine labeledLine)
/*     */   {
/* 467 */     if (labeledLine != null) {
/* 468 */       this.drawingLayer.removeSelected();
/* 469 */       Iterator it = labeledLine.createDEIterator();
/* 470 */       while (it.hasNext())
/* 471 */         this.drawingLayer.addSelected((AbstractDrawableComponent)it.next());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setCcfpText(Coordinate loc, LabeledLine ll, Line ln, Text t, Label lbl)
/*     */   {
/* 477 */     t.setLocation(loc);
/* 478 */     t.setParent(lbl);
/* 479 */     t.setPgenCategory("Text");
/* 480 */     t.setPgenType("AVIATION_TEXT");
/*     */ 
/* 482 */     t.setMask(Boolean.valueOf(true));
/* 483 */     t.setDisplayType(IText.DisplayType.BOX);
/* 484 */     t.setFontSize(14.0F);
/* 485 */     t.setFontName("Courier");
/* 486 */     t.setStyle(IText.FontStyle.REGULAR);
/* 487 */     t.setJustification(IText.TextJustification.CENTER);
/*     */ 
/* 489 */     t.setText(CcfpInfo.getCcfpTxt2(((Ccfp)ll).getSigmet()));
/* 490 */     t.setRotationRelativity(IText.TextRotation.SCREEN_RELATIVE);
/* 491 */     t.setColors(this.dlg.getColors());
/*     */ 
/* 493 */     ArrayList locs = new ArrayList();
/* 494 */     locs.add(loc);
/*     */ 
/* 496 */     if (!this.pts.isEmpty()) {
/* 497 */       for (int ii = this.pts.size() - 1; ii >= 0; ii--) {
/* 498 */         locs.add((Coordinate)this.pts.get(ii));
/*     */       }
/*     */     }
/*     */ 
/* 502 */     if (!isPtsInArea(((Ccfp)ll).getAreaLine(), loc))
/*     */     {
/* 504 */       locs.add(((Ccfp)ll).getAreaLine().getCentroid());
/*     */ 
/* 506 */       Line arrowLn = new Line(null, new Color[] { Color.WHITE }, 2.0F, 1.0D, 
/* 507 */         false, false, locs, 0, FillPatternList.FillPattern.FILL_PATTERN_2, "Lines", "POINTED_ARROW");
/* 508 */       lbl.addArrow(arrowLn);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean handleCcfpMouseMove(int x, int y)
/*     */   {
/* 515 */     Coordinate loc = this.mapEditor.translateClick(x, y);
/* 516 */     if (loc == null) return false;
/*     */ 
/* 518 */     LabeledLine ll = this.prevTool.getLabeledLine();
/* 519 */     if (ll == null) return false;
/*     */ 
/* 521 */     Ccfp cc = (Ccfp)ll;
/*     */ 
/* 523 */     Line areaLine = cc.getAreaLine();
/* 524 */     if (areaLine == null) return false;
/*     */ 
/* 527 */     this.ghostLabel = createLabel(loc, ll, this.lineSelected);
/*     */ 
/* 529 */     this.drawingLayer.setGhostLine(this.ghostLabel);
/* 530 */     this.mapEditor.refresh();
/*     */ 
/* 532 */     return true;
/*     */   }
/*     */ 
/*     */   private void addCcfpLabel(Coordinate loc, LabeledLine ll)
/*     */   {
/* 540 */     LabeledLine newll = ll.copy();
/* 541 */     Label lbl = createLabel(loc, newll, ((Ccfp)ll).getAreaLine());
/* 542 */     newll.addLabel(lbl);
/*     */ 
/* 544 */     this.drawingLayer.replaceElement(ll, newll);
/* 545 */     this.prevTool.setLabeledLine(newll);
/*     */ 
/* 547 */     if (this.drawingLayer.getSelectedDE() != null) {
/* 548 */       resetSelected(newll);
/*     */     }
/*     */ 
/* 551 */     this.ghostLabel = null;
/* 552 */     this.drawingLayer.removeGhostLine();
/* 553 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   private boolean inPoly(Coordinate loc, Line ln)
/*     */   {
/* 558 */     int[] xpoints = new int[ln.getPoints().size()];
/* 559 */     int[] ypoints = new int[ln.getPoints().size()];
/* 560 */     for (int ii = 0; ii < ln.getPoints().size(); ii++) {
/* 561 */       double[] pt = this.mapEditor.translateInverseClick((Coordinate)ln.getPoints().get(ii));
/* 562 */       xpoints[ii] = ((int)pt[0]);
/* 563 */       ypoints[ii] = ((int)pt[1]);
/*     */     }
/*     */ 
/* 566 */     java.awt.Polygon poly = new java.awt.Polygon(xpoints, ypoints, ln.getPoints().size());
/*     */ 
/* 568 */     double[] scnLoc = this.mapEditor.translateInverseClick(loc);
/*     */ 
/* 570 */     return poly.contains(scnLoc[0], scnLoc[1]);
/*     */   }
/*     */ 
/*     */   private void cleanUp() {
/* 574 */     this.lineSelected = null;
/* 575 */     this.drawingLayer.removeGhostLine();
/* 576 */     this.mapEditor.refresh();
/*     */ 
/* 578 */     this.prevTool.resetMouseHandler();
/*     */   }
/*     */ 
/*     */   public static boolean isPtsInArea(Line l, Coordinate pts)
/*     */   {
/* 583 */     Coordinate[] c = new Coordinate[l.getLinePoints().length + 1];
/* 584 */     c = (Coordinate[])Arrays.copyOf(l.getLinePoints(), c.length);
/* 585 */     c[(c.length - 1)] = c[0];
/*     */ 
/* 587 */     if (c.length < 4) return false;
/*     */ 
/* 589 */     GeometryFactory f = new GeometryFactory();
/*     */ 
/* 591 */     return f.createPolygon(f.createLinearRing(c), null).contains(new GeometryFactory().createPoint(pts));
/*     */   }
/*     */ 
/*     */   private Coordinate getScreenCentroid(Line ln) {
/* 595 */     GeometryFactory factory = new GeometryFactory();
/* 596 */     Coordinate[] ptArray = new Coordinate[ln.getPoints().size() + 1];
/*     */ 
/* 598 */     for (int ii = 0; ii < ln.getPoints().size(); ii++) {
/* 599 */       double[] xy = this.mapEditor.translateInverseClick((Coordinate)ln.getPoints().get(ii));
/* 600 */       ptArray[ii] = new Coordinate(xy[0], xy[1]);
/*     */     }
/*     */ 
/* 603 */     ptArray[(ptArray.length - 1)] = ptArray[0];
/* 604 */     LineString g = factory.createLineString(ptArray);
/* 605 */     Point p = g.getCentroid();
/* 606 */     return p.getCoordinate();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenAddLabelHandler
 * JD-Core Version:    0.6.2
 */