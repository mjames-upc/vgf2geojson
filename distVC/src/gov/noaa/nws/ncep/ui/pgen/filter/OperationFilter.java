/*    */ package gov.noaa.nws.ncep.ui.pgen.filter;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*    */ import gov.noaa.nws.ncep.ui.pgen.annotation.Operation;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*    */ 
/*    */ public class OperationFilter
/*    */   implements ElementFilter
/*    */ {
/*    */   Operation oper;
/*    */ 
/*    */   public OperationFilter(Operation oper)
/*    */   {
/* 28 */     this.oper = oper;
/*    */   }
/*    */ 
/*    */   public boolean accept(AbstractDrawableComponent adc)
/*    */   {
/* 34 */     if (adc.getClass().isAnnotationPresent(ElementOperations.class)) {
/* 35 */       ElementOperations ops = (ElementOperations)adc.getClass().getAnnotation(ElementOperations.class);
/* 36 */       for (Operation op : ops.value()) {
/* 37 */         if (op == this.oper) return true;
/*    */       }
/*    */     }
/*    */ 
/* 41 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.filter.OperationFilter
 * JD-Core Version:    0.6.2
 */