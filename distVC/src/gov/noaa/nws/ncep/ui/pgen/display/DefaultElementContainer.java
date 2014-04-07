/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*    */ import com.raytheon.uf.viz.core.drawables.PaintProperties;
/*    */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*    */ import gov.noaa.nws.ncep.ui.pgen.gfa.IGfa;
/*    */ 
/*    */ public class DefaultElementContainer extends AbstractElementContainer
/*    */ {
/* 27 */   private DisplayProperties saveProps = null;
/* 28 */   private float zoomLevel = 0.0F;
/*    */ 
/*    */   public DefaultElementContainer(DrawableElement element, IMapDescriptor mapDescriptor, IGraphicsTarget target)
/*    */   {
/* 37 */     super(element, mapDescriptor, target);
/*    */   }
/*    */ 
/*    */   public void draw(IGraphicsTarget target, PaintProperties paintProps, DisplayProperties dprops)
/*    */   {
/* 49 */     draw(target, paintProps, dprops, false);
/*    */   }
/*    */ 
/*    */   public void draw(IGraphicsTarget target, PaintProperties paintProps, DisplayProperties dprops, boolean needsCreate)
/*    */   {
/* 61 */     if ((this.displayEls == null) || (paintProps.isZooming())) needsCreate = true;
/*    */ 
/* 63 */     if (paintProps.getZoomLevel() != this.zoomLevel) {
/* 64 */       needsCreate = true;
/* 65 */       this.zoomLevel = paintProps.getZoomLevel();
/*    */     }
/*    */ 
/* 68 */     if ((dprops != null) && (!dprops.equals(this.saveProps))) {
/* 69 */       this.def.setLayerDisplayAttr(dprops.getLayerMonoColor(), dprops.getLayerColor(), dprops.getLayerFilled());
/* 70 */       needsCreate = true;
/*    */     }
/* 72 */     else if (((this.element instanceof IMidCloudText)) || ((this.element instanceof IAvnText)) || 
/* 73 */       (((this.element instanceof IText)) && 
/* 74 */       (((IText)this.element).getDisplayType().equals(IText.DisplayType.BOX))) || 
/* 75 */       ((this.element instanceof IGfa))) {
/* 76 */       needsCreate = true;
/*    */     }
/*    */ 
/* 79 */     if (needsCreate) createDisplayables(paintProps);
/* 80 */     this.saveProps = dprops;
/*    */ 
/* 82 */     for (IDisplayable each : this.displayEls)
/* 83 */       each.draw(target, paintProps);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.DefaultElementContainer
 * JD-Core Version:    0.6.2
 */