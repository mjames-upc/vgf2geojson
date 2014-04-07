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
/*     */ public class AbstractPatternApplicatorTest
/*     */ {
/*     */   private static LengthIndexedLine line;
/*  47 */   private final double epsilon = 1.E-09D;
/*     */ 
/*     */   @BeforeClass
/*     */   public static void setUpBeforeClass()
/*     */     throws Exception
/*     */   {
/*  56 */     GeometryFactory gf = new GeometryFactory();
/*  57 */     Coordinate[] coords = { new Coordinate(0.0D, 0.0D), 
/*  58 */       new Coordinate(0.0D, 4.0D), 
/*  59 */       new Coordinate(2.0D, 6.0D), 
/*  60 */       new Coordinate(4.0D, 4.0D), 
/*  61 */       new Coordinate(4.0D, 0.0D), 
/*  62 */       new Coordinate(0.0D, 0.0D) };
/*  63 */     LineString path = gf.createLineString(coords);
/*  64 */     line = new LengthIndexedLine(path);
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
/*     */   public void testGetStartPoint()
/*     */   {
/*  95 */     DefaultPatternApplicator dpa = new DefaultPatternApplicator(line);
/*     */ 
/*  97 */     Coordinate test = dpa.getStartPoint();
/*  98 */     Assert.assertTrue("Test 1 failed", test.equals2D(new Coordinate(0.0D, 0.0D)));
/*     */ 
/* 100 */     dpa.setStartLoc(4.0D);
/* 101 */     test = dpa.getStartPoint();
/* 102 */     Assert.assertTrue("Test 2 failed", test.equals2D(new Coordinate(0.0D, 4.0D)));
/*     */ 
/* 104 */     dpa.setStartLoc(4.0D * (1.0D + Math.sqrt(2.0D)));
/* 105 */     test = dpa.getStartPoint();
/* 106 */     Assert.assertTrue("Test 3 failed", test.equals2D(new Coordinate(4.0D, 4.0D)));
/*     */ 
/* 110 */     dpa.setStartLoc(-8.0D);
/* 111 */     test = dpa.getStartPoint();
/* 112 */     Assert.assertTrue("Test 4 failed", test.equals2D(new Coordinate(4.0D, 4.0D)));
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testGetEndPoint()
/*     */   {
/* 123 */     DefaultPatternApplicator dpa = new DefaultPatternApplicator(line);
/*     */ 
/* 125 */     Coordinate test = dpa.getEndPoint();
/* 126 */     Assert.assertTrue("Test 1 failed.", test.equals2D(new Coordinate(0.0D, 0.0D)));
/*     */ 
/* 128 */     dpa.setEndLoc(4.0D + 2.0D * Math.sqrt(2.0D));
/* 129 */     test = dpa.getEndPoint();
/* 130 */     Assert.assertTrue("Test 2 failed", test.equals2D(new Coordinate(2.0D, 6.0D)));
/*     */ 
/* 133 */     dpa.setEndLoc(0.0D);
/* 134 */     dpa.setStartLoc(4.0D);
/* 135 */     test = dpa.getEndPoint();
/* 136 */     Assert.assertTrue("Test 3 failed", test.equals2D(new Coordinate(0.0D, 0.0D)));
/*     */ 
/* 139 */     dpa.setStartLoc(100.0D);
/* 140 */     test = dpa.getEndPoint();
/* 141 */     Assert.assertTrue("Test 4 failed", test.equals2D(new Coordinate(0.0D, 0.0D)));
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testGetMidpoint()
/*     */   {
/* 152 */     DefaultPatternApplicator dpa = new DefaultPatternApplicator(line);
/*     */ 
/* 154 */     dpa.setStartLoc(4.0D);
/* 155 */     dpa.setEndLoc(4.0D * (1.0D + Math.sqrt(2.0D)));
/* 156 */     Coordinate midpoint = dpa.getMidpoint();
/* 157 */     Assert.assertTrue("Test 1 failed.", midpoint.equals2D(new Coordinate(2.0D, 4.0D)));
/*     */ 
/* 159 */     dpa.setEndLoc(4.0D * (2.0D + Math.sqrt(2.0D)));
/* 160 */     midpoint = dpa.getMidpoint();
/* 161 */     Assert.assertTrue("Test 2 failed.", midpoint.equals2D(new Coordinate(2.0D, 2.0D)));
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testGetDistance()
/*     */   {
/* 171 */     DefaultPatternApplicator dpa = new DefaultPatternApplicator(line);
/*     */ 
/* 173 */     double dist = dpa.getDistance();
/* 174 */     Assert.assertEquals("Test 1 failed.", 0.0D, dist, 1.E-09D);
/*     */ 
/* 176 */     dpa.setStartLoc(4.0D);
/* 177 */     dpa.setEndLoc(4.0D + 2.0D * Math.sqrt(2.0D));
/* 178 */     dist = dpa.getDistance();
/* 179 */     Assert.assertEquals("Test 1 failed.", 2.0D * Math.sqrt(2.0D), dist, 1.E-09D);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testGetSegmentAngle()
/*     */   {
/* 190 */     DefaultPatternApplicator dpa = new DefaultPatternApplicator(line);
/*     */ 
/* 192 */     double angle = dpa.getSegmentAngle();
/* 193 */     Assert.assertEquals("Test 1 failed.", 0.0D, angle, 1.E-09D);
/*     */ 
/* 195 */     dpa.setEndLoc(4.0D);
/* 196 */     angle = dpa.getSegmentAngle();
/* 197 */     Assert.assertEquals("Test 2 failed", 90.0D, angle, 1.E-09D);
/*     */ 
/* 199 */     dpa.setStartLoc(4.0D + 2.0D * Math.sqrt(2.0D));
/* 200 */     dpa.setEndLoc(4.0D + 4.0D * Math.sqrt(2.0D));
/* 201 */     angle = dpa.getSegmentAngle();
/* 202 */     Assert.assertEquals("Test 3 failed", -45.0D, angle, 1.E-09D);
/*     */ 
/* 204 */     dpa.setStartLoc(4.0D);
/* 205 */     angle = dpa.getSegmentAngle();
/* 206 */     Assert.assertEquals("Test 4 failed", 0.0D, angle, 1.E-09D);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testGetSegmentPath()
/*     */   {
/* 216 */     DefaultPatternApplicator dpa = new DefaultPatternApplicator(line);
/*     */ 
/* 218 */     Coordinate[] path = dpa.getSegmentPath();
/* 219 */     Coordinate[] coords = { new Coordinate(0.0D, 0.0D), 
/* 220 */       new Coordinate(0.0D, 4.0D), 
/* 221 */       new Coordinate(2.0D, 6.0D), 
/* 222 */       new Coordinate(4.0D, 4.0D), 
/* 223 */       new Coordinate(4.0D, 0.0D), 
/* 224 */       new Coordinate(0.0D, 0.0D) };
/* 225 */     Assert.assertArrayEquals("Test 1 failed.", coords, path);
/*     */ 
/* 227 */     dpa.setStartLoc(2.0D);
/* 228 */     dpa.setEndLoc(6.0D + 4.0D * Math.sqrt(2.0D));
/* 229 */     path = dpa.getSegmentPath();
/* 230 */     coords = new Coordinate[] { new Coordinate(0.0D, 2.0D), 
/* 231 */       new Coordinate(0.0D, 4.0D), 
/* 232 */       new Coordinate(2.0D, 6.0D), 
/* 233 */       new Coordinate(4.0D, 4.0D), 
/* 234 */       new Coordinate(4.0D, 2.0D) };
/* 235 */     Assert.assertArrayEquals("Test 2 failed.", coords, path);
/*     */ 
/* 238 */     dpa.setStartLoc(-6.0D);
/* 239 */     dpa.setEndLoc(4.0D + 2.0D * Math.sqrt(2.0D));
/* 240 */     path = dpa.getSegmentPath();
/* 241 */     coords = new Coordinate[] { new Coordinate(4.0D, 2.0D), 
/* 242 */       new Coordinate(4.0D, 4.0D), 
/* 243 */       new Coordinate(2.0D, 6.0D) };
/* 244 */     Assert.assertArrayEquals("Test 3 failed.", coords, path);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testGetSegmentPts()
/*     */   {
/* 255 */     DefaultPatternApplicator dpa = new DefaultPatternApplicator(line);
/*     */ 
/* 257 */     dpa.setEndLoc(4.0D);
/* 258 */     double[][] exp = { { 0.0D, 0.0D, 0.0D }, 
/* 259 */       { 0.0D, 4.0D, 0.0D } };
/* 260 */     double[][] pts = dpa.getSegmentPts();
/* 261 */     Assert.assertEquals("Different number of points", exp.length, pts.length);
/* 262 */     for (int i = 0; i < exp.length; i++) {
/* 263 */       for (int j = 0; j < 3; j++) {
/* 264 */         String message = "TEST1 - Comparing " + i + "th point - position " + j + ":";
/* 265 */         Assert.assertEquals(message, exp[i][j], pts[i][j], 1.E-09D);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 270 */     dpa.setStartLoc(4.0D);
/* 271 */     dpa.setEndLoc(4.0D);
/* 272 */     exp = new double[][] { { 0.0D, 4.0D, 0.0D }, 
/* 273 */       { 0.0D, 4.0D, 0.0D } };
/* 274 */     pts = dpa.getSegmentPts();
/* 275 */     Assert.assertEquals("Different number of points", exp.length, pts.length);
/* 276 */     for (int i = 0; i < exp.length; i++)
/* 277 */       for (int j = 0; j < 3; j++) {
/* 278 */         String message = "TEST2 - Comparing " + i + "th point - position " + j + ":";
/* 279 */         Assert.assertEquals(message, exp[i][j], pts[i][j], 1.E-09D);
/*     */       }
/*     */   }
/*     */ 
/*     */   class DefaultPatternApplicator extends AbstractPatternApplicator
/*     */   {
/*     */     DefaultPatternApplicator(LengthIndexedLine line)
/*     */     {
/*  39 */       super(); } 
/*  40 */     DefaultPatternApplicator(LengthIndexedLine line, double start, double end) { super(start, end); } 
/*  41 */     public Coordinate[] calculateFillArea() { return null; } 
/*  42 */     public double[][] calculateLines() { return null; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.AbstractPatternApplicatorTest
 * JD-Core Version:    0.6.2
 */