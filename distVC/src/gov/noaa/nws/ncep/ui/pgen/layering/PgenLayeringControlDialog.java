/*     */ package gov.noaa.nws.ncep.ui.pgen.layering;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.GfaAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductInfo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Widget;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenLayeringControlDialog extends PgenLayeringDialog
/*     */ {
/*  56 */   private final Color defaultLayerButtonColor = Color.lightGray;
/*  57 */   private final Color activeLayerButtonColor = Color.green;
/*     */ 
/*  62 */   protected PgenLayeringNameDialog layerNameDlg = null;
/*  63 */   protected PgenLayeringDisplayDialog displayDlg = null;
/*     */ 
/*  69 */   private ArrayList<Layer> layerList = null;
/*  70 */   private ArrayList<Button> layerNameBtns = null;
/*  71 */   private ArrayList<Button> displayOnOffBtns = null;
/*  72 */   private ArrayList<Button> colorModeBtns = null;
/*     */ 
/*  74 */   private Button allOnOffBtn = null;
/*     */ 
/*  79 */   private int layerInUse = -1;
/*  80 */   private int colorModeBtnInUse = -1;
/*  81 */   private boolean allOnOff = false;
/*     */ 
/*  86 */   private Button arrowBtn = null;
/*  87 */   boolean compact = true;
/*  88 */   boolean openNameDialog = false;
/*     */ 
/*     */   public PgenLayeringControlDialog(Shell parentShell)
/*     */   {
/*  94 */     super(parentShell);
/*     */   }
/*     */ 
/*     */   public void setTitle()
/*     */   {
/* 102 */     this.shell.setText("Layering");
/*     */   }
/*     */ 
/*     */   public void setDefaultLocation(Shell parent)
/*     */   {
/* 112 */     if (this.shellLocation == null) {
/* 113 */       Point pt = parent.getLocation();
/* 114 */       this.shell.setLocation(pt.x + 255, pt.y + 146);
/*     */     }
/*     */     else {
/* 117 */       this.shell.setLocation(this.shellLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void popupSecondDialog()
/*     */   {
/* 126 */     if (this.openNameDialog)
/* 127 */       editLayerName();
/*     */   }
/*     */ 
/*     */   public void initializeComponents()
/*     */   {
/* 138 */     initialize();
/*     */ 
/* 141 */     Composite addLayerComp = new Composite(this.shell, 0);
/*     */ 
/* 143 */     GridLayout gl = new GridLayout(2, true);
/* 144 */     gl.makeColumnsEqualWidth = false;
/* 145 */     gl.marginHeight = 1;
/* 146 */     gl.marginWidth = 1;
/* 147 */     gl.verticalSpacing = 1;
/* 148 */     gl.horizontalSpacing = 1;
/* 149 */     addLayerComp.setLayout(gl);
/*     */ 
/* 151 */     Button addLayerBtn = new Button(addLayerComp, 0);
/* 152 */     addLayerBtn.setText("Add Layer");
/* 153 */     addLayerBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 155 */         PgenLayeringControlDialog.this.addLayer();
/*     */       }
/*     */     });
/* 160 */     if (!this.compact) {
/* 161 */       Button delLayerBtn = new Button(addLayerComp, 0);
/* 162 */       delLayerBtn.setText("Delete");
/* 163 */       delLayerBtn.addSelectionListener(new SelectionAdapter() {
/*     */         public void widgetSelected(SelectionEvent event) {
/* 165 */           PgenLayeringControlDialog.this.deleteLayer();
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 170 */     addSeparator();
/*     */ 
/* 173 */     createLayers();
/*     */ 
/* 175 */     addSeparator();
/*     */ 
/* 178 */     Composite centeredComp = new Composite(this.shell, 0);
/* 179 */     centeredComp.setLayout(gl);
/*     */ 
/* 181 */     this.allOnOffBtn = new Button(centeredComp, 0);
/* 182 */     this.allOnOffBtn.setText("All On");
/* 183 */     this.allOnOffBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 185 */         PgenLayeringControlDialog.this.updateDisplayChecks();
/*     */       }
/*     */     });
/* 190 */     if (!this.compact)
/*     */     {
/* 192 */       Button editNameBtn = new Button(centeredComp, 0);
/* 193 */       editNameBtn.setText("Edit Name");
/* 194 */       editNameBtn.addSelectionListener(new SelectionAdapter() {
/*     */         public void widgetSelected(SelectionEvent event) {
/* 196 */           PgenLayeringControlDialog.this.editLayerName();
/*     */         }
/*     */ 
/*     */       });
/*     */     }
/*     */ 
/* 203 */     Composite exitComp = new Composite(this.shell, 0);
/* 204 */     exitComp.setLayout(gl);
/*     */ 
/* 206 */     Button exitBtn = new Button(exitComp, 0);
/* 207 */     exitBtn.setText("Exit");
/* 208 */     exitBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 210 */         PgenLayeringControlDialog.this.exitLayering();
/*     */       }
/*     */     });
/* 214 */     this.arrowBtn = new Button(exitComp, 0);
/*     */ 
/* 216 */     if (this.compact) {
/* 217 */       this.arrowBtn.setText(">>");
/*     */     }
/*     */     else {
/* 220 */       this.arrowBtn.setText("<<");
/*     */     }
/*     */ 
/* 223 */     this.arrowBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 226 */         PgenLayeringControlDialog.this.openNameDialog = false;
/*     */ 
/* 228 */         if (PgenLayeringControlDialog.this.compact) {
/* 229 */           PgenLayeringControlDialog.this.arrowBtn.setText("<<");
/*     */         }
/*     */         else {
/* 232 */           PgenLayeringControlDialog.this.arrowBtn.setText(">>");
/*     */         }
/*     */ 
/* 235 */         PgenLayeringControlDialog.this.compact = (!PgenLayeringControlDialog.this.compact);
/*     */ 
/* 237 */         PgenLayeringControlDialog.this.startLayering();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createLayers()
/*     */   {
/* 249 */     Composite layersComp = new Composite(this.shell, 0);
/* 250 */     GridLayout gl = new GridLayout(3, false);
/*     */ 
/* 252 */     if (this.compact) {
/* 253 */       gl = new GridLayout(2, false);
/*     */     }
/*     */ 
/* 256 */     gl.marginHeight = 1;
/* 257 */     gl.marginWidth = 1;
/* 258 */     gl.verticalSpacing = 1;
/* 259 */     gl.horizontalSpacing = 0;
/*     */ 
/* 261 */     layersComp.setLayout(gl);
/*     */ 
/* 263 */     int ii = 0;
/* 264 */     for (Layer lyr : this.layerList)
/*     */     {
/* 266 */       Button nameBtn = new Button(layersComp, 8);
/* 267 */       nameBtn.setText(lyr.getName().replace("&", "&&"));
/* 268 */       setButtonColor(nameBtn, this.defaultLayerButtonColor);
/* 269 */       nameBtn.setData(Integer.valueOf(ii));
/* 270 */       nameBtn.addSelectionListener(new SelectionAdapter() {
/*     */         public void widgetSelected(SelectionEvent event) {
/* 272 */           PgenLayeringControlDialog.this.switchLayer(Integer.parseInt(event.widget.getData().toString()));
/*     */         }
/*     */       });
/* 276 */       this.layerNameBtns.add(nameBtn);
/*     */ 
/* 278 */       Button dispBtn = new Button(layersComp, 32);
/* 279 */       dispBtn.setSelection(lyr.isOnOff());
/* 280 */       dispBtn.setData(Integer.valueOf(ii));
/* 281 */       dispBtn.addSelectionListener(new SelectionAdapter() {
/*     */         public void widgetSelected(SelectionEvent event) {
/* 283 */           PgenLayeringControlDialog.this.turnOnDisplay(Integer.parseInt(event.widget.getData().toString()));
/*     */         }
/*     */       });
/* 287 */       this.displayOnOffBtns.add(dispBtn);
/*     */ 
/* 289 */       if (!this.compact) {
/* 290 */         Button clrBtn = new Button(layersComp, 8);
/* 291 */         if (lyr.isMonoColor()) {
/* 292 */           clrBtn.setText("M/F");
/*     */         }
/*     */         else {
/* 295 */           clrBtn.setText("A/F");
/*     */         }
/*     */ 
/* 298 */         setButtonColor(clrBtn, lyr.getColor());
/* 299 */         clrBtn.setData(Integer.valueOf(ii));
/*     */ 
/* 301 */         clrBtn.addSelectionListener(new SelectionAdapter() {
/*     */           public void widgetSelected(SelectionEvent event) {
/* 303 */             PgenLayeringControlDialog.this.colorModeBtnInUse = Integer.parseInt(event.widget.getData().toString());
/* 304 */             PgenLayeringControlDialog.this.editDisplayAttr();
/*     */           }
/*     */         });
/* 309 */         this.colorModeBtns.add(clrBtn);
/*     */       }
/*     */ 
/* 313 */       ii++;
/*     */     }
/*     */ 
/* 317 */     if ((this.layerInUse < 0) || (this.layerInUse >= this.layerList.size())) {
/* 318 */       this.layerInUse = (this.layerList.size() - 1);
/*     */     }
/* 320 */     if (this.layerList.contains(this.drawingLayer.getActiveLayer())) {
/* 321 */       this.layerInUse = this.layerList.indexOf(this.drawingLayer.getActiveLayer());
/*     */     }
/*     */ 
/* 324 */     setButtonColor((Button)this.layerNameBtns.get(this.layerInUse), this.activeLayerButtonColor);
/* 325 */     ((Button)this.displayOnOffBtns.get(this.layerInUse)).setSelection(true);
/* 326 */     ((Layer)this.layerList.get(this.layerInUse)).setInUse(true);
/*     */ 
/* 328 */     this.currentLayer = ((Layer)this.layerList.get(this.layerInUse));
/* 329 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/*     */   }
/*     */ 
/*     */   public void initialize()
/*     */   {
/* 339 */     if (this.currentProduct == null)
/*     */     {
/* 341 */       this.currentProduct = new Product("Default", "Default", "Default", 
/* 342 */         new ProductInfo(), new ProductTime(), new ArrayList());
/*     */ 
/* 344 */       this.drawingLayer.addProduct(this.currentProduct);
/* 345 */       this.drawingLayer.setActiveProduct(this.currentProduct);
/*     */     }
/*     */ 
/* 348 */     if (this.currentLayer == null)
/*     */     {
/* 350 */       this.currentLayer = new Layer();
/*     */ 
/* 352 */       this.currentProduct.addLayer(this.currentLayer);
/*     */ 
/* 354 */       this.drawingLayer.setActiveLayer(this.currentLayer);
/*     */     }
/*     */ 
/* 357 */     if (this.currentLayer.getName().equalsIgnoreCase("Default")) {
/* 358 */       this.currentLayer.setName("Layer_1");
/*     */     }
/*     */ 
/* 361 */     this.currentLayer.setInUse(true);
/*     */ 
/* 363 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*     */ 
/* 365 */     this.layerNameBtns = new ArrayList();
/* 366 */     this.displayOnOffBtns = new ArrayList();
/* 367 */     this.colorModeBtns = new ArrayList();
/*     */   }
/*     */ 
/*     */   protected void updateActiveLayerName(String name)
/*     */   {
/* 377 */     boolean update = false;
/*     */ 
/* 386 */     if ((name != null) && (name.length() > 0)) {
/* 387 */       update = true;
/* 388 */       for (Layer lyr : this.currentProduct.getLayers()) {
/* 389 */         if (lyr.getName().equals(name)) {
/* 390 */           update = false;
/* 391 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 395 */       if (name.equalsIgnoreCase("Default")) {
/* 396 */         update = false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 405 */     if (update)
/*     */     {
/* 407 */       ((Button)this.layerNameBtns.get(this.layerInUse)).setText(name.replace("&", "&&"));
/*     */ 
/* 409 */       if (this.layerInUse >= 0) {
/* 410 */         ((Layer)this.layerList.get(this.layerInUse)).setName(name.replace("&", "&&"));
/* 411 */         this.currentLayer.setName(name);
/*     */       }
/*     */ 
/* 414 */       this.drawingLayer.setActiveLayer(this.currentLayer);
/*     */ 
/* 416 */       this.openNameDialog = false;
/*     */ 
/* 418 */       startLayering();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void updateDisplayAttr(boolean mono, Color clr, boolean fill)
/*     */   {
/* 428 */     if (this.colorModeBtnInUse >= 0)
/*     */     {
/* 430 */       if (mono) {
/* 431 */         ((Button)this.colorModeBtns.get(this.colorModeBtnInUse)).setText("M/F");
/*     */       }
/*     */       else {
/* 434 */         ((Button)this.colorModeBtns.get(this.colorModeBtnInUse)).setText("A/F");
/*     */       }
/*     */ 
/* 437 */       ((Layer)this.layerList.get(this.colorModeBtnInUse)).setMonoColor(mono);
/* 438 */       ((Layer)this.layerList.get(this.colorModeBtnInUse)).setColor(clr);
/*     */ 
/* 440 */       setButtonColor((Button)this.colorModeBtns.get(this.colorModeBtnInUse), clr);
/*     */ 
/* 442 */       ((Layer)this.layerList.get(this.colorModeBtnInUse)).setFilled(fill);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Layer getActiveLayer()
/*     */   {
/* 453 */     return this.currentLayer;
/*     */   }
/*     */ 
/*     */   protected Layer getLayerForColorMode()
/*     */   {
/* 462 */     return (Layer)this.layerList.get(this.colorModeBtnInUse);
/*     */   }
/*     */ 
/*     */   private void editLayerName()
/*     */   {
/* 474 */     Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/* 475 */     if (this.layerNameDlg == null) this.layerNameDlg = new PgenLayeringNameDialog(shell, this);
/*     */ 
/* 477 */     if ((this.layerNameDlg != null) && (!this.layerNameDlg.isOpen()))
/* 478 */       this.layerNameDlg.open();
/*     */   }
/*     */ 
/*     */   private void editDisplayAttr()
/*     */   {
/* 491 */     Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/* 492 */     if (this.displayDlg == null) this.displayDlg = new PgenLayeringDisplayDialog(shell, this);
/*     */ 
/* 494 */     if (this.displayDlg.isOpen()) {
/* 495 */       this.displayDlg.close();
/*     */     }
/*     */ 
/* 498 */     this.displayDlg.open();
/*     */   }
/*     */ 
/*     */   private void addLayer()
/*     */   {
/* 509 */     this.openNameDialog = true;
/*     */ 
/* 512 */     int size1 = this.layerList.size() + 1;
/* 513 */     String name = new String("Layer_" + size1);
/*     */ 
/* 516 */     for (int ii = 0; ii < this.layerList.size(); ii++) {
/* 517 */       if (name.equals(((Layer)this.layerList.get(ii)).getName())) {
/* 518 */         name = new String("Layer_" + size1++);
/* 519 */         ii = 0;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 524 */     this.currentLayer = new Layer();
/* 525 */     this.currentLayer.setName(name);
/*     */ 
/* 527 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/* 528 */     this.currentProduct.addLayer(this.currentLayer);
/*     */ 
/* 530 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*     */ 
/* 532 */     this.layerInUse = (this.layerList.size() - 1);
/*     */ 
/* 535 */     startLayering();
/*     */   }
/*     */ 
/*     */   public void switchLayer(String newLayer)
/*     */   {
/* 543 */     String clayer = ((Layer)this.layerList.get(this.layerInUse)).getName();
/* 544 */     int which = -1;
/*     */ 
/* 546 */     if (!newLayer.equals(clayer)) {
/* 547 */       for (int ii = 0; ii < this.layerNameBtns.size(); ii++) {
/* 548 */         if (((Button)this.layerNameBtns.get(ii)).getText().replace("&&", "&").equals(newLayer)) {
/* 549 */           which = ii;
/* 550 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 554 */       if (which >= 0)
/* 555 */         switchLayer(which);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void switchLayer(int which)
/*     */   {
/* 568 */     setButtonColor((Button)this.layerNameBtns.get(this.layerInUse), this.defaultLayerButtonColor);
/*     */ 
/* 570 */     this.layerInUse = which;
/*     */ 
/* 572 */     setButtonColor((Button)this.layerNameBtns.get(this.layerInUse), this.activeLayerButtonColor);
/*     */ 
/* 576 */     ((Layer)this.layerList.get(this.layerInUse)).setInUse(true);
/*     */ 
/* 579 */     if (this.layerNameDlg != null) this.layerNameDlg.close();
/* 580 */     if (this.displayDlg != null) this.displayDlg.close();
/* 581 */     this.openNameDialog = false;
/*     */ 
/* 583 */     this.currentLayer = ((Layer)this.layerList.get(this.layerInUse));
/*     */ 
/* 587 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/*     */ 
/* 589 */     this.drawingLayer.removeGhostLine();
/*     */ 
/* 591 */     if (GfaAttrDlg.getInstance(getParent()).isGfaOpen()) {
/* 592 */       if (this.drawingLayer.getSelectedDE() != null) {
/* 593 */         GfaAttrDlg.getInstance(getParent()).close();
/*     */       }
/*     */       else {
/* 596 */         GfaAttrDlg.getInstance(getParent()).switchHazard(this.currentLayer.getName());
/*     */       }
/*     */     }
/*     */     else {
/* 600 */       PgenUtil.setSelectingMode();
/*     */     }
/*     */ 
/* 603 */     this.drawingLayer.removeSelected();
/*     */ 
/* 606 */     PgenSession.getInstance().disableUndoRedo();
/*     */ 
/* 608 */     PgenUtil.refresh();
/*     */   }
/*     */ 
/*     */   private void turnOnDisplay(int which)
/*     */   {
/* 617 */     if (which == this.layerInUse) {
/* 618 */       ((Button)this.displayOnOffBtns.get(which)).setSelection(true);
/*     */     }
/*     */ 
/* 621 */     ((Layer)this.layerList.get(which)).setOnOff(((Button)this.displayOnOffBtns.get(which)).getSelection());
/*     */ 
/* 623 */     PgenUtil.refresh();
/*     */   }
/*     */ 
/*     */   private void updateDisplayChecks()
/*     */   {
/* 632 */     if (this.allOnOff) {
/* 633 */       this.allOnOff = false;
/* 634 */       this.allOnOffBtn.setText("All On");
/*     */     }
/*     */     else {
/* 637 */       this.allOnOff = true;
/* 638 */       this.allOnOffBtn.setText("All Off");
/*     */     }
/*     */ 
/* 641 */     for (int ii = 0; ii < this.layerList.size(); ii++) {
/* 642 */       ((Button)this.displayOnOffBtns.get(ii)).setSelection(this.allOnOff);
/* 643 */       ((Layer)this.layerList.get(ii)).setOnOff(this.allOnOff);
/*     */     }
/*     */ 
/* 653 */     PgenUtil.refresh();
/*     */   }
/*     */ 
/*     */   protected String getColorModeLayerName()
/*     */   {
/* 663 */     if ((this.layerList != null) && (this.colorModeBtnInUse >= 0)) {
/* 664 */       return ((Layer)this.layerList.get(this.colorModeBtnInUse)).getName();
/*     */     }
/*     */ 
/* 667 */     return null;
/*     */   }
/*     */ 
/*     */   private void exitLayering()
/*     */   {
/* 679 */     this.currentLayer = new Layer();
/*     */ 
/* 681 */     for (int ii = 0; ii < this.currentProduct.getLayers().size(); ii++) {
/* 682 */       this.currentLayer.add(this.currentProduct.getLayer(ii).getDrawables());
/*     */     }
/*     */ 
/* 688 */     this.currentProduct.clear();
/*     */ 
/* 690 */     this.currentProduct.addLayer(this.currentLayer);
/*     */ 
/* 695 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/* 696 */     this.drawingLayer.setActiveProduct(this.currentProduct);
/*     */ 
/* 698 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*     */ 
/* 703 */     PgenUtil.refresh();
/*     */ 
/* 705 */     PgenSession.getInstance().disableUndoRedo();
/*     */ 
/* 710 */     if (this.layerNameDlg != null) this.layerNameDlg.close();
/* 711 */     if (this.displayDlg != null) this.displayDlg.close();
/*     */ 
/* 713 */     close();
/*     */   }
/*     */ 
/*     */   private void addSeparator()
/*     */   {
/* 722 */     GridData gd = new GridData(768);
/* 723 */     Label sepLbl = new Label(this.shell, 258);
/* 724 */     sepLbl.setLayoutData(gd);
/*     */   }
/*     */ 
/*     */   private void deleteLayer()
/*     */   {
/* 733 */     this.openNameDialog = false;
/*     */ 
/* 736 */     if (this.currentProduct.getLayers().size() > 1) {
/* 737 */       this.currentProduct.removeLayer(this.layerInUse);
/*     */     }
/*     */ 
/* 740 */     if (this.layerInUse >= this.currentProduct.getLayers().size()) {
/* 741 */       this.layerInUse -= 1;
/*     */     }
/*     */ 
/* 744 */     this.currentLayer = this.currentProduct.getLayer(this.layerInUse);
/*     */ 
/* 746 */     this.currentLayer.setOnOff(true);
/*     */ 
/* 748 */     this.drawingLayer.setActiveLayer(this.currentLayer);
/*     */ 
/* 750 */     this.layerList = ((ArrayList)this.currentProduct.getLayers());
/*     */ 
/* 753 */     startLayering();
/*     */ 
/* 755 */     PgenUtil.refresh();
/*     */   }
/*     */ 
/*     */   private void startLayering()
/*     */   {
/* 765 */     if (isOpen()) {
/* 766 */       this.shell.dispose();
/*     */     }
/*     */ 
/* 770 */     if ((this.layerNameDlg != null) && (this.layerNameDlg.isOpen())) {
/* 771 */       this.layerNameDlg.close();
/* 772 */       this.layerNameDlg = null;
/*     */     }
/*     */ 
/* 776 */     open();
/*     */   }
/*     */ 
/*     */   protected void exit()
/*     */   {
/* 784 */     exitLayering();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.layering.PgenLayeringControlDialog
 * JD-Core Version:    0.6.2
 */