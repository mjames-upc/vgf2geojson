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
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class LabeledSymbolAttrDlg extends SymbolAttrDlg
/*     */ {
/*  43 */   private static LabeledSymbolAttrDlg INSTANCE = null;
/*     */   protected Button labelChkBox;
/*     */   private Button labelColorChkBox;
/*     */   private boolean lastLabelStatus;
/*     */   private boolean lastUseColorStatus;
/*     */ 
/*     */   protected LabeledSymbolAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  58 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static SymbolAttrDlg getInstance(Shell parShell)
/*     */   {
/*  71 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/*  75 */         INSTANCE = new LabeledSymbolAttrDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/*  79 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  84 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   protected void initializeComponents()
/*     */   {
/*  93 */     super.initializeComponents();
/*     */ 
/*  95 */     Composite inCmp = new Composite(this.top, 0);
/*  96 */     inCmp.setLayout(getGridLayout(3, false, 0, 0, 0, 0));
/*     */ 
/*  98 */     this.chkBox[SymbolAttrDlg.ChkBox.LABEL.ordinal()] = new Button(inCmp, 32);
/*  99 */     this.chkBox[SymbolAttrDlg.ChkBox.LABEL.ordinal()].setLayoutData(new GridData(16, 28));
/* 100 */     this.chkBox[SymbolAttrDlg.ChkBox.LABEL.ordinal()].addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 104 */         Button btn = (Button)e.widget;
/* 105 */         if (btn.getSelection()) {
/* 106 */           LabeledSymbolAttrDlg.this.labelChkBox.setEnabled(true);
/*     */         }
/*     */         else
/* 109 */           LabeledSymbolAttrDlg.this.labelChkBox.setEnabled(false);
/*     */       }
/*     */     });
/* 115 */     this.chkBox[SymbolAttrDlg.ChkBox.LABEL.ordinal()].setVisible(false);
/*     */ 
/* 117 */     this.labelChkBox = new Button(inCmp, 32);
/* 118 */     this.labelChkBox.setText("Label");
/*     */ 
/* 120 */     this.labelColorChkBox = new Button(inCmp, 32);
/* 121 */     this.labelColorChkBox.setText("Use Symbol Color");
/*     */ 
/* 123 */     this.labelChkBox.addListener(4, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/* 128 */         if (((Button)event.widget).getSelection()) {
/* 129 */           LabeledSymbolAttrDlg.this.labelColorChkBox.setEnabled(true);
/*     */         }
/*     */         else {
/* 132 */           LabeledSymbolAttrDlg.this.labelColorChkBox.setEnabled(false);
/*     */         }
/*     */ 
/* 136 */         LabeledSymbolAttrDlg.this.lastLabelStatus = ((Button)event.widget).getSelection();
/*     */       }
/*     */     });
/* 140 */     this.labelColorChkBox.addListener(4, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/* 144 */         LabeledSymbolAttrDlg.this.lastUseColorStatus = ((Button)event.widget).getSelection();
/*     */       }
/*     */     });
/* 149 */     if ((de != null) && (de.getPgenType().equalsIgnoreCase(this.pgenType))) {
/* 150 */       this.labelChkBox.setSelection(this.lastLabelStatus);
/* 151 */       this.labelColorChkBox.setSelection(this.lastUseColorStatus);
/*     */     }
/*     */     else {
/* 154 */       this.labelChkBox.setSelection(false);
/* 155 */       this.labelColorChkBox.setSelection(false);
/* 156 */       this.lastLabelStatus = false;
/* 157 */       this.lastUseColorStatus = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean labelEnabled()
/*     */   {
/* 167 */     return this.labelChkBox.getSelection();
/*     */   }
/*     */ 
/*     */   public boolean useSymbolColor()
/*     */   {
/* 175 */     return this.labelColorChkBox.getSelection();
/*     */   }
/*     */ 
/*     */   public void setLabelChkBox(boolean enabled)
/*     */   {
/* 183 */     this.labelChkBox.setEnabled(enabled);
/*     */ 
/* 185 */     if ((!this.labelChkBox.isEnabled()) || (!this.labelChkBox.getSelection()))
/* 186 */       this.labelColorChkBox.setEnabled(false);
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 194 */     create();
/*     */ 
/* 198 */     if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 198 */       .equalsIgnoreCase("MultiSelect")) {
/* 199 */       enableChkBoxes(true);
/* 200 */       enableAllWidgets(false);
/*     */     }
/*     */     else {
/* 203 */       enableChkBoxes(false);
/*     */     }
/*     */ 
/* 206 */     int rt = super.open();
/* 207 */     return rt;
/*     */   }
/*     */ 
/*     */   private void enableChkBoxes(boolean flag)
/*     */   {
/* 216 */     if (!flag) {
/* 217 */       setAllChkBoxes();
/*     */     }
/* 219 */     for (SymbolAttrDlg.ChkBox chk : SymbolAttrDlg.ChkBox.values()) {
/* 220 */       this.chkBox[chk.ordinal()].setVisible(flag);
/*     */     }
/*     */ 
/* 223 */     this.chkBox[SymbolAttrDlg.ChkBox.LAT.ordinal()].setVisible(false);
/* 224 */     this.chkBox[SymbolAttrDlg.ChkBox.LON.ordinal()].setVisible(false);
/* 225 */     this.chkBox[SymbolAttrDlg.ChkBox.LABEL.ordinal()].setVisible(false);
/*     */   }
/*     */ 
/*     */   private void enableAllWidgets(boolean flag)
/*     */   {
/* 235 */     this.colorLbl.setEnabled(flag);
/*     */ 
/* 237 */     this.clearLbl.setEnabled(flag);
/* 238 */     this.clearBtn1.setEnabled(flag);
/* 239 */     this.clearBtn2.setEnabled(flag);
/*     */ 
/* 241 */     this.widthLbl.setEnabled(flag);
/* 242 */     this.widthSpinnerSlider.setEnabled(flag);
/*     */ 
/* 244 */     this.sizeLbl.setEnabled(flag);
/* 245 */     this.sizeSpinnerSlider.setEnabled(flag);
/*     */ 
/* 247 */     this.latitudeLabel.setEnabled(flag);
/* 248 */     this.latitudeText.setEnabled(flag);
/*     */ 
/* 250 */     this.longitudeLabel.setEnabled(flag);
/* 251 */     this.longitudeText.setEnabled(flag);
/*     */ 
/* 253 */     this.labelChkBox.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   private void setAllChkBoxes()
/*     */   {
/* 262 */     for (SymbolAttrDlg.ChkBox chk : SymbolAttrDlg.ChkBox.values())
/* 263 */       this.chkBox[chk.ordinal()].setSelection(true);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.LabeledSymbolAttrDlg
 * JD-Core Version:    0.6.2
 */