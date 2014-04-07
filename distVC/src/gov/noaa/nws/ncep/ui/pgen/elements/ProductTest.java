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
/*     */ public class ProductTest
/*     */ {
/*     */   private Product tProduct;
/*     */   private ArrayList<Layer> tLayers;
/*     */   private ProductInfo tInfo;
/*     */   private ProductTime tTime;
/*     */   private Layer firstLayer;
/*     */   private Layer secondLayer;
/*     */   private Layer thirdLayer;
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
/*  66 */     this.tInfo = new ProductInfo();
/*  67 */     this.tTime = new ProductTime();
/*  68 */     this.tLayers = new ArrayList();
/*  69 */     this.tProduct = new Product("SFC_ANL", "Default", "Forecaster", this.tInfo, this.tTime, this.tLayers);
/*     */ 
/*  71 */     this.tlist = new ArrayList();
/*  72 */     this.firstLayer = new Layer("First", true, true, Color.red, true, this.tlist);
/*  73 */     this.secondLayer = new Layer("Second", true, true, Color.green, true, this.tlist);
/*  74 */     this.thirdLayer = new Layer("Third", true, true, Color.blue, true, this.tlist);
/*     */   }
/*     */ 
/*     */   @After
/*     */   public void tearDown()
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testAddLayerLayer()
/*     */   {
/*  89 */     this.tProduct.addLayer(this.thirdLayer);
/*  90 */     Assert.assertTrue(this.tProduct.getLayer("Third").equals(this.thirdLayer));
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testAddLayerIntLayer()
/*     */   {
/*  98 */     int nlayers = this.tProduct.getLayers().size();
/*  99 */     this.tProduct.addLayer(nlayers, this.thirdLayer);
/* 100 */     Assert.assertTrue(this.tProduct.getLayer(nlayers).equals(this.thirdLayer));
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testRemoveLayer()
/*     */   {
/* 108 */     int nlayers = this.tProduct.getLayers().size();
/* 109 */     this.tProduct.addLayer(this.thirdLayer);
/* 110 */     this.tProduct.removeLayer(nlayers);
/* 111 */     Assert.assertTrue(this.tProduct.getLayers().size() == nlayers);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testMakeProduct()
/*     */   {
/* 119 */     System.out.println(this.tProduct.makeProduct());
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public final void testToString()
/*     */   {
/* 127 */     int nlayers = this.tProduct.getLayers().size();
/* 128 */     this.tProduct.addLayer(nlayers, this.firstLayer);
/* 129 */     this.tProduct.addLayer(nlayers + 1, this.secondLayer);
/* 130 */     System.out.println("\nContent in Product Object\n");
/* 131 */     System.out.println(this.tProduct);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.ProductTest
 * JD-Core Version:    0.6.2
 */