/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public class ByteBufferAdapter
/*    */   implements ISerializationTypeAdapter<ByteBuffer>
/*    */ {
/*    */   public ByteBuffer deserialize(IDeserializationContext deserializer)
/*    */     throws SerializationException
/*    */   {
/* 50 */     ByteBuffer buf = null;
/*    */ 
/* 52 */     byte[] b = deserializer.readBinary();
/* 53 */     buf = ByteBuffer.wrap(b);
/*    */ 
/* 55 */     return buf;
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, ByteBuffer buffer)
/*    */     throws SerializationException
/*    */   {
/*    */     byte[] b;
/*    */     byte[] b;
/* 62 */     if (buffer.hasArray()) {
/* 63 */       b = buffer.array();
/*    */     } else {
/* 65 */       b = new byte[buffer.capacity()];
/* 66 */       ((ByteBuffer)buffer.duplicate().rewind()).get(b);
/*    */     }
/* 68 */     serializer.writeBinary(b);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.ByteBufferAdapter
 * JD-Core Version:    0.6.2
 */