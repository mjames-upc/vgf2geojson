/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.common.serialization.SerializationUtil;
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.BPGeography;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.Basin;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.Breakpoint;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.BreakpointPair;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.ITca;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.StormAdvisoryNumber;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.TropicalCycloneAdvisory;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenTcaTool;
/*      */ import java.io.File;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.TimeZone;
/*      */ import org.eclipse.jface.dialogs.IDialogConstants;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.events.SelectionListener;
/*      */ import org.eclipse.swt.events.VerifyEvent;
/*      */ import org.eclipse.swt.events.VerifyListener;
/*      */ import org.eclipse.swt.layout.FillLayout;
/*      */ import org.eclipse.swt.layout.FormAttachment;
/*      */ import org.eclipse.swt.layout.FormData;
/*      */ import org.eclipse.swt.layout.FormLayout;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.layout.RowLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.DateTime;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Spinner;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class TcaAttrDlg extends AttrDlg
/*      */   implements ITca, SelectionListener
/*      */ {
/*   78 */   static TcaAttrDlg INSTANCE = null;
/*      */ 
/*   80 */   private static TcaAttrInfo info = null;
/*      */ 
/*   82 */   private PgenTcaTool tcaTool = null;
/*      */   private static final int SAVE_ID = 8610;
/*      */   private static final String SAVE_LABEL = "Save TCA";
/*      */   private static final int CREATE_TEXT_ID = 8611;
/*      */   private static final String CREATE_TEXT_LABEL = "Create Text";
/*      */   private static final int CANCEL_ID = 8612;
/*      */   private static final String CANCEL_LABEL = "Cancel All";
/*      */   public static final String PGEN_TCA_ATTR_INFO = "TCAinfo.xml";
/*   98 */   private final String APPLY = "Apply";
/*      */ 
/*  100 */   private final String DELETE_SEGMENT = "Delete Segment";
/*      */ 
/*  102 */   private final String NEW_SEGMENT = "New Segment";
/*      */ 
/*  104 */   private final String SEV_TROPICAL_STORM = "Tropical Storm";
/*      */ 
/*  106 */   private final String ADVISORY_WATCH = "Watch";
/*      */ 
/*  108 */   private final String BREAKPOINT_OFFICIAL = "Official";
/*      */ 
/*  110 */   private final String GEOG_TYPE_NONE = "None";
/*      */ 
/*  112 */   private Composite top = null;
/*      */ 
/*  114 */   private Combo statusItems = null;
/*      */ 
/*  116 */   private Combo stormTypes = null;
/*      */ 
/*  118 */   private Combo basinTypes = null;
/*      */ 
/*  120 */   private Combo specialGeogTypes = null;
/*      */ 
/*  122 */   private Text advisoryNumber = null;
/*      */ 
/*  124 */   private Text stormNameField = null;
/*      */ 
/*  126 */   private Spinner stormNumber = null;
/*      */ 
/*  128 */   private Combo timeZoneTypes = null;
/*      */ 
/*  130 */   private Combo severityTypes = null;
/*      */ 
/*  132 */   private Combo advisoryTypes = null;
/*      */ 
/*  134 */   private Combo breakpointTypes = null;
/*      */ 
/*  136 */   private Text validTime = null;
/*      */ 
/*  138 */   private DateTime validDate = null;
/*      */ 
/*  140 */   private Coordinate textLocation = new Coordinate(-80.400000000000006D, 25.800000000000001D);
/*      */   private Button apply;
/*      */   private Button deleteSegment;
/*      */   private Text bkpt1Field;
/*      */   private Text bkpt2Field;
/*  150 */   private org.eclipse.swt.widgets.List breakpointList = null;
/*      */   private ArrayList<TropicalCycloneAdvisory> advisories;
/*      */   private String dataURI;
/*      */ 
/*      */   private TcaAttrDlg(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  164 */     super(parShell);
/*      */ 
/*  166 */     this.advisories = new ArrayList();
/*      */   }
/*      */ 
/*      */   public static TcaAttrDlg getInstance(Shell parShell)
/*      */   {
/*  179 */     if (INSTANCE == null)
/*      */     {
/*      */       try
/*      */       {
/*  183 */         INSTANCE = new TcaAttrDlg(parShell);
/*      */       }
/*      */       catch (VizException e)
/*      */       {
/*  187 */         e.printStackTrace();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  192 */     readOptions();
/*      */ 
/*  194 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public int open()
/*      */   {
/*  206 */     int ret = super.open();
/*  207 */     this.advisories.clear();
/*  208 */     return ret;
/*      */   }
/*      */ 
/*      */   public void createButtonsForButtonBar(Composite parent)
/*      */   {
/*  217 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/*  218 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*      */ 
/*  221 */     createButton(parent, 8610, "Save TCA", true);
/*  222 */     getButton(8610).setEnabled(false);
/*      */ 
/*  225 */     createButton(parent, 8611, "Create Text", true);
/*  226 */     getButton(8611).setEnabled(false);
/*      */ 
/*  229 */     createButton(parent, 8612, "Cancel All", true);
/*  230 */     getButton(8612).setEnabled(false);
/*      */ 
/*  233 */     createButton(parent, 12, 
/*  234 */       IDialogConstants.CLOSE_LABEL, true);
/*      */ 
/*  236 */     getButton(8612).setLayoutData(
/*  237 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*  238 */     getButton(12).setLayoutData(
/*  239 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*  240 */     getButton(8611).setLayoutData(
/*  241 */       new GridData(ctrlBtnWidth + 10, ctrlBtnHeight));
/*  242 */     getButton(8610).setLayoutData(
/*  243 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  252 */     this.top = ((Composite)super.createDialogArea(parent));
/*  253 */     getShell().setText("TCA Attributes");
/*      */ 
/*  256 */     GridLayout mainLayout = new GridLayout(1, true);
/*  257 */     mainLayout.marginHeight = 3;
/*  258 */     mainLayout.marginWidth = 3;
/*  259 */     this.top.setLayout(mainLayout);
/*      */ 
/*  264 */     Group g1 = new Group(this.top, 16);
/*  265 */     g1.setLayoutData(new GridData(768));
/*  266 */     createStormInfoArea(g1);
/*      */ 
/*  272 */     Group g2 = new Group(this.top, 16);
/*  273 */     g2.setLayoutData(new GridData(768));
/*  274 */     createBreakpointTools(g2);
/*      */ 
/*  279 */     Group g3 = new Group(this.top, 16);
/*  280 */     g3.setLayoutData(new GridData(768));
/*  281 */     createBreakpointList(g3);
/*      */ 
/*  284 */     return this.top;
/*      */   }
/*      */ 
/*      */   private void createStormInfoArea(Group g1)
/*      */   {
/*  293 */     FormLayout layout = new FormLayout();
/*  294 */     layout.marginHeight = 3;
/*  295 */     layout.marginWidth = 3;
/*  296 */     g1.setLayout(layout);
/*      */ 
/*  301 */     Label statusLabel = new Label(g1, 0);
/*  302 */     statusLabel.setText("Issuing Status:");
/*  303 */     FormData fd = new FormData();
/*  304 */     fd.left = new FormAttachment(0, 10);
/*  305 */     fd.top = new FormAttachment(0, 10);
/*  306 */     statusLabel.setLayoutData(fd);
/*      */ 
/*  311 */     this.statusItems = new Combo(g1, 12);
/*  312 */     for (String st : info.getStatusList()) {
/*  313 */       this.statusItems.add(st);
/*      */     }
/*  315 */     this.statusItems.setText(this.statusItems.getItem(0));
/*  316 */     fd = new FormData();
/*  317 */     fd.left = new FormAttachment(0, 10);
/*  318 */     fd.top = new FormAttachment(statusLabel, 5, 1024);
/*  319 */     this.statusItems.setLayoutData(fd);
/*  320 */     int idx = this.statusItems.indexOf("Operational");
/*  321 */     if (idx != -1) {
/*  322 */       this.statusItems.select(idx);
/*      */     }
/*      */ 
/*  327 */     Label typeLabel = new Label(g1, 0);
/*  328 */     typeLabel.setText("Storm Type:");
/*  329 */     fd = new FormData();
/*  330 */     fd.top = new FormAttachment(0, 10);
/*      */ 
/*  333 */     typeLabel.setLayoutData(fd);
/*      */ 
/*  338 */     this.stormTypes = new Combo(g1, 12);
/*  339 */     for (String st : info.getTypeList()) {
/*  340 */       this.stormTypes.add(st);
/*      */     }
/*  342 */     this.stormTypes.setText(this.stormTypes.getItem(0));
/*  343 */     fd = new FormData();
/*  344 */     fd.left = new FormAttachment(this.statusItems, 50, 131072);
/*  345 */     fd.right = new FormAttachment(100, -10);
/*  346 */     fd.top = new FormAttachment(typeLabel, 5, 1024);
/*  347 */     this.stormTypes.setLayoutData(fd);
/*      */ 
/*  350 */     fd = (FormData)typeLabel.getLayoutData();
/*  351 */     fd.left = new FormAttachment(this.stormTypes, 0, 16384);
/*      */ 
/*  356 */     Label basinLabel = new Label(g1, 0);
/*  357 */     basinLabel.setText("Basin:");
/*  358 */     fd = new FormData();
/*      */ 
/*  360 */     fd.left = new FormAttachment(0, 10);
/*  361 */     fd.top = new FormAttachment(this.statusItems, 15, 1024);
/*  362 */     basinLabel.setLayoutData(fd);
/*      */ 
/*  367 */     this.basinTypes = new Combo(g1, 12);
/*  368 */     for (String st : info.getBasinList()) {
/*  369 */       this.basinTypes.add(st);
/*      */     }
/*  371 */     this.basinTypes.setText(this.basinTypes.getItem(0));
/*  372 */     fd = new FormData();
/*  373 */     fd.left = new FormAttachment(basinLabel, 30, 131072);
/*      */ 
/*  375 */     fd.top = new FormAttachment(this.statusItems, 10, 1024);
/*  376 */     fd.right = new FormAttachment(this.statusItems, 0, 131072);
/*  377 */     this.basinTypes.setLayoutData(fd);
/*      */ 
/*  382 */     Label stormNameLabel = new Label(g1, 0);
/*  383 */     stormNameLabel.setText("Name:");
/*  384 */     fd = new FormData();
/*  385 */     fd.top = new FormAttachment(this.stormTypes, 15, 1024);
/*  386 */     fd.left = new FormAttachment(this.stormTypes, 0, 16384);
/*  387 */     stormNameLabel.setLayoutData(fd);
/*      */ 
/*  392 */     this.stormNameField = new Text(g1, 2052);
/*  393 */     fd = new FormData();
/*  394 */     fd.top = new FormAttachment(this.stormTypes, 10, 1024);
/*  395 */     fd.left = new FormAttachment(stormNameLabel, 10, 131072);
/*  396 */     fd.right = new FormAttachment(this.stormTypes, 0, 131072);
/*  397 */     this.stormNameField.setLayoutData(fd);
/*      */ 
/*  402 */     Label stormNumberLabel = new Label(g1, 0);
/*  403 */     stormNumberLabel.setText("Storm#:");
/*  404 */     fd = new FormData();
/*      */ 
/*  406 */     fd.left = new FormAttachment(0, 10);
/*  407 */     fd.top = new FormAttachment(this.basinTypes, 15, 1024);
/*  408 */     stormNumberLabel.setLayoutData(fd);
/*      */ 
/*  413 */     this.stormNumber = new Spinner(g1, 2048);
/*  414 */     fd = new FormData();
/*  415 */     fd.left = new FormAttachment(stormNumberLabel, 35, 131072);
/*  416 */     fd.top = new FormAttachment(this.basinTypes, 10, 1024);
/*  417 */     fd.right = new FormAttachment(this.statusItems, 0, 131072);
/*  418 */     this.stormNumber.setLayoutData(fd);
/*  419 */     this.stormNumber.setMinimum(1);
/*      */ 
/*  424 */     Label validTimeLabel = new Label(g1, 0);
/*  425 */     validTimeLabel.setText("Valid Time:");
/*  426 */     fd = new FormData();
/*  427 */     fd.top = new FormAttachment(this.stormNameField, 15, 1024);
/*  428 */     fd.left = new FormAttachment(this.stormTypes, 0, 16384);
/*  429 */     validTimeLabel.setLayoutData(fd);
/*      */ 
/*  431 */     this.validDate = new DateTime(g1, 2080);
/*  432 */     fd = new FormData();
/*  433 */     fd.top = new FormAttachment(this.stormNameField, 10, 1024);
/*  434 */     fd.left = new FormAttachment(validTimeLabel, 10, 131072);
/*  435 */     this.validDate.setLayoutData(fd);
/*      */ 
/*  449 */     this.validTime = new Text(g1, 16779268);
/*  450 */     fd = new FormData();
/*  451 */     fd.top = new FormAttachment(this.stormNameField, 10, 1024);
/*  452 */     fd.left = new FormAttachment(this.validDate, 10, 131072);
/*      */ 
/*  454 */     this.validTime.setLayoutData(fd);
/*  455 */     PgenUtil.setUTCTimeTextField(g1, this.validTime, 
/*  456 */       Calendar.getInstance(TimeZone.getTimeZone("GMT")), 
/*  457 */       this.stormNameField, 15);
/*      */ 
/*  462 */     Label advisoryNumberLabel = new Label(g1, 0);
/*  463 */     advisoryNumberLabel.setText("Advisory#:");
/*  464 */     fd = new FormData();
/*      */ 
/*  466 */     fd.left = new FormAttachment(0, 10);
/*  467 */     fd.top = new FormAttachment(this.stormNumber, 15, 1024);
/*  468 */     advisoryNumberLabel.setLayoutData(fd);
/*      */ 
/*  473 */     this.advisoryNumber = new Text(g1, 2052);
/*  474 */     fd = new FormData();
/*  475 */     fd.left = new FormAttachment(advisoryNumberLabel, 25, 131072);
/*  476 */     fd.top = new FormAttachment(this.stormNumber, 10, 1024);
/*  477 */     fd.bottom = new FormAttachment(100, -10);
/*  478 */     fd.width = 50;
/*  479 */     this.advisoryNumber.setLayoutData(fd);
/*      */ 
/*  484 */     this.advisoryNumber.addVerifyListener(new VerifyListener()
/*      */     {
/*      */       public void verifyText(VerifyEvent e) {
/*  487 */         e.doit = TcaAttrDlg.this.validateAdvisoryNumber(e);
/*  488 */         if (!e.doit)
/*  489 */           Display.getCurrent().beep();
/*      */       }
/*      */     });
/*  496 */     Button upArrow = new Button(g1, 132);
/*  497 */     fd = new FormData();
/*  498 */     fd.left = new FormAttachment(this.advisoryNumber, 0, 131072);
/*  499 */     fd.top = new FormAttachment(this.stormNumber, 10, 1024);
/*  500 */     fd.bottom = new FormAttachment(this.advisoryNumber, 0, 1024);
/*  501 */     upArrow.setLayoutData(fd);
/*  502 */     upArrow.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  513 */         int num = StormAdvisoryNumber.getRegularAdvisory(TcaAttrDlg.this.advisoryNumber
/*  514 */           .getText());
/*  515 */         if (num < 999)
/*  516 */           num++;
/*  517 */         TcaAttrDlg.this.advisoryNumber.setText(Integer.toString(num));
/*  518 */         if (!TcaAttrDlg.this.advisories.isEmpty()) {
/*  519 */           TcaAttrDlg.this.getButton(8612).setEnabled(true);
/*  520 */           TcaAttrDlg.this.getButton(8610).setEnabled(true);
/*  521 */           TcaAttrDlg.this.getButton(8611).setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  531 */     Button downArrow = new Button(g1, 1028);
/*  532 */     fd = new FormData();
/*  533 */     fd.left = new FormAttachment(upArrow, 0, 131072);
/*  534 */     fd.top = new FormAttachment(this.stormNumber, 10, 1024);
/*  535 */     fd.bottom = new FormAttachment(this.advisoryNumber, 0, 1024);
/*  536 */     fd.right = new FormAttachment(this.statusItems, 0, 131072);
/*  537 */     downArrow.setLayoutData(fd);
/*  538 */     downArrow.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  549 */         int num = StormAdvisoryNumber.getRegularAdvisory(TcaAttrDlg.this.advisoryNumber
/*  550 */           .getText());
/*  551 */         if (num > 1)
/*      */         {
/*  553 */           if (!StormAdvisoryNumber.isIntermediate(TcaAttrDlg.this.advisoryNumber
/*  553 */             .getText()))
/*  554 */             num--; 
/*      */         }
/*  555 */         TcaAttrDlg.this.advisoryNumber.setText(Integer.toString(num));
/*      */       }
/*      */     });
/*  560 */     ((FormData)upArrow.getLayoutData()).width = 20;
/*  561 */     ((FormData)downArrow.getLayoutData()).width = 20;
/*  562 */     this.advisoryNumber.setText("1");
/*      */ 
/*  567 */     Label timeZoneLabel = new Label(g1, 0);
/*  568 */     timeZoneLabel.setText("Time Zone for TCV product:");
/*  569 */     fd = new FormData();
/*  570 */     fd.top = new FormAttachment(this.validTime, 15, 1024);
/*  571 */     fd.left = new FormAttachment(this.stormTypes, 0, 16384);
/*  572 */     timeZoneLabel.setLayoutData(fd);
/*      */ 
/*  577 */     this.timeZoneTypes = new Combo(g1, 12);
/*  578 */     for (String st : info.getTimezones()) {
/*  579 */       this.timeZoneTypes.add(st);
/*      */     }
/*  581 */     this.timeZoneTypes.setText(this.timeZoneTypes.getItem(0));
/*  582 */     fd = new FormData();
/*  583 */     fd.top = new FormAttachment(this.validTime, 10, 1024);
/*  584 */     fd.left = new FormAttachment(timeZoneLabel, 10, 131072);
/*  585 */     fd.right = new FormAttachment(this.stormTypes, 0, 131072);
/*  586 */     this.timeZoneTypes.setLayoutData(fd);
/*      */   }
/*      */ 
/*      */   private void createBreakpointTools(Group g2)
/*      */   {
/*  595 */     FormLayout layout = new FormLayout();
/*  596 */     layout.marginHeight = 3;
/*  597 */     layout.marginWidth = 3;
/*  598 */     g2.setLayout(layout);
/*      */ 
/*  603 */     Composite bkptInfo = new Composite(g2, 0);
/*  604 */     FormData fd = new FormData();
/*  605 */     fd.left = new FormAttachment(0, 10);
/*  606 */     fd.right = new FormAttachment(100, -10);
/*  607 */     fd.top = new FormAttachment(0, 10);
/*      */ 
/*  609 */     bkptInfo.setLayoutData(fd);
/*      */ 
/*  611 */     GridLayout grid = new GridLayout(3, true);
/*  612 */     grid.horizontalSpacing = 30;
/*  613 */     bkptInfo.setLayout(grid);
/*      */ 
/*  618 */     Label severityLabel = new Label(bkptInfo, 0);
/*  619 */     severityLabel.setText("Severity:");
/*      */ 
/*  624 */     Label advisoryLabel = new Label(bkptInfo, 0);
/*  625 */     advisoryLabel.setText("Advisory Type:");
/*      */ 
/*  630 */     Label breakpointTypeLabel = new Label(bkptInfo, 0);
/*  631 */     breakpointTypeLabel.setText("Breakpoint Type:");
/*      */ 
/*  636 */     this.severityTypes = new Combo(bkptInfo, 12);
/*  637 */     for (String st : info.getSeverityList()) {
/*  638 */       this.severityTypes.add(st);
/*      */     }
/*  640 */     this.severityTypes.setText(this.severityTypes.getItem(0));
/*  641 */     GridData gd = new GridData(768);
/*      */ 
/*  643 */     this.severityTypes.setLayoutData(gd);
/*      */ 
/*  648 */     this.advisoryTypes = new Combo(bkptInfo, 12);
/*  649 */     for (String st : info.getAdvisoryList()) {
/*  650 */       this.advisoryTypes.add(st);
/*      */     }
/*  652 */     this.advisoryTypes.setLayoutData(new GridData(768));
/*      */ 
/*  654 */     this.advisoryTypes.setText(this.advisoryTypes.getItem(0));
/*      */ 
/*  659 */     this.breakpointTypes = new Combo(bkptInfo, 12);
/*  660 */     for (String st : info.getBreakpointTypeList()) {
/*  661 */       this.breakpointTypes.add(st);
/*      */     }
/*  663 */     this.breakpointTypes.setLayoutData(new GridData(
/*  664 */       768));
/*  665 */     this.breakpointTypes.setText(this.breakpointTypes.getItem(0));
/*      */ 
/*  670 */     Label bkpt1Label = new Label(bkptInfo, 0);
/*  671 */     bkpt1Label.setText("Break Point 1:");
/*      */ 
/*  676 */     Label bkpt2Label = new Label(bkptInfo, 0);
/*  677 */     bkpt2Label.setText("Break Point 2:");
/*      */ 
/*  682 */     Label specialGeogLabel = new Label(bkptInfo, 0);
/*  683 */     specialGeogLabel.setText("Special Geography:");
/*      */ 
/*  688 */     this.bkpt1Field = new Text(bkptInfo, 2052);
/*  689 */     this.bkpt1Field.setLayoutData(new GridData(768));
/*      */ 
/*  695 */     this.bkpt2Field = new Text(bkptInfo, 2052);
/*  696 */     this.bkpt2Field.setLayoutData(new GridData(768));
/*      */ 
/*  702 */     this.specialGeogTypes = new Combo(bkptInfo, 12);
/*  703 */     for (String st : info.getGeographyTypeList()) {
/*  704 */       this.specialGeogTypes.add(st);
/*      */     }
/*  706 */     this.specialGeogTypes.setText(this.specialGeogTypes.getItem(0));
/*  707 */     this.specialGeogTypes.setLayoutData(new GridData(
/*  708 */       768));
/*      */ 
/*  714 */     Composite forButtons = new Composite(g2, 0);
/*  715 */     fd = new FormData();
/*  716 */     fd.left = new FormAttachment(0, 10);
/*  717 */     fd.right = new FormAttachment(100, -10);
/*  718 */     fd.top = new FormAttachment(bkptInfo, 15, 1024);
/*  719 */     fd.bottom = new FormAttachment(100, -10);
/*  720 */     forButtons.setLayoutData(fd);
/*      */ 
/*  722 */     RowLayout row = new RowLayout(256);
/*  723 */     row.pack = false;
/*  724 */     row.justify = true;
/*  725 */     forButtons.setLayout(row);
/*      */ 
/*  730 */     this.apply = new Button(forButtons, 8);
/*  731 */     this.apply.setText("Apply");
/*  732 */     this.apply.setEnabled(false);
/*  733 */     this.apply.addSelectionListener(this);
/*      */ 
/*  738 */     this.deleteSegment = new Button(forButtons, 8);
/*  739 */     this.deleteSegment.setText("Delete Segment");
/*  740 */     this.deleteSegment.setEnabled(false);
/*  741 */     this.deleteSegment.addSelectionListener(this);
/*      */ 
/*  746 */     Button newSegment = new Button(forButtons, 8);
/*  747 */     newSegment.setText("New Segment");
/*  748 */     newSegment.addSelectionListener(this);
/*      */   }
/*      */ 
/*      */   private void createBreakpointList(Group g3)
/*      */   {
/*  758 */     FillLayout layout = new FillLayout();
/*  759 */     layout.marginHeight = 3;
/*  760 */     layout.marginWidth = 3;
/*  761 */     g3.setLayout(layout);
/*      */ 
/*  766 */     this.breakpointList = new org.eclipse.swt.widgets.List(g3, 2820);
/*      */ 
/*  768 */     GridData gd = (GridData)g3.getLayoutData();
/*  769 */     gd.heightHint = (6 * this.breakpointList.getItemHeight());
/*      */ 
/*  771 */     this.breakpointList.addSelectionListener(new SelectionListener()
/*      */     {
/*  773 */       private int lastSelected = -1;
/*      */ 
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  787 */         if (TcaAttrDlg.this.breakpointList.getSelectionIndex() == this.lastSelected) {
/*  788 */           TcaAttrDlg.this.breakpointList.deselect(TcaAttrDlg.this.breakpointList.getSelectionIndex());
/*  789 */           this.lastSelected = -1;
/*  790 */           TcaAttrDlg.this.resetAdvisoryInfo();
/*      */ 
/*  792 */           TcaAttrDlg.this.tcaTool.deselectAdvisory();
/*      */ 
/*  794 */           return;
/*      */         }
/*      */ 
/*  801 */         this.lastSelected = TcaAttrDlg.this.breakpointList.getSelectionIndex();
/*  802 */         TcaAttrDlg.this.updateAdvisoryInfo(this.lastSelected);
/*  803 */         TcaAttrDlg.this.tcaTool.selectAdvisory(this.lastSelected);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   protected void resetAdvisoryInfo()
/*      */   {
/*  817 */     this.apply.setEnabled(false);
/*  818 */     this.deleteSegment.setEnabled(false);
/*      */ 
/*  821 */     this.severityTypes.setText("Tropical Storm");
/*  822 */     this.advisoryTypes.setText("Watch");
/*  823 */     this.breakpointTypes.setText("Official");
/*  824 */     this.bkpt1Field.setText("");
/*  825 */     this.bkpt2Field.setText("");
/*  826 */     this.specialGeogTypes.setText("None");
/*  827 */     this.specialGeogTypes.setEnabled(true);
/*      */   }
/*      */ 
/*      */   protected void updateAdvisoryInfo(int index)
/*      */   {
/*  837 */     this.apply.setEnabled(true);
/*  838 */     this.deleteSegment.setEnabled(true);
/*      */ 
/*  841 */     TropicalCycloneAdvisory tca = (TropicalCycloneAdvisory)this.advisories.get(index);
/*  842 */     this.severityTypes.setText(tca.getSeverity());
/*  843 */     this.advisoryTypes.setText(tca.getAdvisoryType());
/*  844 */     this.specialGeogTypes.setText(tca.getGeographyType());
/*  845 */     this.specialGeogTypes.setEnabled(false);
/*      */ 
/*  847 */     if ((tca.getSegment() instanceof BreakpointPair)) {
/*  848 */       this.bkpt1Field.setText(((Breakpoint)tca.getSegment().getBreakpoints().get(0))
/*  849 */         .getName());
/*  850 */       this.bkpt2Field.setText(((Breakpoint)tca.getSegment().getBreakpoints().get(1))
/*  851 */         .getName());
/*      */     } else {
/*  853 */       this.bkpt1Field.setText("");
/*  854 */       this.bkpt2Field.setText("");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void selectAdvisory(int index)
/*      */   {
/*  865 */     this.breakpointList.select(index);
/*  866 */     updateAdvisoryInfo(index);
/*      */   }
/*      */ 
/*      */   public void deselectAdvisory()
/*      */   {
/*  874 */     this.breakpointList.deselectAll();
/*  875 */     resetAdvisoryInfo();
/*      */   }
/*      */ 
/*      */   protected void buttonPressed(int buttonId)
/*      */   {
/*  890 */     if (buttonId == 8610)
/*      */     {
/*  893 */       if (this.stormNameField.getText().isEmpty()) {
/*  894 */         String msg = "Please provide the storm name.";
/*  895 */         MessageDialog messageDlg = new MessageDialog(
/*  896 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  897 */           "Warning", null, msg, 1, 
/*  898 */           new String[] { "OK" }, 0);
/*  899 */         messageDlg.open();
/*  900 */       } else if (!PgenUtil.isTimeValid(this.validTime.getText())) {
/*  901 */         StringBuilder msg = new StringBuilder("The Product Time ");
/*  902 */         msg.append('"');
/*  903 */         msg.append(this.validTime.getText());
/*  904 */         msg.append(" UTC");
/*  905 */         msg.append('"');
/*  906 */         msg.append(" is invalid.");
/*  907 */         MessageDialog messageDlg = new MessageDialog(
/*  908 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  909 */           "Warning", null, msg.toString(), 1, 
/*  910 */           new String[] { "OK" }, 0);
/*  911 */         messageDlg.open();
/*      */       } else {
/*  913 */         this.dataURI = this.tcaTool.saveAdvisory();
/*  914 */         if (this.dataURI != null) {
/*  915 */           getButton(8610).setEnabled(false);
/*  916 */           getButton(8611).setEnabled(true);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*  924 */     else if (buttonId == 8611)
/*      */     {
/*  926 */       TcaTextMessageDlg textMessage = new TcaTextMessageDlg(
/*  927 */         getShell());
/*      */ 
/*  929 */       textMessage.setOutputFilename(generateVTECFilename());
/*  930 */       textMessage.setDataURI(this.dataURI);
/*      */ 
/*  932 */       String tcvMessage = this.tcaTool.createTCV();
/*  933 */       textMessage.setMessage(tcvMessage);
/*      */ 
/*  935 */       textMessage.open();
/*      */ 
/*  937 */       if (textMessage.getReturnCode() == 0) {
/*  938 */         getButton(8611).setEnabled(false);
/*      */       }
/*      */ 
/*      */     }
/*  946 */     else if (buttonId == 8612)
/*      */     {
/*  949 */       String msg = "Cancel all current watches and warnings\nAnd create TCV message?";
/*  950 */       MessageDialog confirmDlg = new MessageDialog(
/*  951 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  952 */         "Confirm", null, msg, 3, new String[] { 
/*  953 */         "OK", "Cancel" }, 0);
/*  954 */       confirmDlg.open();
/*      */ 
/*  956 */       if (confirmDlg.getReturnCode() == 0) {
/*  957 */         if (!this.advisories.isEmpty())
/*  958 */           setTextLocation(
/*  959 */             ((Breakpoint)((TropicalCycloneAdvisory)this.advisories.get(0)).getSegment()
/*  959 */             .getBreakpoints().get(0)).getLocation());
/*  960 */         this.breakpointList.removeAll();
/*  961 */         this.advisories.clear();
/*  962 */         getButton(8610).setEnabled(false);
/*  963 */         getButton(8611).setEnabled(false);
/*  964 */         this.tcaTool.advisoryDeleted();
/*      */ 
/*  966 */         TcaTextMessageDlg textMessage = new TcaTextMessageDlg(
/*  967 */           getShell());
/*  968 */         textMessage.setOutputFilename(generateVTECFilename());
/*  969 */         String tcvMessage = this.tcaTool.createTCV();
/*  970 */         textMessage.setMessage(tcvMessage);
/*  971 */         textMessage.open();
/*      */       }
/*      */ 
/*      */     }
/*  978 */     else if (buttonId == 12)
/*      */     {
/*  980 */       PgenUtil.setSelectingMode();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void enableButtons()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute attr)
/*      */   {
/*  998 */     if ((attr instanceof SelectionListener))
/*      */     {
/* 1000 */       ITca tca = (SelectionListener)attr;
/* 1001 */       setIssuingStatus(tca.getIssuingStatus());
/* 1002 */       setStormType(((SelectionListener)attr).getStormType());
/* 1003 */       setBasin(tca.getBasin());
/* 1004 */       setStormName(tca.getStormName());
/* 1005 */       setStormNumber(tca.getStormNumber());
/* 1006 */       setAdvisoryNumber(tca.getAdvisoryNumber());
/* 1007 */       setAdvisoryTime(tca.getAdvisoryTime());
/* 1008 */       setTimeZone(tca.getTimeZone());
/* 1009 */       setTextLocation(tca.getTextLocation());
/*      */ 
/* 1011 */       this.advisories.clear();
/* 1012 */       this.breakpointList.removeAll();
/* 1013 */       for (TropicalCycloneAdvisory adv : tca.getAdvisories())
/* 1014 */         addAdvisory(adv.copy());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void readOptions()
/*      */   {
/* 1025 */     File tcainfoFile = PgenStaticDataProvider.getProvider().getStaticFile(
/* 1026 */       PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + 
/* 1027 */       "TCAinfo.xml");
/*      */     try
/*      */     {
/* 1030 */       info = (TcaAttrInfo)SerializationUtil.jaxbUnmarshalFromXmlFile(
/* 1031 */         TcaAttrInfo.class, tcainfoFile.getAbsoluteFile());
/*      */     } catch (Exception e) {
/* 1033 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean validateAdvisoryNumber(VerifyEvent ve)
/*      */   {
/* 1044 */     boolean stat = false;
/*      */ 
/* 1046 */     if ((ve.widget instanceof Text)) {
/* 1047 */       Text advnum = (Text)ve.widget;
/* 1048 */       StringBuffer str = new StringBuffer(advnum.getText());
/* 1049 */       str.replace(ve.start, ve.end, ve.text);
/*      */ 
/* 1051 */       if (str.toString().isEmpty())
/* 1052 */         return true;
/* 1053 */       stat = StormAdvisoryNumber.isValid(str.toString());
/*      */     }
/*      */ 
/* 1056 */     return stat;
/*      */   }
/*      */ 
/*      */   private String generateVTECFilename()
/*      */   {
/* 1064 */     String basin = Basin.getBasinAbbrev(getBasin()).toUpperCase();
/* 1065 */     if (basin.equals("AL"))
/* 1066 */       basin = new String("AT");
/* 1067 */     int num = getStormNumber() % 5;
/* 1068 */     if (num == 0)
/* 1069 */       num = 5;
/* 1070 */     String name = String.format("%sTCV%2s%1d", new Object[] { PgenUtil.getCurrentOffice(), 
/* 1071 */       basin, Integer.valueOf(num) });
/*      */ 
/* 1074 */     return name;
/*      */   }
/*      */ 
/*      */   public ArrayList<TropicalCycloneAdvisory> getAdvisories()
/*      */   {
/* 1084 */     return this.advisories;
/*      */   }
/*      */ 
/*      */   public void addAdvisory(TropicalCycloneAdvisory advisory)
/*      */   {
/* 1091 */     this.advisories.add(advisory);
/* 1092 */     this.breakpointList.add(toListString(advisory));
/* 1093 */     getButton(8610).setEnabled(true);
/*      */   }
/*      */ 
/*      */   public void replaceAdvisory(int index, TropicalCycloneAdvisory advisory)
/*      */   {
/* 1101 */     this.advisories.set(index, advisory);
/* 1102 */     this.breakpointList.setItem(index, toListString(advisory));
/* 1103 */     updateAdvisoryInfo(index);
/*      */   }
/*      */ 
/*      */   private String toListString(TropicalCycloneAdvisory advisory)
/*      */   {
/* 1111 */     StringBuilder sb = new StringBuilder(advisory.getSeverity() + "\t" + 
/* 1112 */       advisory.getAdvisoryType() + "\t");
/* 1113 */     sb.append(((Breakpoint)advisory.getSegment().getBreakpoints().get(0)).getName());
/* 1114 */     if ((advisory.getSegment() instanceof BreakpointPair))
/* 1115 */       sb.append("\t" + 
/* 1116 */         ((Breakpoint)advisory.getSegment().getBreakpoints().get(1)).getName());
/* 1117 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public String getAdvisoryNumber()
/*      */   {
/* 1127 */     return this.advisoryNumber.getText();
/*      */   }
/*      */ 
/*      */   private void setAdvisoryNumber(String adnum)
/*      */   {
/* 1134 */     this.advisoryNumber.setText(adnum);
/*      */   }
/*      */ 
/*      */   public String getStormName()
/*      */   {
/* 1139 */     return this.stormNameField.getText();
/*      */   }
/*      */ 
/*      */   private void setStormName(String name) {
/* 1143 */     this.stormNameField.setText(name);
/*      */   }
/*      */ 
/*      */   public int getStormNumber()
/*      */   {
/* 1148 */     return this.stormNumber.getSelection();
/*      */   }
/*      */ 
/*      */   private void setStormNumber(int num) {
/* 1152 */     this.stormNumber.setSelection(num);
/*      */   }
/*      */ 
/*      */   public String getStormType()
/*      */   {
/* 1157 */     return this.stormTypes.getText();
/*      */   }
/*      */ 
/*      */   private void setStormType(String type) {
/* 1161 */     this.stormTypes.setText(type);
/*      */   }
/*      */ 
/*      */   public Coordinate getTextLocation()
/*      */   {
/* 1166 */     return this.textLocation;
/*      */   }
/*      */ 
/*      */   public void setTextLocation(Coordinate loc)
/*      */   {
/* 1171 */     this.textLocation = loc;
/*      */   }
/*      */ 
/*      */   public String getTimeZone()
/*      */   {
/* 1176 */     return this.timeZoneTypes.getText();
/*      */   }
/*      */ 
/*      */   private void setTimeZone(String zone) {
/* 1180 */     this.timeZoneTypes.setText(zone);
/*      */   }
/*      */ 
/*      */   public Calendar getAdvisoryTime()
/*      */   {
/* 1185 */     int time = Integer.parseInt(this.validTime.getText());
/* 1186 */     int hours = time / 100;
/* 1187 */     int minutes = time % 100;
/*      */ 
/* 1189 */     Calendar advTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 1190 */     advTime.set(this.validDate.getYear(), this.validDate.getMonth(), 
/* 1191 */       this.validDate.getDay(), hours, minutes, 0);
/* 1192 */     advTime.set(14, 0);
/* 1193 */     return advTime;
/*      */   }
/*      */ 
/*      */   private void setAdvisoryTime(Calendar time) {
/* 1197 */     this.validDate.setYear(time.get(1));
/* 1198 */     this.validDate.setMonth(time.get(2));
/* 1199 */     this.validDate.setDay(time.get(5));
/*      */ 
/* 1201 */     this.validTime.setText(String.format("%02d%02d", new Object[] { 
/* 1202 */       Integer.valueOf(time.get(11)), Integer.valueOf(time.get(12)) }));
/*      */   }
/*      */ 
/*      */   public String getIssuingStatus()
/*      */   {
/* 1210 */     return this.statusItems.getText();
/*      */   }
/*      */ 
/*      */   private void setIssuingStatus(String status) {
/* 1214 */     this.statusItems.setText(status);
/*      */   }
/*      */ 
/*      */   public String getBasin()
/*      */   {
/* 1219 */     return this.basinTypes.getText();
/*      */   }
/*      */ 
/*      */   private void setBasin(String basin) {
/* 1223 */     this.basinTypes.setText(basin);
/*      */   }
/*      */ 
/*      */   public void widgetDefaultSelected(SelectionEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void widgetSelected(SelectionEvent e)
/*      */   {
/* 1241 */     if ((e.widget instanceof Button)) {
/* 1242 */       Button b = (Button)e.widget;
/*      */ 
/* 1247 */       if (b.getText().equals("New Segment")) {
/* 1248 */         if (getGeogType().equals("None"))
/* 1249 */           this.tcaTool.setPairMode();
/*      */         else {
/* 1251 */           this.tcaTool.setSingleMode();
/*      */         }
/*      */ 
/*      */       }
/* 1258 */       else if (b.getText().equals("Delete Segment")) {
/* 1259 */         int idx = this.breakpointList.getSelectionIndex();
/* 1260 */         if (idx != -1) {
/* 1261 */           setTextLocation(
/* 1262 */             ((Breakpoint)((TropicalCycloneAdvisory)this.advisories.get(idx)).getSegment()
/* 1262 */             .getBreakpoints().get(0)).getLocation());
/* 1263 */           this.breakpointList.remove(idx);
/* 1264 */           this.advisories.remove(idx);
/* 1265 */           if (this.advisories.isEmpty())
/* 1266 */             getButton(8610).setEnabled(false);
/*      */           else
/* 1268 */             getButton(8610).setEnabled(true);
/*      */         }
/* 1270 */         resetAdvisoryInfo();
/* 1271 */         this.tcaTool.advisoryDeleted();
/*      */       }
/* 1277 */       else if (b.getText().equals("Apply")) {
/* 1278 */         int idx = this.breakpointList.getSelectionIndex();
/* 1279 */         TropicalCycloneAdvisory tca = (TropicalCycloneAdvisory)this.advisories.get(idx);
/* 1280 */         tca.setSeverity(this.severityTypes.getText());
/* 1281 */         tca.setAdvisoryType(this.advisoryTypes.getText());
/*      */ 
/* 1283 */         this.breakpointList.setItem(idx, toListString(tca));
/* 1284 */         if (this.advisories.isEmpty())
/* 1285 */           getButton(8610).setEnabled(false);
/*      */         else
/* 1287 */           getButton(8610).setEnabled(true);
/* 1288 */         this.tcaTool.updateTcaElement();
/* 1289 */         this.tcaTool.selectAdvisory(idx);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getGeogType()
/*      */   {
/* 1297 */     return this.specialGeogTypes.getText();
/*      */   }
/*      */ 
/*      */   public String getSeverity() {
/* 1301 */     return this.severityTypes.getText();
/*      */   }
/*      */ 
/*      */   public String getAdvisoryType() {
/* 1305 */     return this.advisoryTypes.getText();
/*      */   }
/*      */ 
/*      */   public String getBreakpointType() {
/* 1309 */     return this.breakpointTypes.getText();
/*      */   }
/*      */ 
/*      */   public void setTcaTool(PgenTcaTool tcaTool)
/*      */   {
/* 1320 */     this.tcaTool = tcaTool;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.TcaAttrDlg
 * JD-Core Version:    0.6.2
 */