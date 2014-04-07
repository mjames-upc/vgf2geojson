/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import org.eclipse.jface.action.ContributionItem;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class PgenFileNameDisplay extends ContributionItem
/*     */ {
/*  37 */   private static PgenFileNameDisplay instance = null;
/*     */ 
/*  39 */   private Shell shell = null;
/*  40 */   private Label fileNameLabel = null;
/*  41 */   private String fileName = "";
/*     */ 
/*     */   public static PgenFileNameDisplay getInstance()
/*     */   {
/*  49 */     if (instance == null) {
/*  50 */       instance = new PgenFileNameDisplay();
/*     */     }
/*  52 */     return instance;
/*     */   }
/*     */ 
/*     */   public void fill(Composite parent)
/*     */   {
/*  66 */     this.shell = parent.getShell();
/*     */ 
/*  68 */     Composite fileNameComposite = new Composite(parent, 0);
/*  69 */     fileNameComposite.setLayout(new GridLayout(1, false));
/*     */ 
/*  71 */     this.fileNameLabel = new Label(fileNameComposite, 0);
/*     */ 
/*  73 */     this.fileNameLabel.setLayoutData(new GridData(4, 16777216, true, true));
/*  74 */     this.fileNameLabel.setText("");
/*     */ 
/*  76 */     setVisible(true);
/*     */   }
/*     */ 
/*     */   public void update()
/*     */   {
/*  82 */     if ((this.fileName != null) && (this.fileNameLabel != null)) {
/*  83 */       this.fileNameLabel.setText(this.fileName);
/*  84 */       this.shell.layout(true, true);
/*  85 */       this.fileNameLabel.pack(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/*  91 */     super.dispose();
/*  92 */     instance = null;
/*     */   }
/*     */ 
/*     */   public String getFileName()
/*     */   {
/*  97 */     return this.fileName;
/*     */   }
/*     */ 
/*     */   public void setFileName(String fileName) {
/* 101 */     this.fileName = fileName;
/* 102 */     update();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.PgenFileNameDisplay
 * JD-Core Version:    0.6.2
 */