/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.PgenExtrapDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ 
/*     */ public class PgenExtrapTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  55 */     if (this.mouseHandler == null)
/*     */     {
/*  57 */       this.mouseHandler = new PgenExtrapHandler();
/*     */     }
/*     */ 
/*  61 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public class PgenExtrapHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     private boolean preempt;
/*  78 */     private PgenExtrapDlg extrapDlg = null;
/*     */ 
/*  80 */     private OperationFilter extrapFilter = new OperationFilter(Operation.EXTRAPOLATE);
/*     */ 
/*     */     public PgenExtrapHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/*  90 */       if (!PgenExtrapTool.this.isResourceEditable()) return false;
/*     */ 
/*  92 */       this.preempt = false;
/*     */ 
/*  95 */       Coordinate loc = PgenExtrapTool.this.mapEditor.translateClick(anX, aY);
/*  96 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/*  98 */       if (button == 1)
/*     */       {
/* 100 */         if (PgenExtrapTool.this.drawingLayer.getSelectedComp() == null)
/*     */         {
/* 103 */           AbstractDrawableComponent elSelected = PgenExtrapTool.this.drawingLayer.getNearestComponent(loc, this.extrapFilter, true);
/* 104 */           PgenExtrapTool.this.drawingLayer.setSelected(elSelected);
/* 105 */           if (elSelected != null) this.preempt = true;
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 110 */           this.extrapDlg = ((PgenExtrapDlg)PgenExtrapTool.this.attrDlg);
/*     */ 
/* 112 */           AbstractDrawableComponent elem = PgenExtrapTool.this.drawingLayer.getSelectedComp();
/*     */ 
/* 114 */           AbstractDrawableComponent newDE = PgenToolUtils.extrapElement(elem, 
/* 115 */             this.extrapDlg.getDirection(), this.extrapDlg.getDistance());
/*     */ 
/* 117 */           boolean getNewDE = true;
/* 118 */           if ((newDE instanceof WatchBox)) {
/* 119 */             getNewDE = PgenWatchBoxModifyTool.resnapWatchBox(PgenExtrapTool.this.mapEditor, (WatchBox)newDE, (WatchBox)newDE);
/*     */           }
/*     */ 
/* 122 */           if (getNewDE) {
/* 123 */             if (this.extrapDlg.isCopy()) {
/* 124 */               PgenExtrapTool.this.drawingLayer.addElement(newDE);
/*     */             }
/*     */             else {
/* 127 */               PgenExtrapTool.this.drawingLayer.replaceElement(PgenExtrapTool.this.drawingLayer.getSelectedComp(), newDE);
/*     */             }
/*     */           }
/*     */ 
/* 131 */           PgenExtrapTool.this.drawingLayer.removeSelected();
/*     */         }
/*     */ 
/* 135 */         PgenExtrapTool.this.mapEditor.refresh();
/*     */ 
/* 137 */         return this.preempt;
/*     */       }
/*     */ 
/* 140 */       if (button == 3)
/*     */       {
/* 142 */         if (PgenExtrapTool.this.drawingLayer.getSelectedComp() != null)
/*     */         {
/* 144 */           PgenExtrapTool.this.drawingLayer.removeSelected();
/* 145 */           PgenExtrapTool.this.mapEditor.refresh();
/*     */         }
/*     */         else
/*     */         {
/* 151 */           PgenUtil.setSelectingMode();
/*     */         }
/*     */ 
/* 155 */         return true;
/*     */       }
/*     */ 
/* 159 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 166 */       if ((!PgenExtrapTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 167 */       return this.preempt;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenExtrapTool
 * JD-Core Version:    0.6.2
 */