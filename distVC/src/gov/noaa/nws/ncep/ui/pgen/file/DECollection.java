/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="", propOrder={"drawableElement"})
/*     */ @XmlRootElement(name="DECollection")
/*     */ public class DECollection
/*     */ {
/*     */ 
/*     */   @XmlElement(name="DrawableElement")
/*     */   protected DrawableElement drawableElement;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String collectionName;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   public DrawableElement getDrawableElement()
/*     */   {
/*  66 */     return this.drawableElement;
/*     */   }
/*     */ 
/*     */   public void setDrawableElement(DrawableElement value)
/*     */   {
/*  78 */     this.drawableElement = value;
/*     */   }
/*     */ 
/*     */   public String getCollectionName()
/*     */   {
/*  90 */     return this.collectionName;
/*     */   }
/*     */ 
/*     */   public void setCollectionName(String value)
/*     */   {
/* 102 */     this.collectionName = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 113 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 125 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 136 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 148 */     this.pgenCategory = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.DECollection
 * JD-Core Version:    0.6.2
 */