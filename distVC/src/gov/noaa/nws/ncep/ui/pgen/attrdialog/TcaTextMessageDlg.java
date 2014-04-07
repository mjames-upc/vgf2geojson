/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import org.eclipse.jface.dialogs.IDialogConstants;
/*     */ import org.eclipse.swt.events.DisposeEvent;
/*     */ import org.eclipse.swt.events.DisposeListener;
/*     */ import org.eclipse.swt.graphics.Font;
/*     */ import org.eclipse.swt.graphics.FontMetrics;
/*     */ import org.eclipse.swt.graphics.GC;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class TcaTextMessageDlg extends CaveJFACEDialog
/*     */ {
/*  37 */   private Composite top = null;
/*     */ 
/*  39 */   private final int SAVE_ID = 8613;
/*     */ 
/*  41 */   private final String SAVE_LABEL = "Save";
/*     */ 
/*  43 */   private final String PRODUCT_TYPE = "TEXT";
/*     */   private String filename;
/*     */   private String message;
/*     */   private String dataURI;
/*  51 */   private final int NUM_LINES = 25;
/*     */ 
/*  53 */   private final int NUM_COLUMNS = 80;
/*     */ 
/*     */   protected TcaTextMessageDlg(Shell parentShell)
/*     */   {
/*  59 */     super(parentShell);
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/*  68 */     createButton(parent, 8613, "Save", true);
/*  69 */     createButton(parent, 1, 
/*  70 */       IDialogConstants.CANCEL_LABEL, true);
/*     */   }
/*     */ 
/*     */   protected void configureShell(Shell shell)
/*     */   {
/*  83 */     super.configureShell(shell);
/*  84 */     shell.setText("TCA Text Message");
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/*  93 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/*  98 */     GridLayout mainLayout = new GridLayout(1, true);
/*  99 */     mainLayout.marginHeight = 3;
/* 100 */     mainLayout.marginWidth = 3;
/* 101 */     this.top.setLayout(mainLayout);
/*     */ 
/* 106 */     Text messageBox = new Text(this.top, 2570);
/*     */ 
/* 108 */     messageBox.setFont(new Font(messageBox.getDisplay(), "Courier", 12, 
/* 109 */       0));
/* 110 */     GridData gd = new GridData(768);
/*     */ 
/* 114 */     gd.heightHint = (25 * messageBox.getLineHeight());
/* 115 */     GC gc = new GC(messageBox);
/* 116 */     FontMetrics fm = gc.getFontMetrics();
/* 117 */     gd.widthHint = (80 * fm.getAverageCharWidth());
/*     */ 
/* 119 */     messageBox.setLayoutData(gd);
/* 120 */     messageBox.setText(this.message);
/*     */ 
/* 123 */     messageBox.addDisposeListener(new DisposeListener()
/*     */     {
/*     */       public void widgetDisposed(DisposeEvent e)
/*     */       {
/* 127 */         Text w = (Text)e.widget;
/* 128 */         w.getFont().dispose();
/*     */       }
/*     */     });
/* 137 */     Label sep = new Label(this.top, 258);
/* 138 */     sep.setLayoutData(new GridData(768));
/*     */ 
/* 143 */     Label filelabel = new Label(this.top, 0);
/* 144 */     filelabel.setText("Text File Name:  " + this.filename);
/*     */ 
/* 149 */     Label sep2 = new Label(this.top, 258);
/* 150 */     sep2.setLayoutData(new GridData(768));
/*     */ 
/* 152 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void buttonPressed(int buttonId)
/*     */   {
/* 167 */     if (buttonId == 8613)
/*     */     {
/*     */       try
/*     */       {
/* 171 */         StorageUtils.storeDerivedProduct(this.dataURI, this.filename, 
/* 172 */           "TEXT", this.message);
/*     */       } catch (PgenStorageException e) {
/* 174 */         StorageUtils.showError(e);
/*     */       }
/*     */ 
/* 177 */       super.buttonPressed(0);
/*     */     }
/*     */     else {
/* 180 */       super.buttonPressed(buttonId);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setOutputFilename(String filename)
/*     */   {
/* 190 */     this.filename = filename;
/*     */   }
/*     */ 
/*     */   public void setMessage(String vtec)
/*     */   {
/* 199 */     this.message = vtec;
/*     */   }
/*     */ 
/*     */   public void setDataURI(String dataURI)
/*     */   {
/* 204 */     this.dataURI = dataURI;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.TcaTextMessageDlg
 * JD-Core Version:    0.6.2
 */