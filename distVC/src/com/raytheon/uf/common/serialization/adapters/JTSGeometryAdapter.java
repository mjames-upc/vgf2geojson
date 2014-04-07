/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ import com.vividsolutions.jts.geom.Geometry;
/*    */ import com.vividsolutions.jts.io.ParseException;
/*    */ import com.vividsolutions.jts.io.WKTReader;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ public class JTSGeometryAdapter extends XmlAdapter<String, Geometry>
/*    */   implements ISerializationTypeAdapter<Geometry>
/*    */ {
/*    */   public String marshal(Geometry g)
/*    */     throws Exception
/*    */   {
/* 52 */     return g.toText();
/*    */   }
/*    */ 
/*    */   public Geometry unmarshal(String wkt) throws Exception
/*    */   {
/* 57 */     Geometry geom = null;
/*    */     try {
/* 59 */       geom = new WKTReader().read(wkt);
/*    */     } catch (ParseException e) {
/* 61 */       throw new SerializationException("Error parsing wkt", e);
/*    */     }
/* 63 */     return geom;
/*    */   }
/*    */ 
/*    */   public Geometry deserialize(IDeserializationContext deserializer)
/*    */     throws SerializationException
/*    */   {
/* 69 */     String wkt = deserializer.readString();
/* 70 */     Geometry geom = null;
/*    */     try {
/* 72 */       geom = new WKTReader().read(wkt);
/*    */     } catch (ParseException e) {
/* 74 */       throw new SerializationException("Error parsing wkt", e);
/*    */     }
/* 76 */     return geom;
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, Geometry object)
/*    */     throws SerializationException
/*    */   {
/* 82 */     serializer.writeString(object.toText());
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.JTSGeometryAdapter
 * JD-Core Version:    0.6.2
 */