/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AvnText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Turbulence;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.ILabeledLine;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.eclipse.swt.events.ModifyEvent;
/*     */ import org.eclipse.swt.events.ModifyListener;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class TurbAttrDlg extends AttrDlg
/*     */   implements ILine
/*     */ {
/*     */   private static TurbAttrDlg INSTANCE;
/*     */   private ILabeledLine turbTool;
/*     */   private Composite top;
/*     */   private Label topLabel;
/*     */   private Text topValue;
/*     */   private Label bottomLabel;
/*     */   private Text bottomValue;
/*     */   protected Label colorLbl;
/*     */   protected ColorButtonSelector cs;
/*     */   protected Button closedChkBox;
/*     */   private Button addLineBtn;
/*     */   private Button delLineBtn;
/*     */   private Button addLabelBtn;
/*     */   private Button delLabelBtn;
/*     */   private SymbolCombo symbolCombo;
/* 109 */   private static final String[] TURB_LIST = { "TURBULENCE_0", "TURBULENCE_1", "TURBULENCE_2", 
/* 110 */     "TURBULENCE_3", "TURBULENCE_4", 
/* 111 */     "TURBULENCE_4|TURBULENCE_6", "TURBULENCE_5", 
/* 112 */     "TURBULENCE_6", "TURBULENCE_6|TURBULENCE_7", 
/* 113 */     "TURBULENCE_7", "TURBULENCE_8" };
/*     */ 
/* 115 */   private float lineWidth = 2.0F;
/*     */ 
/* 117 */   private String prevTop = "";
/* 118 */   private String prevBottom = "";
/* 119 */   private String prevSymbol = "";
/*     */ 
/*     */   private TurbAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 127 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static TurbAttrDlg getInstance(Shell parShell)
/*     */   {
/* 140 */     if (INSTANCE == null) {
/*     */       try
/*     */       {
/* 143 */         INSTANCE = new TurbAttrDlg(parShell);
/*     */       } catch (VizException e) {
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
/* 165 */     mainLayout.marginWidth = 20;
/* 166 */     this.top.setLayout(mainLayout);
/*     */ 
/* 169 */     initializeComponents();
/*     */ 
/* 171 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 181 */     getShell().setText("Turbulence Attributes");
/*     */ 
/* 185 */     Composite pane1 = new Composite(this.top, 0);
/* 186 */     GridLayout gl = new GridLayout(2, false);
/* 187 */     pane1.setLayout(gl);
/*     */ 
/* 192 */     createTopAttr(pane1);
/*     */ 
/* 197 */     createBottomAttr(pane1);
/*     */ 
/* 202 */     createColorAttr(pane1);
/*     */ 
/* 207 */     createSymbolAttr(pane1);
/*     */ 
/* 212 */     createClosedAttr(pane1);
/*     */ 
/* 215 */     Button openCloseBtn = new Button(pane1, 2);
/* 216 */     openCloseBtn.setVisible(false);
/*     */ 
/* 239 */     this.addLineBtn = new Button(pane1, 2);
/* 240 */     this.addLineBtn.setText("Add Line");
/* 241 */     this.addLineBtn.setLayoutData(new GridData(120, 30));
/* 242 */     this.addLineBtn.setSelection(true);
/* 243 */     this.addLineBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 246 */         if (((Button)e.widget).getSelection())
/*     */         {
/* 248 */           TurbAttrDlg.this.turbTool.resetMouseHandler();
/* 249 */           TurbAttrDlg.this.addLabelBtn.setSelection(false);
/* 250 */           TurbAttrDlg.this.delLineBtn.setSelection(false);
/* 251 */           TurbAttrDlg.this.delLabelBtn.setSelection(false);
/*     */         }
/*     */       }
/*     */     });
/* 261 */     this.addLabelBtn = new Button(pane1, 2);
/* 262 */     this.addLabelBtn.setText("Add Label");
/* 263 */     this.addLabelBtn.setLayoutData(new GridData(120, 30));
/* 264 */     this.addLabelBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 267 */         if (((Button)e.widget).getSelection()) {
/* 268 */           if ((TurbAttrDlg.this.turbTool != null) && (TurbAttrDlg.this.turbTool.getLabeledLine() != null) && 
/* 269 */             (TurbAttrDlg.this.turbTool.getLabeledLine().getLines().size() > 0)) {
/* 270 */             TurbAttrDlg.this.turbTool.setAddingLabelHandler();
/* 271 */             TurbAttrDlg.this.addLineBtn.setSelection(false);
/* 272 */             TurbAttrDlg.this.delLineBtn.setSelection(false);
/* 273 */             TurbAttrDlg.this.delLabelBtn.setSelection(false);
/*     */           }
/*     */           else
/*     */           {
/* 277 */             e.doit = false;
/* 278 */             ((Button)e.widget).setSelection(false);
/*     */           }
/*     */ 
/*     */         }
/* 282 */         else if (TurbAttrDlg.this.turbTool != null)
/* 283 */           TurbAttrDlg.this.turbTool.resetMouseHandler();
/*     */       }
/*     */     });
/* 293 */     this.delLineBtn = new Button(pane1, 2);
/* 294 */     this.delLineBtn.setText("Del Line");
/* 295 */     this.delLineBtn.setLayoutData(new GridData(120, 30));
/* 296 */     this.delLineBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 299 */         if (((Button)e.widget).getSelection()) {
/* 300 */           TurbAttrDlg.this.turbTool.setDeleteHandler(true, false, false);
/* 301 */           TurbAttrDlg.this.addLineBtn.setSelection(false);
/* 302 */           TurbAttrDlg.this.addLabelBtn.setSelection(false);
/* 303 */           TurbAttrDlg.this.delLabelBtn.setSelection(false);
/*     */         }
/*     */         else
/*     */         {
/* 307 */           TurbAttrDlg.this.turbTool.resetMouseHandler();
/*     */         }
/*     */       }
/*     */     });
/* 314 */     this.delLabelBtn = new Button(pane1, 2);
/* 315 */     this.delLabelBtn.setText("Del Label");
/* 316 */     this.delLabelBtn.setLayoutData(new GridData(120, 30));
/* 317 */     this.delLabelBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 320 */         if (((Button)e.widget).getSelection()) {
/* 321 */           TurbAttrDlg.this.turbTool.setDeleteHandler(false, false, false);
/* 322 */           TurbAttrDlg.this.addLineBtn.setSelection(false);
/* 323 */           TurbAttrDlg.this.addLabelBtn.setSelection(false);
/* 324 */           TurbAttrDlg.this.delLineBtn.setSelection(false);
/*     */         }
/*     */         else
/*     */         {
/* 328 */           TurbAttrDlg.this.turbTool.resetMouseHandler();
/*     */         }
/*     */       }
/*     */     });
/* 357 */     addSeparator(this.top);
/*     */   }
/*     */ 
/*     */   private void createTopAttr(Composite comp)
/*     */   {
/* 383 */     this.topLabel = new Label(comp, 0);
/* 384 */     this.topLabel.setText("Top:");
/*     */ 
/* 386 */     this.topValue = new Text(comp, 2052);
/* 387 */     this.topValue.setEditable(true);
/*     */ 
/* 389 */     if (this.prevTop.isEmpty()) {
/* 390 */       this.topValue.setText("XXX");
/*     */     }
/*     */     else {
/* 393 */       this.topValue.setText(this.prevTop);
/*     */     }
/*     */ 
/* 396 */     this.topValue.addModifyListener(new ModifyListener()
/*     */     {
/*     */       public void modifyText(ModifyEvent e)
/*     */       {
/* 400 */         TurbAttrDlg.this.prevTop = ((Text)e.widget).getText();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createBottomAttr(Composite comp)
/*     */   {
/* 429 */     this.bottomLabel = new Label(comp, 0);
/* 430 */     this.bottomLabel.setText("Bottom:");
/*     */ 
/* 432 */     this.bottomValue = new Text(comp, 2052);
/* 433 */     this.bottomValue.setEditable(true);
/*     */ 
/* 435 */     if (this.prevBottom.isEmpty()) {
/* 436 */       this.bottomValue.setText("XXX");
/*     */     }
/*     */     else {
/* 439 */       this.bottomValue.setText(this.prevBottom);
/*     */     }
/*     */ 
/* 442 */     this.bottomValue.addModifyListener(new ModifyListener()
/*     */     {
/*     */       public void modifyText(ModifyEvent e)
/*     */       {
/* 446 */         TurbAttrDlg.this.prevBottom = ((Text)e.widget).getText();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createColorAttr(Composite comp)
/*     */   {
/* 473 */     Composite colorGroup = new Composite(comp, 0);
/* 474 */     GridLayout gl = new GridLayout(2, false);
/* 475 */     gl.marginWidth = 0;
/* 476 */     colorGroup.setLayout(gl);
/*     */ 
/* 478 */     this.colorLbl = new Label(colorGroup, 16384);
/* 479 */     this.colorLbl.setText("Color:");
/* 480 */     this.cs = new ColorButtonSelector(colorGroup);
/* 481 */     this.cs.setColorValue(new RGB(238, 238, 0));
/*     */   }
/*     */ 
/*     */   private void createSymbolAttr(Composite comp)
/*     */   {
/* 504 */     this.symbolCombo = new SymbolCombo(comp);
/* 505 */     this.symbolCombo.setLayoutData(new GridData(10, 1));
/* 506 */     this.symbolCombo.setItems(TURB_LIST);
/*     */ 
/* 508 */     if (this.prevSymbol.isEmpty()) {
/* 509 */       this.symbolCombo.select(4);
/*     */     }
/*     */     else
/* 512 */       this.symbolCombo.setSelectedText(this.prevSymbol);
/*     */   }
/*     */ 
/*     */   private void createClosedAttr(Composite comp)
/*     */   {
/* 541 */     this.closedChkBox = new Button(comp, 32);
/* 542 */     this.closedChkBox.setText("Closed");
/* 543 */     this.closedChkBox.setSelection(true);
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute ia)
/*     */   {
/* 548 */     if ((this.turbTool != null) && (this.turbTool.getLabeledLine() != null)) {
/* 549 */       Iterator it = this.turbTool.getLabeledLine().createDEIterator();
/* 550 */       while (it.hasNext()) {
/* 551 */         DrawableElement de = (DrawableElement)it.next();
/* 552 */         if ((de instanceof AvnText)) {
/* 553 */           setColor(de.getColors()[0]);
/* 554 */           this.topValue.setText(((AvnText)de).getTopValue());
/* 555 */           this.bottomValue.setText(((AvnText)de).getBottomValue());
/* 556 */           break;
/*     */         }
/* 558 */         if ((de instanceof Line)) {
/* 559 */           setColor(de.getColors()[0]);
/*     */         }
/*     */       }
/* 562 */       Line ln = (Line)this.turbTool.getLabeledLine().getPrimaryDE();
/* 563 */       this.closedChkBox.setSelection(ln.isClosedLine().booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAttr(AbstractDrawableComponent adc)
/*     */   {
/* 573 */     if ((adc instanceof Turbulence)) {
/* 574 */       LabeledLine ll = (LabeledLine)adc;
/* 575 */       Iterator it = ll.createDEIterator();
/* 576 */       while (it.hasNext()) {
/* 577 */         DrawableElement de = (DrawableElement)it.next();
/* 578 */         if ((de instanceof AvnText)) {
/* 579 */           setColor(de.getColors()[0]);
/* 580 */           this.topValue.setText(((AvnText)de).getTopValue());
/* 581 */           this.bottomValue.setText(((AvnText)de).getBottomValue());
/* 582 */           break;
/*     */         }
/* 584 */         if ((de instanceof Line)) {
/* 585 */           setColor(de.getColors()[0]);
/*     */         }
/*     */       }
/* 588 */       Line ln = (Line)ll.getPrimaryDE();
/* 589 */       this.closedChkBox.setSelection(ln.isClosedLine().booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getSmoothFactor()
/*     */   {
/* 595 */     return 2;
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 605 */     Color[] colors = new Color[2];
/*     */ 
/* 607 */     colors[0] = new Color(this.cs.getColorValue().red, 
/* 608 */       this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*     */ 
/* 611 */     colors[1] = colors[0];
/*     */ 
/* 613 */     return colors;
/*     */   }
/*     */ 
/*     */   public void setColor(Color clr)
/*     */   {
/* 626 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 633 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */   public void setLineWidth(float lnWidth) {
/* 637 */     this.lineWidth = lnWidth;
/*     */   }
/*     */ 
/*     */   public Boolean isClosedLine()
/*     */   {
/* 643 */     return Boolean.valueOf(this.closedChkBox.getSelection());
/*     */   }
/*     */ 
/*     */   public String getTopValue() {
/* 647 */     return this.topValue.getText();
/*     */   }
/*     */ 
/*     */   public String getBottomValue() {
/* 651 */     return this.bottomValue.getText();
/*     */   }
/*     */ 
/*     */   public void setTurbDrawingTool(ILabeledLine pgenTool)
/*     */   {
/* 656 */     this.turbTool = pgenTool;
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 666 */     PgenUtil.setSelectingMode();
/* 667 */     super.cancelPressed();
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 675 */     if ((this.turbTool != null) && (this.turbTool.getLabeledLine() != null)) {
/* 676 */       LabeledLine ll = this.turbTool.getLabeledLine();
/* 677 */       LabeledLine newll = ll.copy();
/*     */ 
/* 679 */       Iterator it = newll.createDEIterator();
/* 680 */       while (it.hasNext()) {
/* 681 */         DrawableElement de = (DrawableElement)it.next();
/* 682 */         if ((de instanceof AvnText)) {
/* 683 */           de.setColors(getColors());
/* 684 */           ((AvnText)de).setTopValue(this.topValue.getText());
/* 685 */           ((AvnText)de).setBottomValue(this.bottomValue.getText());
/* 686 */           ((AvnText)de).setSymbolPatternName(getSymbolPatternName());
/*     */         }
/* 688 */         else if ((de instanceof Line)) {
/* 689 */           de.setColors(getColors());
/* 690 */           ((Line)de).setClosed(isClosedLine());
/*     */         }
/*     */       }
/*     */ 
/* 694 */       this.drawingLayer.replaceElement(ll, newll);
/* 695 */       this.turbTool.setLabeledLine(newll);
/* 696 */       AttrSettings.getInstance().setSettings(newll);
/*     */ 
/* 699 */       this.drawingLayer.removeSelected();
/* 700 */       Iterator iterator = newll.createDEIterator();
/* 701 */       while (iterator.hasNext()) {
/* 702 */         this.drawingLayer.addSelected((AbstractDrawableComponent)iterator.next());
/*     */       }
/*     */ 
/* 705 */       this.mapEditor.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resetLabeledLineBtns()
/*     */   {
/* 712 */     this.addLineBtn.setSelection(false);
/* 713 */     this.addLabelBtn.setSelection(false);
/* 714 */     this.delLineBtn.setSelection(false);
/* 715 */     this.delLabelBtn.setSelection(false);
/*     */   }
/*     */ 
/*     */   public boolean isAddLineMode()
/*     */   {
/* 720 */     return this.addLineBtn.getSelection();
/*     */   }
/*     */ 
/*     */   public String getSymbolPatternName() {
/* 724 */     return this.symbolCombo.getSelectedText();
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 730 */     return null;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLinePoints()
/*     */   {
/* 736 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 742 */     return null;
/*     */   }
/*     */ 
/*     */   public FillPatternList.FillPattern getFillPattern()
/*     */   {
/* 748 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean close()
/*     */   {
/* 757 */     if (!this.symbolCombo.isDisposed())
/* 758 */       this.prevSymbol = this.symbolCombo.getSelectedText();
/* 759 */     return super.close();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.TurbAttrDlg
 * JD-Core Version:    0.6.2
 */