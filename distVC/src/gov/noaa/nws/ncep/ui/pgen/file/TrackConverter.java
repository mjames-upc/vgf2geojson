/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ITrack.ExtraPointTimeDisplayOption;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.List;
/*     */ import javax.xml.datatype.DatatypeConfigurationException;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*     */ 
/*     */ public class TrackConverter
/*     */ {
/*     */   public static List<gov.noaa.nws.ncep.ui.pgen.elements.Track> getTrackElementListByTrackBeanList(List<Track> trackBeanList)
/*     */   {
/*  52 */     List trackElementList = new ArrayList(trackBeanList.size());
/*     */ 
/*  54 */     for (Track trackBean : trackBeanList) {
/*  55 */       gov.noaa.nws.ncep.ui.pgen.elements.Track trackElement = new gov.noaa.nws.ncep.ui.pgen.elements.Track();
/*     */ 
/*  57 */       trackElement.setInitialColor(getColorByColorTypeBean(trackBean.initialColor, true));
/*  58 */       trackElement.setExtrapColor(getColorByColorTypeBean(trackBean.extrapColor, false));
/*     */ 
/*  60 */       trackElement.setInitTrackPoints((gov.noaa.nws.ncep.ui.pgen.display.TrackPoint[])getTrackPointElementListByTrackPointBeanList(trackBean.getInitialPoints()).toArray(new gov.noaa.nws.ncep.ui.pgen.display.TrackPoint[trackBean.getInitialPoints().size()]));
/*  61 */       trackElement.setExtrapPoints((gov.noaa.nws.ncep.ui.pgen.display.TrackPoint[])getTrackPointElementListByTrackPointBeanList(trackBean.getExtrapPoints()).toArray(new gov.noaa.nws.ncep.ui.pgen.display.TrackPoint[trackBean.getExtrapPoints().size()]));
/*     */ 
/*  66 */       trackElement.setFirstTimeCalendar(getFirstOrSecondTimeCalendarByTrackBean(trackBean, true));
/*  67 */       trackElement.setSecondTimeCalendar(getFirstOrSecondTimeCalendarByTrackBean(trackBean, false));
/*     */ 
/*  75 */       trackElement.setLinePointsValue(trackElement.getInitialPoints(), trackElement.getExtrapPoints());
/*     */ 
/*  77 */       trackElement.setExtraPointTimeTextDisplayIndicator(getBooleanArrayByBooleanList(trackBean.extraPointTimeTextDisplayIndicator));
/*     */ 
/*  79 */       trackElement.setInitialLinePattern(trackBean.getInitialLinePattern());
/*  80 */       trackElement.setExtrapLinePattern(trackBean.getExtrapLinePattern());
/*  81 */       trackElement.setInitialMarker(trackBean.getInitialMarker());
/*  82 */       trackElement.setExtrapMarker(trackBean.getExtrapMarker());
/*  83 */       trackElement.setFontName(trackBean.getFontName());
/*  84 */       if (trackBean.getLineWidth() != null)
/*  85 */         trackElement.setLineWidth(trackBean.getLineWidth().floatValue());
/*     */       else
/*  87 */         trackElement.setLineWidth(1.0F);
/*  88 */       if (trackBean.getFontSize() != null)
/*  89 */         trackElement.setFontSize(trackBean.getFontSize().floatValue());
/*     */       else {
/*  91 */         trackElement.setFontSize(2.0F);
/*     */       }
/*  93 */       if (trackBean.getPgenCategory() == null)
/*  94 */         trackElement.setPgenCategory("Track");
/*     */       else {
/*  96 */         trackElement.setPgenCategory(trackBean.getPgenCategory());
/*     */       }
/*  98 */       if (trackBean.getPgenType() == null)
/*  99 */         trackElement.setPgenType("STORM_TRACK");
/*     */       else {
/* 101 */         trackElement.setPgenType(trackBean.getPgenType());
/*     */       }
/*     */ 
/* 106 */       gov.noaa.nws.ncep.ui.pgen.display.TrackPoint[] initPts = trackElement.getInitTrackPoints();
/* 107 */       int initPtsLength = initPts.length;
/* 108 */       Coordinate initPointBeforeLastInitPointCoordinate = initPts[(initPtsLength - 2)].getLocation();
/* 109 */       Coordinate lastInitPointCoordinate = initPts[(initPtsLength - 1)].getLocation();
/*     */ 
/* 111 */       GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/* 112 */       gc.setStartingGeographicPoint(initPointBeforeLastInitPointCoordinate.x, 
/* 113 */         initPointBeforeLastInitPointCoordinate.y);
/* 114 */       gc.setDestinationGeographicPoint(lastInitPointCoordinate.x, 
/* 115 */         lastInitPointCoordinate.y);
/* 116 */       double direction = gc.getAzimuth();
/*     */ 
/* 118 */       double distanceInMeter = gc.getOrthodromicDistance();
/* 119 */       long timeDifference = initPts[(initPtsLength - 1)].getTime().getTimeInMillis() - 
/* 120 */         initPts[(initPtsLength - 2)].getTime().getTimeInMillis();
/* 121 */       double speed = distanceInMeter / timeDifference;
/*     */ 
/* 123 */       trackElement.setDirectionForExtraPoints(direction);
/* 124 */       trackElement.setSpeed(speed);
/*     */ 
/* 129 */       trackElement.setSizeScale(1.0D);
/* 130 */       trackElement.setSmoothFactor(0);
/* 131 */       trackElement.setClosed(Boolean.valueOf(false));
/* 132 */       trackElement.setFilled(Boolean.valueOf(false));
/* 133 */       trackElement.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_0);
/*     */ 
/* 139 */       boolean setTimeButtonSelectedFlag = true;
/* 140 */       if (trackBean.isSetTimeButtonSelected() != null)
/* 141 */         setTimeButtonSelectedFlag = trackBean.isSetTimeButtonSelected().booleanValue();
/* 142 */       trackElement.setSetTimeButtonSelected(setTimeButtonSelectedFlag);
/*     */ 
/* 144 */       int intervalComboSelectedIndexValue = 0;
/* 145 */       if (trackBean.getIntervalComboSelectedIndex() != null)
/* 146 */         intervalComboSelectedIndexValue = trackBean.getIntervalComboSelectedIndex().intValue();
/* 147 */       trackElement.setIntervalComboSelectedIndex(intervalComboSelectedIndexValue);
/*     */ 
/* 149 */       trackElement.setIntervalTimeString(trackBean.getIntervalTimeTextString());
/*     */ 
/* 151 */       String extraPointTimeDisplayOptionName = ITrack.ExtraPointTimeDisplayOption.SKIP_FACTOR.name();
/* 152 */       if (trackBean.getExtraPointTimeDisplayOptionName() != null)
/* 153 */         extraPointTimeDisplayOptionName = trackBean.getExtraPointTimeDisplayOptionName();
/* 154 */       trackElement.setExtraPointTimeDisplayOption(ITrack.ExtraPointTimeDisplayOption.valueOf(extraPointTimeDisplayOptionName));
/*     */ 
/* 156 */       String skipFactorTextString = "0";
/* 157 */       if (trackBean.getSkipFactorTextString() != null)
/* 158 */         skipFactorTextString = trackBean.getSkipFactorTextString();
/* 159 */       trackElement.setSkipFactorTextString(skipFactorTextString);
/*     */ 
/* 161 */       int fontNameComboSelectedIndex = 0;
/* 162 */       if (trackBean.getFontNameComboSelectedIndex() != null)
/* 163 */         fontNameComboSelectedIndex = trackBean.getFontNameComboSelectedIndex().intValue();
/* 164 */       trackElement.setFontNameComboSelectedIndex(fontNameComboSelectedIndex);
/*     */ 
/* 166 */       int fontSizeComboSelectedIndex = 0;
/* 167 */       if (trackBean.getFontSizeComboSelectedIndex() != null)
/* 168 */         fontSizeComboSelectedIndex = trackBean.getFontSizeComboSelectedIndex().intValue();
/* 169 */       trackElement.setFontSizeComboSelectedIndex(fontSizeComboSelectedIndex);
/*     */ 
/* 171 */       int fontStyleComboSelectedIndex = 0;
/* 172 */       if (trackBean.getFontStyleComboSelectedIndex() != null)
/* 173 */         fontStyleComboSelectedIndex = trackBean.getFontStyleComboSelectedIndex().intValue();
/* 174 */       trackElement.setFontStyleComboSelectedIndex(fontStyleComboSelectedIndex);
/*     */ 
/* 176 */       trackElementList.add(trackElement);
/*     */     }
/*     */ 
/* 179 */     return trackElementList;
/*     */   }
/*     */ 
/*     */   public static Track getTrackBeanByTrackElement(gov.noaa.nws.ncep.ui.pgen.elements.Track trackElement)
/*     */   {
/* 184 */     Track trackBean = new Track();
/*     */ 
/* 186 */     trackBean.setInitialColor(getColorTypeBeanByColorElement(trackElement.getInitialColor()));
/* 187 */     trackBean.setExtrapColor(getColorTypeBeanByColorElement(trackElement.getExtrapColor()));
/*     */ 
/* 189 */     if (trackElement.getInitialPoints() != null) {
/* 190 */       for (gov.noaa.nws.ncep.ui.pgen.display.TrackPoint currentTrackPoint : trackElement.getInitialPoints()) {
/* 191 */         trackBean.getInitialPoints().add(getTrackPointBeanByTrackPointElement(currentTrackPoint));
/*     */       }
/*     */     }
/* 194 */     if (trackElement.getExtrapPoints() != null) {
/* 195 */       for (gov.noaa.nws.ncep.ui.pgen.display.TrackPoint currentTrackPoint : trackElement.getExtrapPoints()) {
/* 196 */         trackBean.getExtrapPoints().add(getTrackPointBeanByTrackPointElement(currentTrackPoint));
/*     */       }
/*     */     }
/*     */ 
/* 200 */     trackBean.getExtraPointTimeTextDisplayIndicator().addAll(getBooleanObjectList(trackElement.getExtraPointTimeTextDisplayIndicator()));
/*     */ 
/* 202 */     trackBean.setInitialLinePattern(trackElement.getInitialLinePattern());
/* 203 */     trackBean.setExtrapLinePattern(trackElement.getExtrapLinePattern());
/* 204 */     trackBean.setInitialMarker(trackElement.getInitialMarker());
/* 205 */     trackBean.setExtrapMarker(trackElement.getExtrapMarker());
/* 206 */     trackBean.setFontName(trackElement.getFontName());
/* 207 */     trackBean.setFontSize(new Float(trackElement.getFontSize()));
/* 208 */     trackBean.setLineWidth(new Float(trackElement.getLineWidth()));
/*     */ 
/* 210 */     trackBean.setPgenCategory(trackElement.getPgenCategory());
/* 211 */     trackBean.setPgenType(trackElement.getPgenType());
/*     */ 
/* 218 */     trackBean.setSetTimeButtonSelected(new Boolean(trackElement.isSetTimeButtonSelected()));
/* 219 */     trackBean.setIntervalComboSelectedIndex(new Integer(trackElement.getIntervalComboSelectedIndex()));
/* 220 */     trackBean.setIntervalTimeTextString(trackElement.getIntervalTimeString());
/* 221 */     trackBean.setExtraPointTimeDisplayOptionName(trackElement.getExtraPointTimeDisplayOption().name());
/* 222 */     trackBean.setSkipFactorTextString(trackElement.getSkipFactorTextString());
/* 223 */     trackBean.setFontNameComboSelectedIndex(new Integer(trackElement.getFontNameComboSelectedIndex()));
/* 224 */     trackBean.setFontSizeComboSelectedIndex(new Integer(trackElement.getFontSizeComboSelectedIndex()));
/* 225 */     trackBean.setFontStyleComboSelectedIndex(new Integer(trackElement.getFontStyleComboSelectedIndex()));
/*     */ 
/* 227 */     return trackBean;
/*     */   }
/*     */ 
/*     */   private static Calendar getFirstOrSecondTimeCalendarByTrackBean(Track trackBean, boolean isFirstTimeCalendar)
/*     */   {
/* 234 */     int indexOffSet = 1;
/* 235 */     if (isFirstTimeCalendar)
/* 236 */       indexOffSet++;
/* 237 */     List trackPointElementList = getTrackPointElementListByTrackPointBeanList(trackBean.getInitialPoints());
/* 238 */     if ((trackPointElementList == null) || (trackPointElementList.size() < 2))
/*     */     {
/* 240 */       return null;
/*     */     }
/* 242 */     int listSize = trackPointElementList.size();
/* 243 */     return ((gov.noaa.nws.ncep.ui.pgen.display.TrackPoint)trackPointElementList.get(listSize - indexOffSet)).getTime();
/*     */   }
/*     */ 
/*     */   private static java.awt.Color getColorByColorTypeBean(ColorType colorTypeBean, boolean isInitColor)
/*     */   {
/* 248 */     if ((colorTypeBean == null) || (colorTypeBean.getColor() == null)) {
/* 249 */       if (isInitColor) {
/* 250 */         return new java.awt.Color(0, 0, 255);
/*     */       }
/* 252 */       return new java.awt.Color(0, 192, 0);
/*     */     }
/* 254 */     Color colorBean = colorTypeBean.getColor();
/* 255 */     return new java.awt.Color(colorBean.getRed(), colorBean.getGreen(), colorBean.getBlue(), 
/* 256 */       colorBean.getAlpha().intValue());
/*     */   }
/*     */ 
/*     */   private static List<gov.noaa.nws.ncep.ui.pgen.display.TrackPoint> getTrackPointElementListByTrackPointBeanList(List<TrackPoint> trackPointBeanList) {
/* 260 */     List trackPointElementList = new ArrayList(trackPointBeanList.size());
/* 261 */     for (TrackPoint trackPointBean : trackPointBeanList) {
/* 262 */       Calendar currentCalendar = null;
/* 263 */       if (trackPointBean.getTime() != null) {
/* 264 */         currentCalendar = trackPointBean.getTime().toGregorianCalendar();
/*     */       }
/* 266 */       Coordinate currentCoordinate = getCoordinateByTrackPointBean(trackPointBean);
/* 267 */       gov.noaa.nws.ncep.ui.pgen.display.TrackPoint currentTrackPointElement = new gov.noaa.nws.ncep.ui.pgen.display.TrackPoint(currentCoordinate, currentCalendar);
/* 268 */       trackPointElementList.add(currentTrackPointElement);
/*     */     }
/* 270 */     return trackPointElementList;
/*     */   }
/*     */ 
/*     */   private static Coordinate getCoordinateByTrackPointBean(TrackPoint trackPointBean) {
/* 274 */     Coordinate coordinate = new Coordinate();
/* 275 */     if (trackPointBean.getLocation() != null) {
/* 276 */       coordinate.x = trackPointBean.getLocation().getLongitude().doubleValue();
/* 277 */       coordinate.y = trackPointBean.getLocation().getLatitude().doubleValue();
/*     */     }
/* 279 */     return coordinate;
/*     */   }
/*     */ 
/*     */   private static boolean[] getBooleanArrayByBooleanList(List<Boolean> booleanList) {
/* 283 */     boolean[] booleanArray = new boolean[booleanList.size()];
/* 284 */     int arrayIndex = 0;
/* 285 */     for (Boolean currentBoolean : booleanList) {
/* 286 */       booleanArray[(arrayIndex++)] = currentBoolean.booleanValue();
/*     */     }
/* 288 */     return booleanArray;
/*     */   }
/*     */ 
/*     */   private static ColorType getColorTypeBeanByColorElement(java.awt.Color colorElement)
/*     */   {
/* 293 */     ColorType colorTypeBean = new ColorType();
/* 294 */     Color colorBean = new Color();
/* 295 */     colorBean.setAlpha(Integer.valueOf(colorElement.getAlpha()));
/* 296 */     colorBean.setBlue(colorElement.getBlue());
/* 297 */     colorBean.setRed(colorElement.getRed());
/* 298 */     colorBean.setGreen(colorElement.getGreen());
/* 299 */     colorTypeBean.setColor(colorBean);
/* 300 */     return colorTypeBean;
/*     */   }
/*     */ 
/*     */   private static TrackPoint getTrackPointBeanByTrackPointElement(gov.noaa.nws.ncep.ui.pgen.display.TrackPoint trackPointElement) {
/* 304 */     TrackPoint trackPointBean = new TrackPoint();
/*     */ 
/* 306 */     if (trackPointElement.getTime() != null) {
/* 307 */       GregorianCalendar gregorianCalendar = new GregorianCalendar();
/* 308 */       gregorianCalendar.setTimeInMillis(trackPointElement.getTime().getTimeInMillis());
/*     */       try {
/* 310 */         XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
/* 311 */         trackPointBean.setTime(xmlGregorianCalendar);
/*     */       }
/*     */       catch (DatatypeConfigurationException localDatatypeConfigurationException) {
/*     */       }
/*     */     }
/* 316 */     if (trackPointElement.getLocation() != null) {
/* 317 */       trackPointBean.location = new TrackPoint.Location();
/* 318 */       trackPointBean.getLocation().setLongitude(Double.valueOf(trackPointElement.getLocation().x));
/* 319 */       trackPointBean.getLocation().setLatitude(Double.valueOf(trackPointElement.getLocation().y));
/*     */     }
/* 321 */     return trackPointBean;
/*     */   }
/*     */ 
/*     */   private static List<Boolean> getBooleanObjectList(boolean[] booleanArray) {
/* 325 */     List booleanList = null;
/* 326 */     if (booleanArray == null)
/* 327 */       booleanList = new ArrayList();
/*     */     else
/* 329 */       booleanList = new ArrayList(booleanArray.length);
/* 330 */     boolean[] arrayOfBoolean = booleanArray; int j = booleanArray.length; for (int i = 0; i < j; i++) { boolean booleanValue = arrayOfBoolean[i];
/* 331 */       Boolean booleanObject = new Boolean(booleanValue);
/* 332 */       booleanList.add(booleanObject);
/*     */     }
/* 334 */     return booleanList;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.TrackConverter
 * JD-Core Version:    0.6.2
 */