/*     */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.FileDialog;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class ProductFileNameDialog extends ProductDialog
/*     */ {
/*     */   private static final int TEXT_BOX_LENGTH = 300;
/*  44 */   private ProductManageDialog prdManageDlg = null;
/*     */ 
/*  47 */   private Text outputFileTxt = null;
/*     */ 
/*  50 */   private String initialOutput = null;
/*     */ 
/*     */   public ProductFileNameDialog(Shell parentShell, ProductManageDialog dlg)
/*     */   {
/*  57 */     super(parentShell);
/*     */ 
/*  59 */     this.prdManageDlg = dlg;
/*     */   }
/*     */ 
/*     */   public void setTitle()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setLayout()
/*     */   {
/*  86 */     GridLayout mainLayout = new GridLayout(1, true);
/*  87 */     mainLayout.marginHeight = 1;
/*  88 */     mainLayout.marginWidth = 1;
/*  89 */     this.shell.setLayout(mainLayout);
/*     */   }
/*     */ 
/*     */   public void setDefaultLocation(Shell parent)
/*     */   {
/*  99 */     if (this.shellLocation == null) {
/* 100 */       Point pt = parent.getLocation();
/* 101 */       this.shell.setLocation(pt.x + 475, pt.y + 146);
/*     */     } else {
/* 103 */       this.shell.setLocation(this.shellLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void initializeComponents()
/*     */   {
/* 118 */     Composite infileComp = new Composite(this.shell, 0);
/* 119 */     GridLayout gl0 = new GridLayout(3, false);
/* 120 */     gl0.marginWidth = 3;
/* 121 */     infileComp.setLayout(gl0);
/*     */ 
/* 147 */     Composite outfileComp = new Composite(this.shell, 0);
/* 148 */     outfileComp.setLayout(gl0);
/*     */ 
/* 150 */     Label outputLbl = new Label(outfileComp, 16384);
/* 151 */     outputLbl.setText("Output:");
/*     */ 
/* 153 */     this.outputFileTxt = new Text(outfileComp, 2052);
/* 154 */     this.outputFileTxt.setLayoutData(new GridData(300, 15));
/* 155 */     this.outputFileTxt.setEditable(true);
/* 156 */     if (this.initialOutput != null) {
/* 157 */       this.outputFileTxt.setText(this.initialOutput);
/*     */     }
/*     */     else {
/* 160 */       this.outputFileTxt.setText("");
/*     */     }
/*     */ 
/* 163 */     Button browseBtn = new Button(outfileComp, 8);
/* 164 */     browseBtn.setText("Browse");
/*     */ 
/* 166 */     browseBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 168 */         ProductFileNameDialog.this.createFileText(ProductFileNameDialog.this.outputFileTxt, ProductFileNameDialog.this.initialOutput);
/*     */       }
/*     */     });
/* 173 */     Composite centeredComp = new Composite(this.shell, 0);
/* 174 */     GridLayout gl2 = new GridLayout(2, true);
/* 175 */     centeredComp.setLayout(gl2);
/* 176 */     GridData gd = new GridData(4, -1, true, false);
/* 177 */     centeredComp.setLayoutData(gd);
/*     */ 
/* 179 */     Button acceptBtn = new Button(centeredComp, 0);
/* 180 */     acceptBtn.setText("Accept");
/* 181 */     acceptBtn.setLayoutData(gd);
/* 182 */     acceptBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent event)
/*     */       {
/* 188 */         ProductFileNameDialog.this.close();
/*     */       }
/*     */     });
/* 192 */     Button cancelBtn = new Button(centeredComp, 0);
/* 193 */     cancelBtn.setText("Cancel");
/* 194 */     cancelBtn.setLayoutData(gd);
/* 195 */     cancelBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 197 */         ProductFileNameDialog.this.close();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createFileText(Text txt, String initialFile)
/*     */   {
/* 212 */     String[] filterNames = { "*.xml", "All Files (*)" };
/* 213 */     String[] filterExtensions = { "*.xml", "*" };
/*     */ 
/* 215 */     String filterPath = PgenUtil.getWorkingDirectory();
/* 216 */     String defaultFile = new String("default.xml");
/*     */ 
/* 218 */     if (initialFile != null) {
/* 219 */       int index = initialFile.lastIndexOf('/');
/* 220 */       if (index >= 0) {
/* 221 */         defaultFile = initialFile.substring(index + 1, initialFile.length());
/* 222 */         filterPath = initialFile.substring(0, index);
/*     */       }
/*     */       else {
/* 225 */         defaultFile = new String(initialFile);
/*     */       }
/*     */     }
/*     */ 
/* 229 */     String selectedFile = selectFile(this.shell, 8192, filterNames, 
/* 230 */       filterExtensions, filterPath, defaultFile, true);
/* 231 */     if (selectedFile != null)
/* 232 */       txt.setText(selectedFile);
/*     */   }
/*     */ 
/*     */   private String selectFile(Shell sh, int mode, String[] nameFilter, String[] extensionFilter, String pathFilter, String defaultFile, boolean overWrite)
/*     */   {
/* 253 */     FileDialog dialog = new FileDialog(sh, mode);
/* 254 */     dialog.setFilterNames(nameFilter);
/* 255 */     dialog.setFilterExtensions(extensionFilter);
/* 256 */     dialog.setFilterPath(pathFilter);
/* 257 */     if (defaultFile != null) {
/* 258 */       dialog.setFileName(defaultFile);
/*     */     }
/* 260 */     dialog.setOverwrite(overWrite);
/*     */ 
/* 262 */     return dialog.open();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.ProductFileNameDialog
 * JD-Core Version:    0.6.2
 */