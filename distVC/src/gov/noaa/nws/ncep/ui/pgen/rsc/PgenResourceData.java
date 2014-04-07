/*      */ package gov.noaa.nws.ncep.ui.pgen.rsc;
/*      */ 
/*      */ import com.raytheon.uf.common.status.IUFStatusHandler;
/*      */ import com.raytheon.uf.common.status.UFStatus;
/*      */ import com.raytheon.uf.common.status.UFStatus.Priority;
/*      */ import com.raytheon.uf.viz.core.drawables.IDescriptor;
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.uf.viz.core.rsc.AbstractResourceData;
/*      */ import com.raytheon.uf.viz.core.rsc.IResourceDataChanged.ChangeType;
/*      */ import com.raytheon.uf.viz.core.rsc.LoadProperties;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.raytheon.viz.ui.perspectives.AbstractVizPerspectiveManager;
/*      */ import com.raytheon.viz.ui.perspectives.VizPerspectiveListener;
/*      */ import com.raytheon.viz.ui.tools.AbstractModalTool;
/*      */ import com.raytheon.viz.ui.tools.ModalToolManager;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.ui.pgen.Activator;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil.PgenMode;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.AddElementCommand;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.AddElementsCommand;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.CommandStackListener;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.DeleteAllCommand;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.DeleteElementCommand;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.DeletePartCommand;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.DeleteSelectedElementsCommand;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenCommand;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenCommandManager;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenRemindDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.ReplaceElementCommand;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.ReplaceElementsCommand;
/*      */ import gov.noaa.nws.ncep.ui.pgen.controls.StoreActivityDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.SymbolImageUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.ui.pgen.layering.PgenLayeringControlDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductManageDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.AbstractPgenDrawingTool;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.File;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import org.eclipse.jface.preference.IPreferenceStore;
/*      */ import org.eclipse.swt.graphics.Image;
/*      */ import org.eclipse.swt.graphics.ImageData;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class PgenResourceData extends AbstractResourceData
/*      */   implements CommandStackListener
/*      */ {
/*   80 */   private static final transient IUFStatusHandler statusHandler = UFStatus.getHandler(PgenResourceData.class);
/*      */   private List<Product> productList;
/*      */   private PgenCommandManager commandMgr;
/*   89 */   private Product activeProduct = null;
/*      */ 
/*   91 */   private ProductManageDialog productManageDlg = null;
/*      */ 
/*   96 */   private Layer activeLayer = null;
/*      */ 
/*   98 */   private PgenLayeringControlDialog layeringControlDlg = null;
/*      */   private String recoveryFilename;
/*  105 */   private String autoSaveFilename = null;
/*      */ 
/*  107 */   private boolean autosave = false;
/*      */   private long autosaveInterval;
/*  111 */   private long lastSaveTime = 0L;
/*      */ 
/*  113 */   private boolean multiSave = true;
/*      */ 
/*  115 */   private boolean needsSaving = false;
/*      */ 
/*  117 */   private int numberOfResources = 0;
/*      */ 
/*      */   public PgenResourceData()
/*      */   {
/*  123 */     this.productList = new ArrayList();
/*  124 */     this.commandMgr = new PgenCommandManager();
/*  125 */     this.commandMgr.addStackListener(this);
/*  126 */     this.recoveryFilename = 
/*  128 */       ("pgen_session." + 
/*  127 */       System.currentTimeMillis() + "." + hashCode() + 
/*  128 */       ".tmp");
/*  129 */     initializeProducts();
/*      */   }
/*      */ 
/*      */   public PgenResource construct(LoadProperties loadProperties, IDescriptor descriptor)
/*      */     throws VizException
/*      */   {
/*  143 */     this.numberOfResources += 1;
/*  144 */     return new PgenResource(this, loadProperties);
/*      */   }
/*      */ 
/*      */   public void update(Object updateData)
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/*  163 */     return obj == this;
/*      */   }
/*      */ 
/*      */   public List<Product> getProductList()
/*      */   {
/*  170 */     return this.productList;
/*      */   }
/*      */ 
/*      */   public PgenCommandManager getCommandMgr()
/*      */   {
/*  177 */     return this.commandMgr;
/*      */   }
/*      */ 
/*      */   public Product getActiveProduct()
/*      */   {
/*  184 */     return this.activeProduct;
/*      */   }
/*      */ 
/*      */   public void setActiveProduct(Product activeProduct)
/*      */   {
/*  192 */     this.activeProduct = activeProduct;
/*      */   }
/*      */ 
/*      */   public ProductManageDialog getProductManageDlg()
/*      */   {
/*  199 */     return this.productManageDlg;
/*      */   }
/*      */ 
/*      */   public void setProductManageDlg(ProductManageDialog productManageDlg)
/*      */   {
/*  207 */     this.productManageDlg = productManageDlg;
/*      */   }
/*      */ 
/*      */   public Layer getActiveLayer()
/*      */   {
/*  214 */     return this.activeLayer;
/*      */   }
/*      */ 
/*      */   public void setActiveLayer(Layer activeLayer)
/*      */   {
/*  222 */     this.activeLayer = activeLayer;
/*      */   }
/*      */ 
/*      */   public PgenLayeringControlDialog getLayeringControlDlg()
/*      */   {
/*  229 */     return this.layeringControlDlg;
/*      */   }
/*      */ 
/*      */   public void setLayeringControlDlg(PgenLayeringControlDialog layeringControlDlg)
/*      */   {
/*  238 */     this.layeringControlDlg = layeringControlDlg;
/*      */   }
/*      */ 
/*      */   public void startProductManage()
/*      */   {
/*  246 */     if (this.productManageDlg != null) {
/*  247 */       this.productManageDlg.close();
/*      */     }
/*      */ 
/*  250 */     if ((!this.activeProduct.getName().equalsIgnoreCase("Default")) || 
/*  251 */       (this.productList.size() > 1))
/*      */     {
/*  253 */       this.activeProduct.setOnOff(true);
/*  254 */       this.activeProduct.setOnOff(true);
/*      */ 
/*  256 */       activateProductManage();
/*  257 */     } else if (this.productList.size() == 1) {
/*  258 */       startLayering();
/*      */     } else {
/*  260 */       PgenUtil.setSelectingMode();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void activateProductManage()
/*      */   {
/*  270 */     if ((this.layeringControlDlg != null) && (this.layeringControlDlg.isOpen())) {
/*  271 */       this.layeringControlDlg.close();
/*      */     }
/*      */ 
/*  274 */     initializeProducts();
/*      */ 
/*  277 */     AbstractEditor mapEditor = PgenUtil.getActiveEditor();
/*      */ 
/*  279 */     if (this.productManageDlg == null)
/*      */     {
/*  281 */       Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
/*  282 */         .getShell();
/*  283 */       this.productManageDlg = new ProductManageDialog(shell);
/*      */     }
/*      */ 
/*  287 */     PgenUtil.setSelectingMode();
/*      */ 
/*  289 */     if (!this.productManageDlg.isOpen()) {
/*  290 */       this.productManageDlg.open();
/*      */     }
/*      */ 
/*  293 */     mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   public void closeDialogs()
/*      */   {
/*  302 */     if ((this.layeringControlDlg != null) && (this.layeringControlDlg.isOpen())) {
/*  303 */       this.layeringControlDlg.close();
/*      */     }
/*      */ 
/*  306 */     if ((this.productManageDlg != null) && (this.productManageDlg.isOpen()))
/*  307 */       this.productManageDlg.close();
/*      */   }
/*      */ 
/*      */   public void initializeProducts()
/*      */   {
/*  321 */     if (this.productList.size() == 0)
/*      */     {
/*  323 */       this.activeProduct = new Product("Default", "Default", "Default", 
/*  324 */         new ProductInfo(), new ProductTime(), 
/*  325 */         new ArrayList());
/*      */ 
/*  327 */       this.activeLayer = new Layer();
/*  328 */       this.activeProduct.addLayer(this.activeLayer);
/*      */ 
/*  330 */       this.productList.add(this.activeProduct);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void replaceElement(AbstractDrawableComponent old, AbstractDrawableComponent newde)
/*      */   {
/*  351 */     PgenCommand cmd = new ReplaceElementCommand(this.productList, old, newde);
/*  352 */     this.commandMgr.addCommand(cmd);
/*      */   }
/*      */ 
/*      */   public void replaceElements(DECollection parent, List<AbstractDrawableComponent> old, List<AbstractDrawableComponent> newde)
/*      */   {
/*  379 */     if ((old == null) || (old.isEmpty())) {
/*  380 */       parent = this.activeLayer;
/*      */     }
/*      */ 
/*  383 */     PgenCommand cmd = new ReplaceElementsCommand(parent, old, newde);
/*  384 */     this.commandMgr.addCommand(cmd);
/*      */   }
/*      */ 
/*      */   public void replaceProduct(List<Product> prds)
/*      */   {
/*  393 */     int index = 0;
/*  394 */     if (this.productList.size() > 0) {
/*  395 */       index = this.productList.indexOf(this.activeProduct);
/*  396 */       this.productList.set(index, (Product)prds.get(0));
/*      */     } else {
/*  398 */       this.productList.addAll(prds);
/*      */     }
/*      */ 
/*  404 */     this.activeProduct = ((Product)this.productList.get(index));
/*  405 */     this.activeLayer = ((Product)this.productList.get(index)).getLayer(0);
/*      */ 
/*  407 */     startProductManage();
/*      */   }
/*      */ 
/*      */   public void addProduct(List<Product> prds)
/*      */   {
/*  422 */     if ((this.productList.size() == 1) && 
/*  423 */       (((Product)this.productList.get(0)).getName().equals("Default")) && 
/*  424 */       (((Product)this.productList.get(0)).getType().equals("Default")) && 
/*  425 */       (((Product)this.productList.get(0)).getLayers().size() == 1))
/*      */     {
/*  427 */       if ((((Layer)((Product)this.productList.get(0)).getLayers().get(0)).getName()
/*  427 */         .equals("Default")) && 
/*  428 */         (((Layer)((Product)this.productList.get(0)).getLayers().get(0)).getDrawables().size() == 0))
/*      */       {
/*  430 */         if ((prds != null) && (prds.size() > 0)) {
/*  431 */           this.productList.clear();
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  437 */     int index = -1;
/*  438 */     if (this.productList.size() > 0) {
/*  439 */       index = this.productList.indexOf(this.activeProduct);
/*      */     }
/*      */ 
/*  443 */     this.productList.addAll(prds);
/*      */ 
/*  462 */     if (index < 0) {
/*  463 */       this.activeProduct = ((Product)this.productList.get(0));
/*  464 */       this.activeLayer = ((Product)this.productList.get(0)).getLayer(0);
/*      */     }
/*      */ 
/*  467 */     startProductManage();
/*      */   }
/*      */ 
/*      */   public void appendProduct(List<Product> prds)
/*      */   {
/*  488 */     if ((this.productList.size() == 1) && 
/*  489 */       (((Product)this.productList.get(0)).getName().equals("Default")) && 
/*  490 */       (((Product)this.productList.get(0)).getLayers().size() == 1))
/*      */     {
/*  492 */       if ((((Layer)((Product)this.productList.get(0)).getLayers().get(0)).getName()
/*  492 */         .equals("Default")) && 
/*  493 */         (((Layer)((Product)this.productList.get(0)).getLayers().get(0)).getDrawables().size() == 0))
/*      */       {
/*  495 */         if ((prds != null) && (prds.size() > 0)) {
/*  496 */           this.productList.clear();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  504 */     if (this.productList.size() < 1) {
/*  505 */       this.productList.add((Product)prds.get(0));
/*  506 */       this.activeProduct = ((Product)this.productList.get(0));
/*  507 */       this.activeLayer = ((Product)this.productList.get(0)).getLayer(0);
/*      */     } else {
/*  509 */       int nlayers = ((Product)prds.get(0)).getLayers().size();
/*      */ 
/*  512 */       if (nlayers == 1) {
/*  513 */         this.activeLayer.add(((Layer)((Product)prds.get(0)).getLayers().get(0)).getDrawables());
/*      */       }
/*      */       else {
/*  516 */         ArrayList layerUsed = new ArrayList();
/*  517 */         for (int ii = 0; ii < nlayers; ii++) {
/*  518 */           layerUsed.add(Boolean.valueOf(false));
/*      */         }
/*      */ 
/*  522 */         Layer matchLayer = ((Product)prds.get(0)).getLayer(this.activeLayer.getName());
/*  523 */         if (matchLayer == null) {
/*  524 */           matchLayer = ((Product)prds.get(0)).getLayer("Default");
/*      */         }
/*      */ 
/*  527 */         if (matchLayer != null) {
/*  528 */           this.activeLayer.add(matchLayer.getDrawables());
/*      */         }
/*      */ 
/*  532 */         int ii = 0;
/*      */         Layer mlyr;
/*  533 */         for (Layer lyr : ((Product)prds.get(0)).getLayers()) {
/*  534 */           if (lyr == matchLayer) {
/*  535 */             layerUsed.set(ii, Boolean.valueOf(true));
/*      */           } else {
/*  537 */             mlyr = this.activeProduct.getLayer(lyr.getName());
/*  538 */             if (mlyr != null) {
/*  539 */               mlyr.add(lyr.getDrawables());
/*  540 */               layerUsed.set(ii, Boolean.valueOf(true));
/*      */             }
/*      */           }
/*      */ 
/*  544 */           ii++;
/*      */         }
/*      */ 
/*  548 */         int jj = 0;
/*  549 */         for (Layer lyr : ((Product)prds.get(0)).getLayers()) {
/*  550 */           if (!((Boolean)layerUsed.get(jj)).booleanValue()) {
/*  551 */             this.activeProduct.addLayer(lyr);
/*      */           }
/*      */ 
/*  554 */           jj++;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  559 */     startProductManage();
/*      */   }
/*      */ 
/*      */   public void removeElement(AbstractDrawableComponent adc)
/*      */   {
/*  574 */     PgenCommand cmd = new DeleteElementCommand(this.productList, adc);
/*  575 */     this.commandMgr.addCommand(cmd);
/*      */   }
/*      */ 
/*      */   public void removeElements(List<AbstractDrawableComponent> adc)
/*      */   {
/*  584 */     PgenCommand cmd = new DeleteSelectedElementsCommand(this.productList, adc);
/*  585 */     this.commandMgr.addCommand(cmd);
/*      */   }
/*      */ 
/*      */   public void removeAllActiveDEs()
/*      */   {
/*  600 */     PgenCommand cmd = new DeleteSelectedElementsCommand(this.productList, 
/*  601 */       this.activeLayer.getDrawables());
/*      */ 
/*  603 */     this.commandMgr.addCommand(cmd);
/*      */   }
/*      */ 
/*      */   public void removeAllProducts()
/*      */   {
/*  618 */     PgenCommand cmd = new DeleteAllCommand(this.productList);
/*  619 */     this.commandMgr.addCommand(cmd);
/*      */   }
/*      */ 
/*      */   public void addElement(AbstractDrawableComponent de)
/*      */   {
/*  631 */     PgenCommand cmd = new AddElementCommand(this.productList, this.activeProduct, 
/*  632 */       this.activeLayer, de);
/*  633 */     this.commandMgr.addCommand(cmd);
/*      */   }
/*      */ 
/*      */   public void addElements(List<AbstractDrawableComponent> elems)
/*      */   {
/*  645 */     PgenCommand cmd = new AddElementsCommand(this.productList, this.activeProduct, 
/*  646 */       this.activeLayer, elems);
/*  647 */     this.commandMgr.addCommand(cmd);
/*      */   }
/*      */ 
/*      */   public void deleteElementPart(Line mpe, Coordinate pt1, Coordinate pt2)
/*      */   {
/*  663 */     PgenCommand cmd = new DeletePartCommand(this.productList, mpe, pt1, pt2);
/*  664 */     this.commandMgr.addCommand(cmd);
/*      */   }
/*      */ 
/*      */   public void startLayering()
/*      */   {
/*  673 */     if (this.layeringControlDlg != null) {
/*  674 */       this.layeringControlDlg.close();
/*      */     }
/*      */ 
/*  677 */     if ((!this.activeLayer.getName().equalsIgnoreCase("Default")) || 
/*  678 */       (this.activeProduct.getLayers().size() > 1))
/*      */     {
/*  680 */       activateLayering();
/*      */     }
/*  682 */     else PgenUtil.setSelectingMode();
/*      */   }
/*      */ 
/*      */   public void activateLayering()
/*      */   {
/*  692 */     if ((this.productManageDlg != null) && (this.productManageDlg.isOpen())) {
/*  693 */       this.productManageDlg.close();
/*      */     }
/*      */ 
/*  697 */     AbstractEditor mapEditor = PgenUtil.getActiveEditor();
/*      */ 
/*  699 */     if (this.layeringControlDlg == null)
/*      */     {
/*  701 */       Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
/*  702 */         .getShell();
/*  703 */       this.layeringControlDlg = new PgenLayeringControlDialog(shell);
/*      */     }
/*      */ 
/*  707 */     PgenUtil.setSelectingMode();
/*      */ 
/*  709 */     if (!this.layeringControlDlg.isOpen()) {
/*  710 */       this.layeringControlDlg.open();
/*      */     }
/*      */ 
/*  713 */     mapEditor.refresh();
/*      */   }
/*      */ 
/*      */   public void setAutoSaveFilename(String autoSaveFilename)
/*      */   {
/*  722 */     this.autoSaveFilename = autoSaveFilename;
/*      */   }
/*      */ 
/*      */   public void setAutosave(boolean autosave)
/*      */   {
/*  730 */     this.autosave = autosave;
/*      */   }
/*      */ 
/*      */   public synchronized void cleanup(BufferedImage paneImage)
/*      */   {
/*  742 */     this.numberOfResources -= 1;
/*  743 */     if (this.numberOfResources != 0) {
/*  744 */       return;
/*      */     }
/*  746 */     this.commandMgr.flushStacks();
/*  747 */     this.commandMgr.removeStackListener(this);
/*      */ 
/*  752 */     removeTempFile();
/*      */ 
/*  754 */     if (this.autosave) {
/*  755 */       storeAllProducts();
/*      */     }
/*      */ 
/*  758 */     if (this.needsSaving) {
/*  759 */       promptToSave(paneImage);
/*      */     }
/*      */ 
/*  762 */     if (PgenUtil.getPgenMode() == PgenUtil.PgenMode.SINGLE)
/*  763 */       PgenUtil.resetResourceData();
/*  764 */     deactivatePgenTools();
/*      */   }
/*      */ 
/*      */   private void deactivatePgenTools()
/*      */   {
/*  772 */     AbstractVizPerspectiveManager mgr = 
/*  773 */       VizPerspectiveListener.getCurrentPerspectiveManager();
/*  774 */     if (mgr != null)
/*      */     {
/*  776 */       Iterator localIterator = mgr.getToolManager()
/*  776 */         .getSelectedModalTools().iterator();
/*      */ 
/*  775 */       while (localIterator.hasNext()) {
/*  776 */         AbstractModalTool mt = (AbstractModalTool)localIterator.next();
/*  777 */         if ((mt instanceof AbstractPgenDrawingTool))
/*  778 */           ((AbstractPgenDrawingTool)mt).deactivateTool();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void removeTempFile()
/*      */   {
/*  789 */     String filepath = PgenUtil.getTempWorkDir() + File.separator + 
/*  790 */       this.recoveryFilename;
/*      */ 
/*  792 */     File tmpFile = new File(filepath);
/*  793 */     if (tmpFile.exists())
/*  794 */       tmpFile.delete();
/*      */   }
/*      */ 
/*      */   private void recoverySave()
/*      */   {
/*  803 */     String filepath = PgenUtil.getTempWorkDir() + File.separator + 
/*  804 */       this.recoveryFilename;
/*  805 */     PgenResource rsc = PgenSession.getInstance().getPgenResource();
/*  806 */     ArrayList prds = (ArrayList)rsc.getProducts();
/*      */ 
/*  808 */     Products filePrds = ProductConverter.convert(prds);
/*  809 */     FileTools.write(filepath, filePrds);
/*      */   }
/*      */ 
/*      */   public void saveProducts(String filename, boolean postSave)
/*      */   {
/*  831 */     this.multiSave = postSave;
/*      */ 
/*  833 */     ArrayList prds = (ArrayList)this.productList;
/*      */ 
/*  835 */     if ((filename != null) && (!prds.isEmpty()))
/*      */     {
/*  837 */       Products filePrds = ProductConverter.convert(prds);
/*      */ 
/*  842 */       FileTools.write(filename, filePrds);
/*      */ 
/*  847 */       int lastind = filename.lastIndexOf("/");
/*  848 */       if (lastind < 0)
/*  849 */         filename = new String(PgenUtil.getWorkingDirectory() + filename);
/*      */       String dlftPrdSaveDir;
/*      */       String dlftPrdSaveDir;
/*  853 */       if (filename.endsWith(".xml"))
/*  854 */         dlftPrdSaveDir = new String(filename.substring(0, 
/*  855 */           filename.length() - 4) + 
/*  856 */           "_post");
/*      */       else {
/*  858 */         dlftPrdSaveDir = new String(filename + "_post");
/*      */       }
/*      */ 
/*  861 */       ArrayList onePrd = new ArrayList();
/*      */       Iterator localIterator2;
/*  862 */       label885: for (Iterator localIterator1 = prds.iterator(); localIterator1.hasNext(); 
/*  940 */         localIterator2.hasNext())
/*      */       {
/*  862 */         Product prd = (Product)localIterator1.next();
/*      */ 
/*  864 */         onePrd.clear();
/*      */ 
/*  866 */         Product backupPrd = prd.copy();
/*  867 */         onePrd.add(backupPrd);
/*      */ 
/*  871 */         String givenFile = prd.getOutputFile();
/*      */ 
/*  873 */         String givenPrdFile = null;
/*  874 */         if (givenFile != null) {
/*  875 */           givenPrdFile = givenFile.trim();
/*      */         }
/*      */ 
/*  878 */         String givenPrdSaveDir = null;
/*  879 */         String givenPrdSaveFile = null;
/*      */ 
/*  881 */         if ((givenPrdFile != null) && (givenPrdFile.length() > 0))
/*      */         {
/*  883 */           int lind = givenPrdFile.lastIndexOf("/");
/*      */ 
/*  885 */           if (givenPrdFile.endsWith(".xml")) {
/*  886 */             if (lind >= 0) {
/*  887 */               givenPrdSaveDir = givenPrdFile.substring(0, lind);
/*  888 */               givenPrdSaveFile = givenPrdFile.substring(lind + 1, 
/*  889 */                 givenPrdFile.length());
/*      */             } else {
/*  891 */               givenPrdSaveFile = new String(givenPrdFile);
/*      */             }
/*      */ 
/*  894 */             if (givenPrdSaveFile.length() == 4) {
/*  895 */               givenPrdSaveFile = null;
/*      */             }
/*      */ 
/*      */           }
/*  899 */           else if (lind >= 0) {
/*  900 */             givenPrdSaveDir = new String(givenPrdFile);
/*      */           } else {
/*  902 */             givenPrdSaveDir = new String(
/*  903 */               PgenUtil.getWorkingDirectory() + "/" + 
/*  904 */               givenPrdFile);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  910 */         Products singlePrd = ProductConverter.convert(onePrd);
/*  911 */         if (givenPrdSaveDir != null) {
/*  912 */           String outpf = new String(givenPrdSaveDir + "/" + 
/*  913 */             prd.getName() + ".xml");
/*  914 */           if (givenPrdSaveFile != null) {
/*  915 */             outpf = new String(givenPrdSaveDir + "/" + 
/*  916 */               givenPrdSaveFile);
/*      */           }
/*      */ 
/*  919 */           FileTools.write(outpf, singlePrd);
/*      */         }
/*  921 */         else if (givenPrdSaveFile != null) {
/*  922 */           String outpf = new String(
/*  923 */             PgenUtil.getWorkingDirectory() + "/" + 
/*  924 */             givenPrdSaveFile);
/*  925 */           FileTools.write(outpf, singlePrd);
/*      */         }
/*      */ 
/*  930 */         String defaultPrdFile = new String(dlftPrdSaveDir + "/" + 
/*  931 */           prd.getName() + ".xml");
/*      */ 
/*  934 */         if ((!postSave) && (!prd.isSaveLayers())) {
/*      */           break label885;
/*      */         }
/*  937 */         FileTools.write(defaultPrdFile, singlePrd);
/*      */ 
/*  940 */         localIterator2 = prd.getLayers().iterator(); continue; Layer lyr = (Layer)localIterator2.next();
/*      */ 
/*  942 */         backupPrd.clear();
/*  943 */         backupPrd.addLayer(lyr);
/*      */ 
/*  945 */         onePrd.clear();
/*  946 */         onePrd.add(backupPrd);
/*      */ 
/*  948 */         String outlyrfile = new String(dlftPrdSaveDir + "/" + 
/*  949 */           prd.getName() + "_post/" + lyr.getName() + 
/*  950 */           ".xml");
/*      */ 
/*  952 */         Products oneLayerPrd = ProductConverter.convert(onePrd);
/*  953 */         FileTools.write(outlyrfile, oneLayerPrd);
/*      */ 
/*  956 */         if (givenPrdSaveDir != null)
/*      */         {
/*      */           String outpf;
/*      */           String outpf;
/*  959 */           if (givenPrdSaveFile != null) {
/*  960 */             String pname = givenPrdSaveFile.substring(0, 
/*  961 */               givenPrdSaveFile.length() - 4);
/*  962 */             outpf = new String(givenPrdSaveDir + "/" + 
/*  963 */               pname + "_post/" + lyr.getName() + 
/*  964 */               ".xml");
/*      */           } else {
/*  966 */             outpf = new String(givenPrdSaveDir + "/" + 
/*  967 */               prd.getName() + "_post/" + 
/*  968 */               lyr.getName() + ".xml");
/*      */           }
/*      */ 
/*  971 */           FileTools.write(outpf, oneLayerPrd);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  978 */       this.needsSaving = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void saveProducts_prev(String filename, boolean multiSave)
/*      */   {
/*  992 */     this.multiSave = multiSave;
/*      */ 
/*  994 */     ArrayList prds = (ArrayList)this.productList;
/*      */ 
/*  996 */     if ((filename != null) && (!prds.isEmpty()))
/*      */     {
/* 1006 */       String infile = null;
/* 1007 */       String outfile = null;
/* 1008 */       String oneFile = filename
/* 1009 */         .substring(0, filename.lastIndexOf(".xml"));
/*      */       Iterator localIterator2;
/* 1011 */       for (Iterator localIterator1 = prds.iterator(); localIterator1.hasNext(); 
/* 1023 */         localIterator2.hasNext())
/*      */       {
/* 1011 */         Product prd = (Product)localIterator1.next();
/* 1012 */         prd.setUseFile(true);
/* 1013 */         outfile = prd.getOutputFile();
/* 1014 */         if ((outfile == null) || (outfile.trim().length() == 0)) {
/* 1015 */           prd.setOutputFile(oneFile + "_" + prd.getName() + ".xml");
/*      */         }
/*      */ 
/* 1018 */         infile = prd.getInputFile();
/* 1019 */         if ((infile == null) || (infile.trim().length() == 0)) {
/* 1020 */           prd.setInputFile(prd.getOutputFile());
/*      */         }
/*      */ 
/* 1023 */         localIterator2 = prd.getLayers().iterator(); continue; Layer lyr = (Layer)localIterator2.next();
/* 1024 */         outfile = lyr.getOutputFile();
/* 1025 */         if ((outfile == null) || (outfile.trim().length() == 0)) {
/* 1026 */           lyr.setOutputFile(oneFile + "_" + prd.getName() + "_" + 
/* 1027 */             lyr.getName() + ".xml");
/*      */         }
/*      */ 
/* 1030 */         infile = lyr.getInputFile();
/* 1031 */         if ((infile == null) || (infile.trim().length() == 0)) {
/* 1032 */           lyr.setInputFile(lyr.getOutputFile());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1037 */       Products filePrds = ProductConverter.convert(prds);
/*      */ 
/* 1044 */       if (multiSave) {
/* 1045 */         ArrayList onePrd = new ArrayList();
/* 1046 */         for (Product prd : prds)
/*      */         {
/* 1048 */           onePrd.clear();
/* 1049 */           prd.setUseFile(true);
/*      */ 
/* 1051 */           Product backupPrd = prd.copy();
/* 1052 */           onePrd.add(backupPrd);
/*      */ 
/* 1054 */           FileTools.write(backupPrd.getOutputFile(), 
/* 1055 */             ProductConverter.convert(onePrd));
/*      */ 
/* 1058 */           if (prd.isSaveLayers()) {
/* 1059 */             for (Layer lyr : prd.getLayers())
/*      */             {
/* 1061 */               backupPrd.clear();
/* 1062 */               backupPrd.addLayer(lyr);
/* 1063 */               backupPrd.setInputFile(lyr.getInputFile());
/* 1064 */               backupPrd.setOutputFile(lyr.getOutputFile());
/*      */ 
/* 1066 */               onePrd.clear();
/* 1067 */               onePrd.add(backupPrd);
/*      */ 
/* 1069 */               FileTools.write(lyr.getOutputFile(), 
/* 1070 */                 ProductConverter.convert(onePrd));
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1083 */       FileTools.write(filename, filePrds);
/* 1084 */       this.needsSaving = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void stacksUpdated(int undoSize, int redoSize)
/*      */   {
/* 1094 */     if (undoSize + redoSize == 0) {
/* 1095 */       return;
/*      */     }
/* 1097 */     this.needsSaving = true;
/*      */ 
/* 1101 */     IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
/* 1102 */     this.autosaveInterval = (prefs.getLong("PGEN_AUTOSAVE_FREQ") * 60L * 1000L);
/*      */ 
/* 1108 */     recoverySave();
/*      */ 
/* 1114 */     if (this.autosave) {
/* 1115 */       long current = System.currentTimeMillis();
/* 1116 */       if (current - this.lastSaveTime > this.autosaveInterval) {
/* 1117 */         storeAllProducts();
/*      */ 
/* 1119 */         this.lastSaveTime = current;
/* 1120 */         this.needsSaving = false;
/*      */       }
/*      */     }
/*      */ 
/* 1124 */     fireChangeListeners(IResourceDataChanged.ChangeType.DATA_UPDATE, null);
/*      */   }
/*      */ 
/*      */   public void promptToSave(BufferedImage paneImage)
/*      */   {
/* 1136 */     ImageData tmpdata = SymbolImageUtil.convertToSWT(paneImage);
/* 1137 */     ImageData idata = tmpdata.scaledTo(tmpdata.width / 2, 
/* 1138 */       tmpdata.height / 2);
/* 1139 */     Image im = new Image(PlatformUI.getWorkbench().getDisplay(), idata);
/*      */ 
/* 1144 */     PgenRemindDialog confirmDlg = new PgenRemindDialog(
/* 1145 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), im);
/*      */ 
/* 1147 */     confirmDlg.open();
/*      */ 
/* 1152 */     if (confirmDlg.getReturnCode() == 0) {
/* 1153 */       StoreActivityDialog storeDialog = null;
/*      */       try {
/* 1155 */         storeDialog = new StoreActivityDialog(PlatformUI.getWorkbench()
/* 1156 */           .getActiveWorkbenchWindow().getShell(), "Save As");
/*      */       } catch (VizException e) {
/* 1158 */         e.printStackTrace();
/*      */       }
/* 1160 */       if (storeDialog != null)
/* 1161 */         storeDialog.open();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean saveOneProduct(Product prd, String outfile)
/*      */   {
/* 1175 */     if (prd == null) {
/* 1176 */       return false;
/*      */     }
/*      */ 
/* 1179 */     ArrayList prds = new ArrayList();
/* 1180 */     prds.add(prd);
/*      */ 
/* 1182 */     String filename = buildFileName(prd);
/* 1183 */     if (outfile != null) {
/* 1184 */       filename = new String(outfile);
/*      */     }
/*      */ 
/* 1187 */     prd.setOutputFile(filename);
/*      */ 
/* 1190 */     Products filePrds = ProductConverter.convert(prds);
/* 1191 */     FileTools.write(filename, filePrds);
/*      */ 
/* 1194 */     if (prd.isSaveLayers())
/*      */     {
/* 1196 */       Product backupPrd = prd.copy();
/*      */ 
/* 1198 */       for (Layer lyr : prd.getLayers()) {
/* 1199 */         backupPrd.clear();
/* 1200 */         backupPrd.addLayer(lyr);
/*      */ 
/* 1202 */         prds.clear();
/* 1203 */         prds.add(backupPrd);
/*      */ 
/* 1205 */         String outlyrfile = new String(filename.substring(0, 
/* 1206 */           filename.length() - 4) + 
/* 1207 */           "." + lyr.getName() + ".xml");
/*      */ 
/* 1209 */         Products oneLayerPrd = ProductConverter.convert(prds);
/* 1210 */         FileTools.write(outlyrfile, oneLayerPrd);
/*      */       }
/*      */     }
/*      */ 
/* 1214 */     this.needsSaving = false;
/*      */ 
/* 1216 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean saveCurrentProduct(String fileName)
/*      */   {
/* 1228 */     return saveOneProduct(getActiveProduct(), fileName);
/*      */   }
/*      */ 
/*      */   public boolean saveAllProducts()
/*      */   {
/* 1239 */     for (Product pp : this.productList) {
/* 1240 */       String ofile = pp.getOutputFile();
/* 1241 */       pp.setInputFile(ofile);
/* 1242 */       saveOneProduct(pp, ofile);
/*      */     }
/*      */ 
/* 1245 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean storeCurrentProduct(String label)
/*      */   {
/* 1252 */     return storeProduct(getActiveProduct(), label);
/*      */   }
/*      */ 
/*      */   public boolean storeAllProducts()
/*      */   {
/* 1261 */     for (Product pp : this.productList) {
/* 1262 */       String ofile = pp.getOutputFile();
/* 1263 */       pp.setInputFile(ofile);
/* 1264 */       storeProduct(pp, ofile);
/*      */     }
/*      */ 
/* 1267 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean storeProduct(Product prd, String label)
/*      */   {
/* 1272 */     if (prd == null) {
/* 1273 */       return false;
/*      */     }
/*      */ 
/* 1279 */     String activityLabel = null;
/* 1280 */     if (label != null)
/* 1281 */       activityLabel = new String(label);
/*      */     else {
/* 1283 */       activityLabel = buildActivityLabel(prd);
/*      */     }
/*      */ 
/* 1286 */     prd.setOutputFile(activityLabel);
/*      */     try
/*      */     {
/* 1291 */       StorageUtils.storeProduct(prd);
/*      */     } catch (PgenStorageException e) {
/* 1293 */       statusHandler.handle(UFStatus.Priority.PROBLEM, e.getLocalizedMessage(), e);
/* 1294 */       return false;
/*      */     }
/*      */ 
/* 1297 */     this.needsSaving = false;
/* 1298 */     return true;
/*      */   }
/*      */ 
/*      */   public String buildFileName(Product prd)
/*      */   {
/* 1307 */     String sfile = null;
/* 1308 */     if (this.productManageDlg != null) {
/* 1309 */       sfile = this.productManageDlg.getPrdOutputFile(prd);
/*      */     }
/*      */     else {
/* 1312 */       StringBuilder sdir = new StringBuilder();
/*      */ 
/* 1314 */       sdir.append(PgenUtil.getPgenOprDirectory() + File.separator + 
/* 1315 */         "Default.");
/*      */ 
/* 1317 */       sdir.append(PgenUtil.formatDate(Calendar.getInstance()) + ".");
/*      */ 
/* 1319 */       sdir.append(Calendar.getInstance().get(11) + 
/* 1320 */         ".xml");
/*      */ 
/* 1322 */       sfile = new String(sdir.toString());
/*      */     }
/*      */ 
/* 1325 */     return sfile;
/*      */   }
/*      */ 
/*      */   public String buildActivityLabel(Product prd)
/*      */   {
/* 1333 */     String sfile = null;
/* 1334 */     if (this.productManageDlg != null) {
/* 1335 */       String temp = this.productManageDlg.getPrdOutputFile(prd);
/* 1336 */       int idx = temp.lastIndexOf(File.separator);
/* 1337 */       sfile = temp.substring(idx + 1);
/*      */     }
/*      */     else {
/* 1340 */       StringBuilder sdir = new StringBuilder();
/*      */ 
/* 1342 */       sdir.append("Default.");
/*      */ 
/* 1344 */       sdir.append(PgenUtil.formatDate(Calendar.getInstance()) + ".");
/*      */ 
/* 1346 */       sdir.append(Calendar.getInstance().get(11) + 
/* 1347 */         ".xml");
/*      */ 
/* 1349 */       sfile = new String(sdir.toString());
/*      */     }
/*      */ 
/* 1352 */     return sfile;
/*      */   }
/*      */ 
/*      */   public boolean isNeedsSaving()
/*      */   {
/* 1359 */     return this.needsSaving;
/*      */   }
/*      */ 
/*      */   public void setNeedsSaving(boolean save) {
/* 1363 */     this.needsSaving = save;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.rsc.PgenResourceData
 * JD-Core Version:    0.6.2
 */