/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenGfaFormatTool;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import org.eclipse.jface.dialogs.IDialogConstants;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.events.SelectionListener;
/*     */ import org.eclipse.swt.graphics.Font;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class GfaFormatAttrDlg extends AttrDlg
/*     */ {
/*     */   private static final String ZULU = "ZULU";
/*     */   private static final String TANGO = "TANGO";
/*     */   private static final String SIERRA = "SIERRA";
/*     */   private static final String EAST = "EAST";
/*     */   private static final String CENTRAL = "CENTRAL";
/*     */   private static final String WEST = "WEST";
/*     */   static GfaFormatAttrDlg instance;
/*     */   private Composite top;
/*     */   private Button nrmlBtn;
/*     */   private Button testBtn;
/*     */   private static final int BTN_WIDTH = 90;
/*     */   private static final int BTN_HEIGHT = 23;
/*     */   private static final int TEXT_WIDTH = 660;
/*     */   private static final int TEXT_HEIGHT = 300;
/* 103 */   private static Font txtFt = null;
/*     */   private static final String SAVE_LABEL = "Generate/Save";
/*     */   private Button westBtn;
/*     */   private Button slcBtn;
/*     */   private Button sfoBtn;
/*     */   private Button centralBtn;
/*     */   private Button chiBtn;
/*     */   private Button dfwBtn;
/*     */   private Button eastBtn;
/*     */   private Button bosBtn;
/*     */   private Button miaBtn;
/*     */   private Button sierraBtn;
/*     */   private Button tangoBtn;
/*     */   private Button zuluBtn;
/* 117 */   private boolean lastNrml = true;
/*     */   private boolean lastWest;
/*     */   private boolean lastSlc;
/*     */   private boolean lastSfo;
/*     */   private boolean lastCentral;
/*     */   private boolean lastChi;
/*     */   private boolean lastDfw;
/*     */   private boolean lastEast;
/*     */   private boolean lastBos;
/*     */   private boolean lastMia;
/*     */   private boolean lastSierra;
/*     */   private boolean lastTango;
/*     */   private boolean lastZulu;
/*     */   private Text text;
/*     */   private PgenGfaFormatTool pgenGfaFormatTool;
/*     */ 
/*     */   private GfaFormatAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 137 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static GfaFormatAttrDlg getInstance(Shell parShell)
/*     */   {
/* 149 */     if (instance == null) {
/*     */       try {
/* 151 */         instance = new GfaFormatAttrDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/* 155 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 159 */     return instance;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 172 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 175 */     GridLayout mainLayout = new GridLayout(1, false);
/* 176 */     mainLayout.marginHeight = 3;
/* 177 */     mainLayout.marginWidth = 3;
/* 178 */     this.top.setLayout(mainLayout);
/*     */ 
/* 181 */     initializeComponents();
/*     */ 
/* 183 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 193 */     getShell().setText("AIRMET Format");
/*     */ 
/* 195 */     createFirstRowBtns();
/*     */ 
/* 197 */     createWCEBtns();
/*     */ 
/* 199 */     createSierraTangoZuluBtns();
/*     */ 
/* 201 */     createTextArea();
/*     */ 
/* 203 */     addSelectionlisteners();
/*     */   }
/*     */ 
/*     */   private void createFirstRowBtns() {
/* 207 */     Composite comp = createComposite();
/*     */ 
/* 209 */     this.nrmlBtn = new Button(comp, 16);
/* 210 */     this.nrmlBtn.setSelection(this.lastNrml);
/* 211 */     this.nrmlBtn.setText("NRML");
/* 212 */     this.testBtn = new Button(comp, 16);
/* 213 */     this.testBtn.setSelection(!this.lastNrml);
/* 214 */     this.testBtn.setText("TEST");
/*     */   }
/*     */ 
/*     */   private Composite createComposite()
/*     */   {
/* 219 */     Group group = new Group(this.top, 0);
/* 220 */     GridData gridData = new GridData(1);
/* 221 */     gridData.horizontalAlignment = 4;
/* 222 */     group.setLayoutData(gridData);
/* 223 */     GridLayout layout = new GridLayout(2, false);
/* 224 */     layout.marginHeight = 0;
/* 225 */     layout.marginWidth = 0;
/* 226 */     group.setLayout(layout);
/*     */ 
/* 228 */     Composite comp = new Composite(group, 0);
/* 229 */     layout = new GridLayout(3, false);
/* 230 */     layout.marginHeight = 3;
/* 231 */     layout.marginWidth = 3;
/* 232 */     comp.setLayout(layout);
/* 233 */     comp.setLayoutData(new GridData(16777216, -1, true, false));
/* 234 */     return comp;
/*     */   }
/*     */ 
/*     */   private void createWCEBtns()
/*     */   {
/* 239 */     Composite comp = createComposite();
/*     */ 
/* 241 */     this.westBtn = createCheckBtn(comp, "WEST", this.lastWest);
/* 242 */     this.centralBtn = createCheckBtn(comp, "CENTRAL", this.lastCentral);
/* 243 */     this.eastBtn = createCheckBtn(comp, "EAST", this.lastEast);
/* 244 */     this.slcBtn = createCheckBtn(comp, "SLC", this.lastSlc);
/* 245 */     this.chiBtn = createCheckBtn(comp, "CHI", this.lastChi);
/* 246 */     this.bosBtn = createCheckBtn(comp, "BOS", this.lastBos);
/* 247 */     this.sfoBtn = createCheckBtn(comp, "SFO", this.lastSfo);
/* 248 */     this.dfwBtn = createCheckBtn(comp, "DFW", this.lastDfw);
/* 249 */     this.miaBtn = createCheckBtn(comp, "MIA", this.lastMia);
/*     */   }
/*     */ 
/*     */   private Button createCheckBtn(Composite comp, String str, boolean lastUsed) {
/* 253 */     Button btn = new Button(comp, 32);
/* 254 */     btn.setText(str);
/* 255 */     GridData gd = new GridData(90, 23);
/* 256 */     btn.setLayoutData(gd);
/* 257 */     btn.setSelection(lastUsed);
/* 258 */     return btn;
/*     */   }
/*     */ 
/*     */   private void createSierraTangoZuluBtns() {
/* 262 */     Composite comp = createComposite();
/*     */ 
/* 264 */     this.sierraBtn = createCheckBtn(comp, "SIERRA", this.lastSierra);
/* 265 */     this.tangoBtn = createCheckBtn(comp, "TANGO", this.lastTango);
/* 266 */     this.zuluBtn = createCheckBtn(comp, "ZULU", this.lastZulu);
/*     */   }
/*     */ 
/*     */   private void createTextArea() {
/* 270 */     int style = 2626;
/* 271 */     this.text = new Text(this.top, style);
/* 272 */     GridData gd = new GridData(660, 300);
/* 273 */     gd.verticalAlignment = 1;
/* 274 */     this.text.setLayoutData(gd);
/* 275 */     this.text.setEditable(false);
/* 276 */     this.text.addTraverseListener(new GfaAttrDlg.TraverseListenerTab());
/*     */ 
/* 278 */     if (txtFt == null) {
/* 279 */       txtFt = new Font(getShell().getDisplay(), "Courier New", 12, 
/* 280 */         0);
/*     */     }
/* 282 */     this.text.setFont(txtFt);
/*     */   }
/*     */ 
/*     */   private void addSelectionlisteners() {
/* 286 */     new ChkBtnSelectionListener(this.westBtn, this.slcBtn, this.sfoBtn);
/* 287 */     new ChkBtnSelectionListener(this.centralBtn, this.chiBtn, this.dfwBtn);
/* 288 */     new ChkBtnSelectionListener(this.eastBtn, this.bosBtn, this.miaBtn);
/*     */ 
/* 291 */     SelectionListener listener = new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 294 */         GfaFormatAttrDlg.this.updateLastUsed();
/*     */       }
/*     */     };
/* 298 */     this.nrmlBtn.addSelectionListener(listener);
/* 299 */     this.testBtn.addSelectionListener(listener);
/*     */ 
/* 301 */     this.sierraBtn.addSelectionListener(listener);
/* 302 */     this.tangoBtn.addSelectionListener(listener);
/* 303 */     this.zuluBtn.addSelectionListener(listener);
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 309 */     super.createButtonsForButtonBar(parent);
/* 310 */     getButton(0).setText("Generate/Save");
/* 311 */     getButton(1).setText(IDialogConstants.CANCEL_LABEL);
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 321 */     String dataURI = null;
/* 322 */     ArrayList areas = getChecked();
/*     */ 
/* 324 */     ArrayList categories = getSelectedCategories();
/*     */ 
/* 326 */     ArrayList all = new ArrayList();
/* 327 */     Product prod = null;
/* 328 */     if (this.drawingLayer != null) {
/* 329 */       prod = this.drawingLayer.getActiveProduct();
/* 330 */       for (Layer layer : prod.getLayers())
/*     */       {
/* 332 */         all.addAll(layer.getDrawables());
/*     */       }
/*     */     }
/*     */ 
/* 336 */     if (prod != null) {
/*     */       try {
/* 338 */         prod.setOutputFile(this.drawingLayer.buildActivityLabel(prod));
/* 339 */         dataURI = StorageUtils.storeProduct(prod);
/*     */       } catch (PgenStorageException e) {
/* 341 */         StorageUtils.showError(e);
/* 342 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 346 */     ArrayList allGfa = new ArrayList();
/* 347 */     for (AbstractDrawableComponent adc : all) {
/* 348 */       if (((adc instanceof Gfa)) && (!((Gfa)adc).isSnapshot())) {
/* 349 */         allGfa.add((Gfa)adc);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 356 */       StringBuilder sb = this.pgenGfaFormatTool.generate(this.drawingLayer, allGfa, areas, 
/* 357 */         categories, dataURI);
/* 358 */       this.text.setText(sb.toString());
/*     */     } catch (IOException e) {
/* 360 */       this.text.setText("I/O Error");
/*     */ 
/* 362 */       e.printStackTrace();
/*     */     } catch (JAXBException e) {
/* 364 */       this.text.setText("Serialization Error");
/*     */ 
/* 366 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 369 */     getButton(0).setEnabled(false);
/*     */   }
/*     */ 
/*     */   private ArrayList<String> getChecked() {
/* 373 */     ArrayList checked = new ArrayList();
/* 374 */     if (this.slcBtn.getSelection())
/* 375 */       checked.add("SLC");
/* 376 */     if (this.sfoBtn.getSelection())
/* 377 */       checked.add("SFO");
/* 378 */     if (this.chiBtn.getSelection())
/* 379 */       checked.add("CHI");
/* 380 */     if (this.dfwBtn.getSelection())
/* 381 */       checked.add("DFW");
/* 382 */     if (this.bosBtn.getSelection())
/* 383 */       checked.add("BOS");
/* 384 */     if (this.miaBtn.getSelection())
/* 385 */       checked.add("MIA");
/* 386 */     return checked;
/*     */   }
/*     */ 
/*     */   private ArrayList<String> getSelectedCategories() {
/* 390 */     ArrayList cats = new ArrayList();
/* 391 */     if (this.sierraBtn.getSelection())
/* 392 */       cats.add("SIERRA");
/* 393 */     if (this.tangoBtn.getSelection())
/* 394 */       cats.add("TANGO");
/* 395 */     if (this.zuluBtn.getSelection())
/* 396 */       cats.add("ZULU");
/* 397 */     return cats;
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 402 */     super.cancelPressed();
/* 403 */     PgenUtil.setSelectingMode();
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 411 */     if (getShell() == null) {
/* 412 */       create();
/*     */     }
/* 414 */     if (this.shellLocation == null) {
/* 415 */       this.shellLocation = centerOfParent();
/*     */     }
/*     */ 
/* 418 */     int op = super.open();
/* 419 */     super.enableButtons();
/*     */ 
/* 421 */     return op;
/*     */   }
/*     */ 
/*     */   public Point centerOfParent() {
/* 425 */     Rectangle parentSize = getParentShell().getBounds();
/* 426 */     Rectangle mySize = getShell().getBounds();
/*     */ 
/* 429 */     int locationX = (parentSize.width - mySize.width) / 2 + parentSize.x;
/* 430 */     int locationY = (parentSize.height - mySize.height) / 2 + parentSize.y;
/*     */ 
/* 432 */     return new Point(locationX, locationY);
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/* 440 */     HashMap attr = new HashMap();
/*     */ 
/* 442 */     return attr;
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute attr)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void updateLastUsed()
/*     */   {
/* 452 */     this.lastNrml = this.nrmlBtn.getSelection();
/* 453 */     this.lastWest = this.westBtn.getSelection();
/* 454 */     this.lastSlc = this.slcBtn.getSelection();
/* 455 */     this.lastSfo = this.sfoBtn.getSelection();
/* 456 */     this.lastCentral = this.centralBtn.getSelection();
/* 457 */     this.lastChi = this.chiBtn.getSelection();
/* 458 */     this.lastDfw = this.dfwBtn.getSelection();
/* 459 */     this.lastEast = this.eastBtn.getSelection();
/* 460 */     this.lastBos = this.bosBtn.getSelection();
/* 461 */     this.lastMia = this.miaBtn.getSelection();
/* 462 */     this.lastSierra = this.sierraBtn.getSelection();
/* 463 */     this.lastTango = this.tangoBtn.getSelection();
/* 464 */     this.lastZulu = this.zuluBtn.getSelection();
/*     */ 
/* 466 */     getButton(0).setEnabled(true);
/*     */   }
/*     */ 
/*     */   public void setGfaFormatTool(PgenGfaFormatTool pgenGfaFormatTool)
/*     */   {
/* 512 */     this.pgenGfaFormatTool = pgenGfaFormatTool;
/*     */   }
/*     */ 
/*     */   private class ChkBtnSelectionListener extends SelectionAdapter
/*     */   {
/*     */     private Button b1;
/*     */     private Button b2;
/*     */     private Button b3;
/*     */ 
/*     */     public ChkBtnSelectionListener(Button b1, Button b2, Button b3)
/*     */     {
/* 484 */       this.b1 = b1;
/* 485 */       this.b2 = b2;
/* 486 */       this.b3 = b3;
/* 487 */       b1.addSelectionListener(this);
/* 488 */       b2.addSelectionListener(this);
/* 489 */       b3.addSelectionListener(this);
/*     */     }
/*     */ 
/*     */     public void widgetSelected(SelectionEvent e)
/*     */     {
/* 494 */       super.widgetSelected(e);
/*     */ 
/* 496 */       Button source = (Button)e.getSource();
/*     */ 
/* 498 */       if (source == this.b1) {
/* 499 */         boolean select = this.b1.getSelection();
/* 500 */         this.b2.setSelection(select);
/* 501 */         this.b3.setSelection(select);
/*     */       } else {
/* 503 */         boolean select = (this.b2.getSelection()) && (this.b3.getSelection());
/* 504 */         this.b1.setSelection(select);
/*     */       }
/*     */ 
/* 507 */       GfaFormatAttrDlg.this.updateLastUsed();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.GfaFormatAttrDlg
 * JD-Core Version:    0.6.2
 */