/*      */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.CoordinateList;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.GeometryCollection;
/*      */ import com.vividsolutions.jts.geom.GeometryFactory;
/*      */ import com.vividsolutions.jts.geom.LineString;
/*      */ import com.vividsolutions.jts.geom.LinearRing;
/*      */ import com.vividsolutions.jts.geom.MultiPolygon;
/*      */ import com.vividsolutions.jts.geom.Point;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
/*      */ import gov.noaa.nws.ncep.common.staticdata.CostalWater;
/*      */ import gov.noaa.nws.ncep.common.staticdata.FAArea;
/*      */ import gov.noaa.nws.ncep.common.staticdata.FARegion;
/*      */ import gov.noaa.nws.ncep.common.staticdata.GreatLake;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.common.staticdata.USState;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.viz.common.SnapUtil.SnapVOR;
/*      */ import java.awt.Color;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import org.dom4j.Document;
/*      */ import org.dom4j.Node;
/*      */ import org.dom4j.io.SAXReader;
/*      */ 
/*      */ public class GfaClip
/*      */ {
/*   93 */   private static GfaClip instance = new GfaClip();
/*      */   private static GeometryFactory geometryFactory;
/*  101 */   public final String DATABASE = "ncep";
/*      */ 
/*  104 */   public final String SCHEMA = "bounds";
/*      */ 
/*  106 */   public final String FA_REGION_TABLE = "fa_region";
/*  107 */   public final String FA_AREA_TABLE = "fa_area";
/*  108 */   public final String STATE_BNDS_TABLE = "statebnds";
/*  109 */   public final String GREATE_LAKE_BNDS_TABLE = "greatlakesbnds";
/*  110 */   public final String COASTAL_WATER_BNDS_TABLE = "airmetcstlbnds";
/*  111 */   public final String FA_AREAX_TABLE = "fa_areax";
/*      */ 
/*  118 */   private final String[] EXCLUDE_STATES = { "AK", "HI", "UM", "GU", "AS", "PR", "VI" };
/*      */ 
/*  121 */   private final String[] WRONG_STATES = { "HI", "VI", "ME", "VA", "MI", "MD", "MA", "AS", "AR", 
/*  122 */     "IL", "MN", "MS", "NJ", "PR", "AK", "AL", "TX", "NC", "ND", "NY", "OK", "OH", 
/*  123 */     "FL", "SD", "SC", "WI", "LA", "GU", "WA" };
/*      */   private Geometry faInternationalBound;
/*      */   private Geometry faInternationalBoundInGrid;
/*      */   private HashMap<String, Geometry> faRegionBounds;
/*      */   private HashMap<String, Geometry> faRegionBoundsInGrid;
/*      */   private HashMap<String, Geometry> faAreaBounds;
/*      */   private HashMap<String, Geometry> faAreaBoundsInGrid;
/*      */   private HashMap<String, Geometry> faAreaXBounds;
/*      */   private HashMap<String, Geometry> faAreaXBoundsInGrid;
/*      */   private HashMap<String, Geometry> stateBounds;
/*      */   private HashMap<String, Geometry> stateBoundsInGrid;
/*      */   private HashMap<String, Geometry> greatLakesBounds;
/*      */   private HashMap<String, Geometry> greatLakesBoundsInGrid;
/*      */   private HashMap<String, Geometry> coastalWaterBounds;
/*      */   private HashMap<String, Geometry> coastalWaterBoundsInGrid;
/*      */   private HashMap<String, Geometry> faRegionCommBounds;
/*      */   private HashMap<String, Geometry> faRegionCommBoundsInGrid;
/*      */   private HashMap<String, Geometry> faAreaXCommBounds;
/*      */   private HashMap<String, Geometry> faAreaXCommBoundsInGrid;
/*  181 */   private static Document mtObscTbl = null;
/*      */ 
/*  184 */   public static String MTOBSC_XPATH = "/MT_OBSC";
/*      */ 
/*  187 */   private static List<String> mtObscStates = null;
/*      */   private static final int NEW_POINT = 2;
/*      */   private static final int BOUND_POINT = 1;
/*      */   private static final int ORIGINAL_POINT = 0;
/*      */   private static final double SMALLF = 0.01D;
/*      */ 
/*      */   private GfaClip()
/*      */   {
/*  202 */     geometryFactory = new GeometryFactory();
/*      */   }
/*      */ 
/*      */   public static GfaClip getInstance()
/*      */   {
/*  211 */     return instance;
/*      */   }
/*      */ 
/*      */   public ArrayList<Gfa> simpleclip(Gfa smear)
/*      */   {
/*  224 */     ArrayList list = new ArrayList();
/*  225 */     for (String key : getFaRegionBounds().keySet()) {
/*  226 */       Geometry region = (Geometry)this.faRegionBounds.get(key);
/*      */ 
/*  228 */       Polygon polygon = gfaToPolygon(smear);
/*      */ 
/*  230 */       Geometry intersection = null;
/*  231 */       if (region.intersects(polygon)) {
/*  232 */         intersection = region.intersection(polygon);
/*  233 */         if ((intersection instanceof MultiPolygon)) {
/*  234 */           MultiPolygon mp = (MultiPolygon)intersection;
/*  235 */           for (int i = 0; i < mp.getNumGeometries(); i++) {
/*  236 */             Gfa g = geometryToGfa(smear, mp.getGeometryN(i));
/*  237 */             g.addNotToBeSnapped(region.getCoordinates());
/*  238 */             list.add(g);
/*      */           }
/*      */         } else {
/*  241 */           Gfa g = geometryToGfa(smear, intersection);
/*  242 */           g.addNotToBeSnapped(region.getCoordinates());
/*  243 */           list.add(g);
/*      */         }
/*  245 */       } else if (region.covers(polygon)) {
/*  246 */         smear.addNotToBeSnapped(region.getCoordinates());
/*  247 */         list.add(smear);
/*      */       }
/*      */     }
/*      */ 
/*  251 */     return list;
/*      */   }
/*      */ 
/*      */   public Polygon gfaToPolygon(Gfa gfa)
/*      */   {
/*  262 */     if (gfa != null) {
/*  263 */       return pointsToPolygon(gfa.getLinePoints());
/*      */     }
/*      */ 
/*  266 */     return null;
/*      */   }
/*      */ 
/*      */   public Polygon gfaToPolygonInGrid(Gfa gfa)
/*      */   {
/*  277 */     if (gfa != null) {
/*  278 */       return pointsToPolygon(PgenUtil.latlonToGrid(gfa.getLinePoints()));
/*      */     }
/*      */ 
/*  281 */     return null;
/*      */   }
/*      */ 
/*      */   public Polygon pointsToPolygon(Coordinate[] points)
/*      */   {
/*  296 */     Coordinate[] coords = (Coordinate[])Arrays.copyOf(points, points.length + 1);
/*  297 */     coords[(coords.length - 1)] = coords[0];
/*      */ 
/*  299 */     CoordinateArraySequence cas = new CoordinateArraySequence(coords);
/*  300 */     LinearRing ring = new LinearRing(cas, geometryFactory);
/*      */ 
/*  302 */     Polygon polygon = new Polygon(ring, null, geometryFactory);
/*      */ 
/*  304 */     return polygon;
/*      */   }
/*      */ 
/*      */   public Geometry pointsToGeometry(Coordinate[] points)
/*      */   {
/*      */     Geometry geom;
/*      */     Geometry geom;
/*  316 */     if ((points == null) || (points.length == 0)) {
/*  317 */       geom = null;
/*      */     }
/*      */     else
/*      */     {
/*      */       Geometry geom;
/*  319 */       if (points.length == 1) {
/*  320 */         CoordinateArraySequence cas = new CoordinateArraySequence(points);
/*  321 */         geom = new Point(cas, geometryFactory);
/*      */       }
/*      */       else
/*      */       {
/*      */         Geometry geom;
/*  323 */         if (points.length == 2) {
/*  324 */           geom = pointsToLineString(points);
/*      */         }
/*      */         else
/*  327 */           geom = pointsToPolygon(points);
/*      */       }
/*      */     }
/*  330 */     return geom;
/*      */   }
/*      */ 
/*      */   public Geometry pointsToGeometry(ArrayList<Coordinate> pts)
/*      */   {
/*  341 */     Coordinate[] points = new Coordinate[pts.size()];
/*  342 */     pts.toArray(points);
/*      */ 
/*  344 */     return pointsToGeometry(points);
/*      */   }
/*      */ 
/*      */   public Geometry pointsToLineString(Coordinate[] points)
/*      */   {
/*  356 */     CoordinateArraySequence cas = new CoordinateArraySequence(points);
/*      */ 
/*  358 */     return new LineString(cas, geometryFactory);
/*      */   }
/*      */ 
/*      */   public Gfa geometryToGfa(Gfa gfaIn, Geometry geomIn)
/*      */   {
/*  372 */     Coordinate[] c = geomIn.getCoordinates();
/*  373 */     ArrayList coor = new ArrayList();
/*      */ 
/*  375 */     coor.addAll(Arrays.asList(c));
/*  376 */     coor.remove(coor.size() - 1);
/*      */ 
/*  378 */     Gfa g = gfaIn.copy();
/*      */ 
/*  380 */     g.setPoints(coor);
/*  381 */     g.setGfaTextCoordinate(g.getCentroid());
/*      */ 
/*  383 */     return g;
/*      */   }
/*      */ 
/*      */   private void readFaRegionBounds()
/*      */     throws VizException
/*      */   {
/*  392 */     this.faRegionBounds = new HashMap();
/*  393 */     for (FARegion fa : PgenStaticDataProvider.getProvider().getFARegions()) {
/*  394 */       this.faRegionBounds.put(fa.getRegion(), fa.getGeometry());
/*      */     }
/*  396 */     loadFaRegionCommBounds();
/*      */   }
/*      */ 
/*      */   private void readFaAreaBounds()
/*      */     throws VizException
/*      */   {
/*  405 */     this.faAreaBounds = new HashMap();
/*  406 */     for (FAArea fa : PgenStaticDataProvider.getProvider().getFAAreas())
/*  407 */       this.faAreaBounds.put(fa.getArea(), fa.getGeometry());
/*      */   }
/*      */ 
/*      */   private void readFaAreaXBounds()
/*      */     throws VizException
/*      */   {
/*  417 */     this.faAreaXBounds = new HashMap();
/*  418 */     for (FAArea fa : PgenStaticDataProvider.getProvider().getFAAreaX())
/*  419 */       this.faAreaXBounds.put(fa.getArea(), fa.getGeometry());
/*      */   }
/*      */ 
/*      */   private void readStateBounds()
/*      */     throws VizException
/*      */   {
/*  442 */     HashMap originalStateBounds = new HashMap();
/*      */ 
/*  444 */     for (USState st : PgenStaticDataProvider.getProvider().getAllstates()) {
/*  445 */       originalStateBounds.put(st.getStateAbrv(), st.getShape());
/*      */     }
/*      */ 
/*  449 */     for (String s : this.EXCLUDE_STATES) {
/*  450 */       originalStateBounds.remove(s);
/*      */     }
/*      */ 
/*  453 */     this.stateBounds = fixStateBounds(originalStateBounds);
/*      */   }
/*      */ 
/*      */   private void readGreatLakeBounds()
/*      */     throws VizException
/*      */   {
/*  463 */     this.greatLakesBounds = new HashMap();
/*  464 */     for (GreatLake lake : PgenStaticDataProvider.getProvider().getGreatLakes())
/*  465 */       this.greatLakesBounds.put(lake.getId(), lake.getGeometry());
/*      */   }
/*      */ 
/*      */   private void readCoastalWaterBounds()
/*      */     throws VizException
/*      */   {
/*  475 */     this.coastalWaterBounds = new HashMap();
/*  476 */     for (CostalWater water : PgenStaticDataProvider.getProvider().getCostalWaters())
/*  477 */       this.coastalWaterBounds.put(water.getId(), water.getGeometry());
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getFaRegionBounds()
/*      */   {
/*  486 */     if (this.faRegionBounds == null) {
/*      */       try {
/*  488 */         readFaRegionBounds();
/*      */       }
/*      */       catch (VizException e) {
/*  491 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  495 */     return this.faRegionBounds;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getFaAreaBounds()
/*      */   {
/*  503 */     if (this.faAreaBounds == null) {
/*      */       try {
/*  505 */         readFaAreaBounds();
/*      */       }
/*      */       catch (VizException e) {
/*  508 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  512 */     return this.faAreaBounds;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getFaAreaXBounds()
/*      */   {
/*  520 */     if (this.faAreaXBounds == null) {
/*      */       try {
/*  522 */         readFaAreaXBounds();
/*      */       }
/*      */       catch (VizException e) {
/*  525 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  529 */     return this.faAreaXBounds;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getStateBounds()
/*      */   {
/*  538 */     if (this.stateBounds == null) {
/*      */       try {
/*  540 */         readStateBounds();
/*      */       }
/*      */       catch (VizException e) {
/*  543 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  547 */     return this.stateBounds;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getGreatLakeBounds()
/*      */   {
/*  555 */     if (this.greatLakesBounds == null) {
/*      */       try {
/*  557 */         readGreatLakeBounds();
/*      */       }
/*      */       catch (VizException e) {
/*  560 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  564 */     return this.greatLakesBounds;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getCoastalWaterBounds()
/*      */   {
/*  572 */     if (this.coastalWaterBounds == null) {
/*      */       try {
/*  574 */         readCoastalWaterBounds();
/*      */       }
/*      */       catch (VizException e) {
/*  577 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  581 */     return this.coastalWaterBounds;
/*      */   }
/*      */ 
/*      */   public Geometry getFaInternationalBound()
/*      */   {
/*  590 */     if (this.faInternationalBound == null) {
/*  591 */       ArrayList rbnds = new ArrayList(getFaRegionBounds().values());
/*  592 */       this.faInternationalBound = quickUnion(rbnds);
/*      */     }
/*      */ 
/*  595 */     return this.faInternationalBound;
/*      */   }
/*      */ 
/*      */   public Geometry quickUnion(ArrayList<Geometry> geoms)
/*      */   {
/*  611 */     if (geoms.size() <= 0) return null;
/*      */ 
/*  613 */     Geometry[] regs = new Geometry[geoms.size()];
/*  614 */     regs = (Geometry[])geoms.toArray(regs);
/*      */ 
/*  616 */     GeometryCollection gc = new GeometryCollection(regs, geometryFactory);
/*      */ 
/*  618 */     return gc.buffer(0.0D);
/*      */   }
/*      */ 
/*      */   public ArrayList<Gfa> clipFARegions(Gfa smear, ArrayList<Gfa> snapshots)
/*      */   {
/*  659 */     ArrayList clippedList = new ArrayList();
/*  660 */     if (smear.getGfaHazard().equals("FZLVL")) {
/*  661 */       return clippedList;
/*      */     }
/*      */ 
/*  667 */     if (!isBiggerInMap(gfaToPolygon(smear))) {
/*  668 */       return clippedList;
/*      */     }
/*      */ 
/*  674 */     boolean _clippingFlg = true;
/*      */ 
/*  676 */     Geometry intlBound = getFaInternationalBound();
/*  677 */     Geometry intlBoundInGrid = getFaInternationalBoundInGrid();
/*      */ 
/*  679 */     Polygon smearPoly = gfaToPolygon(smear);
/*  680 */     Polygon smearPolyInGrid = pointsToPolygon(PgenUtil.latlonToGrid(smear.getLinePoints()));
/*      */ 
/*  682 */     Geometry clipAgstIntlBnd = null;
/*      */ 
/*  684 */     if (smearPolyInGrid.intersects(intlBoundInGrid)) {
/*  685 */       clipAgstIntlBnd = smearPolyInGrid.intersection(intlBoundInGrid);
/*      */     }
/*      */ 
/*  688 */     if ((clipAgstIntlBnd == null) || (clipAgstIntlBnd.getNumGeometries() <= 0) || 
/*  689 */       (!isBiggerInGrid(clipAgstIntlBnd))) {
/*  690 */       return clippedList;
/*      */     }
/*      */ 
/*  699 */     HashMap intlPts = getIntlIntersectionPt(smearPoly, intlBound, clipAgstIntlBnd);
/*      */ 
/*  701 */     HashMap regionInterPts = getRegionIntersectionPt(smearPoly);
/*      */ 
/*  707 */     HashMap rgbnds = getFaRegionBounds();
/*  708 */     HashMap rgbndsInGrid = getFaRegionBoundsInGrid();
/*      */ 
/*  710 */     CoordinateList nonReduceable = new CoordinateList();
/*  711 */     nonReduceable.addAll(intlPts.values(), false);
/*  712 */     nonReduceable.addAll(regionInterPts.values(), false);
/*  713 */     for (Geometry gb : rgbnds.values()) {
/*  714 */       nonReduceable.addAll(Arrays.asList(gb.getCoordinates()), false);
/*      */     }
/*      */ 
/*  719 */     HashMap replacePts = new HashMap();
/*  720 */     replacePts.putAll(intlPts);
/*  721 */     replacePts.putAll(regionInterPts);
/*      */ 
/*  731 */     HashMap clipWithRegions = new HashMap();
/*      */ 
/*  733 */     ArrayList smallPoly = new ArrayList();
/*      */ 
/*  735 */     for (String regionName : rgbnds.keySet())
/*      */     {
/*  737 */       clipWithRegions.put(regionName, new ArrayList());
/*      */ 
/*  740 */       Geometry startPoly = intlBoundInGrid.intersection(smearPolyInGrid);
/*      */ 
/*  743 */       if (_clippingFlg)
/*      */       {
/*  745 */         Geometry regionBnd = (Geometry)rgbndsInGrid.get(regionName);
/*      */ 
/*  747 */         if (regionBnd.intersects(startPoly)) {
/*  748 */           Geometry regionPoly = regionBnd.intersection(startPoly);
/*      */ 
/*  750 */           if (regionPoly != null)
/*      */           {
/*  752 */             for (int kk = 0; kk < regionPoly.getNumGeometries(); kk++) {
/*  753 */               Geometry bigPoly = regionPoly.getGeometryN(kk);
/*      */ 
/*  755 */               if (isBiggerInGrid(bigPoly)) {
/*  756 */                 ((ArrayList)clipWithRegions.get(regionName)).add(bigPoly);
/*      */               }
/*      */               else {
/*  759 */                 smallPoly.add(bigPoly);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  781 */     for (String regionName : clipWithRegions.keySet())
/*      */     {
/*  783 */       ArrayList bigs = (ArrayList)clipWithRegions.get(regionName);
/*      */ 
/*  785 */       ArrayList toBeUnioned = new ArrayList();
/*  786 */       toBeUnioned.addAll(bigs);
/*  787 */       toBeUnioned.addAll(smallPoly);
/*  788 */       Geometry result = quickUnion(toBeUnioned);
/*      */ 
/*  790 */       ArrayList commPts = getCommonPoints(bigs, smallPoly);
/*      */ 
/*  792 */       if (result != null) {
/*  793 */         for (int kk = 0; kk < result.getNumGeometries(); kk++)
/*      */         {
/*  795 */           Geometry one = result.getGeometryN(kk);
/*      */ 
/*  801 */           Geometry onePart = removeCommonPoints(one, commPts);
/*      */ 
/*  806 */           Coordinate[] gPts = PgenUtil.gridToLatlon(onePart.getCoordinates());
/*  807 */           ArrayList points = new ArrayList();
/*  808 */           for (Coordinate c : gPts) {
/*  809 */             points.add(c);
/*      */           }
/*  811 */           points.remove(points.size() - 1);
/*      */ 
/*  813 */           Geometry cleanPts = cleanupPoints(pointsToGeometry(points));
/*      */ 
/*  815 */           if (isAddableAsSmear(cleanPts, snapshots, smear))
/*      */           {
/*  818 */             Geometry rplPts = replacePts(cleanPts, replacePts);
/*      */ 
/*  821 */             Geometry finalPts = cleanupPoints(rplPts);
/*      */ 
/*  823 */             if (finalPts != null)
/*      */             {
/*  825 */               Gfa newElm = geometryToGfa(smear, finalPts);
/*      */ 
/*  828 */               addReduceFlags(newElm, nonReduceable);
/*      */ 
/*  830 */               newElm.addAttribute("FA_REGION", new String(regionName));
/*      */ 
/*  832 */               clippedList.add(newElm);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  840 */     return clippedList;
/*      */   }
/*      */ 
/*      */   public boolean isAddableAsSmear(Geometry g, ArrayList<Gfa> snapshots, Gfa smear)
/*      */   {
/*  862 */     return (isBiggerInMap(g)) && 
/*  863 */       (passed6_6Rule(g, snapshots, smear)) && 
/*  864 */       (intersectBigWithSS(g, snapshots));
/*      */   }
/*      */ 
/*      */   private boolean intersectBigWithSS(Geometry g, ArrayList<Gfa> snapshots)
/*      */   {
/*  880 */     boolean addable = false;
/*      */ 
/*  882 */     if ((snapshots == null) || (snapshots.size() <= 0)) {
/*  883 */       addable = true;
/*      */     }
/*      */     else {
/*  886 */       Coordinate[] pts = PgenUtil.latlonToGrid(g.getCoordinates());
/*  887 */       Geometry poly = pointsToGeometry(pts);
/*  888 */       for (Gfa ss : snapshots) {
/*  889 */         if (isBiggerInGrid(poly.intersection(gfaToPolygonInGrid(ss)))) {
/*  890 */           addable = true;
/*  891 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  896 */     return addable;
/*      */   }
/*      */ 
/*      */   private boolean passed6_6Rule(Geometry g, ArrayList<Gfa> snapshots, Gfa smear)
/*      */   {
/*  912 */     boolean addable = true;
/*      */ 
/*  914 */     if ((snapshots != null) && (snapshots.size() > 0) && 
/*  915 */       (smear.getForecastHours().equals("6-6")))
/*      */     {
/*  917 */       Gfa ss_6 = null;
/*      */ 
/*  919 */       for (Gfa ss : snapshots) {
/*  920 */         if (ss.getForecastHours().equals("6")) {
/*  921 */           ss_6 = ss;
/*  922 */           break;
/*      */         }
/*      */       }
/*      */ 
/*  926 */       if (ss_6 == null) {
/*  927 */         addable = false;
/*      */       }
/*      */ 
/*  930 */       if (addable) {
/*  931 */         Coordinate[] pts = PgenUtil.latlonToGrid(g.getCoordinates());
/*  932 */         Geometry poly = pointsToGeometry(pts);
/*  933 */         if (!isBiggerInGrid(poly.intersection(gfaToPolygonInGrid(ss_6)))) {
/*  934 */           addable = false;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  940 */     return addable;
/*      */   }
/*      */ 
/*      */   private boolean isBigger(Geometry g, double limit)
/*      */   {
/*  950 */     return (g != null) && (PgenUtil.getSphPolyArea(g) >= limit);
/*      */   }
/*      */ 
/*      */   public boolean isBiggerInMap(Geometry g)
/*      */   {
/*  961 */     return isBigger(g, 3000.0D);
/*      */   }
/*      */ 
/*      */   public boolean isBiggerInGrid(Geometry g)
/*      */   {
/*  975 */     double area = 0.0D;
/*  976 */     if ((g instanceof Polygon)) {
/*  977 */       area = PgenUtil.getSphPolyAreaInGrid((Polygon)g);
/*  978 */     } else if ((g instanceof MultiPolygon)) {
/*  979 */       MultiPolygon mp = (MultiPolygon)g;
/*  980 */       for (int nn = 0; nn < mp.getNumGeometries(); nn++) {
/*  981 */         area += PgenUtil.getSphPolyAreaInGrid((Polygon)mp.getGeometryN(nn));
/*      */       }
/*      */     }
/*      */ 
/*  985 */     return area >= 3000.0D;
/*      */   }
/*      */ 
/*      */   public List<String> getMtObscStates()
/*      */   {
/*  997 */     if (mtObscStates == null) {
/*  998 */       mtObscStates = new ArrayList();
/*  999 */       String xpath = MTOBSC_XPATH;
/*      */ 
/* 1001 */       Document dm = readMtObscTbl();
/*      */ 
/* 1003 */       if (dm != null) {
/* 1004 */         Node mtObscInfo = dm.selectSingleNode(xpath);
/* 1005 */         List nodes = mtObscInfo.selectNodes("area");
/*      */         Iterator localIterator2;
/* 1006 */         for (Iterator localIterator1 = nodes.iterator(); localIterator1.hasNext(); 
/* 1008 */           localIterator2.hasNext())
/*      */         {
/* 1006 */           Node node = (Node)localIterator1.next();
/* 1007 */           node.selectNodes("state");
/* 1008 */           localIterator2 = node.selectNodes("state").iterator(); continue; Object nd = localIterator2.next();
/* 1009 */           mtObscStates.add(((Node)nd).getText());
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1015 */     return mtObscStates;
/*      */   }
/*      */ 
/*      */   private static Document readMtObscTbl()
/*      */   {
/* 1024 */     if (mtObscTbl == null) {
/*      */       try {
/* 1026 */         String mtObscFile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/* 1027 */           PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "mt_obsc_states.xml");
/*      */ 
/* 1029 */         SAXReader reader = new SAXReader();
/* 1030 */         mtObscTbl = reader.read(mtObscFile);
/*      */       } catch (Exception e) {
/* 1032 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/* 1036 */     return mtObscTbl;
/*      */   }
/*      */ 
/*      */   private HashMap<Coordinate, Coordinate> getIntlIntersectionPt(Geometry gfaPoly, Geometry bndPoly, Geometry clipped)
/*      */   {
/* 1058 */     HashMap interPts = new HashMap();
/*      */ 
/* 1061 */     Coordinate[] pts = clipped.getGeometryN(0).getCoordinates();
/* 1062 */     Coordinate[] intlPoly = PgenUtil.gridToLatlon(pts);
/* 1063 */     ArrayList intlList = new ArrayList();
/* 1064 */     intlList.addAll(Arrays.asList(intlPoly));
/* 1065 */     intlList.remove(intlList.size() - 1);
/* 1066 */     ArrayList cwPoly = GfaSnap.getInstance().reorderInClockwise(intlList, null);
/*      */ 
/* 1072 */     int[] ptFlag = new int[cwPoly.size()];
/* 1073 */     for (int ii = 0; ii < cwPoly.size(); ii++)
/*      */     {
/* 1075 */       ptFlag[ii] = 2;
/*      */ 
/* 1077 */       for (Coordinate bndc : bndPoly.getCoordinates()) {
/* 1078 */         if (isSamePoint(bndc, (Coordinate)cwPoly.get(ii), 0.01D)) {
/* 1079 */           ptFlag[ii] = 1;
/* 1080 */           break;
/*      */         }
/*      */       }
/*      */ 
/* 1084 */       if (ptFlag[ii] == 2)
/*      */       {
/* 1086 */         for (Coordinate orgc : gfaPoly.getCoordinates()) {
/* 1087 */           if (isSamePoint(orgc, (Coordinate)cwPoly.get(ii), 0.01D)) {
/* 1088 */             ptFlag[ii] = 0;
/* 1089 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1096 */     while (ptFlag[0] != 0)
/*      */     {
/* 1098 */       cwPoly = GfaSnap.getInstance().shiftArray(cwPoly);
/*      */ 
/* 1100 */       int eflag = ptFlag[0];
/* 1101 */       for (int jj = 0; jj < ptFlag.length - 1; jj++) {
/* 1102 */         ptFlag[jj] = ptFlag[(jj + 1)];
/*      */       }
/*      */ 
/* 1105 */       ptFlag[(ptFlag.length - 1)] = eflag;
/*      */     }
/*      */ 
/* 1114 */     int np = cwPoly.size();
/* 1115 */     Coordinate[] snapPt = new Coordinate[1];
/*      */ 
/* 1117 */     ArrayList usedPts = new ArrayList();
/* 1118 */     usedPts.addAll(cwPoly);
/*      */ 
/* 1120 */     for (int ii = 0; ii < cwPoly.size(); ii++)
/*      */     {
/* 1122 */       if (ptFlag[ii] == 2)
/*      */       {
/* 1131 */         if ((ii + 1 < cwPoly.size()) && 
/* 1132 */           (ptFlag[(ii + 1)] == 2) && 
/* 1133 */           (GfaSnap.getInstance().isCluster((Coordinate)cwPoly.get(ii), (Coordinate)cwPoly.get(ii + 1))))
/*      */         {
/* 1135 */           int ptBefore = (ii - 1 + cwPoly.size()) % cwPoly.size();
/* 1136 */           int ptAfter = (ii + 2 + cwPoly.size()) % cwPoly.size();
/*      */ 
/* 1138 */           List gfaPts = Arrays.asList(gfaPoly.getCoordinates());
/* 1139 */           int indInGfa1 = findPoint((Coordinate)cwPoly.get(ptBefore), gfaPts, 0.01D);
/* 1140 */           int indInGfa2 = findPoint((Coordinate)cwPoly.get(ptAfter), gfaPts, 0.01D);
/*      */           int snap_indx1;
/*      */           int snap_indx2;
/*      */           int status;
/*      */           int jj;
/* 1142 */           if ((indInGfa1 >= 0) && (indInGfa2 >= 0) && 
/* 1143 */             (indInGfa2 - indInGfa1 == 2))
/*      */           {
/* 1145 */             int indBtw = indInGfa1 + 1;
/* 1146 */             if ((GfaSnap.getInstance().isCluster((Coordinate)cwPoly.get(ii), (Coordinate)gfaPts.get(indBtw))) && 
/* 1147 */               (GfaSnap.getInstance().isCluster((Coordinate)cwPoly.get(ii + 1), (Coordinate)gfaPts.get(indBtw))))
/*      */             {
/* 1149 */               interPts.put((Coordinate)cwPoly.get(ii), (Coordinate)gfaPts.get(indBtw));
/* 1150 */               interPts.put((Coordinate)cwPoly.get(ii + 1), (Coordinate)gfaPts.get(indBtw));
/*      */ 
/* 1152 */               ii++;
/* 1153 */               continue;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1166 */         snap_indx1 = ii;
/* 1167 */         snap_indx2 = ii;
/*      */         do
/*      */         {
/* 1172 */           snap_indx1--;
/*      */ 
/* 1169 */           if (snap_indx1 <= 0) break;
/*      */         }
/* 1171 */         while (GfaSnap.getInstance().isCluster((Coordinate)cwPoly.get(ii), 
/* 1171 */           (Coordinate)cwPoly.get(snap_indx1 - 1)));
/*      */ 
/* 1175 */         while ((snap_indx2 < np - 1) && 
/* 1177 */           (GfaSnap.getInstance().isCluster((Coordinate)cwPoly.get(ii), 
/* 1177 */           (Coordinate)cwPoly.get(snap_indx2 + 1)))) {
/* 1178 */           snap_indx2++;
/*      */         }
/*      */ 
/* 1181 */         status = GfaSnap.getInstance().snapPtGFA(snap_indx1, snap_indx2, 
/* 1182 */           usedPts, null, cwPoly, true, true, 3.0D, snapPt);
/*      */ 
/* 1185 */         if ((status != 0) && (snap_indx1 != snap_indx2))
/*      */         {
/* 1187 */           snap_indx1 = ii;
/* 1188 */           snap_indx2 = ii;
/*      */ 
/* 1190 */           status = GfaSnap.getInstance().snapPtGFA(snap_indx1, snap_indx2, 
/* 1191 */             usedPts, null, cwPoly, true, true, 3.0D, snapPt);
/*      */         }
/*      */ 
/* 1195 */         if (status != 0) {
/* 1196 */           snapPt[0].x = ((Coordinate)cwPoly.get(ii)).x;
/* 1197 */           snapPt[0].y = ((Coordinate)cwPoly.get(ii)).y;
/*      */         }
/*      */ 
/* 1205 */         for (jj = snap_indx1; jj <= snap_indx2; jj++) {
/* 1206 */           interPts.put((Coordinate)cwPoly.get(jj), snapPt[0]);
/*      */         }
/*      */ 
/* 1210 */         ii += snap_indx2 - snap_indx1;
/*      */ 
/* 1212 */         usedPts.add(snapPt[0]);
/*      */       }
/*      */     }
/*      */ 
/* 1216 */     return interPts;
/*      */   }
/*      */ 
/*      */   private boolean isSamePoint(Coordinate p1, Coordinate p2, double tieDist)
/*      */   {
/* 1226 */     boolean sameP = false;
/* 1227 */     if ((p1 != null) && (p2 != null) && 
/* 1228 */       (Math.abs(p1.x - p2.x) < Math.abs(tieDist)) && 
/* 1229 */       (Math.abs(p1.y - p2.y) < Math.abs(tieDist))) {
/* 1230 */       sameP = true;
/*      */     }
/*      */ 
/* 1233 */     return sameP;
/*      */   }
/*      */ 
/*      */   private HashMap<Coordinate, Coordinate> getRegionIntersectionPt(Geometry gfaPoly)
/*      */   {
/* 1250 */     HashMap interPtsPair = new HashMap();
/*      */ 
/* 1252 */     ArrayList interPts = new ArrayList();
/*      */ 
/* 1254 */     ArrayList interIndex = new ArrayList();
/* 1255 */     ArrayList indx = new ArrayList();
/*      */ 
/* 1258 */     ArrayList gfaPoints = new ArrayList();
/* 1259 */     for (Coordinate c : gfaPoly.getCoordinates()) {
/* 1260 */       gfaPoints.add(c);
/*      */     }
/*      */ 
/* 1263 */     ArrayList cwGfaPts = GfaSnap.getInstance().reorderInClockwise(gfaPoints, null);
/*      */ 
/* 1266 */     HashMap regionCommBnds = getFaRegionCommBounds();
/* 1267 */     for (??? = regionCommBnds.values().iterator(); ((Iterator)???).hasNext(); ) { Geometry bnd = (Geometry)((Iterator)???).next();
/* 1268 */       ArrayList pts = lineIntersect((Coordinate[])cwGfaPts.toArray(new Coordinate[cwGfaPts.size()]), 
/* 1269 */         bnd.getCoordinates(), indx);
/*      */ 
/* 1271 */       if (pts.size() > 0) {
/* 1272 */         interPts.addAll(pts);
/* 1273 */         interIndex.addAll(indx);
/*      */       }
/*      */     }
/*      */ 
/* 1277 */     if (interPts.size() <= 0) {
/* 1278 */       return interPtsPair;
/*      */     }
/*      */ 
/* 1308 */     ArrayList checkPoints = new ArrayList();
/* 1309 */     Geometry centralBnd = (Geometry)getFaRegionBounds().get("CENTRAL");
/* 1310 */     for (Coordinate c : centralBnd.getCoordinates()) {
/* 1311 */       Geometry pp = pointsToGeometry(new Coordinate[] { c });
/* 1312 */       if (pp.within(gfaPoly)) {
/* 1313 */         checkPoints.add(c);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1335 */     ArrayList usedPoints = new ArrayList();
/* 1336 */     usedPoints.addAll(cwGfaPts);
/*      */ 
/* 1338 */     ArrayList ePts = new ArrayList();
/* 1339 */     for (int ii = 0; ii < interPts.size(); ii++)
/*      */     {
/* 1341 */       Coordinate interP = (Coordinate)interPts.get(ii);
/* 1342 */       Coordinate ptBefore = (Coordinate)cwGfaPts.get(((Integer)interIndex.get(ii)).intValue());
/* 1343 */       Coordinate ptAfter = (Coordinate)cwGfaPts.get(((Integer)interIndex.get(ii)).intValue() + 1);
/*      */ 
/* 1345 */       double qdist1 = GfaSnap.getInstance().distance(interP, ptBefore);
/* 1346 */       double qdist2 = GfaSnap.getInstance().distance(interP, ptAfter);
/*      */ 
/* 1348 */       int addOne = -1;
/* 1349 */       int qmatch = -1;
/*      */       double qdist;
/*      */       double qdist;
/* 1352 */       if (qdist1 < qdist2) {
/* 1353 */         qmatch = ((Integer)interIndex.get(ii)).intValue();
/* 1354 */         qdist = qdist1;
/*      */       }
/*      */       else {
/* 1357 */         qmatch = ((Integer)interIndex.get(ii)).intValue() + 1;
/* 1358 */         qdist = qdist2;
/*      */       }
/*      */ 
/* 1361 */       if (qdist / 1852.0D < GfaSnap.CLUSTER_DIST)
/*      */       {
/* 1363 */         Coordinate ptMatch = (Coordinate)cwGfaPts.get(qmatch);
/* 1364 */         for (Coordinate c : checkPoints) {
/* 1365 */           if (GfaSnap.getInstance().isCluster(c, ptMatch)) {
/* 1366 */             addOne = qmatch;
/* 1367 */             break;
/*      */           }
/*      */         }
/*      */ 
/* 1371 */         if (addOne < 0) {
/* 1372 */           interPtsPair.put(new Coordinate(interP), 
/* 1373 */             new Coordinate(ptMatch));
/* 1374 */           continue;
/*      */         }
/*      */       }
/*      */       int kk;
/*      */       int kk2;
/* 1380 */       if (addOne >= 0) {
/* 1381 */         int kk = addOne;
/* 1382 */         int kk2 = kk;
/*      */ 
/* 1384 */         ePts.clear();
/*      */ 
/* 1387 */         if (addOne == cwGfaPts.size() - 1) {
/* 1388 */           ePts.addAll(GfaSnap.getInstance().insertArray(cwGfaPts, ((Integer)interIndex.get(ii)).intValue() + 1, interP));
/* 1389 */           kk = ((Integer)interIndex.get(ii)).intValue() + 1;
/* 1390 */           kk2 = kk;
/*      */         }
/*      */         else {
/* 1393 */           ePts.addAll(cwGfaPts);
/*      */         }
/*      */       }
/*      */       else {
/* 1397 */         ePts.clear();
/* 1398 */         ePts.addAll(GfaSnap.getInstance().insertArray(cwGfaPts, ((Integer)interIndex.get(ii)).intValue() + 1, interP));
/* 1399 */         kk = ((Integer)interIndex.get(ii)).intValue() + 1;
/* 1400 */         kk2 = kk;
/*      */       }
/*      */ 
/* 1404 */       ePts.remove(ePts.size() - 1);
/*      */ 
/* 1406 */       Coordinate[] snapped = new Coordinate[1];
/*      */ 
/* 1409 */       int status = GfaSnap.getInstance().snapPtGFA(kk, kk2, usedPoints, checkPoints, 
/* 1410 */         ePts, true, true, 3.0D, snapped);
/*      */ 
/* 1412 */       if (status != 0) {
/* 1413 */         if (addOne >= 0) {
/* 1414 */           snapped[0] = new Coordinate((Coordinate)ePts.get(kk));
/*      */         }
/*      */         else {
/* 1417 */           snapped[0] = new Coordinate(interP);
/*      */         }
/*      */       }
/*      */ 
/* 1421 */       interPtsPair.put(new Coordinate(interP), 
/* 1422 */         new Coordinate(snapped[0]));
/*      */ 
/* 1424 */       if (addOne >= 0) {
/* 1425 */         interPtsPair.put(new Coordinate((Coordinate)cwGfaPts.get(addOne)), 
/* 1426 */           new Coordinate(snapped[0]));
/*      */       }
/*      */ 
/* 1429 */       usedPoints.add(snapped[0]);
/*      */     }
/*      */ 
/* 1435 */     return interPtsPair;
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> lineIntersect(Coordinate[] line1, Coordinate[] line2, ArrayList<Integer> indexInLine1)
/*      */   {
/* 1453 */     ArrayList interPts = new ArrayList();
/* 1454 */     if (indexInLine1 == null) {
/* 1455 */       indexInLine1 = new ArrayList();
/*      */     }
/*      */     else {
/* 1458 */       indexInLine1.clear();
/*      */     }
/*      */ 
/* 1461 */     Coordinate[] line1InGrid = PgenUtil.latlonToGrid(line1);
/* 1462 */     Coordinate[] line2InGrid = PgenUtil.latlonToGrid(line2);
/*      */ 
/* 1465 */     for (int ii = 0; ii < line1.length - 1; ii++) {
/* 1466 */       for (int jj = 0; jj < line2.length - 1; jj++)
/*      */       {
/* 1468 */         Coordinate onePt = segmentIntersect(line1InGrid[ii], line1InGrid[(ii + 1)], 
/* 1469 */           line2InGrid[jj], line2InGrid[(jj + 1)]);
/* 1470 */         if (onePt != null) {
/* 1471 */           Coordinate[] onePtInMap = PgenUtil.gridToLatlon(new Coordinate[] { onePt });
/* 1472 */           interPts.add(new Coordinate(onePtInMap[0]));
/* 1473 */           indexInLine1.add(new Integer(ii));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1478 */     return interPts;
/*      */   }
/*      */ 
/*      */   public Coordinate segmentIntersect(Coordinate seg1_start, Coordinate seg1_end, Coordinate seg2_start, Coordinate seg2_end)
/*      */   {
/* 1489 */     Coordinate interPt = null;
/*      */ 
/* 1491 */     Geometry g1 = pointsToLineString(new Coordinate[] { seg1_start, seg1_end });
/* 1492 */     Geometry g2 = pointsToLineString(new Coordinate[] { seg2_start, seg2_end });
/*      */ 
/* 1494 */     if (g1.intersects(g2)) {
/* 1495 */       interPt = g1.intersection(g2).getCoordinates()[0];
/*      */     }
/*      */ 
/* 1498 */     return interPt;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getFaRegionCommBounds()
/*      */   {
/* 1506 */     if (this.faRegionCommBounds == null) {
/* 1507 */       loadFaRegionCommBounds();
/*      */     }
/*      */ 
/* 1510 */     return this.faRegionCommBounds;
/*      */   }
/*      */ 
/*      */   private void loadFaRegionCommBounds()
/*      */   {
/* 1518 */     this.faRegionCommBounds = new HashMap();
/* 1519 */     HashMap rgbnds = getFaRegionBounds();
/* 1520 */     ArrayList used = new ArrayList();
/*      */     Iterator localIterator2;
/* 1522 */     for (Iterator localIterator1 = rgbnds.keySet().iterator(); localIterator1.hasNext(); 
/* 1525 */       localIterator2.hasNext())
/*      */     {
/* 1522 */       String regionName1 = (String)localIterator1.next();
/* 1523 */       used.add(regionName1);
/* 1524 */       Geometry bnd1 = (Geometry)rgbnds.get(regionName1);
/* 1525 */       localIterator2 = rgbnds.keySet().iterator(); continue; String regionName2 = (String)localIterator2.next();
/* 1526 */       if (!used.contains(regionName2)) {
/* 1527 */         Geometry bnd2 = (Geometry)rgbnds.get(regionName2);
/* 1528 */         if (bnd1.intersects(bnd2)) {
/* 1529 */           String bname = new String(regionName1 + "-" + regionName2);
/* 1530 */           Geometry comm = bnd1.intersection(bnd2);
/*      */ 
/* 1532 */           CoordinateList clist = new CoordinateList();
/* 1533 */           clist.add(comm.getCoordinates(), false);
/*      */ 
/* 1535 */           this.faRegionCommBounds.put(bname, pointsToLineString(clist.toCoordinateArray()));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getFaAreaXCommBounds()
/*      */   {
/* 1548 */     if (this.faAreaXCommBounds == null) {
/* 1549 */       loadFaAreaXCommBounds();
/*      */     }
/*      */ 
/* 1552 */     return this.faAreaXCommBounds;
/*      */   }
/*      */ 
/*      */   private void loadFaAreaXCommBounds()
/*      */   {
/* 1561 */     this.faAreaXCommBounds = new HashMap();
/* 1562 */     HashMap areaxbnds = getFaAreaXBounds();
/*      */ 
/* 1564 */     Geometry bnd1 = (Geometry)areaxbnds.get("SLC");
/* 1565 */     Geometry bnd2 = (Geometry)areaxbnds.get("SFO");
/*      */ 
/* 1567 */     if (bnd1.intersects(bnd2)) {
/* 1568 */       String bname = new String("SLC-SFO");
/* 1569 */       Geometry comm = bnd1.intersection(bnd2);
/*      */ 
/* 1571 */       CoordinateList clist = new CoordinateList();
/* 1572 */       clist.add(comm.getCoordinates(), false);
/*      */ 
/* 1574 */       this.faAreaXCommBounds.put(bname, pointsToLineString(clist.toCoordinateArray()));
/*      */     }
/*      */ 
/* 1577 */     bnd1 = (Geometry)areaxbnds.get("CHI");
/* 1578 */     bnd2 = (Geometry)areaxbnds.get("DFW");
/*      */ 
/* 1580 */     if (bnd1.intersects(bnd2)) {
/* 1581 */       String bname = new String("CHI-DFW");
/* 1582 */       Geometry comm = bnd1.intersection(bnd2);
/*      */ 
/* 1584 */       CoordinateList clist = new CoordinateList();
/* 1585 */       clist.add(comm.getCoordinates(), false);
/*      */ 
/* 1587 */       this.faAreaXCommBounds.put(bname, pointsToLineString(clist.toCoordinateArray()));
/*      */ 
/* 1589 */       String bname2 = new String("BOS-MIA");
/* 1590 */       this.faAreaXCommBounds.put(bname2, pointsToLineString(clist.toCoordinateArray()));
/*      */     }
/*      */   }
/*      */ 
/*      */   public Geometry cleanupPoints(Geometry geom)
/*      */   {
/* 1608 */     Coordinate[] gPts = geom.getCoordinates();
/* 1609 */     ArrayList gList = new ArrayList();
/*      */ 
/* 1611 */     for (Coordinate c : gPts) {
/* 1612 */       gList.add(new Coordinate(Math.rint(c.x * 100.0D) / 100.0D, 
/* 1613 */         Math.rint(c.y * 100.0D) / 100.0D));
/*      */     }
/*      */ 
/* 1616 */     gList.remove(gList.size() - 1);
/*      */ 
/* 1619 */     int ii = 0;
/* 1620 */     boolean done = false;
/* 1621 */     while (!done)
/*      */     {
/* 1623 */       int jj = ii;
/* 1624 */       while ((jj + 1 < gList.size()) && 
/* 1625 */         (Math.abs(((Coordinate)gList.get(ii)).x - ((Coordinate)gList.get(jj + 1)).x) < 0.01D) && (
/* 1626 */         Math.abs(((Coordinate)gList.get(ii)).y - ((Coordinate)gList.get(jj + 1)).y) < 0.01D)) {
/* 1627 */         jj++;
/*      */       }
/* 1629 */       int nshift = jj - ii;
/*      */ 
/* 1631 */       if (nshift != 0) {
/* 1632 */         gList = GfaSnap.getInstance().collapseArray(gList, ii, nshift);
/*      */       }
/*      */ 
/* 1635 */       ii++;
/*      */ 
/* 1637 */       if (ii >= gList.size()) done = true;
/*      */ 
/*      */     }
/*      */ 
/* 1641 */     ii = 0;
/* 1642 */     done = false;
/* 1643 */     while (!done)
/*      */     {
/* 1645 */       if ((ii + 2 < gList.size()) && 
/* 1646 */         (Math.abs(((Coordinate)gList.get(ii)).x - ((Coordinate)gList.get(ii + 2)).x) < 0.01D) && 
/* 1647 */         (Math.abs(((Coordinate)gList.get(ii)).y - ((Coordinate)gList.get(ii + 2)).y) < 0.01D))
/*      */       {
/* 1649 */         gList = GfaSnap.getInstance().collapseArray(gList, ii, 2);
/*      */       }
/*      */ 
/* 1652 */       ii++;
/*      */ 
/* 1654 */       if (ii >= gList.size()) done = true;
/*      */ 
/*      */     }
/*      */ 
/* 1660 */     if ((Math.abs(((Coordinate)gList.get(0)).x - ((Coordinate)gList.get(gList.size() - 1)).x) < 0.01D) && 
/* 1661 */       (Math.abs(((Coordinate)gList.get(0)).y - ((Coordinate)gList.get(gList.size() - 1)).y) < 0.01D)) {
/* 1662 */       gList.remove(gList.size() - 1);
/*      */     }
/*      */ 
/* 1665 */     Coordinate[] outp = (Coordinate[])gList.toArray(new Coordinate[gList.size()]);
/*      */ 
/* 1667 */     return pointsToGeometry(outp);
/*      */   }
/*      */ 
/*      */   protected Geometry replacePts(Geometry geom, HashMap<Coordinate, Coordinate> pairs)
/*      */   {
/* 1676 */     Coordinate[] poly = geom.getCoordinates();
/* 1677 */     Coordinate[] pts = new Coordinate[poly.length - 1];
/*      */ 
/* 1679 */     for (int ii = 0; ii < poly.length - 1; ii++)
/*      */     {
/* 1681 */       pts[ii] = new Coordinate(poly[ii]);
/*      */ 
/* 1683 */       if (pairs != null) {
/* 1684 */         for (Coordinate p : pairs.keySet()) {
/* 1685 */           if ((Math.abs(poly[ii].x - p.x) < 0.01D) && 
/* 1686 */             (Math.abs(poly[ii].y - p.y) < 0.01D)) {
/* 1687 */             pts[ii] = new Coordinate((Coordinate)pairs.get(p));
/* 1688 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1694 */     return pointsToGeometry(pts);
/*      */   }
/*      */ 
/*      */   private void addReduceFlags(Gfa elm, CoordinateList pts)
/*      */   {
/* 1712 */     Coordinate[] poly = elm.getLinePoints();
/* 1713 */     int np = poly.length;
/* 1714 */     boolean[] reduceable = new boolean[np];
/* 1715 */     for (int ii = 0; ii < np; ii++) {
/* 1716 */       reduceable[ii] = true;
/*      */     }
/*      */ 
/* 1719 */     for (int ii = 0; ii < np; ii++) {
/* 1720 */       for (int jj = 0; jj < pts.size(); jj++) {
/* 1721 */         if ((Math.abs(poly[ii].x - pts.getCoordinate(jj).x) < 0.01D) && 
/* 1722 */           (Math.abs(poly[ii].y - pts.getCoordinate(jj).y) < 0.01D)) {
/* 1723 */           reduceable[ii] = false;
/* 1724 */           reduceable[((ii - 1 + np) % np)] = false;
/* 1725 */           reduceable[((ii + 1 + np) % np)] = false;
/* 1726 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1731 */     elm.setReduceFlags(reduceable);
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> getCommonPoints(ArrayList<Geometry> big, ArrayList<Geometry> small)
/*      */   {
/* 1741 */     ArrayList commPts = new ArrayList();
/*      */     Iterator localIterator2;
/* 1743 */     for (Iterator localIterator1 = big.iterator(); localIterator1.hasNext(); 
/* 1744 */       localIterator2.hasNext())
/*      */     {
/* 1743 */       Geometry g1 = (Geometry)localIterator1.next();
/* 1744 */       localIterator2 = small.iterator(); continue; Geometry g2 = (Geometry)localIterator2.next();
/* 1745 */       if (g1.intersects(g2)) {
/* 1746 */         for (Coordinate c : g1.intersection(g2).getCoordinates()) {
/* 1747 */           if (!commPts.contains(c)) {
/* 1748 */             commPts.add(new Coordinate(c));
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1755 */     return commPts;
/*      */   }
/*      */ 
/*      */   public Geometry removeCommonPoints(Geometry gm, ArrayList<Coordinate> pts)
/*      */   {
/* 1763 */     if ((pts == null) || (pts.size() == 0)) {
/* 1764 */       return gm;
/*      */     }
/*      */ 
/* 1767 */     ArrayList newPts = new ArrayList();
/*      */ 
/* 1769 */     Coordinate[] gPts = gm.getCoordinates();
/*      */ 
/* 1771 */     for (int ii = 0; ii < gPts.length - 1; ii++) {
/* 1772 */       if (!pts.contains(gPts[ii])) {
/* 1773 */         newPts.add(new Coordinate(gPts[ii]));
/*      */       }
/*      */     }
/*      */ 
/* 1777 */     return pointsToGeometry(newPts);
/*      */   }
/*      */ 
/*      */   public void loadGfaBounds()
/*      */   {
/* 1787 */     getStateBounds();
/* 1788 */     getFaRegionBounds();
/* 1789 */     getFaAreaBounds();
/* 1790 */     getFaAreaXBounds();
/*      */ 
/* 1792 */     getGreatLakeBounds();
/* 1793 */     getCoastalWaterBounds();
/* 1794 */     getFaInternationalBound();
/* 1795 */     getMtObscStates();
/*      */ 
/* 1797 */     getFaRegionCommBounds();
/* 1798 */     getFaAreaXCommBounds();
/*      */ 
/* 1801 */     updateGfaBoundsInGrid();
/*      */ 
/* 1804 */     SnapUtil.SnapVOR.getSnapStns(null, 16);
/*      */   }
/*      */ 
/*      */   public void updateGfaBoundsInGrid()
/*      */   {
/* 1813 */     this.faInternationalBoundInGrid = geometryInGrid(getFaInternationalBound());
/*      */ 
/* 1815 */     this.faRegionBoundsInGrid = updateBoundsInGrid(getFaRegionBounds());
/* 1816 */     this.faAreaBoundsInGrid = updateBoundsInGrid(getFaAreaBounds());
/* 1817 */     this.faAreaXBoundsInGrid = updateBoundsInGrid(getFaAreaXBounds());
/*      */ 
/* 1819 */     this.stateBoundsInGrid = updateBoundsInGrid(getStateBounds());
/*      */ 
/* 1843 */     this.greatLakesBoundsInGrid = updateBoundsInGrid(getGreatLakeBounds());
/* 1844 */     this.coastalWaterBoundsInGrid = updateBoundsInGrid(getCoastalWaterBounds());
/* 1845 */     this.faRegionCommBoundsInGrid = updateBoundsInGrid(getFaRegionCommBounds());
/* 1846 */     this.faAreaXCommBoundsInGrid = updateBoundsInGrid(getFaAreaXCommBounds());
/*      */   }
/*      */ 
/*      */   private HashMap<String, Geometry> updateBoundsInGrid(HashMap<String, Geometry> bndInMap)
/*      */   {
/* 1855 */     HashMap bndInGrid = new HashMap();
/*      */ 
/* 1857 */     for (String bname : bndInMap.keySet()) {
/* 1858 */       bndInGrid.put(bname, geometryInGrid((Geometry)bndInMap.get(bname)));
/*      */     }
/*      */ 
/* 1861 */     return bndInGrid;
/*      */   }
/*      */ 
/*      */   public Geometry getFaInternationalBoundInGrid()
/*      */   {
/* 1870 */     return this.faInternationalBoundInGrid;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getFaRegionBoundsInGrid() {
/* 1874 */     return this.faRegionBoundsInGrid;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getFaAreaBoundsInGrid() {
/* 1878 */     return this.faAreaBoundsInGrid;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getFaAreaXBoundsInGrid() {
/* 1882 */     return this.faAreaXBoundsInGrid;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getStateBoundsInGrid() {
/* 1886 */     return this.stateBoundsInGrid;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getGreatLakesBoundsInGrid() {
/* 1890 */     return this.greatLakesBoundsInGrid;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getCoastalWaterBoundsInGrid() {
/* 1894 */     return this.coastalWaterBoundsInGrid;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getFaRegionCommBoundsInGrid() {
/* 1898 */     return this.faRegionCommBoundsInGrid;
/*      */   }
/*      */ 
/*      */   public HashMap<String, Geometry> getFaAreaXCommBoundsInGrid() {
/* 1902 */     return this.faAreaXCommBoundsInGrid;
/*      */   }
/*      */ 
/*      */   private Geometry geometryInGrid(Geometry geomInMap)
/*      */   {
/* 1910 */     Geometry gout = null;
/* 1911 */     if (((geomInMap instanceof Polygon)) || ((geomInMap instanceof MultiPolygon)))
/*      */     {
/* 1913 */       Polygon[] polygons = new Polygon[geomInMap.getNumGeometries()];
/*      */ 
/* 1915 */       for (int ii = 0; ii < geomInMap.getNumGeometries(); ii++) {
/* 1916 */         Geometry g = geomInMap.getGeometryN(ii);
/* 1917 */         Coordinate[] gpts = g.getCoordinates();
/* 1918 */         Coordinate[] gpts1 = (Coordinate[])Arrays.copyOf(gpts, gpts.length - 1);
/* 1919 */         Coordinate[] pts = PgenUtil.latlonToGrid(gpts1);
/*      */ 
/* 1921 */         polygons[ii] = pointsToPolygon(pts);
/*      */       }
/*      */ 
/* 1924 */       gout = geometryFactory.createMultiPolygon(polygons);
/*      */     }
/* 1926 */     else if ((geomInMap instanceof LineString)) {
/* 1927 */       Coordinate[] gpts = geomInMap.getCoordinates();
/* 1928 */       Coordinate[] pts = PgenUtil.latlonToGrid(gpts);
/*      */ 
/* 1930 */       gout = pointsToLineString(pts);
/*      */     }
/*      */ 
/* 1933 */     return gout;
/*      */   }
/*      */ 
/*      */   public void validateGfaBounds()
/*      */   {
/* 1942 */     writeErrorBound("FA_States_Map_Fixed", getStateBounds());
/*      */   }
/*      */ 
/*      */   private void writeErrorBound(String bndName, HashMap<String, Geometry> bndMap)
/*      */   {
/* 1951 */     ArrayList errorStates = new ArrayList();
/*      */ 
/* 1956 */     boolean isvalid = true;
/*      */ 
/* 1958 */     StringBuilder ss = new StringBuilder();
/* 1959 */     for (String key : bndMap.keySet()) {
/* 1960 */       ss.append(key + " ");
/*      */     }
/*      */ 
/* 1963 */     StringBuilder as = new StringBuilder();
/* 1964 */     for (String key : bndMap.keySet())
/*      */     {
/* 1966 */       Product activeProduct = new Product("Default", "Default", "Default", 
/* 1967 */         new ProductInfo(), new ProductTime(), new ArrayList());
/*      */ 
/* 1969 */       Layer activeLayer = new Layer();
/* 1970 */       activeProduct.addLayer(activeLayer);
/*      */ 
/* 1972 */       List productList = new ArrayList();
/* 1973 */       productList.add(activeProduct);
/*      */ 
/* 1975 */       Geometry g = (Geometry)bndMap.get(key);
/*      */ 
/* 1977 */       ArrayList pts = new ArrayList();
/*      */ 
/* 1980 */       isvalid = g.isValid();
/* 1981 */       if (!isvalid) {
/* 1982 */         as.append(" " + key);
/* 1983 */         System.out.println("\tInvalid Bound " + key + "\t " + g.getNumGeometries());
/*      */       }
/*      */ 
/* 1989 */       System.out.println("\tProcess Bound " + key + "\t " + g.getNumGeometries());
/*      */ 
/* 1991 */       for (int jk = 0; jk < g.getNumGeometries(); jk++)
/*      */       {
/* 1993 */         pts.clear();
/*      */ 
/* 1995 */         Geometry one = g.getGeometryN(jk);
/*      */ 
/* 1997 */         for (Coordinate c : one.getCoordinates()) {
/* 1998 */           pts.add(new Coordinate(c));
/*      */         }
/* 2000 */         pts.remove(pts.size() - 1);
/*      */ 
/* 2002 */         Line cline = new Line(null, new Color[] { Color.red }, 
/* 2003 */           2.0F, 1.0D, true, false, pts, 0, FillPatternList.FillPattern.SOLID, 
/* 2004 */           "Lines", "LINE_SOLID");
/* 2005 */         String str = "";
/* 2006 */         str = str + key + "_" + jk;
/*      */ 
/* 2008 */         Text lbl = new Text(null, "Courier", 14.0F, 
/* 2009 */           IText.TextJustification.CENTER, null, 0.0D, 
/* 2010 */           IText.TextRotation.SCREEN_RELATIVE, new String[] { str }, 
/* 2011 */           IText.FontStyle.REGULAR, Color.GREEN, 0, 0, true, IText.DisplayType.NORMAL, 
/* 2012 */           "Text", "General Text");
/* 2013 */         lbl.setLocation(cline.getLinePoints()[0]);
/*      */ 
/* 2016 */         activeLayer.add(cline);
/*      */       }
/*      */ 
/* 2022 */       if (((Product)productList.get(0)).getLayer(0).getDrawables().size() > 0) {
/* 2023 */         Products filePrds = ProductConverter.convert(productList);
/*      */         String a;
/*      */         String a;
/* 2025 */         if (isvalid) {
/* 2026 */           a = "/export/cdbsrv/jwu/stateBnd/" + bndName + "_high_" + key + "_correct_nolabel.xml";
/*      */         }
/*      */         else {
/* 2029 */           a = "/export/cdbsrv/jwu/stateBnd/" + bndName + "_high_" + key + "_icorrect_nolabel.xml";
/*      */         }
/* 2031 */         System.out.println("Write to validation file: " + a);
/* 2032 */         FileTools.write(a, filePrds);
/*      */       }
/*      */     }
/*      */ 
/* 2036 */     System.out.println("Invalid states are: " + as);
/*      */   }
/*      */ 
/*      */   private HashMap<String, Geometry> fixStateBounds(HashMap<String, Geometry> stateBnd)
/*      */   {
/* 2047 */     HashMap fixedBounds = new HashMap();
/*      */ 
/* 2049 */     ArrayList errorStates = new ArrayList();
/* 2050 */     for (String s : this.WRONG_STATES) {
/* 2051 */       errorStates.add(s);
/*      */     }
/*      */ 
/* 2054 */     for (String key : stateBnd.keySet())
/*      */     {
/* 2056 */       Geometry g = (Geometry)stateBnd.get(key);
/*      */ 
/* 2058 */       if (!errorStates.contains(key)) {
/* 2059 */         fixedBounds.put(key, g);
/*      */       }
/*      */       else {
/* 2062 */         ArrayList pts = new ArrayList();
/*      */ 
/* 2064 */         Polygon[] polygons = new Polygon[g.getNumGeometries()];
/*      */ 
/* 2066 */         for (int jk = 0; jk < g.getNumGeometries(); jk++)
/*      */         {
/* 2068 */           pts.clear();
/*      */ 
/* 2070 */           Geometry one = g.getGeometryN(jk);
/* 2071 */           Coordinate[] bpts = one.getCoordinates();
/* 2072 */           Coordinate firstPt = bpts[0];
/* 2073 */           pts.add(firstPt);
/* 2074 */           int nn = 1;
/* 2075 */           while ((!bpts[nn].equals(firstPt)) && (nn < bpts.length)) {
/* 2076 */             pts.add(bpts[nn]);
/* 2077 */             nn++;
/*      */           }
/*      */ 
/* 2080 */           polygons[jk] = pointsToPolygon((Coordinate[])pts.toArray(new Coordinate[pts.size()]));
/*      */         }
/*      */ 
/* 2083 */         MultiPolygon multipolygon = geometryFactory.createMultiPolygon(polygons);
/*      */ 
/* 2085 */         fixedBounds.put(key, multipolygon);
/*      */       }
/*      */     }
/*      */ 
/* 2089 */     return fixedBounds;
/*      */   }
/*      */ 
/*      */   private int findPoint(Coordinate pt, List<Coordinate> list, double tie)
/*      */   {
/* 2101 */     int indx = -1;
/*      */ 
/* 2103 */     if ((pt != null) && (list != null)) {
/* 2104 */       for (int ii = 0; ii < list.size(); ii++) {
/* 2105 */         if (isSamePoint(pt, (Coordinate)list.get(ii), tie)) {
/* 2106 */           indx = ii;
/* 2107 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2112 */     return indx;
/*      */   }
/*      */ 
/*      */   public void testJTS()
/*      */   {
/* 2120 */     Coordinate[] polyPts = { new Coordinate(-110.0D, 40.0D), 
/* 2121 */       new Coordinate(-110.0D, 50.0D), 
/* 2122 */       new Coordinate(-90.0D, 50.0D), 
/* 2123 */       new Coordinate(-90.0D, 40.0D) };
/* 2124 */     Geometry poly = pointsToGeometry(polyPts);
/* 2125 */     Geometry bndPts = pointsToGeometry(new Coordinate[] { new Coordinate(-110.0D, 50.0D) });
/* 2126 */     Geometry inPts = pointsToGeometry(new Coordinate[] { new Coordinate(-100.0D, 45.0D) });
/* 2127 */     Geometry outPts = pointsToGeometry(new Coordinate[] { new Coordinate(-100.0D, 30.0D) });
/*      */ 
/* 2129 */     Geometry a1 = bndPts.intersection(poly);
/* 2130 */     Geometry a2 = inPts.intersection(poly);
/* 2131 */     Geometry a3 = outPts.intersection(poly);
/*      */ 
/* 2133 */     Geometry bndlin = pointsToGeometry(new Coordinate[] { new Coordinate(-110.0D, 50.0D), 
/* 2134 */       new Coordinate(-110.0D, 40.0D) });
/*      */ 
/* 2136 */     Geometry inlin = pointsToGeometry(new Coordinate[] { new Coordinate(-110.0D, 50.0D), 
/* 2137 */       new Coordinate(-100.0D, 45.0D) });
/*      */ 
/* 2139 */     Geometry outlin = pointsToGeometry(new Coordinate[] { new Coordinate(-110.0D, 50.0D), 
/* 2140 */       new Coordinate(-100.0D, 30.0D) });
/* 2141 */     Geometry a4 = bndlin.intersection(poly);
/* 2142 */     Geometry a5 = inlin.intersection(poly);
/* 2143 */     Geometry a6 = outlin.intersection(poly);
/*      */ 
/* 2145 */     System.out.println("\n\na1=" + a1.getCoordinates().length);
/* 2146 */     System.out.println("a2=" + a2.getCoordinates().length);
/* 2147 */     System.out.println("a3=" + a3.getCoordinates().length);
/* 2148 */     System.out.println("a4=" + a4.getCoordinates().length);
/* 2149 */     System.out.println("a5=" + a5.getCoordinates().length);
/* 2150 */     System.out.println("a6=" + a6.getCoordinates().length);
/*      */ 
/* 2152 */     Geometry l1 = pointsToGeometry(new Coordinate[] { new Coordinate(-110.0D, 40.0D), 
/* 2153 */       new Coordinate(-110.0D, 50.0D) });
/* 2154 */     Geometry l2 = pointsToGeometry(new Coordinate[] { new Coordinate(-90.0D, 40.0D), 
/* 2155 */       new Coordinate(-100.0D, 45.0D) });
/* 2156 */     Geometry l3 = pointsToGeometry(new Coordinate[] { new Coordinate(-90.0D, 40.0D), 
/* 2157 */       new Coordinate(-115.0D, 40.0D) });
/*      */ 
/* 2159 */     Geometry a7 = l1.intersection(l2);
/* 2160 */     System.out.println("a7=" + a7.getCoordinates().length);
/*      */ 
/* 2162 */     Geometry a8 = l1.intersection(l3);
/* 2163 */     System.out.println("a8=" + a8.getCoordinates().length);
/*      */ 
/* 2165 */     Geometry g1 = pointsToGeometry(new Coordinate[] { 
/* 2166 */       new Coordinate(-110.0D, 47.899999999999999D), 
/* 2167 */       new Coordinate(-104.0D, 46.850000000000001D), 
/* 2168 */       new Coordinate(-104.0D, 42.799999999999997D), 
/* 2169 */       new Coordinate(-110.0D, 43.0D) });
/*      */ 
/* 2171 */     Geometry g2 = pointsToGeometry(new Coordinate[] { 
/* 2172 */       new Coordinate(-104.0D, 46.850000000000001D), 
/* 2173 */       new Coordinate(-97.0D, 46.399999999999999D), 
/* 2174 */       new Coordinate(-98.0D, 42.799999999999997D), 
/* 2175 */       new Coordinate(-104.0D, 42.799999999999997D) });
/*      */ 
/* 2177 */     Geometry u1 = g1.union(g2);
/* 2178 */     System.out.println("\nTest union of two polygons clipped from one polygon:");
/* 2179 */     for (Coordinate c : u1.getCoordinates()) {
/* 2180 */       System.out.println(c.y + "," + c.x);
/*      */     }
/*      */ 
/* 2183 */     ArrayList uniquePts = new ArrayList();
/* 2184 */     for (Coordinate c : u1.getCoordinates()) {
/* 2185 */       if ((!Arrays.asList(g1.getCoordinates()).contains(c)) || 
/* 2186 */         (!Arrays.asList(g2.getCoordinates()).contains(c)))
/*      */       {
/* 2189 */         uniquePts.add(c);
/*      */       }
/*      */     }
/*      */ 
/* 2193 */     System.out.println("\nUnique points found:");
/* 2194 */     for (Coordinate c : uniquePts) {
/* 2195 */       System.out.println(c.y + "," + c.x);
/*      */     }
/*      */ 
/* 2198 */     Geometry p1 = pointsToGeometry(new Coordinate[] { new Coordinate(20.0D, 10.0D), new Coordinate(30.0D, 20.0D), 
/* 2199 */       new Coordinate(40.0D, 10.0D) });
/* 2200 */     Geometry p2 = pointsToGeometry(new Coordinate[] { new Coordinate(20.0D, 10.0D) });
/* 2201 */     System.out.println("\nTest:");
/*      */ 
/* 2203 */     if (p1.covers(p2)) {
/* 2204 */       System.out.println("Covered");
/*      */     }
/*      */     else {
/* 2207 */       System.out.println("Not Covered");
/*      */     }
/*      */ 
/* 2210 */     if (p2.intersects(p1)) {
/* 2211 */       System.out.println("Intersect");
/*      */     }
/*      */     else
/* 2214 */       System.out.println("Not Intersect");
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaClip
 * JD-Core Version:    0.6.2
 */