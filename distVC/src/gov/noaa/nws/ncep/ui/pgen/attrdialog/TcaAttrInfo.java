/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlElementWrapper;
/*     */ import javax.xml.bind.annotation.XmlElements;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ 
/*     */ @XmlRootElement(name="TCAAttributeInfo")
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ public class TcaAttrInfo
/*     */   implements ISerializableObject
/*     */ {
/*     */ 
/*     */   @XmlElementWrapper(name="issuingStatus")
/*     */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="entry", type=String.class)})
/*     */   private String[] statusList;
/*     */ 
/*     */   @XmlElementWrapper(name="stormTypes")
/*     */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="entry", type=String.class)})
/*     */   private String[] typeList;
/*     */ 
/*     */   @XmlElementWrapper(name="basins")
/*     */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="entry", type=String.class)})
/*     */   private String[] basinList;
/*     */ 
/*     */   @XmlElementWrapper(name="timeZones")
/*     */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="entry", type=String.class)})
/*     */   private String[] timezones;
/*     */ 
/*     */   @XmlElementWrapper(name="advisorySeverity")
/*     */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="entry", type=String.class)})
/*     */   private String[] severityList;
/*     */ 
/*     */   @XmlElementWrapper(name="advisoryTypes")
/*     */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="entry", type=String.class)})
/*     */   private String[] advisoryList;
/*     */ 
/*     */   @XmlElementWrapper(name="breakpointTypes")
/*     */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="entry", type=String.class)})
/*     */   private String[] breakpointTypeList;
/*     */ 
/*     */   @XmlElementWrapper(name="geographyTypes")
/*     */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="entry", type=String.class)})
/*     */   private String[] geographyTypeList;
/*     */ 
/*     */   public String[] getStatusList()
/*     */   {
/*  73 */     return this.statusList;
/*     */   }
/*     */ 
/*     */   public void setStatusList(String[] statusList)
/*     */   {
/*  80 */     this.statusList = statusList;
/*     */   }
/*     */ 
/*     */   public String[] getTypeList()
/*     */   {
/*  87 */     return this.typeList;
/*     */   }
/*     */ 
/*     */   public void setTypeList(String[] typeList)
/*     */   {
/*  94 */     this.typeList = typeList;
/*     */   }
/*     */ 
/*     */   public String[] getBasinList()
/*     */   {
/* 101 */     return this.basinList;
/*     */   }
/*     */ 
/*     */   public void setBasinList(String[] basinList)
/*     */   {
/* 108 */     this.basinList = basinList;
/*     */   }
/*     */ 
/*     */   public String[] getTimezones()
/*     */   {
/* 115 */     return this.timezones;
/*     */   }
/*     */ 
/*     */   public void setTimezones(String[] timezones)
/*     */   {
/* 122 */     this.timezones = timezones;
/*     */   }
/*     */ 
/*     */   public String[] getSeverityList()
/*     */   {
/* 129 */     return this.severityList;
/*     */   }
/*     */ 
/*     */   public void setSeverityList(String[] severityList)
/*     */   {
/* 136 */     this.severityList = severityList;
/*     */   }
/*     */ 
/*     */   public String[] getAdvisoryList()
/*     */   {
/* 143 */     return this.advisoryList;
/*     */   }
/*     */ 
/*     */   public void setAdvisoryList(String[] advisoryList)
/*     */   {
/* 150 */     this.advisoryList = advisoryList;
/*     */   }
/*     */ 
/*     */   public String[] getBreakpointTypeList()
/*     */   {
/* 157 */     return this.breakpointTypeList;
/*     */   }
/*     */ 
/*     */   public void setBreakpointTypeList(String[] breakpointTypeList)
/*     */   {
/* 164 */     this.breakpointTypeList = breakpointTypeList;
/*     */   }
/*     */ 
/*     */   public String[] getGeographyTypeList()
/*     */   {
/* 171 */     return this.geographyTypeList;
/*     */   }
/*     */ 
/*     */   public void setGeographyTypeList(String[] geographyTypeList)
/*     */   {
/* 178 */     this.geographyTypeList = geographyTypeList;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.TcaAttrInfo
 * JD-Core Version:    0.6.2
 */