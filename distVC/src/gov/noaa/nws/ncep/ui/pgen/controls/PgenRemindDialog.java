/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import org.eclipse.jface.dialogs.Dialog;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class PgenRemindDialog extends Dialog
/*     */ {
/*  39 */   private final String dlgTitle = "PGEN Exit Confirmation";
/*     */ 
/*  41 */   private Composite dlgArea = null;
/*     */ 
/*  43 */   private Button yesButton = null;
/*  44 */   private Button noButton = null;
/*     */   private Image image;
/*     */ 
/*     */   public PgenRemindDialog(Shell parShell, Image im)
/*     */   {
/*  52 */     super(parShell);
/*  53 */     setShellStyle(34912);
/*  54 */     this.image = im;
/*     */   }
/*     */ 
/*     */   protected void configureShell(Shell shell)
/*     */   {
/*  65 */     super.configureShell(shell);
/*  66 */     shell.setText("PGEN Exit Confirmation");
/*     */ 
/*  68 */     shell.setLocation(500, 200);
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/*  80 */     this.dlgArea = ((Composite)super.createDialogArea(parent));
/*     */ 
/*  82 */     GridLayout gl = new GridLayout(1, true);
/*  83 */     gl.marginTop = 5;
/*  84 */     gl.marginBottom = 5;
/*  85 */     gl.verticalSpacing = 10;
/*  86 */     this.dlgArea.setLayout(gl);
/*     */ 
/*  91 */     Label info = new Label(this.dlgArea, 0);
/*  92 */     info.setText("There are unsaved changes in this PGEN session.\nDo you want to save them?");
/*  93 */     info.setLayoutData(new GridData());
/*     */ 
/*  98 */     Button pic = new Button(this.dlgArea, 8388608);
/*  99 */     pic.setImage(this.image);
/*     */ 
/* 101 */     return this.dlgArea;
/*     */   }
/*     */ 
/*     */   protected void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 110 */     this.yesButton = createButton(parent, 0, "YES", false);
/* 111 */     this.noButton = createButton(parent, 1, "NO", false);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.PgenRemindDialog
 * JD-Core Version:    0.6.2
 */