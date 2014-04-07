/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.IDisplayPane;
/*     */ import com.raytheon.uf.viz.core.drawables.IDescriptor;
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaReducePoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo;
/*     */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.geotools.coverage.grid.GeneralGridGeometry;
/*     */ import org.geotools.coverage.grid.GridEnvelope2D;
/*     */ import org.opengis.coverage.grid.GridEnvelope;
/*     */ 
/*     */ public class PgenCopyElement extends AbstractPgenTool
/*     */ {
/*  71 */   protected IInputHandler copyHandler = null;
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  85 */     if (this.copyHandler == null)
/*     */     {
/*  87 */       this.copyHandler = new PgenCopyHandler();
/*     */     }
/*     */ 
/*  91 */     return this.copyHandler;
/*     */   }
/*     */ 
/*     */   public class PgenCopyHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     private boolean preempt;
/* 103 */     private OperationFilter copyFilter = new OperationFilter(Operation.COPY_MOVE);
/* 104 */     private Coordinate ptSelected = null;
/*     */ 
/* 107 */     protected AbstractDrawableComponent ghostEl = null;
/*     */     protected boolean simulate;
/*     */ 
/*     */     public PgenCopyHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 121 */       if (!PgenCopyElement.this.isResourceEditable()) return false;
/*     */ 
/* 123 */       this.preempt = false;
/*     */ 
/* 126 */       Coordinate loc = PgenCopyElement.this.mapEditor.translateClick(anX, aY);
/* 127 */       if ((loc == null) || (this.shiftDown) || (this.simulate)) return false;
/*     */ 
/* 129 */       if (button == 1)
/*     */       {
/* 131 */         if (PgenCopyElement.this.drawingLayer.getSelectedComp() == null)
/*     */         {
/* 137 */           AbstractDrawableComponent nadc = PgenCopyElement.this.drawingLayer.getNearestComponent(loc, this.copyFilter, true);
/* 138 */           if ((nadc instanceof Contours)) {
/* 139 */             nadc = PgenCopyElement.this.drawingLayer.getNearestElement(loc, (DECollection)nadc).getParent();
/*     */           }
/*     */ 
/* 142 */           PgenCopyElement.this.drawingLayer.setSelected(nadc);
/* 143 */           if (nadc != null) this.preempt = true;
/* 144 */           PgenCopyElement.this.mapEditor.refresh();
/*     */         }
/* 146 */         return this.preempt;
/*     */       }
/*     */ 
/* 149 */       if (button == 3)
/*     */       {
/* 151 */         if (PgenCopyElement.this.drawingLayer.getSelectedComp() != null)
/*     */         {
/* 153 */           PgenCopyElement.this.drawingLayer.removeSelected();
/* 154 */           PgenCopyElement.this.drawingLayer.removeGhostLine();
/* 155 */           this.ghostEl = null;
/* 156 */           PgenCopyElement.this.mapEditor.refresh();
/*     */         }
/*     */         else
/*     */         {
/* 160 */           PgenUtil.setSelectingMode();
/*     */         }
/*     */ 
/* 163 */         return true;
/*     */       }
/*     */ 
/* 168 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int anX, int aY, int button)
/*     */     {
/* 183 */       if ((this.shiftDown) || (!PgenCopyElement.this.isResourceEditable())) return false;
/*     */ 
/* 186 */       Coordinate loc = PgenCopyElement.this.mapEditor.translateClick(anX, aY);
/*     */ 
/* 189 */       AbstractDrawableComponent elSelected = PgenCopyElement.this.drawingLayer.getSelectedComp();
/* 190 */       Color ghostColor = new Color(255, 255, 255);
/* 191 */       double distanceToSelect = 20.0D;
/*     */ 
/* 194 */       if (loc != null)
/*     */       {
/* 196 */         if ((PgenCopyElement.this.drawingLayer.getDistance(elSelected, loc) > distanceToSelect) && (this.ghostEl == null)) {
/* 197 */           return false;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 202 */         if (elSelected == null) {
/* 203 */           return false;
/*     */         }
/* 205 */         return true;
/*     */       }
/*     */ 
/* 208 */       if (elSelected != null)
/*     */       {
/* 210 */         this.preempt = true;
/*     */ 
/* 212 */         for (Coordinate elPoint : elSelected.getPoints())
/*     */         {
/* 214 */           double[] screenPt = PgenCopyElement.this.mapEditor.translateInverseClick(elPoint);
/*     */ 
/* 216 */           double distance = Math.sqrt((screenPt[0] - anX) * (screenPt[0] - anX) + 
/* 217 */             (screenPt[1] - aY) * (screenPt[1] - aY));
/*     */ 
/* 219 */           if ((distance < distanceToSelect) && (this.ghostEl == null)) {
/* 220 */             this.ptSelected = elPoint;
/* 221 */             this.ghostEl = PgenCopyElement.this.drawingLayer.getSelectedComp().copy();
/* 222 */             break;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 227 */         if (this.ghostEl != null)
/*     */         {
/* 230 */           double[] ptScreen = PgenCopyElement.this.mapEditor.translateInverseClick(this.ptSelected);
/*     */ 
/* 232 */           double deltaX = anX - ptScreen[0];
/* 233 */           double deltaY = aY - ptScreen[1];
/*     */ 
/* 236 */           for (int idx = 0; idx < elSelected.getPoints().size(); idx++) {
/* 237 */             double[] scnPt = PgenCopyElement.this.mapEditor.translateInverseClick((Coordinate)elSelected.getPoints().get(idx));
/* 238 */             scnPt[0] += deltaX;
/* 239 */             scnPt[1] += deltaY;
/*     */ 
/* 241 */             GridEnvelope ge = PgenCopyElement.this.mapEditor.getActiveDisplayPane().getDescriptor().getGridGeometry().getGridRange();
/*     */ 
/* 243 */             double[] world = PgenCopyElement.this.mapEditor.getActiveDisplayPane().screenToGrid(scnPt[0], scnPt[1], 0.0D);
/*     */ 
/* 245 */             if (world[0] > ((GridEnvelope2D)ge).getWidth())
/*     */             {
/* 247 */               world[0] -= ((GridEnvelope2D)ge).getWidth();
/* 248 */               scnPt = PgenCopyElement.this.mapEditor.getActiveDisplayPane().gridToScreen(world);
/*     */             }
/* 250 */             else if (world[0] < 0.0D)
/*     */             {
/* 252 */               world[0] += ((GridEnvelope2D)ge).getWidth();
/* 253 */               scnPt = PgenCopyElement.this.mapEditor.getActiveDisplayPane().gridToScreen(world);
/*     */             }
/*     */ 
/* 257 */             Coordinate cord = PgenCopyElement.this.mapEditor.translateClick(scnPt[0], scnPt[1]);
/*     */ 
/* 259 */             if (cord != null)
/*     */             {
/* 261 */               ((Coordinate)this.ghostEl.getPoints().get(idx)).x = cord.x;
/* 262 */               ((Coordinate)this.ghostEl.getPoints().get(idx)).y = cord.y;
/*     */             }
/*     */           }
/*     */ 
/* 266 */           if ((elSelected instanceof Gfa))
/*     */           {
/* 268 */             double[] scnPt = PgenCopyElement.this.mapEditor.translateInverseClick(((Gfa)elSelected).getGfaTextCoordinate());
/* 269 */             scnPt[0] += deltaX;
/* 270 */             scnPt[1] += deltaY;
/*     */ 
/* 272 */             Coordinate cord = PgenCopyElement.this.mapEditor.translateClick(scnPt[0], scnPt[1]);
/* 273 */             if (cord != null) {
/* 274 */               ((Gfa)this.ghostEl).getGfaTextCoordinate().x = cord.x;
/* 275 */               ((Gfa)this.ghostEl).getGfaTextCoordinate().y = cord.y;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 293 */           this.ghostEl.setColors(new Color[] { ghostColor, new Color(255, 255, 255) });
/*     */ 
/* 295 */           PgenCopyElement.this.drawingLayer.setGhostLine(this.ghostEl);
/* 296 */           PgenCopyElement.this.mapEditor.refresh();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 302 */       if (this.preempt)
/*     */       {
/* 304 */         this.simulate = true;
/*     */ 
/* 306 */         PgenUtil.simulateMouseDown(anX, aY, 1, PgenCopyElement.this.mapEditor);
/*     */ 
/* 308 */         this.simulate = false;
/*     */       }
/*     */ 
/* 311 */       return this.preempt;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int x, int y, int button)
/*     */     {
/* 324 */       if ((!PgenCopyElement.this.isResourceEditable()) || (this.shiftDown) || (this.simulate)) return false;
/*     */ 
/* 326 */       if (this.ghostEl != null)
/*     */       {
/* 330 */         Iterator iterator1 = this.ghostEl.createDEIterator();
/* 331 */         Iterator iterator2 = PgenCopyElement.this.drawingLayer.getSelectedComp().createDEIterator();
/*     */ 
/* 333 */         while ((iterator1.hasNext()) && (iterator2.hasNext())) {
/* 334 */           ((DrawableElement)iterator1.next()).setColors(((DrawableElement)iterator2.next()).getColors());
/*     */         }
/*     */ 
/* 337 */         AbstractDrawableComponent parent = PgenCopyElement.this.drawingLayer.getSelectedComp().getParent();
/*     */ 
/* 339 */         if ((this.ghostEl instanceof WatchBox)) {
/* 340 */           if (PgenWatchBoxModifyTool.resnapWatchBox(PgenCopyElement.this.mapEditor, (WatchBox)this.ghostEl, (WatchBox)this.ghostEl)) {
/* 341 */             PgenCopyElement.this.drawingLayer.addElement(this.ghostEl);
/* 342 */             PgenCopyElement.this.drawingLayer.setSelected(this.ghostEl);
/*     */           }
/*     */         }
/* 345 */         else if (parent.getName().equalsIgnoreCase("Contours")) {
/* 346 */           copyContoursComponent(parent);
/*     */         }
/* 348 */         else if ((parent instanceof Outlook)) {
/* 349 */           ((Outlook)parent).add(this.ghostEl);
/* 350 */           PgenCopyElement.this.drawingLayer.setSelected(this.ghostEl);
/*     */         }
/* 352 */         else if ((this.ghostEl instanceof Gfa))
/*     */         {
/* 354 */           if (((Gfa)this.ghostEl).getGfaFcstHr().indexOf("-") > -1)
/*     */           {
/* 356 */             ((Gfa)this.ghostEl).snap();
/* 357 */             GfaReducePoint.WarningForOverThreeLines((Gfa)this.ghostEl);
/*     */           }
/*     */ 
/* 360 */           ((Gfa)this.ghostEl).setGfaVorText(Gfa.buildVorText((Gfa)this.ghostEl));
/* 361 */           PgenCopyElement.this.drawingLayer.addElement(this.ghostEl);
/* 362 */           PgenCopyElement.this.drawingLayer.setSelected(this.ghostEl);
/*     */         }
/* 366 */         else if (SigmetInfo.isSnapADC(this.ghostEl)) {
/* 367 */           ArrayList list = SnapUtil.getSnapWithStation(
/* 368 */             this.ghostEl.getPoints(), 
/* 369 */             SnapUtil.VOR_STATION_LIST, 
/* 370 */             10, 
/* 371 */             SigmetInfo.getNumOfCompassPts(this.ghostEl));
/*     */ 
/* 373 */           AbstractDrawableComponent ghostElCp = this.ghostEl.copy();
/* 374 */           ((DrawableElement)ghostElCp).setPoints(list);
/* 375 */           PgenCopyElement.this.drawingLayer.addElement(ghostElCp);
/* 376 */           PgenCopyElement.this.drawingLayer.setSelected(ghostElCp);
/*     */         }
/*     */         else {
/* 379 */           PgenCopyElement.this.drawingLayer.addElement(this.ghostEl);
/* 380 */           PgenCopyElement.this.drawingLayer.setSelected(this.ghostEl);
/*     */         }
/*     */ 
/* 384 */         PgenCopyElement.this.drawingLayer.removeGhostLine();
/* 385 */         this.ghostEl = null;
/* 386 */         PgenCopyElement.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 390 */       return true;
/*     */     }
/*     */ 
/*     */     private void copyContoursComponent(AbstractDrawableComponent adc)
/*     */     {
/* 403 */       Contours newContours = new Contours();
/* 404 */       Iterator iterator = ((Contours)adc).getComponentIterator();
/*     */ 
/* 406 */       while (iterator.hasNext())
/*     */       {
/* 408 */         AbstractDrawableComponent oldAdc = (AbstractDrawableComponent)iterator.next();
/* 409 */         AbstractDrawableComponent newAdc = oldAdc.copy();
/*     */ 
/* 415 */         if (oldAdc.equals(PgenCopyElement.this.drawingLayer.getSelectedComp()))
/*     */         {
/* 417 */           AbstractDrawableComponent dup = this.ghostEl.copy();
/* 418 */           dup.setParent(newContours);
/* 419 */           newContours.add(dup);
/*     */ 
/* 421 */           PgenCopyElement.this.drawingLayer.setSelected(dup);
/*     */         }
/*     */ 
/* 425 */         newAdc.setParent(newContours);
/* 426 */         newContours.add(newAdc);
/*     */       }
/*     */ 
/* 429 */       newContours.update((Contours)adc);
/* 430 */       PgenCopyElement.this.drawingLayer.replaceElement(adc, newContours);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenCopyElement
 * JD-Core Version:    0.6.2
 */