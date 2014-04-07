/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
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
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class WatchWCCDlg extends Dialog
/*     */ {
/*  48 */   private Composite top = null;
/*     */ 
/*  50 */   private final String filename = "KNCFNIMNAT";
/*     */   private String wccText;
/*     */   private String launchText;
/*     */   private String outputPath;
/*     */   private String dataURI;
/*  60 */   private final int NUM_LINES = 25;
/*     */ 
/*  62 */   private final int NUM_COLUMNS = 68;
/*     */ 
/*     */   protected WatchWCCDlg(Shell parentShell)
/*     */   {
/*  68 */     super(parentShell);
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/*  79 */     getShell().setText("Save WCC");
/*  80 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/*  85 */     GridLayout mainLayout = new GridLayout(1, true);
/*  86 */     mainLayout.marginHeight = 3;
/*  87 */     mainLayout.marginWidth = 3;
/*  88 */     this.top.setLayout(mainLayout);
/*     */ 
/*  93 */     Text messageBox = new Text(this.top, 2570);
/*     */ 
/*  95 */     messageBox.setFont(new Font(messageBox.getDisplay(), "Courier", 12, 
/*  96 */       0));
/*  97 */     GridData gd = new GridData(768);
/*     */ 
/* 101 */     gd.heightHint = (25 * messageBox.getLineHeight());
/* 102 */     GC gc = new GC(messageBox);
/* 103 */     FontMetrics fm = gc.getFontMetrics();
/* 104 */     gd.widthHint = (68 * fm.getAverageCharWidth());
/*     */ 
/* 106 */     messageBox.setLayoutData(gd);
/* 107 */     messageBox.setText(this.wccText);
/*     */ 
/* 110 */     messageBox.addDisposeListener(new DisposeListener()
/*     */     {
/*     */       public void widgetDisposed(DisposeEvent e)
/*     */       {
/* 114 */         Text w = (Text)e.widget;
/* 115 */         w.getFont().dispose();
/*     */       }
/*     */     });
/* 123 */     Label sep = new Label(this.top, 258);
/* 124 */     sep.setLayoutData(new GridData(768));
/*     */ 
/* 129 */     Label filelabel = new Label(this.top, 0);
/* 130 */     filelabel.setText("WCC File Name:  KNCFNIMNAT");
/*     */ 
/* 135 */     Label sep2 = new Label(this.top, 258);
/* 136 */     sep2.setLayoutData(new GridData(768));
/*     */ 
/* 138 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void okPressed()
/*     */   {
/*     */     try
/*     */     {
/* 157 */       StorageUtils.storeDerivedProduct(this.dataURI, "KNCFNIMNAT", "TEXT", this.wccText);
/*     */     } catch (PgenStorageException e) {
/* 159 */       StorageUtils.showError(e);
/* 160 */       return;
/*     */     }
/* 162 */     FileTools.writeFile(this.outputPath + "KNCFNIMNAT" + ".launch", this.launchText);
/*     */ 
/* 164 */     super.okPressed();
/*     */   }
/*     */ 
/*     */   public void setMessage(String wcc)
/*     */   {
/* 174 */     this.wccText = wcc;
/*     */   }
/*     */ 
/*     */   public void setWCCLaunchText(String launchTxt)
/*     */   {
/* 183 */     this.launchText = launchTxt;
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 192 */     if (getShell() == null) {
/* 193 */       create();
/*     */     }
/*     */ 
/* 196 */     getButton(0).setText("Save");
/* 197 */     getButtonBar().pack();
/* 198 */     return super.open();
/*     */   }
/*     */ 
/*     */   public void setOutputPath(String outputPath)
/*     */   {
/* 203 */     this.outputPath = outputPath;
/*     */   }
/*     */ 
/*     */   public String getOutputPath() {
/* 207 */     return this.outputPath;
/*     */   }
/*     */ 
/*     */   public String getDataURI() {
/* 211 */     return this.dataURI;
/*     */   }
/*     */ 
/*     */   public void setDataURI(String dataURI) {
/* 215 */     this.dataURI = dataURI;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchWCCDlg
 * JD-Core Version:    0.6.2
 */