/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.IDisplayPaneContainer;
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.EditorUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.VolcanoCreateDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchPage;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenVolcanoCreateTool extends AbstractPgenTool
/*     */ {
/*     */   protected AttrDlg attrDlg;
/*  44 */   protected String pgenType = null;
/*  45 */   protected String pgenCategory = null;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  50 */     PlatformUI.getWorkbench().getActiveWorkbenchWindow()
/*  51 */       .getActivePage().activate(EditorUtil.getActiveEditor());
/*     */ 
/*  53 */     super.activateTool();
/*     */ 
/*  60 */     if (isUsedVolProd()) {
/*  61 */       openConfirmBox("Please start with Volcano Activity to create a new volcano.");
/*  62 */       return;
/*     */     }
/*     */ 
/*  66 */     String param = this.event.getParameter("name");
/*  67 */     if (param != null) this.pgenType = param;
/*  68 */     param = this.event.getParameter("className");
/*  69 */     if (param != null) this.pgenCategory = param;
/*     */ 
/*  71 */     if (super.isDelObj())
/*     */     {
/*  73 */       int numEls = this.drawingLayer.selectObj(this.pgenType);
/*     */ 
/*  75 */       if (numEls > 0)
/*     */       {
/*  77 */         MessageDialog confirmDlg = new MessageDialog(
/*  78 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  79 */           "Confirm Delete", null, "Are you sure you want to delete all " + numEls + " selected element(s)?", 
/*  80 */           3, new String[] { "OK", "Cancel" }, 0);
/*     */ 
/*  82 */         confirmDlg.open();
/*     */ 
/*  84 */         if (confirmDlg.getReturnCode() == 0)
/*  85 */           this.drawingLayer.deleteSelectedElements();
/*     */         else {
/*  87 */           this.drawingLayer.removeSelected();
/*     */         }
/*  89 */         this.editor.refresh();
/*     */       }
/*     */ 
/*  92 */       return;
/*     */     }
/*     */ 
/*  96 */     VolcanoCreateDlg attrDlg = 
/*  97 */       VolcanoCreateDlg.getInstance(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/*     */ 
/*  99 */     if (attrDlg != null)
/*     */     {
/* 101 */       attrDlg.setBlockOnOpen(false);
/* 102 */       attrDlg.open();
/*     */ 
/* 104 */       attrDlg.setDefaultAttr();
/*     */ 
/* 106 */       attrDlg.setPgenCategory(this.pgenCategory);
/* 107 */       attrDlg.setPgenType(this.pgenType);
/* 108 */       attrDlg.setDrawingLayer(this.drawingLayer);
/* 109 */       attrDlg.setMapEditor(this.mapEditor);
/* 110 */       attrDlg.setBlockOnOpen(true);
/*     */ 
/* 112 */       attrDlg.setCreateTool(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/* 121 */     super.deactivateTool();
/*     */ 
/* 125 */     if (this.attrDlg != null) {
/* 126 */       this.attrDlg.close();
/* 127 */       this.attrDlg = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isUsedVolProd()
/*     */   {
/* 140 */     if (this.drawingLayer == null) {
/* 141 */       return true;
/*     */     }
/* 143 */     Product prod = this.drawingLayer.getActiveProduct();
/*     */ 
/* 145 */     if (prod == null) {
/* 146 */       return true;
/*     */     }
/*     */ 
/* 149 */     if (!"VOLCANO".equalsIgnoreCase(prod.getType())) {
/* 150 */       return true;
/*     */     }
/*     */ 
/* 158 */     if (VaaInfo.VOL_PROD_MAP.containsValue(prod))
/*     */     {
/* 160 */       List lyrList = prod.getLayers();
/*     */ 
/* 163 */       if (lyrList == null) {
/* 164 */         return true;
/*     */       }
/* 166 */       boolean isCloudExist = false;
/*     */ 
/* 168 */       for (Layer lyr : lyrList) {
/* 169 */         List list = lyr.getDrawables();
/*     */ 
/* 171 */         if (list != null) {
/* 172 */           for (AbstractDrawableComponent adc : list)
/*     */           {
/* 174 */             if ((adc != null) && ("VOLC_SIGMET".equals(adc.getPgenType()))) {
/* 175 */               return true;
/*     */             }
/*     */ 
/* 178 */             if ((adc != null) && ("VACL_SIGMET".equals(adc.getPgenType()))) {
/* 179 */               isCloudExist = true;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 190 */       if (isCloudExist) {
/* 191 */         openConfirmBox("Not Deleted Ash Clouds of the Deleted Volcano Should Be\nDeleted Or Will Belong to the Newly Created Volcano!\n");
/*     */       }
/*     */ 
/* 195 */       return false;
/*     */     }
/* 197 */     return false;
/*     */   }
/*     */ 
/*     */   private void openConfirmBox(String msg)
/*     */   {
/* 209 */     MessageDialog confirmDlg = new MessageDialog(
/* 210 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 211 */       "Message", null, msg, 
/* 212 */       2, new String[] { "OK" }, 0);
/*     */ 
/* 214 */     confirmDlg.open();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 219 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenVolcanoCreateTool
 * JD-Core Version:    0.6.2
 */