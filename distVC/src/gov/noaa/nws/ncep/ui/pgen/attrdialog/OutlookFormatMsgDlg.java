/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*     */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureDialog;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.TimeZone;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.Node;
/*     */ import org.eclipse.jface.dialogs.Dialog;
/*     */ import org.eclipse.jface.dialogs.InputDialog;
/*     */ import org.eclipse.swt.events.DisposeEvent;
/*     */ import org.eclipse.swt.events.DisposeListener;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Font;
/*     */ import org.eclipse.swt.graphics.FontMetrics;
/*     */ import org.eclipse.swt.graphics.GC;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class OutlookFormatMsgDlg extends Dialog
/*     */ {
/*     */   private Composite top;
/*     */   private Text messageBox;
/*     */   private String message;
/*     */   private OutlookFormatDlg ofd;
/*     */   private Outlook otlk;
/*     */   private Layer layer;
/*  82 */   private final int NUM_LINES = 25;
/*     */ 
/*  84 */   private final int NUM_COLUMNS = 68;
/*     */ 
/*     */   protected OutlookFormatMsgDlg(Shell parentShell, OutlookFormatDlg ofd, Outlook otlk, Layer layer)
/*     */   {
/*  91 */     super(parentShell);
/*  92 */     setShellStyle(96);
/*  93 */     this.ofd = ofd;
/*  94 */     this.otlk = otlk;
/*  95 */     this.layer = layer;
/*  96 */     this.message = "";
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 106 */     getShell().setText("Outlook Message");
/*     */ 
/* 108 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 113 */     GridLayout mainLayout = new GridLayout(1, true);
/* 114 */     mainLayout.marginHeight = 3;
/* 115 */     mainLayout.marginWidth = 3;
/* 116 */     this.top.setLayout(mainLayout);
/*     */ 
/* 121 */     this.messageBox = new Text(this.top, 2570);
/*     */ 
/* 123 */     this.messageBox.setFont(new Font(this.messageBox.getDisplay(), "Courier", 12, 
/* 124 */       0));
/* 125 */     GridData gd = new GridData(768);
/*     */ 
/* 129 */     gd.heightHint = (25 * this.messageBox.getLineHeight());
/* 130 */     GC gc = new GC(this.messageBox);
/* 131 */     FontMetrics fm = gc.getFontMetrics();
/* 132 */     gd.widthHint = (68 * fm.getAverageCharWidth());
/*     */ 
/* 134 */     this.messageBox.setLayoutData(gd);
/* 135 */     this.messageBox.setText(this.message);
/*     */ 
/* 138 */     this.messageBox.addDisposeListener(new DisposeListener()
/*     */     {
/*     */       public void widgetDisposed(DisposeEvent e)
/*     */       {
/* 142 */         Text w = (Text)e.widget;
/* 143 */         w.getFont().dispose();
/*     */       }
/*     */     });
/* 148 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void okPressed()
/*     */   {
/* 159 */     savePressed();
/* 160 */     super.okPressed();
/*     */   }
/*     */ 
/*     */   private void savePressed()
/*     */   {
/* 168 */     String pdName = this.ofd.getOtlkDlg().drawingLayer.getActiveProduct()
/* 169 */       .getType();
/* 170 */     ProductType pt = (ProductType)ProductConfigureDialog.getProductTypes().get(pdName);
/* 171 */     if (pt != null) {
/* 172 */       pdName = pt.getType();
/*     */     }
/* 174 */     String pd1 = pdName.replaceAll(" ", "_");
/*     */ 
/* 176 */     String dirPath = PgenUtil.getPgenOprDirectory() + File.separator + pd1 + 
/* 177 */       File.separator + "prod" + File.separator + "text" + 
/* 178 */       File.separator;
/*     */ 
/* 180 */     String fileName = getFileName(this.otlk) + ".dat";
/*     */ 
/* 182 */     InputDialog dlg = new InputDialog(getShell(), "Save Outlook", 
/* 183 */       "Save To File:", fileName, null);
/* 184 */     dlg.open();
/* 185 */     if (dlg.getReturnCode() == 0) {
/* 186 */       fileName = dlg.getValue();
/* 187 */       if (!fileName.isEmpty()) {
/* 188 */         this.ofd.issueOutlook(this.otlk);
/*     */ 
/* 190 */         String dataURI = storeProduct(getFileName(this.otlk) + ".xml");
/* 191 */         if (dataURI == null)
/* 192 */           return;
/*     */         try
/*     */         {
/* 195 */           StorageUtils.storeDerivedProduct(dataURI, fileName, "TEXT", 
/* 196 */             this.message, true);
/*     */         } catch (PgenStorageException e) {
/* 198 */           StorageUtils.showError(e);
/* 199 */           return;
/*     */         }
/*     */ 
/* 205 */         close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getFileName(Outlook ol)
/*     */   {
/* 218 */     String type = "";
/* 219 */     if (ol != null) {
/* 220 */       type = ol.getOutlookType();
/*     */     }
/*     */     else
/*     */     {
/* 224 */       type = this.layer.getName();
/*     */     }
/*     */ 
/* 228 */     String name = "";
/* 229 */     if (type != null) {
/* 230 */       type = type.toUpperCase();
/* 231 */       String xpath = OutlookAttrDlg.OTLK_XPATH + "[@name='" + type + "']";
/* 232 */       String prefix = "";
/* 233 */       Node nd = OutlookAttrDlg.readOutlookTbl()
/* 234 */         .selectSingleNode(xpath);
/*     */ 
/* 236 */       if (nd != null) {
/* 237 */         prefix = nd.valueOf("@prefix");
/*     */       }
/*     */ 
/* 240 */       if (prefix.isEmpty())
/* 241 */         name = name + "outlook";
/*     */       else {
/* 243 */         name = name + prefix + "outlook";
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 251 */     String days = this.ofd.getDays();
/*     */ 
/* 253 */     if (days.contains("Enh")) {
/* 254 */       days = "Day1";
/*     */     }
/*     */ 
/* 257 */     name = name + "_" + 
/* 258 */       days.toUpperCase().replace("-", "").replace(" ", "");
/* 259 */     name = name + "_" + String.format("%1$td%1$tH%1$tM", new Object[] { this.ofd.getInitTime() }) + "Z";
/* 260 */     return name;
/*     */   }
/*     */ 
/*     */   private void updatePressed()
/*     */   {
/* 268 */     if (this.otlk != null)
/*     */     {
/* 270 */       this.otlk.reorderLines();
/* 271 */       this.messageBox.setText(this.ofd.formatOtlk(this.otlk, this.layer));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void cancelPressed()
/*     */   {
/* 285 */     this.ofd.close();
/* 286 */     super.cancelPressed();
/*     */   }
/*     */ 
/*     */   public void setMessage(String str)
/*     */   {
/* 297 */     this.message = str;
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 307 */     if (getShell() == null) {
/* 308 */       create();
/*     */     }
/*     */ 
/* 311 */     getShell().setLocation(getShell().getParent().getLocation());
/* 312 */     getButton(0).setText("Save");
/* 313 */     getButtonBar().pack();
/*     */ 
/* 315 */     return super.open();
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 325 */     GridLayout barGl = new GridLayout(3, false);
/* 326 */     parent.setLayout(barGl);
/*     */ 
/* 328 */     Button updtBtn = new Button(parent, 8);
/*     */ 
/* 330 */     super.createButtonsForButtonBar(parent);
/*     */ 
/* 333 */     updtBtn.setText("Update");
/* 334 */     updtBtn.setLayoutData(getButton(1)
/* 335 */       .getLayoutData());
/* 336 */     updtBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 340 */         OutlookFormatMsgDlg.this.updatePressed();
/*     */       }
/*     */     });
/* 345 */     getButton(0).setText("Save");
/*     */   }
/*     */ 
/*     */   public void setOtlk(Outlook otlk) {
/* 349 */     this.otlk = otlk;
/*     */   }
/*     */ 
/*     */   public Outlook getOtlk() {
/* 353 */     return this.otlk;
/*     */   }
/*     */ 
/*     */   public void setLayer(Layer layer) {
/* 357 */     this.layer = layer;
/*     */   }
/*     */ 
/*     */   public Layer getLayer() {
/* 361 */     return this.layer;
/*     */   }
/*     */ 
/*     */   private String storeProduct(String label)
/*     */   {
/* 373 */     Layer defaultLayer = new Layer();
/* 374 */     if (this.otlk != null) defaultLayer.addElement(this.otlk);
/* 375 */     ArrayList layerList = new ArrayList();
/* 376 */     layerList.add(defaultLayer);
/*     */ 
/* 378 */     ProductTime refTime = null;
/* 379 */     String forecaster = "";
/* 380 */     if (this.otlk != null) {
/* 381 */       refTime = new ProductTime(this.otlk.getIssueTime());
/* 382 */       forecaster = this.otlk.getForecaster();
/*     */     }
/*     */     else {
/* 385 */       refTime = new ProductTime(Calendar.getInstance(TimeZone.getTimeZone("GMT")));
/* 386 */       forecaster = this.ofd.getForecaster();
/*     */     }
/*     */ 
/* 389 */     Product defaultProduct = new Product("", "OUTLOOK", forecaster, null, 
/* 390 */       refTime, layerList);
/*     */ 
/* 395 */     defaultProduct.setOutputFile(label);
/* 396 */     defaultProduct.setCenter(PgenUtil.getCurrentOffice());
/*     */     try
/*     */     {
/* 399 */       dataURI = StorageUtils.storeProduct(defaultProduct);
/*     */     }
/*     */     catch (PgenStorageException e)
/*     */     {
/*     */       String dataURI;
/* 401 */       StorageUtils.showError(e);
/* 402 */       return null;
/*     */     }
/*     */     String dataURI;
/* 405 */     return dataURI;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.OutlookFormatMsgDlg
 * JD-Core Version:    0.6.2
 */