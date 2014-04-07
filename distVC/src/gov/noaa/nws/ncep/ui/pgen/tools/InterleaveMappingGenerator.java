/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.linearref.LengthIndexedLine;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class InterleaveMappingGenerator
/*     */   implements IMappingGenerator
/*     */ {
/*  33 */   Coordinate[] fromLine = null;
/*  34 */   Coordinate[] toLine = null;
/*  35 */   Coordinate[] initialFromPts = null;
/*  36 */   Coordinate[] initialToPts = null;
/*     */ 
/*     */   public InterleaveMappingGenerator(Coordinate[] fromLine, Coordinate[] toLine, Coordinate[] initialFromPts, Coordinate[] initialToPts)
/*     */   {
/*  51 */     this.fromLine = fromLine;
/*  52 */     this.toLine = toLine;
/*  53 */     this.initialFromPts = initialFromPts;
/*  54 */     this.initialToPts = initialToPts;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getFromLine()
/*     */   {
/*  61 */     return this.fromLine;
/*     */   }
/*     */ 
/*     */   public void setFromLine(Coordinate[] fromLine)
/*     */   {
/*  68 */     this.fromLine = fromLine;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getToLine()
/*     */   {
/*  75 */     return this.toLine;
/*     */   }
/*     */ 
/*     */   public void setToLine(Coordinate[] toLine)
/*     */   {
/*  82 */     this.toLine = toLine;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getInitialFromPts()
/*     */   {
/*  89 */     return this.initialFromPts;
/*     */   }
/*     */ 
/*     */   public void setInitialFromPts(Coordinate[] initialFromPts)
/*     */   {
/*  96 */     this.initialFromPts = initialFromPts;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getInitialToPts()
/*     */   {
/* 103 */     return this.initialToPts;
/*     */   }
/*     */ 
/*     */   public void setInitialToPts(Coordinate[] initialToPts)
/*     */   {
/* 110 */     this.initialToPts = initialToPts;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<Coordinate, Coordinate> generateMappingPoints()
/*     */   {
/* 121 */     if ((this.fromLine == null) || (this.initialFromPts == null) || (this.toLine == null) || (this.initialToPts == null)) {
/* 122 */       throw new IllegalStateException("All attributes must be non null");
/*     */     }
/* 124 */     LinkedHashMap map = new LinkedHashMap();
/*     */ 
/* 129 */     GeometryFactory gf = new GeometryFactory();
/* 130 */     LineString ls = gf.createLineString(this.fromLine);
/* 131 */     LengthIndexedLine fromLil = new LengthIndexedLine(ls);
/* 132 */     ls = gf.createLineString(this.toLine);
/* 133 */     LengthIndexedLine toLil = new LengthIndexedLine(ls);
/*     */ 
/* 140 */     TreeMap fromMap = new TreeMap();
/* 141 */     fromMap.put(new Double(0.0D), this.initialFromPts[0]);
/* 142 */     fromMap.put(new Double(1.0D), this.initialFromPts[(this.initialFromPts.length - 1)]);
/*     */ 
/* 149 */     TreeMap toMap = new TreeMap();
/* 150 */     toMap.put(new Double(0.0D), this.initialToPts[0]);
/* 151 */     toMap.put(new Double(1.0D), this.initialToPts[(this.initialToPts.length - 1)]);
/*     */ 
/* 159 */     for (int j = 1; j < this.initialFromPts.length - 1; j++) {
/* 160 */       double pct = fromLil.indexOf(this.initialFromPts[j]) / fromLil.getEndIndex();
/* 161 */       fromMap.put(new Double(pct), this.initialFromPts[j]);
/* 162 */       double index = pct * toLil.getEndIndex();
/* 163 */       toMap.put(new Double(pct), toLil.extractPoint(index));
/*     */     }
/*     */     double pct;
/* 172 */     for (int j = 1; j < this.initialToPts.length - 1; j++) {
/* 173 */       pct = toLil.indexOf(this.initialToPts[j]) / toLil.getEndIndex();
/* 174 */       toMap.put(new Double(pct), this.initialToPts[j]);
/* 175 */       double index = pct * fromLil.getEndIndex();
/* 176 */       fromMap.put(new Double(pct), fromLil.extractPoint(index));
/*     */     }
/*     */ 
/* 184 */     for (Double key : fromMap.keySet()) {
/* 185 */       Coordinate first = (Coordinate)fromMap.get(key);
/* 186 */       Coordinate second = (Coordinate)toMap.get(key);
/* 187 */       map.put(first, second);
/*     */     }
/*     */ 
/* 190 */     return map;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.InterleaveMappingGenerator
 * JD-Core Version:    0.6.2
 */