/*    */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.junit.After;
/*    */ import org.junit.AfterClass;
/*    */ import org.junit.Assert;
/*    */ import org.junit.Before;
/*    */ import org.junit.BeforeClass;
/*    */ import org.junit.Test;
/*    */ 
/*    */ public class ProductInfoTest
/*    */ {
/*    */   ProductInfo info;
/*    */   HashMap<String, String> tmap;
/*    */ 
/*    */   @BeforeClass
/*    */   public static void setUpBeforeClass()
/*    */     throws Exception
/*    */   {
/*    */   }
/*    */ 
/*    */   @AfterClass
/*    */   public static void tearDownAfterClass()
/*    */     throws Exception
/*    */   {
/*    */   }
/*    */ 
/*    */   @Before
/*    */   public void setUp()
/*    */     throws Exception
/*    */   {
/* 44 */     this.tmap = new HashMap();
/* 45 */     this.tmap.put("Center", "AWC");
/* 46 */     this.info = new ProductInfo(this.tmap);
/*    */   }
/*    */ 
/*    */   @After
/*    */   public void tearDown()
/*    */     throws Exception
/*    */   {
/*    */   }
/*    */ 
/*    */   @Test
/*    */   public void testTmap()
/*    */   {
/* 58 */     Assert.assertNotNull(this.tmap);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.ProductInfoTest
 * JD-Core Version:    0.6.2
 */