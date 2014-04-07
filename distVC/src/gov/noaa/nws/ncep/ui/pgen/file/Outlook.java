/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlSchemaType;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="", propOrder={"deCollection"})
/*     */ @XmlRootElement(name="Outlook")
/*     */ public class Outlook
/*     */ {
/*     */ 
/*     */   @XmlElement(name="DECollection")
/*     */   protected List<DECollection> deCollection;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String cint;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected XMLGregorianCalendar time;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String level;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String parm;
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
/*     */   protected String outlookType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String forecaster;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String days;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String lineInfo;
/*     */ 
/*     */   @XmlAttribute
/*     */   @XmlSchemaType(name="dateTime")
/*     */   protected XMLGregorianCalendar issueTime;
/*     */ 
/*     */   @XmlAttribute
/*     */   @XmlSchemaType(name="dateTime")
/*     */   protected XMLGregorianCalendar expTime;
/*     */ 
/*     */   public List<DECollection> getDECollection()
/*     */   {
/* 116 */     if (this.deCollection == null) {
/* 117 */       this.deCollection = new ArrayList();
/*     */     }
/* 119 */     return this.deCollection;
/*     */   }
/*     */ 
/*     */   public String getCint()
/*     */   {
/* 131 */     return this.cint;
/*     */   }
/*     */ 
/*     */   public void setCint(String value)
/*     */   {
/* 143 */     this.cint = value;
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar getTime()
/*     */   {
/* 155 */     return this.time;
/*     */   }
/*     */ 
/*     */   public void setTime(XMLGregorianCalendar value)
/*     */   {
/* 167 */     this.time = value;
/*     */   }
/*     */ 
/*     */   public String getLevel()
/*     */   {
/* 179 */     return this.level;
/*     */   }
/*     */ 
/*     */   public void setLevel(String value)
/*     */   {
/* 191 */     this.level = value;
/*     */   }
/*     */ 
/*     */   public String getParm()
/*     */   {
/* 203 */     return this.parm;
/*     */   }
/*     */ 
/*     */   public void setParm(String value)
/*     */   {
/* 215 */     this.parm = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 227 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 239 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 251 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 263 */     this.pgenCategory = value;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 275 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String value)
/*     */   {
/* 287 */     this.name = value;
/*     */   }
/*     */ 
/*     */   public String getOutlookType()
/*     */   {
/* 299 */     return this.outlookType;
/*     */   }
/*     */ 
/*     */   public void setOutlookType(String value)
/*     */   {
/* 311 */     this.outlookType = value;
/*     */   }
/*     */ 
/*     */   public String getForecaster()
/*     */   {
/* 323 */     return this.forecaster;
/*     */   }
/*     */ 
/*     */   public void setForecaster(String value)
/*     */   {
/* 335 */     this.forecaster = value;
/*     */   }
/*     */ 
/*     */   public String getDays()
/*     */   {
/* 347 */     return this.days;
/*     */   }
/*     */ 
/*     */   public void setDays(String value)
/*     */   {
/* 359 */     this.days = value;
/*     */   }
/*     */ 
/*     */   public String getLineInfo()
/*     */   {
/* 371 */     return this.lineInfo;
/*     */   }
/*     */ 
/*     */   public void setLineInfo(String value)
/*     */   {
/* 383 */     this.lineInfo = value;
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar getIssueTime()
/*     */   {
/* 395 */     return this.issueTime;
/*     */   }
/*     */ 
/*     */   public void setIssueTime(XMLGregorianCalendar value)
/*     */   {
/* 407 */     this.issueTime = value;
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar getExpTime()
/*     */   {
/* 419 */     return this.expTime;
/*     */   }
/*     */ 
/*     */   public void setExpTime(XMLGregorianCalendar value)
/*     */   {
/* 431 */     this.expTime = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Outlook
 * JD-Core Version:    0.6.2
 */