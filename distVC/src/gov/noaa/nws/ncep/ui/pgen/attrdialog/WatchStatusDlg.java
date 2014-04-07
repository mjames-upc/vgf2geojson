/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.StationTable;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenToolUtils;
/*     */ import java.util.Calendar;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
/*     */ import org.eclipse.swt.events.VerifyEvent;
/*     */ import org.eclipse.swt.events.VerifyListener;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.DateTime;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*     */ 
/*     */ public class WatchStatusDlg extends CaveJFACEDialog
/*     */ {
/*     */   private WatchBox wb;
/*     */   private Composite top;
/*     */   private Text watchNumber;
/*     */   private Button refBtn;
/*     */   private Text refTxt;
/*     */   private WatchStatusMsgDlg statusMsgDlg;
/*     */   private Combo dirCombo;
/*  80 */   private static String[] dir = { "RIGHT", "LEFT", "NORTH", "SOUTH", "EAST", "WEST", 
/*  81 */     "NORTHEAST", "SOUTHEAST", "NORTHWEST", "SOUTHWEST", 
/*  82 */     "NORTH AND EAST", "SOUTH AND EAST", "NORTH AND WEST", "SOUTH AND WEST" };
/*     */   private DateTime validDate;
/*     */   private DateTime validTime;
/*     */   private Button expBtn;
/*     */   private DateTime expDate;
/*     */   private DateTime expTime;
/*     */   private Text lnTxt;
/*     */   private Text forecaster;
/*     */ 
/*     */   protected WatchStatusDlg(Shell parentShell, WatchBox wb)
/*     */   {
/* 103 */     super(parentShell);
/* 104 */     this.wb = wb;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 115 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 118 */     getShell().setText("Watch Status");
/*     */ 
/* 121 */     GridLayout mainLayout = new GridLayout(1, false);
/* 122 */     mainLayout.marginHeight = 3;
/* 123 */     mainLayout.marginWidth = 3;
/* 124 */     mainLayout.verticalSpacing = 3;
/* 125 */     this.top.setLayout(mainLayout);
/*     */ 
/* 127 */     Composite wnComp = new Composite(this.top, 0);
/* 128 */     wnComp.setLayout(new GridLayout(4, false));
/*     */ 
/* 131 */     Label wnLbl = new Label(wnComp, 16384);
/* 132 */     wnLbl.setText("Watch:");
/* 133 */     this.watchNumber = new Text(wnComp, 133124);
/*     */ 
/* 135 */     this.watchNumber.addVerifyListener(new VerifyListener()
/*     */     {
/*     */       public void verifyText(VerifyEvent e)
/*     */       {
/* 139 */         e.doit = PgenUtil.validatePositiveInteger(e);
/* 140 */         if (!e.doit) Display.getCurrent().beep();
/*     */       }
/*     */     });
/* 146 */     if ((this.wb != null) && (this.wb.hasStatusLine())) {
/* 147 */       Label dirLbl = new Label(wnComp, 16384);
/* 148 */       dirLbl.setText("Continues To The");
/*     */ 
/* 150 */       this.dirCombo = new Combo(wnComp, 12);
/*     */ 
/* 152 */       for (String str : dir) {
/* 153 */         this.dirCombo.add(str);
/*     */       }
/*     */ 
/* 156 */       this.dirCombo.select(0);
/*     */ 
/* 158 */       Composite lnComp = new Composite(this.top, 0);
/* 159 */       lnComp.setLayout(new GridLayout(2, false));
/*     */ 
/* 161 */       Label lnLbl = new Label(lnComp, 16384);
/* 162 */       lnLbl.setText("Of The Line");
/* 163 */       this.lnTxt = new Text(lnComp, 18436);
/* 164 */       this.lnTxt.setLayoutData(new GridData(350, 20));
/*     */     }
/*     */     else {
/* 167 */       Label dirLbl = new Label(wnComp, 16384);
/* 168 */       dirLbl.setText("Continues Across Entire Area");
/*     */     }
/*     */ 
/* 171 */     AttrDlg.addSeparator(this.top);
/*     */ 
/* 174 */     Composite refComp = new Composite(this.top, 0);
/* 175 */     refComp.setLayout(new GridLayout(2, false));
/* 176 */     this.refBtn = new Button(refComp, 32);
/* 177 */     this.refBtn.setText("Reference Mesoscale Discussion#:");
/* 178 */     this.refTxt = new Text(refComp, 133124);
/* 179 */     this.refTxt.addVerifyListener(new VerifyListener()
/*     */     {
/*     */       public void verifyText(VerifyEvent e)
/*     */       {
/* 183 */         e.doit = PgenUtil.validatePositiveInteger(e);
/* 184 */         if (!e.doit) Display.getCurrent().beep();
/*     */       }
/*     */     });
/* 190 */     Composite expDt = new Composite(this.top, 0);
/* 191 */     expDt.setLayout(new GridLayout(3, false));
/* 192 */     this.expBtn = new Button(expDt, 32);
/* 193 */     this.expBtn.setText("Final Status - Expiration Time:");
/*     */ 
/* 195 */     this.expDate = new DateTime(expDt, 2080);
/* 196 */     this.expTime = new DateTime(expDt, 34944);
/*     */ 
/* 199 */     Label validLbl = new Label(expDt, 0);
/* 200 */     validLbl.setText("Status Valid Until:");
/*     */ 
/* 202 */     this.validDate = new DateTime(expDt, 2080);
/* 203 */     this.validTime = new DateTime(expDt, 34944);
/*     */ 
/* 206 */     Composite fcstComp = new Composite(this.top, 0);
/* 207 */     fcstComp.setLayout(new GridLayout(2, false));
/* 208 */     Label fcstLbl = new Label(fcstComp, 0);
/* 209 */     fcstLbl.setText("Forecaster:");
/* 210 */     this.forecaster = new Text(fcstComp, 133124);
/* 211 */     AttrDlg.addSeparator(this.top);
/*     */ 
/* 213 */     return this.top;
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 223 */     if (getShell() == null) {
/* 224 */       create();
/*     */     }
/*     */ 
/* 229 */     getButton(0).setText("Format");
/*     */ 
/* 231 */     if (this.wb.getIssueFlag() != 0) {
/* 232 */       initDlg();
/*     */     }
/* 234 */     return super.open();
/*     */   }
/*     */ 
/*     */   private void initDlg()
/*     */   {
/* 242 */     this.watchNumber.setText(String.valueOf(this.wb.getWatchNumber()));
/* 243 */     if (this.lnTxt != null) this.lnTxt.setText(generateFromLine());
/*     */ 
/* 245 */     if (this.wb.getStatusForecaster() != null) {
/* 246 */       this.forecaster.setText(this.wb.getStatusForecaster());
/*     */     }
/*     */     else {
/* 249 */       this.forecaster.setText(this.wb.getForecaster());
/*     */     }
/*     */ 
/* 252 */     Calendar exp = this.wb.getExpTime();
/* 253 */     if (exp != null) {
/* 254 */       this.expDate.setDate(exp.get(1), exp.get(2), exp.get(5));
/* 255 */       this.expTime.setTime(exp.get(10), exp.get(12), 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String generateFromLine()
/*     */   {
/* 266 */     String ln = "";
/*     */ 
/* 269 */     AbstractDrawableComponent adc = this.wb.getParent();
/* 270 */     if (((adc instanceof DECollection)) && (adc.getName().equalsIgnoreCase("Watch")))
/*     */     {
/* 272 */       Iterator it = ((DECollection)adc).getComponentIterator();
/*     */ 
/* 275 */       List anchors = PgenStaticDataProvider.getProvider().getAnchorTbl().getStationList();
/*     */ 
/* 278 */       while (it.hasNext()) {
/* 279 */         AbstractDrawableComponent elem = (AbstractDrawableComponent)it.next();
/* 280 */         if (((elem instanceof Line)) && (elem.getPgenType().equalsIgnoreCase("POINTED_ARROW")))
/*     */         {
/* 283 */           if (!ln.isEmpty()) ln = ln + " AND ";
/*     */ 
/* 285 */           for (Coordinate pt : elem.getPoints()) {
/* 286 */             Station st = WatchBox.getNearestAnchorPt(pt, anchors);
/*     */ 
/* 288 */             GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*     */ 
/* 290 */             gc.setStartingGeographicPoint(st.getLongitude().floatValue(), st.getLatitude().floatValue());
/* 291 */             gc.setDestinationGeographicPoint(pt.x, pt.y);
/*     */ 
/* 293 */             long dist = Math.round(gc.getOrthodromicDistance() / 1609.3399658203125D);
/* 294 */             long dir = Math.round(gc.getAzimuth());
/* 295 */             if (dir < 0L) dir += 360L;
/* 296 */             ln = ln + dist + " " + WatchBox.dirs[((int)Math.round(dir / 22.5D))] + " " + st.getStid() + " TO ";
/*     */           }
/*     */ 
/* 300 */           if (ln.length() >= 4) ln = ln.substring(0, ln.length() - 4);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 306 */     return ln;
/*     */   }
/*     */ 
/*     */   public int getWatchNumber()
/*     */   {
/* 315 */     if (this.watchNumber.getText() == null) {
/* 316 */       return 0;
/*     */     }
/*     */ 
/* 319 */     return Integer.valueOf(this.watchNumber.getText()).intValue();
/*     */   }
/*     */ 
/*     */   public int getDiscussionNumber()
/*     */   {
/* 328 */     if ((this.refTxt.getText() == null) || (this.refTxt.getText().isEmpty())) {
/* 329 */       return 0;
/*     */     }
/*     */ 
/* 332 */     return Integer.valueOf(this.refTxt.getText()).intValue();
/*     */   }
/*     */ 
/*     */   public WatchBox getWatchBox()
/*     */   {
/* 341 */     return this.wb;
/*     */   }
/*     */ 
/*     */   private Calendar getExpirationTime()
/*     */   {
/* 350 */     if (this.expBtn.getSelection()) {
/* 351 */       Calendar expiration = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 352 */       expiration.set(this.expDate.getYear(), this.expDate.getMonth(), this.expDate.getDay(), 
/* 353 */         this.expTime.getHours(), this.expTime.getMinutes(), 0);
/* 354 */       expiration.set(14, 0);
/* 355 */       return expiration;
/*     */     }
/*     */ 
/* 358 */     return getValidTime();
/*     */   }
/*     */ 
/*     */   private Calendar getValidTime()
/*     */   {
/* 367 */     Calendar expiration = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 368 */     expiration.set(this.validDate.getYear(), this.validDate.getMonth(), this.validDate.getDay(), 
/* 369 */       this.validTime.getHours(), this.validTime.getMinutes(), 0);
/* 370 */     expiration.set(14, 0);
/* 371 */     return expiration;
/*     */   }
/*     */ 
/*     */   protected void okPressed()
/*     */   {
/* 383 */     applyOnWB();
/*     */ 
/* 386 */     this.statusMsgDlg = new WatchStatusMsgDlg(getParentShell(), this);
/* 387 */     this.statusMsgDlg.setBlockOnOpen(false);
/* 388 */     this.statusMsgDlg.setMessage(generateStatusMsg());
/* 389 */     this.statusMsgDlg.open();
/*     */   }
/*     */ 
/*     */   public void applyOnWB()
/*     */   {
/* 397 */     this.wb.setWatchNumber(getWatchNumber());
/*     */ 
/* 399 */     int dNum = 0;
/* 400 */     if (this.refBtn.getSelection()) {
/* 401 */       dNum = getDiscussionNumber();
/*     */     }
/*     */ 
/* 404 */     Calendar eTime = null;
/* 405 */     if (this.expBtn.getSelection()) {
/* 406 */       eTime = getExpirationTime();
/*     */     }
/*     */ 
/* 409 */     this.wb.addStatus(getContinueText(), dNum, getValidTime(), eTime, this.forecaster.getText());
/*     */   }
/*     */ 
/*     */   private String generateStatusMsg()
/*     */   {
/* 418 */     String msg = "";
/* 419 */     msg = msg + "WOUS20 KWNS " + String.format("%1$td%1$tH%1$tM\n", new Object[] { getValidTime() }) + "\n" + 
/* 420 */       "WWASPC" + "\n" + 
/* 421 */       "SPC WW-A " + String.format("%1$td%1$tH%1$tM\n", new Object[] { getValidTime() }) + "\n";
/*     */ 
/* 423 */     String stateLine = "";
/* 424 */     for (String st : this.wb.getStates()) {
/* 425 */       stateLine = stateLine + st + "Z000-";
/*     */     }
/*     */ 
/* 428 */     stateLine = stateLine + String.format("%1$td%1$tH%1$tM", new Object[] { getExpirationTime() }) + "-" + "\n";
/*     */ 
/* 430 */     msg = msg + stateLine + "\n" + 
/* 431 */       "STATUS REPORT ON WW " + getWatchNumber() + "\n\n" + 
/* 432 */       PgenToolUtils.wrapWatchText(new StringBuilder("THE SEVERE WEATHER THREAT ").append(getContinueText()).toString(), 65) + "\n\n";
/*     */ 
/* 434 */     if (this.expBtn.getSelection()) {
/* 435 */       msg = msg + "WW " + this.wb.getWatchNumber() + " WILL BE ALLOWED TO EXPIRE AT " + 
/* 436 */         String.format("%1$td%1$tH%1$tM", new Object[] { getExpirationTime() }) + "Z." + "\n\n";
/*     */     }
/*     */ 
/* 439 */     if (this.refBtn.getSelection()) {
/* 440 */       msg = msg + "FOR ADDITIONAL INFORMATION SEE MESOSCALE DISCUSSION " + 
/* 441 */         String.format("%1$04d", new Object[] { Integer.valueOf(getDiscussionNumber()) }) + "\n\n" + 
/* 442 */         ".." + this.forecaster.getText().toUpperCase() + ".." + 
/* 443 */         String.format("%1$tm/%1$td/%1$ty", new Object[] { getExpirationTime() }) + 
/* 444 */         "\n\n";
/*     */     }
/*     */ 
/* 447 */     msg = msg + "ATTN...WFO...";
/*     */ 
/* 449 */     for (String wfo : this.wb.getWFOs()) {
/* 450 */       msg = msg + wfo + "...";
/*     */     }
/*     */ 
/* 453 */     msg = msg + "\n\n&&\n\nSTATUS REPORT FOR WT " + this.wb.getWatchNumber() + "\n\n";
/*     */ 
/* 455 */     msg = msg + "SEVERE WEATHER THREAT CONTINUES FOR THE FOLLOWING AREAS\n\n";
/*     */ 
/* 457 */     for (String state : this.wb.getStates()) {
/* 458 */       msg = msg + this.wb.createCountyInfo(state, getExpirationTime());
/* 459 */       msg = msg + "$$\n\n\n";
/*     */     }
/*     */ 
/* 462 */     msg = msg + "THE WATCH STATUS MESSAGE IS FOR GUIDANCE PURPOSES ONLY. PLEASE\nREFER TO WATCH COUNTY NOTIFICATION STATEMENTS FOR OFFICIAL\nINFORMATION ON COUNTIES...INDEPENDENT CITIES AND MARINE ZONES\nCLEARED FROM SEVERE THUNDERSTORM AND TORNADO WATCHES.\n$$\n";
/*     */ 
/* 468 */     return msg;
/*     */   }
/*     */ 
/*     */   private String getContinueText()
/*     */   {
/* 477 */     String fromLn = "";
/* 478 */     if (this.lnTxt != null) {
/* 479 */       fromLn = "CONTINUES TO THE " + this.dirCombo.getText() + " OF THE LINE " + 
/* 480 */         this.lnTxt.getText();
/*     */     }
/*     */     else {
/* 483 */       fromLn = "CONTINUES ACROSS ENTIRE AREA";
/*     */     }
/*     */ 
/* 486 */     return fromLn;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchStatusDlg
 * JD-Core Version:    0.6.2
 */