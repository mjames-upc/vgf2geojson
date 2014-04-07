/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.map.MapDescriptor;
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TrackExtrapPointInfoDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ISinglePoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Vector;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ 
/*     */ public class PgenRotateElement extends AbstractPgenDrawingTool
/*     */ {
/*     */   protected void activateTool()
/*     */   {
/*  53 */     this.attrDlg = null;
/*  54 */     if (this.buttonName == null) {
/*  55 */       this.buttonName = new String("Select");
/*     */     }
/*     */ 
/*  58 */     super.activateTool();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  68 */     if (this.mouseHandler == null) {
/*  69 */       this.mouseHandler = new PgenSelectRotateHandler();
/*     */     }
/*  71 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public class PgenSelectRotateHandler extends InputHandlerDefaultImpl
/*     */   {
/*  82 */     OperationFilter rotateFilter = new OperationFilter(Operation.ROTATE);
/*     */ 
/*  85 */     TrackExtrapPointInfoDlg trackExtrapPointInfoDlg = null;
/*     */ 
/*  88 */     protected boolean ptSelected = false;
/*     */ 
/*  91 */     private Double oldDir = null;
/*     */ 
/*     */     public PgenSelectRotateHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int x, int y, int button)
/*     */     {
/* 101 */       if (!PgenRotateElement.this.isResourceEditable()) return false;
/*     */ 
/* 104 */       Coordinate loc = PgenRotateElement.this.mapEditor.translateClick(x, y);
/* 105 */       if ((loc == null) || (this.shiftDown)) {
/* 106 */         return false;
/*     */       }
/*     */ 
/* 109 */       if (button == 1)
/*     */       {
/* 112 */         if ((this.ptSelected) || (PgenRotateElement.this.drawingLayer.getSelectedDE() != null)) {
/* 113 */           return true;
/*     */         }
/*     */ 
/* 117 */         DrawableElement elSelected = PgenRotateElement.this.drawingLayer.getNearestElement(loc, this.rotateFilter);
/*     */ 
/* 127 */         if (elSelected != null) PgenRotateElement.this.drawingLayer.setSelected(elSelected);
/*     */ 
/* 129 */         PgenRotateElement.this.mapEditor.refresh();
/* 130 */         return false;
/*     */       }
/* 132 */       if (button == 3)
/*     */       {
/* 134 */         if (this.trackExtrapPointInfoDlg != null) {
/* 135 */           this.trackExtrapPointInfoDlg.close();
/* 136 */           this.trackExtrapPointInfoDlg = null;
/*     */         }
/*     */ 
/* 139 */         PgenRotateElement.this.drawingLayer.removeGhostLine();
/* 140 */         this.ptSelected = false;
/* 141 */         PgenRotateElement.this.drawingLayer.removeSelected();
/* 142 */         PgenRotateElement.this.mapEditor.refresh();
/*     */ 
/* 144 */         return false;
/*     */       }
/*     */ 
/* 147 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int button)
/*     */     {
/* 158 */       if (!PgenRotateElement.this.isResourceEditable()) return false;
/*     */ 
/* 161 */       Coordinate loc = PgenRotateElement.this.mapEditor.translateClick(x, y);
/* 162 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 164 */       DrawableElement el = PgenRotateElement.this.drawingLayer.getSelectedDE();
/*     */ 
/* 168 */       if (el != null) {
/* 169 */         Coordinate origin = ((ISinglePoint)el).getLocation();
/*     */ 
/* 171 */         if ((el instanceof Vector)) {
/* 172 */           if (this.oldDir == null) {
/* 173 */             this.oldDir = Double.valueOf(((Vector)el).getDirection());
/*     */           }
/* 175 */           double[] swtCoordinates = PgenRotateElement.this.mapEditor.translateInverseClick(origin);
/* 176 */           Double newDir = Double.valueOf(PgenToolUtils.calculateAngle(this.oldDir.doubleValue(), swtCoordinates[0], swtCoordinates[1], x, y));
/* 177 */           if ("Hash".equals(((Vector)el).getPgenType())) {
/* 178 */             newDir = Double.valueOf(PgenToolUtils.transformToRange0To360(180.0D - newDir.doubleValue()));
/* 179 */             newDir = Double.valueOf(newDir.doubleValue() - southOffsetAngle(origin));
/*     */           }
/*     */           else {
/* 182 */             newDir = Double.valueOf(newDir.doubleValue() + southOffsetAngle(origin));
/*     */           }
/* 184 */           ((Vector)el).setDirection(newDir.doubleValue());
/*     */         }
/* 186 */         else if ((el instanceof Text)) {
/* 187 */           if (this.oldDir == null) {
/* 188 */             this.oldDir = Double.valueOf(((Text)el).getRotation());
/*     */           }
/* 190 */           double[] swtCoordinates = PgenRotateElement.this.mapEditor.translateInverseClick(origin);
/* 191 */           Double newRotation = Double.valueOf(180.0D - PgenToolUtils.calculateAngle(this.oldDir.doubleValue(), swtCoordinates[0], swtCoordinates[1], x, y));
/* 192 */           newRotation = Double.valueOf(PgenToolUtils.transformToRange0To360(newRotation.doubleValue()));
/* 193 */           if (((Text)el).getRotationRelativity() == IText.TextRotation.NORTH_RELATIVE)
/*     */           {
/* 195 */             newRotation = Double.valueOf(newRotation.doubleValue() - southOffsetAngle(origin));
/*     */           }
/* 197 */           ((Text)el).setRotation(newRotation.doubleValue());
/*     */         }
/*     */ 
/* 200 */         PgenRotateElement.this.drawingLayer.resetElement(el);
/* 201 */         PgenRotateElement.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 204 */       return true;
/*     */     }
/*     */ 
/*     */     private double southOffsetAngle(Coordinate loc)
/*     */     {
/* 215 */       double delta = 0.05D;
/*     */ 
/* 222 */       double[] south = { loc.x, loc.y - delta, 0.0D };
/* 223 */       double[] pt1 = ((MapDescriptor)PgenRotateElement.this.drawingLayer.getDescriptor()).worldToPixel(south);
/*     */ 
/* 225 */       double[] north = { loc.x, loc.y + delta, 0.0D };
/* 226 */       double[] pt2 = ((MapDescriptor)PgenRotateElement.this.drawingLayer.getDescriptor()).worldToPixel(north);
/*     */ 
/* 228 */       return -90.0D - Math.toDegrees(Math.atan2(pt2[1] - pt1[1], pt2[0] - pt1[0]));
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int x, int y, int button)
/*     */     {
/* 240 */       if (!PgenRotateElement.this.isResourceEditable()) return false;
/*     */ 
/* 243 */       if ((button == 1) && (PgenRotateElement.this.drawingLayer != null))
/*     */       {
/* 246 */         DrawableElement el = PgenRotateElement.this.drawingLayer.getSelectedDE();
/*     */ 
/* 248 */         if ((el != null) && (this.oldDir != null))
/*     */         {
/* 250 */           DrawableElement newEl = (DrawableElement)el.copy();
/*     */ 
/* 252 */           PgenRotateElement.this.drawingLayer.resetElement(el);
/*     */ 
/* 254 */           if ((el instanceof Vector)) {
/* 255 */             ((Vector)el).setDirection(this.oldDir.doubleValue());
/* 256 */             this.oldDir = null;
/* 257 */           } else if ((el instanceof Text)) {
/* 258 */             ((Text)el).setRotation(this.oldDir.doubleValue());
/* 259 */             this.oldDir = null;
/*     */           }
/*     */ 
/* 262 */           PgenRotateElement.this.drawingLayer.replaceElement(el, newEl);
/* 263 */           PgenRotateElement.this.drawingLayer.setSelected(newEl);
/*     */ 
/* 265 */           PgenRotateElement.this.mapEditor.refresh();
/*     */         }
/*     */       }
/*     */ 
/* 269 */       return false;
/*     */     }
/*     */ 
/*     */     protected int getNearestPtIndex(MultiPointElement el, Coordinate pt)
/*     */     {
/* 283 */       int ptId = 0;
/* 284 */       double minDistance = -1.0D;
/*     */ 
/* 286 */       GeodeticCalculator gc = new GeodeticCalculator(PgenRotateElement.this.drawingLayer.getCoordinateReferenceSystem());
/* 287 */       gc.setStartingGeographicPoint(pt.x, pt.y);
/* 288 */       int index = 0;
/* 289 */       for (Coordinate elPoint : el.getPoints()) {
/* 290 */         gc.setDestinationGeographicPoint(elPoint.x, elPoint.y);
/* 291 */         double dist = gc.getOrthodromicDistance();
/* 292 */         if ((minDistance < 0.0D) || (dist < minDistance)) {
/* 293 */           minDistance = dist;
/* 294 */           ptId = index;
/*     */         }
/* 296 */         index++;
/*     */       }
/* 298 */       return ptId;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenRotateElement
 * JD-Core Version:    0.6.2
 */