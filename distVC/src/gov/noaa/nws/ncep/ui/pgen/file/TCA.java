/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.adapters.CoordAdapter;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.TropicalCycloneAdvisory;
/*     */ import java.util.ArrayList;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="")
/*     */ @XmlRootElement(name="TCA")
/*     */ public class TCA
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
/*     */   protected String issueStatus;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String stormType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String advisoryNumber;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected XMLGregorianCalendar advisoryTime;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String timeZone;
/*     */ 
/*     */   @XmlJavaTypeAdapter(CoordAdapter.class)
/*     */   @XmlAttribute
/*     */   protected Coordinate textLocation;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenType;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String pgenCategory;
/*     */ 
/*     */   @XmlElement(name="advisory")
/*     */   protected ArrayList<TropicalCycloneAdvisory> advisories;
/*     */ 
/*     */   public Integer getStormNumber()
/*     */   {
/*  99 */     return this.stormNumber;
/*     */   }
/*     */ 
/*     */   public void setStormNumber(Integer value)
/*     */   {
/* 111 */     this.stormNumber = value;
/*     */   }
/*     */ 
/*     */   public String getStormName()
/*     */   {
/* 123 */     return this.stormName;
/*     */   }
/*     */ 
/*     */   public void setStormName(String value)
/*     */   {
/* 135 */     this.stormName = value;
/*     */   }
/*     */ 
/*     */   public String getBasin()
/*     */   {
/* 147 */     return this.basin;
/*     */   }
/*     */ 
/*     */   public void setBasin(String value)
/*     */   {
/* 159 */     this.basin = value;
/*     */   }
/*     */ 
/*     */   public String getIssueStatus()
/*     */   {
/* 171 */     return this.issueStatus;
/*     */   }
/*     */ 
/*     */   public void setIssueStatus(String value)
/*     */   {
/* 183 */     this.issueStatus = value;
/*     */   }
/*     */ 
/*     */   public String getStormType()
/*     */   {
/* 195 */     return this.stormType;
/*     */   }
/*     */ 
/*     */   public void setStormType(String value)
/*     */   {
/* 207 */     this.stormType = value;
/*     */   }
/*     */ 
/*     */   public String getAdvisoryNumber()
/*     */   {
/* 219 */     return this.advisoryNumber;
/*     */   }
/*     */ 
/*     */   public void setAdvisoryNumber(String value)
/*     */   {
/* 231 */     this.advisoryNumber = value;
/*     */   }
/*     */ 
/*     */   public XMLGregorianCalendar getAdvisoryTime()
/*     */   {
/* 243 */     return this.advisoryTime;
/*     */   }
/*     */ 
/*     */   public void setAdvisoryTime(XMLGregorianCalendar value)
/*     */   {
/* 255 */     this.advisoryTime = value;
/*     */   }
/*     */ 
/*     */   public String getTimeZone()
/*     */   {
/* 267 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */   public void setTimeZone(String value)
/*     */   {
/* 279 */     this.timeZone = value;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/* 291 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String value)
/*     */   {
/* 303 */     this.pgenType = value;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 315 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String value)
/*     */   {
/* 327 */     this.pgenCategory = value;
/*     */   }
/*     */ 
/*     */   public Coordinate getTextLocation()
/*     */   {
/* 334 */     return this.textLocation;
/*     */   }
/*     */ 
/*     */   public void setTextLocation(Coordinate textLocation)
/*     */   {
/* 341 */     this.textLocation = textLocation;
/*     */   }
/*     */ 
/*     */   public ArrayList<TropicalCycloneAdvisory> getAdvisories()
/*     */   {
/* 348 */     return this.advisories;
/*     */   }
/*     */ 
/*     */   public void setAdvisories(ArrayList<TropicalCycloneAdvisory> advisories)
/*     */   {
/* 355 */     this.advisories = advisories;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.TCA
 * JD-Core Version:    0.6.2
 */