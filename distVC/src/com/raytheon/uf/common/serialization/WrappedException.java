/*    */ package com.raytheon.uf.common.serialization;
/*    */ 
/*    */ public class WrappedException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final String exceptionClass;
/*    */ 
/*    */   public WrappedException(String exceptionClass, String message, Throwable cause)
/*    */   {
/* 51 */     super(message, cause);
/* 52 */     this.exceptionClass = exceptionClass;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 61 */     String s = this.exceptionClass != null ? this.exceptionClass : getClass()
/* 62 */       .getName();
/* 63 */     String message = getLocalizedMessage();
/* 64 */     return message != null ? s + ": " + message : s;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.WrappedException
 * JD-Core Version:    0.6.2
 */