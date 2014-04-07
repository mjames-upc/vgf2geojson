/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ import com.vividsolutions.jts.geom.Coordinate;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ public class CoordAdapter extends XmlAdapter<String, Coordinate>
/*    */   implements ISerializationTypeAdapter<Coordinate>
/*    */ {
/*    */   public String marshal(Coordinate coord)
/*    */     throws Exception
/*    */   {
/* 54 */     StringBuffer buf = new StringBuffer();
/* 55 */     buf.append(Double.toString(coord.x)).append(",").append(
/* 56 */       Double.toString(coord.y));
/* 57 */     return buf.toString();
/*    */   }
/*    */ 
/*    */   public Coordinate unmarshal(String v) throws Exception
/*    */   {
/* 62 */     Coordinate retVal = null;
/* 63 */     if (v != null) {
/* 64 */       String[] tokens = v.split(",");
/* 65 */       retVal = new Coordinate(Double.valueOf(tokens[0]).doubleValue(), 
/* 66 */         Double.valueOf(tokens[1]).doubleValue());
/*    */     }
/* 68 */     return retVal;
/*    */   }
/*    */ 
/*    */   public Coordinate deserialize(IDeserializationContext deserializer)
/*    */     throws SerializationException
/*    */   {
/* 74 */     double x = deserializer.readDouble();
/* 75 */     double y = deserializer.readDouble();
/*    */ 
/* 77 */     return new Coordinate(x, y);
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, Coordinate object)
/*    */     throws SerializationException
/*    */   {
/* 83 */     serializer.writeDouble(object.x);
/* 84 */     serializer.writeDouble(object.y);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.CoordAdapter
 * JD-Core Version:    0.6.2
 */