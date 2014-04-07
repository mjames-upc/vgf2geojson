/*      */ package gov.noaa.nws.ncep.ui.pgen.file;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import javax.xml.bind.annotation.XmlAccessType;
/*      */ import javax.xml.bind.annotation.XmlAccessorType;
/*      */ import javax.xml.bind.annotation.XmlAttribute;
/*      */ import javax.xml.bind.annotation.XmlElement;
/*      */ import javax.xml.bind.annotation.XmlRootElement;
/*      */ import javax.xml.bind.annotation.XmlType;
/*      */ 
/*      */ @XmlAccessorType(XmlAccessType.FIELD)
/*      */ @XmlType(name="", propOrder={"color", "point"})
/*      */ @XmlRootElement(name="Gfa")
/*      */ public class Gfa
/*      */ {
/*      */ 
/*      */   @XmlElement(name="Color", required=true)
/*      */   protected List<Color> color;
/*      */ 
/*      */   @XmlElement(name="Point", required=true)
/*      */   protected List<Point> point;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String otlkCondsEndg;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String otlkCondsDvlpg;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String condsContg;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String fromCondsEndg;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String fromCondsDvlpg;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String airmetTag;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String outlookEndTime;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String untilTime;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String issueTime;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String textVor;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String otherValues;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String contour;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String gr;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String frequency;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String tsCategory;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String fzlRange;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String level;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String intensity;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String speed;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String dueTo;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String lyr;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String coverage;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String fzlTopBottom;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String cig;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String vis;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String bottom;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String top;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String states;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String ending;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String beginning;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String area;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String type;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer cycleHour;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer cycleDay;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Boolean isOutlook;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String issueType;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String desk;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String tag;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String fcstHr;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String hazard;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Double lonText;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Double latText;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String fillPattern;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Boolean filled;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Boolean closed;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer smoothFactor;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Double sizeScale;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Float lineWidth;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String pgenType;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String pgenCategory;
/*      */ 
/*      */   public List<Color> getColor()
/*      */   {
/*  225 */     if (this.color == null) {
/*  226 */       this.color = new ArrayList();
/*      */     }
/*  228 */     return this.color;
/*      */   }
/*      */ 
/*      */   public List<Point> getPoint()
/*      */   {
/*  254 */     if (this.point == null) {
/*  255 */       this.point = new ArrayList();
/*      */     }
/*  257 */     return this.point;
/*      */   }
/*      */ 
/*      */   public String getOtlkCondsEndg()
/*      */   {
/*  269 */     return this.otlkCondsEndg;
/*      */   }
/*      */ 
/*      */   public void setOtlkCondsEndg(String value)
/*      */   {
/*  281 */     this.otlkCondsEndg = value;
/*      */   }
/*      */ 
/*      */   public String getOtlkCondsDvlpg()
/*      */   {
/*  293 */     return this.otlkCondsDvlpg;
/*      */   }
/*      */ 
/*      */   public void setOtlkCondsDvlpg(String value)
/*      */   {
/*  305 */     this.otlkCondsDvlpg = value;
/*      */   }
/*      */ 
/*      */   public String getCondsContg()
/*      */   {
/*  317 */     return this.condsContg;
/*      */   }
/*      */ 
/*      */   public void setCondsContg(String value)
/*      */   {
/*  329 */     this.condsContg = value;
/*      */   }
/*      */ 
/*      */   public String getFromCondsEndg()
/*      */   {
/*  341 */     return this.fromCondsEndg;
/*      */   }
/*      */ 
/*      */   public void setFromCondsEndg(String value)
/*      */   {
/*  353 */     this.fromCondsEndg = value;
/*      */   }
/*      */ 
/*      */   public String getFromCondsDvlpg()
/*      */   {
/*  365 */     return this.fromCondsDvlpg;
/*      */   }
/*      */ 
/*      */   public void setFromCondsDvlpg(String value)
/*      */   {
/*  377 */     this.fromCondsDvlpg = value;
/*      */   }
/*      */ 
/*      */   public String getAirmetTag()
/*      */   {
/*  389 */     return this.airmetTag;
/*      */   }
/*      */ 
/*      */   public void setAirmetTag(String value)
/*      */   {
/*  401 */     this.airmetTag = value;
/*      */   }
/*      */ 
/*      */   public String getOutlookEndTime()
/*      */   {
/*  412 */     return this.outlookEndTime;
/*      */   }
/*      */ 
/*      */   public void setOutlookEndTime(String value)
/*      */   {
/*  424 */     this.outlookEndTime = value;
/*      */   }
/*      */ 
/*      */   public String getUntilTime()
/*      */   {
/*  435 */     return this.untilTime;
/*      */   }
/*      */ 
/*      */   public void setUntilTime(String value)
/*      */   {
/*  447 */     this.untilTime = value;
/*      */   }
/*      */ 
/*      */   public String getIssueTime()
/*      */   {
/*  459 */     return this.issueTime;
/*      */   }
/*      */ 
/*      */   public void setIssueTime(String value)
/*      */   {
/*  471 */     this.issueTime = value;
/*      */   }
/*      */ 
/*      */   public String getTextVor()
/*      */   {
/*  483 */     return this.textVor;
/*      */   }
/*      */ 
/*      */   public void setTextVor(String value)
/*      */   {
/*  495 */     this.textVor = value;
/*      */   }
/*      */ 
/*      */   public String getOtherValues()
/*      */   {
/*  507 */     return this.otherValues;
/*      */   }
/*      */ 
/*      */   public void setOtherValues(String value)
/*      */   {
/*  519 */     this.otherValues = value;
/*      */   }
/*      */ 
/*      */   public String getContour()
/*      */   {
/*  531 */     return this.contour;
/*      */   }
/*      */ 
/*      */   public void setContour(String value)
/*      */   {
/*  543 */     this.contour = value;
/*      */   }
/*      */ 
/*      */   public String getGr()
/*      */   {
/*  555 */     return this.gr;
/*      */   }
/*      */ 
/*      */   public void setGr(String value)
/*      */   {
/*  567 */     this.gr = value;
/*      */   }
/*      */ 
/*      */   public String getFrequency()
/*      */   {
/*  579 */     return this.frequency;
/*      */   }
/*      */ 
/*      */   public void setFrequency(String value)
/*      */   {
/*  591 */     this.frequency = value;
/*      */   }
/*      */ 
/*      */   public String getTsCategory()
/*      */   {
/*  603 */     return this.tsCategory;
/*      */   }
/*      */ 
/*      */   public void setTsCategory(String value)
/*      */   {
/*  615 */     this.tsCategory = value;
/*      */   }
/*      */ 
/*      */   public String getFzlRange()
/*      */   {
/*  627 */     return this.fzlRange;
/*      */   }
/*      */ 
/*      */   public void setFzlRange(String value)
/*      */   {
/*  639 */     this.fzlRange = value;
/*      */   }
/*      */ 
/*      */   public String getLevel()
/*      */   {
/*  651 */     return this.level;
/*      */   }
/*      */ 
/*      */   public void setLevel(String value)
/*      */   {
/*  663 */     this.level = value;
/*      */   }
/*      */ 
/*      */   public String getIntensity()
/*      */   {
/*  675 */     return this.intensity;
/*      */   }
/*      */ 
/*      */   public void setIntensity(String value)
/*      */   {
/*  687 */     this.intensity = value;
/*      */   }
/*      */ 
/*      */   public String getSpeed()
/*      */   {
/*  699 */     return this.speed;
/*      */   }
/*      */ 
/*      */   public void setSpeed(String value)
/*      */   {
/*  711 */     this.speed = value;
/*      */   }
/*      */ 
/*      */   public String getDueTo()
/*      */   {
/*  723 */     return this.dueTo;
/*      */   }
/*      */ 
/*      */   public void setDueTo(String value)
/*      */   {
/*  735 */     this.dueTo = value;
/*      */   }
/*      */ 
/*      */   public String getLyr()
/*      */   {
/*  747 */     return this.lyr;
/*      */   }
/*      */ 
/*      */   public void setLyr(String value)
/*      */   {
/*  759 */     this.lyr = value;
/*      */   }
/*      */ 
/*      */   public String getCoverage()
/*      */   {
/*  771 */     return this.coverage;
/*      */   }
/*      */ 
/*      */   public void setCoverage(String value)
/*      */   {
/*  783 */     this.coverage = value;
/*      */   }
/*      */ 
/*      */   public String getFzlTopBottom()
/*      */   {
/*  795 */     return this.fzlTopBottom;
/*      */   }
/*      */ 
/*      */   public void setFzlTopBottom(String value)
/*      */   {
/*  807 */     this.fzlTopBottom = value;
/*      */   }
/*      */ 
/*      */   public String getCig()
/*      */   {
/*  819 */     return this.cig;
/*      */   }
/*      */ 
/*      */   public void setCig(String value)
/*      */   {
/*  831 */     this.cig = value;
/*      */   }
/*      */ 
/*      */   public String getVis()
/*      */   {
/*  842 */     return this.vis;
/*      */   }
/*      */ 
/*      */   public void setVis(String value)
/*      */   {
/*  854 */     this.vis = value;
/*      */   }
/*      */ 
/*      */   public String getBottom()
/*      */   {
/*  865 */     return this.bottom;
/*      */   }
/*      */ 
/*      */   public void setBottom(String value)
/*      */   {
/*  877 */     this.bottom = value;
/*      */   }
/*      */ 
/*      */   public String getTop()
/*      */   {
/*  889 */     return this.top;
/*      */   }
/*      */ 
/*      */   public void setTop(String value)
/*      */   {
/*  901 */     this.top = value;
/*      */   }
/*      */ 
/*      */   public String getStates()
/*      */   {
/*  913 */     return this.states;
/*      */   }
/*      */ 
/*      */   public void setStates(String value)
/*      */   {
/*  925 */     this.states = value;
/*      */   }
/*      */ 
/*      */   public String getEnding()
/*      */   {
/*  937 */     return this.ending;
/*      */   }
/*      */ 
/*      */   public void setEnding(String value)
/*      */   {
/*  949 */     this.ending = value;
/*      */   }
/*      */ 
/*      */   public String getBeginning()
/*      */   {
/*  961 */     return this.beginning;
/*      */   }
/*      */ 
/*      */   public void setBeginning(String value)
/*      */   {
/*  973 */     this.beginning = value;
/*      */   }
/*      */ 
/*      */   public String getArea()
/*      */   {
/*  985 */     return this.area;
/*      */   }
/*      */ 
/*      */   public void setArea(String value)
/*      */   {
/*  997 */     this.area = value;
/*      */   }
/*      */ 
/*      */   public String getType()
/*      */   {
/* 1009 */     return this.type;
/*      */   }
/*      */ 
/*      */   public void setType(String value)
/*      */   {
/* 1021 */     this.type = value;
/*      */   }
/*      */ 
/*      */   public Integer getCycleHour()
/*      */   {
/* 1033 */     return this.cycleHour;
/*      */   }
/*      */ 
/*      */   public void setCycleHour(Integer value)
/*      */   {
/* 1045 */     this.cycleHour = value;
/*      */   }
/*      */ 
/*      */   public Integer getCycleDay()
/*      */   {
/* 1057 */     return this.cycleDay;
/*      */   }
/*      */ 
/*      */   public void setCycleDay(Integer value)
/*      */   {
/* 1069 */     this.cycleDay = value;
/*      */   }
/*      */ 
/*      */   public Boolean isIsOutlook()
/*      */   {
/* 1081 */     return this.isOutlook;
/*      */   }
/*      */ 
/*      */   public void setIsOutlook(Boolean value)
/*      */   {
/* 1093 */     this.isOutlook = value;
/*      */   }
/*      */ 
/*      */   public String getIssueType()
/*      */   {
/* 1105 */     return this.issueType;
/*      */   }
/*      */ 
/*      */   public void setIssueType(String value)
/*      */   {
/* 1117 */     this.issueType = value;
/*      */   }
/*      */ 
/*      */   public String getDesk()
/*      */   {
/* 1129 */     return this.desk;
/*      */   }
/*      */ 
/*      */   public void setDesk(String value)
/*      */   {
/* 1141 */     this.desk = value;
/*      */   }
/*      */ 
/*      */   public String getTag()
/*      */   {
/* 1153 */     return this.tag;
/*      */   }
/*      */ 
/*      */   public void setTag(String value)
/*      */   {
/* 1165 */     this.tag = value;
/*      */   }
/*      */ 
/*      */   public String getFcstHr()
/*      */   {
/* 1177 */     return this.fcstHr;
/*      */   }
/*      */ 
/*      */   public void setFcstHr(String value)
/*      */   {
/* 1189 */     this.fcstHr = value;
/*      */   }
/*      */ 
/*      */   public String getHazard()
/*      */   {
/* 1201 */     return this.hazard;
/*      */   }
/*      */ 
/*      */   public void setHazard(String value)
/*      */   {
/* 1213 */     this.hazard = value;
/*      */   }
/*      */ 
/*      */   public Double getLonText()
/*      */   {
/* 1225 */     return this.lonText;
/*      */   }
/*      */ 
/*      */   public void setLonText(Double value)
/*      */   {
/* 1237 */     this.lonText = value;
/*      */   }
/*      */ 
/*      */   public Double getLatText()
/*      */   {
/* 1249 */     return this.latText;
/*      */   }
/*      */ 
/*      */   public void setLatText(Double value)
/*      */   {
/* 1261 */     this.latText = value;
/*      */   }
/*      */ 
/*      */   public String getFillPattern()
/*      */   {
/* 1273 */     return this.fillPattern;
/*      */   }
/*      */ 
/*      */   public void setFillPattern(String value)
/*      */   {
/* 1285 */     this.fillPattern = value;
/*      */   }
/*      */ 
/*      */   public Boolean isFilled()
/*      */   {
/* 1297 */     return this.filled;
/*      */   }
/*      */ 
/*      */   public void setFilled(Boolean value)
/*      */   {
/* 1309 */     this.filled = value;
/*      */   }
/*      */ 
/*      */   public Boolean isClosed()
/*      */   {
/* 1321 */     return this.closed;
/*      */   }
/*      */ 
/*      */   public void setClosed(Boolean value)
/*      */   {
/* 1333 */     this.closed = value;
/*      */   }
/*      */ 
/*      */   public Integer getSmoothFactor()
/*      */   {
/* 1345 */     return this.smoothFactor;
/*      */   }
/*      */ 
/*      */   public void setSmoothFactor(Integer value)
/*      */   {
/* 1357 */     this.smoothFactor = value;
/*      */   }
/*      */ 
/*      */   public Double getSizeScale()
/*      */   {
/* 1369 */     return this.sizeScale;
/*      */   }
/*      */ 
/*      */   public void setSizeScale(Double value)
/*      */   {
/* 1381 */     this.sizeScale = value;
/*      */   }
/*      */ 
/*      */   public Float getLineWidth()
/*      */   {
/* 1393 */     return this.lineWidth;
/*      */   }
/*      */ 
/*      */   public void setLineWidth(Float value)
/*      */   {
/* 1405 */     this.lineWidth = value;
/*      */   }
/*      */ 
/*      */   public String getPgenType()
/*      */   {
/* 1417 */     return this.pgenType;
/*      */   }
/*      */ 
/*      */   public void setPgenType(String value)
/*      */   {
/* 1429 */     this.pgenType = value;
/*      */   }
/*      */ 
/*      */   public String getPgenCategory()
/*      */   {
/* 1441 */     return this.pgenCategory;
/*      */   }
/*      */ 
/*      */   public void setPgenCategory(String value)
/*      */   {
/* 1453 */     this.pgenCategory = value;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Gfa
 * JD-Core Version:    0.6.2
 */