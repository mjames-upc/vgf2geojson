/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlgFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.FrontAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.SymbolAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchBoxAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.VolcanoVaaAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.AcceptFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ import java.awt.Polygon;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenMultiSelectTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  64 */     if (this.mouseHandler == null)
/*     */     {
/*  66 */       this.mouseHandler = new PgenMultiSelectHandler();
/*     */     }
/*     */ 
/*  70 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public class PgenMultiSelectHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     private int theFirstMouseX;
/*     */     private int theFirstMouseY;
/*     */     private boolean noCat;
/*     */     private boolean selectRect;
/*     */     private String pgenCat;
/*     */     private String pgenObj;
/*     */     private List<Coordinate> polyPoints;
/*     */ 
/*     */     public PgenMultiSelectHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 105 */       if (!PgenMultiSelectTool.this.isResourceEditable()) return false;
/*     */ 
/* 107 */       this.theFirstMouseX = anX;
/* 108 */       this.theFirstMouseY = aY;
/*     */ 
/* 110 */       if (button == 1)
/*     */       {
/* 112 */         this.pgenCat = PgenSession.getInstance().getPgenPalette().getCurrentCategory();
/* 113 */         this.pgenObj = PgenSession.getInstance().getPgenPalette().getCurrentObject();
/*     */ 
/* 116 */         if (this.pgenCat == null)
/*     */         {
/* 118 */           MessageDialog infoDlg = new MessageDialog(
/* 119 */             PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 120 */             "Information", null, "Please select a Pgen Class from the Palette.", 
/* 121 */             2, new String[] { "OK" }, 0);
/*     */ 
/* 123 */           infoDlg.open();
/* 124 */           this.noCat = true;
/*     */         }
/*     */         else
/*     */         {
/* 128 */           this.noCat = false;
/*     */         }
/*     */ 
/* 131 */         return true;
/*     */       }
/*     */ 
/* 134 */       if (button == 3) {
/* 135 */         if ((this.polyPoints == null) || (this.polyPoints.isEmpty()))
/*     */         {
/* 137 */           if (PgenMultiSelectTool.this.attrDlg != null) {
/* 138 */             PgenMultiSelectTool.this.attrDlg.close();
/*     */           }
/*     */ 
/* 141 */           PgenMultiSelectTool.this.attrDlg = null;
/* 142 */           PgenMultiSelectTool.this.pgenCategory = null;
/* 143 */           PgenMultiSelectTool.this.pgenType = null;
/*     */ 
/* 145 */           PgenMultiSelectTool.this.drawingLayer.removeGhostLine();
/* 146 */           PgenMultiSelectTool.this.drawingLayer.removeSelected();
/* 147 */           PgenMultiSelectTool.this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 152 */         return false;
/*     */       }
/*     */ 
/* 157 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int anX, int aY, int button)
/*     */     {
/* 171 */       if ((!PgenMultiSelectTool.this.isResourceEditable()) || (button != 1) || (this.noCat)) {
/* 172 */         return false;
/*     */       }
/*     */ 
/* 175 */       this.selectRect = true;
/*     */ 
/* 178 */       ArrayList points = new ArrayList();
/*     */ 
/* 180 */       points.add(PgenMultiSelectTool.this.mapEditor.translateClick(this.theFirstMouseX, this.theFirstMouseY));
/* 181 */       points.add(PgenMultiSelectTool.this.mapEditor.translateClick(this.theFirstMouseX, aY));
/* 182 */       points.add(PgenMultiSelectTool.this.mapEditor.translateClick(anX, aY));
/* 183 */       points.add(PgenMultiSelectTool.this.mapEditor.translateClick(anX, this.theFirstMouseY));
/*     */ 
/* 185 */       DrawableElementFactory def = new DrawableElementFactory();
/* 186 */       Line ghost = (Line)def.create(DrawableType.LINE, null, 
/* 187 */         "Lines", "LINE_SOLID", points, PgenMultiSelectTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 189 */       ghost.setLineWidth(1.0F);
/* 190 */       ghost.setColors(new Color[] { new Color(255, 255, 255), new Color(255, 255, 255) });
/* 191 */       ghost.setClosed(Boolean.valueOf(true));
/* 192 */       ghost.setSmoothFactor(0);
/*     */ 
/* 194 */       PgenMultiSelectTool.this.drawingLayer.setGhostLine(ghost);
/*     */ 
/* 196 */       PgenMultiSelectTool.this.mapEditor.refresh();
/* 197 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int anX, int aY, int button)
/*     */     {
/* 209 */       if ((!PgenMultiSelectTool.this.isResourceEditable()) || (this.noCat))
/* 210 */         return false;
/*     */       int[] ypoints;
/* 213 */       if (button == 1)
/*     */       {
/* 215 */         if ((this.shiftDown) && (!this.selectRect)) {
/* 216 */           if (this.polyPoints == null) {
/* 217 */             this.polyPoints = new ArrayList();
/*     */           }
/* 219 */           this.polyPoints.add(new Coordinate(anX, aY));
/*     */         }
/* 222 */         else if (this.selectRect)
/*     */         {
/* 225 */           int[] xpoints = { this.theFirstMouseX, this.theFirstMouseX, anX, anX };
/* 226 */           int[] ypoints = { this.theFirstMouseY, aY, aY, this.theFirstMouseY };
/*     */ 
/* 228 */           Polygon rectangle = new Polygon(xpoints, ypoints, 4);
/*     */ 
/* 230 */           PgenMultiSelectTool.this.drawingLayer.addSelected(inPoly(rectangle));
/*     */ 
/* 232 */           PgenMultiSelectTool.this.drawingLayer.removeGhostLine();
/* 233 */           this.selectRect = false;
/*     */         }
/*     */         else
/*     */         {
/* 240 */           Coordinate loc = PgenMultiSelectTool.this.mapEditor.translateClick(anX, aY);
/* 241 */           if (loc == null) return false;
/*     */ 
/* 243 */           if (!this.pgenCat.equalsIgnoreCase("met"))
/*     */           {
/* 245 */             this.noCat = false;
/*     */ 
/* 249 */             AbstractDrawableComponent adc = null;
/* 250 */             AbstractDrawableComponent contour = PgenMultiSelectTool.this.drawingLayer.getNearestComponent(loc, new AcceptFilter(), false);
/* 251 */             if ((contour instanceof Contours)) {
/* 252 */               adc = PgenMultiSelectTool.this.drawingLayer.getNearestElement(loc, (Contours)contour);
/*     */             }
/*     */             else
/*     */             {
/* 256 */               adc = PgenMultiSelectTool.this.drawingLayer.getNearestComponent(loc, new AcceptFilter(), true);
/*     */             }
/*     */ 
/* 259 */             if ((adc != null) && (adc.getPgenCategory().equalsIgnoreCase(this.pgenCat)))
/*     */             {
/* 261 */               if ((PgenMultiSelectTool.this.pgenType == null) || (PgenMultiSelectTool.this.pgenType.equalsIgnoreCase("MultiSelect"))) {
/* 262 */                 PgenMultiSelectTool.this.pgenType = adc.getPgenType();
/*     */               }
/*     */ 
/* 265 */               if ((!this.pgenCat.equalsIgnoreCase("Text")) || (
/* 266 */                 (this.pgenCat.equalsIgnoreCase("Text")) && 
/* 267 */                 (adc.getPgenType().equalsIgnoreCase(PgenMultiSelectTool.this.pgenType))))
/*     */               {
/* 269 */                 if (!PgenMultiSelectTool.this.drawingLayer.getAllSelected().contains(adc)) {
/* 270 */                   PgenMultiSelectTool.this.drawingLayer.addSelected(adc);
/*     */                 }
/*     */                 else {
/* 273 */                   PgenMultiSelectTool.this.drawingLayer.removeSelected(adc);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 278 */           else if ((this.pgenObj != null) && (this.pgenObj.equalsIgnoreCase("GFA")))
/*     */           {
/* 280 */             AbstractDrawableComponent adc = PgenMultiSelectTool.this.drawingLayer.getNearestComponent(loc, new AcceptFilter(), true);
/*     */ 
/* 282 */             if ((adc != null) && (adc.getPgenType().equalsIgnoreCase("GFA")))
/*     */             {
/* 284 */               if ((PgenMultiSelectTool.this.pgenType == null) || (PgenMultiSelectTool.this.pgenType.equalsIgnoreCase("MultiSelect"))) {
/* 285 */                 PgenMultiSelectTool.this.pgenType = adc.getPgenType();
/*     */               }
/*     */ 
/* 291 */               if (!PgenMultiSelectTool.this.drawingLayer.getAllSelected().contains(adc)) {
/* 292 */                 PgenMultiSelectTool.this.drawingLayer.addSelected(adc);
/*     */               }
/*     */               else {
/* 295 */                 PgenMultiSelectTool.this.drawingLayer.removeSelected(adc);
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/* 303 */       else if (button == 3)
/*     */       {
/* 305 */         if (this.polyPoints != null) {
/* 306 */           if (this.polyPoints.size() > 2) {
/* 307 */             int[] xpoints = new int[this.polyPoints.size()];
/* 308 */             ypoints = new int[this.polyPoints.size()];
/* 309 */             for (int ii = 0; ii < this.polyPoints.size(); ii++) {
/* 310 */               xpoints[ii] = ((int)((Coordinate)this.polyPoints.get(ii)).x);
/* 311 */               ypoints[ii] = ((int)((Coordinate)this.polyPoints.get(ii)).y);
/*     */             }
/*     */ 
/* 314 */             Polygon poly = new Polygon(xpoints, ypoints, this.polyPoints.size());
/* 315 */             PgenMultiSelectTool.this.drawingLayer.addSelected(inPoly(poly));
/*     */ 
/* 317 */             PgenMultiSelectTool.this.drawingLayer.removeGhostLine();
/*     */           }
/*     */ 
/* 321 */           this.polyPoints.clear();
/* 322 */           this.shiftDown = false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 327 */       if ((PgenMultiSelectTool.this.attrDlg != null) && (PgenMultiSelectTool.this.attrDlg.getShell() == null)) {
/* 328 */         PgenMultiSelectTool.this.attrDlg = null;
/*     */       }
/*     */ 
/* 331 */       if ((PgenMultiSelectTool.this.attrDlg == null) && (PgenMultiSelectTool.this.drawingLayer.getAllSelected() != null) && 
/* 332 */         (!PgenMultiSelectTool.this.drawingLayer.getAllSelected().isEmpty())) {
/* 333 */         if (this.pgenCat.equalsIgnoreCase("MET")) {
/* 334 */           PgenMultiSelectTool.this.pgenType = this.pgenObj;
/*     */         }
/*     */ 
/* 337 */         PgenMultiSelectTool.this.attrDlg = AttrDlgFactory.createAttrDlg(this.pgenCat, PgenMultiSelectTool.this.pgenType, 
/* 338 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/*     */ 
/* 341 */         if ((PgenMultiSelectTool.this.attrDlg instanceof VolcanoVaaAttrDlg)) {
/* 342 */           return false;
/*     */         }
/*     */ 
/* 345 */         if (PgenMultiSelectTool.this.attrDlg != null) {
/* 346 */           PgenMultiSelectTool.this.attrDlg.setBlockOnOpen(false);
/* 347 */           PgenMultiSelectTool.this.attrDlg.open();
/*     */ 
/* 349 */           PgenMultiSelectTool.this.attrDlg.enableButtons();
/* 350 */           PgenMultiSelectTool.this.attrDlg.setPgenCategory(this.pgenCat);
/* 351 */           PgenMultiSelectTool.this.attrDlg.setPgenType(null);
/* 352 */           PgenMultiSelectTool.this.attrDlg.setDrawingLayer(PgenMultiSelectTool.this.drawingLayer);
/* 353 */           PgenMultiSelectTool.this.attrDlg.setMapEditor(PgenMultiSelectTool.this.mapEditor);
/*     */ 
/* 355 */           if ((PgenMultiSelectTool.this.attrDlg instanceof SymbolAttrDlg)) {
/* 356 */             ((SymbolAttrDlg)PgenMultiSelectTool.this.attrDlg).enableLatLon(false);
/*     */           }
/* 358 */           else if ((PgenMultiSelectTool.this.attrDlg instanceof WatchBoxAttrDlg)) {
/* 359 */             ((WatchBoxAttrDlg)PgenMultiSelectTool.this.attrDlg).enableShapeBtn(false);
/* 360 */             ((WatchBoxAttrDlg)PgenMultiSelectTool.this.attrDlg).enableDspBtn(false);
/*     */           }
/* 362 */           else if ((PgenMultiSelectTool.this.attrDlg instanceof FrontAttrDlg))
/*     */           {
/* 364 */             for (AbstractDrawableComponent adc : PgenMultiSelectTool.this.drawingLayer.getAllSelected()) {
/* 365 */               if (((adc instanceof DrawableElement)) && 
/* 366 */                 (((DrawableElement)adc).getColors().length > 1)) {
/* 367 */                 ((FrontAttrDlg)PgenMultiSelectTool.this.attrDlg).setColor(new Color[] { Color.green, Color.green });
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 377 */       PgenMultiSelectTool.this.mapEditor.setFocus();
/* 378 */       PgenMultiSelectTool.this.mapEditor.refresh();
/*     */ 
/* 381 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int anX, int aY)
/*     */     {
/* 387 */       if ((!PgenMultiSelectTool.this.isResourceEditable()) || (this.noCat)) {
/* 388 */         return false;
/*     */       }
/*     */ 
/* 392 */       if ((this.polyPoints != null) && (!this.polyPoints.isEmpty()))
/*     */       {
/* 394 */         this.polyPoints.add(new Coordinate(anX, aY));
/*     */ 
/* 396 */         if (this.polyPoints.size() > 1)
/*     */         {
/* 398 */           ArrayList points = new ArrayList();
/*     */ 
/* 400 */           for (Coordinate loc : this.polyPoints) {
/* 401 */             points.add(PgenMultiSelectTool.this.mapEditor.translateClick(loc.x, loc.y));
/*     */           }
/*     */ 
/* 404 */           DrawableElementFactory def = new DrawableElementFactory();
/* 405 */           Line ghost = (Line)def.create(DrawableType.LINE, null, 
/* 406 */             "Lines", "LINE_SOLID", points, PgenMultiSelectTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 408 */           ghost.setLineWidth(1.0F);
/* 409 */           ghost.setColors(new Color[] { new Color(255, 255, 255), new Color(255, 255, 255) });
/* 410 */           ghost.setClosed(Boolean.valueOf(true));
/* 411 */           ghost.setSmoothFactor(0);
/*     */ 
/* 413 */           PgenMultiSelectTool.this.drawingLayer.setGhostLine(ghost);
/*     */         }
/*     */ 
/* 416 */         PgenMultiSelectTool.this.mapEditor.refresh();
/*     */ 
/* 418 */         this.polyPoints.remove(this.polyPoints.size() - 1);
/*     */       }
/* 420 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleKeyDown(int keyCode)
/*     */     {
/* 425 */       if (!PgenMultiSelectTool.this.isResourceEditable()) return false;
/*     */ 
/* 427 */       if (keyCode == 131072) {
/* 428 */         this.shiftDown = true;
/*     */       }
/* 430 */       else if (keyCode == 127) {
/* 431 */         PgenResource pResource = PgenSession.getInstance().getPgenResource();
/* 432 */         pResource.deleteSelectedElements();
/*     */       }
/*     */ 
/* 435 */       return true;
/*     */     }
/*     */ 
/*     */     private List<AbstractDrawableComponent> inPoly(Polygon poly)
/*     */     {
/* 445 */       String pgType = null;
/* 446 */       Iterator it = PgenMultiSelectTool.this.drawingLayer.getActiveLayer().getComponentIterator();
/* 447 */       List adcList = new ArrayList();
/*     */ 
/* 449 */       while (it.hasNext()) {
/* 450 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/*     */ 
/* 452 */         if ((adc instanceof Contours)) {
/* 453 */           adcList.addAll(contourChildrenInPoly((Contours)adc, poly));
/*     */         }
/* 456 */         else if (((pgType == null) && (adc.getPgenCategory().equalsIgnoreCase(this.pgenCat))) || (
/* 457 */           (pgType != null) && (adc.getPgenType().equalsIgnoreCase(pgType)))) {
/* 458 */           List pts = adc.getPoints();
/* 459 */           for (Coordinate pt : pts) {
/* 460 */             double[] pix = PgenMultiSelectTool.this.mapEditor.translateInverseClick(pt);
/* 461 */             if (poly.contains(pix[0], pix[1])) {
/* 462 */               adcList.add(adc);
/* 463 */               if (!this.pgenCat.equalsIgnoreCase("Text")) break;
/* 464 */               pgType = adc.getPgenType();
/* 465 */               PgenMultiSelectTool.this.pgenType = pgType;
/*     */ 
/* 467 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 473 */       return adcList;
/*     */     }
/*     */ 
/*     */     private List<AbstractDrawableComponent> contourChildrenInPoly(Contours con, Polygon poly)
/*     */     {
/* 483 */       Iterator it = con.createDEIterator();
/* 484 */       List adcList = new ArrayList();
/*     */ 
/* 486 */       while (it.hasNext()) {
/* 487 */         DrawableElement de = (DrawableElement)it.next();
/*     */ 
/* 489 */         if (de.getPgenCategory().equalsIgnoreCase(this.pgenCat)) {
/* 490 */           List pts = de.getPoints();
/* 491 */           for (Coordinate pt : pts) {
/* 492 */             double[] pix = PgenMultiSelectTool.this.mapEditor.translateInverseClick(pt);
/* 493 */             if (poly.contains(pix[0], pix[1])) {
/* 494 */               adcList.add(de);
/* 495 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 501 */       return adcList;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenMultiSelectTool
 * JD-Core Version:    0.6.2
 */