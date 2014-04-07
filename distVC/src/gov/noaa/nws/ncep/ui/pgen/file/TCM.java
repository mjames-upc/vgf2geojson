/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.TcmFcst;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.TcmWindQuarters;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="")
/*     */ @XmlRootElement(name="TCM")
/*     */ public class TCM
/*     */ {
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer stormNumber;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String stormName;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String basin;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String stormType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer advisoryNumber;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected XMLGregorianCalendar advisoryTime;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer eyeSize;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer positionAccuracy;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean correction;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer centralPressure;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   @XmlElement(name="waves")
/*     */   protected TcmWindQuarters tcmWaves;
/*     */ 
/*     */   @XmlElement(name="fcst")
/*     */   protected ArrayList<TcmFcst> tcmFcst;
/*     */ 
/*     */   public Integer getStormNumber()
/*     */   {
/* 103 */     return this.stormNumber;
/*     */   }
/*     */ 
/*     */   public void setStormNumber(Integer value)
/*     */   {
/* 115 */     this.stormNumber = value;
/*     */   }
/*     */ 
/*     */   public String getStormName()
/*     */   {
/* 127 */     return this.stormName;
/*     */   }
/*     */ 
/*     */   public void setStormName(String value)
/*     */   {
/* 139 */     this.stormName = value;
/*     */   }
/*     */ 
/*     */   public String getBasin()
/*     */   {
/* 151 */     return this.basin;
/*     */   }
/*     */ 
/*     */   public void setBasin(String value)
/*     */   {
/* 163 */     this.basin = value;
/*     */   }
/*     */ 
/*     */   public String getStormType()
/*     */   {
/* 175 */     return this.stormType;
/*     */   }
/*     */ 
/*     */   public void setStormType(String value)
/*     */   {
/* 187 */     this.stormType = value;
/*     */   }
/*     */ 
/*     */   public Integer getAdvisoryNumber()
/*     */   {
/* 199 */     return this.advisoryNumber;
/*     */   }
/*     */ 
/*     */   public void setAdvisoryNumber(Integer value)
/*     */   {
/* 211 */     this.advisoryNumber = value;
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar getAdvisoryTime()
/*     */   {
/* 223 */     return this.advisoryTime;
/*     */   }
/*     */ 
/*     */   public void setAdvisoryTime(XMLGregorianCalendar value)
/*     */   {
/* 235 */     this.advisoryTime = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 248 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 260 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 272 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 284 */     this.pgenCategory = value;
/*     */   }
/*     */ 
/*     */   public ArrayList<TcmFcst> getTcmFcst()
/*     */   {
/* 291 */     return this.tcmFcst;
/*     */   }
/*     */ 
/*     */   public void setTcmFcst(List<TcmFcst> tcmFcst)
/*     */   {
/* 298 */     this.tcmFcst = ((ArrayList)tcmFcst);
/*     */   }
/*     */ 
/*     */   public Integer getEyeSize() {
/* 302 */     return this.eyeSize;
/*     */   }
/*     */ 
/*     */   public void setEyeSize(Integer eyeSize) {
/* 306 */     this.eyeSize = eyeSize;
/*     */   }
/*     */ 
/*     */   public Integer getPositionAccuracy() {
/* 310 */     return this.positionAccuracy;
/*     */   }
/*     */ 
/*     */   public void setPositionAccuracy(Integer positionAccuracy) {
/* 314 */     this.positionAccuracy = positionAccuracy;
/*     */   }
/*     */ 
/*     */   public Boolean getCorrection() {
/* 318 */     return this.correction;
/*     */   }
/*     */ 
/*     */   public void setCorrection(Boolean correction) {
/* 322 */     this.correction = correction;
/*     */   }
/*     */ 
/*     */   public Integer getCentralPressure() {
/* 326 */     return this.centralPressure;
/*     */   }
/*     */ 
/*     */   public void setCentralPressure(Integer centralPressure) {
/* 330 */     this.centralPressure = centralPressure;
/*     */   }
/*     */ 
/*     */   public TcmWindQuarters getTcmWaves() {
/* 334 */     return this.tcmWaves;
/*     */   }
/*     */ 
/*     */   public void setTcmWaves(TcmWindQuarters tcmWaves) {
/* 338 */     this.tcmWaves = tcmWaves;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.TCM
 * JD-Core Version:    0.6.2
 */