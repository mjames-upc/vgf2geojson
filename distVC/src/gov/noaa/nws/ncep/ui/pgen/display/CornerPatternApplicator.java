/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.linearref.LengthIndexedLine;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class CornerPatternApplicator extends AbstractPatternApplicator
/*     */ {
/*  33 */   private double height = 1.0D;
/*  34 */   private CornerPattern patternType = CornerPattern.BOX;
/*     */ 
/*     */   public CornerPatternApplicator(LengthIndexedLine line)
/*     */   {
/*  42 */     super(line);
/*     */   }
/*     */ 
/*     */   public CornerPatternApplicator(LengthIndexedLine line, double startLoc, double endLoc)
/*     */   {
/*  58 */     super(line, startLoc, endLoc);
/*     */   }
/*     */ 
/*     */   public void setHeight(double height)
/*     */   {
/*  66 */     this.height = height;
/*     */   }
/*     */ 
/*     */   public void setPatternType(CornerPattern type)
/*     */   {
/*  75 */     this.patternType = type;
/*     */   }
/*     */ 
/*     */   public Coordinate[] calculateFillArea()
/*     */   {
/*  92 */     int cnum = 5;
/*  93 */     double[][] corner = new double[cnum][3];
/*  94 */     Coordinate[] cfill = new Coordinate[cnum];
/*     */ 
/*  97 */     corner = calculateLines();
/*     */ 
/* 101 */     for (int i = 0; i < cnum; i++) {
/* 102 */       cfill[i] = new Coordinate(corner[i][0], corner[i][1]);
/*     */     }
/*     */ 
/* 105 */     return cfill;
/*     */   }
/*     */ 
/*     */   public double[][] calculateLines()
/*     */   {
/* 129 */     double theta = getSegmentAngle();
/*     */ 
/* 131 */     double xOffset = this.height * Math.cos(Math.toRadians(90.0D + theta));
/* 132 */     double yOffset = this.height * Math.sin(Math.toRadians(90.0D + theta));
/*     */     double[][] corners;
/* 134 */     switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$display$CornerPatternApplicator$CornerPattern()[this.patternType.ordinal()]) {
/*     */     case 1:
/* 136 */       int cnum = 5;
/* 137 */       double[][] corners = new double[cnum][3];
/*     */ 
/* 139 */       corners[0][0] = (getStartPoint().x + xOffset);
/* 140 */       corners[0][1] = (getStartPoint().y + yOffset);
/*     */ 
/* 142 */       corners[1][0] = (getStartPoint().x - xOffset);
/* 143 */       corners[1][1] = (getStartPoint().y - yOffset);
/*     */ 
/* 145 */       corners[2][0] = (getEndPoint().x - xOffset);
/* 146 */       corners[2][1] = (getEndPoint().y - yOffset);
/*     */ 
/* 148 */       corners[3][0] = (getEndPoint().x + xOffset);
/* 149 */       corners[3][1] = (getEndPoint().y + yOffset);
/*     */ 
/* 151 */       corners[4] = corners[0];
/* 152 */       break;
/*     */     case 4:
/* 155 */       int cnum = 4;
/* 156 */       double[][] corners = new double[cnum][3];
/*     */ 
/* 158 */       corners[0][0] = (getStartPoint().x + xOffset);
/* 159 */       corners[0][1] = (getStartPoint().y + yOffset);
/*     */ 
/* 161 */       corners[1][0] = (getEndPoint().x + xOffset);
/* 162 */       corners[1][1] = (getEndPoint().y + yOffset);
/*     */ 
/* 164 */       corners[2][0] = (getStartPoint().x - xOffset);
/* 165 */       corners[2][1] = (getStartPoint().y - yOffset);
/*     */ 
/* 167 */       corners[3][0] = (getEndPoint().x - xOffset);
/* 168 */       corners[3][1] = (getEndPoint().y - yOffset);
/* 169 */       break;
/*     */     case 2:
/* 172 */       int cnum = 4;
/* 173 */       double[][] corners = new double[cnum][3];
/*     */ 
/* 175 */       corners[0][0] = (getStartPoint().x + xOffset);
/* 176 */       corners[0][1] = (getStartPoint().y + yOffset);
/*     */ 
/* 178 */       corners[1][0] = (getEndPoint().x - xOffset);
/* 179 */       corners[1][1] = (getEndPoint().y - yOffset);
/*     */ 
/* 181 */       corners[2][0] = (getStartPoint().x - xOffset);
/* 182 */       corners[2][1] = (getStartPoint().y - yOffset);
/*     */ 
/* 184 */       corners[3][0] = (getEndPoint().x + xOffset);
/* 185 */       corners[3][1] = (getEndPoint().y + yOffset);
/* 186 */       break;
/*     */     case 3:
/* 189 */       int cnum = 4;
/* 190 */       double[][] corners = new double[cnum][3];
/*     */ 
/* 192 */       corners[0][0] = (getStartPoint().x + xOffset);
/* 193 */       corners[0][1] = (getStartPoint().y + yOffset);
/*     */ 
/* 195 */       corners[1][0] = (getEndPoint().x + xOffset);
/* 196 */       corners[1][1] = (getEndPoint().y + yOffset);
/*     */ 
/* 198 */       corners[2][0] = (getStartPoint().x - xOffset);
/* 199 */       corners[2][1] = (getStartPoint().y - yOffset);
/*     */ 
/* 201 */       corners[3][0] = (getEndPoint().x - xOffset);
/* 202 */       corners[3][1] = (getEndPoint().y - yOffset);
/* 203 */       break;
/*     */     case 5:
/* 206 */       int cnum = 2;
/* 207 */       double[][] corners = new double[cnum][3];
/*     */ 
/* 209 */       corners[0][0] = getEndPoint().x;
/* 210 */       corners[0][1] = getEndPoint().y;
/*     */ 
/* 212 */       corners[1][0] = (getEndPoint().x - xOffset);
/* 213 */       corners[1][1] = (getEndPoint().y - yOffset);
/* 214 */       break;
/*     */     default:
/* 218 */       corners = null;
/* 219 */       System.out.println("Corner type is not set.");
/*     */     }
/*     */ 
/* 223 */     return corners;
/*     */   }
/*     */ 
/*     */   public static enum CornerPattern
/*     */   {
/*  28 */     BOX, X_PATTERN, Z_PATTERN, DOUBLE_LINE, TICK;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.CornerPatternApplicator
 * JD-Core Version:    0.6.2
 */