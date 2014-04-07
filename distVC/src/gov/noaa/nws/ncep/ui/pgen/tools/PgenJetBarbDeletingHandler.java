/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.JetAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ISinglePoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.IJetTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ 
/*     */ public class PgenJetBarbDeletingHandler extends InputHandlerDefaultImpl
/*     */ {
/*     */   private AbstractEditor mapEditor;
/*     */   private PgenResource drawingLayer;
/*     */   private IJetBarb prevTool;
/*     */   private JetAttrDlg jetDlg;
/*     */   private AbstractDrawableComponent barbSelected;
/*     */ 
/*     */   public PgenJetBarbDeletingHandler(AbstractEditor mapEditor, PgenResource drawingLayer, IJetBarb prevTool, JetAttrDlg jetDlg)
/*     */   {
/*  64 */     this.mapEditor = mapEditor;
/*  65 */     this.drawingLayer = drawingLayer;
/*  66 */     this.prevTool = prevTool;
/*  67 */     this.jetDlg = jetDlg;
/*     */ 
/*  69 */     drawingLayer.removeGhostLine();
/*  70 */     mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/*  81 */     if (!this.drawingLayer.isEditable()) return false;
/*     */ 
/*  84 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/*  85 */     if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/*  87 */     Jet jet = this.prevTool.getJet();
/*  88 */     if (jet == null) return false;
/*     */ 
/*  90 */     if (button == 1) {
/*  91 */       if (this.barbSelected != null) {
/*  92 */         Jet newJet = jet.copy();
/*     */ 
/*  94 */         newJet.remove(newJet.getNearestComponent(((ISinglePoint)this.barbSelected.getPrimaryDE()).getLocation()));
/*  95 */         IJetTools snapTool = newJet.getSnapTool();
/*  96 */         if (snapTool != null) {
/*  97 */           snapTool.snapJet(newJet);
/*     */         }
/*     */ 
/* 100 */         this.drawingLayer.replaceElement(jet, newJet);
/* 101 */         jet = newJet;
/* 102 */         this.prevTool.setJet(jet);
/*     */ 
/* 105 */         this.drawingLayer.removeSelected(this.barbSelected);
/* 106 */         this.drawingLayer.setGhostLine(null);
/*     */ 
/* 108 */         this.barbSelected = null;
/* 109 */         this.jetDlg.updateSegmentPane();
/*     */       }
/*     */       else
/*     */       {
/* 114 */         this.barbSelected = jet.getNearestComponent(loc);
/* 115 */         if (!this.barbSelected.getName().equalsIgnoreCase("WindInfo")) {
/* 116 */           this.barbSelected = null;
/*     */         }
/*     */         else {
/* 119 */           this.drawingLayer.addSelected(this.barbSelected);
/*     */ 
/* 122 */           DECollection dec = new DECollection();
/* 123 */           for (Coordinate pt : this.barbSelected.getPoints()) {
/* 124 */             Symbol redDot = new Symbol(null, new Color[] { Color.red }, 
/* 125 */               2.5F, 7.5D, Boolean.valueOf(false), pt, 
/* 126 */               "Marker", "DOT");
/* 127 */             dec.add(redDot);
/*     */           }
/* 129 */           this.drawingLayer.setGhostLine(dec);
/*     */         }
/*     */       }
/*     */ 
/* 133 */       this.mapEditor.refresh();
/* 134 */       return true;
/*     */     }
/*     */ 
/* 137 */     if (button == 3)
/*     */     {
/* 139 */       this.drawingLayer.removeGhostLine();
/* 140 */       this.mapEditor.refresh();
/*     */ 
/* 142 */       this.prevTool.resetMouseHandler();
/* 143 */       return true;
/*     */     }
/*     */ 
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */   {
/* 156 */     if ((!this.drawingLayer.isEditable()) || (this.shiftDown)) return false;
/* 157 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenJetBarbDeletingHandler
 * JD-Core Version:    0.6.2
 */