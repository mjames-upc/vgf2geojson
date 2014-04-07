/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.IDisplayPane;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IMultiPoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaReducePoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.List;
/*     */ 
/*     */ public class PgenDeletePointHandler extends PgenSelectHandler
/*     */ {
/*  40 */   private OperationFilter delPointFilter = new OperationFilter(Operation.DELETE_POINT);
/*     */ 
/*     */   public PgenDeletePointHandler(AbstractPgenTool tool)
/*     */   {
/*  47 */     super(tool, tool.mapEditor, tool.getDrawingLayer(), null);
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/*  58 */     if (!this.tool.isResourceEditable()) return false;
/*     */ 
/*  61 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/*  62 */     if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/*  64 */     if (button == 1)
/*     */     {
/*  66 */       if (this.pgenrsc.getSelectedDE() == null)
/*     */       {
/*  69 */         DrawableElement elSelected = this.pgenrsc.getNearestElement(loc, this.delPointFilter);
/*  70 */         if (((elSelected instanceof MultiPointElement)) && 
/*  71 */           (!(elSelected instanceof WatchBox))) {
/*  72 */           this.pgenrsc.setSelected(elSelected);
/*     */         }
/*     */         else {
/*  75 */           return false;
/*     */         }
/*     */       }
/*  78 */       else if (!this.ptSelected)
/*     */       {
/*  81 */         this.ptIndex = getNearestPtIndex((MultiPointElement)this.pgenrsc.getSelectedDE(), loc);
/*  82 */         this.pgenrsc.addPtSelected(this.ptIndex);
/*  83 */         this.ptSelected = true;
/*     */       }
/*  89 */       else if ((this.pgenrsc.getSelectedDE() instanceof MultiPointElement)) {
/*  90 */         DrawableElement newEl = (DrawableElement)this.pgenrsc.getSelectedDE().copy();
/*  91 */         if (((IMultiPoint)newEl).getLinePoints().length <= 2) return true;
/*  92 */         newEl.getPoints().remove(this.ptIndex);
/*     */ 
/*  94 */         if ((newEl instanceof Gfa)) {
/*  95 */           ((Gfa)newEl).setGfaVorText(Gfa.buildVorText((Gfa)newEl));
/*  96 */           GfaReducePoint.WarningForOverThreeLines((Gfa)newEl);
/*     */         }
/*     */ 
/*  99 */         if ((newEl instanceof Jet.JetLine))
/*     */         {
/* 101 */           Jet jet = (Jet)this.pgenrsc.getActiveLayer().search(this.pgenrsc.getSelectedDE());
/* 102 */           Jet newJet = jet.copy();
/* 103 */           this.pgenrsc.replaceElement(jet, newJet);
/* 104 */           newJet.getPrimaryDE().setPoints(((MultiPointElement)newEl).getPoints());
/* 105 */           this.pgenrsc.setSelected(newJet.getPrimaryDE());
/*     */         }
/*     */         else
/*     */         {
/* 109 */           this.pgenrsc.replaceElement(this.pgenrsc.getSelectedDE(), newEl);
/*     */ 
/* 112 */           this.pgenrsc.setSelected(newEl);
/*     */         }
/*     */ 
/* 115 */         this.pgenrsc.removePtsSelected();
/* 116 */         this.ptSelected = false;
/*     */ 
/* 118 */         if (!(this.tool instanceof PgenDeletePoint)) {
/* 119 */           this.tool.resetMouseHandler();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 125 */       this.mapEditor.refresh();
/* 126 */       return true;
/*     */     }
/*     */ 
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int anX, int aY, int button)
/*     */   {
/* 143 */     if ((!this.tool.isResourceEditable()) || (this.shiftDown)) return false;
/* 144 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseUp(int x, int y, int button)
/*     */   {
/* 152 */     if (button == 3)
/*     */     {
/* 154 */       if ((this.pgenrsc.getSelectedDE() != null) && ((this.tool instanceof PgenDeletePoint))) {
/* 155 */         this.ptSelected = false;
/* 156 */         this.pgenrsc.removeSelected();
/* 157 */         this.mapEditor.refresh();
/*     */       }
/* 161 */       else if ((this.tool instanceof PgenDeletePoint)) {
/* 162 */         PgenUtil.setSelectingMode();
/*     */       }
/*     */       else {
/* 165 */         this.pgenrsc.removePtsSelected();
/* 166 */         this.mapEditor.refresh();
/* 167 */         this.tool.resetMouseHandler();
/*     */       }
/*     */ 
/* 171 */       return true;
/*     */     }
/*     */ 
/* 175 */     return false;
/*     */   }
/*     */ 
/*     */   public void cleanup()
/*     */   {
/* 183 */     this.ptSelected = false;
/* 184 */     this.pgenrsc.removeSelected();
/*     */   }
/*     */ 
/*     */   public void preprocess()
/*     */   {
/* 193 */     Coordinate lastClick = this.mapEditor.translateClick(this.mapEditor.getActiveDisplayPane().getLastMouseX(), 
/* 194 */       this.mapEditor.getActiveDisplayPane().getLastMouseY());
/*     */ 
/* 196 */     this.ptIndex = getNearestPtIndex((MultiPointElement)this.pgenrsc.getSelectedDE(), lastClick);
/* 197 */     this.pgenrsc.addPtSelected(this.ptIndex);
/* 198 */     this.ptSelected = true;
/*     */ 
/* 200 */     this.mapEditor.refresh();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenDeletePointHandler
 * JD-Core Version:    0.6.2
 */