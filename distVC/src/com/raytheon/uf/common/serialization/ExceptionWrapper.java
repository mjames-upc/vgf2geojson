/*    */ package com.raytheon.uf.common.serialization;
/*    */ 
/*    */ public class ExceptionWrapper
/*    */ {
/*    */   public static SerializableExceptionWrapper wrapThrowable(Throwable t)
/*    */   {
/* 42 */     return buildWrapper(t);
/*    */   }
/*    */ 
/*    */   private static SerializableExceptionWrapper buildWrapper(Throwable t) {
/* 46 */     if (t == null) {
/* 47 */       return null;
/*    */     }
/* 49 */     SerializableExceptionWrapper wrapper = new SerializableExceptionWrapper();
/* 50 */     wrapper.setExceptionClass(t.getClass().getName());
/* 51 */     wrapper.setMessage(t.getLocalizedMessage());
/* 52 */     wrapper.setStackTrace(t.getStackTrace());
/* 53 */     wrapper.setWrapper(buildWrapper(t.getCause()));
/* 54 */     return wrapper;
/*    */   }
/*    */ 
/*    */   public static Throwable unwrapThrowable(SerializableExceptionWrapper sew) {
/* 58 */     return buildThrowable(sew);
/*    */   }
/*    */ 
/*    */   private static Throwable buildThrowable(SerializableExceptionWrapper wrapper) {
/* 62 */     if (wrapper == null) {
/* 63 */       return null;
/*    */     }
/* 65 */     Throwable t = new WrappedException(wrapper.getExceptionClass(), wrapper
/* 66 */       .getMessage(), buildThrowable(wrapper.getWrapper()));
/* 67 */     t.setStackTrace(wrapper.getStackTrace());
/* 68 */     return t;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.ExceptionWrapper
 * JD-Core Version:    0.6.2
 */