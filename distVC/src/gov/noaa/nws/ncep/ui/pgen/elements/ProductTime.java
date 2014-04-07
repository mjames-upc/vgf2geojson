/*    */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ 
/*    */ public class ProductTime
/*    */ {
/*    */   private Calendar cycle;
/*    */   private Calendar startTime;
/*    */   private Calendar endTime;
/*    */   private ProductRelationship relationship;
/*    */ 
/*    */   public ProductTime()
/*    */   {
/* 32 */     Calendar calendar = Calendar.getInstance();
/* 33 */     this.cycle = calendar;
/* 34 */     this.startTime = calendar;
/* 35 */     this.endTime = calendar;
/* 36 */     this.relationship = ProductRelationship.NORMAL;
/*    */   }
/*    */ 
/*    */   public ProductTime(Calendar calendar)
/*    */   {
/* 43 */     this.cycle = calendar;
/* 44 */     this.startTime = calendar;
/* 45 */     this.endTime = calendar;
/* 46 */     this.relationship = ProductRelationship.NORMAL;
/*    */   }
/*    */ 
/*    */   public Calendar getCycle() {
/* 50 */     return this.cycle;
/*    */   }
/*    */ 
/*    */   public void setCycle(Calendar cycle) {
/* 54 */     this.cycle = cycle;
/*    */   }
/*    */ 
/*    */   public Calendar getStartTime() {
/* 58 */     return this.startTime;
/*    */   }
/*    */ 
/*    */   public void setStartTime(Calendar startTime) {
/* 62 */     this.startTime = startTime;
/*    */   }
/*    */ 
/*    */   public Calendar getEndTime() {
/* 66 */     return this.endTime;
/*    */   }
/*    */ 
/*    */   public void setEndTime(Calendar endTime) {
/* 70 */     this.endTime = endTime;
/*    */   }
/*    */ 
/*    */   public ProductRelationship getRelationship() {
/* 74 */     return this.relationship;
/*    */   }
/*    */ 
/*    */   public void setRelationship(ProductRelationship relationship) {
/* 78 */     this.relationship = relationship;
/*    */   }
/*    */ 
/*    */   public boolean compare(ProductTime prdTime) {
/* 82 */     if ((this.cycle.compareTo(prdTime.cycle) == 0) && 
/* 83 */       (this.startTime.compareTo(prdTime.startTime) == 0) && 
/* 84 */       (this.endTime.compareTo(prdTime.endTime) == 0) && 
/* 85 */       (this.relationship == prdTime.relationship)) {
/* 86 */       return true;
/*    */     }
/*    */ 
/* 89 */     return false;
/*    */   }
/*    */ 
/*    */   public long getRange()
/*    */   {
/* 95 */     return this.endTime.getTimeInMillis() - this.startTime.getTimeInMillis();
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.ProductTime
 * JD-Core Version:    0.6.2
 */