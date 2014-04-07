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
/*      */ @XmlRootElement(name="Sigmet")
/*      */ public class Sigmet
/*      */ {
/*      */ 
/*      */   @XmlElement(name="Color", required=true)
/*      */   protected List<Color> color;
/*      */ 
/*      */   @XmlElement(name="Point", required=true)
/*      */   protected List<Point> point;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String pgenType;
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
/*      */   protected String pgenCategory;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String type;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected Double width;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrArea;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrIssueOffice;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrStatus;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrId;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrSeqNum;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrStartTime;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrEndTime;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrRemarks;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrPhenom;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrPhenom2;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrPhenomName;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrPhenomLat;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrPhenomLon;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrPhenomPressure;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrPhenomMaxWind;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrFreeText;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrTrend;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrMovement;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrPhenomSpeed;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrPhenomDirection;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrLevel;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrLevelInfo1;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrLevelInfo2;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrLevelText1;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrLevelText2;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrFromLine;
/*      */ 
/*      */   @XmlAttribute
/*      */   protected String editableAttrFir;
/*      */ 
/*      */   public List<Color> getColor()
/*      */   {
/*  188 */     if (this.color == null) {
/*  189 */       this.color = new ArrayList();
/*      */     }
/*  191 */     return this.color;
/*      */   }
/*      */ 
/*      */   public List<Point> getPoint()
/*      */   {
/*  217 */     if (this.point == null) {
/*  218 */       this.point = new ArrayList();
/*      */     }
/*  220 */     return this.point;
/*      */   }
/*      */ 
/*      */   public String getPgenType()
/*      */   {
/*  232 */     return this.pgenType;
/*      */   }
/*      */ 
/*      */   public void setPgenType(String value)
/*      */   {
/*  244 */     this.pgenType = value;
/*      */   }
/*      */ 
/*      */   public String getFillPattern()
/*      */   {
/*  256 */     return this.fillPattern;
/*      */   }
/*      */ 
/*      */   public void setFillPattern(String value)
/*      */   {
/*  268 */     this.fillPattern = value;
/*      */   }
/*      */ 
/*      */   public Boolean isFilled()
/*      */   {
/*  280 */     return this.filled;
/*      */   }
/*      */ 
/*      */   public void setFilled(Boolean value)
/*      */   {
/*  292 */     this.filled = value;
/*      */   }
/*      */ 
/*      */   public Boolean isClosed()
/*      */   {
/*  304 */     return this.closed;
/*      */   }
/*      */ 
/*      */   public void setClosed(Boolean value)
/*      */   {
/*  316 */     this.closed = value;
/*      */   }
/*      */ 
/*      */   public Integer getSmoothFactor()
/*      */   {
/*  328 */     return this.smoothFactor;
/*      */   }
/*      */ 
/*      */   public void setSmoothFactor(Integer value)
/*      */   {
/*  340 */     this.smoothFactor = value;
/*      */   }
/*      */ 
/*      */   public Double getSizeScale()
/*      */   {
/*  352 */     return this.sizeScale;
/*      */   }
/*      */ 
/*      */   public void setSizeScale(Double value)
/*      */   {
/*  364 */     this.sizeScale = value;
/*      */   }
/*      */ 
/*      */   public Float getLineWidth()
/*      */   {
/*  376 */     return this.lineWidth;
/*      */   }
/*      */ 
/*      */   public void setLineWidth(Float value)
/*      */   {
/*  388 */     this.lineWidth = value;
/*      */   }
/*      */ 
/*      */   public String getPgenCategory()
/*      */   {
/*  400 */     return this.pgenCategory;
/*      */   }
/*      */ 
/*      */   public void setPgenCategory(String value)
/*      */   {
/*  412 */     this.pgenCategory = value;
/*      */   }
/*      */ 
/*      */   public String getType()
/*      */   {
/*  424 */     return this.type;
/*      */   }
/*      */ 
/*      */   public void setType(String value)
/*      */   {
/*  436 */     this.type = value;
/*      */   }
/*      */ 
/*      */   public Double getWidth()
/*      */   {
/*  448 */     return this.width;
/*      */   }
/*      */ 
/*      */   public void setWidth(Double value)
/*      */   {
/*  460 */     this.width = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrArea()
/*      */   {
/*  472 */     return this.editableAttrArea;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrArea(String value)
/*      */   {
/*  484 */     this.editableAttrArea = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrIssueOffice()
/*      */   {
/*  496 */     return this.editableAttrIssueOffice;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrIssueOffice(String value)
/*      */   {
/*  508 */     this.editableAttrIssueOffice = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrStatus()
/*      */   {
/*  520 */     return this.editableAttrStatus;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrStatus(String value)
/*      */   {
/*  532 */     this.editableAttrStatus = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrId()
/*      */   {
/*  544 */     return this.editableAttrId;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrId(String value)
/*      */   {
/*  556 */     this.editableAttrId = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrSeqNum()
/*      */   {
/*  568 */     return this.editableAttrSeqNum;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrSeqNum(String value)
/*      */   {
/*  580 */     this.editableAttrSeqNum = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrStartTime()
/*      */   {
/*  592 */     return this.editableAttrStartTime;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrStartTime(String value)
/*      */   {
/*  604 */     this.editableAttrStartTime = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrEndTime()
/*      */   {
/*  616 */     return this.editableAttrEndTime;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrEndTime(String value)
/*      */   {
/*  628 */     this.editableAttrEndTime = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrRemarks()
/*      */   {
/*  640 */     return this.editableAttrRemarks;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrRemarks(String value)
/*      */   {
/*  652 */     this.editableAttrRemarks = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenom()
/*      */   {
/*  664 */     return this.editableAttrPhenom;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenom(String value)
/*      */   {
/*  676 */     this.editableAttrPhenom = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenom2()
/*      */   {
/*  688 */     return this.editableAttrPhenom2;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenom2(String value)
/*      */   {
/*  700 */     this.editableAttrPhenom2 = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomName()
/*      */   {
/*  712 */     return this.editableAttrPhenomName;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomName(String value)
/*      */   {
/*  724 */     this.editableAttrPhenomName = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomLat()
/*      */   {
/*  736 */     return this.editableAttrPhenomLat;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomLat(String value)
/*      */   {
/*  748 */     this.editableAttrPhenomLat = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomLon()
/*      */   {
/*  760 */     return this.editableAttrPhenomLon;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomLon(String value)
/*      */   {
/*  772 */     this.editableAttrPhenomLon = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomPressure()
/*      */   {
/*  784 */     return this.editableAttrPhenomPressure;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomPressure(String value)
/*      */   {
/*  796 */     this.editableAttrPhenomPressure = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomMaxWind()
/*      */   {
/*  808 */     return this.editableAttrPhenomMaxWind;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomMaxWind(String value)
/*      */   {
/*  820 */     this.editableAttrPhenomMaxWind = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrFreeText()
/*      */   {
/*  832 */     return this.editableAttrFreeText;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrFreeText(String value)
/*      */   {
/*  844 */     this.editableAttrFreeText = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrTrend()
/*      */   {
/*  856 */     return this.editableAttrTrend;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrTrend(String value)
/*      */   {
/*  868 */     this.editableAttrTrend = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrMovement()
/*      */   {
/*  880 */     return this.editableAttrMovement;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrMovement(String value)
/*      */   {
/*  892 */     this.editableAttrMovement = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomSpeed()
/*      */   {
/*  904 */     return this.editableAttrPhenomSpeed;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomSpeed(String value)
/*      */   {
/*  916 */     this.editableAttrPhenomSpeed = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomDirection()
/*      */   {
/*  928 */     return this.editableAttrPhenomDirection;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomDirection(String value)
/*      */   {
/*  940 */     this.editableAttrPhenomDirection = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrLevel()
/*      */   {
/*  952 */     return this.editableAttrLevel;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrLevel(String value)
/*      */   {
/*  964 */     this.editableAttrLevel = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrLevelInfo1()
/*      */   {
/*  976 */     return this.editableAttrLevelInfo1;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrLevelInfo1(String value)
/*      */   {
/*  988 */     this.editableAttrLevelInfo1 = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrLevelInfo2()
/*      */   {
/* 1000 */     return this.editableAttrLevelInfo2;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrLevelInfo2(String value)
/*      */   {
/* 1012 */     this.editableAttrLevelInfo2 = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrLevelText1()
/*      */   {
/* 1024 */     return this.editableAttrLevelText1;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrLevelText1(String value)
/*      */   {
/* 1036 */     this.editableAttrLevelText1 = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrLevelText2()
/*      */   {
/* 1048 */     return this.editableAttrLevelText2;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrLevelText2(String value)
/*      */   {
/* 1060 */     this.editableAttrLevelText2 = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrFromLine()
/*      */   {
/* 1072 */     return this.editableAttrFromLine;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrFromLine(String value)
/*      */   {
/* 1084 */     this.editableAttrFromLine = value;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrFir()
/*      */   {
/* 1096 */     return this.editableAttrFir;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrFir(String value)
/*      */   {
/* 1108 */     this.editableAttrFir = value;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Sigmet
 * JD-Core Version:    0.6.2
 */