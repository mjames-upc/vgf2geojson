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
/*     */ @XmlRootElement(name="Spenes")
/*     */ public class Spenes
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Color", required=true)
/*     */   protected List<Color> color;
/*     */ 
/*     */   @XmlElement(name="Point", required=true)
/*     */   protected List<Point> point;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected float lineWidth;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected int smoothLevel;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String stateZ000;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String initDateTime;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String latestDataUsed;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected int obsHr;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String forecasters;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String location;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String attnWFOs;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String attnRFCs;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String event;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String satAnalysisTrends;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected int shortTermBegin;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected int shortTermEnd;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String outLookLevel;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String addlInfo;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String latlon;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String product;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   public List<Color> getColor()
/*     */   {
/* 133 */     if (this.color == null) {
/* 134 */       this.color = new ArrayList();
/*     */     }
/* 136 */     return this.color;
/*     */   }
/*     */ 
/*     */   public List<Point> getPoint()
/*     */   {
/* 162 */     if (this.point == null) {
/* 163 */       this.point = new ArrayList();
/*     */     }
/* 165 */     return this.point;
/*     */   }
/*     */ 
/*     */   public Float getLineWidth()
/*     */   {
/* 177 */     return Float.valueOf(this.lineWidth);
/*     */   }
/*     */ 
/*     */   public void setLineWidth(Float value)
/*     */   {
/* 189 */     this.lineWidth = value.floatValue();
/*     */   }
/*     */ 
/*     */   public Integer getSmoothLevel()
/*     */   {
/* 201 */     return Integer.valueOf(this.smoothLevel);
/*     */   }
/*     */ 
/*     */   public void setSmoothLevel(Integer value)
/*     */   {
/* 213 */     this.smoothLevel = value.intValue();
/*     */   }
/*     */ 
/*     */   public String getStateZ0000()
/*     */   {
/* 225 */     return this.stateZ000;
/*     */   }
/*     */ 
/*     */   public void setStateZ000(String stateZ000)
/*     */   {
/* 238 */     this.stateZ000 = stateZ000;
/*     */   }
/*     */ 
/*     */   public String getInitDateTime()
/*     */   {
/* 250 */     return this.initDateTime;
/*     */   }
/*     */ 
/*     */   public void setInitDateTime(String value)
/*     */   {
/* 262 */     this.initDateTime = value;
/*     */   }
/*     */ 
/*     */   public String getLatestDataUsed()
/*     */   {
/* 274 */     return this.latestDataUsed;
/*     */   }
/*     */ 
/*     */   public void setLatestDataUsed(String value)
/*     */   {
/* 286 */     this.latestDataUsed = value;
/*     */   }
/*     */ 
/*     */   public int getObsHr()
/*     */   {
/* 298 */     return this.obsHr;
/*     */   }
/*     */ 
/*     */   public void setObsHr(int value)
/*     */   {
/* 310 */     this.obsHr = value;
/*     */   }
/*     */ 
/*     */   public String getForecasters()
/*     */   {
/* 322 */     return this.forecasters;
/*     */   }
/*     */ 
/*     */   public void setForecasters(String value)
/*     */   {
/* 334 */     this.forecasters = value;
/*     */   }
/*     */ 
/*     */   public String getLocation()
/*     */   {
/* 346 */     return this.location;
/*     */   }
/*     */ 
/*     */   public void setLocation(String value)
/*     */   {
/* 358 */     this.location = value;
/*     */   }
/*     */ 
/*     */   public String getAttnWFOs()
/*     */   {
/* 370 */     return this.attnWFOs;
/*     */   }
/*     */ 
/*     */   public void setAttnWFOs(String value)
/*     */   {
/* 382 */     this.attnWFOs = value;
/*     */   }
/*     */ 
/*     */   public String getAttnRFCs()
/*     */   {
/* 394 */     return this.attnRFCs;
/*     */   }
/*     */ 
/*     */   public void setAttnRFCs(String value)
/*     */   {
/* 406 */     this.attnRFCs = value;
/*     */   }
/*     */ 
/*     */   public String getEvent()
/*     */   {
/* 418 */     return this.event;
/*     */   }
/*     */ 
/*     */   public void setEvent(String value)
/*     */   {
/* 430 */     this.event = value;
/*     */   }
/*     */ 
/*     */   public String getSatAnalysisTrends()
/*     */   {
/* 442 */     return this.satAnalysisTrends;
/*     */   }
/*     */ 
/*     */   public void setSatAnalysisTrends(String value)
/*     */   {
/* 454 */     this.satAnalysisTrends = value;
/*     */   }
/*     */ 
/*     */   public int getShortTermBegin()
/*     */   {
/* 466 */     return this.shortTermBegin;
/*     */   }
/*     */ 
/*     */   public void setShortTermBegin(int value)
/*     */   {
/* 478 */     this.shortTermBegin = value;
/*     */   }
/*     */ 
/*     */   public int getShortTermEnd()
/*     */   {
/* 490 */     return this.shortTermEnd;
/*     */   }
/*     */ 
/*     */   public void setShortTermEnd(int value)
/*     */   {
/* 502 */     this.shortTermEnd = value;
/*     */   }
/*     */ 
/*     */   public String getOutLookLevel()
/*     */   {
/* 515 */     return this.outLookLevel;
/*     */   }
/*     */ 
/*     */   public void setOutLookLevel(String value)
/*     */   {
/* 527 */     this.outLookLevel = value;
/*     */   }
/*     */ 
/*     */   public String getAddlInfo()
/*     */   {
/* 539 */     return this.addlInfo;
/*     */   }
/*     */ 
/*     */   public void setAddlInfo(String value)
/*     */   {
/* 551 */     this.addlInfo = value;
/*     */   }
/*     */ 
/*     */   public String getLatlon()
/*     */   {
/* 563 */     return this.latlon;
/*     */   }
/*     */ 
/*     */   public void setLatlon(String value)
/*     */   {
/* 575 */     this.latlon = value;
/*     */   }
/*     */ 
/*     */   public String getProduct() {
/* 579 */     return this.product;
/*     */   }
/*     */ 
/*     */   public void setProduct(String value)
/*     */   {
/* 591 */     this.product = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 603 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 615 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 627 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 639 */     this.pgenCategory = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Spenes
 * JD-Core Version:    0.6.2
 */