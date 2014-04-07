/*    */ package gov.noaa.nws.ncep.ui.pgen.filter;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class ElementFilterCollection
/*    */   implements ElementFilter
/*    */ {
/*    */   Set<ElementFilter> filters;
/*    */ 
/*    */   public ElementFilterCollection()
/*    */   {
/* 26 */     this.filters = new HashSet();
/*    */   }
/*    */ 
/*    */   public ElementFilterCollection(ElementFilter filter)
/*    */   {
/* 33 */     this();
/* 34 */     addFilter(filter);
/*    */   }
/*    */ 
/*    */   public void addFilter(ElementFilter filter) {
/* 38 */     this.filters.add(filter);
/*    */   }
/*    */ 
/*    */   public void removeFilter(ElementFilter filter) {
/* 42 */     if (this.filters.contains(filter))
/* 43 */       this.filters.remove(filter);
/*    */   }
/*    */ 
/*    */   public boolean accept(AbstractDrawableComponent adc)
/*    */   {
/* 51 */     for (ElementFilter f : this.filters) {
/* 52 */       if (!f.accept(adc)) return false;
/*    */     }
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean acceptOnce(AbstractDrawableComponent adc)
/*    */   {
/* 67 */     if (this.filters.isEmpty()) return true;
/*    */ 
/* 69 */     for (ElementFilter f : this.filters) {
/* 70 */       if (f.accept(adc)) return true;
/*    */     }
/*    */ 
/* 73 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.filter.ElementFilterCollection
 * JD-Core Version:    0.6.2
 */