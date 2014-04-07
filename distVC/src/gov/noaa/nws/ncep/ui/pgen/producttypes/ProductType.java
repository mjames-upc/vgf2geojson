/*     */ package gov.noaa.nws.ncep.ui.pgen.producttypes;
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
/*     */ @XmlType(name="", propOrder={"pgenControls", "pgenActions", "pgenClass", "pgenLayer", "pgenSave", "prodType", "pgenSettingsFile", "clipFlag", "clipBoundsTable", "clipBoundsName"})
/*     */ @XmlRootElement(name="ProductType")
/*     */ public class ProductType
/*     */ {
/*     */ 
/*     */   @XmlElement(name="PgenControls")
/*     */   protected PgenControls pgenControls;
/*     */ 
/*     */   @XmlElement(name="PgenActions")
/*     */   protected PgenActions pgenActions;
/*     */ 
/*     */   @XmlElement(name="PgenClass")
/*     */   protected List<PgenClass> pgenClass;
/*     */ 
/*     */   @XmlElement(name="PgenLayer")
/*     */   protected List<PgenLayer> pgenLayer;
/*     */ 
/*     */   @XmlElement(name="PgenSave")
/*     */   protected PgenSave pgenSave;
/*     */ 
/*     */   @XmlElement(name="ProdType")
/*     */   protected List<ProdType> prodType;
/*     */ 
/*     */   @XmlElement(name="PgenSettingsFile", required=true)
/*     */   protected String pgenSettingsFile;
/*     */ 
/*     */   @XmlElement(name="ClipFlag")
/*     */   protected Boolean clipFlag;
/*     */ 
/*     */   @XmlElement(name="ClipBoundsTable")
/*     */   protected String clipBoundsTable;
/*     */ 
/*     */   @XmlElement(name="ClipBoundsName")
/*     */   protected String clipBoundsName;
/*     */ 
/*     */   @XmlAttribute(name="Subtype")
/*     */   protected String subtype;
/*     */ 
/*     */   @XmlAttribute(name="Type")
/*     */   protected String type;
/*     */ 
/*     */   @XmlAttribute(name="Name")
/*     */   protected String name;
/*     */ 
/*     */   public PgenControls getPgenControls()
/*     */   {
/*  98 */     return this.pgenControls;
/*     */   }
/*     */ 
/*     */   public void setPgenControls(PgenControls value)
/*     */   {
/* 110 */     this.pgenControls = value;
/*     */   }
/*     */ 
/*     */   public PgenActions getPgenActions()
/*     */   {
/* 122 */     return this.pgenActions;
/*     */   }
/*     */ 
/*     */   public void setPgenActions(PgenActions value)
/*     */   {
/* 134 */     this.pgenActions = value;
/*     */   }
/*     */ 
/*     */   public List<PgenClass> getPgenClass()
/*     */   {
/* 160 */     if (this.pgenClass == null) {
/* 161 */       this.pgenClass = new ArrayList();
/*     */     }
/* 163 */     return this.pgenClass;
/*     */   }
/*     */ 
/*     */   public List<PgenLayer> getPgenLayer()
/*     */   {
/* 189 */     if (this.pgenLayer == null) {
/* 190 */       this.pgenLayer = new ArrayList();
/*     */     }
/* 192 */     return this.pgenLayer;
/*     */   }
/*     */ 
/*     */   public PgenSave getPgenSave()
/*     */   {
/* 204 */     return this.pgenSave;
/*     */   }
/*     */ 
/*     */   public void setPgenSave(PgenSave value)
/*     */   {
/* 216 */     this.pgenSave = value;
/*     */   }
/*     */ 
/*     */   public List<ProdType> getProdType()
/*     */   {
/* 242 */     if (this.prodType == null) {
/* 243 */       this.prodType = new ArrayList();
/*     */     }
/* 245 */     return this.prodType;
/*     */   }
/*     */ 
/*     */   public String getPgenSettingsFile()
/*     */   {
/* 257 */     return this.pgenSettingsFile;
/*     */   }
/*     */ 
/*     */   public void setPgenSettingsFile(String value)
/*     */   {
/* 269 */     this.pgenSettingsFile = value;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 281 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String value)
/*     */   {
/* 293 */     this.name = value;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 305 */     return this.type;
/*     */   }
/*     */ 
/*     */   public void setType(String value)
/*     */   {
/* 317 */     this.type = value;
/*     */   }
/*     */ 
/*     */   public String getSubtype()
/*     */   {
/* 329 */     return this.subtype;
/*     */   }
/*     */ 
/*     */   public void setSubtype(String value)
/*     */   {
/* 341 */     this.subtype = value;
/*     */   }
/*     */ 
/*     */   public Boolean getClipFlag()
/*     */   {
/* 348 */     return this.clipFlag;
/*     */   }
/*     */ 
/*     */   public void setClipFlag(Boolean clipFlag)
/*     */   {
/* 355 */     this.clipFlag = clipFlag;
/*     */   }
/*     */ 
/*     */   public String getClipBoundsTable()
/*     */   {
/* 362 */     return this.clipBoundsTable;
/*     */   }
/*     */ 
/*     */   public void setClipBoundsTable(String table)
/*     */   {
/* 369 */     this.clipBoundsTable = table;
/*     */   }
/*     */ 
/*     */   public String getClipBoundsName()
/*     */   {
/* 376 */     return this.clipBoundsName;
/*     */   }
/*     */ 
/*     */   public void setClipBoundsName(String clipBoundsName)
/*     */   {
/* 383 */     this.clipBoundsName = clipBoundsName;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType
 * JD-Core Version:    0.6.2
 */