/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaReducePoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class PgenModifyTool extends AbstractPgenTool
/*     */ {
/*  58 */   protected IInputHandler modifyHandler = null;
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  73 */     if (this.modifyHandler == null)
/*     */     {
/*  75 */       this.modifyHandler = new PgenModifyHandler();
/*     */     }
/*     */ 
/*  79 */     return this.modifyHandler;
/*     */   }
/*     */ 
/*     */   private boolean isModifiableSigmet(DrawableElement el)
/*     */   {
/* 404 */     if ((el instanceof Sigmet)) {
/* 405 */       Sigmet sig = (Sigmet)el;
/*     */ 
/* 407 */       if ((!sig.getType().contains("Text")) && (!sig.getType().contains("Isolated"))) {
/* 408 */         return true;
/*     */       }
/*     */     }
/* 411 */     return false;
/*     */   }
/*     */ 
/*     */   public class PgenModifyHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     private boolean preempt;
/*  91 */     OperationFilter modifyFilter = new OperationFilter(Operation.MODIFY);
/*     */ 
/*  96 */     ArrayList<Coordinate> clickPts = null;
/*     */ 
/* 101 */     MultiPointElement ghostEl = null;
/*     */ 
/* 106 */     PgenModifyLine pml = null;
/*     */ 
/* 111 */     Color ghostColor = new Color(255, 255, 255);
/*     */ 
/*     */     public PgenModifyHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 121 */       if (!PgenModifyTool.this.isResourceEditable()) return false;
/*     */ 
/* 123 */       this.preempt = false;
/*     */ 
/* 125 */       Coordinate loc = PgenModifyTool.this.mapEditor.translateClick(anX, aY);
/* 126 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 128 */       if (button == 1)
/*     */       {
/* 130 */         if (PgenModifyTool.this.drawingLayer.getSelectedDE() == null)
/*     */         {
/* 133 */           DrawableElement elSelected = PgenModifyTool.this.drawingLayer.getNearestElement(loc, this.modifyFilter);
/* 134 */           if ((((elSelected instanceof Line)) && (!(elSelected instanceof Arc))) || (PgenModifyTool.this.isModifiableSigmet(elSelected))) {
/* 135 */             PgenModifyTool.this.drawingLayer.setSelected(elSelected);
/* 136 */             PgenModifyTool.this.mapEditor.refresh();
/* 137 */             this.preempt = true;
/*     */           }
/*     */           else {
/* 140 */             return false;
/*     */           }
/*     */         }
/*     */         else {
/* 144 */           this.preempt = true;
/*     */ 
/* 146 */           if (this.clickPts == null) {
/* 147 */             this.clickPts = new ArrayList();
/*     */           }
/*     */ 
/* 150 */           this.clickPts.add(loc);
/*     */ 
/* 152 */           if (this.pml == null) {
/* 153 */             this.pml = new PgenModifyLine();
/*     */           }
/*     */ 
/* 156 */           this.pml.setClickPts(latlonToPixel((Coordinate[])this.clickPts.toArray(new Coordinate[this.clickPts.size()])));
/*     */ 
/* 158 */           ModifyLine();
/*     */ 
/* 160 */           this.ghostEl.setColors(new Color[] { this.ghostColor, new Color(255, 255, 255) });
/*     */ 
/* 162 */           PgenModifyTool.this.drawingLayer.setGhostLine(this.ghostEl);
/* 163 */           PgenModifyTool.this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 167 */         return this.preempt;
/*     */       }
/*     */ 
/* 170 */       if (button == 3)
/*     */       {
/* 172 */         if (PgenModifyTool.this.drawingLayer.getSelectedDE() != null)
/*     */         {
/* 174 */           if ((this.clickPts != null) && (!this.clickPts.isEmpty()))
/*     */           {
/* 176 */             this.pml.setClickPts(latlonToPixel((Coordinate[])this.clickPts.toArray(new Coordinate[this.clickPts.size()])));
/*     */ 
/* 178 */             ModifyLine();
/*     */ 
/* 180 */             if ((!((Line)PgenModifyTool.this.drawingLayer.getSelectedDE()).isClosedLine().booleanValue()) || 
/* 181 */               (this.ghostEl.getLinePoints().length >= 3))
/*     */             {
/* 183 */               MultiPointElement selected = (MultiPointElement)PgenModifyTool.this.drawingLayer.getSelectedDE();
/*     */ 
/* 185 */               if ((selected instanceof Jet.JetLine))
/*     */               {
/* 187 */                 Jet jet = (Jet)PgenModifyTool.this.drawingLayer.getActiveLayer().search(selected);
/* 188 */                 Jet newJet = jet.copy();
/* 189 */                 PgenModifyTool.this.drawingLayer.replaceElement(jet, newJet);
/* 190 */                 newJet.getPrimaryDE().setPoints(this.ghostEl.getPoints());
/* 191 */                 PgenModifyTool.this.drawingLayer.setSelected(newJet.getPrimaryDE());
/*     */               }
/*     */               else {
/* 194 */                 MultiPointElement mpe = (MultiPointElement)PgenModifyTool.this.drawingLayer.getSelectedDE().copy();
/*     */ 
/* 196 */                 PgenModifyTool.this.drawingLayer.replaceElement(PgenModifyTool.this.drawingLayer.getSelectedDE(), mpe);
/*     */ 
/* 209 */                 mpe.setPoints(this.ghostEl.getPoints());
/* 210 */                 if ((mpe instanceof Gfa)) {
/* 211 */                   if (((Gfa)mpe).getGfaFcstHr().indexOf("-") > -1)
/*     */                   {
/* 213 */                     ((Gfa)mpe).snap();
/*     */ 
/* 215 */                     GfaReducePoint.WarningForOverThreeLines((Gfa)mpe);
/*     */                   }
/*     */ 
/* 219 */                   ((Gfa)mpe).setGfaVorText(Gfa.buildVorText((Gfa)mpe));
/*     */                 }
/*     */ 
/* 222 */                 PgenModifyTool.this.drawingLayer.setSelected(mpe);
/*     */               }
/*     */             }
/*     */ 
/* 226 */             PgenModifyTool.this.drawingLayer.removeGhostLine();
/* 227 */             this.clickPts.clear();
/*     */ 
/* 229 */             PgenModifyTool.this.mapEditor.refresh();
/*     */           }
/*     */           else
/*     */           {
/* 234 */             this.ghostEl = null;
/*     */ 
/* 236 */             PgenModifyTool.this.drawingLayer.removeGhostLine();
/* 237 */             PgenModifyTool.this.drawingLayer.removeSelected();
/* 238 */             PgenModifyTool.this.mapEditor.refresh();
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 245 */           PgenModifyTool.this.drawingLayer.removeSelected();
/* 246 */           PgenModifyTool.this.mapEditor.refresh();
/*     */ 
/* 249 */           PgenUtil.setSelectingMode();
/*     */         }
/*     */ 
/* 253 */         return true;
/*     */       }
/*     */ 
/* 257 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 270 */       if (!PgenModifyTool.this.isResourceEditable()) return false;
/*     */ 
/* 273 */       Coordinate loc = PgenModifyTool.this.mapEditor.translateClick(x, y);
/* 274 */       if (loc == null) return false;
/*     */ 
/* 277 */       if ((this.clickPts != null) && (this.clickPts.size() >= 1))
/*     */       {
/* 279 */         ArrayList newPts = new ArrayList(this.clickPts);
/* 280 */         newPts.add(loc);
/*     */ 
/* 282 */         this.pml.setClickPts(latlonToPixel((Coordinate[])newPts.toArray(new Coordinate[newPts.size()])));
/*     */ 
/* 284 */         ModifyLine();
/*     */ 
/* 286 */         this.ghostEl.setColors(new Color[] { this.ghostColor, new Color(255, 255, 255) });
/* 287 */         PgenModifyTool.this.drawingLayer.setGhostLine(this.ghostEl);
/* 288 */         PgenModifyTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 292 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int x, int y, int button)
/*     */     {
/* 305 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 311 */       if ((!PgenModifyTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 312 */       return this.preempt;
/*     */     }
/*     */ 
/*     */     private void ModifyLine()
/*     */     {
/* 321 */       this.pml.setOriginalPts(latlonToPixel(((Line)PgenModifyTool.this.drawingLayer.getSelectedDE()).getLinePoints()));
/*     */ 
/* 323 */       this.pml.setSmoothLevel(((Line)PgenModifyTool.this.drawingLayer.getSelectedDE()).getSmoothFactor());
/*     */ 
/* 325 */       this.pml.setClosed(((Line)PgenModifyTool.this.drawingLayer.getSelectedDE()).isClosedLine().booleanValue());
/*     */ 
/* 327 */       this.pml.PerformModify();
/*     */ 
/* 329 */       buildNewElement();
/*     */     }
/*     */ 
/*     */     private double[][] latlonToPixel(Coordinate[] pts)
/*     */     {
/* 340 */       double[] point = new double[2];
/* 341 */       double[][] pixels = new double[pts.length][2];
/*     */ 
/* 343 */       int ii = 0;
/* 344 */       for (Coordinate crd : pts)
/*     */       {
/* 346 */         point = PgenModifyTool.this.mapEditor.translateInverseClick(crd);
/* 347 */         pixels[ii][0] = point[0];
/* 348 */         pixels[ii][1] = point[1];
/*     */ 
/* 350 */         ii++;
/*     */       }
/*     */ 
/* 353 */       return pixels;
/*     */     }
/*     */ 
/*     */     private ArrayList<Coordinate> pixelToLatlon(double[][] pixels)
/*     */     {
/* 364 */       ArrayList crd = new ArrayList();
/*     */ 
/* 366 */       for (int ii = 0; ii < pixels.length; ii++) {
/* 367 */         crd.add(PgenModifyTool.this.mapEditor.translateClick(pixels[ii][0], pixels[ii][1]));
/*     */       }
/*     */ 
/* 370 */       return crd;
/*     */     }
/*     */ 
/*     */     private void buildNewElement()
/*     */     {
/* 381 */       this.ghostEl = ((MultiPointElement)PgenModifyTool.this.drawingLayer.getSelectedDE().copy());
/*     */ 
/* 383 */       if ((this.ghostEl != null) && (this.pml.getModifiedPts() != null) && (this.pml.getModifiedPts().length > 1))
/*     */       {
/* 385 */         this.ghostEl.setLinePoints(pixelToLatlon(this.pml.getModifiedPts()));
/*     */ 
/* 387 */         if ((((Line)PgenModifyTool.this.drawingLayer.getSelectedDE()).isClosedLine().booleanValue()) && 
/* 388 */           (this.pml.getModifiedPts().length < 3))
/* 389 */           this.ghostEl.setClosed(Boolean.valueOf(false));
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenModifyTool
 * JD-Core Version:    0.6.2
 */