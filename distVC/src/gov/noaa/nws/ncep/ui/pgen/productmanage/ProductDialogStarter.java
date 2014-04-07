/*    */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*    */ 
/*    */ public class ProductDialogStarter
/*    */   implements Runnable
/*    */ {
/*  7 */   PgenResource pgen = null;
/*    */ 
/*    */   public ProductDialogStarter(PgenResource pgen)
/*    */   {
/* 13 */     this.pgen = pgen;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 18 */     this.pgen.startProductManage();
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.ProductDialogStarter
 * JD-Core Version:    0.6.2
 */