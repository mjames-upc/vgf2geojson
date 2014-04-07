/*      */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*      */ 
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.GeometryCollection;
/*      */ import com.vividsolutions.jts.geom.GeometryFactory;
/*      */ import com.vividsolutions.jts.geom.LinearRing;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.common.staticdata.SPCCounty;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.StationTable;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IWatchBox;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*      */ import java.awt.Color;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerFactory;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import javax.xml.transform.stream.StreamSource;
/*      */ import org.geotools.referencing.GeodeticCalculator;
/*      */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*      */ 
/*      */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE})
/*      */ public class WatchBox extends MultiPointElement
/*      */   implements IWatchBox
/*      */ {
/*      */   public static final float HALF_WIDTH = 60.0F;
/*      */   private static HashMap<String, String> stateName;
/*   89 */   public static String[] dirs = { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", 
/*   90 */     "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N" };
/*      */   private WatchShape boxShape;
/*      */   private String symbolType;
/*      */   private float symbolWidth;
/*      */   private double symbolSize;
/*      */   private boolean fillFlag;
/*      */   private Color fillColor;
/*      */   private Station[] anchors;
/*      */   private List<SPCCounty> countyList;
/*      */   private String issueStatus;
/*      */   private Calendar issueTime;
/*      */   private Calendar expTime;
/*      */   private String severity;
/*      */   private String timeZone;
/*      */   private float hailSize;
/*      */   private int gust;
/*      */   private int top;
/*      */   private int moveDir;
/*      */   private int moveSpeed;
/*      */   private String statesIncl;
/*      */   private String adjAreas;
/*      */   private int replWatch;
/*      */   private String contWatch;
/*      */   private int issueFlag;
/*      */   private String watchType;
/*      */   private String forecaster;
/*      */   private int watchNumber;
/*      */   private String endPointAnc;
/*      */   private String endPointVor;
/*      */   private int halfWidthSm;
/*      */   private int halfWidthNm;
/*      */   private int wathcAreaNm;
/*      */   private String cntyInfo;
/*      */   private String dataURI;
/*      */   private List<WatchStatus> statusHistory;
/*      */ 
/*      */   public WatchBox()
/*      */   {
/*  164 */     this.anchors = new Station[2];
/*  165 */     this.countyList = new ArrayList();
/*  166 */     this.issueFlag = 0;
/*      */   }
/*      */ 
/*      */   public void update(IAttribute iattr)
/*      */   {
/*  174 */     if ((iattr instanceof IWatchBox)) {
/*  175 */       IWatchBox attr = (IWatchBox)iattr;
/*  176 */       setColors(attr.getColors());
/*  177 */       this.fillFlag = attr.getFillFlag();
/*  178 */       this.fillColor = attr.getFillColor();
/*  179 */       this.boxShape = attr.getWatchBoxShape();
/*  180 */       this.symbolType = attr.getWatchSymbolType();
/*  181 */       this.symbolWidth = attr.getWatchSymbolWidth();
/*  182 */       this.symbolSize = attr.getWatchSymbolSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   public AbstractDrawableComponent copy()
/*      */   {
/*  196 */     WatchBox newWatchBox = new WatchBox();
/*      */ 
/*  198 */     newWatchBox.setAnchors(this.anchors[0], this.anchors[1]);
/*  199 */     newWatchBox.update(this);
/*      */ 
/*  205 */     ArrayList ptsCopy = new ArrayList();
/*  206 */     for (int i = 0; i < getPoints().size(); i++) {
/*  207 */       ptsCopy.add(new Coordinate((Coordinate)getPoints().get(i)));
/*      */     }
/*  209 */     newWatchBox.setPoints(ptsCopy);
/*      */ 
/*  214 */     Color[] colorCopy = new Color[getColors().length];
/*  215 */     for (int i = 0; i < getColors().length; i++) {
/*  216 */       colorCopy[i] = new Color(getColors()[i].getRed(), 
/*  217 */         getColors()[i].getGreen(), 
/*  218 */         getColors()[i].getBlue());
/*      */     }
/*  220 */     newWatchBox.setColors(colorCopy);
/*  221 */     if (this.fillColor != null) {
/*  222 */       newWatchBox.setFillColor(new Color(this.fillColor.getRed(), this.fillColor
/*  223 */         .getGreen(), this.fillColor.getBlue()));
/*      */     }
/*      */ 
/*  228 */     newWatchBox.setPgenCategory(new String(getPgenCategory()));
/*  229 */     newWatchBox.setPgenType(new String(getPgenType()));
/*  230 */     newWatchBox.setParent(getParent());
/*  231 */     newWatchBox.setWatchBoxShape(this.boxShape);
/*  232 */     newWatchBox.setWatchSymbolType(this.symbolType);
/*  233 */     newWatchBox.setWatchSymbolSize(this.symbolSize);
/*  234 */     newWatchBox.setWatchSymbolWidth(this.symbolWidth);
/*      */ 
/*  236 */     return newWatchBox;
/*      */   }
/*      */ 
/*      */   public Color getFillColor()
/*      */   {
/*  244 */     return this.fillColor;
/*      */   }
/*      */ 
/*      */   public void setFillColor(Color color)
/*      */   {
/*  253 */     this.fillColor = color;
/*      */   }
/*      */ 
/*      */   public WatchShape getWatchBoxShape()
/*      */   {
/*  261 */     return this.boxShape;
/*      */   }
/*      */ 
/*      */   public void setWatchBoxShape(WatchShape ws)
/*      */   {
/*  270 */     this.boxShape = ws;
/*      */   }
/*      */ 
/*      */   public void setWatchBoxShape(String ws) {
/*  274 */     this.boxShape = WatchShape.valueOf(ws);
/*      */   }
/*      */ 
/*      */   public void setAnchors(Station anchor1, Station anchor2)
/*      */   {
/*  284 */     this.anchors[0] = anchor1;
/*  285 */     this.anchors[1] = anchor2;
/*      */   }
/*      */ 
/*      */   public Station[] getAnchors()
/*      */   {
/*  294 */     return this.anchors;
/*      */   }
/*      */ 
/*      */   public double getHalfWidth()
/*      */   {
/*  305 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*      */ 
/*  307 */     gc.setStartingGeographicPoint(((Coordinate)this.linePoints.get(0)).x, ((Coordinate)this.linePoints.get(0)).y);
/*  308 */     gc.setDestinationGeographicPoint(((Coordinate)this.linePoints.get(1)).x, 
/*  309 */       ((Coordinate)this.linePoints.get(1)).y);
/*      */ 
/*  311 */     return gc.getOrthodromicDistance();
/*      */   }
/*      */ 
/*      */   public String getSpec()
/*      */   {
/*  322 */     String spec = 
/*  323 */       String.format("%1$5.2f%2$10.2f", new Object[] { Double.valueOf(((Coordinate)this.linePoints.get(0)).y), 
/*  323 */       Double.valueOf(((Coordinate)this.linePoints.get(0)).x) }) + 
/*  324 */       "   " + 
/*  325 */       getRelative((Coordinate)this.linePoints.get(0), this.anchors[0]) + 
/*  326 */       "   " + 
/*  327 */       getRelative((Coordinate)this.linePoints.get(0), 
/*  328 */       getNearestVor((Coordinate)this.linePoints.get(0))) + 
/*  329 */       "\n" + 
/*  330 */       String.format("%1$5.2f%2$10.2f", new Object[] { Double.valueOf(((Coordinate)this.linePoints.get(4)).y), 
/*  331 */       Double.valueOf(((Coordinate)this.linePoints.get(4)).x) }) + 
/*  332 */       "   " + 
/*  333 */       getRelative((Coordinate)this.linePoints.get(4), this.anchors[1]) + 
/*  334 */       "   " + 
/*  335 */       getRelative((Coordinate)this.linePoints.get(4), 
/*  336 */       getNearestVor((Coordinate)this.linePoints.get(4))) + 
/*  337 */       "\n" + 
/*  338 */       "ORIENT: " + 
/*  339 */       this.boxShape.toString() + 
/*  340 */       "  - HALF WIDTH: " + 
/*  341 */       String.format("%1$4.0f", new Object[] { Double.valueOf(getHalfWidth() / 1609.3399658203125D) }) + 
/*  342 */       "sm (" + 
/*  343 */       Math.round(getHalfWidth() / 1852.0D / 5.0D) * 
/*  344 */       5L + 
/*  345 */       " nm)\n" + 
/*  346 */       "AREA(sq nautical miles):  " + 
/*  347 */       String.format("%1$-8.0f", new Object[] { Double.valueOf(getWatchArea()) }) + "\n";
/*      */ 
/*  349 */     return spec;
/*      */   }
/*      */ 
/*      */   public String getRelative(Coordinate pt, Station st)
/*      */   {
/*  362 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*      */ 
/*  364 */     gc.setStartingGeographicPoint(st.getLongitude().floatValue(), st.getLatitude().floatValue());
/*  365 */     gc.setDestinationGeographicPoint(pt.x, pt.y);
/*      */ 
/*  367 */     long dist = Math.round(gc.getOrthodromicDistance() / 1609.3399658203125D);
/*  368 */     long dir = Math.round(gc.getAzimuth());
/*  369 */     if (dir < 0L)
/*  370 */       dir += 360L;
/*  371 */     String str = String.format("%1$4d%2$5s%3$5s", new Object[] { Long.valueOf(dist), 
/*  372 */       dirs[((int)Math.round(dir / 22.5D))], st.getStid() });
/*      */ 
/*  375 */     return str;
/*      */   }
/*      */ 
/*      */   public Station getNearestVor(Coordinate loc)
/*      */   {
/*  386 */     return PgenStaticDataProvider.getProvider().getVorTbl()
/*  387 */       .getNearestStation(loc);
/*      */   }
/*      */ 
/*      */   public double getWatchArea()
/*      */   {
/*  397 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*      */ 
/*  399 */     gc.setStartingGeographicPoint(((Coordinate)this.linePoints.get(0)).x, ((Coordinate)this.linePoints.get(0)).y);
/*  400 */     gc.setDestinationGeographicPoint(((Coordinate)this.linePoints.get(4)).x, 
/*  401 */       ((Coordinate)this.linePoints.get(4)).y);
/*      */ 
/*  403 */     double base = gc.getOrthodromicDistance();
/*      */ 
/*  405 */     Coordinate intrPt = new Coordinate();
/*  406 */     getDistanceFromLine((Coordinate)this.linePoints.get(1), (Coordinate)this.linePoints.get(0), 
/*  407 */       (Coordinate)this.linePoints.get(4), intrPt);
/*      */ 
/*  409 */     gc.setStartingGeographicPoint(((Coordinate)this.linePoints.get(1)).x, ((Coordinate)this.linePoints.get(1)).y);
/*  410 */     gc.setDestinationGeographicPoint(intrPt.x, intrPt.y);
/*      */ 
/*  412 */     double height = gc.getOrthodromicDistance();
/*      */ 
/*  414 */     return base * height * 2.0D / 3429904.0D;
/*      */   }
/*      */ 
/*      */   private double getDistanceFromLine(Coordinate point, Coordinate lnPt1, Coordinate lnPt2, Coordinate intrsctPt)
/*      */   {
/*  433 */     if (lnPt1.x == lnPt2.x) {
/*  434 */       intrsctPt.x = lnPt1.x;
/*  435 */       intrsctPt.y = point.y;
/*  436 */       return Math.abs(point.x - lnPt1.x);
/*  437 */     }if (lnPt1.y == lnPt2.y) {
/*  438 */       intrsctPt.x = point.x;
/*  439 */       intrsctPt.y = lnPt1.y;
/*  440 */       return Math.abs(point.y - lnPt1.y);
/*      */     }
/*  442 */     double mi = (lnPt2.y - lnPt1.y) / (lnPt2.x - lnPt1.x);
/*  443 */     double bi = lnPt1.y - mi * lnPt1.x;
/*  444 */     double m = -1.0D / mi;
/*  445 */     double b = point.y - m * point.x;
/*  446 */     intrsctPt.x = ((b - bi) / (mi - m));
/*  447 */     intrsctPt.y = (m * intrsctPt.x + b);
/*  448 */     double d = (intrsctPt.x - point.x) * (intrsctPt.x - point.x) + 
/*  449 */       (intrsctPt.y - point.y) * (intrsctPt.y - point.y);
/*  450 */     return Math.sqrt(d);
/*      */   }
/*      */ 
/*      */   public void addCounty(SPCCounty cnty)
/*      */   {
/*  460 */     this.countyList.add(cnty);
/*      */   }
/*      */ 
/*      */   public void removeCounty(SPCCounty cnty) {
/*  464 */     this.countyList.remove(cnty);
/*      */   }
/*      */ 
/*      */   public List<SPCCounty> getCountyList()
/*      */   {
/*  471 */     return this.countyList;
/*      */   }
/*      */ 
/*      */   public void setCountyList(List<SPCCounty> cl)
/*      */   {
/*  480 */     List newList = new ArrayList();
/*  481 */     for (SPCCounty cnty : cl) {
/*  482 */       newList.add(cnty);
/*      */     }
/*  484 */     this.countyList = newList;
/*      */   }
/*      */ 
/*      */   public void clearCntyList()
/*      */   {
/*  491 */     this.countyList.clear();
/*      */   }
/*      */ 
/*      */   public String getWatchSymbolType()
/*      */   {
/*  498 */     return this.symbolType;
/*      */   }
/*      */ 
/*      */   public float getWatchSymbolWidth()
/*      */   {
/*  505 */     return this.symbolWidth;
/*      */   }
/*      */ 
/*      */   public double getWatchSymbolSize()
/*      */   {
/*  512 */     return this.symbolSize;
/*      */   }
/*      */ 
/*      */   public void setWatchSymbolType(String str)
/*      */   {
/*  521 */     this.symbolType = str;
/*      */   }
/*      */ 
/*      */   public void setWatchSymbolWidth(float width)
/*      */   {
/*  530 */     this.symbolWidth = width;
/*      */   }
/*      */ 
/*      */   public void setWatchSymbolSize(double size)
/*      */   {
/*  539 */     this.symbolSize = size;
/*      */   }
/*      */ 
/*      */   public List<String> getWFOs()
/*      */   {
/*  548 */     ArrayList wfos = new ArrayList();
/*      */ 
/*  550 */     if ((this.countyList != null) && (!this.countyList.isEmpty())) {
/*  551 */       for (SPCCounty cnty : this.countyList)
/*      */       {
/*  553 */         String wfo = cnty.getWfo();
/*      */ 
/*  555 */         if (wfo != null) {
/*  556 */           for (int ii = 0; ii < wfo.length(); ii += 3) {
/*  557 */             String wfoStr = wfo.substring(ii, 
/*  558 */               wfo.length() > ii + 3 ? ii + 3 : wfo.length());
/*  559 */             if (!wfos.contains(wfoStr)) {
/*  560 */               wfos.add(wfoStr);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  567 */     return wfos;
/*      */   }
/*      */ 
/*      */   public List<String> getStates()
/*      */   {
/*  576 */     ArrayList states = new ArrayList();
/*  577 */     if ((this.countyList != null) && (!this.countyList.isEmpty())) {
/*  578 */       for (SPCCounty cnty : this.countyList)
/*      */       {
/*  580 */         if ((cnty.getState() != null) && 
/*  581 */           (!states.contains(cnty.getState()))) {
/*  582 */           states.add(cnty.getState());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  590 */     return states;
/*      */   }
/*      */ 
/*      */   public void removeState(String state)
/*      */   {
/*  599 */     if ((this.countyList != null) && (!this.countyList.isEmpty())) {
/*  600 */       Iterator it = this.countyList.iterator();
/*  601 */       while (it.hasNext()) {
/*  602 */         SPCCounty cnty = (SPCCounty)it.next();
/*  603 */         if ((cnty.getState() != null) && 
/*  604 */           (cnty.getState().equalsIgnoreCase(state)))
/*  605 */           it.remove();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeCwa(String cwa)
/*      */   {
/*  617 */     if ((this.countyList != null) && (!this.countyList.isEmpty())) {
/*  618 */       Iterator it = this.countyList.iterator();
/*  619 */       while (it.hasNext()) {
/*  620 */         SPCCounty cnty = (SPCCounty)it.next();
/*  621 */         if ((cnty.getWfo() != null) && 
/*  622 */           (cnty.getWfo().equalsIgnoreCase(cwa)))
/*  623 */           it.remove();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addCwa(String cwa)
/*      */   {
/*  635 */     List allCounties = PgenStaticDataProvider.getProvider()
/*  636 */       .getSPCCounties();
/*      */ 
/*  638 */     if (allCounties != null)
/*  639 */       for (SPCCounty cnty : allCounties)
/*  640 */         if ((cnty.getWfo() != null) && 
/*  641 */           (cnty.getWfo().equalsIgnoreCase(cwa)) && 
/*  642 */           (!this.countyList.contains(cnty)))
/*  643 */           this.countyList.add(cnty);
/*      */   }
/*      */ 
/*      */   public void setFillFlag(boolean fillFlag)
/*      */   {
/*  654 */     this.fillFlag = fillFlag;
/*      */   }
/*      */ 
/*      */   public boolean getFillFlag()
/*      */   {
/*  661 */     return this.fillFlag;
/*      */   }
/*      */ 
/*      */   public List<SPCCounty> getInactiveCountiesInWB()
/*      */   {
/*  672 */     if ((this.countyList == null) || (this.countyList.isEmpty())) {
/*  673 */       return null;
/*      */     }
/*      */ 
/*  676 */     List rt = new ArrayList();
/*  677 */     Geometry union = getCountyUnion();
/*  678 */     GeometryFactory gf = new GeometryFactory();
/*      */ 
/*  681 */     for (int ii = 0; ii < union.getNumGeometries(); ii++)
/*      */     {
/*  683 */       Polygon poly = (Polygon)union.getGeometryN(ii);
/*      */ 
/*  686 */       for (int jj = 0; jj < poly.getNumInteriorRing(); jj++)
/*      */       {
/*  688 */         LinearRing lr = (LinearRing)poly.getInteriorRingN(jj);
/*      */ 
/*  690 */         Polygon p = gf.createPolygon(lr, null);
/*  691 */         double area = p.getArea();
/*      */ 
/*  694 */         if (area >= 0.01D)
/*      */         {
/*  699 */           Iterator localIterator = PgenStaticDataProvider.getProvider()
/*  699 */             .getSPCCounties().iterator();
/*      */ 
/*  698 */           while (localIterator.hasNext()) {
/*  699 */             SPCCounty cnty = (SPCCounty)localIterator.next();
/*      */ 
/*  701 */             if (p.contains(gf.createPoint(cnty.getCentriod()))) {
/*  702 */               rt.add(cnty);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  709 */     return rt;
/*      */   }
/*      */ 
/*      */   public List<SPCCounty> getActiveCountiesOutsideWB()
/*      */   {
/*  719 */     if ((this.countyList == null) || (this.countyList.isEmpty())) {
/*  720 */       return null;
/*      */     }
/*      */ 
/*  724 */     List rt = new ArrayList();
/*  725 */     Geometry union = getCountyUnion();
/*  726 */     GeometryFactory gf = new GeometryFactory();
/*  727 */     Polygon largestPoly = null;
/*      */     Polygon poly;
/*  730 */     for (int ii = 0; ii < union.getNumGeometries(); ii++) {
/*  731 */       poly = (Polygon)union.getGeometryN(ii);
/*  732 */       if (largestPoly == null)
/*  733 */         largestPoly = poly;
/*  734 */       else if (poly.getArea() > largestPoly.getArea()) {
/*  735 */         largestPoly = poly;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  742 */     for (SPCCounty cnty : this.countyList)
/*      */     {
/*  744 */       if (!largestPoly.contains(gf.createPoint(cnty.getCentriod()))) {
/*  745 */         rt.add(cnty);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  750 */     return rt;
/*      */   }
/*      */ 
/*      */   public Geometry getCountyUnion()
/*      */   {
/*  760 */     Collection gCollection = new ArrayList();
/*      */ 
/*  763 */     for (SPCCounty cnty : this.countyList) {
/*  764 */       Geometry countyGeo = cnty.getShape();
/*  765 */       if (countyGeo != null) {
/*  766 */         if (!countyGeo.isValid()) {
/*  767 */           System.out.println("Invalid county: " + cnty.getFips());
/*      */         }
/*  769 */         gCollection.add(countyGeo.buffer(0.04D));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  774 */     GeometryFactory gf = new GeometryFactory();
/*      */ 
/*  776 */     GeometryCollection geometryCollection = (GeometryCollection)gf
/*  777 */       .buildGeometry(gCollection);
/*      */ 
/*  779 */     return geometryCollection.union();
/*      */   }
/*      */ 
/*      */   public String getBoxShape()
/*      */   {
/*  784 */     return this.boxShape.toString();
/*      */   }
/*      */ 
/*      */   public void setIssueStatus(String issueStatus) {
/*  788 */     this.issueStatus = issueStatus;
/*      */   }
/*      */ 
/*      */   public String getIssueStatus() {
/*  792 */     return this.issueStatus;
/*      */   }
/*      */ 
/*      */   public void setIssueTime(Calendar issueTime) {
/*  796 */     this.issueTime = issueTime;
/*      */   }
/*      */ 
/*      */   public Calendar getIssueTime() {
/*  800 */     return this.issueTime;
/*      */   }
/*      */ 
/*      */   public void setExpTime(Calendar expTime) {
/*  804 */     this.expTime = expTime;
/*      */   }
/*      */ 
/*      */   public Calendar getExpTime() {
/*  808 */     return this.expTime;
/*      */   }
/*      */ 
/*      */   public void setSeverity(String severity) {
/*  812 */     this.severity = severity;
/*      */   }
/*      */ 
/*      */   public String getSeverity() {
/*  816 */     return this.severity;
/*      */   }
/*      */ 
/*      */   public void setTimeZone(String timeZone) {
/*  820 */     this.timeZone = timeZone;
/*      */   }
/*      */ 
/*      */   public String getTimeZone() {
/*  824 */     return this.timeZone;
/*      */   }
/*      */ 
/*      */   public void setHailSize(float hailSize) {
/*  828 */     this.hailSize = hailSize;
/*      */   }
/*      */ 
/*      */   public float getHailSize() {
/*  832 */     return this.hailSize;
/*      */   }
/*      */ 
/*      */   public void setGust(int gust) {
/*  836 */     this.gust = gust;
/*      */   }
/*      */ 
/*      */   public int getGust() {
/*  840 */     return this.gust;
/*      */   }
/*      */ 
/*      */   public void setTop(int top) {
/*  844 */     this.top = top;
/*      */   }
/*      */ 
/*      */   public int getTop() {
/*  848 */     return this.top;
/*      */   }
/*      */ 
/*      */   public void setMoveDir(int moveDir) {
/*  852 */     this.moveDir = moveDir;
/*      */   }
/*      */ 
/*      */   public int getMoveDir() {
/*  856 */     return this.moveDir;
/*      */   }
/*      */ 
/*      */   public void setMoveSpeed(int moveSpeed) {
/*  860 */     this.moveSpeed = moveSpeed;
/*      */   }
/*      */ 
/*      */   public int getMoveSpeed() {
/*  864 */     return this.moveSpeed;
/*      */   }
/*      */ 
/*      */   public void setStatesIncl(String statesIncl) {
/*  868 */     this.statesIncl = statesIncl;
/*      */   }
/*      */ 
/*      */   public String getStatesIncl() {
/*  872 */     return this.statesIncl;
/*      */   }
/*      */ 
/*      */   public void setAdjAreas(String adjAreas) {
/*  876 */     this.adjAreas = adjAreas;
/*      */   }
/*      */ 
/*      */   public String getAdjAreas() {
/*  880 */     return this.adjAreas;
/*      */   }
/*      */ 
/*      */   public void setReplWatch(int replWatch) {
/*  884 */     this.replWatch = replWatch;
/*      */   }
/*      */ 
/*      */   public int getReplWatch() {
/*  888 */     return this.replWatch;
/*      */   }
/*      */ 
/*      */   public void setIssueFlag(int issueFlag) {
/*  892 */     this.issueFlag = issueFlag;
/*      */   }
/*      */ 
/*      */   public int getIssueFlag() {
/*  896 */     return this.issueFlag;
/*      */   }
/*      */ 
/*      */   public void setWatchType(String watchType) {
/*  900 */     this.watchType = watchType;
/*      */   }
/*      */ 
/*      */   public String getWatchType() {
/*  904 */     return this.watchType;
/*      */   }
/*      */ 
/*      */   public void setForecaster(String forecaster) {
/*  908 */     this.forecaster = forecaster;
/*      */   }
/*      */ 
/*      */   public String getForecaster() {
/*  912 */     return this.forecaster;
/*      */   }
/*      */ 
/*      */   public void setWatchNumber(int watchNumber) {
/*  916 */     this.watchNumber = watchNumber;
/*      */   }
/*      */ 
/*      */   public int getWatchNumber() {
/*  920 */     return this.watchNumber;
/*      */   }
/*      */ 
/*      */   public String convert2Text()
/*      */   {
/*  925 */     String msg = "";
/*  926 */     msg = msg + String.format("WATCH NUMBER:\t\t%1$s\n", new Object[] { Integer.valueOf(getWatchNumber()) });
/*  927 */     msg = msg + String.format("TYPE:\t\t\t%1$s\n", new Object[] { getWatchType() });
/*  928 */     msg = msg + String.format("PDS/Normal:\t\t%1$s\n", new Object[] { getSeverity() });
/*  929 */     msg = msg + "ISSUE TIME:\t\tXX XX XXXX XXXX\n";
/*  930 */     msg = msg + "VALID TIME:\t\tXX XX XXXX XXXX\n";
/*  931 */     msg = msg + String.format("EXPIRATION TIME:\t%1$tm %1$td %1$tY %1$tY\n", new Object[] { 
/*  932 */       getExpTime() });
/*  933 */     msg = msg + String.format("ENDPOINT (ANC,sm):\t%1$s - %2$s\n", new Object[] { 
/*  934 */       getRelative((Coordinate)getPoints().get(0), getAnchors()[0]).trim(), 
/*  935 */       getRelative((Coordinate)getPoints().get(4), getAnchors()[1]).trim() });
/*  936 */     msg = msg + String.format(
/*  937 */       "ENDPOINT (VOR,nm):\t%1$s - %2$s\n", new Object[] { 
/*  938 */       getRelative((Coordinate)getPoints().get(0), 
/*  939 */       getNearestVor((Coordinate)getPoints().get(0))).trim(), 
/*  940 */       getRelative((Coordinate)getPoints().get(4), 
/*  941 */       getNearestVor((Coordinate)getPoints().get(4))).trim() });
/*  942 */     msg = msg + String.format("ATTRIB (ANC,sm):\t%1$-4.0f\n", new Object[] { 
/*  943 */       Double.valueOf(getHalfWidth() / 
/*  943 */       1609.3399658203125D) });
/*  944 */     msg = msg + String.format("ATTRIB (VOR,nm):\t%1$-4d\n", new Object[] { 
/*  945 */       Long.valueOf(Math.round(getHalfWidth() / 1852.0D / 5.0D) * 5L) });
/*  946 */     msg = msg + String.format("WATCH CORNER POINT:\t%1$-6.2f %2$-6.2f\n", new Object[] { 
/*  947 */       Double.valueOf(((Coordinate)getPoints().get(1)).y), Double.valueOf(((Coordinate)getPoints().get(1)).x) });
/*  948 */     msg = msg + String.format("WATCH CORNER POINT:\t%1$-6.2f %2$-6.2f\n", new Object[] { 
/*  949 */       Double.valueOf(((Coordinate)getPoints().get(3)).y), Double.valueOf(((Coordinate)getPoints().get(3)).x) });
/*  950 */     msg = msg + String.format("WATCH CORNER POINT:\t%1$-6.2f %2$-6.2f\n", new Object[] { 
/*  951 */       Double.valueOf(((Coordinate)getPoints().get(5)).y), Double.valueOf(((Coordinate)getPoints().get(5)).x) });
/*  952 */     msg = msg + String.format("WATCH CORNER POINT:\t%1$-6.2f %2$-6.2f\n", new Object[] { 
/*  953 */       Double.valueOf(((Coordinate)getPoints().get(7)).y), Double.valueOf(((Coordinate)getPoints().get(7)).x) });
/*  954 */     msg = msg + String.format("HAIL SIZE (in):\t\t%1$-4.2f\n", new Object[] { Float.valueOf(getHailSize()) });
/*  955 */     msg = msg + String.format("MAX GUSTS (kts):\t%1$-4d\n", new Object[] { Integer.valueOf(getGust()) });
/*  956 */     msg = msg + String.format("MAX TOPS (100s ft):\t%1$-6d\n", new Object[] { Integer.valueOf(getTop()) });
/*  957 */     msg = msg + String.format("MOTION (deg,kts):\t%1$-4d %2$-4d\n", new Object[] { 
/*  958 */       Integer.valueOf(getMoveDir()), Integer.valueOf(getMoveSpeed()) });
/*  959 */     msg = msg + String.format("TIME ZONE:\t\t%1$s\n", new Object[] { getTimeZone() });
/*  960 */     msg = msg + String.format("REPL WATCH NUMBER:\t%1$s\n", new Object[] { Integer.valueOf(getReplWatch()) });
/*  961 */     msg = msg + String.format("STATES INCLUDED:\t%1$s\n", new Object[] { getStatesIncl() });
/*  962 */     msg = msg + String.format("STATUS:\t\t\t%1$s\n", new Object[] { getIssueStatus() });
/*  963 */     msg = msg + String.format("FORECASTER:\t\t%1$s\n", new Object[] { getForecaster() });
/*  964 */     msg = msg + String.format("WATCH AREA (sq nm):\t%1$-8.0f\n", new Object[] { Double.valueOf(getWatchArea()) });
/*  965 */     msg = msg + "UGC   State County Name   Lat/Long    CntyFips WFO\n";
/*  966 */     msg = msg + formatCountyInfo(getCountyList());
/*      */ 
/*  968 */     return msg;
/*      */   }
/*      */ 
/*      */   public String generateWOU()
/*      */   {
/*  973 */     String res = "";
/*  974 */     String wbFile = "ww0002.xml";
/*  975 */     String xsltFile = "wou.xlt";
/*      */ 
/*  980 */     Source xmlSource = new StreamSource(wbFile);
/*      */ 
/*  985 */     Source xsltSource = new StreamSource(xsltFile);
/*      */ 
/*  990 */     TransformerFactory transFact = TransformerFactory.newInstance();
/*      */     try {
/*  992 */       Transformer trans = transFact.newTransformer(xsltSource);
/*      */ 
/*  997 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*      */ 
/*  999 */       trans.transform(xmlSource, new StreamResult(baos));
/*      */ 
/* 1003 */       res = new String(baos.toByteArray());
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1008 */       e.printStackTrace();
/*      */     }
/*      */ 
/* 1012 */     System.out.print(res);
/*      */ 
/* 1014 */     return res;
/*      */   }
/*      */ 
/*      */   public boolean hasStatusLine()
/*      */   {
/* 1022 */     AbstractDrawableComponent adc = getParent();
/* 1023 */     if (((adc instanceof DECollection)) && 
/* 1024 */       (adc.getName().equalsIgnoreCase("Watch"))) {
/* 1025 */       Iterator it = ((DECollection)adc)
/* 1026 */         .createDEIterator();
/* 1027 */       while (it.hasNext()) {
/* 1028 */         DrawableElement de = (DrawableElement)it.next();
/* 1029 */         if (((de instanceof Line)) && 
/* 1030 */           (de.getPgenType().equalsIgnoreCase("POINTED_ARROW"))) {
/* 1031 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1036 */     return false;
/*      */   }
/*      */ 
/*      */   public void saveToFile(String filename)
/*      */   {
/* 1046 */     Layer defaultLayer = new Layer();
/*      */ 
/* 1048 */     defaultLayer.addElement(getParent());
/*      */ 
/* 1050 */     Product defaultProduct = new Product();
/* 1051 */     defaultProduct.addLayer(defaultLayer);
/*      */ 
/* 1053 */     ArrayList prds = new ArrayList();
/* 1054 */     prds.add(defaultProduct);
/* 1055 */     Products filePrds = ProductConverter.convert(prds);
/*      */ 
/* 1057 */     FileTools.write(filename, filePrds);
/*      */   }
/*      */ 
/*      */   public String storeProduct(String label)
/*      */   {
/* 1067 */     Layer defaultLayer = new Layer();
/* 1068 */     defaultLayer.addElement(getParent());
/* 1069 */     ArrayList layerList = new ArrayList();
/* 1070 */     layerList.add(defaultLayer);
/*      */ 
/* 1072 */     ProductTime refTime = new ProductTime(getIssueTime());
/*      */ 
/* 1074 */     Product defaultProduct = new Product("", "WATCHBOX", this.forecaster, null, 
/* 1075 */       refTime, layerList);
/*      */ 
/* 1077 */     defaultProduct.setOutputFile(label);
/* 1078 */     defaultProduct.setCenter(PgenUtil.getCurrentOffice());
/*      */     try
/*      */     {
/* 1081 */       this.dataURI = StorageUtils.storeProduct(defaultProduct, true);
/*      */     } catch (PgenStorageException e) {
/* 1083 */       StorageUtils.showError(e);
/* 1084 */       return null;
/*      */     }
/*      */ 
/* 1087 */     return this.dataURI;
/*      */   }
/*      */ 
/*      */   public void setContWatch(String contWatch) {
/* 1091 */     this.contWatch = contWatch;
/*      */   }
/*      */ 
/*      */   public String getContWatch() {
/* 1095 */     return this.contWatch;
/*      */   }
/*      */ 
/*      */   public void setEndPointAnc(String endPointAnc) {
/* 1099 */     this.endPointAnc = endPointAnc;
/*      */   }
/*      */ 
/*      */   public String getEndPointAnc() {
/* 1103 */     return this.endPointAnc;
/*      */   }
/*      */ 
/*      */   public void setEndPointVor(String endPointVor) {
/* 1107 */     this.endPointVor = endPointVor;
/*      */   }
/*      */ 
/*      */   public String getEndPointVor() {
/* 1111 */     return this.endPointVor;
/*      */   }
/*      */ 
/*      */   public void setHalfWidthSm(int halfWidthSm) {
/* 1115 */     this.halfWidthSm = halfWidthSm;
/*      */   }
/*      */ 
/*      */   public int getHalfWidthSm() {
/* 1119 */     return this.halfWidthSm;
/*      */   }
/*      */ 
/*      */   public void setHalfWidthNm(int halfWidthNm) {
/* 1123 */     this.halfWidthNm = halfWidthNm;
/*      */   }
/*      */ 
/*      */   public int getHalfWidthNm() {
/* 1127 */     return this.halfWidthNm;
/*      */   }
/*      */ 
/*      */   public void setWathcAreaNm(int wathcAreaNm) {
/* 1131 */     this.wathcAreaNm = wathcAreaNm;
/*      */   }
/*      */ 
/*      */   public int getWathcAreaNm() {
/* 1135 */     return this.wathcAreaNm;
/*      */   }
/*      */ 
/*      */   public void setCntyInfo(String cntyInfo) {
/* 1139 */     this.cntyInfo = cntyInfo;
/*      */   }
/*      */ 
/*      */   public String getCntyInfo() {
/* 1143 */     return this.cntyInfo;
/*      */   }
/*      */ 
/*      */   public String getFromLine()
/*      */   {
/* 1151 */     if ((this.statusHistory != null) && (!this.statusHistory.isEmpty())) {
/* 1152 */       return ((WatchStatus)this.statusHistory.get(this.statusHistory.size() - 1)).fromLine;
/*      */     }
/* 1154 */     return null;
/*      */   }
/*      */ 
/*      */   public int getDiscussion()
/*      */   {
/* 1162 */     if ((this.statusHistory != null) && (!this.statusHistory.isEmpty())) {
/* 1163 */       return ((WatchStatus)this.statusHistory.get(this.statusHistory.size() - 1)).discussion;
/*      */     }
/* 1165 */     return 0;
/*      */   }
/*      */ 
/*      */   public Calendar getStatusValidTime()
/*      */   {
/* 1173 */     if ((this.statusHistory != null) && (!this.statusHistory.isEmpty())) {
/* 1174 */       return ((WatchStatus)this.statusHistory.get(this.statusHistory.size() - 1)).statusValidTime;
/*      */     }
/* 1176 */     return null;
/*      */   }
/*      */ 
/*      */   public Calendar getStatusExpTime()
/*      */   {
/* 1184 */     if ((this.statusHistory != null) && (!this.statusHistory.isEmpty())) {
/* 1185 */       return ((WatchStatus)this.statusHistory.get(this.statusHistory.size() - 1)).statusExpTime;
/*      */     }
/* 1187 */     return null;
/*      */   }
/*      */ 
/*      */   public String getStatusForecaster()
/*      */   {
/* 1196 */     if ((this.statusHistory != null) && (!this.statusHistory.isEmpty())) {
/* 1197 */       return ((WatchStatus)this.statusHistory.get(this.statusHistory.size() - 1)).statusForecaster;
/*      */     }
/* 1199 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDataURI() {
/* 1203 */     return this.dataURI;
/*      */   }
/*      */ 
/*      */   public static Coordinate snapOnAnchor(Station anchor, Coordinate point)
/*      */   {
/* 1218 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/* 1219 */     gc.setStartingGeographicPoint(anchor.getLongitude().floatValue(), 
/* 1220 */       anchor.getLatitude().floatValue());
/* 1221 */     gc.setDestinationGeographicPoint(point.x, point.y);
/*      */ 
/* 1223 */     double dis = 
/* 1224 */       Math.round((float)(gc.getOrthodromicDistance() / 1609.3399658203125D) / 5.0F) * 5.0D;
/* 1225 */     double angle = Math.round(gc.getAzimuth() / 22.5D) * 22.5D;
/*      */ 
/* 1227 */     gc.setDirection(angle, dis * 1609.3399658203125D);
/* 1228 */     Point2D pt1 = gc.getDestinationGeographicPoint();
/*      */ 
/* 1230 */     return new Coordinate(pt1.getX(), pt1.getY());
/*      */   }
/*      */ 
/*      */   public static Station getNearestAnchorPt(Coordinate pt, List<Station> anchorList)
/*      */   {
/* 1246 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/* 1247 */     gc.setStartingGeographicPoint(pt.x, pt.y);
/*      */ 
/* 1249 */     double dist = 0.0D;
/* 1250 */     double minDist = -1.0D;
/* 1251 */     Station anchor = null;
/*      */ 
/* 1254 */     for (Station stn : anchorList) {
/* 1255 */       gc.setDestinationGeographicPoint(stn.getLongitude().floatValue(), 
/* 1256 */         stn.getLatitude().floatValue());
/*      */       try
/*      */       {
/* 1259 */         dist = gc.getOrthodromicDistance();
/*      */       } catch (Exception e) {
/* 1261 */         dist = 1.7976931348623157E+308D;
/*      */       }
/*      */ 
/* 1264 */       if ((minDist < 0.0D) || (dist < minDist)) {
/* 1265 */         minDist = dist;
/* 1266 */         anchor = stn;
/*      */       }
/*      */     }
/*      */ 
/* 1270 */     return anchor;
/*      */   }
/*      */ 
/*      */   public static ArrayList<Coordinate> generateWatchBoxPts(WatchShape ws, double halfWidth, Coordinate point1, Coordinate point2)
/*      */   {
/*      */     ArrayList watchBoxPts;
/*      */     ArrayList watchBoxPts;
/* 1292 */     if (ws == WatchShape.NS) {
/* 1293 */       watchBoxPts = generateWatchBoxPts(0.0D, halfWidth, point1, point2);
/*      */     }
/*      */     else
/*      */     {
/*      */       ArrayList watchBoxPts;
/* 1294 */       if (ws == WatchShape.EW) {
/* 1295 */         watchBoxPts = generateWatchBoxPts(90.0D, halfWidth, point1, point2);
/*      */       }
/*      */       else
/*      */       {
/*      */         ArrayList watchBoxPts;
/* 1296 */         if (ws == WatchShape.ESOL)
/*      */         {
/*      */           double dir;
/*      */           double dir;
/* 1299 */           if (Math.abs(point1.x - point2.x) < 0.0001D) {
/* 1300 */             dir = 90.0D;
/*      */           } else {
/* 1302 */             dir = 180.0D - 
/* 1303 */               Math.atan((point2.y - point1.y) / (
/* 1304 */               point2.x - point1.x)) * 180.0D / 3.141592653589793D;
/* 1305 */             if (dir > 180.0D) {
/* 1306 */               dir -= 360.0D;
/*      */             }
/*      */           }
/*      */ 
/* 1310 */           watchBoxPts = generateWatchBoxPts(dir, halfWidth, point1, point2);
/*      */         }
/*      */         else {
/* 1313 */           watchBoxPts = null;
/*      */         }
/*      */       }
/*      */     }
/* 1316 */     return watchBoxPts;
/*      */   }
/*      */ 
/*      */   private static ArrayList<Coordinate> generateWatchBoxPts(double direction, double halfWidth, Coordinate point1, Coordinate point2)
/*      */   {
/* 1334 */     double dir1 = direction + 180.0D;
/* 1335 */     if (dir1 > 180.0D) {
/* 1336 */       dir1 -= 360.0D;
/*      */     }
/* 1338 */     halfWidth = Math.round(halfWidth / 1609.3399658203125D / 5.0D) * 5L;
/* 1339 */     halfWidth *= 1609.3399658203125D;
/*      */ 
/* 1341 */     ArrayList watchBoxPts = new ArrayList();
/*      */ 
/* 1343 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*      */ 
/* 1345 */     Coordinate pt1 = new Coordinate(point1.x, point1.y);
/*      */ 
/* 1347 */     gc.setStartingGeographicPoint(point1.x, point1.y);
/* 1348 */     gc.setDirection(dir1, halfWidth);
/* 1349 */     Point2D pt = gc.getDestinationGeographicPoint();
/*      */ 
/* 1351 */     Coordinate pt2 = new Coordinate(pt.getX(), pt.getY());
/*      */ 
/* 1353 */     gc.setDirection(direction, halfWidth);
/* 1354 */     pt = gc.getDestinationGeographicPoint();
/* 1355 */     Coordinate pt8 = new Coordinate(pt.getX(), pt.getY());
/*      */ 
/* 1357 */     gc.setStartingGeographicPoint(point2.x, point2.y);
/* 1358 */     gc.setDirection(dir1, halfWidth);
/* 1359 */     pt = gc.getDestinationGeographicPoint();
/* 1360 */     Coordinate pt4 = new Coordinate(pt.getX(), pt.getY());
/*      */ 
/* 1362 */     Coordinate pt3 = new Coordinate((pt2.x + pt4.x) / 2.0D, 
/* 1363 */       (pt2.y + pt4.y) / 2.0D);
/*      */ 
/* 1365 */     Coordinate pt5 = new Coordinate(point2.x, point2.y);
/*      */ 
/* 1367 */     gc.setDirection(direction, halfWidth);
/* 1368 */     pt = gc.getDestinationGeographicPoint();
/* 1369 */     Coordinate pt6 = new Coordinate(pt.getX(), pt.getY());
/*      */ 
/* 1371 */     Coordinate pt7 = new Coordinate((pt6.x + pt8.x) / 2.0D, 
/* 1372 */       (pt6.y + pt8.y) / 2.0D);
/*      */ 
/* 1374 */     watchBoxPts.add(pt1);
/* 1375 */     watchBoxPts.add(pt2);
/* 1376 */     watchBoxPts.add(pt3);
/* 1377 */     watchBoxPts.add(pt4);
/* 1378 */     watchBoxPts.add(pt5);
/* 1379 */     watchBoxPts.add(pt6);
/* 1380 */     watchBoxPts.add(pt7);
/* 1381 */     watchBoxPts.add(pt8);
/*      */ 
/* 1383 */     return watchBoxPts;
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> createNewWatchBox(int ptIdx, Coordinate loc, WatchShape ws)
/*      */   {
/* 1399 */     Coordinate newPt0 = new Coordinate();
/* 1400 */     Coordinate newPt4 = new Coordinate();
/*      */ 
/* 1402 */     double newWidth = getNewHalfWidth(ptIdx, loc, newPt0, newPt4);
/* 1403 */     return generateWatchBoxPts(ws, newWidth, newPt0, newPt4);
/*      */   }
/*      */ 
/*      */   private double getNewHalfWidth(int ptIdx, Coordinate loc, Coordinate newWbPt0, Coordinate newWbPt4)
/*      */   {
/* 1426 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/* 1427 */     Coordinate newPt0 = new Coordinate();
/* 1428 */     Coordinate newPt4 = new Coordinate();
/* 1429 */     Coordinate newPtx = new Coordinate();
/*      */ 
/* 1431 */     Coordinate pt0 = (Coordinate)getPoints().get(0);
/* 1432 */     Coordinate pt4 = (Coordinate)getPoints().get(4);
/* 1433 */     Coordinate pt5 = (Coordinate)getPoints().get(5);
/* 1434 */     Coordinate pt7 = (Coordinate)getPoints().get(7);
/* 1435 */     Coordinate pt1 = (Coordinate)getPoints().get(1);
/* 1436 */     Coordinate pt3 = (Coordinate)getPoints().get(3);
/*      */ 
/* 1438 */     double halfWidth = 0.0D;
/*      */ 
/* 1442 */     if (ptIdx == 0)
/*      */     {
/* 1444 */       gc.setStartingGeographicPoint(pt0.x, pt0.y);
/* 1445 */       gc.setDestinationGeographicPoint(pt1.x, pt1.y);
/*      */ 
/* 1447 */       halfWidth = gc.getOrthodromicDistance();
/*      */ 
/* 1449 */       newWbPt0.x = loc.x;
/* 1450 */       newWbPt0.y = loc.y;
/* 1451 */       newWbPt4.x = pt4.x;
/* 1452 */       newWbPt4.y = pt4.y;
/*      */     }
/* 1454 */     else if (ptIdx == 4)
/*      */     {
/* 1456 */       gc.setStartingGeographicPoint(pt0.x, pt0.y);
/* 1457 */       gc.setDestinationGeographicPoint(pt1.x, pt1.y);
/*      */ 
/* 1459 */       halfWidth = gc.getOrthodromicDistance();
/*      */ 
/* 1461 */       newWbPt0.x = pt0.x;
/* 1462 */       newWbPt0.y = pt0.y;
/* 1463 */       newWbPt4.x = loc.x;
/* 1464 */       newWbPt4.y = loc.y;
/*      */     }
/* 1466 */     else if ((ptIdx == 1) || (ptIdx == 7))
/*      */     {
/* 1468 */       if (getWatchBoxShape() == WatchShape.NS) {
/* 1469 */         if (pt0.x == pt4.x)
/* 1470 */           newPt0.y = loc.y;
/*      */         else {
/* 1472 */           pt4.y += (loc.x - pt4.x) * (pt4.y - pt0.y) / (
/* 1473 */             pt4.x - pt0.x);
/*      */         }
/*      */ 
/* 1476 */         newPt0.x = loc.x;
/*      */       }
/* 1478 */       else if (getWatchBoxShape() == WatchShape.EW) {
/* 1479 */         if (pt0.y == pt4.y)
/* 1480 */           newPt0.x = loc.x;
/*      */         else {
/* 1482 */           pt4.x += (loc.y - pt4.y) * (pt4.x - pt0.x) / (
/* 1483 */             pt4.y - pt0.y);
/*      */         }
/* 1485 */         newPt0.y = loc.y;
/* 1486 */       } else if (getWatchBoxShape() == WatchShape.ESOL) {
/* 1487 */         getDistanceFromLine(loc, pt0, pt4, newPt0);
/*      */       }
/*      */ 
/* 1490 */       gc.setStartingGeographicPoint(loc.x, loc.y);
/* 1491 */       gc.setDestinationGeographicPoint(newPt0.x, newPt0.y);
/*      */ 
/* 1493 */       halfWidth = gc.getOrthodromicDistance();
/*      */ 
/* 1495 */       newWbPt0.x = newPt0.x;
/* 1496 */       newWbPt0.y = newPt0.y;
/*      */ 
/* 1498 */       newWbPt4.x = pt4.x;
/* 1499 */       newWbPt4.y = pt4.y;
/* 1500 */     } else if ((ptIdx == 3) || (ptIdx == 5))
/*      */     {
/* 1502 */       if (getWatchBoxShape() == WatchShape.NS) {
/* 1503 */         if (pt0.x == pt4.x)
/* 1504 */           newPt4.y = loc.y;
/*      */         else {
/* 1506 */           pt0.y += (loc.x - pt0.x) * (pt4.y - pt0.y) / (
/* 1507 */             pt4.x - pt0.x);
/*      */         }
/*      */ 
/* 1510 */         newPt4.x = loc.x;
/*      */       }
/* 1512 */       else if (getWatchBoxShape() == WatchShape.EW) {
/* 1513 */         if (pt0.y == pt4.y)
/* 1514 */           newPt4.x = loc.x;
/*      */         else {
/* 1516 */           pt0.x += (loc.y - pt0.y) * (pt4.x - pt0.x) / (
/* 1517 */             pt4.y - pt0.y);
/*      */         }
/* 1519 */         newPt4.y = loc.y;
/* 1520 */       } else if (getWatchBoxShape() == WatchShape.ESOL) {
/* 1521 */         getDistanceFromLine(loc, pt0, pt4, newPt4);
/*      */       }
/*      */ 
/* 1524 */       gc.setStartingGeographicPoint(loc.x, loc.y);
/* 1525 */       gc.setDestinationGeographicPoint(newPt4.x, newPt4.y);
/*      */ 
/* 1527 */       halfWidth = gc.getOrthodromicDistance();
/*      */ 
/* 1529 */       newWbPt4.x = newPt4.x;
/* 1530 */       newWbPt4.y = newPt4.y;
/*      */ 
/* 1532 */       newWbPt0.x = pt0.x;
/* 1533 */       newWbPt0.y = pt0.y;
/*      */     }
/* 1535 */     else if (ptIdx == 2)
/*      */     {
/* 1537 */       if (getWatchBoxShape() == WatchShape.NS)
/*      */       {
/* 1539 */         if (pt5.x == pt7.x)
/* 1540 */           newPtx.y = loc.y;
/*      */         else {
/* 1542 */           pt5.y += (loc.x - pt5.x) * (pt7.y - pt5.y) / (
/* 1543 */             pt7.x - pt5.x);
/*      */         }
/* 1545 */         newPtx.x = loc.x;
/*      */       }
/* 1547 */       else if (getWatchBoxShape() == WatchShape.EW) {
/* 1548 */         if (pt5.y == pt7.y)
/* 1549 */           newPtx.x = loc.x;
/*      */         else {
/* 1551 */           pt5.x += (loc.y - pt5.y) * (pt7.x - pt5.x) / (
/* 1552 */             pt7.y - pt5.y);
/*      */         }
/*      */ 
/* 1555 */         newPtx.y = loc.y;
/* 1556 */       } else if (getWatchBoxShape() == WatchShape.ESOL) {
/* 1557 */         getDistanceFromLine(loc, pt5, pt7, newPtx);
/*      */       }
/*      */ 
/* 1561 */       gc.setStartingGeographicPoint(loc.x, loc.y);
/* 1562 */       gc.setDestinationGeographicPoint(newPtx.x, newPtx.y);
/*      */ 
/* 1564 */       halfWidth = gc.getOrthodromicDistance() / 2.0D;
/*      */ 
/* 1566 */       gc.setStartingGeographicPoint(pt7.x, pt7.y);
/* 1567 */       gc.setDestinationGeographicPoint(pt1.x, pt1.y);
/* 1568 */       double angle = gc.getAzimuth();
/* 1569 */       gc.setDirection(angle, halfWidth);
/*      */ 
/* 1571 */       Point2D pt02d = gc.getDestinationGeographicPoint();
/* 1572 */       newWbPt0.x = pt02d.getX();
/* 1573 */       newWbPt0.y = pt02d.getY();
/*      */ 
/* 1575 */       gc.setStartingGeographicPoint(pt5.x, pt5.y);
/* 1576 */       gc.setDestinationGeographicPoint(pt3.x, pt3.y);
/* 1577 */       angle = gc.getAzimuth();
/* 1578 */       gc.setDirection(angle, halfWidth);
/*      */ 
/* 1580 */       Point2D pt42d = gc.getDestinationGeographicPoint();
/*      */ 
/* 1582 */       newWbPt4.x = pt42d.getX();
/* 1583 */       newWbPt4.y = pt42d.getY();
/* 1584 */     } else if (ptIdx == 6)
/*      */     {
/* 1586 */       if (getWatchBoxShape() == WatchShape.NS)
/*      */       {
/* 1588 */         if (pt1.x == pt3.x)
/* 1589 */           newPtx.y = loc.y;
/*      */         else {
/* 1591 */           pt1.y += (loc.x - pt1.x) * (pt3.y - pt1.y) / (
/* 1592 */             pt3.x - pt1.x);
/*      */         }
/* 1594 */         newPtx.x = loc.x;
/*      */       }
/* 1596 */       else if (getWatchBoxShape() == WatchShape.EW) {
/* 1597 */         if (pt1.y == pt3.y)
/* 1598 */           newPtx.x = loc.x;
/*      */         else {
/* 1600 */           pt1.x += (loc.y - pt1.y) * (pt3.x - pt1.x) / (
/* 1601 */             pt3.y - pt1.y);
/*      */         }
/*      */ 
/* 1604 */         newPtx.y = loc.y;
/* 1605 */       } else if (getWatchBoxShape() == WatchShape.ESOL) {
/* 1606 */         getDistanceFromLine(loc, pt1, pt3, newPtx);
/*      */       }
/*      */ 
/* 1610 */       gc.setStartingGeographicPoint(loc.x, loc.y);
/* 1611 */       gc.setDestinationGeographicPoint(newPtx.x, newPtx.y);
/*      */ 
/* 1613 */       halfWidth = gc.getOrthodromicDistance() / 2.0D;
/*      */ 
/* 1615 */       gc.setStartingGeographicPoint(pt1.x, pt1.y);
/* 1616 */       gc.setDestinationGeographicPoint(pt7.x, pt7.y);
/* 1617 */       double angle = gc.getAzimuth();
/*      */ 
/* 1619 */       gc.setDirection(angle, halfWidth);
/*      */ 
/* 1621 */       Point2D pt02d = gc.getDestinationGeographicPoint();
/* 1622 */       newWbPt0.x = pt02d.getX();
/* 1623 */       newWbPt0.y = pt02d.getY();
/*      */ 
/* 1625 */       gc.setStartingGeographicPoint(pt3.x, pt3.y);
/* 1626 */       gc.setDestinationGeographicPoint(pt5.x, pt5.y);
/* 1627 */       angle = gc.getAzimuth();
/* 1628 */       gc.setDirection(angle, halfWidth);
/*      */ 
/* 1630 */       Point2D pt42d = gc.getDestinationGeographicPoint();
/* 1631 */       newWbPt4.x = pt42d.getX();
/* 1632 */       newWbPt4.y = pt42d.getY();
/*      */     }
/*      */ 
/* 1636 */     return halfWidth;
/*      */   }
/*      */ 
/*      */   public List<String> getNearbyWFOs()
/*      */   {
/* 1645 */     Geometry union = getCountyUnion();
/* 1646 */     Geometry bUnion = union.buffer(0.01D);
/*      */ 
/* 1648 */     List counties = new ArrayList();
/*      */ 
/* 1650 */     counties.addAll(PgenStaticDataProvider.getProvider()
/* 1651 */       .getCountiesInGeometry(bUnion));
/* 1652 */     ArrayList nWFOs = new ArrayList();
/*      */ 
/* 1654 */     List wfos = getWFOs();
/* 1655 */     for (SPCCounty cnty : counties) {
/* 1656 */       String wfo = cnty.getWfo();
/*      */ 
/* 1658 */       if (wfo != null) {
/* 1659 */         for (int ii = 0; ii < wfo.length(); ii += 3) {
/* 1660 */           String wfoStr = wfo.substring(ii, 
/* 1661 */             wfo.length() > ii + 3 ? ii + 3 : wfo.length());
/* 1662 */           if ((!wfos.contains(wfoStr)) && (!nWFOs.contains(wfoStr))) {
/* 1663 */             nWFOs.add(wfoStr);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1669 */     return nWFOs;
/*      */   }
/*      */ 
/*      */   public HashMap<String, String> getStateName()
/*      */   {
/* 1679 */     return PgenStaticDataProvider.getProvider().getStateAbrvMap();
/*      */   }
/*      */ 
/*      */   public String createCountyInfo(String state, Calendar exp)
/*      */   {
/* 1691 */     if (stateName == null) {
/* 1692 */       stateName = getStateName();
/*      */     }
/* 1694 */     String ugcStr = "";
/* 1695 */     String oneLine = "";
/* 1696 */     String counties = "";
/* 1697 */     String cities = "";
/* 1698 */     String waters = "";
/*      */ 
/* 1700 */     int lnLength = 65;
/*      */ 
/* 1702 */     int iCiti = 0;
/* 1703 */     int iCnty = 0;
/*      */ 
/* 1705 */     for (SPCCounty county : this.countyList) {
/* 1706 */       if ((county.getState() != null) && 
/* 1707 */         (state.equalsIgnoreCase(county.getState()))) {
/* 1708 */         if (ugcStr.isEmpty()) {
/* 1709 */           ugcStr = county.getUgcId();
/*      */         } else {
/* 1711 */           if (ugcStr.contains("\n"))
/* 1712 */             oneLine = ugcStr
/* 1713 */               .substring(ugcStr.lastIndexOf('\n') + 1);
/*      */           else {
/* 1715 */             oneLine = ugcStr;
/*      */           }
/*      */ 
/* 1718 */           if (oneLine.length() >= lnLength - 4) {
/* 1719 */             ugcStr = ugcStr + "\n";
/*      */           }
/*      */ 
/* 1722 */           ugcStr = ugcStr + "-" + county.getFips().substring(2);
/*      */         }
/*      */ 
/* 1725 */         if (ugcStr.charAt(2) == 'Z')
/*      */         {
/* 1727 */           waters = waters + "\n" + 
/* 1728 */             county.getZoneName().toUpperCase()
/* 1729 */             .replaceAll("_", " ") + "\n";
/* 1730 */         } else if (Integer.valueOf(county.getFips().substring(2)).intValue() > 509) {
/* 1731 */           if (iCiti == 3) {
/* 1732 */             cities = cities + "\n";
/* 1733 */             iCiti = 0;
/*      */           }
/*      */ 
/* 1736 */           String citi = county.getName().toUpperCase();
/* 1737 */           if (iCiti == 0)
/* 1738 */             cities = cities + String.format("%1$-21s", new Object[] { 
/* 1739 */               citi.replaceAll("CITY OF ", "") });
/*      */           else {
/* 1741 */             cities = cities + String.format("%1$-20s", new Object[] { 
/* 1742 */               citi.replaceAll("CITY OF ", "") });
/*      */           }
/* 1744 */           iCiti++;
/*      */         }
/*      */         else {
/* 1747 */           if (iCnty == 3) {
/* 1748 */             counties = counties + "\n";
/* 1749 */             iCnty = 0;
/*      */           }
/*      */ 
/* 1752 */           if (iCnty == 0)
/* 1753 */             counties = counties + String.format("%1$-21s", new Object[] { county.getName() });
/*      */           else {
/* 1755 */             counties = counties + String.format("%1$-20s", new Object[] { county.getName() });
/*      */           }
/*      */ 
/* 1758 */           iCnty++;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1764 */     if (ugcStr.substring(ugcStr.lastIndexOf('\n') + 1).length() >= lnLength - 7) {
/* 1765 */       ugcStr = ugcStr + "\n";
/*      */     }
/*      */ 
/* 1768 */     ugcStr = ugcStr + String.format("-%1td%1$tH%1$tM-\n\n", new Object[] { exp });
/*      */ 
/* 1770 */     if (!ugcStr.endsWith("\n")) {
/* 1771 */       ugcStr = ugcStr + "\n";
/*      */     }
/* 1773 */     if (!cities.isEmpty())
/* 1774 */       cities = cities + "\n";
/* 1775 */     counties = counties + "\n";
/*      */ 
/* 1777 */     String cntyStr = "";
/* 1778 */     if (!ugcStr.isEmpty())
/*      */     {
/* 1780 */       if (ugcStr.charAt(2) == 'Z')
/*      */       {
/* 1782 */         cntyStr = cntyStr + "CW\n\n.    ADJACENT COASTAL WATERS INCLUDED ARE:\n" + 
/* 1783 */           waters;
/*      */       } else {
/* 1785 */         String stName = ((String)stateName.get(state)).toUpperCase();
/*      */ 
/* 1787 */         if (stName != null) {
/* 1788 */           if (state.equalsIgnoreCase("LA"))
/* 1789 */             cntyStr = cntyStr + state + "\n\n" + ".    " + stName + 
/* 1790 */               " PARISHES INCLUDED ARE:\n\n" + 
/* 1791 */               counties.toUpperCase();
/*      */           else {
/* 1793 */             cntyStr = cntyStr + state + "\n\n" + ".    " + stName + 
/* 1794 */               " COUNTIES INCLUDED ARE:\n\n" + 
/* 1795 */               counties.toUpperCase();
/*      */           }
/*      */ 
/* 1798 */           if (!cities.isEmpty()) {
/* 1799 */             cntyStr = cntyStr + "\n\n" + stName + 
/* 1800 */               " INDEPENDENT CITIES INCLUDED ARE:\n\n" + 
/* 1801 */               cities.toUpperCase();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1807 */     return ugcStr.concat(cntyStr);
/*      */   }
/*      */ 
/*      */   private Set<String> findCntyInClst(String fips)
/*      */   {
/* 1818 */     Set rt = 
/* 1819 */       (Set)PgenStaticDataProvider.getProvider().getClstTbl()
/* 1819 */       .get(fips);
/* 1820 */     return rt == null ? new HashSet(Arrays.asList(new String[] { fips })) : rt;
/*      */   }
/*      */ 
/*      */   public void rmClstCnty(SPCCounty county)
/*      */   {
/* 1830 */     if ((county.getFips().isEmpty()) || 
/* 1831 */       (county.getFips().equalsIgnoreCase("00000"))) {
/* 1832 */       removeCounty(county);
/*      */     }
/* 1834 */     for (String fips : findCntyInClst(county.getFips()))
/* 1835 */       removeCounty(PgenStaticDataProvider.getProvider().findCounty(fips));
/*      */   }
/*      */ 
/*      */   public void addClstCnty(SPCCounty county)
/*      */   {
/* 1845 */     if ((county.getFips().isEmpty()) || 
/* 1846 */       (county.getFips().equalsIgnoreCase("00000")))
/* 1847 */       addCounty(county);
/*      */     else
/* 1849 */       for (String fips : findCntyInClst(county.getFips())) {
/* 1850 */         SPCCounty cnty = PgenStaticDataProvider.getProvider()
/* 1851 */           .findCounty(fips);
/* 1852 */         if ((cnty != null) && (!this.countyList.contains(cnty)))
/* 1853 */           addCounty(cnty);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void addStatus(String fromLine, int dNum, Calendar vTime, Calendar eTime, String name)
/*      */   {
/* 1875 */     if (this.statusHistory == null) {
/* 1876 */       this.statusHistory = new ArrayList();
/*      */     }
/*      */ 
/* 1879 */     this.statusHistory.add(new WatchStatus(fromLine, dNum, vTime, eTime, name, null));
/*      */   }
/*      */ 
/*      */   public void rmLastStatus()
/*      */   {
/* 1887 */     if ((this.statusHistory != null) && (!this.statusHistory.isEmpty()))
/* 1888 */       this.statusHistory.remove(this.statusHistory.size() - 1);
/*      */   }
/*      */ 
/*      */   public List<WatchStatus> getStatusHistory()
/*      */   {
/* 1898 */     return this.statusHistory;
/*      */   }
/*      */ 
/*      */   public String formatCountyInfo(List<SPCCounty> cntyList)
/*      */   {
/* 1909 */     String cntyInfo = "";
/* 1910 */     if ((cntyList != null) && (!cntyList.isEmpty()))
/*      */     {
/* 1912 */       for (SPCCounty cnty : cntyList) {
/* 1913 */         String cntyName = cnty.getName().replaceAll("City of ", "")
/* 1914 */           .replaceAll(" City", "");
/* 1915 */         cntyInfo = cntyInfo + String.format(
/* 1916 */           "%1$-7s%2$-5s%3$-12s%4$6.2f%5$8.2f%6$7s%7$5s", new Object[] { 
/* 1917 */           cnty.getUgcId(), cnty.getState(), cntyName, 
/* 1918 */           Double.valueOf(cnty.getCentriod().y), Double.valueOf(cnty.getCentriod().x), 
/* 1919 */           cnty.getFips(), cnty.getWfo() });
/* 1920 */         cntyInfo = cntyInfo + "\n";
/*      */       }
/*      */     }
/* 1923 */     else cntyInfo = "None...None...None...\n";
/*      */ 
/* 1926 */     return cntyInfo;
/*      */   }
/*      */ 
/*      */   public static enum WatchShape
/*      */   {
/*   86 */     NS, EW, ESOL;
/*      */   }
/*      */ 
/*      */   public class WatchStatus
/*      */   {
/*      */     private String fromLine;
/*      */     private int discussion;
/*      */     private Calendar statusValidTime;
/*      */     private Calendar statusExpTime;
/*      */     private String statusForecaster;
/*      */ 
/*      */     private WatchStatus(String fromLine, int dNum, Calendar vTime, Calendar eTime, String name)
/*      */     {
/* 1950 */       this.fromLine = fromLine;
/* 1951 */       this.discussion = dNum;
/* 1952 */       this.statusValidTime = vTime;
/* 1953 */       this.statusExpTime = eTime;
/* 1954 */       this.statusForecaster = name;
/*      */     }
/*      */ 
/*      */     public String getFromLine() {
/* 1958 */       return this.fromLine;
/*      */     }
/*      */ 
/*      */     public void setFromLine(String fromLine) {
/* 1962 */       this.fromLine = fromLine;
/*      */     }
/*      */ 
/*      */     public int getDiscussion() {
/* 1966 */       return this.discussion;
/*      */     }
/*      */ 
/*      */     public void setDiscussion(int discussion) {
/* 1970 */       this.discussion = discussion;
/*      */     }
/*      */ 
/*      */     public Calendar getStatusValidTime() {
/* 1974 */       return this.statusValidTime;
/*      */     }
/*      */ 
/*      */     public void setStatusValidTime(Calendar statusValidTime) {
/* 1978 */       this.statusValidTime = statusValidTime;
/*      */     }
/*      */ 
/*      */     public Calendar getStatusExpTime() {
/* 1982 */       return this.statusExpTime;
/*      */     }
/*      */ 
/*      */     public void setStatusExpTime(Calendar statusExpTime) {
/* 1986 */       this.statusExpTime = statusExpTime;
/*      */     }
/*      */ 
/*      */     public String getStatusForecaster() {
/* 1990 */       return this.statusForecaster;
/*      */     }
/*      */ 
/*      */     public void setStatusForecaster(String statusForecaster) {
/* 1994 */       this.statusForecaster = statusForecaster;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.WatchBox
 * JD-Core Version:    0.6.2
 */