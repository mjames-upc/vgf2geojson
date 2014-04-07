/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ public abstract interface IAvnText extends IText
/*    */ {
/*    */   public abstract AviationTextType getAvnTextType();
/*    */ 
/*    */   public abstract String getSymbolPatternName();
/*    */ 
/*    */   public abstract boolean hasSymbolPattern();
/*    */ 
/*    */   public abstract String getTopValue();
/*    */ 
/*    */   public abstract String getBottomValue();
/*    */ 
/*    */   public abstract boolean hasBottomValue();
/*    */ 
/*    */   public static enum AviationTextType
/*    */   {
/* 18 */     LOW_PRESSURE_BOX, HIGH_PRESSURE_BOX, FLIGHT_LEVEL, 
/* 19 */     LOW_LEVEL_TURBULENCE, HIGH_LEVEL_TURBULENCE, 
/* 20 */     CLOUD_LEVEL, FREEZING_LEVEL, MID_LEVEL_ICING;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.IAvnText
 * JD-Core Version:    0.6.2
 */