/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.TimeZone;
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
/*     */ public class WatchStatusMsgDlg extends CaveJFACEDialog
/*     */ {
/*     */   private Composite top;
/*     */   private Text productName;
/*     */   private String statusMsg;
/*     */   private WatchStatusDlg wsd;
/*  62 */   private final int NUM_LINES = 25;
/*     */ 
/*  64 */   private final int NUM_COLUMNS = 68;
/*     */ 
/*     */   protected WatchStatusMsgDlg(Shell parentShell, WatchStatusDlg wsd)
/*     */   {
/*  70 */     super(parentShell);
/*  71 */     this.wsd = wsd;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/*  81 */     getShell().setText("Watch Status Save");
/*     */ 
/*  83 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/*  88 */     GridLayout mainLayout = new GridLayout(1, true);
/*  89 */     mainLayout.marginHeight = 3;
/*  90 */     mainLayout.marginWidth = 3;
/*  91 */     this.top.setLayout(mainLayout);
/*     */ 
/*  96 */     Text messageBox = new Text(this.top, 2570);
/*     */ 
/*  98 */     messageBox.setFont(new Font(messageBox.getDisplay(), "Courier", 12, 
/*  99 */       0));
/* 100 */     GridData gd = new GridData(768);
/*     */ 
/* 104 */     gd.heightHint = (25 * messageBox.getLineHeight());
/* 105 */     GC gc = new GC(messageBox);
/* 106 */     FontMetrics fm = gc.getFontMetrics();
/* 107 */     gd.widthHint = (68 * fm.getAverageCharWidth());
/*     */ 
/* 109 */     messageBox.setLayoutData(gd);
/* 110 */     messageBox.setText(this.statusMsg);
/*     */ 
/* 112 */     this.productName = new Text(this.top, 2052);
/* 113 */     GridData gd2 = new GridData(768);
/* 114 */     this.productName.setLayoutData(gd2);
/* 115 */     this.productName.setText(generateName());
/*     */ 
/* 118 */     messageBox.addDisposeListener(new DisposeListener()
/*     */     {
/*     */       public void widgetDisposed(DisposeEvent e)
/*     */       {
/* 122 */         Text w = (Text)e.widget;
/* 123 */         w.getFont().dispose();
/*     */       }
/*     */     });
/* 128 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void okPressed()
/*     */   {
/* 143 */     String watchNumber = String.format("%1$04d", new Object[] { Integer.valueOf(this.wsd.getWatchNumber()) });
/* 144 */     String fname = "WW" + watchNumber + ".xml";
/*     */ 
/* 149 */     String dataURI = this.wsd.getWatchBox().storeProduct(fname);
/* 150 */     if (dataURI != null) {
/*     */       try {
/* 152 */         StorageUtils.storeDerivedProduct(dataURI, 
/* 153 */           this.productName.getText(), "TEXT", this.statusMsg);
/*     */       } catch (PgenStorageException e) {
/* 155 */         StorageUtils.showError(e);
/*     */       }
/*     */     }
/* 158 */     this.wsd.close();
/* 159 */     super.okPressed();
/*     */   }
/*     */ 
/*     */   protected void cancelPressed()
/*     */   {
/* 173 */     this.wsd.getWatchBox().rmLastStatus();
/*     */ 
/* 176 */     super.cancelPressed();
/*     */   }
/*     */ 
/*     */   public void setMessage(String str)
/*     */   {
/* 187 */     this.statusMsg = str;
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 197 */     if (getShell() == null) {
/* 198 */       create();
/*     */     }
/*     */ 
/* 202 */     getButton(0).setText("Save");
/* 203 */     getButtonBar().pack();
/*     */ 
/* 205 */     return super.open();
/*     */   }
/*     */ 
/*     */   private String generateName()
/*     */   {
/* 210 */     SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
/* 211 */     Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 212 */     String sdate = sdf.format(date.getTime());
/* 213 */     String watchNumber = String.format("%1$04d", new Object[] { Integer.valueOf(this.wsd.getWatchNumber()) });
/*     */ 
/* 215 */     StringBuilder dpname = new StringBuilder("WSMenh_");
/* 216 */     dpname.append(watchNumber);
/* 217 */     dpname.append('_');
/* 218 */     dpname.append(sdate);
/* 219 */     dpname.append(".txt");
/* 220 */     return dpname.toString();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchStatusMsgDlg
 * JD-Core Version:    0.6.2
 */