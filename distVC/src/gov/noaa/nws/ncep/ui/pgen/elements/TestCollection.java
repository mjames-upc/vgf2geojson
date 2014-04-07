/*    */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*    */ 
/*    */ import com.vividsolutions.jts.geom.Coordinate;
/*    */ import java.awt.Color;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import org.junit.Assert;
/*    */ import org.junit.Before;
/*    */ import org.junit.Test;
/*    */ 
/*    */ public class TestCollection
/*    */ {
/* 40 */   DECollection dec = new DECollection("test collection");
/* 41 */   DECollection subDec = new DECollection("sub collection");
/*    */ 
/* 43 */   DrawableElement subDe1 = new Symbol(null, new Color[] { new Color(255, 255, 255) }, 
/* 44 */     1.0F, 1.0D, Boolean.valueOf(true), new Coordinate(10.0D, 10.0D), "symbol", "vol");
/*    */ 
/* 46 */   DrawableElement subDe2 = new Line(null, new Color[] { new Color(255, 255, 255) }, 
/* 47 */     1.0F, 1.0D, false, false, 
/* 48 */     new ArrayList(), 2, null, 
/* 49 */     "line", "solid");
/*    */ 
/* 50 */   DrawableElement de1 = new Symbol(null, new Color[] { new Color(255, 255, 255) }, 
/* 51 */     2.0F, 2.0D, Boolean.valueOf(true), new Coordinate(70.0D, 70.0D), "symbol", "rain");
/*    */ 
/* 52 */   DrawableElement de2 = new Line(null, new Color[] { new Color(255, 255, 255) }, 
/* 53 */     1.0F, 1.0D, false, false, 
/* 54 */     new ArrayList(), 2, null, 
/* 55 */     "line", "solid");
/*    */ 
/*    */   @Before
/*    */   public void setUp()
/*    */     throws Exception
/*    */   {
/* 59 */     this.subDec.addElement(this.subDe1);
/* 60 */     this.subDec.addElement(this.subDe2);
/*    */ 
/* 62 */     this.dec.addElement(this.de1);
/* 63 */     this.dec.addElement(this.subDec);
/* 64 */     this.dec.addElement(this.de2);
/*    */   }
/*    */ 
/*    */   @Test
/*    */   public void testCollectionIterator() throws Exception
/*    */   {
/* 70 */     Iterator it = this.dec.createDEIterator();
/*    */ 
/* 72 */     int total = 0;
/*    */ 
/* 74 */     while (it.hasNext()) {
/* 75 */       System.out.print(((DrawableElement)it.next()).toString());
/* 76 */       total++;
/*    */     }
/*    */ 
/* 79 */     Assert.assertEquals(4L, total);
/*    */   }
/*    */ 
/*    */   @Test
/*    */   public void testCollectionSearch()
/*    */     throws Exception
/*    */   {
/* 86 */     Assert.assertEquals(this.dec, this.dec.search(this.de1));
/* 87 */     Assert.assertEquals(this.subDec, this.dec.search(this.subDe1));
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.TestCollection
 * JD-Core Version:    0.6.2
 */