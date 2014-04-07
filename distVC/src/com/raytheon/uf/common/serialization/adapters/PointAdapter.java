/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ import java.awt.Point;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ public class PointAdapter extends XmlAdapter<String, Point>
/*    */   implements ISerializationTypeAdapter<Point>
/*    */ {
/*    */   public String marshal(Point coord)
/*    */     throws Exception
/*    */   {
/* 51 */     StringBuffer buf = new StringBuffer();
/* 52 */     buf.append(Double.toString(coord.x)).append(",").append(
/* 53 */       Double.toString(coord.y));
/* 54 */     return buf.toString();
/*    */   }
/*    */ 
/*    */   public Point unmarshal(String v) throws Exception
/*    */   {
/* 59 */     Point retVal = null;
/* 60 */     if (v != null) {
/* 61 */       String[] tokens = v.split(",");
/* 62 */       retVal = new Point(Integer.valueOf(tokens[0]).intValue(), 
/* 63 */         Integer.valueOf(tokens[1]).intValue());
/*    */     }
/* 65 */     return retVal;
/*    */   }
/*    */ 
/*    */   public Point deserialize(IDeserializationContext deserializer)
/*    */     throws SerializationException
/*    */   {
/* 71 */     int x = deserializer.readI32();
/* 72 */     int y = deserializer.readI32();
/*    */ 
/* 74 */     return new Point(x, y);
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, Point object)
/*    */     throws SerializationException
/*    */   {
/* 80 */     serializer.writeI32(object.x);
/* 81 */     serializer.writeI32(object.y);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.PointAdapter
 * JD-Core Version:    0.6.2
 */