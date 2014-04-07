/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="TrackPoint", propOrder={"time", "location"})
/*     */ public class TrackPoint
/*     */ {
/*     */ 
/*     */   @XmlElement(required=true)
/*     */   protected XMLGregorianCalendar time;
/*     */ 
/*     */   @XmlElement(required=true)
/*     */   protected Location location;
/*     */ 
/*     */   public XMLGregorianCalendar getTime()
/*     */   {
/*  68 */     return this.time;
/*     */   }
/*     */ 
/*     */   public void setTime(XMLGregorianCalendar value)
/*     */   {
/*  80 */     this.time = value;
/*     */   }
/*     */ 
/*     */   public Location getLocation()
/*     */   {
/*  92 */     return this.location;
/*     */   }
/*     */ 
/*     */   public void setLocation(Location value)
/*     */   {
/* 104 */     this.location = value;
/*     */   }
/*     */ 
/*     */   @XmlAccessorType(XmlAccessType.FIELD)
/*     */   @XmlType(name="")
/*     */   public static class Location
/*     */   {
/*     */ 
/*     */     @XmlAttribute
/*     */     protected Double latitude;
/*     */ 
/*     */     @XmlAttribute
/*     */     protected Double longitude;
/*     */ 
/*     */     public Double getLatitude()
/*     */     {
/* 144 */       return this.latitude;
/*     */     }
/*     */ 
/*     */     public void setLatitude(Double value)
/*     */     {
/* 156 */       this.latitude = value;
/*     */     }
/*     */ 
/*     */     public Double getLongitude()
/*     */     {
/* 168 */       return this.longitude;
/*     */     }
/*     */ 
/*     */     public void setLongitude(Double value)
/*     */     {
/* 180 */       this.longitude = value;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.TrackPoint
 * JD-Core Version:    0.6.2
 */