/*     */ package gov.noaa.nws.ncep.ui.pgen.sigmet;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_POINT})
/*     */ public class Volcano extends SinglePointElement
/*     */   implements ISigmet
/*     */ {
/*     */   public static final String NIL_STRING = "NIL";
/*     */   public static final String WORD_SPLITTER = ":::";
/*  48 */   Coordinate[] locs = null;
/*     */ 
/*  50 */   private String name = null;
/*  51 */   private String number = null;
/*  52 */   private String txtLoc = null;
/*  53 */   private String area = null;
/*  54 */   private String elev = null;
/*     */ 
/*  56 */   private String origStnVAAC = null;
/*  57 */   private String wmoId = null;
/*  58 */   private String hdrNum = null;
/*  59 */   private String product = null;
/*  60 */   private String year = null;
/*  61 */   private String advNum = null;
/*  62 */   private String corr = null;
/*     */ 
/*  64 */   private String infoSource = null;
/*  65 */   private String addInfoSource = null;
/*  66 */   private String aviColorCode = null;
/*  67 */   private String erupDetails = null;
/*     */ 
/*  69 */   private String obsAshDate = null;
/*  70 */   private String obsAshTime = null;
/*  71 */   private String nil = null;
/*     */ 
/*  73 */   private String obsFcstAshCloudInfo = null;
/*  74 */   private String obsFcstAshCloudInfo6 = null;
/*  75 */   private String obsFcstAshCloudInfo12 = null;
/*  76 */   private String obsFcstAshCloudInfo18 = null;
/*     */ 
/*  78 */   private String remarks = null;
/*  79 */   private String nextAdv = null;
/*  80 */   private String forecasters = null;
/*     */ 
/*     */   public void setLinePoints(ArrayList<Coordinate> locs)
/*     */   {
/*  84 */     this.locs = ((Coordinate[])locs.toArray(new Coordinate[0]));
/*  85 */     if ((locs != null) && (locs.size() > 0))
/*  86 */       setLocation((Coordinate)locs.get(0));
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLinePoints() {
/*  90 */     return new Coordinate[] { this.locs != null ? this.locs : getLocation() };
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent copy()
/*     */   {
/*  96 */     Volcano newVol = new Volcano();
/*  97 */     newVol.update(this);
/*     */ 
/*  99 */     newVol.setColors(new Color[] { 
/* 100 */       new Color(getColors()[0].getRed(), 
/* 101 */       getColors()[0].getGreen(), 
/* 102 */       getColors()[0].getBlue()) });
/* 103 */     newVol.setLocation(new Coordinate(getLocation()));
/* 104 */     newVol.setPgenCategory(this.pgenCategory);
/* 105 */     newVol.setPgenType(this.pgenType);
/* 106 */     newVol.setParent(getParent());
/*     */ 
/* 110 */     return newVol;
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 116 */     return getPgenType();
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 120 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String name) {
/* 124 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public String getNumber() {
/* 128 */     return this.number;
/*     */   }
/*     */ 
/*     */   public void setNumber(String number) {
/* 132 */     this.number = number;
/*     */   }
/*     */ 
/*     */   public String getArea() {
/* 136 */     return this.area;
/*     */   }
/*     */ 
/*     */   public void setArea(String area) {
/* 140 */     this.area = area;
/*     */   }
/*     */ 
/*     */   public String getElev() {
/* 144 */     return this.elev;
/*     */   }
/*     */ 
/*     */   public void setElev(String elev) {
/* 148 */     this.elev = elev;
/*     */   }
/*     */ 
/*     */   public String getTxtLoc() {
/* 152 */     return this.txtLoc;
/*     */   }
/*     */ 
/*     */   public void setTxtLoc(String txtLoc) {
/* 156 */     this.txtLoc = txtLoc;
/*     */   }
/*     */ 
/*     */   public String getOrigStnVAAC() {
/* 160 */     return this.origStnVAAC;
/*     */   }
/*     */ 
/*     */   public void setOrigStnVAAC(String origStnVAAC) {
/* 164 */     this.origStnVAAC = origStnVAAC;
/*     */   }
/*     */ 
/*     */   public String getWmoId() {
/* 168 */     return this.wmoId;
/*     */   }
/*     */ 
/*     */   public void setWmoId(String wmoId) {
/* 172 */     this.wmoId = wmoId;
/*     */   }
/*     */ 
/*     */   public String getHdrNum() {
/* 176 */     return this.hdrNum;
/*     */   }
/*     */ 
/*     */   public void setHdrNum(String hdrNum) {
/* 180 */     this.hdrNum = hdrNum;
/*     */   }
/*     */ 
/*     */   public String getProduct() {
/* 184 */     return this.product;
/*     */   }
/*     */ 
/*     */   public void setProduct(String product) {
/* 188 */     this.product = product;
/*     */   }
/*     */ 
/*     */   public String getYear() {
/* 192 */     return this.year;
/*     */   }
/*     */ 
/*     */   public void setYear(String year) {
/* 196 */     this.year = year;
/*     */   }
/*     */ 
/*     */   public String getAdvNum()
/*     */   {
/* 201 */     return this.advNum;
/*     */   }
/*     */ 
/*     */   public void setAdvNum(String advNum) {
/* 205 */     this.advNum = advNum;
/*     */   }
/*     */ 
/*     */   public String getCorr() {
/* 209 */     return this.corr;
/*     */   }
/*     */ 
/*     */   public void setCorr(String corr) {
/* 213 */     this.corr = corr;
/*     */   }
/*     */ 
/*     */   public String getInfoSource() {
/* 217 */     return this.infoSource;
/*     */   }
/*     */ 
/*     */   public void setInfoSource(String infoSource) {
/* 221 */     this.infoSource = infoSource;
/*     */   }
/*     */ 
/*     */   public String getAddInfoSource() {
/* 225 */     return this.addInfoSource;
/*     */   }
/*     */ 
/*     */   public void setAddInfoSource(String addInfoSource) {
/* 229 */     this.addInfoSource = addInfoSource;
/*     */   }
/*     */ 
/*     */   public String getAviColorCode() {
/* 233 */     return this.aviColorCode;
/*     */   }
/*     */ 
/*     */   public void setAviColorCode(String aviColorCode) {
/* 237 */     this.aviColorCode = aviColorCode;
/*     */   }
/*     */ 
/*     */   public String getErupDetails() {
/* 241 */     return this.erupDetails;
/*     */   }
/*     */ 
/*     */   public void setErupDetails(String erupDetails) {
/* 245 */     this.erupDetails = erupDetails;
/*     */   }
/*     */ 
/*     */   public String getObsAshDate() {
/* 249 */     return this.obsAshDate;
/*     */   }
/*     */ 
/*     */   public void setObsAshDate(String obsAshDate) {
/* 253 */     this.obsAshDate = obsAshDate;
/*     */   }
/*     */ 
/*     */   public String getObsAshTime() {
/* 257 */     return this.obsAshTime;
/*     */   }
/*     */ 
/*     */   public void setObsAshTime(String obsAshTime) {
/* 261 */     this.obsAshTime = obsAshTime;
/*     */   }
/*     */ 
/*     */   public String getObsFcstAshCloudInfo() {
/* 265 */     return this.obsFcstAshCloudInfo;
/*     */   }
/*     */ 
/*     */   public void setObsFcstAshCloudInfo(String obsFcstAshCloudInfo) {
/* 269 */     this.obsFcstAshCloudInfo = obsFcstAshCloudInfo;
/*     */   }
/*     */ 
/*     */   public String getObsFcstAshCloudInfo6() {
/* 273 */     return this.obsFcstAshCloudInfo6;
/*     */   }
/*     */ 
/*     */   public void setObsFcstAshCloudInfo6(String obsFcstAshCloudInfo6) {
/* 277 */     this.obsFcstAshCloudInfo6 = obsFcstAshCloudInfo6;
/*     */   }
/*     */ 
/*     */   public String getObsFcstAshCloudInfo12() {
/* 281 */     return this.obsFcstAshCloudInfo12;
/*     */   }
/*     */ 
/*     */   public void setObsFcstAshCloudInfo12(String obsFcstAshCloudInfo12) {
/* 285 */     this.obsFcstAshCloudInfo12 = obsFcstAshCloudInfo12;
/*     */   }
/*     */ 
/*     */   public String getObsFcstAshCloudInfo18() {
/* 289 */     return this.obsFcstAshCloudInfo18;
/*     */   }
/*     */ 
/*     */   public void setObsFcstAshCloudInfo18(String obsFcstAshCloudInfo18) {
/* 293 */     this.obsFcstAshCloudInfo18 = obsFcstAshCloudInfo18;
/*     */   }
/*     */ 
/*     */   public String getRemarks() {
/* 297 */     return this.remarks;
/*     */   }
/*     */ 
/*     */   public void setRemarks(String remarks) {
/* 301 */     this.remarks = remarks;
/*     */   }
/*     */ 
/*     */   public String getNextAdv() {
/* 305 */     return this.nextAdv;
/*     */   }
/*     */ 
/*     */   public void setNextAdv(String nextAdv) {
/* 309 */     this.nextAdv = nextAdv;
/*     */   }
/*     */ 
/*     */   public String getForecasters() {
/* 313 */     return this.forecasters;
/*     */   }
/*     */ 
/*     */   public void setForecasters(String forecasters) {
/* 317 */     this.forecasters = forecasters;
/*     */   }
/*     */ 
/*     */   public String getNil() {
/* 321 */     return this.nil;
/*     */   }
/*     */ 
/*     */   public void setNil(String nil) {
/* 325 */     this.nil = nil;
/*     */   }
/*     */ 
/*     */   public ArrayList<Coordinate> getPoints()
/*     */   {
/* 338 */     if (VaaInfo.isNonDrawableVol(this)) {
/* 339 */       return new ArrayList();
/*     */     }
/* 341 */     return super.getPoints();
/*     */   }
/*     */ 
/*     */   public ArrayList<Coordinate> getConverterVolcPoints()
/*     */   {
/* 347 */     return super.getPoints();
/*     */   }
/*     */ 
/*     */   public void setExtraErupDetails(String info)
/*     */   {
/* 357 */     this.erupDetails = (getNoNullTxt(this.erupDetails) + ":::" + info);
/*     */   }
/*     */ 
/*     */   public void setExtraInfoSource(String info)
/*     */   {
/* 367 */     this.infoSource = (getNoNullTxt(this.infoSource) + ":::" + info);
/*     */   }
/*     */ 
/*     */   public void setExtraRemarks(String info)
/*     */   {
/* 377 */     this.remarks = (getNoNullTxt(this.remarks) + ":::" + info);
/*     */   }
/*     */ 
/*     */   public void setExtraNextAdv(String info)
/*     */   {
/* 387 */     this.nextAdv = (getNoNullTxt(this.nextAdv) + ":::" + info);
/*     */   }
/*     */ 
/*     */   public static String getNoNullTxt(String s)
/*     */   {
/* 398 */     return s == null ? "" : s;
/*     */   }
/*     */ 
/*     */   public static String getUserInputPart(String word)
/*     */   {
/* 410 */     String input = "";
/*     */ 
/* 412 */     if (word != null) {
/* 413 */       input = word.split(":::")[0];
/*     */     }
/*     */ 
/* 416 */     return input;
/*     */   }
/*     */ 
/*     */   public int getSmoothFactor()
/*     */   {
/* 422 */     return 0;
/*     */   }
/*     */ 
/*     */   public Boolean isClosedLine()
/*     */   {
/* 428 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 434 */     return null;
/*     */   }
/*     */ 
/*     */   public FillPatternList.FillPattern getFillPattern()
/*     */   {
/* 440 */     return null;
/*     */   }
/*     */ 
/*     */   public String getLineType()
/*     */   {
/* 446 */     return null;
/*     */   }
/*     */ 
/*     */   public double getWidth()
/*     */   {
/* 452 */     return 0.0D;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano
 * JD-Core Version:    0.6.2
 */