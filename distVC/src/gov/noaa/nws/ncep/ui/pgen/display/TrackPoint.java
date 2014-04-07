/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import com.vividsolutions.jts.geom.Coordinate;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PGenRuntimeException;
/*    */ import java.util.Calendar;
/*    */ 
/*    */ public class TrackPoint
/*    */ {
/*    */   private Coordinate location;
/*    */   private Calendar time;
/*    */ 
/*    */   public TrackPoint(Coordinate location, Calendar time)
/*    */   {
/* 40 */     this.location = location;
/* 41 */     this.time = time;
/*    */   }
/*    */ 
/*    */   public Coordinate getLocation()
/*    */   {
/* 49 */     return this.location;
/*    */   }
/*    */ 
/*    */   public void setLocation(Coordinate location)
/*    */   {
/* 57 */     this.location = location;
/*    */   }
/*    */ 
/*    */   public Calendar getTime()
/*    */   {
/* 65 */     return this.time;
/*    */   }
/*    */ 
/*    */   public void setTime(Calendar time)
/*    */   {
/* 73 */     this.time = time;
/*    */   }
/*    */ 
/*    */   public static TrackPoint clone(Coordinate location, Calendar time) {
/* 77 */     if (location == null) {
/* 78 */       throw new PGenRuntimeException("Class: TrackPoint, invalid input paremeter,  Coordinate location is NULL");
/*    */     }
/* 80 */     Coordinate newCoordinate = new Coordinate();
/* 81 */     newCoordinate.x = location.x;
/* 82 */     newCoordinate.y = location.y;
/*    */ 
/* 84 */     Calendar newCalendar = null;
/* 85 */     if (time != null) {
/* 86 */       newCalendar = Calendar.getInstance();
/* 87 */       newCalendar.setTimeInMillis(time.getTimeInMillis());
/*    */     }
/* 89 */     return new TrackPoint(newCoordinate, newCalendar);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.TrackPoint
 * JD-Core Version:    0.6.2
 */