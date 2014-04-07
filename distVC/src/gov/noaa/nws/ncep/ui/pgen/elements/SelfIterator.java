/*    */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class SelfIterator
/*    */   implements Iterator<DrawableElement>
/*    */ {
/*    */   private DrawableElement de;
/*    */   boolean nextFlag;
/*    */ 
/*    */   public SelfIterator(DrawableElement de)
/*    */   {
/* 34 */     this.de = de;
/* 35 */     if (de != null) this.nextFlag = true;
/*    */   }
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 40 */     return this.nextFlag;
/*    */   }
/*    */ 
/*    */   public DrawableElement next()
/*    */   {
/* 45 */     if (hasNext()) {
/* 46 */       this.nextFlag = false;
/* 47 */       return this.de;
/*    */     }
/*    */ 
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   public void remove()
/*    */   {
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.SelfIterator
 * JD-Core Version:    0.6.2
 */