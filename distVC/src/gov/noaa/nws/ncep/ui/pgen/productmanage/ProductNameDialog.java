/*     */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenLayer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenSave;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.FileDialog;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Menu;
/*     */ import org.eclipse.swt.widgets.MenuItem;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.swt.widgets.ToolBar;
/*     */ import org.eclipse.swt.widgets.ToolItem;
/*     */ 
/*     */ public class ProductNameDialog extends ProductDialog
/*     */ {
/*  63 */   private Text nameText = null;
/*  64 */   private Combo typeCombo = null;
/*  65 */   private Composite typeComp = null;
/*  66 */   private Text typeText = null;
/*  67 */   private ToolBar typeToolBar = null;
/*  68 */   private Text forecasterText = null;
/*  69 */   private Text centerText = null;
/*  70 */   private Button saveLayerBtn = null;
/*  71 */   private Text outputFileTxt = null;
/*  72 */   private Group layersGrp = null;
/*     */ 
/*  74 */   private String initialOutput = null;
/*     */ 
/*  76 */   private ProductManageDialog prdManageDlg = null;
/*     */ 
/*     */   public ProductNameDialog(Shell parentShell, ProductManageDialog dlg)
/*     */   {
/*  84 */     super(parentShell);
/*     */ 
/*  86 */     this.prdManageDlg = dlg;
/*     */   }
/*     */ 
/*     */   public void setTitle()
/*     */   {
/*  94 */     this.shell.setText("Edit Product");
/*     */   }
/*     */ 
/*     */   public void setLayout()
/*     */   {
/* 102 */     GridLayout mainLayout = new GridLayout(1, true);
/* 103 */     mainLayout.marginHeight = 1;
/* 104 */     mainLayout.marginWidth = 1;
/* 105 */     this.shell.setLayout(mainLayout);
/*     */   }
/*     */ 
/*     */   public void setDefaultLocation(Shell parent)
/*     */   {
/* 116 */     if (this.shellLocation == null) {
/* 117 */       Point pt = parent.getLocation();
/* 118 */       this.shell.setLocation(pt.x + 420, pt.y + 200);
/*     */     } else {
/* 120 */       this.shell.setLocation(this.shellLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void initializeComponents()
/*     */   {
/* 132 */     this.initialOutput = this.prdManageDlg.getActiveProduct().getOutputFile();
/*     */ 
/* 134 */     Composite main = new Composite(this.shell, 0);
/* 135 */     GridLayout gl1 = new GridLayout(1, false);
/* 136 */     main.setLayout(gl1);
/*     */ 
/* 138 */     Composite top = new Composite(main, 0);
/* 139 */     GridLayout gl = new GridLayout(2, false);
/* 140 */     top.setLayout(gl);
/*     */ 
/* 142 */     GridData gd = new GridData(4, -1, true, false);
/*     */ 
/* 144 */     Label pname = new Label(top, 0);
/* 145 */     pname.setText("Name:");
/*     */ 
/* 147 */     this.nameText = new Text(top, 2052);
/* 148 */     this.nameText.setLayoutData(new GridData(100, 20));
/* 149 */     this.nameText.setEditable(true);
/* 150 */     this.nameText.setText(this.prdManageDlg.getActiveProduct().getName());
/*     */ 
/* 152 */     Label ptype = new Label(top, 0);
/* 153 */     ptype.setText("Type:");
/*     */ 
/* 155 */     String curType = this.prdManageDlg.getActiveProduct().getType();
/*     */ 
/* 157 */     this.typeComp = new Composite(top, 16384);
/* 158 */     this.typeComp.setLayout(new GridLayout(2, false));
/*     */ 
/* 160 */     this.typeText = new Text(this.typeComp, 18432);
/* 161 */     this.typeText.setSize(300, 20);
/* 162 */     this.typeText.setText(curType);
/* 163 */     this.typeText.setData(curType);
/* 164 */     this.typeText.setEditable(false);
/*     */ 
/* 166 */     this.typeToolBar = new ToolBar(this.typeComp, 256);
/* 167 */     final ToolItem ti = new ToolItem(this.typeToolBar, 4);
/*     */ 
/* 169 */     ti.setEnabled(true);
/*     */ 
/* 171 */     final Menu mu = new Menu(this.shell.getShell(), 8);
/*     */ 
/* 173 */     MenuItem mi1 = new MenuItem(mu, 8, 0);
/* 174 */     mi1.setText("Default");
/* 175 */     mi1.setData("Default");
/* 176 */     mi1.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 178 */         ProductNameDialog.this.typeText.setText(((MenuItem)e.widget).getData().toString());
/* 179 */         ProductNameDialog.this.typeText.pack();
/* 180 */         ProductNameDialog.this.typeComp.pack();
/* 181 */         ProductNameDialog.this.typeComp.getParent().pack();
/* 182 */         ProductNameDialog.this.switchProductType("Default");
/*     */       }
/*     */     });
/* 186 */     int ntyp = 1;
/* 187 */     ArrayList typeUsed = new ArrayList();
/* 188 */     for (String ptypName : this.prdManageDlg.prdTypesMap.keySet())
/*     */     {
/* 190 */       ProductType prdType = (ProductType)this.prdManageDlg.prdTypesMap.get(ptypName);
/* 191 */       LinkedHashMap subtypesNalias = this.prdManageDlg.getSubtypes(prdType.getType(), true);
/*     */ 
/* 193 */       if (((ptypName.equals(prdType.getName())) && 
/* 194 */         (!prdType.getType().equals(prdType.getName()))) || 
/* 195 */         (!this.prdManageDlg.hasSubtypes(subtypesNalias.values())))
/*     */       {
/* 197 */         MenuItem typeItem = new MenuItem(mu, 8, ntyp);
/*     */ 
/* 199 */         typeItem.setText(ptypName);
/* 200 */         typeItem.setData(ptypName);
/* 201 */         typeItem.addSelectionListener(new SelectionAdapter() {
/*     */           public void widgetSelected(SelectionEvent e) {
/* 203 */             String typeName = ((MenuItem)e.widget).getData().toString();
/* 204 */             ProductNameDialog.this.typeText.setText(typeName);
/* 205 */             ProductNameDialog.this.typeText.pack();
/* 206 */             ProductNameDialog.this.typeComp.pack();
/* 207 */             ProductNameDialog.this.typeComp.getParent().pack();
/* 208 */             ProductNameDialog.this.switchProductType(typeName);
/*     */           }
/*     */ 
/*     */         });
/*     */       }
/*     */       else
/*     */       {
/* 215 */         if (typeUsed.contains(prdType.getType()))
/*     */         {
/*     */           continue;
/*     */         }
/* 219 */         typeUsed.add(prdType.getType());
/*     */ 
/* 223 */         MenuItem typeItem = new MenuItem(mu, 64, ntyp);
/*     */ 
/* 225 */         typeItem.setText(prdType.getType());
/* 226 */         Menu submenu = new Menu(typeItem);
/* 227 */         typeItem.setMenu(submenu);
/*     */ 
/* 229 */         for (String styp : subtypesNalias.keySet()) {
/* 230 */           MenuItem subtypeItem = new MenuItem(submenu, 8);
/* 231 */           subtypeItem.setText((String)subtypesNalias.get(styp));
/*     */ 
/* 233 */           subtypeItem.setData(styp);
/*     */ 
/* 235 */           subtypeItem.addSelectionListener(new SelectionAdapter() {
/*     */             public void widgetSelected(SelectionEvent e) {
/* 237 */               String typeName = ((MenuItem)e.widget).getData().toString();
/* 238 */               ProductNameDialog.this.typeText.setText(typeName);
/* 239 */               ProductNameDialog.this.typeText.pack();
/* 240 */               ProductNameDialog.this.typeComp.pack();
/* 241 */               ProductNameDialog.this.typeComp.getParent().pack();
/*     */ 
/* 243 */               ProductNameDialog.this.switchProductType(typeName);
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */ 
/* 249 */       ntyp++;
/*     */     }
/*     */ 
/* 252 */     ti.addListener(13, new Listener() {
/*     */       public void handleEvent(Event event) {
/* 254 */         Rectangle bounds = ti.getBounds();
/* 255 */         Point point = ProductNameDialog.this.typeToolBar.toDisplay(bounds.x, bounds.y + bounds.height);
/* 256 */         mu.setLocation(point);
/* 257 */         mu.setVisible(true);
/*     */       }
/*     */     });
/* 261 */     Label pfst = new Label(top, 0);
/* 262 */     pfst.setText("Forecaster:");
/*     */ 
/* 264 */     this.forecasterText = new Text(top, 2052);
/* 265 */     this.forecasterText.setLayoutData(new GridData(100, 20));
/* 266 */     this.forecasterText.setEditable(true);
/* 267 */     this.forecasterText.setText(this.prdManageDlg.getActiveProduct().getForecaster());
/*     */ 
/* 269 */     Label pcnt = new Label(top, 0);
/* 270 */     pcnt.setText("Center:");
/*     */ 
/* 272 */     this.centerText = new Text(top, 2052);
/* 273 */     this.centerText.setLayoutData(new GridData(100, 20));
/* 274 */     this.centerText.setEditable(true);
/* 275 */     this.centerText.setText(this.prdManageDlg.getActiveProduct().getCenter());
/*     */ 
/* 277 */     Group typeInfoGrp = new Group(main, 4);
/* 278 */     typeInfoGrp.setLayout(new GridLayout(1, false));
/* 279 */     typeInfoGrp.setText("Product Type Information");
/*     */ 
/* 281 */     Composite bot1 = new Composite(typeInfoGrp, 0);
/* 282 */     bot1.setLayout(new GridLayout(2, false));
/*     */ 
/* 284 */     Composite bot2 = new Composite(typeInfoGrp, 0);
/* 285 */     bot2.setLayout(new GridLayout(3, false));
/*     */ 
/* 287 */     Label psave = new Label(bot1, 0);
/* 288 */     psave.setText("Save Layers:");
/*     */ 
/* 290 */     this.saveLayerBtn = new Button(bot1, 32);
/* 291 */     this.saveLayerBtn.setSelection(this.prdManageDlg.getActiveProduct().isSaveLayers());
/*     */ 
/* 294 */     Label outputLbl = new Label(bot2, 16384);
/* 295 */     outputLbl.setText("Output:");
/*     */ 
/* 297 */     this.outputFileTxt = new Text(bot2, 2052);
/* 298 */     this.outputFileTxt.setLayoutData(new GridData(150, 20));
/* 299 */     this.outputFileTxt.setEditable(true);
/*     */ 
/* 301 */     if (this.initialOutput != null) {
/* 302 */       this.outputFileTxt.setText(this.initialOutput);
/*     */     }
/*     */     else {
/* 305 */       this.outputFileTxt.setText("");
/*     */     }
/*     */ 
/* 308 */     Button browseBtn = new Button(bot2, 8);
/* 309 */     browseBtn.setText("Browse");
/*     */ 
/* 311 */     this.layersGrp = new Group(typeInfoGrp, 0);
/* 312 */     this.layersGrp.setLayout(new GridLayout(1, false));
/* 313 */     this.layersGrp.setText("Layers Defined");
/*     */ 
/* 315 */     String prevType = this.prdManageDlg.getActiveProduct().getType();
/* 316 */     createLayersTemplate(this.layersGrp, prevType);
/*     */ 
/* 318 */     browseBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 320 */         ProductNameDialog.this.createFileText(ProductNameDialog.this.outputFileTxt, ProductNameDialog.this.initialOutput);
/*     */       }
/*     */     });
/* 324 */     browseBtn.setEnabled(false);
/*     */ 
/* 327 */     Composite centeredComp = new Composite(this.shell, 0);
/* 328 */     GridLayout gl2 = new GridLayout(2, true);
/* 329 */     centeredComp.setLayout(gl2);
/* 330 */     centeredComp.setLayoutData(gd);
/*     */ 
/* 332 */     Button acceptBtn = new Button(centeredComp, 0);
/* 333 */     acceptBtn.setText("Accept");
/* 334 */     acceptBtn.setLayoutData(gd);
/* 335 */     acceptBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 337 */         ProductNameDialog.this.updateProduct();
/* 338 */         ProductNameDialog.this.close();
/*     */       }
/*     */     });
/* 342 */     Button cancelBtn = new Button(centeredComp, 0);
/* 343 */     cancelBtn.setText(" Close ");
/* 344 */     cancelBtn.setLayoutData(gd);
/* 345 */     cancelBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 347 */         ProductNameDialog.this.prdManageDlg.setOpenPrdNameDlg(false);
/* 348 */         ProductNameDialog.this.close();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void updateProduct()
/*     */   {
/* 362 */     HashMap attr = new HashMap();
/*     */ 
/* 364 */     attr.put("name", this.nameText.getText());
/* 365 */     attr.put("type", this.typeText.getText());
/* 366 */     attr.put("forecaster", this.forecasterText.getText());
/* 367 */     attr.put("center", this.centerText.getText());
/* 368 */     if (this.saveLayerBtn.getSelection()) {
/* 369 */       attr.put("saveLayers", "true");
/*     */     }
/*     */     else {
/* 372 */       attr.put("saveLayers", "false");
/*     */     }
/* 374 */     attr.put("outputfile", this.outputFileTxt.getText());
/*     */ 
/* 376 */     if (this.prdManageDlg != null)
/* 377 */       this.prdManageDlg.updateProductAttr(attr);
/*     */   }
/*     */ 
/*     */   private void selectProductType()
/*     */   {
/* 388 */     this.typeCombo.getText().equals(this.prdManageDlg.getActiveProduct().getType());
/*     */   }
/*     */ 
/*     */   private void createFileText(Text txt, String initialFile)
/*     */   {
/* 418 */     String[] filterNames = { "*.xml", "All Files (*)" };
/* 419 */     String[] filterExtensions = { "*.xml", "*" };
/*     */ 
/* 421 */     String filterPath = PgenUtil.getWorkingDirectory();
/* 422 */     String defaultFile = new String("default.xml");
/*     */ 
/* 424 */     if (initialFile != null) {
/* 425 */       int index = initialFile.lastIndexOf('/');
/* 426 */       if (index >= 0) {
/* 427 */         defaultFile = initialFile.substring(index + 1, initialFile.length());
/* 428 */         filterPath = initialFile.substring(0, index);
/*     */       }
/*     */       else {
/* 431 */         defaultFile = new String(initialFile);
/*     */       }
/*     */     }
/*     */ 
/* 435 */     String selectedFile = selectFile(this.shell, 4096, filterNames, 
/* 436 */       filterExtensions, filterPath, defaultFile, true);
/* 437 */     if (selectedFile != null)
/* 438 */       txt.setText(selectedFile);
/*     */   }
/*     */ 
/*     */   private String selectFile(Shell sh, int mode, String[] nameFilter, String[] extensionFilter, String pathFilter, String defaultFile, boolean overWrite)
/*     */   {
/* 459 */     FileDialog dialog = new FileDialog(sh, mode);
/* 460 */     dialog.setFilterNames(nameFilter);
/* 461 */     dialog.setFilterExtensions(extensionFilter);
/* 462 */     dialog.setFilterPath(pathFilter);
/* 463 */     if (defaultFile != null) {
/* 464 */       dialog.setFileName(defaultFile);
/*     */     }
/* 466 */     dialog.setOverwrite(overWrite);
/*     */ 
/* 468 */     return dialog.open();
/*     */   }
/*     */ 
/*     */   private void createLayersTemplate(Composite cmp, String typ)
/*     */   {
/* 477 */     Control[] wids = cmp.getChildren();
/* 478 */     for (int jj = 0; jj < wids.length; jj++) {
/* 479 */       wids[jj].dispose();
/*     */     }
/*     */ 
/* 482 */     cmp.pack();
/* 483 */     this.shell.pack(true);
/*     */ 
/* 485 */     ProductType prdtype = (ProductType)this.prdManageDlg.prdTypesMap.get(typ);
/* 486 */     if ((prdtype == null) || (prdtype.getPgenLayer() == null) || 
/* 487 */       (prdtype.getPgenLayer().size() <= 0)) {
/* 488 */       return;
/*     */     }
/*     */ 
/* 491 */     Composite layersComp = new Composite(cmp, 0);
/* 492 */     GridLayout gl = new GridLayout(3, false);
/*     */ 
/* 494 */     gl.marginHeight = 1;
/* 495 */     gl.marginWidth = 1;
/* 496 */     gl.verticalSpacing = 1;
/* 497 */     gl.horizontalSpacing = 10;
/*     */ 
/* 499 */     layersComp.setLayout(gl);
/*     */ 
/* 501 */     for (PgenLayer lyr : prdtype.getPgenLayer())
/*     */     {
/* 503 */       Button nameBtn = new Button(layersComp, 8);
/* 504 */       nameBtn.setText(lyr.getName());
/*     */ 
/* 506 */       Button dispBtn = new Button(layersComp, 32);
/* 507 */       dispBtn.setSelection(lyr.isOnOff().booleanValue());
/* 508 */       dispBtn.setEnabled(false);
/*     */ 
/* 510 */       Button clrBtn = new Button(layersComp, 8);
/* 511 */       clrBtn.setText("A/F");
/*     */ 
/* 513 */       java.awt.Color clr = new java.awt.Color(lyr.getColor().getRed(), 
/* 514 */         lyr.getColor().getGreen(), 
/* 515 */         lyr.getColor().getBlue(), 
/* 516 */         lyr.getColor().getAlpha().intValue());
/*     */ 
/* 518 */       setButtonColor(clrBtn, clr);
/*     */     }
/*     */ 
/* 521 */     cmp.pack();
/* 522 */     this.shell.pack(true);
/*     */   }
/*     */ 
/*     */   private void switchProductType(String newType)
/*     */   {
/* 531 */     createLayersTemplate(this.layersGrp, newType);
/*     */ 
/* 533 */     this.nameText.setText(newType);
/*     */ 
/* 535 */     this.saveLayerBtn.setSelection(false);
/* 536 */     this.outputFileTxt.setText("");
/* 537 */     this.outputFileTxt.setEditable(false);
/*     */ 
/* 539 */     ProductType ptyp = (ProductType)this.prdManageDlg.prdTypesMap.get(newType);
/* 540 */     if ((ptyp != null) && 
/* 541 */       (ptyp.getPgenSave() != null)) {
/* 542 */       if (ptyp.getPgenSave().getOutputFile() != null) {
/* 543 */         this.outputFileTxt.setText(ptyp.getPgenSave().getOutputFile());
/*     */       }
/* 545 */       if (ptyp.getPgenSave().isSaveLayers() != null)
/* 546 */         this.saveLayerBtn.setSelection(ptyp.getPgenSave().isSaveLayers().booleanValue());
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.ProductNameDialog
 * JD-Core Version:    0.6.2
 */