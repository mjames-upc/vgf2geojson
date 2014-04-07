/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAvnText;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAvnText.AviationTextType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*      */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*      */ import java.awt.Color;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import org.eclipse.swt.events.ControlEvent;
/*      */ import org.eclipse.swt.events.ControlListener;
/*      */ import org.eclipse.swt.events.SelectionAdapter;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.graphics.RGB;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ 
/*      */ public class AvnTextAttrDlg extends AttrDlg
/*      */   implements IAvnText
/*      */ {
/*   63 */   public static int[] FontSizeValue = { 10, 12, 14, 18, 24, 34 };
/*      */ 
/*   65 */   public static final String[] FontName = { "Courier", "Nimbus Sans L", "Liberation Serif" };
/*      */ 
/*   67 */   public static final String[] BoxName = { "Normal", "Boxed", "Blanked", "Outline" };
/*      */ 
/*   69 */   private static final String[] ICING_LIST = { "ICING_00", "ICING_01", "ICING_02", 
/*   70 */     "ICING_03", "ICING_04", "ICING_05", 
/*   71 */     "ICING_06", "ICING_07", "ICING_08", 
/*   72 */     "ICING_09", "ICING_10" };
/*      */ 
/*   74 */   private static final String[] TURB_LIST = { "TURBULENCE_0", "TURBULENCE_1", "TURBULENCE_2", 
/*   75 */     "TURBULENCE_3", "TURBULENCE_4", 
/*   76 */     "TURBULENCE_4|TURBULENCE_6", "TURBULENCE_5", 
/*   77 */     "TURBULENCE_6", "TURBULENCE_6|TURBULENCE_7", 
/*   78 */     "TURBULENCE_7", "TURBULENCE_8" };
/*      */ 
/*   82 */   static AvnTextAttrDlg INSTANCE = null;
/*      */ 
/*   84 */   private Composite top = null;
/*      */   private Label colorLbl;
/*   87 */   private ColorButtonSelector cs = null;
/*      */   private Label topLabel;
/*   90 */   private Text topValue = null;
/*      */ 
/*   92 */   private Text bottomValue = null;
/*   93 */   private Label bottomLabel = null;
/*      */   private Label typeLabel;
/*   96 */   private Combo typeCombo = null;
/*      */   private Label sizeLbl;
/*   99 */   private Combo sizeCombo = null;
/*      */   private Label fontLbl;
/*  102 */   private Combo fontCombo = null;
/*      */   private Label styleLbl;
/*  105 */   private Combo styleCombo = null;
/*      */   private Label justLbl;
/*  108 */   private Combo justCombo = null;
/*      */ 
/*  110 */   private SymbolCombo symbolCombo = null;
/*  111 */   private Label symbolLabel = null;
/*      */   private Set<Control> enableList;
/*      */   private Button[] chkBox;
/*      */ 
/*      */   private AvnTextAttrDlg(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  124 */     super(parShell);
/*      */   }
/*      */ 
/*      */   public static AvnTextAttrDlg getInstance(Shell parShell)
/*      */   {
/*  137 */     if (INSTANCE == null)
/*      */     {
/*      */       try
/*      */       {
/*  141 */         INSTANCE = new AvnTextAttrDlg(parShell);
/*      */       }
/*      */       catch (VizException e)
/*      */       {
/*  145 */         e.printStackTrace();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  150 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  160 */     this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/*  163 */     GridLayout mainLayout = new GridLayout(3, false);
/*  164 */     mainLayout.marginHeight = 3;
/*  165 */     mainLayout.marginWidth = 3;
/*  166 */     this.top.setLayout(mainLayout);
/*      */ 
/*  169 */     initializeComponents();
/*      */ 
/*  171 */     return this.top;
/*      */   }
/*      */ 
/*      */   private void initializeComponents()
/*      */   {
/*  181 */     this.enableList = new HashSet();
/*      */ 
/*  183 */     getShell().setText("Aviation Text Attributes");
/*      */ 
/*  185 */     this.chkBox = new Button[9];
/*      */ 
/*  190 */     createTypeAttr();
/*      */ 
/*  195 */     createTopAttr();
/*      */ 
/*  200 */     createBottomAttr();
/*      */ 
/*  205 */     createSizeAttr();
/*      */ 
/*  211 */     createFontAttr();
/*      */ 
/*  216 */     createStyleAttr();
/*      */ 
/*  221 */     createJustAttr();
/*      */ 
/*  226 */     createColorAttr();
/*      */ 
/*  231 */     createSymbolAttr();
/*      */ 
/*  234 */     if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/*  234 */       .equalsIgnoreCase("MultiSelect")) {
/*  235 */       enableChkBoxes(true);
/*  236 */       enableAllWidgets(false);
/*      */     }
/*      */     else {
/*  239 */       enableChkBoxes(false);
/*      */ 
/*  242 */       updateDialog(IAvnText.AviationTextType.values()[0]);
/*      */     }
/*      */ 
/*  245 */     addSeparator(this.top.getParent());
/*      */   }
/*      */ 
/*      */   private void updateDialog(IAvnText.AviationTextType atype)
/*      */   {
/*  256 */     this.typeCombo.setText(atype.toString());
/*      */ 
/*  261 */     for (Control widget : this.enableList) {
/*  262 */       widget.setEnabled(true);
/*      */     }
/*  264 */     this.symbolCombo.removeAll();
/*      */ 
/*  269 */     Set disableList = getDisableList(atype);
/*  270 */     for (Control widget : disableList) {
/*  271 */       widget.setEnabled(false);
/*      */     }
/*      */ 
/*  278 */     if (!this.chkBox[ChkBox.BOTTOM.ordinal()].getSelection()) {
/*  279 */       this.bottomLabel.setEnabled(false);
/*  280 */       this.bottomValue.setEnabled(false);
/*      */     }
/*      */ 
/*  283 */     if (!this.chkBox[ChkBox.SYMBOL.ordinal()].getSelection()) {
/*  284 */       this.symbolLabel.setEnabled(false);
/*  285 */       this.symbolCombo.setEnabled(false);
/*      */     }
/*      */ 
/*  291 */     if (atype == IAvnText.AviationTextType.MID_LEVEL_ICING) {
/*  292 */       setIcingSymbols();
/*      */     }
/*  294 */     else if ((atype == IAvnText.AviationTextType.LOW_LEVEL_TURBULENCE) || 
/*  295 */       (atype == IAvnText.AviationTextType.HIGH_LEVEL_TURBULENCE)) {
/*  296 */       setTurbulenceSymbols();
/*      */     }
/*      */ 
/*  299 */     this.top.pack();
/*      */   }
/*      */ 
/*      */   protected void setTurbulenceSymbols()
/*      */   {
/*  307 */     this.symbolCombo.setItems(TURB_LIST);
/*  308 */     this.symbolCombo.select(4);
/*      */   }
/*      */ 
/*      */   protected void setIcingSymbols()
/*      */   {
/*  315 */     this.symbolCombo.setItems(ICING_LIST);
/*  316 */     this.symbolCombo.select(5);
/*      */   }
/*      */ 
/*      */   protected Set<Control> getDisableList(IAvnText.AviationTextType type)
/*      */   {
/*  325 */     Set disableList = new HashSet();
/*      */ 
/*  327 */     switch (type) {
/*      */     case CLOUD_LEVEL:
/*      */     case FLIGHT_LEVEL:
/*      */     case FREEZING_LEVEL:
/*      */     case LOW_PRESSURE_BOX:
/*  332 */       disableList.add(this.bottomLabel);
/*  333 */       disableList.add(this.bottomValue);
/*  334 */       disableList.add(this.symbolLabel);
/*  335 */       disableList.add(this.symbolCombo);
/*  336 */       return disableList;
/*      */     case LOW_LEVEL_TURBULENCE:
/*  339 */       disableList.add(this.symbolLabel);
/*  340 */       disableList.add(this.symbolCombo);
/*  341 */       return disableList;
/*      */     case HIGH_LEVEL_TURBULENCE:
/*      */     case HIGH_PRESSURE_BOX:
/*  344 */     }return disableList;
/*      */   }
/*      */ 
/*      */   public float getFontSize()
/*      */   {
/*  351 */     if (this.chkBox[ChkBox.SIZE.ordinal()].getSelection()) {
/*  352 */       return FontSizeValue[this.sizeCombo.getSelectionIndex()];
/*      */     }
/*      */ 
/*  355 */     return (0.0F / 0.0F);
/*      */   }
/*      */ 
/*      */   public String getFontName()
/*      */   {
/*  363 */     if (this.chkBox[ChkBox.FONT.ordinal()].getSelection()) {
/*  364 */       return this.fontCombo.getText();
/*      */     }
/*      */ 
/*  367 */     return null;
/*      */   }
/*      */ 
/*      */   public IText.FontStyle getStyle()
/*      */   {
/*  375 */     if (this.chkBox[ChkBox.STYLE.ordinal()].getSelection()) {
/*  376 */       return IText.FontStyle.values()[this.styleCombo.getSelectionIndex()];
/*      */     }
/*      */ 
/*  379 */     return null;
/*      */   }
/*      */ 
/*      */   public IText.TextJustification getJustification()
/*      */   {
/*  387 */     if (this.chkBox[ChkBox.JUST.ordinal()].getSelection()) {
/*  388 */       return IText.TextJustification.values()[this.justCombo.getSelectionIndex()];
/*      */     }
/*      */ 
/*  391 */     return null;
/*      */   }
/*      */ 
/*      */   public Color[] getColors()
/*      */   {
/*  399 */     if (this.chkBox[ChkBox.COLOR.ordinal()].getSelection())
/*      */     {
/*  402 */       Color[] colors = new Color[1];
/*      */ 
/*  404 */       colors[0] = new Color(this.cs.getColorValue().red, 
/*  405 */         this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*      */ 
/*  407 */       return colors;
/*      */     }
/*      */ 
/*  410 */     return null;
/*      */   }
/*      */ 
/*      */   public void setTextType(IAvnText.AviationTextType type)
/*      */   {
/*  420 */     updateDialog(type);
/*      */   }
/*      */ 
/*      */   public void setFontSize(float size)
/*      */   {
/*  428 */     int index = 0;
/*  429 */     for (int ii = 0; ii < FontSizeValue.length; ii++) {
/*  430 */       if ((int)size == FontSizeValue[ii]) {
/*  431 */         index = ii;
/*  432 */         break;
/*      */       }
/*      */     }
/*      */ 
/*  436 */     this.sizeCombo.select(index);
/*      */   }
/*      */ 
/*      */   public void setTopValue(String value)
/*      */   {
/*  445 */     this.topValue.setText(value);
/*      */   }
/*      */ 
/*      */   public void setBottomValue(String value)
/*      */   {
/*  453 */     this.bottomValue.setText(value);
/*      */   }
/*      */ 
/*      */   public void setFontName(String name)
/*      */   {
/*  460 */     for (String st : FontName)
/*  461 */       if (st.equalsIgnoreCase(name)) {
/*  462 */         this.fontCombo.setText(st);
/*  463 */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void setStyle(IText.FontStyle style)
/*      */   {
/*  472 */     for (IText.FontStyle fs : IText.FontStyle.values())
/*  473 */       if (fs == style) {
/*  474 */         this.styleCombo.setText(fs.name());
/*  475 */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void setJustification(IText.TextJustification just)
/*      */   {
/*  484 */     for (IText.TextJustification js : IText.TextJustification.values())
/*  485 */       if (js == just) {
/*  486 */         this.justCombo.setText(js.name());
/*  487 */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   private void setColor(Color clr)
/*      */   {
/*  498 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute iattr)
/*      */   {
/*  507 */     if ((iattr instanceof IAvnText)) {
/*  508 */       IAvnText attr = (IAvnText)iattr;
/*  509 */       setTextType(attr.getAvnTextType());
/*  510 */       setTopValue(attr.getTopValue());
/*  511 */       setBottomValue(attr.getBottomValue());
/*  512 */       setFontName(attr.getFontName());
/*  513 */       setFontSize(attr.getFontSize());
/*  514 */       setJustification(attr.getJustification());
/*  515 */       setStyle(attr.getStyle());
/*  516 */       setSymbolPatternName(attr.getSymbolPatternName());
/*      */ 
/*  518 */       Color clr = attr.getColors()[0];
/*  519 */       if (clr != null) setColor(clr);
/*      */     }
/*      */ 
/*  522 */     this.top.pack();
/*      */   }
/*      */ 
/*      */   private void setSymbolPatternName(String symbolPatternName)
/*      */   {
/*  531 */     this.symbolCombo.setSelectedText(symbolPatternName);
/*      */   }
/*      */ 
/*      */   public IAvnText.AviationTextType getAvnTextType()
/*      */   {
/*  536 */     if (this.chkBox[ChkBox.TYPE.ordinal()].getSelection()) {
/*  537 */       return IAvnText.AviationTextType.valueOf(this.typeCombo.getText());
/*      */     }
/*      */ 
/*  540 */     return null;
/*      */   }
/*      */ 
/*      */   public String getBottomValue()
/*      */   {
/*  546 */     if (this.chkBox[ChkBox.BOTTOM.ordinal()].getSelection()) {
/*  547 */       return this.bottomValue.getText();
/*      */     }
/*      */ 
/*  550 */     return null;
/*      */   }
/*      */ 
/*      */   public String getSymbolPatternName()
/*      */   {
/*  556 */     if (this.chkBox[ChkBox.SYMBOL.ordinal()].getSelection())
/*      */     {
/*  558 */       return this.symbolCombo.getSelectedText();
/*      */     }
/*      */ 
/*  561 */     return null;
/*      */   }
/*      */ 
/*      */   public String getTopValue()
/*      */   {
/*  567 */     if (this.chkBox[ChkBox.TOP.ordinal()].getSelection()) {
/*  568 */       return this.topValue.getText();
/*      */     }
/*      */ 
/*  571 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean hasBottomValue()
/*      */   {
/*  577 */     IAvnText.AviationTextType atype = getAvnTextType();
/*  578 */     switch (atype) {
/*      */     case CLOUD_LEVEL:
/*      */     case FLIGHT_LEVEL:
/*      */     case FREEZING_LEVEL:
/*      */     case LOW_PRESSURE_BOX:
/*  583 */       return false;
/*      */     case HIGH_LEVEL_TURBULENCE:
/*      */     case HIGH_PRESSURE_BOX:
/*  585 */     case LOW_LEVEL_TURBULENCE: } return true;
/*      */   }
/*      */ 
/*      */   public boolean hasSymbolPattern()
/*      */   {
/*  592 */     IAvnText.AviationTextType atype = getAvnTextType();
/*  593 */     switch (atype) {
/*      */     case HIGH_LEVEL_TURBULENCE:
/*      */     case HIGH_PRESSURE_BOX:
/*      */     case MID_LEVEL_ICING:
/*  597 */       return true;
/*      */     case LOW_LEVEL_TURBULENCE:
/*  599 */     case LOW_PRESSURE_BOX: } return false;
/*      */   }
/*      */ 
/*      */   private void createTypeAttr()
/*      */   {
/*  608 */     this.chkBox[ChkBox.TYPE.ordinal()] = new Button(this.top, 32);
/*  609 */     this.chkBox[ChkBox.TYPE.ordinal()].setLayoutData(new GridData(16, 33));
/*  610 */     this.chkBox[ChkBox.TYPE.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  614 */         Button btn = (Button)e.widget;
/*  615 */         if (btn.getSelection()) {
/*  616 */           AvnTextAttrDlg.this.typeLabel.setEnabled(true);
/*  617 */           AvnTextAttrDlg.this.typeCombo.setEnabled(true);
/*      */         }
/*      */         else {
/*  620 */           AvnTextAttrDlg.this.typeLabel.setEnabled(false);
/*  621 */           AvnTextAttrDlg.this.typeCombo.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  628 */     this.typeLabel = new Label(this.top, 0);
/*  629 */     this.typeLabel.setText("Text Type:");
/*      */ 
/*  631 */     this.typeCombo = new Combo(this.top, 12);
/*  632 */     for (IAvnText.AviationTextType type : IAvnText.AviationTextType.values()) {
/*  633 */       this.typeCombo.add(type.toString());
/*      */     }
/*      */ 
/*  639 */     this.typeCombo.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  642 */         Combo ctype = (Combo)e.getSource();
/*      */ 
/*  644 */         AvnTextAttrDlg.this.updateDialog(IAvnText.AviationTextType.valueOf(ctype.getText()));
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void createTopAttr()
/*      */   {
/*  656 */     this.chkBox[ChkBox.TOP.ordinal()] = new Button(this.top, 32);
/*  657 */     this.chkBox[ChkBox.TOP.ordinal()].setLayoutData(new GridData(16, 33));
/*  658 */     this.chkBox[ChkBox.TOP.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  662 */         Button btn = (Button)e.widget;
/*  663 */         if (btn.getSelection()) {
/*  664 */           AvnTextAttrDlg.this.topLabel.setEnabled(true);
/*  665 */           AvnTextAttrDlg.this.topValue.setEnabled(true);
/*      */         }
/*      */         else {
/*  668 */           AvnTextAttrDlg.this.topLabel.setEnabled(false);
/*  669 */           AvnTextAttrDlg.this.topValue.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  675 */     this.topLabel = new Label(this.top, 0);
/*  676 */     this.topLabel.setText("Top Value:");
/*      */ 
/*  678 */     this.topValue = new Text(this.top, 2052);
/*  679 */     this.topValue.setEditable(true);
/*  680 */     this.topValue.setText("XXX");
/*      */   }
/*      */ 
/*      */   private void createBottomAttr()
/*      */   {
/*  689 */     this.chkBox[ChkBox.BOTTOM.ordinal()] = new Button(this.top, 32);
/*  690 */     this.chkBox[ChkBox.BOTTOM.ordinal()].setLayoutData(new GridData(16, 33));
/*  691 */     this.chkBox[ChkBox.BOTTOM.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  695 */         Button btn = (Button)e.widget;
/*  696 */         if (btn.getSelection()) {
/*  697 */           Set disableList = AvnTextAttrDlg.this.getDisableList(AvnTextAttrDlg.this.getAvnTextType());
/*  698 */           if (!disableList.contains(AvnTextAttrDlg.this.bottomLabel)) {
/*  699 */             AvnTextAttrDlg.this.bottomLabel.setEnabled(true);
/*      */           }
/*  701 */           if (!disableList.contains(AvnTextAttrDlg.this.bottomValue))
/*  702 */             AvnTextAttrDlg.this.bottomValue.setEnabled(true);
/*      */         }
/*      */         else
/*      */         {
/*  706 */           AvnTextAttrDlg.this.bottomLabel.setEnabled(false);
/*  707 */           AvnTextAttrDlg.this.bottomValue.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  713 */     this.bottomLabel = new Label(this.top, 0);
/*  714 */     this.bottomLabel.setText("Bottom Value:");
/*  715 */     this.enableList.add(this.bottomLabel);
/*      */ 
/*  717 */     this.bottomValue = new Text(this.top, 2052);
/*  718 */     this.bottomValue.setEditable(true);
/*  719 */     this.bottomValue.setText("XXX");
/*  720 */     this.enableList.add(this.bottomValue);
/*      */   }
/*      */ 
/*      */   private void createSizeAttr()
/*      */   {
/*  728 */     this.chkBox[ChkBox.SIZE.ordinal()] = new Button(this.top, 32);
/*  729 */     this.chkBox[ChkBox.SIZE.ordinal()].setLayoutData(new GridData(16, 33));
/*  730 */     this.chkBox[ChkBox.SIZE.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  734 */         Button btn = (Button)e.widget;
/*  735 */         if (btn.getSelection()) {
/*  736 */           AvnTextAttrDlg.this.sizeLbl.setEnabled(true);
/*  737 */           AvnTextAttrDlg.this.sizeCombo.setEnabled(true);
/*      */         }
/*      */         else {
/*  740 */           AvnTextAttrDlg.this.sizeLbl.setEnabled(false);
/*  741 */           AvnTextAttrDlg.this.sizeCombo.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  747 */     this.sizeLbl = new Label(this.top, 16384);
/*  748 */     this.sizeLbl.setText("Size:");
/*      */ 
/*  750 */     this.sizeCombo = new Combo(this.top, 12);
/*      */ 
/*  752 */     for (FontSizeName fs : FontSizeName.values()) {
/*  753 */       this.sizeCombo.add(fs.name());
/*      */     }
/*  755 */     this.sizeCombo.select(2);
/*      */   }
/*      */ 
/*      */   private void createFontAttr()
/*      */   {
/*  764 */     this.chkBox[ChkBox.FONT.ordinal()] = new Button(this.top, 32);
/*  765 */     this.chkBox[ChkBox.FONT.ordinal()].setLayoutData(new GridData(16, 33));
/*  766 */     this.chkBox[ChkBox.FONT.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  770 */         Button btn = (Button)e.widget;
/*  771 */         if (btn.getSelection()) {
/*  772 */           AvnTextAttrDlg.this.fontLbl.setEnabled(true);
/*  773 */           AvnTextAttrDlg.this.fontCombo.setEnabled(true);
/*      */         }
/*      */         else {
/*  776 */           AvnTextAttrDlg.this.fontLbl.setEnabled(false);
/*  777 */           AvnTextAttrDlg.this.fontCombo.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  784 */     this.fontLbl = new Label(this.top, 16384);
/*  785 */     this.fontLbl.setText("Font:");
/*      */ 
/*  787 */     this.fontCombo = new Combo(this.top, 12);
/*      */ 
/*  789 */     for (String st : FontName) {
/*  790 */       this.fontCombo.add(st);
/*      */     }
/*  792 */     this.fontCombo.select(0);
/*      */   }
/*      */ 
/*      */   private void createStyleAttr()
/*      */   {
/*  800 */     this.chkBox[ChkBox.STYLE.ordinal()] = new Button(this.top, 32);
/*  801 */     this.chkBox[ChkBox.STYLE.ordinal()].setLayoutData(new GridData(16, 33));
/*  802 */     this.chkBox[ChkBox.STYLE.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  806 */         Button btn = (Button)e.widget;
/*  807 */         if (btn.getSelection()) {
/*  808 */           AvnTextAttrDlg.this.styleLbl.setEnabled(true);
/*  809 */           AvnTextAttrDlg.this.styleCombo.setEnabled(true);
/*      */         }
/*      */         else {
/*  812 */           AvnTextAttrDlg.this.styleLbl.setEnabled(false);
/*  813 */           AvnTextAttrDlg.this.styleCombo.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  820 */     this.styleLbl = new Label(this.top, 16384);
/*  821 */     this.styleLbl.setText("Style:");
/*      */ 
/*  823 */     this.styleCombo = new Combo(this.top, 12);
/*      */ 
/*  825 */     for (IText.FontStyle fs : IText.FontStyle.values()) {
/*  826 */       this.styleCombo.add(fs.name());
/*      */     }
/*  828 */     this.styleCombo.select(0);
/*      */   }
/*      */ 
/*      */   private void createJustAttr()
/*      */   {
/*  835 */     this.chkBox[ChkBox.JUST.ordinal()] = new Button(this.top, 32);
/*  836 */     this.chkBox[ChkBox.JUST.ordinal()].setLayoutData(new GridData(16, 33));
/*  837 */     this.chkBox[ChkBox.JUST.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  841 */         Button btn = (Button)e.widget;
/*  842 */         if (btn.getSelection()) {
/*  843 */           AvnTextAttrDlg.this.justLbl.setEnabled(true);
/*  844 */           AvnTextAttrDlg.this.justCombo.setEnabled(true);
/*      */         }
/*      */         else {
/*  847 */           AvnTextAttrDlg.this.justLbl.setEnabled(false);
/*  848 */           AvnTextAttrDlg.this.justCombo.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  853 */     this.justLbl = new Label(this.top, 16384);
/*  854 */     this.justLbl.setText("Just:");
/*      */ 
/*  856 */     this.justCombo = new Combo(this.top, 12);
/*      */ 
/*  858 */     for (IText.TextJustification js : IText.TextJustification.values()) {
/*  859 */       this.justCombo.add(js.name());
/*      */     }
/*  861 */     this.justCombo.select(1);
/*      */   }
/*      */ 
/*      */   private void createColorAttr()
/*      */   {
/*  870 */     this.chkBox[ChkBox.COLOR.ordinal()] = new Button(this.top, 32);
/*  871 */     this.chkBox[ChkBox.COLOR.ordinal()].setLayoutData(new GridData(16, 28));
/*  872 */     this.chkBox[ChkBox.COLOR.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  876 */         Button btn = (Button)e.widget;
/*  877 */         if (btn.getSelection()) {
/*  878 */           AvnTextAttrDlg.this.colorLbl.setEnabled(true);
/*      */         }
/*      */         else
/*  881 */           AvnTextAttrDlg.this.colorLbl.setEnabled(false);
/*      */       }
/*      */     });
/*  887 */     this.colorLbl = new Label(this.top, 16384);
/*  888 */     this.colorLbl.setText("Color:");
/*  889 */     this.cs = new ColorButtonSelector(this.top);
/*  890 */     this.cs.setColorValue(new RGB(0, 255, 0));
/*      */   }
/*      */ 
/*      */   private void createSymbolAttr()
/*      */   {
/*  897 */     this.chkBox[ChkBox.SYMBOL.ordinal()] = new Button(this.top, 32);
/*  898 */     this.chkBox[ChkBox.SYMBOL.ordinal()].setLayoutData(new GridData(16, 28));
/*  899 */     this.chkBox[ChkBox.SYMBOL.ordinal()].addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  903 */         Button btn = (Button)e.widget;
/*  904 */         if ((btn.getSelection()) && (AvnTextAttrDlg.this.getAvnTextType() != null))
/*      */         {
/*  906 */           Set disableList = AvnTextAttrDlg.this.getDisableList(AvnTextAttrDlg.this.getAvnTextType());
/*  907 */           if (!disableList.contains(AvnTextAttrDlg.this.symbolLabel)) {
/*  908 */             AvnTextAttrDlg.this.symbolLabel.setEnabled(true);
/*      */           }
/*  910 */           if (!disableList.contains(AvnTextAttrDlg.this.symbolCombo)) {
/*  911 */             AvnTextAttrDlg.this.symbolCombo.setEnabled(true);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  917 */           AvnTextAttrDlg.this.symbolLabel.setEnabled(false);
/*  918 */           AvnTextAttrDlg.this.symbolCombo.setEnabled(false);
/*      */         }
/*      */       }
/*      */     });
/*  925 */     this.symbolLabel = new Label(this.top, 16384);
/*  926 */     this.symbolLabel.setText("Symbol:");
/*  927 */     this.enableList.add(this.symbolLabel);
/*      */ 
/*  929 */     this.symbolCombo = new SymbolCombo(this.top);
/*  930 */     this.symbolCombo.setLayoutData(new GridData(10, 1));
/*  931 */     this.enableList.add(this.symbolCombo);
/*      */ 
/*  933 */     this.symbolCombo.addControlListener(new ControlListener()
/*      */     {
/*      */       public void controlMoved(ControlEvent e) {
/*  936 */         AvnTextAttrDlg.this.top.pack();
/*      */       }
/*      */ 
/*      */       public void controlResized(ControlEvent e) {
/*  940 */         AvnTextAttrDlg.this.top.pack();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void enableChkBoxes(boolean flag)
/*      */   {
/*  952 */     if (!flag) {
/*  953 */       setAllChkBoxes();
/*      */     }
/*  955 */     for (ChkBox chk : ChkBox.values())
/*  956 */       this.chkBox[chk.ordinal()].setVisible(flag);
/*      */   }
/*      */ 
/*      */   private void setAllChkBoxes()
/*      */   {
/*  964 */     for (ChkBox chk : ChkBox.values())
/*  965 */       this.chkBox[chk.ordinal()].setSelection(true);
/*      */   }
/*      */ 
/*      */   private void enableAllWidgets(boolean flag)
/*      */   {
/*  975 */     this.colorLbl.setEnabled(flag);
/*      */ 
/*  977 */     this.typeLabel.setEnabled(flag);
/*  978 */     this.typeCombo.setEnabled(flag);
/*      */ 
/*  980 */     this.topLabel.setEnabled(flag);
/*  981 */     this.topValue.setEnabled(flag);
/*      */ 
/*  983 */     this.bottomLabel.setEnabled(flag);
/*  984 */     this.bottomValue.setEnabled(flag);
/*      */ 
/*  986 */     this.sizeLbl.setEnabled(flag);
/*  987 */     this.sizeCombo.setEnabled(flag);
/*      */ 
/*  989 */     this.fontLbl.setEnabled(flag);
/*  990 */     this.fontCombo.setEnabled(flag);
/*      */ 
/*  992 */     this.styleLbl.setEnabled(flag);
/*  993 */     this.styleCombo.setEnabled(flag);
/*      */ 
/*  995 */     this.justLbl.setEnabled(flag);
/*  996 */     this.justCombo.setEnabled(flag);
/*      */ 
/*  998 */     this.symbolLabel.setEnabled(flag);
/*  999 */     this.symbolCombo.setEnabled(flag);
/*      */   }
/*      */ 
/*      */   public Coordinate getPosition()
/*      */   {
/* 1005 */     return null;
/*      */   }
/*      */ 
/*      */   public Color getTextColor()
/*      */   {
/* 1011 */     return null;
/*      */   }
/*      */ 
/*      */   public Boolean isClear()
/*      */   {
/* 1016 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public Coordinate getLocation()
/*      */   {
/* 1021 */     return null;
/*      */   }
/*      */ 
/*      */   public String[] getString()
/*      */   {
/* 1029 */     return new String[] { new String("") };
/*      */   }
/*      */ 
/*      */   public double getRotation()
/*      */   {
/* 1034 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   public IText.TextRotation getRotationRelativity()
/*      */   {
/* 1039 */     return IText.TextRotation.SCREEN_RELATIVE;
/*      */   }
/*      */ 
/*      */   public IText.DisplayType getDisplayType()
/*      */   {
/* 1044 */     return null;
/*      */   }
/*      */ 
/*      */   public Boolean maskText()
/*      */   {
/* 1049 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public Boolean getHide()
/*      */   {
/* 1054 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public Boolean getAuto()
/*      */   {
/* 1059 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public int getXOffset()
/*      */   {
/* 1064 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getYOffset()
/*      */   {
/* 1069 */     return 0;
/*      */   }
/*      */ 
/*      */   private static enum ChkBox
/*      */   {
/*   80 */     TYPE, TOP, BOTTOM, SIZE, FONT, STYLE, JUST, COLOR, SYMBOL;
/*      */   }
/*      */ 
/*      */   public static enum FontSizeName
/*      */   {
/*   62 */     TINY, SMALL, MEDIUM, LARGE, HUGE, GIANT;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.AvnTextAttrDlg
 * JD-Core Version:    0.6.2
 */