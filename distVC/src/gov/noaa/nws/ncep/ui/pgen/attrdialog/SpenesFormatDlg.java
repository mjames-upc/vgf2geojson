/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Spenes;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import org.dom4j.Document;
/*      */ import org.dom4j.Node;
/*      */ import org.dom4j.io.SAXReader;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.swt.events.ModifyEvent;
/*      */ import org.eclipse.swt.events.ModifyListener;
/*      */ import org.eclipse.swt.graphics.Font;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.DateTime;
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
/*      */ public class SpenesFormatDlg extends CaveJFACEDialog
/*      */ {
/*   61 */   private SpenesFormatMsgDlg sfMsgDlg = null;
/*      */ 
/*   66 */   private static SpenesFormatDlg INSTANCE = null;
/*      */ 
/*   71 */   private static Spenes spenes = null;
/*      */ 
/*   76 */   private SpenesAttrDlg spDlg = null;
/*      */   private Composite top;
/*   86 */   private static int FORMAT_ID = 20120508;
/*      */ 
/*   91 */   private static int RESET_ID = 20120509;
/*      */ 
/*   96 */   private Combo comboLatestData = null;
/*      */ 
/*  101 */   private Combo comboForecaster = null;
/*      */ 
/*  106 */   private Combo comboOutlookLevel = null;
/*      */   private Text txtAbrv;
/*  116 */   private Text txtLocation = null;
/*      */ 
/*  121 */   private Text txtAttnWFOs = null;
/*      */ 
/*  126 */   private Text txtAttnRFCs = null;
/*      */ 
/*  131 */   private Text txtEvent = null;
/*      */ 
/*  136 */   private Text txtSatAnaTre = null;
/*      */ 
/*  141 */   private Text txtAddlInfo = null;
/*      */   private DateTime dtObsHr;
/*  151 */   private GridData singleTxtGridData = new GridData(300, 36);
/*      */ 
/*  156 */   private Button btnSatAnaTre = null;
/*      */ 
/*  160 */   private Button btnAddlInfo = null;
/*      */   private DateTime dtTermsHr;
/*      */   private DateTime dtTerms2Hr;
/*  174 */   private static String[] DATATYPEARRAY = { "GOES-11", "GOES-12", "GOES-13", "GOES-15", "DPD", "IND", "Meteosat", "VAAC" };
/*      */ 
/*  178 */   private static Document forecasterTbl = null;
/*  179 */   private static String[] FORECASTERS = null;
/*  180 */   private static String FORECASTER_XPATH = "/forecasters/forecaster";
/*      */   private static final String PGEN_FORECASTER = "forecasters.xml";
/*  186 */   private static String[] OUTLOOKLEVEL = { "HIGH", "MEDIUM", "LOW" };
/*      */ 
/*      */   private SpenesFormatDlg(Shell parentShell, SpenesAttrDlg spDlg)
/*      */     throws VizException
/*      */   {
/*  195 */     super(parentShell);
/*  196 */     setShellStyle(96);
/*      */ 
/*  198 */     setSpDlg(spDlg);
/*  199 */     readForecasterTbl();
/*  200 */     FORECASTERS = getForecasters();
/*      */   }
/*      */ 
/*      */   public static SpenesFormatDlg getInstance(Shell parentShell, SpenesAttrDlg spenesDlg, Spenes sp)
/*      */   {
/*  209 */     if (INSTANCE == null) {
/*      */       try {
/*  211 */         INSTANCE = new SpenesFormatDlg(parentShell, spenesDlg);
/*      */       }
/*      */       catch (VizException e) {
/*  214 */         e.printStackTrace();
/*      */       }
/*      */     }
/*  217 */     spenes = sp;
/*      */ 
/*  219 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  227 */     this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/*  229 */     getShell().setText("SPENES FORMAT");
/*      */ 
/*  232 */     GridLayout mainLayout = new GridLayout(2, false);
/*  233 */     mainLayout.marginHeight = 3;
/*  234 */     mainLayout.marginWidth = 3;
/*  235 */     mainLayout.verticalSpacing = 3;
/*  236 */     this.top.setLayout(mainLayout);
/*      */ 
/*  242 */     Group top1 = new Group(this.top, 16384);
/*  243 */     top1.setLayoutData(new GridData(4, 16777216, true, true, 2, 1));
/*  244 */     top1.setLayout(new GridLayout(2, false));
/*  245 */     createArea1(top1);
/*      */ 
/*  247 */     Group top2 = new Group(this.top, 16384);
/*  248 */     top2.setLayoutData(new GridData(4, 16777216, true, true, 2, 1));
/*  249 */     top2.setLayout(new GridLayout(2, false));
/*  250 */     createArea2(top2);
/*      */ 
/*  252 */     Group top3 = new Group(this.top, 16384);
/*  253 */     top3.setLayoutData(new GridData(4, 16777216, true, true, 2, 1));
/*  254 */     top3.setLayout(new GridLayout(2, false));
/*  255 */     createArea3(top3);
/*      */ 
/*  257 */     Group top4 = new Group(this.top, 16384);
/*  258 */     top4.setLayoutData(new GridData(4, 16777216, true, true, 2, 1));
/*  259 */     top4.setLayout(new GridLayout(2, false));
/*  260 */     createArea4(top4);
/*      */ 
/*  263 */     Group top5 = new Group(this.top, 16384);
/*  264 */     top5.setLayoutData(new GridData(4, 16777216, true, true, 2, 1));
/*  265 */     top5.setLayout(new GridLayout(2, false));
/*  266 */     createArea5(top5);
/*      */ 
/*  268 */     return this.top;
/*      */   }
/*      */ 
/*      */   public int open()
/*      */   {
/*  277 */     if (getShell() == null) {
/*  278 */       create();
/*      */     }
/*      */ 
/*  286 */     return super.open();
/*      */   }
/*      */ 
/*      */   public Spenes getSpenes()
/*      */   {
/*  295 */     return spenes;
/*      */   }
/*      */ 
/*      */   public void setSpenes(Spenes sp)
/*      */   {
/*  303 */     spenes = sp;
/*      */   }
/*      */ 
/*      */   protected void buttonPressed(int buttonId)
/*      */   {
/*  312 */     if (buttonId == 0)
/*  313 */       okPressed();
/*  314 */     else if (1 == buttonId)
/*  315 */       cancelPressed();
/*  316 */     else if (FORMAT_ID == buttonId)
/*  317 */       formatPressed();
/*  318 */     else if (RESET_ID == buttonId)
/*  319 */       resetPressed();
/*      */   }
/*      */ 
/*      */   public void formatPressed()
/*      */   {
/*  327 */     copyAttrToSpenes();
/*      */ 
/*  329 */     SpenesFormatMsgDlg sfmDlg = new SpenesFormatMsgDlg(getParentShell(), this);
/*  330 */     sfmDlg.setBlockOnOpen(false);
/*  331 */     sfmDlg.open();
/*      */   }
/*      */ 
/*      */   public void okPressed()
/*      */   {
/*  339 */     String err = checkErr();
/*  340 */     if (!err.isEmpty()) {
/*  341 */       MessageDialog infoDlg = new MessageDialog(
/*  342 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  343 */         "Warning!", null, err, 
/*  344 */         2, new String[] { "OK" }, 0);
/*  345 */       infoDlg.open();
/*      */     }
/*      */     else {
/*  348 */       copyAttrToSpenes();
/*  349 */       this.spDlg.drawingLayer.resetElement(spenes);
/*  350 */       this.spDlg.mapEditor.refresh();
/*      */     }
/*      */   }
/*      */ 
/*      */   private String checkErr()
/*      */   {
/*  359 */     boolean showErr = false;
/*  360 */     String err = "The following Problems have not been identified:\n\n";
/*  361 */     System.out.println("Latest Data Used: " + this.comboLatestData.getText());
/*  362 */     if (this.comboLatestData.getText().isEmpty()) {
/*  363 */       showErr = true;
/*  364 */       err = err + "\nEntry of Latest Data is empty.\n";
/*      */     }
/*      */ 
/*  367 */     if (this.comboForecaster.getText().isEmpty()) {
/*  368 */       showErr = true;
/*  369 */       err = err + "\nEntry of Forecaster is empty.\n";
/*      */     }
/*  371 */     if (this.txtEvent.getText().isEmpty()) {
/*  372 */       showErr = true;
/*  373 */       err = err + "\nEvent is empty.\n";
/*      */     }
/*      */ 
/*  377 */     if (this.txtSatAnaTre.getText().isEmpty()) {
/*  378 */       err = err + "\nSatellite Analysis and Trends is empty.\n";
/*      */     }
/*      */ 
/*  381 */     if (showErr) {
/*  382 */       return err;
/*      */     }
/*  384 */     return "";
/*      */   }
/*      */ 
/*      */   public void resetPressed()
/*      */   {
/*  392 */     this.txtEvent.setText("");
/*  393 */     this.txtSatAnaTre.setText("");
/*  394 */     this.txtAddlInfo.setText("");
/*      */ 
/*  396 */     this.comboLatestData.deselectAll();
/*  397 */     this.comboForecaster.deselectAll();
/*  398 */     this.comboOutlookLevel.deselectAll();
/*      */   }
/*      */ 
/*      */   public void createButtonsForButtonBar(Composite parent)
/*      */   {
/*  409 */     createButton(parent, 0, "Apply", true);
/*  410 */     createButton(parent, FORMAT_ID, "Format SPENES", true);
/*  411 */     createButton(parent, RESET_ID, "Reset", true);
/*  412 */     createButton(parent, 1, "Cancel", true);
/*      */ 
/*  414 */     getButton(0).setEnabled(true);
/*  415 */     getButton(FORMAT_ID).setEnabled(true);
/*  416 */     getButton(RESET_ID).setEnabled(true);
/*  417 */     getButton(1).setEnabled(true);
/*      */   }
/*      */ 
/*      */   private void createArea1(Group top)
/*      */   {
/*  425 */     Label lblTime = new Label(top, 16384);
/*  426 */     lblTime.setText("ESTIMATE...DATA/TIME ");
/*      */ 
/*  428 */     Label lblTimeTxt = new Label(top, 16384);
/*  429 */     if (spenes.getInitDateTime() == null) spenes.setInitTime();
/*  430 */     lblTimeTxt.setText(spenes.getInitDateTime());
/*      */ 
/*  432 */     Label lblLtsData = new Label(top, 16385);
/*  433 */     lblLtsData.setText("LATEST DATA USED: ");
/*      */ 
/*  435 */     this.comboLatestData = new Combo(top, 4);
/*  436 */     if (DATATYPEARRAY != null) {
/*  437 */       for (String str : DATATYPEARRAY) {
/*  438 */         this.comboLatestData.add(str);
/*      */       }
/*  440 */       this.comboLatestData.select(1);
/*      */     }
/*  442 */     if (spenes.getLatestDataUsed() != null) {
/*  443 */       this.comboLatestData.setText(spenes.getLatestDataUsed());
/*      */     }
/*  445 */     this.comboLatestData.addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event e)
/*      */       {
/*      */       }
/*      */     });
/*  453 */     Label lblObsHr = new Label(top, 16384);
/*  454 */     lblObsHr.setText("Observation Time: ");
/*  455 */     Composite dt = new Composite(top, 0);
/*  456 */     GridLayout dtGL = new GridLayout(1, false);
/*  457 */     dt.setLayout(dtGL);
/*  458 */     this.dtObsHr = new DateTime(dt, 34944);
/*  459 */     if (spenes.getObsHr() >= 0)
/*  460 */       this.dtObsHr.setHours(spenes.getObsHr());
/*  461 */     this.dtObsHr.setMinutes(0);
/*  462 */     this.dtObsHr.addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */       }
/*      */     });
/*  468 */     Label lblForecaster = new Label(top, 16384);
/*  469 */     lblForecaster.setText("Forecaster: ");
/*  470 */     this.comboForecaster = new Combo(top, 4);
/*  471 */     if (FORECASTERS != null) {
/*  472 */       for (String str : FORECASTERS) {
/*  473 */         this.comboForecaster.add(str);
/*      */       }
/*      */     }
/*  476 */     if (spenes.getForecasters() != null) {
/*  477 */       this.comboForecaster.setText(spenes.getForecasters());
/*      */     }
/*  479 */     this.comboForecaster.addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event e)
/*      */       {
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void createArea2(Group top)
/*      */   {
/*  495 */     Label lblAbrv = new Label(top, 16384);
/*  496 */     lblAbrv.setText("States Abrv: ");
/*      */ 
/*  498 */     this.txtAbrv = new Text(top, 19008);
/*  499 */     this.txtAbrv.setLayoutData(this.singleTxtGridData);
/*      */ 
/*  501 */     System.out.println("State Z000: " + spenes.getStateZ000());
/*  502 */     this.txtAbrv.setText(spenes.getStateZ000());
/*  503 */     this.txtAbrv.addModifyListener(new TxtModifyListener());
/*      */ 
/*  505 */     Label lblLocation = new Label(top, 16384);
/*  506 */     lblLocation.setText("Location: ");
/*      */ 
/*  508 */     this.txtLocation = new Text(top, 19008);
/*  509 */     this.txtLocation.setLayoutData(this.singleTxtGridData);
/*  510 */     this.txtLocation.setText(spenes.getLocation());
/*  511 */     this.txtLocation.addModifyListener(new TxtModifyListener());
/*      */ 
/*  513 */     Label lblAttnWFOs = new Label(top, 16384);
/*  514 */     lblAttnWFOs.setText("Attn WFOs: ");
/*  515 */     this.txtAttnWFOs = new Text(top, 19008);
/*  516 */     this.txtAttnWFOs.setLayoutData(this.singleTxtGridData);
/*  517 */     this.txtAttnWFOs.setText(spenes.getAttnWFOs());
/*  518 */     this.txtAttnWFOs.addModifyListener(new TxtModifyListener());
/*      */ 
/*  520 */     Label lblAttnRFCs = new Label(top, 16384);
/*  521 */     lblAttnRFCs.setText("Attn RFCs: ");
/*  522 */     this.txtAttnRFCs = new Text(top, 19008);
/*  523 */     this.txtAttnRFCs.setLayoutData(this.singleTxtGridData);
/*  524 */     this.txtAttnRFCs.setText(spenes.getAttnRFCs());
/*  525 */     this.txtAttnRFCs.addModifyListener(new TxtModifyListener());
/*      */   }
/*      */ 
/*      */   private void createArea3(Group top)
/*      */   {
/*  532 */     Label lblEvent = new Label(top, 16384);
/*  533 */     lblEvent.setText("Event:     ");
/*      */ 
/*  536 */     this.txtEvent = new Text(top, 19008);
/*  537 */     this.txtEvent.setLayoutData(this.singleTxtGridData);
/*  538 */     if (spenes.getEvent() != null)
/*  539 */       this.txtEvent.setText(spenes.getEvent());
/*  540 */     this.txtEvent.addModifyListener(new TxtModifyListener());
/*      */ 
/*  542 */     this.btnSatAnaTre = new Button(top, 8);
/*  543 */     this.btnSatAnaTre.setText("S.A.T.");
/*      */ 
/*  545 */     this.btnSatAnaTre.setLayoutData(new GridData(60, 26));
/*  546 */     this.btnSatAnaTre.addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */         try {
/*  551 */           SpenesFormatDlg.SpenesSATDlg SATDlg = new SpenesFormatDlg.SpenesSATDlg(SpenesFormatDlg.this, SpenesFormatDlg.this.getShell());
/*  552 */           SATDlg.setBlockOnOpen(false);
/*  553 */           SATDlg.open();
/*      */         } catch (VizException e) {
/*  555 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*  561 */     this.txtSatAnaTre = new Text(top, 2626);
/*  562 */     GridData gData1 = new GridData(300, 96);
/*  563 */     gData1.horizontalAlignment = 7;
/*  564 */     this.txtSatAnaTre.setLayoutData(gData1);
/*  565 */     if (spenes.getSatAnalysisTrend() != null)
/*  566 */       this.txtSatAnaTre.setText(spenes.getSatAnalysisTrend());
/*  567 */     this.txtSatAnaTre.addModifyListener(new TxtModifyListener());
/*      */   }
/*      */ 
/*      */   private void createArea4(Group top)
/*      */   {
/*  575 */     Composite dt = new Composite(top, 0);
/*  576 */     GridLayout dtGL = new GridLayout(3, false);
/*  577 */     dt.setLayout(dtGL);
/*      */ 
/*  579 */     Label lblShortTermFrom = new Label(dt, 0);
/*  580 */     lblShortTermFrom.setText("ShortTerm: ");
/*      */ 
/*  583 */     this.dtTermsHr = new DateTime(dt, 34944);
/*  584 */     if (spenes.getShortTermBegin() >= 0)
/*  585 */       this.dtTermsHr.setHours(spenes.getShortTermBegin());
/*  586 */     this.dtTermsHr.setMinutes(0);
/*  587 */     this.dtTermsHr.addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */       }
/*      */     });
/*  591 */     this.dtTerms2Hr = new DateTime(dt, 34944);
/*  592 */     if (spenes.getShortTermEnd() >= 0)
/*  593 */       this.dtTerms2Hr.setHours(spenes.getShortTermEnd());
/*  594 */     this.dtTerms2Hr.setMinutes(0);
/*  595 */     this.dtTerms2Hr.addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void createArea5(Group top)
/*      */   {
/*  605 */     Label lblOutlookLevel = new Label(top, 16384);
/*  606 */     lblOutlookLevel.setText("Confidence: ");
/*      */ 
/*  608 */     this.comboOutlookLevel = new Combo(top, 4);
/*  609 */     if (spenes.getOutlookLevel() != null)
/*  610 */       this.comboOutlookLevel.setText(spenes.getOutlookLevel());
/*  611 */     if (OUTLOOKLEVEL != null) {
/*  612 */       for (String str : OUTLOOKLEVEL) {
/*  613 */         this.comboOutlookLevel.add(str);
/*      */       }
/*  615 */       this.comboOutlookLevel.select(2);
/*      */     }
/*      */ 
/*  618 */     if (spenes.getOutlookLevel() != null)
/*  619 */       this.comboOutlookLevel.setText(spenes.getOutlookLevel());
/*  620 */     this.comboOutlookLevel.addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event e)
/*      */       {
/*      */       }
/*      */     });
/*  628 */     this.btnAddlInfo = new Button(top, 8);
/*  629 */     this.btnAddlInfo.setText("H.P.");
/*  630 */     this.btnAddlInfo.setLayoutData(new GridData(60, 26));
/*  631 */     this.btnAddlInfo.addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */         try
/*      */         {
/*  637 */           SpenesFormatDlg.SpenesHPDlg HPDlg = new SpenesFormatDlg.SpenesHPDlg(SpenesFormatDlg.this, SpenesFormatDlg.this.getShell());
/*  638 */           HPDlg.setBlockOnOpen(false);
/*  639 */           HPDlg.open();
/*      */         }
/*      */         catch (VizException e) {
/*  642 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*  649 */     this.txtAddlInfo = new Text(top, 2626);
/*  650 */     GridData gData2 = new GridData(300, 96);
/*  651 */     gData2.horizontalAlignment = 7;
/*  652 */     this.txtAddlInfo.setLayoutData(gData2);
/*  653 */     if (spenes.getAddlInfo() != null)
/*  654 */       this.txtAddlInfo.setText(spenes.getAddlInfo());
/*  655 */     this.txtAddlInfo.addModifyListener(new TxtModifyListener());
/*      */   }
/*      */ 
/*      */   public static Document readForecasterTbl()
/*      */   {
/*  679 */     if (FORECASTERS == null) {
/*      */       try {
/*  681 */         String forecasterFile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/*  682 */           PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "forecasters.xml");
/*      */ 
/*  684 */         SAXReader reader = new SAXReader();
/*  685 */         forecasterTbl = reader.read(forecasterFile);
/*      */       } catch (Exception e) {
/*  687 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  691 */     return forecasterTbl;
/*      */   }
/*      */ 
/*      */   public static String[] getForecasters()
/*      */   {
/*  699 */     if (forecasterTbl == null) {
/*  700 */       FORECASTERS = new String[] { "BALDWIN", "BIRCH", "EVANS", "GALLINA" };
/*      */     } else {
/*  702 */       List list = new ArrayList();
/*  703 */       List nodes = forecasterTbl.selectNodes(FORECASTER_XPATH);
/*      */ 
/*  705 */       for (Node node : nodes) {
/*  706 */         list.add(node.valueOf("@name").toString());
/*      */       }
/*      */ 
/*  709 */       FORECASTERS = new String[list.size()];
/*  710 */       FORECASTERS = (String[])list.toArray(FORECASTERS);
/*      */     }
/*      */ 
/*  713 */     return FORECASTERS;
/*      */   }
/*      */ 
/*      */   public SpenesAttrDlg getSpDlg()
/*      */   {
/*  721 */     return this.spDlg;
/*      */   }
/*      */ 
/*      */   public void setSpDlg(SpenesAttrDlg spDlg)
/*      */   {
/*  729 */     this.spDlg = spDlg;
/*      */   }
/*      */ 
/*      */   public boolean close()
/*      */   {
/*  739 */     if (this.sfMsgDlg != null) this.sfMsgDlg.close();
/*  740 */     return super.close();
/*      */   }
/*      */ 
/*      */   public void copyAttrToSpenes()
/*      */   {
/*  747 */     spenes.setInitDateTime(spenes.getInitDateTime());
/*  748 */     spenes.setStateZ000(this.txtAbrv.getText());
/*  749 */     spenes.setLatestData(this.comboLatestData.getText());
/*  750 */     spenes.setObsHr(this.dtObsHr.getHours());
/*  751 */     spenes.setForecasters(this.comboForecaster.getText());
/*  752 */     spenes.setLocation(this.txtLocation.getText());
/*  753 */     spenes.setAttnWFOs(this.txtAttnWFOs.getText());
/*  754 */     spenes.setAttnRFCs(this.txtAttnRFCs.getText());
/*  755 */     spenes.setEvent(this.txtEvent.getText());
/*  756 */     spenes.setSatAnalysisTrend(this.txtSatAnaTre.getText());
/*  757 */     spenes.setShortTermBegin(this.dtTermsHr.getHours());
/*  758 */     spenes.setShortTermEnd(this.dtTerms2Hr.getHours());
/*  759 */     spenes.setOutlookLevel(this.comboOutlookLevel.getText());
/*  760 */     spenes.setAddlInfo(this.txtAddlInfo.getText());
/*      */   }
/*      */ 
/*      */   private class SpenesHPDlg extends CaveJFACEDialog
/*      */   {
/*  908 */     private int RESETHP_ID = 2012062602;
/*      */ 
/*  913 */     private Composite top = null;
/*      */     private Text txtHP;
/*  916 */     private String message = "";
/*      */ 
/*      */     protected SpenesHPDlg(Shell parShell)
/*      */       throws VizException
/*      */     {
/*  921 */       super();
/*      */     }
/*      */ 
/*      */     public Control createDialogArea(Composite parent)
/*      */     {
/*  929 */       getShell().setText("Edit Heavy Precipitation...");
/*      */ 
/*  931 */       this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/*  936 */       GridLayout mainLayout = new GridLayout(1, true);
/*  937 */       mainLayout.marginHeight = 3;
/*  938 */       mainLayout.marginWidth = 3;
/*  939 */       this.top.setLayout(mainLayout);
/*      */ 
/*  942 */       this.txtHP = new Text(this.top, 2626);
/*  943 */       this.txtHP.setFont(new Font(this.txtHP.getDisplay(), "Courier", 12, 0));
/*  944 */       this.txtHP.setLayoutData(new GridData(500, 600));
/*  945 */       this.txtHP.setText(SpenesFormatDlg.this.txtAddlInfo.getText());
/*  946 */       this.txtHP.addModifyListener(new SpenesFormatDlg.TxtModifyListener(SpenesFormatDlg.this));
/*      */ 
/*  948 */       return this.top;
/*      */     }
/*      */ 
/*      */     public void createButtonsForButtonBar(Composite parent)
/*      */     {
/*  958 */       createButton(parent, 0, "Ok", true);
/*  959 */       createButton(parent, this.RESETHP_ID, "Reset", true);
/*  960 */       createButton(parent, 1, "Cancel", true);
/*      */ 
/*  962 */       getButton(0).setEnabled(true);
/*  963 */       getButton(this.RESETHP_ID).setEnabled(true);
/*  964 */       getButton(1).setEnabled(true);
/*      */     }
/*      */ 
/*      */     protected void buttonPressed(int buttonId)
/*      */     {
/*  974 */       if (buttonId == 0)
/*  975 */         okPressed();
/*  976 */       else if (this.RESETHP_ID == buttonId)
/*  977 */         resetPressed();
/*  978 */       else if (1 == buttonId)
/*  979 */         cancelPressed();
/*      */     }
/*      */ 
/*      */     private void resetHP()
/*      */     {
/*  986 */       this.txtHP.setText("");
/*  987 */       this.message = "";
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/*  995 */       this.message = this.txtHP.getText();
/*  996 */       if (this.message != null) {
/*  997 */         SpenesFormatDlg.this.txtAddlInfo.setText(this.message);
/*      */       }
/*  999 */       close();
/*      */     }
/*      */ 
/*      */     public void resetPressed()
/*      */     {
/* 1006 */       resetHP();
/*      */     }
/*      */ 
/*      */     public int open()
/*      */     {
/* 1015 */       if (getShell() == null) {
/* 1016 */         create();
/*      */       }
/* 1018 */       getButtonBar().pack();
/* 1019 */       return super.open();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SpenesSATDlg extends CaveJFACEDialog
/*      */   {
/*  769 */     private int RESETSAT_ID = 2012062601;
/*      */ 
/*  774 */     private Composite top = null;
/*      */     private Text txtSAT;
/*  777 */     private String message = "";
/*      */ 
/*      */     protected SpenesSATDlg(Shell parShell)
/*      */       throws VizException
/*      */     {
/*  782 */       super();
/*      */     }
/*      */ 
/*      */     public Control createDialogArea(Composite parent)
/*      */     {
/*  790 */       getShell().setText("Edit Satellite Analysis and Trends ...");
/*      */ 
/*  792 */       this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/*  797 */       GridLayout mainLayout = new GridLayout(1, true);
/*  798 */       mainLayout.marginHeight = 3;
/*  799 */       mainLayout.marginWidth = 3;
/*  800 */       this.top.setLayout(mainLayout);
/*      */ 
/*  803 */       this.txtSAT = new Text(this.top, 2626);
/*  804 */       this.txtSAT.setFont(new Font(this.txtSAT.getDisplay(), "Courier", 12, 0));
/*  805 */       this.txtSAT.setLayoutData(new GridData(500, 600));
/*  806 */       this.txtSAT.setText(SpenesFormatDlg.this.txtSatAnaTre.getText());
/*  807 */       this.txtSAT.addModifyListener(new SpenesFormatDlg.TxtModifyListener(SpenesFormatDlg.this));
/*      */ 
/*  809 */       return this.top;
/*      */     }
/*      */ 
/*      */     public void createButtonsForButtonBar(Composite parent)
/*      */     {
/*  824 */       createButton(parent, 0, "Ok", true);
/*  825 */       createButton(parent, this.RESETSAT_ID, "Reset", true);
/*  826 */       createButton(parent, 1, "Cancel", true);
/*      */ 
/*  828 */       getButton(0).setEnabled(true);
/*  829 */       getButton(this.RESETSAT_ID).setEnabled(true);
/*  830 */       getButton(1).setEnabled(true);
/*      */     }
/*      */ 
/*      */     protected void buttonPressed(int buttonId)
/*      */     {
/*  841 */       if (buttonId == 0)
/*  842 */         okPressed();
/*  843 */       else if (this.RESETSAT_ID == buttonId)
/*  844 */         resetPressed();
/*  845 */       else if (1 == buttonId)
/*  846 */         cancelPressed();
/*      */     }
/*      */ 
/*      */     private void resetSAT()
/*      */     {
/*  856 */       this.txtSAT.setText("");
/*  857 */       this.message = "";
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/*  865 */       this.message = this.txtSAT.getText();
/*  866 */       if (this.message != null) {
/*  867 */         SpenesFormatDlg.this.txtSatAnaTre.setText(this.message);
/*      */       }
/*  869 */       close();
/*      */     }
/*      */ 
/*      */     public void cancelPressed()
/*      */     {
/*  874 */       super.cancelPressed();
/*      */     }
/*      */ 
/*      */     public void resetPressed()
/*      */     {
/*  881 */       resetSAT();
/*      */     }
/*      */ 
/*      */     public int open()
/*      */     {
/*  890 */       if (getShell() == null) {
/*  891 */         create();
/*      */       }
/*      */ 
/*  895 */       getButtonBar().pack();
/*  896 */       return super.open();
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TxtModifyListener
/*      */     implements ModifyListener
/*      */   {
/*      */     public TxtModifyListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void modifyText(ModifyEvent e)
/*      */     {
/*  666 */       if (!(e.widget instanceof Text))
/*      */       {
/*  668 */         (e.widget instanceof Combo);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.SpenesFormatDlg
 * JD-Core Version:    0.6.2
 */