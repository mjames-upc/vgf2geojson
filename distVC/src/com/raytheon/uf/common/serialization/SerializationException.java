/*    */ package com.raytheon.uf.common.serialization;
/*    */ 
/*    */ public class SerializationException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public SerializationException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SerializationException(String message, Throwable cause)
/*    */   {
/* 45 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public SerializationException(String message) {
/* 49 */     super(message);
/*    */   }
/*    */ 
/*    */   public SerializationException(Throwable cause) {
/* 53 */     super(cause);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.SerializationException
 * JD-Core Version:    0.6.2
 */