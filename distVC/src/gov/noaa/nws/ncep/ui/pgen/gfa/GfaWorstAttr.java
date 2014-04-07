/*     */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.dom4j.Node;
/*     */ 
/*     */ public class GfaWorstAttr
/*     */ {
/*  51 */   private static final int[] onTimeCycles = { 3, 9, 15, 21, 2, 8, 14, 20 };
/*  52 */   private static final int[] offTimeCycles = { 0, 6, 12, 18, 5, 11, 17, 23 };
/*     */ 
/*     */   public static void apply(Gfa smear, ArrayList<Gfa> clipped, ArrayList<Gfa> snapshots, ArrayList<Gfa> canceled)
/*     */   {
/*  69 */     if ((smear.getGfaHazard().equals("FZLVL")) || 
/*  70 */       (snapshots.size() + canceled.size() == 0)) {
/*  71 */       return;
/*     */     }
/*     */ 
/*  76 */     int baseTime = PgenCycleTool.getCycleHour();
/*  77 */     boolean onTime = false;
/*  78 */     for (int ii = 0; ii < onTimeCycles.length; ii++)
/*  79 */       if (baseTime == onTimeCycles[ii]) {
/*  80 */         onTime = true;
/*  81 */         break;
/*     */       }
/*     */     int endTime;
/*     */     int startTime;
/*     */     int endTime;
/*  92 */     if (smear.isOutlook())
/*     */     {
/*     */       int endTime;
/*  93 */       if (onTime) {
/*  94 */         int startTime = 360;
/*  95 */         endTime = 720;
/*     */       }
/*     */       else {
/*  98 */         int startTime = 180;
/*  99 */         endTime = 540;
/*     */       }
/*     */     }
/*     */     else {
/* 103 */       startTime = 0;
/*     */       int endTime;
/* 104 */       if (onTime) {
/* 105 */         endTime = 360;
/*     */       }
/*     */       else {
/* 108 */         endTime = 180;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 115 */     for (Gfa gfa : clipped)
/*     */     {
/* 117 */       ArrayList ssGrp = getSnapshotGroup(snapshots, gfa);
/*     */ 
/* 120 */       int start_indx = -1;
/* 121 */       int end_indx = -1;
/* 122 */       int nhour = ssGrp.size();
/*     */ 
/* 124 */       for (int jj = 0; jj < nhour; jj++)
/*     */       {
/* 126 */         int fcst_min = ((SnapshotGroup)ssGrp.get(jj)).hour * 60 + ((SnapshotGroup)ssGrp.get(jj)).minute;
/*     */ 
/* 128 */         if ((start_indx < 0) && (fcst_min >= startTime)) {
/* 129 */           start_indx = jj;
/* 130 */           end_indx = jj;
/*     */         }
/*     */ 
/* 133 */         if (fcst_min <= endTime) {
/* 134 */           end_indx = jj;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 145 */       for (int jj = start_indx; jj <= end_indx; jj++) {
/* 146 */         if (((SnapshotGroup)ssGrp.get(jj)).type == GfaRules.SnapshotType.X)
/*     */         {
/*     */           break;
/*     */         }
/* 150 */         if ((jj + 1 < nhour) && (((SnapshotGroup)ssGrp.get(jj + 1)).type == GfaRules.SnapshotType.X))
/*     */         {
/*     */           break;
/*     */         }
/* 154 */         if (start_indx + 1 < nhour) {
/* 155 */           start_indx++;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 161 */       for (int jj = end_indx; jj >= start_indx; jj--) {
/* 162 */         if (((SnapshotGroup)ssGrp.get(jj)).type == GfaRules.SnapshotType.X)
/*     */         {
/*     */           break;
/*     */         }
/* 166 */         if ((jj - 1 >= 0) && (((SnapshotGroup)ssGrp.get(jj - 1)).type == GfaRules.SnapshotType.X))
/*     */         {
/*     */           break;
/*     */         }
/* 170 */         end_indx--;
/*     */       }
/*     */ 
/* 176 */       if (start_indx >= 0)
/*     */       {
/* 181 */         ArrayList qualifiedSS = new ArrayList();
/*     */         int kk;
/* 182 */         for (int jj = start_indx; jj <= end_indx; jj++)
/*     */         {
/* 184 */           int nsnap = ((SnapshotGroup)ssGrp.get(jj)).getSnapshotInfo().size();
/* 185 */           for (kk = 0; kk < nsnap; kk++)
/*     */           {
/* 187 */             if ((((SnapshotGroup)ssGrp.get(jj)).type != GfaRules.SnapshotType.X) || 
/* 188 */               (((SnapshotInfo)((SnapshotGroup)ssGrp.get(jj)).getSnapshotInfo().get(kk)).type != GfaRules.SnapshotType.O))
/*     */             {
/* 192 */               qualifiedSS.add((Gfa)snapshots.get(((SnapshotGroup)ssGrp.get(jj)).start + kk));
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 198 */         ArrayList worstIssueSS = new ArrayList(qualifiedSS);
/* 199 */         if ((canceled != null) && (canceled.size() > 0)) {
/* 200 */           for (Gfa cgfa : canceled)
/*     */           {
/* 202 */             if (!worstIssueSS.contains(cgfa)) {
/* 203 */               worstIssueSS.add(cgfa);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 208 */         String worstIssue = getGfaWorstIssueType(worstIssueSS);
/* 209 */         if (worstIssue.length() > 0) {
/* 210 */           gfa.setGfaIssueType(worstIssue);
/*     */         }
/*     */ 
/* 217 */         HashMap topBotValues = GfaFormat.findGfaTopBots(qualifiedSS);
/* 218 */         HashMap existingValues = gfa.getGfaValues();
/* 219 */         for (String skey : topBotValues.keySet()) {
/* 220 */           String value = (String)topBotValues.get(skey);
/* 221 */           if ((value != null) && (value.length() > 0)) {
/* 222 */             existingValues.put(skey, value);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 227 */         String combineTypes = GfaFormat.findGfaSubTypes(qualifiedSS);
/* 228 */         if ((combineTypes != null) && (combineTypes.length() > 0)) {
/* 229 */           gfa.setGfaType(combineTypes);
/*     */         }
/*     */ 
/* 233 */         String worstFreq = getGfaWorstFrequency(qualifiedSS);
/* 234 */         if ((worstFreq != null) && (worstFreq.length() > 0))
/* 235 */           gfa.setGfaValue("Frequency", worstFreq);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static ArrayList<SnapshotGroup> getSnapshotGroup(ArrayList<Gfa> snapshots, Gfa clipped)
/*     */   {
/* 339 */     HashMap ssgrpMap = new HashMap();
/* 340 */     GfaWorstAttr wattr = new GfaWorstAttr();
/*     */ 
/* 342 */     int ii = 0;
/* 343 */     for (Gfa gfa : snapshots)
/*     */     {
/* 345 */       SnapshotInfo ssinfo = wattr.createSnapshotInfo(gfa, clipped);
/* 346 */       SnapshotGroup sgrp = (SnapshotGroup)ssgrpMap.get(gfa.getGfaFcstHr());
/*     */ 
/* 348 */       if (sgrp != null)
/*     */       {
/* 350 */         sgrp.getSnapshotInfo().add(ssinfo);
/*     */ 
/* 352 */         if (ssinfo.type == GfaRules.SnapshotType.X) {
/* 353 */           sgrp.setType(ssinfo.type);
/* 354 */           sgrp.setDvlpg_hr(ssinfo.dvlpg_hr);
/* 355 */           sgrp.setEndg_hr(ssinfo.endg_hr);
/*     */         }
/*     */       }
/*     */       else {
/* 359 */         sgrp = wattr.createSnapshotGroup(ssinfo, ii);
/* 360 */         ssgrpMap.put(gfa.getGfaFcstHr(), sgrp);
/*     */       }
/*     */ 
/* 363 */       ii++;
/*     */     }
/*     */ 
/* 367 */     return new ArrayList(ssgrpMap.values());
/*     */   }
/*     */ 
/*     */   private SnapshotInfo createSnapshotInfo(Gfa snapshot, Gfa clipped)
/*     */   {
/* 385 */     Polygon ssp = GfaClip.getInstance().gfaToPolygonInGrid(snapshot);
/* 386 */     Polygon clip = GfaClip.getInstance().gfaToPolygonInGrid(clipped);
/*     */ 
/* 388 */     GfaRules.SnapshotType type = GfaRules.SnapshotType.O;
/* 389 */     if (ssp.intersects(clip)) {
/* 390 */       double area = PgenUtil.getSphPolyAreaInGrid(ssp.intersection(clip));
/* 391 */       if (area > 3000.0D) {
/* 392 */         type = GfaRules.SnapshotType.X;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 404 */     int[] ftime = Gfa.getHourMinInt(snapshot.getForecastHours());
/* 405 */     double hmD = ftime[0] + ftime[1] / 60.0D;
/*     */     int endg_hr;
/*     */     int dvlpg_hr;
/*     */     int endg_hr;
/* 408 */     if (type == GfaRules.SnapshotType.X) {
/* 409 */       int dvlpg_hr = (int)Math.ceil(hmD);
/* 410 */       endg_hr = (int)Math.floor(hmD);
/*     */     } else {
/* 412 */       dvlpg_hr = (int)Math.floor(hmD);
/* 413 */       endg_hr = (int)Math.ceil(hmD);
/*     */     }
/*     */ 
/* 416 */     return new SnapshotInfo(type, ftime[0], ftime[1], dvlpg_hr, endg_hr);
/*     */   }
/*     */ 
/*     */   private SnapshotGroup createSnapshotGroup(SnapshotInfo ssinfo, int start)
/*     */   {
/* 428 */     ArrayList sin = new ArrayList();
/* 429 */     sin.add(ssinfo);
/*     */ 
/* 431 */     return new SnapshotGroup(ssinfo.type, ssinfo.hour, ssinfo.minute, 
/* 432 */       ssinfo.dvlpg_hr, ssinfo.endg_hr, start, sin);
/*     */   }
/*     */ 
/*     */   public static String getGfaWorstIssueType(ArrayList<Gfa> ssList)
/*     */   {
/* 445 */     String worstIssue = "";
/*     */ 
/* 447 */     if ((ssList != null) && (ssList.size() > 0)) {
/* 448 */       ArrayList ssIssueTypes = new ArrayList();
/* 449 */       for (Gfa ss : ssList) {
/* 450 */         ssIssueTypes.add(ss.getGfaIssueType());
/*     */       }
/*     */ 
/* 453 */       worstIssue = new String(gfaWorstIssueType(ssIssueTypes));
/*     */     }
/*     */ 
/* 456 */     return worstIssue;
/*     */   }
/*     */ 
/*     */   private static String gfaWorstIssueType(ArrayList<String> issueTypes)
/*     */   {
/* 469 */     String worstIssue = "";
/* 470 */     int nEl = issueTypes.size();
/*     */ 
/* 473 */     if (nEl <= 0) {
/* 474 */       return worstIssue;
/*     */     }
/*     */ 
/* 478 */     worstIssue = new String("NRML");
/*     */ 
/* 481 */     for (int ii = 0; ii < nEl; ii++) {
/* 482 */       if (((String)issueTypes.get(ii)).equals("COR")) {
/* 483 */         worstIssue = new String("COR");
/* 484 */         return worstIssue;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 489 */     for (int ii = 0; ii < nEl; ii++) {
/* 490 */       if (((String)issueTypes.get(ii)).equals("AMD")) {
/* 491 */         worstIssue = new String("AMD");
/* 492 */         return worstIssue;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 500 */     boolean allNew = true;
/* 501 */     boolean someNew = false;
/*     */ 
/* 503 */     for (int ii = 0; ii < nEl; ii++) {
/* 504 */       boolean thisIsNew = ((String)issueTypes.get(ii)).equals("NEW");
/* 505 */       allNew = (allNew) && (thisIsNew);
/* 506 */       if (thisIsNew) someNew = true;
/*     */     }
/*     */ 
/* 509 */     someNew = (someNew) && (!allNew);
/*     */ 
/* 511 */     if (allNew) {
/* 512 */       worstIssue = new String("NEW");
/* 513 */       return worstIssue;
/*     */     }
/* 515 */     if (someNew) {
/* 516 */       worstIssue = new String("AMD");
/* 517 */       return worstIssue;
/*     */     }
/*     */ 
/* 524 */     boolean allCan = true;
/* 525 */     boolean someCan = false;
/*     */ 
/* 527 */     for (int ii = 0; ii < nEl; ii++) {
/* 528 */       boolean thisIsCan = ((String)issueTypes.get(ii)).equals("CAN");
/* 529 */       allCan = (allCan) && (thisIsCan);
/* 530 */       if (thisIsCan) someCan = true;
/*     */     }
/*     */ 
/* 533 */     someCan = (someCan) && (!allCan);
/*     */ 
/* 535 */     if (allCan) {
/* 536 */       worstIssue = new String("CAN");
/* 537 */       return worstIssue;
/*     */     }
/*     */ 
/* 540 */     if (someCan) {
/* 541 */       worstIssue = new String("AMD");
/* 542 */       return worstIssue;
/*     */     }
/*     */ 
/* 547 */     return worstIssue;
/*     */   }
/*     */ 
/*     */   private static String getGfaWorstFrequency(ArrayList<Gfa> ssList)
/*     */   {
/* 562 */     String worstFreq = "";
/*     */ 
/* 564 */     if ((ssList != null) && (ssList.size() > 0))
/*     */     {
/* 566 */       String hazard = ((Gfa)ssList.get(0)).getGfaHazard();
/*     */ 
/* 568 */       String freq = ((Gfa)ssList.get(0)).getGfaValue("Frequency");
/*     */ 
/* 570 */       if ((freq != null) && (freq.length() > 0))
/*     */       {
/* 572 */         String xPath = "/root/hazard[@name='" + hazard + "']/*";
/* 573 */         List nodes = GfaInfo.selectNodes(xPath);
/*     */ 
/* 575 */         ArrayList frequencyInfo = new ArrayList();
/* 576 */         for (Node node : nodes) {
/* 577 */           if ("dropdown".equalsIgnoreCase(node.getName())) {
/* 578 */             String lblStr = node.valueOf("@label");
/* 579 */             if ((lblStr != null) && (lblStr.equalsIgnoreCase("Frequency"))) {
/* 580 */               List list = node.selectNodes("value");
/* 581 */               for (Node n : list) {
/* 582 */                 frequencyInfo.add(n.getText());
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 588 */         worstFreq = new String(freq);
/*     */ 
/* 590 */         for (int ii = 1; ii < ssList.size(); ii++) {
/* 591 */           worstFreq = gfaWorstFrequency(frequencyInfo, worstFreq, 
/* 592 */             ((Gfa)ssList.get(ii)).getGfaValue("Frequency"));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 597 */     return worstFreq;
/*     */   }
/*     */ 
/*     */   private static String gfaWorstFrequency(ArrayList<String> freqs, String value1, String value2)
/*     */   {
/* 620 */     String worstFreq = new String(value1);
/*     */ 
/* 629 */     if (value2.equalsIgnoreCase("No Qualifier")) {
/* 630 */       return worstFreq;
/*     */     }
/*     */ 
/* 633 */     if (value1.equalsIgnoreCase("No Qualifier")) {
/* 634 */       worstFreq = new String(value2);
/* 635 */       return worstFreq;
/*     */     }
/*     */ 
/* 638 */     if ((freqs != null) && (freqs.size() > 0))
/*     */     {
/* 640 */       for (String fq : freqs) {
/* 641 */         boolean findValue1 = value1.equalsIgnoreCase(fq);
/* 642 */         boolean findValue2 = value2.equalsIgnoreCase(fq);
/*     */ 
/* 644 */         if ((findValue1) || (findValue2)) {
/* 645 */           if (!findValue1) break;
/* 646 */           worstFreq = new String(value2);
/*     */ 
/* 648 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 653 */     return worstFreq;
/*     */   }
/*     */ 
/*     */   class SnapshotGroup
/*     */   {
/*     */     GfaRules.SnapshotType type;
/*     */     int hour;
/*     */     int minute;
/*     */     int dvlpg_hr;
/*     */     int endg_hr;
/*     */     int start;
/*     */     ArrayList<GfaWorstAttr.SnapshotInfo> snapshotInfo;
/*     */ 
/*     */     public SnapshotGroup(int type, int hour, int minute, int dvlpg_hr, int endg_gr, ArrayList<GfaWorstAttr.SnapshotInfo> start)
/*     */     {
/* 285 */       this.type = type;
/* 286 */       this.hour = hour;
/* 287 */       this.minute = minute;
/* 288 */       this.dvlpg_hr = dvlpg_hr;
/* 289 */       this.endg_hr = endg_gr;
/* 290 */       this.start = start;
/*     */ 
/* 292 */       this.snapshotInfo = new ArrayList();
/* 293 */       if (snapshotInfo != null)
/* 294 */         this.snapshotInfo.addAll(snapshotInfo);
/*     */     }
/*     */ 
/*     */     public ArrayList<GfaWorstAttr.SnapshotInfo> getSnapshotInfo()
/*     */     {
/* 303 */       return this.snapshotInfo;
/*     */     }
/*     */ 
/*     */     public void setType(GfaRules.SnapshotType type)
/*     */     {
/* 310 */       this.type = type;
/*     */     }
/*     */ 
/*     */     public void setDvlpg_hr(int dvlpg_hr)
/*     */     {
/* 317 */       this.dvlpg_hr = dvlpg_hr;
/*     */     }
/*     */ 
/*     */     public void setEndg_hr(int endg_hr)
/*     */     {
/* 324 */       this.endg_hr = endg_hr;
/*     */     }
/*     */   }
/*     */ 
/*     */   class SnapshotInfo
/*     */   {
/*     */     GfaRules.SnapshotType type;
/*     */     int hour;
/*     */     int minute;
/*     */     int dvlpg_hr;
/*     */     int endg_hr;
/*     */ 
/*     */     public SnapshotInfo(GfaRules.SnapshotType type, int hour, int minute, int dvlpg_hr, int endg_gr)
/*     */     {
/* 257 */       this.type = type;
/* 258 */       this.hour = hour;
/* 259 */       this.minute = minute;
/* 260 */       this.dvlpg_hr = dvlpg_hr;
/* 261 */       this.endg_hr = endg_gr;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaWorstAttr
 * JD-Core Version:    0.6.2
 */