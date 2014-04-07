/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Product
/*     */ {
/*     */   private String name;
/*     */   private String type;
/*     */   private String forecaster;
/*     */   private String center;
/*     */   private ProductInfo info;
/*     */   private ProductTime time;
/*     */   private String inputFile;
/*     */   private String outputFile;
/*     */   private List<Layer> layers;
/*     */   private boolean onOff;
/*     */   private boolean inUse;
/*     */   private boolean useFile;
/*     */   private boolean saveLayers;
/*     */ 
/*     */   public Product()
/*     */   {
/*  52 */     this.name = "Default";
/*  53 */     this.type = "Default";
/*  54 */     this.forecaster = "Default";
/*  55 */     setCenter("Default");
/*  56 */     this.info = new ProductInfo();
/*  57 */     this.time = new ProductTime();
/*  58 */     this.layers = new ArrayList();
/*  59 */     this.onOff = true;
/*  60 */     this.inUse = true;
/*  61 */     this.inputFile = null;
/*  62 */     this.outputFile = null;
/*  63 */     this.useFile = false;
/*  64 */     this.saveLayers = false;
/*     */   }
/*     */ 
/*     */   public Product(String myName, String myType, String myForecaster, ProductInfo myInfo, ProductTime myTime, ArrayList<Layer> myLayers)
/*     */   {
/*  69 */     this.name = myName;
/*  70 */     this.type = myType;
/*  71 */     this.forecaster = myForecaster;
/*  72 */     setCenter("Default");
/*  73 */     this.info = myInfo;
/*  74 */     this.time = myTime;
/*  75 */     this.layers = myLayers;
/*  76 */     this.onOff = true;
/*  77 */     this.inUse = true;
/*  78 */     this.inputFile = null;
/*  79 */     this.outputFile = null;
/*  80 */     this.useFile = false;
/*  81 */     this.saveLayers = false;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  86 */     return this.name;
/*     */   }
/*     */   public void setName(String myName) {
/*  89 */     this.name = myName;
/*     */   }
/*     */ 
/*     */   public void setType(String type) {
/*  93 */     this.type = type;
/*     */   }
/*     */ 
/*     */   public String getType() {
/*  97 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String getForecaster() {
/* 101 */     return this.forecaster;
/*     */   }
/*     */ 
/*     */   public void setForecaster(String myForecaster) {
/* 105 */     this.forecaster = myForecaster;
/*     */   }
/*     */ 
/*     */   public ProductInfo getInfo()
/*     */   {
/* 112 */     return this.info;
/*     */   }
/*     */ 
/*     */   public void setInfo(ProductInfo myInfo)
/*     */   {
/* 119 */     this.info = myInfo;
/*     */   }
/*     */ 
/*     */   public ProductTime getTime() {
/* 123 */     return this.time;
/*     */   }
/*     */ 
/*     */   public void setTime(ProductTime myTime) {
/* 127 */     this.time = myTime;
/*     */   }
/*     */ 
/*     */   public List<Layer> getLayers() {
/* 131 */     return this.layers;
/*     */   }
/*     */ 
/*     */   public void setLayers(List<Layer> myLayers) {
/* 135 */     this.layers = myLayers;
/*     */   }
/*     */ 
/*     */   public Layer getLayer(int index) {
/* 139 */     return (Layer)this.layers.get(index);
/*     */   }
/*     */ 
/*     */   public Layer getLayer(String layerName) {
/* 143 */     for (Layer ly : this.layers) {
/* 144 */       if (ly.getName().equals(layerName)) {
/* 145 */         return ly;
/*     */       }
/*     */     }
/*     */ 
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */   public void addLayer(Layer layer) {
/* 153 */     this.layers.add(layer);
/*     */   }
/*     */ 
/*     */   public void addLayer(int index, Layer layer) {
/* 157 */     this.layers.add(index, layer);
/*     */   }
/*     */ 
/*     */   public void removeLayer(int index) {
/* 161 */     this.layers.remove(index);
/*     */   }
/*     */ 
/*     */   public void removeLayer(Layer lyr)
/*     */   {
/* 169 */     this.layers.remove(lyr);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 176 */     this.layers.clear();
/*     */   }
/*     */ 
/*     */   public String makeProduct() {
/* 180 */     return "Make Product .......... " + this.name;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 184 */     StringBuilder result = new StringBuilder("\n");
/* 185 */     result.append("name:\t\t" + this.name + "\n");
/* 186 */     result.append("type:\t\t" + this.type + "\n");
/* 187 */     result.append("forecaster:\t\t" + this.forecaster + "\n");
/* 188 */     result.append("center:\t\t" + this.center + "\n");
/* 189 */     result.append("inputFile:\t\t" + this.inputFile + "\n");
/* 190 */     result.append("outputFile:\t\t" + this.outputFile + "\n");
/* 191 */     result.append("info:\t\t" + this.info + "\n");
/* 192 */     result.append("time:\t\t" + this.time + "\n");
/* 193 */     result.append("OnOff:\t\t" + this.onOff + "\n");
/* 194 */     result.append("InUse:\t\t" + this.inUse + "\n");
/*     */ 
/* 196 */     result.append("\nTotal Layers:\t" + this.layers.size() + "\n");
/*     */ 
/* 198 */     int ii = 0;
/* 199 */     for (Layer ly : this.layers) {
/* 200 */       result.append("Layer:\t" + ii);
/* 201 */       result.append(ly);
/* 202 */       result.append("\n");
/* 203 */       ii++;
/*     */     }
/*     */ 
/* 206 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public boolean contains(Layer lyr)
/*     */   {
/* 215 */     return this.layers.contains(lyr);
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 223 */     return this.layers.isEmpty();
/*     */   }
/*     */ 
/*     */   public Product copy()
/*     */   {
/* 231 */     Product prd = new Product();
/*     */ 
/* 233 */     prd.setName(getName());
/* 234 */     prd.setType(getType());
/* 235 */     prd.setForecaster(getForecaster());
/* 236 */     prd.setCenter(getCenter());
/* 237 */     prd.setInfo(getInfo());
/* 238 */     prd.setTime(getTime());
/* 239 */     prd.setInputFile(getInputFile());
/* 240 */     prd.setOutputFile(getOutputFile());
/* 241 */     prd.setOnOff(isOnOff());
/* 242 */     prd.setInUse(isInUse());
/* 243 */     prd.setUseFile(isUseFile());
/*     */ 
/* 245 */     for (Layer lyr : getLayers()) {
/* 246 */       prd.addLayer(lyr.copy());
/*     */     }
/*     */ 
/* 249 */     return prd;
/*     */   }
/*     */ 
/*     */   public void setOnOff(boolean isOnOff)
/*     */   {
/* 257 */     this.onOff = isOnOff;
/*     */   }
/*     */ 
/*     */   public boolean isOnOff() {
/* 261 */     return this.onOff;
/*     */   }
/*     */ 
/*     */   public void setInUse(boolean inUse) {
/* 265 */     this.inUse = inUse;
/*     */   }
/*     */ 
/*     */   public boolean isInUse() {
/* 269 */     return this.inUse;
/*     */   }
/*     */ 
/*     */   public void setCenter(String center) {
/* 273 */     this.center = center;
/*     */   }
/*     */ 
/*     */   public String getCenter() {
/* 277 */     return this.center;
/*     */   }
/*     */ 
/*     */   public void setInputFile(String fileName) {
/* 281 */     this.inputFile = fileName;
/*     */   }
/*     */ 
/*     */   public String getInputFile() {
/* 285 */     return this.inputFile;
/*     */   }
/*     */ 
/*     */   public void setOutputFile(String fileName) {
/* 289 */     this.outputFile = fileName;
/*     */   }
/*     */ 
/*     */   public String getOutputFile() {
/* 293 */     return this.outputFile;
/*     */   }
/*     */ 
/*     */   public void setUseFile(boolean useFile)
/*     */   {
/* 298 */     this.useFile = useFile;
/*     */   }
/*     */ 
/*     */   public boolean isUseFile() {
/* 302 */     return this.useFile;
/*     */   }
/*     */ 
/*     */   public void setSaveLayers(boolean saveLayers)
/*     */   {
/* 309 */     this.saveLayers = saveLayers;
/*     */   }
/*     */ 
/*     */   public boolean isSaveLayers()
/*     */   {
/* 316 */     return this.saveLayers;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Product
 * JD-Core Version:    0.6.2
 */