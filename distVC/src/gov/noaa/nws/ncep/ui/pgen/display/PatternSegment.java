/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ public class PatternSegment
/*     */   implements ISerializableObject
/*     */ {
/*     */ 
/*     */   @XmlAttribute
/*     */   private double length;
/*     */ 
/*     */   @XmlAttribute(name="type")
/*     */   private PatternType attribute;
/*     */ 
/*     */   @XmlAttribute
/*     */   private int colorLocation;
/*     */ 
/*     */   @XmlAttribute
/*     */   private int numberInArc;
/*     */ 
/*     */   @XmlAttribute
/*     */   private int offsetSize;
/*     */ 
/*     */   @XmlAttribute
/*     */   private boolean reverseSide;
/*     */ 
/*     */   public PatternSegment()
/*     */   {
/*     */   }
/*     */ 
/*     */   public PatternSegment(double length, PatternType attribute, int colorLocation, int numberInArc, int offset, boolean reverseSide)
/*     */   {
/*  93 */     this.length = length;
/*  94 */     this.attribute = attribute;
/*  95 */     this.colorLocation = colorLocation;
/*  96 */     this.numberInArc = numberInArc;
/*  97 */     this.offsetSize = offset;
/*  98 */     this.reverseSide = reverseSide;
/*     */   }
/*     */ 
/*     */   public double getLength()
/*     */   {
/* 106 */     return this.length;
/*     */   }
/*     */ 
/*     */   public void setLength(double length)
/*     */   {
/* 114 */     this.length = length;
/*     */   }
/*     */ 
/*     */   public PatternType getPatternType()
/*     */   {
/* 122 */     return this.attribute;
/*     */   }
/*     */ 
/*     */   public void setPatternType(PatternType attr)
/*     */   {
/* 130 */     this.attribute = attr;
/*     */   }
/*     */ 
/*     */   public int getColorLocation()
/*     */   {
/* 138 */     return this.colorLocation;
/*     */   }
/*     */ 
/*     */   public void setColorLocation(int colorLocation)
/*     */   {
/* 146 */     this.colorLocation = colorLocation;
/*     */   }
/*     */ 
/*     */   public int getNumberInArc()
/*     */   {
/* 154 */     return this.numberInArc;
/*     */   }
/*     */ 
/*     */   public void setNumberInArc(int numberInArc)
/*     */   {
/* 162 */     this.numberInArc = numberInArc;
/*     */   }
/*     */ 
/*     */   public int getOffsetSize()
/*     */   {
/* 170 */     return this.offsetSize;
/*     */   }
/*     */ 
/*     */   public void setOffsetSize(int offset)
/*     */   {
/* 178 */     this.offsetSize = offset;
/*     */   }
/*     */ 
/*     */   public boolean isReverseSide()
/*     */   {
/* 187 */     return this.reverseSide;
/*     */   }
/*     */ 
/*     */   public void setReverseSide(boolean reverseSide)
/*     */   {
/* 195 */     this.reverseSide = reverseSide;
/*     */   }
/*     */ 
/*     */   public PatternSegment copy()
/*     */   {
/* 200 */     return new PatternSegment(this.length, this.attribute, this.colorLocation, 
/* 201 */       this.numberInArc, this.offsetSize, this.reverseSide);
/*     */   }
/*     */ 
/*     */   public static enum PatternType
/*     */   {
/*  31 */     BLANK, LINE, 
/*  32 */     CIRCLE, CIRCLE_FILLED, ARC_180_DEGREE, ARC_180_DEGREE_FILLED, 
/*  33 */     ARC_180_DEGREE_CLOSED, ARC_90_DEGREE, ARC_270_DEGREE, ARC_270_DEGREE_WITH_LINE, 
/*  34 */     BOX, BOX_FILLED, X_PATTERN, Z_PATTERN, DOUBLE_LINE, TICK, ARROW_HEAD;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.PatternSegment
 * JD-Core Version:    0.6.2
 */