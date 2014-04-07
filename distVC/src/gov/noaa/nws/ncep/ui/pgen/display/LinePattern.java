/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*     */ import java.util.ArrayList;
/*     */ import javax.xml.bind.annotation.XmlAccessOrder;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorOrder;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ 
/*     */ @XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ public class LinePattern
/*     */   implements ISerializableObject
/*     */ {
/*     */ 
/*     */   @XmlAttribute(name="name")
/*     */   private String name;
/*     */ 
/*     */   @XmlElement(name="patternSegment")
/*     */   private ArrayList<PatternSegment> segments;
/*     */ 
/*     */   @XmlAttribute(name="hasArrowHead")
/*     */   private boolean arrowHead;
/*     */ 
/*     */   @XmlAttribute
/*     */   private ArrowHead.ArrowHeadType arrowHeadType;
/*     */ 
/*     */   public LinePattern()
/*     */   {
/*  60 */     this.segments = new ArrayList();
/*     */   }
/*     */ 
/*     */   public LinePattern(String name, ArrayList<PatternSegment> segments, boolean arrowHead, ArrowHead.ArrowHeadType arrowHeadType)
/*     */   {
/*  72 */     this.name = name;
/*  73 */     this.segments = segments;
/*  74 */     this.arrowHead = arrowHead;
/*  75 */     this.arrowHeadType = arrowHeadType;
/*     */ 
/*  77 */     if ((this.arrowHeadType == null) && (this.arrowHead)) this.arrowHeadType = ArrowHead.ArrowHeadType.OPEN;
/*     */   }
/*     */ 
/*     */   public LinePattern(String name, boolean arrowHead, ArrowHead.ArrowHeadType arrowHeadType)
/*     */   {
/*  89 */     this(name, new ArrayList(), arrowHead, arrowHeadType);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  98 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 106 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public ArrayList<PatternSegment> getSegments()
/*     */   {
/* 114 */     return this.segments;
/*     */   }
/*     */ 
/*     */   public void setSegments(ArrayList<PatternSegment> segments)
/*     */   {
/* 122 */     this.segments = segments;
/*     */   }
/*     */ 
/*     */   public boolean hasArrowHead()
/*     */   {
/* 130 */     return this.arrowHead;
/*     */   }
/*     */ 
/*     */   public void setArrowHead(boolean arrowHead)
/*     */   {
/* 138 */     this.arrowHead = arrowHead;
/*     */   }
/*     */ 
/*     */   public ArrowHead.ArrowHeadType getArrowHeadType()
/*     */   {
/* 146 */     return this.arrowHeadType;
/*     */   }
/*     */ 
/*     */   public void setArrowHeadType(ArrowHead.ArrowHeadType arrowHeadType)
/*     */   {
/* 154 */     this.arrowHeadType = arrowHeadType;
/*     */   }
/*     */ 
/*     */   public double getLength()
/*     */   {
/* 163 */     double length = 0.0D;
/* 164 */     for (PatternSegment seg : this.segments) {
/* 165 */       length += seg.getLength();
/*     */     }
/* 167 */     return length;
/*     */   }
/*     */ 
/*     */   public int getNumSegments()
/*     */   {
/* 176 */     return this.segments.size();
/*     */   }
/*     */ 
/*     */   public void addSegment(PatternSegment ps)
/*     */   {
/* 184 */     this.segments.add(ps);
/*     */   }
/*     */ 
/*     */   public double getMaxExtent()
/*     */   {
/* 189 */     double extent = 1.0D;
/*     */ 
/* 192 */     for (PatternSegment seg : this.segments)
/*     */     {
/*     */       double temp;
/*     */       double temp;
/*     */       double temp;
/* 193 */       switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$display$PatternSegment$PatternType()[seg.getPatternType().ordinal()])
/*     */       {
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 17:
/* 204 */         temp = seg.getLength() * 0.5D;
/* 205 */         break;
/*     */       case 11:
/*     */       case 12:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/* 214 */         temp = seg.getOffsetSize();
/* 215 */         break;
/*     */       default:
/* 217 */         temp = 1.0D;
/*     */       }
/*     */ 
/* 220 */       extent = Math.max(extent, temp);
/*     */     }
/*     */ 
/* 223 */     return extent;
/*     */   }
/*     */ 
/*     */   public boolean needsLengthUpdate()
/*     */   {
/* 232 */     if (this.segments.isEmpty()) return false;
/*     */ 
/* 234 */     for (PatternSegment seg : this.segments) {
/* 235 */       if (seg.getLength() == 0.0D) return true;
/*     */     }
/*     */ 
/* 238 */     return false;
/*     */   }
/*     */ 
/*     */   public LinePattern updateLength(double length)
/*     */   {
/* 248 */     LinePattern lp = new LinePattern(this.name, this.arrowHead, this.arrowHeadType);
/*     */ 
/* 250 */     for (PatternSegment seg : this.segments) {
/* 251 */       PatternSegment newseg = seg.copy();
/* 252 */       if (newseg.getLength() == 0.0D) newseg.setLength(length);
/* 253 */       lp.addSegment(newseg);
/*     */     }
/*     */ 
/* 256 */     return lp;
/*     */   }
/*     */ 
/*     */   public LinePattern flipSide()
/*     */   {
/* 265 */     LinePattern lp = new LinePattern(this.name, this.arrowHead, this.arrowHeadType);
/*     */ 
/* 267 */     for (PatternSegment seg : this.segments) {
/* 268 */       PatternSegment newseg = seg.copy();
/* 269 */       newseg.setReverseSide(!seg.isReverseSide());
/* 270 */       lp.addSegment(newseg);
/*     */     }
/*     */ 
/* 273 */     return lp;
/*     */   }
/*     */ 
/*     */   public LinePattern scaleToLength(double length)
/*     */   {
/* 283 */     LinePattern lp = new LinePattern(this.name, this.arrowHead, this.arrowHeadType);
/*     */ 
/* 285 */     double scale = length / getLength();
/*     */ 
/* 287 */     for (PatternSegment seg : this.segments) {
/* 288 */       PatternSegment newseg = seg.copy();
/* 289 */       newseg.setLength(seg.getLength() * scale);
/* 290 */       lp.addSegment(newseg);
/*     */     }
/*     */ 
/* 293 */     return lp;
/*     */   }
/*     */ 
/*     */   public LinePattern scaleBlankLineToLength(double length)
/*     */   {
/* 305 */     LinePattern lp = new LinePattern(this.name, this.arrowHead, this.arrowHeadType);
/*     */ 
/* 310 */     double segsum = 0.0D;
/* 311 */     for (PatternSegment seg : this.segments) {
/* 312 */       if ((seg.getPatternType() == PatternSegment.PatternType.BLANK) || 
/* 313 */         (seg.getPatternType() == PatternSegment.PatternType.LINE)) {
/* 314 */         segsum += seg.getLength();
/*     */       }
/*     */     }
/*     */ 
/* 318 */     double scale = 1.0D;
/* 319 */     if (segsum > 0.0D) {
/* 320 */       scale = (length - getLength()) / segsum;
/* 321 */       scale += 1.0D;
/*     */     }
/*     */ 
/* 327 */     for (PatternSegment seg : this.segments) {
/* 328 */       PatternSegment newseg = seg.copy();
/* 329 */       if ((seg.getPatternType() == PatternSegment.PatternType.BLANK) || 
/* 330 */         (seg.getPatternType() == PatternSegment.PatternType.LINE)) {
/* 331 */         newseg.setLength(seg.getLength() * scale);
/*     */       }
/* 333 */       lp.addSegment(newseg);
/*     */     }
/*     */ 
/* 336 */     return lp;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.LinePattern
 * JD-Core Version:    0.6.2
 */