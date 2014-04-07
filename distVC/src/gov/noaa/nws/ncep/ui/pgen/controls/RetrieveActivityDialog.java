/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import com.raytheon.uf.common.dataquery.requests.DbQueryRequest;
/*     */ import com.raytheon.uf.common.dataquery.responses.DbQueryResponse;
/*     */ import com.raytheon.uf.common.status.IUFStatusHandler;
/*     */ import com.raytheon.uf.common.status.UFStatus;
/*     */ import com.raytheon.uf.common.status.UFStatus.Priority;
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*     */ import com.raytheon.uf.viz.core.requests.ThriftClient;
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.PgenRecord;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResourceData;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenSnapJet;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.jface.viewers.ArrayContentProvider;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.ISelectionChangedListener;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.jface.viewers.LabelProvider;
/*     */ import org.eclipse.jface.viewers.ListViewer;
/*     */ import org.eclipse.jface.viewers.SelectionChangedEvent;
/*     */ import org.eclipse.jface.viewers.Viewer;
/*     */ import org.eclipse.jface.viewers.ViewerComparator;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.layout.FormAttachment;
/*     */ import org.eclipse.swt.layout.FormData;
/*     */ import org.eclipse.swt.layout.FormLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class RetrieveActivityDialog extends CaveJFACEDialog
/*     */ {
/*  80 */   private static final transient IUFStatusHandler statusHandler = UFStatus.getHandler(RetrieveActivityDialog.class);
/*     */ 
/* 110 */   private String title = null;
/*     */   private Shell shell;
/* 114 */   private Button sortByNameBtn = null;
/*     */ 
/* 116 */   private Button sortByDateBtn = null;
/*     */ 
/* 118 */   private org.eclipse.swt.widgets.List dirList = null;
/*     */ 
/* 120 */   private ListViewer fileListViewer = null;
/*     */ 
/* 122 */   private Button browseBtn = null;
/*     */ 
/* 124 */   private Button autoSaveOffBtn = null;
/*     */ 
/* 126 */   private Button autoSaveOnBtn = null;
/*     */   private static final int ADD_ID = 8611;
/*     */   private static final String ADD_LABEL = "Add";
/*     */   private static final int REPLACE_ID = 8610;
/*     */   private static final String REPLACE_LABEL = "Replace";
/*     */   private static final int ADVANCE_ID = 8612;
/*     */   private static final String ADVANCE_LABEL = "Advanced";
/*     */   private static final int CLOSE_ID = 8614;
/*     */   private static final String CLOSE_LABEL = "Close";
/* 144 */   private Button replaceBtn = null;
/*     */ 
/* 146 */   private Button addBtn = null;
/*     */ 
/* 148 */   private Button appendBtn = null;
/*     */ 
/* 150 */   private Button cancelBtn = null;
/*     */   private Map<String, java.util.List<ActivityElement>> activityMap;
/* 154 */   private static String selectedDir = null;
/*     */ 
/* 156 */   private static String fullName = null;
/*     */ 
/*     */   public RetrieveActivityDialog(Shell parShell, String btnName)
/*     */     throws VizException
/*     */   {
/* 164 */     super(parShell);
/*     */ 
/* 166 */     setTitle(btnName);
/*     */   }
/*     */ 
/*     */   private void setTitle(String btnName)
/*     */   {
/* 175 */     if (btnName.equals("Open"))
/* 176 */       this.title = "Rretrive a PGEN Activity";
/*     */   }
/*     */ 
/*     */   protected void configureShell(Shell shell)
/*     */   {
/* 190 */     super.configureShell(shell);
/* 191 */     setShellStyle(32784);
/*     */ 
/* 193 */     this.shell = shell;
/* 194 */     if (this.title != null)
/* 195 */       shell.setText(this.title);
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 207 */     Composite dlgAreaForm = (Composite)super.createDialogArea(parent);
/*     */ 
/* 209 */     Composite topForm = new Composite(dlgAreaForm, 0);
/* 210 */     topForm.setLayout(new FormLayout());
/*     */ 
/* 215 */     Composite sortForm = new Composite(topForm, 0);
/* 216 */     sortForm.setLayout(new FormLayout());
/*     */ 
/* 218 */     this.sortByNameBtn = new Button(sortForm, 16);
/* 219 */     this.sortByNameBtn.setText("Sort Alphabetically");
/*     */ 
/* 221 */     FormData layoutData1 = new FormData(370, 25);
/* 222 */     layoutData1.top = new FormAttachment(0, 0);
/* 223 */     layoutData1.left = new FormAttachment(0, 0);
/*     */ 
/* 225 */     this.sortByNameBtn.setLayoutData(layoutData1);
/*     */ 
/* 227 */     this.sortByNameBtn.setSelection(true);
/*     */ 
/* 233 */     this.sortByNameBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent ev) {
/* 235 */         RetrieveActivityDialog.this.fileListViewer.setComparator(new ViewerComparator());
/* 236 */         RetrieveActivityDialog.this.fileListViewer.refresh(true);
/*     */       }
/*     */     });
/* 241 */     this.sortByDateBtn = new Button(sortForm, 16);
/* 242 */     this.sortByDateBtn.setText("Sort By Date");
/*     */ 
/* 244 */     FormData layoutData3 = new FormData();
/* 245 */     layoutData3.top = new FormAttachment(this.sortByNameBtn, 5, 1024);
/* 246 */     layoutData3.left = new FormAttachment(this.sortByNameBtn, 0, 16384);
/* 247 */     this.sortByDateBtn.setLayoutData(layoutData3);
/*     */ 
/* 253 */     this.sortByDateBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent ev) {
/* 255 */         RetrieveActivityDialog.this.fileListViewer.setComparator(new RetrieveActivityDialog.ActivityTimeComparator(RetrieveActivityDialog.this));
/* 256 */         RetrieveActivityDialog.this.fileListViewer.refresh(true);
/*     */       }
/*     */     });
/* 264 */     Label dirLbl = new Label(topForm, 0);
/* 265 */     dirLbl.setText("Select Activity Type:");
/*     */ 
/* 267 */     FormData layoutData5 = new FormData();
/* 268 */     layoutData5.top = new FormAttachment(sortForm, 15, 1024);
/* 269 */     layoutData5.left = new FormAttachment(sortForm, 0, 16384);
/*     */ 
/* 271 */     dirLbl.setLayoutData(layoutData5);
/*     */ 
/* 273 */     this.dirList = new org.eclipse.swt.widgets.List(topForm, 2564);
/*     */ 
/* 276 */     this.activityMap = getActivityMap();
/* 277 */     for (String str : this.activityMap.keySet()) {
/* 278 */       this.dirList.add(str);
/*     */     }
/*     */ 
/* 281 */     FormData layoutData6 = new FormData(350, 200);
/* 282 */     layoutData6.top = new FormAttachment(dirLbl, 5, 1024);
/* 283 */     layoutData6.left = new FormAttachment(dirLbl, 0, 16384);
/* 284 */     this.dirList.setLayoutData(layoutData6);
/*     */ 
/* 286 */     this.dirList.addListener(13, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 288 */         if (RetrieveActivityDialog.this.dirList.getSelectionCount() > 0)
/*     */         {
/* 290 */           RetrieveActivityDialog.selectedDir = RetrieveActivityDialog.this.dirList.getSelection()[0];
/* 291 */           RetrieveActivityDialog.this.fileListViewer.setInput(RetrieveActivityDialog.this.activityMap.get(RetrieveActivityDialog.selectedDir));
/* 292 */           RetrieveActivityDialog.this.fileListViewer.getList().setToolTipText(null);
/* 293 */           RetrieveActivityDialog.this.fileListViewer.refresh();
/*     */ 
/* 296 */           RetrieveActivityDialog.fullName = null;
/*     */         }
/*     */       }
/*     */     });
/* 304 */     Label fileLbl = new Label(topForm, 0);
/* 305 */     fileLbl.setText("Select an Activity Label:");
/*     */ 
/* 307 */     FormData layoutData8 = new FormData();
/* 308 */     layoutData8.top = new FormAttachment(this.dirList, 20, 1024);
/* 309 */     layoutData8.left = new FormAttachment(this.dirList, 0, 16384);
/*     */ 
/* 311 */     fileLbl.setLayoutData(layoutData8);
/*     */ 
/* 313 */     this.fileListViewer = new ListViewer(topForm, 2564);
/*     */ 
/* 316 */     FormData layoutData9 = new FormData(350, 200);
/* 317 */     layoutData9.top = new FormAttachment(fileLbl, 5, 1024);
/* 318 */     layoutData9.left = new FormAttachment(fileLbl, 0, 16384);
/*     */ 
/* 320 */     this.fileListViewer.getList().setLayoutData(layoutData9);
/*     */ 
/* 322 */     this.fileListViewer.setContentProvider(ArrayContentProvider.getInstance());
/* 323 */     if (this.sortByNameBtn.getSelection())
/* 324 */       this.fileListViewer.setComparator(new ViewerComparator());
/*     */     else {
/* 326 */       this.fileListViewer.setComparator(new ActivityTimeComparator());
/*     */     }
/*     */ 
/* 329 */     this.fileListViewer.setLabelProvider(new LabelProvider()
/*     */     {
/*     */       public String getText(Object element)
/*     */       {
/* 333 */         if ((element instanceof RetrieveActivityDialog.ActivityElement)) {
/* 334 */           return ((RetrieveActivityDialog.ActivityElement)element).activityLabel;
/*     */         }
/* 336 */         return super.getText(element);
/*     */       }
/*     */     });
/* 341 */     this.fileListViewer
/* 342 */       .addSelectionChangedListener(new ISelectionChangedListener() {
/*     */       public void selectionChanged(SelectionChangedEvent event) {
/* 344 */         IStructuredSelection selection = (IStructuredSelection)event
/* 345 */           .getSelection();
/* 346 */         if ((selection.getFirstElement() instanceof RetrieveActivityDialog.ActivityElement)) {
/* 347 */           RetrieveActivityDialog.ActivityElement elem = (RetrieveActivityDialog.ActivityElement)selection
/* 348 */             .getFirstElement();
/*     */ 
/* 353 */           RetrieveActivityDialog.this.fileListViewer.getList().setToolTipText(
/* 354 */             elem.dataURI);
/*     */         } else {
/* 356 */           System.out.println("GOT??? " + 
/* 357 */             selection.getFirstElement().getClass()
/* 358 */             .getCanonicalName());
/*     */         }
/*     */       }
/*     */     });
/* 366 */     this.browseBtn = new Button(topForm, 8);
/*     */ 
/* 368 */     FormData layoutData10 = new FormData(355, 25);
/* 369 */     layoutData10.top = new FormAttachment(this.fileListViewer.getList(), 20, 
/* 370 */       1024);
/* 371 */     layoutData10.left = new FormAttachment(this.fileListViewer.getList(), 0, 
/* 372 */       16384);
/*     */ 
/* 374 */     this.browseBtn.setLayoutData(layoutData10);
/* 375 */     this.browseBtn.setSize(330, 20);
/* 376 */     this.browseBtn.setText("Browse");
/* 377 */     this.browseBtn.setEnabled(false);
/*     */ 
/* 382 */     Label autoSaveLbl = new Label(topForm, 0);
/* 383 */     autoSaveLbl.setText("Auto Save:");
/*     */ 
/* 385 */     FormData layoutData11 = new FormData();
/* 386 */     layoutData11.top = new FormAttachment(this.browseBtn, 20, 1024);
/* 387 */     layoutData11.left = new FormAttachment(this.browseBtn, 0, 16384);
/* 388 */     autoSaveLbl.setLayoutData(layoutData11);
/*     */ 
/* 390 */     this.autoSaveOffBtn = new Button(topForm, 16);
/* 391 */     this.autoSaveOffBtn.setText("Off");
/* 392 */     this.autoSaveOffBtn.setSelection(true);
/*     */ 
/* 394 */     FormData layoutData12 = new FormData();
/* 395 */     layoutData12.top = new FormAttachment(autoSaveLbl, 0, 128);
/* 396 */     layoutData12.left = new FormAttachment(autoSaveLbl, 10, 131072);
/* 397 */     this.autoSaveOffBtn.setLayoutData(layoutData12);
/*     */ 
/* 399 */     this.autoSaveOnBtn = new Button(topForm, 16);
/* 400 */     this.autoSaveOnBtn.setText("On");
/*     */ 
/* 402 */     FormData layoutData13 = new FormData();
/* 403 */     layoutData13.top = new FormAttachment(this.autoSaveOffBtn, 0, 128);
/* 404 */     layoutData13.left = new FormAttachment(this.autoSaveOffBtn, 10, 131072);
/* 405 */     this.autoSaveOnBtn.setLayoutData(layoutData13);
/*     */ 
/* 407 */     return dlgAreaForm;
/*     */   }
/*     */ 
/*     */   private Map<String, java.util.List<ActivityElement>> getActivityMap()
/*     */   {
/* 412 */     Map activityMap = new HashMap();
/*     */ 
/* 414 */     DbQueryRequest request = new DbQueryRequest();
/* 415 */     request.setEntityClass(PgenRecord.class.getName());
/* 416 */     request.addRequestField("activityType");
/* 417 */     request.addRequestField("activityLabel");
/* 418 */     request.addRequestField("dataURI");
/* 419 */     request.addRequestField("dataTime.refTime");
/* 420 */     request.setOrderByField("activityType");
/*     */     try
/*     */     {
/* 424 */       DbQueryResponse response = (DbQueryResponse)ThriftClient.sendRequest(request);
/* 425 */       for (Map result : response.getResults()) {
/* 426 */         ActivityElement elem = new ActivityElement();
/* 427 */         elem.activityType = 
/* 428 */           ((String)result
/* 428 */           .get("activityType"));
/* 429 */         elem.activityLabel = 
/* 430 */           ((String)result
/* 430 */           .get("activityLabel"));
/* 431 */         elem.dataURI = ((String)result.get("dataURI"));
/* 432 */         elem.refTime = ((Date)result.get("dataTime.refTime"));
/*     */ 
/* 434 */         if (activityMap.containsKey(elem.activityType))
/*     */         {
/* 436 */           ((java.util.List)activityMap
/* 436 */             .get(elem.activityType)).add(elem);
/*     */         } else {
/* 438 */           java.util.List elist = new ArrayList();
/* 439 */           elist.add(elem);
/* 440 */           activityMap.put(elem.activityType, elist);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (VizException e) {
/* 445 */       statusHandler.handle(UFStatus.Priority.PROBLEM, e.getLocalizedMessage(), e);
/*     */     }
/* 447 */     return activityMap;
/*     */   }
/*     */ 
/*     */   protected void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 457 */     this.addBtn = createButton(parent, 8611, "Add", true);
/* 458 */     this.replaceBtn = createButton(parent, 8610, "Replace", true);
/* 459 */     this.appendBtn = createButton(parent, 8612, "Advanced", true);
/*     */ 
/* 461 */     this.replaceBtn.addListener(4, new Listener() {
/*     */       public void handleEvent(Event event) {
/* 463 */         RetrieveActivityDialog.this.openProducts(true);
/*     */       }
/*     */     });
/* 467 */     this.addBtn.addListener(4, new Listener() {
/*     */       public void handleEvent(Event event) {
/* 469 */         RetrieveActivityDialog.this.openProducts(false);
/*     */       }
/*     */     });
/* 473 */     this.appendBtn.addListener(4, new Listener() {
/*     */       public void handleEvent(Event event) {
/* 475 */         RetrieveActivityDialog.this.appendProducts();
/*     */       }
/*     */     });
/* 479 */     this.cancelBtn = createButton(parent, 8614, "Close", true);
/* 480 */     this.cancelBtn.addListener(4, new Listener() {
/*     */       public void handleEvent(Event event) {
/* 482 */         RetrieveActivityDialog.this.close();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void openProducts(boolean replace)
/*     */   {
/* 494 */     ActivityElement elem = getActivitySelection();
/* 495 */     if (elem == null) {
/* 496 */       return;
/*     */     }
/* 498 */     fullName = elem.activityLabel;
/*     */ 
/* 500 */     java.util.List pgenProds = null;
/*     */     try {
/* 502 */       pgenProds = StorageUtils.retrieveProduct(elem.dataURI);
/*     */     } catch (PgenStorageException e) {
/* 504 */       StorageUtils.showError(e);
/*     */     }
/*     */ 
/* 511 */     if (VaaInfo.isNoneDrawableTxt(pgenProds)) {
/* 512 */       VaaInfo.openMsgDlg("THIS IS A TEXT PRODUCT THAT CANNOT BE DISPLAYED!");
/* 513 */       return;
/*     */     }
/*     */ 
/* 519 */     PgenResource pgen = PgenSession.getInstance().getPgenResource();
/*     */ 
/* 527 */     if (replace) {
/* 528 */       MessageDialog confirmOpen = new MessageDialog(
/* 529 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 530 */         "Confirm File Replace", null, "Replace Activity <" + 
/* 531 */         pgen.getActiveProduct().getType() + 
/* 532 */         "> with New Activity <" + 
/* 533 */         ((Product)pgenProds.get(0)).getType() + "> ?", 
/* 534 */         2, new String[] { "Yes", "No" }, 0);
/*     */ 
/* 536 */       confirmOpen.open();
/*     */ 
/* 538 */       if (confirmOpen.getReturnCode() != 0) {
/* 539 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 544 */     pgen.setAutosave(this.autoSaveOnBtn.getSelection());
/* 545 */     if (fullName.endsWith(".lpf"))
/* 546 */       pgen.setAutoSaveFilename(fullName.replace(".lpf", "xml"));
/*     */     else {
/* 548 */       pgen.setAutoSaveFilename(fullName);
/*     */     }
/*     */ 
/* 551 */     ((Product)pgenProds.get(0)).setInputFile(fullName);
/*     */ 
/* 553 */     setJetTool(pgenProds);
/*     */ 
/* 555 */     close();
/*     */ 
/* 560 */     if (replace)
/*     */     {
/* 562 */       for (Product pp : pgenProds) {
/* 563 */         pp.setOutputFile(null);
/*     */       }
/*     */ 
/* 566 */       PgenFileNameDisplay.getInstance().setFileName(fullName);
/*     */ 
/* 568 */       pgen.replaceProduct(pgenProds);
/*     */     } else {
/* 570 */       if (pgen.getActiveProduct() == null) {
/* 571 */         PgenFileNameDisplay.getInstance().setFileName(fullName);
/*     */       }
/*     */ 
/* 574 */       pgen.addProduct(pgenProds);
/*     */     }
/*     */ 
/* 577 */     PgenUtil.refresh();
/*     */   }
/*     */ 
/*     */   private void appendProducts()
/*     */   {
/* 586 */     ActivityElement elem = getActivitySelection();
/* 587 */     if (elem == null) {
/* 588 */       return;
/*     */     }
/* 590 */     fullName = elem.activityLabel;
/*     */ 
/* 592 */     java.util.List pgenProds = null;
/*     */     try {
/* 594 */       pgenProds = StorageUtils.retrieveProduct(elem.dataURI);
/*     */     } catch (PgenStorageException e) {
/* 596 */       StorageUtils.showError(e);
/*     */     }
/*     */ 
/* 603 */     if (VaaInfo.isNoneDrawableTxt(pgenProds)) {
/* 604 */       VaaInfo.openMsgDlg("THIS IS A TEXT PRODUCT THAT CANNOT BE DISPLAYED!");
/* 605 */       return;
/*     */     }
/*     */ 
/* 608 */     PgenResource pgen = PgenSession.getInstance().getPgenResource();
/*     */ 
/* 610 */     PgenLayerMergeDialog layerMergeDlg = null;
/*     */     try {
/* 612 */       layerMergeDlg = new PgenLayerMergeDialog(this.shell, (Product)pgenProds.get(0), 
/* 613 */         fullName);
/*     */     } catch (Exception e) {
/* 615 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 618 */     if (layerMergeDlg != null)
/*     */     {
/* 620 */       layerMergeDlg.open();
/* 621 */       if (layerMergeDlg.getReturnCode() == 0)
/*     */       {
/* 623 */         pgen.setAutosave(this.autoSaveOnBtn.getSelection());
/* 624 */         if (fullName.endsWith(".lpf"))
/* 625 */           pgen.setAutoSaveFilename(fullName.replace(".lpf", "xml"));
/*     */         else {
/* 627 */           pgen.setAutoSaveFilename(fullName);
/*     */         }
/*     */ 
/* 630 */         setJetTool(pgenProds);
/*     */ 
/* 632 */         close();
/*     */ 
/* 634 */         ((PgenResourceData)pgen.getResourceData()).startProductManage();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private ActivityElement getActivitySelection()
/*     */   {
/* 641 */     ActivityElement elem = null;
/*     */ 
/* 643 */     if (!this.fileListViewer.getSelection().isEmpty()) {
/* 644 */       IStructuredSelection sel = (IStructuredSelection)this.fileListViewer
/* 645 */         .getSelection();
/* 646 */       elem = (ActivityElement)sel.getFirstElement();
/*     */     }
/*     */     else
/*     */     {
/* 652 */       MessageDialog confirmDlg = new MessageDialog(
/* 653 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 654 */         "Invalid PGEN Selection", null, 
/* 655 */         "Please select an Activity from the Activity Label list.", 
/* 656 */         2, new String[] { "OK" }, 0);
/*     */ 
/* 658 */       confirmDlg.open();
/*     */ 
/* 660 */       return null;
/*     */     }
/*     */ 
/* 663 */     return elem;
/*     */   }
/*     */ 
/*     */   private void setJetTool(java.util.List<Product> prods)
/*     */   {
/* 674 */     PgenSnapJet st = new PgenSnapJet(
/* 675 */       (IMapDescriptor)PgenSession.getInstance()
/* 675 */       .getPgenResource().getDescriptor(), PgenUtil.getActiveEditor(), 
/* 676 */       null);
/*     */     Iterator localIterator2;
/* 678 */     for (Iterator localIterator1 = prods.iterator(); localIterator1.hasNext(); 
/* 679 */       localIterator2.hasNext())
/*     */     {
/* 678 */       Product prod = (Product)localIterator1.next();
/* 679 */       localIterator2 = prod.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/*     */ 
/* 681 */       Iterator iterator = layer
/* 682 */         .getComponentIterator();
/* 683 */       while (iterator.hasNext()) {
/* 684 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)iterator.next();
/* 685 */         if ((adc instanceof Jet))
/* 686 */           ((Jet)adc).setSnapTool(st);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class ActivityElement
/*     */   {
/*     */     String dataURI;
/*     */     String activityType;
/*     */     String activityLabel;
/*     */     Date refTime;
/*     */ 
/*     */     ActivityElement()
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   class ActivityTimeComparator extends ViewerComparator
/*     */   {
/*     */     ActivityTimeComparator()
/*     */     {
/*     */     }
/*     */ 
/*     */     public int compare(Viewer viewer, Object e1, Object e2)
/*     */     {
/* 102 */       Date elem1 = ((RetrieveActivityDialog.ActivityElement)e1).refTime;
/* 103 */       Date elem2 = ((RetrieveActivityDialog.ActivityElement)e2).refTime;
/* 104 */       return -1 * elem1.compareTo(elem2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.RetrieveActivityDialog
 * JD-Core Version:    0.6.2
 */