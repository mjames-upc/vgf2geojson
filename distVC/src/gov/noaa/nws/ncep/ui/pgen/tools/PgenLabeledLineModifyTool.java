/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.CloudAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TurbAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.CcfpAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Cloud;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Label;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Turbulence;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Ccfp;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ 
/*     */ public class PgenLabeledLineModifyTool extends PgenSelectingTool
/*     */   implements ILabeledLine
/*     */ {
/*  57 */   Ccfp ccfp = null;
/*     */   LabeledLine labeledLine;
/*     */   DrawableElement elSelected;
/*     */   Coordinate click;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  76 */     super.activateTool();
/*     */ 
/*  78 */     this.elSelected = null;
/*  79 */     this.click = null;
/*     */ 
/*  81 */     if (this.attrDlg == null) {
/*  82 */       return;
/*     */     }
/*  84 */     if ((this.event.getTrigger() instanceof Cloud)) {
/*  85 */       this.labeledLine = ((LabeledLine)this.event.getTrigger());
/*  86 */       ((CloudAttrDlg)this.attrDlg).setCloudDrawingTool(this);
/*  87 */       ((CloudAttrDlg)this.attrDlg).resetLabeledLineBtns();
/*     */ 
/*  89 */       if (this.labeledLine != null) {
/*  90 */         ((CloudAttrDlg)this.attrDlg).setAttrForDlg(null);
/*  91 */         this.attrDlg.enableButtons();
/*     */       }
/*     */     }
/*  94 */     else if ((this.event.getTrigger() instanceof Turbulence)) {
/*  95 */       this.labeledLine = ((LabeledLine)this.event.getTrigger());
/*  96 */       ((TurbAttrDlg)this.attrDlg).setTurbDrawingTool(this);
/*  97 */       ((TurbAttrDlg)this.attrDlg).resetLabeledLineBtns();
/*     */ 
/*  99 */       if (this.labeledLine != null) {
/* 100 */         ((TurbAttrDlg)this.attrDlg).setAttrForDlg(null);
/* 101 */         this.attrDlg.enableButtons();
/*     */       }
/*     */     }
/* 104 */     else if ((this.event.getTrigger() instanceof Ccfp)) {
/* 105 */       this.labeledLine = ((LabeledLine)this.event.getTrigger());
/* 106 */       this.attrDlg.enableButtons();
/* 107 */       ((CcfpAttrDlg)this.attrDlg).setMouseHandlerName("LabeledLine Modify");
/* 108 */       this.ccfp = ((Ccfp)this.labeledLine);
/* 109 */       ((CcfpAttrDlg)this.attrDlg).setAttrForDlg(this.ccfp.getSigmet());
/* 110 */       ((CcfpAttrDlg)this.attrDlg).setCcfpDrawingTool(this);
/* 111 */       this.ccfp.setAttributes(this.attrDlg);
/*     */     }
/* 113 */     setHandler(new PgenLabeledLineModifyHandler(this, this.mapEditor, this.drawingLayer, this.attrDlg));
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 123 */     if (this.mouseHandler == null)
/*     */     {
/* 125 */       this.mouseHandler = new PgenLabeledLineModifyHandler(this, this.mapEditor, this.drawingLayer, this.attrDlg);
/*     */     }
/*     */ 
/* 129 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public void setAddingLabelHandler()
/*     */   {
/* 537 */     setHandler(new PgenAddLabelHandler(this.mapEditor, this.drawingLayer, 
/* 538 */       this, this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void resetMouseHandler()
/*     */   {
/* 545 */     this.click = null;
/* 546 */     this.elSelected = null;
/* 547 */     setHandler(new PgenLabeledLineModifyHandler(this, this.mapEditor, this.drawingLayer, this.attrDlg));
/*     */   }
/*     */ 
/*     */   public LabeledLine getLabeledLine()
/*     */   {
/* 552 */     return this.labeledLine;
/*     */   }
/*     */ 
/*     */   public void setLabeledLine(LabeledLine ln)
/*     */   {
/* 557 */     this.labeledLine = ln;
/*     */   }
/*     */ 
/*     */   private void resetSelected() {
/* 561 */     if (this.labeledLine != null) {
/* 562 */       this.drawingLayer.removeSelected();
/* 563 */       Iterator it = this.labeledLine.createDEIterator();
/* 564 */       while (it.hasNext())
/* 565 */         this.drawingLayer.addSelected((AbstractDrawableComponent)it.next());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDeleteHandler(boolean delLine, boolean flip, boolean openClose)
/*     */   {
/* 572 */     setHandler(new PgenLabeledLineDelHandler(this.mapEditor, this.drawingLayer, this, this.attrDlg, delLine, flip, openClose));
/*     */   }
/*     */ 
/*     */   private class PgenLabeledLineModifyHandler extends PgenSelectHandler
/*     */   {
/* 139 */     private ArrayList<Coordinate> points = new ArrayList();
/*     */     private boolean simulate;
/* 145 */     protected DrawableElementFactory def = new DrawableElementFactory();
/* 146 */     protected boolean ptSelected2 = false;
/*     */ 
/*     */     public PgenLabeledLineModifyHandler(AbstractPgenTool tool, AbstractEditor mapEditor, PgenResource resource, AttrDlg attrDlg)
/*     */     {
/* 157 */       super(mapEditor, resource, attrDlg);
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 168 */       if (!PgenLabeledLineModifyTool.this.isResourceEditable()) return false;
/*     */ 
/* 171 */       Coordinate loc = this.mapEditor.translateClick(anX, aY);
/* 172 */       if ((loc == null) || (this.shiftDown) || (this.simulate)) return false;
/*     */ 
/* 174 */       PgenLabeledLineModifyTool.this.click = loc;
/* 175 */       this.ptSelected2 = false;
/*     */ 
/* 177 */       if (button == 1) {
/* 178 */         if (this.attrDlg.isAddLineMode()) {
/* 179 */           this.points.add(PgenLabeledLineModifyTool.this.click);
/*     */         }
/*     */         else {
/* 182 */           PgenLabeledLineModifyTool.this.elSelected = PgenLabeledLineModifyTool.this.drawingLayer.getNearestElement(PgenLabeledLineModifyTool.this.click, PgenLabeledLineModifyTool.this.labeledLine);
/* 183 */           this.firstDown = loc;
/*     */         }
/*     */ 
/* 186 */         return false;
/*     */       }
/*     */ 
/* 189 */       if (button == 3)
/*     */       {
/* 191 */         if (this.attrDlg.isAddLineMode()) {
/* 192 */           if (this.points.size() == 0) {
/* 193 */             this.attrDlg.resetLabeledLineBtns();
/*     */           }
/* 195 */           else if (this.points.size() < 2) {
/* 196 */             PgenLabeledLineModifyTool.this.drawingLayer.removeGhostLine();
/* 197 */             this.points.clear();
/* 198 */             this.mapEditor.refresh();
/*     */           }
/*     */           else
/*     */           {
/* 206 */             LabeledLine newll = this.def.createLabeledLine(PgenLabeledLineModifyTool.this.pgenCategory, PgenLabeledLineModifyTool.this.pgenType, this.attrDlg, 
/* 207 */               this.points, null, PgenLabeledLineModifyTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 210 */             PgenLabeledLineModifyTool.this.drawingLayer.addElement(newll);
/* 211 */             PgenLabeledLineModifyTool.this.labeledLine = newll;
/*     */ 
/* 213 */             PgenLabeledLineModifyTool.this.drawingLayer.removeGhostLine();
/* 214 */             this.points.clear();
/*     */ 
/* 219 */             PgenLabeledLineModifyTool.this.drawingLayer.setSelected(newll);
/*     */ 
/* 221 */             this.mapEditor.refresh();
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 227 */           if (this.attrDlg != null) {
/* 228 */             this.attrDlg.close();
/*     */           }
/*     */ 
/* 231 */           this.attrDlg = null;
/*     */ 
/* 233 */           PgenLabeledLineModifyTool.this.drawingLayer.removeGhostLine();
/* 234 */           this.ptSelected2 = false; this.ptSelected = false;
/* 235 */           PgenLabeledLineModifyTool.this.drawingLayer.removeSelected();
/* 236 */           this.mapEditor.refresh();
/* 237 */           PgenUtil.setSelectingMode();
/*     */         }
/* 239 */         return true;
/*     */       }
/*     */ 
/* 244 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 258 */       if (!PgenLabeledLineModifyTool.this.isResourceEditable()) return false;
/*     */ 
/* 261 */       Coordinate loc = this.mapEditor.translateClick(x, y);
/* 262 */       if ((loc == null) || (this.simulate)) return false;
/*     */ 
/* 264 */       if (this.attrDlg.isAddLineMode())
/*     */       {
/* 266 */         String lineType = "LINE_DASHED_4";
/* 267 */         if (PgenLabeledLineModifyTool.this.labeledLine.getPgenType().equalsIgnoreCase("Cloud")) {
/* 268 */           lineType = "SCALLOPED";
/*     */         }
/* 270 */         else if (PgenLabeledLineModifyTool.this.labeledLine.getPgenType().equalsIgnoreCase("Turbulence")) {
/* 271 */           lineType = "LINE_DASHED_4";
/*     */         }
/* 273 */         AbstractDrawableComponent ghost = this.def.create(DrawableType.LINE, this.attrDlg, 
/* 274 */           "Lines", lineType, this.points, PgenLabeledLineModifyTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 276 */         if ((this.points != null) && (this.points.size() >= 1))
/*     */         {
/* 278 */           ArrayList ghostPts = new ArrayList(this.points);
/* 279 */           ghostPts.add(loc);
/* 280 */           Line ln = (Line)ghost;
/* 281 */           ln.setLinePoints(new ArrayList(ghostPts));
/*     */ 
/* 283 */           PgenLabeledLineModifyTool.this.drawingLayer.setGhostLine(ghost);
/* 284 */           this.mapEditor.refresh();
/*     */         }
/*     */       }
/*     */ 
/* 288 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int button)
/*     */     {
/* 299 */       if ((!PgenLabeledLineModifyTool.this.isResourceEditable()) || (this.shiftDown) || (this.simulate)) return false;
/*     */ 
/* 301 */       if (button == 1)
/*     */       {
/* 303 */         Coordinate loc = this.mapEditor.translateClick(x, y);
/* 304 */         if ((loc == null) || (PgenLabeledLineModifyTool.this.elSelected == null)) return true;
/*     */ 
/* 307 */         if ((PgenLabeledLineModifyTool.this.drawingLayer.getDistance(PgenLabeledLineModifyTool.this.elSelected, loc) > 30.0D) && (!this.ptSelected2))
/*     */         {
/* 309 */           if ((this.firstDown != null) && (PgenLabeledLineModifyTool.this.drawingLayer.getDistance(PgenLabeledLineModifyTool.this.elSelected, this.firstDown) < 30.0D)) {
/* 310 */             this.firstDown = null;
/*     */           }
/*     */           else {
/* 313 */             return false;
/*     */           }
/*     */         }
/*     */ 
/* 317 */         this.ptSelected2 = true;
/* 318 */         if (PgenLabeledLineModifyTool.this.elSelected != null) {
/* 319 */           if ((PgenLabeledLineModifyTool.this.elSelected.getParent() instanceof Label))
/*     */           {
/* 321 */             if ((PgenLabeledLineModifyTool.this.elSelected instanceof Line))
/*     */             {
/* 323 */               Line ln = (Line)PgenLabeledLineModifyTool.this.elSelected;
/* 324 */               int idx = getNearestPtIndex(ln, PgenLabeledLineModifyTool.this.click);
/*     */ 
/* 327 */               if (idx == 0)
/*     */               {
/* 329 */                 PgenLabeledLineModifyTool.this.elSelected = ((Label)PgenLabeledLineModifyTool.this.elSelected.getParent()).getSpe();
/*     */               }
/*     */               else
/*     */               {
/* 333 */                 this.ghostEl = ((Line)ln.copy());
/* 334 */                 this.ghostEl.setColors(new Color[] { this.ghostColor, new Color(255, 255, 255) });
/* 335 */                 this.ghostEl.setPgenCategory(ln.getPgenCategory());
/* 336 */                 this.ghostEl.setPgenType(ln.getPgenType());
/*     */ 
/* 338 */                 this.ghostEl.removePoint(idx);
/* 339 */                 this.ghostEl.addPoint(idx, loc);
/*     */ 
/* 341 */                 PgenLabeledLineModifyTool.this.drawingLayer.setGhostLine(this.ghostEl);
/* 342 */                 this.mapEditor.refresh();
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 347 */             if ((PgenLabeledLineModifyTool.this.elSelected instanceof SinglePointElement))
/*     */             {
/* 349 */               ((SinglePointElement)PgenLabeledLineModifyTool.this.elSelected).setLocation(loc);
/* 350 */               PgenLabeledLineModifyTool.this.drawingLayer.resetElement(PgenLabeledLineModifyTool.this.elSelected);
/*     */ 
/* 353 */               Label lbl = (Label)PgenLabeledLineModifyTool.this.elSelected.getParent();
/*     */ 
/* 355 */               for (Line ln : lbl.getArrows()) {
/* 356 */                 ln.removePoint(0);
/* 357 */                 ln.addPoint(0, loc);
/* 358 */                 PgenLabeledLineModifyTool.this.drawingLayer.resetElement(ln);
/*     */               }
/*     */ 
/* 361 */               if ((PgenLabeledLineModifyTool.this.ccfp != null) && (PgenAddLabelHandler.isPtsInArea(PgenLabeledLineModifyTool.this.ccfp.getAreaLine(), loc))) {
/* 362 */                 for (Line ln : lbl.getArrows()) {
/* 363 */                   lbl.remove(ln);
/*     */                 }
/*     */               }
/* 366 */               if ((PgenLabeledLineModifyTool.this.ccfp != null) && (!PgenAddLabelHandler.isPtsInArea(PgenLabeledLineModifyTool.this.ccfp.getAreaLine(), loc)) && (lbl.getArrows().size() == 0)) {
/* 367 */                 addArrow(loc, lbl);
/*     */               }
/* 369 */               this.mapEditor.refresh();
/* 370 */               if (this.oldLoc == null)
/* 371 */                 this.oldLoc = new Coordinate(((SinglePointElement)PgenLabeledLineModifyTool.this.elSelected).getLocation());
/*     */             }
/*     */           }
/* 374 */           else if ((PgenLabeledLineModifyTool.this.elSelected instanceof Line)) {
/* 375 */             Line ln = (Line)PgenLabeledLineModifyTool.this.elSelected;
/*     */ 
/* 377 */             this.ghostEl = ((Line)ln.copy());
/* 378 */             this.ghostEl.setColors(new Color[] { this.ghostColor, new Color(255, 255, 255) });
/* 379 */             this.ghostEl.setPgenCategory(ln.getPgenCategory());
/* 380 */             this.ghostEl.setPgenType(ln.getPgenType());
/*     */ 
/* 382 */             this.ptIndex = getNearestPtIndex(this.ghostEl, PgenLabeledLineModifyTool.this.click);
/*     */ 
/* 384 */             this.ghostEl.removePoint(this.ptIndex);
/* 385 */             this.ghostEl.addPoint(this.ptIndex, loc);
/*     */ 
/* 387 */             PgenLabeledLineModifyTool.this.drawingLayer.setGhostLine(this.ghostEl);
/* 388 */             this.mapEditor.refresh();
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 395 */         this.simulate = true;
/* 396 */         PgenUtil.simulateMouseDown(x, y, button, this.mapEditor);
/* 397 */         this.simulate = false;
/*     */       }
/*     */ 
/* 400 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int x, int y, int button)
/*     */     {
/* 412 */       this.firstDown = null;
/* 413 */       if ((!PgenLabeledLineModifyTool.this.isResourceEditable()) || (this.shiftDown) || (this.simulate)) return false;
/*     */ 
/* 415 */       if ((button == 1) && (PgenLabeledLineModifyTool.this.drawingLayer != null))
/*     */       {
/* 417 */         LabeledLine mergedLine = null;
/*     */ 
/* 419 */         if (PgenLabeledLineModifyTool.this.elSelected != null) {
/* 420 */           LabeledLine newll = PgenLabeledLineModifyTool.this.labeledLine.copy();
/*     */ 
/* 422 */           if ((PgenLabeledLineModifyTool.this.elSelected instanceof SinglePointElement))
/*     */           {
/* 424 */             if ((PgenLabeledLineModifyTool.this.elSelected.getParent() instanceof Label))
/*     */             {
/* 426 */               mergedLine = PgenUtil.mergeLabels(newll, newll.getLabelAt(((SinglePointElement)PgenLabeledLineModifyTool.this.elSelected).getLocation()), ((SinglePointElement)PgenLabeledLineModifyTool.this.elSelected).getLocation(), this.mapEditor, PgenLabeledLineModifyTool.this.drawingLayer);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 431 */           if ((PgenLabeledLineModifyTool.this.elSelected.getParent() instanceof Label))
/*     */           {
/* 433 */             if (((PgenLabeledLineModifyTool.this.elSelected instanceof Line)) && (this.ghostEl != null) && (this.ptSelected2)) {
/* 434 */               this.ptSelected2 = false;
/*     */ 
/* 436 */               DrawableElement el = PgenLabeledLineModifyTool.this.drawingLayer.getNearestElement(PgenLabeledLineModifyTool.this.click, newll);
/*     */ 
/* 439 */               if ((el instanceof Line)) {
/* 440 */                 ((Line)el).setPoints(this.ghostEl.getPoints());
/* 441 */                 PgenLabeledLineModifyTool.this.drawingLayer.removeGhostLine();
/*     */               }
/*     */             }
/* 444 */             else if ((PgenLabeledLineModifyTool.this.elSelected instanceof SinglePointElement))
/*     */             {
/* 447 */               if ((PgenLabeledLineModifyTool.this.elSelected.getParent() instanceof Label))
/*     */               {
/* 449 */                 Label lbl = (Label)PgenLabeledLineModifyTool.this.elSelected.getParent();
/* 450 */                 lbl.getSpe().setLocation(this.oldLoc);
/* 451 */                 for (Line ln : lbl.getArrows()) {
/* 452 */                   ln.removePoint(0);
/* 453 */                   ln.addPoint(0, this.oldLoc);
/*     */                 }
/*     */               }
/*     */ 
/* 457 */               this.oldLoc = null;
/*     */             }
/*     */ 
/*     */           }
/* 461 */           else if (((PgenLabeledLineModifyTool.this.elSelected instanceof Line)) && (this.ghostEl != null) && (this.ptSelected2)) {
/* 462 */             this.ptSelected2 = false;
/*     */ 
/* 464 */             DrawableElement el = PgenLabeledLineModifyTool.this.drawingLayer.getNearestElement(PgenLabeledLineModifyTool.this.click, newll);
/*     */ 
/* 466 */             if ((el instanceof Line)) {
/* 467 */               ((Line)el).setPoints(this.ghostEl.getPoints());
/* 468 */               PgenLabeledLineModifyTool.this.drawingLayer.removeGhostLine();
/* 469 */               this.ghostEl = null;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 474 */           if (mergedLine != null)
/*     */           {
/* 476 */             ArrayList old = new ArrayList();
/* 477 */             old.add(mergedLine);
/* 478 */             old.add(PgenLabeledLineModifyTool.this.labeledLine);
/*     */ 
/* 480 */             ArrayList newLines = new ArrayList();
/* 481 */             newLines.add(newll);
/*     */ 
/* 483 */             PgenLabeledLineModifyTool.this.drawingLayer.replaceElements(old, newLines);
/*     */           }
/*     */           else {
/* 486 */             PgenLabeledLineModifyTool.this.drawingLayer.replaceElement(PgenLabeledLineModifyTool.this.labeledLine, newll);
/*     */           }
/*     */ 
/* 489 */           PgenLabeledLineModifyTool.this.labeledLine = newll;
/*     */ 
/* 491 */           if (((PgenLabeledLineModifyTool.this.labeledLine instanceof Ccfp)) && (PgenLabeledLineModifyTool.this.labeledLine.getLabels() != null) && (PgenLabeledLineModifyTool.this.labeledLine.getLabels().size() > 0))
/*     */           {
/* 493 */             Label lbl = (Label)PgenLabeledLineModifyTool.this.labeledLine.getLabels().get(0);
/* 494 */             Coordinate loc = lbl.getSpe().getLocation();
/*     */ 
/* 496 */             if ((!PgenAddLabelHandler.isPtsInArea((Line)PgenLabeledLineModifyTool.this.labeledLine.getPrimaryDE(), loc)) && (lbl.getArrows().size() == 0)) {
/* 497 */               addArrow(loc, lbl);
/*     */             }
/* 499 */             if ((PgenAddLabelHandler.isPtsInArea((Line)PgenLabeledLineModifyTool.this.labeledLine.getPrimaryDE(), loc)) && (lbl.getArrows().size() > 0)) {
/* 500 */               for (Line ln : lbl.getArrows()) {
/* 501 */                 lbl.remove(ln);
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 506 */           PgenLabeledLineModifyTool.this.resetSelected();
/*     */ 
/* 508 */           PgenLabeledLineModifyTool.this.elSelected = null;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 515 */       if ((PgenLabeledLineModifyTool.this.labeledLine instanceof Ccfp)) ((Ccfp)PgenLabeledLineModifyTool.this.labeledLine).moveText2Last();
/*     */ 
/* 517 */       this.mapEditor.refresh();
/* 518 */       return true;
/*     */     }
/*     */ 
/*     */     private void addArrow(Coordinate loc, Label lbl)
/*     */     {
/* 523 */       ArrayList locs = new ArrayList();
/* 524 */       locs.add(loc);
/* 525 */       locs.add(PgenLabeledLineModifyTool.this.ccfp.getAreaLine().getCentroid());
/*     */ 
/* 527 */       Line aln = new Line(null, new Color[] { Color.WHITE }, 2.0F, 1.0D, false, false, locs, 0, 
/* 528 */         FillPatternList.FillPattern.FILL_PATTERN_2, "Lines", "POINTED_ARROW");
/* 529 */       lbl.addArrow(aln);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenLabeledLineModifyTool
 * JD-Core Version:    0.6.2
 */