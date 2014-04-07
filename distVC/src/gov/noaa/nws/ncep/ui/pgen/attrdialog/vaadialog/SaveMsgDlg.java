/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.jface.dialogs.IDialogConstants;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class SaveMsgDlg extends CaveJFACEDialog
/*     */ {
/*  48 */   private static SaveMsgDlg INSTANCE = null;
/*     */ 
/*  53 */   private static VolcanoVaaAttrDlg volAttrDlgInstance = null;
/*     */ 
/*  58 */   private gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano volcano = null;
/*     */ 
/*  63 */   private gov.noaa.nws.ncep.ui.pgen.file.Volcano fVol = null;
/*     */   Text txtInfo;
/*     */   Text txtSave;
/*  79 */   String dirLocal = "."; String txtFileContent = "";
/*     */ 
/*     */   SaveMsgDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  89 */     super(parShell);
/*  90 */     setShellStyle(65632);
/*     */   }
/*     */ 
/*     */   public static SaveMsgDlg getInstance(Shell parShell)
/*     */   {
/* 103 */     if (INSTANCE == null) {
/*     */       try {
/* 105 */         INSTANCE = new SaveMsgDlg(parShell);
/*     */       } catch (VizException e) {
/* 107 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 110 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/* 117 */     HashMap attr = null;
/* 118 */     return attr;
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 127 */     createButton(parent, 0, "Save", true);
/* 128 */     createButton(parent, 1, 
/* 129 */       IDialogConstants.CANCEL_LABEL, false);
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 138 */     setReturnCode(1);
/* 139 */     close();
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 154 */     setReturnCode(0);
/* 155 */     close();
/*     */   }
/*     */ 
/*     */   public void setVolAttrDlgInstance(VolcanoVaaAttrDlg vaDlg)
/*     */   {
/* 166 */     volAttrDlgInstance = vaDlg;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 175 */     Composite top = (Composite)super.createDialogArea(parent);
/*     */ 
/* 177 */     GridLayout mainLayout = new GridLayout(3, false);
/* 178 */     mainLayout.marginHeight = 3;
/* 179 */     mainLayout.marginWidth = 3;
/* 180 */     top.setLayout(mainLayout);
/*     */ 
/* 182 */     getShell().setText("VAA Save");
/*     */ 
/* 184 */     this.volcano = volAttrDlgInstance.getVolcano();
/*     */ 
/* 189 */     this.txtInfo = new Text(top, 2122);
/*     */ 
/* 191 */     GridData gData = new GridData(400, 640);
/* 192 */     gData.horizontalSpan = 3;
/* 193 */     this.txtInfo.setLayoutData(gData);
/* 194 */     this.txtInfo.setText(getFileContent2());
/*     */ 
/* 196 */     this.txtSave = new Text(top, 2056);
/* 197 */     this.txtSave.setLayoutData(new GridData(4, 16777216, true, false, 
/* 198 */       3, 1));
/* 199 */     this.txtSave.setText(getFileName());
/*     */ 
/* 201 */     return top;
/*     */   }
/*     */ 
/*     */   public String getFileName()
/*     */   {
/* 210 */     String connector = "_";
/*     */ 
/* 212 */     StringBuilder sb = new StringBuilder();
/* 213 */     sb.append(this.volcano.getName());
/*     */ 
/* 215 */     sb.append(connector);
/* 216 */     sb.append(VaaInfo.getDateTime("yyyyMMdd"));
/* 217 */     sb.append(connector);
/* 218 */     sb.append(VaaInfo.getDateTime("HHmm"));
/*     */ 
/* 221 */     String corr = this.volcano.getCorr();
/* 222 */     if ((corr != null) && (corr.length() > 0)) {
/* 223 */       sb.append(connector).append(corr);
/*     */     }
/*     */ 
/* 227 */     sb.append(".txt");
/*     */ 
/* 229 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private String getVolNameId()
/*     */   {
/* 234 */     return this.volcano.getName() + ":::" + 
/* 235 */       this.volcano.getNumber();
/*     */   }
/*     */ 
/*     */   private String getVolcanoLoc()
/*     */   {
/* 240 */     return this.volcano == null ? "" : this.volcano.getTxtLoc();
/*     */   }
/*     */ 
/*     */   public gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano getVolcano() {
/* 244 */     return this.volcano;
/*     */   }
/*     */ 
/*     */   public void setVolcano(gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano volcano) {
/* 248 */     this.volcano = volcano;
/*     */   }
/*     */ 
/*     */   public String getFileContent2()
/*     */   {
/* 253 */     return this.txtFileContent;
/*     */   }
/*     */ 
/*     */   public void setTxtFileContent(String txt)
/*     */   {
/* 263 */     this.txtFileContent = txt.trim();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.SaveMsgDlg
 * JD-Core Version:    0.6.2
 */