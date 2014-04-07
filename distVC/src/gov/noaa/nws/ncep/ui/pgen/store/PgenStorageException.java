/*    */ package gov.noaa.nws.ncep.ui.pgen.store;
/*    */ 
/*    */ public class PgenStorageException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public PgenStorageException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public PgenStorageException(String message)
/*    */   {
/* 55 */     super(message);
/*    */   }
/*    */ 
/*    */   public PgenStorageException(Throwable cause)
/*    */   {
/* 63 */     super(cause);
/*    */   }
/*    */ 
/*    */   public PgenStorageException(String message, Throwable cause)
/*    */   {
/* 72 */     super(message, cause);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException
 * JD-Core Version:    0.6.2
 */