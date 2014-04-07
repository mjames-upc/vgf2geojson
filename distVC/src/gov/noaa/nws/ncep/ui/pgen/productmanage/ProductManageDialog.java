/*      */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.GfaAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.OutlookAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenFileManageDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*      */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenLayer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenSave;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import java.io.File;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.swt.events.SelectionAdapter;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.graphics.Point;
/*      */ import org.eclipse.swt.graphics.Rectangle;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Listener;
/*      */ import org.eclipse.swt.widgets.Menu;
/*      */ import org.eclipse.swt.widgets.MenuItem;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ import org.eclipse.swt.widgets.ToolBar;
/*      */ import org.eclipse.swt.widgets.ToolItem;
/*      */ import org.eclipse.swt.widgets.Widget;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class ProductManageDialog extends ProductDialog
/*      */ {
/*   87 */   private ArrayList<Product> prdList = null;
/*      */ 
/*   89 */   private ArrayList<Text> prdTypeTexts = null;
/*   90 */   private ArrayList<Button> prdNameBtns = null;
/*   91 */   private ArrayList<Button> prdDispOnOffBtns = null;
/*      */ 
/*   94 */   protected ProductNameDialog prdNameDlg = null;
/*   95 */   protected ProductFileNameDialog prdFileInOutDlg = null;
/*      */ 
/*   97 */   private Button prodAllOnOffBtn = null;
/*   98 */   private boolean prodAllOnOff = false;
/*      */ 
/*  100 */   private int prdInUse = -1;
/*  101 */   boolean openPrdNameDialog = false;
/*      */ 
/*  104 */   protected LinkedHashMap<String, ProductType> prdTypesMap = null;
/*      */ 
/*  110 */   private final java.awt.Color defaultButtonColor = java.awt.Color.lightGray;
/*  111 */   private final java.awt.Color activeButtonColor = java.awt.Color.green;
/*      */ 
/*  116 */   protected LayeringNameDialog layerNameDlg = null;
/*  117 */   protected LayeringDisplayDialog displayDlg = null;
/*  118 */   protected LayeringLpfFileDialog layerLpfFileDlg = null;
/*      */ 
/*  124 */   private ArrayList<Layer> layerList = null;
/*  125 */   private Composite layersComp = null;
/*  126 */   private ArrayList<Button> layerNameBtns = null;
/*  127 */   private ArrayList<Button> displayOnOffBtns = null;
/*  128 */   private ArrayList<Button> colorModeBtns = null;
/*      */ 
/*  131 */   private Button allOnOffBtn = null;
/*      */ 
/*  136 */   private int layerInUse = -1;
/*  137 */   private int colorModeBtnInUse = -1;
/*  138 */   private boolean allOnOff = false;
/*      */ 
/*  144 */   private Button arrowBtn = null;
/*  145 */   boolean compact = true;
/*  146 */   boolean openLayerNameDialog = false;
/*      */ 
/*      */   public ProductManageDialog(Shell parentShell)
/*      */   {
/*  152 */     super(parentShell);
/*      */   }
/*      */ 
/*      */   public void setTitle()
/*      */   {
/*  160 */     this.shell.setText("Product Center");
/*      */   }
/*      */ 
/*      */   public void setDefaultLocation(Shell parent)
/*      */   {
/*  170 */     if (this.shellLocation == null) {
/*  171 */       Point pt = parent.getLocation();
/*  172 */       this.shell.setLocation(pt.x + 255, pt.y + 146);
/*      */     }
/*      */     else {
/*  175 */       this.shell.setLocation(this.shellLocation);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void popupSecondDialog()
/*      */   {
/*  185 */     if (this.openPrdNameDialog) {
/*  186 */       editProductAttr();
/*      */     }
/*  188 */     else if (this.openLayerNameDialog)
/*  189 */       editLayerName();
/*      */   }
/*      */ 
/*      */   public void initializeComponents()
/*      */   {
/*  201 */     initialize();
/*      */ 
/*  204 */     this.prdTypesMap = ProductConfigureDialog.getProductTypes();
/*      */ 
/*  207 */     createProductPart();
/*  208 */     addSeparator();
/*  209 */     addSeparator();
/*      */ 
/*  212 */     createLayeringPart();
/*  213 */     addSeparator();
/*  214 */     addSeparator();
/*      */ 
/*  217 */     createExitPart();
/*      */ 
/*  220 */     resetPalette(this.currentProduct);
/*      */ 
/*  223 */     AttrSettings.getInstance().loadProdSettings(this.currentProduct.getType());
/*      */   }
/*      */ 
/*      */   private void initialize()
/*      */   {
/*  232 */     this.prdList = ((ArrayList)this.drawingLayer.getProducts());
/*      */ 
/*  234 */     if (this.currentProduct == null)
/*      */     {
/*  236 */       this.currentProduct = new Product("Default", "Default", "Default", 
/*  237 */         new ProductInfo(), new ProductTime(), new ArrayList());
/*      */ 
/*  239 */       this.currentProduct.setOnOff(false);
/*      */ 
/*  241 */       this.drawingLayer.addProduct(this.currentProduct);
/*  242 */       this.drawingLayer.setActiveProduct(this.currentProduct);
/*      */     }
/*  245 */     else if (this.prdList.contains(this.currentProduct)) {
/*  246 */       this.prdInUse = this.prdList.indexOf(this.currentProduct);
/*      */     } else {
/*  248 */       this.prdInUse = -1;
/*      */     }
/*      */ 
/*  251 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*      */ 
/*  253 */     if (this.currentLayer == null)
/*      */     {
/*  255 */       this.currentLayer = new Layer();
/*      */ 
/*  257 */       this.currentProduct.addLayer(this.currentLayer);
/*      */ 
/*  259 */       this.currentLayer.setOnOff(false);
/*      */ 
/*  261 */       this.drawingLayer.setActiveLayer(this.currentLayer);
/*      */     }
/*  264 */     else if (this.layerList.contains(this.currentLayer)) {
/*  265 */       this.layerInUse = this.layerList.indexOf(this.currentLayer);
/*      */     } else {
/*  267 */       this.layerInUse = -1;
/*      */     }
/*      */ 
/*  270 */     this.currentLayer.setInUse(true);
/*      */ 
/*  272 */     this.prdNameBtns = new ArrayList();
/*  273 */     this.prdDispOnOffBtns = new ArrayList();
/*  274 */     this.prdTypeTexts = new ArrayList();
/*      */ 
/*  278 */     this.layerNameBtns = new ArrayList();
/*  279 */     this.displayOnOffBtns = new ArrayList();
/*  280 */     this.colorModeBtns = new ArrayList();
/*      */   }
/*      */ 
/*      */   private void createProductPart()
/*      */   {
/*  291 */     Composite titleComp = new Composite(this.shell, 0);
/*      */     GridLayout gl0;
/*      */     GridLayout gl0;
/*  294 */     if (this.compact) {
/*  295 */       gl0 = new GridLayout(1, true);
/*      */     }
/*      */     else {
/*  298 */       gl0 = new GridLayout(2, true);
/*      */     }
/*      */ 
/*  301 */     gl0.makeColumnsEqualWidth = false;
/*      */ 
/*  303 */     titleComp.setLayout(gl0);
/*      */ 
/*  305 */     Label prds = new Label(titleComp, 0);
/*  306 */     prds.setText("Products:");
/*      */ 
/*  323 */     addSeparator();
/*      */ 
/*  326 */     Composite addProdComp = new Composite(this.shell, 0);
/*      */     int numActionBtns;
/*      */     int numActionBtns;
/*  329 */     if (this.compact) {
/*  330 */       numActionBtns = 2;
/*      */     }
/*      */     else {
/*  333 */       numActionBtns = 3;
/*      */     }
/*      */ 
/*  336 */     GridLayout gl = new GridLayout(numActionBtns, true);
/*      */ 
/*  338 */     gl.makeColumnsEqualWidth = false;
/*  339 */     gl.marginHeight = 1;
/*  340 */     gl.marginWidth = 1;
/*  341 */     gl.verticalSpacing = 1;
/*  342 */     gl.horizontalSpacing = 1;
/*  343 */     addProdComp.setLayout(gl);
/*      */ 
/*  345 */     Button addPrdBtn = new Button(addProdComp, 0);
/*  346 */     addPrdBtn.setText("New");
/*  347 */     addPrdBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  349 */         ProductManageDialog.this.addProduct();
/*      */       }
/*      */     });
/*  353 */     this.prodAllOnOffBtn = new Button(addProdComp, 0);
/*  354 */     this.prodAllOnOffBtn.setText("All On");
/*  355 */     this.prodAllOnOffBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  357 */         ProductManageDialog.this.updateProdDispChecks();
/*      */       }
/*      */     });
/*  362 */     if (!this.compact)
/*      */     {
/*  364 */       Button delPrdBtn = new Button(addProdComp, 0);
/*  365 */       delPrdBtn.setText("Delete");
/*  366 */       delPrdBtn.addSelectionListener(new SelectionAdapter() {
/*      */         public void widgetSelected(SelectionEvent event) {
/*  368 */           ProductManageDialog.this.deleteProduct();
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*  373 */     addSeparator();
/*      */ 
/*  376 */     createProducts();
/*      */   }
/*      */ 
/*      */   private void addSeparator()
/*      */   {
/*  384 */     GridData gd = new GridData(768);
/*  385 */     Label sepLbl = new Label(this.shell, 258);
/*  386 */     sepLbl.setLayoutData(gd);
/*      */   }
/*      */ 
/*      */   private void createProducts()
/*      */   {
/*  394 */     Composite prdsComp = new Composite(this.shell, 0);
/*      */     int numActionBtns;
/*      */     int numActionBtns;
/*  397 */     if (this.compact) {
/*  398 */       numActionBtns = 2;
/*      */     }
/*      */     else {
/*  401 */       numActionBtns = 3;
/*      */     }
/*      */ 
/*  404 */     GridLayout gl = new GridLayout(numActionBtns, true);
/*  405 */     gl.makeColumnsEqualWidth = false;
/*  406 */     gl.marginHeight = 1;
/*  407 */     gl.marginWidth = 1;
/*  408 */     gl.verticalSpacing = 1;
/*  409 */     gl.horizontalSpacing = 1;
/*      */ 
/*  411 */     prdsComp.setLayout(gl);
/*      */ 
/*  413 */     if ((this.prdInUse < 0) || (this.prdInUse >= this.prdList.size())) {
/*  414 */       this.prdInUse = 0;
/*  415 */       this.layerInUse = 0;
/*  416 */       this.currentProduct = ((Product)this.prdList.get(this.prdInUse));
/*  417 */       this.currentProduct.setInUse(true);
/*      */ 
/*  419 */       this.drawingLayer.setActiveProduct(this.currentProduct);
/*      */ 
/*  421 */       this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*  422 */       this.currentLayer = this.currentProduct.getLayer(this.layerInUse);
/*  423 */       this.currentLayer.setInUse(true);
/*      */ 
/*  425 */       this.drawingLayer.setActiveLayer(this.currentLayer);
/*      */     }
/*      */ 
/*  429 */     int ii = 0;
/*  430 */     for (Product prd : this.prdList)
/*      */     {
/*  433 */       ProductType ptyp = (ProductType)this.prdTypesMap.get(prd.getType());
/*  434 */       if ((ptyp != null) && (ptyp.getPgenSave() != null)) {
/*  435 */         prd.setSaveLayers(ptyp.getPgenSave().isSaveLayers().booleanValue());
/*      */       }
/*      */ 
/*  438 */       Button nameBtn = new Button(prdsComp, 8);
/*  439 */       nameBtn.setText(prd.getName());
/*      */ 
/*  441 */       if (ii == this.prdInUse) {
/*  442 */         setButtonColor(nameBtn, this.activeButtonColor);
/*      */       }
/*      */       else {
/*  445 */         setButtonColor(nameBtn, this.defaultButtonColor);
/*      */       }
/*      */ 
/*  448 */       nameBtn.setData(Integer.valueOf(ii));
/*  449 */       nameBtn.addSelectionListener(new SelectionAdapter()
/*      */       {
/*      */         public void widgetSelected(SelectionEvent event) {
/*  452 */           int iprd = Integer.parseInt(event.widget.getData().toString());
/*      */ 
/*  454 */           if (ProductManageDialog.this.prdInUse == iprd)
/*      */           {
/*  456 */             ProductManageDialog.this.openPrdNameDialog = true;
/*  457 */             ProductManageDialog.this.openLayerNameDialog = false;
/*      */ 
/*  459 */             if (ProductManageDialog.this.layerNameDlg != null) ProductManageDialog.this.layerNameDlg.close();
/*      */ 
/*  461 */             ProductManageDialog.this.editProductAttr();
/*      */           }
/*      */           else
/*      */           {
/*  465 */             ProductManageDialog.this.switchProduct(Integer.parseInt(event.widget.getData().toString()));
/*      */           }
/*      */         }
/*      */       });
/*  472 */       this.prdNameBtns.add(nameBtn);
/*      */ 
/*  474 */       Button dispBtn = new Button(prdsComp, 32);
/*  475 */       dispBtn.setSelection(prd.isOnOff());
/*  476 */       dispBtn.setData(Integer.valueOf(ii));
/*  477 */       dispBtn.addSelectionListener(new SelectionAdapter() {
/*      */         public void widgetSelected(SelectionEvent event) {
/*  479 */           ProductManageDialog.this.turnOnProduct(Integer.parseInt(event.widget.getData().toString()));
/*      */         }
/*      */       });
/*  483 */       this.prdDispOnOffBtns.add(dispBtn);
/*      */ 
/*  485 */       if (!this.compact)
/*      */       {
/*  487 */         Composite typeComp = new Composite(prdsComp, 131072);
/*  488 */         typeComp.setLayout(new GridLayout(2, false));
/*      */ 
/*  490 */         final Text typeText = new Text(typeComp, 18432);
/*  491 */         typeText.setSize(200, 20);
/*  492 */         typeText.setText(((Product)this.prdList.get(ii)).getType());
/*  493 */         typeText.setData(Integer.valueOf(ii));
/*  494 */         typeText.setEditable(false);
/*  495 */         this.prdTypeTexts.add(typeText);
/*      */ 
/*  497 */         final ToolBar tb = new ToolBar(typeComp, 256);
/*  498 */         final ToolItem ti = new ToolItem(tb, 4);
/*      */ 
/*  500 */         ti.setEnabled(true);
/*      */ 
/*  502 */         final Menu mu = new Menu(this.shell.getShell(), 8);
/*      */ 
/*  504 */         MenuItem mi1 = new MenuItem(mu, 8, 0);
/*  505 */         mi1.setText("Default");
/*  506 */         mi1.setData("Default");
/*  507 */         mi1.addSelectionListener(new SelectionAdapter() {
/*      */           public void widgetSelected(SelectionEvent e) {
/*  509 */             typeText.setText(((MenuItem)e.widget).getData().toString());
/*  510 */             typeText.pack();
/*  511 */             ProductManageDialog.this.shell.pack();
/*  512 */             ProductManageDialog.this.switchProductType(typeText);
/*      */           }
/*      */         });
/*  516 */         int ntyp = 1;
/*  517 */         ArrayList typeUsed = new ArrayList();
/*  518 */         for (String ptypName : this.prdTypesMap.keySet())
/*      */         {
/*  520 */           ProductType prdType = (ProductType)this.prdTypesMap.get(ptypName);
/*  521 */           LinkedHashMap subtypesNalias = getSubtypes(prdType.getType(), true);
/*      */ 
/*  523 */           if (((ptypName.equals(prdType.getName())) && 
/*  524 */             (!prdType.getType().equals(prdType.getName()))) || 
/*  525 */             (!hasSubtypes(subtypesNalias.values())))
/*      */           {
/*  527 */             MenuItem typeItem = new MenuItem(mu, 8, ntyp);
/*      */ 
/*  529 */             typeItem.setText(ptypName);
/*  530 */             typeItem.setData(ptypName);
/*  531 */             typeItem.addSelectionListener(new SelectionAdapter() {
/*      */               public void widgetSelected(SelectionEvent e) {
/*  533 */                 String typeName = ((MenuItem)e.widget).getData().toString();
/*  534 */                 typeText.setText(typeName);
/*  535 */                 typeText.pack();
/*  536 */                 ProductManageDialog.this.shell.pack();
/*  537 */                 ProductManageDialog.this.switchProductType(typeText);
/*      */               }
/*      */ 
/*      */             });
/*      */           }
/*      */           else
/*      */           {
/*  544 */             if (typeUsed.contains(prdType.getType()))
/*      */             {
/*      */               continue;
/*      */             }
/*  548 */             typeUsed.add(prdType.getType());
/*      */ 
/*  552 */             MenuItem typeItem = new MenuItem(mu, 64, ntyp);
/*      */ 
/*  554 */             typeItem.setText(prdType.getType());
/*  555 */             Menu submenu = new Menu(typeItem);
/*  556 */             typeItem.setMenu(submenu);
/*      */ 
/*  558 */             for (String styp : subtypesNalias.keySet()) {
/*  559 */               MenuItem subtypeItem = new MenuItem(submenu, 8);
/*  560 */               subtypeItem.setText((String)subtypesNalias.get(styp));
/*      */ 
/*  562 */               subtypeItem.setData(styp);
/*      */ 
/*  564 */               subtypeItem.addSelectionListener(new SelectionAdapter() {
/*      */                 public void widgetSelected(SelectionEvent e) {
/*  566 */                   String typeName = ((MenuItem)e.widget).getData().toString();
/*  567 */                   typeText.setText(typeName);
/*  568 */                   typeText.pack();
/*  569 */                   ProductManageDialog.this.shell.pack();
/*  570 */                   ProductManageDialog.this.switchProductType(typeText);
/*      */                 }
/*      */               });
/*      */             }
/*      */           }
/*      */ 
/*  576 */           ntyp++;
/*      */         }
/*      */ 
/*  579 */         ti.addListener(13, new Listener() {
/*      */           public void handleEvent(Event event) {
/*  581 */             Rectangle bounds = ti.getBounds();
/*  582 */             Point point = tb.toDisplay(bounds.x, bounds.y + bounds.height);
/*  583 */             mu.setLocation(point);
/*  584 */             mu.setVisible(true);
/*      */           }
/*      */ 
/*      */         });
/*      */       }
/*      */ 
/*  590 */       ii++;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void createLayeringPart()
/*      */   {
/*  601 */     Composite titleComp = new Composite(this.shell, 0);
/*      */ 
/*  603 */     Label lyrs = new Label(titleComp, 0);
/*  604 */     lyrs.setText("Layers:");
/*      */ 
/*  606 */     GridLayout gl0 = new GridLayout(1, true);
/*  607 */     titleComp.setLayout(gl0);
/*      */ 
/*  609 */     addSeparator();
/*      */ 
/*  611 */     Composite addLayerComp = new Composite(this.shell, 0);
/*      */     int numActionBtns;
/*      */     int numActionBtns;
/*  614 */     if (this.compact) {
/*  615 */       numActionBtns = 2;
/*      */     }
/*      */     else {
/*  618 */       numActionBtns = 3;
/*      */     }
/*      */ 
/*  621 */     GridLayout gl = new GridLayout(numActionBtns, true);
/*  622 */     gl.makeColumnsEqualWidth = false;
/*  623 */     gl.marginHeight = 1;
/*  624 */     gl.marginWidth = 1;
/*  625 */     gl.verticalSpacing = 1;
/*  626 */     gl.horizontalSpacing = 1;
/*  627 */     addLayerComp.setLayout(gl);
/*      */ 
/*  629 */     Button addLayerBtn = new Button(addLayerComp, 0);
/*  630 */     addLayerBtn.setText("New");
/*  631 */     addLayerBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  633 */         ProductManageDialog.this.addLayer();
/*      */       }
/*      */     });
/*  638 */     this.allOnOffBtn = new Button(addLayerComp, 0);
/*  639 */     this.allOnOffBtn.setText("All On");
/*  640 */     this.allOnOffBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/*  642 */         ProductManageDialog.this.updateDisplayChecks();
/*      */       }
/*      */     });
/*  647 */     if (!this.compact)
/*      */     {
/*  649 */       Button delLayerBtn = new Button(addLayerComp, 0);
/*  650 */       delLayerBtn.setText("Delete");
/*  651 */       delLayerBtn.addSelectionListener(new SelectionAdapter() {
/*      */         public void widgetSelected(SelectionEvent event) {
/*  653 */           ProductManageDialog.this.deleteLayer();
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*  658 */     addSeparator();
/*      */ 
/*  661 */     createLayers();
/*      */   }
/*      */ 
/*      */   private void createLayers()
/*      */   {
/*  671 */     if ((this.layerList == null) || (this.layerList.size() == 0)) {
/*  672 */       return;
/*      */     }
/*      */ 
/*  675 */     this.layersComp = new Composite(this.shell, 0);
/*  676 */     GridLayout gl = new GridLayout(3, false);
/*  677 */     if (this.compact) {
/*  678 */       gl = new GridLayout(2, false);
/*      */     }
/*      */ 
/*  681 */     gl.marginHeight = 1;
/*  682 */     gl.marginWidth = 1;
/*  683 */     gl.verticalSpacing = 1;
/*  684 */     gl.horizontalSpacing = 0;
/*      */ 
/*  686 */     this.layersComp.setLayout(gl);
/*      */ 
/*  688 */     int ii = 0;
/*  689 */     for (Layer lyr : this.layerList)
/*      */     {
/*  691 */       Button nameBtn = new Button(this.layersComp, 8);
/*  692 */       nameBtn.setText(lyr.getName().replace("&", "&&"));
/*  693 */       setButtonColor(nameBtn, this.defaultButtonColor);
/*  694 */       nameBtn.setData(Integer.valueOf(ii));
/*  695 */       nameBtn.addSelectionListener(new SelectionAdapter() {
/*      */         public void widgetSelected(SelectionEvent event) {
/*  697 */           int ilayer = Integer.parseInt(event.widget.getData().toString());
/*  698 */           if (ProductManageDialog.this.layerInUse == ilayer)
/*      */           {
/*  700 */             ProductManageDialog.this.openPrdNameDialog = false;
/*  701 */             ProductManageDialog.this.openLayerNameDialog = true;
/*      */ 
/*  703 */             if (ProductManageDialog.this.prdNameDlg != null) ProductManageDialog.this.prdNameDlg.close();
/*  704 */             ProductManageDialog.this.editLayerName();
/*      */           }
/*      */           else
/*      */           {
/*  708 */             ProductManageDialog.this.switchLayer(ilayer);
/*      */           }
/*      */         }
/*      */       });
/*  713 */       this.layerNameBtns.add(nameBtn);
/*      */ 
/*  715 */       Button dispBtn = new Button(this.layersComp, 32);
/*      */ 
/*  717 */       dispBtn.setSelection(lyr.isOnOff());
/*  718 */       dispBtn.setData(Integer.valueOf(ii));
/*  719 */       dispBtn.addSelectionListener(new SelectionAdapter() {
/*      */         public void widgetSelected(SelectionEvent event) {
/*  721 */           ProductManageDialog.this.turnOnLayer(Integer.parseInt(event.widget.getData().toString()));
/*      */         }
/*      */       });
/*  725 */       this.displayOnOffBtns.add(dispBtn);
/*      */ 
/*  727 */       if (!this.compact) {
/*  728 */         Button clrBtn = new Button(this.layersComp, 8);
/*  729 */         if (lyr.isMonoColor()) {
/*  730 */           clrBtn.setText("M/F");
/*      */         }
/*      */         else {
/*  733 */           clrBtn.setText("A/F");
/*      */         }
/*      */ 
/*  736 */         setButtonColor(clrBtn, lyr.getColor());
/*  737 */         clrBtn.setData(Integer.valueOf(ii));
/*      */ 
/*  739 */         clrBtn.addSelectionListener(new SelectionAdapter() {
/*      */           public void widgetSelected(SelectionEvent event) {
/*  741 */             ProductManageDialog.this.colorModeBtnInUse = Integer.parseInt(event.widget.getData().toString());
/*  742 */             ProductManageDialog.this.editDisplayAttr();
/*      */           }
/*      */         });
/*  747 */         this.colorModeBtns.add(clrBtn);
/*      */       }
/*      */ 
/*  766 */       ii++;
/*      */     }
/*      */ 
/*  770 */     if ((this.layerInUse < 0) || (this.layerInUse >= this.layerList.size())) {
/*  771 */       this.layerInUse = (this.layerList.size() - 1);
/*      */     }
/*      */ 
/*  774 */     setButtonColor((Button)this.layerNameBtns.get(this.layerInUse), this.activeButtonColor);
/*      */ 
/*  778 */     this.currentLayer = ((Layer)this.layerList.get(this.layerInUse));
/*  779 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/*      */   }
/*      */ 
/*      */   protected void updateActiveLayerName(String name)
/*      */   {
/*  789 */     boolean update = false;
/*      */ 
/*  798 */     if ((name != null) && (name.length() > 0)) {
/*  799 */       update = true;
/*  800 */       for (Layer lyr : this.currentProduct.getLayers()) {
/*  801 */         if (lyr.getName().equals(name)) {
/*  802 */           update = false;
/*  803 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  813 */     this.openLayerNameDialog = false;
/*  814 */     if (update)
/*      */     {
/*  816 */       ((Button)this.layerNameBtns.get(this.layerInUse)).setText(name);
/*      */ 
/*  818 */       if (this.layerInUse >= 0) {
/*  819 */         ((Layer)this.layerList.get(this.layerInUse)).setName(name);
/*  820 */         this.currentLayer.setName(name);
/*      */       }
/*      */ 
/*  823 */       this.drawingLayer.setActiveLayer(this.currentLayer);
/*      */ 
/*  827 */       startProductManage();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void updateDisplayAttr(boolean mono, java.awt.Color clr, boolean fill)
/*      */   {
/*  837 */     if (this.colorModeBtnInUse >= 0)
/*      */     {
/*  839 */       if (mono) {
/*  840 */         ((Button)this.colorModeBtns.get(this.colorModeBtnInUse)).setText("M/F");
/*      */       }
/*      */       else {
/*  843 */         ((Button)this.colorModeBtns.get(this.colorModeBtnInUse)).setText("A/F");
/*      */       }
/*      */ 
/*  846 */       ((Layer)this.layerList.get(this.colorModeBtnInUse)).setMonoColor(mono);
/*  847 */       ((Layer)this.layerList.get(this.colorModeBtnInUse)).setColor(clr);
/*      */ 
/*  849 */       setButtonColor((Button)this.colorModeBtns.get(this.colorModeBtnInUse), clr);
/*      */ 
/*  851 */       ((Layer)this.layerList.get(this.colorModeBtnInUse)).setFilled(fill);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Layer getActiveLayer()
/*      */   {
/*  873 */     return this.currentLayer;
/*      */   }
/*      */ 
/*      */   protected Layer getLayerForColorMode()
/*      */   {
/*  882 */     return (Layer)this.layerList.get(this.colorModeBtnInUse);
/*      */   }
/*      */ 
/*      */   private void editLayerName()
/*      */   {
/*  903 */     Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/*  904 */     if (this.layerNameDlg == null) this.layerNameDlg = new LayeringNameDialog(shell, this);
/*      */ 
/*  906 */     cleanupDialogs();
/*      */ 
/*  908 */     this.layerNameDlg.open();
/*      */   }
/*      */ 
/*      */   private void editDisplayAttr()
/*      */   {
/*  919 */     Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/*  920 */     if (this.displayDlg == null) this.displayDlg = new LayeringDisplayDialog(shell, this);
/*      */ 
/*  922 */     cleanupDialogs();
/*      */ 
/*  924 */     this.displayDlg.open();
/*      */   }
/*      */ 
/*      */   private void editLayerLpfFileAttr()
/*      */   {
/*  936 */     Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/*      */ 
/*  938 */     if (this.layerLpfFileDlg == null) {
/*  939 */       this.layerLpfFileDlg = new LayeringLpfFileDialog(shell, this);
/*      */     }
/*      */ 
/*  942 */     cleanupDialogs();
/*      */ 
/*  944 */     this.layerLpfFileDlg.open();
/*      */   }
/*      */ 
/*      */   private void addLayer()
/*      */   {
/*  955 */     this.openLayerNameDialog = true;
/*  956 */     this.openPrdNameDialog = false;
/*      */ 
/*  958 */     cleanupDialogs();
/*      */ 
/*  961 */     int size1 = this.layerList.size() + 1;
/*  962 */     String name = new String("Layer_" + size1);
/*      */ 
/*  965 */     for (int ii = 0; ii < this.layerList.size(); ii++) {
/*  966 */       if (name.equals(((Layer)this.layerList.get(ii)).getName())) {
/*  967 */         name = new String("Layer_" + size1++);
/*  968 */         ii = 0;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  976 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*  977 */     boolean ponoff = ((Button)this.displayOnOffBtns.get(this.layerInUse)).getSelection();
/*  978 */     ((Layer)this.layerList.get(this.layerInUse)).setOnOff(ponoff);
/*      */ 
/*  981 */     this.currentLayer = new Layer();
/*  982 */     this.currentLayer.setName(name);
/*      */ 
/*  984 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/*  985 */     this.currentProduct.addLayer(this.currentLayer);
/*      */ 
/*  987 */     this.layerInUse = (this.layerList.size() - 1);
/*      */ 
/*  990 */     startProductManage();
/*      */   }
/*      */ 
/*      */   public void switchLayer(String newLayer)
/*      */   {
/*  998 */     String clayer = ((Layer)this.layerList.get(this.layerInUse)).getName();
/*  999 */     int which = -1;
/*      */ 
/* 1001 */     if (!newLayer.equals(clayer)) {
/* 1002 */       for (int ii = 0; ii < this.layerNameBtns.size(); ii++) {
/* 1003 */         if (((Button)this.layerNameBtns.get(ii)).getText().equals(newLayer)) {
/* 1004 */           which = ii;
/* 1005 */           break;
/*      */         }
/*      */       }
/*      */ 
/* 1009 */       if (which >= 0)
/* 1010 */         switchLayer(which);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void switchLayer(int which)
/*      */   {
/* 1025 */     boolean ponoff = ((Button)this.displayOnOffBtns.get(this.layerInUse)).getSelection();
/* 1026 */     ((Layer)this.layerList.get(this.layerInUse)).setOnOff(ponoff);
/*      */ 
/* 1029 */     setButtonColor((Button)this.layerNameBtns.get(this.layerInUse), this.defaultButtonColor);
/* 1030 */     this.layerInUse = which;
/*      */ 
/* 1032 */     setButtonColor((Button)this.layerNameBtns.get(this.layerInUse), this.activeButtonColor);
/*      */ 
/* 1037 */     this.openPrdNameDialog = false;
/* 1038 */     this.openLayerNameDialog = false;
/*      */ 
/* 1040 */     cleanupDialogs();
/*      */ 
/* 1042 */     this.currentLayer = ((Layer)this.layerList.get(this.layerInUse));
/*      */ 
/* 1044 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/*      */ 
/* 1046 */     this.drawingLayer.removeGhostLine();
/*      */ 
/* 1048 */     if (GfaAttrDlg.getInstance(getParent()).isGfaOpen()) {
/* 1049 */       if (this.drawingLayer.getSelectedDE() != null) {
/* 1050 */         GfaAttrDlg.getInstance(getParent()).close();
/*      */       }
/*      */       else {
/* 1053 */         GfaAttrDlg.getInstance(getParent()).switchHazard(this.currentLayer.getName());
/*      */       }
/*      */     }
/* 1056 */     else if (OutlookAttrDlg.getInstance(getParent()).getShell() != null) {
/* 1057 */       if (this.drawingLayer.getSelectedDE() != null) {
/* 1058 */         OutlookAttrDlg.getInstance(getParent()).close();
/*      */       }
/*      */       else {
/* 1061 */         OutlookAttrDlg.getInstance(getParent()).setOtlkType(this.currentLayer.getName());
/*      */       }
/*      */     }
/*      */     else {
/* 1065 */       PgenUtil.setSelectingMode();
/*      */     }
/*      */ 
/* 1068 */     this.drawingLayer.removeSelected();
/*      */ 
/* 1071 */     PgenSession.getInstance().disableUndoRedo();
/*      */ 
/* 1073 */     PgenUtil.refresh();
/*      */   }
/*      */ 
/*      */   private void turnOnLayer(int which)
/*      */   {
/* 1082 */     if (which != this.layerInUse) {
/* 1083 */       ((Layer)this.layerList.get(which)).setOnOff(((Button)this.displayOnOffBtns.get(which)).getSelection());
/*      */     }
/*      */ 
/* 1086 */     PgenUtil.refresh();
/*      */   }
/*      */ 
/*      */   private void updateDisplayChecks()
/*      */   {
/* 1095 */     if (this.allOnOff) {
/* 1096 */       this.allOnOff = false;
/* 1097 */       this.allOnOffBtn.setText("All On");
/*      */     }
/*      */     else {
/* 1100 */       this.allOnOff = true;
/* 1101 */       this.allOnOffBtn.setText("All Off");
/*      */     }
/*      */ 
/* 1104 */     for (int ii = 0; ii < this.layerList.size(); ii++) {
/* 1105 */       ((Button)this.displayOnOffBtns.get(ii)).setSelection(this.allOnOff);
/* 1106 */       ((Layer)this.layerList.get(ii)).setOnOff(this.allOnOff);
/*      */     }
/*      */ 
/* 1116 */     PgenUtil.refresh();
/*      */   }
/*      */ 
/*      */   protected String getColorModeLayerName()
/*      */   {
/* 1126 */     if ((this.layerList != null) && (this.colorModeBtnInUse >= 0)) {
/* 1127 */       return ((Layer)this.layerList.get(this.colorModeBtnInUse)).getName();
/*      */     }
/*      */ 
/* 1130 */     return null;
/*      */   }
/*      */ 
/*      */   private void exitLayering()
/*      */   {
/* 1153 */     this.currentLayer = new Layer();
/*      */ 
/* 1155 */     for (int ii = 0; ii < this.currentProduct.getLayers().size(); ii++) {
/* 1156 */       this.currentLayer.add(this.currentProduct.getLayer(ii).getDrawables());
/*      */     }
/*      */ 
/* 1162 */     this.currentProduct.clear();
/*      */ 
/* 1164 */     this.currentProduct.addLayer(this.currentLayer);
/*      */ 
/* 1169 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/* 1170 */     this.drawingLayer.setActiveProduct(this.currentProduct);
/*      */ 
/* 1172 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*      */ 
/* 1177 */     PgenUtil.refresh();
/*      */ 
/* 1179 */     PgenSession.getInstance().disableUndoRedo();
/*      */ 
/* 1184 */     if (this.layerNameDlg != null) this.layerNameDlg.close();
/* 1185 */     if (this.displayDlg != null) this.displayDlg.close();
/* 1186 */     if (this.layerLpfFileDlg != null) this.layerLpfFileDlg.close();
/*      */ 
/* 1188 */     close();
/*      */   }
/*      */ 
/*      */   private void deleteLayer()
/*      */   {
/* 1198 */     this.openLayerNameDialog = false;
/*      */ 
/* 1201 */     if (this.currentProduct.getLayers().size() > 1) {
/* 1202 */       this.currentProduct.removeLayer(this.layerInUse);
/*      */     }
/*      */ 
/* 1205 */     if (this.layerInUse >= this.currentProduct.getLayers().size()) {
/* 1206 */       this.layerInUse -= 1;
/*      */     }
/*      */ 
/* 1209 */     this.currentLayer = this.currentProduct.getLayer(this.layerInUse);
/*      */ 
/* 1213 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/*      */ 
/* 1215 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*      */ 
/* 1218 */     startProductManage();
/*      */ 
/* 1220 */     PgenUtil.refresh();
/*      */   }
/*      */ 
/*      */   private void startProductManage()
/*      */   {
/* 1230 */     cleanupDialogs();
/*      */ 
/* 1233 */     if (isOpen()) {
/* 1234 */       close();
/*      */     }
/*      */ 
/* 1238 */     open();
/*      */   }
/*      */ 
/*      */   private void turnOnProduct(int which)
/*      */   {
/* 1250 */     if (which != this.prdInUse) {
/* 1251 */       ((Product)this.prdList.get(which)).setOnOff(((Button)this.prdDispOnOffBtns.get(which)).getSelection());
/*      */     }
/*      */ 
/* 1254 */     PgenUtil.refresh();
/*      */   }
/*      */ 
/*      */   private void exitProductManage()
/*      */   {
/* 1266 */     if (needSaving()) {
/* 1267 */       MessageDialog confirmDlg = new MessageDialog(
/* 1268 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 1269 */         "Confirm Exit from Product Management", null, 
/* 1270 */         "Do you want to save the changes?", 
/* 1271 */         3, new String[] { "Yes", "No" }, 0);
/*      */ 
/* 1273 */       confirmDlg.open();
/*      */ 
/* 1275 */       if (confirmDlg.getReturnCode() == 0)
/*      */       {
/* 1277 */         PgenFileManageDialog file_dlg = null;
/*      */ 
/* 1279 */         if (file_dlg == null) {
/*      */           try {
/* 1281 */             file_dlg = new PgenFileManageDialog(this.shell, "Save");
/*      */           }
/*      */           catch (VizException e) {
/* 1284 */             e.printStackTrace();
/*      */           }
/*      */         }
/*      */ 
/* 1288 */         if (file_dlg != null) file_dlg.open();
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1298 */     if ((this.drawingLayer.getProducts().size() == 1) && 
/* 1299 */       (this.currentProduct.getName().equalsIgnoreCase("Default")) && 
/* 1300 */       (this.currentProduct.getType().equalsIgnoreCase("Default")))
/*      */     {
/* 1302 */       exitLayering();
/*      */ 
/* 1304 */       if (this.prdNameDlg != null) this.prdNameDlg.close();
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1309 */       this.drawingLayer.removeAllProducts();
/*      */ 
/* 1311 */       this.currentProduct = new Product("Default", "Default", "Default", 
/* 1312 */         new ProductInfo(), new ProductTime(), new ArrayList());
/*      */ 
/* 1314 */       this.currentProduct.setType("Default");
/*      */ 
/* 1316 */       this.currentLayer = new Layer();
/* 1317 */       this.currentProduct.addLayer(this.currentLayer);
/*      */ 
/* 1319 */       this.drawingLayer.addProduct(this.currentProduct);
/* 1320 */       this.drawingLayer.setActiveProduct(this.currentProduct);
/* 1321 */       this.drawingLayer.setActiveLayer(this.currentLayer);
/*      */ 
/* 1326 */       PgenUtil.refresh();
/*      */ 
/* 1329 */       PgenSession.getInstance().disableUndoRedo();
/*      */ 
/* 1334 */       cleanupDialogs();
/*      */ 
/* 1336 */       close();
/*      */     }
/*      */ 
/* 1340 */     this.currentProduct.setOutputFile(null);
/*      */ 
/* 1345 */     refreshPgenPalette(null);
/* 1346 */     this.compact = true;
/* 1347 */     this.openLayerNameDialog = false;
/* 1348 */     this.openPrdNameDialog = false;
/*      */   }
/*      */ 
/*      */   private void createExitPart()
/*      */   {
/* 1357 */     Composite exitComp = new Composite(this.shell, 0);
/*      */ 
/* 1359 */     GridLayout gl = new GridLayout(2, false);
/* 1360 */     gl.marginHeight = 1;
/* 1361 */     gl.marginWidth = 1;
/* 1362 */     gl.verticalSpacing = 1;
/* 1363 */     gl.horizontalSpacing = 2;
/*      */ 
/* 1365 */     exitComp.setLayout(gl);
/*      */ 
/* 1367 */     Button exitBtn = new Button(exitComp, 0);
/* 1368 */     exitBtn.setText("Exit");
/* 1369 */     exitBtn.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent event) {
/* 1371 */         ProductManageDialog.this.exitProductManage();
/*      */       }
/*      */     });
/* 1375 */     this.arrowBtn = new Button(exitComp, 0);
/*      */ 
/* 1377 */     if (this.compact) {
/* 1378 */       this.arrowBtn.setText(">>");
/*      */     }
/*      */     else {
/* 1381 */       this.arrowBtn.setText("<<");
/*      */     }
/*      */ 
/* 1384 */     this.arrowBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent event) {
/* 1387 */         ProductManageDialog.this.openLayerNameDialog = false;
/*      */ 
/* 1389 */         if (ProductManageDialog.this.compact) {
/* 1390 */           ProductManageDialog.this.arrowBtn.setText("<<");
/*      */         }
/*      */         else {
/* 1393 */           ProductManageDialog.this.arrowBtn.setText(">>");
/*      */         }
/*      */ 
/* 1396 */         ProductManageDialog.this.compact = (!ProductManageDialog.this.compact);
/*      */ 
/* 1398 */         ProductManageDialog.this.openPrdNameDialog = false;
/* 1399 */         ProductManageDialog.this.openLayerNameDialog = false;
/*      */ 
/* 1401 */         ProductManageDialog.this.cleanupDialogs();
/*      */ 
/* 1403 */         ProductManageDialog.this.startProductManage();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void addProduct()
/*      */   {
/* 1416 */     this.openPrdNameDialog = true;
/* 1417 */     this.openLayerNameDialog = false;
/*      */ 
/* 1419 */     cleanupDialogs();
/*      */ 
/* 1422 */     String name = new String("Default");
/*      */ 
/* 1425 */     if (this.currentProduct != null) {
/* 1426 */       this.currentProduct.setInUse(false);
/* 1427 */       this.currentProduct.setOnOff(false);
/*      */     }
/*      */ 
/* 1430 */     this.currentProduct = new Product();
/* 1431 */     this.currentProduct.setName(name);
/*      */ 
/* 1433 */     this.drawingLayer.addProduct(this.currentProduct);
/* 1434 */     this.drawingLayer.setActiveProduct(this.currentProduct);
/*      */ 
/* 1436 */     this.prdList = ((ArrayList)this.drawingLayer.getProducts());
/*      */ 
/* 1438 */     this.prdInUse = (this.prdList.size() - 1);
/*      */ 
/* 1441 */     this.currentLayer = new Layer();
/*      */ 
/* 1443 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/* 1444 */     this.currentProduct.addLayer(this.currentLayer);
/*      */ 
/* 1446 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*      */ 
/* 1448 */     this.layerInUse = (this.layerList.size() - 1);
/*      */ 
/* 1452 */     startProductManage();
/*      */   }
/*      */ 
/*      */   private void switchProduct(int which)
/*      */   {
/* 1462 */     setButtonColor((Button)this.prdNameBtns.get(this.prdInUse), this.defaultButtonColor);
/* 1463 */     ((Button)this.prdDispOnOffBtns.get(this.prdInUse)).setSelection(false);
/* 1464 */     ((Product)this.prdList.get(this.prdInUse)).setOnOff(false);
/* 1465 */     ((Product)this.prdList.get(this.prdInUse)).setInUse(false);
/*      */ 
/* 1467 */     this.prdInUse = which;
/*      */ 
/* 1469 */     setButtonColor((Button)this.prdNameBtns.get(this.prdInUse), this.activeButtonColor);
/*      */ 
/* 1472 */     this.openPrdNameDialog = false;
/* 1473 */     this.openLayerNameDialog = false;
/*      */ 
/* 1475 */     cleanupDialogs();
/*      */ 
/* 1477 */     this.currentProduct = ((Product)this.prdList.get(this.prdInUse));
/*      */ 
/* 1479 */     this.currentProduct.setOnOff(true);
/* 1480 */     this.currentProduct.setInUse(true);
/*      */ 
/* 1482 */     this.drawingLayer.setActiveProduct(this.currentProduct);
/*      */ 
/* 1485 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*      */ 
/* 1487 */     this.layerInUse = 0;
/*      */ 
/* 1489 */     this.currentLayer = ((Layer)this.layerList.get(this.layerInUse));
/*      */ 
/* 1491 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/* 1492 */     this.drawingLayer.removeGhostLine();
/* 1493 */     this.drawingLayer.removeSelected();
/*      */ 
/* 1495 */     PgenUtil.setSelectingMode();
/*      */ 
/* 1498 */     resetPalette(this.currentProduct);
/*      */ 
/* 1501 */     PgenSession.getInstance().disableUndoRedo();
/*      */ 
/* 1503 */     PgenUtil.refresh();
/*      */ 
/* 1506 */     startProductManage();
/*      */ 
/* 1509 */     AttrSettings.getInstance().loadProdSettings(this.currentProduct.getType());
/*      */   }
/*      */ 
/*      */   private void deleteProduct()
/*      */   {
/* 1517 */     if (this.prdList.size() <= 1) return;
/*      */ 
/* 1520 */     this.openPrdNameDialog = false;
/* 1521 */     this.openLayerNameDialog = false;
/*      */ 
/* 1524 */     if (this.currentProduct != null) {
/* 1525 */       this.drawingLayer.removeProduct(this.currentProduct);
/*      */     }
/*      */ 
/* 1528 */     this.prdList = ((ArrayList)this.drawingLayer.getProducts());
/*      */ 
/* 1530 */     this.prdInUse -= 1;
/*      */ 
/* 1532 */     if (this.prdInUse < 0) {
/* 1533 */       this.prdInUse = 0;
/*      */     }
/*      */ 
/* 1536 */     this.currentProduct = ((Product)this.prdList.get(this.prdInUse));
/* 1537 */     this.currentProduct.setInUse(true);
/*      */ 
/* 1540 */     this.drawingLayer.setActiveProduct(this.currentProduct);
/*      */ 
/* 1543 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/* 1544 */     this.layerInUse = (this.layerList.size() - 1);
/* 1545 */     this.currentLayer = ((Layer)this.layerList.get(this.layerInUse));
/*      */ 
/* 1548 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/*      */ 
/* 1550 */     PgenUtil.refresh();
/*      */ 
/* 1553 */     startProductManage();
/*      */   }
/*      */ 
/*      */   private void updateProdDispChecks()
/*      */   {
/* 1562 */     if (this.prodAllOnOff) {
/* 1563 */       this.prodAllOnOff = false;
/* 1564 */       this.prodAllOnOffBtn.setText("All On");
/*      */     }
/*      */     else {
/* 1567 */       this.prodAllOnOff = true;
/* 1568 */       this.prodAllOnOffBtn.setText("All Off");
/*      */     }
/*      */ 
/* 1571 */     for (int ii = 0; ii < this.prdList.size(); ii++) {
/* 1572 */       ((Button)this.prdDispOnOffBtns.get(ii)).setSelection(this.prodAllOnOff);
/* 1573 */       ((Product)this.prdList.get(ii)).setOnOff(this.prodAllOnOff);
/*      */     }
/*      */ 
/* 1583 */     PgenUtil.refresh();
/*      */   }
/*      */ 
/*      */   protected Product getActiveProduct()
/*      */   {
/* 1592 */     return this.currentProduct;
/*      */   }
/*      */ 
/*      */   protected void updateProductAttr(HashMap<String, String> attr)
/*      */   {
/* 1602 */     boolean update = false;
/*      */ 
/* 1605 */     String value = (String)attr.get("name");
/* 1606 */     if ((value != null) && (value.length() > 0) && 
/* 1607 */       (!value.equals(this.currentProduct.getName()))) {
/* 1608 */       this.currentProduct.setName(value);
/* 1609 */       update = true;
/*      */     }
/*      */ 
/* 1612 */     value = (String)attr.get("type");
/* 1613 */     if ((value != null) && (value.length() > 0) && 
/* 1614 */       (!value.equals(this.currentProduct.getType()))) {
/* 1615 */       updateLayerInfoFromNewPrdType(this.currentProduct, value);
/* 1616 */       update = true;
/*      */     }
/*      */ 
/* 1619 */     value = (String)attr.get("forecaster");
/* 1620 */     if (!value.equals(this.currentProduct.getForecaster())) {
/* 1621 */       if (value != null) {
/* 1622 */         value = value.trim();
/*      */       }
/* 1624 */       this.currentProduct.setForecaster(value);
/*      */     }
/*      */ 
/* 1627 */     value = (String)attr.get("center");
/* 1628 */     if (!value.equals(this.currentProduct.getCenter())) {
/* 1629 */       if (value != null) {
/* 1630 */         value = value.trim();
/*      */       }
/*      */ 
/* 1633 */       this.currentProduct.setCenter(value);
/*      */     }
/*      */ 
/* 1636 */     value = (String)attr.get("saveLayers");
/* 1637 */     if (value != null) {
/* 1638 */       if (value.equals("true")) {
/* 1639 */         this.currentProduct.setSaveLayers(true);
/*      */       }
/*      */       else {
/* 1642 */         this.currentProduct.setSaveLayers(false);
/*      */       }
/*      */     }
/*      */ 
/* 1646 */     value = (String)attr.get("outputfile");
/* 1647 */     if ((!value.equals(this.currentProduct.getOutputFile())) && 
/* 1648 */       (value != null)) {
/* 1649 */       value = value.trim();
/*      */     }
/*      */ 
/* 1659 */     this.openPrdNameDialog = false;
/* 1660 */     if (update)
/*      */     {
/* 1662 */       resetPalette(this.currentProduct);
/*      */ 
/* 1664 */       startProductManage();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateLayerInfoFromNewPrdType(Product prd, String prdtype)
/*      */   {
/* 1689 */     if (!prd.getType().equals(prdtype))
/*      */     {
/* 1691 */       prd.setSaveLayers(false);
/* 1692 */       prd.setOutputFile(null);
/*      */ 
/* 1694 */       prd.setType(prdtype);
/*      */ 
/* 1696 */       ProductType newType = (ProductType)this.prdTypesMap.get(prdtype);
/*      */ 
/* 1699 */       ArrayList layers = new ArrayList();
/* 1700 */       for (Layer lyr : prd.getLayers()) {
/* 1701 */         if (lyr.getDrawables().size() > 0) {
/* 1702 */           layers.add(lyr);
/*      */         }
/*      */       }
/*      */ 
/* 1706 */       prd.clear();
/*      */ 
/* 1708 */       if (layers.size() > 0) {
/* 1709 */         for (Layer lyr : layers) {
/* 1710 */           prd.addLayer(lyr);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1715 */       if (newType != null)
/*      */       {
/* 1717 */         PgenSave psave = newType.getPgenSave();
/*      */ 
/* 1719 */         if (psave != null) {
/* 1720 */           prd.setSaveLayers(psave.isSaveLayers().booleanValue());
/*      */         }
/*      */ 
/* 1724 */         ArrayList lyrs = (ArrayList)prd.getLayers();
/* 1725 */         for (PgenLayer plyr : newType.getPgenLayer())
/*      */         {
/* 1728 */           boolean updated = false;
/* 1729 */           for (Layer olyr : lyrs) {
/* 1730 */             if (olyr.getName().equals(plyr.getName()))
/*      */             {
/* 1732 */               olyr.setOnOff(plyr.isOnOff().booleanValue());
/* 1733 */               olyr.setMonoColor(plyr.isMonoColor().booleanValue());
/* 1734 */               olyr.setFilled(plyr.isFilled().booleanValue());
/* 1735 */               olyr.setInputFile(null);
/* 1736 */               olyr.setOutputFile(null);
/*      */ 
/* 1738 */               java.awt.Color clr = new java.awt.Color(plyr.getColor().getRed(), 
/* 1739 */                 plyr.getColor().getGreen(), 
/* 1740 */                 plyr.getColor().getBlue(), 
/* 1741 */                 plyr.getColor().getAlpha().intValue());
/* 1742 */               olyr.setColor(clr);
/* 1743 */               updated = true;
/* 1744 */               break;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1749 */           if (!updated)
/*      */           {
/* 1751 */             Layer lyr = new Layer();
/* 1752 */             lyr.setName(plyr.getName());
/* 1753 */             lyr.setOnOff(plyr.isOnOff().booleanValue());
/* 1754 */             lyr.setMonoColor(plyr.isMonoColor().booleanValue());
/* 1755 */             lyr.setFilled(plyr.isFilled().booleanValue());
/* 1756 */             lyr.setInputFile(null);
/* 1757 */             lyr.setOutputFile(null);
/*      */ 
/* 1759 */             java.awt.Color clr = new java.awt.Color(plyr.getColor().getRed(), 
/* 1760 */               plyr.getColor().getGreen(), 
/* 1761 */               plyr.getColor().getBlue(), 
/* 1762 */               plyr.getColor().getAlpha().intValue());
/* 1763 */             lyr.setColor(clr);
/*      */ 
/* 1765 */             prd.addLayer(lyr);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1772 */       if (prd.getLayers().size() <= 0) {
/* 1773 */         prd.addLayer(new Layer());
/*      */       }
/*      */ 
/* 1777 */       if (prd.equals(this.currentProduct)) {
/* 1778 */         this.layerList = ((ArrayList)this.currentProduct.getLayers());
/* 1779 */         this.layerInUse = -1;
/* 1780 */         if (this.layerList.size() > 0)
/* 1781 */           this.layerInUse = 0;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void editProductAttr()
/*      */   {
/* 1797 */     Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/* 1798 */     if (this.prdNameDlg == null) this.prdNameDlg = new ProductNameDialog(shell, this);
/*      */ 
/* 1800 */     cleanupDialogs();
/* 1801 */     this.prdNameDlg.open();
/*      */   }
/*      */ 
/*      */   private void switchProductType(Text txt)
/*      */   {
/* 1810 */     if ((this.prdNameDlg != null) && (this.prdNameDlg.isOpen())) {
/* 1811 */       this.prdNameDlg.close();
/*      */     }
/*      */ 
/* 1814 */     int prdIndex = Integer.parseInt(txt.getData().toString());
/* 1815 */     String prevType = ((Product)this.prdList.get(prdIndex)).getType();
/* 1816 */     boolean typeExist = false;
/*      */ 
/* 1818 */     for (String ptypName : this.prdTypesMap.keySet()) {
/* 1819 */       if (prevType.equals(ptypName)) {
/* 1820 */         typeExist = true;
/* 1821 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1825 */     if (!typeExist) {
/* 1826 */       prevType = new String("Default");
/*      */     }
/*      */ 
/* 1829 */     if (!txt.getText().equals(prevType))
/*      */     {
/* 1831 */       MessageDialog confirmDlg = new MessageDialog(
/* 1832 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 1833 */         "Confirm changing product types", null, 
/* 1834 */         "Are you sure you want to change from type " + 
/* 1835 */         prevType + " to type " + txt.getText() + "?", 
/* 1836 */         3, new String[] { "OK", "Cancel" }, 0);
/*      */ 
/* 1838 */       confirmDlg.open();
/*      */ 
/* 1840 */       if (confirmDlg.getReturnCode() == 0) {
/* 1841 */         Product prd = (Product)this.prdList.get(prdIndex);
/*      */ 
/* 1843 */         if (this.currentProduct.equals(prd)) {
/* 1844 */           resetPalette(this.currentProduct);
/*      */         }
/*      */ 
/* 1850 */         String newTypeName = txt.getText();
/*      */ 
/* 1852 */         updateLayerInfoFromNewPrdType(prd, newTypeName);
/*      */ 
/* 1854 */         ((Product)this.prdList.get(prdIndex)).setType(newTypeName);
/* 1855 */         ((Product)this.prdList.get(prdIndex)).setName(newTypeName);
/* 1856 */         ((Button)this.prdNameBtns.get(prdIndex)).setText(newTypeName);
/*      */ 
/* 1858 */         if (prd.equals(this.currentProduct))
/* 1859 */           switchProduct(this.prdInUse);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void switchProductType1(Combo cmb)
/*      */   {
/* 1871 */     if ((this.prdNameDlg != null) && (this.prdNameDlg.isOpen())) {
/* 1872 */       this.prdNameDlg.close();
/*      */     }
/*      */ 
/* 1875 */     int prdIndex = Integer.parseInt(cmb.getData().toString());
/* 1876 */     String prevType = ((Product)this.prdList.get(prdIndex)).getType();
/* 1877 */     boolean typeExist = false;
/*      */ 
/* 1879 */     for (String ptypName : this.prdTypesMap.keySet()) {
/* 1880 */       if (prevType.equals(ptypName)) {
/* 1881 */         typeExist = true;
/* 1882 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1886 */     if (!typeExist) {
/* 1887 */       prevType = cmb.getItem(0);
/*      */     }
/*      */ 
/* 1890 */     if (!cmb.getText().equals(prevType))
/*      */     {
/* 1892 */       MessageDialog confirmDlg = new MessageDialog(
/* 1893 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 1894 */         "Confirm changing product types", null, 
/* 1895 */         "Are you sure you want to change from type " + 
/* 1896 */         prevType + " to type " + cmb.getText() + "?", 
/* 1897 */         3, new String[] { "OK", "Cancel" }, 0);
/*      */ 
/* 1899 */       confirmDlg.open();
/*      */ 
/* 1901 */       if (confirmDlg.getReturnCode() == 0)
/*      */       {
/* 1903 */         if (this.currentProduct.getName().equals(((Product)this.prdList.get(prdIndex)).getName())) {
/* 1904 */           resetPalette(this.currentProduct);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1909 */         int kk = 0;
/* 1910 */         int isel = 0;
/* 1911 */         for (String str : cmb.getItems())
/*      */         {
/* 1913 */           if (str.equals(prevType)) {
/* 1914 */             isel = kk;
/*      */           }
/*      */ 
/* 1917 */           kk++;
/*      */         }
/*      */ 
/* 1920 */         cmb.select(isel);
/*      */       }
/*      */ 
/* 1927 */       if (!cmb.getText().equals(prevType)) {
/* 1928 */         Product prd = (Product)this.prdList.get(prdIndex);
/*      */ 
/* 1930 */         String newTypeName = cmb.getText();
/*      */ 
/* 1932 */         updateLayerInfoFromNewPrdType(prd, newTypeName);
/*      */ 
/* 1934 */         if (prd.equals(this.currentProduct))
/* 1935 */           switchProduct(this.prdInUse);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void resetPalette(Product prd)
/*      */   {
/* 1954 */     ProductType ptyp = (ProductType)this.prdTypesMap.get(prd.getType());
/* 1955 */     refreshPgenPalette(ptyp);
/*      */ 
/* 1957 */     PgenSession.getInstance().getPgenPalette().setActiveIcon("Select");
/* 1958 */     PgenUtil.setSelectingMode();
/*      */   }
/*      */ 
/*      */   protected void cleanupDialogs()
/*      */   {
/* 1966 */     if (this.prdNameDlg != null) this.prdNameDlg.close();
/* 1967 */     if (this.layerNameDlg != null) this.layerNameDlg.close();
/* 1968 */     if (this.displayDlg != null) this.displayDlg.close();
/* 1969 */     if (this.layerLpfFileDlg != null) this.layerLpfFileDlg.close();
/* 1970 */     if (this.prdFileInOutDlg != null) this.prdFileInOutDlg.close();
/*      */   }
/*      */ 
/*      */   private void editProductFileAttr()
/*      */   {
/* 1981 */     Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/*      */ 
/* 1983 */     if (this.prdFileInOutDlg == null) {
/* 1984 */       this.prdFileInOutDlg = new ProductFileNameDialog(shell, this);
/*      */     }
/*      */ 
/* 1987 */     cleanupDialogs();
/*      */ 
/* 1989 */     this.prdFileInOutDlg.open();
/*      */   }
/*      */ 
/*      */   protected void setOpenPrdNameDlg(boolean value)
/*      */   {
/* 2029 */     this.openPrdNameDialog = value;
/*      */   }
/*      */ 
/*      */   protected void setopenLayerNameDlg(boolean value)
/*      */   {
/* 2036 */     this.openLayerNameDialog = value;
/*      */   }
/*      */ 
/*      */   public String getPrdOutputFile(Product prd)
/*      */   {
/* 2044 */     String sfile = null;
/*      */ 
/* 2046 */     ProductType actTyp = (ProductType)this.prdTypesMap.get(prd.getType());
/* 2047 */     if (actTyp != null)
/*      */     {
/* 2049 */       if (actTyp.getPgenSave() != null) {
/* 2050 */         sfile = actTyp.getPgenSave().getOutputFile();
/*      */       }
/*      */ 
/* 2053 */       if ((sfile != null) && (sfile.trim().length() > 0)) {
/* 2054 */         String dtyp = new String(actTyp.getType());
/* 2055 */         String dstyp = actTyp.getSubtype();
/* 2056 */         if ((dstyp != null) && (dstyp.trim().length() > 0) && 
/* 2057 */           (!dstyp.equalsIgnoreCase("None"))) {
/* 2058 */           dtyp = new String(actTyp.getType() + "(" + dstyp + ")");
/*      */         }
/*      */ 
/* 2061 */         if ((!dtyp.equals(prd.getName())) && 
/* 2062 */           (actTyp.getName() != null) && 
/* 2063 */           (!actTyp.getName().equals(prd.getName()))) {
/* 2064 */           sfile = new String(prd.getName() + "." + sfile);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2069 */     if ((sfile == null) || (sfile.trim().length() == 0)) {
/* 2070 */       StringBuilder fname = new StringBuilder();
/*      */ 
/* 2072 */       fname.append("Default.");
/*      */ 
/* 2074 */       fname.append(PgenUtil.formatDate(Calendar.getInstance()) + ".");
/*      */ 
/* 2076 */       fname.append(Calendar.getInstance().get(11) + ".xml");
/*      */ 
/* 2078 */       sfile = fname.toString();
/*      */     }
/*      */ 
/* 2082 */     String filename = new String(buildFilePath(prd) + File.separator + sfile);
/* 2083 */     filename = filename.replaceFirst("DDMMYYYY", 
/* 2084 */       PgenUtil.formatDate(Calendar.getInstance()));
/* 2085 */     filename = filename.replaceFirst("HH", 
/* 2086 */       Calendar.getInstance().get(11));
/*      */ 
/* 2088 */     return filename;
/*      */   }
/*      */ 
/*      */   private String buildFilePath(Product prd)
/*      */   {
/* 2096 */     StringBuilder sdir = new StringBuilder();
/*      */ 
/* 2098 */     sdir.append(PgenUtil.getPgenOprDirectory());
/*      */ 
/* 2100 */     String typeName = prd.getType();
/* 2101 */     ProductType actTyp = (ProductType)this.prdTypesMap.get(typeName);
/* 2102 */     if (actTyp != null) {
/* 2103 */       sdir.append(File.separator + actTyp.getType());
/* 2104 */       sdir.append(File.separator + "xml");
/*      */     }
/*      */ 
/* 2107 */     String path = sdir.toString().replace(' ', '_');
/*      */ 
/* 2109 */     return path;
/*      */   }
/*      */ 
/*      */   protected LinkedHashMap<String, String> getSubtypes(String ptype, boolean noAlias)
/*      */   {
/* 2120 */     LinkedHashMap stypes = new LinkedHashMap();
/*      */ 
/* 2122 */     for (String typeID : this.prdTypesMap.keySet()) {
/* 2123 */       ProductType prdType = (ProductType)this.prdTypesMap.get(typeID);
/* 2124 */       if (prdType.getType().equals(ptype)) {
/* 2125 */         if (noAlias) {
/* 2126 */           if ((prdType.getName() == null) || (prdType.getName().trim().length() == 0) || 
/* 2127 */             (prdType.getName().equals(prdType.getType()))) {
/* 2128 */             stypes.put(typeID, prdType.getSubtype());
/*      */           }
/*      */         }
/*      */         else {
/* 2132 */           stypes.put(typeID, prdType.getSubtype());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2137 */     return stypes;
/*      */   }
/*      */ 
/*      */   protected boolean hasSubtypes(Collection<String> subtypes)
/*      */   {
/* 2147 */     boolean hasSubtypes = true;
/* 2148 */     if ((subtypes == null) || (subtypes.size() == 0)) {
/* 2149 */       hasSubtypes = false;
/*      */     }
/* 2151 */     else if (subtypes.size() == 1) {
/* 2152 */       for (String st : subtypes) {
/* 2153 */         if (st.equalsIgnoreCase("None")) {
/* 2154 */           hasSubtypes = false;
/* 2155 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2160 */     return hasSubtypes;
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/* 2167 */     PgenUtil.setSelectingMode();
/* 2168 */     super.close();
/*      */   }
/*      */ 
/*      */   protected void exit()
/*      */   {
/* 2175 */     exitProductManage();
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.ProductManageDialog
 * JD-Core Version:    0.6.2
 */