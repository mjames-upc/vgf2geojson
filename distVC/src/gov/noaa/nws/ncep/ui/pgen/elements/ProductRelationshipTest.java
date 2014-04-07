/*    */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import org.junit.After;
/*    */ import org.junit.AfterClass;
/*    */ import org.junit.Before;
/*    */ import org.junit.BeforeClass;
/*    */ import org.junit.Test;
/*    */ 
/*    */ public class ProductRelationshipTest
/*    */ {
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
/*    */   }
/*    */ 
/*    */   @After
/*    */   public void tearDown()
/*    */     throws Exception
/*    */   {
/*    */   }
/*    */ 
/*    */   @Test
/*    */   public final void testToString()
/*    */   {
/* 62 */     System.out.println("\nContent in ProductRelationship Class\n");
/* 63 */     for (ProductRelationship pr : ProductRelationship.values())
/* 64 */       System.out.println(pr + ":\t" + pr.ordinal());
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.ProductRelationshipTest
 * JD-Core Version:    0.6.2
 */