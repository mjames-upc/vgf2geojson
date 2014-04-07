/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.IDisplayPaneContainer;
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlgFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.GfaAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.PgenFilterDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaClip;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaSnap;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.IGfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenGfaDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   private boolean startGfaText;
/*  66 */   private static String msg = "This Gfa is invalid and will be excluded in any future FROM actions. Please correct it before starting FROM.";
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  77 */     super.activateTool();
/*     */ 
/*  79 */     if (this.attrDlg == null) {
/*  80 */       this.attrDlg = AttrDlgFactory.createAttrDlg(this.pgenCategory, this.pgenType, 
/*  81 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/*     */     }
/*     */ 
/*  84 */     if ((this.attrDlg != null) && (!isDelObj()))
/*     */     {
/*     */       String param;
/*  87 */       if (((param = this.event.getParameter("startGfaText")) != null) && 
/*  88 */         (param.equalsIgnoreCase("true")))
/*     */       {
/*  90 */         Gfa gfa = (Gfa)this.event.getParameters().get("lastUsedGfa");
/*  91 */         this.startGfaText = true;
/*  92 */         PgenGfaDrawingHandler handler = (PgenGfaDrawingHandler)getMouseHandler();
/*  93 */         if (gfa != null) {
/*  94 */           handler.points.addAll(gfa.getPoints());
/*  95 */           if (handler.elem == null) {
/*  96 */             handler.elem = gfa;
/*     */           }
/*  98 */           GfaAttrDlg dlg = (GfaAttrDlg)this.attrDlg;
/*  99 */           dlg.setGfaArea(gfa.getGfaArea());
/* 100 */           dlg.setGfaBeginning(gfa.getGfaBeginning());
/* 101 */           dlg.setGfaEnding(gfa.getGfaEnding());
/* 102 */           dlg.setGfaStates(gfa.getGfaStates());
/* 103 */           this.drawingLayer.setSelected(gfa);
/* 104 */           this.editor.refresh();
/*     */         }
/*     */       }
/*     */       else {
/* 108 */         this.startGfaText = false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/* 121 */     super.deactivateTool();
/*     */ 
/* 123 */     PgenGfaDrawingHandler mph = (PgenGfaDrawingHandler)this.mouseHandler;
/* 124 */     if (mph != null) mph.clearPoints();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 132 */     if (this.mouseHandler == null) {
/* 133 */       this.mouseHandler = new PgenGfaDrawingHandler();
/*     */     }
/* 135 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public class PgenGfaDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/* 150 */     protected ArrayList<Coordinate> points = new ArrayList();
/*     */     protected AbstractDrawableComponent elem;
/* 161 */     protected DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/*     */     public PgenGfaDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 173 */       Coordinate loc = PgenGfaDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 174 */       if ((loc == null) || (this.shiftDown)) return false;
/* 175 */       DrawableType drawableType = getDrawableType(PgenGfaDrawingTool.this.pgenType);
/*     */ 
/* 177 */       if (button == 1) {
/* 178 */         if ((PgenGfaDrawingTool.this.startGfaText) && (((GfaAttrDlg)PgenGfaDrawingTool.this.attrDlg).isMoveTextEnable()))
/*     */         {
/* 180 */           Gfa newGfa = (Gfa)this.elem.copy();
/* 181 */           PgenGfaDrawingTool.this.drawingLayer.replaceElement(this.elem, newGfa);
/* 182 */           this.elem = newGfa;
/*     */ 
/* 184 */           handleGfaMouseDown(loc, drawableType);
/* 185 */           PgenGfaDrawingTool.this.drawingLayer.setSelected((Gfa)this.elem);
/*     */         }
/* 188 */         else if ((PgenGfaDrawingTool.this.startGfaText) && (drawableType == DrawableType.GFA)) {
/* 189 */           PgenGfaDrawingTool.this.startGfaText = false;
/* 190 */           PgenGfaDrawingTool.this.drawingLayer.addElement(this.elem);
/*     */ 
/* 192 */           validateGfa((Gfa)this.elem);
/* 193 */           handleGfaMouseDown(loc, drawableType);
/* 194 */           ((GfaAttrDlg)PgenGfaDrawingTool.this.attrDlg).enableMoveTextBtn(true);
/*     */         }
/*     */         else
/*     */         {
/* 198 */           if (this.points.size() == 0)
/*     */           {
/* 200 */             ((GfaAttrDlg)PgenGfaDrawingTool.this.attrDlg).enableMoveTextBtn(false);
/*     */           }
/*     */ 
/* 203 */           if (isValidPoint(this.points, loc)) {
/* 204 */             this.points.add(loc);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 210 */         PgenGfaDrawingTool.this.attrDlg.setAttrForDlg(PgenGfaDrawingTool.this.attrDlg);
/* 211 */         return true;
/*     */       }
/* 213 */       if (button == 3)
/*     */       {
/* 216 */         if (PgenGfaDrawingTool.this.startGfaText) {
/* 217 */           PgenGfaDrawingTool.this.startGfaText = false;
/*     */ 
/* 219 */           if (((Gfa)this.elem).getGfaTextCoordinate() == null)
/*     */           {
/* 221 */             ((Gfa)this.elem).setGfaTextCoordinate(loc);
/* 222 */             PgenGfaDrawingTool.this.drawingLayer.addElement(this.elem);
/* 223 */             validateGfa((Gfa)this.elem);
/* 224 */             PgenGfaDrawingTool.this.mapEditor.refresh();
/* 225 */             this.points.clear();
/*     */           }
/*     */           else
/*     */           {
/* 229 */             PgenUtil.setSelectingMode(this.elem);
/* 230 */             this.elem = null;
/* 231 */             return true;
/*     */           }
/*     */         }
/*     */ 
/* 235 */         if (this.points.size() == 0)
/*     */         {
/* 237 */           closeAttrDlg(PgenGfaDrawingTool.this.attrDlg);
/* 238 */           PgenGfaDrawingTool.this.attrDlg = null;
/* 239 */           PgenGfaDrawingTool.this.drawingLayer.removeSelected((Gfa)this.elem);
/* 240 */           this.elem = null;
/* 241 */           PgenUtil.setSelectingMode();
/*     */         }
/* 245 */         else if (this.points.size() < 2) {
/* 246 */           removeClearRefresh();
/*     */         }
/*     */         else
/*     */         {
/* 251 */           if ((drawableType == DrawableType.GFA) && (this.points.size() == 2)) {
/* 252 */             removeClearRefresh();
/* 253 */             return true;
/*     */           }
/* 255 */           if (!((GfaAttrDlg)PgenGfaDrawingTool.this.attrDlg).validateRequiredFields()) return false;
/*     */ 
/* 257 */           if (((IGfa)PgenGfaDrawingTool.this.attrDlg).getGfaFcstHr().indexOf("-") > -1)
/*     */           {
/* 259 */             this.points = SnapUtil.getSnapWithStation(this.points, SnapUtil.VOR_STATION_LIST, 10, 16);
/*     */           }
/*     */ 
/* 263 */           this.elem = this.def.create(drawableType, PgenGfaDrawingTool.this.attrDlg, 
/* 264 */             PgenGfaDrawingTool.this.pgenCategory, PgenGfaDrawingTool.this.pgenType, this.points, PgenGfaDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 267 */           if (PgenFilterDlg.isFilterDlgOpen()) {
/* 268 */             PgenFilterDlg.getInstance(null).setHourChkBox(((Gfa)this.elem).getForecastHours(), true);
/*     */           }
/*     */ 
/* 272 */           String vorText = Gfa.buildVorText((Gfa)this.elem);
/* 273 */           ((Gfa)this.elem).setGfaVorText(vorText);
/* 274 */           ((GfaAttrDlg)PgenGfaDrawingTool.this.attrDlg).setVorText(vorText);
/*     */ 
/* 276 */           PgenGfaDrawingTool.this.startGfaText = true;
/* 277 */           PgenGfaDrawingTool.this.attrDlg.setAttrForDlg(PgenGfaDrawingTool.this.attrDlg);
/*     */ 
/* 279 */           return true;
/*     */         }
/*     */ 
/* 282 */         return true;
/*     */       }
/* 284 */       if (button == 2) {
/* 285 */         if (PgenGfaDrawingTool.this.startGfaText)
/* 286 */           return handleMouseDown(anX, aY, 1);
/* 287 */         if (this.points.size() >= 2) {
/* 288 */           return handleMouseDown(anX, aY, 3);
/*     */         }
/* 290 */         return true;
/*     */       }
/*     */ 
/* 293 */       return false;
/*     */     }
/*     */ 
/*     */     private boolean handleGfaMouseDown(Coordinate loc, DrawableType drawableType)
/*     */     {
/* 298 */       ((Gfa)this.elem).setGfaTextCoordinate(loc);
/*     */ 
/* 300 */       removeClearRefresh();
/*     */ 
/* 302 */       ((GfaAttrDlg)PgenGfaDrawingTool.this.attrDlg).populateTagCbo();
/* 303 */       ((GfaAttrDlg)PgenGfaDrawingTool.this.attrDlg).setDrawableElement((Gfa)this.elem);
/*     */ 
/* 305 */       return true;
/*     */     }
/*     */ 
/*     */     private void removeClearRefresh() {
/* 309 */       PgenGfaDrawingTool.this.drawingLayer.removeGhostLine();
/* 310 */       this.points.clear();
/* 311 */       PgenGfaDrawingTool.this.mapEditor.refresh();
/*     */     }
/*     */ 
/*     */     private DrawableType getDrawableType(String pgenTypeString) {
/* 315 */       if (pgenTypeString.equalsIgnoreCase("GFA"))
/* 316 */         return DrawableType.GFA;
/* 317 */       return DrawableType.LINE;
/*     */     }
/*     */ 
/*     */     private void closeAttrDlg(AttrDlg attrDlgObject) {
/* 321 */       if (attrDlgObject == null)
/* 322 */         return;
/* 323 */       attrDlgObject.close();
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 334 */       if (!PgenGfaDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 337 */       Coordinate loc = PgenGfaDrawingTool.this.mapEditor.translateClick(x, y);
/* 338 */       if (loc == null) return false;
/*     */ 
/* 341 */       AbstractDrawableComponent ghost = null;
/* 342 */       if ((PgenGfaDrawingTool.this.startGfaText) && (((GfaAttrDlg)PgenGfaDrawingTool.this.attrDlg).isMoveTextEnable())) {
/* 343 */         return handleGfaTextMouseMove(loc);
/*     */       }
/* 345 */       if (PgenGfaDrawingTool.this.startGfaText) {
/* 346 */         return handleGfaMouseMove(loc);
/*     */       }
/*     */ 
/* 349 */       ghost = this.def.create(DrawableType.GFA, PgenGfaDrawingTool.this.attrDlg, 
/* 350 */         PgenGfaDrawingTool.this.pgenCategory, PgenGfaDrawingTool.this.pgenType, this.points, PgenGfaDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 353 */       if ((this.points != null) && (this.points.size() >= 1))
/*     */       {
/* 355 */         ArrayList ghostPts = new ArrayList(this.points);
/* 356 */         ghostPts.add(loc);
/* 357 */         Line ln = (Line)ghost;
/* 358 */         ln.setLinePoints(new ArrayList(ghostPts));
/*     */ 
/* 360 */         PgenGfaDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 361 */         PgenGfaDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 365 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 371 */       if ((!PgenGfaDrawingTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 372 */       return true;
/*     */     }
/*     */ 
/*     */     private boolean handleGfaTextMouseMove(Coordinate loc)
/*     */     {
/* 377 */       AbstractDrawableComponent ghost = null;
/*     */ 
/* 382 */       if ((this.elem != null) && (((Gfa)this.elem).getGfaTextCoordinate() != null)) {
/* 383 */         ghost = (Gfa)this.elem.copy();
/*     */       }
/*     */       else {
/* 386 */         ghost = (Gfa)this.def.create(DrawableType.GFA, PgenGfaDrawingTool.this.attrDlg, 
/* 387 */           PgenGfaDrawingTool.this.pgenCategory, PgenGfaDrawingTool.this.pgenType, this.points, PgenGfaDrawingTool.this.drawingLayer.getActiveLayer());
/*     */       }
/*     */ 
/* 390 */       ((Gfa)ghost).setGfaTextCoordinate(loc);
/*     */ 
/* 392 */       PgenGfaDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 393 */       PgenGfaDrawingTool.this.mapEditor.refresh();
/* 394 */       return false;
/*     */     }
/*     */ 
/*     */     private boolean handleGfaMouseMove(Coordinate loc)
/*     */     {
/* 399 */       Gfa gfa = (Gfa)this.def.create(DrawableType.GFA, PgenGfaDrawingTool.this.attrDlg, 
/* 400 */         PgenGfaDrawingTool.this.pgenCategory, PgenGfaDrawingTool.this.pgenType, this.points, PgenGfaDrawingTool.this.drawingLayer.getActiveLayer());
/* 401 */       gfa.setGfaTextCoordinate(loc);
/* 402 */       PgenGfaDrawingTool.this.drawingLayer.setGhostLine(gfa);
/* 403 */       PgenGfaDrawingTool.this.mapEditor.refresh();
/* 404 */       return false;
/*     */     }
/*     */ 
/*     */     public void clearPoints() {
/* 408 */       this.points.clear();
/*     */     }
/*     */ 
/*     */     private void validateGfa(Gfa gfa)
/*     */     {
/* 416 */       if (!gfa.isValid()) {
/* 417 */         MessageDialog confirmDlg = new MessageDialog(
/* 418 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 419 */           "Invalid GFA Polygon", null, PgenGfaDrawingTool.msg, 
/* 420 */           4, new String[] { "OK" }, 0);
/*     */ 
/* 422 */         confirmDlg.open();
/*     */       }
/*     */     }
/*     */ 
/*     */     private boolean isValidPoint(ArrayList<Coordinate> points, Coordinate loc)
/*     */     {
/* 436 */       boolean valid = true;
/*     */ 
/* 438 */       if (loc == null) {
/* 439 */         valid = false;
/*     */       }
/*     */       else {
/* 442 */         int ll = points.size();
/* 443 */         if (ll > 1)
/*     */         {
/* 445 */           if ((GfaSnap.getInstance().isCluster(loc, (Coordinate)points.get(ll - 1))) || 
/* 446 */             (GfaSnap.getInstance().isCluster(loc, (Coordinate)points.get(0))))
/*     */           {
/* 448 */             Coordinate[] pts = new Coordinate[points.size() + 1];
/* 449 */             points.toArray(pts);
/* 450 */             pts[(pts.length - 1)] = loc;
/*     */ 
/* 452 */             Polygon poly1 = GfaClip.getInstance().pointsToPolygon(pts);
/*     */ 
/* 454 */             valid = poly1.isValid();
/*     */ 
/* 456 */             if (valid) {
/* 457 */               Polygon poly2 = GfaClip.getInstance().pointsToPolygon(PgenUtil.latlonToGrid(pts));
/* 458 */               valid = poly2.isValid();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 464 */       return valid;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenGfaDrawingTool
 * JD-Core Version:    0.6.2
 */