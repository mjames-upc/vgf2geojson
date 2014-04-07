/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.Calendar;
/*    */ 
/*    */ public abstract interface ITrack extends ILine
/*    */ {
/*    */   public abstract IText.FontStyle getFontStyle();
/*    */ 
/*    */   public abstract boolean[] getExtraPointTimeTextDisplayIndicator();
/*    */ 
/*    */   public abstract ExtraPointTimeDisplayOption getExtraPointTimeDisplayOption();
/*    */ 
/*    */   public abstract Color getInitialColor();
/*    */ 
/*    */   public abstract String getInitialLinePattern();
/*    */ 
/*    */   public abstract String getInitialMarker();
/*    */ 
/*    */   public abstract TrackPoint[] getInitialPoints();
/*    */ 
/*    */   public abstract Color getExtrapColor();
/*    */ 
/*    */   public abstract String getExtrapLinePattern();
/*    */ 
/*    */   public abstract String getExtrapMarker();
/*    */ 
/*    */   public abstract TrackPoint[] getExtrapPoints();
/*    */ 
/*    */   public abstract String getFontName();
/*    */ 
/*    */   public abstract float getFontSize();
/*    */ 
/*    */   public abstract Calendar getFirstTimeCalendar();
/*    */ 
/*    */   public abstract Calendar getSecondTimeCalendar();
/*    */ 
/*    */   public abstract boolean isSetTimeButtonSelected();
/*    */ 
/*    */   public abstract int getExtraDrawingPointNumber();
/*    */ 
/*    */   public abstract IText.FontStyle getStyle();
/*    */ 
/*    */   public abstract String getSkipFactorText();
/*    */ 
/*    */   public abstract int getFontNameComboSelectedIndex();
/*    */ 
/*    */   public abstract int getFontSizeComboSelectedIndex();
/*    */ 
/*    */   public abstract int getFontStyleComboSelectedIndex();
/*    */ 
/*    */   public abstract int getUnitComboSelectedIndex();
/*    */ 
/*    */   public abstract int getRoundComboSelectedIndex();
/*    */ 
/*    */   public abstract int getRoundDirComboSelectedIndex();
/*    */ 
/*    */   public abstract String getIntervalTimeString();
/*    */ 
/*    */   public static enum ExtraPointTimeDisplayOption
/*    */   {
/* 25 */     SKIP_FACTOR, SHOW_FIRST_LAST, ON_ONE_HOUR, ON_HALF_HOUR;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.ITrack
 * JD-Core Version:    0.6.2
 */