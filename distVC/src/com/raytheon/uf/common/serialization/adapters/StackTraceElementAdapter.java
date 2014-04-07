/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ 
/*    */ public class StackTraceElementAdapter
/*    */   implements ISerializationTypeAdapter<StackTraceElement>
/*    */ {
/*    */   public StackTraceElement deserialize(IDeserializationContext deserializer)
/*    */     throws SerializationException
/*    */   {
/* 50 */     return new StackTraceElement(deserializer.readString(), 
/* 51 */       deserializer.readString(), deserializer.readString(), 
/* 52 */       deserializer.readI32());
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, StackTraceElement object)
/*    */     throws SerializationException
/*    */   {
/* 58 */     serializer.writeString(object.getClassName() == null ? "" : object
/* 59 */       .getClassName());
/* 60 */     serializer.writeString(object.getMethodName() == null ? "" : object
/* 61 */       .getMethodName());
/* 62 */     serializer.writeString(object.getFileName() == null ? "" : object
/* 63 */       .getFileName());
/* 64 */     serializer.writeI32(object.getLineNumber());
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.StackTraceElementAdapter
 * JD-Core Version:    0.6.2
 */