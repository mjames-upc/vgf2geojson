/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.IContours;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.Node;
/*     */ import org.dom4j.io.SAXReader;
/*     */ import org.eclipse.jface.dialogs.IDialogConstants;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.DateTime;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class ContoursInfoDlg extends CaveJFACEDialog
/*     */   implements IContours
/*     */ {
/*     */   private static Document contoursInfoTbl;
/*  61 */   public static String CNTRINFO_XPATH = "/root/contoursInfo";
/*     */ 
/*  63 */   private Composite top = null;
/*     */ 
/*  65 */   private Combo parmCombo = null;
/*  66 */   private Text parmTxt = null;
/*     */ 
/*  68 */   private Combo levelCombo1 = null;
/*  69 */   private Text levelTxt1 = null;
/*  70 */   private Combo levelCombo2 = null;
/*  71 */   private Text levelTxt2 = null;
/*     */ 
/*  73 */   private Combo fcsthrCombo = null;
/*  74 */   private Text fcsthrTxt = null;
/*     */ 
/*  76 */   private Text cintTxt = null;
/*  77 */   private AttrDlg contoursAttrDlg = null;
/*     */ 
/*  79 */   private DateTime date1 = null;
/*  80 */   private DateTime time1 = null;
/*  81 */   private DateTime date2 = null;
/*  82 */   private DateTime time2 = null;
/*     */ 
/*     */   protected ContoursInfoDlg(Shell parentShell)
/*     */   {
/*  89 */     super(parentShell);
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/*  98 */     createButton(parent, 0, IDialogConstants.OK_LABEL, true);
/*  99 */     createButton(parent, 1, IDialogConstants.CANCEL_LABEL, true);
/*     */   }
/*     */ 
/*     */   protected void configureShell(Shell shell)
/*     */   {
/* 110 */     super.configureShell(shell);
/* 111 */     shell.setText("Contours Attributes");
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 120 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 122 */     GridLayout mainLayout = new GridLayout(2, false);
/* 123 */     mainLayout.marginHeight = 3;
/* 124 */     mainLayout.marginWidth = 3;
/* 125 */     mainLayout.horizontalSpacing = 3;
/* 126 */     this.top.setLayout(mainLayout);
/*     */ 
/* 128 */     initializeComponents(this.top);
/*     */ 
/* 130 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents(Composite comp)
/*     */   {
/* 138 */     GridLayout layout1 = new GridLayout(2, false);
/* 139 */     layout1.marginHeight = 1;
/* 140 */     layout1.marginWidth = 1;
/* 141 */     layout1.horizontalSpacing = 3;
/*     */ 
/* 144 */     Label parmLbl = new Label(comp, 0);
/* 145 */     parmLbl.setText("PARM:");
/*     */ 
/* 147 */     Composite parmComp = new Composite(comp, 0);
/* 148 */     parmComp.setLayout(layout1);
/*     */ 
/* 150 */     this.parmCombo = new Combo(parmComp, 12);
/* 151 */     for (String st : getContourParms("Parm")) {
/* 152 */       this.parmCombo.add(st);
/*     */     }
/* 154 */     this.parmCombo.add("Other");
/* 155 */     this.parmCombo.select(0);
/*     */ 
/* 157 */     this.parmCombo.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 159 */         ContoursInfoDlg.this.updateComboText(ContoursInfoDlg.this.parmCombo, ContoursInfoDlg.this.parmTxt, ContoursInfoDlg.this.parmCombo.getText());
/*     */       }
/*     */     });
/* 163 */     this.parmTxt = new Text(parmComp, 2052);
/* 164 */     this.parmTxt.setLayoutData(new GridData(45, 15));
/* 165 */     this.parmTxt.setEditable(true);
/* 166 */     this.parmTxt.setText(this.parmCombo.getText());
/*     */ 
/* 169 */     Label levelLbl = new Label(comp, 0);
/* 170 */     levelLbl.setText("Level 1:");
/*     */ 
/* 172 */     Composite lvl1Comp = new Composite(comp, 0);
/* 173 */     lvl1Comp.setLayout(layout1);
/*     */ 
/* 175 */     this.levelCombo1 = new Combo(lvl1Comp, 12);
/* 176 */     for (String st : getContourParms("Level")) {
/* 177 */       this.levelCombo1.add(st);
/*     */     }
/* 179 */     this.levelCombo1.add("Other");
/* 180 */     this.levelCombo1.select(0);
/* 181 */     this.levelCombo1.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 183 */         ContoursInfoDlg.this.updateComboText(ContoursInfoDlg.this.levelCombo1, ContoursInfoDlg.this.levelTxt1, ContoursInfoDlg.this.levelCombo1.getText());
/*     */       }
/*     */     });
/* 187 */     this.levelTxt1 = new Text(lvl1Comp, 2052);
/* 188 */     this.levelTxt1.setLayoutData(new GridData(45, 15));
/* 189 */     this.levelTxt1.setEditable(true);
/* 190 */     this.levelTxt1.setText(this.levelCombo1.getText());
/*     */ 
/* 193 */     Label levelLbl2 = new Label(comp, 0);
/* 194 */     levelLbl2.setText("Level 2:");
/*     */ 
/* 196 */     Composite lvl2Comp = new Composite(comp, 0);
/* 197 */     lvl2Comp.setLayout(layout1);
/*     */ 
/* 199 */     this.levelCombo2 = new Combo(lvl2Comp, 12);
/* 200 */     for (String st : getContourParms("Level")) {
/* 201 */       this.levelCombo2.add(st);
/*     */     }
/* 203 */     this.levelCombo2.select(0);
/* 204 */     this.levelCombo2.add("Other");
/* 205 */     this.levelCombo2.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 207 */         ContoursInfoDlg.this.updateComboText(ContoursInfoDlg.this.levelCombo2, ContoursInfoDlg.this.levelTxt2, ContoursInfoDlg.this.levelCombo2.getText());
/*     */       }
/*     */     });
/* 211 */     this.levelTxt2 = new Text(lvl2Comp, 2052);
/* 212 */     this.levelTxt2.setLayoutData(new GridData(45, 15));
/* 213 */     this.levelTxt2.setEditable(true);
/* 214 */     this.levelTxt2.setText("");
/*     */ 
/* 217 */     Label fcsthrLbl = new Label(comp, 0);
/* 218 */     fcsthrLbl.setText("Fcst Hour:");
/*     */ 
/* 220 */     Composite fhrComp = new Composite(comp, 0);
/* 221 */     fhrComp.setLayout(layout1);
/*     */ 
/* 223 */     this.fcsthrCombo = new Combo(fhrComp, 12);
/* 224 */     for (String st : getContourParms("ForecastHour")) {
/* 225 */       this.fcsthrCombo.add(st);
/*     */     }
/* 227 */     this.fcsthrCombo.add("Other");
/* 228 */     this.fcsthrCombo.select(0);
/*     */ 
/* 230 */     this.fcsthrCombo.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 232 */         ContoursInfoDlg.this.updateComboText(ContoursInfoDlg.this.fcsthrCombo, ContoursInfoDlg.this.fcsthrTxt, ContoursInfoDlg.this.fcsthrCombo.getText());
/*     */       }
/*     */     });
/* 236 */     this.fcsthrTxt = new Text(fhrComp, 2052);
/* 237 */     this.fcsthrTxt.setLayoutData(new GridData(45, 15));
/* 238 */     this.fcsthrTxt.setEditable(true);
/* 239 */     this.fcsthrTxt.setText(this.fcsthrCombo.getText());
/*     */ 
/* 242 */     Label dateLbl = new Label(comp, 0);
/* 243 */     dateLbl.setText("Time 1:");
/*     */ 
/* 245 */     Composite dtComp = new Composite(comp, 0);
/* 246 */     dtComp.setLayout(layout1);
/*     */ 
/* 248 */     this.date1 = new DateTime(dtComp, 2080);
/* 249 */     this.time1 = new DateTime(dtComp, 34944);
/*     */ 
/* 252 */     Label dateLbl2 = new Label(comp, 0);
/* 253 */     dateLbl2.setText("Time 2:");
/*     */ 
/* 255 */     Composite dtComp2 = new Composite(comp, 0);
/* 256 */     dtComp2.setLayout(layout1);
/*     */ 
/* 258 */     this.date2 = new DateTime(dtComp2, 2208);
/* 259 */     this.time2 = new DateTime(dtComp2, 34944);
/*     */ 
/* 261 */     Label cintLbl = new Label(comp, 0);
/* 262 */     cintLbl.setText("Cint:");
/*     */ 
/* 265 */     this.cintTxt = new Text(comp, 2052);
/* 266 */     this.cintTxt.setLayoutData(new GridData(100, 15));
/* 267 */     this.cintTxt.setEditable(true);
/* 268 */     this.cintTxt.setText("");
/*     */ 
/* 270 */     updateContourInfoSelection((IContours)this.contoursAttrDlg);
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 279 */     if (getShell() == null) {
/* 280 */       create();
/*     */     }
/*     */ 
/* 283 */     Point pt = getShell().getParent().getLocation();
/*     */ 
/* 285 */     getShell().setLocation(pt.x + 350, pt.y + 50);
/*     */ 
/* 287 */     return super.open();
/*     */   }
/*     */ 
/*     */   public String getParm()
/*     */   {
/* 296 */     String parm = this.parmTxt.getText();
/* 297 */     if (parm == null) {
/* 298 */       parm = "";
/*     */     }
/*     */ 
/* 301 */     return parm;
/*     */   }
/*     */ 
/*     */   public String getLevel()
/*     */   {
/* 309 */     String level = this.levelTxt1.getText();
/* 310 */     if (level == null) {
/* 311 */       level = "";
/*     */     }
/*     */ 
/* 314 */     String level2 = this.levelTxt2.getText();
/* 315 */     if (level2 == null) {
/* 316 */       level2 = "";
/*     */     }
/*     */ 
/* 319 */     if (level2.trim().length() > 0) {
/* 320 */       level = new String(level + ":" + level2);
/*     */     }
/*     */ 
/* 323 */     return level;
/*     */   }
/*     */ 
/*     */   public String getForecastHour()
/*     */   {
/* 331 */     String hr = this.fcsthrTxt.getText();
/* 332 */     if (hr == null) {
/* 333 */       hr = "";
/*     */     }
/*     */ 
/* 336 */     return hr;
/*     */   }
/*     */ 
/*     */   public String getCint()
/*     */   {
/* 343 */     return this.cintTxt.getText();
/*     */   }
/*     */ 
/*     */   public Calendar getTime1()
/*     */   {
/* 351 */     Calendar myTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 352 */     myTime.set(this.date1.getYear(), this.date1.getMonth(), this.date1.getDay(), 
/* 353 */       this.time1.getHours(), this.time1.getMinutes(), 0);
/*     */ 
/* 355 */     return myTime;
/*     */   }
/*     */ 
/*     */   private void setTime1(Calendar time)
/*     */   {
/* 363 */     this.date1.setYear(time.get(1));
/* 364 */     this.date1.setMonth(time.get(2));
/* 365 */     this.date1.setDay(time.get(5));
/* 366 */     this.time1.setHours(time.get(10));
/* 367 */     this.time1.setMinutes(time.get(12));
/* 368 */     this.time1.setSeconds(0);
/*     */   }
/*     */ 
/*     */   public Calendar getTime2()
/*     */   {
/* 376 */     Calendar myTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 377 */     myTime.set(this.date2.getYear(), this.date2.getMonth(), this.date2.getDay(), 
/* 378 */       this.time2.getHours(), this.time2.getMinutes(), 0);
/*     */ 
/* 380 */     return myTime;
/*     */   }
/*     */ 
/*     */   private void setTime2(Calendar time)
/*     */   {
/* 388 */     if (time != null) {
/* 389 */       this.date2.setYear(time.get(1));
/* 390 */       this.date2.setMonth(time.get(2));
/* 391 */       this.date2.setDay(time.get(5));
/* 392 */       this.time2.setHours(time.get(10));
/* 393 */       this.time2.setMinutes(time.get(12));
/* 394 */       this.time2.setSeconds(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setContoursAttrDlg(AttrDlg contoursAttrDlg)
/*     */   {
/* 402 */     this.contoursAttrDlg = contoursAttrDlg;
/*     */   }
/*     */ 
/*     */   public AttrDlg getContoursAttrDlg()
/*     */   {
/* 409 */     return this.contoursAttrDlg;
/*     */   }
/*     */ 
/*     */   private void updateContoursAttrDlg()
/*     */   {
/* 418 */     if ((this.contoursAttrDlg instanceof ContoursAttrDlg)) {
/* 419 */       ((ContoursAttrDlg)this.contoursAttrDlg).setAttributes(this);
/*     */     }
/* 421 */     else if ((this.contoursAttrDlg instanceof OutlookAttrDlg))
/* 422 */       ((OutlookAttrDlg)this.contoursAttrDlg).setAttributes(this);
/*     */   }
/*     */ 
/*     */   private void updateContourInfoSelection(IContours attr)
/*     */   {
/* 433 */     updateComboText(this.parmCombo, this.parmTxt, attr.getParm());
/*     */ 
/* 435 */     String lvl = attr.getLevel();
/* 436 */     int spi = lvl.indexOf(":");
/* 437 */     String lvl1 = new String(lvl);
/* 438 */     if (spi > 0) {
/* 439 */       lvl1 = lvl.substring(0, spi);
/*     */     }
/*     */ 
/* 442 */     updateComboText(this.levelCombo1, this.levelTxt1, lvl1);
/*     */ 
/* 444 */     String lvl2 = new String("");
/* 445 */     if ((spi > 0) && (spi < lvl.length())) {
/* 446 */       lvl2 = lvl.substring(spi + 1, lvl.length());
/*     */     }
/*     */ 
/* 449 */     updateComboText(this.levelCombo2, this.levelTxt2, lvl2);
/*     */ 
/* 451 */     updateComboText(this.fcsthrCombo, this.fcsthrTxt, attr.getForecastHour());
/*     */ 
/* 453 */     this.cintTxt.setText(attr.getCint());
/*     */ 
/* 455 */     setTime1(attr.getTime1());
/* 456 */     setTime2(attr.getTime2());
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 464 */     updateContoursAttrDlg();
/* 465 */     super.okPressed();
/*     */   }
/*     */ 
/*     */   public static final Document readInfoTbl()
/*     */   {
/* 474 */     if (contoursInfoTbl == null) {
/*     */       try {
/* 476 */         String cntrInfoFile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/* 477 */           PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "contoursInfo.xml");
/*     */ 
/* 479 */         SAXReader reader = new SAXReader();
/* 480 */         contoursInfoTbl = reader.read(cntrInfoFile);
/*     */       } catch (Exception e) {
/* 482 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 486 */     return contoursInfoTbl;
/*     */   }
/*     */ 
/*     */   public static Document readInfoTbl1()
/*     */   {
/* 495 */     Document dm = null;
/*     */     try {
/* 497 */       SAXReader reader = new SAXReader();
/* 498 */       dm = reader.read("/usr1/jwu/r1g1-6/eclipse/AAA.xml");
/*     */     } catch (Exception e) {
/* 500 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 503 */     return dm;
/*     */   }
/*     */ 
/*     */   private static List<String> getContourParms(String type)
/*     */   {
/* 513 */     List lbls = new ArrayList();
/* 514 */     String xpath = CNTRINFO_XPATH + "[@name='" + type + "']";
/*     */ 
/* 516 */     Document dm = readInfoTbl();
/*     */ 
/* 518 */     if (dm != null) {
/* 519 */       Node cntrInfo = dm.selectSingleNode(xpath);
/* 520 */       List nodes = cntrInfo.selectNodes("label");
/* 521 */       for (Node node : nodes) {
/* 522 */         lbls.add(node.valueOf("@text"));
/*     */       }
/*     */     }
/*     */ 
/* 526 */     return lbls;
/*     */   }
/*     */ 
/*     */   private void updateComboText(Combo cmb, Text txt, String sel)
/*     */   {
/* 536 */     if (sel == null) {
/* 537 */       sel = cmb.getText();
/*     */     }
/*     */ 
/* 541 */     txt.setText(sel);
/*     */ 
/* 544 */     int index = -1;
/* 545 */     boolean found = false;
/* 546 */     for (String str : cmb.getItems()) {
/* 547 */       if (str.equals(sel)) {
/* 548 */         found = true;
/* 549 */         break;
/*     */       }
/*     */ 
/* 552 */       index++;
/*     */     }
/*     */ 
/* 556 */     if (found) {
/* 557 */       cmb.select(index + 1);
/* 558 */       if (sel.equalsIgnoreCase("Other")) {
/* 559 */         txt.setText("");
/* 560 */         txt.setEnabled(true);
/*     */       }
/*     */       else {
/* 563 */         txt.setEnabled(false);
/*     */       }
/*     */     }
/*     */     else {
/* 567 */       cmb.select(cmb.getItemCount() - 1);
/* 568 */       txt.setEnabled(true);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.ContoursInfoDlg
 * JD-Core Version:    0.6.2
 */