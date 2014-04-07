/*      */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*      */ 
/*      */ import com.vividsolutions.jts.algorithm.CGAlgorithms;
/*      */ import com.vividsolutions.jts.algorithm.SimplePointInRing;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.CoordinateList;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.GeometryFactory;
/*      */ import com.vividsolutions.jts.geom.LineSegment;
/*      */ import com.vividsolutions.jts.geom.LineString;
/*      */ import com.vividsolutions.jts.geom.LinearRing;
/*      */ import com.vividsolutions.jts.geom.MultiLineString;
/*      */ import com.vividsolutions.jts.geom.Point;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import com.vividsolutions.jts.operation.distance.DistanceOp;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.CurveFitter;
/*      */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*      */ import gov.noaa.nws.ncep.viz.common.graphicUtil.ReducePoints;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import org.geotools.referencing.GeodeticCalculator;
/*      */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*      */ 
/*      */ public class FrzlFormatter
/*      */ {
/*      */   private static final double NEAR_FA_BOUND = 20.0D;
/*      */   private static final double EXT_DIST = 5000.0D;
/*      */   private static final double TOLERANCE = 0.15D;
/*      */   private static final int FRZL_MAX_POINTS = 10;
/*      */   private static final double FZLVL_MAX_GAP = 100.0D;
/*      */   private static final double FZLVL_MIN_LEN = 100.0D;
/*      */   private static final double REDUCE_INCR_PCT = 3.0D;
/*      */   private static final double MAX_REDUCE_PCT = 25.0D;
/*      */   private static final double REDUCE_INCR_DST = 100.0D;
/*      */   private static final int MAX_CHARS_PER_LINE = 65;
/*      */   private static final int FRZL_INDENT = 7;
/*      */   private static final int MAX_LINES = 3;
/*      */   private List<Gfa> frzlList;
/*   90 */   private GeometryFactory gf = new GeometryFactory();
/*      */ 
/*      */   public FrzlFormatter(List<Gfa> fzlvlList)
/*      */   {
/*   97 */     this.frzlList = fzlvlList;
/*      */   }
/*      */ 
/*      */   public List<Gfa> format()
/*      */   {
/*  106 */     List ret = new ArrayList();
/*      */ 
/*  108 */     for (Gfa frzl : this.frzlList) {
/*  109 */       if (frzl.getGfaHazard().equalsIgnoreCase("FZLVL")) {
/*  110 */         if (frzl.isClosedLine().booleanValue()) {
/*  111 */           ret.addAll(formatClosedFrzl(frzl));
/*      */         }
/*      */         else {
/*  114 */           ret.addAll(formatOpenFrzl(frzl));
/*      */         }
/*      */       }
/*      */     }
/*  118 */     return ret;
/*      */   }
/*      */ 
/*      */   private List<Gfa> formatOpenFrzl(Gfa openFrzl)
/*      */   {
/*  129 */     List ret = new ArrayList();
/*      */ 
/*  132 */     ArrayList frzlCoord = new ArrayList();
/*  133 */     frzlCoord.addAll(openFrzl.getPoints());
/*      */ 
/*  136 */     HashMap areaBnds = GfaClip.getInstance().getFaAreaBounds();
/*      */     Geometry clip;
/*      */     int ii;
/*  137 */     for (Iterator localIterator = areaBnds.keySet().iterator(); localIterator.hasNext(); 
/*  167 */       ii < clip.getNumGeometries())
/*      */     {
/*  137 */       String areaName = (String)localIterator.next();
/*      */ 
/*  141 */       if (needExtend((Coordinate)frzlCoord.get(0), (Geometry)areaBnds.get(areaName))) {
/*  142 */         frzlCoord.add(0, extendLineSeg((Coordinate)frzlCoord.get(1), (Coordinate)frzlCoord.get(0)));
/*      */       }
/*      */ 
/*  146 */       if (needExtend((Coordinate)frzlCoord.get(frzlCoord.size() - 1), (Geometry)areaBnds.get(areaName))) {
/*  147 */         frzlCoord.add(extendLineSeg((Coordinate)frzlCoord.get(frzlCoord.size() - 2), (Coordinate)frzlCoord.get(frzlCoord.size() - 1)));
/*      */       }
/*      */ 
/*  151 */       clip = ((Geometry)areaBnds.get(areaName)).intersection(this.gf.createLineString((Coordinate[])frzlCoord.toArray(new Coordinate[frzlCoord.size()])));
/*      */ 
/*  154 */       LinearRing bigBox = this.gf.createLinearRing(new Coordinate[] { 
/*  155 */         new Coordinate(-150.0D, 89.0D), new Coordinate(-150.0D, 0.0D), 
/*  156 */         new Coordinate(150.0D, 0.0D), new Coordinate(150.0D, 89.0D), 
/*  157 */         new Coordinate(-150.0D, 89.0D) });
/*      */ 
/*  160 */       Geometry diff = this.gf.createPolygon(bigBox, 
/*  161 */         new LinearRing[] { this.gf.createLinearRing(((Geometry)areaBnds.get(areaName)).getCoordinates()) }).intersection(
/*  162 */         this.gf.createLineString((Coordinate[])frzlCoord.toArray(new Coordinate[frzlCoord.size()])));
/*      */ 
/*  165 */       clip = addBackSmallGaps(clip, diff);
/*      */ 
/*  167 */       ii = 0; continue;
/*      */ 
/*  169 */       ArrayList pts = reducePtOpenFrzl(clip.getGeometryN(ii).getCoordinates());
/*      */ 
/*  172 */       if (getLength((Coordinate[])pts.toArray(new Coordinate[pts.size()])) / 1852.0D > 100.0D)
/*      */       {
/*  174 */         Gfa clippedGfa = points2Frzl(pts, openFrzl);
/*  175 */         if (clippedGfa != null) {
/*  176 */           GfaRules.assignIssueTime(clippedGfa);
/*  177 */           clippedGfa.setGfaArea(areaName);
/*  178 */           clippedGfa.setGfaVorText(Gfa.buildVorText(clippedGfa));
/*  179 */           ret.add(clippedGfa);
/*      */         }
/*      */       }
/*  167 */       ii++;
/*      */     }
/*      */ 
/*  185 */     return ret;
/*      */   }
/*      */ 
/*      */   private List<Gfa> formatClosedFrzl(Gfa closedFrzl)
/*      */   {
/*  195 */     List ret = new ArrayList();
/*      */ 
/*  198 */     ArrayList frzlCoord = new ArrayList();
/*  199 */     frzlCoord.addAll(closedFrzl.getPoints());
/*  200 */     frzlCoord.add((Coordinate)frzlCoord.get(0));
/*      */ 
/*  203 */     HashMap areaBnds = GfaClip.getInstance().getFaAreaBounds();
/*  204 */     for (String areaName : areaBnds.keySet())
/*      */     {
/*  206 */       Geometry clip = intersectRings(closedFrzl.toPolygon(), (Geometry)areaBnds.get(areaName));
/*  207 */       if (clip != null)
/*      */       {
/*  210 */         LinearRing bigBox = this.gf.createLinearRing(new Coordinate[] { 
/*  211 */           new Coordinate(-150.0D, 89.0D), new Coordinate(-150.0D, 0.0D), 
/*  212 */           new Coordinate(150.0D, 0.0D), new Coordinate(150.0D, 89.0D), 
/*  213 */           new Coordinate(-150.0D, 89.0D) });
/*      */ 
/*  216 */         Geometry diff = intersectRings(closedFrzl.toPolygon(), this.gf.createPolygon(bigBox, 
/*  217 */           new LinearRing[] { this.gf.createLinearRing(((Geometry)areaBnds.get(areaName)).getCoordinates()) }));
/*      */ 
/*  220 */         clip = addBackSmallGaps(clip, diff);
/*      */ 
/*  222 */         for (int ii = 0; ii < clip.getNumGeometries(); ii++)
/*      */         {
/*      */           List pts;
/*      */           List pts;
/*  226 */           if ((clip.getGeometryN(ii) instanceof LinearRing)) {
/*  227 */             pts = reducePtClosedFrzl(clip.getGeometryN(ii).getCoordinates());
/*      */           }
/*      */           else {
/*  230 */             pts = reducePtOpenFrzl(clip.getGeometryN(ii).getCoordinates());
/*      */           }
/*      */ 
/*  234 */           if (getLength((Coordinate[])pts.toArray(new Coordinate[pts.size()])) / 1852.0D > 100.0D)
/*      */           {
/*  236 */             Gfa clippedGfa = points2Frzl(pts, closedFrzl);
/*  237 */             if (clippedGfa != null) {
/*  238 */               GfaRules.assignIssueTime(clippedGfa);
/*  239 */               clippedGfa.setGfaArea(areaName);
/*  240 */               clippedGfa.setGfaVorText(Gfa.buildVorText(clippedGfa));
/*  241 */               ret.add(clippedGfa);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  247 */     return ret;
/*      */   }
/*      */ 
/*      */   private Gfa points2Frzl(List<Coordinate> pts, Gfa model)
/*      */   {
/*  257 */     if ((pts != null) && (!pts.isEmpty())) {
/*  258 */       Gfa resultGfa = model.copy();
/*      */ 
/*  261 */       if (matchCoords((Coordinate)pts.get(0), (Coordinate)pts.get(pts.size() - 1))) {
/*  262 */         resultGfa.setClosed(Boolean.valueOf(true));
/*  263 */         pts.remove(pts.size() - 1);
/*      */       }
/*      */       else {
/*  266 */         resultGfa.setClosed(Boolean.valueOf(false));
/*      */       }
/*      */ 
/*  269 */       resultGfa.setPointsOnly(new ArrayList(pts));
/*      */ 
/*  271 */       return resultGfa;
/*      */     }
/*  273 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean needExtend(Coordinate endPt, Geometry geo)
/*      */   {
/*  285 */     boolean ret = false;
/*      */ 
/*  287 */     Point jtsPt = this.gf.createPoint(endPt);
/*      */ 
/*  289 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*      */ 
/*  293 */     if (geo.contains(jtsPt)) {
/*  294 */       Geometry bnd = geo.getBoundary();
/*  295 */       Coordinate[] nearestPts = DistanceOp.nearestPoints(bnd, jtsPt);
/*      */ 
/*  297 */       gc.setStartingGeographicPoint(endPt.x, endPt.y);
/*  298 */       gc.setDestinationGeographicPoint(nearestPts[0].x, nearestPts[0].y);
/*  299 */       double dist = gc.getOrthodromicDistance() / 1852.0D;
/*      */ 
/*  301 */       if (dist > 20.0D) {
/*  302 */         ret = true;
/*      */       }
/*      */     }
/*      */ 
/*  306 */     return ret;
/*      */   }
/*      */ 
/*      */   private Coordinate extendLineSeg(Coordinate p1, Coordinate p2)
/*      */   {
/*  316 */     LineSegment ls = new LineSegment(p1, p2);
/*      */ 
/*  318 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*  319 */     gc.setStartingGeographicPoint(p1.x, p1.y);
/*  320 */     gc.setDestinationGeographicPoint(p2.x, p2.y);
/*  321 */     double distMap = gc.getOrthodromicDistance() / 1852.0D;
/*      */ 
/*  323 */     double dist = 5000.0D / distMap;
/*      */ 
/*  325 */     double x = p2.x + dist * Math.cos(ls.angle());
/*  326 */     double y = p2.y + dist * Math.sin(ls.angle());
/*      */ 
/*  328 */     return new Coordinate(x, y);
/*      */   }
/*      */ 
/*      */   private ArrayList<Coordinate> reduceLinePoly(Coordinate[] inPts, double tolerance)
/*      */   {
/*  362 */     ArrayList outPts = new ArrayList();
/*      */ 
/*  364 */     if ((tolerance > 0.0D) && (inPts != null))
/*      */     {
/*  366 */       if (inPts.length > 2)
/*      */       {
/*  368 */         outPts.add(new Coordinate(inPts[0].x, inPts[0].y));
/*      */ 
/*  370 */         int lastPt = 0;
/*  371 */         int curPt = 2;
/*      */ 
/*  373 */         boolean lineOk = false;
/*      */ 
/*  375 */         while (curPt < inPts.length) {
/*  376 */           lineOk = checkTolerance(lastPt, curPt, inPts, tolerance);
/*  377 */           if (!lineOk) {
/*  378 */             outPts.add(new Coordinate(inPts[(curPt - 1)].x, inPts[(curPt - 1)].y));
/*  379 */             lastPt = curPt - 1;
/*      */           }
/*  381 */           curPt++;
/*      */         }
/*      */ 
/*  385 */         outPts.add(new Coordinate(inPts[(inPts.length - 1)].x, inPts[(inPts.length - 1)].y));
/*      */       }
/*      */       else
/*      */       {
/*  389 */         for (int ii = 0; ii < inPts.length; ii++) {
/*  390 */           outPts.add(inPts[ii]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  395 */     return outPts;
/*      */   }
/*      */ 
/*      */   private boolean checkTolerance(int lastPt, int curPt, Coordinate[] inPts, double tolerance)
/*      */   {
/*  408 */     boolean withinTolerance = true;
/*      */ 
/*  410 */     LineSegment ls = new LineSegment(inPts[lastPt], inPts[curPt]);
/*      */ 
/*  412 */     for (int ii = lastPt; (ii < curPt) && (withinTolerance); ii++)
/*      */     {
/*  414 */       if (ls.distancePerpendicular(inPts[ii]) > tolerance) {
/*  415 */         withinTolerance = false;
/*      */       }
/*      */     }
/*      */ 
/*  419 */     return withinTolerance;
/*      */   }
/*      */ 
/*      */   private Coordinate[] snapFrzl(Coordinate[] inPts)
/*      */   {
/*  429 */     Coordinate[] ret = new Coordinate[inPts.length];
/*      */ 
/*  431 */     for (int ii = 0; ii < inPts.length; ii++) {
/*  432 */       ret[ii] = GfaSnap.getInstance().snapOnePt(inPts[ii]);
/*      */     }
/*      */ 
/*  435 */     return ret;
/*      */   }
/*      */ 
/*      */   private List<Coordinate> reducePtClosedFrzl(Coordinate[] inPts)
/*      */   {
/*  447 */     boolean reorder = false;
/*  448 */     if (CGAlgorithms.isCCW(inPts)) {
/*  449 */       reorder = true;
/*  450 */       reversePointOrder(inPts);
/*      */     }
/*      */ 
/*  453 */     boolean done = false;
/*  454 */     ArrayList reduceFlg = new ArrayList();
/*      */ 
/*  456 */     List ptList = new ArrayList(Arrays.asList(inPts));
/*      */ 
/*  459 */     ptList.remove(0);
/*      */ 
/*  461 */     double reducePct = 3.0D;
/*      */ 
/*  463 */     while (!done)
/*      */     {
/*  465 */       for (int ii = 0; ii < ptList.size(); ii++) {
/*  466 */         reduceFlg.add(Boolean.valueOf(true));
/*      */       }
/*      */ 
/*  470 */       reduceFlg.set(0, Boolean.valueOf(false));
/*  471 */       reduceFlg.set(reduceFlg.size() - 1, Boolean.valueOf(false));
/*      */ 
/*  473 */       if ((!reduceKeepConcav(ptList, reduceFlg, reducePct, 100.0D)) && (reducePct < 25.0D)) {
/*  474 */         reducePct += 1.0D;
/*  475 */         reduceFlg.clear();
/*      */       }
/*      */       else {
/*  478 */         done = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  483 */     ptList.add(0, (Coordinate)ptList.get(ptList.size() - 1));
/*      */ 
/*  485 */     Coordinate[] outPts = (Coordinate[])ptList.toArray(new Coordinate[ptList.size()]);
/*      */ 
/*  487 */     if (reorder) {
/*  488 */       outPts = reversePointOrder((Coordinate[])ptList.toArray(new Coordinate[ptList.size()]));
/*      */     }
/*      */ 
/*  491 */     List outList = new ArrayList(Arrays.asList(outPts));
/*      */ 
/*  493 */     outList.remove(0);
/*  494 */     if (!canBeFormatted(outList)) {
/*  495 */       outList.remove(1);
/*      */     }
/*  497 */     outList.add(0, (Coordinate)outList.get(outList.size() - 1));
/*      */ 
/*  499 */     return outList;
/*      */   }
/*      */ 
/*      */   private ArrayList<Coordinate> reducePtOpenFrzl(Coordinate[] inPts)
/*      */   {
/*  510 */     if (inPts.length > 10)
/*      */     {
/*  512 */       double[][] linePts = new double[inPts.length][2];
/*      */ 
/*  514 */       int ii = 0;
/*  515 */       for (Coordinate loc : inPts) {
/*  516 */         linePts[ii][0] = loc.x;
/*  517 */         linePts[ii][1] = loc.y;
/*  518 */         ii++;
/*      */       }
/*      */ 
/*  521 */       double[][] fitCurve = CurveFitter.fitParametricCurve(linePts, 5.0F);
/*      */ 
/*  524 */       Coordinate[] fitCurvePts = new Coordinate[fitCurve.length];
/*  525 */       for (ii = 0; ii < fitCurve.length; ii++) {
/*  526 */         fitCurvePts[ii] = new Coordinate(fitCurve[ii][0], fitCurve[ii][1]);
/*      */       }
/*      */ 
/*  529 */       ArrayList pts = reduceLinePoly(fitCurvePts, 0.15D);
/*      */ 
/*  531 */       ArrayList reduceFlg = new ArrayList();
/*  532 */       for (ii = 0; ii < pts.size(); ii++) {
/*  533 */         reduceFlg.add(Integer.valueOf(1));
/*      */       }
/*      */ 
/*  536 */       reduceFlg.set(0, Integer.valueOf(0));
/*  537 */       reduceFlg.set(reduceFlg.size() - 1, Integer.valueOf(0));
/*      */ 
/*  540 */       CoordinateList cl = new CoordinateList();
/*  541 */       cl.addAll(pts, true);
/*      */ 
/*  543 */       return reduceLinePoly(snapFrzl(ReducePoints.reduceByAngle(cl, reduceFlg, 10).toCoordinateArray()), 
/*  544 */         0.15D);
/*      */     }
/*      */ 
/*  548 */     return reduceLinePoly(snapFrzl(inPts), 0.15D);
/*      */   }
/*      */ 
/*      */   private Geometry addBackSmallGaps(Geometry clip, Geometry diff)
/*      */   {
/*  555 */     if ((!(clip instanceof LineString)) && (!(clip instanceof MultiLineString))) return clip;
/*      */ 
/*  563 */     if (diff != null) {
/*  564 */       for (int ii = 0; ii < diff.getNumGeometries(); ii++)
/*      */       {
/*  566 */         Coordinate[] gapPts = diff.getGeometryN(ii).getCoordinates();
/*  567 */         double dist = getLength(gapPts) / 1852.0D;
/*  568 */         if (dist < 100.0D) {
/*  569 */           Geometry geo1 = null;
/*  570 */           Geometry geo2 = null;
/*  571 */           Geometry geo3 = null;
/*  572 */           Geometry geo4 = null;
/*      */ 
/*  574 */           Coordinate[] linePts = null;
/*      */ 
/*  576 */           for (int jj = 0; jj < clip.getNumGeometries(); jj++)
/*      */           {
/*  578 */             linePts = clip.getGeometryN(jj).getCoordinates();
/*  579 */             if (matchCoords(linePts[(linePts.length - 1)], gapPts[0])) {
/*  580 */               geo1 = clip.getGeometryN(jj);
/*      */             }
/*  582 */             if (matchCoords(linePts[0], gapPts[(gapPts.length - 1)])) {
/*  583 */               geo2 = clip.getGeometryN(jj);
/*      */             }
/*  585 */             if (matchCoords(linePts[0], gapPts[0])) {
/*  586 */               geo3 = clip.getGeometryN(jj);
/*      */             }
/*  588 */             if (matchCoords(linePts[(linePts.length - 1)], gapPts[(gapPts.length - 1)])) {
/*  589 */               geo4 = clip.getGeometryN(jj);
/*      */             }
/*      */           }
/*      */ 
/*  593 */           Coordinate[] newLinePts = null;
/*      */ 
/*  596 */           if ((geo1 != null) && (geo2 != null)) {
/*  597 */             if (geo1 == geo2) {
/*  598 */               newLinePts = arrayMerge(new Coordinate[][] { (Coordinate[])Arrays.copyOfRange(geo1.getCoordinates(), 1, geo1.getCoordinates().length - 1), 
/*  599 */                 (Coordinate[])Arrays.copyOfRange(gapPts, 1, gapPts.length - 1), 
/*  600 */                 { geo1.getCoordinates()[1] } });
/*  601 */               clip = new GeometryFactory().createLinearRing(newLinePts);
/*  602 */               return clip;
/*      */             }
/*      */ 
/*  608 */             newLinePts = arrayMerge(new Coordinate[][] { geo1.getCoordinates(), gapPts, geo2.getCoordinates() });
/*      */           }
/*      */           else
/*      */           {
/*  612 */             if (geo1 != null) {
/*  613 */               newLinePts = arrayMerge(new Coordinate[][] { (Coordinate[])Arrays.copyOfRange(geo1.getCoordinates(), 0, geo1.getCoordinates().length - 1), 
/*  614 */                 (Coordinate[])Arrays.copyOfRange(gapPts, 1, gapPts.length) });
/*      */             }
/*      */ 
/*  617 */             if (geo2 != null) {
/*  618 */               newLinePts = arrayMerge(new Coordinate[][] { (Coordinate[])Arrays.copyOfRange(gapPts, 0, gapPts.length - 1), 
/*  619 */                 (Coordinate[])Arrays.copyOfRange(geo2.getCoordinates(), 1, geo2.getCoordinates().length) });
/*      */             }
/*      */           }
/*      */ 
/*  623 */           if ((geo3 != null) && (geo4 != null)) {
/*  624 */             reversePointOrder(gapPts);
/*  625 */             if (geo3 == geo4) {
/*  626 */               newLinePts = arrayMerge(new Coordinate[][] { (Coordinate[])Arrays.copyOfRange(geo3.getCoordinates(), 1, geo3.getCoordinates().length - 1), 
/*  627 */                 (Coordinate[])Arrays.copyOfRange(gapPts, 1, gapPts.length - 1), 
/*  628 */                 { geo3.getCoordinates()[1] } });
/*  629 */               clip = new GeometryFactory().createLinearRing(newLinePts);
/*  630 */               return clip;
/*      */             }
/*      */ 
/*  633 */             newLinePts = arrayMerge(new Coordinate[][] { (Coordinate[])Arrays.copyOfRange(geo4.getCoordinates(), 0, geo4.getCoordinates().length - 1), 
/*  634 */               (Coordinate[])Arrays.copyOfRange(gapPts, 1, gapPts.length - 1), 
/*  635 */               (Coordinate[])Arrays.copyOfRange(geo3.getCoordinates(), 1, geo3.getCoordinates().length) });
/*      */           }
/*      */           else
/*      */           {
/*  639 */             reversePointOrder(gapPts);
/*  640 */             if (geo3 != null)
/*      */             {
/*  642 */               newLinePts = arrayMerge(new Coordinate[][] { (Coordinate[])Arrays.copyOfRange(gapPts, 0, gapPts.length - 1), 
/*  643 */                 (Coordinate[])Arrays.copyOfRange(geo3.getCoordinates(), 1, geo3.getCoordinates().length) });
/*      */             }
/*  646 */             else if (geo4 != null) {
/*  647 */               newLinePts = arrayMerge(new Coordinate[][] { (Coordinate[])Arrays.copyOfRange(geo4.getCoordinates(), 0, geo4.getCoordinates().length - 1), 
/*  648 */                 (Coordinate[])Arrays.copyOfRange(gapPts, 1, gapPts.length) });
/*      */             }
/*      */           }
/*      */ 
/*  652 */           int numLines = clip.getNumGeometries();
/*  653 */           if ((geo1 != null) && (geo2 != null)) numLines--;
/*  654 */           if ((geo3 != null) && (geo4 != null)) numLines--;
/*      */ 
/*  656 */           LineString[] newLines = new LineString[numLines];
/*      */ 
/*  658 */           newLines[0] = new GeometryFactory().createLineString(newLinePts);
/*      */ 
/*  660 */           int mm = 1;
/*  661 */           for (int kk = 0; kk < clip.getNumGeometries(); kk++) {
/*  662 */             if ((clip.getGeometryN(kk) != geo1) && (clip.getGeometryN(kk) != geo2) && 
/*  663 */               (clip.getGeometryN(kk) != geo3) && (clip.getGeometryN(kk) != geo4)) {
/*  664 */               newLines[mm] = new GeometryFactory().createLineString(clip.getGeometryN(kk).getCoordinates());
/*  665 */               mm++;
/*      */             }
/*      */           }
/*      */ 
/*  669 */           clip = new GeometryFactory().createMultiLineString(newLines);
/*      */ 
/*  671 */           geo1 = null;
/*  672 */           geo2 = null;
/*  673 */           geo3 = null;
/*  674 */           geo4 = null;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  679 */     return clip;
/*      */   }
/*      */ 
/*      */   private double getLength(Coordinate[] pts)
/*      */   {
/*  690 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*      */ 
/*  692 */     double dist = 0.0D;
/*  693 */     for (int ii = 0; ii < pts.length - 1; ii++)
/*      */     {
/*  695 */       gc.setStartingGeographicPoint(pts[ii].x, pts[ii].y);
/*  696 */       gc.setDestinationGeographicPoint(pts[(ii + 1)].x, pts[(ii + 1)].y);
/*      */ 
/*  698 */       dist += gc.getOrthodromicDistance();
/*      */     }
/*      */ 
/*  701 */     return dist;
/*      */   }
/*      */ 
/*      */   private boolean matchCoords(Coordinate pt1, Coordinate pt2)
/*      */   {
/*  711 */     return (Math.abs(pt1.x - pt2.x) < 0.0001D) && (Math.abs(pt1.y - pt2.y) < 0.0001D);
/*      */   }
/*      */ 
/*      */   private Coordinate[] arrayMerge(Coordinate[][] arrays)
/*      */   {
/*  723 */     int count = 0;
/*  724 */     for (Coordinate[] array : arrays)
/*      */     {
/*  726 */       count += array.length;
/*      */     }
/*      */ 
/*  731 */     Coordinate[] mergedArray = (Coordinate[])Array.newInstance(
/*  732 */       arrays[0][0].getClass(), count);
/*      */ 
/*  736 */     int start = 0;
/*  737 */     for (Coordinate[] array : arrays)
/*      */     {
/*  739 */       System.arraycopy(array, 0, 
/*  740 */         mergedArray, start, array.length);
/*  741 */       start += array.length;
/*      */     }
/*      */ 
/*  744 */     return mergedArray;
/*      */   }
/*      */ 
/*      */   private Coordinate[] reversePointOrder(Coordinate[] coords)
/*      */   {
/*  755 */     for (int ii = 0; ii < coords.length / 2; ii++) {
/*  756 */       Coordinate tmp = coords[ii];
/*  757 */       coords[ii] = coords[(coords.length - ii - 1)];
/*  758 */       coords[(coords.length - ii - 1)] = tmp;
/*      */     }
/*      */ 
/*  761 */     return coords;
/*      */   }
/*      */ 
/*      */   private boolean canBeFormatted(List<Coordinate> pts)
/*      */   {
/*  770 */     return createClosedFromLine(pts).length() <= 168.19999999999999D;
/*      */   }
/*      */ 
/*      */   private String createClosedFromLine(List<Coordinate> pts)
/*      */   {
/*  779 */     ArrayList snap = SnapUtil.getSnapWithStation(pts, SnapUtil.VOR_STATION_LIST, 10, 16, false);
/*  780 */     Coordinate[] a = new Coordinate[snap.size()];
/*  781 */     a = (Coordinate[])snap.toArray(a);
/*  782 */     String fromLine = "BOUNDED BY " + SnapUtil.getVORText(a, "-", "Area", -1, true, false, true);
/*  783 */     return fromLine;
/*      */   }
/*      */ 
/*      */   private boolean reduceKeepConcav(List<Coordinate> inPts, List<Boolean> reduceFlg, double reducePct, double maxDist)
/*      */   {
/*  804 */     boolean done = false;
/*  805 */     boolean canBeFormatted = canBeFormatted(inPts);
/*      */ 
/*  807 */     Coordinate[] replacementPts = new Coordinate[2];
/*  808 */     Coordinate pt1 = null;
/*  809 */     Coordinate pt2 = null;
/*  810 */     double[] sizeDiff = { 1.7976931348623157E+308D };
/*      */ 
/*  812 */     while ((!done) && (!canBeFormatted))
/*      */     {
/*  814 */       double minSizeDiff = 1.7976931348623157E+308D;
/*      */ 
/*  816 */       int rmIdx = -1;
/*      */ 
/*  818 */       for (int ii = 0; ii < inPts.size(); ii++) {
/*  819 */         if ((((Boolean)reduceFlg.get(ii)).booleanValue()) && 
/*  820 */           (removeOnePt(reducePct, maxDist, ii, inPts, reduceFlg, replacementPts, sizeDiff)) && 
/*  821 */           (sizeDiff[0] < minSizeDiff))
/*      */         {
/*  823 */           minSizeDiff = sizeDiff[0];
/*  824 */           rmIdx = ii;
/*  825 */           if ((replacementPts[0] != null) && (replacementPts[1] != null)) {
/*  826 */             pt1 = replacementPts[0];
/*  827 */             pt2 = replacementPts[1];
/*  828 */             replacementPts[0] = null;
/*  829 */             replacementPts[1] = null;
/*      */           }
/*      */           else {
/*  832 */             pt1 = pt2 = null;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  839 */       if (rmIdx < 0) { done = true;
/*      */       } else {
/*  841 */         if ((pt1 != null) && (pt2 != null)) {
/*  842 */           inPts.set((rmIdx - 1 + inPts.size()) % inPts.size(), pt1);
/*  843 */           inPts.set((rmIdx + 1) % inPts.size(), pt2);
/*      */         }
/*      */ 
/*  846 */         inPts.remove(rmIdx);
/*  847 */         reduceFlg.remove(rmIdx);
/*  848 */         canBeFormatted = canBeFormatted(inPts);
/*      */       }
/*      */     }
/*      */ 
/*  852 */     return canBeFormatted;
/*      */   }
/*      */ 
/*      */   private boolean removeOnePt(double incrPct, double incrDist, int rmIdx, List<Coordinate> inPts, List<Boolean> reduceFlg, Coordinate[] replacementPts, double[] sizeDiff)
/*      */   {
/*  867 */     ArrayList newPts = new ArrayList(inPts);
/*  868 */     newPts.remove(rmIdx);
/*  869 */     Coordinate[] newPtArray = (Coordinate[])newPts.toArray(new Coordinate[newPts.size()]);
/*      */ 
/*  871 */     newPts.add(newPtArray[0]);
/*  872 */     GeometryFactory gf = new GeometryFactory();
/*  873 */     SimplePointInRing spir = new SimplePointInRing(gf.createLinearRing((Coordinate[])newPts.toArray(new Coordinate[newPts.size()])));
/*      */ 
/*  875 */     int beforeThePt = (rmIdx - 1 + newPtArray.length) / newPtArray.length;
/*  876 */     int afterThePt = rmIdx / newPtArray.length;
/*      */ 
/*  878 */     if (!spir.isInside((Coordinate)inPts.get(rmIdx)))
/*      */     {
/*  880 */       if ((((Boolean)reduceFlg.get((rmIdx - 1 + inPts.size()) / inPts.size())).booleanValue()) && 
/*  881 */         (((Boolean)reduceFlg.get(rmIdx / inPts.size())).booleanValue()) && 
/*  882 */         (findReplacementPts(incrDist, rmIdx, (Coordinate[])inPts.toArray(new Coordinate[inPts.size()]), replacementPts)))
/*      */       {
/*  884 */         newPtArray[beforeThePt] = replacementPts[0];
/*  885 */         newPtArray[afterThePt] = replacementPts[1];
/*      */ 
/*  889 */         Coordinate[] snappedPt0 = new Coordinate[1];
/*  890 */         GfaSnap.getInstance().snapPtGFA((rmIdx - 1 + inPts.size()) / inPts.size(), 
/*  891 */           (rmIdx - 1 + inPts.size()) / inPts.size(), 
/*  892 */           null, null, Arrays.asList(newPtArray), 
/*  893 */           false, true, 0.0D, snappedPt0);
/*      */ 
/*  895 */         newPtArray[beforeThePt] = snappedPt0[0];
/*      */ 
/*  897 */         Coordinate[] snappedPt1 = new Coordinate[1];
/*  898 */         GfaSnap.getInstance().snapPtGFA(rmIdx / inPts.size(), rmIdx / inPts.size(), 
/*  899 */           null, null, Arrays.asList(newPtArray), 
/*  900 */           false, true, 0.0D, snappedPt1);
/*      */ 
/*  902 */         newPtArray[afterThePt] = snappedPt1[0];
/*      */       }
/*      */       else
/*      */       {
/*  906 */         return false;
/*      */       }
/*      */     }
/*  909 */     inPts.add((Coordinate)inPts.get(0));
/*  910 */     double origArea = gf.createPolygon(gf.createLinearRing((Coordinate[])inPts.toArray(new Coordinate[inPts.size()])), null).getArea();
/*  911 */     inPts.remove(inPts.size() - 1);
/*      */ 
/*  913 */     ArrayList newPtList = new ArrayList(Arrays.asList(newPtArray));
/*  914 */     newPtList.add(newPtArray[0]);
/*  915 */     double newArea = gf.createPolygon(gf.createLinearRing((Coordinate[])newPtList.toArray(new Coordinate[newPtList.size()])), null).getArea();
/*  916 */     sizeDiff[0] = (newArea - origArea);
/*      */ 
/*  918 */     if (sizeDiff[0] / origArea * 100.0D <= incrPct) {
/*  919 */       return true;
/*      */     }
/*      */ 
/*  922 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean findReplacementPts(double maxDistance, int rmIdx, Coordinate[] inPts, Coordinate[] replacementPts)
/*      */   {
/*  935 */     Coordinate ptB = inPts[((rmIdx - 1 + inPts.length) % inPts.length)];
/*  936 */     Coordinate ptBB = inPts[((rmIdx - 2 + inPts.length) % inPts.length)];
/*      */ 
/*  938 */     Coordinate ptA = inPts[((rmIdx + 1) % inPts.length)];
/*  939 */     Coordinate ptAA = inPts[((rmIdx + 2) % inPts.length)];
/*      */ 
/*  941 */     Coordinate pt = inPts[rmIdx];
/*      */ 
/*  944 */     Coordinate ptM = lineIntersection(ptB, ptBB, pt, ptA);
/*  945 */     if ((ptM == null) || (
/*  946 */       (ptM.x >= Math.min(pt.x, ptA.x)) && (ptM.x <= Math.max(pt.x, ptA.x)) && 
/*  947 */       (ptM.y >= Math.min(pt.y, ptA.y)) && (ptM.y <= Math.max(pt.y, ptA.y))))
/*      */     {
/*  949 */       return false;
/*      */     }
/*      */ 
/*  953 */     Coordinate ptN = lineIntersection(ptA, ptAA, pt, ptB);
/*  954 */     if ((ptN == null) || (
/*  955 */       (ptN.x >= Math.min(pt.x, ptB.x)) && (ptN.x <= Math.max(pt.x, ptB.x)) && 
/*  956 */       (ptN.y >= Math.min(pt.y, ptB.y)) && (ptN.y <= Math.max(pt.y, ptB.y))))
/*      */     {
/*  958 */       return false;
/*      */     }
/*      */ 
/*  961 */     Coordinate ptL = new Coordinate();
/*  962 */     if (ptA.x == ptB.x) {
/*  963 */       ptL.x = pt.x;
/*  964 */       pt.y -= ptA.y - ptB.y;
/*      */     }
/*      */     else {
/*  967 */       ptL.x = 0.0D;
/*  968 */       pt.y -= (ptA.y - ptB.y) / (ptA.x - ptB.x) * pt.x;
/*      */     }
/*      */ 
/*  972 */     replacementPts[0] = lineIntersection(ptB, ptBB, pt, ptL);
/*  973 */     if (replacementPts[0] == null) return false;
/*      */ 
/*  976 */     replacementPts[1] = lineIntersection(ptA, ptAA, pt, ptL);
/*  977 */     if (replacementPts[1] == null) return false;
/*      */ 
/*  980 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*      */ 
/*  982 */     gc.setStartingGeographicPoint(ptB.x, ptB.y);
/*  983 */     gc.setDestinationGeographicPoint(replacementPts[0].x, replacementPts[0].y);
/*  984 */     double distMap = gc.getOrthodromicDistance() / 1852.0D;
/*  985 */     if (distMap > 100.0D) return false;
/*      */ 
/*  987 */     gc.setStartingGeographicPoint(ptA.x, ptA.y);
/*  988 */     gc.setDestinationGeographicPoint(replacementPts[1].x, replacementPts[1].y);
/*  989 */     distMap = gc.getOrthodromicDistance() / 1852.0D;
/*  990 */     if (distMap > 100.0D) return false;
/*      */ 
/*  992 */     return true;
/*      */   }
/*      */ 
/*      */   private Coordinate lineIntersection(Coordinate p1, Coordinate p2, Coordinate p3, Coordinate p4)
/*      */   {
/* 1004 */     double d = (p1.x - p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x - p4.x);
/* 1005 */     if (d <= 1.0E-06D) return null;
/*      */ 
/* 1007 */     return new Coordinate(((p3.x - p4.x) * (p1.x * p2.y - p1.y * p2.x) - (p1.x - p2.x) * (p3.x * p4.y - p3.y * p4.x)) / d, 
/* 1008 */       ((p3.y - p4.y) * (p1.x * p2.y - p1.y * p2.x) - (p1.y - p2.y) * (p3.x * p4.y - p3.y - p4.x)) / d);
/*      */   }
/*      */ 
/*      */   private Geometry intersectRings(Geometry g1, Geometry g2)
/*      */   {
/* 1014 */     Geometry clipc = g1.intersection(g2.getBoundary());
/* 1015 */     Geometry clipx = g1.intersection(g2);
/*      */ 
/* 1017 */     Geometry ret = null;
/*      */     try {
/* 1019 */       ret = clipx.getBoundary().difference(clipc);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/* 1024 */     return ret;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.FrzlFormatter
 * JD-Core Version:    0.6.2
 */