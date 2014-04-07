/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.JetAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class PgenJetDrawingTool extends PgenMultiPointDrawingTool
/*     */   implements IJetBarb
/*     */ {
/*     */   private Jet jet;
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/*  65 */     resetMouseHandler();
/*     */ 
/*  67 */     super.deactivateTool();
/*     */ 
/*  69 */     if ((this.mouseHandler instanceof PgenJetDrawingHandler)) {
/*  70 */       PgenJetDrawingHandler jdh = (PgenJetDrawingHandler)this.mouseHandler;
/*  71 */       if (jdh != null) jdh.clearPoints();
/*     */     }
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  82 */     if (this.mouseHandler == null)
/*     */     {
/*  84 */       this.mouseHandler = new PgenJetDrawingHandler(null);
/*     */     }
/*     */ 
/*  88 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public void setAddingBarbHandler()
/*     */   {
/*  94 */     setHandler(new PgenJetBarbAddingHandler(this.mapEditor, this.drawingLayer, 
/*  95 */       this, (JetAttrDlg)this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void setDeletingBarbHandler()
/*     */   {
/* 101 */     setHandler(new PgenJetBarbDeletingHandler(this.mapEditor, this.drawingLayer, 
/* 102 */       this, (JetAttrDlg)this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void setAddingHashHandler()
/*     */   {
/* 108 */     setHandler(new PgenJetHashAddingHandler(this.mapEditor, this.drawingLayer, 
/* 109 */       this, (JetAttrDlg)this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void setDeletingHashHandler()
/*     */   {
/* 115 */     setHandler(new PgenJetHashDeletingHandler(this.mapEditor, this.drawingLayer, 
/* 116 */       this, (JetAttrDlg)this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void resetMouseHandler()
/*     */   {
/* 121 */     setHandler(new PgenJetDrawingHandler(null));
/*     */   }
/*     */ 
/*     */   public void setSelected()
/*     */   {
/* 128 */     this.drawingLayer.setSelected(this.jet);
/* 129 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   public void setJet(Jet jet)
/*     */   {
/* 137 */     this.jet = jet;
/*     */   }
/*     */ 
/*     */   public Jet getJet()
/*     */   {
/* 144 */     return this.jet;
/*     */   }
/*     */ 
/*     */   public void deSelect()
/*     */   {
/* 151 */     this.drawingLayer.removeSelected();
/* 152 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   private class PgenJetDrawingHandler extends PgenMultiPointDrawingTool.PgenMultiPointDrawingHandler
/*     */   {
/*     */     private PgenJetDrawingHandler()
/*     */     {
/* 160 */       super();
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 170 */       if (!PgenJetDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 173 */       Coordinate loc = PgenJetDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 174 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 176 */       if (button == 1)
/*     */       {
/* 178 */         this.points.add(loc);
/* 179 */         if (this.points.size() == 1) {
/* 180 */           ((JetAttrDlg)PgenJetDrawingTool.this.attrDlg).enableBarbBtns(false);
/*     */         }
/*     */ 
/* 183 */         return true;
/*     */       }
/*     */ 
/* 186 */       if (button == 3)
/*     */       {
/* 188 */         if (this.points.size() == 0)
/*     */         {
/* 190 */           PgenJetDrawingTool.this.drawingLayer.removeGhostLine();
/* 191 */           PgenJetDrawingTool.this.mapEditor.refresh();
/* 192 */           PgenJetDrawingTool.this.attrDlg.close();
/* 193 */           PgenJetDrawingTool.this.attrDlg = null;
/* 194 */           PgenUtil.setSelectingMode();
/*     */         }
/* 197 */         else if (this.points.size() < 2)
/*     */         {
/* 199 */           PgenJetDrawingTool.this.drawingLayer.removeGhostLine();
/* 200 */           this.points.clear();
/*     */ 
/* 202 */           PgenJetDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */         else
/*     */         {
/* 208 */           this.elem = this.def.create(DrawableType.JET, PgenJetDrawingTool.this.attrDlg, 
/* 209 */             PgenJetDrawingTool.this.pgenCategory, PgenJetDrawingTool.this.pgenType, this.points, PgenJetDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 211 */           PgenJetDrawingTool.this.jet = ((Jet)this.elem);
/*     */ 
/* 213 */           PgenJetDrawingTool.this.jet.setSnapTool(new PgenSnapJet((IMapDescriptor)PgenJetDrawingTool.this.drawingLayer.getDescriptor(), PgenJetDrawingTool.this.mapEditor, (JetAttrDlg)PgenJetDrawingTool.this.attrDlg));
/*     */ 
/* 216 */           PgenJetDrawingTool.this.drawingLayer.addElement(PgenJetDrawingTool.this.jet);
/*     */ 
/* 219 */           AbstractDrawableComponent adc = (AbstractDrawableComponent)AttrSettings.getInstance().getSettings().get(PgenJetDrawingTool.this.pgenType);
/* 220 */           if ((adc != null) && ((adc instanceof Jet))) {
/* 221 */             ((Jet)adc).getJetLine().update(PgenJetDrawingTool.this.attrDlg);
/*     */           }
/*     */ 
/* 224 */           PgenJetDrawingTool.this.drawingLayer.removeGhostLine();
/* 225 */           this.points.clear();
/*     */ 
/* 227 */           PgenJetDrawingTool.this.mapEditor.refresh();
/*     */ 
/* 229 */           ((JetAttrDlg)PgenJetDrawingTool.this.attrDlg).setJetDrawingTool(PgenJetDrawingTool.this);
/* 230 */           ((JetAttrDlg)PgenJetDrawingTool.this.attrDlg).enableBarbBtns(true);
/*     */         }
/*     */ 
/* 234 */         return true;
/*     */       }
/*     */ 
/* 239 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 252 */       if (!PgenJetDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 255 */       Coordinate loc = PgenJetDrawingTool.this.mapEditor.translateClick(x, y);
/* 256 */       if (loc == null) return false;
/*     */ 
/* 259 */       AbstractDrawableComponent ghost = this.def.create(DrawableType.LINE, PgenJetDrawingTool.this.attrDlg, 
/* 260 */         "Lines", "FILLED_ARROW", this.points, PgenJetDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 262 */       if ((this.points != null) && (this.points.size() >= 1))
/*     */       {
/* 264 */         ArrayList ghostPts = new ArrayList(this.points);
/* 265 */         ghostPts.add(loc);
/* 266 */         ((Line)ghost).setLinePoints(new ArrayList(ghostPts));
/*     */ 
/* 272 */         PgenJetDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 273 */         PgenJetDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 277 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int aX, int aY, int button)
/*     */     {
/* 282 */       if ((!PgenJetDrawingTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 283 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenJetDrawingTool
 * JD-Core Version:    0.6.2
 */