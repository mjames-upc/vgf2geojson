/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ import java.nio.FloatBuffer;
/*    */ 
/*    */ public class FloatBufferAdapter
/*    */   implements ISerializationTypeAdapter<FloatBuffer>
/*    */ {
/*    */   public FloatBuffer deserialize(IDeserializationContext deserializer)
/*    */     throws SerializationException
/*    */   {
/* 52 */     FloatBuffer buf = null;
/* 53 */     float[] array = deserializer.readFloatArray();
/* 54 */     buf = FloatBuffer.wrap(array);
/* 55 */     return buf;
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, FloatBuffer object)
/*    */     throws SerializationException
/*    */   {
/* 61 */     float[] array = object.array();
/* 62 */     serializer.writeFloatArray(array);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.FloatBufferAdapter
 * JD-Core Version:    0.6.2
 */