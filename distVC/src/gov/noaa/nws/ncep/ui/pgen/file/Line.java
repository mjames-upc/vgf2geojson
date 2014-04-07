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
/*     */ @XmlRootElement(name="Line")
/*     */ public class Line
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Color", required=true)
/*     */   protected List<Color> color;
/*     */ 
/*     */   @XmlElement(name="Point", required=true)
/*     */   protected List<Point> point;
/*     */ 
/*     */   @XmlAttribute
/*  62 */   protected boolean flipSide = false;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String fillPattern;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean filled;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean closed;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer smoothFactor;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Double sizeScale;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Float lineWidth;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenType;
/*     */ 
/*     */   public List<Color> getColor()
/*     */   {
/* 103 */     if (this.color == null) {
/* 104 */       this.color = new ArrayList();
/*     */     }
/* 106 */     return this.color;
/*     */   }
/*     */ 
/*     */   public List<Point> getPoint()
/*     */   {
/* 132 */     if (this.point == null) {
/* 133 */       this.point = new ArrayList();
/*     */     }
/* 135 */     return this.point;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 147 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 159 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getFillPattern()
/*     */   {
/* 171 */     return this.fillPattern;
/*     */   }
/*     */ 
/*     */   public void setFillPattern(String value)
/*     */   {
/* 183 */     this.fillPattern = value;
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 195 */     return this.filled;
/*     */   }
/*     */ 
/*     */   public void setFilled(Boolean value)
/*     */   {
/* 207 */     this.filled = value;
/*     */   }
/*     */ 
/*     */   public Boolean isClosed()
/*     */   {
/* 219 */     return this.closed;
/*     */   }
/*     */ 
/*     */   public void setClosed(Boolean value)
/*     */   {
/* 231 */     this.closed = value;
/*     */   }
/*     */ 
/*     */   public Integer getSmoothFactor()
/*     */   {
/* 243 */     return this.smoothFactor;
/*     */   }
/*     */ 
/*     */   public void setSmoothFactor(Integer value)
/*     */   {
/* 255 */     this.smoothFactor = value;
/*     */   }
/*     */ 
/*     */   public Double getSizeScale()
/*     */   {
/* 267 */     return this.sizeScale;
/*     */   }
/*     */ 
/*     */   public void setSizeScale(Double value)
/*     */   {
/* 279 */     this.sizeScale = value;
/*     */   }
/*     */ 
/*     */   public Float getLineWidth()
/*     */   {
/* 291 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */   public void setLineWidth(Float value)
/*     */   {
/* 303 */     this.lineWidth = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 315 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 327 */     this.pgenCategory = value;
/*     */   }
/*     */ 
/*     */   public boolean isFlipSide()
/*     */   {
/* 334 */     return this.flipSide;
/*     */   }
/*     */ 
/*     */   public void setFlipSide(boolean flipSide)
/*     */   {
/* 341 */     this.flipSide = flipSide;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Line
 * JD-Core Version:    0.6.2
 */