/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.CoordinateList;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.linearref.LinearLocation;
/*     */ import com.vividsolutions.jts.linearref.LocationIndexedLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
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
/*     */ import java.util.List;
/*     */ 
/*     */ public class PgenAddPointHandler extends InputHandlerDefaultImpl
/*     */ {
/*     */   protected AbstractEditor mapEditor;
/*     */   protected PgenResource pgenrsc;
/*     */   protected AttrDlg attrDlg;
/*     */   protected AbstractPgenTool tool;
/*     */   private boolean preempt;
/*  64 */   private ADD_STATUS status = ADD_STATUS.START;
/*     */   private DrawableElement newEl;
/*  66 */   private DECollection ghost = new DECollection();
/*  67 */   Color ghostColor = new Color(255, 255, 255);
/*  68 */   private Symbol newDot = new Symbol(null, new Color[] { new Color(255, 0, 0) }, 2.5F, 7.5D, 
/*  69 */     Boolean.valueOf(false), null, "Marker", "DOT");
/*     */ 
/*  71 */   OperationFilter addPointFilter = new OperationFilter(Operation.ADD_POINT);
/*     */ 
/*     */   public PgenAddPointHandler(AbstractPgenTool tool)
/*     */   {
/*  78 */     this.mapEditor = tool.mapEditor;
/*  79 */     this.pgenrsc = tool.getDrawingLayer();
/*  80 */     this.tool = tool;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/*  92 */     if (!this.tool.isResourceEditable()) return false;
/*     */ 
/*  94 */     this.preempt = false;
/*     */ 
/*  97 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/*  98 */     if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 100 */     if (button == 1)
/*     */     {
/* 102 */       switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenAddPointHandler$ADD_STATUS()[this.status.ordinal()])
/*     */       {
/*     */       case 1:
/* 110 */         AbstractDrawableComponent elSelected = this.pgenrsc.getNearestElement(loc, this.addPointFilter);
/*     */ 
/* 112 */         if (elSelected == null) return false;
/* 113 */         this.pgenrsc.setSelected(elSelected);
/* 114 */         this.status = ADD_STATUS.SELECTED;
/* 115 */         this.preempt = true;
/* 116 */         break;
/*     */       case 2:
/* 122 */         this.pgenrsc.removeGhostLine();
/* 123 */         AbstractDrawableComponent newComp = addPointToElement(loc, this.pgenrsc.getSelectedComp());
/* 124 */         if ((newComp instanceof Gfa)) {
/* 125 */           if (((Gfa)newComp).getGfaFcstHr().indexOf("-") > -1)
/*     */           {
/* 127 */             ((Gfa)newComp).snap();
/* 128 */             GfaReducePoint.WarningForOverThreeLines((Gfa)newComp);
/*     */           }
/*     */ 
/* 131 */           ((Gfa)newComp).setGfaVorText(Gfa.buildVorText((Gfa)newComp));
/*     */         }
/*     */ 
/* 134 */         this.pgenrsc.replaceElement(this.pgenrsc.getSelectedComp(), newComp);
/*     */ 
/* 137 */         if ((this.tool instanceof PgenAddPoint)) {
/* 138 */           this.pgenrsc.removeSelected();
/* 139 */           this.status = ADD_STATUS.START;
/*     */         }
/*     */         else {
/* 142 */           this.tool.resetMouseHandler();
/* 143 */           this.pgenrsc.removeSelected();
/* 144 */           this.pgenrsc.setSelected(this.pgenrsc.getNearestElement(loc));
/* 145 */           this.tool.setWorkingComponent(this.pgenrsc.getNearestComponent(loc));
/*     */         }
/*     */ 
/* 148 */         this.preempt = false;
/*     */       }
/*     */ 
/* 153 */       this.mapEditor.refresh();
/* 154 */       return this.preempt;
/*     */     }
/*     */ 
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseUp(int x, int y, int button)
/*     */   {
/* 173 */     if (!this.tool.isResourceEditable()) return false;
/* 174 */     if (button == 3)
/*     */     {
/* 176 */       switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenAddPointHandler$ADD_STATUS()[this.status.ordinal()])
/*     */       {
/*     */       case 2:
/* 179 */         if ((this.tool instanceof PgenAddPoint))
/*     */         {
/* 184 */           this.pgenrsc.removeGhostLine();
/* 185 */           this.pgenrsc.removeSelected();
/* 186 */           this.status = ADD_STATUS.START;
/* 187 */           this.mapEditor.refresh();
/*     */         }
/*     */         else {
/* 190 */           this.pgenrsc.removeGhostLine();
/* 191 */           this.tool.resetMouseHandler();
/*     */         }
/*     */ 
/* 194 */         break;
/*     */       default:
/* 196 */         this.tool.resetMouseHandler();
/*     */       }
/*     */     }
/*     */ 
/* 200 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseMove(int anX, int aY)
/*     */   {
/* 209 */     if (!this.tool.isResourceEditable()) return false;
/*     */ 
/* 211 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/* 212 */     if (loc == null) return false;
/*     */ 
/* 214 */     if (this.pgenrsc.getSelectedDE() != null) this.status = ADD_STATUS.SELECTED;
/*     */ 
/* 216 */     switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenAddPointHandler$ADD_STATUS()[this.status.ordinal()])
/*     */     {
/*     */     case 2:
/* 222 */       this.ghost.clear();
/* 223 */       this.newDot.setLocation(loc);
/*     */ 
/* 225 */       this.newEl = addPointToElement(loc, this.pgenrsc.getSelectedDE());
/* 226 */       this.newEl.setColors(new Color[] { this.ghostColor, this.ghostColor });
/* 227 */       this.ghost.add(this.newEl);
/* 228 */       this.ghost.add(this.newDot);
/* 229 */       this.pgenrsc.setGhostLine(this.ghost);
/* 230 */       this.mapEditor.refresh();
/*     */     }
/*     */ 
/* 234 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */   {
/* 239 */     if ((this.shiftDown) || (!this.tool.isResourceEditable())) return false;
/* 240 */     return this.preempt;
/*     */   }
/*     */ 
/*     */   public DrawableElement addPointToElement(Coordinate point, DrawableElement selectedDE)
/*     */   {
/* 249 */     DrawableElement newEl = (DrawableElement)selectedDE.copy();
/*     */ 
/* 251 */     ArrayList pts = calculateNewPoints(point, selectedDE.getPoints());
/*     */ 
/* 253 */     newEl.setPoints(pts);
/* 254 */     return newEl;
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent addPointToElement(Coordinate point, AbstractDrawableComponent elem)
/*     */   {
/* 264 */     if ((elem instanceof DrawableElement)) {
/* 265 */       DrawableElement de = (DrawableElement)elem;
/* 266 */       return addPointToElement(point, de);
/*     */     }
/* 268 */     if ((elem instanceof Jet)) {
/* 269 */       Jet newEl = ((Jet)elem).copy();
/*     */ 
/* 271 */       ArrayList pts = calculateNewPoints(point, elem.getPrimaryDE().getPoints());
/*     */ 
/* 273 */       newEl.getPrimaryDE().setPoints(pts);
/* 274 */       return newEl;
/*     */     }
/* 276 */     return elem;
/*     */   }
/*     */ 
/*     */   private ArrayList<Coordinate> calculateNewPoints(Coordinate point, List<Coordinate> vertices)
/*     */   {
/* 286 */     GeometryFactory gf = new GeometryFactory();
/*     */ 
/* 291 */     LineString ls = gf.createLineString((Coordinate[])vertices.toArray(new Coordinate[0]));
/* 292 */     LocationIndexedLine lil = new LocationIndexedLine(ls);
/* 293 */     LinearLocation loc = lil.project(point);
/*     */     CoordinateList coords;
/* 295 */     if (loc.compareTo(lil.getStartIndex()) == 0)
/*     */     {
/* 299 */       CoordinateList coords = new CoordinateList(new Coordinate[] { point }, false);
/* 300 */       coords.add(ls.getCoordinates(), false);
/*     */     }
/* 302 */     else if (loc.getSegmentIndex() == lil.getEndIndex().getSegmentIndex())
/*     */     {
/* 306 */       CoordinateList coords = new CoordinateList(ls.getCoordinates(), false);
/* 307 */       coords.add(new Coordinate[] { point }, false);
/*     */     }
/*     */     else
/*     */     {
/* 313 */       LinearLocation previous = new LinearLocation(loc.getComponentIndex(), loc.getSegmentIndex(), 0.0D);
/* 314 */       LinearLocation next = new LinearLocation(loc.getComponentIndex(), loc.getSegmentIndex() + 1, 0.0D);
/* 315 */       Geometry g1 = lil.extractLine(lil.getStartIndex(), previous);
/* 316 */       Geometry g2 = lil.extractLine(next, lil.getEndIndex());
/*     */ 
/* 318 */       coords = new CoordinateList(g1.getCoordinates(), false);
/* 319 */       coords.add(point, false);
/* 320 */       coords.add(g2.getCoordinates(), false);
/*     */     }
/*     */ 
/* 323 */     ArrayList pts = new ArrayList();
/* 324 */     for (int i = 0; i < coords.size(); i++) pts.add(coords.getCoordinate(i));
/*     */ 
/* 326 */     return pts;
/*     */   }
/*     */ 
/*     */   public AbstractEditor getMapEditor() {
/* 330 */     return this.mapEditor;
/*     */   }
/*     */ 
/*     */   public PgenResource getPgenrsc() {
/* 334 */     return this.pgenrsc;
/*     */   }
/*     */ 
/*     */   private static enum ADD_STATUS
/*     */   {
/*  56 */     START, SELECTED;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenAddPointHandler
 * JD-Core Version:    0.6.2
 */