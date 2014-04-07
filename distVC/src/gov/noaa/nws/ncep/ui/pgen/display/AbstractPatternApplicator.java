/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.linearref.LengthIndexedLine;
/*     */ 
/*     */ public abstract class AbstractPatternApplicator
/*     */ {
/*     */   protected LengthIndexedLine line;
/*     */   protected double startLoc;
/*     */   private Coordinate startPoint;
/*     */   protected double endLoc;
/*     */   private Coordinate endPoint;
/*     */ 
/*     */   protected AbstractPatternApplicator(LengthIndexedLine line)
/*     */   {
/*  52 */     this(line, 0.0D, line.getEndIndex());
/*     */   }
/*     */ 
/*     */   protected AbstractPatternApplicator(LengthIndexedLine line, double startLoc, double endLoc)
/*     */   {
/*  65 */     this.line = line;
/*  66 */     this.startLoc = startLoc;
/*  67 */     this.startPoint = line.extractPoint(startLoc);
/*  68 */     this.endLoc = endLoc;
/*  69 */     this.endPoint = line.extractPoint(endLoc);
/*     */   }
/*     */ 
/*     */   public double getStartLoc()
/*     */   {
/*  76 */     return this.startLoc;
/*     */   }
/*     */ 
/*     */   protected void setStartLoc(double startLoc)
/*     */   {
/*  83 */     this.startLoc = startLoc;
/*  84 */     this.startPoint = this.line.extractPoint(startLoc);
/*     */   }
/*     */ 
/*     */   public double getEndLoc()
/*     */   {
/*  91 */     return this.endLoc;
/*     */   }
/*     */ 
/*     */   protected void setEndLoc(double endLoc)
/*     */   {
/*  98 */     this.endLoc = endLoc;
/*  99 */     this.endPoint = this.line.extractPoint(endLoc);
/*     */   }
/*     */ 
/*     */   protected Coordinate getStartPoint()
/*     */   {
/* 108 */     return this.startPoint;
/*     */   }
/*     */ 
/*     */   protected Coordinate getEndPoint()
/*     */   {
/* 117 */     return this.endPoint;
/*     */   }
/*     */ 
/*     */   protected Coordinate getMidpoint()
/*     */   {
/* 125 */     Coordinate c1 = getStartPoint();
/* 126 */     Coordinate c2 = getEndPoint();
/* 127 */     return new Coordinate((c1.x + c2.x) / 2.0D, (c1.y + c2.y) / 2.0D);
/*     */   }
/*     */ 
/*     */   protected double getDistance()
/*     */   {
/* 135 */     Coordinate c1 = getStartPoint();
/* 136 */     Coordinate c2 = getEndPoint();
/*     */ 
/* 138 */     return c1.distance(c2);
/*     */   }
/*     */ 
/*     */   protected double getSegmentAngle()
/*     */   {
/* 146 */     Coordinate c1 = getStartPoint();
/* 147 */     Coordinate c2 = getEndPoint();
/*     */ 
/* 149 */     return Math.toDegrees(Math.atan2(c2.y - c1.y, c2.x - c1.x));
/*     */   }
/*     */ 
/*     */   protected Coordinate[] getSegmentPath()
/*     */   {
/* 157 */     return this.line.extractLine(this.startLoc, this.endLoc).getCoordinates();
/*     */   }
/*     */ 
/*     */   protected double[][] getSegmentPts()
/*     */   {
/* 165 */     Coordinate[] tmp = getSegmentPath();
/* 166 */     double[][] newpts = new double[tmp.length][3];
/* 167 */     for (int k = 0; k < tmp.length; k++) {
/* 168 */       newpts[k][0] = tmp[k].x;
/* 169 */       newpts[k][1] = tmp[k].y;
/*     */     }
/* 171 */     return newpts;
/*     */   }
/*     */ 
/*     */   public abstract Coordinate[] calculateFillArea();
/*     */ 
/*     */   public abstract double[][] calculateLines();
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.AbstractPatternApplicator
 * JD-Core Version:    0.6.2
 */