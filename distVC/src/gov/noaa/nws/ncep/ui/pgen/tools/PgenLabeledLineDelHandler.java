/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Label;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class PgenLabeledLineDelHandler extends InputHandlerDefaultImpl
/*     */ {
/*     */   private AbstractEditor mapEditor;
/*     */   private PgenResource drawingLayer;
/*     */   private ILabeledLine prevTool;
/*     */   private AttrDlg dlg;
/*     */   private boolean delLine;
/*     */   private boolean flip;
/*     */   private boolean openClose;
/*     */   private LabeledLine labeledLine;
/*     */ 
/*     */   public PgenLabeledLineDelHandler(AbstractEditor mapEditor, PgenResource drawingLayer, ILabeledLine prevTool, AttrDlg dlg, boolean delLine, boolean flip, boolean openClose)
/*     */   {
/*  75 */     this.mapEditor = mapEditor;
/*  76 */     this.drawingLayer = drawingLayer;
/*  77 */     this.prevTool = prevTool;
/*  78 */     this.dlg = dlg;
/*  79 */     this.labeledLine = prevTool.getLabeledLine();
/*  80 */     this.delLine = delLine;
/*  81 */     this.flip = flip;
/*  82 */     this.openClose = openClose;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/*  93 */     if (!this.drawingLayer.isEditable()) return false;
/*     */ 
/*  95 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/*  96 */     if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/*  98 */     if (button == 1)
/*     */     {
/* 100 */       if (this.labeledLine != null) {
/* 101 */         AbstractDrawableComponent nearestComp = this.drawingLayer.getNearestComponent(loc);
/*     */ 
/* 103 */         if ((nearestComp != null) && (nearestComp.getPgenType().equalsIgnoreCase(this.labeledLine.getPgenType()))) {
/* 104 */           this.labeledLine = ((LabeledLine)nearestComp);
/*     */ 
/* 107 */           LabeledLine newll = this.labeledLine.copy();
/*     */ 
/* 109 */           AbstractDrawableComponent adc = getNearest(loc, newll);
/* 110 */           if (adc == null) return false;
/*     */ 
/* 112 */           if (this.flip)
/*     */           {
/* 114 */             if ((adc instanceof Line)) {
/* 115 */               Line newLn = (Line)PgenToolUtils.createReversedDrawableElement((Line)adc);
/* 116 */               newll.remove(adc);
/* 117 */               newll.add(newLn);
/*     */             } else {
/* 119 */               return false;
/*     */             }
/* 121 */           } else if (this.openClose) {
/* 122 */             if ((adc instanceof Line)) {
/* 123 */               Line newLn = (Line)adc.copy();
/* 124 */               if (newLn.isClosedLine().booleanValue()) {
/* 125 */                 newLn.setClosed(Boolean.valueOf(false));
/*     */               }
/*     */               else {
/* 128 */                 newLn.setClosed(Boolean.valueOf(true));
/*     */               }
/* 130 */               newll.remove(adc);
/* 131 */               newll.add(newLn);
/*     */             } else {
/* 133 */               return false;
/*     */             }
/*     */           }
/*     */           else {
/* 137 */             newll.remove(adc);
/*     */           }
/*     */ 
/* 140 */           this.drawingLayer.replaceElement(this.labeledLine, newll);
/* 141 */           this.labeledLine = newll;
/* 142 */           this.prevTool.setLabeledLine(newll);
/*     */ 
/* 144 */           if (this.drawingLayer.getSelectedDE() != null) {
/* 145 */             resetSelected(newll);
/*     */           }
/*     */ 
/* 148 */           this.mapEditor.refresh();
/*     */         }
/*     */       }
/* 151 */       return true;
/*     */     }
/*     */ 
/* 154 */     if (button == 3)
/*     */     {
/* 156 */       this.dlg.resetLabeledLineBtns();
/* 157 */       this.prevTool.resetMouseHandler();
/* 158 */       return true;
/*     */     }
/*     */ 
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */   {
/* 171 */     if ((!this.drawingLayer.isEditable()) || (this.shiftDown)) return false;
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */   private AbstractDrawableComponent getNearest(Coordinate loc, LabeledLine ll)
/*     */   {
/* 184 */     AbstractDrawableComponent ret = null;
/* 185 */     double[] locScreen = this.mapEditor.translateInverseClick(loc);
/*     */ 
/* 188 */     Iterator it = ll.getComponentIterator();
/* 189 */     double minDist = 1.7976931348623157E+308D;
/*     */ 
/* 191 */     while (it.hasNext()) {
/* 192 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 193 */       double dist = 1.7976931348623157E+308D;
/*     */ 
/* 195 */       if ((this.delLine) && ((adc instanceof Line)))
/*     */       {
/* 198 */         Object[] pts = adc.getPoints().toArray();
/*     */ 
/* 200 */         for (int ii = 0; ii < pts.length; ii++)
/*     */         {
/* 202 */           if (ii == pts.length - 1) {
/* 203 */             if ((!(adc instanceof Line)) || (!((Line)adc).isClosedLine().booleanValue()))
/*     */               break;
/* 205 */             dist = PgenResource.distanceFromLineSegment(loc, (Coordinate)pts[ii], (Coordinate)pts[0]);
/*     */           }
/*     */           else
/*     */           {
/* 214 */             dist = PgenResource.distanceFromLineSegment(loc, (Coordinate)pts[ii], (Coordinate)pts[(ii + 1)]);
/*     */           }
/*     */ 
/* 218 */           if (dist < minDist)
/*     */           {
/* 220 */             minDist = dist;
/* 221 */             ret = adc;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/* 226 */       else if ((!this.delLine) && ((adc instanceof Label)))
/*     */       {
/* 228 */         double[] pt = this.mapEditor.translateInverseClick(((Label)adc).getSpe().getLocation());
/* 229 */         Point ptScreen = new GeometryFactory().createPoint(new Coordinate(pt[0], pt[1]));
/*     */ 
/* 231 */         dist = ptScreen.distance(new GeometryFactory().createPoint(new Coordinate(locScreen[0], locScreen[1])));
/*     */ 
/* 233 */         if (dist < minDist)
/*     */         {
/* 235 */           minDist = dist;
/* 236 */           ret = adc;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 243 */     return ret;
/*     */   }
/*     */ 
/*     */   private void resetSelected(LabeledLine labeledLine)
/*     */   {
/* 251 */     if (labeledLine != null) {
/* 252 */       this.drawingLayer.removeSelected();
/* 253 */       Iterator it = labeledLine.createDEIterator();
/* 254 */       while (it.hasNext())
/* 255 */         this.drawingLayer.addSelected((AbstractDrawableComponent)it.next());
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenLabeledLineDelHandler
 * JD-Core Version:    0.6.2
 */