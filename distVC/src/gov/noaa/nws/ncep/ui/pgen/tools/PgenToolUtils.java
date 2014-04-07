/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo;
/*     */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*     */ 
/*     */ public class PgenToolUtils
/*     */ {
/*     */   public static AbstractDrawableComponent extrapElement(AbstractDrawableComponent adc, double dir, double dist)
/*     */   {
/*  60 */     AbstractDrawableComponent newEl = null;
/*     */ 
/*  62 */     if (adc != null)
/*     */     {
/*  64 */       newEl = adc.copy();
/*  65 */       Iterator it = newEl.createDEIterator();
/*  66 */       while (it.hasNext()) {
/*  67 */         DrawableElement de = (DrawableElement)it.next();
/*  68 */         de.setPointsOnly(extrapPoints(de.getPoints(), dir, dist, adc));
/*     */       }
/*     */ 
/*  74 */       if ((adc instanceof Gfa)) {
/*  75 */         ArrayList pts = new ArrayList();
/*  76 */         pts.add(((Gfa)adc).getGfaTextCoordinate());
/*  77 */         ArrayList expPts = extrapPoints(pts, dir, dist, adc);
/*  78 */         ((Gfa)newEl).setGfaTextCoordinate((Coordinate)expPts.get(0));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  83 */     return newEl;
/*     */   }
/*     */ 
/*     */   private static ArrayList<Coordinate> extrapPoints(List<Coordinate> points, double direction, double distance, AbstractDrawableComponent adc)
/*     */   {
/*  98 */     ArrayList newPoints = null;
/*     */ 
/* 100 */     if ((points != null) && (points.size() > 0))
/*     */     {
/* 102 */       newPoints = new ArrayList();
/*     */ 
/* 104 */       GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*     */ 
/* 106 */       for (Coordinate pt : points)
/*     */       {
/* 113 */         gc.setStartingGeographicPoint(pt.x, pt.y);
/*     */ 
/* 115 */         gc.setDirection(direction - 180.0D, distance);
/*     */ 
/* 117 */         Point2D newP = gc.getDestinationGeographicPoint();
/* 118 */         newPoints.add(new Coordinate(newP.getX(), newP.getY()));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 123 */     if (SigmetInfo.isSnapADC(adc)) {
/* 124 */       int numOfCompassPts = SigmetInfo.getNumOfCompassPts(adc);
/* 125 */       return SnapUtil.getSnapWithStation(newPoints, SnapUtil.VOR_STATION_LIST, 10, numOfCompassPts);
/*     */     }
/* 127 */     return newPoints;
/*     */   }
/*     */ 
/*     */   public static AbstractDrawableComponent createReversedDrawableElement(AbstractDrawableComponent drawableElement)
/*     */   {
/* 141 */     if (drawableElement == null)
/* 142 */       return null;
/* 143 */     AbstractDrawableComponent drawableElementWithReversedPoints = drawableElement.copy();
/* 144 */     if (drawableElementWithReversedPoints.getPgenCategory().equals("Front"))
/* 145 */       ((Line)drawableElementWithReversedPoints).setFlipSide(!((Line)drawableElementWithReversedPoints).isFlipSide());
/*     */     else
/* 147 */       reverseElementCoordinatePoints(drawableElementWithReversedPoints.getPrimaryDE());
/* 148 */     return drawableElementWithReversedPoints;
/*     */   }
/*     */ 
/*     */   private static void reverseElementCoordinatePoints(DrawableElement selectedDrawableElement)
/*     */   {
/* 158 */     if (selectedDrawableElement == null)
/* 159 */       return;
/* 160 */     List drawableElementPointList = selectedDrawableElement.getPoints();
/* 161 */     ArrayList reverseDrawableElementPointList = reverseDrawableElementPoints(drawableElementPointList);
/* 162 */     selectedDrawableElement.setPoints(reverseDrawableElementPointList);
/*     */   }
/*     */ 
/*     */   private static ArrayList<Coordinate> reverseDrawableElementPoints(List<Coordinate> drawableElementPointList)
/*     */   {
/* 171 */     ArrayList reversedDrawableElementPointList = null;
/* 172 */     if (drawableElementPointList != null) {
/* 173 */       reversedDrawableElementPointList = new ArrayList(drawableElementPointList.size());
/* 174 */       Coordinate[] coordinatePointArray = new Coordinate[drawableElementPointList.size()];
/* 175 */       drawableElementPointList.toArray(coordinatePointArray);
/*     */ 
/* 177 */       for (int i = coordinatePointArray.length - 1; i >= 0; i--)
/* 178 */         reversedDrawableElementPointList.add(coordinatePointArray[i]);
/*     */     }
/*     */     else
/*     */     {
/* 182 */       reversedDrawableElementPointList = new ArrayList();
/*     */     }
/* 184 */     return reversedDrawableElementPointList;
/*     */   }
/*     */ 
/*     */   public static double calculateAngle(double old, double x1, double y1, double x2, double y2)
/*     */   {
/* 198 */     double delx = x2 - x1;
/* 199 */     double dely = y2 - y1;
/* 200 */     if ((Math.abs(delx) < 0.1D) && (Math.abs(dely) < 0.1D)) {
/* 201 */       return old;
/*     */     }
/* 203 */     double theta = Math.atan2(-delx, dely);
/* 204 */     double dir = theta * 180.0D / 3.141592653589793D;
/* 205 */     return transformToRange0To360(dir);
/*     */   }
/*     */ 
/*     */   public static double transformToRange0To360(double angle)
/*     */   {
/* 215 */     angle %= 360.0D;
/* 216 */     if (angle < 0.0D) {
/* 217 */       angle += 360.0D;
/*     */     }
/* 219 */     return angle;
/*     */   }
/*     */ 
/*     */   public static String wrapWatchText(String wcc, int lineLength)
/*     */   {
/* 230 */     if (wcc.charAt(wcc.length() - 1) != '\n') wcc = wcc + '\n';
/*     */ 
/* 232 */     StringBuffer strb = new StringBuffer(wcc);
/*     */ 
/* 234 */     String oneLine = wcc;
/* 235 */     int len = wcc.length();
/*     */ 
/* 237 */     while (len > lineLength)
/*     */     {
/* 239 */       int newline = oneLine.indexOf("\n");
/* 240 */       if (newline <= lineLength) {
/* 241 */         len -= newline + 1;
/* 242 */         oneLine = oneLine.substring(newline + 1);
/*     */       }
/*     */       else {
/* 245 */         int blk = oneLine.lastIndexOf(" ", lineLength);
/* 246 */         int dot = oneLine.lastIndexOf("...", lineLength);
/*     */ 
/* 248 */         int brk = 0;
/* 249 */         if (blk > dot) brk = blk + 1; else {
/* 250 */           brk = dot - 3;
/*     */         }
/* 252 */         strb.insert(brk + strb.length() - len, '\n');
/*     */ 
/* 254 */         oneLine = strb.toString().substring(brk + strb.length() - len);
/* 255 */         len -= brk;
/*     */       }
/*     */     }
/*     */ 
/* 259 */     return strb.toString();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenToolUtils
 * JD-Core Version:    0.6.2
 */