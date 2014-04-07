/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import com.vividsolutions.jts.geom.Coordinate;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ public class CoordinateArrayAdapter extends XmlAdapter<String, Coordinate[]>
/*    */ {
/*    */   public String marshal(Coordinate[] v)
/*    */     throws Exception
/*    */   {
/* 33 */     StringBuffer buffer = new StringBuffer();
/* 34 */     for (int i = 0; i < v.length; i++) {
/* 35 */       buffer.append(Double.toString(v[i].x)).append(",").append(Double.toString(v[i].y));
/* 36 */       if (i != v.length - 1) buffer.append(' ');
/*    */     }
/*    */ 
/* 39 */     return buffer.toString();
/*    */   }
/*    */ 
/*    */   public Coordinate[] unmarshal(String v)
/*    */     throws Exception
/*    */   {
/* 49 */     Coordinate[] points = null;
/*    */ 
/* 51 */     if (v != null) {
/* 52 */       String[] pairs = v.split(" ");
/* 53 */       points = new Coordinate[pairs.length];
/*    */ 
/* 55 */       for (int i = 0; i < pairs.length; i++) {
/* 56 */         String[] value = pairs[i].split(",");
/* 57 */         points[i] = new Coordinate(Double.valueOf(value[0]).doubleValue(), Double.valueOf(value[1]).doubleValue());
/*    */       }
/*    */     }
/*    */ 
/* 61 */     return points;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.CoordinateArrayAdapter
 * JD-Core Version:    0.6.2
 */