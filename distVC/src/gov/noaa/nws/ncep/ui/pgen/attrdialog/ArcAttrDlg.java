/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IArc;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import org.eclipse.swt.events.KeyAdapter;
/*     */ import org.eclipse.swt.events.KeyEvent;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Slider;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class ArcAttrDlg extends AttrDlg
/*     */   implements IArc
/*     */ {
/*     */   private static ArcAttrDlg INSTANCE;
/*     */   private Composite top;
/*     */   private Label colorLbl;
/*     */   private ColorButtonSelector cs;
/*     */   private Label widthLbl;
/*     */   private Slider lineWidthSlider;
/*     */   private Text lineWidthText;
/*     */   protected Label axisRatioLbl;
/*     */   protected Slider axisRatioSlider;
/*     */   protected Text axisRatioText;
/*     */   protected Label startAngleLbl;
/*     */   protected Slider startAngleSlider;
/*     */   protected Text startAngleText;
/*     */   protected Label endAngleLbl;
/*     */   protected Slider endAngleSlider;
/*     */   protected Text endAngleText;
/*     */   private Button[] chkBox;
/*     */ 
/*     */   protected ArcAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  94 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static ArcAttrDlg getInstance(Shell parShell)
/*     */   {
/* 107 */     if (INSTANCE == null) {
/*     */       try
/*     */       {
/* 110 */         INSTANCE = new ArcAttrDlg(parShell);
/*     */       } catch (VizException e) {
/* 112 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 117 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 127 */     this.top = ((Composite)super.createDialogArea(parent));
/* 128 */     this.top.setLayout(getGridLayout(1, false, 0, 0, 0, 0));
/*     */ 
/* 131 */     initializeComponents();
/*     */ 
/* 133 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 141 */     getShell().setText("Arc Attributes");
/* 142 */     this.chkBox = new Button[5];
/*     */ 
/* 144 */     createColorAttr();
/* 145 */     createWidthAttr();
/* 146 */     createRatioAttr();
/* 147 */     createStartAngleAttr();
/* 148 */     createEndAngleAttr();
/* 149 */     addSeparator(this.top.getParent());
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 158 */     if (this.chkBox[ChkBox.COLOR.ordinal()].getSelection())
/*     */     {
/* 161 */       Color[] colors = new Color[1];
/*     */ 
/* 163 */       colors[0] = new Color(this.cs.getColorValue().red, 
/* 164 */         this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*     */ 
/* 166 */       return colors;
/*     */     }
/*     */ 
/* 169 */     return null;
/*     */   }
/*     */ 
/*     */   private void setColor(Color clr)
/*     */   {
/* 179 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 187 */     if (this.chkBox[ChkBox.WIDTH.ordinal()].getSelection())
/*     */     {
/* 189 */       return this.lineWidthSlider.getSelection();
/*     */     }
/*     */ 
/* 192 */     return (0.0F / 0.0F);
/*     */   }
/*     */ 
/*     */   private void setLineWidth(float lw)
/*     */   {
/* 203 */     this.lineWidthSlider.setSelection((int)lw);
/* 204 */     this.lineWidthText.setText((int)lw);
/*     */   }
/*     */ 
/*     */   public double getAxisRatio()
/*     */   {
/* 212 */     if (this.chkBox[ChkBox.AXIS_RATIO.ordinal()].getSelection())
/*     */     {
/* 214 */       return this.axisRatioSlider.getSelection() / 100.0D;
/*     */     }
/*     */ 
/* 217 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public void setAxisRatio(double ar)
/*     */   {
/* 226 */     this.axisRatioSlider.setSelection((int)(ar * 100.0D));
/* 227 */     this.axisRatioText.setText(ar);
/*     */   }
/*     */ 
/*     */   public double getStartAngle()
/*     */   {
/* 234 */     if (this.chkBox[ChkBox.START_ANGLE.ordinal()].getSelection())
/*     */     {
/* 236 */       return this.startAngleSlider.getSelection();
/*     */     }
/*     */ 
/* 239 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public void setStartAngle(double sa)
/*     */   {
/* 248 */     this.startAngleSlider.setSelection((int)sa);
/* 249 */     this.startAngleText.setText(sa);
/*     */   }
/*     */ 
/*     */   public double getEndAngle()
/*     */   {
/* 256 */     if (this.chkBox[ChkBox.END_ANGLE.ordinal()].getSelection())
/*     */     {
/* 258 */       return this.endAngleSlider.getSelection();
/*     */     }
/*     */ 
/* 261 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public void setEndAngle(double ea)
/*     */   {
/* 270 */     this.endAngleSlider.setSelection((int)ea);
/* 271 */     this.endAngleText.setText(ea);
/*     */   }
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 279 */     return true;
/*     */   }
/*     */ 
/*     */   public FillPatternList.FillPattern getFillPattern()
/*     */   {
/* 288 */     return FillPatternList.FillPattern.SOLID;
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 297 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public int getSmoothFactor()
/*     */   {
/* 307 */     return 2;
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute iattr)
/*     */   {
/* 316 */     if ((iattr instanceof IArc)) {
/* 317 */       IArc attr = (IArc)iattr;
/* 318 */       Color clr = attr.getColors()[0];
/* 319 */       if (clr != null) setColor(clr);
/*     */ 
/* 321 */       float lw = attr.getLineWidth();
/* 322 */       if (lw > 0.0F) setLineWidth(lw);
/*     */ 
/* 324 */       double ar = attr.getAxisRatio();
/* 325 */       if (ar > 0.0D) setAxisRatio(ar);
/*     */ 
/* 327 */       double sa = attr.getStartAngle();
/* 328 */       if (sa > 0.0D) setStartAngle(sa);
/*     */ 
/* 330 */       double ea = attr.getEndAngle();
/* 331 */       if (ea > 0.0D) setEndAngle(ea);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createColorAttr()
/*     */   {
/* 341 */     Composite inCmp = new Composite(this.top, 0);
/* 342 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 344 */     this.chkBox[ChkBox.COLOR.ordinal()] = new Button(inCmp, 32);
/* 345 */     this.chkBox[ChkBox.COLOR.ordinal()].setLayoutData(new GridData(16, 28));
/* 346 */     this.chkBox[ChkBox.COLOR.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 350 */         Button btn = (Button)e.widget;
/* 351 */         if (btn.getSelection()) {
/* 352 */           ArcAttrDlg.this.colorLbl.setEnabled(true);
/*     */         }
/*     */         else
/* 355 */           ArcAttrDlg.this.colorLbl.setEnabled(false);
/*     */       }
/*     */     });
/* 360 */     this.colorLbl = new Label(inCmp, 16384);
/* 361 */     this.colorLbl.setText("Color:");
/*     */ 
/* 363 */     this.cs = new ColorButtonSelector(inCmp, 20, 15);
/* 364 */     this.cs.setColorValue(new RGB(0, 255, 0));
/*     */   }
/*     */ 
/*     */   private void createWidthAttr()
/*     */   {
/* 372 */     Composite inCmp = new Composite(this.top, 0);
/* 373 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 375 */     this.chkBox[ChkBox.WIDTH.ordinal()] = new Button(inCmp, 32);
/* 376 */     this.chkBox[ChkBox.WIDTH.ordinal()].setLayoutData(new GridData(16, 28));
/* 377 */     this.chkBox[ChkBox.WIDTH.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 381 */         Button btn = (Button)e.widget;
/* 382 */         if (btn.getSelection()) {
/* 383 */           ArcAttrDlg.this.widthLbl.setEnabled(true);
/* 384 */           ArcAttrDlg.this.lineWidthSlider.setEnabled(true);
/* 385 */           ArcAttrDlg.this.lineWidthText.setEnabled(true);
/*     */         }
/*     */         else
/*     */         {
/* 389 */           ArcAttrDlg.this.widthLbl.setEnabled(false);
/* 390 */           ArcAttrDlg.this.lineWidthSlider.setEnabled(false);
/* 391 */           ArcAttrDlg.this.lineWidthText.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 396 */     this.widthLbl = new Label(inCmp, 16384);
/* 397 */     this.widthLbl.setText("Line Width ");
/*     */ 
/* 399 */     Group lineWidthGrp = new Group(inCmp, 0);
/* 400 */     lineWidthGrp.setLayout(getGridLayout(2, false, 0, 0, 0, 0));
/*     */ 
/* 402 */     this.lineWidthSlider = new Slider(lineWidthGrp, 256);
/* 403 */     this.lineWidthSlider.setValues(2, 1, 11, 1, 1, 1);
/* 404 */     this.lineWidthSlider.setLayoutData(new GridData(90, 15));
/* 405 */     this.lineWidthSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 407 */         ArcAttrDlg.this.lineWidthText.setText(ArcAttrDlg.this.lineWidthSlider.getSelection());
/*     */       }
/*     */     });
/* 411 */     this.lineWidthText = new Text(lineWidthGrp, 2052);
/* 412 */     this.lineWidthText.setLayoutData(new GridData(30, 8));
/* 413 */     this.lineWidthText.setEditable(true);
/* 414 */     this.lineWidthText.setText("2");
/* 415 */     this.lineWidthText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 417 */         int value = 0;
/*     */         try {
/* 419 */           value = Integer.parseInt(ArcAttrDlg.this.lineWidthText.getText());
/* 420 */           if ((value >= 1) && (value <= 10)) {
/* 421 */             ArcAttrDlg.this.lineWidthSlider.setSelection(value);
/* 422 */             ArcAttrDlg.this.lineWidthText.setToolTipText("");
/*     */           }
/*     */           else {
/* 425 */             ArcAttrDlg.this.lineWidthText.setToolTipText("Only integer values between 1 and 10 are accepted.");
/*     */           }
/*     */         } catch (NumberFormatException e1) {
/* 428 */           ArcAttrDlg.this.lineWidthText.setToolTipText("Only integer values between 1 and 10 are accepted.");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createRatioAttr()
/*     */   {
/* 438 */     Composite inCmp = new Composite(this.top, 0);
/* 439 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 441 */     this.chkBox[ChkBox.AXIS_RATIO.ordinal()] = new Button(inCmp, 32);
/* 442 */     this.chkBox[ChkBox.AXIS_RATIO.ordinal()].setLayoutData(new GridData(16, 28));
/* 443 */     this.chkBox[ChkBox.AXIS_RATIO.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 447 */         Button btn = (Button)e.widget;
/* 448 */         if (btn.getSelection()) {
/* 449 */           ArcAttrDlg.this.axisRatioLbl.setEnabled(true);
/* 450 */           ArcAttrDlg.this.axisRatioSlider.setEnabled(true);
/* 451 */           ArcAttrDlg.this.axisRatioText.setEnabled(true);
/*     */         }
/*     */         else
/*     */         {
/* 455 */           ArcAttrDlg.this.axisRatioLbl.setEnabled(false);
/* 456 */           ArcAttrDlg.this.axisRatioSlider.setEnabled(false);
/* 457 */           ArcAttrDlg.this.axisRatioText.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 463 */     this.axisRatioLbl = new Label(inCmp, 16384);
/* 464 */     this.axisRatioLbl.setText("Axis Ratio ");
/*     */ 
/* 466 */     Group axisRatioGrp = new Group(inCmp, 0);
/* 467 */     axisRatioGrp.setLayout(getGridLayout(2, false, 0, 0, 0, 0));
/*     */ 
/* 469 */     this.axisRatioSlider = new Slider(axisRatioGrp, 256);
/* 470 */     this.axisRatioSlider.setValues(100, 0, 101, 1, 1, 1);
/* 471 */     this.axisRatioSlider.setLayoutData(new GridData(92, 15));
/* 472 */     this.axisRatioSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 474 */         ArcAttrDlg.this.axisRatioText.setText(ArcAttrDlg.this.axisRatioSlider.getSelection() / 100.0D);
/*     */       }
/*     */     });
/* 478 */     this.axisRatioText = new Text(axisRatioGrp, 2052);
/* 479 */     this.axisRatioText.setLayoutData(new GridData(30, 10));
/* 480 */     this.axisRatioText.setEditable(true);
/* 481 */     this.axisRatioText.setText("1.0");
/* 482 */     this.axisRatioText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 484 */         double value = 0.0D;
/*     */         try {
/* 486 */           value = Double.parseDouble(ArcAttrDlg.this.axisRatioText.getText());
/* 487 */           if ((value >= 0.0D) && (value <= 100.0D)) {
/* 488 */             ArcAttrDlg.this.axisRatioSlider.setSelection((int)(value * 100.0D));
/* 489 */             ArcAttrDlg.this.axisRatioText.setToolTipText("");
/*     */           }
/*     */           else {
/* 492 */             ArcAttrDlg.this.axisRatioText.setToolTipText("Only values between 0.0 and 1.0 are accepted.");
/*     */           }
/*     */         } catch (NumberFormatException e1) {
/* 495 */           ArcAttrDlg.this.axisRatioText.setToolTipText("Only values between 0.0 and 1.0 are accepted.");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createStartAngleAttr()
/*     */   {
/* 506 */     Composite inCmp = new Composite(this.top, 0);
/* 507 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 509 */     this.chkBox[ChkBox.START_ANGLE.ordinal()] = new Button(inCmp, 32);
/* 510 */     this.chkBox[ChkBox.START_ANGLE.ordinal()].setLayoutData(new GridData(16, 28));
/* 511 */     this.chkBox[ChkBox.START_ANGLE.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 515 */         Button btn = (Button)e.widget;
/* 516 */         if (btn.getSelection()) {
/* 517 */           ArcAttrDlg.this.startAngleLbl.setEnabled(true);
/* 518 */           ArcAttrDlg.this.startAngleSlider.setEnabled(true);
/* 519 */           ArcAttrDlg.this.startAngleText.setEnabled(true);
/*     */         }
/*     */         else
/*     */         {
/* 523 */           ArcAttrDlg.this.startAngleLbl.setEnabled(false);
/* 524 */           ArcAttrDlg.this.startAngleSlider.setEnabled(false);
/* 525 */           ArcAttrDlg.this.startAngleText.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 531 */     this.startAngleLbl = new Label(inCmp, 16384);
/* 532 */     this.startAngleLbl.setText("Start Angle ");
/*     */ 
/* 534 */     Group startAngleGrp = new Group(inCmp, 0);
/* 535 */     startAngleGrp.setLayout(getGridLayout(2, false, 0, 0, 0, 0));
/*     */ 
/* 537 */     this.startAngleSlider = new Slider(startAngleGrp, 256);
/* 538 */     this.startAngleSlider.setValues(0, 0, 361, 1, 1, 5);
/* 539 */     this.startAngleSlider.setLayoutData(new GridData(87, 15));
/* 540 */     this.startAngleSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 542 */         ArcAttrDlg.this.startAngleText.setText(ArcAttrDlg.this.startAngleSlider.getSelection());
/*     */       }
/*     */     });
/* 546 */     this.startAngleText = new Text(startAngleGrp, 2052);
/* 547 */     this.startAngleText.setLayoutData(new GridData(30, 10));
/* 548 */     this.startAngleText.setEditable(true);
/* 549 */     this.startAngleText.setText("0");
/* 550 */     this.startAngleText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 552 */         int value = 0;
/*     */         try {
/* 554 */           value = Integer.parseInt(ArcAttrDlg.this.startAngleText.getText());
/* 555 */           if ((value >= 0) && (value <= 360)) {
/* 556 */             ArcAttrDlg.this.startAngleSlider.setSelection(value);
/* 557 */             ArcAttrDlg.this.startAngleText.setToolTipText("");
/*     */           }
/*     */           else {
/* 560 */             ArcAttrDlg.this.startAngleText.setToolTipText("Only integer values between 0 and 360 are accepted.");
/*     */           }
/*     */         } catch (NumberFormatException e1) {
/* 563 */           ArcAttrDlg.this.startAngleText.setToolTipText("Only integer  values between 0 and 360 are accepted.");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createEndAngleAttr()
/*     */   {
/* 575 */     Composite inCmp = new Composite(this.top, 0);
/* 576 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 578 */     this.chkBox[ChkBox.END_ANGLE.ordinal()] = new Button(inCmp, 32);
/* 579 */     this.chkBox[ChkBox.END_ANGLE.ordinal()].setLayoutData(new GridData(16, 28));
/* 580 */     this.chkBox[ChkBox.END_ANGLE.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 584 */         Button btn = (Button)e.widget;
/* 585 */         if (btn.getSelection()) {
/* 586 */           ArcAttrDlg.this.endAngleLbl.setEnabled(true);
/* 587 */           ArcAttrDlg.this.endAngleSlider.setEnabled(true);
/* 588 */           ArcAttrDlg.this.endAngleText.setEnabled(true);
/*     */         }
/*     */         else
/*     */         {
/* 592 */           ArcAttrDlg.this.endAngleLbl.setEnabled(false);
/* 593 */           ArcAttrDlg.this.endAngleSlider.setEnabled(false);
/* 594 */           ArcAttrDlg.this.endAngleText.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 600 */     this.endAngleLbl = new Label(inCmp, 16384);
/* 601 */     this.endAngleLbl.setText("End Angle ");
/*     */ 
/* 603 */     Group endAngleGrp = new Group(inCmp, 0);
/* 604 */     endAngleGrp.setLayout(getGridLayout(2, false, 0, 0, 0, 0));
/*     */ 
/* 606 */     this.endAngleSlider = new Slider(endAngleGrp, 256);
/* 607 */     this.endAngleSlider.setValues(360, 0, 361, 1, 1, 5);
/* 608 */     this.endAngleSlider.setLayoutData(new GridData(88, 15));
/* 609 */     this.endAngleSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 611 */         ArcAttrDlg.this.endAngleText.setText(ArcAttrDlg.this.endAngleSlider.getSelection());
/*     */       }
/*     */     });
/* 615 */     this.endAngleText = new Text(endAngleGrp, 2052);
/* 616 */     this.endAngleText.setLayoutData(new GridData(33, 10));
/* 617 */     this.endAngleText.setEditable(true);
/* 618 */     this.endAngleText.setText("360");
/* 619 */     this.endAngleText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 621 */         int value = 0;
/*     */         try {
/* 623 */           value = Integer.parseInt(ArcAttrDlg.this.endAngleText.getText());
/* 624 */           if ((value >= 0) && (value <= 360)) {
/* 625 */             ArcAttrDlg.this.endAngleSlider.setSelection(value);
/* 626 */             ArcAttrDlg.this.endAngleText.setToolTipText("");
/*     */           }
/*     */           else {
/* 629 */             ArcAttrDlg.this.endAngleText.setToolTipText("Only integer values between 0 and 360 are accepted.");
/*     */           }
/*     */         } catch (NumberFormatException e1) {
/* 632 */           ArcAttrDlg.this.endAngleText.setToolTipText("Only integer  values between 0 and 360 are accepted.");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 641 */     create();
/*     */ 
/* 644 */     if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 644 */       .equalsIgnoreCase("MultiSelect")) {
/* 645 */       enableChkBoxes(true);
/* 646 */       enableAllWidgets(false);
/*     */     }
/*     */     else {
/* 649 */       enableChkBoxes(false);
/*     */     }
/*     */ 
/* 652 */     return super.open();
/*     */   }
/*     */ 
/*     */   private void enableChkBoxes(boolean flag)
/*     */   {
/* 661 */     if (!flag) {
/* 662 */       setAllChkBoxes();
/*     */     }
/* 664 */     for (ChkBox chk : ChkBox.values())
/* 665 */       this.chkBox[chk.ordinal()].setVisible(flag);
/*     */   }
/*     */ 
/*     */   private void enableAllWidgets(boolean flag)
/*     */   {
/* 677 */     this.colorLbl.setEnabled(flag);
/*     */ 
/* 679 */     this.widthLbl.setEnabled(flag);
/* 680 */     this.lineWidthSlider.setEnabled(flag);
/* 681 */     this.lineWidthText.setEnabled(flag);
/*     */ 
/* 683 */     this.axisRatioLbl.setEnabled(flag);
/* 684 */     this.axisRatioSlider.setEnabled(flag);
/* 685 */     this.axisRatioText.setEnabled(flag);
/*     */ 
/* 687 */     this.startAngleLbl.setEnabled(flag);
/* 688 */     this.startAngleSlider.setEnabled(flag);
/* 689 */     this.startAngleText.setEnabled(flag);
/*     */ 
/* 691 */     this.endAngleLbl.setEnabled(flag);
/* 692 */     this.endAngleSlider.setEnabled(flag);
/* 693 */     this.endAngleText.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   private void setAllChkBoxes()
/*     */   {
/* 700 */     for (ChkBox chk : ChkBox.values())
/* 701 */       this.chkBox[chk.ordinal()].setSelection(true);
/*     */   }
/*     */ 
/*     */   public Coordinate getCenterPoint()
/*     */   {
/* 710 */     return null;
/*     */   }
/*     */ 
/*     */   public Coordinate getCircumferencePoint() {
/* 714 */     return null;
/*     */   }
/*     */ 
/*     */   private static enum ChkBox
/*     */   {
/*  59 */     COLOR, WIDTH, AXIS_RATIO, START_ANGLE, END_ANGLE;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.ArcAttrDlg
 * JD-Core Version:    0.6.2
 */