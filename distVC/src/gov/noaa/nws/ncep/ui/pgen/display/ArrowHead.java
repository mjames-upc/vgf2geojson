/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ 
/*     */ public class ArrowHead
/*     */ {
/*     */   private Coordinate location;
/*     */   private double pointAngle;
/*     */   private double direction;
/*     */   private double extent;
/*     */   private ArrowHeadType type;
/*     */ 
/*     */   public ArrowHead(Coordinate location, double pointAngle, double direction, double extent, ArrowHeadType type)
/*     */   {
/*  61 */     this.location = location;
/*  62 */     this.pointAngle = pointAngle;
/*  63 */     this.direction = direction;
/*  64 */     this.extent = extent;
/*  65 */     this.type = type;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getArrowHeadShape()
/*     */   {
/*  74 */     Coordinate[] ahead = null;
/*  75 */     double halfAngle = this.pointAngle * 0.5D;
/*     */ 
/*  81 */     double length = this.extent / Math.sin(Math.toRadians(halfAngle));
/*  82 */     double supp = 180.0D - halfAngle;
/*     */ 
/*  87 */     double radang = Math.toRadians(this.direction + supp);
/*  88 */     Coordinate side1 = new Coordinate(this.location.x + length * Math.cos(radang), 
/*  89 */       this.location.y + length * Math.sin(radang));
/*     */ 
/*  94 */     radang = Math.toRadians(this.direction - supp);
/*  95 */     Coordinate side2 = new Coordinate(this.location.x + length * Math.cos(radang), 
/*  96 */       this.location.y + length * Math.sin(radang));
/*     */ 
/* 101 */     switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$display$ArrowHead$ArrowHeadType()[this.type.ordinal()]) {
/*     */     case 1:
/* 103 */       ahead = new Coordinate[] { side1, this.location, side2 };
/* 104 */       break;
/*     */     case 2:
/* 106 */       ahead = new Coordinate[] { side1, this.location, side2, side1 };
/*     */     }
/*     */ 
/* 110 */     return ahead;
/*     */   }
/*     */ 
/*     */   public double getLength()
/*     */   {
/* 119 */     return this.extent / Math.tan(Math.toRadians(this.pointAngle * 0.5D));
/*     */   }
/*     */ 
/*     */   public static enum ArrowHeadType
/*     */   {
/*  24 */     OPEN, FILLED;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.ArrowHead
 * JD-Core Version:    0.6.2
 */