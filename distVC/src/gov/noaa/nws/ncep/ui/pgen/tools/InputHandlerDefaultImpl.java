/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.viz.ui.input.InputAdapter;
/*    */ 
/*    */ public class InputHandlerDefaultImpl extends InputAdapter
/*    */ {
/*    */   protected boolean shiftDown;
/*    */ 
/*    */   public boolean handleKeyDown(int keyCode)
/*    */   {
/* 13 */     if (keyCode == 131072) {
/* 14 */       this.shiftDown = true;
/*    */     }
/*    */ 
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean handleKeyUp(int keyCode)
/*    */   {
/* 22 */     if (keyCode == 131072) {
/* 23 */       this.shiftDown = false;
/*    */     }
/* 25 */     return true;
/*    */   }
/*    */ 
/*    */   public void preprocess()
/*    */   {
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.InputHandlerDefaultImpl
 * JD-Core Version:    0.6.2
 */