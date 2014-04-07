/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="", propOrder={"color", "point"})
/*     */ @XmlRootElement(name="Symbol")
/*     */ public class Symbol
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Color", required=true)
/*     */   protected List<Color> color;
/*     */ 
/*     */   @XmlElement(name="Point", required=true)
/*     */   protected Point point;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean clear;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Double sizeScale;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Float lineWidth;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   public List<Color> getColor()
/*     */   {
/*  92 */     if (this.color == null) {
/*  93 */       this.color = new ArrayList();
/*     */     }
/*  95 */     return this.color;
/*     */   }
/*     */ 
/*     */   public Point getPoint()
/*     */   {
/* 107 */     return this.point;
/*     */   }
/*     */ 
/*     */   public void setPoint(Point value)
/*     */   {
/* 119 */     this.point = value;
/*     */   }
/*     */ 
/*     */   public Boolean isClear()
/*     */   {
/* 131 */     return this.clear;
/*     */   }
/*     */ 
/*     */   public void setClear(Boolean value)
/*     */   {
/* 143 */     this.clear = value;
/*     */   }
/*     */ 
/*     */   public Double getSizeScale()
/*     */   {
/* 155 */     return this.sizeScale;
/*     */   }
/*     */ 
/*     */   public void setSizeScale(Double value)
/*     */   {
/* 167 */     this.sizeScale = value;
/*     */   }
/*     */ 
/*     */   public Float getLineWidth()
/*     */   {
/* 179 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */   public void setLineWidth(Float value)
/*     */   {
/* 191 */     this.lineWidth = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 203 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 215 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 227 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 239 */     this.pgenCategory = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Symbol
 * JD-Core Version:    0.6.2
 */