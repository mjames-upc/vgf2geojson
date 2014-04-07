/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.IDisplayPane;
/*     */ import com.raytheon.uf.viz.core.drawables.IRenderableDisplay;
/*     */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.PgenInterpDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*     */ import java.awt.Color;
/*     */ import java.util.List;
/*     */ 
/*     */ public class PgenInterpolationTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   private SELECT_STATUS status;
/*  64 */   List<AbstractDrawableComponent> selectedEls = null;
/*     */ 
/*  66 */   private PgenInterpDlg interpDlg = null;
/*  67 */   Symbol verifySymbol = null;
/*     */ 
/*     */   public PgenInterpolationTool()
/*     */   {
/*  75 */     this.status = SELECT_STATUS.START;
/*  76 */     this.verifySymbol = new Symbol(null, new Color[] { new Color(255, 0, 0) }, 1.0F, 1.0D, 
/*  77 */       Boolean.valueOf(false), null, "Marker", "FILLED_BOX");
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  86 */     if (this.mouseHandler == null)
/*     */     {
/*  88 */       this.mouseHandler = new PgenInterpHandler();
/*     */     }
/*     */ 
/*  92 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   private void armDialog()
/*     */   {
/* 254 */     this.interpDlg.arm(this);
/*     */   }
/*     */ 
/*     */   public boolean comparisonIsValid(DrawableElement el2, DrawableElement orig)
/*     */   {
/* 268 */     if ((orig == null) || (el2 == null)) return false;
/* 269 */     if (orig == el2) return false;
/*     */ 
/* 271 */     if ((orig instanceof Gfa))
/*     */     {
/* 273 */       if (((el2 instanceof Gfa)) && 
/* 274 */         (((Gfa)el2).getGfaHazard().equals(((Gfa)orig).getGfaHazard())) && 
/* 275 */         (((Gfa)el2).getGfaTag().equals(((Gfa)orig).getGfaTag())) && 
/* 276 */         (((Gfa)el2).getGfaDesk().equals(((Gfa)orig).getGfaDesk()))) {
/* 277 */         return true;
/*     */       }
/*     */ 
/* 280 */       return false;
/*     */     }
/*     */ 
/* 284 */     if (el2.getPgenCategory().equals(orig.getPgenCategory()))
/*     */     {
/* 286 */       if ((bothClosed(orig, el2)) || (bothOpen(orig, el2))) {
/* 287 */         return true;
/*     */       }
/*     */ 
/* 290 */       return false;
/*     */     }
/*     */ 
/* 293 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean selectionIsValid(DrawableElement el1)
/*     */   {
/* 305 */     if (el1 == null) return false;
/*     */ 
/* 307 */     if ((el1.getPgenCategory().equals("Lines")) || (el1.getPgenCategory().equals("Front")) || 
/* 308 */       (isInterpolableSigmet(el1)) || ((el1 instanceof Gfa))) {
/* 309 */       return true;
/*     */     }
/* 311 */     return false;
/*     */   }
/*     */ 
/*     */   public void performInterpolation()
/*     */   {
/* 327 */     InterpolationProperties props = new InterpolationProperties(this.interpDlg.getStartTime(), 
/* 328 */       this.interpDlg.getEndTime(), this.interpDlg.getInterval());
/*     */ 
/* 333 */     List deList = PgenInterpolator.interpolate(((AbstractDrawableComponent)this.selectedEls.get(0)).getPrimaryDE(), ((AbstractDrawableComponent)this.selectedEls.get(1)).getPrimaryDE(), 
/* 334 */       props, getDescriptor(this.mapEditor));
/*     */ 
/* 339 */     if (deList.size() > 0) this.drawingLayer.addElements(deList);
/*     */ 
/* 344 */     this.drawingLayer.removeSelected();
/* 345 */     this.interpDlg.disarm();
/* 346 */     this.status = SELECT_STATUS.START;
/* 347 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   private boolean bothClosed(DrawableElement el1, DrawableElement el2)
/*     */   {
/* 355 */     if ((((ILine)el1).isClosedLine().booleanValue()) && (((ILine)el2).isClosedLine().booleanValue())) {
/* 356 */       return true;
/*     */     }
/*     */ 
/* 359 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean bothOpen(DrawableElement el1, DrawableElement el2)
/*     */   {
/* 366 */     if ((((ILine)el1).isClosedLine().booleanValue()) || (((ILine)el2).isClosedLine().booleanValue())) {
/* 367 */       return false;
/*     */     }
/*     */ 
/* 370 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean isInterpolableSigmet(DrawableElement el)
/*     */   {
/* 378 */     if (el.getPgenCategory().equals("SIGMET"))
/*     */     {
/* 380 */       Sigmet sig = (Sigmet)el;
/*     */ 
/* 382 */       if ((!sig.getType().contains("Text")) && (!sig.getType().contains("Isolated"))) {
/* 383 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 391 */     return false;
/*     */   }
/*     */ 
/*     */   private IMapDescriptor getDescriptor(AbstractEditor editor)
/*     */   {
/* 398 */     IMapDescriptor descriptor = null;
/* 399 */     IRenderableDisplay display = editor.getActiveDisplayPane()
/* 400 */       .getRenderableDisplay();
/* 401 */     if (display != null) {
/* 402 */       descriptor = (IMapDescriptor)display.getDescriptor();
/*     */     }
/* 404 */     return descriptor;
/*     */   }
/*     */ 
/*     */   private boolean useGfaFcsthr(DrawableElement el)
/*     */   {
/* 411 */     if (!(el instanceof Gfa)) {
/* 412 */       return false;
/*     */     }
/*     */ 
/* 415 */     String fcstHr = ((Gfa)el).getGfaFcstHr();
/* 416 */     if ((fcstHr.contains("-")) || (fcstHr.contains(":"))) {
/* 417 */       return false;
/*     */     }
/*     */ 
/* 420 */     return true;
/*     */   }
/*     */ 
/*     */   public class PgenInterpHandler extends InputHandlerDefaultImpl
/*     */   {
/* 104 */     OperationFilter interpFilter = new OperationFilter(Operation.INTERPOLATE);
/*     */ 
/*     */     public PgenInterpHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 114 */       if (!PgenInterpolationTool.this.isResourceEditable()) return false;
/*     */ 
/* 117 */       Coordinate loc = PgenInterpolationTool.this.mapEditor.translateClick(anX, aY);
/* 118 */       if (loc == null) return false;
/*     */ 
/* 120 */       PgenInterpolationTool.this.selectedEls = PgenInterpolationTool.this.drawingLayer.getAllSelected();
/* 121 */       PgenInterpolationTool.this.interpDlg = ((PgenInterpDlg)PgenInterpolationTool.this.attrDlg);
/*     */ 
/* 126 */       if (button == 1)
/*     */       {
/* 128 */         switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenInterpolationTool$SELECT_STATUS()[PgenInterpolationTool.this.status.ordinal()])
/*     */         {
/*     */         case 1:
/* 133 */           DrawableElement el1 = PgenInterpolationTool.this.drawingLayer.getNearestElement(loc, this.interpFilter);
/* 134 */           if (PgenInterpolationTool.this.selectionIsValid(el1)) {
/* 135 */             PgenInterpolationTool.this.drawingLayer.setSelected(el1);
/* 136 */             PgenInterpolationTool.this.interpDlg.enableStartTime();
/* 137 */             if (PgenInterpolationTool.this.useGfaFcsthr(el1)) {
/* 138 */               PgenInterpolationTool.this.interpDlg.setStartTime(((Gfa)el1).getGfaFcstHr());
/*     */             }
/* 140 */             PgenInterpolationTool.this.status = PgenInterpolationTool.SELECT_STATUS.SELECTED_1;
/*     */           }
/* 142 */           break;
/*     */         case 2:
/* 147 */           PgenInterpolationTool.this.drawingLayer.registerSelectedSymbol((AbstractDrawableComponent)PgenInterpolationTool.this.selectedEls.get(0), PgenInterpolationTool.this.verifySymbol);
/* 148 */           if ((PgenInterpolationTool.this.selectedEls.get(0) instanceof Gfa)) {
/* 149 */             PgenInterpolationTool.this.interpDlg.disableStartTime();
/*     */           }
/* 151 */           PgenInterpolationTool.this.status = PgenInterpolationTool.SELECT_STATUS.VERIFIED_1;
/* 152 */           break;
/*     */         case 3:
/* 159 */           DrawableElement el2 = PgenInterpolationTool.this.drawingLayer.getNearestElement(loc, this.interpFilter);
/* 160 */           DrawableElement first = ((AbstractDrawableComponent)PgenInterpolationTool.this.selectedEls.get(0)).getPrimaryDE();
/* 161 */           if (PgenInterpolationTool.this.comparisonIsValid(el2, first)) {
/* 162 */             PgenInterpolationTool.this.drawingLayer.addSelected(el2);
/* 163 */             PgenInterpolationTool.this.interpDlg.enableEndTime();
/* 164 */             if (PgenInterpolationTool.this.useGfaFcsthr(el2)) {
/* 165 */               PgenInterpolationTool.this.interpDlg.setEndTime(((Gfa)el2).getGfaFcstHr());
/*     */             }
/* 167 */             PgenInterpolationTool.this.status = PgenInterpolationTool.SELECT_STATUS.SELECTED_2;
/*     */           }
/* 169 */           break;
/*     */         case 4:
/* 174 */           PgenInterpolationTool.this.drawingLayer.registerSelectedSymbol((AbstractDrawableComponent)PgenInterpolationTool.this.selectedEls.get(1), PgenInterpolationTool.this.verifySymbol);
/* 175 */           if ((PgenInterpolationTool.this.selectedEls.get(1) instanceof Gfa)) {
/* 176 */             PgenInterpolationTool.this.interpDlg.disableEndTime();
/*     */           }
/* 178 */           PgenInterpolationTool.this.status = PgenInterpolationTool.SELECT_STATUS.VERIFIED_2;
/* 179 */           PgenInterpolationTool.this.armDialog();
/*     */         }
/*     */ 
/* 183 */         PgenInterpolationTool.this.mapEditor.refresh();
/*     */ 
/* 185 */         return false;
/*     */       }
/*     */ 
/* 192 */       if (button == 3)
/*     */       {
/* 194 */         switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenInterpolationTool$SELECT_STATUS()[PgenInterpolationTool.this.status.ordinal()])
/*     */         {
/*     */         case 1:
/* 199 */           PgenUtil.setSelectingMode();
/* 200 */           break;
/*     */         case 2:
/* 205 */           PgenInterpolationTool.this.drawingLayer.removeSelected();
/* 206 */           PgenInterpolationTool.this.status = PgenInterpolationTool.SELECT_STATUS.START;
/* 207 */           break;
/*     */         case 3:
/* 212 */           PgenInterpolationTool.this.interpDlg.enableStartTime();
/* 213 */           PgenInterpolationTool.this.interpDlg.enableEndTime();
/* 214 */           PgenInterpolationTool.this.drawingLayer.removeSelected();
/* 215 */           PgenInterpolationTool.this.status = PgenInterpolationTool.SELECT_STATUS.START;
/* 216 */           break;
/*     */         case 4:
/* 221 */           PgenInterpolationTool.this.drawingLayer.removeSelected((AbstractDrawableComponent)PgenInterpolationTool.this.selectedEls.get(1));
/* 222 */           PgenInterpolationTool.this.status = PgenInterpolationTool.SELECT_STATUS.VERIFIED_1;
/* 223 */           break;
/*     */         case 5:
/* 228 */           PgenInterpolationTool.this.drawingLayer.removeSelected();
/* 229 */           PgenInterpolationTool.this.interpDlg.enableStartTime();
/* 230 */           PgenInterpolationTool.this.interpDlg.enableEndTime();
/* 231 */           PgenInterpolationTool.this.interpDlg.disarm();
/* 232 */           PgenInterpolationTool.this.status = PgenInterpolationTool.SELECT_STATUS.START;
/*     */         }
/*     */ 
/* 236 */         PgenInterpolationTool.this.mapEditor.refresh();
/*     */ 
/* 238 */         return true;
/*     */       }
/*     */ 
/* 243 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static enum SELECT_STATUS
/*     */   {
/*  57 */     START, SELECTED_1, VERIFIED_1, SELECTED_2, VERIFIED_2;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenInterpolationTool
 * JD-Core Version:    0.6.2
 */