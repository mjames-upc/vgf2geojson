/*     */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*     */ 
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*     */ import java.io.File;
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.Node;
/*     */ import org.dom4j.io.SAXReader;
/*     */ 
/*     */ public class AirmetCycleInfo
/*     */ {
/*     */   private static Document airmetCycleDoc;
/*     */   public static final String AIRMET_ELEMENT_XPATH = "/airmetcycle/element";
/*     */ 
/*     */   public static Document getDocument()
/*     */   {
/*  47 */     if (airmetCycleDoc == null) {
/*  48 */       readAirmetCycle();
/*     */     }
/*  50 */     return airmetCycleDoc;
/*     */   }
/*     */ 
/*     */   private static void readAirmetCycle()
/*     */   {
/*     */     try
/*     */     {
/*  59 */       File airmetCycleFile = PgenStaticDataProvider.getProvider().getStaticFile(
/*  60 */         PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "airmetcycle.xml");
/*     */ 
/*  62 */       SAXReader reader = new SAXReader();
/*  63 */       airmetCycleDoc = reader.read(airmetCycleFile.getAbsoluteFile());
/*     */     } catch (Exception e) {
/*  65 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static List<Node> selectNodes(String xPath)
/*     */   {
/*  74 */     return getDocument().selectNodes(xPath);
/*     */   }
/*     */ 
/*     */   public static String getIssueTime() {
/*  78 */     int cycleH = PgenCycleTool.getCycleHour();
/*  79 */     int cycleD = PgenCycleTool.getCycleDay();
/*     */ 
/*  81 */     String xPath = "/airmetcycle/element[@cycle='" + PgenCycleTool.pad(cycleH) + "']";
/*     */ 
/*  83 */     List elements = selectNodes(xPath);
/*     */     String ret;
/*     */     String ret;
/*  87 */     if (elements.isEmpty()) {
/*  88 */       xPath = "/airmetcycle/element[@timezone='DEFAULT']";
/*  89 */       elements = selectNodes(xPath);
/*  90 */       String d = ((Node)elements.get(0)).valueOf("@delayMin");
/*  91 */       int delayMin = Integer.parseInt(d);
/*  92 */       Calendar cal = Calendar.getInstance();
/*  93 */       cal.set(cal.get(1), cal.get(2), cycleD, cycleH, 0, 0);
/*     */ 
/*  95 */       cal.add(12, -delayMin);
/*     */ 
/*  97 */       ret = PgenCycleTool.pad(cal.get(11)) + 
/*  98 */         PgenCycleTool.pad(cal.get(12));
/*     */     } else {
/* 100 */       ret = ((Node)elements.get(0)).valueOf("@issue");
/*     */     }
/*     */ 
/* 103 */     return ret;
/*     */   }
/*     */ 
/*     */   public static Calendar getUntilTime() {
/* 107 */     int cycleH = PgenCycleTool.getCycleHour();
/* 108 */     int cycleD = PgenCycleTool.getCycleDay();
/*     */ 
/* 110 */     Calendar cal = Calendar.getInstance();
/* 111 */     cal.set(5, cycleD);
/* 112 */     cal.set(11, cycleH);
/* 113 */     cal.set(12, 0);
/* 114 */     cal.set(13, 0);
/* 115 */     cal.add(11, 6);
/*     */ 
/* 117 */     return cal;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.AirmetCycleInfo
 * JD-Core Version:    0.6.2
 */