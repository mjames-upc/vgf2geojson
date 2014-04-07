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
/*     */ public class LayeringLpfFileDialog extends ProductDialog
/*     */ {
/*     */   private static final int TEXT_BOX_LENGTH = 300;
/*  44 */   private ProductManageDialog prdManageDlg = null;
/*     */ 
/*  46 */   private Text inputFileTxt = null;
/*  47 */   private Text outputFileTxt = null;
/*     */ 
/*  49 */   private String initialInput = null;
/*  50 */   private String initialOutput = null;
/*     */ 
/*     */   public LayeringLpfFileDialog(Shell parentShell, ProductManageDialog dlg)
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
/* 119 */     Composite infileComp = new Composite(this.shell, 0);
/* 120 */     GridLayout gl0 = new GridLayout(3, false);
/* 121 */     gl0.marginWidth = 3;
/* 122 */     infileComp.setLayout(gl0);
/*     */ 
/* 124 */     Label inputLbl = new Label(infileComp, 16384);
/* 125 */     inputLbl.setText("Input:");
/*     */ 
/* 127 */     this.inputFileTxt = new Text(infileComp, 2052);
/* 128 */     this.inputFileTxt.setLayoutData(new GridData(300, 15));
/* 129 */     this.inputFileTxt.setEditable(true);
/* 130 */     if (this.initialInput != null) {
/* 131 */       this.inputFileTxt.setText(this.initialInput);
/*     */     }
/*     */     else {
/* 134 */       this.inputFileTxt.setText("");
/*     */     }
/*     */ 
/* 137 */     Button nameBtn = new Button(infileComp, 8);
/* 138 */     nameBtn.setText("Browse");
/*     */ 
/* 140 */     nameBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 142 */         LayeringLpfFileDialog.this.createFileText(LayeringLpfFileDialog.this.inputFileTxt, LayeringLpfFileDialog.this.initialInput);
/*     */       }
/*     */     });
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
/* 168 */         LayeringLpfFileDialog.this.createFileText(LayeringLpfFileDialog.this.outputFileTxt, LayeringLpfFileDialog.this.initialOutput);
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
/* 187 */         LayeringLpfFileDialog.this.close();
/*     */       }
/*     */     });
/* 191 */     Button cancelBtn = new Button(centeredComp, 0);
/* 192 */     cancelBtn.setText("Cancel");
/* 193 */     cancelBtn.setLayoutData(gd);
/* 194 */     cancelBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 196 */         LayeringLpfFileDialog.this.close();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createFileText(Text txt, String initialFile)
/*     */   {
/* 211 */     String[] filterNames = { "*.xml", "All Files (*)" };
/* 212 */     String[] filterExtensions = { "*.xml", "*" };
/*     */ 
/* 214 */     String filterPath = PgenUtil.getWorkingDirectory();
/* 215 */     String defaultFile = new String("default.xml");
/*     */ 
/* 217 */     if (initialFile != null) {
/* 218 */       int index = initialFile.lastIndexOf('/');
/* 219 */       if (index >= 0) {
/* 220 */         defaultFile = initialFile.substring(index + 1, initialFile.length());
/* 221 */         filterPath = initialFile.substring(0, index);
/*     */       }
/*     */       else {
/* 224 */         defaultFile = new String(initialFile);
/*     */       }
/*     */     }
/*     */ 
/* 228 */     String selectedFile = selectFile(this.shell, 8192, filterNames, 
/* 229 */       filterExtensions, filterPath, defaultFile, true);
/* 230 */     if (selectedFile != null)
/* 231 */       txt.setText(selectedFile);
/*     */   }
/*     */ 
/*     */   private String selectFile(Shell sh, int mode, String[] nameFilter, String[] extensionFilter, String pathFilter, String defaultFile, boolean overWrite)
/*     */   {
/* 252 */     FileDialog dialog = new FileDialog(sh, mode);
/* 253 */     dialog.setFilterNames(nameFilter);
/* 254 */     dialog.setFilterExtensions(extensionFilter);
/* 255 */     dialog.setFilterPath(pathFilter);
/* 256 */     if (defaultFile != null) {
/* 257 */       dialog.setFileName(defaultFile);
/*     */     }
/* 259 */     dialog.setOverwrite(overWrite);
/*     */ 
/* 261 */     return dialog.open();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.LayeringLpfFileDialog
 * JD-Core Version:    0.6.2
 */