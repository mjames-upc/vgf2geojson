/*     */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*     */ 
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import java.awt.Color;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.Node;
/*     */ import org.dom4j.io.SAXReader;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ 
/*     */ public class GfaInfo
/*     */ {
/*     */   private static Document doc;
/*     */   public static final String HAZARD_XPATH = "/root/hazard";
/*     */   public static final String FCSTHR_XPATH = "/root/fcstHr";
/*     */   public static final String TAG_XPATH = "/root/tag";
/*     */   public static final String DESK_XPATH = "/root/desk";
/*     */   public static final String ISSUE_TYPE_XPATH = "/root/issueType";
/*     */   public static final String GFA_OTLKGEN_RATIO_XPATH = "/root/gfaOtlkgenRatio";
/*     */   public static final String AIRMET_ELEMENT_XPATH = "/airmetcycle/element";
/*     */   public static final String GFA_SNAPSHOT = "snapshot";
/*     */   public static final String GFA_SMEAR = "smear";
/*     */   public static final String GFA_OUTLOOK = "outlook";
/*     */   public static final int GFA_SMEAR_LINEWIDTH = 3;
/*     */   public static final int GFA_OUTLOOK_LINEWIDTH = 4;
/*     */   public static final int GFA_OTHER_LINEWIDTH = 2;
/*     */   private static HashMap<String, RGB[]> rgbMap;
/*     */   private static HashMap<String, RGB> definedColors;
/*     */   private static HashMap<String, HazardCategory> hazardCategories;
/*     */   private static HashMap<String, ArrayList<String>> stateOrderByArea;
/*     */   private static HashMap<String, RGB> fzlvlSfcColors;
/*     */ 
/*     */   public static Document getDocument()
/*     */   {
/* 101 */     if (doc == null) {
/* 102 */       readOptions();
/*     */     }
/* 104 */     return doc;
/*     */   }
/*     */ 
/*     */   private static void readOptions()
/*     */   {
/* 111 */     File gfainfoFile = PgenStaticDataProvider.getProvider().getStaticFile(
/* 112 */       PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "gfa.xml");
/*     */     try
/*     */     {
/* 115 */       SAXReader reader = new SAXReader();
/* 116 */       doc = reader.read(gfainfoFile.getAbsoluteFile());
/*     */     } catch (Exception e) {
/* 118 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static List<Node> selectNodes(String xPath)
/*     */   {
/* 127 */     return getDocument().selectNodes(xPath);
/*     */   }
/*     */ 
/*     */   public static RGB getRGB(String hazard, int fcstHrIndex)
/*     */   {
/* 137 */     if (rgbMap == null) {
/* 138 */       loadColors();
/*     */     }
/*     */ 
/* 141 */     if ((fcstHrIndex < 0) || (fcstHrIndex >= ((RGB[])rgbMap.get(hazard)).length))
/*     */     {
/* 143 */       fcstHrIndex = ((RGB[])rgbMap.get(hazard)).length - 1;
/*     */     }
/*     */ 
/* 146 */     return ((RGB[])rgbMap.get(hazard))[fcstHrIndex];
/*     */   }
/*     */ 
/*     */   private static void loadColors()
/*     */   {
/* 153 */     List colorNodes = selectNodes("/root/color/value");
/*     */ 
/* 155 */     definedColors = new HashMap();
/*     */     int g;
/* 156 */     for (Node n : colorNodes) {
/* 157 */       int r = Integer.parseInt(n.valueOf("@r"));
/* 158 */       g = Integer.parseInt(n.valueOf("@g"));
/* 159 */       int b = Integer.parseInt(n.valueOf("@b"));
/*     */ 
/* 161 */       definedColors.put(n.valueOf("@name"), new RGB(r, g, b));
/*     */     }
/*     */ 
/* 164 */     List hazardNodes = selectNodes("/root/hazard");
/* 165 */     List fcstHrNodes = selectNodes("/root/fcstHr");
/*     */ 
/* 169 */     rgbMap = new HashMap();
/*     */ 
/* 171 */     for (Node n : hazardNodes) {
/* 172 */       RGB[] colors = new RGB[fcstHrNodes.size()];
/* 173 */       int i = 0;
/* 174 */       for (Node f : fcstHrNodes) {
/* 175 */         String type = f.valueOf("@type");
/* 176 */         String colorStr = n.valueOf("@" + type);
/*     */ 
/* 178 */         colors[(i++)] = ((RGB)definedColors.get(colorStr));
/*     */       }
/* 180 */       rgbMap.put(n.valueOf("@name"), colors);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Color[] getDefaultColors(String hazard, String fcstHr)
/*     */   {
/* 192 */     RGB rgb = getDefaultRGB(hazard, fcstHr);
/*     */ 
/* 194 */     Color color = new Color(rgb.red, rgb.green, rgb.blue);
/* 195 */     return new Color[] { color, color };
/*     */   }
/*     */ 
/*     */   public static RGB getDefaultRGB(String hazard, String fcstHr)
/*     */   {
/* 206 */     if (definedColors == null) {
/* 207 */       loadColors();
/*     */     }
/*     */ 
/* 210 */     String xPath = "/root/hazard[@name='" + hazard + "']";
/* 211 */     List hazardNodes = selectNodes(xPath);
/*     */ 
/* 213 */     xPath = "/root/fcstHr[@name='" + fcstHr + "']";
/* 214 */     List fcsthrNodes = selectNodes(xPath);
/*     */ 
/* 216 */     if (fcsthrNodes.size() != 1) {
/*     */       try {
/* 218 */         if (fcstHr.indexOf("-") == -1) {
/* 219 */           xPath = "/root/fcstHr[@name='0 Z']";
/*     */         } else {
/* 221 */           String second = fcstHr.split("-")[1];
/* 222 */           String hour = second.split(":")[0];
/* 223 */           if (Integer.parseInt(hour) <= 6)
/* 224 */             xPath = "/root/fcstHr[@name='0-6']";
/*     */           else
/* 226 */             xPath = "/root/fcstHr[@name='6-9']";
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 230 */         xPath = "/root/fcstHr[@name='Other']";
/*     */       }
/* 232 */       fcsthrNodes = selectNodes(xPath);
/*     */     }
/*     */ 
/* 235 */     String gfaType = "snapshot";
/* 236 */     if (fcsthrNodes.size() != 1) {
/* 237 */       if (fcstHr.indexOf("-") >= 0) {
/* 238 */         String second = fcstHr.split("-")[1];
/* 239 */         String hour = second.split(":")[0];
/* 240 */         if (Integer.parseInt(hour) <= 6)
/* 241 */           gfaType = "smear";
/*     */         else {
/* 243 */           gfaType = "outlook";
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 248 */       gfaType = ((Node)fcsthrNodes.get(0)).valueOf("@type");
/*     */     }
/*     */ 
/* 251 */     if (hazardNodes.size() != 1) {
/* 252 */       throw new IllegalArgumentException("Please check hazard name");
/*     */     }
/*     */ 
/* 255 */     String colorStr = ((Node)hazardNodes.get(0)).valueOf("@" + gfaType);
/*     */ 
/* 257 */     RGB rgb = (RGB)definedColors.get(colorStr);
/*     */ 
/* 259 */     return rgb;
/*     */   }
/*     */ 
/*     */   public static int getLineWidth(String fcstHr)
/*     */   {
/* 269 */     String xPath = "/root/fcstHr[@name='" + fcstHr + "']";
/* 270 */     List fcsthrNodes = selectNodes(xPath);
/*     */ 
/* 272 */     int lineWidth = 2;
/* 273 */     if (fcsthrNodes.size() > 0) {
/* 274 */       lineWidth = Integer.parseInt(((Node)fcsthrNodes.get(0)).valueOf("@linewidth"));
/*     */     }
/* 277 */     else if (fcstHr.indexOf("-") >= 0) {
/* 278 */       String second = fcstHr.split("-")[1];
/* 279 */       String hour = second.split(":")[0];
/* 280 */       if (Integer.parseInt(hour) <= 6)
/* 281 */         lineWidth = 3;
/*     */       else {
/* 283 */         lineWidth = 4;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 289 */     return lineWidth;
/*     */   }
/*     */ 
/*     */   public static boolean isFormat(String hazard) {
/* 293 */     if (definedColors == null) {
/* 294 */       loadColors();
/*     */     }
/*     */ 
/* 297 */     String xPath = "/root/hazard[@name='" + hazard + "']";
/* 298 */     List hazardNodes = selectNodes(xPath);
/* 299 */     if (hazardNodes.size() != 1) {
/* 300 */       throw new IllegalArgumentException("Please check hazard name");
/*     */     }
/* 302 */     String format = ((Node)hazardNodes.get(0)).valueOf("@format");
/* 303 */     return !"false".equals(format);
/*     */   }
/*     */ 
/*     */   private static HashMap<String, HazardCategory> getHazardCategories() {
/* 307 */     if (hazardCategories == null) {
/* 308 */       hazardCategories = new HashMap();
/* 309 */       List nodes = selectNodes("/root/hazard");
/* 310 */       for (Node n : nodes) {
/* 311 */         String key = n.valueOf("@name");
/* 312 */         String category = n.valueOf("@category");
/* 313 */         HazardCategory cat = (HazardCategory)HazardCategory.valueOf(HazardCategory.class, category);
/* 314 */         if (cat == null) cat = HazardCategory.NONE;
/* 315 */         hazardCategories.put(key, cat);
/*     */       }
/*     */     }
/* 318 */     return hazardCategories;
/*     */   }
/*     */ 
/*     */   public static HazardCategory getHazardCategory(String hazard) {
/* 322 */     return (HazardCategory)getHazardCategories().get(hazard);
/*     */   }
/*     */ 
/*     */   public static double getGfaOtlkgenRatio() {
/* 326 */     List nodes = selectNodes("/root/gfaOtlkgenRatio");
/* 327 */     Node n = (Node)nodes.get(0);
/* 328 */     String rationStr = n.getStringValue();
/* 329 */     return Double.parseDouble(rationStr);
/*     */   }
/*     */ 
/*     */   public static HashMap<String, ArrayList<String>> getStateOrderByArea()
/*     */   {
/* 337 */     if (stateOrderByArea == null) {
/* 338 */       stateOrderByArea = new HashMap();
/* 339 */       ArrayList bos = new ArrayList();
/* 340 */       String[] bosStr = { "ME", "NH", "VT", "MA", "RI", "CT", "NY", 
/* 341 */         "LO", "PA", "NJ", "OH", "LE", "WV", "MD", 
/* 342 */         "DC", "DE", "VA", "CSTL WTRS" };
/* 343 */       for (String st : bosStr) {
/* 344 */         bos.add(st);
/*     */       }
/* 346 */       stateOrderByArea.put("BOS", bos);
/*     */ 
/* 348 */       ArrayList mia = new ArrayList();
/* 349 */       String[] miaStr = { "NC", "SC", "GA", "FL", "CSTL WTRS" };
/* 350 */       for (String st : miaStr) {
/* 351 */         mia.add(st);
/*     */       }
/* 353 */       stateOrderByArea.put("MIA", mia);
/*     */ 
/* 355 */       ArrayList chi = new ArrayList();
/* 356 */       String[] chiStr = { "ND", "SD", "NE", "KS", "MN", "IA", 
/* 357 */         "MO", "WI", "LM", "LS", "MI", "LH", 
/* 358 */         "IL", "IN", "KY" };
/* 359 */       for (String st : chiStr) {
/* 360 */         chi.add(st);
/*     */       }
/* 362 */       stateOrderByArea.put("CHI", chi);
/*     */ 
/* 364 */       ArrayList dfw = new ArrayList();
/* 365 */       String[] dfwStr = { "OK", "TX", "AR", "TN", "LA", "MS", 
/* 366 */         "AL", "CSTL WTRS" };
/* 367 */       for (String st : dfwStr) {
/* 368 */         dfw.add(st);
/*     */       }
/* 370 */       stateOrderByArea.put("DFW", dfw);
/*     */ 
/* 372 */       ArrayList slc = new ArrayList();
/* 373 */       String[] slcStr = { "ID", "MT", "WY", "NV", "UT", "CO", 
/* 374 */         "AZ", "NM" };
/* 375 */       for (String st : slcStr) {
/* 376 */         slc.add(st);
/*     */       }
/* 378 */       stateOrderByArea.put("SLC", slc);
/*     */ 
/* 380 */       ArrayList sfo = new ArrayList();
/* 381 */       String[] sfoStr = { "WA", "OR", "CA", "CSTL WTRS" };
/* 382 */       for (String st : sfoStr) {
/* 383 */         sfo.add(st);
/*     */       }
/* 385 */       stateOrderByArea.put("SFO", sfo);
/*     */     }
/*     */ 
/* 389 */     return stateOrderByArea;
/*     */   }
/*     */ 
/*     */   public static RGB getFzlvlSfcColor(String name)
/*     */   {
/* 398 */     if (definedColors == null) {
/* 399 */       loadColors();
/*     */     }
/*     */ 
/* 402 */     List colorNodes = selectNodes("/root/fzlvlSFC/value");
/*     */ 
/* 404 */     if (fzlvlSfcColors == null) {
/* 405 */       fzlvlSfcColors = new HashMap();
/* 406 */       for (Node nd : colorNodes) {
/* 407 */         fzlvlSfcColors.put(nd.valueOf("@name"), (RGB)definedColors.get(nd.valueOf("@nmapcolor")));
/*     */       }
/*     */     }
/*     */ 
/* 411 */     RGB clr = (RGB)fzlvlSfcColors.get(name);
/*     */ 
/* 413 */     if (clr == null) clr = (RGB)definedColors.get("sky");
/*     */ 
/* 415 */     return clr;
/*     */   }
/*     */ 
/*     */   public static enum HazardCategory
/*     */   {
/*  82 */     SIERRA, TANGO, ZULU, NONE;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaInfo
 * JD-Core Version:    0.6.2
 */