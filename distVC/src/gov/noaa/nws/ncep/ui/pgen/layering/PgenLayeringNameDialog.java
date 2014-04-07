/*     */ package gov.noaa.nws.ncep.ui.pgen.layering;
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
/*     */ public class PgenLayeringNameDialog extends PgenLayeringDialog
/*     */ {
/*  39 */   private Text nameText = null;
/*  40 */   private PgenLayeringControlDialog layeringDlg = null;
/*     */ 
/*     */   public PgenLayeringNameDialog(Shell parentShell, PgenLayeringControlDialog dlg)
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
/*  79 */     Point pt = parent.getLocation();
/*  80 */     this.shell.setLocation(pt.x + 400, pt.y + 380);
/*     */   }
/*     */ 
/*     */   public void initializeComponents()
/*     */   {
/*  89 */     Composite top = new Composite(this.shell, 0);
/*  90 */     GridLayout gl = new GridLayout(1, false);
/*  91 */     GridData gd = new GridData(4, -1, true, false);
/*  92 */     top.setLayoutData(gd);
/*  93 */     top.setLayout(gl);
/*     */ 
/*  95 */     this.nameText = new Text(top, 2052);
/*  96 */     this.nameText.setLayoutData(new GridData(95, 20));
/*  97 */     this.nameText.setEditable(true);
/*  98 */     this.nameText.setText(this.layeringDlg.getActiveLayer().getName());
/*     */ 
/* 100 */     Composite centeredComp = new Composite(this.shell, 0);
/* 101 */     GridLayout gl2 = new GridLayout(2, true);
/* 102 */     centeredComp.setLayout(gl2);
/* 103 */     centeredComp.setLayoutData(gd);
/*     */ 
/* 105 */     Button acceptBtn = new Button(centeredComp, 0);
/* 106 */     acceptBtn.setText("Accept");
/* 107 */     acceptBtn.setLayoutData(gd);
/* 108 */     acceptBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 110 */         PgenLayeringNameDialog.this.updateName(PgenLayeringNameDialog.this.nameText.getText());
/* 111 */         PgenLayeringNameDialog.this.shell.dispose();
/*     */       }
/*     */     });
/* 115 */     Button cancelBtn = new Button(centeredComp, 0);
/* 116 */     cancelBtn.setText("  Close ");
/* 117 */     cancelBtn.setLayoutData(gd);
/* 118 */     cancelBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 120 */         PgenLayeringNameDialog.this.shell.dispose();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void updateName(String txt)
/*     */   {
/* 133 */     if (this.layeringDlg != null)
/* 134 */       this.layeringDlg.updateActiveLayerName(txt);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.layering.PgenLayeringNameDialog
 * JD-Core Version:    0.6.2
 */