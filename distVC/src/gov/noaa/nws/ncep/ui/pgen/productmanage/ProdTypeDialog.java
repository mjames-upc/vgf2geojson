/*     */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProdType;
/*     */ import org.eclipse.jface.dialogs.Dialog;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class ProdTypeDialog extends Dialog
/*     */ {
/*  44 */   private static String[] typeStr = { "GEMPAK/GIF", "XML", "Text Prod", "CAP", "KML" };
/*     */   private ProductConfigureDialog pcd;
/*     */   private ProdType pt;
/*     */   private Button ptBtn;
/*     */   private Label typeLbl;
/*     */   private Combo typeCbo;
/*     */   private Text nameTxt;
/*     */   private Text ssTxt;
/*     */   private Text outputTxt;
/*     */ 
/*     */   protected ProdTypeDialog(ProductConfigureDialog pcd)
/*     */   {
/*  72 */     super(pcd.shell);
/*  73 */     this.pcd = pcd;
/*  74 */     this.pt = null;
/*  75 */     setShellStyle(96);
/*     */   }
/*     */ 
/*     */   protected ProdTypeDialog(ProductConfigureDialog pcd, ProdType pt, Button ptBtn, Label typeLbl)
/*     */   {
/*  80 */     super(pcd.shell);
/*  81 */     this.pcd = pcd;
/*  82 */     this.pt = pt;
/*  83 */     this.ptBtn = ptBtn;
/*  84 */     this.typeLbl = typeLbl;
/*     */ 
/*  86 */     setShellStyle(96);
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/*  96 */     getShell().setText("Product Type");
/*     */ 
/*  98 */     Composite top = (Composite)super.createDialogArea(parent);
/*     */ 
/* 101 */     GridLayout mainLayout = new GridLayout(2, false);
/* 102 */     mainLayout.marginHeight = 3;
/* 103 */     mainLayout.marginWidth = 3;
/* 104 */     top.setLayout(mainLayout);
/*     */ 
/* 109 */     Label typeLbl = new Label(top, 0);
/* 110 */     typeLbl.setText("Type:");
/* 111 */     this.typeCbo = new Combo(top, 12);
/*     */ 
/* 113 */     this.typeCbo.setItems(typeStr);
/*     */ 
/* 115 */     if (this.pt != null) {
/* 116 */       for (int ii = 0; ii < typeStr.length; ii++) {
/* 117 */         if (this.pt.getType().equalsIgnoreCase(typeStr[ii])) {
/* 118 */           this.typeCbo.select(ii);
/* 119 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 124 */       this.typeCbo.select(2);
/*     */     }
/*     */ 
/* 128 */     Label nameLbl = new Label(top, 0);
/* 129 */     nameLbl.setText("Name:");
/* 130 */     nameLbl.setToolTipText("Product name");
/*     */ 
/* 132 */     this.nameTxt = new Text(top, 2052);
/* 133 */     this.nameTxt.setLayoutData(new GridData(60, 18));
/*     */ 
/* 135 */     if (this.pt != null) {
/* 136 */       this.nameTxt.setText(this.pt.getName());
/*     */     }
/*     */ 
/* 140 */     Label styleSheetLbl = new Label(top, 0);
/* 141 */     styleSheetLbl.setText("Style Sheet File:");
/*     */ 
/* 143 */     String tipText = "File path under NCEP/PGEN/xslt/prod/ in localization.";
/*     */ 
/* 145 */     styleSheetLbl.setToolTipText(tipText);
/*     */ 
/* 147 */     Composite ssComp = new Composite(top, 0);
/* 148 */     GridLayout ssLayout = new GridLayout(2, false);
/* 149 */     ssLayout.marginWidth = 0;
/* 150 */     ssComp.setLayout(ssLayout);
/*     */ 
/* 152 */     this.ssTxt = new Text(ssComp, 2052);
/* 153 */     if (this.pt != null) this.ssTxt.setText(this.pt.getStyleSheetFile());
/*     */ 
/* 156 */     Label outputLbl = new Label(top, 0);
/* 157 */     outputLbl.setText("Output File:");
/* 158 */     outputLbl.setToolTipText("Output file name. The output file will be stored in the 'prod' directory under the current activity");
/*     */ 
/* 160 */     this.outputTxt = new Text(top, 2052);
/* 161 */     if (this.pt != null) this.outputTxt.setText(this.pt.getOutputFile());
/*     */ 
/* 166 */     return top;
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 178 */     if (this.nameTxt.getText().isEmpty()) {
/* 179 */       MessageDialog infoDlg = new MessageDialog(
/* 180 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 181 */         "Error", null, "Please provide a name!", 
/* 182 */         1, new String[] { "OK" }, 0);
/* 183 */       infoDlg.open();
/* 184 */       return;
/*     */     }
/*     */ 
/* 187 */     if (this.pt == null)
/*     */     {
/* 189 */       ProdType ptype = new ProdType();
/* 190 */       ptype.setName(this.nameTxt.getText());
/* 191 */       ptype.setType(this.typeCbo.getText());
/* 192 */       ptype.setStyleSheetFile(this.ssTxt.getText());
/*     */ 
/* 194 */       ptype.setOutputFile(this.outputTxt.getText());
/* 195 */       this.pcd.addProdType(ptype);
/*     */     }
/*     */     else
/*     */     {
/* 199 */       this.pt.setName(this.nameTxt.getText());
/* 200 */       this.pt.setType(this.typeCbo.getText());
/* 201 */       this.pt.setStyleSheetFile(this.ssTxt.getText());
/*     */ 
/* 203 */       this.pt.setOutputFile(this.outputTxt.getText());
/* 204 */       this.ptBtn.setText(this.pt.getName());
/* 205 */       this.typeLbl.setText(this.pt.getType());
/*     */     }
/*     */ 
/* 209 */     super.okPressed();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.ProdTypeDialog
 * JD-Core Version:    0.6.2
 */