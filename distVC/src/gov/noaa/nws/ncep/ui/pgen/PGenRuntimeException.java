/*    */ package gov.noaa.nws.ncep.ui.pgen;
/*    */ 
/*    */ public class PGenRuntimeException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 2066263644154503458L;
/*    */ 
/*    */   public PGenRuntimeException(String message)
/*    */   {
/* 21 */     super(message);
/*    */   }
/*    */ 
/*    */   public PGenRuntimeException(String message, Throwable cause)
/*    */   {
/* 30 */     super(message, cause);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.PGenRuntimeException
 * JD-Core Version:    0.6.2
 */