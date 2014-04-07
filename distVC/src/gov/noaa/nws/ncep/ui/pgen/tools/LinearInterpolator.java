/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.vividsolutions.jts.geom.Coordinate;
/*    */ import com.vividsolutions.jts.geom.LineSegment;
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedHashMap;
/*    */ 
/*    */ public class LinearInterpolator
/*    */ {
/* 31 */   private ArrayList<LineSegment> lines = null;
/*    */ 
/*    */   public LinearInterpolator(LinkedHashMap<Coordinate, Coordinate> pointMap)
/*    */   {
/* 38 */     this.lines = new ArrayList();
/* 39 */     for (Coordinate pt1 : pointMap.keySet())
/* 40 */       this.lines.add(new LineSegment(pt1, (Coordinate)pointMap.get(pt1)));
/*    */   }
/*    */ 
/*    */   public ArrayList<Coordinate> interpolate(double fraction)
/*    */   {
/* 55 */     ArrayList newpts = new ArrayList();
/* 56 */     for (LineSegment ls : this.lines) {
/* 57 */       newpts.add(ls.pointAlong(fraction));
/*    */     }
/*    */ 
/* 60 */     return newpts;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.LinearInterpolator
 * JD-Core Version:    0.6.2
 */