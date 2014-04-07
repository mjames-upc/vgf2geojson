/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.common.staticdata.SPCCounty;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IWatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox.WatchShape;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenWatchBoxDrawingTool;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenWatchBoxModifyTool;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.swt.events.KeyAdapter;
/*     */ import org.eclipse.swt.events.KeyEvent;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.layout.RowLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Slider;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class WatchBoxAttrDlg extends AttrDlg
/*     */   implements IWatchBox
/*     */ {
/*  80 */   private static WatchBoxAttrDlg INSTANCE = null;
/*     */   private WatchBox wb;
/*  86 */   private static final String[] SYMBOL_LIST = { "PLUS_SIGN", "TRIANGLE", 
/*  87 */     "UP_ARROW", "SMALL_X", "BOX", "OCTAGON" };
/*     */   private PgenWatchBoxModifyTool wbTool;
/*     */   private WatchInfoDlg infoDlg;
/*     */   private Composite top;
/*     */   private Label colorLbl;
/*     */   private ColorButtonSelector cs;
/*     */   private Button fillBtn;
/*     */   private ColorButtonSelector symbolColor;
/*     */   private Button nsBtn;
/*     */   private Button ewBtn;
/*     */   private Button esolBtn;
/*     */   private SymbolCombo symbolCombo;
/*     */   private Label widthLbl;
/*     */   private Slider widthSlider;
/*     */   private Text widthText;
/*     */   private Label sizeLbl;
/*     */   private Slider sizeSlider;
/*     */   private Text sizeText;
/*     */   private Button dispBtn;
/*     */ 
/*     */   protected WatchBoxAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 135 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static WatchBoxAttrDlg getInstance(Shell parShell)
/*     */   {
/* 148 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/* 152 */         INSTANCE = new WatchBoxAttrDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/* 156 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 161 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 171 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 174 */     GridLayout mainLayout = new GridLayout(1, false);
/* 175 */     mainLayout.marginHeight = 3;
/* 176 */     mainLayout.marginWidth = 3;
/* 177 */     this.top.setLayout(mainLayout);
/*     */ 
/* 180 */     initializeComponents();
/*     */ 
/* 182 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void initializeComponents()
/*     */   {
/* 191 */     getShell().setText("Watch Attributes");
/*     */ 
/* 193 */     createColorAttr();
/* 194 */     addSeparator(this.top);
/* 195 */     createShapeFillAttr();
/* 196 */     addSeparator(this.top);
/*     */ 
/* 198 */     createTypeAttr();
/* 199 */     createSizeAttr();
/* 200 */     createWidthAttr();
/*     */ 
/* 202 */     addSeparator(this.top);
/* 203 */     createDispBtn();
/*     */ 
/* 205 */     addSeparator(this.top);
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 217 */     Color[] colors = new Color[2];
/*     */ 
/* 219 */     colors[0] = new Color(this.cs.getColorValue().red, 
/* 220 */       this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*     */ 
/* 222 */     colors[1] = Color.green;
/*     */ 
/* 224 */     return colors;
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 233 */     return 1.5F;
/*     */   }
/*     */ 
/*     */   private void setColor(Color clr)
/*     */   {
/* 243 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute iattr)
/*     */   {
/* 253 */     if ((iattr instanceof IWatchBox)) {
/* 254 */       IWatchBox ia = (IWatchBox)iattr;
/* 255 */       Color clr = ia.getColors()[0];
/* 256 */       if (clr != null) {
/* 257 */         setColor(clr);
/*     */       }
/*     */ 
/* 260 */       Color fillClr = ia.getColors()[1];
/* 261 */       if (fillClr != null) {
/* 262 */         this.symbolColor.setColorValue(
/* 263 */           new RGB(fillClr.getRed(), fillClr.getGreen(), fillClr.getBlue()));
/*     */       }
/*     */ 
/* 266 */       this.fillBtn.setSelection(ia.getFillFlag());
/*     */ 
/* 268 */       setShapeBtn(ia.getWatchBoxShape());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setShapeBtn(WatchBox.WatchShape ws) {
/* 273 */     if (ws == WatchBox.WatchShape.NS) {
/* 274 */       this.nsBtn.setSelection(true);
/* 275 */       this.ewBtn.setSelection(false);
/* 276 */       this.esolBtn.setSelection(false);
/*     */     }
/* 278 */     else if (ws == WatchBox.WatchShape.EW) {
/* 279 */       this.ewBtn.setSelection(true);
/* 280 */       this.esolBtn.setSelection(false);
/* 281 */       this.nsBtn.setSelection(false);
/*     */     }
/* 283 */     else if (ws == WatchBox.WatchShape.ESOL) {
/* 284 */       this.esolBtn.setSelection(true);
/* 285 */       this.nsBtn.setSelection(false);
/* 286 */       this.ewBtn.setSelection(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createColorAttr()
/*     */   {
/* 305 */     Composite colorComp = new Composite(this.top, 0);
/* 306 */     GridLayout gl = new GridLayout(2, false);
/* 307 */     colorComp.setLayout(gl);
/*     */ 
/* 309 */     this.colorLbl = new Label(colorComp, 16384);
/* 310 */     this.colorLbl.setText("Color:");
/*     */ 
/* 312 */     this.cs = new ColorButtonSelector(colorComp);
/* 313 */     this.cs.setColorValue(new RGB(255, 0, 255));
/*     */   }
/*     */ 
/*     */   private void createShapeFillAttr()
/*     */   {
/* 321 */     Composite shapeFillComp = new Composite(this.top, 0);
/*     */ 
/* 323 */     shapeFillComp.setLayout(new RowLayout());
/* 324 */     Group shapeGrp = new Group(shapeFillComp, 0);
/* 325 */     shapeGrp.setText("Shape");
/*     */ 
/* 327 */     GridLayout shapeGl = new GridLayout(1, false);
/*     */ 
/* 329 */     shapeGrp.setLayout(shapeGl);
/*     */ 
/* 331 */     this.nsBtn = new Button(shapeGrp, 16);
/* 332 */     this.nsBtn.setText("NS");
/* 333 */     this.ewBtn = new Button(shapeGrp, 16);
/* 334 */     this.ewBtn.setText("EW");
/* 335 */     this.esolBtn = new Button(shapeGrp, 16);
/* 336 */     this.esolBtn.setText("ESOL");
/*     */ 
/* 339 */     this.esolBtn.setSelection(true);
/*     */ 
/* 341 */     Group fillGrp = new Group(shapeFillComp, 0);
/* 342 */     fillGrp.setText("Fill");
/* 343 */     GridLayout fillGl = new GridLayout(1, false);
/* 344 */     fillGl.marginBottom = 13;
/* 345 */     fillGrp.setLayout(fillGl);
/*     */ 
/* 347 */     this.fillBtn = new Button(fillGrp, 32);
/* 348 */     this.fillBtn.setText("Use Fill");
/*     */ 
/* 350 */     Composite colorComp = new Composite(fillGrp, 0);
/* 351 */     colorComp.setLayout(new GridLayout(2, false));
/*     */ 
/* 353 */     Label colorLbl = new Label(colorComp, 16384);
/* 354 */     colorLbl.setText("Color:");
/*     */ 
/* 356 */     this.symbolColor = new ColorButtonSelector(colorComp);
/* 357 */     this.symbolColor.setColorValue(new RGB(0, 255, 0));
/*     */   }
/*     */ 
/*     */   private void createTypeAttr()
/*     */   {
/* 366 */     Composite typeGrp = new Composite(this.top, 0);
/* 367 */     GridLayout gl = new GridLayout(2, false);
/* 368 */     gl.marginHeight = 1;
/* 369 */     gl.verticalSpacing = 1;
/* 370 */     gl.marginBottom = 0;
/* 371 */     typeGrp.setLayout(gl);
/* 372 */     Label symbolLabel = new Label(typeGrp, 16384);
/* 373 */     symbolLabel.setText("Type:");
/*     */ 
/* 375 */     this.symbolCombo = new SymbolCombo(typeGrp);
/* 376 */     this.symbolCombo.setLayoutData(new GridData(10, 1));
/* 377 */     this.symbolCombo.setItems(SYMBOL_LIST);
/*     */   }
/*     */ 
/*     */   private void createWidthAttr()
/*     */   {
/* 386 */     Composite lineWidthGrp = new Composite(this.top, 0);
/* 387 */     GridLayout gl = new GridLayout(3, false);
/* 388 */     gl.marginHeight = 1;
/* 389 */     gl.verticalSpacing = 1;
/* 390 */     lineWidthGrp.setLayout(gl);
/*     */ 
/* 392 */     this.widthLbl = new Label(lineWidthGrp, 16384);
/* 393 */     this.widthLbl.setText("Width:");
/* 394 */     this.widthLbl.setLayoutData(new GridData(50, 20));
/*     */ 
/* 396 */     this.widthSlider = new Slider(lineWidthGrp, 256);
/* 397 */     this.widthSlider.setValues(15, 1, 101, 1, 1, 1);
/* 398 */     this.widthSlider.setLayoutData(new GridData(100, 25));
/*     */ 
/* 400 */     this.widthSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 402 */         WatchBoxAttrDlg.this.widthText.setText(new Float(WatchBoxAttrDlg.this.widthSlider.getSelection() / 10.0D).toString());
/*     */       }
/*     */     });
/* 406 */     this.widthText = new Text(lineWidthGrp, 2052);
/* 407 */     this.widthText.setLayoutData(new GridData(30, 10));
/* 408 */     this.widthText.setEditable(true);
/* 409 */     this.widthText.setText("1.5");
/* 410 */     this.widthText.addListener(25, new Listener()
/*     */     {
/*     */       public void handleEvent(Event e)
/*     */       {
/* 415 */         e.doit = PgenUtil.validateNumberTextField(e);
/*     */ 
/* 417 */         if ((e.doit) && (e.keyCode != 0))
/*     */         {
/* 420 */           StringBuffer str = new StringBuffer(WatchBoxAttrDlg.this.widthText.getText());
/* 421 */           str.insert(e.start, e.text);
/*     */ 
/* 423 */           float value = 0.0F;
/*     */           try {
/* 425 */             value = Float.parseFloat(new String(str));
/* 426 */             if ((value >= 0.1D) && (value <= 10.0F)) {
/* 427 */               e.doit = true;
/*     */             }
/*     */             else
/* 430 */               e.doit = false;
/*     */           }
/*     */           catch (NumberFormatException e1)
/*     */           {
/* 434 */             e.doit = false;
/*     */           }
/*     */         }
/*     */       }
/*     */     });
/* 441 */     this.widthText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 443 */         float value = 0.0F;
/*     */         try {
/* 445 */           value = Float.parseFloat(WatchBoxAttrDlg.this.widthText.getText());
/* 446 */           if ((value >= 0.1D) && (value <= 10.0F))
/* 447 */             WatchBoxAttrDlg.this.widthSlider.setSelection((int)(value * 10.0F));
/*     */         }
/*     */         catch (NumberFormatException localNumberFormatException)
/*     */         {
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createSizeAttr()
/*     */   {
/* 463 */     Composite sizeGrp = new Composite(this.top, 0);
/* 464 */     GridLayout gl = new GridLayout(3, false);
/* 465 */     gl.marginHeight = 1;
/* 466 */     gl.verticalSpacing = 1;
/* 467 */     sizeGrp.setLayout(gl);
/*     */ 
/* 469 */     this.sizeLbl = new Label(sizeGrp, 16384);
/* 470 */     this.sizeLbl.setText("Size:");
/* 471 */     this.sizeLbl.setLayoutData(new GridData(50, 20));
/*     */ 
/* 473 */     this.sizeSlider = new Slider(sizeGrp, 256);
/* 474 */     this.sizeSlider.setValues(7, 1, 101, 1, 1, 1);
/* 475 */     this.sizeSlider.setLayoutData(new GridData(100, 25));
/*     */ 
/* 477 */     this.sizeSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 479 */         WatchBoxAttrDlg.this.sizeText.setText(new Float(WatchBoxAttrDlg.this.sizeSlider.getSelection() / 10.0D).toString());
/*     */       }
/*     */     });
/* 483 */     this.sizeText = new Text(sizeGrp, 2052);
/* 484 */     this.sizeText.setLayoutData(new GridData(30, 10));
/* 485 */     this.sizeText.setEditable(true);
/* 486 */     this.sizeText.setText("0.7");
/* 487 */     this.sizeText.addListener(25, new Listener()
/*     */     {
/*     */       public void handleEvent(Event e)
/*     */       {
/* 492 */         e.doit = PgenUtil.validateNumberTextField(e);
/*     */ 
/* 494 */         if ((e.doit) && (e.keyCode != 0))
/*     */         {
/* 496 */           StringBuffer str = new StringBuffer(WatchBoxAttrDlg.this.sizeText.getText());
/* 497 */           str.insert(e.start, e.text);
/*     */ 
/* 499 */           float value = 0.0F;
/*     */           try {
/* 501 */             value = Float.parseFloat(new String(str));
/* 502 */             if ((value >= 0.1D) && (value <= 10.0F)) {
/* 503 */               e.doit = true;
/*     */             }
/*     */             else
/* 506 */               e.doit = false;
/*     */           }
/*     */           catch (NumberFormatException e1)
/*     */           {
/* 510 */             e.doit = false;
/*     */           }
/*     */         }
/*     */       }
/*     */     });
/* 517 */     this.sizeText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 519 */         float value = 0.0F;
/*     */         try {
/* 521 */           value = Float.parseFloat(WatchBoxAttrDlg.this.sizeText.getText());
/* 522 */           if ((value >= 0.1D) && (value <= 10.0F))
/* 523 */             WatchBoxAttrDlg.this.sizeSlider.setSelection((int)(value * 10.0F));
/*     */         }
/*     */         catch (NumberFormatException localNumberFormatException)
/*     */         {
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createDispBtn()
/*     */   {
/* 540 */     this.dispBtn = new Button(this.top, 8);
/* 541 */     this.dispBtn.setText("Show Display");
/* 542 */     GridData gd = new GridData(16777216, -1, true, false);
/* 543 */     this.dispBtn.setLayoutData(gd);
/*     */ 
/* 545 */     this.dispBtn.addListener(3, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/* 549 */         WatchBoxAttrDlg.this.openSpecDlg();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void openSpecDlg()
/*     */   {
/* 559 */     if (this.dispBtn.getText().equalsIgnoreCase("Show Display")) {
/* 560 */       final Shell shell = getShell();
/*     */ 
/* 562 */       Display.getDefault().asyncExec(new Runnable()
/*     */       {
/*     */         public void run() {
/* 565 */           if ((shell != null) && (!shell.isDisposed())) {
/* 566 */             WatchBoxAttrDlg.this.dispBtn.setText("Hide Display");
/* 567 */             if (WatchBoxAttrDlg.this.infoDlg == null) {
/* 568 */               WatchBoxAttrDlg.this.infoDlg = WatchInfoDlg.getInstance(WatchBoxAttrDlg.this.getParentShell(), WatchBoxAttrDlg.INSTANCE);
/*     */             }
/* 570 */             WatchBoxAttrDlg.this.infoDlg.setBlockOnOpen(false);
/*     */ 
/* 572 */             WatchBoxAttrDlg.this.infoDlg.open();
/* 573 */             WatchBoxAttrDlg.this.infoDlg.clearCwaPane();
/* 574 */             WatchBoxAttrDlg.this.infoDlg.createCWAs(WatchBoxAttrDlg.this.wb.getWFOs());
/* 575 */             WatchBoxAttrDlg.this.infoDlg.setStatesWFOs();
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     else
/*     */     {
/* 582 */       this.dispBtn.setText("Show Display");
/* 583 */       if (this.infoDlg != null)
/* 584 */         this.infoDlg.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enableShapeBtn(boolean flag)
/*     */   {
/* 594 */     this.nsBtn.setEnabled(flag);
/* 595 */     this.ewBtn.setEnabled(flag);
/* 596 */     this.esolBtn.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   public void enableDspBtn(boolean flag)
/*     */   {
/* 604 */     if (!this.dispBtn.isDisposed())
/* 605 */       this.dispBtn.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   public boolean close()
/*     */   {
/* 613 */     this.drawingLayer.removeSelected();
/* 614 */     this.wbTool = null;
/* 615 */     if (this.infoDlg != null) {
/* 616 */       this.infoDlg.close();
/*     */     }
/* 618 */     return super.close();
/*     */   }
/*     */ 
/*     */   public Color getFillColor()
/*     */   {
/* 626 */     if (this.fillBtn.getSelection()) {
/* 627 */       return new Color(this.symbolColor.getColorValue().red, 
/* 628 */         this.symbolColor.getColorValue().green, this.symbolColor.getColorValue().blue);
/*     */     }
/*     */ 
/* 631 */     return null;
/*     */   }
/*     */ 
/*     */   public WatchBox.WatchShape getWatchBoxShape()
/*     */   {
/* 641 */     if (this.nsBtn.getSelection()) return WatchBox.WatchShape.NS;
/* 642 */     if (this.ewBtn.getSelection()) return WatchBox.WatchShape.EW;
/* 643 */     if (this.esolBtn.getSelection()) return WatchBox.WatchShape.ESOL;
/* 644 */     return null;
/*     */   }
/*     */ 
/*     */   public void setWatchBox(WatchBox wb)
/*     */   {
/* 653 */     this.wb = wb;
/*     */   }
/*     */ 
/*     */   public WatchBox getWatchBox()
/*     */   {
/* 661 */     return this.wb;
/*     */   }
/*     */ 
/*     */   public WatchInfoDlg getWatchInfoDlg()
/*     */   {
/* 669 */     return this.infoDlg;
/*     */   }
/*     */ 
/*     */   public String getWatchSymbolType()
/*     */   {
/* 676 */     return this.symbolCombo.getSelectedText();
/*     */   }
/*     */ 
/*     */   public float getWatchSymbolWidth()
/*     */   {
/* 683 */     return this.widthSlider.getSelection() / 10.0F;
/*     */   }
/*     */ 
/*     */   public double getWatchSymbolSize()
/*     */   {
/* 690 */     return this.sizeSlider.getSelection() / 10.0D;
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 699 */     ArrayList adcList = null;
/* 700 */     ArrayList newList = new ArrayList();
/*     */ 
/* 703 */     if (this.drawingLayer != null) {
/* 704 */       adcList = (ArrayList)this.drawingLayer.getAllSelected();
/*     */     }
/*     */ 
/* 707 */     if ((adcList != null) && (!adcList.isEmpty()))
/*     */     {
/* 710 */       for (AbstractDrawableComponent adc : adcList)
/*     */       {
/* 712 */         WatchBox el = (WatchBox)adc.getPrimaryDE();
/*     */ 
/* 714 */         if (el != null)
/*     */         {
/* 717 */           WatchBox newEl = (WatchBox)el.copy();
/* 718 */           newEl.setCountyList(el.getCountyList());
/*     */ 
/* 721 */           newEl.update(this);
/* 722 */           this.wb = newEl;
/*     */ 
/* 725 */           boolean updateShape = false;
/* 726 */           if (el.getWatchBoxShape() != getWatchBoxShape()) {
/* 727 */             ArrayList anchorsInPoly = PgenWatchBoxDrawingTool.getAnchorsInPoly(this.mapEditor, 
/* 728 */               WatchBox.generateWatchBoxPts(getWatchBoxShape(), 
/* 729 */               this.wb.getHalfWidth(), (Coordinate)this.wb.getPoints().get(0), (Coordinate)this.wb.getPoints().get(5)));
/* 730 */             if ((anchorsInPoly != null) && (!anchorsInPoly.isEmpty())) {
/* 731 */               DECollection dec = new DrawableElementFactory().createWatchBox("Met", "Watch", getWatchBoxShape(), 
/* 732 */                 (Coordinate)this.wb.getPoints().get(0), (Coordinate)this.wb.getPoints().get(5), anchorsInPoly, this);
/* 733 */               if (dec != null) {
/* 734 */                 this.wb.setLinePoints(((WatchBox)dec.getPrimaryDE()).getPoints());
/* 735 */                 this.wb.setAnchors(((WatchBox)dec.getPrimaryDE()).getAnchors()[0], ((WatchBox)dec.getPrimaryDE()).getAnchors()[1]);
/* 736 */                 updateShape = true;
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 742 */           if (!updateShape) {
/* 743 */             this.wb.setWatchBoxShape(el.getWatchBoxShape());
/* 744 */             setShapeBtn(this.wb.getWatchBoxShape());
/*     */           }
/*     */ 
/* 747 */           newList.add(newEl);
/*     */         }
/*     */       }
/*     */ 
/* 751 */       ArrayList oldList = new ArrayList(adcList);
/* 752 */       this.drawingLayer.replaceElements(oldList, newList);
/*     */     }
/*     */ 
/* 755 */     this.drawingLayer.removeSelected();
/*     */ 
/* 758 */     for (AbstractDrawableComponent adc : newList) {
/* 759 */       this.drawingLayer.addSelected(adc);
/*     */     }
/*     */ 
/* 762 */     if (this.mapEditor != null)
/* 763 */       this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   public boolean getFillFlag()
/*     */   {
/* 773 */     return this.fillBtn.getSelection();
/*     */   }
/*     */ 
/*     */   public void setWbTool(PgenWatchBoxModifyTool wbTool)
/*     */   {
/* 781 */     this.wbTool = wbTool;
/*     */   }
/*     */ 
/*     */   public PgenWatchBoxModifyTool getWbTool()
/*     */   {
/* 789 */     return this.wbTool;
/*     */   }
/*     */ 
/*     */   public Station[] getAnchors()
/*     */   {
/* 795 */     return null;
/*     */   }
/*     */ 
/*     */   public List<SPCCounty> getCountyList()
/*     */   {
/* 801 */     return null;
/*     */   }
/*     */ 
/*     */   public int getWatchNumber()
/*     */   {
/* 807 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getIssueFlag()
/*     */   {
/* 813 */     return 0;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLinePoints()
/*     */   {
/* 819 */     return null;
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 825 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/* 826 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/* 827 */     super.createButtonsForButtonBar(parent);
/* 828 */     getButton(1).setEnabled(false);
/* 829 */     getButton(0).setEnabled(false);
/*     */ 
/* 831 */     getButton(1).setLayoutData(new GridData(90, 30));
/* 832 */     getButton(0).setLayoutData(new GridData(90, 30));
/*     */   }
/*     */ 
/*     */   public Control createButtonBar(Composite parent)
/*     */   {
/* 838 */     Control bar = super.createButtonBar(parent);
/* 839 */     GridData gd = new GridData(16777216, -1, true, false);
/* 840 */     gd.widthHint = 220;
/* 841 */     gd.heightHint = 35;
/*     */ 
/* 843 */     bar.setLayoutData(gd);
/* 844 */     return bar;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchBoxAttrDlg
 * JD-Core Version:    0.6.2
 */