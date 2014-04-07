/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import org.eclipse.jface.dialogs.Dialog;
/*     */ import org.eclipse.swt.events.DisposeEvent;
/*     */ import org.eclipse.swt.events.DisposeListener;
/*     */ import org.eclipse.swt.graphics.Font;
/*     */ import org.eclipse.swt.graphics.FontMetrics;
/*     */ import org.eclipse.swt.graphics.GC;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class WatchWCLDlg extends Dialog
/*     */ {
/*  47 */   private Composite top = null;
/*     */ 
/*  49 */   private String fileName = "WCL_report";
/*     */   private String wclText;
/*     */   private String outputPath;
/*     */   private String dataURI;
/*  57 */   private final int NUM_LINES = 25;
/*     */ 
/*  59 */   private final int NUM_COLUMNS = 68;
/*     */ 
/*     */   protected WatchWCLDlg(Shell parentShell)
/*     */   {
/*  65 */     super(parentShell);
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/*  76 */     getShell().setText("Save WCL");
/*  77 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/*  82 */     GridLayout mainLayout = new GridLayout(1, true);
/*  83 */     mainLayout.marginHeight = 3;
/*  84 */     mainLayout.marginWidth = 3;
/*  85 */     this.top.setLayout(mainLayout);
/*     */ 
/*  90 */     Text messageBox = new Text(this.top, 2570);
/*     */ 
/*  92 */     messageBox.setFont(new Font(messageBox.getDisplay(), "Courier", 12, 
/*  93 */       0));
/*  94 */     GridData gd = new GridData(768);
/*     */ 
/*  98 */     gd.heightHint = (25 * messageBox.getLineHeight());
/*  99 */     GC gc = new GC(messageBox);
/* 100 */     FontMetrics fm = gc.getFontMetrics();
/* 101 */     gd.widthHint = (68 * fm.getAverageCharWidth());
/*     */ 
/* 103 */     messageBox.setLayoutData(gd);
/* 104 */     messageBox.setText(this.wclText);
/*     */ 
/* 107 */     messageBox.addDisposeListener(new DisposeListener()
/*     */     {
/*     */       public void widgetDisposed(DisposeEvent e)
/*     */       {
/* 111 */         Text w = (Text)e.widget;
/* 112 */         w.getFont().dispose();
/*     */       }
/*     */     });
/* 117 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void okPressed()
/*     */   {
/*     */     try
/*     */     {
/* 136 */       StorageUtils.storeDerivedProduct(this.dataURI, this.fileName, "TEXT", this.wclText);
/*     */     } catch (PgenStorageException e) {
/* 138 */       StorageUtils.showError(e);
/* 139 */       return;
/*     */     }
/*     */ 
/* 142 */     super.okPressed();
/*     */   }
/*     */ 
/*     */   public void setMessage(String wcl)
/*     */   {
/* 152 */     this.wclText = wcl;
/*     */   }
/*     */ 
/*     */   public void setWCLFileNmae(String file)
/*     */   {
/* 162 */     this.fileName = file;
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 171 */     if (getShell() == null) {
/* 172 */       create();
/*     */     }
/*     */ 
/* 175 */     getButton(0).setText("Save");
/* 176 */     getButtonBar().pack();
/* 177 */     return super.open();
/*     */   }
/*     */ 
/*     */   public void setOutputPath(String outputPath)
/*     */   {
/* 182 */     this.outputPath = outputPath;
/*     */   }
/*     */ 
/*     */   public String getOutputPath() {
/* 186 */     return this.outputPath;
/*     */   }
/*     */ 
/*     */   public String getDataURI() {
/* 190 */     return this.dataURI;
/*     */   }
/*     */ 
/*     */   public void setDataURI(String dataURI) {
/* 194 */     this.dataURI = dataURI;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchWCLDlg
 * JD-Core Version:    0.6.2
 */