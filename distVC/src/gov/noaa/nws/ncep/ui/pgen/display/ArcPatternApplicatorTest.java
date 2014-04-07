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
/*     */ public class ArcPatternApplicatorTest
/*     */ {
/*     */   private static LengthIndexedLine line;
/*  34 */   private final double epsilon = 1.E-09D;
/*     */ 
/*     */   @BeforeClass
/*     */   public static void setUpBeforeClass()
/*     */     throws Exception
/*     */   {
/*  43 */     GeometryFactory gf = new GeometryFactory();
/*  44 */     Coordinate[] coords = { new Coordinate(0.0D, 0.0D), 
/*  45 */       new Coordinate(0.0D, 4.0D), 
/*  46 */       new Coordinate(2.0D, 6.0D), 
/*  47 */       new Coordinate(4.0D, 4.0D), 
/*  48 */       new Coordinate(4.0D, 0.0D), 
/*  49 */       new Coordinate(0.0D, 0.0D) };
/*  50 */     LineString path = gf.createLineString(coords);
/*  51 */     line = new LengthIndexedLine(path);
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
/*  86 */     ArcPatternApplicator arcapp = new ArcPatternApplicator(line, 1.0D, 3.0D);
/*  87 */     arcapp.setArcAttributes(0.0D, 180.0D, 2);
/*  88 */     arcapp.addSegmentToFill(true);
/*  89 */     Coordinate[] fill = arcapp.calculateFillArea();
/*     */ 
/*  92 */     Coordinate[] result = { new Coordinate(0.0D, 1.0D), new Coordinate(0.0D, 3.0D), 
/*  93 */       new Coordinate(0.0D, 3.0D), new Coordinate(-1.0D, 2.0D), 
/*  94 */       new Coordinate(0.0D, 1.0D), new Coordinate(0.0D, 1.0D) };
/*     */ 
/*  97 */     Assert.assertEquals("TEST1: Different number of points", result.length, fill.length);
/*  98 */     for (int i = 0; i < result.length; i++) {
/*  99 */       String message = "TEST1 for point " + i + " x-coord failed:";
/* 100 */       Assert.assertEquals(message, result[i].x, fill[i].x, 1.E-09D);
/* 101 */       message = "TEST1 for point " + i + " y-coord failed:";
/* 102 */       Assert.assertEquals(message, result[i].y, fill[i].y, 1.E-09D);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testCalculateLines()
/*     */   {
/* 117 */     ArcPatternApplicator arcapp = new ArcPatternApplicator(line, 4.0D, 4.0D + 4.0D * Math.sqrt(2.0D));
/* 118 */     arcapp.setArcAttributes(0.0D, 360.0D, 4);
/* 119 */     double[][] pts = arcapp.calculateLines();
/*     */ 
/* 122 */     double[][] exp = { { 4.0D, 4.0D, 0.0D }, { 2.0D, 6.0D, 0.0D }, 
/* 123 */       { 0.0D, 4.0D, 0.0D }, { 2.0D, 2.0D, 0.0D }, 
/* 124 */       { 4.0D, 4.0D, 0.0D } };
/*     */ 
/* 127 */     Assert.assertEquals("TEST1: Different number of points", exp.length, pts.length);
/* 128 */     for (int i = 0; i < exp.length; i++) {
/* 129 */       for (int j = 0; j < 3; j++) {
/* 130 */         String message = "TEST1 - Comparing " + i + "th point - position " + j + ":";
/* 131 */         Assert.assertEquals(message, exp[i][j], pts[i][j], 1.E-09D);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 138 */     arcapp.setStartLoc(-8.0D);
/* 139 */     arcapp.setEndLoc(-4.0D);
/* 140 */     arcapp.setArcAttributes(0.0D, -180.0D, 2);
/* 141 */     pts = arcapp.calculateLines();
/*     */ 
/* 144 */     exp = new double[][] { { 4.0D, 0.0D, 0.0D }, { 2.0D, 2.0D, 0.0D }, 
/* 145 */       { 4.0D, 4.0D, 0.0D } };
/*     */ 
/* 148 */     Assert.assertEquals("TEST2: Different number of points", exp.length, pts.length);
/* 149 */     for (int i = 0; i < exp.length; i++)
/* 150 */       for (int j = 0; j < 3; j++) {
/* 151 */         String message = "TEST2 - Comparing " + i + "th point - position " + j + ":";
/* 152 */         Assert.assertEquals(message, exp[i][j], pts[i][j], 1.E-09D);
/*     */       }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.ArcPatternApplicatorTest
 * JD-Core Version:    0.6.2
 */