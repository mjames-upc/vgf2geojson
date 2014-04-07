/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.StationTable;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchBoxAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox.WatchShape;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Polygon;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenWatchBoxDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   protected void activateTool()
/*     */   {
/*  65 */     super.activateTool();
/*  66 */     if (super.isDelObj()) return;
/*     */ 
/*  68 */     if (this.attrDlg != null) {
/*  69 */       ((WatchBoxAttrDlg)this.attrDlg).enableDspBtn(false);
/*     */     }
/*     */ 
/*  72 */     new Thread() {
/*     */       public void run() {
/*  74 */         PgenStaticDataProvider.getProvider().getSPCCounties();
/*     */       }
/*     */     }
/*  76 */     .start();
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/*  89 */     super.deactivateTool();
/*     */ 
/*  91 */     PgenWatchBoxDrawingHandler wbh = (PgenWatchBoxDrawingHandler)this.mouseHandler;
/*  92 */     if (wbh != null) wbh.clearPoints();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 102 */     if (this.mouseHandler == null)
/*     */     {
/* 104 */       this.mouseHandler = new PgenWatchBoxDrawingHandler(null);
/*     */     }
/*     */ 
/* 108 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public static ArrayList<Station> getAnchorsInPoly(AbstractEditor editor, List<Coordinate> polyPoints)
/*     */   {
/* 349 */     ArrayList stnList = new ArrayList();
/*     */ 
/* 352 */     int[] xpoints = new int[polyPoints.size()];
/* 353 */     int[] ypoints = new int[polyPoints.size()];
/* 354 */     for (int ii = 0; ii < polyPoints.size(); ii++) {
/* 355 */       double[] pt = editor.translateInverseClick((Coordinate)polyPoints.get(ii));
/* 356 */       xpoints[ii] = ((int)pt[0]);
/* 357 */       ypoints[ii] = ((int)pt[1]);
/*     */     }
/*     */ 
/* 360 */     Polygon poly = new Polygon(xpoints, ypoints, polyPoints.size());
/*     */ 
/* 363 */     StationTable anchorTbl = PgenStaticDataProvider.getProvider().getAnchorTbl();
/* 364 */     List anchorList = anchorTbl.getStationList();
/*     */ 
/* 367 */     for (Station stn : anchorList) {
/* 368 */       double[] loc = editor.translateInverseClick(new Coordinate(stn.getLongitude().floatValue(), stn.getLatitude().floatValue()));
/* 369 */       if (poly.contains(loc[0], loc[1])) {
/* 370 */         stnList.add(stn);
/*     */       }
/*     */     }
/*     */ 
/* 374 */     return stnList;
/*     */   }
/*     */ 
/*     */   private class PgenWatchBoxDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/* 123 */     private ArrayList<Coordinate> points = new ArrayList();
/*     */ 
/* 129 */     private DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/*     */     private PgenWatchBoxDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 139 */       if (!PgenWatchBoxDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 142 */       Coordinate loc = PgenWatchBoxDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 143 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 145 */       if (button == 1)
/*     */       {
/* 150 */         if (((WatchBoxAttrDlg)PgenWatchBoxDrawingTool.this.attrDlg).getWatchBoxShape() == null) {
/* 151 */           MessageDialog infoDlg = new MessageDialog(
/* 152 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 153 */             "Information", null, "Please select a watch shape before drawing.", 
/* 154 */             2, new String[] { "OK" }, 0);
/*     */ 
/* 156 */           infoDlg.open();
/*     */         }
/*     */         else {
/* 159 */           this.points.add(loc);
/*     */         }
/*     */ 
/* 162 */         return true;
/*     */       }
/*     */ 
/* 165 */       if (button == 3)
/*     */       {
/* 167 */         if (this.points.size() == 0)
/*     */         {
/* 169 */           PgenWatchBoxDrawingTool.this.attrDlg.close();
/* 170 */           PgenWatchBoxDrawingTool.this.attrDlg = null;
/* 171 */           PgenUtil.setSelectingMode();
/*     */         }
/* 174 */         else if (this.points.size() < 2)
/*     */         {
/* 176 */           PgenWatchBoxDrawingTool.this.drawingLayer.removeGhostLine();
/* 177 */           this.points.clear();
/*     */ 
/* 179 */           PgenWatchBoxDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 183 */         return true;
/*     */       }
/*     */ 
/* 188 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int x, int y, int button)
/*     */     {
/* 202 */       if (!PgenWatchBoxDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 204 */       if ((button == 1) && 
/* 205 */         (this.points != null) && (this.points.size() == 2))
/*     */       {
/* 207 */         WatchBox.WatchShape ws = ((WatchBoxAttrDlg)PgenWatchBoxDrawingTool.this.attrDlg).getWatchBoxShape();
/*     */ 
/* 212 */         ArrayList anchorsInPoly = PgenWatchBoxDrawingTool.getAnchorsInPoly(PgenWatchBoxDrawingTool.this.mapEditor, 
/* 213 */           WatchBox.generateWatchBoxPts(ws, 96560.3984375D, (Coordinate)this.points.get(0), (Coordinate)this.points.get(1)));
/*     */ 
/* 220 */         if ((anchorsInPoly == null) || (anchorsInPoly.isEmpty())) {
/* 221 */           MessageDialog infoDlg = new MessageDialog(
/* 222 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 223 */             "Information", null, "No anchor point in the area!", 
/* 224 */             2, new String[] { "OK" }, 0);
/*     */ 
/* 226 */           infoDlg.open();
/*     */         }
/*     */         else
/*     */         {
/* 230 */           DECollection dec = this.def.createWatchBox(PgenWatchBoxDrawingTool.this.pgenCategory, PgenWatchBoxDrawingTool.this.pgenType, ws, 
/* 231 */             (Coordinate)this.points.get(0), (Coordinate)this.points.get(1), 
/* 232 */             anchorsInPoly, PgenWatchBoxDrawingTool.this.attrDlg);
/* 233 */           if (dec != null) {
/* 234 */             PgenWatchBoxDrawingTool.this.drawingLayer.addElement(dec);
/*     */ 
/* 237 */             ((WatchBoxAttrDlg)PgenWatchBoxDrawingTool.this.attrDlg).enableDspBtn(true);
/* 238 */             ((WatchBoxAttrDlg)PgenWatchBoxDrawingTool.this.attrDlg).setWatchBox((WatchBox)dec.getPrimaryDE());
/* 239 */             this.points.clear();
/* 240 */             PgenUtil.loadWatchBoxModifyTool(dec.getPrimaryDE());
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 245 */         PgenWatchBoxDrawingTool.this.drawingLayer.removeGhostLine();
/* 246 */         this.points.clear();
/*     */ 
/* 248 */         PgenWatchBoxDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 254 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 265 */       if (!PgenWatchBoxDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 268 */       Coordinate loc = PgenWatchBoxDrawingTool.this.mapEditor.translateClick(x, y);
/* 269 */       if (loc == null) return false;
/*     */ 
/* 272 */       AbstractDrawableComponent ghost = this.def.create(DrawableType.LINE, PgenWatchBoxDrawingTool.this.attrDlg, 
/* 273 */         "Line", "LINE_SOLID", this.points, PgenWatchBoxDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 275 */       if ((this.points != null) && (this.points.size() >= 1))
/*     */       {
/* 277 */         ArrayList ghostPts = new ArrayList(this.points);
/* 278 */         ghostPts.add(loc);
/* 279 */         ((Line)ghost).setLinePoints(new ArrayList(ghostPts));
/*     */ 
/* 281 */         PgenWatchBoxDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 282 */         PgenWatchBoxDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 286 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 295 */       if ((!PgenWatchBoxDrawingTool.this.drawingLayer.isEditable()) || (this.shiftDown)) return false;
/* 296 */       return true;
/*     */     }
/*     */ 
/*     */     public void clearPoints()
/*     */     {
/* 303 */       this.points.clear();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenWatchBoxDrawingTool
 * JD-Core Version:    0.6.2
 */