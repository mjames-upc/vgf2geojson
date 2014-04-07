/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.ElementFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.ElementFilterCollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.ForecastHourFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.Node;
/*     */ import org.dom4j.io.SAXReader;
/*     */ import org.eclipse.swt.events.KeyEvent;
/*     */ import org.eclipse.swt.events.KeyListener;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class PgenFilterDlg extends CaveJFACEDialog
/*     */ {
/*  67 */   private static PgenFilterDlg INSTANCE = null;
/*  68 */   private static GfaAttrDlg gfaAttrDlg = null;
/*     */   private PgenResource drawingLayer;
/*     */   private AbstractEditor mapEditor;
/*     */   private Composite top;
/*     */   private int matchIndex;
/*     */   protected static Button[] hourBtns;
/*  81 */   protected int indexOfHotKeyEnabledButton = -1;
/*  82 */   protected int buttonEnabledCounter = 0;
/*     */ 
/*  84 */   private static String[] HOURS = null;
/*  85 */   private static Document filterHourTbl = null;
/*  86 */   private static String HOUR_XPATH = "/root/filterHour/label";
/*     */   private HashMap<String, ElementFilter> filterMap;
/*     */   protected FilterButtonKeyListener filterBtnKeyLstnr;
/*     */   private ForecastHourFilter blockAll;
/*     */   private String[] fcstCboTextArray;
/*     */   private boolean isGfaDialogOpen;
/*     */ 
/*     */   private void updateTheGfaFcstHrIfItMatchesFilterHr(String currentFilter)
/*     */   {
/* 269 */     currentFilter = currentFilter.endsWith("+") ? currentFilter.replace('+', ' ').trim() : currentFilter;
/* 270 */     String[] fcstCboTextArray = gfaAttrDlg.fcstHrCbo.getItems();
/*     */ 
/* 272 */     if (currentFilter.compareTo(gfaAttrDlg.getGfaFcstHr()) != 0) {
/* 273 */       int index = -1;
/* 274 */       for (String currFcstHrText : fcstCboTextArray) {
/* 275 */         index++;
/* 276 */         String fcstHr = currFcstHrText.split(" ")[0];
/* 277 */         if (fcstHr.compareTo(currentFilter) == 0) {
/* 278 */           gfaAttrDlg.fcstHrCbo.setText(currFcstHrText);
/* 279 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static PgenFilterDlg getInstance(Shell parShell)
/*     */   {
/* 296 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/* 300 */         INSTANCE = new PgenFilterDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/* 304 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 309 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   private PgenFilterDlg(Shell parentShell)
/*     */     throws VizException
/*     */   {
/* 319 */     super(parentShell);
/* 320 */     setShellStyle(96);
/*     */ 
/* 322 */     filterHourTbl = readFilterHourTbl();
/* 323 */     HOURS = getFilterHour();
/*     */ 
/* 325 */     this.filterMap = new HashMap();
/* 326 */     this.filterBtnKeyLstnr = new FilterButtonKeyListener(null);
/* 327 */     for (String str : HOURS) {
/* 328 */       this.filterMap.put(str, new ForecastHourFilter(str));
/*     */     }
/*     */ 
/* 331 */     this.blockAll = new ForecastHourFilter("blockAll");
/* 332 */     gfaAttrDlg = GfaAttrDlg.getInstance(parentShell);
/* 333 */     this.matchIndex = -1;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 344 */     getShell().setText("Forecast Hour Filter");
/*     */ 
/* 346 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 351 */     GridLayout mainLayout = new GridLayout(HOURS.length, false);
/*     */ 
/* 353 */     mainLayout.marginHeight = 3;
/* 354 */     mainLayout.marginWidth = 3;
/*     */ 
/* 356 */     this.top.setLayout(mainLayout);
/*     */ 
/* 358 */     hourBtns = new Button[HOURS.length];
/*     */ 
/* 361 */     for (int ii = 0; ii < HOURS.length; ii++) {
/* 362 */       hourBtns[ii] = new Button(this.top, 32);
/* 363 */       hourBtns[ii].setText(HOURS[ii]);
/* 364 */       hourBtns[ii].addKeyListener(this.filterBtnKeyLstnr);
/* 365 */       hourBtns[ii].addSelectionListener(new SelectionAdapter()
/*     */       {
/*     */         public void widgetSelected(SelectionEvent e)
/*     */         {
/* 372 */           String filterText = ((Button)e.widget).getText();
/*     */ 
/* 374 */           if (((Button)e.widget).getSelection())
/*     */           {
/* 376 */             PgenFilterDlg.this.drawingLayer.getFilters().addFilter((ElementFilter)PgenFilterDlg.this.filterMap.get(filterText));
/*     */ 
/* 378 */             if (PgenFilterDlg.gfaAttrDlg.isGfaOpen()) {
/* 379 */               PgenFilterDlg.this.updateGfaDialog(true, filterText);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 384 */             PgenFilterDlg.this.drawingLayer.getFilters().removeFilter((ElementFilter)PgenFilterDlg.this.filterMap.get(filterText));
/* 385 */             PgenFilterDlg.this.drawingLayer.removeSelected();
/* 386 */             PgenUtil.setSelectingMode();
/* 387 */             if (PgenFilterDlg.gfaAttrDlg.isGfaOpen()) {
/* 388 */               PgenFilterDlg.this.updateGfaDialog(false, "");
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 393 */           PgenFilterDlg.this.mapEditor.refresh();
/*     */         }
/*     */ 
/*     */       });
/*     */     }
/*     */ 
/* 401 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void updateGfaDialog(boolean activateButton, String filterText)
/*     */   {
/* 412 */     if ((filterText.compareTo("AIRM") == 0) || 
/* 413 */       (filterText.compareTo("OTLK") == 0)) {
/* 414 */       return;
/*     */     }
/* 416 */     for (int ii = HOURS.length - 1; ii >= 0; ii--) {
/* 417 */       if (((hourBtns[ii].getText().compareTo("AIRM") == 0) || 
/* 418 */         (hourBtns[ii].getText().compareTo("OTLK") == 0)) && 
/* 419 */         (hourBtns[ii].getSelection())) {
/* 420 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 425 */     this.fcstCboTextArray = new String[gfaAttrDlg.fcstHrCbo.getItemCount()];
/* 426 */     if (filterText.endsWith("+")) {
/* 427 */       filterText = new String(filterText.replace('+', ' ').trim());
/*     */     }
/* 429 */     this.fcstCboTextArray = gfaAttrDlg.fcstHrCbo.getItems();
/* 430 */     if (activateButton)
/*     */     {
/* 432 */       if ((this.fcstCboTextArray != null) && (this.fcstCboTextArray.length > 0))
/*     */       {
/* 434 */         String gfaFcstHour = gfaAttrDlg.getGfaFcstHr();
/*     */ 
/* 436 */         if (Integer.parseInt(filterText) > Integer.parseInt(gfaFcstHour))
/*     */         {
/* 438 */           int index = -1;
/* 439 */           for (String currFcstHrText : this.fcstCboTextArray) {
/* 440 */             index++;
/* 441 */             String fcstHr = currFcstHrText.split(" ")[0];
/* 442 */             if (filterText.compareTo(fcstHr) == 0)
/*     */             {
/* 444 */               if (index > this.matchIndex) {
/* 445 */                 gfaAttrDlg.fcstHrCbo.setText(currFcstHrText);
/*     */               }
/* 447 */               this.matchIndex = index;
/* 448 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 456 */       for (int ii = HOURS.length - 1; ii >= 0; ii--) {
/* 457 */         String thisBtnText = hourBtns[ii].getText();
/* 458 */         if (thisBtnText.endsWith("+"))
/* 459 */           thisBtnText = new String(thisBtnText.replace('+', ' ').trim());
/* 460 */         if ((hourBtns[ii].getSelection()) && 
/* 461 */           (thisBtnText.compareTo("AIRM") != 0) && 
/* 462 */           (thisBtnText.compareTo("OTLK") != 0))
/*     */         {
/* 464 */           for (String currFcstHrText : this.fcstCboTextArray) {
/* 465 */             String fcstHr = currFcstHrText.split(" ")[0];
/* 466 */             if (fcstHr.compareTo(thisBtnText) == 0) {
/* 467 */               gfaAttrDlg.fcstHrCbo.setText(currFcstHrText);
/* 468 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 476 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   protected void okPressed()
/*     */   {
/* 487 */     if (getButton(0).getText().equalsIgnoreCase("All On"))
/*     */     {
/* 490 */       for (Button btn : hourBtns)
/*     */       {
/* 492 */         btn.setSelection(true);
/* 493 */         this.drawingLayer.getFilters().addFilter((ElementFilter)this.filterMap.get(btn.getText()));
/*     */       }
/* 495 */       this.buttonEnabledCounter = hourBtns.length;
/* 496 */       getButton(0).setText("All Off");
/*     */     }
/*     */     else
/*     */     {
/* 500 */       for (Button btn : hourBtns) {
/* 501 */         btn.setSelection(false);
/* 502 */         this.drawingLayer.getFilters().removeFilter((ElementFilter)this.filterMap.get(btn.getText()));
/*     */       }
/* 504 */       this.buttonEnabledCounter = 0;
/* 505 */       getButton(0).setText("All On");
/*     */     }
/*     */ 
/* 508 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   protected void cancelPressed()
/*     */   {
/* 521 */     for (Button btn : hourBtns) {
/* 522 */       btn.setSelection(false);
/* 523 */       this.drawingLayer.getFilters().removeFilter((ElementFilter)this.filterMap.get(btn.getText()));
/*     */     }
/*     */ 
/* 526 */     this.drawingLayer.getFilters().removeFilter(this.blockAll);
/*     */ 
/* 528 */     this.mapEditor.refresh();
/*     */ 
/* 530 */     super.cancelPressed();
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 541 */     if (getShell() == null) {
/* 542 */       create();
/*     */     }
/*     */ 
/* 545 */     getShell().setLocation(getShell().getParent().getLocation());
/* 546 */     getButton(0).setText("All On");
/* 547 */     getButton(0).addKeyListener(this.filterBtnKeyLstnr);
/* 548 */     getButton(1).setText("Close");
/*     */ 
/* 550 */     getButtonBar().pack();
/*     */ 
/* 553 */     this.drawingLayer.getFilters().addFilter(this.blockAll);
/*     */ 
/* 555 */     return super.open();
/*     */   }
/*     */ 
/*     */   public void setResource(PgenResource resource, AbstractEditor editor)
/*     */   {
/* 566 */     this.drawingLayer = resource;
/* 567 */     this.mapEditor = editor;
/*     */   }
/*     */ 
/*     */   public static Document readFilterHourTbl()
/*     */   {
/* 577 */     if (HOURS == null) {
/*     */       try {
/* 579 */         String filterHourFile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/* 580 */           PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "filterHour.xml");
/*     */ 
/* 582 */         SAXReader reader = new SAXReader();
/* 583 */         filterHourTbl = reader.read(filterHourFile);
/*     */       } catch (Exception e) {
/* 585 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 589 */     return filterHourTbl;
/*     */   }
/*     */ 
/*     */   private String[] getFilterHour()
/*     */   {
/* 596 */     if (filterHourTbl == null) {
/* 597 */       HOURS = new String[] { "0", "0+", "3", "3+", "6", "9", "12", "0-0", "3-3", "6-6", "0-3", "0-6", "3-6", "6-9", 
/* 598 */         "6-12", "9-12", "AIRM", "OTLK" };
/*     */     } else {
/* 600 */       List list = new ArrayList();
/* 601 */       List nodes = filterHourTbl.selectNodes(HOUR_XPATH);
/*     */ 
/* 603 */       for (Node node : nodes) {
/* 604 */         list.add(node.valueOf("@text").toString());
/*     */       }
/*     */ 
/* 607 */       HOURS = new String[list.size()];
/* 608 */       HOURS = (String[])list.toArray(HOURS);
/*     */     }
/*     */ 
/* 611 */     return HOURS;
/*     */   }
/*     */ 
/*     */   public void setHourChkBox(String hours, boolean flag)
/*     */   {
/* 623 */     Button hrBtn = null;
/* 624 */     for (Button btn : hourBtns) {
/* 625 */       if (btn.getText().equalsIgnoreCase(hours)) {
/* 626 */         hrBtn = btn;
/* 627 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 633 */     if (hrBtn == null) {
/* 634 */       double fHrs = -1.0D;
/* 635 */       double fMinutes = -1.0D;
/* 636 */       if (hours.contains(":")) {
/* 637 */         String[] hm = hours.split(":");
/*     */         try {
/* 639 */           fHrs = Integer.valueOf(hm[0]).intValue();
/* 640 */           fMinutes = Integer.valueOf(hm[1]).intValue();
/*     */         }
/*     */         catch (Exception e) {
/* 643 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */       else {
/*     */         try {
/* 648 */           fHrs = Integer.valueOf(hours).intValue();
/*     */         }
/*     */         catch (Exception e) {
/* 651 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */ 
/* 655 */       fMinutes = fMinutes > 0.0D ? fMinutes / 60.0D : 0.0D;
/* 656 */       if (fHrs > 0.0D) {
/* 657 */         String hrStr = "";
/* 658 */         fHrs += fMinutes;
/* 659 */         if (fHrs <= 0.001D) hrStr = "0";
/* 660 */         else if ((fHrs > 0.001D) && (fHrs < 3.0D)) hrStr = "0+";
/* 661 */         else if (Math.abs(fHrs - 3.0D) < 0.001D) hrStr = "3";
/* 662 */         else if (fHrs > 3.001D) hrStr = "3+";
/* 663 */         if (!hrStr.isEmpty()) {
/* 664 */           for (Button btn : hourBtns) {
/* 665 */             if (btn.getText().equalsIgnoreCase(hrStr)) {
/* 666 */               hrBtn = btn;
/* 667 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 676 */     if (hrBtn != null)
/* 677 */       if (flag) {
/* 678 */         hrBtn.setSelection(true);
/* 679 */         this.drawingLayer.getFilters().addFilter((ElementFilter)this.filterMap.get(hrBtn.getText()));
/*     */       }
/*     */       else {
/* 682 */         hrBtn.setSelection(false);
/* 683 */         this.drawingLayer.getFilters().removeFilter((ElementFilter)this.filterMap.get(hrBtn.getText()));
/*     */       }
/*     */   }
/*     */ 
/*     */   public static boolean isFilterDlgOpen()
/*     */   {
/* 696 */     if ((INSTANCE != null) && (INSTANCE.getShell() != null)) {
/* 697 */       return true;
/*     */     }
/* 699 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean close()
/*     */   {
/* 706 */     this.matchIndex = -1;
/* 707 */     return super.close();
/*     */   }
/*     */ 
/*     */   private class FilterButtonKeyListener
/*     */     implements KeyListener
/*     */   {
/*     */     int pivotalIndex;
/*     */     protected int prevIndex;
/*     */     protected int nextIndex;
/*  99 */     int indexArraySize = 0;
/*     */     int index;
/* 102 */     int hoursArrayLength = PgenFilterDlg.HOURS.length;
/* 103 */     List<Integer> enabledButtonIndexList = new ArrayList(0);
/*     */ 
/*     */     private FilterButtonKeyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void keyPressed(KeyEvent e) {
/* 110 */       for (this.index = 0; this.index < this.hoursArrayLength; this.index += 1)
/*     */       {
/* 113 */         if (PgenFilterDlg.hourBtns[this.index].getSelection())
/*     */         {
/* 116 */           if (PgenFilterDlg.this.buttonEnabledCounter >= this.hoursArrayLength) {
/* 117 */             PgenFilterDlg.this.buttonEnabledCounter = this.hoursArrayLength;
/*     */           }
/*     */           else {
/* 120 */             PgenFilterDlg.this.buttonEnabledCounter += 1;
/*     */           }
/*     */ 
/* 123 */           this.enabledButtonIndexList.add(Integer.valueOf(this.index));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 131 */       this.indexArraySize = this.enabledButtonIndexList.size();
/*     */ 
/* 139 */       if (PgenFilterDlg.this.buttonEnabledCounter == this.hoursArrayLength) {
/* 140 */         disableAllButtons();
/*     */       }
/*     */ 
/* 144 */       switch (e.keyCode)
/*     */       {
/*     */       case 91:
/* 155 */         if (PgenFilterDlg.this.buttonEnabledCounter > 0)
/*     */         {
/* 157 */           this.pivotalIndex = ((Integer)this.enabledButtonIndexList.get(0)).intValue();
/*     */         }
/* 159 */         else this.pivotalIndex = PgenFilterDlg.HOURS.length;
/*     */ 
/* 162 */         this.prevIndex = (this.pivotalIndex - 1);
/*     */ 
/* 165 */         if (this.prevIndex < 0)
/*     */         {
/* 167 */           this.prevIndex = 0;
/*     */         }
/*     */ 
/* 173 */         disableAllButtons();
/*     */ 
/* 176 */         PgenFilterDlg.hourBtns[this.prevIndex].setSelection(true);
/* 177 */         PgenFilterDlg.this.indexOfHotKeyEnabledButton = this.prevIndex;
/* 178 */         PgenFilterDlg.this.buttonEnabledCounter += 1;
/* 179 */         PgenFilterDlg.hourBtns[this.prevIndex].notifyListeners(13, new Event());
/*     */ 
/* 181 */         break;
/*     */       case 93:
/* 192 */         if (PgenFilterDlg.this.buttonEnabledCounter > 0) {
/* 193 */           this.pivotalIndex = ((Integer)this.enabledButtonIndexList.get(this.indexArraySize - 1)).intValue();
/*     */         }
/*     */         else {
/* 196 */           this.pivotalIndex = -1;
/*     */         }
/* 198 */         this.nextIndex = (this.pivotalIndex + 1);
/*     */ 
/* 201 */         if (this.nextIndex == this.hoursArrayLength) {
/* 202 */           this.nextIndex -= 1;
/*     */         }
/*     */ 
/* 207 */         disableAllButtons();
/*     */ 
/* 211 */         PgenFilterDlg.hourBtns[this.nextIndex].setSelection(true);
/* 212 */         PgenFilterDlg.this.indexOfHotKeyEnabledButton = this.nextIndex;
/* 213 */         PgenFilterDlg.this.buttonEnabledCounter += 1;
/* 214 */         PgenFilterDlg.hourBtns[this.nextIndex].notifyListeners(13, new Event());
/* 215 */         break;
/*     */       case 92:
/*     */       }
/*     */ 
/* 221 */       if (PgenFilterDlg.gfaAttrDlg.isGfaOpen()) {
/* 222 */         changeFcstHrLabelInGfaDialog();
/*     */       }
/*     */ 
/* 225 */       this.enabledButtonIndexList = new ArrayList(0);
/*     */ 
/* 227 */       PgenFilterDlg.this.buttonEnabledCounter = 0;
/*     */     }
/*     */ 
/*     */     public void keyReleased(KeyEvent e)
/*     */     {
/*     */     }
/*     */ 
/*     */     private void disableAllButtons()
/*     */     {
/* 239 */       for (this.index = 0; this.index < this.hoursArrayLength; this.index += 1) {
/* 240 */         PgenFilterDlg.hourBtns[this.index].setSelection(false);
/* 241 */         PgenFilterDlg.hourBtns[this.index].notifyListeners(13, new Event());
/*     */       }
/*     */     }
/*     */ 
/*     */     private void changeFcstHrLabelInGfaDialog()
/*     */     {
/* 247 */       String currentFilter = "";
/* 248 */       if (PgenFilterDlg.this.indexOfHotKeyEnabledButton != -1) {
/* 249 */         currentFilter = new String(PgenFilterDlg.hourBtns[PgenFilterDlg.this.indexOfHotKeyEnabledButton].getText());
/*     */       }
/* 251 */       if ((PgenFilterDlg.this.buttonEnabledCounter <= PgenFilterDlg.HOURS.length) && 
/* 252 */         (PgenFilterDlg.this.buttonEnabledCounter >= 0) && 
/* 253 */         (currentFilter.compareTo("AIRM") != 0) && 
/* 254 */         (currentFilter.compareTo("OTLK") != 0))
/* 255 */         PgenFilterDlg.this.updateTheGfaFcstHrIfItMatchesFilterHr(currentFilter);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.PgenFilterDlg
 * JD-Core Version:    0.6.2
 */