/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import org.eclipse.jface.dialogs.Dialog;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenLayerMergeDialog extends Dialog
/*     */ {
/*     */   private Composite top;
/*  78 */   private static String[] BulkActionNames = { 
/*  79 */     "Take no action", 
/*  80 */     "Reset all layers to 'Take no action'", 
/*  81 */     "Set all layers to 'Add as a new layer'", 
/*  82 */     "Set all layers to 'Replace content of like-named layers', if available", 
/*  83 */     "Set all layers to 'Merge content into like-named layers', if available", 
/*  84 */     "Set all layers to 'Merge into ActiveLayer'" };
/*     */   private static final int BULK_NO_ACTION = 0;
/*     */   private static final int BULK_RESET_ALL_NO_ACTION = 1;
/*     */   private static final int BULK_ADD_ALL_AS_NEW = 2;
/*     */   private static final int BULK_REPLACE_ALL_LIKE_NAME_LAYER = 3;
/*     */   private static final int BULK_MERGE_ALL_LIKE_NAME_LAYER = 4;
/*     */   private static final int BULK_MERGE_ALL_INTO_ACTIVE_LAYER = 5;
/*  96 */   protected PgenResource drawingLayer = null;
/*  97 */   protected Product incomingActivity = null;
/*  98 */   protected String incomingFile = null;
/*     */ 
/* 100 */   private LinkedHashMap<Layer, Combo> layerComboMap = null;
/*     */   private static Point shellLocation;
/*     */ 
/*     */   public PgenLayerMergeDialog(Shell parShell, Product prodIn, String fileIn)
/*     */     throws VizException
/*     */   {
/* 111 */     super(parShell);
/* 112 */     setShellStyle(32800);
/*     */ 
/* 114 */     this.drawingLayer = PgenSession.getInstance().getPgenResource();
/* 115 */     this.incomingActivity = prodIn;
/* 116 */     this.incomingFile = fileIn;
/*     */ 
/* 118 */     this.layerComboMap = new LinkedHashMap();
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 124 */     super.createButtonsForButtonBar(parent);
/*     */   }
/*     */ 
/*     */   public Control createButtonBar(Composite parent)
/*     */   {
/* 130 */     Control bar = super.createButtonBar(parent);
/* 131 */     GridData gd = new GridData(16777216, -1, true, false);
/* 132 */     bar.setLayoutData(gd);
/* 133 */     return bar;
/*     */   }
/*     */ 
/*     */   public void handleShellCloseEvent()
/*     */   {
/* 143 */     super.handleShellCloseEvent();
/* 144 */     PgenUtil.setSelectingMode();
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 153 */     if (getShell() == null) {
/* 154 */       create();
/*     */     }
/*     */ 
/* 157 */     if (shellLocation == null)
/* 158 */       getShell().setLocation(getShell().getParent().getLocation());
/*     */     else {
/* 160 */       getShell().setLocation(shellLocation);
/*     */     }
/*     */ 
/* 163 */     return super.open();
/*     */   }
/*     */ 
/*     */   public boolean close()
/*     */   {
/* 171 */     if (getShell() != null) {
/* 172 */       Rectangle bounds = getShell().getBounds();
/* 173 */       shellLocation = new Point(bounds.x, bounds.y);
/*     */     }
/* 175 */     return super.close();
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 184 */     this.top = ((Composite)super.createDialogArea(parent));
/* 185 */     getShell().setText("Contours Attributes");
/*     */ 
/* 188 */     GridLayout mainLayout = new GridLayout(1, false);
/* 189 */     mainLayout.marginHeight = 5;
/* 190 */     mainLayout.marginWidth = 3;
/* 191 */     this.top.setLayout(mainLayout);
/*     */ 
/* 194 */     initializeComponents();
/*     */ 
/* 196 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 204 */     getShell().setText("Import/Merge Layers");
/*     */ 
/* 206 */     Composite topComp = new Composite(this.top, 0);
/* 207 */     topComp.setLayoutData(new GridData(16777216, -1, true, false));
/* 208 */     GridLayout layout0 = new GridLayout(1, false);
/* 209 */     topComp.setLayout(layout0);
/*     */ 
/* 211 */     Label advLbl = new Label(topComp, 16777216);
/* 212 */     advLbl.setText("ADVANCED");
/*     */ 
/* 215 */     Composite curActComp = new Composite(this.top, 0);
/* 216 */     curActComp.setLayoutData(new GridData(16384, -1, true, false));
/*     */ 
/* 218 */     GridLayout layout = new GridLayout(2, true);
/* 219 */     curActComp.setLayout(layout);
/*     */ 
/* 221 */     Group curActGrp = new Group(curActComp, 64);
/* 222 */     GridLayout layout1 = new GridLayout(1, false);
/* 223 */     curActGrp.setLayout(layout1);
/*     */ 
/* 225 */     curActGrp.setText("Current Activity");
/*     */ 
/* 227 */     Label lbl1 = new Label(curActGrp, 0);
/* 228 */     lbl1.setText(this.drawingLayer.getActiveProduct().getName());
/*     */ 
/* 230 */     Group curLyrGrp = new Group(curActComp, 64);
/* 231 */     GridLayout layout2 = new GridLayout(1, false);
/* 232 */     curLyrGrp.setLayout(layout2);
/*     */ 
/* 234 */     curLyrGrp.setText("Active Layer");
/*     */ 
/* 236 */     Label lbl2 = new Label(curLyrGrp, 0);
/* 237 */     lbl2.setText(this.drawingLayer.getActiveLayer().getName());
/*     */ 
/* 240 */     Composite selActComp = new Composite(this.top, 0);
/* 241 */     selActComp.setLayoutData(new GridData(16384, -1, true, false));
/*     */ 
/* 243 */     GridLayout layout3 = new GridLayout(1, false);
/* 244 */     selActComp.setLayout(layout3);
/*     */ 
/* 246 */     Group selActGrp = new Group(selActComp, 64);
/* 247 */     GridLayout layout4 = new GridLayout(1, false);
/* 248 */     selActGrp.setLayout(layout4);
/*     */ 
/* 250 */     selActGrp.setText("Selected File Activity and Filename");
/*     */ 
/* 252 */     Label lbl3 = new Label(selActGrp, 0);
/* 253 */     lbl3.setText(this.incomingActivity.getName() + " { " + this.incomingFile + " }");
/*     */ 
/* 256 */     Composite layersComp = new Composite(this.top, 0);
/* 257 */     layersComp.setLayoutData(new GridData(16384, -1, true, false));
/*     */ 
/* 259 */     GridLayout layout5 = new GridLayout(1, false);
/* 260 */     layersComp.setLayout(layout5);
/*     */ 
/* 262 */     Group layersGrp = new Group(layersComp, 64);
/* 263 */     GridLayout layout6 = new GridLayout(2, false);
/* 264 */     layersGrp.setLayout(layout6);
/*     */ 
/* 266 */     layersGrp.setText("Incoming Layers");
/*     */     Combo layerActCombo;
/*     */     boolean sameLayerExist;
/* 268 */     for (Layer lyr : this.incomingActivity.getLayers()) {
/* 269 */       Label lbl4 = new Label(layersGrp, 0);
/* 270 */       lbl4.setText(lyr.getName());
/*     */ 
/* 272 */       layerActCombo = new Combo(layersGrp, 12);
/*     */ 
/* 274 */       sameLayerExist = layerExist(lyr.getName());
/* 275 */       layerActCombo.add("Take no action                                                                                   ");
/* 276 */       layerActCombo.add("Add as layer '" + lyr.getName() + "'");
/*     */ 
/* 278 */       if (sameLayerExist) {
/* 279 */         layerActCombo.add("Replace content of '" + lyr.getName() + "'");
/*     */       }
/*     */ 
/* 282 */       layerActCombo.add("Replace content of 'ActiveLayer'");
/* 283 */       layerActCombo.add("Replace content of 'ActiveLayer' and rename to '" + lyr.getName() + "'");
/*     */ 
/* 285 */       if (sameLayerExist) {
/* 286 */         layerActCombo.add("Merge content into '" + lyr.getName() + "'");
/*     */       }
/*     */ 
/* 289 */       layerActCombo.add("Merge content into 'ActiveLayer'");
/* 290 */       layerActCombo.add("Merge content into 'ActiveLayer' and rename to '" + lyr.getName() + "'");
/*     */ 
/* 292 */       layerActCombo.select(0);
/*     */ 
/* 294 */       this.layerComboMap.put(lyr, layerActCombo);
/*     */     }
/*     */ 
/* 298 */     if (this.incomingActivity.getLayers().size() > 1) {
/* 299 */       Label lbl4 = new Label(layersGrp, 0);
/* 300 */       lbl4.setText("Bulk Action");
/*     */ 
/* 302 */       Combo bulkCombo = new Combo(layersGrp, 12);
/*     */       String[] arrayOfString;
/* 304 */       sameLayerExist = (arrayOfString = BulkActionNames).length; for (layerActCombo = 0; layerActCombo < sameLayerExist; layerActCombo++) { String str = arrayOfString[layerActCombo];
/* 305 */         bulkCombo.add(str);
/*     */       }
/*     */ 
/* 308 */       bulkCombo.addSelectionListener(new SelectionAdapter()
/*     */       {
/*     */         public void widgetSelected(SelectionEvent e)
/*     */         {
/* 312 */           PgenLayerMergeDialog.this.performBulkActions(((Combo)e.widget).getSelectionIndex());
/*     */         }
/*     */       });
/* 317 */       bulkCombo.select(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean layerExist(String layerName)
/*     */   {
/* 326 */     return this.drawingLayer.getActiveProduct().getLayer(layerName) != null;
/*     */   }
/*     */ 
/*     */   private void performBulkActions(int actionNum)
/*     */   {
/* 333 */     switch (actionNum) {
/*     */     case 0:
/* 335 */       break;
/*     */     case 1:
/* 338 */       for (Combo cmb : this.layerComboMap.values()) {
/* 339 */         cmb.select(0);
/*     */       }
/* 341 */       break;
/*     */     case 2:
/* 344 */       for (Combo cmb : this.layerComboMap.values()) {
/* 345 */         cmb.select(1);
/*     */       }
/*     */ 
/* 348 */       break;
/*     */     case 3:
/* 351 */       for (Combo cmb : this.layerComboMap.values()) {
/* 352 */         if (cmb.getItemCount() > 6) {
/* 353 */           cmb.select(2);
/*     */         }
/*     */       }
/*     */ 
/* 357 */       break;
/*     */     case 4:
/* 359 */       for (Combo cmb : this.layerComboMap.values()) {
/* 360 */         if (cmb.getItemCount() > 6) {
/* 361 */           cmb.select(5);
/*     */         }
/*     */       }
/*     */ 
/* 365 */       break;
/*     */     case 5:
/* 367 */       ??? = this.layerComboMap.values().iterator();
/*     */       while (true) { Combo cmb = (Combo)???.next();
/* 368 */         if (cmb.getItemCount() > 6) {
/* 369 */           cmb.select(6);
/*     */         }
/*     */         else
/* 372 */           cmb.select(4);
/* 367 */         if (!???.hasNext())
/*     */         {
/* 376 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void addSeparator(Composite top)
/*     */   {
/* 389 */     GridData gd = new GridData(768);
/* 390 */     Label sepLbl = new Label(top, 258);
/* 391 */     sepLbl.setLayoutData(gd);
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 399 */     if (conflictActions()) {
/* 400 */       return;
/*     */     }
/*     */ 
/* 403 */     Product activeAct = this.drawingLayer.getActiveProduct();
/* 404 */     Layer activeLayer = this.drawingLayer.getActiveLayer();
/*     */ 
/* 406 */     for (Layer lyr : this.layerComboMap.keySet())
/*     */     {
/* 408 */       int index = getLayerActionIndex((Combo)this.layerComboMap.get(lyr));
/*     */ 
/* 410 */       switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$controls$PgenLayerMergeDialog$LayerActions()[LayerActions.values()[index].ordinal()])
/*     */       {
/*     */       case 1:
/* 413 */         break;
/*     */       case 2:
/* 416 */         addLayer(activeAct, lyr);
/* 417 */         break;
/*     */       case 3:
/* 420 */         replaceMergeLayer(activeAct.getLayer(lyr.getName()), lyr, true, null);
/* 421 */         break;
/*     */       case 4:
/* 424 */         replaceMergeLayer(activeLayer, lyr, true, null);
/* 425 */         break;
/*     */       case 5:
/* 428 */         replaceMergeLayer(activeLayer, lyr, true, activeAct);
/* 429 */         break;
/*     */       case 6:
/* 432 */         replaceMergeLayer(activeAct.getLayer(lyr.getName()), lyr, false, null);
/* 433 */         break;
/*     */       case 7:
/* 436 */         replaceMergeLayer(this.drawingLayer.getActiveLayer(), lyr, false, null);
/* 437 */         break;
/*     */       case 8:
/* 440 */         replaceMergeLayer(activeLayer, lyr, false, activeAct);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 450 */     close();
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 458 */     super.cancelPressed();
/*     */   }
/*     */ 
/*     */   private void addLayer(Product prd, Layer newLayer)
/*     */   {
/* 467 */     if ((prd != null) && (newLayer != null)) {
/* 468 */       newLayer.setName(findUniqueLayerName(prd, newLayer));
/* 469 */       prd.addLayer(newLayer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String findUniqueLayerName(Product prd, Layer newLayer)
/*     */   {
/* 479 */     String lyrName = "New Layer";
/*     */ 
/* 481 */     if ((prd != null) && (newLayer != null)) {
/* 482 */       lyrName = newLayer.getName();
/* 483 */       int ii = 1;
/* 484 */       while (prd.getLayer(lyrName) != null) {
/* 485 */         lyrName = new String(lyrName + " " + ii);
/*     */       }
/*     */     }
/*     */ 
/* 489 */     return lyrName;
/*     */   }
/*     */ 
/*     */   private void replaceMergeLayer(Layer existingLayer, Layer newLayer, boolean replace, Product prd)
/*     */   {
/* 503 */     if ((existingLayer != null) && (newLayer != null))
/*     */     {
/* 505 */       if (replace) existingLayer.clear();
/*     */ 
/* 507 */       existingLayer.add(newLayer.getDrawables());
/* 508 */       mergeOutlooks(existingLayer);
/*     */ 
/* 510 */       if (prd != null)
/* 511 */         existingLayer.setName(findUniqueLayerName(prd, newLayer));
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getLayerActionIndex(Combo lyrCombo)
/*     */   {
/* 522 */     int index = lyrCombo.getSelectionIndex();
/* 523 */     if (lyrCombo.getItemCount() < LayerActions.values().length) {
/* 524 */       if ((index == 2) || (index == 3)) {
/* 525 */         index++;
/*     */       }
/* 527 */       else if (index > 3) {
/* 528 */         index += 2;
/*     */       }
/*     */     }
/*     */ 
/* 532 */     return index;
/*     */   }
/*     */ 
/*     */   private boolean conflictActions()
/*     */   {
/* 547 */     boolean conflict = false;
/*     */ 
/* 549 */     ArrayList replacingLayers = new ArrayList();
/* 550 */     ArrayList renamingLayers = new ArrayList();
/*     */     int index;
/* 552 */     for (Layer lyr : this.layerComboMap.keySet())
/*     */     {
/* 554 */       index = getLayerActionIndex((Combo)this.layerComboMap.get(lyr));
/*     */ 
/* 556 */       switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$controls$PgenLayerMergeDialog$LayerActions()[LayerActions.values()[index].ordinal()])
/*     */       {
/*     */       case 1:
/* 559 */         break;
/*     */       case 2:
/* 562 */         break;
/*     */       case 3:
/* 565 */         if (this.drawingLayer.getActiveLayer() == 
/* 566 */           this.drawingLayer.getActiveProduct().getLayer(lyr.getName())) {
/* 567 */           replacingLayers.add(lyr.getName());
/*     */         }
/* 569 */         break;
/*     */       case 4:
/* 572 */         replacingLayers.add(lyr.getName());
/*     */ 
/* 574 */         break;
/*     */       case 5:
/* 578 */         replacingLayers.add(lyr.getName());
/* 579 */         renamingLayers.add(lyr.getName());
/*     */ 
/* 581 */         break;
/*     */       case 6:
/* 584 */         break;
/*     */       case 7:
/* 586 */         break;
/*     */       case 8:
/* 589 */         renamingLayers.add(lyr.getName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 600 */     String msg = "The following conflicts have been found for the active layer '" + 
/* 601 */       this.drawingLayer.getActiveLayer().getName() + "'. \nPlease fix before proceed:\n\n";
/* 602 */     if (replacingLayers.size() > 1) {
/* 603 */       conflict = true;
/* 604 */       msg = msg + "1. " + replacingLayers.size() + " layers (";
/* 605 */       for (String st : replacingLayers) {
/* 606 */         msg = msg + st;
/* 607 */         if (st != replacingLayers.get(replacingLayers.size() - 1)) {
/* 608 */           msg = msg + ", ";
/*     */         }
/*     */       }
/*     */ 
/* 612 */       msg = msg + ") are selected to replace the content of the active layer.\n\n";
/* 613 */       msg = msg + "But only one layer is allowed and others should use 'Merge'.\n\n";
/*     */     }
/*     */ 
/* 616 */     if (renamingLayers.size() > 1) {
/* 617 */       conflict = true;
/* 618 */       msg = msg + "2. Layers (";
/* 619 */       for (String st : renamingLayers) {
/* 620 */         msg = msg + st;
/* 621 */         if (st != renamingLayers.get(renamingLayers.size() - 1)) {
/* 622 */           msg = msg + ", ";
/*     */         }
/*     */       }
/*     */ 
/* 626 */       msg = msg + ") are selected to rename the active layer.\n\n";
/* 627 */       msg = msg + "But only one layer is allowed to be selected to do so.\n\n";
/*     */     }
/*     */ 
/* 630 */     if (conflict) {
/* 631 */       MessageDialog confirmOpen = new MessageDialog(
/* 632 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 633 */         "Conflicting actions to import layers", null, 
/* 634 */         msg, 
/* 635 */         4, new String[] { "Ok" }, 0);
/*     */ 
/* 637 */       confirmOpen.open();
/*     */     }
/*     */ 
/* 640 */     return conflict;
/*     */   }
/*     */ 
/*     */   private void mergeOutlooks(Layer layer)
/*     */   {
/* 652 */     ArrayList otlkList = new ArrayList();
/*     */ 
/* 657 */     for (AbstractDrawableComponent adc : layer.getDrawables()) {
/* 658 */       if ((adc instanceof Outlook)) {
/* 659 */         Outlook look = (Outlook)adc;
/*     */ 
/* 661 */         boolean found = false;
/* 662 */         for (ArrayList aList : otlkList) {
/* 663 */           if (look.getOutlookType().equalsIgnoreCase(((Outlook)aList.get(0)).getOutlookType())) {
/* 664 */             aList.add(look);
/* 665 */             found = true;
/*     */           }
/*     */         }
/*     */ 
/* 669 */         if (!found) {
/* 670 */           ArrayList oList = new ArrayList();
/* 671 */           oList.add(look);
/* 672 */           otlkList.add(oList);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 679 */     for (ArrayList tList : otlkList)
/* 680 */       if (tList.size() > 1)
/* 681 */         for (int ii = 1; ii < tList.size(); ii++) {
/* 682 */           Iterator it = ((Outlook)tList.get(ii)).getComponentIterator();
/* 683 */           while (it.hasNext()) {
/* 684 */             ((Outlook)tList.get(0)).add((AbstractDrawableComponent)it.next());
/*     */           }
/* 686 */           layer.remove((AbstractDrawableComponent)tList.get(ii));
/*     */         }
/*     */   }
/*     */ 
/*     */   private static enum LayerActions
/*     */   {
/*  66 */     NO_ACTION, 
/*  67 */     ADD_AS_NEW_LAYER, 
/*  68 */     REPLACE_LIKE_NAME_LAYER, 
/*  69 */     REPLACE_ACTIVE_LAYER, 
/*  70 */     REPLACE_RENAME_ACTIVE_LAYER, 
/*  71 */     MERGE_INTO_LIKE_NAME_LAYER, 
/*  72 */     MERGE_INTO_ACTIVE_LAYER, 
/*  73 */     MERGE_RENAME_ACTIVE_LAYER;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.PgenLayerMergeDialog
 * JD-Core Version:    0.6.2
 */