/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.OutlookAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class PgenOutlookDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   private Outlook otlk;
/*     */   private String lbl;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  64 */     super.activateTool();
/*     */ 
/*  66 */     if ((this.attrDlg != null) && (!isDelObj())) {
/*  67 */       ((OutlookAttrDlg)this.attrDlg).enableAddDel(false);
/*     */ 
/*  70 */       String layer = this.drawingLayer.getActiveLayer().getName();
/*  71 */       boolean setName = false;
/*  72 */       if ((layer != null) && (!layer.isEmpty()) && (!layer.equalsIgnoreCase("Default"))) {
/*  73 */         ((OutlookAttrDlg)this.attrDlg).setOtlkType(layer);
/*     */ 
/*  75 */         setName = true;
/*     */       }
/*     */ 
/*  78 */       if (this.otlk != null)
/*     */       {
/*  80 */         if (!setName)
/*  81 */           ((OutlookAttrDlg)this.attrDlg).setOtlkType(this.otlk.getOutlookType());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/* 105 */     super.deactivateTool();
/*     */ 
/* 107 */     PgenOutlookDrawingHandler mph = (PgenOutlookDrawingHandler)this.mouseHandler;
/* 108 */     if (mph != null) mph.clearPoints();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 118 */     if (this.mouseHandler == null)
/*     */     {
/* 120 */       this.mouseHandler = new PgenOutlookDrawingHandler();
/*     */     }
/*     */ 
/* 124 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   private Outlook getCurrentOtlk(String type)
/*     */   {
/* 325 */     Outlook ol = null;
/*     */ 
/* 327 */     Iterator it = this.drawingLayer.getActiveLayer().getComponentIterator();
/* 328 */     while (it.hasNext()) {
/* 329 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 330 */       if (((adc instanceof Outlook)) && (adc.getPgenType().equalsIgnoreCase(type))) {
/* 331 */         ol = (Outlook)adc;
/* 332 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 336 */     return ol;
/*     */   }
/*     */ 
/*     */   public class PgenOutlookDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/* 139 */     protected ArrayList<Coordinate> points = new ArrayList();
/*     */     protected DrawableElement elem;
/* 150 */     protected DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/*     */     public PgenOutlookDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 160 */       if (!PgenOutlookDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 162 */       DECollection dec = null;
/*     */ 
/* 165 */       Coordinate loc = PgenOutlookDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 166 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 168 */       if (button == 1)
/*     */       {
/* 170 */         this.points.add(loc);
/*     */ 
/* 172 */         return true;
/*     */       }
/*     */ 
/* 175 */       if (button == 3)
/*     */       {
/* 177 */         if (this.points.size() == 0)
/*     */         {
/* 179 */           PgenOutlookDrawingTool.this.attrDlg.close();
/* 180 */           PgenOutlookDrawingTool.this.attrDlg = null;
/* 181 */           PgenUtil.setSelectingMode();
/*     */         }
/* 184 */         else if (this.points.size() < 2)
/*     */         {
/* 186 */           PgenOutlookDrawingTool.this.drawingLayer.removeGhostLine();
/* 187 */           this.points.clear();
/*     */ 
/* 189 */           PgenOutlookDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */         else
/*     */         {
/* 193 */           String lineType = ((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).getLineType();
/*     */ 
/* 200 */           this.elem = ((DrawableElement)this.def.create(DrawableType.LINE, PgenOutlookDrawingTool.this.attrDlg, 
/* 201 */             "Lines", lineType, this.points, PgenOutlookDrawingTool.this.drawingLayer.getActiveLayer()));
/*     */ 
/* 204 */           dec = new DECollection(Outlook.OUTLOOK_LABELED_LINE);
/* 205 */           dec.setPgenCategory("MET");
/*     */ 
/* 207 */           PgenOutlookDrawingTool.this.otlk = PgenOutlookDrawingTool.this.getCurrentOtlk(((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).getOutlookType());
/*     */ 
/* 209 */           Outlook newOtlk = this.def.createOutlook(((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).getOutlookType(), 
/* 210 */             this.elem, dec, PgenOutlookDrawingTool.this.otlk);
/*     */ 
/* 212 */           newOtlk.update((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg);
/*     */ 
/* 215 */           if ((PgenOutlookDrawingTool.this.otlk == null) || (!PgenOutlookDrawingTool.this.otlk.getPgenType().equalsIgnoreCase(((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).getOutlookType()))) {
/* 216 */             PgenOutlookDrawingTool.this.drawingLayer.addElement(newOtlk);
/*     */           }
/*     */           else {
/* 219 */             PgenOutlookDrawingTool.this.drawingLayer.replaceElement(PgenOutlookDrawingTool.this.otlk, newOtlk);
/*     */           }
/*     */ 
/* 222 */           PgenOutlookDrawingTool.this.otlk = newOtlk;
/*     */ 
/* 224 */           PgenOutlookDrawingTool.this.attrDlg.setDrawableElement(this.elem);
/*     */ 
/* 226 */           PgenOutlookDrawingTool.this.drawingLayer.removeGhostLine();
/* 227 */           this.points.clear();
/*     */ 
/* 229 */           PgenOutlookDrawingTool.this.mapEditor.refresh();
/*     */ 
/* 233 */           if (((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).addLabel()) {
/* 234 */             if (((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).addText()) {
/* 235 */               PgenOutlookDrawingTool.this.lbl = ((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).getLblTxt();
/* 236 */               PgenUtil.setDrawingTextMode(true, ((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).useLineColor(), 
/* 237 */                 PgenOutlookDrawingTool.this.lbl, dec);
/*     */             }
/* 239 */             else if (((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).addSymbol())
/*     */             {
/* 241 */               PgenUtil.setDrawingSymbolMode(((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).getSymbolCat(), 
/* 242 */                 ((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).getSymbolType(), 
/* 243 */                 ((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).useLineColor(), dec);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 248 */         return true;
/*     */       }
/*     */ 
/* 251 */       if (button == 2)
/*     */       {
/* 253 */         return true;
/*     */       }
/*     */ 
/* 258 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 272 */       if (!PgenOutlookDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 275 */       Coordinate loc = PgenOutlookDrawingTool.this.mapEditor.translateClick(x, y);
/* 276 */       if (loc == null) return false;
/*     */ 
/* 279 */       AbstractDrawableComponent ghost = null;
/* 280 */       String lineType = ((OutlookAttrDlg)PgenOutlookDrawingTool.this.attrDlg).getLineType();
/*     */ 
/* 287 */       ghost = this.def.create(DrawableType.LINE, PgenOutlookDrawingTool.this.attrDlg, 
/* 288 */         "Lines", lineType, this.points, PgenOutlookDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 290 */       if (((ILine)PgenOutlookDrawingTool.this.attrDlg).isFilled().booleanValue()) ((Line)ghost).setFillPattern(((ILine)PgenOutlookDrawingTool.this.attrDlg).getFillPattern());
/*     */ 
/* 292 */       if ((this.points != null) && (this.points.size() >= 1))
/*     */       {
/* 294 */         ArrayList ghostPts = new ArrayList(this.points);
/* 295 */         ghostPts.add(loc);
/* 296 */         Line ln = (Line)ghost;
/* 297 */         ln.setLinePoints(new ArrayList(ghostPts));
/*     */ 
/* 299 */         PgenOutlookDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 300 */         PgenOutlookDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 304 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 310 */       if ((!PgenOutlookDrawingTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 311 */       return true;
/*     */     }
/*     */ 
/*     */     public void clearPoints() {
/* 315 */       this.points.clear();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenOutlookDrawingTool
 * JD-Core Version:    0.6.2
 */