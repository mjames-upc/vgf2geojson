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
/*     */ import java.io.PrintStream;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.eclipse.jface.dialogs.IDialogConstants;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class CcfpTimeDlg extends AttrDlg
/*     */ {
/*  67 */   private static CcfpTimeDlg INSTANCE = null;
/*     */   private static final int LAYOUT_WIDTH = 14;
/*     */   public static final String PGEN_TYPE_CCFP = "CCFP_SIGMET";
/*     */   public static final String CCFP_ISSUE_TIME = "issue";
/*  86 */   public Map<String, String[]> issueValidTimeMap = new HashMap();
/*     */ 
/*  91 */   public Map<String, String> issueTimeDayMap = new HashMap();
/*     */ 
/*  94 */   protected Composite top = null;
/*     */   private Combo cmbIssTime;
/*     */   private Combo cmbVaTime;
/* 102 */   private String ccfpIssueTime = "";
/*     */ 
/* 105 */   private String ccfpValidTime = "";
/*     */ 
/*     */   public CcfpTimeDlg(Shell parShell) throws VizException {
/* 108 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static CcfpTimeDlg getInstance(Shell parShell)
/*     */   {
/* 113 */     if (INSTANCE == null) {
/*     */       try {
/* 115 */         INSTANCE = new CcfpTimeDlg(parShell);
/*     */       } catch (VizException e) {
/* 117 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 120 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 126 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 128 */     GridLayout mainLayout = new GridLayout(14, false);
/* 129 */     mainLayout.marginHeight = 14;
/* 130 */     mainLayout.marginWidth = 14;
/* 131 */     this.top.setLayout(mainLayout);
/* 132 */     getShell().setText("Collaborative Convective");
/*     */ 
/* 134 */     createTimesArea(this.top);
/*     */ 
/* 136 */     return this.top;
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 143 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/* 144 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*     */ 
/* 146 */     createButton(parent, 0, "Continue", true);
/* 147 */     createButton(parent, 1, 
/* 148 */       IDialogConstants.CANCEL_LABEL, false);
/*     */ 
/* 150 */     getButton(1).setEnabled(true);
/* 151 */     getButton(0).setEnabled(true);
/*     */ 
/* 153 */     getButton(1).setLayoutData(
/* 154 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/* 155 */     getButton(0).setLayoutData(
/* 156 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 162 */     setReturnCode(1);
/* 163 */     close();
/*     */   }
/*     */ 
/*     */   private void createTimesArea(Composite top)
/*     */   {
/* 170 */     Group top_3 = new Group(top, 16384);
/* 171 */     top_3.setLayoutData(new GridData(4, 16777216, true, true, 
/* 172 */       14, 1));
/* 173 */     top_3.setLayout(new GridLayout(14, false));
/*     */ 
/* 177 */     Label lblIssTime = new Label(top_3, 16384);
/* 178 */     lblIssTime.setText("Issue Time: ");
/*     */ 
/* 180 */     fillSpaces(top_3, 16384, 9, true);
/*     */ 
/* 182 */     this.cmbIssTime = new Combo(top_3, 16392);
/* 183 */     this.cmbIssTime.setItems(parseCcfpTimesFile());
/* 184 */     this.cmbIssTime.select(0);
/*     */ 
/* 186 */     fillSpaces(top_3, 16384, 3, true);
/*     */ 
/* 190 */     Label lblVaTime = new Label(top_3, 16384);
/* 191 */     lblVaTime.setText("Valid Time: ");
/*     */ 
/* 193 */     fillSpaces(top_3, 16384, 9, true);
/*     */ 
/* 195 */     String[] vtimes = (String[])this.issueValidTimeMap.get(this.cmbIssTime.getText().trim());
/*     */ 
/* 197 */     this.cmbVaTime = new Combo(top_3, 16392);
/* 198 */     this.cmbVaTime.setItems(vtimes);
/* 199 */     this.cmbVaTime.select(0);
/*     */ 
/* 201 */     fillSpaces(top_3, 16384, 3, true);
/*     */ 
/* 205 */     this.ccfpIssueTime = this.cmbIssTime.getText().trim();
/* 206 */     this.ccfpValidTime = this.cmbVaTime.getText().trim();
/*     */ 
/* 210 */     addCmbListeners();
/*     */   }
/*     */ 
/*     */   public String getCcfpIssueTime()
/*     */   {
/* 215 */     return this.ccfpIssueTime;
/*     */   }
/*     */ 
/*     */   public void setCcfpIssueTime(String ccfpIssueTime) {
/* 219 */     this.ccfpIssueTime = ccfpIssueTime;
/*     */   }
/*     */ 
/*     */   public String getCcfpValidTime() {
/* 223 */     return this.ccfpValidTime;
/*     */   }
/*     */ 
/*     */   public void setCcfpValidTime(String ccfpValidTime) {
/* 227 */     this.ccfpValidTime = ccfpValidTime;
/*     */   }
/*     */ 
/*     */   private void fillSpaces(Composite gp, int dir, int num, boolean empty)
/*     */   {
/* 234 */     for (int i = 0; i < num; i++) {
/* 235 */       Label lbl = new Label(gp, dir);
/* 236 */       lbl.setText(empty ? "" : " ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute ia)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 252 */     Product product = CcfpInfo.getCcfpPrds(this.ccfpIssueTime, this.ccfpValidTime);
/*     */     try
/*     */     {
/* 259 */       activityXML = StorageUtils.serializeProduct(product);
/*     */     }
/*     */     catch (PgenStorageException e1)
/*     */     {
/*     */       String activityXML;
/* 261 */       StorageUtils.showError(e1);
/*     */       return;
/*     */     }
/*     */     String activityXML;
/* 264 */     String txtPrd = CcfpInfo.convertXml2Txt(
/* 265 */       activityXML, 
/* 266 */       PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/* 267 */       PgenStaticDataProvider.getProvider()
/* 268 */       .getPgenLocalizationRoot() + 
/* 269 */       CcfpMsgDlg.PGEN_CCFP_XSLT));
/*     */ 
/* 272 */     cancelPressed();
/*     */ 
/* 275 */     CcfpMsgDlg cmDlg = CcfpMsgDlg.getInstance(getParentShell());
/* 276 */     cmDlg.setIssueTime(this.ccfpIssueTime);
/* 277 */     cmDlg.setValidTime(this.ccfpValidTime);
/* 278 */     cmDlg.setTimeDlg(this);
/* 279 */     cmDlg.setFileContent(txtPrd.trim());
/* 280 */     cmDlg.setProduct(product);
/* 281 */     cmDlg.setFileName(getCcfpIssueTime().trim() + ".ccfp");
/*     */ 
/* 283 */     cmDlg.open();
/*     */   }
/*     */ 
/*     */   private String[] parseCcfpTimesFile()
/*     */   {
/* 295 */     ArrayList issueTimes = new ArrayList();
/* 296 */     ArrayList validTimes = new ArrayList();
/*     */ 
/* 298 */     Document doc = null;
/* 299 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*     */     try
/*     */     {
/* 304 */       File file = PgenStaticDataProvider.getProvider().getFile(
/* 305 */         PgenStaticDataProvider.getProvider()
/* 306 */         .getPgenLocalizationRoot() + "ccfpTimes.xml");
/*     */ 
/* 308 */       DocumentBuilder builder = factory.newDocumentBuilder();
/* 309 */       doc = builder.parse(file.getAbsoluteFile());
/*     */     } catch (Exception e) {
/* 311 */       System.out.println("-----------" + e.getMessage());
/*     */     }
/*     */ 
/* 314 */     NodeList nlist = doc.getElementsByTagNameNS("*", "*");
/*     */ 
/* 316 */     for (int i = 0; i < nlist.getLength(); i++)
/*     */     {
/* 318 */       Node nElem = nlist.item(i);
/*     */ 
/* 321 */       NamedNodeMap nnMap = nElem.getAttributes();
/*     */ 
/* 323 */       ArrayList vtimes = new ArrayList();
/* 324 */       String itime = "";
/*     */ 
/* 326 */       for (int j = 0; j < nnMap.getLength(); j++)
/*     */       {
/* 328 */         Node nAttr = nnMap.item(j);
/*     */ 
/* 331 */         if ("issue".equalsIgnoreCase(nAttr.getNodeName()))
/*     */         {
/* 333 */           itime = nAttr.getNodeValue();
/* 334 */           handleTime(issueTimes, itime, true, itime);
/*     */         }
/*     */         else {
/* 337 */           handleTime(vtimes, nAttr.getNodeValue(), false, itime);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 342 */       validTimes.add((String[])vtimes.toArray(new String[0]));
/*     */     }
/*     */ 
/* 346 */     for (int k = 0; k < issueTimes.size(); k++) {
/* 347 */       this.issueValidTimeMap.put((String)issueTimes.get(k), (String[])validTimes.get(k + 1));
/*     */     }
/* 349 */     return (String[])issueTimes.toArray(new String[0]);
/*     */   }
/*     */ 
/*     */   public void handleTime(ArrayList<String> list, String hour, boolean isIssueTime, String itime)
/*     */   {
/* 359 */     Boolean hourOnly = null; Boolean today = Boolean.valueOf(true); Boolean tomorrow = Boolean.valueOf(false);
/*     */ 
/* 362 */     int fh = 0; int ch = 0; int ih = 0;
/*     */     try {
/* 364 */       ch = Integer.parseInt(getDayOrHour(hourOnly));
/* 365 */       fh = Integer.parseInt(hour);
/* 366 */       ih = Integer.parseInt(itime);
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 370 */     if (isIssueTime) {
/* 371 */       String day = fh + 100 < ch ? getDayOrHour(tomorrow) : 
/* 372 */         getDayOrHour(today);
/* 373 */       list.add(day + "_" + hour);
/* 374 */       this.issueTimeDayMap.put(itime, day);
/*     */     } else {
/* 376 */       list.add((fh < ih ? getDayOrHour(tomorrow) : 
/* 377 */         (String)this.issueTimeDayMap
/* 377 */         .get(itime)) + "_" + hour);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getDayOrHour(Boolean isToday)
/*     */   {
/* 387 */     Calendar today = Calendar.getInstance();
/*     */ 
/* 389 */     Calendar tomorrow = Calendar.getInstance();
/* 390 */     tomorrow.add(5, 1);
/*     */ 
/* 392 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
/* 393 */     sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
/*     */ 
/* 395 */     SimpleDateFormat sdft = new SimpleDateFormat("HH00");
/* 396 */     sdft.setTimeZone(TimeZone.getTimeZone("GMT"));
/*     */ 
/* 398 */     return 
/* 399 */       isToday.booleanValue() ? sdf.format(today.getTime()) : isToday == null ? sdft.format(today.getTime()) : 
/* 399 */       sdf.format(tomorrow
/* 400 */       .getTime());
/*     */   }
/*     */ 
/*     */   private void addCmbListeners()
/*     */   {
/* 408 */     this.cmbIssTime.addListener(13, new Listener()
/*     */     {
/*     */       public void handleEvent(Event e) {
/* 411 */         String it = CcfpTimeDlg.this.cmbIssTime.getText().trim();
/* 412 */         String[] items = (String[])CcfpTimeDlg.this.issueValidTimeMap.get(it);
/* 413 */         CcfpTimeDlg.this.cmbVaTime.setItems(items);
/* 414 */         CcfpTimeDlg.this.cmbVaTime.select(0);
/*     */ 
/* 416 */         CcfpTimeDlg.this.ccfpIssueTime = it;
/*     */       }
/*     */     });
/* 421 */     this.cmbVaTime.addListener(13, new Listener()
/*     */     {
/*     */       public void handleEvent(Event e) {
/* 424 */         CcfpTimeDlg.this.ccfpValidTime = CcfpTimeDlg.this.cmbVaTime.getText().trim();
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.CcfpTimeDlg
 * JD-Core Version:    0.6.2
 */