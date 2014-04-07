/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.JetAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ISinglePoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.IJetTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetHash;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ 
/*     */ public class PgenJetHashDeletingHandler extends InputHandlerDefaultImpl
/*     */ {
/*     */   private AbstractEditor mapEditor;
/*     */   private PgenResource drawingLayer;
/*     */   private IJetBarb prevTool;
/*     */   private AbstractDrawableComponent hashSelected;
/*     */   private JetAttrDlg jetDlg;
/*     */ 
/*     */   public PgenJetHashDeletingHandler(AbstractEditor mapEditor, PgenResource drawingLayer, IJetBarb prevTool, JetAttrDlg jetDlg)
/*     */   {
/*  60 */     this.mapEditor = mapEditor;
/*  61 */     this.drawingLayer = drawingLayer;
/*  62 */     this.prevTool = prevTool;
/*  63 */     this.jetDlg = jetDlg;
/*     */ 
/*  65 */     drawingLayer.removeGhostLine();
/*  66 */     mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/*  77 */     if (!this.drawingLayer.isEditable()) return false;
/*     */ 
/*  79 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/*  80 */     if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/*  82 */     Jet jet = this.prevTool.getJet();
/*  83 */     if (jet == null) return false;
/*     */ 
/*  85 */     if (button == 1) {
/*  86 */       if (this.hashSelected != null) {
/*  87 */         Jet newJet = jet.copy();
/*     */ 
/*  89 */         newJet.remove(newJet.getNearestComponent(((ISinglePoint)this.hashSelected.getPrimaryDE()).getLocation()));
/*  90 */         IJetTools snapTool = newJet.getSnapTool();
/*  91 */         if (snapTool != null) {
/*  92 */           snapTool.snapJet(newJet);
/*     */         }
/*     */ 
/*  95 */         this.drawingLayer.replaceElement(jet, newJet);
/*  96 */         jet = newJet;
/*  97 */         this.prevTool.setJet(jet);
/*     */ 
/* 100 */         this.drawingLayer.removeSelected(this.hashSelected);
/* 101 */         this.drawingLayer.setGhostLine(null);
/*     */ 
/* 103 */         this.hashSelected = null;
/* 104 */         this.jetDlg.updateSegmentPane();
/*     */       }
/*     */       else
/*     */       {
/* 109 */         this.hashSelected = jet.getNearestDE(loc);
/* 110 */         if (!(this.hashSelected instanceof Jet.JetHash)) {
/* 111 */           this.hashSelected = null;
/*     */         }
/*     */         else {
/* 114 */           this.drawingLayer.addSelected(this.hashSelected);
/* 115 */           Symbol selectSymbol = new Symbol(null, new Color[] { Color.red }, 
/* 116 */             2.5F, 7.5D, Boolean.valueOf(false), ((Jet.JetHash)this.hashSelected).getLocation(), 
/* 117 */             "Marker", "DOT");
/* 118 */           this.drawingLayer.setGhostLine(selectSymbol);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 123 */       this.mapEditor.refresh();
/* 124 */       return true;
/*     */     }
/*     */ 
/* 127 */     if (button == 3)
/*     */     {
/* 129 */       this.drawingLayer.removeGhostLine();
/* 130 */       this.mapEditor.refresh();
/*     */ 
/* 132 */       this.prevTool.resetMouseHandler();
/* 133 */       return true;
/*     */     }
/*     */ 
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */   {
/* 146 */     if ((!this.drawingLayer.isEditable()) || (this.shiftDown)) return false;
/* 147 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenJetHashDeletingHandler
 * JD-Core Version:    0.6.2
 */