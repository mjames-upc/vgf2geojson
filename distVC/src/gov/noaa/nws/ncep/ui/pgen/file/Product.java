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
/*     */ @XmlType(name="", propOrder={"layer"})
/*     */ @XmlRootElement(name="Product")
/*     */ public class Product
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Layer", required=true)
/*     */   protected List<Layer> layer;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String inputFile;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String outputFile;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean useFile;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean saveLayers;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean onOff;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String center;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String forecaster;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String type;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String name;
/*     */ 
/*     */   public List<Layer> getLayer()
/*     */   {
/* 100 */     if (this.layer == null) {
/* 101 */       this.layer = new ArrayList();
/*     */     }
/* 103 */     return this.layer;
/*     */   }
/*     */ 
/*     */   public String getInputFile()
/*     */   {
/* 115 */     return this.inputFile;
/*     */   }
/*     */ 
/*     */   public void setInputFile(String value)
/*     */   {
/* 127 */     this.inputFile = value;
/*     */   }
/*     */ 
/*     */   public String getOutputFile()
/*     */   {
/* 139 */     return this.outputFile;
/*     */   }
/*     */ 
/*     */   public void setOutputFile(String value)
/*     */   {
/* 151 */     this.outputFile = value;
/*     */   }
/*     */ 
/*     */   public Boolean isUseFile()
/*     */   {
/* 163 */     return this.useFile;
/*     */   }
/*     */ 
/*     */   public void setUseFile(Boolean value)
/*     */   {
/* 175 */     this.useFile = value;
/*     */   }
/*     */ 
/*     */   public Boolean isSaveLayers()
/*     */   {
/* 187 */     return this.saveLayers;
/*     */   }
/*     */ 
/*     */   public void setSaveLayers(Boolean value)
/*     */   {
/* 199 */     this.saveLayers = value;
/*     */   }
/*     */ 
/*     */   public Boolean isOnOff()
/*     */   {
/* 211 */     return this.onOff;
/*     */   }
/*     */ 
/*     */   public void setOnOff(Boolean value)
/*     */   {
/* 223 */     this.onOff = value;
/*     */   }
/*     */ 
/*     */   public String getCenter()
/*     */   {
/* 235 */     return this.center;
/*     */   }
/*     */ 
/*     */   public void setCenter(String value)
/*     */   {
/* 247 */     this.center = value;
/*     */   }
/*     */ 
/*     */   public String getForecaster()
/*     */   {
/* 259 */     return this.forecaster;
/*     */   }
/*     */ 
/*     */   public void setForecaster(String value)
/*     */   {
/* 271 */     this.forecaster = value;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 283 */     return this.type;
/*     */   }
/*     */ 
/*     */   public void setType(String value)
/*     */   {
/* 295 */     this.type = value;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 307 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String value)
/*     */   {
/* 319 */     this.name = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Product
 * JD-Core Version:    0.6.2
 */