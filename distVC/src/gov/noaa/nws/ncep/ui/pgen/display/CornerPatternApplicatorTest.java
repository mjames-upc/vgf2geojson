/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.linearref.LengthIndexedLine;
/*     */ import org.junit.After;
/*     */ import org.junit.AfterClass;
/*     */ import org.junit.Assert;
/*     */ import org.junit.Before;
/*     */ import org.junit.BeforeClass;
/*     */ import org.junit.Test;
/*     */ 
/*     */ public class CornerPatternApplicatorTest
/*     */ {
/*  33 */   private final double epsilon = 1.E-09D;
/*     */ 
/*     */   @BeforeClass
/*     */   public static void setUpBeforeClass()
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   @AfterClass
/*     */   public static void tearDownAfterClass()
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   @Before
/*     */   public void setUp()
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   @After
/*     */   public void tearDown()
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testCalculateFillArea()
/*     */   {
/*  70 */     GeometryFactory gf = new GeometryFactory();
/*  71 */     Coordinate[] coords = { new Coordinate(0.0D, 0.0D), 
/*  72 */       new Coordinate(0.0D, 4.0D), 
/*  73 */       new Coordinate(2.0D, 6.0D), 
/*  74 */       new Coordinate(4.0D, 4.0D), 
/*  75 */       new Coordinate(4.0D, 0.0D), 
/*  76 */       new Coordinate(0.0D, 0.0D) };
/*  77 */     LineString path = gf.createLineString(coords);
/*  78 */     LengthIndexedLine line = new LengthIndexedLine(path);
/*     */ 
/*  82 */     CornerPatternApplicator boxapp = new CornerPatternApplicator(line, 1.0D, 3.0D);
/*  83 */     boxapp.setHeight(2.0D);
/*  84 */     boxapp.setPatternType(CornerPatternApplicator.CornerPattern.BOX);
/*  85 */     Coordinate[] box = boxapp.calculateFillArea();
/*     */ 
/*  88 */     Coordinate[] result = { new Coordinate(-2.0D, 1.0D), new Coordinate(2.0D, 1.0D), 
/*  89 */       new Coordinate(2.0D, 3.0D), new Coordinate(-2.0D, 3.0D), 
/*  90 */       new Coordinate(-2.0D, 1.0D) };
/*     */ 
/*  93 */     for (int i = 0; i < result.length; i++) {
/*  94 */       String message = "TEST1 for point " + i + " x-coord failed:";
/*  95 */       Assert.assertEquals(message, result[i].x, box[i].x, 1.E-09D);
/*  96 */       message = "TEST1 for point " + i + " y-coord failed:";
/*  97 */       Assert.assertEquals(message, result[i].y, box[i].y, 1.E-09D);
/*     */     }
/*     */ 
/* 101 */     boxapp.setHeight(1.0D);
/* 102 */     boxapp.setStartLoc(4.0D + Math.sqrt(2.0D));
/* 103 */     boxapp.setEndLoc(4.0D + 3.0D * Math.sqrt(2.0D));
/* 104 */     boxapp.setPatternType(CornerPatternApplicator.CornerPattern.BOX);
/* 105 */     box = boxapp.calculateFillArea();
/*     */ 
/* 108 */     result = new Coordinate[] { new Coordinate(1.0D, 6.0D), new Coordinate(1.0D, 4.0D), 
/* 109 */       new Coordinate(3.0D, 4.0D), new Coordinate(3.0D, 6.0D), 
/* 110 */       new Coordinate(1.0D, 6.0D) };
/*     */ 
/* 113 */     for (int i = 0; i < result.length; i++) {
/* 114 */       String message = "TEST2 for point " + i + " x-coord failed:";
/* 115 */       Assert.assertEquals(message, result[i].x, box[i].x, 1.E-09D);
/* 116 */       message = "TEST2 for point " + i + " y-coord failed:";
/* 117 */       Assert.assertEquals(message, result[i].y, box[i].y, 1.E-09D);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testCalculateLines()
/*     */   {
/* 129 */     GeometryFactory gf = new GeometryFactory();
/* 130 */     Coordinate[] coords = { new Coordinate(0.0D, 0.0D), 
/* 131 */       new Coordinate(0.0D, 4.0D), 
/* 132 */       new Coordinate(2.0D, 6.0D), 
/* 133 */       new Coordinate(4.0D, 4.0D), 
/* 134 */       new Coordinate(4.0D, 0.0D), 
/* 135 */       new Coordinate(0.0D, 0.0D) };
/* 136 */     LineString path = gf.createLineString(coords);
/* 137 */     LengthIndexedLine line = new LengthIndexedLine(path);
/*     */ 
/* 141 */     CornerPatternApplicator boxapp = new CornerPatternApplicator(line, 4.0D + 2.0D * Math.sqrt(2.0D), -8.0D);
/* 142 */     boxapp.setHeight(Math.sqrt(2.0D));
/* 143 */     boxapp.setPatternType(CornerPatternApplicator.CornerPattern.DOUBLE_LINE);
/* 144 */     double[][] pts = boxapp.calculateLines();
/*     */ 
/* 147 */     double[][] exp = { { 3.0D, 7.0D, 0.0D }, { 5.0D, 5.0D, 0.0D }, 
/* 148 */       { 1.0D, 5.0D, 0.0D }, { 3.0D, 3.0D, 0.0D } };
/*     */ 
/* 151 */     Assert.assertEquals("Different number of points", exp.length, pts.length);
/* 152 */     for (int i = 0; i < exp.length; i++) {
/* 153 */       for (int j = 0; j < 3; j++) {
/* 154 */         String message = "TEST3 - Comparing " + i + "th point - position " + j + ":";
/* 155 */         Assert.assertEquals(message, exp[i][j], pts[i][j], 1.E-09D);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 160 */     boxapp.setPatternType(CornerPatternApplicator.CornerPattern.Z_PATTERN);
/* 161 */     pts = boxapp.calculateLines();
/*     */ 
/* 164 */     exp = new double[][] { { 3.0D, 7.0D, 0.0D }, { 5.0D, 5.0D, 0.0D }, 
/* 165 */       { 1.0D, 5.0D, 0.0D }, { 3.0D, 3.0D, 0.0D } };
/*     */ 
/* 168 */     Assert.assertEquals("Different number of points", exp.length, pts.length);
/* 169 */     for (int i = 0; i < exp.length; i++) {
/* 170 */       for (int j = 0; j < 3; j++) {
/* 171 */         String message = "TEST4 - Comparing " + i + "th point - position " + j + ":";
/* 172 */         Assert.assertEquals(message, exp[i][j], pts[i][j], 1.E-09D);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 177 */     boxapp.setHeight(5.0D);
/* 178 */     boxapp.setStartLoc(0.0D);
/* 179 */     boxapp.setEndLoc(-7.0D);
/* 180 */     boxapp.setPatternType(CornerPatternApplicator.CornerPattern.X_PATTERN);
/* 181 */     pts = boxapp.calculateLines();
/*     */ 
/* 184 */     exp = new double[][] { { -3.0D, 4.0D, 0.0D }, { 7.0D, -1.0D, 0.0D }, 
/* 185 */       { 3.0D, -4.0D, 0.0D }, { 1.0D, 7.0D, 0.0D } };
/*     */ 
/* 188 */     Assert.assertEquals("Different number of points", exp.length, pts.length);
/* 189 */     for (int i = 0; i < exp.length; i++) {
/* 190 */       for (int j = 0; j < 3; j++) {
/* 191 */         String message = "TEST5 - Comparing " + i + "th point - position " + j + ":";
/* 192 */         Assert.assertEquals(message, exp[i][j], pts[i][j], 1.E-09D);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 197 */     boxapp.setHeight(1.5D);
/* 198 */     boxapp.setStartLoc(4.0D);
/* 199 */     boxapp.setEndLoc(-8.0D);
/* 200 */     boxapp.setPatternType(CornerPatternApplicator.CornerPattern.TICK);
/* 201 */     pts = boxapp.calculateLines();
/*     */ 
/* 204 */     exp = new double[][] { { 4.0D, 4.0D, 0.0D }, { 4.0D, 2.5D, 0.0D } };
/*     */ 
/* 207 */     Assert.assertEquals("Different number of points", exp.length, pts.length);
/* 208 */     for (int i = 0; i < exp.length; i++)
/* 209 */       for (int j = 0; j < 3; j++) {
/* 210 */         String message = "TEST5 - Comparing " + i + "th point - position " + j + ":";
/* 211 */         Assert.assertEquals(message, exp[i][j], pts[i][j], 1.E-09D);
/*     */       }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.CornerPatternApplicatorTest
 * JD-Core Version:    0.6.2
 */