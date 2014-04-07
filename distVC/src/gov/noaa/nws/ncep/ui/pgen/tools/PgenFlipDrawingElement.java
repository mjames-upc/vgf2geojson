/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Cloud;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ 
/*     */ public class PgenFlipDrawingElement extends AbstractPgenTool
/*     */ {
/*     */   protected IInputHandler flipHandler;
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  63 */     if (this.flipHandler == null) {
/*  64 */       this.flipHandler = new PgenFlipHandler(this.drawingLayer, this.mapEditor);
/*     */     }
/*  66 */     return this.flipHandler;
/*     */   }
/*     */ 
/*     */   public class PgenFlipHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     private PgenResource flipPgenSource;
/*     */     private AbstractEditor flipNCMapEditor;
/*     */     private boolean preempt;
/*     */     private OperationFilter flipFilter;
/*     */ 
/*     */     public PgenFlipHandler(PgenResource _flipPgenSource, AbstractEditor _flipNCMapEditor)
/*     */     {
/*  84 */       this.flipPgenSource = _flipPgenSource;
/*  85 */       this.flipNCMapEditor = _flipNCMapEditor;
/*  86 */       this.flipFilter = new OperationFilter(Operation.FLIP);
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/*  97 */       if (!PgenFlipDrawingElement.this.isResourceEditable()) return false;
/*     */ 
/*  99 */       this.preempt = false;
/*     */ 
/* 101 */       Coordinate loc = this.flipNCMapEditor.translateClick(anX, aY);
/* 102 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 105 */       AbstractDrawableComponent selectedDrawableElement = this.flipPgenSource.getSelectedComp();
/*     */ 
/* 107 */       if (button == 1)
/*     */       {
/* 111 */         AbstractDrawableComponent reversedDrawableElement = null;
/* 112 */         if ((selectedDrawableElement instanceof Cloud)) {
/* 113 */           reversedDrawableElement = selectedDrawableElement.copy();
/* 114 */           DrawableElement de = this.flipPgenSource.getNearestElement(loc, (Cloud)reversedDrawableElement);
/* 115 */           if ((de != null) && ((de instanceof Line))) {
/* 116 */             ((Cloud)reversedDrawableElement).add(PgenToolUtils.createReversedDrawableElement(de));
/* 117 */             ((Cloud)reversedDrawableElement).remove(de);
/*     */           }
/*     */           else {
/* 120 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/* 124 */           reversedDrawableElement = PgenToolUtils.createReversedDrawableElement(selectedDrawableElement);
/*     */         }
/*     */ 
/* 127 */         leftMouseButtonDownHandler(this.flipPgenSource, selectedDrawableElement, reversedDrawableElement, loc);
/* 128 */         this.flipNCMapEditor.refresh();
/* 129 */         if (selectedDrawableElement != null) this.preempt = true;
/*     */       }
/* 131 */       else if (button == 3) {
/* 132 */         rightMouseButtonDownHandler(this.flipPgenSource, selectedDrawableElement, this.flipNCMapEditor);
/*     */       }
/* 134 */       return this.preempt;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 140 */       if ((!PgenFlipDrawingElement.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 141 */       return this.preempt;
/*     */     }
/*     */ 
/*     */     private void leftMouseButtonDownHandler(PgenResource thePgenSource, AbstractDrawableComponent selectedDrawableElement, AbstractDrawableComponent reversedDrawableElement, Coordinate currentMouselocation)
/*     */     {
/* 157 */       if (selectedDrawableElement == null)
/*     */       {
/* 159 */         selectedDrawableElement = thePgenSource.getNearestComponent(currentMouselocation, this.flipFilter, true);
/* 160 */         if (selectedDrawableElement == null)
/* 161 */           return;
/* 162 */         thePgenSource.setSelected(selectedDrawableElement);
/*     */       } else {
/* 164 */         thePgenSource.replaceElement(selectedDrawableElement, reversedDrawableElement);
/* 165 */         thePgenSource.setSelected(reversedDrawableElement);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void rightMouseButtonDownHandler(PgenResource thePpgenSource, AbstractDrawableComponent selectedDrawableElement, AbstractEditor theNCMapEditor)
/*     */     {
/* 180 */       if (selectedDrawableElement != null)
/*     */       {
/* 182 */         thePpgenSource.removeSelected();
/* 183 */         selectedDrawableElement = null;
/* 184 */         theNCMapEditor.refresh();
/*     */       }
/*     */       else
/*     */       {
/* 188 */         PgenUtil.setSelectingMode();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenFlipDrawingElement
 * JD-Core Version:    0.6.2
 */