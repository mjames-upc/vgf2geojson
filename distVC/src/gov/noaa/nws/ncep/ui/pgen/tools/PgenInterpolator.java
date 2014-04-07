/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*     */ import com.vividsolutions.jts.algorithm.CGAlgorithms;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.CoordinateArrays;
/*     */ import com.vividsolutions.jts.geom.CoordinateList;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.CurveFitter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IMultiPoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ public class PgenInterpolator
/*     */ {
/*     */   public static List<AbstractDrawableComponent> interpolate(DrawableElement from, DrawableElement to, InterpolationProperties props, IMapDescriptor mapDescriptor)
/*     */   {
/*  49 */     if ((from == null) || (to == null))
/*  50 */       throw new IllegalArgumentException("PgenInterpolator: Drawable elements must not be null.");
/*  51 */     if ((!(from instanceof ILine)) || (!(to instanceof ILine))) {
/*  52 */       throw new IllegalArgumentException("PgenInterpolator: Drawable elements must be lines.");
/*     */     }
/*  54 */     ArrayList newElems = new ArrayList();
/*     */ 
/*  59 */     double[][] fromPixel = PgenUtil.latlonToPixel(((IMultiPoint)from).getLinePoints(), mapDescriptor);
/*  60 */     double[][] toPixel = PgenUtil.latlonToPixel(((IMultiPoint)to).getLinePoints(), mapDescriptor);
/*     */ 
/*  66 */     if (((ILine)from).isClosedLine().booleanValue()) fromPixel = reorderPoints(fromPixel);
/*  67 */     if (((ILine)to).isClosedLine().booleanValue()) toPixel = reorderPoints(toPixel);
/*     */ 
/*  72 */     Coordinate[] fromPts = toCoordinates(fromPixel);
/*  73 */     Coordinate[] toPts = toCoordinates(toPixel);
/*     */ 
/*  78 */     Coordinate[] fromLine = prepareLine(fromPixel, ((ILine)from).getSmoothFactor());
/*  79 */     Coordinate[] toLine = prepareLine(toPixel, ((ILine)to).getSmoothFactor());
/*     */ 
/*  87 */     LinkedHashMap mapping = null;
/*     */ 
/*  89 */     if ((!((ILine)from).isClosedLine().booleanValue()) && (!((ILine)to).isClosedLine().booleanValue()))
/*     */     {
/*  92 */       IMappingGenerator mapper = new InterleaveMappingGenerator(fromLine, toLine, fromPts, toPts);
/*  93 */       mapping = mapper.generateMappingPoints();
/*     */     }
/*  95 */     else if ((((ILine)from).isClosedLine().booleanValue()) && (((ILine)to).isClosedLine().booleanValue()))
/*     */     {
/*  98 */       IMappingGenerator mapper = new ConvexHullMappingGenerator(fromPts, toPts);
/*  99 */       mapping = mapper.generateMappingPoints();
/* 100 */       if (mapping.isEmpty())
/*     */       {
/* 104 */         mapper = new DefaultPolyMappingGenerator(fromPts, toPts);
/* 105 */         mapping = mapper.generateMappingPoints();
/*     */       }
/*     */     }
/*     */     else {
/* 109 */       return newElems;
/*     */     }
/*     */     IMappingGenerator mapper;
/* 111 */     LinearInterpolator interp = new LinearInterpolator(mapping);
/*     */ 
/* 118 */     int start = props.getStartingTime();
/* 119 */     int end = props.getEndingTime();
/* 120 */     int interval = props.getInterval();
/* 121 */     if (start > end) {
/* 122 */       start = -start;
/* 123 */       end = -end;
/*     */     }
/*     */ 
/* 126 */     for (int i = start + interval; i < end; i += interval)
/*     */     {
/* 129 */       double fraction = Math.abs((i - start) / (end - start));
/*     */ 
/* 135 */       ArrayList interpPts = interp.interpolate(fraction);
/* 136 */       ArrayList newPts = toWorld(interpPts, mapDescriptor);
/*     */ 
/* 141 */       DrawableElement de = (DrawableElement)from.copy();
/* 142 */       de.setPoints(newPts);
/* 143 */       newElems.add(de);
/*     */     }
/*     */ 
/* 149 */     if ((newElems.size() > 0) && ((newElems.get(0) instanceof Gfa))) {
/* 150 */       ArrayList txtLoc = interpolate(((Gfa)from).getGfaTextCoordinate(), ((Gfa)to).getGfaTextCoordinate(), 
/* 151 */         props, mapDescriptor);
/*     */ 
/* 153 */       int len = Math.min(newElems.size(), txtLoc.size());
/* 154 */       int[] tbs = getGfaTopBottoms((Gfa)from, (Gfa)to);
/* 155 */       boolean dotb = (tbs[0] >= 0) && (tbs[1] >= 0) && (tbs[2] >= 0) && (tbs[3] >= 0);
/* 156 */       int ii = start + interval; for (int jj = 0; (ii < end) && (jj < len); jj++) {
/* 157 */         Gfa elem = (Gfa)newElems.get(jj);
/* 158 */         elem.setGfaTextCoordinate((Coordinate)txtLoc.get(jj));
/* 159 */         elem.setGfaFcstHr(Math.abs(ii));
/*     */ 
/* 162 */         double fraction = Math.abs((ii - start) / (end - start));
/* 163 */         if (dotb) {
/* 164 */           int top = interpolate(tbs[0], tbs[2], fraction);
/* 165 */           int bot = interpolate(tbs[1], tbs[3], fraction);
/* 166 */           elem.setGfaValue("Top", top);
/* 167 */           elem.setGfaValue("Bottom", bot);
/* 168 */           elem.setGfaValue("Top/Bottom", top + "/" + bot);
/*     */         }
/* 156 */         ii += interval;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 174 */     return newElems;
/*     */   }
/*     */ 
/*     */   private static double[][] reorderPoints(double[][] pts)
/*     */   {
/* 184 */     Coordinate[] coords = toCoordinates(pts);
/*     */ 
/* 189 */     if (coords[0].equals2D(coords[(coords.length - 1)])) {
/* 190 */       coords = CoordinateArrays.extract(coords, 0, coords.length - 2);
/*     */     }
/*     */ 
/* 195 */     double maxX = coords[0].x;
/* 196 */     Coordinate start = coords[0];
/* 197 */     for (Coordinate coord : coords) {
/* 198 */       if (coord.x > maxX) {
/* 199 */         maxX = coord.x;
/* 200 */         start = coord;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 207 */     CoordinateArrays.scroll(coords, start);
/*     */ 
/* 212 */     CoordinateList cl = new CoordinateList(coords);
/* 213 */     cl.closeRing();
/* 214 */     coords = cl.toCoordinateArray();
/*     */ 
/* 222 */     if (CGAlgorithms.isCCW(coords)) CoordinateArrays.reverse(coords);
/*     */ 
/* 225 */     return toDouble(coords);
/*     */   }
/*     */ 
/*     */   private static double[][] toDouble(Coordinate[] coords)
/*     */   {
/* 233 */     double[][] pts = new double[coords.length][3];
/*     */ 
/* 235 */     for (int i = 0; i < coords.length; i++) {
/* 236 */       pts[i] = { coords[i].x, coords[i].y, 0.0D };
/*     */     }
/*     */ 
/* 239 */     return pts;
/*     */   }
/*     */ 
/*     */   private static Coordinate[] toCoordinates(double[][] pts)
/*     */   {
/* 246 */     Coordinate[] coords = new Coordinate[pts.length];
/* 247 */     for (int i = 0; i < pts.length; i++) {
/* 248 */       coords[i] = new Coordinate(pts[i][0], pts[i][1]);
/*     */     }
/* 250 */     return coords;
/*     */   }
/*     */ 
/*     */   private static Coordinate[] prepareLine(double[][] points, int smoothFactor)
/*     */   {
/* 258 */     float devScale = 50.0F;
/*     */     double[][] smoothpts;
/*     */     double[][] smoothpts;
/* 264 */     if (smoothFactor > 0)
/*     */     {
/* 265 */       float density;
/*     */       float density;
/* 265 */       if (smoothFactor == 1) density = devScale / 1.0F; else
/* 266 */         density = devScale / 5.0F;
/* 267 */       smoothpts = CurveFitter.fitParametricCurve(points, density);
/*     */     }
/*     */     else {
/* 270 */       smoothpts = points;
/*     */     }
/*     */ 
/* 273 */     Coordinate[] coords = toCoordinates(smoothpts);
/* 274 */     return coords;
/*     */   }
/*     */ 
/*     */   public static ArrayList<Coordinate> interpolate(Coordinate from, Coordinate to, InterpolationProperties props, IMapDescriptor mapDescriptor)
/*     */   {
/* 289 */     if ((from == null) || (to == null)) {
/* 290 */       throw new IllegalArgumentException("PgenInterpolator: Start/End point must not be null.");
/*     */     }
/* 292 */     ArrayList newPts = new ArrayList();
/*     */ 
/* 294 */     Coordinate[] pointsInMap = new Coordinate[2];
/* 295 */     pointsInMap[0] = from;
/* 296 */     pointsInMap[1] = to;
/*     */ 
/* 301 */     double[][] ptsPixel = PgenUtil.latlonToPixel(pointsInMap, mapDescriptor);
/*     */ 
/* 306 */     Coordinate[] ptsCoord = toCoordinates(ptsPixel);
/*     */ 
/* 311 */     int start = props.getStartingTime();
/* 312 */     int end = props.getEndingTime();
/* 313 */     int interval = props.getInterval();
/* 314 */     if (start > end) {
/* 315 */       start = -start;
/* 316 */       end = -end;
/*     */     }
/*     */ 
/* 319 */     for (int i = start + interval; i < end; i += interval)
/*     */     {
/* 322 */       double fraction = (i - start) / (end - start);
/*     */ 
/* 328 */       double dx = (ptsCoord[1].x - ptsCoord[0].x) * fraction;
/* 329 */       double dy = (ptsCoord[1].y - ptsCoord[0].y) * fraction;
/*     */ 
/* 331 */       Coordinate pt = new Coordinate(ptsCoord[0].x + dx, ptsCoord[0].y + dy);
/* 332 */       newPts.add(pt);
/*     */     }
/*     */ 
/* 337 */     newPts = toWorld(newPts, mapDescriptor);
/*     */ 
/* 339 */     return newPts;
/*     */   }
/*     */ 
/*     */   private static ArrayList<Coordinate> toWorld(ArrayList<Coordinate> coords, IMapDescriptor mapDescriptor)
/*     */   {
/* 345 */     ArrayList latlons = new ArrayList();
/*     */ 
/* 347 */     for (Coordinate coord : coords)
/*     */     {
/* 349 */       double[] in = { coord.x, coord.y, 0.0D };
/* 350 */       double[] out = mapDescriptor.pixelToWorld(in);
/* 351 */       latlons.add(new Coordinate(out[0], out[1]));
/*     */     }
/*     */ 
/* 354 */     return latlons;
/*     */   }
/*     */ 
/*     */   private static int interpolate(int start, int end, double fraction)
/*     */   {
/* 362 */     return (int)(start - fraction * (start - end));
/*     */   }
/*     */ 
/*     */   private static int[] getGfaTopBottoms(Gfa from, Gfa to)
/*     */   {
/* 369 */     int[] tb = { -1, -1, -1, -1 };
/* 370 */     tb[0] = parseGfaTopBottom(from.getGfaTop());
/* 371 */     tb[1] = parseGfaTopBottom(from.getGfaBottom());
/* 372 */     tb[2] = parseGfaTopBottom(to.getGfaTop());
/* 373 */     tb[3] = parseGfaTopBottom(to.getGfaBottom());
/*     */ 
/* 375 */     return tb;
/*     */   }
/*     */ 
/*     */   private static int parseGfaTopBottom(String topBot)
/*     */   {
/* 382 */     int tb = -1;
/*     */ 
/* 384 */     if (topBot != null) {
/* 385 */       if (topBot.equalsIgnoreCase("SFC"))
/* 386 */         tb = 0;
/*     */       else {
/*     */         try
/*     */         {
/* 390 */           tb = Integer.parseInt(topBot);
/*     */         }
/*     */         catch (NumberFormatException ee) {
/* 393 */           tb = -1;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 398 */     return tb;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenInterpolator
 * JD-Core Version:    0.6.2
 */