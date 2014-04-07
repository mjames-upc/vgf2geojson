/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*    */ import com.raytheon.uf.viz.core.drawables.PaintProperties;
/*    */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*    */ 
/*    */ public class RasterElementContainer extends AbstractElementContainer
/*    */ {
/* 25 */   private DisplayProperties saveProps = null;
/*    */ 
/*    */   public RasterElementContainer(DrawableElement element, IMapDescriptor mapDescriptor, IGraphicsTarget target)
/*    */   {
/* 34 */     super(element, mapDescriptor, target);
/*    */   }
/*    */ 
/*    */   public void draw(IGraphicsTarget target, PaintProperties paintProps, DisplayProperties dprops)
/*    */   {
/* 46 */     draw(target, paintProps, dprops, false);
/*    */   }
/*    */ 
/*    */   public void draw(IGraphicsTarget target, PaintProperties paintProps, DisplayProperties dprops, boolean needsCreate)
/*    */   {
/* 58 */     if (this.displayEls == null) needsCreate = true;
/*    */ 
/* 60 */     if ((dprops != null) && (!dprops.equals(this.saveProps))) {
/* 61 */       this.def.setLayerDisplayAttr(dprops.getLayerMonoColor(), dprops.getLayerColor(), dprops.getLayerFilled());
/* 62 */       needsCreate = true;
/*    */     }
/*    */ 
/* 65 */     if (needsCreate) createDisplayables(paintProps);
/* 66 */     this.saveProps = dprops;
/*    */ 
/* 68 */     for (IDisplayable each : this.displayEls)
/* 69 */       each.draw(target, paintProps);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.RasterElementContainer
 * JD-Core Version:    0.6.2
 */