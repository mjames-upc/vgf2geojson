/*      */ package gov.noaa.nws.ncep.ui.pgen.file;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import javax.xml.bind.annotation.XmlAccessType;
/*      */ import javax.xml.bind.annotation.XmlAccessorType;
/*      */ import javax.xml.bind.annotation.XmlAttribute;
/*      */ import javax.xml.bind.annotation.XmlElement;
/*      */ import javax.xml.bind.annotation.XmlRootElement;
/*      */ import javax.xml.bind.annotation.XmlSchemaType;
/*      */ import javax.xml.bind.annotation.XmlType;
/*      */ import javax.xml.datatype.XMLGregorianCalendar;
/*      */ 
/*      */ @XmlAccessorType(XmlAccessType.FIELD)
/*      */ @XmlType(name="", propOrder={"color", "fillColor", "point", "anchorPoints", "counties", "states", "outline", "hole", "status"})
/*      */ @XmlRootElement(name="WatchBox")
/*      */ public class WatchBox
/*      */ {
/*      */ 
/*      */   @XmlElement(name="Color", required=true)
/*      */   protected List<Color> color;
/*      */ 
/*      */   @XmlElement(required=true)
/*      */   protected ColorType fillColor;
/*      */ 
/*      */   @XmlElement(name="Point", required=true)
/*      */   protected List<Point> point;
/*      */ 
/*      */   @XmlElement(name="AnchorPoints")
/*      */   protected List<String> anchorPoints;
/*      */ 
/*      */   @XmlElement(name="Counties")
/*      */   protected List<String> counties;
/*      */ 
/*      */   @XmlElement(name="States")
/*      */   protected List<String> states;
/*      */ 
/*      */   @XmlElement(name="Outline")
/*      */   protected List<Outline> outline;
/*      */ 
/*      */   @XmlElement(name="Hole")
/*      */   protected List<Hole> hole;
/*      */ 
/*      */   @XmlElement(name="Status")
/*      */   protected List<Status> status;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String pgenType;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String pgenCategory;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String boxShape;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Boolean fillFlag;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String symbolType;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Float symbolWidth;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Double symbolSize;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String issueStatus;
/*      */ 
/*      */   @XmlAttribute
/*      */   @XmlSchemaType(name="dateTime")
/*      */   protected XMLGregorianCalendar issueTime;
/*      */ 
/*      */   @XmlAttribute
/*      */   @XmlSchemaType(name="dateTime")
/*      */   protected XMLGregorianCalendar expTime;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String severity;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String timeZone;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Float hailSize;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer gust;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer top;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer moveDir;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer moveSpeed;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String adjAreas;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer replWatch;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer issueFlag;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String watchType;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer watchNumber;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String contWatch;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String forecaster;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String endPointAnc;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String endPointVor;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer halfWidthNm;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer halfWidthSm;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Integer watchAreaNm;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String wfos;
/*      */ 
/*      */   public List<Color> getColor()
/*      */   {
/*  231 */     if (this.color == null) {
/*  232 */       this.color = new ArrayList();
/*      */     }
/*  234 */     return this.color;
/*      */   }
/*      */ 
/*      */   public ColorType getFillColor()
/*      */   {
/*  246 */     return this.fillColor;
/*      */   }
/*      */ 
/*      */   public void setFillColor(ColorType value)
/*      */   {
/*  258 */     this.fillColor = value;
/*      */   }
/*      */ 
/*      */   public List<Point> getPoint()
/*      */   {
/*  284 */     if (this.point == null) {
/*  285 */       this.point = new ArrayList();
/*      */     }
/*  287 */     return this.point;
/*      */   }
/*      */ 
/*      */   public List<String> getAnchorPoints()
/*      */   {
/*  313 */     if (this.anchorPoints == null) {
/*  314 */       this.anchorPoints = new ArrayList();
/*      */     }
/*  316 */     return this.anchorPoints;
/*      */   }
/*      */ 
/*      */   public List<String> getCounties()
/*      */   {
/*  342 */     if (this.counties == null) {
/*  343 */       this.counties = new ArrayList();
/*      */     }
/*  345 */     return this.counties;
/*      */   }
/*      */ 
/*      */   public List<String> getStates()
/*      */   {
/*  371 */     if (this.states == null) {
/*  372 */       this.states = new ArrayList();
/*      */     }
/*  374 */     return this.states;
/*      */   }
/*      */ 
/*      */   public List<Outline> getOutline()
/*      */   {
/*  400 */     if (this.outline == null) {
/*  401 */       this.outline = new ArrayList();
/*      */     }
/*  403 */     return this.outline;
/*      */   }
/*      */ 
/*      */   public List<Hole> getHole()
/*      */   {
/*  429 */     if (this.hole == null) {
/*  430 */       this.hole = new ArrayList();
/*      */     }
/*  432 */     return this.hole;
/*      */   }
/*      */ 
/*      */   public List<Status> getStatus()
/*      */   {
/*  458 */     if (this.status == null) {
/*  459 */       this.status = new ArrayList();
/*      */     }
/*  461 */     return this.status;
/*      */   }
/*      */ 
/*      */   public String getPgenType()
/*      */   {
/*  473 */     return this.pgenType;
/*      */   }
/*      */ 
/*      */   public void setPgenType(String value)
/*      */   {
/*  485 */     this.pgenType = value;
/*      */   }
/*      */ 
/*      */   public String getPgenCategory()
/*      */   {
/*  497 */     return this.pgenCategory;
/*      */   }
/*      */ 
/*      */   public void setPgenCategory(String value)
/*      */   {
/*  509 */     this.pgenCategory = value;
/*      */   }
/*      */ 
/*      */   public String getBoxShape()
/*      */   {
/*  521 */     return this.boxShape;
/*      */   }
/*      */ 
/*      */   public void setBoxShape(String value)
/*      */   {
/*  533 */     this.boxShape = value;
/*      */   }
/*      */ 
/*      */   public Boolean isFillFlag()
/*      */   {
/*  545 */     return this.fillFlag;
/*      */   }
/*      */ 
/*      */   public void setFillFlag(Boolean value)
/*      */   {
/*  557 */     this.fillFlag = value;
/*      */   }
/*      */ 
/*      */   public String getSymbolType()
/*      */   {
/*  569 */     return this.symbolType;
/*      */   }
/*      */ 
/*      */   public void setSymbolType(String value)
/*      */   {
/*  581 */     this.symbolType = value;
/*      */   }
/*      */ 
/*      */   public Float getSymbolWidth()
/*      */   {
/*  593 */     return this.symbolWidth;
/*      */   }
/*      */ 
/*      */   public void setSymbolWidth(Float value)
/*      */   {
/*  605 */     this.symbolWidth = value;
/*      */   }
/*      */ 
/*      */   public Double getSymbolSize()
/*      */   {
/*  617 */     return this.symbolSize;
/*      */   }
/*      */ 
/*      */   public void setSymbolSize(Double value)
/*      */   {
/*  629 */     this.symbolSize = value;
/*      */   }
/*      */ 
/*      */   public String getIssueStatus()
/*      */   {
/*  641 */     return this.issueStatus;
/*      */   }
/*      */ 
/*      */   public void setIssueStatus(String value)
/*      */   {
/*  653 */     this.issueStatus = value;
/*      */   }
/*      */ 
/*      */   public XMLGregorianCalendar getIssueTime()
/*      */   {
/*  665 */     return this.issueTime;
/*      */   }
/*      */ 
/*      */   public void setIssueTime(XMLGregorianCalendar value)
/*      */   {
/*  677 */     this.issueTime = value;
/*      */   }
/*      */ 
/*      */   public XMLGregorianCalendar getExpTime()
/*      */   {
/*  689 */     return this.expTime;
/*      */   }
/*      */ 
/*      */   public void setExpTime(XMLGregorianCalendar value)
/*      */   {
/*  701 */     this.expTime = value;
/*      */   }
/*      */ 
/*      */   public String getSeverity()
/*      */   {
/*  713 */     return this.severity;
/*      */   }
/*      */ 
/*      */   public void setSeverity(String value)
/*      */   {
/*  725 */     this.severity = value;
/*      */   }
/*      */ 
/*      */   public String getTimeZone()
/*      */   {
/*  737 */     return this.timeZone;
/*      */   }
/*      */ 
/*      */   public void setTimeZone(String value)
/*      */   {
/*  749 */     this.timeZone = value;
/*      */   }
/*      */ 
/*      */   public Float getHailSize()
/*      */   {
/*  761 */     return this.hailSize;
/*      */   }
/*      */ 
/*      */   public void setHailSize(Float value)
/*      */   {
/*  773 */     this.hailSize = value;
/*      */   }
/*      */ 
/*      */   public Integer getGust()
/*      */   {
/*  785 */     return this.gust;
/*      */   }
/*      */ 
/*      */   public void setGust(Integer value)
/*      */   {
/*  797 */     this.gust = value;
/*      */   }
/*      */ 
/*      */   public Integer getTop()
/*      */   {
/*  809 */     return this.top;
/*      */   }
/*      */ 
/*      */   public void setTop(Integer value)
/*      */   {
/*  821 */     this.top = value;
/*      */   }
/*      */ 
/*      */   public Integer getMoveDir()
/*      */   {
/*  833 */     return this.moveDir;
/*      */   }
/*      */ 
/*      */   public void setMoveDir(Integer value)
/*      */   {
/*  845 */     this.moveDir = value;
/*      */   }
/*      */ 
/*      */   public Integer getMoveSpeed()
/*      */   {
/*  857 */     return this.moveSpeed;
/*      */   }
/*      */ 
/*      */   public void setMoveSpeed(Integer value)
/*      */   {
/*  869 */     this.moveSpeed = value;
/*      */   }
/*      */ 
/*      */   public String getAdjAreas()
/*      */   {
/*  881 */     return this.adjAreas;
/*      */   }
/*      */ 
/*      */   public void setAdjAreas(String value)
/*      */   {
/*  893 */     this.adjAreas = value;
/*      */   }
/*      */ 
/*      */   public Integer getReplWatch()
/*      */   {
/*  905 */     return this.replWatch;
/*      */   }
/*      */ 
/*      */   public void setReplWatch(Integer value)
/*      */   {
/*  917 */     this.replWatch = value;
/*      */   }
/*      */ 
/*      */   public Integer getIssueFlag()
/*      */   {
/*  929 */     return this.issueFlag;
/*      */   }
/*      */ 
/*      */   public void setIssueFlag(Integer value)
/*      */   {
/*  941 */     this.issueFlag = value;
/*      */   }
/*      */ 
/*      */   public String getWatchType()
/*      */   {
/*  953 */     return this.watchType;
/*      */   }
/*      */ 
/*      */   public void setWatchType(String value)
/*      */   {
/*  965 */     this.watchType = value;
/*      */   }
/*      */ 
/*      */   public Integer getWatchNumber()
/*      */   {
/*  977 */     return this.watchNumber;
/*      */   }
/*      */ 
/*      */   public void setWatchNumber(Integer value)
/*      */   {
/*  989 */     this.watchNumber = value;
/*      */   }
/*      */ 
/*      */   public String getContWatch()
/*      */   {
/* 1001 */     return this.contWatch;
/*      */   }
/*      */ 
/*      */   public void setContWatch(String value)
/*      */   {
/* 1013 */     this.contWatch = value;
/*      */   }
/*      */ 
/*      */   public String getForecaster()
/*      */   {
/* 1025 */     return this.forecaster;
/*      */   }
/*      */ 
/*      */   public void setForecaster(String value)
/*      */   {
/* 1037 */     this.forecaster = value;
/*      */   }
/*      */ 
/*      */   public String getEndPointAnc()
/*      */   {
/* 1049 */     return this.endPointAnc;
/*      */   }
/*      */ 
/*      */   public void setEndPointAnc(String value)
/*      */   {
/* 1061 */     this.endPointAnc = value;
/*      */   }
/*      */ 
/*      */   public String getEndPointVor()
/*      */   {
/* 1073 */     return this.endPointVor;
/*      */   }
/*      */ 
/*      */   public void setEndPointVor(String value)
/*      */   {
/* 1085 */     this.endPointVor = value;
/*      */   }
/*      */ 
/*      */   public Integer getHalfWidthNm()
/*      */   {
/* 1097 */     return this.halfWidthNm;
/*      */   }
/*      */ 
/*      */   public void setHalfWidthNm(Integer value)
/*      */   {
/* 1109 */     this.halfWidthNm = value;
/*      */   }
/*      */ 
/*      */   public Integer getHalfWidthSm()
/*      */   {
/* 1121 */     return this.halfWidthSm;
/*      */   }
/*      */ 
/*      */   public void setHalfWidthSm(Integer value)
/*      */   {
/* 1133 */     this.halfWidthSm = value;
/*      */   }
/*      */ 
/*      */   public Integer getWatchAreaNm()
/*      */   {
/* 1145 */     return this.watchAreaNm;
/*      */   }
/*      */ 
/*      */   public void setWatchAreaNm(Integer value)
/*      */   {
/* 1157 */     this.watchAreaNm = value;
/*      */   }
/*      */ 
/*      */   public String getWfos()
/*      */   {
/* 1169 */     return this.wfos;
/*      */   }
/*      */ 
/*      */   public void setWfos(String value)
/*      */   {
/* 1181 */     this.wfos = value;
/*      */   }
/*      */ 
/*      */   @XmlAccessorType(XmlAccessType.FIELD)
/*      */   @XmlType(name="", propOrder={"point"})
/*      */   public static class Hole
/*      */   {
/*      */ 
/*      */     @XmlElement(name="Point", required=true)
/*      */     protected List<Point> point;
/*      */ 
/*      */     public List<Point> getPoint()
/*      */     {
/* 1236 */       if (this.point == null) {
/* 1237 */         this.point = new ArrayList();
/*      */       }
/* 1239 */       return this.point;
/*      */     }
/*      */   }
/*      */ 
/*      */   @XmlAccessorType(XmlAccessType.FIELD)
/*      */   @XmlType(name="", propOrder={"point"})
/*      */   public static class Outline
/*      */   {
/*      */ 
/*      */     @XmlElement(name="Point", required=true)
/*      */     protected List<Point> point;
/*      */ 
/*      */     public List<Point> getPoint()
/*      */     {
/* 1296 */       if (this.point == null) {
/* 1297 */         this.point = new ArrayList();
/*      */       }
/* 1299 */       return this.point;
/*      */     }
/*      */   }
/*      */ 
/*      */   @XmlAccessorType(XmlAccessType.FIELD)
/*      */   @XmlType(name="")
/*      */   public static class Status
/*      */   {
/*      */ 
/*      */     @XmlAttribute
/*      */     protected String fromLine;
/*      */ 
/*      */     @XmlAttribute
/*      */     protected Integer mesoDiscussionNumber;
/*      */ 
/*      */     @XmlAttribute
/*      */     @XmlSchemaType(name="dateTime")
/*      */     protected XMLGregorianCalendar statusValidTime;
/*      */ 
/*      */     @XmlAttribute
/*      */     @XmlSchemaType(name="dateTime")
/*      */     protected XMLGregorianCalendar statusExpTime;
/*      */ 
/*      */     @XmlAttribute
/*      */     protected String statusForecaster;
/*      */ 
/*      */     public String getFromLine()
/*      */     {
/* 1352 */       return this.fromLine;
/*      */     }
/*      */ 
/*      */     public void setFromLine(String value)
/*      */     {
/* 1364 */       this.fromLine = value;
/*      */     }
/*      */ 
/*      */     public Integer getMesoDiscussionNumber()
/*      */     {
/* 1376 */       return this.mesoDiscussionNumber;
/*      */     }
/*      */ 
/*      */     public void setMesoDiscussionNumber(Integer value)
/*      */     {
/* 1388 */       this.mesoDiscussionNumber = value;
/*      */     }
/*      */ 
/*      */     public XMLGregorianCalendar getStatusValidTime()
/*      */     {
/* 1400 */       return this.statusValidTime;
/*      */     }
/*      */ 
/*      */     public void setStatusValidTime(XMLGregorianCalendar value)
/*      */     {
/* 1412 */       this.statusValidTime = value;
/*      */     }
/*      */ 
/*      */     public XMLGregorianCalendar getStatusExpTime()
/*      */     {
/* 1424 */       return this.statusExpTime;
/*      */     }
/*      */ 
/*      */     public void setStatusExpTime(XMLGregorianCalendar value)
/*      */     {
/* 1436 */       this.statusExpTime = value;
/*      */     }
/*      */ 
/*      */     public String getStatusForecaster()
/*      */     {
/* 1448 */       return this.statusForecaster;
/*      */     }
/*      */ 
/*      */     public void setStatusForecaster(String value)
/*      */     {
/* 1460 */       this.statusForecaster = value;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.WatchBox
 * JD-Core Version:    0.6.2
 */