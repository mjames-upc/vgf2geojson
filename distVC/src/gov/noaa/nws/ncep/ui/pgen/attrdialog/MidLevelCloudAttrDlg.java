/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IMidCloudText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class MidLevelCloudAttrDlg extends AttrDlg
/*     */   implements IMidCloudText
/*     */ {
/*  65 */   public static int[] FontSizeValue = { 10, 12, 14, 18, 24, 34 };
/*     */ 
/*  67 */   public static final String[] FontName = { "Courier", "Nimbus Sans L", "Liberation Serif" };
/*     */ 
/*  70 */   private static final String[] ICING_LIST = { "ICING_00", "ICING_01", "ICING_02", 
/*  71 */     "ICING_03", "ICING_04", "ICING_05", 
/*  72 */     "ICING_06", "ICING_07", "ICING_08", 
/*  73 */     "ICING_09", "ICING_10" };
/*     */ 
/*  75 */   private static final String[] TURB_LIST = { "TURBULENCE_0", "TURBULENCE_1", "TURBULENCE_2", 
/*  76 */     "TURBULENCE_3", "TURBULENCE_4", 
/*  77 */     "TURBULENCE_4|TURBULENCE_6", "TURBULENCE_5", 
/*  78 */     "TURBULENCE_6", "TURBULENCE_6|TURBULENCE_7", 
/*  79 */     "TURBULENCE_7", "TURBULENCE_8" };
/*     */ 
/*  83 */   static MidLevelCloudAttrDlg INSTANCE = null;
/*     */ 
/*  85 */   private Composite top = null;
/*  86 */   Composite multigroup = null;
/*     */   private Label colorLbl;
/*  89 */   private ColorButtonSelector cs = null;
/*     */   private List<Button> cloudTypeButtons;
/*     */   private Text turbLevel;
/*     */   private Text icingLevel;
/*     */   private Text tstormLevel;
/*     */   private SymbolCombo turbCombo;
/*     */   private SymbolCombo iceCombo;
/*     */   private List<Button> tstormTypeButtons;
/*     */   private Label typeLabel;
/*     */   private Label sizeLbl;
/* 103 */   private Combo sizeCombo = null;
/*     */   private Label fontLbl;
/* 106 */   private Combo fontCombo = null;
/*     */   private Label styleLbl;
/* 109 */   private Combo styleCombo = null;
/*     */   private Label justLbl;
/* 112 */   private Combo justCombo = null;
/*     */   private Button[] chkBox;
/*     */   private boolean multiselectMode;
/*     */ 
/*     */   protected MidLevelCloudAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 124 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static MidLevelCloudAttrDlg getInstance(Shell parShell)
/*     */   {
/* 137 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/* 141 */         INSTANCE = new MidLevelCloudAttrDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/* 145 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 150 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 160 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 163 */     GridLayout mainLayout = new GridLayout(1, false);
/* 164 */     mainLayout.marginHeight = 3;
/* 165 */     mainLayout.marginWidth = 3;
/* 166 */     this.top.setLayout(mainLayout);
/*     */ 
/* 169 */     initializeComponents();
/*     */ 
/* 171 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 181 */     getShell().setText("Mid Level Cloud Attributes");
/*     */ 
/* 183 */     this.chkBox = new Button[5];
/*     */ 
/* 188 */     this.multiselectMode = true;
/*     */ 
/* 190 */     if (!PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 190 */       .equalsIgnoreCase("MultiSelect")) {
/* 191 */       createCloudTypes();
/*     */ 
/* 193 */       createTurbulenceSection();
/* 194 */       createIcingSection();
/* 195 */       createThunderstormSection();
/* 196 */       this.multiselectMode = false;
/*     */     }
/*     */ 
/* 199 */     this.multigroup = new Composite(this.top, 0);
/* 200 */     this.multigroup.setLayout(new GridLayout(3, false));
/*     */ 
/* 205 */     createSizeAttr();
/*     */ 
/* 211 */     createFontAttr();
/*     */ 
/* 216 */     createStyleAttr();
/*     */ 
/* 221 */     createJustAttr();
/*     */ 
/* 226 */     createColorAttr();
/*     */ 
/* 229 */     if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 229 */       .equalsIgnoreCase("MultiSelect")) {
/* 230 */       enableChkBoxes(true);
/* 231 */       enableAllWidgets(false);
/*     */     }
/*     */     else {
/* 234 */       enableChkBoxes(false);
/*     */     }
/*     */ 
/* 237 */     this.top.pack();
/*     */   }
/*     */ 
/*     */   public float getFontSize()
/*     */   {
/* 245 */     if (this.chkBox[ChkBox.SIZE.ordinal()].getSelection()) {
/* 246 */       return FontSizeValue[this.sizeCombo.getSelectionIndex()];
/*     */     }
/*     */ 
/* 249 */     return (0.0F / 0.0F);
/*     */   }
/*     */ 
/*     */   public String getFontName()
/*     */   {
/* 257 */     if (this.chkBox[ChkBox.FONT.ordinal()].getSelection()) {
/* 258 */       return this.fontCombo.getText();
/*     */     }
/*     */ 
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */   public IText.FontStyle getStyle()
/*     */   {
/* 269 */     if (this.chkBox[ChkBox.STYLE.ordinal()].getSelection()) {
/* 270 */       return IText.FontStyle.values()[this.styleCombo.getSelectionIndex()];
/*     */     }
/*     */ 
/* 273 */     return null;
/*     */   }
/*     */ 
/*     */   public IText.TextJustification getJustification()
/*     */   {
/* 281 */     if (this.chkBox[ChkBox.JUST.ordinal()].getSelection()) {
/* 282 */       return IText.TextJustification.values()[this.justCombo.getSelectionIndex()];
/*     */     }
/*     */ 
/* 285 */     return null;
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 293 */     if (this.chkBox[ChkBox.COLOR.ordinal()].getSelection())
/*     */     {
/* 296 */       Color[] colors = new Color[1];
/*     */ 
/* 298 */       colors[0] = new Color(this.cs.getColorValue().red, 
/* 299 */         this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*     */ 
/* 301 */       return colors;
/*     */     }
/*     */ 
/* 304 */     return null;
/*     */   }
/*     */ 
/*     */   public void setFontSize(float size)
/*     */   {
/* 314 */     int index = 0;
/* 315 */     for (int ii = 0; ii < FontSizeValue.length; ii++) {
/* 316 */       if ((int)size == FontSizeValue[ii]) {
/* 317 */         index = ii;
/* 318 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 322 */     this.sizeCombo.select(index);
/*     */   }
/*     */ 
/*     */   public void setFontName(String name)
/*     */   {
/* 330 */     for (String st : FontName)
/* 331 */       if (st.equalsIgnoreCase(name)) {
/* 332 */         this.fontCombo.setText(st);
/* 333 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void setStyle(IText.FontStyle style)
/*     */   {
/* 342 */     for (IText.FontStyle fs : IText.FontStyle.values())
/* 343 */       if (fs == style) {
/* 344 */         this.styleCombo.setText(fs.name());
/* 345 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void setJustification(IText.TextJustification just)
/*     */   {
/* 354 */     for (IText.TextJustification js : IText.TextJustification.values())
/* 355 */       if (js == just) {
/* 356 */         this.justCombo.setText(js.name());
/* 357 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void setColor(Color clr)
/*     */   {
/* 368 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute attr)
/*     */   {
/* 377 */     if ((attr instanceof IMidCloudText)) {
/* 378 */       IMidCloudText mid = (IMidCloudText)attr;
/* 379 */       setButtons(this.cloudTypeButtons, mid.getCloudTypes());
/*     */ 
/* 381 */       setTurbulencePattern(mid.getTurbulencePattern());
/* 382 */       setTurbulenceLevels(mid.getTurbulenceLevels());
/* 383 */       setIcingPattern(mid.getIcingPattern());
/* 384 */       setIcingLevels(mid.getIcingLevels());
/* 385 */       setButtons(this.tstormTypeButtons, mid.getTstormTypes());
/* 386 */       setTstormLevels(mid.getTstormLevels());
/*     */ 
/* 388 */       setFontName(mid.getFontName());
/* 389 */       setFontSize(mid.getFontSize());
/* 390 */       setJustification(mid.getJustification());
/* 391 */       setStyle(mid.getStyle());
/*     */ 
/* 393 */       Color clr = attr.getColors()[0];
/* 394 */       if (clr != null) setColor(clr);
/* 395 */       this.top.pack();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setButtons(List<Button> checkboxes, String types)
/*     */   {
/* 402 */     for (Button btn : checkboxes)
/* 403 */       if (types.contains(btn.getText())) {
/* 404 */         btn.setSelection(true);
/*     */       }
/*     */       else
/* 407 */         btn.setSelection(false);
/*     */   }
/*     */ 
/*     */   private void createCloudTypes()
/*     */   {
/* 418 */     this.cloudTypeButtons = new ArrayList();
/*     */ 
/* 420 */     this.typeLabel = new Label(this.top, 0);
/* 421 */     this.typeLabel.setText("Cloud Type:");
/*     */ 
/* 423 */     Composite c1 = new Composite(this.top, 0);
/* 424 */     c1.setLayout(new GridLayout(4, true));
/*     */ 
/* 426 */     for (CloudTypes type : CloudTypes.values()) {
/* 427 */       Button btn = new Button(c1, 32);
/* 428 */       btn.setText(type.toString());
/* 429 */       this.cloudTypeButtons.add(btn);
/*     */     }
/*     */ 
/* 432 */     Label sep1 = new Label(this.top, 258);
/* 433 */     sep1.setLayoutData(new GridData(768));
/*     */   }
/*     */ 
/*     */   private void createTurbulenceSection()
/*     */   {
/* 465 */     Composite c3 = new Composite(this.top, 0);
/* 466 */     c3.setLayout(new GridLayout(2, false));
/*     */ 
/* 468 */     Label turbLabel = new Label(c3, 0);
/* 469 */     turbLabel.setText("Turb Type:");
/*     */ 
/* 471 */     this.turbCombo = new SymbolCombo(c3);
/* 472 */     this.turbCombo.setLayoutData(new GridData(10, 1));
/* 473 */     this.turbCombo.setItems(TURB_LIST);
/* 474 */     this.turbCombo.select(4);
/*     */ 
/* 476 */     Label turbLevelText = new Label(c3, 0);
/* 477 */     turbLevelText.setText("Top/Base:");
/*     */ 
/* 479 */     this.turbLevel = new Text(c3, 2052);
/*     */ 
/* 481 */     Label sep3 = new Label(this.top, 258);
/* 482 */     sep3.setLayoutData(new GridData(768));
/*     */   }
/*     */ 
/*     */   private void createIcingSection()
/*     */   {
/* 490 */     Composite c4 = new Composite(this.top, 0);
/* 491 */     c4.setLayout(new GridLayout(2, true));
/*     */ 
/* 493 */     Label iceLabel = new Label(c4, 0);
/* 494 */     iceLabel.setText("Icing Type:");
/*     */ 
/* 497 */     this.iceCombo = new SymbolCombo(c4);
/* 498 */     this.iceCombo.setLayoutData(new GridData(10, 1));
/* 499 */     this.iceCombo.setItems(ICING_LIST);
/*     */ 
/* 501 */     Label iceLevelText = new Label(c4, 0);
/* 502 */     iceLevelText.setText("Top/Base:");
/*     */ 
/* 504 */     this.icingLevel = new Text(c4, 2052);
/*     */ 
/* 506 */     Label sep4 = new Label(this.top, 258);
/* 507 */     sep4.setLayoutData(new GridData(768));
/*     */   }
/*     */ 
/*     */   private void createThunderstormSection()
/*     */   {
/* 515 */     this.tstormTypeButtons = new ArrayList();
/*     */ 
/* 517 */     Label tstormLabel = new Label(this.top, 0);
/* 518 */     tstormLabel.setText("Thunderstorm Type:");
/*     */ 
/* 520 */     Composite c5 = new Composite(this.top, 0);
/* 521 */     c5.setLayout(new GridLayout(4, true));
/*     */ 
/* 523 */     for (TstormTypes type : TstormTypes.values()) {
/* 524 */       Button btn = new Button(c5, 32);
/* 525 */       btn.setText(type.toString());
/* 526 */       this.tstormTypeButtons.add(btn);
/*     */     }
/*     */ 
/* 529 */     Composite c6 = new Composite(this.top, 0);
/* 530 */     c6.setLayout(new GridLayout(2, true));
/*     */ 
/* 532 */     Label tstormText = new Label(c6, 0);
/* 533 */     tstormText.setText("Top/Base:");
/*     */ 
/* 535 */     this.tstormLevel = new Text(c6, 2052);
/*     */ 
/* 537 */     Label sep5 = new Label(this.top, 258);
/* 538 */     sep5.setLayoutData(new GridData(768));
/*     */   }
/*     */ 
/*     */   private void createSizeAttr()
/*     */   {
/* 546 */     this.chkBox[ChkBox.SIZE.ordinal()] = new Button(this.multigroup, 32);
/* 547 */     this.chkBox[ChkBox.SIZE.ordinal()].setLayoutData(new GridData(16, 33));
/* 548 */     this.chkBox[ChkBox.SIZE.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 552 */         Button btn = (Button)e.widget;
/* 553 */         if (btn.getSelection()) {
/* 554 */           MidLevelCloudAttrDlg.this.sizeLbl.setEnabled(true);
/* 555 */           MidLevelCloudAttrDlg.this.sizeCombo.setEnabled(true);
/*     */         }
/*     */         else {
/* 558 */           MidLevelCloudAttrDlg.this.sizeLbl.setEnabled(false);
/* 559 */           MidLevelCloudAttrDlg.this.sizeCombo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 565 */     this.sizeLbl = new Label(this.multigroup, 16384);
/* 566 */     this.sizeLbl.setText("Size:");
/*     */ 
/* 568 */     this.sizeCombo = new Combo(this.multigroup, 12);
/*     */ 
/* 570 */     for (FontSizeName fs : FontSizeName.values()) {
/* 571 */       this.sizeCombo.add(fs.name());
/*     */     }
/* 573 */     this.sizeCombo.select(2);
/*     */   }
/*     */ 
/*     */   private void createFontAttr()
/*     */   {
/* 582 */     this.chkBox[ChkBox.FONT.ordinal()] = new Button(this.multigroup, 32);
/* 583 */     this.chkBox[ChkBox.FONT.ordinal()].setLayoutData(new GridData(16, 33));
/* 584 */     this.chkBox[ChkBox.FONT.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 588 */         Button btn = (Button)e.widget;
/* 589 */         if (btn.getSelection()) {
/* 590 */           MidLevelCloudAttrDlg.this.fontLbl.setEnabled(true);
/* 591 */           MidLevelCloudAttrDlg.this.fontCombo.setEnabled(true);
/*     */         }
/*     */         else {
/* 594 */           MidLevelCloudAttrDlg.this.fontLbl.setEnabled(false);
/* 595 */           MidLevelCloudAttrDlg.this.fontCombo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 602 */     this.fontLbl = new Label(this.multigroup, 16384);
/* 603 */     this.fontLbl.setText("Font:");
/*     */ 
/* 605 */     this.fontCombo = new Combo(this.multigroup, 12);
/*     */ 
/* 607 */     for (String st : FontName) {
/* 608 */       this.fontCombo.add(st);
/*     */     }
/* 610 */     this.fontCombo.select(0);
/*     */   }
/*     */ 
/*     */   private void createStyleAttr()
/*     */   {
/* 618 */     this.chkBox[ChkBox.STYLE.ordinal()] = new Button(this.multigroup, 32);
/* 619 */     this.chkBox[ChkBox.STYLE.ordinal()].setLayoutData(new GridData(16, 33));
/* 620 */     this.chkBox[ChkBox.STYLE.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 624 */         Button btn = (Button)e.widget;
/* 625 */         if (btn.getSelection()) {
/* 626 */           MidLevelCloudAttrDlg.this.styleLbl.setEnabled(true);
/* 627 */           MidLevelCloudAttrDlg.this.styleCombo.setEnabled(true);
/*     */         }
/*     */         else {
/* 630 */           MidLevelCloudAttrDlg.this.styleLbl.setEnabled(false);
/* 631 */           MidLevelCloudAttrDlg.this.styleCombo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 638 */     this.styleLbl = new Label(this.multigroup, 16384);
/* 639 */     this.styleLbl.setText("Style:");
/*     */ 
/* 641 */     this.styleCombo = new Combo(this.multigroup, 12);
/*     */ 
/* 643 */     for (IText.FontStyle fs : IText.FontStyle.values()) {
/* 644 */       this.styleCombo.add(fs.name());
/*     */     }
/* 646 */     this.styleCombo.select(0);
/*     */   }
/*     */ 
/*     */   private void createJustAttr()
/*     */   {
/* 653 */     this.chkBox[ChkBox.JUST.ordinal()] = new Button(this.multigroup, 32);
/* 654 */     this.chkBox[ChkBox.JUST.ordinal()].setLayoutData(new GridData(16, 33));
/* 655 */     this.chkBox[ChkBox.JUST.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 659 */         Button btn = (Button)e.widget;
/* 660 */         if (btn.getSelection()) {
/* 661 */           MidLevelCloudAttrDlg.this.justLbl.setEnabled(true);
/* 662 */           MidLevelCloudAttrDlg.this.justCombo.setEnabled(true);
/*     */         }
/*     */         else {
/* 665 */           MidLevelCloudAttrDlg.this.justLbl.setEnabled(false);
/* 666 */           MidLevelCloudAttrDlg.this.justCombo.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 671 */     this.justLbl = new Label(this.multigroup, 16384);
/* 672 */     this.justLbl.setText("Just:");
/*     */ 
/* 674 */     this.justCombo = new Combo(this.multigroup, 12);
/*     */ 
/* 676 */     for (IText.TextJustification js : IText.TextJustification.values()) {
/* 677 */       this.justCombo.add(js.name());
/*     */     }
/* 679 */     this.justCombo.select(1);
/*     */   }
/*     */ 
/*     */   private void createColorAttr()
/*     */   {
/* 688 */     this.chkBox[ChkBox.COLOR.ordinal()] = new Button(this.multigroup, 32);
/* 689 */     this.chkBox[ChkBox.COLOR.ordinal()].setLayoutData(new GridData(16, 28));
/* 690 */     this.chkBox[ChkBox.COLOR.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 694 */         Button btn = (Button)e.widget;
/* 695 */         if (btn.getSelection()) {
/* 696 */           MidLevelCloudAttrDlg.this.colorLbl.setEnabled(true);
/*     */         }
/*     */         else
/* 699 */           MidLevelCloudAttrDlg.this.colorLbl.setEnabled(false);
/*     */       }
/*     */     });
/* 705 */     this.colorLbl = new Label(this.multigroup, 16384);
/* 706 */     this.colorLbl.setText("Color:");
/* 707 */     this.cs = new ColorButtonSelector(this.multigroup);
/* 708 */     this.cs.setColorValue(new RGB(0, 255, 0));
/*     */ 
/* 710 */     Label sep6 = new Label(this.top, 258);
/* 711 */     sep6.setLayoutData(new GridData(768));
/*     */   }
/*     */ 
/*     */   private void enableChkBoxes(boolean flag)
/*     */   {
/* 720 */     if (!flag) {
/* 721 */       setAllChkBoxes();
/*     */     }
/* 723 */     for (ChkBox chk : ChkBox.values())
/* 724 */       this.chkBox[chk.ordinal()].setVisible(flag);
/*     */   }
/*     */ 
/*     */   private void setAllChkBoxes()
/*     */   {
/* 732 */     for (ChkBox chk : ChkBox.values())
/* 733 */       this.chkBox[chk.ordinal()].setSelection(true);
/*     */   }
/*     */ 
/*     */   private void enableAllWidgets(boolean flag)
/*     */   {
/* 743 */     this.colorLbl.setEnabled(flag);
/*     */ 
/* 745 */     this.sizeLbl.setEnabled(flag);
/* 746 */     this.sizeCombo.setEnabled(flag);
/*     */ 
/* 748 */     this.fontLbl.setEnabled(flag);
/* 749 */     this.fontCombo.setEnabled(flag);
/*     */ 
/* 751 */     this.styleLbl.setEnabled(flag);
/* 752 */     this.styleCombo.setEnabled(flag);
/*     */ 
/* 754 */     this.justLbl.setEnabled(flag);
/* 755 */     this.justCombo.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   public String getCloudAmounts()
/*     */   {
/* 761 */     return null;
/*     */   }
/*     */ 
/*     */   public String getCloudTypes()
/*     */   {
/* 768 */     if (this.multiselectMode) return null;
/* 769 */     return assembleString(this.cloudTypeButtons);
/*     */   }
/*     */ 
/*     */   public String getIcingLevels()
/*     */   {
/* 774 */     if (this.multiselectMode) return null;
/* 775 */     return this.icingLevel.getText();
/*     */   }
/*     */ 
/*     */   public void setIcingLevels(String levels) {
/* 779 */     this.icingLevel.setText(levels);
/*     */   }
/*     */ 
/*     */   public String getIcingPattern()
/*     */   {
/* 784 */     if (this.multiselectMode) return null;
/* 785 */     return this.iceCombo.getSelectedText();
/*     */   }
/*     */ 
/*     */   public void setIcingPattern(String pattern) {
/* 789 */     this.iceCombo.setSelectedText(pattern);
/*     */   }
/*     */ 
/*     */   public String getTstormLevels()
/*     */   {
/* 794 */     if (this.multiselectMode) return null;
/* 795 */     return this.tstormLevel.getText();
/*     */   }
/*     */ 
/*     */   public void setTstormLevels(String levels) {
/* 799 */     this.tstormLevel.setText(levels);
/*     */   }
/*     */ 
/*     */   public String getTstormTypes()
/*     */   {
/* 804 */     if (this.multiselectMode) return null;
/* 805 */     return assembleString(this.tstormTypeButtons);
/*     */   }
/*     */ 
/*     */   public String getTurbulenceLevels()
/*     */   {
/* 810 */     if (this.multiselectMode) return null;
/* 811 */     return this.turbLevel.getText();
/*     */   }
/*     */ 
/*     */   public void setTurbulenceLevels(String levels) {
/* 815 */     this.turbLevel.setText(levels);
/*     */   }
/*     */ 
/*     */   public String getTurbulencePattern()
/*     */   {
/* 820 */     if (this.multiselectMode) return null;
/* 821 */     return this.turbCombo.getSelectedText();
/*     */   }
/*     */ 
/*     */   public void setTurbulencePattern(String pattern) {
/* 825 */     this.turbCombo.setSelectedText(pattern);
/*     */   }
/*     */ 
/*     */   public boolean hasIcing()
/*     */   {
/* 830 */     if ((this.icingLevel.getText() == null) || (this.icingLevel.getText().isEmpty())) return false;
/* 831 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean hasTstorm()
/*     */   {
/* 836 */     if ((this.tstormLevel.getText() == null) || (this.tstormLevel.getText().isEmpty())) return false;
/* 837 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean hasTurbulence()
/*     */   {
/* 842 */     if ((this.turbLevel.getText() == null) || (this.turbLevel.getText().isEmpty())) return false;
/* 843 */     return true;
/*     */   }
/*     */ 
/*     */   public Coordinate getPosition()
/*     */   {
/* 849 */     return null;
/*     */   }
/*     */ 
/*     */   public Color getTextColor()
/*     */   {
/* 855 */     return null;
/*     */   }
/*     */ 
/*     */   private String assembleString(List<Button> list)
/*     */   {
/* 863 */     StringBuilder sb = new StringBuilder();
/* 864 */     for (Button btn : list) {
/* 865 */       if (btn.getSelection()) {
/* 866 */         sb.append(btn.getText());
/* 867 */         sb.append('|');
/*     */       }
/*     */     }
/* 870 */     if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
/* 871 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public boolean isTwoColumns()
/*     */   {
/* 877 */     return true;
/*     */   }
/*     */ 
/*     */   public Boolean isClear()
/*     */   {
/* 882 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public Coordinate getLocation()
/*     */   {
/* 887 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getString()
/*     */   {
/* 895 */     return new String[] { new String("") };
/*     */   }
/*     */ 
/*     */   public double getRotation()
/*     */   {
/* 900 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public IText.TextRotation getRotationRelativity()
/*     */   {
/* 905 */     return IText.TextRotation.SCREEN_RELATIVE;
/*     */   }
/*     */ 
/*     */   public IText.DisplayType getDisplayType()
/*     */   {
/* 910 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean maskText()
/*     */   {
/* 915 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public Boolean getHide()
/*     */   {
/* 920 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public Boolean getAuto()
/*     */   {
/* 925 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public int getXOffset()
/*     */   {
/* 930 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getYOffset()
/*     */   {
/* 935 */     return 0;
/*     */   }
/*     */ 
/*     */   private static enum ChkBox
/*     */   {
/*  81 */     SIZE, FONT, STYLE, JUST, COLOR;
/*     */   }
/*     */ 
/*     */   private static enum CloudTypes
/*     */   {
/*  60 */     CU, ST, SC, NS, AS, AC, CS, CC, CI;
/*     */   }
/*     */ 
/*     */   public static enum FontSizeName {
/*  64 */     TINY, SMALL, MEDIUM, LARGE, HUGE, GIANT;
/*     */   }
/*     */ 
/*     */   private static enum TstormTypes
/*     */   {
/*  62 */     ISOL, OCNL, FRQ, EMBD;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.MidLevelCloudAttrDlg
 * JD-Core Version:    0.6.2
 */