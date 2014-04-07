/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*      */ 
/*      */ import com.raytheon.uf.common.serialization.SerializationUtil;
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.ISigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo.ProductInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VolcanoAshCloud;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.PrintStream;
/*      */ import java.io.StringReader;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.xml.bind.JAXBException;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerFactory;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import javax.xml.transform.stream.StreamSource;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.swt.events.ModifyEvent;
/*      */ import org.eclipse.swt.events.ModifyListener;
/*      */ import org.eclipse.swt.events.VerifyEvent;
/*      */ import org.eclipse.swt.events.VerifyListener;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Listener;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class VolcanoVaaAttrDlg extends AttrDlg
/*      */   implements ISigmet
/*      */ {
/*      */   private static VolcanoVaaAttrDlg INSTANCE;
/*  100 */   private static int FORMAT_ID = 20100201;
/*      */ 
/*  105 */   private static int RESET_ID = 20100202;
/*      */ 
/*  108 */   private static final String PGEN_VAA_XSLT = "xslt" + File.separator + "vaa" + 
/*  108 */     File.separator + "vaaXml2Txt.xslt";
/*      */ 
/*  113 */   protected Composite top = null;
/*      */ 
/*  118 */   private Volcano volcano = null;
/*      */ 
/*  123 */   Label lblLocText = null;
/*      */ 
/*  128 */   Label lblAreaText = null;
/*      */ 
/*  133 */   Label lblElevText = null;
/*      */ 
/*  138 */   Combo comboStn = null;
/*      */ 
/*  143 */   Combo comboId = null;
/*      */ 
/*  148 */   Combo comboHdr = null;
/*      */ 
/*  153 */   Combo comboType = null;
/*      */ 
/*  158 */   Text txtYear = null;
/*      */ 
/*  163 */   Text txtAdNo = null;
/*      */ 
/*  168 */   Text txtCorr = null;
/*      */ 
/*  173 */   Text txtInfoSour = null;
/*      */ 
/*  178 */   Text txtAddInfoSour = null;
/*      */ 
/*  183 */   Text txtErup = null;
/*      */ 
/*  188 */   Text txtAshDate = null;
/*      */ 
/*  193 */   Text txtAshTime = null;
/*      */ 
/*  198 */   Text txtRemark = null;
/*      */ 
/*  203 */   Combo comboAviColoCode = null;
/*      */ 
/*  208 */   Combo comboNextAdv = null;
/*      */ 
/*  213 */   Combo comboForecaster = null;
/*      */ 
/*  218 */   org.eclipse.swt.widgets.List listInfoSour = null;
/*      */ 
/*  223 */   private boolean fromEditDlg = true;
/*      */ 
/*  228 */   private boolean fromSelection = true;
/*      */ 
/*  233 */   private Button btnNil = null;
/*      */ 
/*  238 */   private Button btnCloudInfo = null;
/*      */ 
/*  243 */   private Button btnCorr = null;
/*      */ 
/*  248 */   private String[] type = VaaInfo.ProductInfo.getProduct(VaaInfo.LOCS[0]);
/*      */ 
/*  253 */   private String elevFootMeterTxt = "";
/*      */ 
/*  258 */   private String volXMLFileName = "";
/*      */ 
/*  263 */   private java.util.List<VolcanoAshCloud> vacList = new ArrayList();
/*      */ 
/*  268 */   private GridData singleTxtGridData = new GridData(58, 16);
/*      */ 
/*  273 */   private TxtVerifyListener tvl = new TxtVerifyListener(null);
/*      */ 
/*  278 */   private String vacInfoFhr6 = null;
/*      */ 
/*  283 */   private String vacInfoFhr12 = null;
/*      */ 
/*  288 */   private String vacInfoFhr18 = null;
/*      */ 
/*      */   public VolcanoVaaAttrDlg(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  298 */     super(parShell);
/*      */   }
/*      */ 
/*      */   public static VolcanoVaaAttrDlg getInstance(Shell parShell)
/*      */   {
/*  310 */     if (INSTANCE == null) {
/*      */       try {
/*  312 */         INSTANCE = new VolcanoVaaAttrDlg(parShell);
/*      */       } catch (VizException e) {
/*  314 */         e.printStackTrace();
/*      */       }
/*      */     }
/*  317 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Object> getAttrFromDlg() {
/*  321 */     return new HashMap();
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute ia)
/*      */   {
/*  336 */     getShell().setText(getDlgTitle());
/*      */ 
/*  340 */     this.lblLocText.setText(this.volcano.getTxtLoc());
/*  341 */     this.lblAreaText.setText(this.volcano.getArea());
/*  342 */     this.lblElevText.setText(this.volcano.getElev());
/*      */ 
/*  346 */     this.txtYear.setText(getYear());
/*  347 */     this.txtAdNo.setText(getAdvisoryNo());
/*      */ 
/*  358 */     this.comboType.setEnabled(true);
/*  359 */     this.comboType.setText(this.volcano.getProduct());
/*      */ 
/*  364 */     this.txtInfoSour.setText(Volcano.getUserInputPart(this.volcano.getInfoSource()));
/*  365 */     this.txtAddInfoSour
/*  366 */       .setText(Volcano.getNoNullTxt(this.volcano.getAddInfoSource()));
/*  367 */     this.txtErup.setText(Volcano.getUserInputPart(this.volcano.getErupDetails()));
/*  368 */     this.txtAshDate.setText(Volcano.getNoNullTxt(this.volcano.getObsAshDate()));
/*  369 */     this.txtAshTime.setText(Volcano.getNoNullTxt(this.volcano.getObsAshTime()));
/*  370 */     this.txtRemark.setText(Volcano.getUserInputPart(this.volcano.getRemarks()));
/*      */ 
/*  374 */     setComboItem(this.comboForecaster, this.volcano.getForecasters(), true);
/*  375 */     setComboItem(this.comboNextAdv, this.volcano.getNextAdv(), false);
/*      */   }
/*      */ 
/*      */   public void setVolcano(DrawableElement de)
/*      */   {
/*  382 */     this.volcano = ((Volcano)de);
/*      */   }
/*      */ 
/*      */   public Volcano getVolcano() {
/*  386 */     return this.volcano;
/*      */   }
/*      */ 
/*      */   protected void buttonPressed(int buttonId)
/*      */   {
/*  396 */     if (buttonId == 0)
/*  397 */       okPressed();
/*  398 */     else if (1 == buttonId)
/*  399 */       cancelPressed();
/*  400 */     else if (FORMAT_ID == buttonId)
/*  401 */       formatPressed();
/*  402 */     else if (RESET_ID == buttonId)
/*  403 */       resetPressed();
/*      */   }
/*      */ 
/*      */   public void okPressed()
/*      */   {
/*  415 */     copyAttr2Vol();
/*      */   }
/*      */ 
/*      */   public void cancelPressed()
/*      */   {
/*  423 */     if (this.drawingLayer == null) {
/*  424 */       setReturnCode(1);
/*  425 */       close();
/*      */     } else {
/*  427 */       super.cancelPressed();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void formatPressed()
/*      */   {
/*  435 */     copyAttr2Vol();
/*      */ 
/*  437 */     SaveMsgDlg smDlg = SaveMsgDlg.getInstance(getParentShell());
/*  438 */     smDlg.setVolAttrDlgInstance(INSTANCE);
/*      */ 
/*  440 */     smDlg.setVolcano(this.volcano);
/*      */ 
/*  442 */     String xsltFile = PgenStaticDataProvider.getProvider()
/*  443 */       .getFileAbsolutePath(
/*  444 */       PgenStaticDataProvider.getProvider()
/*  445 */       .getPgenLocalizationRoot() + PGEN_VAA_XSLT);
/*      */ 
/*  447 */     ArrayList prds = new ArrayList();
/*  448 */     Product volProd = getVaaProduct(this.volcano);
/*      */ 
/*  450 */     String xml = "";
/*  451 */     if (volProd != null) {
/*  452 */       prds.add(volProd);
/*  453 */       Products filePrds = ProductConverter.convert(prds);
/*      */       try {
/*  455 */         xml = SerializationUtil.marshalToXml(filePrds);
/*      */       }
/*      */       catch (JAXBException e) {
/*  458 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  462 */     String textMsg = convertXml2Txt(xml, xsltFile);
/*  463 */     smDlg.setTxtFileContent(textMsg);
/*  464 */     String name = smDlg.getFileName();
/*      */ 
/*  466 */     smDlg.setBlockOnOpen(true);
/*  467 */     smDlg.open();
/*      */ 
/*  469 */     if (smDlg.getReturnCode() == 0) {
/*  470 */       String dataURI = saveVolcano();
/*  471 */       if (dataURI != null)
/*      */         try {
/*  473 */           StorageUtils.storeDerivedProduct(dataURI, name, "TEXT", 
/*  474 */             PgenUtil.wrap(textMsg, 51, null, false));
/*      */         } catch (PgenStorageException e) {
/*  476 */           StorageUtils.showError(e);
/*  477 */           return;
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void resetPressed()
/*      */   {
/*  488 */     this.txtYear.setText("");
/*  489 */     this.txtAdNo.setText("");
/*  490 */     this.txtInfoSour.setText("");
/*  491 */     this.txtAddInfoSour.setText("");
/*  492 */     this.txtErup.setText("");
/*  493 */     this.txtAshDate.setText("");
/*  494 */     this.txtAshTime.setText("");
/*  495 */     this.txtRemark.setText("");
/*      */ 
/*  497 */     this.comboForecaster.deselectAll();
/*  498 */     this.comboHdr.deselectAll();
/*  499 */     this.comboId.deselectAll();
/*  500 */     this.comboNextAdv.deselectAll();
/*  501 */     this.comboStn.deselectAll();
/*      */   }
/*      */ 
/*      */   public void createButtonsForButtonBar(Composite parent)
/*      */   {
/*  515 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/*  516 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*      */ 
/*  518 */     createButton(parent, 0, "Apply", true);
/*  519 */     createButton(parent, 1, "Cancel", true);
/*  520 */     createButton(parent, FORMAT_ID, "Format VAA", true);
/*  521 */     createButton(parent, RESET_ID, "Reset Form", true);
/*      */ 
/*  523 */     getButton(0).setEnabled(true);
/*  524 */     getButton(1).setEnabled(true);
/*  525 */     getButton(FORMAT_ID).setEnabled(true);
/*  526 */     getButton(RESET_ID).setEnabled(true);
/*  527 */     mouseHandlerName = null;
/*      */ 
/*  530 */     getButton(1).setLayoutData(
/*  531 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*  532 */     getButton(0).setLayoutData(
/*  533 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*  534 */     getButton(FORMAT_ID).setLayoutData(
/*  535 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*  536 */     getButton(RESET_ID).setLayoutData(
/*  537 */       new GridData(ctrlBtnWidth + 10, ctrlBtnHeight));
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  550 */     this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/*  552 */     getShell().setText(getDlgTitle());
/*      */ 
/*  554 */     GridLayout mainLayout = new GridLayout(8, false);
/*  555 */     mainLayout.marginHeight = 3;
/*  556 */     mainLayout.marginWidth = 3;
/*  557 */     this.top.setLayout(mainLayout);
/*      */ 
/*  559 */     Group top1 = new Group(this.top, 16384);
/*  560 */     top1.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/*  561 */     top1.setLayout(new GridLayout(8, false));
/*  562 */     createArea1(top1);
/*      */ 
/*  564 */     Group top2 = new Group(this.top, 16384);
/*  565 */     top2.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/*  566 */     top2.setLayout(new GridLayout(8, false));
/*  567 */     createArea2(top2);
/*      */ 
/*  569 */     Group top3 = new Group(this.top, 16384);
/*  570 */     top3.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/*  571 */     top3.setLayout(new GridLayout(8, false));
/*  572 */     createArea3(top3);
/*      */ 
/*  574 */     Group top4 = new Group(this.top, 16384);
/*  575 */     top4.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/*  576 */     top4.setLayout(new GridLayout(8, false));
/*  577 */     createArea4(top4);
/*      */ 
/*  579 */     return this.top;
/*      */   }
/*      */ 
/*      */   private void createArea1(Group top)
/*      */   {
/*  589 */     Label lblLoc = new Label(top, 16384);
/*  590 */     lblLoc.setText("Location: ");
/*      */ 
/*  592 */     this.lblLocText = new Label(top, 16384);
/*  593 */     this.lblLocText.setText(getLocText());
/*      */ 
/*  595 */     Label lblArea = new Label(top, 16384);
/*  596 */     lblArea.setText("Area: ");
/*      */ 
/*  598 */     this.lblAreaText = new Label(top, 16384);
/*  599 */     this.lblAreaText.setLayoutData(new GridData(4, 16777216, true, 
/*  600 */       false, 5, 1));
/*  601 */     this.lblAreaText.setText(getAreaText());
/*      */ 
/*  603 */     Label lblElev = new Label(top, 16384);
/*  604 */     lblElev.setText("Elevation: ");
/*      */ 
/*  606 */     this.lblElevText = new Label(top, 16384);
/*  607 */     this.lblElevText.setText(getElevText());
/*      */   }
/*      */ 
/*      */   private void createArea2(Group top)
/*      */   {
/*  617 */     Label lblOrig = new Label(top, 16384);
/*  618 */     lblOrig.setText("Orig Stn/VAAC: ");
/*      */ 
/*  620 */     this.comboStn = new Combo(top, 8);
/*  621 */     this.comboStn.setLayoutData(new GridData(4, 16777216, true, false, 
/*  622 */       5, 1));
/*  623 */     this.comboStn.setItems(getStnIdNumArray(null, null));
/*  624 */     this.comboStn.select(1);
/*      */ 
/*  626 */     Label lblDummy1 = new Label(top, 16384);
/*  627 */     Label lblDummy2 = new Label(top, 
/*  627 */       16384);
/*      */ 
/*  629 */     Label lblId = new Label(top, 16384);
/*  630 */     lblId.setText("WMO ID: ");
/*      */ 
/*  632 */     this.comboId = new Combo(top, 8);
/*  633 */     this.comboId.setItems(getStnIdNumArray(Boolean.valueOf(true), this.comboStn.getText().trim()));
/*  634 */     this.comboId.select(0);
/*      */ 
/*  636 */     Label lblHdr = new Label(top, 16384);
/*  637 */     lblHdr.setText("Hdr Number: ");
/*      */ 
/*  639 */     this.comboHdr = new Combo(top, 8);
/*  640 */     this.comboHdr.setItems(getStnIdNumArray(Boolean.valueOf(false), this.comboStn.getText()
/*  641 */       .trim()));
/*  642 */     this.comboHdr.select(0);
/*      */ 
/*  644 */     this.comboType = new Combo(top, 8);
/*  645 */     this.comboType.setItems(this.type);
/*  646 */     this.comboType.select(0);
/*  647 */     this.comboType.setLayoutData(new GridData(131072, 16777216, true, 
/*  648 */       false, 2, 1));
/*  649 */     this.comboType.setEnabled(isFromSelection());
/*      */ 
/*  651 */     this.comboStn.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  653 */         String stn = VolcanoVaaAttrDlg.this.comboStn.getText().trim();
/*      */ 
/*  655 */         VolcanoVaaAttrDlg.this.comboId.setItems(VolcanoVaaAttrDlg.getStnIdNumArray(Boolean.valueOf(true), stn));
/*  656 */         VolcanoVaaAttrDlg.this.comboId.select(0);
/*      */ 
/*  658 */         VolcanoVaaAttrDlg.this.comboHdr.setItems(VolcanoVaaAttrDlg.getStnIdNumArray(Boolean.valueOf(false), stn));
/*  659 */         VolcanoVaaAttrDlg.this.comboHdr.select(0);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void createArea3(Group top)
/*      */   {
/*  673 */     Label lblYear = new Label(top, 16384);
/*  674 */     lblYear.setText("Year: ");
/*      */ 
/*  676 */     this.txtYear = new Text(top, 18432);
/*  677 */     this.txtYear.setLayoutData(this.singleTxtGridData);
/*      */ 
/*  679 */     Label lblAdNo = new Label(top, 16384);
/*  680 */     lblAdNo.setText("Advisory No: ");
/*      */ 
/*  682 */     this.txtAdNo = new Text(top, 18432);
/*  683 */     this.txtAdNo.setLayoutData(this.singleTxtGridData);
/*      */ 
/*  685 */     this.btnCorr = new Button(top, 32);
/*  686 */     this.btnCorr.setText("Correction: ");
/*      */ 
/*  688 */     this.txtCorr = new Text(top, 18432);
/*  689 */     this.txtCorr.setEnabled(false);
/*  690 */     this.txtCorr.addVerifyListener(this.tvl);
/*      */ 
/*  692 */     this.btnCorr.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  694 */         boolean flag = ((Button)e.widget).getSelection();
/*  695 */         if (!flag) {
/*  696 */           String corr = VolcanoVaaAttrDlg.this.txtCorr.getText();
/*  697 */           if ((corr != null) && (corr.length() > 0)) {
/*  698 */             VolcanoVaaAttrDlg.this.setTxtChange(VolcanoVaaAttrDlg.this.txtAdNo, true);
/*      */ 
/*  701 */             VolcanoVaaAttrDlg.this.txtCorr.removeVerifyListener(VolcanoVaaAttrDlg.this.tvl);
/*  702 */             VolcanoVaaAttrDlg.this.txtCorr.setText("");
/*  703 */             VolcanoVaaAttrDlg.this.txtCorr.addVerifyListener(VolcanoVaaAttrDlg.this.tvl);
/*      */           }
/*      */         }
/*      */ 
/*  707 */         VolcanoVaaAttrDlg.this.txtCorr.setEnabled(flag);
/*      */       }
/*      */     });
/*  711 */     this.txtYear.setText(getYear());
/*  712 */     this.txtAdNo.setText("001");
/*      */   }
/*      */ 
/*      */   public void createArea4(Group top)
/*      */   {
/*  736 */     Button btnInfoSour = new Button(top, 8);
/*  737 */     btnInfoSour.setText("Information Source:");
/*  738 */     btnInfoSour.setLayoutData(new GridData(
/*  739 */       2));
/*      */ 
/*  741 */     this.txtInfoSour = new Text(top, 2114);
/*  742 */     GridData gData = new GridData(300, 96);
/*  743 */     gData.horizontalSpan = 7;
/*  744 */     this.txtInfoSour.setLayoutData(gData);
/*  745 */     this.txtInfoSour.setEditable(false);
/*      */ 
/*  747 */     btnInfoSour.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  749 */         AshCloudInfoSourceDlg isDlg = 
/*  750 */           AshCloudInfoSourceDlg.getInstance(VolcanoVaaAttrDlg.this.getParentShell());
/*      */ 
/*  752 */         isDlg.open();
/*      */       }
/*      */     });
/*  758 */     Label lblAddInfoSour = new Label(top, 16384);
/*  759 */     lblAddInfoSour.setText("Add'l Info Source: ");
/*  760 */     lblAddInfoSour.setLayoutData(new GridData(
/*  761 */       2));
/*      */ 
/*  763 */     this.txtAddInfoSour = new Text(top, 2114);
/*  764 */     GridData gData2 = new GridData(300, 96);
/*  765 */     gData2.horizontalSpan = 7;
/*  766 */     this.txtAddInfoSour.setLayoutData(gData2);
/*  767 */     this.txtAddInfoSour.addModifyListener(new TxtModifyListener(null));
/*      */ 
/*  771 */     Label lblAviColoCode = new Label(top, 16384);
/*  772 */     lblAviColoCode.setText("Aviation Color Code: ");
/*      */ 
/*  774 */     this.comboAviColoCode = new Combo(top, 8);
/*  775 */     this.comboAviColoCode.setItems(getAviColoCode());
/*  776 */     this.comboAviColoCode.setLayoutData(new GridData(4, 16777216, true, 
/*  777 */       false, 7, 1));
/*  778 */     this.comboAviColoCode.setEnabled(false);
/*      */ 
/*  782 */     Label lblErup = new Label(top, 16384);
/*  783 */     lblErup.setText("Eruption Details: ");
/*  784 */     lblErup.setLayoutData(new GridData(2));
/*      */ 
/*  786 */     this.txtErup = new Text(top, 2050);
/*  787 */     GridData gData3 = new GridData(300, 96);
/*  788 */     gData3.horizontalSpan = 7;
/*  789 */     this.txtErup.setLayoutData(gData3);
/*  790 */     this.txtErup.addModifyListener(new TxtModifyListener(null));
/*      */ 
/*  794 */     Label lblAshDate = new Label(top, 16384);
/*  795 */     lblAshDate.setText("Obs Ash Date(DD): ");
/*      */ 
/*  797 */     this.txtAshDate = new Text(top, 2048);
/*  798 */     this.txtAshDate.setLayoutData(this.singleTxtGridData);
/*      */ 
/*  800 */     Label lblAshTime = new Label(top, 16384);
/*  801 */     lblAshTime.setText("Time(HHHH):");
/*      */ 
/*  803 */     this.txtAshTime = new Text(top, 2048);
/*  804 */     this.txtAshTime.setLayoutData(this.singleTxtGridData);
/*      */ 
/*  806 */     Label lblZ = new Label(top, 16384);
/*  807 */     lblZ.setText("Z");
/*      */ 
/*  809 */     this.btnNil = new Button(top, 32);
/*  810 */     this.btnNil.setText("NIL");
/*  811 */     this.btnNil.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  813 */         String nil = "NIL"; String clear = "";
/*      */ 
/*  815 */         if (((Button)e.widget).getSelection()) {
/*  816 */           VolcanoVaaAttrDlg.this.txtAshDate.setText(nil);
/*  817 */           VolcanoVaaAttrDlg.this.txtAshTime.setText(nil);
/*  818 */           VolcanoVaaAttrDlg.this.txtAshDate.setEditable(false);
/*  819 */           VolcanoVaaAttrDlg.this.txtAshTime.setEditable(false);
/*      */         } else {
/*  821 */           VolcanoVaaAttrDlg.this.txtAshDate.setText(clear);
/*  822 */           VolcanoVaaAttrDlg.this.txtAshTime.setText(clear);
/*  823 */           VolcanoVaaAttrDlg.this.txtAshDate.setEditable(true);
/*  824 */           VolcanoVaaAttrDlg.this.txtAshTime.setEditable(true);
/*      */         }
/*      */       }
/*      */     });
/*  829 */     Label lblDummyNil = new Label(top, 16384);
/*  830 */     Label lblDummyNil2 = new Label(
/*  830 */       top, 16384);
/*      */ 
/*  834 */     this.btnCloudInfo = new Button(top, 8);
/*  835 */     this.btnCloudInfo.setText("Observed and Forecast Ash Cloud Inormation...");
/*  836 */     this.btnCloudInfo.setLayoutData(new GridData(4, 16777216, true, 
/*  837 */       false, 8, 1));
/*  838 */     this.btnCloudInfo.addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event e) {
/*  841 */         AshCloudInfoDlg aciDlg = 
/*  842 */           AshCloudInfoDlg.getInstance(VolcanoVaaAttrDlg.this.getParentShell());
/*      */ 
/*  844 */         aciDlg.setDateTimeForDlg(VolcanoVaaAttrDlg.this.txtAshDate.getText().trim(), 
/*  845 */           VolcanoVaaAttrDlg.this.txtAshTime.getText().trim());
/*  846 */         aciDlg.setVaaAttrDlg(VolcanoVaaAttrDlg.this);
/*  847 */         aciDlg.open();
/*      */       }
/*      */     });
/*  854 */     Label lblRemarks = new Label(top, 16384);
/*  855 */     lblRemarks.setText("Remarks: ");
/*  856 */     lblRemarks
/*  857 */       .setLayoutData(new GridData(2));
/*      */ 
/*  859 */     this.txtRemark = new Text(top, 2114);
/*  860 */     GridData gData4 = new GridData(300, 96);
/*  861 */     gData4.horizontalSpan = 7;
/*  862 */     this.txtRemark.setLayoutData(gData4);
/*  863 */     this.txtRemark.addModifyListener(new TxtModifyListener(null));
/*      */ 
/*  867 */     Label lblNextAdv = new Label(top, 16384);
/*  868 */     lblNextAdv.setText("Next Advisory: ");
/*      */ 
/*  870 */     this.comboNextAdv = new Combo(top, 4);
/*  871 */     this.comboNextAdv.setItems(getNextAdvText());
/*  872 */     this.comboNextAdv.setLayoutData(new GridData(4, 16777216, true, 
/*  873 */       false, 7, 1));
/*  874 */     this.comboNextAdv.addModifyListener(new TxtModifyListener(null));
/*      */ 
/*  877 */     Label lblForecaster = new Label(top, 16384);
/*  878 */     lblForecaster.setText("Forecaster(s): ");
/*      */ 
/*  880 */     this.comboForecaster = new Combo(top, 12);
/*  881 */     this.comboForecaster.setItems(getForecastersName());
/*  882 */     this.comboForecaster.setLayoutData(new GridData(4, 16777216, true, 
/*  883 */       false, 7, 1));
/*      */ 
/*  887 */     this.txtRemark.setText(getRemarks());
/*      */ 
/*  893 */     if (VaaInfo.isNonDrawableVol(this.volcano)) {
/*  894 */       this.btnNil.setSelection(true);
/*  895 */       this.txtAshDate.setText("NIL");
/*  896 */       this.txtAshTime.setText("NIL");
/*  897 */       this.txtAshDate.setEditable(false);
/*  898 */       this.txtAshTime.setEditable(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getDlgTitle()
/*      */   {
/*  910 */     StringBuilder sb = new StringBuilder("VAA - ");
/*      */ 
/*  912 */     String num = this.volcano.getNumber();
/*  913 */     sb.append(this.volcano.getName() + "(" + (num == null ? "" : num) + ")");
/*      */ 
/*  915 */     return sb.toString().toUpperCase();
/*      */   }
/*      */ 
/*      */   private String getLocText()
/*      */   {
/*  920 */     return this.volcano.getTxtLoc();
/*      */   }
/*      */ 
/*      */   private String getAreaText()
/*      */   {
/*  925 */     return this.volcano.getArea();
/*      */   }
/*      */ 
/*      */   private String getElevText()
/*      */   {
/*  934 */     if (this.volcano == null) {
/*  935 */       return "";
/*      */     }
/*      */ 
/*  938 */     StringBuilder sb = new StringBuilder();
/*      */ 
/*  940 */     int dMeter = 0;
/*  941 */     String sMeter = this.volcano.getElev();
/*      */ 
/*  946 */     if ((sMeter == null) || (sMeter.length() == 0)) {
/*  947 */       sb.append(dMeter).append("  ft (").append(dMeter).append("  m)");
/*  948 */       this.volcano.setElev(sb.toString());
/*      */     }
/*  950 */     this.elevFootMeterTxt = this.volcano.getElev();
/*  951 */     return this.elevFootMeterTxt;
/*      */   }
/*      */ 
/*      */   public String getElevFootMeterTxt()
/*      */   {
/*  956 */     return this.elevFootMeterTxt;
/*      */   }
/*      */ 
/*      */   public static String[] getStnIdNumArray(Boolean isId, String stn)
/*      */   {
/*  969 */     Map map = VaaInfo.VAA_INFO_STN_MAP;
/*      */ 
/*  971 */     if (isId == null)
/*  972 */       return (String[])map.keySet().toArray(new String[0]);
/*  973 */     if (isId.booleanValue()) {
/*  974 */       return stn == null ? new String[0] : ((String[])map.get(stn))[0].substring(1)
/*  975 */         .split(";");
/*      */     }
/*  977 */     return stn == null ? new String[0] : ((String[])map.get(stn))[1].substring(1)
/*  978 */       .split(";");
/*      */   }
/*      */ 
/*      */   public static String[] getInfoSourItems()
/*      */   {
/*  988 */     String[] s = (String[])VaaInfo.VAA_INFO_SINGLE_MAP.get("information-source");
/*      */ 
/*  990 */     return s;
/*      */   }
/*      */ 
/*      */   public static String[] getForecastersName()
/*      */   {
/*  999 */     String[] s = (String[])VaaInfo.VAA_INFO_SINGLE_MAP.get("forecasters");
/*      */ 
/* 1001 */     return s;
/*      */   }
/*      */ 
/*      */   public static String[] getAviColoCode()
/*      */   {
/* 1010 */     String[] s = (String[])VaaInfo.VAA_INFO_SINGLE_MAP.get("aviation-color-code");
/*      */ 
/* 1012 */     return s;
/*      */   }
/*      */ 
/*      */   public static String[] getNextAdvText()
/*      */   {
/* 1021 */     String[] s = (String[])VaaInfo.VAA_INFO_SINGLE_MAP.get("next-advisory");
/*      */ 
/* 1023 */     return s;
/*      */   }
/*      */ 
/*      */   public String getRemarks()
/*      */   {
/* 1033 */     String rmk = Volcano.getNoNullTxt(this.volcano.getRemarks());
/*      */ 
/* 1035 */     String[] words = rmk.split(":::");
/* 1036 */     if ((VaaInfo.isNonDrawableVol(this.volcano)) && (words.length > 1))
/* 1037 */       return words[1];
/* 1038 */     return words[0];
/*      */   }
/*      */ 
/*      */   public String getYear()
/*      */   {
/* 1047 */     return 1900 + new Date().getYear();
/*      */   }
/*      */ 
/*      */   public String getAdvisoryNo()
/*      */   {
/* 1069 */     if (VaaInfo.isNonDrawableVol(this.volcano)) {
/* 1070 */       return "001";
/*      */     }
/* 1072 */     String no = VaaInfo.getLatestAdvNo(this.volcano.getName());
/* 1073 */     int noInt = Integer.parseInt(no);
/*      */ 
/* 1075 */     return new DecimalFormat("000").format(++noInt);
/*      */   }
/*      */ 
/*      */   public boolean isFromSelection() {
/* 1079 */     return this.fromSelection;
/*      */   }
/*      */ 
/*      */   public void setFromSelection(boolean fromSelection) {
/* 1083 */     this.fromSelection = fromSelection;
/*      */   }
/*      */ 
/*      */   public void copyAttrToVolcano()
/*      */   {
/* 1091 */     this.volcano.setOrigStnVAAC(this.comboStn.getText());
/* 1092 */     this.volcano.setWmoId(this.comboId.getText());
/* 1093 */     this.volcano.setHdrNum(this.comboHdr.getText());
/* 1094 */     if (this.fromSelection) {
/* 1095 */       this.volcano.setProduct(this.comboType.getText());
/*      */     }
/* 1097 */     this.volcano.setYear(this.txtYear.getText());
/* 1098 */     this.volcano.setAdvNum(this.txtAdNo.getText());
/* 1099 */     this.volcano.setCorr(this.txtCorr.getText());
/*      */ 
/* 1104 */     this.volcano.setInfoSource(this.txtInfoSour.getText());
/*      */ 
/* 1106 */     this.volcano.setAddInfoSource(getTxtNoRsrvWord(this.txtAddInfoSour.getText()));
/* 1107 */     this.volcano.setAviColorCode(this.comboAviColoCode.getText());
/* 1108 */     this.volcano.setErupDetails(getTxtNoRsrvWord(this.txtErup.getText()));
/*      */ 
/* 1110 */     this.volcano.setObsAshDate(this.txtAshDate.getText());
/* 1111 */     this.volcano.setObsAshTime(this.txtAshTime.getText());
/* 1112 */     this.volcano.setNil(this.btnNil.isEnabled());
/*      */ 
/* 1115 */     this.volcano.setObsFcstAshCloudInfo(VaaInfo.getAshCloudInfo("00"));
/*      */ 
/* 1119 */     String[] fhrDT = VaaInfo.getFhrTimes(this.txtAshDate.getText().trim(), 
/* 1120 */       this.txtAshTime.getText().trim());
/* 1121 */     this.volcano.setObsFcstAshCloudInfo6(getVacInfoFhr6(fhrDT));
/* 1122 */     this.volcano.setObsFcstAshCloudInfo12(getVacInfoFhr12(fhrDT));
/* 1123 */     this.volcano.setObsFcstAshCloudInfo18(getVacInfoFhr18(fhrDT));
/*      */ 
/* 1127 */     this.volcano.setRemarks(getTxtNoRsrvWord(this.txtRemark.getText()));
/* 1128 */     this.volcano.setNextAdv(getTxtNoRsrvWord(getNxtAdvTxt(this.comboNextAdv.getText())));
/* 1129 */     this.volcano.setForecasters(this.comboForecaster.getText());
/*      */   }
/*      */ 
/*      */   private void copyAttr2Vol()
/*      */   {
/* 1141 */     copyFixedAttr2Vol();
/*      */ 
/* 1143 */     if (VaaInfo.isNonDrawableVol(this.volcano))
/*      */     {
/* 1148 */       return;
/*      */     }
/*      */ 
/* 1152 */     copyAttrToVolcano();
/*      */ 
/* 1157 */     VaaInfo.setVolcanoFields(this.volcano, this.volcano.getProduct(), true);
/*      */   }
/*      */ 
/*      */   private void copyFixedAttr2Vol()
/*      */   {
/* 1168 */     this.volcano.setWmoId(this.comboId.getText());
/* 1169 */     this.volcano.setHdrNum(this.comboHdr.getText());
/* 1170 */     this.volcano.setOrigStnVAAC(this.comboStn.getText());
/* 1171 */     this.volcano.setCorr(this.txtCorr.getText());
/*      */ 
/* 1173 */     if ((this.volcano.getProduct() == null) || (this.volcano.getProduct().length() == 0)) {
/* 1174 */       this.volcano.setProduct(this.comboType.getText());
/*      */     }
/* 1176 */     if ("BACKUP".equals(this.volcano.getProduct()))
/* 1177 */       this.volcano.setRemarks(getTxtNoRsrvWord(this.txtRemark.getText()));
/*      */   }
/*      */ 
/*      */   public void setInfoSource(String[] source)
/*      */   {
/* 1188 */     if ((source == null) || (source.length == 0)) {
/* 1189 */       return;
/*      */     }
/* 1191 */     StringBuilder sb = new StringBuilder();
/*      */ 
/* 1193 */     for (String s : source) {
/* 1194 */       sb.append(s).append(". ");
/*      */     }
/*      */ 
/* 1198 */     this.txtInfoSour.setFocus();
/*      */ 
/* 1200 */     this.txtInfoSour.setText(sb.toString());
/*      */   }
/*      */ 
/*      */   private void init()
/*      */   {
/*      */   }
/*      */ 
/*      */   private void setTxtChange(Text txtAdNo, boolean increFlag)
/*      */   {
/* 1216 */     if (VaaInfo.isNonDrawableVol(this.volcano)) {
/* 1217 */       return;
/*      */     }
/* 1219 */     if ((txtAdNo == null) || (txtAdNo.isDisposed())) {
/* 1220 */       return;
/*      */     }
/* 1222 */     String adNo = txtAdNo.getText();
/* 1223 */     if ((adNo == null) || (adNo.length() == 0)) {
/* 1224 */       return;
/*      */     }
/* 1226 */     int ano = 0; int orig = 0;
/*      */     try {
/* 1228 */       ano = Integer.parseInt(txtAdNo.getText());
/* 1229 */       orig = Integer.parseInt(getAdvisoryNo());
/*      */     } catch (Exception e) {
/* 1231 */       return;
/*      */     }
/*      */ 
/* 1234 */     if (orig > 1) {
/* 1235 */       if (increFlag)
/* 1236 */         ano++;
/*      */       else {
/* 1238 */         ano--;
/*      */       }
/* 1240 */       txtAdNo.setText(new DecimalFormat("000").format(ano));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setComboItem(Combo c, String s, boolean isReadOnly)
/*      */   {
/* 1297 */     if ((isReadOnly) && (Arrays.asList(c.getItems()).contains(s)))
/* 1298 */       c.setText(s);
/* 1299 */     if ((!isReadOnly) && (s != null) && (s.length() > 0))
/* 1300 */       c.setText(Volcano.getUserInputPart(s));
/*      */   }
/*      */ 
/*      */   public String getVacInfoFhr6(String[] fhrDT)
/*      */   {
/* 1309 */     return this.vacInfoFhr6 == null ? fhrDT[0] + "  " + 
/* 1310 */       VaaInfo.getAshCloudInfo(VaaInfo.LAYERS[2]) : this.vacInfoFhr6;
/*      */   }
/*      */ 
/*      */   public void setVacInfoFhr6(String vacInfoFhr6) {
/* 1314 */     this.vacInfoFhr6 = vacInfoFhr6;
/*      */   }
/*      */ 
/*      */   public String getVacInfoFhr12(String[] fhrDT) {
/* 1318 */     return this.vacInfoFhr12 == null ? fhrDT[1] + "  " + 
/* 1319 */       VaaInfo.getAshCloudInfo(VaaInfo.LAYERS[3]) : this.vacInfoFhr12;
/*      */   }
/*      */ 
/*      */   public void setVacInfoFhr12(String vacInfoFhr12) {
/* 1323 */     this.vacInfoFhr12 = vacInfoFhr12;
/*      */   }
/*      */ 
/*      */   public String getVacInfoFhr18(String[] fhrDT) {
/* 1327 */     return this.vacInfoFhr18 == null ? fhrDT[2] + "  " + 
/* 1328 */       VaaInfo.getAshCloudInfo(VaaInfo.LAYERS[4]) : this.vacInfoFhr18;
/*      */   }
/*      */ 
/*      */   public void setVacInfoFhr18(String vacInfoFhr18) {
/* 1332 */     this.vacInfoFhr18 = vacInfoFhr18;
/*      */   }
/*      */ 
/*      */   private Product getVaaProduct(Volcano vol)
/*      */   {
/* 1353 */     return vol != null ? (Product)VaaInfo.VOL_PROD_MAP.get(vol) : null;
/*      */   }
/*      */ 
/*      */   private String getFileName()
/*      */   {
/* 1362 */     String connector = "_";
/*      */ 
/* 1364 */     StringBuilder sb = new StringBuilder();
/* 1365 */     sb.append(this.volcano.getName());
/* 1366 */     sb.append(connector);
/* 1367 */     sb.append(VaaInfo.getDateTime("yyyyMMdd"));
/* 1368 */     sb.append(connector);
/* 1369 */     sb.append(VaaInfo.getDateTime("HHmm"));
/*      */ 
/* 1371 */     sb.append(".xml");
/* 1372 */     this.volXMLFileName = sb.toString();
/* 1373 */     return this.volXMLFileName;
/*      */   }
/*      */ 
/*      */   private String saveVolcano()
/*      */   {
/* 1384 */     String label = getFileName();
/*      */ 
/* 1386 */     Product volProd = getVaaProduct(this.volcano);
/*      */ 
/* 1388 */     if (volProd != null)
/*      */     {
/* 1392 */       volProd.setOutputFile(label);
/* 1393 */       volProd.setName(this.volcano.getName());
/* 1394 */       volProd.setForecaster(this.volcano.getForecasters());
/*      */       try {
/* 1396 */         dataURI = StorageUtils.storeProduct(volProd);
/*      */       }
/*      */       catch (PgenStorageException e)
/*      */       {
/*      */         String dataURI;
/* 1398 */         StorageUtils.showError(e);
/* 1399 */         return null;
/*      */       }
/*      */       String dataURI;
/* 1401 */       return dataURI;
/*      */     }
/*      */ 
/* 1404 */     return null;
/*      */   }
/*      */ 
/*      */   private String convertXml2Txt(String xmlString, String xltFileName)
/*      */   {
/* 1418 */     String res = "";
/*      */ 
/* 1420 */     Source xmlSource = new StreamSource(new StringReader(xmlString));
/* 1421 */     Source xsltSource = new StreamSource(xltFileName);
/*      */ 
/* 1423 */     TransformerFactory transFact = TransformerFactory.newInstance();
/*      */     try
/*      */     {
/* 1426 */       Transformer trans = transFact.newTransformer(xsltSource);
/* 1427 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 1428 */       trans.transform(xmlSource, new StreamResult(baos));
/*      */ 
/* 1430 */       res = new String(baos.toByteArray());
/*      */     } catch (Exception e) {
/* 1432 */       System.out.println("Error: File is corrupt");
/*      */     }
/*      */ 
/* 1435 */     return getNormalizedTxt(res);
/*      */   }
/*      */ 
/*      */   private String getNxtAdvTxt(String nxtAdv)
/*      */   {
/* 1447 */     String word = "YYYYMMMDD/HHNNZ";
/*      */ 
/* 1449 */     if ((nxtAdv != null) && (nxtAdv.trim().length() > 0) && 
/* 1450 */       (nxtAdv.contains(word))) {
/* 1451 */       StringBuilder sb = new StringBuilder();
/*      */ 
/* 1453 */       sb.append(VaaInfo.getDateTime("yyyyMMdd"));
/* 1454 */       sb.append("/");
/* 1455 */       sb.append(VaaInfo.getDateTime("HHmm"));
/* 1456 */       sb.append("Z");
/*      */ 
/* 1458 */       return nxtAdv.replaceFirst(word, sb.toString());
/*      */     }
/*      */ 
/* 1461 */     return nxtAdv;
/*      */   }
/*      */ 
/*      */   private String getTxtNoRsrvWord(String txt)
/*      */   {
/* 1505 */     if ((txt == null) || (txt.length() == 0)) {
/* 1506 */       return "";
/*      */     }
/* 1508 */     if (txt.contains(":::")) {
/* 1509 */       return txt.replaceAll(":::", "");
/*      */     }
/*      */ 
/* 1512 */     return txt;
/*      */   }
/*      */ 
/*      */   private String getNormalizedTxt(String s)
/*      */   {
/* 1520 */     StringBuilder sb = new StringBuilder("");
/*      */ 
/* 1522 */     String[] txt = s.split("\n");
/* 1523 */     int start = 0; int end = 0;
/*      */ 
/* 1525 */     for (int i = 0; i < txt.length; i++) {
/* 1526 */       if (txt.length > 3)
/*      */       {
/* 1528 */         if ((txt[i].startsWith("FV")) && 
/* 1529 */           (txt[(i + 1)].startsWith("VA ADVISORY")) && 
/* 1530 */           (txt[(i + 2)].startsWith("DTG")))
/*      */         {
/* 1532 */           start = i;
/*      */         }
/*      */ 
/* 1535 */         if (("NNNN".equals(txt[i])) && (i > start)) {
/* 1536 */           end = i;
/* 1537 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1542 */     for (int j = start; j <= end; j++) {
/* 1543 */       sb.append(txt[j]).append("\n");
/*      */     }
/*      */ 
/* 1546 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public Coordinate[] getLinePoints()
/*      */   {
/* 1552 */     return null;
/*      */   }
/*      */ 
/*      */   public String getPatternName()
/*      */   {
/* 1558 */     return null;
/*      */   }
/*      */ 
/*      */   public int getSmoothFactor()
/*      */   {
/* 1564 */     return 0;
/*      */   }
/*      */ 
/*      */   public Boolean isClosedLine()
/*      */   {
/* 1570 */     return null;
/*      */   }
/*      */ 
/*      */   public Boolean isFilled()
/*      */   {
/* 1576 */     return null;
/*      */   }
/*      */ 
/*      */   public FillPatternList.FillPattern getFillPattern()
/*      */   {
/* 1582 */     return null;
/*      */   }
/*      */ 
/*      */   public String getLineType()
/*      */   {
/* 1588 */     return null;
/*      */   }
/*      */ 
/*      */   public double getWidth()
/*      */   {
/* 1594 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   public boolean close()
/*      */   {
/* 1599 */     this.drawingLayer.removeSelected();
/* 1600 */     SaveMsgDlg.getInstance(getParentShell()).close();
/* 1601 */     return super.close();
/*      */   }
/*      */ 
/*      */   private class TxtModifyListener
/*      */     implements ModifyListener
/*      */   {
/*      */     private TxtModifyListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void modifyText(ModifyEvent e)
/*      */     {
/* 1474 */       String txt = "";
/* 1475 */       if ((e.widget instanceof Text))
/* 1476 */         txt = ((Text)e.widget).getText();
/* 1477 */       else if ((e.widget instanceof Combo)) {
/* 1478 */         txt = ((Combo)e.widget).getText();
/*      */       }
/*      */ 
/* 1481 */       if ((txt != null) && (txt.length() > 0) && 
/* 1482 */         (txt.contains(":::")))
/*      */       {
/* 1484 */         MessageDialog confirmDlg = new MessageDialog(
/* 1485 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow()
/* 1486 */           .getShell(), 
/* 1487 */           "Warning", 
/* 1488 */           null, 
/* 1489 */           " ::: IS A RESERVED WORD AND WILL BE REMOVED FROM THE TEXT!", 
/* 1490 */           4, new String[] { "OK" }, 0);
/*      */ 
/* 1492 */         confirmDlg.open();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class TxtVerifyListener
/*      */     implements VerifyListener
/*      */   {
/*      */     private TxtVerifyListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void verifyText(VerifyEvent event)
/*      */     {
/* 1258 */       event.doit = false;
/*      */ 
/* 1261 */       int myChar = event.character;
/* 1262 */       String text = ((Text)event.widget).getText();
/*      */ 
/* 1266 */       if (((myChar >= 65) && (myChar <= 90)) || 
/* 1267 */         ((myChar >= 97) && (myChar <= 122)) || (
/* 1268 */         (Character.isDigit(myChar)) && (text.length() == 0))) {
/* 1269 */         event.doit = true;
/* 1270 */         VolcanoVaaAttrDlg.this.setTxtChange(VolcanoVaaAttrDlg.this.txtAdNo, false);
/*      */       }
/*      */ 
/* 1278 */       if (((myChar == 8) || (myChar == 127)) && (text.length() == 1)) {
/* 1279 */         event.doit = true;
/* 1280 */         VolcanoVaaAttrDlg.this.setTxtChange(VolcanoVaaAttrDlg.this.txtAdNo, true);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.VolcanoVaaAttrDlg
 * JD-Core Version:    0.6.2
 */