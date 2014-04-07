/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*    */ 
/*    */ public class PgenDeletePoint extends PgenSelectingTool
/*    */ {
/* 39 */   protected PgenDeletePointHandler delPtHandler = null;
/*    */ 
/*    */   public void deactivateTool()
/*    */   {
/* 56 */     if (this.delPtHandler != null) this.delPtHandler.cleanup();
/*    */ 
/* 58 */     super.deactivateTool();
/*    */   }
/*    */ 
/*    */   public IInputHandler getMouseHandler()
/*    */   {
/* 68 */     if ((this.delPtHandler == null) || (this.mapEditor != this.delPtHandler.getMapEditor()) || 
/* 69 */       (this.drawingLayer != this.delPtHandler.getPgenrsc())) {
/* 70 */       this.delPtHandler = new PgenDeletePointHandler(this);
/*    */     }
/*    */ 
/* 74 */     return this.delPtHandler;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenDeletePoint
 * JD-Core Version:    0.6.2
 */