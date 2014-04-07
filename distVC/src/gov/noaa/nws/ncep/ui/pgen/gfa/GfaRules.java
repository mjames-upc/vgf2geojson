/*      */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*      */ 
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.MultiPolygon;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ public class GfaRules
/*      */ {
/*      */   public static final String WORDING = "WORDING";
/*      */   public static final double AREA_LIMIT = 3000.0D;
/*      */   private static final double PRECISION = 1.E-09D;
/*   85 */   public static String MTOBSC_XPATH = "/MT_OBSC";
/*      */ 
/*      */   public static void applyRules(Gfa smear, ArrayList<Gfa> clipped, ArrayList<Gfa> snapshots)
/*      */   {
/*  100 */     areaRules(smear, clipped, snapshots);
/*      */ 
/*  105 */     removeOutlooks(clipped, snapshots);
/*      */ 
/*  111 */     GfaReducePoint.getInstance().reducePoints(clipped, snapshots);
/*      */ 
/*  116 */     setWording(clipped, snapshots);
/*      */   }
/*      */ 
/*      */   private static void areaRules(Gfa original, ArrayList<Gfa> clippedList, ArrayList<Gfa> snapshots)
/*      */   {
/*  138 */     if (original.getGfaHazard().equals("FZLVL")) {
/*  139 */       return;
/*      */     }
/*      */ 
/*  145 */     for (Gfa g : clippedList)
/*  146 */       findGfaAreaInGrid(g, snapshots);
/*      */   }
/*      */ 
/*      */   private static void removeOutlooks(ArrayList<Gfa> clipped, ArrayList<Gfa> original)
/*      */   {
/*  159 */     ArrayList toRemove = new ArrayList();
/*  160 */     for (Gfa g : clipped)
/*  161 */       if (g.isOutlook()) {
/*  162 */         boolean intersectsWithAtLeastOneOriginal = false;
/*  163 */         for (Gfa o : original) {
/*  164 */           if (!"6".equals(o.getGfaFcstHr()))
/*      */           {
/*  166 */             Polygon gP = GfaClip.getInstance().gfaToPolygonInGrid(g);
/*  167 */             Polygon oP = GfaClip.getInstance().gfaToPolygonInGrid(o);
/*  168 */             Geometry intersection = gP.intersection(oP);
/*  169 */             if (((intersection instanceof Polygon)) || ((intersection instanceof MultiPolygon)))
/*  170 */               intersectsWithAtLeastOneOriginal = true;
/*      */           }
/*      */         }
/*  173 */         if (!intersectsWithAtLeastOneOriginal) toRemove.add(g);
/*      */       }
/*  175 */     clipped.removeAll(toRemove);
/*      */   }
/*      */ 
/*      */   public static boolean compareCoordinates(Coordinate c1, Coordinate c2)
/*      */   {
/*  190 */     if ((Math.abs(c1.x - c2.x) < 1.E-09D) && (Math.abs(c1.y - c2.y) < 1.E-09D)) {
/*  191 */       return true;
/*      */     }
/*  193 */     return false;
/*      */   }
/*      */ 
/*      */   private static void setWording(ArrayList<Gfa> clipped, ArrayList<Gfa> originalSS)
/*      */   {
/*  207 */     ArrayList checkStates = new ArrayList();
/*      */     String stList;
/*  208 */     for (Gfa smear : clipped)
/*      */     {
/*  211 */       updateGfaStates(smear);
/*      */ 
/*  214 */       stList = smear.getGfaStates();
/*  215 */       if ((stList != null) && (stList.trim().length() > 1)) {
/*  216 */         checkStates.add(smear);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  224 */     ArrayList invalidOtlk = new ArrayList();
/*  225 */     for (Gfa smear : checkStates)
/*      */     {
/*  237 */       assignXorO(originalSS, smear);
/*      */ 
/*  245 */       assignSSForecastTime(originalSS);
/*      */ 
/*  247 */       assignIssueTime(smear);
/*      */ 
/*  252 */       assignAirmetTag(smear);
/*      */ 
/*  257 */       GfaWording wording = new GfaWording();
/*      */ 
/*  260 */       wording.fromCondsDvlpg = fromCondsDvlpg(smear, originalSS);
/*      */ 
/*  263 */       wording.fromCondsEndg = fromCondsEndg(smear, originalSS);
/*      */ 
/*  265 */       int[] olkSeq = findOtlkSeq(originalSS);
/*      */ 
/*  268 */       wording.genOlk = otlkGenWording(originalSS, olkSeq);
/*  269 */       if ("MAYBE".equals(wording.genOlk)) {
/*  270 */         wording.genOlk = processMAYBE(smear, originalSS);
/*      */       }
/*      */ 
/*  276 */       if ((smear.isOutlook()) && (!"YES".equalsIgnoreCase(wording.genOlk))) {
/*  277 */         invalidOtlk.add(smear);
/*      */       }
/*      */ 
/*  282 */       if (smear.isAirmet()) {
/*  283 */         wording.condsContg = contgBydWording(originalSS, olkSeq, smear);
/*      */       }
/*      */ 
/*  287 */       if ("YES".equalsIgnoreCase(wording.genOlk)) {
/*  288 */         wording.otlkCondsDvlpg = otlkDvlpgWording(originalSS, olkSeq);
/*      */       }
/*      */ 
/*  292 */       if ("YES".equalsIgnoreCase(wording.genOlk)) {
/*  293 */         wording.otlkCondsEndg = otlkEndgWording(originalSS, olkSeq);
/*      */       }
/*  295 */       smear.addAttribute("WORDING", wording);
/*      */ 
/*  301 */       parseWording(smear, wording);
/*      */ 
/*  303 */       clearAttributes(originalSS);
/*      */     }
/*      */ 
/*  308 */     clipped.clear();
/*  309 */     if (invalidOtlk.size() > 0) checkStates.removeAll(invalidOtlk);
/*  310 */     clipped.addAll(checkStates);
/*      */   }
/*      */ 
/*      */   private static void updateGfaStates(Gfa smear)
/*      */   {
/*  329 */     HashMap states = GfaClip.getInstance().getStateBoundsInGrid();
/*  330 */     HashMap greatLakes = GfaClip.getInstance().getGreatLakesBoundsInGrid();
/*  331 */     HashMap coastalWater = GfaClip.getInstance().getCoastalWaterBoundsInGrid();
/*  332 */     LinkedHashMap stateMaps = new LinkedHashMap();
/*  333 */     stateMaps.putAll(states);
/*      */ 
/*  342 */     List mtobscsts = GfaClip.getInstance().getMtObscStates();
/*  343 */     Polygon smearP = GfaClip.getInstance().gfaToPolygonInGrid(smear);
/*  344 */     boolean is_MTOBSC = false;
/*  345 */     if (smear.getGfaHazard().equalsIgnoreCase("MT_OBSC")) {
/*  346 */       is_MTOBSC = true;
/*      */     }
/*      */ 
/*  349 */     int nState = 0;
/*      */     Geometry g;
/*  350 */     for (String key : stateMaps.keySet()) {
/*  351 */       g = (Geometry)stateMaps.get(key);
/*      */ 
/*  353 */       if ((!is_MTOBSC) || (mtobscsts.contains(key)))
/*      */       {
/*  355 */         if (smearP.intersects(g)) {
/*  356 */           nState++;
/*  357 */           updateGfaStatesField(smear, key);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  371 */     int nWaters = 0;
/*  372 */     if (!is_MTOBSC)
/*      */     {
/*  374 */       for (String key : greatLakes.keySet()) {
/*  375 */         Geometry g = (Geometry)greatLakes.get(key);
/*      */ 
/*  377 */         if (g.intersects(smearP)) {
/*  378 */           updateGfaStatesField(smear, key);
/*      */         }
/*      */       }
/*      */ 
/*  382 */       for (String key : coastalWater.keySet()) {
/*  383 */         Geometry g = (Geometry)coastalWater.get(key);
/*      */ 
/*  385 */         if (g.intersects(smearP))
/*      */         {
/*  387 */           nWaters++;
/*  388 */           updateGfaStatesField(smear, key);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  398 */     if (nWaters > 0)
/*      */     {
/*  400 */       StringBuilder s = new StringBuilder(nvl(smear.getGfaStates()));
/*      */ 
/*  406 */       if (nState > 0) {
/*  407 */         s.append(" AND");
/*      */       }
/*      */ 
/*  410 */       s.append(" CSTL WTRS");
/*      */ 
/*  412 */       smear.setGfaStates(s.toString());
/*      */     }
/*      */ 
/*  418 */     reorderStateList(smear);
/*      */   }
/*      */ 
/*      */   private static boolean updateGfaStatesField(Gfa gfa, String state)
/*      */   {
/*  431 */     boolean added = false;
/*  432 */     String s = nvl(gfa.getGfaStates());
/*  433 */     if (!s.contains(state)) {
/*  434 */       if (!s.isEmpty()) s = s + " ";
/*  435 */       s = s + state;
/*  436 */       gfa.setGfaStates(s);
/*  437 */       added = true;
/*      */     }
/*      */ 
/*  440 */     return added;
/*      */   }
/*      */ 
/*      */   private static void assignXorO(ArrayList<Gfa> originalSS, Gfa smear)
/*      */   {
/*  457 */     Polygon smearP = GfaClip.getInstance().gfaToPolygonInGrid(smear);
/*  458 */     boolean allOutlook = true;
/*  459 */     for (Gfa ss : originalSS) {
/*  460 */       assignSnapshotType(smearP, ss);
/*  461 */       String fcstHr = ss.getGfaFcstHr();
/*  462 */       if ((!"9".equals(fcstHr)) && (!"12".equals(fcstHr))) {
/*  463 */         allOutlook = false;
/*      */       }
/*      */     }
/*      */ 
/*  467 */     Gfa first = (Gfa)originalSS.get(0);
/*  468 */     ArrayList outlooks = (ArrayList)first.getAttribute("OUTLOOKS", ArrayList.class);
/*      */ 
/*  472 */     if (("6".equals(first.getGfaFcstHr())) && (!allOutlook) && 
/*  473 */       (outlooks != null))
/*      */     {
/*  475 */       ArrayList otkl = (ArrayList)first.getAttribute("OTLK_LIST", ArrayList.class);
/*  476 */       if ((otkl == null) || (otkl.isEmpty())) return;
/*  477 */       smearP = findTheSmear(smearP, outlooks);
/*      */ 
/*  479 */       for (Gfa g : otkl)
/*  480 */         if (g != originalSS.get(0))
/*  481 */           assignSnapshotType(smearP, g);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void assignSnapshotType(Polygon smearP, Gfa ss)
/*      */   {
/*  498 */     Polygon ssP = GfaClip.getInstance().gfaToPolygonInGrid(ss);
/*  499 */     Geometry intersection = ssP.intersection(smearP);
/*      */ 
/*  501 */     ss.addAttribute("SNAPSHOT_TYPE", SnapshotType.O);
/*      */ 
/*  503 */     if (PgenUtil.getSphPolyAreaInGrid(intersection) > 3000.0D)
/*  504 */       ss.addAttribute("SNAPSHOT_TYPE", SnapshotType.X);
/*      */   }
/*      */ 
/*      */   private static Polygon findTheSmear(Polygon smearP, ArrayList<Gfa> outlooks)
/*      */   {
/*  510 */     for (Gfa g : outlooks)
/*      */     {
/*  512 */       Polygon p = GfaClip.getInstance().gfaToPolygonInGrid(g);
/*      */ 
/*  514 */       Geometry intersection = p.intersection(smearP);
/*      */ 
/*  516 */       double area = PgenUtil.getSphPolyAreaInGrid(intersection);
/*      */ 
/*  518 */       if (area > 3000.0D) {
/*  519 */         return p;
/*      */       }
/*      */     }
/*  522 */     return smearP;
/*      */   }
/*      */ 
/*      */   private static void assignSSForecastTime(ArrayList<Gfa> originalSS)
/*      */   {
/*  536 */     for (Gfa ss : originalSS) {
/*  537 */       String fcstHr = ss.getGfaFcstHr();
/*  538 */       int[] hm = Gfa.getHourMinInt(nvl(fcstHr));
/*  539 */       ss.addAttribute("HOUR_INT", Integer.valueOf(hm[0]));
/*  540 */       ss.addAttribute("MIN_INT", Integer.valueOf(hm[1]));
/*  541 */       double hmD = hm[0] + hm[1] / 60.0D;
/*  542 */       SnapshotType type = (SnapshotType)ss.getAttribute("SNAPSHOT_TYPE", SnapshotType.class);
/*  543 */       if (type == SnapshotType.X) {
/*  544 */         ss.addAttribute("DVLPG_HR", Integer.valueOf((int)Math.ceil(hmD)));
/*  545 */         ss.addAttribute("ENDG_HR", Integer.valueOf((int)Math.floor(hmD)));
/*  546 */       } else if (type == SnapshotType.O) {
/*  547 */         ss.addAttribute("DVLPG_HR", Integer.valueOf((int)Math.floor(hmD)));
/*  548 */         ss.addAttribute("ENDG_HR", Integer.valueOf((int)Math.ceil(hmD)));
/*      */       }
/*      */       else
/*      */       {
/*  552 */         String err = "Snapshot type must be assigned by this time";
/*      */ 
/*  554 */         throw new IllegalArgumentException(err);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static void assignIssueTime(Gfa smear)
/*      */   {
/*  570 */     boolean overrideIssueTime = !"NRML".equalsIgnoreCase(smear.getGfaIssueType());
/*      */ 
/*  572 */     Calendar localTimeCal = Calendar.getInstance();
/*      */ 
/*  574 */     String timeStr = AirmetCycleInfo.getIssueTime();
/*  575 */     Calendar issueTimeCal = Calendar.getInstance();
/*  576 */     int hour = Integer.parseInt(timeStr.substring(0, 2));
/*  577 */     int min = Integer.parseInt(timeStr.substring(2));
/*  578 */     issueTimeCal.set(11, hour);
/*  579 */     issueTimeCal.set(12, min);
/*  580 */     issueTimeCal.set(13, 0);
/*      */ 
/*  582 */     if (overrideIssueTime)
/*      */     {
/*  584 */       if (issueTimeCal.before(localTimeCal)) {
/*  585 */         issueTimeCal = localTimeCal;
/*      */       }
/*      */       else {
/*  588 */         issueTimeCal.add(12, 1);
/*      */       }
/*      */     }
/*      */ 
/*  592 */     smear.addAttribute("ISSUE_TIME", issueTimeCal);
/*      */ 
/*  594 */     Calendar untilTimeCal = AirmetCycleInfo.getUntilTime();
/*  595 */     smear.addAttribute("UNTIL_TIME", untilTimeCal);
/*      */ 
/*  597 */     Calendar otlkEndTime = Calendar.getInstance();
/*  598 */     otlkEndTime.set(5, untilTimeCal.get(5));
/*  599 */     otlkEndTime.set(11, untilTimeCal.get(11) + 6);
/*  600 */     otlkEndTime.set(12, untilTimeCal.get(12));
/*  601 */     otlkEndTime.set(13, untilTimeCal.get(13));
/*      */ 
/*  603 */     smear.addAttribute("OUTLOOK_END_TIME", otlkEndTime);
/*      */   }
/*      */ 
/*      */   private static String fromCondsDvlpg(Gfa smear, ArrayList<Gfa> originalSS)
/*      */   {
/*  616 */     int endHour = 6;
/*      */ 
/*  635 */     String wording = "";
/*  636 */     int xx = 0;
/*  637 */     int yy = 0;
/*  638 */     Gfa ss = (Gfa)originalSS.get(0);
/*  639 */     int dvlpgHr = ((Integer)ss.getAttribute("DVLPG_HR", Integer.class)).intValue();
/*  640 */     if (ss.getAttribute("SNAPSHOT_TYPE") == SnapshotType.X) {
/*  641 */       if (dvlpgHr != 0)
/*      */       {
/*  643 */         if (dvlpgHr <= 3) {
/*  644 */           wording = "CONDS DVLPG +00-+" + PgenCycleTool.pad(dvlpgHr) + "Z";
/*  645 */           yy = dvlpgHr;
/*  646 */         } else if (dvlpgHr <= 6) {
/*  647 */           wording = "CONDS DVLPG +03-+" + PgenCycleTool.pad(dvlpgHr) + "Z";
/*  648 */           xx = 3;
/*  649 */           yy = dvlpgHr;
/*      */         }
/*      */       }
/*      */     } else { int lastO = 0;
/*  653 */       for (int i = 1; i < originalSS.size(); i++) {
/*  654 */         Gfa g = (Gfa)originalSS.get(i);
/*  655 */         dvlpgHr = ((Integer)ss.getAttribute("DVLPG_HR", Integer.class)).intValue();
/*  656 */         if ((g.getAttribute("SNAPSHOT_TYPE") == SnapshotType.X) && (dvlpgHr <= endHour)) {
/*  657 */           lastO = i - 1;
/*  658 */           break;
/*      */         }
/*      */       }
/*  661 */       dvlpgHr = ((Integer)((Gfa)originalSS.get(lastO)).getAttribute("DVLPG_HR", Integer.class)).intValue();
/*  662 */       wording = "CONDS DVLPG AFT +" + PgenCycleTool.pad(dvlpgHr) + "Z";
/*  663 */       xx = dvlpgHr;
/*  664 */       yy = -1;
/*      */     }
/*      */ 
/*  667 */     if ((!PgenCycleTool.isRoutine()) && (!wording.isEmpty()) && (xx >= 0) && (yy >= 0))
/*      */     {
/*  669 */       Calendar cal = (Calendar)smear.getAttribute("ISSUE_TIME", Calendar.class);
/*  670 */       int issueHrMin = cal.get(11) * 100 + cal.get(12);
/*      */ 
/*  672 */       int basetime = PgenCycleTool.getCycleHour();
/*      */ 
/*  674 */       if ((yy + basetime) * 100 <= issueHrMin) {
/*  675 */         wording = "";
/*      */       }
/*  677 */       else if ((xx + basetime) * 100 <= issueHrMin)
/*  678 */         wording = "CONDS DVLPG BY +" + PgenCycleTool.pad(yy) + "Z";
/*      */       else {
/*  680 */         wording = "CONDS DVLPG +" + PgenCycleTool.pad(xx) + "-+" + PgenCycleTool.pad(yy) + "Z";
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  685 */     return wording;
/*      */   }
/*      */ 
/*      */   private static String fromCondsEndg(Gfa smear, ArrayList<Gfa> originalSS)
/*      */   {
/*  697 */     int endHour = 6;
/*      */ 
/*  722 */     String wording = "";
/*  723 */     int xx = -1;
/*  724 */     int yy = -1;
/*  725 */     int lastSSIndex = originalSS.size() - 1;
/*  726 */     for (int i = originalSS.size() - 1; i >= 0; i--) {
/*  727 */       lastSSIndex = i;
/*  728 */       int hr = ((Integer)((Gfa)originalSS.get(lastSSIndex)).getAttribute("ENDG_HR", Integer.class)).intValue();
/*  729 */       if (hr <= endHour) break;
/*      */     }
/*  731 */     Gfa lastSS = (Gfa)originalSS.get(lastSSIndex);
/*  732 */     int lastEndgHr = ((Integer)lastSS.getAttribute("ENDG_HR", Integer.class)).intValue();
/*  733 */     String lastFcstHr = lastSS.getGfaFcstHr();
/*  734 */     int[] hm = Gfa.getHourMinInt(lastFcstHr);
/*      */ 
/*  736 */     if (lastSS.getAttribute("SNAPSHOT_TYPE") == SnapshotType.X)
/*      */     {
/*  738 */       if ((hm[0] != endHour) || (hm[1] != 0))
/*      */       {
/*  740 */         if (lastEndgHr < 3)
/*      */         {
/*  742 */           wording = "CONDS ENDG +" + PgenCycleTool.pad(lastEndgHr) + "-+03Z";
/*  743 */           xx = lastEndgHr;
/*  744 */           yy = 3;
/*  745 */         } else if (lastEndgHr <= 6)
/*      */         {
/*  747 */           wording = "CONDS ENDG +" + PgenCycleTool.pad(lastEndgHr) + "-+06Z";
/*  748 */           xx = lastEndgHr;
/*  749 */           yy = 6;
/*      */         }
/*      */       }
/*      */     } else { int firstO = lastSSIndex;
/*      */ 
/*  754 */       ArrayList otlkList = (ArrayList)lastSS.getAttribute("OTLK_LIST", ArrayList.class);
/*  755 */       if (("6".equals(((Gfa)originalSS.get(originalSS.size() - 1)).getGfaFcstHr())) && (otlkList != null)) {
/*  756 */         lastSSIndex = otlkList.size() - 1;
/*  757 */         lastSS = (Gfa)otlkList.get(lastSSIndex);
/*  758 */         for (int jj = otlkList.size() - 2; jj >= 0; jj--) {
/*  759 */           lastSS = (Gfa)otlkList.get(jj);
/*  760 */           if (lastSS.getAttribute("SNAPSHOT_TYPE") == SnapshotType.X) {
/*  761 */             firstO = jj + 1;
/*  762 */             break;
/*      */           }
/*      */         }
/*  765 */         lastEndgHr = Integer.parseInt(((Gfa)otlkList.get(firstO)).getGfaFcstHr());
/*      */       } else {
/*  767 */         for (int jj = lastSSIndex - 1; jj >= 0; jj--) {
/*  768 */           lastSS = (Gfa)originalSS.get(jj);
/*  769 */           if (lastSS.getAttribute("SNAPSHOT_TYPE") == SnapshotType.X) {
/*  770 */             firstO = jj + 1;
/*  771 */             break;
/*      */           }
/*      */         }
/*  774 */         lastEndgHr = ((Integer)((Gfa)originalSS.get(firstO)).getAttribute("ENDG_HR", Integer.class)).intValue();
/*      */       }
/*  776 */       wording = "CONDS ENDG BY +" + PgenCycleTool.pad(lastEndgHr) + "Z";
/*      */     }
/*      */ 
/*  780 */     if ((!PgenCycleTool.isRoutine()) && (!wording.isEmpty()) && (xx >= 0) && (yy >= 0))
/*      */     {
/*  782 */       Calendar cal = (Calendar)smear.getAttribute("ISSUE_TIME", Calendar.class);
/*  783 */       int issueHrMin = cal.get(11) * 100 + cal.get(12);
/*      */ 
/*  785 */       int basetime = PgenCycleTool.getCycleHour();
/*      */ 
/*  787 */       if ((yy + basetime) * 100 <= issueHrMin) {
/*  788 */         wording = "CONDS HV ENDED";
/*      */       }
/*  790 */       else if ((xx + basetime) * 100 <= issueHrMin)
/*  791 */         wording = "CONDS ENDG BY +" + PgenCycleTool.pad(yy) + "Z";
/*      */       else {
/*  793 */         wording = "CONDS ENDG +" + PgenCycleTool.pad(xx) + "-+" + PgenCycleTool.pad(yy) + "Z";
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  798 */     return wording;
/*      */   }
/*      */ 
/*      */   private static int[] findOtlkSeq(ArrayList<Gfa> originalSS) {
/*  802 */     int startHour = 6;
/*      */ 
/*  804 */     int[] olkSeq = new int[3];
/*  805 */     for (int i = 0; i < 3; i++) {
/*  806 */       olkSeq[i] = -1;
/*      */     }
/*      */ 
/*  809 */     for (int ii = 0; ii < originalSS.size(); ii++) {
/*  810 */       String fcstHr = ((Gfa)originalSS.get(ii)).getGfaFcstHr();
/*  811 */       int[] hm = Gfa.getHourMinInt(nvl(fcstHr));
/*  812 */       if (hm[1] == 0)
/*      */       {
/*  814 */         if (hm[0] == startHour)
/*  815 */           olkSeq[0] = ii;
/*  816 */         else if (hm[0] == startHour + 3)
/*  817 */           olkSeq[1] = ii;
/*  818 */         else if (hm[0] == startHour + 6)
/*  819 */           olkSeq[2] = ii;
/*      */       }
/*      */     }
/*  822 */     return olkSeq;
/*      */   }
/*      */ 
/*      */   private static String otlkGenWording(ArrayList<Gfa> originalSS, int[] olkSeq)
/*      */   {
/*  857 */     String genOlk = "NO";
/*  858 */     if ((olkSeq[0] >= 0) || (olkSeq[1] >= 0) || (olkSeq[2] >= 0)) {
/*  859 */       if ((olkSeq[0] < 0) || (
/*  860 */         (olkSeq[0] >= 0) && (((Gfa)originalSS.get(olkSeq[0])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.O)))
/*  861 */         genOlk = "YES";
/*  862 */       else if (((olkSeq[1] >= 0) && (((Gfa)originalSS.get(olkSeq[1])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.X)) || (
/*  863 */         (olkSeq[2] >= 0) && (((Gfa)originalSS.get(olkSeq[2])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.X))) {
/*  864 */         genOlk = "MAYBE";
/*      */       }
/*      */     }
/*      */ 
/*  868 */     return genOlk;
/*      */   }
/*      */ 
/*      */   private static String processMAYBE(Gfa outlook, ArrayList<Gfa> originalSS)
/*      */   {
/*  874 */     Polygon outlookP = GfaClip.getInstance().gfaToPolygon(outlook);
/*  875 */     HashMap states = GfaClip.getInstance().getStateBounds();
/*      */ 
/*  877 */     Gfa g6 = null;
/*  878 */     for (Gfa g : originalSS) {
/*  879 */       updateGfaStates(g);
/*  880 */       if ("6".equals(g.getGfaFcstHr())) g6 = g;
/*      */ 
/*      */     }
/*      */ 
/*  886 */     ArrayList airmets = (ArrayList)g6.getAttribute("AIRMETS", ArrayList.class);
/*      */ 
/*  890 */     TreeSet statesInAirmet = new TreeSet();
/*  891 */     Gfa airmet = null;
/*  892 */     for (Gfa g : airmets) {
/*  893 */       Polygon gP = GfaClip.getInstance().gfaToPolygon(g);
/*  894 */       Geometry intersection = outlookP.intersection(gP);
/*  895 */       if (((intersection instanceof Polygon)) || ((intersection instanceof MultiPolygon)))
/*      */       {
/*  897 */         String[] statesArray = nvl(g.getGfaStates()).split(" ");
/*      */         String[] arrayOfString2;
/*  898 */         int i = (arrayOfString2 = statesArray).length; for (str1 = 0; str1 < i; str1++) { s = arrayOfString2[str1];
/*  899 */           statesInAirmet.add(s.trim());
/*      */         }
/*  901 */         airmet = g;
/*  902 */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  921 */     String[] statesInOutlook = nvl(outlook.getGfaStates()).split(" ");
/*  922 */     double gfaOtlkgenRatio = GfaInfo.getGfaOtlkgenRatio();
/*  923 */     Polygon airmetP = GfaClip.getInstance().gfaToPolygon(airmet);
/*      */     String[] arrayOfString1;
/*  924 */     String str1 = (arrayOfString1 = statesInOutlook).length; for (String s = 0; s < str1; s++) { String stateStr = arrayOfString1[s];
/*  925 */       if (!stateStr.isEmpty()) {
/*  926 */         Geometry stateP = (Geometry)states.get(stateStr);
/*      */ 
/*  928 */         if (stateP != null)
/*      */         {
/*  930 */           if (statesInAirmet.contains(stateStr))
/*      */           {
/*  943 */             Geometry a = airmetP.intersection(outlookP);
/*  944 */             if (a != null)
/*      */             {
/*  946 */               Geometry o = outlookP.intersection(stateP);
/*      */ 
/*  948 */               Geometry i = a.intersection(stateP);
/*  949 */               if (i != null)
/*      */               {
/*  951 */                 double oArea = PgenUtil.getSphPolyArea(o);
/*  952 */                 double iArea = PgenUtil.getSphPolyArea(i);
/*      */ 
/*  954 */                 double ratio = 0.0D;
/*  955 */                 if (oArea - iArea > 0.0D) {
/*  956 */                   double sArea = PgenUtil.getSphPolyArea(stateP);
/*  957 */                   ratio = (oArea - iArea) / sArea;
/*      */                 }
/*      */ 
/*  960 */                 if (ratio >= gfaOtlkgenRatio) return "YES"; 
/*      */               }
/*      */             }
/*      */           }
/*  964 */           else { Geometry intersection = outlookP.intersection(stateP);
/*  965 */             double area = PgenUtil.getSphPolyArea(intersection);
/*  966 */             if (area > 3000.0D)
/*      */             {
/*  968 */               return "YES";
/*      */             } }
/*      */         }
/*      */       }
/*      */     }
/*  973 */     return "NO";
/*      */   }
/*      */ 
/*      */   private static String contgBydWording(ArrayList<Gfa> origSS, int[] olkSeqIn, Gfa smr)
/*      */   {
/* 1012 */     boolean ss06Exists = false;
/* 1013 */     for (Gfa g : origSS) {
/* 1014 */       int[] hm = Gfa.getHourMinInt(g.getGfaFcstHr());
/* 1015 */       if ((hm[0] == 6) && (hm[1] == 0) && (g.getAttribute("SNAPSHOT_TYPE") == SnapshotType.X)) {
/* 1016 */         ss06Exists = true;
/* 1017 */         break;
/*      */       }
/*      */     }
/* 1020 */     if (!ss06Exists) return "";
/*      */ 
/* 1023 */     int[] olkSeq = new int[olkSeqIn.length];
/* 1024 */     for (int ii = 0; ii < olkSeqIn.length; ii++) {
/* 1025 */       olkSeq[ii] = olkSeqIn[ii];
/*      */     }
/*      */ 
/* 1028 */     String wording = "";
/*      */     int lastSS;
/*      */     int lastSS;
/* 1030 */     if (olkSeq[2] >= 0) {
/* 1031 */       lastSS = 2;
/*      */     }
/*      */     else
/*      */     {
/*      */       int lastSS;
/* 1032 */       if (olkSeq[1] >= 0)
/* 1033 */         lastSS = 1;
/*      */       else
/* 1035 */         lastSS = 0;
/*      */     }
/* 1037 */     ArrayList originalSS = new ArrayList();
/* 1038 */     originalSS.addAll(origSS);
/*      */ 
/* 1040 */     boolean isX = ((Gfa)originalSS.get(olkSeq[lastSS])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.X;
/* 1041 */     ArrayList otlkList = (ArrayList)((Gfa)originalSS.get(olkSeq[lastSS])).getAttribute("OTLK_LIST", ArrayList.class);
/*      */ 
/* 1044 */     if ((lastSS == 0) && (otlkList != null)) {
/* 1045 */       for (Gfa g : otlkList) {
/* 1046 */         if ((g.getAttribute("SNAPSHOT_TYPE") == SnapshotType.O) && (!"6".equals(g.getGfaFcstHr()))) {
/* 1047 */           isX = false;
/* 1048 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1057 */     boolean adjEmbedOtlk = false;
/* 1058 */     ArrayList embedOtlks = (ArrayList)((Gfa)originalSS.get(olkSeq[lastSS])).getAttribute("OUTLOOKS", ArrayList.class);
/* 1059 */     if ((embedOtlks != null) && (embedOtlks.size() > 0))
/*      */     {
/* 1065 */       Gfa otlkInSameRegion = null;
/* 1066 */       for (Gfa gg : embedOtlks) {
/* 1067 */         if (gg.getAttribute("FA_REGION").equals(smr.getAttribute("FA_REGION"))) {
/* 1068 */           otlkInSameRegion = gg;
/* 1069 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1074 */       GfaFormat.FcstHrListPair p = (GfaFormat.FcstHrListPair)((Gfa)embedOtlks.get(0)).getAttribute("PAIR", GfaFormat.FcstHrListPair.class);
/* 1075 */       for (Gfa gg : p.getOriginal()) {
/* 1076 */         if (!originalSS.contains(gg)) {
/* 1077 */           Gfa temp = gg.copy();
/* 1078 */           temp.addAttribute("SNAPSHOT_TYPE", SnapshotType.O);
/*      */ 
/* 1080 */           if (otlkInSameRegion != null) {
/* 1081 */             Polygon otlkPoly = GfaClip.getInstance().gfaToPolygonInGrid(otlkInSameRegion);
/* 1082 */             assignSnapshotType(otlkPoly, temp);
/*      */           }
/*      */ 
/* 1085 */           originalSS.add(temp);
/*      */         }
/*      */       }
/*      */ 
/* 1089 */       adjEmbedOtlk = true;
/*      */     }
/*      */ 
/* 1092 */     if (adjEmbedOtlk) {
/* 1093 */       olkSeq = findOtlkSeq(originalSS);
/* 1094 */       if (olkSeq[2] >= 0)
/* 1095 */         lastSS = 2;
/* 1096 */       else if (olkSeq[1] >= 0)
/* 1097 */         lastSS = 1;
/*      */       else {
/* 1099 */         lastSS = 0;
/*      */       }
/* 1101 */       isX = ((Gfa)originalSS.get(olkSeq[lastSS])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.X;
/*      */     }
/*      */ 
/* 1107 */     if (isX)
/*      */     {
/* 1109 */       String hourMinStr = ((Gfa)originalSS.get(olkSeq[lastSS])).getGfaFcstHr();
/* 1110 */       int[] hm = Gfa.getHourMinInt(hourMinStr);
/* 1111 */       String lastXinOtlk = "";
/* 1112 */       if ((otlkList != null) && (!otlkList.isEmpty()) && 
/* 1113 */         (((Gfa)otlkList.get(otlkList.size() - 1)).getAttribute("SNAPSHOT_TYPE") == SnapshotType.X)) {
/* 1114 */         lastXinOtlk = ((Gfa)otlkList.get(otlkList.size() - 1)).getGfaFcstHr();
/*      */       }
/*      */ 
/* 1117 */       if ((hm[0] == 12) || ("12".equals(lastXinOtlk)))
/* 1118 */         wording = "CONDS CONTG BYD +06Z THRU +12Z";
/* 1119 */       else if ((hm[0] == 9) || ("9".equals(lastXinOtlk)))
/* 1120 */         wording = "CONDS CONTG BYD +06Z ENDG +09-+12Z";
/* 1121 */       else if (hm[0] == 6)
/* 1122 */         wording = "CONDS CONTG BYD +06Z ENDG +06-+09Z";
/*      */     }
/*      */     else
/*      */     {
/* 1126 */       int firstO = lastSS;
/* 1127 */       int firstX = -1;
/*      */ 
/* 1129 */       for (int ii = lastSS; ii >= 0; ii--) {
/* 1130 */         if ((olkSeq[ii] >= 0) && 
/* 1131 */           (((Gfa)originalSS.get(olkSeq[ii])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.X)) {
/* 1132 */           firstX = ii;
/* 1133 */           break;
/*      */         }
/*      */       }
/*      */ 
/* 1137 */       if (firstX >= 0) {
/* 1138 */         for (int ii = firstX + 1; ii <= lastSS; ii++) {
/* 1139 */           if ((olkSeq[ii] >= 0) && 
/* 1140 */             (((Gfa)originalSS.get(olkSeq[ii])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.O)) {
/* 1141 */             firstO = ii;
/* 1142 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1147 */       String hourMinStr = ((Gfa)originalSS.get(olkSeq[firstO])).getGfaFcstHr();
/* 1148 */       int[] hm = Gfa.getHourMinInt(hourMinStr);
/* 1149 */       String firstOinOtlk = "";
/* 1150 */       if ((otlkList != null) && (!otlkList.isEmpty())) {
/* 1151 */         for (Gfa el : otlkList) {
/* 1152 */           if (el.getAttribute("SNAPSHOT_TYPE") == SnapshotType.O) {
/* 1153 */             firstOinOtlk = el.getGfaFcstHr();
/* 1154 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1159 */       if ((hm[0] == 12) || ("12".equals(firstOinOtlk)))
/* 1160 */         wording = "CONDS CONTG BYD +06Z ENDG BY +12Z";
/* 1161 */       else if ((hm[0] == 9) || ("9".equals(firstOinOtlk)))
/* 1162 */         wording = "CONDS CONTG BYD +06Z ENDG BY +09Z";
/* 1163 */       else if (hm[0] == 6) {
/* 1164 */         wording = "CONDS CONTG BYD +03Z ENDG BY +06Z";
/*      */       }
/*      */     }
/*      */ 
/* 1168 */     return wording;
/*      */   }
/*      */ 
/*      */   private static String otlkDvlpgWording(ArrayList<Gfa> originalSS, int[] olkSeq)
/*      */   {
/* 1197 */     int firstSS = -1;
/* 1198 */     for (int ii = 0; ii < 3; ii++) {
/* 1199 */       if (olkSeq[ii] >= 0) {
/* 1200 */         firstSS = ii;
/* 1201 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1205 */     String hourMinStr = ((Gfa)originalSS.get(olkSeq[firstSS])).getGfaFcstHr();
/* 1206 */     int[] hm = Gfa.getHourMinInt(hourMinStr);
/*      */ 
/* 1208 */     String wording = "";
/*      */ 
/* 1210 */     if (((Gfa)originalSS.get(olkSeq[firstSS])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.X)
/*      */     {
/* 1212 */       if (firstSS != olkSeq[0])
/*      */       {
/* 1214 */         if (hm[0] == 6)
/* 1215 */           wording = "CONDS DVLPG +03-+06Z";
/* 1216 */         else if (hm[0] == 9)
/* 1217 */           wording = "CONDS DVLPG +06-+09Z";
/* 1218 */         else if (hm[0] == 12)
/* 1219 */           wording = "CONDS DVLPG +09-+12Z";
/*      */       }
/*      */     } else {
/* 1222 */       int lastO = firstSS;
/* 1223 */       for (int ii = 0; ii < 2; ii++) {
/* 1224 */         if ((lastO == olkSeq[ii]) && 
/* 1225 */           (olkSeq[(ii + 1)] >= 0) && 
/* 1226 */           (((Gfa)originalSS.get(olkSeq[(ii + 1)])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.O)) {
/* 1227 */           lastO = olkSeq[(ii + 1)];
/*      */         }
/*      */       }
/* 1230 */       hourMinStr = ((Gfa)originalSS.get(olkSeq[lastO])).getGfaFcstHr();
/* 1231 */       hm = Gfa.getHourMinInt(hourMinStr);
/*      */ 
/* 1233 */       wording = "CONDS DVLPG AFT +" + PgenCycleTool.pad(hm[0]) + "Z";
/*      */     }
/* 1235 */     return wording;
/*      */   }
/*      */ 
/*      */   private static String otlkEndgWording(ArrayList<Gfa> originalSS, int[] olkSeq)
/*      */   {
/*      */     int lastSS;
/*      */     int lastSS;
/* 1269 */     if (olkSeq[2] >= 0) {
/* 1270 */       lastSS = 2;
/*      */     }
/*      */     else
/*      */     {
/*      */       int lastSS;
/* 1271 */       if (olkSeq[1] >= 0)
/* 1272 */         lastSS = 1;
/*      */       else
/* 1274 */         lastSS = 0;
/*      */     }
/* 1276 */     String hourMinStr = ((Gfa)originalSS.get(olkSeq[lastSS])).getGfaFcstHr();
/* 1277 */     int[] hm = Gfa.getHourMinInt(hourMinStr);
/*      */ 
/* 1279 */     String wording = "";
/*      */ 
/* 1281 */     if (((Gfa)originalSS.get(olkSeq[lastSS])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.X)
/*      */     {
/* 1283 */       if (hm[0] == 12)
/* 1284 */         wording = "CONDS CONTG THRU +12Z";
/* 1285 */       else if (hm[0] == 9)
/* 1286 */         wording = "CONDS ENDG +09-+12Z";
/* 1287 */       else hm[0];
/*      */     }
/*      */     else
/*      */     {
/* 1291 */       int firstO = lastSS;
/* 1292 */       int firstX = -1;
/*      */ 
/* 1294 */       for (int ii = lastSS; ii >= 0; ii--) {
/* 1295 */         if ((olkSeq[ii] >= 0) && 
/* 1296 */           (((Gfa)originalSS.get(olkSeq[ii])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.X)) {
/* 1297 */           firstX = ii;
/* 1298 */           break;
/*      */         }
/*      */       }
/*      */ 
/* 1302 */       if (firstX >= 0) {
/* 1303 */         for (int ii = firstX + 1; ii <= lastSS; ii++) {
/* 1304 */           if ((olkSeq[ii] >= 0) && 
/* 1305 */             (((Gfa)originalSS.get(olkSeq[ii])).getAttribute("SNAPSHOT_TYPE") == SnapshotType.O)) {
/* 1306 */             firstO = ii;
/* 1307 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1312 */       hourMinStr = ((Gfa)originalSS.get(olkSeq[firstO])).getGfaFcstHr();
/* 1313 */       hm = Gfa.getHourMinInt(hourMinStr);
/*      */ 
/* 1315 */       if (hm[0] == 12)
/* 1316 */         wording = "CONDS ENDG BY +12Z";
/* 1317 */       else hm[0];
/*      */ 
/*      */     }
/*      */ 
/* 1322 */     return wording;
/*      */   }
/*      */ 
/*      */   private static void parseWording(Gfa smear, GfaWording wording)
/*      */   {
/* 1333 */     String beginWording = "";
/* 1334 */     String endWording = "";
/*      */ 
/* 1336 */     if (smear.isAirmet())
/*      */     {
/* 1339 */       beginWording = wording.fromCondsDvlpg;
/*      */ 
/* 1341 */       if (!wording.condsContg.isEmpty()) {
/* 1342 */         if (beginWording.isEmpty())
/* 1343 */           beginWording = beginWording + wording.condsContg;
/*      */         else {
/* 1345 */           beginWording = beginWording + ". " + wording.condsContg;
/*      */         }
/*      */       }
/* 1348 */       endWording = wording.fromCondsEndg;
/*      */     }
/*      */     else
/*      */     {
/* 1352 */       beginWording = wording.otlkCondsDvlpg;
/*      */ 
/* 1354 */       if (!wording.condsContg.isEmpty()) {
/* 1355 */         if (beginWording.isEmpty())
/* 1356 */           beginWording = beginWording + wording.condsContg;
/*      */         else {
/* 1358 */           beginWording = beginWording + ". " + wording.condsContg;
/*      */         }
/*      */       }
/*      */ 
/* 1362 */       if (!wording.otlkCondsEndg.isEmpty()) {
/* 1363 */         endWording = wording.otlkCondsEndg;
/*      */       }
/*      */     }
/*      */ 
/* 1367 */     smear.setGfaBeginning(beginWording);
/* 1368 */     smear.setGfaEnding(endWording);
/*      */ 
/* 1370 */     replacePlusWithCycle(smear);
/*      */   }
/*      */ 
/*      */   public static void replacePlusWithCycle(Gfa gfa)
/*      */   {
/* 1381 */     int cycle = gfa.getGfaCycleHour();
/*      */ 
/* 1383 */     String b = gfa.getGfaBeginning();
/* 1384 */     b = replacePlusWithCycle(b, cycle);
/* 1385 */     gfa.setGfaBeginning(b);
/*      */ 
/* 1387 */     String e = gfa.getGfaEnding();
/* 1388 */     e = replacePlusWithCycle(e, cycle);
/* 1389 */     gfa.setGfaEnding(e);
/*      */   }
/*      */ 
/*      */   public static String replacePlusWithCycle(String b, int cycle)
/*      */   {
/*      */     int i;
/* 1402 */     while ((i = b.indexOf("+")) > -1)
/*      */     {
/*      */       int i;
/* 1403 */       String toReplace = b.substring(i, i + 3);
/* 1404 */       String hour = toReplace.substring(1);
/* 1405 */       int h = Integer.parseInt(hour);
/* 1406 */       hour = PgenCycleTool.pad((h + cycle) % 24);
/* 1407 */       b = b.replace(toReplace, hour);
/*      */     }
/* 1409 */     return b;
/*      */   }
/*      */ 
/*      */   private static void clearAttributes(ArrayList<Gfa> originalSS)
/*      */   {
/* 1418 */     for (Gfa ss : originalSS) {
/* 1419 */       ss.removeAttribute("SNAPSHOT_TYPE");
/* 1420 */       ss.removeAttribute("DVLPG_HR");
/* 1421 */       ss.removeAttribute("ENDG_HR");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String nvl(String value) {
/* 1426 */     return value == null ? "" : value;
/*      */   }
/*      */ 
/*      */   private static void findGfaAreaInGrid(Gfa current, ArrayList<Gfa> snapShots)
/*      */   {
/* 1456 */     HashMap areaBnds = GfaClip.getInstance().getFaAreaBoundsInGrid();
/* 1457 */     HashMap interSizes = new HashMap();
/* 1458 */     HashMap interWithSS = new HashMap();
/*      */ 
/* 1460 */     Polygon clipPoly = GfaClip.getInstance().gfaToPolygonInGrid(current);
/*      */     double ss;
/*      */     Gfa gfa;
/* 1462 */     for (String areaName : areaBnds.keySet())
/*      */     {
/* 1464 */       Geometry g = (Geometry)areaBnds.get(areaName);
/*      */ 
/* 1466 */       if (clipPoly.intersects(g))
/*      */       {
/* 1468 */         Geometry interAA = clipPoly.intersection(g);
/*      */ 
/* 1470 */         ss = PgenUtil.getSphPolyAreaInGrid(interAA);
/*      */ 
/* 1472 */         interSizes.put(areaName, Double.valueOf(ss));
/*      */ 
/* 1474 */         boolean interSSBig = false;
/* 1475 */         if ((snapShots == null) || (snapShots.size() <= 0)) {
/* 1476 */           interSSBig = true;
/*      */         }
/*      */         else {
/* 1479 */           for (Iterator localIterator2 = snapShots.iterator(); localIterator2.hasNext(); ) { gfa = (Gfa)localIterator2.next();
/*      */ 
/* 1481 */             Polygon p = GfaClip.getInstance().gfaToPolygonInGrid(gfa);
/*      */ 
/* 1485 */             double narea = 0.0D;
/* 1486 */             for (int nn = 0; nn < interAA.getNumGeometries(); nn++) {
/* 1487 */               if (p.intersects(interAA.getGeometryN(nn))) {
/* 1488 */                 Geometry in_1 = p.intersection(interAA.getGeometryN(nn));
/* 1489 */                 narea += PgenUtil.getSphPolyAreaInGrid(in_1);
/*      */               }
/*      */             }
/*      */ 
/* 1493 */             if (narea >= 3000.0D) {
/* 1494 */               interSSBig = true;
/* 1495 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1500 */         interWithSS.put(areaName, Boolean.valueOf(interSSBig));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1505 */     String primaryArea = null;
/* 1506 */     double biggest = 0.0D;
/* 1507 */     for (String areaName : interSizes.keySet()) {
/* 1508 */       if (((Double)interSizes.get(areaName)).doubleValue() > biggest) {
/* 1509 */         primaryArea = new String(areaName);
/* 1510 */         biggest = ((Double)interSizes.get(areaName)).doubleValue();
/*      */       }
/*      */     }
/*      */ 
/* 1514 */     String secondArea = null;
/* 1515 */     double second = 0.0D;
/* 1516 */     for (String areaName : interSizes.keySet()) {
/* 1517 */       if ((!areaName.equals(primaryArea)) && 
/* 1518 */         (((Double)interSizes.get(areaName)).doubleValue() > second))
/*      */       {
/* 1520 */         secondArea = areaName;
/* 1521 */         second = ((Double)interSizes.get(areaName)).doubleValue();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1526 */     StringBuilder gfaArea = new StringBuilder();
/* 1527 */     if ((primaryArea != null) && (((Boolean)interWithSS.get(primaryArea)).booleanValue()))
/*      */     {
/* 1529 */       gfaArea.append(primaryArea);
/*      */ 
/* 1531 */       if ((secondArea != null) && (((Double)interSizes.get(secondArea)).doubleValue() >= 3000.0D) && 
/* 1532 */         (((Boolean)interWithSS.get(secondArea)).booleanValue()))
/*      */       {
/* 1534 */         gfaArea.append("-" + secondArea);
/*      */       }
/*      */     }
/*      */ 
/* 1538 */     current.setGfaArea(gfaArea.toString());
/*      */   }
/*      */ 
/*      */   static void assignAirmetTag(Gfa smear)
/*      */   {
/* 1552 */     boolean addAirmetTag = true;
/* 1553 */     String haz = smear.getGfaHazard();
/* 1554 */     if ((addAirmetTag) && (!"FZLVL".equals(haz)) && (!"M_FZLVL".equals(haz))) {
/* 1555 */       String prefix = "";
/* 1556 */       if (haz.equals("TURB-HI")) {
/* 1557 */         prefix = "H";
/*      */       }
/* 1559 */       else if (haz.equals("TURB-LO")) {
/* 1560 */         prefix = "L";
/*      */       }
/*      */ 
/* 1563 */       String airmetTag = new String(prefix + smear.getGfaTag() + smear.getGfaDesk());
/* 1564 */       smear.setGfaValue("AIRMET_TAG", airmetTag);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void reorderStateList(Gfa gg)
/*      */   {
/* 1580 */     String area = gg.getGfaArea();
/*      */ 
/* 1582 */     if ((area == null) || (!isValidGfaArea(area)) || (gg.getGfaStates() == null) || 
/* 1583 */       (gg.getGfaStates().trim().length() == 0)) {
/* 1584 */       return;
/*      */     }
/*      */ 
/* 1587 */     ArrayList oldStates = new ArrayList();
/* 1588 */     ArrayList oldStatesNoWater = new ArrayList();
/* 1589 */     for (String ss : gg.getGfaStates().split(" ")) {
/* 1590 */       oldStates.add(ss);
/* 1591 */       if (ss.length() == 2) {
/* 1592 */         oldStatesNoWater.add(ss);
/*      */       }
/*      */     }
/* 1595 */     String[] s = area.split("-");
/*      */ 
/* 1597 */     ArrayList statesInArea1 = (ArrayList)GfaInfo.getStateOrderByArea().get(s[0]);
/* 1598 */     ArrayList statesInArea2 = null;
/* 1599 */     if (s.length > 1) {
/* 1600 */       statesInArea2 = (ArrayList)GfaInfo.getStateOrderByArea().get(s[1]);
/*      */     }
/*      */ 
/* 1604 */     StringBuilder newStates = new StringBuilder();
/* 1605 */     for (String st : statesInArea1) {
/* 1606 */       if ((st.length() == 2) && (oldStatesNoWater.contains(st))) {
/* 1607 */         newStates.append(st);
/* 1608 */         newStates.append(" ");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1613 */     if (statesInArea2 != null) {
/* 1614 */       for (String st : statesInArea2) {
/* 1615 */         if ((st.length() == 2) && (oldStatesNoWater.contains(st))) {
/* 1616 */           newStates.append(st);
/* 1617 */           newStates.append(" ");
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1623 */     for (String st : oldStates) {
/* 1624 */       if (st.length() > 2) {
/* 1625 */         newStates.append(st);
/* 1626 */         newStates.append(" ");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1631 */     gg.setGfaStates(newStates.toString().trim());
/*      */   }
/*      */ 
/*      */   private static boolean isValidGfaArea(String area)
/*      */   {
/* 1639 */     boolean validArea = false;
/*      */ 
/* 1641 */     if ((area.equalsIgnoreCase("BOS")) || (area.equalsIgnoreCase("MIA")) || 
/* 1642 */       (area.equalsIgnoreCase("CHI")) || (area.equalsIgnoreCase("DFW")) || 
/* 1643 */       (area.equalsIgnoreCase("SLC")) || (area.equalsIgnoreCase("SFO")))
/*      */     {
/* 1645 */       validArea = true;
/*      */     }
/*      */ 
/* 1648 */     return validArea;
/*      */   }
/*      */ 
/*      */   public static enum SnapshotType
/*      */   {
/*   78 */     X, 
/*      */ 
/*   80 */     O;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaRules
 * JD-Core Version:    0.6.2
 */