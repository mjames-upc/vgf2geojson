/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="", propOrder={"color", "drawableElement"})
/*     */ @XmlRootElement(name="Layer")
/*     */ public class Layer
/*     */ {
/*     */ 
/*     */   @XmlElement(name="Color", required=true)
/*     */   protected Color color;
/*     */ 
/*     */   @XmlElement(name="DrawableElement")
/*     */   protected DrawableElement drawableElement;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String outputFile;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String inputFile;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean filled;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean monoColor;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected Boolean onOff;
/*     */ 
/*     */   @XmlAttribute
/*     */   protected String name;
/*     */ 
/*     */   public Color getColor()
/*     */   {
/*  79 */     return this.color;
/*     */   }
/*     */ 
/*     */   public void setColor(Color value)
/*     */   {
/*  91 */     this.color = value;
/*     */   }
/*     */ 
/*     */   public DrawableElement getDrawableElement()
/*     */   {
/* 103 */     return this.drawableElement;
/*     */   }
/*     */ 
/*     */   public void setDrawableElement(DrawableElement value)
/*     */   {
/* 115 */     this.drawableElement = value;
/*     */   }
/*     */ 
/*     */   public String getOutputFile()
/*     */   {
/* 127 */     return this.outputFile;
/*     */   }
/*     */ 
/*     */   public void setOutputFile(String value)
/*     */   {
/* 139 */     this.outputFile = value;
/*     */   }
/*     */ 
/*     */   public String getInputFile()
/*     */   {
/* 151 */     return this.inputFile;
/*     */   }
/*     */ 
/*     */   public void setInputFile(String value)
/*     */   {
/* 163 */     this.inputFile = value;
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 175 */     return this.filled;
/*     */   }
/*     */ 
/*     */   public void setFilled(Boolean value)
/*     */   {
/* 187 */     this.filled = value;
/*     */   }
/*     */ 
/*     */   public Boolean isMonoColor()
/*     */   {
/* 199 */     return this.monoColor;
/*     */   }
/*     */ 
/*     */   public void setMonoColor(Boolean value)
/*     */   {
/* 211 */     this.monoColor = value;
/*     */   }
/*     */ 
/*     */   public Boolean isOnOff()
/*     */   {
/* 223 */     return this.onOff;
/*     */   }
/*     */ 
/*     */   public void setOnOff(Boolean value)
/*     */   {
/* 235 */     this.onOff = value;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 247 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String value)
/*     */   {
/* 259 */     this.name = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Layer
 * JD-Core Version:    0.6.2
 */