/*     */ package gov.noaa.nws.ncep.standalone.vgfConverter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.eclipse.jface.dialogs.IDialogConstants;
/*     */ import org.eclipse.jface.dialogs.TitleAreaDialog;
/*     */ import org.eclipse.jface.resource.JFaceResources;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class VgfConvertDialog extends TitleAreaDialog
/*     */ {
/*     */   private Text text1;
/*     */   private Text text2;
/*     */   private Text text3;
/*     */   private Text text4;
/*     */   private Text text5;
/*  47 */   private String in = "";
/*  48 */   private String out = "";
/*  49 */   private String activity = "";
/*  50 */   private String subActivity = "";
/*  51 */   private String contTbl = "";
/*     */ 
/*     */   public VgfConvertDialog(Shell parentShell) {
/*  54 */     super(parentShell);
/*     */   }
/*     */ 
/*     */   protected Control createContents(Composite parent)
/*     */   {
/*  60 */     Control contents = super.createContents(parent);
/*  61 */     setTitle("To convert the binary VGF files that end with vgf to XML files.\nPlease input the source file directory or source file name, and the destination directory.");
/*     */ 
/*  64 */     setMessage("When click the Convert button, the converted files are saved to the destination directory.\nIt converts one file, or converts all files in the directory if a source directory was input.");
/*     */ 
/*  66 */     return contents;
/*     */   }
/*     */ 
/*     */   protected Control createDialogArea(Composite parent)
/*     */   {
/*  73 */     GridLayout layout = new GridLayout();
/*  74 */     layout.numColumns = 2;
/*  75 */     parent.setLayout(layout);
/*     */ 
/*  77 */     Label label0 = new Label(parent, 1);
/*  78 */     label0.setText(" ");
/*  79 */     Label label00 = new Label(parent, 1);
/*  80 */     label00.setText(" ");
/*     */ 
/*  82 */     Label label1 = new Label(parent, 0);
/*  83 */     label1.setText("Source Directory File: ");
/*  84 */     this.text1 = new Text(parent, 2048);
/*  85 */     this.text1.setText(this.in);
/*  86 */     this.text1.setLayoutData(new GridData(600, -1));
/*     */ 
/*  88 */     Label label2 = new Label(parent, 0);
/*  89 */     label2.setText("Destination Directory: ");
/*  90 */     this.text2 = new Text(parent, 2048);
/*  91 */     this.text2.setText(this.out);
/*  92 */     this.text2.setLayoutData(new GridData(600, -1));
/*     */ 
/*  94 */     Label label3 = new Label(parent, 0);
/*  95 */     label3.setText("Activity: ");
/*  96 */     this.text3 = new Text(parent, 2048);
/*  97 */     this.text3.setText(this.activity);
/*  98 */     this.text3.setLayoutData(new GridData(600, -1));
/*     */ 
/* 100 */     Label label4 = new Label(parent, 0);
/* 101 */     label4.setText("Sub activity: ");
/* 102 */     this.text4 = new Text(parent, 2048);
/* 103 */     this.text4.setText(this.subActivity);
/* 104 */     this.text4.setLayoutData(new GridData(600, -1));
/*     */ 
/* 106 */     Label label5 = new Label(parent, 0);
/* 107 */     label5.setText("Contour Tbl: ");
/* 108 */     this.text5 = new Text(parent, 2048);
/* 109 */     this.text5.setText(this.contTbl);
/* 110 */     this.text5.setLayoutData(new GridData(600, -1));
/*     */ 
/* 112 */     Label label6 = new Label(parent, 1);
/* 113 */     label6.setText(" ");
/* 114 */     Label label7 = new Label(parent, 1);
/* 115 */     label7.setText(" ");
/* 116 */     Label label8 = new Label(parent, 1);
/* 117 */     label8.setText(" ");
/*     */ 
/* 119 */     return parent;
/*     */   }
/*     */ 
/*     */   protected void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 124 */     createOkButton(parent, 0, 
/* 125 */       IDialogConstants.OK_LABEL, true);
/* 126 */     createButton(parent, 1, 
/* 127 */       IDialogConstants.CANCEL_LABEL, false);
/*     */   }
/*     */ 
/*     */   protected Button createOkButton(Composite parent, int id, String label, boolean defaultButton)
/*     */   {
/* 133 */     ((GridLayout)parent.getLayout()).numColumns += 1;
/* 134 */     Button btOK = new Button(parent, 8);
/* 135 */     btOK.setText("Convert");
/* 136 */     btOK.setFont(JFaceResources.getDialogFont());
/* 137 */     btOK.setData(new Integer(id));
/* 138 */     btOK.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 140 */         String s1 = VgfConvertDialog.this.text1.getText().trim();
/* 141 */         String s2 = VgfConvertDialog.this.text2.getText().trim();
/* 142 */         String s3 = VgfConvertDialog.this.text3.getText().trim();
/* 143 */         String s4 = VgfConvertDialog.this.text4.getText().trim();
/* 144 */         String s5 = VgfConvertDialog.this.text5.getText().trim();
/*     */         String s1Tem;
/*     */         String s1Tem;
/* 147 */         if ((s1.endsWith("*")) || (s1.endsWith(".vgf")))
/* 148 */           s1Tem = s1.substring(0, s1.lastIndexOf("/"));
/*     */         else {
/* 150 */           s1Tem = s1;
/*     */         }
/*     */ 
/* 153 */         int fileconverted = 0;
/*     */ 
/* 155 */         if ((s1.length() == 0) || (s2.length() == 0)) {
/* 156 */           VgfConvertDialog.this.setErrorMessage("Please input the directory.");
/*     */         }
/* 158 */         else if (!new File(s1Tem).exists()) {
/* 159 */           VgfConvertDialog.this.setErrorMessage("The Source directory does not exist");
/*     */         }
/* 161 */         else if (!new File(s2).exists()) {
/* 162 */           VgfConvertDialog.this.setErrorMessage("The Destination directory does not exist");
/*     */         }
/*     */         else
/*     */         {
/*     */           try {
/* 167 */             fileconverted = new Convert().convertMap(s1, s2, s3, s4, s5);
/*     */           } catch (IOException e) {
/* 169 */             System.out.println("The convertion failed.");
/*     */           }
/*     */ 
/* 173 */           VgfConvertDialog.this.setErrorMessage(fileconverted + " files are converted.  " + "The Convertion is finished.");
/*     */         }
/*     */       }
/*     */     });
/* 178 */     if (defaultButton) {
/* 179 */       Shell shell = parent.getShell();
/* 180 */       if (shell != null) {
/* 181 */         shell.setDefaultButton(btOK);
/*     */       }
/*     */     }
/*     */ 
/* 185 */     setButtonLayoutData(btOK);
/* 186 */     return btOK;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.standalone.vgfConverter.VgfConvertDialog
 * JD-Core Version:    0.6.2
 */