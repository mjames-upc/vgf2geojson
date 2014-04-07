/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.GeometryCollection;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LinearRing;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PGenException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ 
/*     */ public class ConvexHullMappingGenerator extends AbstractPolyMappingGenerator
/*     */ {
/*  54 */   Coordinate[] fromPoly = null;
/*  55 */   Coordinate[] toPoly = null;
/*     */ 
/*     */   public ConvexHullMappingGenerator(Coordinate[] fromPoly, Coordinate[] toPoly)
/*     */   {
/*  65 */     this.fromPoly = fromPoly;
/*  66 */     this.toPoly = toPoly;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getFromPoly()
/*     */   {
/*  73 */     return this.fromPoly;
/*     */   }
/*     */ 
/*     */   public void setFromPoly(Coordinate[] fromPoly)
/*     */   {
/*  80 */     this.fromPoly = fromPoly;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getToPoly()
/*     */   {
/*  87 */     return this.toPoly;
/*     */   }
/*     */ 
/*     */   public void setToPoly(Coordinate[] toPoly)
/*     */   {
/*  94 */     this.toPoly = toPoly;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<Coordinate, Coordinate> generateMappingPoints()
/*     */   {
/* 102 */     LinkedHashMap map = new LinkedHashMap();
/* 103 */     GeometryFactory gf = new GeometryFactory();
/*     */ 
/* 108 */     if ((this.fromPoly == null) || (this.toPoly == null)) {
/* 109 */       throw new IllegalStateException("All attributes must be non null");
/*     */     }
/*     */ 
/* 114 */     LinkedHashMap fromMap = new LinkedHashMap();
/* 115 */     for (Coordinate pt : this.fromPoly) fromMap.put(pt, null);
/*     */ 
/* 120 */     LinkedHashMap toMap = new LinkedHashMap();
/* 121 */     for (Coordinate pt : this.toPoly) toMap.put(pt, null);
/*     */ 
/* 126 */     LinearRing[] rings = { gf.createLinearRing(this.fromPoly), gf.createLinearRing(this.toPoly) };
/* 127 */     GeometryCollection poly = new GeometryCollection(rings, gf);
/* 128 */     Geometry hull = poly.convexHull();
/*     */ 
/* 134 */     Coordinate[] chull = hull.getCoordinates();
/* 135 */     boolean prev = rings[0].contains(gf.createPoint(chull[0]));
/* 136 */     for (int i = 1; i < chull.length; i++) {
/* 137 */       Point pt = gf.createPoint(chull[i]);
/* 138 */       boolean current = rings[0].contains(pt);
/* 139 */       if ((prev) && (!current)) {
/* 140 */         fromMap.put(chull[(i - 1)], chull[i]);
/* 141 */         toMap.put(chull[i], chull[(i - 1)]);
/*     */       }
/* 143 */       if ((!prev) && (current)) {
/* 144 */         fromMap.put(chull[i], chull[(i - 1)]);
/* 145 */         toMap.put(chull[(i - 1)], chull[i]);
/*     */       }
/* 147 */       prev = current;
/*     */     }
/*     */     LinkedHashMap secondary;
/*     */     LinkedHashMap primary;
/*     */     LinkedHashMap secondary;
/* 154 */     if (this.toPoly.length > this.fromPoly.length) {
/* 155 */       LinkedHashMap primary = toMap;
/* 156 */       secondary = fromMap;
/*     */     } else {
/* 158 */       primary = fromMap;
/* 159 */       secondary = toMap;
/*     */     }
/*     */ 
/* 166 */     ArrayList clist = new ArrayList();
/* 167 */     for (Coordinate c : primary.keySet()) {
/* 168 */       if (primary.get(c) != null) clist.add(c);
/*     */     }
/* 170 */     if (clist.isEmpty()) {
/* 171 */       map.clear();
/* 172 */       return map;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 179 */       for (??? = clist.iterator(); ???.hasNext(); ) { Coordinate c = (Coordinate)???.next();
/* 180 */         mapSection(primary, secondary, c);
/*     */       }
/*     */ 
/* 187 */       mapRemaining(secondary, primary);
/*     */ 
/* 193 */       mapRemaining(primary, secondary);
/*     */     }
/*     */     catch (PGenException pe) {
/* 196 */       System.out.println(pe.getMessage());
/* 197 */       map.clear();
/* 198 */       return map;
/*     */     }
/*     */ 
/* 201 */     if (this.toPoly.length > this.fromPoly.length)
/*     */     {
/* 205 */       for (Coordinate coord : primary.keySet()) {
/* 206 */         map.put((Coordinate)primary.get(coord), coord);
/*     */       }
/*     */     }
/*     */     else {
/* 210 */       map = primary;
/*     */     }
/* 212 */     return map;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.ConvexHullMappingGenerator
 * JD-Core Version:    0.6.2
 */