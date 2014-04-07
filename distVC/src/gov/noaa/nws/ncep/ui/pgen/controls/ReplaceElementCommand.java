/*    */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.PGenException;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ReplaceElementCommand extends PgenCommand
/*    */ {
/*    */   private List<Product> list;
/*    */   private Layer layer;
/*    */   private AbstractDrawableComponent oldElement;
/*    */   private AbstractDrawableComponent newElement;
/*    */ 
/*    */   public ReplaceElementCommand(List<Product> list, AbstractDrawableComponent oldElement, AbstractDrawableComponent newElement)
/*    */   {
/* 53 */     this.list = list;
/* 54 */     this.oldElement = oldElement;
/* 55 */     this.newElement = newElement;
/*    */   }
/*    */ 
/*    */   public void execute()
/*    */     throws PGenException
/*    */   {
/*    */     Iterator localIterator2;
/* 66 */     for (Iterator localIterator1 = this.list.iterator(); localIterator1.hasNext(); 
/* 68 */       localIterator2.hasNext())
/*    */     {
/* 66 */       Product currProd = (Product)localIterator1.next();
/*    */ 
/* 68 */       localIterator2 = currProd.getLayers().iterator(); continue; Layer currLayer = (Layer)localIterator2.next();
/*    */ 
/* 70 */       if (currLayer.replace(this.oldElement, this.newElement)) {
/* 71 */         this.layer = currLayer;
/* 72 */         return;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 78 */     throw new PGenException("Could not find specified element in current product list");
/*    */   }
/*    */ 
/*    */   public void undo()
/*    */     throws PGenException
/*    */   {
/* 89 */     if (this.layer.replace(this.newElement, this.oldElement)) {
/* 90 */       return;
/*    */     }
/*    */ 
/* 93 */     throw new PGenException("Could not find original element in current product list for undo");
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.ReplaceElementCommand
 * JD-Core Version:    0.6.2
 */