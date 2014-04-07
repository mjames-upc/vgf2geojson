/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.CcfpInfo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import java.io.File;
/*     */ import org.eclipse.jface.dialogs.IDialogConstants;
/*     */ import org.eclipse.swt.graphics.Color;
/*     */ import org.eclipse.swt.graphics.Font;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class CcfpMsgDlg extends AttrDlg
/*     */ {
/*     */   private static final int CCFP_CONSTANT_UPDATE = 20101007;
/*  58 */   public static final String PGEN_CCFP_XSLT = "xslt" + File.separator + 
/*  58 */     "ccfp" + File.separator + "ccfpXml2Txt.xslt";
/*     */ 
/*  61 */   private static CcfpMsgDlg INSTANCE = null;
/*     */ 
/*  64 */   private CcfpTimeDlg timeDlg = null;
/*     */   private String issueTime;
/*     */   private String validTime;
/*     */   private Product product;
/*     */   private Text txtInfo;
/*     */   private Text txtSave;
/*  80 */   private String dirLocal = "."; private String txtFileContent = ""; private String txtFileName = "";
/*     */   private static final int LAYOUT_WIDTH = 2;
/*     */ 
/*     */   public CcfpMsgDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  93 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static CcfpMsgDlg getInstance(Shell parShell)
/*     */   {
/* 106 */     if (INSTANCE == null) {
/*     */       try {
/* 108 */         INSTANCE = new CcfpMsgDlg(parShell);
/*     */       } catch (VizException e) {
/* 110 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 113 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute ia)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 127 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/* 128 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*     */ 
/* 130 */     createButton(parent, 20101007, "Update", true);
/* 131 */     createButton(parent, 0, "Save", true);
/* 132 */     createButton(parent, 1, 
/* 133 */       IDialogConstants.CANCEL_LABEL, false);
/*     */ 
/* 135 */     getButton(20101007).addListener(13, 
/* 136 */       new Listener()
/*     */     {
/*     */       public void handleEvent(Event e) {
/* 139 */         CcfpMsgDlg.this.product = CcfpInfo.getCcfpPrds(CcfpMsgDlg.this.getIssueTime(), 
/* 140 */           CcfpMsgDlg.this.getValidTime());
/*     */         try
/*     */         {
/* 144 */           activityXML = 
/* 145 */             StorageUtils.serializeProduct(CcfpMsgDlg.this.product);
/*     */         }
/*     */         catch (PgenStorageException e1)
/*     */         {
/*     */           String activityXML;
/* 147 */           StorageUtils.showError(e1);
/*     */           return;
/*     */         }
/*     */         String activityXML;
/* 150 */         String txtFileContent = 
/* 151 */           CcfpInfo.convertXml2Txt(
/* 152 */           activityXML, 
/* 154 */           PgenStaticDataProvider.getProvider()
/* 155 */           .getFileAbsolutePath(
/* 157 */           PgenStaticDataProvider.getProvider()
/* 158 */           .getPgenLocalizationRoot() + 
/* 159 */           CcfpMsgDlg.PGEN_CCFP_XSLT));
/* 160 */         CcfpMsgDlg.this.txtInfo.setText(txtFileContent.trim());
/*     */       }
/*     */     });
/* 165 */     getButton(1).setLayoutData(
/* 166 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/* 167 */     getButton(0).setLayoutData(
/* 168 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/* 169 */     getButton(20101007).setLayoutData(
/* 170 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*     */   }
/*     */ 
/*     */   public void enableButtons()
/*     */   {
/* 179 */     getButton(20101007).setEnabled(true);
/* 180 */     getButton(1).setEnabled(true);
/* 181 */     getButton(0).setEnabled(true);
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 190 */     setReturnCode(1);
/* 191 */     close();
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/*     */     try
/*     */     {
/* 205 */       String dataURI = CcfpInfo.storeCcfpXmlFile(this.product);
/*     */ 
/* 207 */       StorageUtils.storeDerivedProduct(dataURI, this.txtSave.getText(), 
/* 208 */         "TEXT", this.txtInfo.getText());
/*     */     } catch (PgenStorageException e) {
/* 210 */       StorageUtils.showError(e);
/* 211 */       return;
/*     */     }
/*     */ 
/* 214 */     setReturnCode(0);
/* 215 */     close();
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 226 */     Composite top = (Composite)super.createDialogArea(parent);
/*     */ 
/* 228 */     GridLayout mainLayout = new GridLayout(3, false);
/* 229 */     mainLayout.marginHeight = 3;
/* 230 */     mainLayout.marginWidth = 3;
/* 231 */     top.setLayout(mainLayout);
/*     */ 
/* 233 */     getShell().setText("Collective Convection Forecast Message");
/*     */ 
/* 238 */     this.txtInfo = new Text(top, 2818);
/*     */ 
/* 240 */     GridData gData = new GridData(800, 300);
/* 241 */     gData.horizontalSpan = 3;
/* 242 */     this.txtInfo.setEditable(false);
/* 243 */     this.txtInfo.setBackground(new Color(getShell().getDisplay(), 235, 235, 
/* 244 */       235));
/* 245 */     this.txtInfo.setLayoutData(gData);
/* 246 */     this.txtInfo.setFont(new Font(getShell().getDisplay(), "Monospace", 11, 
/* 247 */       0));
/* 248 */     this.txtInfo.setText(getFileContent());
/*     */ 
/* 250 */     Group top_3 = new Group(top, 16384);
/* 251 */     top_3.setLayoutData(new GridData(4, 16777216, true, true, 
/* 252 */       2, 1));
/* 253 */     top_3.setLayout(new GridLayout(2, false));
/*     */ 
/* 255 */     Label lblFileName = new Label(top_3, 16384);
/* 256 */     lblFileName.setText("File Name: ");
/*     */ 
/* 258 */     this.txtSave = new Text(top_3, 2048);
/* 259 */     this.txtSave.setLayoutData(new GridData(4, 16777216, true, false, 
/* 260 */       1, 1));
/* 261 */     this.txtSave.setText(getFileName());
/*     */ 
/* 263 */     return top;
/*     */   }
/*     */ 
/*     */   public void setFileContent(String txt)
/*     */   {
/* 273 */     this.txtFileContent = txt;
/* 274 */     if ((this.txtInfo != null) && (!this.txtInfo.isDisposed()))
/* 275 */       this.txtInfo.setText(this.txtFileContent);
/*     */   }
/*     */ 
/*     */   public void setFileName(String name)
/*     */   {
/* 284 */     this.txtFileName = name;
/*     */   }
/*     */ 
/*     */   public void setTxtSaveTxt(String fname)
/*     */   {
/*     */   }
/*     */ 
/*     */   private String getFileContent()
/*     */   {
/* 295 */     return this.txtFileContent;
/*     */   }
/*     */ 
/*     */   private String getFileName()
/*     */   {
/* 302 */     return this.txtFileName;
/*     */   }
/*     */ 
/*     */   public CcfpTimeDlg getTimeDlg()
/*     */   {
/* 311 */     return this.timeDlg;
/*     */   }
/*     */ 
/*     */   public void setTimeDlg(CcfpTimeDlg timeDlg)
/*     */   {
/* 321 */     this.timeDlg = timeDlg;
/*     */   }
/*     */ 
/*     */   public String getIssueTime()
/*     */   {
/* 330 */     return this.issueTime;
/*     */   }
/*     */ 
/*     */   public void setIssueTime(String issueTime)
/*     */   {
/* 339 */     this.issueTime = issueTime;
/*     */   }
/*     */ 
/*     */   public String getValidTime()
/*     */   {
/* 348 */     return this.validTime;
/*     */   }
/*     */ 
/*     */   public void setValidTime(String validTime)
/*     */   {
/* 357 */     this.validTime = validTime;
/*     */   }
/*     */ 
/*     */   public void setProduct(Product product) {
/* 361 */     this.product = product;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.CcfpMsgDlg
 * JD-Core Version:    0.6.2
 */