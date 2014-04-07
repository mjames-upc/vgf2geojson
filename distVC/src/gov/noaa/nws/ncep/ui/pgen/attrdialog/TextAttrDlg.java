/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import java.io.PrintStream;
/*     */ import org.eclipse.swt.events.KeyAdapter;
/*     */ import org.eclipse.swt.events.KeyEvent;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Slider;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class TextAttrDlg extends AttrDlg
/*     */   implements IText
/*     */ {
/*  62 */   public static int[] FontSizeValue = { 10, 12, 14, 18, 24, 34 };
/*     */ 
/*  64 */   public static String[] FontName = { "Courier", "Nimbus Sans L", "Liberation Serif" };
/*     */   private static final String bgmask = " w/ bg mask";
/*     */   private static final int MIN_ROTATION = 0;
/*     */   private static final int MAX_ROTATION = 360;
/*     */   private static final int START_ROTATION = 0;
/*     */   private static final int INC_ROTATION = 1;
/*  75 */   private static TextAttrDlg INSTANCE = null;
/*     */ 
/*  77 */   private final int TEXT_WIDTH = 160;
/*  78 */   private final int TEXT_HEIGHT = 40;
/*     */ 
/*  80 */   private Composite top = null;
/*     */   private Label colorLbl;
/*  83 */   private ColorButtonSelector cs = null;
/*     */ 
/*  85 */   protected Text text = null;
/*     */   protected Label textLabel;
/*     */   protected Label boxLbl;
/*  89 */   private Combo boxCombo = null;
/*     */   private Label sizeLbl;
/*  92 */   protected Combo sizeCombo = null;
/*     */   private Label fontLbl;
/*  95 */   private Combo fontCombo = null;
/*     */   private Label styleLbl;
/*  98 */   private Combo styleCombo = null;
/*     */   private Label justLbl;
/* 101 */   private Combo justCombo = null;
/*     */   protected Label rotLbl;
/*     */   private Group rotGroup;
/*     */   private Group relGroup;
/* 106 */   protected Slider rotSlider = null;
/* 107 */   protected Text rotText = null;
/*     */ 
/* 109 */   protected Button screenBtn = null;
/* 110 */   protected Button northBtn = null;
/*     */   private Button[] chkBox;
/*     */ 
/*     */   protected TextAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 123 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static TextAttrDlg getInstance(Shell parShell)
/*     */   {
/* 136 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/* 140 */         INSTANCE = new TextAttrDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/* 144 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 149 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 159 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 162 */     GridLayout mainLayout = new GridLayout(3, false);
/* 163 */     mainLayout.marginHeight = 3;
/* 164 */     mainLayout.marginWidth = 3;
/* 165 */     mainLayout.horizontalSpacing = 3;
/* 166 */     this.top.setLayout(mainLayout);
/*     */ 
/* 169 */     initializeComponents();
/*     */ 
/* 171 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 181 */     getShell().setText("Text Attributes");
/*     */ 
/* 183 */     this.chkBox = new Button[8];
/*     */ 
/* 185 */     createTextAttr();
/* 186 */     createBoxAttr();
/* 187 */     createSizeAttr();
/* 188 */     createFontAttr();
/* 189 */     createStyleAttr();
/* 190 */     createJustAttr();
/* 191 */     createColorAttr();
/* 192 */     createRotationAttr();
/* 193 */     addSeparator(this.top.getParent());
/*     */   }
/*     */ 
/*     */   public String[] getString()
/*     */   {
/* 202 */     if (this.chkBox[ChkBox.TEXT.ordinal()].getSelection()) {
/* 203 */       return this.text.getText().split("\n");
/*     */     }
/*     */ 
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */   public float getFontSize()
/*     */   {
/* 216 */     if (this.chkBox[ChkBox.SIZE.ordinal()].getSelection()) {
/* 217 */       return FontSizeValue[this.sizeCombo.getSelectionIndex()];
/*     */     }
/*     */ 
/* 220 */     return (0.0F / 0.0F);
/*     */   }
/*     */ 
/*     */   public String getFontName()
/*     */   {
/* 228 */     if (this.chkBox[ChkBox.FONT.ordinal()].getSelection()) {
/* 229 */       return this.fontCombo.getText();
/*     */     }
/*     */ 
/* 232 */     return null;
/*     */   }
/*     */ 
/*     */   public IText.FontStyle getStyle()
/*     */   {
/* 240 */     if (this.chkBox[ChkBox.STYLE.ordinal()].getSelection()) {
/* 241 */       return IText.FontStyle.values()[this.styleCombo.getSelectionIndex()];
/*     */     }
/*     */ 
/* 244 */     return null;
/*     */   }
/*     */ 
/*     */   public IText.TextJustification getJustification()
/*     */   {
/* 252 */     if (this.chkBox[ChkBox.JUSTIFICATION.ordinal()].getSelection()) {
/* 253 */       return IText.TextJustification.values()[this.justCombo.getSelectionIndex()];
/*     */     }
/*     */ 
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */   public double getRotation()
/*     */   {
/* 264 */     if (this.chkBox[ChkBox.ROTATION.ordinal()].getSelection()) {
/* 265 */       return this.rotSlider.getSelection();
/*     */     }
/*     */ 
/* 268 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public IText.TextRotation getRotationRelativity()
/*     */   {
/* 278 */     if (this.chkBox[ChkBox.ROTATION.ordinal()].getSelection())
/*     */     {
/* 280 */       if (this.screenBtn.getSelection()) {
/* 281 */         return IText.TextRotation.SCREEN_RELATIVE;
/*     */       }
/*     */ 
/* 284 */       return IText.TextRotation.NORTH_RELATIVE;
/*     */     }
/*     */ 
/* 288 */     return null;
/*     */   }
/*     */ 
/*     */   public IText.DisplayType getDisplayType()
/*     */   {
/* 297 */     if (this.chkBox[ChkBox.BOX.ordinal()].getSelection())
/*     */     {
/* 299 */       for (IText.DisplayType type : IText.DisplayType.values()) {
/* 300 */         if (this.boxCombo.getText().startsWith(type.name())) {
/* 301 */           return type;
/*     */         }
/*     */       }
/* 304 */       return null;
/*     */     }
/*     */ 
/* 307 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean maskText()
/*     */   {
/* 312 */     if (this.chkBox[ChkBox.BOX.ordinal()].getSelection())
/*     */     {
/* 314 */       if (this.boxCombo.getText().contains(" w/ bg mask")) {
/* 315 */         return Boolean.valueOf(true);
/*     */       }
/*     */ 
/* 318 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/* 322 */     return null;
/*     */   }
/*     */ 
/*     */   public int getXOffset()
/*     */   {
/* 330 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getYOffset() {
/* 334 */     return 0;
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 341 */     if (this.chkBox[ChkBox.COLOR.ordinal()].getSelection())
/*     */     {
/* 345 */       Color[] colors = new Color[1];
/*     */ 
/* 347 */       colors[0] = new Color(this.cs.getColorValue().red, 
/* 348 */         this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*     */ 
/* 350 */       return colors;
/*     */     }
/*     */ 
/* 353 */     return null;
/*     */   }
/*     */ 
/*     */   public void setText(String[] txt)
/*     */   {
/* 365 */     StringBuilder result = new StringBuilder("");
/* 366 */     for (String st : txt) {
/* 367 */       result.append(st + "\n");
/*     */     }
/*     */ 
/* 370 */     int length = result.length();
/* 371 */     if (length > 0) {
/* 372 */       result.delete(length - 1, length - 1);
/*     */     }
/*     */ 
/* 375 */     this.text.setText(result.toString());
/*     */   }
/*     */ 
/*     */   public void setFontSize(float size)
/*     */   {
/* 383 */     int index = 0;
/* 384 */     for (int ii = 0; ii < FontSizeValue.length; ii++) {
/* 385 */       if ((int)size == FontSizeValue[ii]) {
/* 386 */         index = ii;
/* 387 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 391 */     this.sizeCombo.select(index);
/*     */   }
/*     */ 
/*     */   public void setFontName(String name)
/*     */   {
/* 399 */     for (String st : FontName)
/* 400 */       if (st.equalsIgnoreCase(name)) {
/* 401 */         this.fontCombo.setText(st);
/* 402 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void setStyle(IText.FontStyle style)
/*     */   {
/* 411 */     for (IText.FontStyle fs : IText.FontStyle.values())
/* 412 */       if (fs == style) {
/* 413 */         this.styleCombo.setText(fs.name());
/* 414 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void setJustification(IText.TextJustification just)
/*     */   {
/* 423 */     for (IText.TextJustification js : IText.TextJustification.values())
/* 424 */       if (js == just) {
/* 425 */         this.justCombo.setText(js.name());
/* 426 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void setRotation(double rot)
/*     */   {
/* 435 */     this.rotSlider.setSelection((int)rot);
/* 436 */     this.rotText.setText((int)rot);
/*     */   }
/*     */ 
/*     */   public void setRotationRelativity(IText.TextRotation trt)
/*     */   {
/* 443 */     if (trt == IText.TextRotation.SCREEN_RELATIVE) {
/* 444 */       this.screenBtn.setSelection(true);
/*     */     }
/*     */     else
/* 447 */       this.northBtn.setSelection(true);
/*     */   }
/*     */ 
/*     */   public void setBoxText(boolean mask, IText.DisplayType outline)
/*     */   {
/* 456 */     StringBuilder sb = new StringBuilder(outline.name());
/*     */ 
/* 458 */     if (mask) {
/* 459 */       sb.append(" w/ bg mask");
/*     */     }
/*     */ 
/* 462 */     this.boxCombo.setText(sb.toString());
/*     */   }
/*     */ 
/*     */   public void setXOffset(int xoff)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setYOffset(int yoff)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setColor(Color clr)
/*     */   {
/* 482 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute iattr)
/*     */   {
/* 491 */     if ((iattr instanceof IText)) {
/* 492 */       IText attr = (IText)iattr;
/* 493 */       setText(attr.getString());
/* 494 */       setFontName(attr.getFontName());
/* 495 */       setFontSize(attr.getFontSize());
/* 496 */       setJustification(attr.getJustification());
/* 497 */       setRotation(attr.getRotation());
/* 498 */       setRotationRelativity(attr.getRotationRelativity());
/* 499 */       setStyle(attr.getStyle());
/* 500 */       setXOffset(attr.getXOffset());
/* 501 */       setYOffset(attr.getYOffset());
/* 502 */       setBoxText(attr.maskText().booleanValue(), attr.getDisplayType());
/*     */ 
/* 504 */       Color clr = attr.getColors()[0];
/* 505 */       if (clr != null) setColor(clr);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void enableChkBoxes(boolean flag)
/*     */   {
/* 515 */     if (!flag) {
/* 516 */       setAllChkBoxes();
/*     */     }
/* 518 */     for (ChkBox chk : ChkBox.values())
/* 519 */       this.chkBox[chk.ordinal()].setVisible(flag);
/*     */   }
/*     */ 
/*     */   private void enableAllWidgets(boolean flag)
/*     */   {
/* 530 */     this.textLabel.setEnabled(flag);
/* 531 */     this.text.setEnabled(flag);
/*     */ 
/* 533 */     this.boxLbl.setEnabled(flag);
/* 534 */     this.boxCombo.setEnabled(flag);
/*     */ 
/* 536 */     this.sizeLbl.setEnabled(flag);
/* 537 */     this.sizeCombo.setEnabled(flag);
/*     */ 
/* 539 */     this.fontLbl.setEnabled(flag);
/* 540 */     this.fontCombo.setEnabled(flag);
/*     */ 
/* 542 */     this.styleLbl.setEnabled(flag);
/* 543 */     this.styleCombo.setEnabled(flag);
/*     */ 
/* 545 */     this.justLbl.setEnabled(flag);
/* 546 */     this.justCombo.setEnabled(flag);
/*     */ 
/* 548 */     this.colorLbl.setEnabled(flag);
/*     */ 
/* 550 */     this.rotLbl.setEnabled(flag);
/* 551 */     this.rotGroup.setEnabled(flag);
/* 552 */     this.rotSlider.setEnabled(flag);
/* 553 */     this.rotText.setEnabled(flag);
/*     */ 
/* 555 */     this.relGroup.setEnabled(flag);
/* 556 */     this.screenBtn.setEnabled(flag);
/* 557 */     this.northBtn.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   private void setAllChkBoxes()
/*     */   {
/* 565 */     for (ChkBox chk : ChkBox.values())
/* 566 */       this.chkBox[chk.ordinal()].setSelection(true);
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 573 */     create();
/*     */ 
/* 576 */     if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 576 */       .equalsIgnoreCase("MultiSelect")) {
/* 577 */       enableChkBoxes(true);
/* 578 */       enableAllWidgets(false);
/*     */     }
/*     */     else {
/* 581 */       enableChkBoxes(false);
/*     */     }
/*     */ 
/* 584 */     return super.open();
/*     */   }
/*     */ 
/*     */   private void createTextAttr()
/*     */   {
/* 592 */     this.chkBox[ChkBox.TEXT.ordinal()] = new Button(this.top, 32);
/* 593 */     this.chkBox[ChkBox.TEXT.ordinal()].setLayoutData(new GridData(16, 28));
/* 594 */     this.chkBox[ChkBox.TEXT.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 598 */         Button btn = (Button)e.widget;
/* 599 */         if (btn.getSelection()) {
/* 600 */           System.out.println("Text checked?");
/* 601 */           TextAttrDlg.this.textLabel.setEnabled(true);
/* 602 */           TextAttrDlg.this.text.setEnabled(true);
/*     */         }
/*     */         else {
/* 605 */           System.out.println("Text un-checked!!!!!!!!!!!!");
/* 606 */           TextAttrDlg.this.textLabel.setEnabled(false);
/* 607 */           TextAttrDlg.this.text.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 614 */     this.textLabel = new Label(this.top, 16384);
/* 615 */     this.textLabel.setText("Text:");
/*     */ 
/* 617 */     int style = 2818;
/* 618 */     this.text = new Text(this.top, style);
/* 619 */     this.text.setLayoutData(new GridData(160, 40));
/* 620 */     this.text.setEditable(true);
/*     */   }
/*     */ 
/*     */   private void createBoxAttr()
/*     */   {
/* 629 */     this.chkBox[ChkBox.BOX.ordinal()] = new Button(this.top, 32);
/* 630 */     this.chkBox[ChkBox.BOX.ordinal()].setLayoutData(new GridData(16, 28));
/* 631 */     this.chkBox[ChkBox.BOX.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 635 */         Button btn = (Button)e.widget;
/* 636 */         if (btn.getSelection()) {
/* 637 */           TextAttrDlg.this.boxLbl.setEnabled(true);
/* 638 */           TextAttrDlg.this.boxCombo.setEnabled(true);
/*     */         }
/*     */         else {
/* 641 */           TextAttrDlg.this.boxLbl.setEnabled(false);
/* 642 */           TextAttrDlg.this.boxCombo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 648 */     this.boxLbl = new Label(this.top, 16384);
/* 649 */     this.boxLbl.setText("Box:");
/*     */ 
/* 651 */     this.boxCombo = new Combo(this.top, 12);
/* 652 */     for (IText.DisplayType type : IText.DisplayType.values()) {
/* 653 */       this.boxCombo.add(type.name());
/* 654 */       this.boxCombo.add(type.name() + " w/ bg mask");
/*     */     }
/*     */ 
/* 657 */     this.boxCombo.select(0);
/*     */   }
/*     */ 
/*     */   private void createSizeAttr()
/*     */   {
/* 665 */     this.chkBox[ChkBox.SIZE.ordinal()] = new Button(this.top, 32);
/* 666 */     this.chkBox[ChkBox.SIZE.ordinal()].setLayoutData(new GridData(16, 28));
/* 667 */     this.chkBox[ChkBox.SIZE.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 671 */         Button btn = (Button)e.widget;
/* 672 */         if (btn.getSelection()) {
/* 673 */           TextAttrDlg.this.sizeLbl.setEnabled(true);
/* 674 */           TextAttrDlg.this.sizeCombo.setEnabled(true);
/*     */         }
/*     */         else {
/* 677 */           TextAttrDlg.this.sizeLbl.setEnabled(false);
/* 678 */           TextAttrDlg.this.sizeCombo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 685 */     this.sizeLbl = new Label(this.top, 16384);
/* 686 */     this.sizeLbl.setText("Size:");
/*     */ 
/* 688 */     this.sizeCombo = new Combo(this.top, 12);
/*     */ 
/* 690 */     for (FontSizeName fs : FontSizeName.values()) {
/* 691 */       this.sizeCombo.add(fs.name());
/*     */     }
/*     */ 
/* 694 */     this.sizeCombo.select(2);
/*     */   }
/*     */ 
/*     */   private void createFontAttr()
/*     */   {
/* 702 */     this.chkBox[ChkBox.FONT.ordinal()] = new Button(this.top, 32);
/* 703 */     this.chkBox[ChkBox.FONT.ordinal()].setLayoutData(new GridData(16, 28));
/* 704 */     this.chkBox[ChkBox.FONT.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 708 */         Button btn = (Button)e.widget;
/* 709 */         if (btn.getSelection()) {
/* 710 */           TextAttrDlg.this.fontLbl.setEnabled(true);
/* 711 */           TextAttrDlg.this.fontCombo.setEnabled(true);
/*     */         }
/*     */         else {
/* 714 */           TextAttrDlg.this.fontLbl.setEnabled(false);
/* 715 */           TextAttrDlg.this.fontCombo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 721 */     this.fontLbl = new Label(this.top, 16384);
/* 722 */     this.fontLbl.setText("Font:");
/*     */ 
/* 724 */     this.fontCombo = new Combo(this.top, 12);
/*     */ 
/* 726 */     for (String st : FontName) {
/* 727 */       this.fontCombo.add(st);
/*     */     }
/*     */ 
/* 730 */     this.fontCombo.select(0);
/*     */   }
/*     */ 
/*     */   private void createStyleAttr()
/*     */   {
/* 737 */     this.chkBox[ChkBox.STYLE.ordinal()] = new Button(this.top, 32);
/* 738 */     this.chkBox[ChkBox.STYLE.ordinal()].setLayoutData(new GridData(16, 28));
/* 739 */     this.chkBox[ChkBox.STYLE.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 743 */         Button btn = (Button)e.widget;
/* 744 */         if (btn.getSelection()) {
/* 745 */           TextAttrDlg.this.styleLbl.setEnabled(true);
/* 746 */           TextAttrDlg.this.styleCombo.setEnabled(true);
/*     */         }
/*     */         else {
/* 749 */           TextAttrDlg.this.styleLbl.setEnabled(false);
/* 750 */           TextAttrDlg.this.styleCombo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 756 */     this.styleLbl = new Label(this.top, 16384);
/* 757 */     this.styleLbl.setText("Style:");
/*     */ 
/* 759 */     this.styleCombo = new Combo(this.top, 12);
/*     */ 
/* 761 */     for (IText.FontStyle fs : IText.FontStyle.values()) {
/* 762 */       this.styleCombo.add(fs.name());
/*     */     }
/*     */ 
/* 765 */     this.styleCombo.select(0);
/*     */   }
/*     */ 
/*     */   private void createJustAttr()
/*     */   {
/* 773 */     this.chkBox[ChkBox.JUSTIFICATION.ordinal()] = new Button(this.top, 32);
/* 774 */     this.chkBox[ChkBox.JUSTIFICATION.ordinal()].setLayoutData(new GridData(16, 28));
/* 775 */     this.chkBox[ChkBox.JUSTIFICATION.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 779 */         Button btn = (Button)e.widget;
/* 780 */         if (btn.getSelection()) {
/* 781 */           TextAttrDlg.this.justLbl.setEnabled(true);
/* 782 */           TextAttrDlg.this.justCombo.setEnabled(true);
/*     */         }
/*     */         else {
/* 785 */           TextAttrDlg.this.justLbl.setEnabled(false);
/* 786 */           TextAttrDlg.this.justCombo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 793 */     this.justLbl = new Label(this.top, 16384);
/* 794 */     this.justLbl.setText("Just:");
/*     */ 
/* 796 */     this.justCombo = new Combo(this.top, 12);
/*     */ 
/* 798 */     for (IText.TextJustification js : IText.TextJustification.values()) {
/* 799 */       this.justCombo.add(js.name());
/*     */     }
/*     */ 
/* 802 */     this.justCombo.select(1);
/*     */   }
/*     */ 
/*     */   private void createColorAttr()
/*     */   {
/* 809 */     this.chkBox[ChkBox.COLOR.ordinal()] = new Button(this.top, 32);
/* 810 */     this.chkBox[ChkBox.COLOR.ordinal()].setLayoutData(new GridData(16, 28));
/* 811 */     this.chkBox[ChkBox.COLOR.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 815 */         Button btn = (Button)e.widget;
/* 816 */         if (btn.getSelection()) {
/* 817 */           TextAttrDlg.this.colorLbl.setEnabled(true);
/*     */         }
/*     */         else
/* 820 */           TextAttrDlg.this.colorLbl.setEnabled(false);
/*     */       }
/*     */     });
/* 827 */     this.colorLbl = new Label(this.top, 16384);
/* 828 */     this.colorLbl.setText("Color:");
/* 829 */     this.cs = new ColorButtonSelector(this.top);
/* 830 */     this.cs.setColorValue(new RGB(0, 255, 0));
/*     */   }
/*     */ 
/*     */   private void createRotationAttr()
/*     */   {
/* 838 */     this.chkBox[ChkBox.ROTATION.ordinal()] = new Button(this.top, 32);
/* 839 */     this.chkBox[ChkBox.ROTATION.ordinal()].setLayoutData(new GridData(16, 28));
/* 840 */     this.chkBox[ChkBox.ROTATION.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 844 */         Button btn = (Button)e.widget;
/* 845 */         if (btn.getSelection()) {
/* 846 */           TextAttrDlg.this.rotLbl.setEnabled(true);
/* 847 */           TextAttrDlg.this.rotGroup.setEnabled(true);
/* 848 */           TextAttrDlg.this.rotSlider.setEnabled(true);
/* 849 */           TextAttrDlg.this.rotText.setEnabled(true);
/*     */ 
/* 851 */           TextAttrDlg.this.relGroup.setEnabled(true);
/* 852 */           TextAttrDlg.this.screenBtn.setEnabled(true);
/* 853 */           TextAttrDlg.this.northBtn.setEnabled(true);
/*     */         }
/*     */         else {
/* 856 */           TextAttrDlg.this.rotLbl.setEnabled(false);
/* 857 */           TextAttrDlg.this.rotGroup.setEnabled(false);
/* 858 */           TextAttrDlg.this.rotSlider.setEnabled(false);
/* 859 */           TextAttrDlg.this.rotText.setEnabled(false);
/*     */ 
/* 861 */           TextAttrDlg.this.relGroup.setEnabled(false);
/* 862 */           TextAttrDlg.this.screenBtn.setEnabled(false);
/* 863 */           TextAttrDlg.this.northBtn.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 869 */     this.rotLbl = new Label(this.top, 16384);
/* 870 */     this.rotLbl.setText("Rot:");
/*     */ 
/* 873 */     this.rotGroup = new Group(this.top, 0);
/* 874 */     GridLayout gl = new GridLayout(2, false);
/* 875 */     this.rotGroup.setLayout(gl);
/*     */ 
/* 877 */     this.rotSlider = new Slider(this.rotGroup, 256);
/* 878 */     this.rotSlider.setValues(0, 0, 360, 1, 1, 5);
/*     */ 
/* 880 */     this.rotSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 882 */         TextAttrDlg.this.rotText.setText(TextAttrDlg.this.rotSlider.getSelection());
/*     */       }
/*     */     });
/* 886 */     this.rotText = new Text(this.rotGroup, 2052);
/* 887 */     this.rotText.setLayoutData(new GridData(20, 10));
/* 888 */     this.rotText.setEditable(true);
/* 889 */     this.rotText.setText("0");
/* 890 */     this.rotText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 892 */         int value = 0;
/*     */         try {
/* 894 */           value = Integer.parseInt(TextAttrDlg.this.rotText.getText());
/* 895 */           if ((value >= 0) && (value < 360)) {
/* 896 */             TextAttrDlg.this.rotSlider.setSelection(value);
/* 897 */             TextAttrDlg.this.rotText.setToolTipText("");
/*     */           }
/*     */           else {
/* 900 */             TextAttrDlg.this.rotText.setToolTipText("Only integer values between 0 and 360 are accepted.");
/*     */           }
/*     */         } catch (NumberFormatException e1) {
/* 903 */           TextAttrDlg.this.rotText.setToolTipText("Only integer values between 0 and 360 are accepted.");
/*     */         }
/*     */       }
/*     */     });
/* 909 */     Button scnChkBox = new Button(this.top, 32);
/* 910 */     scnChkBox.setLayoutData(new GridData(16, 28));
/* 911 */     scnChkBox.setVisible(false);
/*     */ 
/* 913 */     Label grpLbl = new Label(this.top, 16384);
/* 914 */     grpLbl.setText("   ");
/*     */ 
/* 916 */     this.relGroup = new Group(this.top, 0);
/* 917 */     this.relGroup.setLayout(gl);
/*     */ 
/* 919 */     this.screenBtn = new Button(this.relGroup, 16);
/* 920 */     this.screenBtn.setText("Screen");
/* 921 */     this.screenBtn.setSelection(true);
/*     */ 
/* 923 */     this.northBtn = new Button(this.relGroup, 16);
/* 924 */     this.northBtn.setText("North");
/*     */   }
/*     */ 
/*     */   public Coordinate getPosition()
/*     */   {
/* 930 */     return null;
/*     */   }
/*     */ 
/*     */   public Color getTextColor()
/*     */   {
/* 936 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean isClear()
/*     */   {
/* 941 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public Coordinate getLocation()
/*     */   {
/* 946 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean getHide()
/*     */   {
/* 951 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public Boolean getAuto()
/*     */   {
/* 956 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   private static enum ChkBox
/*     */   {
/*  68 */     TEXT, BOX, SIZE, FONT, STYLE, JUSTIFICATION, COLOR, ROTATION;
/*     */   }
/*     */ 
/*     */   public static enum FontSizeName
/*     */   {
/*  61 */     TINY, SMALL, MEDIUM, LARGE, HUGE, GIANT;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.TextAttrDlg
 * JD-Core Version:    0.6.2
 */