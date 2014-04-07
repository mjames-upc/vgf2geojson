/*     */ package gov.noaa.nws.ncep.ui.pgen.producttypes;
/*     */ 
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="", propOrder={"color"})
/*     */ @XmlRootElement(name="PgenLayer")
/*     */ public class PgenLayer
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Color", required=true)
/*     */   protected Color color;
/*     */ 
/*     */   @XmlAttribute(name="OutputFile")
/*     */   protected String outputFile;
/*     */ 
/*     */   @XmlAttribute(name="InputFile")
/*     */   protected String inputFile;
/*     */ 
/*     */   @XmlAttribute(name="Filled")
/*     */   protected Boolean filled;
/*     */ 
/*     */   @XmlAttribute(name="MonoColor")
/*     */   protected Boolean monoColor;
/*     */ 
/*     */   @XmlAttribute(name="OnOff")
/*     */   protected Boolean onOff;
/*     */ 
/*     */   @XmlAttribute(name="Name")
/*     */   protected String name;
/*     */ 
/*     */   public Color getColor()
/*     */   {
/*  75 */     return this.color;
/*     */   }
/*     */ 
/*     */   public void setColor(Color value)
/*     */   {
/*  87 */     this.color = value;
/*     */   }
/*     */ 
/*     */   public String getOutputFile()
/*     */   {
/*  99 */     return this.outputFile;
/*     */   }
/*     */ 
/*     */   public void setOutputFile(String value)
/*     */   {
/* 111 */     this.outputFile = value;
/*     */   }
/*     */ 
/*     */   public String getInputFile()
/*     */   {
/* 123 */     return this.inputFile;
/*     */   }
/*     */ 
/*     */   public void setInputFile(String value)
/*     */   {
/* 135 */     this.inputFile = value;
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 147 */     return this.filled;
/*     */   }
/*     */ 
/*     */   public void setFilled(Boolean value)
/*     */   {
/* 159 */     this.filled = value;
/*     */   }
/*     */ 
/*     */   public Boolean isMonoColor()
/*     */   {
/* 171 */     return this.monoColor;
/*     */   }
/*     */ 
/*     */   public void setMonoColor(Boolean value)
/*     */   {
/* 183 */     this.monoColor = value;
/*     */   }
/*     */ 
/*     */   public Boolean isOnOff()
/*     */   {
/* 195 */     return this.onOff;
/*     */   }
/*     */ 
/*     */   public void setOnOff(Boolean value)
/*     */   {
/* 207 */     this.onOff = value;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 219 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String value)
/*     */   {
/* 231 */     this.name = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.producttypes.PgenLayer
 * JD-Core Version:    0.6.2
 */