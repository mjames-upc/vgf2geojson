/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureDialog;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenToolUtils;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.Node;
/*     */ import org.dom4j.io.SAXReader;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.swt.layout.FormAttachment;
/*     */ import org.eclipse.swt.layout.FormData;
/*     */ import org.eclipse.swt.layout.FormLayout;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.DateTime;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class WatchCoordDlg extends CaveJFACEDialog
/*     */ {
/*     */   static final int LINE_LEN = 65;
/*     */   public static final String PGEN_FORECASTER = "forecasters.xml";
/*  73 */   private static WatchCoordDlg INSTANCE = null;
/*     */ 
/*  76 */   private static final String[] ID_LIST = { "A", "B", "C", "D", 
/*  77 */     "E", "F", "G", "H", "I", "J" };
/*     */ 
/*  80 */   private static final String[] PHONE_LIST = { "1-800-5551111", 
/*  81 */     "1-800-5552222", "1-800-5553333", "1-800-5554444", "1-800-5555555", 
/*  82 */     "1-800-5556666", "1-800-5557777", "1-800-5558888", "1-800-5559999" };
/*     */   private WatchBoxAttrDlg wbDlg;
/*     */   private Composite top;
/*     */   private Button tornadoBtn;
/*     */   private Button stormBtn;
/*     */   private Combo forecasterCombo;
/*  98 */   private static Document forecasterTbl = null;
/*     */ 
/* 100 */   private static String[] FORECASTERS = null;
/*     */ 
/* 102 */   private static String FORECASTER_XPATH = "/forecasters/forecaster";
/*     */   private DateTime validDate;
/*     */   private Text validTime;
/*     */   private Combo phoneCombo;
/*     */   private Combo idCombo;
/*     */   private Text proposedWFOs;
/*     */   private Composite wfoPane;
/*     */   private List<Button> wfoBtns;
/*     */   private String dirPath;
/*     */   private String dataURI;
/*     */   private Text rText;
/*     */ 
/*     */   protected WatchCoordDlg(Shell parentShell, WatchBoxAttrDlg wbDlg)
/*     */   {
/* 137 */     super(parentShell);
/* 138 */     setShellStyle(96);
/*     */ 
/* 140 */     this.wbDlg = wbDlg;
/* 141 */     this.wfoBtns = new ArrayList();
/*     */ 
/* 143 */     forecasterTbl = readForecasterTbl();
/* 144 */     FORECASTERS = getForecasters();
/*     */   }
/*     */ 
/*     */   public static WatchCoordDlg getInstance(Shell parShell, WatchBoxAttrDlg wbDlg)
/*     */   {
/* 157 */     if (INSTANCE == null)
/*     */     {
/* 159 */       INSTANCE = new WatchCoordDlg(parShell, wbDlg);
/*     */     }
/*     */ 
/* 163 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 173 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 176 */     GridLayout mainLayout = new GridLayout(1, false);
/* 177 */     mainLayout.marginHeight = 3;
/* 178 */     mainLayout.marginWidth = 3;
/* 179 */     mainLayout.verticalSpacing = 3;
/* 180 */     this.top.setLayout(mainLayout);
/*     */ 
/* 183 */     initializeComponents();
/*     */ 
/* 185 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void initializeComponents()
/*     */   {
/* 195 */     getShell().setText("Watch Coordination");
/*     */ 
/* 197 */     Composite panel1 = new Composite(this.top, 0);
/* 198 */     GridLayout pLayout = new GridLayout(2, false);
/* 199 */     pLayout.marginHeight = 3;
/* 200 */     pLayout.marginWidth = 3;
/* 201 */     pLayout.verticalSpacing = 3;
/* 202 */     panel1.setLayout(pLayout);
/*     */ 
/* 205 */     Label idLbl = new Label(panel1, 16384);
/* 206 */     idLbl.setText("Preliminiary ID:");
/*     */ 
/* 208 */     this.idCombo = new Combo(panel1, 12);
/* 209 */     for (String st : ID_LIST) {
/* 210 */       this.idCombo.add(st);
/*     */     }
/*     */ 
/* 213 */     this.idCombo.select(0);
/*     */ 
/* 216 */     Label typeLbl = new Label(panel1, 16384);
/* 217 */     typeLbl.setText("Watch Type:");
/*     */ 
/* 219 */     Composite wType = new Composite(panel1, 0);
/* 220 */     GridLayout btnGl = new GridLayout(2, false);
/* 221 */     wType.setLayout(btnGl);
/*     */ 
/* 223 */     this.stormBtn = new Button(wType, 16);
/* 224 */     this.stormBtn.setText("Svr T'Storm");
/* 225 */     this.tornadoBtn = new Button(wType, 16);
/* 226 */     this.tornadoBtn.setText("Tornado");
/*     */ 
/* 229 */     Label timeLbl = new Label(panel1, 16384);
/* 230 */     timeLbl.setText("Expiration Time:");
/*     */ 
/* 232 */     Composite dt = new Composite(panel1, 0);
/* 233 */     GridLayout dtGl = new GridLayout(2, false);
/* 234 */     dt.setLayout(dtGl);
/* 235 */     this.validDate = new DateTime(dt, 2080);
/*     */ 
/* 238 */     Calendar expTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 239 */     this.validDate.setYear(expTime.get(1));
/* 240 */     this.validDate.setMonth(expTime.get(2));
/* 241 */     this.validDate.setDay(expTime.get(5));
/*     */ 
/* 243 */     Composite c1 = new Composite(dt, 0);
/* 244 */     c1.setLayout(new FormLayout());
/*     */ 
/* 246 */     this.validTime = new Text(c1, 16779268);
/*     */ 
/* 248 */     FormData fd = new FormData();
/*     */ 
/* 250 */     fd.left = new FormAttachment(this.validDate, 5, 131072);
/* 251 */     this.validTime.setLayoutData(fd);
/* 252 */     PgenUtil.setUTCTimeTextField(c1, this.validTime, expTime, wType, 5);
/*     */ 
/* 254 */     Label phoneLbl = new Label(panel1, 16384);
/* 255 */     phoneLbl.setText("Phone Number:");
/*     */ 
/* 257 */     this.phoneCombo = new Combo(panel1, 12);
/* 258 */     for (String st : PHONE_LIST) {
/* 259 */       this.phoneCombo.add(st);
/*     */     }
/*     */ 
/* 262 */     this.phoneCombo.select(0);
/*     */ 
/* 265 */     Label forecasterLbl = new Label(panel1, 16384);
/* 266 */     forecasterLbl.setText("Forecaster:");
/*     */ 
/* 268 */     this.forecasterCombo = new Combo(panel1, 4);
/* 269 */     for (String str : FORECASTERS) {
/* 270 */       this.forecasterCombo.add(str);
/*     */     }
/* 272 */     if (this.wbDlg.getWatchBox().getForecaster() != null) {
/* 273 */       this.forecasterCombo.setText(this.wbDlg.getWatchBox().getForecaster());
/*     */     }
/*     */ 
/* 277 */     Label replaceLbl = new Label(panel1, 16384);
/* 278 */     replaceLbl.setText("Replace Watch#:");
/* 279 */     this.rText = new Text(panel1, 133124);
/*     */ 
/* 281 */     AttrDlg.addSeparator(this.top);
/*     */ 
/* 283 */     Composite panel2 = new Composite(this.top, 0);
/* 284 */     panel2.setLayout(pLayout);
/*     */ 
/* 286 */     Label pwfoLabel = new Label(panel2, 0);
/* 287 */     pwfoLabel.setText("Proposed WFOs:");
/* 288 */     this.proposedWFOs = new Text(panel2, 2);
/* 289 */     this.proposedWFOs.setEditable(false);
/* 290 */     this.proposedWFOs.setText(WatchInfoDlg.formatWfoStr(this.wbDlg.getWatchBox()
/* 291 */       .getWFOs()));
/*     */ 
/* 293 */     Label rwfoLabel = new Label(panel2, 0);
/* 294 */     rwfoLabel.setText("Replaced WFOs:");
/* 295 */     Text replacedWFOs = new Text(panel2, 2);
/* 296 */     replacedWFOs.setEditable(false);
/*     */ 
/* 298 */     Label nwfoLabel = new Label(panel2, 0);
/* 299 */     nwfoLabel.setText("Nearby WFOs:");
/* 300 */     this.wfoPane = new Composite(panel2, 0);
/* 301 */     GridLayout wfoGl = new GridLayout(4, false);
/* 302 */     this.wfoPane.setLayout(wfoGl);
/* 303 */     createWfoChkBoxes(this.wbDlg.getWatchBox().getNearbyWFOs());
/*     */ 
/* 305 */     AttrDlg.addSeparator(this.top);
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 315 */     if (getShell() == null) {
/* 316 */       create();
/*     */     }
/*     */ 
/* 319 */     getShell().setLocation(getShell().getParent().getLocation());
/* 320 */     getButton(0).setText("Format");
/* 321 */     getButtonBar().pack();
/* 322 */     return super.open();
/*     */   }
/*     */ 
/*     */   private void createWfoChkBoxes(List<String> wfos)
/*     */   {
/*     */     Button btn;
/* 334 */     if (this.wfoBtns != null) {
/* 335 */       Iterator it = this.wfoBtns.iterator();
/* 336 */       while (it.hasNext()) {
/* 337 */         btn = (Button)it.next();
/* 338 */         btn.dispose();
/* 339 */         it.remove();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 344 */     if ((wfos != null) && (!wfos.isEmpty())) {
/* 345 */       for (String st : wfos)
/*     */       {
/* 348 */         Button stBtn = new Button(this.wfoPane, 32);
/* 349 */         stBtn.setText(st);
/* 350 */         stBtn.setSelection(false);
/*     */ 
/* 352 */         this.wfoBtns.add(stBtn);
/* 353 */         this.wfoPane.layout();
/* 354 */         this.wfoPane.pack(true);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 359 */     this.wfoPane.layout();
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 368 */     if ((!this.stormBtn.getSelection()) && (!this.tornadoBtn.getSelection()))
/*     */     {
/* 370 */       String msg = "Please select weather type!";
/*     */ 
/* 372 */       MessageDialog confirmDlg = new MessageDialog(
/* 373 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 374 */         "Watch Information", null, msg, 2, 
/* 375 */         new String[] { "OK" }, 0);
/* 376 */       confirmDlg.open();
/*     */     }
/* 378 */     else if (this.forecasterCombo.getText().isEmpty()) {
/* 379 */       String msg = "Please type in forecaster name!";
/*     */ 
/* 381 */       MessageDialog confirmDlg = new MessageDialog(
/* 382 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 383 */         "Watch Information", null, msg, 2, 
/* 384 */         new String[] { "OK" }, 0);
/* 385 */       confirmDlg.open();
/*     */     } else {
/* 387 */       String pdName = this.wbDlg.drawingLayer.getActiveProduct().getType();
/* 388 */       ProductType pt = (ProductType)ProductConfigureDialog.getProductTypes().get(
/* 389 */         pdName);
/* 390 */       if (pt != null) {
/* 391 */         pdName = pt.getType();
/*     */       }
/* 393 */       String pd1 = pdName.replaceAll(" ", "_");
/*     */ 
/* 395 */       this.dirPath = 
/* 397 */         (PgenUtil.getPgenOprDirectory() + File.separator + pd1 + 
/* 396 */         File.separator + "prod" + File.separator + "text" + 
/* 397 */         File.separator);
/* 398 */       updateWatch();
/*     */ 
/* 400 */       String label = "WWC_WCL.xml";
/* 401 */       this.dataURI = this.wbDlg.getWatchBox().storeProduct(label);
/* 402 */       openWCCDlg();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateWatch() {
/* 407 */     WatchBox wb = this.wbDlg.getWatchBox();
/*     */ 
/* 409 */     wb.setWatchType(getWatchType());
/* 410 */     wb.setForecaster(this.forecasterCombo.getText());
/*     */     try {
/* 412 */       wb.setReplWatch(Integer.parseInt(this.rText.getText()));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 416 */     wb.setExpTime(getExpirationTime());
/* 417 */     wb.setIssueTime(Calendar.getInstance(TimeZone.getTimeZone("GMT")));
/*     */   }
/*     */ 
/*     */   private String getWeatherType()
/*     */   {
/* 426 */     String weatherType = "";
/* 427 */     if (this.stormBtn.getSelection())
/* 428 */       weatherType = "SEVERE THUNDERSTORM";
/* 429 */     else if (this.tornadoBtn.getSelection())
/* 430 */       weatherType = "TORNADO";
/* 431 */     return weatherType;
/*     */   }
/*     */ 
/*     */   private Calendar getExpirationTime()
/*     */   {
/* 440 */     Calendar expiration = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 441 */     expiration.set(this.validDate.getYear(), this.validDate.getMonth(), 
/* 442 */       this.validDate.getDay(), getExpHour(), getExpMinute(), 0);
/* 443 */     expiration.set(14, 0);
/* 444 */     return expiration;
/*     */   }
/*     */ 
/*     */   private void openWCCDlg()
/*     */   {
/* 453 */     Calendar meetMe = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 454 */     meetMe.add(12, 4);
/*     */ 
/* 457 */     Calendar expiration = getExpirationTime();
/*     */ 
/* 460 */     String wccText = 
/* 461 */       String.format("THERE WILL BE A MEET-ME CONFERENCE CALL WITH THE STORM PREDICTION CENTER AT %1$02d%2$02d UTC %3$02d %4$s %5$4d TO COORDINATE A POTENTIAL %6$s WATCH VALID THROUGH %7$02d%8$02d UTC %9$02d %10$s %11$04d.", new Object[] { 
/* 462 */       Integer.valueOf(meetMe.get(11)), 
/* 463 */       Integer.valueOf(meetMe.get(12)), 
/* 464 */       Integer.valueOf(meetMe.get(5)), 
/* 465 */       meetMe.getDisplayName(2, 2, 
/* 466 */       Locale.US).toUpperCase(), 
/* 467 */       Integer.valueOf(meetMe.get(1)), 
/* 468 */       getWeatherType(), 
/* 469 */       Integer.valueOf(expiration.get(11)), 
/* 470 */       Integer.valueOf(expiration.get(12)), 
/* 471 */       Integer.valueOf(expiration.get(5)), 
/* 472 */       expiration.getDisplayName(2, 
/* 473 */       2, Locale.US).toUpperCase(), 
/* 474 */       Integer.valueOf(expiration.get(1)) });
/*     */ 
/* 476 */     wccText = wccText + "\n\nTHE FOLLOWING NATIONAL WEATHER SERVICE FORECAST OFFICES ARE NEEDED ON THE CONFERENCE CALL:\n\n";
/* 477 */     String wfoList = 
/* 478 */       WatchInfoDlg.formatWfoStr(this.wbDlg.getWatchBox().getWFOs()).substring(3)
/* 479 */       .replaceAll("\n", "");
/* 480 */     wccText = wccText + "   " + wfoList;
/*     */ 
/* 483 */     boolean nearby = false;
/* 484 */     String nearbyWFOs = "";
/* 485 */     for (Button btn : this.wfoBtns) {
/* 486 */       if (btn.getSelection()) {
/* 487 */         if (!nearby)
/* 488 */           nearby = true;
/* 489 */         if (nearbyWFOs.isEmpty())
/* 490 */           nearbyWFOs = nearbyWFOs + btn.getText();
/*     */         else {
/* 492 */           nearbyWFOs = nearbyWFOs + "..." + btn.getText();
/*     */         }
/*     */       }
/*     */     }
/* 496 */     if (nearby) {
/* 497 */       wccText = wccText + "\n\nTHE FOLLOWING NWS WFOS NEAR THE PROPOSED WATCH AREA ARE BEING REQUESTED TO PARTICIPATE ON THE CONFERENCE CALL:";
/* 498 */       wccText = wccText + "\n\n   " + nearbyWFOs;
/*     */     }
/*     */ 
/* 501 */     wccText = wccText + "\n\nPhone Number: " + this.phoneCombo.getText();
/* 502 */     wccText = wccText + "\nPassword:     -     ";
/* 503 */     wccText = wccText + "\n\nIF PASSWORD IS NOT AVAILABLE, CONTACT SPC LEAD FORECASTER.";
/* 504 */     wccText = wccText + "\n\nATTN..." + wfoList + "...WNAW...WNAR";
/* 505 */     wccText = wccText + "\n\n" + this.forecasterCombo.getText();
/*     */ 
/* 507 */     String msg = wccText + "\n\n------------------------------------\n";
/* 508 */     msg = msg + "\nSave To:   KNCFNIMNAT";
/*     */ 
/* 510 */     String wfoLaunch = "None";
/* 511 */     if (!this.wbDlg.getWatchBox().getWFOs().isEmpty()) {
/* 512 */       wfoLaunch = 
/* 513 */         WatchInfoDlg.formatWfoStr(this.wbDlg.getWatchBox().getWFOs()).substring(3)
/* 514 */         .replaceAll("\n", "");
/* 515 */       wfoLaunch = wfoLaunch.replaceAll("\\.\\.\\.", ",") + ",WNAW,WNAR";
/*     */     }
/*     */ 
/* 518 */     String wccLaunch = "#!/bin/csh\nlaunch_prod text_" + wfoLaunch + 
/* 519 */       " KNCFNIMNAT\n";
/*     */ 
/* 521 */     WatchWCCDlg wccDlg = new WatchWCCDlg(PlatformUI.getWorkbench()
/* 522 */       .getActiveWorkbenchWindow().getShell());
/* 523 */     wccDlg.setMessage(PgenToolUtils.wrapWatchText(wccText, 65));
/* 524 */     wccDlg.setWCCLaunchText(wccLaunch);
/* 525 */     wccDlg.setBlockOnOpen(true);
/* 526 */     wccDlg.setOutputPath(this.dirPath);
/* 527 */     wccDlg.setDataURI(this.dataURI);
/* 528 */     wccDlg.open();
/*     */ 
/* 530 */     if (wccDlg.getReturnCode() == 0)
/* 531 */       openWCLDlg();
/*     */   }
/*     */ 
/*     */   private void openWCLDlg()
/*     */   {
/* 540 */     Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*     */ 
/* 542 */     String wclText = String.format("NWUS64 KWNS %1$td%1$tH%1$tM", new Object[] { utc });
/* 543 */     wclText = wclText + "\nWCL" + this.idCombo.getText();
/*     */ 
/* 545 */     wclText = wclText + "\n\n." + getWeatherType() + " WATCH " + this.idCombo.getText();
/*     */ 
/* 547 */     wclText = wclText + "\nCOORDINATION COUNTY LIST FROM THE NWS STORM PREDICTION CENTER\n";
/* 548 */     wclText = wclText + String.format("EFFECTIVE UNTIL %1$tH%1$tM UTC.\n\n\n", new Object[] { 
/* 549 */       getExpirationTime() });
/*     */ 
/* 551 */     for (String state : this.wbDlg.getWatchBox().getStates()) {
/* 552 */       wclText = wclText + PgenToolUtils.wrapWatchText(this.wbDlg.getWatchBox()
/* 553 */         .createCountyInfo(state, getExpirationTime()), 65);
/* 554 */       wclText = wclText + "$$\n\n\n";
/*     */     }
/* 556 */     String attn = 
/* 558 */       PgenToolUtils.wrapWatchText(new StringBuilder("ATTN...WFO")
/* 557 */       .append(WatchInfoDlg.formatWfoStr(this.wbDlg.getWatchBox().getWFOs())
/* 558 */       .replaceAll("\n", "")).toString(), 
/* 558 */       65) + 
/* 559 */       "\n";
/* 560 */     wclText = wclText + attn;
/*     */ 
/* 562 */     String msg = wclText;
/*     */ 
/* 564 */     WatchWCLDlg wclDlg = new WatchWCLDlg(PlatformUI.getWorkbench()
/* 565 */       .getActiveWorkbenchWindow().getShell());
/* 566 */     wclDlg.setWCLFileNmae("KWNSWCL" + this.idCombo.getText());
/* 567 */     wclDlg.setMessage(msg);
/* 568 */     wclDlg.setBlockOnOpen(true);
/* 569 */     wclDlg.setOutputPath(this.dirPath);
/* 570 */     wclDlg.setDataURI(this.dataURI);
/* 571 */     wclDlg.open();
/*     */ 
/* 573 */     if (wclDlg.getReturnCode() == 0)
/* 574 */       close();
/*     */   }
/*     */ 
/*     */   public static Document readForecasterTbl()
/*     */   {
/* 581 */     if (FORECASTERS == null) {
/*     */       try {
/* 583 */         String forecasterFile = PgenStaticDataProvider.getProvider()
/* 584 */           .getFileAbsolutePath(
/* 585 */           PgenStaticDataProvider.getProvider()
/* 586 */           .getPgenLocalizationRoot() + 
/* 587 */           "forecasters.xml");
/*     */ 
/* 589 */         SAXReader reader = new SAXReader();
/* 590 */         forecasterTbl = reader.read(forecasterFile);
/*     */       } catch (Exception e) {
/* 592 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 596 */     return forecasterTbl;
/*     */   }
/*     */ 
/*     */   public static String[] getForecasters() {
/* 600 */     if (forecasterTbl == null) {
/* 601 */       FORECASTERS = new String[] { "BALDWIN", "BIRCH", "EVANS", "GALLINA" };
/*     */     } else {
/* 603 */       List list = new ArrayList();
/* 604 */       List nodes = forecasterTbl.selectNodes(FORECASTER_XPATH);
/*     */ 
/* 606 */       for (Node node : nodes) {
/* 607 */         list.add(node.valueOf("@name").toString());
/*     */       }
/*     */ 
/* 610 */       FORECASTERS = new String[list.size()];
/* 611 */       FORECASTERS = (String[])list.toArray(FORECASTERS);
/*     */     }
/*     */ 
/* 614 */     return FORECASTERS;
/*     */   }
/*     */ 
/*     */   private String getWatchType()
/*     */   {
/* 623 */     String type = "";
/* 624 */     if (this.stormBtn.getSelection())
/* 625 */       type = "SEVERE THUNDERSTORM";
/* 626 */     else if (this.tornadoBtn.getSelection()) {
/* 627 */       type = "TORNADO";
/*     */     }
/* 629 */     return type;
/*     */   }
/*     */ 
/*     */   private int getExpHour()
/*     */   {
/* 636 */     int ret = 0;
/*     */     try {
/* 638 */       String hm = this.validTime.getText();
/* 639 */       ret = Integer.parseInt(hm.substring(0, hm.length() == 4 ? 2 : 1));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 643 */     return ret;
/*     */   }
/*     */ 
/*     */   private int getExpMinute()
/*     */   {
/* 650 */     int ret = 0;
/*     */     try {
/* 652 */       String hm = this.validTime.getText();
/* 653 */       ret = Integer.parseInt(hm.substring(hm.length() == 4 ? 2 : 1), 
/* 654 */         hm.length() - 1);
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 658 */     return ret;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchCoordDlg
 * JD-Core Version:    0.6.2
 */