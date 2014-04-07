/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.Activator;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.SpinnerSlider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.eclipse.jface.resource.ImageDescriptor;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.events.VerifyEvent;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ import org.eclipse.swt.graphics.ImageData;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class LineAttrDlg extends AttrDlg
/*     */   implements ILine
/*     */ {
/*  73 */   protected static int MIN_LINE_WIDTH = 1;
/*  74 */   protected static int MAX_LINE_WIDTH = 20;
/*  75 */   protected static int DEFAULT_LINE_WIDTH = 2;
/*     */ 
/*  77 */   protected static double MIN_PATTERN_SIZE = 0.1D;
/*  78 */   protected static double MAX_PATTERN_SIZE = 10.0D;
/*  79 */   protected static double DEFAULT_PATTERN_SIZE = 2.0D;
/*     */   private static String[] FILL_PATTERNS;
/*     */   private static HashMap<String, Image> FILL_PATTERN_MENU_ITEMS;
/*     */   private static LineAttrDlg INSTANCE;
/*     */   protected Composite top;
/*     */   protected Composite colorGroup;
/*     */   protected Label colorLbl;
/*     */   protected ColorButtonSelector cs;
/*     */   protected List<ColorButtonSelector> csList;
/*     */   protected Label widthLbl;
/*  94 */   protected SpinnerSlider widthSpinnerSlider = null;
/*     */   protected Label patternSizeLbl;
/*  97 */   protected SpinnerSlider patternSizeSpinnerSlider = null;
/*     */   protected Label smoothLbl;
/* 100 */   protected Combo smoothLvlCbo = null;
/*     */   protected Label fillPatternLbl;
/*     */   protected SymbolCombo fillPatternCbo;
/* 105 */   protected Button closedBtn = null;
/* 106 */   protected Button filledBtn = null;
/*     */   protected Button[] chkBox;
/*     */ 
/*     */   protected LineAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 116 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static LineAttrDlg getInstance(Shell parShell)
/*     */   {
/* 129 */     if (INSTANCE == null) {
/*     */       try
/*     */       {
/* 132 */         INSTANCE = new LineAttrDlg(parShell);
/*     */       } catch (VizException e) {
/* 134 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 139 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 150 */     this.top = ((Composite)super.createDialogArea(parent));
/* 151 */     this.top.setLayout(getGridLayout(1, false, 0, 0, 0, 0));
/*     */ 
/* 154 */     initializeComponents();
/*     */ 
/* 156 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void initializeComponents()
/*     */   {
/* 164 */     getShell().setText("Line Attributes");
/*     */ 
/* 166 */     this.chkBox = new Button[ChkBox.values().length];
/*     */ 
/* 168 */     createColorCloseFillAttr();
/* 169 */     createWidthAttr();
/* 170 */     createPatternSizeAttr();
/* 171 */     createSmoothAttr();
/* 172 */     createFillPatternAttr();
/* 173 */     addSeparator(this.top.getParent());
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 180 */     if (this.chkBox[ChkBox.COLOR.ordinal()].getSelection())
/*     */     {
/* 183 */       Color[] colors = new Color[this.csList.size()];
/*     */ 
/* 185 */       for (int j = 0; j < this.csList.size(); j++) {
/* 186 */         RGB rgb = ((ColorButtonSelector)this.csList.get(j)).getColorValue();
/* 187 */         colors[j] = new Color(rgb.red, rgb.green, rgb.blue);
/*     */       }
/*     */ 
/* 190 */       return colors;
/*     */     }
/*     */ 
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   public void setColor(Color[] colors)
/*     */   {
/* 203 */     manageColorBoxes(colors.length);
/*     */ 
/* 205 */     for (int j = 0; j < colors.length; j++) {
/* 206 */       Color clr = colors[j];
/* 207 */       ((ColorButtonSelector)this.csList.get(j)).setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void manageColorBoxes(int num)
/*     */   {
/* 217 */     for (ColorButtonSelector cbs : this.csList) {
/* 218 */       cbs.dispose();
/*     */     }
/* 220 */     this.csList.clear();
/*     */ 
/* 222 */     for (int n = 0; n < num; n++) {
/* 223 */       ColorButtonSelector cbs = new ColorButtonSelector(this.colorGroup);
/* 224 */       this.csList.add(cbs);
/*     */     }
/* 226 */     this.colorGroup.pack();
/* 227 */     this.colorGroup.layout();
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 234 */     if (this.chkBox[ChkBox.WIDTH.ordinal()].getSelection())
/*     */     {
/* 236 */       return this.widthSpinnerSlider.getSelection();
/*     */     }
/*     */ 
/* 239 */     return (0.0F / 0.0F);
/*     */   }
/*     */ 
/*     */   private void setLineWidth(float lw)
/*     */   {
/* 250 */     this.widthSpinnerSlider.setSelection((int)lw);
/*     */   }
/*     */ 
/*     */   public double getSizeScale()
/*     */   {
/* 255 */     if (this.chkBox[ChkBox.PATTERN_SIZE.ordinal()].getSelection()) {
/* 256 */       return this.patternSizeSpinnerSlider.getSelection() / 10.0D;
/*     */     }
/*     */ 
/* 259 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   private void setSizeScale(double ps)
/*     */   {
/* 266 */     this.patternSizeSpinnerSlider.setSelection((int)(ps * 10.0D));
/*     */   }
/*     */ 
/*     */   public Boolean isClosedLine()
/*     */   {
/* 274 */     if (this.chkBox[ChkBox.CLOSE.ordinal()].getSelection())
/*     */     {
/* 276 */       return Boolean.valueOf(this.closedBtn.getSelection());
/*     */     }
/*     */ 
/* 279 */     return null;
/*     */   }
/*     */ 
/*     */   public FillPatternList.FillPattern getFillPattern()
/*     */   {
/* 289 */     if (this.chkBox[ChkBox.FILL_PATTERN.ordinal()].getSelection()) {
/* 290 */       return FillPatternList.FillPattern.valueOf(this.fillPatternCbo.getSelectedText());
/*     */     }
/*     */ 
/* 293 */     return null;
/*     */   }
/*     */ 
/*     */   private void setClosed(Boolean cls)
/*     */   {
/* 304 */     if (this.closedBtn != null)
/* 305 */       this.closedBtn.setSelection(cls.booleanValue());
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 313 */     if (this.chkBox[ChkBox.FILL.ordinal()].getSelection())
/*     */     {
/* 315 */       return Boolean.valueOf(this.filledBtn.getSelection());
/*     */     }
/*     */ 
/* 318 */     return null;
/*     */   }
/*     */ 
/*     */   private void setFilled(Boolean filled)
/*     */   {
/* 329 */     if (this.filledBtn != null)
/* 330 */       this.filledBtn.setSelection(filled.booleanValue());
/*     */   }
/*     */ 
/*     */   public int getSmoothFactor()
/*     */   {
/* 338 */     if (this.chkBox[ChkBox.SMOOTH.ordinal()].getSelection()) {
/* 339 */       return this.smoothLvlCbo.getSelectionIndex();
/*     */     }
/*     */ 
/* 342 */     return -1;
/*     */   }
/*     */ 
/*     */   public void setSmoothLvl(int sl)
/*     */   {
/* 352 */     this.smoothLvlCbo.select(sl);
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute iattr)
/*     */   {
/* 360 */     if ((iattr instanceof ILine)) {
/* 361 */       ILine attr = (ILine)iattr;
/* 362 */       Color[] clr = attr.getColors();
/* 363 */       if (clr != null) setColor(clr);
/*     */ 
/* 365 */       float lw = attr.getLineWidth();
/* 366 */       if (lw > 0.0F) setLineWidth(lw);
/*     */ 
/* 368 */       double ps = attr.getSizeScale();
/* 369 */       if (ps > 0.0D) setSizeScale(ps);
/*     */ 
/* 371 */       setClosed(attr.isClosedLine());
/* 372 */       setFilled(attr.isFilled());
/*     */ 
/* 374 */       if (attr.isFilled().booleanValue()) {
/* 375 */         this.fillPatternCbo.setEnabled(true);
/*     */       }
/*     */ 
/* 378 */       setFillPattern(attr.getFillPattern());
/*     */ 
/* 380 */       int sl = attr.getSmoothFactor();
/* 381 */       if (sl >= 0) setSmoothLvl(sl); 
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setFillPattern(FillPatternList.FillPattern fp)
/*     */   {
/* 386 */     if ((fp != null) && (this.fillPatternCbo != null)) this.fillPatternCbo.select(fp.ordinal());
/*     */   }
/*     */ 
/*     */   private void createColorAttr(Composite comp)
/*     */   {
/* 394 */     Composite inCmp = new Composite(comp, 0);
/* 395 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 397 */     this.chkBox[ChkBox.COLOR.ordinal()] = new Button(inCmp, 32);
/* 398 */     this.chkBox[ChkBox.COLOR.ordinal()].setLayoutData(new GridData(16, 28));
/* 399 */     this.chkBox[ChkBox.COLOR.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 403 */         Button btn = (Button)e.widget;
/* 404 */         if (btn.getSelection()) {
/* 405 */           LineAttrDlg.this.colorLbl.setEnabled(true);
/*     */         }
/*     */         else
/* 408 */           LineAttrDlg.this.colorLbl.setEnabled(false);
/*     */       }
/*     */     });
/* 414 */     this.colorLbl = new Label(inCmp, 16384);
/* 415 */     this.colorLbl.setText("Color ");
/*     */ 
/* 417 */     this.colorGroup = new Composite(inCmp, 0);
/* 418 */     this.colorGroup.setLayout(getGridLayout(1, false, 0, 0, 0, 0));
/* 419 */     this.csList = new ArrayList();
/*     */ 
/* 421 */     ColorButtonSelector dflt = new ColorButtonSelector(this.colorGroup);
/* 422 */     dflt.setColorValue(new RGB(0, 255, 0));
/* 423 */     this.csList.add(dflt);
/*     */   }
/*     */ 
/*     */   private void createWidthAttr()
/*     */   {
/* 431 */     Composite inCmp = new Composite(this.top, 0);
/* 432 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 434 */     this.chkBox[ChkBox.WIDTH.ordinal()] = new Button(inCmp, 32);
/* 435 */     this.chkBox[ChkBox.WIDTH.ordinal()].setLayoutData(new GridData(16, 28));
/* 436 */     this.chkBox[ChkBox.WIDTH.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 440 */         Button btn = (Button)e.widget;
/* 441 */         if (btn.getSelection()) {
/* 442 */           LineAttrDlg.this.widthLbl.setEnabled(true);
/* 443 */           LineAttrDlg.this.widthSpinnerSlider.setEnabled(true);
/*     */         }
/*     */         else {
/* 446 */           LineAttrDlg.this.widthLbl.setEnabled(false);
/* 447 */           LineAttrDlg.this.widthSpinnerSlider.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 454 */     this.widthLbl = new Label(inCmp, 16384);
/* 455 */     this.widthLbl.setText("Line Width ");
/*     */ 
/* 457 */     this.widthSpinnerSlider = 
/* 458 */       new SpinnerSlider(inCmp, 256, 1);
/* 459 */     this.widthSpinnerSlider.setLayoutData(new GridData(148, 25));
/* 460 */     this.widthSpinnerSlider.setMinimum(1);
/* 461 */     this.widthSpinnerSlider.setMaximum(20);
/* 462 */     this.widthSpinnerSlider.setIncrement(1);
/* 463 */     this.widthSpinnerSlider.setPageIncrement(3);
/* 464 */     this.widthSpinnerSlider.setDigits(0);
/*     */   }
/*     */ 
/*     */   protected boolean validateLineWidth(VerifyEvent ve)
/*     */   {
/* 472 */     boolean stat = false;
/*     */ 
/* 474 */     if ((ve.widget instanceof Text)) {
/* 475 */       Text wText = (Text)ve.widget;
/* 476 */       StringBuffer str = new StringBuffer(wText.getText());
/* 477 */       str.replace(ve.start, ve.end, ve.text);
/*     */ 
/* 479 */       if (str.toString().isEmpty()) return true;
/*     */       try
/*     */       {
/* 482 */         int value = Integer.parseInt(str.toString());
/* 483 */         if ((value >= MIN_LINE_WIDTH) && (value <= MAX_LINE_WIDTH)) {
/* 484 */           stat = true;
/*     */         }
/*     */         else
/* 487 */           stat = false;
/*     */       } catch (NumberFormatException e1) {
/* 489 */         stat = false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 494 */     return stat;
/*     */   }
/*     */ 
/*     */   private void createSmoothAttr()
/*     */   {
/* 502 */     Composite inCmp = new Composite(this.top, 0);
/* 503 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 505 */     this.chkBox[ChkBox.SMOOTH.ordinal()] = new Button(inCmp, 32);
/* 506 */     this.chkBox[ChkBox.SMOOTH.ordinal()].setLayoutData(new GridData(16, 28));
/* 507 */     this.chkBox[ChkBox.SMOOTH.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 511 */         Button btn = (Button)e.widget;
/* 512 */         if (btn.getSelection()) {
/* 513 */           LineAttrDlg.this.smoothLbl.setEnabled(true);
/* 514 */           LineAttrDlg.this.smoothLvlCbo.setEnabled(true);
/*     */         }
/*     */         else {
/* 517 */           LineAttrDlg.this.smoothLbl.setEnabled(false);
/* 518 */           LineAttrDlg.this.smoothLvlCbo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 525 */     this.smoothLbl = new Label(inCmp, 16384);
/* 526 */     this.smoothLbl.setText("Smooth Level ");
/*     */ 
/* 528 */     this.smoothLvlCbo = new Combo(inCmp, 12);
/*     */ 
/* 530 */     this.smoothLvlCbo.add("0");
/* 531 */     this.smoothLvlCbo.add("1");
/* 532 */     this.smoothLvlCbo.add("2");
/*     */ 
/* 534 */     this.smoothLvlCbo.select(2);
/*     */   }
/*     */ 
/*     */   private void createPatternSizeAttr()
/*     */   {
/* 541 */     Composite inCmp = new Composite(this.top, 0);
/* 542 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 545 */     this.chkBox[ChkBox.PATTERN_SIZE.ordinal()] = new Button(inCmp, 32);
/* 546 */     this.chkBox[ChkBox.PATTERN_SIZE.ordinal()].setLayoutData(new GridData(16, 28));
/* 547 */     this.chkBox[ChkBox.PATTERN_SIZE.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 551 */         Button btn = (Button)e.widget;
/* 552 */         if (btn.getSelection()) {
/* 553 */           LineAttrDlg.this.patternSizeLbl.setEnabled(true);
/* 554 */           LineAttrDlg.this.patternSizeSpinnerSlider.setEnabled(true);
/*     */         }
/*     */         else {
/* 557 */           LineAttrDlg.this.patternSizeLbl.setEnabled(false);
/* 558 */           LineAttrDlg.this.patternSizeSpinnerSlider.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 565 */     this.patternSizeLbl = new Label(inCmp, 16384);
/* 566 */     this.patternSizeLbl.setText("Pattern Size ");
/*     */ 
/* 568 */     this.patternSizeSpinnerSlider = 
/* 569 */       new SpinnerSlider(inCmp, 256, 1);
/* 570 */     this.patternSizeSpinnerSlider.setLayoutData(new GridData(140, 25));
/* 571 */     this.patternSizeSpinnerSlider.setMinimum(1);
/* 572 */     this.patternSizeSpinnerSlider.setMaximum(100);
/* 573 */     this.patternSizeSpinnerSlider.setIncrement(1);
/* 574 */     this.patternSizeSpinnerSlider.setPageIncrement(10);
/* 575 */     this.patternSizeSpinnerSlider.setDigits(1);
/*     */   }
/*     */ 
/*     */   protected boolean validatePatternSize(VerifyEvent ve)
/*     */   {
/* 580 */     boolean stat = false;
/*     */ 
/* 582 */     if ((ve.widget instanceof Text)) {
/* 583 */       Text wText = (Text)ve.widget;
/* 584 */       StringBuffer str = new StringBuffer(wText.getText());
/* 585 */       str.replace(ve.start, ve.end, ve.text);
/*     */ 
/* 587 */       if (str.toString().isEmpty()) return true;
/*     */       try
/*     */       {
/* 590 */         double value = Double.parseDouble(str.toString());
/* 591 */         if ((value >= MIN_PATTERN_SIZE) && (value <= MAX_PATTERN_SIZE)) {
/* 592 */           stat = true;
/*     */         }
/*     */         else
/* 595 */           stat = false;
/*     */       } catch (NumberFormatException e1) {
/* 597 */         stat = false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 602 */     return stat;
/*     */   }
/*     */ 
/*     */   private void createCloseAttr(Composite comp)
/*     */   {
/* 609 */     Composite inCmp = new Composite(comp, 0);
/* 610 */     inCmp.setLayout(getGridLayout(2, false, 0, 0, 0, 0));
/*     */ 
/* 612 */     this.chkBox[ChkBox.CLOSE.ordinal()] = new Button(inCmp, 32);
/* 613 */     this.chkBox[ChkBox.CLOSE.ordinal()].setLayoutData(new GridData(16, 28));
/* 614 */     this.chkBox[ChkBox.CLOSE.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 618 */         Button btn = (Button)e.widget;
/* 619 */         if (btn.getSelection()) {
/* 620 */           LineAttrDlg.this.closedBtn.setEnabled(true);
/*     */         }
/*     */         else
/* 623 */           LineAttrDlg.this.closedBtn.setEnabled(false);
/*     */       }
/*     */     });
/* 629 */     this.closedBtn = new Button(inCmp, 32);
/* 630 */     this.closedBtn.setText("Closed");
/*     */   }
/*     */ 
/*     */   private void createFillPatternAttr()
/*     */   {
/* 638 */     Composite inCmp = new Composite(this.top, 0);
/* 639 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 641 */     this.chkBox[ChkBox.FILL_PATTERN.ordinal()] = new Button(inCmp, 32);
/* 642 */     this.chkBox[ChkBox.FILL_PATTERN.ordinal()].setLayoutData(new GridData(16, 28));
/* 643 */     this.chkBox[ChkBox.FILL_PATTERN.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 647 */         Button btn = (Button)e.widget;
/* 648 */         if (btn.getSelection()) {
/* 649 */           LineAttrDlg.this.fillPatternLbl.setEnabled(true);
/* 650 */           LineAttrDlg.this.fillPatternCbo.setEnabled(true);
/*     */         }
/*     */         else {
/* 653 */           LineAttrDlg.this.fillPatternLbl.setEnabled(false);
/* 654 */           LineAttrDlg.this.fillPatternCbo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 661 */     this.fillPatternLbl = new Label(inCmp, 16384);
/* 662 */     this.fillPatternLbl.setText("Fill Pattern ");
/*     */ 
/* 664 */     this.fillPatternCbo = new SymbolCombo(inCmp);
/* 665 */     this.fillPatternCbo.setLayoutData(new GridData(10, 1));
/*     */ 
/* 667 */     if (FILL_PATTERNS == null) {
/* 668 */       FILL_PATTERNS = new String[FillPatternList.FillPattern.values().length];
/* 669 */       FILL_PATTERN_MENU_ITEMS = new HashMap();
/*     */ 
/* 671 */       int ii = 0;
/* 672 */       for (FillPatternList.FillPattern fp : FillPatternList.FillPattern.values()) {
/* 673 */         FILL_PATTERNS[(ii++)] = fp.name();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 678 */     Image[] icons = new Image[FILL_PATTERNS.length];
/*     */ 
/* 680 */     for (int ii = 0; ii < FILL_PATTERNS.length; ii++)
/*     */     {
/* 682 */       Image img = null;
/*     */       try {
/* 684 */         switch (ii) {
/*     */         case 7:
/* 686 */           img = getFillPatternIcon("icons/patt_solid.gif");
/* 687 */           break;
/*     */         case 8:
/* 689 */           img = getFillPatternIcon("icons/patt_trans.gif");
/* 690 */           break;
/*     */         case 0:
/* 693 */           Image orgImg = getFillPatternIcon("icons/patt00.gif");
/* 694 */           img = new Image(getShell().getDisplay(), orgImg.getImageData().scaledTo(40, 20));
/* 695 */           break;
/*     */         case 1:
/* 697 */           img = getFillPatternIcon("icons/patt01.gif");
/* 698 */           break;
/*     */         case 2:
/* 700 */           img = getFillPatternIcon("icons/patt02.gif");
/* 701 */           break;
/*     */         case 3:
/* 703 */           img = getFillPatternIcon("icons/patt03.gif");
/* 704 */           break;
/*     */         case 4:
/* 706 */           img = getFillPatternIcon("icons/patt04.gif");
/* 707 */           break;
/*     */         case 5:
/* 709 */           img = getFillPatternIcon("icons/patt05.gif");
/* 710 */           break;
/*     */         case 6:
/* 712 */           img = getFillPatternIcon("icons/patt06.gif");
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*     */ 
/* 720 */       FILL_PATTERN_MENU_ITEMS.put(FILL_PATTERNS[ii], img);
/* 721 */       icons[ii] = img;
/*     */     }
/*     */ 
/* 724 */     this.fillPatternCbo.setItems(FILL_PATTERNS, icons);
/*     */ 
/* 726 */     this.fillPatternCbo.select(0);
/*     */ 
/* 728 */     this.fillPatternCbo.setEnabled(false);
/*     */   }
/*     */ 
/*     */   private void createFillAttr(Composite comp)
/*     */   {
/* 737 */     Composite fillGrp = new Composite(comp, 0);
/* 738 */     fillGrp.setLayout(getGridLayout(2, false, 0, 0, 0, 0));
/*     */ 
/* 740 */     this.chkBox[ChkBox.FILL.ordinal()] = new Button(fillGrp, 32);
/* 741 */     this.chkBox[ChkBox.FILL.ordinal()].setLayoutData(new GridData(16, 33));
/* 742 */     this.chkBox[ChkBox.FILL.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 746 */         Button btn = (Button)e.widget;
/* 747 */         if (btn.getSelection()) {
/* 748 */           LineAttrDlg.this.filledBtn.setEnabled(true);
/*     */         }
/*     */         else
/* 751 */           LineAttrDlg.this.filledBtn.setEnabled(false);
/*     */       }
/*     */     });
/* 758 */     this.filledBtn = new Button(fillGrp, 32);
/* 759 */     this.filledBtn.setText("Filled");
/* 760 */     this.filledBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 764 */         Button btn = (Button)e.widget;
/* 765 */         if (btn.getSelection()) {
/* 766 */           LineAttrDlg.this.fillPatternCbo.setEnabled(true);
/* 767 */           LineAttrDlg.this.fillPatternCbo.select(0);
/*     */         }
/*     */         else {
/* 770 */           LineAttrDlg.this.fillPatternCbo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 782 */     if ((getShell() == null) || (getShell().isDisposed())) {
/* 783 */       create();
/*     */     }
/*     */ 
/* 787 */     if ((PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 787 */       .equalsIgnoreCase("MultiSelect")) && 
/* 788 */       (!(this instanceof GfaAttrDlg))) {
/* 789 */       enableChkBoxes(true);
/* 790 */       enableAllWidgets(false);
/*     */     }
/* 793 */     else if (this.chkBox != null) {
/* 794 */       enableChkBoxes(false);
/*     */     }
/*     */ 
/* 798 */     return super.open();
/*     */   }
/*     */ 
/*     */   private void enableChkBoxes(boolean flag)
/*     */   {
/* 807 */     if (!flag) {
/* 808 */       setAllChkBoxes();
/*     */     }
/* 810 */     for (ChkBox chk : ChkBox.values())
/* 811 */       if (this.chkBox[chk.ordinal()] != null) this.chkBox[chk.ordinal()].setVisible(flag);
/*     */   }
/*     */ 
/*     */   private void enableAllWidgets(boolean flag)
/*     */   {
/* 823 */     this.colorLbl.setEnabled(flag);
/*     */ 
/* 825 */     this.widthLbl.setEnabled(flag);
/* 826 */     this.widthSpinnerSlider.setEnabled(flag);
/*     */ 
/* 828 */     this.smoothLbl.setEnabled(flag);
/* 829 */     this.smoothLvlCbo.setEnabled(flag);
/*     */ 
/* 831 */     this.filledBtn.setEnabled(flag);
/* 832 */     this.closedBtn.setEnabled(flag);
/*     */ 
/* 834 */     this.patternSizeLbl.setEnabled(flag);
/* 835 */     this.patternSizeSpinnerSlider.setEnabled(flag);
/*     */ 
/* 837 */     this.fillPatternLbl.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   private void setAllChkBoxes()
/*     */   {
/* 846 */     for (ChkBox chk : ChkBox.values())
/* 847 */       if (this.chkBox[chk.ordinal()] != null) this.chkBox[chk.ordinal()].setSelection(true);
/*     */   }
/*     */ 
/*     */   private Image getFillPatternIcon(String iconLocation)
/*     */   {
/* 856 */     ImageDescriptor id = Activator.imageDescriptorFromPlugin(
/* 857 */       "gov.noaa.nws.ncep.ui.pgen", iconLocation);
/* 858 */     Image icon = null;
/* 859 */     if (id != null) {
/* 860 */       icon = id.createImage();
/*     */     }
/*     */ 
/* 863 */     return icon;
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 869 */     return null;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLinePoints()
/*     */   {
/* 875 */     return null;
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 882 */     PgenUtil.setSelectingMode();
/* 883 */     super.cancelPressed();
/*     */   }
/*     */ 
/*     */   private void createColorCloseFillAttr()
/*     */   {
/* 892 */     Composite inCmp = new Composite(this.top, 0);
/* 893 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 895 */     createColorAttr(inCmp);
/* 896 */     createCloseAttr(inCmp);
/* 897 */     createFillAttr(inCmp);
/*     */   }
/*     */ 
/*     */   protected static enum ChkBox
/*     */   {
/*  71 */     COLOR, WIDTH, SMOOTH, PATTERN_SIZE, CLOSE, FILL, FILL_PATTERN, LABEL;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.LineAttrDlg
 * JD-Core Version:    0.6.2
 */