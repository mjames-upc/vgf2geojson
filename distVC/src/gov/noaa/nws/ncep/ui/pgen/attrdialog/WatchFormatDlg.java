/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.events.SelectionListener;
/*     */ import org.eclipse.swt.events.VerifyEvent;
/*     */ import org.eclipse.swt.events.VerifyListener;
/*     */ import org.eclipse.swt.layout.FormAttachment;
/*     */ import org.eclipse.swt.layout.FormData;
/*     */ import org.eclipse.swt.layout.FormLayout;
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
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class WatchFormatDlg extends CaveJFACEDialog
/*     */ {
/*     */   private WatchFormatMsgDlg msgDlg;
/*  68 */   private static WatchFormatDlg INSTANCE = null;
/*     */   private WatchBox wb;
/*     */   private WatchBoxAttrDlg wbDlg;
/*     */   private Composite top;
/*     */   private Button testBtn;
/*     */   private Button actvBtn;
/*     */   private Button tornadoBtn;
/*     */   private Button stormBtn;
/*     */   private Button estBtn;
/*     */   private Button cstBtn;
/*     */   private Button mstBtn;
/*     */   private Button pstBtn;
/*     */   private Button edtBtn;
/*     */   private Button cdtBtn;
/*     */   private Button mdtBtn;
/*     */   private Button pdtBtn;
/*     */   private Button normalBtn;
/*     */   private Button pdsBtn;
/*     */   private Text watchNumber;
/*     */   private Combo forecasterCombo;
/*  99 */   private static String[] FORECASTERS = null;
/*     */   private DateTime validDate;
/*     */   private Text validTime;
/*     */   private Combo hailCombo;
/* 107 */   public static String[] HAILSIZE = { "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0" };
/*     */   private Combo windCombo;
/* 111 */   public static String[] WINDGUST = { "50", "60", "70", "80", "90", "100" };
/*     */   private Combo topCombo;
/* 115 */   public static String[] TOPLEVEL = { "300", "350", "400", "450", "500", "550", "600", "650", "700" };
/*     */   private Combo dirCombo;
/* 119 */   public static String[] MOVEDIR = { "180", "190", "200", "210", "220", "230", "240", "250", "260", 
/* 120 */     "270", "280", "290", "300", "310", "320", "330", "340", "350", "360" };
/*     */   private Combo spdCombo;
/* 122 */   public static String[] MOVESPD = { "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70" };
/*     */   public static final String S_STORM = "SEVERE THUNDERSTORM";
/*     */   public static final String TORNADO = "TORNADO";
/* 127 */   private static final Color STORM_COLOR = Color.CYAN;
/* 128 */   private static final Color TORNADO_COLOR = Color.RED;
/*     */   private Text rText;
/*     */   private Text cText;
/*     */   private Text states;
/*     */ 
/*     */   protected WatchFormatDlg(Shell parentShell, WatchBoxAttrDlg wbDlg)
/*     */   {
/* 146 */     super(parentShell);
/* 147 */     setShellStyle(96);
/*     */ 
/* 149 */     setWbDlg(wbDlg);
/*     */ 
/* 151 */     WatchCoordDlg.readForecasterTbl();
/* 152 */     FORECASTERS = WatchCoordDlg.getForecasters();
/*     */   }
/*     */ 
/*     */   public static WatchFormatDlg getInstance(Shell parShell, WatchBoxAttrDlg wbDlg)
/*     */   {
/* 162 */     if (INSTANCE == null)
/*     */     {
/* 164 */       INSTANCE = new WatchFormatDlg(parShell, wbDlg);
/*     */     }
/*     */ 
/* 168 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 178 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 181 */     GridLayout mainLayout = new GridLayout(2, false);
/* 182 */     mainLayout.marginHeight = 3;
/* 183 */     mainLayout.marginWidth = 3;
/* 184 */     mainLayout.verticalSpacing = 3;
/* 185 */     this.top.setLayout(mainLayout);
/*     */ 
/* 188 */     initializeComponents();
/*     */ 
/* 190 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void initializeComponents()
/*     */   {
/* 200 */     getShell().setText("Format Watch");
/*     */ 
/* 203 */     Label statusLbl = new Label(this.top, 16384);
/* 204 */     statusLbl.setText("Issue Status:");
/*     */ 
/* 206 */     Composite status = new Composite(this.top, 0);
/* 207 */     GridLayout btnGl = new GridLayout(2, false);
/* 208 */     status.setLayout(btnGl);
/*     */ 
/* 210 */     this.testBtn = new Button(status, 16);
/* 211 */     this.testBtn.setText("Test");
/* 212 */     this.actvBtn = new Button(status, 16);
/* 213 */     this.actvBtn.setText("Active");
/* 214 */     this.actvBtn.setSelection(true);
/*     */ 
/* 217 */     Label wnLbl = new Label(this.top, 16384);
/* 218 */     wnLbl.setText("Watch Number:");
/* 219 */     this.watchNumber = new Text(this.top, 133124);
/*     */ 
/* 221 */     this.watchNumber.addVerifyListener(new VerifyListener()
/*     */     {
/*     */       public void verifyText(VerifyEvent e)
/*     */       {
/* 225 */         e.doit = PgenUtil.validatePositiveInteger(e);
/* 226 */         if (!e.doit) Display.getCurrent().beep();
/*     */       }
/*     */     });
/* 231 */     Label timeLbl = new Label(this.top, 16384);
/* 232 */     timeLbl.setText("Expiration Time:");
/*     */ 
/* 234 */     Composite dt = new Composite(this.top, 0);
/* 235 */     GridLayout dtGl = new GridLayout(2, false);
/* 236 */     dt.setLayout(dtGl);
/* 237 */     this.validDate = new DateTime(dt, 2080);
/*     */ 
/* 239 */     Calendar expTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 240 */     this.validDate.setYear(expTime.get(1));
/* 241 */     this.validDate.setMonth(expTime.get(2));
/* 242 */     this.validDate.setDay(expTime.get(5));
/*     */ 
/* 244 */     Composite c1 = new Composite(dt, 0);
/* 245 */     c1.setLayout(new FormLayout());
/*     */ 
/* 247 */     this.validTime = new Text(c1, 16779268);
/*     */ 
/* 249 */     FormData fd = new FormData();
/* 250 */     fd.top = new FormAttachment(this.watchNumber, 2, 1024);
/* 251 */     fd.left = new FormAttachment(this.validDate, 5, 131072);
/* 252 */     this.validTime.setLayoutData(fd);
/* 253 */     PgenUtil.setUTCTimeTextField(c1, this.validTime, expTime, this.watchNumber, 5);
/*     */ 
/* 256 */     Label typeLbl = new Label(this.top, 16384);
/* 257 */     typeLbl.setText("Watch Type:");
/*     */ 
/* 259 */     Composite wType = new Composite(this.top, 0);
/* 260 */     wType.setLayout(btnGl);
/*     */ 
/* 262 */     this.stormBtn = new Button(wType, 16);
/* 263 */     this.stormBtn.setText("Svr T'STORM");
/*     */ 
/* 265 */     this.tornadoBtn = new Button(wType, 16);
/* 266 */     this.tornadoBtn.setText("Tornado");
/*     */ 
/* 269 */     Label sevLbl = new Label(this.top, 16384);
/* 270 */     sevLbl.setText("Severity:");
/*     */ 
/* 272 */     Composite sev = new Composite(this.top, 0);
/* 273 */     sev.setLayout(btnGl);
/*     */ 
/* 275 */     this.normalBtn = new Button(sev, 16);
/* 276 */     this.normalBtn.setText("Normal");
/* 277 */     this.normalBtn.setSelection(true);
/* 278 */     this.pdsBtn = new Button(sev, 16);
/* 279 */     this.pdsBtn.setText("PDS");
/*     */ 
/* 282 */     Label zoneLbl = new Label(this.top, 16384);
/* 283 */     zoneLbl.setText("Time Zone:");
/*     */ 
/* 285 */     Composite zone = new Composite(this.top, 0);
/* 286 */     GridLayout zoneGl = new GridLayout(4, false);
/* 287 */     zone.setLayout(zoneGl);
/*     */ 
/* 289 */     this.estBtn = new Button(zone, 16);
/* 290 */     this.estBtn.setText("EST");
/* 291 */     this.cstBtn = new Button(zone, 16);
/* 292 */     this.cstBtn.setText("CST");
/* 293 */     this.mstBtn = new Button(zone, 16);
/* 294 */     this.mstBtn.setText("MST");
/* 295 */     this.pstBtn = new Button(zone, 16);
/* 296 */     this.pstBtn.setText("PST");
/*     */ 
/* 298 */     this.edtBtn = new Button(zone, 16);
/* 299 */     this.edtBtn.setText("EDT");
/* 300 */     this.cdtBtn = new Button(zone, 16);
/* 301 */     this.cdtBtn.setText("CDT");
/* 302 */     this.mdtBtn = new Button(zone, 16);
/* 303 */     this.mdtBtn.setText("MDT");
/* 304 */     this.pdtBtn = new Button(zone, 16);
/* 305 */     this.pdtBtn.setText("PDT");
/*     */ 
/* 308 */     Label hailLbl = new Label(this.top, 16384);
/* 309 */     hailLbl.setText("Max Hail Size (in.):");
/*     */ 
/* 311 */     this.hailCombo = new Combo(this.top, 6);
/* 312 */     this.hailCombo.setLayoutData(new GridData(80, 30));
/*     */ 
/* 314 */     for (String hailSize : HAILSIZE) {
/* 315 */       this.hailCombo.add(hailSize);
/*     */     }
/*     */ 
/* 318 */     this.hailCombo.select(3);
/*     */ 
/* 322 */     Label windLbl = new Label(this.top, 16384);
/* 323 */     windLbl.setText("Max Wind Gust (kts):");
/*     */ 
/* 325 */     this.windCombo = new Combo(this.top, 6);
/* 326 */     this.windCombo.setLayoutData(new GridData(80, 30));
/*     */ 
/* 328 */     for (String wind : WINDGUST) {
/* 329 */       this.windCombo.add(wind);
/*     */     }
/*     */ 
/* 332 */     this.windCombo.select(1);
/*     */ 
/* 335 */     Label topLbl = new Label(this.top, 16384);
/* 336 */     topLbl.setText("Max Top (100 feet):");
/*     */ 
/* 338 */     this.topCombo = new Combo(this.top, 6);
/* 339 */     this.topCombo.setLayoutData(new GridData(80, 30));
/*     */ 
/* 341 */     for (String top : TOPLEVEL) {
/* 342 */       this.topCombo.add(top);
/*     */     }
/*     */ 
/* 345 */     this.topCombo.select(4);
/*     */ 
/* 348 */     Label moveLbl = new Label(this.top, 16384);
/* 349 */     moveLbl.setText("Motion Vector(deg):");
/*     */ 
/* 351 */     Composite moveVec = new Composite(this.top, 0);
/* 352 */     GridLayout moveLayout = new GridLayout(3, false);
/* 353 */     moveVec.setLayout(moveLayout);
/*     */ 
/* 355 */     this.dirCombo = new Combo(moveVec, 6);
/* 356 */     this.dirCombo.setLayoutData(new GridData(80, 30));
/* 357 */     for (String st : MOVEDIR) {
/* 358 */       this.dirCombo.add(st);
/*     */     }
/*     */ 
/* 361 */     this.dirCombo.select(6);
/*     */ 
/* 363 */     Label spdLbl = new Label(moveVec, 16384);
/* 364 */     spdLbl.setText("(kts):");
/*     */ 
/* 366 */     this.spdCombo = new Combo(moveVec, 6);
/* 367 */     this.spdCombo.setLayoutData(new GridData(80, 30));
/* 368 */     for (String st : MOVESPD) {
/* 369 */       this.spdCombo.add(st);
/*     */     }
/*     */ 
/* 372 */     this.spdCombo.select(5);
/*     */ 
/* 375 */     Label stLabel = new Label(this.top, 0);
/* 376 */     stLabel.setText("States Included:");
/* 377 */     this.states = new Text(this.top, 2);
/* 378 */     this.states.setEditable(false);
/*     */ 
/* 380 */     String stStr = "";
/* 381 */     for (??? = this.wb.getStates().iterator(); ((Iterator)???).hasNext(); ) { String str = (String)((Iterator)???).next();
/* 382 */       stStr = stStr + str + " ";
/*     */     }
/*     */ 
/* 385 */     this.states.setText(stStr);
/*     */ 
/* 388 */     Label replaceLbl = new Label(this.top, 16384);
/* 389 */     replaceLbl.setText("Replace Watch#:");
/* 390 */     this.rText = new Text(this.top, 133124);
/*     */ 
/* 392 */     this.rText.addVerifyListener(new VerifyListener()
/*     */     {
/*     */       public void verifyText(VerifyEvent e)
/*     */       {
/* 396 */         e.doit = PgenUtil.validatePositiveInteger(e);
/* 397 */         if (!e.doit) Display.getCurrent().beep();
/*     */       }
/*     */     });
/* 402 */     List contWatch = PgenStaticDataProvider.getProvider().loadContWatchNum();
/* 403 */     String cont = "";
/* 404 */     if ((contWatch != null) && (!contWatch.isEmpty())) {
/* 405 */       for (int i = 0; i < contWatch.size(); i++) {
/* 406 */         cont = cont + (String)contWatch.get(i) + " ";
/*     */       }
/* 408 */       if (cont.endsWith(" "))
/* 409 */         cont = cont.substring(0, cont.length() - 1);
/*     */     }
/*     */     else
/*     */     {
/* 413 */       cont = "0000";
/*     */     }
/*     */ 
/* 416 */     Label continueLbl = new Label(this.top, 16384);
/* 417 */     continueLbl.setText("Continue Watch#:");
/* 418 */     this.cText = new Text(this.top, 133124);
/*     */ 
/* 420 */     this.cText.setText(cont);
/* 421 */     this.cText.setEditable(true);
/*     */ 
/* 434 */     Label forecasterLbl = new Label(this.top, 16384);
/* 435 */     forecasterLbl.setText("Forecaster:");
/* 436 */     this.forecasterCombo = new Combo(this.top, 4);
/* 437 */     for (String str : FORECASTERS) {
/* 438 */       this.forecasterCombo.add(str);
/*     */     }
/*     */ 
/* 441 */     AttrDlg.addSeparator(this.top);
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 451 */     if (getShell() == null) {
/* 452 */       create();
/*     */     }
/*     */ 
/* 455 */     getShell().setLocation(getShell().getParent().getLocation());
/*     */ 
/* 457 */     initDlg();
/*     */ 
/* 459 */     return super.open();
/*     */   }
/*     */ 
/*     */   private Calendar getExpirationTime()
/*     */   {
/* 468 */     Calendar expiration = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 469 */     expiration.set(this.validDate.getYear(), this.validDate.getMonth(), this.validDate.getDay(), 
/* 470 */       getExpHour(), getExpMinute(), 0);
/* 471 */     expiration.set(14, 0);
/* 472 */     return expiration;
/*     */   }
/*     */ 
/*     */   private int getExpHour()
/*     */   {
/* 479 */     int ret = 0;
/*     */     try {
/* 481 */       String hm = this.validTime.getText();
/* 482 */       ret = Integer.parseInt(hm.substring(0, hm.length() == 4 ? 2 : 1));
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 487 */     return ret;
/*     */   }
/*     */ 
/*     */   private int getExpMinute()
/*     */   {
/* 494 */     int ret = 0;
/*     */     try {
/* 496 */       String hm = this.validTime.getText();
/* 497 */       ret = Integer.parseInt(hm.substring(hm.length() == 4 ? 2 : 1), hm.length() - 1);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 502 */     return ret;
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 512 */     String err = checkErr();
/* 513 */     if (!err.isEmpty()) {
/* 514 */       MessageDialog infoDlg = new MessageDialog(
/* 515 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 516 */         "Warning!", null, err, 
/* 517 */         2, new String[] { "OK" }, 0);
/* 518 */       infoDlg.open();
/*     */     }
/*     */     else {
/* 521 */       applyOnWB(this.wb);
/* 522 */       this.wbDlg.drawingLayer.resetElement(this.wb);
/* 523 */       this.wbDlg.mapEditor.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 533 */     GridLayout barGl = new GridLayout(3, false);
/* 534 */     parent.setLayout(barGl);
/*     */ 
/* 536 */     Button contBtn = new Button(parent, 8);
/*     */ 
/* 538 */     super.createButtonsForButtonBar(parent);
/*     */ 
/* 540 */     contBtn.setText("Continue");
/* 541 */     contBtn.setLayoutData(getButton(1).getLayoutData());
/* 542 */     contBtn.addSelectionListener(new SelectionListener()
/*     */     {
/*     */       public void widgetDefaultSelected(SelectionEvent e)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 552 */         String err = WatchFormatDlg.this.checkErr();
/* 553 */         if (!err.isEmpty()) {
/* 554 */           MessageDialog infoDlg = new MessageDialog(
/* 555 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 556 */             "Warning!", null, err, 
/* 557 */             2, new String[] { "OK" }, 0);
/* 558 */           infoDlg.open();
/*     */         }
/*     */         else {
/* 561 */           WatchFormatDlg.this.applyOnWB(WatchFormatDlg.this.wb);
/* 562 */           WatchFormatDlg.this.wbDlg.drawingLayer.resetElement(WatchFormatDlg.this.wb);
/* 563 */           WatchFormatDlg.this.wbDlg.mapEditor.refresh();
/* 564 */           WatchFormatDlg.this.msgDlg = new WatchFormatMsgDlg(WatchFormatDlg.this.getParentShell(), 
/* 565 */             WatchFormatDlg.this);
/* 566 */           WatchFormatDlg.this.msgDlg.setBlockOnOpen(false);
/*     */ 
/* 568 */           WatchFormatDlg.this.msgDlg.open();
/*     */         }
/*     */       }
/*     */     });
/* 574 */     getButton(0).setText("Apply");
/*     */   }
/*     */ 
/*     */   private String checkErr()
/*     */   {
/* 582 */     boolean showErr = false;
/* 583 */     String err = "The following Problems have been identified:\n\n";
/* 584 */     if (this.watchNumber.getText().isEmpty()) {
/* 585 */       showErr = true;
/* 586 */       err = err + "\tWatch number is invalid.\n";
/*     */     }
/*     */ 
/* 589 */     if ((!this.stormBtn.getSelection()) && (!this.tornadoBtn.getSelection())) {
/* 590 */       showErr = true;
/* 591 */       err = err + "\tWeather type is not selected.\n";
/*     */     }
/* 593 */     if (getTimeZone().isEmpty()) {
/* 594 */       showErr = true;
/* 595 */       err = err + "\tTime zone is not selected.\n";
/*     */     }
/* 597 */     if (this.forecasterCombo.getText().isEmpty()) {
/* 598 */       showErr = true;
/* 599 */       err = err + "\tForecaster is not selected.";
/*     */     }
/*     */ 
/* 602 */     if (showErr) {
/* 603 */       return err;
/*     */     }
/* 605 */     return "";
/*     */   }
/*     */ 
/*     */   private String getTimeZone()
/*     */   {
/* 614 */     if (this.estBtn.getSelection()) {
/* 615 */       return this.estBtn.getText();
/*     */     }
/* 617 */     if (this.cstBtn.getSelection()) {
/* 618 */       return this.cstBtn.getText();
/*     */     }
/* 620 */     if (this.mstBtn.getSelection()) {
/* 621 */       return this.mstBtn.getText();
/*     */     }
/* 623 */     if (this.pstBtn.getSelection()) {
/* 624 */       return this.pstBtn.getText();
/*     */     }
/* 626 */     if (this.edtBtn.getSelection()) {
/* 627 */       return this.edtBtn.getText();
/*     */     }
/* 629 */     if (this.cdtBtn.getSelection()) {
/* 630 */       return this.cdtBtn.getText();
/*     */     }
/* 632 */     if (this.mdtBtn.getSelection()) {
/* 633 */       return this.mdtBtn.getText();
/*     */     }
/* 635 */     if (this.pdtBtn.getSelection()) {
/* 636 */       return this.pdtBtn.getText();
/*     */     }
/*     */ 
/* 639 */     return "";
/*     */   }
/*     */ 
/*     */   private void setTimeZone(String zone)
/*     */   {
/* 649 */     if (zone == null) return;
/*     */ 
/* 651 */     if (zone.equalsIgnoreCase("est")) {
/* 652 */       this.estBtn.setSelection(true);
/*     */     }
/* 654 */     if (zone.equalsIgnoreCase("cst")) {
/* 655 */       this.cstBtn.setSelection(true);
/*     */     }
/* 657 */     if (zone.equalsIgnoreCase("mst")) {
/* 658 */       this.mstBtn.setSelection(true);
/*     */     }
/* 660 */     if (zone.equalsIgnoreCase("pst")) {
/* 661 */       this.pstBtn.setSelection(true);
/*     */     }
/* 663 */     if (zone.equalsIgnoreCase("edt")) {
/* 664 */       this.edtBtn.setSelection(true);
/*     */     }
/* 666 */     if (zone.equalsIgnoreCase("cdt")) {
/* 667 */       this.cdtBtn.setSelection(true);
/*     */     }
/* 669 */     if (zone.equalsIgnoreCase("mdt")) {
/* 670 */       this.mdtBtn.setSelection(true);
/*     */     }
/* 672 */     if (zone.equalsIgnoreCase("pdt"))
/* 673 */       this.pdtBtn.setSelection(true);
/*     */   }
/*     */ 
/*     */   private String getWatchType()
/*     */   {
/* 682 */     String type = "";
/* 683 */     if (this.stormBtn.getSelection()) {
/* 684 */       type = "SEVERE THUNDERSTORM";
/*     */     }
/* 686 */     else if (this.tornadoBtn.getSelection()) {
/* 687 */       type = "TORNADO";
/*     */     }
/* 689 */     return type;
/*     */   }
/*     */ 
/*     */   private String getStatus()
/*     */   {
/* 697 */     String type = "";
/* 698 */     if (this.testBtn.getSelection()) {
/* 699 */       type = this.testBtn.getText();
/*     */     }
/* 701 */     else if (this.actvBtn.getSelection()) {
/* 702 */       type = this.actvBtn.getText();
/*     */     }
/* 704 */     return type;
/*     */   }
/*     */ 
/*     */   private String getPdsType()
/*     */   {
/* 712 */     String type = "";
/* 713 */     if (this.pdsBtn.getSelection()) {
/* 714 */       type = "PDS";
/*     */     }
/* 716 */     else if (this.normalBtn.getSelection()) {
/* 717 */       type = "NORMAL";
/*     */     }
/* 719 */     return type;
/*     */   }
/*     */ 
/*     */   public int getWatchNumber()
/*     */   {
/* 727 */     if ((this.watchNumber.getText() == null) || (this.watchNumber.getText().isEmpty())) {
/* 728 */       return 0;
/*     */     }
/*     */ 
/* 731 */     return Integer.valueOf(this.watchNumber.getText()).intValue();
/*     */   }
/*     */ 
/*     */   private String getContNumber()
/*     */   {
/* 736 */     if ((this.cText.getText() == null) || (this.cText.getText().isEmpty())) {
/* 737 */       return "";
/*     */     }
/*     */ 
/* 740 */     return this.cText.getText();
/*     */   }
/*     */ 
/*     */   private int getReplaceNumber()
/*     */   {
/* 745 */     if ((this.rText.getText() == null) || (this.rText.getText().isEmpty())) {
/* 746 */       return 0;
/*     */     }
/*     */ 
/* 749 */     return Integer.valueOf(this.rText.getText()).intValue();
/*     */   }
/*     */ 
/*     */   private void applyOnWB(WatchBox wb)
/*     */   {
/* 757 */     wb.setIssueStatus(getStatus());
/* 758 */     wb.setIssueTime(Calendar.getInstance(TimeZone.getTimeZone("GMT")));
/* 759 */     wb.setExpTime(getExpirationTime());
/* 760 */     wb.setSeverity(getPdsType());
/* 761 */     wb.setTimeZone(getTimeZone());
/* 762 */     wb.setHailSize(Float.valueOf(this.hailCombo.getText()).floatValue());
/* 763 */     wb.setGust(Integer.valueOf(this.windCombo.getText()).intValue());
/* 764 */     wb.setTop(Integer.valueOf(this.topCombo.getText()).intValue());
/* 765 */     wb.setMoveDir(Integer.valueOf(this.dirCombo.getText()).intValue());
/* 766 */     wb.setMoveSpeed(Integer.valueOf(this.spdCombo.getText()).intValue());
/* 767 */     wb.setStatesIncl(this.states.getText());
/* 768 */     wb.setAdjAreas("");
/* 769 */     wb.setIssueFlag(1);
/* 770 */     wb.setWatchType(getWatchType());
/* 771 */     wb.setWatchNumber(Integer.valueOf(this.watchNumber.getText()).intValue());
/* 772 */     wb.setForecaster(this.forecasterCombo.getText());
/* 773 */     wb.setEndPointAnc(String.format("%1$s - %2$s", new Object[] { wb.getRelative((Coordinate)wb.getPoints().get(0), wb.getAnchors()[0]), 
/* 774 */       wb.getRelative((Coordinate)wb.getPoints().get(4), wb.getAnchors()[1]) }));
/* 775 */     wb.setEndPointVor(String.format("%1$s - %2$s", new Object[] { wb.getRelative((Coordinate)wb.getPoints().get(0), wb.getNearestVor((Coordinate)wb.getPoints().get(0))), 
/* 776 */       wb.getRelative((Coordinate)wb.getPoints().get(4), wb.getNearestVor((Coordinate)wb.getPoints().get(4))) }));
/* 777 */     wb.setHalfWidthSm((int)Math.round(wb.getHalfWidth() / 1609.3399658203125D));
/* 778 */     wb.setHalfWidthNm((int)Math.round(wb.getHalfWidth() / 1852.0D / 5.0D) * 5);
/* 779 */     wb.setWathcAreaNm((int)Math.round(wb.getWatchArea()));
/* 780 */     wb.setCntyInfo(wb.formatCountyInfo(wb.getCountyList()));
/*     */ 
/* 782 */     if (wb.getWatchType().equalsIgnoreCase("SEVERE THUNDERSTORM")) {
/* 783 */       wb.setColors(new Color[] { STORM_COLOR, STORM_COLOR });
/*     */     }
/* 785 */     else if (wb.getWatchType().equalsIgnoreCase("TORNADO")) {
/* 786 */       wb.setColors(new Color[] { TORNADO_COLOR, TORNADO_COLOR });
/*     */     }
/*     */ 
/* 789 */     wb.setContWatch(getContNumber());
/* 790 */     wb.setReplWatch(getReplaceNumber());
/*     */   }
/*     */ 
/*     */   private void initDlg()
/*     */   {
/* 798 */     if (this.wb.getIssueFlag() != 0)
/*     */     {
/* 800 */       if (this.wb.getIssueStatus() != null) {
/* 801 */         if (this.wb.getIssueStatus().equalsIgnoreCase("Test")) {
/* 802 */           this.testBtn.setSelection(true);
/*     */         }
/* 804 */         else if (this.wb.getIssueStatus().equalsIgnoreCase("Active")) {
/* 805 */           this.actvBtn.setSelection(true);
/*     */         }
/*     */       }
/*     */ 
/* 809 */       this.watchNumber.setText(String.valueOf(this.wb.getWatchNumber()));
/*     */ 
/* 811 */       setTimeZone(this.wb.getTimeZone());
/*     */ 
/* 813 */       this.hailCombo.setText(String.valueOf(this.wb.getHailSize()));
/* 814 */       this.windCombo.setText(String.valueOf(this.wb.getGust()));
/* 815 */       this.topCombo.setText(String.valueOf(this.wb.getTop()));
/* 816 */       this.dirCombo.setText(String.valueOf(this.wb.getMoveDir()));
/* 817 */       this.spdCombo.setText(String.valueOf(this.wb.getMoveSpeed()));
/*     */ 
/* 819 */       this.cText.setText(String.valueOf(this.wb.getContWatch()));
/*     */     }
/*     */ 
/* 823 */     Calendar exp = this.wb.getExpTime();
/* 824 */     if (exp != null) {
/* 825 */       this.validDate.setDate(exp.get(1), exp.get(2), exp.get(5));
/* 826 */       this.validTime.setText(PgenUtil.getInitialTime(exp));
/*     */     }
/*     */ 
/* 830 */     if (this.wb.getWatchType() != null) {
/* 831 */       if (this.wb.getWatchType().equalsIgnoreCase("Tornado")) {
/* 832 */         this.tornadoBtn.setSelection(true);
/*     */       }
/* 834 */       else if (this.wb.getWatchType().equalsIgnoreCase("SEVERE THUNDERSTORM")) {
/* 835 */         this.stormBtn.setSelection(true);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 840 */     this.rText.setText(String.valueOf(this.wb.getReplWatch()));
/*     */ 
/* 842 */     if (this.wb.getForecaster() != null)
/* 843 */       this.forecasterCombo.setText(this.wb.getForecaster());
/*     */   }
/*     */ 
/*     */   public WatchBox getWatchBox()
/*     */   {
/* 851 */     return this.wb;
/*     */   }
/*     */ 
/*     */   public void setWatchBox(WatchBox wb)
/*     */   {
/* 859 */     this.wb = wb;
/*     */   }
/*     */ 
/*     */   public void setWbDlg(WatchBoxAttrDlg wbDlg)
/*     */   {
/* 867 */     this.wbDlg = wbDlg;
/*     */   }
/*     */ 
/*     */   public WatchBoxAttrDlg getWbDlg()
/*     */   {
/* 875 */     return this.wbDlg;
/*     */   }
/*     */ 
/*     */   public boolean close()
/*     */   {
/* 883 */     if (this.msgDlg != null) this.msgDlg.close();
/* 884 */     return super.close();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchFormatDlg
 * JD-Core Version:    0.6.2
 */