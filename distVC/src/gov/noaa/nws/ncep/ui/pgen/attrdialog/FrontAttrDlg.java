/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.SpinnerSlider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class FrontAttrDlg extends LineAttrDlg
/*     */ {
/*  42 */   private static FrontAttrDlg INSTANCE = null;
/*     */   private Button labelChkBox;
/*     */   private Button labelColorChkBox;
/*     */   private boolean lastLabelStatus;
/*     */   private boolean lastUseColorStatus;
/*     */ 
/*     */   protected FrontAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  57 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static FrontAttrDlg getInstance(Shell parShell)
/*     */   {
/*  70 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/*  74 */         INSTANCE = new FrontAttrDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/*  78 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  83 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   protected void initializeComponents()
/*     */   {
/*  92 */     super.initializeComponents();
/*     */ 
/*  94 */     Composite inCmp = new Composite(this.top, 0);
/*  95 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/*  97 */     this.chkBox[LineAttrDlg.ChkBox.LABEL.ordinal()] = new Button(inCmp, 32);
/*  98 */     this.chkBox[LineAttrDlg.ChkBox.LABEL.ordinal()].setLayoutData(new GridData(16, 28));
/*  99 */     this.chkBox[LineAttrDlg.ChkBox.LABEL.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 103 */         Button btn = (Button)e.widget;
/* 104 */         if (btn.getSelection()) {
/* 105 */           FrontAttrDlg.this.labelChkBox.setEnabled(true);
/*     */         }
/*     */         else
/* 108 */           FrontAttrDlg.this.labelChkBox.setEnabled(false);
/*     */       }
/*     */     });
/* 114 */     this.chkBox[LineAttrDlg.ChkBox.LABEL.ordinal()].setVisible(false);
/*     */ 
/* 116 */     this.labelChkBox = new Button(inCmp, 32);
/* 117 */     this.labelChkBox.setText("Label");
/*     */ 
/* 119 */     this.labelColorChkBox = new Button(inCmp, 32);
/* 120 */     this.labelColorChkBox.setText("Use Front Color");
/*     */ 
/* 122 */     this.labelChkBox.addListener(4, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/* 127 */         if (((Button)event.widget).getSelection()) {
/* 128 */           FrontAttrDlg.this.labelColorChkBox.setEnabled(true);
/*     */         }
/*     */         else {
/* 131 */           FrontAttrDlg.this.labelColorChkBox.setEnabled(false);
/*     */         }
/*     */ 
/* 135 */         FrontAttrDlg.this.lastLabelStatus = ((Button)event.widget).getSelection();
/*     */       }
/*     */     });
/* 139 */     this.labelColorChkBox.addListener(4, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/* 143 */         FrontAttrDlg.this.lastUseColorStatus = ((Button)event.widget).getSelection();
/*     */       }
/*     */     });
/* 148 */     if ((de != null) && (de.getPgenType().equalsIgnoreCase(this.pgenType))) {
/* 149 */       this.labelChkBox.setSelection(this.lastLabelStatus);
/* 150 */       this.labelColorChkBox.setSelection(this.lastUseColorStatus);
/*     */     }
/*     */     else {
/* 153 */       this.labelChkBox.setSelection(false);
/* 154 */       this.labelColorChkBox.setSelection(false);
/* 155 */       this.lastLabelStatus = false;
/* 156 */       this.lastUseColorStatus = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean labelEnabled()
/*     */   {
/* 166 */     return this.labelChkBox.getSelection();
/*     */   }
/*     */ 
/*     */   public boolean useFrontColor()
/*     */   {
/* 174 */     return this.labelColorChkBox.getSelection();
/*     */   }
/*     */ 
/*     */   public void setLabelChkBox(boolean enabled)
/*     */   {
/* 182 */     this.labelChkBox.setEnabled(enabled);
/*     */ 
/* 184 */     if ((!this.labelChkBox.isEnabled()) || (!this.labelChkBox.getSelection()))
/* 185 */       this.labelColorChkBox.setEnabled(false);
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 192 */     create();
/* 193 */     int rt = super.open();
/*     */ 
/* 196 */     if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 196 */       .equalsIgnoreCase("MultiSelect")) {
/* 197 */       enableChkBoxes(true);
/* 198 */       enableAllWidgets(false);
/*     */     }
/*     */     else {
/* 201 */       enableAllWidgets(true);
/* 202 */       enableChkBoxes(false);
/*     */     }
/*     */ 
/* 205 */     getShell().setText("Front Attributes");
/*     */ 
/* 207 */     return rt;
/*     */   }
/*     */ 
/*     */   private void enableChkBoxes(boolean flag)
/*     */   {
/* 216 */     if (!flag) {
/* 217 */       setAllChkBoxes();
/*     */     }
/* 219 */     for (LineAttrDlg.ChkBox chk : LineAttrDlg.ChkBox.values()) {
/* 220 */       this.chkBox[chk.ordinal()].setVisible(flag);
/*     */     }
/*     */ 
/* 223 */     this.chkBox[LineAttrDlg.ChkBox.LABEL.ordinal()].setVisible(false);
/*     */   }
/*     */ 
/*     */   private void enableAllWidgets(boolean flag)
/*     */   {
/* 233 */     this.colorLbl.setEnabled(flag);
/*     */ 
/* 235 */     this.patternSizeLbl.setEnabled(flag);
/*     */ 
/* 237 */     this.widthLbl.setEnabled(flag);
/* 238 */     this.widthSpinnerSlider.setEnabled(flag);
/*     */ 
/* 240 */     this.smoothLbl.setEnabled(flag);
/* 241 */     this.smoothLvlCbo.setEnabled(flag);
/*     */ 
/* 243 */     this.closedBtn.setEnabled(false);
/* 244 */     this.filledBtn.setEnabled(false);
/*     */ 
/* 246 */     this.fillPatternLbl.setEnabled(false);
/* 247 */     this.fillPatternCbo.setEnabled(false);
/*     */ 
/* 249 */     this.labelChkBox.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   private void setAllChkBoxes()
/*     */   {
/* 258 */     for (LineAttrDlg.ChkBox chk : LineAttrDlg.ChkBox.values())
/* 259 */       this.chkBox[chk.ordinal()].setSelection(true);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.FrontAttrDlg
 * JD-Core Version:    0.6.2
 */