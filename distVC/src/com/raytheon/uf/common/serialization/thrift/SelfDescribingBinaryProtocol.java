/*     */ package com.raytheon.uf.common.serialization.thrift;
/*     */ 
/*     */ import com.facebook.thrift.TException;
/*     */ import com.facebook.thrift.protocol.TBinaryProtocol;
/*     */ import com.facebook.thrift.protocol.TField;
/*     */ import com.facebook.thrift.protocol.TStruct;
/*     */ import com.facebook.thrift.transport.TTransport;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ 
/*     */ public class SelfDescribingBinaryProtocol extends TBinaryProtocol
/*     */ {
/*     */   public static final byte FLOAT = 64;
/*     */   private static int MAX_READ_LENGTH;
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  80 */       int sizeInMB = Integer.parseInt(
/*  81 */         System.getProperty("thrift.stream.maxsize"));
/*  82 */       MAX_READ_LENGTH = sizeInMB * 1024 * 1024;
/*     */     } catch (Throwable t) {
/*  84 */       System.err
/*  85 */         .println("Error reading property thrift.stream.maxsize - falling back to default of 200 MB");
/*  86 */       t.printStackTrace();
/*  87 */       MAX_READ_LENGTH = 209715200;
/*     */     }
/*     */   }
/*     */ 
/*     */   public SelfDescribingBinaryProtocol(TTransport trans) {
/*  92 */     this(trans, false, true);
/*     */   }
/*     */ 
/*     */   public SelfDescribingBinaryProtocol(TTransport trans, boolean strictRead, boolean strictWrite)
/*     */   {
/*  97 */     super(trans, strictRead, strictWrite);
/*  98 */     setReadLength(MAX_READ_LENGTH);
/*     */   }
/*     */ 
/*     */   public TField readFieldBegin()
/*     */     throws TException
/*     */   {
/* 109 */     TField field = new TField();
/* 110 */     field.type = readByte();
/* 111 */     if (field.type != 0) {
/* 112 */       field.name = readString();
/* 113 */       field.id = readI16();
/*     */     }
/* 115 */     return field;
/*     */   }
/*     */ 
/*     */   public void writeFieldBegin(TField field)
/*     */     throws TException
/*     */   {
/* 128 */     writeByte(field.type);
/* 129 */     writeString(field.name);
/* 130 */     writeI16(field.id);
/*     */   }
/*     */ 
/*     */   public TStruct readStructBegin()
/*     */   {
/*     */     try
/*     */     {
/* 143 */       name = readString();
/*     */     }
/*     */     catch (TException e)
/*     */     {
/*     */       String name;
/* 147 */       throw new RuntimeException(e);
/*     */     }
/*     */     String name;
/* 149 */     return new TStruct(name);
/*     */   }
/*     */ 
/*     */   public void writeStructBegin(TStruct struct)
/*     */   {
/*     */     try
/*     */     {
/* 163 */       writeString(struct.name);
/*     */     }
/*     */     catch (TException e)
/*     */     {
/* 167 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeFloat(float flt)
/*     */     throws TException
/*     */   {
/* 178 */     writeI32(Float.floatToIntBits(flt));
/*     */   }
/*     */ 
/*     */   public float readFloat()
/*     */     throws TException
/*     */   {
/* 188 */     return Float.intBitsToFloat(readI32());
/*     */   }
/*     */ 
/*     */   private ByteBuffer readBytes(int length)
/*     */     throws TException
/*     */   {
/* 199 */     byte[] b = new byte[length];
/* 200 */     int n = this.trans_.readAll(b, 0, length);
/* 201 */     if (n != length) {
/* 202 */       throw new TException("Bytes read does not match indicated size");
/*     */     }
/* 204 */     ByteBuffer buf = ByteBuffer.wrap(b);
/* 205 */     return buf;
/*     */   }
/*     */ 
/*     */   public float[] readF32List(int sz)
/*     */     throws TException
/*     */   {
/* 216 */     ByteBuffer buf = readBytes(sz * 4);
/* 217 */     FloatBuffer fbuf = buf.asFloatBuffer();
/* 218 */     float[] f = new float[sz];
/* 219 */     fbuf.get(f);
/* 220 */     return f;
/*     */   }
/*     */ 
/*     */   public void writeF32List(float[] arr)
/*     */     throws TException
/*     */   {
/* 230 */     byte[] b = new byte[4 * arr.length];
/* 231 */     ByteBuffer bb = ByteBuffer.wrap(b);
/* 232 */     FloatBuffer fb = bb.asFloatBuffer();
/* 233 */     fb.put(arr);
/* 234 */     this.trans_.write(bb.array());
/*     */   }
/*     */ 
/*     */   public int[] readI32List(int sz)
/*     */     throws TException
/*     */   {
/* 245 */     ByteBuffer buf = readBytes(sz * 4);
/* 246 */     IntBuffer ibuf = buf.asIntBuffer();
/* 247 */     int[] i = new int[sz];
/* 248 */     ibuf.get(i);
/* 249 */     return i;
/*     */   }
/*     */ 
/*     */   public void writeI32List(int[] arr)
/*     */     throws TException
/*     */   {
/* 259 */     byte[] b = new byte[4 * arr.length];
/* 260 */     ByteBuffer bb = ByteBuffer.wrap(b);
/* 261 */     IntBuffer ib = bb.asIntBuffer();
/* 262 */     ib.put(arr);
/* 263 */     this.trans_.write(bb.array());
/*     */   }
/*     */ 
/*     */   public double[] readD64List(int sz)
/*     */     throws TException
/*     */   {
/* 274 */     ByteBuffer buf = readBytes(sz * 8);
/* 275 */     DoubleBuffer pbuf = buf.asDoubleBuffer();
/* 276 */     double[] arr = new double[sz];
/* 277 */     pbuf.get(arr);
/* 278 */     return arr;
/*     */   }
/*     */ 
/*     */   public void writeD64List(double[] arr)
/*     */     throws TException
/*     */   {
/* 288 */     byte[] b = new byte[8 * arr.length];
/* 289 */     ByteBuffer bb = ByteBuffer.wrap(b);
/* 290 */     DoubleBuffer pb = bb.asDoubleBuffer();
/* 291 */     pb.put(arr);
/* 292 */     this.trans_.write(bb.array());
/*     */   }
/*     */ 
/*     */   public long[] readI64List(int sz)
/*     */     throws TException
/*     */   {
/* 303 */     ByteBuffer buf = readBytes(sz * 8);
/* 304 */     LongBuffer pbuf = buf.asLongBuffer();
/* 305 */     long[] arr = new long[sz];
/* 306 */     pbuf.get(arr);
/* 307 */     return arr;
/*     */   }
/*     */ 
/*     */   public void writeI64List(long[] arr)
/*     */     throws TException
/*     */   {
/* 317 */     byte[] b = new byte[8 * arr.length];
/* 318 */     ByteBuffer bb = ByteBuffer.wrap(b);
/* 319 */     LongBuffer pb = bb.asLongBuffer();
/* 320 */     pb.put(arr);
/* 321 */     this.trans_.write(bb.array());
/*     */   }
/*     */ 
/*     */   public short[] readI16List(int sz)
/*     */     throws TException
/*     */   {
/* 332 */     ByteBuffer buf = readBytes(sz * 2);
/* 333 */     ShortBuffer pbuf = buf.asShortBuffer();
/* 334 */     short[] arr = new short[sz];
/* 335 */     pbuf.get(arr);
/* 336 */     return arr;
/*     */   }
/*     */ 
/*     */   public void writeI16List(short[] arr)
/*     */     throws TException
/*     */   {
/* 346 */     byte[] b = new byte[2 * arr.length];
/* 347 */     ByteBuffer bb = ByteBuffer.wrap(b);
/* 348 */     ShortBuffer pb = bb.asShortBuffer();
/* 349 */     pb.put(arr);
/* 350 */     this.trans_.write(bb.array());
/*     */   }
/*     */ 
/*     */   public byte[] readI8List(int sz)
/*     */     throws TException
/*     */   {
/* 361 */     ByteBuffer buf = readBytes(sz);
/* 362 */     return buf.array();
/*     */   }
/*     */ 
/*     */   public void writeI8List(byte[] arr)
/*     */     throws TException
/*     */   {
/* 372 */     this.trans_.write(arr);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.thrift.SelfDescribingBinaryProtocol
 * JD-Core Version:    0.6.2
 */