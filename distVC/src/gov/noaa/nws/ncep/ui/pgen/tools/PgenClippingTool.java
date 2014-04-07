/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.clipper.ClipProduct;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureDialog;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ public class PgenClippingTool extends AbstractPgenTool
/*     */ {
/*     */   private static HashMap<String, Polygon> bounds;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  57 */     super.activateTool();
/*  58 */     if (!isResourceEditable()) return;
/*     */ 
/*  60 */     this.drawingLayer.removeGhostLine();
/*  61 */     this.drawingLayer.removeSelected();
/*     */ 
/*  63 */     String pdName = this.drawingLayer.getActiveProduct().getType();
/*  64 */     ProductType pt = (ProductType)ProductConfigureDialog.getProductTypes().get(pdName);
/*     */ 
/*  66 */     Polygon boundsPoly = null;
/*     */ 
/*  68 */     if ((pt != null) && (pt.getClipFlag() != null) && (pt.getClipFlag().booleanValue())) {
/*  69 */       boundsPoly = getBoundsPoly(pt.getClipBoundsTable(), pt.getClipBoundsName());
/*  70 */       if (boundsPoly != null) {
/*  71 */         processClip(boundsPoly);
/*     */       }
/*     */     }
/*     */ 
/*  75 */     PgenUtil.setSelectingMode();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */   private void processClip(Polygon boundsPoly)
/*     */   {
/* 100 */     List before = new ArrayList();
/* 101 */     for (AbstractDrawableComponent adc : this.drawingLayer.getActiveLayer().getDrawables()) {
/* 102 */       before.add(adc.copy());
/*     */     }
/*     */ 
/* 105 */     List clipped = new ClipProduct(boundsPoly, true).clipDrawableComponents(before);
/* 106 */     Object old = new ArrayList(this.drawingLayer.getActiveLayer().getDrawables());
/*     */ 
/* 108 */     this.drawingLayer.replaceElements((List)old, clipped);
/*     */   }
/*     */ 
/*     */   private Polygon getBoundsPoly(String boundsTbl, String boundsName)
/*     */   {
/* 119 */     if (bounds == null) {
/* 120 */       bounds = new HashMap();
/*     */     }
/*     */ 
/* 124 */     Polygon boundsPoly = (Polygon)bounds.get(boundsTbl + boundsName);
/*     */ 
/* 127 */     if (boundsPoly == null) {
/* 128 */       boundsPoly = PgenStaticDataProvider.getProvider().loadBounds(boundsTbl, boundsName);
/* 129 */       if (boundsPoly != null)
/*     */       {
/* 131 */         bounds.clear();
/* 132 */         bounds.put(boundsTbl + boundsName, boundsPoly);
/*     */       }
/*     */     }
/*     */ 
/* 136 */     return boundsPoly;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenClippingTool
 * JD-Core Version:    0.6.2
 */