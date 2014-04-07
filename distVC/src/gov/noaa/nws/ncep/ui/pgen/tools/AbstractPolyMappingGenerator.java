/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.CoordinateArrays;
/*     */ import com.vividsolutions.jts.geom.CoordinateList;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.linearref.LengthIndexedLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PGenException;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class AbstractPolyMappingGenerator
/*     */   implements IMappingGenerator
/*     */ {
/*     */   public abstract LinkedHashMap<Coordinate, Coordinate> generateMappingPoints();
/*     */ 
/*     */   protected void mapSection(LinkedHashMap<Coordinate, Coordinate> primary, LinkedHashMap<Coordinate, Coordinate> secondary, Coordinate start)
/*     */     throws PGenException
/*     */   {
/*  56 */     Coordinate[] pCoords = (Coordinate[])primary.keySet().toArray(new Coordinate[primary.size()]);
/*  57 */     Coordinate[] sCoords = (Coordinate[])secondary.keySet().toArray(new Coordinate[secondary.size()]);
/*     */ 
/*  62 */     if (primary.get(start) == null) {
/*  63 */       throw new PGenException("mapSection: Starting coordinate is not mapped.");
/*     */     }
/*     */ 
/*  69 */     Coordinate end = null;
/*  70 */     Coordinate last = getPreviousCoordinate(start, pCoords);
/*  71 */     Coordinate current = start;
/*     */     do {
/*  73 */       current = getNextCoordinate(current, pCoords);
/*  74 */       if (primary.get(current) != null) {
/*  75 */         end = current;
/*  76 */         break;
/*     */       }
/*     */     }
/*  78 */     while (current != last);
/*     */ 
/*  84 */     if (end == null) {
/*  85 */       end = last;
/*  86 */       Coordinate otherLast = getPreviousCoordinate((Coordinate)primary.get(start), sCoords);
/*  87 */       primary.put(end, otherLast);
/*  88 */       secondary.put(otherLast, end);
/*     */     }
/*     */ 
/*  95 */     Coordinate[] section1 = extractCoordinates(pCoords, start, end);
/*  96 */     Coordinate[] section2 = extractCoordinates(sCoords, (Coordinate)primary.get(start), (Coordinate)primary.get(end));
/*     */ 
/* 101 */     if ((section1.length <= 2) || (section2.length <= 2)) return;
/*     */ 
/* 107 */     if (section1.length == section2.length) {
/* 108 */       for (int j = 1; j < section1.length - 1; j++) {
/* 109 */         primary.put(section1[j], section2[j]);
/* 110 */         secondary.put(section2[j], section1[j]);
/*     */       }
/* 112 */       return;
/*     */     }
/*     */ 
/* 121 */     GeometryFactory gf = new GeometryFactory();
/* 122 */     LineString ls1 = gf.createLineString(section1);
/* 123 */     LengthIndexedLine lil1 = new LengthIndexedLine(ls1);
/* 124 */     LineString ls2 = gf.createLineString(section2);
/* 125 */     LengthIndexedLine lil2 = new LengthIndexedLine(ls2);
/*     */ 
/* 127 */     Coordinate new1 = null; Coordinate new2 = null;
/* 128 */     double minDiff = 1.7976931348623157E+308D;
/* 129 */     for (int j = 1; j < section1.length - 1; j++) {
/* 130 */       double dist1 = lil1.indexOf(section1[j]) / ls1.getLength();
/* 131 */       for (int k = 1; k < section2.length - 1; k++) {
/* 132 */         double dist2 = lil2.indexOf(section2[k]) / ls2.getLength();
/* 133 */         double diff = Math.abs(dist1 - dist2);
/* 134 */         if (diff < minDiff) {
/* 135 */           minDiff = diff;
/* 136 */           new1 = section1[j];
/* 137 */           new2 = section2[k];
/*     */         }
/*     */       }
/*     */     }
/* 141 */     primary.put(new1, new2);
/* 142 */     secondary.put(new2, new1);
/*     */ 
/* 144 */     mapSection(primary, secondary, start);
/*     */ 
/* 146 */     mapSection(primary, secondary, new1);
/*     */   }
/*     */ 
/*     */   protected void mapRemaining(LinkedHashMap<Coordinate, Coordinate> first, LinkedHashMap<Coordinate, Coordinate> second)
/*     */     throws PGenException
/*     */   {
/* 160 */     GeometryFactory gf = new GeometryFactory();
/* 161 */     LinkedHashMap tempMap = new LinkedHashMap();
/*     */ 
/* 170 */     Coordinate[] fromCoords = (Coordinate[])first.keySet().toArray(new Coordinate[first.size()]);
/* 171 */     Coordinate[] toCoords = (Coordinate[])second.keySet().toArray(new Coordinate[second.size()]);
/*     */     Coordinate current;
/* 176 */     for (int i = 0; i < fromCoords.length; i++)
/*     */     {
/* 178 */       if (first.get(fromCoords[i]) == null)
/*     */       {
/* 183 */         current = fromCoords[i];
/*     */         do
/* 185 */           current = getPreviousCoordinate(current, fromCoords);
/* 186 */         while (first.get(current) == null);
/* 187 */         Coordinate start = current;
/*     */ 
/* 192 */         current = fromCoords[i];
/*     */         do
/* 194 */           current = getNextCoordinate(current, fromCoords);
/* 195 */         while (first.get(current) == null);
/* 196 */         Coordinate end = current;
/*     */ 
/* 202 */         Coordinate[] section1 = extractCoordinates(fromCoords, start, end);
/* 203 */         Coordinate[] section2 = extractCoordinates(toCoords, (Coordinate)first.get(start), (Coordinate)first.get(end));
/*     */ 
/* 208 */         LineString ls1 = gf.createLineString(section1);
/* 209 */         LengthIndexedLine lil1 = new LengthIndexedLine(ls1);
/* 210 */         LineString ls2 = gf.createLineString(section2);
/* 211 */         LengthIndexedLine lil2 = new LengthIndexedLine(ls2);
/*     */ 
/* 218 */         double dist1 = lil1.indexOf(fromCoords[i]) / ls1.getLength();
/* 219 */         Coordinate newone = lil2.extractPoint(dist1 * ls2.getLength());
/* 220 */         tempMap.put(fromCoords[i], newone);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 227 */     for (Coordinate c : tempMap.keySet()) {
/* 228 */       first.put(c, (Coordinate)tempMap.get(c));
/* 229 */       Coordinate prev = getPreviousCoordinate(c, fromCoords);
/* 230 */       insertPoint(second, (Coordinate)first.get(prev), (Coordinate)tempMap.get(c));
/* 231 */       second.put((Coordinate)tempMap.get(c), c);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Coordinate getPreviousCoordinate(Coordinate start, Coordinate[] coordList)
/*     */   {
/* 242 */     int length = coordList.length;
/* 243 */     int idx = CoordinateArrays.indexOf(start, coordList);
/* 244 */     idx = (idx - 1 + length) % length;
/* 245 */     return coordList[idx];
/*     */   }
/*     */ 
/*     */   private Coordinate getNextCoordinate(Coordinate start, Coordinate[] coordList)
/*     */   {
/* 254 */     int length = coordList.length;
/* 255 */     int idx = CoordinateArrays.indexOf(start, coordList);
/* 256 */     idx = (idx + 1 + length) % length;
/* 257 */     return coordList[idx];
/*     */   }
/*     */ 
/*     */   private Coordinate[] extractCoordinates(Coordinate[] coordList, Coordinate start, Coordinate end)
/*     */   {
/* 269 */     CoordinateList list = new CoordinateList();
/* 270 */     Coordinate current = start;
/* 271 */     list.add(current, true);
/*     */     do {
/* 273 */       current = getNextCoordinate(current, coordList);
/* 274 */       list.add(current, true);
/* 275 */     }while (current != end);
/* 276 */     return list.toCoordinateArray();
/*     */   }
/*     */ 
/*     */   private void insertPoint(LinkedHashMap<Coordinate, Coordinate> map, Coordinate existing, Coordinate newpos)
/*     */   {
/* 288 */     LinkedHashMap newMap = new LinkedHashMap();
/*     */ 
/* 293 */     for (Coordinate c : map.keySet()) {
/* 294 */       newMap.put(c, (Coordinate)map.get(c));
/*     */     }
/*     */ 
/* 298 */     map.clear();
/*     */ 
/* 304 */     for (Coordinate c : newMap.keySet()) {
/* 305 */       map.put(c, (Coordinate)newMap.get(c));
/* 306 */       if (c.equals2D(existing)) map.put(newpos, null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.AbstractPolyMappingGenerator
 * JD-Core Version:    0.6.2
 */