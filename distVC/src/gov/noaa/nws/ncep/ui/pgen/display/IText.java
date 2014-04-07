/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import com.vividsolutions.jts.geom.Coordinate;
/*    */ import java.awt.Color;
/*    */ 
/*    */ public abstract interface IText extends ISinglePoint
/*    */ {
/*    */   public abstract String[] getString();
/*    */ 
/*    */   public abstract String getFontName();
/*    */ 
/*    */   public abstract float getFontSize();
/*    */ 
/*    */   public abstract FontStyle getStyle();
/*    */ 
/*    */   public abstract Coordinate getPosition();
/*    */ 
/*    */   public abstract Color getTextColor();
/*    */ 
/*    */   public abstract TextJustification getJustification();
/*    */ 
/*    */   public abstract double getRotation();
/*    */ 
/*    */   public abstract TextRotation getRotationRelativity();
/*    */ 
/*    */   public abstract DisplayType getDisplayType();
/*    */ 
/*    */   public abstract Boolean maskText();
/*    */ 
/*    */   public abstract int getXOffset();
/*    */ 
/*    */   public abstract int getYOffset();
/*    */ 
/*    */   public abstract Boolean getHide();
/*    */ 
/*    */   public abstract Boolean getAuto();
/*    */ 
/*    */   public static enum DisplayType
/*    */   {
/* 50 */     NORMAL, BOX, UNDERLINE, OVERLINE;
/*    */   }
/*    */ 
/*    */   public static enum FontStyle
/*    */   {
/* 45 */     REGULAR, BOLD, ITALIC, BOLD_ITALIC;
/*    */   }
/*    */ 
/*    */   public static enum TextJustification
/*    */   {
/* 36 */     LEFT_JUSTIFY, CENTER, RIGHT_JUSTIFY;
/*    */   }
/*    */ 
/*    */   public static enum TextRotation
/*    */   {
/* 27 */     SCREEN_RELATIVE, NORTH_RELATIVE;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.IText
 * JD-Core Version:    0.6.2
 */