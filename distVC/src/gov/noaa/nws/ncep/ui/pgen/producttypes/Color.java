/*     */ package gov.noaa.nws.ncep.ui.pgen.producttypes;
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
/*     */   protected int green;
/*     */ 
/*     */   @XmlAttribute(required=true)
/*     */   protected int red;
/*     */ 
/*     */   @XmlAttribute(required=true)
/*     */   protected int blue;
/*     */ 
/*     */   public Integer getAlpha()
/*     */   {
/*  61 */     return this.alpha;
/*     */   }
/*     */ 
/*     */   public void setAlpha(Integer value)
/*     */   {
/*  73 */     this.alpha = value;
/*     */   }
/*     */ 
/*     */   public int getGreen()
/*     */   {
/*  81 */     return this.green;
/*     */   }
/*     */ 
/*     */   public void setGreen(int value)
/*     */   {
/*  89 */     this.green = value;
/*     */   }
/*     */ 
/*     */   public int getRed()
/*     */   {
/*  97 */     return this.red;
/*     */   }
/*     */ 
/*     */   public void setRed(int value)
/*     */   {
/* 105 */     this.red = value;
/*     */   }
/*     */ 
/*     */   public int getBlue()
/*     */   {
/* 113 */     return this.blue;
/*     */   }
/*     */ 
/*     */   public void setBlue(int value)
/*     */   {
/* 121 */     this.blue = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.producttypes.Color
 * JD-Core Version:    0.6.2
 */