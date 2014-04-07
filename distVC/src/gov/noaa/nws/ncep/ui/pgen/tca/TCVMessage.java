/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class TCVMessage
/*     */ {
/*     */   private static final String TIME_FORMAT = "hmm a ";
/*     */   private static final String DATE_FORMAT = " E MMM d yyyy";
/*     */   private static final String BREAKPOINT_FORMAT = "%-36s%.2fN %.2fW";
/*     */   private static final String TPC = "NWS TPC/NATIONAL HURRICANE CENTER MIAMI FL";
/*     */   private static final String CPHC = "NWS CENTRAL PACIFIC HURRICANE CENTER";
/*     */   private static final String HEADER = " WATCH/WARNING BREAKPOINTS/ADVISORY NUMBER ";
/*     */   private static final String HEADER_INT = " WATCH/WARNING BREAKPOINTS/INTERMEDIATE ADVISORY NUMBER ";
/*     */   private static final String TEST_STRING = "...THIS IS ONLY A TEST...";
/*     */   public static final String ACTION_NEW = "NEW";
/*     */   public static final String ACTION_CONT = "CON";
/*     */   public static final String ACTION_CANCEL = "CAN";
/*  54 */   private static final HashMap<String, String> prBreakpoints = new HashMap();
/*     */   private String issuingCenter;
/*     */   private String stormName;
/*     */   private int stormNumber;
/*     */   private String advisoryNumber;
/*     */   private String basin;
/*     */   private Calendar issueTime;
/*     */   private String stormType;
/*     */   private String timeZone;
/*     */   private String productClass;
/*     */   private Calendar purgeTime;
/*     */   private int seqNumber;
/*     */   private List<TCVEvent> events;
/*     */   private HashMap<TropicalCycloneAdvisory, String> statusMap;
/*     */ 
/*     */   static
/*     */   {
/*  80 */     prBreakpoints.put("PRZ008", "Puerto_Rico_Northwest");
/*  81 */     prBreakpoints.put("PRZ002", "Puerto_Rico_Northeast");
/*  82 */     prBreakpoints.put("PRZ003", "Puerto_Rico_Southeast");
/*  83 */     prBreakpoints.put("PRZ011", "Puerto_Rico_Southwest");
/*     */   }
/*     */ 
/*     */   public TCVMessage(String center, TCAElement tca)
/*     */   {
/*  95 */     saveStormInfo(center, tca);
/*  96 */     this.events = createNewEvents(tca);
/*     */   }
/*     */ 
/*     */   public TCVMessage(String center, TCAElement prev, TCAElement tca)
/*     */   {
/* 109 */     saveStormInfo(center, tca);
/* 110 */     this.events = createEvents(prev, tca);
/*     */   }
/*     */ 
/*     */   private void saveStormInfo(String center, TCAElement tca)
/*     */   {
/* 119 */     this.issuingCenter = center;
/*     */ 
/* 121 */     this.stormName = tca.getStormName().toUpperCase();
/* 122 */     this.stormNumber = tca.getStormNumber();
/* 123 */     this.advisoryNumber = tca.getAdvisoryNumber().toUpperCase();
/* 124 */     this.basin = Basin.getBasinAbbrev(tca.getBasin()).toUpperCase();
/* 125 */     this.issueTime = tca.getAdvisoryTime();
/* 126 */     this.stormType = tca.getStormType().toUpperCase();
/* 127 */     this.timeZone = tca.getTimeZone();
/*     */ 
/* 129 */     this.productClass = convertStatus(tca.getIssueStatus());
/*     */ 
/* 131 */     this.purgeTime = calculatePurgeTime(tca.getAdvisoryTime());
/* 132 */     this.seqNumber = generateETN().intValue();
/*     */ 
/* 135 */     this.statusMap = new HashMap();
/*     */   }
/*     */ 
/*     */   private List<TCVEvent> createNewEvents(TCAElement tca)
/*     */   {
/* 143 */     for (TropicalCycloneAdvisory tcadv : tca.getAdvisories())
/*     */     {
/* 147 */       if ((tcadv.getSegment().getZones() != null) && 
/* 148 */         (!tcadv.getSegment().getZones().isEmpty()))
/*     */       {
/* 150 */         this.statusMap.put(tcadv, "NEW");
/*     */       }
/*     */     }
/*     */ 
/* 154 */     return advisoriesToEvents();
/*     */   }
/*     */ 
/*     */   private List<TCVEvent> createEvents(TCAElement prev, TCAElement tca)
/*     */   {
/* 166 */     for (TropicalCycloneAdvisory padv : prev.getAdvisories())
/*     */     {
/* 169 */       if ((padv.getSegment().getZones() != null) && 
/* 170 */         (!padv.getSegment().getZones().isEmpty()))
/*     */       {
/* 172 */         if (((padv.getSegment() instanceof IslandBreakpoint)) || 
/* 173 */           ((padv.getSegment() instanceof WaterBreakpoint)))
/*     */         {
/* 179 */           String action = "CAN";
/* 180 */           for (TropicalCycloneAdvisory a : tca.getAdvisories()) {
/* 181 */             if (padv.equals(a)) {
/* 182 */               action = "CON";
/* 183 */               break;
/*     */             }
/*     */           }
/* 186 */           this.statusMap.put(padv, action);
/*     */         }
/* 189 */         else if ((padv.getSegment() instanceof BreakpointPair))
/*     */         {
/* 196 */           HashMap split = null;
/* 197 */           for (TropicalCycloneAdvisory a : tca.getAdvisories()) {
/* 198 */             if (padv.overlaps(a)) {
/* 199 */               split = AdvisoryUtils.createSegmentMap(padv, a);
/* 200 */               this.statusMap.putAll(split);
/*     */             }
/*     */           }
/* 203 */           if (split == null) {
/* 204 */             this.statusMap.put(padv, "CAN");
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 214 */     for (TropicalCycloneAdvisory adv : tca.getAdvisories())
/*     */     {
/* 217 */       if ((adv.getSegment().getZones() != null) && 
/* 218 */         (!adv.getSegment().getZones().isEmpty()))
/*     */       {
/* 220 */         if (((adv.getSegment() instanceof IslandBreakpoint)) || 
/* 221 */           ((adv.getSegment() instanceof WaterBreakpoint)))
/*     */         {
/* 227 */           String action = "NEW";
/* 228 */           for (TropicalCycloneAdvisory t : this.statusMap.keySet()) {
/* 229 */             if (adv.equals(t)) {
/* 230 */               action = null;
/* 231 */               break;
/*     */             }
/*     */           }
/*     */ 
/* 235 */           if (action != null) this.statusMap.put(adv, action);
/*     */ 
/*     */         }
/* 238 */         else if ((adv.getSegment() instanceof BreakpointPair))
/*     */         {
/* 245 */           String action = "NEW";
/* 246 */           for (TropicalCycloneAdvisory t : this.statusMap.keySet()) {
/* 247 */             if (adv.overlaps(t)) {
/* 248 */               action = null;
/* 249 */               break;
/*     */             }
/*     */           }
/* 252 */           if (action != null) this.statusMap.put(adv, action);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 258 */     return advisoriesToEvents();
/*     */   }
/*     */ 
/*     */   private List<TCVEvent> advisoriesToEvents()
/*     */   {
/* 267 */     List eventList = new ArrayList();
/*     */ 
/* 270 */     segmentAdvisories();
/*     */ 
/* 272 */     for (TropicalCycloneAdvisory tcadv : this.statusMap.keySet())
/*     */     {
/* 275 */       if ((tcadv.getSegment().getZones() != null) && 
/* 276 */         (!tcadv.getSegment().getZones().isEmpty()))
/*     */       {
/* 279 */         TropicalCycloneAdvisory adv = preprocessAdvisory(tcadv);
/*     */ 
/* 281 */         TCVEvent.TCVEventType type = TCVEvent.TCVEventType.LIST;
/* 282 */         if ((adv.getSegment() instanceof BreakpointPair)) type = TCVEvent.TCVEventType.SEGMENT;
/* 283 */         TCVEvent event = new TCVEvent(type);
/*     */ 
/* 286 */         TVtecObject vtec = new TVtecObject(this.productClass, (String)this.statusMap.get(tcadv), this.issuingCenter, 
/* 287 */           convertSeverity(adv.getSeverity()), 
/* 288 */           convertAdvisoryType(adv.getAdvisoryType()), 
/* 289 */           this.seqNumber, this.issueTime, null);
/*     */ 
/* 292 */         event.addVtecLine(vtec);
/* 293 */         event.setBreakpoints(adv.getSegment().getBreakpoints());
/* 294 */         event.addZones(adv.getSegment().getZones());
/* 295 */         event.setPurgeTime(this.purgeTime);
/*     */ 
/* 298 */         addToEventList(event, eventList);
/*     */       }
/*     */     }
/* 301 */     return eventList;
/*     */   }
/*     */ 
/*     */   private void addToEventList(TCVEvent event, List<TCVEvent> eventList)
/*     */   {
/* 310 */     switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tca$TCVEvent$TCVEventType()[event.getEvenType().ordinal()])
/*     */     {
/*     */     case 1:
/* 313 */       addListEvent(event, eventList);
/* 314 */       break;
/*     */     case 2:
/* 317 */       addSegmentEvent(event, eventList);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addListEvent(TCVEvent event, List<TCVEvent> eventList)
/*     */   {
/* 329 */     Breakpoint thisbkpt = (Breakpoint)event.getBreakpoints().get(0);
/*     */ 
/* 331 */     for (TCVEvent tcev : eventList) {
/* 332 */       if (tcev.getEvenType() == TCVEvent.TCVEventType.LIST) {
/* 333 */         Breakpoint bkpt2 = (Breakpoint)tcev.getBreakpoints().get(0);
/* 334 */         if ((event.equals(tcev)) && 
/* 335 */           (thisbkpt.getState().equals(bkpt2.getState()))) {
/* 336 */           tcev.addBreakpoint(thisbkpt);
/* 337 */           tcev.addZones(event.getUgc().getZones());
/* 338 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 342 */     eventList.add(event);
/*     */   }
/*     */ 
/*     */   private void addSegmentEvent(TCVEvent event, List<TCVEvent> eventList)
/*     */   {
/* 351 */     List pair1 = event.getBreakpoints();
/*     */ 
/* 353 */     for (TCVEvent tcev : eventList) {
/* 354 */       if (tcev.getEvenType() == TCVEvent.TCVEventType.SEGMENT)
/*     */       {
/* 360 */         List pair2 = tcev.getBreakpoints();
/* 361 */         if ((((Breakpoint)pair1.get(0)).equals(pair2.get(0))) && 
/* 362 */           (((Breakpoint)pair1.get(1)).equals(pair2.get(1)))) {
/* 363 */           tcev.addVtecLine((TVtecObject)event.getVtecLines().get(0));
/* 364 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 368 */     eventList.add(event);
/*     */   }
/*     */ 
/*     */   private void segmentAdvisories()
/*     */   {
/* 378 */     Set bkptSet = new HashSet();
/*     */ 
/* 383 */     for (TropicalCycloneAdvisory tcadv : this.statusMap.keySet()) {
/* 384 */       if ((tcadv.getSegment() instanceof BreakpointPair)) {
/* 385 */         bkptSet.add((Breakpoint)tcadv.getSegment().getBreakpoints().get(0));
/* 386 */         bkptSet.add((Breakpoint)tcadv.getSegment().getBreakpoints().get(1));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 394 */     HashMap newmap = new HashMap();
/* 395 */     for (TropicalCycloneAdvisory tcadv : this.statusMap.keySet()) {
/* 396 */       if ((tcadv.getSegment() instanceof BreakpointPair)) {
/* 397 */         List advlist = AdvisoryUtils.segmentAdvisory(tcadv, bkptSet);
/* 398 */         if (!advlist.isEmpty()) {
/* 399 */           String status = (String)this.statusMap.get(tcadv);
/*     */ 
/* 401 */           for (TropicalCycloneAdvisory tadv : advlist)
/* 402 */             newmap.put(tadv, status);
/*     */         }
/*     */         else
/*     */         {
/* 406 */           newmap.put(tcadv, (String)this.statusMap.get(tcadv));
/*     */         }
/*     */       }
/*     */       else {
/* 410 */         newmap.put(tcadv, (String)this.statusMap.get(tcadv));
/*     */       }
/*     */     }
/* 413 */     this.statusMap = newmap;
/*     */   }
/*     */ 
/*     */   private TropicalCycloneAdvisory preprocessAdvisory(TropicalCycloneAdvisory adv)
/*     */   {
/* 427 */     if (advisoryCrossesUSBorder(adv)) {
/* 428 */       return setBorder(adv);
/*     */     }
/*     */ 
/* 434 */     if (advisoryInPuertoRico(adv)) {
/* 435 */       return createPRBreakpoint(adv);
/*     */     }
/*     */ 
/* 438 */     return adv;
/*     */   }
/*     */ 
/*     */   private TropicalCycloneAdvisory createPRBreakpoint(TropicalCycloneAdvisory adv)
/*     */   {
/* 449 */     TropicalCycloneAdvisory newadv = new TropicalCycloneAdvisory();
/* 450 */     newadv.setAdvisoryType(adv.getAdvisoryType());
/* 451 */     newadv.setGeographyType(adv.getGeographyType());
/* 452 */     newadv.setSeverity(adv.getSeverity());
/*     */ 
/* 454 */     BPGeography segment = adv.getSegment();
/* 455 */     BreakpointList bl = new BreakpointList();
/* 456 */     bl.setPaths(segment.getPaths());
/* 457 */     bl.setZones(segment.getZones());
/*     */ 
/* 459 */     Breakpoint bkpt1 = (Breakpoint)segment.getBreakpoints().get(0);
/* 460 */     Breakpoint bkpt2 = (Breakpoint)segment.getBreakpoints().get(1);
/*     */ 
/* 466 */     if (bkpt1.equals(bkpt2)) {
/* 467 */       Breakpoint prall = new Breakpoint();
/* 468 */       prall.setName("PUERTO-RICO-ALL");
/* 469 */       prall.setLocation(new Coordinate(-66.450000000000003D, 18.199999999999999D));
/* 470 */       prall.setCountry("US");
/* 471 */       prall.setState("PR");
/* 472 */       prall.setOfficial(true);
/* 473 */       bl.addBreakpoint(prall);
/*     */     }
/*     */     else
/*     */     {
/* 479 */       BreakpointManager bm = BreakpointManager.getInstance();
/* 480 */       for (String key : prBreakpoints.keySet()) {
/* 481 */         if (segment.getZones().contains(key)) {
/* 482 */           bl.addBreakpoint(bm.getBreakpoint((String)prBreakpoints.get(key)));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 487 */     newadv.setSegment(bl);
/* 488 */     return newadv;
/*     */   }
/*     */ 
/*     */   private boolean advisoryInPuertoRico(TropicalCycloneAdvisory adv)
/*     */   {
/* 496 */     if (((Breakpoint)adv.getSegment().getBreakpoints().get(0)).getState().equals("PR")) {
/* 497 */       return true;
/*     */     }
/* 499 */     return false;
/*     */   }
/*     */ 
/*     */   private TropicalCycloneAdvisory setBorder(TropicalCycloneAdvisory adv)
/*     */   {
/* 509 */     BreakpointManager bm = BreakpointManager.getInstance();
/*     */ 
/* 511 */     TropicalCycloneAdvisory newadv = new TropicalCycloneAdvisory();
/* 512 */     newadv.setAdvisoryType(adv.getAdvisoryType());
/* 513 */     newadv.setGeographyType(adv.getGeographyType());
/* 514 */     newadv.setSeverity(adv.getSeverity());
/*     */ 
/* 516 */     Breakpoint bkpt1 = (Breakpoint)adv.getSegment().getBreakpoints().get(0);
/* 517 */     Breakpoint bkpt2 = (Breakpoint)adv.getSegment().getBreakpoints().get(1);
/* 518 */     Breakpoint border = bm.findBorderPoint(bkpt1, bkpt2);
/*     */     BPGeography bp;
/*     */     BPGeography bp;
/* 521 */     if (bkpt1.getCountry().equals("US"))
/* 522 */       bp = bm.getBreakpointPair(bkpt1, border);
/*     */     else {
/* 524 */       bp = bm.getBreakpointPair(border, bkpt2);
/*     */     }
/* 526 */     newadv.setSegment(bp);
/*     */ 
/* 528 */     return newadv;
/*     */   }
/*     */ 
/*     */   private boolean advisoryCrossesUSBorder(TropicalCycloneAdvisory adv)
/*     */   {
/* 536 */     boolean retval = false;
/*     */ 
/* 538 */     if ((adv.getSegment() instanceof BreakpointPair)) {
/* 539 */       String country1 = ((Breakpoint)adv.getSegment().getBreakpoints().get(0)).getCountry();
/* 540 */       String country2 = ((Breakpoint)adv.getSegment().getBreakpoints().get(1)).getCountry();
/*     */ 
/* 542 */       if ((country1.equals("US")) && (!country2.equals("US")))
/* 543 */         retval = true;
/* 544 */       else if ((!country1.equals("US")) && (country2.equals("US"))) {
/* 545 */         retval = true;
/*     */       }
/*     */     }
/* 548 */     return retval;
/*     */   }
/*     */ 
/*     */   private Calendar calculatePurgeTime(Calendar advisoryTime)
/*     */   {
/* 556 */     Calendar purgeTime = Calendar.getInstance(advisoryTime.getTimeZone());
/* 557 */     purgeTime.setTimeInMillis(advisoryTime.getTimeInMillis());
/* 558 */     purgeTime.add(11, 6);
/*     */ 
/* 560 */     int hour = purgeTime.get(11);
/* 561 */     if ((hour > 3) && (hour < 9)) {
/* 562 */       purgeTime.set(11, 3);
/*     */     }
/* 564 */     else if ((hour > 9) && (hour < 15)) {
/* 565 */       purgeTime.set(11, 9);
/*     */     }
/* 567 */     else if ((hour > 15) && (hour < 21)) {
/* 568 */       purgeTime.set(11, 15);
/*     */     }
/* 570 */     else if (hour > 21) {
/* 571 */       purgeTime.set(11, 21);
/*     */     }
/* 573 */     else if (hour < 3) {
/* 574 */       purgeTime.setTimeInMillis(advisoryTime.getTimeInMillis());
/* 575 */       purgeTime.set(11, 21);
/*     */     }
/*     */ 
/* 578 */     return purgeTime;
/*     */   }
/*     */ 
/*     */   private Integer generateETN()
/*     */   {
/* 586 */     int etn = Basin.getBasinNumber(this.basin) * 1000 + this.stormNumber;
/* 587 */     return new Integer(etn);
/*     */   }
/*     */ 
/*     */   private String convertAdvisoryType(String advisoryType)
/*     */   {
/* 594 */     String s = new String("X");
/*     */ 
/* 596 */     if (advisoryType.equals("Watch")) {
/* 597 */       s = new String("A");
/*     */     }
/* 599 */     else if (advisoryType.equals("Warning")) {
/* 600 */       s = new String("W");
/*     */     }
/*     */ 
/* 603 */     return s;
/*     */   }
/*     */ 
/*     */   private String convertSeverity(String severity)
/*     */   {
/* 610 */     String pp = new String("XX");
/*     */ 
/* 612 */     if (severity.equals("Hurricane")) {
/* 613 */       pp = new String("HU");
/*     */     }
/* 615 */     else if (severity.equals("Tropical Storm")) {
/* 616 */       pp = new String("TR");
/*     */     }
/*     */ 
/* 619 */     return pp;
/*     */   }
/*     */ 
/*     */   private String convertStatus(String issueStatus)
/*     */   {
/* 627 */     String k = new String("T");
/*     */ 
/* 629 */     if (issueStatus.equals("Operational")) {
/* 630 */       k = new String("O");
/*     */     }
/* 632 */     else if (issueStatus.equals("Experimental")) {
/* 633 */       k = new String("E");
/*     */     }
/* 635 */     else if (issueStatus.equals("Experimental Operational")) {
/* 636 */       k = new String("X");
/*     */     }
/*     */ 
/* 639 */     return k;
/*     */   }
/*     */ 
/*     */   public String createText()
/*     */   {
/* 648 */     String localTime = createLocalTimeString();
/*     */ 
/* 650 */     String center = "NWS TPC/NATIONAL HURRICANE CENTER MIAMI FL";
/* 651 */     if ((PgenUtil.getCurrentOffice().equalsIgnoreCase("PHFO")) || 
/* 652 */       (PgenUtil.getCurrentOffice().equalsIgnoreCase("HFO"))) center = "NWS CENTRAL PACIFIC HURRICANE CENTER";
/*     */ 
/* 654 */     StringWriter sw = new StringWriter();
/* 655 */     PrintWriter pw = new PrintWriter(sw);
/*     */ 
/* 657 */     pw.println();
/* 658 */     if (StormAdvisoryNumber.isIntermediate(this.advisoryNumber)) {
/* 659 */       pw.println(this.stormName + " WATCH/WARNING BREAKPOINTS/INTERMEDIATE ADVISORY NUMBER " + this.advisoryNumber);
/*     */     }
/*     */     else {
/* 662 */       pw.println(this.stormName + " WATCH/WARNING BREAKPOINTS/ADVISORY NUMBER " + this.advisoryNumber);
/*     */     }
/*     */ 
/* 665 */     if (this.productClass.equals("T")) {
/* 666 */       pw.format("TEST...%s %s%02d%4d...TEST\n", new Object[] { center, this.basin, Integer.valueOf(this.stormNumber), Integer.valueOf(this.issueTime.get(1)) });
/*     */     }
/*     */     else {
/* 669 */       pw.format("%s %s%02d%4d\n", new Object[] { center, this.basin, Integer.valueOf(this.stormNumber), Integer.valueOf(this.issueTime.get(1)) });
/*     */     }
/* 671 */     pw.println(localTime);
/*     */ 
/* 673 */     if (this.productClass.equals("T")) {
/* 674 */       pw.println();
/* 675 */       pw.println("...THIS IS ONLY A TEST...");
/*     */     }
/*     */ 
/* 678 */     pw.println();
/* 679 */     pw.println("." + this.stormType + " " + this.stormName);
/* 680 */     pw.println();
/*     */ 
/* 682 */     Collections.sort(this.events);
/*     */ 
/* 684 */     for (TCVEvent event : this.events) {
/* 685 */       pw.println(event.getUgc().createUGCString());
/* 686 */       for (TVtecObject vtec : event.getVtecLines()) {
/* 687 */         pw.println(vtec.getVtecString());
/*     */       }
/* 689 */       pw.println(localTime);
/* 690 */       pw.println("");
/* 691 */       for (Breakpoint bkpt : event.getBreakpoints()) {
/* 692 */         pw.println(formatBreakpoint(bkpt));
/*     */       }
/* 694 */       pw.println("");
/* 695 */       pw.println("$$");
/* 696 */       pw.println("");
/*     */     }
/*     */ 
/* 699 */     String wfoList = generateWFOList();
/* 700 */     pw.format("ATTN...WFO...%s", new Object[] { wfoList });
/*     */ 
/* 702 */     return sw.toString();
/*     */   }
/*     */ 
/*     */   private String generateWFOList()
/*     */   {
/* 711 */     BreakpointManager bm = BreakpointManager.getInstance();
/* 712 */     TreeSet wfos = new TreeSet();
/* 713 */     StringBuilder sb = new StringBuilder();
/*     */     Iterator localIterator2;
/* 715 */     for (Iterator localIterator1 = this.events.iterator(); localIterator1.hasNext(); 
/* 716 */       localIterator2.hasNext())
/*     */     {
/* 715 */       TCVEvent event = (TCVEvent)localIterator1.next();
/* 716 */       localIterator2 = event.getUgc().getZones().iterator(); continue; String zone = (String)localIterator2.next();
/* 717 */       if (bm.getCWA(zone) != null)
/* 718 */         wfos.add(bm.getCWA(zone));
/*     */       else {
/* 720 */         System.out.println("Could not find WFO associated with zone: " + zone);
/*     */       }
/*     */     }
/*     */ 
/* 724 */     for (String wfo : wfos) {
/* 725 */       sb.append(wfo);
/* 726 */       sb.append("...");
/*     */     }
/*     */ 
/* 729 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private String createLocalTimeString()
/*     */   {
/* 738 */     String localFormat = "hmm a '" + this.timeZone + "'" + " E MMM d yyyy";
/* 739 */     SimpleDateFormat sdf = new SimpleDateFormat(localFormat);
/* 740 */     sdf.setTimeZone(createLocalTimeZone());
/* 741 */     String time = sdf.format(this.issueTime.getTime()).toUpperCase();
/*     */ 
/* 743 */     return time;
/*     */   }
/*     */ 
/*     */   private TimeZone createLocalTimeZone()
/*     */   {
/*     */     TimeZone tz;
/*     */     TimeZone tz;
/* 752 */     if (this.timeZone.equalsIgnoreCase("AST")) {
/* 753 */       tz = TimeZone.getTimeZone("GMT-4");
/*     */     }
/*     */     else
/*     */     {
/*     */       TimeZone tz;
/* 755 */       if (this.timeZone.equalsIgnoreCase("EDT")) {
/* 756 */         tz = TimeZone.getTimeZone("GMT-4");
/*     */       }
/*     */       else
/*     */       {
/*     */         TimeZone tz;
/* 758 */         if (this.timeZone.equalsIgnoreCase("EST")) {
/* 759 */           tz = TimeZone.getTimeZone("GMT-5");
/*     */         }
/*     */         else
/*     */         {
/*     */           TimeZone tz;
/* 761 */           if (this.timeZone.equalsIgnoreCase("CDT")) {
/* 762 */             tz = TimeZone.getTimeZone("GMT-5");
/*     */           }
/*     */           else
/*     */           {
/*     */             TimeZone tz;
/* 764 */             if (this.timeZone.equalsIgnoreCase("CST")) {
/* 765 */               tz = TimeZone.getTimeZone("GMT-6");
/*     */             }
/*     */             else
/*     */             {
/*     */               TimeZone tz;
/* 767 */               if (this.timeZone.equalsIgnoreCase("PDT")) {
/* 768 */                 tz = TimeZone.getTimeZone("GMT-7");
/*     */               }
/*     */               else
/*     */               {
/*     */                 TimeZone tz;
/* 770 */                 if (this.timeZone.equalsIgnoreCase("PST")) {
/* 771 */                   tz = TimeZone.getTimeZone("GMT-8");
/*     */                 }
/*     */                 else
/* 774 */                   tz = TimeZone.getTimeZone("GMT"); 
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 778 */     return tz;
/*     */   }
/*     */ 
/*     */   private String formatBreakpoint(Breakpoint bkpt)
/*     */   {
/* 786 */     double lat = bkpt.getLocation().y;
/* 787 */     double lon = bkpt.getLocation().x;
/* 788 */     String state = bkpt.getState();
/*     */ 
/* 790 */     StringBuilder name = new StringBuilder(bkpt.getName().replace('_', '-').toUpperCase());
/* 791 */     if ((state != null) && (!state.isEmpty()) && (!state.equals("--")) && (!state.equals("PR"))) {
/* 792 */       name.append("-" + state.toUpperCase());
/*     */     }
/*     */ 
/* 795 */     String bkptLine = String.format("%-36s%.2fN %.2fW", new Object[] { name.toString(), Double.valueOf(lat), Double.valueOf(Math.abs(lon)) });
/* 796 */     return bkptLine;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.TCVMessage
 * JD-Core Version:    0.6.2
 */