/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.IJetTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.KinkLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ 
/*     */ public class PgenConnectTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  66 */     if (this.mouseHandler == null) {
/*  67 */       this.mouseHandler = new PgenConnectHandler();
/*     */     }
/*     */ 
/*  70 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public class PgenConnectHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     String pgenCategory;
/*     */     String pgenType;
/*  88 */     private OperationFilter connectFilter = new OperationFilter(Operation.CONNECT);
/*     */ 
/*  93 */     protected int nearPt = -1;
/*  94 */     protected int secondPt = -1;
/*     */ 
/* 100 */     private MultiPointElement ghostEl = null;
/*     */ 
/* 105 */     private MultiPointElement firstEl = null;
/* 106 */     private MultiPointElement secondEl = null;
/*     */ 
/* 108 */     private Jet firstJet = null;
/* 109 */     private Jet secondJet = null;
/*     */ 
/* 114 */     private Color ghostColor = new Color(255, 255, 255);
/*     */ 
/*     */     public PgenConnectHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 125 */       if (!PgenConnectTool.this.isResourceEditable()) return false;
/*     */ 
/* 128 */       Coordinate loc = PgenConnectTool.this.mapEditor.translateClick(anX, aY);
/* 129 */       if (loc == null) return false;
/*     */ 
/* 131 */       if (button == 1)
/*     */       {
/* 133 */         if (this.firstEl == null)
/*     */         {
/* 136 */           DrawableElement elSelected = PgenConnectTool.this.drawingLayer.getNearestElement(loc, this.connectFilter);
/* 137 */           AbstractDrawableComponent adc = PgenConnectTool.this.drawingLayer.getNearestComponent(loc, this.connectFilter, false);
/*     */ 
/* 139 */           this.pgenCategory = getPgenCategory(elSelected, adc);
/* 140 */           this.pgenType = getPgenType(elSelected, adc);
/*     */ 
/* 142 */           if (((elSelected instanceof Line)) && (!(elSelected instanceof Arc)) && 
/* 143 */             (!(elSelected instanceof KinkLine)))
/*     */           {
/* 145 */             if ((adc != null) && ((adc instanceof Jet))) {
/* 146 */               this.firstJet = ((Jet)adc);
/*     */             }
/*     */ 
/* 149 */             this.firstEl = ((MultiPointElement)elSelected);
/* 150 */             PgenConnectTool.this.drawingLayer.setSelected(this.firstEl);
/*     */ 
/* 152 */             this.nearPt = findNearEnd(0, getNearestPtIndex(this.firstEl, loc), 
/* 153 */               this.firstEl.getPoints().size() - 1);
/*     */ 
/* 155 */             this.ghostEl = createGhostElement(this.firstEl, this.nearPt, loc);
/*     */ 
/* 157 */             PgenConnectTool.this.drawingLayer.setGhostLine(this.ghostEl);
/*     */ 
/* 159 */             PgenConnectTool.this.mapEditor.refresh();
/*     */           }
/*     */           else {
/* 162 */             return false;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 167 */           if (this.secondEl == null)
/*     */           {
/* 169 */             DrawableElement elSelected = PgenConnectTool.this.drawingLayer.getNearestElement(loc, this.connectFilter);
/* 170 */             AbstractDrawableComponent adc = PgenConnectTool.this.drawingLayer.getNearestComponent(loc, this.connectFilter, false);
/*     */ 
/* 172 */             if ((elSelected != this.firstEl) && (elSelected != null) && 
/* 173 */               (getPgenCategory(elSelected, adc).equals(this.pgenCategory)) && 
/* 174 */               (getPgenType(elSelected, adc).equals(this.pgenType)) && 
/* 175 */               (selectableContourLine((MultiPointElement)elSelected, this.firstEl)))
/*     */             {
/* 178 */               if ((adc != null) && ((adc instanceof Jet))) {
/* 179 */                 this.secondJet = ((Jet)adc);
/*     */               }
/*     */ 
/* 182 */               this.secondEl = ((MultiPointElement)elSelected);
/*     */ 
/* 184 */               MultiPointElement tmpEl = (MultiPointElement)this.secondEl.copy();
/*     */ 
/* 186 */               this.secondPt = findNearEnd(0, getNearestPtIndex(this.secondEl, loc), 
/* 187 */                 this.secondEl.getPoints().size() - 1);
/*     */ 
/* 190 */               if (this.secondPt > 0) {
/* 191 */                 int np = tmpEl.getPoints().size();
/* 192 */                 for (int ii = 0; ii < np; ii++) {
/* 193 */                   tmpEl.getPoints().set(ii, (Coordinate)this.secondEl.getPoints().get(np - ii - 1));
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/* 198 */               if (this.ghostEl.getPoints().size() > this.firstEl.getPoints().size()) {
/* 199 */                 this.ghostEl.removePoint(this.ghostEl.getPoints().size() - 1);
/*     */               }
/*     */ 
/* 203 */               this.ghostEl.getPoints().addAll(tmpEl.getPoints());
/*     */ 
/* 205 */               PgenConnectTool.this.drawingLayer.setGhostLine(this.ghostEl);
/*     */ 
/* 207 */               PgenConnectTool.this.mapEditor.refresh();
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 216 */             ArrayList oldElem = new ArrayList();
/* 217 */             ArrayList newElem = new ArrayList();
/*     */ 
/* 219 */             if ((this.firstJet != null) && (this.secondJet != null))
/*     */             {
/* 221 */               oldElem.add(this.firstJet);
/* 222 */               oldElem.add(this.secondJet);
/*     */ 
/* 224 */               newElem.add(connectJet(this.firstJet, this.secondJet));
/*     */             }
/* 227 */             else if ((this.firstEl.getParent() instanceof ContourLine))
/*     */             {
/* 230 */               oldElem.add(this.firstEl.getParent().getParent());
/* 231 */               newElem.add(connectContourLine(this.firstEl, this.secondEl));
/*     */             }
/*     */             else {
/* 234 */               oldElem.add(this.firstEl);
/* 235 */               oldElem.add(this.secondEl);
/*     */ 
/* 237 */               MultiPointElement mpe = (MultiPointElement)this.ghostEl.copy();
/* 238 */               mpe.setColors(this.firstEl.getColors());
/*     */ 
/* 240 */               newElem.add(mpe);
/*     */             }
/*     */ 
/* 244 */             PgenConnectTool.this.drawingLayer.replaceElements(oldElem, newElem);
/*     */ 
/* 249 */             PgenConnectTool.this.drawingLayer.removeGhostLine();
/* 250 */             PgenConnectTool.this.drawingLayer.removeSelected();
/*     */ 
/* 252 */             this.nearPt = -1;
/* 253 */             this.firstEl = null;
/* 254 */             this.secondEl = null;
/* 255 */             this.firstJet = null;
/* 256 */             this.secondJet = null;
/*     */ 
/* 258 */             PgenConnectTool.this.mapEditor.refresh();
/*     */           }
/*     */ 
/* 261 */           return false;
/*     */         }
/*     */ 
/* 265 */         return false;
/*     */       }
/*     */ 
/* 268 */       if (button == 3)
/*     */       {
/* 270 */         if (this.secondEl != null)
/*     */         {
/* 272 */           this.ghostEl = createGhostElement(this.firstEl, this.nearPt, loc);
/* 273 */           PgenConnectTool.this.drawingLayer.setGhostLine(this.ghostEl);
/*     */ 
/* 275 */           this.secondEl = null;
/* 276 */           this.secondJet = null;
/*     */         }
/* 280 */         else if (this.firstEl != null)
/*     */         {
/* 282 */           PgenConnectTool.this.drawingLayer.removeGhostLine();
/* 283 */           PgenConnectTool.this.drawingLayer.removeSelected();
/* 284 */           this.nearPt = 0;
/* 285 */           this.firstEl = null;
/* 286 */           this.firstJet = null;
/*     */         }
/*     */         else
/*     */         {
/* 290 */           PgenUtil.setSelectingMode();
/*     */         }
/*     */ 
/* 295 */         PgenConnectTool.this.mapEditor.refresh();
/*     */ 
/* 297 */         return false;
/*     */       }
/*     */ 
/* 302 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 316 */       if (!PgenConnectTool.this.isResourceEditable()) return false;
/*     */ 
/* 319 */       Coordinate loc = PgenConnectTool.this.mapEditor.translateClick(x, y);
/* 320 */       if (loc == null) return false;
/*     */ 
/* 322 */       if ((this.firstEl != null) && (this.secondEl == null))
/*     */       {
/* 324 */         this.ghostEl = createGhostElement(this.firstEl, this.nearPt, loc);
/* 325 */         PgenConnectTool.this.drawingLayer.setGhostLine(this.ghostEl);
/*     */ 
/* 327 */         PgenConnectTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 331 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int x, int y, int button)
/*     */     {
/* 343 */       if (!PgenConnectTool.this.isResourceEditable()) return false;
/* 344 */       return true;
/*     */     }
/*     */ 
/*     */     protected int getNearestPtIndex(MultiPointElement el, Coordinate pt)
/*     */     {
/* 356 */       int ptId = 0;
/* 357 */       double minDistance = -1.0D;
/*     */ 
/* 359 */       GeodeticCalculator gc = new GeodeticCalculator(PgenConnectTool.this.drawingLayer.getCoordinateReferenceSystem());
/* 360 */       gc.setStartingGeographicPoint(pt.x, pt.y);
/*     */ 
/* 362 */       int index = 0;
/* 363 */       for (Coordinate elPoint : el.getPoints())
/*     */       {
/* 365 */         gc.setDestinationGeographicPoint(elPoint.x, elPoint.y);
/*     */ 
/* 367 */         double dist = gc.getOrthodromicDistance();
/*     */ 
/* 369 */         if ((minDistance < 0.0D) || (dist < minDistance))
/*     */         {
/* 371 */           minDistance = dist;
/* 372 */           ptId = index;
/*     */         }
/*     */ 
/* 376 */         index++;
/*     */       }
/*     */ 
/* 380 */       return ptId;
/*     */     }
/*     */ 
/*     */     private int findNearEnd(int lowEnd, int near, int highEnd)
/*     */     {
/* 392 */       return near - lowEnd < highEnd - near ? lowEnd : highEnd;
/*     */     }
/*     */ 
/*     */     private MultiPointElement createGhostElement(MultiPointElement fstEl, int nearPt, Coordinate clickPt)
/*     */     {
/* 405 */       MultiPointElement gstEl = null;
/*     */ 
/* 407 */       if (fstEl != null)
/*     */       {
/* 409 */         gstEl = (MultiPointElement)fstEl.copy();
/* 410 */         gstEl.setColors(new Color[] { this.ghostColor, new Color(255, 255, 255) });
/*     */ 
/* 412 */         if (nearPt == 0) {
/* 413 */           int np = gstEl.getPoints().size();
/* 414 */           for (int ii = 0; ii < np; ii++) {
/* 415 */             gstEl.getPoints().set(ii, (Coordinate)fstEl.getPoints().get(np - ii - 1));
/*     */           }
/*     */         }
/*     */ 
/* 419 */         gstEl.getPoints().add(clickPt);
/*     */       }
/*     */ 
/* 422 */       return gstEl;
/*     */     }
/*     */ 
/*     */     private String getPgenCategory(DrawableElement elSel, AbstractDrawableComponent adc)
/*     */     {
/* 433 */       String cat = null;
/*     */ 
/* 435 */       if (((adc instanceof Jet)) && (adc.getPrimaryDE() == elSel)) {
/* 436 */         cat = adc.getName();
/*     */       }
/* 439 */       else if (elSel != null) cat = elSel.getPgenCategory();
/*     */ 
/* 442 */       return cat;
/*     */     }
/*     */ 
/*     */     private String getPgenType(DrawableElement elSel, AbstractDrawableComponent adc)
/*     */     {
/* 453 */       String type = null;
/*     */ 
/* 455 */       if (((adc instanceof Jet)) && (adc.getPrimaryDE() == elSel)) {
/* 456 */         type = adc.getName();
/*     */       }
/* 459 */       else if (elSel != null) type = elSel.getPgenType();
/*     */ 
/* 462 */       return type;
/*     */     }
/*     */ 
/*     */     private Jet connectJet(Jet fstJet, Jet sedJet)
/*     */     {
/* 475 */       Jet mpe = fstJet.copy();
/*     */ 
/* 478 */       mpe.getJetLine().getPoints().clear();
/*     */ 
/* 480 */       mpe.getJetLine().getPoints().addAll(this.ghostEl.getPoints());
/*     */ 
/* 483 */       Iterator it = sedJet.getComponentIterator();
/*     */ 
/* 485 */       while (it.hasNext()) {
/* 486 */         AbstractDrawableComponent de = ((AbstractDrawableComponent)it.next()).copy();
/* 487 */         if (de.getName().equalsIgnoreCase("windInfo"))
/*     */         {
/* 491 */           mpe.addBarb((DECollection)de);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 497 */       mpe.getSnapTool().snapJet(mpe);
/*     */ 
/* 499 */       return mpe;
/*     */     }
/*     */ 
/*     */     private boolean selectableContourLine(MultiPointElement el2bSelected, MultiPointElement elSelected)
/*     */     {
/*     */       boolean selectable;
/*     */       boolean selectable;
/* 517 */       if ((!(el2bSelected.getParent() instanceof ContourLine)) && 
/* 518 */         (!(elSelected.getParent() instanceof ContourLine))) {
/* 519 */         selectable = true;
/*     */       }
/*     */       else
/*     */       {
/* 523 */         selectable = false;
/*     */ 
/* 525 */         if (((el2bSelected.getParent() instanceof ContourLine)) && 
/* 526 */           ((elSelected.getParent() instanceof ContourLine)))
/*     */         {
/* 528 */           if (el2bSelected.getParent().getParent().equals(
/* 528 */             elSelected.getParent().getParent()))
/*     */           {
/* 530 */             String[] label1 = ((ContourLine)el2bSelected.getParent()).getLabelString();
/* 531 */             String[] label2 = ((ContourLine)elSelected.getParent()).getLabelString();
/*     */ 
/* 533 */             selectable = true;
/*     */ 
/* 535 */             if (label1.length != label2.length) {
/* 536 */               selectable = false;
/*     */             }
/*     */             else {
/* 539 */               for (int ii = 0; ii < label1.length; ii++) {
/* 540 */                 if (!label1[ii].equals(label2[ii])) {
/* 541 */                   selectable = false;
/* 542 */                   break;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 551 */       return selectable;
/*     */     }
/*     */ 
/*     */     private Contours connectContourLine(MultiPointElement cline1, MultiPointElement cline2)
/*     */     {
/* 562 */       Contours newContours = new Contours();
/* 563 */       Contours oldContours = (Contours)cline1.getParent().getParent();
/*     */ 
/* 565 */       Iterator iterator = oldContours.getComponentIterator();
/*     */ 
/* 567 */       boolean connected = false;
/*     */ 
/* 569 */       while (iterator.hasNext())
/*     */       {
/* 571 */         AbstractDrawableComponent oldContourComp = (AbstractDrawableComponent)iterator.next();
/* 572 */         AbstractDrawableComponent newContourComp = oldContourComp.copy();
/*     */ 
/* 574 */         if ((oldContourComp instanceof ContourLine))
/*     */         {
/* 576 */           Line oldLine = ((ContourLine)oldContourComp).getLine();
/*     */ 
/* 578 */           if ((oldLine.equals((Line)cline1)) || (oldLine.equals(cline2)))
/*     */           {
/* 580 */             if (!connected)
/*     */             {
/* 582 */               Line ln = ((ContourLine)newContourComp).getLine();
/* 583 */               ln.getPoints().clear();
/* 584 */               ln.getPoints().addAll(this.ghostEl.getPoints());
/*     */ 
/* 586 */               newContourComp.setParent(newContours);
/* 587 */               newContours.add(newContourComp);
/*     */ 
/* 589 */               connected = true;
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 595 */             newContourComp.setParent(newContours);
/* 596 */             newContours.add(newContourComp);
/*     */           }
/*     */         }
/*     */         else {
/* 600 */           newContourComp.setParent(newContours);
/* 601 */           newContours.add(newContourComp);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 606 */       newContours.update(oldContours);
/*     */ 
/* 608 */       return newContours;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenConnectTool
 * JD-Core Version:    0.6.2
 */