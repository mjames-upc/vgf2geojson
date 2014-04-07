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
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Cloud;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.ILabeledLine;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class CloudAttrDlg extends AttrDlg
/*     */   implements ILine
/*     */ {
/*     */   private static CloudAttrDlg INSTANCE;
/*     */   private ILabeledLine cloudTool;
/*     */   private MidLevelCloudAttrDlg labelDlg;
/*     */   private Composite top;
/*     */   protected org.eclipse.swt.widgets.Label colorLbl;
/*     */   protected ColorButtonSelector cs;
/*     */   protected Button closedChkBox;
/*     */   private Button addLineBtn;
/*     */   private Button delLineBtn;
/*     */   private Button addLabelBtn;
/*     */   private Button delLabelBtn;
/*     */   private Button flipBtn;
/*     */   private Button editLabelBtn;
/* 103 */   private float lineWidth = 2.0F;
/*     */   private Point labelDlgLocation;
/*     */ 
/*     */   private CloudAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 113 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static CloudAttrDlg getInstance(Shell parShell)
/*     */   {
/* 126 */     if (INSTANCE == null) {
/*     */       try
/*     */       {
/* 129 */         INSTANCE = new CloudAttrDlg(parShell);
/*     */       } catch (VizException e) {
/* 131 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 136 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 146 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 149 */     GridLayout mainLayout = new GridLayout(1, false);
/* 150 */     mainLayout.marginHeight = 3;
/* 151 */     mainLayout.marginWidth = 20;
/* 152 */     this.top.setLayout(mainLayout);
/*     */ 
/* 155 */     initializeComponents();
/*     */ 
/* 157 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 167 */     getShell().setText("Cloud Attributes");
/*     */ 
/* 171 */     Composite pane1 = new Composite(this.top, 0);
/* 172 */     GridLayout gl = new GridLayout(2, false);
/* 173 */     pane1.setLayout(gl);
/*     */ 
/* 178 */     createColorAttr(pane1);
/*     */ 
/* 183 */     createClosedAttr(pane1);
/*     */ 
/* 186 */     this.addLineBtn = new Button(pane1, 2);
/* 187 */     this.addLineBtn.setText("Add Line");
/* 188 */     this.addLineBtn.setLayoutData(new GridData(120, 30));
/* 189 */     this.addLineBtn.setSelection(true);
/* 190 */     this.addLineBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 193 */         if (((Button)e.widget).getSelection())
/*     */         {
/* 195 */           CloudAttrDlg.this.cloudTool.resetMouseHandler();
/* 196 */           CloudAttrDlg.this.addLabelBtn.setSelection(false);
/* 197 */           if (CloudAttrDlg.this.labelDlg != null) CloudAttrDlg.this.labelDlg.close();
/* 198 */           CloudAttrDlg.this.delLineBtn.setSelection(false);
/* 199 */           CloudAttrDlg.this.delLabelBtn.setSelection(false);
/* 200 */           CloudAttrDlg.this.flipBtn.setSelection(false);
/*     */         }
/*     */       }
/*     */     });
/* 210 */     this.addLabelBtn = new Button(pane1, 2);
/* 211 */     this.addLabelBtn.setText("Add Label");
/* 212 */     this.addLabelBtn.setLayoutData(new GridData(120, 30));
/* 213 */     this.addLabelBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 216 */         if (((Button)e.widget).getSelection()) {
/* 217 */           if ((CloudAttrDlg.this.cloudTool != null) && (CloudAttrDlg.this.cloudTool.getLabeledLine() != null) && 
/* 218 */             (CloudAttrDlg.this.cloudTool.getLabeledLine().getLines().size() > 0)) {
/* 219 */             CloudAttrDlg.this.cloudTool.setAddingLabelHandler();
/* 220 */             CloudAttrDlg.this.addLineBtn.setSelection(false);
/* 221 */             CloudAttrDlg.this.delLineBtn.setSelection(false);
/* 222 */             CloudAttrDlg.this.delLabelBtn.setSelection(false);
/* 223 */             CloudAttrDlg.this.flipBtn.setSelection(false);
/*     */             try
/*     */             {
/* 226 */               CloudAttrDlg.this.labelDlg = new CloudAttrDlg.LabelAttrDlg(CloudAttrDlg.this, CloudAttrDlg.this.getParentShell(), null);
/*     */ 
/* 228 */               CloudAttrDlg.this.labelDlg.setBlockOnOpen(false);
/* 229 */               CloudAttrDlg.this.labelDlg.open();
/* 230 */               CloudAttrDlg.this.labelDlg.setPgenType("MID_LEVEL_CLOUD");
/* 231 */               CloudAttrDlg.this.labelDlg.setDefaultAttr();
/*     */             }
/*     */             catch (VizException e1)
/*     */             {
/* 235 */               e1.printStackTrace();
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 240 */             e.doit = false;
/* 241 */             ((Button)e.widget).setSelection(false);
/*     */           }
/*     */         }
/*     */         else {
/* 245 */           if (CloudAttrDlg.this.labelDlg != null) CloudAttrDlg.this.labelDlg.close();
/* 246 */           if (CloudAttrDlg.this.cloudTool != null) CloudAttrDlg.this.cloudTool.resetMouseHandler();
/*     */         }
/*     */       }
/*     */     });
/* 254 */     this.delLineBtn = new Button(pane1, 2);
/* 255 */     this.delLineBtn.setText("Del Line");
/* 256 */     this.delLineBtn.setLayoutData(new GridData(120, 30));
/* 257 */     this.delLineBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 260 */         if (((Button)e.widget).getSelection()) {
/* 261 */           CloudAttrDlg.this.cloudTool.setDeleteHandler(true, false, false);
/* 262 */           CloudAttrDlg.this.addLineBtn.setSelection(false);
/* 263 */           CloudAttrDlg.this.addLabelBtn.setSelection(false);
/* 264 */           if (CloudAttrDlg.this.labelDlg != null) CloudAttrDlg.this.labelDlg.close();
/* 265 */           CloudAttrDlg.this.delLabelBtn.setSelection(false);
/* 266 */           CloudAttrDlg.this.flipBtn.setSelection(false);
/*     */         }
/*     */         else
/*     */         {
/* 270 */           CloudAttrDlg.this.cloudTool.resetMouseHandler();
/*     */         }
/*     */       }
/*     */     });
/* 277 */     this.delLabelBtn = new Button(pane1, 2);
/* 278 */     this.delLabelBtn.setText("Del Label");
/* 279 */     this.delLabelBtn.setLayoutData(new GridData(120, 30));
/* 280 */     this.delLabelBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 283 */         if (((Button)e.widget).getSelection()) {
/* 284 */           CloudAttrDlg.this.cloudTool.setDeleteHandler(false, false, false);
/* 285 */           CloudAttrDlg.this.addLineBtn.setSelection(false);
/* 286 */           CloudAttrDlg.this.addLabelBtn.setSelection(false);
/* 287 */           if (CloudAttrDlg.this.labelDlg != null) CloudAttrDlg.this.labelDlg.close();
/* 288 */           CloudAttrDlg.this.delLineBtn.setSelection(false);
/* 289 */           CloudAttrDlg.this.flipBtn.setSelection(false);
/*     */         }
/*     */         else
/*     */         {
/* 293 */           CloudAttrDlg.this.cloudTool.resetMouseHandler();
/*     */         }
/*     */       }
/*     */     });
/* 300 */     this.flipBtn = new Button(pane1, 2);
/* 301 */     this.flipBtn.setText("Flip");
/* 302 */     this.flipBtn.setLayoutData(new GridData(120, 30));
/* 303 */     this.flipBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 306 */         if (((Button)e.widget).getSelection()) {
/* 307 */           CloudAttrDlg.this.cloudTool.setDeleteHandler(true, true, false);
/* 308 */           CloudAttrDlg.this.addLineBtn.setSelection(false);
/* 309 */           CloudAttrDlg.this.addLabelBtn.setSelection(false);
/* 310 */           if (CloudAttrDlg.this.labelDlg != null) CloudAttrDlg.this.labelDlg.close();
/* 311 */           CloudAttrDlg.this.delLineBtn.setSelection(false);
/* 312 */           CloudAttrDlg.this.delLabelBtn.setSelection(false);
/*     */         }
/*     */         else
/*     */         {
/* 316 */           CloudAttrDlg.this.cloudTool.resetMouseHandler();
/*     */         }
/*     */       }
/*     */     });
/* 323 */     this.editLabelBtn = new Button(pane1, 8);
/* 324 */     this.editLabelBtn.setText("Edit Label");
/* 325 */     this.editLabelBtn.setLayoutData(new GridData(120, 30));
/* 326 */     this.editLabelBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 329 */         if ((CloudAttrDlg.this.cloudTool != null) && (CloudAttrDlg.this.cloudTool.getLabeledLine() != null) && 
/* 330 */           (CloudAttrDlg.this.cloudTool.getLabeledLine().getLines().size() > 0) && 
/* 331 */           (!CloudAttrDlg.this.addLabelBtn.getSelection())) {
/* 332 */           CloudAttrDlg.this.addLineBtn.setSelection(false);
/* 333 */           CloudAttrDlg.this.delLineBtn.setSelection(false);
/* 334 */           CloudAttrDlg.this.delLabelBtn.setSelection(false);
/* 335 */           CloudAttrDlg.this.addLabelBtn.setSelection(false);
/* 336 */           CloudAttrDlg.this.flipBtn.setSelection(false);
/*     */           try
/*     */           {
/* 340 */             CloudAttrDlg.this.labelDlg = new CloudAttrDlg.LabelAttrDlg(CloudAttrDlg.this, CloudAttrDlg.this.getParentShell(), null);
/* 341 */             CloudAttrDlg.this.labelDlg.setBlockOnOpen(false);
/* 342 */             CloudAttrDlg.this.labelDlg.open();
/* 343 */             CloudAttrDlg.this.labelDlg.setPgenType("MID_LEVEL_CLOUD");
/* 344 */             CloudAttrDlg.this.labelDlg.setDefaultAttr();
/* 345 */             CloudAttrDlg.this.labelDlg.enableButtons();
/* 346 */             CloudAttrDlg.this.labelDlg.setDrawableElement(CloudAttrDlg.this.cloudTool.getLabeledLine());
/* 347 */             CloudAttrDlg.this.labelDlg.setDrawingLayer(CloudAttrDlg.this.drawingLayer);
/*     */           }
/*     */           catch (VizException e1)
/*     */           {
/* 351 */             e1.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */     });
/* 379 */     addSeparator(this.top);
/*     */   }
/*     */ 
/*     */   private void createColorAttr(Composite comp)
/*     */   {
/* 405 */     Composite colorGroup = new Composite(comp, 0);
/* 406 */     GridLayout gl = new GridLayout(2, false);
/* 407 */     gl.marginWidth = 0;
/* 408 */     colorGroup.setLayout(gl);
/*     */ 
/* 410 */     this.colorLbl = new org.eclipse.swt.widgets.Label(colorGroup, 16384);
/* 411 */     this.colorLbl.setText("Color:");
/* 412 */     this.cs = new ColorButtonSelector(colorGroup);
/* 413 */     this.cs.setColorValue(new RGB(0, 178, 238));
/*     */   }
/*     */ 
/*     */   private void createClosedAttr(Composite comp)
/*     */   {
/* 440 */     this.closedChkBox = new Button(comp, 32);
/* 441 */     this.closedChkBox.setText("Closed");
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute ia)
/*     */   {
/* 446 */     Line ln = null;
/*     */ 
/* 448 */     if (ia != null) {
/* 449 */       ln = (Line)ia;
/*     */     }
/* 451 */     else if ((this.cloudTool != null) && (this.cloudTool.getLabeledLine() != null)) {
/* 452 */       ln = (Line)this.cloudTool.getLabeledLine().getPrimaryDE();
/*     */     }
/*     */ 
/* 455 */     if (ln != null) {
/* 456 */       setColor(ln.getColors()[0]);
/* 457 */       this.closedChkBox.setSelection(ln.isClosedLine().booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAttr(AbstractDrawableComponent adc)
/*     */   {
/* 466 */     if ((adc instanceof Cloud))
/* 467 */       setAttrForDlg((IAttribute)((Cloud)adc).getLines().get(0));
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 478 */     Color[] colors = new Color[2];
/*     */ 
/* 480 */     colors[0] = new Color(this.cs.getColorValue().red, 
/* 481 */       this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*     */ 
/* 484 */     colors[1] = colors[0];
/*     */ 
/* 486 */     return colors;
/*     */   }
/*     */ 
/*     */   public void setColor(Color clr)
/*     */   {
/* 499 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 509 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */   public void setLineWidth(float lnWidth)
/*     */   {
/* 517 */     this.lineWidth = lnWidth;
/*     */   }
/*     */ 
/*     */   public Boolean isClosedLine()
/*     */   {
/* 526 */     return Boolean.valueOf(this.closedChkBox.getSelection());
/*     */   }
/*     */ 
/*     */   public void setCloudDrawingTool(ILabeledLine pgenTool)
/*     */   {
/* 552 */     this.cloudTool = pgenTool;
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 562 */     PgenUtil.setSelectingMode();
/* 563 */     super.cancelPressed();
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 572 */     if ((this.cloudTool != null) && (this.cloudTool.getLabeledLine() != null)) {
/* 573 */       LabeledLine ll = this.cloudTool.getLabeledLine();
/* 574 */       LabeledLine newll = ll.copy();
/*     */ 
/* 576 */       Iterator it = newll.getComponentIterator();
/* 577 */       while (it.hasNext()) {
/* 578 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 579 */         if ((adc instanceof Line)) {
/* 580 */           ((Line)adc).setColors(getColors());
/* 581 */           ((Line)adc).setClosed(isClosedLine());
/*     */         }
/*     */       }
/*     */ 
/* 585 */       this.drawingLayer.replaceElement(ll, newll);
/* 586 */       this.cloudTool.setLabeledLine(newll);
/* 587 */       AttrSettings.getInstance().setSettings(newll);
/*     */ 
/* 590 */       this.drawingLayer.removeSelected();
/* 591 */       Iterator iterator = newll.createDEIterator();
/* 592 */       while (iterator.hasNext()) {
/* 593 */         this.drawingLayer.addSelected((AbstractDrawableComponent)iterator.next());
/*     */       }
/*     */ 
/* 596 */       this.mapEditor.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resetLabeledLineBtns()
/*     */   {
/* 603 */     this.addLineBtn.setSelection(false);
/* 604 */     this.addLabelBtn.setSelection(false);
/* 605 */     if (this.labelDlg != null) this.labelDlg.close();
/* 606 */     this.delLineBtn.setSelection(false);
/* 607 */     this.delLabelBtn.setSelection(false);
/* 608 */     this.flipBtn.setSelection(false);
/*     */   }
/*     */ 
/*     */   public boolean isAddLineMode()
/*     */   {
/* 613 */     return this.addLineBtn.getSelection();
/*     */   }
/*     */ 
/*     */   public boolean close()
/*     */   {
/* 622 */     if (this.labelDlg != null) this.labelDlg.close();
/* 623 */     return super.close();
/*     */   }
/*     */ 
/*     */   public MidLevelCloudAttrDlg getLabelDlg()
/*     */   {
/* 632 */     return this.labelDlg;
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 713 */     return null;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLinePoints()
/*     */   {
/* 719 */     return null;
/*     */   }
/*     */ 
/*     */   public int getSmoothFactor()
/*     */   {
/* 724 */     return 2;
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 730 */     return null;
/*     */   }
/*     */ 
/*     */   public FillPatternList.FillPattern getFillPattern()
/*     */   {
/* 736 */     return null;
/*     */   }
/*     */ 
/*     */   private class LabelAttrDlg extends MidLevelCloudAttrDlg
/*     */   {
/*     */     private LabelAttrDlg(Shell parShell)
/*     */       throws VizException
/*     */     {
/* 650 */       super();
/*     */     }
/*     */ 
/*     */     public void okPressed()
/*     */     {
/* 657 */       Cloud newCloud = (Cloud)CloudAttrDlg.this.cloudTool.getLabeledLine().copy();
/*     */ 
/* 660 */       for (gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Label lbl : newCloud.getLabels()) {
/* 661 */         if ((lbl.getSpe() instanceof MidCloudText)) {
/* 662 */           ((MidCloudText)lbl.getSpe()).update(this);
/* 663 */           for (Line ln : lbl.getArrows()) {
/* 664 */             ln.setColors(getColors());
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 670 */       if (!newCloud.getLabels().isEmpty()) {
/* 671 */         AttrSettings.getInstance().setSettings(((gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Label)newCloud.getLabels().get(0)).getSpe());
/*     */       }
/*     */ 
/* 675 */       CloudAttrDlg.this.drawingLayer.replaceElement(CloudAttrDlg.this.cloudTool.getLabeledLine(), newCloud);
/* 676 */       this.drawingLayer.setSelected(newCloud);
/*     */ 
/* 678 */       CloudAttrDlg.this.cloudTool.setLabeledLine(newCloud);
/*     */ 
/* 680 */       CloudAttrDlg.this.mapEditor.refresh();
/* 681 */       close();
/*     */     }
/*     */ 
/*     */     public int open()
/*     */     {
/* 690 */       if (CloudAttrDlg.this.labelDlgLocation != null) {
/* 691 */         this.shellLocation = CloudAttrDlg.this.labelDlgLocation;
/*     */       }
/*     */ 
/* 694 */       return super.open();
/*     */     }
/*     */ 
/*     */     public boolean close()
/*     */     {
/* 702 */       if (getShell() != null) {
/* 703 */         Rectangle bounds = getShell().getBounds();
/* 704 */         CloudAttrDlg.this.labelDlgLocation = new Point(bounds.x, bounds.y);
/*     */       }
/* 706 */       return super.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.CloudAttrDlg
 * JD-Core Version:    0.6.2
 */