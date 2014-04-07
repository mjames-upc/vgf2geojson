/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaReducePoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo;
/*     */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class PgenMoveElement extends PgenCopyElement
/*     */ {
/*  48 */   protected IInputHandler moveHandler = null;
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  56 */     if (this.moveHandler == null)
/*     */     {
/*  58 */       this.moveHandler = new PgenMoveHandler();
/*     */     }
/*     */ 
/*  62 */     return this.moveHandler;
/*     */   }
/*     */   public class PgenMoveHandler extends PgenCopyElement.PgenCopyHandler {
/*     */     public PgenMoveHandler() {
/*  66 */       super();
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int x, int y, int button)
/*     */     {
/*  77 */       if ((!PgenMoveElement.this.isResourceEditable()) || (this.shiftDown) || (this.simulate)) return false;
/*     */ 
/*  79 */       if (this.ghostEl != null)
/*     */       {
/*  81 */         AbstractDrawableComponent comp = PgenMoveElement.this.drawingLayer.getSelectedComp();
/*     */ 
/*  83 */         Iterator it1 = comp.createDEIterator();
/*  84 */         Iterator it2 = this.ghostEl.createDEIterator();
/*     */ 
/*  86 */         while ((it1.hasNext()) && (it2.hasNext())) {
/*  87 */           ((DrawableElement)it2.next()).setColors(((DrawableElement)it1.next()).getColors());
/*     */         }
/*     */ 
/*  90 */         if ((this.ghostEl instanceof WatchBox)) {
/*  91 */           if ((this.ghostEl instanceof WatchBox)) {
/*  93 */             if (!PgenWatchBoxModifyTool.resnapWatchBox(PgenMoveElement.this.mapEditor, 
/*  93 */               (WatchBox)this.ghostEl, (WatchBox)this.ghostEl));
/*     */           }
/*  95 */         } else if (SigmetInfo.isSnapADC(this.ghostEl)) {
/*  96 */           ArrayList list = SnapUtil.getSnapWithStation(
/*  97 */             this.ghostEl.getPoints(), 
/*  98 */             SnapUtil.VOR_STATION_LIST, 
/*  99 */             10, 
/* 100 */             SigmetInfo.getNumOfCompassPts(this.ghostEl));
/* 101 */           AbstractDrawableComponent ghostElCp = this.ghostEl.copy();
/* 102 */           ((DrawableElement)ghostElCp).setPoints(list);
/*     */ 
/* 104 */           PgenMoveElement.this.drawingLayer.replaceElement(comp, ghostElCp);
/* 105 */           PgenMoveElement.this.drawingLayer.setSelected(ghostElCp);
/*     */         }
/* 107 */         else if ((this.ghostEl instanceof Gfa))
/*     */         {
/* 109 */           if (((Gfa)this.ghostEl).getGfaFcstHr().indexOf("-") > -1)
/*     */           {
/* 111 */             ((Gfa)this.ghostEl).snap();
/* 112 */             GfaReducePoint.WarningForOverThreeLines((Gfa)this.ghostEl);
/*     */           }
/*     */ 
/* 115 */           ((Gfa)this.ghostEl).setGfaVorText(Gfa.buildVorText((Gfa)this.ghostEl));
/*     */ 
/* 117 */           PgenMoveElement.this.drawingLayer.replaceElement(comp, this.ghostEl);
/* 118 */           PgenMoveElement.this.drawingLayer.setSelected(this.ghostEl);
/*     */         }
/*     */         else
/*     */         {
/* 123 */           PgenMoveElement.this.drawingLayer.replaceElement(comp, this.ghostEl);
/* 124 */           PgenMoveElement.this.drawingLayer.setSelected(this.ghostEl);
/*     */         }
/*     */ 
/* 129 */         PgenMoveElement.this.drawingLayer.removeGhostLine();
/* 130 */         this.ghostEl = null;
/* 131 */         PgenMoveElement.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 135 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenMoveElement
 * JD-Core Version:    0.6.2
 */