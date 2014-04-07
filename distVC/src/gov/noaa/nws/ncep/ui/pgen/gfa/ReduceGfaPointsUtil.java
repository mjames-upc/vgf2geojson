/*     */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Envelope;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineSegment;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import com.vividsolutions.jts.index.quadtree.Quadtree;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.StationTable;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*     */ 
/*     */ public class ReduceGfaPointsUtil
/*     */ {
/*  52 */   static int NM2M = 1852;
/*  53 */   static int LENFROM = 65;
/*  54 */   static int NUMFROM = 3;
/*  55 */   static double RMISSED = -9999.0D;
/*  56 */   static double GDIFFD = 1.0E-06D;
/*  57 */   public static final String[] dirs = { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", 
/*  58 */     "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N" };
/*     */   static StationTable stationTable;
/*     */ 
/*     */   public static StationTable getStationTable()
/*     */   {
/*  63 */     stationTable = PgenStaticDataProvider.getProvider().getVorTbl();
/*  64 */     return stationTable;
/*     */   }
/*     */ 
/*     */   public static boolean canFormatted(List<Coordinate> xyList, String prefix)
/*     */   {
/*  78 */     String fromLine = "";
/*  79 */     String formattedLine = "";
/*  80 */     String separator = "";
/*     */ 
/*  82 */     List xyCopyList = null;
/*  83 */     List vorList = new ArrayList();
/*     */ 
/*  85 */     if (prefix.equalsIgnoreCase("FROM")) {
/*  86 */       separator = " TO ";
/*     */     }
/*  88 */     else if (prefix.equalsIgnoreCase("BOUNDED BY")) {
/*  89 */       separator = "-";
/*     */     }
/*     */ 
/*  92 */     fromLine = prefix + " ";
/*     */ 
/*  94 */     int i = 0;
/*  95 */     int xySize = xyList.size();
/*     */ 
/*  97 */     if ((xyList != null) && (xyList.size() > 3)) {
/*  98 */       xyCopyList = new ArrayList();
/*  99 */       for (Coordinate c : xyList)
/* 100 */         xyCopyList.add(c);
/*     */     }
/*     */     else
/*     */     {
/* 104 */       return true;
/*     */     }
/*     */ 
/* 107 */     xyCopyList = getNorthMostOrder(xyCopyList);
/*     */ 
/* 109 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/* 110 */     for (i = 0; i < xySize; i++) {
/* 111 */       Coordinate loc = (Coordinate)xyCopyList.get(i);
/* 112 */       Station theStation = null;
/* 113 */       if (stationTable != null) {
/* 114 */         theStation = stationTable.getNearestStation(loc);
/*     */       }
/*     */ 
/* 120 */       if (theStation == null) {
/* 121 */         return true;
/*     */       }
/*     */ 
/* 126 */       String vor = getRelative(loc, theStation).trim();
/* 127 */       vor = getRoundedVor(vor);
/* 128 */       vorList.add(vor);
/*     */     }
/*     */ 
/* 132 */     for (int n = 0; n < vorList.size(); n++) {
/* 133 */       fromLine = fromLine + (String)vorList.get(n) + separator;
/*     */     }
/* 135 */     if (fromLine.length() > LENFROM * NUMFROM) {
/* 136 */       return false;
/*     */     }
/* 138 */     formattedLine = getFormattedLine(vorList, prefix);
/*     */ 
/* 140 */     if (formattedLine.length() > LENFROM * NUMFROM + 2) {
/* 141 */       return false;
/*     */     }
/* 143 */     return true;
/*     */   }
/*     */ 
/*     */   public static List<Coordinate> findRemovePt(List<Coordinate> xyList, List<Integer> reduceFlg, List<Integer> rmFlg, List<Double> areaDiff, int index, double incrPct, double incrDst)
/*     */   {
/* 171 */     List resultList = new ArrayList();
/* 172 */     List tmpList = null;
/* 173 */     List xyCloseList = null;
/* 174 */     double sizeDiff = 0.0D;
/* 175 */     int xySize = xyList.size();
/*     */ 
/* 180 */     if ((xyList != null) && (xyList.size() > 3)) {
/* 181 */       xyCloseList = new ArrayList();
/* 182 */       for (Coordinate c : xyList) {
/* 183 */         xyCloseList.add(c);
/*     */       }
/* 185 */       tmpList = new ArrayList();
/* 186 */       for (Coordinate c : xyList)
/* 187 */         tmpList.add(c);
/*     */     }
/*     */     else
/*     */     {
/* 191 */       return null;
/*     */     }
/* 193 */     tmpList.remove(index);
/*     */ 
/* 198 */     Double xyArea = getArea(xyCloseList);
/* 199 */     Double tmpArea = getArea(tmpList);
/*     */ 
/* 205 */     int iib = (index - 1 + xySize) % xySize;
/* 206 */     int iia = (index + 1) % xySize;
/*     */ 
/* 208 */     if (((Coordinate)tmpList.get(0)).equals(tmpList.get(tmpList.size() - 1))) {
/* 209 */       tmpList.remove(tmpList.size() - 1);
/*     */     }
/*     */ 
/* 212 */     if (tmpArea.doubleValue() < xyArea.doubleValue()) {
/* 213 */       if ((reduceFlg == null) || ((((Integer)reduceFlg.get(iib)).intValue() == 1) && (((Integer)reduceFlg.get(iia)).intValue() == 1))) {
/* 214 */         Coordinate coA = findReplacePtsA(xyList, index, incrDst);
/* 215 */         Coordinate coB = findReplacePtsB(xyList, index, incrDst);
/*     */ 
/* 217 */         if ((coA == null) || (coB == null)) {
/* 218 */           rmFlg.set(index, Integer.valueOf(-1));
/* 219 */           return null;
/*     */         }
/*     */ 
/* 226 */         Coordinate[] OaArray = { coA };
/* 227 */         Coordinate[] ObArray = { coB };
/* 228 */         Coordinate[] trans1 = PgenUtil.gridToLatlon(OaArray);
/* 229 */         Coordinate[] trans2 = PgenUtil.gridToLatlon(ObArray);
/* 230 */         if ((Math.abs(trans1[0].x) > 180.0D) || (Math.abs(trans1[0].y) > 90.0D)) {
/* 231 */           return null;
/*     */         }
/* 233 */         double dist = GfaSnap.getInstance().distance(trans1[0], trans2[0]);
/* 234 */         if (dist / NM2M <= 30.0D) {
/* 235 */           coA = null;
/* 236 */           coB = null;
/* 237 */           rmFlg.set(index, Integer.valueOf(-1));
/* 238 */           return null;
/*     */         }
/*     */ 
/* 241 */         resultList.add(0, coB);
/* 242 */         resultList.add(1, coA);
/* 243 */         rmFlg.set(index, Integer.valueOf(1));
/*     */ 
/* 247 */         int tmpSize = tmpList.size();
/* 248 */         int ib = (index - 1 + tmpSize) % tmpSize;
/* 249 */         int ia = index % tmpSize;
/*     */ 
/* 251 */         tmpList.set(ia, coA);
/* 252 */         tmpList.set(ib, coB);
/*     */ 
/* 255 */         Coordinate[] snappedA = new Coordinate[1];
/* 256 */         Coordinate[] snappedB = new Coordinate[1];
/* 257 */         ArrayList transList = PgenUtil.gridToLatlon((ArrayList)tmpList);
/*     */ 
/* 260 */         GfaSnap.getInstance().snapPtGFA(ia, ia, null, null, 
/* 261 */           transList, true, true, 3.0D, snappedA);
/* 262 */         GfaSnap.getInstance().snapPtGFA(ib, ib, null, null, 
/* 263 */           transList, true, true, 3.0D, snappedB);
/*     */ 
/* 265 */         transList.set(ib, snappedB[0]);
/* 266 */         transList.set(ia, snappedA[0]);
/*     */ 
/* 268 */         tmpList = PgenUtil.latlonToGrid(transList);
/*     */       }
/*     */       else
/*     */       {
/* 272 */         rmFlg.set(index, Integer.valueOf(-1));
/* 273 */         return null;
/*     */       }
/*     */     }
/*     */     else {
/* 277 */       rmFlg.set(index, Integer.valueOf(0));
/*     */     }
/*     */ 
/* 281 */     if (((Integer)rmFlg.get(index)).intValue() >= 0) {
/* 282 */       sizeDiff = Math.abs(xyArea.doubleValue() - getArea(tmpList).doubleValue());
/* 283 */       areaDiff.set(index, Double.valueOf(sizeDiff));
/*     */ 
/* 285 */       if ((incrPct >= 0.0D) && (sizeDiff / xyArea.doubleValue() * 100.0D > incrPct)) {
/* 286 */         rmFlg.set(index, Integer.valueOf(-2));
/*     */       }
/*     */     }
/*     */ 
/* 290 */     if (((Integer)rmFlg.get(index)).intValue() > 0) {
/* 291 */       return resultList;
/*     */     }
/* 293 */     return null;
/*     */   }
/*     */ 
/*     */   public static Coordinate findReplacePtsB(List<Coordinate> xyList, int index, double incrDst)
/*     */   {
/* 319 */     Coordinate Ob = null;
/*     */ 
/* 321 */     int xySize = xyList.size();
/* 322 */     int ib = (index - 1 + xySize) % xySize;
/* 323 */     int ibb = (index - 2 + xySize) % xySize;
/* 324 */     int ia = (index + 1) % xySize;
/*     */ 
/* 326 */     Coordinate a = (Coordinate)xyList.get(ia);
/* 327 */     Coordinate p = (Coordinate)xyList.get(index);
/* 328 */     Coordinate b = (Coordinate)xyList.get(ib);
/*     */ 
/* 334 */     LineSegment bb = new LineSegment((Coordinate)xyList.get(ibb), (Coordinate)xyList.get(ib));
/* 335 */     LineSegment ap = new LineSegment((Coordinate)xyList.get(ia), (Coordinate)xyList.get(index));
/*     */ 
/* 337 */     Coordinate m = getIntersection(bb, ap);
/*     */ 
/* 339 */     if ((m != null) && 
/* 340 */       (m.x >= Math.min(a.x, p.x)) && (m.x <= Math.max(a.x, p.x)) && 
/* 341 */       (m.y >= Math.min(a.y, p.y)) && (m.y <= Math.max(a.y, p.y))) {
/* 342 */       return null;
/*     */     }
/*     */ 
/* 348 */     Coordinate l = new Coordinate();
/* 349 */     if (((Coordinate)xyList.get(index)).x != 0.0D) {
/* 350 */       l.x = 0.0D;
/* 351 */       p.y -= (a.y - b.y) / (a.x - b.x) * p.x;
/*     */     }
/*     */     else {
/* 354 */       l.x = 1.0D;
/* 355 */       p.y -= (a.y - b.y) / (a.x - b.x) * (p.x - l.y);
/*     */     }
/*     */ 
/* 359 */     LineSegment lp = new LineSegment(l, (Coordinate)xyList.get(index));
/* 360 */     Ob = getIntersection(bb, lp);
/* 361 */     if (Ob == null) {
/* 362 */       return null;
/*     */     }
/*     */ 
/* 367 */     if (incrDst > 0.0D)
/*     */     {
/* 369 */       Coordinate[] ObArray = { Ob };
/* 370 */       Coordinate[] PtArray = { (Coordinate)xyList.get(ib) };
/* 371 */       Coordinate[] trans1 = PgenUtil.gridToLatlon(ObArray);
/* 372 */       Coordinate[] trans2 = PgenUtil.gridToLatlon(PtArray);
/* 373 */       if ((Math.abs(trans1[0].x) > 180.0D) || (Math.abs(trans1[0].y) > 90.0D)) {
/* 374 */         return null;
/*     */       }
/* 376 */       double dist = GfaSnap.getInstance().distance(trans1[0], trans2[0]);
/*     */ 
/* 378 */       if (dist / NM2M > incrDst) {
/* 379 */         return null;
/*     */       }
/*     */     }
/* 382 */     return Ob;
/*     */   }
/*     */ 
/*     */   public static Coordinate findReplacePtsA(List<Coordinate> xyList, int index, double incrDst)
/*     */   {
/* 393 */     Coordinate Oa = null;
/*     */ 
/* 395 */     int xySize = xyList.size();
/* 396 */     int ib = (index - 1 + xySize) % xySize;
/* 397 */     int ia = (index + 1) % xySize;
/* 398 */     int iaa = (index + 2) % xySize;
/*     */ 
/* 400 */     Coordinate b = (Coordinate)xyList.get(ib);
/* 401 */     Coordinate p = (Coordinate)xyList.get(index);
/* 402 */     Coordinate a = (Coordinate)xyList.get(ia);
/*     */ 
/* 407 */     LineSegment aa = new LineSegment((Coordinate)xyList.get(iaa), (Coordinate)xyList.get(ia));
/* 408 */     LineSegment bp = new LineSegment((Coordinate)xyList.get(ib), (Coordinate)xyList.get(index));
/*     */ 
/* 410 */     Coordinate n = getIntersection(aa, bp);
/* 411 */     if ((n != null) && 
/* 412 */       (n.x >= Math.min(b.x, p.x)) && (n.x <= Math.max(b.x, p.x)) && 
/* 413 */       (n.y >= Math.min(b.y, p.y)) && (n.y <= Math.max(b.y, p.y))) {
/* 414 */       return null;
/*     */     }
/*     */ 
/* 420 */     Coordinate l = new Coordinate();
/* 421 */     if (((Coordinate)xyList.get(index)).x != 0.0D) {
/* 422 */       l.x = 0.0D;
/* 423 */       p.y -= (a.y - b.y) / (a.x - b.x) * p.x;
/*     */     }
/*     */     else {
/* 426 */       l.x = 1.0D;
/* 427 */       p.y -= (a.y - b.y) / (a.x - b.x) * (p.x - l.y);
/*     */     }
/*     */ 
/* 430 */     LineSegment lp = new LineSegment(l, (Coordinate)xyList.get(index));
/* 431 */     Oa = getIntersection(aa, lp);
/* 432 */     if (Oa == null) {
/* 433 */       return null;
/*     */     }
/*     */ 
/* 438 */     if (incrDst > 0.0D)
/*     */     {
/* 444 */       Coordinate[] OaArray = { Oa };
/* 445 */       Coordinate[] PtArray = { (Coordinate)xyList.get(ia) };
/* 446 */       Coordinate[] trans1 = PgenUtil.gridToLatlon(OaArray);
/* 447 */       Coordinate[] trans2 = PgenUtil.gridToLatlon(PtArray);
/* 448 */       if ((Math.abs(trans1[0].x) > 180.0D) || (Math.abs(trans1[0].y) > 90.0D)) {
/* 449 */         return null;
/*     */       }
/* 451 */       double dist = GfaSnap.getInstance().distance(trans1[0], trans2[0]);
/*     */ 
/* 453 */       if (dist / NM2M > incrDst) {
/* 454 */         return null;
/*     */       }
/*     */     }
/* 457 */     return Oa;
/*     */   }
/*     */ 
/*     */   public static Coordinate getIntersection(LineSegment line1, LineSegment line2)
/*     */   {
/* 468 */     Coordinate intersec = null;
/* 469 */     double x = 0.0D;
/* 470 */     double y = 0.0D;
/* 471 */     double m1 = 0.0D;
/* 472 */     double b1 = 0.0D;
/* 473 */     double m2 = 0.0D;
/* 474 */     double b2 = 0.0D;
/*     */ 
/* 476 */     double x1a = line1.getCoordinate(0).x;
/* 477 */     double x1b = line1.getCoordinate(1).x;
/* 478 */     double y1a = line1.getCoordinate(0).y;
/* 479 */     double y1b = line1.getCoordinate(1).y;
/* 480 */     double x2a = line2.getCoordinate(0).x;
/* 481 */     double x2b = line2.getCoordinate(1).x;
/* 482 */     double y2a = line2.getCoordinate(0).y;
/* 483 */     double y2b = line2.getCoordinate(1).y;
/*     */ 
/* 488 */     if (x1a == x1b) {
/* 489 */       x = x1a;
/* 490 */       if (x2a == x2b) return null;
/* 491 */       m2 = (y2b - y2a) / (x2b - x2a);
/* 492 */       b2 = y2a - m2 * x2a;
/* 493 */       y = m2 * x + b2;
/*     */     }
/* 499 */     else if (x2a == x2b) {
/* 500 */       x = x2a;
/* 501 */       if (x1a == x1b) return null;
/* 502 */       m1 = (y1b - y1a) / (x1b - x1a);
/* 503 */       b1 = y1a - m1 * x1a;
/* 504 */       y = m1 * x + b1;
/*     */     }
/*     */     else
/*     */     {
/* 511 */       m1 = (y1b - y1a) / (x1b - x1a);
/* 512 */       b1 = y1a - m1 * x1a;
/*     */ 
/* 514 */       m2 = (y2b - y2a) / (x2b - x2a);
/* 515 */       b2 = y2a - m2 * x2a;
/*     */ 
/* 517 */       if (m1 == m2) {
/* 518 */         x = RMISSED;
/* 519 */         y = RMISSED;
/*     */       }
/* 522 */       else if (m1 == 0.0D) {
/* 523 */         x = (b2 - y1a) / -m2;
/* 524 */         y = y1a;
/*     */       }
/* 526 */       else if (m2 == 0.0D) {
/* 527 */         x = (y2a - b1) / m1;
/* 528 */         y = y2a;
/*     */       }
/*     */       else {
/* 531 */         x = (b2 - b1) / (m1 - m2);
/* 532 */         y = m1 * x + b1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 537 */     if ((x == RMISSED) || (y == RMISSED)) {
/* 538 */       return null;
/*     */     }
/* 540 */     intersec = new Coordinate(x, y);
/* 541 */     return intersec;
/*     */   }
/*     */ 
/*     */   public static Double getArea(List<Coordinate> xyList)
/*     */   {
/* 550 */     Double xyArea = Double.valueOf(0.0D);
/* 551 */     int xySize = xyList.size();
/*     */ 
/* 573 */     List xyCloseList = null;
/*     */ 
/* 575 */     if (xyList != null) {
/* 576 */       xyCloseList = new ArrayList();
/* 577 */       for (Coordinate c : xyList) {
/* 578 */         xyCloseList.add(c);
/*     */       }
/*     */     }
/*     */ 
/* 582 */     if (!((Coordinate)xyCloseList.get(0)).equals(xyCloseList.get(xySize - 1))) {
/* 583 */       xyCloseList.add(xySize, (Coordinate)xyCloseList.get(0));
/*     */     }
/* 585 */     GeometryFactory gf = new GeometryFactory();
/* 586 */     Coordinate[] coorArray = new Coordinate[xyCloseList.size()];
/* 587 */     for (int i = 0; i < xyCloseList.size(); i++) {
/* 588 */       coorArray[i] = ((Coordinate)xyCloseList.get(i));
/*     */     }
/*     */ 
/* 591 */     Polygon xyClosePoly = gf.createPolygon(gf.createLinearRing(coorArray), null);
/* 592 */     xyArea = Double.valueOf(xyClosePoly.getArea());
/*     */ 
/* 594 */     return xyArea;
/*     */   }
/*     */ 
/*     */   public static String getRelative(Coordinate pt, Station st)
/*     */   {
/* 605 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*     */ 
/* 607 */     gc.setStartingGeographicPoint(st.getLongitude().floatValue(), st.getLatitude().floatValue());
/* 608 */     gc.setDestinationGeographicPoint(pt.x, pt.y);
/*     */ 
/* 610 */     long dist = Math.round(gc.getOrthodromicDistance() / NM2M);
/* 611 */     long dir = Math.round(gc.getAzimuth());
/* 612 */     if (dir < 0L) dir += 360L;
/*     */ 
/* 614 */     String str = dist + dirs[((int)Math.round(dir / 22.5D))] + " " + st.getStid();
/* 615 */     return str;
/*     */   }
/*     */ 
/*     */   public static Station getNearestStation(Coordinate loc)
/*     */   {
/* 623 */     double DIST = 1.0D;
/* 624 */     double min = 1.7976931348623157E+308D;
/* 625 */     Station found = null;
/*     */ 
/* 627 */     String path = ReduceGfaPointsUtil.class.getProtectionDomain().getCodeSource().getLocation().toString();
/* 628 */     String subPath = path.substring(5, path.length() - 1);
/* 629 */     subPath = subPath.substring(0, subPath.lastIndexOf("/"));
/* 630 */     String tbl = subPath + "/build.edex/esb/data/utility/cave_ncep/base/stns/vors.xml";
/*     */ 
/* 632 */     StationTable stationTable = new StationTable(tbl);
/* 633 */     Station s = stationTable.getNearestStation(loc);
/*     */ 
/* 635 */     List listOfItems = stationTable.getStationList();
/*     */ 
/* 637 */     GeodeticCalculator gc = new GeodeticCalculator();
/* 638 */     gc.setStartingGeographicPoint(loc.x, loc.y);
/*     */ 
/* 640 */     Envelope searchEnv = new Envelope(loc.x - DIST, loc.x + DIST, loc.y - DIST, loc.y + DIST);
/*     */ 
/* 642 */     Quadtree stTree = new Quadtree();
/*     */     Coordinate c;
/* 643 */     for (Station st : listOfItems) {
/* 644 */       c = new Coordinate(st.getLongitude().floatValue(), st.getLatitude().floatValue());
/* 645 */       Envelope env = new Envelope(c.x - DIST, c.x + DIST, c.y - DIST, c.y + DIST);
/* 646 */       stTree.insert(env, st);
/*     */     }
/*     */ 
/* 649 */     List res = stTree.query(searchEnv);
/* 650 */     if ((res == null) || (res.isEmpty())) {
/* 651 */       DIST = 5.0D;
/* 652 */       searchEnv = new Envelope(loc.x - DIST, loc.x + DIST, loc.y - DIST, loc.y + DIST);
/* 653 */       for (Station st : listOfItems) {
/* 654 */         Coordinate c = new Coordinate(st.getLongitude().floatValue(), st.getLatitude().floatValue());
/* 655 */         Envelope env = new Envelope(c.x - DIST, c.x + DIST, c.y - DIST, c.y + DIST);
/* 656 */         stTree.insert(env, st);
/*     */       }
/* 658 */       res = stTree.query(searchEnv);
/* 659 */       if ((res == null) || (res.isEmpty())) {
/* 660 */         return null;
/*     */       }
/*     */     }
/* 663 */     Iterator iter = res.iterator();
/*     */ 
/* 665 */     while (iter.hasNext()) {
/* 666 */       Station st = (Station)iter.next();
/* 667 */       Coordinate where = new Coordinate(st.getLongitude().floatValue(), st.getLatitude().floatValue());
/* 668 */       gc.setDestinationGeographicPoint(where.x, where.y);
/*     */ 
/* 670 */       double dist = gc.getOrthodromicDistance();
/* 671 */       if (dist < min) {
/* 672 */         min = dist;
/* 673 */         found = st;
/*     */       }
/*     */     }
/*     */ 
/* 677 */     return found;
/*     */   }
/*     */ 
/*     */   public static String getFormattedLine(List<String> vorList, String prefix)
/*     */   {
/* 689 */     String formattedLine = "";
/* 690 */     String separator = "";
/* 691 */     String line1 = "";
/* 692 */     String line2 = "";
/* 693 */     String line3 = "";
/* 694 */     String linePart1 = "";
/* 695 */     String linePart2 = "";
/*     */ 
/* 697 */     if (prefix.equalsIgnoreCase("FROM")) {
/* 698 */       separator = " TO ";
/*     */     }
/* 700 */     else if (prefix.equalsIgnoreCase("BOUNDED BY")) {
/* 701 */       separator = "-";
/*     */     }
/*     */ 
/* 707 */     for (int i = 0; i < vorList.size() - 1; i++) {
/* 708 */       if (((String)vorList.get(i)).toString().equalsIgnoreCase(((String)vorList.get(i + 1)).toString())) {
/* 709 */         vorList.remove(i);
/* 710 */         i--;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 716 */     line1 = prefix + " ";
/* 717 */     for (int i = 0; i < vorList.size(); i++) {
/* 718 */       line1 = line1 + (String)vorList.get(i) + separator;
/*     */     }
/*     */ 
/* 726 */     linePart1 = "";
/* 727 */     linePart2 = "";
/* 728 */     if (line1.length() > LENFROM) {
/* 729 */       linePart1 = line1.substring(0, LENFROM);
/* 730 */       linePart2 = line1.substring(LENFROM);
/*     */     }
/*     */     else {
/* 733 */       formattedLine = formattedLine + line1 + (String)vorList.get(0) + "\n";
/* 734 */       return formattedLine;
/*     */     }
/*     */ 
/* 737 */     if ((linePart1.length() != 0) && ((linePart1.endsWith(" ")) || (linePart1.endsWith("-")))) {
/* 738 */       formattedLine = formattedLine + linePart1 + "\n";
/* 739 */       line2 = linePart2;
/*     */     }
/* 741 */     else if ((linePart2.length() != 0) && ((linePart2.startsWith(" ")) || (linePart2.startsWith("-")))) {
/* 742 */       formattedLine = formattedLine + linePart1 + "\n";
/* 743 */       if (linePart2.startsWith("-"))
/* 744 */         line2 = linePart2;
/*     */       else
/* 746 */         line2 = linePart2.substring(1);
/*     */     }
/*     */     else {
/* 749 */       line2 = linePart2;
/* 750 */       int lastEmp = linePart1.lastIndexOf(" ");
/* 751 */       int lastHyph = linePart1.lastIndexOf("-");
/* 752 */       String tmp = linePart1.substring(0, Math.max(lastEmp, lastHyph) + 1);
/* 753 */       for (int i = Math.max(lastEmp, lastHyph) + 1; i < LENFROM; i++)
/* 754 */         tmp = tmp + " ";
/* 755 */       formattedLine = formattedLine + tmp + "\n";
/* 756 */       line2 = linePart1.substring(Math.max(lastEmp, lastHyph) + 1) + line2;
/*     */     }
/*     */ 
/* 762 */     linePart1 = "";
/* 763 */     linePart2 = "";
/* 764 */     if (line2.length() > LENFROM) {
/* 765 */       linePart1 = line2.substring(0, LENFROM);
/* 766 */       linePart2 = line2.substring(LENFROM);
/*     */     }
/*     */     else {
/* 769 */       formattedLine = formattedLine + line2 + (String)vorList.get(0) + "\n";
/* 770 */       return formattedLine;
/*     */     }
/*     */ 
/* 773 */     if ((linePart1.length() != 0) && ((linePart1.endsWith(" ")) || (linePart1.endsWith("-")))) {
/* 774 */       formattedLine = formattedLine + linePart1 + "\n";
/* 775 */       line3 = linePart2;
/*     */     }
/* 777 */     else if ((linePart2.length() != 0) && ((linePart2.startsWith(" ")) || (linePart2.startsWith("-")))) {
/* 778 */       formattedLine = formattedLine + linePart1 + "\n";
/* 779 */       if (linePart2.startsWith("-"))
/* 780 */         line3 = linePart2;
/*     */       else
/* 782 */         line3 = linePart2.substring(1);
/*     */     }
/*     */     else {
/* 785 */       line3 = linePart2;
/* 786 */       int lastEmp = linePart1.lastIndexOf(" ");
/* 787 */       int lastHyph = linePart1.lastIndexOf("-");
/* 788 */       String temp = linePart1.substring(0, Math.max(lastEmp, lastHyph) + 1);
/* 789 */       for (int i = Math.max(lastEmp, lastHyph) + 1; i < LENFROM; i++)
/* 790 */         temp = temp + " ";
/* 791 */       formattedLine = formattedLine + temp + "\n";
/* 792 */       line3 = linePart1.substring(Math.max(lastEmp, lastHyph) + 1) + line3;
/*     */     }
/*     */ 
/* 798 */     formattedLine = formattedLine + line3 + (String)vorList.get(0) + "\n";
/*     */ 
/* 801 */     return formattedLine;
/*     */   }
/*     */ 
/*     */   private static String getRoundedVor(String vor)
/*     */   {
/* 810 */     String numStr = "";
/* 811 */     int i = 0;
/* 812 */     int rounded = 0;
/* 813 */     int vorSize = vor.length();
/*     */ 
/* 815 */     if ((vor != null) && (!vor.isEmpty())) {
/* 816 */       for (i = 0; i < vorSize; i++) {
/* 817 */         String s = vor.substring(i, i + 1);
/* 818 */         if (!s.matches("[0-9]")) break;
/* 819 */         numStr = numStr + s;
/*     */       }
/*     */ 
/* 824 */       int dist = 0;
/*     */       try {
/* 826 */         dist = Integer.parseInt(numStr);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException)
/*     */       {
/*     */       }
/*     */ 
/* 832 */       rounded = (dist + 5) / 10 * 10;
/* 833 */       if (rounded == 0)
/* 834 */         numStr = vor.substring(vor.indexOf(" ") + 1, vorSize);
/*     */       else {
/* 836 */         numStr = new Integer(rounded).toString() + vor.substring(i, vorSize);
/*     */       }
/*     */     }
/* 839 */     return numStr;
/*     */   }
/*     */ 
/*     */   private static List<Coordinate> getNorthMostOrder(List<Coordinate> xyCopyList)
/*     */   {
/* 849 */     List newList = new ArrayList();
/* 850 */     double northMostLat = 0.0D;
/* 851 */     int northMostI = 0;
/* 852 */     int xySize = 0;
/*     */ 
/* 854 */     if ((xyCopyList != null) && (!xyCopyList.isEmpty())) {
/* 855 */       xySize = xyCopyList.size();
/*     */ 
/* 857 */       northMostLat = ((Coordinate)xyCopyList.get(0)).y;
/*     */ 
/* 859 */       for (int i = 0; i < xySize; i++) {
/* 860 */         if (northMostLat < ((Coordinate)xyCopyList.get(i)).y) {
/* 861 */           northMostLat = ((Coordinate)xyCopyList.get(i)).y;
/* 862 */           northMostI = i;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 870 */       for (int i = northMostI; i < xySize; i++)
/* 871 */         newList.add((Coordinate)xyCopyList.get(i));
/* 872 */       for (int i = 0; i < northMostI; i++) {
/* 873 */         newList.add((Coordinate)xyCopyList.get(i));
/*     */       }
/*     */     }
/* 876 */     return newList;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.ReduceGfaPointsUtil
 * JD-Core Version:    0.6.2
 */