/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.IGfa;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.PreloadGfaDataThread;
/*      */ import gov.noaa.nws.ncep.ui.pgen.layering.PgenLayeringControlDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*      */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductManageDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.TreeSet;
/*      */ import org.dom4j.Node;
/*      */ import org.eclipse.jface.dialogs.IDialogConstants;
/*      */ import org.eclipse.jface.util.IPropertyChangeListener;
/*      */ import org.eclipse.jface.util.PropertyChangeEvent;
/*      */ import org.eclipse.swt.events.FocusAdapter;
/*      */ import org.eclipse.swt.events.FocusEvent;
/*      */ import org.eclipse.swt.events.ModifyEvent;
/*      */ import org.eclipse.swt.events.ModifyListener;
/*      */ import org.eclipse.swt.events.SelectionAdapter;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.events.TraverseEvent;
/*      */ import org.eclipse.swt.events.TraverseListener;
/*      */ import org.eclipse.swt.events.VerifyEvent;
/*      */ import org.eclipse.swt.events.VerifyListener;
/*      */ import org.eclipse.swt.graphics.Image;
/*      */ import org.eclipse.swt.graphics.Point;
/*      */ import org.eclipse.swt.graphics.RGB;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ import org.eclipse.swt.widgets.Widget;
/*      */ 
/*      */ public class GfaAttrDlg extends LineAttrDlg
/*      */   implements IGfa
/*      */ {
/*   80 */   private static GfaAttrDlg instance = null;
/*      */   public static final String PGEN_RED_CROSS_IMG = "red_cross.png";
/*   87 */   private final String[] LABELS = { "Hazard:", "Fcst Hr:", "Tag:", "Desk:", "Issue Type:", "", "" };
/*      */   private Combo hazardCbo;
/*      */   protected Combo fcstHrCbo;
/*      */   private Combo tagCbo;
/*      */   private Combo deskCbo;
/*      */   private Combo issueTypeCbo;
/*      */   private Button moveTextBtn;
/*      */   private Label emptyLabel;
/*      */   private Text otherText;
/*      */   private Text textVOR;
/*      */   private Text type;
/*      */   private Text areaText;
/*      */   private Text beginningText;
/*      */   private Text endingText;
/*      */   private Button statesBtn;
/*      */   private static boolean statesBtnEnabled;
/*      */   private static org.eclipse.swt.graphics.Color statesBtnBackground;
/*      */   private Text statesText;
/*      */   private static int hazardIndexLastUsed;
/*      */   private static int fcstHrIndexLastUsed;
/*      */   private static int deskIndexLastUsed;
/*      */   private static int issueTypeIndexLastUsed;
/*      */   private static String tagLastUsed;
/*      */   private static String typeLastUsed;
/*      */   private static RGB rgbLastUsed;
/*      */   private Gfa lastUsedGfa;
/*      */   private String otherTextLastUsed;
/*      */   private Group panelComboGroup;
/*      */   private Group hazardSpecificGroup;
/*      */   private Group bottomGroup;
/*  122 */   private LinkedHashMap<String, Boolean> typeCheckboxes = new LinkedHashMap();
/*  123 */   private String addRemoveBtnLabel = "";
/*  124 */   private LinkedHashMap<String, Boolean> addRemoveDlgCheckboxes = new LinkedHashMap();
/*      */ 
/*  127 */   private HashMap<String, String> values = new HashMap();
/*  128 */   private HashMap<String, Widget> widgets = new HashMap();
/*      */ 
/*  130 */   private final int TEXT_WIDTH = 450;
/*  131 */   private final int TEXT_HEIGHT = 25;
/*      */   private AddRemoveTypeDlg addRemoveTypeDlg;
/*  134 */   private ArrayList<String> popupCheckboxes = new ArrayList();
/*  135 */   private HashMap<String, Boolean> requiredFields = new HashMap();
/*  136 */   private HashMap<String, Label> requiredCrosses = new HashMap();
/*      */   private Label warning;
/*  140 */   private final String RED_CROSS_PATH = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/*  141 */     PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "red_cross.png");
/*      */ 
/*  144 */   private final String TIME = "[01234569]?|12|[012345]?:|[012345]?:(0|1|3|4|00|15|30|45)";
/*      */ 
/*      */   private GfaAttrDlg(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  155 */     super(parShell);
/*      */   }
/*      */ 
/*      */   public static GfaAttrDlg getInstance(Shell parShell)
/*      */   {
/*  167 */     if (instance == null) {
/*      */       try {
/*  169 */         instance = new GfaAttrDlg(parShell);
/*      */       }
/*      */       catch (VizException e) {
/*  172 */         e.printStackTrace();
/*      */       }
/*      */     }
/*  175 */     return instance;
/*      */   }
/*      */ 
/*      */   protected void initializeComponents()
/*      */   {
/*  184 */     getShell().setText("GFA Attributes");
/*      */ 
/*  186 */     if (!PreloadGfaDataThread.loaded)
/*      */     {
/*  188 */       new PreloadGfaDataThread().start();
/*      */     }
/*      */ 
/*  192 */     GridLayout mainLayout = new GridLayout(2, false);
/*  193 */     mainLayout.marginHeight = 3;
/*  194 */     mainLayout.marginWidth = 3;
/*  195 */     this.top.setLayout(mainLayout);
/*      */ 
/*  197 */     Group comboGroup = createPanelCombo();
/*  198 */     comboGroup.setLayoutData(new GridData(16384, 1, false, true));
/*      */ 
/*  200 */     this.hazardSpecificGroup = new Group(this.top, 2048);
/*  201 */     this.hazardSpecificGroup.setLayoutData(new GridData(16384, 1, false, true));
/*  202 */     createHazardSpecificPanel();
/*      */ 
/*  204 */     createBottomPanel();
/*      */ 
/*  206 */     addSelectionListeners();
/*      */ 
/*  208 */     populateTagCbo();
/*      */   }
/*      */ 
/*      */   private Group createPanelCombo()
/*      */   {
/*  216 */     this.panelComboGroup = new Group(this.top, 2048);
/*  217 */     GridLayout layout = new GridLayout(7, false);
/*  218 */     layout.marginHeight = 3;
/*  219 */     layout.marginWidth = 3;
/*  220 */     this.panelComboGroup.setLayout(layout);
/*      */ 
/*  222 */     Label lbl = null;
/*  223 */     for (String s : this.LABELS) {
/*  224 */       lbl = new Label(this.panelComboGroup, 16384);
/*  225 */       lbl.setText(s);
/*      */     }
/*      */ 
/*  228 */     this.hazardCbo = createCombo(this.panelComboGroup, "/root/hazard", hazardIndexLastUsed);
/*  229 */     this.fcstHrCbo = createFcstHrCombo(this.panelComboGroup, "/root/fcstHr", fcstHrIndexLastUsed);
/*      */ 
/*  231 */     this.tagCbo = new Combo(this.panelComboGroup, 12);
/*  232 */     this.deskCbo = createCombo(this.panelComboGroup, "/root/desk", deskIndexLastUsed);
/*  233 */     this.issueTypeCbo = createCombo(this.panelComboGroup, "/root/issueType", issueTypeIndexLastUsed);
/*      */ 
/*  235 */     this.moveTextBtn = new Button(this.panelComboGroup, 16384);
/*  236 */     this.moveTextBtn.setText(" Move Text ");
/*  237 */     this.moveTextBtn.setEnabled(false);
/*      */ 
/*  239 */     GridData gridData = new GridData(0);
/*  240 */     gridData.horizontalSpan = 2;
/*  241 */     gridData.horizontalAlignment = 4;
/*  242 */     this.moveTextBtn.setLayoutData(gridData);
/*      */ 
/*  244 */     this.moveTextBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  248 */         PgenUtil.setDrawingGfaTextMode((Gfa)GfaAttrDlg.de);
/*  249 */         GfaAttrDlg.this.moveTextBtn.setEnabled(true);
/*      */       }
/*      */     });
/*  254 */     this.textVOR = createTextAttr(this.panelComboGroup, 450, 25, 6, true, false);
/*      */ 
/*  256 */     createColorButtonSelector();
/*      */ 
/*  258 */     createOtherText();
/*      */ 
/*  260 */     return this.panelComboGroup;
/*      */   }
/*      */ 
/*      */   private void createColorButtonSelector()
/*      */   {
/*  265 */     this.cs = new ColorButtonSelector(this.panelComboGroup);
/*  266 */     if (rgbLastUsed == null)
/*      */     {
/*  268 */       RGB rgb = GfaInfo.getDefaultRGB(this.hazardCbo.getItem(hazardIndexLastUsed), getGfaFcstHr());
/*      */ 
/*  270 */       if (this.hazardCbo.getItem(hazardIndexLastUsed).equalsIgnoreCase("FZLVL")) {
/*  271 */         rgb = getDefaultFzlvlSfcColor(getGfaFcstHr());
/*      */       }
/*      */ 
/*  274 */       this.cs.setColorValue(rgb);
/*      */     }
/*      */     else {
/*  277 */       this.cs.setColorValue(rgbLastUsed);
/*      */     }
/*      */ 
/*  280 */     this.cs.addListener(new IPropertyChangeListener()
/*      */     {
/*      */       public void propertyChange(PropertyChangeEvent event) {
/*  283 */         GfaAttrDlg.rgbLastUsed = GfaAttrDlg.this.cs.getColorValue();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void createHazardSpecificPanel()
/*      */   {
/*  293 */     this.type = null;
/*      */ 
/*  295 */     GridLayout layout1 = new GridLayout(2, false);
/*  296 */     layout1.marginHeight = 3;
/*  297 */     layout1.marginWidth = 3;
/*  298 */     this.hazardSpecificGroup.setLayout(layout1);
/*      */ 
/*  300 */     this.requiredFields.clear();
/*      */ 
/*  302 */     String selectedHazard = this.hazardCbo.getText();
/*      */ 
/*  305 */     String xPath = "/root/hazard[@name='" + selectedHazard + "']/*";
/*  306 */     List nodes = GfaInfo.selectNodes(xPath);
/*      */ 
/*  308 */     for (Node node : nodes)
/*      */     {
/*  310 */       if ("checkbox".equalsIgnoreCase(node.getName())) {
/*  311 */         processCheckboxNode(node);
/*  312 */       } else if ("popup".equalsIgnoreCase(node.getName())) {
/*  313 */         processPopupNode(this.hazardSpecificGroup, node);
/*  314 */       } else if ("text".equalsIgnoreCase(node.getName())) {
/*  315 */         Text txt = processTextNode(this.hazardSpecificGroup, node);
/*  316 */         boolean required = "true".equals(node.valueOf("@required"));
/*  317 */         if ("type".equalsIgnoreCase(node.valueOf("@type"))) {
/*  318 */           this.type = txt;
/*  319 */           this.requiredFields.put("type", Boolean.valueOf(required));
/*  320 */           if (typeLastUsed != null) this.type.setText(typeLastUsed); 
/*      */         }
/*      */         else
/*      */         {
/*  323 */           String lbl = node.valueOf("@label");
/*  324 */           this.requiredFields.put(lbl, Boolean.valueOf(required));
/*  325 */           this.values.put(lbl, txt.getText());
/*  326 */           this.widgets.put(lbl, txt);
/*      */         }
/*  328 */       } else if ("dropdown".equalsIgnoreCase(node.getName())) {
/*  329 */         processDropdownNode(this.hazardSpecificGroup, node);
/*  330 */       } else if ("fzlText".equalsIgnoreCase(node.getName())) {
/*  331 */         processFzlNode(this.hazardSpecificGroup, node);
/*      */       }
/*      */     }
/*  334 */     if (this.type != null) updateType();
/*  335 */     this.hazardSpecificGroup.setVisible(nodes.size() > 0);
/*      */ 
/*  340 */     if (this.hazardCbo.getText().equalsIgnoreCase("FZLVL")) {
/*  341 */       Widget levelCmb = (Widget)this.widgets.get("Level");
/*  342 */       if ((levelCmb != null) && (!levelCmb.isDisposed())) {
/*  343 */         String lvl = ((Combo)this.widgets.get("Level")).getText();
/*  344 */         if (lvl.equalsIgnoreCase("SFC")) {
/*  345 */           RGB useFzlvlSfcClr = getDefaultFzlvlSfcColor(getGfaFcstHr());
/*  346 */           this.cs.setColorValue(useFzlvlSfcClr);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void createBottomPanel()
/*      */   {
/*  358 */     if ((this.fcstHrCbo.getText().indexOf('Z') > -1) || (
/*  359 */       ("Other".equalsIgnoreCase(this.fcstHrCbo.getText())) && (!this.otherText.getText().contains("-")))) {
/*  360 */       if ((this.bottomGroup != null) && (!this.bottomGroup.isDisposed())) {
/*  361 */         this.bottomGroup.dispose();
/*      */       }
/*  363 */       return;
/*      */     }
/*      */ 
/*  366 */     this.bottomGroup = new Group(this.top, 2048);
/*  367 */     GridData gridData = new GridData(1, 0, true, false, 2, 1);
/*  368 */     this.bottomGroup.setLayoutData(gridData);
/*      */ 
/*  370 */     this.bottomGroup.setVisible(true);
/*      */ 
/*  372 */     GridLayout layout = new GridLayout(6, false);
/*  373 */     layout.marginHeight = 3;
/*  374 */     layout.marginWidth = 3;
/*  375 */     this.bottomGroup.setLayout(layout);
/*      */ 
/*  377 */     Label lbl = new Label(this.bottomGroup, 131072);
/*  378 */     lbl.setText("Area:");
/*  379 */     lbl.setLayoutData(new GridData(131072, 1, true, true));
/*  380 */     this.areaText = createTextAttr(this.bottomGroup, 100, 25, 1, false, true);
/*      */ 
/*  382 */     lbl = new Label(this.bottomGroup, 131072);
/*  383 */     lbl.setText("  Beginning:");
/*  384 */     lbl.setLayoutData(new GridData(131072, 1, true, true));
/*  385 */     this.beginningText = createTextAttr(this.bottomGroup, 200, 25, 1, true, true);
/*      */ 
/*  387 */     lbl = new Label(this.bottomGroup, 131072);
/*  388 */     lbl.setText("  Ending:");
/*  389 */     lbl.setLayoutData(new GridData(131072, 1, true, true));
/*  390 */     this.endingText = createTextAttr(this.bottomGroup, 200, 25, 1, true, true);
/*      */ 
/*  392 */     this.statesBtn = new Button(this.bottomGroup, 131072);
/*  393 */     this.statesBtn.setText("States:");
/*  394 */     statesBtnBackground = this.statesBtn.getBackground();
/*  395 */     this.statesBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  398 */         GfaAttrDlg.this.setEnableStatesButton(false);
/*      */       }
/*      */     });
/*  401 */     setEnableStatesButton(statesBtnEnabled);
/*      */ 
/*  403 */     this.statesText = createTextAttr(this.bottomGroup, 300, 25, 5, true, true);
/*      */   }
/*      */ 
/*      */   private void processCheckboxNode(Node node)
/*      */   {
/*  413 */     Label lbl = new Label(this.hazardSpecificGroup, 16384);
/*  414 */     final String str = node.valueOf("@label");
/*  415 */     lbl.setText(str + ":");
/*      */ 
/*  417 */     final Button chkBox = new Button(this.hazardSpecificGroup, 32);
/*  418 */     if ("type".equalsIgnoreCase(node.valueOf("@type"))) {
/*  419 */       if (this.typeCheckboxes.get(str) == null)
/*  420 */         this.typeCheckboxes.put(str, Boolean.valueOf(false));
/*      */       else {
/*  422 */         chkBox.setSelection(((Boolean)this.typeCheckboxes.get(str)).booleanValue());
/*      */       }
/*  424 */       chkBox.addSelectionListener(new SelectionAdapter()
/*      */       {
/*      */         public void widgetSelected(SelectionEvent e)
/*      */         {
/*  428 */           GfaAttrDlg.this.typeCheckboxes.put(str, Boolean.valueOf(chkBox.getSelection()));
/*  429 */           GfaAttrDlg.this.updateType();
/*      */         } } );
/*      */     }
/*      */     else {
/*  433 */       if (this.values.get(str) != null) {
/*  434 */         chkBox.setSelection(new Boolean((String)this.values.get(str)).booleanValue());
/*      */       }
/*  436 */       this.values.put(node.valueOf("@label"), chkBox.getSelection());
/*  437 */       this.widgets.put(node.valueOf("@label"), chkBox);
/*      */     }
/*      */ 
/*  440 */     chkBox.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  443 */         GfaAttrDlg.this.updateValues();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void processPopupNode(Group group, Node node)
/*      */   {
/*  456 */     Label lbl = new Label(group, 16384);
/*  457 */     this.addRemoveBtnLabel = node.valueOf("@label");
/*  458 */     lbl.setText(this.addRemoveBtnLabel + ":");
/*      */ 
/*  460 */     List list = node.selectNodes("checkbox");
/*  461 */     this.popupCheckboxes.clear();
/*  462 */     for (Node n : list) {
/*  463 */       this.popupCheckboxes.add(n.valueOf("@label"));
/*      */     }
/*      */ 
/*  466 */     Button btn = new Button(group, 16384);
/*  467 */     btn.setText("Add/Remove Types");
/*  468 */     btn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*      */         try {
/*  472 */           if ((GfaAttrDlg.this.addRemoveTypeDlg == null) || (GfaAttrDlg.this.addRemoveTypeDlg.getShell() == null) || 
/*  473 */             (GfaAttrDlg.this.addRemoveTypeDlg.getShell().isDisposed()) || 
/*  474 */             (GfaAttrDlg.this.addRemoveTypeDlg.getShell().getParent() == null)) {
/*  475 */             GfaAttrDlg.this.addRemoveTypeDlg = new GfaAttrDlg.AddRemoveTypeDlg(GfaAttrDlg.this, GfaAttrDlg.this.getShell(), null);
/*  476 */             GfaAttrDlg.this.addRemoveTypeDlg.parentLabel = GfaAttrDlg.this.addRemoveBtnLabel;
/*      */           }
/*  478 */           GfaAttrDlg.this.openAttrDlg(GfaAttrDlg.this.addRemoveTypeDlg);
/*  479 */           Point p = GfaAttrDlg.this.addRemoveTypeDlg.getShell().getLocation();
/*  480 */           GfaAttrDlg.this.addRemoveTypeDlg.getShell().setLocation(p.x + 450 + 160, 
/*  481 */             p.y + 130);
/*  482 */           GfaAttrDlg.this.addRemoveTypeDlg.getShell().setText("Add/Remove Types");
/*      */         }
/*      */         catch (VizException e1) {
/*  485 */           e1.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private Text processTextNode(Group group, Node node)
/*      */   {
/*  498 */     String lblStr = node.valueOf("@label");
/*  499 */     boolean scrollable = "true".equalsIgnoreCase(node.valueOf("@scrollable"));
/*  500 */     boolean editable = !"false".equalsIgnoreCase(node.valueOf("@editable"));
/*  501 */     int horizontalSpan = 2;
/*  502 */     if (!lblStr.isEmpty()) {
/*  503 */       Label lbl = new Label(group, 16384);
/*  504 */       lbl.setText(lblStr + ":");
/*  505 */       horizontalSpan = 1;
/*  506 */       if (!editable) {
/*  507 */         lbl.setForeground(Display.getDefault().getSystemColor(15));
/*      */       }
/*      */     }
/*      */ 
/*  511 */     int w = Integer.parseInt(node.valueOf("@width"));
/*  512 */     int h = Integer.parseInt(node.valueOf("@height"));
/*      */     String limit;
/*      */     int CHAR_LIMIT;
/*      */     int CHAR_LIMIT;
/*  515 */     if ((limit = node.valueOf("@characterLimit")).isEmpty())
/*  516 */       CHAR_LIMIT = 2147483647;
/*      */     else {
/*  518 */       CHAR_LIMIT = Integer.parseInt(limit);
/*      */     }
/*  520 */     Group g = this.hazardSpecificGroup;
/*      */     Text txt;
/*  522 */     if ("true".equalsIgnoreCase(node.valueOf("@required"))) {
/*  523 */       GridData gridData = new GridData(1);
/*  524 */       if (lblStr.isEmpty()) gridData.horizontalSpan = 2;
/*  525 */       gridData.horizontalAlignment = 4;
/*  526 */       g = new Group(this.hazardSpecificGroup, 0);
/*  527 */       g.setLayoutData(gridData);
/*  528 */       GridLayout layout = new GridLayout(2, false);
/*  529 */       layout.marginHeight = 1;
/*  530 */       layout.marginWidth = 1;
/*  531 */       g.setLayout(layout);
/*      */ 
/*  533 */       Text txt = createTextAttr(g, w, h, 1, scrollable, editable);
/*      */ 
/*  535 */       Label l = new Label(g, 16385);
/*  536 */       Image image = new Image(getShell().getDisplay(), this.RED_CROSS_PATH);
/*  537 */       l.setImage(image);
/*  538 */       l.setBounds(image.getBounds());
/*  539 */       l.setToolTipText("Required field");
/*  540 */       l.setVisible(false);
/*  541 */       if (lblStr.isEmpty())
/*  542 */         this.requiredCrosses.put("type", l);
/*      */       else
/*  544 */         this.requiredCrosses.put(lblStr, l);
/*      */     }
/*      */     else
/*      */     {
/*  548 */       txt = createTextAttr(g, w, h, horizontalSpan, scrollable, editable);
/*  549 */       txt.addFocusListener(new FocusAdapter() {
/*      */         public void focusLost(FocusEvent e) {
/*  551 */           GfaAttrDlg.this.updateValues();
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/*  557 */     boolean digitsOnly = "digitsOnly".equalsIgnoreCase(node.valueOf("@characterType"));
/*  558 */     if ((digitsOnly) || ("digitsAndSlash".equalsIgnoreCase(node.valueOf("@characterType"))))
/*      */     {
/*  560 */       boolean slashAllowed = "digitsAndSlash".equalsIgnoreCase(node.valueOf("@characterType"));
/*  561 */       String padWithZeros = node.valueOf("@padWithZeros");
/*      */ 
/*  563 */       txt.addVerifyListener(new VerifyListenerDigitsOnly(slashAllowed, CHAR_LIMIT, null));
/*      */ 
/*  565 */       FocusListenerPadding fl = new FocusListenerPadding(padWithZeros, digitsOnly, null);
/*  566 */       if ("Bottom".equalsIgnoreCase(lblStr)) fl.maxFirstToken = 999;
/*  567 */       txt.addFocusListener(fl);
/*      */     }
/*      */ 
/*  570 */     if (this.values.get(lblStr) != null) {
/*  571 */       txt.setText((String)this.values.get(lblStr));
/*      */     }
/*      */ 
/*  574 */     return txt;
/*      */   }
/*      */ 
/*      */   private void processDropdownNode(Group group, Node node)
/*      */   {
/*  584 */     String lblStr = node.valueOf("@label");
/*  585 */     new Label(group, 16384).setText(lblStr + ":");
/*  586 */     Combo cbo = new Combo(group, 12);
/*  587 */     List list = node.selectNodes("value");
/*  588 */     for (Node n : list) {
/*  589 */       cbo.add(n.getText());
/*      */     }
/*  591 */     String v = (String)this.values.get(lblStr);
/*  592 */     int index = -1;
/*  593 */     if (v != null) {
/*  594 */       index = cbo.indexOf(v);
/*      */     }
/*  596 */     index = index == -1 ? 0 : index;
/*      */ 
/*  598 */     cbo.select(index);
/*      */ 
/*  600 */     this.values.put(lblStr, cbo.getText());
/*  601 */     this.widgets.put(lblStr, cbo);
/*  602 */     cbo.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  605 */         GfaAttrDlg.this.updateValues();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void processFzlNode(Group group, Node node)
/*      */   {
/*  617 */     boolean scrollable = "true".equalsIgnoreCase(node.valueOf("@scrollable"));
/*  618 */     int w = Integer.parseInt(node.valueOf("@width"));
/*  619 */     int h = Integer.parseInt(node.valueOf("@height"));
/*  620 */     int CHAR_LIMIT = 3;
/*      */ 
/*  622 */     Label lbl1 = new Label(group, 16384);
/*  623 */     lbl1.setText("Top/Bottom:");
/*      */ 
/*  625 */     Group g = new Group(this.hazardSpecificGroup, 0);
/*  626 */     GridData gridData = new GridData(1);
/*  627 */     gridData.horizontalAlignment = 4;
/*  628 */     g.setLayoutData(gridData);
/*  629 */     GridLayout layout = new GridLayout(2, false);
/*  630 */     layout.marginHeight = 1;
/*  631 */     layout.marginWidth = 1;
/*  632 */     g.setLayout(layout);
/*      */ 
/*  634 */     Text txt1 = createTextAttr(g, w, h, 1, scrollable, true);
/*      */ 
/*  636 */     Label lblCross1 = new Label(g, 16385);
/*  637 */     Image image = new Image(getShell().getDisplay(), this.RED_CROSS_PATH);
/*  638 */     lblCross1.setImage(image);
/*  639 */     lblCross1.setBounds(image.getBounds());
/*  640 */     lblCross1.setToolTipText("Required field");
/*  641 */     lblCross1.setVisible(false);
/*  642 */     this.requiredCrosses.put("Top/Bottom", lblCross1);
/*  643 */     this.requiredFields.put("Top/Bottom", Boolean.valueOf(true));
/*      */ 
/*  645 */     final Label lbl2 = new Label(group, 16384);
/*  646 */     lbl2.setText("FZL Top/Bottom:");
/*  647 */     lbl2.setForeground(Display.getDefault().getSystemColor(15));
/*      */ 
/*  649 */     g = new Group(this.hazardSpecificGroup, 0);
/*  650 */     gridData = new GridData(1);
/*  651 */     gridData.horizontalAlignment = 4;
/*  652 */     g.setLayoutData(gridData);
/*  653 */     layout = new GridLayout(2, false);
/*  654 */     layout.marginHeight = 1;
/*  655 */     layout.marginWidth = 1;
/*  656 */     g.setLayout(layout);
/*      */ 
/*  658 */     final Text txt2 = createTextAttr(g, w, h, 1, scrollable, true);
/*      */ 
/*  660 */     final Label lblCross2 = new Label(g, 16385);
/*  661 */     lblCross2.setImage(image);
/*  662 */     lblCross2.setBounds(image.getBounds());
/*  663 */     lblCross2.setToolTipText("Required field");
/*  664 */     lblCross2.setVisible(false);
/*      */ 
/*  666 */     txt1.addVerifyListener(new VerifyListenerDigitsOnly(true, 3, null));
/*  667 */     FocusListenerPadding fl = new FocusListenerPadding("3", false, null);
/*  668 */     fl.defaultBottom = "FZL";
/*  669 */     txt1.addFocusListener(fl);
/*  670 */     txt2.addVerifyListener(new VerifyListenerDigitsOnly(true, 3, null));
/*  671 */     fl = new FocusListenerPadding("3", false, null);
/*  672 */     txt2.addFocusListener(fl);
/*  673 */     txt2.setEditable(false);
/*  674 */     txt1.addModifyListener(new ModifyListener()
/*      */     {
/*      */       public void modifyText(ModifyEvent e) {
/*  677 */         boolean hasFZL = ((Text)e.widget).getText().contains("FZL");
/*  678 */         if (hasFZL) {
/*  679 */           GfaAttrDlg.this.values.put("FZL Top/Bottom", txt2.getText());
/*  680 */           GfaAttrDlg.this.requiredFields.put("FZL Top/Bottom", Boolean.valueOf(true));
/*  681 */           GfaAttrDlg.this.requiredCrosses.put("FZL Top/Bottom", lblCross2);
/*      */         } else {
/*  683 */           txt2.setText("");
/*  684 */           GfaAttrDlg.this.values.put("FZL Top/Bottom", "");
/*  685 */           GfaAttrDlg.this.requiredFields.remove("FZL Top/Bottom");
/*  686 */           lblCross2.setVisible(false);
/*  687 */           GfaAttrDlg.this.requiredCrosses.remove("FZL Top/Bottom");
/*      */         }
/*  689 */         txt2.setEditable(hasFZL);
/*  690 */         int colorInt = hasFZL ? 2 : 15;
/*  691 */         lbl2.setForeground(Display.getDefault().getSystemColor(colorInt));
/*      */       }
/*      */     });
/*  696 */     if (this.values.get("Top/Bottom") != null) txt2.setText(nvl((String)this.values.get("FZL Top/Bottom")));
/*  697 */     if (this.values.get("Top/Bottom") != null) txt1.setText(nvl((String)this.values.get("Top/Bottom")));
/*      */ 
/*  699 */     this.values.put("Top/Bottom", txt1.getText());
/*  700 */     this.widgets.put("Top/Bottom", txt1);
/*  701 */     this.values.put("FZL Top/Bottom", txt2.getText());
/*  702 */     this.widgets.put("FZL Top/Bottom", txt2);
/*      */   }
/*      */ 
/*      */   private Combo createCombo(Group group, String xPath, int indexToSelect)
/*      */   {
/*  714 */     Combo cbo = new Combo(group, 12);
/*  715 */     List nodes = GfaInfo.selectNodes(xPath);
/*  716 */     for (Node node : nodes) {
/*  717 */       cbo.add(node.valueOf("@name"));
/*      */     }
/*  719 */     cbo.select(indexToSelect);
/*  720 */     return cbo;
/*      */   }
/*      */ 
/*      */   public void populateTagCbo()
/*      */   {
/*  728 */     this.tagCbo.removeAll();
/*  729 */     TreeSet tags = new TreeSet(new Comparator() {
/*      */       public int compare(String s1, String s2) {
/*  731 */         if (s1.equals(s2)) return 0;
/*  732 */         if ("New".equals(s1)) return -1;
/*  733 */         if ("New".equals(s2)) return 1;
/*      */ 
/*  735 */         int i1 = Integer.parseInt(s1.replace("*", ""));
/*  736 */         int i2 = Integer.parseInt(s2.replace("*", ""));
/*  737 */         if (i1 < i2) return -1;
/*  738 */         if (i1 == i2) return 0;
/*  739 */         return 1;
/*      */       }
/*      */     });
/*  742 */     int index = -1;
/*  743 */     if (this.drawingLayer != null) {
/*  744 */       Layer layer = this.drawingLayer.getActiveLayer();
/*  745 */       all = layer.getDrawables();
/*  746 */       List selected = this.drawingLayer.getAllSelected();
/*      */       Gfa gfa;
/*  747 */       for (AbstractDrawableComponent adc : all)
/*  748 */         if (((adc instanceof Gfa)) && (((Gfa)adc).isSnapshot()) && 
/*  749 */           (((Gfa)adc).getGfaHazard().equals(getGfaHazard())) && 
/*  750 */           (((Gfa)adc).getGfaDesk().equals(getGfaDesk()))) {
/*  751 */           gfa = (Gfa)adc;
/*  752 */           tags.add(gfa.getGfaTag());
/*      */         }
/*  754 */       ArrayList reinsertWithStar = new ArrayList();
/*  755 */       for (String tag : tags) {
/*  756 */         if (needStar(tag)) reinsertWithStar.add(tag);
/*      */       }
/*  758 */       for (String s : reinsertWithStar) {
/*  759 */         tags.remove(s);
/*  760 */         tags.add(s + "*");
/*      */       }
/*      */ 
/*  763 */       Gfa selectedGfa = null;
/*  764 */       for (AbstractDrawableComponent adc : selected) {
/*  765 */         if ((adc instanceof Gfa)) selectedGfa = (Gfa)adc;
/*      */       }
/*  767 */       if (selectedGfa != null) {
/*  768 */         index = this.tagCbo.indexOf(selectedGfa.getGfaTag());
/*      */       }
/*      */     }
/*  771 */     tags.add("New");
/*      */     String s;
/*  772 */     for (List all = tags.iterator(); all.hasNext(); this.tagCbo.add(s)) s = (String)all.next();
/*      */ 
/*  774 */     if ((index == -1) && (tagLastUsed != null)) {
/*  775 */       index = this.tagCbo.indexOf(tagLastUsed);
/*  776 */       if (index == -1) index = this.tagCbo.indexOf(tagLastUsed + "*");
/*      */     }
/*  778 */     if (index == -1) index = 0;
/*      */ 
/*  780 */     this.tagCbo.select(index);
/*      */   }
/*      */ 
/*      */   private boolean needStar(String tag) {
/*  784 */     if (this.drawingLayer != null) {
/*  785 */       Layer layer = this.drawingLayer.getActiveLayer();
/*  786 */       List all = layer.getDrawables();
/*      */ 
/*  788 */       for (AbstractDrawableComponent adc : all)
/*  789 */         if (((adc instanceof Gfa)) && 
/*  790 */           (((Gfa)adc).isSnapshot()) && 
/*  791 */           (((Gfa)adc).getGfaHazard().equals(getGfaHazard())) && 
/*  792 */           (((Gfa)adc).getGfaFcstHr().equals(getGfaFcstHr())) && 
/*  793 */           (((Gfa)adc).getGfaDesk().equals(getGfaDesk())) && 
/*  794 */           (((Gfa)adc).getGfaTag().equals(tag)))
/*      */         {
/*  796 */           return true;
/*      */         }
/*      */     }
/*  799 */     return false;
/*      */   }
/*      */ 
/*      */   private String nextNewTag() {
/*  803 */     if (this.drawingLayer == null) return "1";
/*  804 */     Layer layer = this.drawingLayer.getActiveLayer();
/*  805 */     List all = layer.getDrawables();
/*      */ 
/*  807 */     TreeSet tags = new TreeSet();
/*      */     int i;
/*  809 */     for (AbstractDrawableComponent adc : all) {
/*  810 */       if (((adc instanceof Gfa)) && 
/*  811 */         (((Gfa)adc).isSnapshot()) && 
/*  812 */         (((Gfa)adc).getGfaHazard().equals(getGfaHazard())) && 
/*  813 */         (((Gfa)adc).getGfaDesk().equals(getGfaDesk()))) {
/*  814 */         i = Integer.parseInt(((Gfa)adc).getGfaTag());
/*  815 */         tags.add(Integer.valueOf(i));
/*      */       }
/*      */     }
/*  818 */     if (tags.isEmpty()) return "1";
/*      */ 
/*  820 */     int current = 1;
/*  821 */     for (Integer i : tags) {
/*  822 */       if (current != i.intValue()) return current;
/*  823 */       current++;
/*      */     }
/*      */ 
/*  827 */     return ((Integer)tags.last()).intValue() + 1;
/*      */   }
/*      */ 
/*      */   private Combo createFcstHrCombo(Group group, String xPath, int indexToSelect)
/*      */   {
/*  838 */     Combo cbo = new Combo(group, 12);
/*      */ 
/*  840 */     List nodes = GfaInfo.selectNodes(xPath);
/*  841 */     int cycle = PgenCycleTool.getCycleHour();
/*      */ 
/*  843 */     String value = null;
/*  844 */     for (Node node : nodes) {
/*  845 */       value = node.valueOf("@name");
/*      */ 
/*  847 */       if (value.indexOf("Z") > -1) {
/*  848 */         String s1 = value.split(" ")[0].trim();
/*  849 */         int s2 = (cycle + Integer.parseInt(s1)) % 24;
/*  850 */         value = s1 + " " + (s2 < 10 ? "0" + s2 : new StringBuilder().append(s2).toString()) + "Z";
/*      */       }
/*      */ 
/*  853 */       cbo.add(value);
/*      */     }
/*  855 */     cbo.select(indexToSelect);
/*  856 */     return cbo;
/*      */   }
/*      */ 
/*      */   private void addSelectionListeners()
/*      */   {
/*  865 */     SelectionAdapter s1 = new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  868 */         GfaAttrDlg.this.updateHazard();
/*  869 */         GfaAttrDlg.this.linkHazardWithLayer(GfaAttrDlg.this.hazardCbo.getText());
/*      */       }
/*      */     };
/*  872 */     SelectionAdapter s2 = new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  876 */         GfaAttrDlg.this.redrawHazardSpecificPanel();
/*  877 */         if (PgenFilterDlg.isFilterDlgOpen())
/*  878 */           GfaAttrDlg.this.selectFilterHourWhenGfaFcstHourIsChanged(GfaAttrDlg.this.getGfaFcstHr());
/*      */       }
/*      */     };
/*  885 */     this.hazardCbo.addSelectionListener(s1);
/*  886 */     this.fcstHrCbo.addSelectionListener(s2);
/*      */ 
/*  889 */     SelectionAdapter saveSettings = new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  892 */         GfaAttrDlg.this.setAttrForDlg(GfaAttrDlg.this);
/*      */ 
/*  894 */         if (!PgenSession.getInstance().getPgenPalette().getCurrentAction()
/*  894 */           .equalsIgnoreCase("MultiSelect"))
/*  895 */           GfaAttrDlg.this.populateTagCbo();
/*      */       }
/*      */     };
/*  899 */     this.deskCbo.addSelectionListener(saveSettings);
/*  900 */     this.issueTypeCbo.addSelectionListener(saveSettings);
/*      */   }
/*      */ 
/*      */   private void selectFilterHourWhenGfaFcstHourIsChanged(String gfaFcstHr)
/*      */   {
/*  909 */     for (Button button : PgenFilterDlg.hourBtns)
/*  910 */       if (!button.getSelection()) {
/*  911 */         String buttonText = button.getText();
/*  912 */         String filterHr = buttonText.endsWith("+") ? buttonText.replace('+', ' ').trim() : buttonText;
/*  913 */         if (gfaFcstHr.compareTo(filterHr) == 0) {
/*  914 */           button.setSelection(true);
/*  915 */           this.mapEditor.refresh();
/*  916 */           break;
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private Text createTextAttr(Group group, int width, int height, int horizontalSpan, boolean scrollable, boolean editable)
/*      */   {
/*  932 */     int style = 2050;
/*  933 */     if (scrollable) {
/*  934 */       style |= 256;
/*      */     }
/*  936 */     Text text = new Text(group, style);
/*  937 */     GridData gd = new GridData(width, height);
/*  938 */     gd.verticalAlignment = 1;
/*  939 */     gd.horizontalSpan = horizontalSpan;
/*  940 */     text.setLayoutData(gd);
/*  941 */     text.setEditable(editable);
/*  942 */     text.addTraverseListener(new TraverseListenerTab());
/*  943 */     return text;
/*      */   }
/*      */ 
/*      */   public void createButtonsForButtonBar(Composite parent)
/*      */   {
/*  948 */     super.createButtonsForButtonBar(parent);
/*  949 */     getButton(0).setEnabled(!statesBtnEnabled);
/*      */   }
/*      */ 
/*      */   public Boolean isClosedLine()
/*      */   {
/*  958 */     return Boolean.valueOf(true);
/*      */   }
/*      */ 
/*      */   public Boolean isFilled()
/*      */   {
/*  967 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public java.awt.Color[] getColors()
/*      */   {
/*  979 */     java.awt.Color[] colors = new java.awt.Color[2];
/*      */ 
/*  981 */     colors[0] = new java.awt.Color(this.cs.getColorValue().red, this.cs.getColorValue().green, this.cs
/*  982 */       .getColorValue().blue);
/*      */ 
/*  984 */     colors[1] = colors[0];
/*      */ 
/*  986 */     return colors;
/*      */   }
/*      */ 
/*      */   public double getSizeScale()
/*      */   {
/*  995 */     return (0.0D / 0.0D);
/*      */   }
/*      */ 
/*      */   public float getLineWidth()
/*      */   {
/* 1020 */     return GfaInfo.getLineWidth(getGfaFcstHr());
/*      */   }
/*      */ 
/*      */   public int getSmoothFactor()
/*      */   {
/* 1029 */     return 0;
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute iattr)
/*      */   {
/* 1036 */     if ((iattr instanceof IGfa)) {
/* 1037 */       IGfa attr = (IGfa)iattr;
/*      */ 
/* 1039 */       java.awt.Color clr = attr.getColors()[0];
/* 1040 */       if (clr != null) {
/* 1041 */         this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*      */       }
/*      */ 
/* 1044 */       if (this.hazardCbo != null) {
/* 1045 */         int index = this.hazardCbo.indexOf(attr.getGfaHazard());
/* 1046 */         if ((index == -1) && (attr.getGfaHazard() != null) && (!attr.getGfaHazard().contains("-"))) {
/* 1047 */           index = 1;
/*      */         }
/* 1049 */         this.hazardCbo.select(index);
/* 1050 */         hazardIndexLastUsed = index;
/*      */       }
/*      */       int h;
/* 1053 */       if (this.fcstHrCbo != null) {
/* 1054 */         String str = attr.getGfaFcstHr();
/* 1055 */         if ((nvl(str).indexOf("-") == -1) && (!nvl(str).isEmpty()))
/*      */           try {
/* 1057 */             int i = Integer.parseInt(str);
/*      */ 
/* 1059 */             h = getGfaCycleHour();
/*      */ 
/* 1061 */             h = (h + i) % 24;
/*      */ 
/* 1063 */             str = str + (h < 10 ? " 0" + h + "Z" : new StringBuilder(" ").append(h).append("Z").toString());
/*      */           }
/*      */           catch (NumberFormatException localNumberFormatException)
/*      */           {
/*      */           }
/* 1068 */         int index = this.fcstHrCbo.indexOf(str);
/*      */ 
/* 1070 */         if ((index == -1) && (!nvl(str).isEmpty()) && (this.otherText != null) && (!this.otherText.isDisposed())) {
/* 1071 */           if (str.contains("Z")) {
/* 1072 */             str = str.split(" ")[0];
/*      */           }
/* 1074 */           if (!this.otherText.getText().equals(str)) {
/* 1075 */             this.otherText.setText(str);
/*      */           }
/* 1077 */           this.otherTextLastUsed = str;
/*      */         }
/*      */ 
/* 1080 */         index = index < 0 ? this.fcstHrCbo.indexOf("Other") : index;
/* 1081 */         this.fcstHrCbo.select(index);
/* 1082 */         fcstHrIndexLastUsed = index;
/*      */       }
/*      */ 
/* 1085 */       if (this.tagCbo != null) {
/* 1086 */         int index = this.tagCbo.indexOf(attr.getGfaTag());
/* 1087 */         if (index == -1) {
/* 1088 */           index = this.tagCbo.indexOf(attr.getGfaTag() + "*");
/*      */         }
/* 1090 */         this.tagCbo.select(index);
/* 1091 */         tagLastUsed = attr.getGfaTag();
/*      */       }
/* 1093 */       if (this.deskCbo != null) {
/* 1094 */         int index = this.deskCbo.indexOf(attr.getGfaDesk());
/* 1095 */         this.deskCbo.select(index);
/* 1096 */         deskIndexLastUsed = index;
/*      */       }
/* 1098 */       if (this.issueTypeCbo != null) {
/* 1099 */         int index = this.issueTypeCbo.indexOf(attr.getGfaIssueType());
/* 1100 */         this.issueTypeCbo.select(index);
/* 1101 */         issueTypeIndexLastUsed = index;
/*      */       }
/* 1103 */       if (this.type != null) {
/* 1104 */         typeLastUsed = attr.getGfaType() == null ? "" : attr.getGfaType();
/* 1105 */         this.type.setText(typeLastUsed);
/*      */ 
/* 1112 */         String[] types = typeLastUsed.replace("/VIS", ":VIS").split(":");
/* 1113 */         for (String key : this.typeCheckboxes.keySet()) {
/* 1114 */           if (!key.isEmpty())
/* 1115 */             this.typeCheckboxes.put(key, Boolean.valueOf(false));
/*      */         }
/* 1117 */         for (String key : this.addRemoveDlgCheckboxes.keySet()) {
/* 1118 */           this.addRemoveDlgCheckboxes.put(key, Boolean.valueOf(false));
/*      */         }
/* 1120 */         for (String s : types) {
/* 1121 */           if (!s.isEmpty()) {
/* 1122 */             if (s.indexOf(this.addRemoveBtnLabel) == -1) {
/* 1123 */               this.typeCheckboxes.put(s, Boolean.valueOf(true));
/*      */             } else {
/* 1125 */               s = s.replaceAll(this.addRemoveBtnLabel, "").trim();
/* 1126 */               for (String key : s.split("/")) {
/* 1127 */                 this.addRemoveDlgCheckboxes.put(key, Boolean.valueOf(true));
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1133 */       if ((iattr instanceof Gfa))
/*      */       {
/* 1135 */         this.lastUsedGfa = ((Gfa)iattr);
/* 1136 */         de = this.lastUsedGfa;
/* 1137 */         this.moveTextBtn.setEnabled(true);
/* 1138 */         java.awt.Color c = this.lastUsedGfa.getColors() == null ? null : this.lastUsedGfa.getColors()[0];
/* 1139 */         if (c != null) {
/* 1140 */           rgbLastUsed = new RGB(c.getRed(), c.getGreen(), c.getBlue());
/*      */         }
/*      */ 
/* 1143 */         if ((this.areaText != null) && (!this.areaText.isDisposed()) && (attr.getGfaArea() != null)) {
/* 1144 */           this.areaText.setText(attr.getGfaArea());
/*      */         }
/* 1146 */         if ((this.beginningText != null) && (!this.beginningText.isDisposed()) && (attr.getGfaBeginning() != null)) {
/* 1147 */           this.beginningText.setText(attr.getGfaBeginning());
/*      */         }
/* 1149 */         if ((this.endingText != null) && (!this.endingText.isDisposed()) && (attr.getGfaEnding() != null)) {
/* 1150 */           this.endingText.setText(attr.getGfaEnding());
/*      */         }
/* 1152 */         if ((this.statesText != null) && (!this.statesText.isDisposed()) && (attr.getGfaStates() != null)) {
/* 1153 */           this.statesText.setText(attr.getGfaStates());
/*      */         }
/*      */ 
/* 1157 */         updateVORText();
/*      */       }
/*      */ 
/* 1160 */       this.values = attr.getGfaValues();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateType()
/*      */   {
/* 1168 */     StringBuilder sb = new StringBuilder(200);
/* 1169 */     for (String key : this.typeCheckboxes.keySet()) {
/* 1170 */       if (((Boolean)this.typeCheckboxes.get(key)).booleanValue()) {
/* 1171 */         if (sb.length() > 0) sb.append("/");
/* 1172 */         sb.append(key);
/*      */       }
/*      */     }
/*      */ 
/* 1176 */     StringBuilder sb2 = new StringBuilder(200);
/* 1177 */     for (String key : this.addRemoveDlgCheckboxes.keySet()) {
/* 1178 */       if (((Boolean)this.addRemoveDlgCheckboxes.get(key)).booleanValue()) {
/* 1179 */         if (sb2.length() == 0) sb2.append(this.addRemoveBtnLabel + " ");
/* 1180 */         sb2.append(key).append("/");
/*      */       }
/*      */     }
/* 1183 */     if (sb2.length() > 0) sb2.setLength(sb2.length() - 1);
/*      */ 
/* 1185 */     if (sb.length() == 0)
/* 1186 */       sb = sb2;
/* 1187 */     else if (sb2.length() != 0) {
/* 1188 */       sb.append("/").append(sb2);
/*      */     }
/*      */ 
/* 1191 */     String typeStr = sb.toString();
/* 1192 */     if (typeStr.contains("CIG")) this.values.put("CIG", "BLW_010");
/* 1193 */     if (typeStr.contains("VIS")) this.values.put("VIS", "BLW_3SM");
/*      */ 
/* 1195 */     this.type.setText(typeStr);
/*      */   }
/*      */ 
/*      */   private void updateValues()
/*      */   {
/* 1203 */     for (String key : this.widgets.keySet()) {
/* 1204 */       Widget w = (Widget)this.widgets.get(key);
/* 1205 */       if ((w != null) && (!w.isDisposed())) {
/* 1206 */         if ((w instanceof Text)) {
/* 1207 */           String v = ((Text)w).getText();
/* 1208 */           if ("Top/Bottom".equalsIgnoreCase(key)) {
/* 1209 */             String[] splitValue = v.split("/");
/* 1210 */             this.values.put("Top", splitValue[0].isEmpty() ? null : splitValue[0]);
/* 1211 */             this.values.put("Bottom", splitValue.length > 1 ? splitValue[1] : null);
/*      */           }
/* 1213 */           this.values.put(key, v);
/* 1214 */         } else if ((w instanceof Combo)) {
/* 1215 */           this.values.put(key, ((Combo)w).getText());
/* 1216 */           if (key.equalsIgnoreCase("Level")) {
/* 1217 */             String lvl = ((Combo)w).getText();
/*      */ 
/* 1219 */             RGB clr = GfaInfo.getDefaultRGB(this.hazardCbo.getItem(hazardIndexLastUsed), getGfaFcstHr());
/* 1220 */             if ((this.hazardCbo.getText().equalsIgnoreCase("FZLVL")) && 
/* 1221 */               (lvl.equalsIgnoreCase("SFC"))) {
/* 1222 */               clr = getDefaultFzlvlSfcColor(getGfaFcstHr());
/*      */             }
/*      */ 
/* 1226 */             this.cs.setColorValue(clr);
/*      */           }
/* 1228 */         } else if ((w instanceof Button)) {
/* 1229 */           this.values.put(key, ((Button)w).getSelection());
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1235 */     updateVORText();
/*      */   }
/*      */ 
/*      */   private void updateVORText() {
/* 1239 */     if (this.drawingLayer == null) return;
/* 1240 */     List selected = this.drawingLayer.getAllSelected();
/* 1241 */     Gfa selectedGfa = null;
/* 1242 */     for (AbstractDrawableComponent adc : selected) {
/* 1243 */       if ((adc instanceof Gfa)) selectedGfa = (Gfa)adc;
/*      */     }
/* 1245 */     if (selectedGfa != null)
/* 1246 */       setVorText(selectedGfa.getGfaVorText());
/*      */   }
/*      */ 
/*      */   public String getGfaHazard()
/*      */   {
/* 1252 */     if (this.hazardCbo != null) {
/* 1253 */       return this.hazardCbo.getText();
/*      */     }
/* 1255 */     return null;
/*      */   }
/*      */ 
/*      */   public String getGfaDesk()
/*      */   {
/* 1260 */     if (this.deskCbo != null) {
/* 1261 */       return this.deskCbo.getText();
/*      */     }
/* 1263 */     return null;
/*      */   }
/*      */ 
/*      */   public String getGfaFcstHr()
/*      */   {
/* 1268 */     if ((this.fcstHrCbo != null) && (!this.fcstHrCbo.isDisposed())) {
/* 1269 */       String str = this.fcstHrCbo.getText();
/*      */ 
/* 1271 */       if (str.indexOf("Z") > -1)
/*      */       {
/* 1274 */         String[] a = str.split(" ");
/* 1275 */         String temp = "";
/* 1276 */         for (String s : a) {
/* 1277 */           temp = temp + (s.indexOf("Z") > -1 ? "" : s);
/*      */         }
/* 1279 */         str = temp.trim();
/*      */       }
/* 1281 */       else if ("Other".equalsIgnoreCase(str)) {
/* 1282 */         if ((this.otherText != null) && (!this.otherText.isDisposed()))
/* 1283 */           str = this.otherText.getText();
/*      */         else {
/* 1285 */           str = "";
/*      */         }
/*      */       }
/* 1288 */       return str;
/*      */     }
/* 1290 */     return null;
/*      */   }
/*      */ 
/*      */   public String getGfaIssueType()
/*      */   {
/* 1295 */     if (this.issueTypeCbo != null) {
/* 1296 */       return this.issueTypeCbo.getText();
/*      */     }
/* 1298 */     return null;
/*      */   }
/*      */ 
/*      */   public String getGfaTag()
/*      */   {
/* 1303 */     if (this.tagCbo != null) {
/* 1304 */       String tag = this.tagCbo.getText();
/* 1305 */       if ("New".equalsIgnoreCase(tag)) {
/* 1306 */         tag = nextNewTag();
/*      */       }
/* 1308 */       return tag.replace("*", "");
/*      */     }
/* 1310 */     return null;
/*      */   }
/*      */ 
/*      */   public String getGfaType()
/*      */   {
/* 1315 */     if ((this.type != null) && (!this.type.isDisposed())) {
/* 1316 */       return this.type.getText();
/*      */     }
/* 1318 */     return null;
/*      */   }
/*      */ 
/*      */   public String getGfaArea()
/*      */   {
/* 1323 */     if ((this.areaText != null) && (!this.areaText.isDisposed())) {
/* 1324 */       return this.areaText.getText();
/*      */     }
/* 1326 */     return null;
/*      */   }
/*      */ 
/*      */   public void setGfaArea(String value) {
/* 1330 */     if ((this.areaText != null) && (!this.areaText.isDisposed()))
/* 1331 */       this.areaText.setText(nvl(value));
/*      */   }
/*      */ 
/*      */   public String getGfaBeginning()
/*      */   {
/* 1337 */     if ((this.beginningText != null) && (!this.beginningText.isDisposed())) {
/* 1338 */       return this.beginningText.getText();
/*      */     }
/* 1340 */     return null;
/*      */   }
/*      */ 
/*      */   public void setGfaBeginning(String value) {
/* 1344 */     if ((this.beginningText != null) && (!this.beginningText.isDisposed()))
/* 1345 */       this.beginningText.setText(nvl(value));
/*      */   }
/*      */ 
/*      */   public String getGfaEnding()
/*      */   {
/* 1351 */     if ((this.endingText != null) && (!this.endingText.isDisposed())) {
/* 1352 */       return this.endingText.getText();
/*      */     }
/* 1354 */     return null;
/*      */   }
/*      */ 
/*      */   public void setGfaEnding(String value) {
/* 1358 */     if ((this.endingText != null) && (!this.endingText.isDisposed()))
/* 1359 */       this.endingText.setText(nvl(value));
/*      */   }
/*      */ 
/*      */   public String getGfaStates()
/*      */   {
/* 1365 */     if ((this.statesText != null) && (!this.statesText.isDisposed())) {
/* 1366 */       return this.statesText.getText();
/*      */     }
/* 1368 */     return null;
/*      */   }
/*      */ 
/*      */   public void setGfaStates(String value) {
/* 1372 */     if ((this.statesText != null) && (!this.statesText.isDisposed()))
/* 1373 */       this.statesText.setText(nvl(value));
/*      */   }
/*      */ 
/*      */   public HashMap<String, String> getGfaValues()
/*      */   {
/* 1383 */     HashMap copy = new HashMap();
/* 1384 */     for (String key : this.values.keySet()) {
/* 1385 */       copy.put(key, (String)this.values.get(key));
/*      */     }
/* 1387 */     return copy;
/*      */   }
/*      */ 
/*      */   private void openAttrDlg(AttrDlg dlg)
/*      */   {
/* 1396 */     dlg.setBlockOnOpen(false);
/* 1397 */     dlg.setDrawingLayer(this.drawingLayer);
/* 1398 */     dlg.setMapEditor(this.mapEditor);
/* 1399 */     dlg.open();
/*      */   }
/*      */ 
/*      */   public void redrawHazardSpecificPanel()
/*      */   {
/* 1405 */     setAttrForDlg(this);
/*      */ 
/* 1408 */     Shell shell = this.hazardSpecificGroup.getShell();
/* 1409 */     if (this.hazardSpecificGroup != null) {
/* 1410 */       for (Control c : this.hazardSpecificGroup.getChildren()) {
/* 1411 */         c.dispose();
/*      */       }
/*      */     }
/* 1414 */     if ((this.bottomGroup != null) && (!this.bottomGroup.isDisposed())) {
/* 1415 */       this.bottomGroup.dispose();
/*      */     }
/* 1417 */     if ((this.emptyLabel != null) && (!this.emptyLabel.isDisposed())) {
/* 1418 */       this.emptyLabel.dispose();
/*      */     }
/*      */ 
/* 1421 */     createOtherText();
/*      */ 
/* 1424 */     if (!PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 1424 */       .equalsIgnoreCase("MultiSelect")) {
/* 1425 */       populateTagCbo();
/*      */     }
/*      */ 
/* 1430 */     rgbLastUsed = GfaInfo.getDefaultRGB(this.hazardCbo.getItem(hazardIndexLastUsed), getGfaFcstHr());
/* 1431 */     this.cs.setColorValue(rgbLastUsed);
/*      */ 
/* 1433 */     shell.pack();
/* 1434 */     if ((this.warning != null) && (!this.warning.isDisposed())) {
/* 1435 */       this.warning.dispose();
/* 1436 */       this.warning = null;
/*      */     }
/*      */ 
/* 1439 */     createHazardSpecificPanel();
/*      */ 
/* 1441 */     createBottomPanel();
/* 1442 */     shell.pack();
/*      */   }
/*      */ 
/*      */   private void createOtherText() {
/* 1446 */     if ((this.otherText != null) && (!this.otherText.isDisposed())) {
/* 1447 */       this.otherText.dispose();
/*      */     }
/* 1449 */     if ("Other".equalsIgnoreCase(this.fcstHrCbo.getText())) {
/* 1450 */       this.emptyLabel = new Label(this.panelComboGroup, 0);
/* 1451 */       this.otherText = new Text(this.panelComboGroup, 2048);
/* 1452 */       this.otherText.addVerifyListener(new VerifyListenerOtherText(null));
/* 1453 */       if (this.otherTextLastUsed != null) this.otherText.setText(this.otherTextLastUsed);
/* 1454 */       GridData gd = new GridData(0, 1, false, true);
/* 1455 */       gd.horizontalAlignment = 4;
/* 1456 */       this.otherText.setLayoutData(gd);
/* 1457 */       this.textVOR.dispose();
/* 1458 */       this.cs.dispose();
/* 1459 */       this.textVOR = createTextAttr(this.panelComboGroup, 450, 25, 6, true, false);
/* 1460 */       createColorButtonSelector();
/* 1461 */       this.panelComboGroup.pack();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void okPressed()
/*      */   {
/* 1470 */     if ((!PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 1470 */       .equalsIgnoreCase("MultiSelect")) && (!validateRequiredFields()))
/*      */       return;
/*      */     Boolean b;
/* 1473 */     for (String key : this.widgets.keySet()) {
/* 1474 */       Widget w = (Widget)this.widgets.get(key);
/* 1475 */       if ((w != null) && (!w.isDisposed()))
/*      */       {
/* 1477 */         if ((w instanceof Button)) {
/* 1478 */           b = new Boolean(((Button)w).getSelection());
/* 1479 */           this.values.put(key, b.toString());
/* 1480 */         } else if ((w instanceof Text)) {
/* 1481 */           this.values.put(key, ((Text)w).getText());
/* 1482 */         } else if ((w instanceof Combo)) {
/* 1483 */           this.values.put(key, ((Combo)w).getText());
/*      */         }
/*      */       }
/*      */     }
/* 1486 */     correctOtherText();
/*      */ 
/* 1488 */     ArrayList adcList = null;
/* 1489 */     ArrayList newList = new ArrayList();
/*      */ 
/* 1492 */     if (this.drawingLayer != null) {
/* 1493 */       adcList = (ArrayList)this.drawingLayer.getAllSelected();
/*      */     }
/*      */ 
/* 1496 */     if ((adcList != null) && (!adcList.isEmpty()))
/*      */     {
/* 1499 */       for (AbstractDrawableComponent adc : adcList) {
/* 1500 */         DrawableElement el = adc.getPrimaryDE();
/* 1501 */         if (el != null)
/*      */         {
/* 1503 */           DrawableElement newEl = (DrawableElement)el.copy();
/*      */ 
/* 1505 */           newEl.update(this);
/* 1506 */           newList.add(newEl);
/*      */ 
/* 1508 */           if ((newEl instanceof Gfa)) {
/* 1509 */             ((Gfa)newEl).snap();
/* 1510 */             this.lastUsedGfa = ((Gfa)newEl);
/* 1511 */             de = this.lastUsedGfa;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1516 */       ArrayList oldList = new ArrayList(adcList);
/* 1517 */       this.drawingLayer.replaceElements(oldList, newList);
/*      */     }
/* 1519 */     this.drawingLayer.removeSelected();
/*      */ 
/* 1522 */     for (AbstractDrawableComponent adc : newList) {
/* 1523 */       this.drawingLayer.addSelected(adc);
/*      */     }
/*      */ 
/* 1526 */     if (this.mapEditor != null) {
/* 1527 */       this.mapEditor.refresh();
/*      */     }
/*      */ 
/* 1530 */     updateValues();
/*      */ 
/* 1532 */     if ((this.fcstHrCbo != null) && (!this.fcstHrCbo.isDisposed()) && 
/* 1533 */       ("Other".equalsIgnoreCase(this.fcstHrCbo.getText())) && (this.otherText != null) && 
/* 1534 */       (!this.otherText.isDisposed())) {
/* 1535 */       this.otherTextLastUsed = this.otherText.getText();
/*      */     }
/* 1537 */     if ((this.tagCbo != null) && (!this.tagCbo.isDisposed()) && (this.lastUsedGfa != null))
/*      */     {
/* 1539 */       if (!PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 1539 */         .equalsIgnoreCase("MultiSelect")) {
/* 1540 */         tagLastUsed = this.lastUsedGfa.getGfaTag();
/* 1541 */         populateTagCbo();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean close() {
/* 1547 */     if (statesBtnEnabled) {
/* 1548 */       return false;
/*      */     }
/* 1550 */     return super.close();
/*      */   }
/*      */ 
/*      */   public int open()
/*      */   {
/* 1555 */     if ((statesBtnEnabled) || ((this.top != null) && (!this.top.isDisposed()))) {
/* 1556 */       return 1;
/*      */     }
/*      */ 
/* 1560 */     if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 1560 */       .equalsIgnoreCase("MultiSelect")) {
/* 1561 */       initMultiSelect();
/*      */     }
/*      */ 
/* 1565 */     create();
/*      */ 
/* 1568 */     if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 1568 */       .equalsIgnoreCase("MultiSelect")) {
/* 1569 */       configMultiSelect();
/*      */     }
/*      */ 
/* 1572 */     int open = super.open();
/*      */ 
/* 1574 */     switchHazard(PgenSession.getInstance().getPgenResource().getActiveLayer().getName());
/*      */ 
/* 1576 */     return open;
/*      */   }
/*      */ 
/*      */   private void correctOtherText()
/*      */   {
/* 1582 */     if ((this.otherText == null) || (this.otherText.isDisposed())) return;
/* 1583 */     String str = this.otherText.getText();
/*      */ 
/* 1585 */     if ((str.endsWith(":")) || (str.endsWith("-"))) str = str.substring(0, str.length() - 1);
/* 1586 */     String[] s = str.split("-");
/* 1587 */     for (int i = 0; i < s.length; i++) {
/* 1588 */       if (s[i].indexOf(":") > -1) {
/* 1589 */         s[i] = padTime(s[i]);
/*      */       }
/*      */     }
/*      */ 
/* 1593 */     switch (s.length) {
/*      */     case 1:
/* 1595 */       str = s[0];
/* 1596 */       break;
/*      */     case 2:
/* 1598 */       str = s[0] + "-" + s[1];
/*      */     }
/*      */ 
/* 1601 */     if (!this.otherText.getText().equals(str))
/* 1602 */       this.otherText.setText(str);
/*      */   }
/*      */ 
/*      */   private String padTime(String in)
/*      */   {
/* 1608 */     if (in.matches("[01234569]|12")) return in += ":00";
/* 1609 */     if (in.matches("12:|[012345]:")) return in += "00";
/* 1610 */     if (!in.matches("[01234569]?|12|[012345]?:|[012345]?:(0|1|3|4|00|15|30|45)")) return in;
/* 1611 */     String[] hm = in.split(":");
/* 1612 */     hm[0] = (hm[0].isEmpty() ? "0" : hm[0]);
/*      */ 
/* 1614 */     if ((hm[1].matches("1")) || (hm[1].matches("4")))
/*      */     {
/*      */       int tmp132_131 = 1;
/*      */       String[] tmp132_130 = hm; tmp132_130[tmp132_131] = (tmp132_130[tmp132_131] + "5");
/*      */     }
/* 1617 */     if ((hm[1].matches("0")) || (hm[1].matches("3")))
/*      */     {
/*      */       int tmp181_180 = 1;
/*      */       String[] tmp181_179 = hm; tmp181_179[tmp181_180] = (tmp181_179[tmp181_180] + "0");
/*      */     }
/* 1620 */     String out = hm[0] + ":" + hm[1];
/* 1621 */     return out;
/*      */   }
/*      */ 
/*      */   public boolean validateRequiredFields()
/*      */   {
/* 1632 */     if ((new Boolean(true).equals(this.requiredFields.get("type"))) && 
/* 1633 */       (this.type != null) && 
/* 1634 */       (!this.type.isDisposed()) && 
/* 1635 */       (this.type.getText().isEmpty())) {
/* 1636 */       displayCrosses(true);
/* 1637 */       return false;
/*      */     }
/*      */ 
/* 1640 */     for (String key : this.widgets.keySet()) {
/* 1641 */       Widget w = (Widget)this.widgets.get(key);
/* 1642 */       if ((w != null) && (!w.isDisposed()))
/*      */       {
/* 1644 */         if ((((w instanceof Text)) || ((w instanceof Combo))) && 
/* 1645 */           (new Boolean(true).equals(this.requiredFields.get(key))) && 
/* 1646 */           (((Text)w).getText().isEmpty())) {
/* 1647 */           displayCrosses(true);
/* 1648 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1653 */     displayCrosses(false);
/*      */ 
/* 1655 */     return true;
/*      */   }
/*      */ 
/*      */   private void displayCrosses(boolean show) {
/* 1659 */     for (String key : this.requiredCrosses.keySet()) {
/* 1660 */       Label w = (Label)this.requiredCrosses.get(key);
/* 1661 */       if ((w != null) && (!w.isDisposed())) {
/* 1662 */         w.setVisible(show);
/*      */       }
/*      */     }
/* 1665 */     Shell shell = this.top.getShell();
/* 1666 */     if ((this.warning != null) && (!this.warning.isDisposed())) {
/* 1667 */       this.warning.dispose();
/* 1668 */       this.warning = null;
/* 1669 */       shell.pack();
/*      */     }
/* 1671 */     if (show) {
/* 1672 */       this.warning = new Label(this.top, 16384);
/* 1673 */       this.warning.setText("Fields marked with red crosses are required ");
/* 1674 */       this.warning.setForeground(Display.getDefault().getSystemColor(3));
/* 1675 */       shell.pack();
/*      */     }
/*      */   }
/*      */ 
/*      */   private String nvl(String value) {
/* 1680 */     return value == null ? "" : value;
/*      */   }
/*      */ 
/*      */   private void updateColorWhenOtherTextChanged(String s)
/*      */   {
/* 1685 */     String hazard = this.hazardCbo.getText();
/*      */ 
/* 1687 */     RGB fzlvlClr = null;
/* 1688 */     if (hazard.equalsIgnoreCase("FZLVL")) {
/* 1689 */       Widget levelCmb = (Widget)this.widgets.get("Level");
/* 1690 */       if ((levelCmb != null) && (!levelCmb.isDisposed())) {
/* 1691 */         String lvl = ((Combo)this.widgets.get("Level")).getText();
/* 1692 */         if (lvl.equalsIgnoreCase("SFC")) {
/* 1693 */           fzlvlClr = getDefaultFzlvlSfcColor(s);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1698 */     if ("Other".equals(this.fcstHrCbo.getText())) {
/* 1699 */       String str = s;
/* 1700 */       if ((str.endsWith(":")) || (str.endsWith("-"))) str = str.substring(0, str.length() - 1);
/* 1701 */       java.awt.Color[] c = GfaInfo.getDefaultColors(hazard, str);
/* 1702 */       RGB rgb = new RGB(c[0].getRed(), c[0].getGreen(), c[0].getBlue());
/* 1703 */       if (fzlvlClr != null) rgb = fzlvlClr;
/*      */ 
/* 1705 */       this.cs.setColorValue(rgb);
/*      */     }
/*      */     else {
/* 1708 */       RGB clr = GfaInfo.getDefaultRGB(this.hazardCbo.getItem(hazardIndexLastUsed), getGfaFcstHr());
/* 1709 */       this.cs.setColorValue(clr);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEnableStatesButton(boolean enabled)
/*      */   {
/* 1855 */     if ((this.statesBtn == null) || (this.statesBtn.isDisposed())) return;
/* 1856 */     if (enabled)
/* 1857 */       this.statesBtn.setBackground(Display.getDefault().getSystemColor(7));
/*      */     else {
/* 1859 */       this.statesBtn.setBackground(statesBtnBackground);
/*      */     }
/* 1861 */     this.statesBtn.setEnabled(enabled);
/* 1862 */     statesBtnEnabled = enabled;
/* 1863 */     if (getButton(0) != null)
/* 1864 */       getButton(0).setEnabled(!enabled);
/*      */   }
/*      */ 
/*      */   public String getPatternName()
/*      */   {
/* 1963 */     return null;
/*      */   }
/*      */ 
/*      */   public String getSymbolType()
/*      */   {
/* 1969 */     return null;
/*      */   }
/*      */ 
/*      */   public int getGfaCycleDay()
/*      */   {
/* 1975 */     return PgenCycleTool.getCycleDay();
/*      */   }
/*      */ 
/*      */   public int getGfaCycleHour()
/*      */   {
/* 1981 */     return PgenCycleTool.getCycleHour();
/*      */   }
/*      */ 
/*      */   public boolean isGfaOpen()
/*      */   {
/* 1988 */     return (this.hazardCbo != null) && (!this.hazardCbo.isDisposed());
/*      */   }
/*      */ 
/*      */   public void switchHazard(String hazard)
/*      */   {
/* 1996 */     if (isGfaOpen())
/*      */     {
/* 1998 */       String currentHaz = this.hazardCbo.getText();
/* 1999 */       if ((hazard != null) && (!currentHaz.equals(hazard)))
/*      */       {
/* 2001 */         int index = this.hazardCbo.indexOf(hazard);
/*      */ 
/* 2003 */         if (index >= 0)
/*      */         {
/* 2005 */           this.hazardCbo.select(index);
/* 2006 */           hazardIndexLastUsed = index;
/*      */ 
/* 2008 */           updateHazard();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateHazard()
/*      */   {
/* 2019 */     if ((this.addRemoveTypeDlg != null) && (!this.addRemoveTypeDlg.isDisposed())) {
/* 2020 */       this.addRemoveTypeDlg.close();
/* 2021 */       this.addRemoveTypeDlg = null;
/*      */     }
/*      */ 
/* 2024 */     if (this.type != null) this.type.setText("");
/*      */ 
/* 2026 */     this.typeCheckboxes.clear();
/* 2027 */     this.values.clear();
/* 2028 */     this.widgets.clear();
/* 2029 */     this.addRemoveDlgCheckboxes.clear();
/* 2030 */     redrawHazardSpecificPanel();
/*      */   }
/*      */ 
/*      */   private void linkHazardWithLayer(String layer)
/*      */   {
/* 2038 */     if ((PgenSession.getInstance().getPgenResource().getProductManageDlg() != null) && 
/* 2039 */       (PgenSession.getInstance().getPgenResource().getProductManageDlg().isOpen())) {
/* 2040 */       PgenSession.getInstance().getPgenResource().getProductManageDlg().switchLayer(layer);
/*      */     }
/* 2042 */     else if ((PgenSession.getInstance().getPgenResource().getLayeringControlDlg() != null) && 
/* 2043 */       (PgenSession.getInstance().getPgenResource().getLayeringControlDlg().isOpen()))
/* 2044 */       PgenSession.getInstance().getPgenResource().getLayeringControlDlg().switchLayer(layer);
/*      */   }
/*      */ 
/*      */   public void setVorText(String vorText)
/*      */   {
/* 2054 */     if (vorText != null) this.textVOR.setText(vorText);
/*      */   }
/*      */ 
/*      */   public boolean isMoveTextEnable()
/*      */   {
/* 2062 */     return this.moveTextBtn.isEnabled();
/*      */   }
/*      */ 
/*      */   public void enableMoveTextBtn(boolean flag)
/*      */   {
/* 2069 */     this.moveTextBtn.setEnabled(flag);
/*      */   }
/*      */ 
/*      */   private void initMultiSelect()
/*      */   {
/* 2076 */     this.typeCheckboxes.clear();
/* 2077 */     this.addRemoveDlgCheckboxes.clear();
/* 2078 */     typeLastUsed = "";
/*      */ 
/* 2080 */     this.values.clear();
/*      */   }
/*      */ 
/*      */   private void configMultiSelect()
/*      */   {
/* 2087 */     this.tagCbo.add("");
/* 2088 */     int index = this.tagCbo.indexOf("");
/* 2089 */     this.tagCbo.select(index);
/*      */ 
/* 2091 */     this.deskCbo.add("");
/* 2092 */     index = this.deskCbo.indexOf("");
/* 2093 */     this.deskCbo.select(index);
/*      */ 
/* 2095 */     this.issueTypeCbo.add("");
/* 2096 */     index = this.issueTypeCbo.indexOf("");
/* 2097 */     this.issueTypeCbo.select(index);
/*      */ 
/* 2099 */     index = this.fcstHrCbo.indexOf("Other");
/* 2100 */     this.fcstHrCbo.select(index);
/*      */   }
/*      */ 
/*      */   private RGB getDefaultFzlvlSfcColor(String fcsthr)
/*      */   {
/* 2108 */     RGB rgb = GfaInfo.getFzlvlSfcColor("Other");
/*      */ 
/* 2110 */     if (fcsthr != null) {
/* 2111 */       String[] hrmin = fcsthr.split("-");
/* 2112 */       if (hrmin.length > 1) {
/* 2113 */         double hr = Double.parseDouble(hrmin[0]);
/* 2114 */         if (hr >= 6.0D) {
/* 2115 */           rgb = GfaInfo.getFzlvlSfcColor("outlook");
/*      */         }
/*      */         else
/* 2118 */           rgb = GfaInfo.getFzlvlSfcColor("smear");
/*      */       }
/*      */       else
/*      */       {
/* 2122 */         rgb = GfaInfo.getFzlvlSfcColor("snapshot");
/*      */       }
/*      */     }
/*      */ 
/* 2126 */     return rgb;
/*      */   }
/*      */ 
/*      */   public void setOtherTextLastUsed(String otherTextLastUsed)
/*      */   {
/* 2138 */     if ((otherTextLastUsed != null) && (otherTextLastUsed.contains("-")))
/* 2139 */       this.otherTextLastUsed = otherTextLastUsed;
/*      */   }
/*      */ 
/*      */   private class AddRemoveTypeDlg extends AttrDlg
/*      */   {
/*      */     String parentLabel;
/*      */     protected Composite top;
/*      */ 
/*      */     private AddRemoveTypeDlg(Shell parShell)
/*      */       throws VizException
/*      */     {
/* 1880 */       super();
/*      */     }
/*      */ 
/*      */     public void createButtonsForButtonBar(Composite parent)
/*      */     {
/* 1887 */       createButton(parent, 12, IDialogConstants.CLOSE_LABEL, true);
/* 1888 */       getButton(12).addSelectionListener(new SelectionAdapter()
/*      */       {
/*      */         public void widgetSelected(SelectionEvent e) {
/* 1891 */           GfaAttrDlg.AddRemoveTypeDlg.this.okPressed();
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void handleShellCloseEvent()
/*      */     {
/* 1899 */       setReturnCode(1);
/* 1900 */       close();
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/* 1905 */       close();
/* 1906 */       GfaAttrDlg.this.addRemoveTypeDlg = null;
/*      */     }
/*      */ 
/*      */     public Control createDialogArea(Composite parent)
/*      */     {
/* 1914 */       this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/* 1917 */       GridLayout mainLayout = new GridLayout(1, false);
/* 1918 */       mainLayout.marginHeight = 3;
/* 1919 */       mainLayout.marginWidth = 3;
/* 1920 */       this.top.setLayout(mainLayout);
/*      */ 
/* 1922 */       Group group = new Group(this.top, 2080);
/* 1923 */       GridLayout layout1 = new GridLayout(1, false);
/* 1924 */       layout1.marginHeight = 3;
/* 1925 */       layout1.marginWidth = 3;
/* 1926 */       group.setLayout(layout1);
/*      */ 
/* 1928 */       for (final String labelStr : GfaAttrDlg.this.popupCheckboxes)
/*      */       {
/* 1930 */         final Button btn = new Button(group, 16777248);
/* 1931 */         btn.setText(labelStr);
/* 1932 */         boolean checked = false;
/* 1933 */         if (GfaAttrDlg.this.addRemoveDlgCheckboxes.get(labelStr) == null)
/* 1934 */           GfaAttrDlg.this.addRemoveDlgCheckboxes.put(labelStr, Boolean.valueOf(false));
/*      */         else {
/* 1936 */           checked = ((Boolean)GfaAttrDlg.this.addRemoveDlgCheckboxes.get(labelStr)).booleanValue();
/*      */         }
/* 1938 */         btn.setSelection(checked);
/* 1939 */         btn.addSelectionListener(new SelectionAdapter()
/*      */         {
/*      */           public void widgetSelected(SelectionEvent e) {
/* 1942 */             GfaAttrDlg.this.addRemoveDlgCheckboxes.put(labelStr, Boolean.valueOf(btn.getSelection()));
/* 1943 */             GfaAttrDlg.this.updateType();
/*      */           }
/*      */         });
/*      */       }
/*      */ 
/* 1948 */       return this.top;
/*      */     }
/*      */ 
/*      */     public void setAttrForDlg(IAttribute ia)
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean isDisposed() {
/* 1956 */       return this.top.isDisposed();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FocusListenerPadding extends FocusAdapter
/*      */   {
/*      */     private final String digitsToPad;
/*      */     private boolean digitsOnly;
/* 1796 */     String defaultBottom = "SFC";
/* 1797 */     int maxFirstToken = 450;
/*      */ 
/*      */     private FocusListenerPadding(String digitsToPad, boolean digitsOnly) {
/* 1800 */       this.digitsToPad = digitsToPad;
/* 1801 */       this.digitsOnly = digitsOnly;
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent e)
/*      */     {
/* 1806 */       String str = ((Text)e.widget).getText();
/* 1807 */       if (str.isEmpty()) return;
/* 1808 */       String[] split = str.split("/");
/* 1809 */       if (!this.digitsToPad.isEmpty()) {
/* 1810 */         int padInt = Integer.parseInt(this.digitsToPad);
/* 1811 */         for (int i = 0; i < split.length; i++) {
/* 1812 */           if ((!this.digitsOnly) && (i == 1) && (split[i].toUpperCase().startsWith("SFC".substring(0, split[i].length()))))
/* 1813 */             split[i] = "SFC";
/* 1814 */           else if ((!this.digitsOnly) && (i == 1) && (split[i].toUpperCase().startsWith("FZL".substring(0, split[i].length()))))
/* 1815 */             split[i] = "FZL";
/*      */           else {
/* 1817 */             split[i] = padWithZeros(split[i], padInt);
/*      */           }
/* 1819 */           if ((i == 0) && (Integer.parseInt(split[i]) > this.maxFirstToken)) split[i] = this.maxFirstToken;
/*      */         }
/*      */       }
/* 1822 */       String out = "";
/* 1823 */       for (int i = 0; i < split.length; i++) {
/* 1824 */         if (!out.isEmpty()) out = out + "/";
/* 1825 */         out = out + split[i].toUpperCase();
/*      */       }
/* 1827 */       if ((!this.digitsOnly) && (split.length == 1)) out = out + "/" + this.defaultBottom;
/*      */ 
/* 1829 */       ((Text)e.widget).setText(out);
/*      */ 
/* 1831 */       GfaAttrDlg.this.updateValues();
/*      */     }
/*      */ 
/*      */     private String padWithZeros(String str, int length) {
/* 1835 */       if (str.length() > length) return str;
/* 1836 */       for (int i = str.length(); i < length; i++) {
/* 1837 */         str = str + "0";
/*      */       }
/* 1839 */       return str;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class TraverseListenerTab
/*      */     implements TraverseListener
/*      */   {
/*      */     public void keyTraversed(TraverseEvent e)
/*      */     {
/* 1848 */       if ((e.detail == 16) || (e.detail == 8))
/* 1849 */         e.doit = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class VerifyListenerDigitsOnly
/*      */     implements VerifyListener
/*      */   {
/*      */     private boolean slashAllowed;
/*      */     private int charLimit;
/*      */ 
/*      */     private VerifyListenerDigitsOnly(boolean slashAllowed, int char_limit)
/*      */     {
/* 1722 */       this.slashAllowed = slashAllowed;
/* 1723 */       this.charLimit = char_limit;
/*      */     }
/*      */ 
/*      */     public void verifyText(VerifyEvent e)
/*      */     {
/* 1729 */       String str = ((Text)e.widget).getText().substring(0, e.start) + e.text;
/* 1730 */       str = str.trim().toUpperCase();
/* 1731 */       if (str.length() > this.charLimit) e.doit = false;
/*      */ 
/* 1733 */       if (this.slashAllowed)
/*      */       {
/* 1735 */         e.doit = str.matches("\\d{0,3}|\\d{1,3}/|\\d{1,3}/\\d{0,3}|\\d{1,3}/S|\\d{1,3}/SF|\\d{1,3}/SFC|\\d{1,3}/F|\\d{1,3}/FZ|\\d{1,3}/FZL");
/*      */       }
/* 1737 */       else e.doit = str.matches("\\d{0," + this.charLimit + "}");
/*      */ 
/* 1741 */       if ((e.character == '\b') || (e.character == '')) e.doit = true; 
/*      */     }
/*      */   }
/*      */ 
/*      */   private class VerifyListenerOtherText
/*      */     implements VerifyListener
/*      */   {
/*      */     private VerifyListenerOtherText()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void verifyText(VerifyEvent e)
/*      */     {
/* 1753 */       String s = ((Text)e.widget).getText();
/* 1754 */       s = s.substring(0, e.start) + e.text + s.substring(e.end);
/*      */ 
/* 1756 */       e.doit = s.matches("[01234569]?|12|[012345]?:|[012345]?:(0|1|3|4|00|15|30|45)");
/* 1757 */       e.doit |= s.matches("([01234569]?|12|[012345]?:|[012345]?:(0|1|3|4|00|15|30|45))-");
/* 1758 */       e.doit |= s.matches("([01234569]?|12|[012345]?:|[012345]?:(0|1|3|4|00|15|30|45))-([01234569]?|12|[012345]?:|[012345]?:(0|1|3|4|00|15|30|45))");
/* 1759 */       if (!e.doit) return;
/*      */ 
/* 1762 */       if (s.contains("-")) {
/* 1763 */         String[] t = s.split("-");
/* 1764 */         if ((t.length == 2) && (!t[0].isEmpty()) && (!t[1].isEmpty()))
/*      */         {
/* 1766 */           t[0] = GfaAttrDlg.this.padTime(t[0]);
/* 1767 */           t[1] = GfaAttrDlg.this.padTime(t[1]);
/* 1768 */           String[] t0 = t[0].split(":");
/* 1769 */           String[] t1 = t[1].split(":");
/* 1770 */           if ((!t0[1].isEmpty()) && (!t1[1].isEmpty()))
/*      */           {
/* 1772 */             if ((Integer.parseInt(t0[0]) > Integer.parseInt(t1[0])) && 
/* 1773 */               (Integer.parseInt(t1[0]) != 1))
/*      */             {
/* 1776 */               e.doit = false;
/* 1777 */               return;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1783 */       if (!e.doit) return;
/*      */ 
/* 1786 */       GfaAttrDlg.this.updateColorWhenOtherTextChanged(s);
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.GfaAttrDlg
 * JD-Core Version:    0.6.2
 */