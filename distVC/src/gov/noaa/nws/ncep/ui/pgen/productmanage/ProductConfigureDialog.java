/*      */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*      */ 
/*      */ import com.raytheon.uf.common.localization.LocalizationContext;
/*      */ import com.raytheon.uf.common.localization.LocalizationContext.LocalizationLevel;
/*      */ import com.raytheon.uf.common.localization.LocalizationContext.LocalizationType;
/*      */ import com.raytheon.uf.common.localization.LocalizationFile;
/*      */ import com.raytheon.uf.common.localization.exception.LocalizationException;
/*      */ import com.raytheon.uf.common.localization.exception.LocalizationOpFailedException;
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.Activator;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*      */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenActions;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenClass;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenControls;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenLayer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenObjects;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenSave;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProdType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductTypes;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import org.eclipse.core.runtime.IConfigurationElement;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.jface.preference.IPreferenceStore;
/*      */ import org.eclipse.swt.events.SelectionAdapter;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.events.SelectionListener;
/*      */ import org.eclipse.swt.graphics.Image;
/*      */ import org.eclipse.swt.graphics.Point;
/*      */ import org.eclipse.swt.graphics.RGB;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.layout.RowData;
/*      */ import org.eclipse.swt.layout.RowLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.FileDialog;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Listener;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.TabFolder;
/*      */ import org.eclipse.swt.widgets.TabItem;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ import org.eclipse.swt.widgets.Widget;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class ProductConfigureDialog extends ProductDialog
/*      */ {
/*  101 */   private static ProductConfigureDialog INSTANCE = null;
/*      */ 
/*  103 */   private final String[] cntlBtnNames = { "Ok", "Add", "Delete", "Close" };
/*  104 */   private final String[] cntlBtnTips = { "Save changes and exit", "Apply changes and continue", 
/*  105 */     "Delete current activity type", "Ignore changes and exit" };
/*      */ 
/*  106 */   private final String[] typeTabNames = { "Palette", "Settings", "Layer", "Save", "Filter", 
/*  107 */     "Share", "Clip", "Products" };
/*      */   private static final String PGEN_PRODUCT_TYPES = "productTypes.xml";
/*  110 */   private HashMap<String, IConfigurationElement> itemMap = null;
/*      */ 
/*  112 */   private static ArrayList<String> controls = null;
/*  113 */   private static ArrayList<String> actions = null;
/*  114 */   private static ArrayList<String> classes = null;
/*  115 */   private static ArrayList<String> objects = null;
/*  116 */   private HashMap<String, Image> iconMap = null;
/*  117 */   private HashMap<String, Image> iconMapSelected = null;
/*      */ 
/*  119 */   private final int numColumns = 10;
/*  120 */   private final int bgcolor = 255;
/*  121 */   private final int fgcolor = 16777215;
/*      */ 
/*  124 */   private static LocalizationFile prdTypesFile = null;
/*  125 */   private ProductTypes prdTyps = null;
/*  126 */   private List<ProdType> ptList = null;
/*      */ 
/*  128 */   private Composite topComp = null;
/*      */ 
/*  130 */   private TabFolder tabFolder = null;
/*  131 */   private Composite[] tabComposites = null;
/*      */ 
/*  133 */   private Composite paletteComp = null;
/*  134 */   private Composite typeComp = null;
/*  135 */   private Group controlGroup = null;
/*  136 */   private Group actionGroup = null;
/*  137 */   private Group classGroup = null;
/*  138 */   private Group objectGroup = null;
/*  139 */   private Composite controlBtnComp = null;
/*      */ 
/*  141 */   private Group activityGrp = null;
/*  142 */   private Combo typeCombo = null;
/*  143 */   private Text typeText = null;
/*      */ 
/*  145 */   private Combo subtypeCombo = null;
/*  146 */   private Text subtypeText = null;
/*  147 */   private static String DEFAULT_SUBTYPE = "None";
/*      */ 
/*  149 */   private Text aliasText = null;
/*      */ 
/*  151 */   private Button[] controlBtns = null;
/*  152 */   private Button[] actionBtns = null;
/*  153 */   private Button[] classChkBtns = null;
/*  154 */   private Button[] classPushBtns = null;
/*  155 */   private Button[] objectBtns = null;
/*  156 */   private Button allOnOffBtn = null;
/*      */ 
/*  158 */   private Button[] dlgCntlBtn = null;
/*      */   private Button clipChkBox;
/*  161 */   private Text shapeFileTxt = null;
/*      */ 
/*  166 */   private Combo layerNameCombo = null;
/*  167 */   private Text layerNameTxt = null;
/*  168 */   private Button layerOnOffBtn = null;
/*  169 */   private Button layerMonoBtn = null;
/*  170 */   private ColorButtonSelector layerColorSelector = null;
/*  171 */   private Button layerFillBtn = null;
/*      */ 
/*  174 */   private Group layerTempGrp = null;
/*  175 */   private ArrayList<Button> layerNameBtns = null;
/*      */ 
/*  177 */   private Text currentSettingsTxt = null;
/*  178 */   private Text settingsTxt = null;
/*      */   private static String autoSavePath;
/*      */   private static int autoSaveInterval;
/*  186 */   private Text saveOutputTxt = null;
/*  187 */   private Button saveIndividualLayerBtn = null;
/*  188 */   private Button autoSaveBtn = null;
/*  189 */   private Text autoSaveFreqTxt = null;
/*      */   private Composite pdComp;
/*      */   private Combo boundsNameCbo;
/*      */   private Combo boundsListCbo;
/*  199 */   private final java.awt.Color defaultButtonColor = java.awt.Color.lightGray;
/*  200 */   private final java.awt.Color activeButtonColor = java.awt.Color.green;
/*  201 */   private final java.awt.Color remindButtonColor = java.awt.Color.yellow;
/*      */ 
/*  204 */   private static String[] HOURS = { "0", "0+", "3", "3+", "6", "9", "12", "0-0", "3-3", "6-6", "0-3", "0-6", "3-6", "6-9", 
/*  205 */     "6-12", "9-12", "AIRM", "OTLK" };
/*      */ 
/*      */   protected ProductConfigureDialog(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  215 */     super(parShell);
/*      */   }
/*      */ 
/*      */   public static ProductConfigureDialog getInstance(Shell parShell)
/*      */   {
/*  228 */     if (INSTANCE == null) {
/*      */       try
/*      */       {
/*  231 */         INSTANCE = new ProductConfigureDialog(parShell);
/*      */       } catch (VizException e) {
/*  233 */         e.printStackTrace();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  238 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public void setTitle()
/*      */   {
/*  247 */     this.shell.setText("Configure Activity Types");
/*      */   }
/*      */ 
/*      */   public void setLayout()
/*      */   {
/*  255 */     GridLayout mainLayout = new GridLayout(1, true);
/*  256 */     mainLayout.marginHeight = 1;
/*  257 */     mainLayout.marginWidth = 1;
/*  258 */     this.shell.setLayout(mainLayout);
/*      */   }
/*      */ 
/*      */   public void setDefaultLocation(Shell parent)
/*      */   {
/*  269 */     if (this.shellLocation == null) {
/*  270 */       Point pt = parent.getLocation();
/*  271 */       this.shell.setLocation(pt.x + 500, pt.y + 100);
/*      */     } else {
/*  273 */       this.shell.setLocation(this.shellLocation);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void initializeComponents()
/*      */   {
/*  288 */     retrievePgenPalette();
/*      */ 
/*  294 */     this.prdTyps = loadProductTypes();
/*      */ 
/*  299 */     IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
/*  300 */     autoSaveInterval = prefs.getInt("PGEN_AUTOSAVE_FREQ");
/*  301 */     autoSavePath = prefs.getString("PGEN_RECOVERY_DIR");
/*      */ 
/*  306 */     this.topComp = new Composite(this.shell, 0);
/*  307 */     GridLayout gl = new GridLayout(1, false);
/*  308 */     this.topComp.setLayout(gl);
/*      */ 
/*  313 */     this.activityGrp = new Group(this.topComp, 4);
/*  314 */     this.activityGrp.setLayout(new GridLayout(1, false));
/*  315 */     this.activityGrp.setText("Activity Identity");
/*      */ 
/*  320 */     createTypeComp(this.activityGrp);
/*      */ 
/*  325 */     Group configGrp = new Group(this.topComp, 4);
/*  326 */     configGrp.setLayout(new GridLayout(1, false));
/*  327 */     configGrp.setText("Activity Configuraton");
/*      */ 
/*  332 */     this.tabFolder = new TabFolder(configGrp, 2048);
/*  333 */     int ntabs = this.typeTabNames.length;
/*  334 */     this.tabComposites = new Composite[ntabs];
/*      */ 
/*  336 */     this.tabFolder.addSelectionListener(new SelectionListener() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  338 */         ProductConfigureDialog.this.editTabItems(ProductConfigureDialog.this.tabFolder.getSelectionIndex());
/*      */       }
/*      */ 
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */     });
/*  349 */     for (int ii = 0; ii < this.typeTabNames.length; ii++)
/*      */     {
/*  351 */       this.tabComposites[ii] = new Composite(this.tabFolder, 0);
/*      */ 
/*  354 */       TabItem item = new TabItem(this.tabFolder, 0);
/*  355 */       item.setText(this.typeTabNames[ii]);
/*  356 */       item.setToolTipText("item " + ii);
/*      */ 
/*  358 */       if (ii == 0) {
/*  359 */         createPaletteTab(this.tabComposites[ii]);
/*      */       }
/*  361 */       else if (ii == 1) {
/*  362 */         createSettingsTab(this.tabComposites[ii]);
/*      */       }
/*  364 */       else if (ii == 2) {
/*  365 */         createLayeringTab(this.tabComposites[ii]);
/*      */       }
/*  367 */       else if (ii == 3) {
/*  368 */         createSaveTab(this.tabComposites[ii]);
/*      */       }
/*  370 */       else if (ii == 4) {
/*  371 */         createFilterTab(this.tabComposites[ii]);
/*      */       }
/*  373 */       else if (ii == 5) {
/*  374 */         createShareTab(this.tabComposites[ii]);
/*      */       }
/*  376 */       else if (ii == 6) {
/*  377 */         createClipTab(this.tabComposites[ii]);
/*      */       }
/*  379 */       else if (ii == 7) {
/*  380 */         createProductsTab(this.tabComposites[ii]);
/*      */       }
/*      */       else {
/*  383 */         createDefaultTab(this.tabComposites[ii]);
/*      */       }
/*      */ 
/*  386 */       item.setControl(this.tabComposites[ii]);
/*      */     }
/*      */ 
/*  393 */     createControlBtns(this.topComp);
/*      */   }
/*      */ 
/*      */   private void editTabItems(int index)
/*      */   {
/*  402 */     if (index == 2) {
/*  403 */       editLayeringTab();
/*      */     }
/*  405 */     else if (index == 3)
/*  406 */       editSaveTab();
/*      */   }
/*      */ 
/*      */   private void createPaletteTab(Composite cmp)
/*      */   {
/*  416 */     this.paletteComp = cmp;
/*  417 */     GridLayout gl = new GridLayout(1, false);
/*  418 */     this.paletteComp.setLayout(gl);
/*      */ 
/*  420 */     this.controlGroup = new Group(this.paletteComp, 4);
/*  421 */     this.controlGroup.setLayout(new GridLayout(10, false));
/*  422 */     this.controlGroup.setText("Controls");
/*      */ 
/*  424 */     int ii = 0;
/*  425 */     for (String str : controls) {
/*  426 */       this.controlBtns[ii] = new Button(this.controlGroup, 2);
/*  427 */       this.controlBtns[ii].setData(str);
/*  428 */       this.controlBtns[ii].setToolTipText(((IConfigurationElement)this.itemMap.get(str)).getAttribute("label"));
/*      */ 
/*  430 */       if (btnAlwaysOn(str)) {
/*  431 */         this.controlBtns[ii].setSelection(true);
/*  432 */         this.controlBtns[ii].setImage((Image)this.iconMapSelected.get(str));
/*      */       }
/*      */       else {
/*  435 */         this.controlBtns[ii].setSelection(false);
/*  436 */         this.controlBtns[ii].setImage((Image)this.iconMap.get(str));
/*      */       }
/*      */ 
/*  439 */       this.controlBtns[ii].addSelectionListener(new SelectionAdapter()
/*      */       {
/*      */         public void widgetSelected(SelectionEvent event) {
/*  442 */           Button btn = (Button)event.widget;
/*  443 */           String name = event.widget.getData().toString();
/*      */ 
/*  445 */           if (ProductConfigureDialog.this.btnAlwaysOn(name)) {
/*  446 */             btn.setSelection(true);
/*      */           }
/*      */ 
/*  449 */           if (btn.getSelection()) {
/*  450 */             btn.setImage((Image)ProductConfigureDialog.this.iconMapSelected.get(name));
/*      */           }
/*      */           else
/*  453 */             btn.setImage((Image)ProductConfigureDialog.this.iconMap.get(name));
/*      */         }
/*      */       });
/*  459 */       ii++;
/*      */     }
/*      */ 
/*  462 */     this.actionGroup = new Group(this.paletteComp, 4);
/*  463 */     this.actionGroup.setLayout(new GridLayout(10, false));
/*  464 */     this.actionGroup.setText("Actions");
/*      */ 
/*  466 */     ii = 0;
/*  467 */     for (String str : actions) {
/*  468 */       this.actionBtns[ii] = new Button(this.actionGroup, 2);
/*  469 */       this.actionBtns[ii].setData(str);
/*  470 */       this.actionBtns[ii].setToolTipText(((IConfigurationElement)this.itemMap.get(str)).getAttribute("label"));
/*      */ 
/*  472 */       if (btnAlwaysOn(str)) {
/*  473 */         this.actionBtns[ii].setImage((Image)this.iconMapSelected.get(str));
/*  474 */         this.actionBtns[ii].setSelection(true);
/*      */       }
/*      */       else {
/*  477 */         this.actionBtns[ii].setImage((Image)this.iconMap.get(str));
/*  478 */         this.actionBtns[ii].setSelection(false);
/*      */       }
/*      */ 
/*  481 */       this.actionBtns[ii].addSelectionListener(new SelectionAdapter()
/*      */       {
/*      */         public void widgetSelected(SelectionEvent event) {
/*  484 */           Button btn = (Button)event.widget;
/*  485 */           String name = event.widget.getData().toString();
/*      */ 
/*  488 */           if (ProductConfigureDialog.this.btnAlwaysOn(name)) {
/*  489 */             btn.setSelection(true);
/*      */           }
/*      */ 
/*  492 */           if (btn.getSelection()) {
/*  493 */             btn.setImage((Image)ProductConfigureDialog.this.iconMapSelected.get(name));
/*      */           }
/*      */           else
/*  496 */             btn.setImage((Image)ProductConfigureDialog.this.iconMap.get(name));
/*      */         }
/*      */       });
/*  501 */       ii++;
/*      */     }
/*      */ 
/*  504 */     this.classGroup = new Group(this.paletteComp, 4);
/*  505 */     this.classGroup.setLayout(new GridLayout(6, false));
/*  506 */     this.classGroup.setText("Classes");
/*      */ 
/*  508 */     ii = 0;
/*  509 */     for (String str : classes) {
/*  510 */       Composite subComp = new Composite(this.classGroup, 0);
/*  511 */       GridLayout gl2 = new GridLayout(2, false);
/*  512 */       gl2.marginRight = 0;
/*  513 */       gl2.horizontalSpacing = 0;
/*  514 */       gl2.verticalSpacing = 0;
/*  515 */       gl2.marginWidth = 0;
/*  516 */       gl2.marginHeight = 0;
/*  517 */       subComp.setLayout(gl2);
/*      */ 
/*  519 */       this.classChkBtns[ii] = new Button(subComp, 32);
/*  520 */       this.classChkBtns[ii].setData(Integer.valueOf(ii));
/*  521 */       this.classChkBtns[ii].setSize(5, 5);
/*      */ 
/*  523 */       this.classChkBtns[ii].setSelection(btnAlwaysOn(str));
/*      */ 
/*  525 */       this.classChkBtns[ii].addSelectionListener(new SelectionAdapter()
/*      */       {
/*      */         public void widgetSelected(SelectionEvent event) {
/*  528 */           int which = Integer.parseInt(event.widget.getData().toString());
/*  529 */           String btnName = ProductConfigureDialog.this.classPushBtns[which].getData().toString();
/*      */ 
/*  531 */           if (ProductConfigureDialog.this.btnAlwaysOn(btnName)) {
/*  532 */             ProductConfigureDialog.this.classChkBtns[which].setSelection(true);
/*      */           }
/*      */ 
/*  535 */           if (ProductConfigureDialog.this.classChkBtns[which].getSelection()) {
/*  536 */             ProductConfigureDialog.this.classPushBtns[which].setEnabled(true);
/*  537 */             ProductConfigureDialog.this.classPushBtns[which].setImage((Image)ProductConfigureDialog.this.iconMapSelected.get(btnName));
/*  538 */             ProductConfigureDialog.this.createObjects(ProductConfigureDialog.this.classPushBtns[which].getData().toString(), false);
/*      */           }
/*      */           else {
/*  541 */             ProductConfigureDialog.this.classPushBtns[which].setEnabled(false);
/*  542 */             ProductConfigureDialog.this.classPushBtns[which].setImage((Image)ProductConfigureDialog.this.iconMap.get(btnName));
/*      */ 
/*  544 */             if ((ProductConfigureDialog.this.objectGroup != null) && 
/*  545 */               (btnName.equals(ProductConfigureDialog.this.objectGroup.getData().toString())))
/*      */             {
/*  547 */               ProductConfigureDialog.this.createObjects("None", true);
/*      */             }
/*      */           }
/*      */         }
/*      */       });
/*  556 */       this.classPushBtns[ii] = new Button(subComp, 2);
/*  557 */       this.classPushBtns[ii].setToolTipText(((IConfigurationElement)this.itemMap.get(str)).getAttribute("label"));
/*      */ 
/*  559 */       this.classPushBtns[ii].setSelection(btnAlwaysOn(str));
/*      */ 
/*  561 */       this.classPushBtns[ii].setData(str);
/*  562 */       this.classPushBtns[ii].setEnabled(false);
/*      */ 
/*  564 */       if (btnAlwaysOn(str)) {
/*  565 */         this.classPushBtns[ii].setEnabled(true);
/*  566 */         this.classPushBtns[ii].setSelection(true);
/*  567 */         this.classPushBtns[ii].setImage((Image)this.iconMapSelected.get(str));
/*      */       }
/*      */       else {
/*  570 */         this.classPushBtns[ii].setEnabled(false);
/*  571 */         this.classPushBtns[ii].setSelection(false);
/*  572 */         this.classPushBtns[ii].setImage((Image)this.iconMap.get(str));
/*      */       }
/*      */ 
/*  576 */       this.classPushBtns[ii].addSelectionListener(new SelectionAdapter() {
/*      */         public void widgetSelected(SelectionEvent event) {
/*  578 */           ProductConfigureDialog.this.createObjects(event.widget.getData().toString(), false);
/*      */         }
/*      */       });
/*  582 */       ii++;
/*      */     }
/*      */ 
/*  585 */     refreshPaletteSelections();
/*      */   }
/*      */ 
/*      */   public static ProductTypes loadProductTypes()
/*      */   {
/*  594 */     ProductTypes ptyps = new ProductTypes();
/*      */ 
/*  596 */     prdTypesFile = PgenStaticDataProvider.getProvider().getStaticLocalizationFile(
/*  597 */       PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "productTypes.xml");
/*      */ 
/*  599 */     if ((prdTypesFile != null) && (prdTypesFile.getFile().exists()) && 
/*  600 */       (prdTypesFile.getFile().canRead())) {
/*  601 */       ptyps = FileTools.readProductTypes(prdTypesFile.getFile().getAbsolutePath());
/*      */     }
/*      */ 
/*  604 */     return ptyps;
/*      */   }
/*      */ 
/*      */   public static LinkedHashMap<String, ProductType> getProductTypes()
/*      */   {
/*  613 */     LinkedHashMap prdTypMap = new LinkedHashMap();
/*  614 */     ProductTypes ptyps = loadProductTypes();
/*      */ 
/*  616 */     for (ProductType ptype : ptyps.getProductType())
/*      */     {
/*      */       String skey;
/*      */       String skey;
/*  618 */       if ((ptype.getName() != null) && (ptype.getName().trim().length() > 0)) {
/*  619 */         skey = new String(ptype.getName());
/*      */       }
/*      */       else {
/*  622 */         skey = new String(ptype.getType());
/*  623 */         String subtyp = ptype.getSubtype();
/*  624 */         if ((subtyp != null) && (subtyp.trim().length() > 0) && 
/*  625 */           (!subtyp.equalsIgnoreCase(DEFAULT_SUBTYPE))) {
/*  626 */           skey = new String(ptype.getType() + "(" + subtyp + ")");
/*      */         }
/*      */       }
/*      */ 
/*  630 */       prdTypMap.put(skey, ptype);
/*      */     }
/*      */ 
/*  633 */     return prdTypMap;
/*      */   }
/*      */ 
/*      */   private void refreshPaletteSelections()
/*      */   {
/*  642 */     String type = this.typeCombo.getText();
/*  643 */     ProductType curPrdTyp = getCurrentPrdTyp();
/*      */ 
/*  645 */     if (curPrdTyp == null) {
/*  646 */       return;
/*      */     }
/*      */ 
/*  649 */     this.typeText.setText(type);
/*      */     Button localButton5;
/*      */     Button btn;
/*  651 */     if (type.equalsIgnoreCase("new"))
/*      */     {
/*  653 */       this.typeText.setText("");
/*  654 */       if ((this.dlgCntlBtn[1] != null) && (!this.dlgCntlBtn[1].isDisposed()))
/*  655 */         this.dlgCntlBtn[1].setText("Add");
/*  658 */       Button[] arrayOfButton1;
/*  658 */       localButton5 = (arrayOfButton1 = this.controlBtns).length; for (Button localButton1 = 0; localButton1 < localButton5; localButton1++) { Button btn = arrayOfButton1[localButton1];
/*      */ 
/*  660 */         if (btnAlwaysOn(btn.getData().toString())) {
/*  661 */           btn.setSelection(true);
/*  662 */           btn.setImage((Image)this.iconMapSelected.get(btn.getData().toString()));
/*      */         }
/*      */         else {
/*  665 */           btn.setSelection(false);
/*  666 */           btn.setImage((Image)this.iconMap.get(btn.getData().toString()));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  671 */       localButton5 = (arrayOfButton1 = this.actionBtns).length; for (Button localButton2 = 0; localButton2 < localButton5; localButton2++) { Button btn = arrayOfButton1[localButton2];
/*      */ 
/*  673 */         if (btnAlwaysOn(btn.getData().toString())) {
/*  674 */           btn.setSelection(true);
/*  675 */           btn.setImage((Image)this.iconMapSelected.get(btn.getData().toString()));
/*      */         }
/*      */         else {
/*  678 */           btn.setSelection(false);
/*  679 */           btn.setImage((Image)this.iconMap.get(btn.getData().toString()));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  684 */       int ij = 0;
/*      */       Button[] arrayOfButton3;
/*  685 */       int j = (arrayOfButton3 = this.classPushBtns).length; for (localButton5 = 0; localButton5 < j; localButton5++) { btn = arrayOfButton3[localButton5];
/*      */ 
/*  687 */         if (btnAlwaysOn(btn.getData().toString())) {
/*  688 */           this.classChkBtns[ij].setSelection(true);
/*  689 */           btn.setEnabled(true);
/*  690 */           btn.setImage((Image)this.iconMapSelected.get(btn.getData().toString()));
/*      */         }
/*      */         else {
/*  693 */           this.classChkBtns[ij].setSelection(false);
/*  694 */           btn.setEnabled(false);
/*  695 */           btn.setImage((Image)this.iconMap.get(btn.getData().toString()));
/*      */         }
/*      */ 
/*  698 */         ij++;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  704 */       if (this.dlgCntlBtn[1] != null) {
/*  705 */         this.dlgCntlBtn[1].setText("Apply");
/*      */       }
/*      */ 
/*  708 */       this.dlgCntlBtn[2].setEnabled(true);
/*      */       Button[] arrayOfButton2;
/*  710 */       localButton5 = (arrayOfButton2 = this.controlBtns).length; for (Button localButton3 = 0; localButton3 < localButton5; localButton3++) { Button btn = arrayOfButton2[localButton3];
/*  711 */         String str = btn.getData().toString();
/*  712 */         if ((btnAlwaysOn(str)) || ((curPrdTyp.getPgenControls() != null) && 
/*  713 */           (curPrdTyp.getPgenControls().getName().contains(str)))) {
/*  714 */           btn.setSelection(true);
/*  715 */           btn.setImage((Image)this.iconMapSelected.get(str));
/*      */         }
/*      */         else {
/*  718 */           btn.setSelection(false);
/*  719 */           btn.setImage((Image)this.iconMap.get(str));
/*      */         }
/*      */       }
/*      */ 
/*  723 */       Button localButton6 = (arrayOfButton2 = this.actionBtns).length;
/*      */       Object str;
/*  723 */       for (Button localButton4 = 0; localButton4 < localButton6; localButton4++) { Button btn = arrayOfButton2[localButton4];
/*  724 */         str = btn.getData().toString();
/*  725 */         if ((btnAlwaysOn((String)str)) || ((curPrdTyp.getPgenActions() != null) && 
/*  726 */           (curPrdTyp.getPgenActions().getName().contains(str)))) {
/*  727 */           btn.setSelection(true);
/*  728 */           btn.setImage((Image)this.iconMapSelected.get(str));
/*      */         }
/*      */         else {
/*  731 */           btn.setSelection(false);
/*  732 */           btn.setImage((Image)this.iconMap.get(str));
/*      */         }
/*      */       }
/*      */ 
/*  736 */       int ii = 0;
/*  737 */       for (Button btn : this.classPushBtns)
/*      */       {
/*  739 */         String str = btn.getData().toString();
/*  740 */         this.classChkBtns[ii].setSelection(false);
/*  741 */         btn.setEnabled(false);
/*  742 */         btn.setImage((Image)this.iconMap.get(str));
/*      */ 
/*  744 */         for (PgenClass cls : curPrdTyp.getPgenClass()) {
/*  745 */           if ((btnAlwaysOn(str)) || (
/*  746 */             (cls != null) && (cls.getName().equalsIgnoreCase(str)))) {
/*  747 */             this.classChkBtns[ii].setSelection(true);
/*  748 */             btn.setEnabled(true);
/*  749 */             btn.setImage((Image)this.iconMapSelected.get(str));
/*  750 */             break;
/*      */           }
/*      */         }
/*      */ 
/*  754 */         ii++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  759 */     refreshPgenPalette(curPrdTyp);
/*      */   }
/*      */ 
/*      */   private void dialogActions(String btnName)
/*      */   {
/*  769 */     if (btnName.equalsIgnoreCase(this.cntlBtnNames[0])) {
/*  770 */       addNewProductType();
/*      */     }
/*  772 */     else if ((btnName.equalsIgnoreCase(this.cntlBtnNames[1])) || 
/*  773 */       (btnName.equalsIgnoreCase("Apply"))) {
/*  774 */       applyProductType();
/*      */     }
/*  776 */     else if (btnName.equalsIgnoreCase(this.cntlBtnNames[2])) {
/*  777 */       deleteProductType();
/*      */     }
/*      */     else {
/*  780 */       this.objectGroup = null;
/*  781 */       close();
/*  782 */       PgenSession.getInstance().getPgenPalette().resetPalette(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addNewProductType()
/*      */   {
/*  792 */     if ((!this.typeCombo.getText().equalsIgnoreCase("new")) || (
/*  793 */       (this.typeText.getText() != null) && (this.typeText.getText().trim().length() != 0)))
/*      */     {
/*  796 */       if (updateProductTypes()) {
/*  797 */         saveProductTypes();
/*      */       }
/*      */     }
/*      */ 
/*  801 */     this.objectGroup = null;
/*  802 */     close();
/*  803 */     if (PgenSession.getInstance().getPgenPalette() != null)
/*  804 */       PgenSession.getInstance().getPgenPalette().resetPalette(null);
/*      */   }
/*      */ 
/*      */   private void applyProductType()
/*      */   {
/*  815 */     if (updateProductTypes()) {
/*  816 */       saveProductTypes();
/*      */     }
/*      */ 
/*  820 */     refreshPaletteSelections();
/*      */   }
/*      */ 
/*      */   private void deleteProductType()
/*      */   {
/*  829 */     ProductType delType = getCurrentPrdTyp();
/*  830 */     int typeIdx = this.prdTyps.getProductType().indexOf(delType);
/*      */ 
/*  832 */     if (typeIdx >= 0)
/*      */     {
/*  834 */       MessageDialog confirmDlg = new MessageDialog(
/*  835 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  836 */         "Confirm Delete a Product Type", null, 
/*  837 */         "Are you sure you want to delete type " + findAlias(delType) + "?", 
/*  838 */         3, new String[] { "OK", "Cancel" }, 0);
/*  839 */       confirmDlg.open();
/*      */ 
/*  841 */       if (confirmDlg.getReturnCode() == 0)
/*      */       {
/*  843 */         this.prdTyps.getProductType().remove(delType);
/*  844 */         saveProductTypes();
/*      */         try
/*      */         {
/*  848 */           String pdName = this.typeText.getText();
/*  849 */           String settings = getSettingFullPath(pdName);
/*      */ 
/*  851 */           if (!settings.equalsIgnoreCase("settings_tbl.xml"))
/*      */           {
/*  853 */             LocalizationContext userContext = PgenStaticDataProvider.getProvider().getLocalizationContext(
/*  854 */               LocalizationContext.LocalizationType.CAVE_STATIC, LocalizationContext.LocalizationLevel.USER);
/*      */ 
/*  856 */             lFile = PgenStaticDataProvider.getProvider().getLocalizationFile(
/*  857 */               userContext, settings);
/*      */ 
/*  859 */             lFile.delete();
/*      */           }
/*      */         }
/*      */         catch (Exception e) {
/*  863 */           e.printStackTrace();
/*      */         }
/*      */ 
/*  867 */         int typeAt = -1;
/*  868 */         int jj = 0;
/*      */         String[] arrayOfString1;
/*  869 */         LocalizationFile localLocalizationFile1 = (arrayOfString1 = this.typeCombo.getItems()).length; for (LocalizationFile lFile = 0; lFile < localLocalizationFile1; lFile++) { String tp = arrayOfString1[lFile];
/*  870 */           if (tp.equals(delType.getType())) {
/*  871 */             typeAt = jj;
/*      */           }
/*  873 */           jj++;
/*      */         }
/*      */ 
/*  876 */         ArrayList stypes = findSubtypes(delType.getType());
/*  877 */         if (stypes.size() == 0) {
/*  878 */           this.typeCombo.remove(delType.getType());
/*      */ 
/*  880 */           int next = Math.min(typeAt, this.typeCombo.getItemCount() - 1);
/*  881 */           this.typeCombo.select(next);
/*      */ 
/*  883 */           this.subtypeCombo.removeAll();
/*  884 */           this.subtypeCombo.add("New");
/*  885 */           ArrayList sntypes = findSubtypes(this.typeCombo.getText());
/*  886 */           for (String st : sntypes) {
/*  887 */             this.subtypeCombo.add(st);
/*      */           }
/*      */ 
/*  890 */           if (sntypes.size() > 0) {
/*  891 */             this.subtypeCombo.select(1);
/*  892 */             this.subtypeText.setText(this.subtypeCombo.getText());
/*      */           }
/*      */           else {
/*  895 */             this.subtypeCombo.select(0);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  901 */           int subtypeAt = -1;
/*  902 */           String ss = delType.getSubtype();
/*  903 */           jj = 0;
/*  904 */           for (String stp : this.subtypeCombo.getItems()) {
/*  905 */             if (stp.equals(ss)) {
/*  906 */               subtypeAt = jj;
/*      */             }
/*  908 */             jj++;
/*      */           }
/*      */ 
/*  911 */           if ((ss != null) && (ss.trim().length() > 0)) {
/*  912 */             this.subtypeCombo.remove(delType.getSubtype());
/*      */           }
/*      */ 
/*  915 */           int next = Math.min(subtypeAt, this.subtypeCombo.getItemCount() - 1);
/*  916 */           this.subtypeCombo.select(next);
/*      */         }
/*      */ 
/*  919 */         switchProductType();
/*      */ 
/*  921 */         this.shell.pack();
/*  922 */         this.shell.layout();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  928 */     if (this.prdTyps.getProductType().size() == 0) {
/*  929 */       this.typeText.setText("");
/*  930 */       this.subtypeText.setText("");
/*      */     }
/*      */ 
/*  933 */     this.typeCombo.pack();
/*  934 */     this.subtypeCombo.pack();
/*  935 */     this.typeCombo.getParent().pack();
/*  936 */     this.activityGrp.pack();
/*      */   }
/*      */ 
/*      */   private void saveProductTypes()
/*      */   {
/*      */     try
/*      */     {
/*  955 */       if (prdTypesFile.getContext().getLocalizationLevel() == 
/*  956 */         LocalizationContext.LocalizationLevel.USER)
/*      */       {
/*  957 */         MessageDialog confirmDlg = new MessageDialog(
/*  958 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  959 */           "Confirm?", null, "The Product Types File Exists\nDo you want to overwrite it?", 
/*  961 */           4, new String[] { "Yes", "No" }, 0);
/*      */ 
/*  963 */         if (confirmDlg.getReturnCode() != 1);
/*      */       }
/*      */       else
/*      */       {
/*  969 */         LocalizationContext userContext = PgenStaticDataProvider.getProvider().getLocalizationContext(
/*  970 */           prdTypesFile.getContext().getLocalizationType(), 
/*  971 */           LocalizationContext.LocalizationLevel.USER);
/*  972 */         prdTypesFile = PgenStaticDataProvider.getProvider().getLocalizationFile(
/*  973 */           userContext, PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "productTypes.xml");
/*      */       }
/*  975 */       File ptypFile = prdTypesFile.getFile();
/*      */ 
/*  977 */       if (ptypFile == null) {
/*  978 */         throw new VizException("Unable to create Localization File");
/*      */       }
/*      */ 
/*  981 */       FileTools.write(ptypFile.getAbsolutePath(), this.prdTyps);
/*      */       try
/*      */       {
/*  984 */         prdTypesFile.save();
/*      */       } catch (LocalizationOpFailedException e) {
/*  986 */         throw new VizException(e);
/*      */       }
/*      */     }
/*      */     catch (VizException ve) {
/*  990 */       MessageDialog errDlg = new MessageDialog(
/*  991 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  992 */         "Error", null, "Error saving Product Types.\n" + ve.getMessage(), 
/*  993 */         1, new String[] { "OK" }, 0);
/*  994 */       errDlg.open();
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean updateProductTypes()
/*      */   {
/* 1004 */     ProductType pTyp = null;
/* 1005 */     boolean validNewTypeName = true;
/*      */ 
/* 1007 */     String typeCmbName = this.typeCombo.getText();
/* 1008 */     String typeInputName = this.typeText.getText();
/*      */ 
/* 1010 */     String subtypeCmbName = this.subtypeCombo.getText();
/* 1011 */     String subtypeInputName = this.subtypeText.getText();
/*      */ 
/* 1013 */     String aliasName = this.aliasText.getText();
/*      */ 
/* 1015 */     validNewTypeName = validatePrdTypName(typeCmbName, typeInputName, subtypeCmbName, 
/* 1016 */       subtypeInputName, aliasName);
/*      */ 
/* 1018 */     if (validNewTypeName)
/*      */     {
/* 1020 */       pTyp = findProductType(typeInputName, subtypeInputName);
/* 1021 */       int existingAt = -1;
/* 1022 */       if (pTyp != null) {
/* 1023 */         existingAt = this.prdTyps.getProductType().indexOf(pTyp);
/*      */       }
/*      */       else {
/* 1026 */         pTyp = copyProductType(findProductType(typeCmbName, subtypeCmbName));
/* 1027 */         if (pTyp == null) {
/* 1028 */           pTyp = new ProductType();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1033 */       pTyp.setType(new String(typeInputName));
/* 1034 */       if ((subtypeInputName != null) && (subtypeInputName.trim().length() > 0)) {
/* 1035 */         pTyp.setSubtype(new String(subtypeInputName));
/*      */       }
/*      */       else {
/* 1038 */         pTyp.setSubtype(DEFAULT_SUBTYPE);
/*      */       }
/*      */ 
/* 1041 */       if ((aliasName != null) && (aliasName.trim().length() > 0)) {
/* 1042 */         pTyp.setName(aliasName);
/*      */       }
/*      */       else {
/* 1045 */         pTyp.setName("");
/*      */       }
/*      */ 
/* 1048 */       if (getCurrentTabName().equals("Palette")) {
/* 1049 */         updatePalette(pTyp);
/*      */       }
/* 1051 */       else if (getCurrentTabName().equals("Layer")) {
/* 1052 */         updateLayers(pTyp);
/* 1053 */         if (existingAt < 0) updatePalette(pTyp);
/*      */       }
/* 1055 */       else if (getCurrentTabName().equals("Save")) {
/* 1056 */         updateSaveInfo(pTyp);
/* 1057 */         if (existingAt < 0) updatePalette(pTyp);
/*      */       }
/* 1059 */       else if (getCurrentTabName().equalsIgnoreCase("Settings")) {
/* 1060 */         updateSettings(pTyp);
/*      */       }
/* 1062 */       else if (getCurrentTabName().equalsIgnoreCase("Clip")) {
/* 1063 */         updateClip(pTyp);
/*      */       }
/* 1065 */       else if (getCurrentTabName().equalsIgnoreCase("Products")) {
/* 1066 */         pTyp.getProdType().clear();
/* 1067 */         if (this.ptList != null) pTyp.getProdType().addAll(this.ptList);
/* 1068 */         if (existingAt < 0) updatePalette(pTyp);
/*      */       }
/*      */ 
/* 1071 */       if (existingAt < 0)
/*      */       {
/* 1073 */         ArrayList allTypes = findTypes();
/* 1074 */         this.prdTyps.getProductType().add(pTyp);
/*      */ 
/* 1077 */         if (!allTypes.contains(typeInputName))
/*      */         {
/* 1079 */           this.typeCombo.add(typeInputName);
/* 1080 */           this.typeCombo.pack();
/* 1081 */           this.typeCombo.select(this.typeCombo.getItemCount() - 1);
/*      */ 
/* 1084 */           this.subtypeCombo.removeAll();
/* 1085 */           this.subtypeCombo.add("New");
/* 1086 */           ArrayList stypes = findSubtypes(typeInputName);
/*      */ 
/* 1088 */           for (String stp : stypes) {
/* 1089 */             this.subtypeCombo.add(stp);
/*      */           }
/*      */ 
/* 1092 */           int select = stypes.indexOf(pTyp.getSubtype());
/* 1093 */           select++;
/*      */ 
/* 1095 */           this.subtypeCombo.select(select);
/* 1096 */           this.subtypeText.setText(this.subtypeCombo.getText());
/*      */         }
/*      */         else
/*      */         {
/* 1100 */           int ii = 0;
/* 1101 */           for (String tp : this.typeCombo.getItems()) {
/* 1102 */             if (tp.equals(typeInputName)) {
/* 1103 */               this.typeCombo.select(ii);
/* 1104 */               break;
/*      */             }
/* 1106 */             ii++;
/*      */           }
/*      */ 
/* 1109 */           String subtype = pTyp.getSubtype();
/* 1110 */           this.subtypeCombo.add(subtype);
/* 1111 */           this.subtypeText.setText(subtype);
/*      */ 
/* 1113 */           this.subtypeCombo.pack();
/* 1114 */           this.subtypeCombo.select(this.subtypeCombo.getItemCount() - 1);
/*      */         }
/*      */ 
/* 1117 */         this.typeCombo.getParent().pack();
/*      */ 
/* 1120 */         updateSaveInfo(pTyp);
/* 1121 */         String fname = createDefaultOutputFile();
/* 1122 */         pTyp.getPgenSave().setOutputFile(fname);
/* 1123 */         this.saveOutputTxt.setText(fname);
/*      */       }
/*      */       else
/*      */       {
/* 1127 */         this.prdTyps.getProductType().set(existingAt, pTyp);
/*      */       }
/*      */ 
/* 1130 */       this.aliasText.setText(pTyp.getName());
/*      */ 
/* 1132 */       this.activityGrp.pack();
/*      */     }
/*      */ 
/* 1136 */     return validNewTypeName;
/*      */   }
/*      */ 
/*      */   private void createObjects(String className, boolean disposeOnly)
/*      */   {
/* 1145 */     ArrayList pgObjs = (ArrayList)PgenSession.getInstance().getPgenPalette().getObjectNames(className);
/* 1146 */     objects = new ArrayList();
/* 1147 */     if (className.equals("Symbol")) {
/* 1148 */       for (String st : pgObjs) {
/* 1149 */         if (!st.contains("TURBULENCE")) {
/* 1150 */           objects.add(st);
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 1155 */       objects.addAll(pgObjs);
/*      */     }
/*      */ 
/* 1158 */     if (this.objectGroup != null)
/*      */     {
/* 1160 */       Control[] wids = this.objectGroup.getChildren();
/*      */ 
/* 1162 */       if (wids != null) {
/* 1163 */         for (int kk = 0; kk < wids.length; kk++) {
/* 1164 */           wids[kk].dispose();
/*      */         }
/*      */       }
/*      */ 
/* 1168 */       this.objectGroup.dispose();
/*      */     }
/*      */ 
/* 1172 */     Control[] wids = this.controlBtnComp.getChildren();
/* 1173 */     for (int jj = 0; jj < wids.length; jj++) {
/* 1174 */       wids[jj].dispose();
/*      */     }
/*      */ 
/* 1177 */     this.controlBtnComp.dispose();
/*      */ 
/* 1181 */     this.shell.pack();
/*      */ 
/* 1183 */     if (disposeOnly) {
/* 1184 */       this.objectGroup = null;
/*      */     }
/*      */     else
/*      */     {
/* 1188 */       this.objectGroup = new Group(this.paletteComp, 4);
/* 1189 */       this.objectGroup.setText(className + " Objects");
/* 1190 */       this.objectGroup.setData(className);
/* 1191 */       this.objectGroup.setSize(400, 200);
/*      */ 
/* 1193 */       int maxRow = 8;
/* 1194 */       int ncol = 10;
/* 1195 */       if (objects.size() / 10 > maxRow) {
/* 1196 */         ncol = objects.size() / maxRow;
/*      */       }
/*      */ 
/* 1199 */       GridLayout ly = new GridLayout(ncol, false);
/* 1200 */       ly.horizontalSpacing = 0;
/* 1201 */       ly.makeColumnsEqualWidth = false;
/* 1202 */       ly.marginHeight = 0;
/* 1203 */       ly.marginWidth = 0;
/* 1204 */       ly.verticalSpacing = 0;
/*      */ 
/* 1206 */       if (ncol != 10) {
/* 1207 */         this.objectGroup.setLayout(ly);
/*      */       }
/*      */       else {
/* 1210 */         this.objectGroup.setLayout(new GridLayout(ncol, false));
/*      */       }
/*      */ 
/* 1213 */       this.objectBtns = new Button[objects.size()];
/*      */ 
/* 1215 */       int ii = 0;
/* 1216 */       ProductType ptype = getCurrentPrdTyp();
/*      */       Object localObject;
/* 1217 */       for (String str : objects)
/*      */       {
/* 1219 */         this.objectBtns[ii] = new Button(this.objectGroup, 2);
/* 1220 */         this.objectBtns[ii].setData(str);
/* 1221 */         this.objectBtns[ii].setImage((Image)this.iconMap.get(str));
/* 1222 */         this.objectBtns[ii].setSelection(btnAlwaysOn(str));
/*      */ 
/* 1224 */         this.objectBtns[ii].setToolTipText(((IConfigurationElement)this.itemMap.get(str)).getAttribute("label"));
/*      */ 
/* 1226 */         objStr = new ArrayList();
/* 1227 */         if ((ptype != null) && (ptype.getPgenClass() != null)) {
/* 1228 */           for (localObject = ptype.getPgenClass().iterator(); ((Iterator)localObject).hasNext(); ) { pclass = (PgenClass)((Iterator)localObject).next();
/* 1229 */             if (pclass.getName().equalsIgnoreCase(className)) {
/* 1230 */               for (int kk = 0; kk < pclass.getPgenObjects().getName().size(); kk++) {
/* 1231 */                 if (pclass.getPgenObjects() != null) {
/* 1232 */                   objStr.add((String)pclass.getPgenObjects().getName().get(kk));
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1239 */         if (objStr.contains(str))
/*      */         {
/* 1241 */           this.objectBtns[ii].setSelection(true);
/* 1242 */           this.objectBtns[ii].setImage((Image)this.iconMapSelected.get(str));
/*      */         }
/*      */ 
/* 1245 */         this.objectBtns[ii].addSelectionListener(new SelectionAdapter()
/*      */         {
/*      */           public void widgetSelected(SelectionEvent event) {
/* 1248 */             Button btn = (Button)event.widget;
/* 1249 */             String name = event.widget.getData().toString();
/*      */ 
/* 1251 */             if (ProductConfigureDialog.this.btnAlwaysOn(name)) {
/* 1252 */               btn.setSelection(true);
/*      */             }
/*      */ 
/* 1255 */             if (btn.getSelection()) {
/* 1256 */               btn.setImage((Image)ProductConfigureDialog.this.iconMapSelected.get(name));
/*      */             }
/*      */             else
/* 1259 */               btn.setImage((Image)ProductConfigureDialog.this.iconMap.get(name));
/*      */           }
/*      */         });
/* 1264 */         ii++;
/*      */       }
/*      */ 
/* 1267 */       boolean allOff = true;
/* 1268 */       PgenClass pclass = (localObject = this.objectBtns).length; for (ArrayList objStr = 0; objStr < pclass; objStr++) { Button btn = localObject[objStr];
/* 1269 */         if (btn.getSelection()) {
/* 1270 */           allOff = false;
/* 1271 */           break;
/*      */         }
/*      */       }
/*      */ 
/* 1275 */       this.allOnOffBtn = new Button(this.objectGroup, 8);
/* 1276 */       setButtonColor(this.allOnOffBtn, this.remindButtonColor);
/*      */ 
/* 1278 */       if (allOff) {
/* 1279 */         this.allOnOffBtn.setText("ON ");
/* 1280 */         this.allOnOffBtn.setToolTipText("Select ALL Objects");
/* 1281 */         this.allOnOffBtn.setData("ONN");
/*      */       }
/*      */       else {
/* 1284 */         this.allOnOffBtn.setText("OFF");
/* 1285 */         this.allOnOffBtn.setToolTipText("Deselect ALL Objects");
/* 1286 */         this.allOnOffBtn.setData("OFF");
/*      */       }
/*      */ 
/* 1289 */       this.allOnOffBtn.addSelectionListener(new SelectionAdapter() {
/*      */         public void widgetSelected(SelectionEvent event) {
/* 1291 */           String status = event.widget.getData().toString();
/* 1292 */           if (status.equalsIgnoreCase("ONN")) {
/* 1293 */             ProductConfigureDialog.this.allOnOffBtn.setText("OFF");
/* 1294 */             ProductConfigureDialog.this.allOnOffBtn.setToolTipText("Deselect ALL Objects");
/* 1295 */             ProductConfigureDialog.this.allOnOffBtn.setData("OFF");
/*      */ 
/* 1297 */             for (Button btn : ProductConfigureDialog.this.objectBtns) {
/* 1298 */               btn.setSelection(true);
/* 1299 */               btn.setImage((Image)ProductConfigureDialog.this.iconMapSelected.get(btn.getData().toString()));
/*      */             }
/*      */           }
/*      */           else {
/* 1303 */             ProductConfigureDialog.this.allOnOffBtn.setText("ON ");
/* 1304 */             ProductConfigureDialog.this.allOnOffBtn.setToolTipText("Select ALL Objects");
/* 1305 */             ProductConfigureDialog.this.allOnOffBtn.setData("ONN");
/*      */ 
/* 1307 */             for (Button btn : ProductConfigureDialog.this.objectBtns) {
/* 1308 */               btn.setSelection(false);
/* 1309 */               btn.setImage((Image)ProductConfigureDialog.this.iconMap.get(btn.getData().toString()));
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/* 1318 */     createControlBtns(this.topComp);
/*      */ 
/* 1329 */     this.shell.pack();
/*      */ 
/* 1331 */     this.shell.layout();
/*      */   }
/*      */ 
/*      */   private void createControlBtns(Composite comp)
/*      */   {
/* 1340 */     this.controlBtnComp = new Composite(comp, 0);
/* 1341 */     GridLayout gl4 = new GridLayout(4, true);
/* 1342 */     GridData gd = new GridData(4, -1, true, false);
/* 1343 */     this.controlBtnComp.setLayout(gl4);
/* 1344 */     this.controlBtnComp.setLayoutData(gd);
/*      */ 
/* 1346 */     int ii = 0;
/* 1347 */     for (String str : this.cntlBtnNames) {
/* 1348 */       this.dlgCntlBtn[ii] = new Button(this.controlBtnComp, 0);
/* 1349 */       this.dlgCntlBtn[ii].setText(str);
/* 1350 */       this.dlgCntlBtn[ii].setToolTipText(this.cntlBtnTips[ii]);
/* 1351 */       this.dlgCntlBtn[ii].setLayoutData(gd);
/* 1352 */       this.dlgCntlBtn[ii].setData(str);
/* 1353 */       this.dlgCntlBtn[ii].addSelectionListener(new SelectionAdapter() {
/*      */         public void widgetSelected(SelectionEvent event) {
/* 1355 */           ProductConfigureDialog.this.dialogActions(event.widget.getData().toString());
/*      */         }
/*      */       });
/* 1359 */       ii++;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void createTypeComp(Composite comp)
/*      */   {
/* 1369 */     this.typeComp = new Composite(comp, 0);
/* 1370 */     this.typeComp.setLayout(new GridLayout(3, false));
/*      */ 
/* 1372 */     Label typeLbl = new Label(this.typeComp, 16384);
/* 1373 */     typeLbl.setText("Type:");
/*      */ 
/* 1375 */     this.typeCombo = new Combo(this.typeComp, 12);
/*      */ 
/* 1377 */     this.typeCombo.add("New");
/* 1378 */     for (String st : findTypes()) {
/* 1379 */       this.typeCombo.add(st);
/*      */     }
/*      */ 
/* 1382 */     this.typeCombo.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/* 1384 */         ProductConfigureDialog.this.switchProductType();
/*      */       }
/*      */     });
/* 1391 */     this.typeCombo.select(0);
/*      */ 
/* 1393 */     this.typeText = new Text(this.typeComp, 2052);
/* 1394 */     this.typeText.setLayoutData(new GridData(150, 15));
/* 1395 */     this.typeText.setEditable(true);
/* 1396 */     this.typeText.setText("");
/*      */ 
/* 1399 */     Label stypeLbl = new Label(this.typeComp, 16384);
/* 1400 */     stypeLbl.setText("Subtype:");
/*      */ 
/* 1402 */     this.subtypeCombo = new Combo(this.typeComp, 12);
/*      */ 
/* 1404 */     this.subtypeCombo.add("New");
/*      */ 
/* 1406 */     this.subtypeCombo.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/* 1408 */         ProductConfigureDialog.this.switchProductSubtype();
/*      */       }
/*      */     });
/* 1412 */     this.subtypeCombo.select(0);
/*      */ 
/* 1414 */     this.subtypeText = new Text(this.typeComp, 2052);
/* 1415 */     this.subtypeText.setLayoutData(new GridData(150, 15));
/* 1416 */     this.subtypeText.setEditable(true);
/* 1417 */     this.subtypeText.setText("");
/*      */ 
/* 1420 */     Label aliasLbl = new Label(this.typeComp, 16384);
/* 1421 */     aliasLbl.setText("Alias:");
/*      */ 
/* 1423 */     this.aliasText = new Text(this.typeComp, 2052);
/* 1424 */     this.aliasText.setLayoutData(new GridData(90, 15));
/* 1425 */     this.aliasText.setEditable(true);
/* 1426 */     this.aliasText.setText("");
/*      */   }
/*      */ 
/*      */   private void retrievePgenPalette()
/*      */   {
/* 1436 */     PgenPaletteWindow plt = PgenSession.getInstance().getPgenPalette();
/*      */ 
/* 1438 */     this.itemMap = plt.getItemMap();
/*      */ 
/* 1440 */     controls = (ArrayList)plt.getControlNames();
/* 1441 */     actions = (ArrayList)plt.getActionNames();
/* 1442 */     classes = (ArrayList)plt.getClassNames();
/*      */ 
/* 1444 */     this.controlBtns = new Button[controls.size()];
/* 1445 */     this.actionBtns = new Button[actions.size()];
/* 1446 */     this.classChkBtns = new Button[classes.size()];
/* 1447 */     this.classPushBtns = new Button[classes.size()];
/*      */ 
/* 1449 */     this.iconMap = new HashMap();
/* 1450 */     this.iconMapSelected = new HashMap();
/*      */ 
/* 1452 */     Image img = null;
/* 1453 */     for (String str : controls) {
/* 1454 */       img = plt.getButtonImage(str);
/* 1455 */       this.iconMap.put(str, img);
/* 1456 */       this.iconMapSelected.put(str, plt.createNewImage(img, 16777215, 255));
/*      */     }
/*      */ 
/* 1459 */     for (String str : actions) {
/* 1460 */       img = plt.getButtonImage(str);
/* 1461 */       this.iconMap.put(str, img);
/* 1462 */       this.iconMapSelected.put(str, plt.createNewImage(img, 16777215, 255));
/*      */     }
/*      */ 
/* 1465 */     for (String str : classes) {
/* 1466 */       img = plt.getButtonImage(str);
/* 1467 */       this.iconMap.put(str, img);
/* 1468 */       this.iconMapSelected.put(str, plt.createNewImage(img, 16777215, 255));
/*      */     }
/*      */ 
/* 1471 */     for (String str : plt.getObjectNames()) {
/* 1472 */       img = plt.getButtonImage(str);
/* 1473 */       this.iconMap.put(str, img);
/* 1474 */       this.iconMapSelected.put(str, plt.createNewImage(img, 16777215, 255));
/*      */     }
/*      */ 
/* 1477 */     this.dlgCntlBtn = new Button[this.cntlBtnNames.length];
/*      */   }
/*      */ 
/*      */   private ArrayList<String> getSelectedObjects(String className)
/*      */   {
/* 1486 */     ArrayList objList = new ArrayList();
/*      */ 
/* 1488 */     if ((this.objectGroup != null) && (className.equalsIgnoreCase(this.objectGroup.getData().toString()))) {
/* 1489 */       for (Button obtn : this.objectBtns) {
/* 1490 */         if (obtn.getSelection()) {
/* 1491 */           objList.add(obtn.getData().toString());
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1497 */       ProductType ptype = getCurrentPrdTyp();
/*      */ 
/* 1499 */       if (ptype == null) {
/* 1500 */         objList = (ArrayList)PgenSession.getInstance().getPgenPalette().getObjectNames(className);
/*      */       }
/*      */       else {
/* 1503 */         PgenClass pclass = null;
/* 1504 */         if ((ptype != null) && (ptype.getPgenClass() != null)) {
/* 1505 */           for (??? = ptype.getPgenClass().iterator(); ((Iterator)???).hasNext(); ) { PgenClass cls = (PgenClass)((Iterator)???).next();
/* 1506 */             if (cls.getName().equalsIgnoreCase(className)) {
/* 1507 */               pclass = cls;
/* 1508 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1513 */         if (pclass != null) {
/* 1514 */           objList.addAll(pclass.getPgenObjects().getName());
/*      */         }
/*      */         else {
/* 1517 */           objList = (ArrayList)PgenSession.getInstance().getPgenPalette().getObjectNames(className);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1524 */     return objList;
/*      */   }
/*      */ 
/*      */   private boolean btnAlwaysOn(String btnName)
/*      */   {
/* 1533 */     String always = ((IConfigurationElement)this.itemMap.get(btnName)).getAttribute("alwaysVisible");
/*      */ 
/* 1535 */     if ((always == null) || (always.equalsIgnoreCase("false"))) {
/* 1536 */       return false;
/*      */     }
/*      */ 
/* 1539 */     return true;
/*      */   }
/*      */ 
/*      */   private void createDefaultTab(Composite cmp)
/*      */   {
/* 1547 */     GridLayout gl = new GridLayout(1, false);
/* 1548 */     cmp.setLayout(gl);
/*      */ 
/* 1551 */     Button button = new Button(cmp, 8);
/* 1552 */     button.setText("This Page is Under Construction! ");
/*      */   }
/*      */ 
/*      */   private void createLayeringTab(Composite cmp)
/*      */   {
/* 1561 */     Control[] wids = cmp.getChildren();
/* 1562 */     for (int jj = 0; jj < wids.length; jj++) {
/* 1563 */       wids[jj].dispose();
/*      */     }
/*      */ 
/* 1566 */     cmp.pack();
/*      */ 
/* 1568 */     GridLayout gly = new GridLayout(1, false);
/* 1569 */     cmp.setLayout(gly);
/*      */ 
/* 1571 */     Group layerEditGrp = new Group(cmp, 0);
/* 1572 */     GridLayout gl0 = new GridLayout(1, false);
/* 1573 */     gl0.verticalSpacing = 0;
/* 1574 */     layerEditGrp.setLayout(gl0);
/* 1575 */     layerEditGrp.setText("Define/Edit Layers");
/*      */ 
/* 1578 */     Composite nameComp = new Composite(layerEditGrp, 0);
/* 1579 */     nameComp.setLayout(new GridLayout(4, false));
/*      */ 
/* 1581 */     Label typeLbl = new Label(nameComp, 16384);
/* 1582 */     typeLbl.setText("Layer:");
/*      */ 
/* 1584 */     this.layerNameCombo = new Combo(nameComp, 12);
/*      */ 
/* 1586 */     this.layerNameCombo.add("New");
/*      */ 
/* 1588 */     ProductType curPrdType = getCurrentPrdTyp();
/*      */ 
/* 1590 */     if (curPrdType != null) {
/* 1591 */       for (PgenLayer lyr : curPrdType.getPgenLayer()) {
/* 1592 */         this.layerNameCombo.add(lyr.getName());
/*      */       }
/*      */     }
/*      */ 
/* 1596 */     this.layerNameCombo.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/* 1598 */         ProductConfigureDialog.this.refreshLayerInput();
/*      */       }
/*      */     });
/* 1602 */     this.layerNameCombo.select(0);
/*      */ 
/* 1604 */     this.layerNameTxt = new Text(nameComp, 2052);
/* 1605 */     this.layerNameTxt.setLayoutData(new GridData(80, 15));
/* 1606 */     this.layerNameTxt.setEditable(true);
/* 1607 */     this.layerNameTxt.setText("");
/*      */ 
/* 1609 */     Button deleteBtn = new Button(nameComp, 8);
/* 1610 */     deleteBtn.setText("Delete");
/* 1611 */     setButtonColor(deleteBtn, this.activeButtonColor);
/* 1612 */     deleteBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/* 1614 */         ProductConfigureDialog.this.removeOneLayer();
/*      */       }
/*      */     });
/* 1620 */     Composite dispComp = new Composite(layerEditGrp, 0);
/* 1621 */     GridLayout gl4 = new GridLayout(5, false);
/* 1622 */     gl4.marginWidth = 3;
/* 1623 */     dispComp.setLayout(gl4);
/*      */ 
/* 1625 */     Label dispLbl = new Label(dispComp, 16384);
/* 1626 */     dispLbl.setText("Display:");
/*      */ 
/* 1628 */     this.layerOnOffBtn = new Button(dispComp, 32);
/* 1629 */     this.layerOnOffBtn.setText("OnOff");
/*      */ 
/* 1631 */     this.layerOnOffBtn.setSelection(false);
/*      */ 
/* 1633 */     this.layerMonoBtn = new Button(dispComp, 32);
/* 1634 */     this.layerMonoBtn.setText("A/M");
/*      */ 
/* 1636 */     this.layerColorSelector = new ColorButtonSelector(dispComp);
/* 1637 */     java.awt.Color clr = java.awt.Color.RED;
/* 1638 */     this.layerColorSelector.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*      */ 
/* 1640 */     this.layerFillBtn = new Button(dispComp, 32);
/* 1641 */     this.layerFillBtn.setText("Filled");
/*      */ 
/* 1708 */     GridLayout gl = new GridLayout(1, false);
/* 1709 */     this.layerTempGrp = new Group(cmp, 0);
/* 1710 */     this.layerTempGrp.setLayout(gl);
/* 1711 */     this.layerTempGrp.setText("Layers Defined");
/*      */ 
/* 1713 */     if (curPrdType != null) {
/* 1714 */       createLayersTemplate(this.layerTempGrp, curPrdType.getPgenLayer());
/*      */     }
/*      */ 
/* 1717 */     this.shell.pack(true);
/*      */   }
/*      */ 
/*      */   private void createLayersTemplate(Composite cmp, List<PgenLayer> layers)
/*      */   {
/* 1727 */     Control[] wids = cmp.getChildren();
/* 1728 */     for (int jj = 0; jj < wids.length; jj++) {
/* 1729 */       wids[jj].dispose();
/*      */     }
/*      */ 
/* 1732 */     cmp.pack();
/*      */ 
/* 1734 */     int ncomp = 3;
/* 1735 */     if (layers.size() <= 8) {
/* 1736 */       ncomp = 1;
/*      */     }
/*      */ 
/* 1739 */     Composite topComp = new Composite(cmp, 0);
/* 1740 */     GridLayout gl = new GridLayout(ncomp, false);
/* 1741 */     gl.marginHeight = 1;
/* 1742 */     gl.marginWidth = 1;
/* 1743 */     gl.verticalSpacing = 1;
/* 1744 */     gl.horizontalSpacing = 40;
/* 1745 */     topComp.setLayout(gl);
/*      */ 
/* 1747 */     Composite layersComp = new Composite(topComp, 0);
/* 1748 */     GridLayout gl2 = new GridLayout(3, false);
/*      */ 
/* 1750 */     gl2.marginHeight = 1;
/* 1751 */     gl2.marginWidth = 1;
/* 1752 */     gl2.verticalSpacing = 1;
/* 1753 */     gl2.horizontalSpacing = 5;
/*      */ 
/* 1755 */     layersComp.setLayout(gl2);
/*      */ 
/* 1757 */     Composite layersComp2 = null;
/* 1758 */     if (ncomp > 1) {
/* 1759 */       GridData gd = new GridData(1040);
/* 1760 */       Label sepLbl = new Label(topComp, 514);
/* 1761 */       sepLbl.setLayoutData(gd);
/*      */ 
/* 1763 */       layersComp2 = new Composite(topComp, 0);
/* 1764 */       GridLayout gl3 = new GridLayout(3, false);
/* 1765 */       gl3.marginHeight = 1;
/* 1766 */       gl3.marginWidth = 1;
/* 1767 */       gl3.verticalSpacing = 1;
/* 1768 */       gl3.horizontalSpacing = 5;
/* 1769 */       layersComp2.setLayout(gl2);
/*      */     }
/*      */ 
/* 1773 */     this.layerNameBtns = new ArrayList();
/*      */ 
/* 1775 */     int nlayers = layers.size();
/* 1776 */     int mod = nlayers - nlayers / 2 * 2;
/* 1777 */     int nleft = Math.max(8, layers.size() / 2 + mod);
/* 1778 */     int ii = 0;
/* 1779 */     for (PgenLayer lyr : layers)
/*      */     {
/*      */       Composite whichCmp;
/*      */       Composite whichCmp;
/* 1781 */       if (ii < nleft) {
/* 1782 */         whichCmp = layersComp;
/*      */       }
/*      */       else {
/* 1785 */         whichCmp = layersComp2;
/*      */       }
/*      */ 
/* 1788 */       Button nameBtn = new Button(whichCmp, 8);
/* 1789 */       nameBtn.setText(lyr.getName().replace("&", "&&"));
/* 1790 */       setButtonColor(nameBtn, this.defaultButtonColor);
/*      */ 
/* 1792 */       nameBtn.setData(Integer.valueOf(ii));
/* 1793 */       nameBtn.addSelectionListener(new SelectionAdapter() {
/*      */         public void widgetSelected(SelectionEvent event) {
/* 1795 */           ProductConfigureDialog.this.switchLayer(((Button)event.widget).getText().replace("&&", "&"));
/*      */         }
/*      */       });
/* 1800 */       this.layerNameBtns.add(nameBtn);
/*      */ 
/* 1802 */       Button dispBtn = new Button(whichCmp, 32);
/* 1803 */       dispBtn.setSelection(lyr.isOnOff().booleanValue());
/* 1804 */       dispBtn.setEnabled(false);
/*      */ 
/* 1806 */       Button clrBtn = new Button(whichCmp, 8);
/* 1807 */       clrBtn.setText("A/F");
/*      */ 
/* 1809 */       java.awt.Color clr = new java.awt.Color(lyr.getColor().getRed(), 
/* 1810 */         lyr.getColor().getGreen(), 
/* 1811 */         lyr.getColor().getBlue(), 
/* 1812 */         lyr.getColor().getAlpha().intValue());
/*      */ 
/* 1814 */       setButtonColor(clrBtn, clr);
/*      */ 
/* 1819 */       ii++;
/*      */     }
/*      */ 
/* 1822 */     cmp.pack();
/* 1823 */     this.shell.pack(true);
/*      */   }
/*      */ 
/*      */   private void createFilterTab(Composite parent)
/*      */   {
/* 1832 */     GridLayout gly = new GridLayout(1, false);
/* 1833 */     parent.setLayout(gly);
/*      */ 
/* 1835 */     Composite titleComp = new Composite(parent, 0);
/* 1836 */     GridLayout gl0 = new GridLayout(2, true);
/* 1837 */     titleComp.setLayout(gl0);
/*      */ 
/* 1839 */     Label filters = new Label(titleComp, 0);
/* 1840 */     filters.setText("Forecast Hour Filters:");
/*      */ 
/* 1842 */     Button allOnBtn = new Button(titleComp, 32);
/* 1843 */     allOnBtn.setText("All On");
/*      */ 
/* 1845 */     Composite hoursComp = new Composite(parent, 0);
/*      */ 
/* 1847 */     GridLayout mainLayout = new GridLayout(HOURS.length / 3, false);
/*      */ 
/* 1849 */     mainLayout.marginHeight = 3;
/* 1850 */     mainLayout.marginWidth = 3;
/*      */ 
/* 1852 */     hoursComp.setLayout(mainLayout);
/*      */ 
/* 1854 */     Button[] hourBtns = new Button[HOURS.length];
/*      */ 
/* 1856 */     for (int ii = 0; ii < HOURS.length; ii++) {
/* 1857 */       hourBtns[ii] = new Button(hoursComp, 32);
/* 1858 */       hourBtns[ii].setText(HOURS[ii]);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void createSettingsTab(Composite parent)
/*      */   {
/* 1866 */     GridLayout gly = new GridLayout(1, false);
/* 1867 */     gly.verticalSpacing = 10;
/* 1868 */     parent.setLayout(gly);
/*      */ 
/* 1870 */     Label currentSettingLbl = new Label(parent, 0);
/* 1871 */     currentSettingLbl.setText("Settings: ");
/*      */ 
/* 1873 */     this.currentSettingsTxt = new Text(parent, 2052);
/*      */ 
/* 1875 */     this.currentSettingsTxt.setEditable(false);
/* 1876 */     this.currentSettingsTxt.setText("Localization\\NCP\\PGEN\\" + getCurrentSetting(this.typeText.getText()));
/* 1877 */     this.currentSettingsTxt.setEnabled(false);
/*      */ 
/* 1879 */     Group prdComp = new Group(parent, 0);
/* 1880 */     RowLayout rl = new RowLayout(256);
/* 1881 */     rl.center = true;
/* 1882 */     prdComp.setLayout(rl);
/*      */ 
/* 1884 */     Label tlbl = new Label(prdComp, 0);
/* 1885 */     tlbl.setText("Load Settings From:");
/*      */ 
/* 1891 */     this.settingsTxt = new Text(prdComp, 2052);
/* 1892 */     this.settingsTxt.setLayoutData(new RowData(200, 15));
/* 1893 */     this.settingsTxt.setEditable(true);
/* 1894 */     this.settingsTxt.setText("");
/*      */ 
/* 1896 */     Button nameBtn = new Button(prdComp, 8);
/* 1897 */     nameBtn.setText("Browse");
/*      */ 
/* 1899 */     nameBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent event) {
/* 1902 */         String[] filterNames = { "XML Files (*.xml)" };
/* 1903 */         String[] filterExtensions = { "*.xml" };
/*      */ 
/* 1905 */         String filterPath = PgenUtil.getWorkingDirectory();
/*      */ 
/* 1907 */         String selectedFile = ProductConfigureDialog.this.selectFile(ProductConfigureDialog.this.shell, 4096, filterNames, 
/* 1908 */           filterExtensions, filterPath, null, false);
/* 1909 */         if (selectedFile != null)
/* 1910 */           ProductConfigureDialog.this.settingsTxt.setText(selectedFile);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void createSaveTab(Composite parent)
/*      */   {
/* 1922 */     GridLayout gly = new GridLayout(1, false);
/* 1923 */     gly.verticalSpacing = 10;
/* 1924 */     parent.setLayout(gly);
/*      */ 
/* 1926 */     Group prdComp = new Group(parent, 0);
/* 1927 */     GridLayout gl0 = new GridLayout(1, true);
/* 1928 */     prdComp.setLayout(gl0);
/*      */ 
/* 1930 */     Label tlbl = new Label(prdComp, 0);
/* 1931 */     tlbl.setText("Save This Product To:");
/*      */ 
/* 1936 */     GridLayout mainLayout = new GridLayout(3, false);
/*      */ 
/* 1938 */     mainLayout.marginHeight = 3;
/* 1939 */     mainLayout.marginWidth = 3;
/*      */ 
/* 1941 */     Composite nameComp = new Composite(prdComp, 0);
/*      */ 
/* 1943 */     nameComp.setLayout(mainLayout);
/*      */ 
/* 1945 */     Label nameLbl = new Label(nameComp, 0);
/* 1946 */     nameLbl.setText("File:");
/*      */ 
/* 1948 */     this.saveOutputTxt = new Text(nameComp, 2052);
/* 1949 */     this.saveOutputTxt.setLayoutData(new GridData(200, 15));
/* 1950 */     this.saveOutputTxt.setEditable(true);
/*      */ 
/* 1952 */     this.saveOutputTxt.setText(createDefaultOutputFile());
/*      */ 
/* 1954 */     Button nameBtn = new Button(nameComp, 8);
/* 1955 */     nameBtn.setText("Browse");
/*      */ 
/* 1974 */     nameBtn.setEnabled(false);
/*      */ 
/* 1980 */     Group titleComp = new Group(parent, 0);
/* 1981 */     titleComp.setLayout(gl0);
/*      */ 
/* 1983 */     Label albl = new Label(titleComp, 0);
/* 1984 */     albl.setText("Do you want to save each layer individually?");
/*      */ 
/* 1986 */     this.saveIndividualLayerBtn = new Button(titleComp, 32);
/* 1987 */     this.saveIndividualLayerBtn.setText("Save layers individually ");
/* 1988 */     this.saveIndividualLayerBtn.setSelection(true);
/* 1989 */     this.saveIndividualLayerBtn.setSelection(false);
/*      */ 
/* 1994 */     Group autosaveComp = new Group(parent, 0);
/* 1995 */     autosaveComp.setLayout(mainLayout);
/*      */ 
/* 1997 */     GridLayout asLayout = new GridLayout(3, false);
/*      */ 
/* 1999 */     asLayout.marginHeight = 3;
/* 2000 */     asLayout.marginWidth = 3;
/*      */ 
/* 2002 */     this.autoSaveBtn = new Button(autosaveComp, 32);
/* 2003 */     this.autoSaveBtn.setText("Auto save");
/* 2004 */     this.autoSaveBtn.setSelection(true);
/* 2005 */     this.autoSaveBtn.setEnabled(false);
/*      */ 
/* 2007 */     Label lbl = new Label(autosaveComp, 0);
/* 2008 */     lbl.setText("      Frequency (min): ");
/*      */ 
/* 2010 */     this.autoSaveFreqTxt = new Text(autosaveComp, 2052);
/* 2011 */     this.autoSaveFreqTxt.setLayoutData(new GridData(60, 15));
/* 2012 */     this.autoSaveFreqTxt.setEditable(true);
/* 2013 */     this.autoSaveFreqTxt.setToolTipText("Please enter a positive integer.");
/*      */ 
/* 2015 */     this.autoSaveFreqTxt.setText(autoSaveInterval);
/* 2016 */     this.autoSaveFreqTxt.setEnabled(false);
/*      */   }
/*      */ 
/*      */   private void createShareTab(Composite parent)
/*      */   {
/* 2026 */     GridLayout gly = new GridLayout(1, false);
/* 2027 */     parent.setLayout(gly);
/*      */ 
/* 2029 */     Composite titleComp = new Composite(parent, 0);
/* 2030 */     GridLayout gl0 = new GridLayout(1, true);
/* 2031 */     titleComp.setLayout(gl0);
/*      */ 
/* 2033 */     Label filters = new Label(titleComp, 0);
/* 2034 */     filters.setText("Share This Product:");
/*      */ 
/* 2039 */     Composite whoComp = new Composite(parent, 0);
/*      */ 
/* 2041 */     GridLayout mainLayout = new GridLayout(2, false);
/*      */ 
/* 2043 */     mainLayout.marginHeight = 3;
/* 2044 */     mainLayout.marginWidth = 3;
/*      */ 
/* 2046 */     whoComp.setLayout(mainLayout);
/*      */ 
/* 2048 */     Label whoLbl = new Label(whoComp, 0);
/* 2049 */     whoLbl.setText("Who:");
/*      */ 
/* 2051 */     Combo whoCmb = new Combo(whoComp, 0);
/* 2052 */     whoCmb.add("AWC");
/* 2053 */     whoCmb.add("HPC");
/* 2054 */     whoCmb.add("CPC");
/* 2055 */     whoCmb.add("SPC");
/* 2056 */     whoCmb.add("Other");
/* 2057 */     whoCmb.select(0);
/*      */ 
/* 2062 */     Composite whatComp = new Composite(parent, 0);
/*      */ 
/* 2064 */     whatComp.setLayout(mainLayout);
/*      */ 
/* 2066 */     Label whatLbl = new Label(whatComp, 0);
/* 2067 */     whatLbl.setText("What:");
/*      */ 
/* 2069 */     Combo whatCmb = new Combo(whatComp, 0);
/* 2070 */     whatCmb.add("All");
/* 2071 */     whatCmb.add("Graphics Only");
/* 2072 */     whatCmb.add("Final Products Only");
/* 2073 */     whatCmb.add("Other");
/* 2074 */     whatCmb.select(0);
/*      */   }
/*      */ 
/*      */   private void createProductsTab(Composite parent)
/*      */   {
/* 2083 */     GridLayout gly = new GridLayout(1, false);
/* 2084 */     parent.setLayout(gly);
/*      */ 
/* 2086 */     Button newBtn = new Button(parent, 8);
/* 2087 */     newBtn.setText("New");
/*      */ 
/* 2089 */     newBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/* 2093 */         ProdTypeDialog ptDlg = new ProdTypeDialog(ProductConfigureDialog.this);
/* 2094 */         ptDlg.open();
/*      */       }
/*      */     });
/* 2098 */     this.pdComp = new Composite(parent, 0);
/* 2099 */     GridLayout gl0 = new GridLayout(3, true);
/* 2100 */     this.pdComp.setLayout(gl0);
/*      */ 
/* 2102 */     ProductType curPrdType = getCurrentPrdTyp();
/* 2103 */     if (this.ptList != null) this.ptList.clear();
/* 2104 */     if ((curPrdType != null) && (curPrdType.getProdType() != null))
/* 2105 */       for (ProdType pt : curPrdType.getProdType())
/* 2106 */         addProdType(pt);
/*      */   }
/*      */ 
/*      */   private void createClipTab(Composite parent)
/*      */   {
/* 2116 */     GridLayout gly = new GridLayout(1, false);
/* 2117 */     parent.setLayout(gly);
/*      */ 
/* 2119 */     Composite titleComp = new Composite(parent, 0);
/* 2120 */     GridLayout gl0 = new GridLayout(2, true);
/* 2121 */     gl0.marginHeight = 3;
/* 2122 */     gl0.marginWidth = 3;
/* 2123 */     titleComp.setLayout(gl0);
/*      */ 
/* 2125 */     Label flagLbl = new Label(titleComp, 0);
/* 2126 */     flagLbl.setText("Clip the Product:");
/*      */ 
/* 2128 */     this.clipChkBox = new Button(titleComp, 32);
/* 2129 */     this.clipChkBox.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/* 2133 */         if (((Button)e.widget).getSelection()) {
/* 2134 */           ProductConfigureDialog.this.boundsListCbo.setEnabled(true);
/* 2135 */           ProductConfigureDialog.this.boundsNameCbo.setEnabled(true);
/*      */         }
/*      */         else {
/* 2138 */           ProductConfigureDialog.this.boundsListCbo.setEnabled(false);
/* 2139 */           ProductConfigureDialog.this.boundsNameCbo.setEnabled(false);
/*      */         }
/*      */       }
/*      */ 
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */     });
/* 2152 */     Label boundsListLbl = new Label(titleComp, 0);
/* 2153 */     boundsListLbl.setText("Bounds Table: ");
/*      */ 
/* 2155 */     this.boundsListCbo = new Combo(titleComp, 12);
/*      */ 
/* 2157 */     int init = 0;
/* 2158 */     int ii = 0;
/* 2159 */     for (String str : PgenStaticDataProvider.getProvider().getBoundsTableList()) {
/* 2160 */       this.boundsListCbo.add(str);
/* 2161 */       if (str.equals("bwus_bnd")) init = ii;
/* 2162 */       ii++;
/*      */     }
/* 2164 */     this.boundsListCbo.select(init);
/*      */ 
/* 2166 */     this.boundsListCbo.addSelectionListener(new SelectionListener()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/* 2170 */         ProductConfigureDialog.this.populateBoundsNames(((Combo)e.widget).getText());
/*      */       }
/*      */ 
/*      */       public void widgetDefaultSelected(SelectionEvent e)
/*      */       {
/*      */       }
/*      */     });
/* 2181 */     Label boundsNameLbl = new Label(titleComp, 0);
/* 2182 */     boundsNameLbl.setText("Bounds Name: ");
/*      */ 
/* 2184 */     this.boundsNameCbo = new Combo(titleComp, 12);
/*      */ 
/* 2186 */     populateBoundsNames(this.boundsListCbo.getText());
/*      */ 
/* 2225 */     if (!this.clipChkBox.getSelection()) {
/* 2226 */       this.boundsListCbo.setEnabled(false);
/* 2227 */       this.boundsNameCbo.setEnabled(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private String selectFile(Shell sh, int mode, String[] nameFilter, String[] extensionFilter, String pathFilter, String defaultFile, boolean overWrite)
/*      */   {
/* 2248 */     FileDialog dialog = new FileDialog(sh, mode);
/* 2249 */     dialog.setFilterNames(nameFilter);
/* 2250 */     dialog.setFilterExtensions(extensionFilter);
/* 2251 */     dialog.setFilterPath(pathFilter);
/* 2252 */     if (defaultFile != null) {
/* 2253 */       dialog.setFileName(defaultFile);
/*      */     }
/* 2255 */     dialog.setOverwrite(overWrite);
/*      */ 
/* 2257 */     return dialog.open();
/*      */   }
/*      */ 
/*      */   private String getCurrentTabName()
/*      */   {
/* 2269 */     return this.tabFolder.getSelection()[0].getText();
/*      */   }
/*      */ 
/*      */   private void updatePalette(ProductType ptyp)
/*      */   {
/* 2282 */     if ((ptyp.getPgenControls() != null) && 
/* 2283 */       (ptyp.getPgenControls().getName() != null)) {
/* 2284 */       ptyp.getPgenControls().getName().clear();
/*      */     }
/*      */ 
/* 2287 */     if ((ptyp.getPgenActions() != null) && 
/* 2288 */       (ptyp.getPgenActions().getName() != null)) {
/* 2289 */       ptyp.getPgenActions().getName().clear();
/*      */     }
/*      */ 
/* 2293 */     PgenControls pControls = new PgenControls();
/*      */ 
/* 2295 */     for (Button btn : this.controlBtns)
/*      */     {
/* 2297 */       if (btn.getSelection()) {
/* 2298 */         pControls.getName().add(btn.getData().toString());
/*      */       }
/*      */     }
/*      */ 
/* 2302 */     ptyp.setPgenControls(pControls);
/*      */ 
/* 2305 */     PgenActions pActions = new PgenActions();
/*      */ 
/* 2307 */     for (Button btn : this.actionBtns)
/*      */     {
/* 2309 */       if (btn.getSelection()) {
/* 2310 */         pActions.getName().add(btn.getData().toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2315 */     ptyp.setPgenActions(pActions);
/*      */ 
/* 2318 */     int ii = 0;
/*      */ 
/* 2323 */     ArrayList pClassList = new ArrayList();
/* 2324 */     for (Button btn : this.classChkBtns)
/*      */     {
/* 2326 */       if (btn.getSelection())
/*      */       {
/* 2328 */         PgenClass pClass = new PgenClass();
/* 2329 */         pClass.setName(this.classPushBtns[ii].getData().toString());
/*      */ 
/* 2331 */         ArrayList selectedObjs = getSelectedObjects(this.classPushBtns[ii].getData().toString());
/*      */ 
/* 2333 */         if (selectedObjs != null)
/*      */         {
/* 2335 */           PgenObjects pObj = new PgenObjects();
/* 2336 */           for (String objStr : selectedObjs) {
/* 2337 */             pObj.getName().add(objStr);
/*      */           }
/*      */ 
/* 2340 */           pClass.setPgenObjects(pObj);
/*      */ 
/* 2342 */           pClassList.add(pClass);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2351 */       ii++;
/*      */     }
/*      */ 
/* 2355 */     if (ptyp.getPgenClass() != null) {
/* 2356 */       ptyp.getPgenClass().clear();
/*      */     }
/*      */ 
/* 2359 */     for (PgenClass pcls : pClassList)
/* 2360 */       ptyp.getPgenClass().add(pcls);
/*      */   }
/*      */ 
/*      */   private boolean updateLayers(ProductType ptyp)
/*      */   {
/* 2374 */     String curName = this.layerNameCombo.getText();
/* 2375 */     String inputName = this.layerNameTxt.getText();
/*      */ 
/* 2377 */     boolean validateLayerName = validateLayerName(curName, inputName);
/*      */ 
/* 2379 */     List pLayers = ptyp.getPgenLayer();
/*      */ 
/* 2381 */     if (validateLayerName)
/*      */     {
/* 2384 */       int layerAt = -1;
/* 2385 */       int kk = 0;
/* 2386 */       for (PgenLayer plyr : pLayers) {
/* 2387 */         if (plyr.getName().equals(inputName)) {
/* 2388 */           layerAt = kk;
/* 2389 */           break;
/*      */         }
/*      */ 
/* 2392 */         kk++;
/*      */       }
/*      */ 
/* 2396 */       PgenLayer pgenlyr = new PgenLayer();
/*      */ 
/* 2398 */       RGB layerRGB = this.layerColorSelector.getColorValue();
/* 2399 */       gov.noaa.nws.ncep.ui.pgen.producttypes.Color pclr = 
/* 2400 */         new gov.noaa.nws.ncep.ui.pgen.producttypes.Color();
/* 2401 */       pclr.setAlpha(Integer.valueOf(255));
/* 2402 */       pclr.setRed(layerRGB.red);
/* 2403 */       pclr.setGreen(layerRGB.green);
/* 2404 */       pclr.setBlue(layerRGB.blue);
/*      */ 
/* 2406 */       pgenlyr.setColor(pclr);
/*      */ 
/* 2408 */       pgenlyr.setFilled(Boolean.valueOf(this.layerFillBtn.getSelection()));
/* 2409 */       pgenlyr.setMonoColor(Boolean.valueOf(this.layerMonoBtn.getSelection()));
/* 2410 */       pgenlyr.setName(inputName);
/* 2411 */       pgenlyr.setOnOff(Boolean.valueOf(this.layerOnOffBtn.getSelection()));
/*      */ 
/* 2414 */       ProductType oldType = getCurrentPrdTyp();
/* 2415 */       if ((oldType == null) || ((ptyp.getType().equals(oldType.getType())) && 
/* 2416 */         (ptyp.getSubtype().equals(oldType.getSubtype())))) {
/* 2417 */         if (layerAt >= 0) {
/* 2418 */           ptyp.getPgenLayer().set(layerAt, pgenlyr);
/*      */         }
/*      */         else {
/* 2421 */           ptyp.getPgenLayer().add(pgenlyr);
/* 2422 */           this.layerNameCombo.add(inputName);
/* 2423 */           this.layerNameCombo.select(this.layerNameCombo.getItemCount() - 1);
/* 2424 */           this.layerNameCombo.pack();
/* 2425 */           this.layerNameCombo.getParent().pack();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2430 */         for (PgenLayer plyr : oldType.getPgenLayer())
/*      */         {
/* 2432 */           PgenLayer newpgenlyr = new PgenLayer();
/*      */ 
/* 2434 */           newpgenlyr.setColor(plyr.getColor());
/* 2435 */           newpgenlyr.setFilled(plyr.isFilled());
/* 2436 */           newpgenlyr.setMonoColor(plyr.isMonoColor());
/* 2437 */           newpgenlyr.setName(plyr.getName());
/* 2438 */           newpgenlyr.setOnOff(plyr.isOnOff());
/* 2439 */           newpgenlyr.setInputFile(plyr.getInputFile());
/* 2440 */           newpgenlyr.setOutputFile(plyr.getOutputFile());
/*      */ 
/* 2442 */           ptyp.getPgenLayer().add(newpgenlyr);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2448 */       createLayersTemplate(this.layerTempGrp, ptyp.getPgenLayer());
/*      */     }
/*      */ 
/* 2452 */     return validateLayerName;
/*      */   }
/*      */ 
/*      */   private boolean validateLayerName(String curLayerName, String inputName)
/*      */   {
/* 2465 */     boolean validNewLayerName = true;
/* 2466 */     String msg = new String("Layer name ");
/*      */ 
/* 2468 */     if ((inputName == null) || (inputName.trim().length() == 0)) {
/* 2469 */       validNewLayerName = false;
/* 2470 */       msg = "Layer name cannot be empty.\n";
/*      */     }
/* 2474 */     else if (inputName.equalsIgnoreCase("New")) {
/* 2475 */       validNewLayerName = false;
/* 2476 */       msg = "cannot be any variations of 'New'.\n";
/*      */     }
/* 2479 */     else if (!curLayerName.equalsIgnoreCase(inputName))
/*      */     {
/* 2481 */       for (String stype : this.layerNameCombo.getItems()) {
/* 2482 */         if (inputName.equalsIgnoreCase(stype)) {
/* 2483 */           msg = msg + "'" + inputName + "' has been used.\n";
/* 2484 */           validNewLayerName = false;
/* 2485 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2492 */     if (!validNewLayerName) {
/* 2493 */       MessageDialog confirmDlg = new MessageDialog(
/* 2494 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 2495 */         "Confirm a new Layer Name", null, msg + "Please specify another valid name.", 
/* 2496 */         4, new String[] { "OK" }, 0);
/*      */ 
/* 2498 */       confirmDlg.open();
/*      */     }
/*      */ 
/* 2501 */     return validNewLayerName;
/*      */   }
/*      */ 
/*      */   private boolean validatePrdTypName(String typeName, String typeInputName, String subtypeName, String subtypeInputName, String aliasName)
/*      */   {
/* 2520 */     boolean isValid = true;
/* 2521 */     StringBuilder msg = new StringBuilder();
/*      */ 
/* 2532 */     if ((typeInputName == null) || (typeInputName.trim().length() == 0)) {
/* 2533 */       isValid = false;
/* 2534 */       msg.append("Activity type name cannot be blank.\n");
/*      */     }
/* 2536 */     else if (typeInputName.equalsIgnoreCase("New")) {
/* 2537 */       isValid = false;
/* 2538 */       msg.append("Activity type name cannot be any variations of 'New'.\n");
/*      */     }
/* 2540 */     else if ((subtypeInputName != null) && (subtypeInputName.trim().length() > 0) && 
/* 2541 */       (subtypeInputName.equalsIgnoreCase("New"))) {
/* 2542 */       isValid = false;
/* 2543 */       msg.append("Activity subtype name cannot be any variations of 'New'.\n");
/*      */     }
/* 2545 */     else if ((aliasName != null) && (aliasName.trim().length() > 0))
/*      */     {
/* 2547 */       ProductType ityp = findProductType(typeInputName, subtypeInputName);
/* 2548 */       for (ProductType ptp : this.prdTyps.getProductType()) {
/* 2549 */         if ((ityp == null) && (ptp.getName().equalsIgnoreCase(aliasName)))
/*      */         {
/* 2551 */           msg.append("Alias name '" + aliasName + "' has been used.\n");
/* 2552 */           isValid = false;
/* 2553 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2564 */     ProductType ptype = null;
/* 2565 */     if ((isValid) && (
/* 2566 */       (typeName.equalsIgnoreCase("New")) || 
/* 2567 */       (subtypeName.equalsIgnoreCase("New"))))
/*      */     {
/* 2569 */       ptype = findProductType(typeInputName, subtypeInputName);
/* 2570 */       if (ptype != null) {
/* 2571 */         isValid = false;
/* 2572 */         String aName = new String(typeInputName);
/* 2573 */         if ((subtypeInputName != null) && (subtypeInputName.trim().length() > 0)) {
/* 2574 */           aName = new String(aName + "(" + subtypeInputName + ")");
/*      */         }
/*      */ 
/* 2577 */         msg.append("Activity name '" + aName + "' has been used.\n");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2592 */     if (isValid) {
/* 2593 */       ptype = findProductType(typeInputName, subtypeInputName);
/* 2594 */       if (ptype != null) {
/* 2595 */         ProductType otype = findProductType(typeName, subtypeName);
/* 2596 */         if (!ptype.equals(otype)) {
/* 2597 */           isValid = false;
/* 2598 */           String aName = new String(typeInputName);
/* 2599 */           if ((subtypeInputName != null) && (subtypeInputName.trim().length() > 0)) {
/* 2600 */             aName = new String(aName + "(" + subtypeInputName + ")");
/*      */           }
/* 2602 */           msg.append("Activity name '" + aName + "' has been used.\n");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2607 */     if (!isValid) {
/* 2608 */       MessageDialog confirmDlg = new MessageDialog(
/* 2609 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 2610 */         "Confirm a new Type Name", null, msg + "Please specify another valid name.", 
/* 2611 */         4, new String[] { "OK" }, 0);
/*      */ 
/* 2613 */       confirmDlg.open();
/*      */     }
/*      */ 
/* 2616 */     return isValid;
/*      */   }
/*      */ 
/*      */   private void editLayeringTab()
/*      */   {
/* 2626 */     ProductType curPrdType = getCurrentPrdTyp();
/*      */ 
/* 2628 */     this.layerNameCombo.removeAll();
/*      */ 
/* 2630 */     this.layerNameCombo.add("New");
/*      */ 
/* 2632 */     if (curPrdType != null) {
/* 2633 */       for (PgenLayer lyr : curPrdType.getPgenLayer()) {
/* 2634 */         this.layerNameCombo.add(lyr.getName());
/*      */       }
/*      */     }
/*      */ 
/* 2638 */     this.layerNameCombo.pack();
/* 2639 */     this.layerNameCombo.getParent().pack();
/*      */ 
/* 2641 */     this.layerNameCombo.select(0);
/* 2642 */     refreshLayerInput();
/*      */ 
/* 2648 */     if (curPrdType != null) {
/* 2649 */       createLayersTemplate(this.layerTempGrp, curPrdType.getPgenLayer());
/*      */     }
/*      */     else {
/* 2652 */       Control[] wids = this.layerTempGrp.getChildren();
/* 2653 */       for (int jj = 0; jj < wids.length; jj++) {
/* 2654 */         wids[jj].dispose();
/*      */       }
/*      */ 
/* 2657 */       this.layerTempGrp.pack();
/*      */     }
/*      */   }
/*      */ 
/*      */   private ProductType getCurrentPrdTyp()
/*      */   {
/* 2668 */     String curPrdtypeName = this.typeCombo.getText();
/* 2669 */     String curPrdSubtypeName = this.subtypeCombo.getText();
/*      */ 
/* 2671 */     ProductType curPrdType = findProductType(curPrdtypeName, curPrdSubtypeName);
/*      */ 
/* 2673 */     return curPrdType;
/*      */   }
/*      */ 
/*      */   private void refreshLayerInput()
/*      */   {
/* 2685 */     ProductType ptyp = getCurrentPrdTyp();
/* 2686 */     String layerName = this.layerNameCombo.getText();
/*      */ 
/* 2688 */     if (layerName.equalsIgnoreCase("New")) {
/* 2689 */       setDefaultLayerInput();
/*      */     }
/*      */     else
/*      */     {
/* 2693 */       this.layerNameTxt.setText(layerName);
/*      */ 
/* 2695 */       if (ptyp != null) {
/* 2696 */         List pLayers = ptyp.getPgenLayer();
/* 2697 */         int layerAt = -1;
/*      */         PgenLayer plyr;
/* 2698 */         if (pLayers != null)
/*      */         {
/* 2700 */           int kk = 0;
/* 2701 */           for (Iterator localIterator = pLayers.iterator(); localIterator.hasNext(); ) { plyr = (PgenLayer)localIterator.next();
/* 2702 */             if (plyr.getName().equals(layerName)) {
/* 2703 */               layerAt = kk;
/* 2704 */               break;
/*      */             }
/*      */ 
/* 2707 */             kk++;
/*      */           }
/*      */         }
/*      */ 
/* 2711 */         if (layerAt >= 0)
/*      */         {
/* 2713 */           PgenLayer pgenlyr = (PgenLayer)pLayers.get(layerAt);
/*      */ 
/* 2715 */           pgenlyr.getColor().getRed();
/* 2716 */           this.layerColorSelector.setColorValue(
/* 2717 */             new RGB(pgenlyr.getColor().getRed(), 
/* 2718 */             pgenlyr.getColor().getGreen(), 
/* 2719 */             pgenlyr.getColor().getBlue()));
/*      */ 
/* 2721 */           this.layerFillBtn.setSelection(pgenlyr.isFilled().booleanValue());
/* 2722 */           this.layerMonoBtn.setSelection(pgenlyr.isMonoColor().booleanValue());
/* 2723 */           this.layerOnOffBtn.setSelection(pgenlyr.isOnOff().booleanValue());
/*      */         }
/*      */ 
/* 2741 */         for (Button btn : this.layerNameBtns)
/* 2742 */           if (btn.getText().replace("&&", "&").equals(layerName)) {
/* 2743 */             setButtonColor(btn, this.activeButtonColor);
/*      */           }
/*      */           else
/* 2746 */             setButtonColor(btn, this.defaultButtonColor);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setDefaultLayerInput()
/*      */   {
/* 2762 */     this.layerNameTxt.setText("");
/*      */ 
/* 2764 */     this.layerOnOffBtn.setSelection(false);
/* 2765 */     this.layerMonoBtn.setSelection(false);
/*      */ 
/* 2767 */     java.awt.Color layerClr = java.awt.Color.YELLOW;
/* 2768 */     this.layerColorSelector.setColorValue(
/* 2769 */       new RGB(layerClr.getRed(), layerClr.getGreen(), layerClr.getBlue()));
/*      */ 
/* 2771 */     this.layerFillBtn.setSelection(false);
/*      */   }
/*      */ 
/*      */   private void removeOneLayer()
/*      */   {
/* 2787 */     ProductType ptyp = getCurrentPrdTyp();
/* 2788 */     String layerName = this.layerNameCombo.getText();
/*      */ 
/* 2790 */     if ((!layerName.equalsIgnoreCase("New")) && (ptyp != null))
/*      */     {
/* 2792 */       List pLayers = ptyp.getPgenLayer();
/*      */ 
/* 2794 */       if (pLayers != null)
/*      */       {
/* 2796 */         for (PgenLayer plyr : pLayers) {
/* 2797 */           if (plyr.getName().equals(layerName)) {
/* 2798 */             this.layerNameCombo.remove(plyr.getName());
/* 2799 */             pLayers.remove(plyr);
/* 2800 */             break;
/*      */           }
/*      */         }
/*      */ 
/* 2804 */         this.layerNameCombo.select(pLayers.size());
/* 2805 */         this.layerNameTxt.setText(this.layerNameCombo.getText());
/* 2806 */         this.layerNameCombo.pack();
/* 2807 */         this.layerNameCombo.getParent().pack();
/* 2808 */         createLayersTemplate(this.layerTempGrp, pLayers);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void switchProductType()
/*      */   {
/* 2822 */     if (this.typeCombo.getSelectionIndex() == 0) {
/* 2823 */       this.typeText.setText("");
/*      */     }
/*      */ 
/* 2827 */     this.subtypeCombo.removeAll();
/*      */ 
/* 2829 */     this.subtypeCombo.add("New");
/* 2830 */     ArrayList stypes = findSubtypes(this.typeCombo.getText());
/* 2831 */     for (String stp : stypes) {
/* 2832 */       this.subtypeCombo.add(stp);
/*      */     }
/*      */ 
/* 2835 */     this.subtypeCombo.pack();
/* 2836 */     if (stypes.size() > 0) {
/* 2837 */       this.subtypeCombo.select(1);
/* 2838 */       this.subtypeText.setText(this.subtypeCombo.getText());
/*      */     }
/*      */     else {
/* 2841 */       this.subtypeCombo.select(0);
/* 2842 */       this.subtypeText.setText("");
/*      */     }
/*      */ 
/* 2845 */     ProductType ctype = getCurrentPrdTyp();
/* 2846 */     if ((ctype != null) && (ctype.getName() != null)) {
/* 2847 */       this.aliasText.setText(ctype.getName());
/*      */     }
/*      */     else {
/* 2850 */       this.aliasText.setText("");
/*      */     }
/*      */ 
/* 2854 */     createObjects("None", true);
/* 2855 */     refreshPaletteSelections();
/*      */ 
/* 2858 */     editLayeringTab();
/*      */ 
/* 2861 */     editSaveTab();
/*      */ 
/* 2864 */     editSettingsTab();
/*      */ 
/* 2867 */     editClipTab();
/*      */ 
/* 2870 */     editProductsTab();
/*      */ 
/* 2872 */     this.activityGrp.pack();
/*      */   }
/*      */ 
/*      */   private void switchLayer(String layername)
/*      */   {
/* 2882 */     this.layerNameCombo.setText(layername);
/* 2883 */     refreshLayerInput();
/*      */   }
/*      */ 
/*      */   private void createFileText(Text txt, String initialFile)
/*      */   {
/* 2894 */     String[] filterNames = { "*.xml", "All Files (*)" };
/* 2895 */     String[] filterExtensions = { "*.xml", "*" };
/*      */ 
/* 2897 */     String filterPath = PgenUtil.getWorkingDirectory();
/* 2898 */     String defaultFile = new String("default.xml");
/*      */ 
/* 2900 */     if (initialFile != null) {
/* 2901 */       int index = initialFile.lastIndexOf('/');
/* 2902 */       if (index >= 0) {
/* 2903 */         defaultFile = initialFile.substring(index + 1, initialFile.length());
/* 2904 */         filterPath = initialFile.substring(0, index);
/*      */       }
/*      */       else {
/* 2907 */         defaultFile = new String(initialFile);
/*      */       }
/*      */     }
/*      */ 
/* 2911 */     String selectedFile = selectFile(this.shell, 8192, filterNames, 
/* 2912 */       filterExtensions, filterPath, defaultFile, true);
/*      */ 
/* 2914 */     if (selectedFile != null)
/* 2915 */       txt.setText(selectedFile);
/*      */   }
/*      */ 
/*      */   private String createDefaultFileName(String prdname, String layername, String suffix)
/*      */   {
/* 2933 */     String fileName = null;
/*      */ 
/* 2935 */     if ((prdname != null) || (layername != null)) {
/* 2936 */       if ((prdname != null) && (prdname.trim().length() > 0)) {
/* 2937 */         fileName = new String(prdname);
/*      */       }
/*      */ 
/* 2940 */       if ((layername != null) && (layername.trim().length() > 0)) {
/* 2941 */         if (fileName != null) {
/* 2942 */           fileName = new String(fileName + "_" + layername);
/*      */         }
/*      */         else {
/* 2945 */           fileName = new String(layername);
/*      */         }
/*      */       }
/*      */ 
/* 2949 */       if (fileName != null)
/*      */       {
/* 2951 */         if (suffix != null) {
/* 2952 */           fileName = new String(fileName + "_" + suffix + ".xml");
/*      */         }
/*      */         else {
/* 2955 */           fileName = new String(fileName + ".xml");
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2961 */     return fileName;
/*      */   }
/*      */ 
/*      */   private void editSaveTab()
/*      */   {
/* 2970 */     ProductType curPrdType = getCurrentPrdTyp();
/*      */ 
/* 2972 */     String tmpFile = createDefaultOutputFile();
/*      */ 
/* 2974 */     if ((curPrdType != null) && (curPrdType.getPgenSave() != null))
/*      */     {
/* 2976 */       PgenSave pSave = curPrdType.getPgenSave();
/* 2977 */       if (pSave != null) {
/* 2978 */         String outFile = pSave.getOutputFile();
/* 2979 */         if ((outFile != null) && (outFile.length() > 7) && 
/* 2980 */           (!outFile.substring(0, 7).equalsIgnoreCase("Default"))) {
/* 2981 */           this.saveOutputTxt.setText(outFile);
/*      */         }
/*      */         else {
/* 2984 */           this.saveOutputTxt.setText(tmpFile);
/*      */         }
/*      */ 
/* 2987 */         this.saveIndividualLayerBtn.setSelection(pSave.isSaveLayers().booleanValue());
/* 2988 */         this.autoSaveBtn.setSelection(true);
/* 2989 */         this.autoSaveFreqTxt.setText("");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2994 */       this.saveOutputTxt.setText(tmpFile);
/*      */ 
/* 2996 */       this.saveIndividualLayerBtn.setSelection(false);
/*      */ 
/* 2998 */       this.autoSaveBtn.setSelection(true);
/* 2999 */       this.autoSaveFreqTxt.setText("");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void editSettingsTab()
/*      */   {
/* 3009 */     ProductType curPrdType = getCurrentPrdTyp();
/* 3010 */     this.currentSettingsTxt.setText("Localization\\NCP\\PGEN\\" + getCurrentSetting(this.typeText.getText()));
/*      */ 
/* 3012 */     if ((curPrdType != null) && (curPrdType.getPgenSettingsFile() != null))
/*      */     {
/* 3014 */       String settingsFile = curPrdType.getPgenSettingsFile();
/* 3015 */       if (settingsFile != null)
/* 3016 */         this.settingsTxt.setText(settingsFile);
/*      */     }
/*      */     else
/*      */     {
/* 3020 */       this.settingsTxt.setText("");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void editClipTab()
/*      */   {
/* 3030 */     ProductType curPrdType = getCurrentPrdTyp();
/*      */ 
/* 3032 */     if ((curPrdType != null) && (curPrdType.getClipFlag() != null) && (curPrdType.getClipFlag().booleanValue()))
/*      */     {
/* 3034 */       this.clipChkBox.setSelection(true);
/* 3035 */       this.boundsListCbo.setEnabled(true);
/* 3036 */       this.boundsNameCbo.setEnabled(true);
/*      */ 
/* 3038 */       if (curPrdType.getClipBoundsTable() != null) {
/* 3039 */         this.boundsListCbo.setText(curPrdType.getClipBoundsTable());
/* 3040 */         if (curPrdType.getClipBoundsName() != null) {
/* 3041 */           populateBoundsNames(this.boundsListCbo.getText());
/* 3042 */           this.boundsNameCbo.setText(curPrdType.getClipBoundsName());
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 3049 */       this.clipChkBox.setSelection(false);
/* 3050 */       this.boundsListCbo.setEnabled(false);
/* 3051 */       this.boundsNameCbo.setEnabled(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void editProductsTab()
/*      */   {
/* 3061 */     this.pdComp.dispose();
/* 3062 */     this.pdComp = new Composite(this.tabComposites[7], 0);
/* 3063 */     GridLayout gl0 = new GridLayout(3, true);
/* 3064 */     this.pdComp.setLayout(gl0);
/*      */ 
/* 3066 */     ProductType curPrdType = getCurrentPrdTyp();
/* 3067 */     if (this.ptList != null) this.ptList.clear();
/* 3068 */     if ((curPrdType != null) && (curPrdType.getProdType() != null)) {
/* 3069 */       for (ProdType pt : curPrdType.getProdType()) {
/* 3070 */         addProdType(pt);
/*      */       }
/*      */     }
/*      */ 
/* 3074 */     this.pdComp.pack();
/* 3075 */     this.pdComp.layout();
/* 3076 */     this.tabComposites[7].pack();
/* 3077 */     this.tabComposites[7].layout();
/*      */   }
/*      */ 
/*      */   private void updateSaveInfo(ProductType ptyp)
/*      */   {
/* 3088 */     PgenSave pSave = ptyp.getPgenSave();
/* 3089 */     if (pSave == null) {
/* 3090 */       pSave = new PgenSave();
/*      */     }
/*      */ 
/* 3094 */     pSave.setInputFile(null);
/*      */ 
/* 3096 */     String outfile = this.saveOutputTxt.getText();
/* 3097 */     if (outfile != null) {
/* 3098 */       pSave.setOutputFile(outfile);
/*      */     }
/*      */ 
/* 3101 */     pSave.setSaveLayers(Boolean.valueOf(this.saveIndividualLayerBtn.getSelection()));
/* 3102 */     pSave.setAutoSave(Boolean.valueOf(this.autoSaveBtn.getSelection()));
/*      */ 
/* 3104 */     int freqInt = 0;
/* 3105 */     if (pSave.getAutoSaveFreq() != null) {
/* 3106 */       freqInt = pSave.getAutoSaveFreq().intValue();
/*      */     }
/*      */     try
/*      */     {
/* 3110 */       freqInt = Math.abs(Integer.parseInt(this.autoSaveFreqTxt.getText()));
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/*      */     }
/* 3115 */     if (freqInt == 0) {
/* 3116 */       freqInt = autoSaveInterval;
/*      */     }
/*      */ 
/* 3119 */     pSave.setAutoSaveFreq(Integer.valueOf(freqInt));
/* 3120 */     this.autoSaveFreqTxt.setText(freqInt);
/*      */ 
/* 3122 */     ptyp.setPgenSave(pSave);
/*      */   }
/*      */ 
/*      */   private void updateClip(ProductType ptyp)
/*      */   {
/* 3133 */     String pdName = this.typeText.getText();
/* 3134 */     if ((pdName == null) || (pdName.isEmpty())) {
/* 3135 */       return;
/*      */     }
/*      */ 
/* 3138 */     ptyp.setClipFlag(Boolean.valueOf(this.clipChkBox.getSelection()));
/* 3139 */     if (this.clipChkBox.getSelection()) {
/* 3140 */       ptyp.setClipBoundsTable(this.boundsListCbo.getText());
/* 3141 */       ptyp.setClipBoundsName(this.boundsNameCbo.getText());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateSettings(ProductType ptyp)
/*      */   {
/* 3153 */     String pdName = this.typeText.getText();
/* 3154 */     if ((pdName == null) || (pdName.isEmpty())) {
/* 3155 */       return;
/*      */     }
/*      */ 
/* 3159 */     String settingsFile = this.settingsTxt.getText();
/* 3160 */     if ((settingsFile != null) && (!settingsFile.isEmpty())) {
/* 3161 */       File sFile = new File(settingsFile);
/*      */ 
/* 3163 */       if ((!sFile.canRead()) || (FileTools.validate(settingsFile, null))) {
/* 3164 */         MessageDialog infoDlg = new MessageDialog(
/* 3165 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 3166 */           "Warning", null, "Pgen settings file " + settingsFile + " does not exist or is invalid!", 
/* 3167 */           4, new String[] { "OK" }, 0);
/*      */ 
/* 3169 */         infoDlg.open();
/*      */       }
/*      */       else {
/* 3172 */         ptyp.setPgenSettingsFile("");
/*      */ 
/* 3175 */         LocalizationContext userContext = PgenStaticDataProvider.getProvider().getLocalizationContext(
/* 3176 */           LocalizationContext.LocalizationType.CAVE_STATIC, LocalizationContext.LocalizationLevel.USER);
/*      */ 
/* 3178 */         LocalizationFile lFile = PgenStaticDataProvider.getProvider().getLocalizationFile(
/* 3179 */           userContext, getSettingFullPath(pdName));
/*      */         try
/*      */         {
/* 3184 */           InputStream is = new FileInputStream(sFile);
/*      */ 
/* 3187 */           long length = sFile.length();
/*      */ 
/* 3190 */           if (length > 2147483647L) {
/* 3191 */             return;
/*      */           }
/*      */ 
/* 3195 */           byte[] bytes = new byte[(int)length];
/*      */ 
/* 3198 */           int offset = 0;
/* 3199 */           int numRead = 0;
/* 3200 */           while ((offset < bytes.length) && (
/* 3201 */             (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
/* 3202 */             offset += numRead;
/*      */           }
/*      */ 
/* 3205 */           lFile.write(bytes);
/*      */         }
/*      */         catch (FileNotFoundException e1)
/*      */         {
/* 3209 */           e1.printStackTrace();
/*      */         }
/*      */         catch (LocalizationException e) {
/* 3212 */           e.printStackTrace();
/*      */         }
/*      */         catch (IOException e) {
/* 3215 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addProdType(final ProdType pt)
/*      */   {
/* 3227 */     if (this.ptList == null) this.ptList = new ArrayList();
/* 3228 */     this.ptList.add(pt);
/*      */ 
/* 3231 */     final Button ptBtn = new Button(this.pdComp, 8);
/* 3232 */     ptBtn.setLayoutData(new GridData(120, 35));
/* 3233 */     ptBtn.setText(pt.getName());
/*      */ 
/* 3236 */     final Label typeLbl = new Label(this.pdComp, 0);
/* 3237 */     GridData gd = new GridData(140, 20);
/* 3238 */     gd.verticalAlignment = 16777216;
/* 3239 */     gd.verticalIndent = 10;
/* 3240 */     typeLbl.setLayoutData(gd);
/* 3241 */     typeLbl.setText(pt.getType());
/*      */ 
/* 3243 */     ptBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/* 3247 */         ProdTypeDialog ptDlg = new ProdTypeDialog(ProductConfigureDialog.this, pt, ptBtn, typeLbl);
/* 3248 */         ptDlg.open();
/*      */       }
/*      */     });
/* 3254 */     final Button delBtn = new Button(this.pdComp, 8);
/* 3255 */     delBtn.setText("Delete");
/*      */ 
/* 3257 */     delBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/* 3261 */         Iterator it = ProductConfigureDialog.this.ptList.iterator();
/* 3262 */         while (it.hasNext()) {
/* 3263 */           ProdType pt = (ProdType)it.next();
/* 3264 */           if (pt.getName().equalsIgnoreCase(ptBtn.getText()))
/*      */           {
/* 3283 */             it.remove();
/* 3284 */             break;
/*      */           }
/*      */         }
/* 3287 */         ptBtn.dispose();
/* 3288 */         typeLbl.dispose();
/* 3289 */         delBtn.dispose();
/* 3290 */         ProductConfigureDialog.this.pdComp.pack();
/* 3291 */         ProductConfigureDialog.this.pdComp.layout();
/*      */       }
/*      */     });
/* 3295 */     this.pdComp.pack();
/* 3296 */     this.pdComp.layout();
/* 3297 */     this.tabComposites[7].pack();
/* 3298 */     this.tabComposites[7].layout();
/*      */   }
/*      */ 
/*      */   private ProductType findProductType(String type, String subtype)
/*      */   {
/* 3307 */     ProductType atyp = null;
/* 3308 */     String ss = null;
/* 3309 */     if ((subtype == null) || (subtype.trim().length() == 0)) {
/* 3310 */       ss = new String(DEFAULT_SUBTYPE);
/*      */     }
/*      */     else {
/* 3313 */       ss = new String(subtype);
/*      */     }
/*      */ 
/* 3316 */     if ((type != null) && (type.trim().length() > 0)) {
/* 3317 */       for (ProductType stype : this.prdTyps.getProductType()) {
/* 3318 */         if ((stype.getType().equals(type)) && 
/* 3319 */           (stype.getSubtype().equals(ss))) {
/* 3320 */           atyp = stype;
/* 3321 */           break;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3327 */     return atyp;
/*      */   }
/*      */ 
/*      */   private ArrayList<String> findTypes()
/*      */   {
/* 3338 */     ArrayList types = new ArrayList();
/* 3339 */     for (ProductType type : this.prdTyps.getProductType()) {
/* 3340 */       if (!types.contains(type.getType())) {
/* 3341 */         types.add(type.getType());
/*      */       }
/*      */     }
/*      */ 
/* 3345 */     return types;
/*      */   }
/*      */ 
/*      */   private ArrayList<String> findSubtypes(String type)
/*      */   {
/* 3356 */     ArrayList subtypes = new ArrayList();
/* 3357 */     if ((type != null) && (type.trim().length() > 0)) {
/* 3358 */       for (ProductType stype : this.prdTyps.getProductType()) {
/* 3359 */         if (stype.getType().equals(type)) {
/* 3360 */           String stp = stype.getSubtype();
/* 3361 */           if ((stp != null) && (stp.trim().length() > 0)) {
/* 3362 */             subtypes.add(stype.getSubtype());
/*      */           }
/*      */           else {
/* 3365 */             subtypes.add(DEFAULT_SUBTYPE);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3371 */     return subtypes;
/*      */   }
/*      */ 
/*      */   private String findAlias(ProductType ptype)
/*      */   {
/* 3379 */     String alias = null;
/* 3380 */     if (ptype != null) {
/* 3381 */       alias = ptype.getName();
/* 3382 */       if ((alias == null) || (alias.trim().length() == 0)) {
/* 3383 */         alias = new String(ptype.getType());
/* 3384 */         String stp = ptype.getSubtype();
/* 3385 */         if ((stp != null) && (stp.trim().length() > 0)) {
/* 3386 */           alias = new String(alias + "(" + stp + ")");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3391 */     return alias;
/*      */   }
/*      */ 
/*      */   private void switchProductSubtype()
/*      */   {
/* 3403 */     ProductType ctype = getCurrentPrdTyp();
/*      */ 
/* 3405 */     this.subtypeText.setText("");
/* 3406 */     if ((ctype != null) && (ctype.getSubtype() != null)) {
/* 3407 */       this.subtypeText.setText(ctype.getSubtype());
/*      */     }
/*      */     else {
/* 3410 */       this.subtypeText.setText("");
/*      */     }
/*      */ 
/* 3413 */     if ((ctype != null) && (ctype.getName() != null)) {
/* 3414 */       this.aliasText.setText(ctype.getName());
/*      */     }
/*      */     else {
/* 3417 */       this.aliasText.setText("");
/*      */     }
/*      */ 
/* 3421 */     createObjects("None", true);
/* 3422 */     refreshPaletteSelections();
/*      */ 
/* 3425 */     editLayeringTab();
/*      */ 
/* 3428 */     editSaveTab();
/*      */ 
/* 3431 */     editSettingsTab();
/*      */ 
/* 3434 */     editClipTab();
/*      */ 
/* 3437 */     editProductsTab();
/*      */   }
/*      */ 
/*      */   private String createDefaultOutputFile()
/*      */   {
/* 3450 */     StringBuilder fname = new StringBuilder();
/*      */ 
/* 3452 */     ProductType ptype = getCurrentPrdTyp();
/* 3453 */     if (ptype == null) {
/* 3454 */       fname.append("Default.");
/*      */     }
/*      */     else
/*      */     {
/* 3458 */       fname.append(ptype.getType().replace(' ', '_') + ".");
/* 3459 */       if ((ptype.getSubtype() != null) && (ptype.getSubtype().trim().length() > 0) && 
/* 3460 */         (!ptype.getSubtype().equalsIgnoreCase(DEFAULT_SUBTYPE))) {
/* 3461 */         fname.append(ptype.getSubtype().replace(' ', '_') + ".");
/*      */       }
/*      */     }
/*      */ 
/* 3465 */     fname.append("DDMMYYYY.HH.xml");
/*      */ 
/* 3467 */     return fname.toString();
/*      */   }
/*      */ 
/*      */   public static String getSettingFullPath(String prodType)
/*      */   {
/* 3477 */     return PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + getSettingFileName(prodType);
/*      */   }
/*      */ 
/*      */   public static String getSettingFileName(String prodType)
/*      */   {
/* 3486 */     String ret = "settings_tbl.xml";
/* 3487 */     if ((prodType == null) || (prodType.isEmpty())) return ret;
/*      */ 
/* 3489 */     String pt = prodType.replaceAll(" ", "_");
/*      */ 
/* 3491 */     return "settings_tbl_" + pt + ".xml";
/*      */   }
/*      */ 
/*      */   private String getCurrentSetting(String prodType)
/*      */   {
/* 3500 */     File settingsFile = PgenStaticDataProvider.getProvider().getStaticFile(
/* 3501 */       getSettingFullPath(prodType));
/*      */ 
/* 3503 */     String fileName = "";
/* 3504 */     if ((settingsFile == null) || (!settingsFile.exists())) {
/* 3505 */       fileName = "settings_tbl.xml";
/* 3506 */       settingsFile = PgenStaticDataProvider.getProvider().getStaticFile(
/* 3507 */         PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + AttrSettings.settingsFileName);
/*      */     }
/*      */     else {
/* 3510 */       fileName = getSettingFileName(prodType);
/*      */     }
/*      */ 
/* 3513 */     String path = settingsFile.getAbsolutePath();
/* 3514 */     if (path.contains("user")) {
/* 3515 */       fileName = fileName + "(USER)";
/*      */     }
/* 3517 */     else if (path.contains("desk")) {
/* 3518 */       fileName = fileName + "(DESK)";
/*      */     }
/* 3520 */     else if (path.contains("site")) {
/* 3521 */       fileName = fileName + "(SITE)";
/*      */     }
/*      */     else {
/* 3524 */       fileName = fileName + "(Base)";
/*      */     }
/*      */ 
/* 3527 */     return fileName;
/*      */   }
/*      */ 
/*      */   private ProductType copyProductType(ProductType typeIn)
/*      */   {
/* 3537 */     if (typeIn == null) {
/* 3538 */       return null;
/*      */     }
/*      */ 
/* 3541 */     ProductType outType = new ProductType();
/*      */ 
/* 3543 */     outType.setName(nvl(typeIn.getName()));
/*      */ 
/* 3545 */     outType.setType(nvl(typeIn.getType()));
/*      */ 
/* 3547 */     outType.setSubtype(nvl(typeIn.getSubtype()));
/*      */ 
/* 3549 */     outType.setPgenSettingsFile(nvl(typeIn.getPgenSettingsFile()));
/*      */ 
/* 3551 */     outType.setClipFlag(typeIn.getClipFlag());
/*      */ 
/* 3554 */     PgenActions pact = new PgenActions();
/* 3555 */     for (String name : typeIn.getPgenActions().getName()) {
/* 3556 */       pact.getName().add(new String(name));
/*      */     }
/* 3558 */     outType.setPgenActions(pact);
/*      */ 
/* 3561 */     PgenControls pcntl = new PgenControls();
/* 3562 */     for (String name : typeIn.getPgenControls().getName()) {
/* 3563 */       pcntl.getName().add(new String(name));
/*      */     }
/* 3565 */     outType.setPgenControls(pcntl);
/*      */ 
/* 3568 */     for (PgenClass pcs : typeIn.getPgenClass()) {
/* 3569 */       PgenClass pcls = new PgenClass();
/* 3570 */       pcls.setName(new String(pcs.getName()));
/*      */ 
/* 3572 */       PgenObjects pobj = new PgenObjects();
/*      */ 
/* 3574 */       for (String obj : pcs.getPgenObjects().getName()) {
/* 3575 */         pobj.getName().add(new String(obj));
/*      */       }
/*      */ 
/* 3578 */       pcls.setPgenObjects(pobj);
/*      */ 
/* 3580 */       outType.getPgenClass().add(pcls);
/*      */     }
/*      */     PgenLayer lyr;
/* 3584 */     for (PgenLayer plyr : typeIn.getPgenLayer()) {
/* 3585 */       lyr = new PgenLayer();
/* 3586 */       gov.noaa.nws.ncep.ui.pgen.producttypes.Color clr = 
/* 3587 */         new gov.noaa.nws.ncep.ui.pgen.producttypes.Color();
/* 3588 */       clr.setAlpha(plyr.getColor().getAlpha());
/* 3589 */       clr.setBlue(plyr.getColor().getBlue());
/* 3590 */       clr.setGreen(plyr.getColor().getGreen());
/* 3591 */       clr.setRed(plyr.getColor().getRed());
/*      */ 
/* 3593 */       lyr.setColor(clr);
/*      */ 
/* 3595 */       lyr.setFilled(plyr.isFilled());
/*      */ 
/* 3597 */       lyr.setInputFile(nvl(plyr.getInputFile()));
/*      */ 
/* 3599 */       lyr.setMonoColor(plyr.isMonoColor());
/*      */ 
/* 3601 */       lyr.setName(nvl(plyr.getName()));
/* 3602 */       lyr.setOnOff(plyr.isOnOff());
/*      */ 
/* 3604 */       lyr.setOutputFile(nvl(plyr.getOutputFile()));
/*      */ 
/* 3606 */       outType.getPgenLayer().add(lyr);
/*      */     }
/*      */ 
/* 3610 */     PgenSave psave = new PgenSave();
/* 3611 */     psave.setAutoSave(typeIn.getPgenSave().isAutoSave());
/* 3612 */     psave.setAutoSaveFreq(typeIn.getPgenSave().getAutoSaveFreq());
/* 3613 */     psave.setInputFile(nvl(typeIn.getPgenSave().getInputFile()));
/* 3614 */     psave.setOutputFile(nvl(typeIn.getPgenSave().getOutputFile()));
/* 3615 */     psave.setSaveLayers(typeIn.getPgenSave().isAutoSave());
/*      */ 
/* 3617 */     outType.setPgenSave(psave);
/*      */ 
/* 3620 */     for (ProdType ptype : typeIn.getProdType()) {
/* 3621 */       ProdType ptp = new ProdType();
/* 3622 */       ptp.setName(new String(ptype.getName()));
/* 3623 */       ptp.setOutputFile(nvl(ptype.getOutputFile()));
/* 3624 */       ptp.setStyleSheetFile(nvl(ptp.getStyleSheetFile()));
/* 3625 */       ptp.setType(nvl(ptype.getType()));
/*      */ 
/* 3627 */       outType.getProdType().add(ptp);
/*      */     }
/*      */ 
/* 3630 */     return outType;
/*      */   }
/*      */ 
/*      */   private static String nvl(String value)
/*      */   {
/* 3637 */     return value == null ? "" : new String(value);
/*      */   }
/*      */ 
/*      */   protected Shell createShell(Shell parent)
/*      */   {
/* 3647 */     Shell newShell = new Shell(parent, 67680);
/* 3648 */     return newShell;
/*      */   }
/*      */ 
/*      */   private void populateBoundsNames(String table)
/*      */   {
/* 3657 */     this.boundsNameCbo.removeAll();
/* 3658 */     for (String str : PgenStaticDataProvider.getProvider().getBoundsNames(table)) {
/* 3659 */       this.boundsNameCbo.add(str);
/*      */     }
/* 3661 */     this.boundsNameCbo.select(0);
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureDialog
 * JD-Core Version:    0.6.2
 */