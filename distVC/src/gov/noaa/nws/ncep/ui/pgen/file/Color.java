/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="")
/*     */ @XmlRootElement(name="Color")
/*     */ public class Color
/*     */ {
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer alpha;
/*     */ 
/*     */   @XmlAttribute(required=true)
/*     */   protected int blue;
/*     */ 
/*     */   @XmlAttribute(required=true)
/*     */   protected int green;
/*     */ 
/*     */   @XmlAttribute(required=true)
/*     */   protected int red;
/*     */ 
/*     */   public Integer getAlpha()
/*     */   {
/*  89 */     return this.alpha;
/*     */   }
/*     */ 
/*     */   public void setAlpha(Integer value)
/*     */   {
/* 101 */     this.alpha = value;
/*     */   }
/*     */ 
/*     */   public int getBlue()
/*     */   {
/* 109 */     return this.blue;
/*     */   }
/*     */ 
/*     */   public void setBlue(int value)
/*     */   {
/* 117 */     this.blue = value;
/*     */   }
/*     */ 
/*     */   public int getGreen()
/*     */   {
/* 125 */     return this.green;
/*     */   }
/*     */ 
/*     */   public void setGreen(int value)
/*     */   {
/* 133 */     this.green = value;
/*     */   }
/*     */ 
/*     */   public int getRed()
/*     */   {
/* 141 */     return this.red;
/*     */   }
/*     */ 
/*     */   public void setRed(int value)
/*     */   {
/* 149 */     this.red = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Color
 * JD-Core Version:    0.6.2
 */