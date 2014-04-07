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
/*     */ @XmlRootElement(name="AvnText")
/*     */ public class AvnText
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Color", required=true)
/*     */   protected List<Color> color;
/*     */ 
/*     */   @XmlElement(name="Point", required=true)
/*     */   protected Point point;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String avnTextType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String topValue;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String bottomValue;
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
/*     */   protected String symbolPatternName;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   public List<Color> getColor()
/*     */   {
/* 107 */     if (this.color == null) {
/* 108 */       this.color = new ArrayList();
/*     */     }
/* 110 */     return this.color;
/*     */   }
/*     */ 
/*     */   public Point getPoint()
/*     */   {
/* 122 */     return this.point;
/*     */   }
/*     */ 
/*     */   public void setPoint(Point value)
/*     */   {
/* 134 */     this.point = value;
/*     */   }
/*     */ 
/*     */   public String getAvnTextType()
/*     */   {
/* 146 */     return this.avnTextType;
/*     */   }
/*     */ 
/*     */   public void setAvnTextType(String value)
/*     */   {
/* 158 */     this.avnTextType = value;
/*     */   }
/*     */ 
/*     */   public String getTopValue()
/*     */   {
/* 170 */     return this.topValue;
/*     */   }
/*     */ 
/*     */   public void setTopValue(String value)
/*     */   {
/* 182 */     this.topValue = value;
/*     */   }
/*     */ 
/*     */   public String getBottomValue()
/*     */   {
/* 194 */     return this.bottomValue;
/*     */   }
/*     */ 
/*     */   public void setBottomValue(String value)
/*     */   {
/* 206 */     this.bottomValue = value;
/*     */   }
/*     */ 
/*     */   public String getJustification()
/*     */   {
/* 218 */     return this.justification;
/*     */   }
/*     */ 
/*     */   public void setJustification(String value)
/*     */   {
/* 230 */     this.justification = value;
/*     */   }
/*     */ 
/*     */   public String getStyle()
/*     */   {
/* 242 */     return this.style;
/*     */   }
/*     */ 
/*     */   public void setStyle(String value)
/*     */   {
/* 254 */     this.style = value;
/*     */   }
/*     */ 
/*     */   public String getFontName()
/*     */   {
/* 266 */     return this.fontName;
/*     */   }
/*     */ 
/*     */   public void setFontName(String value)
/*     */   {
/* 278 */     this.fontName = value;
/*     */   }
/*     */ 
/*     */   public Float getFontSize()
/*     */   {
/* 290 */     return this.fontSize;
/*     */   }
/*     */ 
/*     */   public void setFontSize(Float value)
/*     */   {
/* 302 */     this.fontSize = value;
/*     */   }
/*     */ 
/*     */   public String getSymbolPatternName()
/*     */   {
/* 314 */     return this.symbolPatternName;
/*     */   }
/*     */ 
/*     */   public void setSymbolPatternName(String value)
/*     */   {
/* 326 */     this.symbolPatternName = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 338 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 350 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 362 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 374 */     this.pgenCategory = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.AvnText
 * JD-Core Version:    0.6.2
 */