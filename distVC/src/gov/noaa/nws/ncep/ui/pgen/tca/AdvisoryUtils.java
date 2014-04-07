/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.operation.overlay.OverlayOp;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class AdvisoryUtils
/*     */ {
/*     */   public static HashMap<TropicalCycloneAdvisory, String> createSegmentMap(TropicalCycloneAdvisory previous, TropicalCycloneAdvisory next)
/*     */   {
/*  27 */     HashMap advmap = new HashMap();
/*     */ 
/*  29 */     if ((!(previous.getSegment() instanceof BreakpointPair)) || 
/*  30 */       (!(next.getSegment() instanceof BreakpointPair))) return advmap;
/*     */ 
/*  32 */     Breakpoint prevBkpt1 = (Breakpoint)previous.getSegment().getBreakpoints().get(0);
/*  33 */     Breakpoint prevBkpt2 = (Breakpoint)previous.getSegment().getBreakpoints().get(1);
/*  34 */     Breakpoint currBkpt1 = (Breakpoint)next.getSegment().getBreakpoints().get(0);
/*  35 */     Breakpoint currBkpt2 = (Breakpoint)next.getSegment().getBreakpoints().get(1);
/*     */ 
/*  37 */     BreakpointManager bm = BreakpointManager.getInstance();
/*  38 */     String coastName = bm.findCoastName(prevBkpt1);
/*  39 */     if (!coastName.equals(bm.findCoastName(currBkpt1))) return advmap;
/*     */ 
/*  41 */     int pindex1 = bm.coastIndexOf(prevBkpt1);
/*  42 */     int pindex2 = bm.coastIndexOf(prevBkpt2);
/*  43 */     int cindex1 = bm.coastIndexOf(currBkpt1);
/*  44 */     int cindex2 = bm.coastIndexOf(currBkpt2);
/*     */ 
/*  50 */     GeometryFactory gf = new GeometryFactory();
/*  51 */     LineString geom0 = gf.createLineString(new Coordinate[] { new Coordinate(pindex1, 0.0D), new Coordinate(pindex2, 0.0D) });
/*  52 */     LineString geom1 = gf.createLineString(new Coordinate[] { new Coordinate(cindex1, 0.0D), new Coordinate(cindex2, 0.0D) });
/*     */ 
/*  58 */     Geometry result = OverlayOp.overlayOp(geom0, geom1, 1);
/*  59 */     if (!result.isEmpty())
/*     */     {
/*  61 */       int index1 = (int)Math.rint(result.getCoordinates()[0].x);
/*  62 */       int index2 = (int)Math.rint(result.getCoordinates()[1].x);
/*  63 */       BreakpointPair newPair = bm.getBreakpointPair(coastName, index1, index2);
/*  64 */       TropicalCycloneAdvisory adv = previous.copy();
/*  65 */       adv.setSegment(newPair);
/*  66 */       advmap.put(adv, "CON");
/*     */     }
/*     */ 
/*  72 */     result = OverlayOp.overlayOp(geom0, geom1, 3);
/*  73 */     if (!result.isEmpty())
/*     */     {
/*  75 */       for (int j = 0; j < result.getNumGeometries(); j++) {
/*  76 */         Geometry g = result.getGeometryN(j);
/*     */ 
/*  78 */         int index1 = (int)Math.rint(g.getCoordinates()[0].x);
/*  79 */         int index2 = (int)Math.rint(g.getCoordinates()[1].x);
/*  80 */         BreakpointPair newPair = bm.getBreakpointPair(coastName, index1, index2);
/*  81 */         TropicalCycloneAdvisory adv = previous.copy();
/*  82 */         adv.setSegment(newPair);
/*  83 */         advmap.put(adv, "CAN");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  95 */     result = OverlayOp.overlayOp(geom1, geom0, 3);
/*  96 */     if (!result.isEmpty()) {
/*  97 */       for (int j = 0; j < result.getNumGeometries(); j++) {
/*  98 */         Geometry g = result.getGeometryN(j);
/*     */ 
/* 100 */         int index1 = (int)Math.rint(g.getCoordinates()[0].x);
/* 101 */         int index2 = (int)Math.rint(g.getCoordinates()[1].x);
/* 102 */         BreakpointPair newPair = bm.getBreakpointPair(coastName, index1, index2);
/* 103 */         TropicalCycloneAdvisory adv = previous.copy();
/* 104 */         adv.setSegment(newPair);
/* 105 */         advmap.put(adv, "NEW");
/*     */       }
/*     */     }
/*     */ 
/* 109 */     return advmap;
/*     */   }
/*     */ 
/*     */   public static List<TropicalCycloneAdvisory> segmentAdvisory(TropicalCycloneAdvisory tcadv, Set<Breakpoint> bkptSet)
/*     */   {
/* 115 */     List alist = new ArrayList();
/*     */ 
/* 117 */     BreakpointManager bm = BreakpointManager.getInstance();
/* 118 */     String coast = bm.findCoastName((Breakpoint)tcadv.getSegment().getBreakpoints().get(0));
/*     */ 
/* 120 */     Breakpoint bkpt1 = (Breakpoint)tcadv.getSegment().getBreakpoints().get(0);
/* 121 */     Breakpoint bkpt2 = (Breakpoint)tcadv.getSegment().getBreakpoints().get(1);
/* 122 */     int index1 = bm.coastIndexOf(bkpt1);
/* 123 */     int index2 = bm.coastIndexOf(bkpt2);
/*     */ 
/* 125 */     TreeSet indexes = new TreeSet();
/* 126 */     indexes.add(new Integer(index1));
/* 127 */     indexes.add(new Integer(index2));
/*     */ 
/* 129 */     for (Breakpoint b : bkptSet) {
/* 130 */       if (coast.equals(bm.findCoastName(b))) {
/* 131 */         int idx = bm.coastIndexOf(b);
/* 132 */         if ((idx > index1) && (idx < index2)) indexes.add(new Integer(idx));
/*     */       }
/*     */     }
/*     */ 
/* 136 */     if (indexes.size() > 2) {
/* 137 */       Integer[] idxs = (Integer[])indexes.toArray(new Integer[indexes.size()]);
/* 138 */       for (int j = 0; j < indexes.size() - 1; j++) {
/* 139 */         BreakpointPair newPair = bm.getBreakpointPair(coast, idxs[j].intValue(), idxs[(j + 1)].intValue());
/* 140 */         TropicalCycloneAdvisory adv = tcadv.copy();
/* 141 */         adv.setSegment(newPair);
/* 142 */         alist.add(adv);
/*     */       }
/*     */     }
/*     */ 
/* 146 */     return alist;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.AdvisoryUtils
 * JD-Core Version:    0.6.2
 */