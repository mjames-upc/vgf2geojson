/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Spenes;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import java.util.ArrayList;
/*     */ import org.eclipse.swt.events.DisposeEvent;
/*     */ import org.eclipse.swt.events.DisposeListener;
/*     */ import org.eclipse.swt.events.ModifyEvent;
/*     */ import org.eclipse.swt.events.ModifyListener;
/*     */ import org.eclipse.swt.graphics.Font;
/*     */ import org.eclipse.swt.graphics.FontMetrics;
/*     */ import org.eclipse.swt.graphics.GC;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class SpenesFormatMsgDlg extends CaveJFACEDialog
/*     */ {
/*     */   private Composite top;
/*     */   private String spenesMsg;
/*     */   private Spenes spenes;
/*     */   private SpenesFormatDlg sfDlg;
/*     */   private Text txtFileLabel;
/*  69 */   private final int NUM_LINES = 25;
/*     */ 
/*  71 */   private final int NUM_COLUMNS = 68;
/*     */ 
/*     */   protected SpenesFormatMsgDlg(Shell parentShell, SpenesFormatDlg sfd)
/*     */   {
/*  77 */     super(parentShell);
/*  78 */     this.sfDlg = sfd;
/*  79 */     this.spenes = this.sfDlg.getSpenes();
/*     */   }
/*     */ 
/*     */   private String getTxtFileName()
/*     */   {
/*  88 */     String connector = "_";
/*  89 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*  91 */     sb.append(this.spenes.getName());
/*  92 */     sb.append(connector);
/*  93 */     String initDataTime = this.spenes.getInitDateTime().replace(" ", connector)
/*  94 */       .replace("/", connector);
/*  95 */     sb.append(initDataTime);
/*  96 */     sb.append(".txt");
/*  97 */     String spenesTxtFileName = sb.toString();
/*  98 */     return spenesTxtFileName;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 106 */     getShell().setText("SPENES TEXT");
/*     */ 
/* 108 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 113 */     GridLayout mainLayout = new GridLayout(1, true);
/* 114 */     mainLayout.marginHeight = 3;
/* 115 */     mainLayout.marginWidth = 3;
/* 116 */     this.top.setLayout(mainLayout);
/*     */ 
/* 121 */     Text messageBox = new Text(this.top, 2626);
/*     */ 
/* 123 */     messageBox.setFont(new Font(messageBox.getDisplay(), "Courier", 12, 
/* 124 */       0));
/* 125 */     GridData gd = new GridData(768);
/*     */ 
/* 129 */     gd.heightHint = (25 * messageBox.getLineHeight());
/* 130 */     GC gc = new GC(messageBox);
/* 131 */     FontMetrics fm = gc.getFontMetrics();
/* 132 */     gd.widthHint = (68 * fm.getAverageCharWidth());
/*     */ 
/* 134 */     messageBox.setLayoutData(gd);
/* 135 */     if (this.spenes != null) {
/* 136 */       setMessage(generateSpenesText(this.spenes));
/*     */     }
/* 138 */     messageBox.setText(this.spenesMsg);
/*     */ 
/* 141 */     messageBox.addDisposeListener(new DisposeListener()
/*     */     {
/*     */       public void widgetDisposed(DisposeEvent e)
/*     */       {
/* 145 */         Text w = (Text)e.widget;
/* 146 */         w.getFont().dispose();
/*     */       }
/*     */     });
/* 154 */     Label sep = new Label(this.top, 258);
/* 155 */     sep.setLayoutData(new GridData(768));
/*     */ 
/* 160 */     Label filelabel = new Label(this.top, 0);
/* 161 */     filelabel.setText("SPENES File Name:  ");
/*     */ 
/* 163 */     this.txtFileLabel = new Text(this.top, 18496);
/* 164 */     GridData gData1 = new GridData(400, 16);
/* 165 */     gData1.horizontalAlignment = 7;
/* 166 */     this.txtFileLabel.setLayoutData(gData1);
/* 167 */     this.txtFileLabel.setText(getTxtFileName());
/* 168 */     this.txtFileLabel.addModifyListener(new TxtModifyListener(null));
/*     */ 
/* 170 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void okPressed()
/*     */   {
/* 197 */     String dataURI = storeSpenesActivity();
/*     */ 
/* 199 */     String txtFileName = this.txtFileLabel.getText();
/*     */ 
/* 204 */     if (dataURI != null) {
/*     */       try {
/* 206 */         StorageUtils.storeDerivedProduct(dataURI, txtFileName, "TEXT", 
/* 207 */           this.spenesMsg);
/*     */       } catch (PgenStorageException e) {
/* 209 */         StorageUtils.showError(e);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 214 */     super.okPressed();
/*     */   }
/*     */ 
/*     */   private String storeSpenesActivity()
/*     */   {
/* 220 */     Layer defaultLayer = new Layer();
/* 221 */     defaultLayer.addElement(this.spenes);
/* 222 */     ArrayList layerList = new ArrayList();
/* 223 */     layerList.add(defaultLayer);
/*     */ 
/* 225 */     String forecaster = System.getProperty("user.name");
/* 226 */     ProductTime refTime = new ProductTime();
/*     */ 
/* 228 */     Product defaultProduct = new Product("", "NESDIS SPENES", forecaster, 
/* 229 */       null, refTime, layerList);
/*     */ 
/* 231 */     defaultProduct.setOutputFile(this.sfDlg.getSpDlg().drawingLayer
/* 232 */       .buildActivityLabel(defaultProduct));
/* 233 */     defaultProduct.setCenter(PgenUtil.getCurrentOffice());
/*     */     try
/*     */     {
/* 236 */       dataURI = StorageUtils.storeProduct(defaultProduct);
/*     */     }
/*     */     catch (PgenStorageException e)
/*     */     {
/*     */       String dataURI;
/* 238 */       StorageUtils.showError(e);
/* 239 */       return null;
/*     */     }
/*     */     String dataURI;
/* 241 */     return dataURI;
/*     */   }
/*     */ 
/*     */   public void setMessage(String spenesMessage)
/*     */   {
/* 252 */     this.spenesMsg = spenesMessage;
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 261 */     if (getShell() == null) {
/* 262 */       create();
/*     */     }
/*     */ 
/* 265 */     getButton(0).setText("Save");
/* 266 */     getButtonBar().pack();
/* 267 */     return super.open();
/*     */   }
/*     */ 
/*     */   private String generateSpenesText(Spenes sp)
/*     */   {
/* 279 */     sp.generateStatesWfosRfcs();
/*     */ 
/* 281 */     StringBuilder sb = new StringBuilder();
/* 282 */     sb.append("ZCZC NFDSPENES ALL\n\nSPENES\n\n");
/*     */ 
/* 284 */     sb.append(sp.getStateZ000());
/*     */ 
/* 286 */     sb.append("\n.\n\nSATELLITE PRECIPITATION ESTIMATES..DATE/TIME ");
/*     */ 
/* 289 */     sb.append(sp.getInitDateTime());
/*     */ 
/* 291 */     sb.append("\n\nSATELLITE ANALYSIS BRANCH/NESDIS---NPPU---TEL.301-763-8678\n\nLATEST DATA USED:");
/* 292 */     sb.append(sp.getLatestDataUsed());
/* 293 */     sb.append("   " + sp.getObsHr() + "00Z");
/* 294 */     sb.append("   " + sp.getForecasters());
/* 295 */     sb.append("\n\n.\n\nLOCATION...");
/* 296 */     sb.append(sp.getLocation());
/*     */ 
/* 298 */     sb.append("\n\n.\n\nATTN WFOS...");
/* 299 */     sb.append(sp.getAttnWFOs());
/* 300 */     sb.append("\n\n.\n\nATTN RFCS...");
/* 301 */     sb.append(sp.getAttnRFCs());
/*     */ 
/* 303 */     sb.append("\n\n.\nEVENT...");
/* 304 */     sb.append(sp.getEvent());
/*     */ 
/* 306 */     sb.append("\n\n.\n\nSATELLITE ANALYSIS AND TRENDS...");
/* 307 */     sb.append(" " + sp.getSatAnalysisTrend());
/* 308 */     sb.append("\n\n.\n\n");
/* 309 */     sb.append("AN ANNOTATED SATELLITE GRAPHIC SHOWING THE HEAV PERCIPITATION THREAT AREA\n\n");
/* 310 */     sb.append("SHOULD BE AVAILABLE ON THE INTERNET ADDRESS LISTED BELOW IN APPROXIMATELY\n\n10-15 MINUTES.\n\n.\n\n");
/* 311 */     sb.append("SHORT TERM OUTLOOK VALID ");
/* 312 */     sb.append(sp.getShortTermBegin() + "00-" + sp.getShortTermEnd() + "00Z");
/* 313 */     sb.append("... " + sp.getOutlookLevel());
/* 314 */     sb.append(" CONFIDENCE FACTOR IN SHORT TERM OUTLOOK...");
/* 315 */     sb.append(sp.getAddlInfo());
/* 316 */     sb.append("\n\n.\n\n");
/* 317 */     sb.append("SEE NCEP HPC DISCUSSION AND QPF/S FOR FORECAST.\n\n....NESDIS IS A MEMBER OF 12 PLANET....\n\n.\n\n");
/* 318 */     sb.append("SSD\\SAB WEB ADDRESS FOR PRECIP ESTIMATES:\n\nHTTP://WWW.SSD.NOAA.GOV/PS/PCPN/\n\n");
/* 319 */     sb.append("...ALL LOWER CASE EXCEPT /PS/PCPN/\n\n.\n\n");
/*     */ 
/* 322 */     sb.append("LAT...LON ");
/* 323 */     sb.append(sp.getLatLon());
/* 324 */     sb.append("\n\n.\n\nNNNN");
/*     */ 
/* 326 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private class TxtModifyListener
/*     */     implements ModifyListener
/*     */   {
/*     */     private TxtModifyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void modifyText(ModifyEvent e)
/*     */     {
/* 180 */       if (!(e.widget instanceof Text))
/*     */       {
/* 182 */         (e.widget instanceof Combo);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.SpenesFormatMsgDlg
 * JD-Core Version:    0.6.2
 */