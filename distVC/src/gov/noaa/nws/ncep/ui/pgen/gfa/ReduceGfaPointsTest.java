/*     */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.junit.Assert;
/*     */ import org.junit.Test;
/*     */ import org.junit.runner.RunWith;
/*     */ import org.junit.runners.Suite;
/*     */ import org.junit.runners.Suite.SuiteClasses;
/*     */ 
/*     */ public class ReduceGfaPointsTest
/*     */ {
/*     */   @Test
/*     */   public final void testReduceBySize()
/*     */   {
/*  23 */     Coordinate[] coord = { new Coordinate(1.0D, 1.0D), new Coordinate(1.0D, -1.0D), new Coordinate(0.0D, -2.0D), new Coordinate(-1.0D, -1.0D), new Coordinate(-1.0D, 1.0D) };
/*  24 */     List cl = Arrays.asList(coord);
/*     */ 
/*  26 */     List reduceFlg = new ArrayList();
/*  27 */     for (int i = 0; i < cl.size(); i++)
/*  28 */       reduceFlg.add(Integer.valueOf(1));
/*  29 */     List orig = new ArrayList();
/*     */ 
/*  31 */     Coordinate[] coordAfter = { new Coordinate(1.666666666666667D, 1.0D), new Coordinate(0.125D, -3.625D), new Coordinate(-1.857142857142857D, 1.0D) };
/*  32 */     List clAfter = Arrays.asList(coordAfter);
/*     */ 
/*  34 */     int result = ReduceGfaPoints.reduceBySize(cl, null, orig, 3, 60.0D, 100.0D);
/*  35 */     Assert.assertEquals("Result", clAfter, cl);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testReduceBySize2()
/*     */   {
/*  41 */     Coordinate[] coord = { new Coordinate(1.0D, 1.0D), new Coordinate(1.0D, 0.0D), new Coordinate(1.0D, -1.0D), new Coordinate(0.0D, -1.0D), 
/*  42 */       new Coordinate(-1.0D, -1.0D), new Coordinate(-1.0D, 0.0D), new Coordinate(-1.0D, 1.0D), new Coordinate(0.0D, 1.0D) };
/*  43 */     List cl = Arrays.asList(coord);
/*     */ 
/*  45 */     List reduceFlg = new ArrayList();
/*  46 */     for (int i = 0; i < cl.size(); i++)
/*  47 */       reduceFlg.add(Integer.valueOf(1));
/*  48 */     List orig = new ArrayList();
/*     */ 
/*  50 */     Coordinate[] coordAfter = { new Coordinate(1.0D, -1.0D), new Coordinate(-1.0D, -1.0D), new Coordinate(-1.0D, 1.0D) };
/*  51 */     List clAfter = Arrays.asList(coordAfter);
/*     */ 
/*  53 */     int result = ReduceGfaPoints.reduceBySize(cl, null, orig, 3, 60.0D, 100.0D);
/*  54 */     Assert.assertEquals("Result", clAfter, cl);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testReduceByPctDist()
/*     */   {
/*  63 */     Coordinate[] coord = { new Coordinate(-110.56999999999999D, 54.719999999999999D), new Coordinate(-100.19D, 54.539999999999999D), new Coordinate(-89.370000000000005D, 48.920000000000002D), 
/*  64 */       new Coordinate(-96.650000000000006D, 41.840000000000003D), new Coordinate(-103.69D, 37.25D), new Coordinate(-113.93000000000001D, 38.340000000000003D), 
/*  65 */       new Coordinate(-117.95D, 42.399999999999999D), new Coordinate(-118.43000000000001D, 47.810000000000002D), new Coordinate(-115.17D, 52.630000000000003D) };
/*  66 */     List cl = Arrays.asList(coord);
/*     */ 
/*  68 */     List reduceFlg = new ArrayList();
/*  69 */     for (int i = 0; i < cl.size(); i++)
/*  70 */       reduceFlg.add(Integer.valueOf(1));
/*  71 */     List orig = new ArrayList();
/*     */ 
/*  73 */     Coordinate[] coordAfter = { new Coordinate(-110.56999999999999D, 54.719999999999999D), new Coordinate(-100.19D, 54.539999999999999D), new Coordinate(-89.370000000000005D, 48.920000000000002D), 
/*  74 */       new Coordinate(-103.69D, 37.25D), new Coordinate(-113.93000000000001D, 38.340000000000003D), new Coordinate(-118.43000000000001D, 47.810000000000002D) };
/*  75 */     List clAfter = Arrays.asList(coordAfter);
/*     */ 
/*  77 */     ReduceGfaPoints.reduceByPctDist(cl, null, orig, 60.0D, 80.0D, 450.0D, "BOUNDED BY");
/*  78 */     Assert.assertEquals("Result", clAfter, cl);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testReduceByPctDist2()
/*     */   {
/*  84 */     Coordinate[] coord = { new Coordinate(-90.0D, 40.0D), new Coordinate(-96.0D, 33.0D), 
/*  85 */       new Coordinate(-155.0D, 30.0D), new Coordinate(-177.0D, 33.0D), new Coordinate(-100.0D, 50.0D) };
/*  86 */     List cl = Arrays.asList(coord);
/*     */ 
/*  88 */     List reduceFlg = new ArrayList();
/*  89 */     for (int i = 0; i < cl.size(); i++)
/*  90 */       reduceFlg.add(Integer.valueOf(1));
/*  91 */     List orig = new ArrayList();
/*     */ 
/*  93 */     Coordinate[] coordAfter = { new Coordinate(-90.0D, 40.0D), new Coordinate(-96.0D, 33.0D), 
/*  94 */       new Coordinate(-155.0D, 30.0D), new Coordinate(-177.0D, 33.0D), new Coordinate(-100.0D, 50.0D) };
/*  95 */     List clAfter = Arrays.asList(coordAfter);
/*     */ 
/*  97 */     ReduceGfaPoints.reduceByPctDist(cl, null, orig, 60.0D, 80.0D, 450.0D, "BOUNDED BY");
/*  98 */     Assert.assertEquals("Result", clAfter, cl);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testReduceByPctDist3()
/*     */   {
/* 104 */     Coordinate[] coord = { new Coordinate(1.0D, 1.0D), new Coordinate(1.0D, -1.0D), new Coordinate(0.0D, -2.0D), new Coordinate(-1.0D, -1.0D), new Coordinate(-1.0D, 1.0D) };
/* 105 */     List cl = Arrays.asList(coord);
/*     */ 
/* 107 */     List reduceFlg = new ArrayList();
/* 108 */     for (int i = 0; i < cl.size(); i++)
/* 109 */       reduceFlg.add(Integer.valueOf(1));
/* 110 */     List orig = new ArrayList();
/*     */ 
/* 112 */     Coordinate[] coordAfter = { new Coordinate(1.666666666666667D, 1.0D), new Coordinate(0.125D, -3.625D), new Coordinate(-1.857142857142857D, 1.0D) };
/* 113 */     List clAfter = Arrays.asList(coordAfter);
/*     */ 
/* 115 */     ReduceGfaPoints.reduceByPctDist(cl, null, orig, 60.0D, 80.0D, 150.0D, "FROM");
/* 116 */     Assert.assertEquals("Result", clAfter, cl);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testReduceKeepConcav()
/*     */   {
/* 126 */     Coordinate[] coord = { new Coordinate(-100.0D, 50.0D), new Coordinate(-90.0D, 40.0D), new Coordinate(-96.0D, 33.0D), new Coordinate(-155.0D, 30.0D), new Coordinate(-177.0D, 33.0D) };
/* 127 */     List cl = Arrays.asList(coord);
/*     */ 
/* 129 */     List reduceFlg = new ArrayList();
/* 130 */     for (int i = 0; i < cl.size(); i++)
/* 131 */       reduceFlg.add(Integer.valueOf(1));
/* 132 */     List orig = new ArrayList();
/*     */ 
/* 134 */     Coordinate[] coordAfter = { new Coordinate(-92.700000000000003D, 51.600000000000001D), new Coordinate(-88.400000000000006D, 33.399999999999999D), new Coordinate(-155.0D, 30.0D), new Coordinate(-177.0D, 33.0D) };
/* 135 */     List clAfter = Arrays.asList(coordAfter);
/*     */ 
/* 137 */     ReduceGfaPoints.reduceKeepConcav(cl, null, orig, 60.0D, 450.0D, "FROM");
/* 138 */     Assert.assertEquals("Result", clAfter, cl);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testReduceKeepConcav2()
/*     */   {
/* 145 */     Coordinate[] coord = { new Coordinate(-110.56999999999999D, 54.719999999999999D), new Coordinate(-100.19D, 54.539999999999999D), new Coordinate(-89.370000000000005D, 48.920000000000002D), 
/* 146 */       new Coordinate(-96.650000000000006D, 41.840000000000003D), new Coordinate(-103.69D, 37.25D), new Coordinate(-113.93000000000001D, 38.340000000000003D), 
/* 147 */       new Coordinate(-117.95D, 42.399999999999999D), new Coordinate(-118.43000000000001D, 47.810000000000002D), new Coordinate(-115.17D, 52.630000000000003D) };
/* 148 */     List cl = Arrays.asList(coord);
/*     */ 
/* 150 */     List reduceFlg = new ArrayList();
/* 151 */     for (int i = 0; i < cl.size(); i++)
/* 152 */       reduceFlg.add(Integer.valueOf(1));
/* 153 */     List orig = new ArrayList();
/*     */ 
/* 155 */     Coordinate[] coordAfter = { new Coordinate(-112.75D, 54.759999999999998D), new Coordinate(-100.19D, 54.539999999999999D), new Coordinate(-89.370000000000005D, 48.920000000000002D), 
/* 156 */       new Coordinate(-96.650000000000006D, 41.840000000000003D), new Coordinate(-103.69D, 37.25D), new Coordinate(-113.93000000000001D, 38.340000000000003D), 
/* 157 */       new Coordinate(-117.0D, 33.0D), new Coordinate(-118.59D, 49.619999999999997D) };
/* 158 */     List clAfter = Arrays.asList(coordAfter);
/*     */ 
/* 160 */     ReduceGfaPoints.reduceKeepConcav(cl, null, orig, 60.0D, 450.0D, "FROM");
/* 161 */     Assert.assertEquals("Result", clAfter, cl);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testReduceKeepConcav3()
/*     */   {
/* 167 */     Coordinate[] coord = { new Coordinate(1.0D, 1.0D), new Coordinate(1.0D, -1.0D), new Coordinate(0.0D, -2.0D), new Coordinate(-1.0D, -1.0D), new Coordinate(-1.0D, 1.0D) };
/* 168 */     List cl = Arrays.asList(coord);
/*     */ 
/* 170 */     List reduceFlg = new ArrayList();
/* 171 */     for (int i = 0; i < cl.size(); i++)
/* 172 */       reduceFlg.add(Integer.valueOf(1));
/* 173 */     List orig = new ArrayList();
/*     */ 
/* 175 */     Coordinate[] coordAfter = { new Coordinate(1.666666666666667D, 1.0D), new Coordinate(0.125D, -3.625D), new Coordinate(-1.857142857142857D, 1.0D) };
/* 176 */     List clAfter = Arrays.asList(coordAfter);
/*     */ 
/* 178 */     ReduceGfaPoints.reduceKeepConcav(cl, null, orig, 60.0D, 150.0D, "FROM");
/* 179 */     Assert.assertEquals("Result", clAfter, cl);
/*     */   }
/*     */ 
/*     */   @RunWith(Suite.class)
/*     */   @Suite.SuiteClasses({ReduceGfaPointsTest.class})
/*     */   public class AllTests
/*     */   {
/*     */     public AllTests()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.ReduceGfaPointsTest
 * JD-Core Version:    0.6.2
 */