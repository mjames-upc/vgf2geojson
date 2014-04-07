/*    */ package com.raytheon.uf.common.serialization;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.annotations.DynamicSerialize;
/*    */ import com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement;
/*    */ 
/*    */ @DynamicSerialize
/*    */ public class SerializableExceptionWrapper
/*    */   implements ISerializableObject
/*    */ {
/*    */ 
/*    */   @DynamicSerializeElement
/*    */   private StackTraceElement[] stackTrace;
/*    */ 
/*    */   @DynamicSerializeElement
/*    */   private String message;
/*    */ 
/*    */   @DynamicSerializeElement
/*    */   private String exceptionClass;
/*    */ 
/*    */   @DynamicSerializeElement
/*    */   private SerializableExceptionWrapper wrapper;
/*    */ 
/*    */   public StackTraceElement[] getStackTrace()
/*    */   {
/* 58 */     return this.stackTrace;
/*    */   }
/*    */ 
/*    */   public void setStackTrace(StackTraceElement[] stackTrace) {
/* 62 */     this.stackTrace = stackTrace;
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 66 */     return this.message;
/*    */   }
/*    */ 
/*    */   public void setMessage(String message) {
/* 70 */     this.message = message;
/*    */   }
/*    */ 
/*    */   public SerializableExceptionWrapper getWrapper() {
/* 74 */     return this.wrapper;
/*    */   }
/*    */ 
/*    */   public void setWrapper(SerializableExceptionWrapper wrapper) {
/* 78 */     this.wrapper = wrapper;
/*    */   }
/*    */ 
/*    */   public String getExceptionClass() {
/* 82 */     return this.exceptionClass;
/*    */   }
/*    */ 
/*    */   public void setExceptionClass(String exceptionClass) {
/* 86 */     this.exceptionClass = exceptionClass;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.SerializableExceptionWrapper
 * JD-Core Version:    0.6.2
 */