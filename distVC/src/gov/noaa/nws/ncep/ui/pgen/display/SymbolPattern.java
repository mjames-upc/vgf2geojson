/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import java.util.ArrayList;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElements;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ public class SymbolPattern
/*     */ {
/*     */ 
/*     */   @XmlAttribute
/*     */   private String name;
/*     */ 
/*     */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="pathPart", type=SymbolPathPart.class), @javax.xml.bind.annotation.XmlElement(name="circlePart", type=SymbolCirclePart.class)})
/*     */   private ArrayList<SymbolPart> parts;
/*     */ 
/*     */   public SymbolPattern()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SymbolPattern(String name)
/*     */   {
/*  54 */     this.name = name;
/*  55 */     this.parts = new ArrayList();
/*     */   }
/*     */ 
/*     */   public SymbolPattern(String name, ArrayList<SymbolPart> parts)
/*     */   {
/*  64 */     this.name = name;
/*  65 */     this.parts = parts;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  73 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/*  81 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public ArrayList<SymbolPart> getParts()
/*     */   {
/*  89 */     return this.parts;
/*     */   }
/*     */ 
/*     */   public void setParts(ArrayList<SymbolPart> parts)
/*     */   {
/*  97 */     this.parts = parts;
/*     */   }
/*     */ 
/*     */   public void addPath(Coordinate[] path)
/*     */   {
/* 106 */     this.parts.add(new SymbolPathPart(path, false));
/*     */   }
/*     */ 
/*     */   public void addPath(Coordinate[] path, boolean fill)
/*     */   {
/* 116 */     this.parts.add(new SymbolPathPart(path, fill));
/*     */   }
/*     */ 
/*     */   public void addDot(Coordinate center, double radius)
/*     */   {
/* 125 */     this.parts.add(new SymbolCirclePart(center, radius, true));
/*     */   }
/*     */ 
/*     */   public void addCircle(Coordinate center, double radius)
/*     */   {
/* 134 */     this.parts.add(new SymbolCirclePart(center, radius, false));
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.SymbolPattern
 * JD-Core Version:    0.6.2
 */