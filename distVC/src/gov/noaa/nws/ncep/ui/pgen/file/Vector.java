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
/*     */ @XmlRootElement(name="Vector")
/*     */ public class Vector
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
/*     */   protected Boolean directionOnly;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Double arrowHeadSize;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Double speed;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Double direction;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   public List<Color> getColor()
/*     */   {
/* 104 */     if (this.color == null) {
/* 105 */       this.color = new ArrayList();
/*     */     }
/* 107 */     return this.color;
/*     */   }
/*     */ 
/*     */   public Point getPoint()
/*     */   {
/* 119 */     return this.point;
/*     */   }
/*     */ 
/*     */   public void setPoint(Point value)
/*     */   {
/* 131 */     this.point = value;
/*     */   }
/*     */ 
/*     */   public Boolean isClear()
/*     */   {
/* 143 */     return this.clear;
/*     */   }
/*     */ 
/*     */   public void setClear(Boolean value)
/*     */   {
/* 155 */     this.clear = value;
/*     */   }
/*     */ 
/*     */   public Double getSizeScale()
/*     */   {
/* 167 */     return this.sizeScale;
/*     */   }
/*     */ 
/*     */   public void setSizeScale(Double value)
/*     */   {
/* 179 */     this.sizeScale = value;
/*     */   }
/*     */ 
/*     */   public Float getLineWidth()
/*     */   {
/* 191 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */   public void setLineWidth(Float value)
/*     */   {
/* 203 */     this.lineWidth = value;
/*     */   }
/*     */ 
/*     */   public Boolean isDirectionOnly()
/*     */   {
/* 215 */     return this.directionOnly;
/*     */   }
/*     */ 
/*     */   public void setDirectionOnly(Boolean value)
/*     */   {
/* 227 */     this.directionOnly = value;
/*     */   }
/*     */ 
/*     */   public Double getArrowHeadSize()
/*     */   {
/* 239 */     return this.arrowHeadSize;
/*     */   }
/*     */ 
/*     */   public void setArrowHeadSize(Double value)
/*     */   {
/* 251 */     this.arrowHeadSize = value;
/*     */   }
/*     */ 
/*     */   public Double getSpeed()
/*     */   {
/* 263 */     return this.speed;
/*     */   }
/*     */ 
/*     */   public void setSpeed(Double value)
/*     */   {
/* 275 */     this.speed = value;
/*     */   }
/*     */ 
/*     */   public Double getDirection()
/*     */   {
/* 287 */     return this.direction;
/*     */   }
/*     */ 
/*     */   public void setDirection(Double value)
/*     */   {
/* 299 */     this.direction = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 311 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 323 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 335 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 347 */     this.pgenCategory = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Vector
 * JD-Core Version:    0.6.2
 */