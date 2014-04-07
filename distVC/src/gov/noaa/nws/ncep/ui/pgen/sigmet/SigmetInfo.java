/*     */ package gov.noaa.nws.ncep.ui.pgen.sigmet;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineSegment;
/*     */ import com.vividsolutions.jts.geom.LinearRing;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.StationTable;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.geotools.data.DefaultQuery;
/*     */ import org.geotools.data.FeatureSource;
/*     */ import org.geotools.data.shapefile.indexed.IndexType;
/*     */ import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
/*     */ import org.geotools.feature.FeatureCollection;
/*     */ import org.geotools.feature.FeatureIterator;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*     */ import org.opengis.feature.simple.SimpleFeature;
/*     */ import org.opengis.feature.simple.SimpleFeatureType;
/*     */ import org.opengis.feature.type.GeometryDescriptor;
/*     */ 
/*     */ public class SigmetInfo
/*     */ {
/*     */   public static List<Station> VOLCANO_STATION_LIST;
/*  73 */   public static final String[] SIGMET_TYPES = { "INTL", "CONV", "NCON", "AIRM", "OUTL" };
/*  74 */   public static final String GFA_TEXT = new String("GFA_TYPE");
/*     */ 
/*  76 */   public static final Map<String, String[]> AREA_MAP = new HashMap();
/*  77 */   public static final Map<String, String[]> ID_MAP = new HashMap();
/*  78 */   public static final Map<String, String[]> PHEN_MAP = new HashMap();
/*     */ 
/*  80 */   public static final String[] SPEED_ARRAY = { "5", "10", "15", "20", "25", "30", "35", "40", "45", "50" };
/*  81 */   public static final String[] DIRECT_ARRAY = { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", 
/*  82 */     "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW" };
/*     */ 
/*  83 */   public static final String[] FIR_ARRAY = { "PAZA|ANCHORAGE", "KZHU|HOUSTON_OCEANIC", "KZMA|MIAMI_OCEANIC", 
/*  84 */     "KZNY|NEW_YORK_OCEANIC", "KZAK|OAKLAND_OCEANIC", "TJZS|SAN_JUAN" };
/*     */ 
/*  85 */   public static final String[] TREND_ARRAY = { "-none-", "NC", "WKN", "INTSF" };
/*  86 */   public static final String[] REM_ARRAY = { "-none-", "BASED_ON_SATELLITE_OBS", "BASED_ON_ACFT_AND_SAT", 
/*  87 */     "BASED_ON_LATST_ADVSRY", "BASED_ON_SAT_AND_LTG_OBS", 
/*  88 */     "BASED_ON_SATELLITE_OBS_AND_LATEST_ADVSRY", 
/*  89 */     "BASED_ON_LATEST_WASHINGTON_VAAC_ADVISORY", 
/*  90 */     "BASED_ON_ACFT_RPT" };
/*     */ 
/*  92 */   public static final String[] VOL_NAME_BUCKET_ARRAY = { "-Not_listed,_Enter_Name/Location-", 
/*  93 */     "AA-AM", "AN-AZ", "B", "CA-CH", "CI-CZ", "D-E", "F", "G", "H", "I-J", "KA-KH", "KI-KZ", "L", 
/*  94 */     "MA-MC", "ME-MZ", "N", "O-P", "Q-R", "SA-SE", "SF-SZ", "TA-TH", "TI-TZ", "U", "V-Z" };
/*     */   public static final String LINE_SEPERATER = ":::";
/*     */   public static Map<String, List<String>> VOLCANO_BUCKET_MAP;
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 106 */       VOLCANO_STATION_LIST = PgenStaticDataProvider.getProvider().getVolcanoTbl().getStationList();
/*     */ 
/* 108 */       VOLCANO_BUCKET_MAP = initVolBucketMap();
/*     */ 
/* 110 */       AREA_MAP.put(SIGMET_TYPES[0], new String[] { "KKCI", "KNHC", "PHFO", "PAWU" });
/* 111 */       AREA_MAP.put(SIGMET_TYPES[1], new String[] { "KMKC" });
/* 112 */       AREA_MAP.put(SIGMET_TYPES[2], new String[] { "KSFO", "KSLC", "KCHI", "KDFW", "KBOS", "KMIA", "PHNL", "PANC", "PAFA", "PAJN" });
/* 113 */       AREA_MAP.put(SIGMET_TYPES[3], new String[] { "KSFO", "KSLC", "KCHI", "KDFW", "KBOS", "KMIA", "PHNL", "PANC", "PAFA", "PAJN" });
/* 114 */       AREA_MAP.put(SIGMET_TYPES[4], new String[] { "KSFO", "KSLC", "KCHI", "KDFW", "KBOS", "KMIA", "PHNL", "PANC", "PAFA", "PAJN" });
/*     */ 
/* 117 */       ID_MAP.put(SIGMET_TYPES[0], new String[] { 
/* 118 */         "ALFA", "BRAVO", "CHARLIE", "DELTA", "ECHO", "FOXTROT", "GOLF", "HOTEL", "INDIA", "JULIETT", 
/* 119 */         "KILO", "LIMA", "MIKE", "NOVEMBER", "OSCAR", "PAPA", "QUEBEC", "ROMEO", "SIERRA", "TANGO", 
/* 120 */         "UNIFORM", "VICTOR", "WHISKEY", "XRAY", "YANKEE", "ZULU" });
/* 121 */       ID_MAP.put(SIGMET_TYPES[1], new String[] { "EAST", "CENTRAL", "WEST" });
/* 122 */       ID_MAP.put(SIGMET_TYPES[2], new String[] { "NOVEMBER", "OSCAR", "PAPA", "QUEBEC", "ROMEO", "UNIFORM", "VICTOR", "WHISKEY", "XRAY", "YANKEE" });
/* 123 */       ID_MAP.put(SIGMET_TYPES[3], new String[] { "SIERRA", "TANGO", "ZULU" });
/* 124 */       ID_MAP.put(SIGMET_TYPES[4], new String[] { "EAST", "CENTRAL", "WEST" });
/*     */ 
/* 126 */       PHEN_MAP.put(SIGMET_TYPES[0], new String[] { "FRQ_TS", "OBSC_TS", "EMBD_TS", "SQL_TS", 
/* 127 */         "SEV_TURB", "SEV_ICE", "VOLCANIC_ASH", "TROPICAL_CYCLONE", 
/* 128 */         "RDOACT_CLD" });
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 142 */       System.out.println(" SigmetInfo initialize error: " + e.getMessage().toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getSigmetTypeString(String pgenType) {
/* 147 */     if ((pgenType == null) || ("".equals(pgenType))) return SIGMET_TYPES[0];
/* 148 */     for (String temp : SIGMET_TYPES) {
/* 149 */       if (pgenType.contains(temp))
/* 150 */         return temp;
/*     */     }
/* 152 */     return SIGMET_TYPES[0];
/*     */   }
/*     */ 
/*     */   private static Map<String, List<String>> initVolBucketMap() {
/* 156 */     Map result = new HashMap();
/*     */ 
/* 158 */     List volcanoStnList = VOLCANO_STATION_LIST;
/* 159 */     ArrayList volcanoList = new ArrayList();
/*     */     Station s;
/* 160 */     for (Iterator localIterator1 = volcanoStnList.iterator(); localIterator1.hasNext(); volcanoList.add(s.getStnname())) s = (Station)localIterator1.next();
/*     */ 
/* 162 */     Collections.sort(volcanoList);
/*     */ 
/* 165 */     for (int ii = 1; ii < VOL_NAME_BUCKET_ARRAY.length; ii++) {
/* 166 */       String bktStr = VOL_NAME_BUCKET_ARRAY[ii];
/* 167 */       String[] keys = bktStr.toUpperCase().split("-");
/* 168 */       ArrayList volSubList = new ArrayList();
/*     */ 
/* 170 */       if ((keys != null) && (keys.length >= 1))
/*     */       {
/* 172 */         char key0 = keys[0].charAt(0);
/* 173 */         char key1 = keys[0].length() > 1 ? keys[0].charAt(1) : 'A';
/* 174 */         char key2 = keys.length > 1 ? keys[1].charAt(0) : keys[0].charAt(0);
/* 175 */         char key3 = (keys.length > 1) && (keys[1].length() > 1) ? keys[1].charAt(1) : 'Z';
/*     */ 
/* 177 */         for (String volName : volcanoList) {
/* 178 */           String volN = new String(volName).toUpperCase();
/*     */ 
/* 180 */           if ((key0 <= volN.charAt(0)) && (volN.charAt(0) <= key2) && 
/* 181 */             (key1 <= volN.charAt(1)) && (volN.charAt(1) <= key3)) {
/* 182 */             volSubList.add(volName);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 187 */       result.put(bktStr, volSubList);
/*     */     }
/*     */ 
/* 190 */     return result;
/*     */   }
/*     */ 
/*     */   public static boolean isVolcanoNameEntered(String name)
/*     */   {
/* 196 */     Collection lists = VOLCANO_BUCKET_MAP.values();
/* 197 */     for (List list : lists) {
/* 198 */       if ((list != null) && (list.contains(name)))
/* 199 */         return false;
/*     */     }
/* 201 */     return true;
/*     */   }
/*     */ 
/*     */   public static Polygon getPolygon(double[] latlonArray, IMapDescriptor mapDescriptor)
/*     */   {
/* 207 */     Coordinate[] coorArray = new Coordinate[latlonArray.length / 2];
/* 208 */     double[] point = new double[3];
/* 209 */     int i = 0; for (int j = 0; (i < latlonArray.length - 1) && (j < coorArray.length); j++) {
/* 210 */       point = mapDescriptor.worldToPixel(new double[] { latlonArray[(i + 1)], latlonArray[i], 0.0D });
/* 211 */       coorArray[j] = new Coordinate(point[0], point[1]);
/*     */ 
/* 209 */       i += 2;
/*     */     }
/*     */ 
/* 214 */     GeometryFactory gf = new GeometryFactory();
/* 215 */     return gf.createPolygon(gf.createLinearRing(coorArray), new LinearRing[0]);
/*     */   }
/*     */ 
/*     */   public static Polygon getPolygon(Coordinate[] latlonArray, IMapDescriptor mapDescriptor) {
/* 219 */     Coordinate[] coorArray = latlonToPixelInCoor(latlonArray, mapDescriptor);
/* 220 */     if (coorArray.length <= 3) coorArray = new Coordinate[0];
/* 221 */     GeometryFactory gf = new GeometryFactory();
/* 222 */     return gf.createPolygon(gf.createLinearRing(coorArray), new LinearRing[0]);
/*     */   }
/*     */ 
/*     */   public static Coordinate[] latlonToPixelInCoor(Coordinate[] coor, IMapDescriptor mapDescriptor) {
/* 226 */     Coordinate[] result = new Coordinate[coor.length];
/* 227 */     double[][] temp = PgenUtil.latlonToPixel(coor, mapDescriptor);
/* 228 */     for (int i = 0; i < result.length; i++) {
/* 229 */       result[i] = new Coordinate(temp[i][0], temp[i][1]);
/*     */     }
/* 231 */     return result;
/*     */   }
/*     */ 
/*     */   public static Coordinate latlonToPixelInCoor1(Coordinate coor, IMapDescriptor mapDescriptor) {
/* 235 */     return latlonToPixelInCoor(new Coordinate[] { coor }, mapDescriptor)[0];
/*     */   }
/*     */ 
/*     */   public static Coordinate[] pixelsToCoorArray(double[][] pixels) {
/* 239 */     Coordinate[] result = new Coordinate[pixels.length];
/* 240 */     for (int i = 0; i < pixels.length; i++) {
/* 241 */       result[i] = new Coordinate(pixels[i][0], pixels[i][1]);
/*     */     }
/* 243 */     return result;
/*     */   }
/*     */ 
/*     */   public static Coordinate[] getIsolated(Coordinate vertex, double widthInNautical, IMapDescriptor mapDescriptor)
/*     */   {
/* 249 */     double[] tmp = { vertex.x, vertex.y, 0.0D };
/* 250 */     double[] center = mapDescriptor.worldToPixel(tmp);
/*     */ 
/* 252 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/* 253 */     gc.setStartingGeographicPoint(tmp[0], tmp[1]);
/* 254 */     gc.setDirection(0.0D, widthInNautical);
/*     */ 
/* 256 */     double[] tmp2 = { gc.getDestinationGeographicPoint().getX(), 
/* 257 */       gc.getDestinationGeographicPoint().getY(), 0.0D };
/* 258 */     double[] circum = mapDescriptor.worldToPixel(tmp2);
/*     */ 
/* 260 */     int numpts = 360;
/* 261 */     double axisAngle = 0.0D;
/*     */ 
/* 263 */     double cosineAxis = Math.cos(-Math.toRadians(axisAngle));
/* 264 */     double sineAxis = Math.sin(-Math.toRadians(axisAngle));
/*     */ 
/* 266 */     ArrayList list = new ArrayList();
/*     */ 
/* 268 */     double[] diff = { circum[0] - center[0], circum[1] - center[1] };
/* 269 */     double width = Math.sqrt(diff[0] * diff[0] + diff[1] * diff[1]);
/*     */ 
/* 271 */     double angle = 0.0D;
/*     */ 
/* 273 */     for (int i = 0; i < numpts; i++) {
/* 274 */       double thisSine = Math.sin(-Math.toRadians(angle));
/* 275 */       double thisCosine = Math.cos(-Math.toRadians(angle));
/*     */ 
/* 277 */       double[] temp = mapDescriptor.pixelToWorld(
/* 278 */         new double[] { center[0] + width * (cosineAxis * thisCosine - sineAxis * thisSine), 
/* 279 */         center[1] + width * (sineAxis * thisCosine + cosineAxis * thisSine) });
/* 280 */       list.add(new Coordinate(temp[0], temp[1]));
/*     */ 
/* 282 */       angle += 1.0D;
/*     */     }
/* 284 */     return (Coordinate[])list.toArray(new Coordinate[0]);
/*     */   }
/*     */ 
/*     */   public static Polygon getIsolatedPolygon(Coordinate vertex, double widthInNautical, IMapDescriptor mapDescriptor) {
/* 288 */     Coordinate[] isolated = getIsolated(vertex, widthInNautical, mapDescriptor);
/* 289 */     Coordinate[] ip = new Coordinate[isolated.length + 1];
/* 290 */     ip = (Coordinate[])Arrays.copyOf(isolated, isolated.length);
/* 291 */     ip[(ip.length - 1)] = isolated[0];
/* 292 */     return getPolygon(ip, mapDescriptor);
/*     */   }
/*     */ 
/*     */   public static Polygon getSOLPolygon(Coordinate[] coors, String line, double width, IMapDescriptor mapDescriptor) {
/* 296 */     Coordinate[] ip = getSOLCoors(coors, line, width, mapDescriptor);
/* 297 */     Coordinate[] ipPlus = new Coordinate[ip.length + 1];
/* 298 */     ipPlus = (Coordinate[])Arrays.copyOf(ip, ipPlus.length);
/* 299 */     ipPlus[(ipPlus.length - 1)] = ip[0];
/* 300 */     return getPolygon(ipPlus, mapDescriptor);
/*     */   }
/*     */ 
/*     */   public static Coordinate[] getSOLCoors(Coordinate[] pts, String lineType, double width, IMapDescriptor mapDescriptor) {
/* 304 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*     */ 
/* 306 */     if (!"ESOL".equals(lineType))
/*     */     {
/* 308 */       double azimuth = 0.0D;
/* 309 */       if ("SOF".equalsIgnoreCase(lineType))
/* 310 */         azimuth = 180.0D;
/* 311 */       else if ("EOF".equalsIgnoreCase(lineType))
/* 312 */         azimuth = 90.0D;
/* 313 */       else if ("WOF".equalsIgnoreCase(lineType)) {
/* 314 */         azimuth = -90.0D;
/*     */       }
/* 316 */       Coordinate[] sides = new Coordinate[pts.length + 2];
/* 317 */       sides[0] = pts[0];
/* 318 */       sides[(sides.length - 1)] = pts[(pts.length - 1)];
/*     */ 
/* 320 */       for (int i = 0; i < pts.length; i++) {
/* 321 */         gc.setStartingGeographicPoint(pts[i].x, pts[i].y);
/* 322 */         gc.setDirection(azimuth, width);
/* 323 */         Point2D jpt = gc.getDestinationGeographicPoint();
/* 324 */         sides[(i + 1)] = new Coordinate(jpt.getX(), jpt.getY());
/*     */       }
/* 326 */       return sides;
/*     */     }
/*     */ 
/* 329 */     Coordinate[][] sides = getSides(pts, width);
/* 330 */     Coordinate[][] sidesWithArcIntsc = getSidesWithArcIntsc(mapDescriptor, pts, sides[0], sides[1]);
/*     */ 
/* 332 */     Coordinate[] result = new Coordinate[sidesWithArcIntsc[0].length + sidesWithArcIntsc[1].length];
/* 333 */     System.arraycopy(sidesWithArcIntsc[0], 0, result, 0, sidesWithArcIntsc[0].length);
/* 334 */     System.arraycopy(sidesWithArcIntsc[1], 0, result, sidesWithArcIntsc[0].length, sidesWithArcIntsc[1].length);
/*     */ 
/* 336 */     return result;
/*     */   }
/*     */ 
/*     */   public static double[] getAzimuth(Coordinate[] vertice)
/*     */   {
/* 341 */     double[] azimuthS = new double[vertice.length];
/* 342 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*     */ 
/* 344 */     for (int i = 0; i < vertice.length; i++) {
/* 345 */       if (i > 0) {
/* 346 */         gc.setStartingGeographicPoint(vertice[(i - 1)].x, vertice[(i - 1)].y);
/* 347 */         gc.setDestinationGeographicPoint(vertice[i].x, vertice[i].y);
/* 348 */         azimuthS[(i - 1)] = gc.getAzimuth();
/*     */ 
/* 350 */         if (i == vertice.length - 1) azimuthS[i] = azimuthS[(i - 1)];
/*     */       }
/*     */     }
/*     */ 
/* 354 */     return azimuthS;
/*     */   }
/*     */ 
/*     */   public static Coordinate[][] getSides(Coordinate[] vertice, double attrSigLineWidth) {
/* 358 */     double[] azimuthS = getAzimuth(vertice);
/* 359 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*     */ 
/* 361 */     int sidesLength = vertice.length * 2;
/* 362 */     Coordinate[] sides = new Coordinate[sidesLength]; Coordinate[] sidesOther = new Coordinate[sidesLength];
/*     */ 
/* 364 */     double azimuth = 0.0D; double a1 = 0.0D; double a2 = 0.0D;
/*     */ 
/* 366 */     for (int i = 0; i < sidesLength; i++)
/*     */     {
/* 368 */       if (i == 0) { sides[i] = vertice[i]; sidesOther[i] = vertice[i]; } else {
/* 369 */         if ((i > 2) && (i == sidesLength - 1)) { sides[i] = vertice[(vertice.length - 1)]; sidesOther[i] = vertice[(vertice.length - 1)]; break;
/*     */         }
/* 371 */         gc.setStartingGeographicPoint(vertice[(i / 2)].x, vertice[(i / 2)].y);
/* 372 */         azimuth = azimuthS[((i - 1) / 2)];
/*     */ 
/* 374 */         a1 = (azimuth > 90.0D) && (azimuth <= 180.0D) ? azimuth - 270.0D : azimuth + 90.0D;
/* 375 */         gc.setDirection(a1, attrSigLineWidth);
/* 376 */         Point2D jpt = gc.getDestinationGeographicPoint();
/* 377 */         sides[i] = new Coordinate(jpt.getX(), jpt.getY());
/*     */ 
/* 379 */         a2 = (azimuth < -90.0D) && (azimuth >= -180.0D) ? azimuth + 270.0D : azimuth - 90.0D;
/* 380 */         gc.setDirection(a2, attrSigLineWidth);
/* 381 */         Point2D jptOther = gc.getDestinationGeographicPoint();
/* 382 */         sidesOther[i] = new Coordinate(jptOther.getX(), jptOther.getY());
/*     */       }
/*     */     }
/*     */ 
/* 386 */     return new Coordinate[][] { sides, sidesOther };
/*     */   }
/*     */ 
/*     */   public static Coordinate[][] getSidesWithArcIntsc(IMapDescriptor mapDescriptor, Coordinate[] vertice, Coordinate[] sides, Coordinate[] sidesOther) {
/* 390 */     ArrayList sidesList = new ArrayList();
/* 391 */     ArrayList sidesOtherList = new ArrayList();
/* 392 */     sidesList.add(sides[0]); sidesOtherList.add(sidesOther[0]);
/* 393 */     sidesList.add(sides[1]); sidesOtherList.add(sidesOther[1]);
/*     */ 
/* 395 */     for (int i = 1; i < sides.length - 4; i += 2)
/*     */     {
/* 397 */       LineSegment ls1 = new LineSegment(latlonToPixelInCoor1(sides[i], mapDescriptor), 
/* 398 */         latlonToPixelInCoor1(sides[(i + 1)], mapDescriptor));
/* 399 */       LineSegment ls2 = new LineSegment(latlonToPixelInCoor1(sides[(i + 2)], mapDescriptor), 
/* 400 */         latlonToPixelInCoor1(sides[(i + 3)], mapDescriptor));
/* 401 */       Coordinate coor = ls1.intersection(ls2);
/*     */ 
/* 403 */       LineSegment lsA = new LineSegment(latlonToPixelInCoor1(sidesOther[i], mapDescriptor), 
/* 404 */         latlonToPixelInCoor1(sidesOther[(i + 1)], mapDescriptor));
/* 405 */       LineSegment lsB = new LineSegment(latlonToPixelInCoor1(sidesOther[(i + 2)], mapDescriptor), 
/* 406 */         latlonToPixelInCoor1(sidesOther[(i + 3)], mapDescriptor));
/* 407 */       Coordinate coor2 = lsA.intersection(lsB);
/*     */ 
/* 409 */       if (coor != null) {
/* 410 */         double[] aPixel = mapDescriptor.pixelToWorld(new double[] { coor.x, coor.y });
/* 411 */         coor = new Coordinate(aPixel[0], aPixel[1]);
/* 412 */         sidesList.add(coor);
/*     */       } else {
/* 414 */         ArrayList list = getArcPath(mapDescriptor, vertice[((i + 1) / 2)], sides[(i + 1)], sides[(i + 2)]);
/* 415 */         sidesList.add((list != null) && (list.size() > 0) ? (Coordinate)list.get(0) : sides[(i + 1)]);
/* 416 */         sidesList.addAll(list);
/* 417 */         sidesList.add((list != null) && (list.size() > 0) ? (Coordinate)list.get(list.size() - 1) : sides[(i + 2)]);
/*     */       }
/*     */ 
/* 420 */       if (coor2 != null) {
/* 421 */         double[] bPixel = mapDescriptor.pixelToWorld(new double[] { coor2.x, coor2.y });
/* 422 */         coor2 = new Coordinate(bPixel[0], bPixel[1]);
/* 423 */         sidesOtherList.add(coor2);
/*     */       } else {
/* 425 */         ArrayList lhList = getArcPath(mapDescriptor, vertice[((i + 1) / 2)], sidesOther[(i + 1)], sidesOther[(i + 2)]);
/* 426 */         sidesOtherList.add((lhList != null) && (lhList.size() > 0) ? (Coordinate)lhList.get(lhList.size() - 1) : sidesOther[(i + 1)]);
/*     */ 
/* 428 */         for (int ii = lhList.size() - 1; ii >= 0; ii--) sidesOtherList.add((Coordinate)lhList.get(ii));
/* 429 */         sidesOtherList.add((lhList != null) && (lhList.size() > 0) ? (Coordinate)lhList.get(0) : sidesOther[(i + 2)]);
/*     */       }
/*     */     }
/* 432 */     sidesList.add(sides[(sides.length - 2)]);
/* 433 */     sidesOtherList.add(sidesOther[(sidesOther.length - 2)]);
/*     */ 
/* 435 */     sidesList.add(sides[(sides.length - 1)]);
/* 436 */     sidesOtherList.add(sidesOther[(sidesOther.length - 1)]);
/*     */ 
/* 438 */     return new Coordinate[][] { (Coordinate[])sidesList.toArray(new Coordinate[0]), (Coordinate[])sidesOtherList.toArray(new Coordinate[0]) };
/*     */   }
/*     */ 
/*     */   public static ArrayList<Coordinate> getArcPath(IMapDescriptor mapDescriptor, Coordinate vertex, Coordinate side1, Coordinate side2)
/*     */   {
/* 443 */     double[] tmp = { vertex.x, vertex.y, 0.0D };
/* 444 */     double[] tmp2 = { side1.x, side1.y, 0.0D };
/* 445 */     double[] tmp3 = { side2.x, side2.y, 0.0D };
/*     */ 
/* 447 */     double[] center = mapDescriptor.worldToPixel(tmp);
/* 448 */     double[] circum = mapDescriptor.worldToPixel(tmp2);
/* 449 */     double[] circum2 = mapDescriptor.worldToPixel(tmp3);
/*     */ 
/* 451 */     double axisAngle = Math.atan2(circum[1] - center[1], circum[0] - center[0]);
/* 452 */     double axisAngle2 = Math.atan2(circum2[1] - center[1], circum2[0] - center[0]);
/*     */ 
/* 454 */     axisAngle = 360.0D - Math.toDegrees((6.283185307179586D + axisAngle) % 6.283185307179586D);
/* 455 */     axisAngle2 = 360.0D - Math.toDegrees((6.283185307179586D + axisAngle2) % 6.283185307179586D);
/* 456 */     int numpts = Math.abs((int)Math.round(getAngExt(axisAngle, axisAngle2)));
/* 457 */     axisAngle = getStartAngle(axisAngle, axisAngle2);
/*     */ 
/* 459 */     double cosineAxis = Math.cos(-Math.toRadians(axisAngle));
/* 460 */     double sineAxis = Math.sin(-Math.toRadians(axisAngle));
/*     */ 
/* 462 */     ArrayList list = new ArrayList();
/*     */ 
/* 464 */     double[] diff = { circum[0] - center[0], circum[1] - center[1] };
/* 465 */     double width = Math.sqrt(diff[0] * diff[0] + diff[1] * diff[1]);
/*     */ 
/* 467 */     double angle = 0.0D;
/*     */ 
/* 469 */     for (int i = 0; i < numpts; i++) {
/* 470 */       double thisSine = Math.sin(-Math.toRadians(angle));
/* 471 */       double thisCosine = Math.cos(-Math.toRadians(angle));
/*     */ 
/* 473 */       double[] temp = mapDescriptor.pixelToWorld(
/* 474 */         new double[] { center[0] + width * (cosineAxis * thisCosine - sineAxis * thisSine), 
/* 475 */         center[1] + width * (sineAxis * thisCosine + cosineAxis * thisSine) });
/* 476 */       list.add(new Coordinate(temp[0], temp[1]));
/*     */ 
/* 478 */       angle += 1.0D;
/*     */     }
/* 480 */     return list;
/*     */   }
/*     */ 
/*     */   public static double getStartAngle(double a1, double a2) {
/* 484 */     if (a2 < a1) {
/* 485 */       if (a1 - a2 <= 180.0D) {
/* 486 */         return a2;
/*     */       }
/* 488 */       return a1;
/*     */     }
/*     */ 
/* 491 */     if (a2 - a1 <= 180.0D) {
/* 492 */       return a1;
/*     */     }
/* 494 */     return a2;
/*     */   }
/*     */ 
/*     */   public static double getAngExt(double a1, double a2)
/*     */   {
/* 500 */     double diff = a2 - a1;
/* 501 */     if (Math.abs(diff) <= 180.0D) return -Math.abs(diff);
/*     */ 
/* 503 */     return Math.abs(diff) - 360.0D;
/*     */   }
/*     */ 
/*     */   public static boolean getAFOSflg()
/*     */   {
/* 508 */     String sigmetFMT = System.getenv("SIGMETFMT");
/*     */ 
/* 510 */     if (sigmetFMT == null) return false;
/* 511 */     return "AFOS".equals(sigmetFMT);
/*     */   }
/*     */ 
/*     */   public static Map<String, Polygon> initFirPolygonMapFromShapfile() {
/* 515 */     Map result = new HashMap();
/* 516 */     IMapDescriptor mapDescriptor = (IMapDescriptor)PgenSession.getInstance().getPgenResource().getDescriptor();
/*     */ 
/* 518 */     HashMap firGeoMap = getGeometriesFromShapefile();
/*     */ 
/* 520 */     for (String firId : firGeoMap.keySet()) {
/* 521 */       Coordinate[] coors = (Coordinate[])firGeoMap.get(firId);
/* 522 */       result.put(firId, getPolygon(coors, mapDescriptor));
/*     */     }
/*     */ 
/* 526 */     return result;
/*     */   }
/*     */ 
/*     */   private static HashMap<String, Coordinate[]> getGeometriesFromShapefile()
/*     */   {
/* 534 */     String[] LABEL_ATTR = { "FIR_ID" };
/*     */ 
/* 536 */     FeatureIterator featureIterator = null;
/* 537 */     HashMap firGeoMap = new HashMap();
/* 538 */     IndexedShapefileDataStore shapefileDataStore = null;
/* 539 */     String shapeField = null;
/*     */     try
/*     */     {
/* 542 */       File file = PgenStaticDataProvider.getProvider().getFirBoundsFile();
/* 543 */       shapefileDataStore = new IndexedShapefileDataStore(file.toURI()
/* 544 */         .toURL(), null, false, true, IndexType.QIX);
/*     */ 
/* 546 */       shapeField = ((SimpleFeatureType)shapefileDataStore.getFeatureSource().getSchema()).getGeometryDescriptor().getLocalName();
/*     */     } catch (Exception e) {
/* 548 */       System.out.println("------- Exception: " + e.getMessage());
/*     */     }
/*     */ 
/* 551 */     String[] labelFields = LABEL_ATTR;
/*     */     try
/*     */     {
/* 555 */       String[] types = shapefileDataStore.getTypeNames();
/* 556 */       DefaultQuery query = new DefaultQuery();
/* 557 */       query.setTypeName(types[0]);
/*     */ 
/* 559 */       String[] fields = new String[labelFields.length + 1];
/* 560 */       for (int i = 0; i < labelFields.length; i++) {
/* 561 */         fields[i] = labelFields[i];
/*     */       }
/* 563 */       fields[labelFields.length] = shapeField;
/*     */ 
/* 565 */       query.setPropertyNames(fields);
/* 566 */       featureIterator = shapefileDataStore.getFeatureSource().getFeatures(query).features();
/*     */ 
/* 568 */       while (featureIterator.hasNext()) {
/* 569 */         SimpleFeature f = (SimpleFeature)featureIterator.next();
/* 570 */         Geometry g = (Geometry)f.getDefaultGeometry();
/* 571 */         firGeoMap.put(f.getAttribute("FIR_ID").toString(), g.getCoordinates());
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 575 */       System.out.println("---------Exception: " + e.getMessage()); } finally {
/* 576 */       if (featureIterator != null) featureIterator.close();
/*     */     }
/* 578 */     return firGeoMap;
/*     */   }
/*     */ 
/*     */   public static double[][] getESOLArea(Coordinate[] side1, Coordinate[] side2, IMapDescriptor map)
/*     */   {
/* 583 */     Coordinate[] sides = new Coordinate[side1.length + side2.length];
/*     */ 
/* 585 */     System.arraycopy(side1, 0, sides, 0, side1.length);
/*     */ 
/* 587 */     List list = Arrays.asList(side2);
/* 588 */     Collections.reverse(list);
/*     */ 
/* 590 */     System.arraycopy(list.toArray(new Coordinate[0]), 0, sides, side1.length, side2.length);
/*     */ 
/* 592 */     GeometryFactory gf = new GeometryFactory();
/* 593 */     return PgenUtil.latlonToPixel(gf.createPolygon(gf.createLinearRing(sides), null).getCoordinates(), map);
/*     */   }
/*     */ 
/*     */   public static boolean isSnapADC(AbstractDrawableComponent adc)
/*     */   {
/* 603 */     String pt = adc.getPgenType();
/* 604 */     return false;
/*     */   }
/*     */ 
/*     */   public static int getNumOfCompassPts(AbstractDrawableComponent adc)
/*     */   {
/* 614 */     String pt = adc.getPgenType();
/*     */ 
/* 618 */     return 16;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo
 * JD-Core Version:    0.6.2
 */