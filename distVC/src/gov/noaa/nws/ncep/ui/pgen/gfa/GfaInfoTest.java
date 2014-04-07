/*    */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*    */ import org.junit.After;
/*    */ import org.junit.Assert;
/*    */ import org.junit.Before;
/*    */ import org.junit.Test;
/*    */ 
/*    */ public class GfaInfoTest
/*    */ {
/*    */   @Before
/*    */   public void setUp()
/*    */     throws Exception
/*    */   {
/* 15 */     GfaFormatTest.configure();
/*    */   }
/*    */ 
/*    */   @After
/*    */   public void tearDown() throws Exception {
/*    */   }
/*    */ 
/*    */   @Test
/*    */   public void testCycle() {
/* 24 */     Assert.assertEquals(20L, PgenCycleTool.getCycleDay());
/* 25 */     Assert.assertEquals(14L, PgenCycleTool.getCycleHour());
/*    */   }
/*    */ 
/*    */   @Test
/*    */   public void getDocument()
/*    */   {
/* 31 */     Assert.assertNotNull(GfaInfo.getDocument());
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaInfoTest
 * JD-Core Version:    0.6.2
 */