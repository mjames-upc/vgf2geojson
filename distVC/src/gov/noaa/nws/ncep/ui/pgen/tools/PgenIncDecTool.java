/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.IncDecDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.AcceptFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ import java.awt.Polygon;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class PgenIncDecTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   protected void activateTool()
/*     */   {
/*  55 */     super.activateTool();
/*  56 */     if ((this.attrDlg != null) && ((this.attrDlg instanceof IncDecDlg)))
/*  57 */       ((IncDecDlg)this.attrDlg).setTool(this);
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/*  69 */     super.deactivateTool();
/*     */ 
/*  71 */     PgenIncDecHandler pidh = (PgenIncDecHandler)this.mouseHandler;
/*  72 */     if (pidh != null) pidh.cleanup();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  81 */     if (this.mouseHandler == null)
/*     */     {
/*  83 */       this.mouseHandler = new PgenIncDecHandler();
/*     */     }
/*     */ 
/*  87 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public class PgenIncDecHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     private int theFirstMouseX;
/*     */     private int theFirstMouseY;
/*     */     private boolean selectRect;
/*     */     private boolean shiftDown;
/*     */     List<Coordinate> polyPoints;
/*     */ 
/*     */     public PgenIncDecHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 117 */       if (!PgenIncDecTool.this.isResourceEditable()) return false;
/*     */ 
/* 119 */       this.theFirstMouseX = anX;
/* 120 */       this.theFirstMouseY = aY;
/*     */ 
/* 122 */       if (button == 1)
/*     */       {
/* 124 */         return true;
/*     */       }
/*     */ 
/* 127 */       if (button == 3)
/*     */       {
/* 129 */         List txtList = PgenIncDecTool.this.drawingLayer.getAllSelected();
/* 130 */         if (((this.polyPoints == null) || (this.polyPoints.isEmpty())) && ((txtList == null) || (txtList.isEmpty())))
/*     */         {
/* 132 */           if (PgenIncDecTool.this.attrDlg != null) {
/* 133 */             PgenIncDecTool.this.attrDlg.close();
/*     */           }
/*     */ 
/* 136 */           PgenIncDecTool.this.attrDlg = null;
/* 137 */           PgenIncDecTool.this.pgenCategory = null;
/* 138 */           PgenIncDecTool.this.pgenType = null;
/*     */ 
/* 140 */           PgenIncDecTool.this.drawingLayer.removeGhostLine();
/* 141 */           PgenIncDecTool.this.drawingLayer.removeSelected();
/* 142 */           PgenIncDecTool.this.mapEditor.refresh();
/* 143 */           PgenUtil.setSelectingMode();
/*     */         }
/* 147 */         else if ((this.polyPoints == null) || (this.polyPoints.isEmpty()))
/*     */         {
/*     */           try
/*     */           {
/* 153 */             List newTxtList = new ArrayList();
/*     */ 
/* 155 */             for (AbstractDrawableComponent adc : PgenIncDecTool.this.drawingLayer.getAllSelected()) {
/* 156 */               if ((adc instanceof Text)) {
/* 157 */                 Text newTxt = (Text)adc.copy();
/* 158 */                 newTxt.setText(new String[] { String.valueOf(Integer.parseInt(((Text)adc).getText()[0]) + ((IncDecDlg)PgenIncDecTool.this.attrDlg).getInterval()) });
/* 159 */                 newTxtList.add(newTxt);
/*     */               }
/*     */             }
/*     */ 
/* 163 */             List oldList = new ArrayList();
/* 164 */             oldList.addAll(PgenIncDecTool.this.drawingLayer.getAllSelected());
/*     */ 
/* 166 */             PgenIncDecTool.this.drawingLayer.replaceElements(oldList, newTxtList);
/*     */           }
/*     */           catch (NumberFormatException localNumberFormatException)
/*     */           {
/*     */           }
/*     */ 
/* 172 */           PgenIncDecTool.this.drawingLayer.removeSelected();
/* 173 */           PgenIncDecTool.this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 176 */         return false;
/*     */       }
/*     */ 
/* 181 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int anX, int aY, int button)
/*     */     {
/* 195 */       if ((!PgenIncDecTool.this.isResourceEditable()) || (button != 1)) {
/* 196 */         return false;
/*     */       }
/*     */ 
/* 199 */       this.selectRect = true;
/*     */ 
/* 202 */       ArrayList points = new ArrayList();
/*     */ 
/* 204 */       points.add(PgenIncDecTool.this.mapEditor.translateClick(this.theFirstMouseX, this.theFirstMouseY));
/* 205 */       points.add(PgenIncDecTool.this.mapEditor.translateClick(this.theFirstMouseX, aY));
/* 206 */       points.add(PgenIncDecTool.this.mapEditor.translateClick(anX, aY));
/* 207 */       points.add(PgenIncDecTool.this.mapEditor.translateClick(anX, this.theFirstMouseY));
/*     */ 
/* 209 */       DrawableElementFactory def = new DrawableElementFactory();
/* 210 */       Line ghost = (Line)def.create(DrawableType.LINE, null, 
/* 211 */         "Lines", "LINE_SOLID", points, PgenIncDecTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 213 */       ghost.setLineWidth(1.0F);
/* 214 */       ghost.setColors(new Color[] { new Color(255, 255, 255), new Color(255, 255, 255) });
/* 215 */       ghost.setClosed(Boolean.valueOf(true));
/* 216 */       ghost.setSmoothFactor(0);
/*     */ 
/* 218 */       PgenIncDecTool.this.drawingLayer.setGhostLine(ghost);
/*     */ 
/* 220 */       PgenIncDecTool.this.mapEditor.refresh();
/* 221 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseUp(int anX, int aY, int button)
/*     */     {
/* 232 */       if (!PgenIncDecTool.this.isResourceEditable()) return false;
/*     */ 
/* 234 */       if (button == 1)
/*     */       {
/* 236 */         if ((this.shiftDown) && (!this.selectRect)) {
/* 237 */           if (this.polyPoints == null) {
/* 238 */             this.polyPoints = new ArrayList();
/*     */           }
/* 240 */           this.polyPoints.add(new Coordinate(anX, aY));
/*     */         }
/* 243 */         else if (this.selectRect)
/*     */         {
/* 246 */           int[] xpoints = { this.theFirstMouseX, this.theFirstMouseX, anX, anX };
/* 247 */           int[] ypoints = { this.theFirstMouseY, aY, aY, this.theFirstMouseY };
/*     */ 
/* 249 */           Polygon rectangle = new Polygon(xpoints, ypoints, 4);
/*     */ 
/* 251 */           PgenIncDecTool.this.drawingLayer.addSelected(inPoly(rectangle));
/*     */ 
/* 253 */           PgenIncDecTool.this.drawingLayer.removeGhostLine();
/* 254 */           this.selectRect = false;
/*     */         }
/*     */         else
/*     */         {
/* 260 */           Coordinate loc = PgenIncDecTool.this.mapEditor.translateClick(anX, aY);
/* 261 */           if (loc == null) return false;
/*     */ 
/* 264 */           DrawableElement el = PgenIncDecTool.this.drawingLayer.getNearestElement(loc, new AcceptFilter());
/*     */ 
/* 266 */           if (PgenIncDecTool.this.drawingLayer.getAllSelected().contains(el)) {
/* 267 */             PgenIncDecTool.this.drawingLayer.removeSelected(el);
/*     */           }
/* 269 */           else if ((el != null) && ((el instanceof Text))) {
/*     */             try {
/* 271 */               Integer.parseInt(((Text)el).getString()[0]);
/* 272 */               PgenIncDecTool.this.drawingLayer.addSelected(el);
/*     */             }
/*     */             catch (NumberFormatException localNumberFormatException)
/*     */             {
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/* 282 */       else if (button == 3)
/*     */       {
/* 284 */         if (this.polyPoints != null) {
/* 285 */           if (this.polyPoints.size() > 2) {
/* 286 */             int[] xpoints = new int[this.polyPoints.size()];
/* 287 */             int[] ypoints = new int[this.polyPoints.size()];
/* 288 */             for (int ii = 0; ii < this.polyPoints.size(); ii++) {
/* 289 */               xpoints[ii] = ((int)((Coordinate)this.polyPoints.get(ii)).x);
/* 290 */               ypoints[ii] = ((int)((Coordinate)this.polyPoints.get(ii)).y);
/*     */             }
/*     */ 
/* 293 */             Polygon poly = new Polygon(xpoints, ypoints, this.polyPoints.size());
/* 294 */             PgenIncDecTool.this.drawingLayer.addSelected(inPoly(poly));
/*     */ 
/* 296 */             PgenIncDecTool.this.drawingLayer.removeGhostLine();
/*     */           }
/*     */ 
/* 300 */           this.polyPoints.clear();
/* 301 */           this.shiftDown = false;
/*     */         }
/*     */       }
/*     */ 
/* 305 */       PgenIncDecTool.this.mapEditor.setFocus();
/* 306 */       PgenIncDecTool.this.mapEditor.refresh();
/*     */ 
/* 308 */       return true;
/*     */     }
/*     */ 
/*     */     private List<Text> inPoly(Polygon poly)
/*     */     {
/* 318 */       Iterator it = PgenIncDecTool.this.drawingLayer.getActiveLayer().getComponentIterator();
/* 319 */       List txtList = new ArrayList();
/*     */ 
/* 321 */       while (it.hasNext()) {
/* 322 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/*     */ 
/* 324 */         if ((adc instanceof Text)) {
/* 325 */           Coordinate pt = ((Text)adc).getLocation();
/* 326 */           double[] pix = PgenIncDecTool.this.mapEditor.translateInverseClick(pt);
/* 327 */           if (poly.contains(pix[0], pix[1])) {
/*     */             try {
/* 329 */               Integer.parseInt(((Text)adc).getString()[0]);
/* 330 */               txtList.add((Text)adc);
/*     */             }
/*     */             catch (NumberFormatException localNumberFormatException)
/*     */             {
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 340 */       return txtList;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int anX, int aY)
/*     */     {
/* 345 */       if (!PgenIncDecTool.this.isResourceEditable()) return false;
/*     */ 
/* 348 */       if ((this.polyPoints != null) && (!this.polyPoints.isEmpty()))
/*     */       {
/* 350 */         this.polyPoints.add(new Coordinate(anX, aY));
/*     */ 
/* 352 */         if (this.polyPoints.size() > 1)
/*     */         {
/* 354 */           ArrayList points = new ArrayList();
/*     */ 
/* 356 */           for (Coordinate loc : this.polyPoints) {
/* 357 */             points.add(PgenIncDecTool.this.mapEditor.translateClick(loc.x, loc.y));
/*     */           }
/*     */ 
/* 360 */           DrawableElementFactory def = new DrawableElementFactory();
/* 361 */           Line ghost = (Line)def.create(DrawableType.LINE, null, 
/* 362 */             "Lines", "LINE_SOLID", points, PgenIncDecTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 364 */           ghost.setLineWidth(1.0F);
/* 365 */           ghost.setColors(new Color[] { new Color(255, 255, 255), new Color(255, 255, 255) });
/* 366 */           ghost.setClosed(Boolean.valueOf(true));
/* 367 */           ghost.setSmoothFactor(0);
/*     */ 
/* 369 */           PgenIncDecTool.this.drawingLayer.setGhostLine(ghost);
/*     */         }
/*     */ 
/* 372 */         PgenIncDecTool.this.mapEditor.refresh();
/*     */ 
/* 374 */         this.polyPoints.remove(this.polyPoints.size() - 1);
/*     */       }
/* 376 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleKeyDown(int keyCode)
/*     */     {
/* 381 */       if (!PgenIncDecTool.this.isResourceEditable()) return false;
/*     */ 
/* 383 */       if (keyCode == 131072) {
/* 384 */         this.shiftDown = true;
/*     */       }
/* 386 */       else if (keyCode == 127) {
/* 387 */         PgenResource pResource = PgenSession.getInstance().getPgenResource();
/* 388 */         pResource.deleteSelectedElements();
/*     */       }
/* 390 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleKeyUp(int keyCode)
/*     */     {
/* 395 */       if (!PgenIncDecTool.this.isResourceEditable()) return false;
/*     */ 
/* 397 */       if (keyCode == 131072) {
/* 398 */         this.shiftDown = false;
/*     */       }
/* 400 */       return true;
/*     */     }
/*     */ 
/*     */     public void cleanup() {
/* 404 */       PgenIncDecTool.this.drawingLayer.removeSelected();
/* 405 */       PgenIncDecTool.this.mapEditor.refresh();
/* 406 */       this.selectRect = false;
/* 407 */       this.shiftDown = false;
/* 408 */       if (this.polyPoints != null) this.polyPoints.clear();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenIncDecTool
 * JD-Core Version:    0.6.2
 */