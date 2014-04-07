/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.ArcAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class PgenDualPointDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  56 */     if (this.mouseHandler == null)
/*     */     {
/*  58 */       this.mouseHandler = new PgenDualPointDrawingHandler();
/*     */     }
/*     */ 
/*  62 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public class PgenDualPointDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/*  77 */     private ArrayList<Coordinate> points = new ArrayList();
/*     */     private AbstractDrawableComponent elem;
/*  88 */     private DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/*     */     public PgenDualPointDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/*  98 */       if (!PgenDualPointDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 101 */       Coordinate loc = PgenDualPointDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 102 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 104 */       if (button == 1)
/*     */       {
/* 106 */         if (this.points.size() == 0)
/*     */         {
/* 108 */           this.points.add(0, loc);
/*     */         }
/*     */         else
/*     */         {
/* 113 */           if (!validArcAngle()) this.points.clear();
/*     */ 
/* 115 */           if (this.points.size() > 1) this.points.remove(1);
/* 116 */           this.points.add(1, loc);
/*     */ 
/* 119 */           this.elem = this.def.create(DrawableType.ARC, PgenDualPointDrawingTool.this.attrDlg, 
/* 120 */             PgenDualPointDrawingTool.this.pgenCategory, PgenDualPointDrawingTool.this.pgenType, this.points, PgenDualPointDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 123 */           PgenDualPointDrawingTool.this.drawingLayer.addElement(this.elem);
/*     */ 
/* 125 */           PgenDualPointDrawingTool.this.drawingLayer.removeGhostLine();
/* 126 */           this.points.clear();
/*     */ 
/* 128 */           PgenDualPointDrawingTool.this.mapEditor.refresh();
/* 129 */           AttrSettings.getInstance().setSettings((DrawableElement)this.elem);
/*     */         }
/*     */ 
/* 133 */         return true;
/*     */       }
/*     */ 
/* 136 */       if (button == 3)
/*     */       {
/* 138 */         PgenDualPointDrawingTool.this.drawingLayer.removeGhostLine();
/* 139 */         PgenDualPointDrawingTool.this.mapEditor.refresh();
/*     */ 
/* 141 */         if (this.points.size() == 0)
/*     */         {
/* 144 */           PgenUtil.setSelectingMode();
/*     */         }
/*     */         else
/*     */         {
/* 149 */           this.points.clear();
/*     */         }
/*     */ 
/* 153 */         return true;
/*     */       }
/*     */ 
/* 156 */       if (button == 2)
/*     */       {
/* 158 */         return true;
/*     */       }
/*     */ 
/* 163 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 177 */       if (!PgenDualPointDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 180 */       Coordinate loc = PgenDualPointDrawingTool.this.mapEditor.translateClick(x, y);
/* 181 */       if (loc == null) return false;
/*     */ 
/* 184 */       AbstractDrawableComponent ghost = null;
/*     */ 
/* 186 */       if (!validArcAngle()) this.points.clear();
/*     */ 
/* 188 */       if ((this.points != null) && (this.points.size() >= 1))
/*     */       {
/* 190 */         ghost = this.def.create(DrawableType.ARC, PgenDualPointDrawingTool.this.attrDlg, 
/* 191 */           PgenDualPointDrawingTool.this.pgenCategory, PgenDualPointDrawingTool.this.pgenType, this.points, 
/* 192 */           PgenDualPointDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 194 */         ArrayList ghostPts = new ArrayList();
/*     */ 
/* 196 */         ghostPts.add(0, (Coordinate)this.points.get(0));
/* 197 */         ghostPts.add(1, loc);
/*     */ 
/* 199 */         Arc arc = (Arc)ghost;
/* 200 */         arc.setLinePoints(new ArrayList(ghostPts));
/*     */ 
/* 202 */         PgenDualPointDrawingTool.this.drawingLayer.setGhostLine(ghost);
/*     */ 
/* 204 */         PgenDualPointDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 209 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 215 */       if ((!PgenDualPointDrawingTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 216 */       return true;
/*     */     }
/*     */ 
/*     */     private boolean validArcAngle()
/*     */     {
/* 225 */       boolean isValid = false;
/*     */ 
/* 227 */       if (PgenDualPointDrawingTool.this.attrDlg != null)
/*     */       {
/* 229 */         double sa = ((ArcAttrDlg)PgenDualPointDrawingTool.this.attrDlg).getStartAngle();
/* 230 */         double ea = ((ArcAttrDlg)PgenDualPointDrawingTool.this.attrDlg).getEndAngle();
/*     */ 
/* 232 */         if (sa < ea) isValid = true;
/*     */       }
/*     */ 
/* 235 */       return isValid;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenDualPointDrawingTool
 * JD-Core Version:    0.6.2
 */