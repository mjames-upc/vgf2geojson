/*     */ package gov.noaa.nws.ncep.ui.pgen.graphtogrid;
/*     */ 
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.gempak.parameters.core.categorymap.CatMap;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Scanner;
/*     */ 
/*     */ public abstract class GraphToGrid
/*     */ {
/*     */   AbstractDrawableComponent currentGraph;
/*  45 */   protected LinkedHashMap<String, String> gridParameters = null;
/*     */ 
/*  47 */   private static ArrayList<String> parameterNames = null;
/*     */ 
/*  49 */   private static String[] gridCalcParams = { 
/*  50 */     "PROJ", "GRDAREA", 
/*  51 */     "KXKY", "GGLIMS", 
/*  52 */     "DISCRETE", "DLINES", 
/*  53 */     "EDGEOPTS", "BOUNDS", 
/*  54 */     "CATMAP" };
/*     */ 
/*  57 */   private static String[] gridDisplayParams = { 
/*  58 */     "CINT", "LINE", 
/*  59 */     "FINT", "FLINE" };
/*     */ 
/*  62 */   private static String[] gridOutputParams = { 
/*  63 */     "HISTGRD", "PATH", 
/*  64 */     "GDOUTF", "MAXGRD", 
/*  65 */     "GDATTIM", "GVCORD", 
/*  66 */     "GLEVEL", "GFUNC", 
/*  67 */     "GPARM" };
/*     */ 
/*     */   public abstract void makeGrid();
/*     */ 
/*     */   public GraphToGrid(AbstractDrawableComponent currentGraph, HashMap<String, String> gridParameters)
/*     */   {
/*  79 */     this.currentGraph = currentGraph;
/*     */ 
/*  81 */     setGridParameters(gridParameters);
/*     */   }
/*     */ 
/*     */   public static final ArrayList<String> getParameterNames()
/*     */   {
/*  89 */     initializeParameterNames();
/*  90 */     return parameterNames;
/*     */   }
/*     */ 
/*     */   private static final void initializeParameterNames()
/*     */   {
/*  98 */     if (parameterNames == null) {
/*  99 */       parameterNames = new ArrayList();
/* 100 */       for (String str : gridCalcParams) {
/* 101 */         parameterNames.add(str);
/*     */       }
/*     */ 
/* 104 */       for (String str : gridDisplayParams) {
/* 105 */         parameterNames.add(str);
/*     */       }
/*     */ 
/* 108 */       for (String str : gridOutputParams) {
/* 109 */         parameterNames.add(str);
/*     */       }
/*     */ 
/* 112 */       parameterNames.add("DISPOPT");
/*     */     }
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent getCurrentGraph()
/*     */   {
/* 121 */     return this.currentGraph;
/*     */   }
/*     */ 
/*     */   public void setCurrentGraph(AbstractDrawableComponent currentGraph)
/*     */   {
/* 128 */     this.currentGraph = currentGraph;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<String, String> getGridParameters()
/*     */   {
/* 136 */     if (this.gridParameters == null) {
/* 137 */       initializeGridParameters();
/*     */     }
/*     */ 
/* 140 */     return this.gridParameters;
/*     */   }
/*     */ 
/*     */   public void setGridParameters(HashMap<String, String> gridParameters)
/*     */   {
/* 148 */     initializeParameterNames();
/*     */ 
/* 150 */     if (this.gridParameters == null) {
/* 151 */       initializeGridParameters();
/*     */     }
/*     */ 
/* 154 */     if (gridParameters != null)
/* 155 */       for (String str : gridParameters.keySet())
/* 156 */         for (String pstr : parameterNames)
/* 157 */           if (pstr.equals(str)) {
/* 158 */             this.gridParameters.put(str, (String)gridParameters.get(str));
/* 159 */             break;
/*     */           }
/*     */   }
/*     */ 
/*     */   public static String[] getGridCalcParams()
/*     */   {
/* 171 */     return gridCalcParams;
/*     */   }
/*     */ 
/*     */   public static String[] getGridDisplayParams()
/*     */   {
/* 178 */     return gridDisplayParams;
/*     */   }
/*     */ 
/*     */   public static String[] getGridOutputParams()
/*     */   {
/* 185 */     return gridOutputParams;
/*     */   }
/*     */ 
/*     */   public void setGridParameter(String name, String value)
/*     */   {
/* 192 */     this.gridParameters.put(name, value);
/*     */   }
/*     */ 
/*     */   public String getGridParameter(String name)
/*     */   {
/* 200 */     if (this.gridParameters.containsKey(name)) {
/* 201 */       return (String)this.gridParameters.get(name);
/*     */     }
/*     */ 
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   private void initializeGridParameters()
/*     */   {
/* 213 */     initializeParameterNames();
/*     */ 
/* 215 */     this.gridParameters = new LinkedHashMap(parameterNames.size());
/*     */ 
/* 217 */     for (String name : parameterNames)
/* 218 */       this.gridParameters.put(name, "");
/*     */   }
/*     */ 
/*     */   public static final LinkedHashMap<String, String> loadParameters(String localizationName)
/*     */   {
/* 229 */     LinkedHashMap params = new LinkedHashMap();
/*     */ 
/* 232 */     String fname = null;
/* 233 */     if (PgenStaticDataProvider.getProvider().getStaticFile(localizationName) != null) {
/* 234 */       fname = PgenStaticDataProvider.getProvider().getStaticFile(localizationName).getAbsolutePath();
/*     */     }
/*     */ 
/* 237 */     File thisFile = null;
/* 238 */     if (fname != null) {
/* 239 */       thisFile = new File(fname);
/* 240 */       if ((!thisFile.exists()) || (!thisFile.canRead())) {
/* 241 */         thisFile = null;
/*     */       }
/*     */     }
/*     */ 
/* 245 */     if (thisFile == null) {
/* 246 */       return params;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 251 */       Scanner fileScanner = new Scanner(thisFile);
/* 252 */       Scanner lineScanner = null;
/*     */       try
/*     */       {
/* 256 */         while (fileScanner.hasNextLine()) {
/* 257 */           String nextLine = fileScanner.nextLine().trim();
/*     */ 
/* 260 */           if (!nextLine.startsWith("!")) {
/* 261 */             lineScanner = new Scanner(nextLine);
/*     */ 
/* 263 */             if (lineScanner.hasNext()) {
/* 264 */               String name = lineScanner.next();
/* 265 */               String value = "";
/*     */ 
/* 267 */               if (lineScanner.hasNext()) {
/* 268 */                 value = lineScanner.next();
/*     */               }
/*     */ 
/* 271 */               params.put(name, value);
/*     */             }
/*     */ 
/* 274 */             lineScanner.close();
/*     */           }
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/* 280 */         fileScanner.close();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 284 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 287 */     return params;
/*     */   }
/*     */ 
/*     */   public static String getParamValues(HashMap<String, String> map, String paramName)
/*     */   {
/* 300 */     String mapVals = null;
/* 301 */     if ((map != null) && (paramName != null)) {
/* 302 */       mapVals = (String)map.get(paramName);
/*     */     }
/*     */ 
/* 305 */     if (mapVals != null) {
/* 306 */       return mapVals;
/*     */     }
/*     */ 
/* 310 */     return new String("");
/*     */   }
/*     */ 
/*     */   public static float getValueForLabel(CatMap cmap, String label)
/*     */   {
/* 323 */     float lblValue = -9999.0F;
/*     */ 
/* 325 */     if ((label != null) && (cmap != null))
/*     */     {
/* 327 */       Float cmapValue = cmap.getMatchingValueForLabel(label);
/* 328 */       if (cmapValue.equals(Float.valueOf((0.0F / 0.0F)))) {
/*     */         try
/*     */         {
/* 331 */           lblValue = Float.parseFloat(label);
/*     */         }
/*     */         catch (Exception e) {
/* 334 */           lblValue = -9999.0F;
/*     */         }
/*     */       }
/*     */       else {
/* 338 */         lblValue = cmapValue.floatValue();
/*     */       }
/*     */     }
/*     */ 
/* 342 */     return lblValue;
/*     */   }
/*     */ 
/*     */   public static String getLabelForValue(CatMap cmap, float value)
/*     */   {
/* 354 */     String lbl = Float.toString(value);
/*     */ 
/* 356 */     if (cmap != null)
/*     */     {
/* 358 */       String cmapLabel = cmap.getMatchingLabelForValue(Float.valueOf(value));
/* 359 */       if (cmapLabel != null) {
/* 360 */         lbl = cmapLabel;
/*     */       }
/*     */     }
/*     */ 
/* 364 */     return lbl;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.graphtogrid.GraphToGrid
 * JD-Core Version:    0.6.2
 */