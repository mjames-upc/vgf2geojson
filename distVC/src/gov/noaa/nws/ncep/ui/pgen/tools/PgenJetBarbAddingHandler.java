/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.JetAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector.VectorType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetBarb;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ 
/*     */ public class PgenJetBarbAddingHandler extends InputHandlerDefaultImpl
/*     */ {
/*     */   private AbstractEditor mapEditor;
/*     */   private PgenResource drawingLayer;
/*     */   private IJetBarb prevTool;
/*     */   private JetAttrDlg jetDlg;
/*     */ 
/*     */   public PgenJetBarbAddingHandler(AbstractEditor mapEditor, PgenResource drawingLayer, IJetBarb prevTool, JetAttrDlg jetDlg)
/*     */   {
/*  67 */     this.mapEditor = mapEditor;
/*  68 */     this.drawingLayer = drawingLayer;
/*  69 */     this.prevTool = prevTool;
/*  70 */     this.jetDlg = jetDlg;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/*  81 */     if (!this.drawingLayer.isEditable()) return false;
/*     */ 
/*  83 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/*  84 */     if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/*  86 */     Jet jet = this.prevTool.getJet();
/*  87 */     if (jet == null) return false;
/*     */ 
/*  89 */     if (button == 1)
/*     */     {
/*  91 */       Jet newJet = jet.copy();
/*  92 */       newJet.addBarb(createWindInfo(loc, newJet, true));
/*     */ 
/*  94 */       this.drawingLayer.replaceElement(jet, newJet);
/*  95 */       jet = newJet;
/*     */ 
/*  97 */       this.prevTool.setJet(jet);
/*     */ 
/*  99 */       this.mapEditor.refresh();
/*     */ 
/* 102 */       if ((this.jetDlg.getFLDepth() != null) && (!this.jetDlg.getFLDepth().isEmpty())) {
/* 103 */         this.jetDlg.clearFLDepth();
/*     */       }
/*     */ 
/* 106 */       this.jetDlg.updateSegmentPane();
/*     */ 
/* 108 */       return true;
/*     */     }
/*     */ 
/* 111 */     if (button == 3)
/*     */     {
/* 113 */       this.drawingLayer.removeGhostLine();
/* 114 */       this.mapEditor.refresh();
/*     */ 
/* 116 */       if (this.jetDlg != null) this.jetDlg.closeBarbDlg();
/*     */ 
/* 118 */       this.prevTool.resetMouseHandler();
/* 119 */       return true;
/*     */     }
/*     */ 
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseMove(int x, int y)
/*     */   {
/* 138 */     if (!this.drawingLayer.isEditable()) return false;
/*     */ 
/* 141 */     Coordinate loc = this.mapEditor.translateClick(x, y);
/* 142 */     if (loc == null) return false;
/*     */ 
/* 144 */     Jet jet = this.prevTool.getJet();
/* 145 */     if (jet == null) return false;
/*     */ 
/* 147 */     this.drawingLayer.setGhostLine(createWindInfo(loc, jet, false));
/* 148 */     this.mapEditor.refresh();
/*     */ 
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */   {
/* 156 */     if ((!this.drawingLayer.isEditable()) || (this.shiftDown)) return false;
/* 157 */     return true;
/*     */   }
/*     */ 
/*     */   private DECollection createWindInfo(Coordinate loc, Jet aJet, boolean updateTemplate)
/*     */   {
/* 167 */     DECollection wind = new DECollection("WindInfo");
/* 168 */     wind.setParent(aJet);
/*     */     Jet tmp22_21 = aJet; tmp22_21.getClass(); Jet.JetBarb barb = new Jet.JetBarb(tmp22_21, null, new Color[] { new Color(0, 255, 0), new Color(255, 0, 0) }, 
/* 172 */       2.0F, 2.0D, Boolean.valueOf(true), loc, IVector.VectorType.WIND_BARB, 
/* 173 */       100.0D, 270.0D, 1.0D, false, "Vector", "Barb");
/*     */ 
/* 175 */     IAttribute barbAttr = this.jetDlg.getBarbAttr();
/*     */ 
/* 177 */     if (barbAttr != null) {
/* 178 */       barb.update(barbAttr);
/*     */     }
/*     */ 
/* 181 */     if (updateTemplate) {
/* 182 */       this.jetDlg.updateBarbTemplate(barb);
/*     */     }
/*     */ 
/* 185 */     barb.setSpeed(this.jetDlg.getBarbSpeed());
/* 186 */     wind.add(barb);
/*     */     String[] flInfo;
/*     */     String[] flInfo;
/* 190 */     if (this.jetDlg.getFLDepth() == null) {
/* 191 */       flInfo = new String[1];
/*     */     }
/*     */     else {
/* 194 */       flInfo = new String[2];
/* 195 */       flInfo[1] = this.jetDlg.getFLDepth();
/*     */     }
/*     */ 
/* 198 */     flInfo[0] = ("FL" + Integer.toString(this.jetDlg.getFlightLevel()));
/*     */     Jet tmp215_214 = aJet; tmp215_214.getClass(); Text txt = new Jet.JetText(tmp215_214, null, "Courier", 18.0F, 
/* 201 */       IText.TextJustification.CENTER, new Coordinate(0.0D, 0.0D), 
/* 202 */       0.0D, IText.TextRotation.SCREEN_RELATIVE, flInfo, 
/* 203 */       IText.FontStyle.REGULAR, new Color(0, 255, 0), 0, 0, 
/* 204 */       true, IText.DisplayType.NORMAL, "Text", "General Text");
/*     */ 
/* 206 */     wind.add(txt);
/*     */ 
/* 208 */     IAttribute flAttr = this.jetDlg.getFLAttr();
/*     */ 
/* 210 */     if (flAttr != null) {
/* 211 */       txt.update(flAttr);
/*     */     }
/*     */ 
/* 214 */     if (updateTemplate) {
/* 215 */       this.jetDlg.updateFlTemplate(txt);
/*     */     }
/*     */ 
/* 218 */     txt.setText(flInfo);
/*     */ 
/* 220 */     return wind;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenJetBarbAddingHandler
 * JD-Core Version:    0.6.2
 */