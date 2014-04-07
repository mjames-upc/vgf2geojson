/*    */ package gov.noaa.nws.ncep.ui.pgen.file;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlType(name="")
/*    */ @XmlRootElement(name="Point")
/*    */ public class Point
/*    */ {
/*    */ 
/*    */   @XmlAttribute(name="Lon")
/*    */   protected Double lon;
/*    */ 
/*    */   @XmlAttribute(name="Lat")
/*    */   protected Double lat;
/*    */ 
/*    */   public Double getLon()
/*    */   {
/* 55 */     return this.lon;
/*    */   }
/*    */ 
/*    */   public void setLon(Double value)
/*    */   {
/* 67 */     this.lon = value;
/*    */   }
/*    */ 
/*    */   public Double getLat()
/*    */   {
/* 79 */     return this.lat;
/*    */   }
/*    */ 
/*    */   public void setLat(Double value)
/*    */   {
/* 91 */     this.lat = value;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Point
 * JD-Core Version:    0.6.2
 */