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
/*     */ @XmlRootElement(name="Volcano")
/*     */ public class Volcano
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Color", required=true)
/*     */   protected List<Color> color;
/*     */ 
/*     */   @XmlElement(name="Point", required=true)
/*     */   protected List<Point> point;
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
/*     */   protected String pgenType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String name;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String number;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String txtLoc;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String area;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String elev;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String origStnVAAC;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String wmoId;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String hdrNum;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String product;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String year;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String advNum;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String corr;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String infoSource;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String addInfoSource;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String aviColorCode;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String erupDetails;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String obsAshDate;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String obsAshTime;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String nil;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String obsFcstAshCloudInfo;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String obsFcstAshCloudInfo6;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String obsFcstAshCloudInfo12;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String obsFcstAshCloudInfo18;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String remarks;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String nextAdv;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String forecasters;
/*     */ 
/*     */   public List<Color> getColor()
/*     */   {
/* 170 */     if (this.color == null) {
/* 171 */       this.color = new ArrayList();
/*     */     }
/* 173 */     return this.color;
/*     */   }
/*     */ 
/*     */   public List<Point> getPoint()
/*     */   {
/* 199 */     if (this.point == null) {
/* 200 */       this.point = new ArrayList();
/*     */     }
/* 202 */     return this.point;
/*     */   }
/*     */ 
/*     */   public Boolean isClear()
/*     */   {
/* 214 */     return this.clear;
/*     */   }
/*     */ 
/*     */   public void setClear(Boolean value)
/*     */   {
/* 226 */     this.clear = value;
/*     */   }
/*     */ 
/*     */   public Double getSizeScale()
/*     */   {
/* 238 */     return this.sizeScale;
/*     */   }
/*     */ 
/*     */   public void setSizeScale(Double value)
/*     */   {
/* 250 */     this.sizeScale = value;
/*     */   }
/*     */ 
/*     */   public Float getLineWidth()
/*     */   {
/* 262 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */   public void setLineWidth(Float value)
/*     */   {
/* 274 */     this.lineWidth = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 286 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 298 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 310 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 322 */     this.pgenCategory = value;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 334 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String value)
/*     */   {
/* 346 */     this.name = value;
/*     */   }
/*     */ 
/*     */   public String getNumber()
/*     */   {
/* 358 */     return this.number;
/*     */   }
/*     */ 
/*     */   public void setNumber(String value)
/*     */   {
/* 370 */     this.number = value;
/*     */   }
/*     */ 
/*     */   public String getTxtLoc()
/*     */   {
/* 382 */     return this.txtLoc;
/*     */   }
/*     */ 
/*     */   public void setTxtLoc(String value)
/*     */   {
/* 394 */     this.txtLoc = value;
/*     */   }
/*     */ 
/*     */   public String getArea()
/*     */   {
/* 406 */     return this.area;
/*     */   }
/*     */ 
/*     */   public void setArea(String value)
/*     */   {
/* 418 */     this.area = value;
/*     */   }
/*     */ 
/*     */   public String getElev()
/*     */   {
/* 430 */     return this.elev;
/*     */   }
/*     */ 
/*     */   public void setElev(String value)
/*     */   {
/* 442 */     this.elev = value;
/*     */   }
/*     */ 
/*     */   public String getOrigStnVAAC()
/*     */   {
/* 454 */     return this.origStnVAAC;
/*     */   }
/*     */ 
/*     */   public void setOrigStnVAAC(String value)
/*     */   {
/* 466 */     this.origStnVAAC = value;
/*     */   }
/*     */ 
/*     */   public String getWmoId()
/*     */   {
/* 478 */     return this.wmoId;
/*     */   }
/*     */ 
/*     */   public void setWmoId(String value)
/*     */   {
/* 490 */     this.wmoId = value;
/*     */   }
/*     */ 
/*     */   public String getHdrNum()
/*     */   {
/* 502 */     return this.hdrNum;
/*     */   }
/*     */ 
/*     */   public void setHdrNum(String value)
/*     */   {
/* 514 */     this.hdrNum = value;
/*     */   }
/*     */ 
/*     */   public String getProduct()
/*     */   {
/* 526 */     return this.product;
/*     */   }
/*     */ 
/*     */   public void setProduct(String value)
/*     */   {
/* 538 */     this.product = value;
/*     */   }
/*     */ 
/*     */   public String getYear()
/*     */   {
/* 550 */     return this.year;
/*     */   }
/*     */ 
/*     */   public void setYear(String value)
/*     */   {
/* 562 */     this.year = value;
/*     */   }
/*     */ 
/*     */   public String getAdvNum()
/*     */   {
/* 574 */     return this.advNum;
/*     */   }
/*     */ 
/*     */   public void setAdvNum(String value)
/*     */   {
/* 586 */     this.advNum = value;
/*     */   }
/*     */ 
/*     */   public String getCorr()
/*     */   {
/* 598 */     return this.corr;
/*     */   }
/*     */ 
/*     */   public void setCorr(String value)
/*     */   {
/* 610 */     this.corr = value;
/*     */   }
/*     */ 
/*     */   public String getInfoSource()
/*     */   {
/* 622 */     return this.infoSource;
/*     */   }
/*     */ 
/*     */   public void setInfoSource(String value)
/*     */   {
/* 634 */     this.infoSource = value;
/*     */   }
/*     */ 
/*     */   public String getAddInfoSource()
/*     */   {
/* 646 */     return this.addInfoSource;
/*     */   }
/*     */ 
/*     */   public void setAddInfoSource(String value)
/*     */   {
/* 658 */     this.addInfoSource = value;
/*     */   }
/*     */ 
/*     */   public String getAviColorCode()
/*     */   {
/* 670 */     return this.aviColorCode;
/*     */   }
/*     */ 
/*     */   public void setAviColorCode(String value)
/*     */   {
/* 682 */     this.aviColorCode = value;
/*     */   }
/*     */ 
/*     */   public String getErupDetails()
/*     */   {
/* 694 */     return this.erupDetails;
/*     */   }
/*     */ 
/*     */   public void setErupDetails(String value)
/*     */   {
/* 706 */     this.erupDetails = value;
/*     */   }
/*     */ 
/*     */   public String getObsAshDate()
/*     */   {
/* 718 */     return this.obsAshDate;
/*     */   }
/*     */ 
/*     */   public void setObsAshDate(String value)
/*     */   {
/* 730 */     this.obsAshDate = value;
/*     */   }
/*     */ 
/*     */   public String getObsAshTime()
/*     */   {
/* 742 */     return this.obsAshTime;
/*     */   }
/*     */ 
/*     */   public void setObsAshTime(String value)
/*     */   {
/* 754 */     this.obsAshTime = value;
/*     */   }
/*     */ 
/*     */   public String getNil()
/*     */   {
/* 766 */     return this.nil;
/*     */   }
/*     */ 
/*     */   public void setNil(String value)
/*     */   {
/* 778 */     this.nil = value;
/*     */   }
/*     */ 
/*     */   public String getObsFcstAshCloudInfo()
/*     */   {
/* 790 */     return this.obsFcstAshCloudInfo;
/*     */   }
/*     */ 
/*     */   public void setObsFcstAshCloudInfo(String value)
/*     */   {
/* 802 */     this.obsFcstAshCloudInfo = value;
/*     */   }
/*     */ 
/*     */   public String getObsFcstAshCloudInfo6()
/*     */   {
/* 814 */     return this.obsFcstAshCloudInfo6;
/*     */   }
/*     */ 
/*     */   public void setObsFcstAshCloudInfo6(String value)
/*     */   {
/* 826 */     this.obsFcstAshCloudInfo6 = value;
/*     */   }
/*     */ 
/*     */   public String getObsFcstAshCloudInfo12()
/*     */   {
/* 838 */     return this.obsFcstAshCloudInfo12;
/*     */   }
/*     */ 
/*     */   public void setObsFcstAshCloudInfo12(String value)
/*     */   {
/* 850 */     this.obsFcstAshCloudInfo12 = value;
/*     */   }
/*     */ 
/*     */   public String getObsFcstAshCloudInfo18()
/*     */   {
/* 862 */     return this.obsFcstAshCloudInfo18;
/*     */   }
/*     */ 
/*     */   public void setObsFcstAshCloudInfo18(String value)
/*     */   {
/* 874 */     this.obsFcstAshCloudInfo18 = value;
/*     */   }
/*     */ 
/*     */   public String getRemarks()
/*     */   {
/* 886 */     return this.remarks;
/*     */   }
/*     */ 
/*     */   public void setRemarks(String value)
/*     */   {
/* 898 */     this.remarks = value;
/*     */   }
/*     */ 
/*     */   public String getNextAdv()
/*     */   {
/* 910 */     return this.nextAdv;
/*     */   }
/*     */ 
/*     */   public void setNextAdv(String value)
/*     */   {
/* 922 */     this.nextAdv = value;
/*     */   }
/*     */ 
/*     */   public String getForecasters()
/*     */   {
/* 934 */     return this.forecasters;
/*     */   }
/*     */ 
/*     */   public void setForecasters(String value)
/*     */   {
/* 946 */     this.forecasters = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Volcano
 * JD-Core Version:    0.6.2
 */