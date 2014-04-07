/*    */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.PGenException;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DeleteAllCommand extends PgenCommand
/*    */ {
/*    */   private List<Product> list;
/*    */   private ArrayList<Product> saveList;
/*    */ 
/*    */   public DeleteAllCommand(List<Product> list)
/*    */   {
/* 39 */     this.list = list;
/*    */   }
/*    */ 
/*    */   public void execute()
/*    */     throws PGenException
/*    */   {
/* 49 */     this.saveList = new ArrayList(this.list);
/* 50 */     this.list.clear();
/*    */   }
/*    */ 
/*    */   public void undo()
/*    */     throws PGenException
/*    */   {
/* 61 */     this.list.addAll(this.saveList);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.DeleteAllCommand
 * JD-Core Version:    0.6.2
 */