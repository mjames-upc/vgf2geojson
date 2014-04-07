/*      */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.GeometryFactory;
/*      */ import com.vividsolutions.jts.geom.Point;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlgFactory;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.ContoursAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.GfaAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.JetAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.LabeledSymbolAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.OutlookAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.SymbolAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TextAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TrackAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TrackExtrapPointInfoDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchBoxAttrDlg;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourCircle;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourMinmax;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ComboSymbol;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetBarb;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetText;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Track;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.Tcm;
/*      */ import gov.noaa.nws.ncep.ui.pgen.filter.AcceptFilter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaReducePoint;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.IGfa;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.TCAElement;
/*      */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*      */ import java.awt.Color;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ import org.geotools.referencing.GeodeticCalculator;
/*      */ 
/*      */ public class PgenSelectHandler extends InputHandlerDefaultImpl
/*      */ {
/*      */   protected AbstractEditor mapEditor;
/*      */   protected PgenResource pgenrsc;
/*      */   protected AttrDlg attrDlg;
/*      */   protected AbstractPgenTool tool;
/*      */   private boolean preempt;
/*   92 */   private boolean dontMove = false;
/*      */ 
/*   96 */   TrackExtrapPointInfoDlg trackExtrapPointInfoDlg = null;
/*      */ 
/*  101 */   protected boolean ptSelected = false;
/*      */   String pgenType;
/*  111 */   protected int ptIndex = 0;
/*      */ 
/*  116 */   protected MultiPointElement ghostEl = null;
/*      */ 
/*  121 */   protected Color ghostColor = new Color(255, 255, 255);
/*      */ 
/*  126 */   protected Coordinate oldLoc = null;
/*      */ 
/*  128 */   protected Coordinate firstDown = null;
/*      */ 
/*  134 */   protected Coordinate tempLoc = null;
/*      */ 
/*  139 */   protected int inOut = 1;
/*      */ 
/*  141 */   protected boolean simulate = false;
/*      */ 
/*      */   public PgenSelectHandler(AbstractPgenTool tool, AbstractEditor mapEditor, PgenResource resource, AttrDlg attrDlg)
/*      */   {
/*  151 */     this.mapEditor = mapEditor;
/*  152 */     this.pgenrsc = resource;
/*  153 */     this.attrDlg = attrDlg;
/*  154 */     this.tool = tool;
/*      */   }
/*      */ 
/*      */   public boolean handleMouseDown(int anX, int aY, int button)
/*      */   {
/*  166 */     if (!this.tool.isResourceEditable()) return false;
/*      */ 
/*  168 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/*  169 */     if ((loc == null) || (this.shiftDown) || (this.simulate)) return false;
/*  170 */     this.preempt = false;
/*      */ 
/*  172 */     if (button == 1)
/*      */     {
/*  175 */       if (this.pgenrsc.getSelectedDE() == null) this.ptSelected = false;
/*      */ 
/*  178 */       if ((this.ptSelected) || (this.pgenrsc.getSelectedDE() != null)) {
/*  179 */         this.dontMove = false;
/*  180 */         this.preempt = true;
/*      */ 
/*  182 */         if (((this.pgenrsc.getSelectedDE() instanceof SinglePointElement)) && 
/*  183 */           (this.pgenrsc.getDistance(this.pgenrsc.getSelectedDE(), loc) > this.pgenrsc.getMaxDistToSelect())) {
/*  184 */           this.ptSelected = false;
/*      */         }
/*      */ 
/*  187 */         if ((!(this.pgenrsc.getSelectedDE() instanceof SinglePointElement)) && (!this.ptSelected)) {
/*  188 */           this.firstDown = loc;
/*      */         }
/*      */ 
/*  191 */         return false;
/*      */       }
/*      */ 
/*  195 */       DrawableElement elSelected = this.pgenrsc.getNearestElement(loc);
/*      */ 
/*  197 */       if ((elSelected instanceof SinglePointElement)) {
/*  198 */         this.ptSelected = true;
/*      */       }
/*      */ 
/*  203 */       AbstractDrawableComponent adc = null;
/*  204 */       if ((elSelected != null) && (elSelected.getParent() != null) && 
/*  205 */         (!elSelected.getParent().getName().equals("Default")))
/*      */       {
/*  207 */         adc = this.pgenrsc.getNearestComponent(loc, new AcceptFilter(), true);
/*      */       }
/*      */ 
/*  212 */       if (elSelected == null) return false;
/*  213 */       this.preempt = true;
/*      */ 
/*  218 */       String pgCategory = null;
/*      */ 
/*  220 */       if ((elSelected instanceof TCAElement)) {
/*  221 */         PgenUtil.loadTCATool(elSelected);
/*      */       }
/*  223 */       else if ((elSelected instanceof WatchBox)) {
/*  224 */         WatchBoxAttrDlg.getInstance(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()).setWatchBox((WatchBox)elSelected);
/*  225 */         PgenUtil.loadWatchBoxModifyTool(elSelected);
/*      */       }
/*  227 */       else if ((elSelected instanceof Tcm)) {
/*  228 */         PgenUtil.loadTcmTool(elSelected);
/*      */       }
/*      */ 
/*  233 */       if ((this.tool instanceof PgenContoursTool))
/*      */       {
/*  235 */         DECollection dec = ((PgenContoursTool)this.tool).getCurrentContour();
/*  236 */         if (dec != null) {
/*  237 */           elSelected = this.pgenrsc.getNearestElement(loc, (Contours)dec);
/*  238 */           if ((elSelected instanceof MultiPointElement))
/*      */           {
/*  240 */             this.ptSelected = false;
/*      */           }
/*      */         }
/*      */ 
/*  244 */         pgCategory = "MET";
/*  245 */         this.pgenType = "Contours";
/*      */       }
/*  250 */       else if ((adc != null) && ((adc instanceof Jet)) && ((this.tool instanceof PgenSelectingTool)))
/*      */       {
/*  252 */         if (adc.getPrimaryDE() == elSelected)
/*      */         {
/*  254 */           pgCategory = adc.getPgenCategory();
/*  255 */           this.pgenType = adc.getPgenType();
/*  256 */           ((PgenSelectingTool)this.tool).setJet((Jet)adc);
/*      */         }
/*      */         else {
/*  259 */           pgCategory = elSelected.getPgenCategory();
/*  260 */           this.pgenType = elSelected.getPgenType();
/*  261 */           if (((Jet)adc).getSnapTool() == null) {
/*  262 */             ((Jet)adc).setSnapTool(new PgenSnapJet((IMapDescriptor)this.pgenrsc.getDescriptor(), this.mapEditor, null));
/*      */           }
/*      */         }
/*      */       }
/*  266 */       else if ((adc != null) && ((adc instanceof LabeledLine)) && (
/*  267 */         (elSelected.getParent() == adc) || (elSelected.getParent().getParent() == adc))) {
/*  268 */         PgenUtil.loadLabeledLineModifyTool((LabeledLine)adc);
/*  269 */         this.pgenrsc.removeSelected();
/*      */ 
/*  271 */         Iterator it = adc.createDEIterator();
/*  272 */         while (it.hasNext()) {
/*  273 */           this.pgenrsc.addSelected((AbstractDrawableComponent)it.next());
/*      */         }
/*      */ 
/*  276 */         elSelected = null;
/*      */       }
/*  279 */       else if ((adc != null) && (adc.getName().equalsIgnoreCase("Contours"))) {
/*  280 */         pgCategory = adc.getPgenCategory();
/*  281 */         this.pgenType = adc.getPgenType();
/*  282 */         PgenUtil.loadContoursTool((Contours)adc);
/*      */       }
/*  284 */       else if ((adc != null) && ((elSelected instanceof Line)) && 
/*  285 */         ((adc instanceof Outlook))) {
/*  286 */         this.pgenType = "Outlook";
/*  287 */         pgCategory = adc.getPgenCategory();
/*      */       }
/*  290 */       else if (elSelected != null) {
/*  291 */         pgCategory = elSelected.getPgenCategory();
/*  292 */         this.pgenType = elSelected.getPgenType();
/*      */       }
/*      */ 
/*  297 */       if (elSelected != null) {
/*  298 */         this.pgenrsc.setSelected(elSelected);
/*  299 */         this.dontMove = true;
/*      */       }
/*      */ 
/*  302 */       if (pgCategory != null)
/*      */       {
/*  304 */         if ((this.attrDlg != null) && ((!(this.attrDlg instanceof ContoursAttrDlg)) || (!(this.tool instanceof PgenContoursTool)))) {
/*  305 */           closeAttrDlg(this.attrDlg, elSelected.getPgenType());
/*  306 */           this.attrDlg = null;
/*      */         }
/*      */ 
/*  309 */         if (this.attrDlg == null) {
/*  310 */           this.attrDlg = AttrDlgFactory.createAttrDlg(pgCategory, this.pgenType, 
/*  311 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/*      */         }
/*      */ 
/*  314 */         if (this.attrDlg == null) {
/*  315 */           this.mapEditor.refresh();
/*  316 */           return false;
/*      */         }
/*      */ 
/*  319 */         this.attrDlg.setBlockOnOpen(false);
/*      */ 
/*  321 */         this.attrDlg.setMouseHandlerName("Pgen Select");
/*  322 */         this.attrDlg.setDrawableElement(elSelected);
/*      */ 
/*  324 */         if ((this.attrDlg != null) && (this.attrDlg.getShell() == null)) {
/*  325 */           this.attrDlg.open();
/*  326 */           if ((this.tool instanceof AbstractPgenDrawingTool)) {
/*  327 */             ((AbstractPgenDrawingTool)this.tool).setAttrDlg(this.attrDlg);
/*      */           }
/*      */         }
/*  330 */         this.mapEditor.setFocus();
/*      */ 
/*  332 */         if ((adc != null) && (adc.getName().equalsIgnoreCase("Contours")))
/*      */         {
/*  334 */           ((ContoursAttrDlg)this.attrDlg).setAttrForDlg((Contours)adc);
/*      */ 
/*  336 */           if (elSelected != null) {
/*  337 */             updateContoursAttrDlg(elSelected);
/*  338 */             ((ContoursAttrDlg)this.attrDlg).setSelectMode();
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  343 */           this.attrDlg.setAttrForDlg(elSelected);
/*      */         }
/*      */ 
/*  346 */         if (((elSelected instanceof SinglePointElement)) && 
/*  347 */           (!(elSelected.getParent() instanceof ContourMinmax)))
/*      */         {
/*  349 */           if (((elSelected instanceof Symbol)) || 
/*  350 */             ((elSelected instanceof ComboSymbol)))
/*      */           {
/*  352 */             SymbolAttrDlg sDlg = (SymbolAttrDlg)this.attrDlg;
/*      */ 
/*  354 */             sDlg.setLongitude(((SinglePointElement)elSelected).getLocation().x);
/*  355 */             sDlg.setLatitude(((SinglePointElement)elSelected).getLocation().y);
/*      */ 
/*  357 */             if ((this.attrDlg instanceof LabeledSymbolAttrDlg)) {
/*  358 */               ((LabeledSymbolAttrDlg)this.attrDlg).setLabelChkBox(false);
/*      */             }
/*      */           }
/*      */         }
/*  362 */         else if ((elSelected instanceof Track)) {
/*  363 */           TrackAttrDlg trackAttrDlg = (TrackAttrDlg)this.attrDlg;
/*  364 */           trackAttrDlg.isNewTrack = false;
/*  365 */           trackAttrDlg.initializeTrackAttrDlg((Track)elSelected);
/*  366 */           displayTrackExtrapPointInfoDlg(trackAttrDlg, (Track)elSelected);
/*      */         }
/*      */ 
/*  369 */         this.attrDlg.enableButtons();
/*  370 */         this.attrDlg.setPgenCategory(pgCategory);
/*      */ 
/*  372 */         if ((adc != null) && ((adc instanceof Contours))) {
/*  373 */           this.attrDlg.setPgenType(this.pgenType);
/*      */         }
/*      */         else {
/*  376 */           this.attrDlg.setPgenType(elSelected.getPgenType());
/*      */         }
/*      */ 
/*  379 */         this.attrDlg.setDrawingLayer(this.pgenrsc);
/*  380 */         this.attrDlg.setMapEditor(this.mapEditor);
/*  381 */         if (((this.attrDlg instanceof JetAttrDlg)) && ((this.tool instanceof PgenSelectingTool))) {
/*  382 */           ((JetAttrDlg)this.attrDlg).setJetDrawingTool((PgenSelectingTool)this.tool);
/*  383 */           ((JetAttrDlg)this.attrDlg).updateSegmentPane();
/*  384 */           if (((PgenSelectingTool)this.tool).getJet().getSnapTool() == null) {
/*  385 */             ((PgenSelectingTool)this.tool).getJet().setSnapTool(new PgenSnapJet((IMapDescriptor)this.pgenrsc.getDescriptor(), this.mapEditor, (JetAttrDlg)this.attrDlg));
/*      */           }
/*      */         }
/*  388 */         else if ((adc != null) && ((this.attrDlg instanceof OutlookAttrDlg))) {
/*  389 */           ((OutlookAttrDlg)this.attrDlg).setOtlkType(((Outlook)adc).getOutlookType());
/*  390 */           String lbl = null;
/*  391 */           Iterator it = elSelected.getParent().createDEIterator();
/*  392 */           while (it.hasNext()) {
/*  393 */             DrawableElement de = (DrawableElement)it.next();
/*  394 */             if ((de instanceof Text)) {
/*  395 */               lbl = ((Text)de).getText()[0];
/*  396 */               break;
/*      */             }
/*      */           }
/*  399 */           if (lbl != null) {
/*  400 */             ((OutlookAttrDlg)this.attrDlg).setLabel(lbl);
/*      */           }
/*      */ 
/*  403 */           this.attrDlg.setAttrForDlg(elSelected);
/*  404 */           ((OutlookAttrDlg)this.attrDlg).setAttrForDlg((Outlook)adc);
/*      */         }
/*  406 */         else if ((this.attrDlg instanceof GfaAttrDlg)) {
/*  407 */           ((GfaAttrDlg)this.attrDlg).setOtherTextLastUsed(elSelected.getForecastHours());
/*  408 */           ((GfaAttrDlg)this.attrDlg).redrawHazardSpecificPanel();
/*  409 */           this.attrDlg.setAttrForDlg(elSelected);
/*  410 */           ((GfaAttrDlg)this.attrDlg).enableMoveTextBtn(true);
/*      */         }
/*      */       }
/*      */ 
/*  414 */       this.mapEditor.setFocus();
/*  415 */       this.mapEditor.refresh();
/*      */ 
/*  417 */       return this.preempt;
/*      */     }
/*      */ 
/*  423 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean handleMouseDownMove(int x, int y, int button)
/*      */   {
/*  437 */     if (!this.tool.isResourceEditable()) return false;
/*      */ 
/*  439 */     if (this.shiftDown) return false;
/*  440 */     if ((this.dontMove) && (this.pgenrsc.getSelectedDE() != null)) return true;
/*      */ 
/*  442 */     Coordinate loc = this.mapEditor.translateClick(x, y);
/*      */ 
/*  445 */     DrawableElement tmpEl = this.pgenrsc.getSelectedDE();
/*  446 */     if (PgenUtil.isUnmovable(tmpEl)) return false;
/*      */ 
/*  449 */     if (loc != null) {
/*  450 */       this.tempLoc = loc;
/*      */     }
/*  452 */     if ((loc != null) && (this.inOut == 1))
/*      */     {
/*  454 */       if ((this.pgenrsc.getDistance(tmpEl, loc) > this.pgenrsc.getMaxDistToSelect()) && (!this.ptSelected))
/*      */       {
/*  457 */         if ((this.firstDown != null) && (this.pgenrsc.getDistance(tmpEl, this.firstDown) < this.pgenrsc.getMaxDistToSelect())) {
/*  458 */           this.firstDown = null;
/*      */         }
/*      */         else {
/*  461 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*  465 */     else if ((loc != null) && (this.inOut == 0)) {
/*  466 */       this.inOut = 1;
/*      */     }
/*      */     else {
/*  469 */       if (this.inOut != 0) {
/*  470 */         this.inOut = 0;
/*      */       }
/*  472 */       if (tmpEl == null) {
/*  473 */         return false;
/*      */       }
/*  475 */       this.simulate = true;
/*  476 */       PgenUtil.simulateMouseDown(x, y, button, this.mapEditor);
/*  477 */       this.simulate = false;
/*      */ 
/*  479 */       return true;
/*      */     }
/*      */ 
/*  483 */     if (tmpEl != null) {
/*  484 */       if ((tmpEl instanceof SinglePointElement))
/*      */       {
/*  486 */         this.ptSelected = true;
/*      */ 
/*  488 */         if (this.oldLoc == null) {
/*  489 */           this.oldLoc = new Coordinate(((SinglePointElement)tmpEl).getLocation());
/*      */         }
/*      */ 
/*  493 */         if ((tmpEl instanceof Jet.JetBarb)) {
/*  494 */           ((Jet.JetBarb)tmpEl).setLocationOnly(loc);
/*  495 */           Jet.JetText jt = ((Jet.JetBarb)tmpEl).getFlightLvl();
/*  496 */           if (jt != null) {
/*  497 */             this.pgenrsc.resetElement(jt);
/*      */           }
/*      */         }
/*  500 */         else if ((tmpEl.getParent() != null) && 
/*  501 */           (tmpEl.getParent().getParent() != null) && 
/*  502 */           (tmpEl.getParent().getParent().getName().equalsIgnoreCase("Contours")))
/*      */         {
/*  504 */           this.pgenrsc.resetADC(tmpEl.getParent());
/*      */ 
/*  506 */           ((SinglePointElement)tmpEl).setLocationOnly(loc);
/*  507 */           ContoursAttrDlg cdlg = (ContoursAttrDlg)this.attrDlg;
/*      */ 
/*  513 */           if ((tmpEl instanceof Text)) {
/*  514 */             ((Text)tmpEl).setText(new String[] { cdlg.getLabel() });
/*  515 */             ((Text)tmpEl).setAuto(Boolean.valueOf(false));
/*      */           }
/*      */ 
/*      */         }
/*  519 */         else if (((tmpEl instanceof Text)) && 
/*  520 */           ((tmpEl.getParent() instanceof DECollection)) && 
/*  521 */           (tmpEl.getParent().getPgenCategory() != null) && 
/*  522 */           (tmpEl.getParent().getPgenCategory().equalsIgnoreCase("Front")))
/*      */         {
/*  524 */           String[] text = ((IText)this.attrDlg).getString();
/*      */ 
/*  527 */           if (text.length == 1) {
/*  528 */             StringBuffer lbl = new StringBuffer(((TextAttrDlg)this.attrDlg).getString()[0]);
/*      */ 
/*  530 */             if (lbl.length() > 0) {
/*  531 */               if (lbl.charAt(0) == '[') lbl.deleteCharAt(0);
/*  532 */               if (lbl.charAt(lbl.length() - 1) == ']') lbl.deleteCharAt(lbl.length() - 1); try
/*      */               {
/*  534 */                 Integer.parseInt(lbl.toString());
/*      */ 
/*  536 */                 if (PgenTextDrawingTool.rightOfLine(this.mapEditor, loc, (Line)tmpEl.getParent().getPrimaryDE()) >= 0) {
/*  537 */                   ((TextAttrDlg)this.attrDlg).setText(new String[] { lbl + "]" });
/*      */                 }
/*      */                 else
/*  540 */                   ((TextAttrDlg)this.attrDlg).setText(new String[] { "[" + lbl });
/*      */               }
/*      */               catch (NumberFormatException localNumberFormatException)
/*      */               {
/*      */               }
/*      */             }
/*      */           }
/*  547 */           ((Text)tmpEl).setText(((TextAttrDlg)this.attrDlg).getString());
/*  548 */           ((SinglePointElement)tmpEl).setLocationOnly(loc);
/*      */         }
/*      */         else
/*      */         {
/*  552 */           ((SinglePointElement)tmpEl).setLocationOnly(loc);
/*      */         }
/*      */ 
/*  563 */         this.pgenrsc.resetElement(tmpEl);
/*      */ 
/*  565 */         this.mapEditor.refresh();
/*      */       }
/*      */       else
/*      */       {
/*  569 */         if (this.ptSelected)
/*      */         {
/*  571 */           this.ghostEl.removePoint(this.ptIndex);
/*  572 */           this.ghostEl.addPoint(this.ptIndex, loc);
/*  573 */           if (((this.ghostEl instanceof Gfa)) && (!((Gfa)this.ghostEl).isSnapshot())) {
/*  574 */             ((GfaAttrDlg)this.attrDlg).setEnableStatesButton(true);
/*      */           }
/*  576 */           this.pgenrsc.setGhostLine(this.ghostEl);
/*  577 */           this.mapEditor.refresh();
/*      */         }
/*  580 */         else if ((tmpEl != null) && 
/*  581 */           ((tmpEl instanceof MultiPointElement)))
/*      */         {
/*  584 */           this.ghostEl = ((MultiPointElement)tmpEl.copy());
/*      */ 
/*  586 */           if (this.ghostEl != null) {
/*  587 */             this.ghostEl.setColors(new Color[] { this.ghostColor, new Color(255, 255, 255) });
/*      */ 
/*  589 */             ArrayList points = new ArrayList();
/*  590 */             points.addAll(tmpEl.getPoints());
/*      */ 
/*  592 */             this.ghostEl.setPoints(points);
/*      */ 
/*  594 */             this.ghostEl.setPgenCategory(tmpEl.getPgenCategory());
/*  595 */             this.ghostEl.setPgenType(tmpEl.getPgenType());
/*      */ 
/*  597 */             this.ptIndex = getNearestPtIndex(this.ghostEl, loc);
/*      */ 
/*  600 */             double[] locScreen = this.mapEditor.translateInverseClick(loc);
/*  601 */             double[] pt = this.mapEditor.translateInverseClick((Coordinate)this.ghostEl.getPoints().get(this.ptIndex));
/*      */ 
/*  603 */             Point ptScreen = new GeometryFactory().createPoint(new Coordinate(pt[0], pt[1]));
/*  604 */             double dist = ptScreen.distance(new GeometryFactory().createPoint(new Coordinate(locScreen[0], locScreen[1])));
/*  605 */             dist = 0.0D;
/*  606 */             if (dist <= this.pgenrsc.getMaxDistToSelect()) {
/*  607 */               this.ghostEl.setPoints(points);
/*      */ 
/*  609 */               setGhostLineColorForTrack(this.ghostEl, this.ptIndex);
/*      */ 
/*  611 */               this.ptSelected = true;
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  617 */         this.simulate = true;
/*  618 */         PgenUtil.simulateMouseDown(x, y, button, this.mapEditor);
/*  619 */         this.simulate = false;
/*  620 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*  624 */     if (this.preempt) {
/*  625 */       this.simulate = true;
/*  626 */       PgenUtil.simulateMouseDown(x, y, button, this.mapEditor);
/*  627 */       this.simulate = false;
/*      */     }
/*  629 */     return this.preempt;
/*      */   }
/*      */ 
/*      */   public boolean handleMouseUp(int x, int y, int button)
/*      */   {
/*  641 */     this.firstDown = null;
/*  642 */     if (!this.tool.isResourceEditable()) return false;
/*      */ 
/*  645 */     if ((button == 1) && (this.pgenrsc != null))
/*      */     {
/*  648 */       DrawableElement el = this.pgenrsc.getSelectedDE();
/*      */ 
/*  650 */       if (el != null)
/*      */       {
/*  652 */         DrawableElement newEl = (DrawableElement)el.copy();
/*      */ 
/*  654 */         if ((el instanceof SinglePointElement))
/*      */         {
/*  656 */           if (this.oldLoc != null)
/*      */           {
/*  658 */             this.pgenrsc.resetElement(el);
/*      */ 
/*  660 */             if ((el instanceof Jet.JetBarb)) {
/*  661 */               DECollection dec = (DECollection)el.getParent();
/*  662 */               if ((dec != null) && (dec.getCollectionName().equalsIgnoreCase("WindInfo"))) {
/*  663 */                 DECollection parent = (DECollection)dec.getParent();
/*  664 */                 if ((parent != null) && (parent.getCollectionName().equalsIgnoreCase("jet"))) {
/*  665 */                   Jet oldJet = (Jet)parent;
/*  666 */                   Jet newJet = oldJet.copy();
/*      */ 
/*  670 */                   DECollection newWind = dec.copy();
/*  671 */                   newJet.replace(newJet.getNearestComponent(((SinglePointElement)el).getLocation()), newWind);
/*  672 */                   this.pgenrsc.replaceElement(oldJet, newJet);
/*      */ 
/*  674 */                   newWind.replace(newWind.getNearestComponent(((SinglePointElement)el).getLocation()), newEl);
/*      */ 
/*  677 */                   Iterator it = dec.createDEIterator();
/*  678 */                   while (it.hasNext()) {
/*  679 */                     DrawableElement de = (DrawableElement)it.next();
/*  680 */                     if ((de instanceof SinglePointElement)) {
/*  681 */                       ((SinglePointElement)de).setLocationOnly(this.oldLoc);
/*      */                     }
/*      */                   }
/*  684 */                   this.oldLoc = null;
/*      */                 }
/*      */               }
/*      */             }
/*  688 */             else if ((el.getParent() != null) && 
/*  689 */               ((el.getParent().getParent() instanceof Contours)))
/*      */             {
/*  691 */               this.pgenrsc.resetADC(el.getParent());
/*      */ 
/*  693 */               AbstractDrawableComponent oldAdc = el.getParent();
/*  694 */               Contours oldContours = (Contours)oldAdc.getParent();
/*      */ 
/*  696 */               if (oldContours != null)
/*      */               {
/*  698 */                 Contours newContours = oldContours.copy();
/*  699 */                 AbstractDrawableComponent newAdc = oldAdc.copy();
/*      */ 
/*  701 */                 newContours.replace(newContours.getNearestComponent(((SinglePointElement)el).getLocation()), newAdc);
/*  702 */                 ((DECollection)newAdc).replace(((DECollection)newAdc).getNearestComponent(((SinglePointElement)el).getLocation()), newEl);
/*      */ 
/*  704 */                 if ((newEl instanceof Text)) {
/*  705 */                   ((Text)newEl).setAuto(Boolean.valueOf(false));
/*      */                 }
/*      */ 
/*  708 */                 (newEl.getParent() instanceof ContourMinmax);
/*      */ 
/*  714 */                 this.pgenrsc.replaceElement(oldContours, newContours);
/*  715 */                 ((PgenContoursTool)this.tool).setCurrentContour(newContours);
/*      */ 
/*  717 */                 Iterator it = oldAdc.createDEIterator();
/*  718 */                 while (it.hasNext()) {
/*  719 */                   DrawableElement de = (DrawableElement)it.next();
/*  720 */                   if (de.equals(el)) {
/*  721 */                     ((SinglePointElement)de).setLocationOnly(this.oldLoc);
/*      */                   }
/*      */                 }
/*      */ 
/*  725 */                 this.oldLoc = null;
/*      */               }
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  732 */               this.pgenrsc.replaceElement(el, newEl);
/*  733 */               ((SinglePointElement)el).setLocationOnly(this.oldLoc);
/*      */ 
/*  735 */               this.oldLoc = null;
/*      */ 
/*  737 */               if ((this.attrDlg instanceof SymbolAttrDlg))
/*      */               {
/*  739 */                 ((SymbolAttrDlg)this.attrDlg).setLatitude(((SinglePointElement)newEl).getLocation().y);
/*  740 */                 ((SymbolAttrDlg)this.attrDlg).setLongitude(((SinglePointElement)newEl).getLocation().x);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  745 */             Coordinate loc = this.mapEditor.translateClick(x, y);
/*  746 */             if (loc != null)
/*  747 */               ((SinglePointElement)newEl).setLocation(loc);
/*      */             else
/*  749 */               ((SinglePointElement)newEl).setLocation(this.tempLoc);
/*  750 */             this.pgenrsc.setSelected(newEl);
/*      */           }
/*      */ 
/*      */         }
/*  754 */         else if (this.ptSelected)
/*      */         {
/*  756 */           this.ptSelected = false;
/*      */ 
/*  758 */           if (((this.tool instanceof PgenSelectingTool)) && ((el instanceof Jet.JetLine)) && 
/*  759 */             (((PgenSelectingTool)this.tool).getJet().getPrimaryDE() == el)) {
/*  760 */             Jet newJet = ((PgenSelectingTool)this.tool).getJet().copy();
/*  761 */             this.pgenrsc.replaceElement(((PgenSelectingTool)this.tool).getJet(), newJet);
/*  762 */             newJet.getPrimaryDE().setPoints(this.ghostEl.getPoints());
/*  763 */             ((PgenSelectingTool)this.tool).setJet(newJet);
/*  764 */             this.pgenrsc.setSelected(newJet.getPrimaryDE());
/*      */           }
/*  767 */           else if (((el.getParent() instanceof ContourLine)) || 
/*  768 */             ((el.getParent() instanceof ContourCircle))) {
/*  769 */             editContoursLineNCircle(el, this.ghostEl.getPoints());
/*      */           }
/*  778 */           else if ((!(newEl instanceof Gfa)) || (
/*  779 */             ((newEl instanceof Gfa)) && (((Gfa)this.ghostEl).isValid())))
/*      */           {
/*  781 */             this.pgenrsc.replaceElement(el, newEl);
/*      */ 
/*  789 */             if (("GFA".equalsIgnoreCase(newEl.getPgenType())) && (((IGfa)this.attrDlg).getGfaFcstHr().indexOf("-") > -1)) {
/*  790 */               ArrayList points = this.ghostEl.getPoints();
/*  791 */               int nearest = getNearestPtIndex(this.ghostEl, this.mapEditor.translateClick(x, y));
/*  792 */               Coordinate toSnap = (Coordinate)this.ghostEl.getPoints().get(nearest);
/*  793 */               List tempList = new ArrayList();
/*  794 */               tempList.add(toSnap);
/*  795 */               tempList = SnapUtil.getSnapWithStation(tempList, SnapUtil.VOR_STATION_LIST, 10, 16);
/*  796 */               Coordinate snapped = (Coordinate)tempList.get(0);
/*      */ 
/*  798 */               ((Coordinate)points.get(nearest)).setCoordinate(snapped);
/*  799 */               newEl.setPoints(points);
/*      */             } else {
/*  801 */               newEl.setPoints(this.ghostEl.getPoints());
/*      */             }
/*      */ 
/*  805 */             if ((newEl instanceof Gfa)) {
/*  806 */               GfaReducePoint.WarningForOverThreeLines((Gfa)newEl);
/*  807 */               ((Gfa)newEl).setGfaVorText(Gfa.buildVorText((Gfa)newEl));
/*  808 */               if ((this.attrDlg instanceof GfaAttrDlg)) {
/*  809 */                 ((GfaAttrDlg)this.attrDlg).setVorText(((Gfa)newEl).getGfaVorText());
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  814 */             if (this.attrDlg != null) {
/*  815 */               this.attrDlg.setDrawableElement(newEl);
/*      */             }
/*      */ 
/*  819 */             if (!(this.pgenrsc.getSelectedComp() instanceof DECollection)) {
/*  820 */               this.pgenrsc.setSelected(newEl);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  826 */           if ((newEl instanceof Track)) {
/*  827 */             if (isModifiedPointOneOfTheLastTwoInitPoint(newEl, this.ptIndex))
/*  828 */               ((Track)newEl).calculateExtrapTrackPoints();
/*  829 */             displayTrackExtrapPointInfoDlg((TrackAttrDlg)this.attrDlg, (Track)newEl);
/*  830 */           } else if ((newEl instanceof Gfa)) {
/*  831 */             this.attrDlg.setAttrForDlg(newEl);
/*      */           }
/*      */ 
/*  834 */           this.pgenrsc.removeGhostLine();
/*      */         }
/*      */ 
/*  838 */         this.mapEditor.refresh();
/*      */       }
/*      */ 
/*      */     }
/*  842 */     else if (button == 3)
/*      */     {
/*  845 */       if (this.attrDlg != null)
/*      */       {
/*  847 */         if ((this.attrDlg instanceof ContoursAttrDlg)) {
/*  848 */           if (this.pgenrsc.getSelectedDE() == null) {
/*  849 */             closeAttrDlg(this.attrDlg, this.pgenType);
/*  850 */             this.attrDlg = null;
/*  851 */             PgenUtil.setSelectingMode();
/*      */           }
/*      */         }
/*      */         else {
/*  855 */           closeAttrDlg(this.attrDlg, this.pgenType);
/*  856 */           if ((this.attrDlg instanceof GfaAttrDlg))
/*      */           {
/*  858 */             PgenUtil.setSelectingMode();
/*      */           }
/*  860 */           this.attrDlg = null;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  870 */       if (this.trackExtrapPointInfoDlg != null)
/*  871 */         this.trackExtrapPointInfoDlg.close();
/*  872 */       this.trackExtrapPointInfoDlg = null;
/*      */ 
/*  874 */       this.pgenrsc.removeGhostLine();
/*  875 */       this.ptSelected = false;
/*  876 */       this.pgenrsc.removeSelected();
/*  877 */       this.mapEditor.refresh();
/*      */     }
/*      */ 
/*  883 */     return false;
/*      */   }
/*      */ 
/*      */   private void setGhostLineColorForTrack(MultiPointElement multiPointElement, int nearestPointIndex)
/*      */   {
/*  907 */     if ((multiPointElement == null) || (!(multiPointElement instanceof Track))) {
/*  908 */       return;
/*      */     }
/*  910 */     Track track = (Track)multiPointElement;
/*  911 */     int initialTrackPointSize = 0;
/*  912 */     if (track.getInitialPoints() != null)
/*  913 */       initialTrackPointSize = track.getInitialPoints().length;
/*  914 */     if (isInitialPointSelected(initialTrackPointSize, nearestPointIndex))
/*  915 */       track.setInitialColor(new Color(255, 255, 255));
/*      */     else
/*  917 */       track.setExtrapColor(new Color(255, 255, 255));
/*      */   }
/*      */ 
/*      */   private boolean isInitialPointSelected(int initialPointSize, int nearestPointIndex) {
/*  921 */     if (nearestPointIndex < initialPointSize)
/*  922 */       return true;
/*  923 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isModifiedPointOneOfTheLastTwoInitPoint(DrawableElement drawableElement, int nearestPointIndex)
/*      */   {
/*  928 */     boolean isOneOfTheLastTwoInitPoint = false;
/*      */ 
/*  932 */     if ((drawableElement == null) || (!(drawableElement instanceof Track))) {
/*  933 */       return isOneOfTheLastTwoInitPoint;
/*      */     }
/*  935 */     Track track = (Track)drawableElement;
/*  936 */     int initialTrackPointSize = 0;
/*  937 */     if (track.getInitialPoints() != null)
/*  938 */       initialTrackPointSize = track.getInitialPoints().length;
/*  939 */     if ((nearestPointIndex == initialTrackPointSize - 1) || 
/*  940 */       (nearestPointIndex == initialTrackPointSize - 2)) {
/*  941 */       isOneOfTheLastTwoInitPoint = true;
/*      */     }
/*  943 */     return isOneOfTheLastTwoInitPoint;
/*      */   }
/*      */ 
/*      */   protected int getNearestPtIndex(MultiPointElement el, Coordinate pt)
/*      */   {
/*  954 */     int ptId = 0;
/*  955 */     double minDistance = -1.0D;
/*      */ 
/*  957 */     GeodeticCalculator gc = new GeodeticCalculator(this.pgenrsc.getCoordinateReferenceSystem());
/*  958 */     gc.setStartingGeographicPoint(pt.x, pt.y);
/*      */ 
/*  960 */     int index = 0;
/*  961 */     for (Coordinate elPoint : el.getPoints())
/*      */     {
/*  963 */       gc.setDestinationGeographicPoint(elPoint.x, elPoint.y);
/*      */ 
/*  965 */       double dist = gc.getOrthodromicDistance();
/*      */ 
/*  967 */       if ((minDistance < 0.0D) || (dist < minDistance))
/*      */       {
/*  969 */         minDistance = dist;
/*  970 */         ptId = index;
/*      */       }
/*      */ 
/*  974 */       index++;
/*      */     }
/*      */ 
/*  978 */     return ptId;
/*      */   }
/*      */ 
/*      */   public void closeDlg()
/*      */   {
/*  983 */     if (this.attrDlg != null) this.attrDlg.close();
/*      */   }
/*      */ 
/*      */   private void displayTrackExtrapPointInfoDlg(TrackAttrDlg attrDlgObject, Track trackObject)
/*      */   {
/*  989 */     if (attrDlgObject == null)
/*  990 */       return;
/*  991 */     TrackExtrapPointInfoDlg extrapPointInfoDlg = attrDlgObject.getTrackExtrapPointInfoDlg();
/*  992 */     if (extrapPointInfoDlg != null) {
/*  993 */       extrapPointInfoDlg.close();
/*      */     } else {
/*  995 */       extrapPointInfoDlg = (TrackExtrapPointInfoDlg)AttrDlgFactory.createAttrDlg("TRACK_EXTRA_POINTS_INFO", 
/*  996 */         this.pgenType, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/*  997 */       attrDlgObject.setTrackExtrapPointInfoDlg(extrapPointInfoDlg);
/*  998 */       this.trackExtrapPointInfoDlg = extrapPointInfoDlg;
/*      */     }
/*      */ 
/* 1005 */     extrapPointInfoDlg.setBlockOnOpen(false);
/* 1006 */     extrapPointInfoDlg.open();
/*      */ 
/* 1008 */     extrapPointInfoDlg.setTrack(trackObject, attrDlgObject.getUnitComboSelectedIndex(), 
/* 1009 */       attrDlgObject.getRoundComboSelectedIndex(), attrDlgObject.getRoundDirComboSelectedIndex());
/*      */ 
/* 1011 */     extrapPointInfoDlg.setBlockOnOpen(true);
/*      */   }
/*      */ 
/*      */   private boolean isTrackElement(DrawableType drawableType)
/*      */   {
/* 1016 */     if (drawableType == DrawableType.TRACK)
/* 1017 */       return true;
/* 1018 */     return false;
/*      */   }
/*      */ 
/*      */   private DrawableType getDrawableType(String pgenTypeString) {
/* 1022 */     if ("STORM_TRACK".equalsIgnoreCase(pgenTypeString))
/* 1023 */       return DrawableType.TRACK;
/* 1024 */     return DrawableType.LINE;
/*      */   }
/*      */ 
/*      */   private boolean closeAttrDlg(AttrDlg attrDlgObject, String pgenTypeString) {
/* 1028 */     if (attrDlgObject == null)
/* 1029 */       return false;
/* 1030 */     if (isTrackElement(getDrawableType(pgenTypeString))) {
/* 1031 */       TrackAttrDlg tempTrackAttrDlg = (TrackAttrDlg)attrDlgObject;
/* 1032 */       TrackExtrapPointInfoDlg tempTrackExtrapPointInfoDlg = tempTrackAttrDlg.getTrackExtrapPointInfoDlg();
/* 1033 */       tempTrackAttrDlg.setTrackExtrapPointInfoDlg(null);
/* 1034 */       this.trackExtrapPointInfoDlg = null;
/* 1035 */       closeTrackExtrapPointInfoDlg(tempTrackExtrapPointInfoDlg);
/*      */     }
/* 1037 */     return attrDlgObject.close();
/*      */   }
/*      */ 
/*      */   private void closeTrackExtrapPointInfoDlg(TrackExtrapPointInfoDlg dlgObject) {
/* 1041 */     if (dlgObject != null)
/* 1042 */       dlgObject.close();
/*      */   }
/*      */ 
/*      */   private void editContoursLineNCircle(DrawableElement el, ArrayList<Coordinate> points)
/*      */   {
/* 1054 */     if (((el.getParent() instanceof ContourLine)) || 
/* 1055 */       ((el.getParent() instanceof ContourCircle)))
/*      */     {
/* 1057 */       Contours oldContours = (Contours)el.getParent().getParent();
/*      */       DrawableElement selElem;
/*      */       DrawableElement selElem;
/* 1060 */       if ((el.getParent() instanceof ContourLine)) {
/* 1061 */         selElem = ((ContourLine)el.getParent()).getLine();
/*      */       }
/*      */       else {
/* 1064 */         selElem = ((ContourCircle)el.getParent()).getCircle();
/*      */       }
/*      */ 
/* 1067 */       if (oldContours != null) {
/* 1068 */         Contours newContours = new Contours();
/*      */ 
/* 1070 */         Iterator it = oldContours.getComponentIterator();
/*      */ 
/* 1072 */         while (it.hasNext())
/*      */         {
/* 1074 */           AbstractDrawableComponent oldAdc = (AbstractDrawableComponent)it.next();
/* 1075 */           AbstractDrawableComponent newAdc = oldAdc.copy();
/*      */ 
/* 1077 */           if (oldAdc.equals(el.getParent()))
/*      */           {
/* 1079 */             if ((el.getParent() instanceof ContourLine)) {
/* 1080 */               ((ContourLine)newAdc).getLine().setPoints(points);
/* 1081 */               selElem = ((ContourLine)newAdc).getLine();
/*      */             }
/*      */             else {
/* 1084 */               ((ContourCircle)newAdc).getCircle().setPoints(points);
/* 1085 */               selElem = ((ContourCircle)newAdc).getCircle();
/*      */             }
/*      */           }
/*      */ 
/* 1089 */           newAdc.setParent(newContours);
/* 1090 */           newContours.add(newAdc);
/*      */         }
/*      */ 
/* 1093 */         newContours.update(oldContours);
/* 1094 */         this.pgenrsc.replaceElement(oldContours, newContours);
/* 1095 */         this.pgenrsc.setSelected(selElem);
/* 1096 */         if (this.attrDlg != null) ((ContoursAttrDlg)this.attrDlg).setCurrentContours(newContours);
/* 1097 */         ((PgenContoursTool)this.tool).setCurrentContour(newContours);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateContoursAttrDlg(DrawableElement elSelected)
/*      */   {
/* 1110 */     AbstractDrawableComponent pele = elSelected.getParent();
/*      */ 
/* 1112 */     if ((pele != null) && ((pele.getParent() instanceof Contours)))
/*      */     {
/* 1114 */       ContoursAttrDlg cdlg = (ContoursAttrDlg)this.attrDlg;
/*      */ 
/* 1116 */       if ((elSelected instanceof Arc))
/*      */       {
/* 1118 */         Text lbl = ((ContourCircle)pele).getLabel();
/* 1119 */         if (lbl != null) {
/* 1120 */           cdlg.setLabel(lbl.getText()[0]);
/* 1121 */           cdlg.setNumOfLabels(1);
/* 1122 */           cdlg.setHideCircleLabel(lbl.getHide().booleanValue());
/*      */         }
/*      */       }
/* 1125 */       else if ((elSelected instanceof Line))
/*      */       {
/* 1127 */         ArrayList lbls = ((ContourLine)pele).getLabels();
/* 1128 */         if ((lbls != null) && (lbls.size() > 0)) {
/* 1129 */           cdlg.setLabel(((Text)lbls.get(0)).getText()[0]);
/*      */         }
/*      */ 
/* 1132 */         cdlg.setNumOfLabels(((ContourLine)pele).getNumOfLabels());
/* 1133 */         cdlg.setClosed(((Line)elSelected).isClosedLine().booleanValue());
/* 1134 */         cdlg.setContourLineType(elSelected.getPgenType());
/*      */       }
/* 1137 */       else if ((elSelected instanceof Symbol)) {
/* 1138 */         Text lbl = ((ContourMinmax)pele).getLabel();
/*      */ 
/* 1140 */         if (lbl != null) {
/* 1141 */           cdlg.setLabel(lbl.getText()[0]);
/* 1142 */           cdlg.setNumOfLabels(1);
/*      */         }
/*      */ 
/*      */       }
/* 1146 */       else if ((elSelected instanceof Text)) {
/* 1147 */         cdlg.setLabel(((Text)elSelected).getText()[0]);
/*      */ 
/* 1149 */         if ((pele instanceof ContourLine))
/*      */         {
/* 1151 */           cdlg.setNumOfLabels(((ContourLine)pele).getNumOfLabels());
/* 1152 */           cdlg.setClosed(((ContourLine)pele).getLine().isClosedLine().booleanValue());
/* 1153 */           cdlg.setContourLineType(((ContourLine)pele).getLine().getPgenType());
/*      */         }
/* 1155 */         else if ((pele instanceof ContourMinmax))
/*      */         {
/* 1157 */           cdlg.setNumOfLabels(1);
/*      */         }
/* 1160 */         else if ((pele instanceof ContourCircle))
/*      */         {
/* 1162 */           cdlg.setNumOfLabels(1);
/* 1163 */           cdlg.setHideCircleLabel(((Text)elSelected).getHide().booleanValue());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public AbstractEditor getMapEditor() {
/* 1170 */     return this.mapEditor;
/*      */   }
/*      */ 
/*      */   public PgenResource getPgenrsc() {
/* 1174 */     return this.pgenrsc;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenSelectHandler
 * JD-Core Version:    0.6.2
 */