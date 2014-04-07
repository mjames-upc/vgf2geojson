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
/*     */ @XmlType(name="", propOrder={"color", "point", "textLine"})
/*     */ @XmlRootElement(name="Text")
/*     */ public class Text
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Color", required=true)
/*     */   protected List<Color> color;
/*     */ 
/*     */   @XmlElement(name="Point", required=true)
/*     */   protected Point point;
/*     */ 
/*     */   @XmlElement(required=true)
/*     */   protected List<String> textLine;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean auto;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean hide;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer xOffset;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer yOffset;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String displayType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean mask;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String rotationRelativity;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Double rotation;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String justification;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String style;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String fontName;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Float fontSize;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   public List<Color> getColor()
/*     */   {
/* 123 */     if (this.color == null) {
/* 124 */       this.color = new ArrayList();
/*     */     }
/* 126 */     return this.color;
/*     */   }
/*     */ 
/*     */   public Point getPoint()
/*     */   {
/* 138 */     return this.point;
/*     */   }
/*     */ 
/*     */   public void setPoint(Point value)
/*     */   {
/* 150 */     this.point = value;
/*     */   }
/*     */ 
/*     */   public List<String> getTextLine()
/*     */   {
/* 176 */     if (this.textLine == null) {
/* 177 */       this.textLine = new ArrayList();
/*     */     }
/* 179 */     return this.textLine;
/*     */   }
/*     */ 
/*     */   public Boolean isAuto()
/*     */   {
/* 191 */     return this.auto;
/*     */   }
/*     */ 
/*     */   public void setAuto(Boolean value)
/*     */   {
/* 203 */     this.auto = value;
/*     */   }
/*     */ 
/*     */   public Boolean isHide()
/*     */   {
/* 215 */     return this.hide;
/*     */   }
/*     */ 
/*     */   public void setHide(Boolean value)
/*     */   {
/* 227 */     this.hide = value;
/*     */   }
/*     */ 
/*     */   public Integer getXOffset()
/*     */   {
/* 239 */     return this.xOffset;
/*     */   }
/*     */ 
/*     */   public void setXOffset(Integer value)
/*     */   {
/* 251 */     this.xOffset = value;
/*     */   }
/*     */ 
/*     */   public Integer getYOffset()
/*     */   {
/* 263 */     return this.yOffset;
/*     */   }
/*     */ 
/*     */   public void setYOffset(Integer value)
/*     */   {
/* 275 */     this.yOffset = value;
/*     */   }
/*     */ 
/*     */   public String getDisplayType()
/*     */   {
/* 287 */     return this.displayType;
/*     */   }
/*     */ 
/*     */   public void setDisplayType(String value)
/*     */   {
/* 299 */     this.displayType = value;
/*     */   }
/*     */ 
/*     */   public Boolean isMask()
/*     */   {
/* 311 */     return this.mask;
/*     */   }
/*     */ 
/*     */   public void setMask(Boolean value)
/*     */   {
/* 323 */     this.mask = value;
/*     */   }
/*     */ 
/*     */   public String getRotationRelativity()
/*     */   {
/* 335 */     return this.rotationRelativity;
/*     */   }
/*     */ 
/*     */   public void setRotationRelativity(String value)
/*     */   {
/* 347 */     this.rotationRelativity = value;
/*     */   }
/*     */ 
/*     */   public Double getRotation()
/*     */   {
/* 359 */     return this.rotation;
/*     */   }
/*     */ 
/*     */   public void setRotation(Double value)
/*     */   {
/* 371 */     this.rotation = value;
/*     */   }
/*     */ 
/*     */   public String getJustification()
/*     */   {
/* 383 */     return this.justification;
/*     */   }
/*     */ 
/*     */   public void setJustification(String value)
/*     */   {
/* 395 */     this.justification = value;
/*     */   }
/*     */ 
/*     */   public String getStyle()
/*     */   {
/* 407 */     return this.style;
/*     */   }
/*     */ 
/*     */   public void setStyle(String value)
/*     */   {
/* 419 */     this.style = value;
/*     */   }
/*     */ 
/*     */   public String getFontName()
/*     */   {
/* 431 */     return this.fontName;
/*     */   }
/*     */ 
/*     */   public void setFontName(String value)
/*     */   {
/* 443 */     this.fontName = value;
/*     */   }
/*     */ 
/*     */   public Float getFontSize()
/*     */   {
/* 455 */     return this.fontSize;
/*     */   }
/*     */ 
/*     */   public void setFontSize(Float value)
/*     */   {
/* 467 */     this.fontSize = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 479 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 491 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 503 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 515 */     this.pgenCategory = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Text
 * JD-Core Version:    0.6.2
 */