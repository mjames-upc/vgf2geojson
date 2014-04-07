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
/*     */ @XmlRootElement(name="Arc")
/*     */ public class Arc
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Color", required=true)
/*     */   protected List<Color> color;
/*     */ 
/*     */   @XmlElement(name="Point", required=true)
/*     */   protected List<Point> point;
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
/*     */   protected String linePattern;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Float lineWidth;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Double axisRatio;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Double startAngle;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Double endAngle;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   public List<Color> getColor()
/*     */   {
/* 113 */     if (this.color == null) {
/* 114 */       this.color = new ArrayList();
/*     */     }
/* 116 */     return this.color;
/*     */   }
/*     */ 
/*     */   public List<Point> getPoint()
/*     */   {
/* 142 */     if (this.point == null) {
/* 143 */       this.point = new ArrayList();
/*     */     }
/* 145 */     return this.point;
/*     */   }
/*     */ 
/*     */   public String getFillPattern()
/*     */   {
/* 157 */     return this.fillPattern;
/*     */   }
/*     */ 
/*     */   public void setFillPattern(String value)
/*     */   {
/* 169 */     this.fillPattern = value;
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 181 */     return this.filled;
/*     */   }
/*     */ 
/*     */   public void setFilled(Boolean value)
/*     */   {
/* 193 */     this.filled = value;
/*     */   }
/*     */ 
/*     */   public Boolean isClosed()
/*     */   {
/* 205 */     return this.closed;
/*     */   }
/*     */ 
/*     */   public void setClosed(Boolean value)
/*     */   {
/* 217 */     this.closed = value;
/*     */   }
/*     */ 
/*     */   public Integer getSmoothFactor()
/*     */   {
/* 229 */     return this.smoothFactor;
/*     */   }
/*     */ 
/*     */   public void setSmoothFactor(Integer value)
/*     */   {
/* 241 */     this.smoothFactor = value;
/*     */   }
/*     */ 
/*     */   public Double getSizeScale()
/*     */   {
/* 253 */     return this.sizeScale;
/*     */   }
/*     */ 
/*     */   public void setSizeScale(Double value)
/*     */   {
/* 265 */     this.sizeScale = value;
/*     */   }
/*     */ 
/*     */   public String getLinePattern()
/*     */   {
/* 277 */     return this.linePattern;
/*     */   }
/*     */ 
/*     */   public void setLinePattern(String value)
/*     */   {
/* 289 */     this.linePattern = value;
/*     */   }
/*     */ 
/*     */   public Float getLineWidth()
/*     */   {
/* 301 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */   public void setLineWidth(Float value)
/*     */   {
/* 313 */     this.lineWidth = value;
/*     */   }
/*     */ 
/*     */   public Double getAxisRatio()
/*     */   {
/* 325 */     return this.axisRatio;
/*     */   }
/*     */ 
/*     */   public void setAxisRatio(Double value)
/*     */   {
/* 337 */     this.axisRatio = value;
/*     */   }
/*     */ 
/*     */   public Double getStartAngle()
/*     */   {
/* 349 */     return this.startAngle;
/*     */   }
/*     */ 
/*     */   public void setStartAngle(Double value)
/*     */   {
/* 361 */     this.startAngle = value;
/*     */   }
/*     */ 
/*     */   public Double getEndAngle()
/*     */   {
/* 373 */     return this.endAngle;
/*     */   }
/*     */ 
/*     */   public void setEndAngle(Double value)
/*     */   {
/* 385 */     this.endAngle = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 397 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 409 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 421 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 433 */     this.pgenCategory = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Arc
 * JD-Core Version:    0.6.2
 */