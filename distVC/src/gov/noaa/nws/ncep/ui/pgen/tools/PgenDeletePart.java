/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.linearref.LinearLocation;
/*     */ import com.vividsolutions.jts.linearref.LocationIndexedLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.CurveFitter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IMultiPoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ 
/*     */ public class PgenDeletePart extends PgenSelectingTool
/*     */ {
/*  63 */   protected IInputHandler delPartHandler = null;
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/*  80 */     if (this.delPartHandler != null) ((PgenDelPartHandler)this.delPartHandler).cleanup();
/*     */ 
/*  82 */     super.deactivateTool();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  92 */     if (this.delPartHandler == null)
/*     */     {
/*  94 */       this.delPartHandler = new PgenDelPartHandler(this, this.mapEditor, this.drawingLayer, this.attrDlg);
/*     */     }
/*     */ 
/*  98 */     return this.delPartHandler;
/*     */   }
/*     */ 
/*     */   public class PgenDelPartHandler extends PgenSelectHandler
/*     */   {
/* 109 */     private Symbol DOT = new Symbol(null, new Color[] { Color.RED }, 1.0F, 7.5D, Boolean.valueOf(false), null, "Marker", "DOT");
/* 110 */     OperationFilter delPartFilter = new OperationFilter(Operation.DELETE_PART);
/*     */ 
/* 115 */     double TOL = 10.0D;
/* 116 */     int pt1Index = 0;
/* 117 */     int pt2Index = 0;
/* 118 */     Coordinate point1 = null;
/* 119 */     Coordinate point2 = null;
/* 120 */     LocationIndexedLine lil = null;
/* 121 */     GeometryFactory gf = new GeometryFactory();
/*     */ 
/*     */     public PgenDelPartHandler(AbstractPgenTool tool, AbstractEditor mapEditor, PgenResource resource, AttrDlg attrDlg)
/*     */     {
/* 132 */       super(mapEditor, resource, attrDlg);
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 143 */       if (!PgenDeletePart.this.isResourceEditable()) return false;
/*     */ 
/* 146 */       Coordinate loc = this.mapEditor.translateClick(anX, aY);
/* 147 */       if (loc == null) return false;
/* 148 */       Coordinate screenPt = new Coordinate(anX, aY);
/*     */ 
/* 150 */       if (button == 1)
/*     */       {
/* 152 */         if (PgenDeletePart.this.drawingLayer.getSelectedDE() == null)
/*     */         {
/* 155 */           DrawableElement elSelected = PgenDeletePart.this.drawingLayer.getNearestElement(loc, this.delPartFilter);
/* 156 */           if (((elSelected instanceof ILine)) && 
/* 157 */             (!(elSelected instanceof WatchBox)))
/*     */           {
/* 159 */             PgenDeletePart.this.drawingLayer.setSelected(elSelected);
/*     */           }
/*     */           else
/*     */           {
/* 170 */             return false;
/*     */           }
/*     */         }
/* 173 */         else if (!this.ptSelected)
/*     */         {
/* 178 */           this.lil = getLil(loc, this.gf, PgenDeletePart.this.drawingLayer.getSelectedDE());
/* 179 */           if (this.lil == null) {
/* 180 */             this.point2 = null;
/* 181 */             return false;
/*     */           }
/*     */ 
/* 187 */           LinearLocation linloc1 = this.lil.project(screenPt);
/* 188 */           Coordinate screen1 = this.lil.extractPoint(linloc1);
/* 189 */           if (screenPt.distance(screen1) > this.TOL) {
/* 190 */             this.point1 = null;
/* 191 */             return false;
/*     */           }
/* 193 */           this.point1 = this.mapEditor.translateClick(screen1.x, screen1.y);
/* 194 */           this.DOT.setLocation(this.point1);
/* 195 */           PgenDeletePart.this.drawingLayer.setGhostLine(this.DOT);
/* 196 */           this.ptSelected = true;
/*     */         }
/*     */         else
/*     */         {
/* 202 */           Line des = (Line)PgenDeletePart.this.drawingLayer.getSelectedDE();
/*     */ 
/* 205 */           this.lil = getLil(loc, this.gf, des);
/* 206 */           if (this.lil == null) {
/* 207 */             this.point2 = null;
/* 208 */             return false;
/*     */           }
/*     */ 
/* 215 */           LinearLocation linloc2 = this.lil.project(screenPt);
/* 216 */           Coordinate screen2 = this.lil.extractPoint(linloc2);
/* 217 */           if (screenPt.distance(screen2) > this.TOL) {
/* 218 */             this.point2 = null;
/* 219 */             return false;
/*     */           }
/* 221 */           this.point2 = this.mapEditor.translateClick(screen2.x, screen2.y);
/*     */ 
/* 223 */           if ((des.getParent() instanceof ContourLine)) {
/* 224 */             Contours oldContours = (Contours)des.getParent().getParent();
/* 225 */             Contours newContours = oldContours.split((ContourLine)des.getParent(), this.point1, this.point2);
/*     */ 
/* 227 */             PgenDeletePart.this.drawingLayer.replaceElement(oldContours, newContours);
/*     */           }
/*     */           else {
/* 230 */             PgenDeletePart.this.drawingLayer.deleteElementPart(des, this.point1, this.point2);
/*     */           }
/*     */ 
/* 233 */           PgenDeletePart.this.drawingLayer.removeGhostLine();
/* 234 */           PgenDeletePart.this.drawingLayer.removeSelected();
/* 235 */           this.lil = null;
/* 236 */           this.ptSelected = false;
/*     */         }
/*     */ 
/* 240 */         this.mapEditor.refresh();
/* 241 */         return true;
/*     */       }
/*     */ 
/* 244 */       if (button == 3)
/*     */       {
/* 247 */         this.ptSelected = false;
/* 248 */         PgenDeletePart.this.drawingLayer.removeGhostLine();
/* 249 */         PgenDeletePart.this.drawingLayer.removeSelected();
/* 250 */         this.lil = null;
/* 251 */         this.mapEditor.refresh();
/* 252 */         PgenUtil.setSelectingMode();
/*     */ 
/* 254 */         return true;
/*     */       }
/*     */ 
/* 259 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int anX, int aY, int button)
/*     */     {
/* 271 */       if ((!PgenDeletePart.this.isResourceEditable()) || (this.shiftDown)) {
/* 272 */         return false;
/*     */       }
/*     */ 
/* 275 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int x, int y, int button)
/*     */     {
/* 284 */       return false;
/*     */     }
/*     */ 
/*     */     private Coordinate[] preprocessLine(int smoothFactor, boolean isClosed, Coordinate[] linePoints)
/*     */     {
/* 295 */       int num = linePoints.length;
/* 296 */       if (isClosed) num++;
/* 297 */       double[][] dpts = new double[num][3];
/*     */ 
/* 299 */       for (int k = 0; k < linePoints.length; k++) {
/* 300 */         dpts[k] = this.mapEditor.translateInverseClick(linePoints[k]);
/*     */       }
/*     */ 
/* 303 */       if (isClosed) dpts[(num - 1)] = dpts[0];
/*     */       double[][] newpts;
/*     */       double[][] newpts;
/* 305 */       if (smoothFactor != 0) {
/* 306 */         newpts = CurveFitter.fitParametricCurve(dpts, 10.0F);
/*     */       }
/*     */       else {
/* 309 */         newpts = dpts;
/*     */       }
/*     */ 
/* 312 */       Coordinate[] coords = new Coordinate[newpts.length];
/*     */ 
/* 314 */       for (int k = 0; k < newpts.length; k++) {
/* 315 */         coords[k] = new Coordinate(newpts[k][0], newpts[k][1]);
/*     */       }
/*     */ 
/* 318 */       return coords;
/*     */     }
/*     */ 
/*     */     private void cleanup()
/*     */     {
/* 323 */       this.ptSelected = false;
/* 324 */       PgenDeletePart.this.drawingLayer.removeSelected();
/*     */     }
/*     */ 
/*     */     private LocationIndexedLine getLil(Coordinate loc, GeometryFactory gf, DrawableElement elem)
/*     */     {
/* 332 */       LocationIndexedLine locIL = null;
/* 333 */       if (((elem instanceof ILine)) && (!(elem instanceof WatchBox)))
/*     */       {
/* 337 */         Coordinate[] coords = preprocessLine(((ILine)elem).getSmoothFactor(), 
/* 338 */           ((ILine)elem).isClosedLine().booleanValue(), ((IMultiPoint)elem).getLinePoints());
/* 339 */         LineString ls = gf.createLineString(coords);
/* 340 */         locIL = new LocationIndexedLine(ls);
/*     */       }
/*     */ 
/* 343 */       return locIL;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenDeletePart
 * JD-Core Version:    0.6.2
 */