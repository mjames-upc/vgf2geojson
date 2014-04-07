/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.IDisplayPaneContainer;
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.EditorUtil;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlgFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IEditorPart;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchPage;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public abstract class AbstractPgenDrawingTool extends AbstractPgenTool
/*     */ {
/*     */   protected IInputHandler mouseHandler;
/*     */   protected AttrDlg attrDlg;
/*  59 */   protected String pgenType = null;
/*     */ 
/*  64 */   protected String pgenCategory = null;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  73 */     IEditorPart ep = EditorUtil.getActiveEditor();
/*     */ 
/*  75 */     if (!PgenUtil.isNatlCntrsEditor(ep))
/*     */     {
/*  77 */       return;
/*     */     }
/*     */ 
/*  80 */     if (!super.isDelObj())
/*     */     {
/*  84 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow()
/*  85 */         .getActivePage().activate(EditorUtil.getActiveEditor());
/*     */     }
/*     */ 
/*  88 */     super.activateTool();
/*     */ 
/*  95 */     String param = this.event.getParameter("name");
/*  96 */     if (param != null) this.pgenType = param;
/*  97 */     param = this.event.getParameter("className");
/*  98 */     if (param != null) this.pgenCategory = param;
/*     */ 
/* 100 */     if (super.isDelObj())
/*     */     {
/* 103 */       PgenSession.getInstance().getPgenPalette().setActiveIcon("Delete Obj");
/* 104 */       PgenUtil.setDelObjMode();
/*     */ 
/* 109 */       int numEls = this.drawingLayer.selectObj(this.pgenType);
/*     */ 
/* 111 */       if (numEls > 0)
/*     */       {
/* 113 */         MessageDialog confirmDlg = new MessageDialog(
/* 114 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 116 */           "Confirm Delete", null, "Are you sure you want to delete all " + numEls + " selected element(s)?", 
/* 117 */           3, new String[] { "OK", "Cancel" }, 0);
/*     */ 
/* 119 */         confirmDlg.open();
/*     */ 
/* 121 */         if (confirmDlg.getReturnCode() == 0)
/*     */         {
/* 123 */           this.drawingLayer.deleteSelectedElements();
/*     */         }
/*     */         else
/*     */         {
/* 128 */           this.drawingLayer.removeSelected();
/*     */         }
/*     */ 
/* 132 */         if ((ep instanceof IDisplayPaneContainer)) {
/* 133 */           ((IDisplayPaneContainer)ep).refresh();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 138 */       return;
/*     */     }
/*     */ 
/* 145 */     if (this.attrDlg == null)
/*     */     {
/* 147 */       this.attrDlg = AttrDlgFactory.createAttrDlg(this.pgenType, null, 
/* 148 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/*     */     }
/*     */ 
/* 155 */     if (this.attrDlg == null)
/*     */     {
/* 157 */       this.attrDlg = AttrDlgFactory.createAttrDlg(this.pgenCategory, this.pgenType, 
/* 158 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/*     */     }
/*     */ 
/* 162 */     if (this.attrDlg != null)
/*     */     {
/* 164 */       this.attrDlg.setType(this.event.getParameter("type"));
/* 165 */       this.attrDlg.setMouseHandlerName(getMouseHandler().getClass().getName());
/*     */ 
/* 170 */       this.attrDlg.setBlockOnOpen(false);
/* 171 */       this.attrDlg.setPgenCategory(this.pgenCategory);
/* 172 */       this.attrDlg.setPgenType(this.pgenType);
/* 173 */       this.attrDlg.setDrawingLayer(this.drawingLayer);
/* 174 */       this.attrDlg.setMapEditor(this.mapEditor);
/*     */ 
/* 176 */       if (this.attrDlg.getShell() == null) {
/* 177 */         this.attrDlg.open();
/*     */       }
/*     */ 
/* 180 */       this.attrDlg.setBlockOnOpen(true);
/* 181 */       this.attrDlg.setDefaultAttr();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/* 211 */     super.deactivateTool();
/*     */ 
/* 213 */     if (this.editor != null)
/* 214 */       this.editor.unregisterMouseHandler(this.mouseHandler);
/* 215 */     if (this.attrDlg != null) {
/* 216 */       this.attrDlg.close();
/* 217 */       this.attrDlg = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setHandler(IInputHandler handler)
/*     */   {
/* 226 */     if ((this.mapEditor != null) && (handler != null)) {
/* 227 */       this.mapEditor.unregisterMouseHandler(this.mouseHandler);
/* 228 */       this.mouseHandler = handler;
/* 229 */       this.mapEditor.registerMouseHandler(this.mouseHandler);
/*     */     }
/*     */ 
/* 232 */     if ((handler instanceof InputHandlerDefaultImpl))
/* 233 */       ((InputHandlerDefaultImpl)handler).preprocess();
/*     */   }
/*     */ 
/*     */   public AttrDlg getAttrDlg()
/*     */   {
/* 239 */     return this.attrDlg;
/*     */   }
/*     */ 
/*     */   public void setAttrDlg(AttrDlg attrDlg) {
/* 243 */     this.attrDlg = attrDlg;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.AbstractPgenDrawingTool
 * JD-Core Version:    0.6.2
 */