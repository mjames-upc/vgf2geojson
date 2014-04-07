/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ public class InterpolationProperties
/*    */ {
/*    */   private int startingTime;
/*    */   private int endingTime;
/*    */   private int interval;
/*    */ 
/*    */   public InterpolationProperties(int startingTime, int endingTime, int interval)
/*    */   {
/* 42 */     this.startingTime = startingTime;
/* 43 */     this.endingTime = endingTime;
/* 44 */     this.interval = interval;
/*    */   }
/*    */ 
/*    */   public int getStartingTime()
/*    */   {
/* 52 */     return this.startingTime;
/*    */   }
/*    */ 
/*    */   public void setStartingTime(int startingTime)
/*    */   {
/* 60 */     this.startingTime = startingTime;
/*    */   }
/*    */ 
/*    */   public int getEndingTime()
/*    */   {
/* 68 */     return this.endingTime;
/*    */   }
/*    */ 
/*    */   public void setEndingTime(int endingTime)
/*    */   {
/* 76 */     this.endingTime = endingTime;
/*    */   }
/*    */ 
/*    */   public int getInterval()
/*    */   {
/* 84 */     return this.interval;
/*    */   }
/*    */ 
/*    */   public void setInterval(int interval)
/*    */   {
/* 92 */     this.interval = interval;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.InterpolationProperties
 * JD-Core Version:    0.6.2
 */