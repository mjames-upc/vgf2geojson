/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.GeometryFactory;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.common.staticdata.SPCCounty;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenWatchBoxModifyTool;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.events.SelectionListener;
/*      */ import org.eclipse.swt.graphics.Font;
/*      */ import org.eclipse.swt.graphics.Point;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Listener;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class WatchInfoDlg extends CaveJFACEDialog
/*      */ {
/*   64 */   private static WatchInfoDlg INSTANCE = null;
/*   65 */   static String specLbl = "LAT              LON              Anchor Relative          Vor Relative       ";
/*   66 */   static String inactLbl = "=====Inactive counties INSIDE the watch area=====\n";
/*   67 */   static String actLbl = "=====Active counties OUTSIDE the watch area=====\n";
/*      */   private WatchFormatDlg fmtDlg;
/*      */   private WatchStatusDlg statusDlg;
/*      */   private static Font txtFt;
/*      */   private static Font cwaFt;
/*      */   private static Font cwaBtnFt;
/*      */   private WatchBoxAttrDlg wbDlg;
/*      */   private Composite top;
/*      */   private Label textLabel;
/*      */   private Text text;
/*      */   private Button specBtn;
/*      */   private Button qcBtn;
/*      */   private Button countyListBtn;
/*      */   private Button createBtn;
/*      */   private Button addBtn;
/*      */   private Button clearBtn;
/*      */   private Button clusteringOnBtn;
/*      */   private Button clusteringOffBtn;
/*      */   private Button wccBtn;
/*      */   private Button lockOnBtn;
/*      */   private Button lockOffBtn;
/*      */   private Composite wfoGrp;
/*      */   private Text wfo;
/*      */   private Composite stGrp;
/*      */   private Composite stPane;
/*      */   private List<Button> stBtns;
/*      */   private Composite cwaGrp;
/*      */   private Composite cwaPane;
/*      */   private Composite wccGrp;
/*      */   private List<CwaComposite> cwaComp;
/*      */ 
/*      */   protected WatchInfoDlg(Shell parentShell, WatchBoxAttrDlg wbDlg)
/*      */   {
/*  154 */     super(parentShell);
/*  155 */     setShellStyle(96);
/*      */ 
/*  157 */     this.wbDlg = wbDlg;
/*  158 */     this.stBtns = new ArrayList();
/*  159 */     this.cwaComp = new ArrayList();
/*      */   }
/*      */ 
/*      */   public static WatchInfoDlg getInstance(Shell parShell, WatchBoxAttrDlg wbDlg)
/*      */   {
/*  169 */     if (INSTANCE == null)
/*      */     {
/*  171 */       INSTANCE = new WatchInfoDlg(parShell, wbDlg);
/*      */     }
/*      */ 
/*  175 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  185 */     this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/*  188 */     GridLayout mainLayout = new GridLayout(1, false);
/*  189 */     mainLayout.marginHeight = 3;
/*  190 */     mainLayout.marginWidth = 3;
/*  191 */     mainLayout.verticalSpacing = 3;
/*  192 */     this.top.setLayout(mainLayout);
/*      */ 
/*  195 */     initializeComponents();
/*      */ 
/*  197 */     return this.top;
/*      */   }
/*      */ 
/*      */   protected void initializeComponents()
/*      */   {
/*  207 */     getShell().setText("Watch Specifications and County List");
/*      */ 
/*  210 */     createRadioButtons();
/*      */ 
/*  212 */     AttrDlg.addSeparator(this.top);
/*      */ 
/*  215 */     this.textLabel = new Label(this.top, 0);
/*  216 */     this.textLabel.setText(specLbl);
/*      */ 
/*  218 */     this.text = new Text(this.top, 2562);
/*  219 */     this.text.setLayoutData(new GridData(500, 100));
/*  220 */     this.text.setEditable(false);
/*      */ 
/*  222 */     if (txtFt == null) createFonts();
/*  223 */     this.text.setFont(txtFt);
/*      */ 
/*  225 */     AttrDlg.addSeparator(this.top);
/*      */ 
/*  228 */     createCountyCtrls();
/*      */ 
/*  230 */     AttrDlg.addSeparator(this.top);
/*      */ 
/*  233 */     this.wfoGrp = new Composite(this.top, 0);
/*      */ 
/*  235 */     GridLayout wfoGl = new GridLayout(2, false);
/*  236 */     wfoGl.verticalSpacing = 0;
/*  237 */     wfoGl.marginHeight = 2;
/*  238 */     wfoGl.marginBottom = 0;
/*      */ 
/*  240 */     this.wfoGrp.setLayout(wfoGl);
/*      */ 
/*  242 */     Label wfoLabel = new Label(this.wfoGrp, 0);
/*  243 */     wfoLabel.setText("WFOs:");
/*  244 */     this.wfo = new Text(this.wfoGrp, 2);
/*  245 */     this.wfo.setEditable(false);
/*  246 */     this.wfo.setFont(txtFt);
/*      */ 
/*  249 */     this.stGrp = new Composite(this.top, 0);
/*  250 */     this.stGrp.setLayout(wfoGl);
/*  251 */     Label stLabel = new Label(this.stGrp, 0);
/*  252 */     stLabel.setText("States:");
/*      */ 
/*  254 */     this.stPane = new Composite(this.stGrp, 0);
/*  255 */     GridLayout stGl = new GridLayout(8, false);
/*  256 */     stGl.verticalSpacing = 0;
/*  257 */     stGl.marginHeight = 3;
/*  258 */     this.stPane.setLayout(stGl);
/*      */ 
/*  261 */     this.cwaGrp = new Composite(this.top, 0);
/*  262 */     this.cwaGrp.setLayout(wfoGl);
/*  263 */     Label cwaLabel = new Label(this.cwaGrp, 0);
/*  264 */     cwaLabel.setText("CWAs:");
/*      */ 
/*  266 */     this.cwaPane = new Composite(this.cwaGrp, 0);
/*  267 */     GridLayout cwaGl = new GridLayout(4, false);
/*  268 */     cwaGl.verticalSpacing = 0;
/*  269 */     cwaGl.marginHeight = 1;
/*  270 */     this.cwaPane.setLayout(cwaGl);
/*      */ 
/*  272 */     AttrDlg.addSeparator(this.top);
/*      */ 
/*  279 */     this.wccGrp = new Composite(this.top, 0);
/*  280 */     GridLayout wccGl = new GridLayout(4, false);
/*  281 */     wccGl.marginLeft = 15;
/*  282 */     this.wccGrp.setLayout(wccGl);
/*      */ 
/*  284 */     this.wccBtn = new Button(this.wccGrp, 8);
/*  285 */     this.wccBtn.setText("WCC/WCL");
/*  286 */     this.wccBtn.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  296 */         boolean openCoordDlg = true;
/*  297 */         if (WatchInfoDlg.this.wbDlg.getWatchBox().getCountyList().isEmpty()) {
/*  298 */           String msg = "Watch has no counties! Set default counties?";
/*      */ 
/*  300 */           MessageDialog confirmDlg = new MessageDialog(
/*  301 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  302 */             "Confirm Delete", null, msg, 
/*  303 */             3, new String[] { "OK", "Cancel" }, 0);
/*  304 */           confirmDlg.open();
/*      */ 
/*  306 */           if (confirmDlg.getReturnCode() == 0) {
/*  307 */             WatchInfoDlg.this.createCounties();
/*      */           }
/*      */           else {
/*  310 */             openCoordDlg = false;
/*      */           }
/*      */         }
/*      */ 
/*  314 */         if (openCoordDlg) {
/*  315 */           WatchCoordDlg coordDlg = WatchCoordDlg.getInstance(WatchInfoDlg.this.getParentShell(), WatchInfoDlg.this.wbDlg);
/*  316 */           coordDlg.setBlockOnOpen(false);
/*  317 */           coordDlg.open();
/*      */         }
/*      */       }
/*      */     });
/*  331 */     Button fmtBtn = new Button(this.wccGrp, 8);
/*  332 */     fmtBtn.setText("Watch Format");
/*  333 */     fmtBtn.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  343 */         boolean openFmtDlg = true;
/*      */ 
/*  345 */         if (WatchInfoDlg.this.wbDlg.getWatchBox().getCountyList().isEmpty()) {
/*  346 */           String msg = "Watch has no counties! Set default counties?";
/*      */ 
/*  348 */           MessageDialog confirmDlg = new MessageDialog(
/*  349 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  350 */             "Set Watch Counties", null, msg, 
/*  351 */             3, new String[] { "OK", "Cancel" }, 0);
/*  352 */           confirmDlg.open();
/*  353 */           if (confirmDlg.getReturnCode() == 0) {
/*  354 */             WatchInfoDlg.this.createCounties();
/*      */           }
/*      */           else {
/*  357 */             openFmtDlg = false;
/*      */           }
/*      */         }
/*      */ 
/*  361 */         if (openFmtDlg) {
/*  362 */           WatchInfoDlg.this.fmtDlg = WatchFormatDlg.getInstance(WatchInfoDlg.this.getParentShell(), WatchInfoDlg.this.wbDlg);
/*  363 */           WatchInfoDlg.this.fmtDlg.setWatchBox(WatchInfoDlg.this.wbDlg.getWatchBox());
/*  364 */           WatchInfoDlg.this.fmtDlg.setBlockOnOpen(false);
/*  365 */           WatchInfoDlg.this.fmtDlg.open();
/*      */         }
/*      */       }
/*      */     });
/*  372 */     Button statusLineBtn = new Button(this.wccGrp, 8);
/*  373 */     statusLineBtn.setText("Add Status Line");
/*  374 */     statusLineBtn.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  383 */         if (WatchInfoDlg.this.wbDlg.getWatchBox().getIssueFlag() == 0) {
/*  384 */           String msg = "Please issue the watch first!";
/*      */ 
/*  386 */           MessageDialog confirmDlg = new MessageDialog(
/*  387 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  388 */             "Warning!", null, msg, 
/*  389 */             2, new String[] { "OK" }, 0);
/*  390 */           confirmDlg.open();
/*      */         }
/*      */         else {
/*  393 */           PgenUtil.setDrawingStatusLineMode(WatchInfoDlg.this.wbDlg.getWatchBox());
/*      */         }
/*      */       }
/*      */     });
/*  400 */     Button statusBtn = new Button(this.wccGrp, 8);
/*  401 */     statusBtn.setText("Watch Status");
/*  402 */     statusBtn.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  411 */         if (WatchInfoDlg.this.wbDlg.getWatchBox().getIssueFlag() == 0) {
/*  412 */           String msg = "Watch has not been issued!";
/*      */ 
/*  414 */           MessageDialog infoDlg = new MessageDialog(
/*  415 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  416 */             "Warning!", null, msg, 
/*  417 */             2, new String[] { "OK" }, 0);
/*  418 */           infoDlg.open();
/*      */         }
/*      */         else {
/*  421 */           WatchInfoDlg.this.statusDlg = new WatchStatusDlg(WatchInfoDlg.this.getParentShell(), WatchInfoDlg.this.wbDlg.getWatchBox());
/*  422 */           WatchInfoDlg.this.statusDlg.setBlockOnOpen(false);
/*  423 */           WatchInfoDlg.this.statusDlg.open();
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void createRadioButtons()
/*      */   {
/*  467 */     Composite btnGrp = new Composite(this.top, 0);
/*  468 */     GridLayout btnGl = new GridLayout(3, false);
/*  469 */     btnGrp.setLayout(btnGl);
/*      */ 
/*  471 */     this.specBtn = new Button(btnGrp, 16);
/*  472 */     this.specBtn.setText("Specifications");
/*  473 */     this.specBtn.setSelection(true);
/*      */ 
/*  475 */     this.specBtn.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  484 */         WatchInfoDlg.this.textLabel.setText(WatchInfoDlg.specLbl);
/*  485 */         WatchInfoDlg.this.text.setText(WatchInfoDlg.this.wbDlg.getWatchBox().getSpec());
/*      */       }
/*      */     });
/*  491 */     this.qcBtn = new Button(btnGrp, 16);
/*  492 */     this.qcBtn.setText("QC Counties");
/*  493 */     this.qcBtn.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  502 */         WatchInfoDlg.this.textLabel.setText(" UGC    State   County Name            Lat/Lon              FIPS      WFO ");
/*      */ 
/*  504 */         WatchInfoDlg.this.text.setText(WatchInfoDlg.inactLbl + WatchInfoDlg.this.wbDlg.getWatchBox().formatCountyInfo(WatchInfoDlg.this.wbDlg.getWatchBox().getInactiveCountiesInWB()) + "\n" + 
/*  505 */           WatchInfoDlg.actLbl + WatchInfoDlg.this.wbDlg.getWatchBox().formatCountyInfo(WatchInfoDlg.this.wbDlg.getWatchBox().getActiveCountiesOutsideWB()));
/*      */       }
/*      */     });
/*  511 */     this.countyListBtn = new Button(btnGrp, 16);
/*  512 */     this.countyListBtn.setText("County List");
/*      */ 
/*  514 */     this.countyListBtn.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  523 */         WatchInfoDlg.this.textLabel.setText(" UGC    State   County Name            Lat/Lon              FIPS      WFO ");
/*  524 */         WatchInfoDlg.this.text.setText(WatchInfoDlg.this.wbDlg.getWatchBox().formatCountyInfo(WatchInfoDlg.this.wbDlg.getWatchBox().getCountyList()));
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void createCountyCtrls()
/*      */   {
/*  538 */     Composite cntyGrp = new Composite(this.top, 0);
/*  539 */     GridLayout gl = new GridLayout(5, false);
/*  540 */     cntyGrp.setLayout(gl);
/*      */ 
/*  542 */     Label cLabel = new Label(cntyGrp, 0);
/*  543 */     cLabel.setText("Counties:");
/*      */ 
/*  545 */     this.createBtn = new Button(cntyGrp, 8);
/*  546 */     this.createBtn.setText("Create");
/*      */ 
/*  548 */     this.createBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*  552 */         WatchInfoDlg.this.createCounties();
/*      */       }
/*      */     });
/*  557 */     this.addBtn = new Button(cntyGrp, 8);
/*  558 */     this.addBtn.setText("Add/Del");
/*  559 */     this.addBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*  563 */         WatchInfoDlg.this.enableAllButtons(false);
/*  564 */         WatchInfoDlg.this.wbDlg.enableDspBtn(false);
/*  565 */         WatchInfoDlg.this.wbDlg.buttonBar.setEnabled(false);
/*  566 */         WatchInfoDlg.this.wbDlg.getWbTool().setAddDelCntyHandler();
/*      */       }
/*      */     });
/*  570 */     this.clearBtn = new Button(cntyGrp, 8);
/*  571 */     this.clearBtn.setText("Clear");
/*      */ 
/*  573 */     this.clearBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*  577 */         WatchBox newWb = (WatchBox)WatchInfoDlg.this.wbDlg.getWatchBox().copy();
/*  578 */         newWb.clearCntyList();
/*  579 */         WatchInfoDlg.this.wbDlg.drawingLayer.replaceElement(WatchInfoDlg.this.wbDlg.getWatchBox(), newWb);
/*  580 */         WatchInfoDlg.this.wbDlg.setWatchBox(newWb);
/*  581 */         WatchInfoDlg.this.wbDlg.drawingLayer.setSelected(newWb);
/*      */ 
/*  583 */         WatchInfoDlg.this.clearCwaPane();
/*  584 */         WatchInfoDlg.this.setStatesWFOs();
/*  585 */         WatchInfoDlg.this.wbDlg.mapEditor.refresh();
/*      */       }
/*      */     });
/*  590 */     Composite grp11 = new Composite(cntyGrp, 0);
/*  591 */     GridLayout gl1 = new GridLayout(3, false);
/*  592 */     grp11.setLayout(gl1);
/*      */ 
/*  594 */     Label clstrLabel = new Label(grp11, 0);
/*  595 */     clstrLabel.setText("Clustering:");
/*      */ 
/*  597 */     this.clusteringOffBtn = new Button(grp11, 16);
/*  598 */     this.clusteringOffBtn.setText("Off");
/*      */ 
/*  600 */     this.clusteringOnBtn = new Button(grp11, 16);
/*  601 */     this.clusteringOnBtn.setText("On");
/*  602 */     this.clusteringOnBtn.setSelection(true);
/*      */ 
/*  605 */     Composite lockGrp = new Composite(this.top, 0);
/*      */ 
/*  607 */     GridLayout gl2 = new GridLayout(3, false);
/*  608 */     lockGrp.setLayout(gl2);
/*  609 */     gl2.marginHeight = 2;
/*      */ 
/*  611 */     Label lockLabel = new Label(lockGrp, 0);
/*  612 */     lockLabel.setText("County Lock:");
/*      */ 
/*  614 */     this.lockOffBtn = new Button(lockGrp, 16);
/*  615 */     this.lockOffBtn.setText("Off");
/*  616 */     this.lockOffBtn.setSelection(true);
/*  617 */     this.lockOffBtn.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  627 */         WatchInfoDlg.this.createBtn.setEnabled(true);
/*  628 */         WatchInfoDlg.this.addBtn.setEnabled(true);
/*  629 */         WatchInfoDlg.this.clearBtn.setEnabled(true);
/*  630 */         WatchInfoDlg.this.clusteringOnBtn.setEnabled(true);
/*  631 */         WatchInfoDlg.this.clusteringOffBtn.setEnabled(true);
/*  632 */         if (WatchInfoDlg.this.stBtns != null) {
/*  633 */           for (Button btn : WatchInfoDlg.this.stBtns) {
/*  634 */             btn.setEnabled(true);
/*      */           }
/*      */         }
/*  637 */         WatchInfoDlg.this.enableCWAComp(true);
/*      */       }
/*      */     });
/*  641 */     this.lockOnBtn = new Button(lockGrp, 16);
/*  642 */     this.lockOnBtn.setText("On");
/*      */ 
/*  644 */     this.lockOnBtn.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  654 */         WatchInfoDlg.this.createBtn.setEnabled(false);
/*  655 */         WatchInfoDlg.this.addBtn.setEnabled(false);
/*  656 */         WatchInfoDlg.this.clearBtn.setEnabled(false);
/*  657 */         WatchInfoDlg.this.clusteringOnBtn.setEnabled(false);
/*  658 */         WatchInfoDlg.this.clusteringOffBtn.setEnabled(false);
/*  659 */         if (WatchInfoDlg.this.stBtns != null) {
/*  660 */           for (Button btn : WatchInfoDlg.this.stBtns) {
/*  661 */             btn.setEnabled(false);
/*      */           }
/*      */         }
/*  664 */         WatchInfoDlg.this.enableCWAComp(false);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public Control createButtonBar(Composite parent)
/*      */   {
/*  674 */     return null;
/*      */   }
/*      */ 
/*      */   public int open()
/*      */   {
/*  683 */     if (getShell() == null) {
/*  684 */       create();
/*      */     }
/*      */ 
/*  687 */     getShell().setLocation(getShell().getParent().getLocation());
/*  688 */     return super.open();
/*      */   }
/*      */ 
/*      */   private void setTextField()
/*      */   {
/*  696 */     if (this.specBtn.getSelection()) {
/*  697 */       this.text.setText(this.wbDlg.getWatchBox().getSpec());
/*      */     }
/*  699 */     else if (this.countyListBtn.getSelection()) {
/*  700 */       this.text.setText(this.wbDlg.getWatchBox().formatCountyInfo(this.wbDlg.getWatchBox().getCountyList()));
/*      */     }
/*  702 */     else if (this.qcBtn.getSelection())
/*  703 */       this.text.setText(inactLbl + this.wbDlg.getWatchBox().formatCountyInfo(this.wbDlg.getWatchBox().getInactiveCountiesInWB()) + "\n" + 
/*  704 */         actLbl + this.wbDlg.getWatchBox().formatCountyInfo(this.wbDlg.getWatchBox().getActiveCountiesOutsideWB()));
/*      */   }
/*      */ 
/*      */   public void setStatesWFOs()
/*      */   {
/*  713 */     setTextField();
/*  714 */     this.wfo.setText(formatWfoStr(this.wbDlg.getWatchBox().getWFOs()));
/*  715 */     createStateChkBoxes(this.wbDlg.getWatchBox().getStates());
/*      */ 
/*  717 */     this.stPane.layout();
/*  718 */     this.stGrp.pack(true);
/*  719 */     this.stGrp.layout();
/*      */ 
/*  721 */     this.cwaPane.layout();
/*  722 */     this.cwaGrp.pack(true);
/*  723 */     this.cwaGrp.layout();
/*      */ 
/*  725 */     this.wccGrp.layout();
/*  726 */     this.wccGrp.pack(true);
/*  727 */     this.wccGrp.layout();
/*      */ 
/*  729 */     this.top.pack(true);
/*  730 */     this.top.layout();
/*      */ 
/*  732 */     getShell().pack(true);
/*  733 */     getShell().layout();
/*  734 */     getShell().redraw(0, 0, getShell().getSize().x, 
/*  735 */       getShell().getSize().y, true);
/*      */   }
/*      */ 
/*      */   public void clearCwaPane()
/*      */   {
/*  742 */     Iterator it = this.cwaComp.iterator();
/*  743 */     while (it.hasNext()) {
/*  744 */       CwaComposite cwa = (CwaComposite)it.next();
/*  745 */       cwa.comp.dispose();
/*  746 */       it.remove();
/*      */     }
/*      */ 
/*  749 */     this.cwaPane.layout();
/*  750 */     this.cwaGrp.pack(true);
/*  751 */     this.cwaGrp.layout();
/*      */   }
/*      */ 
/*      */   public void createCWAs(List<String> cwas)
/*      */   {
/*  760 */     if ((cwas != null) && (!cwas.isEmpty()))
/*      */     {
/*  763 */       for (String aCWA : cwas)
/*      */       {
/*  765 */         this.cwaComp.add(new CwaComposite(aCWA, null));
/*  766 */         this.cwaPane.layout();
/*  767 */         this.cwaPane.pack(true);
/*      */       }
/*      */ 
/*  771 */       setCwaBtns();
/*      */     }
/*      */ 
/*  774 */     this.cwaPane.layout();
/*  775 */     this.cwaGrp.pack(true);
/*  776 */     this.cwaGrp.layout();
/*      */   }
/*      */ 
/*      */   private void createStateChkBoxes(List<String> states)
/*      */   {
/*      */     Button btn;
/*  786 */     if (this.stBtns != null) {
/*  787 */       Iterator it = this.stBtns.iterator();
/*  788 */       while (it.hasNext()) {
/*  789 */         btn = (Button)it.next();
/*  790 */         if (!states.contains(btn.getText())) {
/*  791 */           btn.dispose();
/*  792 */           it.remove();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  799 */     if ((states != null) && (!states.isEmpty())) {
/*  800 */       for (String st : states)
/*      */       {
/*  803 */         boolean notIn = true;
/*  804 */         for (Button btn : this.stBtns) {
/*  805 */           if (st.equalsIgnoreCase(btn.getText())) {
/*  806 */             notIn = false;
/*  807 */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  812 */         if (notIn) {
/*  813 */           Button stBtn = new Button(this.stPane, 32);
/*  814 */           stBtn.setText(st);
/*  815 */           stBtn.setSelection(true);
/*      */ 
/*  818 */           stBtn.addSelectionListener(new SelectionListener()
/*      */           {
/*      */             public void widgetDefaultSelected(SelectionEvent e)
/*      */             {
/*      */             }
/*      */ 
/*      */             public void widgetSelected(SelectionEvent e)
/*      */             {
/*  828 */               WatchBox newWb = (WatchBox)WatchInfoDlg.this.wbDlg.getWatchBox().copy();
/*  829 */               newWb.setCountyList(WatchInfoDlg.this.wbDlg.getWatchBox().getCountyList());
/*  830 */               newWb.removeState(((Button)e.widget).getText());
/*  831 */               WatchInfoDlg.this.wbDlg.drawingLayer.replaceElement(WatchInfoDlg.this.wbDlg.getWatchBox(), newWb);
/*  832 */               WatchInfoDlg.this.wbDlg.setWatchBox(newWb);
/*  833 */               WatchInfoDlg.this.wbDlg.drawingLayer.setSelected(newWb);
/*      */ 
/*  835 */               WatchInfoDlg.this.setStatesWFOs();
/*  836 */               WatchInfoDlg.this.setCwaBtns();
/*  837 */               WatchInfoDlg.this.wbDlg.mapEditor.refresh();
/*      */             }
/*      */           });
/*  843 */           this.stBtns.add(stBtn);
/*  844 */           this.stPane.layout();
/*  845 */           this.stPane.pack(true);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  850 */     this.stPane.layout();
/*  851 */     this.stGrp.pack(true);
/*  852 */     this.stGrp.layout();
/*      */   }
/*      */ 
/*      */   public boolean close()
/*      */   {
/*  861 */     this.stBtns.clear();
/*  862 */     if (this.fmtDlg != null) this.fmtDlg.close();
/*  863 */     WatchCoordDlg.getInstance(getParentShell(), this.wbDlg).close();
/*  864 */     if (this.wbDlg != null) {
/*  865 */       if (this.wbDlg.getWbTool() != null) this.wbDlg.getWbTool().resetMouseHandler();
/*  866 */       this.wbDlg.enableDspBtn(true);
/*      */     }
/*      */ 
/*  869 */     return super.close();
/*      */   }
/*      */ 
/*      */   public static String formatWfoStr(List<String> wfos)
/*      */   {
/*  880 */     String wfoStr = "";
/*  881 */     for (int ii = 0; ii < 42; ii++) {
/*  882 */       wfoStr = wfoStr + " ";
/*      */     }
/*      */ 
/*  885 */     if ((wfos != null) && (!wfos.isEmpty()))
/*      */     {
/*  887 */       wfoStr = "";
/*  888 */       int nWfo = 0;
/*  889 */       for (String aWfo : wfos)
/*      */       {
/*  891 */         if ((aWfo != null) && (!wfoStr.contains(aWfo)))
/*      */         {
/*  894 */           if ((nWfo != 0) && (nWfo % 7 == 0)) wfoStr = wfoStr + "\n";
/*  895 */           wfoStr = wfoStr + "...";
/*  896 */           wfoStr = wfoStr + aWfo;
/*  897 */           nWfo++;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  903 */     return wfoStr;
/*      */   }
/*      */ 
/*      */   private void enableCWAComp(boolean flag)
/*      */   {
/*  911 */     for (CwaComposite cwa : this.cwaComp) {
/*  912 */       cwa.comp.setEnabled(flag);
/*  913 */       cwa.lbl.setEnabled(flag);
/*  914 */       cwa.inBtn.setEnabled(flag);
/*  915 */       cwa.outBtn.setEnabled(flag);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isCountyLock()
/*      */   {
/*  924 */     return this.lockOnBtn.getSelection();
/*      */   }
/*      */ 
/*      */   public boolean isClusteringOn()
/*      */   {
/*  932 */     return this.clusteringOnBtn.getSelection();
/*      */   }
/*      */ 
/*      */   public void setCwaBtns()
/*      */   {
/*  942 */     List cntyInWb = this.wbDlg.getWatchBox().getCountyList();
/*  943 */     for (CwaComposite cwa : this.cwaComp)
/*      */     {
/*  946 */       int countiesIn = getCountiesOfCwa(cwa.lbl.getText(), cntyInWb);
/*      */ 
/*  948 */       if (countiesIn == 0) {
/*  949 */         cwa.outBtn.setSelection(true);
/*  950 */         cwa.inBtn.setSelection(false);
/*      */       }
/*      */       else
/*      */       {
/*  955 */         int totalCounties = getCountiesOfCwa(cwa.lbl.getText(), PgenStaticDataProvider.getProvider().getSPCCounties());
/*  956 */         if (totalCounties == countiesIn) {
/*  957 */           cwa.inBtn.setSelection(true);
/*  958 */           cwa.outBtn.setSelection(false);
/*      */         }
/*      */         else
/*      */         {
/*  962 */           cwa.inBtn.setSelection(false);
/*  963 */           cwa.outBtn.setSelection(false);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int getCountiesOfCwa(String cwa, List<SPCCounty> counties)
/*      */   {
/*  976 */     int total = 0;
/*  977 */     for (SPCCounty cnty : counties) {
/*  978 */       if ((cnty.getWfo() != null) && (cwa.equalsIgnoreCase(cnty.getWfo()))) {
/*  979 */         total++;
/*      */       }
/*      */     }
/*  982 */     return total;
/*      */   }
/*      */ 
/*      */   public void enableAllButtons(boolean flag)
/*      */   {
/* 1092 */     this.createBtn.setEnabled(flag);
/* 1093 */     this.addBtn.setEnabled(flag);
/* 1094 */     this.clearBtn.setEnabled(flag);
/* 1095 */     this.clusteringOnBtn.setEnabled(flag);
/* 1096 */     this.clusteringOffBtn.setEnabled(flag);
/* 1097 */     this.lockOnBtn.setEnabled(flag);
/* 1098 */     this.lockOffBtn.setEnabled(flag);
/* 1099 */     this.specBtn.setEnabled(flag);
/* 1100 */     this.qcBtn.setEnabled(flag);
/* 1101 */     this.countyListBtn.setEnabled(flag);
/*      */ 
/* 1103 */     this.wccBtn.setEnabled(flag);
/*      */ 
/* 1105 */     if (this.stBtns != null) {
/* 1106 */       for (Button btn : this.stBtns) {
/* 1107 */         btn.setEnabled(flag);
/*      */       }
/*      */     }
/* 1110 */     enableCWAComp(flag);
/*      */   }
/*      */ 
/*      */   private void createCounties()
/*      */   {
/* 1119 */     this.wbDlg.getWatchBox().clearCntyList();
/*      */ 
/* 1122 */     GeometryFactory gf = new GeometryFactory();
/* 1123 */     Coordinate[] pts = new Coordinate[7];
/* 1124 */     pts[0] = ((Coordinate)this.wbDlg.getWatchBox().getPoints().get(1));
/* 1125 */     pts[1] = ((Coordinate)this.wbDlg.getWatchBox().getPoints().get(2));
/* 1126 */     pts[2] = ((Coordinate)this.wbDlg.getWatchBox().getPoints().get(3));
/* 1127 */     pts[3] = ((Coordinate)this.wbDlg.getWatchBox().getPoints().get(5));
/* 1128 */     pts[4] = ((Coordinate)this.wbDlg.getWatchBox().getPoints().get(6));
/* 1129 */     pts[5] = ((Coordinate)this.wbDlg.getWatchBox().getPoints().get(7));
/* 1130 */     pts[6] = ((Coordinate)this.wbDlg.getWatchBox().getPoints().get(1));
/*      */ 
/* 1132 */     Polygon watchGeo = new Polygon(gf.createLinearRing(pts), null, gf);
/*      */ 
/* 1134 */     WatchBox newWb = (WatchBox)this.wbDlg.getWatchBox().copy();
/*      */ 
/* 1136 */     List counties = PgenStaticDataProvider.getProvider().getCountiesInGeometry(watchGeo);
/*      */ 
/* 1138 */     for (SPCCounty county : counties) {
/* 1139 */       if (isClusteringOn()) {
/* 1140 */         newWb.addClstCnty(county);
/*      */       }
/*      */       else {
/* 1143 */         newWb.addCounty(county);
/*      */       }
/*      */     }
/*      */ 
/* 1147 */     clearCwaPane();
/* 1148 */     this.top.pack(true);
/* 1149 */     this.top.layout();
/*      */ 
/* 1151 */     getShell().pack(true);
/* 1152 */     getShell().layout();
/*      */ 
/* 1154 */     newWb.update(this.wbDlg);
/* 1155 */     this.wbDlg.drawingLayer.replaceElement(this.wbDlg.getWatchBox(), newWb);
/* 1156 */     this.wbDlg.drawingLayer.setSelected(newWb);
/*      */ 
/* 1158 */     this.wbDlg.setWatchBox(newWb);
/* 1159 */     createCWAs(newWb.getWFOs());
/* 1160 */     setStatesWFOs();
/*      */ 
/* 1162 */     this.wbDlg.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   private void createFonts()
/*      */   {
/* 1170 */     txtFt = new Font(getShell().getDisplay(), "Courier New", 12, 0);
/* 1171 */     cwaBtnFt = new Font(getShell().getDisplay(), "Courier New", 9, 0);
/* 1172 */     cwaFt = new Font(getShell().getDisplay(), "Courier New", 12, 0);
/*      */   }
/*      */ 
/*      */   private class CwaComposite
/*      */   {
/*      */     private Composite comp;
/*      */     private Label lbl;
/*      */     private Button inBtn;
/*      */     private Button outBtn;
/*      */ 
/*      */     private CwaComposite(final String cwa)
/*      */     {
/* 1011 */       this.comp = new Composite(WatchInfoDlg.this.cwaPane, 2048);
/*      */ 
/* 1013 */       GridLayout cwaGl = new GridLayout(2, false);
/* 1014 */       cwaGl.marginHeight = 2;
/* 1015 */       cwaGl.marginWidth = 4;
/* 1016 */       this.comp.setLayout(cwaGl);
/*      */ 
/* 1019 */       Composite ioBtnComp = new Composite(this.comp, 0);
/*      */ 
/* 1021 */       GridLayout btnGl = new GridLayout(1, false);
/* 1022 */       btnGl.marginHeight = 0;
/* 1023 */       ioBtnComp.setLayout(btnGl);
/*      */ 
/* 1025 */       this.inBtn = new Button(ioBtnComp, 16);
/* 1026 */       this.inBtn.setFont(WatchInfoDlg.cwaBtnFt);
/* 1027 */       this.inBtn.setText("IN");
/*      */ 
/* 1029 */       this.inBtn.addSelectionListener(new SelectionListener()
/*      */       {
/*      */         public void widgetDefaultSelected(SelectionEvent e)
/*      */         {
/*      */         }
/*      */ 
/*      */         public void widgetSelected(SelectionEvent e)
/*      */         {
/* 1039 */           WatchBox newWb = (WatchBox)WatchInfoDlg.this.wbDlg.getWatchBox().copy();
/* 1040 */           newWb.setCountyList(WatchInfoDlg.this.wbDlg.getWatchBox().getCountyList());
/* 1041 */           newWb.addCwa(cwa);
/* 1042 */           WatchInfoDlg.this.wbDlg.drawingLayer.replaceElement(WatchInfoDlg.this.wbDlg.getWatchBox(), newWb);
/* 1043 */           WatchInfoDlg.this.wbDlg.setWatchBox(newWb);
/* 1044 */           WatchInfoDlg.this.wbDlg.drawingLayer.setSelected(newWb);
/*      */ 
/* 1046 */           WatchInfoDlg.this.setStatesWFOs();
/* 1047 */           WatchInfoDlg.this.wbDlg.mapEditor.refresh();
/*      */         }
/*      */       });
/* 1052 */       this.outBtn = new Button(ioBtnComp, 16);
/* 1053 */       this.outBtn.setFont(WatchInfoDlg.cwaBtnFt);
/* 1054 */       this.outBtn.setText("OUT");
/*      */ 
/* 1057 */       this.lbl = new Label(this.comp, 0);
/* 1058 */       this.lbl.setText(cwa);
/* 1059 */       this.lbl.setFont(WatchInfoDlg.cwaFt);
/*      */ 
/* 1061 */       this.outBtn.addSelectionListener(new SelectionListener()
/*      */       {
/*      */         public void widgetDefaultSelected(SelectionEvent e)
/*      */         {
/*      */         }
/*      */ 
/*      */         public void widgetSelected(SelectionEvent e)
/*      */         {
/* 1071 */           WatchBox newWb = (WatchBox)WatchInfoDlg.this.wbDlg.getWatchBox().copy();
/* 1072 */           newWb.setCountyList(WatchInfoDlg.this.wbDlg.getWatchBox().getCountyList());
/* 1073 */           newWb.removeCwa(cwa);
/* 1074 */           WatchInfoDlg.this.wbDlg.drawingLayer.replaceElement(WatchInfoDlg.this.wbDlg.getWatchBox(), newWb);
/* 1075 */           WatchInfoDlg.this.wbDlg.setWatchBox(newWb);
/* 1076 */           WatchInfoDlg.this.wbDlg.drawingLayer.setSelected(newWb);
/*      */ 
/* 1078 */           WatchInfoDlg.this.setStatesWFOs();
/* 1079 */           WatchInfoDlg.this.wbDlg.mapEditor.refresh();
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchInfoDlg
 * JD-Core Version:    0.6.2
 */