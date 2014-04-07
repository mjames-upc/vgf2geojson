/*    */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ import org.junit.After;
/*    */ import org.junit.AfterClass;
/*    */ import org.junit.Assert;
/*    */ import org.junit.Before;
/*    */ import org.junit.BeforeClass;
/*    */ import org.junit.Test;
/*    */ 
/*    */ public class ProductTimeTest
/*    */ {
/*    */   private Calendar tcalendar;
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
/* 54 */     this.tcalendar = Calendar.getInstance();
/*    */   }
/*    */ 
/*    */   @After
/*    */   public void tearDown()
/*    */     throws Exception
/*    */   {
/*    */   }
/*    */ 
/*    */   @Test
/*    */   public final void testCompare()
/*    */   {
/* 69 */     ProductTime ninfo = new ProductTime(this.tcalendar);
/* 70 */     ProductTime ninfo1 = new ProductTime(this.tcalendar);
/* 71 */     Assert.assertTrue(ninfo.compare(ninfo1));
/*    */   }
/*    */ 
/*    */   @Test
/*    */   public final void testGetRange()
/*    */   {
/* 79 */     Calendar ncalendar = Calendar.getInstance();
/* 80 */     ProductTime ninfo = new ProductTime(this.tcalendar);
/* 81 */     ninfo.setEndTime(ncalendar);
/* 82 */     Assert.assertTrue(ninfo.getRange() == 
/* 83 */       ncalendar.getTimeInMillis() - this.tcalendar.getTimeInMillis());
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.ProductTimeTest
 * JD-Core Version:    0.6.2
 */