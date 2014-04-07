/*     */ package gov.noaa.nws.ncep.ui.pgen.graphtogrid;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.ContoursAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.OutlookAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourCircle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourMinmax;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.IContours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.viz.gempak.nativelib.LibraryLoader;
/*     */ import java.awt.Color;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.eclipse.swt.events.KeyAdapter;
/*     */ import org.eclipse.swt.events.KeyEvent;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.events.SelectionListener;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class GraphToGridParamDialog extends CaveJFACEDialog
/*     */ {
/*  84 */   private static LinkedHashMap<String, String> productMaps = null;
/*  85 */   private static HashMap<String, String> currentProductParams = null;
/*  86 */   private static ArrayList<String> productNames = null;
/*  87 */   private static String grphgdTblName = PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "grphgd.tbl";
/*  88 */   private static ArrayList<HashMap<String, String>> productDefaults = null;
/*     */   private static final int BASIC_ADV_ID = 8609;
/*     */   private static final String BASIC_LABEL = "Basic...";
/*     */   private static final String ADVANCED_LABEL = "Advanced...";
/*     */   private static final int SHOW_EXT_ID = 8610;
/*     */   private static final String SHOW_EXT_LABEL = "Show Extensions";
/*     */   private static final int MAKE_GRID_ID = 8611;
/*     */   private static final String MAKE_GRID_LABEL = "Make Grid";
/*     */   private static final int CANCEL_ID = 8612;
/*     */   private static final String CANCEL_LABEL = "Cancel";
/*     */   private static final int H_SPACING = 12;
/* 102 */   private static int NCYCLES = 4;
/* 103 */   private static int CYCLE_INTERVAL = 12;
/* 104 */   private static int NFCSTHRS = 13;
/* 105 */   private static int FCSTHUR_INTREVAL = 6;
/*     */ 
/* 107 */   private Color[] extColor = { Color.blue };
/*     */ 
/* 110 */   private Composite top = null;
/* 111 */   private Combo prdCombo = null;
/* 112 */   private Combo cycleCombo = null;
/* 113 */   private Combo fcstCombo = null;
/* 114 */   private Group advancedGrp = null;
/* 115 */   private ArrayList<Text> paramText = null;
/* 116 */   private String currentPrd = null;
/* 117 */   private Button displayOption = null;
/*     */ 
/* 119 */   private AttrDlg cntAttrDlg = null;
/*     */ 
/*     */   public GraphToGridParamDialog(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 128 */     super(parShell);
/* 129 */     setShellStyle(96);
/*     */ 
/* 132 */     LibraryLoader.load("g2g");
/*     */ 
/* 134 */     if (productMaps == null) {
/* 135 */       productMaps = GraphToGrid.loadParameters(grphgdTblName);
/* 136 */       productNames = new ArrayList(productMaps.keySet());
/*     */     }
/*     */ 
/* 139 */     if (productDefaults == null)
/*     */     {
/* 142 */       productDefaults = new ArrayList();
/*     */ 
/* 144 */       for (String str : productNames)
/*     */       {
/* 146 */         String value = (String)productMaps.get(str);
/* 147 */         String fileName = value.substring(value.lastIndexOf('/') + 1);
/*     */ 
/* 149 */         HashMap map = GraphToGrid.loadParameters(
/* 150 */           PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + File.separator + fileName);
/*     */ 
/* 152 */         if (map.size() > 0) {
/* 153 */           productDefaults.add(map);
/*     */         }
/*     */         else
/* 156 */           productMaps.remove(str);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 173 */     createButton(parent, 8609, "Advanced...", true);
/*     */ 
/* 176 */     createButton(parent, 8610, "Show Extensions", true);
/*     */ 
/* 179 */     createButton(parent, 8611, "Make Grid", true);
/*     */ 
/* 182 */     createButton(parent, 8612, "Cancel", true);
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 192 */     this.top = ((Composite)super.createDialogArea(parent));
/* 193 */     getShell().setText("GRAPH-to-GRID Processing");
/*     */ 
/* 196 */     GridLayout mainLayout = new GridLayout(1, true);
/* 197 */     mainLayout.marginHeight = 3;
/* 198 */     mainLayout.marginWidth = 3;
/* 199 */     this.top.setLayout(mainLayout);
/*     */ 
/* 202 */     Composite prdComp = new Composite(this.top, 0);
/* 203 */     GridLayout layout = new GridLayout(3, false);
/* 204 */     layout.horizontalSpacing = 25;
/* 205 */     prdComp.setLayout(layout);
/*     */ 
/* 208 */     Label prdLbl = new Label(prdComp, 16384);
/* 209 */     prdLbl.setText("PRODUCT");
/*     */ 
/* 211 */     this.prdCombo = new Combo(prdComp, 12);
/* 212 */     for (String st : productMaps.keySet()) {
/* 213 */       this.prdCombo.add(st);
/*     */     }
/* 215 */     this.prdCombo.add("-none-");
/*     */ 
/* 217 */     this.prdCombo.addSelectionListener(new SelectionListener()
/*     */     {
/*     */       public void widgetDefaultSelected(SelectionEvent e)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 227 */         String newPrd = ((Combo)e.widget).getText();
/* 228 */         if (!newPrd.equals(GraphToGridParamDialog.this.currentPrd)) {
/* 229 */           GraphToGridParamDialog.this.currentPrd = newPrd;
/* 230 */           GraphToGridParamDialog.currentProductParams = GraphToGridParamDialog.this.retrievePrdMap(GraphToGridParamDialog.this.currentPrd);
/* 231 */           GraphToGridParamDialog.currentProductParams.putAll(GraphToGridParamDialog.this.generateParameters());
/* 232 */           GraphToGridParamDialog.this.setParameters(GraphToGridParamDialog.currentProductParams);
/*     */         }
/*     */       }
/*     */     });
/* 237 */     this.prdCombo.select(0);
/* 238 */     this.currentPrd = this.prdCombo.getText();
/*     */ 
/* 240 */     this.displayOption = new Button(prdComp, 32);
/* 241 */     this.displayOption.setText("Show Result as a Ghost Contours");
/* 242 */     this.displayOption.setSelection(true);
/*     */ 
/* 244 */     createBasicInfo(this.top);
/*     */ 
/* 246 */     currentProductParams = retrievePrdMap(this.currentPrd);
/* 247 */     currentProductParams.putAll(generateParameters());
/*     */ 
/* 249 */     return this.top;
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 259 */     if (getShell() == null) {
/* 260 */       create();
/*     */     }
/*     */ 
/* 263 */     Point pt = getShell().getParent().getLocation();
/* 264 */     getShell().setLocation(pt.x + 400, pt.y);
/*     */ 
/* 266 */     return super.open();
/*     */   }
/*     */ 
/*     */   public boolean close()
/*     */   {
/* 275 */     if ((this.advancedGrp != null) && (this.paramText != null))
/*     */     {
/* 277 */       Control[] wids = this.advancedGrp.getChildren();
/*     */ 
/* 279 */       if (wids != null) {
/* 280 */         for (int kk = 0; kk < wids.length; kk++) {
/* 281 */           wids[kk].dispose();
/*     */         }
/*     */       }
/*     */ 
/* 285 */       this.advancedGrp.dispose();
/*     */ 
/* 287 */       this.advancedGrp = null;
/*     */ 
/* 289 */       this.paramText = null;
/*     */     }
/*     */ 
/* 293 */     return super.close();
/*     */   }
/*     */ 
/*     */   protected void buttonPressed(int buttonId)
/*     */   {
/* 307 */     if (buttonId == 8609)
/*     */     {
/* 309 */       if (getButton(8609).getText().equals("Advanced...")) {
/* 310 */         createAdvancedInfo(this.top);
/* 311 */         getButton(8609).setText("Basic...");
/*     */ 
/* 314 */         currentProductParams.putAll(generateParameters());
/*     */ 
/* 316 */         setParameters(currentProductParams);
/*     */       }
/*     */       else
/*     */       {
/* 320 */         disposeAdvancedInfo();
/* 321 */         getButton(8609).setText("Advanced...");
/*     */       }
/*     */     }
/* 324 */     else if (buttonId == 8610) {
/* 325 */       showExtension();
/*     */     }
/* 328 */     else if (buttonId == 8611) {
/* 329 */       makeGrid();
/*     */     }
/* 332 */     else if (buttonId == 8612) {
/* 333 */       disposeAdvancedInfo();
/* 334 */       close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createBasicInfo(Composite comp)
/*     */   {
/* 345 */     Group g1 = new Group(comp, 16);
/* 346 */     g1.setText("Basic Info");
/* 347 */     g1.setLayoutData(new GridData(768));
/*     */ 
/* 349 */     GridLayout layout = new GridLayout(4, false);
/* 350 */     layout.horizontalSpacing = 12;
/* 351 */     g1.setLayout(layout);
/*     */ 
/* 354 */     Label cycleLbl = new Label(g1, 16384);
/* 355 */     cycleLbl.setText("CYCLE");
/*     */ 
/* 357 */     GridData gdata1 = new GridData(75, 20);
/* 358 */     GridData gdata2 = new GridData(190, 25);
/*     */ 
/* 360 */     cycleLbl.setLayoutData(gdata1);
/*     */ 
/* 362 */     this.cycleCombo = new Combo(g1, 4);
/* 363 */     this.cycleCombo.setLayoutData(gdata2);
/* 364 */     for (String str : buildCycles(NCYCLES, CYCLE_INTERVAL)) {
/* 365 */       this.cycleCombo.add(str);
/*     */     }
/* 367 */     this.cycleCombo.select(0);
/*     */ 
/* 370 */     Label fcstLbl = new Label(g1, 16384);
/* 371 */     fcstLbl.setText("FCST_HR");
/* 372 */     cycleLbl.setLayoutData(gdata1);
/*     */ 
/* 374 */     this.fcstCombo = new Combo(g1, 4);
/* 375 */     this.fcstCombo.setLayoutData(gdata2);
/* 376 */     for (String str : buildFcstHrs(NFCSTHRS, FCSTHUR_INTREVAL)) {
/* 377 */       this.fcstCombo.add(str);
/*     */     }
/*     */ 
/* 380 */     this.fcstCombo.select(0);
/*     */ 
/* 382 */     if ((this.cntAttrDlg instanceof ContoursAttrDlg)) {
/* 383 */       Contours curCnt = ((ContoursAttrDlg)this.cntAttrDlg).getCurrentContours();
/* 384 */       String fcsthr = "";
/* 385 */       if (curCnt != null) {
/* 386 */         fcsthr = curCnt.getForecastHour();
/*     */       }
/*     */       else {
/* 389 */         fcsthr = ((ContoursAttrDlg)this.cntAttrDlg).getForecastHour();
/*     */       }
/*     */ 
/* 392 */       int index = -1;
/* 393 */       boolean found = false;
/* 394 */       for (String str : this.fcstCombo.getItems()) {
/* 395 */         if (str.equals(fcsthr)) {
/* 396 */           found = true;
/* 397 */           break;
/*     */         }
/*     */ 
/* 400 */         index++;
/*     */       }
/*     */ 
/* 403 */       if (found)
/* 404 */         this.fcstCombo.select(index + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createAdvancedInfo(Composite comp)
/*     */   {
/* 417 */     this.advancedGrp = new Group(comp, 16);
/* 418 */     this.advancedGrp.setText("Advanced Info");
/* 419 */     this.advancedGrp.setLayoutData(new GridData(768));
/*     */ 
/* 421 */     GridLayout layout = new GridLayout(1, false);
/* 422 */     this.advancedGrp.setLayout(layout);
/*     */ 
/* 424 */     this.paramText = new ArrayList();
/* 425 */     createPanelFromList(this.advancedGrp, "Grid Navigation", 
/* 426 */       GraphToGrid.getGridCalcParams(), this.paramText, 9);
/* 427 */     createPanelFromList(this.advancedGrp, "Grid Display", 
/* 428 */       GraphToGrid.getGridDisplayParams(), this.paramText, 1);
/* 429 */     createPanelFromList(this.advancedGrp, "Grid Output", 
/* 430 */       GraphToGrid.getGridOutputParams(), this.paramText, 9);
/*     */ 
/* 432 */     getShell().pack();
/*     */ 
/* 434 */     getShell().layout();
/*     */   }
/*     */ 
/*     */   private void createPanelFromList(Group parent, String title, String[] params, ArrayList<Text> textList, int active)
/*     */   {
/* 445 */     Group grp = new Group(parent, 16);
/* 446 */     grp.setText(title);
/* 447 */     GridLayout layout1 = new GridLayout(4, false);
/* 448 */     layout1.horizontalSpacing = 12;
/* 449 */     grp.setLayout(layout1);
/*     */ 
/* 451 */     GridData gdata1 = new GridData(70, 15);
/* 452 */     GridData gdata2 = new GridData(180, 15);
/*     */ 
/* 454 */     int ii = 0;
/* 455 */     for (String str : params)
/*     */     {
/* 457 */       Label label = new Label(grp, 16384);
/* 458 */       label.setText(str);
/* 459 */       label.setLayoutData(gdata1);
/*     */ 
/* 461 */       Text txt = new Text(grp, 2052);
/*     */ 
/* 463 */       txt.setLayoutData(gdata2);
/* 464 */       txt.setEditable(true);
/*     */ 
/* 466 */       txt.setData(str);
/* 467 */       if (str.equals("PATH")) {
/* 468 */         txt.setToolTipText("The directory must exist and has lower case only!");
/*     */       }
/*     */ 
/* 471 */       txt.addKeyListener(new KeyAdapter()
/*     */       {
/*     */         public void keyReleased(KeyEvent e)
/*     */         {
/*     */         }
/*     */       });
/* 481 */       if (ii >= active) {
/* 482 */         label.setEnabled(false);
/* 483 */         txt.setEnabled(false);
/*     */       }
/*     */ 
/* 486 */       ii++;
/*     */ 
/* 488 */       textList.add(txt);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void disposeAdvancedInfo()
/*     */   {
/* 499 */     retrievePrdMap(this.currentPrd);
/*     */ 
/* 501 */     if ((this.advancedGrp != null) && (this.paramText != null))
/*     */     {
/* 503 */       Control[] wids = this.advancedGrp.getChildren();
/*     */ 
/* 505 */       if (wids != null) {
/* 506 */         for (int kk = 0; kk < wids.length; kk++) {
/* 507 */           wids[kk].dispose();
/*     */         }
/*     */       }
/*     */ 
/* 511 */       this.advancedGrp.dispose();
/*     */ 
/* 513 */       this.advancedGrp = null;
/*     */ 
/* 515 */       this.paramText = null;
/*     */     }
/*     */ 
/* 519 */     getShell().pack();
/*     */ 
/* 521 */     getShell().layout();
/*     */   }
/*     */ 
/*     */   private String[] buildCycles(int ncycles, int interval)
/*     */   {
/* 533 */     Calendar cntTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*     */ 
/* 535 */     cntTime.add(11, interval);
/*     */ 
/* 537 */     String[] cycleTimes = new String[ncycles];
/*     */ 
/* 539 */     String cycle = "";
/*     */ 
/* 541 */     int year = cntTime.get(1);
/*     */ 
/* 543 */     for (int ii = 0; ii < ncycles; ii++) {
/* 544 */       cycle = cycle + (year - year / 100 * 100);
/*     */ 
/* 546 */       if (cntTime.get(2) + 1 < 10) {
/* 547 */         cycle = cycle + "0";
/*     */       }
/* 549 */       cycle = cycle + (cntTime.get(2) + 1);
/*     */ 
/* 551 */       if (cntTime.get(5) < 10) {
/* 552 */         cycle = cycle + "0";
/*     */       }
/* 554 */       cycle = cycle + cntTime.get(5);
/*     */ 
/* 556 */       cycle = cycle + "/";
/*     */ 
/* 558 */       int hour = cntTime.get(11) / 12 * 12;
/* 559 */       if (hour < 10) {
/* 560 */         cycle = cycle + "0";
/*     */       }
/* 562 */       cycle = cycle + hour;
/*     */ 
/* 564 */       cycle = cycle + "00";
/*     */ 
/* 566 */       cycleTimes[ii] = new String(cycle.toString());
/*     */ 
/* 568 */       cycle = "";
/*     */ 
/* 570 */       cntTime.add(11, -interval);
/*     */     }
/*     */ 
/* 574 */     return cycleTimes;
/*     */   }
/*     */ 
/*     */   private String[] buildFcstHrs(int nhours, int interval)
/*     */   {
/* 584 */     String[] fcsthrs = new String[nhours];
/*     */ 
/* 586 */     String hourStr = "f";
/* 587 */     int value = 0;
/*     */ 
/* 589 */     for (int ii = 0; ii < nhours; ii++)
/*     */     {
/* 591 */       value = ii * interval;
/* 592 */       if (value < 10) {
/* 593 */         hourStr = hourStr + "00";
/*     */       }
/* 595 */       else if (value < 100) {
/* 596 */         hourStr = hourStr + "0";
/*     */       }
/*     */ 
/* 599 */       hourStr = hourStr + value;
/*     */ 
/* 601 */       fcsthrs[ii] = new String(hourStr);
/*     */ 
/* 603 */       hourStr = "f";
/*     */     }
/*     */ 
/* 607 */     return fcsthrs;
/*     */   }
/*     */ 
/*     */   private void setParameters(HashMap<String, String> params)
/*     */   {
/* 617 */     String value = (String)params.get("CYCLE");
/* 618 */     if (value != null) {
/* 619 */       this.cycleCombo.setText(value.toString());
/*     */     }
/*     */ 
/* 622 */     value = (String)params.get("FCST_HR");
/* 623 */     if (value != null) {
/* 624 */       this.fcstCombo.setText(value.toString());
/*     */     }
/*     */ 
/* 627 */     if ((this.advancedGrp != null) && (this.paramText != null))
/*     */     {
/* 629 */       for (Text txt : this.paramText)
/*     */       {
/* 631 */         txt.setText("");
/* 632 */         value = (String)params.get(txt.getData().toString());
/* 633 */         if (value != null)
/* 634 */           txt.setText(value);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public HashMap<String, String> getParameters()
/*     */   {
/* 647 */     HashMap params = new HashMap();
/* 648 */     if (currentProductParams != null) {
/* 649 */       params.putAll(currentProductParams);
/*     */     }
/*     */ 
/* 652 */     params.put("PRODUCT", this.prdCombo.getText());
/* 653 */     params.put("CYCLE", this.cycleCombo.getText());
/* 654 */     params.put("FCST_HR", this.fcstCombo.getText());
/* 655 */     if (this.displayOption.getSelection()) {
/* 656 */       params.put("DISPOPT", "TRUE");
/*     */     }
/*     */     else {
/* 659 */       params.put("DISPOPT", "FALSE");
/*     */     }
/*     */ 
/* 662 */     if ((this.advancedGrp != null) && (this.paramText != null))
/*     */     {
/* 664 */       for (Text txt : this.paramText) {
/* 665 */         params.put(txt.getData().toString(), txt.getText());
/*     */       }
/*     */     }
/*     */ 
/* 669 */     return params;
/*     */   }
/*     */ 
/*     */   private HashMap<String, String> retrievePrdMap(String prdName)
/*     */   {
/* 678 */     HashMap prdMap = new HashMap();
/*     */ 
/* 680 */     int index = -1;
/* 681 */     for (int ii = 0; ii < productNames.size(); ii++) {
/* 682 */       if (prdName.equals(productNames.get(ii))) {
/* 683 */         index = ii;
/* 684 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 688 */     if (index >= 0) {
/* 689 */       prdMap.putAll((Map)productDefaults.get(index));
/*     */     }
/*     */ 
/* 692 */     prdMap.put("PRODUCT", prdName);
/* 693 */     prdMap.put("CYCLE", this.cycleCombo.getText());
/* 694 */     prdMap.put("FCST_HR", this.fcstCombo.getText());
/*     */ 
/* 696 */     if ((this.advancedGrp != null) && (this.paramText != null)) {
/* 697 */       for (Text txt : this.paramText) {
/* 698 */         currentProductParams.put(txt.getData().toString(), txt.getText());
/*     */       }
/*     */     }
/*     */ 
/* 702 */     return prdMap;
/*     */   }
/*     */ 
/*     */   public void setCntAttrDlg(AttrDlg cntAttrDlg)
/*     */   {
/* 710 */     this.cntAttrDlg = cntAttrDlg;
/*     */   }
/*     */ 
/*     */   public AttrDlg getCntAttrDlg()
/*     */   {
/* 718 */     return this.cntAttrDlg;
/*     */   }
/*     */ 
/*     */   private void showExtension()
/*     */   {
/* 727 */     AbstractEditor currentEditor = PgenUtil.getActiveEditor();
/* 728 */     PgenResource drawingLayer = PgenSession.getInstance().getPgenResource();
/*     */ 
/* 730 */     Contours cnt = getCurrentContours();
/*     */ 
/* 732 */     if (cnt != null)
/*     */     {
/* 734 */       HashMap prm = getParameters();
/*     */ 
/* 736 */       String proj = GraphToGrid.getParamValues(prm, "PROJ");
/* 737 */       String garea = GraphToGrid.getParamValues(prm, "GRDAREA");
/* 738 */       String kxky = GraphToGrid.getParamValues(prm, "KXKY");
/*     */ 
/* 740 */       String[] nkxky = kxky.split(";");
/*     */ 
/* 742 */       int kx = 63;
/* 743 */       int ky = 28;
/* 744 */       if (nkxky.length > 1) {
/* 745 */         kx = Integer.parseInt(nkxky[0]);
/* 746 */         ky = Integer.parseInt(nkxky[1]);
/*     */       }
/*     */ 
/* 753 */       CoordinateTransform gtrans = new CoordinateTransform(proj, garea, kx, ky);
/*     */ 
/* 755 */       Contours extContours = cnt.copy();
/*     */ 
/* 757 */       DECollection extLines = new DECollection("Contours Extensions");
/*     */ 
/* 763 */       ContoursExtension cntExt = new ContoursExtension(extContours, 
/* 764 */         gtrans, kx, ky, this.extColor, null);
/*     */ 
/* 766 */       extLines.add(cntExt.getExtLines());
/*     */ 
/* 768 */       drawingLayer.setGhostLine(extLines);
/*     */ 
/* 770 */       currentEditor.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void makeGrid()
/*     */   {
/* 781 */     Contours cnt = getCurrentContours();
/*     */ 
/* 783 */     if (cnt != null)
/*     */     {
/* 785 */       ContoursToGrid cnt2grd = new ContoursToGrid(cnt, getParameters());
/*     */ 
/* 787 */       if (cnt2grd != null)
/* 788 */         cnt2grd.makeGrid();
/*     */     }
/*     */   }
/*     */ 
/*     */   private Contours getCurrentContours()
/*     */   {
/* 800 */     Contours curCnt = null;
/* 801 */     PgenResource drawingLayer = PgenSession.getInstance().getPgenResource();
/*     */ 
/* 803 */     if ((this.cntAttrDlg instanceof ContoursAttrDlg))
/*     */     {
/* 805 */       curCnt = ((ContoursAttrDlg)this.cntAttrDlg).getCurrentContours();
/*     */ 
/* 807 */       if (curCnt == null) {
/* 808 */         DrawableElement de = drawingLayer.getSelectedDE();
/*     */ 
/* 810 */         if ((de != null) && (
/* 811 */           ((de.getParent() instanceof ContourLine)) || 
/* 812 */           ((de.getParent() instanceof ContourMinmax)) || 
/* 813 */           ((de.getParent() instanceof ContourCircle))))
/*     */         {
/* 815 */           curCnt = (Contours)de.getParent().getParent();
/*     */         }
/*     */       }
/*     */     }
/* 819 */     else if ((this.cntAttrDlg instanceof OutlookAttrDlg)) {
/* 820 */       curCnt = ((OutlookAttrDlg)this.cntAttrDlg).getCurrentOtlk();
/*     */     }
/*     */ 
/* 823 */     return curCnt;
/*     */   }
/*     */ 
/*     */   public void setSingleParameter(String param, String value)
/*     */   {
/* 831 */     if (this.paramText != null)
/* 832 */       for (Text txt : this.paramText)
/* 833 */         if (txt.getData().toString().equals(param)) {
/* 834 */           txt.setText(value);
/* 835 */           break;
/*     */         }
/*     */   }
/*     */ 
/*     */   private HashMap<String, String> generateParameters()
/*     */   {
/* 846 */     HashMap prm = new HashMap(currentProductParams);
/*     */ 
/* 848 */     String gdt = null;
/* 849 */     String gparm = null;
/* 850 */     String level = null;
/* 851 */     String gdoutf = null;
/*     */ 
/* 853 */     IContours dlg = (IContours)this.cntAttrDlg;
/* 854 */     gdt = PgenUtil.calendarToGempakDattim(dlg.getTime1());
/* 855 */     gparm = dlg.getParm();
/* 856 */     level = dlg.getLevel();
/* 857 */     gdoutf = new String(gparm.toLowerCase() + "_" + dlg.getTime1().get(1) + 
/* 858 */       gdt.substring(2, 6) + gdt.substring(7, 9) + 
/* 859 */       this.fcstCombo.getText() + ".grd");
/*     */ 
/* 861 */     prm.put("GPARM", gparm);
/* 862 */     prm.put("GLEVEL", level);
/* 863 */     prm.put("GDATTIM", gdt + this.fcstCombo.getText());
/* 864 */     prm.put("GFUNC", gparm);
/* 865 */     prm.put("GDOUTF", gdoutf);
/*     */ 
/* 868 */     if ((this.cntAttrDlg instanceof OutlookAttrDlg)) {
/* 869 */       OutlookAttrDlg odlg = (OutlookAttrDlg)this.cntAttrDlg;
/* 870 */       String cmap = odlg.getCatmapForType(odlg.getOutlookType());
/* 871 */       prm.put("CATMAP", cmap);
/*     */     }
/*     */ 
/* 874 */     return prm;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.graphtogrid.GraphToGridParamDialog
 * JD-Core Version:    0.6.2
 */