/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.LineSegment;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaReducePoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class PgenAddPointAltHandler extends InputHandlerDefaultImpl
/*     */ {
/*     */   protected AbstractEditor mapEditor;
/*     */   protected PgenResource pgenrsc;
/*     */   protected AttrDlg attrDlg;
/*     */   protected AbstractPgenTool tool;
/*     */   private boolean preempt;
/*  64 */   private ADD_STATUS status = ADD_STATUS.START;
/*     */   private DrawableElement newEl;
/*  66 */   Map<Coordinate, Integer> newPoints = null;
/*  67 */   private Integer index = null;
/*  68 */   private DECollection ghost = new DECollection();
/*  69 */   Color ghostColor = new Color(255, 255, 255);
/*  70 */   private Symbol marker = new Symbol(null, new Color[] { new Color(255, 0, 0) }, 2.5F, 1.0D, 
/*  71 */     Boolean.valueOf(false), null, "Marker", "BOX");
/*     */ 
/*  73 */   OperationFilter addPointFilter = new OperationFilter(Operation.ADD_POINT);
/*     */ 
/*     */   public PgenAddPointAltHandler(AbstractPgenTool tool)
/*     */   {
/*  81 */     this.mapEditor = tool.mapEditor;
/*  82 */     this.pgenrsc = tool.getDrawingLayer();
/*  83 */     this.tool = tool;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/*  95 */     if (!this.tool.isResourceEditable()) return false;
/*     */ 
/*  97 */     this.preempt = false;
/*     */ 
/* 100 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/* 101 */     if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 103 */     if (button == 1)
/*     */     {
/* 105 */       switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenAddPointAltHandler$ADD_STATUS()[this.status.ordinal()])
/*     */       {
/*     */       case 1:
/* 112 */         AbstractDrawableComponent elSelected = this.pgenrsc.getNearestElement(loc, this.addPointFilter);
/*     */ 
/* 114 */         if (elSelected == null) return false;
/* 115 */         this.pgenrsc.setSelected(elSelected);
/*     */ 
/* 120 */         this.newPoints = calculateNewPoints(this.pgenrsc.getSelectedDE());
/* 121 */         this.ghost.clear();
/* 122 */         for (Coordinate coord : this.newPoints.keySet()) {
/* 123 */           Symbol sym = new Symbol(null, new Color[] { new Color(255, 0, 0) }, 2.5F, 1.0D, 
/* 124 */             Boolean.valueOf(false), coord, "Marker", "BOX");
/* 125 */           this.ghost.add(sym);
/*     */         }
/* 127 */         this.pgenrsc.setGhostLine(this.ghost);
/*     */ 
/* 129 */         this.status = ADD_STATUS.SELECTED;
/* 130 */         break;
/*     */       case 2:
/* 136 */         this.index = null;
/* 137 */         if (this.newPoints != null) {
/* 138 */           for (Coordinate coord : this.newPoints.keySet()) {
/* 139 */             if (loc.distance(coord) < 0.2D) {
/* 140 */               this.index = ((Integer)this.newPoints.get(coord));
/* 141 */               this.status = ADD_STATUS.MOVING;
/* 142 */               this.preempt = true;
/* 143 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         break;
/*     */       }
/*     */ 
/* 150 */       this.mapEditor.refresh();
/* 151 */       return this.preempt;
/*     */     }
/*     */ 
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int anX, int aY, int button)
/*     */   {
/* 169 */     if ((!this.tool.isResourceEditable()) || (button != 1) || (this.shiftDown)) return false;
/*     */ 
/* 171 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/* 172 */     if (loc == null) return false;
/*     */ 
/* 174 */     switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenAddPointAltHandler$ADD_STATUS()[this.status.ordinal()])
/*     */     {
/*     */     case 3:
/* 180 */       this.ghost.clear();
/* 181 */       this.marker.setLocation(loc);
/* 182 */       this.ghost.add(this.marker);
/*     */ 
/* 187 */       this.newEl = addPointToElement(loc, this.index, this.pgenrsc.getSelectedDE());
/* 188 */       this.newEl.setColors(new Color[] { this.ghostColor, this.ghostColor });
/* 189 */       this.ghost.add(this.newEl);
/* 190 */       this.pgenrsc.setGhostLine(this.ghost);
/*     */     }
/*     */ 
/* 194 */     this.mapEditor.refresh();
/* 195 */     return this.preempt;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseUp(int anX, int aY, int button)
/*     */   {
/* 204 */     if (!this.tool.isResourceEditable()) return false;
/*     */ 
/* 206 */     if (button == 1) {
/* 207 */       Coordinate loc = this.mapEditor.translateClick(anX, aY);
/* 208 */       if (loc == null) return false;
/*     */ 
/* 210 */       switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenAddPointAltHandler$ADD_STATUS()[this.status.ordinal()])
/*     */       {
/*     */       case 3:
/* 217 */         this.pgenrsc.removeGhostLine();
/* 218 */         AbstractDrawableComponent newComp = addPointToElement(loc, this.index, this.pgenrsc.getSelectedComp());
/* 219 */         this.pgenrsc.replaceElement(this.pgenrsc.getSelectedComp(), newComp);
/*     */ 
/* 221 */         if ((newComp instanceof Gfa)) {
/* 222 */           if (((Gfa)newComp).getGfaFcstHr().indexOf("-") > -1)
/*     */           {
/* 224 */             ((Gfa)newComp).snap();
/* 225 */             GfaReducePoint.WarningForOverThreeLines((Gfa)newComp);
/*     */           }
/*     */ 
/* 228 */           ((Gfa)newComp).setGfaVorText(Gfa.buildVorText((Gfa)newComp));
/*     */         }
/*     */ 
/* 231 */         if ((this.tool instanceof PgenAddPointAlt)) {
/* 232 */           this.pgenrsc.removeSelected();
/* 233 */           this.status = ADD_STATUS.START;
/*     */         }
/*     */         else {
/* 236 */           this.tool.resetMouseHandler();
/* 237 */           this.pgenrsc.removeSelected();
/* 238 */           this.pgenrsc.setSelected(this.pgenrsc.getNearestElement(loc));
/* 239 */           this.tool.setWorkingComponent(this.pgenrsc.getNearestComponent(loc));
/*     */         }
/*     */ 
/* 242 */         this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 246 */       return false;
/*     */     }
/* 248 */     if (button == 3)
/*     */     {
/* 250 */       switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenAddPointAltHandler$ADD_STATUS()[this.status.ordinal()])
/*     */       {
/*     */       case 2:
/* 254 */         if ((this.tool instanceof PgenAddPointAlt))
/*     */         {
/* 258 */           this.pgenrsc.removeGhostLine();
/* 259 */           this.pgenrsc.removeSelected();
/* 260 */           this.status = ADD_STATUS.START;
/* 261 */           this.mapEditor.refresh();
/*     */         }
/*     */         else {
/* 264 */           this.pgenrsc.removeGhostLine();
/* 265 */           this.tool.resetMouseHandler();
/*     */         }
/*     */ 
/* 268 */         break;
/*     */       default:
/* 270 */         PgenUtil.setSelectingMode();
/*     */       }
/*     */ 
/* 273 */       return true;
/*     */     }
/*     */ 
/* 277 */     return false;
/*     */   }
/*     */ 
/*     */   private DrawableElement addPointToElement(Coordinate point, Integer index, DrawableElement selectedDE)
/*     */   {
/* 287 */     DrawableElement newEl = (DrawableElement)selectedDE.copy();
/*     */ 
/* 289 */     ArrayList pts = new ArrayList(selectedDE.getPoints());
/* 290 */     pts.add(index.intValue(), point);
/*     */ 
/* 292 */     newEl.setPoints(pts);
/* 293 */     return newEl;
/*     */   }
/*     */ 
/*     */   private AbstractDrawableComponent addPointToElement(Coordinate point, Integer index, AbstractDrawableComponent elem)
/*     */   {
/* 303 */     if ((elem instanceof DrawableElement)) {
/* 304 */       DrawableElement de = (DrawableElement)elem;
/* 305 */       return addPointToElement(point, index, de);
/*     */     }
/* 307 */     if ((elem instanceof Jet)) {
/* 308 */       Jet newEl = ((Jet)elem).copy();
/*     */ 
/* 310 */       ArrayList pts = new ArrayList(elem.getPrimaryDE().getPoints());
/* 311 */       pts.add(index.intValue(), point);
/*     */ 
/* 313 */       newEl.getPrimaryDE().setPoints(pts);
/* 314 */       return newEl;
/*     */     }
/* 316 */     return elem;
/*     */   }
/*     */ 
/*     */   private Map<Coordinate, Integer> calculateNewPoints(DrawableElement elSelected)
/*     */   {
/* 327 */     Map newLocs = new HashMap();
/* 328 */     List points = new ArrayList();
/*     */ 
/* 333 */     for (Coordinate c : elSelected.getPoints()) {
/* 334 */       double[] tmp = this.mapEditor.translateInverseClick(c);
/* 335 */       points.add(new Coordinate(tmp[0], tmp[1]));
/*     */     }
/*     */ 
/* 341 */     for (int i = 0; i < elSelected.getPoints().size() - 1; i++) {
/* 342 */       LineSegment ls = new LineSegment((Coordinate)points.get(i), (Coordinate)points.get(i + 1));
/* 343 */       newLocs.put(toLatLon(ls.midPoint()), Integer.valueOf(i + 1));
/*     */     }
/*     */ 
/* 346 */     if (!((ILine)elSelected).isClosedLine().booleanValue())
/*     */     {
/* 349 */       Coordinate prev = (Coordinate)points.get(0);
/* 350 */       newLocs.put(toLatLon(prev), Integer.valueOf(0));
/*     */ 
/* 354 */       Coordinate next = (Coordinate)points.get(points.size() - 1);
/* 355 */       newLocs.put(toLatLon(next), Integer.valueOf(points.size()));
/*     */     }
/*     */     else {
/* 358 */       LineSegment ls = new LineSegment((Coordinate)points.get(0), (Coordinate)points.get(points.size() - 1));
/* 359 */       newLocs.put(toLatLon(ls.midPoint()), Integer.valueOf(points.size()));
/*     */     }
/*     */ 
/* 362 */     return newLocs;
/*     */   }
/*     */ 
/*     */   private Coordinate toLatLon(Coordinate pixel)
/*     */   {
/* 370 */     return this.mapEditor.translateClick(pixel.x, pixel.y);
/*     */   }
/*     */ 
/*     */   public void preprocess()
/*     */   {
/* 375 */     this.newPoints = calculateNewPoints(this.pgenrsc.getSelectedDE());
/* 376 */     this.ghost.clear();
/* 377 */     for (Coordinate coord : this.newPoints.keySet()) {
/* 378 */       Symbol sym = new Symbol(null, new Color[] { new Color(255, 0, 0) }, 2.5F, 1.0D, 
/* 379 */         Boolean.valueOf(false), coord, "Marker", "BOX");
/* 380 */       this.ghost.add(sym);
/*     */     }
/* 382 */     this.pgenrsc.setGhostLine(this.ghost);
/*     */ 
/* 384 */     this.status = ADD_STATUS.SELECTED;
/*     */   }
/*     */ 
/*     */   public AbstractEditor getMapEditor()
/*     */   {
/* 389 */     return this.mapEditor;
/*     */   }
/*     */ 
/*     */   public PgenResource getPgenrsc()
/*     */   {
/* 394 */     return this.pgenrsc;
/*     */   }
/*     */ 
/*     */   private static enum ADD_STATUS
/*     */   {
/*  56 */     START, SELECTED, MOVING;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenAddPointAltHandler
 * JD-Core Version:    0.6.2
 */