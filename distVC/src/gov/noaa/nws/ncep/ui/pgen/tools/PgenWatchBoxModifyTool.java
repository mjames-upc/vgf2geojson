/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchBoxAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchInfoDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IWatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenWatchBoxModifyTool extends PgenSelectingTool
/*     */ {
/*     */   protected void activateTool()
/*     */   {
/*  63 */     super.activateTool();
/*     */ 
/*  65 */     if ((this.event.getTrigger() instanceof WatchBox))
/*     */     {
/*  67 */       ((WatchBoxAttrDlg)this.attrDlg).setWbTool(this);
/*     */ 
/*  71 */       if (this.drawingLayer.getSelectedDE() == null) {
/*  72 */         this.drawingLayer.setSelected(((WatchBoxAttrDlg)this.attrDlg).getWatchBox());
/*  73 */         ((WatchBoxAttrDlg)this.attrDlg).setAttrForDlg(((WatchBoxAttrDlg)this.attrDlg).getWatchBox());
/*  74 */         ((WatchBoxAttrDlg)this.attrDlg).enableButtons();
/*  75 */         ((WatchBoxAttrDlg)this.attrDlg).openSpecDlg();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  87 */     if (this.mouseHandler == null)
/*     */     {
/*  89 */       this.mouseHandler = new PgenWatchBoxModifyHandler(this, this.mapEditor, this.drawingLayer, this.attrDlg);
/*     */     }
/*     */ 
/*  93 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public static boolean resnapWatchBox(AbstractEditor editor, WatchBox oldBox, WatchBox newBox)
/*     */   {
/* 296 */     Station anchor1 = null;
/* 297 */     Station anchor2 = null;
/* 298 */     ArrayList watchPts = null;
/*     */ 
/* 301 */     ArrayList anchorsInPoly = PgenWatchBoxDrawingTool.getAnchorsInPoly(editor, oldBox.getPoints());
/*     */ 
/* 304 */     if ((anchorsInPoly == null) || (anchorsInPoly.isEmpty())) {
/* 305 */       MessageDialog infoDlg = new MessageDialog(
/* 306 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 307 */         "Information", null, "No anchor point in the area!", 
/* 308 */         2, new String[] { "OK" }, 0);
/*     */ 
/* 310 */       infoDlg.open();
/* 311 */       return false;
/*     */     }
/*     */ 
/* 316 */     anchor1 = WatchBox.getNearestAnchorPt((Coordinate)oldBox.getPoints().get(0), anchorsInPoly);
/* 317 */     anchor2 = WatchBox.getNearestAnchorPt((Coordinate)oldBox.getPoints().get(4), anchorsInPoly);
/*     */ 
/* 319 */     if ((anchor1 != null) && (anchor2 != null))
/*     */     {
/* 321 */       watchPts = WatchBox.generateWatchBoxPts(newBox.getWatchBoxShape(), 
/* 322 */         oldBox.getHalfWidth(), 
/* 323 */         WatchBox.snapOnAnchor(anchor1, (Coordinate)oldBox.getPoints().get(0)), 
/* 324 */         WatchBox.snapOnAnchor(anchor2, (Coordinate)oldBox.getPoints().get(4)));
/*     */ 
/* 326 */       newBox.setPoints(watchPts);
/* 327 */       newBox.setAnchors(anchor1, anchor2);
/*     */     }
/*     */ 
/* 330 */     return true;
/*     */   }
/*     */ 
/*     */   public void setAddDelCntyHandler()
/*     */   {
/* 336 */     setHandler(new PgenWatchBoxAddDelCntyHandler(this.mapEditor, this.drawingLayer, 
/* 337 */       ((WatchBoxAttrDlg)this.attrDlg).getWatchBox(), this));
/*     */   }
/*     */ 
/*     */   public void resetMouseHandler() {
/* 341 */     setHandler(new PgenWatchBoxModifyHandler(this, this.mapEditor, this.drawingLayer, this.attrDlg));
/*     */   }
/*     */ 
/*     */   private class PgenWatchBoxModifyHandler extends PgenSelectHandler
/*     */   {
/* 104 */     private boolean dontMove = true;
/*     */     private boolean simulate;
/*     */ 
/*     */     public PgenWatchBoxModifyHandler(AbstractPgenTool tool, AbstractEditor mapEditor, PgenResource resource, AttrDlg attrDlg)
/*     */     {
/* 110 */       super(mapEditor, resource, attrDlg);
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 121 */       if (!PgenWatchBoxModifyTool.this.isResourceEditable()) return false;
/*     */ 
/* 124 */       Coordinate loc = this.mapEditor.translateClick(anX, aY);
/* 125 */       if ((loc == null) || (this.shiftDown) || (this.simulate)) return false;
/*     */ 
/* 127 */       if (button == 1) {
/* 128 */         this.dontMove = false;
/* 129 */         this.firstDown = loc;
/* 130 */         return false;
/*     */       }
/*     */ 
/* 133 */       if (button == 3)
/*     */       {
/* 136 */         if (PgenWatchBoxModifyTool.this.attrDlg != null) {
/* 137 */           PgenWatchBoxModifyTool.this.attrDlg.close();
/*     */         }
/*     */ 
/* 140 */         PgenWatchBoxModifyTool.this.attrDlg = null;
/*     */ 
/* 142 */         PgenWatchBoxModifyTool.this.drawingLayer.removeGhostLine();
/* 143 */         this.ptSelected = false;
/* 144 */         PgenWatchBoxModifyTool.this.drawingLayer.removeSelected();
/* 145 */         this.mapEditor.refresh();
/* 146 */         PgenUtil.setSelectingMode();
/*     */ 
/* 148 */         return true;
/*     */       }
/*     */ 
/* 153 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int button)
/*     */     {
/* 167 */       if (!PgenWatchBoxModifyTool.this.isResourceEditable()) return false;
/* 168 */       if (this.dontMove) return true;
/*     */ 
/* 171 */       Coordinate loc = this.mapEditor.translateClick(x, y);
/* 172 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 174 */       DrawableElement tmpEl = PgenWatchBoxModifyTool.this.drawingLayer.getSelectedDE();
/*     */ 
/* 176 */       if ((PgenWatchBoxModifyTool.this.drawingLayer.getDistance(tmpEl, loc) > 30.0D) && (!this.ptSelected)) {
/* 177 */         if ((this.firstDown != null) && (PgenWatchBoxModifyTool.this.drawingLayer.getDistance(tmpEl, this.firstDown) < 30.0D)) {
/* 178 */           this.firstDown = null;
/*     */         }
/*     */         else {
/* 181 */           return false;
/*     */         }
/*     */       }
/*     */ 
/* 185 */       if ((tmpEl != null) && ((tmpEl instanceof WatchBox)))
/*     */       {
/* 187 */         if (!this.ptSelected)
/*     */         {
/* 189 */           this.ghostEl = ((WatchBox)tmpEl.copy());
/*     */ 
/* 191 */           if (this.ghostEl != null) {
/* 192 */             this.ghostEl.setColors(new Color[] { this.ghostColor, new Color(255, 255, 255) });
/*     */ 
/* 194 */             ArrayList points = new ArrayList();
/* 195 */             points.addAll(tmpEl.getPoints());
/*     */ 
/* 197 */             this.ghostEl.setPoints(points);
/*     */ 
/* 199 */             this.ghostEl.setPgenCategory(tmpEl.getPgenCategory());
/* 200 */             this.ghostEl.setPgenType(tmpEl.getPgenType());
/*     */ 
/* 202 */             this.ptIndex = getNearestPtIndex(this.ghostEl, loc);
/*     */ 
/* 204 */             this.ptSelected = true;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 209 */           this.ghostEl.setPoints(((WatchBox)tmpEl).createNewWatchBox(this.ptIndex, loc, ((IWatchBox)PgenWatchBoxModifyTool.this.attrDlg).getWatchBoxShape()));
/* 210 */           PgenWatchBoxModifyTool.this.drawingLayer.setGhostLine(this.ghostEl);
/* 211 */           this.mapEditor.refresh();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 216 */       this.simulate = true;
/* 217 */       PgenUtil.simulateMouseDown(x, y, button, this.mapEditor);
/* 218 */       this.simulate = false;
/* 219 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int x, int y, int button)
/*     */     {
/* 231 */       this.firstDown = null;
/* 232 */       if (!PgenWatchBoxModifyTool.this.isResourceEditable()) return false;
/*     */ 
/* 235 */       if ((button == 1) && (PgenWatchBoxModifyTool.this.drawingLayer != null))
/*     */       {
/* 238 */         WatchBox el = (WatchBox)PgenWatchBoxModifyTool.this.drawingLayer.getSelectedDE();
/*     */ 
/* 240 */         if (el != null)
/*     */         {
/* 242 */           WatchBox newEl = (WatchBox)el.copy();
/*     */ 
/* 244 */           if (this.ptSelected)
/*     */           {
/* 246 */             this.ptSelected = false;
/*     */ 
/* 249 */             PgenWatchBoxModifyTool.resnapWatchBox(this.mapEditor, (WatchBox)this.ghostEl, newEl);
/* 250 */             ((WatchBoxAttrDlg)PgenWatchBoxModifyTool.this.attrDlg).setWatchBox(newEl);
/* 251 */             WatchInfoDlg infoDlg = ((WatchBoxAttrDlg)PgenWatchBoxModifyTool.this.attrDlg).getWatchInfoDlg();
/* 252 */             if ((infoDlg != null) && (infoDlg.getShell() != null)) {
/* 253 */               if (infoDlg.isCountyLock()) {
/* 254 */                 newEl.setCountyList(el.getCountyList());
/*     */               }
/*     */               else {
/* 257 */                 infoDlg.clearCwaPane();
/*     */               }
/* 259 */               infoDlg.setStatesWFOs();
/*     */             }
/*     */ 
/* 263 */             PgenWatchBoxModifyTool.this.drawingLayer.replaceElement(el, newEl);
/*     */ 
/* 267 */             if (!(PgenWatchBoxModifyTool.this.drawingLayer.getSelectedComp() instanceof DECollection)) {
/* 268 */               PgenWatchBoxModifyTool.this.drawingLayer.setSelected(newEl);
/*     */             }
/*     */ 
/* 271 */             PgenWatchBoxModifyTool.this.drawingLayer.removeGhostLine();
/*     */           }
/*     */ 
/* 275 */           this.mapEditor.refresh();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 280 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenWatchBoxModifyTool
 * JD-Core Version:    0.6.2
 */