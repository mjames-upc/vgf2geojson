/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ import com.vividsolutions.jts.geom.Envelope;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ public class JTSEnvelopeAdapter extends XmlAdapter<String, Envelope>
/*    */   implements ISerializationTypeAdapter<Envelope>
/*    */ {
/*    */   private static final String SEPARATOR = ",";
/*    */ 
/*    */   public Envelope deserialize(IDeserializationContext deserializer)
/*    */     throws SerializationException
/*    */   {
/* 53 */     return new Envelope(deserializer.readDouble(), 
/* 54 */       deserializer.readDouble(), deserializer.readDouble(), 
/* 55 */       deserializer.readDouble());
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, Envelope object)
/*    */     throws SerializationException
/*    */   {
/* 61 */     serializer.writeDouble(object.getMinX());
/* 62 */     serializer.writeDouble(object.getMaxX());
/* 63 */     serializer.writeDouble(object.getMinY());
/* 64 */     serializer.writeDouble(object.getMaxY());
/*    */   }
/*    */ 
/*    */   public Envelope unmarshal(String v) throws Exception
/*    */   {
/* 69 */     String[] split = v.split(",");
/* 70 */     double minX = Double.valueOf(split[0]).doubleValue();
/* 71 */     double maxX = Double.valueOf(split[1]).doubleValue();
/* 72 */     double minY = Double.valueOf(split[2]).doubleValue();
/* 73 */     double maxY = Double.valueOf(split[3]).doubleValue();
/* 74 */     return new Envelope(minX, maxX, minY, maxY);
/*    */   }
/*    */ 
/*    */   public String marshal(Envelope v) throws Exception
/*    */   {
/* 79 */     StringBuffer sb = new StringBuffer();
/* 80 */     sb.append(v.getMinX());
/* 81 */     sb.append(",");
/* 82 */     sb.append(v.getMaxX());
/* 83 */     sb.append(",");
/* 84 */     sb.append(v.getMinY());
/* 85 */     sb.append(",");
/* 86 */     sb.append(v.getMaxY());
/* 87 */     return sb.toString();
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.JTSEnvelopeAdapter
 * JD-Core Version:    0.6.2
 */