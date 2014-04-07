/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.TrackPoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Track;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class TrackExtrapPointInfoDlg extends AttrDlg
/*     */ {
/*  52 */   private static TrackExtrapPointInfoDlg INSTANCE = null;
/*     */ 
/*  54 */   private final int DEFAULT_TEXT_WIDTH = 115;
/*  55 */   private final int DEFAULT_TEXT_HEIGHT = 15;
/*  56 */   private final int DEFAULT_SCOLL_TEXT_WIDTH = 320;
/*  57 */   private final int DEFAULT_SCOLL_TEXT_HEIGHT = 60;
/*     */ 
/*  59 */   private final String THREE_DECIMAL_DIGIT_PATTERN = "#.###";
/*  60 */   private final String TWO_DECIMAL_DIGIT_PATTERN = "#.##";
/*     */   private Track track;
/*     */   private Text speedText;
/*     */   private Text directionText;
/*     */   private Text firstInitPointTimeText;
/*     */   private Text firstInitPointLatText;
/*     */   private Text firstInitPointLonText;
/*     */   private Text secondInitPointTimeText;
/*     */   private Text secondInitPointLatText;
/*     */   private Text secondInitPointLonText;
/*     */   private Text extraPointInfoText;
/*     */ 
/*     */   public void setSpeedText(String speedText)
/*     */   {
/*  77 */     this.speedText.setText(speedText);
/*     */   }
/*     */ 
/*     */   public void setDirectionText(String directionText) {
/*  81 */     this.directionText.setText(directionText);
/*     */   }
/*     */ 
/*     */   private Text getFirstInitPointTimeText() {
/*  85 */     return this.firstInitPointTimeText;
/*     */   }
/*     */ 
/*     */   private Text getFirstInitPointLatText() {
/*  89 */     return this.firstInitPointLatText;
/*     */   }
/*     */ 
/*     */   private Text getFirstInitPointLonText() {
/*  93 */     return this.firstInitPointLonText;
/*     */   }
/*     */ 
/*     */   private Text getSecondInitPointTimeText() {
/*  97 */     return this.secondInitPointTimeText;
/*     */   }
/*     */ 
/*     */   private Text getSecondInitPointLatText() {
/* 101 */     return this.secondInitPointLatText;
/*     */   }
/*     */ 
/*     */   private Text getSecondInitPointLonText() {
/* 105 */     return this.secondInitPointLonText;
/*     */   }
/*     */ 
/*     */   public Track getTrack() {
/* 109 */     return this.track;
/*     */   }
/*     */ 
/*     */   public void setTrack(Track _track, int unitComboSelectedIndex, int roundComboSelectedIndex, int roundDirComboSelectedIndex) {
/* 113 */     this.track = _track;
/* 114 */     String unit = "";
/* 115 */     double speed = 0.0D;
/* 116 */     double dir = 0.0D;
/* 117 */     int roundSpeed = 0;
/* 118 */     int roundDir = 0;
/*     */ 
/* 121 */     if (unitComboSelectedIndex == 0) {
/* 122 */       unit = " kts";
/* 123 */       speed = this.track.getSpeedInKnotOverHour();
/*     */     }
/* 125 */     else if (unitComboSelectedIndex == 1) {
/* 126 */       unit = " kph";
/* 127 */       speed = this.track.getSpeedInKilometerOverHour();
/*     */     }
/* 129 */     else if (unitComboSelectedIndex == 2) {
/* 130 */       unit = " mph";
/* 131 */       speed = this.track.getSpeedInMileOverHour();
/*     */     }
/*     */ 
/* 134 */     if (roundComboSelectedIndex == 1)
/* 135 */       roundSpeed = roundTo5((int)(speed + 0.5D));
/* 136 */     else if (roundComboSelectedIndex == 2) {
/* 137 */       roundSpeed = roundTo10((int)(speed + 0.5D));
/*     */     }
/* 139 */     if (roundComboSelectedIndex > 0)
/* 140 */       this.speedText.setText("Spd: " + roundSpeed + unit);
/*     */     else {
/* 142 */       this.speedText.setText("Spd: " + doubleValurFormater(speed, 2) + unit);
/*     */     }
/*     */ 
/* 145 */     dir = this.track.getFromdirection();
/* 146 */     if (roundDirComboSelectedIndex == 1)
/* 147 */       roundDir = (int)(dir + 0.5D);
/* 148 */     else if (roundDirComboSelectedIndex == 2) {
/* 149 */       roundDir = roundTo5((int)(dir + 0.5D));
/*     */     }
/* 151 */     if (roundDirComboSelectedIndex > 0)
/* 152 */       this.directionText.setText("Dir: " + roundDir + " deg.");
/*     */     else {
/* 154 */       this.directionText.setText("Dir: " + doubleValurFormater(dir, 2) + " deg.");
/*     */     }
/*     */ 
/* 159 */     fillLastTwoInitPointInfo(this.track.getInitialPoints());
/*     */ 
/* 161 */     fillExtraPointInfo(this.track.getExtrapPoints());
/*     */   }
/*     */ 
/*     */   private int roundTo5(int speed) {
/* 165 */     int remain = speed % 10;
/* 166 */     int divid = speed / 10;
/* 167 */     int roundSpeed = 0;
/*     */ 
/* 169 */     if ((remain >= 1) && (remain <= 2))
/* 170 */       roundSpeed = divid * 10;
/* 171 */     else if ((remain >= 3) && (remain <= 7))
/* 172 */       roundSpeed = divid * 10 + 5;
/* 173 */     else if ((remain >= 8) && (remain <= 9))
/* 174 */       roundSpeed = divid * 10 + 10;
/*     */     else {
/* 176 */       roundSpeed = speed;
/*     */     }
/* 178 */     return roundSpeed;
/*     */   }
/*     */ 
/*     */   private int roundTo10(int speed) {
/* 182 */     int remain = speed % 10;
/* 183 */     int divid = speed / 10;
/* 184 */     int roundSpeed = 0;
/*     */ 
/* 186 */     if ((remain >= 1) && (remain <= 4))
/* 187 */       roundSpeed = divid * 10;
/* 188 */     else if ((remain >= 5) && (remain <= 9))
/* 189 */       roundSpeed = divid * 10 + 10;
/*     */     else {
/* 191 */       roundSpeed = speed;
/*     */     }
/* 193 */     return roundSpeed;
/*     */   }
/*     */ 
/*     */   private TrackExtrapPointInfoDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 202 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static TrackExtrapPointInfoDlg getInstance(Shell parShell)
/*     */   {
/* 213 */     if (INSTANCE == null) {
/*     */       try {
/* 215 */         INSTANCE = new TrackExtrapPointInfoDlg(parShell);
/*     */       } catch (VizException e) {
/* 217 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 220 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 229 */     Composite top = (Composite)super.createDialogArea(parent);
/*     */ 
/* 232 */     GridLayout mainLayout = new GridLayout(3, false);
/* 233 */     mainLayout.marginHeight = 3;
/* 234 */     mainLayout.marginWidth = 3;
/* 235 */     top.setLayout(mainLayout);
/*     */ 
/* 239 */     initializeComponents(top, parent);
/*     */ 
/* 241 */     return top;
/*     */   }
/*     */ 
/*     */   private void fillLastTwoInitPointInfo(TrackPoint[] trackPoints) {
/* 245 */     if ((trackPoints == null) || (trackPoints.length < 2))
/* 246 */       return;
/* 247 */     int length = trackPoints.length;
/* 248 */     TrackPoint firstTrackPoint = trackPoints[(length - 2)];
/* 249 */     TrackPoint secondTrackPoint = trackPoints[(length - 1)];
/*     */ 
/* 251 */     setInitPointTimeText(firstTrackPoint, getFirstInitPointTimeText());
/* 252 */     setInitPointLatText(firstTrackPoint, getFirstInitPointLatText());
/* 253 */     setInitPointLonText(firstTrackPoint, getFirstInitPointLonText());
/*     */ 
/* 255 */     setInitPointTimeText(secondTrackPoint, getSecondInitPointTimeText());
/* 256 */     setInitPointLatText(secondTrackPoint, getSecondInitPointLatText());
/* 257 */     setInitPointLonText(secondTrackPoint, getSecondInitPointLonText());
/*     */   }
/*     */ 
/*     */   private void fillExtraPointInfo(TrackPoint[] trackPoints)
/*     */   {
/* 265 */     if ((trackPoints == null) || (trackPoints.length < 2)) {
/* 266 */       return;
/*     */     }
/* 268 */     StringBuilder strBuilder = new StringBuilder(100);
/* 269 */     for (int i = 0; i < trackPoints.length; i++) {
/* 270 */       strBuilder.append(getTimeStringByTrackPoint(trackPoints[i]));
/* 271 */       strBuilder.append("\t\t\t\t");
/* 272 */       strBuilder.append(getLatStringByTrackPoint(trackPoints[i]));
/* 273 */       strBuilder.append("\t\t");
/* 274 */       strBuilder.append(getLonStringByTrackPoint(trackPoints[i]));
/* 275 */       strBuilder.append(Text.DELIMITER);
/*     */     }
/* 277 */     this.extraPointInfoText.setText(strBuilder.toString().trim());
/*     */   }
/*     */ 
/*     */   private void setInitPointTimeText(TrackPoint trackPoint, Text initTimeTextObject) {
/* 281 */     if ((trackPoint == null) || (trackPoint.getTime() == null))
/* 282 */       return;
/* 283 */     String timeString = getHourMinuteTimeString(trackPoint.getTime());
/* 284 */     initTimeTextObject.setText(timeString);
/*     */   }
/*     */ 
/*     */   private void setInitPointLatText(TrackPoint trackPoint, Text initLatTextObject) {
/* 288 */     if ((trackPoint == null) || (trackPoint.getLocation() == null))
/* 289 */       return;
/* 290 */     Coordinate coordinate = trackPoint.getLocation();
/* 291 */     initLatTextObject.setText(doubleValurFormater(coordinate.y, 3));
/*     */   }
/*     */ 
/*     */   private void setInitPointLonText(TrackPoint trackPoint, Text initLonTextObject) {
/* 295 */     if ((trackPoint == null) || (trackPoint.getLocation() == null))
/* 296 */       return;
/* 297 */     Coordinate coordinate = trackPoint.getLocation();
/* 298 */     initLonTextObject.setText(doubleValurFormater(coordinate.x, 3));
/*     */   }
/*     */ 
/*     */   private String getTimeStringByTrackPoint(TrackPoint trackPoint) {
/* 302 */     return getHourMinuteTimeString(trackPoint.getTime());
/*     */   }
/*     */ 
/*     */   private String getLatStringByTrackPoint(TrackPoint trackPoint) {
/* 306 */     Coordinate coordinate = trackPoint.getLocation();
/* 307 */     if (coordinate == null)
/* 308 */       return "";
/* 309 */     return String.valueOf(doubleValurFormater(coordinate.y, 3));
/*     */   }
/*     */ 
/*     */   private String getLonStringByTrackPoint(TrackPoint trackPoint) {
/* 313 */     Coordinate coordinate = trackPoint.getLocation();
/* 314 */     if (coordinate == null)
/* 315 */       return "";
/* 316 */     return String.valueOf(doubleValurFormater(coordinate.x, 3));
/*     */   }
/*     */ 
/*     */   private String getHourMinuteTimeString(Calendar cal) {
/* 320 */     if (cal == null)
/* 321 */       return "";
/* 322 */     StringBuilder timeStringBuilder = new StringBuilder(4);
/* 323 */     if (cal.get(11) < 10)
/* 324 */       timeStringBuilder.append(0);
/* 325 */     timeStringBuilder.append(cal.get(11));
/* 326 */     if (cal.get(12) < 10)
/* 327 */       timeStringBuilder.append(0);
/* 328 */     timeStringBuilder.append(cal.get(12));
/* 329 */     return timeStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private String doubleValurFormater(double doubleValue, int decimalLength) {
/* 333 */     NumberFormat formatter = null;
/* 334 */     if (decimalLength > 2)
/* 335 */       formatter = new DecimalFormat("#.###");
/*     */     else {
/* 337 */       formatter = new DecimalFormat("#.##");
/*     */     }
/* 339 */     return formatter.format(doubleValue);
/*     */   }
/*     */ 
/*     */   private void initializeComponents(Composite topComposite, Composite parent)
/*     */   {
/* 347 */     getShell().setText("Track Results");
/*     */ 
/* 349 */     this.speedText = createTextfieldWithoutLabel(topComposite, 
/* 350 */       2052, 115, 15, false);
/* 351 */     this.directionText = createTextfieldWithoutLabel(topComposite, 
/* 352 */       2052, 115, 15, false);
/*     */ 
/* 354 */     Label emptyLabel = new Label(topComposite, 16384);
/* 355 */     emptyLabel.setText("   ");
/*     */ 
/* 360 */     Label timeTitleLabel = new Label(topComposite, 16384);
/* 361 */     timeTitleLabel.setText("Time");
/* 362 */     Label latitudeTitleLabel = new Label(topComposite, 16384);
/* 363 */     latitudeTitleLabel.setText("Latitude");
/* 364 */     Label longitudeTitleLabel = new Label(topComposite, 16384);
/* 365 */     longitudeTitleLabel.setText("Longitude");
/*     */ 
/* 370 */     drawSingleLabelSpanColumns(topComposite, 3, "------------------------------------------------------------------------------");
/*     */ 
/* 375 */     this.firstInitPointTimeText = createTextfieldWithoutLabel(topComposite, 
/* 376 */       2052, 57, 15, false);
/* 377 */     this.firstInitPointLatText = createTextfieldWithoutLabel(topComposite, 
/* 378 */       2052, 57, 15, false);
/* 379 */     this.firstInitPointLonText = createTextfieldWithoutLabel(topComposite, 
/* 380 */       2052, 57, 15, false);
/*     */ 
/* 385 */     this.secondInitPointTimeText = createTextfieldWithoutLabel(topComposite, 
/* 386 */       2052, 57, 15, false);
/* 387 */     this.secondInitPointLatText = createTextfieldWithoutLabel(topComposite, 
/* 388 */       2052, 57, 15, false);
/* 389 */     this.secondInitPointLonText = createTextfieldWithoutLabel(topComposite, 
/* 390 */       2052, 57, 15, false);
/*     */ 
/* 395 */     drawSingleLabelSpanColumns(topComposite, 3, "------------------------------------------------------------------------------");
/*     */ 
/* 398 */     this.extraPointInfoText = new Text(topComposite, 2818);
/*     */ 
/* 400 */     GridData gridData = new GridData(320, 60);
/* 401 */     gridData.horizontalSpan = 3;
/* 402 */     this.extraPointInfoText.setLayoutData(gridData);
/* 403 */     this.extraPointInfoText.setEditable(false);
/*     */   }
/*     */ 
/*     */   private void drawSingleLabelSpanColumns(Composite parentComposite, int spanColumns, String labelString)
/*     */   {
/* 409 */     Label label = new Label(parentComposite, 16384);
/* 410 */     label.setText(labelString);
/*     */ 
/* 412 */     GridData gridData = new GridData();
/* 413 */     gridData.horizontalSpan = spanColumns;
/* 414 */     label.setLayoutData(gridData);
/*     */   }
/*     */ 
/*     */   private Text createTextfieldWithoutLabel(Composite parentComposite, int textStyle, int textWidth, int textHeight, boolean isEditable)
/*     */   {
/* 419 */     Text text = new Text(parentComposite, textStyle);
/* 420 */     text.setLayoutData(new GridData(textWidth, textHeight));
/* 421 */     text.setEditable(isEditable);
/* 422 */     return text;
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/* 430 */     HashMap attr = new HashMap();
/*     */ 
/* 432 */     return attr;
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute attr)
/*     */   {
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.TrackExtrapPointInfoDlg
 * JD-Core Version:    0.6.2
 */