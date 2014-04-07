/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.common.serialization.SerializationUtil;
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.ITcm;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.Tcm;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.TcmFcst;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.TcmWindQuarters;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import java.io.File;
/*      */ import java.util.Calendar;
/*      */ import java.util.List;
/*      */ import java.util.TimeZone;
/*      */ import org.eclipse.swt.events.ModifyEvent;
/*      */ import org.eclipse.swt.events.ModifyListener;
/*      */ import org.eclipse.swt.events.SelectionAdapter;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.events.SelectionListener;
/*      */ import org.eclipse.swt.events.VerifyEvent;
/*      */ import org.eclipse.swt.events.VerifyListener;
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
/*      */ 
/*      */ public class TcmAttrDlg extends AttrDlg
/*      */   implements ITcm, SelectionListener
/*      */ {
/*   72 */   static TcmAttrDlg INSTANCE = null;
/*   73 */   private static TcaAttrInfo info = null;
/*      */ 
/*   75 */   private static final String[] fcstHrStr = { "OBS", "06", "12", "24", "36", "48", "72", "96", "120", "144" };
/*      */ 
/*   77 */   private final String APPLY = "Apply";
/*   78 */   private final String DELETE = "Delete";
/*   79 */   private final String ADD = "Add";
/*      */ 
/*   81 */   private Composite top = null;
/*   82 */   private Combo stormTypes = null;
/*   83 */   private Combo basinTypes = null;
/*   84 */   private Spinner advisoryNumber = null;
/*   85 */   private Text stormNameField = null;
/*   86 */   private Spinner stormNumber = null;
/*      */ 
/*   88 */   private Combo fcstHrs = null;
/*   89 */   private Text validTime = null;
/*   90 */   private DateTime validDate = null;
/*      */   private Button apply;
/*      */   private Button deleteFcst;
/*      */   private Button addFcst;
/*      */   private Text gustField;
/*      */   private Text windMaxField;
/*      */   private Text spdField;
/*      */   private Text dirField;
/*      */   private Text pressureField;
/*      */   private Text ne12ftField;
/*      */   private Text nw12ftField;
/*      */   private Text sw12ftField;
/*      */   private Text se12ftField;
/*      */   private Text latField;
/*      */   private Text lonField;
/*      */   private Text ne34Field;
/*      */   private Text nw34Field;
/*      */   private Text se34Field;
/*      */   private Text sw34Field;
/*      */   private Text ne50Field;
/*      */   private Text nw50Field;
/*      */   private Text se50Field;
/*      */   private Text sw50Field;
/*      */   private Text ne64Field;
/*      */   private Text nw64Field;
/*      */   private Text se64Field;
/*      */   private Text sw64Field;
/*      */   private Text eyeSizeField;
/*      */   private Text accuracyField;
/*      */   private Button corr;
/*  113 */   private Tcm tcm = null;
/*  114 */   private TcmWindQuarters waves = null;
/*      */ 
/*      */   private TcmAttrDlg(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  123 */     super(parShell);
/*      */   }
/*      */ 
/*      */   public static TcmAttrDlg getInstance(Shell parShell)
/*      */   {
/*  136 */     if (INSTANCE == null)
/*      */     {
/*      */       try
/*      */       {
/*  140 */         INSTANCE = new TcmAttrDlg(parShell);
/*      */       }
/*      */       catch (VizException e)
/*      */       {
/*  144 */         e.printStackTrace();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  149 */     readOptions();
/*  150 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   private static void readOptions()
/*      */   {
/*  159 */     File tcainfoFile = PgenStaticDataProvider.getProvider().getStaticFile(
/*  160 */       PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "TCAinfo.xml");
/*      */     try
/*      */     {
/*  164 */       info = (TcaAttrInfo)SerializationUtil.jaxbUnmarshalFromXmlFile(tcainfoFile.getAbsoluteFile());
/*      */     }
/*      */     catch (Exception e) {
/*  167 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int open()
/*      */   {
/*  178 */     int ret = super.open();
/*  179 */     return ret;
/*      */   }
/*      */ 
/*      */   public void createButtonsForButtonBar(Composite parent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  196 */     this.top = ((Composite)super.createDialogArea(parent));
/*  197 */     getShell().setText("TCM Attributes");
/*      */ 
/*  200 */     GridLayout mainLayout = new GridLayout(1, true);
/*  201 */     mainLayout.marginHeight = 3;
/*  202 */     mainLayout.marginWidth = 3;
/*  203 */     this.top.setLayout(mainLayout);
/*      */ 
/*  208 */     Group g1 = new Group(this.top, 16);
/*  209 */     g1.setLayoutData(new GridData(768));
/*  210 */     createStormInfoArea(g1);
/*      */ 
/*  216 */     Group g2 = new Group(this.top, 16);
/*  217 */     g2.setLayoutData(new GridData(768));
/*  218 */     createTcmFcst(g2);
/*      */ 
/*  221 */     return this.top;
/*      */   }
/*      */ 
/*      */   private void createStormInfoArea(Group g1)
/*      */   {
/*  230 */     FormLayout layout = new FormLayout();
/*  231 */     layout.marginHeight = 3;
/*  232 */     layout.marginWidth = 3;
/*  233 */     g1.setLayout(layout);
/*      */ 
/*  238 */     Label typeLabel = new Label(g1, 0);
/*  239 */     typeLabel.setText("Storm Type:");
/*  240 */     FormData fd = new FormData();
/*  241 */     fd.top = new FormAttachment(0, 10);
/*  242 */     fd.top = new FormAttachment(0, 10);
/*      */ 
/*  246 */     typeLabel.setLayoutData(fd);
/*      */ 
/*  251 */     this.stormTypes = new Combo(g1, 12);
/*  252 */     for (String st : info.getTypeList()) {
/*  253 */       this.stormTypes.add(st);
/*      */     }
/*  255 */     this.stormTypes.setText(this.stormTypes.getItem(0));
/*  256 */     fd = new FormData();
/*  257 */     fd.left = new FormAttachment(0, 10);
/*  258 */     fd.top = new FormAttachment(typeLabel, 5, 1024);
/*  259 */     this.stormTypes.setLayoutData(fd);
/*      */ 
/*  262 */     fd = (FormData)typeLabel.getLayoutData();
/*  263 */     fd.left = new FormAttachment(this.stormTypes, 0, 16384);
/*      */ 
/*  268 */     Label basinLabel = new Label(g1, 0);
/*  269 */     basinLabel.setText("Basin:");
/*  270 */     fd = new FormData();
/*  271 */     fd.top = new FormAttachment(0, 10);
/*  272 */     fd.left = new FormAttachment(typeLabel, 150, 131072);
/*      */ 
/*  274 */     basinLabel.setLayoutData(fd);
/*      */ 
/*  279 */     this.basinTypes = new Combo(g1, 12);
/*  280 */     for (String st : info.getBasinList()) {
/*  281 */       this.basinTypes.add(st);
/*      */     }
/*  283 */     this.basinTypes.setText(this.basinTypes.getItem(0));
/*  284 */     fd = new FormData();
/*  285 */     fd.left = new FormAttachment(this.stormTypes, 30, 131072);
/*      */ 
/*  287 */     fd.top = new FormAttachment(basinLabel, 5, 1024);
/*      */ 
/*  289 */     this.basinTypes.setLayoutData(fd);
/*      */ 
/*  294 */     Label stormNameLabel = new Label(g1, 0);
/*  295 */     stormNameLabel.setText("Name:");
/*  296 */     fd = new FormData();
/*  297 */     fd.top = new FormAttachment(this.stormTypes, 15, 1024);
/*  298 */     fd.left = new FormAttachment(this.stormTypes, 0, 16384);
/*  299 */     stormNameLabel.setLayoutData(fd);
/*      */ 
/*  304 */     this.stormNameField = new Text(g1, 2052);
/*  305 */     fd = new FormData();
/*  306 */     fd.top = new FormAttachment(this.stormTypes, 10, 1024);
/*  307 */     fd.left = new FormAttachment(stormNameLabel, 10, 131072);
/*  308 */     fd.right = new FormAttachment(this.stormTypes, 0, 131072);
/*  309 */     this.stormNameField.setLayoutData(fd);
/*      */ 
/*  314 */     Label stormNumberLabel = new Label(g1, 0);
/*  315 */     stormNumberLabel.setText("Storm#:");
/*  316 */     fd = new FormData();
/*      */ 
/*  318 */     fd.left = new FormAttachment(this.basinTypes, 0, 16384);
/*  319 */     fd.top = new FormAttachment(this.stormNameField, 8, 128);
/*  320 */     stormNumberLabel.setLayoutData(fd);
/*      */ 
/*  325 */     this.stormNumber = new Spinner(g1, 2048);
/*  326 */     fd = new FormData();
/*  327 */     fd.left = new FormAttachment(stormNumberLabel, 32, 131072);
/*  328 */     fd.top = new FormAttachment(stormNumberLabel, -5, 128);
/*  329 */     this.stormNumber.setLayoutData(fd);
/*  330 */     this.stormNumber.setMinimum(1);
/*      */ 
/*  335 */     Label advisoryNumberLabel = new Label(g1, 0);
/*  336 */     advisoryNumberLabel.setText("Advisory#:");
/*  337 */     fd = new FormData();
/*  338 */     fd.left = new FormAttachment(stormNumberLabel, 0, 16384);
/*  339 */     fd.top = new FormAttachment(stormNumberLabel, 55, 1024);
/*  340 */     advisoryNumberLabel.setLayoutData(fd);
/*      */ 
/*  345 */     this.advisoryNumber = new Spinner(g1, 2048);
/*  346 */     fd = new FormData();
/*  347 */     fd.left = new FormAttachment(advisoryNumberLabel, 10, 131072);
/*  348 */     fd.top = new FormAttachment(advisoryNumberLabel, -5, 128);
/*  349 */     this.advisoryNumber.setLayoutData(fd);
/*  350 */     this.advisoryNumber.setMinimum(1);
/*      */ 
/*  352 */     this.corr = new Button(g1, 32);
/*  353 */     this.corr.setText("Correction");
/*  354 */     fd = new FormData();
/*  355 */     fd.left = new FormAttachment(stormNumberLabel, 0, 16384);
/*  356 */     fd.top = new FormAttachment(advisoryNumberLabel, 20, 1024);
/*  357 */     this.corr.setLayoutData(fd);
/*      */ 
/*  362 */     Label eyeSizeLabel = new Label(g1, 0);
/*  363 */     eyeSizeLabel.setText("EyeSize:");
/*  364 */     fd = new FormData();
/*  365 */     fd.top = new FormAttachment(stormNumberLabel, 53, 1024);
/*  366 */     fd.left = new FormAttachment(0, 10);
/*  367 */     eyeSizeLabel.setLayoutData(fd);
/*      */ 
/*  372 */     this.eyeSizeField = new Text(g1, 2052);
/*  373 */     fd = new FormData();
/*  374 */     fd.top = new FormAttachment(eyeSizeLabel, -3, 128);
/*  375 */     fd.left = new FormAttachment(eyeSizeLabel, 82, 131072);
/*  376 */     fd.right = new FormAttachment(this.stormTypes, 0, 131072);
/*  377 */     this.eyeSizeField.setLayoutData(fd);
/*      */ 
/*  382 */     Label pressureLabel = new Label(g1, 0);
/*  383 */     pressureLabel.setText("CentralPressure:");
/*  384 */     fd = new FormData();
/*  385 */     fd.top = new FormAttachment(eyeSizeLabel, 15, 1024);
/*  386 */     fd.left = new FormAttachment(0, 10);
/*  387 */     pressureLabel.setLayoutData(fd);
/*      */ 
/*  392 */     this.pressureField = new Text(g1, 2052);
/*  393 */     fd = new FormData();
/*  394 */     fd.top = new FormAttachment(pressureLabel, -3, 128);
/*  395 */     fd.left = new FormAttachment(this.eyeSizeField, 0, 16384);
/*  396 */     fd.right = new FormAttachment(this.stormTypes, 0, 131072);
/*  397 */     this.pressureField.setLayoutData(fd);
/*      */ 
/*  402 */     Label accuracyLabel = new Label(g1, 0);
/*  403 */     accuracyLabel.setText("PositionAccuracy:");
/*  404 */     fd = new FormData();
/*  405 */     fd.top = new FormAttachment(pressureLabel, 15, 1024);
/*  406 */     fd.left = new FormAttachment(0, 10);
/*  407 */     accuracyLabel.setLayoutData(fd);
/*      */ 
/*  412 */     this.accuracyField = new Text(g1, 2052);
/*  413 */     fd = new FormData();
/*  414 */     fd.top = new FormAttachment(accuracyLabel, -3, 128);
/*  415 */     fd.left = new FormAttachment(accuracyLabel, 15, 131072);
/*  416 */     fd.right = new FormAttachment(this.stormTypes, 0, 131072);
/*  417 */     this.accuracyField.setLayoutData(fd);
/*      */ 
/*  423 */     Label validTimeLabel = new Label(g1, 0);
/*  424 */     validTimeLabel.setText("Valid Time:");
/*  425 */     fd = new FormData();
/*  426 */     fd.top = new FormAttachment(stormNameLabel, 18, 1024);
/*  427 */     fd.left = new FormAttachment(0, 10);
/*  428 */     validTimeLabel.setLayoutData(fd);
/*      */ 
/*  430 */     this.validDate = new DateTime(g1, 2080);
/*  431 */     fd = new FormData();
/*  432 */     fd.top = new FormAttachment(validTimeLabel, -3, 128);
/*  433 */     fd.left = new FormAttachment(validTimeLabel, 10, 131072);
/*  434 */     this.validDate.setLayoutData(fd);
/*      */ 
/*  438 */     this.validTime = new Text(g1, 16779268);
/*  439 */     this.validTime.setTextLimit(4);
/*  440 */     fd = new FormData();
/*  441 */     fd.top = new FormAttachment(this.validDate, -1, 128);
/*  442 */     fd.left = new FormAttachment(this.validDate, 10, 131072);
/*      */ 
/*  444 */     this.validTime.setLayoutData(fd);
/*  445 */     this.validTime.setText(getInitialTime());
/*  446 */     this.validTime.addVerifyListener(new VerifyListener()
/*      */     {
/*      */       public void verifyText(VerifyEvent ve)
/*      */       {
/*  450 */         char BACKSPACE = '\b';
/*  451 */         char DELETE = '';
/*      */ 
/*  453 */         if ((Character.isDigit(ve.character)) || 
/*  454 */           (ve.character == '\b') || (ve.character == '')) ve.doit = true;
/*      */         else
/*  456 */           ve.doit = false;
/*      */       }
/*      */     });
/*  461 */     this.validTime.addModifyListener(new ModifyListener()
/*      */     {
/*      */       public void modifyText(ModifyEvent e)
/*      */       {
/*  465 */         if (TcmAttrDlg.this.isTimeValid(TcmAttrDlg.this.validTime.getText()))
/*  466 */           TcmAttrDlg.this.validTime.setBackground(Display.getCurrent().getSystemColor(1));
/*      */         else
/*  468 */           TcmAttrDlg.this.validTime.setBackground(Display.getCurrent().getSystemColor(3));
/*      */       }
/*      */     });
/*  473 */     Label utcLabel = new Label(g1, 0);
/*  474 */     utcLabel.setText("UTC");
/*  475 */     fd = new FormData();
/*  476 */     fd.top = new FormAttachment(this.validTime, 5, 128);
/*  477 */     fd.left = new FormAttachment(this.validTime, 5, 131072);
/*  478 */     utcLabel.setLayoutData(fd);
/*      */ 
/*  483 */     Label ne12ftLabel = new Label(g1, 0);
/*  484 */     ne12ftLabel.setText("NE12FT:");
/*  485 */     fd = new FormData();
/*  486 */     fd.top = new FormAttachment(accuracyLabel, 18, 1024);
/*  487 */     fd.left = new FormAttachment(0, 10);
/*  488 */     ne12ftLabel.setLayoutData(fd);
/*      */ 
/*  493 */     this.ne12ftField = new Text(g1, 2052);
/*  494 */     fd = new FormData();
/*  495 */     fd.top = new FormAttachment(ne12ftLabel, 5, 1024);
/*  496 */     fd.left = new FormAttachment(0, 10);
/*  497 */     this.ne12ftField.setLayoutData(fd);
/*      */ 
/*  503 */     Label se12ftLabel = new Label(g1, 0);
/*  504 */     se12ftLabel.setText("SE12FT:");
/*  505 */     fd = new FormData();
/*  506 */     fd.top = new FormAttachment(ne12ftLabel, 0, 128);
/*  507 */     fd.left = new FormAttachment(ne12ftLabel, 50, 131072);
/*  508 */     se12ftLabel.setLayoutData(fd);
/*      */ 
/*  513 */     this.se12ftField = new Text(g1, 2052);
/*  514 */     fd = new FormData();
/*  515 */     fd.top = new FormAttachment(se12ftLabel, 5, 1024);
/*  516 */     fd.left = new FormAttachment(se12ftLabel, 0, 16384);
/*  517 */     this.se12ftField.setLayoutData(fd);
/*      */ 
/*  523 */     Label sw12ftLabel = new Label(g1, 0);
/*  524 */     sw12ftLabel.setText("SW12FT:");
/*  525 */     fd = new FormData();
/*  526 */     fd.top = new FormAttachment(se12ftLabel, 0, 128);
/*  527 */     fd.left = new FormAttachment(se12ftLabel, 50, 131072);
/*  528 */     sw12ftLabel.setLayoutData(fd);
/*      */ 
/*  533 */     this.sw12ftField = new Text(g1, 2052);
/*  534 */     fd = new FormData();
/*  535 */     fd.top = new FormAttachment(sw12ftLabel, 5, 1024);
/*  536 */     fd.left = new FormAttachment(sw12ftLabel, 0, 16384);
/*  537 */     this.sw12ftField.setLayoutData(fd);
/*      */ 
/*  542 */     Label nw12ftLabel = new Label(g1, 0);
/*  543 */     nw12ftLabel.setText("NW12FT:");
/*  544 */     fd = new FormData();
/*  545 */     fd.top = new FormAttachment(sw12ftLabel, 0, 128);
/*  546 */     fd.left = new FormAttachment(sw12ftLabel, 50, 131072);
/*  547 */     nw12ftLabel.setLayoutData(fd);
/*      */ 
/*  552 */     this.nw12ftField = new Text(g1, 2052);
/*  553 */     fd = new FormData();
/*  554 */     fd.top = new FormAttachment(nw12ftLabel, 5, 1024);
/*  555 */     fd.left = new FormAttachment(nw12ftLabel, 0, 16384);
/*  556 */     this.nw12ftField.setLayoutData(fd);
/*      */   }
/*      */ 
/*      */   private String getInitialTime()
/*      */   {
/*  563 */     Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*  564 */     int minute = now.get(12);
/*  565 */     if (minute >= 15) now.add(11, 1);
/*  566 */     int hour = now.get(11);
/*      */ 
/*  568 */     return String.format("%02d00", new Object[] { Integer.valueOf(hour) });
/*      */   }
/*      */ 
/*      */   private void createTcmFcst(Group g2)
/*      */   {
/*  576 */     FormLayout layout = new FormLayout();
/*  577 */     layout.marginHeight = 3;
/*  578 */     layout.marginWidth = 3;
/*  579 */     g2.setLayout(layout);
/*      */ 
/*  584 */     Composite fcstInfo = new Composite(g2, 0);
/*  585 */     FormData fd = new FormData();
/*  586 */     fd.left = new FormAttachment(0, 10);
/*  587 */     fd.right = new FormAttachment(100, -10);
/*  588 */     fd.top = new FormAttachment(0, 10);
/*      */ 
/*  590 */     fcstInfo.setLayoutData(fd);
/*      */ 
/*  592 */     RowLayout rl = new RowLayout();
/*  593 */     rl.type = 512;
/*  594 */     fcstInfo.setLayout(rl);
/*      */ 
/*  596 */     Composite comp1 = new Composite(fcstInfo, 0);
/*  597 */     comp1.setLayout(new FormLayout());
/*      */ 
/*  601 */     Label fcstHrLabel = new Label(comp1, 0);
/*  602 */     fcstHrLabel.setText("Forecast Hour:");
/*  603 */     fd = new FormData();
/*  604 */     fd.top = new FormAttachment(0, 5);
/*  605 */     fd.left = new FormAttachment(0, 5);
/*  606 */     fcstHrLabel.setLayoutData(fd);
/*      */ 
/*  611 */     this.fcstHrs = new Combo(comp1, 12);
/*  612 */     for (String st : fcstHrStr) {
/*  613 */       this.fcstHrs.add(st);
/*      */     }
/*      */ 
/*  616 */     this.fcstHrs.setText(this.fcstHrs.getItem(0));
/*  617 */     fd = new FormData();
/*  618 */     fd.top = new FormAttachment(0, 0);
/*  619 */     fd.left = new FormAttachment(fcstHrLabel, 18, 131072);
/*  620 */     this.fcstHrs.setLayoutData(fd);
/*      */ 
/*  622 */     this.fcstHrs.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  626 */         int hr = 0;
/*      */         try {
/*  628 */           hr = Integer.parseInt(((Combo)e.widget).getText());
/*      */         }
/*      */         catch (Exception ex) {
/*  631 */           hr = 0;
/*      */         }
/*      */ 
/*  634 */         TcmAttrDlg.this.addFcst.setEnabled(true);
/*      */ 
/*  636 */         if (TcmAttrDlg.this.tcm != null)
/*  637 */           for (TcmFcst fcst : TcmAttrDlg.this.tcm.getTcmFcst())
/*  638 */             if (fcst.getFcstHr() == hr) {
/*  639 */               TcmAttrDlg.this.setTcmFcstInfo(fcst);
/*  640 */               TcmAttrDlg.this.addFcst.setEnabled(false);
/*  641 */               break;
/*      */             }
/*      */       }
/*      */     });
/*  654 */     Composite comp2 = new Composite(fcstInfo, 0);
/*  655 */     GridLayout gdl = new GridLayout(4, true);
/*  656 */     comp2.setLayout(gdl);
/*      */ 
/*  658 */     Label latLabel = new Label(comp2, 0);
/*  659 */     latLabel.setText("Lat:");
/*  660 */     this.latField = new Text(comp2, 2052);
/*  661 */     Label lonLabel = new Label(comp2, 0);
/*  662 */     lonLabel.setText("Lon:");
/*  663 */     this.lonField = new Text(comp2, 2052);
/*      */ 
/*  668 */     Label windMaxLabel = new Label(comp2, 0);
/*  669 */     windMaxLabel.setText("WindMax:");
/*  670 */     this.windMaxField = new Text(comp2, 2052);
/*      */ 
/*  675 */     Label gustLabel = new Label(comp2, 0);
/*  676 */     gustLabel.setText("Gust:");
/*  677 */     this.gustField = new Text(comp2, 2052);
/*      */ 
/*  682 */     Label dirLabel = new Label(comp2, 0);
/*  683 */     dirLabel.setText("StormDir:");
/*  684 */     this.dirField = new Text(comp2, 2052);
/*      */ 
/*  689 */     Label spdLabel = new Label(comp2, 0);
/*  690 */     spdLabel.setText("StormSpd:");
/*  691 */     this.spdField = new Text(comp2, 2052);
/*      */ 
/*  694 */     Composite comp3 = new Composite(fcstInfo, 0);
/*  695 */     GridLayout gdl1 = new GridLayout(5, true);
/*  696 */     comp3.setLayout(gdl1);
/*      */ 
/*  698 */     Label spdQuatroLabel = new Label(comp3, 0);
/*  699 */     spdQuatroLabel.setText(" ");
/*      */ 
/*  701 */     Label neLabel = new Label(comp3, 0);
/*  702 */     neLabel.setText("NorthEast");
/*  703 */     Label swLabel = new Label(comp3, 0);
/*  704 */     swLabel.setText("SouthEast");
/*  705 */     Label seLabel = new Label(comp3, 0);
/*  706 */     seLabel.setText("SouthWest");
/*  707 */     Label nwLabel = new Label(comp3, 0);
/*  708 */     nwLabel.setText("NorthWest");
/*      */ 
/*  711 */     Label spd34Label = new Label(comp3, 0);
/*  712 */     spd34Label.setText("34 Knots");
/*      */ 
/*  714 */     this.ne34Field = new Text(comp3, 2052);
/*  715 */     this.se34Field = new Text(comp3, 2052);
/*  716 */     this.sw34Field = new Text(comp3, 2052);
/*  717 */     this.nw34Field = new Text(comp3, 2052);
/*      */ 
/*  719 */     Label spd50Label = new Label(comp3, 0);
/*  720 */     spd50Label.setText("50 Knots");
/*      */ 
/*  722 */     this.ne50Field = new Text(comp3, 2052);
/*  723 */     this.se50Field = new Text(comp3, 2052);
/*  724 */     this.sw50Field = new Text(comp3, 2052);
/*  725 */     this.nw50Field = new Text(comp3, 2052);
/*      */ 
/*  727 */     Label spd64Label = new Label(comp3, 0);
/*  728 */     spd64Label.setText("64 Knots");
/*      */ 
/*  730 */     this.ne64Field = new Text(comp3, 2052);
/*  731 */     this.se64Field = new Text(comp3, 2052);
/*  732 */     this.sw64Field = new Text(comp3, 2052);
/*  733 */     this.nw64Field = new Text(comp3, 2052);
/*      */ 
/*  738 */     Composite forButtons = new Composite(g2, 0);
/*  739 */     fd = new FormData();
/*  740 */     fd.left = new FormAttachment(0, 10);
/*  741 */     fd.right = new FormAttachment(100, -10);
/*  742 */     fd.top = new FormAttachment(fcstInfo, 15, 1024);
/*  743 */     fd.bottom = new FormAttachment(100, -10);
/*  744 */     forButtons.setLayoutData(fd);
/*      */ 
/*  746 */     RowLayout row = new RowLayout(256);
/*  747 */     row.pack = false;
/*  748 */     row.justify = true;
/*  749 */     forButtons.setLayout(row);
/*      */ 
/*  753 */     this.addFcst = new Button(forButtons, 8);
/*  754 */     this.addFcst.setText("Add");
/*  755 */     this.addFcst.addSelectionListener(this);
/*      */ 
/*  760 */     this.apply = new Button(forButtons, 8);
/*  761 */     this.apply.setText("Apply");
/*  762 */     this.apply.setEnabled(true);
/*  763 */     this.apply.addSelectionListener(this);
/*      */ 
/*  768 */     this.deleteFcst = new Button(forButtons, 8);
/*  769 */     this.deleteFcst.setText("Delete");
/*  770 */     this.deleteFcst.setEnabled(false);
/*  771 */     this.deleteFcst.addSelectionListener(this);
/*      */ 
/*  778 */     Button closeSegment = new Button(forButtons, 8);
/*  779 */     closeSegment.setText("Close");
/*  780 */     closeSegment.addSelectionListener(this);
/*      */   }
/*      */ 
/*      */   private boolean isTimeValid(String text)
/*      */   {
/*  785 */     int time = Integer.parseInt(text);
/*  786 */     int hour = time / 100;
/*  787 */     int minute = time % 100;
/*      */ 
/*  789 */     if ((hour >= 0) && (hour <= 23) && 
/*  790 */       (minute >= 0) && (minute <= 59)) return true;
/*      */ 
/*  792 */     return false;
/*      */   }
/*      */ 
/*      */   public void enableButtons()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute attr)
/*      */   {
/*  808 */     if ((attr instanceof SelectionListener))
/*      */     {
/*  810 */       ITcm itcm = (SelectionListener)attr;
/*  811 */       setStormName(itcm.getStormName());
/*  812 */       setStormType(itcm.getStormType());
/*  813 */       setStormNumber(itcm.getStormNumber());
/*  814 */       setAdvisoryNumber(itcm.getAdvisoryNumber());
/*  815 */       setBasin(itcm.getBasin());
/*  816 */       setEyeSize(itcm.getEyeSize());
/*  817 */       setPositionAccuracy(itcm.getPositionAccuracy());
/*  818 */       setCorrection(itcm.isCorrection());
/*  819 */       setCentralPressure(itcm.getCentralPressure());
/*      */ 
/*  821 */       setAdvisoryTime(itcm.getAdvisoryTime());
/*  822 */       setWaves(itcm.getWaveQuarters());
/*      */ 
/*  824 */       setTcmFcstInfo((TcmFcst)itcm.getTcmFcst().get(0));
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getAdvisoryNumber()
/*      */   {
/*  834 */     return this.advisoryNumber.getSelection();
/*      */   }
/*      */ 
/*      */   private void setAdvisoryNumber(int adnum)
/*      */   {
/*  841 */     this.advisoryNumber.setSelection(adnum);
/*      */   }
/*      */ 
/*      */   public String getStormName() {
/*  845 */     return this.stormNameField.getText();
/*      */   }
/*      */ 
/*      */   private void setStormName(String name) {
/*  849 */     this.stormNameField.setText(name);
/*      */   }
/*      */ 
/*      */   public int getStormNumber() {
/*  853 */     return this.stormNumber.getSelection();
/*      */   }
/*      */ 
/*      */   private void setStormNumber(int num) {
/*  857 */     this.stormNumber.setSelection(num);
/*      */   }
/*      */ 
/*      */   public String getStormType() {
/*  861 */     return this.stormTypes.getText();
/*      */   }
/*      */ 
/*      */   private void setStormType(String type) {
/*  865 */     this.stormTypes.setText(type);
/*      */   }
/*      */ 
/*      */   public Calendar getAdvisoryTime() {
/*  869 */     int time = Integer.parseInt(this.validTime.getText());
/*  870 */     int hours = time / 100;
/*  871 */     int minutes = time % 100;
/*      */ 
/*  873 */     Calendar advTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*  874 */     advTime.set(this.validDate.getYear(), this.validDate.getMonth(), this.validDate.getDay(), 
/*  875 */       hours, minutes, 0);
/*  876 */     advTime.set(14, 0);
/*  877 */     return advTime;
/*      */   }
/*      */ 
/*      */   private void setAdvisoryTime(Calendar time) {
/*  881 */     this.validDate.setYear(time.get(1));
/*  882 */     this.validDate.setMonth(time.get(2));
/*  883 */     this.validDate.setDay(time.get(5));
/*      */ 
/*  885 */     this.validTime.setText(String.format("%02d%02d", new Object[] { Integer.valueOf(time.get(11)), Integer.valueOf(time.get(12)) }));
/*      */   }
/*      */ 
/*      */   public String getBasin()
/*      */   {
/*  892 */     return this.basinTypes.getText();
/*      */   }
/*      */ 
/*      */   private void setBasin(String basin) {
/*  896 */     this.basinTypes.setText(basin);
/*      */   }
/*      */ 
/*      */   public void widgetDefaultSelected(SelectionEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void widgetSelected(SelectionEvent e)
/*      */   {
/*  911 */     if ((e.widget instanceof Button)) {
/*  912 */       Button b = (Button)e.widget;
/*      */ 
/*  917 */       if (b.getText().equals("Add"))
/*      */       {
/*  919 */         if (this.tcm == null) {
/*  920 */           this.tcm = new Tcm(getStormType(), getStormNumber(), 
/*  921 */             getAdvisoryNumber(), getStormName(), 
/*  922 */             getBasin(), getEyeSize(), getPositionAccuracy(), 
/*  923 */             isCorrection(), 
/*  924 */             getAdvisoryTime(), getCentralPressure());
/*  925 */           this.drawingLayer.addElement(this.tcm);
/*      */         }
/*      */         else {
/*  928 */           Tcm newTcm = (Tcm)this.tcm.copy();
/*  929 */           this.drawingLayer.replaceElement(this.tcm, newTcm);
/*  930 */           this.tcm = newTcm;
/*      */         }
/*      */ 
/*  933 */         TcmFcst elem = (TcmFcst)new DrawableElementFactory().create(
/*  934 */           DrawableType.TCM_FCST, this, 
/*  935 */           this.pgenCategory, this.pgenType, new Coordinate(getLon(), getLat()), 
/*  936 */           this.drawingLayer.getActiveLayer());
/*      */ 
/*  938 */         elem.setSpeed(getStormSpeed());
/*  939 */         elem.setDirection(getStormDirection());
/*  940 */         elem.setGust(getGust());
/*  941 */         elem.setWindMax(getWindMax());
/*  942 */         elem.setParent(null);
/*      */ 
/*  944 */         this.tcm.addTcmFcst(elem);
/*  945 */         if (getFcstHr() == 0) {
/*  946 */           this.tcm.setWaveQuatro(getWaveQuarters());
/*      */         }
/*      */ 
/*  949 */         this.mapEditor.refresh();
/*      */ 
/*  951 */         this.addFcst.setEnabled(false);
/*      */       }
/*  957 */       else if (!b.getText().equals("Delete"))
/*      */       {
/*  964 */         if (b.getText().equals("Apply")) {
/*  965 */           Tcm newTcm = (Tcm)this.tcm.copy();
/*  966 */           newTcm.setStormType(getStormType());
/*  967 */           newTcm.setBasin(getBasin());
/*  968 */           newTcm.setStormName(getStormName());
/*  969 */           newTcm.setStormNumber(getStormNumber());
/*  970 */           newTcm.setAdvisoryNumber(getAdvisoryNumber());
/*  971 */           newTcm.setCorrection(isCorrection());
/*  972 */           newTcm.setEyeSize(getEyeSize());
/*  973 */           newTcm.setCentralPressure(getCentralPressure());
/*  974 */           newTcm.setPositionAccuracy(getPositionAccuracy());
/*  975 */           newTcm.setTime(getAdvisoryTime());
/*  976 */           newTcm.setWaveQuatro(getWaveQuarters());
/*      */ 
/*  978 */           TcmFcst newFcst = null;
/*  979 */           int ii = 0;
/*  980 */           for (TcmFcst fcst : newTcm.getTcmFcst())
/*      */           {
/*  982 */             if (getFcstHr() == fcst.getFcstHr()) {
/*  983 */               newFcst = (TcmFcst)new DrawableElementFactory().create(
/*  984 */                 DrawableType.TCM_FCST, this, 
/*  985 */                 this.pgenCategory, this.pgenType, new Coordinate(getLon(), getLat()), 
/*  986 */                 this.drawingLayer.getActiveLayer());
/*  987 */               newFcst.setSpeed(getStormSpeed());
/*  988 */               newFcst.setDirection(getStormDirection());
/*  989 */               newFcst.setGust(getGust());
/*  990 */               newFcst.setWindMax(getWindMax());
/*  991 */               newFcst.setParent(null);
/*      */ 
/*  993 */               break;
/*      */             }
/*  995 */             ii++;
/*      */           }
/*      */ 
/*  998 */           if (newFcst != null) {
/*  999 */             newTcm.getTcmFcst().remove(ii);
/* 1000 */             newTcm.getTcmFcst().add(ii, newFcst);
/*      */ 
/* 1002 */             if (ii == 0) newTcm.getWaveQuarters().setLocation(newFcst.getLocation());
/*      */           }
/*      */ 
/* 1005 */           this.drawingLayer.replaceElement(this.tcm, newTcm);
/* 1006 */           this.drawingLayer.removeGhostLine();
/* 1007 */           this.drawingLayer.setSelected(newTcm);
/* 1008 */           this.tcm = newTcm;
/* 1009 */           this.mapEditor.refresh();
/*      */         }
/* 1012 */         else if (b.getText().equals("Close")) {
/* 1013 */           this.tcm = null;
/* 1014 */           PgenUtil.setSelectingMode();
/* 1015 */           super.close();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private double getLat()
/*      */   {
/* 1028 */     double ret = 35.0D;
/*      */     try {
/* 1030 */       ret = Double.parseDouble(this.latField.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/* 1036 */     if ((ret >= 90.0D) || (ret <= -90.0D)) ret = 35.0D;
/*      */ 
/* 1038 */     return ret;
/*      */   }
/*      */ 
/*      */   private double getLon()
/*      */   {
/* 1047 */     double ret = -95.0D;
/*      */     try {
/* 1049 */       ret = Double.parseDouble(this.lonField.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/* 1055 */     if ((ret > 180.0D) || (ret < -180.0D)) ret = -95.0D;
/*      */ 
/* 1057 */     return ret;
/*      */   }
/*      */ 
/*      */   public double[][] getWindRadius()
/*      */   {
/* 1066 */     double[][] ret = new double[4][3];
/* 1067 */     ret[0][0] = getRadius(this.ne34Field);
/* 1068 */     ret[1][0] = getRadius(this.se34Field);
/* 1069 */     ret[2][0] = getRadius(this.sw34Field);
/* 1070 */     ret[3][0] = getRadius(this.nw34Field);
/*      */ 
/* 1072 */     ret[0][1] = getRadius(this.ne50Field);
/* 1073 */     ret[1][1] = getRadius(this.se50Field);
/* 1074 */     ret[2][1] = getRadius(this.sw50Field);
/* 1075 */     ret[3][1] = getRadius(this.nw50Field);
/*      */ 
/* 1077 */     ret[0][2] = getRadius(this.ne64Field);
/* 1078 */     ret[1][2] = getRadius(this.se64Field);
/* 1079 */     ret[2][2] = getRadius(this.sw64Field);
/* 1080 */     ret[3][2] = getRadius(this.nw64Field);
/*      */ 
/* 1082 */     return ret;
/*      */   }
/*      */ 
/*      */   private double getRadius(Text field)
/*      */   {
/* 1091 */     double ret = 0.0D;
/*      */     try {
/* 1093 */       ret = Double.parseDouble(field.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/* 1098 */     return ret;
/*      */   }
/*      */ 
/*      */   public List<TcmFcst> getTcmFcst()
/*      */   {
/* 1103 */     if (this.tcm == null)
/* 1104 */       return null;
/* 1105 */     return this.tcm.getTcmFcst();
/*      */   }
/*      */ 
/*      */   public int getCentralPressure()
/*      */   {
/* 1113 */     int ret = 0;
/*      */     try {
/* 1115 */       ret = Integer.parseInt(this.pressureField.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/* 1121 */     return ret;
/*      */   }
/*      */ 
/*      */   private void setCentralPressure(int pressure)
/*      */   {
/* 1129 */     this.pressureField.setText(Integer.toString(pressure));
/*      */   }
/*      */ 
/*      */   public int getFcstHr()
/*      */   {
/* 1138 */     int ret = 0;
/*      */     try {
/* 1140 */       ret = Integer.parseInt(this.fcstHrs.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/* 1145 */     return ret;
/*      */   }
/*      */ 
/*      */   public TcmWindQuarters getWaveQuarters()
/*      */   {
/* 1153 */     if (this.waves == null) {
/* 1154 */       return new TcmWindQuarters(((TcmFcst)this.tcm.getTcmFcst().get(0)).getLocation(), 
/* 1155 */         0, 
/* 1156 */         getRadius(this.ne12ftField), 
/* 1157 */         getRadius(this.se12ftField), 
/* 1158 */         getRadius(this.sw12ftField), 
/* 1159 */         getRadius(this.nw12ftField));
/*      */     }
/* 1161 */     return this.waves;
/*      */   }
/*      */ 
/*      */   public int getEyeSize()
/*      */   {
/* 1169 */     int ret = 0;
/*      */     try {
/* 1171 */       ret = Integer.parseInt(this.eyeSizeField.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/* 1176 */     return ret;
/*      */   }
/*      */ 
/*      */   private void setEyeSize(int eye)
/*      */   {
/* 1184 */     this.eyeSizeField.setText(Integer.toString(eye));
/*      */   }
/*      */ 
/*      */   public int getPositionAccuracy()
/*      */   {
/* 1192 */     int ret = 0;
/*      */     try {
/* 1194 */       ret = Integer.parseInt(this.accuracyField.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/* 1199 */     return ret;
/*      */   }
/*      */ 
/*      */   private void setPositionAccuracy(int posAccu)
/*      */   {
/* 1207 */     this.accuracyField.setText(Integer.toString(posAccu));
/*      */   }
/*      */ 
/*      */   public boolean isCorrection()
/*      */   {
/* 1215 */     return this.corr.getSelection();
/*      */   }
/*      */ 
/*      */   private void setCorrection(boolean corrFlag)
/*      */   {
/* 1223 */     this.corr.setSelection(corrFlag);
/*      */   }
/*      */ 
/*      */   private void setWaves(TcmWindQuarters waves)
/*      */   {
/* 1231 */     this.ne12ftField.setText(Integer.toString((int)waves.getQuarters()[0]));
/* 1232 */     this.se12ftField.setText(Integer.toString((int)waves.getQuarters()[1]));
/* 1233 */     this.sw12ftField.setText(Integer.toString((int)waves.getQuarters()[2]));
/* 1234 */     this.nw12ftField.setText(Integer.toString((int)waves.getQuarters()[3]));
/*      */   }
/*      */ 
/*      */   private void setTcmFcstInfo(TcmFcst fcst)
/*      */   {
/* 1242 */     int hr = fcst.getFcstHr();
/* 1243 */     this.fcstHrs.setText(hr == 0 ? "OBS" : String.format("%1$02d", new Object[] { Integer.valueOf(hr) }));
/* 1244 */     this.latField.setText(Double.toString(fcst.getLocation().y));
/* 1245 */     this.lonField.setText(Double.toString(fcst.getLocation().x));
/*      */ 
/* 1247 */     setGust(fcst.getGust());
/* 1248 */     setWindMax(fcst.getWindMax());
/* 1249 */     setStormSpeed(fcst.getSpeed());
/* 1250 */     setStormDirection(fcst.getDirection());
/*      */ 
/* 1252 */     this.ne34Field.setText(Integer.toString((int)fcst.getQuarters()[0].getQuarters()[0]));
/* 1253 */     this.nw34Field.setText(Integer.toString((int)fcst.getQuarters()[0].getQuarters()[3]));
/* 1254 */     this.sw34Field.setText(Integer.toString((int)fcst.getQuarters()[0].getQuarters()[2]));
/* 1255 */     this.se34Field.setText(Integer.toString((int)fcst.getQuarters()[0].getQuarters()[1]));
/*      */ 
/* 1257 */     this.ne50Field.setText(Integer.toString((int)fcst.getQuarters()[1].getQuarters()[0]));
/* 1258 */     this.nw50Field.setText(Integer.toString((int)fcst.getQuarters()[1].getQuarters()[3]));
/* 1259 */     this.sw50Field.setText(Integer.toString((int)fcst.getQuarters()[1].getQuarters()[2]));
/* 1260 */     this.se50Field.setText(Integer.toString((int)fcst.getQuarters()[1].getQuarters()[1]));
/*      */ 
/* 1262 */     this.ne64Field.setText(Integer.toString((int)fcst.getQuarters()[2].getQuarters()[0]));
/* 1263 */     this.nw64Field.setText(Integer.toString((int)fcst.getQuarters()[2].getQuarters()[3]));
/* 1264 */     this.sw64Field.setText(Integer.toString((int)fcst.getQuarters()[2].getQuarters()[2]));
/* 1265 */     this.se64Field.setText(Integer.toString((int)fcst.getQuarters()[2].getQuarters()[1]));
/*      */   }
/*      */ 
/*      */   public void setTcm(Tcm tcm)
/*      */   {
/* 1273 */     this.tcm = tcm;
/*      */   }
/*      */ 
/*      */   public int getGust()
/*      */   {
/* 1281 */     int ret = 0;
/*      */     try
/*      */     {
/* 1284 */       ret = Integer.parseInt(this.gustField.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/* 1290 */     return ret;
/*      */   }
/*      */ 
/*      */   private void setGust(int gust)
/*      */   {
/* 1298 */     this.gustField.setText(Integer.toString(gust));
/*      */   }
/*      */ 
/*      */   public int getWindMax()
/*      */   {
/* 1306 */     int ret = 0;
/*      */     try
/*      */     {
/* 1309 */       ret = Integer.parseInt(this.windMaxField.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/* 1315 */     return ret;
/*      */   }
/*      */ 
/*      */   private void setWindMax(int wind)
/*      */   {
/* 1323 */     this.windMaxField.setText(Integer.toString(wind));
/*      */   }
/*      */ 
/*      */   public int getStormSpeed()
/*      */   {
/* 1331 */     int ret = 0;
/*      */     try
/*      */     {
/* 1334 */       ret = Integer.parseInt(this.spdField.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/* 1340 */     return ret;
/*      */   }
/*      */ 
/*      */   private void setStormSpeed(int speed)
/*      */   {
/* 1348 */     this.spdField.setText(Integer.toString(speed));
/*      */   }
/*      */ 
/*      */   public int getStormDirection()
/*      */   {
/* 1356 */     int ret = 0;
/*      */     try
/*      */     {
/* 1359 */       ret = Integer.parseInt(this.dirField.getText());
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/* 1365 */     return ret;
/*      */   }
/*      */ 
/*      */   private void setStormDirection(int dir)
/*      */   {
/* 1373 */     this.dirField.setText(Integer.toString(dir));
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.TcmAttrDlg
 * JD-Core Version:    0.6.2
 */