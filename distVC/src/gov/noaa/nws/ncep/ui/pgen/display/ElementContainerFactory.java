/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*    */ import com.raytheon.uf.viz.core.map.MapDescriptor;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*    */ 
/*    */ public class ElementContainerFactory
/*    */ {
/*    */   public static AbstractElementContainer createContainer(DrawableElement el, MapDescriptor descriptor, IGraphicsTarget target)
/*    */   {
/* 32 */     if ((el instanceof Symbol)) {
/* 33 */       return new RasterElementContainer(el, descriptor, target);
/*    */     }
/* 35 */     return new DefaultElementContainer(el, descriptor, target);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.ElementContainerFactory
 * JD-Core Version:    0.6.2
 */