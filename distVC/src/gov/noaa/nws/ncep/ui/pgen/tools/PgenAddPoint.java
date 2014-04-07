/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*    */ 
/*    */ public class PgenAddPoint extends AbstractPgenTool
/*    */ {
/* 36 */   protected PgenAddPointHandler addPtHandler = null;
/*    */ 
/*    */   public IInputHandler getMouseHandler()
/*    */   {
/* 51 */     if ((this.addPtHandler == null) || (this.mapEditor != this.addPtHandler.getMapEditor()) || 
/* 52 */       (this.drawingLayer != this.addPtHandler.getPgenrsc())) {
/* 53 */       this.addPtHandler = new PgenAddPointHandler(this);
/*    */     }
/*    */ 
/* 57 */     return this.addPtHandler;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenAddPoint
 * JD-Core Version:    0.6.2
 */