/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenIncDecTool;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenIncDecTool.PgenIncDecHandler;
/*     */ import org.eclipse.swt.events.VerifyEvent;
/*     */ import org.eclipse.swt.events.VerifyListener;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Spinner;
/*     */ 
/*     */ public class IncDecDlg extends AttrDlg
/*     */ {
/*  47 */   static IncDecDlg INSTANCE = null;
/*     */ 
/*  49 */   private Composite top = null;
/*     */ 
/*  51 */   private Spinner interval = null;
/*  52 */   private int intervalValue = 1;
/*     */   private PgenIncDecTool tool;
/*     */ 
/*     */   private IncDecDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  63 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static IncDecDlg getInstance(Shell parShell)
/*     */   {
/*  76 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/*  80 */         INSTANCE = new IncDecDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/*  84 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  89 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 102 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 105 */     GridLayout mainLayout = new GridLayout();
/* 106 */     mainLayout.marginTop = 5;
/* 107 */     mainLayout.marginWidth = 1;
/* 108 */     this.top.setLayout(mainLayout);
/*     */ 
/* 111 */     initializeComponents();
/*     */ 
/* 113 */     return this.top;
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 122 */     super.createButtonsForButtonBar(parent);
/*     */ 
/* 124 */     getButton(1).setEnabled(true);
/* 125 */     getButton(0).setEnabled(true);
/* 126 */     getButton(1).setText("Deselect");
/* 127 */     getButton(0).setText("Exit");
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 136 */     getShell().setText("Inc/Dec Editor");
/*     */ 
/* 141 */     this.interval = new Spinner(this.top, 16779264);
/* 142 */     this.interval.setMinimum(-10000);
/* 143 */     this.interval.setMaximum(10000);
/* 144 */     this.interval.setSelection(this.intervalValue);
/*     */ 
/* 146 */     this.interval.setLayoutData(new GridData(16777216, 16777216, true, true, 1, 1));
/*     */   }
/*     */ 
/*     */   protected void buttonPressed(int buttonId)
/*     */   {
/* 158 */     ((PgenIncDecTool.PgenIncDecHandler)this.tool.getMouseHandler()).cleanup();
/*     */ 
/* 160 */     if (buttonId == 0) {
/* 161 */       PgenUtil.setSelectingMode();
/* 162 */       close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute attr)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int getInterval()
/*     */   {
/* 178 */     return this.interval.getSelection();
/*     */   }
/*     */ 
/*     */   public boolean close()
/*     */   {
/* 183 */     if (getShell() != null) this.intervalValue = this.interval.getSelection();
/* 184 */     return super.close();
/*     */   }
/*     */ 
/*     */   public void setTool(PgenIncDecTool tool) {
/* 188 */     this.tool = tool;
/*     */   }
/*     */ 
/*     */   public PgenIncDecTool getTool() {
/* 192 */     return this.tool;
/*     */   }
/*     */ 
/*     */   class DigitVerifyListener
/*     */     implements VerifyListener
/*     */   {
/*     */     DigitVerifyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void verifyText(VerifyEvent e)
/*     */     {
/* 206 */       char BACKSPACE = '\b';
/* 207 */       char DELETE = '';
/*     */ 
/* 209 */       if ((Character.isDigit(e.character)) || 
/* 210 */         (e.character == '\b') || (e.character == '')) { e.doit = true;
/*     */       } else {
/* 212 */         e.doit = false;
/* 213 */         Display.getCurrent().beep();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.IncDecDlg
 * JD-Core Version:    0.6.2
 */