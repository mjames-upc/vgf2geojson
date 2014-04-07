/*     */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class LayeringNameDialog extends ProductDialog
/*     */ {
/*  39 */   private Text nameText = null;
/*  40 */   private ProductManageDialog layeringDlg = null;
/*     */ 
/*     */   public LayeringNameDialog(Shell parentShell, ProductManageDialog dlg)
/*     */   {
/*  48 */     super(parentShell);
/*     */ 
/*  50 */     this.layeringDlg = dlg;
/*     */   }
/*     */ 
/*     */   public void setTitle()
/*     */   {
/*  58 */     this.shell.setText("Layer Name");
/*     */   }
/*     */ 
/*     */   public void setLayout()
/*     */   {
/*  66 */     GridLayout mainLayout = new GridLayout(1, true);
/*  67 */     mainLayout.marginHeight = 1;
/*  68 */     mainLayout.marginWidth = 1;
/*  69 */     this.shell.setLayout(mainLayout);
/*     */   }
/*     */ 
/*     */   public void setDefaultLocation(Shell parent)
/*     */   {
/*  80 */     if (this.shellLocation == null) {
/*  81 */       Point pt = parent.getLocation();
/*  82 */       this.shell.setLocation(pt.x + 400, pt.y + 380);
/*     */     } else {
/*  84 */       this.shell.setLocation(this.shellLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void initializeComponents()
/*     */   {
/*  94 */     Composite top = new Composite(this.shell, 0);
/*  95 */     GridLayout gl = new GridLayout(1, false);
/*  96 */     GridData gd = new GridData(4, -1, true, false);
/*  97 */     top.setLayoutData(gd);
/*  98 */     top.setLayout(gl);
/*     */ 
/* 100 */     this.nameText = new Text(top, 2052);
/* 101 */     this.nameText.setLayoutData(new GridData(100, 20));
/* 102 */     this.nameText.setEditable(true);
/* 103 */     this.nameText.setText(this.layeringDlg.getActiveLayer().getName());
/*     */ 
/* 105 */     Composite centeredComp = new Composite(this.shell, 0);
/* 106 */     GridLayout gl2 = new GridLayout(2, true);
/* 107 */     centeredComp.setLayout(gl2);
/* 108 */     centeredComp.setLayoutData(gd);
/*     */ 
/* 110 */     Button acceptBtn = new Button(centeredComp, 0);
/* 111 */     acceptBtn.setText("Accept");
/* 112 */     acceptBtn.setLayoutData(gd);
/* 113 */     acceptBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 115 */         LayeringNameDialog.this.updateName(LayeringNameDialog.this.nameText.getText());
/* 116 */         LayeringNameDialog.this.close();
/*     */       }
/*     */     });
/* 120 */     Button cancelBtn = new Button(centeredComp, 0);
/* 121 */     cancelBtn.setText("  Close ");
/* 122 */     cancelBtn.setLayoutData(gd);
/* 123 */     cancelBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 125 */         LayeringNameDialog.this.layeringDlg.setopenLayerNameDlg(false);
/* 126 */         LayeringNameDialog.this.close();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void updateName(String txt)
/*     */   {
/* 139 */     if (this.layeringDlg != null)
/* 140 */       this.layeringDlg.updateActiveLayerName(txt);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.LayeringNameDialog
 * JD-Core Version:    0.6.2
 */