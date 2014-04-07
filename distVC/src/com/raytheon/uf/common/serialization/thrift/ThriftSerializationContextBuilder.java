/*    */ package com.raytheon.uf.common.serialization.thrift;
/*    */ 
/*    */ import com.facebook.thrift.transport.TIOStreamTransport;
/*    */ import com.facebook.thrift.transport.TTransport;
/*    */ import com.raytheon.uf.common.serialization.DynamicSerializationManager;
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContextBuilder;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class ThriftSerializationContextBuilder
/*    */   implements ISerializationContextBuilder
/*    */ {
/*    */   public IDeserializationContext buildDeserializationContext(InputStream data, DynamicSerializationManager manager)
/*    */   {
/* 60 */     TTransport transport = new TIOStreamTransport(data);
/* 61 */     SelfDescribingBinaryProtocol proto = new SelfDescribingBinaryProtocol(
/* 62 */       transport);
/*    */ 
/* 64 */     return new ThriftSerializationContext(proto, manager);
/*    */   }
/*    */ 
/*    */   public ISerializationContext buildSerializationContext(OutputStream data, DynamicSerializationManager manager)
/*    */   {
/* 76 */     TTransport transport = new TIOStreamTransport(data);
/* 77 */     SelfDescribingBinaryProtocol proto = new SelfDescribingBinaryProtocol(
/* 78 */       transport);
/*    */ 
/* 80 */     return new ThriftSerializationContext(proto, manager);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.thrift.ThriftSerializationContextBuilder
 * JD-Core Version:    0.6.2
 */