/*      */ package gov.noaa.nws.ncep.ui.pgen.palette;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.IDisplayPane;
/*      */ import com.raytheon.uf.viz.core.VizApp;
/*      */ import com.raytheon.uf.viz.core.drawables.IDescriptor;
/*      */ import com.raytheon.uf.viz.core.drawables.IRenderableDisplay;
/*      */ import com.raytheon.uf.viz.core.drawables.ResourcePair;
/*      */ import com.raytheon.uf.viz.core.rsc.ResourceList;
/*      */ import com.raytheon.uf.viz.core.rsc.ResourceProperties;
/*      */ import com.raytheon.viz.ui.UiUtil;
/*      */ import com.raytheon.viz.ui.VizWorkbenchManager;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.raytheon.viz.ui.editor.ISelectedPanesChangedListener;
/*      */ import com.raytheon.viz.ui.perspectives.AbstractVizPerspectiveManager;
/*      */ import com.raytheon.viz.ui.perspectives.VizPerspectiveListener;
/*      */ import com.raytheon.viz.ui.tools.AbstractModalTool;
/*      */ import com.raytheon.viz.ui.tools.ModalToolManager;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil.PgenMode;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.CommandStackListener;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.filter.CategoryFilter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.PreloadGfaDataThread;
/*      */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductDialogStarter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenSelectingTool;
/*      */ import gov.noaa.nws.ncep.viz.common.display.NcDisplayName;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.NmapCommon;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import org.eclipse.core.commands.Command;
/*      */ import org.eclipse.core.commands.ExecutionEvent;
/*      */ import org.eclipse.core.runtime.IConfigurationElement;
/*      */ import org.eclipse.core.runtime.IExtensionPoint;
/*      */ import org.eclipse.core.runtime.IExtensionRegistry;
/*      */ import org.eclipse.core.runtime.Platform;
/*      */ import org.eclipse.core.runtime.Status;
/*      */ import org.eclipse.jface.dialogs.ErrorDialog;
/*      */ import org.eclipse.jface.resource.ImageDescriptor;
/*      */ import org.eclipse.swt.custom.ScrolledComposite;
/*      */ import org.eclipse.swt.events.ControlAdapter;
/*      */ import org.eclipse.swt.events.ControlEvent;
/*      */ import org.eclipse.swt.events.DisposeEvent;
/*      */ import org.eclipse.swt.events.DisposeListener;
/*      */ import org.eclipse.swt.events.SelectionEvent;
/*      */ import org.eclipse.swt.events.SelectionListener;
/*      */ import org.eclipse.swt.graphics.Image;
/*      */ import org.eclipse.swt.graphics.ImageData;
/*      */ import org.eclipse.swt.graphics.Rectangle;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.layout.RowLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.MessageBox;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.ui.IEditorPart;
/*      */ import org.eclipse.ui.IPartListener2;
/*      */ import org.eclipse.ui.IViewSite;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchPage;
/*      */ import org.eclipse.ui.IWorkbenchPart;
/*      */ import org.eclipse.ui.IWorkbenchPartReference;
/*      */ import org.eclipse.ui.IWorkbenchPartSite;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PartInitException;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ import org.eclipse.ui.commands.ICommandService;
/*      */ import org.eclipse.ui.contexts.IContextActivation;
/*      */ import org.eclipse.ui.contexts.IContextService;
/*      */ import org.eclipse.ui.part.ViewPart;
/*      */ 
/*      */ public class PgenPaletteWindow extends ViewPart
/*      */   implements SelectionListener, DisposeListener, CommandStackListener, IPartListener2, ISelectedPanesChangedListener
/*      */ {
/*  126 */   private final int bgcolor = 255;
/*  127 */   private final int fgcolor = 16777215;
/*  128 */   private final String EXTENSION_POINT = "gov.noaa.nws.ncep.ui.pgen.palette";
/*  129 */   private final String CONTROL_SECTION = "control";
/*  130 */   private final String ACTION_SECTION = "action";
/*  131 */   private final String CLASS_SECTION = "class";
/*  132 */   private final String OBJECT_SECTION = "object";
/*  133 */   private final String CONTROL_LABEL = "Controls:";
/*  134 */   private final String ACTION_LABEL = "Actions:";
/*  135 */   private final String CLASS_LABEL = "Classes:";
/*  136 */   private final String OBJECT_LABEL = "Objects:";
/*      */   private IWorkbenchPage page;
/*      */   private Composite mainComp;
/*      */   private Composite paletteComp;
/*      */   private ScrolledComposite scroll;
/*  144 */   private static IConfigurationElement[] paletteElements = null;
/*      */ 
/*  147 */   private static HashMap<String, IConfigurationElement> itemMap = null;
/*      */ 
/*  150 */   private ArrayList<String> controlNames = null;
/*      */ 
/*  153 */   private ArrayList<String> actionNames = null;
/*      */ 
/*  156 */   private ArrayList<String> classNames = null;
/*      */ 
/*  159 */   private ArrayList<String> objectNames = null;
/*      */   private static Group objectBox;
/*  162 */   private Button undoButton = null;
/*  163 */   private Button redoButton = null;
/*  164 */   private String currentCategory = null;
/*  165 */   private String currentObject = null;
/*      */ 
/*  167 */   private String currentAction = "";
/*      */ 
/*  169 */   private HashMap<String, Button> buttonMap = null;
/*  170 */   private HashMap<String, Image> iconMap = null;
/*  171 */   private HashMap<String, Image> activeIconMap = null;
/*      */ 
/*  173 */   private List<String> buttonList = null;
/*      */   private IContextActivation pgenContextActivation;
/*  176 */   private AbstractEditor currentIsMultiPane = null;
/*      */ 
/*      */   public void init(IViewSite site)
/*      */   {
/*      */     try
/*      */     {
/*  195 */       super.init(site);
/*      */     }
/*      */     catch (PartInitException pie)
/*      */     {
/*  199 */       pie.printStackTrace();
/*      */     }
/*      */ 
/*  203 */     this.page = site.getPage();
/*  204 */     this.page.addPartListener(this);
/*      */ 
/*  210 */     if (paletteElements == null)
/*      */     {
/*  212 */       IExtensionRegistry registry = Platform.getExtensionRegistry();
/*  213 */       IExtensionPoint epoint = registry.getExtensionPoint("gov.noaa.nws.ncep.ui.pgen.palette");
/*  214 */       paletteElements = epoint.getConfigurationElements();
/*      */     }
/*      */ 
/*  223 */     itemMap = new LinkedHashMap(paletteElements.length);
/*  224 */     this.controlNames = new ArrayList();
/*  225 */     this.actionNames = new ArrayList();
/*  226 */     this.classNames = new ArrayList();
/*  227 */     this.objectNames = new ArrayList();
/*      */ 
/*  229 */     for (int i = 0; i < paletteElements.length; i++)
/*      */     {
/*  232 */       String itemName = paletteElements[i].getAttribute("name");
/*  233 */       itemMap.put(itemName, paletteElements[i]);
/*      */ 
/*  238 */       String type = paletteElements[i].getName();
/*  239 */       if (type.equals("control"))
/*  240 */         this.controlNames.add(itemName);
/*  241 */       else if (type.equals("action"))
/*  242 */         this.actionNames.add(itemName);
/*  243 */       else if (type.equals("class"))
/*  244 */         this.classNames.add(itemName);
/*  245 */       else if (type.equals("object")) {
/*  246 */         this.objectNames.add(itemName);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  254 */     this.buttonMap = new HashMap();
/*  255 */     this.iconMap = new HashMap();
/*  256 */     this.activeIconMap = new HashMap();
/*      */ 
/*  261 */     PgenCycleTool.updateTitle();
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/*  272 */     super.dispose();
/*      */ 
/*  277 */     PgenResource pgen = PgenSession.getInstance().getPgenResource();
/*  278 */     if (pgen != null) {
/*  279 */       pgen.closeDialogs();
/*      */     }
/*      */ 
/*  285 */     PgenSession.getInstance().removePalette();
/*      */ 
/*  290 */     this.page.removePartListener(this);
/*      */ 
/*  295 */     this.buttonMap.clear();
/*      */ 
/*  301 */     for (Image icon : this.iconMap.values()) {
/*  302 */       icon.dispose();
/*      */     }
/*  304 */     for (Image icon : this.activeIconMap.values()) {
/*  305 */       icon.dispose();
/*      */     }
/*      */ 
/*  309 */     this.iconMap.clear();
/*  310 */     this.activeIconMap.clear();
/*      */ 
/*  316 */     PgenUtil.resetCaveTitle();
/*      */   }
/*      */ 
/*      */   public void createPartControl(Composite comp)
/*      */   {
/*  325 */     this.mainComp = comp;
/*  326 */     this.scroll = new ScrolledComposite(comp, 2560);
/*  327 */     this.paletteComp = new Composite(this.scroll, 0);
/*  328 */     this.scroll.setContent(this.paletteComp);
/*      */ 
/*  333 */     this.paletteComp.setLayout(new GridLayout(1, false));
/*      */ 
/*  339 */     this.scroll.addControlListener(new ControlAdapter() {
/*      */       public void controlResized(ControlEvent e) {
/*  341 */         Rectangle r = PgenPaletteWindow.this.scroll.getClientArea();
/*  342 */         PgenPaletteWindow.this.paletteComp.setSize(PgenPaletteWindow.this.paletteComp.computeSize(r.width, -1));
/*  343 */         PgenPaletteWindow.this.paletteComp.layout();
/*      */       }
/*      */     });
/*  350 */     resetPalette(null);
/*      */ 
/*  355 */     PgenSession.getInstance().setPalette(this);
/*      */ 
/*  361 */     PgenResource current = PgenUtil.findPgenResource(null);
/*  362 */     if (current != null) PgenSession.getInstance().setResource(current);
/*      */   }
/*      */ 
/*      */   private void createObjectSection(Composite parent)
/*      */   {
/*  372 */     Label control = new Label(parent, 0);
/*  373 */     control.setText("Objects:");
/*      */ 
/*  375 */     objectBox = new Group(parent, 4);
/*      */ 
/*  377 */     objectBox.setLayout(new RowLayout(256));
/*  378 */     objectBox.setLayoutData(new GridData(768));
/*      */   }
/*      */ 
/*      */   private void createPaletteSection(Composite parent, String section)
/*      */   {
/*  393 */     Label control = new Label(parent, 0);
/*  394 */     control.setText(section);
/*      */ 
/*  399 */     Group box = new Group(parent, 4);
/*      */ 
/*  402 */     box.setLayout(new RowLayout(256));
/*  403 */     box.setLayoutData(new GridData(768));
/*      */ 
/*  408 */     List buttons = null;
/*  409 */     if (section.equals("Controls:"))
/*  410 */       buttons = getControlNames();
/*  411 */     else if (section.equals("Actions:"))
/*  412 */       buttons = getActionNames();
/*  413 */     else if (section.equals("Classes:")) {
/*  414 */       buttons = getClassNames();
/*      */     }
/*      */ 
/*  421 */     for (String bname : buttons)
/*      */     {
/*  423 */       IConfigurationElement element = (IConfigurationElement)itemMap.get(bname);
/*      */ 
/*  428 */       String always = element.getAttribute("alwaysVisible");
/*  429 */       if (((always != null) && (!always.equalsIgnoreCase("false"))) || 
/*  430 */         (this.buttonList == null) || 
/*  431 */         (this.buttonList.contains(bname)))
/*      */       {
/*  435 */         Button item = new Button(box, 8);
/*      */ 
/*  440 */         if (element.getAttribute("label") != null) {
/*  441 */           item.setToolTipText(element.getAttribute("label"));
/*      */         }
/*      */ 
/*  447 */         if (element.getAttribute("icon") != null)
/*      */         {
/*  449 */           Image icon = getIcon(element.getAttribute("icon"));
/*      */ 
/*  451 */           if (icon != null)
/*      */           {
/*  453 */             item.setImage(icon);
/*  454 */             item.addDisposeListener(this);
/*      */           }
/*      */           else
/*      */           {
/*  459 */             item.setText(element.getAttribute("name"));
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  465 */           item.setText(element.getAttribute("name"));
/*      */         }
/*      */ 
/*  473 */         item.setData(element.getAttribute("name"));
/*  474 */         item.addSelectionListener(this);
/*      */ 
/*  477 */         this.buttonMap.put(element.getAttribute("name"), item);
/*      */ 
/*  482 */         if (item.getData().equals("Undo")) {
/*  483 */           this.undoButton = item;
/*      */         }
/*  485 */         if (item.getData().equals("Redo")) {
/*  486 */           this.redoButton = item;
/*      */         }
/*      */       }
/*      */     }
/*  490 */     box.pack();
/*  491 */     box.redraw();
/*      */   }
/*      */ 
/*      */   public void resetPalette(List<String> buttonNames)
/*      */   {
/*  503 */     this.buttonList = buttonNames;
/*      */ 
/*  508 */     Control[] kids = this.paletteComp.getChildren();
/*  509 */     for (int j = 0; j < kids.length; j++) {
/*  510 */       kids[j].dispose();
/*      */     }
/*      */ 
/*  516 */     createPaletteSection(this.paletteComp, "Controls:");
/*  517 */     createPaletteSection(this.paletteComp, "Actions:");
/*  518 */     createPaletteSection(this.paletteComp, "Classes:");
/*  519 */     createObjectSection(this.paletteComp);
/*      */ 
/*  524 */     Rectangle r = this.scroll.getClientArea();
/*  525 */     this.paletteComp.setSize(this.paletteComp.computeSize(r.width, -1));
/*  526 */     this.paletteComp.layout();
/*      */ 
/*  529 */     disableUndoRedo();
/*      */   }
/*      */ 
/*      */   public void disableUndoRedo()
/*      */   {
/*  536 */     this.undoButton.setEnabled(false);
/*  537 */     this.redoButton.setEnabled(false);
/*      */   }
/*      */ 
/*      */   public void setFocus()
/*      */   {
/*  546 */     this.mainComp.setFocus();
/*      */   }
/*      */ 
/*      */   public void widgetSelected(SelectionEvent se)
/*      */   {
/*  555 */     IEditorPart editor = VizWorkbenchManager.getInstance().getActiveEditor();
/*  556 */     if ((editor instanceof AbstractEditor))
/*      */     {
/*  561 */       Button btn = (Button)se.getSource();
/*  562 */       IConfigurationElement elem = (IConfigurationElement)itemMap.get(btn.getData());
/*      */ 
/*  567 */       String point = elem.getName();
/*      */       AbstractModalTool tool;
/*  574 */       if ((point.equals("control")) || (point.equals("action")) || 
/*  575 */         (point.equals("object")))
/*      */       {
/*  578 */         if ((point.equals("object")) && 
/*  579 */           (this.currentAction.equalsIgnoreCase("MultiSelect"))) {
/*  580 */           if ((this.currentCategory != null) && (this.currentCategory.equalsIgnoreCase("MET"))) {
/*  581 */             if (this.currentObject != null) {
/*  582 */               resetIcon(this.currentObject);
/*      */             }
/*      */ 
/*  585 */             this.currentObject = elem.getAttribute("name");
/*  586 */             setActiveIcon(this.currentObject);
/*      */           }
/*      */ 
/*  589 */           elem = (IConfigurationElement)itemMap.get("MultiSelect");
/*      */         }
/*  591 */         else if (this.currentObject != null) {
/*  592 */           resetIcon(this.currentObject);
/*      */         }
/*      */ 
/*  596 */         PgenSelectingTool selTool = null;
/*  597 */         if ((point.equals("object")) && (this.currentAction.equalsIgnoreCase("Select")) && (
/*  598 */           (this.currentCategory.equalsIgnoreCase("Front")) || 
/*  599 */           (this.currentCategory.equalsIgnoreCase("Lines")))) {
/*  600 */           AbstractVizPerspectiveManager mgr = 
/*  601 */             VizPerspectiveListener.getCurrentPerspectiveManager();
/*  602 */           for (Iterator localIterator = mgr.getToolManager().getSelectedModalTools().iterator(); localIterator.hasNext(); ) { tool = (AbstractModalTool)localIterator.next();
/*      */ 
/*  604 */             if ((tool instanceof PgenSelectingTool)) {
/*  605 */               DrawableElement prevDe = ((PgenSelectingTool)tool).getPreviousSelectedDE();
/*  606 */               if ((prevDe == null) || ((!prevDe.getPgenCategory().equalsIgnoreCase("Lines")) && 
/*  607 */                 (!prevDe.getPgenCategory().equalsIgnoreCase("Front")))) break;
/*  608 */               selTool = (PgenSelectingTool)tool;
/*      */ 
/*  610 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  615 */         if (selTool != null) {
/*  616 */           selTool.changeSelectedLineType(elem.getAttribute("name"));
/*      */         }
/*      */         else
/*      */         {
/*  620 */           PgenResource pgen = PgenUtil.findPgenResource((AbstractEditor)editor);
/*  621 */           if (pgen != null) {
/*  622 */             pgen.removeGhostLine();
/*  623 */             pgen.removeSelected();
/*  624 */             pgen.deactivatePgenTools();
/*      */           }
/*      */ 
/*  627 */           exeCommand(elem);
/*      */ 
/*  629 */           if (point.equals("action"))
/*  630 */             this.currentAction = elem.getAttribute("name");
/*      */         }
/*      */       }
/*  633 */       else if (point.equals("class"))
/*      */       {
/*  641 */         Control[] kids = objectBox.getChildren();
/*  642 */         for (int j = 0; j < kids.length; j++) {
/*  643 */           kids[j].dispose();
/*      */         }
/*      */ 
/*  647 */         if (this.currentCategory != null) {
/*  648 */           resetIcon(this.currentCategory);
/*      */         }
/*      */ 
/*  651 */         this.currentCategory = elem.getAttribute("name");
/*      */ 
/*  654 */         setActiveIcon(this.currentCategory);
/*      */ 
/*  659 */         for (String bname : getObjectNames(this.currentCategory))
/*      */         {
/*  661 */           IConfigurationElement element = (IConfigurationElement)itemMap.get(bname);
/*      */ 
/*  666 */           if ((this.buttonList == null) || 
/*  667 */             (this.buttonList.contains(bname)))
/*      */           {
/*  670 */             Button item = new Button(objectBox, 8);
/*      */ 
/*  673 */             if (element.getAttribute("label") != null) {
/*  674 */               item.setToolTipText(element.getAttribute("label"));
/*      */             }
/*      */ 
/*  680 */             if (element.getAttribute("icon") != null)
/*      */             {
/*  682 */               Image icon = getIcon(element.getAttribute("icon"));
/*  683 */               item.setImage(icon);
/*  684 */               item.addDisposeListener(this);
/*      */             }
/*      */             else
/*      */             {
/*  689 */               item.setText(element.getAttribute("name"));
/*      */             }
/*      */ 
/*  695 */             item.setData(element.getAttribute("name"));
/*  696 */             item.addSelectionListener(this);
/*  697 */             this.buttonMap.put(element.getAttribute("name"), item);
/*      */ 
/*  699 */             objectBox.setSize(objectBox.computeSize(-1, 
/*  700 */               -1, true));
/*  701 */             objectBox.pack();
/*  702 */             objectBox.layout(true);
/*  703 */             objectBox.redraw();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  709 */         if (this.currentAction != null) {
/*  710 */           if (this.currentAction.isEmpty()) this.currentAction = "Select";
/*  711 */           if ((this.currentAction.equalsIgnoreCase("Select")) || 
/*  712 */             (this.currentAction.equalsIgnoreCase("MultiSelect")) || 
/*  713 */             (this.currentAction.equalsIgnoreCase("Copy")) || 
/*  714 */             (this.currentAction.equalsIgnoreCase("Move")) || 
/*  715 */             (this.currentAction.equalsIgnoreCase("Modify")) || 
/*  716 */             (this.currentAction.equalsIgnoreCase("Connect")) || 
/*  717 */             (this.currentAction.equalsIgnoreCase("Rotate")) || 
/*  718 */             (this.currentAction.equalsIgnoreCase("Flip")) || 
/*  719 */             (this.currentAction.equalsIgnoreCase("Extrap")) || 
/*  720 */             (this.currentAction.equalsIgnoreCase("Interp"))) {
/*  721 */             elem = (IConfigurationElement)itemMap.get(this.currentAction);
/*  722 */             if (elem != null) exeCommand(elem);
/*      */           }
/*      */         }
/*      */ 
/*  726 */         Rectangle r = this.scroll.getClientArea();
/*  727 */         this.paletteComp.setSize(this.paletteComp.computeSize(r.width, -1));
/*  728 */         this.paletteComp.layout();
/*      */       }
/*      */     }
/*      */     else {
/*  732 */       Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/*      */ 
/*  735 */       MessageBox mb = new MessageBox(shell, 40);
/*      */ 
/*  738 */       mb.setMessage("Pgen is not supported in this editor. Please select a mapEditor for Pgen to use first!");
/*  739 */       mb.open();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void widgetDefaultSelected(SelectionEvent se)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void widgetDisposed(DisposeEvent event)
/*      */   {
/*  758 */     if ((event.getSource() instanceof Button))
/*      */     {
/*  760 */       Button btn = (Button)event.getSource();
/*  761 */       this.buttonMap.remove(btn.getData());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void stacksUpdated(int undoSize, int redoSize)
/*      */   {
/*  774 */     if (this.undoButton != null) {
/*  775 */       if (undoSize <= 0) this.undoButton.setEnabled(false); else {
/*  776 */         this.undoButton.setEnabled(true);
/*      */       }
/*      */     }
/*  779 */     if (this.redoButton != null)
/*  780 */       if (redoSize <= 0) this.redoButton.setEnabled(false); else
/*  781 */         this.redoButton.setEnabled(true);
/*      */   }
/*      */ 
/*      */   public void partActivated(IWorkbenchPartReference partRef)
/*      */   {
/*  793 */     IWorkbenchPart part = partRef.getPart(false);
/*      */ 
/*  797 */     if (PgenUtil.isNatlCntrsEditor(part))
/*      */     {
/*  799 */       PgenResource rsc = PgenUtil.findPgenResource((AbstractEditor)part);
/*  800 */       if ((rsc == null) && (PgenUtil.getPgenMode() == PgenUtil.PgenMode.SINGLE)) rsc = PgenUtil.createNewResource();
/*  801 */       if (rsc != null) rsc.setCatFilter(new CategoryFilter(this.currentCategory == null ? "Any" : this.currentCategory));
/*  802 */       PgenSession.getInstance().setResource(rsc);
/*      */ 
/*  804 */       AbstractEditor editor = (AbstractEditor)part;
/*      */ 
/*  806 */       if (PgenUtil.getNumberofPanes(editor) > 1) {
/*  807 */         this.currentIsMultiPane = editor;
/*      */ 
/*  809 */         PgenUtil.addSelectedPaneChangedListener(editor, this);
/*      */       }
/*  811 */       activatePGENContext();
/*      */     }
/*  814 */     else if ((part instanceof PgenPaletteWindow)) {
/*  815 */       activatePGENContext();
/*      */ 
/*  819 */       AbstractEditor editor = PgenUtil.getActiveEditor();
/*  820 */       if (editor != null) {
/*  821 */         IRenderableDisplay display = editor.getActiveDisplayPane().getRenderableDisplay();
/*  822 */         ResourceList rscList = display.getDescriptor().getResourceList();
/*      */ 
/*  824 */         for (ResourcePair rp : rscList)
/*      */         {
/*  826 */           if ((rp != null) && ((rp.getResource() instanceof PgenResource)) && 
/*  827 */             (!rp.getProperties().isVisible())) {
/*  828 */             rp.getProperties().setVisible(true);
/*      */           }
/*      */         }
/*  831 */         editor.refresh();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void partBroughtToTop(IWorkbenchPartReference partRef)
/*      */   {
/*  845 */     IWorkbenchPart part = partRef.getPart(false);
/*      */ 
/*  847 */     partActivated(partRef);
/*      */ 
/*  849 */     if (PgenUtil.isNatlCntrsEditor(part)) {
/*  850 */       AbstractEditor editor = (AbstractEditor)part;
/*  851 */       PgenResource rsc = PgenUtil.findPgenResource((AbstractEditor)part);
/*      */ 
/*  853 */       if ((rsc != null) && (PgenUtil.getPgenMode() == PgenUtil.PgenMode.SINGLE) && 
/*  854 */         (PgenUtil.doesLayerLink()))
/*      */       {
/*  856 */         NcDisplayName dispName = PgenUtil.getDisplayName(editor);
/*      */ 
/*  858 */         if ((dispName != null) && 
/*  859 */           (dispName.getId() > 0) && (rsc != null))
/*      */         {
/*  861 */           Product prod = rsc.getActiveProduct();
/*  862 */           if (dispName.getId() <= prod.getLayers().size()) {
/*  863 */             rsc.setActiveLayer(prod.getLayer(dispName.getId() - 1));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  870 */       if (rsc != null) VizApp.runAsync(new ProductDialogStarter(rsc));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void partClosed(IWorkbenchPartReference partRef)
/*      */   {
/*  877 */     IWorkbenchPart part = partRef.getPart(false);
/*      */ 
/*  880 */     if ((part instanceof PgenPaletteWindow))
/*      */     {
/*  882 */       if (PgenUtil.getPgenMode() == PgenUtil.PgenMode.SINGLE) {
/*  883 */         PgenUtil.resetResourceData();
/*  884 */         if (VizPerspectiveListener.getCurrentPerspectiveManager() == null) return;
/*      */ 
/*  886 */         AbstractEditor[] editors = UiUtil.getEditors(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), 
/*  887 */           VizPerspectiveListener.getCurrentPerspectiveManager().getPerspectiveId());
/*      */ 
/*  892 */         for (int i = editors.length - 1; i >= 0; i--) {
/*  893 */           unloadPgenResource(editors[i]);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  898 */       if (this.currentIsMultiPane != null) {
/*  899 */         PgenUtil.removeSelectedPaneChangedListener(this.currentIsMultiPane, this);
/*      */       }
/*      */     }
/*  902 */     else if (PgenUtil.isNatlCntrsEditor(part)) {
/*  903 */       PgenResource pgen = PgenUtil.findPgenResource((AbstractEditor)part);
/*  904 */       if (pgen != null)
/*  905 */         pgen.closeDialogs();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void partDeactivated(IWorkbenchPartReference partRef)
/*      */   {
/*  912 */     IWorkbenchPart part = partRef.getPart(false);
/*      */ 
/*  915 */     if (PgenUtil.isNatlCntrsEditor(part))
/*      */     {
/*  917 */       PgenResource pgen = PgenUtil.findPgenResource((AbstractEditor)part);
/*  918 */       if (pgen != null)
/*      */       {
/*  930 */         deactivatePGENContext();
/*  931 */         ((AbstractEditor)part).refresh();
/*      */       }
/*      */ 
/*  934 */       AbstractEditor editor = (AbstractEditor)part;
/*      */ 
/*  936 */       if (PgenUtil.getNumberofPanes(editor) > 1) {
/*  937 */         this.currentIsMultiPane = null;
/*      */ 
/*  939 */         PgenUtil.removeSelectedPaneChangedListener(editor, this);
/*      */       }
/*      */ 
/*      */     }
/*  944 */     else if ((part instanceof PgenPaletteWindow)) {
/*  945 */       deactivatePGENContext();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void partOpened(IWorkbenchPartReference partRef)
/*      */   {
/*  953 */     IWorkbenchPart part = partRef.getPart(false);
/*  954 */     if ((part instanceof PgenPaletteWindow))
/*  955 */       ((PgenPaletteWindow)part).setPartName("PGEN");
/*      */   }
/*      */ 
/*      */   public void partHidden(IWorkbenchPartReference partRef)
/*      */   {
/*  961 */     IWorkbenchPart part = partRef.getPart(false);
/*      */ 
/*  964 */     if (PgenUtil.isNatlCntrsEditor(part)) {
/*  965 */       PgenResource pgen = PgenUtil.findPgenResource((AbstractEditor)part);
/*  966 */       if (pgen != null)
/*  967 */         pgen.closeDialogs();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void partInputChanged(IWorkbenchPartReference partRef)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void partVisible(IWorkbenchPartReference partRef)
/*      */   {
/*  979 */     IWorkbenchPart part = partRef.getPart(false);
/*      */ 
/*  981 */     if ((PgenUtil.isNatlCntrsEditor(part)) && (!PreloadGfaDataThread.loaded))
/*      */     {
/*  983 */       new PreloadGfaDataThread().start();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void unloadPgenResource(AbstractEditor editor)
/*      */   {
/*  990 */     for (IRenderableDisplay display : UiUtil.getDisplaysFromContainer(editor))
/*  991 */       for (ResourcePair rp : display.getDescriptor().getResourceList())
/*  992 */         if ((rp.getResource() instanceof PgenResource)) {
/*  993 */           PgenResource rsc = (PgenResource)rp.getResource();
/*  994 */           rsc.unload();
/*  995 */           display.getDescriptor().getResourceList().removePreRemoveListener(rsc);
/*      */         }
/*      */   }
/*      */ 
/*      */   public String getCurrentCategory()
/*      */   {
/* 1006 */     return this.currentCategory;
/*      */   }
/*      */ 
/*      */   private Image getIcon(String iconLocation)
/*      */   {
/* 1017 */     if (this.iconMap.containsKey(iconLocation)) {
/* 1018 */       return (Image)this.iconMap.get(iconLocation);
/*      */     }
/*      */ 
/* 1024 */     ImageDescriptor id = Activator.imageDescriptorFromPlugin(
/* 1025 */       "gov.noaa.nws.ncep.ui.pgen", iconLocation);
/* 1026 */     if (id != null) {
/* 1027 */       Image icon = id.createImage();
/*      */ 
/* 1029 */       this.iconMap.put(iconLocation, icon);
/* 1030 */       return icon;
/*      */     }
/*      */ 
/* 1033 */     return null;
/*      */   }
/*      */ 
/*      */   public String getCurrentAction()
/*      */   {
/* 1038 */     return this.currentAction;
/*      */   }
/*      */ 
/*      */   public void setActiveIcon(String name)
/*      */   {
/* 1048 */     if (!itemMap.containsKey(name)) return;
/*      */ 
/* 1050 */     if (!this.buttonMap.containsKey(name)) return;
/*      */ 
/* 1052 */     String iconLocation = ((IConfigurationElement)itemMap.get(name)).getAttribute("icon");
/*      */ 
/* 1057 */     if (this.activeIconMap.containsKey(iconLocation)) {
/* 1058 */       Image im = (Image)this.activeIconMap.get(iconLocation);
/* 1059 */       ((Button)this.buttonMap.get(name)).setImage(im);
/*      */     }
/*      */     else
/*      */     {
/* 1065 */       Image im = (Image)this.iconMap.get(iconLocation);
/* 1066 */       ImageData id = im.getImageData();
/*      */ 
/* 1068 */       for (int y = 0; y < id.height; y++) {
/* 1069 */         for (int x = 0; x < id.width; x++) {
/* 1070 */           if (id.getPixel(x, y) == 0)
/* 1071 */             id.setPixel(x, y, 16777215);
/*      */           else {
/* 1073 */             id.setPixel(x, y, 255);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1078 */       Image icon = new Image(im.getDevice(), id);
/* 1079 */       ((Button)this.buttonMap.get(name)).setImage(icon);
/* 1080 */       this.activeIconMap.put(iconLocation, icon);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void resetIcon(String name)
/*      */   {
/* 1092 */     if (!itemMap.containsKey(name)) return;
/*      */ 
/* 1094 */     if (!this.buttonMap.containsKey(name)) return;
/*      */ 
/* 1096 */     IConfigurationElement elem = (IConfigurationElement)itemMap.get(name);
/*      */ 
/* 1101 */     Image icon = getIcon(elem.getAttribute("icon"));
/* 1102 */     if (icon != null)
/* 1103 */       ((Button)this.buttonMap.get(name)).setImage(icon);
/*      */   }
/*      */ 
/*      */   private void exeCommand(IConfigurationElement elem)
/*      */   {
/* 1115 */     String commandId = elem.getAttribute("commandId");
/*      */ 
/* 1122 */     IEditorPart part = VizWorkbenchManager.getInstance().getActiveEditor();
/* 1123 */     ICommandService service = (ICommandService)part.getSite()
/* 1124 */       .getService(ICommandService.class);
/* 1125 */     Command cmd = service.getCommand(commandId);
/*      */ 
/* 1127 */     if (cmd != null)
/*      */     {
/*      */       try
/*      */       {
/* 1134 */         HashMap params = new HashMap();
/* 1135 */         params.put("editor", part);
/* 1136 */         params.put("name", elem.getAttribute("name"));
/* 1137 */         params.put("className", elem.getAttribute("className"));
/* 1138 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, null, 
/* 1139 */           elem.getAttribute("name"));
/*      */ 
/* 1142 */         cmd.executeWithChecks(exec);
/*      */ 
/* 1145 */         for (String toolbarID : NmapCommon.getGUIUpdateElementCommands()) {
/* 1146 */           service.refreshElements(toolbarID, null);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/* 1152 */         e.printStackTrace();
/* 1153 */         String msg = "Could not set PGEN drawing mode for the current map";
/* 1154 */         ErrorDialog.openError(Display.getCurrent().getActiveShell(), 
/* 1155 */           "Error Activating PGEN Tool", msg, 
/* 1156 */           new Status(4, "gov.noaa.nws.ncep.ui.pgen", msg, e));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public List<String> getControlNames()
/*      */   {
/* 1167 */     return this.controlNames;
/*      */   }
/*      */ 
/*      */   public List<String> getActionNames()
/*      */   {
/* 1175 */     return this.actionNames;
/*      */   }
/*      */ 
/*      */   public List<String> getClassNames()
/*      */   {
/* 1183 */     return this.classNames;
/*      */   }
/*      */ 
/*      */   public List<String> getObjectNames()
/*      */   {
/* 1191 */     return this.objectNames;
/*      */   }
/*      */ 
/*      */   public List<String> getObjectNames(String className)
/*      */   {
/* 1200 */     ArrayList objs = new ArrayList();
/* 1201 */     for (String name : getObjectNames()) {
/* 1202 */       if (((IConfigurationElement)itemMap.get(name)).getAttribute("className").equals(className)) objs.add(name);
/*      */     }
/* 1204 */     return objs;
/*      */   }
/*      */ 
/*      */   public Image getButtonImage(String bname)
/*      */   {
/* 1213 */     return getIcon(((IConfigurationElement)itemMap.get(bname)).getAttribute("icon"));
/*      */   }
/*      */ 
/*      */   public Image createNewImage(Image im, int fg, int bg)
/*      */   {
/* 1226 */     ImageData id = im.getImageData();
/*      */ 
/* 1228 */     for (int ii = 0; ii < id.height; ii++) {
/* 1229 */       for (int jj = 0; jj < id.width; jj++) {
/* 1230 */         if (id.getPixel(jj, ii) == 0)
/* 1231 */           id.setPixel(jj, ii, fg);
/*      */         else {
/* 1233 */           id.setPixel(jj, ii, bg);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1238 */     return new Image(im.getDevice(), id);
/*      */   }
/*      */ 
/*      */   public HashMap<String, IConfigurationElement> getItemMap()
/*      */   {
/* 1247 */     return itemMap;
/*      */   }
/*      */ 
/*      */   private void deactivatePGENContext() {
/* 1251 */     if (this.pgenContextActivation != null)
/*      */     {
/* 1253 */       IContextService ctxSvc = (IContextService)
/* 1254 */         PlatformUI.getWorkbench().getService(IContextService.class);
/* 1255 */       ctxSvc.deactivateContext(this.pgenContextActivation);
/*      */ 
/* 1257 */       this.pgenContextActivation = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void activatePGENContext() {
/* 1262 */     if (this.pgenContextActivation == null) {
/* 1263 */       IContextService ctxSvc = (IContextService)
/* 1264 */         PlatformUI.getWorkbench().getService(IContextService.class);
/* 1265 */       this.pgenContextActivation = ctxSvc.activateContext("gov.noaa.nws.ncep.ui.pgen.pgenContext");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDefaultAction()
/*      */   {
/* 1274 */     this.currentAction = "Select";
/*      */   }
/*      */ 
/*      */   public void selectedPanesChanged(String id, IDisplayPane[] pane)
/*      */   {
/*      */   }
/*      */ 
/*      */   public String getCurrentObject()
/*      */   {
/* 1293 */     return this.currentObject;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow
 * JD-Core Version:    0.6.2
 */