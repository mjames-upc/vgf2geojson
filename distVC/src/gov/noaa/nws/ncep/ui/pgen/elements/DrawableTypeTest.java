/*    */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import org.junit.After;
/*    */ import org.junit.AfterClass;
/*    */ import org.junit.Before;
/*    */ import org.junit.BeforeClass;
/*    */ import org.junit.Test;
/*    */ 
/*    */ public class DrawableTypeTest
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
/* 54 */     System.out.println("\nContent in DrawableType Class\n");
/* 55 */     for (DrawableType dt : DrawableType.values())
/* 56 */       System.out.println(dt + ":\t" + dt.ordinal());
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.DrawableTypeTest
 * JD-Core Version:    0.6.2
 */