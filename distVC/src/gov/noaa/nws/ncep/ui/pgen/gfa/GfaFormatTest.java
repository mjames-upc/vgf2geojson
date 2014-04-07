/*     */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.SerializationException;
/*     */ import com.raytheon.uf.viz.core.localization.LocalizationManager;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.Node;
/*     */ import org.dom4j.io.SAXReader;
/*     */ import org.junit.After;
/*     */ import org.junit.Assert;
/*     */ import org.junit.Before;
/*     */ import org.junit.Ignore;
/*     */ import org.junit.Test;
/*     */ 
/*     */ public class GfaFormatTest
/*     */ {
/*     */   public static final String BASE_DIR = "../../eclipse/";
/*     */   public static final int CYCLE_DAY = 20;
/*     */   public static final int CYCLE_HOUR = 14;
/*     */   public static final String HTTP_SERVER = "http://localhost:9581/services";
/*  39 */   public static final String PACKAGE = "unit-test/" + 
/*  39 */     GfaFormatTest.class.getPackage().getName().replace(".", "/") + "/xml/";
/*  40 */   public static String rulesXml = "rules.xml";
/*     */   private static Document doc;
/* 182 */   static int count = 0;
/*     */ 
/*     */   @Before
/*     */   public void setUp()
/*     */     throws Exception
/*     */   {
/*  47 */     configure();
/*  48 */     getDocument();
/*     */   }
/*     */ 
/*     */   @After
/*     */   public void tearDown() throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   @Ignore
/*     */   public void getDocumentTest() {
/*  58 */     Assert.assertNotNull(doc);
/*     */   }
/*     */ 
/*     */   private void createSmearsTest(String fileName, GfaWording expectedWording, int adj) throws FileNotFoundException, SerializationException {
/*  62 */     List drawables = read(PACKAGE + fileName);
/*     */ 
/*  64 */     ArrayList gfaList = new ArrayList();
/*  65 */     for (AbstractDrawableComponent adc : drawables) {
/*  66 */       if ((adc instanceof Gfa))
/*  67 */         gfaList.add((Gfa)adc);
/*     */     }
/*  69 */     Assert.assertFalse(gfaList.isEmpty());
/*     */ 
/*  71 */     int sizeBefore = gfaList.size();
/*     */ 
/*  73 */     GfaFormat gf = new GfaFormat();
/*     */ 
/*  76 */     gf.createSmears(drawables);
/*     */ 
/*  78 */     int sizeAfter = drawables.size();
/*  79 */     Assert.assertTrue(sizeAfter > sizeBefore);
/*     */ 
/*  81 */     Gfa gfa = (Gfa)drawables.get(sizeBefore + adj);
/*  82 */     GfaWording e = expectedWording;
/*  83 */     GfaWording r = (GfaWording)gfa.getAttribute("WORDING", GfaWording.class);
/*  84 */     Assert.assertEquals("failed fromCondsDvlpg in " + fileName, e.fromCondsDvlpg, r.fromCondsDvlpg);
/*  85 */     Assert.assertEquals("failed fromCondsEndg in " + fileName, e.fromCondsEndg, r.fromCondsEndg);
/*  86 */     Assert.assertEquals("failed genOlk in " + fileName, e.genOlk, r.genOlk);
/*  87 */     Assert.assertEquals("failed condsContg in " + fileName, e.condsContg, r.condsContg);
/*  88 */     Assert.assertEquals("failed otlkCondsDvlpg in " + fileName, e.otlkCondsDvlpg, r.otlkCondsDvlpg);
/*  89 */     Assert.assertEquals("failed otlkCondsEndg in " + fileName, e.otlkCondsEndg, r.otlkCondsEndg);
/*  90 */     Assert.assertEquals("failed in " + fileName, e, r);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void runRules() {
/*  95 */     String xPath = "/rules/rule";
/*  96 */     List nodes = selectNodes(xPath);
/*  97 */     StringBuilder sb = new StringBuilder(500);
/*  98 */     int errors = 0;
/*     */     int j;
/*     */     int i;
/*  99 */     for (Iterator localIterator = nodes.iterator(); localIterator.hasNext(); 
/* 102 */       i < j)
/*     */     {
/*  99 */       Node n = (Node)localIterator.next();
/* 100 */       String[] fileNames = n.valueOf("@filenames").split(",");
/*     */       String[] arrayOfString1;
/* 102 */       j = (arrayOfString1 = fileNames).length; i = 0; continue; String fileName = arrayOfString1[i];
/*     */ 
/* 105 */       String fromCondsDvlpg = n.valueOf("@fromCondsDvlpg");
/* 106 */       String fromCondsEndg = n.valueOf("@fromCondsEndg");
/* 107 */       String genOlk = n.valueOf("@genOlk");
/* 108 */       String condsContg = n.valueOf("@condsContg");
/* 109 */       String otlkCondsDvlpg = n.valueOf("@otlkCondsDvlpg");
/* 110 */       String otlkCondsEndg = n.valueOf("@otlkCondsEndg");
/* 111 */       String orderInQueue = n.valueOf("@orderInQueue");
/* 112 */       int plus = 0;
/* 113 */       if (!orderInQueue.isEmpty()) {
/* 114 */         plus = Integer.parseInt(orderInQueue);
/*     */       }
/*     */ 
/* 117 */       GfaWording expected = new GfaWording();
/* 118 */       expected.fromCondsDvlpg = fromCondsDvlpg;
/* 119 */       expected.fromCondsEndg = fromCondsEndg;
/* 120 */       expected.genOlk = genOlk;
/* 121 */       expected.condsContg = condsContg;
/* 122 */       expected.otlkCondsDvlpg = otlkCondsDvlpg;
/* 123 */       expected.otlkCondsEndg = otlkCondsEndg;
/* 124 */       sb.append("filename=\"" + fileName + "\"\nExpected:\n");
/* 125 */       sb.append(expected.toString());
/*     */ 
/* 127 */       sb.setLength(0);
/*     */       try {
/* 129 */         createSmearsTest(fileName, expected, plus);
/*     */       }
/*     */       catch (Exception e) {
/* 132 */         e.printStackTrace();
/* 133 */         errors++;
/*     */       }
/* 102 */       i++;
/*     */     }
/*     */ 
/* 138 */     Assert.assertEquals("Number of failed files ", 0L, errors);
/*     */   }
/*     */ 
/*     */   private List<AbstractDrawableComponent> read(String file)
/*     */     throws FileNotFoundException, SerializationException
/*     */   {
/* 144 */     Products products = Util.read(file);
/* 145 */     List productList = ProductConverter.convert(products);
/* 146 */     Assert.assertEquals(1L, productList.size());
/* 147 */     List layerList = ((Product)productList.get(0)).getLayers();
/* 148 */     Assert.assertEquals(1L, layerList.size());
/* 149 */     List drawables = ((Layer)layerList.get(0)).getDrawables();
/* 150 */     return drawables;
/*     */   }
/*     */ 
/*     */   public static void configure()
/*     */     throws NoSuchFieldException, IllegalAccessException
/*     */   {
/* 161 */     long time = System.currentTimeMillis();
/*     */ 
/* 163 */     LocalizationManager.setBaseDir("../../eclipse/");
/*     */ 
/* 165 */     Field field = PgenCycleTool.class.getDeclaredField("cycleDay");
/* 166 */     field.setAccessible(true);
/* 167 */     field.set(null, Integer.valueOf(20));
/* 168 */     field = PgenCycleTool.class.getDeclaredField("cycleHour");
/* 169 */     field.setAccessible(true);
/* 170 */     field.set(null, Integer.valueOf(14));
/*     */ 
/* 176 */     if (!PreloadGfaDataThread.loaded)
/*     */     {
/* 178 */       new PreloadGfaDataThread().run();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void getDocument()
/*     */   {
/* 184 */     if (doc != null) return; try
/*     */     {
/* 186 */       SAXReader reader = new SAXReader();
/* 187 */       doc = reader.read(PACKAGE + rulesXml);
/*     */     }
/*     */     catch (Exception e) {
/* 190 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private List<Node> selectNodes(String xPath)
/*     */   {
/* 196 */     return doc.selectNodes(xPath);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaFormatTest
 * JD-Core Version:    0.6.2
 */