/*      */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*      */ 
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.TreeSet;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class GfaReducePoint
/*      */ {
/*      */   private static final int SMEAR_INCR_PCT = 3;
/*      */   private static final int SMEAR_INCR_DST = 100;
/*      */   private static final int REDUCEPTS_INCR_PCT_ORIG = 3;
/*      */   private static final int GFA_AF_BISECT_PCT = 60;
/*      */   private static final String BISECT_MIDDLE_POINT = "BISECT_MIDDLE_POINT";
/*   82 */   private static GfaReducePoint instance = new GfaReducePoint();
/*      */ 
/*      */   public static GfaReducePoint getInstance()
/*      */   {
/*   96 */     return instance;
/*      */   }
/*      */ 
/*      */   public void reducePoints(ArrayList<Gfa> glist, ArrayList<Gfa> snapshots)
/*      */   {
/*  150 */     ArrayList smearList = new ArrayList();
/*      */ 
/*  153 */     for (Gfa gg : glist) {
/*  154 */       smearList.addAll(reducePoints(gg, snapshots));
/*      */     }
/*      */ 
/*  158 */     glist.clear();
/*  159 */     glist.addAll(smearList);
/*      */   }
/*      */ 
/*      */   public ArrayList<Gfa> reducePoints(Gfa smear, ArrayList<Gfa> snapshots)
/*      */   {
/*  171 */     ArrayList glist = new ArrayList();
/*      */ 
/*  177 */     Gfa tSmear = regularPointReduction(smear);
/*      */ 
/*  183 */     if (canBeFormatted(tSmear)) {
/*  184 */       smear.setPointsOnly(tSmear.getPoints());
/*  185 */       smear.setReduceFlags(tSmear.getReduceFlags());
/*  186 */       glist.add(smear);
/*  187 */       return glist;
/*      */     }
/*      */ 
/*  194 */     ArrayList listOfAreaClip = new ArrayList();
/*  195 */     listOfAreaClip.addAll(faAreaClip(smear, snapshots));
/*      */ 
/*  198 */     ArrayList listOfFirstBisect = new ArrayList();
/*      */     Gfa tmpSmear;
/*  199 */     for (Gfa gg : listOfAreaClip)
/*      */     {
/*  202 */       tmpSmear = regularPointReduction(gg);
/*  203 */       gg.setPointsOnly(tmpSmear.getPoints());
/*  204 */       gg.setReduceFlags(tmpSmear.getReduceFlags());
/*      */ 
/*  206 */       if (canBeFormatted(tmpSmear)) {
/*  207 */         glist.add(gg);
/*      */       }
/*      */       else {
/*  210 */         listOfFirstBisect.addAll(bisect(gg, true));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  215 */     Coordinate midPt = null;
/*  216 */     if (listOfFirstBisect.size() > 0) {
/*  217 */       midPt = (Coordinate)((Gfa)listOfFirstBisect.get(0)).getAttribute("BISECT_MIDDLE_POINT");
/*      */     }
/*      */ 
/*  221 */     for (Gfa gg : listOfFirstBisect)
/*      */     {
/*  224 */       Gfa tmpSmear = regularPointReduction(gg);
/*  225 */       gg.setPointsOnly(tmpSmear.getPoints());
/*  226 */       gg.setReduceFlags(tmpSmear.getReduceFlags());
/*      */ 
/*  228 */       if (canBeFormatted(tmpSmear)) {
/*  229 */         glist.add(gg);
/*      */       }
/*      */       else
/*      */       {
/*  233 */         glist.addAll(bisect(gg, false));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  243 */     boolean removeMidPt = false;
/*      */     Gfa gg;
/*  244 */     if (midPt != null) {
/*  245 */       int ii = 0;
/*  246 */       for (Iterator localIterator2 = glist.iterator(); localIterator2.hasNext(); ) { gg = (Gfa)localIterator2.next();
/*  247 */         for (Coordinate cc : gg.getPoints()) {
/*  248 */           if (GfaSnap.getInstance().isSamePoint(cc, midPt, 0.0001D)) {
/*  249 */             ii++;
/*  250 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  255 */       if (ii < 3) {
/*  256 */         removeMidPt = true;
/*      */       }
/*      */     }
/*      */ 
/*  260 */     if (removeMidPt)
/*      */     {
/*  262 */       for (Gfa gg : glist) {
/*  263 */         Coordinate removePt = null;
/*  264 */         for (Coordinate cc : gg.getPoints()) {
/*  265 */           if (GfaSnap.getInstance().isSamePoint(cc, midPt, 0.0001D)) {
/*  266 */             removePt = cc;
/*  267 */             break;
/*      */           }
/*      */         }
/*      */ 
/*  271 */         if (removePt != null) gg.getPoints().remove(removePt);
/*      */       }
/*      */     }
/*      */ 
/*  275 */     return glist;
/*      */   }
/*      */ 
/*      */   public static boolean canBeFormatted(Gfa smear)
/*      */   {
/*  285 */     boolean formattable = false;
/*  286 */     if (!smear.isSnapshot()) {
/*  287 */       String prefix = getPrefixString(smear);
/*  288 */       formattable = canBeFormatted(smear.getPoints(), prefix);
/*      */     }
/*      */ 
/*  291 */     return formattable;
/*      */   }
/*      */ 
/*      */   private static String getPrefixString(Gfa smear)
/*      */   {
/*  304 */     String prefix = "";
/*  305 */     if (!smear.isSnapshot()) {
/*  306 */       if (smear.isOutlook()) {
/*  307 */         prefix = "BOUNDED BY";
/*      */       }
/*      */       else {
/*  310 */         prefix = "FROM";
/*      */       }
/*      */     }
/*      */ 
/*  314 */     return prefix;
/*      */   }
/*      */ 
/*      */   private static boolean canBeFormatted(ArrayList<Coordinate> pts, String prefix)
/*      */   {
/*  327 */     return ReduceGfaPointsUtil.canFormatted(pts, prefix);
/*      */   }
/*      */ 
/*      */   private ArrayList<Gfa> faAreaClip(Gfa smear, ArrayList<Gfa> snapshots)
/*      */   {
/*  352 */     ArrayList newSmears = new ArrayList();
/*      */ 
/*  355 */     if (canBeFormatted(smear)) {
/*  356 */       newSmears.add(smear);
/*  357 */       return newSmears;
/*      */     }
/*      */ 
/*  364 */     String faAreas = smear.getGfaArea();
/*  365 */     if ((!faAreas.contains("-")) || 
/*  366 */       (!isSameFaRegion(faAreas))) {
/*  367 */       newSmears.add(smear);
/*  368 */       return newSmears;
/*      */     }
/*      */ 
/*  374 */     HashMap xareaBnds = GfaClip.getInstance().getFaAreaXBoundsInGrid();
/*      */ 
/*  380 */     Polygon smearPoly = GfaClip.getInstance().gfaToPolygon(smear);
/*  381 */     Polygon smearPolyInGrid = GfaClip.getInstance().gfaToPolygonInGrid(smear);
/*  382 */     HashMap areaInterPts = getAreaIntersectionPt(smearPoly);
/*      */ 
/*  391 */     HashMap clipWithAreas = new HashMap();
/*      */ 
/*  393 */     ArrayList smallPoly = new ArrayList();
/*      */ 
/*  395 */     for (String areaName : xareaBnds.keySet())
/*      */     {
/*  397 */       if (faAreas.contains(areaName))
/*      */       {
/*  399 */         clipWithAreas.put(areaName, new ArrayList());
/*      */ 
/*  402 */         Geometry areaBnd = (Geometry)xareaBnds.get(areaName);
/*  403 */         if (areaBnd.intersects(smearPolyInGrid))
/*      */         {
/*  405 */           Geometry areaPoly = areaBnd.intersection(smearPolyInGrid);
/*      */ 
/*  407 */           if (areaPoly != null)
/*  408 */             for (int kk = 0; kk < areaPoly.getNumGeometries(); kk++) {
/*  409 */               Geometry bigPoly = areaPoly.getGeometryN(kk);
/*      */ 
/*  411 */               if (GfaClip.getInstance().isBiggerInGrid(bigPoly)) {
/*  412 */                 ((ArrayList)clipWithAreas.get(areaName)).add(bigPoly);
/*      */               }
/*      */               else
/*  415 */                 smallPoly.add(bigPoly);
/*      */             }
/*      */         }
/*      */       }
/*      */     }
/*      */     ArrayList commPts;
/*  437 */     for (String areaName : clipWithAreas.keySet())
/*      */     {
/*  439 */       ArrayList bigs = (ArrayList)clipWithAreas.get(areaName);
/*      */ 
/*  441 */       ArrayList toBeUnioned = new ArrayList();
/*  442 */       toBeUnioned.addAll(bigs);
/*  443 */       toBeUnioned.addAll(smallPoly);
/*  444 */       Geometry result = GfaClip.getInstance().quickUnion(toBeUnioned);
/*      */ 
/*  446 */       commPts = GfaClip.getInstance().getCommonPoints(bigs, smallPoly);
/*      */ 
/*  448 */       if (result != null)
/*      */       {
/*  450 */         for (int kk = 0; kk < result.getNumGeometries(); kk++)
/*      */         {
/*  452 */           Geometry one = result.getGeometryN(kk);
/*      */ 
/*  455 */           Geometry onePart = GfaClip.getInstance().removeCommonPoints(one, commPts);
/*      */ 
/*  458 */           Coordinate[] gPts = PgenUtil.gridToLatlon(onePart.getCoordinates());
/*  459 */           ArrayList points = new ArrayList();
/*  460 */           for (Coordinate c : gPts) {
/*  461 */             points.add(c);
/*      */           }
/*  463 */           points.remove(points.size() - 1);
/*      */ 
/*  467 */           Geometry cleanPts = GfaClip.getInstance().cleanupPoints(
/*  468 */             GfaClip.getInstance().pointsToGeometry(points));
/*      */ 
/*  470 */           if (GfaClip.getInstance().isAddableAsSmear(cleanPts, snapshots, smear))
/*      */           {
/*  473 */             Geometry rplPts = GfaClip.getInstance().replacePts(cleanPts, areaInterPts);
/*      */ 
/*  476 */             cleanPts = GfaClip.getInstance().cleanupPoints(rplPts);
/*      */ 
/*  478 */             Gfa newElm = GfaClip.getInstance().geometryToGfa(smear, cleanPts);
/*      */ 
/*  480 */             newElm.setGfaArea(areaName);
/*      */ 
/*  482 */             newSmears.add(newElm);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  490 */     for (Gfa gg : newSmears) {
/*  491 */       boolean[] reduFlg = new boolean[gg.getPoints().size()];
/*  492 */       int ii = 0;
/*  493 */       for (Coordinate newPt : gg.getPoints()) {
/*  494 */         reduFlg[ii] = false;
/*      */ 
/*  496 */         for (int jj = 0; jj < smear.getPoints().size(); jj++)
/*      */         {
/*  498 */           if (GfaSnap.getInstance().isSamePoint(newPt, 
/*  498 */             (Coordinate)smear.getPoints().get(jj), 0.0001D)) {
/*  499 */             reduFlg[ii] = smear.getReduceFlags()[jj];
/*  500 */             break;
/*      */           }
/*      */         }
/*  503 */         ii++;
/*      */       }
/*      */ 
/*  506 */       gg.setReduceFlags(reduFlg);
/*      */     }
/*      */ 
/*  509 */     return newSmears;
/*      */   }
/*      */ 
/*      */   private HashMap<Coordinate, Coordinate> getAreaIntersectionPt(Geometry gfaPoly)
/*      */   {
/*  525 */     HashMap interPtsPair = new HashMap();
/*      */ 
/*  527 */     ArrayList interPts = new ArrayList();
/*      */ 
/*  529 */     ArrayList interIndex = new ArrayList();
/*  530 */     ArrayList indx = new ArrayList();
/*      */ 
/*  533 */     ArrayList gfaPoints = new ArrayList();
/*  534 */     for (Coordinate c : gfaPoly.getCoordinates()) {
/*  535 */       gfaPoints.add(c);
/*      */     }
/*      */ 
/*  538 */     ArrayList cwGfaPts = GfaSnap.getInstance().reorderInClockwise(gfaPoints, null);
/*      */ 
/*  541 */     HashMap areaCommBnds = GfaClip.getInstance().getFaAreaXCommBounds();
/*  542 */     for (??? = areaCommBnds.values().iterator(); ((Iterator)???).hasNext(); ) { Geometry bnd = (Geometry)((Iterator)???).next();
/*  543 */       ArrayList pts = GfaClip.getInstance().lineIntersect((Coordinate[])cwGfaPts.toArray(new Coordinate[cwGfaPts.size()]), 
/*  544 */         bnd.getCoordinates(), indx);
/*      */ 
/*  546 */       if (pts.size() > 0) {
/*  547 */         interPts.addAll(pts);
/*  548 */         interIndex.addAll(indx);
/*      */       }
/*      */     }
/*      */ 
/*  552 */     if (interPts.size() <= 0) {
/*  553 */       return interPtsPair;
/*      */     }
/*      */ 
/*  560 */     ArrayList checkPoints = new ArrayList();
/*      */     int m;
/*      */     int k;
/*  561 */     for (Iterator localIterator1 = areaCommBnds.values().iterator(); localIterator1.hasNext(); 
/*  562 */       k < m)
/*      */     {
/*  561 */       Geometry g = (Geometry)localIterator1.next();
/*      */       Coordinate[] arrayOfCoordinate1;
/*  562 */       m = (arrayOfCoordinate1 = g.getCoordinates()).length; k = 0; continue; Coordinate c = arrayOfCoordinate1[k];
/*  563 */       Geometry pp = GfaClip.getInstance().pointsToGeometry(new Coordinate[] { c });
/*  564 */       if ((!checkPoints.contains(c)) && (pp.within(gfaPoly)))
/*  565 */         checkPoints.add(c);
/*  562 */       k++;
/*      */     }
/*      */ 
/*  588 */     ArrayList usedPoints = new ArrayList();
/*  589 */     usedPoints.addAll(cwGfaPts);
/*      */ 
/*  591 */     ArrayList ePts = new ArrayList();
/*  592 */     for (int ii = 0; ii < interPts.size(); ii++)
/*      */     {
/*  594 */       Coordinate interP = (Coordinate)interPts.get(ii);
/*  595 */       Coordinate ptBefore = (Coordinate)cwGfaPts.get(((Integer)interIndex.get(ii)).intValue());
/*  596 */       Coordinate ptAfter = (Coordinate)cwGfaPts.get(((Integer)interIndex.get(ii)).intValue() + 1);
/*      */ 
/*  598 */       double qdist1 = GfaSnap.getInstance().distance(interP, ptBefore);
/*  599 */       double qdist2 = GfaSnap.getInstance().distance(interP, ptAfter);
/*      */ 
/*  601 */       int addOne = -1;
/*  602 */       int qmatch = -1;
/*      */       double qdist;
/*      */       double qdist;
/*  605 */       if (qdist1 < qdist2) {
/*  606 */         qmatch = ((Integer)interIndex.get(ii)).intValue();
/*  607 */         qdist = qdist1;
/*      */       }
/*      */       else {
/*  610 */         qmatch = ((Integer)interIndex.get(ii)).intValue() + 1;
/*  611 */         qdist = qdist2;
/*      */       }
/*      */ 
/*  614 */       if (qdist / 1852.0D < GfaSnap.CLUSTER_DIST)
/*      */       {
/*  616 */         Coordinate ptMatch = (Coordinate)cwGfaPts.get(qmatch);
/*  617 */         for (Coordinate c : checkPoints) {
/*  618 */           if (GfaSnap.getInstance().isCluster(c, ptMatch)) {
/*  619 */             addOne = qmatch;
/*  620 */             break;
/*      */           }
/*      */         }
/*      */ 
/*  624 */         if (addOne < 0) {
/*  625 */           interPtsPair.put(new Coordinate(interP), 
/*  626 */             new Coordinate(ptMatch));
/*  627 */           continue;
/*      */         }
/*      */       }
/*      */       int kk;
/*      */       int kk2;
/*  633 */       if (addOne >= 0) {
/*  634 */         int kk = addOne;
/*  635 */         int kk2 = kk;
/*      */ 
/*  637 */         ePts.clear();
/*  638 */         ePts.addAll(cwGfaPts);
/*      */       }
/*      */       else {
/*  641 */         ePts.clear();
/*  642 */         ePts.addAll(GfaSnap.getInstance().insertArray(cwGfaPts, ((Integer)interIndex.get(ii)).intValue() + 1, interP));
/*  643 */         kk = ((Integer)interIndex.get(ii)).intValue() + 1;
/*  644 */         kk2 = kk;
/*      */       }
/*      */ 
/*  648 */       ePts.remove(ePts.size() - 1);
/*      */ 
/*  650 */       Coordinate[] snapped = new Coordinate[1];
/*      */ 
/*  653 */       int status = GfaSnap.getInstance().snapPtGFA(kk, kk2, usedPoints, checkPoints, 
/*  654 */         ePts, true, true, 3.0D, snapped);
/*      */ 
/*  656 */       if (status != 0) {
/*  657 */         if (addOne >= 0) {
/*  658 */           snapped[0] = new Coordinate((Coordinate)ePts.get(kk));
/*      */         }
/*      */         else {
/*  661 */           snapped[0] = new Coordinate(interP);
/*      */         }
/*      */       }
/*      */ 
/*  665 */       interPtsPair.put(new Coordinate(interP), 
/*  666 */         new Coordinate(snapped[0]));
/*      */ 
/*  668 */       if (addOne >= 0) {
/*  669 */         interPtsPair.put(new Coordinate((Coordinate)cwGfaPts.get(addOne)), 
/*  670 */           new Coordinate(snapped[0]));
/*      */       }
/*      */ 
/*  673 */       usedPoints.add(snapped[0]);
/*      */     }
/*      */ 
/*  677 */     return interPtsPair;
/*      */   }
/*      */ 
/*      */   private ArrayList<Gfa> bisect(Gfa smear, boolean addMidPt)
/*      */   {
/*  701 */     ArrayList newSmears = new ArrayList();
/*      */ 
/*  704 */     boolean cbf = canBeFormatted(smear);
/*      */ 
/*  707 */     if (cbf) {
/*  708 */       newSmears.add(smear);
/*  709 */       return newSmears;
/*      */     }
/*      */ 
/*  716 */     Coordinate[] smearPts = smear.getLinePoints();
/*  717 */     TreeSet segments = findAllSegments(smearPts);
/*      */ 
/*  722 */     double polyArea = PgenUtil.getSphPolyArea(smearPts);
/*      */ 
/*  729 */     double prefPct = 40.0D;
/*  730 */     double prefPctMax = Math.max(prefPct, 60.0D);
/*  731 */     double prefPctMin = Math.min(prefPct, 60.0D);
/*      */ 
/*  733 */     ArrayList tmpPoly = new ArrayList();
/*      */ 
/*  736 */     BisectDistInfo bisectSeg = null;
/*      */ 
/*  738 */     for (BisectDistInfo bd : segments)
/*      */     {
/*  741 */       tmpPoly.clear();
/*  742 */       for (int ii = bd.start; ii <= bd.end; ii++) {
/*  743 */         tmpPoly.add(new Coordinate(smearPts[ii]));
/*      */       }
/*      */ 
/*  747 */       double tarea = PgenUtil.getSphPolyArea((Coordinate[])tmpPoly.toArray(new Coordinate[tmpPoly.size()]));
/*  748 */       double pct = 100.0D * (tarea / polyArea);
/*      */ 
/*  752 */       if ((pct >= prefPctMin) && (pct <= prefPctMax)) {
/*  753 */         bisectSeg = new BisectDistInfo(bd.distance, bd.start, bd.end);
/*  754 */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  766 */     if (bisectSeg == null) {
/*  767 */       newSmears.add(smear);
/*      */     }
/*      */     else
/*      */     {
/*  772 */       Coordinate midPt = null;
/*  773 */       if (addMidPt) {
/*  774 */         midPt = new Coordinate();
/*  775 */         midPt.x = ((smearPts[bisectSeg.start].x + smearPts[bisectSeg.end].x) / 2.0D);
/*  776 */         midPt.y = ((smearPts[bisectSeg.start].y + smearPts[bisectSeg.end].y) / 2.0D);
/*      */ 
/*  778 */         Coordinate snappedMidPt = GfaSnap.getInstance().snapOnePt(midPt);
/*      */ 
/*  780 */         if ((!GfaSnap.getInstance().isCluster(snappedMidPt, smearPts[bisectSeg.start])) && 
/*  781 */           (!GfaSnap.getInstance().isCluster(snappedMidPt, smearPts[bisectSeg.end])))
/*      */         {
/*  783 */           Coordinate[] seg1 = { smearPts[bisectSeg.start], snappedMidPt };
/*  784 */           Coordinate[] seg2 = { smearPts[bisectSeg.end], snappedMidPt };
/*      */ 
/*  786 */           if ((segIntPoly(seg1, smearPts)) && (segIntPoly(seg2, smearPts))) {
/*  787 */             midPt.x = snappedMidPt.x;
/*  788 */             midPt.y = snappedMidPt.y;
/*      */           }
/*      */           else {
/*  791 */             addMidPt = false;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  800 */       Gfa first = smear.copy();
/*      */ 
/*  802 */       ArrayList coor1 = new ArrayList();
/*  803 */       ArrayList reduFlg1 = new ArrayList();
/*  804 */       for (int ii = bisectSeg.start; ii <= bisectSeg.end; ii++) {
/*  805 */         coor1.add(new Coordinate(smearPts[ii]));
/*  806 */         reduFlg1.add(new Boolean(smear.getReduceFlags()[ii]));
/*      */       }
/*      */ 
/*  809 */       if (addMidPt) {
/*  810 */         Coordinate mpt1 = new Coordinate(midPt);
/*  811 */         coor1.add(mpt1);
/*  812 */         first.addAttribute("BISECT_MIDDLE_POINT", mpt1);
/*  813 */         reduFlg1.add(new Boolean(false));
/*      */       }
/*      */ 
/*  816 */       first.setPointsOnly(coor1);
/*  817 */       first.setGfaTextCoordinate(first.getCentroid());
/*      */ 
/*  819 */       boolean[] redu1 = new boolean[reduFlg1.size()];
/*  820 */       int kk = 0;
/*  821 */       for (Boolean bb : reduFlg1) {
/*  822 */         if (bb.booleanValue()) {
/*  823 */           redu1[kk] = true;
/*      */         }
/*      */         else {
/*  826 */           redu1[kk] = false;
/*      */         }
/*      */ 
/*  829 */         kk++;
/*      */       }
/*  831 */       first.setReduceFlags(redu1);
/*      */ 
/*  837 */       Gfa second = smear.copy();
/*  838 */       ArrayList coor2 = new ArrayList();
/*  839 */       ArrayList reduFlg2 = new ArrayList();
/*      */ 
/*  841 */       for (int ii = 0; ii <= bisectSeg.start; ii++) {
/*  842 */         coor2.add(new Coordinate(smearPts[ii]));
/*  843 */         reduFlg2.add(new Boolean(smear.getReduceFlags()[ii]));
/*      */       }
/*      */ 
/*  846 */       if (addMidPt) {
/*  847 */         Coordinate mpt2 = new Coordinate(midPt);
/*  848 */         coor2.add(mpt2);
/*  849 */         second.addAttribute("BISECT_MIDDLE_POINT", mpt2);
/*  850 */         reduFlg2.add(new Boolean(false));
/*      */       }
/*      */ 
/*  853 */       for (int ii = bisectSeg.end; ii < smearPts.length; ii++) {
/*  854 */         coor2.add(new Coordinate(smearPts[ii]));
/*  855 */         reduFlg2.add(new Boolean(smear.getReduceFlags()[ii]));
/*      */       }
/*      */ 
/*  858 */       second.setPointsOnly(coor2);
/*  859 */       second.setGfaTextCoordinate(second.getCentroid());
/*      */ 
/*  861 */       boolean[] redu2 = new boolean[reduFlg2.size()];
/*  862 */       int kj = 0;
/*  863 */       for (Boolean bb : reduFlg2) {
/*  864 */         if (bb.booleanValue()) {
/*  865 */           redu2[kj] = true;
/*      */         }
/*      */         else {
/*  868 */           redu2[kj] = false;
/*      */         }
/*      */ 
/*  871 */         kj++;
/*      */       }
/*  873 */       second.setReduceFlags(redu2);
/*      */ 
/*  876 */       newSmears.add(first);
/*  877 */       newSmears.add(second);
/*      */     }
/*      */ 
/*  881 */     return newSmears;
/*      */   }
/*      */ 
/*      */   private TreeSet<BisectDistInfo> findAllSegments(Coordinate[] poly)
/*      */   {
/*  927 */     TreeSet distInfo = new TreeSet();
/*      */ 
/*  929 */     Coordinate[] seg = new Coordinate[2];
/*      */ 
/*  932 */     for (int ii = 0; ii < poly.length - 2; ii++) {
/*  933 */       for (int jj = ii + 2; jj < poly.length; jj++) {
/*  934 */         seg[0] = poly[ii];
/*  935 */         seg[1] = poly[jj];
/*      */ 
/*  937 */         boolean qualify = polysegIntPoly(seg, poly);
/*  938 */         if (qualify) {
/*  939 */           double dist = GfaSnap.getInstance().distance(seg[0], seg[1]) / 1852.0D;
/*  940 */           if (dist > GfaSnap.CLUSTER_DIST) {
/*  941 */             distInfo.add(new BisectDistInfo(dist, ii, jj));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  947 */     return distInfo;
/*      */   }
/*      */ 
/*      */   private boolean polysegIntPoly(Coordinate[] seg, Coordinate[] poly)
/*      */   {
/*  965 */     boolean qualify = true;
/*      */ 
/*  968 */     List polyList = Arrays.asList(poly);
/*  969 */     int index1 = polyList.indexOf(seg[0]);
/*  970 */     int index2 = polyList.indexOf(seg[1]);
/*  971 */     int igap = Math.abs(index2 - index1);
/*      */ 
/*  973 */     if ((igap == 1) || (igap == poly.length - 1)) {
/*  974 */       qualify = false;
/*      */     }
/*      */     else {
/*  977 */       qualify = segIntPoly(seg, poly);
/*      */     }
/*      */ 
/*  980 */     return qualify;
/*      */   }
/*      */ 
/*      */   private boolean segIntPoly(Coordinate[] segIn, Coordinate[] polyIn)
/*      */   {
/*  997 */     boolean qualify = true;
/*      */ 
/* 1000 */     Coordinate[] seg = PgenUtil.latlonToGrid(segIn);
/* 1001 */     Coordinate[] poly = PgenUtil.latlonToGrid(polyIn);
/*      */ 
/* 1004 */     Coordinate[] midp = new Coordinate[1];
/* 1005 */     midp[0] = new Coordinate((seg[0].x + seg[1].x) / 2.0D, (seg[0].y + seg[1].y) / 2.0D);
/* 1006 */     Geometry point = GfaClip.getInstance().pointsToGeometry(midp);
/* 1007 */     Geometry polygon = GfaClip.getInstance().pointsToGeometry(poly);
/*      */ 
/* 1010 */     if (!point.within(polygon)) {
/* 1011 */       qualify = false;
/*      */     }
/*      */     else {
/* 1014 */       Geometry segment = GfaClip.getInstance().pointsToGeometry(seg);
/*      */ 
/* 1016 */       if (segment.intersects(polygon)) {
/* 1017 */         Coordinate[] ipts = segment.intersection(polygon).getCoordinates();
/*      */ 
/* 1023 */         if (ipts.length != 2) {
/* 1024 */           qualify = false;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1030 */     return qualify;
/*      */   }
/*      */ 
/*      */   private TreeSet<BisectDistInfo> findSegmentsBySlack(int index, int slack, Coordinate[] polyIn)
/*      */   {
/* 1045 */     TreeSet distInfo = new TreeSet();
/*      */ 
/* 1048 */     Coordinate[] poly = PgenUtil.latlonToGrid(polyIn);
/*      */ 
/* 1054 */     int npts = poly.length;
/* 1055 */     int mid = npts / 2;
/* 1056 */     int nseg = 2 * slack + 1;
/*      */ 
/* 1058 */     ArrayList pointsToCheck = new ArrayList();
/* 1059 */     for (int ii = 0; ii < nseg; ii++) {
/* 1060 */       pointsToCheck.add(Integer.valueOf((ii - slack + index + mid + npts) % npts));
/*      */     }
/*      */ 
/* 1067 */     Coordinate[] seg = new Coordinate[2];
/* 1068 */     seg[0] = poly[index];
/*      */ 
/* 1071 */     for (Integer jj : pointsToCheck)
/*      */     {
/* 1074 */       if (jj.intValue() < poly.length)
/*      */       {
/* 1076 */         seg[1] = poly[jj.intValue()];
/*      */ 
/* 1078 */         boolean qualify = polysegIntPoly(seg, poly);
/* 1079 */         if (qualify) {
/* 1080 */           double dist = GfaSnap.getInstance().distance(seg[0], seg[1]) / 1852.0D;
/* 1081 */           if (dist > GfaSnap.CLUSTER_DIST) {
/* 1082 */             distInfo.add(new BisectDistInfo(dist, index, jj.intValue()));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1087 */     return distInfo;
/*      */   }
/*      */ 
/*      */   private boolean isSameFaRegion(String areas)
/*      */   {
/* 1096 */     return (areas.equalsIgnoreCase("BOS-MIA")) || 
/* 1097 */       (areas.equalsIgnoreCase("MIA-BOS")) || 
/* 1098 */       (areas.equalsIgnoreCase("SLC-SFO")) || 
/* 1099 */       (areas.equalsIgnoreCase("SFO-SLC")) || 
/* 1100 */       (areas.equalsIgnoreCase("CHI-DFW")) || 
/* 1101 */       (areas.equalsIgnoreCase("DFW-CHI"));
/*      */   }
/*      */ 
/*      */   private Gfa regularPointReduction(Gfa smearIn)
/*      */   {
/* 1112 */     ArrayList xyList = new ArrayList(smearIn.getPoints());
/* 1113 */     List reduceFlg = new ArrayList();
/* 1114 */     List orig = new ArrayList();
/*      */ 
/* 1116 */     boolean[] reduceF = smearIn.getReduceFlags();
/*      */ 
/* 1118 */     for (int ii = 0; ii < reduceF.length; ii++)
/*      */     {
/* 1120 */       if (reduceF[ii] != 0) {
/* 1121 */         reduceFlg.add(Integer.valueOf(1));
/*      */       }
/*      */       else {
/* 1124 */         reduceFlg.add(Integer.valueOf(0));
/*      */       }
/*      */ 
/* 1127 */       orig.add(Integer.valueOf(1));
/*      */     }
/*      */ 
/* 1136 */     double incrPct = 3.0D;
/* 1137 */     double incrPctOrig = 3.0D;
/* 1138 */     double incrDst = 100.0D;
/*      */ 
/* 1140 */     String prefix = getPrefixString(smearIn);
/*      */ 
/* 1142 */     int ier = ReduceGfaPoints.reduceByPctDist(xyList, reduceFlg, orig, 
/* 1143 */       incrPct, incrPctOrig, incrDst, prefix);
/*      */ 
/* 1145 */     boolean[] reduceFl = new boolean[xyList.size()];
/* 1146 */     for (int ii = 0; ii < xyList.size(); ii++) {
/* 1147 */       if (((Integer)reduceFlg.get(ii)).intValue() == 1)
/* 1148 */         reduceFl[ii] = true;
/*      */       else {
/* 1150 */         reduceFl[ii] = false;
/*      */       }
/*      */     }
/* 1153 */     Gfa newSmear = smearIn.copy();
/* 1154 */     newSmear.setPointsOnly(xyList);
/* 1155 */     newSmear.setReduceFlags(reduceFl);
/*      */ 
/* 1157 */     return newSmear;
/*      */   }
/*      */ 
/*      */   private void test()
/*      */   {
/* 1166 */     Coordinate[] polyPts = { 
/* 1167 */       new Coordinate(-98.501655242492447D, 45.216163960194365D), 
/* 1168 */       new Coordinate(-96.075628058267981D, 39.725652270504796D), 
/* 1169 */       new Coordinate(-88.951799715181551D, 39.080886895052743D), 
/* 1170 */       new Coordinate(-87.968935610661319D, 44.647653935023214D), 
/* 1171 */       new Coordinate(-89.728764499969145D, 41.544489734823657D) };
/*      */ 
/* 1173 */     Coordinate[] seg = new Coordinate[2];
/*      */ 
/* 1175 */     Coordinate midPt = new Coordinate();
/* 1176 */     midPt.x = ((polyPts[0].x + polyPts[2].x) / 2.0D - 1.0D);
/* 1177 */     midPt.y = ((polyPts[0].y + polyPts[2].y) / 2.0D - 1.0D);
/* 1178 */     seg[0] = polyPts[0];
/* 1179 */     seg[1] = midPt;
/*      */ 
/* 1182 */     for (int ii = 0; ii < polyPts.length - 2; ii++)
/* 1183 */       for (int jj = ii + 2; jj < polyPts.length; jj++) {
/* 1184 */         seg[0] = polyPts[ii];
/* 1185 */         seg[1] = polyPts[jj];
/*      */ 
/* 1187 */         boolean good = polysegIntPoly(seg, polyPts);
/*      */         String s;
/*      */         String s;
/* 1189 */         if (good) {
/* 1190 */           s = "Qualify!";
/*      */         }
/*      */         else {
/* 1193 */           s = "Not qualify";
/*      */         }
/*      */ 
/* 1196 */         System.out.println("\npoint " + ii + "\t<Point Lat=\"" + 
/* 1197 */           polyPts[ii].y + "\" Lon=\"" + polyPts[ii].x + "\"/>" + 
/* 1198 */           "\t=>\t" + "point " + jj + "\t<Point Lat=\"" + 
/* 1199 */           polyPts[jj].y + "\" Lon=\"" + polyPts[jj].x + "\"/>" + 
/* 1200 */           "\t" + s);
/*      */       }
/*      */   }
/*      */ 
/*      */   public static void WarningForOverThreeLines(Gfa smear)
/*      */   {
/* 1213 */     if (!smear.isSnapshot()) {
/* 1214 */       String prefix = getPrefixString(smear);
/*      */ 
/* 1216 */       boolean formattable = canBeFormatted(smear.getPoints(), prefix);
/*      */ 
/* 1218 */       if (!formattable) {
/* 1219 */         String message = "";
/* 1220 */         if (prefix.equals("FROM")) {
/* 1221 */           message = new String("This AIRMET will generate more than 3 FROM lines when formatted.");
/*      */         }
/*      */         else {
/* 1224 */           message = new String("This OUTLOOK will generate more than 3 FROM lines when formatted.");
/*      */         }
/*      */ 
/* 1227 */         MessageDialog confirmDlg = new MessageDialog(
/* 1228 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 1229 */           "Over 3 FROM Lines", null, message, 
/* 1230 */           4, new String[] { "OK" }, 0);
/*      */ 
/* 1232 */         confirmDlg.open();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class BisectDistInfo
/*      */     implements Comparable<BisectDistInfo>
/*      */   {
/*      */     double distance;
/*      */     int start;
/*      */     int end;
/*      */ 
/*      */     public BisectDistInfo(double dist, int index1, int index2)
/*      */     {
/*  895 */       this.distance = dist;
/*  896 */       this.start = index1;
/*  897 */       this.end = index2;
/*      */     }
/*      */ 
/*      */     public int compareTo(BisectDistInfo o)
/*      */     {
/*  903 */       if (this == o) {
/*  904 */         return 0;
/*      */       }
/*      */ 
/*  907 */       if (this.distance > o.distance) {
/*  908 */         return 1;
/*      */       }
/*  910 */       return -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaReducePoint
 * JD-Core Version:    0.6.2
 */