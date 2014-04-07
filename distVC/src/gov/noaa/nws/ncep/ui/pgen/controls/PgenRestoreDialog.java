/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import org.eclipse.jface.dialogs.Dialog;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Table;
/*     */ import org.eclipse.swt.widgets.TableColumn;
/*     */ import org.eclipse.swt.widgets.TableItem;
/*     */ 
/*     */ public class PgenRestoreDialog extends Dialog
/*     */ {
/*  54 */   private final String dlgTitle = "PGEN Restore";
/*     */ 
/*  56 */   private Composite dlgArea = null;
/*  57 */   private Table fileTable = null;
/*     */   private static final int RESTORE_ID = 8621;
/*     */   private static final int EMPTY_ID = 8622;
/*  62 */   private Button restoreBtn = null;
/*  63 */   private Button emptyBtn = null;
/*  64 */   private Button cancelBtn = null;
/*     */   private String tmpdir;
/*     */   private FilenameFilter filter;
/*     */   private boolean filesFound;
/*     */ 
/*     */   public PgenRestoreDialog(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  75 */     super(parShell);
/*  76 */     setShellStyle(32784);
/*  77 */     this.tmpdir = PgenUtil.getTempWorkDir();
/*     */ 
/*  82 */     this.filter = new FilenameFilter()
/*     */     {
/*     */       public boolean accept(File dir, String name) {
/*  85 */         return (name.startsWith("pgen_session.")) && 
/*  86 */           (name.endsWith(".tmp"));
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected void configureShell(Shell shell)
/*     */   {
/* 100 */     super.configureShell(shell);
/*     */ 
/* 102 */     shell.setText("PGEN Restore");
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 114 */     this.dlgArea = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 116 */     GridLayout gl = new GridLayout(1, true);
/* 117 */     gl.marginTop = 5;
/* 118 */     gl.marginBottom = 5;
/* 119 */     gl.verticalSpacing = 10;
/* 120 */     this.dlgArea.setLayout(gl);
/*     */ 
/* 125 */     Label info = new Label(this.dlgArea, 0);
/* 126 */     info.setText("Select a PGEN Session to restore:");
/* 127 */     info.setLayoutData(new GridData());
/*     */ 
/* 129 */     this.fileTable = new Table(this.dlgArea, 2052);
/* 130 */     this.fileTable.setHeaderVisible(true);
/* 131 */     this.fileTable.setLinesVisible(true);
/*     */ 
/* 136 */     String[] titles = { "PGEN Session", "Timestamp" };
/* 137 */     for (int i = 0; i < titles.length; i++) {
/* 138 */       TableColumn column = new TableColumn(this.fileTable, 0);
/* 139 */       column.setText(titles[i]);
/*     */     }
/*     */ 
/* 142 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
/* 143 */     File dir = new File(this.tmpdir);
/*     */ 
/* 148 */     for (File f : dir.listFiles(this.filter)) {
/* 149 */       TableItem item = new TableItem(this.fileTable, 0);
/* 150 */       item.setText(0, f.getName());
/* 151 */       Calendar time = Calendar.getInstance();
/* 152 */       time.setTimeInMillis(f.lastModified());
/* 153 */       item.setText(1, sdf.format(time.getTime()));
/* 154 */       item.setData(f);
/*     */     }
/*     */ 
/* 160 */     this.filesFound = true;
/* 161 */     if (dir.listFiles(this.filter).length == 0) {
/* 162 */       TableItem item = new TableItem(this.fileTable, 0);
/* 163 */       item.setText(0, "No Data Available.");
/* 164 */       item.setData(null);
/* 165 */       this.filesFound = false;
/*     */     }
/*     */ 
/* 171 */     for (int i = 0; i < titles.length; i++) {
/* 172 */       this.fileTable.getColumn(i).pack();
/*     */     }
/*     */ 
/* 175 */     this.fileTable.setLayoutData(new GridData(4, 4, true, true));
/*     */ 
/* 177 */     return this.dlgArea;
/*     */   }
/*     */ 
/*     */   protected void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 186 */     this.restoreBtn = createButton(parent, 8621, "Restore", false);
/*     */ 
/* 188 */     this.emptyBtn = createButton(parent, 8622, "Empty List", false);
/*     */ 
/* 191 */     this.cancelBtn = createButton(parent, 1, "Cancel", false);
/*     */ 
/* 193 */     this.restoreBtn.setEnabled(this.filesFound);
/* 194 */     this.emptyBtn.setEnabled(this.filesFound);
/*     */   }
/*     */ 
/*     */   protected void buttonPressed(int buttonId)
/*     */   {
/* 203 */     super.buttonPressed(buttonId);
/*     */ 
/* 205 */     if (buttonId == 8621)
/*     */     {
/* 207 */       TableItem[] items = this.fileTable.getSelection();
/*     */ 
/* 211 */       String fileName = new String(items[0].getData().toString());
/* 212 */       Products products = FileTools.read(fileName);
/* 213 */       PgenSession.getInstance().getPgenResource().replaceProduct(ProductConverter.convert(products));
/* 214 */       super.buttonPressed(0);
/*     */     }
/* 217 */     else if (buttonId == 8622)
/*     */     {
/* 221 */       this.fileTable.removeAll();
/* 222 */       deleteFiles();
/* 223 */       this.restoreBtn.setEnabled(false);
/* 224 */       this.emptyBtn.setEnabled(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void deleteFiles()
/*     */   {
/* 234 */     File dir = new File(this.tmpdir);
/* 235 */     for (File f : dir.listFiles(this.filter))
/* 236 */       f.delete();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.PgenRestoreDialog
 * JD-Core Version:    0.6.2
 */