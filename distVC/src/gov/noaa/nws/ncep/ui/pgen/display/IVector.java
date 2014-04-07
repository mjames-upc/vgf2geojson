/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import java.awt.Color;
/*    */ 
/*    */ public abstract interface IVector extends ISinglePoint
/*    */ {
/*    */   public abstract VectorType getVectorType();
/*    */ 
/*    */   public abstract Color getColor();
/*    */ 
/*    */   public abstract Boolean hasBackgroundMask();
/*    */ 
/*    */   public abstract double getSpeed();
/*    */ 
/*    */   public abstract double getDirection();
/*    */ 
/*    */   public abstract double getArrowHeadSize();
/*    */ 
/*    */   public abstract boolean hasDirectionOnly();
/*    */ 
/*    */   public static enum VectorType
/*    */   {
/* 26 */     ARROW, WIND_BARB, HASH_MARK;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.IVector
 * JD-Core Version:    0.6.2
 */