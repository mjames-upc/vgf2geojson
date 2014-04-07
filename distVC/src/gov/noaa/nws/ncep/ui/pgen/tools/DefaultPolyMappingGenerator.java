/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PGenException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class DefaultPolyMappingGenerator extends AbstractPolyMappingGenerator
/*     */ {
/*  43 */   Coordinate[] fromPoly = null;
/*  44 */   Coordinate[] toPoly = null;
/*     */ 
/*     */   public DefaultPolyMappingGenerator(Coordinate[] fromPoly, Coordinate[] toPoly)
/*     */   {
/*  54 */     this.fromPoly = fromPoly;
/*  55 */     this.toPoly = toPoly;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getFromPoly()
/*     */   {
/*  62 */     return this.fromPoly;
/*     */   }
/*     */ 
/*     */   public void setFromPoly(Coordinate[] fromPoly)
/*     */   {
/*  69 */     this.fromPoly = fromPoly;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getToPoly()
/*     */   {
/*  76 */     return this.toPoly;
/*     */   }
/*     */ 
/*     */   public void setToPoly(Coordinate[] toPoly)
/*     */   {
/*  83 */     this.toPoly = toPoly;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<Coordinate, Coordinate> generateMappingPoints()
/*     */   {
/*  94 */     if ((this.fromPoly == null) || (this.toPoly == null)) {
/*  95 */       throw new IllegalStateException("All attributes must be non null");
/*     */     }
/*  97 */     LinkedHashMap map = new LinkedHashMap();
/*     */ 
/* 102 */     LinkedHashMap fromMap = new LinkedHashMap();
/* 103 */     for (Coordinate pt : this.fromPoly) fromMap.put(pt, null);
/*     */ 
/* 108 */     LinkedHashMap toMap = new LinkedHashMap();
/* 109 */     for (Coordinate pt : this.toPoly) toMap.put(pt, null);
/*     */ 
/* 114 */     fromMap.put(this.fromPoly[0], this.toPoly[0]);
/* 115 */     toMap.put(this.toPoly[0], this.fromPoly[0]);
/*     */     LinkedHashMap secondary;
/*     */     LinkedHashMap primary;
/*     */     LinkedHashMap secondary;
/* 121 */     if (this.toPoly.length > this.fromPoly.length) {
/* 122 */       LinkedHashMap primary = toMap;
/* 123 */       secondary = fromMap;
/*     */     } else {
/* 125 */       primary = fromMap;
/* 126 */       secondary = toMap;
/*     */     }
/*     */ 
/* 132 */     Coordinate[] pCoords = (Coordinate[])primary.keySet().toArray(new Coordinate[primary.size()]);
/*     */     try
/*     */     {
/* 139 */       mapSection(primary, secondary, pCoords[0]);
/*     */ 
/* 145 */       mapRemaining(primary, secondary);
/*     */     }
/*     */     catch (PGenException pe) {
/* 148 */       System.out.println(pe.getMessage());
/* 149 */       map.clear();
/* 150 */       return map;
/*     */     }
/*     */ 
/* 153 */     if (this.toPoly.length > this.fromPoly.length)
/*     */     {
/* 157 */       for (Coordinate coord : primary.keySet()) {
/* 158 */         map.put((Coordinate)primary.get(coord), coord);
/*     */       }
/*     */     }
/*     */     else {
/* 162 */       map = primary;
/*     */     }
/* 164 */     return map;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.DefaultPolyMappingGenerator
 * JD-Core Version:    0.6.2
 */