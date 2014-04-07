/*    */ package gov.noaa.nws.ncep.ui.pgen;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class PgenClipboard
/*    */ {
/* 34 */   private static PgenClipboard instance = null;
/*    */ 
/* 39 */   private List<AbstractDrawableComponent> elSelected = null;
/*    */ 
/*    */   public static synchronized PgenClipboard getInstance()
/*    */   {
/* 54 */     if (instance == null) instance = new PgenClipboard();
/* 55 */     return instance;
/*    */   }
/*    */ 
/*    */   public void setElSelected(List<AbstractDrawableComponent> elSelected)
/*    */   {
/* 62 */     this.elSelected = elSelected;
/*    */   }
/*    */ 
/*    */   public List<AbstractDrawableComponent> getElSelected()
/*    */   {
/* 69 */     return this.elSelected;
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/* 76 */     this.elSelected.clear();
/*    */   }
/*    */ 
/*    */   public void copy(List<AbstractDrawableComponent> elSelected)
/*    */   {
/* 85 */     if (this.elSelected == null) {
/* 86 */       this.elSelected = new ArrayList();
/*    */     }
/*    */ 
/* 89 */     clear();
/* 90 */     for (AbstractDrawableComponent el : elSelected)
/* 91 */       this.elSelected.add(el.copy());
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.PgenClipboard
 * JD-Core Version:    0.6.2
 */