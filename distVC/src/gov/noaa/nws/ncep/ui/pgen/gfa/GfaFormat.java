/*      */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*      */ 
/*      */ import com.vividsolutions.jts.algorithm.CGAlgorithms;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.GeometryCollection;
/*      */ import com.vividsolutions.jts.geom.GeometryFactory;
/*      */ import com.vividsolutions.jts.geom.LineString;
/*      */ import com.vividsolutions.jts.geom.LinearRing;
/*      */ import com.vividsolutions.jts.geom.Point;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.TreeSet;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class GfaFormat
/*      */ {
/*      */   PgenResource drawingLayer;
/*      */   private static GeometryFactory geometryFactory;
/*      */ 
/*      */   public GfaFormat()
/*      */   {
/*      */   }
/*      */ 
/*      */   public GfaFormat(PgenResource drawingLayer)
/*      */   {
/*   89 */     this.drawingLayer = drawingLayer;
/*      */   }
/*      */ 
/*      */   public void formatAllPressed()
/*      */   {
/*   96 */     GfaClip.getInstance().updateGfaBoundsInGrid();
/*      */ 
/*   98 */     if (this.drawingLayer != null) {
/*   99 */       validateAllGfas();
/*      */       Iterator localIterator2;
/*  100 */       for (Iterator localIterator1 = this.drawingLayer.getProducts().iterator(); localIterator1.hasNext(); 
/*  101 */         localIterator2.hasNext())
/*      */       {
/*  100 */         Product p = (Product)localIterator1.next();
/*  101 */         localIterator2 = p.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/*      */ 
/*  103 */         formatLayer(layer, false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void formatLayerPressed()
/*      */   {
/*  114 */     GfaClip.getInstance().updateGfaBoundsInGrid();
/*  115 */     if (this.drawingLayer != null) {
/*  116 */       validateActiveGfas();
/*  117 */       Layer layer = this.drawingLayer.getActiveLayer();
/*  118 */       formatLayer(layer, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void formatTagPressed()
/*      */   {
/*  126 */     GfaClip.getInstance().updateGfaBoundsInGrid();
/*  127 */     if (this.drawingLayer != null) {
/*  128 */       validateActiveGfas();
/*  129 */       Layer layer = this.drawingLayer.getActiveLayer();
/*  130 */       formatLayer(layer, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void formatLayer(Layer layer, boolean checkTag)
/*      */   {
/*  143 */     ArrayList oldList = new ArrayList();
/*  144 */     ArrayList newList = new ArrayList();
/*      */ 
/*  147 */     DrawableElement de = this.drawingLayer.getSelectedDE();
/*      */ 
/*  149 */     for (AbstractDrawableComponent adc : layer.getDrawables())
/*      */     {
/*  151 */       if (((adc instanceof Gfa)) && (((Gfa)adc).isValid()))
/*      */       {
/*  153 */         if ((!checkTag) || 
/*  154 */           (de == null) || (isSameHazardAndTag((Gfa)adc, (Gfa)de)))
/*      */         {
/*  160 */           if (((!((Gfa)adc).getGfaHazard().equalsIgnoreCase("M_FZLVL")) && 
/*  161 */             (!((Gfa)adc).getGfaHazard().equalsIgnoreCase("LLWS"))) || 
/*  162 */             (!((Gfa)adc).isSnapshot()) || 
/*  164 */             (Gfa.getHourMinInt(((Gfa)adc).getGfaFcstHr())[0] + 
/*  164 */             Gfa.getHourMinInt(((Gfa)adc).getGfaFcstHr())[1] / 60.0D <= 6.0D)) {
/*  165 */             oldList.add(adc);
/*      */           }
/*      */ 
/*  168 */           if ((((Gfa)adc).isSnapshot()) && (
/*  169 */             ((!((Gfa)adc).getGfaHazard().equalsIgnoreCase("M_FZLVL")) && 
/*  170 */             (!((Gfa)adc).getGfaHazard().equalsIgnoreCase("LLWS"))) || 
/*  172 */             (Gfa.getHourMinInt(((Gfa)adc).getGfaFcstHr())[0] + 
/*  172 */             Gfa.getHourMinInt(((Gfa)adc).getGfaFcstHr())[1] / 60.0D <= 6.0D)))
/*      */           {
/*  174 */             newList.add(adc);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  182 */     createSmears(newList);
/*      */ 
/*  185 */     List frzl = formatFrzl(layer, checkTag);
/*  186 */     if ((frzl != null) && (!frzl.isEmpty())) {
/*  187 */       newList.addAll(frzl);
/*      */     }
/*      */ 
/*  194 */     if (!newList.isEmpty())
/*  195 */       this.drawingLayer.replaceElements(oldList, newList);
/*      */   }
/*      */ 
/*      */   protected void createSmears(List<AbstractDrawableComponent> list)
/*      */   {
/*  214 */     TreeSet tree = new TreeSet(new SnapshotComparator(null));
/*  215 */     for (AbstractDrawableComponent adc : list) {
/*  216 */       if ((!((Gfa)adc).getGfaHazard().equalsIgnoreCase("FZLVL")) && 
/*  217 */         (((Gfa)adc).isFormat())) tree.add((Gfa)adc);
/*      */     }
/*      */ 
/*  220 */     if (tree.isEmpty()) return;
/*      */ 
/*  230 */     TreeSet splittedSS = splitSnapshots(tree);
/*      */ 
/*  233 */     Coordinate[] intlBnds = GfaClip.getInstance().getFaInternationalBound().getGeometryN(0).getCoordinates();
/*  234 */     ArrayList intlBndsPts = new ArrayList();
/*  235 */     for (Coordinate cc : intlBnds) {
/*  236 */       intlBndsPts.add(cc);
/*      */     }
/*      */ 
/*  242 */     ArrayList listOfLists = new ArrayList();
/*  243 */     for (FcstHrListPair pair : splittedSS)
/*      */     {
/*  246 */       HashMap values = findGfaTopBots(pair.list);
/*  247 */       String type = findGfaSubTypes(pair.list);
/*      */ 
/*  250 */       ArrayList toRemove = new ArrayList();
/*      */       Gfa g;
/*  252 */       for (int ii = 0; ii < pair.list.size() - 1; ii++)
/*      */       {
/*  254 */         g = (Gfa)pair.list.get(ii);
/*  255 */         Gfa gNext = (Gfa)pair.list.get(ii + 1);
/*      */ 
/*  257 */         if (g.getGfaFcstHr().equals(gNext.getGfaFcstHr()))
/*      */         {
/*  259 */           ArrayList l = new ArrayList();
/*  260 */           l.add(g);
/*  261 */           l.add(gNext);
/*      */ 
/*  263 */           ArrayList shrinkWrapped = createModifiedHull(getGeometryFactory(), l);
/*      */ 
/*  265 */           gNext = new Gfa(PgenUtil.gridToLatlon(shrinkWrapped), 
/*  266 */             g.getGfaHazard(), g.getGfaFcstHr(), g.getGfaTag(), 
/*  267 */             g.getGfaDesk(), g.getGfaIssueType(), g.getGfaType(), 
/*  268 */             g.getGfaValues());
/*      */ 
/*  270 */           pair.list.remove(ii + 1);
/*  271 */           pair.list.add(ii + 1, gNext);
/*      */ 
/*  273 */           toRemove.add(g);
/*      */         }
/*      */       }
/*      */ 
/*  277 */       for (Gfa remove : toRemove) {
/*  278 */         pair.list.remove(remove);
/*      */       }
/*      */ 
/*  282 */       ArrayList toUnite = new ArrayList();
/*      */ 
/*  284 */       for (int ii = 0; ii < pair.list.size() - 1; ii++)
/*      */       {
/*  286 */         Gfa g = (Gfa)pair.list.get(ii);
/*  287 */         Gfa gNext = (Gfa)pair.list.get(ii + 1);
/*      */ 
/*  289 */         ArrayList l = new ArrayList();
/*  290 */         l.add(g);
/*  291 */         l.add(gNext);
/*      */ 
/*  293 */         ArrayList shrinkWrapped = createModifiedHull(getGeometryFactory(), l);
/*      */ 
/*  295 */         Coordinate[] a = new Coordinate[shrinkWrapped.size() + 1];
/*  296 */         shrinkWrapped.toArray(a);
/*  297 */         a[(a.length - 1)] = a[0];
/*  298 */         LinearRing shell = getGeometryFactory().createLinearRing(a);
/*  299 */         Polygon poly = getGeometryFactory().createPolygon(shell, null);
/*      */ 
/*  301 */         toUnite.add(poly);
/*      */       }
/*      */ 
/*  310 */       ArrayList coordinates = new ArrayList();
/*      */ 
/*  312 */       if (toUnite.isEmpty()) {
/*  313 */         coordinates = ((Gfa)pair.list.get(0)).getPoints();
/*      */       }
/*      */       else {
/*  316 */         Geometry union = (Geometry)toUnite.get(0);
/*  317 */         for (int ii = 1; ii < toUnite.size(); ii++) {
/*  318 */           Polygon r = (Polygon)toUnite.get(ii);
/*  319 */           union = union.union(r);
/*      */         }
/*      */ 
/*  322 */         Coordinate[] c = union.getCoordinates();
/*  323 */         coordinates.clear();
/*  324 */         coordinates.addAll(Arrays.asList(c));
/*  325 */         coordinates.remove(coordinates.size() - 1);
/*  326 */         coordinates = PgenUtil.gridToLatlon(coordinates);
/*      */       }
/*      */ 
/*  335 */       Geometry intlBoundInGrid = GfaClip.getInstance().getFaInternationalBoundInGrid();
/*      */ 
/*  337 */       ArrayList pointsInGrid = PgenUtil.latlonToGrid(coordinates);
/*      */ 
/*  339 */       Geometry smearPolyInGrid = GfaClip.getInstance().pointsToGeometry(pointsInGrid);
/*      */ 
/*  341 */       Geometry clipAgstIntlBnd = null;
/*  342 */       if (smearPolyInGrid.intersects(intlBoundInGrid)) {
/*  343 */         clipAgstIntlBnd = smearPolyInGrid.intersection(intlBoundInGrid);
/*      */       }
/*      */ 
/*  346 */       if ((clipAgstIntlBnd != null) && (clipAgstIntlBnd.getNumGeometries() > 0) && 
/*  347 */         (GfaClip.getInstance().isBiggerInGrid(clipAgstIntlBnd)))
/*      */       {
/*  358 */         ArrayList snapped = GfaSnap.getInstance().snapPolygon(coordinates);
/*      */ 
/*  361 */         Gfa smear = (Gfa)pair.list.get(0);
/*  362 */         smear = new Gfa(snapped, smear.getGfaHazard(), pair.fcstHr, smear.getGfaTag(), 
/*  363 */           smear.getGfaDesk(), smear.getGfaIssueType(), type, values);
/*      */ 
/*  365 */         if (pair.hadCancelledRemoved) {
/*  366 */           smear.setGfaIssueType("AMD");
/*      */         }
/*      */ 
/*  369 */         String worstIssueType = GfaWorstAttr.getGfaWorstIssueType(pair.all);
/*  370 */         smear.setGfaIssueType(worstIssueType);
/*      */ 
/*  373 */         ArrayList clippedWithRegions = GfaClip.getInstance().clipFARegions(smear, pair.original);
/*      */ 
/*  375 */         if ((clippedWithRegions != null) && (clippedWithRegions.size() != 0))
/*      */         {
/*  382 */           for (Gfa gg : clippedWithRegions) {
/*  383 */             if (!gg.toPolygon().isValid()) {
/*  384 */               ArrayList newPts = new ArrayList(gg.getPoints());
/*  385 */               for (int ii = 0; ii < gg.getPoints().size(); ii++) {
/*  386 */                 int jj = ii + 1;
/*  387 */                 if (jj == gg.getPoints().size()) jj = 0;
/*      */ 
/*  389 */                 double distance = GfaSnap.getInstance().distance((Coordinate)gg.getPoints().get(ii), (Coordinate)gg.getPoints().get(jj));
/*  390 */                 if (distance / 1852.0D <= GfaSnap.CLUSTER_DIST)
/*      */                 {
/*  392 */                   if (intlBndsPts.contains(gg.getPoints().get(ii))) {
/*  393 */                     newPts.remove(gg.getPoints().get(jj));
/*      */                   }
/*      */                   else {
/*  396 */                     newPts.remove(gg.getPoints().get(ii));
/*      */                   }
/*      */                 }
/*      */               }
/*      */ 
/*  401 */               if (newPts.size() < gg.getPoints().size()) {
/*  402 */                 gg.setPointsOnly(newPts);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  412 */           ((Gfa)clippedWithRegions.get(0)).addAttribute("TEMP_SMEAR", smear);
/*      */ 
/*  414 */           for (Gfa g : clippedWithRegions) {
/*  415 */             g.addAttribute("TEMP_SMEAR", smear);
/*      */ 
/*  417 */             if (smear.isOutlook()) {
/*  418 */               g.addAttribute("OUTLOOKS", clippedWithRegions);
/*      */             }
/*      */             else {
/*  421 */               g.addAttribute("AIRMETS", clippedWithRegions);
/*      */             }
/*      */ 
/*  424 */             g.addAttribute("PAIR", pair);
/*      */           }
/*      */ 
/*  430 */           for (Gfa g : pair.original) {
/*  431 */             if (smear.isOutlook()) {
/*  432 */               g.addAttribute("OUTLOOKS", clippedWithRegions);
/*      */             }
/*      */             else {
/*  435 */               g.addAttribute("AIRMETS", clippedWithRegions);
/*      */             }
/*      */           }
/*      */ 
/*  439 */           if (pair.canceled != null) {
/*  440 */             for (Gfa g : pair.canceled) {
/*  441 */               if (smear.isOutlook()) {
/*  442 */                 g.addAttribute("OUTLOOKS", clippedWithRegions);
/*      */               }
/*      */               else {
/*  445 */                 g.addAttribute("AIRMETS", clippedWithRegions);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  452 */           listOfLists.add(clippedWithRegions);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  460 */     for (ArrayList clippedGroup : listOfLists)
/*      */     {
/*  462 */       Gfa smear = (Gfa)((Gfa)clippedGroup.get(0)).getAttribute("TEMP_SMEAR", Gfa.class);
/*      */ 
/*  464 */       ((Gfa)clippedGroup.get(0)).removeAttribute("TEMP_SMEAR");
/*      */ 
/*  466 */       FcstHrListPair pair = (FcstHrListPair)((Gfa)clippedGroup.get(0)).getAttribute("PAIR", FcstHrListPair.class);
/*      */ 
/*  469 */       GfaRules.applyRules(smear, clippedGroup, pair.original);
/*      */ 
/*  472 */       GfaWorstAttr.apply(smear, clippedGroup, pair.original, pair.canceled);
/*      */ 
/*  475 */       list.addAll(clippedGroup);
/*      */     }
/*      */ 
/*  480 */     clearAttributes(listOfLists);
/*      */ 
/*  483 */     for (AbstractDrawableComponent gg : list)
/*  484 */       if ((gg instanceof Gfa))
/*  485 */         ((Gfa)gg).setGfaVorText(Gfa.buildVorText((Gfa)gg));
/*      */   }
/*      */ 
/*      */   private TreeSet<FcstHrListPair> splitSnapshots(TreeSet<Gfa> tree)
/*      */   {
/*  562 */     TreeSet pairs = new TreeSet();
/*  563 */     ArrayList list1 = new ArrayList();
/*  564 */     ArrayList list2 = new ArrayList();
/*  565 */     ArrayList list3 = new ArrayList();
/*      */ 
/*  572 */     for (Gfa gfa : tree) {
/*  573 */       int[] hm = Gfa.getHourMinInt(gfa.getGfaFcstHr());
/*  574 */       if (hm[0] < 6) {
/*  575 */         list1.add(gfa);
/*  576 */       } else if (hm[0] == 6) {
/*  577 */         list1.add(gfa);
/*  578 */         list2.add(gfa);
/*      */       } else {
/*  580 */         list2.add(gfa);
/*      */       }
/*      */ 
/*  583 */       list3.add(gfa);
/*      */     }
/*      */ 
/*  587 */     HashMap map = new HashMap();
/*  588 */     splitByHazardTag(list1, map);
/*  589 */     splitByHazardTag(list2, map);
/*      */ 
/*  592 */     HashMap mapall = new HashMap();
/*  593 */     splitByHazardTag(list3, mapall);
/*      */ 
/*  602 */     ArrayList all66 = new ArrayList();
/*      */     ArrayList allss;
/*  603 */     for (String key : map.keySet()) {
/*  604 */       ArrayList l = (ArrayList)map.get(key);
/*  605 */       if (!l.isEmpty()) {
/*  606 */         String fcst = determineFcst(l);
/*  607 */         if ("6-6".equals(fcst))
/*      */         {
/*  609 */           all66.add(l);
/*      */         }
/*      */         else {
/*  612 */           ArrayList canceledSS = findCancelled(l);
/*  613 */           allss = findFullList((Gfa)l.get(0), mapall);
/*  614 */           FcstHrListPair pair = new FcstHrListPair(fcst, l, canceledSS, allss);
/*      */ 
/*  616 */           pairs.add(pair);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  624 */     label455: for (ArrayList l66 : all66)
/*      */     {
/*  628 */       Gfa gfa = (Gfa)l66.get(0);
/*      */ 
/*  630 */       boolean found = false;
/*      */       Iterator localIterator3;
/*      */       Gfa g;
/*  631 */       for (allss = map.values().iterator(); allss.hasNext(); 
/*  633 */         localIterator3.hasNext())
/*      */       {
/*  631 */         ArrayList l = (ArrayList)allss.next();
/*  632 */         if ((l66 == l) || (isOutlook(determineFcst(l)))) break label455;
/*  633 */         localIterator3 = l.iterator(); continue; g = (Gfa)localIterator3.next();
/*  634 */         if (g == gfa) {
/*  635 */           found = true;
/*  636 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  641 */       if (!found) {
/*  642 */         ArrayList all = findFullList((Gfa)l66.get(0), mapall);
/*  643 */         pairs.add(new FcstHrListPair("6-6", l66, null, all));
/*      */ 
/*  645 */         for (String key : map.keySet()) {
/*  646 */           if ((key.endsWith(gfa.getGfaHazard() + gfa.getGfaTag() + gfa.getGfaDesk())) && (
/*  647 */             (key.contains("-9")) || (key.contains("-12")))) {
/*  648 */             gfa.addAttribute("OTLK_LIST", map.get(key));
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  655 */     return pairs;
/*      */   }
/*      */ 
/*      */   private void splitByHazardTag(ArrayList<Gfa> list, HashMap<String, ArrayList<Gfa>> map)
/*      */   {
/*  670 */     for (Gfa g : list)
/*      */     {
/*  672 */       String key = determineFcst(list) + g.getGfaHazard() + g.getGfaTag() + g.getGfaDesk();
/*      */ 
/*  674 */       if (map.containsKey(key)) {
/*  675 */         if (!((ArrayList)map.get(key)).contains(g))
/*  676 */           ((ArrayList)map.get(key)).add(g);
/*      */       }
/*      */       else
/*      */       {
/*  680 */         ArrayList l = new ArrayList();
/*  681 */         l.add(g);
/*  682 */         map.put(key, l);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private String determineFcst(ArrayList<Gfa> list1)
/*      */   {
/*  695 */     String str1 = ((Gfa)list1.get(0)).getGfaFcstHr().split(" ")[0];
/*  696 */     String str2 = ((Gfa)list1.get(list1.size() - 1)).getGfaFcstHr().split(" ")[0];
/*  697 */     return str1 + "-" + str2;
/*      */   }
/*      */ 
/*      */   private void clearAttributes(ArrayList<ArrayList<Gfa>> listOfLists)
/*      */   {
/*      */     Iterator localIterator2;
/*  706 */     for (Iterator localIterator1 = listOfLists.iterator(); localIterator1.hasNext(); 
/*  707 */       localIterator2.hasNext())
/*      */     {
/*  706 */       ArrayList clipped = (ArrayList)localIterator1.next();
/*  707 */       localIterator2 = clipped.iterator(); continue; Gfa g = (Gfa)localIterator2.next();
/*  708 */       g.removeAttribute("AIRMETS");
/*  709 */       g.removeAttribute("PAIR");
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isOutlook(String fcst)
/*      */   {
/*  719 */     String[] s = fcst.split("-");
/*  720 */     if ((s.length == 2) && (s[1].isEmpty())) return false;
/*  721 */     String h = s[0].split(":")[0];
/*  722 */     return Integer.parseInt(h) >= 6;
/*      */   }
/*      */ 
/*      */   private ArrayList<Gfa> findCancelled(ArrayList<Gfa> list)
/*      */   {
/*  740 */     ArrayList cancelled = new ArrayList();
/*  741 */     for (Gfa gfa : list) {
/*  742 */       if ("CAN".equalsIgnoreCase(gfa.getGfaIssueType())) {
/*  743 */         cancelled.add(gfa);
/*      */       }
/*      */     }
/*      */ 
/*  747 */     if (cancelled.size() != 0)
/*      */     {
/*  749 */       if (list.size() != cancelled.size()) {
/*  750 */         for (Gfa gfa : cancelled) {
/*  751 */           list.remove(gfa);
/*      */         }
/*      */       }
/*      */       else {
/*  755 */         cancelled.clear();
/*      */       }
/*      */     }
/*      */ 
/*  759 */     return cancelled;
/*      */   }
/*      */ 
/*      */   private ArrayList<Coordinate> createModifiedHull(GeometryFactory gf, ArrayList<Gfa> list)
/*      */   {
/*  767 */     LinearRing[] rings = new LinearRing[list.size()];
/*  768 */     int i = 0;
/*      */ 
/*  770 */     for (Gfa g : list)
/*      */     {
/*  772 */       Coordinate[] a = new Coordinate[g.getPoints().size() + 1];
/*  773 */       g.getPoints().toArray(a);
/*  774 */       a[(a.length - 1)] = a[0];
/*      */ 
/*  777 */       a = PgenUtil.latlonToGrid(a);
/*      */ 
/*  779 */       LinearRing ring = gf.createLinearRing(a);
/*  780 */       if (CGAlgorithms.isCCW(a))
/*      */       {
/*  782 */         LineString ls = (LineString)ring.reverse();
/*  783 */         ring = gf.createLinearRing(ls.getCoordinates());
/*      */       }
/*      */ 
/*  786 */       rings[(i++)] = ring;
/*      */     }
/*      */ 
/*  789 */     GeometryCollection poly = new GeometryCollection(rings, gf);
/*  790 */     Geometry hull = poly.convexHull();
/*  791 */     Coordinate[] hullCoordinates = hull.getCoordinates();
/*      */ 
/*  798 */     ArrayList hullCoordinatesList = shrinkWrapSmear(gf, rings, hullCoordinates);
/*      */ 
/*  800 */     return hullCoordinatesList;
/*      */   }
/*      */ 
/*      */   private ArrayList<Coordinate> shrinkWrapSmear(GeometryFactory gf, LinearRing[] rings, Coordinate[] hullCoordinates)
/*      */   {
/*  814 */     ArrayList hullCoordinatesList = new ArrayList();
/*      */ 
/*  816 */     for (int i = 0; i < hullCoordinates.length - 1; i++) {
/*  817 */       Coordinate c = hullCoordinates[i];
/*  818 */       Coordinate cNext = i + 1 == hullCoordinates.length - 1 ? hullCoordinates[0] : 
/*  819 */         hullCoordinates[(i + 1)];
/*      */ 
/*  821 */       CoordinateArraySequence cas = new CoordinateArraySequence(new Coordinate[] { c });
/*  822 */       Point p = new Point(cas, gf);
/*  823 */       cas = new CoordinateArraySequence(new Coordinate[] { cNext });
/*  824 */       Point pNext = new Point(cas, gf);
/*      */ 
/*  826 */       if (!hullCoordinatesList.contains(c)) hullCoordinatesList.add(c);
/*      */ 
/*  828 */       for (int j = 0; j < rings.length; j++) {
/*  829 */         LinearRing ring = rings[j];
/*      */ 
/*  831 */         if ((ring.contains(p)) && (ring.contains(pNext)))
/*      */         {
/*  833 */           Coordinate[] ringCoordinates = ring.getCoordinates();
/*  834 */           List rcList = Arrays.asList(ringCoordinates);
/*  835 */           int index = rcList.indexOf(c);
/*  836 */           int indexNext = rcList.indexOf(cNext);
/*      */ 
/*  838 */           if (Math.abs(indexNext - index) > 1)
/*      */           {
/*  840 */             indexNext = indexNext > index ? indexNext : indexNext + ringCoordinates.length;
/*      */ 
/*  843 */             for (int ii = index; ii < indexNext; ii++) {
/*  844 */               Coordinate toInsert = ringCoordinates[(ii % ringCoordinates.length)];
/*  845 */               if (!hullCoordinatesList.contains(toInsert)) {
/*  846 */                 hullCoordinatesList.add(toInsert);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  853 */     return hullCoordinatesList;
/*      */   }
/*      */ 
/*      */   public static GeometryFactory getGeometryFactory()
/*      */   {
/*  864 */     if (geometryFactory == null) {
/*  865 */       geometryFactory = new GeometryFactory();
/*      */     }
/*      */ 
/*  868 */     return geometryFactory;
/*      */   }
/*      */ 
/*      */   public static HashMap<String, String> findGfaTopBots(ArrayList<Gfa> list)
/*      */   {
/*  881 */     Gfa g = (Gfa)list.get(0);
/*      */ 
/*  884 */     Comparator comp = new Comparator() {
/*      */       public int compare(String s1, String s2) {
/*  886 */         if (s1.equals(s2))
/*  887 */           return 0;
/*  888 */         if (("SFC".equalsIgnoreCase(s1)) && ("FZL".equalsIgnoreCase(s2)))
/*  889 */           return -1;
/*  890 */         if (("FZL".equalsIgnoreCase(s1)) && ("SFC".equalsIgnoreCase(s2)))
/*  891 */           return 1;
/*  892 */         if (("SFC".equalsIgnoreCase(s1)) || ("FZL".equalsIgnoreCase(s1)))
/*  893 */           return -1;
/*  894 */         if (("SFC".equalsIgnoreCase(s2)) || ("FZL".equalsIgnoreCase(s2))) {
/*  895 */           return 1;
/*      */         }
/*      */ 
/*  898 */         return s1.compareTo(s2);
/*      */       }
/*      */     };
/*  903 */     HashMap values = new HashMap();
/*      */ 
/*  905 */     for (String key : g.getGfaValues().keySet()) {
/*  906 */       values.put(key, (String)g.getGfaValues().get(key));
/*      */     }
/*      */ 
/*  912 */     TreeSet tops = new TreeSet(comp);
/*  913 */     TreeSet bottoms = new TreeSet(comp);
/*  914 */     for (Gfa gfa : list) {
/*  915 */       if (gfa.getGfaTop() != null) {
/*  916 */         tops.add(gfa.getGfaTop());
/*      */       }
/*      */ 
/*  919 */       if (gfa.getGfaBottom() != null) {
/*  920 */         bottoms.add(gfa.getGfaBottom());
/*      */       }
/*      */     }
/*      */ 
/*  924 */     if ((tops.size() > 0) && (bottoms.size() > 0)) {
/*  925 */       values.put("Top", (String)tops.last());
/*  926 */       values.put("Bottom", (String)bottoms.first());
/*  927 */       values.put("Top/Bottom", (String)tops.last() + "/" + (String)bottoms.first());
/*      */     }
/*      */ 
/*  934 */     tops.clear();
/*      */ 
/*  937 */     while ((bottoms.size() > 0) && ("FZL".equals(bottoms.first()))) {
/*  938 */       bottoms.remove(bottoms.first());
/*      */     }
/*      */ 
/*  941 */     for (Gfa gfa : list) {
/*  942 */       if ((gfa.getGfaValue("FZL Top/Bottom") != null) && 
/*  943 */         (!gfa.getGfaValue("FZL Top/Bottom").isEmpty()))
/*      */       {
/*  945 */         String[] s = gfa.getGfaValue("FZL Top/Bottom").split("/");
/*      */ 
/*  947 */         tops.add(s[0]);
/*  948 */         bottoms.add(s[1]);
/*      */       }
/*      */     }
/*      */ 
/*  952 */     if (tops.size() > 0) {
/*  953 */       values.put("FZL Top/Bottom", (String)tops.last() + "/" + (String)bottoms.first());
/*      */     }
/*      */ 
/*  956 */     return values;
/*      */   }
/*      */ 
/*      */   public static String findGfaSubTypes(ArrayList<Gfa> list)
/*      */   {
/*  969 */     String cig = "";
/*  970 */     String vis = "";
/*      */ 
/*  993 */     TreeSet visTypes = new TreeSet(new Comparator()
/*      */     {
/*  974 */       ArrayList<String> types = new ArrayList();
/*      */ 
/*      */       public int compare(String s1, String s2)
/*      */       {
/*  984 */         if (s1.equals(s2)) return 0;
/*  985 */         int i1 = this.types.indexOf(s1);
/*  986 */         int i2 = this.types.indexOf(s2);
/*  987 */         return i1 - i2;
/*      */       }
/*      */     });
/*      */     String[] s;
/*  994 */     for (Gfa gfa : list) {
/*  995 */       if ((gfa.getGfaType() != null) && (!gfa.getGfaType().isEmpty()))
/*      */       {
/*  997 */         s = gfa.getGfaType().replace("/VIS", ":VIS").split(":");
/*  998 */         int ii = 0;
/*  999 */         if (s[0].startsWith("CIG")) {
/* 1000 */           cig = s[0];
/* 1001 */           ii = 1;
/*      */         }
/*      */ 
/* 1004 */         if ((s.length > ii) && (!s[ii].isEmpty())) {
/* 1005 */           int last = s[ii].trim().lastIndexOf(" ");
/* 1006 */           if (last > -1)
/*      */           {
/* 1008 */             vis = s[ii].substring(0, last);
/* 1009 */             String[] b = s[ii].substring(last + 1).split("/");
/* 1010 */             if (b.length > 0) {
/* 1011 */               for (String str : b) {
/* 1012 */                 visTypes.add(str);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1021 */     String ret = "";
/* 1022 */     if (!cig.isEmpty()) ret = cig;
/*      */ 
/* 1024 */     if (!vis.isEmpty()) {
/* 1025 */       ret = ret + (ret.isEmpty() ? vis + " " : new StringBuilder(":").append(vis).append(" ").toString());
/*      */     }
/*      */ 
/* 1028 */     for (String s : visTypes) {
/* 1029 */       ret = ret + s + "/";
/*      */     }
/*      */ 
/* 1032 */     if (ret.endsWith("/")) ret = ret.substring(0, ret.length() - 1);
/*      */ 
/* 1034 */     return ret.replace(":VIS", "/VIS");
/*      */   }
/*      */ 
/*      */   private boolean isSameHazardAndTag(Gfa gfa1, Gfa gfa2)
/*      */   {
/* 1078 */     return (gfa1.getGfaHazard().equals(gfa2.getGfaHazard())) && 
/* 1079 */       (gfa1.getGfaTag().equals(gfa2.getGfaTag())) && 
/* 1080 */       (gfa1.getGfaDesk().equals(gfa2.getGfaDesk()));
/*      */   }
/*      */ 
/*      */   private List<Gfa> formatFrzl(Layer layer, boolean checkTag)
/*      */   {
/* 1090 */     List smears = new ArrayList();
/*      */ 
/* 1092 */     DrawableElement de = this.drawingLayer.getSelectedDE();
/*      */ 
/* 1094 */     for (AbstractDrawableComponent adc : layer.getDrawables())
/*      */     {
/* 1096 */       if ((!checkTag) || 
/* 1097 */         (de == null) || (isSameHazardAndTag((Gfa)adc, (Gfa)de)))
/*      */       {
/* 1102 */         if (((adc instanceof Gfa)) && 
/* 1103 */           (((Gfa)adc).getGfaHazard().equalsIgnoreCase("FZLVL")) && 
/* 1104 */           (!((Gfa)adc).isSnapshot()))
/*      */         {
/* 1106 */           smears.add((Gfa)adc);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1111 */     if (smears.isEmpty()) return smears;
/* 1112 */     return new FrzlFormatter(smears).format();
/*      */   }
/*      */ 
/*      */   public static void writePoints(ArrayList<Coordinate> snp, String filename)
/*      */   {
/* 1122 */     Writer output = null;
/* 1123 */     File file = new File("/export/cdbsrv/jwu/" + filename);
/*      */     try {
/* 1125 */       FileWriter fw = new FileWriter(file);
/* 1126 */       output = new BufferedWriter(fw);
/*      */ 
/* 1128 */       for (Coordinate cc : snp) {
/* 1129 */         String c = new String("<Point Lat=\"" + cc.y + "\" Lon=\" " + cc.x + " \"/>\n");
/* 1130 */         output.write(c);
/*      */       }
/*      */ 
/* 1133 */       output.close();
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 1137 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void validateAllGfas()
/*      */   {
/* 1147 */     PgenResource psrc = PgenSession.getInstance().getPgenResource();
/* 1148 */     StringBuilder msg = new StringBuilder();
/* 1149 */     msg.append("Warning: The following Gfas are invalid.  Please correct them or they will be ");
/* 1150 */     msg.append("excluded in the FROM action.\n\n");
/*      */ 
/* 1152 */     int nn = 0;
/*      */     Iterator localIterator2;
/* 1153 */     for (Iterator localIterator1 = psrc.getProducts().iterator(); localIterator1.hasNext(); 
/* 1154 */       localIterator2.hasNext())
/*      */     {
/* 1153 */       Product prd = (Product)localIterator1.next();
/* 1154 */       localIterator2 = prd.getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/* 1155 */       for (AbstractDrawableComponent adc : layer.getDrawables()) {
/* 1156 */         if (((adc instanceof Gfa)) && (!((Gfa)adc).isValid())) {
/* 1157 */           nn++;
/* 1158 */           Gfa gg = (Gfa)adc;
/* 1159 */           msg.append(prd.getName() + "\t" + prd.getType() + "\t" + layer.getName() + "\t" + 
/* 1160 */             gg.getGfaHazard() + "," + gg.getGfaFcstHr() + "," + 
/* 1161 */             gg.getGfaTag() + gg.getGfaDesk() + "\n");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1168 */     if (nn > 0) {
/* 1169 */       MessageDialog confirmDlg = new MessageDialog(
/* 1170 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 1171 */         "Invalid GFA Polygons", null, msg.toString(), 
/* 1172 */         4, new String[] { "OK" }, 0);
/*      */ 
/* 1174 */       confirmDlg.open();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void validateActiveGfas()
/*      */   {
/* 1185 */     Layer curLayer = PgenSession.getInstance().getPgenResource().getActiveLayer();
/* 1186 */     StringBuilder msg = new StringBuilder();
/* 1187 */     msg.append("Warning: The following Gfas are invalid.  Please correct them or they will be \n");
/* 1188 */     msg.append("excluded in the FROM action.\n\n");
/*      */ 
/* 1190 */     int nn = 0;
/* 1191 */     for (AbstractDrawableComponent adc : curLayer.getDrawables()) {
/* 1192 */       if (((adc instanceof Gfa)) && (!((Gfa)adc).isValid())) {
/* 1193 */         nn++;
/* 1194 */         Gfa gg = (Gfa)adc;
/* 1195 */         msg.append(gg.getGfaHazard() + "," + gg.getGfaFcstHr() + "," + 
/* 1196 */           gg.getGfaTag() + gg.getGfaDesk() + "\n");
/*      */       }
/*      */     }
/*      */ 
/* 1200 */     if (nn > 0) {
/* 1201 */       MessageDialog confirmDlg = new MessageDialog(
/* 1202 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 1203 */         "Invalid GFA Polygons", null, msg.toString(), 
/* 1204 */         4, new String[] { "OK" }, 0);
/*      */ 
/* 1206 */       confirmDlg.open();
/*      */     }
/*      */   }
/*      */ 
/*      */   private ArrayList<Gfa> findFullList(Gfa gfa, HashMap<String, ArrayList<Gfa>> mapAll)
/*      */   {
/* 1214 */     ArrayList fullList = new ArrayList();
/* 1215 */     if ((mapAll != null) && (mapAll.size() > 0)) {
/* 1216 */       String ttd = gfa.getGfaHazard() + gfa.getGfaTag() + gfa.getGfaDesk();
/* 1217 */       for (String key : mapAll.keySet()) {
/* 1218 */         if (key.contains(ttd)) {
/* 1219 */           fullList.addAll((Collection)mapAll.get(key));
/* 1220 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1225 */     return fullList;
/*      */   }
/*      */ 
/*      */   class FcstHrListPair
/*      */     implements Comparable<FcstHrListPair>
/*      */   {
/*  504 */     boolean hadCancelledRemoved = false;
/*      */     String fcstHr;
/*      */     ArrayList<Gfa> list;
/*      */     ArrayList<Gfa> original;
/*      */     ArrayList<Gfa> canceled;
/*      */     ArrayList<Gfa> all;
/*      */ 
/*      */     public FcstHrListPair(ArrayList<Gfa> fcstHr, ArrayList<Gfa> list, ArrayList<Gfa> canceled)
/*      */     {
/*  513 */       this.fcstHr = fcstHr;
/*  514 */       this.list = list;
/*  515 */       this.original = new ArrayList();
/*  516 */       this.original.addAll(list);
/*  517 */       this.canceled = new ArrayList();
/*  518 */       this.all = allss;
/*      */ 
/*  520 */       if (canceled != null) {
/*  521 */         this.canceled.addAll(canceled);
/*  522 */         if (canceled.size() > 0)
/*  523 */           this.hadCancelledRemoved = true;
/*      */       }
/*      */     }
/*      */ 
/*      */     public int compareTo(FcstHrListPair o)
/*      */     {
/*  530 */       String[] s = this.fcstHr.split("-");
/*  531 */       int[] hm0 = Gfa.getHourMinInt(s[0]);
/*  532 */       int[] hm1 = Gfa.getHourMinInt(s[1]);
/*  533 */       boolean isOutlook = (hm0[0] >= 6) && (hm1[0] > 6);
/*      */ 
/*  535 */       if (this == o)
/*  536 */         return 0;
/*  537 */       if (isOutlook) {
/*  538 */         return 1;
/*      */       }
/*  540 */       return -1;
/*      */     }
/*      */ 
/*      */     public ArrayList<Gfa> getOriginal()
/*      */     {
/*  545 */       return this.original;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SnapshotComparator
/*      */     implements Comparator<Gfa>
/*      */   {
/*      */     private SnapshotComparator()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int compare(Gfa g1, Gfa g2)
/*      */     {
/* 1047 */       if ((g1 == null) || (g2 == null) || (!g1.isSnapshot()) || (!g2.isSnapshot())) return -1;
/*      */       try
/*      */       {
/* 1050 */         String f1 = g1.getGfaFcstHr().split(" ")[0];
/* 1051 */         if (f1.contains(":")) f1 = f1.replace(":", ".");
/* 1052 */         String f2 = g2.getGfaFcstHr().split(" ")[0];
/* 1053 */         if (f2.contains(":")) f2 = f2.replace(":", ".");
/*      */ 
/* 1055 */         double d1 = Double.parseDouble(f1);
/* 1056 */         double d2 = Double.parseDouble(f2);
/*      */ 
/* 1058 */         if ((d1 < d2) || (Math.abs(d2 - d1) <= 0.0001D)) {
/* 1059 */           return -1;
/*      */         }
/* 1061 */         return 1;
/*      */       }
/*      */       catch (RuntimeException e) {
/* 1064 */         e.printStackTrace();
/* 1065 */       }return -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaFormat
 * JD-Core Version:    0.6.2
 */