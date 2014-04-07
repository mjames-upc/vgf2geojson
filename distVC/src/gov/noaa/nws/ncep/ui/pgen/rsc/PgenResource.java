/*      */ package gov.noaa.nws.ncep.ui.pgen.rsc;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.IDisplayPaneContainer;
/*      */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*      */ import com.raytheon.uf.viz.core.PixelExtent;
/*      */ import com.raytheon.uf.viz.core.drawables.PaintProperties;
/*      */ import com.raytheon.uf.viz.core.drawables.ResourcePair;
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*      */ import com.raytheon.uf.viz.core.map.MapDescriptor;
/*      */ import com.raytheon.uf.viz.core.rsc.AbstractVizResource;
/*      */ import com.raytheon.uf.viz.core.rsc.IResourceDataChanged;
/*      */ import com.raytheon.uf.viz.core.rsc.IResourceDataChanged.ChangeType;
/*      */ import com.raytheon.uf.viz.core.rsc.LoadProperties;
/*      */ import com.raytheon.uf.viz.core.rsc.ResourceList.RemoveListener;
/*      */ import com.raytheon.uf.viz.core.rsc.capabilities.EditableCapability;
/*      */ import com.raytheon.viz.core.gl.IGLTarget;
/*      */ import com.raytheon.viz.ui.cmenu.IContextMenuProvider;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.raytheon.viz.ui.input.EditableManager;
/*      */ import com.raytheon.viz.ui.perspectives.AbstractVizPerspectiveManager;
/*      */ import com.raytheon.viz.ui.perspectives.VizPerspectiveListener;
/*      */ import com.raytheon.viz.ui.tools.AbstractModalTool;
/*      */ import com.raytheon.viz.ui.tools.ModalToolManager;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.CoordinateArrays;
/*      */ import com.vividsolutions.jts.geom.CoordinateList;
/*      */ import com.vividsolutions.jts.geom.GeometryFactory;
/*      */ import com.vividsolutions.jts.geom.LineSegment;
/*      */ import com.vividsolutions.jts.geom.Point;
/*      */ import gov.noaa.nws.ncep.ui.pgen.Activator;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil.PgenMode;
/*      */ import gov.noaa.nws.ncep.ui.pgen.action.PgenAction;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenCommandManager;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenFileNameDisplay;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.AbstractElementContainer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.DisplayElementFactory;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.DisplayProperties;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.ElementContainerFactory;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IDisplayable;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.IJetTools;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.SymbolLocationSet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.Tcm;
/*      */ import gov.noaa.nws.ncep.ui.pgen.filter.AcceptFilter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.filter.CategoryFilter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.filter.ElementFilter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.filter.ElementFilterCollection;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*      */ import gov.noaa.nws.ncep.ui.pgen.layering.PgenLayeringControlDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*      */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductManageDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.BPGeography;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.TCAElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.TropicalCycloneAdvisory;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.AbstractPgenTool;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenSnapJet;
/*      */ import java.awt.Color;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import org.eclipse.core.runtime.IConfigurationElement;
/*      */ import org.eclipse.jface.action.IMenuManager;
/*      */ import org.eclipse.jface.preference.IPreferenceStore;
/*      */ import org.geotools.referencing.GeodeticCalculator;
/*      */ import org.opengis.referencing.crs.CoordinateReferenceSystem;
/*      */ 
/*      */ public class PgenResource extends AbstractVizResource<PgenResourceData, MapDescriptor>
/*      */   implements ResourceList.RemoveListener, IResourceDataChanged, IContextMenuProvider
/*      */ {
/*  160 */   PgenResourceGhost ghost = null;
/*      */ 
/*  165 */   private List<AbstractDrawableComponent> elSelected = null;
/*      */   private ConcurrentHashMap<DrawableElement, AbstractElementContainer> displayMap;
/*  173 */   private HashMap<AbstractDrawableComponent, Symbol> selectedSymbol = null;
/*      */ 
/*  175 */   private List<Integer> ptsSelectedIndex = null;
/*      */ 
/*  177 */   private Color ptsSelectedColor = null;
/*      */   private BufferedImage paneImage;
/*  181 */   private boolean saveOnNextPaint = true;
/*      */   private ElementFilterCollection filters;
/*      */   private CategoryFilter catFilter;
/*      */   private static final String resourceName = "PGEN Resource";
/*      */   private static final double GFA_TEXTBOX_SELECT_SCALE = 2.0D;
/*      */ 
/*      */   protected PgenResource(PgenResourceData resourceData, LoadProperties loadProperties)
/*      */   {
/*  199 */     super(resourceData, loadProperties);
/*  200 */     ((EditableCapability)getCapability(EditableCapability.class)).setEditable(true);
/*      */ 
/*  202 */     resourceData.addChangeListener(this);
/*      */ 
/*  205 */     this.elSelected = new ArrayList();
/*  206 */     this.selectedSymbol = new HashMap();
/*  207 */     this.displayMap = new ConcurrentHashMap();
/*  208 */     this.filters = new ElementFilterCollection();
/*  209 */     setCatFilter(new CategoryFilter("any"));
/*      */ 
/*  212 */     PgenSession.getInstance().setResource(this);
/*      */   }
/*      */ 
/*      */   public void disposeInternal()
/*      */   {
/*  226 */     if (PgenSession.getInstance().getCurrentResource() == this) {
/*  227 */       PgenSession.getInstance().removeResource();
/*      */     }
/*      */ 
/*  232 */     for (AbstractElementContainer disp : this.displayMap.values()) {
/*  233 */       disp.dispose();
/*      */     }
/*  235 */     this.displayMap.clear();
/*      */ 
/*  237 */     ((PgenResourceData)this.resourceData).removeChangeListener(this);
/*      */ 
/*  239 */     closeDialogs();
/*      */   }
/*      */ 
/*      */   public void saveProducts(String filename)
/*      */   {
/*  250 */     if (filename == null) {
/*  251 */       return;
/*      */     }
/*  253 */     ((PgenResourceData)this.resourceData).saveProducts(filename, false);
/*      */   }
/*      */ 
/*      */   public CoordinateReferenceSystem getCoordinateReferenceSystem()
/*      */   {
/*  265 */     if (this.descriptor == null) {
/*  266 */       return null;
/*      */     }
/*  268 */     return ((MapDescriptor)this.descriptor).getCRS();
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  280 */     return "PGEN Resource";
/*      */   }
/*      */ 
/*      */   public PgenCommandManager getCommandMgr()
/*      */   {
/*  290 */     return ((PgenResourceData)this.resourceData).getCommandMgr();
/*      */   }
/*      */ 
/*      */   public String getShortName()
/*      */   {
/*  300 */     return null;
/*      */   }
/*      */ 
/*      */   public void initInternal(IGraphicsTarget target)
/*      */     throws VizException
/*      */   {
/*  312 */     EditableManager.makeEditable(this, 
/*  313 */       ((EditableCapability)getCapability(EditableCapability.class)).isEditable());
/*      */   }
/*      */ 
/*      */   public boolean isApplicable(PixelExtent extent)
/*      */   {
/*  325 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean okToUnload()
/*      */   {
/*  339 */     if (PgenUtil.getPgenMode() == PgenUtil.PgenMode.SINGLE) {
/*  340 */       return false;
/*      */     }
/*  342 */     return true;
/*      */   }
/*      */ 
/*      */   public void paintInternal(IGraphicsTarget target, PaintProperties paintProps)
/*      */     throws VizException
/*      */   {
/*  357 */     IDisplayPaneContainer editor = getResourceContainer();
/*  358 */     if ((editor instanceof AbstractEditor))
/*      */     {
/*  361 */       DisplayElementFactory df = new DisplayElementFactory(target, 
/*  362 */         (IMapDescriptor)this.descriptor);
/*      */ 
/*  364 */       drawProduct(target, paintProps);
/*  365 */       if (this.elSelected != null)
/*  366 */         drawSelected(target, paintProps);
/*  367 */       if (this.ghost != null) {
/*  368 */         this.ghost.draw(target, paintProps, df, (IMapDescriptor)this.descriptor);
/*      */       }
/*      */ 
/*  371 */       if (this.saveOnNextPaint) {
/*  372 */         this.paneImage = target.screenshot();
/*      */ 
/*  377 */         if ((target instanceof IGLTarget))
/*  378 */           ((IGLTarget)target).makeContextCurrent();
/*  379 */         this.saveOnNextPaint = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isProjectable(CoordinateReferenceSystem mapData)
/*      */   {
/*  395 */     return true;
/*      */   }
/*      */ 
/*      */   public void project(CoordinateReferenceSystem mapData)
/*      */     throws VizException
/*      */   {
/*      */     Iterator localIterator2;
/*  410 */     for (Iterator localIterator1 = ((PgenResourceData)this.resourceData).getProductList().iterator(); localIterator1.hasNext(); 
/*  411 */       localIterator2.hasNext())
/*      */     {
/*  410 */       Product prod = (Product)localIterator1.next();
/*  411 */       localIterator2 = prod.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/*      */ 
/*  413 */       Iterator iterator = layer
/*  414 */         .getComponentIterator();
/*  415 */       while (iterator.hasNext()) {
/*  416 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)iterator.next();
/*  417 */         if (adc.getName().equalsIgnoreCase("jet")) {
/*  418 */           IJetTools snapTool = ((Jet)adc).getSnapTool();
/*  419 */           if (snapTool != null)
/*      */           {
/*  421 */             ((PgenSnapJet)snapTool)
/*  422 */               .setMapDescriptor((IMapDescriptor)getDescriptor());
/*  423 */             snapTool.snapJet((Jet)adc);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  431 */     for (AbstractElementContainer disp : this.displayMap.values())
/*  432 */       disp.setMapDescriptor((IMapDescriptor)getDescriptor());
/*      */   }
/*      */ 
/*      */   public void setAutoSaveFilename(String autoSaveFilename)
/*      */   {
/*  443 */     ((PgenResourceData)this.resourceData).setAutoSaveFilename(autoSaveFilename);
/*      */   }
/*      */ 
/*      */   public void setAutosave(boolean autosave)
/*      */   {
/*  451 */     ((PgenResourceData)this.resourceData).setAutosave(autosave);
/*      */   }
/*      */ 
/*      */   private void drawProductOriginal(IGraphicsTarget target, PaintProperties paintProps)
/*      */   {
/*  469 */     int nprds = ((PgenResourceData)this.resourceData).getProductList().size();
/*      */     Iterator localIterator2;
/*  471 */     label316: for (Iterator localIterator1 = ((PgenResourceData)this.resourceData).getProductList().iterator(); localIterator1.hasNext(); 
/*  477 */       localIterator2.hasNext())
/*      */     {
/*  471 */       Product prod = (Product)localIterator1.next();
/*  472 */       if ((!prod.isOnOff()) && (nprds != 1) && 
/*  473 */         (prod != ((PgenResourceData)this.resourceData).getActiveProduct()))
/*      */         break label316;
/*  475 */       int nlyrs = prod.getLayers().size();
/*      */ 
/*  477 */       localIterator2 = prod.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/*      */ 
/*  479 */       if ((layer.isOnOff()) || (nlyrs == 1) || 
/*  480 */         (layer == ((PgenResourceData)this.resourceData).getActiveLayer()))
/*      */       {
/*  482 */         DisplayProperties dprops = new DisplayProperties();
/*  483 */         if (layer != ((PgenResourceData)this.resourceData).getActiveLayer()) {
/*  484 */           dprops.setLayerMonoColor(Boolean.valueOf(layer.isMonoColor()));
/*  485 */           dprops.setLayerColor(layer.getColor());
/*  486 */           dprops.setLayerFilled(Boolean.valueOf(layer.isFilled()));
/*      */         }
/*      */ 
/*  489 */         Iterator iterator = layer
/*  490 */           .createDEIterator();
/*  491 */         while (iterator.hasNext()) {
/*  492 */           DrawableElement el = (DrawableElement)iterator.next();
/*  493 */           if (this.filters.acceptOnce(el)) {
/*  494 */             if (!this.displayMap.containsKey(el)) {
/*  495 */               AbstractElementContainer container = 
/*  496 */                 ElementContainerFactory.createContainer(el, (MapDescriptor)this.descriptor, 
/*  497 */                 target);
/*  498 */               this.displayMap.put(el, container);
/*      */             }
/*  500 */             ((AbstractElementContainer)this.displayMap.get(el)).draw(target, paintProps, 
/*  501 */               dprops);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addProduct(Product prd)
/*      */   {
/*  522 */     ((PgenResourceData)this.resourceData).getProductList().add(prd);
/*      */   }
/*      */ 
/*      */   public void setGhostLine(AbstractDrawableComponent ghost)
/*      */   {
/*  533 */     if (this.ghost == null) {
/*  534 */       this.ghost = new PgenResourceGhost();
/*      */     }
/*      */ 
/*  537 */     this.ghost.setGhostLine(ghost);
/*      */   }
/*      */ 
/*      */   public void removeGhostLine()
/*      */   {
/*  546 */     this.ghost = null;
/*      */   }
/*      */ 
/*      */   public DrawableElement getNearestElement(Coordinate point)
/*      */   {
/*  558 */     DrawableElement nearestGfabyTextBox = getNearestGfaByTextBox(point, 
/*  559 */       this.catFilter);
/*  560 */     DrawableElement nearestElm = getNearestElement(point, this.catFilter);
/*      */ 
/*  563 */     if ((nearestGfabyTextBox != null) && (
/*  564 */       (nearestElm == null) || ((nearestElm instanceof Gfa)))) {
/*  565 */       return nearestGfabyTextBox;
/*      */     }
/*  567 */     return nearestElm;
/*      */   }
/*      */ 
/*      */   public DrawableElement getNearestElement(Coordinate point, ElementFilter filter)
/*      */   {
/*  583 */     DrawableElement nearestElement = null;
/*  584 */     double minDistance = 1.7976931348623157E+308D;
/*      */ 
/*  586 */     Iterator iterator = ((PgenResourceData)this.resourceData).getActiveLayer()
/*  587 */       .createDEIterator();
/*  588 */     while (iterator.hasNext()) {
/*  589 */       DrawableElement element = (DrawableElement)iterator.next();
/*      */ 
/*  591 */       if ((filter.accept(element)) && (this.filters.acceptOnce(element)))
/*      */       {
/*  593 */         if (this.catFilter.accept(element))
/*      */         {
/*  596 */           double dist = getDistance(element, point);
/*  597 */           if (dist < minDistance)
/*      */           {
/*  599 */             minDistance = dist;
/*  600 */             nearestElement = element;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  605 */     if (minDistance < getMaxDistToSelect()) {
/*  606 */       return nearestElement;
/*      */     }
/*  608 */     return null;
/*      */   }
/*      */ 
/*      */   public AbstractDrawableComponent getNearestComponent(Coordinate point)
/*      */   {
/*  621 */     return getNearestComponent(point, new AcceptFilter(), false);
/*      */   }
/*      */ 
/*      */   public AbstractDrawableComponent getNearestComponent(Coordinate point, ElementFilter filter, boolean applyCatFilter)
/*      */   {
/*  634 */     AbstractDrawableComponent nearestComponent = null;
/*  635 */     double minDistance = 1.7976931348623157E+308D;
/*      */ 
/*  637 */     GeodeticCalculator gc = new GeodeticCalculator(((MapDescriptor)this.descriptor).getCRS());
/*      */ 
/*  639 */     gc.setStartingGeographicPoint(point.x, point.y);
/*      */ 
/*  641 */     Iterator iterator = ((PgenResourceData)this.resourceData)
/*  642 */       .getActiveLayer().getComponentIterator();
/*      */ 
/*  644 */     while (iterator.hasNext()) {
/*  645 */       AbstractDrawableComponent comp = (AbstractDrawableComponent)iterator.next();
/*      */ 
/*  647 */       if ((filter.accept(comp)) && (this.filters.acceptOnce(comp)))
/*      */       {
/*  649 */         if ((!applyCatFilter) || 
/*  650 */           (this.catFilter.accept(comp)))
/*      */         {
/*  654 */           double dist = getDistance(comp, point);
/*      */ 
/*  656 */           if (dist < minDistance)
/*      */           {
/*  658 */             minDistance = dist;
/*  659 */             nearestComponent = comp;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  664 */     if (minDistance < getMaxDistToSelect()) {
/*  665 */       return nearestComponent;
/*      */     }
/*  667 */     return null;
/*      */   }
/*      */ 
/*      */   private void drawSelected(IGraphicsTarget target, PaintProperties paintProps)
/*      */   {
/*  673 */     if (!this.elSelected.isEmpty()) {
/*  674 */       DisplayElementFactory df = new DisplayElementFactory(target, 
/*  675 */         (IMapDescriptor)this.descriptor);
/*  676 */       List displayEls = new ArrayList();
/*  677 */       HashMap map = new HashMap();
/*      */       Symbol defaultSymbol;
/*      */       Symbol defaultSymbol;
/*  681 */       if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/*  681 */         .equalsIgnoreCase("IncDec"))
/*  682 */         defaultSymbol = new Symbol(null, new Color[] { Color.MAGENTA }, 
/*  683 */           2.5F, 0.7D, Boolean.valueOf(false), null, "Symbol", "CIRCLE");
/*      */       else {
/*  685 */         defaultSymbol = new Symbol(null, 
/*  686 */           new Color[] { Color.lightGray }, 2.5F, 7.5D, Boolean.valueOf(false), 
/*  687 */           null, "Marker", "DOT");
/*      */       }
/*      */ 
/*  690 */       Symbol selectSymbol = new Symbol(null, 
/*  691 */         new Color[] { getPtsSelectedColor() }, 2.5F, 7.5D, Boolean.valueOf(false), 
/*  692 */         null, "Marker", "DOT");
/*      */ 
/*  694 */       CoordinateList defaultPts = new CoordinateList();
/*  695 */       CoordinateList selectPts = new CoordinateList();
/*      */ 
/*  697 */       for (AbstractDrawableComponent el : this.elSelected)
/*      */       {
/*      */         Coordinate[] pts;
/*  699 */         if (this.selectedSymbol.containsKey(el)) {
/*  700 */           Symbol currSym = (Symbol)this.selectedSymbol.get(el);
/*  701 */           pts = CoordinateArrays.toCoordinateArray(el
/*  702 */             .getPoints());
/*  703 */           if (map.containsKey(currSym))
/*  704 */             ((CoordinateList)map.get(currSym)).add(pts, true);
/*      */           else
/*  706 */             map.put(currSym, new CoordinateList(pts));
/*      */         }
/*      */         else {
/*  709 */           for (Coordinate point : el.getPoints()) {
/*  710 */             int pointIdx = el.getPoints().indexOf(point);
/*  711 */             if (inSelectedIndex(pointIdx))
/*  712 */               selectPts.add(point, true);
/*      */             else {
/*  714 */               defaultPts.add(point, true);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  720 */       if (!defaultPts.isEmpty()) {
/*  721 */         SymbolLocationSet symset = new SymbolLocationSet(defaultSymbol, 
/*  722 */           defaultPts.toCoordinateArray());
/*  723 */         displayEls.addAll(df.createDisplayElements(symset, 
/*  724 */           paintProps));
/*      */       }
/*  726 */       if (!selectPts.isEmpty()) {
/*  727 */         SymbolLocationSet symset = new SymbolLocationSet(selectSymbol, 
/*  728 */           selectPts.toCoordinateArray());
/*  729 */         displayEls.addAll(df.createDisplayElements(symset, 
/*  730 */           paintProps));
/*      */       }
/*  732 */       if (!map.isEmpty()) {
/*  733 */         for (Symbol sym : map.keySet()) {
/*  734 */           SymbolLocationSet symset = new SymbolLocationSet(sym, 
/*  735 */             ((CoordinateList)map
/*  735 */             .get(sym)).toCoordinateArray());
/*  736 */           displayEls.addAll(df.createDisplayElements(
/*  737 */             symset, paintProps));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  742 */       for (IDisplayable each : displayEls) {
/*  743 */         each.draw(target, paintProps);
/*  744 */         each.dispose();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean inSelectedIndex(int pointIdx) {
/*  750 */     if ((this.ptsSelectedIndex != null) && (!this.ptsSelectedIndex.isEmpty())) {
/*  751 */       return this.ptsSelectedIndex.contains(new Integer(pointIdx));
/*      */     }
/*  753 */     return false;
/*      */   }
/*      */ 
/*      */   public void setSelected(AbstractDrawableComponent comp)
/*      */   {
/*  806 */     this.elSelected.clear();
/*  807 */     if (comp != null)
/*  808 */       this.elSelected.add(comp);
/*      */   }
/*      */ 
/*      */   public void setSelected(List<AbstractDrawableComponent> adcList)
/*      */   {
/*  814 */     this.elSelected.clear();
/*  815 */     if (adcList != null)
/*  816 */       this.elSelected.addAll(adcList);
/*      */   }
/*      */ 
/*      */   public void addSelected(AbstractDrawableComponent adc)
/*      */   {
/*  826 */     this.elSelected.add(adc);
/*      */   }
/*      */ 
/*      */   public void addSelected(List<? extends AbstractDrawableComponent> adcList)
/*      */   {
/*  835 */     this.elSelected.addAll(adcList);
/*      */   }
/*      */ 
/*      */   public void removeSelected(AbstractDrawableComponent adc)
/*      */   {
/*  844 */     if (this.elSelected.contains(adc)) {
/*  845 */       this.elSelected.remove(adc);
/*  846 */       removeSelectedSymbol(adc);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeSelected()
/*      */   {
/*  856 */     this.elSelected.clear();
/*  857 */     clearSelectedSymbol();
/*      */ 
/*  859 */     removePtsSelected();
/*      */   }
/*      */ 
/*      */   public DrawableElement getSelectedDE()
/*      */   {
/*  870 */     if (this.elSelected.isEmpty()) {
/*  871 */       return null;
/*      */     }
/*  873 */     return ((AbstractDrawableComponent)this.elSelected.get(0)).getPrimaryDE();
/*      */   }
/*      */ 
/*      */   public AbstractDrawableComponent getSelectedComp()
/*      */   {
/*  884 */     if (this.elSelected.isEmpty()) {
/*  885 */       return null;
/*      */     }
/*  887 */     return (AbstractDrawableComponent)this.elSelected.get(0);
/*      */   }
/*      */ 
/*      */   public List<AbstractDrawableComponent> getAllSelected()
/*      */   {
/*  895 */     return this.elSelected;
/*      */   }
/*      */ 
/*      */   public List<Product> getProducts()
/*      */   {
/*  903 */     return ((PgenResourceData)this.resourceData).getProductList();
/*      */   }
/*      */ 
/*      */   public void replaceElement(AbstractDrawableComponent old, AbstractDrawableComponent newde)
/*      */   {
/*  922 */     resetADC(old);
/*      */ 
/*  924 */     ((PgenResourceData)this.resourceData).replaceElement(old, newde);
/*      */   }
/*      */ 
/*      */   public void replaceElements(List<AbstractDrawableComponent> old, List<AbstractDrawableComponent> newde)
/*      */   {
/*  942 */     for (AbstractDrawableComponent adc : old) {
/*  943 */       resetADC(adc);
/*      */     }
/*      */ 
/*  946 */     DECollection parent = null;
/*  947 */     if ((old != null) && (!old.isEmpty())) {
/*  948 */       parent = (DECollection)((AbstractDrawableComponent)old.get(0)).getParent();
/*      */     }
/*      */ 
/*  951 */     ((PgenResourceData)this.resourceData).replaceElements(parent, old, newde);
/*      */   }
/*      */ 
/*      */   public void replaceElements(DECollection parent, List<AbstractDrawableComponent> old, List<AbstractDrawableComponent> newde)
/*      */   {
/*  975 */     for (AbstractDrawableComponent adc : old) {
/*  976 */       resetADC(adc);
/*      */     }
/*      */ 
/*  979 */     ((PgenResourceData)this.resourceData).replaceElements(parent, old, newde);
/*      */   }
/*      */ 
/*      */   public void replaceProduct(List<Product> prds)
/*      */   {
/*  987 */     for (Layer layer : ((PgenResourceData)this.resourceData).getActiveProduct().getLayers()) {
/*  988 */       resetADC(layer);
/*      */     }
/*      */ 
/*  991 */     ((PgenResourceData)this.resourceData).replaceProduct(prds);
/*      */   }
/*      */ 
/*      */   public void addProduct(List<Product> prds)
/*      */   {
/* 1000 */     ((PgenResourceData)this.resourceData).addProduct(prds);
/*      */   }
/*      */ 
/*      */   public void appendProduct(List<Product> prds)
/*      */   {
/* 1009 */     ((PgenResourceData)this.resourceData).appendProduct(prds);
/*      */   }
/*      */ 
/*      */   public void removeElement(AbstractDrawableComponent adc)
/*      */   {
/* 1024 */     resetADC(adc);
/*      */ 
/* 1026 */     ((PgenResourceData)this.resourceData).removeElement(adc);
/*      */   }
/*      */ 
/*      */   public void removeAllProducts()
/*      */   {
/*      */     Iterator localIterator2;
/* 1041 */     for (Iterator localIterator1 = ((PgenResourceData)this.resourceData).getProductList().iterator(); localIterator1.hasNext(); 
/* 1042 */       localIterator2.hasNext())
/*      */     {
/* 1041 */       Product prod = (Product)localIterator1.next();
/* 1042 */       localIterator2 = prod.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/* 1043 */       resetADC(layer);
/*      */     }
/*      */ 
/* 1047 */     ((PgenResourceData)this.resourceData).removeAllProducts();
/*      */   }
/*      */ 
/*      */   public void addElement(AbstractDrawableComponent de)
/*      */   {
/* 1059 */     ((PgenResourceData)this.resourceData).addElement(de);
/*      */   }
/*      */ 
/*      */   public void addElements(List<AbstractDrawableComponent> elems)
/*      */   {
/* 1071 */     ((PgenResourceData)this.resourceData).addElements(elems);
/*      */   }
/*      */ 
/*      */   public void addPtSelected(int ptIdx)
/*      */   {
/* 1083 */     if (this.ptsSelectedIndex == null) {
/* 1084 */       this.ptsSelectedIndex = new ArrayList();
/*      */     }
/*      */ 
/* 1087 */     this.ptsSelectedIndex.add(Integer.valueOf(ptIdx));
/*      */   }
/*      */ 
/*      */   public void removePtsSelected()
/*      */   {
/* 1095 */     if ((this.ptsSelectedIndex != null) && (!this.ptsSelectedIndex.isEmpty()))
/* 1096 */       this.ptsSelectedIndex.clear();
/*      */   }
/*      */ 
/*      */   public Color getPtsSelectedColor()
/*      */   {
/* 1107 */     if (this.ptsSelectedColor == null) {
/* 1108 */       return Color.red;
/*      */     }
/* 1110 */     return this.ptsSelectedColor;
/*      */   }
/*      */ 
/*      */   public void setPtsSelectedColor(Color clr) {
/* 1114 */     this.ptsSelectedColor = clr;
/*      */   }
/*      */ 
/*      */   public void setDefaultPtsSelectedColor() {
/* 1118 */     this.ptsSelectedColor = null;
/*      */   }
/*      */ 
/*      */   public void deleteElementPart(Line mpe, Coordinate pt1, Coordinate pt2)
/*      */   {
/* 1136 */     resetADC(mpe);
/*      */ 
/* 1138 */     ((PgenResourceData)this.resourceData).deleteElementPart(mpe, pt1, pt2);
/*      */   }
/*      */ 
/*      */   public int selectObj(String pgenType)
/*      */   {
/* 1151 */     int total = 0;
/* 1152 */     this.elSelected.clear();
/*      */ 
/* 1154 */     Iterator iterator = ((PgenResourceData)this.resourceData)
/* 1155 */       .getActiveLayer().getComponentIterator();
/*      */ 
/* 1157 */     while (iterator.hasNext()) {
/* 1158 */       AbstractDrawableComponent element = (AbstractDrawableComponent)iterator.next();
/* 1159 */       String elType = element.getPgenType();
/* 1160 */       if ((elType != null) && (elType.equalsIgnoreCase(pgenType))) {
/* 1161 */         this.elSelected.add(element);
/* 1162 */         total++;
/*      */       }
/*      */     }
/*      */ 
/* 1166 */     return total;
/*      */   }
/*      */ 
/*      */   public void deleteSelectedElements()
/*      */   {
/* 1174 */     ((PgenResourceData)this.resourceData).removeElements(this.elSelected);
/*      */   }
/*      */ 
/*      */   public void setActiveProduct(Product activeProduct)
/*      */   {
/* 1183 */     ((PgenResourceData)this.resourceData).setActiveProduct(activeProduct);
/*      */ 
/* 1185 */     String fname = activeProduct.getInputFile();
/* 1186 */     if (fname == null) {
/* 1187 */       fname = activeProduct.getOutputFile();
/*      */     }
/*      */ 
/* 1190 */     if ((fname == null) && (((PgenResourceData)this.resourceData).getProductManageDlg() != null)) {
/* 1191 */       fname = ((PgenResourceData)this.resourceData).getProductManageDlg().getPrdOutputFile(
/* 1192 */         activeProduct);
/*      */     }
/*      */ 
/* 1195 */     if (fname != null)
/* 1196 */       PgenFileNameDisplay.getInstance().setFileName(fname);
/*      */   }
/*      */ 
/*      */   public Product getActiveProduct()
/*      */   {
/* 1204 */     return ((PgenResourceData)this.resourceData).getActiveProduct();
/*      */   }
/*      */ 
/*      */   public void setActiveLayer(Layer activeLayer)
/*      */   {
/* 1212 */     ((PgenResourceData)this.resourceData).setActiveLayer(activeLayer);
/*      */   }
/*      */ 
/*      */   public Layer getActiveLayer()
/*      */   {
/* 1219 */     return ((PgenResourceData)this.resourceData).getActiveLayer();
/*      */   }
/*      */ 
/*      */   public void activateLayering()
/*      */   {
/* 1227 */     ((PgenResourceData)this.resourceData).activateLayering();
/*      */   }
/*      */ 
/*      */   public void removeAllActiveDEs()
/*      */   {
/* 1242 */     resetADC(((PgenResourceData)this.resourceData).getActiveLayer());
/*      */ 
/* 1244 */     ((PgenResourceData)this.resourceData).removeAllActiveDEs();
/*      */   }
/*      */ 
/*      */   public void registerSelectedSymbol(AbstractDrawableComponent adc, Symbol sym)
/*      */   {
/* 1258 */     this.selectedSymbol.put(adc, sym);
/*      */   }
/*      */ 
/*      */   public void removeSelectedSymbol(AbstractDrawableComponent adc)
/*      */   {
/* 1268 */     this.selectedSymbol.remove(adc);
/*      */   }
/*      */ 
/*      */   public void clearSelectedSymbol()
/*      */   {
/* 1275 */     this.selectedSymbol.clear();
/*      */   }
/*      */ 
/*      */   public void startProductManage()
/*      */   {
/* 1283 */     ((PgenResourceData)this.resourceData).startProductManage();
/*      */   }
/*      */ 
/*      */   public void activateProductManage()
/*      */   {
/* 1292 */     ((PgenResourceData)this.resourceData).activateProductManage();
/*      */   }
/*      */ 
/*      */   public void removeProduct(Product prd)
/*      */   {
/* 1304 */     for (Layer layer : prd.getLayers()) {
/* 1305 */       resetADC(layer);
/*      */     }
/*      */ 
/* 1308 */     ((PgenResourceData)this.resourceData).getProductList().remove(prd);
/*      */   }
/*      */ 
/*      */   public ProductManageDialog getProductManageDlg()
/*      */   {
/* 1315 */     return ((PgenResourceData)this.resourceData).getProductManageDlg();
/*      */   }
/*      */ 
/*      */   public PgenLayeringControlDialog getLayeringControlDlg()
/*      */   {
/* 1322 */     return ((PgenResourceData)this.resourceData).getLayeringControlDlg();
/*      */   }
/*      */ 
/*      */   public void closeDialogs()
/*      */   {
/* 1330 */     ((PgenResourceData)this.resourceData).closeDialogs();
/*      */   }
/*      */ 
/*      */   public void resetElement(DrawableElement el)
/*      */   {
/* 1347 */     if (this.displayMap.containsKey(el)) {
/* 1348 */       ((AbstractElementContainer)this.displayMap.get(el)).dispose();
/* 1349 */       this.displayMap.remove(el);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void resetADC(AbstractDrawableComponent adc)
/*      */   {
/* 1360 */     Iterator iterator = adc.createDEIterator();
/* 1361 */     while (iterator.hasNext()) {
/* 1362 */       DrawableElement el = (DrawableElement)iterator.next();
/* 1363 */       resetElement(el);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DrawableElement getNearestElement(Coordinate point, DECollection dec)
/*      */   {
/* 1375 */     DrawableElement nearestElement = null;
/* 1376 */     double minDistance = 1.7976931348623157E+308D;
/*      */ 
/* 1378 */     Iterator iterator = dec.createDEIterator();
/* 1379 */     while (iterator.hasNext()) {
/* 1380 */       DrawableElement element = (DrawableElement)iterator.next();
/*      */ 
/* 1382 */       if (this.filters.acceptOnce(element))
/*      */       {
/* 1384 */         double dist = getDistance(element, point);
/* 1385 */         if ((minDistance < 0.0D) || (dist < minDistance))
/*      */         {
/* 1387 */           minDistance = dist;
/* 1388 */           nearestElement = element;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1394 */     return nearestElement;
/*      */   }
/*      */ 
/*      */   public void notifyRemove(ResourcePair rp)
/*      */     throws VizException
/*      */   {
/* 1407 */     if (rp.getResource() == this)
/*      */     {
/* 1412 */       ((PgenResourceData)this.resourceData).cleanup(this.paneImage);
/*      */     }
/*      */   }
/*      */ 
/*      */   public ElementFilterCollection getFilters()
/*      */   {
/* 1423 */     return this.filters;
/*      */   }
/*      */ 
/*      */   public double getDistance(AbstractDrawableComponent adc, Coordinate loc)
/*      */   {
/* 1438 */     double minDist = 1.7976931348623157E+308D;
/*      */ 
/* 1440 */     AbstractEditor mapEditor = PgenUtil.getActiveEditor();
/* 1441 */     double[] locScreen = mapEditor.translateInverseClick(loc);
/*      */ 
/* 1443 */     if ((adc instanceof SinglePointElement))
/*      */     {
/* 1445 */       double[] pt = mapEditor
/* 1446 */         .translateInverseClick(((SinglePointElement)adc)
/* 1447 */         .getLocation());
/*      */ 
/* 1449 */       Point ptScreen = new GeometryFactory().createPoint(new Coordinate(
/* 1450 */         pt[0], pt[1]));
/* 1451 */       minDist = ptScreen.distance(new GeometryFactory()
/* 1452 */         .createPoint(new Coordinate(locScreen[0], locScreen[1])));
/* 1453 */     } else if ((adc instanceof TCAElement)) {
/* 1454 */       TCAElement tca = (TCAElement)adc;
/* 1455 */       double dist = 1.7976931348623157E+308D;
/*      */       Iterator localIterator2;
/* 1457 */       for (Iterator localIterator1 = tca.getAdvisories().iterator(); localIterator1.hasNext(); 
/* 1458 */         localIterator2.hasNext())
/*      */       {
/* 1457 */         TropicalCycloneAdvisory advisory = (TropicalCycloneAdvisory)localIterator1.next();
/* 1458 */         localIterator2 = advisory.getSegment().getPaths().iterator(); continue; Coordinate[] coords = (Coordinate[])localIterator2.next();
/* 1459 */         for (int ii = 0; ii < coords.length - 1; ii++)
/*      */         {
/* 1461 */           dist = distanceFromLineSegment(loc, 
/* 1462 */             coords[ii], 
/* 1463 */             coords[(ii + 1)]);
/* 1464 */           if (dist < minDist)
/*      */           {
/* 1466 */             minDist = dist;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/* 1473 */     else if ((adc instanceof Gfa))
/*      */     {
/* 1475 */       Gfa gfa = (Gfa)adc;
/*      */ 
/* 1478 */       double[] pt = mapEditor.translateInverseClick(gfa
/* 1479 */         .getGfaTextCoordinate());
/* 1480 */       Point ptScreen = new GeometryFactory().createPoint(new Coordinate(
/* 1481 */         pt[0], pt[1]));
/* 1482 */       minDist = ptScreen.distance(new GeometryFactory()
/* 1483 */         .createPoint(new Coordinate(locScreen[0], locScreen[1])));
/*      */ 
/* 1485 */       double dist = 1.7976931348623157E+308D;
/*      */ 
/* 1487 */       Object[] pts = gfa.getPoints().toArray();
/*      */ 
/* 1489 */       for (int ii = 0; ii < pts.length; ii++)
/*      */       {
/* 1491 */         if (ii == pts.length - 1) {
/* 1492 */           if (!gfa.isClosedLine().booleanValue()) break;
/* 1493 */           dist = distanceFromLineSegment(loc, 
/* 1494 */             (Coordinate)pts[ii], (Coordinate)pts[0]);
/*      */         }
/*      */         else
/*      */         {
/* 1499 */           dist = distanceFromLineSegment(loc, (Coordinate)pts[ii], 
/* 1500 */             (Coordinate)pts[(ii + 1)]);
/*      */         }
/*      */ 
/* 1503 */         if (dist < minDist)
/*      */         {
/* 1505 */           minDist = dist;
/*      */         }
/*      */       }
/*      */     }
/* 1509 */     else if ((adc instanceof Arc)) {
/* 1510 */       Arc arc = (Arc)adc;
/*      */ 
/* 1512 */       double[] center = mapEditor.translateInverseClick(arc
/* 1513 */         .getCenterPoint());
/* 1514 */       double[] circum = mapEditor.translateInverseClick(arc
/* 1515 */         .getCircumferencePoint());
/*      */ 
/* 1517 */       Point ctrScreen = new GeometryFactory().createPoint(new Coordinate(
/* 1518 */         center[0], center[1]));
/*      */ 
/* 1520 */       minDist = ctrScreen.distance(new GeometryFactory()
/* 1521 */         .createPoint(new Coordinate(locScreen[0], locScreen[1])));
/*      */ 
/* 1526 */       double axisAngle = Math.toDegrees(Math.atan2(
/* 1527 */         circum[1] - center[1], circum[0] - center[0]));
/* 1528 */       double cosineAxis = Math.cos(Math.toRadians(axisAngle));
/* 1529 */       double sineAxis = Math.sin(Math.toRadians(axisAngle));
/*      */ 
/* 1534 */       double[] diff = { circum[0] - center[0], circum[1] - center[1] };
/* 1535 */       double major = Math.sqrt(diff[0] * diff[0] + diff[1] * diff[1]);
/* 1536 */       double minor = major * arc.getAxisRatio();
/*      */ 
/* 1538 */       double angle = arc.getStartAngle();
/* 1539 */       int numpts = (int)Math.round(arc.getEndAngle() - 
/* 1540 */         arc.getStartAngle() + 1.0D);
/*      */ 
/* 1542 */       for (int j = 0; j < numpts; j++) {
/* 1543 */         double thisSine = Math.sin(Math.toRadians(angle));
/* 1544 */         double thisCosine = Math.cos(Math.toRadians(angle));
/*      */ 
/* 1546 */         double xx = center[0] + major * cosineAxis * thisCosine - 
/* 1547 */           minor * sineAxis * thisSine;
/* 1548 */         double yy = center[1] + major * sineAxis * thisCosine + 
/* 1549 */           minor * cosineAxis * thisSine;
/*      */ 
/* 1551 */         Point ptScreen = new GeometryFactory()
/* 1552 */           .createPoint(new Coordinate(xx, yy));
/* 1553 */         double dist = ptScreen
/* 1554 */           .distance(new GeometryFactory()
/* 1555 */           .createPoint(new Coordinate(locScreen[0], 
/* 1556 */           locScreen[1])));
/* 1557 */         if (dist < minDist) {
/* 1558 */           minDist = dist;
/*      */         }
/* 1560 */         angle += 1.0D;
/*      */       }
/* 1562 */     } else if ((adc instanceof MultiPointElement))
/*      */     {
/* 1564 */       if ((adc instanceof Sigmet))
/*      */       {
/* 1567 */         if (("Isolated"
/* 1566 */           .equalsIgnoreCase(((Sigmet)adc)
/* 1567 */           .getType())) || 
/* 1568 */           (((Sigmet)adc)
/* 1568 */           .getType().contains("Text")))
/*      */         {
/* 1570 */           double[] pt = mapEditor
/* 1571 */             .translateInverseClick(((Sigmet)adc)
/* 1572 */             .getLinePoints()[0]);
/* 1573 */           Point ptScreen = new GeometryFactory()
/* 1574 */             .createPoint(new Coordinate(pt[0], pt[1]));
/* 1575 */           minDist = ptScreen
/* 1576 */             .distance(new GeometryFactory()
/* 1577 */             .createPoint(new Coordinate(locScreen[0], 
/* 1578 */             locScreen[1])));
/*      */         }
/*      */       }
/*      */ 
/* 1582 */       MultiPointElement mpe = (MultiPointElement)adc;
/*      */ 
/* 1584 */       double dist = 1.7976931348623157E+308D;
/*      */ 
/* 1586 */       Object[] pts = mpe.getPoints().toArray();
/*      */ 
/* 1588 */       for (int ii = 0; ii < pts.length; ii++)
/*      */       {
/* 1590 */         if (((mpe instanceof Tcm)) && (pts.length == 1)) {
/* 1591 */           double[] pt = mapEditor.translateInverseClick(mpe
/* 1592 */             .getLinePoints()[0]);
/* 1593 */           Point ptScreen = new GeometryFactory()
/* 1594 */             .createPoint(new Coordinate(pt[0], pt[1]));
/* 1595 */           minDist = ptScreen.distance(new GeometryFactory()
/* 1596 */             .createPoint(new Coordinate(locScreen[0], 
/* 1597 */             locScreen[1])));
/* 1598 */           break;
/*      */         }
/*      */ 
/* 1601 */         if (ii == pts.length - 1) {
/* 1602 */           if (((!(mpe instanceof Line)) || (!((Line)mpe).isClosedLine().booleanValue())) && 
/* 1603 */             (!(mpe instanceof WatchBox))) {
/* 1604 */             if (!(mpe instanceof Sigmet))
/*      */               break;
/* 1606 */             if (!"Area"
/* 1605 */               .equalsIgnoreCase(((Sigmet)mpe)
/* 1606 */               .getType())) break;
/*      */           }
/* 1608 */           dist = distanceFromLineSegment(loc, 
/* 1609 */             (Coordinate)pts[ii], (Coordinate)pts[0]);
/*      */         }
/*      */         else
/*      */         {
/* 1616 */           dist = distanceFromLineSegment(loc, (Coordinate)pts[ii], 
/* 1617 */             (Coordinate)pts[(ii + 1)]);
/*      */         }
/*      */ 
/* 1621 */         if (dist < minDist)
/*      */         {
/* 1623 */           minDist = dist;
/*      */         }
/*      */       }
/*      */     }
/* 1627 */     else if ((adc instanceof DECollection)) {
/* 1628 */       Iterator it = ((DECollection)adc)
/* 1629 */         .createDEIterator();
/* 1630 */       while (it.hasNext()) {
/* 1631 */         double dist = getDistance((AbstractDrawableComponent)it.next(), loc);
/* 1632 */         if (dist < minDist) {
/* 1633 */           minDist = dist;
/*      */         }
/*      */       }
/*      */     }
/* 1637 */     return minDist;
/*      */   }
/*      */ 
/*      */   public static double distanceFromLineSegment(Coordinate loc, Coordinate startPt, Coordinate endPt)
/*      */   {
/* 1654 */     double dist = 1.7976931348623157E+308D;
/*      */ 
/* 1657 */     AbstractEditor mapEditor = PgenUtil.getActiveEditor();
/* 1658 */     double[] locScreen = mapEditor.translateInverseClick(loc);
/*      */ 
/* 1660 */     double[] pt1 = mapEditor.translateInverseClick(startPt);
/* 1661 */     double[] pt2 = mapEditor.translateInverseClick(endPt);
/* 1662 */     LineSegment seg = new LineSegment(new Coordinate(pt1[0], pt1[1]), 
/* 1663 */       new Coordinate(pt2[0], pt2[1]));
/*      */ 
/* 1665 */     dist = seg.distance(new Coordinate(locScreen[0], locScreen[1]));
/*      */ 
/* 1667 */     return dist;
/*      */   }
/*      */ 
/*      */   public void resourceChanged(IResourceDataChanged.ChangeType type, Object object)
/*      */   {
/* 1672 */     this.saveOnNextPaint = true;
/*      */   }
/*      */ 
/*      */   public void setCatFilter(CategoryFilter catFilter) {
/* 1676 */     this.catFilter = catFilter;
/*      */   }
/*      */ 
/*      */   public int getMaxDistToSelect() {
/* 1680 */     IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
/* 1681 */     int maxDist = prefs.getInt("PGEN_MAX_DISTANCE_TO_SELECT");
/* 1682 */     if (maxDist <= 0)
/* 1683 */       maxDist = 30;
/* 1684 */     return maxDist;
/*      */   }
/*      */ 
/*      */   public void saveCurrentProduct(String filename)
/*      */   {
/* 1693 */     ((PgenResourceData)this.resourceData).saveCurrentProduct(filename);
/*      */   }
/*      */ 
/*      */   public void storeCurrentProduct(String label)
/*      */   {
/* 1702 */     ((PgenResourceData)this.resourceData).storeCurrentProduct(label);
/*      */   }
/*      */ 
/*      */   public void saveAllProducts()
/*      */   {
/* 1711 */     ((PgenResourceData)this.resourceData).saveAllProducts();
/*      */   }
/*      */ 
/*      */   public void storeAllProducts()
/*      */   {
/* 1721 */     ((PgenResourceData)this.resourceData).storeAllProducts();
/*      */   }
/*      */ 
/*      */   public String buildFileName(Product prd)
/*      */   {
/* 1730 */     return ((PgenResourceData)this.resourceData).buildFileName(prd);
/*      */   }
/*      */ 
/*      */   public String buildActivityLabel(Product prd)
/*      */   {
/* 1737 */     return ((PgenResourceData)this.resourceData).buildActivityLabel(prd);
/*      */   }
/*      */ 
/*      */   public void deactivatePgenTools()
/*      */   {
/* 1744 */     AbstractVizPerspectiveManager mgr = 
/* 1745 */       VizPerspectiveListener.getCurrentPerspectiveManager();
/* 1746 */     if (mgr != null) {
/* 1747 */       Iterator it = mgr.getToolManager()
/* 1748 */         .getSelectedModalTools().iterator();
/* 1749 */       while (it.hasNext()) {
/* 1750 */         AbstractModalTool tool = (AbstractModalTool)it.next();
/* 1751 */         if ((tool != null) && ((tool instanceof AbstractPgenTool))) {
/* 1752 */           tool.deactivate();
/* 1753 */           it.remove();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void drawProduct(IGraphicsTarget target, PaintProperties paintProps)
/*      */   {
/* 1782 */     drawFilledElements(target, paintProps);
/*      */ 
/* 1784 */     drawNonFilledElements(target, paintProps);
/*      */   }
/*      */ 
/*      */   private void drawFilledElements(IGraphicsTarget target, PaintProperties paintProps)
/*      */   {
/*      */     Iterator localIterator2;
/* 1800 */     label96: for (Iterator localIterator1 = ((PgenResourceData)this.resourceData).getProductList().iterator(); localIterator1.hasNext(); 
/* 1802 */       localIterator2.hasNext())
/*      */     {
/* 1800 */       Product prod = (Product)localIterator1.next();
/* 1801 */       if ((!prod.isOnOff()) || (prod == ((PgenResourceData)this.resourceData).getActiveProduct())) break label96;
/* 1802 */       localIterator2 = prod.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/* 1803 */       drawFilledElements(layer, target, paintProps);
/*      */     }
/*      */ 
/* 1809 */     for (Layer layer : ((PgenResourceData)this.resourceData).getActiveProduct().getLayers()) {
/* 1810 */       if (layer != ((PgenResourceData)this.resourceData).getActiveLayer()) {
/* 1811 */         drawFilledElements(layer, target, paintProps);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1816 */     drawFilledElements(((PgenResourceData)this.resourceData).getActiveLayer(), target, paintProps);
/*      */   }
/*      */ 
/*      */   private void drawFilledElements(Layer layer, IGraphicsTarget target, PaintProperties paintProps)
/*      */   {
/* 1832 */     if ((layer != null) && (
/* 1833 */       (layer.isOnOff()) || (layer == ((PgenResourceData)this.resourceData).getActiveLayer())))
/*      */     {
/* 1835 */       DisplayProperties dprops = new DisplayProperties();
/*      */ 
/* 1837 */       if (layer != ((PgenResourceData)this.resourceData).getActiveLayer()) {
/* 1838 */         dprops.setLayerMonoColor(Boolean.valueOf(layer.isMonoColor()));
/* 1839 */         dprops.setLayerColor(layer.getColor());
/* 1840 */         dprops.setLayerFilled(Boolean.valueOf(layer.isFilled()));
/*      */       }
/*      */ 
/* 1843 */       Iterator iterator = layer.createDEIterator();
/* 1844 */       while (iterator.hasNext()) {
/* 1845 */         DrawableElement el = (DrawableElement)iterator.next();
/*      */ 
/* 1847 */         if (((el instanceof MultiPointElement)) && 
/* 1848 */           (((MultiPointElement)el).getFilled()))
/*      */         {
/* 1850 */           drawElement(el, target, paintProps, dprops);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void drawNonFilledElements(IGraphicsTarget target, PaintProperties paintProps)
/*      */   {
/*      */     Iterator localIterator2;
/* 1868 */     label96: for (Iterator localIterator1 = ((PgenResourceData)this.resourceData).getProductList().iterator(); localIterator1.hasNext(); 
/* 1870 */       localIterator2.hasNext())
/*      */     {
/* 1868 */       Product prod = (Product)localIterator1.next();
/* 1869 */       if ((!prod.isOnOff()) || (prod == ((PgenResourceData)this.resourceData).getActiveProduct())) break label96;
/* 1870 */       localIterator2 = prod.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/* 1871 */       drawNonFilledElements(layer, target, paintProps);
/*      */     }
/*      */ 
/* 1877 */     for (Layer layer : ((PgenResourceData)this.resourceData).getActiveProduct().getLayers()) {
/* 1878 */       if (layer != ((PgenResourceData)this.resourceData).getActiveLayer()) {
/* 1879 */         drawNonFilledElements(layer, target, paintProps);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1884 */     drawNonFilledElements(((PgenResourceData)this.resourceData).getActiveLayer(), target, paintProps);
/*      */   }
/*      */ 
/*      */   private void drawNonFilledElements(Layer layer, IGraphicsTarget target, PaintProperties paintProps)
/*      */   {
/* 1903 */     if ((layer != null) && (
/* 1904 */       (layer.isOnOff()) || (layer == ((PgenResourceData)this.resourceData).getActiveLayer())))
/*      */     {
/* 1906 */       DisplayProperties dprops = new DisplayProperties();
/* 1907 */       if (layer != ((PgenResourceData)this.resourceData).getActiveLayer()) {
/* 1908 */         dprops.setLayerMonoColor(Boolean.valueOf(layer.isMonoColor()));
/* 1909 */         dprops.setLayerColor(layer.getColor());
/* 1910 */         dprops.setLayerFilled(Boolean.valueOf(layer.isFilled()));
/*      */       }
/*      */ 
/* 1913 */       Iterator iterator = layer.createDEIterator();
/*      */ 
/* 1915 */       ArrayList filledElements = new ArrayList();
/* 1916 */       ArrayList textElements = new ArrayList();
/* 1917 */       ArrayList otherElements = new ArrayList();
/*      */ 
/* 1919 */       while (iterator.hasNext()) {
/* 1920 */         DrawableElement el = (DrawableElement)iterator.next();
/*      */ 
/* 1922 */         if ((el instanceof Text))
/* 1923 */           textElements.add(el);
/* 1924 */         else if (((el instanceof MultiPointElement)) && 
/* 1925 */           (((MultiPointElement)el).getFilled()))
/* 1926 */           filledElements.add(el);
/*      */         else {
/* 1928 */           otherElements.add(el);
/*      */         }
/*      */       }
/*      */ 
/* 1932 */       drawElements(otherElements, target, paintProps, dprops);
/* 1933 */       drawElements(textElements, target, paintProps, dprops);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void drawElements(ArrayList<DrawableElement> elements, IGraphicsTarget target, PaintProperties paintProps, DisplayProperties dispProps)
/*      */   {
/* 1953 */     for (DrawableElement de : elements)
/* 1954 */       drawElement(de, target, paintProps, dispProps);
/*      */   }
/*      */ 
/*      */   private void drawElement(DrawableElement de, IGraphicsTarget target, PaintProperties paintProps, DisplayProperties dispProps)
/*      */   {
/* 1972 */     if (this.filters.acceptOnce(de))
/*      */     {
/* 1974 */       if (!this.displayMap.containsKey(de)) {
/* 1975 */         AbstractElementContainer container = 
/* 1976 */           ElementContainerFactory.createContainer(de, (MapDescriptor)this.descriptor, target);
/* 1977 */         this.displayMap.put(de, container);
/*      */       }
/*      */ 
/* 1980 */       ((AbstractElementContainer)this.displayMap.get(de)).draw(target, paintProps, dispProps);
/*      */     }
/*      */   }
/*      */ 
/*      */   private DrawableElement getNearestGfaByTextBox(Coordinate point, ElementFilter filter)
/*      */   {
/* 1996 */     DrawableElement nearestElement = null;
/*      */ 
/* 1998 */     double boxScale = 2.0D;
/* 1999 */     double minDistance = getMaxDistToSelect() * boxScale;
/*      */ 
/* 2001 */     AbstractEditor mapEditor = PgenUtil.getActiveEditor();
/* 2002 */     double[] locScreen = mapEditor.translateInverseClick(point);
/* 2003 */     Point clickPt = new GeometryFactory().createPoint(new Coordinate(
/* 2004 */       locScreen[0], locScreen[1]));
/*      */ 
/* 2006 */     Iterator iterator = ((PgenResourceData)this.resourceData).getActiveLayer()
/* 2007 */       .createDEIterator();
/* 2008 */     while (iterator.hasNext()) {
/* 2009 */       DrawableElement element = (DrawableElement)iterator.next();
/*      */ 
/* 2011 */       if ((element instanceof Gfa))
/*      */       {
/* 2013 */         if ((filter.accept(element)) && (this.filters.acceptOnce(element)))
/*      */         {
/* 2015 */           if (this.catFilter.accept(element))
/*      */           {
/* 2018 */             Gfa gfa = (Gfa)element;
/*      */ 
/* 2021 */             double[] pt = mapEditor.translateInverseClick(gfa
/* 2022 */               .getGfaTextCoordinate());
/* 2023 */             Point ptScreen = new GeometryFactory().createPoint(new Coordinate(
/* 2024 */               pt[0], pt[1]));
/* 2025 */             double distToBox = ptScreen.distance(clickPt);
/*      */ 
/* 2027 */             if (distToBox < minDistance) {
/* 2028 */               minDistance = distToBox;
/* 2029 */               nearestElement = element;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2034 */     return nearestElement;
/*      */   }
/*      */ 
/*      */   public boolean isEditable()
/*      */   {
/* 2044 */     return ((EditableCapability)getCapability(EditableCapability.class)).isEditable();
/*      */   }
/*      */ 
/*      */   public void provideContextMenuItems(IMenuManager menuManager, int x, int y)
/*      */   {
/* 2053 */     ResourcePair pgenPair = null;
/* 2054 */     for (ResourcePair rp : ((MapDescriptor)this.descriptor).getResourceList()) {
/* 2055 */       if (rp.getResource() == this) {
/* 2056 */         pgenPair = rp;
/* 2057 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 2061 */     if ((pgenPair != null) && (pgenPair.getResource() != null))
/* 2062 */       fillContextMenu(menuManager, pgenPair);
/*      */   }
/*      */ 
/*      */   protected void fillContextMenu(IMenuManager menuManager, ResourcePair selectedResource)
/*      */   {
/* 2075 */     if ((getSelectedDE() != null) && 
/* 2076 */       (!(getSelectedDE() instanceof Jet.JetLine))) {
/* 2077 */       List actList = getActionList(getSelectedDE()
/* 2078 */         .getPgenType());
/* 2079 */       if (actList != null)
/* 2080 */         for (String act : actList)
/* 2081 */           menuManager.add(new PgenAction(act.trim()));
/*      */     }
/*      */   }
/*      */ 
/*      */   private List<String> getActionList(String objName)
/*      */   {
/* 2096 */     HashMap itemMap = 
/* 2097 */       PgenSession.getInstance().getPgenPalette().getItemMap();
/*      */ 
/* 2099 */     IConfigurationElement ice = (IConfigurationElement)itemMap.get(objName);
/* 2100 */     if (ice != null)
/*      */     {
/* 2102 */       String actList = ice.getAttribute("actions");
/*      */ 
/* 2105 */       if (actList == null) {
/* 2106 */         String category = ice.getAttribute("className");
/* 2107 */         if (category != null) {
/* 2108 */           IConfigurationElement cat = (IConfigurationElement)itemMap.get(category);
/* 2109 */           if (cat != null) {
/* 2110 */             actList = cat.getAttribute("actions");
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2115 */       if ((actList != null) && (!actList.isEmpty())) {
/* 2116 */         return Arrays.asList(actList.split("\\s*,\\s*"));
/*      */       }
/*      */     }
/*      */ 
/* 2120 */     return null;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource
 * JD-Core Version:    0.6.2
 */