/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*    */ 
/*    */ public class PgenDistanceOptions extends AbstractPgenDrawingTool
/*    */ {
/*    */   public IInputHandler getMouseHandler()
/*    */   {
/* 42 */     if (this.mouseHandler == null)
/*    */     {
/* 45 */       this.mouseHandler = new InputHandlerDefaultImpl();
/*    */     }
/*    */ 
/* 49 */     return this.mouseHandler;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenDistanceOptions
 * JD-Core Version:    0.6.2
 */