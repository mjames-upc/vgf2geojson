/*      */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*      */ 
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.ITrack;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.ITrack.ExtraPointTimeDisplayOption;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.TrackPoint;
/*      */ import java.awt.Color;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import org.geotools.referencing.GeodeticCalculator;
/*      */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*      */ 
/*      */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE})
/*      */ public class Track extends Line
/*      */   implements ITrack
/*      */ {
/*      */   private static final float DEFAULT_FONT_SIZE = 14.0F;
/*      */   private static final float DEFAULT_LINE_WIDTH = 0.1F;
/*      */   private static final int DEFAULT_EXTRA_POINT_NUMBER = 5;
/*      */   private static final String INTERVAL_TIME_FORMAT_PATTERN = "HH:mm";
/*      */   private static final String INITIAL_MARKER = "FILLED_DIAMOND";
/*      */   private static final String EXTRAP_MARKER = "FILLED_TRIANGLE";
/*      */   private static final String INITIAL_LINE_PATTERN = "LINE_SOLID";
/*      */   private static final String EXTRAP_LINE_PATTERN = "LINE_SOLID";
/*      */   public static final String TRACK_PGEN_CATEGORY = "Track";
/*      */   public static final String TRACK_INFO_DLG_CATEGORY_NAME = "TRACK_EXTRA_POINTS_INFO";
/*      */   public static final String TRACK_PGEN_TYPE = "STORM_TRACK";
/*      */   private Color initialColor;
/*      */   private Color extrapColor;
/*      */   private String initialMarker;
/*      */   private String initialLinePattern;
/*      */   private String extrapMarker;
/*      */   private String extrapLinePattern;
/*      */   private TrackPoint[] initTrackPoints;
/*      */   private TrackPoint[] extrapPoints;
/*      */   private String fontName;
/*      */   private float fontSize;
/*      */   private IText.FontStyle fontStyle;
/*      */   private float lineWidth;
/*      */   private int extraDrawingPointNumber;
/*      */   private boolean setTimeButtonSelected;
/*      */   private Calendar firstTimeCalendar;
/*      */   private Calendar secondTimeCalendar;
/*      */   private String intervalTimeString;
/*      */   private int intervalComboSelectedIndex;
/*      */   private int fontSizeComboSelectedIndex;
/*      */   private int fontNameComboSelectedIndex;
/*      */   private int fontStyleComboSelectedIndex;
/*      */   private int elapsedHourForExtraPoint;
/*      */   private int elapsedMinuteForExtraPoint;
/*      */   private ITrack.ExtraPointTimeDisplayOption extraPointTimeDisplayOption;
/*      */   private String skipFactorTextString;
/*      */   private boolean[] extraPointTimeTextDisplayIndicator;
/*      */   private double directionForExtraPoints;
/*      */   private boolean roundDirBtnSelected;
/*      */   private int roundDirComboSelectedIndex;
/*      */   private double speed;
/*      */   private double speedInKnotOverHour;
/*      */   private double speedInKilometerOverHour;
/*      */   private double speedInMileOverHour;
/*      */   private boolean roundBtnSelected;
/*      */   private int unitComboSelectedIndex;
/*      */   private int roundComboSelectedIndex;
/*      */ 
/*      */   public void setExtrapMarker(String extrapMarker)
/*      */   {
/*   61 */     this.extrapMarker = extrapMarker;
/*      */   }
/*      */ 
/*      */   public void setExtrapLinePattern(String extrapLinePattern) {
/*   65 */     this.extrapLinePattern = extrapLinePattern;
/*      */   }
/*      */ 
/*      */   public Track()
/*      */   {
/*      */   }
/*      */ 
/*      */   public Track(ArrayList<Coordinate> _locations, Calendar _firstTimeCalendar, Calendar _secondTimeCalendar)
/*      */   {
/*  132 */     initializeInitTrackPoints(_locations, 
/*  133 */       _firstTimeCalendar, _secondTimeCalendar);
/*      */   }
/*      */ 
/*      */   public void initializeInitTrackPoints(ArrayList<Coordinate> locations, Calendar firstTimeCalendar, Calendar secondTimeCalendar)
/*      */   {
/*  139 */     initializeInitTrackPoints(locations);
/*  140 */     initializeInitFirstTimeCalendar(firstTimeCalendar);
/*  141 */     initializeInitSecondTimeCalendar(secondTimeCalendar);
/*      */   }
/*      */ 
/*      */   public void initializeTrackByTrackAttrDlgAndLocationList(ITrack trackAttrDlgObject, ArrayList<Coordinate> locations) {
/*  145 */     initializeInitTrackPoints(locations);
/*  146 */     initializeInitFirstTimeCalendar(trackAttrDlgObject.getFirstTimeCalendar());
/*  147 */     initializeInitSecondTimeCalendar(trackAttrDlgObject.getSecondTimeCalendar());
/*      */ 
/*  149 */     setInitialMarker("");
/*  150 */     setSetTimeButtonSelected(trackAttrDlgObject.isSetTimeButtonSelected());
/*  151 */     setExtraDrawingPointNumber(trackAttrDlgObject.getExtraDrawingPointNumber());
/*      */ 
/*  153 */     setIntervalTimeString(getIntervalTimeTextStringValue(trackAttrDlgObject));
/*      */ 
/*  155 */     setFontName(trackAttrDlgObject.getFontName());
/*  156 */     setFontStyle(trackAttrDlgObject.getStyle());
/*  157 */     setFontSize(trackAttrDlgObject.getFontSize());
/*  158 */     setInitialColor(trackAttrDlgObject.getInitialColor());
/*  159 */     setExtrapColor(trackAttrDlgObject.getExtrapColor());
/*  160 */     setExtraPointTimeDisplayOption(trackAttrDlgObject.getExtraPointTimeDisplayOption());
/*  161 */     setSkipFactorTextString(trackAttrDlgObject.getSkipFactorText());
/*  162 */     setFontNameComboSelectedIndex(trackAttrDlgObject.getFontNameComboSelectedIndex());
/*  163 */     setFontSizeComboSelectedIndex(trackAttrDlgObject.getFontSizeComboSelectedIndex());
/*  164 */     setFontStyleComboSelectedIndex(trackAttrDlgObject.getFontStyleComboSelectedIndex());
/*  165 */     setUnitComboSelectedIndex(trackAttrDlgObject.getUnitComboSelectedIndex());
/*  166 */     setRoundComboSelectedIndex(trackAttrDlgObject.getRoundComboSelectedIndex());
/*  167 */     if (trackAttrDlgObject.getRoundComboSelectedIndex() > 0)
/*  168 */       setRoundBtnSelected(true);
/*      */     else {
/*  170 */       setRoundBtnSelected(false);
/*      */     }
/*  172 */     setRoundDirComboSelectedIndex(trackAttrDlgObject.getRoundDirComboSelectedIndex());
/*  173 */     if (trackAttrDlgObject.getRoundDirComboSelectedIndex() > 0)
/*  174 */       setRoundDirBtnSelected(true);
/*      */     else {
/*  176 */       setRoundDirBtnSelected(false);
/*      */     }
/*      */ 
/*  179 */     calculateExtrapTrackPoints();
/*      */ 
/*  187 */     setPgenCategory("Track");
/*  188 */     setPgenType("STORM_TRACK");
/*      */   }
/*      */ 
/*      */   public void initializeInitTrackPoints(ArrayList<Coordinate> locations) {
/*  192 */     TrackPoint[] initTrackPointArray = new TrackPoint[locations.size()];
/*  193 */     int arrayIndex = 0;
/*      */ 
/*  195 */     for (Coordinate currentCoordinate : locations) {
/*  196 */       TrackPoint eachTrackPoint = new TrackPoint(currentCoordinate, null);
/*  197 */       initTrackPointArray[(arrayIndex++)] = eachTrackPoint;
/*      */     }
/*  199 */     setInitTrackPoints(initTrackPointArray);
/*      */   }
/*      */ 
/*      */   public void initializeInitFirstTimeCalendar(Calendar firstCalendar) {
/*  203 */     setFirstTimeCalendar(firstCalendar);
/*  204 */     if (getInitTrackPoints() != null) {
/*  205 */       int initTrackPointLength = getInitTrackPoints().length;
/*  206 */       if (initTrackPointLength >= 2)
/*  207 */         getInitTrackPoints()[(initTrackPointLength - 2)].setTime(firstCalendar);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void initializeInitSecondTimeCalendar(Calendar secondCalendar)
/*      */   {
/*  213 */     setSecondTimeCalendar(secondCalendar);
/*  214 */     if (getInitTrackPoints() != null) {
/*  215 */       int initTrackPointLength = getInitTrackPoints().length;
/*  216 */       if (initTrackPointLength >= 2)
/*  217 */         getInitTrackPoints()[(initTrackPointLength - 1)].setTime(secondCalendar);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void calculateExtrapTrackPoints()
/*      */   {
/*  223 */     this.extrapPoints = calculateExtrapTrackPoints(getInitialPoints(), getFirstTimeCalendar(), 
/*  224 */       getSecondTimeCalendar(), getExtraDrawingPointNumber(), 
/*  225 */       getElapsedHourForExtraPoint(), getElapsedMinuteForExtraPoint(), 
/*  226 */       getExtraPointTimeDisplayOption(), getSkipFactorTextString());
/*      */ 
/*  232 */     setLinePointsValue(getInitialPoints(), getExtrapPoints());
/*      */ 
/*  238 */     setPgenCategory("Track");
/*  239 */     setPgenType("STORM_TRACK");
/*      */   }
/*      */ 
/*      */   public void setLinePointsValue(TrackPoint[] initTrackPoints, TrackPoint[] extrapPoints) {
/*  243 */     int listSize = 1;
/*  244 */     if (initTrackPoints != null)
/*  245 */       listSize += initTrackPoints.length;
/*  246 */     if (extrapPoints != null)
/*  247 */       listSize += extrapPoints.length;
/*  248 */     ArrayList coordinatePointList = new ArrayList(listSize);
/*  249 */     addArrayToArrayList(coordinatePointList, initTrackPoints);
/*  250 */     addArrayToArrayList(coordinatePointList, extrapPoints);
/*      */ 
/*  255 */     setLinePoints(coordinatePointList);
/*      */   }
/*      */ 
/*      */   public Track copy()
/*      */   {
/*  269 */     Track newTrack = new Track();
/*  270 */     Calendar newFirstTimeCalendar = Calendar.getInstance();
/*  271 */     newFirstTimeCalendar.setTimeInMillis(getFirstTimeCalendar().getTimeInMillis());
/*  272 */     newTrack.setFirstTimeCalendar(newFirstTimeCalendar);
/*      */ 
/*  275 */     Calendar newSecondTimeCalendar = Calendar.getInstance();
/*  276 */     newSecondTimeCalendar.setTimeInMillis(getSecondTimeCalendar().getTimeInMillis());
/*  277 */     newTrack.setSecondTimeCalendar(newSecondTimeCalendar);
/*      */ 
/*  280 */     TrackPoint[] newInitTrackPoints = new TrackPoint[getInitTrackPoints().length];
/*      */ 
/*  282 */     for (int i = 0; i < newInitTrackPoints.length; i++)
/*      */     {
/*  289 */       newInitTrackPoints[i] = TrackPoint.clone(getInitTrackPoints()[i].getLocation(), getInitTrackPoints()[i].getTime());
/*      */     }
/*  291 */     newTrack.setInitTrackPoints(newInitTrackPoints);
/*      */ 
/*  302 */     newTrack.setFontStyle(getFontStyle());
/*  303 */     newTrack.setFontSize(getFontSize());
/*  304 */     newTrack.setFontName(new String(getFontName()));
/*  305 */     newTrack.setExtraPointTimeDisplayOption(getExtraPointTimeDisplayOption());
/*      */ 
/*  307 */     boolean[] newExtraPointTimeTextDisplayIndicator = new boolean[getExtraPointTimeTextDisplayIndicator().length];
/*  308 */     for (int i = 0; i < getExtraPointTimeTextDisplayIndicator().length; i++) {
/*  309 */       newExtraPointTimeTextDisplayIndicator[i] = getExtraPointTimeTextDisplayIndicator()[i];
/*      */     }
/*  311 */     newTrack.setExtraPointTimeTextDisplayIndicator(newExtraPointTimeTextDisplayIndicator);
/*      */ 
/*  314 */     newTrack.setSkipFactorTextString(new String(getSkipFactorTextString()));
/*  315 */     newTrack.setInitialColor(new Color(getInitialColor().getRed(), getInitialColor().getGreen(), 
/*  316 */       getInitialColor().getBlue()));
/*  317 */     newTrack.setExtrapColor(new Color(getExtrapColor().getRed(), getExtrapColor().getGreen(), 
/*  318 */       getExtrapColor().getBlue()));
/*      */ 
/*  320 */     newTrack.setIntervalTimeString(new String(getIntervalTimeString()));
/*      */ 
/*  326 */     if (getExtrapPoints() != null)
/*  327 */       newTrack.setExtraDrawingPointNumber(getExtrapPoints().length);
/*      */     else {
/*  329 */       newTrack.setExtraDrawingPointNumber(getExtraDrawingPointNumber());
/*      */     }
/*  331 */     newTrack.setExtrapLinePattern(new String(getExtrapLinePattern()));
/*  332 */     newTrack.setExtrapMarker(new String(getExtrapMarker()));
/*      */ 
/*  334 */     newTrack.setInitialLinePattern(new String(getInitialLinePattern()));
/*  335 */     newTrack.setInitialMarker(new String(getInitialMarker()));
/*      */ 
/*  337 */     if (getPgenCategory() != null)
/*  338 */       newTrack.setPgenCategory(new String(getPgenCategory()));
/*  339 */     if (getPgenType() != null) {
/*  340 */       newTrack.setPgenType(new String(getPgenType()));
/*      */     }
/*  342 */     newTrack.calculateExtrapTrackPoints();
/*      */ 
/*  347 */     newTrack.setClosed(isClosedLine());
/*  348 */     newTrack.setFilled(isFilled());
/*      */ 
/*  353 */     Color[] initColors = new Color[1];
/*  354 */     initColors[0] = getInitialColor();
/*  355 */     newTrack.setColors(initColors);
/*  356 */     newTrack.setLineWidth(getLineWidth());
/*  357 */     newTrack.setSizeScale(getSizeScale());
/*      */ 
/*  359 */     newTrack.setSmoothFactor(2);
/*  360 */     newTrack.setFillPattern(getFillPattern());
/*      */ 
/*  362 */     newTrack.setParent(getParent());
/*      */ 
/*  365 */     return newTrack;
/*      */   }
/*      */ 
/*      */   public void update(ITrack trackAttrDlg)
/*      */   {
/*  370 */     initializeInitFirstTimeCalendar(trackAttrDlg.getFirstTimeCalendar());
/*  371 */     initializeInitSecondTimeCalendar(trackAttrDlg.getSecondTimeCalendar());
/*      */ 
/*  373 */     setInitialMarker("FILLED_DIAMOND");
/*  374 */     setSetTimeButtonSelected(trackAttrDlg.isSetTimeButtonSelected());
/*  375 */     setExtraDrawingPointNumber(trackAttrDlg.getExtraDrawingPointNumber());
/*  376 */     setIntervalTimeString(trackAttrDlg.getIntervalTimeString());
/*  377 */     setFontName(trackAttrDlg.getFontName());
/*  378 */     setFontStyle(trackAttrDlg.getStyle());
/*  379 */     setFontSize(trackAttrDlg.getFontSize());
/*  380 */     setInitialColor(trackAttrDlg.getInitialColor());
/*  381 */     setExtrapColor(trackAttrDlg.getExtrapColor());
/*  382 */     setExtraPointTimeDisplayOption(trackAttrDlg.getExtraPointTimeDisplayOption());
/*  383 */     setSkipFactorTextString(trackAttrDlg.getSkipFactorText());
/*  384 */     setFontNameComboSelectedIndex(trackAttrDlg.getFontNameComboSelectedIndex());
/*  385 */     setFontSizeComboSelectedIndex(trackAttrDlg.getFontSizeComboSelectedIndex());
/*  386 */     setFontStyleComboSelectedIndex(trackAttrDlg.getFontStyleComboSelectedIndex());
/*  387 */     setUnitComboSelectedIndex(trackAttrDlg.getUnitComboSelectedIndex());
/*  388 */     setRoundComboSelectedIndex(trackAttrDlg.getRoundComboSelectedIndex());
/*  389 */     if (trackAttrDlg.getRoundComboSelectedIndex() > 0)
/*  390 */       setRoundBtnSelected(true);
/*      */     else {
/*  392 */       setRoundBtnSelected(false);
/*      */     }
/*  394 */     setRoundDirComboSelectedIndex(trackAttrDlg.getRoundDirComboSelectedIndex());
/*  395 */     if (trackAttrDlg.getRoundDirComboSelectedIndex() > 0)
/*  396 */       setRoundDirBtnSelected(true);
/*      */     else {
/*  398 */       setRoundDirBtnSelected(false);
/*      */     }
/*      */ 
/*  401 */     calculateExtrapTrackPoints();
/*      */ 
/*  409 */     setPgenType("STORM_TRACK");
/*  410 */     setPgenCategory("Track");
/*      */   }
/*      */ 
/*      */   public void addPoint(int index, Coordinate point) {
/*  414 */     ArrayList allLinePoints = getPoints();
/*  415 */     if (allLinePoints == null) {
/*  416 */       allLinePoints = new ArrayList();
/*      */     }
/*  418 */     if (allLinePoints.size() <= index)
/*  419 */       allLinePoints.add(point);
/*      */     else {
/*  421 */       allLinePoints.add(index, point);
/*      */     }
/*  423 */     int initialPointSize = getInitialPoints().length;
/*  424 */     int extrapPointSize = getExtrapPoints().length;
/*      */ 
/*  426 */     if (index < initialPointSize) {
/*  427 */       TrackPoint modifiedTrackPoint = getInitialPoints()[index];
/*  428 */       modifiedTrackPoint.setLocation(point);
/*      */     } else {
/*  430 */       int extrapindex = index - initialPointSize;
/*  431 */       if ((extrapindex >= 0) && (extrapindex < extrapPointSize)) {
/*  432 */         TrackPoint modifiedTrackPoint = getExtrapPoints()[extrapindex];
/*  433 */         modifiedTrackPoint.setLocation(point);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removePoint(int index)
/*      */   {
/*  440 */     ArrayList allLinePoints = getPoints();
/*  441 */     if (allLinePoints == null) {
/*  442 */       return;
/*      */     }
/*  444 */     if (allLinePoints.size() > index)
/*  445 */       allLinePoints.remove(index);
/*      */   }
/*      */ 
/*      */   public void setPoints(ArrayList<Coordinate> pts)
/*      */   {
/*  453 */     if (pts == null) {
/*  454 */       return;
/*      */     }
/*      */ 
/*  458 */     super.setPoints(pts);
/*      */ 
/*  462 */     int allPointsSize = pts.size();
/*      */ 
/*  464 */     int initTrackPointSize = 0;
/*  465 */     if (getInitTrackPoints() != null)
/*  466 */       initTrackPointSize = getInitTrackPoints().length;
/*  467 */     int index = 0;
/*      */ 
/*  472 */     while ((index < initTrackPointSize) && (index < allPointsSize)) {
/*  473 */       TrackPoint currentTrackPoint = getInitTrackPoints()[index];
/*  474 */       if (currentTrackPoint != null)
/*  475 */         currentTrackPoint.setLocation((Coordinate)pts.get(index));
/*  476 */       index++;
/*      */     }
/*      */ 
/*  482 */     int extrapTrackPointSize = 0;
/*  483 */     if (getExtrapPoints() != null)
/*  484 */       extrapTrackPointSize = getExtrapPoints().length;
/*  485 */     int extrapIndex = 0;
/*  486 */     while ((extrapIndex < extrapTrackPointSize) && (
/*  487 */       index < allPointsSize)) {
/*  488 */       TrackPoint currentTrackPoint = getExtrapPoints()[extrapIndex];
/*  489 */       if (currentTrackPoint != null)
/*  490 */         currentTrackPoint.setLocation((Coordinate)pts.get(index));
/*  491 */       index++;
/*  492 */       extrapIndex++;
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getIntervalTimeTextStringValue(ITrack trackAttrDlg)
/*      */   {
/*  505 */     return trackAttrDlg.getIntervalTimeString();
/*      */   }
/*      */ 
/*      */   private void addArrayToArrayList(ArrayList<Coordinate> coordinatePointList, TrackPoint[] trackPoints) {
/*  509 */     if (trackPoints == null)
/*  510 */       return;
/*  511 */     for (TrackPoint trackPoint : trackPoints)
/*  512 */       coordinatePointList.add(trackPoint.getLocation());
/*      */   }
/*      */ 
/*      */   private TrackPoint[] calculateExtrapTrackPoints(TrackPoint[] initialPoints, Calendar initPointBeforeLastInitPointTimeCal, Calendar lastInitPointTimeCal, int extraDrawingPointNumber, int elapsedHourForExtraPointValue, int elapsedMinuteForExtraPointValue, ITrack.ExtraPointTimeDisplayOption extraPointTimeDisplayOption, String skipFactorTextString)
/*      */   {
/*  524 */     if ((initialPoints == null) || (initialPoints.length < 2))
/*      */     {
/*  526 */       return null;
/*      */     }
/*      */ 
/*  529 */     int arrayLength = initialPoints.length;
/*  530 */     Coordinate initPointBeforeLastInitPointCoordinate = initialPoints[(arrayLength - 2)].getLocation();
/*  531 */     Coordinate lastInitPointCoordinate = initialPoints[(arrayLength - 1)].getLocation();
/*  532 */     if ((!isCoordinateValid(initPointBeforeLastInitPointCoordinate)) || 
/*  533 */       (!isCoordinateValid(lastInitPointCoordinate)) || 
/*  534 */       (initPointBeforeLastInitPointTimeCal == null) || 
/*  535 */       (lastInitPointTimeCal == null))
/*      */     {
/*  538 */       return null;
/*      */     }
/*      */ 
/*  541 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*  542 */     gc.setStartingGeographicPoint(initPointBeforeLastInitPointCoordinate.x, 
/*  543 */       initPointBeforeLastInitPointCoordinate.y);
/*  544 */     gc.setDestinationGeographicPoint(lastInitPointCoordinate.x, 
/*  545 */       lastInitPointCoordinate.y);
/*  546 */     double direction = gc.getAzimuth();
/*  547 */     setDirectionForExtraPoints(direction);
/*      */ 
/*  549 */     double distanceInMeter = gc.getOrthodromicDistance();
/*  550 */     long timeDifference = getTimeDifferenceInMillisecond(initPointBeforeLastInitPointTimeCal, 
/*  551 */       lastInitPointTimeCal);
/*  552 */     double speed = distanceInMeter / timeDifference;
/*  553 */     setSpeed(speed);
/*      */ 
/*  556 */     Calendar firstExtraPointTimeCal = getTimeElapsedCalendarForFirstExtraPoint(lastInitPointTimeCal, 
/*  557 */       elapsedHourForExtraPointValue, 
/*  558 */       elapsedMinuteForExtraPointValue);
/*      */ 
/*  561 */     double distanceBetweenLastInitPointAndFirstExtraPoint = calculateDistanceBetweenLastInitPointAndFirstExtraPoint(speed, 
/*  562 */       lastInitPointTimeCal, firstExtraPointTimeCal);
/*      */ 
/*  565 */     double distanceForExtraPoint = calculateDistanceForExtraPoints(speed, 
/*  566 */       firstExtraPointTimeCal, elapsedHourForExtraPointValue, 
/*  567 */       elapsedMinuteForExtraPointValue);
/*      */ 
/*  569 */     TrackPoint[] extrapTrackPointArray = calculateExtrapTrackPoints(gc, 
/*  570 */       lastInitPointCoordinate, direction, 
/*  571 */       distanceBetweenLastInitPointAndFirstExtraPoint, 
/*  572 */       distanceForExtraPoint, 
/*  573 */       extraDrawingPointNumber, lastInitPointTimeCal, 
/*  574 */       firstExtraPointTimeCal, 
/*  575 */       elapsedHourForExtraPointValue, 
/*  576 */       elapsedMinuteForExtraPointValue);
/*  577 */     disableSomeTimeTagsDisplayBasedOnExtraPointTimeDisplayOption(extrapTrackPointArray, 
/*  578 */       extraPointTimeDisplayOption, skipFactorTextString);
/*      */ 
/*  580 */     return extrapTrackPointArray;
/*      */   }
/*      */ 
/*      */   private void disableSomeTimeTagsDisplayBasedOnExtraPointTimeDisplayOption(TrackPoint[] extrapTrackPointArray, ITrack.ExtraPointTimeDisplayOption extraPointTimeDisplayOption, String skipFactorTextString)
/*      */   {
/*  585 */     if (extrapTrackPointArray == null)
/*  586 */       return;
/*  587 */     boolean[] extraPointTimeTagFlagArray = new boolean[extrapTrackPointArray.length];
/*  588 */     initializeBooleanArray(extraPointTimeTagFlagArray, true);
/*      */ 
/*  590 */     if (extraPointTimeDisplayOption == ITrack.ExtraPointTimeDisplayOption.SKIP_FACTOR)
/*  591 */       removeTimeTagsBasedOnSkipFactor(extrapTrackPointArray, skipFactorTextString, extraPointTimeTagFlagArray);
/*  592 */     else if (extraPointTimeDisplayOption == ITrack.ExtraPointTimeDisplayOption.SHOW_FIRST_LAST)
/*  593 */       removeTimeTagsBasedOnShowLastFirstOnly(extrapTrackPointArray, extraPointTimeTagFlagArray);
/*  594 */     else if (extraPointTimeDisplayOption == ITrack.ExtraPointTimeDisplayOption.ON_ONE_HOUR)
/*  595 */       removeTimeTagsBasedOnHourMinuteValue(extrapTrackPointArray, true, extraPointTimeTagFlagArray);
/*  596 */     else if (extraPointTimeDisplayOption == ITrack.ExtraPointTimeDisplayOption.ON_HALF_HOUR) {
/*  597 */       removeTimeTagsBasedOnHourMinuteValue(extrapTrackPointArray, false, extraPointTimeTagFlagArray);
/*      */     }
/*      */     else {
/*  600 */       removeTimeTagsBasedOnHourMinuteValue(extrapTrackPointArray, false, extraPointTimeTagFlagArray);
/*      */     }
/*      */ 
/*  608 */     setExtraPointTimeTextDisplayIndicator(extraPointTimeTagFlagArray);
/*      */   }
/*      */ 
/*      */   private void removeTimeTagsBasedOnShowLastFirstOnly(TrackPoint[] extrapTrackPointArray, boolean[] timeTagIndicatorArray) {
/*  612 */     if (extrapTrackPointArray.length < 3)
/*  613 */       return;
/*  614 */     for (int i = 1; i < extrapTrackPointArray.length - 1; i++)
/*  615 */       timeTagIndicatorArray[i] = false;
/*      */   }
/*      */ 
/*      */   private void removeTimeTagsBasedOnSkipFactor(TrackPoint[] extrapTrackPointArray, String skipFactorTextString, boolean[] timeTagIndicatorArray)
/*      */   {
/*  621 */     if (skipFactorTextString == null) {
/*  622 */       return;
/*      */     }
/*      */ 
/*  626 */     if (extrapTrackPointArray.length < 3) {
/*  627 */       return;
/*      */     }
/*  629 */     int skipFactorIntValue = 0;
/*      */     try {
/*  631 */       skipFactorIntValue = Integer.parseInt(skipFactorTextString);
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/*      */     }
/*      */ 
/*  643 */     if ((skipFactorIntValue <= 0) || (skipFactorIntValue > extrapTrackPointArray.length - 2)) {
/*  644 */       return;
/*      */     }
/*  646 */     for (int i = 1; i <= skipFactorIntValue; i++)
/*  647 */       timeTagIndicatorArray[i] = false;
/*      */   }
/*      */ 
/*      */   private void removeTimeTagsBasedOnHourMinuteValue(TrackPoint[] extrapTrackPointArray, boolean isExactHourDisplayed, boolean[] timeTagIndicatorArray)
/*      */   {
/*  653 */     for (int i = 0; i < extrapTrackPointArray.length; i++) {
/*  654 */       TrackPoint targetTrackPoint = extrapTrackPointArray[i];
/*  655 */       Calendar targetPointTimeCal = targetTrackPoint.getTime();
/*      */ 
/*  657 */       if (!isTimeTagDisplayable(targetPointTimeCal, isExactHourDisplayed))
/*      */       {
/*  661 */         timeTagIndicatorArray[i] = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initializeBooleanArray(boolean[] booleanArray, boolean initValue) {
/*  667 */     if (booleanArray == null)
/*  668 */       return;
/*  669 */     for (int i = 0; i < booleanArray.length; i++)
/*  670 */       booleanArray[i] = initValue;
/*      */   }
/*      */ 
/*      */   private boolean isTimeTagDisplayable(Calendar targetPointTimeCal, boolean isExactHourDisplayed) {
/*  674 */     boolean isDisplayable = false;
/*  675 */     if (targetPointTimeCal == null) {
/*  676 */       return isDisplayable;
/*      */     }
/*  678 */     int minuteIntValue = targetPointTimeCal.get(12);
/*  679 */     if (isExactHourDisplayed) {
/*  680 */       if (minuteIntValue == 0) {
/*  681 */         isDisplayable = true;
/*      */       }
/*      */     }
/*  684 */     else if ((minuteIntValue == 0) || (minuteIntValue == 30)) {
/*  685 */       isDisplayable = true;
/*      */     }
/*  687 */     return isDisplayable;
/*      */   }
/*      */ 
/*      */   private double calculateDistanceBetweenLastInitPointAndFirstExtraPoint(double speed, Calendar lastInitPointCal, Calendar firstExtraPointCal)
/*      */   {
/*  692 */     long timeDifferenceBetweenExtraPoints = getTimeDifferenceInMillisecond(lastInitPointCal, 
/*  693 */       firstExtraPointCal);
/*  694 */     double distanceBetweenLastInitPointAndFirstExtraPoint = speed * timeDifferenceBetweenExtraPoints;
/*  695 */     return distanceBetweenLastInitPointAndFirstExtraPoint;
/*      */   }
/*      */ 
/*      */   private double calculateDistanceForExtraPoints(double speed, Calendar firstExtraPointCal, int elapsedHourForExtraPoint, int elapsedMinuteForExtraPoint)
/*      */   {
/*  701 */     Calendar nextExtraPointTimeCal = getTimeElapsedCalendar(firstExtraPointCal, 
/*  702 */       elapsedHourForExtraPoint, 
/*  703 */       elapsedMinuteForExtraPoint);
/*  704 */     long timeDifferenceBetweenExtraPoints = getTimeDifferenceInMillisecond(firstExtraPointCal, 
/*  705 */       nextExtraPointTimeCal);
/*  706 */     double distanceForExtraPoint = speed * timeDifferenceBetweenExtraPoints;
/*  707 */     return distanceForExtraPoint;
/*      */   }
/*      */ 
/*      */   private TrackPoint[] calculateExtrapTrackPoints(GeodeticCalculator gc, Coordinate lastInitPointCoordinate, double direction, double distanceBetweenLastInitPointAndFirstExtraPoint, double distanceBetweenExtraPoint, int extraPointNumber, Calendar lastInitPointTimeCal, Calendar firstExtraPointTimeCal, int elapsedHourForExtraPoint, int elapsedMinuteForExtraPoint)
/*      */   {
/*  717 */     double startLongitude = lastInitPointCoordinate.x;
/*  718 */     double startLatitude = lastInitPointCoordinate.y;
/*  719 */     TrackPoint[] trackPointArray = new TrackPoint[extraPointNumber];
/*      */ 
/*  724 */     Point2D firstExtraPointPoint2dValue = getNextPoint2DValue(gc, startLongitude, startLatitude, 
/*  725 */       direction, distanceBetweenLastInitPointAndFirstExtraPoint);
/*  726 */     startLongitude = firstExtraPointPoint2dValue.getX();
/*  727 */     startLatitude = firstExtraPointPoint2dValue.getY();
/*  728 */     Coordinate firstExtraPointCoordinate = new Coordinate(startLongitude, startLatitude);
/*  729 */     TrackPoint firstExtraTrackPoint = new TrackPoint(firstExtraPointCoordinate, firstExtraPointTimeCal);
/*  730 */     trackPointArray[0] = firstExtraTrackPoint;
/*      */ 
/*  733 */     Calendar newPointCal = firstExtraPointTimeCal;
/*  734 */     for (int i = 1; i < extraPointNumber; i++) {
/*  735 */       Point2D pt = getNextPoint2DValue(gc, startLongitude, startLatitude, 
/*  736 */         direction, distanceBetweenExtraPoint);
/*  737 */       startLongitude = pt.getX();
/*  738 */       startLatitude = pt.getY();
/*  739 */       Coordinate nextCoordinate = new Coordinate(startLongitude, startLatitude);
/*  740 */       newPointCal = getTimeElapsedCalendar(newPointCal, elapsedHourForExtraPoint, 
/*  741 */         elapsedMinuteForExtraPoint);
/*  742 */       TrackPoint eachTrackPoint = new TrackPoint(nextCoordinate, newPointCal);
/*  743 */       trackPointArray[i] = eachTrackPoint;
/*      */     }
/*  745 */     return trackPointArray;
/*      */   }
/*      */ 
/*      */   private Point2D getNextPoint2DValue(GeodeticCalculator gc, double startingPointLongitude, double startIngPointLatitude, double direction, double distanceBetweenTwoPoints)
/*      */   {
/*  751 */     gc.setStartingGeographicPoint(startingPointLongitude, startIngPointLatitude);
/*  752 */     gc.setDirection(direction, distanceBetweenTwoPoints);
/*  753 */     Point2D pt = gc.getDestinationGeographicPoint();
/*  754 */     return pt;
/*      */   }
/*      */ 
/*      */   private long getTimeDifferenceInMillisecond(Calendar startTimeCal, Calendar endTimeCal) {
/*  758 */     long startTimeInMillisecond = startTimeCal.getTimeInMillis();
/*  759 */     long endTimeInMillisecond = endTimeCal.getTimeInMillis();
/*      */ 
/*  761 */     long timeDiffInMillisecond = endTimeInMillisecond - startTimeInMillisecond;
/*  762 */     return timeDiffInMillisecond;
/*      */   }
/*      */ 
/*      */   private boolean isCoordinateValid(Coordinate coordinate) {
/*  766 */     if ((coordinate.x > 180.0D) || (coordinate.x < -180.0D) || (coordinate.y > 90.0D) || 
/*  767 */       (coordinate.y < -90.0D))
/*  768 */       return false;
/*  769 */     return true;
/*      */   }
/*      */ 
/*      */   private Calendar getIntervalCalendarByParsingString(String dateString, String formatStringPattern, Calendar secondTimeCal)
/*      */   {
/*  774 */     SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStringPattern);
/*  775 */     Calendar cal = null;
/*  776 */     int elapsedHour = 1;
/*  777 */     int elapsedMinute = 0;
/*  778 */     if (dateString != null) {
/*      */       try {
/*  780 */         Date date = simpleDateFormat.parse(dateString);
/*  781 */         cal = Calendar.getInstance();
/*  782 */         cal.setTime(date);
/*  783 */         elapsedHour = cal.get(11);
/*  784 */         elapsedMinute = cal.get(12);
/*      */       }
/*      */       catch (ParseException pe) {
/*  787 */         elapsedHour = 1;
/*      */       }
/*      */     }
/*  790 */     setElapsedHourForExtraPoint(elapsedHour);
/*  791 */     setElapsedMinuteForExtraPoint(elapsedMinute);
/*  792 */     cal = getTimeElapsedCalendar(secondTimeCal, elapsedHour, elapsedMinute);
/*  793 */     return cal;
/*      */   }
/*      */ 
/*      */   private Calendar getTimeElapsedCalendarForFirstExtraPoint(Calendar startCalendar, int elapsedHour, int elapsedMinute)
/*      */   {
/*  798 */     Calendar nextCal = Calendar.getInstance();
/*  799 */     nextCal.setTimeInMillis(startCalendar.getTimeInMillis());
/*      */ 
/*  810 */     if (elapsedHour >= 1) {
/*  811 */       nextCal.set(12, 0);
/*  812 */       nextCal.add(11, 1);
/*      */     } else {
/*  814 */       int currentMinute = nextCal.get(12);
/*  815 */       if (currentMinute >= elapsedMinute) {
/*  816 */         nextCal.set(12, 0);
/*  817 */         nextCal.add(11, 1);
/*      */       } else {
/*  819 */         nextCal.set(12, elapsedMinute);
/*      */       }
/*      */     }
/*  822 */     return nextCal;
/*      */   }
/*      */ 
/*      */   private Calendar getTimeElapsedCalendar(Calendar startCalendar, int elapsedHour, int elapsedMinute)
/*      */   {
/*  827 */     Calendar nextCal = Calendar.getInstance();
/*  828 */     nextCal.setTimeInMillis(startCalendar.getTimeInMillis());
/*  829 */     nextCal.add(11, elapsedHour);
/*  830 */     nextCal.add(12, elapsedMinute);
/*  831 */     return nextCal;
/*      */   }
/*      */ 
/*      */   public Color getExtrapColor()
/*      */   {
/*  840 */     return this.extrapColor;
/*      */   }
/*      */ 
/*      */   public String getExtrapLinePattern()
/*      */   {
/*  845 */     return "LINE_SOLID";
/*      */   }
/*      */ 
/*      */   public String getExtrapMarker()
/*      */   {
/*  850 */     return "FILLED_TRIANGLE";
/*      */   }
/*      */ 
/*      */   public TrackPoint[] getInitTrackPoints() {
/*  854 */     return this.initTrackPoints;
/*      */   }
/*      */ 
/*      */   public void setInitTrackPoints(TrackPoint[] initTrackPoints) {
/*  858 */     this.initTrackPoints = initTrackPoints;
/*      */   }
/*      */ 
/*      */   public void setExtrapPoints(TrackPoint[] extrapPoints) {
/*  862 */     this.extrapPoints = extrapPoints;
/*      */   }
/*      */ 
/*      */   public TrackPoint[] getExtrapPoints()
/*      */   {
/*  867 */     return this.extrapPoints;
/*      */   }
/*      */ 
/*      */   public Color getInitialColor()
/*      */   {
/*  872 */     return this.initialColor;
/*      */   }
/*      */ 
/*      */   public String getInitialLinePattern()
/*      */   {
/*  877 */     return "LINE_SOLID";
/*      */   }
/*      */ 
/*      */   public String getInitialMarker()
/*      */   {
/*  882 */     return "FILLED_DIAMOND";
/*      */   }
/*      */ 
/*      */   public TrackPoint[] getInitialPoints()
/*      */   {
/*  887 */     return this.initTrackPoints;
/*      */   }
/*      */ 
/*      */   public String getFontName()
/*      */   {
/*  892 */     return this.fontName;
/*      */   }
/*      */ 
/*      */   public void setFontName(String _fontName) {
/*  896 */     this.fontName = _fontName;
/*      */   }
/*      */ 
/*      */   public float getFontSize()
/*      */   {
/*  901 */     if (this.fontSize <= 0.0D)
/*  902 */       return 14.0F;
/*  903 */     return this.fontSize;
/*      */   }
/*      */ 
/*      */   public void setFontSize(float _fontSize) {
/*  907 */     this.fontSize = _fontSize;
/*      */   }
/*      */ 
/*      */   public IText.FontStyle getFontStyle() {
/*  911 */     return this.fontStyle;
/*      */   }
/*      */ 
/*      */   public void setFontStyle(IText.FontStyle fontStyle) {
/*  915 */     this.fontStyle = fontStyle;
/*      */   }
/*      */ 
/*      */   public float getLineWidth()
/*      */   {
/*  920 */     if (this.lineWidth <= 0.0D)
/*  921 */       return 0.1F;
/*  922 */     return this.lineWidth;
/*      */   }
/*      */ 
/*      */   public boolean isSetTimeButtonSelected()
/*      */   {
/*  929 */     return this.setTimeButtonSelected;
/*      */   }
/*      */ 
/*      */   public void setSetTimeButtonSelected(boolean setTimeButtonSelected) {
/*  933 */     this.setTimeButtonSelected = setTimeButtonSelected;
/*      */   }
/*      */ 
/*      */   public int getFontSizeComboSelectedIndex() {
/*  937 */     return this.fontSizeComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setFontSizeComboSelectedIndex(int fontSizeComboSelectedIndex) {
/*  941 */     this.fontSizeComboSelectedIndex = fontSizeComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public int getFontNameComboSelectedIndex() {
/*  945 */     return this.fontNameComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setFontNameComboSelectedIndex(int fontNameComboSelectedIndex) {
/*  949 */     this.fontNameComboSelectedIndex = fontNameComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public int getFontStyleComboSelectedIndex() {
/*  953 */     return this.fontStyleComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setFontStyleComboSelectedIndex(int fontStyleComboSelectedIndex) {
/*  957 */     this.fontStyleComboSelectedIndex = fontStyleComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public int getIntervalComboSelectedIndex() {
/*  961 */     return this.intervalComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setIntervalComboSelectedIndex(int intervalComboSelectedIndex) {
/*  965 */     this.intervalComboSelectedIndex = intervalComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public int getUnitComboSelectedIndex() {
/*  969 */     return this.unitComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setUnitComboSelectedIndex(int unitComboSelectedIndex) {
/*  973 */     this.unitComboSelectedIndex = unitComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public int getRoundComboSelectedIndex() {
/*  977 */     return this.roundComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setRoundComboSelectedIndex(int roundComboSelectedIndex) {
/*  981 */     this.roundComboSelectedIndex = roundComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public boolean getRoundBtnSelected() {
/*  985 */     return this.roundBtnSelected;
/*      */   }
/*      */ 
/*      */   public void setRoundBtnSelected(boolean roundBtnSelected) {
/*  989 */     this.roundBtnSelected = roundBtnSelected;
/*      */   }
/*      */ 
/*      */   public int getRoundDirComboSelectedIndex() {
/*  993 */     return this.roundDirComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setRoundDirComboSelectedIndex(int roundDirComboSelectedIndex) {
/*  997 */     this.roundDirComboSelectedIndex = roundDirComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public boolean getRoundDirBtnSelected() {
/* 1001 */     return this.roundDirBtnSelected;
/*      */   }
/*      */ 
/*      */   public void setRoundDirBtnSelected(boolean roundDirBtnSelected) {
/* 1005 */     this.roundDirBtnSelected = roundDirBtnSelected;
/*      */   }
/*      */ 
/*      */   public double getDirectionForExtraPoints() {
/* 1009 */     return this.directionForExtraPoints;
/*      */   }
/*      */ 
/*      */   public double getFromdirection()
/*      */   {
/* 1014 */     Coordinate initPointBeforeLastInitPointCoordinate = this.initTrackPoints[(this.initTrackPoints.length - 2)].getLocation();
/* 1015 */     Coordinate lastInitPointCoordinate = this.initTrackPoints[(this.initTrackPoints.length - 1)].getLocation();
/*      */ 
/* 1017 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/* 1018 */     gc.setStartingGeographicPoint(lastInitPointCoordinate.x, 
/* 1019 */       lastInitPointCoordinate.y);
/* 1020 */     gc.setDestinationGeographicPoint(initPointBeforeLastInitPointCoordinate.x, 
/* 1021 */       initPointBeforeLastInitPointCoordinate.y);
/* 1022 */     double dir = gc.getAzimuth();
/* 1023 */     if (dir < 0.0D) dir += 360.0D;
/* 1024 */     return dir;
/*      */   }
/*      */ 
/*      */   public void setDirectionForExtraPoints(double directionForExtraPoints) {
/* 1028 */     this.directionForExtraPoints = directionForExtraPoints;
/*      */   }
/*      */ 
/*      */   public double getSpeed() {
/* 1032 */     return this.speed;
/*      */   }
/*      */ 
/*      */   public void setSpeed(double speed) {
/* 1036 */     this.speed = speed;
/*      */ 
/* 1041 */     this.speedInKnotOverHour = (speed * 1944.0D);
/* 1042 */     this.speedInKilometerOverHour = (speed * 3600.0D);
/* 1043 */     this.speedInMileOverHour = (speed * 2237.0D);
/*      */   }
/*      */ 
/*      */   public double getSpeedInKnotOverHour() {
/* 1047 */     return this.speedInKnotOverHour;
/*      */   }
/*      */   public double getSpeedInKilometerOverHour() {
/* 1050 */     return this.speedInKilometerOverHour;
/*      */   }
/*      */   public double getSpeedInMileOverHour() {
/* 1053 */     return this.speedInMileOverHour;
/*      */   }
/*      */ 
/*      */   public ITrack.ExtraPointTimeDisplayOption getExtraPointTimeDisplayOption()
/*      */   {
/* 1058 */     return this.extraPointTimeDisplayOption;
/*      */   }
/*      */ 
/*      */   public void setExtraPointTimeDisplayOption(ITrack.ExtraPointTimeDisplayOption extraPointTimeDisplayOption) {
/* 1062 */     this.extraPointTimeDisplayOption = extraPointTimeDisplayOption;
/*      */   }
/*      */ 
/*      */   public String getSkipFactorTextString() {
/* 1066 */     return this.skipFactorTextString;
/*      */   }
/*      */ 
/*      */   public void setSkipFactorTextString(String skipFactorTextString) {
/* 1070 */     this.skipFactorTextString = skipFactorTextString;
/*      */   }
/*      */ 
/*      */   public boolean[] getExtraPointTimeTextDisplayIndicator() {
/* 1074 */     return this.extraPointTimeTextDisplayIndicator;
/*      */   }
/*      */ 
/*      */   public void setExtraPointTimeTextDisplayIndicator(boolean[] extraPointTimeTextDisplayIndicator)
/*      */   {
/* 1079 */     this.extraPointTimeTextDisplayIndicator = extraPointTimeTextDisplayIndicator;
/*      */   }
/*      */ 
/*      */   public void setInitialColor(Color initialColor) {
/* 1083 */     this.initialColor = initialColor;
/*      */   }
/*      */ 
/*      */   public void setExtrapColor(Color extrapColor) {
/* 1087 */     this.extrapColor = extrapColor;
/*      */   }
/*      */ 
/*      */   public void setInitialMarker(String initialMarker) {
/* 1091 */     this.initialMarker = initialMarker;
/*      */   }
/*      */ 
/*      */   public void setInitialLinePattern(String initialLinePattern) {
/* 1095 */     this.initialLinePattern = initialLinePattern;
/*      */   }
/*      */ 
/*      */   public void setLineWidth(float lineWidth) {
/* 1099 */     this.lineWidth = lineWidth;
/*      */   }
/*      */ 
/*      */   public int getExtraDrawingPointNumber() {
/* 1103 */     if (this.extraDrawingPointNumber <= 0)
/* 1104 */       return 5;
/* 1105 */     return this.extraDrawingPointNumber;
/*      */   }
/*      */ 
/*      */   public void setExtraDrawingPointNumber(int extraDrawingPointNumber) {
/* 1109 */     this.extraDrawingPointNumber = extraDrawingPointNumber;
/*      */   }
/*      */ 
/*      */   public Calendar getFirstTimeCalendar() {
/* 1113 */     return this.firstTimeCalendar;
/*      */   }
/*      */ 
/*      */   public void setFirstTimeCalendar(Calendar firstTimeCalendar) {
/* 1117 */     this.firstTimeCalendar = firstTimeCalendar;
/*      */   }
/*      */ 
/*      */   public Calendar getSecondTimeCalendar() {
/* 1121 */     return this.secondTimeCalendar;
/*      */   }
/*      */ 
/*      */   public void setSecondTimeCalendar(Calendar secondTimeCalendar) {
/* 1125 */     this.secondTimeCalendar = secondTimeCalendar;
/*      */   }
/*      */ 
/*      */   public String getIntervalTimeString() {
/* 1129 */     return this.intervalTimeString;
/*      */   }
/*      */ 
/*      */   public void setIntervalTimeString(String _intervalTimeString)
/*      */   {
/* 1136 */     this.intervalTimeString = _intervalTimeString;
/*      */ 
/* 1138 */     Calendar intervalTimeCal = getIntervalCalendarByParsingString(_intervalTimeString, 
/* 1139 */       "HH:mm", getSecondTimeCalendar());
/*      */   }
/*      */ 
/*      */   public int getElapsedHourForExtraPoint()
/*      */   {
/* 1144 */     return this.elapsedHourForExtraPoint;
/*      */   }
/*      */ 
/*      */   private void setElapsedHourForExtraPoint(int elapsedHourForExtraPoint) {
/* 1148 */     this.elapsedHourForExtraPoint = elapsedHourForExtraPoint;
/*      */   }
/*      */ 
/*      */   public int getElapsedMinuteForExtraPoint() {
/* 1152 */     return this.elapsedMinuteForExtraPoint;
/*      */   }
/*      */ 
/*      */   private void setElapsedMinuteForExtraPoint(int elapsedMinuteForExtraPoint) {
/* 1156 */     this.elapsedMinuteForExtraPoint = elapsedMinuteForExtraPoint;
/*      */   }
/*      */ 
/*      */   public String getSkipFactorText()
/*      */   {
/* 1161 */     return this.skipFactorTextString;
/*      */   }
/*      */ 
/*      */   public IText.FontStyle getStyle()
/*      */   {
/* 1167 */     return null;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Track
 * JD-Core Version:    0.6.2
 */