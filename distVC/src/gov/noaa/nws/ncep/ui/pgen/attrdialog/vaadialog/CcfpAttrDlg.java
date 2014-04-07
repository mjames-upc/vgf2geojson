/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.AbstractSigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Ccfp;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.CcfpInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.ICcfp;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.ILabeledLine;
/*      */ import java.awt.Color;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import org.eclipse.jface.dialogs.IDialogConstants;
/*      */ import org.eclipse.swt.graphics.Point;
/*      */ import org.eclipse.swt.graphics.Rectangle;
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
/*      */ 
/*      */ public class CcfpAttrDlg extends AttrDlg
/*      */   implements ICcfp
/*      */ {
/*   72 */   private static CcfpAttrDlg INSTANCE = null;
/*   73 */   private static String mouseHandlerName = null;
/*      */   private ILabeledLine ccfpTool;
/*   77 */   private Sigmet asig = null;
/*      */   public static final String AREA = "Area";
/*      */   public static final String LINE = "Line";
/*      */   public static final String LINE_MED = "LineMed";
/*      */   public static final String LINE_SEPERATER = ":::";
/*   82 */   private String lineType = "";
/*      */   private static final String WIDTH = "10.00";
/*   84 */   private String width = "10.00";
/*      */ 
/*   86 */   private static final Color PURPLE = new Color(145, 44, 238);
/*   87 */   private static final Color LIGHT_BLUE = new Color(30, 144, 255);
/*      */ 
/*   89 */   private static final String[] ITEMS_CVRG = { "75-100%", "40-74%", "25-39%" };
/*   90 */   private static final String[] ITEMS_TOPS = { "400+", "350-390", "300-340", "250-290" };
/*   91 */   private static final String[] ITEMS_CONF = { "50-100%", "25-49%" };
/*   92 */   private static final String[] ITEMS_GWTH = { "↑", "NC", "↓" };
/*   93 */   private static final String[] ITEMS_SPD = { "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60" };
/*   94 */   private static final String[] ITEMS_DIR = SigmetInfo.DIRECT_ARRAY;
/*      */ 
/*   96 */   private Color[] colors = { LIGHT_BLUE, PURPLE };
/*      */   private Group top_3;
/*      */   private Button btnArea;
/*      */   private Button btnLine;
/*      */   private Button btnLineMed;
/*  102 */   protected Composite top = null;
/*      */   private Label lblCvrg;
/*      */   private Label lblTops;
/*      */   private Label lblConf;
/*      */   private Label lblGwth;
/*      */   private Label lblSpd;
/*      */   private Label lblDir;
/*      */   private Combo cmbCvrg;
/*      */   private Combo cmbTops;
/*      */   private Combo cmbConf;
/*      */   private Combo cmbGwth;
/*      */   private Combo cmbSpd;
/*      */   private Combo cmbDir;
/*      */   private Combo cmbIssTime;
/*      */   private Combo cmbVaTime;
/*      */   private static final int LAYOUT_WIDTH = 7;
/*  119 */   private static boolean NotFirstOpen = false;
/*      */ 
/*  121 */   private boolean withExpandedArea = false; private boolean copiedToSigmet = false;
/*      */   private Control detailsArea;
/*      */   private Point cachedWindowSize;
/*  125 */   private HashMap<String, Control> attrControlMap = new HashMap();
/*  126 */   private HashMap<String, Button[]> attrButtonMap = new HashMap();
/*      */ 
/*  128 */   private String editableAttrArea = "";
/*  129 */   private String editableAttrFromLine = "";
/*  130 */   private String editableAttrId = "EAST";
/*  131 */   private String editableAttrSequence = "";
/*      */ 
/*  134 */   private String ccfpIssueTime = "";
/*      */ 
/*  136 */   private String ccfpValidTime = "";
/*      */ 
/*  138 */   private String ccfpCvrg = ITEMS_CVRG[0];
/*      */ 
/*  140 */   private String ccfpTops = ITEMS_TOPS[0];
/*      */ 
/*  142 */   private String ccfpConf = ITEMS_CONF[0];
/*      */ 
/*  144 */   private String ccfpGrwt = ITEMS_GWTH[0];
/*      */ 
/*  146 */   private String ccfpSpd = ITEMS_SPD[0];
/*      */ 
/*  148 */   private String ccfpDir = ITEMS_DIR[0];
/*      */ 
/*      */   protected CcfpAttrDlg(Shell parShell) throws VizException {
/*  151 */     super(parShell);
/*      */   }
/*      */ 
/*      */   public static CcfpAttrDlg getInstance(Shell parShell) {
/*  155 */     if (INSTANCE == null) {
/*      */       try {
/*  157 */         INSTANCE = new CcfpAttrDlg(parShell);
/*      */       } catch (VizException e) {
/*  159 */         e.printStackTrace();
/*      */       }
/*      */     }
/*  162 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public String getCcfpLineType()
/*      */   {
/*  167 */     if ("LineMed".equalsIgnoreCase(this.lineType)) {
/*  168 */       return "LINE_DASHED_4";
/*      */     }
/*  170 */     return "LINE_SOLID";
/*      */   }
/*      */ 
/*      */   public FillPatternList.FillPattern getFillPattern()
/*      */   {
/*  175 */     if ((this.cmbCvrg == null) || (this.cmbCvrg.isDisposed())) {
/*  176 */       return FillPatternList.FillPattern.FILL_PATTERN_1;
/*      */     }
/*  178 */     this.ccfpCvrg = this.cmbCvrg.getText().trim();
/*      */ 
/*  180 */     if ((this.ccfpCvrg == null) || (this.ccfpCvrg.length() == 0)) {
/*  181 */       return FillPatternList.FillPattern.FILL_PATTERN_1;
/*      */     }
/*  183 */     if (ITEMS_CVRG[0].equals(this.ccfpCvrg))
/*  184 */       return FillPatternList.FillPattern.FILL_PATTERN_5;
/*  185 */     if (ITEMS_CVRG[1].equals(this.ccfpCvrg)) {
/*  186 */       return FillPatternList.FillPattern.FILL_PATTERN_3;
/*      */     }
/*  188 */     return FillPatternList.FillPattern.FILL_PATTERN_1;
/*      */   }
/*      */ 
/*      */   public boolean close()
/*      */   {
/*  197 */     NotFirstOpen = false;
/*      */ 
/*  199 */     if (getShell() != null) {
/*  200 */       Rectangle bounds = getShell().getBounds();
/*  201 */       this.shellLocation = new Point(bounds.x, bounds.y);
/*      */     }
/*  203 */     return super.close();
/*      */   }
/*      */ 
/*      */   public Boolean isFilled()
/*      */   {
/*  208 */     return Boolean.valueOf(true);
/*      */   }
/*      */ 
/*      */   public void createButtonsForButtonBar(Composite parent)
/*      */   {
/*  214 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/*  215 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*      */ 
/*  217 */     createButton(parent, 20091229, "Format...", false);
/*  218 */     createButton(parent, 20091021, "Apply", false);
/*  219 */     createButton(parent, 1, IDialogConstants.CANCEL_LABEL, false);
/*      */ 
/*  222 */     getButton(20091229).addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event e) {
/*  225 */         CcfpTimeDlg ct = null;
/*      */         try {
/*  227 */           ct = CcfpTimeDlg.getInstance(CcfpAttrDlg.this.getParentShell());
/*      */         } catch (Exception ee) {
/*  229 */           System.out.println(ee.getMessage());
/*      */         }
/*  231 */         if (ct != null) ct.open();
/*      */       }
/*      */     });
/*  237 */     getButton(20091021).addListener(13, new Listener()
/*      */     {
/*      */       public void handleEvent(Event e) {
/*  240 */         CcfpAttrDlg.this.okPressed2();
/*      */       }
/*      */     });
/*  243 */     getButton(20091229).setEnabled(false);
/*  244 */     getButton(20091021).setEnabled(false);
/*  245 */     getButton(1).setEnabled(false);
/*      */ 
/*  247 */     getButton(20091229).setLayoutData(new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*  248 */     getButton(20091021).setLayoutData(new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*  249 */     getButton(1).setLayoutData(new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*      */   }
/*      */ 
/*      */   public void enableButtons()
/*      */   {
/*  261 */     getButton(20091229).setEnabled(true);
/*  262 */     getButton(20091021).setEnabled(true);
/*  263 */     getButton(1).setEnabled(true);
/*      */   }
/*      */ 
/*      */   public AbstractSigmet getAbstractSigmet() {
/*  267 */     return this.asig;
/*      */   }
/*      */ 
/*      */   public void setAbstractSigmet(DrawableElement de) {
/*  271 */     setAttrForDlg(de);
/*      */   }
/*      */ 
/*      */   public void copyEditableAttrToAbstractSigmet(AbstractSigmet ba)
/*      */   {
/*  276 */     ba.setColors(getColors());
/*      */ 
/*  278 */     ba.setType(getLineType());
/*  279 */     ba.setWidth(getWidth());
/*      */ 
/*  281 */     Sigmet sig = (Sigmet)ba;
/*  282 */     sig.setEditableAttrStartTime(this.ccfpIssueTime);
/*  283 */     sig.setEditableAttrEndTime(this.ccfpValidTime);
/*  284 */     sig.setEditableAttrPhenom(this.ccfpCvrg);
/*  285 */     sig.setEditableAttrPhenom2(this.ccfpTops);
/*  286 */     sig.setEditableAttrPhenomLat(this.ccfpConf);
/*  287 */     sig.setEditableAttrPhenomLon(this.ccfpGrwt);
/*  288 */     sig.setEditableAttrPhenomSpeed(this.ccfpSpd);
/*  289 */     sig.setEditableAttrPhenomDirection(this.ccfpDir);
/*      */ 
/*  291 */     if (ITEMS_CVRG[0].equals(this.ccfpCvrg))
/*  292 */       sig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_5);
/*  293 */     else if (ITEMS_CVRG[1].equals(this.ccfpCvrg))
/*  294 */       sig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_3);
/*      */     else {
/*  296 */       sig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_1);
/*      */     }
/*  298 */     this.copiedToSigmet = true;
/*      */   }
/*      */ 
/*      */   public Color[] getColors()
/*      */   {
/*  305 */     if (!"Area".equalsIgnoreCase(this.lineType)) {
/*  306 */       return new Color[] { PURPLE };
/*      */     }
/*      */ 
/*  309 */     if ((this.cmbConf == null) || (this.cmbConf.isDisposed())) {
/*  310 */       return this.colors;
/*      */     }
/*  312 */     this.ccfpConf = this.cmbConf.getText().trim();
/*  313 */     if (this.ccfpConf.contains(ITEMS_CONF[1])) {
/*  314 */       return new Color[] { PURPLE };
/*      */     }
/*  316 */     return new Color[] { LIGHT_BLUE };
/*      */   }
/*      */ 
/*      */   private void setColor(Color clr)
/*      */   {
/*  324 */     this.colors[0] = clr;
/*      */   }
/*      */ 
/*      */   public String getLineType()
/*      */   {
/*  329 */     return this.lineType;
/*      */   }
/*      */ 
/*      */   public void setLineType(String lType) {
/*  333 */     this.lineType = lType;
/*      */   }
/*      */ 
/*      */   public String getSideOfLine() {
/*  337 */     return "";
/*      */   }
/*      */ 
/*      */   public void setSideOfLine(String lineSideString)
/*      */   {
/*      */   }
/*      */ 
/*      */   public String getWidth() {
/*  345 */     return this.width;
/*      */   }
/*      */ 
/*      */   public void setWidth(String widthString) {
/*  349 */     this.width = widthString;
/*      */   }
/*      */ 
/*      */   private double stringToDouble(String s) {
/*  353 */     double dValue = 10.0D;
/*      */     try {
/*  355 */       dValue = Double.parseDouble(s);
/*  356 */       dValue = (dValue < 0.0D) || (dValue > 5000.0D) ? 10.0D : dValue;
/*      */     } catch (NumberFormatException localNumberFormatException) {
/*      */     }
/*      */     finally {
/*  360 */       return dValue;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  367 */     this.top = ((Composite)super.createDialogArea(parent));
/*      */ 
/*  369 */     GridLayout mainLayout = new GridLayout(7, false);
/*  370 */     mainLayout.marginHeight = 7;
/*  371 */     mainLayout.marginWidth = 7;
/*  372 */     this.top.setLayout(mainLayout);
/*  373 */     getShell().setText("Collaborative Convective");
/*      */ 
/*  375 */     this.btnArea = new Button(this.top, 16);
/*  376 */     this.btnArea.setSelection(true);
/*  377 */     this.btnArea.setText("Area");
/*  378 */     if ((mouseHandlerName == null) || (mouseHandlerName.contains("PgenLabeledLineDrawingHandler"))) {
/*  379 */       setLineType("Area");
/*      */     }
/*  381 */     fillSpaces(this.top, 16384, 2, false);
/*      */ 
/*  383 */     this.btnLine = new Button(this.top, 16);
/*  384 */     this.btnLine.setText("Line");
/*      */ 
/*  386 */     fillSpaces(this.top, 16384, 2, false);
/*      */ 
/*  388 */     this.btnLineMed = new Button(this.top, 16);
/*  389 */     this.btnLineMed.setText("Line(Med)  ");
/*      */ 
/*  391 */     this.attrButtonMap.put("lineType", new Button[] { this.btnArea, this.btnLine, this.btnLineMed });
/*      */ 
/*  394 */     addBtnListeners();
/*      */ 
/*  396 */     if ((this.asig == null) && ((this.lineType == null) || (this.lineType.isEmpty()))) {
/*  397 */       setLineType("Area");
/*      */     }
/*      */ 
/*  400 */     String lt = getLineType();
/*  401 */     if ((lt != null) && (lt.equalsIgnoreCase("Area"))) {
/*  402 */       createAreaInfo(this.top);
/*      */     }
/*      */ 
/*  405 */     init();
/*      */ 
/*  407 */     setMouseHandlerName(null);
/*  408 */     return this.top;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrArea()
/*      */   {
/*  413 */     return this.editableAttrArea;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrArea(String editableAttrArea) {
/*  417 */     this.editableAttrArea = editableAttrArea;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrFromLine() {
/*  421 */     return this.editableAttrFromLine;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrFromLine(String editableAttrFromLine) {
/*  425 */     this.editableAttrFromLine = editableAttrFromLine;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrId() {
/*  429 */     return this.editableAttrId;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrId(String editableAttrId) {
/*  433 */     this.editableAttrId = editableAttrId;
/*      */   }
/*      */ 
/*      */   public String getEditableAttrSequence() {
/*  437 */     return this.editableAttrSequence;
/*      */   }
/*      */ 
/*      */   public void setEditableAttrSequence(String editableAttrSequence) {
/*  441 */     this.editableAttrSequence = editableAttrSequence;
/*      */   }
/*      */ 
/*      */   public void saveApplyPressed()
/*      */   {
/*  447 */     ArrayList adcList = null;
/*  448 */     ArrayList newList = new ArrayList();
/*      */ 
/*  450 */     if (this.drawingLayer != null)
/*  451 */       adcList = (ArrayList)this.drawingLayer.getAllSelected();
/*      */     ArrayList oldList;
/*  454 */     if ((adcList != null) && (!adcList.isEmpty()))
/*      */     {
/*  456 */       Sigmet newEl = null;
/*      */ 
/*  458 */       for (AbstractDrawableComponent adc : adcList)
/*      */       {
/*  460 */         Sigmet el = (Sigmet)adc.getPrimaryDE();
/*  461 */         if (el != null) {
/*  462 */           newEl = (Sigmet)el.copy();
/*      */ 
/*  464 */           attrUpdate();
/*      */ 
/*  466 */           copyEditableAttrToAbstractSigmet(newEl);
/*      */ 
/*  468 */           setAbstractSigmet(newEl);
/*  469 */           newList.add(newEl);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  475 */       oldList = new ArrayList(adcList);
/*  476 */       this.drawingLayer.replaceElements(oldList, newList);
/*      */     }
/*      */ 
/*  479 */     this.drawingLayer.removeSelected();
/*  480 */     for (AbstractDrawableComponent adc : newList) {
/*  481 */       this.drawingLayer.addSelected(adc);
/*      */     }
/*      */ 
/*  484 */     if (this.mapEditor != null)
/*  485 */       this.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   private void attrUpdate() {
/*  489 */     if ((this.cmbConf == null) || (this.cmbConf.isDisposed())) return;
/*  490 */     this.ccfpConf = this.cmbConf.getText().trim();
/*  491 */     this.ccfpCvrg = this.cmbCvrg.getText().trim();
/*  492 */     this.ccfpDir = this.cmbDir.getText().trim();
/*  493 */     this.ccfpGrwt = this.cmbGwth.getText().trim();
/*      */ 
/*  495 */     this.ccfpSpd = this.cmbSpd.getText().trim();
/*  496 */     this.ccfpTops = this.cmbTops.getText().trim();
/*      */   }
/*      */ 
/*      */   public void setMouseHandlerName(String mhName)
/*      */   {
/*  503 */     mouseHandlerName = mhName;
/*      */   }
/*      */ 
/*      */   static Color getDefaultColor(String pType) {
/*  507 */     return LIGHT_BLUE;
/*      */   }
/*      */ 
/*      */   private void fillSpaces(Composite gp, int dir, int num, boolean empty)
/*      */   {
/*  514 */     for (int i = 0; i < num; i++) {
/*  515 */       Label lbl = new Label(gp, dir);
/*  516 */       lbl.setText(empty ? "" : " ");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setEnabled(Group g, boolean flag)
/*      */   {
/*  522 */     if (g == null) return;
/*  523 */     for (Control c : g.getChildren())
/*  524 */       if (c != null) c.setEnabled(flag);
/*      */   }
/*      */ 
/*      */   public int open()
/*      */   {
/*  531 */     if (getShell() == null) {
/*  532 */       create();
/*      */     }
/*  534 */     if (this.shellLocation == null)
/*  535 */       getShell().setLocation(getShell().getParent().getLocation());
/*      */     else {
/*  537 */       getShell().setLocation(this.shellLocation);
/*      */     }
/*      */ 
/*  541 */     int i = super.open();
/*  542 */     if ((!"Area".equalsIgnoreCase(this.lineType)) && (this.lineType != null) && (this.lineType.length() == 0));
/*  545 */     Point shellSize = getShell().getSize();
/*  546 */     shellSize.x += 20;
/*  547 */     getShell().setSize(shellSize);
/*  548 */     return i;
/*      */   }
/*      */ 
/*      */   public String getCcfpIssueTime()
/*      */   {
/*  553 */     return this.ccfpIssueTime;
/*      */   }
/*      */ 
/*      */   public void setCcfpIssueTime(String ccfpIssueTime) {
/*  557 */     this.ccfpIssueTime = ccfpIssueTime;
/*      */   }
/*      */ 
/*      */   public String getCcfpValidTime() {
/*  561 */     return this.ccfpValidTime;
/*      */   }
/*      */ 
/*      */   public void setCcfpValidTime(String ccfpValidTime) {
/*  565 */     this.ccfpValidTime = ccfpValidTime;
/*      */   }
/*      */ 
/*      */   public String getCcfpCvrg() {
/*  569 */     return this.ccfpCvrg;
/*      */   }
/*      */ 
/*      */   public void setCcfpCvrg(String ccfpCvrg) {
/*  573 */     this.ccfpCvrg = ccfpCvrg;
/*      */   }
/*      */ 
/*      */   public String getCcfpTops() {
/*  577 */     return this.ccfpTops;
/*      */   }
/*      */ 
/*      */   public void setCcfpTops(String ccfpTops) {
/*  581 */     this.ccfpTops = ccfpTops;
/*      */   }
/*      */ 
/*      */   public String getCcfpConf() {
/*  585 */     return this.ccfpConf;
/*      */   }
/*      */ 
/*      */   public void setCcfpConf(String ccfpConf) {
/*  589 */     this.ccfpConf = ccfpConf;
/*      */   }
/*      */ 
/*      */   public String getCcfpGrwt() {
/*  593 */     return this.ccfpGrwt;
/*      */   }
/*      */ 
/*      */   public void setCcfpGrwt(String ccfpGrwt) {
/*  597 */     this.ccfpGrwt = ccfpGrwt;
/*      */   }
/*      */ 
/*      */   public String getCcfpSpd() {
/*  601 */     return this.ccfpSpd;
/*      */   }
/*      */ 
/*      */   public void setCcfpSpd(String ccfpSpd) {
/*  605 */     this.ccfpSpd = ccfpSpd;
/*      */   }
/*      */ 
/*      */   public String getCcfpDir() {
/*  609 */     return this.ccfpDir;
/*      */   }
/*      */ 
/*      */   public void setCcfpDir(String ccfpDir) {
/*  613 */     this.ccfpDir = ccfpDir;
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute attr)
/*      */   {
/*  618 */     if (attr == null) return;
/*      */ 
/*  620 */     this.asig = ((Sigmet)attr);
/*      */ 
/*  622 */     Color clr = attr.getColors()[0];
/*  623 */     if (clr != null) setColor(clr);
/*      */ 
/*  638 */     this.lineType = this.asig.getType();
/*  639 */     this.ccfpIssueTime = this.asig.getEditableAttrStartTime();
/*  640 */     this.ccfpValidTime = this.asig.getEditableAttrEndTime();
/*  641 */     this.ccfpCvrg = this.asig.getEditableAttrPhenom();
/*  642 */     this.ccfpTops = this.asig.getEditableAttrPhenom2();
/*  643 */     this.ccfpConf = this.asig.getEditableAttrPhenomLat();
/*  644 */     this.ccfpGrwt = this.asig.getEditableAttrPhenomLon();
/*  645 */     this.ccfpSpd = this.asig.getEditableAttrPhenomSpeed();
/*  646 */     this.ccfpDir = this.asig.getEditableAttrPhenomDirection();
/*  647 */     init();
/*      */   }
/*      */ 
/*      */   private void addBtnListeners()
/*      */   {
/*  654 */     this.btnArea.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  656 */         if (!CcfpAttrDlg.this.btnArea.getSelection()) {
/*  657 */           return;
/*      */         }
/*  659 */         if ((CcfpAttrDlg.this.top_3 == null) || (CcfpAttrDlg.this.top_3.isDisposed())) {
/*  660 */           CcfpAttrDlg.NotFirstOpen = true;
/*  661 */           CcfpAttrDlg.this.createAreaInfo(CcfpAttrDlg.this.top);
/*      */         }
/*      */ 
/*  664 */         CcfpAttrDlg.this.setLineType("Area");
/*      */ 
/*  666 */         if ((CcfpAttrDlg.this.cmbConf == null) || (CcfpAttrDlg.this.cmbConf.isDisposed())) return;
/*      */ 
/*  668 */         CcfpAttrDlg.this.ccfpConf = CcfpAttrDlg.this.cmbConf.getText().trim();
/*  669 */         if (CcfpAttrDlg.this.ccfpConf.contains(CcfpAttrDlg.ITEMS_CONF[1]))
/*  670 */           CcfpAttrDlg.this.setColor(CcfpAttrDlg.PURPLE);
/*      */         else
/*  672 */           CcfpAttrDlg.this.setColor(CcfpAttrDlg.LIGHT_BLUE);
/*      */       }
/*      */     });
/*  678 */     this.btnLine.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  680 */         if (!CcfpAttrDlg.this.btnLine.getSelection()) {
/*  681 */           return;
/*      */         }
/*  683 */         if (CcfpAttrDlg.this.top_3 != null) {
/*  684 */           CcfpAttrDlg.this.disposeAreaInfo();
/*      */         }
/*  686 */         CcfpAttrDlg.this.setLineType("Line");
/*      */       }
/*      */     });
/*  694 */     this.btnLineMed.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  696 */         if (!CcfpAttrDlg.this.btnLineMed.getSelection()) {
/*  697 */           return;
/*      */         }
/*  699 */         if (CcfpAttrDlg.this.top_3 != null) {
/*  700 */           CcfpAttrDlg.this.disposeAreaInfo();
/*      */         }
/*  702 */         CcfpAttrDlg.this.setLineType("LineMed");
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void addCmbListeners()
/*      */   {
/*  712 */     this.cmbCvrg.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  714 */         CcfpAttrDlg.this.ccfpCvrg = CcfpAttrDlg.this.cmbCvrg.getText().trim();
/*      */ 
/*  716 */         if (CcfpAttrDlg.this.asig == null) return;
/*      */ 
/*  718 */         if (CcfpAttrDlg.ITEMS_CVRG[0].equals(CcfpAttrDlg.this.ccfpCvrg))
/*  719 */           CcfpAttrDlg.this.asig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_5);
/*  720 */         else if (CcfpAttrDlg.ITEMS_CVRG[1].equals(CcfpAttrDlg.this.ccfpCvrg))
/*  721 */           CcfpAttrDlg.this.asig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_3);
/*      */         else
/*  723 */           CcfpAttrDlg.this.asig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_1);
/*      */       }
/*      */     });
/*  727 */     this.cmbTops.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  729 */         CcfpAttrDlg.this.ccfpTops = CcfpAttrDlg.this.cmbTops.getText().trim();
/*      */       }
/*      */     });
/*  734 */     this.cmbConf.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  736 */         CcfpAttrDlg.this.ccfpConf = CcfpAttrDlg.this.cmbConf.getText().trim();
/*  737 */         if (CcfpAttrDlg.this.ccfpConf.contains(CcfpAttrDlg.ITEMS_CONF[1]))
/*  738 */           CcfpAttrDlg.this.setColor(CcfpAttrDlg.PURPLE);
/*      */         else
/*  740 */           CcfpAttrDlg.this.setColor(CcfpAttrDlg.LIGHT_BLUE);
/*      */       }
/*      */     });
/*  745 */     this.cmbGwth.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  747 */         CcfpAttrDlg.this.ccfpGrwt = CcfpAttrDlg.this.cmbGwth.getText().trim();
/*      */       }
/*      */     });
/*  751 */     this.cmbSpd.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  753 */         CcfpAttrDlg.this.ccfpSpd = CcfpAttrDlg.this.cmbSpd.getText().trim();
/*      */       }
/*      */     });
/*  756 */     this.cmbDir.addListener(13, new Listener() {
/*      */       public void handleEvent(Event e) {
/*  758 */         CcfpAttrDlg.this.ccfpDir = CcfpAttrDlg.this.cmbDir.getText().trim();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void init()
/*      */   {
/*  768 */     if (this.asig == null) return;
/*      */ 
/*  770 */     Button[] btns = (Button[])this.attrButtonMap.get("lineType");
/*      */ 
/*  772 */     if (btns == null) return;
/*      */ 
/*  774 */     for (Button btn : btns) {
/*  775 */       if ((btn == null) || (btn.isDisposed())) {
/*  776 */         return;
/*      */       }
/*      */     }
/*  779 */     if (this.lineType != null) {
/*  780 */       if (this.lineType.equals("Area")) {
/*  781 */         btns[0].setSelection(true);
/*  782 */         btns[1].setSelection(false);
/*  783 */         btns[2].setSelection(false);
/*  784 */       } else if (this.lineType.equals("Line")) {
/*  785 */         btns[0].setSelection(false);
/*  786 */         btns[1].setSelection(true);
/*  787 */         btns[2].setSelection(false);
/*      */       }
/*  791 */       else if (this.lineType.equals("LineMed")) {
/*  792 */         btns[0].setSelection(false);
/*  793 */         btns[1].setSelection(false);
/*  794 */         btns[2].setSelection(true);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  800 */     initCombos();
/*      */   }
/*      */ 
/*      */   private void createAreaInfo(Composite comp)
/*      */   {
/*  806 */     this.top_3 = new Group(comp, 16384);
/*  807 */     this.top_3.setLayoutData(new GridData(4, 16777216, true, true, 7, 1));
/*  808 */     this.top_3.setLayout(new GridLayout(7, false));
/*      */ 
/*  812 */     this.lblCvrg = new Label(this.top_3, 16384);
/*  813 */     this.lblCvrg.setText("Coverage: ");
/*      */ 
/*  815 */     fillSpaces(this.top_3, 16384, 2, true);
/*      */ 
/*  817 */     this.cmbCvrg = new Combo(this.top_3, 16392);
/*  818 */     this.cmbCvrg.setItems(ITEMS_CVRG);
/*  819 */     this.cmbCvrg.select(0);
/*      */ 
/*  821 */     fillSpaces(this.top_3, 16384, 3, true);
/*      */ 
/*  825 */     this.lblTops = new Label(this.top_3, 16384);
/*  826 */     this.lblTops.setText("Echo Tops:");
/*      */ 
/*  828 */     fillSpaces(this.top_3, 16384, 2, true);
/*      */ 
/*  830 */     this.cmbTops = new Combo(this.top_3, 16392);
/*  831 */     this.cmbTops.setItems(ITEMS_TOPS);
/*  832 */     this.cmbTops.select(0);
/*      */ 
/*  834 */     fillSpaces(this.top_3, 16384, 3, true);
/*      */ 
/*  838 */     this.lblConf = new Label(this.top_3, 16384);
/*  839 */     this.lblConf.setText("Confidence:");
/*      */ 
/*  841 */     fillSpaces(this.top_3, 16384, 2, true);
/*      */ 
/*  843 */     this.cmbConf = new Combo(this.top_3, 16392);
/*  844 */     this.cmbConf.setItems(ITEMS_CONF);
/*  845 */     this.cmbConf.select(0);
/*      */ 
/*  847 */     fillSpaces(this.top_3, 16384, 3, true);
/*      */ 
/*  851 */     this.lblGwth = new Label(this.top_3, 16384);
/*  852 */     this.lblGwth.setText("Growth:");
/*      */ 
/*  854 */     fillSpaces(this.top_3, 16384, 2, true);
/*      */ 
/*  856 */     this.cmbGwth = new Combo(this.top_3, 16392);
/*  857 */     this.cmbGwth.setItems(ITEMS_GWTH);
/*  858 */     this.cmbGwth.select(0);
/*      */ 
/*  860 */     fillSpaces(this.top_3, 16384, 3, true);
/*      */ 
/*  864 */     this.lblSpd = new Label(this.top_3, 16384);
/*  865 */     this.lblSpd.setText("Speed(kts):");
/*      */ 
/*  867 */     fillSpaces(this.top_3, 16384, 2, true);
/*      */ 
/*  869 */     this.cmbSpd = new Combo(this.top_3, 16392);
/*  870 */     this.cmbSpd.setItems(ITEMS_SPD);
/*  871 */     this.cmbSpd.select(0);
/*      */ 
/*  873 */     fillSpaces(this.top_3, 16384, 3, true);
/*      */ 
/*  877 */     this.lblDir = new Label(this.top_3, 16384);
/*  878 */     this.lblDir.setText("Direction:");
/*      */ 
/*  880 */     fillSpaces(this.top_3, 16384, 2, true);
/*      */ 
/*  882 */     this.cmbDir = new Combo(this.top_3, 16392);
/*  883 */     this.cmbDir.setItems(ITEMS_DIR);
/*  884 */     this.cmbDir.select(0);
/*      */ 
/*  886 */     fillSpaces(this.top_3, 16384, 3, true);
/*      */ 
/*  888 */     addCmbListeners();
/*      */ 
/*  890 */     if (NotFirstOpen) {
/*  891 */       getShell().pack();
/*  892 */       getShell().layout();
/*      */     }
/*      */ 
/*  895 */     NotFirstOpen = true;
/*      */ 
/*  897 */     initCombos();
/*      */   }
/*      */ 
/*      */   private void disposeAreaInfo()
/*      */   {
/*  902 */     if ((this.top_3 != null) && (!this.top_3.isDisposed()))
/*      */     {
/*  904 */       Control[] wids = this.top_3.getChildren();
/*      */ 
/*  906 */       for (int kk = 0; (wids != null) && (kk < wids.length); kk++) {
/*  907 */         wids[kk].dispose();
/*      */       }
/*      */ 
/*  910 */       this.top_3.dispose();
/*  911 */       this.top_3 = null;
/*      */     }
/*      */ 
/*  915 */     getShell().pack();
/*  916 */     getShell().layout();
/*      */   }
/*      */ 
/*      */   private void initCombos()
/*      */   {
/*  922 */     if ((this.cmbCvrg != null) && (!this.cmbCvrg.isDisposed()) && (this.ccfpCvrg != null) && (this.ccfpCvrg.length() > 0))
/*  923 */       this.cmbCvrg.setText(this.ccfpCvrg);
/*  924 */     if ((this.cmbTops != null) && (!this.cmbTops.isDisposed()) && (this.ccfpTops != null) && (this.ccfpTops.length() > 0))
/*  925 */       this.cmbTops.setText(this.ccfpTops);
/*  926 */     if ((this.cmbConf != null) && (!this.cmbConf.isDisposed()) && (this.ccfpConf != null) && (this.ccfpConf.length() > 0))
/*  927 */       this.cmbConf.setText(this.ccfpConf);
/*  928 */     if ((this.cmbGwth != null) && (!this.cmbGwth.isDisposed()) && (this.ccfpGrwt != null) && (this.ccfpGrwt.length() > 0))
/*  929 */       this.cmbGwth.setText(this.ccfpGrwt);
/*  930 */     if ((this.cmbSpd != null) && (!this.cmbSpd.isDisposed()) && (this.ccfpSpd != null) && (this.ccfpSpd.length() > 0))
/*  931 */       this.cmbSpd.setText(this.ccfpSpd);
/*  932 */     if ((this.cmbDir != null) && (!this.cmbDir.isDisposed()) && (this.ccfpDir != null) && (this.ccfpDir.length() > 0))
/*  933 */       this.cmbDir.setText(this.ccfpDir);
/*      */   }
/*      */ 
/*      */   public Coordinate[] getLinePoints()
/*      */   {
/*  939 */     return null;
/*      */   }
/*      */ 
/*      */   public String getPatternName()
/*      */   {
/*  945 */     return null;
/*      */   }
/*      */ 
/*      */   public int getSmoothFactor()
/*      */   {
/*  951 */     return 0;
/*      */   }
/*      */ 
/*      */   public Boolean isClosedLine()
/*      */   {
/*  957 */     return null;
/*      */   }
/*      */ 
/*      */   public void copyEditableAttrToAbstractSigmet2(AbstractSigmet ba, LabeledLine ll)
/*      */   {
/*  962 */     ba.setColors(getColors());
/*      */ 
/*  964 */     ba.setType(getLineType());
/*  965 */     ba.setWidth(getWidth());
/*      */ 
/*  967 */     Sigmet sig = (Sigmet)ba;
/*  968 */     sig.setEditableAttrStartTime(this.ccfpIssueTime);
/*  969 */     sig.setEditableAttrEndTime(this.ccfpValidTime);
/*  970 */     sig.setEditableAttrPhenom(this.ccfpCvrg);
/*  971 */     sig.setEditableAttrPhenom2(this.ccfpTops);
/*  972 */     sig.setEditableAttrPhenomLat(this.ccfpConf);
/*  973 */     sig.setEditableAttrPhenomLon(this.ccfpGrwt);
/*  974 */     sig.setEditableAttrPhenomSpeed(this.ccfpSpd);
/*  975 */     sig.setEditableAttrPhenomDirection(this.ccfpDir);
/*      */ 
/*  977 */     if (ITEMS_CVRG[0].equals(this.ccfpCvrg))
/*  978 */       sig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_5);
/*  979 */     else if (ITEMS_CVRG[1].equals(this.ccfpCvrg))
/*  980 */       sig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_3);
/*      */     else {
/*  982 */       sig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_1);
/*      */     }
/*      */ 
/*  986 */     StringBuilder sb = new StringBuilder("CCFP_SIGMET");
/*      */ 
/*  988 */     sb.append(":::");
/*  989 */     sb.append(sig.getEditableAttrPhenomSpeed()).append(":::");
/*  990 */     sb.append(sig.getEditableAttrPhenomDirection()).append(":::");
/*  991 */     sb.append(sig.getStartTime()).append(":::");
/*  992 */     sb.append(sig.getEndtime()).append(":::");
/*      */ 
/*  994 */     sb.append(sig.getEditableAttrPhenom()).append(":::");
/*  995 */     sb.append(sig.getEditableAttrPhenom2()).append(":::");
/*  996 */     sb.append(sig.getEditableAttrPhenomLat()).append(":::");
/*  997 */     sb.append(sig.getEditableAttrPhenomLon()).append(":::");
/*  998 */     sb.append(this.lineType);
/*      */ 
/* 1000 */     ((Ccfp)ll).setCollectionName(sb.toString());
/*      */   }
/*      */ 
/*      */   public void setLine(Line sig)
/*      */   {
/* 1005 */     sig.setColors(getColors());
/*      */ 
/* 1007 */     if (ITEMS_CVRG[0].equals(this.ccfpCvrg))
/* 1008 */       sig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_5);
/* 1009 */     else if (ITEMS_CVRG[1].equals(this.ccfpCvrg))
/* 1010 */       sig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_3);
/*      */     else
/* 1012 */       sig.setFillPattern(FillPatternList.FillPattern.FILL_PATTERN_1);
/*      */   }
/*      */ 
/*      */   public void setText(Text txt)
/*      */   {
/* 1018 */     txt.setColors(getColors());
/*      */   }
/*      */ 
/*      */   public void setCcfpDrawingTool(ILabeledLine pgenTool)
/*      */   {
/* 1023 */     this.ccfpTool = pgenTool;
/*      */   }
/*      */ 
/*      */   public void okPressed2() {
/* 1027 */     if ((this.ccfpTool != null) && (this.ccfpTool.getLabeledLine() != null))
/*      */     {
/* 1029 */       String origLineType = this.asig != null ? this.asig.getType() : "";
/* 1030 */       String newLineType = getLineType();
/*      */ 
/* 1032 */       if (!newLineType.equals(origLineType)) {
/* 1033 */         convertType();
/*      */       }
/*      */ 
/* 1036 */       LabeledLine ll = this.ccfpTool.getLabeledLine();
/*      */ 
/* 1038 */       Line pln = (Line)ll.getPrimaryDE();
/*      */ 
/* 1041 */       if (!pln.isClosedLine().booleanValue()) {
/* 1042 */         return;
/*      */       }
/*      */ 
/* 1045 */       if ((pln.isClosedLine().booleanValue()) && (("Line".equals(this.lineType)) || ("LineMed".equals(this.lineType)))) {
/* 1046 */         return;
/*      */       }
/* 1048 */       LabeledLine newll = ll.copy();
/* 1049 */       attrUpdate();
/* 1050 */       Sigmet sig = ((Ccfp)newll).getSigmet();
/* 1051 */       copyEditableAttrToAbstractSigmet2(sig, newll);
/* 1052 */       setAbstractSigmet(sig);
/*      */ 
/* 1054 */       Iterator it = newll.createDEIterator();
/* 1055 */       while (it.hasNext()) {
/* 1056 */         DrawableElement de = (DrawableElement)it.next();
/*      */ 
/* 1058 */         if ((de instanceof Text))
/*      */         {
/* 1060 */           de.setColors(getColors());
/* 1061 */           Text txt = (Text)de; txt.setText(CcfpInfo.getCcfpTxt2(sig));
/* 1062 */         } else if ((de instanceof Line))
/*      */         {
/* 1064 */           if ("LINE_SOLID".equals(de.getPgenType())) {
/* 1065 */             setLine((Line)de);
/*      */           }
/*      */ 
/* 1068 */           if (!"POINTED_ARROW".equals(de.getPgenType())) {
/* 1069 */             de.setColors(getColors());
/*      */           }
/*      */         }
/*      */       }
/* 1073 */       this.drawingLayer.replaceElement(ll, newll);
/* 1074 */       this.ccfpTool.setLabeledLine(newll);
/*      */ 
/* 1077 */       this.drawingLayer.removeSelected();
/* 1078 */       Iterator iterator = newll.createDEIterator();
/* 1079 */       while (iterator.hasNext()) {
/* 1080 */         this.drawingLayer.addSelected((AbstractDrawableComponent)iterator.next());
/*      */       }
/*      */ 
/* 1084 */       if ((newll instanceof Ccfp)) {
/* 1085 */         ((Ccfp)newll).moveText2Last();
/*      */       }
/* 1087 */       this.mapEditor.refresh();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setType(String type)
/*      */   {
/* 1094 */     if (type != null)
/* 1095 */       setLineType(type);
/*      */   }
/*      */ 
/*      */   public boolean isAreaType() {
/* 1099 */     return "Area".equalsIgnoreCase(this.lineType);
/*      */   }
/*      */ 
/*      */   public void grayOutUnselectedBtns() {
/* 1103 */     if ((mouseHandlerName == null) || (mouseHandlerName.contains("PgenLabeledLineDrawingHandler"))) {
/* 1104 */       return;
/*      */     }
/* 1106 */     if ("Area".equalsIgnoreCase(this.lineType)) {
/* 1107 */       this.btnLine.setEnabled(false);
/* 1108 */       this.btnLineMed.setEnabled(false);
/* 1109 */       return;
/*      */     }
/*      */ 
/* 1112 */     if ("Line".equalsIgnoreCase(this.lineType)) {
/* 1113 */       this.btnLineMed.setEnabled(false);
/* 1114 */       this.btnArea.setEnabled(false);
/* 1115 */       return;
/*      */     }
/*      */ 
/* 1118 */     if ("LineMed".equalsIgnoreCase(this.lineType)) {
/* 1119 */       this.btnArea.setEnabled(false);
/* 1120 */       this.btnLine.setEnabled(false);
/* 1121 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void convertType() {
/* 1126 */     if ((this.ccfpTool != null) && (this.ccfpTool.getLabeledLine() != null)) {
/* 1127 */       LabeledLine ll = this.ccfpTool.getLabeledLine();
/* 1128 */       LabeledLine newll = ll.copy();
/* 1129 */       attrUpdate();
/*      */ 
/* 1131 */       Sigmet sig = ((Ccfp)newll).getSigmet();
/* 1132 */       copyEditableAttrToAbstractSigmet2(sig, newll);
/* 1133 */       setAbstractSigmet(sig);
/*      */ 
/* 1135 */       newll = createLabeledLine(newll);
/*      */ 
/* 1137 */       this.drawingLayer.replaceElement(ll, newll);
/* 1138 */       this.ccfpTool.setLabeledLine(newll);
/*      */ 
/* 1141 */       this.drawingLayer.removeSelected();
/* 1142 */       Iterator iterator = newll.createDEIterator();
/* 1143 */       while (iterator.hasNext()) {
/* 1144 */         this.drawingLayer.addSelected((AbstractDrawableComponent)iterator.next());
/*      */       }
/* 1146 */       this.mapEditor.refresh();
/*      */     }
/*      */   }
/*      */ 
/*      */   public LabeledLine createLabeledLine(LabeledLine ll)
/*      */   {
/* 1153 */     CcfpAttrDlg ccdlg = this;
/*      */ 
/* 1155 */     List newPoints = ll.getPoints();
/*      */ 
/* 1158 */     Iterator it = ll.createDEIterator();
/* 1159 */     while (it.hasNext()) {
/* 1160 */       DrawableElement de = (DrawableElement)it.next();
/*      */ 
/* 1162 */       if ((de instanceof Text))
/* 1163 */         newPoints.remove(newPoints.size() - 1);
/* 1164 */       else if ((de instanceof Line))
/*      */       {
/* 1166 */         if ("POINTED_ARROW".equals(de.getPgenType())) {
/* 1167 */           newPoints.remove(newPoints.size() - 1);
/* 1168 */           newPoints.remove(newPoints.size() - 1);
/*      */         }
/*      */       }
/*      */     }
/* 1172 */     Sigmet sig = new Sigmet();
/* 1173 */     sig.setType(ccdlg.getCcfpLineType());
/*      */ 
/* 1175 */     Line ln = new Line();
/* 1176 */     ln.update(ccdlg);
/* 1177 */     ln.setLinePoints(newPoints);
/* 1178 */     ln.setPgenCategory("Lines");
/* 1179 */     ln.setPgenType(ccdlg.getCcfpLineType());
/* 1180 */     ln.setColors(ccdlg.getColors());
/* 1181 */     ln.setClosed(Boolean.valueOf(ccdlg.isAreaType()));
/* 1182 */     ln.setFilled(Boolean.valueOf(ccdlg.isAreaType()));
/* 1183 */     if (!ccdlg.isAreaType())
/* 1184 */       ln.setLineWidth(3.0F);
/*      */     else {
/* 1186 */       ln.setLineWidth(2.0F);
/*      */     }
/* 1188 */     LabeledLine newll = new Ccfp("CCFP_SIGMET");
/* 1189 */     newll.setPgenCategory("SIGMET");
/* 1190 */     newll.setPgenType("CCFP_SIGMET");
/* 1191 */     newll.setParent(ll.getParent());
/*      */ 
/* 1193 */     ((Ccfp)newll).setSigmet(sig);
/* 1194 */     ((Ccfp)newll).setAreaLine(ln);
/* 1195 */     ((Ccfp)newll).setAttributes(ccdlg);
/*      */ 
/* 1197 */     newll.addLine(ln);
/* 1198 */     return newll;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.CcfpAttrDlg
 * JD-Core Version:    0.6.2
 */