/*     */ package com.raytheon.uf.common.serialization.adapters;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*     */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*     */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*     */ import com.raytheon.uf.common.serialization.SerializationException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ 
/*     */ public class BufferAdapter
/*     */   implements ISerializationTypeAdapter<Buffer>
/*     */ {
/*     */   public void serialize(ISerializationContext serializer, Buffer buffer)
/*     */     throws SerializationException
/*     */   {
/*  67 */     serializer.writeBool(buffer.isDirect());
/*  68 */     buffer.position(0);
/*  69 */     ByteBuffer bb = null;
/*  70 */     byte[] bytes = null;
/*  71 */     if ((buffer instanceof ByteBuffer)) {
/*  72 */       serializer.writeByte((byte)0);
/*  73 */       bytes = new byte[buffer.capacity()];
/*  74 */       bb = ByteBuffer.wrap(bytes);
/*  75 */       bb.put((ByteBuffer)buffer);
/*  76 */     } else if ((buffer instanceof ShortBuffer)) {
/*  77 */       serializer.writeByte((byte)1);
/*  78 */       bytes = new byte[2 * buffer.capacity()];
/*  79 */       bb = ByteBuffer.wrap(bytes);
/*  80 */       bb.asShortBuffer().put((ShortBuffer)buffer);
/*  81 */     } else if ((buffer instanceof FloatBuffer)) {
/*  82 */       serializer.writeByte((byte)2);
/*  83 */       bytes = new byte[4 * buffer.capacity()];
/*  84 */       bb = ByteBuffer.wrap(bytes);
/*  85 */       bb.asFloatBuffer().put((FloatBuffer)buffer);
/*  86 */     } else if ((buffer instanceof IntBuffer)) {
/*  87 */       serializer.writeByte((byte)3);
/*  88 */       bytes = new byte[4 * buffer.capacity()];
/*  89 */       bb = ByteBuffer.wrap(bytes);
/*  90 */       bb.asIntBuffer().put((IntBuffer)buffer);
/*  91 */     } else if ((buffer instanceof DoubleBuffer)) {
/*  92 */       serializer.writeByte((byte)4);
/*  93 */       bytes = new byte[8 * buffer.capacity()];
/*  94 */       bb = ByteBuffer.wrap(bytes);
/*  95 */       bb.asDoubleBuffer().put((DoubleBuffer)buffer);
/*  96 */     } else if ((buffer instanceof LongBuffer)) {
/*  97 */       serializer.writeByte((byte)5);
/*  98 */       bytes = new byte[8 * buffer.capacity()];
/*  99 */       bb = ByteBuffer.wrap(bytes);
/* 100 */       bb.asLongBuffer().put((LongBuffer)buffer);
/*     */     } else {
/* 102 */       throw new SerializationException("Could not handle buffer type: " + 
/* 103 */         buffer.getClass());
/*     */     }
/* 105 */     serializer.writeBinary(bb.array());
/*     */   }
/*     */ 
/*     */   public Buffer deserialize(IDeserializationContext deserializer)
/*     */     throws SerializationException
/*     */   {
/* 118 */     boolean direct = deserializer.readBool();
/* 119 */     byte type = deserializer.readByte();
/* 120 */     byte[] bytes = deserializer.readBinary();
/* 121 */     ByteBuffer buffer = direct ? ByteBuffer.allocateDirect(bytes.length) : 
/* 122 */       ByteBuffer.allocate(bytes.length);
/* 123 */     buffer.put(bytes);
/* 124 */     buffer.rewind();
/* 125 */     Buffer dataBuffer = null;
/* 126 */     switch (type) {
/*     */     case 0:
/* 128 */       dataBuffer = buffer;
/* 129 */       break;
/*     */     case 1:
/* 131 */       dataBuffer = buffer.asShortBuffer();
/* 132 */       break;
/*     */     case 2:
/* 134 */       dataBuffer = buffer.asFloatBuffer();
/* 135 */       break;
/*     */     case 3:
/* 137 */       dataBuffer = buffer.asIntBuffer();
/* 138 */       break;
/*     */     case 4:
/* 140 */       dataBuffer = buffer.asDoubleBuffer();
/* 141 */       break;
/*     */     case 5:
/* 143 */       dataBuffer = buffer.asLongBuffer();
/* 144 */       break;
/*     */     default:
/* 146 */       throw new SerializationException("Unrecognized buffer type: " + 
/* 147 */         type);
/*     */     }
/* 149 */     return dataBuffer;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.BufferAdapter
 * JD-Core Version:    0.6.2
 */