/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.ITrack;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.ITrack.ExtraPointTimeDisplayOption;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.TrackPoint;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Track;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*      */ import java.awt.Color;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import org.eclipse.swt.events.ModifyEvent;
/*      */ import org.eclipse.swt.events.ModifyListener;
/*      */ import org.eclipse.swt.events.SelectionAdapter;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.graphics.RGB;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ 
/*      */ public class TrackAttrDlg extends AttrDlg
/*      */   implements ITrack
/*      */ {
/*   75 */   public static int[] FontSizeValue = { 10, 12, 14, 18, 24, 34 };
/*      */ 
/*   77 */   public static String[] FontName = { "Courier", "Nimbus Sans L", "Liberation Serif" };
/*      */ 
/*   79 */   public static String[] BoxName = { "Normal", "Boxed", "Blanked", "Outline" };
/*      */ 
/*   81 */   private final int DEFAULT_NUMBER_OF_TIMES = 5;
/*   82 */   private final int DEFAULT_HOUR_SHIFT_FOR_FIRST_TIME = 4;
/*   83 */   private final int DEFAULT_HOUR_SHIFT_BEYOND_FIRST_TIME = 1;
/*      */ 
/*   85 */   private static String[] IntervalTimeValues = { "00:15", "00:30", "01:00", "02:00", "06:00", 
/*   86 */     "12:00", "Other" };
/*      */ 
/*   87 */   private String previousIntervalTimeValue = "";
/*      */ 
/*   89 */   private static String[] UnitValues = { "kts", "kph", "mph" };
/*   90 */   private static String[] RoundTo = { " ", "5", "10" };
/*   91 */   private static String[] RoundDirTo = { " ", "1", "5" };
/*      */   private Text firstTimeText;
/*      */   private Text secondTimeText;
/*      */   private boolean setTimeButtonSelected;
/*      */   private Button frameTimeButton;
/*      */   private Button setTimeButton;
/*      */   private Text numberOfTimesText;
/*      */   private Text intervalText;
/*      */   private Text skipFactorText;
/*      */   private ITrack.ExtraPointTimeDisplayOption extraPointTimeDisplayOption;
/*      */   private ColorButtonSelector initialCS;
/*      */   private ColorButtonSelector extrapCS;
/*  109 */   private Text text = null;
/*      */   private Combo intervalCombo;
/*      */   private Combo unitCombo;
/*      */   private int unitComboSelectedIndex;
/*      */   private Button roundButton;
/*      */   private Combo roundCombo;
/*      */   private int roundComboSelectedIndex;
/*      */   private Button roundDirButton;
/*      */   private Combo roundDirCombo;
/*      */   private int roundDirComboSelectedIndex;
/*      */   private Combo fontSizeCombo;
/*      */   private int fontSizeComboSelectedIndex;
/*      */   private Combo fontNameCombo;
/*      */   private int fontNameComboSelectedIndex;
/*      */   private Combo fontStyleCombo;
/*      */   private int fontStyleComboSelectedIndex;
/*      */   private Button skipFactorButton;
/*      */   private Button showFirstLastButton;
/*      */   private Button onHourButton;
/*      */   private Button onHalfHourButton;
/*      */   private Calendar firstTimeCalendar;
/*      */   private Calendar secondTimeCalendar;
/*  137 */   private int numberOfTimes = 5;
/*      */   TrackExtrapPointInfoDlg trackExtrapPointInfoDlg;
/*  141 */   private static TrackAttrDlg INSTANCE = null;
/*      */ 
/*  143 */   public boolean isNewTrack = false;
/*      */ 
/*      */   private TrackAttrDlg(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  151 */     super(parShell);
/*      */   }
/*      */ 
/*      */   public static TrackAttrDlg getInstance(Shell parShell)
/*      */   {
/*  162 */     if (INSTANCE == null) {
/*      */       try {
/*  164 */         INSTANCE = new TrackAttrDlg(parShell);
/*      */       } catch (VizException e) {
/*  166 */         e.printStackTrace();
/*      */       }
/*      */     }
/*  169 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   public void okPressed()
/*      */   {
/*  174 */     ArrayList adcList = null;
/*  175 */     ArrayList newList = new ArrayList();
/*      */ 
/*  178 */     if (this.drawingLayer != null)
/*  179 */       adcList = (ArrayList)this.drawingLayer.getAllSelected();
/*      */     ArrayList oldList;
/*  182 */     if ((adcList != null) && (!adcList.isEmpty()))
/*      */     {
/*  184 */       Track newEl = null;
/*      */ 
/*  186 */       for (AbstractDrawableComponent adc : adcList)
/*      */       {
/*  188 */         Track el = (Track)adc.getPrimaryDE();
/*      */ 
/*  190 */         if (el != null)
/*      */         {
/*  192 */           newEl = el.copy();
/*      */ 
/*  194 */           newEl.update(this);
/*      */ 
/*  198 */           populateTrackExtrapPointInfoDlgWithNewTrackData(getTrackExtrapPointInfoDlg(), newEl, 
/*  199 */             this.unitComboSelectedIndex, this.roundComboSelectedIndex, this.roundDirComboSelectedIndex);
/*      */ 
/*  201 */           newList.add(newEl);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  206 */       if (newEl != null) {
/*  207 */         AttrSettings.getInstance().setSettings(newEl);
/*      */       }
/*      */ 
/*  210 */       oldList = new ArrayList(adcList);
/*  211 */       this.drawingLayer.replaceElements(oldList, newList);
/*      */     }
/*      */ 
/*  215 */     this.drawingLayer.removeSelected();
/*  216 */     for (AbstractDrawableComponent adc : newList) {
/*  217 */       this.drawingLayer.addSelected(adc);
/*      */     }
/*      */ 
/*  220 */     if (this.mapEditor != null)
/*  221 */       this.mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   public void cancelPressed()
/*      */   {
/*  227 */     if (this.trackExtrapPointInfoDlg != null) {
/*  228 */       this.trackExtrapPointInfoDlg.close();
/*  229 */       this.trackExtrapPointInfoDlg = null;
/*      */     }
/*  231 */     super.cancelPressed();
/*      */   }
/*      */ 
/*      */   public Control createDialogArea(Composite parent)
/*      */   {
/*  241 */     Composite top = (Composite)super.createDialogArea(parent);
/*      */ 
/*  244 */     GridLayout mainLayout = new GridLayout(2, false);
/*  245 */     mainLayout.marginHeight = 3;
/*  246 */     mainLayout.marginWidth = 3;
/*  247 */     top.setLayout(mainLayout);
/*      */ 
/*  250 */     initializeComponents(top);
/*      */ 
/*  252 */     return top;
/*      */   }
/*      */ 
/*      */   public void initializeTrackAttrDlg(Track track)
/*      */   {
/*  257 */     if (track == null) {
/*  258 */       return;
/*      */     }
/*      */ 
/*  262 */     if (track.isSetTimeButtonSelected()) {
/*  263 */       getSetTimeButton().setSelection(true);
/*  264 */       getFrameTimeButton().setSelection(false);
/*      */     } else {
/*  266 */       getFrameTimeButton().setSelection(true);
/*  267 */       getSetTimeButton().setSelection(false);
/*      */     }
/*      */ 
/*  272 */     if (this.isNewTrack) {
/*  273 */       String[] firstSecondTimeValueArray = getFirstSecondTimeInitialTimeValueForSetTimeButton();
/*  274 */       getFirstTimeText().setText(firstSecondTimeValueArray[0]);
/*  275 */       getSecondTimeText().setText(firstSecondTimeValueArray[1]);
/*      */     }
/*      */     else
/*      */     {
/*  279 */       getFirstTimeText().setText(getFirstOrSecondTimeStringValue(track.getFirstTimeCalendar(), true, track.getInitialPoints()));
/*  280 */       getSecondTimeText().setText(getFirstOrSecondTimeStringValue(track.getSecondTimeCalendar(), false, track.getInitialPoints()));
/*      */     }
/*      */ 
/*  287 */     if (track.getExtrapPoints() != null) {
/*  288 */       this.numberOfTimesText.setText(String.valueOf(track.getExtrapPoints().length));
/*      */     }
/*      */ 
/*  298 */     setIntervalTimeString(track.getIntervalTimeString());
/*      */ 
/*  305 */     Color initColor = track.getInitialColor();
/*  306 */     this.initialCS.setColorValue(new RGB(initColor.getRed(), initColor.getGreen(), initColor.getBlue()));
/*  307 */     Color extrapColor = track.getExtrapColor();
/*  308 */     this.extrapCS.setColorValue(new RGB(extrapColor.getRed(), extrapColor.getGreen(), extrapColor.getBlue()));
/*      */ 
/*  313 */     setExtraPointTimeDisplayOption(track.getExtraPointTimeDisplayOption());
/*  314 */     makeTimeDisplayOptionSelected(track.getExtraPointTimeDisplayOption(), track.getSkipFactorTextString());
/*      */ 
/*  319 */     getFontNameCombo().select(track.getFontNameComboSelectedIndex());
/*  320 */     getFontSizeCombo().select(track.getFontSizeComboSelectedIndex());
/*  321 */     getFontStyleCombo().select(track.getFontStyleComboSelectedIndex());
/*      */ 
/*  326 */     this.unitComboSelectedIndex = track.getUnitComboSelectedIndex();
/*  327 */     getUnitCombo().select(this.unitComboSelectedIndex);
/*      */ 
/*  329 */     this.roundComboSelectedIndex = track.getRoundComboSelectedIndex();
/*  330 */     getRoundCombo().select(this.roundComboSelectedIndex);
/*  331 */     if (this.roundComboSelectedIndex > 0)
/*  332 */       this.roundButton.setSelection(true);
/*      */     else {
/*  334 */       this.roundButton.setSelection(false);
/*      */     }
/*  336 */     this.roundDirComboSelectedIndex = track.getRoundDirComboSelectedIndex();
/*  337 */     getRoundDirCombo().select(this.roundDirComboSelectedIndex);
/*  338 */     if (this.roundDirComboSelectedIndex > 0)
/*  339 */       this.roundDirButton.setSelection(true);
/*      */     else
/*  341 */       this.roundDirButton.setSelection(false);
/*      */   }
/*      */ 
/*      */   private void populateTrackExtrapPointInfoDlgWithNewTrackData(TrackExtrapPointInfoDlg trackExtrapPointInfoDlgObject, Track newTrackObject, int unitComboSelectedIndex, int roundComboSelectedIndex, int roundDirComboSelectedIndex)
/*      */   {
/*  346 */     if ((trackExtrapPointInfoDlgObject != null) && (newTrackObject != null)) {
/*  347 */       trackExtrapPointInfoDlgObject.close();
/*      */ 
/*  349 */       trackExtrapPointInfoDlgObject.setBlockOnOpen(false);
/*  350 */       trackExtrapPointInfoDlgObject.open();
/*      */ 
/*  352 */       trackExtrapPointInfoDlgObject.setTrack(newTrackObject, unitComboSelectedIndex, roundComboSelectedIndex, roundDirComboSelectedIndex);
/*      */ 
/*  354 */       trackExtrapPointInfoDlgObject.setBlockOnOpen(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void makeTimeDisplayOptionSelected(ITrack.ExtraPointTimeDisplayOption extraPointTimeDisplayOption, String skipFactorTextString)
/*      */   {
/*  374 */     getSkipFactorButton().setSelection(false);
/*  375 */     this.skipFactorText.setText("");
/*  376 */     if (extraPointTimeDisplayOption == ITrack.ExtraPointTimeDisplayOption.ON_HALF_HOUR) {
/*  377 */       getOnHalfHourButton().setSelection(true);
/*  378 */     } else if (extraPointTimeDisplayOption == ITrack.ExtraPointTimeDisplayOption.ON_ONE_HOUR) {
/*  379 */       getOnHourButton().setSelection(true);
/*  380 */     } else if (extraPointTimeDisplayOption == ITrack.ExtraPointTimeDisplayOption.SHOW_FIRST_LAST) {
/*  381 */       getShowFirstLastButton().setSelection(true);
/*      */     } else {
/*  383 */       getSkipFactorButton().setSelection(true);
/*  384 */       this.skipFactorText.setText(skipFactorTextString);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initializeComponents(Composite topComposite)
/*      */   {
/*  393 */     this.isNewTrack = true;
/*      */ 
/*  395 */     int textWidth = 120;
/*  396 */     int textHeight = 15;
/*      */ 
/*  398 */     GridLayout childGridLayout = new GridLayout(2, false);
/*      */ 
/*  400 */     getShell().setText("Track Attributes");
/*      */ 
/*  406 */     Group timeRadioButtonGroup = new Group(topComposite, 0);
/*  407 */     timeRadioButtonGroup.setLayout(childGridLayout);
/*      */ 
/*  409 */     this.frameTimeButton = new Button(timeRadioButtonGroup, 16);
/*      */ 
/*  411 */     this.frameTimeButton.setText("Frame time");
/*      */ 
/*  414 */     this.setTimeButton = new Button(timeRadioButtonGroup, 16);
/*  415 */     this.setTimeButton.setText("Set Time");
/*      */ 
/*  418 */     Label emptyLabel = new Label(topComposite, 16384);
/*  419 */     emptyLabel.setText("   ");
/*      */ 
/*  424 */     String[] firstSecondTimeValueArray = getFirstSecondTimeInitialTimeValueForSetTimeButton();
/*  425 */     setFirstTimeText(createTextfieldWithLabel(topComposite, "First time:", 
/*  426 */       2052, textWidth, textHeight, true));
/*  427 */     getFirstTimeText().setText(firstSecondTimeValueArray[0]);
/*  428 */     getFirstTimeText().addModifyListener(new ModifyListener() {
/*      */       public void modifyText(ModifyEvent e) {
/*  430 */         Text txt = (Text)e.widget;
/*  431 */         Calendar cal = TrackAttrDlg.this.gempakTM2Calendar(txt.getText());
/*  432 */         if (cal != null) TrackAttrDlg.this.firstTimeCalendar = cal;
/*      */       }
/*      */     });
/*  438 */     setSecondTimeText(createTextfieldWithLabel(topComposite, "Second time:", 
/*  439 */       2052, textWidth, textHeight, true));
/*  440 */     getSecondTimeText().setText(firstSecondTimeValueArray[1]);
/*      */ 
/*  442 */     getSecondTimeText().addModifyListener(new ModifyListener() {
/*      */       public void modifyText(ModifyEvent e) {
/*  444 */         Text txt = (Text)e.widget;
/*  445 */         Calendar cal = TrackAttrDlg.this.gempakTM2Calendar(txt.getText());
/*  446 */         if (cal != null) TrackAttrDlg.this.secondTimeCalendar = cal;
/*      */       }
/*      */     });
/*  451 */     this.setTimeButton.setSelection(true);
/*  452 */     setSetTimeButtonSelected(true);
/*  453 */     frameAndSetTimeButtonSelectionListenerAction(this.frameTimeButton, getFirstTimeText(), 
/*  454 */       getSecondTimeText());
/*  455 */     frameAndSetTimeButtonSelectionListenerAction(this.setTimeButton, getFirstTimeText(), 
/*  456 */       getSecondTimeText());
/*      */ 
/*  463 */     getFirstTimeText().setEditable(true);
/*  464 */     getSecondTimeText().setEditable(true);
/*      */ 
/*  469 */     setNumberOfTimesText(createTextfieldWithLabel(topComposite, "Number of times:", 
/*  470 */       2052, textWidth / 3, textHeight, true));
/*  471 */     this.numberOfTimesText.setText(String.valueOf(this.numberOfTimes));
/*  472 */     this.numberOfTimesText.addModifyListener(new ModifyListener() {
/*      */       public void modifyText(ModifyEvent e) {
/*      */         try {
/*  475 */           TrackAttrDlg.this.numberOfTimes = Integer.parseInt(TrackAttrDlg.this.numberOfTimesText.getText());
/*      */         } catch (NumberFormatException nfe) {
/*  477 */           TrackAttrDlg.this.numberOfTimes = 5;
/*      */         }
/*      */       }
/*      */     });
/*  487 */     Label intervalLabel = new Label(topComposite, 16384);
/*  488 */     intervalLabel.setText("Interval:");
/*      */ 
/*  490 */     Group intervalRowGroup = new Group(topComposite, 0);
/*  491 */     intervalRowGroup.setLayout(childGridLayout);
/*      */ 
/*  493 */     this.intervalCombo = new Combo(intervalRowGroup, 12);
/*  494 */     for (String currentString : IntervalTimeValues) {
/*  495 */       this.intervalCombo.add(currentString);
/*      */     }
/*  497 */     this.intervalCombo.select(2);
/*  498 */     setPreviousIntervalTimeValue(this.intervalCombo.getText());
/*  499 */     this.intervalText = new Text(intervalRowGroup, 2052);
/*  500 */     this.intervalCombo.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  502 */         if (TrackAttrDlg.this.intervalCombo.getText().equalsIgnoreCase("Other")) {
/*  503 */           TrackAttrDlg.this.intervalText.setEditable(true);
/*  504 */           TrackAttrDlg.this.intervalText.setText(TrackAttrDlg.this.getPreviousIntervalTimeValue());
/*      */         } else {
/*  506 */           TrackAttrDlg.this.intervalText.setEditable(false);
/*  507 */           TrackAttrDlg.this.intervalText.setText("");
/*  508 */           TrackAttrDlg.this.setPreviousIntervalTimeValue(TrackAttrDlg.this.intervalCombo.getText());
/*      */         }
/*      */       }
/*      */     });
/*  514 */     this.intervalText.addModifyListener(new ModifyListener() {
/*      */       public void modifyText(ModifyEvent e) {
/*  516 */         if (!((Text)e.widget).getText().isEmpty());
/*      */       }
/*      */     });
/*  524 */     Label colorLabel = new Label(topComposite, 16384);
/*  525 */     colorLabel.setText("Initial Color:");
/*  526 */     this.initialCS = new ColorButtonSelector(topComposite);
/*  527 */     this.initialCS.setColorValue(new RGB(0, 0, 255));
/*      */ 
/*  529 */     colorLabel = new Label(topComposite, 16384);
/*  530 */     colorLabel.setText("Extra Color:");
/*  531 */     this.extrapCS = new ColorButtonSelector(topComposite);
/*  532 */     this.extrapCS.setColorValue(new RGB(0, 192, 0));
/*      */ 
/*  537 */     Label speedUnitLabel = new Label(topComposite, 16384);
/*  538 */     speedUnitLabel.setText("Speed Unit:");
/*      */ 
/*  540 */     this.unitCombo = new Combo(topComposite, 12);
/*  541 */     for (String unit : UnitValues) {
/*  542 */       this.unitCombo.add(unit);
/*      */     }
/*      */ 
/*  545 */     this.unitCombo.select(0);
/*  546 */     setUnitComboSelectedIndex(0);
/*  547 */     this.unitCombo.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  549 */         TrackAttrDlg.this.setUnitComboSelectedIndex(TrackAttrDlg.this.unitCombo.getSelectionIndex());
/*      */       }
/*      */     });
/*  556 */     this.roundButton = new Button(topComposite, 32);
/*  557 */     String roundTo = "Round speed To:";
/*  558 */     this.roundButton.setText(roundTo);
/*  559 */     this.roundButton.setSelection(false);
/*  560 */     this.roundButton.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  562 */         if (!TrackAttrDlg.this.roundButton.getSelection())
/*  563 */           TrackAttrDlg.this.setRoundComboSelectedIndex(-1);
/*      */       }
/*      */     });
/*  567 */     this.roundCombo = new Combo(topComposite, 12);
/*  568 */     for (String round : RoundTo) {
/*  569 */       this.roundCombo.add(round);
/*      */     }
/*  571 */     this.roundCombo.select(0);
/*  572 */     setRoundComboSelectedIndex(0);
/*  573 */     this.roundCombo.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  575 */         if (TrackAttrDlg.this.roundButton.getSelection())
/*  576 */           TrackAttrDlg.this.setRoundComboSelectedIndex(TrackAttrDlg.this.roundCombo.getSelectionIndex());
/*      */         else
/*  578 */           TrackAttrDlg.this.setRoundComboSelectedIndex(-1);
/*      */       }
/*      */     });
/*  585 */     this.roundDirButton = new Button(topComposite, 32);
/*  586 */     String roundDirTo = "Round Direction To:";
/*  587 */     this.roundDirButton.setText(roundDirTo);
/*  588 */     this.roundDirButton.setSelection(false);
/*  589 */     this.roundDirButton.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  591 */         if (!TrackAttrDlg.this.roundDirButton.getSelection())
/*  592 */           TrackAttrDlg.this.setRoundDirComboSelectedIndex(-1);
/*      */       }
/*      */     });
/*  596 */     this.roundDirCombo = new Combo(topComposite, 12);
/*  597 */     for (String round : RoundDirTo) {
/*  598 */       this.roundDirCombo.add(round);
/*      */     }
/*  600 */     this.roundDirCombo.select(0);
/*  601 */     setRoundDirComboSelectedIndex(0);
/*  602 */     this.roundDirCombo.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  604 */         if (TrackAttrDlg.this.roundDirButton.getSelection())
/*  605 */           TrackAttrDlg.this.setRoundDirComboSelectedIndex(TrackAttrDlg.this.roundDirCombo.getSelectionIndex());
/*      */         else
/*  607 */           TrackAttrDlg.this.setRoundDirComboSelectedIndex(-1);
/*      */       }
/*      */     });
/*  614 */     Label optionLabel = new Label(topComposite, 16384);
/*  615 */     optionLabel.setText("Label Options:");
/*      */ 
/*  617 */     GridData gridData = new GridData();
/*  618 */     gridData.horizontalSpan = 2;
/*  619 */     optionLabel.setLayoutData(gridData);
/*      */ 
/*  624 */     this.skipFactorButton = new Button(topComposite, 16);
/*  625 */     String skipFactorButtonText = "Skip factor";
/*  626 */     this.skipFactorButton.setText(skipFactorButtonText);
/*  627 */     this.skipFactorButton.setSelection(true);
/*  628 */     this.skipFactorText = new Text(topComposite, 2052);
/*  629 */     this.skipFactorText.setLayoutData(new GridData(textWidth / 4, textHeight));
/*      */ 
/*  631 */     this.skipFactorText.setText("0");
/*  632 */     labelOptionButtonSelectionListenerAction(this.skipFactorButton, this.skipFactorText, true, 
/*  633 */       skipFactorButtonText, 0);
/*      */ 
/*  635 */     this.showFirstLastButton = createButton(topComposite, "Show first&&last", true, 2);
/*  636 */     labelOptionButtonSelectionListenerAction(this.showFirstLastButton, this.skipFactorText, false, 
/*  637 */       skipFactorButtonText, 0);
/*      */ 
/*  639 */     this.onHourButton = createButton(topComposite, "On hour", true, 2);
/*  640 */     labelOptionButtonSelectionListenerAction(this.onHourButton, this.skipFactorText, false, 
/*  641 */       skipFactorButtonText, 0);
/*      */ 
/*  643 */     this.onHalfHourButton = createButton(topComposite, "On half-hour", true, 2);
/*  644 */     labelOptionButtonSelectionListenerAction(this.onHalfHourButton, this.skipFactorText, false, 
/*  645 */       skipFactorButtonText, 0);
/*      */ 
/*  650 */     setExtraPointTimeDisplayOption(ITrack.ExtraPointTimeDisplayOption.SKIP_FACTOR);
/*      */ 
/*  655 */     Label fontLabel = new Label(topComposite, 16384);
/*  656 */     fontLabel.setText("Font:");
/*  657 */     this.fontNameCombo = new Combo(topComposite, 12);
/*  658 */     for (String fontName : FontName) {
/*  659 */       this.fontNameCombo.add(fontName);
/*      */     }
/*  661 */     this.fontNameCombo.select(0);
/*  662 */     setFontNameComboSelectedIndex(0);
/*  663 */     this.fontNameCombo.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  665 */         TrackAttrDlg.this.setFontNameComboSelectedIndex(TrackAttrDlg.this.fontNameCombo.getSelectionIndex());
/*      */       }
/*      */     });
/*  669 */     Label fontSizeLabel = new Label(topComposite, 16384);
/*  670 */     fontSizeLabel.setText("Size:");
/*  671 */     this.fontSizeCombo = new Combo(topComposite, 12);
/*  672 */     for (FontSizeName fontSizeName : FontSizeName.values()) {
/*  673 */       this.fontSizeCombo.add(fontSizeName.name());
/*      */     }
/*  675 */     this.fontSizeCombo.select(2);
/*  676 */     setFontSizeComboSelectedIndex(2);
/*  677 */     this.fontSizeCombo.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  679 */         TrackAttrDlg.this.setFontSizeComboSelectedIndex(TrackAttrDlg.this.fontSizeCombo.getSelectionIndex());
/*      */       }
/*      */     });
/*  683 */     Label fontStyleLabel = new Label(topComposite, 16384);
/*  684 */     fontStyleLabel.setText("Style:");
/*  685 */     this.fontStyleCombo = new Combo(topComposite, 12);
/*  686 */     for (IText.FontStyle fontStyle : IText.FontStyle.values()) {
/*  687 */       this.fontStyleCombo.add(fontStyle.name());
/*      */     }
/*  689 */     this.fontStyleCombo.select(2);
/*  690 */     setFontStyleComboSelectedIndex(2);
/*  691 */     this.fontStyleCombo.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  693 */         TrackAttrDlg.this.setFontStyleComboSelectedIndex(TrackAttrDlg.this.fontStyleCombo.getSelectionIndex());
/*      */       }
/*      */     });
/*  697 */     addSeparator(topComposite.getParent());
/*      */   }
/*      */ 
/*      */   private void frameAndSetTimeButtonSelectionListenerAction(final Button button, final Text firstTimeText, final Text secondTimeText)
/*      */   {
/*  703 */     button.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  705 */         String[] firstAndSecondTimeValueArray = null;
/*  706 */         String buttonText = button.getText();
/*  707 */         if (buttonText.indexOf("Frame") < 0) {
/*  708 */           firstAndSecondTimeValueArray = TrackAttrDlg.this.getFirstSecondTimeInitialTimeValueForSetTimeButton();
/*  709 */           TrackAttrDlg.this.setSetTimeButtonSelected(true);
/*      */         } else {
/*  711 */           firstAndSecondTimeValueArray = TrackAttrDlg.this.getFirstSecondTimeInitialTimeValueForFrameTimeButton();
/*  712 */           TrackAttrDlg.this.setSetTimeButtonSelected(false);
/*      */         }
/*  714 */         firstTimeText.setText(firstAndSecondTimeValueArray[0]);
/*  715 */         secondTimeText.setText(firstAndSecondTimeValueArray[1]);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private String[] getFirstSecondTimeInitialTimeValueForFrameTimeButton() {
/*  721 */     return getFirstSecondTimeInitialTimeValueForSetTimeButton();
/*      */   }
/*      */ 
/*      */   private String[] getFirstSecondTimeInitialTimeValueForSetTimeButton() {
/*  725 */     String[] timeValueResult = new String[2];
/*  726 */     Calendar calendar = Calendar.getInstance();
/*      */ 
/*  728 */     calendar.add(11, 4);
/*  729 */     setFirstTimeCalendar(calendar);
/*  730 */     timeValueResult[0] = getDateTimeStringValue(calendar);
/*  731 */     calendar.add(11, 1);
/*  732 */     setSecondTimeCalendar(calendar);
/*  733 */     timeValueResult[1] = getDateTimeStringValue(calendar);
/*      */ 
/*  771 */     return timeValueResult;
/*      */   }
/*      */ 
/*      */   private String getFirstOrSecondTimeStringValue(Calendar timeCalendar, boolean isFirstTimeCalendar, TrackPoint[] initTrackPoints) {
/*  775 */     String timeStringValue = "";
/*  776 */     if (timeCalendar != null) {
/*  777 */       timeStringValue = getDateTimeStringValue(timeCalendar);
/*      */     }
/*  779 */     else if ((initTrackPoints != null) && (initTrackPoints.length >= 2)) {
/*  780 */       int trackPointArrayIndex = initTrackPoints.length - 1;
/*  781 */       if (isFirstTimeCalendar)
/*  782 */         trackPointArrayIndex--;
/*  783 */       timeStringValue = getInitialPointsTimeStringValue(initTrackPoints, trackPointArrayIndex);
/*      */     }
/*      */ 
/*  786 */     return timeStringValue;
/*      */   }
/*      */ 
/*      */   private String getInitialPointsTimeStringValue(TrackPoint[] trackPointArray, int pointArrayIndex) {
/*  790 */     String timeStringValue = "";
/*  791 */     if (pointArrayIndex < trackPointArray.length) {
/*  792 */       TrackPoint targetTrackPoint = trackPointArray[pointArrayIndex];
/*  793 */       if ((targetTrackPoint != null) && (targetTrackPoint.getTime() != null))
/*  794 */         timeStringValue = getDateTimeStringValue(targetTrackPoint.getTime());
/*      */     }
/*  796 */     return timeStringValue;
/*      */   }
/*      */ 
/*      */   private String getDateTimeStringValue(Calendar calendar)
/*      */   {
/*  801 */     StringBuilder stringBuilder = new StringBuilder(11);
/*  802 */     int year = calendar.get(1);
/*  803 */     int month = calendar.get(2);
/*  804 */     int day = calendar.get(5);
/*  805 */     int hour = calendar.get(11);
/*  806 */     int minute = calendar.get(12);
/*      */ 
/*  808 */     String yearString = String.valueOf(year);
/*  809 */     stringBuilder.append(yearString.substring(2));
/*  810 */     if (month + 1 < 10)
/*  811 */       stringBuilder.append(0);
/*  812 */     stringBuilder.append(month + 1);
/*  813 */     if (day < 10)
/*  814 */       stringBuilder.append(0);
/*  815 */     stringBuilder.append(day);
/*  816 */     stringBuilder.append("/");
/*  817 */     if (hour < 10)
/*  818 */       stringBuilder.append(0);
/*  819 */     stringBuilder.append(hour);
/*  820 */     if (minute < 10)
/*  821 */       stringBuilder.append(0);
/*  822 */     stringBuilder.append(minute);
/*      */ 
/*  824 */     return stringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private Text createTextfieldWithLabel(Composite parentComposite, String textLabel, int textStyle, int textWidth, int textHeight, boolean isEditable)
/*      */   {
/*  855 */     Label firstTimeLabel = new Label(parentComposite, 0);
/*  856 */     firstTimeLabel.setText(textLabel);
/*      */ 
/*  858 */     Text text = new Text(parentComposite, textStyle);
/*  859 */     text.setLayoutData(new GridData(textWidth, textHeight));
/*  860 */     text.setEditable(isEditable);
/*  861 */     return text;
/*      */   }
/*      */ 
/*      */   private Button createButton(Composite parentComposite, String buttonText, boolean isHorizontalSpan, int spanValue)
/*      */   {
/*  874 */     Button button = new Button(parentComposite, 16);
/*  875 */     button.setText(buttonText);
/*      */ 
/*  879 */     if (isHorizontalSpan) {
/*  880 */       GridData gridData = new GridData();
/*  881 */       gridData.horizontalSpan = spanValue;
/*  882 */       button.setLayoutData(gridData);
/*      */     }
/*  884 */     return button;
/*      */   }
/*      */ 
/*      */   private void labelOptionButtonSelectionListenerAction(final Button button, final Text targetText, final boolean isTargetTextEditable, String skipFactorButtonText, final int defaultSkipFactorValue)
/*      */   {
/*  890 */     button.addSelectionListener(new SelectionAdapter() {
/*      */       public void widgetSelected(SelectionEvent e) {
/*  892 */         targetText.setEditable(isTargetTextEditable);
/*  893 */         if (isTargetTextEditable)
/*  894 */           targetText.setText(String.valueOf(defaultSkipFactorValue));
/*      */         else
/*  896 */           targetText.setText("");
/*  897 */         String buttonTextString = button.getText();
/*  898 */         ITrack.ExtraPointTimeDisplayOption extraPointTimeDisplayOption = 
/*  899 */           TrackAttrDlg.this.decideExtraPointTimeDisplayOptionByButtonText(buttonTextString);
/*  900 */         TrackAttrDlg.this.setExtraPointTimeDisplayOption(extraPointTimeDisplayOption);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private ITrack.ExtraPointTimeDisplayOption decideExtraPointTimeDisplayOptionByButtonText(String buttonTextString)
/*      */   {
/*  908 */     if (buttonTextString.equalsIgnoreCase("Show first&&last"))
/*  909 */       return ITrack.ExtraPointTimeDisplayOption.SHOW_FIRST_LAST;
/*  910 */     if (buttonTextString.equalsIgnoreCase("On hour"))
/*  911 */       return ITrack.ExtraPointTimeDisplayOption.ON_ONE_HOUR;
/*  912 */     if (buttonTextString.equalsIgnoreCase("On half-hour")) {
/*  913 */       return ITrack.ExtraPointTimeDisplayOption.ON_HALF_HOUR;
/*      */     }
/*  915 */     return ITrack.ExtraPointTimeDisplayOption.SKIP_FACTOR;
/*      */   }
/*      */ 
/*      */   public String[] getString()
/*      */   {
/*  924 */     return this.text.getText().split("\n");
/*      */   }
/*      */ 
/*      */   public float getFontSize()
/*      */   {
/*  932 */     return FontSizeValue[this.fontSizeCombo.getSelectionIndex()];
/*      */   }
/*      */ 
/*      */   public String getFontName()
/*      */   {
/*  939 */     return FontName[this.fontNameCombo.getSelectionIndex()];
/*      */   }
/*      */ 
/*      */   public IText.FontStyle getStyle()
/*      */   {
/*  946 */     return IText.FontStyle.values()[this.fontStyleCombo.getSelectionIndex()];
/*      */   }
/*      */ 
/*      */   public void setFontSize(float size)
/*      */   {
/*  954 */     int index = 0;
/*  955 */     for (int ii = 0; ii < FontSizeValue.length; ii++) {
/*  956 */       if ((int)size == FontSizeValue[ii]) {
/*  957 */         index = ii;
/*  958 */         break;
/*      */       }
/*      */     }
/*      */ 
/*  962 */     this.fontSizeCombo.select(index);
/*      */   }
/*      */ 
/*      */   public void setFontName(String name)
/*      */   {
/*  970 */     for (String st : FontName)
/*  971 */       if (st.equalsIgnoreCase(name)) {
/*  972 */         this.fontNameCombo.setText(st);
/*  973 */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void setStyle(IText.FontStyle style)
/*      */   {
/*  982 */     for (IText.FontStyle fs : IText.FontStyle.values())
/*  983 */       if (fs == style) {
/*  984 */         this.fontStyleCombo.setText(fs.name());
/*  985 */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void setAttrForDlg(IAttribute attr)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setAttr(AbstractDrawableComponent adc)
/*      */   {
/*  998 */     if ((adc instanceof Track))
/*  999 */       initializeTrackAttrDlg((Track)adc);
/*      */   }
/*      */ 
/*      */   public TrackExtrapPointInfoDlg getTrackExtrapPointInfoDlg()
/*      */   {
/* 1007 */     return this.trackExtrapPointInfoDlg;
/*      */   }
/*      */ 
/*      */   public void setTrackExtrapPointInfoDlg(TrackExtrapPointInfoDlg trackExtrapPointInfoDlg)
/*      */   {
/* 1012 */     this.trackExtrapPointInfoDlg = trackExtrapPointInfoDlg;
/*      */   }
/*      */ 
/*      */   public Button getSkipFactorButton() {
/* 1016 */     return this.skipFactorButton;
/*      */   }
/*      */ 
/*      */   public Button getShowFirstLastButton() {
/* 1020 */     return this.showFirstLastButton;
/*      */   }
/*      */ 
/*      */   public Button getOnHourButton() {
/* 1024 */     return this.onHourButton;
/*      */   }
/*      */ 
/*      */   public Button getOnHalfHourButton() {
/* 1028 */     return this.onHalfHourButton;
/*      */   }
/*      */ 
/*      */   public Combo getFontSizeCombo() {
/* 1032 */     return this.fontSizeCombo;
/*      */   }
/*      */ 
/*      */   public Combo getFontNameCombo() {
/* 1036 */     return this.fontNameCombo;
/*      */   }
/*      */ 
/*      */   public Combo getFontStyleCombo() {
/* 1040 */     return this.fontStyleCombo;
/*      */   }
/*      */ 
/*      */   public int getFontSizeComboSelectedIndex()
/*      */   {
/* 1045 */     return this.fontSizeComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setFontSizeComboSelectedIndex(int fontSizeComboSelectedIndex) {
/* 1049 */     this.fontSizeComboSelectedIndex = fontSizeComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public int getFontNameComboSelectedIndex() {
/* 1053 */     return this.fontNameComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setFontNameComboSelectedIndex(int fontNameComboSelectedIndex) {
/* 1057 */     this.fontNameComboSelectedIndex = fontNameComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public int getFontStyleComboSelectedIndex() {
/* 1061 */     return this.fontStyleComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setFontStyleComboSelectedIndex(int fontStyleComboSelectedIndex) {
/* 1065 */     this.fontStyleComboSelectedIndex = fontStyleComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public Combo getUnitCombo() {
/* 1069 */     return this.unitCombo;
/*      */   }
/*      */ 
/*      */   public int getUnitComboSelectedIndex() {
/* 1073 */     return this.unitComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setUnitComboSelectedIndex(int unitComboSelectedIndex) {
/* 1077 */     this.unitComboSelectedIndex = unitComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public Combo getRoundCombo() {
/* 1081 */     return this.roundCombo;
/*      */   }
/*      */ 
/*      */   public int getRoundComboSelectedIndex() {
/* 1085 */     return this.roundComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setRoundComboSelectedIndex(int roundComboSelectedIndex) {
/* 1089 */     this.roundComboSelectedIndex = roundComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public Combo getRoundDirCombo() {
/* 1093 */     return this.roundDirCombo;
/*      */   }
/*      */ 
/*      */   public int getRoundDirComboSelectedIndex() {
/* 1097 */     return this.roundDirComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public void setRoundDirComboSelectedIndex(int roundDirComboSelectedIndex) {
/* 1101 */     this.roundDirComboSelectedIndex = roundDirComboSelectedIndex;
/*      */   }
/*      */ 
/*      */   public boolean isSetTimeButtonSelected()
/*      */   {
/* 1106 */     return this.setTimeButtonSelected;
/*      */   }
/*      */ 
/*      */   public void setSetTimeButtonSelected(boolean setTimeButtonSelected) {
/* 1110 */     this.setTimeButtonSelected = setTimeButtonSelected;
/*      */   }
/*      */   public Button getFrameTimeButton() {
/* 1113 */     return this.frameTimeButton;
/*      */   }
/*      */ 
/*      */   public void setFrameTimeButton(Button frameTimeButton) {
/* 1117 */     this.frameTimeButton = frameTimeButton;
/*      */   }
/*      */ 
/*      */   public Button getSetTimeButton() {
/* 1121 */     return this.setTimeButton;
/*      */   }
/*      */ 
/*      */   public void setSetTimeButton(Button setTimeButton) {
/* 1125 */     this.setTimeButton = setTimeButton;
/*      */   }
/*      */ 
/*      */   public ITrack.ExtraPointTimeDisplayOption getExtraPointTimeDisplayOption()
/*      */   {
/* 1130 */     return this.extraPointTimeDisplayOption;
/*      */   }
/*      */ 
/*      */   public void setExtraPointTimeDisplayOption(ITrack.ExtraPointTimeDisplayOption extraPointTimeDisplayOption) {
/* 1134 */     this.extraPointTimeDisplayOption = extraPointTimeDisplayOption;
/*      */   }
/*      */ 
/*      */   public String getSkipFactorText() {
/* 1138 */     return this.skipFactorText.getText();
/*      */   }
/*      */ 
/*      */   public void setSkipFactorText(String skipFactorText) {
/* 1142 */     this.skipFactorText.setText(skipFactorText);
/*      */   }
/*      */ 
/*      */   public Text getFirstTimeText() {
/* 1146 */     return this.firstTimeText;
/*      */   }
/*      */ 
/*      */   public void setFirstTimeText(Text firstTimeText) {
/* 1150 */     this.firstTimeText = firstTimeText;
/*      */   }
/*      */ 
/*      */   public Text getSecondTimeText() {
/* 1154 */     return this.secondTimeText;
/*      */   }
/*      */ 
/*      */   public void setSecondTimeText(Text secondTimeText) {
/* 1158 */     this.secondTimeText = secondTimeText;
/*      */   }
/*      */ 
/*      */   public int getExtraDrawingPointNumber() {
/* 1162 */     int ret = 0;
/*      */     try {
/* 1164 */       ret = Integer.parseInt(this.numberOfTimesText.getText());
/*      */     }
/*      */     catch (Exception e) {
/* 1167 */       ret = 0;
/*      */     }
/*      */ 
/* 1170 */     return ret;
/*      */   }
/*      */ 
/*      */   public void setNumberOfTimesText(Text numberOfTimesText) {
/* 1174 */     this.numberOfTimesText = numberOfTimesText;
/*      */   }
/*      */ 
/*      */   public String getPreviousIntervalTimeValue() {
/* 1178 */     return this.previousIntervalTimeValue;
/*      */   }
/*      */ 
/*      */   public void setPreviousIntervalTimeValue(String previousIntervalTimeValue) {
/* 1182 */     this.previousIntervalTimeValue = previousIntervalTimeValue;
/*      */   }
/*      */ 
/*      */   public Calendar getFirstTimeCalendar() {
/* 1186 */     return this.firstTimeCalendar;
/*      */   }
/*      */ 
/*      */   public String getIntervalTimeString() {
/* 1190 */     if (this.intervalCombo.getSelectionIndex() == this.intervalCombo.getItemCount() - 1) {
/* 1191 */       return this.intervalText.getText();
/*      */     }
/*      */ 
/* 1194 */     return this.intervalCombo.getText();
/*      */   }
/*      */ 
/*      */   private void setIntervalTimeString(String intervalTimeString) {
/* 1198 */     for (int ii = 0; ii < this.intervalCombo.getItemCount(); ii++) {
/* 1199 */       if (intervalTimeString.equalsIgnoreCase(this.intervalCombo.getItem(ii))) {
/* 1200 */         this.intervalCombo.select(ii);
/* 1201 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1206 */     this.intervalCombo.select(this.intervalCombo.getItemCount() - 1);
/* 1207 */     this.intervalText.setText(intervalTimeString);
/*      */   }
/*      */ 
/*      */   public void setFirstTimeCalendar(Calendar firstTimeCalendar)
/*      */   {
/* 1212 */     Calendar cal = Calendar.getInstance();
/* 1213 */     cal.set(1, firstTimeCalendar.get(1));
/* 1214 */     cal.set(2, firstTimeCalendar.get(2));
/* 1215 */     cal.set(5, firstTimeCalendar.get(5));
/* 1216 */     cal.set(11, firstTimeCalendar.get(11));
/* 1217 */     cal.set(12, firstTimeCalendar.get(12));
/* 1218 */     this.firstTimeCalendar = cal;
/*      */   }
/*      */ 
/*      */   public Calendar getSecondTimeCalendar() {
/* 1222 */     return this.secondTimeCalendar;
/*      */   }
/*      */ 
/*      */   public void setSecondTimeCalendar(Calendar secondTimeCalendar) {
/* 1226 */     Calendar cal = Calendar.getInstance();
/* 1227 */     cal.set(1, secondTimeCalendar.get(1));
/* 1228 */     cal.set(2, secondTimeCalendar.get(2));
/* 1229 */     cal.set(5, secondTimeCalendar.get(5));
/* 1230 */     cal.set(11, secondTimeCalendar.get(11));
/* 1231 */     cal.set(12, secondTimeCalendar.get(12));
/* 1232 */     this.secondTimeCalendar = cal;
/*      */   }
/*      */ 
/*      */   public Color getExtrapColor()
/*      */   {
/* 1237 */     Color color = new Color(this.extrapCS.getColorValue().red, 
/* 1238 */       this.extrapCS.getColorValue().green, this.extrapCS.getColorValue().blue);
/* 1239 */     return color;
/*      */   }
/*      */ 
/*      */   public String getExtrapLinePattern()
/*      */   {
/* 1245 */     return null;
/*      */   }
/*      */ 
/*      */   public String getExtrapMarker()
/*      */   {
/* 1251 */     return null;
/*      */   }
/*      */ 
/*      */   public TrackPoint[] getExtrapPoints()
/*      */   {
/* 1257 */     return null;
/*      */   }
/*      */ 
/*      */   public Color getInitialColor()
/*      */   {
/* 1262 */     Color color = new Color(this.initialCS.getColorValue().red, 
/* 1263 */       this.initialCS.getColorValue().green, this.initialCS.getColorValue().blue);
/* 1264 */     return color;
/*      */   }
/*      */ 
/*      */   public String getInitialLinePattern()
/*      */   {
/* 1270 */     return null;
/*      */   }
/*      */ 
/*      */   public String getInitialMarker()
/*      */   {
/* 1276 */     return null;
/*      */   }
/*      */ 
/*      */   public TrackPoint[] getInitialPoints()
/*      */   {
/* 1282 */     return null;
/*      */   }
/*      */ 
/*      */   public int getSmoothFactor()
/*      */   {
/* 1295 */     return 0;
/*      */   }
/*      */ 
/*      */   public String getLinePattern() {
/* 1299 */     return "Solid Line";
/*      */   }
/*      */ 
/*      */   public FillPatternList.FillPattern getFillPattern() {
/* 1303 */     return FillPatternList.FillPattern.FILL_PATTERN_0;
/*      */   }
/*      */ 
/*      */   public Boolean isClosedLine() {
/* 1307 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public Boolean isFilled() {
/* 1311 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public Color[] getColors() {
/* 1315 */     Color[] colors = new Color[1];
/* 1316 */     colors[0] = getInitialColor();
/* 1317 */     return colors;
/*      */   }
/*      */ 
/*      */   public float getLineWidth() {
/* 1321 */     return 1.0F;
/*      */   }
/*      */ 
/*      */   public double getSizeScale() {
/* 1325 */     return 2.0D;
/*      */   }
/*      */ 
/*      */   public IText.FontStyle getFontStyle()
/*      */   {
/* 1331 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean[] getExtraPointTimeTextDisplayIndicator()
/*      */   {
/* 1337 */     return null;
/*      */   }
/*      */ 
/*      */   public Coordinate[] getLinePoints()
/*      */   {
/* 1343 */     return null;
/*      */   }
/*      */ 
/*      */   public String getPatternName()
/*      */   {
/* 1349 */     return null;
/*      */   }
/*      */ 
/*      */   private Calendar gempakTM2Calendar(String gempakTm)
/*      */   {
/* 1359 */     Calendar cal = null;
/*      */     try {
/* 1361 */       cal = Calendar.getInstance();
/* 1362 */       int year = 2000 + Integer.valueOf(gempakTm.substring(0, 2)).intValue();
/* 1363 */       int month = Integer.valueOf(gempakTm.substring(2, 4)).intValue() - 1;
/* 1364 */       int day = Integer.valueOf(gempakTm.substring(4, 6)).intValue();
/* 1365 */       int hour = Integer.valueOf(gempakTm.substring(7, 9)).intValue();
/* 1366 */       int min = Integer.valueOf(gempakTm.substring(9)).intValue();
/* 1367 */       cal.set(1, year);
/* 1368 */       cal.set(2, month);
/* 1369 */       cal.set(5, day);
/* 1370 */       cal.set(11, hour);
/* 1371 */       cal.set(12, min);
/*      */     }
/*      */     catch (Exception e) {
/* 1374 */       cal = null;
/*      */     }
/*      */ 
/* 1377 */     return cal;
/*      */   }
/*      */ 
/*      */   public static enum FontSizeName
/*      */   {
/*   74 */     TINY, SMALL, MEDIUM, LARGE, HUGE, GIANT;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.TrackAttrDlg
 * JD-Core Version:    0.6.2
 */