/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.MultiPolygon;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import com.vividsolutions.jts.io.WKBReader;
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
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.AbstractSigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.ISigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*      */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*      */ import java.awt.Color;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import org.eclipse.jface.dialogs.IDialogConstants;
/*      */ import org.eclipse.swt.graphics.Font;
/*      */ import org.eclipse.swt.graphics.RGB;
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
/*      */ import org.eclipse.swt.widgets.Spinner;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ 
/*      */ public class SigmetCommAttrDlg extends AttrDlg
/*      */   implements ISigmet
/*      */ {
/*   91 */   private static SigmetCommAttrDlg INSTANCE = null;
/*      */ 
/*   93 */   private static String mouseHandlerName = null;
/*      */ 
/*   95 */   private AbstractSigmet asig = null;
/*      */ 
/*   97 */   private HashMap<String, Object> attr = new HashMap();
/*      */   public static final String AREA = "Area";
/*      */   public static final String LINE = "Line";
/*      */   public static final String ISOLATED = "Isolated";
/*      */   public static final String LINE_SEPERATER = ":::";
/*  104 */   private String lineType = "Area";
/*      */ 
/*  106 */   private String origLineType = this.lineType;
/*      */   private static final String WIDTH = "10.00";
/*  110 */   private String width = "10.00";
/*      */ 
/*  112 */   private static final String[] LINE_SIDES = { "ESOL" };
/*      */ 
/*  114 */   private String sideOfLine = LINE_SIDES[0];
/*      */ 
/*  116 */   protected Composite top = null;
/*      */ 
/*  118 */   protected ColorButtonSelector cs = null;
/*      */ 
/*  120 */   private boolean withExpandedArea = false; private boolean copiedToSigmet = false;
/*      */ 
/*  122 */   private HashMap<String, Control> attrControlMap = new HashMap();
/*      */ 
/*  124 */   private HashMap<String, Button[]> attrButtonMap = new HashMap();
/*      */ 
/*  126 */   private HashMap<Control, Control[]> controlEnablerMap = new HashMap();
/*      */ 
/*  128 */   private String editableAttrArea = "";
/*      */ 
/*  130 */   private String editableAttrFromLine = "";
/*      */ 
/*  132 */   private String editableAttrId = "EAST";
/*      */ 
/*  134 */   private String editableAttrSequence = "";
/*      */ 
/*  136 */   private Combo comboMWO = null;
/*      */ 
/*  138 */   private Combo comboID = null;
/*      */ 
/*  140 */   private Spinner spiSeq = null;
/*      */ 
/*  142 */   private Text txtInfo = null;
/*      */ 
/*  145 */   private String relatedState = "";
/*      */   private static Font txtfont;
/*      */ 
/*      */   protected SigmetCommAttrDlg(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  150 */     super(parShell);
/*  151 */     if (txtfont == null)
/*  152 */       txtfont = new Font(parShell.getDisplay(), "Courier", 12, 0);
/*      */   }
/*      */ 
/*      */   public static SigmetCommAttrDlg getInstance(Shell parShell)
/*      */   {
/*  157 */     if (INSTANCE == null) {
/*      */       try {
/*  159 */         INSTANCE = new SigmetCommAttrDlg(parShell);
/*      */       } catch (VizException e) {
/*  161 */         e.printStackTrace();
/*      */       }
/*      */     }
/*  164 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public void createButtonsForButtonBar(Composite parent)
/*      */   {
/*  170 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/*  171 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*      */ 
/*  173 */     if (("Pgen Select".equals(mouseHandlerName)) || (this.withExpandedArea)) {
/*  174 */       createButton(parent, 20091229, "Save", false);
/*      */ 
/*  177 */       createButton(parent, 20091021, "Apply", false);
/*      */ 
/*  179 */       getButton(20091229).setEnabled(true);
/*  180 */       getButton(20091229).addListener(13, new Listener()
/*      */       {
/*      */         public void handleEvent(Event e) {
/*  183 */           SigmetCommAttrDlg.this.saveApplyPressed();
/*      */ 
/*  185 */           SigmetCommAttrDlg.SigmetCommAttrDlgSaveMsgDlg md = null;
/*      */           try
/*      */           {
/*  188 */             md = new SigmetCommAttrDlg.SigmetCommAttrDlgSaveMsgDlg(SigmetCommAttrDlg.this, SigmetCommAttrDlg.this.getShell());
/*      */           } catch (Exception ee) {
/*  190 */             System.out.println(ee.getMessage());
/*      */           }
/*      */ 
/*  193 */           if (md != null)
/*  194 */             md.open();
/*      */         }
/*      */       });
/*  201 */       getButton(20091021).setEnabled(true);
/*  202 */       getButton(20091021).addListener(13, new Listener()
/*      */       {
/*      */         public void handleEvent(Event e) {
/*  205 */           SigmetCommAttrDlg.this.saveApplyPressed();
/*      */         }
/*      */       });
/*  208 */       getButton(20091229).setLayoutData(
/*  209 */         new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*  210 */       getButton(20091021).setLayoutData(
/*  211 */         new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*      */     }
/*      */     else {
/*  214 */       createButton(parent, 0, 
/*  215 */         IDialogConstants.OK_LABEL, true);
/*  216 */       getButton(0).setLayoutData(
/*  217 */         new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*      */     }
/*  219 */     createButton(parent, 1, 
/*  220 */       IDialogConstants.CANCEL_LABEL, false);
/*  221 */     setMouseHandlerName(null);
/*  222 */     this.withExpandedArea = false;
/*      */ 
/*  224 */     getButton(1).setLayoutData(
/*  225 */       new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*      */   }
/*      */ 
/*      */   public void enableButtons()
/*      */   {
/*  231 */     getButton(1).setEnabled(true);
/*      */   }
/*      */ 
/*      */   public AbstractSigmet getAbstractSigmet()
/*      */   {
/*  237 */     return this.asig;
/*      */   }
/*      */ 
/*      */   public void setAbstractSigmet(DrawableElement de)
/*      */   {
/*  242 */     this.asig = ((AbstractSigmet)de);
/*  243 */     setLineType(this.asig.getType());
/*      */ 
/*  245 */     Coordinate[] coors = this.asig.getLinePoints();
/*  246 */     String s = getVOR(coors);
/*  247 */     setEditableAttrFromLine(s);
/*  248 */     this.relatedState = getRelatedStates(coors, de);
/*  249 */     ((AbstractSigmet)de).setEditableAttrFromLine(s);
/*  250 */     if ((this.txtInfo != null) && (!this.txtInfo.isDisposed()) && (s != null))
/*  251 */       this.txtInfo.setText(s);
/*      */   }
/*      */ 
/*      */   private void copyEditableAttrToAbstractSigmet(AbstractSigmet ba)
/*      */   {
/*  256 */     ba.setColors(getColors());
/*  257 */     ba.setEditableAttrArea(getEditableAttrArea());
/*  258 */     ba.setEditableAttrFromLine(getEditableAttrFromLine());
/*  259 */     ba.setEditableAttrId(getEditableAttrId());
/*  260 */     if ((getEditableAttrSequence() == null) || 
/*  261 */       (getEditableAttrSequence().length() == 0))
/*  262 */       setEditableAttrSequence("1");
/*  263 */     ba.setEditableAttrSeqNum(getEditableAttrSequence());
/*  264 */     ba.setType(getLineType());
/*  265 */     ba.setWidth(getWidth());
/*      */ 
/*  267 */     this.copiedToSigmet = true;
/*      */   }
/*      */ 
/*      */   public Color[] getColors()
/*      */   {
/*  273 */     Color[] colors = new Color[2];
/*  274 */     colors[0] = new Color(this.cs.getColorValue().red, 
/*  275 */       this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*  276 */     colors[1] = Color.green;
/*      */ 
/*  278 */     return colors;
/*      */   }
/*      */ 
/*      */   private void setColor(Color clr) {
/*  282 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*      */   }
/*      */ 
/*      */   public String getLineType() {
/*  286 */     return this.lineType;
/*      */   }
/*      */ 
/*      */   public void setLineType(String lType) {
/*  290 */     setOrigLineType(getLineType());
/*  291 */     this.lineType = lType;
/*      */   }
/*      */ 
/*      */   public String getOrigLineType() {
/*  295 */     return this.origLineType;
/*      */   }
/*      */ 
/*      */   public void setOrigLineType(String lType) {
/*  299 */     this.origLineType = lType;
/*      */   }
/*      */ 
/*      */   public String getSideOfLine() {
/*  303 */     return this.sideOfLine;
/*      */   }
/*      */ 
/*      */   public void setSideOfLine(String lineSideString) {
/*  307 */     this.sideOfLine = lineSideString;
/*      */   }
/*      */ 
/*      */   public double getWidth() {
/*  311 */     return Double.parseDouble(this.width);
/*      */   }
/*      */ 
/*      */   public void setWidth(String widthString) {
/*  315 */     this.width = widthString;
/*      */   }
/*      */ 
/*      */   private double stringToDouble(String s) {
/*  319 */     double dValue = 10.0D;
/*      */     try {
/*  321 */       dValue = Double.parseDouble(s);
/*  322 */       dValue = (dValue < 0.0D) || (dValue > 5000.0D) ? 10.0D : dValue;
/*      */     } catch (NumberFormatException localNumberFormatException) {
/*      */     }
/*      */     finally {
/*  326 */       return dValue;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute attr)
/*      */   {
/*  332 */     Color clr = attr.getColors()[0];
/*  333 */     if (clr != null)
/*  334 */       setColor(clr);
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  341 */     this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/*  343 */     GridLayout mainLayout = new GridLayout(8, false);
/*  344 */     mainLayout.marginHeight = 3;
/*  345 */     mainLayout.marginWidth = 3;
/*  346 */     this.top.setLayout(mainLayout);
/*      */ 
/*  348 */     if ("CONV_SIGMET".equals(this.pgenType))
/*  349 */       getShell().setText("Convective SIGMET Edit");
/*  350 */     else if ("NCON_SIGMET".equals(this.pgenType))
/*  351 */       getShell().setText("Non-convective SIGMET Edit");
/*  352 */     else if ("AIRM_SIGMET".equals(this.pgenType))
/*  353 */       getShell().setText("AIRMET Edit");
/*  354 */     else if ("OUTL_SIGMET".equals(this.pgenType)) {
/*  355 */       getShell().setText("Convective Outlook Edit");
/*      */     }
/*  357 */     if (("NCON_SIGMET".equals(this.pgenType)) || 
/*  358 */       ("AIRM_SIGMET".equals(this.pgenType)) || 
/*  359 */       ("OUTL_SIGMET".equals(this.pgenType))) {
/*  360 */       setLineType("Area");
/*      */     }
/*      */ 
/*  363 */     Button btnArea = new Button(this.top, 16);
/*  364 */     btnArea.setSelection(true);
/*  365 */     btnArea.setText("Area");
/*  366 */     if (mouseHandlerName == null) {
/*  367 */       setLineType("Area");
/*      */     }
/*  369 */     Button btnLine = new Button(this.top, 16);
/*  370 */     btnLine.setText("Line");
/*      */ 
/*  372 */     final Combo comboLine = new Combo(this.top, 8);
/*  373 */     this.attrControlMap.put("lineType", comboLine);
/*  374 */     comboLine.setItems(LINE_SIDES);
/*  375 */     this.attrControlMap.put("sideOfLine", comboLine);
/*  376 */     comboLine.select(0);
/*  377 */     comboLine.setEnabled(false);
/*      */ 
/*  379 */     Button btnIsolated = new Button(this.top, 16);
/*  380 */     btnIsolated.setText("Isolated  ");
/*      */ 
/*  382 */     Label lblText = new Label(this.top, 16384);
/*  383 */     lblText.setText("Width: ");
/*  384 */     final Text txtWidth = new Text(this.top, 2052);
/*  385 */     this.attrControlMap.put("width", txtWidth);
/*  386 */     txtWidth.setText("10.00");
/*  387 */     txtWidth.setEnabled(false);
/*  388 */     this.attrButtonMap.put("lineType", new Button[] { btnArea, btnLine, 
/*  389 */       btnIsolated });
/*      */ 
/*  391 */     btnArea.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  393 */         comboLine.setEnabled(false);
/*  394 */         txtWidth.setEnabled(false);
/*      */ 
/*  396 */         SigmetCommAttrDlg.this.setLineType("Area");
/*      */       }
/*      */     });
/*  400 */     btnLine.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  402 */         comboLine.setEnabled(true);
/*  403 */         txtWidth.setEnabled(true);
/*      */ 
/*  405 */         SigmetCommAttrDlg.this.setLineType("Line:::" + 
/*  406 */           SigmetCommAttrDlg.this.getSideOfLine());
/*      */       }
/*      */     });
/*  410 */     btnIsolated.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  412 */         comboLine.setEnabled(false);
/*  413 */         txtWidth.setEnabled(true);
/*      */ 
/*  415 */         SigmetCommAttrDlg.this.setLineType("Isolated");
/*      */       }
/*      */     });
/*  419 */     comboLine.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  421 */         SigmetCommAttrDlg.this.setSideOfLine(comboLine.getText());
/*  422 */         SigmetCommAttrDlg.this.setLineType("Line:::" + 
/*  423 */           SigmetCommAttrDlg.this.getSideOfLine());
/*      */       }
/*      */     });
/*  427 */     txtWidth.addListener(24, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  429 */         SigmetCommAttrDlg.this.setWidth(txtWidth.getText());
/*      */       }
/*      */     });
/*  433 */     Label colorLbl = new Label(this.top, 16384);
/*  434 */     colorLbl.setText("Color:");
/*      */ 
/*  436 */     this.cs = new ColorButtonSelector(this.top);
/*  437 */     Color clr = getDefaultColor(this.pgenType);
/*  438 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*      */ 
/*  441 */     if ((!"INTL_SIGMET".equals(this.pgenType)) && (!"CONV_SIGMET".equals(this.pgenType))) {
/*  442 */       btnLine.setEnabled(false);
/*  443 */       btnIsolated.setEnabled(false);
/*  444 */       comboLine.setEnabled(false);
/*  445 */       txtWidth.setEnabled(false);
/*      */     }
/*      */ 
/*  448 */     if (("Pgen Select".equals(mouseHandlerName)) || (this.withExpandedArea))
/*      */     {
/*  450 */       String[] MWO_ITEMS = (String[])SigmetInfo.AREA_MAP.get(
/*  451 */         SigmetInfo.getSigmetTypeString(this.pgenType));
/*  452 */       String[] ID_ITEMS = (String[])SigmetInfo.ID_MAP.get(
/*  453 */         SigmetInfo.getSigmetTypeString(this.pgenType));
/*      */ 
/*  455 */       Composite topSelect = (Composite)super.createDialogArea(parent);
/*      */ 
/*  457 */       GridLayout mainLayout2 = new GridLayout(8, false);
/*  458 */       mainLayout2.marginHeight = 3;
/*  459 */       mainLayout2.marginWidth = 3;
/*  460 */       topSelect.setLayout(mainLayout2);
/*      */ 
/*  462 */       Group top2 = new Group(topSelect, 16384);
/*  463 */       top2.setLayoutData(new GridData(4, 16777216, true, true, 
/*  464 */         8, 1));
/*  465 */       top2.setLayout(new GridLayout(8, false));
/*      */ 
/*  467 */       Label lblMWO = new Label(top2, 16384);
/*  468 */       lblMWO.setText(" MWO: ");
/*  469 */       this.comboMWO = new Combo(top2, 8);
/*  470 */       this.attrControlMap.put("editableAttrArea", this.comboMWO);
/*  471 */       this.comboMWO.setItems(MWO_ITEMS);
/*  472 */       this.comboMWO.select(0);
/*      */ 
/*  474 */       this.comboMWO.setLayoutData(new GridData(16384, 16777216, true, 
/*  475 */         true, 1, 1));
/*      */ 
/*  477 */       this.comboMWO.addListener(13, new Listener() {
/*      */         public void handleEvent(Event e) {
/*  479 */           SigmetCommAttrDlg.this.setEditableAttrArea(SigmetCommAttrDlg.this.comboMWO
/*  480 */             .getText());
/*      */         }
/*      */       });
/*  484 */       Label lblID = new Label(top2, 16384);
/*  485 */       lblID.setText(" ID: ");
/*  486 */       this.comboID = new Combo(top2, 8);
/*  487 */       this.attrControlMap.put("editableAttrId", this.comboID);
/*  488 */       this.comboID.setItems(ID_ITEMS);
/*  489 */       this.comboID.select(0);
/*      */ 
/*  491 */       this.comboID.setLayoutData(new GridData(16384, 16777216, true, 
/*  492 */         true, 1, 1));
/*      */ 
/*  494 */       this.comboID.addListener(13, new Listener() {
/*      */         public void handleEvent(Event e) {
/*  496 */           SigmetCommAttrDlg.this.setEditableAttrId(SigmetCommAttrDlg.this.comboID.getText());
/*      */         }
/*      */       });
/*  500 */       Label lblSequence = new Label(top2, 16384);
/*  501 */       lblSequence.setText(" Sequence: ");
/*  502 */       this.spiSeq = new Spinner(top2, 2048);
/*  503 */       this.attrControlMap.put("editableAttrSeqNum", this.spiSeq);
/*  504 */       this.spiSeq.setMinimum(1);
/*  505 */       this.spiSeq.setMaximum(300);
/*      */ 
/*  507 */       this.spiSeq.setLayoutData(new GridData(4, 16777216, true, 
/*  508 */         false, 3, 1));
/*      */ 
/*  510 */       this.spiSeq.addListener(13, new Listener() {
/*      */         public void handleEvent(Event e) {
/*  512 */           SigmetCommAttrDlg.this.setEditableAttrSequence(
/*  513 */             SigmetCommAttrDlg.this.spiSeq.getSelection());
/*      */         }
/*      */       });
/*  517 */       Button btnNew = new Button(top2, 16);
/*      */ 
/*  519 */       btnNew.setText("New(Prepend dir)");
/*  520 */       btnNew.setLayoutData(new GridData(16384, 16777216, true, 
/*  521 */         false, 2, 1));
/*  522 */       btnNew.setEnabled(false);
/*      */ 
/*  524 */       Button btnOld = new Button(top2, 16);
/*  525 */       btnOld.setText("Old(Postpend dir)");
/*  526 */       btnOld.setLayoutData(new GridData(16384, 16777216, true, 
/*  527 */         false, 2, 1));
/*  528 */       btnOld.setEnabled(false);
/*      */ 
/*  530 */       Button btnVor = new Button(top2, 16);
/*  531 */       btnVor.setText("VOR");
/*  532 */       btnVor.setLayoutData(new GridData(16384, 16777216, true, 
/*  533 */         false, 4, 1));
/*  534 */       btnVor.setEnabled(true);
/*  535 */       btnVor.setSelection(true);
/*      */ 
/*  537 */       int style = 2570;
/*  538 */       this.txtInfo = new Text(top2, style);
/*  539 */       this.txtInfo.setFont(txtfont);
/*  540 */       this.attrControlMap.put("editableAttrFromLine", this.txtInfo);
/*  541 */       GridData gData = new GridData(600, 48);
/*  542 */       gData.horizontalSpan = 8;
/*  543 */       this.txtInfo.setLayoutData(gData);
/*  544 */       this.txtInfo.setText(getEditableAttrFromLine());
/*  545 */       if ((this.editableAttrFromLine == null) || (this.editableAttrFromLine.equals(""))) {
/*  546 */         setEditableAttrFromLine(this.txtInfo.getText());
/*      */       }
/*  548 */       this.attrButtonMap.put("editableAttrFromLine", new Button[] { btnNew, 
/*  549 */         btnOld, btnVor });
/*      */ 
/*  551 */       StringBuilder coorsLatLon = new StringBuilder();
/*  552 */       AbstractDrawableComponent elSelected = 
/*  553 */         PgenSession.getInstance().getPgenResource().getSelectedComp();
/*  554 */       Coordinate[] coors = elSelected == null ? null : 
/*  555 */         (Coordinate[])elSelected
/*  555 */         .getPoints().toArray(new Coordinate[0]);
/*      */ 
/*  557 */       if ((coors != null) && (
/*  558 */         (this.editableAttrFromLine == null) || 
/*  559 */         (this.editableAttrFromLine.equals(""))))
/*      */       {
/*  561 */         StringBuilder sb = new StringBuilder();
/*  562 */         sb.append(getVOR(coors));
/*  563 */         this.txtInfo.setText(sb.toString());
/*      */ 
/*  565 */         coorsLatLon.append(":::");
/*  566 */         String str = "VOR";
/*      */       }
/*      */ 
/*  571 */       if (this.asig == null) {
/*  572 */         setEditableAttrArea(this.comboMWO.getText());
/*  573 */         setEditableAttrId(this.comboID.getText());
/*  574 */         setEditableAttrSequence(this.spiSeq.getSelection());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  579 */     if (this.asig == null) {
/*  580 */       setLineType("Area");
/*  581 */       setSideOfLine(comboLine.getText());
/*  582 */       setWidth(txtWidth.getText());
/*      */     }
/*  584 */     init();
/*  585 */     addSeparator(this.top.getParent());
/*      */ 
/*  587 */     return this.top;
/*      */   }
/*      */ 
/*      */   private String getVOR(Coordinate[] coors) {
/*  591 */     boolean isSnapped = false;
/*  592 */     String vorConnector = ("NCON_SIGMET".equals(this.pgenType)) || 
/*  593 */       ("AIRM_SIGMET"
/*  593 */       .equals(this.pgenType)) ? " TO " : "-";
/*      */ 
/*  603 */     if (("OUTL_SIGMET".equals(this.pgenType)) || ("CONV_SIGMET".equals(this.pgenType)) || 
/*  604 */       ("NCON_SIGMET".equals(this.pgenType))) {
/*  605 */       return SnapUtil.getVORText(coors, vorConnector, this.lineType, 6, 
/*  606 */         isSnapped, true, false, this.pgenType);
/*      */     }
/*  608 */     return SnapUtil.getVORText(coors, vorConnector, this.lineType, 6, isSnapped);
/*      */   }
/*      */ 
/*      */   private void init() {
/*  612 */     if (this.asig == null) {
/*  613 */       return;
/*      */     }
/*  615 */     Button[] btns = (Button[])this.attrButtonMap.get("lineType");
/*  616 */     if (btns != null) {
/*  617 */       if ((this.lineType.equals("Area")) || ("NCON_SIGMET".equals(this.pgenType)) || 
/*  618 */         ("AIRM_SIGMET".equals(this.pgenType)) || 
/*  619 */         ("OUTL_SIGMET".equals(this.pgenType))) {
/*  620 */         btns[0].setSelection(true);
/*  621 */         btns[1].setSelection(false);
/*  622 */         btns[2].setSelection(false);
/*  623 */       } else if (this.lineType.contains("Line")) {
/*  624 */         btns[0].setSelection(false);
/*  625 */         btns[1].setSelection(true);
/*  626 */         btns[2].setSelection(false);
/*      */ 
/*  628 */         ((Control)this.attrControlMap.get("lineType")).setEnabled(true);
/*  629 */         ((Control)this.attrControlMap.get("width")).setEnabled(true);
/*  630 */       } else if (this.lineType.equals("Isolated")) {
/*  631 */         btns[0].setSelection(false);
/*  632 */         btns[1].setSelection(false);
/*  633 */         btns[2].setSelection(true);
/*      */ 
/*  635 */         ((Control)this.attrControlMap.get("width")).setEnabled(true);
/*      */       }
/*      */     }
/*      */ 
/*  639 */     Combo comboLine = (Combo)this.attrControlMap.get("lineType");
/*  640 */     String lt = getLineType();
/*  641 */     if ((comboLine != null) && (!comboLine.isDisposed()) && (lt != null) && 
/*  642 */       (lt.contains(":::"))) {
/*  643 */       if (lt.length() > 7)
/*  644 */         comboLine.setText(lt.substring(7));
/*      */       else {
/*  646 */         comboLine.select(0);
/*      */       }
/*      */     }
/*  649 */     Text txtWidth = (Text)this.attrControlMap.get("width");
/*  650 */     String width = this.width == null ? "10.00" : this.width;
/*  651 */     if ((txtWidth != null) && (!txtWidth.isDisposed())) {
/*  652 */       txtWidth.setText(width);
/*      */     }
/*  654 */     Combo comboMWO = (Combo)this.attrControlMap.get("editableAttrArea");
/*  655 */     if ((comboMWO != null) && (!comboMWO.isDisposed()) && 
/*  656 */       (getEditableAttrArea() != null)) {
/*  657 */       String area = getEditableAttrArea();
/*  658 */       if ((area != null) && (area.length() > 0))
/*  659 */         comboMWO.setText(area);
/*      */       else {
/*  661 */         comboMWO.select(0);
/*      */       }
/*      */     }
/*  664 */     Combo comboId = (Combo)this.attrControlMap.get("editableAttrId");
/*  665 */     if ((comboId != null) && (!comboId.isDisposed()) && 
/*  666 */       (getEditableAttrId() != null)) {
/*  667 */       String id = getEditableAttrId();
/*  668 */       if ((id != null) && (id.length() > 0))
/*  669 */         comboId.setText(id);
/*      */       else {
/*  671 */         comboId.select(0);
/*      */       }
/*      */     }
/*  674 */     Spinner seq = (Spinner)this.attrControlMap.get("editableAttrSeqNum");
/*  675 */     if ((seq != null) && (!seq.isDisposed()) && 
/*  676 */       (getEditableAttrSequence() != null)) {
/*  677 */       String seqAttr = getEditableAttrSequence();
/*  678 */       int i = 0;
/*      */       try {
/*  680 */         i = Integer.parseInt(seqAttr);
/*      */       } catch (Exception e) {
/*  682 */         i = 0;
/*      */       }
/*  684 */       seq.setSelection(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getEditableAttrArea()
/*      */   {
/*  691 */     return this.editableAttrArea;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrArea(String editableAttrArea) {
/*  695 */     this.editableAttrArea = editableAttrArea;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrFromLine() {
/*  699 */     return this.editableAttrFromLine;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrFromLine(String editableAttrFromLine) {
/*  703 */     this.editableAttrFromLine = editableAttrFromLine;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrId() {
/*  707 */     return this.editableAttrId;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrId(String editableAttrId) {
/*  711 */     this.editableAttrId = editableAttrId;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrSequence() {
/*  715 */     return this.editableAttrSequence;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrSequence(String editableAttrSequence) {
/*  719 */     this.editableAttrSequence = editableAttrSequence;
/*      */   }
/*      */ 
/*      */   private String getRelatedStates(Coordinate[] c1, DrawableElement de)
/*      */   {
/*  892 */     StringBuilder sb = new StringBuilder();
/*      */ 
/*  894 */     String lineType = ((Sigmet)de).getType();
/*      */ 
/*  896 */     Polygon cSigPoly = null;
/*  897 */     IMapDescriptor mapDescriptor = 
/*  898 */       (IMapDescriptor)PgenSession.getInstance().getPgenResource().getDescriptor();
/*  899 */     if (c1 != null)
/*      */     {
/*  901 */       double width = Double.parseDouble(this.width);
/*      */ 
/*  903 */       if ("Area".equals(lineType))
/*      */       {
/*  905 */         Coordinate[] coorsP = new Coordinate[c1.length + 1];
/*  906 */         coorsP = (Coordinate[])Arrays.copyOf(c1, coorsP.length);
/*  907 */         coorsP[(coorsP.length - 1)] = c1[0];
/*      */ 
/*  909 */         cSigPoly = SigmetInfo.getPolygon(coorsP, mapDescriptor);
/*      */       }
/*  911 */       else if ("Isolated".equals(lineType))
/*      */       {
/*  913 */         cSigPoly = SigmetInfo.getIsolatedPolygon(c1[0], width, 
/*  914 */           mapDescriptor);
/*      */       }
/*      */       else {
/*  917 */         String subLineType = lineType
/*  918 */           .split(":::")[1];
/*  919 */         cSigPoly = SigmetInfo.getSOLPolygon(c1, subLineType, width, 
/*  920 */           mapDescriptor);
/*      */       }
/*      */     }
/*      */ 
/*  924 */     List s = getAreaStringList(cSigPoly, mapDescriptor, "state", 
/*  925 */       "bounds.statebnds");
/*  926 */     List l = getAreaStringList(cSigPoly, mapDescriptor, "id", 
/*  927 */       "bounds.greatlakesbnds");
/*  928 */     List c = getAreaStringList(cSigPoly, mapDescriptor, "id", 
/*  929 */       "bounds.adjcstlbnds");
/*      */ 
/*  931 */     return getStates(s, l, c);
/*      */   }
/*      */ 
/*      */   public void saveApplyPressed()
/*      */   {
/*  961 */     ArrayList adcList = null;
/*  962 */     ArrayList newList = new ArrayList();
/*      */ 
/*  964 */     if (this.drawingLayer != null) {
/*  965 */       adcList = (ArrayList)this.drawingLayer
/*  966 */         .getAllSelected();
/*      */     }
/*      */ 
/*  969 */     if ((adcList != null) && (!adcList.isEmpty()))
/*      */     {
/*  971 */       for (AbstractDrawableComponent adc : adcList)
/*      */       {
/*  973 */         Sigmet el = (Sigmet)adc.getPrimaryDE();
/*  974 */         if (el != null) {
/*  975 */           Sigmet newEl = (Sigmet)el.copy();
/*      */ 
/*  977 */           attrUpdate();
/*      */ 
/*  979 */           copyEditableAttrToAbstractSigmet(newEl);
/*      */ 
/*  982 */           newEl = convertType(newEl);
/*      */ 
/*  984 */           setAbstractSigmet(newEl);
/*  985 */           newList.add(newEl);
/*      */         }
/*      */       }
/*      */ 
/*  989 */       ArrayList oldList = new ArrayList(
/*  990 */         adcList);
/*  991 */       this.drawingLayer.replaceElements(oldList, newList);
/*      */     }
/*      */ 
/*  994 */     this.drawingLayer.removeSelected();
/*  995 */     for (AbstractDrawableComponent adc : newList) {
/*  996 */       this.drawingLayer.addSelected(adc);
/*      */     }
/*      */ 
/*  999 */     if (this.mapEditor != null)
/* 1000 */       this.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   private void attrUpdate()
/*      */   {
/* 1005 */     setEditableAttrArea(this.comboMWO.getText());
/* 1006 */     setEditableAttrId(this.comboID.getText());
/* 1007 */     setEditableAttrSequence(this.spiSeq.getText());
/*      */   }
/*      */ 
/*      */   public void setMouseHandlerName(String mhName) {
/* 1011 */     mouseHandlerName = mhName;
/*      */   }
/*      */ 
/*      */   static Color getDefaultColor(String pType) {
/* 1015 */     if ("INTL_SIGMET".equalsIgnoreCase(pType))
/* 1016 */       return Color.cyan;
/* 1017 */     if ("CONV_SIGMET".equalsIgnoreCase(pType))
/* 1018 */       return Color.yellow;
/* 1019 */     if ("AIRM_SIGMET".equalsIgnoreCase(pType))
/* 1020 */       return Color.green;
/* 1021 */     if ("NCON_SIGMET".equalsIgnoreCase(pType))
/* 1022 */       return Color.magenta;
/* 1023 */     if ("OUTL_SIGMET".equalsIgnoreCase(pType)) {
/* 1024 */       return Color.orange;
/*      */     }
/* 1026 */     return Color.green;
/*      */   }
/*      */ 
/*      */   public static List<Object[]> initAllStates(String field, String table)
/*      */   {
/* 1032 */     return PgenStaticDataProvider.getProvider().queryNcepDB(field, table);
/*      */   }
/*      */ 
/*      */   public static List<String> getAreaStringList(Polygon cSigPoly, IMapDescriptor mapDescriptor, String field, String table)
/*      */   {
/* 1037 */     List list = new ArrayList();
/*      */ 
/* 1039 */     WKBReader wkbReader = new WKBReader();
/*      */ 
/* 1041 */     for (Object[] state : initAllStates(field, table))
/*      */     {
/* 1044 */       byte[] wkb = (byte[])state[0];
/*      */ 
/* 1046 */       MultiPolygon stateGeo = null;
/*      */       try
/*      */       {
/* 1049 */         stateGeo = (MultiPolygon)wkbReader.read(wkb);
/*      */       } catch (Exception e) {
/* 1051 */         System.out
/* 1052 */           .println("___ Error: SigmetCommAttrDlg: getAreaString(): " + 
/* 1053 */           e.getMessage());
/*      */       }
/*      */ 
/* 1056 */       if (stateGeo != null) {
/* 1057 */         for (int i = 0; i < stateGeo.getNumGeometries(); i++) {
/* 1058 */           Geometry g = stateGeo.getGeometryN(i);
/* 1059 */           if (g != null) {
/* 1060 */             Coordinate[] cc = g.getCoordinates();
/* 1061 */             Coordinate[] ccc = new Coordinate[cc.length + 1];
/* 1062 */             ccc = (Coordinate[])Arrays.copyOf(cc, cc.length);
/* 1063 */             ccc[(ccc.length - 1)] = cc[0];
/*      */ 
/* 1065 */             Polygon sp = SigmetInfo.getPolygon(ccc, mapDescriptor);
/* 1066 */             if ((cSigPoly.intersects(sp)) || (cSigPoly.covers(sp)) || 
/* 1067 */               (cSigPoly.within(sp))) {
/* 1068 */               String s = (String)state[1];
/* 1069 */               if ((s != null) && (!list.contains(s))) {
/* 1070 */                 list.add(s.toUpperCase());
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1078 */     return list;
/*      */   }
/*      */ 
/*      */   public static String getStates(List<String> states, List<String> lakes, List<String> coastal)
/*      */   {
/* 1085 */     if (((states == null) || (states.size() == 0)) && 
/* 1086 */       ((lakes == null) || (lakes.size() == 0)) && (
/* 1087 */       (coastal == null) || (coastal.size() == 0)))
/*      */     {
/* 1089 */       return "";
/*      */     }
/*      */ 
/* 1093 */     if ((states == null) || (states.size() == 0)) {
/* 1094 */       if ((lakes == null) || (lakes.size() == 0)) {
/* 1095 */         return list2String(coastal) + " CSTL WTRS";
/*      */       }
/* 1097 */       return list2String(lakes);
/*      */     }
/*      */ 
/* 1101 */     if ((coastal == null) || (coastal.size() == 0)) {
/* 1102 */       if (states != null) {
/* 1103 */         states.addAll(lakes);
/* 1104 */         return list2String(states);
/*      */       }
/*      */ 
/* 1107 */       return list2String(lakes);
/*      */     }
/*      */ 
/* 1111 */     if (states.size() != coastal.size())
/*      */     {
/* 1113 */       states.addAll(lakes);
/*      */ 
/* 1115 */       return list2String(states) + " AND " + list2String(coastal) + 
/* 1116 */         " CSTL WTRS";
/*      */     }
/*      */ 
/* 1119 */     if (is2ListSame(states, coastal)) {
/* 1120 */       return list2String(states) + " AND CSTL WTRS";
/*      */     }
/* 1122 */     states.addAll(lakes);
/* 1123 */     return list2String(states) + " AND " + list2String(coastal) + 
/* 1124 */       " CSTL WTRS";
/*      */   }
/*      */ 
/*      */   public static boolean is2ListSame(List<String> s, List<String> c)
/*      */   {
/* 1131 */     for (String ss : s) {
/* 1132 */       if (!c.contains(ss)) {
/* 1133 */         return false;
/*      */       }
/*      */     }
/* 1136 */     return true;
/*      */   }
/*      */ 
/*      */   public static String list2String(List<String> s)
/*      */   {
/* 1141 */     if ((s == null) || (s.size() == 0)) {
/* 1142 */       return "";
/*      */     }
/*      */ 
/* 1145 */     StringBuilder sb = new StringBuilder();
/*      */ 
/* 1147 */     for (String ss : s) {
/* 1148 */       sb.append(ss).append(" ");
/*      */     }
/*      */ 
/* 1151 */     return sb.toString().trim();
/*      */   }
/*      */ 
/*      */   public Coordinate[] getLinePoints()
/*      */   {
/* 1157 */     return null;
/*      */   }
/*      */ 
/*      */   public String getPatternName()
/*      */   {
/* 1163 */     return null;
/*      */   }
/*      */ 
/*      */   public int getSmoothFactor()
/*      */   {
/* 1169 */     return 0;
/*      */   }
/*      */ 
/*      */   public Boolean isClosedLine()
/*      */   {
/* 1175 */     return null;
/*      */   }
/*      */ 
/*      */   public Boolean isFilled()
/*      */   {
/* 1181 */     return null;
/*      */   }
/*      */ 
/*      */   public FillPatternList.FillPattern getFillPattern()
/*      */   {
/* 1187 */     return null;
/*      */   }
/*      */ 
/*      */   public Sigmet convertType(Sigmet newEl)
/*      */   {
/* 1192 */     String origLineType = getOrigLineType();
/* 1193 */     String newLineType = getLineType();
/*      */ 
/* 1195 */     if (!newLineType.equals(origLineType))
/*      */     {
/* 1197 */       float p45 = 45.0F; float p135 = 135.0F; float p225 = 225.0F; float p315 = 315.0F;
/*      */ 
/* 1199 */       ArrayList ptsCopy = newEl.getPoints();
/* 1200 */       ArrayList newPtsCopy = new ArrayList();
/*      */ 
/* 1202 */       if ("Isolated".equals(origLineType))
/*      */       {
/* 1207 */         Coordinate centerCoor = (Coordinate)ptsCopy.get(0);
/*      */ 
/* 1209 */         if (newLineType.startsWith("Line"))
/*      */         {
/* 1213 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 1214 */             Float.parseFloat(this.width), p315));
/* 1215 */           newPtsCopy.add(centerCoor);
/* 1216 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 1217 */             Float.parseFloat(this.width), p135));
/*      */         }
/*      */         else
/*      */         {
/* 1222 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 1223 */             Float.parseFloat(this.width), p45));
/* 1224 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 1225 */             Float.parseFloat(this.width), p135));
/* 1226 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 1227 */             Float.parseFloat(this.width), p225));
/* 1228 */           newPtsCopy.add(PgenUtil.computePoint(centerCoor, 
/* 1229 */             Float.parseFloat(this.width), p315));
/*      */         }
/*      */ 
/* 1232 */         newEl.setPoints(newPtsCopy);
/* 1233 */       } else if ("Isolated".equals(newLineType))
/*      */       {
/* 1237 */         newPtsCopy.add((Coordinate)ptsCopy.get(0));
/* 1238 */         newEl.setPoints(newPtsCopy);
/*      */       }
/*      */     }
/*      */ 
/* 1242 */     setSigmetFromLine(newEl);
/* 1243 */     return newEl;
/*      */   }
/*      */ 
/*      */   public void setSigmetFromLine(DrawableElement sigmet)
/*      */   {
/* 1248 */     Coordinate[] coors = ((Sigmet)sigmet).getLinePoints();
/*      */ 
/* 1250 */     StringBuilder s = new StringBuilder();
/* 1251 */     s.append(getVOR(coors));
/* 1252 */     s.append(":::");
/* 1253 */     String fromLineText = "VOR";
/* 1254 */     setEditableAttrFromLine(fromLineText);
/*      */ 
/* 1256 */     if ((this.txtInfo != null) && (!this.txtInfo.isDisposed()) && (s != null))
/* 1257 */       this.txtInfo.setText(s.toString());
/*      */   }
/*      */ 
/*      */   public void copyEditableAttrToSigmetAttrDlg(AbstractSigmet sig) {
/* 1261 */     setEditableAttrArea(sig.getEditableAttrArea());
/*      */ 
/* 1264 */     setEditableAttrFromLine(sig.getEditableAttrFromLine());
/* 1265 */     setEditableAttrId(sig.getEditableAttrId());
/* 1266 */     setEditableAttrSequence(sig.getEditableAttrSeqNum());
/* 1267 */     setLineType(sig.getType());
/* 1268 */     setWidth(sig.getWidth());
/*      */   }
/*      */ 
/*      */   private class SigmetCommAttrDlgSaveMsgDlg extends AttrDlg
/*      */   {
/*      */     Text txtInfo;
/*      */     Text txtSave;
/*  728 */     String dirLocal = ".";
/*      */ 
/*  730 */     boolean firCalledForSecondLine = false;
/*      */ 
/*      */     SigmetCommAttrDlgSaveMsgDlg(Shell parShell) throws VizException {
/*  733 */       super();
/*      */     }
/*      */ 
/*      */     public HashMap<String, Object> getAttrFromDlg() {
/*  737 */       HashMap attr = null;
/*  738 */       return attr;
/*      */     }
/*      */ 
/*      */     public void createButtonsForButtonBar(Composite parent)
/*      */     {
/*  743 */       ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/*  744 */       ((GridLayout)parent.getLayout()).marginHeight = 3;
/*      */ 
/*  746 */       createButton(parent, 0, "Save", true);
/*  747 */       createButton(parent, 1, 
/*  748 */         IDialogConstants.CANCEL_LABEL, false);
/*      */ 
/*  750 */       getButton(0).setLayoutData(
/*  751 */         new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*  752 */       getButton(1).setLayoutData(
/*  753 */         new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*      */     }
/*      */ 
/*      */     public void enableButtons()
/*      */     {
/*  758 */       getButton(1).setEnabled(true);
/*  759 */       getButton(0).setEnabled(true);
/*      */     }
/*      */ 
/*      */     public void cancelPressed()
/*      */     {
/*  764 */       setReturnCode(1);
/*  765 */       close();
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/*  771 */       String dataURI = storeActivity();
/*      */ 
/*  775 */       if (dataURI != null) {
/*      */         try {
/*  777 */           StorageUtils.storeDerivedProduct(dataURI, 
/*  778 */             this.txtSave.getText(), "TEXT", this.txtInfo.getText());
/*      */         } catch (PgenStorageException e) {
/*  780 */           StorageUtils.showError(e);
/*      */         }
/*      */       }
/*      */ 
/*  784 */       setReturnCode(0);
/*  785 */       close();
/*  786 */       SigmetCommAttrDlg.this.drawingLayer.removeSelected();
/*  787 */       SigmetCommAttrDlg.this.close();
/*  788 */       PgenUtil.setSelectingMode();
/*      */     }
/*      */ 
/*      */     private String storeActivity()
/*      */     {
/*  795 */       Layer defaultLayer = new Layer();
/*  796 */       defaultLayer.addElement(SigmetCommAttrDlg.this.drawingLayer
/*  797 */         .getSelectedDE());
/*  798 */       ArrayList layerList = new ArrayList();
/*  799 */       layerList.add(defaultLayer);
/*      */ 
/*  801 */       String forecaster = System.getProperty("user.name");
/*  802 */       ProductTime refTime = new ProductTime();
/*      */ 
/*  804 */       Product defaultProduct = new Product(
/*  805 */         SigmetCommAttrDlg.this.pgenType, 
/*  806 */         SigmetCommAttrDlg.this.pgenType, forecaster, null, refTime, 
/*  807 */         layerList);
/*      */ 
/*  809 */       defaultProduct.setOutputFile(SigmetCommAttrDlg.this.drawingLayer
/*  810 */         .buildActivityLabel(defaultProduct));
/*  811 */       defaultProduct.setCenter(PgenUtil.getCurrentOffice());
/*      */       try
/*      */       {
/*  814 */         dataURI = StorageUtils.storeProduct(defaultProduct);
/*      */       }
/*      */       catch (PgenStorageException e)
/*      */       {
/*      */         String dataURI;
/*  816 */         StorageUtils.showError(e);
/*  817 */         return null;
/*      */       }
/*      */       String dataURI;
/*  819 */       return dataURI;
/*      */     }
/*      */ 
/*      */     public void setAttrForDlg(IAttribute ia)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Control createDialogArea(Composite parent)
/*      */     {
/*  829 */       Composite top = (Composite)super.createDialogArea(parent);
/*      */ 
/*  831 */       GridLayout mainLayout = new GridLayout(3, false);
/*  832 */       mainLayout.marginHeight = 3;
/*  833 */       mainLayout.marginWidth = 3;
/*  834 */       top.setLayout(mainLayout);
/*      */ 
/*  836 */       getShell().setText("SIGMET Save");
/*      */ 
/*  838 */       this.txtInfo = new Text(top, 2122);
/*      */ 
/*  840 */       this.txtInfo.setFont(SigmetCommAttrDlg.txtfont);
/*  841 */       GridData gData = new GridData(512, 300);
/*  842 */       gData.horizontalSpan = 3;
/*  843 */       this.txtInfo.setLayoutData(gData);
/*  844 */       this.txtInfo.setText(getFileContent());
/*      */ 
/*  846 */       this.txtSave = new Text(top, 2056);
/*  847 */       this.txtSave.setLayoutData(new GridData(4, 16777216, true, 
/*  848 */         false, 3, 1));
/*  849 */       this.txtSave.setText(getFileName());
/*      */ 
/*  851 */       return top;
/*      */     }
/*      */ 
/*      */     private String getFileContent() {
/*  855 */       String s = "From " + 
/*  856 */         SigmetCommAttrDlg.this.getEditableAttrFromLine();
/*  857 */       if ((s == null) || (s.contains("null"))) {
/*  858 */         s = "";
/*      */       }
/*  860 */       if (("NCON_SIGMET".equalsIgnoreCase(SigmetCommAttrDlg.this.pgenType)) || 
/*  862 */         ("AIRM_SIGMET"
/*  862 */         .equalsIgnoreCase(SigmetCommAttrDlg.this.pgenType)) || 
/*  864 */         ("OUTL_SIGMET"
/*  864 */         .equalsIgnoreCase(SigmetCommAttrDlg.this.pgenType))) {
/*  865 */         return s.toUpperCase();
/*      */       }
/*  867 */       return SigmetCommAttrDlg.this.relatedState + "\n" + s.toUpperCase();
/*      */     }
/*      */ 
/*      */     private String getFileName() {
/*  871 */       String s = "";
/*  872 */       if ("CONV_SIGMET".equalsIgnoreCase(SigmetCommAttrDlg.this.pgenType)) {
/*  873 */         s = SigmetCommAttrDlg.this.getAbstractSigmet().getTopText();
/*      */       }
/*  875 */       else if ("OUTL_SIGMET"
/*  875 */         .equalsIgnoreCase(SigmetCommAttrDlg.this.pgenType))
/*  876 */         s = SigmetCommAttrDlg.this.getAbstractSigmet().getTopText() + 
/*  877 */           "O";
/*      */       else {
/*  879 */         s = SigmetCommAttrDlg.this.editableAttrId + "_" + 
/*  880 */           SigmetCommAttrDlg.this.editableAttrSequence;
/*      */       }
/*  882 */       if ((s == null) || (s.contains("null")))
/*  883 */         s = "";
/*      */       else
/*  885 */         s = s + ".from";
/*  886 */       return s;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.SigmetCommAttrDlg
 * JD-Core Version:    0.6.2
 */