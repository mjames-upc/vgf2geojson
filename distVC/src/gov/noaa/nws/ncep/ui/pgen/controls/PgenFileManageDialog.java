/*      */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*      */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Color;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*      */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenSnapJet;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.FileDateComparator;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.FileNameComparator;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.NmapCommon;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Scanner;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.jface.viewers.ISelectionChangedListener;
/*      */ import org.eclipse.jface.viewers.ListViewer;
/*      */ import org.eclipse.jface.viewers.SelectionChangedEvent;
/*      */ import org.eclipse.swt.events.ModifyEvent;
/*      */ import org.eclipse.swt.events.ModifyListener;
/*      */ import org.eclipse.swt.events.SelectionAdapter;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.layout.FormAttachment;
/*      */ import org.eclipse.swt.layout.FormData;
/*      */ import org.eclipse.swt.layout.FormLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.FileDialog;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Listener;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class PgenFileManageDialog extends CaveJFACEDialog
/*      */ {
/*  107 */   private String title = null;
/*      */   private Shell shell;
/*  111 */   private Button sortByNameBtn = null;
/*  112 */   private Button sortByDateBtn = null;
/*      */ 
/*  114 */   private org.eclipse.swt.widgets.List dirList = null;
/*  115 */   private ListViewer fileListViewer = null;
/*  116 */   private static Text fileNameText = null;
/*      */ 
/*  118 */   private Button browseBtn = null;
/*  119 */   private FileDialog browseDlg = null;
/*      */ 
/*  121 */   private Button autoSaveOffBtn = null;
/*  122 */   private Button autoSaveOnBtn = null;
/*      */   private static final int REPLACE_ID = 8610;
/*      */   private static final String REPLACE_LABEL = "Replace";
/*      */   private static final int ADD_ID = 8611;
/*      */   private static final String ADD_LABEL = "Add";
/*      */   private static final int APPEND_ID = 8612;
/*      */   private static final String APPEND_LABEL = "Append";
/*      */   private static final int SAVE_ID = 8613;
/*      */   private static final String SAVE_LABEL = "Save";
/*      */   private static final int CANCEL_ID = 8614;
/*      */   private static final String CANCEL_LABEL = "Cancel";
/*  135 */   private Button replaceBtn = null;
/*  136 */   private Button addBtn = null;
/*  137 */   private Button appendBtn = null;
/*  138 */   private Button cancelBtn = null;
/*  139 */   private Button saveBtn = null;
/*      */ 
/*  141 */   private static LinkedHashMap<String, String> dirTableMap = null;
/*  142 */   private String dirLocal = ".";
/*      */ 
/*  144 */   private static String selectedDir = null;
/*  145 */   private static String fileName = null;
/*  146 */   private static String fullName = null;
/*      */   private static final String LPF_LAYER_NAME = "name";
/*      */   private static final String LPF_LAYER_INPUT_FILE = "file";
/*      */   private static final String LPF_LAYER_OUTPUT_FILE = "output_file";
/*      */   private static final String LPF_LAYER_COLOR_MODE = "color_mode";
/*      */   private static final String LPF_LAYER_COLOR_ID = "color_id";
/*      */   private static final String LPF_LAYER_FILL_MODE = "fill_mode";
/*      */   private static final String LPF_LAYER_DISPLAY_MODE = "display_mode";
/*  158 */   private static PgenFileMode fileMode = PgenFileMode.OPEN;
/*      */ 
/*  160 */   private static int lastDirPos = -1;
/*      */ 
/*  165 */   private static HashMap<Integer, Integer[]> nmapColors = null;
/*      */ 
/*      */   public PgenFileManageDialog(Shell parShell, String btnName)
/*      */     throws VizException
/*      */   {
/*  172 */     super(parShell);
/*      */ 
/*  174 */     setFileMode(btnName);
/*      */ 
/*  176 */     String currentWorkingDir = PgenUtil.getWorkingDirectory();
/*      */ 
/*  178 */     if (currentWorkingDir != null) {
/*  179 */       this.dirLocal = new String(currentWorkingDir);
/*      */     }
/*      */ 
/*  182 */     loadUserTable();
/*      */   }
/*      */ 
/*      */   private void setFileMode(String btnName)
/*      */   {
/*  190 */     if (btnName.equals("Open")) {
/*  191 */       fileMode = PgenFileMode.OPEN;
/*  192 */       this.title = "Open a PGEN Product file";
/*      */     }
/*  194 */     else if ((btnName.equals("Save")) || (btnName.equals("Save All"))) {
/*  195 */       fileMode = PgenFileMode.SAVE;
/*  196 */       this.title = "Save the PGEN Product";
/*      */     }
/*  198 */     else if (btnName.equals("Save As")) {
/*  199 */       fileMode = PgenFileMode.SAVE_AS;
/*  200 */       this.title = "Save the PGEN Product as";
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void configureShell(Shell shell)
/*      */   {
/*  212 */     super.configureShell(shell);
/*  213 */     setShellStyle(32784);
/*      */ 
/*  215 */     this.shell = shell;
/*  216 */     if (this.title != null)
/*  217 */       shell.setText(this.title);
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  230 */     Composite dlgAreaForm = (Composite)super.createDialogArea(parent);
/*      */ 
/*  232 */     Composite topForm = new Composite(dlgAreaForm, 0);
/*  233 */     topForm.setLayout(new FormLayout());
/*      */ 
/*  238 */     Composite sortForm = new Composite(topForm, 0);
/*  239 */     sortForm.setLayout(new FormLayout());
/*      */ 
/*  241 */     this.sortByNameBtn = new Button(sortForm, 16);
/*  242 */     this.sortByNameBtn.setText("Sort Alphabetically");
/*      */ 
/*  244 */     FormData layoutData1 = new FormData(340, 25);
/*  245 */     layoutData1.top = new FormAttachment(0, 0);
/*  246 */     layoutData1.left = new FormAttachment(0, 0);
/*      */ 
/*  248 */     this.sortByNameBtn.setLayoutData(layoutData1);
/*      */ 
/*  250 */     this.sortByNameBtn.setSelection(true);
/*      */ 
/*  255 */     this.sortByNameBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent ev) {
/*  257 */         PgenFileManageDialog.this.fileListViewer.setContentProvider(NmapCommon.createFileContentProvider(new String[] { ".xml" }, new FileNameComparator()));
/*  258 */         PgenFileManageDialog.this.fileListViewer.setLabelProvider(NmapCommon.createFileLabelProvider());
/*  259 */         PgenFileManageDialog.this.fileListViewer.refresh(true);
/*      */       }
/*      */     });
/*  264 */     this.sortByDateBtn = new Button(sortForm, 16);
/*  265 */     this.sortByDateBtn.setText("Sort By Date");
/*      */ 
/*  267 */     FormData layoutData3 = new FormData();
/*  268 */     layoutData3.top = new FormAttachment(this.sortByNameBtn, 5, 1024);
/*  269 */     layoutData3.left = new FormAttachment(this.sortByNameBtn, 0, 16384);
/*  270 */     this.sortByDateBtn.setLayoutData(layoutData3);
/*      */ 
/*  275 */     this.sortByDateBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent ev) {
/*  277 */         PgenFileManageDialog.this.fileListViewer.setContentProvider(NmapCommon.createFileContentProvider(new String[] { ".xml" }, new FileDateComparator()));
/*  278 */         PgenFileManageDialog.this.fileListViewer.setLabelProvider(NmapCommon.createFileLabelProvider());
/*  279 */         PgenFileManageDialog.this.fileListViewer.refresh(true);
/*      */       }
/*      */     });
/*  287 */     Label dirLbl = new Label(topForm, 0);
/*  288 */     dirLbl.setText("Select Directory:");
/*      */ 
/*  290 */     FormData layoutData5 = new FormData();
/*  291 */     layoutData5.top = new FormAttachment(sortForm, 15, 1024);
/*  292 */     layoutData5.left = new FormAttachment(sortForm, 0, 16384);
/*      */ 
/*  294 */     dirLbl.setLayoutData(layoutData5);
/*      */ 
/*  296 */     this.dirList = new org.eclipse.swt.widgets.List(topForm, 2564);
/*      */ 
/*  308 */     for (String str : dirTableMap.keySet()) {
/*  309 */       this.dirList.add(str);
/*      */     }
/*      */ 
/*  312 */     if (lastDirPos < 0) {
/*  313 */       lastDirPos = this.dirList.getItemCount() - 1;
/*      */     }
/*      */ 
/*  316 */     this.dirList.setSelection(lastDirPos);
/*  317 */     selectedDir = (String)dirTableMap.get(this.dirList.getSelection()[0]);
/*      */ 
/*  319 */     FormData layoutData6 = new FormData(320, 200);
/*  320 */     layoutData6.top = new FormAttachment(dirLbl, 5, 1024);
/*  321 */     layoutData6.left = new FormAttachment(dirLbl, 0, 16384);
/*  322 */     this.dirList.setLayoutData(layoutData6);
/*      */ 
/*  324 */     this.dirList.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  326 */         if (PgenFileManageDialog.this.dirList.getSelectionCount() > 0)
/*      */         {
/*  328 */           PgenFileManageDialog.selectedDir = (String)PgenFileManageDialog.dirTableMap.get(PgenFileManageDialog.this.dirList.getSelection()[0]);
/*      */ 
/*  330 */           PgenFileManageDialog.this.dirList.setToolTipText(PgenFileManageDialog.selectedDir);
/*      */ 
/*  332 */           PgenFileManageDialog.this.fileListViewer.setInput(new File(PgenFileManageDialog.selectedDir));
/*  333 */           PgenFileManageDialog.lastDirPos = PgenFileManageDialog.this.dirList.getSelectionIndex();
/*  334 */           PgenFileManageDialog.this.fileListViewer.refresh();
/*      */ 
/*  337 */           PgenFileManageDialog.fullName = null;
/*  338 */           if (PgenFileManageDialog.fileMode != PgenFileManageDialog.PgenFileMode.OPEN)
/*  339 */             PgenFileManageDialog.fullName = new String(PgenFileManageDialog.selectedDir + File.separator + PgenFileManageDialog.fileNameText.getText());
/*      */         }
/*      */       }
/*      */     });
/*  349 */     Label fileLbl = new Label(topForm, 0);
/*  350 */     fileLbl.setText("Select a Product File:");
/*      */ 
/*  352 */     FormData layoutData8 = new FormData();
/*  353 */     layoutData8.top = new FormAttachment(this.dirList, 20, 1024);
/*  354 */     layoutData8.left = new FormAttachment(this.dirList, 0, 16384);
/*      */ 
/*  356 */     fileLbl.setLayoutData(layoutData8);
/*      */ 
/*  358 */     this.fileListViewer = new ListViewer(topForm, 2564);
/*      */ 
/*  360 */     FormData layoutData9 = new FormData(320, 200);
/*  361 */     layoutData9.top = new FormAttachment(fileLbl, 5, 1024);
/*  362 */     layoutData9.left = new FormAttachment(fileLbl, 0, 16384);
/*      */ 
/*  364 */     this.fileListViewer.getList().setLayoutData(layoutData9);
/*      */ 
/*  366 */     if (this.sortByNameBtn.getSelection()) {
/*  367 */       this.fileListViewer.setContentProvider(NmapCommon.createFileContentProvider(new String[] { ".xml", ".lpf" }, new FileNameComparator()));
/*      */     }
/*      */     else {
/*  370 */       this.fileListViewer.setContentProvider(NmapCommon.createFileContentProvider(new String[] { ".xml", ".lpf" }, new FileDateComparator()));
/*      */     }
/*      */ 
/*  373 */     this.fileListViewer.setLabelProvider(NmapCommon.createFileLabelProvider(new String[] { "" }));
/*      */ 
/*  375 */     this.fileListViewer.setInput(new File(selectedDir));
/*  376 */     this.fileListViewer.refresh();
/*      */ 
/*  378 */     if (fileName != null) {
/*  379 */       int selFileInd = this.fileListViewer.getList().indexOf(fileName);
/*  380 */       if (selFileInd >= 0) {
/*  381 */         this.fileListViewer.getList().select(selFileInd);
/*      */       }
/*      */       else {
/*  384 */         fileName = null;
/*  385 */         fullName = null;
/*      */       }
/*      */     }
/*      */ 
/*  389 */     this.fileListViewer.addSelectionChangedListener(new ISelectionChangedListener() {
/*      */       public void selectionChanged(SelectionChangedEvent event) {
/*  391 */         String[] selectedFile = PgenFileManageDialog.this.fileListViewer.getList().getSelection();
/*      */ 
/*  393 */         if (selectedFile.length == 0) {
/*  394 */           if (PgenFileManageDialog.fileMode != PgenFileManageDialog.PgenFileMode.OPEN) {
/*  395 */             PgenFileManageDialog.this.saveBtn.setEnabled(false);
/*      */           }
/*      */           else {
/*  398 */             PgenFileManageDialog.this.replaceBtn.setEnabled(false);
/*  399 */             PgenFileManageDialog.this.addBtn.setEnabled(false);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  404 */           PgenFileManageDialog.fileName = selectedFile[0];
/*  405 */           PgenFileManageDialog.fullName = PgenFileManageDialog.selectedDir + File.separator + PgenFileManageDialog.fileName;
/*      */ 
/*  407 */           if (PgenFileManageDialog.fileMode != PgenFileManageDialog.PgenFileMode.OPEN) {
/*  408 */             PgenFileManageDialog.fileNameText.setText(PgenFileManageDialog.fileName);
/*  409 */             PgenFileManageDialog.this.saveBtn.setEnabled(true);
/*      */           }
/*      */           else {
/*  412 */             PgenFileManageDialog.this.replaceBtn.setEnabled(true);
/*  413 */             PgenFileManageDialog.this.addBtn.setEnabled(true);
/*      */           }
/*      */         }
/*      */       }
/*      */     });
/*  423 */     if (fileMode != PgenFileMode.OPEN) {
/*  424 */       Label fileNameLbl = new Label(topForm, 0);
/*  425 */       fileNameLbl.setText("Or Enter a File Name");
/*      */ 
/*  427 */       FormData layoutData9_1 = new FormData();
/*  428 */       layoutData9_1.top = new FormAttachment(this.fileListViewer.getList(), 25, 1024);
/*  429 */       layoutData9_1.left = new FormAttachment(this.fileListViewer.getList(), 0, 16384);
/*  430 */       fileNameLbl.setLayoutData(layoutData9_1);
/*      */ 
/*  432 */       fileNameText = new Text(topForm, 2052);
/*      */ 
/*  434 */       FormData layoutData9_2 = new FormData(320, 15);
/*  435 */       layoutData9_2.top = new FormAttachment(fileNameLbl, 10, 1024);
/*  436 */       layoutData9_2.left = new FormAttachment(fileNameLbl, 0, 16384);
/*  437 */       fileNameText.setLayoutData(layoutData9_2);
/*      */ 
/*  439 */       fileNameText.setText("");
/*      */ 
/*  443 */       String curFile = PgenSession.getInstance().getPgenResource().getActiveProduct().getOutputFile();
/*  444 */       String configuredFile = getConfiguredFile();
/*  445 */       if ((curFile == null) && (configuredFile != null)) {
/*  446 */         fileName = new String(configuredFile);
/*  447 */         fullName = new String(selectedDir + File.separator + fileName);
/*      */       }
/*      */ 
/*  450 */       if (fileName != null) {
/*  451 */         fileNameText.setText(fileName);
/*      */       }
/*      */ 
/*  455 */       fileNameText.setSize(310, 10);
/*  456 */       fileNameText.setEditable(true);
/*      */ 
/*  458 */       fileNameText.addModifyListener(new ModifyListener()
/*      */       {
/*      */         public void modifyText(ModifyEvent event) {
/*  461 */           String usrFile = PgenFileManageDialog.fileNameText.getText().trim();
/*      */ 
/*  463 */           if ((!usrFile.isEmpty()) && 
/*  464 */             (!usrFile.contains(File.separator))) {
/*  465 */             usrFile = PgenFileManageDialog.selectedDir + File.separator + usrFile;
/*      */           }
/*      */ 
/*  469 */           if (!usrFile.endsWith(".xml")) {
/*  470 */             usrFile = usrFile + ".xml";
/*      */           }
/*      */ 
/*  473 */           PgenFileManageDialog.fullName = usrFile;
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/*  483 */     this.browseBtn = new Button(topForm, 8);
/*      */ 
/*  485 */     FormData layoutData10 = new FormData(330, 25);
/*  486 */     if (fileMode != PgenFileMode.OPEN) {
/*  487 */       layoutData10.top = new FormAttachment(fileNameText, 20, 1024);
/*  488 */       layoutData10.left = new FormAttachment(fileNameText, 0, 16384);
/*      */     }
/*      */     else {
/*  491 */       layoutData10.top = new FormAttachment(this.fileListViewer.getList(), 20, 1024);
/*  492 */       layoutData10.left = new FormAttachment(this.fileListViewer.getList(), 0, 16384);
/*      */     }
/*      */ 
/*  495 */     this.browseBtn.setLayoutData(layoutData10);
/*  496 */     this.browseBtn.setSize(330, 20);
/*  497 */     this.browseBtn.setText("Browse");
/*      */ 
/*  499 */     this.browseBtn.addListener(4, new Listener() {
/*      */       public void handleEvent(Event event) {
/*  501 */         if (PgenFileManageDialog.fileMode != PgenFileManageDialog.PgenFileMode.OPEN) {
/*  502 */           PgenFileManageDialog.this.browseDlg = new FileDialog(PgenFileManageDialog.this.shell, 8192);
/*  503 */           PgenFileManageDialog.this.browseDlg.setFileName("default.xml");
/*      */         }
/*      */         else {
/*  506 */           PgenFileManageDialog.this.browseDlg = new FileDialog(PgenFileManageDialog.this.shell, 4096);
/*      */         }
/*      */ 
/*  509 */         PgenFileManageDialog.this.browseDlg.setFilterNames(new String[] { "Product files (*.xml, *.lpf)" });
/*  510 */         PgenFileManageDialog.this.browseDlg.setFilterExtensions(new String[] { "*.xml", "*.lpf" });
/*  511 */         PgenFileManageDialog.this.browseDlg.setFilterPath(PgenFileManageDialog.this.dirLocal);
/*      */ 
/*  513 */         String s = PgenFileManageDialog.this.browseDlg.open();
/*  514 */         if ((s != null) && (!s.isEmpty()) && 
/*  515 */           (!PgenFileManageDialog.this.browseDlg.getFileName().isEmpty())) {
/*  516 */           PgenFileManageDialog.selectedDir = PgenFileManageDialog.this.browseDlg.getFilterPath();
/*  517 */           PgenFileManageDialog.fileName = PgenFileManageDialog.this.browseDlg.getFileName();
/*  518 */           PgenFileManageDialog.fullName = PgenFileManageDialog.selectedDir + File.separator + PgenFileManageDialog.fileName;
/*  519 */           if (PgenFileManageDialog.fileMode != PgenFileManageDialog.PgenFileMode.OPEN)
/*  520 */             PgenFileManageDialog.fileNameText.setText(PgenFileManageDialog.fullName);
/*      */         }
/*      */       }
/*      */     });
/*  530 */     Label autoSaveLbl = new Label(topForm, 0);
/*  531 */     autoSaveLbl.setText("Auto Save:");
/*      */ 
/*  533 */     FormData layoutData11 = new FormData();
/*  534 */     layoutData11.top = new FormAttachment(this.browseBtn, 20, 1024);
/*  535 */     layoutData11.left = new FormAttachment(this.browseBtn, 0, 16384);
/*  536 */     autoSaveLbl.setLayoutData(layoutData11);
/*      */ 
/*  538 */     this.autoSaveOffBtn = new Button(topForm, 16);
/*  539 */     this.autoSaveOffBtn.setText("Off");
/*  540 */     this.autoSaveOffBtn.setSelection(true);
/*      */ 
/*  542 */     FormData layoutData12 = new FormData();
/*  543 */     layoutData12.top = new FormAttachment(autoSaveLbl, 0, 128);
/*  544 */     layoutData12.left = new FormAttachment(autoSaveLbl, 10, 131072);
/*  545 */     this.autoSaveOffBtn.setLayoutData(layoutData12);
/*      */ 
/*  547 */     this.autoSaveOnBtn = new Button(topForm, 16);
/*  548 */     this.autoSaveOnBtn.setText("On");
/*      */ 
/*  550 */     FormData layoutData13 = new FormData();
/*  551 */     layoutData13.top = new FormAttachment(this.autoSaveOffBtn, 0, 128);
/*  552 */     layoutData13.left = new FormAttachment(this.autoSaveOffBtn, 10, 131072);
/*  553 */     this.autoSaveOnBtn.setLayoutData(layoutData13);
/*      */ 
/*  555 */     return dlgAreaForm;
/*      */   }
/*      */ 
/*      */   protected void createButtonsForButtonBar(Composite parent)
/*      */   {
/*  565 */     if (fileMode == PgenFileMode.OPEN)
/*      */     {
/*  567 */       this.addBtn = createButton(parent, 8611, "Add", true);
/*  568 */       this.replaceBtn = createButton(parent, 8610, "Replace", true);
/*  569 */       this.appendBtn = createButton(parent, 8612, "Append", true);
/*      */ 
/*  571 */       this.replaceBtn.addListener(4, new Listener() {
/*      */         public void handleEvent(Event event) {
/*  573 */           PgenFileManageDialog.this.openProducts(true);
/*      */         }
/*      */       });
/*  577 */       this.addBtn.addListener(4, new Listener() {
/*      */         public void handleEvent(Event event) {
/*  579 */           PgenFileManageDialog.this.openProducts(false);
/*      */         }
/*      */       });
/*  583 */       this.appendBtn.addListener(4, new Listener() {
/*      */         public void handleEvent(Event event) {
/*  585 */           PgenFileManageDialog.this.appendProducts();
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */     else
/*      */     {
/*  592 */       this.saveBtn = createButton(parent, 8613, "Save", true);
/*      */ 
/*  594 */       this.saveBtn.addListener(4, new Listener() {
/*      */         public void handleEvent(Event event) {
/*  596 */           PgenFileManageDialog.this.saveProducts();
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/*  602 */     this.cancelBtn = createButton(parent, 8614, "Cancel", true);
/*  603 */     this.cancelBtn.addListener(4, new Listener() {
/*      */       public void handleEvent(Event event) {
/*  605 */         PgenFileManageDialog.this.close();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void loadUserTable()
/*      */   {
/*  616 */     dirTableMap = new LinkedHashMap();
/*      */ 
/*  645 */     String pbase = PgenUtil.getPgenOprDirectory();
/*  646 */     String curPrdType = PgenSession.getInstance().getPgenResource().getActiveProduct().getType();
/*      */ 
/*  648 */     LinkedHashMap prdTypes = ProductConfigureDialog.getProductTypes();
/*      */ 
/*  650 */     ProductType curAct = (ProductType)prdTypes.get(curPrdType);
/*  651 */     if (curAct != null) {
/*  652 */       curPrdType = new String(curAct.getType());
/*      */     }
/*      */ 
/*  655 */     ArrayList uniPrdTypeNames = new ArrayList();
/*      */     String mtyp;
/*  657 */     for (String ptypName : prdTypes.keySet()) {
/*  658 */       mtyp = ((ProductType)prdTypes.get(ptypName)).getType();
/*  659 */       if (!uniPrdTypeNames.contains(mtyp)) {
/*  660 */         uniPrdTypeNames.add(mtyp);
/*      */       }
/*      */     }
/*      */ 
/*  664 */     int ii = 0;
/*  665 */     lastDirPos = -1;
/*  666 */     for (String ptypName : uniPrdTypeNames)
/*      */     {
/*  668 */       String sdir = new String(pbase + File.separator + 
/*  669 */         ptypName.replace(' ', '_') + 
/*  670 */         File.separator + "xml");
/*      */ 
/*  672 */       dirTableMap.put(ptypName, sdir);
/*      */ 
/*  674 */       if (ptypName.equalsIgnoreCase(curPrdType)) {
/*  675 */         lastDirPos = ii;
/*      */       }
/*  677 */       ii++;
/*      */     }
/*      */ 
/*  680 */     dirTableMap.put("Local", this.dirLocal);
/*      */   }
/*      */ 
/*      */   private void openProducts(boolean replace)
/*      */   {
/*  691 */     if (!validProductFile(fullName)) {
/*  692 */       return;
/*      */     }
/*      */ 
/*  695 */     Products products = null;
/*  696 */     if (fullName.endsWith(".lpf")) {
/*  697 */       products = loadLpfFile(fullName);
/*      */     }
/*      */     else {
/*  700 */       products = FileTools.read(fullName);
/*      */     }
/*      */ 
/*  703 */     if (products == null) {
/*  704 */       MessageDialog confirmDlg = new MessageDialog(
/*  705 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  706 */         "Invalid PGEN Product File", null, 
/*  707 */         "File " + fileName + " is not a valid PGEN product file. \nPlease select another one.", 
/*  708 */         2, new String[] { "OK" }, 0);
/*      */ 
/*  710 */       confirmDlg.open();
/*      */ 
/*  712 */       return;
/*      */     }
/*      */ 
/*  719 */     if (VaaInfo.isNoneDrawableTxt(products)) {
/*  720 */       VaaInfo.openMsgDlg("THIS IS A TEXT PRODUCT THAT CANNOT BE DISPLAYED!");
/*  721 */       return;
/*      */     }
/*      */ 
/*  727 */     PgenResource pgen = PgenSession.getInstance().getPgenResource();
/*      */ 
/*  729 */     java.util.List pgenProds = 
/*  730 */       ProductConverter.convert(products);
/*      */ 
/*  740 */     if (replace) {
/*  741 */       MessageDialog confirmOpen = new MessageDialog(
/*  742 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  743 */         "Confirm File Replace", null, 
/*  744 */         "Replace Activity <" + pgen.getActiveProduct().getType() + 
/*  745 */         "> with New Activity <" + ((gov.noaa.nws.ncep.ui.pgen.elements.Product)pgenProds.get(0)).getType() + "> ?", 
/*  746 */         2, new String[] { "Yes", "No" }, 0);
/*      */ 
/*  748 */       confirmOpen.open();
/*      */ 
/*  750 */       if (confirmOpen.getReturnCode() != 0) {
/*  751 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  756 */     pgen.setAutosave(this.autoSaveOnBtn.getSelection());
/*  757 */     if (fullName.endsWith(".lpf")) {
/*  758 */       pgen.setAutoSaveFilename(fullName.replace(".lpf", "xml"));
/*      */     }
/*      */     else {
/*  761 */       pgen.setAutoSaveFilename(fullName);
/*      */     }
/*      */ 
/*  764 */     ((gov.noaa.nws.ncep.ui.pgen.elements.Product)pgenProds.get(0)).setInputFile(fullName);
/*      */ 
/*  766 */     setJetTool(pgenProds);
/*      */ 
/*  768 */     close();
/*      */ 
/*  773 */     if (replace)
/*      */     {
/*  775 */       for (gov.noaa.nws.ncep.ui.pgen.elements.Product pp : pgenProds) {
/*  776 */         pp.setOutputFile(null);
/*      */       }
/*      */ 
/*  779 */       PgenFileNameDisplay.getInstance().setFileName(fullName);
/*      */ 
/*  781 */       pgen.replaceProduct(pgenProds);
/*      */     }
/*      */     else {
/*  784 */       if (pgen.getActiveProduct() == null) {
/*  785 */         PgenFileNameDisplay.getInstance().setFileName(fullName);
/*      */       }
/*      */ 
/*  788 */       pgen.addProduct(pgenProds);
/*      */     }
/*      */ 
/*  791 */     PgenUtil.refresh();
/*      */   }
/*      */ 
/*      */   private void appendProducts()
/*      */   {
/*  803 */     if (!validProductFile(fullName)) {
/*  804 */       return;
/*      */     }
/*      */ 
/*  807 */     Products products = null;
/*  808 */     if (fullName.endsWith(".lpf")) {
/*  809 */       products = loadLpfFile(fullName);
/*      */     }
/*      */     else {
/*  812 */       products = FileTools.read(fullName);
/*      */     }
/*      */ 
/*  815 */     if (products == null) {
/*  816 */       MessageDialog confirmDlg = new MessageDialog(
/*  817 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  818 */         "Invalid PGEN Product File", null, 
/*  819 */         "File " + fileName + " is not a valid PGEN product file. \nPlease select another one.", 
/*  820 */         2, new String[] { "OK" }, 0);
/*      */ 
/*  822 */       confirmDlg.open();
/*      */ 
/*  824 */       return;
/*      */     }
/*      */ 
/*  831 */     if (VaaInfo.isNoneDrawableTxt(products)) {
/*  832 */       VaaInfo.openMsgDlg("THIS IS A TEXT PRODUCT THAT CANNOT BE DISPLAYED!");
/*  833 */       return;
/*      */     }
/*      */ 
/*  836 */     PgenResource pgen = PgenSession.getInstance().getPgenResource();
/*      */ 
/*  838 */     java.util.List pgenProds = 
/*  839 */       ProductConverter.convert(products);
/*      */ 
/*  841 */     MessageDialog confirmOpen = new MessageDialog(
/*  842 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  843 */       "Confirm File Append", null, 
/*  844 */       "Append Contens in Activity <" + ((gov.noaa.nws.ncep.ui.pgen.elements.Product)pgenProds.get(0)).getType() + 
/*  845 */       "> to Current Activity <" + pgen.getActiveProduct().getType() + "> ?", 
/*  846 */       2, new String[] { "Yes", "No" }, 0);
/*      */ 
/*  848 */     confirmOpen.open();
/*      */ 
/*  850 */     if (confirmOpen.getReturnCode() != 0) {
/*  851 */       return;
/*      */     }
/*      */ 
/*  855 */     pgen.setAutosave(this.autoSaveOnBtn.getSelection());
/*  856 */     if (fullName.endsWith(".lpf")) {
/*  857 */       pgen.setAutoSaveFilename(fullName.replace(".lpf", "xml"));
/*      */     }
/*      */     else {
/*  860 */       pgen.setAutoSaveFilename(fullName);
/*      */     }
/*      */ 
/*  863 */     setJetTool(pgenProds);
/*      */ 
/*  865 */     close();
/*      */ 
/*  867 */     pgen.appendProduct(pgenProds);
/*      */ 
/*  869 */     PgenUtil.refresh();
/*      */   }
/*      */ 
/*      */   private void saveProducts()
/*      */   {
/*  878 */     if (!validProductFile(fullName)) {
/*  879 */       return;
/*      */     }
/*      */ 
/*  883 */     PgenResource rsc = PgenSession.getInstance().getPgenResource();
/*      */ 
/*  885 */     boolean dup = false;
/*  886 */     for (gov.noaa.nws.ncep.ui.pgen.elements.Product p : rsc.getProducts()) {
/*  887 */       for (gov.noaa.nws.ncep.ui.pgen.elements.Product p1 : rsc.getProducts()) {
/*  888 */         if ((!p1.equals(p)) && (p1.getName().equals(p.getName()))) {
/*  889 */           dup = true;
/*  890 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  895 */     if (dup) {
/*  896 */       MessageDialog confirmDlg = new MessageDialog(
/*  897 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  898 */         "Duplicate Product Name", null, 
/*  899 */         "There are duplicate product names. \nPlease make them unique before saving!", 
/*  900 */         4, new String[] { "OK" }, 0);
/*      */ 
/*  902 */       confirmDlg.open();
/*      */     }
/*      */ 
/*  914 */     boolean saveFile = true;
/*  915 */     File infile = new File(fullName);
/*  916 */     String msg = "Are you sure you want to save to: " + fullName + "?";
/*  917 */     if (infile.exists()) {
/*  918 */       msg = "File " + fullName + " exists. Are you sure you want to overwrite it?";
/*      */     }
/*      */ 
/*  921 */     MessageDialog confirmDlg = new MessageDialog(
/*  922 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  923 */       "Saving a PGEN file", null, msg, 
/*  924 */       3, new String[] { "Yes", "No" }, 0);
/*      */ 
/*  926 */     confirmDlg.open();
/*      */ 
/*  928 */     if (confirmDlg.getReturnCode() != 0) {
/*  929 */       saveFile = false;
/*      */     }
/*      */ 
/*  933 */     if (saveFile)
/*      */     {
/*  935 */       rsc.setAutosave(this.autoSaveOnBtn.getSelection());
/*  936 */       rsc.setAutoSaveFilename(fullName);
/*      */ 
/*  938 */       close();
/*  939 */       PgenSession.getInstance().getPgenPalette().setActiveIcon("Select");
/*      */ 
/*  941 */       rsc.getActiveProduct().setInputFile(fullName);
/*  942 */       rsc.getActiveProduct().setOutputFile(fullName);
/*      */ 
/*  944 */       rsc.saveCurrentProduct(fullName);
/*      */ 
/*  946 */       PgenFileNameDisplay.getInstance().setFileName(fullName);
/*      */ 
/*  948 */       PgenUtil.setSelectingMode();
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean validProductFile(String vfile)
/*      */   {
/*  958 */     String msg = null;
/*  959 */     if (vfile == null) {
/*  960 */       if (fileMode == PgenFileMode.OPEN) {
/*  961 */         msg = "Please select a file!";
/*      */       }
/*      */       else
/*  964 */         msg = "Please input a file name!";
/*      */     }
/*      */     else
/*      */     {
/*  968 */       File infile = new File(vfile);
/*  969 */       if (fileMode == PgenFileMode.OPEN) {
/*  970 */         if ((!infile.exists()) && (!infile.canRead())) {
/*  971 */           msg = "File " + vfile + " cannot be read!";
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  976 */         String fpath = fullName.substring(0, fullName.lastIndexOf("/"));
/*  977 */         File indir = new File(fpath);
/*  978 */         if ((indir.exists()) && (!indir.canWrite())) {
/*  979 */           msg = "No write permission to directory: " + fpath + "!";
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  984 */     if (msg != null) {
/*  985 */       MessageDialog confirmDlg = new MessageDialog(
/*  986 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  987 */         "Invalid PGEN Product File", null, msg, 
/*  988 */         2, new String[] { "OK" }, 0);
/*      */ 
/*  990 */       confirmDlg.open();
/*      */ 
/*  992 */       return false;
/*      */     }
/*      */ 
/*  995 */     return true;
/*      */   }
/*      */ 
/*      */   private void setJetTool(java.util.List<gov.noaa.nws.ncep.ui.pgen.elements.Product> prods)
/*      */   {
/* 1007 */     PgenSnapJet st = new PgenSnapJet((IMapDescriptor)PgenSession.getInstance().getPgenResource().getDescriptor(), 
/* 1008 */       PgenUtil.getActiveEditor(), null);
/*      */     Iterator localIterator2;
/* 1010 */     for (Iterator localIterator1 = prods.iterator(); localIterator1.hasNext(); 
/* 1011 */       localIterator2.hasNext())
/*      */     {
/* 1010 */       gov.noaa.nws.ncep.ui.pgen.elements.Product prod = (gov.noaa.nws.ncep.ui.pgen.elements.Product)localIterator1.next();
/* 1011 */       localIterator2 = prod.getLayers().iterator(); continue; gov.noaa.nws.ncep.ui.pgen.elements.Layer layer = (gov.noaa.nws.ncep.ui.pgen.elements.Layer)localIterator2.next();
/*      */ 
/* 1013 */       Iterator iterator = layer.getComponentIterator();
/* 1014 */       while (iterator.hasNext()) {
/* 1015 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)iterator.next();
/* 1016 */         if ((adc instanceof Jet))
/* 1017 */           ((Jet)adc).setSnapTool(st);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getConfiguredFile()
/*      */   {
/* 1033 */     String filename = PgenSession.getInstance().getPgenResource().buildFileName(
/* 1034 */       PgenSession.getInstance().getPgenResource().getActiveProduct());
/*      */ 
/* 1036 */     filename = filename.substring(filename.lastIndexOf(File.separator) + 1);
/*      */ 
/* 1038 */     return filename;
/*      */   }
/*      */ 
/*      */   private Products loadLpfFile(String fname)
/*      */   {
/* 1048 */     Products pgenFilePrds = null;
/*      */ 
/* 1050 */     LinkedHashMap prdTypes = ProductConfigureDialog.getProductTypes();
/*      */ 
/* 1052 */     HashMap lpfMap = loadLpfParameters(fname);
/*      */ 
/* 1054 */     ArrayList layerPrefixes = getLayerPrefixes(lpfMap);
/*      */ 
/* 1056 */     String masterType = matchActivityType(lpfMap, prdTypes);
/*      */ 
/* 1059 */     if (layerPrefixes.size() > 0) {
/* 1060 */       pgenFilePrds = new Products();
/* 1061 */       for (String layerPre : layerPrefixes)
/*      */       {
/* 1063 */         Products layerFp = null;
/*      */ 
/* 1065 */         String layerName = (String)lpfMap.get(layerPre + "_" + "name");
/*      */ 
/* 1067 */         if ((layerName != null) && (layerName.trim().length() != 0))
/*      */         {
/* 1071 */           String layerOutputFile = (String)lpfMap.get(layerPre + "_" + "output_file");
/* 1072 */           String layerColorMode = (String)lpfMap.get(layerPre + "_" + "color_mode");
/* 1073 */           String layerColorID = (String)lpfMap.get(layerPre + "_" + "color_id");
/* 1074 */           String layerFillMode = (String)lpfMap.get(layerPre + "_" + "fill_mode");
/*      */ 
/* 1076 */           String layerDisplayMode = (String)lpfMap.get(layerPre + "_" + "display_mode");
/*      */ 
/* 1078 */           String layerFile = (String)lpfMap.get(layerPre + "_" + "file");
/*      */ 
/* 1086 */           String layerInputFile = PgenUtil.parsePgenFileName(layerFile);
/* 1087 */           if (!layerInputFile.contains(File.separator)) {
/* 1088 */             if ((masterType != null) && (!masterType.equalsIgnoreCase("Default"))) {
/* 1089 */               String fnm = new String(PgenUtil.getPgenOprDirectory() + File.separator + 
/* 1090 */                 masterType.replace(" ", "_") + File.separator + "xml" + 
/* 1091 */                 File.separator + layerInputFile);
/*      */ 
/* 1093 */               if (fnm.endsWith("vgf")) {
/* 1094 */                 fnm = fnm.replace(".vgf", ".xml");
/*      */               }
/*      */ 
/* 1097 */               File ff = new File(fnm);
/* 1098 */               if ((ff.exists()) && (ff.canRead())) {
/* 1099 */                 layerInputFile = new String(fnm);
/*      */               }
/*      */               else
/* 1102 */                 layerInputFile = new String(fname.substring(0, fname.lastIndexOf(File.separator)) + 
/* 1103 */                   File.separator + layerInputFile);
/*      */             }
/*      */             else
/*      */             {
/* 1107 */               layerInputFile = new String(fname.substring(0, fname.lastIndexOf(File.separator)) + 
/* 1108 */                 File.separator + layerInputFile);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1113 */           if (layerInputFile != null) {
/* 1114 */             if (layerInputFile.endsWith("vgf")) {
/* 1115 */               layerInputFile = layerInputFile.replace(".vgf", ".xml");
/*      */             }
/*      */ 
/* 1118 */             File ft = new File(layerInputFile);
/* 1119 */             if ((ft.exists()) && (ft.canRead())) {
/* 1120 */               layerFp = FileTools.read(layerInputFile);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1125 */           if (layerFp == null) {
/* 1126 */             gov.noaa.nws.ncep.ui.pgen.elements.Product dfltp = new gov.noaa.nws.ncep.ui.pgen.elements.Product("Default", "Default", "Default", 
/* 1127 */               new ProductInfo(), new ProductTime(), new ArrayList());
/*      */ 
/* 1129 */             gov.noaa.nws.ncep.ui.pgen.elements.Layer dfltly = new gov.noaa.nws.ncep.ui.pgen.elements.Layer();
/* 1130 */             dfltp.addLayer(dfltly);
/*      */ 
/* 1132 */             ArrayList dfltPrds = new ArrayList();
/* 1133 */             dfltPrds.add(dfltp);
/*      */ 
/* 1135 */             layerFp = ProductConverter.convert(dfltPrds);
/*      */           }
/*      */ 
/* 1139 */           if (!VaaInfo.isNoneDrawableTxt(layerFp)) {
/* 1140 */             if (pgenFilePrds.getProduct().size() == 0) {
/* 1141 */               pgenFilePrds.getProduct().addAll(layerFp.getProduct());
/* 1142 */               if ((masterType != null) && (!masterType.equalsIgnoreCase("Default"))) {
/* 1143 */                 ((gov.noaa.nws.ncep.ui.pgen.file.Product)pgenFilePrds.getProduct().get(0)).setType(masterType);
/* 1144 */                 ((gov.noaa.nws.ncep.ui.pgen.file.Product)pgenFilePrds.getProduct().get(0)).setName(masterType);
/*      */               }
/*      */             }
/*      */             else {
/* 1148 */               ((gov.noaa.nws.ncep.ui.pgen.file.Product)pgenFilePrds.getProduct().get(0)).getLayer().addAll(
/* 1149 */                 ((gov.noaa.nws.ncep.ui.pgen.file.Product)layerFp.getProduct().get(0)).getLayer());
/*      */             }
/*      */ 
/* 1156 */             if (((gov.noaa.nws.ncep.ui.pgen.file.Product)pgenFilePrds.getProduct().get(0)).getType().equalsIgnoreCase("Default")) {
/* 1157 */               String ptype = ((gov.noaa.nws.ncep.ui.pgen.file.Product)layerFp.getProduct().get(0)).getType();
/* 1158 */               ptype = matchActivityType(ptype, prdTypes);
/* 1159 */               if (!ptype.equalsIgnoreCase("Default")) {
/* 1160 */                 ((gov.noaa.nws.ncep.ui.pgen.file.Product)pgenFilePrds.getProduct().get(0)).setType(ptype);
/* 1161 */                 ((gov.noaa.nws.ncep.ui.pgen.file.Product)pgenFilePrds.getProduct().get(0)).setName(ptype);
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1167 */           int nly = ((gov.noaa.nws.ncep.ui.pgen.file.Product)pgenFilePrds.getProduct().get(0)).getLayer().size();
/* 1168 */           gov.noaa.nws.ncep.ui.pgen.file.Layer clayer = 
/* 1169 */             (gov.noaa.nws.ncep.ui.pgen.file.Layer)((gov.noaa.nws.ncep.ui.pgen.file.Product)pgenFilePrds.getProduct().get(0)).getLayer().get(nly - 1);
/* 1170 */           clayer.setName(layerName);
/*      */ 
/* 1173 */           if ((layerColorMode != null) && (layerColorMode.trim().length() > 0) && 
/* 1174 */             (layerColorMode.trim().toUpperCase().startsWith("A"))) {
/* 1175 */             clayer.setMonoColor(Boolean.valueOf(false));
/*      */           }
/*      */           else {
/* 1178 */             clayer.setMonoColor(Boolean.valueOf(true));
/*      */           }
/*      */ 
/* 1182 */           if ((layerFillMode != null) && (layerFillMode.trim().length() > 0) && 
/* 1183 */             (layerFillMode.trim().equalsIgnoreCase("On"))) {
/* 1184 */             clayer.setFilled(Boolean.valueOf(true));
/*      */           }
/*      */           else {
/* 1187 */             clayer.setFilled(Boolean.valueOf(false));
/*      */           }
/*      */ 
/* 1191 */           if ((layerDisplayMode != null) && (layerDisplayMode.trim().length() > 0) && 
/* 1192 */             (layerDisplayMode.trim().equalsIgnoreCase("On"))) {
/* 1193 */             clayer.setOnOff(Boolean.valueOf(true));
/*      */           }
/*      */           else {
/* 1196 */             clayer.setOnOff(Boolean.valueOf(false));
/*      */           }
/*      */ 
/* 1200 */           if ((layerOutputFile != null) && (layerOutputFile.trim().length() > 0)) {
/* 1201 */             clayer.setOutputFile(layerOutputFile);
/*      */           }
/*      */           else {
/* 1204 */             clayer.setOutputFile(null);
/*      */           }
/*      */ 
/* 1208 */           if ((layerColorID != null) && (layerColorID.trim().length() > 0)) {
/* 1209 */             int colorNum = Integer.parseInt(layerColorID);
/* 1210 */             if ((colorNum > 0) && (colorNum <= 32)) {
/* 1211 */               Integer[] nmapColor = (Integer[])getNmapColors().get(Integer.valueOf(colorNum));
/* 1212 */               if (nmapColor != null) {
/* 1213 */                 if (clayer.getColor() == null) {
/* 1214 */                   clayer.setColor(new Color());
/* 1215 */                   clayer.getColor().setAlpha(Integer.valueOf(255));
/*      */                 }
/*      */ 
/* 1218 */                 clayer.getColor().setRed(nmapColor[0].intValue());
/* 1219 */                 clayer.getColor().setGreen(nmapColor[1].intValue());
/* 1220 */                 clayer.getColor().setBlue(nmapColor[2].intValue());
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1227 */     return pgenFilePrds;
/*      */   }
/*      */ 
/*      */   private ArrayList<String> getLayerPrefixes(HashMap<String, String> lpfMap)
/*      */   {
/* 1238 */     ArrayList layerKeys = new ArrayList();
/* 1239 */     if ((lpfMap != null) && (lpfMap.size() > 0)) {
/* 1240 */       for (String key : lpfMap.keySet()) {
/* 1241 */         if ((key != null) && (key.contains("name"))) {
/* 1242 */           layerKeys.add(new String(key.substring(0, key.indexOf("_"))));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1247 */     return layerKeys;
/*      */   }
/*      */ 
/*      */   private LinkedHashMap<String, String> loadLpfParameters(String fname)
/*      */   {
/* 1259 */     LinkedHashMap params = new LinkedHashMap();
/*      */ 
/* 1262 */     File thisFile = null;
/* 1263 */     if (fname != null) {
/* 1264 */       thisFile = new File(fname);
/* 1265 */       if ((!thisFile.exists()) || (!thisFile.canRead())) {
/* 1266 */         thisFile = null;
/*      */       }
/*      */     }
/*      */ 
/* 1270 */     if (thisFile == null) {
/* 1271 */       return params;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1276 */       Scanner fileScanner = new Scanner(thisFile);
/*      */       try
/*      */       {
/* 1280 */         while (fileScanner.hasNextLine()) {
/* 1281 */           String nextLine = fileScanner.nextLine().trim();
/*      */ 
/* 1284 */           if (!nextLine.startsWith("!"))
/*      */           {
/* 1286 */             int start = nextLine.indexOf("<");
/* 1287 */             int end = nextLine.indexOf(">");
/*      */ 
/* 1289 */             if ((start >= 0) && (end > 0) && (end > start) && 
/* 1290 */               (nextLine.length() > end))
/*      */             {
/* 1292 */               String name = nextLine.substring(start + 1, end).trim();
/* 1293 */               if (name.length() > 0) {
/* 1294 */                 String value = nextLine.substring(end + 1).trim();
/*      */ 
/* 1296 */                 if (value.length() > 0)
/* 1297 */                   params.put(name, value);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/* 1305 */         fileScanner.close();
/*      */       }
/*      */     }
/*      */     catch (IOException e) {
/* 1309 */       e.printStackTrace();
/*      */     }
/*      */ 
/* 1312 */     return params;
/*      */   }
/*      */ 
/*      */   private static HashMap<Integer, Integer[]> getNmapColors()
/*      */   {
/* 1320 */     if (nmapColors == null) {
/* 1321 */       nmapColors = new HashMap();
/* 1322 */       nmapColors.put(Integer.valueOf(0), new Integer[] { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) });
/* 1323 */       nmapColors.put(Integer.valueOf(1), new Integer[] { Integer.valueOf(255), Integer.valueOf(228), Integer.valueOf(220) });
/* 1324 */       nmapColors.put(Integer.valueOf(2), new Integer[] { Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(0) });
/* 1325 */       nmapColors.put(Integer.valueOf(3), new Integer[] { Integer.valueOf(0), Integer.valueOf(255), Integer.valueOf(0) });
/* 1326 */       nmapColors.put(Integer.valueOf(4), new Integer[] { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255) });
/* 1327 */       nmapColors.put(Integer.valueOf(5), new Integer[] { Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(0) });
/* 1328 */       nmapColors.put(Integer.valueOf(6), new Integer[] { Integer.valueOf(0), Integer.valueOf(255), Integer.valueOf(255) });
/* 1329 */       nmapColors.put(Integer.valueOf(7), new Integer[] { Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255) });
/* 1330 */       nmapColors.put(Integer.valueOf(8), new Integer[] { Integer.valueOf(139), Integer.valueOf(71), Integer.valueOf(38) });
/* 1331 */       nmapColors.put(Integer.valueOf(9), new Integer[] { Integer.valueOf(255), Integer.valueOf(130), Integer.valueOf(71) });
/* 1332 */       nmapColors.put(Integer.valueOf(10), new Integer[] { Integer.valueOf(255), Integer.valueOf(165), Integer.valueOf(79) });
/* 1333 */       nmapColors.put(Integer.valueOf(11), new Integer[] { Integer.valueOf(255), Integer.valueOf(174), Integer.valueOf(185) });
/* 1334 */       nmapColors.put(Integer.valueOf(12), new Integer[] { Integer.valueOf(255), Integer.valueOf(106), Integer.valueOf(106) });
/* 1335 */       nmapColors.put(Integer.valueOf(13), new Integer[] { Integer.valueOf(238), Integer.valueOf(44), Integer.valueOf(44) });
/* 1336 */       nmapColors.put(Integer.valueOf(14), new Integer[] { Integer.valueOf(139), Integer.valueOf(0), Integer.valueOf(0) });
/* 1337 */       nmapColors.put(Integer.valueOf(15), new Integer[] { Integer.valueOf(205), Integer.valueOf(0), Integer.valueOf(0) });
/* 1338 */       nmapColors.put(Integer.valueOf(16), new Integer[] { Integer.valueOf(238), Integer.valueOf(64), Integer.valueOf(0) });
/* 1339 */       nmapColors.put(Integer.valueOf(17), new Integer[] { Integer.valueOf(255), Integer.valueOf(127), Integer.valueOf(0) });
/* 1340 */       nmapColors.put(Integer.valueOf(18), new Integer[] { Integer.valueOf(205), Integer.valueOf(133), Integer.valueOf(0) });
/* 1341 */       nmapColors.put(Integer.valueOf(19), new Integer[] { Integer.valueOf(255), Integer.valueOf(215), Integer.valueOf(0) });
/* 1342 */       nmapColors.put(Integer.valueOf(20), new Integer[] { Integer.valueOf(238), Integer.valueOf(238), Integer.valueOf(0) });
/* 1343 */       nmapColors.put(Integer.valueOf(21), new Integer[] { Integer.valueOf(127), Integer.valueOf(255), Integer.valueOf(0) });
/* 1344 */       nmapColors.put(Integer.valueOf(22), new Integer[] { Integer.valueOf(0), Integer.valueOf(205), Integer.valueOf(0) });
/* 1345 */       nmapColors.put(Integer.valueOf(23), new Integer[] { Integer.valueOf(0), Integer.valueOf(139), Integer.valueOf(0) });
/* 1346 */       nmapColors.put(Integer.valueOf(24), new Integer[] { Integer.valueOf(16), Integer.valueOf(78), Integer.valueOf(139) });
/* 1347 */       nmapColors.put(Integer.valueOf(25), new Integer[] { Integer.valueOf(30), Integer.valueOf(144), Integer.valueOf(255) });
/* 1348 */       nmapColors.put(Integer.valueOf(26), new Integer[] { Integer.valueOf(0), Integer.valueOf(178), Integer.valueOf(238) });
/* 1349 */       nmapColors.put(Integer.valueOf(27), new Integer[] { Integer.valueOf(0), Integer.valueOf(238), Integer.valueOf(238) });
/* 1350 */       nmapColors.put(Integer.valueOf(28), new Integer[] { Integer.valueOf(137), Integer.valueOf(104), Integer.valueOf(205) });
/* 1351 */       nmapColors.put(Integer.valueOf(29), new Integer[] { Integer.valueOf(145), Integer.valueOf(44), Integer.valueOf(238) });
/* 1352 */       nmapColors.put(Integer.valueOf(30), new Integer[] { Integer.valueOf(139), Integer.valueOf(0), Integer.valueOf(139) });
/* 1353 */       nmapColors.put(Integer.valueOf(31), new Integer[] { Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255) });
/* 1354 */       nmapColors.put(Integer.valueOf(32), new Integer[] { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) });
/*      */     }
/*      */ 
/* 1357 */     return nmapColors;
/*      */   }
/*      */ 
/*      */   private String matchActivityType(String typeName, HashMap<String, ProductType> prdTypes)
/*      */   {
/* 1368 */     String tpName = typeName;
/*      */ 
/* 1370 */     boolean matchFound = false;
/*      */ 
/* 1373 */     for (String name : prdTypes.keySet()) {
/* 1374 */       String palias = ((ProductType)prdTypes.get(name)).getName();
/* 1375 */       if ((palias != null) && (typeName.equalsIgnoreCase(palias))) {
/* 1376 */         matchFound = true;
/* 1377 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1381 */     if (!matchFound) {
/* 1382 */       for (String name : prdTypes.keySet()) {
/* 1383 */         String fullTypeStr = new String(((ProductType)prdTypes.get(name)).getType());
/* 1384 */         String subtype = ((ProductType)prdTypes.get(name)).getSubtype();
/* 1385 */         if ((subtype != null) && (!subtype.equalsIgnoreCase("None"))) {
/* 1386 */           fullTypeStr = new String(fullTypeStr + "(" + subtype + ")");
/*      */         }
/*      */ 
/* 1389 */         if (typeName.equalsIgnoreCase(fullTypeStr)) {
/* 1390 */           String st = ((ProductType)prdTypes.get(name)).getName();
/* 1391 */           if ((st != null) && (st.trim().length() > 0)) {
/* 1392 */             tpName = new String(st);
/*      */           }
/*      */           else {
/* 1395 */             tpName = fullTypeStr;
/*      */           }
/*      */ 
/* 1398 */           matchFound = true;
/* 1399 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1404 */     if (!matchFound) {
/* 1405 */       tpName = new String("Default");
/*      */     }
/*      */ 
/* 1408 */     return tpName;
/*      */   }
/*      */ 
/*      */   private String matchActivityType(HashMap<String, String> lpfMap, HashMap<String, ProductType> prdTypes)
/*      */   {
/* 1419 */     HashMap actMap = getActivityInfoMap(lpfMap);
/*      */ 
/* 1421 */     String actType = new String("Default");
/*      */ 
/* 1423 */     if (actMap.size() > 0) {
/* 1424 */       String alias = null;
/* 1425 */       String type = null;
/* 1426 */       String subtype = null;
/*      */ 
/* 1428 */       for (String key : actMap.keySet()) {
/* 1429 */         if (key.contains("alias")) {
/* 1430 */           alias = (String)actMap.get(key);
/*      */         }
/*      */ 
/* 1433 */         if (key.contains("type")) {
/* 1434 */           type = (String)actMap.get(key);
/*      */         }
/*      */ 
/* 1437 */         if (key.contains("subtype")) {
/* 1438 */           subtype = (String)actMap.get(key);
/*      */         }
/*      */       }
/*      */ 
/* 1442 */       if (alias != null) {
/* 1443 */         actType = matchActivityType(alias, prdTypes);
/*      */       }
/*      */ 
/* 1446 */       if (((actType == null) || (actType.equalsIgnoreCase("Default"))) && 
/* 1447 */         (type != null)) {
/* 1448 */         String fullTypeName = new String(type);
/* 1449 */         if (subtype != null) {
/* 1450 */           fullTypeName = new String(fullTypeName + "(" + subtype + ")");
/*      */         }
/*      */ 
/* 1453 */         actType = matchActivityType(fullTypeName, prdTypes);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1458 */     return actType;
/*      */   }
/*      */ 
/*      */   private HashMap<String, String> getActivityInfoMap(HashMap<String, String> lpfMap)
/*      */   {
/* 1468 */     HashMap actInfo = new HashMap();
/* 1469 */     if ((lpfMap != null) && (lpfMap.size() > 0)) {
/* 1470 */       for (String key : lpfMap.keySet()) {
/* 1471 */         if ((key != null) && (key.contains("activity"))) {
/* 1472 */           actInfo.put(key, (String)lpfMap.get(key));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1477 */     return actInfo;
/*      */   }
/*      */ 
/*      */   public static enum PgenFileMode
/*      */   {
/*  105 */     OPEN, SAVE, SAVE_AS, SAVE_ALL;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.PgenFileManageDialog
 * JD-Core Version:    0.6.2
 */