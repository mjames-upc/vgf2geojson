/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ import com.vividsolutions.jts.geom.Geometry;
/*    */ import com.vividsolutions.jts.io.ParseException;
/*    */ import com.vividsolutions.jts.io.WKBReader;
/*    */ import com.vividsolutions.jts.io.WKBWriter;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class GeometryTypeAdapter
/*    */   implements ISerializationTypeAdapter<Geometry>
/*    */ {
/*    */   public Geometry deserialize(IDeserializationContext serializer)
/*    */     throws SerializationException
/*    */   {
/* 59 */     byte[] data = serializer.readBinary();
/* 60 */     if (data.length == 0) {
/* 61 */       return null;
/*    */     }
/*    */     try
/*    */     {
/* 65 */       WKBReader parser = new WKBReader();
/* 66 */       return parser.read(data);
/*    */     }
/*    */     catch (RuntimeException e) {
/* 69 */       e.printStackTrace();
/* 70 */       System.out.println("Bad data: " + data);
/* 71 */       return null;
/*    */     } catch (ParseException e) {
/* 73 */       System.out.println("Bad data, unable to parse: " + data);
/* 74 */     }return null;
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, Geometry object)
/*    */     throws SerializationException
/*    */   {
/* 88 */     byte[] data = null;
/* 89 */     if (object == null) {
/* 90 */       data = new byte[0];
/*    */     } else {
/* 92 */       WKBWriter writer = new WKBWriter();
/* 93 */       data = writer.write(object);
/*    */     }
/*    */ 
/* 96 */     serializer.writeBinary(data);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.GeometryTypeAdapter
 * JD-Core Version:    0.6.2
 */