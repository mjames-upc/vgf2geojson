/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ 
/*     */ public class PgenAvnTextDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  58 */     if (this.mouseHandler == null)
/*     */     {
/*  60 */       this.mouseHandler = new PgenAvnTextDrawingHandler();
/*     */     }
/*     */ 
/*  64 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   private DrawableType getDrawableType()
/*     */   {
/* 180 */     if (this.pgenType.equals("AVIATION_TEXT")) {
/* 181 */       return DrawableType.AVN_TEXT;
/*     */     }
/* 183 */     return DrawableType.MID_CLOUD_TEXT;
/*     */   }
/*     */ 
/*     */   public class PgenAvnTextDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/*  79 */     private DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/*  84 */     private AbstractDrawableComponent elem = null;
/*     */ 
/*     */     public PgenAvnTextDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/*  94 */       if (!PgenAvnTextDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/*  97 */       Coordinate loc = PgenAvnTextDrawingTool.this.mapEditor.translateClick(anX, aY);
/*  98 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 100 */       if (button == 1)
/*     */       {
/* 103 */         this.elem = this.def.create(PgenAvnTextDrawingTool.this.getDrawableType(), PgenAvnTextDrawingTool.this.attrDlg, 
/* 104 */           PgenAvnTextDrawingTool.this.pgenCategory, PgenAvnTextDrawingTool.this.pgenType, loc, PgenAvnTextDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 107 */         if (this.elem != null) {
/* 108 */           PgenAvnTextDrawingTool.this.drawingLayer.addElement(this.elem);
/* 109 */           PgenAvnTextDrawingTool.this.mapEditor.refresh();
/* 110 */           AttrSettings.getInstance().setSettings((DrawableElement)this.elem);
/*     */         }
/*     */ 
/* 113 */         return true;
/*     */       }
/*     */ 
/* 116 */       if (button == 3)
/*     */       {
/* 123 */         PgenUtil.setSelectingMode();
/*     */ 
/* 125 */         return true;
/*     */       }
/*     */ 
/* 130 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 144 */       if (!PgenAvnTextDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 147 */       Coordinate loc = PgenAvnTextDrawingTool.this.mapEditor.translateClick(x, y);
/* 148 */       if (loc == null) return false;
/*     */ 
/* 150 */       if (PgenAvnTextDrawingTool.this.attrDlg != null)
/*     */       {
/* 152 */         AbstractDrawableComponent ghost = null;
/*     */ 
/* 154 */         ghost = this.def.create(PgenAvnTextDrawingTool.this.getDrawableType(), PgenAvnTextDrawingTool.this.attrDlg, 
/* 155 */           PgenAvnTextDrawingTool.this.pgenCategory, PgenAvnTextDrawingTool.this.pgenType, loc, PgenAvnTextDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 157 */         PgenAvnTextDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 158 */         PgenAvnTextDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 162 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 168 */       if ((this.shiftDown) || (!PgenAvnTextDrawingTool.this.isResourceEditable())) return false;
/* 169 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenAvnTextDrawingTool
 * JD-Core Version:    0.6.2
 */