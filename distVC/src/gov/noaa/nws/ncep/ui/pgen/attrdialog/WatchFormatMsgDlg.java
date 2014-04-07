/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.common.localization.LocalizationFile;
/*     */ import com.raytheon.uf.common.serialization.SerializationUtil;
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.DerivedProduct;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*     */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureDialog;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import org.eclipse.swt.events.DisposeEvent;
/*     */ import org.eclipse.swt.events.DisposeListener;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.events.SelectionListener;
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
/*     */ import org.w3c.dom.Document;
/*     */ 
/*     */ public class WatchFormatMsgDlg extends CaveJFACEDialog
/*     */ {
/*     */   public static final String PROD_TYPE = "TEXT";
/*     */   private Composite top;
/*     */   private String watchMsg;
/*     */   private WatchFormatDlg wfd;
/*  86 */   private final int NUM_LINES = 25;
/*     */ 
/*  88 */   private final int NUM_COLUMNS = 68;
/*     */ 
/*     */   protected WatchFormatMsgDlg(Shell parentShell, WatchFormatDlg wfd)
/*     */   {
/*  94 */     super(parentShell);
/*  95 */     this.wfd = wfd;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 105 */     getShell().setText("Severe Weather Watch");
/*     */ 
/* 107 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 112 */     GridLayout mainLayout = new GridLayout(1, true);
/* 113 */     mainLayout.marginHeight = 3;
/* 114 */     mainLayout.marginWidth = 3;
/* 115 */     this.top.setLayout(mainLayout);
/*     */ 
/* 120 */     Text messageBox = new Text(this.top, 2570);
/*     */ 
/* 122 */     messageBox.setFont(new Font(messageBox.getDisplay(), "Courier", 12, 
/* 123 */       0));
/* 124 */     GridData gd = new GridData(768);
/*     */ 
/* 128 */     gd.heightHint = (25 * messageBox.getLineHeight());
/* 129 */     GC gc = new GC(messageBox);
/* 130 */     FontMetrics fm = gc.getFontMetrics();
/* 131 */     gd.widthHint = (68 * fm.getAverageCharWidth());
/*     */ 
/* 133 */     messageBox.setLayoutData(gd);
/* 134 */     setMessage(generateProducts(putWatcInProduct(this.wfd.getWatchBox()), 
/* 135 */       "WatchText.xlt"));
/* 136 */     messageBox.setText(this.watchMsg);
/*     */ 
/* 139 */     messageBox.addDisposeListener(new DisposeListener()
/*     */     {
/*     */       public void widgetDisposed(DisposeEvent e)
/*     */       {
/* 143 */         Text w = (Text)e.widget;
/* 144 */         w.getFont().dispose();
/*     */       }
/*     */     });
/* 149 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected void okPressed()
/*     */   {
/* 164 */     String watchNumber = String.format("%1$04d", new Object[] { Integer.valueOf(this.wfd.getWatchNumber()) });
/*     */ 
/* 166 */     String pdName = this.wfd.getWbDlg().drawingLayer.getActiveProduct()
/* 167 */       .getType();
/* 168 */     ProductType pt = (ProductType)ProductConfigureDialog.getProductTypes().get(pdName);
/* 169 */     if (pt != null) {
/* 170 */       pdName = pt.getType();
/*     */     }
/* 172 */     String pd1 = pdName.replaceAll(" ", "_");
/*     */ 
/* 174 */     String dirPath = PgenUtil.getPgenOprDirectory() + File.separator + pd1 + 
/* 175 */       File.separator + "prod" + File.separator + "text" + 
/* 176 */       File.separator;
/*     */ 
/* 178 */     String fname = "WW" + watchNumber + ".xml";
/*     */ 
/* 182 */     this.wfd.getWatchBox().setIssueFlag(1);
/*     */ 
/* 185 */     this.wfd.getWbDlg().drawingLayer.resetElement(this.wfd.getWatchBox());
/* 186 */     this.wfd.getWbDlg().mapEditor.refresh();
/*     */ 
/* 189 */     this.wfd.getWatchBox().storeProduct(fname);
/* 190 */     String dataURI = this.wfd.getWatchBox().getDataURI();
/*     */ 
/* 194 */     Products pd = putWatcInProduct(this.wfd.getWatchBox());
/*     */ 
/* 208 */     List prodList = new ArrayList();
/* 209 */     prodList.add(new DerivedProduct("ww" + watchNumber + ".txt", "TEXT", 
/* 210 */       this.watchMsg));
/* 211 */     prodList.add(new DerivedProduct("WW" + watchNumber + ".SAW", "TEXT", 
/* 212 */       generateProducts(pd, "SAW.xlt")));
/* 213 */     prodList.add(new DerivedProduct("WW" + watchNumber + ".SEL", "TEXT", 
/* 214 */       generateProducts(pd, "SEL.xlt")));
/* 215 */     prodList.add(new DerivedProduct("WW" + watchNumber + ".SEV", "TEXT", 
/* 216 */       generateProducts(pd, "SEV.xlt")));
/* 217 */     prodList.add(new DerivedProduct("WW" + watchNumber + ".WOU", "TEXT", 
/* 218 */       generateProducts(pd, "WOU.xlt")));
/*     */     try
/*     */     {
/* 221 */       StorageUtils.storeDerivedProducts(dataURI, prodList);
/*     */     } catch (PgenStorageException e) {
/* 223 */       StorageUtils.showError(e);
/*     */     }
/*     */ 
/* 226 */     this.wfd.close();
/* 227 */     WatchBoxAttrDlg.getInstance(null).close();
/* 228 */     super.okPressed();
/*     */   }
/*     */ 
/*     */   protected void cancelPressed()
/*     */   {
/* 242 */     this.wfd.close();
/* 243 */     WatchBoxAttrDlg.getInstance(null).close();
/* 244 */     super.cancelPressed();
/*     */   }
/*     */ 
/*     */   private void saveProducts(String outStr, String outFile)
/*     */   {
/* 250 */     if ((outStr != null) && (!outStr.isEmpty()))
/*     */     {
/* 252 */       FileTools.writeFile(outFile, outStr);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String generateProducts(Products pd, String xslt)
/*     */   {
/* 259 */     Document sw = null;
/*     */     try
/*     */     {
/* 262 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 263 */       dbf.setNamespaceAware(true);
/* 264 */       DocumentBuilder db = dbf.newDocumentBuilder();
/* 265 */       sw = db.newDocument();
/* 266 */       Marshaller mar = SerializationUtil.getJaxbContext()
/* 267 */         .createMarshaller();
/* 268 */       mar.marshal(pd, sw);
/*     */     } catch (Exception e) {
/* 270 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 273 */     DOMSource ds = new DOMSource(sw);
/*     */ 
/* 276 */     String xsltPath = PgenStaticDataProvider.getProvider()
/* 277 */       .getPgenLocalizationRoot() + 
/* 278 */       "xslt" + 
/* 279 */       File.separator + 
/* 280 */       "watchbox" + File.separator + xslt;
/*     */ 
/* 282 */     LocalizationFile lFile = PgenStaticDataProvider.getProvider()
/* 283 */       .getStaticLocalizationFile(xsltPath);
/*     */ 
/* 285 */     String outStr = "";
/* 286 */     if (lFile != null) {
/* 287 */       outStr = PgenUtil.applyStyleSheet(ds, lFile.getFile()
/* 288 */         .getAbsolutePath());
/*     */     }
/*     */ 
/* 291 */     return outStr;
/*     */   }
/*     */ 
/*     */   public void setMessage(String str)
/*     */   {
/* 302 */     this.watchMsg = str;
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 312 */     if (getShell() == null) {
/* 313 */       create();
/*     */     }
/*     */ 
/* 317 */     getButton(0).setText("Save");
/* 318 */     getButtonBar().pack();
/*     */ 
/* 320 */     return super.open();
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 330 */     GridLayout barGl = new GridLayout(3, false);
/* 331 */     parent.setLayout(barGl);
/*     */ 
/* 333 */     Button editBtn = new Button(parent, 8);
/*     */ 
/* 335 */     super.createButtonsForButtonBar(parent);
/*     */ 
/* 338 */     editBtn.setText("Re-edit");
/* 339 */     editBtn.setLayoutData(getButton(1)
/* 340 */       .getLayoutData());
/* 341 */     editBtn.addSelectionListener(new SelectionListener()
/*     */     {
/*     */       public void widgetDefaultSelected(SelectionEvent e)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 351 */         WatchFormatMsgDlg.this.close();
/*     */       }
/*     */     });
/* 356 */     getButton(0).setText("Save");
/*     */   }
/*     */ 
/*     */   private Products putWatcInProduct(WatchBox wb) {
/* 360 */     Layer defaultLayer = new Layer();
/*     */ 
/* 362 */     defaultLayer.addElement(wb.getParent());
/*     */ 
/* 364 */     Product defaultProduct = new Product();
/* 365 */     defaultProduct.addLayer(defaultLayer);
/*     */ 
/* 367 */     ArrayList prds = new ArrayList();
/* 368 */     prds.add(defaultProduct);
/* 369 */     Products fileProduct = ProductConverter.convert(prds);
/*     */ 
/* 371 */     return fileProduct;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchFormatMsgDlg
 * JD-Core Version:    0.6.2
 */