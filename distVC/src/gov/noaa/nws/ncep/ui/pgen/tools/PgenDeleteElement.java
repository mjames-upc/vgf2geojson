/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*    */ 
/*    */ public class PgenDeleteElement extends AbstractPgenTool
/*    */ {
/* 39 */   protected PgenDeleteElementHandler deleteHandler = null;
/*    */ 
/*    */   public IInputHandler getMouseHandler()
/*    */   {
/* 54 */     if ((this.deleteHandler == null) || (this.mapEditor != this.deleteHandler.getMapEditor()) || 
/* 55 */       (this.drawingLayer != this.deleteHandler.getPgenrsc())) {
/* 56 */       this.deleteHandler = new PgenDeleteElementHandler(this);
/*    */     }
/*    */ 
/* 60 */     return this.deleteHandler;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenDeleteElement
 * JD-Core Version:    0.6.2
 */