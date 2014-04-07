/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.ISigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*      */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*      */ import java.awt.Color;
/*      */ import java.beans.PropertyDescriptor;
/*      */ import java.io.File;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.text.DateFormat;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import org.apache.commons.beanutils.BeanUtils;
/*      */ import org.eclipse.jface.dialogs.IDialogConstants;
/*      */ import org.eclipse.swt.events.FocusEvent;
/*      */ import org.eclipse.swt.events.FocusListener;
/*      */ import org.eclipse.swt.events.KeyEvent;
/*      */ import org.eclipse.swt.events.KeyListener;
/*      */ import org.eclipse.swt.graphics.Point;
/*      */ import org.eclipse.swt.graphics.RGB;
/*      */ import org.eclipse.swt.graphics.Rectangle;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Listener;
/*      */ import org.eclipse.swt.widgets.Menu;
/*      */ import org.eclipse.swt.widgets.MenuItem;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Spinner;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ import org.eclipse.swt.widgets.ToolBar;
/*      */ import org.eclipse.swt.widgets.ToolItem;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ 
/*      */ public class SigmetAttrDlg extends AttrDlg
/*      */   implements ISigmet
/*      */ {
/*  110 */   private static SigmetAttrDlg INSTANCE = null;
/*      */ 
/*  112 */   private static String mouseHandlerName = null;
/*      */ 
/*  114 */   private HashMap<String, Object> attr = new HashMap();
/*      */   public static final String AREA = "Area";
/*      */   public static final String LINE = "Line";
/*      */   public static final String ISOLATED = "Isolated";
/*  119 */   private String lineType = "Area";
/*      */ 
/*  121 */   private String origLineType = this.lineType;
/*      */   private static final String WIDTH = "10.00";
/*  125 */   private String widthStr = "10.00";
/*      */ 
/*  127 */   private static final String[] LINE_SIDES = { "ESOL", "NOF", 
/*  128 */     "SOF", "EOF", "WOF" };
/*      */ 
/*  130 */   private String sideOfLine = LINE_SIDES[0];
/*      */ 
/*  132 */   private DrawableElement sigmet = null;
/*      */ 
/*  134 */   protected Composite top = null;
/*      */ 
/*  136 */   protected ColorButtonSelector cs = null;
/*      */ 
/*  138 */   private boolean withExpandedArea = false; private boolean copiedToSigmet = false;
/*  139 */   private boolean comboPhenomCalled = false;
/*      */ 
/*  141 */   private boolean tropCycFlag = false;
/*      */   private Text txtInfo;
/*      */   private Control detailsArea;
/*      */   private Point cachedWindowSize;
/*      */   private String latLonFormatFlagAndText;
/*      */   private String editableAttrArea;
/*      */   private String editableAttrIssueOffice;
/*      */   private String editableAttrStatus;
/*      */   private String editableAttrId;
/*      */   private String editableAttrSeqNum;
/*      */   private String editableAttrStartTime;
/*      */   private String editableAttrEndTime;
/*      */   private String editableAttrRemarks;
/*      */   private String editableAttrPhenom;
/*      */   private String editableAttrPhenom2;
/*      */   private String editableAttrPhenomName;
/*      */   private String editableAttrPhenomLat;
/*      */   private String editableAttrPhenomLon;
/*      */   private String editableAttrPhenomPressure;
/*      */   private String editableAttrPhenomMaxWind;
/*      */   private String editableAttrFreeText;
/*      */   private String editableAttrTrend;
/*      */   private String editableAttrMovement;
/*      */   private String editableAttrPhenomSpeed;
/*      */   private String editableAttrPhenomDirection;
/*      */   private String editableAttrLevel;
/*      */   private String editableAttrLevelInfo1;
/*      */   private String editableAttrLevelInfo2;
/*      */   private String editableAttrLevelText1;
/*      */   private String editableAttrLevelText2;
/*      */   private String editableAttrFromLine;
/*      */   private String editableAttrFir;
/*  205 */   private HashMap<String, Control> attrControlMap = new HashMap();
/*      */ 
/*  207 */   private HashMap<String, Button[]> attrButtonMap = new HashMap();
/*      */ 
/*  209 */   private HashMap<Control, Control[]> controlEnablerMap = new HashMap();
/*      */ 
/*      */   protected SigmetAttrDlg(Shell parShell) throws VizException {
/*  212 */     super(parShell);
/*      */   }
/*      */ 
/*      */   public static SigmetAttrDlg getInstance(Shell parShell) {
/*  216 */     if (INSTANCE == null) {
/*      */       try {
/*  218 */         INSTANCE = new SigmetAttrDlg(parShell);
/*      */       } catch (VizException e) {
/*  220 */         e.printStackTrace();
/*      */       }
/*      */     }
/*  223 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   private void resetText(String coorsLatLon, Text txtInfo)
/*      */   {
/*  228 */     StringBuilder sb = new StringBuilder();
/*  229 */     String[] strings = coorsLatLon.split(":::");
/*  230 */     for (int i = 0; i < strings.length; i++) {
/*  231 */       if ((i != 0) && (i % 6 == 0))
/*  232 */         sb.append("\n");
/*  233 */       if ((i != strings.length - 1) || (
/*  234 */         (!"New".equals(strings[i])) && (!"Old".equals(strings[i])) && 
/*  235 */         (!"VOR"
/*  235 */         .equals(strings[i]))))
/*      */       {
/*  237 */         sb.append(strings[i] + "  ");
/*      */       }
/*      */     }
/*  240 */     txtInfo.setText(sb.toString());
/*      */   }
/*      */ 
/*      */   public void okPressed()
/*      */   {
/*  245 */     ArrayList adcList = null;
/*  246 */     ArrayList newList = new ArrayList();
/*      */ 
/*  249 */     if (this.drawingLayer != null) {
/*  250 */       adcList = (ArrayList)this.drawingLayer
/*  251 */         .getAllSelected();
/*      */     }
/*      */ 
/*  254 */     if ((adcList != null) && (!adcList.isEmpty()))
/*      */     {
/*  257 */       for (AbstractDrawableComponent adc : adcList)
/*      */       {
/*  259 */         Sigmet el = (Sigmet)adc.getPrimaryDE();
/*      */ 
/*  261 */         if (el != null)
/*      */         {
/*  263 */           Sigmet newEl = (Sigmet)el.copy();
/*      */ 
/*  266 */           copyEditableAttrToSigmet(newEl);
/*      */ 
/*  270 */           newEl = convertType(newEl);
/*      */ 
/*  272 */           newList.add(newEl);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  277 */       ArrayList oldList = new ArrayList(
/*  278 */         adcList);
/*  279 */       this.drawingLayer.replaceElements(oldList, newList);
/*      */     }
/*      */ 
/*  283 */     this.drawingLayer.removeSelected();
/*  284 */     for (AbstractDrawableComponent adc : newList) {
/*  285 */       this.drawingLayer.addSelected(adc);
/*      */     }
/*      */ 
/*  288 */     if (this.mapEditor != null)
/*  289 */       this.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   public void setMouseHandlerName(String mhName)
/*      */   {
/*  295 */     mouseHandlerName = mhName;
/*      */   }
/*      */ 
/*      */   public Color[] getColors()
/*      */   {
/*  304 */     Color[] colors = new Color[2];
/*  305 */     colors[0] = new Color(this.cs.getColorValue().red, 
/*  306 */       this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*  307 */     colors[1] = Color.green;
/*      */ 
/*  309 */     return colors;
/*      */   }
/*      */ 
/*      */   private void setColor(Color clr) {
/*  313 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*      */   }
/*      */ 
/*      */   public String getLineType() {
/*  317 */     return this.lineType;
/*      */   }
/*      */ 
/*      */   public void setLineType(String lType) {
/*  321 */     setOrigLineType(getLineType());
/*  322 */     this.lineType = lType;
/*      */   }
/*      */ 
/*      */   public String getOrigLineType() {
/*  326 */     return this.origLineType;
/*      */   }
/*      */ 
/*      */   public void setOrigLineType(String lType) {
/*  330 */     this.origLineType = lType;
/*      */   }
/*      */ 
/*      */   public String getSideOfLine() {
/*  334 */     return this.sideOfLine;
/*      */   }
/*      */ 
/*      */   public void setSideOfLine(String lineSideString) {
/*  338 */     this.sideOfLine = lineSideString;
/*      */   }
/*      */ 
/*      */   public double getWidth() {
/*  342 */     return Double.parseDouble(this.widthStr);
/*      */   }
/*      */ 
/*      */   public String getWidthStr() {
/*  346 */     return this.widthStr;
/*      */   }
/*      */ 
/*      */   public void setWidthStr(String widthString) {
/*  350 */     this.widthStr = widthString;
/*      */   }
/*      */ 
/*      */   private double stringToDouble(String s) {
/*  354 */     double dValue = 10.0D;
/*      */     try {
/*  356 */       dValue = Double.parseDouble(s);
/*  357 */       dValue = (dValue < 0.0D) || (dValue > 5000.0D) ? 10.0D : dValue;
/*      */     } catch (NumberFormatException localNumberFormatException) {
/*      */     }
/*      */     finally {
/*  361 */       return dValue;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute attr) {
/*  366 */     Color clr = attr.getColors()[0];
/*  367 */     if (clr != null)
/*  368 */       setColor(clr);
/*      */   }
/*      */ 
/*      */   public int getSmoothFactor()
/*      */   {
/*  376 */     return 0;
/*      */   }
/*      */ 
/*      */   public String getLinePattern() {
/*  380 */     return "Solid Line";
/*      */   }
/*      */ 
/*      */   public FillPatternList.FillPattern getFillPattern() {
/*  384 */     return FillPatternList.FillPattern.FILL_PATTERN_0;
/*      */   }
/*      */ 
/*      */   public boolean isClosed() {
/*  388 */     return false;
/*      */   }
/*      */ 
/*      */   public Boolean isFilled() {
/*  392 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public float getLineWidth() {
/*  396 */     return 1.0F;
/*      */   }
/*      */ 
/*      */   public double getSizeScale() {
/*  400 */     return 2.0D;
/*      */   }
/*      */ 
/*      */   public void createButtonsForButtonBar(Composite parent)
/*      */   {
/*  409 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/*  410 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*      */ 
/*  412 */     if (("Pgen Select".equals(mouseHandlerName)) || (this.withExpandedArea)) {
/*  413 */       createButton(parent, 20091020, "Save", false);
/*  414 */       createButton(parent, 20091021, "Apply", false);
/*      */ 
/*  416 */       getButton(20091020).setEnabled(true);
/*  417 */       getButton(20091020).addListener(13, new Listener()
/*      */       {
/*      */         public void handleEvent(Event e) {
/*  420 */           Sigmet sig = (Sigmet)SigmetAttrDlg.this.getSigmet();
/*  421 */           SigmetAttrDlg.this.okPressed();
/*      */ 
/*  423 */           SigmetAttrDlg.SigmetAttrDlgSaveMsgDlg md = null;
/*      */           try {
/*  425 */             md = new SigmetAttrDlg.SigmetAttrDlgSaveMsgDlg(SigmetAttrDlg.this, SigmetAttrDlg.this.getShell());
/*      */           } catch (Exception ee) {
/*  427 */             System.out.println(ee.getMessage());
/*      */           }
/*  429 */           if (md != null)
/*  430 */             md.open();
/*      */         }
/*      */       });
/*  434 */       getButton(20091021).setEnabled(true);
/*  435 */       getButton(20091021).addListener(13, new Listener()
/*      */       {
/*      */         public void handleEvent(Event e)
/*      */         {
/*  439 */           Sigmet sig = (Sigmet)SigmetAttrDlg.this.getSigmet();
/*  440 */           SigmetAttrDlg.this.okPressed();
/*      */         }
/*      */       });
/*  444 */       getButton(20091020).setLayoutData(
/*  445 */         new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*  446 */       getButton(20091021).setLayoutData(
/*  447 */         new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*      */     } else {
/*  449 */       createButton(parent, 0, 
/*  450 */         IDialogConstants.OK_LABEL, true);
/*  451 */       getButton(0).setLayoutData(
/*  452 */         new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*      */     }
/*      */ 
/*  455 */     createButton(parent, 1, 
/*  456 */       IDialogConstants.CANCEL_LABEL, false);
/*  457 */     getButton(1).setLayoutData(
/*  458 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*      */ 
/*  460 */     setMouseHandlerName(null);
/*  461 */     this.withExpandedArea = false;
/*      */   }
/*      */ 
/*      */   public void enableButtons()
/*      */   {
/*  468 */     getButton(1).setEnabled(true);
/*      */   }
/*      */ 
/*      */   private Control createDetailsArea(Composite parent)
/*      */   {
/*  474 */     Composite top5 = (Composite)super.createDialogArea(parent);
/*  475 */     GridData gdText = new GridData();
/*  476 */     gdText.widthHint = 66;
/*  477 */     GridLayout mainLayout5 = new GridLayout(8, false);
/*  478 */     mainLayout5.marginHeight = 3;
/*  479 */     mainLayout5.marginWidth = 3;
/*  480 */     top5.setLayout(mainLayout5);
/*      */ 
/*  482 */     Group top_3 = new Group(top5, 16384);
/*  483 */     top_3.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/*  484 */     top_3.setLayout(new GridLayout(8, false));
/*      */ 
/*  486 */     Button btnNewUpdate = new Button(top_3, 16);
/*  487 */     btnNewUpdate.setText("New/Update");
/*  488 */     btnNewUpdate.setSelection(true);
/*  489 */     setEditableAttrStatus("0");
/*  490 */     btnNewUpdate.setLayoutData(new GridData(16384, 16777216, true, 
/*  491 */       false, 2, 1));
/*  492 */     btnNewUpdate.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  494 */         SigmetAttrDlg.this.setEditableAttrStatus("0");
/*      */       }
/*      */     });
/*  498 */     Button btnAmend = new Button(top_3, 16);
/*  499 */     btnAmend.setText("Amend");
/*  500 */     btnAmend.setLayoutData(new GridData(16384, 16777216, true, false, 
/*  501 */       2, 1));
/*  502 */     btnAmend.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  504 */         SigmetAttrDlg.this.setEditableAttrStatus("1");
/*      */       }
/*      */     });
/*  508 */     Button btnCancel = new Button(top_3, 16);
/*  509 */     btnCancel.setText("Cancel");
/*  510 */     btnCancel.setLayoutData(new GridData(16384, 16777216, true, false, 
/*  511 */       4, 1));
/*  512 */     btnCancel.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  514 */         SigmetAttrDlg.this.setEditableAttrStatus("2");
/*      */       }
/*      */     });
/*  517 */     this.attrButtonMap.put("editableAttrStatus", new Button[] { btnNewUpdate, 
/*  518 */       btnAmend, btnCancel });
/*      */ 
/*  520 */     Label lblValidFrom = new Label(top_3, 16384);
/*  521 */     lblValidFrom.setText("Valid from:");
/*      */ 
/*  523 */     final Text txtValidFrom = new Text(top_3, 18432);
/*  524 */     this.attrControlMap.put("editableAttrStartTime", txtValidFrom);
/*  525 */     String startTime = (this.editableAttrStartTime == null) || 
/*  526 */       (this.editableAttrStartTime.equals("")) ? 
/*  527 */       getTimeStringPlusHourInHMS(0) : this.editableAttrStartTime;
/*  528 */     txtValidFrom.setText(startTime);
/*  529 */     setEditableAttrStartTime(txtValidFrom.getText());
/*      */ 
/*  532 */     txtValidFrom.addListener(25, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  534 */         e.doit = SigmetAttrDlg.this.validateHMSInput(e, txtValidFrom);
/*  535 */         if (!e.doit)
/*  536 */           return;
/*  537 */         SigmetAttrDlg.this.setEditableAttrStartTime(txtValidFrom
/*  538 */           .getText());
/*      */       }
/*      */     });
/*  541 */     txtValidFrom
/*  542 */       .addFocusListener(new FocusListener() {
/*      */       public void focusGained(FocusEvent e) {
/*      */       }
/*      */ 
/*      */       public void focusLost(FocusEvent e) {
/*  547 */         String timeString = txtValidFrom.getText();
/*  548 */         if ((timeString == null) || (timeString.length() != 6) || 
/*  549 */           (!SigmetAttrDlg.this.validateTimeStringInHMS(timeString))) {
/*  550 */           txtValidFrom.setText(SigmetAttrDlg.this.getTimeStringPlusHourInHMS(0));
/*      */         }
/*  552 */         SigmetAttrDlg.this
/*  553 */           .setEditableAttrStartTime(txtValidFrom
/*  554 */           .getText());
/*      */       }
/*      */     });
/*  557 */     txtValidFrom.addKeyListener(new KeyListener() {
/*      */       public void keyPressed(KeyEvent e) {
/*  559 */         if ((e.keyCode == 16777296) || (e.keyCode == 13)) {
/*  560 */           String timeString = txtValidFrom.getText();
/*  561 */           if ((timeString == null) || (timeString.length() != 6) || 
/*  562 */             (!SigmetAttrDlg.this.validateTimeStringInHMS(timeString))) {
/*  563 */             txtValidFrom.setText(SigmetAttrDlg.this.getTimeStringPlusHourInHMS(0));
/*      */           }
/*      */         }
/*  566 */         SigmetAttrDlg.this.setEditableAttrStartTime(txtValidFrom
/*  567 */           .getText());
/*      */       }
/*      */ 
/*      */       public void keyReleased(KeyEvent e)
/*      */       {
/*      */       }
/*      */     });
/*  574 */     Label lblTo = new Label(top_3, 16384);
/*  575 */     lblTo.setText("To:");
/*      */ 
/*  577 */     final Text txtTo = new Text(top_3, 18432);
/*  578 */     this.attrControlMap.put("editableAttrEndTime", txtTo);
/*  579 */     String endTime = (this.editableAttrEndTime == null) || 
/*  580 */       (this.editableAttrEndTime.equals("")) ? 
/*  581 */       getTimeStringPlusHourInHMS(4) : this.editableAttrEndTime;
/*  582 */     txtTo.setText(endTime);
/*  583 */     setEditableAttrEndTime(txtTo.getText());
/*      */ 
/*  586 */     txtTo.addListener(25, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  588 */         e.doit = SigmetAttrDlg.this.validateHMSInput(e, txtTo);
/*  589 */         if (!e.doit)
/*  590 */           return;
/*  591 */         SigmetAttrDlg.this.setEditableAttrEndTime(txtTo.getText());
/*      */       }
/*      */     });
/*  594 */     txtTo.addFocusListener(new FocusListener() {
/*      */       public void focusGained(FocusEvent e) {
/*      */       }
/*      */ 
/*      */       public void focusLost(FocusEvent e) {
/*  599 */         String timeString = txtValidFrom.getText();
/*  600 */         if ((timeString == null) || (timeString.length() != 6) || 
/*  601 */           (!SigmetAttrDlg.this.validateTimeStringInHMS(timeString))) {
/*  602 */           txtValidFrom.setText(SigmetAttrDlg.this.getTimeStringPlusHourInHMS(4));
/*      */         }
/*  604 */         SigmetAttrDlg.this.setEditableAttrEndTime(txtTo.getText());
/*      */       }
/*      */     });
/*  607 */     txtTo.addKeyListener(new KeyListener() {
/*      */       public void keyPressed(KeyEvent e) {
/*  609 */         if ((e.keyCode == 16777296) || (e.keyCode == 13)) {
/*  610 */           String timeString = txtValidFrom.getText();
/*  611 */           if ((timeString == null) || (timeString.length() != 6) || 
/*  612 */             (!SigmetAttrDlg.this.validateTimeStringInHMS(timeString))) {
/*  613 */             txtValidFrom.setText(SigmetAttrDlg.this.getTimeStringPlusHourInHMS(4));
/*      */           }
/*      */         }
/*  616 */         SigmetAttrDlg.this.setEditableAttrEndTime(txtTo.getText());
/*      */       }
/*      */ 
/*      */       public void keyReleased(KeyEvent e)
/*      */       {
/*      */       }
/*      */     });
/*  623 */     Label lblStartPlus = new Label(top_3, 16384);
/*  624 */     lblStartPlus.setText("Start plus:");
/*  625 */     Button btnStartPlus4hrs = new Button(top_3, 8);
/*  626 */     btnStartPlus4hrs.setText("4hrs");
/*  627 */     btnStartPlus4hrs.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  629 */         if (txtValidFrom.getText().length() != 6)
/*  630 */           return;
/*  631 */         txtTo.setText(SigmetAttrDlg.this.convertTimeStringPlusHourInHMS(
/*  632 */           txtValidFrom.getText(), 4, true));
/*      */       }
/*      */     });
/*  636 */     Button btnStartPlus6hrs = new Button(top_3, 8);
/*  637 */     btnStartPlus6hrs.setText("6hrs");
/*  638 */     btnStartPlus6hrs.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  640 */         if (txtValidFrom.getText().length() != 6)
/*  641 */           return;
/*  642 */         txtTo.setText(SigmetAttrDlg.this.convertTimeStringPlusHourInHMS(
/*  643 */           txtValidFrom.getText(), 6, true));
/*      */       }
/*      */     });
/*  647 */     Label lblDummy5_1 = new Label(top_3, 16384);
/*      */ 
/*  649 */     Label lblPhenom = new Label(top_3, 16384);
/*  650 */     lblPhenom.setText("Phenom:");
/*      */ 
/*  652 */     final Combo comboPhenom = new Combo(top_3, 16392);
/*  653 */     this.attrControlMap.put("editableAttrPhenom", comboPhenom);
/*  654 */     comboPhenom
/*  655 */       .setItems(getPhenomenons(
/*  656 */       SigmetInfo.getSigmetTypeString(this.pgenType)));
/*  657 */     setControl(comboPhenom, "editableAttrPhenom");
/*  658 */     comboPhenom.setLayoutData(new GridData(16384, 16777216, true, 
/*  659 */       true, 7, 1));
/*      */ 
/*  662 */     comboPhenom.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  664 */         SigmetAttrDlg.this.setEditableAttrPhenom(comboPhenom.getText());
/*  665 */         SigmetAttrDlg.this.withExpandedArea = true;
/*  666 */         SigmetAttrDlg.this.tropCycFlag = "TROPICAL_CYCLONE".equals(SigmetAttrDlg.this.editableAttrPhenom);
/*  667 */         SigmetAttrDlg.this.copyEditableAttrToSigmet(
/*  668 */           (Sigmet)SigmetAttrDlg.this
/*  668 */           .getSigmet());
/*      */       }
/*      */     });
/*  674 */     if (("TROPICAL_CYCLONE".equals(this.editableAttrPhenom)) || 
/*  675 */       ("VOLCANIC_ASH".equals(this.editableAttrPhenom))) {
/*  676 */       Group top_phenom = new Group(top5, 16384);
/*  677 */       top_phenom.setLayoutData(new GridData(4, 16777216, true, 
/*  678 */         true, 8, 1));
/*  679 */       top_phenom.setLayout(new GridLayout(8, false));
/*      */ 
/*  681 */       Shell shell = getShell();
/*  682 */       Label lblSEPhenomName = new Label(top_phenom, 16384);
/*  683 */       lblSEPhenomName.setText("Select / Enter\nPhenom Name: ");
/*      */ 
/*  687 */       final Text txtSEPhenomName = new Text(top_phenom, 18432);
/*      */ 
/*  689 */       this.attrControlMap.put("editableAttrPhenomName", txtSEPhenomName);
/*  690 */       txtSEPhenomName.setLayoutData(new GridData(4, 16777216, 
/*  691 */         true, false, 6, 1));
/*  692 */       txtSEPhenomName.addListener(24, new Listener() {
/*      */         public void handleEvent(Event e) {
/*  694 */           SigmetAttrDlg.this
/*  695 */             .setEditableAttrPhenomName(txtSEPhenomName
/*  696 */             .getText());
/*      */         }
/*      */       });
/*  700 */       final ToolBar tb = new ToolBar(top_phenom, 256);
/*  701 */       final ToolItem ti = new ToolItem(tb, 4);
/*  702 */       if ("TROPICAL_CYCLONE".equals(this.editableAttrPhenom)) {
/*  703 */         ti.setEnabled(false);
/*      */       }
/*  705 */       final Menu mu = new Menu(shell, 8);
/*      */ 
/*  707 */       for (int i = 0; i < SigmetInfo.VOL_NAME_BUCKET_ARRAY.length; i++) {
/*  708 */         if (i == 0) {
/*  709 */           MenuItem mi1 = new MenuItem(mu, 8);
/*  710 */           mi1.setText(SigmetInfo.VOL_NAME_BUCKET_ARRAY[i]);
/*      */         } else {
/*  712 */           MenuItem mi1 = new MenuItem(mu, 64);
/*  713 */           mi1.setText(SigmetInfo.VOL_NAME_BUCKET_ARRAY[i]);
/*  714 */           Menu mi1Menu = new Menu(shell, 4);
/*  715 */           mi1.setMenu(mi1Menu);
/*      */ 
/*  717 */           List list = 
/*  718 */             (List)SigmetInfo.VOLCANO_BUCKET_MAP
/*  718 */             .get(SigmetInfo.VOL_NAME_BUCKET_ARRAY[i]);
/*  719 */           int size = list.size();
/*  720 */           for (int j = 0; j < size; j++) {
/*  721 */             final MenuItem mi1MenuMi1 = new MenuItem(mi1Menu, 
/*  722 */               8);
/*  723 */             mi1MenuMi1.setText((String)list.get(j));
/*  724 */             mi1MenuMi1.addListener(13, new Listener() {
/*      */               public void handleEvent(Event e) {
/*  726 */                 txtSEPhenomName.setText(mi1MenuMi1.getText());
/*      */               }
/*      */             });
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  733 */       ti.addListener(13, new Listener()
/*      */       {
/*      */         public void handleEvent(Event event) {
/*  736 */           Rectangle bounds = ti.getBounds();
/*  737 */           Point point = tb.toDisplay(bounds.x, bounds.y + 
/*  738 */             bounds.height);
/*  739 */           mu.setLocation(point);
/*  740 */           mu.setVisible(true);
/*      */         }
/*      */       });
/*  744 */       Label lblPheLat = new Label(top_phenom, 16384);
/*  745 */       lblPheLat.setText("Phenom\nLat: ");
/*  746 */       final Text txtPheLat = new Text(top_phenom, 18432);
/*  747 */       this.attrControlMap.put("editableAttrPhenomLat", txtPheLat);
/*      */ 
/*  749 */       txtPheLat.addListener(24, new Listener() {
/*      */         public void handleEvent(Event e) {
/*  751 */           String phenomLat = SigmetAttrDlg.this.getPhenomLatLon(txtPheLat.getText()
/*  752 */             .trim(), true);
/*  753 */           if (!"".equals(phenomLat))
/*  754 */             SigmetAttrDlg.this.setEditableAttrPhenomLat(phenomLat);
/*      */           else
/*  756 */             SigmetAttrDlg.this.setEditableAttrPhenomLat(null);
/*      */         }
/*      */       });
/*  759 */       txtPheLat.addFocusListener(new FocusListener()
/*      */       {
/*      */         public void focusGained(FocusEvent e)
/*      */         {
/*      */         }
/*      */ 
/*      */         public void focusLost(FocusEvent e) {
/*  766 */           if (SigmetAttrDlg.this.getEditableAttrPhenomLat() != null)
/*  767 */             txtPheLat.setText(SigmetAttrDlg.this
/*  768 */               .getEditableAttrPhenomLat());
/*      */           else
/*  770 */             txtPheLat.setText("???");
/*      */         }
/*      */       });
/*  774 */       txtPheLat.setLayoutData(gdText);
/*      */ 
/*  776 */       Label lblPheLon = new Label(top_phenom, 16384);
/*  777 */       lblPheLon.setText("Phenom\nLon: ");
/*  778 */       final Text txtPheLon = new Text(top_phenom, 18432);
/*  779 */       this.attrControlMap.put("editableAttrPhenomLon", txtPheLon);
/*      */ 
/*  781 */       txtPheLon.addListener(24, new Listener() {
/*      */         public void handleEvent(Event e) {
/*  783 */           String phenomLon = SigmetAttrDlg.this.getPhenomLatLon(txtPheLon.getText()
/*  784 */             .trim(), false);
/*  785 */           if (!"".equals(phenomLon))
/*  786 */             SigmetAttrDlg.this.setEditableAttrPhenomLon(phenomLon);
/*      */           else
/*  788 */             SigmetAttrDlg.this.setEditableAttrPhenomLon(null);
/*      */         }
/*      */       });
/*  791 */       txtPheLon.addFocusListener(new FocusListener()
/*      */       {
/*      */         public void focusGained(FocusEvent e)
/*      */         {
/*      */         }
/*      */ 
/*      */         public void focusLost(FocusEvent e) {
/*  798 */           if (SigmetAttrDlg.this.getEditableAttrPhenomLon() != null)
/*  799 */             txtPheLon.setText(SigmetAttrDlg.this
/*  800 */               .getEditableAttrPhenomLon());
/*      */           else
/*  802 */             txtPheLon.setText("???");
/*      */         }
/*      */       });
/*  806 */       txtPheLon.setLayoutData(gdText);
/*      */ 
/*  808 */       Label lblPressure = new Label(top_phenom, 16384);
/*  809 */       lblPressure.setEnabled(this.tropCycFlag);
/*  810 */       lblPressure.setText("Pressure\nHPA: ");
/*  811 */       final Text txtPressure = new Text(top_phenom, 18432);
/*  812 */       txtPressure.setEnabled(this.tropCycFlag);
/*      */ 
/*  814 */       txtPressure.addListener(24, new Listener() {
/*      */         public void handleEvent(Event e) {
/*  816 */           if (SigmetAttrDlg.this.validateNumInput(txtPressure.getText()))
/*  817 */             SigmetAttrDlg.this
/*  818 */               .setEditableAttrPhenomPressure(txtPressure
/*  819 */               .getText());
/*      */         }
/*      */       });
/*  823 */       txtPressure.setLayoutData(gdText);
/*  824 */       this.attrControlMap.put("editableAttrPhenomPressure", txtPressure);
/*      */ 
/*  826 */       Label lblMaxWinds = new Label(top_phenom, 16384);
/*  827 */       lblMaxWinds.setEnabled(this.tropCycFlag);
/*  828 */       lblMaxWinds.setText("Max\nWinds: ");
/*  829 */       final Text txtMaxWinds = new Text(top_phenom, 18432);
/*  830 */       txtMaxWinds.setEnabled(this.tropCycFlag);
/*  831 */       setEditableAttrPhenomMaxWind(txtMaxWinds.getText());
/*      */ 
/*  833 */       txtMaxWinds.addListener(24, new Listener() {
/*      */         public void handleEvent(Event e) {
/*  835 */           if (SigmetAttrDlg.this.validateNumInput(txtMaxWinds.getText()))
/*  836 */             SigmetAttrDlg.this
/*  837 */               .setEditableAttrPhenomMaxWind(txtMaxWinds
/*  838 */               .getText());
/*      */         }
/*      */       });
/*  842 */       txtMaxWinds.setLayoutData(gdText);
/*  843 */       this.attrControlMap.put("editableAttrPhenomMaxWind", txtMaxWinds);
/*      */     }
/*      */ 
/*  850 */     final Group top_4 = new Group(top5, 16384);
/*  851 */     top_4.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/*  852 */     top_4.setLayout(new GridLayout(8, false));
/*  853 */     top_4.setText(
/*  854 */       this.editableAttrPhenom + " Attributes: ");
/*      */ 
/*  856 */     comboPhenom.addListener(13, new Listener() {
/*      */       public void handleEvent(Event event) {
/*  858 */         SigmetAttrDlg.this.editableAttrPhenom = comboPhenom.getText().trim();
/*  859 */         SigmetAttrDlg.this.comboPhenomCalled = true;
/*  860 */         top_4.setText(SigmetAttrDlg.this.editableAttrPhenom + " Attributes: ");
/*  861 */         SigmetAttrDlg.this.copyEditableAttrToSigmet(
/*  862 */           (Sigmet)SigmetAttrDlg.this
/*  862 */           .getSigmet());
/*      */ 
/*  864 */         SigmetAttrDlg.this.showDetailsArea();
/*      */       }
/*      */     });
/*  869 */     Label lblMovement = new Label(top_4, 16384);
/*  870 */     lblMovement.setText("Movement: ");
/*      */ 
/*  872 */     Button btnSTNRY = new Button(top_4, 16);
/*  873 */     btnSTNRY.setText("STNRY");
/*  874 */     btnSTNRY.setSelection(true);
/*  875 */     setEditableAttrMovement("STNRY");
/*      */ 
/*  877 */     btnSTNRY.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  879 */         SigmetAttrDlg.this.setEditableAttrMovement("STNRY");
/*      */       }
/*      */     });
/*  883 */     Button btnMVG = new Button(top_4, 16);
/*  884 */     btnMVG.setText("MVG      ");
/*  885 */     btnMVG.setLayoutData(new GridData(16384, 16777216, true, false, 2, 
/*  886 */       1));
/*      */ 
/*  888 */     btnMVG.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  890 */         SigmetAttrDlg.this.setEditableAttrMovement("MVG");
/*      */       }
/*      */     });
/*  893 */     this.attrButtonMap.put("editableAttrMovement", new Button[] { btnSTNRY, 
/*  894 */       btnMVG });
/*      */ 
/*  896 */     Label lblSpeed = new Label(top_4, 16384);
/*  897 */     lblSpeed.setText("Speed: ");
/*  898 */     final Combo comboSpeed = new Combo(top_4, 8);
/*  899 */     this.attrControlMap.put("editableAttrPhenomSpeed", comboSpeed);
/*  900 */     comboSpeed.setItems(SigmetInfo.SPEED_ARRAY);
/*  901 */     comboSpeed.select(0);
/*  902 */     setEditableAttrPhenomSpeed(comboSpeed.getText());
/*      */ 
/*  904 */     comboSpeed.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  906 */         SigmetAttrDlg.this.setEditableAttrPhenomSpeed(comboSpeed
/*  907 */           .getText());
/*      */       }
/*      */     });
/*  911 */     Label lblDirection = new Label(top_4, 16384);
/*  912 */     lblDirection.setText("Direction toward:");
/*  913 */     final Combo comboDirection = new Combo(top_4, 8);
/*  914 */     this.attrControlMap.put("editableAttrPhenomDirection", comboDirection);
/*  915 */     comboDirection.setItems(SigmetInfo.DIRECT_ARRAY);
/*  916 */     comboDirection.select(0);
/*  917 */     setEditableAttrPhenomDirection(comboDirection.getText());
/*      */ 
/*  919 */     comboDirection.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  921 */         SigmetAttrDlg.this
/*  922 */           .setEditableAttrPhenomDirection(comboDirection
/*  923 */           .getText());
/*      */       }
/*      */     });
/*  927 */     Label lblTrend = new Label(top_4, 16384);
/*  928 */     lblTrend.setText("Trend: ");
/*  929 */     final Combo comboTrend = new Combo(top_4, 8);
/*  930 */     this.attrControlMap.put("editableAttrTrend", comboTrend);
/*  931 */     comboTrend.setItems(SigmetInfo.TREND_ARRAY);
/*  932 */     comboTrend.select(0);
/*  933 */     setEditableAttrTrend(comboTrend.getText());
/*  934 */     comboTrend.setLayoutData(new GridData(16384, 16777216, false, 
/*  935 */       false, 7, 1));
/*      */ 
/*  937 */     comboTrend.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  939 */         SigmetAttrDlg.this.setEditableAttrTrend(comboTrend.getText());
/*      */       }
/*      */     });
/*  945 */     if ("TROPICAL_CYCLONE".equals(this.editableAttrPhenom)) {
/*  946 */       Group top_secPhenom = new Group(top5, 16384);
/*  947 */       top_secPhenom.setLayoutData(new GridData(4, 16777216, 
/*  948 */         true, true, 8, 1));
/*  949 */       top_secPhenom.setLayout(new GridLayout(8, false));
/*      */ 
/*  951 */       Label lblSecPhenom = new Label(top_secPhenom, 16384);
/*  952 */       lblSecPhenom.setText("Second Phenom: ");
/*  953 */       final Combo comboSecPhenom = new Combo(top_secPhenom, 8);
/*  954 */       this.attrControlMap.put("editableAttrPhenom2", comboSecPhenom);
/*  955 */       comboSecPhenom.setItems((String[])SigmetInfo.PHEN_MAP.get(
/*  956 */         SigmetInfo.getSigmetTypeString(this.pgenType)));
/*  957 */       setControl(comboSecPhenom, "editableAttrPhenom2");
/*  958 */       comboSecPhenom.addListener(13, new Listener() {
/*      */         public void handleEvent(Event e) {
/*  960 */           SigmetAttrDlg.this.setEditableAttrPhenom2(comboSecPhenom
/*  961 */             .getText());
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/*  969 */     Group top_5 = new Group(top5, 16384);
/*  970 */     top_5.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/*  971 */     top_5.setLayout(new GridLayout(8, false));
/*      */ 
/*  973 */     Label lblLevelInfo = new Label(top_5, 16384);
/*  974 */     lblLevelInfo.setText("Level Info: ");
/*      */ 
/*  976 */     final Combo comboLevel = new Combo(top_5, 8);
/*  977 */     this.attrControlMap.put("editableAttrLevel", comboLevel);
/*  978 */     comboLevel.setItems(new String[] { "-none-", "FCST", "TOPS" });
/*      */ 
/*  980 */     setControl(comboLevel, "editableAttrLevel");
/*  981 */     comboLevel.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  983 */         SigmetAttrDlg.this.setEditableAttrLevel(comboLevel.getText());
/*      */       }
/*      */     });
/*  987 */     final Combo comboLevelInfo1 = new Combo(top_5, 8);
/*  988 */     this.attrControlMap.put("editableAttrLevelInfo1", comboLevelInfo1);
/*  989 */     comboLevelInfo1.setItems(new String[] { "TO", "ABV", "BLW", "BTN" });
/*      */ 
/*  991 */     setControl(comboLevelInfo1, "editableAttrLevelInfo1");
/*  992 */     comboLevelInfo1.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  994 */         SigmetAttrDlg.this.setEditableAttrLevelInfo1(comboLevelInfo1
/*  995 */           .getText());
/*      */       }
/*      */     });
/*  999 */     final Text txtLevelInfo1 = new Text(top_5, 2052);
/* 1000 */     this.attrControlMap.put("editableAttrLevelText1", txtLevelInfo1);
/* 1001 */     txtLevelInfo1.setLayoutData(new GridData(16384, 16777216, true, 
/* 1002 */       false, 2, 1));
/* 1003 */     txtLevelInfo1.addListener(25, new Listener() {
/*      */       public void handleEvent(Event e) {
/* 1005 */         e.doit = SigmetAttrDlg.this.validateNumInput(e);
/* 1006 */         if (!e.doit);
/*      */       }
/*      */     });
/* 1011 */     GridData gdText1 = new GridData();
/* 1012 */     gdText1.widthHint = 66;
/* 1013 */     gdText1.grabExcessHorizontalSpace = true;
/*      */ 
/* 1015 */     txtLevelInfo1.setLayoutData(gdText1);
/* 1016 */     txtLevelInfo1.addListener(24, new Listener() {
/*      */       public void handleEvent(Event e) {
/* 1018 */         SigmetAttrDlg.this.setEditableAttrLevelText1(txtLevelInfo1
/* 1019 */           .getText());
/*      */       }
/*      */     });
/* 1023 */     final Combo comboLevelInfo2 = new Combo(top_5, 8);
/* 1024 */     this.attrControlMap.put("editableAttrLevelInfo2", comboLevelInfo2);
/* 1025 */     comboLevelInfo2.setItems(new String[] { "-none-", "AND" });
/*      */ 
/* 1027 */     setControl(comboLevelInfo2, "editableAttrLevelInfo2");
/*      */ 
/* 1029 */     comboLevelInfo2.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/* 1031 */         SigmetAttrDlg.this.setEditableAttrLevelInfo2(comboLevelInfo2
/* 1032 */           .getText());
/*      */       }
/*      */     });
/* 1036 */     final Text txtLevelInfo2 = new Text(top_5, 2052);
/* 1037 */     this.attrControlMap.put("editableAttrLevelText2", txtLevelInfo2);
/* 1038 */     txtLevelInfo2.setLayoutData(new GridData(16384, 16777216, true, 
/* 1039 */       false, 2, 1));
/*      */ 
/* 1041 */     txtLevelInfo2.addListener(25, new Listener() {
/*      */       public void handleEvent(Event e) {
/* 1043 */         e.doit = SigmetAttrDlg.this.validateNumInput(e);
/* 1044 */         if (!e.doit);
/*      */       }
/*      */     });
/* 1048 */     txtLevelInfo2.setLayoutData(gdText1);
/*      */ 
/* 1050 */     txtLevelInfo2.addListener(24, new Listener() {
/*      */       public void handleEvent(Event e) {
/* 1052 */         SigmetAttrDlg.this.setEditableAttrLevelText2(txtLevelInfo2
/* 1053 */           .getText());
/*      */       }
/*      */     });
/* 1059 */     Group top_6 = new Group(top5, 16384);
/* 1060 */     top_6.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/* 1061 */     top_6.setLayout(new GridLayout(8, false));
/*      */ 
/* 1078 */     Label lblFreeText = new Label(top_6, 16384);
/* 1079 */     lblFreeText.setText("Free Text:   ");
/* 1080 */     lblFreeText.setLayoutData(new GridData(
/* 1081 */       2));
/* 1082 */     final Text txtFreeText = new Text(top_6, 2050);
/* 1083 */     this.attrControlMap.put("editableAttrFreeText", txtFreeText);
/* 1084 */     GridData gData = new GridData(4, 16777216, true, true, 7, 1);
/* 1085 */     gData.heightHint = 48;
/* 1086 */     txtFreeText.setLayoutData(gData);
/* 1087 */     txtFreeText.addListener(24, new Listener() {
/*      */       public void handleEvent(Event e) {
/* 1089 */         SigmetAttrDlg.this.setEditableAttrFreeText(txtFreeText
/* 1090 */           .getText());
/*      */       }
/*      */     });
/* 1094 */     if ((!"TROPICAL_CYCLONE".equals(this.editableAttrPhenom)) && 
/* 1095 */       (!"VOLCANIC_ASH".equals(this.editableAttrPhenom))) {
/* 1096 */       lblFreeText.setEnabled(false);
/* 1097 */       txtFreeText.setEnabled(false);
/*      */     }
/*      */ 
/* 1102 */     Label lblDummy = new Label(top5, 16777216);
/* 1103 */     lblDummy.setLayoutData(new GridData(4, 16777216, true, false, 
/* 1104 */       4, 1));
/*      */ 
/* 1106 */     createButtonsForButtonBar(top5);
/*      */ 
/* 1108 */     if (this.comboPhenomCalled) {
/* 1109 */       this.withExpandedArea = true;
/* 1110 */       this.comboPhenomCalled = false;
/*      */     }
/*      */ 
/* 1113 */     Label lblDummy1 = new Label(top5, 16777216);
/* 1114 */     lblDummy1.setLayoutData(new GridData(4, 16777216, true, false, 
/* 1115 */       1, 1));
/*      */ 
/* 1117 */     if (this.copiedToSigmet) {
/* 1118 */       init();
/* 1119 */       this.copiedToSigmet = false;
/*      */     }
/*      */ 
/* 1122 */     return top5;
/*      */   }
/*      */ 
/*      */   protected final void showDetailsArea()
/*      */   {
/* 1127 */     this.withExpandedArea = true;
/*      */ 
/* 1129 */     Point oldWindowSize = getShell().getSize(); Point newWindowSize = this.cachedWindowSize;
/* 1130 */     if (this.detailsArea == null) {
/* 1131 */       this.detailsArea = createDetailsArea((Composite)getContents());
/*      */     }
/*      */     else {
/* 1134 */       this.detailsArea.dispose();
/* 1135 */       this.detailsArea = createDetailsArea((Composite)getContents());
/*      */     }
/*      */ 
/* 1139 */     Point oldSize = getContents().getSize();
/* 1140 */     Point newSize = getContents().computeSize(-1, -1);
/*      */ 
/* 1142 */     if (newWindowSize == null) {
/* 1143 */       newWindowSize = new Point(oldWindowSize.x, oldWindowSize.y + (
/* 1144 */         newSize.y - oldSize.y));
/*      */     }
/* 1146 */     Point windowLoc = getShell().getLocation();
/* 1147 */     Rectangle screenArea = getContents().getDisplay().getClientArea();
/*      */ 
/* 1149 */     if (newWindowSize.y > screenArea.height - (windowLoc.y - screenArea.y)) {
/* 1150 */       newWindowSize.y = (screenArea.height - (windowLoc.y - screenArea.y));
/*      */     }
/* 1152 */     getShell().setSize(newWindowSize);
/* 1153 */     ((Composite)getContents()).layout(true, true);
/*      */   }
/*      */ 
/*      */   private String getVOR(Coordinate[] coors)
/*      */   {
/* 1161 */     return SnapUtil.getVORText(coors, "-", this.lineType, 6, false);
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/* 1168 */     this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/* 1170 */     GridLayout mainLayout = new GridLayout(8, false);
/* 1171 */     mainLayout.marginHeight = 3;
/* 1172 */     mainLayout.marginWidth = 3;
/* 1173 */     this.top.setLayout(mainLayout);
/*      */ 
/* 1175 */     getShell().setText("International SIGMET Edit");
/*      */ 
/* 1177 */     Button btnArea = new Button(this.top, 16);
/* 1178 */     btnArea.setSelection(true);
/* 1179 */     btnArea.setText("Area");
/*      */ 
/* 1181 */     Button btnLine = new Button(this.top, 16);
/* 1182 */     btnLine.setText("Line");
/*      */ 
/* 1184 */     final Combo comboLine = new Combo(this.top, 8);
/* 1185 */     this.attrControlMap.put("lineType", comboLine);
/* 1186 */     comboLine.setItems(LINE_SIDES);
/* 1187 */     this.attrControlMap.put("sideOfLine", comboLine);
/* 1188 */     comboLine.select(0);
/* 1189 */     comboLine.setEnabled(false);
/*      */ 
/* 1191 */     Button btnIsolated = new Button(this.top, 16);
/* 1192 */     btnIsolated.setText("Isolated  ");
/*      */ 
/* 1194 */     Label lblText = new Label(this.top, 16384);
/* 1195 */     lblText.setText("Width: ");
/* 1196 */     final Text txtWidth = new Text(this.top, 2052);
/* 1197 */     this.attrControlMap.put("widthStr", txtWidth);
/* 1198 */     txtWidth.setText("10.00");
/* 1199 */     txtWidth.setEnabled(false);
/* 1200 */     this.attrButtonMap.put("lineType", new Button[] { btnArea, btnLine, 
/* 1201 */       btnIsolated });
/*      */ 
/* 1203 */     btnArea.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/* 1205 */         comboLine.setEnabled(false);
/* 1206 */         txtWidth.setEnabled(false);
/*      */ 
/* 1208 */         SigmetAttrDlg.this.setLineType("Area");
/*      */       }
/*      */     });
/* 1212 */     btnLine.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/* 1214 */         comboLine.setEnabled(true);
/* 1215 */         txtWidth.setEnabled(true);
/*      */ 
/* 1217 */         SigmetAttrDlg.this.setLineType("Line:::" + 
/* 1218 */           SigmetAttrDlg.this.getSideOfLine());
/*      */       }
/*      */     });
/* 1222 */     btnIsolated.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/* 1224 */         comboLine.setEnabled(false);
/* 1225 */         txtWidth.setEnabled(true);
/*      */ 
/* 1227 */         SigmetAttrDlg.this.setLineType("Isolated");
/*      */       }
/*      */     });
/* 1231 */     comboLine.addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event e)
/*      */       {
/* 1235 */         SigmetAttrDlg.this.setSideOfLine(comboLine.getText());
/* 1236 */         SigmetAttrDlg.this.setLineType("Line:::" + 
/* 1238 */           SigmetAttrDlg.this.getSideOfLine());
/*      */       }
/*      */     });
/* 1243 */     txtWidth.addListener(24, new Listener() {
/*      */       public void handleEvent(Event e) {
/* 1245 */         SigmetAttrDlg.this.setWidthStr(txtWidth.getText());
/*      */       }
/*      */     });
/* 1249 */     Label colorLbl = new Label(this.top, 16384);
/* 1250 */     colorLbl.setText("Color:");
/*      */ 
/* 1252 */     this.cs = new ColorButtonSelector(this.top);
/* 1253 */     Color clr = Color.cyan;
/* 1254 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*      */ 
/* 1256 */     setLineType("Area");
/* 1257 */     setSideOfLine(comboLine.getText());
/* 1258 */     setWidthStr(txtWidth.getText());
/*      */ 
/* 1260 */     if ((!"INTL_SIGMET".equals(this.pgenType)) && (!"CONV_SIGMET".equals(this.pgenType)))
/*      */     {
/* 1265 */       btnLine.setEnabled(false);
/* 1266 */       btnIsolated.setEnabled(false);
/* 1267 */       comboLine.setEnabled(false);
/* 1268 */       txtWidth.setEnabled(false);
/*      */     }
/*      */ 
/* 1273 */     if (("Pgen Select".equals(mouseHandlerName)) || (this.withExpandedArea))
/*      */     {
/* 1275 */       String[] MWO_ITEMS = (String[])SigmetInfo.AREA_MAP.get(
/* 1276 */         SigmetInfo.getSigmetTypeString(this.pgenType));
/* 1277 */       String[] ID_ITEMS = (String[])SigmetInfo.ID_MAP.get(
/* 1278 */         SigmetInfo.getSigmetTypeString(this.pgenType));
/*      */ 
/* 1280 */       Composite topSelect = (Composite)super.createDialogArea(parent);
/*      */ 
/* 1282 */       GridLayout mainLayout2 = new GridLayout(8, false);
/* 1283 */       mainLayout2.marginHeight = 3;
/* 1284 */       mainLayout2.marginWidth = 4;
/* 1285 */       topSelect.setLayout(mainLayout2);
/*      */ 
/* 1287 */       Group top2 = new Group(topSelect, 16384);
/* 1288 */       top2.setLayoutData(new GridData(4, 16777216, true, true, 
/* 1289 */         8, 1));
/* 1290 */       top2.setLayout(new GridLayout(8, false));
/*      */ 
/* 1292 */       Label lblISU = new Label(top2, 16384);
/* 1293 */       lblISU.setText("ISSUE: ");
/* 1294 */       final Combo comboISU = new Combo(top2, 8);
/* 1295 */       this.attrControlMap.put("editableAttrIssueOffice", comboISU);
/* 1296 */       comboISU.setItems(MWO_ITEMS);
/* 1297 */       comboISU.select(0);
/* 1298 */       setEditableAttrIssueOffice(comboISU.getText());
/* 1299 */       comboISU.setLayoutData(new GridData(16384, 16777216, true, 
/* 1300 */         true, 1, 1));
/*      */ 
/* 1302 */       comboISU.addListener(13, new Listener() {
/*      */         public void handleEvent(Event e) {
/* 1304 */           SigmetAttrDlg.this.setEditableAttrIssueOffice(comboISU
/* 1305 */             .getText());
/*      */         }
/*      */       });
/* 1309 */       Label lblMWO = new Label(top2, 16384);
/* 1310 */       lblMWO.setText(" MWO: ");
/* 1311 */       final Combo comboMWO = new Combo(top2, 8);
/* 1312 */       this.attrControlMap.put("editableAttrArea", comboMWO);
/* 1313 */       comboMWO.setItems(MWO_ITEMS);
/* 1314 */       comboMWO.select(0);
/* 1315 */       setEditableAttrArea(comboMWO.getText());
/* 1316 */       comboMWO.setLayoutData(new GridData(16384, 16777216, true, 
/* 1317 */         true, 1, 1));
/*      */ 
/* 1319 */       comboMWO.addListener(13, new Listener() {
/*      */         public void handleEvent(Event e) {
/* 1321 */           SigmetAttrDlg.this.setEditableAttrArea(comboMWO.getText());
/*      */         }
/*      */       });
/* 1325 */       Label lblID = new Label(top2, 16384);
/* 1326 */       lblID.setText("ID: ");
/* 1327 */       final Combo comboID = new Combo(top2, 8);
/* 1328 */       this.attrControlMap.put("editableAttrId", comboID);
/* 1329 */       comboID.setItems(ID_ITEMS);
/* 1330 */       comboID.select(0);
/* 1331 */       setEditableAttrId(comboID.getText());
/* 1332 */       comboID.setLayoutData(new GridData(16384, 16777216, true, 
/* 1333 */         true, 1, 1));
/*      */ 
/* 1335 */       comboID.addListener(13, new Listener() {
/*      */         public void handleEvent(Event e) {
/* 1337 */           SigmetAttrDlg.this.setEditableAttrId(comboID.getText());
/*      */         }
/*      */       });
/* 1341 */       Label lblSequence = new Label(top2, 16384);
/* 1342 */       lblSequence.setText("Sequence: ");
/* 1343 */       final Spinner spiSeq = new Spinner(top2, 2048);
/* 1344 */       this.attrControlMap.put("editableAttrSeqNum", spiSeq);
/* 1345 */       spiSeq.setMinimum(1);
/* 1346 */       spiSeq.setMaximum(300);
/* 1347 */       setEditableAttrSeqNum(spiSeq.getSelection());
/* 1348 */       spiSeq.setLayoutData(new GridData(4, 16777216, true, 
/* 1349 */         false, 1, 1));
/*      */ 
/* 1351 */       spiSeq.addListener(13, new Listener() {
/*      */         public void handleEvent(Event e) {
/* 1353 */           SigmetAttrDlg.this.setEditableAttrSeqNum(
/* 1354 */             spiSeq.getSelection());
/*      */         }
/*      */       });
/* 1358 */       Button btnNew = new Button(top2, 16);
/* 1359 */       btnNew.setSelection(true);
/* 1360 */       btnNew.setText("LATLON");
/* 1361 */       btnNew.setLayoutData(new GridData(16384, 16777216, true, 
/* 1362 */         false, 2, 1));
/*      */ 
/* 1369 */       Button btnVor = new Button(top2, 16);
/* 1370 */       btnVor.setText("VOR");
/* 1371 */       btnVor.setLayoutData(new GridData(16384, 16777216, true, 
/* 1372 */         false, 4, 1));
/*      */ 
/* 1374 */       int style = 2826;
/*      */ 
/* 1376 */       this.txtInfo = new Text(top2, style);
/* 1377 */       this.attrControlMap.put("editableAttrFromLine", this.txtInfo);
/* 1378 */       GridData gData = new GridData(600, 48);
/* 1379 */       gData.horizontalSpan = 8;
/* 1380 */       this.txtInfo.setLayoutData(gData);
/*      */ 
/* 1389 */       this.attrButtonMap.put("editableAttrFromLine", new Button[] { btnNew, 
/* 1390 */         btnVor });
/*      */ 
/* 1392 */       StringBuilder coorsLatLon = new StringBuilder();
/* 1393 */       final AbstractDrawableComponent elSelected = 
/* 1394 */         PgenSession.getInstance().getPgenResource().getSelectedComp();
/* 1395 */       final Coordinate[] coors = elSelected == null ? null : 
/* 1396 */         (Coordinate[])elSelected
/* 1396 */         .getPoints().toArray(new Coordinate[0]);
/*      */ 
/* 1398 */       if ((coors != null) && (
/* 1399 */         (this.editableAttrFromLine == null) || 
/* 1400 */         (this.editableAttrFromLine.equals("")))) {
/* 1401 */         coorsLatLon.append(getLatLonStringPrepend2(coors, 
/* 1402 */           "Area".equals(((Sigmet)elSelected).getType())));
/* 1403 */         resetText(coorsLatLon.toString(), this.txtInfo);
/* 1404 */         coorsLatLon.append(":::");
/*      */ 
/* 1407 */         String latLonFmtText = "New";
/*      */ 
/* 1409 */         setLatLonFormatFlagAndText(latLonFmtText);
/* 1410 */         setEditableAttrFromLine(latLonFmtText);
/*      */       }
/*      */ 
/* 1415 */       btnNew.addListener(13, new Listener() {
/*      */         public void handleEvent(Event e) {
/* 1417 */           StringBuilder sb = new StringBuilder();
/* 1418 */           sb.append(SigmetAttrDlg.getLatLonStringPrepend2(coors, 
/* 1419 */             "Area".equals(((Sigmet)elSelected).getType())));
/* 1420 */           SigmetAttrDlg.this.resetText(sb.toString(), SigmetAttrDlg.this.txtInfo);
/*      */ 
/* 1422 */           sb.append(":::");
/*      */ 
/* 1424 */           String latLonFmtText = "New";
/* 1425 */           SigmetAttrDlg.this
/* 1426 */             .setLatLonFormatFlagAndText(latLonFmtText);
/* 1427 */           SigmetAttrDlg.this.setEditableAttrFromLine(latLonFmtText);
/*      */         }
/*      */       });
/* 1446 */       btnVor.addListener(13, new Listener() {
/*      */         public void handleEvent(Event e) {
/* 1448 */           StringBuilder sb = new StringBuilder();
/* 1449 */           sb.append(SigmetAttrDlg.this.getVOR(coors));
/* 1450 */           SigmetAttrDlg.this.resetText(sb.toString(), SigmetAttrDlg.this.txtInfo);
/*      */ 
/* 1452 */           sb.append(":::");
/*      */ 
/* 1454 */           String latLonFmtText = "VOR";
/* 1455 */           SigmetAttrDlg.this
/* 1456 */             .setLatLonFormatFlagAndText(latLonFmtText);
/* 1457 */           SigmetAttrDlg.this.setEditableAttrFromLine(latLonFmtText);
/*      */         }
/*      */       });
/* 1461 */       if (!this.withExpandedArea)
/*      */       {
/* 1463 */         final Button btnEdit = new Button(top2, 8);
/* 1464 */         btnEdit.setText("Edit Attributes");
/* 1465 */         btnEdit.setLayoutData(new GridData(4, 16777216, false, 
/* 1466 */           false, 8, 1));
/*      */ 
/* 1468 */         btnEdit.addListener(13, new Listener() {
/*      */           public void handleEvent(Event e) {
/* 1470 */             SigmetAttrDlg.this.withExpandedArea = true;
/* 1471 */             btnEdit.dispose();
/* 1472 */             SigmetAttrDlg.this.getButton(20091020).dispose();
/* 1473 */             SigmetAttrDlg.this.getButton(20091021).dispose();
/* 1474 */             SigmetAttrDlg.this
/* 1475 */               .getButton(1)
/* 1476 */               .dispose();
/* 1477 */             SigmetAttrDlg.this.copyEditableAttrToSigmet(
/* 1478 */               (Sigmet)SigmetAttrDlg.this
/* 1478 */               .getSigmet());
/* 1479 */             SigmetAttrDlg.this.showDetailsArea();
/* 1480 */             SigmetAttrDlg.this.withExpandedArea = true;
/* 1481 */             SigmetAttrDlg.this.init();
/*      */ 
/* 1483 */             SigmetAttrDlg.this.withExpandedArea = false;
/*      */           }
/*      */         });
/*      */       }
/*      */     }
/*      */ 
/* 1489 */     init();
/* 1490 */     addSeparator(this.top.getParent());
/*      */ 
/* 1492 */     return this.top;
/*      */   }
/*      */ 
/*      */   private String getTimeStringPlusHourInHMS(int plusHour) {
/* 1496 */     Calendar c = Calendar.getInstance();
/* 1497 */     c.set(11, c.get(11) + plusHour);
/*      */ 
/* 1500 */     c.set(12, c.get(12) / 5 * 5);
/*      */ 
/* 1502 */     DateFormat dateFormat = new SimpleDateFormat("ddHHmm");
/* 1503 */     dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 1504 */     return dateFormat.format(c.getTime());
/*      */   }
/*      */ 
/*      */   private String convertTimeStringPlusHourInHMS(String timeString, int plusHour, boolean dayNeeded)
/*      */   {
/* 1519 */     Calendar c = Calendar.getInstance();
/* 1520 */     System.out.println("____________timeString: " + timeString);
/* 1521 */     c.set(5, 
/* 1522 */       Integer.parseInt(timeString.substring(0, 2)));
/* 1523 */     c.set(11, 
/* 1524 */       Integer.parseInt(timeString.substring(2, 4)) + plusHour);
/* 1525 */     c.set(12, Integer.parseInt(timeString.substring(4, 6)));
/* 1526 */     String day = 
/* 1527 */       "0" + 
/* 1528 */       c.get(5);
/* 1529 */     String hour = 
/* 1530 */       "0" + 
/* 1531 */       c.get(11);
/* 1532 */     String minute = 
/* 1533 */       "0" + c.get(12);
/*      */ 
/* 1535 */     return 
/* 1536 */       hour + 
/* 1537 */       minute;
/*      */   }
/*      */ 
/*      */   private boolean validateTimeStringInHMS(String time) {
/* 1541 */     if ((time == null) || (time.trim().length() != 6))
/* 1542 */       return false;
/* 1543 */     String txt = time.trim();
/* 1544 */     char[] chars = txt.toCharArray();
/* 1545 */     boolean result = true;
/* 1546 */     if ((chars[0] < '0') || (chars[0] > '3')) {
/* 1547 */       result = false;
/*      */     }
/* 1549 */     if ((chars[1] < '0') || (chars[1] > '9') || 
/* 1550 */       ((chars[0] == '3') && (chars[1] > '1')) || (
/* 1551 */       (chars[0] == '0') && (chars[1] < '1'))) {
/* 1552 */       result = false;
/*      */     }
/* 1554 */     if ((chars[2] < '0') || (chars[2] > '2')) {
/* 1555 */       result = false;
/*      */     }
/* 1557 */     if ((chars[3] < '0') || (chars[3] > '9') || (
/* 1558 */       (chars[2] == '2') && (chars[3] > '3'))) {
/* 1559 */       result = false;
/*      */     }
/* 1561 */     if ((chars[4] < '0') || (chars[4] > '5')) {
/* 1562 */       result = false;
/*      */     }
/* 1564 */     if ((chars[5] < '0') || (chars[5] > '9')) {
/* 1565 */       result = false;
/*      */     }
/*      */ 
/* 1568 */     return result;
/*      */   }
/*      */ 
/*      */   private boolean validateHMSInput(Event e, Text txt)
/*      */   {
/* 1573 */     boolean result = true;
/*      */ 
/* 1575 */     String string = e.text.trim();
/* 1576 */     char[] chars = new char[string.length()];
/* 1577 */     string.getChars(0, chars.length, chars, 0);
/*      */ 
/* 1579 */     int i = e.start;
/*      */ 
/* 1581 */     if (i > 5) {
/* 1582 */       result = false;
/*      */     }
/*      */ 
/* 1585 */     if (chars.length > 0)
/*      */     {
/* 1587 */       if ((i == 0) && ((chars[0] < '0') || (chars[0] > '3'))) {
/* 1588 */         result = false;
/*      */       }
/*      */ 
/* 1591 */       if ((i == 1) && (
/* 1592 */         (chars[0] < '0') || 
/* 1593 */         (chars[0] > '9') || 
/* 1594 */         ((txt.getText().charAt(0) == '3') && (chars[0] > '1')) || (
/* 1595 */         (txt
/* 1595 */         .getText().charAt(0) == '0') && (chars[0] < '1')))) {
/* 1596 */         result = false;
/*      */       }
/*      */ 
/* 1599 */       if ((i == 2) && ((chars[0] < '0') || (chars[0] > '2'))) {
/* 1600 */         result = false;
/*      */       }
/*      */ 
/* 1603 */       if ((i == 3) && (
/* 1604 */         (chars[0] < '0') || (chars[0] > '9') || (
/* 1605 */         (txt.getText()
/* 1605 */         .charAt(2) == '2') && (chars[0] > '3')))) {
/* 1606 */         result = false;
/*      */       }
/*      */ 
/* 1609 */       if ((i == 4) && ((chars[0] < '0') || (chars[0] > '5'))) {
/* 1610 */         result = false;
/*      */       }
/*      */ 
/* 1613 */       if ((i == 5) && ((chars[0] < '0') || (chars[0] > '9'))) {
/* 1614 */         result = false;
/*      */       }
/*      */     }
/* 1617 */     return result;
/*      */   }
/*      */ 
/*      */   private boolean validateNumInput(Event e)
/*      */   {
/* 1622 */     boolean result = true;
/* 1623 */     String string = e.text;
/* 1624 */     char[] chars = new char[string.length()];
/* 1625 */     string.getChars(0, chars.length, chars, 0);
/* 1626 */     for (int i = 0; i < chars.length; i++) {
/* 1627 */       if (('0' > chars[i]) || (chars[i] > '9')) {
/* 1628 */         result = false;
/*      */       }
/*      */     }
/* 1631 */     return result;
/*      */   }
/*      */ 
/*      */   public void copyEditableAttrToSigmet(Sigmet ba) {
/* 1635 */     Field[] ff = getClass().getDeclaredFields();
/* 1636 */     for (Field f : ff) {
/*      */       try {
/* 1638 */         if (f.getName().contains("editableAttr"))
/* 1639 */           BeanUtils.copyProperty(ba, f.getName(), f.get(this));
/*      */       } catch (Exception e) {
/* 1641 */         System.out.println(e.getMessage());
/*      */       }
/*      */     }
/* 1644 */     ba.setType(getLineType());
/* 1645 */     ba.setWidth(getWidth());
/* 1646 */     ba.setEditableAttrFromLine(this.latLonFormatFlagAndText);
/*      */ 
/* 1648 */     ba.setColors(getColors());
/*      */ 
/* 1651 */     this.copiedToSigmet = true;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrIssueOffice() {
/* 1655 */     return (this.editableAttrIssueOffice == null) || 
/* 1656 */       (this.editableAttrIssueOffice
/* 1656 */       .length() == 0) ? ((String[])SigmetInfo.AREA_MAP.get(SigmetInfo.SIGMET_TYPES[0]))[0] : 
/* 1657 */       this.editableAttrIssueOffice;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrIssueOffice(String editableAttrIssueOffice) {
/* 1661 */     this.editableAttrIssueOffice = editableAttrIssueOffice;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrArea() {
/* 1665 */     return (this.editableAttrArea == null) || (this.editableAttrArea.length() == 0) ? 
/* 1666 */       ((String[])SigmetInfo.AREA_MAP
/* 1666 */       .get(SigmetInfo.SIGMET_TYPES[0]))[0] : this.editableAttrArea;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrArea(String editableAttrArea) {
/* 1670 */     this.editableAttrArea = editableAttrArea;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrStatus() {
/* 1674 */     return this.editableAttrStatus;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrStatus(String editableAttrStatus) {
/* 1678 */     this.editableAttrStatus = editableAttrStatus;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrId() {
/* 1682 */     return (this.editableAttrId == null) || (this.editableAttrId.length() == 0) ? 
/* 1683 */       ((String[])SigmetInfo.ID_MAP
/* 1683 */       .get(SigmetInfo.SIGMET_TYPES[0]))[0] : this.editableAttrId;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrId(String editableAttrId) {
/* 1687 */     this.editableAttrId = editableAttrId;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrSeqNum() {
/* 1691 */     return (this.editableAttrSeqNum == null) || (this.editableAttrSeqNum.length() == 0) ? "1" : 
/* 1692 */       this.editableAttrSeqNum;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrSeqNum(String editableAttrSeqNum) {
/* 1696 */     this.editableAttrSeqNum = editableAttrSeqNum;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrStartTime() {
/* 1700 */     return this.editableAttrStartTime;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrStartTime(String editableAttrStartTime) {
/* 1704 */     this.editableAttrStartTime = editableAttrStartTime;
/* 1705 */     ((Sigmet)getSigmet())
/* 1706 */       .setEditableAttrStartTime(editableAttrStartTime);
/*      */   }
/*      */ 
/*      */   public String getEditableAttrEndTime() {
/* 1710 */     return this.editableAttrEndTime;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrEndTime(String editableAttrEndTime) {
/* 1714 */     this.editableAttrEndTime = editableAttrEndTime;
/* 1715 */     ((Sigmet)getSigmet()).setEditableAttrEndTime(editableAttrEndTime);
/*      */   }
/*      */ 
/*      */   public String getEditableAttrRemarks() {
/* 1719 */     return this.editableAttrRemarks;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrRemarks(String editableAttrRemarks) {
/* 1723 */     this.editableAttrRemarks = editableAttrRemarks;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenom() {
/* 1727 */     return this.editableAttrPhenom;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenom(String editableAttrPhenom) {
/* 1731 */     this.editableAttrPhenom = editableAttrPhenom;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenom2() {
/* 1735 */     return this.editableAttrPhenom2;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenom2(String editableAttrPhenom2) {
/* 1739 */     this.editableAttrPhenom2 = editableAttrPhenom2;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomName() {
/* 1743 */     return this.editableAttrPhenomName;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomName(String editableAttrPhenomName) {
/* 1747 */     this.editableAttrPhenomName = editableAttrPhenomName;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomLat() {
/* 1751 */     return this.editableAttrPhenomLat;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomLat(String editableAttrPhenomLat) {
/* 1755 */     this.editableAttrPhenomLat = editableAttrPhenomLat;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomLon() {
/* 1759 */     return this.editableAttrPhenomLon;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomLon(String editableAttrPhenomLon) {
/* 1763 */     this.editableAttrPhenomLon = editableAttrPhenomLon;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomPressure() {
/* 1767 */     return this.editableAttrPhenomPressure;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomPressure(String editableAttrPhenomPressure) {
/* 1771 */     this.editableAttrPhenomPressure = editableAttrPhenomPressure;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomMaxWind() {
/* 1775 */     return this.editableAttrPhenomMaxWind;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomMaxWind(String editableAttrPhenomMaxWind) {
/* 1779 */     this.editableAttrPhenomMaxWind = editableAttrPhenomMaxWind;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrFreeText() {
/* 1783 */     return this.editableAttrFreeText;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrFreeText(String editableAttrFreeText) {
/* 1787 */     this.editableAttrFreeText = editableAttrFreeText;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrTrend() {
/* 1791 */     return this.editableAttrTrend;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrTrend(String editableAttrTrend) {
/* 1795 */     this.editableAttrTrend = editableAttrTrend;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrMovement() {
/* 1799 */     return this.editableAttrMovement;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrMovement(String editableAttrMovement) {
/* 1803 */     this.editableAttrMovement = editableAttrMovement;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomSpeed() {
/* 1807 */     return this.editableAttrPhenomSpeed;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomSpeed(String editableAttrPhenomSpeed) {
/* 1811 */     this.editableAttrPhenomSpeed = editableAttrPhenomSpeed;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrPhenomDirection() {
/* 1815 */     return this.editableAttrPhenomDirection;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrPhenomDirection(String editableAttrPhenomDirection)
/*      */   {
/* 1820 */     this.editableAttrPhenomDirection = editableAttrPhenomDirection;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrLevel() {
/* 1824 */     return this.editableAttrLevel;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrLevel(String editableAttrLevel) {
/* 1828 */     this.editableAttrLevel = editableAttrLevel;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrLevelInfo1() {
/* 1832 */     return this.editableAttrLevelInfo1;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrLevelInfo1(String editableAttrLevelInfo1) {
/* 1836 */     this.editableAttrLevelInfo1 = editableAttrLevelInfo1;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrLevelInfo2() {
/* 1840 */     return this.editableAttrLevelInfo2;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrLevelInfo2(String editableAttrLevelInfo2) {
/* 1844 */     this.editableAttrLevelInfo2 = editableAttrLevelInfo2;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrLevelText1() {
/* 1848 */     return this.editableAttrLevelText1;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrLevelText1(String editableAttrLevelText1) {
/* 1852 */     this.editableAttrLevelText1 = editableAttrLevelText1;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrLevelText2() {
/* 1856 */     return this.editableAttrLevelText2;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrLevelText2(String editableAttrLevelText2) {
/* 1860 */     this.editableAttrLevelText2 = editableAttrLevelText2;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrFromLine() {
/* 1864 */     return this.editableAttrFromLine;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrFromLine(String editableAttrFromLine) {
/* 1868 */     this.editableAttrFromLine = editableAttrFromLine;
/* 1869 */     ((Sigmet)getSigmet())
/* 1870 */       .setEditableAttrFromLine(editableAttrFromLine);
/*      */   }
/*      */ 
/*      */   public String getLatLonFormatFlagAndText() {
/* 1874 */     return this.latLonFormatFlagAndText;
/*      */   }
/*      */ 
/*      */   public void setLatLonFormatFlagAndText(String latLonFormatFlagAndText) {
/* 1878 */     this.latLonFormatFlagAndText = latLonFormatFlagAndText;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrFir() {
/* 1882 */     return this.editableAttrFir;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrFir(String editableAttrFir) {
/* 1886 */     this.editableAttrFir = editableAttrFir;
/*      */   }
/*      */ 
/*      */   private void setControl(Control cont, String prop)
/*      */   {
/*      */     try
/*      */     {
/* 1894 */       PropertyDescriptor pd = new PropertyDescriptor(prop, getClass());
/* 1895 */       if (pd != null) {
/* 1896 */         Method pdReadMethod = pd.getReadMethod();
/* 1897 */         Method pdWriteMethod = pd.getWriteMethod();
/*      */ 
/* 1899 */         if (pdReadMethod != null) {
/* 1900 */           String propValue = (String)pdReadMethod.invoke(this, null);
/*      */ 
/* 1902 */           if (propValue == null) {
/* 1903 */             if ((cont instanceof Combo)) {
/* 1904 */               Combo contCombo = (Combo)cont;
/* 1905 */               contCombo.select(0);
/* 1906 */               pdWriteMethod.invoke(this, new Object[] { contCombo.getText() });
/*      */             }
/*      */ 
/* 1909 */             if ((cont instanceof Spinner)) {
/* 1910 */               Spinner contSpinner = (Spinner)cont;
/* 1911 */               contSpinner.setSelection(0);
/* 1912 */               pdWriteMethod.invoke(this, new Object[] { contSpinner.getText() });
/*      */             }
/*      */           } else {
/* 1915 */             if ((cont instanceof Combo)) {
/* 1916 */               Combo c = (Combo)cont;
/* 1917 */               c.setText(propValue);
/*      */ 
/* 1919 */               if (c.getText().contains("CYCLONE"))
/* 1920 */                 this.tropCycFlag = true;
/*      */             }
/* 1922 */             if ((cont instanceof Text)) {
/* 1923 */               ((Text)cont).setText(propValue);
/*      */             }
/*      */ 
/* 1927 */             if ((cont instanceof Spinner))
/* 1928 */               ((Spinner)cont).setSelection(
/* 1929 */                 Integer.parseInt(propValue));
/*      */           }
/*      */         }
/*      */       }
/*      */     } catch (Exception e) {
/* 1934 */       System.out.println("--- inside setControl(): " + e.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getLineTypeForSOL(String typeString)
/*      */   {
/* 1940 */     String[] lineTypes = { "EITHER SIDE OF", "NORTH OF", 
/* 1941 */       "SOUTH OF", "EAST OF", "WEST OF" };
/*      */ 
/* 1943 */     String type = typeString.split(":::")[1];
/* 1944 */     int index = 0;
/* 1945 */     for (int i = 0; i < LINE_SIDES.length; i++) {
/* 1946 */       if (LINE_SIDES[i].equals(type))
/* 1947 */         index = i;
/*      */     }
/* 1949 */     return lineTypes[index];
/*      */   }
/*      */ 
/*      */   public DrawableElement getSigmet()
/*      */   {
/* 2621 */     return this.sigmet;
/*      */   }
/*      */ 
/*      */   public void setSigmet(DrawableElement sigmet)
/*      */   {
/* 2626 */     this.sigmet = ((Sigmet)sigmet);
/* 2627 */     Button[] buttons = (Button[])this.attrButtonMap.get("editableAttrFromLine");
/* 2628 */     Coordinate[] coors = ((Sigmet)sigmet).getLinePoints();
/* 2629 */     String s = "";
/*      */ 
/* 2631 */     for (int i = 0; (buttons != null) && (i < buttons.length); i++) {
/* 2632 */       Button btn = buttons[i];
/* 2633 */       if ((btn != null) && (!btn.isDisposed()) && (btn.getSelection()) && 
/* 2634 */         (btn.getText() != null) && (btn.getText().length() > 0)) {
/* 2635 */         if (btn.getText().contains("VOR")) {
/* 2636 */           s = getVOR(coors);
/*      */         }
/*      */         else {
/* 2639 */           s = getLatLonStringPrepend2(coors, 
/* 2640 */             "Area".equals(((Sigmet)sigmet).getType()));
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2646 */     if ((this.txtInfo != null) && (!this.txtInfo.isDisposed()) && (s != null))
/* 2647 */       resetText(s, this.txtInfo);
/*      */   }
/*      */ 
/*      */   private void init()
/*      */   {
/* 2653 */     Sigmet sigmet = (Sigmet)getSigmet();
/* 2654 */     if (sigmet != null) {
/* 2655 */       copyEditableAttrToSigmetAttrDlg(sigmet);
/*      */     }
/* 2657 */     Field[] fields = getClass().getDeclaredFields();
/* 2658 */     for (Field f : fields) {
/* 2659 */       String attr = f.getName();
/* 2660 */       String typeValue = "";
/*      */       try {
/* 2662 */         typeValue = (String)f.get(this);
/*      */       } catch (Exception localException) {
/*      */       }
/* 2665 */       Control cont = (Control)this.attrControlMap.get(attr);
/*      */       String lastPart;
/*      */       Button[] arrayOfButton1;
/* 2667 */       if (((cont instanceof Combo)) || ((cont instanceof Spinner)) || 
/* 2668 */         ((cont instanceof Text))) {
/* 2669 */         if (!cont.isDisposed()) {
/* 2670 */           if (attr.equals("editableAttrFromLine"))
/* 2671 */             resetText(typeValue, (Text)cont);
/*      */           else {
/* 2673 */             setControl(cont, attr);
/*      */           }
/*      */         }
/*      */ 
/* 2677 */         if (("editableAttrFromLine".equals(attr)) && (!cont.isDisposed()) && 
/* 2678 */           (typeValue != null)) {
/* 2679 */           Button[] butts = (Button[])this.attrButtonMap.get(attr);
/*      */ 
/* 2684 */           String[] words = typeValue.split(":::");
/* 2685 */           lastPart = words[(words.length - 1)];
/*      */ 
/* 2687 */           if (butts != null) {
/* 2688 */             if ("New".equals(lastPart))
/*      */             {
/* 2690 */               butts[0].setSelection(true);
/* 2691 */               butts[1].setSelection(false);
/*      */             }
/* 2693 */             else if ("Old".equals(lastPart))
/*      */             {
/* 2696 */               butts[0].setSelection(false);
/* 2697 */               butts[1].setSelection(true);
/*      */             }
/*      */             else {
/* 2700 */               butts[0].setSelection(false);
/* 2701 */               butts[1].setSelection(true);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2707 */         if ("lineType".equals(attr)) {
/* 2708 */           Button[] butts = (Button[])this.attrButtonMap.get(attr);
/* 2709 */           if (butts != null) {
/* 2710 */             String str1 = (arrayOfButton1 = butts).length; for (lastPart = 0; lastPart < str1; lastPart++) { Button butt = arrayOfButton1[lastPart];
/*      */               Button[] butts;
/*      */               String str2;
/* 2711 */               if (butt != null)
/*      */               {
/* 2713 */                 if (typeValue
/* 2713 */                   .contains(butt.getText().trim())) {
/* 2714 */                   butt.setSelection(true);
/* 2715 */                   butt.notifyListeners(13, new Event());
/* 2716 */                   for (Button butt2 : butts)
/* 2717 */                     if (butt2 != butt)
/* 2718 */                       butt2.setSelection(false);
/* 2719 */                   break;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 2726 */       if (("editableAttrMovement".equals(attr)) || 
/* 2727 */         ("editableAttrStatus".equals(attr)))
/*      */       {
/* 2729 */         butts = null;
/*      */ 
/* 2731 */         if (("editableAttrMovement".equals(attr)) && (this.withExpandedArea)) {
/* 2732 */           butts = (Button[])this.attrButtonMap.get(attr);
/* 2733 */           if (butts != null) {
/* 2734 */             str2 = (arrayOfButton1 = butts).length; for (lastPart = 0; lastPart < str2; lastPart++) { Button butt = arrayOfButton1[lastPart];
/*      */               char status;
/* 2735 */               if ((butt != null) && 
/* 2736 */                 (!butt.isDisposed()) && 
/* 2737 */                 (typeValue != null))
/*      */               {
/* 2739 */                 if (typeValue
/* 2739 */                   .contains(butt.getText().trim())) {
/* 2740 */                   butt.setSelection(true);
/* 2741 */                   for (Button butt2 : butts)
/* 2742 */                     if (butt2 != butt)
/* 2743 */                       butt2.setSelection(false);
/* 2744 */                   break;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/* 2751 */         else if (("editableAttrStatus".equals(attr)) && (this.withExpandedArea)) {
/* 2752 */           butts = (Button[])this.attrButtonMap.get(attr);
/* 2753 */           if ((butts != null) && (typeValue != null)) {
/* 2754 */             status = typeValue.charAt(0);
/* 2755 */             switch (status) {
/*      */             case '0':
/* 2757 */               butts[0].setSelection(true);
/* 2758 */               butts[1].setSelection(false);
/* 2759 */               butts[2].setSelection(false);
/* 2760 */               break;
/*      */             case '1':
/* 2762 */               butts[1].setSelection(true);
/* 2763 */               butts[0].setSelection(false);
/* 2764 */               butts[2].setSelection(false);
/* 2765 */               break;
/*      */             case '2':
/* 2767 */               butts[2].setSelection(true);
/* 2768 */               butts[1].setSelection(false);
/* 2769 */               butts[0].setSelection(false);
/* 2770 */               break;
/*      */             default:
/* 2772 */               butts[0].setSelection(true);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2781 */     this.tropCycFlag = false;
/* 2782 */     this.withExpandedArea = false;
/*      */   }
/*      */ 
/*      */   private boolean validateLatLon(String coor, boolean isLat) {
/* 2786 */     String regexLat = "(-?[0-8]?[0-9](\\.\\d*)?)|-?90(\\.[0]*)?";
/* 2787 */     String regexLon = "(-?([1]?[0-7][1-9]|[1-9]?[0-9])?(\\.\\d*)?)|-?180(\\.[0]*)?";
/*      */     Matcher m;
/*      */     Matcher m;
/* 2790 */     if (isLat)
/* 2791 */       m = Pattern.compile(regexLat).matcher(coor);
/*      */     else {
/* 2793 */       m = Pattern.compile(regexLon).matcher(coor);
/*      */     }
/*      */ 
/* 2797 */     return m.matches();
/*      */   }
/*      */ 
/*      */   private boolean validateNumInput(String num) {
/*      */     try {
/* 2802 */       Double.parseDouble(num);
/*      */     } catch (Exception e) {
/* 2804 */       return false;
/*      */     }
/* 2806 */     return true;
/*      */   }
/*      */ 
/*      */   public static String latlonWithSpace(String s) {
/* 2810 */     if ((s == null) || (s.length() < 1)) {
/* 2811 */       return "";
/*      */     }
/* 2813 */     String rr = "";
/* 2814 */     if (s.length() > 0) {
/* 2815 */       char r = s.charAt(0);
/*      */ 
/* 2817 */       if ((r == 'N') || (r == 'S')) {
/* 2818 */         rr = latlonAddSpace(s, true);
/*      */       } else {
/* 2820 */         char e = s.charAt(s.length() - 1);
/* 2821 */         if ((e == 'W') || (e == 'E')) {
/* 2822 */           rr = latlonAddSpace(s, false);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2827 */     return rr;
/*      */   }
/*      */ 
/*      */   public static String latlonAddSpace(String s, boolean isHeadNS) {
/* 2831 */     if (isHeadNS) {
/* 2832 */       if (s.contains("W"))
/* 2833 */         return s.replace("W", " W");
/* 2834 */       if (s.contains("E"))
/* 2835 */         return s.replace("E", " E");
/*      */     } else {
/* 2837 */       if (s.contains("N"))
/* 2838 */         return s.replace("N", "N ");
/* 2839 */       if (s.contains("S")) {
/* 2840 */         return s.replace("S", "S ");
/*      */       }
/*      */     }
/* 2843 */     return "";
/*      */   }
/*      */ 
/*      */   public static final String getLatLonStringPrepend2(Coordinate[] coors, boolean isLineTypeArea)
/*      */   {
/* 2849 */     String paddedDash = " - ";
/*      */ 
/* 2851 */     String FOUR_ZERO = "0000"; String FIVE_ZERO = "00000";
/*      */ 
/* 2853 */     StringBuilder result = new StringBuilder();
/* 2854 */     for (int i = 0; i < coors.length; i++) {
/* 2855 */       Coordinate coor = coors[i];
/*      */ 
/* 2857 */       result.append(coor.y >= 0.0D ? "N" : "S");
/* 2858 */       long y = (int)Math.abs(coor.y) * 100 + Math.round(Math.abs(coor.y - (int)coor.y) * 60.0D);
/* 2859 */       result.append(new DecimalFormat(FOUR_ZERO).format(y));
/*      */ 
/* 2861 */       result.append(coor.x >= 0.0D ? " E" : " W");
/* 2862 */       long x = (int)Math.abs(coor.x) * 100 + Math.round(Math.abs(coor.x - (int)coor.x) * 60.0D);
/*      */ 
/* 2864 */       result.append(new DecimalFormat(FIVE_ZERO).format(x));
/*      */ 
/* 2866 */       if (i < coors.length - 1) {
/* 2867 */         result.append(paddedDash);
/*      */       }
/*      */     }
/* 2870 */     if (isLineTypeArea) {
/* 2871 */       result.append(paddedDash).append(
/* 2872 */         result.toString().split(paddedDash)[0]);
/*      */     }
/* 2874 */     return result.toString();
/*      */   }
/*      */ 
/*      */   public static String[] getPhenomenons(String type)
/*      */   {
/* 2879 */     Set set = new LinkedHashSet();
/*      */ 
/* 2881 */     if ((type == null) || (type.isEmpty())) {
/* 2882 */       return (String[])set.toArray(new String[0]);
/*      */     }
/* 2884 */     Logger log = 
/* 2885 */       Logger.getAnonymousLogger();
/* 2886 */     log.setLevel(Level.SEVERE);
/*      */ 
/* 2888 */     NodeList nlist = null;
/*      */ 
/* 2890 */     File file = PgenStaticDataProvider.getProvider().getStaticFile(
/* 2891 */       PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + 
/* 2892 */       "phenomenons.xml");
/*      */     try
/*      */     {
/* 2895 */       Document doc = 
/* 2896 */         DocumentBuilderFactory.newInstance().newDocumentBuilder()
/* 2897 */         .parse(file.getAbsoluteFile());
/* 2898 */       nlist = doc.getElementsByTagName(type);
/*      */ 
/* 2900 */       if ((nlist != null) && (nlist.getLength() > 0)) {
/* 2901 */         nlist = nlist.item(0).getChildNodes();
/*      */ 
/* 2903 */         int i = 0;
/*      */         do { String phenom = nlist.item(i).getTextContent();
/*      */ 
/* 2906 */           if ((phenom != null) && (phenom.trim().length() > 0))
/* 2907 */             set.add(phenom.trim());
/* 2903 */           i++; if (nlist == null) break;  } while (i < nlist.getLength());
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 2913 */       log.log(Level.SEVERE, 
/* 2914 */         "___SigmetAttrDlg: getPhenomenons(): " + e.getMessage());
/*      */     }
/*      */ 
/* 2917 */     if (set.size() == 0) {
/* 2918 */       set.add("--");
/*      */     }
/* 2920 */     return (String[])set.toArray(new String[0]);
/*      */   }
/*      */ 
/*      */   public Coordinate[] getLinePoints()
/*      */   {
/* 2926 */     return null;
/*      */   }
/*      */ 
/*      */   public String getPatternName()
/*      */   {
/* 2932 */     return null;
/*      */   }
/*      */ 
/*      */   public Boolean isClosedLine()
/*      */   {
/* 2938 */     return null;
/*      */   }
/*      */ 
/*      */   private String getPhenomLatLon(String input, boolean isLat)
/*      */   {
/* 2943 */     if ((input.startsWith("S")) || (input.startsWith("s")) || 
/* 2944 */       (input.startsWith("W")) || (input.startsWith("w")) || 
/* 2945 */       (input.endsWith("S")) || (input.endsWith("s")) || 
/* 2946 */       (input.endsWith("W")) || (input.endsWith("w")))
/* 2947 */       input = "-" + input;
/* 2948 */     input = input.replaceAll("[^-0-9.]", "");
/*      */ 
/* 2950 */     StringBuilder result = new StringBuilder();
/* 2951 */     if ((!"".equals(input)) && (!"-".equals(input)) && 
/* 2952 */       (validateLatLon(input, isLat))) {
/* 2953 */       Double value = Double.valueOf(Double.parseDouble(input));
/*      */ 
/* 2955 */       if (isLat) {
/* 2956 */         result.append(value.doubleValue() >= 0.0D ? "N" : "S");
/* 2957 */         double y = Math.abs(value.doubleValue());
/* 2958 */         result.append(y);
/*      */       } else {
/* 2960 */         result.append(value.doubleValue() >= 0.0D ? " E" : " W");
/* 2961 */         double x = Math.abs(value.doubleValue());
/* 2962 */         result.append(x);
/*      */       }
/*      */ 
/* 2965 */       return result.toString().trim();
/*      */     }
/* 2967 */     return "";
/*      */   }
/*      */ 
/*      */   public Sigmet convertType(Sigmet newEl)
/*      */   {
/* 2972 */     String origLineType = getOrigLineType();
/* 2973 */     String newLineType = getLineType();
/*      */ 
/* 2975 */     if (!newLineType.equals(origLineType))
/*      */     {
/* 2977 */       float p45 = 45.0F; float p135 = 135.0F; float p225 = 225.0F; float p315 = 315.0F;
/*      */ 
/* 2979 */       ArrayList ptsCopy = newEl.getPoints();
/* 2980 */       ArrayList newPtsCopy = new ArrayList();
/*      */ 
/* 2982 */       if ("Isolated".equals(origLineType))
/*      */       {
/* 2987 */         Coordinate centerCoor = (Coordinate)ptsCopy.get(0);
/*      */ 
/* 2989 */         if (newLineType.startsWith("Line"))
/*      */         {
/* 2993 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 2994 */             Float.parseFloat(this.widthStr), p315));
/* 2995 */           newPtsCopy.add(centerCoor);
/* 2996 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 2997 */             Float.parseFloat(this.widthStr), p135));
/*      */         }
/*      */         else
/*      */         {
/* 3002 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 3003 */             Float.parseFloat(this.widthStr), p45));
/* 3004 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 3005 */             Float.parseFloat(this.widthStr), p135));
/* 3006 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 3007 */             Float.parseFloat(this.widthStr), p225));
/* 3008 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 3009 */             Float.parseFloat(this.widthStr), p315));
/*      */         }
/*      */ 
/* 3012 */         newEl.setPoints(newPtsCopy);
/* 3013 */       } else if ("Isolated".equals(newLineType))
/*      */       {
/* 3017 */         newPtsCopy.add((Coordinate)ptsCopy.get(0));
/* 3018 */         newEl.setPoints(newPtsCopy);
/*      */       }
/*      */     }
/*      */ 
/* 3022 */     setSigmetFromLine(newEl);
/* 3023 */     return newEl;
/*      */   }
/*      */ 
/*      */   public void setSigmetFromLine(DrawableElement sigmet)
/*      */   {
/* 3028 */     this.sigmet = ((Sigmet)sigmet);
/* 3029 */     Button[] buttons = (Button[])this.attrButtonMap.get("editableAttrFromLine");
/* 3030 */     Coordinate[] coors = ((Sigmet)sigmet).getLinePoints();
/* 3031 */     StringBuilder s = new StringBuilder();
/*      */ 
/* 3033 */     for (int i = 0; (buttons != null) && (i < buttons.length); i++) {
/* 3034 */       Button btn = buttons[i];
/* 3035 */       if ((btn != null) && (!btn.isDisposed()) && (btn.getSelection()) && 
/* 3036 */         (btn.getText() != null) && (btn.getText().length() > 0)) {
/* 3037 */         if (btn.getText().contains("VOR")) {
/* 3038 */           s.append(getVOR(coors));
/* 3039 */           s.append(":::");
/* 3040 */           String latLonFmtText = "VOR";
/*      */ 
/* 3042 */           setLatLonFormatFlagAndText(latLonFmtText);
/* 3043 */           setEditableAttrFromLine(latLonFmtText);
/*      */         } else {
/* 3045 */           s.append(getLatLonStringPrepend2(coors, 
/* 3046 */             "Area".equals(((Sigmet)sigmet).getType())));
/* 3047 */           s.append(":::");
/* 3048 */           String latLonFmtText = "New";
/*      */ 
/* 3050 */           setLatLonFormatFlagAndText(latLonFmtText);
/* 3051 */           setEditableAttrFromLine(latLonFmtText);
/*      */         }
/*      */       }
/*      */     }
/* 3055 */     if ((this.txtInfo != null) && (!this.txtInfo.isDisposed()) && (s != null))
/* 3056 */       resetText(s.toString(), this.txtInfo);
/*      */   }
/*      */ 
/*      */   public void copyEditableAttrToSigmetAttrDlg(Sigmet sig)
/*      */   {
/* 3061 */     setLineType(sig.getType());
/* 3062 */     setWidthStr(sig.getWidth());
/*      */ 
/* 3064 */     setEditableAttrFreeText(sig.getEditableAttrFreeText());
/* 3065 */     setEditableAttrStatus(sig.getEditableAttrStatus());
/* 3066 */     setEditableAttrStartTime(sig.getEditableAttrStartTime());
/* 3067 */     setEditableAttrEndTime(sig.getEditableAttrEndTime());
/* 3068 */     setEditableAttrPhenom(sig.getEditableAttrPhenom());
/* 3069 */     setEditableAttrPhenom2(sig.getEditableAttrPhenom2());
/* 3070 */     setEditableAttrPhenomLat(sig.getEditableAttrPhenomLat());
/* 3071 */     setEditableAttrPhenomLon(sig.getEditableAttrPhenomLon());
/* 3072 */     setEditableAttrPhenomSpeed(sig.getEditableAttrPhenomSpeed());
/* 3073 */     setEditableAttrPhenomDirection(sig
/* 3074 */       .getEditableAttrPhenomDirection());
/*      */ 
/* 3076 */     setEditableAttrRemarks(sig.getEditableAttrRemarks());
/* 3077 */     setEditableAttrPhenomName(sig.getEditableAttrPhenomName());
/* 3078 */     setEditableAttrPhenomPressure(sig.getEditableAttrPhenomPressure());
/* 3079 */     setEditableAttrPhenomMaxWind(sig.getEditableAttrPhenomMaxWind());
/* 3080 */     setEditableAttrTrend(sig.getEditableAttrTrend());
/* 3081 */     setEditableAttrMovement(sig.getEditableAttrMovement());
/* 3082 */     setEditableAttrLevel(sig.getEditableAttrLevel());
/* 3083 */     setEditableAttrLevelInfo1(sig.getEditableAttrLevelInfo1());
/* 3084 */     setEditableAttrLevelInfo2(sig.getEditableAttrLevelInfo2());
/* 3085 */     setEditableAttrLevelText1(sig.getEditableAttrLevelText1());
/* 3086 */     setEditableAttrLevelText2(sig.getEditableAttrLevelText2());
/* 3087 */     setEditableAttrFir(sig.getEditableAttrFir());
/*      */ 
/* 3089 */     String lineType = getType();
/* 3090 */     if ((lineType != null) && (lineType.contains(":::"))) {
/* 3091 */       setSideOfLine(lineType.split(":::")[1]);
/*      */     }
/*      */ 
/* 3094 */     setWidthStr(sig.getWidth());
/* 3095 */     setLatLonFormatFlagAndText(sig.getEditableAttrFromLine());
/*      */ 
/* 3099 */     setEditableAttrArea(sig.getEditableAttrArea());
/* 3100 */     setEditableAttrIssueOffice(sig.getEditableAttrIssueOffice());
/* 3101 */     setEditableAttrFromLine(sig.getEditableAttrFromLine());
/* 3102 */     setEditableAttrId(sig.getEditableAttrId());
/* 3103 */     setEditableAttrSeqNum(sig.getEditableAttrSeqNum());
/*      */   }
/*      */ 
/*      */   private class SigmetAttrDlgSaveMsgDlg extends AttrDlg
/*      */   {
/* 1953 */     String startTime = ""; String endTime = "";
/*      */     Text txtInfo;
/*      */     Text txtSave;
/* 1959 */     boolean firCalledForSecondLine = false;
/*      */ 
/*      */     SigmetAttrDlgSaveMsgDlg(Shell parShell) throws VizException {
/* 1962 */       super();
/*      */     }
/*      */ 
/*      */     public Control createDialogArea(Composite parent)
/*      */     {
/* 1967 */       Composite top = (Composite)super.createDialogArea(parent);
/*      */ 
/* 1969 */       GridLayout mainLayout = new GridLayout(3, false);
/* 1970 */       mainLayout.marginHeight = 3;
/* 1971 */       mainLayout.marginWidth = 3;
/* 1972 */       top.setLayout(mainLayout);
/*      */ 
/* 1974 */       getShell().setText("SIGMET Save");
/*      */ 
/* 1976 */       this.txtInfo = new Text(top, 2122);
/*      */ 
/* 1978 */       GridData gData = new GridData(512, 300);
/* 1979 */       gData.horizontalSpan = 3;
/* 1980 */       this.txtInfo.setLayoutData(gData);
/* 1981 */       this.txtInfo.setText(getFileContent());
/*      */ 
/* 1984 */       this.txtSave = new Text(top, 2056);
/* 1985 */       this.txtSave.setLayoutData(new GridData(4, 16777216, true, 
/* 1986 */         false, 3, 1));
/* 1987 */       this.txtSave.setText(getFileName());
/*      */ 
/* 1989 */       return top;
/*      */     }
/*      */ 
/*      */     public void createButtonsForButtonBar(Composite parent)
/*      */     {
/* 1995 */       ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/* 1996 */       ((GridLayout)parent.getLayout()).marginHeight = 3;
/*      */ 
/* 1998 */       createButton(parent, 0, "Save", true);
/* 1999 */       createButton(parent, 1, 
/* 2000 */         IDialogConstants.CANCEL_LABEL, false);
/*      */ 
/* 2002 */       getButton(0).setLayoutData(
/* 2003 */         new GridData(ctrlBtnWidth, ctrlBtnHeight));
/* 2004 */       getButton(1).setLayoutData(
/* 2005 */         new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*      */     }
/*      */ 
/*      */     public void enableButtons()
/*      */     {
/* 2010 */       getButton(1).setEnabled(true);
/* 2011 */       getButton(0).setEnabled(true);
/*      */     }
/*      */ 
/*      */     public void cancelPressed()
/*      */     {
/* 2016 */       setReturnCode(1);
/* 2017 */       close();
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/* 2023 */       String dataURI = storeActivity();
/*      */ 
/* 2027 */       if (dataURI != null) {
/*      */         try {
/* 2029 */           StorageUtils.storeDerivedProduct(dataURI, 
/* 2030 */             this.txtSave.getText(), "TEXT", this.txtInfo.getText());
/*      */         } catch (PgenStorageException e) {
/* 2032 */           StorageUtils.showError(e);
/*      */         }
/*      */       }
/*      */ 
/* 2036 */       setReturnCode(0);
/* 2037 */       close();
/* 2038 */       SigmetAttrDlg.this.drawingLayer.removeSelected();
/* 2039 */       SigmetAttrDlg.this.close();
/* 2040 */       PgenUtil.setSelectingMode();
/*      */     }
/*      */ 
/*      */     private String storeActivity()
/*      */     {
/* 2047 */       Layer defaultLayer = new Layer();
/* 2048 */       defaultLayer.addElement(SigmetAttrDlg.this.drawingLayer
/* 2049 */         .getSelectedDE());
/* 2050 */       ArrayList layerList = new ArrayList();
/* 2051 */       layerList.add(defaultLayer);
/*      */ 
/* 2053 */       String forecaster = System.getProperty("user.name");
/* 2054 */       ProductTime refTime = new ProductTime();
/*      */ 
/* 2056 */       Product defaultProduct = new Product("Intl_SIGMET", "Intl SIGMET", 
/* 2057 */         forecaster, null, refTime, layerList);
/*      */ 
/* 2059 */       defaultProduct.setOutputFile(SigmetAttrDlg.this.drawingLayer
/* 2060 */         .buildActivityLabel(defaultProduct));
/* 2061 */       defaultProduct.setCenter(PgenUtil.getCurrentOffice());
/*      */       try
/*      */       {
/* 2064 */         dataURI = StorageUtils.storeProduct(defaultProduct);
/*      */       }
/*      */       catch (PgenStorageException e)
/*      */       {
/*      */         String dataURI;
/* 2066 */         StorageUtils.showError(e);
/* 2067 */         return null;
/*      */       }
/*      */       String dataURI;
/* 2069 */       return dataURI;
/*      */     }
/*      */ 
/*      */     private String getFileContent() {
/* 2073 */       StringBuilder sb = new StringBuilder();
/*      */ 
/* 2075 */       if (SigmetInfo.getAFOSflg()) {
/* 2076 */         sb.append("ZCZC ");
/* 2077 */         sb.append(getIdnode());
/* 2078 */         sb.append(getAfospil());
/* 2079 */         sb.append("\n").append(getWmo());
/* 2080 */         sb.append("\n").append(getFirstLine());
/* 2081 */         sb.append("\n").append(getSecondLine());
/* 2082 */         sb.append("\n").append("NNNN");
/* 2083 */         sb.append("\n");
/*      */       } else {
/* 2085 */         sb.append(getWmo());
/* 2086 */         sb.append("\n").append(getAfospil());
/* 2087 */         sb.append("\n").append(getFirstLine());
/* 2088 */         sb.append("\n").append(getSecondLine());
/*      */       }
/*      */ 
/* 2091 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     private String getFileName() {
/* 2095 */       StringBuilder sb = new StringBuilder();
/* 2096 */       sb.append(SigmetAttrDlg.this.getEditableAttrArea());
/* 2097 */       sb.append("_").append(SigmetAttrDlg.this.getEditableAttrId());
/* 2098 */       sb.append("_").append(SigmetAttrDlg.this.getEditableAttrSeqNum());
/* 2099 */       sb.append(".sigintl");
/* 2100 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     private String getWmo() {
/* 2104 */       int HEADER_WMO = 1;
/* 2105 */       StringBuilder sb = new StringBuilder();
/* 2106 */       sb.append("W");
/* 2107 */       sb.append(getWmoPhen());
/* 2108 */       sb.append(getOcnWmoAwpHeaders()[HEADER_WMO]);
/* 2109 */       sb.append(getInum());
/* 2110 */       sb.append(" ").append(SigmetAttrDlg.this.getEditableAttrArea());
/* 2111 */       sb.append(" ").append(SigmetAttrDlg.this.getTimeStringPlusHourInHMS(0));
/*      */ 
/* 2113 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     private String getAfospil() {
/* 2117 */       int HEADER_AFOSPIL = 2;
/* 2118 */       StringBuilder sb = new StringBuilder();
/*      */ 
/* 2120 */       if ("PAWU".equals(SigmetAttrDlg.this.getEditableAttrArea())) {
/* 2121 */         sb.append(getAwpPhen());
/* 2122 */         sb.append(getOcnWmoAwpHeaders()[HEADER_AFOSPIL]);
/* 2123 */         sb.append(getInum());
/* 2124 */         sb.append("\n").append(getIdnode());
/* 2125 */         sb.append(SigmetAttrDlg.this.getEditableAttrId().charAt(0));
/* 2126 */         sb.append(" WS ").append(SigmetAttrDlg.this.getTimeStringPlusHourInHMS(0));
/*      */       } else {
/* 2128 */         sb.append(getAwpPhen());
/* 2129 */         sb.append(getOcnWmoAwpHeaders()[HEADER_AFOSPIL]);
/* 2130 */         sb.append(SigmetAttrDlg.this.getEditableAttrId()
/* 2131 */           .substring(0, 1));
/*      */       }
/*      */ 
/* 2134 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     private String getFirstLine() {
/* 2138 */       StringBuilder sb = new StringBuilder();
/* 2139 */       this.startTime = SigmetAttrDlg.this.getTimeStringPlusHourInHMS(0);
/* 2140 */       this.endTime = SigmetAttrDlg.this.getTimeStringPlusHourInHMS(4);
/*      */ 
/* 2142 */       sb.append(getFirs());
/* 2143 */       sb.append(" ").append("SIGMET");
/* 2144 */       sb.append(" ").append(SigmetAttrDlg.this.getEditableAttrId());
/* 2145 */       sb.append(" ").append(SigmetAttrDlg.this.getEditableAttrSeqNum());
/* 2146 */       sb.append(" ").append("VALID");
/* 2147 */       sb.append(" ")
/* 2148 */         .append(SigmetAttrDlg.this.getEditableAttrStartTime() == null ? this.startTime : 
/* 2149 */         SigmetAttrDlg.this.getEditableAttrStartTime());
/* 2150 */       sb.append("/")
/* 2151 */         .append(SigmetAttrDlg.this.getEditableAttrEndTime() == null ? this.endTime : 
/* 2152 */         SigmetAttrDlg.this.getEditableAttrEndTime());
/*      */ 
/* 2157 */       sb.append(" ")
/* 2158 */         .append(SigmetAttrDlg.this.getEditableAttrIssueOffice())
/* 2159 */         .append("-");
/*      */ 
/* 2161 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     private String getSecondLine() {
/* 2165 */       StringBuilder sb = new StringBuilder();
/* 2166 */       boolean isPhenNameEntered = false; boolean isTropCyc = false;
/*      */ 
/* 2169 */       String phen = SigmetAttrDlg.this.getEditableAttrPhenom();
/* 2170 */       String phenName = SigmetAttrDlg.this.getEditableAttrPhenomName();
/* 2171 */       phenName = phenName == null ? phenName : phenName.toUpperCase();
/* 2172 */       if ("VOLCANIC_ASH".equals(phen)) {
/* 2173 */         isPhenNameEntered = SigmetInfo.isVolcanoNameEntered(phenName);
/*      */       }
/*      */ 
/* 2177 */       this.firCalledForSecondLine = true;
/* 2178 */       sb.append(getFirs());
/* 2179 */       this.firCalledForSecondLine = false;
/*      */ 
/* 2182 */       if (!"TROPICAL_CYCLONE".equals(phen)) {
/* 2183 */         String pString = phen == null ? 
/* 2184 */           ((String[])SigmetInfo.PHEN_MAP
/* 2184 */           .get(SigmetInfo.SIGMET_TYPES[0]))[0] : phen;
/* 2185 */         sb.append(pString.replace('_', ' ')).append(" ");
/*      */       } else {
/* 2187 */         isTropCyc = true;
/* 2188 */         isPhenNameEntered = true;
/*      */ 
/* 2190 */         sb.append("TC");
/*      */ 
/* 2192 */         if (phenName != null) {
/* 2193 */           sb.append(" ").append(phenName.trim()).append(" ");
/*      */         }
/* 2195 */         String presHPA = SigmetAttrDlg.this
/* 2196 */           .getEditableAttrPhenomPressure();
/* 2197 */         if (presHPA != null) {
/* 2198 */           sb.append(" ").append(presHPA.trim()).append("HPA ");
/*      */         }
/* 2200 */         Sigmet sig = (Sigmet)SigmetAttrDlg.this.drawingLayer
/* 2201 */           .getSelectedDE();
/* 2202 */         if ("Isolated".equals(sig.getType())) {
/* 2203 */           sb.append(" ").append("NEAR");
/*      */         }
/* 2207 */         else if ((SigmetAttrDlg.this.getEditableAttrPhenomLat() != null) && 
/* 2208 */           (SigmetAttrDlg.this.getEditableAttrPhenomLon() != null)) {
/* 2209 */           sb.append(" ").append("NEAR");
/* 2210 */           sb.append(" ").append(SigmetAttrDlg.this.getEditableAttrPhenomLat());
/* 2211 */           sb.append(SigmetAttrDlg.this.getEditableAttrPhenomLon());
/*      */         }
/*      */ 
/* 2215 */         sb.append(" ").append("AT ");
/* 2216 */         sb.append(SigmetAttrDlg.this.getTimeStringPlusHourInHMS(0).substring(0, 4));
/*      */ 
/* 2220 */         sb.append("Z.");
/*      */ 
/* 2224 */         String movement = SigmetAttrDlg.this.getEditableAttrMovement();
/* 2225 */         if ("STNRY".equals(movement)) {
/* 2226 */           sb.append(" ").append("STNR. ");
/* 2227 */         } else if ("MVG".equals(movement)) {
/* 2228 */           sb.append(" ").append("MOV");
/* 2229 */           sb.append(" ")
/* 2230 */             .append(SigmetAttrDlg.this
/* 2231 */             .getEditableAttrPhenomDirection());
/* 2232 */           sb.append(" ").append(
/* 2233 */             SigmetAttrDlg.this.getEditableAttrPhenomSpeed());
/* 2234 */           sb.append("KT.  ");
/*      */         }
/*      */ 
/* 2239 */         String maxWinds = SigmetAttrDlg.this
/* 2240 */           .getEditableAttrPhenomMaxWind();
/* 2241 */         if ((maxWinds != null) && (!"".equals(maxWinds.trim()))) {
/* 2242 */           sb.append(" ").append("MAX WINDS ");
/* 2243 */           sb.append(maxWinds).append("KT.  ");
/*      */         }
/*      */ 
/* 2248 */         String trend = SigmetAttrDlg.this.getEditableAttrTrend();
/* 2249 */         if (!"-none-".equals(trend)) {
/* 2250 */           sb.append(trend).append(".");
/*      */         }
/*      */ 
/* 2255 */         String phen2 = SigmetAttrDlg.this.getEditableAttrPhenom2();
/* 2256 */         if ((phen2 != null) && (!"".equals(phen2.trim()))) {
/* 2257 */           sb.append(" ").append(phen2.replace('_', ' ')).append(" ");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2263 */       if ("VOLCANIC_ASH".equals(phen)) {
/* 2264 */         sb.append(" ").append("FROM ");
/* 2265 */         sb.append(phenName == null ? "" : phenName).append(".");
/*      */ 
/* 2270 */         String phenLat = SigmetAttrDlg.this.getEditableAttrPhenomLat();
/*      */ 
/* 2274 */         String phenLon = SigmetAttrDlg.this.getEditableAttrPhenomLon();
/*      */ 
/* 2276 */         if ((isPhenNameEntered) && (phenLat != null) && 
/* 2277 */           (!"".equals(phenLat.trim())) && (phenLon != null) && 
/* 2278 */           (!"".equals(phenLon.trim())))
/*      */         {
/* 2280 */           sb.append(" ").append("LOC ");
/* 2281 */           sb.append(phenLat);
/* 2282 */           sb.append(phenLon);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2288 */       String tops = SigmetAttrDlg.this.getEditableAttrLevel();
/*      */ 
/* 2290 */       if (("FCST".equals(tops)) || (isTropCyc)) {
/* 2291 */         sb.append(tops.equals("-none-") ? "" : tops).append(" ");
/* 2292 */         sb.append(SigmetAttrDlg.this.getEditableAttrLevelInfo1())
/* 2293 */           .append(" ");
/* 2294 */         sb.append("FL");
/* 2295 */         String text1 = SigmetAttrDlg.this.getEditableAttrLevelText1();
/* 2296 */         sb.append(text1 == null ? "" : text1).append(" ");
/*      */ 
/* 2298 */         String levelInfo2 = SigmetAttrDlg.this
/* 2299 */           .getEditableAttrLevelInfo2();
/* 2300 */         if (!"-none-".equals(levelInfo2)) {
/* 2301 */           sb.append(levelInfo2).append(" ");
/* 2302 */           sb.append("FL");
/*      */         }
/* 2304 */         String text2 = SigmetAttrDlg.this.getEditableAttrLevelText2();
/* 2305 */         sb.append(text2 == null ? "" : text2);
/*      */       }
/*      */ 
/* 2310 */       if ((tops != null) && (tops.contains("FCST")) && 
/* 2311 */         ("VOLCANIC_ASH".equals(phen))) {
/* 2312 */         sb.append(" ");
/*      */       }
/*      */ 
/* 2321 */       String lineType = ((Sigmet)SigmetAttrDlg.this.drawingLayer
/* 2322 */         .getSelectedDE()).getType();
/* 2323 */       String fromLineWithFormat = SigmetAttrDlg.this
/* 2324 */         .getLatLonFormatFlagAndText();
/*      */ 
/* 2326 */       String[] lineArray = fromLineWithFormat
/* 2327 */         .split(":::");
/*      */ 
/* 2329 */       if ("Isolated".equals(lineType))
/*      */       {
/* 2331 */         if (isTropCyc) {
/* 2332 */           sb.append(" ").append("WITHIN ");
/* 2333 */           sb.append(SigmetAttrDlg.this.getWidth());
/* 2334 */           sb.append(" ").append("NM CENTER.");
/*      */         } else {
/* 2336 */           sb.append(" ").append("WI ");
/* 2337 */           sb.append((int)SigmetAttrDlg.this.getWidth());
/* 2338 */           sb.append(" ").append("NM OF ");
/* 2339 */           for (int i = 0; i < lineArray.length - 1; i++)
/* 2340 */             sb.append(" ").append(lineArray[i]);
/* 2341 */           sb.append(".");
/*      */         }
/*      */       }
/* 2344 */       else if ("Area".equals(lineType)) {
/* 2345 */         if (!fromLineWithFormat.contains("VOR")) {
/* 2346 */           if (sb.toString().contains("VOLCANIC"))
/* 2347 */             sb.append(" ").append("VA CLD WI AREA BOUNDED BY ");
/*      */           else
/* 2349 */             sb.append(" ").append("WI AREA BOUNDED BY");
/*      */         }
/*      */         else {
/* 2352 */           sb.append(" ").append("WI AREA BOUNDED BY LINE FM ");
/*      */         }
/*      */ 
/* 2355 */         for (int i = 0; i < lineArray.length - 1; i++)
/* 2356 */           sb.append(" ").append(lineArray[i]);
/* 2357 */         sb.append(".");
/*      */       }
/*      */       else {
/* 2360 */         sb.append(" ").append("WI ");
/* 2361 */         sb.append((int)SigmetAttrDlg.this.getWidth());
/* 2362 */         sb.append(" ").append("NM ");
/* 2363 */         sb.append(SigmetAttrDlg.this.getLineTypeForSOL(lineType));
/*      */ 
/* 2366 */         if (!fromLineWithFormat.contains("VOR"))
/* 2367 */           sb.append(" ").append("LINE");
/*      */         else {
/* 2369 */           sb.append(" ").append("LINE FM");
/*      */         }
/*      */ 
/* 2372 */         for (int i = 0; i < lineArray.length - 1; i++)
/* 2373 */           sb.append(" ").append(lineArray[i]);
/* 2374 */         sb.append(".");
/*      */       }
/*      */ 
/* 2377 */       if (!isTropCyc)
/*      */       {
/* 2380 */         if ("TOPS".equals(tops)) {
/* 2381 */           sb.append(" ").append(tops).append(" ");
/* 2382 */           sb.append(SigmetAttrDlg.this.getEditableAttrLevelInfo1())
/* 2383 */             .append(" ");
/* 2384 */           sb.append("FL");
/* 2385 */           String text1 = SigmetAttrDlg.this
/* 2386 */             .getEditableAttrLevelText1();
/* 2387 */           sb.append(text1 == null ? "" : text1).append(" ");
/*      */ 
/* 2389 */           String levelInfo2 = SigmetAttrDlg.this
/* 2390 */             .getEditableAttrLevelInfo2();
/* 2391 */           if (!"-none-".equals(levelInfo2)) {
/* 2392 */             sb.append(levelInfo2).append(" ");
/* 2393 */             sb.append("FL");
/*      */           }
/* 2395 */           String text2 = SigmetAttrDlg.this
/* 2396 */             .getEditableAttrLevelText2();
/* 2397 */           sb.append(text2 == null ? "" : text2).append(". ");
/*      */         }
/*      */ 
/* 2401 */         String movement = SigmetAttrDlg.this.getEditableAttrMovement();
/* 2402 */         if (("STNRY".equals(movement)) || (movement == null)) {
/* 2403 */           sb.append(" ").append("STNR.");
/* 2404 */         } else if ("MVG".equals(movement)) {
/* 2405 */           sb.append(" ").append("MOV");
/* 2406 */           sb.append(" ")
/* 2407 */             .append(SigmetAttrDlg.this
/* 2408 */             .getEditableAttrPhenomDirection());
/* 2409 */           sb.append(" ").append(
/* 2410 */             SigmetAttrDlg.this.getEditableAttrPhenomSpeed());
/* 2411 */           sb.append("KT. ");
/*      */         }
/*      */ 
/* 2415 */         String trend = SigmetAttrDlg.this.getEditableAttrTrend();
/* 2416 */         if ((!"-none-".equals(trend)) && (trend != null)) {
/* 2417 */           sb.append(" ").append(trend).append(".");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2422 */       String remarks = SigmetAttrDlg.this.getEditableAttrRemarks();
/* 2423 */       if ((!"-none-".equals(remarks)) && (remarks != null)) {
/* 2424 */         sb.append(" ").append(remarks).append(".");
/*      */       }
/*      */ 
/* 2428 */       String startTime = SigmetAttrDlg.this.getEditableAttrStartTime();
/*      */ 
/* 2430 */       if ("VOLCANIC_ASH".equals(phen)) {
/* 2431 */         sb.append(" ").append("FORECAST ");
/* 2432 */         sb.append(SigmetAttrDlg.this.convertTimeStringPlusHourInHMS(startTime, 6, false))
/* 2433 */           .append("Z");
/* 2434 */         sb.append(" ").append("VA CLD APRX ");
/*      */ 
/* 2436 */         String freeText = SigmetAttrDlg.this.getEditableAttrFreeText();
/* 2437 */         if ((freeText != null) && (freeText.length() > 0)) {
/* 2438 */           sb.append(freeText.toUpperCase());
/*      */         }
/*      */       }
/*      */ 
/* 2442 */       if ("TROPICAL_CYCLONE".equals(phen)) {
/* 2443 */         sb.append(" ").append("FORECAST ");
/* 2444 */         sb.append(SigmetAttrDlg.this.convertTimeStringPlusHourInHMS(startTime, 6, false))
/* 2445 */           .append("Z");
/* 2446 */         sb.append(" ").append("TC CENTER ");
/*      */ 
/* 2448 */         String freeText = SigmetAttrDlg.this.getEditableAttrFreeText();
/* 2449 */         if ((freeText != null) && (freeText.length() > 0)) {
/* 2450 */           sb.append(freeText.toUpperCase());
/*      */         }
/*      */       }
/*      */ 
/* 2454 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     private String getWmoPhen() {
/* 2458 */       String phen = SigmetAttrDlg.this.getEditableAttrPhenom();
/* 2459 */       if (phen != null)
/* 2460 */         phen = phen.trim();
/* 2461 */       if ("VOLCANIC_ASH".equals(phen))
/* 2462 */         return "V";
/* 2463 */       if ("TROPICAL_CYCLONE".equals(phen))
/* 2464 */         return "C";
/* 2465 */       return "S";
/*      */     }
/*      */ 
/*      */     private String getAwpPhen() {
/* 2469 */       String wmophen = getWmoPhen();
/* 2470 */       if ("S".equals(wmophen))
/* 2471 */         return "SIG";
/* 2472 */       if ("V".equals(wmophen))
/* 2473 */         return "WSV";
/* 2474 */       return "WST";
/*      */     }
/*      */ 
/*      */     private String getInum() {
/* 2478 */       char firstIdChar = SigmetAttrDlg.this.getEditableAttrId().charAt(0);
/*      */ 
/* 2480 */       if ("PHFO".equals(SigmetAttrDlg.this.getEditableAttrArea())) {
/* 2481 */         int inum = firstIdChar - 'M';
/* 2482 */         return "0" + inum;
/* 2483 */       }if ("PAWU".equals(SigmetAttrDlg.this.getEditableAttrArea())) {
/* 2484 */         int inum = firstIdChar - 'H';
/* 2485 */         return "0" + inum;
/*      */       }
/* 2487 */       int inum = firstIdChar - '@';
/* 2488 */       return "0" + inum;
/*      */     }
/*      */ 
/*      */     private String getIdnode() {
/* 2492 */       String area = SigmetAttrDlg.this.getEditableAttrArea();
/*      */ 
/* 2494 */       if ("KKCI".equals(area))
/* 2495 */         return "MKC";
/* 2496 */       if ("KNHC".equals(area))
/* 2497 */         return "NHC";
/* 2498 */       if ("PHFO".equals(area))
/* 2499 */         return "HFO";
/* 2500 */       return "ANC";
/*      */     }
/*      */ 
/*      */     private String[] getOcnWmoAwpHeaders() {
/* 2504 */       String area = SigmetAttrDlg.this.getEditableAttrArea();
/* 2505 */       String[] headers = new String[3];
/*      */       String tmp23_22 = (headers[2] =  = ""); headers[1] = tmp23_22; headers[0] = tmp23_22;
/*      */ 
/* 2509 */       if ("PAWU".equals(area)) {
/* 2510 */         headers[1] = "AK";
/* 2511 */         headers[2] = "AK";
/* 2512 */       } else if ("PHFO".equals(area)) {
/* 2513 */         headers[1] = "PA";
/* 2514 */         headers[2] = "PA";
/*      */       } else {
/* 2516 */         String fir = getFirs();
/* 2517 */         if ((fir != null) && (fir.length() != 0))
/*      */         {
/* 2519 */           if ((fir.contains("KZHU")) || (fir.contains("KZMA")) || 
/* 2520 */             (fir.contains("KZNY")) || (fir.contains("TJZS"))) {
/* 2521 */             headers[0] = "NT";
/* 2522 */             headers[1] = "NT";
/* 2523 */             headers[2] = "A0";
/* 2524 */           } else if ((fir.contains("KZAK")) || (fir.contains("PAZA"))) {
/* 2525 */             headers[0] = "PN";
/* 2526 */             headers[1] = "PN";
/* 2527 */             headers[2] = "P0";
/*      */           }
/*      */         }
/*      */       }
/* 2531 */       return headers;
/*      */     }
/*      */ 
/*      */     private String getFirs() {
/* 2535 */       StringBuilder fir = new StringBuilder();
/*      */ 
/* 2538 */       AbstractDrawableComponent elSelected = SigmetAttrDlg.this.drawingLayer
/* 2539 */         .getSelectedComp();
/* 2540 */       Coordinate[] coors = elSelected == null ? null : 
/* 2541 */         (Coordinate[])elSelected
/* 2541 */         .getPoints().toArray(new Coordinate[0]);
/*      */ 
/* 2543 */       String lineType = ((Sigmet)SigmetAttrDlg.this.drawingLayer
/* 2544 */         .getSelectedDE()).getType();
/*      */ 
/* 2546 */       if (coors != null) {
/* 2547 */         IMapDescriptor mapDescriptor = 
/* 2548 */           (IMapDescriptor)SigmetAttrDlg.this.drawingLayer
/* 2548 */           .getDescriptor();
/* 2549 */         double width = Double.parseDouble(SigmetAttrDlg.this.widthStr);
/*      */ 
/* 2551 */         if ("Area".equals(lineType))
/*      */         {
/* 2553 */           Coordinate[] coorsP = new Coordinate[coors.length + 1];
/* 2554 */           coorsP = (Coordinate[])Arrays.copyOf(coors, coorsP.length);
/* 2555 */           coorsP[(coorsP.length - 1)] = coors[0];
/*      */ 
/* 2557 */           Polygon areaP = 
/* 2558 */             SigmetInfo.getPolygon(coorsP, mapDescriptor);
/* 2559 */           fir.append(getFirString(areaP));
/*      */         }
/* 2561 */         else if ("Isolated".equals(lineType))
/*      */         {
/* 2563 */           Polygon areaP = SigmetInfo.getIsolatedPolygon(coors[0], 
/* 2564 */             width, mapDescriptor);
/* 2565 */           fir.append(getFirString(areaP));
/*      */         }
/*      */         else {
/* 2568 */           String subLineType = lineType
/* 2569 */             .split(":::")[1];
/* 2570 */           Polygon areaP = SigmetInfo.getSOLPolygon(coors, 
/* 2571 */             subLineType, width, mapDescriptor);
/* 2572 */           fir.append(getFirString(areaP));
/*      */         }
/*      */       }
/* 2575 */       return fir.toString();
/*      */     }
/*      */ 
/*      */     private String getFirString(Polygon areaP) {
/* 2579 */       StringBuilder fir = new StringBuilder();
/* 2580 */       Map FIR_POLYGON_MAP = 
/* 2581 */         SigmetInfo.initFirPolygonMapFromShapfile();
/*      */ 
/* 2583 */       for (String aFir : FIR_POLYGON_MAP.keySet()) {
/* 2584 */         Polygon firP = (Polygon)FIR_POLYGON_MAP.get(aFir);
/* 2585 */         if (((firP.covers(areaP)) || (firP.intersects(areaP))) && 
/* 2586 */           (!fir.toString().contains(aFir.substring(0, 4)))) {
/* 2587 */           fir.append(aFir.substring(0, 4)).append(" ");
/*      */         }
/*      */       }
/* 2590 */       String firId = fir.toString();
/*      */ 
/* 2592 */       String[] firIdArray = firId.split(" ");
/* 2593 */       StringBuilder firNameBuilder = new StringBuilder();
/* 2594 */       for (String id : firIdArray) {
/* 2595 */         String firName = "";
/* 2596 */         for (String s : SigmetInfo.FIR_ARRAY) {
/* 2597 */           if (id.equals(s.substring(0, 4))) {
/* 2598 */             firName = s.substring(5, s.length());
/*      */           }
/*      */         }
/* 2601 */         String[] ss = firName.split("_");
/* 2602 */         for (int i = 0; i < ss.length; i++) {
/* 2603 */           firNameBuilder.append(ss[i]).append(" ");
/*      */         }
/* 2605 */         if (!firId.toString().trim().equals("")) {
/* 2606 */           firNameBuilder.append(" FIR ");
/*      */         }
/*      */       }
/* 2609 */       return this.firCalledForSecondLine ? firNameBuilder.toString() : firId;
/*      */     }
/*      */ 
/*      */     public HashMap<String, Object> getAttrFromDlg() {
/* 2613 */       return new HashMap();
/*      */     }
/*      */ 
/*      */     public void setAttrForDlg(IAttribute ia)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.SigmetAttrDlg
 * JD-Core Version:    0.6.2
 */