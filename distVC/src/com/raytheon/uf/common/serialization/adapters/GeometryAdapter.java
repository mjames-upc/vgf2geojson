/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.vividsolutions.jts.geom.Geometry;
/*    */ import com.vividsolutions.jts.io.WKTReader;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ public class GeometryAdapter extends XmlAdapter<String, Geometry>
/*    */ {
/*    */   public String marshal(Geometry v)
/*    */     throws Exception
/*    */   {
/* 46 */     return v.toString();
/*    */   }
/*    */ 
/*    */   public Geometry unmarshal(String v) throws Exception
/*    */   {
/* 51 */     return new WKTReader().read(v);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.GeometryAdapter
 * JD-Core Version:    0.6.2
 */