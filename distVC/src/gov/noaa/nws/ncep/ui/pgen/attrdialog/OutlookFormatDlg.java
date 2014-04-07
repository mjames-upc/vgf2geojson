/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.common.localization.LocalizationFile;
/*      */ import com.raytheon.uf.common.serialization.SerializationUtil;
/*      */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.StationTable;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.clipper.ClipProduct;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import java.io.File;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.TimeZone;
/*      */ import javax.xml.bind.JAXBContext;
/*      */ import javax.xml.bind.Marshaller;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.transform.dom.DOMSource;
/*      */ import org.dom4j.Node;
/*      */ import org.dom4j.io.SAXReader;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.swt.events.ModifyEvent;
/*      */ import org.eclipse.swt.events.ModifyListener;
/*      */ import org.eclipse.swt.events.SelectionAdapter;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.layout.FormAttachment;
/*      */ import org.eclipse.swt.layout.FormData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.DateTime;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ import org.geotools.referencing.GeodeticCalculator;
/*      */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*      */ 
/*      */ public class OutlookFormatDlg extends CaveJFACEDialog
/*      */ {
/*      */   private static org.dom4j.Document otlkTimesTbl;
/*      */   private static HashMap<String, Polygon> bounds;
/*  104 */   private String[] orderedLabels = { "HIGH", "MDT", "SLGT", "TSTM", "NONE", "2%", "5%", "10%", "15%", 
/*  105 */     "25%", "30%", "35%", "40%", "45%", "50%", "60%", "70%", "75%", "HATCHED", "AREA", 
/*  106 */     "ISODRYT", "SCTDRYT", "DRY-TSTM", "ELEVATED", "CRITICAL", "EXTREME", "D3", "D4", "D5", 
/*  107 */     "D6", "D7", "D8", "D3-4", "D3-5", "D3-6", "D3-7", "D3-8", "D4-5", "D4-6", "D4-7", "D4-8", 
/*  108 */     "D5-6", "D5-7", "D5-8", "D6-6", "D6-8", "D7-8" };
/*      */   private OutlookAttrDlg otlkDlg;
/*      */   private OutlookFormatMsgDlg msgDlg;
/*      */   private Outlook otlk;
/*      */   private Composite top;
/*      */   private Button[] dayBtn;
/*  123 */   private static String[] days = { "Day1", "Day2", "Day3", "Day4-8", 
/*  124 */     "Enh00", "Enh04", "Enh12", "Enh16", "Enh20", "Day1 Fire", "Day2 Fire", "Day3-8 Fire" };
/*      */   private DateTime initDate;
/*      */   private org.eclipse.swt.widgets.Text initTime;
/*      */   private DateTime expDate;
/*      */   private org.eclipse.swt.widgets.Text expTime;
/*      */   private Combo forecasterCombo;
/*  136 */   private static String lastForecaster = "";
/*      */ 
/*      */   public OutlookFormatDlg(Shell parentShell, OutlookAttrDlg otlkDlg, Outlook otlk)
/*      */   {
/*  143 */     super(parentShell);
/*  144 */     setShellStyle(96);
/*  145 */     this.otlk = otlk;
/*  146 */     this.otlkDlg = otlkDlg;
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  157 */     this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/*  160 */     getShell().setText("Format Outlook");
/*      */ 
/*  163 */     GridLayout mainLayout = new GridLayout(2, false);
/*  164 */     mainLayout.marginHeight = 3;
/*  165 */     mainLayout.marginWidth = 3;
/*  166 */     mainLayout.verticalSpacing = 3;
/*  167 */     this.top.setLayout(mainLayout);
/*      */ 
/*  170 */     Label dayLbl = new Label(this.top, 16384);
/*  171 */     dayLbl.setText("Day/Prod:");
/*      */ 
/*  173 */     Group dayGrp = new Group(this.top, 16);
/*  174 */     dayGrp.setLayout(new GridLayout(3, false));
/*      */ 
/*  176 */     this.dayBtn = new Button[days.length];
/*      */ 
/*  178 */     for (int ii = 0; ii < days.length; ii++) {
/*  179 */       this.dayBtn[ii] = new Button(dayGrp, 16);
/*  180 */       this.dayBtn[ii].setText(days[ii]);
/*  181 */       this.dayBtn[ii].addSelectionListener(new SelectionAdapter()
/*      */       {
/*      */         public void widgetSelected(SelectionEvent e)
/*      */         {
/*  185 */           if (((Button)e.widget).getSelection()) {
/*  186 */             OutlookFormatDlg.this.setInitDt(OutlookFormatDlg.access$0(OutlookFormatDlg.this, ((Button)e.widget).getText().replaceAll(" Fire", "")));
/*  187 */             OutlookFormatDlg.this.setExpDt(OutlookFormatDlg.access$2(OutlookFormatDlg.this, ((Button)e.widget).getText().replaceAll(" Fire", "")));
/*      */           }
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/*  194 */     this.dayBtn[0].setSelection(true);
/*      */ 
/*  198 */     Label initLbl = new Label(this.top, 0);
/*  199 */     initLbl.setText("Initial Time:");
/*      */ 
/*  201 */     Composite initDt = new Composite(this.top, 0);
/*  202 */     initDt.setLayout(new GridLayout(2, false));
/*  203 */     this.initDate = new DateTime(initDt, 2080);
/*  204 */     this.initTime = new org.eclipse.swt.widgets.Text(initDt, 16779268);
/*      */ 
/*  206 */     FormData fd = new FormData();
/*  207 */     fd.top = new FormAttachment(dayGrp, 2, 1024);
/*  208 */     fd.left = new FormAttachment(this.initDate, 5, 131072);
/*  209 */     this.initTime.setLayoutData(fd);
/*  210 */     PgenUtil.setUTCTimeTextField(initDt, this.initTime, 
/*  211 */       getDefaultInitDT(getDays().replaceAll(" Fire", "")), dayGrp, 5);
/*      */ 
/*  213 */     setInitDt(getDefaultInitDT(getDays().replaceAll(" Fire", "")));
/*      */ 
/*  216 */     Label expLbl = new Label(this.top, 0);
/*  217 */     expLbl.setText("Expiration Time:");
/*  218 */     Composite expDt = new Composite(this.top, 0);
/*  219 */     expDt.setLayout(new GridLayout(2, false));
/*  220 */     this.expDate = new DateTime(expDt, 2080);
/*  221 */     this.expTime = new org.eclipse.swt.widgets.Text(expDt, 16779268);
/*      */ 
/*  223 */     FormData fd2 = new FormData();
/*  224 */     fd2.top = new FormAttachment(this.initTime, 2, 1024);
/*  225 */     fd2.left = new FormAttachment(this.expDate, 5, 131072);
/*  226 */     this.expTime.setLayoutData(fd2);
/*      */ 
/*  228 */     PgenUtil.setUTCTimeTextField(expDt, this.expTime, 
/*  229 */       getDefaultExpDT(getDays().replaceAll(" Fire", "")), dayGrp, 5);
/*      */ 
/*  231 */     setExpDt(getDefaultExpDT(getDays().replaceAll(" Fire", "")));
/*      */ 
/*  234 */     Label fcstLbl = new Label(this.top, 0);
/*  235 */     fcstLbl.setText("Forecaster:");
/*  236 */     this.forecasterCombo = new Combo(this.top, 4);
/*  237 */     WatchCoordDlg.readForecasterTbl();
/*  238 */     for (String str : WatchCoordDlg.getForecasters()) {
/*  239 */       this.forecasterCombo.add(str);
/*      */     }
/*      */ 
/*  242 */     if (!lastForecaster.isEmpty()) {
/*  243 */       this.forecasterCombo.setText(lastForecaster);
/*      */     }
/*      */ 
/*  246 */     this.forecasterCombo.addModifyListener(new ModifyListener()
/*      */     {
/*      */       public void modifyText(ModifyEvent e)
/*      */       {
/*  250 */         OutlookFormatDlg.lastForecaster = ((Combo)e.widget).getText();
/*      */       }
/*      */     });
/*  256 */     Label typeLbl = new Label(this.top, 0);
/*  257 */     typeLbl.setText("Outlook Type:");
/*      */ 
/*  259 */     org.eclipse.swt.widgets.Text typeTxt = new org.eclipse.swt.widgets.Text(this.top, 131076);
/*  260 */     typeTxt.setText(getOutlookType());
/*  261 */     typeTxt.setEditable(false);
/*      */ 
/*  263 */     return this.top;
/*      */   }
/*      */ 
/*      */   public int open()
/*      */   {
/*  273 */     if (getShell() == null) {
/*  274 */       create();
/*      */     }
/*      */ 
/*  277 */     getShell().setLocation(getShell().getParent().getLocation());
/*  278 */     getButton(0).setText("OtlkAll");
/*      */ 
/*  280 */     return super.open();
/*      */   }
/*      */ 
/*      */   public void createButtonsForButtonBar(Composite parent)
/*      */   {
/*  290 */     GridLayout barGl = new GridLayout(3, false);
/*  291 */     parent.setLayout(barGl);
/*      */ 
/*  293 */     Button contBtn = new Button(parent, 8);
/*      */ 
/*  295 */     super.createButtonsForButtonBar(parent);
/*      */ 
/*  297 */     contBtn.setText("Continue");
/*  298 */     contBtn.setLayoutData(getButton(1).getLayoutData());
/*  299 */     contBtn.addSelectionListener(new SelectionAdapter()
/*      */     {
/*      */       public void widgetSelected(SelectionEvent e)
/*      */       {
/*  303 */         long mins = (OutlookFormatDlg.this.getExpTime().getTime().getTime() - OutlookFormatDlg.this.getInitTime().getTime().getTime()) / 60000L;
/*  304 */         String msg = "The duration of your outlook will be " + (int)Math.floor(mins / 60L) + "h " + 
/*  305 */           mins % 60L + "m";
/*  306 */         msg = msg + "\n";
/*  307 */         if (OutlookFormatDlg.this.getInitTime().before(Calendar.getInstance())) {
/*  308 */           long dMins = (Calendar.getInstance().getTime().getTime() - OutlookFormatDlg.this.getInitTime().getTime().getTime()) / 60000L;
/*  309 */           msg = msg + "The outlook became valid " + (int)Math.floor(dMins / 60L) + "h " + 
/*  310 */             dMins % 60L + "m" + " ago.";
/*      */         }
/*      */         else {
/*  313 */           long dMins = (OutlookFormatDlg.this.getInitTime().getTime().getTime() - Calendar.getInstance().getTime().getTime()) / 60000L;
/*  314 */           msg = msg + "The outlook will become valid in " + (int)Math.floor(dMins / 60L) + "h " + 
/*  315 */             dMins % 60L + "m.";
/*      */         }
/*      */ 
/*  319 */         MessageDialog confirmDlg = new MessageDialog(
/*  320 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  321 */           "Warning", null, msg, 
/*  322 */           3, new String[] { "OK", "Cancel" }, 0);
/*  323 */         confirmDlg.open();
/*      */ 
/*  325 */         if (confirmDlg.getReturnCode() == 0) {
/*  326 */           OutlookFormatDlg.this.msgDlg = new OutlookFormatMsgDlg(OutlookFormatDlg.this.getParentShell(), 
/*  327 */             OutlookFormatDlg.this, OutlookFormatDlg.this.otlk, OutlookFormatDlg.this.otlkDlg.drawingLayer.getActiveLayer());
/*  328 */           OutlookFormatDlg.this.msgDlg.setBlockOnOpen(true);
/*  329 */           OutlookFormatDlg.this.msgDlg.setMessage(OutlookFormatDlg.this.formatOtlk(OutlookFormatDlg.this.otlk, OutlookFormatDlg.this.otlkDlg.drawingLayer.getActiveLayer()));
/*  330 */           int rt = OutlookFormatDlg.this.msgDlg.open();
/*  331 */           if (rt == 0) OutlookFormatDlg.this.cleanup();
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public Calendar getExpTime()
/*      */   {
/*  351 */     Calendar expiration = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*  352 */     expiration.set(this.expDate.getYear(), this.expDate.getMonth(), this.expDate.getDay(), 
/*  353 */       getHourFromTextField(this.expTime), 
/*  354 */       getMinuteFromTextField(this.expTime), 0);
/*  355 */     expiration.set(14, 0);
/*  356 */     return expiration;
/*      */   }
/*      */ 
/*      */   public Calendar getInitTime()
/*      */   {
/*  364 */     Calendar init = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*  365 */     init.set(this.initDate.getYear(), this.initDate.getMonth(), this.initDate.getDay(), 
/*  366 */       getHourFromTextField(this.initTime), 
/*  367 */       getMinuteFromTextField(this.initTime), 0);
/*      */ 
/*  369 */     init.set(14, 0);
/*      */ 
/*  371 */     return init;
/*      */   }
/*      */ 
/*      */   protected void okPressed()
/*      */   {
/*  381 */     otlkAll();
/*      */   }
/*      */ 
/*      */   private void otlkAll()
/*      */   {
/*      */     Iterator localIterator2;
/*  389 */     for (Iterator localIterator1 = this.otlkDlg.drawingLayer.getProducts().iterator(); localIterator1.hasNext(); 
/*  390 */       localIterator2.hasNext())
/*      */     {
/*  389 */       Product pd = (Product)localIterator1.next();
/*  390 */       localIterator2 = pd.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/*  391 */       Iterator it = layer.getComponentIterator();
/*  392 */       label181: for (; it.hasNext(); 
/*  407 */         return)
/*      */       {
/*  393 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/*  394 */         if ((!adc.getName().equalsIgnoreCase("Outlook")) || 
/*  395 */           (!adc.getPgenType().equalsIgnoreCase("hail")))
/*      */           break label181;
/*  397 */         if (this.msgDlg != null) {
/*  398 */           this.msgDlg.close();
/*      */         }
/*  400 */         this.msgDlg = new OutlookFormatMsgDlg(getParentShell(), 
/*  401 */           this, (Outlook)adc, layer);
/*  402 */         this.msgDlg.setBlockOnOpen(true);
/*  403 */         this.msgDlg.setMessage(formatOtlk((Outlook)adc, layer));
/*      */ 
/*  405 */         int rt = this.msgDlg.open();
/*      */ 
/*  407 */         if (rt != 1)
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  423 */     for (localIterator1 = this.otlkDlg.drawingLayer.getProducts().iterator(); localIterator1.hasNext(); 
/*  424 */       localIterator2.hasNext())
/*      */     {
/*  423 */       Product pd = (Product)localIterator1.next();
/*  424 */       localIterator2 = pd.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/*  425 */       Iterator it = layer.getComponentIterator();
/*  426 */       Outlook lk = null;
/*  427 */       boolean hailFound = false;
/*      */ 
/*  429 */       while (it.hasNext()) {
/*  430 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/*  431 */         if ((adc.getName().equalsIgnoreCase("Outlook")) && 
/*  432 */           (!adc.getPgenType().equalsIgnoreCase("hail")))
/*      */         {
/*  434 */           lk = (Outlook)adc;
/*  435 */           break;
/*      */         }
/*  437 */         if ((adc.getName().equalsIgnoreCase("Outlook")) && 
/*  438 */           (adc.getPgenType().equalsIgnoreCase("hail"))) {
/*  439 */           hailFound = true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  445 */       if (!hailFound)
/*      */       {
/*  447 */         if (this.msgDlg != null) {
/*  448 */           this.msgDlg.close();
/*      */         }
/*  450 */         this.msgDlg = new OutlookFormatMsgDlg(getParentShell(), 
/*  451 */           this, lk, layer);
/*  452 */         this.msgDlg.setBlockOnOpen(true);
/*  453 */         this.msgDlg.setMessage(formatOtlk(lk, layer));
/*      */ 
/*  455 */         int rt = this.msgDlg.open();
/*      */ 
/*  457 */         if (rt == 1) return;
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  468 */     cleanup();
/*      */   }
/*      */ 
/*      */   private String generateOutlookMsg(Outlook ol, Layer layer)
/*      */   {
/*  487 */     String msg = "";
/*      */ 
/*  489 */     if (ol == null)
/*      */     {
/*  492 */       msg = msg + formatDays().toUpperCase() + "\n";
/*      */ 
/*  495 */       msg = msg + getForecaster().toUpperCase() + "\n";
/*      */ 
/*  498 */       msg = msg + String.format("%1$td%1$tH%1$tMZ", new Object[] { getInitTime() }) + " - " + 
/*  499 */         String.format("%1$td%1$tH%1$tMZ", new Object[] { getExpTime() }) + "\n";
/*      */     }
/*      */     else {
/*  502 */       Layer defaultLayer = this.otlkDlg.drawingLayer.getActiveLayer().copy();
/*  503 */       defaultLayer.clear();
/*      */ 
/*  505 */       Product defaultProduct = new Product();
/*  506 */       defaultProduct.addLayer(defaultLayer);
/*      */ 
/*  508 */       String pdName = this.otlkDlg.drawingLayer.getActiveProduct().getType();
/*  509 */       ProductType pt = (ProductType)ProductConfigureDialog.getProductTypes().get(pdName);
/*      */ 
/*  511 */       Polygon boundsPoly = null;
/*      */ 
/*  513 */       if ((pt != null) && (pt.getClipFlag() != null) && (pt.getClipFlag().booleanValue())) {
/*  514 */         boundsPoly = getBoundsPoly(pt.getClipBoundsTable(), pt.getClipBoundsName());
/*  515 */         if (boundsPoly != null) {
/*  516 */           processClip(ol, defaultLayer, boundsPoly);
/*      */         }
/*      */       }
/*      */ 
/*  520 */       if ((pt == null) || (pt.getClipFlag() == null) || (!pt.getClipFlag().booleanValue()) || (boundsPoly == null))
/*      */       {
/*  522 */         defaultLayer.addElement(issueOutlook(ol));
/*      */       }
/*      */ 
/*  526 */       ArrayList prds = new ArrayList();
/*  527 */       prds.add(defaultProduct);
/*  528 */       Products fileProduct = ProductConverter.convert(prds);
/*      */ 
/*  530 */       org.w3c.dom.Document sw = null;
/*      */       try
/*      */       {
/*  533 */         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  534 */         dbf.setNamespaceAware(true);
/*  535 */         DocumentBuilder db = dbf.newDocumentBuilder();
/*  536 */         sw = db.newDocument();
/*  537 */         Marshaller mar = SerializationUtil.getJaxbContext().createMarshaller();
/*  538 */         mar.marshal(fileProduct, sw);
/*      */       } catch (Exception e) {
/*  540 */         e.printStackTrace();
/*      */       }
/*      */ 
/*  543 */       DOMSource ds = new DOMSource(sw);
/*      */ 
/*  546 */       String xsltPath = PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + File.separator + "xslt" + File.separator + "outlook" + File.separator + "Outlook.xlt";
/*      */ 
/*  548 */       LocalizationFile lFile = PgenStaticDataProvider.getProvider().getStaticLocalizationFile(xsltPath);
/*      */ 
/*  550 */       if (lFile != null) {
/*  551 */         msg = PgenUtil.applyStyleSheet(ds, lFile.getFile().getAbsolutePath());
/*      */       }
/*      */ 
/*  555 */       Iterator it = layer.getComponentIterator();
/*  556 */       while (it.hasNext()) {
/*  557 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/*  558 */         if (((adc instanceof Outlook)) && 
/*  559 */           (!((Outlook)adc).getOutlookType().equalsIgnoreCase(ol.getOutlookType()))) {
/*  560 */           MessageDialog warningDlg = new MessageDialog(
/*  561 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  562 */             "Warning!", null, "More than one outlook types are found in one layer!", 
/*  563 */             2, new String[] { "OK" }, 0);
/*  564 */           warningDlg.open();
/*  565 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  570 */     return msg;
/*      */   }
/*      */ 
/*      */   public String formatDays()
/*      */   {
/*  579 */     String dayStr = "";
/*      */ 
/*  581 */     for (Button btn : this.dayBtn) {
/*  582 */       if (btn.getSelection()) {
/*  583 */         dayStr = btn.getText();
/*  584 */         if (dayStr.contains("Enh")) {
/*  585 */           dayStr = "Day1";
/*  586 */           break;
/*      */         }
/*  588 */         dayStr = dayStr.replace("-", "").replace("Fire", "");
/*      */ 
/*  590 */         break;
/*      */       }
/*      */     }
/*      */ 
/*  594 */     return dayStr;
/*      */   }
/*      */ 
/*      */   public String formatOtlk(Outlook ol, Layer layer)
/*      */   {
/*  602 */     if (ol != null) this.otlkDlg.showContLines(ol);
/*  603 */     return generateOutlookMsg(ol, layer);
/*      */   }
/*      */ 
/*      */   public String getForecaster()
/*      */   {
/*  611 */     return this.forecasterCombo.getText();
/*      */   }
/*      */ 
/*      */   public String getDays()
/*      */   {
/*  619 */     for (Button btn : this.dayBtn) {
/*  620 */       if (btn.getSelection()) {
/*  621 */         return btn.getText();
/*      */       }
/*      */     }
/*  624 */     return "";
/*      */   }
/*      */ 
/*      */   private org.dom4j.Document readOtlkTimesTbl()
/*      */   {
/*  633 */     if (otlkTimesTbl == null) {
/*      */       try {
/*  635 */         String outlookTimesFile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/*  636 */           PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "outlooktimes.xml");
/*      */ 
/*  638 */         SAXReader reader = new SAXReader();
/*  639 */         otlkTimesTbl = reader.read(outlookTimesFile);
/*      */       } catch (Exception e) {
/*  641 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  645 */     return otlkTimesTbl;
/*      */   }
/*      */ 
/*      */   private Calendar getDefaultInitDT(String days)
/*      */   {
/*  653 */     String xpath = "/root/days[@name='" + days.toUpperCase() + "']";
/*      */ 
/*  655 */     Node day = readOtlkTimesTbl().selectSingleNode(xpath);
/*  656 */     List nodes = day.selectNodes("range");
/*      */ 
/*  658 */     Calendar curDt = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*  659 */     int minutes = curDt.get(11) * 60 + curDt.get(12);
/*  660 */     int initAdjDay = 0;
/*  661 */     int initHH = 0;
/*  662 */     int initMM = 0;
/*      */ 
/*  664 */     for (Node node : nodes) {
/*      */       try {
/*  666 */         int from = Integer.valueOf(node.valueOf("@from").substring(0, 2)).intValue() * 60 + 
/*  667 */           Integer.valueOf(node.valueOf("@from").substring(2)).intValue();
/*  668 */         int to = Integer.valueOf(node.valueOf("@to").substring(0, 2)).intValue() * 60 + 
/*  669 */           Integer.valueOf(node.valueOf("@to").substring(2)).intValue();
/*  670 */         if ((minutes > from) && (minutes <= to)) {
/*  671 */           initHH = Integer.valueOf(node.valueOf("@init").substring(0, 2)).intValue();
/*  672 */           initMM = Integer.valueOf(node.valueOf("@init").substring(2)).intValue();
/*  673 */           initAdjDay = Integer.valueOf(node.valueOf("@initAdj")).intValue();
/*      */         }
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  682 */     curDt.add(5, initAdjDay);
/*  683 */     curDt.set(11, initHH);
/*  684 */     curDt.set(12, initMM);
/*      */ 
/*  686 */     return curDt;
/*      */   }
/*      */ 
/*      */   private Calendar getDefaultExpDT(String days)
/*      */   {
/*  696 */     String xpath = "/root/days[@name='" + days.toUpperCase() + "']";
/*      */ 
/*  698 */     Node day = readOtlkTimesTbl().selectSingleNode(xpath);
/*  699 */     List nodes = day.selectNodes("range");
/*      */ 
/*  701 */     Calendar curDt = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*  702 */     int minutes = curDt.get(11) * 60 + curDt.get(12);
/*  703 */     int expAdjDay = 0;
/*  704 */     int expHH = 0;
/*  705 */     int expMM = 0;
/*      */ 
/*  707 */     for (Node node : nodes) {
/*      */       try {
/*  709 */         int from = Integer.valueOf(node.valueOf("@from").substring(0, 2)).intValue() * 60 + 
/*  710 */           Integer.valueOf(node.valueOf("@from").substring(2)).intValue();
/*  711 */         int to = Integer.valueOf(node.valueOf("@to").substring(0, 2)).intValue() * 60 + 
/*  712 */           Integer.valueOf(node.valueOf("@to").substring(2)).intValue();
/*  713 */         if ((minutes > from) && (minutes <= to)) {
/*  714 */           expHH = Integer.valueOf(node.valueOf("@exp").substring(0, 2)).intValue();
/*  715 */           expMM = Integer.valueOf(node.valueOf("@exp").substring(2)).intValue();
/*  716 */           expAdjDay = Integer.valueOf(node.valueOf("@expAdj")).intValue();
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  726 */     curDt.add(5, expAdjDay);
/*  727 */     curDt.set(11, expHH);
/*  728 */     curDt.set(12, expMM);
/*      */ 
/*  730 */     return curDt;
/*      */   }
/*      */ 
/*      */   private void setInitDt(Calendar cal)
/*      */   {
/*  738 */     this.initDate.setYear(cal.get(1));
/*  739 */     this.initDate.setMonth(cal.get(2));
/*  740 */     this.initDate.setDay(cal.get(5));
/*  741 */     this.initTime.setText(String.format("%1$tH%1$tM", new Object[] { cal }));
/*      */   }
/*      */ 
/*      */   private void setExpDt(Calendar cal)
/*      */   {
/*  749 */     this.expDate.setYear(cal.get(1));
/*  750 */     this.expDate.setMonth(cal.get(2));
/*  751 */     this.expDate.setDay(cal.get(5));
/*  752 */     this.expTime.setText(String.format("%1$tH%1$tM", new Object[] { cal }));
/*      */   }
/*      */ 
/*      */   public Outlook issueOutlook(Outlook ol)
/*      */   {
/*  760 */     if (ol != null) {
/*  761 */       reorder(ol);
/*  762 */       ol.setForecaster(getForecaster().toUpperCase());
/*  763 */       ol.setDays(getDays().toUpperCase());
/*  764 */       ol.setIssueTime(getInitTime());
/*  765 */       ol.setExpirationTime(getExpTime());
/*  766 */       ol.setLineInfo(generateLineInfo(ol, "new_line"));
/*      */     }
/*  768 */     return ol;
/*      */   }
/*      */ 
/*      */   public OutlookAttrDlg getOtlkDlg() {
/*  772 */     return this.otlkDlg;
/*      */   }
/*      */ 
/*      */   public void setOtlkDlg(OutlookAttrDlg otlkDlg) {
/*  776 */     this.otlkDlg = otlkDlg;
/*      */   }
/*      */ 
/*      */   private int getHourFromTextField(org.eclipse.swt.widgets.Text txt)
/*      */   {
/*  784 */     int ret = 0;
/*      */     try {
/*  786 */       String hm = txt.getText();
/*  787 */       ret = Integer.parseInt(hm.substring(0, hm.length() == 4 ? 2 : 1));
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*  792 */     return ret;
/*      */   }
/*      */ 
/*      */   private int getMinuteFromTextField(org.eclipse.swt.widgets.Text txt)
/*      */   {
/*  799 */     int ret = 0;
/*      */     try {
/*  801 */       String hm = txt.getText();
/*  802 */       ret = Integer.parseInt(hm.substring(hm.length() == 4 ? 2 : 1));
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*  807 */     return ret;
/*      */   }
/*      */ 
/*      */   private void cleanup()
/*      */   {
/*  814 */     close();
/*  815 */     getOtlkDlg().drawingLayer.removeSelected();
/*  816 */     getOtlkDlg().mapEditor.refresh();
/*  817 */     getOtlkDlg().close();
/*  818 */     PgenUtil.setSelectingMode();
/*      */   }
/*      */ 
/*      */   private void processClip(Outlook ol, Layer layer, Polygon boundsPoly)
/*      */   {
/*  836 */     Outlook clipped = new ClipProduct(boundsPoly, true).clipOutlook(issueOutlook(ol));
/*      */ 
/*  839 */     layer.addElement(clipped);
/*  840 */     issueOutlook(clipped);
/*      */ 
/*  843 */     this.otlkDlg.drawingLayer.replaceElement(ol, clipped);
/*  844 */     this.otlk = clipped;
/*      */ 
/*  847 */     if ((!clipped.isEmpty()) && ((clipped.getPrimaryDE() instanceof Line))) {
/*  848 */       this.otlkDlg.setDrawableElement(clipped.getPrimaryDE());
/*  849 */       if ((this.otlkDlg.drawingLayer.getSelectedComp() != null) && (!clipped.isEmpty())) {
/*  850 */         this.otlkDlg.drawingLayer.setSelected(clipped.getPrimaryDE());
/*      */       }
/*  852 */       this.otlkDlg.drawingLayer.removeGhostLine();
/*  853 */       this.otlkDlg.mapEditor.refresh();
/*      */     }
/*  855 */     else if (clipped.isEmpty()) {
/*  856 */       this.otlkDlg.drawingLayer.removeSelected();
/*  857 */       this.otlkDlg.drawingLayer.removeGhostLine();
/*  858 */       this.otlkDlg.mapEditor.refresh();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Polygon getBoundsPoly(String boundsTbl, String boundsName)
/*      */   {
/*  870 */     if (bounds == null) {
/*  871 */       bounds = new HashMap();
/*      */     }
/*      */ 
/*  875 */     Polygon boundsPoly = (Polygon)bounds.get(boundsTbl + boundsName);
/*      */ 
/*  878 */     if (boundsPoly == null) {
/*  879 */       boundsPoly = PgenStaticDataProvider.getProvider().loadBounds(boundsTbl, boundsName);
/*  880 */       if (boundsPoly != null)
/*      */       {
/*  882 */         bounds.clear();
/*  883 */         bounds.put(boundsTbl + boundsName, boundsPoly);
/*      */       }
/*      */     }
/*      */ 
/*  887 */     return boundsPoly;
/*      */   }
/*      */ 
/*      */   private String generateLineInfo(Outlook ol, String lineBreaker)
/*      */   {
/*  899 */     if (ol.getOutlookType().equalsIgnoreCase("EXCE_RAIN")) return excessiveRain(ol, lineBreaker);
/*      */ 
/*  901 */     String lnInfo = "";
/*      */ 
/*  903 */     List anchors = PgenStaticDataProvider.getProvider().getAnchorTbl().getStationList();
/*      */ 
/*  905 */     Iterator it = ol.getComponentIterator();
/*  906 */     while (it.hasNext()) {
/*  907 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/*      */       Coordinate pt;
/*  908 */       if (adc.getName().equalsIgnoreCase(Outlook.OUTLOOK_LABELED_LINE)) {
/*  909 */         Iterator itDe = ((DECollection)adc).createDEIterator();
/*  910 */         List lines = new ArrayList();
/*  911 */         gov.noaa.nws.ncep.ui.pgen.elements.Text txt = null;
/*  912 */         while (itDe.hasNext()) {
/*  913 */           DrawableElement de = (DrawableElement)itDe.next();
/*  914 */           if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text)) txt = (gov.noaa.nws.ncep.ui.pgen.elements.Text)de;
/*  915 */           else if ((de instanceof Line)) lines.add((Line)de);
/*      */         }
/*      */ 
/*  918 */         String lblInfo = "";
/*  919 */         if (txt == null) {
/*  920 */           lblInfo = lblInfo + "LABEL -1 -1" + lineBreaker;
/*      */         }
/*      */         else
/*      */         {
/*  924 */           lblInfo = this.otlkDlg.getTextForLabel(ol.getOutlookType(), txt.getText()[0]);
/*      */ 
/*  928 */           lblInfo = lblInfo + lineBreaker;
/*  929 */           lblInfo = lblInfo + "LABEL " + String.format("%1$5.2f %2$5.2f", new Object[] { Double.valueOf(txt.getLocation().y), Double.valueOf(txt.getLocation().x) });
/*  930 */           lblInfo = lblInfo + lineBreaker;
/*      */         }
/*      */ 
/*  933 */         if (!lines.isEmpty()) {
/*  934 */           for (Line ln : lines) {
/*  935 */             lnInfo = lnInfo + lblInfo;
/*      */ 
/*  937 */             ArrayList points = new ArrayList();
/*  938 */             points.addAll(ln.getPoints());
/*      */ 
/*  940 */             if (ln.isClosedLine().booleanValue()) {
/*  941 */               points.add((Coordinate)points.get(0));
/*      */             }
/*      */ 
/*  944 */             for (Iterator localIterator2 = points.iterator(); localIterator2.hasNext(); ) { pt = (Coordinate)localIterator2.next();
/*  945 */               Station st = WatchBox.getNearestAnchorPt(pt, anchors);
/*  946 */               GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*      */ 
/*  948 */               gc.setStartingGeographicPoint(st.getLongitude().floatValue(), st.getLatitude().floatValue());
/*  949 */               gc.setDestinationGeographicPoint(pt.x, pt.y);
/*      */ 
/*  951 */               long dist = Math.round(gc.getOrthodromicDistance() / 1609.3399658203125D);
/*  952 */               long dir = Math.round(gc.getAzimuth());
/*  953 */               if (dir < 0L) dir += 360L;
/*      */ 
/*  955 */               lnInfo = lnInfo + String.format("%1$5.2f %2$7.2f%3$4d %4$-3s%5$4s", new Object[] { Double.valueOf(pt.y), Double.valueOf(pt.x), Long.valueOf(dist), 
/*  956 */                 WatchBox.dirs[((int)Math.round(dir / 22.5D))], st.getStid() });
/*  957 */               lnInfo = lnInfo + lineBreaker;
/*      */             }
/*      */ 
/*  960 */             lnInfo = lnInfo + "$$" + lineBreaker;
/*      */           }
/*      */         }
/*      */       }
/*  964 */       else if (adc.getName().equalsIgnoreCase(Outlook.OUTLOOK_LINE_GROUP)) {
/*  965 */         Iterator itDe = ((DECollection)adc).createDEIterator();
/*  966 */         ArrayList lns = new ArrayList();
/*  967 */         gov.noaa.nws.ncep.ui.pgen.elements.Text txt = null;
/*  968 */         while (itDe.hasNext()) {
/*  969 */           DrawableElement de = (DrawableElement)itDe.next();
/*  970 */           if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text)) txt = (gov.noaa.nws.ncep.ui.pgen.elements.Text)de;
/*  971 */           else if ((de instanceof Line)) lns.add((Line)de);
/*      */         }
/*      */ 
/*  974 */         if (txt == null) {
/*  975 */           lnInfo = lnInfo + "LABEL -1 -1" + lineBreaker;
/*      */         }
/*      */         else {
/*  978 */           lnInfo = lnInfo + txt.getText()[0] + lineBreaker;
/*  979 */           lnInfo = lnInfo + "LABEL " + String.format("%1$5.2f %2$5.2f", new Object[] { Double.valueOf(txt.getLocation().y), Double.valueOf(txt.getLocation().x) });
/*  980 */           lnInfo = lnInfo + lineBreaker;
/*      */         }
/*      */ 
/*  983 */         int iLines = 0;
/*  984 */         for (Line ln : lns) {
/*  985 */           iLines++;
/*  986 */           for (Coordinate pt : ln.getPoints()) {
/*  987 */             Station st = WatchBox.getNearestAnchorPt(pt, anchors);
/*  988 */             GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*      */ 
/*  990 */             gc.setStartingGeographicPoint(st.getLongitude().floatValue(), st.getLatitude().floatValue());
/*  991 */             gc.setDestinationGeographicPoint(pt.x, pt.y);
/*      */ 
/*  993 */             long dist = Math.round(gc.getOrthodromicDistance() / 1609.3399658203125D);
/*  994 */             long dir = Math.round(gc.getAzimuth());
/*  995 */             if (dir < 0L) dir += 360L;
/*      */ 
/*  997 */             lnInfo = lnInfo + String.format("%1$5.2f %2$7.2f%3$4d %4$-3s%5$4s", new Object[] { Double.valueOf(pt.y), Double.valueOf(pt.x), Long.valueOf(dist), 
/*  998 */               WatchBox.dirs[((int)Math.round(dir / 22.5D))], st.getStid() });
/*  999 */             lnInfo = lnInfo + lineBreaker;
/*      */           }
/*      */ 
/* 1002 */           if (iLines < lns.size()) lnInfo = lnInfo + "...CONT..." + lineBreaker;
/*      */         }
/*      */ 
/* 1005 */         lnInfo = lnInfo + "$$" + lineBreaker;
/*      */       }
/*      */     }
/*      */ 
/* 1009 */     return lnInfo;
/*      */   }
/*      */ 
/*      */   private String excessiveRain(Outlook ol, String lineBreaker) {
/* 1013 */     String ret = "";
/*      */ 
/* 1015 */     String rainTxt = "RISK OF RAINFALL EXCEEDING FFG TO THE RIGHT OF A LINE FROM";
/* 1016 */     String fiveInch = "TOTAL RAINFALL AMOUNTS OF FIVE INCHES WILL BE POSSIBLE TO THE RIGHT OF A LINE FROM";
/*      */ 
/* 1018 */     List sfstns = PgenStaticDataProvider.getProvider().getSfStnTbl().getStationList();
/*      */ 
/* 1020 */     Iterator it = ol.getComponentIterator();
/* 1021 */     while (it.hasNext()) {
/* 1022 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 1023 */       if (adc.getName().equalsIgnoreCase(Outlook.OUTLOOK_LABELED_LINE)) {
/* 1024 */         Iterator itDe = ((DECollection)adc).createDEIterator();
/* 1025 */         List lines = new ArrayList();
/* 1026 */         gov.noaa.nws.ncep.ui.pgen.elements.Text txt = null;
/* 1027 */         while (itDe.hasNext()) {
/* 1028 */           DrawableElement de = (DrawableElement)itDe.next();
/* 1029 */           if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text)) txt = (gov.noaa.nws.ncep.ui.pgen.elements.Text)de;
/* 1030 */           else if ((de instanceof Line)) lines.add((Line)de);
/*      */         }
/*      */ 
/* 1033 */         String lblInfo = "";
/* 1034 */         if (txt == null) {
/*      */           break;
/*      */         }
/* 1037 */         if (txt.getString()[0].equalsIgnoreCase("5 INCH")) {
/* 1038 */           ret = ret + fiveInch;
/*      */         }
/* 1040 */         else if (txt.getString()[0].equalsIgnoreCase("SLGT")) {
/* 1041 */           lblInfo = lblInfo + "SLIGHT ";
/*      */         }
/* 1043 */         else if (txt.getString()[0].equalsIgnoreCase("MDT")) {
/* 1044 */           lblInfo = lblInfo + "MODERATE ";
/*      */         }
/* 1046 */         else if (txt.getString()[0].equalsIgnoreCase("HIGH")) {
/* 1047 */           lblInfo = lblInfo + "HIGH ";
/*      */         }
/*      */ 
/* 1050 */         if (!lblInfo.isEmpty()) ret = ret + lblInfo + rainTxt;
/*      */ 
/* 1052 */         if (!lines.isEmpty())
/*      */         {
/*      */           Iterator localIterator2;
/* 1053 */           for (Iterator localIterator1 = lines.iterator(); localIterator1.hasNext(); 
/* 1056 */             localIterator2.hasNext())
/*      */           {
/* 1053 */             Line ln = (Line)localIterator1.next();
/* 1054 */             ArrayList pts = ln.getPoints();
/* 1055 */             if (ln.isClosedLine().booleanValue()) pts.add((Coordinate)pts.get(0));
/* 1056 */             localIterator2 = pts.iterator(); continue; Coordinate pt = (Coordinate)localIterator2.next();
/* 1057 */             Station st = WatchBox.getNearestAnchorPt(pt, sfstns);
/* 1058 */             GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*      */ 
/* 1060 */             gc.setStartingGeographicPoint(st.getLongitude().floatValue(), st.getLatitude().floatValue());
/* 1061 */             gc.setDestinationGeographicPoint(pt.x, pt.y);
/*      */ 
/* 1063 */             long dist = Math.round(gc.getOrthodromicDistance() / 1609.3399658203125D);
/* 1064 */             long dir = Math.round(gc.getAzimuth());
/* 1065 */             if (dir < 0L) dir += 360L;
/*      */ 
/* 1067 */             ret = ret + String.format(" %1$d %2$s %3$s", new Object[] { Integer.valueOf((int)(dist / 5L * 5L)), 
/* 1068 */               WatchBox.dirs[((int)Math.round(dir / 22.5D))], st.getStid() });
/*      */           }
/*      */ 
/* 1072 */           ret = ret + "\n";
/*      */         }
/*      */       }
/* 1075 */       ret = ret + "\n";
/*      */     }
/*      */ 
/* 1078 */     return PgenUtil.wrap(ret, 65, "\n", false);
/*      */   }
/*      */ 
/*      */   private void reorder(Outlook otlk)
/*      */   {
/* 1087 */     List ordered = new ArrayList();
/*      */ 
/* 1089 */     for (String str : this.orderedLabels) {
/* 1090 */       int ii = 0;
/* 1091 */       Iterator it = otlk.getComponentIterator();
/* 1092 */       while (it.hasNext()) {
/* 1093 */         boolean found = false;
/* 1094 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 1095 */         Iterator it1 = null;
/*      */ 
/* 1097 */         if (adc.getName().equalsIgnoreCase(
/* 1097 */           Outlook.OUTLOOK_LABELED_LINE)) {
/* 1098 */           it1 = ((DECollection)adc).getComponentIterator();
/*      */         }
/* 1101 */         else if (adc.getName().equalsIgnoreCase(
/* 1101 */           Outlook.OUTLOOK_LINE_GROUP))
/*      */         {
/* 1103 */           if (((DECollection)adc).getItemAt(0).getName().equalsIgnoreCase(
/* 1103 */             Outlook.OUTLOOK_LABELED_LINE)) {
/* 1104 */             it1 = ((DECollection)((DECollection)adc).getItemAt(0)).getComponentIterator();
/*      */           }
/*      */         }
/*      */ 
/* 1108 */         if (it1 == null);
/* 1110 */         while (it1.hasNext()) {
/* 1111 */           AbstractDrawableComponent adcInside = (AbstractDrawableComponent)it1.next();
/*      */ 
/* 1113 */           if (((adcInside instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text)) && 
/* 1114 */             (((gov.noaa.nws.ncep.ui.pgen.elements.Text)adcInside).getText()[0].equalsIgnoreCase(str))) {
/* 1115 */             found = true;
/* 1116 */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1121 */         if (found) {
/* 1122 */           ordered.add(adc);
/* 1123 */           it.remove();
/* 1124 */           ii++;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1130 */     if (!ordered.isEmpty())
/* 1131 */       otlk.add(ordered);
/*      */   }
/*      */ 
/*      */   private String getOutlookType()
/*      */   {
/* 1136 */     if (this.otlk != null) {
/* 1137 */       return this.otlk.getOutlookType();
/*      */     }
/*      */ 
/* 1140 */     return this.otlkDlg.getOutlookType();
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.OutlookFormatDlg
 * JD-Core Version:    0.6.2
 */