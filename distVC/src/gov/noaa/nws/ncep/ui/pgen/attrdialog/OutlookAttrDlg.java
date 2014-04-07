/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.SpinnerSlider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.IContours;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.ui.pgen.graphtogrid.GraphToGridParamDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*      */ import java.awt.Color;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import org.dom4j.Document;
/*      */ import org.dom4j.Node;
/*      */ import org.dom4j.io.SAXReader;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.swt.events.SelectionAdapter;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.graphics.RGB;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Slider;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class OutlookAttrDlg extends AttrDlg
/*      */   implements IContours, ILine
/*      */ {
/*      */   private static Document outlookTbl;
/*      */   private static HashMap<String, Line> settings;
/*   96 */   private static final String[] SYMBOL_LIST = { "PAST_WX_09", "PRESENT_WX_065", "PRESENT_WX_073", 
/*   97 */     "PRESENT_WX_075", "PRESENT_WX_073|PRESENT_WX_075", "PRESENT_WX_056", 
/*   98 */     "PRESENT_WX_079", "PRESENT_WX_056|PRESENT_WX_079" };
/*      */   private static OutlookAttrDlg INSTANCE;
/*  104 */   public static String OTLK_XPATH = "/root/otlktype";
/*      */   private OutlookFormatDlg fmtDlg;
/*      */   private Composite top;
/*      */   private Composite panel2;
/*      */   private Label colorLbl;
/*      */   protected ColorButtonSelector cs;
/*      */   private Label widthLbl;
/*      */   protected Slider widthSlider;
/*      */   protected org.eclipse.swt.widgets.Text widthText;
/*      */   protected SpinnerSlider widthSpinnerSlider;
/*      */   private Label smoothLbl;
/*      */   protected Combo smoothLvlCbo;
/*      */   private Button closedBtn;
/*      */   private Button filledBtn;
/*      */   private Button infoBtn;
/*      */   private Button makeGridBtn;
/*      */   private Button[] chkBox;
/*      */   private Combo outlookCombo;
/*      */   private Button lblBtn;
/*      */   private Button txtBtn;
/*      */   private Combo txtCombo;
/*      */   private Button symbolBtn;
/*      */   private SymbolCombo symbolCombo;
/*      */   private Button lnColorBtn;
/*      */   private Button addLineBtn;
/*      */   private Button delLineBtn;
/*      */   private Button setContBtn;
/*      */   private Button showContBtn;
/*      */   private Button fmtBtn;
/*      */   private String prevLbl;
/*      */   private String prevType;
/*      */   private boolean useLineColor;
/*  190 */   private static GraphToGridParamDialog g2gDlg = null;
/*      */ 
/*  193 */   private static ContoursInfoDlg contoursInfoDlg = null;
/*      */ 
/*  195 */   private String contourParm = "HGMT";
/*      */ 
/*  197 */   private String contourLevel = "1000";
/*  198 */   private String contourFcstHr = "f000";
/*      */ 
/*  200 */   private Calendar contourTime1 = Calendar.getInstance();
/*  201 */   private Calendar contourTime2 = Calendar.getInstance();
/*  202 */   private String contourCint = "10/0/100";
/*      */ 
/*  204 */   private boolean needFromLine = false;
/*      */ 
/*  206 */   private boolean showGrid = true;
/*  207 */   private boolean flagLabel = true;
/*  208 */   private boolean flagAction = true;
/*      */ 
/*  210 */   private String defaultLinetype = "POINTED_ARROW";
/*  211 */   private String lineType = this.defaultLinetype;
/*      */ 
/*      */   protected OutlookAttrDlg(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  220 */     super(parShell);
/*  221 */     this.useLineColor = true;
/*      */   }
/*      */ 
/*      */   public static OutlookAttrDlg getInstance(Shell parShell)
/*      */   {
/*  233 */     if (INSTANCE == null)
/*      */     {
/*      */       try {
/*  236 */         INSTANCE = new OutlookAttrDlg(parShell);
/*      */       } catch (VizException e) {
/*  238 */         e.printStackTrace();
/*      */       }
/*      */ 
/*  241 */       loadSettings();
/*      */     }
/*      */ 
/*  244 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  254 */     this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/*  257 */     GridLayout mainLayout = new GridLayout(1, false);
/*  258 */     mainLayout.marginHeight = 3;
/*  259 */     mainLayout.marginWidth = 3;
/*  260 */     this.top.setLayout(mainLayout);
/*      */ 
/*  263 */     initializeComponents();
/*      */ 
/*  265 */     return this.top;
/*      */   }
/*      */ 
/*      */   protected void initializeComponents()
/*      */   {
/*  273 */     getShell().setText("Outlook Attributes");
/*      */ 
/*  277 */     Composite infoComp = new Composite(this.top, 0);
/*  278 */     infoComp.setLayoutData(new GridData(16777216, -1, true, false));
/*      */ 
/*  280 */     GridLayout layout = new GridLayout(2, false);
/*  281 */     layout.horizontalSpacing = 20;
/*  282 */     infoComp.setLayout(layout);
/*      */ 
/*  284 */     this.infoBtn = new Button(infoComp, 8);
/*  285 */     this.infoBtn.setToolTipText("Bring up the contour attribute dialog");
/*  286 */     this.infoBtn.setEnabled(this.showGrid);
/*  287 */     setInfoBtnText();
/*      */ 
/*  289 */     this.infoBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  291 */         OutlookAttrDlg.this.openContourInfoDlg();
/*      */       }
/*      */     });
/*  296 */     this.makeGridBtn = new Button(infoComp, 8);
/*  297 */     this.makeGridBtn.setText("Make Grid");
/*  298 */     this.makeGridBtn.setToolTipText("Generate grid for this Outlook");
/*  299 */     this.makeGridBtn.setEnabled(this.showGrid);
/*  300 */     this.makeGridBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  303 */         OutlookAttrDlg.this.openG2GDlg();
/*      */       }
/*      */     });
/*  307 */     addSeparator(this.top);
/*      */ 
/*  310 */     Composite panel1 = new Composite(this.top, 0);
/*  311 */     panel1.setLayout(new GridLayout(2, false));
/*      */ 
/*  314 */     Label outlookLbl = new Label(panel1, 16384);
/*  315 */     outlookLbl.setText("Type:");
/*  316 */     this.outlookCombo = new Combo(panel1, 12);
/*      */ 
/*  318 */     List types = getOutlookTypes();
/*  319 */     if (!types.isEmpty()) {
/*  320 */       for (String str : types) {
/*  321 */         this.outlookCombo.add(str);
/*      */       }
/*      */     }
/*      */     else {
/*  325 */       this.outlookCombo.add("OUTLOOK");
/*      */     }
/*      */ 
/*  328 */     this.outlookCombo.select(0);
/*  329 */     this.outlookCombo.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  333 */         String type = ((Combo)e.widget).getText();
/*  334 */         OutlookAttrDlg.this.prevType = type;
/*      */ 
/*  336 */         boolean warning = false;
/*      */ 
/*  340 */         Iterator it = OutlookAttrDlg.this.drawingLayer.getActiveLayer().getComponentIterator();
/*  341 */         while (it.hasNext()) {
/*  342 */           AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/*  343 */           if (((adc instanceof Outlook)) && (!((Outlook)adc).getOutlookType().equalsIgnoreCase(type))) {
/*  344 */             warning = true;
/*  345 */             break;
/*      */           }
/*      */         }
/*      */ 
/*  349 */         if (warning) {
/*  350 */           MessageDialog msgDlg = new MessageDialog(
/*  351 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  352 */             "Warning!", null, "There is at least one outlook with different type in the same layer!", 
/*  353 */             2, new String[] { "OK" }, 0);
/*  354 */           msgDlg.open();
/*      */         }
/*      */ 
/*  358 */         OutlookAttrDlg.this.showGrid = OutlookAttrDlg.this.showMakeGrid(type);
/*  359 */         OutlookAttrDlg.this.flagLabel = OutlookAttrDlg.this.showLabel(type);
/*  360 */         OutlookAttrDlg.this.flagAction = OutlookAttrDlg.this.showAction(type);
/*      */ 
/*  362 */         OutlookAttrDlg.this.infoBtn.setEnabled(OutlookAttrDlg.this.showGrid);
/*  363 */         OutlookAttrDlg.this.makeGridBtn.setEnabled(OutlookAttrDlg.this.showGrid);
/*  364 */         OutlookAttrDlg.this.lblBtn.setEnabled(OutlookAttrDlg.this.flagLabel);
/*  365 */         OutlookAttrDlg.this.txtBtn.setEnabled(OutlookAttrDlg.this.flagLabel);
/*  366 */         OutlookAttrDlg.this.symbolBtn.setEnabled(OutlookAttrDlg.this.flagLabel);
/*  367 */         OutlookAttrDlg.this.lnColorBtn.setEnabled(OutlookAttrDlg.this.flagLabel);
/*  368 */         OutlookAttrDlg.this.addLineBtn.setEnabled(OutlookAttrDlg.this.flagAction);
/*  369 */         OutlookAttrDlg.this.delLineBtn.setEnabled(OutlookAttrDlg.this.flagAction);
/*  370 */         OutlookAttrDlg.this.setContBtn.setEnabled(OutlookAttrDlg.this.flagAction);
/*  371 */         OutlookAttrDlg.this.showContBtn.setEnabled(OutlookAttrDlg.this.flagAction);
/*  372 */         OutlookAttrDlg.this.fmtBtn.setEnabled(OutlookAttrDlg.this.flagAction);
/*      */ 
/*  374 */         OutlookAttrDlg.this.needFromLine = OutlookAttrDlg.this.setFromLineFlag(type);
/*      */ 
/*  377 */         OutlookAttrDlg.this.setDefaultLabels(type);
/*      */ 
/*  380 */         OutlookAttrDlg.this.setDefaultLineAttr(OutlookAttrDlg.this.outlookCombo.getText() + OutlookAttrDlg.this.txtCombo.getText());
/*      */       }
/*      */     });
/*  385 */     if (this.prevType == null) {
/*  386 */       this.outlookCombo.select(0);
/*  387 */       this.prevType = this.outlookCombo.getText();
/*      */     }
/*      */ 
/*  391 */     this.lblBtn = new Button(panel1, 32);
/*  392 */     this.lblBtn.setText("Label:");
/*  393 */     this.lblBtn.setEnabled(this.flagLabel);
/*  394 */     this.lblBtn.setSelection(true);
/*  395 */     this.lblBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  398 */         if (((Button)e.widget).getSelection()) {
/*  399 */           OutlookAttrDlg.this.txtBtn.setEnabled(OutlookAttrDlg.this.flagLabel);
/*  400 */           OutlookAttrDlg.this.txtBtn.setSelection(true);
/*  401 */           OutlookAttrDlg.this.txtCombo.setEnabled(OutlookAttrDlg.this.flagLabel);
/*  402 */           OutlookAttrDlg.this.symbolBtn.setEnabled(OutlookAttrDlg.this.flagLabel);
/*  403 */           OutlookAttrDlg.this.symbolCombo.setEnabled(OutlookAttrDlg.this.flagLabel);
/*  404 */           OutlookAttrDlg.this.lnColorBtn.setEnabled(OutlookAttrDlg.this.flagLabel);
/*      */         }
/*      */         else {
/*  407 */           OutlookAttrDlg.this.txtBtn.setSelection(false);
/*  408 */           OutlookAttrDlg.this.txtBtn.setEnabled(false);
/*  409 */           OutlookAttrDlg.this.txtCombo.setEnabled(false);
/*      */ 
/*  411 */           OutlookAttrDlg.this.symbolBtn.setSelection(false);
/*  412 */           OutlookAttrDlg.this.symbolBtn.setEnabled(false);
/*  413 */           OutlookAttrDlg.this.symbolCombo.setEnabled(false);
/*      */ 
/*  415 */           OutlookAttrDlg.this.lnColorBtn.setSelection(false);
/*  416 */           OutlookAttrDlg.this.lnColorBtn.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  422 */     Group lblGrp = new Group(panel1, 0);
/*  423 */     GridLayout layout1 = new GridLayout(1, false);
/*  424 */     layout1.marginWidth = 10;
/*  425 */     lblGrp.setLayout(layout1);
/*      */ 
/*  428 */     Composite txtComp = new Composite(lblGrp, 0);
/*  429 */     GridLayout txtLayout = new GridLayout(2, false);
/*  430 */     txtLayout.marginWidth = 0;
/*  431 */     txtComp.setLayout(txtLayout);
/*      */ 
/*  433 */     this.txtBtn = new Button(txtComp, 32);
/*  434 */     this.txtBtn.setLayoutData(new GridData(55, -1));
/*  435 */     this.txtBtn.setText("Text");
/*  436 */     this.txtBtn.setEnabled(this.flagLabel);
/*  437 */     this.txtBtn.setSelection(true);
/*  438 */     this.txtBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  441 */         if ((((Button)e.widget).getSelection()) && (OutlookAttrDlg.this.symbolBtn.getSelection()))
/*      */         {
/*  443 */           OutlookAttrDlg.this.symbolBtn.setSelection(false);
/*      */         }
/*      */       }
/*      */     });
/*  449 */     this.txtCombo = new Combo(txtComp, 12);
/*  450 */     this.txtCombo.setLayoutData(new GridData(100, -1));
/*      */ 
/*  452 */     this.txtCombo.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  456 */         OutlookAttrDlg.this.prevLbl = ((Combo)e.widget).getText();
/*  457 */         OutlookAttrDlg.this.setDefaultLineAttr(OutlookAttrDlg.this.outlookCombo.getText() + OutlookAttrDlg.this.txtCombo.getText());
/*      */       }
/*      */     });
/*  462 */     setDefaultLabels(this.outlookCombo.getText());
/*      */ 
/*  464 */     if (this.prevLbl == null) {
/*  465 */       this.txtCombo.select(0);
/*  466 */       this.prevLbl = this.txtCombo.getText();
/*      */     }
/*      */ 
/*  470 */     this.symbolBtn = new Button(txtComp, 32);
/*  471 */     this.symbolBtn.setText("Symbol");
/*  472 */     this.symbolBtn.setEnabled(this.flagLabel);
/*  473 */     this.symbolBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  476 */         if ((((Button)e.widget).getSelection()) && (OutlookAttrDlg.this.txtBtn.getSelection()))
/*  477 */           OutlookAttrDlg.this.txtBtn.setSelection(false);
/*      */       }
/*      */     });
/*  483 */     this.symbolCombo = new SymbolCombo(txtComp);
/*  484 */     this.symbolCombo.setLayoutData(new GridData(10, 1));
/*  485 */     this.symbolCombo.setItems(SYMBOL_LIST);
/*  486 */     this.symbolCombo.select(0);
/*      */ 
/*  489 */     this.lnColorBtn = new Button(lblGrp, 32);
/*  490 */     this.lnColorBtn.setText("Use Line Color");
/*  491 */     this.lnColorBtn.setEnabled(this.flagLabel);
/*  492 */     this.lnColorBtn.setSelection(this.useLineColor);
/*  493 */     this.lnColorBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  497 */         OutlookAttrDlg.this.useLineColor = ((Button)e.widget).getSelection();
/*      */       }
/*      */     });
/*  503 */     Composite btnsComp = new Composite(this.top, 0);
/*  504 */     GridLayout gl = new GridLayout(2, false);
/*  505 */     gl.marginLeft = 15;
/*      */ 
/*  507 */     btnsComp.setLayout(gl);
/*      */ 
/*  510 */     this.addLineBtn = new Button(btnsComp, 8);
/*  511 */     this.addLineBtn.setText("Add Line");
/*  512 */     this.addLineBtn.setLayoutData(new GridData(120, 30));
/*  513 */     this.addLineBtn.setEnabled(this.flagAction);
/*  514 */     this.addLineBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  517 */         PgenUtil.loadOutlookDrawingTool();
/*      */       }
/*      */     });
/*  522 */     this.delLineBtn = new Button(btnsComp, 8);
/*  523 */     this.delLineBtn.setText("Del Line");
/*  524 */     this.delLineBtn.setLayoutData(new GridData(120, 30));
/*  525 */     this.delLineBtn.setEnabled(this.flagAction);
/*  526 */     this.delLineBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  529 */         if ((OutlookAttrDlg.this.drawingLayer.getSelectedDE() != null) && (OutlookAttrDlg.de != null) && ((OutlookAttrDlg.de instanceof Line)) && 
/*  530 */           (OutlookAttrDlg.de.getParent().getName().equalsIgnoreCase(Outlook.OUTLOOK_LABELED_LINE)))
/*      */         {
/*  532 */           DECollection dec = (DECollection)OutlookAttrDlg.de.getParent();
/*      */ 
/*  535 */           Outlook oldOtlk = null;
/*  536 */           if ((dec.getParent() instanceof Outlook)) {
/*  537 */             oldOtlk = (Outlook)dec.getParent();
/*      */           }
/*  539 */           else if ((dec.getParent().getParent() instanceof Outlook)) {
/*  540 */             oldOtlk = (Outlook)dec.getParent().getParent();
/*      */           }
/*      */ 
/*  543 */           if (oldOtlk.size() <= 1)
/*      */           {
/*  545 */             if (OutlookAttrDlg.this.drawingLayer.getActiveLayer().getDrawables().contains(oldOtlk)) {
/*  546 */               OutlookAttrDlg.this.drawingLayer.removeElement(oldOtlk);
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  552 */             String deCat = OutlookAttrDlg.de.getPgenCategory();
/*  553 */             OutlookAttrDlg.de.setPgenCategory("del");
/*      */ 
/*  556 */             Outlook newOtlk = oldOtlk.copy();
/*      */ 
/*  559 */             Iterator it = newOtlk.createDEIterator();
/*  560 */             while (it.hasNext()) {
/*  561 */               DrawableElement tmpDe = (DrawableElement)it.next();
/*  562 */               if (((tmpDe instanceof Line)) && (tmpDe.getPgenCategory().equalsIgnoreCase("del"))) {
/*  563 */                 newOtlk.removeLine((Line)tmpDe);
/*  564 */                 break;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  569 */             OutlookAttrDlg.de.setPgenCategory(deCat);
/*      */ 
/*  572 */             OutlookAttrDlg.this.drawingLayer.replaceElement(oldOtlk, newOtlk);
/*      */           }
/*      */ 
/*  576 */           OutlookAttrDlg.this.drawingLayer.removeSelected();
/*      */         }
/*      */ 
/*  580 */         if (OutlookAttrDlg.this.mapEditor != null)
/*  581 */           OutlookAttrDlg.this.mapEditor.refresh();
/*      */       }
/*      */     });
/*  587 */     this.setContBtn = new Button(btnsComp, 8);
/*  588 */     this.setContBtn.setText("Set Cont");
/*  589 */     this.setContBtn.setLayoutData(new GridData(120, 30));
/*  590 */     this.setContBtn.setEnabled(this.flagAction);
/*  591 */     this.setContBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  594 */         Outlook ol = null;
/*  595 */         if ((OutlookAttrDlg.de.getParent() instanceof Outlook)) {
/*  596 */           ol = (Outlook)OutlookAttrDlg.de.getParent();
/*      */         }
/*  598 */         else if ((OutlookAttrDlg.de.getParent().getParent() instanceof Outlook)) {
/*  599 */           ol = (Outlook)OutlookAttrDlg.de.getParent().getParent();
/*      */         }
/*  601 */         OutlookAttrDlg.this.drawingLayer.removeSelected();
/*  602 */         PgenUtil.loadOutlookSetContTool(ol);
/*      */       }
/*      */     });
/*  607 */     this.showContBtn = new Button(btnsComp, 2);
/*  608 */     this.showContBtn.setText("Show Cont");
/*  609 */     this.showContBtn.setLayoutData(new GridData(120, 30));
/*  610 */     this.showContBtn.setEnabled(this.flagAction);
/*  611 */     this.showContBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  614 */         if (((Button)e.widget).getSelection())
/*      */         {
/*  616 */           if ((OutlookAttrDlg.de.getParent().getParent() instanceof Outlook)) {
/*  617 */             OutlookAttrDlg.this.showContLines((Outlook)OutlookAttrDlg.de.getParent().getParent());
/*      */           }
/*  619 */           else if ((OutlookAttrDlg.de.getParent().getParent().getParent() != null) && 
/*  620 */             ((OutlookAttrDlg.de.getParent().getParent().getParent() instanceof Outlook))) {
/*  621 */             OutlookAttrDlg.this.showContLines((Outlook)OutlookAttrDlg.de.getParent().getParent().getParent());
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  626 */           OutlookAttrDlg.this.drawingLayer.removeGhostLine();
/*  627 */           OutlookAttrDlg.this.mapEditor.refresh();
/*      */         }
/*      */       }
/*      */     });
/*  633 */     this.fmtBtn = new Button(btnsComp, 8);
/*  634 */     this.fmtBtn.setText("Format");
/*  635 */     this.fmtBtn.setLayoutData(new GridData(120, 30));
/*  636 */     this.fmtBtn.setEnabled(this.flagAction);
/*  637 */     this.fmtBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  651 */         OutlookAttrDlg.this.fmtDlg = new OutlookFormatDlg(OutlookAttrDlg.this.getParentShell(), OutlookAttrDlg.this, OutlookAttrDlg.this.findOtlk());
/*  652 */         OutlookAttrDlg.this.fmtDlg.open();
/*      */       }
/*      */     });
/*  659 */     AttrDlg.addSeparator(this.top);
/*      */ 
/*  662 */     this.panel2 = new Composite(this.top, 0);
/*      */ 
/*  665 */     GridLayout p2Layout = new GridLayout(3, false);
/*  666 */     p2Layout.marginHeight = 3;
/*  667 */     p2Layout.marginWidth = 3;
/*  668 */     this.panel2.setLayout(p2Layout);
/*      */ 
/*  670 */     this.chkBox = new Button[5];
/*      */ 
/*  672 */     createColorAttr();
/*  673 */     createWidthAttr();
/*  674 */     createSmoothAttr();
/*  675 */     createCloseAttr();
/*  676 */     createFillAttr();
/*      */ 
/*  678 */     AttrDlg.addSeparator(this.top);
/*      */   }
/*      */ 
/*      */   public String getParm()
/*      */   {
/*  685 */     return this.contourParm;
/*      */   }
/*      */ 
/*      */   public void setParm(String contourParm)
/*      */   {
/*  692 */     this.contourParm = contourParm;
/*      */   }
/*      */ 
/*      */   public String getLevel()
/*      */   {
/*  699 */     return this.contourLevel;
/*      */   }
/*      */ 
/*      */   public void setLevel(String contourLevel)
/*      */   {
/*  706 */     this.contourLevel = contourLevel;
/*      */   }
/*      */ 
/*      */   public String getForecastHour()
/*      */   {
/*  713 */     return this.contourFcstHr;
/*      */   }
/*      */ 
/*      */   public void setForecsatHour(String fcsthr)
/*      */   {
/*  720 */     this.contourFcstHr = fcsthr;
/*      */   }
/*      */ 
/*      */   public Calendar getTime1()
/*      */   {
/*  727 */     return this.contourTime1;
/*      */   }
/*      */ 
/*      */   public Calendar getTime2()
/*      */   {
/*  734 */     return this.contourTime2;
/*      */   }
/*      */ 
/*      */   public void setTime1(Calendar contourTime)
/*      */   {
/*  741 */     this.contourTime1 = contourTime;
/*      */   }
/*      */ 
/*      */   public void setTime2(Calendar contourTime)
/*      */   {
/*  748 */     this.contourTime2 = contourTime;
/*      */   }
/*      */ 
/*      */   public String getCint()
/*      */   {
/*  755 */     return this.contourCint;
/*      */   }
/*      */ 
/*      */   public void setCint(String contourCint)
/*      */   {
/*  762 */     this.contourCint = contourCint;
/*      */   }
/*      */ 
/*      */   public Color[] getColors()
/*      */   {
/*  769 */     if (this.chkBox[ChkBox.COLOR.ordinal()].getSelection())
/*      */     {
/*  772 */       Color[] colors = new Color[2];
/*      */ 
/*  774 */       colors[0] = new Color(this.cs.getColorValue().red, 
/*  775 */         this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*      */ 
/*  777 */       colors[1] = colors[0];
/*      */ 
/*  779 */       return colors;
/*      */     }
/*      */ 
/*  782 */     return null;
/*      */   }
/*      */ 
/*      */   private void setColor(Color clr)
/*      */   {
/*  792 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*      */   }
/*      */ 
/*      */   public float getLineWidth()
/*      */   {
/*  800 */     if (this.chkBox[ChkBox.WIDTH.ordinal()].getSelection()) {
/*  801 */       return this.widthSpinnerSlider.getSelection();
/*      */     }
/*      */ 
/*  804 */     return (0.0F / 0.0F);
/*      */   }
/*      */ 
/*      */   private void setLineWidth(float lw)
/*      */   {
/*  813 */     this.widthSpinnerSlider.setSelection((int)lw);
/*      */   }
/*      */ 
/*      */   public Boolean isClosedLine()
/*      */   {
/*  822 */     if (this.chkBox[ChkBox.CLOSE.ordinal()].getSelection()) {
/*  823 */       return Boolean.valueOf(this.closedBtn.getSelection());
/*      */     }
/*      */ 
/*  826 */     return null;
/*      */   }
/*      */ 
/*      */   public FillPatternList.FillPattern getFillPattern()
/*      */   {
/*  834 */     return FillPatternList.FillPattern.FILL_PATTERN_1;
/*      */   }
/*      */ 
/*      */   private void setClosed(Boolean cls)
/*      */   {
/*  842 */     if (this.closedBtn != null)
/*  843 */       this.closedBtn.setSelection(cls.booleanValue());
/*      */   }
/*      */ 
/*      */   public Boolean isFilled()
/*      */   {
/*  851 */     if (this.chkBox[ChkBox.FILL.ordinal()].getSelection())
/*      */     {
/*  853 */       return Boolean.valueOf(this.filledBtn.getSelection());
/*      */     }
/*      */ 
/*  856 */     return null;
/*      */   }
/*      */ 
/*      */   private void setFilled(Boolean filled)
/*      */   {
/*  865 */     if (this.filledBtn != null)
/*  866 */       this.filledBtn.setSelection(filled.booleanValue());
/*      */   }
/*      */ 
/*      */   public int getSmoothFactor()
/*      */   {
/*  874 */     if (this.chkBox[ChkBox.SMOOTH.ordinal()].getSelection()) {
/*  875 */       return this.smoothLvlCbo.getSelectionIndex();
/*      */     }
/*      */ 
/*  878 */     return -1;
/*      */   }
/*      */ 
/*      */   public void setSmoothLvl(int sl)
/*      */   {
/*  887 */     this.smoothLvlCbo.select(sl);
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute iattr)
/*      */   {
/*  895 */     if ((iattr instanceof ILine)) {
/*  896 */       ILine attr = (ILine)iattr;
/*  897 */       Color clr = attr.getColors()[0];
/*  898 */       if (clr != null) setColor(clr);
/*      */ 
/*  900 */       float lw = attr.getLineWidth();
/*  901 */       if (lw > 0.0F) setLineWidth(lw);
/*      */ 
/*  903 */       setClosed(attr.isClosedLine());
/*  904 */       setFilled(attr.isFilled());
/*      */ 
/*  906 */       int sl = attr.getSmoothFactor();
/*  907 */       if (sl >= 0) setSmoothLvl(sl);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IContours ic)
/*      */   {
/*  915 */     setAttributes(ic);
/*      */   }
/*      */ 
/*      */   private void createColorAttr()
/*      */   {
/*  924 */     this.chkBox[ChkBox.COLOR.ordinal()] = new Button(this.panel2, 32);
/*  925 */     this.chkBox[ChkBox.COLOR.ordinal()].setLayoutData(new GridData(16, 28));
/*  926 */     this.chkBox[ChkBox.COLOR.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  930 */         Button btn = (Button)e.widget;
/*  931 */         if (btn.getSelection()) {
/*  932 */           OutlookAttrDlg.this.colorLbl.setEnabled(true);
/*      */         }
/*      */         else
/*  935 */           OutlookAttrDlg.this.colorLbl.setEnabled(false);
/*      */       }
/*      */     });
/*  941 */     this.colorLbl = new Label(this.panel2, 16384);
/*  942 */     this.colorLbl.setText("Color:");
/*      */ 
/*  945 */     this.cs = new ColorButtonSelector(this.panel2);
/*  946 */     this.cs.setColorValue(new RGB(255, 0, 0));
/*      */   }
/*      */ 
/*      */   private void createWidthAttr()
/*      */   {
/*  954 */     this.chkBox[ChkBox.WIDTH.ordinal()] = new Button(this.panel2, 32);
/*  955 */     this.chkBox[ChkBox.WIDTH.ordinal()].setLayoutData(new GridData(16, 28));
/*  956 */     this.chkBox[ChkBox.WIDTH.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  960 */         Button btn = (Button)e.widget;
/*  961 */         if (btn.getSelection()) {
/*  962 */           OutlookAttrDlg.this.widthLbl.setEnabled(true);
/*  963 */           OutlookAttrDlg.this.widthSlider.setEnabled(true);
/*      */         }
/*      */         else {
/*  966 */           OutlookAttrDlg.this.widthLbl.setEnabled(false);
/*  967 */           OutlookAttrDlg.this.widthSlider.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  974 */     this.widthLbl = new Label(this.panel2, 16384);
/*  975 */     this.widthLbl.setText("Line Width:");
/*      */ 
/*  977 */     GridLayout gl = new GridLayout(3, false);
/*  978 */     Group widthGrp = new Group(this.panel2, 0);
/*  979 */     gl.horizontalSpacing = 1;
/*  980 */     gl.verticalSpacing = 0;
/*  981 */     gl.marginHeight = 1;
/*  982 */     gl.marginWidth = 1;
/*  983 */     widthGrp.setLayout(gl);
/*      */ 
/*  995 */     this.widthSpinnerSlider = 
/*  996 */       new SpinnerSlider(widthGrp, 256, 1);
/*  997 */     this.widthSpinnerSlider.setLayoutData(new GridData(130, 30));
/*  998 */     this.widthSpinnerSlider.setMinimum(1);
/*  999 */     this.widthSpinnerSlider.setMaximum(10);
/* 1000 */     this.widthSpinnerSlider.setIncrement(1);
/* 1001 */     this.widthSpinnerSlider.setPageIncrement(3);
/* 1002 */     this.widthSpinnerSlider.setDigits(0);
/*      */   }
/*      */ 
/*      */   private void createSmoothAttr()
/*      */   {
/* 1009 */     this.chkBox[ChkBox.SMOOTH.ordinal()] = new Button(this.panel2, 32);
/* 1010 */     this.chkBox[ChkBox.SMOOTH.ordinal()].setLayoutData(new GridData(16, 28));
/* 1011 */     this.chkBox[ChkBox.SMOOTH.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/* 1015 */         Button btn = (Button)e.widget;
/* 1016 */         if (btn.getSelection()) {
/* 1017 */           OutlookAttrDlg.this.smoothLbl.setEnabled(true);
/* 1018 */           OutlookAttrDlg.this.smoothLvlCbo.setEnabled(true);
/*      */         }
/*      */         else {
/* 1021 */           OutlookAttrDlg.this.smoothLbl.setEnabled(false);
/* 1022 */           OutlookAttrDlg.this.smoothLvlCbo.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/* 1029 */     this.smoothLbl = new Label(this.panel2, 16384);
/* 1030 */     this.smoothLbl.setText("Smooth Level:");
/*      */ 
/* 1032 */     this.smoothLvlCbo = new Combo(this.panel2, 12);
/*      */ 
/* 1034 */     this.smoothLvlCbo.add("0");
/* 1035 */     this.smoothLvlCbo.add("1");
/* 1036 */     this.smoothLvlCbo.add("2");
/*      */ 
/* 1038 */     this.smoothLvlCbo.select(0);
/*      */   }
/*      */ 
/*      */   private void createCloseAttr()
/*      */   {
/* 1046 */     this.chkBox[ChkBox.CLOSE.ordinal()] = new Button(this.panel2, 32);
/* 1047 */     this.chkBox[ChkBox.CLOSE.ordinal()].setLayoutData(new GridData(16, 28));
/* 1048 */     this.chkBox[ChkBox.CLOSE.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/* 1052 */         Button btn = (Button)e.widget;
/* 1053 */         if (btn.getSelection()) {
/* 1054 */           OutlookAttrDlg.this.closedBtn.setEnabled(true);
/*      */         }
/*      */         else
/* 1057 */           OutlookAttrDlg.this.closedBtn.setEnabled(false);
/*      */       }
/*      */     });
/* 1063 */     this.closedBtn = new Button(this.panel2, 32);
/* 1064 */     this.closedBtn.setText("Closed");
/*      */   }
/*      */ 
/*      */   private void createFillAttr()
/*      */   {
/* 1073 */     Composite fillGrp = new Composite(this.panel2, 0);
/* 1074 */     fillGrp.setLayout(new GridLayout(2, false));
/*      */ 
/* 1076 */     this.chkBox[ChkBox.FILL.ordinal()] = new Button(fillGrp, 32);
/* 1077 */     this.chkBox[ChkBox.FILL.ordinal()].setLayoutData(new GridData(16, 33));
/* 1078 */     this.chkBox[ChkBox.FILL.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/* 1082 */         Button btn = (Button)e.widget;
/* 1083 */         if (btn.getSelection()) {
/* 1084 */           OutlookAttrDlg.this.filledBtn.setEnabled(true);
/*      */         }
/*      */         else
/* 1087 */           OutlookAttrDlg.this.filledBtn.setEnabled(false);
/*      */       }
/*      */     });
/* 1094 */     this.filledBtn = new Button(fillGrp, 32);
/* 1095 */     this.filledBtn.setText("Filled");
/*      */   }
/*      */ 
/*      */   public int open()
/*      */   {
/* 1102 */     create();
/*      */ 
/* 1106 */     if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 1106 */       .equalsIgnoreCase("MultiSelect")) {
/* 1107 */       enableChkBoxes(true);
/* 1108 */       enableAllWidgets(false);
/*      */     }
/* 1111 */     else if (this.chkBox != null) {
/* 1112 */       enableChkBoxes(false);
/*      */     }
/*      */ 
/* 1117 */     if (this.prevType == null) {
/* 1118 */       this.outlookCombo.select(0);
/*      */     }
/*      */     else {
/* 1121 */       int idx = this.outlookCombo.indexOf(this.prevType);
/* 1122 */       if (idx >= 0) this.outlookCombo.select(idx); else {
/* 1123 */         this.outlookCombo.select(0);
/*      */       }
/* 1125 */       setDefaultLabels(this.prevType);
/*      */     }
/*      */ 
/* 1129 */     if (this.prevLbl == null) {
/* 1130 */       this.txtCombo.select(0);
/*      */     }
/*      */     else {
/* 1133 */       int idx = this.txtCombo.indexOf(this.prevLbl);
/* 1134 */       if (idx >= 0) this.txtCombo.select(idx); else {
/* 1135 */         this.txtCombo.select(0);
/*      */       }
/*      */     }
/*      */ 
/* 1139 */     setDefaultLineAttr(this.outlookCombo.getText() + this.txtCombo.getText());
/*      */ 
/* 1141 */     return super.open();
/*      */   }
/*      */ 
/*      */   private void enableChkBoxes(boolean flag)
/*      */   {
/* 1150 */     if (!flag) {
/* 1151 */       setAllChkBoxes();
/*      */     }
/* 1153 */     for (ChkBox chk : ChkBox.values())
/* 1154 */       this.chkBox[chk.ordinal()].setVisible(flag);
/*      */   }
/*      */ 
/*      */   private void enableAllWidgets(boolean flag)
/*      */   {
/* 1165 */     this.colorLbl.setEnabled(flag);
/*      */ 
/* 1167 */     this.widthLbl.setEnabled(flag);
/* 1168 */     this.widthSlider.setEnabled(flag);
/*      */ 
/* 1170 */     this.smoothLbl.setEnabled(flag);
/* 1171 */     this.smoothLvlCbo.setEnabled(flag);
/*      */ 
/* 1173 */     this.filledBtn.setEnabled(flag);
/* 1174 */     this.closedBtn.setEnabled(flag);
/*      */   }
/*      */ 
/*      */   private void setAllChkBoxes()
/*      */   {
/* 1183 */     for (ChkBox chk : ChkBox.values())
/* 1184 */       this.chkBox[chk.ordinal()].setSelection(true);
/*      */   }
/*      */ 
/*      */   public boolean addLabel()
/*      */   {
/* 1192 */     return this.lblBtn.getSelection();
/*      */   }
/*      */ 
/*      */   public boolean addText()
/*      */   {
/* 1199 */     return this.txtBtn.getSelection();
/*      */   }
/*      */ 
/*      */   public boolean addSymbol()
/*      */   {
/* 1206 */     return this.symbolBtn.getSelection();
/*      */   }
/*      */ 
/*      */   public boolean useLineColor()
/*      */   {
/* 1213 */     return this.lnColorBtn.getSelection();
/*      */   }
/*      */ 
/*      */   public String getLblTxt()
/*      */   {
/* 1220 */     return this.txtCombo.getText();
/*      */   }
/*      */ 
/*      */   public String getSymbolType()
/*      */   {
/* 1227 */     return this.symbolCombo.getSelectedText();
/*      */   }
/*      */ 
/*      */   public String getSymbolCat()
/*      */   {
/* 1234 */     if (getSymbolType().contains("|")) {
/* 1235 */       return "Combo";
/*      */     }
/*      */ 
/* 1238 */     return "Symbol";
/*      */   }
/*      */ 
/*      */   public String getOutlookType()
/*      */   {
/* 1246 */     return this.outlookCombo.getText();
/*      */   }
/*      */ 
/*      */   public void enableAddDel(boolean flag)
/*      */   {
/* 1254 */     this.addLineBtn.setEnabled(flag);
/* 1255 */     this.delLineBtn.setEnabled(flag);
/*      */   }
/*      */ 
/*      */   public void showContLines(Outlook ol)
/*      */   {
/* 1263 */     Iterator it = ol.getComponentIterator();
/* 1264 */     DECollection vLines = new DECollection("OutlookVirtualLines");
/*      */ 
/* 1267 */     while (it.hasNext()) {
/* 1268 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 1269 */       if (adc.getName().equalsIgnoreCase(Outlook.OUTLOOK_LINE_GROUP))
/*      */       {
/* 1272 */         ArrayList lns = new ArrayList();
/*      */ 
/* 1274 */         Iterator itDe = adc.createDEIterator();
/* 1275 */         while (itDe.hasNext()) {
/* 1276 */           DrawableElement dElem = (DrawableElement)itDe.next();
/* 1277 */           if ((dElem instanceof Line)) {
/* 1278 */             lns.add((Line)dElem);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1283 */         for (int ii = 0; ii < lns.size() - 1; ii++) {
/* 1284 */           ArrayList pts = new ArrayList();
/* 1285 */           pts.add((Coordinate)((Line)lns.get(ii)).getPoints().get(((Line)lns.get(ii)).getPoints().size() - 1));
/* 1286 */           pts.add((Coordinate)((Line)lns.get(ii + 1)).getPoints().get(0));
/*      */ 
/* 1288 */           Line vLn = new Line(null, ((Line)lns.get(0)).getColors(), 
/* 1289 */             ((Line)lns.get(0)).getLineWidth(), ((Line)lns.get(0)).getSizeScale(), 
/* 1290 */             false, false, pts, 
/* 1291 */             ((Line)lns.get(0)).getSmoothFactor(), 
/* 1292 */             null, "Lines", "LINE_DASHED_4");
/*      */ 
/* 1294 */           vLines.add(vLn);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1302 */     if (!vLines.isEmpty()) {
/* 1303 */       this.drawingLayer.setGhostLine(vLines);
/* 1304 */       this.mapEditor.refresh();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Document readOutlookTbl()
/*      */   {
/* 1314 */     if (outlookTbl == null) {
/*      */       try {
/* 1316 */         String outlookTypeFile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/* 1317 */           PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "outlooktype.xml");
/*      */ 
/* 1319 */         SAXReader reader = new SAXReader();
/* 1320 */         outlookTbl = reader.read(outlookTypeFile);
/*      */       } catch (Exception e) {
/* 1322 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/* 1326 */     return outlookTbl;
/*      */   }
/*      */ 
/*      */   private List<String> getOutlookTypes()
/*      */   {
/* 1334 */     List otlkType = new ArrayList();
/* 1335 */     Document doc = readOutlookTbl();
/* 1336 */     if (doc == null) {
/* 1337 */       return otlkType;
/*      */     }
/* 1339 */     List nodes = readOutlookTbl().selectNodes(OTLK_XPATH);
/* 1340 */     for (Node node : nodes) {
/* 1341 */       otlkType.add(node.valueOf("@name"));
/*      */     }
/*      */ 
/* 1344 */     return otlkType;
/*      */   }
/*      */ 
/*      */   private List<String> getLabelsForType(String type)
/*      */   {
/* 1354 */     List lbls = new ArrayList();
/* 1355 */     String xpath = OTLK_XPATH + "[@name='" + type.toUpperCase() + "']";
/*      */ 
/* 1357 */     Node otlkType = readOutlookTbl().selectSingleNode(xpath);
/* 1358 */     List nodes = otlkType.selectNodes("label");
/* 1359 */     for (Node node : nodes) {
/* 1360 */       lbls.add(node.valueOf("@name"));
/*      */     }
/*      */ 
/* 1363 */     return lbls;
/*      */   }
/*      */ 
/*      */   public String getTextForLabel(String outlookType, String label)
/*      */   {
/* 1373 */     if ((label == null) || (label.isEmpty())) return "";
/*      */ 
/* 1375 */     String ret = "";
/* 1376 */     String xpath = OTLK_XPATH + "[@name='" + outlookType.toUpperCase() + "']";
/*      */ 
/* 1378 */     Node otlkType = readOutlookTbl().selectSingleNode(xpath);
/* 1379 */     List nodes = otlkType.selectNodes("label");
/*      */ 
/* 1381 */     for (Node node : nodes) {
/* 1382 */       if (label.equals(node.valueOf("@name"))) {
/* 1383 */         ret = node.valueOf("@text");
/* 1384 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1388 */     if (ret.isEmpty()) ret = label;
/*      */ 
/* 1390 */     return ret;
/*      */   }
/*      */ 
/*      */   public String getCatmapForType(String type)
/*      */   {
/* 1399 */     StringBuilder cmap = new StringBuilder("");
/*      */ 
/* 1401 */     String xpath = OTLK_XPATH + "[@name='" + type.toUpperCase() + "']";
/*      */ 
/* 1403 */     boolean allNumbers = true;
/* 1404 */     Node otlkType = readOutlookTbl().selectSingleNode(xpath);
/* 1405 */     if (otlkType != null) {
/* 1406 */       List nodes = otlkType.selectNodes("label");
/*      */ 
/* 1408 */       for (Node node : nodes) {
/* 1409 */         String text = node.valueOf("@name");
/* 1410 */         cmap.append(text);
/* 1411 */         cmap.append("=");
/*      */ 
/* 1413 */         String value = node.valueOf("@value");
/*      */ 
/* 1415 */         StringBuilder cb = new StringBuilder("");
/* 1416 */         if ((value == null) || (value.length() == 0)) {
/* 1417 */           for (int ii = 0; ii < text.length(); ii++) {
/* 1418 */             char ch = text.charAt(ii);
/* 1419 */             if ((ch == '.') || ((ch >= '0') && (ch <= '9'))) {
/* 1420 */               cb.append(ch);
/*      */             }
/*      */             else {
/* 1423 */               allNumbers = false;
/*      */             }
/*      */           }
/*      */ 
/* 1427 */           value = cb.toString();
/*      */         }
/*      */         else {
/* 1430 */           allNumbers = false;
/*      */         }
/*      */ 
/* 1433 */         cmap.append(value);
/* 1434 */         cmap.append(";");
/*      */       }
/*      */     }
/*      */ 
/* 1438 */     if (allNumbers) {
/* 1439 */       return new String("");
/*      */     }
/*      */ 
/* 1442 */     return cmap.toString();
/*      */   }
/*      */ 
/*      */   private void setDefaultLabels(String otlkType)
/*      */   {
/* 1451 */     List lbls = getLabelsForType(otlkType);
/* 1452 */     if (!lbls.isEmpty()) {
/* 1453 */       this.txtCombo.removeAll();
/* 1454 */       for (String str : lbls)
/* 1455 */         this.txtCombo.add(str);
/*      */     }
/*      */     else
/*      */     {
/* 1459 */       this.txtCombo.add("2%");
/* 1460 */       this.txtCombo.add("5%");
/* 1461 */       this.txtCombo.add("10%");
/*      */     }
/*      */ 
/* 1464 */     this.txtCombo.add("Other");
/*      */ 
/* 1467 */     if (this.prevLbl == null) {
/* 1468 */       this.txtCombo.select(0);
/*      */     }
/*      */     else {
/* 1471 */       int idx = this.txtCombo.indexOf(this.prevLbl);
/* 1472 */       if (idx >= 0) this.txtCombo.select(idx); else
/* 1473 */         this.txtCombo.select(0);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setOtlkType(String type)
/*      */   {
/* 1482 */     int idx = this.outlookCombo.indexOf(type.toUpperCase());
/* 1483 */     if (idx >= 0) {
/* 1484 */       this.outlookCombo.select(this.outlookCombo.indexOf(type.toUpperCase()));
/* 1485 */       setDefaultLabels(getOutlookType());
/* 1486 */       setDefaultLineAttr(this.outlookCombo.getText() + this.txtCombo.getText());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setLabel(String lbl)
/*      */   {
/* 1495 */     int idx = this.txtCombo.indexOf(lbl);
/* 1496 */     if (idx >= 0) {
/* 1497 */       this.txtCombo.select(idx);
/* 1498 */       this.prevLbl = lbl;
/*      */     }
/*      */ 
/* 1501 */     String grpType = this.outlookCombo.getText();
/* 1502 */     setDefaultLineAttr(grpType + this.txtCombo.getText());
/*      */ 
/* 1505 */     this.showGrid = showMakeGrid(grpType);
/* 1506 */     this.flagLabel = showLabel(grpType);
/* 1507 */     this.flagAction = showAction(grpType);
/*      */ 
/* 1509 */     this.infoBtn.setEnabled(this.showGrid);
/* 1510 */     this.makeGridBtn.setEnabled(this.showGrid);
/* 1511 */     this.lblBtn.setEnabled(this.flagLabel);
/* 1512 */     this.txtBtn.setEnabled(this.flagLabel);
/* 1513 */     this.symbolBtn.setEnabled(this.flagLabel);
/* 1514 */     this.lnColorBtn.setEnabled(this.flagLabel);
/* 1515 */     this.addLineBtn.setEnabled(this.flagAction);
/* 1516 */     this.delLineBtn.setEnabled(this.flagAction);
/* 1517 */     this.setContBtn.setEnabled(this.flagAction);
/* 1518 */     this.showContBtn.setEnabled(this.flagAction);
/* 1519 */     this.fmtBtn.setEnabled(this.flagAction);
/*      */   }
/*      */ 
/*      */   private static void loadSettings()
/*      */   {
/* 1528 */     settings = new HashMap();
/*      */ 
/* 1530 */     String settingFile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/* 1531 */       PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "outlooksettings.xml");
/*      */ 
/* 1533 */     Products products = FileTools.read(settingFile);
/*      */ 
/* 1535 */     if (products != null)
/*      */     {
/* 1538 */       List prds = ProductConverter.convert(products);
/*      */       Iterator localIterator2;
/* 1540 */       for (Iterator localIterator1 = prds.iterator(); localIterator1.hasNext(); 
/* 1542 */         localIterator2.hasNext())
/*      */       {
/* 1540 */         Product p = (Product)localIterator1.next();
/*      */ 
/* 1542 */         localIterator2 = p.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/*      */ 
/* 1544 */         Iterator it = layer.getComponentIterator();
/* 1545 */         while (it.hasNext()) {
/* 1546 */           AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 1547 */           if (adc.getName().equalsIgnoreCase("OUTLOOK")) {
/* 1548 */             Iterator itLn = ((Outlook)adc).getComponentIterator();
/* 1549 */             while (itLn.hasNext()) {
/* 1550 */               AbstractDrawableComponent lnGrp = (AbstractDrawableComponent)itLn.next();
/* 1551 */               if (lnGrp.getName().equalsIgnoreCase(Outlook.OUTLOOK_LABELED_LINE)) {
/* 1552 */                 String key = null;
/* 1553 */                 Line ln = null;
/* 1554 */                 Iterator itDe = ((DECollection)lnGrp).createDEIterator();
/* 1555 */                 while (itDe.hasNext()) {
/* 1556 */                   DrawableElement de = (DrawableElement)itDe.next();
/* 1557 */                   if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text)) {
/* 1558 */                     key = ((Outlook)adc).getOutlookType() + ((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getText()[0];
/*      */                   }
/* 1560 */                   else if ((de instanceof Line)) {
/* 1561 */                     ln = (Line)de;
/*      */                   }
/*      */                 }
/*      */ 
/* 1565 */                 if ((key != null) && (ln != null))
/* 1566 */                   settings.put(key, ln);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setDefaultLineAttr(String key)
/*      */   {
/* 1586 */     if (settings != null) {
/* 1587 */       Line ln = (Line)settings.get(key);
/* 1588 */       if (ln != null) {
/* 1589 */         setAttrForDlg(ln);
/* 1590 */         this.lineType = ln.getPgenType();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getLineType() {
/* 1596 */     return this.lineType;
/*      */   }
/*      */ 
/*      */   private void openG2GDlg()
/*      */   {
/* 1604 */     if (g2gDlg == null) {
/*      */       try
/*      */       {
/* 1607 */         g2gDlg = new GraphToGridParamDialog(
/* 1608 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/* 1609 */         g2gDlg.setCntAttrDlg(this);
/*      */       }
/*      */       catch (VizException e)
/*      */       {
/* 1613 */         e.printStackTrace();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1618 */     if (g2gDlg != null)
/* 1619 */       g2gDlg.open();
/*      */   }
/*      */ 
/*      */   public boolean close()
/*      */   {
/* 1630 */     if (g2gDlg != null) g2gDlg.close();
/*      */ 
/* 1632 */     return super.close();
/*      */   }
/*      */ 
/*      */   public Outlook getCurrentOtlk()
/*      */   {
/* 1642 */     Outlook ol = null;
/*      */ 
/* 1644 */     String type = getOutlookType();
/*      */ 
/* 1646 */     Iterator it = this.drawingLayer.getActiveLayer().getComponentIterator();
/* 1647 */     while (it.hasNext()) {
/* 1648 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 1649 */       if (((adc instanceof Outlook)) && (adc.getPgenType().equalsIgnoreCase(type))) {
/* 1650 */         ol = (Outlook)adc;
/* 1651 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1655 */     return ol;
/*      */   }
/*      */ 
/*      */   private void openContourInfoDlg()
/*      */   {
/* 1663 */     if (contoursInfoDlg == null) {
/* 1664 */       contoursInfoDlg = new ContoursInfoDlg(getShell());
/* 1665 */       contoursInfoDlg.setContoursAttrDlg(this);
/*      */     }
/*      */ 
/* 1668 */     contoursInfoDlg.open();
/*      */   }
/*      */ 
/*      */   public void setAttributes(IContours attr)
/*      */   {
/* 1677 */     setParm(attr.getParm());
/* 1678 */     setLevel(attr.getLevel());
/* 1679 */     setTime1(attr.getTime1());
/* 1680 */     setTime2(attr.getTime2());
/* 1681 */     setCint(attr.getCint());
/*      */ 
/* 1683 */     setInfoBtnText();
/*      */   }
/*      */ 
/*      */   private void setInfoBtnText()
/*      */   {
/* 1692 */     if ((this.contourParm == null) || (this.contourLevel == null) || 
/* 1693 */       (this.contourTime1 == null)) return;
/*      */ 
/* 1695 */     String str = this.contourParm + ", " + this.contourLevel + "\n" + 
/* 1696 */       this.contourTime1.get(1) + "-" + (
/* 1697 */       this.contourTime1.get(2) + 1) + "-" + 
/* 1698 */       this.contourTime1.get(5) + "  " + 
/* 1699 */       this.contourTime1.get(11) + ":" + 
/* 1700 */       this.contourTime1.get(12) + "Z";
/*      */ 
/* 1702 */     this.infoBtn.setText(str);
/*      */   }
/*      */ 
/*      */   public void okPressed()
/*      */   {
/* 1711 */     DrawableElement de = this.drawingLayer.getSelectedDE();
/* 1712 */     DECollection parentAdc = null;
/*      */ 
/* 1715 */     if ((de != null) && ((de.getParent() instanceof DECollection))) {
/* 1716 */       parentAdc = (DECollection)de.getParent();
/*      */     }
/*      */ 
/* 1723 */     if ((parentAdc != null) && ((parentAdc.getParent() instanceof Outlook)))
/*      */     {
/* 1725 */       DrawableElement newEl = (DrawableElement)de.copy();
/*      */ 
/* 1727 */       Outlook oldOutlook = (Outlook)de.getParent().getParent();
/*      */ 
/* 1729 */       Outlook newOutlook = oldOutlook.copy();
/* 1730 */       newOutlook.clear();
/*      */ 
/* 1732 */       Iterator iterator = oldOutlook.getComponentIterator();
/*      */ 
/* 1734 */       while (iterator.hasNext())
/*      */       {
/* 1736 */         AbstractDrawableComponent oadc = (AbstractDrawableComponent)iterator.next();
/* 1737 */         AbstractDrawableComponent nadc = oadc.copy();
/* 1738 */         nadc.setParent(newOutlook);
/*      */ 
/* 1740 */         if (oadc.equals(parentAdc))
/*      */         {
/* 1742 */           Iterator deit = parentAdc.createDEIterator();
/* 1743 */           ((DECollection)nadc).clear();
/*      */ 
/* 1745 */           while (deit.hasNext())
/*      */           {
/* 1747 */             DrawableElement ode = (DrawableElement)deit.next();
/* 1748 */             AbstractDrawableComponent nde = ode.copy();
/* 1749 */             nde.setParent(nadc);
/*      */ 
/* 1751 */             if ((nde instanceof Line)) {
/* 1752 */               ((Line)nde).update(this);
/* 1753 */               newEl = (DrawableElement)nde;
/*      */             }
/* 1755 */             else if ((nde instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text)) {
/* 1756 */               ((gov.noaa.nws.ncep.ui.pgen.elements.Text)nde).setText(new String[] { getLblTxt() });
/*      */ 
/* 1758 */               if (useLineColor()) {
/* 1759 */                 ((gov.noaa.nws.ncep.ui.pgen.elements.Text)nde).setColors(getColors());
/*      */               }
/*      */             }
/* 1762 */             else if ((nde instanceof Symbol)) {
/* 1763 */               ((Symbol)nde).setPgenCategory(getSymbolCat());
/* 1764 */               ((Symbol)nde).setPgenType(getSymbolType());
/* 1765 */               if (useLineColor()) {
/* 1766 */                 ((Symbol)nde).setColors(getColors());
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/* 1771 */             ((DECollection)nadc).addElement(nde);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1776 */         newOutlook.addElement(nadc);
/*      */       }
/*      */ 
/* 1783 */       newOutlook.update(this);
/* 1784 */       newOutlook.reorderLines();
/* 1785 */       newOutlook.setOutlookType(getOutlookType());
/*      */ 
/* 1787 */       this.drawingLayer.replaceElement(oldOutlook, newOutlook);
/*      */ 
/* 1792 */       this.drawingLayer.removeSelected();
/* 1793 */       this.drawingLayer.setSelected(newEl);
/*      */     }
/*      */ 
/* 1798 */     if (this.mapEditor != null)
/* 1799 */       this.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   public String getPatternName()
/*      */   {
/* 1807 */     return null;
/*      */   }
/*      */ 
/*      */   public Coordinate[] getLinePoints()
/*      */   {
/* 1813 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean showMakeGrid(String type)
/*      */   {
/* 1822 */     boolean ret = true;
/* 1823 */     if (type != null) {
/* 1824 */       type = type.toUpperCase();
/* 1825 */       String xpath = OTLK_XPATH + "[@name='" + type + "']";
/* 1826 */       String makegrid = readOutlookTbl().selectSingleNode(xpath).valueOf("@makegrid");
/* 1827 */       if ((makegrid != null) && (!makegrid.isEmpty()) && (makegrid.equalsIgnoreCase("false"))) {
/* 1828 */         ret = false;
/*      */       }
/*      */     }
/*      */ 
/* 1832 */     return ret;
/*      */   }
/*      */ 
/*      */   private boolean showLabel(String type)
/*      */   {
/* 1841 */     boolean ret = true;
/* 1842 */     if (type != null) {
/* 1843 */       type = type.toUpperCase();
/* 1844 */       String xpath = OTLK_XPATH + "[@name='" + type + "']";
/* 1845 */       String makegrid = readOutlookTbl().selectSingleNode(xpath).valueOf("@flagLabel");
/* 1846 */       if ((makegrid != null) && (!makegrid.isEmpty()) && (makegrid.equalsIgnoreCase("false"))) {
/* 1847 */         ret = false;
/*      */       }
/*      */     }
/*      */ 
/* 1851 */     return ret;
/*      */   }
/*      */ 
/*      */   private boolean showAction(String type)
/*      */   {
/* 1860 */     boolean ret = true;
/* 1861 */     if (type != null) {
/* 1862 */       type = type.toUpperCase();
/* 1863 */       String xpath = OTLK_XPATH + "[@name='" + type + "']";
/* 1864 */       String makegrid = readOutlookTbl().selectSingleNode(xpath).valueOf("@flagAction");
/* 1865 */       if ((makegrid != null) && (!makegrid.isEmpty()) && (makegrid.equalsIgnoreCase("false"))) {
/* 1866 */         ret = false;
/*      */       }
/*      */     }
/*      */ 
/* 1870 */     return ret;
/*      */   }
/*      */ 
/*      */   private boolean setFromLineFlag(String type)
/*      */   {
/* 1879 */     boolean ret = false;
/* 1880 */     if (type != null) {
/* 1881 */       type = type.toUpperCase();
/* 1882 */       String xpath = OTLK_XPATH + "[@name='" + type + "']";
/* 1883 */       String makegrid = readOutlookTbl().selectSingleNode(xpath).valueOf("@fromline");
/* 1884 */       if ((makegrid != null) && (!makegrid.isEmpty()) && (makegrid.equalsIgnoreCase("true"))) {
/* 1885 */         ret = true;
/*      */       }
/*      */     }
/*      */ 
/* 1889 */     return ret;
/*      */   }
/*      */ 
/*      */   public boolean fromLineFlag()
/*      */   {
/* 1894 */     return this.needFromLine;
/*      */   }
/*      */ 
/*      */   private Outlook findOtlk()
/*      */   {
/* 1905 */     Outlook otlk = null;
/*      */ 
/* 1924 */     Iterator it = this.drawingLayer.getActiveLayer().getComponentIterator();
/* 1925 */     while (it.hasNext()) {
/* 1926 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 1927 */       if (((adc instanceof Outlook)) && (((Outlook)adc).getOutlookType().equalsIgnoreCase(getOutlookType()))) {
/* 1928 */         otlk = (Outlook)adc;
/* 1929 */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1934 */     return otlk;
/*      */   }
/*      */ 
/*      */   private static enum ChkBox
/*      */   {
/*   93 */     COLOR, WIDTH, SMOOTH, CLOSE, FILL;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.OutlookAttrDlg
 * JD-Core Version:    0.6.2
 */