/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.FromAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ 
/*     */ public class PgenFromTool extends AbstractPgenDrawingTool
/*     */ {
/*  36 */   private FromAttrDlg fromAttrDlg = null;
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  52 */     if (this.mouseHandler == null) {
/*  53 */       this.mouseHandler = new PgenFromHandler();
/*     */     }
/*     */ 
/*  56 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public class PgenFromHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     private boolean preempt;
/*  68 */     OperationFilter fromFilter = new OperationFilter(Operation.GFA_FROM);
/*     */ 
/*     */     public PgenFromHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/*  78 */       if (!PgenFromTool.this.isResourceEditable()) return false;
/*     */ 
/*  80 */       this.preempt = false;
/*     */ 
/*  82 */       Coordinate loc = PgenFromTool.this.mapEditor.translateClick(anX, aY);
/*  83 */       if (loc == null) return false;
/*     */ 
/*  85 */       PgenFromTool.this.fromAttrDlg = ((FromAttrDlg)PgenFromTool.this.attrDlg);
/*     */ 
/*  90 */       if (button == 1) {
/*  91 */         if (!PgenFromTool.this.fromAttrDlg.isFormatByTag()) {
/*  92 */           return false;
/*     */         }
/*     */ 
/*  97 */         DrawableElement el1 = PgenFromTool.this.drawingLayer.getNearestElement(loc, this.fromFilter);
/*  98 */         PgenFromTool.this.drawingLayer.setSelected(el1);
/*  99 */         if (el1 != null) this.preempt = true;
/*     */ 
/* 101 */         PgenFromTool.this.fromAttrDlg.formatTagPressed();
/*     */ 
/* 103 */         PgenFromTool.this.mapEditor.refresh();
/* 104 */         return this.preempt;
/*     */       }
/*     */ 
/* 111 */       if (button == 3)
/*     */       {
/* 116 */         PgenFromTool.this.drawingLayer.removeSelected();
/* 117 */         PgenUtil.setSelectingMode();
/*     */ 
/* 119 */         PgenFromTool.this.mapEditor.refresh();
/* 120 */         return true;
/*     */       }
/*     */ 
/* 124 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 131 */       if (!PgenFromTool.this.isResourceEditable()) return false;
/* 132 */       return this.preempt;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenFromTool
 * JD-Core Version:    0.6.2
 */