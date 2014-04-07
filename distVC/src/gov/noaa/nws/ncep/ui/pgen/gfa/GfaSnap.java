/*      */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*      */ 
/*      */ import com.vividsolutions.jts.algorithm.CGAlgorithms;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.GeometryFactory;
/*      */ import com.vividsolutions.jts.geom.LineString;
/*      */ import com.vividsolutions.jts.geom.LinearRing;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.viz.common.SnapUtil.SnapVOR;
/*      */ import java.awt.Color;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ public class GfaSnap
/*      */ {
/*   79 */   private static List<Station> snapStns = null;
/*   80 */   private static double EARTH_RADIUS = 6371200.0D;
/*   81 */   private static double RMISSD = -9999.0D;
/*   82 */   protected static double CLUSTER_DIST = 30.0D;
/*   83 */   private static int NUM_SNAP = 100;
/*      */   protected static final double MAP_PRECISION = 0.0001D;
/*      */   private static final double GDIFFD = 1.0E-06D;
/*      */   private static final double ONLINE_TOL = 0.001D;
/*   90 */   private static GfaSnap instance = new GfaSnap();
/*      */ 
/*      */   private GfaSnap()
/*      */   {
/*   96 */     if (snapStns == null)
/*   97 */       snapStns = SnapUtil.SnapVOR.getSnapStns(null, 16);
/*      */   }
/*      */ 
/*      */   public static GfaSnap getInstance()
/*      */   {
/*  107 */     return instance;
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> snapPolygon1(List<Coordinate> poly, GeometryFactory gf)
/*      */   {
/*  120 */     ArrayList snapPtsList = new ArrayList();
/*      */ 
/*  123 */     if (gf == null) gf = GfaFormat.getGeometryFactory();
/*      */ 
/*  126 */     ArrayList coors = reorderInClockwise(poly, gf);
/*      */ 
/*  131 */     Coordinate coor = null;
/*      */ 
/*  133 */     for (int ii = 0; (coors != null) && (ii < coors.size()); ii++)
/*      */     {
/*  135 */       coor = (Coordinate)coors.get(ii);
/*      */ 
/*  138 */       TreeMap snapPtsTreeMap = sortSnapPoints(coor, NUM_SNAP);
/*      */ 
/*  141 */       Station first = (Station)snapPtsTreeMap.firstEntry().getValue();
/*      */ 
/*  144 */       Coordinate coorNext = ii + 1 >= coors.size() ? (Coordinate)coors.get(0) : (Coordinate)coors.get(ii + 1);
/*  145 */       Coordinate coorPrev = ii - 1 < 0 ? (Coordinate)coors.get(coors.size() - 1) : (Coordinate)coors.get(ii - 1);
/*      */ 
/*  151 */       while (!snapPtsTreeMap.isEmpty()) {
/*  152 */         Double key = (Double)snapPtsTreeMap.firstKey();
/*  153 */         Station s = (Station)snapPtsTreeMap.remove(key);
/*  154 */         Coordinate coorSnap = new Coordinate(s.getLongitude().floatValue(), s.getLatitude().floatValue());
/*      */ 
/*  156 */         if ((onLeft(coorSnap, coorPrev, coor)) && 
/*  157 */           (onLeft(coorSnap, coor, coorNext)) && 
/*  158 */           (!snapPtsList.contains(coorSnap)))
/*      */         {
/*  160 */           snapPtsList.add(coorSnap);
/*  161 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  166 */       if (snapPtsTreeMap.isEmpty()) {
/*  167 */         snapPtsList.add(new Coordinate(first.getLongitude().floatValue(), first.getLatitude().floatValue()));
/*      */       }
/*      */ 
/*  170 */       snapPtsTreeMap.clear();
/*      */     }
/*      */ 
/*  174 */     return snapPtsList;
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> snapPolygon(ArrayList<Coordinate> pointsIn)
/*      */   {
/*  190 */     return snapPolygon(Boolean.valueOf(true), 0.0F, Boolean.valueOf(true), Boolean.valueOf(true), pointsIn);
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> snapPolygon(Boolean expandOnly, float tolerance, Boolean reorder, Boolean closed, ArrayList<Coordinate> pointsIn)
/*      */   {
/*  210 */     ArrayList snappedPts = new ArrayList();
/*      */ 
/*  213 */     if (pointsIn.size() < 3) {
/*  214 */       snappedPts.addAll(pointsIn);
/*  215 */       return snappedPts;
/*      */     }
/*      */ 
/*  220 */     ArrayList snappedOne = snapOneRound(expandOnly, tolerance, reorder.booleanValue(), 
/*  221 */       closed.booleanValue(), pointsIn);
/*      */ 
/*  224 */     boolean done = false;
/*  225 */     int nround = 0;
/*      */ 
/*  229 */     while (!done)
/*      */     {
/*  231 */       int npts = snappedOne.size();
/*  232 */       boolean clusterFound = false;
/*      */ 
/*  236 */       for (int ii = 0; ii < npts; ii++)
/*      */       {
/*  238 */         int next = ii + 1 < npts ? ii + 1 : 0;
/*      */ 
/*  240 */         if (isCluster((Coordinate)snappedOne.get(ii), (Coordinate)snappedOne.get(next))) {
/*  241 */           clusterFound = true;
/*  242 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  247 */       if (clusterFound) {
/*  248 */         snappedOne = snapOneRound(expandOnly, tolerance, false, closed.booleanValue(), snappedOne);
/*      */       }
/*      */       else {
/*  251 */         done = true;
/*      */       }
/*      */ 
/*  255 */       nround++;
/*  256 */       if (nround > 5) done = true;
/*      */     }
/*      */ 
/*  259 */     snappedPts.addAll(snappedOne);
/*      */ 
/*  261 */     return snappedPts;
/*      */   }
/*      */ 
/*      */   private ArrayList<Coordinate> snapOneRound(Boolean expandOnly, float tolerance, boolean reorder, boolean closed, ArrayList<Coordinate> inCoors)
/*      */   {
/*  279 */     ArrayList snappedPts = new ArrayList();
/*      */ 
/*  282 */     if (inCoors.size() < 3) {
/*  283 */       snappedPts.addAll(inCoors);
/*  284 */       return snappedPts;
/*      */     }
/*      */ 
/*  289 */     ArrayList coors = new ArrayList();
/*  290 */     if (reorder) {
/*  291 */       coors.addAll(reorderInClockwise(inCoors, null));
/*      */     }
/*      */     else {
/*  294 */       coors.addAll(inCoors);
/*      */     }
/*      */ 
/*  303 */     int ii = 0;
/*  304 */     int jj = 0;
/*      */ 
/*  306 */     if (closed) {
/*  307 */       while ((jj < coors.size()) && 
/*  308 */         (isCluster((Coordinate)coors.get(ii), (Coordinate)coors.get(coors.size() - 1))))
/*      */       {
/*  310 */         coors = shiftArray(coors);
/*  311 */         jj++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  318 */     ii = 0;
/*  319 */     boolean done = false;
/*  320 */     int ier_prev = 0;
/*  321 */     int npts = coors.size();
/*      */ 
/*  324 */     Coordinate[] snappedPt = new Coordinate[1];
/*  325 */     ArrayList chkPts = new ArrayList();
/*      */ 
/*  327 */     while (!done)
/*      */     {
/*  330 */       int ii2 = ii;
/*  331 */       while ((ii2 + 1 < npts) && 
/*  332 */         (isCluster((Coordinate)coors.get(ii), (Coordinate)coors.get(ii2 + 1)))) {
/*  333 */         ii2++;
/*      */       }
/*  335 */       int nshift = ii2 - ii;
/*      */ 
/*  344 */       if (ii - 1 >= 0) {
/*  345 */         chkPts.clear();
/*  346 */         chkPts.add((Coordinate)snappedPts.get(ii - 1));
/*      */ 
/*  348 */         if (ii == coors.size() - 1)
/*  349 */           chkPts.add((Coordinate)snappedPts.get(0));
/*      */       }
/*      */       int status;
/*      */       int status;
/*  359 */       if (!isInFaBound((Coordinate)coors.get(ii))) {
/*  360 */         snappedPt[0] = new Coordinate((Coordinate)coors.get(ii));
/*  361 */         status = 0;
/*      */       }
/*      */       else
/*      */       {
/*  365 */         status = snapPtGFA(ii, ii2, null, null, coors, false, true, tolerance, snappedPt);
/*      */       }
/*      */ 
/*  368 */       snappedPts.add(new Coordinate(snappedPt[0]));
/*      */ 
/*  374 */       if (status == 1) {
/*  375 */         snappedPts.set(snappedPts.size() - 1, new Coordinate((Coordinate)coors.get(ii)));
/*      */       }
/*  377 */       else if (status == 2)
/*      */       {
/*  384 */         if (ier_prev == 2)
/*      */         {
/*  394 */           if (ii - 1 >= 0) {
/*  395 */             coors = collapseArray(coors, ii - 1, 1);
/*  396 */             npts = coors.size();
/*  397 */             ii--;
/*      */           }
/*      */ 
/*  400 */           snappedPts.set(snappedPts.size() - 1, new Coordinate((Coordinate)coors.get(ii)));
/*      */         }
/*      */         else
/*      */         {
/*  404 */           coors = insertArray(coors, ii, snappedPt[0]);
/*  405 */           npts = coors.size();
/*      */         }
/*      */       }
/*  408 */       else if (status < 0) {
/*  409 */         done = true;
/*  410 */         status = -2;
/*      */       }
/*  412 */       else if (nshift != 0)
/*      */       {
/*  414 */         coors = collapseArray(coors, ii, nshift);
/*  415 */         coors.set(ii, snappedPt[0]);
/*  416 */         npts = coors.size();
/*      */       }
/*      */ 
/*  419 */       ii++;
/*  420 */       if (ii >= npts) done = true;
/*  421 */       ier_prev = status;
/*      */     }
/*      */ 
/*  424 */     return snappedPts;
/*      */   }
/*      */ 
/*      */   public int snapPtGFA(int index, int index2, List<Coordinate> usedPts, List<Coordinate> checkPts, List<Coordinate> smearPts, boolean checkDist, boolean expandOnly, double tolerance, Coordinate[] snapped)
/*      */   {
/*  457 */     int status = 1;
/*  458 */     int snap_indx1 = index;
/*  459 */     int snap_indx2 = index2;
/*  460 */     Coordinate tPoint = (Coordinate)smearPts.get(snap_indx1);
/*      */ 
/*  462 */     Coordinate snappedPt = new Coordinate(tPoint);
/*      */ 
/*  465 */     if (tolerance <= 0.0D) {
/*  466 */       tolerance = 3.0D;
/*      */     }
/*      */ 
/*  470 */     ArrayList snapPts = getNumSnapPoints(tPoint, NUM_SNAP);
/*      */ 
/*  475 */     if ((index == index2) && (isSnapPoint(tPoint, snapPts))) {
/*  476 */       snapped[0] = new Coordinate(tPoint);
/*  477 */       status = 0;
/*  478 */       return status;
/*      */     }
/*      */ 
/*  493 */     int nclose = snapPts.size();
/*  494 */     int npts = smearPts.size();
/*      */ 
/*  497 */     Coordinate[] coorL2 = new Coordinate[2];
/*  498 */     coorL2[0] = ((Coordinate)smearPts.get((snap_indx2 + npts + 1) % npts));
/*  499 */     coorL2[1] = ((Coordinate)smearPts.get((snap_indx2 + npts) % npts));
/*      */ 
/*  502 */     Coordinate[] coorB = new Coordinate[2];
/*  503 */     coorB[0] = ((Coordinate)smearPts.get((snap_indx1 + npts - 1) % npts));
/*      */ 
/*  506 */     Coordinate[] coorA = new Coordinate[2];
/*  507 */     coorA[0] = new Coordinate(coorL2[0]);
/*      */ 
/*  510 */     Coordinate[] gfaPts = (Coordinate[])smearPts.toArray(new Coordinate[smearPts.size()]);
/*  511 */     Polygon gfaPolygon = null;
/*  512 */     if (gfaPts.length > 2) {
/*  513 */       gfaPolygon = GfaClip.getInstance().pointsToPolygon(PgenUtil.latlonToGrid(gfaPts));
/*      */     }
/*      */ 
/*  516 */     boolean done = false;
/*  517 */     boolean firstSnap = false;
/*      */ 
/*  519 */     int firstSnapIndex = -1;
/*      */ 
/*  522 */     while (!done)
/*      */     {
/*  524 */       for (int ii = 0; ii < nclose; ii++)
/*      */       {
/*  529 */         if ((usedPts == null) || (!usedPts.contains(snapPts.get(ii))))
/*      */         {
/*  537 */           if ((!checkDist) || ((!isCluster(coorB[0], (Coordinate)snapPts.get(ii))) && 
/*  538 */             (!isCluster(coorA[0], (Coordinate)snapPts.get(ii)))))
/*      */           {
/*  546 */             boolean qualify = true;
/*  547 */             if (checkPts != null) {
/*  548 */               for (Coordinate cc : checkPts) {
/*  549 */                 if (isCluster(cc, (Coordinate)snapPts.get(ii))) {
/*  550 */                   qualify = false;
/*  551 */                   break;
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/*  556 */             if (qualify)
/*      */             {
/*  563 */               double dist = distance(tPoint, (Coordinate)snapPts.get(ii)) / 1852.0D;
/*  564 */               if ((dist <= tolerance) && (!expandOnly)) {
/*  565 */                 snappedPt = new Coordinate((Coordinate)snapPts.get(ii));
/*  566 */                 done = true;
/*  567 */                 break;
/*      */               }
/*      */               Coordinate[] line;
/*      */               Geometry seg;
/*  580 */               if (expandOnly)
/*      */               {
/*  582 */                 coorB[1] = ((Coordinate)snapPts.get(ii));
/*  583 */                 coorA[1] = ((Coordinate)snapPts.get(ii));
/*      */ 
/*  585 */                 boolean accept = true;
/*      */ 
/*  587 */                 for (int jj = snap_indx1; jj <= snap_indx2; jj++)
/*      */                 {
/*  589 */                   Coordinate candidatePt = (Coordinate)smearPts.get((jj + npts) % npts);
/*      */ 
/*  600 */                   if (atLeft(candidatePt, coorB, false, 0.001D)) {
/*  601 */                     accept = false;
/*  602 */                     break;
/*      */                   }
/*      */ 
/*  605 */                   if (!atLeft(candidatePt, coorA, false, 0.001D)) {
/*  606 */                     accept = false;
/*  607 */                     break;
/*      */                   }
/*      */ 
/*      */                 }
/*      */ 
/*  612 */                 if (accept)
/*      */                 {
/*  614 */                   if (!firstSnap) {
/*  615 */                     firstSnap = true;
/*  616 */                     firstSnapIndex = ii;
/*      */                   }
/*      */ 
/*  619 */                   snappedPt = new Coordinate((Coordinate)snapPts.get(ii));
/*  620 */                   status = 0;
/*  621 */                   done = true;
/*  622 */                   break;
/*      */                 }
/*      */ 
/*  626 */                 if ((!done) && (ii == nclose - 1) && (firstSnap) && 
/*  627 */                   (firstSnapIndex >= 0))
/*      */                 {
/*  630 */                   snappedPt = new Coordinate((Coordinate)snapPts.get(firstSnapIndex));
/*  631 */                   status = -2;
/*  632 */                   done = true;
/*  633 */                   break;
/*      */                 }
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*  640 */                 line = new Coordinate[] { tPoint, (Coordinate)snapPts.get(ii) };
/*  641 */                 seg = GfaClip.getInstance().pointsToGeometry(
/*  642 */                   PgenUtil.latlonToGrid(line));
/*      */ 
/*  644 */                 if ((gfaPolygon == null) || (!gfaPolygon.isValid()) || (seg.intersection(gfaPolygon).getCoordinates().length == 1)) {
/*  645 */                   snappedPt = new Coordinate((Coordinate)snapPts.get(ii));
/*  646 */                   status = 0;
/*  647 */                   done = true;
/*  648 */                   break;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  654 */       if (ii >= nclose)
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  679 */     snapped[0] = new Coordinate(snappedPt);
/*      */ 
/*  681 */     return status;
/*      */   }
/*      */ 
/*      */   public Coordinate snapOnePt(Coordinate coor)
/*      */   {
/*  693 */     Coordinate snapped = coor;
/*      */ 
/*  696 */     ArrayList snapPts = getNumSnapPoints(coor, 1);
/*  697 */     if (snapPts.size() > 0) {
/*  698 */       snapped = new Coordinate((Coordinate)snapPts.get(0));
/*      */     }
/*      */ 
/*  701 */     return snapped;
/*      */   }
/*      */ 
/*      */   private TreeMap<Double, Station> sortSnapPoints(Coordinate coor, int nclosest)
/*      */   {
/*  717 */     TreeMap tmapSimple = new TreeMap();
/*  718 */     TreeMap tmap = new TreeMap();
/*      */     double dx;
/*  722 */     for (Station stn : snapStns)
/*      */     {
/*  724 */       dx = coor.x - stn.getLongitude().floatValue();
/*  725 */       double dy = coor.y - stn.getLatitude().floatValue();
/*  726 */       double dist = dx * dx + dy * dy;
/*      */ 
/*  728 */       tmapSimple.put(Double.valueOf(dist), stn);
/*      */     }
/*      */ 
/*  732 */     int ii = 0;
/*  733 */     for (Station stn : tmapSimple.values()) {
/*  734 */       double dist = distance(coor, new Coordinate(stn.getLongitude().floatValue(), stn.getLatitude().floatValue()));
/*  735 */       tmap.put(Double.valueOf(dist), stn);
/*      */ 
/*  737 */       ii++;
/*      */ 
/*  739 */       if (ii > nclosest * 5) {
/*      */         break;
/*      */       }
/*      */     }
/*  743 */     return tmap;
/*      */   }
/*      */ 
/*      */   public boolean isCluster(Coordinate p1, Coordinate p2)
/*      */   {
/*  755 */     boolean clustered = false;
/*      */ 
/*  757 */     double dist = distance(p1, p2) / 1852.0D;
/*  758 */     if ((dist > 0.0D) && (dist <= CLUSTER_DIST)) {
/*  759 */       clustered = true;
/*      */     }
/*      */ 
/*  762 */     return clustered;
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> shiftArray(ArrayList<Coordinate> list)
/*      */   {
/*  774 */     ArrayList newlist = new ArrayList();
/*      */ 
/*  776 */     Coordinate first = (Coordinate)list.get(0);
/*  777 */     List after = list.subList(1, list.size());
/*      */ 
/*  779 */     for (Coordinate c : after) {
/*  780 */       newlist.add(new Coordinate(c));
/*      */     }
/*      */ 
/*  783 */     newlist.add(new Coordinate(first));
/*      */ 
/*  785 */     return newlist;
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> collapseArray(ArrayList<Coordinate> list, int start, int nshift)
/*      */   {
/*  798 */     ArrayList newlist = new ArrayList();
/*      */ 
/*  800 */     if ((start < 0) || (nshift < 0) || (start > list.size() - 1) || 
/*  801 */       (start + nshift > list.size()))
/*      */     {
/*  803 */       for (Coordinate c : list) {
/*  804 */         newlist.add(new Coordinate(c));
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  810 */       List before = list.subList(0, start);
/*  811 */       List after = list.subList(start + nshift, list.size());
/*      */ 
/*  813 */       for (Coordinate c : before) {
/*  814 */         newlist.add(new Coordinate(c));
/*      */       }
/*      */ 
/*  817 */       for (Coordinate c : after) {
/*  818 */         newlist.add(new Coordinate(c));
/*      */       }
/*      */     }
/*      */ 
/*  822 */     return newlist;
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> insertArray(ArrayList<Coordinate> list, int index, Coordinate pt)
/*      */   {
/*  837 */     ArrayList newlist = new ArrayList();
/*      */ 
/*  839 */     List before = list.subList(0, index);
/*  840 */     List after = list.subList(index, list.size());
/*      */ 
/*  842 */     for (Coordinate c : before) {
/*  843 */       newlist.add(new Coordinate(c));
/*      */     }
/*      */ 
/*  846 */     newlist.add(new Coordinate(pt));
/*      */ 
/*  848 */     for (Coordinate c : after) {
/*  849 */       newlist.add(new Coordinate(c));
/*      */     }
/*      */ 
/*  852 */     return newlist;
/*      */   }
/*      */ 
/*      */   private ArrayList<Coordinate> getNumSnapPoints(Coordinate coor, int num)
/*      */   {
/*  864 */     TreeMap snapPtsTreeMap = sortSnapPoints(coor, num);
/*      */ 
/*  866 */     ArrayList snapPts = new ArrayList();
/*      */ 
/*  868 */     if (num <= 0) {
/*  869 */       num = NUM_SNAP;
/*      */     }
/*      */ 
/*  872 */     int nn = 0;
/*  873 */     while ((nn <= num) && (!snapPtsTreeMap.isEmpty()))
/*      */     {
/*  875 */       Double key = (Double)snapPtsTreeMap.firstKey();
/*  876 */       Station s = (Station)snapPtsTreeMap.remove(key);
/*      */ 
/*  878 */       snapPts.add(new Coordinate(s.getLongitude().floatValue(), s.getLatitude().floatValue()));
/*  879 */       nn++;
/*      */     }
/*      */ 
/*  883 */     return snapPts;
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> reorderInClockwise(List<Coordinate> coors, GeometryFactory gf)
/*      */   {
/*  896 */     ArrayList newList = new ArrayList();
/*      */ 
/*  899 */     if (gf == null) gf = GfaFormat.getGeometryFactory();
/*      */ 
/*  901 */     Coordinate[] tmp = new Coordinate[coors.size() + 1];
/*  902 */     coors.toArray(tmp);
/*  903 */     tmp[(tmp.length - 1)] = tmp[0];
/*      */ 
/*  908 */     if (!CGAlgorithms.isCCW(tmp)) {
/*  909 */       newList.addAll(coors);
/*      */     }
/*      */     else {
/*  912 */       LinearRing shell = gf.createLinearRing(tmp);
/*  913 */       LineString ls = (LineString)shell.reverse();
/*  914 */       shell = gf.createLinearRing(ls.getCoordinates());
/*  915 */       tmp = shell.getCoordinates();
/*      */ 
/*  917 */       newList.addAll(Arrays.asList(tmp));
/*  918 */       newList.remove(newList.size() - 1);
/*      */     }
/*      */ 
/*  921 */     return newList;
/*      */   }
/*      */ 
/*      */   public boolean onLeft(Coordinate snapPt, Coordinate start, Coordinate end)
/*      */   {
/*  938 */     Coordinate[] points = { snapPt, start, end };
/*  939 */     Coordinate[] gridPts = PgenUtil.latlonToGrid(points);
/*      */ 
/*  941 */     Coordinate coorSnap = gridPts[0];
/*  942 */     Coordinate c1 = gridPts[1];
/*  943 */     Coordinate c2 = gridPts[2];
/*      */ 
/*  946 */     double theta1 = Math.atan2(c2.y - c1.y, c2.x - c1.x);
/*      */ 
/*  949 */     double theta2 = Math.atan2(coorSnap.y - c1.y, coorSnap.x - c1.x);
/*      */ 
/*  952 */     double delta = theta2 - theta1;
/*      */ 
/*  955 */     if (delta > 3.141592653589793D) {
/*  956 */       delta -= 6.283185307179586D;
/*      */     }
/*  958 */     else if (delta < -3.141592653589793D) {
/*  959 */       delta += 6.283185307179586D;
/*      */     }
/*      */ 
/*  962 */     return delta > 0.0D;
/*      */   }
/*      */ 
/*      */   private ArrayList<Double> distance(Coordinate startPt, ArrayList<Coordinate> endPts)
/*      */   {
/*  978 */     ArrayList dist = new ArrayList();
/*  979 */     for (Coordinate pt : endPts) {
/*  980 */       dist.add(Double.valueOf(distance(startPt, pt)));
/*      */     }
/*      */ 
/*  983 */     return dist;
/*      */   }
/*      */ 
/*      */   public double distance(Coordinate startPt, Coordinate endPt)
/*      */   {
/*  997 */     double dist = RMISSD;
/*      */ 
/* 1000 */     double lat1 = startPt.y;
/* 1001 */     double lon1 = startPt.x;
/* 1002 */     double lat2 = endPt.y;
/* 1003 */     double lon2 = endPt.x;
/*      */ 
/* 1005 */     if ((lat1 < -90.0D) || (lat1 > 90.0D) || 
/* 1006 */       (lon1 < -180.0D) || (lon1 > 180.0D) || 
/* 1007 */       (lat2 < -90.0D) || (lat2 > 90.0D) || 
/* 1008 */       (lon2 < -180.0D) || (lon2 > 180.0D)) {
/* 1009 */       dist = RMISSD;
/*      */     }
/*      */     else
/*      */     {
/* 1013 */       double dtr = 0.0174532925199433D;
/* 1014 */       double rlat1 = dtr * lat1;
/* 1015 */       double rlat2 = dtr * lat2;
/* 1016 */       double rlon1 = dtr * lon1;
/* 1017 */       double rlon2 = dtr * lon2;
/*      */ 
/* 1019 */       double dlon = rlon1 - rlon2;
/*      */ 
/* 1022 */       double val = Math.sin(rlat1) * Math.sin(rlat2) + 
/* 1023 */         Math.cos(rlat1) * Math.cos(rlat2) * Math.cos(dlon);
/*      */ 
/* 1025 */       dist = 0.0D;
/* 1026 */       if ((-1.0D <= val) && (val <= 1.0D)) {
/* 1027 */         dist = EARTH_RADIUS * Math.acos(val);
/*      */       }
/*      */     }
/*      */ 
/* 1031 */     return dist;
/*      */   }
/*      */ 
/*      */   public void createSnapPointFiles()
/*      */   {
/* 1040 */     ArrayList pts = new ArrayList();
/* 1041 */     for (Station s : snapStns) {
/* 1042 */       pts.add(new Coordinate(s.getLongitude().floatValue(), s.getLatitude().floatValue()));
/*      */     }
/*      */ 
/* 1045 */     ArrayList ptsIn = new ArrayList();
/* 1046 */     for (Coordinate c : pts) {
/* 1047 */       if ((c.x > -79.0D) && (c.y > 22.0D) && (c.y < 35.0D)) {
/* 1048 */         ptsIn.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1052 */     writeSnapFile("snapTest", ptsIn);
/* 1053 */     ptsIn.clear();
/*      */ 
/* 1056 */     Geometry bnd = GfaClip.getInstance().getFaInternationalBound();
/*      */ 
/* 1058 */     for (Coordinate c : pts) {
/* 1059 */       Geometry p = GfaClip.getInstance().pointsToGeometry(new Coordinate[] { c });
/* 1060 */       if (!bnd.contains(p)) {
/* 1061 */         ptsIn.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1065 */     writeSnapFile("snapPointsOutside", ptsIn);
/* 1066 */     ptsIn.clear();
/*      */ 
/* 1069 */     HashMap areabnd = GfaClip.getInstance().getFaAreaBounds();
/* 1070 */     Geometry SFO = (Geometry)areabnd.get("SFO");
/* 1071 */     Geometry SLC = (Geometry)areabnd.get("SLC");
/* 1072 */     Geometry CHI = (Geometry)areabnd.get("CHI");
/* 1073 */     Geometry DFW = (Geometry)areabnd.get("DFW");
/* 1074 */     Geometry BOS = (Geometry)areabnd.get("BOS");
/* 1075 */     Geometry MIA = (Geometry)areabnd.get("MIA");
/*      */ 
/* 1077 */     ArrayList ppp = new ArrayList();
/*      */ 
/* 1080 */     for (Coordinate c : pts) {
/* 1081 */       if (c.x <= -113.0D) {
/* 1082 */         ptsIn.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1086 */     for (Coordinate c : ptsIn) {
/* 1087 */       Geometry p = GfaClip.getInstance().pointsToGeometry(new Coordinate[] { c });
/* 1088 */       if (!SLC.contains(p)) {
/* 1089 */         ppp.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1093 */     writeSnapFile("snapPointsSFO", ppp);
/* 1094 */     ptsIn.clear();
/* 1095 */     ppp.clear();
/*      */ 
/* 1098 */     for (Coordinate c : pts) {
/* 1099 */       if ((c.x >= -122.0D) && (c.x <= -101.0D)) {
/* 1100 */         ptsIn.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1104 */     for (Coordinate c : ptsIn) {
/* 1105 */       Geometry p = GfaClip.getInstance().pointsToGeometry(new Coordinate[] { c });
/* 1106 */       if ((!SFO.contains(p)) && (!CHI.contains(p)) && (!DFW.contains(p))) {
/* 1107 */         ppp.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1111 */     writeSnapFile("snapPointsSLC", ppp);
/* 1112 */     ptsIn.clear();
/* 1113 */     ppp.clear();
/*      */ 
/* 1116 */     for (Coordinate c : pts) {
/* 1117 */       if ((c.x >= -105.0D) && (c.x <= -81.0D) && (c.y >= 36.0D)) {
/* 1118 */         ptsIn.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1122 */     for (Coordinate c : ptsIn) {
/* 1123 */       Geometry p = GfaClip.getInstance().pointsToGeometry(new Coordinate[] { c });
/* 1124 */       if ((!SLC.contains(p)) && (!BOS.contains(p)) && (!DFW.contains(p)) && (!MIA.contains(p))) {
/* 1125 */         ppp.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1129 */     writeSnapFile("snapPointsCHI", ppp);
/* 1130 */     ptsIn.clear();
/* 1131 */     ppp.clear();
/*      */ 
/* 1134 */     for (Coordinate c : pts) {
/* 1135 */       if ((c.x >= -107.0D) && (c.x <= -81.0D) && (c.y <= 37.5D)) {
/* 1136 */         ptsIn.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1140 */     for (Coordinate c : ptsIn) {
/* 1141 */       Geometry p = GfaClip.getInstance().pointsToGeometry(new Coordinate[] { c });
/* 1142 */       if ((!SLC.contains(p)) && (!CHI.contains(p)) && (!MIA.contains(p)) && (!BOS.contains(p))) {
/* 1143 */         ppp.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1147 */     writeSnapFile("snapPointsDFW", ppp);
/* 1148 */     ptsIn.clear();
/* 1149 */     ppp.clear();
/*      */ 
/* 1152 */     for (Coordinate c : pts) {
/* 1153 */       if ((c.x >= -86.0D) && (c.y >= 36.0D)) {
/* 1154 */         ptsIn.add(c);
/*      */       }
/*      */     }
/* 1157 */     for (Coordinate c : ptsIn) {
/* 1158 */       Geometry p = GfaClip.getInstance().pointsToGeometry(new Coordinate[] { c });
/* 1159 */       if ((!MIA.contains(p)) && (!CHI.contains(p)) && (!DFW.contains(p))) {
/* 1160 */         ppp.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1164 */     writeSnapFile("snapPointsBOS", ppp);
/* 1165 */     ptsIn.clear();
/* 1166 */     ppp.clear();
/*      */ 
/* 1169 */     for (Coordinate c : pts) {
/* 1170 */       if ((c.x >= -88.5D) && (c.y <= 37.5D)) {
/* 1171 */         ptsIn.add(c);
/*      */       }
/*      */     }
/* 1174 */     for (Coordinate c : ptsIn) {
/* 1175 */       Geometry p = GfaClip.getInstance().pointsToGeometry(new Coordinate[] { c });
/* 1176 */       if ((!BOS.contains(p)) && (!CHI.contains(p)) && (!DFW.contains(p))) {
/* 1177 */         ppp.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 1181 */     writeSnapFile("snapPointsMIA", ppp);
/* 1182 */     ptsIn.clear();
/* 1183 */     ppp.clear();
/*      */   }
/*      */ 
/*      */   private void writeSnapFile(String fname, List<Coordinate> pts)
/*      */   {
/* 1192 */     Product activeProduct = new Product("Default", "Default", "Default", 
/* 1193 */       new ProductInfo(), new ProductTime(), new ArrayList());
/*      */ 
/* 1195 */     Layer activeLayer = new Layer();
/* 1196 */     activeProduct.addLayer(activeLayer);
/*      */ 
/* 1198 */     List productList = new ArrayList();
/* 1199 */     productList.add(activeProduct);
/*      */ 
/* 1201 */     for (Coordinate c : pts) {
/* 1202 */       Symbol cmm = new Symbol(null, new Color[] { Color.green }, 
/* 1203 */         1.0F, 0.5D, Boolean.valueOf(false), c, "Symbol", "TRIANGLE_SPCL");
/* 1204 */       activeLayer.add(cmm);
/*      */     }
/*      */ 
/* 1207 */     System.out.println("Total snap stations for " + fname + ": " + pts.size());
/* 1208 */     Products filePrds1 = ProductConverter.convert(productList);
/* 1209 */     String aa = "/export/cdbsrv/jwu/" + fname + ".xml";
/* 1210 */     FileTools.write(aa, filePrds1);
/*      */   }
/*      */ 
/*      */   private boolean isInFaBound(Coordinate pt)
/*      */   {
/* 1221 */     Geometry intlBoundInGrid = GfaClip.getInstance().getFaInternationalBoundInGrid();
/*      */ 
/* 1223 */     Coordinate[] points = { pt };
/* 1224 */     Coordinate[] ptInGrid = PgenUtil.latlonToGrid(points);
/*      */ 
/* 1226 */     Geometry ptGeom = GfaClip.getInstance().pointsToGeometry(ptInGrid);
/*      */ 
/* 1228 */     return intlBoundInGrid.covers(ptGeom);
/*      */   }
/*      */ 
/*      */   public boolean isSamePoint(Coordinate c1, Coordinate c2, double pres)
/*      */   {
/* 1243 */     if ((Math.abs(c1.x - c2.x) < pres) && (Math.abs(c1.y - c2.y) < pres)) {
/* 1244 */       return true;
/*      */     }
/*      */ 
/* 1247 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isSnapPoint(Coordinate cc, ArrayList<Coordinate> snapPtsList)
/*      */   {
/* 1263 */     if ((snapPtsList.size() >= 0) && (isSamePoint(cc, (Coordinate)snapPtsList.get(0), 0.0001D))) {
/* 1264 */       return true;
/*      */     }
/*      */ 
/* 1267 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isInList(Coordinate cc, ArrayList<Coordinate> clist)
/*      */   {
/* 1282 */     for (Coordinate pp : clist) {
/* 1283 */       if (isSamePoint(cc, pp, 0.0001D)) {
/* 1284 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1288 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean atLeft(Coordinate c1, Coordinate c2, Coordinate c3)
/*      */   {
/* 1312 */     Coordinate[] points = { c1, c2, c3 };
/* 1313 */     Coordinate[] gridPts = PgenUtil.latlonToGrid(points);
/*      */ 
/* 1315 */     Coordinate p1 = gridPts[0];
/* 1316 */     Coordinate p2 = gridPts[1];
/* 1317 */     Coordinate p3 = gridPts[2];
/*      */ 
/* 1319 */     double value = p1.x * p2.y + p3.x * p1.y + p2.x * p3.y - 
/* 1320 */       p3.x * p2.y - p2.x * p1.y - p1.x * p3.y;
/*      */ 
/* 1322 */     return value > 0.0D;
/*      */   }
/*      */ 
/*      */   public boolean atLeft(Coordinate pp, Coordinate[] line, boolean closed, double tolerance)
/*      */   {
/* 1344 */     boolean clsd = closed;
/*      */     boolean duplicate;
/*      */     boolean duplicate;
/* 1346 */     if (isSamePoint(line[0], line[(line.length - 1)], 0.0001D)) {
/* 1347 */       duplicate = true;
/*      */     }
/*      */     else {
/* 1350 */       duplicate = false;
/*      */     }
/*      */ 
/* 1353 */     if ((duplicate) && (!clsd)) clsd = true;
/*      */ 
/* 1357 */     Coordinate[] points = { pp };
/* 1358 */     Coordinate[] gridPts = PgenUtil.latlonToGrid(points);
/*      */ 
/* 1360 */     Coordinate cp = gridPts[0];
/*      */ 
/* 1362 */     Coordinate[] linep = PgenUtil.latlonToGrid(line);
/*      */ 
/* 1365 */     int flag = queryPointPosition(cp, linep, clsd, duplicate, tolerance);
/*      */ 
/* 1367 */     return flag <= 0;
/*      */   }
/*      */ 
/*      */   private int queryPointPosition(Coordinate cp, Coordinate[] linep, boolean closed, boolean duplicate, double tolerance)
/*      */   {
/* 1395 */     int flag = 0;
/*      */ 
/* 1398 */     boolean clsd = closed;
/* 1399 */     if ((duplicate) && (!clsd)) clsd = true;
/*      */ 
/* 1402 */     int[] indx = new int[2];
/* 1403 */     double[] dinfo = segmentDist(linep, cp, indx);
/*      */ 
/* 1405 */     int nearest_vtx = indx[0];
/* 1406 */     int next_vtx = indx[1];
/* 1407 */     double xner = dinfo[0];
/* 1408 */     double yner = dinfo[1];
/*      */ 
/* 1410 */     int np = linep.length;
/*      */ 
/* 1413 */     double[] va = new double[3]; double[] vb = new double[3];
/*      */ 
/* 1419 */     if (((gdiff(xner, linep[nearest_vtx].x, 1.0E-06D)) && (gdiff(yner, linep[nearest_vtx].y, 1.0E-06D))) || (
/* 1420 */       (gdiff(xner, linep[next_vtx].x, 1.0E-06D)) && (gdiff(yner, linep[next_vtx].y, 1.0E-06D))))
/*      */     {
/*      */       boolean linend;
/*      */       boolean linend;
/* 1423 */       if (((gdiff(xner, linep[0].x, 1.0E-06D)) && (gdiff(yner, linep[0].y, 1.0E-06D))) || (
/* 1424 */         (gdiff(xner, linep[(np - 1)].x, 1.0E-06D)) && (gdiff(yner, linep[(np - 1)].y, 1.0E-06D))))
/* 1425 */         linend = true;
/*      */       else {
/* 1427 */         linend = false;
/*      */       }
/*      */ 
/* 1430 */       if ((clsd) || ((!clsd) && (!linend))) {
/* 1431 */         int l1 = nearest_vtx;
/* 1432 */         int l0 = l1 - 1;
/* 1433 */         int l2 = l1 + 1;
/* 1434 */         if (l0 < 0) {
/* 1435 */           if (duplicate)
/* 1436 */             l0 = np - 2;
/*      */           else {
/* 1438 */             l0 = np - 1;
/*      */           }
/*      */         }
/* 1441 */         if (l2 == np) {
/* 1442 */           if (duplicate)
/* 1443 */             l2 = 1;
/*      */           else {
/* 1445 */             l2 = 0;
/*      */           }
/*      */         }
/*      */ 
/* 1449 */         va[0] = (cp.x - linep[l0].x);
/* 1450 */         va[1] = (cp.y - linep[l0].y);
/* 1451 */         vb[0] = (linep[l1].x - linep[l0].x);
/* 1452 */         vb[1] = (linep[l1].y - linep[l0].y);
/* 1453 */         double[] vc = crossProduct(va, vb);
/* 1454 */         double z1 = vc[2];
/*      */ 
/* 1456 */         va[0] = (cp.x - linep[l1].x);
/* 1457 */         va[1] = (cp.y - linep[l1].y);
/* 1458 */         vb[0] = (linep[l2].x - linep[l1].x);
/* 1459 */         vb[1] = (linep[l2].y - linep[l1].y);
/* 1460 */         vc = crossProduct(va, vb);
/* 1461 */         double z2 = vc[2];
/*      */ 
/* 1463 */         va[0] = (linep[l1].x - linep[l0].x);
/* 1464 */         va[1] = (linep[l1].y - linep[l0].y);
/* 1465 */         vb[0] = (linep[l2].x - linep[l1].x);
/* 1466 */         vb[1] = (linep[l2].y - linep[l1].y);
/* 1467 */         vc = crossProduct(va, vb);
/* 1468 */         double z3 = vc[2];
/*      */ 
/* 1470 */         if (gdiff(z1 * z2, 0.0D, tolerance)) {
/* 1471 */           if (z3 > 0.0D)
/* 1472 */             flag = (z1 > 0.0D) || (z2 > 0.0D) ? 1 : 0;
/* 1473 */           else if (z3 < 0.0D)
/* 1474 */             flag = (z1 < 0.0D) || (z2 < 0.0D) ? -1 : 0;
/*      */           else
/* 1476 */             flag = 0;
/*      */         }
/* 1478 */         else if (z1 * z2 > 0.0D)
/* 1479 */           flag = z1 > 0.0D ? 1 : -1;
/*      */         else
/* 1481 */           flag = z3 > 0.0D ? 1 : -1;
/*      */       }
/*      */       else {
/* 1484 */         int l1 = Math.min(nearest_vtx, next_vtx);
/* 1485 */         int l2 = l1 + 1;
/* 1486 */         va[0] = (cp.x - linep[l1].x);
/* 1487 */         va[1] = (cp.y - linep[l1].y);
/* 1488 */         vb[0] = (linep[l2].x - linep[l1].x);
/* 1489 */         vb[1] = (linep[l2].y - linep[l1].y);
/* 1490 */         double[] vc = crossProduct(va, vb);
/* 1491 */         flag = vc[2] > 0.0D ? 1 : gdiff(vc[2], 0.0D, tolerance) ? 0 : -1;
/*      */       }
/*      */     } else {
/* 1494 */       int l1 = Math.min(nearest_vtx, next_vtx);
/* 1495 */       int l2 = l1 + 1;
/* 1496 */       va[0] = (cp.x - linep[l1].x);
/* 1497 */       va[1] = (cp.y - linep[l1].y);
/* 1498 */       vb[0] = (linep[l2].x - linep[l1].x);
/* 1499 */       vb[1] = (linep[l2].y - linep[l1].y);
/* 1500 */       double[] vc = crossProduct(va, vb);
/* 1501 */       flag = vc[2] > 0.0D ? 1 : gdiff(vc[2], 0.0D, tolerance) ? 0 : -1;
/*      */     }
/*      */ 
/* 1504 */     return flag;
/*      */   }
/*      */ 
/*      */   private double[] crossProduct(double[] va, double[] vb)
/*      */   {
/* 1520 */     double[] vc = new double[3];
/*      */ 
/* 1522 */     vc[0] = (va[1] * vb[2] - va[2] * vb[1]);
/* 1523 */     vc[1] = (va[2] * vb[0] - va[0] * vb[2]);
/* 1524 */     vc[2] = (va[0] * vb[1] - va[1] * vb[0]);
/*      */ 
/* 1526 */     return vc;
/*      */   }
/*      */ 
/*      */   private double[] segmentDist(Coordinate[] line, Coordinate fixPt, int[] index)
/*      */   {
/* 1550 */     double[] dist = new double[3];
/*      */ 
/* 1552 */     int np = line.length;
/*      */ 
/* 1554 */     if (np == 1)
/*      */     {
/* 1556 */       index[0] = 0;
/* 1557 */       index[1] = 0;
/*      */ 
/* 1559 */       dist[0] = line[0].x;
/* 1560 */       dist[1] = line[0].y;
/* 1561 */       dist[2] = gdist(line[0].x, line[0].y, fixPt.x, fixPt.y);
/*      */ 
/* 1563 */       return dist;
/*      */     }
/*      */ 
/* 1567 */     double dt = 1.0E+38D;
/*      */ 
/* 1572 */     for (int ii = 0; ii < np - 1; ii++)
/*      */     {
/* 1574 */       double xmin = Math.min(line[ii].x, line[(ii + 1)].x);
/* 1575 */       double xmax = Math.max(line[ii].x, line[(ii + 1)].x);
/* 1576 */       double ymin = Math.min(line[ii].y, line[(ii + 1)].y);
/* 1577 */       double ymax = Math.max(line[ii].y, line[(ii + 1)].y);
/*      */       double qy;
/*      */       double qx;
/*      */       double qy;
/* 1586 */       if (gdiff(xmin, xmax, 1.0E-06D)) {
/* 1587 */         double qx = xmin;
/*      */         double qy;
/* 1588 */         if (fixPt.y < ymin) {
/* 1589 */           qy = ymin;
/*      */         }
/*      */         else
/*      */         {
/*      */           double qy;
/* 1590 */           if (fixPt.y > ymax)
/* 1591 */             qy = ymax;
/*      */           else
/* 1593 */             qy = fixPt.y;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         double qx;
/* 1597 */         if (gdiff(ymin, ymax, 1.0E-06D)) {
/* 1598 */           double qy = ymin;
/*      */           double qx;
/* 1599 */           if (fixPt.x < xmin) {
/* 1600 */             qx = xmin;
/*      */           }
/*      */           else
/*      */           {
/*      */             double qx;
/* 1601 */             if (fixPt.x > xmax)
/* 1602 */               qx = xmax;
/*      */             else {
/* 1604 */               qx = fixPt.x;
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1611 */           double m1 = (line[(ii + 1)].y - line[ii].y) / (line[(ii + 1)].x - line[ii].x);
/* 1612 */           double b1 = line[ii].y - m1 * line[ii].x;
/*      */ 
/* 1615 */           double m2 = -1.0D / m1;
/* 1616 */           double b2 = fixPt.y - m2 * fixPt.x;
/*      */ 
/* 1629 */           qx = (b2 - b1) / (m1 - m2);
/* 1630 */           qy = m2 * qx + b2;
/*      */         }
/*      */       }
/*      */       double curDist;
/*      */       double curDist;
/* 1634 */       if ((xmin <= qx) && (qx <= xmax)) {
/* 1635 */         curDist = gdist(fixPt.x, fixPt.y, qx, qy);
/*      */       }
/*      */       else {
/* 1638 */         double d0 = gdist(fixPt.x, fixPt.y, line[ii].x, line[ii].y);
/* 1639 */         double d1 = gdist(fixPt.x, fixPt.y, line[(ii + 1)].x, line[(ii + 1)].y);
/* 1640 */         curDist = d0 <= d1 ? d0 : d1;
/*      */       }
/*      */ 
/* 1644 */       if (curDist < dt)
/*      */       {
/* 1646 */         dt = curDist;
/*      */ 
/* 1648 */         dist[0] = qx;
/* 1649 */         dist[1] = qy;
/*      */ 
/* 1652 */         double d0 = gdist(fixPt.x, fixPt.y, line[ii].x, line[ii].y);
/* 1653 */         double d1 = gdist(fixPt.x, fixPt.y, line[(ii + 1)].x, line[(ii + 1)].y);
/*      */ 
/* 1655 */         if (d0 < d1) {
/* 1656 */           index[0] = ii;
/* 1657 */           index[1] = (ii + 1);
/*      */         }
/*      */         else {
/* 1660 */           index[0] = (ii + 1);
/* 1661 */           index[1] = ii;
/*      */         }
/*      */ 
/* 1664 */         if ((dist[0] < xmin) || (xmax < dist[0])) {
/* 1665 */           dist[0] = line[index[0]].x;
/* 1666 */           dist[1] = line[index[0]].y;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1672 */     return dist;
/*      */   }
/*      */ 
/*      */   private boolean gdiff(double v1, double v2, double pres)
/*      */   {
/* 1679 */     return Math.abs(v1 - v2) < pres;
/*      */   }
/*      */ 
/*      */   private double gdist(double x1, double y1, double x2, double y2)
/*      */   {
/* 1687 */     return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaSnap
 * JD-Core Version:    0.6.2
 */