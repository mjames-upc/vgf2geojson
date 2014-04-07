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
/*     */ @XmlRootElement(name="Contours")
/*     */ public class Contours
/*     */ {
/*     */ 
/*     */   @XmlElement(name="DECollection")
/*     */   protected List<DECollection> deCollection;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String cint;
/*     */ 
/*     */   @XmlAttribute
/*     */   @XmlSchemaType(name="dateTime")
/*     */   protected XMLGregorianCalendar time2;
/*     */ 
/*     */   @XmlAttribute
/*     */   @XmlSchemaType(name="dateTime")
/*     */   protected XMLGregorianCalendar time1;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String forecastHour;
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
/*     */   protected String collectionName;
/*     */ 
/*     */   public List<DECollection> getDECollection()
/*     */   {
/* 104 */     if (this.deCollection == null) {
/* 105 */       this.deCollection = new ArrayList();
/*     */     }
/* 107 */     return this.deCollection;
/*     */   }
/*     */ 
/*     */   public String getCint()
/*     */   {
/* 119 */     return this.cint;
/*     */   }
/*     */ 
/*     */   public void setCint(String value)
/*     */   {
/* 131 */     this.cint = value;
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar getTime2()
/*     */   {
/* 143 */     return this.time2;
/*     */   }
/*     */ 
/*     */   public void setTime2(XMLGregorianCalendar value)
/*     */   {
/* 155 */     this.time2 = value;
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar getTime1()
/*     */   {
/* 167 */     return this.time1;
/*     */   }
/*     */ 
/*     */   public void setTime1(XMLGregorianCalendar value)
/*     */   {
/* 179 */     this.time1 = value;
/*     */   }
/*     */ 
/*     */   public String getForecastHour()
/*     */   {
/* 191 */     return this.forecastHour;
/*     */   }
/*     */ 
/*     */   public void setForecastHour(String value)
/*     */   {
/* 203 */     this.forecastHour = value;
/*     */   }
/*     */ 
/*     */   public String getLevel()
/*     */   {
/* 215 */     return this.level;
/*     */   }
/*     */ 
/*     */   public void setLevel(String value)
/*     */   {
/* 227 */     this.level = value;
/*     */   }
/*     */ 
/*     */   public String getParm()
/*     */   {
/* 239 */     return this.parm;
/*     */   }
/*     */ 
/*     */   public void setParm(String value)
/*     */   {
/* 251 */     this.parm = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 263 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 275 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 287 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 299 */     this.pgenCategory = value;
/*     */   }
/*     */ 
/*     */   public String getCollectionName()
/*     */   {
/* 311 */     return this.collectionName;
/*     */   }
/*     */ 
/*     */   public void setCollectionName(String value)
/*     */   {
/* 323 */     this.collectionName = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Contours
 * JD-Core Version:    0.6.2
 */