/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import gov.noaa.nws.ncep.common.staticdata.Cwa;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.common.staticdata.Rfc;
/*     */ import gov.noaa.nws.ncep.common.staticdata.USState;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.CONNECT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_PART, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.MODIFY, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.INTERPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.ADD_POINT})
/*     */ public class Spenes extends Line
/*     */ {
/*     */   private ArrayList<USState> states;
/*     */   private ArrayList<Rfc> rfcs;
/*     */   private ArrayList<Cwa> cwas;
/*  45 */   private String stateZ000 = null;
/*  46 */   private String initDateTime = null;
/*  47 */   private String latestDataUsed = null;
/*  48 */   private int obsHr = -1;
/*  49 */   private String forecasters = null;
/*  50 */   private String location = null;
/*  51 */   private String attnWFOs = null;
/*  52 */   private String attnRFCs = null;
/*  53 */   private String event = null;
/*  54 */   private String satAnalysisTrend = null;
/*  55 */   private int shortTermBegin = -1;
/*  56 */   private int shortTermEnd = -1;
/*  57 */   private String outlookLevel = null;
/*  58 */   private String addlInfo = null;
/*  59 */   private String latLon = null;
/*     */ 
/*     */   public Spenes()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Spenes(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, boolean closed, boolean filled, List<Coordinate> linePoints, int smoothFactor, FillPatternList.FillPattern fillPattern, String pgenCategory, String pgenType)
/*     */   {
/*  70 */     super(range, colors, lineWidth, sizeScale, closed, filled, 
/*  70 */       linePoints, smoothFactor, fillPattern, pgenCategory, pgenType);
/*     */   }
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/*  82 */     Spenes newEl = new Spenes();
/*     */ 
/*  87 */     newEl.setPgenCategory(new String(getPgenCategory()));
/*  88 */     newEl.setPgenType(new String(getPgenType()));
/*  89 */     newEl.setParent(getParent());
/*     */ 
/*  91 */     newEl.update(this);
/*     */ 
/*  97 */     ArrayList ptsCopy = new ArrayList();
/*  98 */     for (int i = 0; i < getPoints().size(); i++) {
/*  99 */       ptsCopy.add(new Coordinate((Coordinate)getPoints().get(i)));
/*     */     }
/* 101 */     newEl.setPoints(ptsCopy);
/*     */ 
/* 107 */     Color[] colorCopy = new Color[getColors().length];
/* 108 */     for (int i = 0; i < getColors().length; i++) {
/* 109 */       colorCopy[i] = new Color(getColors()[i].getRed(), 
/* 110 */         getColors()[i].getGreen(), 
/* 111 */         getColors()[i].getBlue());
/*     */     }
/* 113 */     newEl.setColors(colorCopy);
/* 114 */     newEl.setLineWidth(getLineWidth());
/* 115 */     newEl.setSmoothFactor(getSmoothFactor());
/*     */ 
/* 117 */     newEl.setInitDateTime(getInitDateTime());
/* 118 */     newEl.setLatestData(getLatestDataUsed());
/* 119 */     newEl.setObsHr(getObsHr());
/* 120 */     newEl.setForecasters(getForecasters());
/* 121 */     newEl.setStateZ000(getStateZ000());
/* 122 */     newEl.setLocation(getLocation());
/* 123 */     newEl.setAttnWFOs(getAttnWFOs());
/* 124 */     newEl.setAttnRFCs(getAttnRFCs());
/* 125 */     newEl.setEvent(getEvent());
/* 126 */     newEl.setSatAnalysisTrend(getSatAnalysisTrend());
/* 127 */     newEl.setShortTermBegin(getShortTermBegin());
/* 128 */     newEl.setShortTermEnd(getShortTermEnd());
/* 129 */     newEl.setOutlookLevel(getOutlookLevel());
/* 130 */     newEl.setAddlInfo(getAddlInfo());
/* 131 */     newEl.setLatLon(getLatLon());
/*     */ 
/* 133 */     return newEl;
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 141 */     return "LINE_SOLID";
/*     */   }
/*     */ 
/*     */   public Boolean getClosed()
/*     */   {
/* 148 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   public void generateStatesWfosRfcs()
/*     */   {
/* 156 */     Polygon poly = toJTSPolygon();
/* 157 */     setStates(PgenStaticDataProvider.getProvider().statesInGeometry(poly));
/* 158 */     setRfcs(PgenStaticDataProvider.getProvider().rfcsInGeometry(poly));
/* 159 */     setCwas(PgenStaticDataProvider.getProvider().cwasInGeometry(poly));
/*     */   }
/*     */ 
/*     */   public void setStates(ArrayList<USState> states)
/*     */   {
/* 166 */     this.states = states;
/*     */   }
/*     */ 
/*     */   public ArrayList<USState> getStates()
/*     */   {
/* 173 */     return this.states;
/*     */   }
/*     */ 
/*     */   public void setRfcs(ArrayList<Rfc> rfcs)
/*     */   {
/* 180 */     this.rfcs = rfcs;
/*     */   }
/*     */ 
/*     */   public ArrayList<Rfc> getRfcs()
/*     */   {
/* 187 */     return this.rfcs;
/*     */   }
/*     */ 
/*     */   public void setCwas(ArrayList<Cwa> cwas)
/*     */   {
/* 194 */     this.cwas = cwas;
/*     */   }
/*     */ 
/*     */   public ArrayList<Cwa> getCwas()
/*     */   {
/* 201 */     return this.cwas;
/*     */   }
/*     */ 
/*     */   public String getStateZ000()
/*     */   {
/* 208 */     return this.stateZ000;
/*     */   }
/*     */ 
/*     */   public void setStateZ000(String statesZ000)
/*     */   {
/* 215 */     this.stateZ000 = statesZ000;
/*     */   }
/*     */ 
/*     */   public void setStateZ000(ArrayList<USState> states)
/*     */   {
/* 222 */     int ii = 0;
/* 223 */     StringBuilder sb = new StringBuilder();
/* 224 */     for (USState st : states) {
/* 225 */       sb.append(st.getStateAbrv());
/* 226 */       sb.append("Z000-");
/* 227 */       ii++;
/* 228 */       if (ii % 9 == 0) sb.append("\n");
/*     */     }
/*     */ 
/* 231 */     this.stateZ000 = sb.toString();
/*     */   }
/*     */ 
/*     */   public String getInitDateTime()
/*     */   {
/* 238 */     return this.initDateTime;
/*     */   }
/*     */ 
/*     */   public void setInitTime()
/*     */   {
/* 247 */     Calendar init = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 248 */     if (init != null)
/* 249 */       this.initDateTime = String.format("%1$tm/%1$td/%1$ty %1$tH%1$tMZ", new Object[] { init });
/*     */   }
/*     */ 
/*     */   public void setInitDateTime(String initDateTime)
/*     */   {
/* 257 */     if (initDateTime == null) setInitTime();
/* 258 */     this.initDateTime = initDateTime;
/*     */   }
/*     */ 
/*     */   public String getLatestDataUsed()
/*     */   {
/* 265 */     return this.latestDataUsed;
/*     */   }
/*     */ 
/*     */   public void setLatestData(String latestDataUsed)
/*     */   {
/* 272 */     this.latestDataUsed = latestDataUsed;
/*     */   }
/*     */ 
/*     */   public int getObsHr()
/*     */   {
/* 280 */     return this.obsHr;
/*     */   }
/*     */ 
/*     */   public void setObsHr(int obsHr)
/*     */   {
/* 287 */     this.obsHr = obsHr;
/*     */   }
/*     */ 
/*     */   public String getForecasters()
/*     */   {
/* 294 */     return this.forecasters;
/*     */   }
/*     */ 
/*     */   public void setForecasters(String forecasters)
/*     */   {
/* 301 */     this.forecasters = forecasters;
/*     */   }
/*     */ 
/*     */   public String getLocation()
/*     */   {
/* 308 */     return this.location;
/*     */   }
/*     */ 
/*     */   public void setLocation(String statesName)
/*     */   {
/* 315 */     this.location = statesName;
/*     */   }
/*     */ 
/*     */   public void setLocation(ArrayList<USState> states)
/*     */   {
/* 322 */     int ii = 0;
/* 323 */     StringBuilder sb = new StringBuilder();
/* 324 */     for (USState st : states) {
/* 325 */       sb.append(st.getName());
/* 326 */       sb.append("...");
/* 327 */       ii++;
/* 328 */       if (ii % 6 == 0) sb.append("\n\n");
/*     */     }
/* 330 */     this.location = sb.toString();
/*     */   }
/*     */ 
/*     */   public String getAttnWFOs()
/*     */   {
/* 337 */     return this.attnWFOs;
/*     */   }
/*     */ 
/*     */   public void setAttnWFOs(String cwas)
/*     */   {
/* 344 */     this.attnWFOs = cwas;
/*     */   }
/*     */ 
/*     */   public void setAttnWFOs(ArrayList<Cwa> cwas)
/*     */   {
/* 351 */     int ii = 0;
/* 352 */     StringBuilder sb = new StringBuilder();
/* 353 */     for (Cwa cwa : cwas) {
/* 354 */       sb.append(cwa.getWfoName());
/* 355 */       sb.append("...");
/* 356 */       ii++;
/* 357 */       if (ii % 11 == 0) sb.append("\n\n");
/*     */     }
/* 359 */     this.attnWFOs = sb.toString();
/*     */   }
/*     */ 
/*     */   public String getAttnRFCs()
/*     */   {
/* 366 */     return this.attnRFCs;
/*     */   }
/*     */ 
/*     */   public void setAttnRFCs(String rfcs)
/*     */   {
/* 373 */     this.attnRFCs = rfcs;
/*     */   }
/*     */ 
/*     */   public void setAttnRFCs(ArrayList<Rfc> rfcs)
/*     */   {
/* 380 */     int ii = 0;
/* 381 */     StringBuilder sb = new StringBuilder();
/* 382 */     for (Rfc rfc : rfcs) {
/* 383 */       sb.append(rfc.getBasinId());
/* 384 */       sb.append("...");
/* 385 */       ii++;
/* 386 */       if (ii % 6 == 0) sb.append("\n\n");
/*     */     }
/* 388 */     this.attnRFCs = sb.toString();
/*     */   }
/*     */ 
/*     */   public void setEvent(String event)
/*     */   {
/* 395 */     this.event = event;
/*     */   }
/*     */ 
/*     */   public String getEvent()
/*     */   {
/* 402 */     return this.event;
/*     */   }
/*     */ 
/*     */   public void setSatAnalysisTrend(String satAnalysisTrend)
/*     */   {
/* 409 */     this.satAnalysisTrend = satAnalysisTrend;
/*     */   }
/*     */ 
/*     */   public String getSatAnalysisTrend()
/*     */   {
/* 416 */     return this.satAnalysisTrend;
/*     */   }
/*     */ 
/*     */   public int getShortTermBegin()
/*     */   {
/* 423 */     return this.shortTermBegin;
/*     */   }
/*     */ 
/*     */   public void setShortTermBegin(int shortTermBegin)
/*     */   {
/* 430 */     this.shortTermBegin = shortTermBegin;
/*     */   }
/*     */ 
/*     */   public int getShortTermEnd()
/*     */   {
/* 437 */     return this.shortTermEnd;
/*     */   }
/*     */ 
/*     */   public void setShortTermEnd(int shortTermEnd)
/*     */   {
/* 444 */     this.shortTermEnd = shortTermEnd;
/*     */   }
/*     */ 
/*     */   public String getOutlookLevel()
/*     */   {
/* 451 */     return this.outlookLevel;
/*     */   }
/*     */ 
/*     */   public void setOutlookLevel(String outlookLevel)
/*     */   {
/* 458 */     this.outlookLevel = outlookLevel;
/*     */   }
/*     */ 
/*     */   public void setAddlInfo(String addlInfo)
/*     */   {
/* 465 */     this.addlInfo = addlInfo;
/*     */   }
/*     */ 
/*     */   public String getAddlInfo()
/*     */   {
/* 472 */     return this.addlInfo;
/*     */   }
/*     */ 
/*     */   public String getLatLon()
/*     */   {
/* 479 */     return this.latLon;
/*     */   }
/*     */ 
/*     */   public void setLatLon(Coordinate[] coords)
/*     */   {
/* 486 */     int ii = 0;
/* 487 */     StringBuilder sb = new StringBuilder();
/* 488 */     for (Coordinate coord : coords) {
/* 489 */       sb.append((int)(coord.y * 100.0D));
/* 490 */       sb.append(" ");
/* 491 */       sb.append((int)(coord.x * 100.0D));
/* 492 */       sb.append(" ");
/* 493 */       ii++;
/* 494 */       if (ii % 4 == 0) sb.append("\n\n");
/*     */     }
/* 496 */     this.latLon = sb.toString();
/*     */   }
/*     */ 
/*     */   public void setLatLon(String latlon)
/*     */   {
/* 503 */     this.latLon = latlon;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Spenes
 * JD-Core Version:    0.6.2
 */