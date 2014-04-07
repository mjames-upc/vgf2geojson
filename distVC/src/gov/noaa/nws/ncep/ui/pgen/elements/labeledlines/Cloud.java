/*    */ package gov.noaa.nws.ncep.ui.pgen.elements.labeledlines;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.FLIP, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.INTERPOLATE})
/*    */ public class Cloud extends LabeledLine
/*    */ {
/*    */   public Cloud(String name)
/*    */   {
/* 41 */     super(name);
/*    */   }
/*    */ 
/*    */   public LabeledLine copy()
/*    */   {
/* 49 */     Cloud ll = new Cloud(getName());
/* 50 */     ll.setParent(this.parent);
/* 51 */     ll.setPgenCategory(this.pgenCategory);
/* 52 */     ll.setPgenType(this.pgenType);
/*    */ 
/* 54 */     Iterator it = getComponentIterator();
/* 55 */     while (it.hasNext()) {
/* 56 */       ll.add(((AbstractDrawableComponent)it.next()).copy());
/*    */     }
/*    */ 
/* 59 */     return ll;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Cloud
 * JD-Core Version:    0.6.2
 */