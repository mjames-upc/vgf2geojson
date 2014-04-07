/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.VectorAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Vector;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class PgenVectorDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   protected void activateTool()
/*     */   {
/*  58 */     super.activateTool();
/*     */ 
/*  60 */     AbstractDrawableComponent attr = (AbstractDrawableComponent)AttrSettings.getInstance().getSettings().get(this.pgenType);
/*  61 */     if (attr == null)
/*  62 */       ((VectorAttrDlg)this.attrDlg).adjustAttrForDlg(this.pgenType);
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  73 */     if (this.mouseHandler == null)
/*     */     {
/*  75 */       this.mouseHandler = new PgenVectorDrawingHandler();
/*     */     }
/*     */ 
/*  79 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public class PgenVectorDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/*  93 */     private ArrayList<Coordinate> points = new ArrayList();
/*     */ 
/*  99 */     private DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/* 104 */     private AbstractDrawableComponent elem = null;
/*     */ 
/*     */     public PgenVectorDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 114 */       if (!PgenVectorDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 117 */       Coordinate loc = PgenVectorDrawingTool.this.mapEditor.translateClick(anX, aY);
/*     */ 
/* 119 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 121 */       if (button == 1)
/*     */       {
/* 123 */         if (this.points.size() == 0) {
/* 124 */           this.points.add(0, loc);
/*     */ 
/* 126 */           this.elem = this.def.create(DrawableType.VECTOR, PgenVectorDrawingTool.this.attrDlg, 
/* 127 */             PgenVectorDrawingTool.this.pgenCategory, PgenVectorDrawingTool.this.pgenType, (Coordinate)this.points.get(0), 
/* 128 */             PgenVectorDrawingTool.this.drawingLayer.getActiveLayer());
/*     */         }
/*     */         else
/*     */         {
/* 132 */           PgenVectorDrawingTool.this.drawingLayer.removeElement(this.elem);
/*     */ 
/* 134 */           this.elem = this.def.create(DrawableType.VECTOR, PgenVectorDrawingTool.this.attrDlg, 
/* 135 */             PgenVectorDrawingTool.this.pgenCategory, PgenVectorDrawingTool.this.pgenType, (Coordinate)this.points.get(0), 
/* 136 */             PgenVectorDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 138 */           double dir = ((Vector)this.elem).vectorDirection((Coordinate)this.points.get(0), loc);
/* 139 */           ((Vector)this.elem).setDirection(dir);
/* 140 */           ((VectorAttrDlg)PgenVectorDrawingTool.this.attrDlg).setDirection(((Vector)this.elem).getDirection());
/*     */         }
/*     */ 
/* 145 */         if (this.elem != null) {
/* 146 */           PgenVectorDrawingTool.this.drawingLayer.addElement(this.elem);
/* 147 */           PgenVectorDrawingTool.this.mapEditor.refresh();
/* 148 */           AttrSettings.getInstance().setSettings((DrawableElement)this.elem);
/*     */         }
/*     */ 
/* 152 */         return true;
/*     */       }
/*     */ 
/* 155 */       if (button == 3)
/*     */       {
/* 157 */         PgenVectorDrawingTool.this.drawingLayer.removeGhostLine();
/* 158 */         PgenVectorDrawingTool.this.mapEditor.refresh();
/*     */ 
/* 160 */         if (this.points.size() > 0)
/*     */         {
/* 162 */           this.points.clear();
/*     */         }
/*     */         else
/*     */         {
/* 167 */           PgenVectorDrawingTool.this.attrDlg.close();
/*     */ 
/* 169 */           PgenUtil.setSelectingMode();
/*     */         }
/* 171 */         return true;
/*     */       }
/*     */ 
/* 176 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 190 */       if (!PgenVectorDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 193 */       Coordinate loc = PgenVectorDrawingTool.this.mapEditor.translateClick(x, y);
/* 194 */       if (loc == null) return false;
/*     */ 
/* 196 */       if ((PgenVectorDrawingTool.this.attrDlg != null) && (this.points.size() != 0))
/*     */       {
/* 198 */         AbstractDrawableComponent ghost = null;
/*     */ 
/* 200 */         ghost = this.def.create(DrawableType.VECTOR, PgenVectorDrawingTool.this.attrDlg, 
/* 201 */           PgenVectorDrawingTool.this.pgenCategory, PgenVectorDrawingTool.this.pgenType, (Coordinate)this.points.get(0), 
/* 202 */           PgenVectorDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 204 */         double dir = ((Vector)ghost).vectorDirection((Coordinate)this.points.get(0), loc);
/*     */ 
/* 206 */         ((Vector)ghost).setDirection(dir);
/* 207 */         ((VectorAttrDlg)PgenVectorDrawingTool.this.attrDlg).setDirection(dir);
/*     */ 
/* 209 */         PgenVectorDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 210 */         PgenVectorDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 213 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 219 */       if ((!PgenVectorDrawingTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 220 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenVectorDrawingTool
 * JD-Core Version:    0.6.2
 */