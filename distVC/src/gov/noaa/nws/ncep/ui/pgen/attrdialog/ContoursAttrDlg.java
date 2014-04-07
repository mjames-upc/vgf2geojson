/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.raytheon.viz.ui.perspectives.AbstractVizPerspectiveManager;
/*      */ import com.raytheon.viz.ui.perspectives.VizPerspectiveListener;
/*      */ import com.raytheon.viz.ui.tools.ModalToolManager;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.gempak.parameters.core.contourinterval.CINT;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourCircle;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourMinmax;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.IContours;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenCommandManager;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*      */ import gov.noaa.nws.ncep.ui.pgen.graphtogrid.GraphToGridParamDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.AbstractPgenTool;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenContoursTool;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenContoursTool.PgenContoursHandler;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenSelectHandler;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import org.dom4j.Document;
/*      */ import org.dom4j.Node;
/*      */ import org.eclipse.core.runtime.IConfigurationElement;
/*      */ import org.eclipse.jface.dialogs.Dialog;
/*      */ import org.eclipse.swt.events.FocusEvent;
/*      */ import org.eclipse.swt.events.FocusListener;
/*      */ import org.eclipse.swt.events.SelectionAdapter;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.events.SelectionListener;
/*      */ import org.eclipse.swt.graphics.Image;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Listener;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Slider;
/*      */ import org.eclipse.swt.widgets.Spinner;
/*      */ import org.eclipse.swt.widgets.Widget;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class ContoursAttrDlg extends AttrDlg
/*      */   implements IContours, SelectionListener, ILine
/*      */ {
/*  111 */   private static ContoursAttrDlg INSTANCE = null;
/*      */ 
/*  113 */   private static ContoursInfoDlg contoursInfoDlg = null;
/*  114 */   private static GraphToGridParamDialog g2gDlg = null;
/*      */ 
/*  123 */   private ContourDrawingStatus drawingStatus = ContourDrawingStatus.DRAW_LINE;
/*      */ 
/*  125 */   private String contourParm = "HGMT";
/*      */ 
/*  127 */   private String contourLevel = "1000";
/*      */ 
/*  129 */   private String contourFcstHr = "f000";
/*      */ 
/*  131 */   private Calendar contourTime1 = Calendar.getInstance();
/*  132 */   private Calendar contourTime2 = Calendar.getInstance();
/*  133 */   private String contourCint = "10/0/100";
/*      */ 
/*  135 */   private final String SELECT_CONTOURLINE = "Select";
/*  136 */   private final String ADD_CONTOURLINE = "Add";
/*  137 */   private final String DELETE_CONTOURLINE = "Delete";
/*      */ 
/*  139 */   private final int MAX_QUICK_SYMBOLS = 15;
/*  140 */   private int numOfQuickSymbols = 2;
/*      */   private Composite top;
/*      */   private Group attrComp;
/*      */   private Group textGrp;
/*  146 */   private Composite infoComp = null;
/*  147 */   private Button infoBtn = null;
/*  148 */   private org.eclipse.swt.widgets.Text labelTxt = null;
/*  149 */   private Button lineClosedBtn = null;
/*  150 */   private Button lineTypeBtn = null;
/*      */ 
/*  152 */   private Composite labelGrp = null;
/*  153 */   private ArrayList<Button> labelBtns = null;
/*  154 */   private Spinner labelNumSpinner = null;
/*      */ 
/*  156 */   private List<Button> quickSymbolBtns = null;
/*  157 */   private Button activeQuickSymbolBtn = null;
/*      */ 
/*  159 */   private Button applyAllLineBtn = null;
/*  160 */   private Button applyAllLabelBtn = null;
/*  161 */   private Button applyAllSymbolBtn = null;
/*  162 */   private Button applyAllCircleBtn = null;
/*      */ 
/*  164 */   private Button circleTypeBtn = null;
/*  165 */   private Button hideCircleLabelBtn = null;
/*      */ 
/*  167 */   private Button selectLineBtn = null;
/*  168 */   private Button deleteLineBtn = null;
/*      */ 
/*  170 */   private Contours currentContours = null;
/*      */ 
/*  172 */   private LinkedHashMap<String, String> lineIconType = null;
/*  173 */   private LinkedHashMap<String, IConfigurationElement> symbolItemMap = null;
/*  174 */   private LinkedHashMap<String, Boolean> quickSymbolType = null;
/*  175 */   private LineTypeSelectionDlg lineTypePanel = null;
/*  176 */   private SymbolTypeSelectionDlg symbolTypePanel = null;
/*  177 */   private final int SYMBOLCOL = 16;
/*      */ 
/*  179 */   private boolean drawClosedLine = false;
/*      */ 
/*  184 */   private final java.awt.Color defaultButtonColor = java.awt.Color.lightGray;
/*  185 */   private final java.awt.Color activeButtonColor = java.awt.Color.green;
/*      */   private ContourLineAttrDlg lineAttrDlg;
/*  195 */   private Line lineTemplate = null;
/*      */   private LabelAttrDlg labelAttrDlg;
/*  205 */   private gov.noaa.nws.ncep.ui.pgen.elements.Text labelTemplate = null;
/*      */   private ContourMinmaxAttrDlg minmaxAttrDlg;
/*  215 */   private Symbol minmaxTemplate = null;
/*      */   private ContourCircleAttrDlg circleAttrDlg;
/*  225 */   private Arc circleTemplate = null;
/*      */ 
/*  230 */   private HashMap<String, AbstractDrawableComponent> contoursAttrSettings = null;
/*      */   private PgenContoursTool tool;
/*      */ 
/*      */   private ContoursAttrDlg(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  241 */     super(parShell);
/*      */ 
/*  243 */     retrieveIconType();
/*      */ 
/*  245 */     if (this.quickSymbolType == null) {
/*  246 */       this.quickSymbolType = getQuickSymbols();
/*      */     }
/*      */ 
/*  249 */     if (this.quickSymbolBtns == null) {
/*  250 */       this.quickSymbolBtns = new ArrayList();
/*      */     }
/*      */ 
/*  253 */     retrieveContoursSettings();
/*      */   }
/*      */ 
/*      */   public static ContoursAttrDlg getInstance(Shell parShell)
/*      */   {
/*  266 */     if (INSTANCE == null)
/*      */     {
/*      */       try
/*      */       {
/*  270 */         INSTANCE = new ContoursAttrDlg(parShell);
/*      */       }
/*      */       catch (VizException e)
/*      */       {
/*  274 */         e.printStackTrace();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  279 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  290 */     this.top = ((Composite)super.createDialogArea(parent));
/*  291 */     getShell().setText("Contours Attributes");
/*      */ 
/*  294 */     GridLayout mainLayout = new GridLayout(1, false);
/*  295 */     mainLayout.marginHeight = 1;
/*  296 */     mainLayout.marginWidth = 1;
/*  297 */     mainLayout.verticalSpacing = 0;
/*  298 */     this.top.setLayout(mainLayout);
/*      */ 
/*  301 */     this.infoComp = new Composite(this.top, 0);
/*  302 */     this.infoComp.setLayoutData(new GridData(16777216, -1, true, false));
/*      */ 
/*  304 */     GridLayout layout = new GridLayout(2, false);
/*  305 */     layout.horizontalSpacing = 15;
/*  306 */     layout.marginHeight = 1;
/*  307 */     layout.marginWidth = 1;
/*  308 */     layout.horizontalSpacing = 1;
/*  309 */     this.infoComp.setLayout(layout);
/*      */ 
/*  311 */     this.infoBtn = new Button(this.infoComp, 8);
/*  312 */     this.infoBtn.setToolTipText("Bring up the contours info attribute dialog");
/*  313 */     setInfoBtnText();
/*      */ 
/*  315 */     this.infoBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  317 */         ContoursAttrDlg.this.openContourInfoDlg();
/*      */       }
/*      */     });
/*  322 */     Button makeGridBtn = new Button(this.infoComp, 8);
/*  323 */     makeGridBtn.setText("Make Grid");
/*  324 */     makeGridBtn.setToolTipText("Generate grid from this Contours");
/*  325 */     makeGridBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  327 */         ContoursAttrDlg.this.openG2GDlg();
/*      */       }
/*      */     });
/*  331 */     addSeparator(this.top);
/*      */ 
/*  336 */     this.attrComp = new Group(this.top, 64);
/*  337 */     GridLayout layout1 = new GridLayout(1, false);
/*  338 */     layout1.marginHeight = 1;
/*  339 */     layout1.marginWidth = 1;
/*  340 */     layout1.verticalSpacing = 0;
/*  341 */     this.attrComp.setLayout(layout1);
/*      */ 
/*  346 */     Composite lineComp = new Composite(this.attrComp, 0);
/*  347 */     GridLayout layout2 = new GridLayout(2, false);
/*  348 */     layout2.horizontalSpacing = 1;
/*  349 */     layout2.marginWidth = 1;
/*  350 */     layout2.marginHeight = 1;
/*  351 */     layout2.verticalSpacing = 1;
/*  352 */     lineComp.setLayout(layout2);
/*      */ 
/*  354 */     Composite closelineComp = new Composite(lineComp, 0);
/*  355 */     GridLayout closelineCompGl = new GridLayout(3, false);
/*  356 */     closelineCompGl.verticalSpacing = 0;
/*  357 */     closelineCompGl.marginHeight = 0;
/*  358 */     closelineCompGl.marginWidth = 1;
/*  359 */     closelineComp.setLayout(closelineCompGl);
/*      */ 
/*  361 */     this.lineTypeBtn = new Button(closelineComp, 8);
/*  362 */     this.lineTypeBtn.setToolTipText("Click to select a line type");
/*  363 */     String ltype = retrieveDefaultLineType();
/*      */ 
/*  365 */     if (this.lineTemplate == null) {
/*  366 */       this.lineTemplate = ((Line)this.contoursAttrSettings.get(ltype));
/*  367 */       this.lineTemplate.setPgenType(ltype);
/*      */     }
/*      */ 
/*  370 */     setButtonColor(this.lineTypeBtn, this.defaultButtonColor, this.lineTemplate.getColors()[0]);
/*  371 */     this.lineTypeBtn.setImage(getIcon(ltype));
/*  372 */     this.lineTypeBtn.setData(ltype);
/*      */ 
/*  375 */     this.lineTypeBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  377 */         if (!ContoursAttrDlg.this.drawContourLine()) {
/*  378 */           if (ContoursAttrDlg.this.lineTemplate == null) {
/*  379 */             ContoursAttrDlg.this.lineTemplate = ((Line)ContoursAttrDlg.this.contoursAttrSettings.get(ContoursAttrDlg.this.retrieveDefaultLineType()));
/*  380 */             ContoursAttrDlg.this.lineTemplate.setPgenType(ContoursAttrDlg.this.retrieveDefaultLineType());
/*      */           }
/*  382 */           ContoursAttrDlg.this.setButtonColor(ContoursAttrDlg.this.lineTypeBtn, ContoursAttrDlg.this.defaultButtonColor, ContoursAttrDlg.this.lineTemplate.getColors()[0]);
/*      */ 
/*  384 */           ContoursAttrDlg.this.setDrawingStatus(ContoursAttrDlg.ContourDrawingStatus.DRAW_LINE);
/*      */         }
/*      */         else {
/*  387 */           ContoursAttrDlg.this.openLineTypePanel();
/*      */         }
/*      */       }
/*      */     });
/*  392 */     this.lineClosedBtn = new Button(closelineComp, 32);
/*  393 */     this.lineClosedBtn.setText("Closed");
/*  394 */     this.lineClosedBtn.setToolTipText("Click to draw a closed line");
/*  395 */     this.lineClosedBtn.setSelection(this.drawClosedLine);
/*  396 */     this.lineClosedBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  398 */         ContoursAttrDlg.this.drawClosedLine = ContoursAttrDlg.this.lineClosedBtn.getSelection();
/*      */       }
/*      */     });
/*  402 */     Composite editlineComp = new Composite(lineComp, 0);
/*  403 */     GridLayout editLineGl = new GridLayout(2, false);
/*  404 */     editLineGl.horizontalSpacing = 1;
/*  405 */     editLineGl.marginWidth = 1;
/*  406 */     editLineGl.verticalSpacing = 0;
/*  407 */     editLineGl.marginHeight = 0;
/*  408 */     editlineComp.setLayout(editLineGl);
/*      */ 
/*  411 */     Button lineAttrBtn = new Button(editlineComp, 8);
/*  412 */     lineAttrBtn.setText("Edit");
/*  413 */     lineAttrBtn.setToolTipText("Edit contour's line attributes");
/*  414 */     lineAttrBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */         try {
/*  419 */           if (ContoursAttrDlg.this.lineAttrDlg == null) {
/*  420 */             ContoursAttrDlg.this.lineAttrDlg = new ContoursAttrDlg.ContourLineAttrDlg(ContoursAttrDlg.this, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), null);
/*      */           }
/*      */ 
/*  423 */           ContoursAttrDlg.this.openAttrDlg(ContoursAttrDlg.this.lineAttrDlg);
/*  424 */           ContoursAttrDlg.ContourLineAttrDlg.access$1(ContoursAttrDlg.this.lineAttrDlg);
/*      */ 
/*  427 */           DrawableElement de = ContoursAttrDlg.this.drawingLayer.getSelectedDE();
/*      */ 
/*  429 */           if ((de != null) && (de.getParent() != null) && ((de.getParent() instanceof ContourLine))) {
/*  430 */             ContourLine pde = (ContourLine)de.getParent();
/*  431 */             ContoursAttrDlg.this.lineAttrDlg.setAttrForDlg(pde.getLine());
/*      */           }
/*      */           else
/*      */           {
/*  435 */             if (ContoursAttrDlg.this.lineTemplate == null) {
/*  436 */               ContoursAttrDlg.this.lineTemplate = ((Line)ContoursAttrDlg.this.contoursAttrSettings.get(ContoursAttrDlg.this.retrieveDefaultLineType()));
/*  437 */               ContoursAttrDlg.this.lineTemplate.setPgenType(ContoursAttrDlg.this.lineTypeBtn.getData().toString());
/*      */             }
/*      */ 
/*  440 */             ContoursAttrDlg.this.lineAttrDlg.setAttrForDlg(ContoursAttrDlg.this.lineTemplate);
/*      */           }
/*      */ 
/*  445 */           ContoursAttrDlg.ContourLineAttrDlg.access$2(ContoursAttrDlg.this.lineAttrDlg);
/*      */         }
/*      */         catch (VizException e)
/*      */         {
/*  449 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*  455 */     this.applyAllLineBtn = new Button(editlineComp, 32);
/*  456 */     this.applyAllLineBtn.setText("All");
/*      */ 
/*  461 */     Composite quickSymbolComp = new Composite(this.attrComp, 0);
/*  462 */     GridLayout layoutm = new GridLayout(2, false);
/*  463 */     layoutm.marginWidth = 1;
/*  464 */     layoutm.marginHeight = 1;
/*  465 */     layoutm.verticalSpacing = 1;
/*      */ 
/*  467 */     if (this.numOfQuickSymbols == 1) {
/*  468 */       layoutm.horizontalSpacing = 72;
/*      */     }
/*  470 */     else if (this.numOfQuickSymbols == 2) {
/*  471 */       layoutm.horizontalSpacing = 38;
/*      */     }
/*      */     else {
/*  474 */       layoutm.horizontalSpacing = 8;
/*      */     }
/*      */ 
/*  477 */     quickSymbolComp.setLayout(layoutm);
/*      */ 
/*  479 */     Composite quickSymbolTypeComp = new Composite(quickSymbolComp, 0);
/*  480 */     GridLayout quickSymbolTypeCompGl = new GridLayout(2, false);
/*  481 */     quickSymbolTypeCompGl.marginHeight = 1;
/*  482 */     quickSymbolTypeCompGl.marginWidth = 1;
/*  483 */     quickSymbolTypeCompGl.verticalSpacing = 0;
/*  484 */     quickSymbolTypeComp.setLayout(quickSymbolTypeCompGl);
/*      */ 
/*  486 */     Composite quickComp = new Composite(quickSymbolTypeComp, 0);
/*  487 */     GridLayout quickCompGl = new GridLayout(3, false);
/*  488 */     quickCompGl.marginHeight = 1;
/*  489 */     quickCompGl.marginWidth = 1;
/*  490 */     quickCompGl.verticalSpacing = 1;
/*  491 */     quickCompGl.horizontalSpacing = 1;
/*  492 */     quickComp.setLayout(quickCompGl);
/*      */ 
/*  494 */     int ii = 0;
/*  495 */     for (String str : this.quickSymbolType.keySet())
/*      */     {
/*  497 */       if ((((Boolean)this.quickSymbolType.get(str)).booleanValue()) && (ii < 15))
/*      */       {
/*  499 */         ii++;
/*      */ 
/*  501 */         Button btn = new Button(quickComp, 8);
/*  502 */         btn.setToolTipText(((IConfigurationElement)this.symbolItemMap.get(str)).getAttribute("label"));
/*  503 */         btn.setImage(getIcon(str));
/*  504 */         btn.setData(str);
/*      */ 
/*  506 */         this.quickSymbolBtns.add(btn);
/*      */ 
/*  508 */         btn.addListener(8, new Listener() {
/*      */           public void handleEvent(Event event) {
/*  510 */             ContoursAttrDlg.this.openSymbolPanel();
/*      */           }
/*      */         });
/*  514 */         btn.addListener(3, new Listener()
/*      */         {
/*      */           public void handleEvent(Event event)
/*      */           {
/*  518 */             Button btnClicked = (Button)event.widget;
/*  519 */             if ((ContoursAttrDlg.this.drawSymbol()) && (btnClicked == ContoursAttrDlg.this.activeQuickSymbolBtn)) {
/*  520 */               ContoursAttrDlg.this.openSymbolPanel();
/*      */             }
/*      */             else {
/*  523 */               ContoursAttrDlg.this.activeQuickSymbolBtn = btnClicked;
/*      */ 
/*  525 */               for (Button b : ContoursAttrDlg.this.quickSymbolBtns) {
/*  526 */                 if (b == ContoursAttrDlg.this.activeQuickSymbolBtn) {
/*  527 */                   DrawableElement de = (SinglePointElement)ContoursAttrDlg.this.contoursAttrSettings.get(b.getData().toString());
/*  528 */                   ContoursAttrDlg.this.setButtonColor(b, ContoursAttrDlg.this.defaultButtonColor, de.getColors()[0]);
/*      */                 }
/*      */                 else {
/*  531 */                   ContoursAttrDlg.this.setButtonColor(b, ContoursAttrDlg.this.activeButtonColor, ContoursAttrDlg.this.defaultButtonColor);
/*      */                 }
/*      */               }
/*      */ 
/*  535 */               if (!ContoursAttrDlg.this.drawSymbol()) {
/*  536 */                 ContoursAttrDlg.this.setDrawingStatus(ContoursAttrDlg.ContourDrawingStatus.DRAW_SYMBOL);
/*      */               }
/*      */             }
/*      */           }
/*      */         });
/*      */       }
/*      */     }
/*      */ 
/*  544 */     this.activeQuickSymbolBtn = ((Button)this.quickSymbolBtns.get(0));
/*  545 */     DrawableElement de = (SinglePointElement)this.contoursAttrSettings.get(this.activeQuickSymbolBtn.getData().toString());
/*  546 */     setButtonColor(this.activeQuickSymbolBtn, this.defaultButtonColor, de.getColors()[0]);
/*      */ 
/*  548 */     Composite editSymbolAttrComp = new Composite(quickSymbolComp, 0);
/*  549 */     GridLayout editSymbolAttrCompGl = new GridLayout(2, false);
/*  550 */     editSymbolAttrCompGl.marginWidth = 1;
/*  551 */     editSymbolAttrCompGl.horizontalSpacing = 1;
/*  552 */     editSymbolAttrComp.setLayout(editSymbolAttrCompGl);
/*      */ 
/*  554 */     Button editSymbolAttrBtn = new Button(editSymbolAttrComp, 8);
/*  555 */     editSymbolAttrBtn.setText("Edit");
/*  556 */     editSymbolAttrBtn.setToolTipText("Edit contour's symbol attributes");
/*  557 */     editSymbolAttrBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */         try {
/*  562 */           if (ContoursAttrDlg.this.minmaxAttrDlg == null) {
/*  563 */             ContoursAttrDlg.this.minmaxAttrDlg = new ContoursAttrDlg.ContourMinmaxAttrDlg(ContoursAttrDlg.this, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), null);
/*  564 */             ContoursAttrDlg.this.minmaxAttrDlg.setPgenCategory("SYMBOL");
/*  565 */             ContoursAttrDlg.this.minmaxAttrDlg.setPgenType(ContoursAttrDlg.this.getActiveSymbolObjType());
/*      */           }
/*      */ 
/*  568 */           ContoursAttrDlg.this.openAttrDlg(ContoursAttrDlg.this.minmaxAttrDlg);
/*      */ 
/*  570 */           ContoursAttrDlg.ContourMinmaxAttrDlg.access$1(ContoursAttrDlg.this.minmaxAttrDlg);
/*      */ 
/*  573 */           DrawableElement de = ContoursAttrDlg.this.drawingLayer.getSelectedDE();
/*  574 */           if ((de != null) && ((de instanceof Symbol)) && 
/*  575 */             ((de.getParent() instanceof ContourMinmax))) {
/*  576 */             ContoursAttrDlg.this.minmaxAttrDlg.setAttrForDlg(de);
/*      */           }
/*      */           else {
/*  579 */             ContoursAttrDlg.this.minmaxTemplate = ((Symbol)ContoursAttrDlg.this.contoursAttrSettings.get(
/*  580 */               ContoursAttrDlg.this.activeQuickSymbolBtn.getData().toString()));
/*  581 */             if (ContoursAttrDlg.this.minmaxTemplate == null) {
/*  582 */               ContoursAttrDlg.this.minmaxTemplate = new Symbol(null, new java.awt.Color[] { java.awt.Color.green }, 
/*  583 */                 2.0F, 2.0D, Boolean.valueOf(true), null, "Symbol", ContoursAttrDlg.this.getActiveSymbolObjType());
/*      */             }
/*      */ 
/*  587 */             ContoursAttrDlg.this.minmaxAttrDlg.setAttrForDlg(ContoursAttrDlg.this.minmaxTemplate);
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (VizException e)
/*      */         {
/*  593 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*  598 */     this.applyAllSymbolBtn = new Button(editSymbolAttrComp, 32);
/*  599 */     this.applyAllSymbolBtn.setText("All");
/*      */ 
/*  604 */     Composite circleComp = new Composite(this.attrComp, 0);
/*  605 */     GridLayout layoutc = new GridLayout(2, false);
/*  606 */     layoutc.horizontalSpacing = 12;
/*  607 */     layoutc.marginHeight = 1;
/*  608 */     layoutc.marginWidth = 1;
/*  609 */     layoutc.verticalSpacing = 1;
/*  610 */     circleComp.setLayout(layoutc);
/*      */ 
/*  612 */     Composite circleTypeComp = new Composite(circleComp, 0);
/*  613 */     GridLayout circleTypeCompGl = new GridLayout(3, false);
/*  614 */     circleTypeCompGl.marginHeight = 1;
/*  615 */     circleTypeCompGl.marginWidth = 1;
/*  616 */     circleTypeCompGl.verticalSpacing = 0;
/*  617 */     circleTypeComp.setLayout(circleTypeCompGl);
/*      */ 
/*  619 */     this.circleTypeBtn = new Button(circleTypeComp, 8);
/*  620 */     this.circleTypeBtn.setToolTipText("Click to select a line type for circle");
/*  621 */     this.circleTypeBtn.setImage(getIcon("Circle"));
/*  622 */     this.circleTypeBtn.setData("Circle");
/*  623 */     if (this.circleTemplate == null) {
/*  624 */       this.circleTemplate = ((Arc)this.contoursAttrSettings.get("Circle"));
/*  625 */       this.circleTemplate.setPgenType("Circle");
/*      */     }
/*  627 */     setButtonColor(this.circleTypeBtn, this.circleTemplate.getColors()[0]);
/*      */ 
/*  629 */     this.circleTypeBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  631 */         if (!ContoursAttrDlg.this.drawCircle()) {
/*  632 */           if (ContoursAttrDlg.this.circleTemplate == null) {
/*  633 */             ContoursAttrDlg.this.circleTemplate = ((Arc)ContoursAttrDlg.this.contoursAttrSettings.get("Circle"));
/*  634 */             ContoursAttrDlg.this.circleTemplate.setPgenType("Circle");
/*      */           }
/*  636 */           ContoursAttrDlg.this.setButtonColor(ContoursAttrDlg.this.circleTypeBtn, ContoursAttrDlg.this.circleTemplate.getColors()[0]);
/*  637 */           ContoursAttrDlg.this.setDrawingStatus(ContoursAttrDlg.ContourDrawingStatus.DRAW_CIRCLE);
/*      */         }
/*      */       }
/*      */     });
/*  643 */     this.hideCircleLabelBtn = new Button(circleTypeComp, 32);
/*  644 */     this.hideCircleLabelBtn.setText("Hide\nLabel");
/*  645 */     this.hideCircleLabelBtn.setToolTipText("Check to hide the label for this circle");
/*      */ 
/*  647 */     Composite editCircleComp = new Composite(circleComp, 0);
/*  648 */     GridLayout editCircleCompGl = new GridLayout(2, false);
/*  649 */     editCircleCompGl.marginHeight = 0;
/*  650 */     editCircleCompGl.verticalSpacing = 0;
/*  651 */     editCircleCompGl.marginWidth = 1;
/*  652 */     editCircleCompGl.horizontalSpacing = 1;
/*  653 */     editCircleComp.setLayout(editCircleCompGl);
/*      */ 
/*  655 */     Button circleAttrBtn = new Button(editCircleComp, 8);
/*  656 */     circleAttrBtn.setText("Edit");
/*  657 */     circleAttrBtn.setEnabled(true);
/*  658 */     circleAttrBtn.setToolTipText("Edit contour's circle attributes");
/*      */ 
/*  660 */     circleAttrBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */         try {
/*  665 */           if (ContoursAttrDlg.this.circleAttrDlg == null) {
/*  666 */             ContoursAttrDlg.this.circleAttrDlg = new ContoursAttrDlg.ContourCircleAttrDlg(ContoursAttrDlg.this, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), null);
/*      */           }
/*      */ 
/*  669 */           ContoursAttrDlg.this.openAttrDlg(ContoursAttrDlg.this.circleAttrDlg);
/*  670 */           ContoursAttrDlg.ContourCircleAttrDlg.access$1(ContoursAttrDlg.this.circleAttrDlg);
/*      */ 
/*  673 */           DrawableElement de = ContoursAttrDlg.this.drawingLayer.getSelectedDE();
/*      */ 
/*  675 */           if ((de != null) && (de.getParent() != null) && ((de.getParent() instanceof ContourCircle))) {
/*  676 */             ContourCircle pde = (ContourCircle)de.getParent();
/*  677 */             ContoursAttrDlg.this.circleAttrDlg.setAttrForDlg(pde.getCircle());
/*      */           }
/*      */           else
/*      */           {
/*  681 */             if (ContoursAttrDlg.this.circleTemplate == null) {
/*  682 */               ContoursAttrDlg.this.circleTemplate = ((Arc)ContoursAttrDlg.this.contoursAttrSettings.get("Circle"));
/*  683 */               ContoursAttrDlg.this.circleTemplate.setPgenType("Circle");
/*      */             }
/*      */ 
/*  686 */             ContoursAttrDlg.this.circleAttrDlg.setAttrForDlg(ContoursAttrDlg.this.circleTemplate);
/*      */           }
/*      */ 
/*  691 */           ContoursAttrDlg.ContourCircleAttrDlg.access$2(ContoursAttrDlg.this.circleAttrDlg);
/*      */         }
/*      */         catch (VizException e)
/*      */         {
/*  695 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*  701 */     this.applyAllCircleBtn = new Button(editCircleComp, 32);
/*  702 */     this.applyAllCircleBtn.setText("All");
/*  703 */     this.applyAllCircleBtn.setEnabled(true);
/*      */ 
/*  707 */     this.textGrp = new Group(this.attrComp, 16);
/*  708 */     this.textGrp.setText("Label");
/*  709 */     GridLayout labelGl = new GridLayout(1, false);
/*  710 */     labelGl.marginHeight = 1;
/*  711 */     labelGl.marginWidth = 1;
/*  712 */     labelGl.verticalSpacing = 1;
/*  713 */     labelGl.horizontalSpacing = 1;
/*      */ 
/*  715 */     this.textGrp.setLayout(labelGl);
/*      */ 
/*  717 */     Composite textComp = new Composite(this.textGrp, 0);
/*  718 */     GridLayout layout3 = new GridLayout(2, false);
/*  719 */     layout3.horizontalSpacing = 1;
/*  720 */     layout3.marginWidth = 1;
/*  721 */     layout3.verticalSpacing = 0;
/*  722 */     layout3.marginHeight = 0;
/*      */ 
/*  724 */     textComp.setLayout(layout3);
/*      */ 
/*  726 */     Composite textValueComp = new Composite(textComp, 0);
/*  727 */     GridLayout layout4 = new GridLayout(4, false);
/*  728 */     layout4.horizontalSpacing = 1;
/*  729 */     layout4.marginWidth = 1;
/*      */ 
/*  731 */     textValueComp.setLayout(layout4);
/*      */ 
/*  733 */     this.labelTxt = new org.eclipse.swt.widgets.Text(textValueComp, 4);
/*  734 */     this.labelTxt.setLayoutData(new GridData(45, 15));
/*  735 */     this.labelTxt.setEditable(true);
/*  736 */     this.labelTxt.setText("0");
/*  737 */     this.labelTxt.addFocusListener(new FocusListener()
/*      */     {
/*      */       public void focusLost(FocusEvent e) {
/*  740 */         float value = 0.0F;
/*      */         try {
/*  742 */           value = Float.parseFloat(ContoursAttrDlg.this.getLabel());
/*  743 */           if ((value == -9999.0F) || 
/*  744 */             (value == 9999.0F)) {
/*  745 */             ContoursAttrDlg.this.lineClosedBtn.setSelection(true);
/*  746 */             ContoursAttrDlg.this.drawClosedLine = true;
/*      */           }
/*      */         }
/*      */         catch (NumberFormatException localNumberFormatException)
/*      */         {
/*      */         }
/*      */       }
/*      */ 
/*      */       public void focusGained(FocusEvent e)
/*      */       {
/*      */       }
/*      */     });
/*  758 */     Button valueUpArrow = new Button(textValueComp, 132);
/*  759 */     valueUpArrow.setLayoutData(new GridData(20, 22));
/*  760 */     valueUpArrow.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  762 */         ContoursAttrDlg.this.changeLabel(true);
/*      */       }
/*      */     });
/*  767 */     Button valueDownArrow = new Button(textValueComp, 1028);
/*  768 */     valueDownArrow.setLayoutData(new GridData(20, 22));
/*  769 */     valueDownArrow.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  771 */         ContoursAttrDlg.this.changeLabel(false);
/*      */       }
/*      */     });
/*  776 */     this.labelNumSpinner = new Spinner(textValueComp, 2048);
/*  777 */     this.labelNumSpinner.setMinimum(1);
/*  778 */     this.labelNumSpinner.setMaximum(10);
/*  779 */     this.labelNumSpinner.setSelection(1);
/*  780 */     this.labelNumSpinner.setIncrement(1);
/*  781 */     this.labelNumSpinner.setPageIncrement(1);
/*      */ 
/*  783 */     Composite editTextComp = new Composite(textComp, 0);
/*  784 */     GridLayout editTextCompGl = new GridLayout(2, false);
/*  785 */     editTextCompGl.verticalSpacing = 0;
/*  786 */     editTextCompGl.marginHeight = 1;
/*  787 */     editTextCompGl.marginWidth = 1;
/*  788 */     editTextCompGl.horizontalSpacing = 1;
/*  789 */     editTextComp.setLayout(editTextCompGl);
/*      */ 
/*  791 */     Button textAttrBtn = new Button(editTextComp, 8);
/*  792 */     textAttrBtn.setText("Edit");
/*  793 */     textAttrBtn.setToolTipText("Edit contour's label attributes");
/*  794 */     textAttrBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */         try {
/*  799 */           if (ContoursAttrDlg.this.labelAttrDlg == null) {
/*  800 */             ContoursAttrDlg.this.labelAttrDlg = new ContoursAttrDlg.LabelAttrDlg(ContoursAttrDlg.this, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), null);
/*      */           }
/*      */ 
/*  803 */           ContoursAttrDlg.this.openAttrDlg(ContoursAttrDlg.this.labelAttrDlg);
/*      */ 
/*  805 */           ContoursAttrDlg.LabelAttrDlg.access$1(ContoursAttrDlg.this.labelAttrDlg);
/*      */ 
/*  808 */           DrawableElement de = ContoursAttrDlg.this.drawingLayer.getSelectedDE();
/*      */ 
/*  810 */           if (de != null) {
/*  811 */             if (((de.getParent() instanceof ContourLine)) && 
/*  812 */               (((ContourLine)de.getParent()).getLabels().size() > 0)) {
/*  813 */               ContoursAttrDlg.this.labelAttrDlg.setAttrForDlg((IAttribute)((ContourLine)de.getParent()).getLabels().get(0));
/*      */             }
/*  815 */             else if (((de.getParent() instanceof ContourMinmax)) && 
/*  816 */               (((ContourMinmax)de.getParent()).getLabel() != null)) {
/*  817 */               ContoursAttrDlg.this.labelAttrDlg.setAttrForDlg(((ContourMinmax)de.getParent()).getLabel());
/*      */             }
/*  819 */             else if (((de.getParent() instanceof ContourCircle)) && 
/*  820 */               (((ContourCircle)de.getParent()).getLabel() != null)) {
/*  821 */               ContoursAttrDlg.this.labelAttrDlg.setAttrForDlg(((ContourCircle)de.getParent()).getLabel());
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  826 */             if (ContoursAttrDlg.this.labelTemplate == null) {
/*  827 */               ContoursAttrDlg.this.labelTemplate = ((gov.noaa.nws.ncep.ui.pgen.elements.Text)ContoursAttrDlg.this.contoursAttrSettings.get("General Text"));
/*      */             }
/*      */ 
/*  830 */             ContoursAttrDlg.this.labelAttrDlg.setAttrForDlg(ContoursAttrDlg.this.labelTemplate);
/*      */           }
/*      */ 
/*  833 */           ContoursAttrDlg.this.labelAttrDlg.setText(new String[] { ContoursAttrDlg.this.getLabel() });
/*      */ 
/*  836 */           ContoursAttrDlg.LabelAttrDlg.access$2(ContoursAttrDlg.this.labelAttrDlg);
/*      */         }
/*      */         catch (VizException e)
/*      */         {
/*  840 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*  846 */     this.applyAllLabelBtn = new Button(editTextComp, 32);
/*  847 */     this.applyAllLabelBtn.setText("All");
/*      */ 
/*  850 */     createLabelBtns(this.textGrp, true);
/*      */ 
/*  852 */     addSeparator(this.top);
/*      */ 
/*  855 */     Composite editContourLineComp = new Composite(this.top, 0);
/*  856 */     GridLayout editContourLineCompGl = new GridLayout(3, false);
/*  857 */     editContourLineCompGl.marginHeight = 1;
/*  858 */     editContourLineCompGl.verticalSpacing = 0;
/*  859 */     editContourLineComp.setLayout(editContourLineCompGl);
/*      */ 
/*  861 */     editContourLineComp.setLayoutData(new GridData(16777216, -1, false, false));
/*      */ 
/*  863 */     this.selectLineBtn = new Button(editContourLineComp, 8);
/*  864 */     this.selectLineBtn.setText("Select");
/*  865 */     this.selectLineBtn.setToolTipText("Select a contour component");
/*  866 */     this.selectLineBtn.addSelectionListener(this);
/*  867 */     setButtonColor(this.selectLineBtn, this.defaultButtonColor);
/*      */ 
/*  869 */     this.deleteLineBtn = new Button(editContourLineComp, 8);
/*  870 */     this.deleteLineBtn.setText("Delete");
/*  871 */     this.deleteLineBtn.setToolTipText("Delete a contour component");
/*  872 */     this.deleteLineBtn.addSelectionListener(this);
/*  873 */     setButtonColor(this.deleteLineBtn, this.defaultButtonColor);
/*      */ 
/*  875 */     addSeparator(this.top);
/*      */ 
/*  877 */     return this.top;
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute ia)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IContours ic)
/*      */   {
/*  890 */     setAttributes(ic);
/*      */   }
/*      */ 
/*      */   public String getCint()
/*      */   {
/*  895 */     return this.contourCint;
/*      */   }
/*      */ 
/*      */   public String getLevel()
/*      */   {
/*  900 */     return this.contourLevel;
/*      */   }
/*      */ 
/*      */   public String getForecastHour()
/*      */   {
/*  905 */     return this.contourFcstHr;
/*      */   }
/*      */ 
/*      */   public String getParm()
/*      */   {
/*  910 */     return this.contourParm;
/*      */   }
/*      */ 
/*      */   public Calendar getTime1()
/*      */   {
/*  915 */     return this.contourTime1;
/*      */   }
/*      */ 
/*      */   public Calendar getTime2()
/*      */   {
/*  920 */     return this.contourTime2;
/*      */   }
/*      */ 
/*      */   public Boolean isClosedLine()
/*      */   {
/*  925 */     return Boolean.valueOf(this.lineClosedBtn.getSelection());
/*      */   }
/*      */ 
/*      */   public String getLabel()
/*      */   {
/*  930 */     String lbl = this.labelTxt.getText();
/*  931 */     if ((lbl == null) || (lbl.trim().length() == 0)) {
/*  932 */       lbl = new String("0.0");
/*  933 */       setLabel(lbl);
/*      */     }
/*      */ 
/*  936 */     return lbl;
/*      */   }
/*      */ 
/*      */   public int getNumOfLabels()
/*      */   {
/*  950 */     int nlabels = Integer.parseInt(this.labelNumSpinner.getText());
/*      */ 
/*  953 */     return nlabels;
/*      */   }
/*      */ 
/*      */   public void setNumOfLabels(int nlabels)
/*      */   {
/*  962 */     this.labelNumSpinner.setSelection(nlabels);
/*      */   }
/*      */ 
/*      */   private void openContourInfoDlg()
/*      */   {
/*  971 */     if (contoursInfoDlg == null) {
/*  972 */       contoursInfoDlg = new ContoursInfoDlg(getShell());
/*  973 */       contoursInfoDlg.setContoursAttrDlg(this);
/*      */     }
/*      */ 
/*  976 */     contoursInfoDlg.open();
/*      */   }
/*      */ 
/*      */   public void setAttributes(IContours attr)
/*      */   {
/*  985 */     setParm(attr.getParm());
/*  986 */     setLevel(attr.getLevel());
/*  987 */     setFcstHr(attr.getForecastHour());
/*  988 */     setTime1(attr.getTime1());
/*  989 */     setTime2(attr.getTime2());
/*  990 */     setCint(attr.getCint());
/*      */ 
/*  992 */     setInfoBtnText();
/*      */   }
/*      */ 
/*      */   public void setLabel(String lbl)
/*      */   {
/*  999 */     this.labelTxt.setText(lbl);
/* 1000 */     updateLabelBtnsSelection(lbl);
/*      */   }
/*      */ 
/*      */   public void setParm(String parm)
/*      */   {
/* 1007 */     this.contourParm = parm;
/*      */   }
/*      */ 
/*      */   public void setLevel(String level)
/*      */   {
/* 1014 */     this.contourLevel = level;
/*      */   }
/*      */ 
/*      */   public void setFcstHr(String fcstHr)
/*      */   {
/* 1021 */     this.contourFcstHr = fcstHr;
/*      */   }
/*      */ 
/*      */   public void setTime1(Calendar time)
/*      */   {
/* 1028 */     this.contourTime1 = time;
/*      */   }
/*      */ 
/*      */   public void setTime2(Calendar time)
/*      */   {
/* 1035 */     this.contourTime2 = time;
/*      */   }
/*      */ 
/*      */   public void setCint(String cint)
/*      */   {
/* 1042 */     this.contourCint = cint;
/* 1043 */     createLabelBtns(this.textGrp, false);
/*      */   }
/*      */ 
/*      */   public void setClosed(boolean closed)
/*      */   {
/* 1050 */     this.lineClosedBtn.setSelection(closed);
/* 1051 */     this.drawClosedLine = closed;
/*      */   }
/*      */ 
/*      */   private void changeLabel(boolean upArrowClicked)
/*      */   {
/* 1059 */     int ii = 0;
/* 1060 */     for (Button btn : this.labelBtns)
/*      */     {
/* 1062 */       if (btn.getSelection())
/*      */       {
/*      */         int next;
/*      */         int next;
/* 1065 */         if (upArrowClicked) {
/* 1066 */           next = (ii + 1) % this.labelBtns.size();
/*      */         }
/*      */         else {
/* 1069 */           next = (ii - 1 + this.labelBtns.size()) % this.labelBtns.size();
/*      */         }
/*      */ 
/* 1072 */         btn.setSelection(false);
/* 1073 */         ((Button)this.labelBtns.get(next)).setSelection(true);
/* 1074 */         this.labelTxt.setText(((Button)this.labelBtns.get(next)).getText());
/* 1075 */         break;
/*      */       }
/*      */ 
/* 1078 */       ii++;
/*      */     }
/*      */ 
/* 1081 */     if (this.labelTemplate != null)
/* 1082 */       this.labelTemplate.setText(new String[] { getLabel() });
/*      */   }
/*      */ 
/*      */   private void updateLabelBtnsSelection(String lbl)
/*      */   {
/* 1092 */     for (Button btn : this.labelBtns) {
/* 1093 */       btn.setSelection(false);
/*      */     }
/*      */ 
/* 1096 */     for (Button btn : this.labelBtns)
/* 1097 */       if (btn.getText().equals(lbl)) {
/* 1098 */         btn.setSelection(true);
/* 1099 */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   private void createLabelBtns(Composite comp, boolean firstTime)
/*      */   {
/* 1110 */     if (firstTime) {
/* 1111 */       this.labelGrp = new Composite(comp, 64);
/* 1112 */       GridLayout gl = new GridLayout(3, true);
/* 1113 */       gl.marginHeight = 1;
/* 1114 */       gl.marginWidth = 1;
/* 1115 */       gl.horizontalSpacing = 1;
/* 1116 */       gl.verticalSpacing = 1;
/* 1117 */       this.labelGrp.setLayout(gl);
/*      */     }
/*      */     else {
/* 1120 */       Control[] wids = this.labelGrp.getChildren();
/* 1121 */       if (wids != null) {
/* 1122 */         for (int jj = 0; jj < wids.length; jj++) {
/* 1123 */           wids[jj].dispose();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1130 */     if (!firstTime) getShell().pack();
/*      */ 
/* 1134 */     if (this.labelBtns != null) {
/* 1135 */       this.labelBtns.clear();
/*      */     }
/*      */     else {
/* 1138 */       this.labelBtns = new ArrayList();
/*      */     }
/*      */ 
/* 1144 */     CINT cd = new CINT(this.contourCint);
/* 1145 */     if (cd.isCINTStringParsed()) {
/* 1146 */       for (String dbl : cd.getContourLabelsForZoomLevel(CINT.FIRST_ZOOM_LEVEL))
/*      */       {
/* 1148 */         Button btn = new Button(this.labelGrp, 16);
/* 1149 */         btn.setText(dbl);
/* 1150 */         btn.setData(dbl);
/* 1151 */         btn.addSelectionListener(new SelectionAdapter() {
/*      */           public void widgetSelected(SelectionEvent event) {
/* 1153 */             ContoursAttrDlg.this.labelTxt.setText(event.widget.getData().toString());
/* 1154 */             if (ContoursAttrDlg.this.labelTemplate != null)
/* 1155 */               ContoursAttrDlg.this.labelTemplate.setText(new String[] { ContoursAttrDlg.this.getLabel() });
/*      */           }
/*      */         });
/* 1161 */         this.labelBtns.add(btn);
/*      */       }
/*      */ 
/* 1164 */       if (this.labelBtns.size() > 0) {
/* 1165 */         ((Button)this.labelBtns.get(0)).setSelection(true);
/* 1166 */         this.labelTxt.setText(((Button)this.labelBtns.get(0)).getText());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1173 */     if (!firstTime) {
/* 1174 */       getShell().pack();
/* 1175 */       getShell().layout();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void widgetDefaultSelected(SelectionEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void widgetSelected(SelectionEvent e)
/*      */   {
/* 1189 */     if ((e.widget instanceof Button))
/*      */     {
/* 1191 */       Button b = (Button)e.widget;
/* 1192 */       DrawableElement de = this.drawingLayer.getSelectedDE();
/*      */ 
/* 1194 */       if (de != null) {
/* 1195 */         this.currentContours = ((Contours)de.getParent().getParent());
/*      */       }
/*      */ 
/* 1201 */       String btnName = b.getText();
/*      */ 
/* 1206 */       if ((btnName.equals("Select")) || 
/* 1207 */         (btnName.equals("Add")) || 
/* 1208 */         (btnName.equals("Delete")))
/*      */       {
/* 1210 */         closeAttrEditDialogs();
/*      */       }
/*      */ 
/* 1216 */       if (btnName.equals("Select"))
/*      */       {
/* 1218 */         setButtonColor(this.selectLineBtn, this.activeButtonColor);
/* 1219 */         setButtonColor(this.deleteLineBtn, this.defaultButtonColor);
/*      */ 
/* 1221 */         this.drawingLayer.removeSelected();
/*      */ 
/* 1223 */         setDrawingStatus(ContourDrawingStatus.SELECT);
/* 1224 */         this.tool.setPgenSelectHandler();
/*      */       }
/* 1231 */       else if (btnName.equals("Add"))
/*      */       {
/* 1233 */         this.tool.setPgenContoursHandler();
/*      */ 
/* 1235 */         getButton(1).setEnabled(false);
/* 1236 */         getButton(0).setEnabled(false);
/*      */ 
/* 1238 */         setButtonColor(this.selectLineBtn, this.defaultButtonColor);
/* 1239 */         setButtonColor(this.deleteLineBtn, this.defaultButtonColor);
/*      */       }
/* 1247 */       else if (btnName.equals("Delete"))
/*      */       {
/* 1249 */         setButtonColor(this.selectLineBtn, this.activeButtonColor);
/* 1250 */         setButtonColor(this.deleteLineBtn, this.defaultButtonColor);
/*      */ 
/* 1252 */         if (((de != null) && 
/* 1253 */           ((de instanceof Line)) && ((de.getParent() instanceof ContourLine))) || 
/* 1254 */           (((de instanceof Symbol)) && ((de.getParent() instanceof ContourMinmax))) || (
/* 1255 */           ((de instanceof Arc)) && ((de.getParent() instanceof ContourCircle))))
/*      */         {
/* 1257 */           Contours oldContours = (Contours)de.getParent().getParent();
/* 1258 */           Contours newContours = new Contours();
/*      */ 
/* 1260 */           if (oldContours.size() <= 1)
/*      */           {
/* 1262 */             this.drawingLayer.removeElement(oldContours);
/* 1263 */             newContours = null;
/*      */           }
/*      */           else
/*      */           {
/* 1267 */             Iterator iterator = oldContours.getComponentIterator();
/*      */ 
/* 1273 */             while (iterator.hasNext())
/*      */             {
/* 1275 */               AbstractDrawableComponent oldAdc = (AbstractDrawableComponent)iterator.next();
/* 1276 */               AbstractDrawableComponent newAdc = oldAdc.copy();
/*      */ 
/* 1278 */               if (!oldAdc.equals(de.getParent())) {
/* 1279 */                 newAdc.setParent(newContours);
/* 1280 */                 newContours.add(newAdc);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/* 1288 */             newContours.update(this);
/* 1289 */             this.drawingLayer.replaceElement(oldContours, newContours);
/*      */           }
/*      */ 
/* 1296 */           this.currentContours = newContours;
/* 1297 */           this.drawingLayer.removeSelected();
/*      */ 
/* 1299 */           if (this.tool != null) this.tool.setCurrentContour(newContours);
/*      */ 
/*      */         }
/*      */ 
/* 1303 */         if (this.mapEditor != null)
/* 1304 */           this.mapEditor.refresh();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setInfoBtnText()
/*      */   {
/* 1316 */     String str = this.contourParm + ", " + this.contourLevel + ", " + this.contourFcstHr + "\n" + 
/* 1317 */       this.contourTime1.get(1) + "-" + (
/* 1318 */       this.contourTime1.get(2) + 1) + "-" + 
/* 1319 */       this.contourTime1.get(5) + "  " + 
/* 1320 */       this.contourTime1.get(11) + ":" + 
/* 1321 */       this.contourTime1.get(12) + "Z";
/*      */ 
/* 1323 */     this.infoBtn.setText(str);
/* 1324 */     this.infoComp.pack();
/*      */   }
/*      */ 
/*      */   public boolean drawContourLine()
/*      */   {
/* 1333 */     return this.drawingStatus == ContourDrawingStatus.DRAW_LINE;
/*      */   }
/*      */ 
/*      */   public void okPressed()
/*      */   {
/* 1345 */     DrawableElement de = this.drawingLayer.getSelectedDE();
/* 1346 */     if ((de != null) && (de.getParent() != null) && 
/* 1347 */       ((de.getParent().getParent() instanceof Contours)))
/*      */     {
/* 1349 */       DrawableElement newEl = (DrawableElement)de.copy();
/*      */ 
/* 1351 */       Contours oldContours = (Contours)de.getParent().getParent();
/*      */ 
/* 1353 */       Iterator iterator = oldContours.getComponentIterator();
/*      */ 
/* 1355 */       Contours newContours = new Contours();
/*      */ 
/* 1361 */       while (iterator.hasNext())
/*      */       {
/* 1363 */         AbstractDrawableComponent oldAdc = (AbstractDrawableComponent)iterator.next();
/* 1364 */         AbstractDrawableComponent newAdc = oldAdc.copy();
/*      */ 
/* 1366 */         if (oldAdc.equals(de.getParent()))
/*      */         {
/* 1368 */           newEl.setParent(newAdc);
/*      */ 
/* 1370 */           if ((oldAdc instanceof ContourCircle))
/*      */           {
/* 1372 */             if ((newEl instanceof Arc)) {
/* 1373 */               ((DECollection)newAdc).replace(((ContourCircle)newAdc).getCircle(), newEl);
/*      */             }
/*      */ 
/* 1376 */             ((ContourCircle)newAdc).updateLabelString(new String[] { getLabel() });
/* 1377 */             ((ContourCircle)newAdc).getLabel().setHide(Boolean.valueOf(hideCircleLabel()));
/*      */           }
/* 1379 */           else if ((oldAdc instanceof ContourLine))
/*      */           {
/* 1381 */             if ((newEl instanceof Line)) {
/* 1382 */               ((DECollection)newAdc).replace(((ContourLine)newAdc).getLine(), newEl);
/*      */             }
/*      */ 
/* 1385 */             ((ContourLine)newAdc).getLine().setPgenType(getContourLineType());
/* 1386 */             ((ContourLine)newAdc).getLine().setClosed(Boolean.valueOf(this.lineClosedBtn.getSelection()));
/*      */ 
/* 1388 */             int nlabels = ((ContourLine)newAdc).getNumOfLabels();
/* 1389 */             if (nlabels != getNumOfLabels())
/*      */             {
/* 1391 */               ((ContourLine)newAdc).updateNumOfLabels(getNumOfLabels());
/*      */ 
/* 1393 */               if ((newEl instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text)) {
/* 1394 */                 if (((ContourLine)newAdc).getLabels().size() > 0) {
/* 1395 */                   newEl = (DrawableElement)((ContourLine)newAdc).getLabels().get(0);
/*      */                 }
/*      */                 else {
/* 1398 */                   this.drawingLayer.removeSelected();
/* 1399 */                   newEl = null;
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/* 1404 */             ((ContourLine)newAdc).updateLabelString(new String[] { getLabel() });
/*      */           }
/* 1407 */           else if ((oldAdc instanceof ContourMinmax))
/*      */           {
/* 1409 */             if ((newEl instanceof Symbol)) {
/* 1410 */               ((DECollection)newAdc).replace(((ContourMinmax)newAdc).getSymbol(), newEl);
/*      */             }
/*      */ 
/* 1413 */             ((ContourMinmax)newAdc).getSymbol().setPgenCategory(getActiveSymbolClass());
/* 1414 */             ((ContourMinmax)newAdc).getSymbol().setPgenType(getActiveSymbolObjType());
/*      */ 
/* 1416 */             ((ContourMinmax)newAdc).updateLabelString(new String[] { getLabel() });
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1421 */         newAdc.setParent(newContours);
/* 1422 */         newContours.add(newAdc);
/*      */       }
/*      */ 
/* 1428 */       newContours.update(this);
/* 1429 */       this.drawingLayer.replaceElement(oldContours, newContours);
/* 1430 */       if (this.tool != null) this.tool.setCurrentContour(newContours);
/*      */ 
/* 1435 */       this.currentContours = newContours;
/* 1436 */       this.drawingLayer.removeSelected();
/* 1437 */       if (newEl != null) {
/* 1438 */         this.drawingLayer.setSelected(newEl);
/*      */       }
/*      */ 
/*      */     }
/* 1442 */     else if (this.currentContours != null)
/*      */     {
/* 1444 */       Contours oldContours = this.currentContours;
/*      */ 
/* 1446 */       if ((!this.contourParm.equals(oldContours.getParm())) || 
/* 1447 */         (!this.contourLevel.equals(oldContours.getLevel())) || 
/* 1448 */         (!this.contourFcstHr.equals(oldContours.getForecastHour())) || 
/* 1449 */         (!this.contourCint.equals(oldContours.getCint())))
/*      */       {
/* 1451 */         Contours newContours = oldContours.copy();
/*      */ 
/* 1453 */         newContours.update(this);
/* 1454 */         this.drawingLayer.replaceElement(oldContours, newContours);
/* 1455 */         if (this.tool != null) this.tool.setCurrentContour(newContours);
/*      */ 
/* 1457 */         this.currentContours = newContours;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1462 */     if (this.mapEditor != null)
/* 1463 */       this.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   public void disableActionButtons()
/*      */   {
/* 1474 */     this.selectLineBtn.setEnabled(false);
/* 1475 */     this.deleteLineBtn.setEnabled(false);
/*      */   }
/*      */ 
/*      */   public boolean close()
/*      */   {
/* 1485 */     closeAttrEditDialogs();
/*      */ 
/* 1487 */     if (g2gDlg != null) g2gDlg.close();
/*      */ 
/* 1489 */     if ((this.quickSymbolBtns != null) && (this.quickSymbolBtns.size() > 0)) {
/* 1490 */       this.quickSymbolBtns.clear();
/*      */     }
/*      */ 
/* 1493 */     this.currentContours = null;
/*      */ 
/* 1495 */     return super.close();
/*      */   }
/*      */ 
/*      */   private void closeAttrEditDialogs()
/*      */   {
/* 1504 */     if (this.labelAttrDlg != null) this.labelAttrDlg.close();
/* 1505 */     if (this.lineAttrDlg != null) this.lineAttrDlg.close();
/* 1506 */     if (this.minmaxAttrDlg != null) this.minmaxAttrDlg.close();
/* 1507 */     if (this.circleAttrDlg != null) this.circleAttrDlg.close();
/*      */   }
/*      */ 
/*      */   public Contours getCurrentContours()
/*      */   {
/* 1516 */     return this.currentContours;
/*      */   }
/*      */ 
/*      */   public void setCurrentContours(Contours currentContours)
/*      */   {
/* 1523 */     this.currentContours = currentContours;
/*      */   }
/*      */ 
/*      */   private Boolean updateAllLineAttr()
/*      */   {
/* 1530 */     return Boolean.valueOf(this.applyAllLineBtn.getSelection());
/*      */   }
/*      */ 
/*      */   private Boolean updateAllLabelAttr()
/*      */   {
/* 1537 */     return Boolean.valueOf(this.applyAllLabelBtn.getSelection());
/*      */   }
/*      */ 
/*      */   private Boolean updateAllCircleAttr()
/*      */   {
/* 1544 */     return Boolean.valueOf(this.applyAllCircleBtn.getSelection());
/*      */   }
/*      */ 
/*      */   public IAttribute getLabelTemplate()
/*      */   {
/* 1551 */     if ((this.labelAttrDlg != null) && (this.labelAttrDlg.getShell() != null)) {
/* 1552 */       return this.labelAttrDlg;
/*      */     }
/* 1554 */     if (this.labelTemplate != null) {
/* 1555 */       return this.labelTemplate;
/*      */     }
/*      */ 
/* 1558 */     return (IAttribute)this.contoursAttrSettings.get("General Text");
/*      */   }
/*      */ 
/*      */   public void setLabelTemplate(gov.noaa.nws.ncep.ui.pgen.elements.Text labelTemplate)
/*      */   {
/* 1566 */     this.labelTemplate = labelTemplate;
/*      */   }
/*      */ 
/*      */   public IAttribute getLineTemplate()
/*      */   {
/* 1573 */     if ((this.lineAttrDlg != null) && (this.lineAttrDlg.getShell() != null)) {
/* 1574 */       return this.lineAttrDlg;
/*      */     }
/* 1576 */     if (this.lineTemplate != null) {
/* 1577 */       return this.lineTemplate;
/*      */     }
/*      */ 
/* 1580 */     this.lineTemplate = ((Line)this.contoursAttrSettings.get(retrieveDefaultLineType()));
/* 1581 */     this.lineTemplate.setPgenType(this.lineTypeBtn.getData().toString());
/* 1582 */     return this.lineTemplate;
/*      */   }
/*      */ 
/*      */   public void setLineTemplate(Line lineTemplate)
/*      */   {
/* 1589 */     this.lineTemplate = lineTemplate;
/*      */   }
/*      */ 
/*      */   public IAttribute getMinmaxTemplate()
/*      */   {
/* 1596 */     if ((this.minmaxAttrDlg != null) && (this.minmaxAttrDlg.getShell() != null)) {
/* 1597 */       return this.minmaxAttrDlg;
/*      */     }
/*      */ 
/* 1600 */     return (IAttribute)this.contoursAttrSettings.get(this.activeQuickSymbolBtn.getData().toString());
/*      */   }
/*      */ 
/*      */   public void setMinmaxTemplate(Symbol minmaxTemplate)
/*      */   {
/* 1608 */     this.minmaxTemplate = minmaxTemplate;
/*      */   }
/*      */ 
/*      */   public IAttribute getCircleTemplate()
/*      */   {
/* 1615 */     if ((this.circleAttrDlg != null) && (this.circleAttrDlg.getShell() != null)) {
/* 1616 */       return this.circleAttrDlg;
/*      */     }
/* 1618 */     if (this.circleTemplate != null) {
/* 1619 */       return this.circleTemplate;
/*      */     }
/*      */ 
/* 1622 */     this.circleTemplate = ((Arc)this.contoursAttrSettings.get("Circle"));
/* 1623 */     return this.circleTemplate;
/*      */   }
/*      */ 
/*      */   public void setCircleTemplate(Arc circleTemplate)
/*      */   {
/* 1631 */     this.circleTemplate = circleTemplate;
/*      */   }
/*      */ 
/*      */   private void openAttrDlg(AttrDlg dlg)
/*      */   {
/* 1638 */     dlg.setBlockOnOpen(false);
/* 1639 */     dlg.setDrawingLayer(this.drawingLayer);
/* 1640 */     dlg.setMapEditor(this.mapEditor);
/* 1641 */     dlg.open();
/* 1642 */     dlg.enableButtons();
/*      */   }
/*      */ 
/*      */   private Boolean updateAllMinmaxAttr()
/*      */   {
/* 1649 */     return Boolean.valueOf(this.applyAllSymbolBtn.getSelection());
/*      */   }
/*      */ 
/*      */   private void updateLabelAttributes()
/*      */   {
/* 1726 */     DrawableElement de = this.drawingLayer.getSelectedDE();
/*      */ 
/* 1728 */     if ((de == null) && (!updateAllLabelAttr().booleanValue())) {
/* 1729 */       return;
/*      */     }
/*      */ 
/* 1736 */     DrawableElement newEl = null;
/* 1737 */     Contours oldContours = null;
/*      */ 
/* 1739 */     if ((de != null) && (de.getParent() != null) && ((de.getParent().getParent() instanceof Contours))) {
/* 1740 */       newEl = (DrawableElement)de.copy();
/* 1741 */       oldContours = (Contours)de.getParent().getParent();
/*      */     }
/*      */     else {
/* 1744 */       oldContours = this.currentContours;
/*      */     }
/*      */ 
/* 1748 */     if (oldContours != null)
/*      */     {
/* 1750 */       Iterator iterator = oldContours.getComponentIterator();
/*      */ 
/* 1752 */       Contours newContours = new Contours();
/*      */ 
/* 1757 */       while (iterator.hasNext())
/*      */       {
/* 1759 */         AbstractDrawableComponent oldAdc = (AbstractDrawableComponent)iterator.next();
/* 1760 */         AbstractDrawableComponent newAdc = oldAdc.copy();
/*      */ 
/* 1762 */         if ((newAdc instanceof ContourLine))
/*      */         {
/* 1764 */           if (updateAllLabelAttr().booleanValue()) {
/* 1765 */             for (gov.noaa.nws.ncep.ui.pgen.elements.Text lbl : ((ContourLine)newAdc).getLabels()) {
/* 1766 */               String[] str = lbl.getText();
/* 1767 */               boolean hide = lbl.getHide().booleanValue();
/* 1768 */               boolean auto = lbl.getAuto().booleanValue();
/* 1769 */               lbl.update(this.labelTemplate);
/* 1770 */               lbl.setHide(Boolean.valueOf(hide));
/* 1771 */               lbl.setAuto(Boolean.valueOf(auto));
/* 1772 */               lbl.setText(str);
/*      */             }
/*      */           }
/*      */ 
/* 1776 */           if ((newEl != null) && (oldAdc.equals(de.getParent())))
/*      */           {
/* 1778 */             newEl.setParent(newAdc);
/*      */ 
/* 1780 */             if ((newEl instanceof Line)) {
/* 1781 */               ((DECollection)newAdc).replace(((ContourLine)newAdc).getLine(), newEl);
/*      */             }
/*      */ 
/* 1784 */             for (gov.noaa.nws.ncep.ui.pgen.elements.Text lbl : ((ContourLine)newAdc).getLabels())
/*      */             {
/* 1786 */               if (lbl.equals(de)) {
/* 1787 */                 ((DECollection)newAdc).replace(lbl, newEl);
/*      */               }
/*      */ 
/* 1790 */               boolean hide = lbl.getHide().booleanValue();
/* 1791 */               boolean auto = lbl.getAuto().booleanValue();
/* 1792 */               lbl.update(this.labelTemplate);
/* 1793 */               lbl.setHide(Boolean.valueOf(hide));
/* 1794 */               lbl.setAuto(Boolean.valueOf(auto));
/* 1795 */               lbl.setText(new String[] { getLabel() });
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/* 1800 */         else if ((newAdc instanceof ContourMinmax))
/*      */         {
/* 1802 */           if (updateAllLabelAttr().booleanValue()) {
/* 1803 */             gov.noaa.nws.ncep.ui.pgen.elements.Text lbl = ((ContourMinmax)newAdc).getLabel();
/* 1804 */             String[] str = lbl.getText();
/* 1805 */             boolean hide = lbl.getHide().booleanValue();
/* 1806 */             boolean auto = lbl.getAuto().booleanValue();
/* 1807 */             lbl.update(this.labelTemplate);
/* 1808 */             lbl.setHide(Boolean.valueOf(hide));
/* 1809 */             lbl.setAuto(Boolean.valueOf(auto));
/* 1810 */             lbl.setText(str);
/*      */           }
/*      */ 
/* 1813 */           if ((newEl != null) && (oldAdc.equals(de.getParent()))) {
/* 1814 */             newEl.setParent(newAdc);
/* 1815 */             if ((newEl instanceof Symbol)) {
/* 1816 */               ((DECollection)newAdc).replace(((ContourMinmax)newAdc).getSymbol(), newEl);
/*      */             }
/*      */             else {
/* 1819 */               ((DECollection)newAdc).replace(((ContourMinmax)newAdc).getLabel(), newEl);
/*      */             }
/*      */ 
/* 1822 */             gov.noaa.nws.ncep.ui.pgen.elements.Text lbl = ((ContourMinmax)newAdc).getLabel();
/* 1823 */             boolean hide = lbl.getHide().booleanValue();
/* 1824 */             boolean auto = lbl.getAuto().booleanValue();
/* 1825 */             lbl.update(this.labelTemplate);
/* 1826 */             lbl.setHide(Boolean.valueOf(hide));
/* 1827 */             lbl.setAuto(Boolean.valueOf(auto));
/* 1828 */             lbl.setText(new String[] { getLabel() });
/*      */           }
/*      */ 
/*      */         }
/* 1832 */         else if ((newAdc instanceof ContourCircle))
/*      */         {
/* 1834 */           if (updateAllLabelAttr().booleanValue()) {
/* 1835 */             gov.noaa.nws.ncep.ui.pgen.elements.Text lbl = ((ContourCircle)newAdc).getLabel();
/* 1836 */             String[] str = lbl.getText();
/* 1837 */             boolean hide = lbl.getHide().booleanValue();
/* 1838 */             boolean auto = lbl.getAuto().booleanValue();
/* 1839 */             lbl.update(this.labelTemplate);
/* 1840 */             lbl.setHide(Boolean.valueOf(hide));
/* 1841 */             lbl.setAuto(Boolean.valueOf(auto));
/* 1842 */             lbl.setText(str);
/*      */           }
/*      */ 
/* 1845 */           if ((newEl != null) && (oldAdc.equals(de.getParent()))) {
/* 1846 */             newEl.setParent(newAdc);
/* 1847 */             if ((newEl instanceof Arc)) {
/* 1848 */               ((DECollection)newAdc).replace(((ContourCircle)newAdc).getCircle(), newEl);
/*      */             }
/*      */             else {
/* 1851 */               ((DECollection)newAdc).replace(((ContourCircle)newAdc).getLabel(), newEl);
/*      */             }
/*      */ 
/* 1854 */             gov.noaa.nws.ncep.ui.pgen.elements.Text lbl = ((ContourCircle)newAdc).getLabel();
/* 1855 */             boolean hide = lbl.getHide().booleanValue();
/* 1856 */             boolean auto = lbl.getAuto().booleanValue();
/* 1857 */             lbl.update(this.labelTemplate);
/* 1858 */             lbl.setHide(Boolean.valueOf(hide));
/* 1859 */             lbl.setAuto(Boolean.valueOf(auto));
/* 1860 */             lbl.setText(new String[] { getLabel() });
/*      */           }
/*      */         }
/*      */ 
/* 1864 */         newAdc.setParent(newContours);
/* 1865 */         newContours.add(newAdc);
/*      */       }
/*      */ 
/* 1871 */       newContours.update(oldContours);
/* 1872 */       this.drawingLayer.replaceElement(oldContours, newContours);
/*      */ 
/* 1877 */       this.currentContours = newContours;
/* 1878 */       if (this.tool != null) this.tool.setCurrentContour(newContours);
/*      */ 
/* 1880 */       if (newEl != null) {
/* 1881 */         this.drawingLayer.removeSelected();
/* 1882 */         this.drawingLayer.setSelected(newEl);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1887 */     if (this.mapEditor != null)
/* 1888 */       this.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   private void updateLineAttributes()
/*      */   {
/* 1966 */     DrawableElement de = this.drawingLayer.getSelectedDE();
/*      */ 
/* 1976 */     DrawableElement newEl = null;
/*      */ 
/* 1978 */     Contours oldContours = null;
/*      */ 
/* 1980 */     if ((de != null) && ((de.getParent() instanceof ContourLine))) {
/* 1981 */       newEl = (DrawableElement)de.copy();
/* 1982 */       oldContours = (Contours)de.getParent().getParent();
/*      */     }
/*      */     else {
/* 1985 */       oldContours = this.currentContours;
/*      */     }
/*      */ 
/* 1989 */     if (oldContours != null)
/*      */     {
/* 1991 */       Iterator iterator = oldContours.getComponentIterator();
/*      */ 
/* 1993 */       Contours newContours = new Contours();
/*      */ 
/* 1998 */       while (iterator.hasNext())
/*      */       {
/* 2000 */         AbstractDrawableComponent oldAdc = (AbstractDrawableComponent)iterator.next();
/* 2001 */         AbstractDrawableComponent newAdc = oldAdc.copy();
/*      */ 
/* 2003 */         if ((newAdc instanceof ContourLine)) {
/* 2004 */           if (updateAllLineAttr().booleanValue()) {
/* 2005 */             Line oneLine = ((ContourLine)newAdc).getLine();
/* 2006 */             Boolean isClosed = oneLine.isClosedLine();
/* 2007 */             oneLine.update(this.lineTemplate);
/* 2008 */             oneLine.setClosed(isClosed);
/*      */           }
/*      */ 
/* 2011 */           if ((newEl != null) && (oldAdc.equals(de.getParent()))) {
/* 2012 */             newEl.setParent(newAdc);
/* 2013 */             if ((newEl instanceof Line)) {
/* 2014 */               ((DECollection)newAdc).replace(((ContourLine)newAdc).getLine(), newEl);
/*      */             }
/*      */ 
/* 2017 */             ((ContourLine)newAdc).getLine().update(this.lineTemplate);
/* 2018 */             ((ContourLine)newAdc).getLine().setClosed(isClosedLine());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2024 */         newAdc.setParent(newContours);
/* 2025 */         newContours.add(newAdc);
/*      */       }
/*      */ 
/* 2031 */       newContours.update(oldContours);
/* 2032 */       this.drawingLayer.replaceElement(oldContours, newContours);
/*      */ 
/* 2037 */       this.currentContours = newContours;
/* 2038 */       if (this.tool != null) this.tool.setCurrentContour(newContours);
/*      */ 
/* 2040 */       if (newEl != null) {
/* 2041 */         this.drawingLayer.removeSelected();
/* 2042 */         this.drawingLayer.setSelected(newEl);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2047 */     if (this.mapEditor != null)
/* 2048 */       this.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   private void updateCircleAttributes()
/*      */   {
/* 2132 */     DrawableElement de = this.drawingLayer.getSelectedDE();
/*      */ 
/* 2142 */     DrawableElement newEl = null;
/*      */ 
/* 2144 */     Contours oldContours = null;
/*      */ 
/* 2146 */     if ((de != null) && ((de.getParent() instanceof ContourCircle))) {
/* 2147 */       newEl = (DrawableElement)de.copy();
/* 2148 */       oldContours = (Contours)de.getParent().getParent();
/*      */     }
/*      */     else {
/* 2151 */       oldContours = this.currentContours;
/*      */     }
/*      */ 
/* 2155 */     if (oldContours != null)
/*      */     {
/* 2157 */       Iterator iterator = oldContours.getComponentIterator();
/*      */ 
/* 2159 */       Contours newContours = new Contours();
/*      */ 
/* 2164 */       while (iterator.hasNext())
/*      */       {
/* 2166 */         AbstractDrawableComponent oldAdc = (AbstractDrawableComponent)iterator.next();
/* 2167 */         AbstractDrawableComponent newAdc = oldAdc.copy();
/*      */ 
/* 2169 */         if ((newAdc instanceof ContourCircle)) {
/* 2170 */           if (updateAllCircleAttr().booleanValue()) {
/* 2171 */             Arc oneCircle = (Arc)((ContourCircle)newAdc).getCircle();
/* 2172 */             oneCircle.update(this.circleTemplate);
/*      */           }
/*      */ 
/* 2175 */           if ((newEl != null) && (oldAdc.equals(de.getParent()))) {
/* 2176 */             newEl.setParent(newAdc);
/* 2177 */             if ((newEl instanceof Arc)) {
/* 2178 */               ((DECollection)newAdc).replace(((ContourCircle)newAdc).getCircle(), newEl);
/*      */             }
/*      */ 
/* 2181 */             ((ContourCircle)newAdc).getCircle().update(this.circleTemplate);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2187 */         newAdc.setParent(newContours);
/* 2188 */         newContours.add(newAdc);
/*      */       }
/*      */ 
/* 2194 */       newContours.update(oldContours);
/* 2195 */       this.drawingLayer.replaceElement(oldContours, newContours);
/*      */ 
/* 2200 */       this.currentContours = newContours;
/* 2201 */       if (this.tool != null) this.tool.setCurrentContour(newContours);
/*      */ 
/* 2203 */       if (newEl != null) {
/* 2204 */         this.drawingLayer.removeSelected();
/* 2205 */         this.drawingLayer.setSelected(newEl);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2210 */     if (this.mapEditor != null)
/* 2211 */       this.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   private void openG2GDlg()
/*      */   {
/* 2223 */     if (g2gDlg == null) {
/*      */       try
/*      */       {
/* 2226 */         g2gDlg = new GraphToGridParamDialog(
/* 2227 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/* 2228 */         g2gDlg.setCntAttrDlg(this);
/*      */       }
/*      */       catch (VizException e)
/*      */       {
/* 2232 */         e.printStackTrace();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2237 */     if (g2gDlg != null)
/* 2238 */       g2gDlg.open();
/*      */   }
/*      */ 
/*      */   private void retrieveIconType()
/*      */   {
/* 2248 */     PgenPaletteWindow plt = PgenSession.getInstance().getPgenPalette();
/*      */ 
/* 2251 */     if (this.lineIconType == null) {
/* 2252 */       this.lineIconType = new LinkedHashMap();
/*      */     }
/*      */ 
/* 2255 */     List lineObjNames = plt.getObjectNames("Lines");
/*      */ 
/* 2257 */     for (String str : lineObjNames) {
/* 2258 */       this.lineIconType.put(str, "Lines");
/*      */     }
/*      */ 
/* 2262 */     this.symbolItemMap = new LinkedHashMap();
/*      */ 
/* 2264 */     HashMap itemMap = plt.getItemMap();
/* 2265 */     for (String str : itemMap.keySet()) {
/* 2266 */       IConfigurationElement ifg = (IConfigurationElement)itemMap.get(str);
/* 2267 */       String type = ifg.getName();
/* 2268 */       if (type.equalsIgnoreCase("object")) {
/* 2269 */         String cls = ifg.getAttribute("className");
/* 2270 */         if ((cls.equals("Symbol")) && (!str.contains("TURBULENCE"))) {
/* 2271 */           this.symbolItemMap.put(str, ifg);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2277 */     for (String str : itemMap.keySet()) {
/* 2278 */       IConfigurationElement ifg = (IConfigurationElement)itemMap.get(str);
/* 2279 */       String type = ifg.getName();
/* 2280 */       if (type.equalsIgnoreCase("object")) {
/* 2281 */         String cls = ifg.getAttribute("className");
/* 2282 */         if (cls.equals("Marker"))
/* 2283 */           this.symbolItemMap.put(str, ifg);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Image getIcon(String iconName)
/*      */   {
/* 2300 */     PgenPaletteWindow plt = PgenSession.getInstance().getPgenPalette();
/*      */ 
/* 2302 */     return plt.getButtonImage(iconName);
/*      */   }
/*      */ 
/*      */   public boolean drawSymbol()
/*      */   {
/* 2311 */     return this.drawingStatus == ContourDrawingStatus.DRAW_SYMBOL;
/*      */   }
/*      */ 
/*      */   public void setDrawingSymbol()
/*      */   {
/* 2319 */     setDrawingStatus(ContourDrawingStatus.DRAW_SYMBOL);
/*      */   }
/*      */ 
/*      */   public void setActiveSymbol(DrawableElement elem)
/*      */   {
/* 2328 */     boolean found = false;
/* 2329 */     String symboltype = elem.getPgenType();
/* 2330 */     java.awt.Color clr = elem.getColors()[0];
/* 2331 */     for (Button btn : this.quickSymbolBtns)
/*      */     {
/* 2333 */       if (symboltype.equals(btn.getData().toString())) {
/* 2334 */         btn.setToolTipText(((IConfigurationElement)this.symbolItemMap.get(symboltype)).getAttribute("label"));
/* 2335 */         btn.setImage(getIcon(symboltype));
/* 2336 */         this.activeQuickSymbolBtn = btn;
/* 2337 */         setButtonColor(btn, this.defaultButtonColor, clr);
/* 2338 */         found = true;
/*      */       }
/*      */       else {
/* 2341 */         setButtonColor(btn, this.activeButtonColor, this.defaultButtonColor);
/*      */       }
/*      */     }
/*      */ 
/* 2345 */     if (!found) {
/* 2346 */       this.activeQuickSymbolBtn = ((Button)this.quickSymbolBtns.get(0));
/* 2347 */       this.activeQuickSymbolBtn.setData(symboltype);
/* 2348 */       this.activeQuickSymbolBtn.setImage(getIcon(symboltype));
/* 2349 */       setButtonColor(this.activeQuickSymbolBtn, this.defaultButtonColor, clr);
/*      */     }
/*      */ 
/* 2352 */     this.contoursAttrSettings.put(symboltype, elem);
/*      */   }
/*      */ 
/*      */   public String getActiveSymbolObjType()
/*      */   {
/* 2360 */     return this.activeQuickSymbolBtn.getData().toString();
/*      */   }
/*      */ 
/*      */   public String getActiveSymbolClass()
/*      */   {
/* 2367 */     return ((IConfigurationElement)this.symbolItemMap.get(getActiveSymbolObjType())).getAttribute("className");
/*      */   }
/*      */ 
/*      */   public void setDrawingLine()
/*      */   {
/* 2375 */     setDrawingStatus(ContourDrawingStatus.DRAW_LINE);
/*      */   }
/*      */ 
/*      */   public void setContourLineType(String str)
/*      */   {
/* 2382 */     this.lineTypeBtn.setData(str);
/* 2383 */     this.lineTypeBtn.setImage(getIcon(str));
/*      */   }
/*      */ 
/*      */   public String getContourLineType()
/*      */   {
/* 2390 */     return this.lineTypeBtn.getData().toString();
/*      */   }
/*      */ 
/*      */   public boolean drawCircle()
/*      */   {
/* 2397 */     return this.drawingStatus == ContourDrawingStatus.DRAW_CIRCLE;
/*      */   }
/*      */ 
/*      */   public void setDrawingCircle()
/*      */   {
/* 2404 */     setDrawingStatus(ContourDrawingStatus.DRAW_CIRCLE);
/*      */   }
/*      */ 
/*      */   public boolean hideCircleLabel()
/*      */   {
/* 2411 */     return this.hideCircleLabelBtn.getSelection();
/*      */   }
/*      */ 
/*      */   public void setHideCircleLabel(boolean hide)
/*      */   {
/* 2418 */     this.hideCircleLabelBtn.setSelection(hide);
/*      */   }
/*      */ 
/*      */   private void openSymbolPanel()
/*      */   {
/* 2691 */     if (this.symbolTypePanel == null) {
/* 2692 */       List objs = new ArrayList(this.symbolItemMap.keySet());
/* 2693 */       Shell sh = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/*      */ 
/* 2695 */       this.symbolTypePanel = new SymbolTypeSelectionDlg(sh, objs, this.activeQuickSymbolBtn, 
/* 2696 */         "Select Contours Quick Access Symbol");
/*      */     }
/*      */ 
/* 2699 */     if (this.symbolTypePanel != null) {
/* 2700 */       this.symbolTypePanel.setActivator(this.activeQuickSymbolBtn);
/* 2701 */       this.symbolTypePanel.open();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void openLineTypePanel()
/*      */   {
/* 2711 */     if (this.lineTypePanel == null) {
/* 2712 */       List objs = new ArrayList(this.lineIconType.keySet());
/* 2713 */       Shell sh = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/* 2714 */       this.lineTypePanel = new LineTypeSelectionDlg(sh, objs, this.lineTypeBtn, 
/* 2715 */         "Contours Line Types");
/*      */     }
/*      */ 
/* 2718 */     if (this.lineTypePanel != null) {
/* 2719 */       this.lineTypePanel.setActivator(this.lineTypeBtn);
/* 2720 */       this.lineTypePanel.open();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateMinmaxAttributes()
/*      */   {
/* 2824 */     DrawableElement de = this.drawingLayer.getSelectedDE();
/*      */ 
/* 2834 */     DrawableElement newEl = null;
/*      */ 
/* 2836 */     Contours oldContours = null;
/*      */ 
/* 2838 */     if ((de != null) && ((de.getParent() instanceof ContourMinmax))) {
/* 2839 */       newEl = (DrawableElement)de.copy();
/* 2840 */       oldContours = (Contours)de.getParent().getParent();
/*      */     }
/*      */     else {
/* 2843 */       oldContours = this.currentContours;
/*      */     }
/*      */ 
/* 2847 */     if (oldContours != null)
/*      */     {
/* 2849 */       Iterator iterator = oldContours.getComponentIterator();
/*      */ 
/* 2851 */       Contours newContours = new Contours();
/*      */ 
/* 2856 */       while (iterator.hasNext())
/*      */       {
/* 2858 */         AbstractDrawableComponent oldAdc = (AbstractDrawableComponent)iterator.next();
/* 2859 */         AbstractDrawableComponent newAdc = oldAdc.copy();
/*      */ 
/* 2861 */         if ((newAdc instanceof ContourMinmax)) {
/* 2862 */           if (updateAllMinmaxAttr().booleanValue()) {
/* 2863 */             Symbol oneSymb = (Symbol)((ContourMinmax)newAdc).getSymbol();
/* 2864 */             oneSymb.update(this.minmaxTemplate);
/*      */           }
/*      */ 
/* 2867 */           if ((newEl != null) && (oldAdc.equals(de.getParent()))) {
/* 2868 */             newEl.setParent(newAdc);
/* 2869 */             if ((newEl instanceof Symbol)) {
/* 2870 */               ((DECollection)newAdc).replace(((ContourMinmax)newAdc).getSymbol(), newEl);
/*      */             }
/*      */ 
/* 2873 */             ((ContourMinmax)newAdc).getSymbol().update(this.minmaxTemplate);
/*      */           }
/*      */         }
/*      */ 
/* 2877 */         newAdc.setParent(newContours);
/* 2878 */         newContours.add(newAdc);
/*      */       }
/*      */ 
/* 2884 */       newContours.update(oldContours);
/* 2885 */       this.drawingLayer.replaceElement(oldContours, newContours);
/*      */ 
/* 2890 */       this.currentContours = newContours;
/* 2891 */       if (this.tool != null) this.tool.setCurrentContour(newContours);
/*      */ 
/* 2893 */       if (newEl != null) {
/* 2894 */         this.drawingLayer.removeSelected();
/* 2895 */         this.drawingLayer.setSelected(newEl);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2900 */     if (this.mapEditor != null)
/* 2901 */       this.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   private void setButtonColor(Button btn, java.awt.Color clr)
/*      */   {
/* 2911 */     btn.setBackground(new org.eclipse.swt.graphics.Color(getShell().getDisplay(), 
/* 2912 */       clr.getRed(), clr.getGreen(), clr.getBlue()));
/*      */   }
/*      */ 
/*      */   private void setButtonColor(Button btn, java.awt.Color fclr, java.awt.Color bclr)
/*      */   {
/* 2921 */     if (btn != null) {
/* 2922 */       btn.setBackground(new org.eclipse.swt.graphics.Color(getShell().getDisplay(), 
/* 2923 */         bclr.getRed(), bclr.getGreen(), bclr.getBlue()));
/*      */ 
/* 2925 */       btn.setForeground(new org.eclipse.swt.graphics.Color(getShell().getDisplay(), 
/* 2926 */         fclr.getRed(), fclr.getGreen(), fclr.getBlue()));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDrawingStatus(ContourDrawingStatus st)
/*      */   {
/* 2935 */     if (this.tool != null) {
/* 2936 */       this.tool.clearSelected();
/*      */     }
/*      */ 
/* 2939 */     switch (st)
/*      */     {
/*      */     case DRAW_LINE:
/* 2943 */       setButtonColor(this.lineTypeBtn, this.activeButtonColor, this.defaultButtonColor);
/* 2944 */       setButtonColor(this.circleTypeBtn, this.activeButtonColor, this.defaultButtonColor);
/* 2945 */       setButtonColor(this.selectLineBtn, this.defaultButtonColor);
/*      */ 
/* 2947 */       this.drawingStatus = ContourDrawingStatus.DRAW_SYMBOL;
/* 2948 */       if (!(this.tool.getMouseHandler() instanceof PgenContoursTool.PgenContoursHandler)) {
/* 2949 */         this.tool.setPgenContoursHandler();
/*      */       }
/* 2951 */       break;
/*      */     case DRAW_SYMBOL:
/* 2955 */       setButtonColor(this.lineTypeBtn, this.activeButtonColor, this.defaultButtonColor);
/* 2956 */       setButtonColor(this.activeQuickSymbolBtn, this.activeButtonColor, this.defaultButtonColor);
/* 2957 */       setButtonColor(this.selectLineBtn, this.defaultButtonColor);
/*      */ 
/* 2959 */       this.drawingStatus = ContourDrawingStatus.DRAW_CIRCLE;
/* 2960 */       if (!(this.tool.getMouseHandler() instanceof PgenContoursTool.PgenContoursHandler)) {
/* 2961 */         this.tool.setPgenContoursHandler();
/*      */       }
/*      */ 
/* 2964 */       break;
/*      */     case DRAW_CIRCLE:
/* 2968 */       setButtonColor(this.activeQuickSymbolBtn, this.activeButtonColor, this.defaultButtonColor);
/* 2969 */       setButtonColor(this.circleTypeBtn, this.activeButtonColor, this.defaultButtonColor);
/* 2970 */       setButtonColor(this.selectLineBtn, this.defaultButtonColor);
/*      */ 
/* 2972 */       this.lineClosedBtn.setSelection(this.drawClosedLine);
/* 2973 */       this.drawingStatus = ContourDrawingStatus.DRAW_LINE;
/* 2974 */       if ((this.tool != null) && (!(this.tool.getMouseHandler() instanceof PgenContoursTool.PgenContoursHandler))) {
/* 2975 */         this.tool.setPgenContoursHandler();
/*      */       }
/* 2977 */       break;
/*      */     case SELECT:
/* 2980 */       setSelectMode();
/* 2981 */       break;
/*      */     default:
/* 2985 */       setButtonColor(this.activeQuickSymbolBtn, this.activeButtonColor, this.defaultButtonColor);
/* 2986 */       setButtonColor(this.circleTypeBtn, this.activeButtonColor, this.defaultButtonColor);
/*      */ 
/* 2988 */       this.lineClosedBtn.setSelection(this.drawClosedLine);
/* 2989 */       this.drawingStatus = ContourDrawingStatus.DRAW_LINE;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSelectMode()
/*      */   {
/* 3000 */     setButtonColor(this.activeQuickSymbolBtn, this.activeButtonColor, this.defaultButtonColor);
/* 3001 */     setButtonColor(this.circleTypeBtn, this.activeButtonColor, this.defaultButtonColor);
/* 3002 */     setButtonColor(this.lineTypeBtn, this.activeButtonColor, this.defaultButtonColor);
/* 3003 */     setButtonColor(this.selectLineBtn, this.activeButtonColor);
/*      */ 
/* 3005 */     this.drawingStatus = ContourDrawingStatus.SELECT;
/*      */ 
/* 3007 */     if (!(this.tool.getMouseHandler() instanceof PgenSelectHandler))
/* 3008 */       this.tool.setPgenSelectHandler();
/*      */   }
/*      */ 
/*      */   private LinkedHashMap<String, Boolean> getQuickSymbols()
/*      */   {
/* 3021 */     LinkedHashMap lbls = new LinkedHashMap();
/* 3022 */     String xpath = ContoursInfoDlg.CNTRINFO_XPATH + "[@name='QuickSymbols']";
/*      */ 
/* 3024 */     Document dm = ContoursInfoDlg.readInfoTbl();
/*      */ 
/* 3026 */     int selected = 0;
/* 3027 */     if (dm != null) {
/* 3028 */       Node cntrInfo = dm.selectSingleNode(xpath);
/* 3029 */       List nodes = cntrInfo.selectNodes("object");
/* 3030 */       for (Node node : nodes) {
/* 3031 */         String quick = node.valueOf("@quickAccess");
/* 3032 */         if ((quick != null) && (quick.trim().equalsIgnoreCase("true"))) {
/* 3033 */           lbls.put(node.valueOf("@name"), Boolean.valueOf(true));
/* 3034 */           selected++;
/*      */         }
/*      */         else {
/* 3037 */           lbls.put(node.valueOf("@name"), Boolean.valueOf(false));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3042 */     this.numOfQuickSymbols = selected;
/*      */ 
/* 3044 */     if (selected == 0) {
/* 3045 */       lbls.put("FILLED_HIGH_PRESSURE_H", Boolean.valueOf(true));
/* 3046 */       lbls.put("FILLED_LOW_PRESSURE_L", Boolean.valueOf(true));
/* 3047 */       this.numOfQuickSymbols = 2;
/*      */     }
/*      */ 
/* 3050 */     return lbls;
/*      */   }
/*      */ 
/*      */   private void retrieveContoursSettings()
/*      */   {
/* 3062 */     if (this.contoursAttrSettings == null) {
/* 3063 */       this.contoursAttrSettings = new HashMap();
/*      */ 
/* 3066 */       for (String str : this.quickSymbolType.keySet()) {
/* 3067 */         this.contoursAttrSettings.put(str, retrieveDefaultSettings(str));
/*      */       }
/*      */ 
/* 3076 */       AbstractDrawableComponent adc = retrieveDefaultSettings("Contours");
/* 3077 */       boolean lineFound = false;
/* 3078 */       boolean labelFound = false;
/* 3079 */       boolean circleFound = false;
/* 3080 */       if ((adc != null) && ((adc instanceof Contours))) {
/* 3081 */         List cline = ((Contours)adc).getContourLines();
/* 3082 */         if ((cline != null) && (cline.size() > 0)) {
/* 3083 */           Line ln = ((ContourLine)cline.get(0)).getLine();
/* 3084 */           if (ln != null) {
/* 3085 */             this.contoursAttrSettings.put(ln.getPgenType(), ln.copy());
/* 3086 */             lineFound = true;
/*      */           }
/*      */ 
/* 3089 */           if ((((ContourLine)cline.get(0)).getLabels() != null) && 
/* 3090 */             (((ContourLine)cline.get(0)).getLabels().size() > 0)) {
/* 3091 */             labelFound = true;
/* 3092 */             this.contoursAttrSettings.put(((gov.noaa.nws.ncep.ui.pgen.elements.Text)((ContourLine)cline.get(0)).getLabels().get(0)).getPgenType(), 
/* 3093 */               ((gov.noaa.nws.ncep.ui.pgen.elements.Text)((ContourLine)cline.get(0)).getLabels().get(0)).copy());
/*      */           }
/*      */         }
/*      */ 
/* 3097 */         List csymbols = ((Contours)adc).getContourMinmaxs();
/* 3098 */         if ((csymbols != null) && (csymbols.size() > 0)) {
/* 3099 */           for (ContourMinmax cmx : csymbols) {
/* 3100 */             this.contoursAttrSettings.put(cmx.getSymbol().getPgenType(), 
/* 3101 */               cmx.getSymbol().copy());
/* 3102 */             if (!labelFound) {
/* 3103 */               this.contoursAttrSettings.put(cmx.getLabel().getPgenType(), 
/* 3104 */                 cmx.getLabel());
/* 3105 */               labelFound = true;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 3110 */         List ccircles = ((Contours)adc).getContourCircles();
/* 3111 */         if ((ccircles != null) && (ccircles.size() > 0)) {
/* 3112 */           Arc cc = (Arc)((ContourCircle)ccircles.get(0)).getCircle();
/* 3113 */           if (cc != null) {
/* 3114 */             this.contoursAttrSettings.put(cc.getPgenType(), cc.copy());
/* 3115 */             circleFound = true;
/*      */           }
/*      */ 
/* 3118 */           if ((!labelFound) && 
/* 3119 */             (((ContourCircle)ccircles.get(0)).getLabel() != null)) {
/* 3120 */             labelFound = true;
/* 3121 */             this.contoursAttrSettings.put(((ContourCircle)ccircles.get(0)).getLabel().getPgenType(), 
/* 3122 */               ((ContourCircle)ccircles.get(0)).getLabel().copy());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3129 */       if (!lineFound) {
/* 3130 */         Line dln = new Line(null, new java.awt.Color[] { java.awt.Color.red }, 
/* 3131 */           2.0F, 2.0D, false, false, null, 2, 
/* 3132 */           FillPatternList.FillPattern.SOLID, "Lines", "LINE_SOLID");
/*      */ 
/* 3134 */         this.contoursAttrSettings.put("LINE_SOLID", dln);
/*      */       }
/*      */ 
/* 3137 */       if (!labelFound) {
/* 3138 */         gov.noaa.nws.ncep.ui.pgen.elements.Text txt = new gov.noaa.nws.ncep.ui.pgen.elements.Text(null, 
/* 3139 */           "Courier", 14.0F, IText.TextJustification.CENTER, 
/* 3140 */           null, 0.0D, IText.TextRotation.SCREEN_RELATIVE, 
/* 3141 */           new String[] { "text" }, 
/* 3142 */           IText.FontStyle.REGULAR, java.awt.Color.GREEN, 0, 0, true, IText.DisplayType.NORMAL, 
/* 3143 */           "Text", "General Text");
/*      */ 
/* 3145 */         this.contoursAttrSettings.put("General Text", txt);
/*      */       }
/*      */ 
/* 3148 */       if (!circleFound)
/*      */       {
/* 3150 */         Arc ccr = new Arc(null, java.awt.Color.red, 
/* 3151 */           2.0F, 2.0D, false, false, 2, FillPatternList.FillPattern.SOLID, "Circle", 
/* 3152 */           null, null, "Arc", 1.0D, 0.0D, 360.0D);
/*      */ 
/* 3154 */         this.contoursAttrSettings.put("Circle", ccr);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private AbstractDrawableComponent retrieveDefaultSettings(String pgenType)
/*      */   {
/* 3166 */     return (AbstractDrawableComponent)AttrSettings.getInstance().getSettings().get(pgenType);
/*      */   }
/*      */ 
/*      */   private String retrieveDefaultLineType()
/*      */   {
/* 3175 */     retrieveContoursSettings();
/* 3176 */     String type = new String("LINE_SOLID");
/* 3177 */     for (AbstractDrawableComponent adc : this.contoursAttrSettings.values()) {
/* 3178 */       if (((adc instanceof Line)) && (!(adc instanceof Arc))) {
/* 3179 */         type = new String(adc.getPgenType());
/* 3180 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 3184 */     return type;
/*      */   }
/*      */ 
/*      */   public Coordinate[] getLinePoints()
/*      */   {
/* 3190 */     return null;
/*      */   }
/*      */ 
/*      */   public String getPatternName()
/*      */   {
/* 3196 */     return null;
/*      */   }
/*      */ 
/*      */   public int getSmoothFactor()
/*      */   {
/* 3202 */     return 0;
/*      */   }
/*      */ 
/*      */   public Boolean isFilled()
/*      */   {
/* 3208 */     return null;
/*      */   }
/*      */ 
/*      */   public FillPatternList.FillPattern getFillPattern()
/*      */   {
/* 3214 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDrawingTool(PgenContoursTool tool)
/*      */   {
/* 3222 */     this.tool = tool;
/*      */   }
/*      */ 
/*      */   public void cancelPressed()
/*      */   {
/* 3230 */     PgenUtil.setSelectingMode();
/* 3231 */     super.cancelPressed();
/*      */   }
/*      */ 
/*      */   private class ContourCircleAttrDlg extends ArcAttrDlg
/*      */   {
/*      */     private ContourCircleAttrDlg(Shell parShell)
/*      */       throws VizException
/*      */     {
/* 2062 */       super();
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/* 2074 */       ContoursAttrDlg.this.circleTemplate = ((Arc)new DrawableElementFactory().create(DrawableType.ARC, 
/* 2075 */         this, "Arc", "Circle", null, null));
/*      */ 
/* 2077 */       if (ContoursAttrDlg.this.drawCircle()) {
/* 2078 */         ContoursAttrDlg.this.setButtonColor(ContoursAttrDlg.this.circleTypeBtn, ContoursAttrDlg.this.defaultButtonColor, ContoursAttrDlg.this.circleTemplate.getColors()[0]);
/*      */       }
/*      */ 
/* 2084 */       ContoursAttrDlg.this.updateCircleAttributes();
/*      */ 
/* 2086 */       close();
/*      */     }
/*      */ 
/*      */     public void cancelPressed()
/*      */     {
/* 2094 */       close();
/*      */     }
/*      */ 
/*      */     private void disableWidgets()
/*      */     {
/* 2102 */       this.axisRatioLbl.setEnabled(false);
/* 2103 */       this.axisRatioSlider.setEnabled(false);
/* 2104 */       this.axisRatioText.setEnabled(false);
/*      */ 
/* 2106 */       this.startAngleLbl.setEnabled(false);
/* 2107 */       this.startAngleSlider.setEnabled(false);
/* 2108 */       this.startAngleText.setEnabled(false);
/*      */ 
/* 2110 */       this.endAngleLbl.setEnabled(false);
/* 2111 */       this.endAngleSlider.setEnabled(false);
/* 2112 */       this.endAngleText.setEnabled(false);
/*      */     }
/*      */ 
/*      */     private void initDlg()
/*      */     {
/* 2119 */       getShell().setText("Contour Circle Attributes");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum ContourDrawingStatus
/*      */   {
/*  120 */     DRAW_LINE, DRAW_SYMBOL, DRAW_CIRCLE, SELECT;
/*      */   }
/*      */ 
/*      */   private class ContourLineAttrDlg extends LineAttrDlg
/*      */   {
/*      */     private ContourLineAttrDlg(Shell parShell)
/*      */       throws VizException
/*      */     {
/* 1903 */       super();
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/* 1915 */       ContoursAttrDlg.this.lineTemplate = ((Line)new DrawableElementFactory().create(DrawableType.LINE, 
/* 1916 */         this, "Line", "LINE_SOLID", null, null));
/*      */ 
/* 1918 */       ContoursAttrDlg.this.lineTemplate.setClosed(isClosedLine());
/*      */ 
/* 1920 */       if (ContoursAttrDlg.this.drawContourLine()) {
/* 1921 */         ContoursAttrDlg.this.setButtonColor(ContoursAttrDlg.this.lineTypeBtn, ContoursAttrDlg.this.defaultButtonColor, ContoursAttrDlg.this.lineTemplate.getColors()[0]);
/*      */       }
/*      */ 
/* 1927 */       ContoursAttrDlg.this.updateLineAttributes();
/* 1928 */       close();
/*      */     }
/*      */ 
/*      */     public void cancelPressed()
/*      */     {
/* 1936 */       close();
/*      */     }
/*      */ 
/*      */     private void disableWidgets()
/*      */     {
/* 1944 */       this.closedBtn.setEnabled(false);
/*      */     }
/*      */ 
/*      */     private void initDlg()
/*      */     {
/* 1951 */       getShell().setText("Contour Line Attributes");
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ContourMinmaxAttrDlg extends LabeledSymbolAttrDlg
/*      */   {
/* 2732 */     private PgenContoursTool tool = null;
/*      */ 
/*      */     private ContourMinmaxAttrDlg(Shell parShell) throws VizException
/*      */     {
/* 2736 */       super();
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/* 2748 */       ContoursAttrDlg.this.minmaxTemplate = ((Symbol)new DrawableElementFactory().create(DrawableType.SYMBOL, 
/* 2749 */         this, "Symbol", ContoursAttrDlg.this.getActiveSymbolObjType(), null, null));
/* 2750 */       ContoursAttrDlg.this.contoursAttrSettings.put(ContoursAttrDlg.this.getActiveSymbolObjType(), ContoursAttrDlg.this.minmaxTemplate);
/*      */ 
/* 2753 */       if (ContoursAttrDlg.this.drawSymbol()) {
/* 2754 */         ContoursAttrDlg.this.setButtonColor(ContoursAttrDlg.this.activeQuickSymbolBtn, ContoursAttrDlg.this.defaultButtonColor, getColors()[0]);
/*      */       }
/*      */ 
/* 2760 */       ContoursAttrDlg.this.updateMinmaxAttributes();
/* 2761 */       close();
/*      */     }
/*      */ 
/*      */     public void cancelPressed()
/*      */     {
/* 2770 */       close();
/*      */     }
/*      */ 
/*      */     private void initDlg()
/*      */     {
/* 2777 */       getShell().setText("Contour Min/Max Attributes");
/* 2778 */       super.setLabelChkBox(false);
/* 2779 */       AbstractPgenTool apt = (AbstractPgenTool)VizPerspectiveListener.getCurrentPerspectiveManager().getToolManager().getSelectedModalTool("gov.noaa.nws.ncep.viz.ui.modalTool");
/* 2780 */       if ((apt instanceof PgenContoursTool)) this.tool = ((PgenContoursTool)apt);
/* 2781 */       if (this.tool != null) {
/* 2782 */         this.tool.resetUndoRedoCount();
/* 2783 */         PgenSession.getInstance().getCommandManager().addStackListener(this.tool);
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean close()
/*      */     {
/* 2789 */       if (this.tool != null) {
/* 2790 */         this.tool.resetUndoRedoCount();
/* 2791 */         PgenSession.getInstance().getCommandManager().removeStackListener(this.tool);
/*      */       }
/* 2793 */       return super.close();
/*      */     }
/*      */ 
/*      */     protected void placeSymbol()
/*      */     {
/* 2802 */       if (this.tool != null)
/*      */       {
/* 2804 */         ((PgenContoursTool.PgenContoursHandler)this.tool.getMouseHandler()).drawContourMinmax(
/* 2805 */           new Coordinate(Double.parseDouble(this.longitudeText.getText()), 
/* 2806 */           Double.parseDouble(this.latitudeText.getText())));
/* 2807 */         this.placeBtn.setEnabled(false);
/* 2808 */         this.undoBtn.setEnabled(true);
/* 2809 */         this.undoBtn.setText("Undo Symbol");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class LabelAttrDlg extends TextAttrDlg
/*      */   {
/*      */     private LabelAttrDlg(Shell parShell)
/*      */       throws VizException
/*      */     {
/* 1661 */       super();
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/* 1674 */       ContoursAttrDlg.this.labelTemplate = ((gov.noaa.nws.ncep.ui.pgen.elements.Text)new DrawableElementFactory().create(DrawableType.TEXT, 
/* 1675 */         this, "Text", "General Text", null, null));
/*      */ 
/* 1677 */       ContoursAttrDlg.this.labelTemplate.setText(new String[] { ContoursAttrDlg.this.getLabel() });
/*      */ 
/* 1682 */       ContoursAttrDlg.this.updateLabelAttributes();
/*      */ 
/* 1684 */       close();
/*      */     }
/*      */ 
/*      */     public void cancelPressed()
/*      */     {
/* 1693 */       if (ContoursAttrDlg.this.tool != null) {
/* 1694 */         ContoursAttrDlg.this.tool.setPgenContoursHandler();
/*      */       }
/* 1696 */       PgenUtil.setSelectingMode();
/* 1697 */       close();
/*      */     }
/*      */ 
/*      */     private void disableWidgets()
/*      */     {
/* 1705 */       this.text.setEnabled(false);
/* 1706 */       this.textLabel.setEnabled(false);
/*      */     }
/*      */ 
/*      */     private void initDlg()
/*      */     {
/* 1713 */       getShell().setText("Contour Label Attributes");
/* 1714 */       setBoxText(true, IText.DisplayType.NORMAL);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class LineTypeSelectionDlg extends Dialog
/*      */   {
/*      */     private List<String> objNames;
/*      */     private String selectedType;
/*      */     private Button activator;
/*      */     private String title;
/*      */     private Composite top;
/*      */ 
/*      */     public LineTypeSelectionDlg(List<String> parent, Button objNames, String activator)
/*      */     {
/* 2440 */       super();
/*      */ 
/* 2442 */       this.objNames = objNames;
/* 2443 */       this.activator = activator;
/* 2444 */       this.selectedType = activator.getData().toString();
/* 2445 */       this.title = title;
/*      */     }
/*      */ 
/*      */     public void createButtonsForButtonBar(Composite parent)
/*      */     {
/* 2455 */       super.createButtonsForButtonBar(parent);
/* 2456 */       getButton(0).setText("Accept");
/*      */     }
/*      */ 
/*      */     public void setActivator(Button activator)
/*      */     {
/* 2464 */       this.activator = activator;
/*      */     }
/*      */ 
/*      */     public Control createDialogArea(Composite parent)
/*      */     {
/* 2473 */       this.top = ((Composite)super.createDialogArea(parent));
/* 2474 */       getShell().setText(this.title);
/*      */ 
/* 2476 */       GridLayout mainLayout = new GridLayout(16, false);
/* 2477 */       mainLayout.marginHeight = 3;
/* 2478 */       mainLayout.marginWidth = 3;
/* 2479 */       mainLayout.horizontalSpacing = 0;
/* 2480 */       mainLayout.verticalSpacing = 0;
/* 2481 */       this.top.setLayout(mainLayout);
/*      */       java.awt.Color clr;
/*      */       final java.awt.Color clr;
/* 2484 */       if (ContoursAttrDlg.this.lineTemplate != null) {
/* 2485 */         clr = ContoursAttrDlg.this.lineTemplate.getColors()[0];
/*      */       }
/*      */       else {
/* 2488 */         clr = ContoursAttrDlg.this.activeButtonColor;
/*      */       }
/*      */ 
/* 2491 */       for (String str : this.objNames) {
/* 2492 */         Button btn = new Button(this.top, 8);
/* 2493 */         btn.setData(str);
/* 2494 */         btn.setImage(ContoursAttrDlg.this.getIcon(str));
/*      */ 
/* 2496 */         if (str.equals(this.activator.getData().toString())) {
/* 2497 */           ContoursAttrDlg.this.setButtonColor(btn, clr);
/*      */         }
/*      */         else {
/* 2500 */           ContoursAttrDlg.this.setButtonColor(btn, ContoursAttrDlg.this.defaultButtonColor);
/*      */         }
/*      */ 
/* 2503 */         btn.addListener(3, new Listener()
/*      */         {
/*      */           public void handleEvent(Event event)
/*      */           {
/* 2508 */             Control[] wids = ContoursAttrDlg.LineTypeSelectionDlg.this.top.getChildren();
/*      */ 
/* 2510 */             if (wids != null) {
/* 2511 */               for (int kk = 0; kk < wids.length; kk++) {
/* 2512 */                 ContoursAttrDlg.this.setButtonColor((Button)wids[kk], ContoursAttrDlg.this.defaultButtonColor);
/*      */               }
/*      */             }
/*      */ 
/* 2516 */             String objstr = event.widget.getData().toString();
/* 2517 */             ContoursAttrDlg.LineTypeSelectionDlg.this.selectedType = objstr;
/* 2518 */             ContoursAttrDlg.this.setButtonColor((Button)event.widget, clr);
/*      */           }
/*      */ 
/*      */         });
/*      */       }
/*      */ 
/* 2525 */       return this.top;
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/* 2532 */       this.activator.setData(this.selectedType);
/* 2533 */       this.activator.setImage(ContoursAttrDlg.this.getIcon(this.selectedType));
/* 2534 */       close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public class SymbolTypeSelectionDlg extends Dialog
/*      */   {
/*      */     private Composite top;
/*      */     private List<String> objNames;
/*      */     private String selectedType;
/*      */     private Button activator;
/*      */     private String title;
/*      */ 
/*      */     public SymbolTypeSelectionDlg(List<String> parent, Button objNames, String activator)
/*      */     {
/* 2558 */       super();
/*      */ 
/* 2560 */       this.objNames = objNames;
/* 2561 */       this.activator = activator;
/* 2562 */       this.selectedType = activator.getData().toString();
/* 2563 */       this.title = title;
/*      */     }
/*      */ 
/*      */     public void createButtonsForButtonBar(Composite parent)
/*      */     {
/* 2573 */       super.createButtonsForButtonBar(parent);
/* 2574 */       getButton(0).setText("Accept");
/*      */     }
/*      */ 
/*      */     public void setActivator(Button activator)
/*      */     {
/* 2582 */       this.activator = activator;
/*      */     }
/*      */ 
/*      */     public Control createDialogArea(Composite parent)
/*      */     {
/* 2591 */       this.top = ((Composite)super.createDialogArea(parent));
/* 2592 */       getShell().setText(this.title);
/*      */ 
/* 2594 */       GridLayout mainLayout = new GridLayout(16, false);
/* 2595 */       mainLayout.marginHeight = 3;
/* 2596 */       mainLayout.marginWidth = 3;
/* 2597 */       mainLayout.horizontalSpacing = 0;
/* 2598 */       mainLayout.verticalSpacing = 0;
/* 2599 */       this.top.setLayout(mainLayout);
/*      */ 
/* 2601 */       for (String str : this.objNames) {
/* 2602 */         Button btn = new Button(this.top, 8);
/* 2603 */         btn.setData(str);
/* 2604 */         btn.setImage(ContoursAttrDlg.this.getIcon(str));
/* 2605 */         btn.setToolTipText(((IConfigurationElement)ContoursAttrDlg.this.symbolItemMap.get(str)).getAttribute("label"));
/*      */ 
/* 2607 */         if (str.equals(this.activator.getData().toString())) {
/* 2608 */           java.awt.Color clr = ContoursAttrDlg.this.defaultButtonColor;
/* 2609 */           SinglePointElement ade = (SinglePointElement)ContoursAttrDlg.this.retrieveDefaultSettings(str);
/* 2610 */           if (ade != null) {
/* 2611 */             clr = ade.getColors()[0];
/*      */           }
/* 2613 */           ContoursAttrDlg.this.setButtonColor(btn, clr);
/*      */         }
/*      */         else {
/* 2616 */           ContoursAttrDlg.this.setButtonColor(btn, ContoursAttrDlg.this.defaultButtonColor);
/*      */         }
/*      */ 
/* 2619 */         btn.addListener(3, new Listener()
/*      */         {
/*      */           public void handleEvent(Event event)
/*      */           {
/* 2624 */             Control[] wids = ContoursAttrDlg.SymbolTypeSelectionDlg.this.top.getChildren();
/*      */ 
/* 2626 */             if (wids != null) {
/* 2627 */               for (int kk = 0; kk < wids.length; kk++) {
/* 2628 */                 ContoursAttrDlg.this.setButtonColor((Button)wids[kk], ContoursAttrDlg.this.defaultButtonColor);
/*      */               }
/*      */             }
/*      */ 
/* 2632 */             String objstr = event.widget.getData().toString();
/* 2633 */             ContoursAttrDlg.SymbolTypeSelectionDlg.this.selectedType = objstr;
/* 2634 */             java.awt.Color clr = ContoursAttrDlg.this.defaultButtonColor;
/*      */ 
/* 2639 */             SinglePointElement ade = (SinglePointElement)ContoursAttrDlg.this.retrieveDefaultSettings(objstr);
/* 2640 */             if (ade != null) {
/* 2641 */               clr = ade.getColors()[0];
/*      */             }
/*      */ 
/* 2645 */             ContoursAttrDlg.this.setButtonColor((Button)event.widget, clr);
/*      */           }
/*      */ 
/*      */         });
/*      */       }
/*      */ 
/* 2652 */       return this.top;
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/* 2671 */       java.awt.Color clr = ContoursAttrDlg.this.activeButtonColor;
/* 2672 */       SinglePointElement ade = (SinglePointElement)ContoursAttrDlg.this.retrieveDefaultSettings(this.selectedType);
/* 2673 */       if (ade != null) {
/* 2674 */         clr = ade.getColors()[0];
/*      */       }
/* 2676 */       ContoursAttrDlg.this.setButtonColor(this.activator, clr);
/*      */ 
/* 2678 */       this.activator.setData(this.selectedType);
/* 2679 */       this.activator.setImage(ContoursAttrDlg.this.getIcon(this.selectedType));
/*      */ 
/* 2681 */       close();
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.ContoursAttrDlg
 * JD-Core Version:    0.6.2
 */