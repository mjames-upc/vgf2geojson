/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.JetAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector.VectorType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetHash;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ 
/*     */ public class PgenJetHashAddingHandler extends InputHandlerDefaultImpl
/*     */ {
/*     */   private AbstractEditor mapEditor;
/*     */   private PgenResource drawingLayer;
/*     */   private IJetBarb prevTool;
/*     */   private JetAttrDlg jetDlg;
/*     */ 
/*     */   public PgenJetHashAddingHandler(AbstractEditor mapEditor, PgenResource drawingLayer, IJetBarb prevTool, JetAttrDlg jetDlg)
/*     */   {
/*  56 */     this.mapEditor = mapEditor;
/*  57 */     this.drawingLayer = drawingLayer;
/*  58 */     this.prevTool = prevTool;
/*  59 */     this.jetDlg = jetDlg;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/*  70 */     if (!this.drawingLayer.isEditable()) return false;
/*     */ 
/*  73 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/*  74 */     if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/*  76 */     Jet jet = this.prevTool.getJet();
/*  77 */     if (jet == null) return false;
/*     */ 
/*  79 */     if (button == 1)
/*     */     {
/*  81 */       Jet newJet = jet.copy();
/*     */ 
/*  83 */       newJet.addHash(createHash(newJet, loc, true));
/*     */ 
/*  85 */       this.drawingLayer.replaceElement(jet, newJet);
/*  86 */       jet = newJet;
/*     */ 
/*  88 */       this.prevTool.setJet(jet);
/*     */ 
/*  90 */       this.mapEditor.refresh();
/*  91 */       this.jetDlg.updateSegmentPane();
/*     */ 
/*  93 */       return true;
/*     */     }
/*     */ 
/*  96 */     if (button == 3)
/*     */     {
/*  98 */       this.drawingLayer.removeGhostLine();
/*  99 */       this.mapEditor.refresh();
/*     */ 
/* 101 */       this.prevTool.resetMouseHandler();
/* 102 */       return true;
/*     */     }
/*     */ 
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseMove(int x, int y)
/*     */   {
/* 122 */     if (!this.drawingLayer.isEditable()) return false;
/*     */ 
/* 125 */     Coordinate loc = this.mapEditor.translateClick(x, y);
/* 126 */     if (loc == null) return false;
/*     */ 
/* 128 */     Jet jet = this.prevTool.getJet();
/* 129 */     if (jet == null) return false;
/*     */ 
/* 131 */     this.drawingLayer.setGhostLine(createHash(jet, loc, false));
/* 132 */     this.mapEditor.refresh();
/*     */ 
/* 134 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */   {
/* 140 */     if ((!this.drawingLayer.isEditable()) || (this.shiftDown)) return false;
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   private Jet.JetHash createHash(Jet aJet, Coordinate loc, boolean updateTemplate)
/*     */   {
/*     */     Jet tmp5_4 = aJet; tmp5_4.getClass(); Jet.JetHash hash = new Jet.JetHash(tmp5_4, null, new Color[] { new Color(0, 255, 0), new Color(255, 0, 0) }, 
/* 146 */       2.0F, 2.0D, Boolean.valueOf(true), loc, IVector.VectorType.HASH_MARK, 
/* 147 */       100.0D, 0.0D, 1.0D, false, "Vector", "Hash");
/*     */ 
/* 149 */     IAttribute hashAttr = this.jetDlg.getHashAttr();
/*     */ 
/* 151 */     if (hashAttr != null) {
/* 152 */       hash.update(hashAttr);
/*     */     }
/*     */ 
/* 155 */     if (updateTemplate) {
/* 156 */       this.jetDlg.updateHashTemplate(hash);
/*     */     }
/*     */ 
/* 159 */     return hash;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenJetHashAddingHandler
 * JD-Core Version:    0.6.2
 */