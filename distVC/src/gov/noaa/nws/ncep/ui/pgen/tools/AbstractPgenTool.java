/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.raytheon.viz.ui.tools.AbstractModalTool;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ 
/*     */ public abstract class AbstractPgenTool extends AbstractModalTool
/*     */ {
/*  45 */   protected AbstractEditor mapEditor = null;
/*     */ 
/*  47 */   protected String buttonName = null;
/*     */   private static boolean delObjFlag;
/*  49 */   private IInputHandler inputHandler = null;
/*     */   protected PgenResource drawingLayer;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  63 */     if (PgenSession.getInstance().getPgenPalette() == null) return;
/*     */ 
/*  67 */     if ((this.editor instanceof AbstractEditor)) {
/*  68 */       this.mapEditor = ((AbstractEditor)this.editor);
/*     */     }
/*     */ 
/*  73 */     String param = this.event.getParameter("name");
/*  74 */     if (param != null) this.buttonName = param;
/*     */ 
/*  77 */     PgenSession.getInstance().getPgenPalette().setActiveIcon(this.buttonName);
/*     */ 
/*  80 */     this.drawingLayer = PgenSession.getInstance().getPgenResource();
/*     */ 
/*  82 */     if ((this instanceof PgenDeleteObj)) {
/*  83 */       delObjFlag = true;
/*     */     }
/*  85 */     else if (((this instanceof PgenSelectingTool)) || 
/*  86 */       (!(this instanceof AbstractPgenDrawingTool))) {
/*  87 */       delObjFlag = false;
/*     */     }
/*     */ 
/*  93 */     if (this.inputHandler != null) {
/*  94 */       this.mapEditor.unregisterMouseHandler(this.inputHandler);
/*     */     }
/*     */ 
/*  97 */     this.inputHandler = getMouseHandler();
/*  98 */     if (this.inputHandler != null) this.mapEditor.registerMouseHandler(this.inputHandler);
/*     */ 
/* 101 */     setEnabled(false);
/*     */   }
/*     */ 
/*     */   public abstract IInputHandler getMouseHandler();
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/* 112 */     if ((this.buttonName != null) && (PgenSession.getInstance().getPgenPalette() != null)) {
/* 113 */       PgenSession.getInstance().getPgenPalette().resetIcon(this.buttonName);
/*     */     }
/* 115 */     if (this.drawingLayer != null)
/*     */     {
/* 117 */       this.drawingLayer.removeGhostLine();
/* 118 */       this.drawingLayer.removeSelected();
/* 119 */       PgenUtil.refresh();
/*     */     }
/*     */ 
/* 123 */     if ((this.mapEditor != null) && (this.inputHandler != null))
/* 124 */       this.mapEditor.unregisterMouseHandler(this.inputHandler);
/*     */   }
/*     */ 
/*     */   protected boolean isDelObj()
/*     */   {
/* 134 */     return delObjFlag;
/*     */   }
/*     */ 
/*     */   public PgenResource getDrawingLayer()
/*     */   {
/* 143 */     return this.drawingLayer;
/*     */   }
/*     */ 
/*     */   public void setDrawingLayer(PgenResource drawingLayer)
/*     */   {
/* 151 */     this.drawingLayer = drawingLayer;
/*     */   }
/*     */ 
/*     */   protected boolean isResourceEditable()
/*     */   {
/* 159 */     if (this.drawingLayer == null) return false;
/* 160 */     return this.drawingLayer.isEditable();
/*     */   }
/*     */ 
/*     */   public void setHandler(IInputHandler handler)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected IInputHandler getDefaultMouseHandler()
/*     */   {
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */   protected void resetMouseHandler()
/*     */   {
/* 182 */     IInputHandler dmh = getDefaultMouseHandler();
/* 183 */     if (dmh == null) {
/* 184 */       PgenUtil.setSelectingMode();
/*     */     }
/*     */     else
/* 187 */       setHandler(dmh);
/*     */   }
/*     */ 
/*     */   protected void setWorkingComponent(AbstractDrawableComponent adc)
/*     */   {
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.AbstractPgenTool
 * JD-Core Version:    0.6.2
 */