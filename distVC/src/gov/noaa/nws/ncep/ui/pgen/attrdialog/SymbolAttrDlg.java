/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.SpinnerSlider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenCommandManager;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ICombo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ISymbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import org.eclipse.swt.events.KeyAdapter;
/*     */ import org.eclipse.swt.events.KeyEvent;
/*     */ import org.eclipse.swt.events.ModifyEvent;
/*     */ import org.eclipse.swt.events.ModifyListener;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.RowData;
/*     */ import org.eclipse.swt.layout.RowLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Slider;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class SymbolAttrDlg extends AttrDlg
/*     */   implements ISymbol
/*     */ {
/*  81 */   private static SymbolAttrDlg INSTANCE = null;
/*     */   protected static final String PLACE_SYMBOL = "Place Symbol";
/*     */   protected static final String UNDO_SYMBOL = "Undo Symbol";
/*     */   protected static final String REDO_SYMBOL = "Redo Symbol";
/*  89 */   protected Composite top = null;
/*     */   protected Label colorLbl;
/*  92 */   private ColorButtonSelector cs = null;
/*     */   protected Label clearLbl;
/*  95 */   protected Button clearBtn1 = null;
/*  96 */   protected Button clearBtn2 = null;
/*     */   protected Label widthLbl;
/*  99 */   protected Slider widthSlider = null;
/* 100 */   protected Text widthText = null;
/* 101 */   protected SpinnerSlider widthSpinnerSlider = null;
/*     */   protected Label sizeLbl;
/* 104 */   protected Slider sizeSlider = null;
/* 105 */   protected Text sizeText = null;
/* 106 */   protected SpinnerSlider sizeSpinnerSlider = null;
/*     */   protected Label latitudeLabel;
/* 109 */   protected Text latitudeText = null;
/* 110 */   protected String lastLat = "";
/*     */   protected Label longitudeLabel;
/* 113 */   protected Text longitudeText = null;
/* 114 */   protected String lastLong = "";
/*     */ 
/* 116 */   protected Button placeBtn = null;
/* 117 */   protected Button undoBtn = null;
/* 118 */   private boolean keyEvent = false;
/*     */   private Coordinate prevLoc;
/*     */   protected Button[] chkBox;
/*     */ 
/*     */   protected SymbolAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 133 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static SymbolAttrDlg getInstance(Shell parShell)
/*     */   {
/* 146 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/* 150 */         INSTANCE = new SymbolAttrDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/* 154 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 159 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 169 */     this.top = ((Composite)super.createDialogArea(parent));
/* 170 */     this.top.setLayout(getGridLayout(1, false, 0, 0, 0, 0));
/*     */ 
/* 173 */     initializeComponents();
/*     */ 
/* 175 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void initializeComponents()
/*     */   {
/* 184 */     String title = this.pgenCategory;
/* 185 */     if (this.pgenCategory.equalsIgnoreCase("Combo")) {
/* 186 */       title = this.pgenCategory + " Symbol";
/*     */     }
/*     */ 
/* 189 */     getShell().setText(title + " Attributes");
/* 190 */     this.chkBox = new Button[7];
/*     */ 
/* 192 */     createColorClearAttr();
/* 193 */     createWidthAttr();
/* 194 */     createSizeAttr();
/* 195 */     createLatAttr();
/* 196 */     createLonAttr();
/* 197 */     addSeparator(this.top.getParent());
/*     */   }
/*     */ 
/*     */   public void setLatitude(double lat)
/*     */   {
/* 207 */     this.latitudeText.setText(new DecimalFormat("###.00").format(lat));
/* 208 */     this.lastLat = this.latitudeText.getText();
/*     */   }
/*     */ 
/*     */   public void setLongitude(double lon)
/*     */   {
/* 218 */     this.longitudeText.setText(new DecimalFormat("####.00").format(lon));
/* 219 */     this.lastLong = this.longitudeText.getText();
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 227 */     if (this.chkBox[ChkBox.COLOR.ordinal()].getSelection())
/*     */     {
/* 230 */       Color[] colors = new Color[2];
/*     */ 
/* 232 */       colors[0] = new Color(this.cs.getColorValue().red, 
/* 233 */         this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*     */ 
/* 235 */       colors[1] = Color.green;
/*     */ 
/* 237 */       return colors;
/*     */     }
/*     */ 
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 248 */     if (this.chkBox[ChkBox.WIDTH.ordinal()].getSelection()) {
/* 249 */       return this.widthSpinnerSlider.getSelection();
/*     */     }
/*     */ 
/* 252 */     return (0.0F / 0.0F);
/*     */   }
/*     */ 
/*     */   public double getSizeScale()
/*     */   {
/* 262 */     if (this.chkBox[ChkBox.SIZE.ordinal()].getSelection()) {
/* 263 */       return this.sizeSpinnerSlider.getSelection() / Math.pow(10.0D, this.sizeSpinnerSlider.getDigits());
/*     */     }
/*     */ 
/* 266 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public Boolean isClear()
/*     */   {
/* 275 */     if (this.chkBox[ChkBox.CLEAR.ordinal()].getSelection()) {
/* 276 */       return Boolean.valueOf(this.clearBtn1.getSelection());
/*     */     }
/*     */ 
/* 279 */     return null;
/*     */   }
/*     */ 
/*     */   public void setColor(Color clr)
/*     */   {
/* 290 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   private void setLineWidth(float lw)
/*     */   {
/* 299 */     this.widthSpinnerSlider.setSelection((int)lw);
/*     */   }
/*     */ 
/*     */   private void setSize(double size)
/*     */   {
/* 307 */     this.sizeSpinnerSlider.setSelection((int)(size * Math.pow(10.0D, this.sizeSpinnerSlider.getDigits())));
/*     */   }
/*     */ 
/*     */   private void setClear(Boolean clr)
/*     */   {
/* 315 */     if (clr.booleanValue()) {
/* 316 */       this.clearBtn1.setSelection(true);
/* 317 */       this.clearBtn2.setSelection(false);
/*     */     }
/*     */     else {
/* 320 */       this.clearBtn1.setSelection(false);
/* 321 */       this.clearBtn2.setSelection(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute iattr)
/*     */   {
/* 330 */     if ((iattr instanceof ISymbol)) {
/* 331 */       ISymbol ia = (ISymbol)iattr;
/* 332 */       Color clr = ia.getColors()[0];
/* 333 */       if (clr != null) setColor(clr);
/*     */ 
/* 335 */       float lw = ia.getLineWidth();
/* 336 */       if (lw > 0.0F) setLineWidth(lw);
/*     */ 
/* 338 */       setClear(ia.isClear());
/*     */ 
/* 340 */       double size = ia.getSizeScale();
/* 341 */       if (size >= 0.0D) setSize(size);
/*     */     }
/* 343 */     else if ((iattr instanceof ICombo)) {
/* 344 */       ICombo ia = (ICombo)iattr;
/* 345 */       Color clr = ia.getColors()[0];
/* 346 */       if (clr != null) setColor(clr);
/*     */ 
/* 348 */       float lw = ia.getLineWidth();
/* 349 */       if (lw > 0.0F) setLineWidth(lw);
/*     */ 
/* 351 */       setClear(ia.isClear());
/*     */ 
/* 353 */       double size = ia.getSizeScale();
/* 354 */       if (size >= 0.0D) setSize(size);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 513 */     ArrayList adcList = null;
/* 514 */     ArrayList newList = new ArrayList();
/*     */ 
/* 517 */     if (this.drawingLayer != null)
/* 518 */       adcList = (ArrayList)this.drawingLayer.getAllSelected();
/*     */     ArrayList oldList;
/* 521 */     if ((adcList != null) && (!adcList.isEmpty()))
/*     */     {
/* 523 */       DrawableElement newEl = null;
/*     */ 
/* 525 */       for (AbstractDrawableComponent adc : adcList)
/*     */       {
/* 527 */         DrawableElement el = adc.getPrimaryDE();
/*     */ 
/* 529 */         if (el != null)
/*     */         {
/* 531 */           newEl = (DrawableElement)el.copy();
/*     */ 
/* 533 */           newEl.update(this);
/* 534 */           if ((this.latitudeText.isEnabled()) && (this.longitudeText.isEnabled())) {
/* 535 */             ArrayList loc = new ArrayList();
/* 536 */             loc.add(new Coordinate(Double.valueOf(this.longitudeText.getText()).doubleValue(), 
/* 537 */               Double.valueOf(this.latitudeText.getText()).doubleValue()));
/* 538 */             newEl.setPoints(loc);
/*     */           }
/*     */ 
/* 542 */           if (((adc instanceof DECollection)) && (el.getParent() == adc))
/*     */           {
/* 544 */             DECollection dec = (DECollection)adc.copy();
/* 545 */             dec.remove(dec.getPrimaryDE());
/* 546 */             dec.add(0, newEl);
/* 547 */             newList.add(dec);
/*     */           }
/*     */           else {
/* 550 */             newList.add(newEl);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 556 */       if (newEl != null) {
/* 557 */         AttrSettings.getInstance().setSettings(newEl);
/*     */       }
/*     */ 
/* 560 */       oldList = new ArrayList(adcList);
/* 561 */       this.drawingLayer.replaceElements(null, oldList, newList);
/*     */     }
/*     */ 
/* 564 */     this.drawingLayer.removeSelected();
/*     */ 
/* 567 */     for (AbstractDrawableComponent adc : newList) {
/* 568 */       this.drawingLayer.addSelected(adc);
/*     */     }
/*     */ 
/* 571 */     if (this.mapEditor != null) {
/* 572 */       this.mapEditor.refresh();
/*     */     }
/*     */ 
/* 575 */     this.placeBtn.setEnabled(false);
/* 576 */     this.undoBtn.setEnabled(false);
/*     */   }
/*     */ 
/*     */   public boolean labelEnabled()
/*     */   {
/* 585 */     return false;
/*     */   }
/*     */ 
/*     */   public void enableLatLon(boolean flag)
/*     */   {
/* 593 */     this.latitudeText.setEnabled(flag);
/* 594 */     this.longitudeText.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   private void createColorAttr(Composite comp)
/*     */   {
/* 602 */     Composite inCmp = new Composite(comp, 0);
/* 603 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 605 */     this.chkBox[ChkBox.COLOR.ordinal()] = new Button(inCmp, 32);
/* 606 */     this.chkBox[ChkBox.COLOR.ordinal()].setLayoutData(new GridData(16, 28));
/* 607 */     this.chkBox[ChkBox.COLOR.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 611 */         Button btn = (Button)e.widget;
/* 612 */         if (btn.getSelection()) {
/* 613 */           SymbolAttrDlg.this.colorLbl.setEnabled(true);
/*     */         }
/*     */         else
/* 616 */           SymbolAttrDlg.this.colorLbl.setEnabled(false);
/*     */       }
/*     */     });
/* 622 */     this.colorLbl = new Label(inCmp, 16384);
/* 623 */     this.colorLbl.setText("Color ");
/* 624 */     this.cs = new ColorButtonSelector(inCmp, 20, 15);
/* 625 */     this.cs.setColorValue(new RGB(0, 255, 0));
/*     */   }
/*     */ 
/*     */   private void createClearAttr(Composite comp)
/*     */   {
/* 633 */     Composite inCmp = new Composite(comp, 0);
/* 634 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 636 */     this.chkBox[ChkBox.CLEAR.ordinal()] = new Button(inCmp, 32);
/* 637 */     this.chkBox[ChkBox.CLEAR.ordinal()].setLayoutData(new GridData(16, 28));
/* 638 */     this.chkBox[ChkBox.CLEAR.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 642 */         Button btn = (Button)e.widget;
/* 643 */         if (btn.getSelection()) {
/* 644 */           SymbolAttrDlg.this.clearLbl.setEnabled(true);
/* 645 */           SymbolAttrDlg.this.clearBtn1.setEnabled(true);
/* 646 */           SymbolAttrDlg.this.clearBtn2.setEnabled(true);
/*     */         }
/*     */         else {
/* 649 */           SymbolAttrDlg.this.clearLbl.setEnabled(false);
/* 650 */           SymbolAttrDlg.this.clearBtn1.setEnabled(false);
/* 651 */           SymbolAttrDlg.this.clearBtn2.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 657 */     this.clearLbl = new Label(inCmp, 16384);
/* 658 */     this.clearLbl.setText("Clear ");
/*     */ 
/* 660 */     Group clearGroup = new Group(inCmp, 0);
/* 661 */     clearGroup.setLayout(getGridLayout(2, false, 0, 0, 0, 0));
/*     */ 
/* 663 */     this.clearBtn1 = new Button(clearGroup, 16);
/* 664 */     this.clearBtn1.setText("On");
/* 665 */     this.clearBtn1.setSelection(true);
/*     */ 
/* 667 */     this.clearBtn2 = new Button(clearGroup, 16);
/* 668 */     this.clearBtn2.setText("Off");
/*     */   }
/*     */ 
/*     */   private void createWidthAttr()
/*     */   {
/* 675 */     Composite inCmp = new Composite(this.top, 0);
/* 676 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 678 */     this.chkBox[ChkBox.WIDTH.ordinal()] = new Button(inCmp, 32);
/* 679 */     this.chkBox[ChkBox.WIDTH.ordinal()].setLayoutData(new GridData(16, 28));
/* 680 */     this.chkBox[ChkBox.WIDTH.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 684 */         Button btn = (Button)e.widget;
/* 685 */         if (btn.getSelection()) {
/* 686 */           SymbolAttrDlg.this.widthLbl.setEnabled(true);
/* 687 */           SymbolAttrDlg.this.widthSpinnerSlider.setEnabled(true);
/*     */         }
/*     */         else {
/* 690 */           SymbolAttrDlg.this.widthLbl.setEnabled(false);
/* 691 */           SymbolAttrDlg.this.widthSpinnerSlider.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 697 */     this.widthLbl = new Label(inCmp, 16384);
/* 698 */     this.widthLbl.setText("Width ");
/*     */ 
/* 700 */     this.widthSpinnerSlider = 
/* 701 */       new SpinnerSlider(inCmp, 256, 1);
/* 702 */     this.widthSpinnerSlider.setLayoutData(new GridData(164, 25));
/* 703 */     this.widthSpinnerSlider.setMinimum(1);
/* 704 */     this.widthSpinnerSlider.setMaximum(10);
/* 705 */     this.widthSpinnerSlider.setIncrement(1);
/* 706 */     this.widthSpinnerSlider.setPageIncrement(3);
/* 707 */     this.widthSpinnerSlider.setDigits(0);
/*     */   }
/*     */ 
/*     */   private void createSizeAttr()
/*     */   {
/* 714 */     Composite inCmp = new Composite(this.top, 0);
/* 715 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 717 */     this.chkBox[ChkBox.SIZE.ordinal()] = new Button(inCmp, 32);
/* 718 */     this.chkBox[ChkBox.SIZE.ordinal()].setLayoutData(new GridData(16, 28));
/* 719 */     this.chkBox[ChkBox.SIZE.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 723 */         Button btn = (Button)e.widget;
/* 724 */         if (btn.getSelection()) {
/* 725 */           SymbolAttrDlg.this.sizeLbl.setEnabled(true);
/* 726 */           SymbolAttrDlg.this.sizeSpinnerSlider.setEnabled(true);
/*     */         }
/*     */         else {
/* 729 */           SymbolAttrDlg.this.sizeLbl.setEnabled(false);
/* 730 */           SymbolAttrDlg.this.sizeSpinnerSlider.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 736 */     this.sizeLbl = new Label(inCmp, 16384);
/* 737 */     this.sizeLbl.setText("Size ");
/*     */ 
/* 739 */     this.sizeSpinnerSlider = 
/* 740 */       new SpinnerSlider(inCmp, 256, 1);
/* 741 */     this.sizeSpinnerSlider.setLayoutData(new GridData(173, 25));
/* 742 */     this.sizeSpinnerSlider.setMinimum(1);
/* 743 */     this.sizeSpinnerSlider.setMaximum(100);
/* 744 */     this.sizeSpinnerSlider.setIncrement(1);
/* 745 */     this.sizeSpinnerSlider.setPageIncrement(10);
/* 746 */     this.sizeSpinnerSlider.setDigits(1);
/*     */ 
/* 748 */     this.sizeSpinnerSlider.setSelection((int)(1.0D * Math.pow(10.0D, this.sizeSpinnerSlider.getDigits())));
/*     */   }
/*     */ 
/*     */   private void createLatAttr()
/*     */   {
/* 756 */     Composite inCmp = new Composite(this.top, 0);
/* 757 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 759 */     this.chkBox[ChkBox.LAT.ordinal()] = new Button(inCmp, 32);
/* 760 */     this.chkBox[ChkBox.LAT.ordinal()].setLayoutData(new GridData(16, 28));
/* 761 */     this.chkBox[ChkBox.LAT.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 765 */         Button btn = (Button)e.widget;
/* 766 */         if (btn.getSelection()) {
/* 767 */           SymbolAttrDlg.this.latitudeLabel.setEnabled(true);
/* 768 */           SymbolAttrDlg.this.latitudeText.setEnabled(true);
/*     */         }
/*     */         else {
/* 771 */           SymbolAttrDlg.this.latitudeLabel.setEnabled(false);
/* 772 */           SymbolAttrDlg.this.latitudeText.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 778 */     this.chkBox[ChkBox.LAT.ordinal()].setVisible(false);
/*     */ 
/* 780 */     this.latitudeLabel = new Label(inCmp, 0);
/* 781 */     this.latitudeLabel.setText("Lat ");
/*     */ 
/* 783 */     Composite latGroup = new Composite(inCmp, 0);
/* 784 */     latGroup.setLayout(new RowLayout(256));
/*     */ 
/* 786 */     this.latitudeText = new Text(latGroup, 133124);
/*     */ 
/* 788 */     this.latitudeText.setTextLimit(8);
/* 789 */     this.latitudeText.setLayoutData(new RowData(new Point(60, 15)));
/* 790 */     this.latitudeText.setText(this.lastLat);
/*     */ 
/* 792 */     this.placeBtn = new Button(latGroup, 8);
/* 793 */     this.placeBtn.setText("Place Symbol");
/* 794 */     this.placeBtn.setEnabled(false);
/* 795 */     this.placeBtn.setLayoutData(new RowData(new Point(104, 27)));
/* 796 */     this.placeBtn.addListener(3, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/* 800 */         SymbolAttrDlg.this.placeSymbol();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createLonAttr()
/*     */   {
/* 809 */     Composite inCmp = new Composite(this.top, 0);
/* 810 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/* 812 */     this.chkBox[ChkBox.LON.ordinal()] = new Button(inCmp, 32);
/* 813 */     this.chkBox[ChkBox.LON.ordinal()].setLayoutData(new GridData(16, 28));
/* 814 */     this.chkBox[ChkBox.LON.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 818 */         Button btn = (Button)e.widget;
/* 819 */         if (btn.getSelection()) {
/* 820 */           SymbolAttrDlg.this.longitudeLabel.setEnabled(true);
/* 821 */           SymbolAttrDlg.this.longitudeText.setEnabled(true);
/*     */         }
/*     */         else {
/* 824 */           SymbolAttrDlg.this.longitudeLabel.setEnabled(false);
/* 825 */           SymbolAttrDlg.this.longitudeText.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 831 */     this.chkBox[ChkBox.LON.ordinal()].setVisible(false);
/*     */ 
/* 833 */     this.longitudeLabel = new Label(inCmp, 0);
/* 834 */     this.longitudeLabel.setText("Lon ");
/*     */ 
/* 836 */     Composite lonGroup = new Composite(inCmp, 0);
/* 837 */     lonGroup.setLayout(new RowLayout(256));
/* 838 */     this.longitudeText = new Text(lonGroup, 133124);
/* 839 */     this.longitudeText.setTextLimit(8);
/* 840 */     this.longitudeText.setLayoutData(new RowData(new Point(59, 15)));
/* 841 */     this.longitudeText.setText(this.lastLong);
/*     */ 
/* 843 */     this.latitudeText.addKeyListener(new LatLonKeyListener());
/* 844 */     this.latitudeText.addModifyListener(new LatModifyListener());
/* 845 */     this.latitudeText.addListener(25, new LatLonVerifyListener());
/*     */ 
/* 847 */     this.longitudeText.addKeyListener(new LatLonKeyListener());
/* 848 */     this.longitudeText.addModifyListener(new LonModifyListener());
/* 849 */     this.longitudeText.addListener(25, new LatLonVerifyListener());
/*     */ 
/* 851 */     this.undoBtn = new Button(lonGroup, 8);
/* 852 */     this.undoBtn.setText("Undo Symbol");
/* 853 */     this.undoBtn.setEnabled(false);
/* 854 */     this.undoBtn.setLayoutData(new RowData(new Point(100, 27)));
/*     */ 
/* 856 */     this.undoBtn.addListener(3, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/* 861 */         if (SymbolAttrDlg.this.undoBtn.getText().equalsIgnoreCase("Undo Symbol"))
/*     */         {
/* 863 */           SymbolAttrDlg.this.undoBtn.setText("Redo Symbol");
/* 864 */           SymbolAttrDlg.this.drawingLayer.getCommandMgr().undo();
/*     */         }
/* 869 */         else if (SymbolAttrDlg.this.undoBtn.getText().equalsIgnoreCase("Redo Symbol")) {
/* 870 */           SymbolAttrDlg.this.undoBtn.setText("Undo Symbol");
/* 871 */           SymbolAttrDlg.this.drawingLayer.getCommandMgr().redo();
/*     */         }
/*     */ 
/* 875 */         if (SymbolAttrDlg.this.drawingLayer.getSelectedDE() != null)
/*     */         {
/* 877 */           SymbolAttrDlg.this.drawingLayer.setSelected(SymbolAttrDlg.this.drawingLayer.getNearestElement(SymbolAttrDlg.this.prevLoc));
/*     */         }
/*     */ 
/* 881 */         SymbolAttrDlg.this.mapEditor.refresh();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void enableUndoBtn(boolean flag)
/*     */   {
/* 889 */     this.undoBtn.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 895 */     return null;
/*     */   }
/*     */ 
/*     */   public Coordinate getLocation()
/*     */   {
/* 900 */     return null;
/*     */   }
/*     */ 
/*     */   protected void placeSymbol()
/*     */   {
/* 908 */     DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/* 913 */     DrawableType which = DrawableType.SYMBOL;
/* 914 */     if (this.pgenCategory.equals("Combo")) which = DrawableType.COMBO_SYMBOL;
/*     */ 
/* 916 */     DrawableElement elem = (DrawableElement)def.create(which, this, this.pgenCategory, 
/* 917 */       this.pgenType, new Coordinate(Double.parseDouble(this.longitudeText.getText()), 
/* 918 */       Double.parseDouble(this.latitudeText.getText())), 
/* 919 */       this.drawingLayer.getActiveLayer());
/*     */ 
/* 921 */     if (this.drawingLayer.getSelectedDE() != null) {
/* 922 */       this.prevLoc = ((ISymbol)this.drawingLayer.getSelectedDE()).getLocation();
/* 923 */       this.drawingLayer.replaceElement(this.drawingLayer.getSelectedDE(), elem);
/* 924 */       this.drawingLayer.setSelected(elem);
/*     */     }
/* 926 */     else if (labelEnabled()) {
/* 927 */       DECollection dec = new DECollection("labeledSymbol");
/* 928 */       dec.setPgenCategory(this.pgenCategory);
/* 929 */       dec.setPgenType(this.pgenType);
/* 930 */       dec.addElement(elem);
/* 931 */       this.drawingLayer.addElement(dec);
/*     */ 
/* 933 */       String defaultTxt = "";
/* 934 */       if ((this instanceof VolcanoAttrDlg)) {
/* 935 */         defaultTxt = ((VolcanoAttrDlg)this).getVolText();
/* 936 */         dec.setCollectionName("Volcano");
/*     */       }
/* 938 */       PgenUtil.setDrawingTextMode(true, ((LabeledSymbolAttrDlg)this).useSymbolColor(), defaultTxt, dec);
/*     */     }
/*     */     else {
/* 941 */       this.drawingLayer.addElement(elem);
/* 942 */       this.placeBtn.setEnabled(false);
/* 943 */       this.undoBtn.setEnabled(true);
/* 944 */       this.undoBtn.setText("Undo Symbol");
/*     */     }
/*     */ 
/* 947 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   private void createColorClearAttr()
/*     */   {
/* 956 */     Composite inCmp = new Composite(this.top, 0);
/* 957 */     inCmp.setLayout(getGridLayout(2, false, 0, 0, 0, 0));
/*     */ 
/* 959 */     createColorAttr(inCmp);
/* 960 */     createClearAttr(inCmp);
/*     */   }
/*     */ 
/*     */   protected static enum ChkBox
/*     */   {
/*  83 */     COLOR, CLEAR, WIDTH, SIZE, LAT, LON, LABEL;
/*     */   }
/*     */ 
/*     */   public class LatLonKeyListener extends KeyAdapter
/*     */   {
/*     */     public LatLonKeyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void keyPressed(KeyEvent e)
/*     */     {
/* 368 */       SymbolAttrDlg.this.keyEvent = true;
/* 369 */       SymbolAttrDlg.this.drawingLayer.removeGhostLine();
/* 370 */       SymbolAttrDlg.this.mapEditor.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   public class LatLonVerifyListener
/*     */     implements Listener
/*     */   {
/*     */     public LatLonVerifyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void handleEvent(Event e)
/*     */     {
/* 387 */       if (SymbolAttrDlg.this.keyEvent)
/*     */       {
/* 389 */         e.doit = PgenUtil.validateNumberTextField(e);
/*     */       }
/*     */ 
/* 393 */       if (!e.doit)
/*     */       {
/* 395 */         SymbolAttrDlg.this.keyEvent = false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public class LatModifyListener
/*     */     implements ModifyListener
/*     */   {
/*     */     public LatModifyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void modifyText(ModifyEvent e)
/*     */     {
/* 411 */       if (SymbolAttrDlg.this.keyEvent)
/*     */       {
/* 413 */         Text txt = (Text)e.widget;
/*     */         try
/*     */         {
/* 417 */           if ((Double.valueOf(txt.getText()).doubleValue() > 90.0D) || 
/* 418 */             (Double.valueOf(txt.getText()).doubleValue() < -90.0D))
/*     */           {
/* 420 */             SymbolAttrDlg.this.placeBtn.setEnabled(false);
/*     */           }
/*     */           else
/*     */           {
/* 425 */             SymbolAttrDlg.this.placeBtn.setEnabled(true);
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (NullPointerException excp)
/*     */         {
/* 431 */           SymbolAttrDlg.this.placeBtn.setEnabled(false);
/*     */         }
/*     */         catch (NumberFormatException excp)
/*     */         {
/* 436 */           SymbolAttrDlg.this.placeBtn.setEnabled(false);
/*     */         }
/*     */ 
/* 440 */         SymbolAttrDlg.this.keyEvent = false;
/*     */       }
/*     */       else
/*     */       {
/* 448 */         SymbolAttrDlg.this.placeBtn.setEnabled(false);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public class LonModifyListener
/*     */     implements ModifyListener
/*     */   {
/*     */     public LonModifyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void modifyText(ModifyEvent e)
/*     */     {
/* 463 */       if (SymbolAttrDlg.this.keyEvent)
/*     */       {
/* 465 */         Text txt = (Text)e.widget;
/*     */         try
/*     */         {
/* 469 */           if ((Double.valueOf(txt.getText()).doubleValue() > 180.0D) || 
/* 470 */             (Double.valueOf(txt.getText()).doubleValue() < -180.0D))
/*     */           {
/* 472 */             SymbolAttrDlg.this.placeBtn.setEnabled(false);
/*     */           }
/*     */           else
/*     */           {
/* 477 */             SymbolAttrDlg.this.placeBtn.setEnabled(true);
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (NullPointerException excp)
/*     */         {
/* 483 */           SymbolAttrDlg.this.placeBtn.setEnabled(false);
/*     */         }
/*     */         catch (NumberFormatException excp)
/*     */         {
/* 488 */           SymbolAttrDlg.this.placeBtn.setEnabled(false);
/*     */         }
/*     */ 
/* 492 */         SymbolAttrDlg.this.keyEvent = false;
/*     */       }
/*     */       else
/*     */       {
/* 500 */         SymbolAttrDlg.this.placeBtn.setEnabled(false);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.SymbolAttrDlg
 * JD-Core Version:    0.6.2
 */