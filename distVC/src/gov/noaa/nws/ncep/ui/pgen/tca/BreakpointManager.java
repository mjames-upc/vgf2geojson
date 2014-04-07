/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.SerializationUtil;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.CoordinateList;
/*     */ import com.vividsolutions.jts.geom.Envelope;
/*     */ import com.vividsolutions.jts.index.quadtree.Quadtree;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ 
/*     */ public class BreakpointManager
/*     */ {
/*  39 */   private static BreakpointManager instance = null;
/*     */ 
/*  41 */   private final double DIST = 1.0D;
/*     */ 
/*  43 */   private static final String PGEN_ROOT = PgenStaticDataProvider.getProvider().getPgenLocalizationRoot();
/*  44 */   private static final String PGEN_ISLND_BRKPTS_TBL = PGEN_ROOT + "IslandBreakpoints.xml";
/*  45 */   private static final String PGEN_WATER_BRKPTS_TBL = PGEN_ROOT + "WaterBreakpoints.xml";
/*  46 */   private static final String PGEN_COAST_BRKPTS_TBL = PGEN_ROOT + "CoastBreakpoints.xml";
/*     */   private GeodeticCalculator gc;
/*  54 */   private CoastBreakpointList coasts = null;
/*  55 */   private IslandBreakpointList islands = null;
/*  56 */   private WaterBreakpointList waterways = null;
/*     */ 
/*  61 */   private HashMap<String, BreakpointSegment> coastMap = null;
/*  62 */   private HashMap<String, IslandBreakpoint> islandMap = null;
/*  63 */   private HashMap<String, WaterBreakpoint> waterwayMap = null;
/*     */ 
/*  69 */   private HashMap<String, String> coastNameMap = null;
/*     */ 
/*  75 */   private HashMap<String, String> zoneMap = null;
/*     */ 
/*  80 */   private Quadtree coastTree = null;
/*  81 */   private Quadtree islandTree = null;
/*  82 */   private Quadtree waterwayTree = null;
/*     */ 
/*     */   protected BreakpointManager()
/*     */   {
/*  89 */     this.gc = new GeodeticCalculator();
/*     */ 
/*  92 */     initializeCoasts();
/*     */ 
/*  95 */     initializeIslands();
/*     */ 
/*  98 */     initializeWaterways();
/*     */ 
/* 101 */     initializeZoneMap();
/*     */   }
/*     */ 
/*     */   public static synchronized BreakpointManager getInstance()
/*     */   {
/* 111 */     if (instance == null) {
/* 112 */       instance = new BreakpointManager();
/*     */     }
/* 114 */     return instance;
/*     */   }
/*     */ 
/*     */   private void initializeCoasts()
/*     */   {
/* 126 */     String bkptfile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/* 127 */       PGEN_COAST_BRKPTS_TBL);
/*     */     try
/*     */     {
/* 130 */       this.coasts = ((CoastBreakpointList)SerializationUtil.jaxbUnmarshalFromXmlFile(bkptfile));
/*     */     }
/*     */     catch (Exception e) {
/* 133 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 136 */     this.coastNameMap = new HashMap();
/* 137 */     this.coastMap = new HashMap();
/* 138 */     this.coastTree = new Quadtree();
/* 139 */     for (Iterator localIterator1 = this.coasts.getCoasts().iterator(); localIterator1.hasNext(); 
/* 152 */       ???.hasNext())
/*     */     {
/* 139 */       CoastBreakpoint coast = (CoastBreakpoint)localIterator1.next();
/*     */ 
/* 144 */       for (BreakpointSegment seg : coast.getSegments()) {
/* 145 */         this.coastMap.put(seg.getBreakpoint().getName(), seg);
/* 146 */         this.coastNameMap.put(seg.getBreakpoint().getName(), coast.getName());
/*     */       }
/*     */ 
/* 152 */       ??? = coast.getSegments().iterator(); continue; BreakpointSegment seg = (BreakpointSegment)???.next();
/* 153 */       Coordinate c = seg.getBreakpoint().getLocation();
/*     */ 
/* 155 */       Envelope env = new Envelope(c);
/* 156 */       this.coastTree.insert(env, seg.getBreakpoint());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initializeIslands()
/*     */   {
/* 171 */     String bkptfile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/* 172 */       PGEN_ISLND_BRKPTS_TBL);
/*     */     try {
/* 174 */       this.islands = ((IslandBreakpointList)SerializationUtil.jaxbUnmarshalFromXmlFile(bkptfile));
/*     */     }
/*     */     catch (Exception e) {
/* 177 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 183 */     this.islandMap = new HashMap();
/* 184 */     for (IslandBreakpoint bkpt : this.islands.getIslands()) {
/* 185 */       this.islandMap.put(bkpt.getBreakpoint().getName(), bkpt);
/*     */     }
/*     */ 
/* 191 */     this.islandTree = new Quadtree();
/* 192 */     for (IslandBreakpoint bkpt : this.islands.getIslands()) {
/* 193 */       Coordinate c = bkpt.getBreakpoint().getLocation();
/* 194 */       Envelope env = new Envelope(c.x - 1.0D, c.x + 1.0D, c.y - 1.0D, c.y + 1.0D);
/* 195 */       this.islandTree.insert(env, bkpt);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initializeWaterways()
/*     */   {
/* 206 */     double DIST = 2.0D;
/*     */ 
/* 211 */     String bkptfile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/* 212 */       PGEN_WATER_BRKPTS_TBL);
/*     */     try {
/* 214 */       this.waterways = ((WaterBreakpointList)SerializationUtil.jaxbUnmarshalFromXmlFile(bkptfile));
/*     */     }
/*     */     catch (Exception e) {
/* 217 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 223 */     this.waterwayMap = new HashMap();
/* 224 */     for (WaterBreakpoint bkpt : this.waterways.getWaterways()) {
/* 225 */       this.waterwayMap.put(bkpt.getBreakpoint().getName(), bkpt);
/*     */     }
/*     */ 
/* 231 */     this.waterwayTree = new Quadtree();
/* 232 */     for (WaterBreakpoint bkpt : this.waterways.getWaterways()) {
/* 233 */       Coordinate c = bkpt.getBreakpoint().getLocation();
/* 234 */       Envelope env = new Envelope(c.x - 2.0D, c.x + 2.0D, c.y - 2.0D, c.y + 2.0D);
/* 235 */       this.waterwayTree.insert(env, bkpt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public BPGeography getNearestIsland(Coordinate loc)
/*     */   {
/* 248 */     double min = 1.7976931348623157E+308D;
/* 249 */     BPGeography found = null;
/* 250 */     this.gc.setStartingGeographicPoint(loc.x, loc.y);
/*     */ 
/* 252 */     Envelope searchEnv = new Envelope(loc.x - 1.0D, loc.x + 1.0D, loc.y - 1.0D, loc.y + 1.0D);
/*     */ 
/* 258 */     List res = this.islandTree.query(searchEnv);
/* 259 */     Iterator iter = res.iterator();
/*     */ 
/* 261 */     while (iter.hasNext()) {
/* 262 */       IslandBreakpoint ibkpt = (IslandBreakpoint)iter.next();
/* 263 */       Coordinate where = ibkpt.getBreakpoint().getLocation();
/* 264 */       this.gc.setDestinationGeographicPoint(where.x, where.y);
/* 265 */       double dist = this.gc.getOrthodromicDistance();
/*     */ 
/* 267 */       if (dist < min) {
/* 268 */         min = dist;
/* 269 */         found = ibkpt;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 274 */     return found;
/*     */   }
/*     */ 
/*     */   public BPGeography getNearestWaterway(Coordinate loc)
/*     */   {
/* 284 */     double min = 1.7976931348623157E+308D;
/* 285 */     BPGeography found = null;
/* 286 */     this.gc.setStartingGeographicPoint(loc.x, loc.y);
/*     */ 
/* 288 */     Envelope searchEnv = new Envelope(loc.x - 1.0D, loc.x + 1.0D, loc.y - 1.0D, loc.y + 1.0D);
/*     */ 
/* 294 */     List res = this.waterwayTree.query(searchEnv);
/* 295 */     Iterator iter = res.iterator();
/*     */ 
/* 297 */     while (iter.hasNext()) {
/* 298 */       WaterBreakpoint ibkpt = (WaterBreakpoint)iter.next();
/* 299 */       Coordinate where = ibkpt.getBreakpoint().getLocation();
/* 300 */       this.gc.setDestinationGeographicPoint(where.x, where.y);
/* 301 */       double dist = this.gc.getOrthodromicDistance();
/*     */ 
/* 303 */       if (dist < min) {
/* 304 */         min = dist;
/* 305 */         found = ibkpt;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 310 */     return found;
/*     */   }
/*     */ 
/*     */   public Breakpoint getNearestBreakpoint(Coordinate loc) {
/* 314 */     return getNearestBreakpoint(loc, new BreakpointFilter());
/*     */   }
/*     */ 
/*     */   public Breakpoint getNearestBreakpoint(Coordinate loc, BreakpointFilter filter)
/*     */   {
/* 319 */     double min = 1.7976931348623157E+308D;
/* 320 */     Breakpoint found = null;
/* 321 */     this.gc.setStartingGeographicPoint(loc.x, loc.y);
/*     */ 
/* 324 */     Envelope searchEnv = new Envelope(loc);
/*     */ 
/* 329 */     List res = this.coastTree.query(searchEnv);
/* 330 */     Iterator iter = res.iterator();
/*     */ 
/* 332 */     while (iter.hasNext()) {
/* 333 */       Breakpoint ibkpt = (Breakpoint)iter.next();
/* 334 */       if (filter.isAccepted(ibkpt)) {
/* 335 */         Coordinate where = ibkpt.getLocation();
/* 336 */         this.gc.setDestinationGeographicPoint(where.x, where.y);
/* 337 */         double dist = this.gc.getOrthodromicDistance();
/*     */ 
/* 339 */         if (dist < min) {
/* 340 */           min = dist;
/* 341 */           found = ibkpt;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 346 */     return found;
/*     */   }
/*     */ 
/*     */   public BreakpointPair getBreakpointPair(Breakpoint bkpt1, Breakpoint bkpt2)
/*     */   {
/* 357 */     BreakpointPair bp = null;
/*     */ 
/* 360 */     String coastName = (String)this.coastNameMap.get(bkpt1.getName());
/*     */ 
/* 362 */     if (isCoastIsland(coastName)) {
/* 363 */       bp = getIslandPair(bkpt1, bkpt2);
/*     */     }
/*     */     else {
/* 366 */       bp = getCoastPair(bkpt1, bkpt2);
/*     */     }
/*     */ 
/* 369 */     return bp;
/*     */   }
/*     */ 
/*     */   private BreakpointPair getCoastPair(Breakpoint bkpt1, Breakpoint bkpt2)
/*     */   {
/* 380 */     CoordinateList clist = new CoordinateList();
/*     */ 
/* 382 */     BreakpointPair bp = new BreakpointPair();
/*     */ 
/* 385 */     String coastName = (String)this.coastNameMap.get(bkpt1.getName());
/* 386 */     List seglist = this.coasts.getCoast(coastName);
/*     */ 
/* 391 */     BreakpointSegment seg1 = (BreakpointSegment)this.coastMap.get(bkpt1.getName());
/* 392 */     int index1 = seglist.indexOf(seg1);
/* 393 */     BreakpointSegment seg2 = (BreakpointSegment)this.coastMap.get(bkpt2.getName());
/* 394 */     int index2 = seglist.indexOf(seg2);
/* 395 */     if (index1 == index2) return null;
/*     */     int first;
/*     */     int last;
/* 401 */     if (index1 < index2) {
/* 402 */       int first = index1;
/* 403 */       int last = index2;
/* 404 */       bp.addBreakpoint(bkpt1);
/* 405 */       bp.addBreakpoint(bkpt2);
/*     */     }
/*     */     else {
/* 408 */       first = index2;
/* 409 */       last = index1;
/* 410 */       bp.addBreakpoint(bkpt2);
/* 411 */       bp.addBreakpoint(bkpt1);
/*     */     }
/*     */ 
/* 417 */     clist.add(((BreakpointSegment)seglist.get(first)).getBreakpoint().getLocation(), true);
/* 418 */     if (!((BreakpointSegment)seglist.get(first)).getPaths().isEmpty())
/* 419 */       clist.add((Coordinate[])((BreakpointSegment)seglist.get(first)).getPaths().get(0), true);
/* 420 */     bp.addZones(((BreakpointSegment)seglist.get(first)).getZones());
/*     */ 
/* 426 */     for (int j = first + 1; j < last; j++) {
/* 427 */       if (((BreakpointSegment)seglist.get(j)).getBreakpoint().isOfficial()) {
/* 428 */         clist.add(((BreakpointSegment)seglist.get(j)).getBreakpoint().getLocation(), true);
/*     */       }
/* 430 */       if (!((BreakpointSegment)seglist.get(j)).getPaths().isEmpty())
/* 431 */         clist.add((Coordinate[])((BreakpointSegment)seglist.get(j)).getPaths().get(0), true);
/* 432 */       bp.addZones(((BreakpointSegment)seglist.get(j)).getZones());
/*     */     }
/*     */ 
/* 436 */     clist.add(((BreakpointSegment)seglist.get(last)).getBreakpoint().getLocation(), true);
/*     */ 
/* 438 */     bp.addPath(clist.toCoordinateArray());
/*     */ 
/* 440 */     return bp;
/*     */   }
/*     */ 
/*     */   private BreakpointPair getIslandPair(Breakpoint bkpt1, Breakpoint bkpt2)
/*     */   {
/* 452 */     CoordinateList clist = new CoordinateList();
/*     */ 
/* 454 */     BreakpointPair bp = new BreakpointPair();
/* 455 */     bp.addBreakpoint(bkpt1);
/* 456 */     bp.addBreakpoint(bkpt2);
/*     */ 
/* 459 */     String coastName = (String)this.coastNameMap.get(bkpt1.getName());
/* 460 */     List seglist = this.coasts.getCoast(coastName);
/* 461 */     int n = seglist.size();
/*     */ 
/* 466 */     BreakpointSegment seg1 = (BreakpointSegment)this.coastMap.get(bkpt1.getName());
/* 467 */     int first = seglist.indexOf(seg1);
/* 468 */     BreakpointSegment seg2 = (BreakpointSegment)this.coastMap.get(bkpt2.getName());
/* 469 */     int last = seglist.indexOf(seg2);
/*     */ 
/* 474 */     clist.add(((BreakpointSegment)seglist.get(first)).getBreakpoint().getLocation(), true);
/* 475 */     if (!((BreakpointSegment)seglist.get(first)).getPaths().isEmpty())
/* 476 */       clist.add((Coordinate[])((BreakpointSegment)seglist.get(first)).getPaths().get(0), true);
/* 477 */     bp.addZones(((BreakpointSegment)seglist.get(first)).getZones());
/*     */ 
/* 484 */     for (int j = (first + 1 + n) % n; j != last; j = (j + 1 + n) % n) {
/* 485 */       if (((BreakpointSegment)seglist.get(j)).getBreakpoint().isOfficial()) {
/* 486 */         clist.add(((BreakpointSegment)seglist.get(j)).getBreakpoint().getLocation(), true);
/*     */       }
/* 488 */       if (!((BreakpointSegment)seglist.get(j)).getPaths().isEmpty())
/* 489 */         clist.add((Coordinate[])((BreakpointSegment)seglist.get(j)).getPaths().get(0), true);
/* 490 */       bp.addZones(((BreakpointSegment)seglist.get(j)).getZones());
/*     */     }
/*     */ 
/* 493 */     clist.add(((BreakpointSegment)seglist.get(last)).getBreakpoint().getLocation(), true);
/*     */ 
/* 495 */     bp.addPath(clist.toCoordinateArray());
/*     */ 
/* 497 */     return bp;
/*     */   }
/*     */ 
/*     */   public String findCoastName(Breakpoint bkpt)
/*     */   {
/* 506 */     return (String)this.coastNameMap.get(bkpt.getName());
/*     */   }
/*     */ 
/*     */   public boolean isCoastIsland(String name)
/*     */   {
/* 515 */     for (CoastBreakpoint coast : this.coasts.getCoasts()) {
/* 516 */       if (coast.getName().equals(name)) return coast.isIsland();
/*     */     }
/* 518 */     return false;
/*     */   }
/*     */ 
/*     */   public Breakpoint findBorderPoint(Breakpoint bkpt1, Breakpoint bkpt2)
/*     */   {
/* 533 */     String coastName = (String)this.coastNameMap.get(bkpt1.getName());
/* 534 */     List seglist = this.coasts.getCoast(coastName);
/*     */ 
/* 539 */     BreakpointSegment seg1 = (BreakpointSegment)this.coastMap.get(bkpt1.getName());
/* 540 */     int index1 = seglist.indexOf(seg1);
/* 541 */     BreakpointSegment seg2 = (BreakpointSegment)this.coastMap.get(bkpt2.getName());
/* 542 */     int index2 = seglist.indexOf(seg2);
/*     */     int last;
/*     */     int first;
/*     */     int last;
/* 545 */     if (bkpt1.getCountry().equals("US")) {
/* 546 */       int first = index2;
/* 547 */       last = index1;
/*     */     }
/*     */     else {
/* 550 */       first = index1;
/* 551 */       last = index2;
/*     */     }
/*     */ 
/* 554 */     int inc = 1;
/* 555 */     if (first > last) inc = -1;
/*     */ 
/* 560 */     Breakpoint border = null;
/* 561 */     for (int j = first; j != last; j += inc) {
/* 562 */       if (((BreakpointSegment)seglist.get(j)).getBreakpoint().getCountry().equals("US")) {
/* 563 */         border = ((BreakpointSegment)seglist.get(j)).getBreakpoint();
/* 564 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 568 */     return border;
/*     */   }
/*     */ 
/*     */   public Breakpoint getBreakpoint(String name)
/*     */   {
/* 578 */     if (this.coastMap.containsKey(name)) {
/* 579 */       return ((BreakpointSegment)this.coastMap.get(name)).getBreakpoint();
/*     */     }
/* 581 */     if (this.islandMap.containsKey(name)) {
/* 582 */       return ((IslandBreakpoint)this.islandMap.get(name)).getBreakpoint();
/*     */     }
/* 584 */     if (this.waterwayMap.containsKey(name)) {
/* 585 */       return ((WaterBreakpoint)this.waterwayMap.get(name)).getBreakpoint();
/*     */     }
/*     */ 
/* 588 */     return null;
/*     */   }
/*     */ 
/*     */   private void initializeZoneMap()
/*     */   {
/* 598 */     this.zoneMap = PgenStaticDataProvider.getProvider().getZoneMap();
/*     */   }
/*     */ 
/*     */   public String getCWA(String zone)
/*     */   {
/* 607 */     return (String)this.zoneMap.get(zone);
/*     */   }
/*     */ 
/*     */   public boolean pairsOverlap(BreakpointPair thispair, BreakpointPair thatpair)
/*     */   {
/* 618 */     Breakpoint thisbkpt1 = (Breakpoint)thispair.getBreakpoints().get(0);
/* 619 */     Breakpoint thisbkpt2 = (Breakpoint)thispair.getBreakpoints().get(1);
/* 620 */     Breakpoint thatbkpt1 = (Breakpoint)thatpair.getBreakpoints().get(0);
/* 621 */     Breakpoint thatbkpt2 = (Breakpoint)thatpair.getBreakpoints().get(1);
/*     */ 
/* 626 */     String coastName = findCoastName(thisbkpt1);
/* 627 */     if (!coastName.equals(findCoastName(thatbkpt1))) return false;
/*     */ 
/* 632 */     List seglist = this.coasts.getCoast(coastName);
/* 633 */     BreakpointSegment seg = (BreakpointSegment)this.coastMap.get(thisbkpt1.getName());
/* 634 */     int thisidx1 = seglist.indexOf(seg);
/* 635 */     seg = (BreakpointSegment)this.coastMap.get(thisbkpt2.getName());
/* 636 */     int thisidx2 = seglist.indexOf(seg);
/* 637 */     seg = (BreakpointSegment)this.coastMap.get(thatbkpt1.getName());
/* 638 */     int thatidx1 = seglist.indexOf(seg);
/* 639 */     seg = (BreakpointSegment)this.coastMap.get(thatbkpt2.getName());
/* 640 */     int thatidx2 = seglist.indexOf(seg);
/*     */ 
/* 642 */     if ((thisidx1 < thisidx2) && (thisidx2 < thatidx1) && (thatidx1 < thatidx2)) return false;
/* 643 */     if ((thatidx1 < thatidx2) && (thatidx2 < thisidx1) && (thisidx1 < thisidx2)) return false;
/*     */ 
/* 645 */     return true;
/*     */   }
/*     */ 
/*     */   public int coastIndexOf(Breakpoint bkpt)
/*     */   {
/* 656 */     String coastName = findCoastName(bkpt);
/* 657 */     List seglist = this.coasts.getCoast(coastName);
/* 658 */     BreakpointSegment seg = (BreakpointSegment)this.coastMap.get(bkpt.getName());
/* 659 */     return seglist.indexOf(seg);
/*     */   }
/*     */ 
/*     */   public BreakpointPair getBreakpointPair(String coastName, int index1, int index2)
/*     */   {
/* 671 */     BreakpointPair bp = null;
/*     */ 
/* 673 */     List seglist = this.coasts.getCoast(coastName);
/* 674 */     Breakpoint bkpt1 = ((BreakpointSegment)seglist.get(index1)).getBreakpoint();
/* 675 */     Breakpoint bkpt2 = ((BreakpointSegment)seglist.get(index2)).getBreakpoint();
/*     */ 
/* 677 */     if (isCoastIsland(coastName)) {
/* 678 */       bp = getIslandPair(bkpt1, bkpt2);
/*     */     }
/*     */     else {
/* 681 */       bp = getCoastPair(bkpt1, bkpt2);
/*     */     }
/*     */ 
/* 684 */     return bp;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.BreakpointManager
 * JD-Core Version:    0.6.2
 */