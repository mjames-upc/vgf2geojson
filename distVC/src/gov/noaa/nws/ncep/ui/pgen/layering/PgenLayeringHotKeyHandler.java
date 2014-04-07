/*    */ package gov.noaa.nws.ncep.ui.pgen.layering;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*    */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*    */ import java.util.List;
/*    */ import org.eclipse.core.commands.AbstractHandler;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ 
/*    */ public class PgenLayeringHotKeyHandler extends AbstractHandler
/*    */ {
/*    */   public Object execute(ExecutionEvent event)
/*    */     throws ExecutionException
/*    */   {
/* 27 */     String layerIndexStr = event.getParameter("layerIndex");
/* 28 */     if ((layerIndexStr == null) || (layerIndexStr.isEmpty())) {
/* 29 */       return null;
/*    */     }
/*    */ 
/* 32 */     int layerIndex = Integer.parseInt(layerIndexStr);
/*    */ 
/* 34 */     PgenResource pgenResource = PgenUtil.findPgenResource(PgenUtil.getActiveEditor());
/* 35 */     if ((pgenResource != null) && (layerIndex > 0)) {
/* 36 */       Product activeProduct = pgenResource.getActiveProduct();
/* 37 */       int layerListSize = activeProduct.getLayers().size();
/*    */ 
/* 39 */       if (layerListSize >= layerIndex) {
/* 40 */         Layer layerToActivate = activeProduct.getLayer(layerIndex - 1);
/* 41 */         layerToActivate.setOnOff(true);
/* 42 */         pgenResource.setActiveLayer(layerToActivate);
/*    */ 
/* 44 */         for (int index = 0; index < layerListSize; index++)
/* 45 */           if (index != layerIndex - 1)
/*    */           {
/* 49 */             Layer currentLayer = pgenResource.getActiveProduct().getLayer(index);
/* 50 */             currentLayer.setOnOff(false);
/*    */           }
/* 52 */         PgenUtil.refresh();
/*    */       }
/*    */     }
/*    */ 
/* 56 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.layering.PgenLayeringHotKeyHandler
 * JD-Core Version:    0.6.2
 */