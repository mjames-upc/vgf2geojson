/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.AbstractSigmet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.IVaaCloud;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.eclipse.jface.util.IPropertyChangeListener;
/*     */ import org.eclipse.jface.util.PropertyChangeEvent;
/*     */ import org.eclipse.swt.graphics.RGB;
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
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class VaaCloudDlg extends AttrDlg
/*     */   implements IVaaCloud
/*     */ {
/*     */   private static VaaCloudDlg INSTANCE;
/*  72 */   protected Composite top = null;
/*     */ 
/*  77 */   private Volcano volcano = null;
/*     */ 
/*  82 */   private AbstractSigmet vaCloud = null;
/*     */ 
/*  87 */   Button btnArea = null;
/*     */ 
/*  92 */   Button btnLine = null;
/*     */ 
/*  97 */   Button btnNotSeen = null;
/*     */ 
/* 102 */   Button btnFcst = null;
/*     */ 
/* 107 */   Text txtWidth = null;
/*     */ 
/* 112 */   Text txtFL = null;
/*     */ 
/* 117 */   Text txtFL2 = null;
/*     */ 
/* 122 */   Text txtDir = null;
/*     */ 
/* 127 */   Text txtSpd = null;
/*     */ 
/* 132 */   Combo comboFcst = null;
/*     */ 
/* 137 */   Combo comboFHR = null;
/*     */ 
/* 142 */   protected ColorButtonSelector cs = null;
/*     */ 
/* 147 */   private String type = "Area";
/*     */ 
/* 152 */   private boolean copiedToSigmet = false;
/*     */   private static final String SEPERATER = ":::";
/* 162 */   private String mouseHandlerName = null;
/*     */ 
/*     */   public VaaCloudDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 170 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static VaaCloudDlg getInstance(Shell parShell)
/*     */   {
/* 180 */     if (INSTANCE == null) {
/*     */       try
/*     */       {
/* 183 */         INSTANCE = new VaaCloudDlg(parShell);
/*     */       } catch (VizException e) {
/* 185 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 190 */     return INSTANCE;
/*     */   }
/*     */   public HashMap<String, Object> getAttrFromDlg() {
/* 193 */     return new HashMap();
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute ia)
/*     */   {
/* 201 */     if (AttrDlg.mouseHandlerName == null) return;
/* 202 */     this.vaCloud = ((AbstractSigmet)ia);
/* 203 */     this.type = this.vaCloud.getType();
/*     */ 
/* 205 */     Color clr = ia.getColors()[0];
/* 206 */     if (clr != null) {
/* 207 */       setColor(clr);
/*     */     }
/* 209 */     this.txtWidth.setText(this.vaCloud.getWidth());
/*     */ 
/* 211 */     if ((this.type.contains("Text")) && (!this.type.contains("WINDS"))) {
/* 212 */       this.comboFcst.select(VaaInfo.getFcstItemIndexFromTxt(this.type.split(":::")[1]));
/*     */     }
/*     */ 
/* 218 */     RadioBtnGroup rBtnGrp = 
/* 219 */       new RadioBtnGroup(new Button[] { this.btnArea, this.btnLine, this.btnNotSeen, this.btnFcst });
/*     */ 
/* 221 */     if (this.type.contains("Area"))
/* 222 */       rBtnGrp.enableBtn(this.btnArea, null, new Control[] { this.txtWidth, this.comboFcst });
/* 223 */     else if (this.type.contains("Line"))
/* 224 */       rBtnGrp.enableBtn(this.btnLine, new Control[] { this.txtWidth }, new Control[] { this.comboFcst });
/* 225 */     else if (this.type.contains("Text")) {
/* 226 */       if (this.type.contains("WINDS"))
/* 227 */         rBtnGrp.enableBtn(this.btnNotSeen, null, new Control[] { this.txtWidth, this.comboFcst });
/*     */       else {
/* 229 */         rBtnGrp.enableBtn(this.btnFcst, new Control[] { this.comboFcst }, new Control[] { this.txtWidth });
/*     */       }
/*     */     }
/* 232 */     setFhrFlDirSpdTxt(this.vaCloud);
/* 233 */     AttrDlg.mouseHandlerName = null;
/*     */ 
/* 236 */     if ("Volcano".equalsIgnoreCase(this.drawingLayer.getActiveProduct().getType())) {
/* 237 */       this.cs.setColorValue(getLayerColor());
/* 238 */       this.comboFHR.setText(this.drawingLayer.getActiveLayer().getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 248 */     applyPressed();
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 257 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/* 258 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*     */ 
/* 262 */     if (this.mouseHandlerName == null) return;
/*     */ 
/* 264 */     createButton(parent, 0, "Apply", true);
/* 265 */     createButton(parent, 1, "Cancel", true);
/*     */ 
/* 267 */     getButton(0).setEnabled(true);
/* 268 */     getButton(1).setEnabled(true);
/*     */ 
/* 272 */     this.mouseHandlerName = null;
/*     */ 
/* 274 */     getButton(1).setLayoutData(new GridData(ctrlBtnWidth, ctrlBtnHeight));
/* 275 */     getButton(0).setLayoutData(new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*     */   }
/*     */ 
/*     */   public void enableButtons()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 292 */     this.top = ((Composite)super.createDialogArea(parent));
/* 293 */     getShell().setText("VAA Ash Cloud Create/Edit");
/*     */ 
/* 295 */     GridLayout mainLayout = new GridLayout(8, false);
/* 296 */     mainLayout.marginHeight = 3;
/* 297 */     mainLayout.marginWidth = 3;
/* 298 */     this.top.setLayout(mainLayout);
/*     */ 
/* 300 */     Group top1 = new Group(this.top, 16384);
/* 301 */     top1.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/* 302 */     top1.setLayout(new GridLayout(10, false));
/* 303 */     createArea1(top1);
/*     */ 
/* 305 */     Group top2 = new Group(this.top, 16384);
/* 306 */     top2.setLayoutData(new GridData(4, 16777216, true, true, 11, 1));
/* 307 */     top2.setLayout(new GridLayout(12, false));
/* 308 */     createArea2(top2);
/*     */ 
/* 310 */     init();
/* 311 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void createArea1(Group top)
/*     */   {
/* 320 */     this.btnArea = new Button(top, 16);
/* 321 */     this.btnArea.setText("Area");
/* 322 */     this.btnArea.setSelection(true);
/* 323 */     this.type = "Area";
/*     */ 
/* 325 */     this.btnLine = new Button(top, 16);
/* 326 */     this.btnLine.setText("Line");
/*     */ 
/* 328 */     Label lblWidth = new Label(top, 16384);
/* 329 */     lblWidth.setText("Width:");
/*     */ 
/* 333 */     this.txtWidth = new Text(top, 2048);
/* 334 */     this.txtWidth.setEnabled(false);
/*     */ 
/* 336 */     this.btnNotSeen = new Button(top, 16);
/* 337 */     this.btnNotSeen.setText("NotSeen");
/*     */ 
/* 340 */     this.btnFcst = new Button(top, 16);
/* 341 */     this.btnFcst.setText("others-FCST");
/*     */ 
/* 343 */     this.comboFcst = new Combo(top, 8);
/* 344 */     this.comboFcst.setItems((String[])VaaInfo.VAA_INFO_OTHERSFCST_MAP.get("dialog"));
/* 345 */     this.comboFcst.select(0);
/* 346 */     this.comboFcst.setEnabled(false);
/*     */ 
/* 348 */     this.comboFcst.addListener(13, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 350 */         VaaCloudDlg.this.type = VaaCloudDlg.this.getFcstTxt();
/*     */       }
/*     */     });
/* 354 */     this.btnArea.addListener(13, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 356 */         VaaCloudDlg.this.type = "Area";
/* 357 */         VaaCloudDlg.this.radioBtnEnable(Boolean.valueOf(false), Boolean.valueOf(false));
/*     */       }
/*     */     });
/* 361 */     this.btnLine.addListener(13, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 363 */         VaaCloudDlg.this.type = "Line:::ESOL";
/* 364 */         VaaCloudDlg.this.radioBtnEnable(Boolean.valueOf(true), Boolean.valueOf(false));
/*     */       }
/*     */     });
/* 368 */     this.btnNotSeen.addListener(13, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 370 */         VaaCloudDlg.this.type = VaaCloudDlg.this.getNotSeenTxt();
/* 371 */         VaaCloudDlg.this.radioBtnEnable(Boolean.valueOf(false), Boolean.valueOf(false));
/*     */       }
/*     */     });
/* 375 */     this.btnFcst.addListener(13, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 377 */         VaaCloudDlg.this.type = VaaCloudDlg.this.getFcstTxt();
/* 378 */         VaaCloudDlg.this.radioBtnEnable(Boolean.valueOf(false), Boolean.valueOf(true));
/*     */       }
/*     */     });
/* 387 */     this.cs = new ColorButtonSelector(top, 50, 20);
/* 388 */     this.cs.setColorValue(getLayerColor());
/* 389 */     this.cs.addListener(new IPropertyChangeListener()
/*     */     {
/*     */       public void propertyChange(PropertyChangeEvent event)
/*     */       {
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createArea2(Group top)
/*     */   {
/* 401 */     Label lblFHR = new Label(top, 16384);
/* 402 */     lblFHR.setText("FHR ");
/*     */ 
/* 404 */     this.comboFHR = new Combo(top, 8);
/* 405 */     this.comboFHR.setItems(new String[] { "F00", "F06", "F12", "F18" });
/* 406 */     this.comboFHR.select(VaaInfo.getLayerIdx() - 1);
/* 407 */     this.comboFHR.addListener(13, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 409 */         if (VaaCloudDlg.this.isTxtType())
/* 410 */           VaaCloudDlg.this.type = VaaCloudDlg.this.getNotSeenTxt();
/*     */       }
/*     */     });
/* 414 */     Label lblDummy = new Label(top, 16384); lblDummy.setText("   ");
/*     */ 
/* 416 */     Label lblFL = new Label(top, 16384);
/* 417 */     lblFL.setText("FL      ");
/*     */ 
/* 419 */     this.txtFL = new Text(top, 2048);
/* 420 */     this.txtFL.setText("SFC          ");
/* 421 */     this.txtFL.addListener(24, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 423 */         if (VaaCloudDlg.this.isTxtType())
/* 424 */           VaaCloudDlg.this.type = VaaCloudDlg.this.getDisplayTxt();
/*     */       }
/*     */     });
/* 428 */     Label lblFLHyphen = new Label(top, 16384);
/* 429 */     lblFLHyphen.setText("-");
/*     */ 
/* 431 */     this.txtFL2 = new Text(top, 2048);
/* 432 */     this.txtFL2.addListener(24, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 434 */         if (VaaCloudDlg.this.isTxtType())
/* 435 */           VaaCloudDlg.this.type = VaaCloudDlg.this.getDisplayTxt();
/*     */       }
/*     */     });
/* 439 */     Label lblDir = new Label(top, 16384);
/* 440 */     lblDir.setText("DIR ");
/*     */ 
/* 442 */     this.txtDir = new Text(top, 2048);
/* 443 */     this.txtDir.addListener(24, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 445 */         if (VaaCloudDlg.this.isTxtType())
/* 446 */           VaaCloudDlg.this.type = VaaCloudDlg.this.getDisplayTxt();
/*     */       }
/*     */     });
/* 450 */     Label lblSpd = new Label(top, 16384);
/* 451 */     lblSpd.setText("SPD ");
/*     */ 
/* 453 */     this.txtSpd = new Text(top, 2048);
/*     */ 
/* 455 */     this.txtSpd.addListener(24, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 457 */         if (VaaCloudDlg.this.isTxtType())
/* 458 */           VaaCloudDlg.this.type = VaaCloudDlg.this.getDisplayTxt();
/*     */       }
/*     */     });
/* 462 */     Label lblKts = new Label(top, 2048);
/* 463 */     lblKts.setText(" kts");
/*     */   }
/*     */ 
/*     */   public String getLineType() {
/* 467 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String getCloudWidth()
/*     */   {
/* 475 */     String dWidth = "25";
/*     */ 
/* 477 */     if ((this.txtWidth == null) || (this.txtWidth.isDisposed())) {
/* 478 */       return dWidth;
/*     */     }
/* 480 */     String w = this.txtWidth.getText();
/* 481 */     double wi = 0.0D;
/*     */     try
/*     */     {
/* 484 */       wi = Double.parseDouble(w);
/*     */     } catch (Exception e) {
/* 486 */       this.txtWidth.setText(dWidth);
/*     */     }
/*     */ 
/* 489 */     if ((w == null) || (w.length() == 0) || (wi < 10.0D)) {
/* 490 */       this.txtWidth.setText(dWidth);
/*     */     }
/*     */ 
/* 493 */     return this.txtWidth.getText();
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 503 */     Color[] colors = new Color[2];
/* 504 */     colors[0] = new Color(this.cs.getColorValue().red, 
/* 505 */       this.cs.getColorValue().green, this.cs.getColorValue().blue);
/* 506 */     colors[1] = Color.green;
/*     */ 
/* 508 */     return colors;
/*     */   }
/*     */ 
/*     */   public void setColor(Color clr) {
/* 512 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   private String getDisplayTxt()
/*     */   {
/* 520 */     if (this.btnNotSeen.getSelection())
/* 521 */       return getNotSeenTxt();
/* 522 */     if (this.btnFcst.getSelection()) {
/* 523 */       return getFcstTxt();
/*     */     }
/* 525 */     return "";
/*     */   }
/*     */ 
/*     */   private String getNotSeenTxt()
/*     */   {
/* 533 */     StringBuilder sb = new StringBuilder("Text").append(":::");
/*     */ 
/* 535 */     if ("F00".equals(this.comboFHR.getText().trim())) {
/* 536 */       sb.append("VA NOT IDENTIFIABLE FROM SATELLITE DATA").append(":::");
/*     */     }
/*     */ 
/* 539 */     sb.append("WINDS ");
/*     */ 
/* 541 */     String sFL = this.txtFL.getText().trim(); String sFL2 = this.txtFL2.getText().trim();
/* 542 */     if ("SFC".equals(sFL))
/* 543 */       sb.append("SFC");
/*     */     else {
/* 545 */       sb.append("FL").append(sFL);
/*     */     }
/* 547 */     sb.append("/FL").append(sFL2 == null ? "" : sFL2);
/*     */ 
/* 549 */     sb.append(" ");
/*     */ 
/* 551 */     String sDir = this.txtDir.getText().trim(); String sSpd = this.txtSpd.getText().trim();
/* 552 */     sb.append(sDir == null ? "" : sDir.toUpperCase());
/* 553 */     sb.append("/").append(sSpd == null ? "" : sSpd);
/*     */ 
/* 555 */     return "KT";
/*     */   }
/*     */ 
/*     */   private String getFcstTxt()
/*     */   {
/* 563 */     Map map = new HashMap();
/*     */ 
/* 566 */     String[] fcstNames = (String[])VaaInfo.VAA_INFO_OTHERSFCST_MAP.get("dialog");
/* 567 */     String[] fcstValues = (String[])VaaInfo.VAA_INFO_OTHERSFCST_MAP.get("display");
/*     */ 
/* 569 */     for (int i = 0; i < fcstNames.length; i++) {
/* 570 */       map.put(fcstNames[i], fcstValues[i]);
/*     */     }
/*     */ 
/* 573 */     StringBuilder sb = new StringBuilder("Text").append(":::");
/*     */ 
/* 575 */     String s = this.comboFcst.getText().trim(); String sFL = this.txtFL.getText().trim(); String sFL2 = this.txtFL2.getText().trim();
/* 576 */     String fl = ("SFC".equals(sFL) ? "SFC" : new StringBuilder("FL").append(sFL).toString()) + "/FL" + sFL2 + " ";
/*     */ 
/* 578 */     if ("ASH DSIPTG".equals(s)) {
/* 579 */       String word = (String)map.get(s);
/* 580 */       int index = word.indexOf("ASH");
/*     */ 
/* 582 */       return fl + word.substring(index);
/*     */     }
/* 584 */     if (s.contains("WITH FL")) {
/* 585 */       sb.append(fl);
/* 586 */       return ((String)map.get(s)).substring(7);
/*     */     }
/* 588 */     return (String)map.get(s);
/*     */   }
/*     */ 
/*     */   public void setSigmet(DrawableElement sigmet)
/*     */   {
/* 595 */     this.vaCloud = ((AbstractSigmet)sigmet);
/*     */   }
/*     */ 
/*     */   private void applyPressed()
/*     */   {
/* 609 */     ArrayList adcList = null;
/* 610 */     ArrayList newList = new ArrayList();
/* 611 */     if (this.drawingLayer != null) {
/* 612 */       adcList = (ArrayList)this.drawingLayer.getAllSelected();
/*     */     }
/* 614 */     if ((adcList != null) && (!adcList.isEmpty())) {
/* 615 */       for (AbstractDrawableComponent adc : adcList) {
/* 616 */         Sigmet el = (Sigmet)adc.getPrimaryDE();
/* 617 */         if (el != null) {
/* 618 */           Sigmet newEl = (Sigmet)el.copy();
/*     */ 
/* 620 */           attrUpdate();
/*     */ 
/* 622 */           copyEditableAttrToAbstractSigmet(newEl);
/*     */ 
/* 624 */           newList.add(newEl);
/*     */         }
/*     */       }
/* 627 */       ArrayList oldList = new ArrayList(adcList);
/* 628 */       this.drawingLayer.replaceElements(oldList, newList);
/*     */     }
/* 630 */     this.drawingLayer.removeSelected();
/* 631 */     for (AbstractDrawableComponent adc : newList) {
/* 632 */       this.drawingLayer.addSelected(adc);
/*     */     }
/* 634 */     if (this.mapEditor != null) this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   private void copyEditableAttrToAbstractSigmet(AbstractSigmet ba)
/*     */   {
/* 644 */     ba.setColors(getColors());
/* 645 */     ((Sigmet)ba).setEditableAttrFreeText(getFhrFlDirSpdTxt());
/*     */ 
/* 653 */     ba.setType(getLineType());
/* 654 */     ba.setWidth(Double.parseDouble(getCloudWidth()));
/*     */ 
/* 656 */     this.copiedToSigmet = true;
/*     */   }
/*     */ 
/*     */   private void attrUpdate()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void setBtnObservers()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void init()
/*     */   {
/* 676 */     setBtnObservers();
/*     */   }
/*     */ 
/*     */   private void radioBtnEnable(Boolean txtW, Boolean comboF)
/*     */   {
/* 684 */     if (txtW != null) {
/* 685 */       this.txtWidth.setEnabled(txtW.booleanValue());
/* 686 */       this.txtWidth.setText("25");
/*     */     }
/* 688 */     if (comboF != null)
/* 689 */       this.comboFcst.setEnabled(comboF.booleanValue());
/*     */   }
/*     */ 
/*     */   public String getFhrFlDirSpdTxt()
/*     */   {
/* 696 */     StringBuilder sb = new StringBuilder();
/*     */ 
/* 698 */     if (!this.comboFHR.isDisposed()) {
/* 699 */       String fhr = this.comboFHR.getText().trim();
/* 700 */       String fl = this.txtFL.getText();
/* 701 */       String fl2 = this.txtFL2.getText();
/* 702 */       String dir = this.txtDir.getText();
/* 703 */       String spd = this.txtSpd.getText();
/*     */ 
/* 705 */       sb.append(fhr).append(":::");
/* 706 */       sb.append((fl == null) || (fl.length() == 0) ? " " : fl.trim().toUpperCase()).append(":::");
/* 707 */       sb.append((fl2 == null) || (fl2.length() == 0) ? " " : fl2.trim().toUpperCase()).append(":::");
/* 708 */       sb.append((dir == null) || (dir.length() == 0) ? " " : dir.trim().toUpperCase()).append(":::");
/* 709 */       sb.append((spd == null) || (spd.length() == 0) ? " " : spd.trim().toUpperCase());
/*     */     }
/*     */ 
/* 712 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private void setFhrFlDirSpdTxt(AbstractSigmet aSig)
/*     */   {
/* 720 */     String fromLine = ((Sigmet)aSig).getEditableAttrFreeText();
/* 721 */     if (fromLine == null) return;
/*     */ 
/* 723 */     String[] wArray = fromLine.split(":::");
/*     */ 
/* 725 */     if (wArray.length > 0) this.comboFHR.setText(wArray[0]);
/* 726 */     if (wArray.length > 1) this.txtFL.setText(wArray[1]);
/* 727 */     if (wArray.length > 2) this.txtFL2.setText(wArray[2]);
/* 728 */     if (wArray.length > 3) this.txtDir.setText(wArray[3]);
/* 729 */     if (wArray.length > 4) this.txtSpd.setText(wArray[4]);
/*     */   }
/*     */ 
/*     */   private boolean isTxtType()
/*     */   {
/* 738 */     return (this.btnNotSeen.getSelection()) || (this.btnFcst.getSelection());
/*     */   }
/*     */ 
/*     */   public Volcano getVolcano() {
/* 742 */     return this.volcano;
/*     */   }
/*     */ 
/*     */   public void setVolcano(Volcano volcano) {
/* 746 */     this.volcano = volcano;
/*     */   }
/*     */ 
/*     */   public RGB getFHRColor(int fhr)
/*     */   {
/* 751 */     return new RGB(255, 255, 255);
/*     */   }
/*     */ 
/*     */   public RGB getLayerColor()
/*     */   {
/* 759 */     Color c = PgenSession.getInstance().getPgenResource().getActiveLayer().getColor();
/* 760 */     return new RGB(c.getRed(), c.getGreen(), c.getBlue());
/*     */   }
/*     */ 
/*     */   public void setMouseHandlerName(String mhName)
/*     */   {
/* 765 */     AttrDlg.mouseHandlerName = mhName;
/* 766 */     this.mouseHandlerName = mhName;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLinePoints()
/*     */   {
/* 772 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 778 */     return null;
/*     */   }
/*     */ 
/*     */   public int getSmoothFactor()
/*     */   {
/* 784 */     return 0;
/*     */   }
/*     */ 
/*     */   public Boolean isClosedLine()
/*     */   {
/* 790 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 796 */     return null;
/*     */   }
/*     */ 
/*     */   public FillPatternList.FillPattern getFillPattern()
/*     */   {
/* 802 */     return null;
/*     */   }
/*     */ 
/*     */   public double getWidth()
/*     */   {
/* 807 */     return Double.parseDouble(getCloudWidth());
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.VaaCloudDlg
 * JD-Core Version:    0.6.2
 */