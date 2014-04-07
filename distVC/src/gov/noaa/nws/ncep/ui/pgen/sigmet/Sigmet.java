/*     */ package gov.noaa.nws.ncep.ui.pgen.sigmet;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.ADD_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.INTERPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.MODIFY})
/*     */ public class Sigmet extends AbstractSigmet
/*     */ {
/*     */   public static final String SIGMET_PGEN_CATEGORY = "Sigmet";
/*     */   public static final String SIGMET_PGEN_TYPE = "INTL_SIGMET";
/*     */   public static final String AREA = "Area";
/*     */   public static final String LINE = "Line";
/*     */   public static final String ISOLATED = "Isolated";
/*     */   private String editableAttrStatus;
/*     */   private String editableAttrStartTime;
/*     */   private String editableAttrEndTime;
/*     */   private String editableAttrRemarks;
/*     */   private String editableAttrPhenom;
/*     */   private String editableAttrPhenom2;
/*     */   private String editableAttrPhenomName;
/*     */   private String editableAttrPhenomLat;
/*     */   private String editableAttrPhenomLon;
/*     */   private String editableAttrPhenomPressure;
/*     */   private String editableAttrPhenomMaxWind;
/*     */   private String editableAttrFreeText;
/*     */   private String editableAttrTrend;
/*     */   private String editableAttrMovement;
/*     */   private String editableAttrPhenomSpeed;
/*     */   private String editableAttrPhenomDirection;
/*     */   private String editableAttrLevel;
/*     */   private String editableAttrLevelInfo1;
/*     */   private String editableAttrLevelInfo2;
/*     */   private String editableAttrLevelText1;
/*     */   private String editableAttrLevelText2;
/*     */   private String editableAttrFir;
/*     */ 
/*     */   public Sigmet()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Sigmet(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, boolean closed, boolean filled, ArrayList<Coordinate> linePoints, int smoothFactor, FillPatternList.FillPattern fillPattern, String pgenCategory, String pgenType, String type, double width, String editableAttrArea, String editableAttrIssueOffice, String editableAttrStatus, String editableAttrId, String editableAttrSeqNum, String editableAttrStartTime, String editableAttrEndTime, String editableAttrRemarks, String editableAttrPhenom, String editableAttrPhenom2, String editableAttrPhenomName, String editableAttrPhenomLat, String editableAttrPhenomLon, String editableAttrPhenomPressure, String editableAttrPhenomMaxWind, String editableAttrFreeText, String editableAttrTrend, String editableAttrMovement, String editableAttrPhenomSpeed, String editableAttrPhenomDirection, String editableAttrLevel, String editableAttrLevelInfo1, String editableAttrLevelInfo2, String editableAttrLevelText1, String editableAttrLevelText2, String editableAttrFromLine, String editableAttrFir)
/*     */   {
/* 135 */     super(range, 
/* 117 */       colors, 
/* 118 */       lineWidth, 
/* 119 */       sizeScale, 
/* 120 */       closed, 
/* 121 */       filled, 
/* 122 */       linePoints, 
/* 123 */       smoothFactor, 
/* 124 */       fillPattern, 
/* 125 */       pgenCategory, 
/* 126 */       pgenType, 
/* 128 */       type, 
/* 129 */       width, 
/* 131 */       editableAttrArea, 
/* 132 */       editableAttrIssueOffice, 
/* 133 */       editableAttrFromLine, 
/* 134 */       editableAttrId, 
/* 135 */       editableAttrSeqNum);
/*     */ 
/* 137 */     this.editableAttrStatus = editableAttrStatus;
/* 138 */     this.editableAttrStartTime = editableAttrStartTime;
/* 139 */     this.editableAttrEndTime = editableAttrEndTime;
/* 140 */     this.editableAttrRemarks = editableAttrRemarks;
/* 141 */     this.editableAttrPhenom = editableAttrPhenom;
/* 142 */     this.editableAttrPhenom2 = editableAttrPhenom2;
/* 143 */     this.editableAttrPhenomName = editableAttrPhenomName;
/* 144 */     this.editableAttrPhenomLat = editableAttrPhenomLat;
/* 145 */     this.editableAttrPhenomLon = editableAttrPhenomLon;
/* 146 */     this.editableAttrPhenomPressure = editableAttrPhenomPressure;
/* 147 */     this.editableAttrPhenomMaxWind = editableAttrPhenomMaxWind;
/* 148 */     this.editableAttrFreeText = editableAttrFreeText;
/* 149 */     this.editableAttrTrend = editableAttrTrend;
/* 150 */     this.editableAttrMovement = editableAttrMovement;
/* 151 */     this.editableAttrPhenomSpeed = editableAttrPhenomSpeed;
/* 152 */     this.editableAttrPhenomDirection = editableAttrPhenomDirection;
/* 153 */     this.editableAttrLevel = editableAttrLevel;
/* 154 */     this.editableAttrLevelInfo1 = editableAttrLevelInfo1;
/* 155 */     this.editableAttrLevelInfo2 = editableAttrLevelInfo2;
/* 156 */     this.editableAttrLevelText1 = editableAttrLevelText1;
/* 157 */     this.editableAttrLevelText2 = editableAttrLevelText2;
/* 158 */     this.editableAttrFir = editableAttrFir;
/*     */   }
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/* 170 */     Sigmet newSigmet = new Sigmet();
/* 171 */     newSigmet.update(this);
/*     */ 
/* 177 */     ArrayList ptsCopy = new ArrayList();
/* 178 */     for (int i = 0; i < getPoints().size(); i++) {
/* 179 */       ptsCopy.add(new Coordinate((Coordinate)getPoints().get(i)));
/*     */     }
/* 181 */     newSigmet.setPoints(ptsCopy);
/*     */ 
/* 187 */     Color[] colorCopy = new Color[getColors().length];
/* 188 */     for (int i = 0; i < getColors().length; i++) {
/* 189 */       colorCopy[i] = new Color(getColors()[i].getRed(), 
/* 190 */         getColors()[i].getGreen(), 
/* 191 */         getColors()[i].getBlue());
/*     */     }
/* 193 */     newSigmet.setColors(colorCopy);
/*     */ 
/* 198 */     newSigmet.setPgenCategory(new String(getPgenCategory()));
/* 199 */     newSigmet.setPgenType(new String(getPgenType()));
/* 200 */     newSigmet.setParent(getParent());
/* 201 */     newSigmet.setType(getType());
/* 202 */     newSigmet.setWidth(getWidth());
/*     */ 
/* 204 */     newSigmet.setEditableAttrArea(getEditableAttrArea());
/* 205 */     newSigmet.setEditableAttrIssueOffice(getEditableAttrIssueOffice());
/* 206 */     newSigmet.setEditableAttrFromLine(getEditableAttrFromLine());
/* 207 */     newSigmet.setEditableAttrId(getEditableAttrId());
/* 208 */     newSigmet.setEditableAttrSeqNum(getEditableAttrSeqNum());
/*     */ 
/* 211 */     newSigmet.setEditableAttrFreeText(getEditableAttrFreeText());
/* 212 */     newSigmet.setEditableAttrFromLine(getEditableAttrFromLine());
/* 213 */     newSigmet.setEditableAttrStartTime(getEditableAttrStartTime());
/* 214 */     newSigmet.setEditableAttrEndTime(getEditableAttrEndTime());
/* 215 */     newSigmet.setEditableAttrPhenom(getEditableAttrPhenom());
/* 216 */     newSigmet.setEditableAttrPhenom2(getEditableAttrPhenom2());
/* 217 */     newSigmet.setEditableAttrPhenomLat(getEditableAttrPhenomLat());
/* 218 */     newSigmet.setEditableAttrPhenomLon(getEditableAttrPhenomLon());
/* 219 */     newSigmet.setEditableAttrPhenomSpeed(getEditableAttrPhenomSpeed());
/* 220 */     newSigmet.setEditableAttrPhenomDirection(getEditableAttrPhenomDirection());
/*     */ 
/* 222 */     newSigmet.setEditableAttrArea(getEditableAttrArea());
/* 223 */     newSigmet.setEditableAttrRemarks(getEditableAttrRemarks());
/* 224 */     newSigmet.setEditableAttrPhenomName(getEditableAttrPhenomName());
/* 225 */     newSigmet.setEditableAttrPhenomPressure(getEditableAttrPhenomPressure());
/* 226 */     newSigmet.setEditableAttrPhenomMaxWind(getEditableAttrPhenomMaxWind());
/* 227 */     newSigmet.setEditableAttrTrend(getEditableAttrTrend());
/* 228 */     newSigmet.setEditableAttrMovement(getEditableAttrMovement());
/* 229 */     newSigmet.setEditableAttrLevel(getEditableAttrLevel());
/* 230 */     newSigmet.setEditableAttrLevelInfo1(getEditableAttrLevelInfo1());
/* 231 */     newSigmet.setEditableAttrLevelInfo2(getEditableAttrLevelInfo2());
/* 232 */     newSigmet.setEditableAttrLevelText1(getEditableAttrLevelText1());
/* 233 */     newSigmet.setEditableAttrLevelText2(getEditableAttrLevelText2());
/* 234 */     newSigmet.setEditableAttrFir(getEditableAttrFir());
/*     */ 
/* 236 */     return newSigmet;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrStatus()
/*     */   {
/* 242 */     return this.editableAttrStatus;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrStatus(String editableAttrStatus)
/*     */   {
/* 248 */     this.editableAttrStatus = editableAttrStatus;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrStartTime()
/*     */   {
/* 254 */     return this.editableAttrStartTime;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrStartTime(String editableAttrStartTime)
/*     */   {
/* 260 */     this.editableAttrStartTime = editableAttrStartTime;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrEndTime()
/*     */   {
/* 266 */     return this.editableAttrEndTime;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrEndTime(String editableAttrEndTime)
/*     */   {
/* 272 */     this.editableAttrEndTime = editableAttrEndTime;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrRemarks()
/*     */   {
/* 278 */     return this.editableAttrRemarks;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrRemarks(String editableAttrRemarks)
/*     */   {
/* 284 */     this.editableAttrRemarks = editableAttrRemarks;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrPhenom()
/*     */   {
/* 290 */     return this.editableAttrPhenom;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrPhenom(String editableAttrPhenom)
/*     */   {
/* 296 */     this.editableAttrPhenom = editableAttrPhenom;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrPhenom2()
/*     */   {
/* 302 */     return this.editableAttrPhenom2;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrPhenom2(String editableAttrPhenom2)
/*     */   {
/* 308 */     this.editableAttrPhenom2 = editableAttrPhenom2;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrPhenomName()
/*     */   {
/* 314 */     return this.editableAttrPhenomName;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrPhenomName(String editableAttrPhenomName)
/*     */   {
/* 320 */     this.editableAttrPhenomName = editableAttrPhenomName;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrPhenomLat()
/*     */   {
/* 326 */     return this.editableAttrPhenomLat;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrPhenomLat(String editableAttrPhenomLat)
/*     */   {
/* 332 */     this.editableAttrPhenomLat = editableAttrPhenomLat;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrPhenomLon()
/*     */   {
/* 338 */     return this.editableAttrPhenomLon;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrPhenomLon(String editableAttrPhenomLon)
/*     */   {
/* 344 */     this.editableAttrPhenomLon = editableAttrPhenomLon;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrPhenomPressure()
/*     */   {
/* 350 */     return this.editableAttrPhenomPressure;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrPhenomPressure(String editableAttrPhenomPressure)
/*     */   {
/* 356 */     this.editableAttrPhenomPressure = editableAttrPhenomPressure;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrPhenomMaxWind()
/*     */   {
/* 362 */     return this.editableAttrPhenomMaxWind;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrPhenomMaxWind(String editableAttrPhenomMaxWind)
/*     */   {
/* 368 */     this.editableAttrPhenomMaxWind = editableAttrPhenomMaxWind;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrFreeText()
/*     */   {
/* 374 */     return this.editableAttrFreeText;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrFreeText(String editableAttrFreeText)
/*     */   {
/* 380 */     this.editableAttrFreeText = editableAttrFreeText;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrTrend()
/*     */   {
/* 386 */     return this.editableAttrTrend;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrTrend(String editableAttrTrend)
/*     */   {
/* 392 */     this.editableAttrTrend = editableAttrTrend;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrMovement()
/*     */   {
/* 398 */     return this.editableAttrMovement;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrMovement(String editableAttrMovement)
/*     */   {
/* 404 */     this.editableAttrMovement = editableAttrMovement;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrPhenomSpeed()
/*     */   {
/* 410 */     return this.editableAttrPhenomSpeed;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrPhenomSpeed(String editableAttrPhenomSpeed)
/*     */   {
/* 416 */     this.editableAttrPhenomSpeed = editableAttrPhenomSpeed;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrPhenomDirection()
/*     */   {
/* 422 */     return this.editableAttrPhenomDirection;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrPhenomDirection(String editableAttrPhenomDirection)
/*     */   {
/* 428 */     this.editableAttrPhenomDirection = editableAttrPhenomDirection;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrLevel()
/*     */   {
/* 434 */     return this.editableAttrLevel;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrLevel(String editableAttrLevel)
/*     */   {
/* 440 */     this.editableAttrLevel = editableAttrLevel;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrLevelInfo1()
/*     */   {
/* 446 */     return this.editableAttrLevelInfo1;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrLevelInfo1(String editableAttrLevelInfo1)
/*     */   {
/* 452 */     this.editableAttrLevelInfo1 = editableAttrLevelInfo1;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrLevelInfo2()
/*     */   {
/* 458 */     return this.editableAttrLevelInfo2;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrLevelInfo2(String editableAttrLevelInfo2)
/*     */   {
/* 464 */     this.editableAttrLevelInfo2 = editableAttrLevelInfo2;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrLevelText1()
/*     */   {
/* 470 */     return this.editableAttrLevelText1;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrLevelText1(String editableAttrLevelText1)
/*     */   {
/* 476 */     this.editableAttrLevelText1 = editableAttrLevelText1;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrLevelText2()
/*     */   {
/* 482 */     return this.editableAttrLevelText2;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrLevelText2(String editableAttrLevelText2)
/*     */   {
/* 488 */     this.editableAttrLevelText2 = editableAttrLevelText2;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrFir()
/*     */   {
/* 494 */     return this.editableAttrFir;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrFir(String editableAttrFir)
/*     */   {
/* 500 */     this.editableAttrFir = editableAttrFir;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet
 * JD-Core Version:    0.6.2
 */