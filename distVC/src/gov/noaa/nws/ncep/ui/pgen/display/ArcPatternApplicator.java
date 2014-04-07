/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.linearref.LengthIndexedLine;
/*     */ 
/*     */ public class ArcPatternApplicator extends AbstractPatternApplicator
/*     */ {
/*  32 */   double startAngle = 0.0D;
/*     */ 
/*  37 */   double endAngle = 0.0D;
/*     */ 
/*  42 */   int numsegs = 0;
/*     */ 
/*  47 */   boolean addSegment = false;
/*     */ 
/*     */   public ArcPatternApplicator(LengthIndexedLine line, double startLoc, double endLoc)
/*     */   {
/*  62 */     super(line, startLoc, endLoc);
/*     */   }
/*     */ 
/*     */   public ArcPatternApplicator(LengthIndexedLine line)
/*     */   {
/*  71 */     super(line);
/*     */   }
/*     */ 
/*     */   public void addSegmentToFill(boolean add)
/*     */   {
/*  79 */     this.addSegment = add;
/*     */   }
/*     */ 
/*     */   public void setArcAttributes(double start, double end, int num)
/*     */   {
/*  90 */     this.startAngle = start;
/*  91 */     this.endAngle = end;
/*  92 */     this.numsegs = num;
/*     */   }
/*     */ 
/*     */   public Coordinate[] calculateFillArea()
/*     */   {
/* 110 */     double[][] arc = calculateLines();
/* 111 */     int arcnum = arc.length;
/*     */     int index;
/*     */     Coordinate[] cfill;
/*     */     int index;
/* 117 */     if (this.addSegment) {
/* 118 */       Coordinate[] path = getSegmentPath();
/* 119 */       Coordinate[] cfill = new Coordinate[path.length + arcnum + 1];
/* 120 */       for (int j = 0; j < path.length; j++) {
/* 121 */         cfill[j] = path[j];
/*     */       }
/*     */ 
/* 124 */       index = path.length;
/*     */     } else {
/* 126 */       cfill = new Coordinate[arcnum + 1];
/* 127 */       index = 0;
/*     */     }
/*     */ 
/* 133 */     for (int i = 0; i < arcnum; i++) {
/* 134 */       cfill[(index++)] = new Coordinate(arc[i][0], arc[i][1]);
/*     */     }
/*     */ 
/* 137 */     cfill[index] = cfill[0];
/*     */ 
/* 139 */     return cfill;
/*     */   }
/*     */ 
/*     */   public double[][] calculateLines()
/*     */   {
/* 181 */     Coordinate mid = getMidpoint();
/* 182 */     double angle = getSegmentAngle();
/* 183 */     double radius = getDistance() / 2.0D;
/* 184 */     double interval = (this.endAngle - this.startAngle) / this.numsegs;
/* 185 */     double theta = angle + this.startAngle;
/* 186 */     double[][] lines = new double[this.numsegs + 1][3];
/*     */ 
/* 191 */     for (int i = 0; i < this.numsegs + 1; i++) {
/* 192 */       lines[i][0] = (radius * Math.cos(Math.toRadians(theta)) + mid.x);
/* 193 */       lines[i][1] = (radius * Math.sin(Math.toRadians(theta)) + mid.y);
/* 194 */       theta += interval;
/*     */     }
/*     */ 
/* 197 */     return lines;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.ArcPatternApplicator
 * JD-Core Version:    0.6.2
 */