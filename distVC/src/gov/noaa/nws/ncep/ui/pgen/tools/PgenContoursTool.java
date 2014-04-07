/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.ContoursAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.ContoursAttrDlg.ContourDrawingStatus;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourCircle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourMinmax;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.controls.CommandStackListener;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenContoursTool extends AbstractPgenDrawingTool
/*     */   implements CommandStackListener
/*     */ {
/*  72 */   private ArrayList<Coordinate> points = new ArrayList();
/*     */ 
/*  78 */   private DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/*  83 */   private boolean addContourLine = false;
/*     */ 
/*  85 */   private Contours elem = null;
/*     */ 
/*  87 */   private Contours lastElem = null;
/*     */ 
/*  89 */   private ExecutionEvent lastEvent = null;
/*     */ 
/*  91 */   private int undo = -1;
/*     */ 
/*  93 */   private int redo = -1;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/* 109 */     super.activateTool();
/*     */ 
/* 118 */     Object de = this.event.getTrigger();
/*     */ 
/* 122 */     if (this.event != this.lastEvent) {
/* 123 */       if ((de instanceof Contours)) {
/* 124 */         this.elem = ((Contours)de);
/*     */ 
/* 126 */         setPgenSelectHandler();
/* 127 */         PgenSession.getInstance().getPgenPalette().setActiveIcon("Select");
/*     */       } else {
/* 129 */         this.elem = null;
/*     */       }
/* 131 */       this.lastEvent = this.event;
/*     */     }
/*     */ 
/* 134 */     if ((this.attrDlg instanceof ContoursAttrDlg))
/*     */     {
/* 136 */       ((ContoursAttrDlg)this.attrDlg).setDrawingTool(this);
/* 137 */       if (de != null) {
/* 138 */         ((ContoursAttrDlg)this.attrDlg).setSelectMode();
/*     */       }
/*     */       else
/* 141 */         ((ContoursAttrDlg)this.attrDlg).setDrawingStatus(ContoursAttrDlg.ContourDrawingStatus.DRAW_LINE);
/*     */     }
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 156 */     if (this.mouseHandler == null)
/*     */     {
/* 158 */       this.mouseHandler = new PgenContoursHandler();
/*     */     }
/*     */ 
/* 162 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public void resetUndoRedoCount()
/*     */   {
/* 793 */     this.undo = -1;
/* 794 */     this.redo = -1;
/*     */   }
/*     */ 
/*     */   public void stacksUpdated(int undoSize, int redoSize)
/*     */   {
/* 800 */     if ((undoSize < this.undo) || (redoSize < this.redo))
/*     */     {
/* 802 */       Contours tmp = this.elem;
/* 803 */       this.elem = this.lastElem;
/* 804 */       this.lastElem = tmp;
/*     */     }
/*     */ 
/* 808 */     this.undo = undoSize;
/* 809 */     this.redo = redoSize;
/*     */   }
/*     */ 
/*     */   public Contours getCurrentContour()
/*     */   {
/* 818 */     return this.elem;
/*     */   }
/*     */ 
/*     */   public void setCurrentContour(Contours con)
/*     */   {
/* 826 */     this.attrDlg.setDrawableElement(con);
/* 827 */     this.elem = con;
/*     */   }
/*     */ 
/*     */   public void setPgenSelectHandler()
/*     */   {
/* 835 */     setHandler(new PgenSelectHandler(this, this.mapEditor, this.drawingLayer, this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void setPgenContoursHandler()
/*     */   {
/* 843 */     setHandler(new PgenContoursHandler());
/*     */   }
/*     */ 
/*     */   public void clearSelected()
/*     */   {
/* 851 */     this.drawingLayer.removeSelected();
/* 852 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   protected IInputHandler getDefaultMouseHandler()
/*     */   {
/* 860 */     return new PgenSelectHandler(this, this.mapEditor, this.drawingLayer, this.attrDlg);
/*     */   }
/*     */ 
/*     */   protected void setWorkingComponent(AbstractDrawableComponent adc)
/*     */   {
/* 868 */     if ((adc instanceof Contours)) {
/* 869 */       setCurrentContour((Contours)adc);
/* 870 */       ((ContoursAttrDlg)this.attrDlg).setCurrentContours(this.elem);
/*     */     }
/*     */   }
/*     */ 
/*     */   public class PgenContoursHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     public PgenContoursHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int anX, int aY, int button)
/*     */     {
/* 178 */       if (!PgenContoursTool.this.isResourceEditable()) {
/* 179 */         return false;
/*     */       }
/*     */ 
/* 182 */       Coordinate loc = PgenContoursTool.this.mapEditor.translateClick(anX, aY);
/* 183 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 186 */       if ((PgenContoursTool.this.attrDlg != null) && (((ContoursAttrDlg)PgenContoursTool.this.attrDlg).drawSymbol()))
/*     */       {
/* 188 */         if (button == 1) {
/* 189 */           drawContourMinmax(loc); } else {
/* 190 */           if (button == 3)
/*     */           {
/* 192 */             PgenContoursTool.this.points.clear();
/* 193 */             if (PgenContoursTool.this.attrDlg != null) {
/* 194 */               ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).setDrawingStatus(ContoursAttrDlg.ContourDrawingStatus.SELECT);
/*     */             }
/* 196 */             PgenContoursTool.this.drawingLayer.removeGhostLine();
/*     */ 
/* 198 */             return true;
/*     */           }
/* 200 */           return false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 205 */       if ((PgenContoursTool.this.attrDlg != null) && (((ContoursAttrDlg)PgenContoursTool.this.attrDlg).drawCircle()))
/*     */       {
/* 207 */         if (button == 1)
/*     */         {
/* 209 */           if (PgenContoursTool.this.points.size() == 0)
/*     */           {
/* 211 */             PgenContoursTool.this.points.add(0, loc);
/*     */           }
/*     */           else
/*     */           {
/* 215 */             if (PgenContoursTool.this.points.size() > 1) {
/* 216 */               PgenContoursTool.this.points.remove(1);
/*     */             }
/* 218 */             PgenContoursTool.this.points.add(1, loc);
/* 219 */             drawContourCircle();
/*     */           }
/*     */ 
/* 222 */           return true;
/*     */         }
/* 224 */         if (button == 3)
/*     */         {
/* 226 */           PgenContoursTool.this.points.clear();
/* 227 */           if (PgenContoursTool.this.attrDlg != null) {
/* 228 */             ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).setDrawingStatus(ContoursAttrDlg.ContourDrawingStatus.SELECT);
/*     */           }
/* 230 */           PgenContoursTool.this.drawingLayer.removeGhostLine();
/*     */ 
/* 232 */           return true;
/*     */         }
/* 234 */         return false;
/*     */       }
/*     */ 
/* 240 */       if (button == 1)
/*     */       {
/* 242 */         if ((PgenContoursTool.this.attrDlg != null) && 
/* 243 */           (!((ContoursAttrDlg)PgenContoursTool.this.attrDlg).drawSymbol()) && 
/* 244 */           (!((ContoursAttrDlg)PgenContoursTool.this.attrDlg).drawCircle()))
/*     */         {
/* 246 */           PgenContoursTool.this.points.add(loc);
/*     */         }
/*     */ 
/* 249 */         return true;
/* 250 */       }if (button == 3) {
/* 251 */         if (PgenContoursTool.this.points.size() == 0) {
/* 252 */           ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).setDrawingStatus(ContoursAttrDlg.ContourDrawingStatus.SELECT);
/*     */         }
/*     */         else {
/* 255 */           setDrawingMode();
/* 256 */           drawContours();
/*     */         }
/* 258 */         return true;
/* 259 */       }if (button == 2) {
/* 260 */         return true;
/*     */       }
/* 262 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 275 */       if ((!PgenContoursTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/*     */ 
/* 278 */       Coordinate loc = PgenContoursTool.this.mapEditor.translateClick(x, y);
/* 279 */       if (loc == null) {
/* 280 */         return false;
/*     */       }
/*     */ 
/* 283 */       if ((PgenContoursTool.this.attrDlg != null) && (((ContoursAttrDlg)PgenContoursTool.this.attrDlg).drawSymbol()))
/*     */       {
/* 285 */         ContoursAttrDlg dlg = (ContoursAttrDlg)PgenContoursTool.this.attrDlg;
/* 286 */         ContourMinmax ghost = null;
/* 287 */         ghost = new ContourMinmax(loc, dlg.getActiveSymbolClass(), 
/* 288 */           dlg.getActiveSymbolObjType(), 
/* 289 */           new String[] { dlg.getLabel() });
/*     */ 
/* 291 */         IAttribute mmTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 292 */           .getMinmaxTemplate();
/*     */ 
/* 294 */         if (mmTemp != null) {
/* 295 */           Symbol oneSymb = (Symbol)ghost.getSymbol();
/* 296 */           oneSymb.update(mmTemp);
/*     */         }
/*     */ 
/* 299 */         IAttribute lblTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 300 */           .getLabelTemplate();
/* 301 */         if (lblTemp != null) {
/* 302 */           Text lbl = ghost.getLabel();
/* 303 */           String[] oldText = lbl.getText();
/* 304 */           boolean hide = lbl.getHide().booleanValue();
/* 305 */           boolean auto = lbl.getAuto().booleanValue();
/* 306 */           lbl.update(lblTemp);
/* 307 */           lbl.setText(oldText);
/* 308 */           lbl.setHide(Boolean.valueOf(hide));
/* 309 */           lbl.setAuto(Boolean.valueOf(auto));
/*     */         }
/*     */ 
/* 312 */         PgenContoursTool.this.drawingLayer.setGhostLine(ghost);
/* 313 */         PgenContoursTool.this.mapEditor.refresh();
/*     */ 
/* 315 */         return false;
/*     */       }
/*     */       boolean auto;
/* 320 */       if ((PgenContoursTool.this.attrDlg != null) && (((ContoursAttrDlg)PgenContoursTool.this.attrDlg).drawCircle()))
/*     */       {
/* 322 */         if ((PgenContoursTool.this.points != null) && (PgenContoursTool.this.points.size() >= 1))
/*     */         {
/* 324 */           ContourCircle ghost = new ContourCircle((Coordinate)PgenContoursTool.this.points.get(0), loc, 
/* 325 */             new String[] { ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 326 */             .getLabel() }, 
/* 327 */             ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).hideCircleLabel());
/*     */ 
/* 329 */           IAttribute circleTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 330 */             .getCircleTemplate();
/* 331 */           if (circleTemp != null) {
/* 332 */             ghost.getCircle().setColors(circleTemp.getColors());
/* 333 */             ((Arc)ghost.getCircle()).setLineWidth(circleTemp
/* 334 */               .getLineWidth());
/*     */           }
/*     */ 
/* 337 */           IAttribute lblTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 338 */             .getLabelTemplate();
/* 339 */           if (lblTemp != null) {
/* 340 */             Text lbl = ghost.getLabel();
/* 341 */             String[] oldText = lbl.getText();
/* 342 */             boolean hide = lbl.getHide().booleanValue();
/* 343 */             auto = lbl.getAuto().booleanValue();
/* 344 */             lbl.update(lblTemp);
/* 345 */             lbl.setText(oldText);
/* 346 */             lbl.setHide(Boolean.valueOf(hide));
/* 347 */             lbl.setAuto(Boolean.valueOf(auto));
/*     */           }
/*     */ 
/* 350 */           PgenContoursTool.this.drawingLayer.setGhostLine(ghost);
/* 351 */           PgenContoursTool.this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 354 */         return false;
/*     */       }
/*     */ 
/* 359 */       if ((PgenContoursTool.this.points != null) && (PgenContoursTool.this.points.size() >= 1))
/*     */       {
/* 361 */         ArrayList ghostPts = new ArrayList(
/* 362 */           PgenContoursTool.this.points);
/* 363 */         ghostPts.add(loc);
/*     */ 
/* 365 */         ContourLine cline = new ContourLine(
/* 366 */           ghostPts, 
/* 367 */           ((ILine)PgenContoursTool.this.attrDlg).isClosedLine().booleanValue(), 
/* 368 */           new String[] { ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getLabel() }, 
/* 369 */           ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getNumOfLabels());
/*     */ 
/* 371 */         IAttribute lineTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 372 */           .getLineTemplate();
/*     */ 
/* 374 */         if (lineTemp != null) {
/* 375 */           Line oneLine = cline.getLine();
/* 376 */           Boolean isClosed = oneLine.isClosedLine();
/* 377 */           oneLine.update(lineTemp);
/* 378 */           oneLine.setClosed(isClosed);
/*     */         }
/*     */ 
/* 381 */         String lblstr = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getLabel();
/* 382 */         if ((lblstr != null) && (lblstr.contains("9999"))) {
/* 383 */           cline.getLine().setSmoothFactor(0);
/*     */         }
/*     */ 
/* 386 */         IAttribute lblTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 387 */           .getLabelTemplate();
/* 388 */         if (lblTemp != null) {
/* 389 */           for (Text lbl : cline.getLabels()) {
/* 390 */             String[] oldText = lbl.getText();
/* 391 */             boolean hide = lbl.getHide().booleanValue();
/* 392 */             boolean auto = lbl.getAuto().booleanValue();
/* 393 */             lbl.update(lblTemp);
/* 394 */             lbl.setText(oldText);
/* 395 */             lbl.setHide(Boolean.valueOf(hide));
/* 396 */             lbl.setAuto(Boolean.valueOf(auto));
/*     */           }
/*     */         }
/*     */ 
/* 400 */         Contours el = (Contours)PgenContoursTool.this.def.create(DrawableType.CONTOURS, 
/* 401 */           null, "MET", "Contours", PgenContoursTool.this.points, 
/* 402 */           PgenContoursTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 404 */         cline.setParent(el);
/* 405 */         cline.getLine().setPgenType(
/* 406 */           ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getContourLineType());
/*     */ 
/* 408 */         el.update((ContoursAttrDlg)PgenContoursTool.this.attrDlg);
/* 409 */         el.add(cline);
/*     */ 
/* 411 */         PgenContoursTool.this.drawingLayer.setGhostLine(el);
/* 412 */         PgenContoursTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 416 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 422 */       if ((!PgenContoursTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 423 */       return true;
/*     */     }
/*     */ 
/*     */     private void drawContours()
/*     */     {
/* 431 */       if (PgenContoursTool.this.points.size() > 1)
/*     */       {
/* 433 */         ContourLine cline = new ContourLine(
/* 434 */           PgenContoursTool.this.points, 
/* 435 */           ((ILine)PgenContoursTool.this.attrDlg).isClosedLine().booleanValue(), 
/* 436 */           new String[] { ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getLabel() }, 
/* 437 */           ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getNumOfLabels());
/*     */ 
/* 439 */         cline.getLine().setPgenType(
/* 440 */           ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getContourLineType());
/*     */ 
/* 442 */         IAttribute lineTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 443 */           .getLineTemplate();
/* 444 */         if (lineTemp != null) {
/* 445 */           Line oneLine = cline.getLine();
/* 446 */           Boolean isClosed = oneLine.isClosedLine();
/* 447 */           oneLine.update(lineTemp);
/* 448 */           oneLine.setClosed(isClosed);
/*     */         }
/*     */ 
/* 451 */         String lblstr = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getLabel();
/* 452 */         if ((lblstr != null) && (lblstr.contains("9999"))) {
/* 453 */           cline.getLine().setSmoothFactor(0);
/*     */         }
/*     */ 
/* 456 */         IAttribute lblTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 457 */           .getLabelTemplate();
/* 458 */         if (lblTemp != null) {
/* 459 */           for (Text lbl : cline.getLabels()) {
/* 460 */             String[] oldText = lbl.getText();
/* 461 */             boolean hide = lbl.getHide().booleanValue();
/* 462 */             boolean auto = lbl.getAuto().booleanValue();
/* 463 */             lbl.update(lblTemp);
/* 464 */             lbl.setText(oldText);
/* 465 */             lbl.setHide(Boolean.valueOf(hide));
/* 466 */             lbl.setAuto(Boolean.valueOf(auto));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 472 */         PgenContoursTool.this.elem = checkExistingContours();
/*     */ 
/* 474 */         if (PgenContoursTool.this.elem == null)
/*     */         {
/* 480 */           PgenContoursTool.this.elem = ((Contours)PgenContoursTool.this.def.create(DrawableType.CONTOURS, null, 
/* 481 */             "MET", "Contours", PgenContoursTool.this.points, 
/* 482 */             PgenContoursTool.this.drawingLayer.getActiveLayer()));
/*     */ 
/* 484 */           cline.setParent(PgenContoursTool.this.elem);
/* 485 */           PgenContoursTool.this.elem.update((ContoursAttrDlg)PgenContoursTool.this.attrDlg);
/* 486 */           PgenContoursTool.this.elem.add(cline);
/* 487 */           PgenContoursTool.this.drawingLayer.addElement(PgenContoursTool.this.elem);
/*     */         }
/*     */         else
/*     */         {
/* 497 */           Contours newElem = PgenContoursTool.this.elem.copy();
/*     */ 
/* 499 */           cline.setParent(newElem);
/*     */ 
/* 501 */           newElem.update((ContoursAttrDlg)PgenContoursTool.this.attrDlg);
/*     */ 
/* 503 */           newElem.add(cline);
/*     */ 
/* 505 */           PgenContoursTool.this.drawingLayer.replaceElement(PgenContoursTool.this.elem, newElem);
/* 506 */           PgenContoursTool.this.elem = newElem;
/*     */         }
/*     */ 
/* 510 */         ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).setCurrentContours(PgenContoursTool.this.elem);
/*     */       }
/*     */ 
/* 515 */       PgenContoursTool.this.points.clear();
/*     */ 
/* 518 */       PgenContoursTool.this.drawingLayer.removeGhostLine();
/* 519 */       PgenContoursTool.this.mapEditor.refresh();
/*     */     }
/*     */ 
/*     */     public void drawContourMinmax(Coordinate loc)
/*     */     {
/* 528 */       if (loc != null)
/*     */       {
/* 530 */         String cls = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getActiveSymbolClass();
/* 531 */         String type = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 532 */           .getActiveSymbolObjType();
/* 533 */         ContourMinmax cmm = new ContourMinmax(loc, cls, type, 
/* 534 */           new String[] { ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getLabel() });
/*     */ 
/* 536 */         IAttribute mmTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 537 */           .getMinmaxTemplate();
/*     */ 
/* 539 */         if (mmTemp != null) {
/* 540 */           Symbol oneSymb = (Symbol)cmm.getSymbol();
/* 541 */           oneSymb.update(mmTemp);
/*     */         }
/*     */ 
/* 544 */         IAttribute lblTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 545 */           .getLabelTemplate();
/* 546 */         if (lblTemp != null) {
/* 547 */           Text lbl = cmm.getLabel();
/* 548 */           String[] oldText = lbl.getText();
/* 549 */           boolean hide = lbl.getHide().booleanValue();
/* 550 */           boolean auto = lbl.getAuto().booleanValue();
/* 551 */           lbl.update(lblTemp);
/* 552 */           lbl.setText(oldText);
/* 553 */           lbl.setHide(Boolean.valueOf(hide));
/* 554 */           lbl.setAuto(Boolean.valueOf(auto));
/*     */         }
/*     */ 
/* 559 */         PgenContoursTool.this.elem = checkExistingContours();
/*     */ 
/* 561 */         if (PgenContoursTool.this.elem == null)
/*     */         {
/* 566 */           PgenContoursTool.this.elem = ((Contours)PgenContoursTool.this.def.create(DrawableType.CONTOURS, null, 
/* 567 */             "MET", "Contours", PgenContoursTool.this.points, 
/* 568 */             PgenContoursTool.this.drawingLayer.getActiveLayer()));
/*     */ 
/* 570 */           cmm.setParent(PgenContoursTool.this.elem);
/* 571 */           PgenContoursTool.this.elem.update((ContoursAttrDlg)PgenContoursTool.this.attrDlg);
/* 572 */           PgenContoursTool.this.elem.add(cmm);
/*     */ 
/* 574 */           PgenContoursTool.this.drawingLayer.addElement(PgenContoursTool.this.elem);
/*     */         }
/*     */         else
/*     */         {
/* 584 */           Contours newElem = PgenContoursTool.this.elem.copy();
/*     */ 
/* 586 */           cmm.setParent(newElem);
/*     */ 
/* 588 */           newElem.update((ContoursAttrDlg)PgenContoursTool.this.attrDlg);
/*     */ 
/* 590 */           newElem.add(cmm);
/*     */ 
/* 592 */           PgenContoursTool.this.drawingLayer.replaceElement(PgenContoursTool.this.elem, newElem);
/*     */ 
/* 594 */           PgenContoursTool.this.lastElem = PgenContoursTool.this.elem;
/* 595 */           PgenContoursTool.this.elem = newElem;
/*     */         }
/*     */ 
/* 599 */         ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).setCurrentContours(PgenContoursTool.this.elem);
/*     */       }
/*     */ 
/* 604 */       PgenContoursTool.this.points.clear();
/*     */ 
/* 607 */       PgenContoursTool.this.drawingLayer.removeGhostLine();
/* 608 */       PgenContoursTool.this.mapEditor.refresh();
/*     */     }
/*     */ 
/*     */     private void setDrawingMode()
/*     */     {
/* 617 */       if (PgenContoursTool.this.points.size() == 0)
/* 618 */         if (PgenContoursTool.this.elem == null)
/*     */         {
/* 621 */           if (PgenContoursTool.this.attrDlg != null) {
/* 622 */             PgenContoursTool.this.attrDlg.close();
/*     */           }
/*     */ 
/* 625 */           PgenContoursTool.this.attrDlg = null;
/*     */ 
/* 627 */           PgenContoursTool.this.addContourLine = false;
/*     */ 
/* 629 */           PgenUtil.setSelectingMode();
/*     */         }
/* 634 */         else if (!PgenContoursTool.this.addContourLine) {
/* 635 */           PgenContoursTool.this.elem = null;
/*     */         } else {
/* 637 */           PgenUtil.setSelectingMode();
/*     */         }
/*     */     }
/*     */ 
/*     */     private void drawContourCircle()
/*     */     {
/* 650 */       if ((PgenContoursTool.this.points != null) && (PgenContoursTool.this.points.size() > 1))
/*     */       {
/* 652 */         ContourCircle cmm = new ContourCircle(
/* 653 */           (Coordinate)PgenContoursTool.this.points.get(0), 
/* 654 */           (Coordinate)PgenContoursTool.this.points.get(1), 
/* 655 */           new String[] { ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).getLabel() }, 
/* 656 */           ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).hideCircleLabel());
/*     */ 
/* 658 */         IAttribute circleTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 659 */           .getCircleTemplate();
/* 660 */         if (circleTemp != null) {
/* 661 */           cmm.getCircle().setColors(circleTemp.getColors());
/* 662 */           ((Arc)cmm.getCircle()).setLineWidth(circleTemp
/* 663 */             .getLineWidth());
/*     */         }
/*     */ 
/* 666 */         IAttribute lblTemp = ((ContoursAttrDlg)PgenContoursTool.this.attrDlg)
/* 667 */           .getLabelTemplate();
/* 668 */         if (lblTemp != null) {
/* 669 */           Text lbl = cmm.getLabel();
/* 670 */           String[] oldText = lbl.getText();
/* 671 */           boolean hide = lbl.getHide().booleanValue();
/* 672 */           boolean auto = lbl.getAuto().booleanValue();
/* 673 */           lbl.update(lblTemp);
/* 674 */           lbl.setText(oldText);
/* 675 */           lbl.setHide(Boolean.valueOf(hide));
/* 676 */           lbl.setAuto(Boolean.valueOf(auto));
/*     */         }
/*     */ 
/* 681 */         PgenContoursTool.this.elem = checkExistingContours();
/*     */ 
/* 683 */         if (PgenContoursTool.this.elem == null)
/*     */         {
/* 688 */           PgenContoursTool.this.elem = ((Contours)PgenContoursTool.this.def.create(DrawableType.CONTOURS, null, 
/* 689 */             "MET", "Contours", PgenContoursTool.this.points, 
/* 690 */             PgenContoursTool.this.drawingLayer.getActiveLayer()));
/*     */ 
/* 692 */           cmm.setParent(PgenContoursTool.this.elem);
/* 693 */           PgenContoursTool.this.elem.update((ContoursAttrDlg)PgenContoursTool.this.attrDlg);
/* 694 */           PgenContoursTool.this.elem.add(cmm);
/*     */ 
/* 696 */           PgenContoursTool.this.drawingLayer.addElement(PgenContoursTool.this.elem);
/*     */         }
/*     */         else
/*     */         {
/* 706 */           Contours newElem = PgenContoursTool.this.elem.copy();
/*     */ 
/* 708 */           cmm.setParent(newElem);
/*     */ 
/* 710 */           newElem.update((ContoursAttrDlg)PgenContoursTool.this.attrDlg);
/*     */ 
/* 712 */           newElem.add(cmm);
/*     */ 
/* 714 */           PgenContoursTool.this.drawingLayer.replaceElement(PgenContoursTool.this.elem, newElem);
/*     */ 
/* 716 */           PgenContoursTool.this.elem = newElem;
/*     */         }
/*     */ 
/* 720 */         ((ContoursAttrDlg)PgenContoursTool.this.attrDlg).setCurrentContours(PgenContoursTool.this.elem);
/*     */       }
/*     */ 
/* 725 */       PgenContoursTool.this.points.clear();
/*     */ 
/* 728 */       PgenContoursTool.this.drawingLayer.removeGhostLine();
/* 729 */       PgenContoursTool.this.mapEditor.refresh();
/*     */     }
/*     */ 
/*     */     private Contours checkExistingContours()
/*     */     {
/* 742 */       Contours existingContours = PgenContoursTool.this.elem;
/*     */ 
/* 749 */       if (existingContours == null)
/*     */       {
/* 751 */         Iterator it = PgenContoursTool.this.drawingLayer
/* 752 */           .getActiveLayer().getComponentIterator();
/* 753 */         while (it.hasNext()) {
/* 754 */           AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 755 */           if (((adc instanceof Contours)) && (!(adc instanceof Outlook))) {
/* 756 */             Contours thisContour = (Contours)adc;
/* 757 */             ContoursAttrDlg thisDlg = (ContoursAttrDlg)PgenContoursTool.this.attrDlg;
/* 758 */             if (thisContour.getParm().equals(thisDlg.getParm()))
/*     */             {
/* 760 */               if (thisContour.getLevel().equals(
/* 760 */                 thisDlg.getLevel())) {
/* 761 */                 existingContours = (Contours)adc;
/* 762 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 767 */         if (existingContours != null) {
/* 768 */           MessageDialog msgDlg = new MessageDialog(
/* 769 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow()
/* 770 */             .getShell(), "Warning!", null, "There is another [" + 
/* 771 */             existingContours.getParm() + "," + 
/* 772 */             existingContours.getLevel() + 
/* 773 */             "] Contours element in this layer.\n" + 
/* 774 */             "Do you want to add to it or create a new one?", 
/* 775 */             2, 
/* 776 */             new String[] { "Add to Existing One", 
/* 777 */             "Create a New One" }, 0);
/* 778 */           msgDlg.open();
/*     */ 
/* 781 */           if (msgDlg.getReturnCode() != 0) {
/* 782 */             existingContours = null;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 787 */       return existingContours;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenContoursTool
 * JD-Core Version:    0.6.2
 */