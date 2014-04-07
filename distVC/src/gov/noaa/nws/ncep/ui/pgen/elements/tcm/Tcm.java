/*     */ package gov.noaa.nws.ncep.ui.pgen.elements.tcm;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Tcm extends MultiPointElement
/*     */   implements ITcm
/*     */ {
/*     */   private String stormName;
/*     */   private String stormType;
/*     */   private int stormNumber;
/*     */   private int advisoryNumber;
/*     */   private String basin;
/*     */   private int eyeSize;
/*     */   private int positionAccuracy;
/*     */   private boolean correction;
/*     */   private Calendar time;
/*     */   private int centralPressure;
/*     */   private TcmWindQuarters waveQuatro;
/*     */   private ArrayList<TcmFcst> tcmFcst;
/*     */ 
/*     */   public Tcm()
/*     */   {
/*  54 */     this.tcmFcst = new ArrayList();
/*     */   }
/*     */ 
/*     */   public Tcm(String stormType, int stormNum, int advisoryNum, String name, String basin, int eyeSize, int posAccuracy, boolean corr, Calendar time, int pressure)
/*     */   {
/*  60 */     this.pgenCategory = "MET";
/*  61 */     this.pgenType = "TCM";
/*     */ 
/*  63 */     this.basin = basin;
/*  64 */     this.stormType = stormType;
/*  65 */     this.stormNumber = stormNum;
/*  66 */     this.advisoryNumber = advisoryNum;
/*  67 */     this.stormName = name;
/*  68 */     this.centralPressure = pressure;
/*     */ 
/*  70 */     this.eyeSize = eyeSize;
/*  71 */     this.positionAccuracy = posAccuracy;
/*  72 */     this.correction = corr;
/*     */ 
/*  74 */     setTime(time);
/*  75 */     this.tcmFcst = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void addTcmFcst(TcmFcst fcst)
/*     */   {
/*  82 */     this.tcmFcst.add(fcst);
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent copy()
/*     */   {
/*  92 */     Tcm newTcm = new Tcm(this.stormType, this.stormNumber, this.advisoryNumber, 
/*  93 */       this.stormName, this.basin, this.eyeSize, this.positionAccuracy, 
/*  94 */       isCorrection(), 
/*  95 */       getTime(), this.centralPressure);
/*  96 */     newTcm.setWaveQuatro((TcmWindQuarters)this.waveQuatro.copy());
/*     */ 
/*  98 */     for (TcmFcst fcst : this.tcmFcst) {
/*  99 */       newTcm.addTcmFcst((TcmFcst)fcst.copy());
/*     */     }
/*     */ 
/* 102 */     newTcm.setStormType(getStormType());
/* 103 */     newTcm.setStormNumber(getStormNumber());
/* 104 */     newTcm.setAdvisoryNumber(getAdvisoryNumber());
/*     */ 
/* 106 */     return newTcm;
/*     */   }
/*     */ 
/*     */   public double[][] getWindRadius()
/*     */   {
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   public List<TcmFcst> getTcmFcst()
/*     */   {
/* 117 */     return this.tcmFcst;
/*     */   }
/*     */ 
/*     */   public void setTcmFcst(List<TcmFcst> fcst) {
/* 121 */     this.tcmFcst = ((ArrayList)fcst);
/*     */   }
/*     */ 
/*     */   public String getStormName()
/*     */   {
/* 127 */     return this.stormName;
/*     */   }
/*     */ 
/*     */   public int getCentralPressure()
/*     */   {
/* 132 */     return this.centralPressure;
/*     */   }
/*     */ 
/*     */   public void setTime(Calendar time) {
/* 136 */     this.time = time;
/*     */   }
/*     */ 
/*     */   public Calendar getAdvisoryTime()
/*     */   {
/* 141 */     return this.time;
/*     */   }
/*     */ 
/*     */   public Calendar getTime() {
/* 145 */     return this.time;
/*     */   }
/*     */ 
/*     */   public int getFcstHr()
/*     */   {
/* 151 */     return 0;
/*     */   }
/*     */ 
/*     */   public void setWaveQuatro(TcmWindQuarters waveQuatro) {
/* 155 */     this.waveQuatro = waveQuatro;
/*     */   }
/*     */ 
/*     */   public TcmWindQuarters getWaveQuarters() {
/* 159 */     return this.waveQuatro;
/*     */   }
/*     */ 
/*     */   public void setStormType(String stormType) {
/* 163 */     this.stormType = stormType;
/*     */   }
/*     */ 
/*     */   public String getStormType() {
/* 167 */     return this.stormType;
/*     */   }
/*     */ 
/*     */   public void setStormNumber(int stormNumber) {
/* 171 */     this.stormNumber = stormNumber;
/*     */   }
/*     */ 
/*     */   public int getStormNumber() {
/* 175 */     return this.stormNumber;
/*     */   }
/*     */ 
/*     */   public void setAdvisoryNumber(int advisoryNumber) {
/* 179 */     this.advisoryNumber = advisoryNumber;
/*     */   }
/*     */ 
/*     */   public int getAdvisoryNumber() {
/* 183 */     return this.advisoryNumber;
/*     */   }
/*     */ 
/*     */   public void setBasin(String basin) {
/* 187 */     this.basin = basin;
/*     */   }
/*     */ 
/*     */   public String getBasin() {
/* 191 */     return this.basin;
/*     */   }
/*     */ 
/*     */   public void setEyeSize(int eyeSize) {
/* 195 */     this.eyeSize = eyeSize;
/*     */   }
/*     */ 
/*     */   public int getEyeSize() {
/* 199 */     return this.eyeSize;
/*     */   }
/*     */ 
/*     */   public int getPositionAccuracy() {
/* 203 */     return this.positionAccuracy;
/*     */   }
/*     */ 
/*     */   public void setPositionAccuracy(int positionAccuracy) {
/* 207 */     this.positionAccuracy = positionAccuracy;
/*     */   }
/*     */ 
/*     */   public void setCorrection(boolean correction) {
/* 211 */     this.correction = correction;
/*     */   }
/*     */ 
/*     */   public boolean isCorrection() {
/* 215 */     return this.correction;
/*     */   }
/*     */ 
/*     */   public void setStormName(String stormName) {
/* 219 */     this.stormName = stormName;
/*     */   }
/*     */ 
/*     */   public void setCentralPressure(int centralPressure) {
/* 223 */     this.centralPressure = centralPressure;
/*     */   }
/*     */ 
/*     */   public ArrayList<Coordinate> getPoints()
/*     */   {
/* 228 */     ArrayList ret = new ArrayList();
/* 229 */     for (TcmFcst fcst : this.tcmFcst) {
/* 230 */       ret.add(fcst.getLocation());
/*     */     }
/* 232 */     return ret;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLinePoints()
/*     */   {
/* 237 */     return (Coordinate[])getPoints().toArray(new Coordinate[getPoints().size()]);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.tcm.Tcm
 * JD-Core Version:    0.6.2
 */