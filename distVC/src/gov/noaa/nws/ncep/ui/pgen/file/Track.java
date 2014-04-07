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
/*     */ @XmlType(name="", propOrder={"initialColor", "extrapColor", "initialPoints", "extrapPoints", "extraPointTimeTextDisplayIndicator"})
/*     */ @XmlRootElement(name="Track")
/*     */ public class Track
/*     */ {
/*     */ 
/*     */   @XmlElement(required=true)
/*     */   protected ColorType initialColor;
/*     */ 
/*     */   @XmlElement(required=true)
/*     */   protected ColorType extrapColor;
/*     */ 
/*     */   @XmlElement(required=true)
/*     */   protected List<TrackPoint> initialPoints;
/*     */ 
/*     */   @XmlElement(required=true)
/*     */   protected List<TrackPoint> extrapPoints;
/*     */ 
/*     */   @XmlElement(type=Boolean.class)
/*     */   protected List<Boolean> extraPointTimeTextDisplayIndicator;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String extraPointTimeDisplayOptionName;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String extrapLinePattern;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String extrapMarker;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String fontName;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer fontNameComboSelectedIndex;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Float fontSize;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer fontSizeComboSelectedIndex;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer fontStyleComboSelectedIndex;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String initialLinePattern;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String initialMarker;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer intervalComboSelectedIndex;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String intervalTimeTextString;
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
/*     */   @XmlAttribute
/*     */   protected Boolean setTimeButtonSelected;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String skipFactorTextString;
/*     */ 
/*     */   public ColorType getInitialColor()
/*     */   {
/* 127 */     return this.initialColor;
/*     */   }
/*     */ 
/*     */   public void setInitialColor(ColorType value)
/*     */   {
/* 139 */     this.initialColor = value;
/*     */   }
/*     */ 
/*     */   public ColorType getExtrapColor()
/*     */   {
/* 151 */     return this.extrapColor;
/*     */   }
/*     */ 
/*     */   public void setExtrapColor(ColorType value)
/*     */   {
/* 163 */     this.extrapColor = value;
/*     */   }
/*     */ 
/*     */   public List<TrackPoint> getInitialPoints()
/*     */   {
/* 189 */     if (this.initialPoints == null) {
/* 190 */       this.initialPoints = new ArrayList();
/*     */     }
/* 192 */     return this.initialPoints;
/*     */   }
/*     */ 
/*     */   public List<TrackPoint> getExtrapPoints()
/*     */   {
/* 218 */     if (this.extrapPoints == null) {
/* 219 */       this.extrapPoints = new ArrayList();
/*     */     }
/* 221 */     return this.extrapPoints;
/*     */   }
/*     */ 
/*     */   public List<Boolean> getExtraPointTimeTextDisplayIndicator()
/*     */   {
/* 247 */     if (this.extraPointTimeTextDisplayIndicator == null) {
/* 248 */       this.extraPointTimeTextDisplayIndicator = new ArrayList();
/*     */     }
/* 250 */     return this.extraPointTimeTextDisplayIndicator;
/*     */   }
/*     */ 
/*     */   public String getExtraPointTimeDisplayOptionName()
/*     */   {
/* 262 */     return this.extraPointTimeDisplayOptionName;
/*     */   }
/*     */ 
/*     */   public void setExtraPointTimeDisplayOptionName(String value)
/*     */   {
/* 274 */     this.extraPointTimeDisplayOptionName = value;
/*     */   }
/*     */ 
/*     */   public String getExtrapLinePattern()
/*     */   {
/* 286 */     return this.extrapLinePattern;
/*     */   }
/*     */ 
/*     */   public void setExtrapLinePattern(String value)
/*     */   {
/* 298 */     this.extrapLinePattern = value;
/*     */   }
/*     */ 
/*     */   public String getExtrapMarker()
/*     */   {
/* 310 */     return this.extrapMarker;
/*     */   }
/*     */ 
/*     */   public void setExtrapMarker(String value)
/*     */   {
/* 322 */     this.extrapMarker = value;
/*     */   }
/*     */ 
/*     */   public String getFontName()
/*     */   {
/* 334 */     return this.fontName;
/*     */   }
/*     */ 
/*     */   public void setFontName(String value)
/*     */   {
/* 346 */     this.fontName = value;
/*     */   }
/*     */ 
/*     */   public Integer getFontNameComboSelectedIndex()
/*     */   {
/* 358 */     return this.fontNameComboSelectedIndex;
/*     */   }
/*     */ 
/*     */   public void setFontNameComboSelectedIndex(Integer value)
/*     */   {
/* 370 */     this.fontNameComboSelectedIndex = value;
/*     */   }
/*     */ 
/*     */   public Float getFontSize()
/*     */   {
/* 382 */     return this.fontSize;
/*     */   }
/*     */ 
/*     */   public void setFontSize(Float value)
/*     */   {
/* 394 */     this.fontSize = value;
/*     */   }
/*     */ 
/*     */   public Integer getFontSizeComboSelectedIndex()
/*     */   {
/* 406 */     return this.fontSizeComboSelectedIndex;
/*     */   }
/*     */ 
/*     */   public void setFontSizeComboSelectedIndex(Integer value)
/*     */   {
/* 418 */     this.fontSizeComboSelectedIndex = value;
/*     */   }
/*     */ 
/*     */   public Integer getFontStyleComboSelectedIndex()
/*     */   {
/* 430 */     return this.fontStyleComboSelectedIndex;
/*     */   }
/*     */ 
/*     */   public void setFontStyleComboSelectedIndex(Integer value)
/*     */   {
/* 442 */     this.fontStyleComboSelectedIndex = value;
/*     */   }
/*     */ 
/*     */   public String getInitialLinePattern()
/*     */   {
/* 454 */     return this.initialLinePattern;
/*     */   }
/*     */ 
/*     */   public void setInitialLinePattern(String value)
/*     */   {
/* 466 */     this.initialLinePattern = value;
/*     */   }
/*     */ 
/*     */   public String getInitialMarker()
/*     */   {
/* 478 */     return this.initialMarker;
/*     */   }
/*     */ 
/*     */   public void setInitialMarker(String value)
/*     */   {
/* 490 */     this.initialMarker = value;
/*     */   }
/*     */ 
/*     */   public Integer getIntervalComboSelectedIndex()
/*     */   {
/* 502 */     return this.intervalComboSelectedIndex;
/*     */   }
/*     */ 
/*     */   public void setIntervalComboSelectedIndex(Integer value)
/*     */   {
/* 514 */     this.intervalComboSelectedIndex = value;
/*     */   }
/*     */ 
/*     */   public String getIntervalTimeTextString()
/*     */   {
/* 526 */     return this.intervalTimeTextString;
/*     */   }
/*     */ 
/*     */   public void setIntervalTimeTextString(String value)
/*     */   {
/* 538 */     this.intervalTimeTextString = value;
/*     */   }
/*     */ 
/*     */   public Float getLineWidth()
/*     */   {
/* 550 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */   public void setLineWidth(Float value)
/*     */   {
/* 562 */     this.lineWidth = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 574 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 586 */     this.pgenCategory = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 598 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 610 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public Boolean isSetTimeButtonSelected()
/*     */   {
/* 622 */     return this.setTimeButtonSelected;
/*     */   }
/*     */ 
/*     */   public void setSetTimeButtonSelected(Boolean value)
/*     */   {
/* 634 */     this.setTimeButtonSelected = value;
/*     */   }
/*     */ 
/*     */   public String getSkipFactorTextString()
/*     */   {
/* 646 */     return this.skipFactorTextString;
/*     */   }
/*     */ 
/*     */   public void setSkipFactorTextString(String value)
/*     */   {
/* 658 */     this.skipFactorTextString = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Track
 * JD-Core Version:    0.6.2
 */