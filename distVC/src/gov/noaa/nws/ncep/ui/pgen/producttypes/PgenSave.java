/*     */ package gov.noaa.nws.ncep.ui.pgen.producttypes;
/*     */ 
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="")
/*     */ @XmlRootElement(name="PgenSave")
/*     */ public class PgenSave
/*     */ {
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Integer autoSaveFreq;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean autoSave;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean saveLayers;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String outputFile;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String inputFile;
/*     */ 
/*     */   public Integer getAutoSaveFreq()
/*     */   {
/*  64 */     return this.autoSaveFreq;
/*     */   }
/*     */ 
/*     */   public void setAutoSaveFreq(Integer value)
/*     */   {
/*  76 */     this.autoSaveFreq = value;
/*     */   }
/*     */ 
/*     */   public Boolean isAutoSave()
/*     */   {
/*  88 */     return this.autoSave;
/*     */   }
/*     */ 
/*     */   public void setAutoSave(Boolean value)
/*     */   {
/* 100 */     this.autoSave = value;
/*     */   }
/*     */ 
/*     */   public Boolean isSaveLayers()
/*     */   {
/* 112 */     return this.saveLayers;
/*     */   }
/*     */ 
/*     */   public void setSaveLayers(Boolean value)
/*     */   {
/* 124 */     this.saveLayers = value;
/*     */   }
/*     */ 
/*     */   public String getOutputFile()
/*     */   {
/* 136 */     return this.outputFile;
/*     */   }
/*     */ 
/*     */   public void setOutputFile(String value)
/*     */   {
/* 148 */     this.outputFile = value;
/*     */   }
/*     */ 
/*     */   public String getInputFile()
/*     */   {
/* 160 */     return this.inputFile;
/*     */   }
/*     */ 
/*     */   public void setInputFile(String value)
/*     */   {
/* 172 */     this.inputFile = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.producttypes.PgenSave
 * JD-Core Version:    0.6.2
 */