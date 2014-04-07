/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.junit.After;
/*     */ import org.junit.AfterClass;
/*     */ import org.junit.Assert;
/*     */ import org.junit.Before;
/*     */ import org.junit.BeforeClass;
/*     */ import org.junit.Test;
/*     */ 
/*     */ public class LayerTest
/*     */ {
/*     */   private Layer tlayer;
/*     */   private List<AbstractDrawableComponent> tlist;
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
/*  62 */     this.tlist = new ArrayList();
/*  63 */     this.tlayer = new Layer("Test", true, true, Color.red, true, this.tlist);
/*     */   }
/*     */ 
/*     */   @After
/*     */   public void tearDown()
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testAddElementIntDrawable()
/*     */   {
/*  78 */     DrawableElement oneElement = new Line();
/*  79 */     this.tlayer.addElement(oneElement);
/*  80 */     Assert.assertTrue(this.tlayer.getElement(0) == oneElement);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testAddElementDrawable()
/*     */   {
/*  88 */     DrawableElement oneElement = new Symbol();
/*  89 */     this.tlayer.addElement(oneElement);
/*  90 */     Assert.assertTrue(this.tlayer.getElement(this.tlayer.getDrawables().size() - 1) == oneElement);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testRemoveElement()
/*     */   {
/*  98 */     DrawableElement oneElement = new Line();
/*  99 */     int tsize = this.tlayer.getDrawables().size();
/* 100 */     this.tlayer.addElement(oneElement);
/* 101 */     this.tlayer.removeElement(0);
/* 102 */     Assert.assertTrue(tsize == this.tlayer.getDrawables().size());
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testGetElement()
/*     */   {
/* 110 */     DrawableElement oneElement = new KinkLine();
/* 111 */     this.tlayer.addElement(oneElement);
/* 112 */     Assert.assertTrue(this.tlayer.getElement(0) == oneElement);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testToString()
/*     */   {
/* 120 */     this.tlist = new ArrayList();
/* 121 */     this.tlayer = new Layer("Test", true, true, Color.red, true, this.tlist);
/*     */ 
/* 123 */     System.out.println("\nContent in Layer Object\n");
/* 124 */     System.out.println(this.tlayer);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.LayerTest
 * JD-Core Version:    0.6.2
 */