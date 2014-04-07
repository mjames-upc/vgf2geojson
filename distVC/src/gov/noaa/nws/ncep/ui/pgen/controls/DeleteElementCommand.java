/*    */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.PGenException;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DeleteElementCommand extends PgenCommand
/*    */ {
/*    */   private List<Product> list;
/*    */   private DECollection collection;
/*    */   private AbstractDrawableComponent comp;
/*    */ 
/*    */   public DeleteElementCommand(List<Product> list, AbstractDrawableComponent comp)
/*    */   {
/* 47 */     this.list = list;
/* 48 */     this.comp = comp;
/*    */   }
/*    */ 
/*    */   public void execute()
/*    */     throws PGenException
/*    */   {
/*    */     Iterator localIterator2;
/* 59 */     for (Iterator localIterator1 = this.list.iterator(); localIterator1.hasNext(); 
/* 61 */       localIterator2.hasNext())
/*    */     {
/* 59 */       Product currProd = (Product)localIterator1.next();
/*    */ 
/* 61 */       localIterator2 = currProd.getLayers().iterator(); continue; Layer currLayer = (Layer)localIterator2.next();
/*    */ 
/* 63 */       DECollection dec = currLayer.search(this.comp);
/*    */ 
/* 65 */       if (dec != null) {
/* 66 */         this.collection = dec;
/* 67 */         dec.removeElement(this.comp);
/* 68 */         return;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 74 */     throw new PGenException("Could not find specified element in current product list");
/*    */   }
/*    */ 
/*    */   public void undo()
/*    */     throws PGenException
/*    */   {
/* 85 */     this.collection.addElement(this.comp);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.DeleteElementCommand
 * JD-Core Version:    0.6.2
 */