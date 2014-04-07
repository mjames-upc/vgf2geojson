/*     */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Scanner;
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
/*     */ public class PgenHelpDialog extends ProductDialog
/*     */ {
/*  48 */   private static PgenHelpDialog INSTANCE = null;
/*     */ 
/*  50 */   private static String helpContent = null;
/*     */   private static final int TEXT_WIDTH = 600;
/*     */   private static final int TEXT_HEIGHT = 600;
/*     */ 
/*     */   protected PgenHelpDialog(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  62 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static PgenHelpDialog getInstance(Shell parShell)
/*     */   {
/*  75 */     if (INSTANCE == null) {
/*     */       try
/*     */       {
/*  78 */         INSTANCE = new PgenHelpDialog(parShell);
/*     */       } catch (VizException e) {
/*  80 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  85 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public void setTitle()
/*     */   {
/*  94 */     this.shell.setText("Product Generation Help");
/*     */   }
/*     */ 
/*     */   public void setLayout()
/*     */   {
/* 102 */     GridLayout mainLayout = new GridLayout(1, true);
/* 103 */     mainLayout.marginHeight = 1;
/* 104 */     mainLayout.marginWidth = 1;
/* 105 */     this.shell.setLayout(mainLayout);
/*     */   }
/*     */ 
/*     */   public void setDefaultLocation(Shell parent)
/*     */   {
/* 116 */     if (this.shellLocation == null) {
/* 117 */       Point pt = parent.getLocation();
/* 118 */       this.shell.setLocation(pt.x + 500, pt.y + 150);
/*     */     } else {
/* 120 */       this.shell.setLocation(this.shellLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void initializeComponents()
/*     */   {
/*     */     try
/*     */     {
/* 135 */       readHelpFile();
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 138 */       System.out.println("Cannot find file - productHelp.txt.");
/*     */     }
/*     */ 
/* 144 */     Composite mainComp = new Composite(this.shell, 0);
/* 145 */     GridLayout gl = new GridLayout(1, false);
/* 146 */     mainComp.setLayout(gl);
/*     */ 
/* 148 */     int style = 2626;
/*     */ 
/* 150 */     Text text = new Text(mainComp, style);
/* 151 */     text.setLayoutData(new GridData(600, 600));
/* 152 */     text.setEditable(false);
/*     */ 
/* 154 */     if (helpContent != null) {
/* 155 */       text.setText(helpContent);
/*     */     }
/*     */ 
/* 161 */     GridData gd = new GridData(16777216, -1, true, false);
/*     */ 
/* 163 */     Button closeBtn = new Button(mainComp, 0);
/* 164 */     closeBtn.setText("Close");
/* 165 */     closeBtn.setLayoutData(gd);
/* 166 */     closeBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 168 */         PgenHelpDialog.this.close();
/* 169 */         PgenHelpDialog.helpContent = null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void readHelpFile()
/*     */     throws FileNotFoundException
/*     */   {
/* 182 */     if (helpContent == null)
/*     */     {
/* 187 */       helpContent = "";
/* 188 */       File prdHelpFile = PgenStaticDataProvider.getProvider().getStaticFile(
/* 189 */         PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "PgenHelp.txt");
/*     */ 
/* 191 */       if ((prdHelpFile != null) && (prdHelpFile.exists()) && (prdHelpFile.canRead()))
/*     */       {
/* 193 */         Scanner scanner = new Scanner(prdHelpFile);
/*     */         try
/*     */         {
/* 196 */           while (scanner.hasNextLine()) {
/* 197 */             helpContent = helpContent + scanner.nextLine() + "\n";
/*     */           }
/*     */         }
/*     */         finally
/*     */         {
/* 202 */           scanner.close();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.PgenHelpDialog
 * JD-Core Version:    0.6.2
 */