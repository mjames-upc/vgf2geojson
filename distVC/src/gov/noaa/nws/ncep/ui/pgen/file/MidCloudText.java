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
/*     */ @XmlRootElement(name="MidCloudText")
/*     */ public class MidCloudText
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Color", required=true)
/*     */   protected List<Color> color;
/*     */ 
/*     */   @XmlElement(name="Point", required=true)
/*     */   protected Point point;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String cloudTypes;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String cloudAmounts;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String turbulenceType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String turbulenceLevels;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String icingType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String icingLevels;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String tstormTypes;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String tstormLevels;
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
/* 119 */     if (this.color == null) {
/* 120 */       this.color = new ArrayList();
/*     */     }
/* 122 */     return this.color;
/*     */   }
/*     */ 
/*     */   public Point getPoint()
/*     */   {
/* 134 */     return this.point;
/*     */   }
/*     */ 
/*     */   public void setPoint(Point value)
/*     */   {
/* 146 */     this.point = value;
/*     */   }
/*     */ 
/*     */   public String getCloudTypes()
/*     */   {
/* 158 */     return this.cloudTypes;
/*     */   }
/*     */ 
/*     */   public void setCloudTypes(String value)
/*     */   {
/* 170 */     this.cloudTypes = value;
/*     */   }
/*     */ 
/*     */   public String getCloudAmounts()
/*     */   {
/* 182 */     return this.cloudAmounts;
/*     */   }
/*     */ 
/*     */   public void setCloudAmounts(String value)
/*     */   {
/* 194 */     this.cloudAmounts = value;
/*     */   }
/*     */ 
/*     */   public String getTurbulenceType()
/*     */   {
/* 206 */     return this.turbulenceType;
/*     */   }
/*     */ 
/*     */   public void setTurbulenceType(String value)
/*     */   {
/* 218 */     this.turbulenceType = value;
/*     */   }
/*     */ 
/*     */   public String getTurbulenceLevels()
/*     */   {
/* 230 */     return this.turbulenceLevels;
/*     */   }
/*     */ 
/*     */   public void setTurbulenceLevels(String value)
/*     */   {
/* 242 */     this.turbulenceLevels = value;
/*     */   }
/*     */ 
/*     */   public String getIcingType()
/*     */   {
/* 254 */     return this.icingType;
/*     */   }
/*     */ 
/*     */   public void setIcingType(String value)
/*     */   {
/* 266 */     this.icingType = value;
/*     */   }
/*     */ 
/*     */   public String getIcingLevels()
/*     */   {
/* 278 */     return this.icingLevels;
/*     */   }
/*     */ 
/*     */   public void setIcingLevels(String value)
/*     */   {
/* 290 */     this.icingLevels = value;
/*     */   }
/*     */ 
/*     */   public String getTstormTypes()
/*     */   {
/* 302 */     return this.tstormTypes;
/*     */   }
/*     */ 
/*     */   public void setTstormTypes(String value)
/*     */   {
/* 314 */     this.tstormTypes = value;
/*     */   }
/*     */ 
/*     */   public String getTstormLevels()
/*     */   {
/* 326 */     return this.tstormLevels;
/*     */   }
/*     */ 
/*     */   public void setTstormLevels(String value)
/*     */   {
/* 338 */     this.tstormLevels = value;
/*     */   }
/*     */ 
/*     */   public String getJustification()
/*     */   {
/* 350 */     return this.justification;
/*     */   }
/*     */ 
/*     */   public void setJustification(String value)
/*     */   {
/* 362 */     this.justification = value;
/*     */   }
/*     */ 
/*     */   public String getStyle()
/*     */   {
/* 374 */     return this.style;
/*     */   }
/*     */ 
/*     */   public void setStyle(String value)
/*     */   {
/* 386 */     this.style = value;
/*     */   }
/*     */ 
/*     */   public String getFontName()
/*     */   {
/* 398 */     return this.fontName;
/*     */   }
/*     */ 
/*     */   public void setFontName(String value)
/*     */   {
/* 410 */     this.fontName = value;
/*     */   }
/*     */ 
/*     */   public Float getFontSize()
/*     */   {
/* 422 */     return this.fontSize;
/*     */   }
/*     */ 
/*     */   public void setFontSize(Float value)
/*     */   {
/* 434 */     this.fontSize = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 446 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 458 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 470 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 482 */     this.pgenCategory = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.MidCloudText
 * JD-Core Version:    0.6.2
 */