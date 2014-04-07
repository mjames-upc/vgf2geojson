/*      */ package gov.noaa.nws.ncep.ui.pgen.display;
/*      */ 
/*      */ import com.raytheon.uf.common.geospatial.util.WorldWrapCorrector;
/*      */ import com.raytheon.uf.viz.core.DrawableBasics;
/*      */ import com.raytheon.uf.viz.core.DrawableString;
/*      */ import com.raytheon.uf.viz.core.IExtent;
/*      */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*      */ import com.raytheon.uf.viz.core.IGraphicsTarget.HorizontalAlignment;
/*      */ import com.raytheon.uf.viz.core.IGraphicsTarget.TextStyle;
/*      */ import com.raytheon.uf.viz.core.IGraphicsTarget.VerticalAlignment;
/*      */ import com.raytheon.uf.viz.core.IView;
/*      */ import com.raytheon.uf.viz.core.PixelExtent;
/*      */ import com.raytheon.uf.viz.core.data.IRenderedImageCallback;
/*      */ import com.raytheon.uf.viz.core.drawables.IDescriptor;
/*      */ import com.raytheon.uf.viz.core.drawables.IFont;
/*      */ import com.raytheon.uf.viz.core.drawables.IFont.Style;
/*      */ import com.raytheon.uf.viz.core.drawables.IImage;
/*      */ import com.raytheon.uf.viz.core.drawables.IShadedShape;
/*      */ import com.raytheon.uf.viz.core.drawables.IWireframeShape;
/*      */ import com.raytheon.uf.viz.core.drawables.PaintProperties;
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*      */ import com.raytheon.viz.core.rsc.jts.JTSCompiler;
/*      */ import com.raytheon.viz.core.rsc.jts.JTSCompiler.PointStyle;
/*      */ import com.raytheon.viz.ui.color.BackgroundColor;
/*      */ import com.raytheon.viz.ui.color.IBackgroundColorChangedListener.BGColorMode;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.GeometryCollection;
/*      */ import com.vividsolutions.jts.geom.GeometryFactory;
/*      */ import com.vividsolutions.jts.geom.LineString;
/*      */ import com.vividsolutions.jts.geom.Point;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
/*      */ import com.vividsolutions.jts.geom.util.AffineTransformation;
/*      */ import com.vividsolutions.jts.linearref.LengthIndexedLine;
/*      */ import com.vividsolutions.jts.linearref.LengthLocationMap;
/*      */ import com.vividsolutions.jts.linearref.LinearLocation;
/*      */ import com.vividsolutions.jts.linearref.LocationIndexedLine;
/*      */ import com.vividsolutions.jts.operation.distance.DistanceOp;
/*      */ import gov.noaa.nws.ncep.common.staticdata.SPCCounty;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourCircle;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourMinmax;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ComboSymbol;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.SymbolLocationSet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Vector;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.ITcm;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.ITcmFcst;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.ITcmWindQuarter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.TcmFcst;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaClip;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.IGfa;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.AbstractSigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.CcfpInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.ISigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.BPGeography;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.ITca;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.TropicalCycloneAdvisory;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.WaterBreakpoint;
/*      */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*      */ import java.awt.Color;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Double;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.io.PrintStream;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ import org.eclipse.swt.graphics.RGB;
/*      */ import org.eclipse.swt.graphics.Rectangle;
/*      */ import org.geotools.referencing.GeodeticCalculator;
/*      */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*      */ 
/*      */ public class DisplayElementFactory
/*      */ {
/*      */   private IGraphicsTarget target;
/*      */   private IDescriptor iDescriptor;
/*      */   private GeometryFactory gf;
/*      */   private IWireframeShape[] wfs;
/*      */   private IShadedShape ss;
/*      */   private IWireframeShape sym;
/*      */   private ILine elem;
/*  169 */   private double deviceScale = 25.0D;
/*  170 */   private double symbolScale = 0.65D;
/*  171 */   private double screenToExtent = 1.0D;
/*  172 */   double screenToWorldRatio = 1.0D;
/*      */   private ArrowHead arrow;
/*  179 */   private Boolean layerMonoColor = Boolean.valueOf(false);
/*  180 */   private Color layerColor = null;
/*  181 */   private Boolean layerFilled = Boolean.valueOf(false);
/*      */ 
/*  183 */   private BackgroundColor backgroundColor = BackgroundColor.getActivePerspectiveInstance();
/*      */ 
/*      */   public DisplayElementFactory(IGraphicsTarget target, IMapDescriptor mapDescriptor)
/*      */   {
/*  218 */     this.target = target;
/*  219 */     this.iDescriptor = mapDescriptor;
/*  220 */     this.gf = new GeometryFactory();
/*      */   }
/*      */ 
/*      */   public DisplayElementFactory(IGraphicsTarget target, IDescriptor iDescriptor)
/*      */   {
/*  226 */     this.target = target;
/*  227 */     this.iDescriptor = iDescriptor;
/*  228 */     this.gf = new GeometryFactory();
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(ILine de, PaintProperties paintProps, boolean worldWrap)
/*      */   {
/*  241 */     if (worldWrap) {
/*  242 */       this.elem = de;
/*  243 */       ArrayList list = new ArrayList();
/*      */ 
/*  245 */       WorldWrapCorrector corrector = new WorldWrapCorrector(
/*  246 */         this.iDescriptor.getGridGeometry());
/*      */       Coordinate[] coord;
/*  250 */       if (de.isClosedLine().booleanValue()) {
/*  251 */         Coordinate[] coord = new Coordinate[de.getLinePoints().length + 1];
/*  252 */         for (int ii = 0; ii < de.getLinePoints().length; ii++) {
/*  253 */           coord[ii] = new Coordinate(de.getLinePoints()[ii].x, de.getLinePoints()[ii].y);
/*      */         }
/*  255 */         coord[de.getLinePoints().length] = new Coordinate(de.getLinePoints()[0].x, de.getLinePoints()[0].y);
/*      */       }
/*      */       else {
/*  258 */         coord = new Coordinate[de.getLinePoints().length];
/*      */ 
/*  260 */         for (int ii = 0; ii < de.getLinePoints().length; ii++) {
/*  261 */           coord[ii] = new Coordinate(de.getLinePoints()[ii].x, de.getLinePoints()[ii].y);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  268 */       Geometry geo = null;
/*      */       try {
/*  270 */         geo = corrector.correct(GfaClip.getInstance().pointsToLineString(coord));
/*      */       }
/*      */       catch (Exception e) {
/*  273 */         System.out.println("World wrap error: " + e.getMessage());
/*  274 */         return list;
/*      */       }
/*      */ 
/*  277 */       if ((geo != null) && (geo.getNumGeometries() > 1)) {
/*  278 */         for (int ii = 0; ii < geo.getNumGeometries(); ii++) {
/*  279 */           Geometry geo1 = geo.getGeometryN(ii);
/*  280 */           double[][] pixels = PgenUtil.latlonToPixel(geo1.getCoordinates(), (IMapDescriptor)this.iDescriptor);
/*      */           double[][] smoothpts;
/*      */           double[][] smoothpts;
/*  285 */           if (de.getSmoothFactor() > 0) {
/*  286 */             float devScale = 50.0F;
/*      */             float density;
/*      */             float density;
/*  287 */             if (de.getSmoothFactor() == 1) density = devScale / 1.0F; else
/*  288 */               density = devScale / 5.0F;
/*  289 */             smoothpts = CurveFitter.fitParametricCurve(pixels, density);
/*      */           }
/*      */           else {
/*  292 */             smoothpts = pixels;
/*      */           }
/*      */ 
/*  295 */           list.addAll(createDisplayElementsForLines(de, smoothpts, paintProps));
/*      */         }
/*      */ 
/*  302 */         return list;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  307 */     return createDisplayElements(de, paintProps);
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createDisplayElementsForLines(ILine de, double[][] smoothpts, PaintProperties paintProps)
/*      */   {
/*  326 */     Color[] dspClr = getDisplayColors(this.elem.getColors());
/*      */ 
/*  331 */     LinePattern pattern = null;
/*  332 */     LinePatternManager lpl = LinePatternManager.getInstance();
/*      */     try {
/*  334 */       pattern = lpl.getLinePattern(de.getPatternName());
/*      */     }
/*      */     catch (LinePatternException lpe)
/*      */     {
/*  341 */       System.out.println(lpe.getMessage() + ":  Using Solid Line by default.");
/*  342 */       pattern = null;
/*      */     }
/*      */ 
/*  349 */     if ((pattern != null) && (pattern.needsLengthUpdate())) {
/*  350 */       pattern = pattern.updateLength(this.screenToExtent * de.getLineWidth() / (de.getSizeScale() * this.deviceScale));
/*      */     }
/*      */ 
/*  357 */     if (((this.elem instanceof Line)) && (((Line)this.elem).isFlipSide())) pattern = pattern.flipSide();
/*      */ 
/*  363 */     ScaleType scaleType = null;
/*  364 */     if ((pattern != null) && (pattern.getNumSegments() > 0)) {
/*  365 */       scaleType = ScaleType.SCALE_ALL_SEGMENTS;
/*  366 */       if ((this.elem instanceof Line)) {
/*  367 */         Line line = (Line)this.elem;
/*      */ 
/*  370 */         if (line.getPgenCategory().equalsIgnoreCase("Front")) scaleType = ScaleType.SCALE_BLANK_LINE_ONLY;
/*      */       }
/*      */     }
/*      */ 
/*  374 */     boolean isCCFP = false;
/*  375 */     AbstractDrawableComponent adc = ((Line)de).getParent();
/*  376 */     isCCFP = (adc != null) && ("CCFP_SIGMET".equals(adc.getPgenType()));
/*  377 */     DECollection ccfp = null;
/*  378 */     if (isCCFP) {
/*  379 */       ccfp = (DECollection)adc;
/*      */     }
/*      */ 
/*  382 */     ArrayList list = new ArrayList();
/*      */ 
/*  384 */     list.addAll(createDisplayElementsFromPts(smoothpts, dspClr, pattern, scaleType, Boolean.valueOf(getDisplayFillMode(de.isFilled())), 
/*  385 */       de.getLineWidth(), isCCFP, ccfp, paintProps));
/*      */ 
/*  390 */     list.addAll(adjustContourLineLabels(this.elem, paintProps, smoothpts));
/*      */ 
/*  393 */     return list;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createDisplayElementsFromPts(double[][] pts, Color[] dspClr, LinePattern pattern, ScaleType scaleType, Boolean isFilled, float lineWidth, boolean isCCFP, DECollection ccfp, PaintProperties paintProps)
/*      */   {
/*  412 */     ArrayList list = new ArrayList();
/*  413 */     this.wfs = new IWireframeShape[dspClr.length];
/*  414 */     for (int i = 0; i < dspClr.length; i++) {
/*  415 */       this.wfs[i] = this.target.createWireframeShape(false, this.iDescriptor);
/*      */     }
/*  417 */     this.ss = this.target.createShadedShape(false, this.iDescriptor, true);
/*      */ 
/*  422 */     if ((pattern != null) && (pattern.hasArrowHead()))
/*      */     {
/*  426 */       double scale = this.elem.getSizeScale();
/*  427 */       if (scale <= 0.0D) scale = 1.0D;
/*  428 */       double sfactor = this.deviceScale * scale;
/*      */ 
/*  430 */       double pointAngle = 60.0D;
/*  431 */       double extent = pattern.getMaxExtent();
/*      */ 
/*  436 */       double height = sfactor * 3.5D;
/*  437 */       if (extent * 1.5D > 3.5D) {
/*  438 */         height = sfactor * extent * 1.5D;
/*      */       }
/*  440 */       int n = pts.length - 1;
/*      */ 
/*  442 */       double slope = Math.toDegrees(Math.atan2(pts[n][1] - pts[(n - 1)][1], pts[n][0] - pts[(n - 1)][0]));
/*      */ 
/*  444 */       this.arrow = new ArrowHead(new Coordinate(pts[n][0], pts[n][1]), 
/*  445 */         pointAngle, slope, height, pattern.getArrowHeadType());
/*  446 */       Coordinate[] ahead = this.arrow.getArrowHeadShape();
/*      */ 
/*  448 */       if (pattern.getArrowHeadType() == ArrowHead.ArrowHeadType.OPEN)
/*      */       {
/*  450 */         this.wfs[0].addLineSegment(toDouble(ahead));
/*  451 */       }if (pattern.getArrowHeadType() == ArrowHead.ArrowHeadType.FILLED)
/*      */       {
/*  454 */         this.ss.addPolygonPixelSpace(toLineString(ahead), 
/*  455 */           new RGB(dspClr[0].getRed(), dspClr[0].getGreen(), dspClr[0].getBlue()));
/*      */       }
/*      */     }
/*  458 */     if ((pattern != null) && (pattern.getNumSegments() > 0)) {
/*  459 */       handleLinePattern(pattern, pts, scaleType);
/*      */     }
/*      */     else {
/*  462 */       this.wfs[0].addLineSegment(pts);
/*      */     }
/*      */ 
/*  465 */     if (isFilled.booleanValue()) {
/*  466 */       list.add(createFill(pts));
/*      */     }
/*      */ 
/*  472 */     for (int k = 0; k < this.wfs.length; k++)
/*      */     {
/*  474 */       this.wfs[k].compile();
/*  475 */       LineDisplayElement lde = new LineDisplayElement(this.wfs[k], dspClr[k], lineWidth);
/*  476 */       list.add(lde);
/*      */     }
/*      */ 
/*  485 */     this.ss.compile();
/*  486 */     FillDisplayElement fde = new FillDisplayElement(this.ss, this.elem.getColors()[0].getAlpha());
/*  487 */     list.add(fde);
/*      */ 
/*  489 */     if (isCCFP) {
/*  490 */       addCcfpSpeed(list, paintProps, ccfp);
/*      */     }
/*      */ 
/*  494 */     return list;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(ILine de, PaintProperties paintProps)
/*      */   {
/*  509 */     setScales(paintProps);
/*      */ 
/*  514 */     this.elem = de;
/*      */ 
/*  520 */     ArrayList list = new ArrayList();
/*      */ 
/*  525 */     Coordinate[] pts = de.getLinePoints();
/*      */ 
/*  530 */     double[][] pixels = PgenUtil.latlonToPixel(pts, (IMapDescriptor)this.iDescriptor);
/*      */ 
/*  535 */     if (de.isClosedLine().booleanValue())
/*  536 */       pixels = ensureClosed(pixels);
/*      */     double[][] smoothpts;
/*      */     double[][] smoothpts;
/*  542 */     if (de.getSmoothFactor() > 0) {
/*  543 */       float devScale = 50.0F;
/*      */       float density;
/*      */       float density;
/*  544 */       if (de.getSmoothFactor() == 1) density = devScale / 1.0F; else
/*  545 */         density = devScale / 5.0F;
/*  546 */       smoothpts = CurveFitter.fitParametricCurve(pixels, density);
/*      */     }
/*      */     else {
/*  549 */       smoothpts = pixels;
/*      */     }
/*      */ 
/*  552 */     list.addAll(createDisplayElementsForLines(de, smoothpts, paintProps));
/*      */ 
/*  557 */     list.addAll(adjustContourLineLabels(this.elem, paintProps, smoothpts));
/*      */ 
/*  559 */     return list;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(IWatchBox watchBox, PaintProperties paintProps)
/*      */   {
/*  573 */     ArrayList dlist = new ArrayList();
/*      */ 
/*  575 */     List counties = watchBox.getCountyList();
/*  576 */     if ((counties != null) && (!counties.isEmpty()))
/*      */     {
/*      */       Color[] colors;
/*  577 */       if (watchBox.getFillFlag())
/*      */       {
/*  579 */         Geometry cntyUnion = null;
/*  580 */         colors = null;
/*      */ 
/*  582 */         Collection gCollection = new ArrayList();
/*      */ 
/*  585 */         for (SPCCounty cnty : counties) {
/*  586 */           Geometry countyGeo = cnty.getShape();
/*      */ 
/*  588 */           colors = watchBox.getColors();
/*  589 */           colors[1] = watchBox.getFillColor();
/*      */ 
/*  591 */           for (int ii = 0; ii < countyGeo.getNumGeometries(); ii++) {
/*  592 */             Polygon poly = (Polygon)countyGeo.getGeometryN(ii);
/*  593 */             List pts = new ArrayList(Arrays.asList(poly.getCoordinates()));
/*      */ 
/*  595 */             Line cntyBorder = new Line(null, colors, 0.5F, 0.5D, true, 
/*  596 */               false, pts, 0, 
/*  597 */               FillPatternList.FillPattern.FILL_PATTERN_6, "Lines", "LINE_SOLID");
/*  598 */             ArrayList cntyLine = createDisplayElements(cntyBorder, paintProps);
/*  599 */             dlist.addAll(cntyLine);
/*      */           }
/*      */ 
/*  602 */           if (countyGeo != null) {
/*  603 */             gCollection.add(countyGeo.buffer(0.02D));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  609 */         GeometryFactory gf = new GeometryFactory();
/*      */ 
/*  611 */         if (gCollection.size() > 1) {
/*  612 */           GeometryCollection geometryCollection = 
/*  613 */             (GeometryCollection)gf.buildGeometry(gCollection);
/*      */ 
/*  615 */           cntyUnion = geometryCollection.union();
/*      */         } else {
/*  617 */           cntyUnion = gf.buildGeometry(gCollection);
/*      */         }
/*  619 */         IShadedShape theShadedShape = this.target.createShadedShape(false, this.iDescriptor, true);
/*      */ 
/*  623 */         JTSCompiler compiler = new JTSCompiler(theShadedShape, 
/*  624 */           null, this.iDescriptor, JTSCompiler.PointStyle.CROSS);
/*      */         try
/*      */         {
/*  627 */           compiler.handle((Geometry)cntyUnion.clone(), 
/*  628 */             new RGB(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue()));
/*      */ 
/*  630 */           if ((this.elem.getFillPattern() != FillPatternList.FillPattern.TRANSPARENCY) && 
/*  631 */             (this.elem.getFillPattern() != FillPatternList.FillPattern.SOLID)) {
/*  632 */             FillPatternList fpl = new FillPatternList();
/*  633 */             byte[] fpattern = fpl.getFillPattern(this.elem.getFillPattern());
/*  634 */             theShadedShape.setFillPattern(fpattern);
/*      */           }
/*  636 */           theShadedShape.compile();
/*      */ 
/*  640 */           dlist.add(new FillDisplayElement(theShadedShape, 0.5F));
/*      */         }
/*      */         catch (VizException e)
/*      */         {
/*  644 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */       else {
/*  648 */         for (SPCCounty cnty : counties)
/*      */         {
/*  650 */           Symbol cSymbol = new Symbol(null, watchBox.getColors(), 
/*  651 */             watchBox.getWatchSymbolWidth(), watchBox.getWatchSymbolSize(), Boolean.valueOf(false), 
/*  652 */             cnty.getCentriod(), "Marker", watchBox.getWatchSymbolType());
/*  653 */           ArrayList cList = createDisplayElements(cSymbol, paintProps);
/*  654 */           dlist.addAll(cList);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  659 */     Coordinate[] points = watchBox.getLinePoints();
/*  660 */     ArrayList ptsList = new ArrayList();
/*      */ 
/*  662 */     for (int ii = 0; ii < points.length; ii++) {
/*  663 */       ptsList.add(points[ii]);
/*      */     }
/*      */ 
/*  667 */     Line box = new Line(null, watchBox.getColors(), 3.0F, 3.0D, true, 
/*  668 */       false, ptsList, 
/*  669 */       0, FillPatternList.FillPattern.SOLID, "Lines", "LINE_SOLID");
/*  670 */     ArrayList dBox = createDisplayElements(box, paintProps);
/*  671 */     dlist.addAll(dBox);
/*      */ 
/*  674 */     ptsList.clear();
/*  675 */     ptsList.add(points[0]);
/*  676 */     ptsList.add(new Coordinate((points[0].x + points[4].x) / 2.0D, 
/*  677 */       (points[0].y + points[4].y) / 2.0D));
/*  678 */     ptsList.add(points[4]);
/*      */ 
/*  680 */     Line centerLine = new Line(null, watchBox.getColors(), 3.0F, 3.0D, false, 
/*  681 */       false, ptsList, 0, FillPatternList.FillPattern.SOLID, "Lines", "LINE_SOLID");
/*      */ 
/*  683 */     ArrayList dLine = createDisplayElements(centerLine, paintProps);
/*  684 */     dlist.addAll(dLine);
/*      */ 
/*  686 */     Station[] anchors = watchBox.getAnchors();
/*  687 */     Symbol anchor1 = new Symbol(null, watchBox.getColors(), 1.5F, 0.7D, Boolean.valueOf(false), 
/*  688 */       new Coordinate(anchors[0].getLongitude().floatValue(), anchors[0].getLatitude().floatValue()), 
/*  689 */       "Marker", "DIAMOND");
/*      */ 
/*  691 */     ArrayList aList1 = createDisplayElements(anchor1, paintProps);
/*  692 */     dlist.addAll(aList1);
/*      */ 
/*  694 */     Symbol anchor2 = new Symbol(null, watchBox.getColors(), 1.5F, 0.7D, Boolean.valueOf(false), 
/*  695 */       new Coordinate(anchors[1].getLongitude().floatValue(), anchors[1].getLatitude().floatValue()), 
/*  696 */       "Marker", "DIAMOND");
/*      */ 
/*  698 */     ArrayList aList2 = createDisplayElements(anchor2, paintProps);
/*  699 */     dlist.addAll(aList2);
/*      */ 
/*  702 */     if (watchBox.getIssueFlag() != 0)
/*      */     {
/*  704 */       String[] wtext = { String.valueOf(watchBox.getWatchNumber()) };
/*  705 */       Text wNumber = new Text(null, "Courier", 18.0F, 
/*  706 */         IText.TextJustification.CENTER, new Coordinate(watchBox.getLinePoints()[7].x - 0.05D, 
/*  707 */         watchBox.getLinePoints()[7].y - 0.1D), 0.0D, IText.TextRotation.SCREEN_RELATIVE, 
/*  708 */         wtext, IText.FontStyle.REGULAR, watchBox.getColors()[0], 
/*  709 */         0, 0, true, IText.DisplayType.NORMAL, "Text", "Text");
/*  710 */       ArrayList tList = createDisplayElements(wNumber, paintProps);
/*      */ 
/*  712 */       dlist.addAll(tList);
/*      */     }
/*      */ 
/*  715 */     return dlist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElementsOrig(ISymbol de, PaintProperties paintProps)
/*      */   {
/*  731 */     setScales(paintProps);
/*  732 */     double sfactor = this.deviceScale * de.getSizeScale();
/*      */ 
/*  734 */     ArrayList slist = new ArrayList();
/*  735 */     this.sym = this.target.createWireframeShape(false, this.iDescriptor);
/*  736 */     this.ss = this.target.createShadedShape(false, this.iDescriptor, true);
/*  737 */     IWireframeShape mask = this.target.createWireframeShape(false, this.iDescriptor);
/*      */ 
/*  739 */     double[] tmp = { de.getLocation().x, de.getLocation().y, 0.0D };
/*  740 */     double[] center = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/*  745 */     Color[] dspClr = getDisplayColors(de.getColors());
/*      */ 
/*  750 */     SymbolPattern pattern = null;
/*  751 */     SymbolPatternManager spl = SymbolPatternManager.getInstance();
/*      */     try {
/*  753 */       pattern = spl.getSymbolPattern(de.getPatternName());
/*      */     }
/*      */     catch (SymbolPatternException spe) {
/*  756 */       System.out.println(spe.getMessage());
/*  757 */       return slist;
/*      */     }
/*      */ 
/*  760 */     for (SymbolPart spart : pattern.getParts()) {
/*  761 */       Coordinate[] coords = spart.getPath();
/*  762 */       double[][] path = new double[coords.length][3];
/*      */ 
/*  764 */       for (int j = 0; j < coords.length; j++) {
/*  765 */         path[j][0] = (center[0] + sfactor * coords[j].x);
/*  766 */         path[j][1] = (center[1] + -sfactor * coords[j].y);
/*      */       }
/*  768 */       this.sym.addLineSegment(path);
/*  769 */       if (de.isClear().booleanValue()) mask.addLineSegment(path);
/*      */ 
/*  775 */       if (getDisplayFillMode(Boolean.valueOf(spart.isFilled()))) {
/*  776 */         Coordinate[] pixels = new Coordinate[path.length];
/*  777 */         for (int k = 0; k < path.length; k++) {
/*  778 */           pixels[k] = new Coordinate(path[k][0], path[k][1]);
/*      */         }
/*  780 */         this.ss.addPolygonPixelSpace(toLineString(pixels), 
/*  781 */           new RGB(dspClr[0].getRed(), dspClr[0].getGreen(), dspClr[0].getBlue()));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  788 */     this.sym.compile();
/*  789 */     this.ss.compile();
/*      */ 
/*  795 */     if (de.isClear().booleanValue()) {
/*  796 */       RGB bgclr = this.backgroundColor.getColor(IBackgroundColorChangedListener.BGColorMode.EDITOR);
/*  797 */       Color bgcolor = new Color(bgclr.red, bgclr.green, bgclr.blue);
/*  798 */       mask.compile();
/*  799 */       LineDisplayElement ldbg = new LineDisplayElement(mask, bgcolor, de.getLineWidth() + 25.0F);
/*  800 */       slist.add(ldbg);
/*      */     }
/*      */ 
/*  806 */     LineDisplayElement lde = new LineDisplayElement(this.sym, dspClr[0], de.getLineWidth());
/*  807 */     slist.add(lde);
/*      */ 
/*  812 */     FillDisplayElement fde = new FillDisplayElement(this.ss, de.getColors()[0].getAlpha());
/*  813 */     slist.add(fde);
/*      */ 
/*  815 */     return slist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(IKink kline, PaintProperties paintProps)
/*      */   {
/*  825 */     setScales(paintProps);
/*  826 */     double sfactor = this.deviceScale * kline.getSizeScale();
/*      */ 
/*  831 */     ArrayList slist = new ArrayList();
/*  832 */     IWireframeShape kinkLine = this.target.createWireframeShape(false, this.iDescriptor);
/*      */ 
/*  837 */     Color dspClr = getDisplayColor(kline.getColor());
/*      */ 
/*  842 */     double[] tmp = { kline.getStartPoint().x, kline.getStartPoint().y, 0.0D };
/*  843 */     double[] first = this.iDescriptor.worldToPixel(tmp);
/*  844 */     Coordinate startPixel = new Coordinate(first[0], first[1]);
/*  845 */     double[] tmp2 = { kline.getEndPoint().x, kline.getEndPoint().y, 0.0D };
/*  846 */     double[] last = this.iDescriptor.worldToPixel(tmp2);
/*  847 */     Coordinate endPixel = new Coordinate(last[0], last[1]);
/*      */ 
/*  854 */     LineString ls = this.gf.createLineString(new Coordinate[] { startPixel, endPixel });
/*  855 */     LengthIndexedLine lil = new LengthIndexedLine(ls);
/*      */ 
/*  860 */     double kinkLocation = ls.getLength() * kline.getKinkPosition();
/*  861 */     double offset = sfactor * 2.0D;
/*  862 */     CornerPatternApplicator ex = new CornerPatternApplicator(lil, kinkLocation - offset, kinkLocation + offset);
/*  863 */     ex.setHeight(offset);
/*  864 */     ex.setPatternType(CornerPatternApplicator.CornerPattern.X_PATTERN);
/*  865 */     double[][] exes = ex.calculateLines();
/*      */ 
/*  870 */     double[][] coords = { first, exes[3], exes[2], last };
/*  871 */     kinkLine.addLineSegment(coords);
/*      */ 
/*  876 */     double pointAngle = 90.0D;
/*  877 */     double slope = Math.toDegrees(Math.atan2(last[1] - exes[2][1], last[0] - exes[2][0]));
/*      */ 
/*  879 */     ArrowHead arrow = new ArrowHead(new Coordinate(last[0], last[1]), pointAngle, slope, 1.5D * offset, 
/*  880 */       kline.getArrowHeadType());
/*  881 */     Coordinate[] ahead = arrow.getArrowHeadShape();
/*      */ 
/*  883 */     if (kline.getArrowHeadType() == ArrowHead.ArrowHeadType.OPEN)
/*      */     {
/*  885 */       kinkLine.addLineSegment(toDouble(ahead));
/*  886 */     }if (kline.getArrowHeadType() == ArrowHead.ArrowHeadType.FILLED)
/*      */     {
/*  891 */       IShadedShape head = this.target.createShadedShape(false, this.iDescriptor, false);
/*  892 */       head.addPolygonPixelSpace(toLineString(ahead), new RGB(dspClr.getRed(), 
/*  893 */         dspClr.getGreen(), 
/*  894 */         dspClr.getBlue()));
/*  895 */       head.compile();
/*  896 */       slist.add(new FillDisplayElement(head, 1.0F));
/*      */     }
/*      */ 
/*  902 */     kinkLine.compile();
/*  903 */     slist.add(new LineDisplayElement(kinkLine, dspClr, kline.getLineWidth()));
/*  904 */     return slist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(IVector vect, PaintProperties paintProps)
/*      */   {
/*  916 */     setScales(paintProps);
/*      */ 
/*  918 */     ArrayList slist = null;
/*      */ 
/*  923 */     switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$display$IVector$VectorType()[vect.getVectorType().ordinal()])
/*      */     {
/*      */     case 1:
/*  926 */       slist = createArrow(vect);
/*  927 */       break;
/*      */     case 2:
/*  930 */       slist = createWindBarb(vect);
/*  931 */       break;
/*      */     case 3:
/*  934 */       slist = createHashMark(vect);
/*  935 */       break;
/*      */     default:
/*  941 */       return new ArrayList();
/*      */     }
/*      */ 
/*  945 */     return slist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(List<IVector> vectors, PaintProperties paintProps)
/*      */   {
/*  959 */     setScales(paintProps);
/*      */ 
/*  961 */     ArrayList slist = null;
/*      */ 
/*  966 */     if ((vectors == null) || (vectors.size() < 1)) {
/*  967 */       return new ArrayList();
/*      */     }
/*  969 */     switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$display$IVector$VectorType()[((IVector)vectors.get(0)).getVectorType().ordinal()])
/*      */     {
/*      */     case 1:
/*  972 */       slist = createArrows(vectors);
/*  973 */       break;
/*      */     case 2:
/*  976 */       slist = createWindBarbs(vectors);
/*  977 */       break;
/*      */     case 3:
/*  980 */       slist = createHashMarks(vectors);
/*  981 */       break;
/*      */     default:
/*  987 */       return new ArrayList();
/*      */     }
/*      */ 
/*  991 */     return slist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(IText txt, PaintProperties paintProps)
/*      */   {
/* 1001 */     setScales(paintProps);
/*      */ 
/* 1006 */     ArrayList slist = new ArrayList();
/*      */ 
/* 1011 */     if ((((Text)txt).getHide() != null) && (((Text)txt).getHide().booleanValue())) {
/* 1012 */       return slist;
/*      */     }
/*      */ 
/* 1019 */     AbstractDrawableComponent tparent = ((Text)txt).getParent();
/* 1020 */     if (tparent != null) {
/* 1021 */       if (((tparent instanceof ContourLine)) || ((tparent instanceof ContourCircle))) {
/* 1022 */         return slist;
/*      */       }
/* 1024 */       if (((tparent instanceof ContourMinmax)) && 
/* 1025 */         (((Text)txt).getAuto() != null) && (((Text)txt).getAuto().booleanValue())) {
/* 1026 */         Coordinate loc = ((ISinglePoint)((ContourMinmax)tparent).getSymbol()).getLocation();
/* 1027 */         double[] pixel = this.iDescriptor.worldToPixel(new double[] { loc.x, loc.y, 0.0D });
/* 1028 */         double sfactor = this.deviceScale * ((ContourMinmax)tparent).getSymbol().getSizeScale();
/*      */ 
/* 1030 */         pixel[1] += sfactor * 5.0D;
/* 1031 */         double[] nloc = this.iDescriptor.pixelToWorld(new double[] { pixel[0], pixel[1], 0.0D });
/* 1032 */         ((Text)txt).setLocationOnly(new Coordinate(nloc[0], nloc[1]));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1037 */     double[] tmp = { txt.getPosition().x, txt.getPosition().y, 0.0D };
/* 1038 */     double[] loc = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/* 1040 */     double horizRatio = paintProps.getView().getExtent().getWidth() / paintProps.getCanvasBounds().width;
/* 1041 */     double vertRatio = paintProps.getView().getExtent().getHeight() / paintProps.getCanvasBounds().height;
/*      */ 
/* 1074 */     IFont font = initializeFont(txt.getFontName(), txt.getFontSize(), txt.getStyle());
/*      */ 
/* 1079 */     boolean adjustOffset = false;
/* 1080 */     if (txt.getXOffset() != 0) {
/* 1081 */       double ratio = paintProps.getView().getExtent().getWidth() / paintProps.getCanvasBounds().width;
/* 1082 */       Rectangle2D bounds = this.target.getStringBounds(font, txt.getString()[0]);
/* 1083 */       double charSize = ratio * bounds.getWidth() / txt.getString()[0].length();
/* 1084 */       loc[0] += 0.5D * charSize * txt.getXOffset();
/* 1085 */       adjustOffset = true;
/*      */     }
/*      */ 
/* 1091 */     if (txt.getYOffset() != 0) {
/* 1092 */       double ratio = paintProps.getView().getExtent().getHeight() / paintProps.getCanvasBounds().height;
/* 1093 */       Rectangle2D bounds = this.target.getStringBounds(font, txt.getString()[0]);
/* 1094 */       double charSize = ratio * bounds.getHeight();
/* 1095 */       loc[1] -= 0.5D * charSize * txt.getYOffset();
/* 1096 */       adjustOffset = true;
/*      */     }
/*      */ 
/* 1099 */     if (adjustOffset) {
/* 1100 */       double[] tmp1 = { loc[0], loc[1], 0.0D };
/* 1101 */       double[] newloc = this.iDescriptor.pixelToWorld(tmp1);
/* 1102 */       ((Text)txt).setLocationOnly(new Coordinate(newloc[0], newloc[1]));
/* 1103 */       ((Text)txt).setXOffset(0);
/* 1104 */       ((Text)txt).setYOffset(0);
/*      */     }
/*      */ 
/* 1110 */     Color clr = getDisplayColor(txt.getTextColor());
/* 1111 */     RGB textColor = new RGB(clr.getRed(), clr.getGreen(), clr.getBlue());
/*      */ 
/* 1117 */     double rotation = txt.getRotation();
/* 1118 */     if (txt.getRotationRelativity() == IText.TextRotation.NORTH_RELATIVE) {
/* 1119 */       rotation += northOffsetAngle(txt.getPosition());
/*      */     }
/*      */ 
/* 1124 */     DrawableString dstring = new DrawableString(txt.getString(), textColor);
/* 1125 */     dstring.font = font;
/* 1126 */     dstring.setCoordinates(loc[0], loc[1]);
/* 1127 */     dstring.textStyle = IGraphicsTarget.TextStyle.NORMAL;
/* 1128 */     dstring.horizontalAlignment = IGraphicsTarget.HorizontalAlignment.CENTER;
/* 1129 */     dstring.verticallAlignment = IGraphicsTarget.VerticalAlignment.MIDDLE;
/* 1130 */     dstring.rotation = rotation;
/*      */ 
/* 1132 */     Rectangle2D bounds = this.target.getStringsBounds(dstring);
/* 1133 */     double xOffset = (bounds.getWidth() + 1.0D) * horizRatio / 2.0D;
/* 1134 */     double yOffset = (bounds.getHeight() + 1.0D) * vertRatio / 2.0D;
/*      */ 
/* 1139 */     IGraphicsTarget.HorizontalAlignment align = IGraphicsTarget.HorizontalAlignment.CENTER;
/* 1140 */     double left = xOffset; double right = xOffset;
/* 1141 */     if (txt.getJustification() != null) {
/* 1142 */       switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$display$IText$TextJustification()[txt.getJustification().ordinal()]) {
/*      */       case 3:
/* 1144 */         align = IGraphicsTarget.HorizontalAlignment.RIGHT;
/* 1145 */         left = xOffset * 2.0D;
/* 1146 */         right = 0.0D;
/* 1147 */         break;
/*      */       case 2:
/* 1149 */         align = IGraphicsTarget.HorizontalAlignment.CENTER;
/* 1150 */         break;
/*      */       case 1:
/* 1152 */         align = IGraphicsTarget.HorizontalAlignment.LEFT;
/* 1153 */         left = 0.0D;
/* 1154 */         right = xOffset * 2.0D;
/* 1155 */         break;
/*      */       default:
/* 1157 */         align = IGraphicsTarget.HorizontalAlignment.CENTER;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1162 */     dstring.horizontalAlignment = align;
/*      */ 
/* 1164 */     if (dstring.rotation != 0.0D) {
/* 1165 */       AffineTransformation localAffineTransformation = AffineTransformation.rotationInstance(
/* 1166 */         dstring.rotation, dstring.basics.x, dstring.basics.y);
/*      */     }
/*      */ 
/* 1169 */     Object box = new PixelExtent(dstring.basics.x - left, dstring.basics.x + right, 
/* 1170 */       dstring.basics.y - yOffset, dstring.basics.y + yOffset);
/*      */ 
/* 1175 */     TextDisplayElement tde = new TextDisplayElement(dstring, txt.maskText().booleanValue(), 
/* 1176 */       txt.getDisplayType(), (IExtent)box);
/* 1177 */     slist.add(tde);
/*      */ 
/* 1179 */     return slist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(ITcm tcm, PaintProperties paintProps)
/*      */   {
/* 1189 */     ArrayList slist = new ArrayList();
/*      */ 
/* 1191 */     ArrayList trackPts = new ArrayList();
/*      */ 
/* 1194 */     slist.addAll(createDisplayElements(tcm.getWaveQuarters(), paintProps));
/*      */ 
/* 1197 */     for (TcmFcst tcmFcst : tcm.getTcmFcst()) {
/* 1198 */       String[] txt = new String[2];
/* 1199 */       Calendar fcstHr = (Calendar)tcm.getAdvisoryTime().clone();
/* 1200 */       fcstHr.add(11, tcmFcst.getFcstHr());
/*      */ 
/* 1202 */       if (tcmFcst.equals(tcm.getTcmFcst().get(0))) {
/* 1203 */         txt[0] = (tcm.getStormName() + "/" + tcm.getCentralPressure() + "mb");
/* 1204 */         txt[1] = String.format("%1$td/%1$tH%1$tM", new Object[] { fcstHr });
/*      */       }
/*      */       else {
/* 1207 */         txt[0] = String.format("%1$td/%1$tH%1$tM", new Object[] { fcstHr });
/* 1208 */         txt[1] = "";
/*      */       }
/*      */ 
/* 1211 */       slist.addAll(createDisplayElements(tcmFcst, paintProps, txt));
/* 1212 */       trackPts.add(tcmFcst.getQuarters()[0].getLocation());
/*      */     }
/*      */ 
/* 1216 */     if (trackPts.size() >= 2) {
/* 1217 */       Line trackLn = new Line(null, new Color[] { new Color(0, 255, 255) }, 1.5F, 0.8D, false, 
/* 1218 */         false, trackPts, 0, 
/* 1219 */         null, "Lines", "LINE_DASHED_6");
/* 1220 */       slist.addAll(createDisplayElements(trackLn, paintProps));
/*      */     }
/*      */ 
/* 1223 */     return slist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createDisplayElements(ITcmFcst tcmFcst, PaintProperties paintProps, String[] txt)
/*      */   {
/* 1233 */     ArrayList slist = new ArrayList();
/* 1234 */     for (ITcmWindQuarter qua : tcmFcst.getQuarters()) {
/* 1235 */       slist.addAll(createDisplayElements(qua, paintProps));
/*      */     }
/*      */ 
/* 1238 */     Symbol ts = new Symbol(null, new Color[] { new Color(0, 255, 255) }, 2.5F, 1.5D, Boolean.valueOf(false), 
/* 1239 */       tcmFcst.getQuarters()[0].getLocation(), 
/* 1240 */       "Symbol", getTcmFcstSymbolType(tcmFcst));
/* 1241 */     slist.addAll(createDisplayElements(ts, paintProps));
/*      */ 
/* 1243 */     if (txt != null) {
/* 1244 */       Text label = new Text(null, "Courier", 14.0F, IText.TextJustification.LEFT_JUSTIFY, 
/* 1245 */         tcmFcst.getQuarters()[0].getLocation(), 0.0D, IText.TextRotation.NORTH_RELATIVE, txt, 
/* 1246 */         IText.FontStyle.REGULAR, getDisplayColor(Color.YELLOW), 4, 0, false, IText.DisplayType.NORMAL, 
/* 1247 */         "Text", "General Text");
/*      */ 
/* 1249 */       slist.addAll(createDisplayElements(label, paintProps));
/*      */     }
/*      */ 
/* 1252 */     return slist;
/*      */   }
/*      */ 
/*      */   private String getTcmFcstSymbolType(ITcmFcst tcmFcst)
/*      */   {
/* 1265 */     String ret = "TROPICAL_STORM_NH";
/* 1266 */     ITcmWindQuarter[] quarters = tcmFcst.getQuarters();
/*      */ 
/* 1268 */     int maxWind = 0;
/*      */ 
/* 1270 */     for (ITcmWindQuarter qtr : quarters) {
/* 1271 */       double[] radius = qtr.getQuarters();
/* 1272 */       for (double r : radius) {
/* 1273 */         if (r > 0.0D) {
/* 1274 */           if (qtr.getWindSpeed() <= maxWind) break; maxWind = qtr.getWindSpeed();
/* 1275 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1280 */     double lat = quarters[0].getLocation().y;
/* 1281 */     if (maxWind >= 64) {
/* 1282 */       if (lat > 0.0D) ret = "HURRICANE_NH"; else
/* 1283 */         ret = "HURRICANE_SH";
/*      */     }
/* 1285 */     else if (maxWind >= 50) {
/* 1286 */       if (lat > 0.0D) ret = "TROPICAL_STORM_NH"; else
/* 1287 */         ret = "TROPICAL_STORM_SH";
/*      */     }
/* 1289 */     else if (maxWind >= 32) {
/* 1290 */       ret = "TROPICAL_DEPRESSION";
/*      */     }
/*      */     else {
/* 1293 */       ret = "LOW_X_FILLED";
/*      */     }
/*      */ 
/* 1296 */     return ret;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createDisplayElements(ITcmWindQuarter quatros, PaintProperties paintProps)
/*      */   {
/* 1307 */     ArrayList slist = new ArrayList();
/*      */ 
/* 1309 */     Color color = Color.GREEN;
/* 1310 */     switch (quatros.getWindSpeed()) {
/*      */     case 0:
/* 1312 */       color = Color.GREEN;
/* 1313 */       break;
/*      */     case 32:
/* 1315 */       color = Color.BLUE;
/* 1316 */       break;
/*      */     case 50:
/* 1318 */       color = Color.YELLOW;
/* 1319 */       break;
/*      */     case 64:
/* 1321 */       color = Color.RED;
/*      */     }
/*      */ 
/* 1325 */     Coordinate center = quatros.getLocation();
/* 1326 */     Arc quatro1 = new Arc(null, color, 
/* 1327 */       1.5F, 1.0D, false, false, 
/* 1328 */       0, null, "Circle", 
/* 1329 */       center, calculateDestinationPointMap(center, 0.0D, quatros.getQuarters()[0]), "Arc", 
/* 1330 */       1.0D, 0.0D, 90.0D);
/* 1331 */     Arc quatro2 = new Arc(null, color, 
/* 1332 */       1.5F, 1.0D, false, false, 
/* 1333 */       0, null, "Circle", 
/* 1334 */       center, calculateDestinationPointMap(center, 0.0D, quatros.getQuarters()[1]), "Arc", 
/* 1335 */       1.0D, 90.0D, 180.0D);
/* 1336 */     Arc quatro3 = new Arc(null, color, 
/* 1337 */       1.5F, 1.0D, false, false, 
/* 1338 */       0, null, "Circle", 
/* 1339 */       center, calculateDestinationPointMap(center, 0.0D, quatros.getQuarters()[2]), "Arc", 
/* 1340 */       1.0D, 180.0D, 270.0D);
/* 1341 */     Arc quatro4 = new Arc(null, color, 
/* 1342 */       1.5F, 1.0D, false, false, 
/* 1343 */       0, null, "Circle", 
/* 1344 */       center, calculateDestinationPointMap(center, 0.0D, quatros.getQuarters()[3]), "Arc", 
/* 1345 */       1.0D, 270.0D, 360.0D);
/* 1346 */     slist.addAll(createDisplayElements(quatro1, paintProps));
/* 1347 */     slist.addAll(createDisplayElements(quatro2, paintProps));
/* 1348 */     slist.addAll(createDisplayElements(quatro3, paintProps));
/* 1349 */     slist.addAll(createDisplayElements(quatro4, paintProps));
/*      */ 
/* 1351 */     Line ln1 = getWindQuatroLine(getPointOnArc(quatro1, 0.0D), 
/* 1352 */       getPointOnArc(quatro2, 0.0D), color);
/*      */ 
/* 1354 */     Line ln2 = getWindQuatroLine(getPointOnArc(quatro2, 90.0D), 
/* 1355 */       getPointOnArc(quatro3, 90.0D), color);
/*      */ 
/* 1357 */     Line ln3 = getWindQuatroLine(getPointOnArc(quatro3, 180.0D), 
/* 1358 */       getPointOnArc(quatro4, 180.0D), color);
/*      */ 
/* 1360 */     Line ln4 = getWindQuatroLine(getPointOnArc(quatro4, 270.0D), 
/* 1361 */       getPointOnArc(quatro1, 270.0D), color);
/*      */ 
/* 1363 */     slist.addAll(createDisplayElements(ln1, paintProps));
/* 1364 */     slist.addAll(createDisplayElements(ln2, paintProps));
/* 1365 */     slist.addAll(createDisplayElements(ln3, paintProps));
/* 1366 */     slist.addAll(createDisplayElements(ln4, paintProps));
/*      */ 
/* 1368 */     return slist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(IArc arc, PaintProperties paintProps)
/*      */   {
/* 1379 */     setScales(paintProps);
/*      */ 
/* 1384 */     ArrayList slist = new ArrayList();
/* 1385 */     IWireframeShape arcpts = this.target.createWireframeShape(false, this.iDescriptor);
/*      */ 
/* 1390 */     double[] tmp = { arc.getCenterPoint().x, arc.getCenterPoint().y, 0.0D };
/* 1391 */     double[] center = this.iDescriptor.worldToPixel(tmp);
/* 1392 */     double[] tmp2 = { arc.getCircumferencePoint().x, arc.getCircumferencePoint().y, 0.0D };
/* 1393 */     double[] circum = this.iDescriptor.worldToPixel(tmp2);
/*      */ 
/* 1398 */     double axisAngle = Math.toDegrees(Math.atan2(circum[1] - center[1], circum[0] - center[0]));
/* 1399 */     double cosineAxis = Math.cos(Math.toRadians(axisAngle));
/* 1400 */     double sineAxis = Math.sin(Math.toRadians(axisAngle));
/*      */ 
/* 1405 */     double[] diff = { circum[0] - center[0], circum[1] - center[1] };
/* 1406 */     double major = Math.sqrt(diff[0] * diff[0] + diff[1] * diff[1]);
/* 1407 */     double minor = major * arc.getAxisRatio();
/*      */ 
/* 1414 */     double angle = arc.getStartAngle();
/* 1415 */     int numpts = (int)Math.round(arc.getEndAngle() - arc.getStartAngle() + 1.0D);
/* 1416 */     double[][] path = new double[numpts][3];
/* 1417 */     for (int j = 0; j < numpts; j++) {
/* 1418 */       double thisSine = Math.sin(Math.toRadians(angle));
/* 1419 */       double thisCosine = Math.cos(Math.toRadians(angle));
/*      */ 
/* 1427 */       path[j][0] = 
/* 1428 */         (center[0] + major * cosineAxis * thisCosine - 
/* 1428 */         minor * sineAxis * thisSine);
/* 1429 */       path[j][1] = 
/* 1430 */         (center[1] + major * sineAxis * thisCosine + 
/* 1430 */         minor * cosineAxis * thisSine);
/*      */ 
/* 1433 */       angle += 1.0D;
/*      */     }
/* 1435 */     arcpts.addLineSegment(path);
/*      */ 
/* 1440 */     arcpts.compile();
/* 1441 */     slist.add(new LineDisplayElement(arcpts, getDisplayColor(arc.getColors()[0]), arc.getLineWidth()));
/*      */ 
/* 1443 */     slist.addAll(adjustContourCircleLabel(arc, paintProps, path));
/*      */ 
/* 1445 */     return slist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(ITrack track, PaintProperties paintProps)
/*      */   {
/* 1458 */     ArrayList points = new ArrayList();
/* 1459 */     setScales(paintProps);
/*      */ 
/* 1461 */     SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
/*      */ 
/* 1466 */     ArrayList slist = new ArrayList();
/*      */ 
/* 1471 */     Color iniDspClr = getDisplayColor(track.getInitialColor());
/* 1472 */     Color expDspClr = getDisplayColor(track.getExtrapColor());
/*      */ 
/* 1478 */     points.clear();
/* 1479 */     for (TrackPoint pt : track.getInitialPoints())
/*      */     {
/* 1481 */       points.add(pt.getLocation());
/*      */     }
/* 1483 */     Line iline = new Line(null, new Color[] { iniDspClr }, track.getLineWidth(), 1.0D, 
/* 1484 */       false, false, points, 0, FillPatternList.FillPattern.SOLID, "Lines", track.getInitialLinePattern());
/* 1485 */     ArrayList temps = createDisplayElements(iline, paintProps);
/* 1486 */     slist.addAll(temps);
/*      */ 
/* 1491 */     int n = 0;
/* 1492 */     Coordinate[] initialPoints = new Coordinate[track.getInitialPoints().length];
/*      */     Text txt;
/* 1493 */     for (TrackPoint pt : track.getInitialPoints())
/*      */     {
/* 1495 */       initialPoints[(n++)] = new Coordinate(pt.getLocation());
/*      */ 
/* 1499 */       if (pt.getTime() != null) {
/* 1500 */         dtime = sdf.format(pt.getTime().getTime());
/* 1501 */         txt = new Text(null, track.getFontName(), track.getFontSize(), IText.TextJustification.LEFT_JUSTIFY, 
/* 1502 */           pt.getLocation(), 0.0D, IText.TextRotation.SCREEN_RELATIVE, new String[] { dtime }, 
/* 1503 */           IText.FontStyle.BOLD, iniDspClr, 0, 3, false, IText.DisplayType.NORMAL, "Text", "General Text");
/* 1504 */         temps = createDisplayElements(txt, paintProps);
/* 1505 */         slist.addAll(temps);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1511 */     SymbolLocationSet imarkers = new SymbolLocationSet(null, new Color[] { iniDspClr }, 
/* 1512 */       track.getLineWidth(), 1.0D, 
/* 1513 */       Boolean.valueOf(false), initialPoints, "SymbolSet", track.getInitialMarker());
/* 1514 */     temps = createDisplayElements(imarkers, paintProps);
/* 1515 */     slist.addAll(temps);
/*      */ 
/* 1521 */     points.clear();
/*      */ 
/* 1526 */     int lastInitialPointIndex = track.getInitialPoints().length - 1;
/* 1527 */     points.add(track.getInitialPoints()[lastInitialPointIndex].getLocation());
/*      */ 
/* 1529 */     String dtime = (txt = track.getExtrapPoints()).length; for (String str1 = 0; str1 < dtime; str1++) { TrackPoint pt = txt[str1];
/* 1530 */       points.add(pt.getLocation());
/*      */     }
/* 1532 */     Line eline = new Line(null, new Color[] { expDspClr }, track.getLineWidth(), 1.0D, 
/* 1533 */       false, false, points, 0, FillPatternList.FillPattern.SOLID, "Lines", track.getExtrapLinePattern());
/* 1534 */     temps = createDisplayElements(eline, paintProps);
/* 1535 */     slist.addAll(temps);
/*      */ 
/* 1540 */     int m = 0;
/* 1541 */     boolean[] extrapPointTimeTextDisplayIndicator = track.getExtraPointTimeTextDisplayIndicator();
/* 1542 */     Coordinate[] extrapPoints = new Coordinate[track.getExtrapPoints().length];
/* 1543 */     for (TrackPoint pt : track.getExtrapPoints())
/*      */     {
/* 1546 */       extrapPoints[m] = new Coordinate(pt.getLocation());
/*      */ 
/* 1550 */       if ((pt.getTime() != null) && (extrapPointTimeTextDisplayIndicator[m] != 0)) {
/* 1551 */         String dtime = sdf.format(pt.getTime().getTime());
/* 1552 */         Text txt = new Text(null, track.getFontName(), track.getFontSize(), IText.TextJustification.LEFT_JUSTIFY, 
/* 1553 */           pt.getLocation(), 0.0D, IText.TextRotation.SCREEN_RELATIVE, new String[] { dtime }, 
/* 1554 */           IText.FontStyle.BOLD, expDspClr, 0, 3, false, IText.DisplayType.NORMAL, "Text", "General Text");
/* 1555 */         temps = createDisplayElements(txt, paintProps);
/* 1556 */         slist.addAll(temps);
/*      */       }
/* 1558 */       m++;
/*      */     }
/*      */ 
/* 1563 */     SymbolLocationSet emarkers = new SymbolLocationSet(null, new Color[] { expDspClr }, 
/* 1564 */       track.getLineWidth(), 1.0D, 
/* 1565 */       Boolean.valueOf(false), extrapPoints, "SymbolSet", track.getExtrapMarker());
/* 1566 */     temps = createDisplayElements(emarkers, paintProps);
/* 1567 */     slist.addAll(temps);
/*      */ 
/* 1570 */     return slist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(ISymbol de, PaintProperties paintProps)
/*      */   {
/* 1582 */     if ((de instanceof Symbol)) {
/* 1583 */       Coordinate[] loc = { de.getLocation() };
/* 1584 */       SymbolLocationSet sym = new SymbolLocationSet((Symbol)de, loc);
/* 1585 */       return createDisplayElements(sym, paintProps);
/*      */     }
/*      */ 
/* 1588 */     return new ArrayList();
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(ISymbolSet symbolSet, PaintProperties paintProps)
/*      */   {
/* 1604 */     setScales(paintProps);
/* 1605 */     double sfactor = this.deviceScale * this.symbolScale * symbolSet.getSymbol().getSizeScale();
/*      */ 
/* 1610 */     ArrayList slist = new ArrayList();
/*      */ 
/* 1613 */     Symbol sym = symbolSet.getSymbol();
/*      */ 
/* 1618 */     Color[] dspClr = getDisplayColors(sym.getColors());
/*      */ 
/* 1623 */     sfactor *= this.screenToWorldRatio;
/*      */ 
/* 1626 */     IRenderedImageCallback imageCb = new SymbolImageCallback(sym.getPatternName(), sfactor, 
/* 1627 */       sym.getLineWidth(), sym.isClear().booleanValue(), dspClr[0]);
/*      */ 
/* 1631 */     IImage pic = null;
/*      */     try {
/* 1633 */       pic = this.target.initializeRaster(imageCb);
/* 1634 */       pic.stage();
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1638 */       System.out.println("SAG:IMAGE CREATION");
/* 1639 */       e.printStackTrace();
/* 1640 */       return slist;
/*      */     }
/*      */ 
/* 1646 */     double[][] pts = PgenUtil.latlonToPixel(symbolSet.getLocations(), (IMapDescriptor)this.iDescriptor);
/*      */ 
/* 1651 */     slist.add(new SymbolSetElement(pic, pts));
/* 1652 */     return slist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(ICombo de, PaintProperties paintProps)
/*      */   {
/* 1665 */     setScales(paintProps);
/* 1666 */     double scale = this.deviceScale * this.symbolScale * de.getSizeScale() * 12.0D;
/*      */ 
/* 1668 */     if ((de instanceof ComboSymbol)) {
/* 1669 */       String[] patterns = de.getPatternNames();
/*      */ 
/* 1674 */       Coordinate[] loc = { de.getLocation() };
/* 1675 */       double[] worldPixel = { loc[0].x, loc[0].y, 0.0D };
/* 1676 */       double[] pixel = this.iDescriptor.worldToPixel(worldPixel);
/*      */ 
/* 1681 */       Color[] dspClr = getDisplayColors(de.getColors());
/*      */ 
/* 1687 */       double[] locUL = this.iDescriptor.pixelToWorld(new double[] { pixel[0] - 0.5D * scale, 
/* 1688 */         pixel[1] - 0.25D * scale, 0.0D });
/* 1689 */       Coordinate[] loc1 = { new Coordinate(locUL[0], locUL[1]) };
/*      */ 
/* 1694 */       SymbolLocationSet sym = new SymbolLocationSet(null, dspClr, de.getLineWidth(), 
/* 1695 */         de.getSizeScale(), de.isClear(), loc1, "Symbol", patterns[0]);
/*      */ 
/* 1701 */       double[] locLR = this.iDescriptor.pixelToWorld(new double[] { pixel[0] + 0.5D * scale, 
/* 1702 */         pixel[1] + 0.25D * scale, 0.0D });
/* 1703 */       Coordinate[] loc2 = { new Coordinate(locLR[0], locLR[1]) };
/*      */ 
/* 1708 */       SymbolLocationSet sym2 = new SymbolLocationSet(null, dspClr, de.getLineWidth(), 
/* 1709 */         de.getSizeScale(), de.isClear(), loc2, "Symbol", patterns[1]);
/*      */ 
/* 1712 */       SymbolLocationSet sym3 = new SymbolLocationSet(null, dspClr, de.getLineWidth(), 
/* 1713 */         de.getSizeScale(), de.isClear(), loc, "Symbol", "SLASH");
/*      */ 
/* 1718 */       ArrayList stuff = createDisplayElements(sym, paintProps);
/* 1719 */       stuff.addAll(createDisplayElements(sym2, paintProps));
/* 1720 */       stuff.addAll(createDisplayElements(sym3, paintProps));
/* 1721 */       return stuff;
/*      */     }
/*      */ 
/* 1724 */     return new ArrayList();
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(ITca tca, PaintProperties paintProps)
/*      */   {
/* 1737 */     ArrayList rlist = new ArrayList();
/*      */ 
/* 1739 */     List advisories = tca.getAdvisories();
/*      */ 
/* 1745 */     if (advisories.isEmpty()) {
/* 1746 */       String[] noneMsg = new String[3];
/* 1747 */       noneMsg[0] = new String(tca.getStormType() + " " + tca.getStormName());
/* 1748 */       noneMsg[1] = new String("Advisory " + tca.getAdvisoryNumber());
/* 1749 */       noneMsg[2] = new String("No current Watches/Warnings");
/*      */ 
/* 1751 */       Text display = new Text(null, "Courier", 14.0F, IText.TextJustification.CENTER, 
/* 1752 */         tca.getTextLocation(), 0.0D, IText.TextRotation.SCREEN_RELATIVE, noneMsg, 
/* 1753 */         IText.FontStyle.REGULAR, getDisplayColor(Color.YELLOW), 0, 0, false, IText.DisplayType.BOX, 
/* 1754 */         "Text", "General Text");
/*      */ 
/* 1756 */       rlist = createDisplayElements(display, paintProps);
/* 1757 */       return rlist;
/*      */     }
/*      */ 
/* 1763 */     rlist.addAll(createAdvisoryDisplay(advisories, "Tropical Storm", "Watch", Color.YELLOW, 7.0F, paintProps));
/* 1764 */     rlist.addAll(createAdvisoryDisplay(advisories, "Hurricane", "Watch", Color.PINK, 13.0F, paintProps));
/* 1765 */     rlist.addAll(createAdvisoryDisplay(advisories, "Tropical Storm", "Warning", Color.BLUE, 7.0F, paintProps));
/* 1766 */     rlist.addAll(createAdvisoryDisplay(advisories, "Hurricane", "Warning", Color.RED, 13.0F, paintProps));
/*      */ 
/* 1768 */     return rlist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createAdvisoryDisplay(List<TropicalCycloneAdvisory> advisories, String severe, String type, Color clr, float lw, PaintProperties paintProps)
/*      */   {
/* 1785 */     ArrayList alist = new ArrayList();
/*      */     Iterator localIterator2;
/* 1791 */     label231: for (Iterator localIterator1 = advisories.iterator(); localIterator1.hasNext(); 
/* 1800 */       localIterator2.hasNext())
/*      */     {
/* 1791 */       TropicalCycloneAdvisory tca = (TropicalCycloneAdvisory)localIterator1.next();
/* 1792 */       if ((!tca.getSeverity().equals(severe)) || 
/* 1793 */         (!tca.getAdvisoryType().equals(type)))
/*      */         break label231;
/* 1795 */       BPGeography segment = tca.getSegment();
/*      */ 
/* 1800 */       localIterator2 = segment.getPaths().iterator(); continue; Coordinate[] coords = (Coordinate[])localIterator2.next();
/*      */ 
/* 1803 */       ArrayList pts = new ArrayList();
/* 1804 */       for (Coordinate c : coords) pts.add(c);
/*      */ 
/* 1808 */       boolean fill = false;
/* 1809 */       if ((coords[0].equals2D(coords[(coords.length - 1)])) && 
/* 1810 */         ((segment instanceof WaterBreakpoint))) fill = true;
/*      */ 
/* 1813 */       Line seg = new Line(null, new Color[] { clr }, lw, 1.0D, 
/* 1814 */         false, fill, pts, 0, FillPatternList.FillPattern.SOLID, "Lines", "LINE_SOLID");
/* 1815 */       alist.addAll(createDisplayElements(seg, paintProps));
/*      */     }
/*      */ 
/* 1823 */     return alist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(IAvnText avntxt, PaintProperties paintProps)
/*      */   {
/* 1834 */     ArrayList rlist = new ArrayList();
/*      */ 
/* 1836 */     setScales(paintProps);
/*      */ 
/* 1838 */     String[] text = createTextString(avntxt);
/* 1839 */     boolean bgmask = hasTextBackgroundMask(avntxt.getAvnTextType());
/* 1840 */     IText.DisplayType outline = hasRegularOutline(avntxt.getAvnTextType());
/*      */ 
/* 1842 */     Text display = new Text(null, avntxt.getFontName(), avntxt.getFontSize(), avntxt.getJustification(), 
/* 1843 */       avntxt.getPosition(), 0.0D, IText.TextRotation.SCREEN_RELATIVE, text, 
/* 1844 */       avntxt.getStyle(), getDisplayColor(avntxt.getTextColor()), 0, 0, bgmask, outline, 
/* 1845 */       "Text", "General Text");
/*      */ 
/* 1847 */     ArrayList txtdable = createDisplayElements(display, paintProps);
/*      */ 
/* 1849 */     if ((avntxt.getAvnTextType() == IAvnText.AviationTextType.HIGH_LEVEL_TURBULENCE) || 
/* 1850 */       (avntxt.getAvnTextType() == IAvnText.AviationTextType.CLOUD_LEVEL) || 
/* 1851 */       (avntxt.getAvnTextType() == IAvnText.AviationTextType.MID_LEVEL_ICING)) {
/* 1852 */       rlist.addAll(txtdable);
/* 1853 */       rlist.add(createLineSeparator(avntxt, paintProps));
/*      */     }
/* 1855 */     else if ((avntxt.getAvnTextType() == IAvnText.AviationTextType.LOW_PRESSURE_BOX) || 
/* 1856 */       (avntxt.getAvnTextType() == IAvnText.AviationTextType.HIGH_PRESSURE_BOX)) {
/* 1857 */       rlist.addAll(createPressureBox(avntxt, paintProps));
/* 1858 */       rlist.addAll(txtdable);
/*      */     }
/*      */     else {
/* 1861 */       rlist.addAll(txtdable);
/*      */     }
/*      */ 
/* 1864 */     if ((avntxt.getAvnTextType() == IAvnText.AviationTextType.LOW_LEVEL_TURBULENCE) || 
/* 1865 */       (avntxt.getAvnTextType() == IAvnText.AviationTextType.HIGH_LEVEL_TURBULENCE) || 
/* 1866 */       (avntxt.getAvnTextType() == IAvnText.AviationTextType.MID_LEVEL_ICING)) {
/* 1867 */       rlist.addAll(createAvnTextSymbol(avntxt, paintProps));
/*      */     }
/*      */ 
/* 1870 */     return rlist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createAvnTextSymbol(IAvnText avntxt, PaintProperties paintProps)
/*      */   {
/* 1879 */     ArrayList rlist = new ArrayList();
/*      */ 
/* 1884 */     IFont font = initializeFont(avntxt.getFontName(), avntxt.getFontSize(), avntxt.getStyle());
/* 1885 */     Rectangle2D bounds = this.target.getStringBounds(font, "XyX");
/* 1886 */     font.dispose();
/* 1887 */     double vertRatio = paintProps.getView().getExtent().getHeight() / paintProps.getCanvasBounds().height;
/* 1888 */     double horizRatio = paintProps.getView().getExtent().getWidth() / paintProps.getCanvasBounds().width;
/*      */ 
/* 1893 */     Color[] clrs = { getDisplayColor(avntxt.getTextColor()) };
/* 1894 */     double symSize = bounds.getHeight() / 12.0D;
/*      */ 
/* 1899 */     Coordinate[] loc = { avntxt.getPosition() };
/* 1900 */     double[] worldPixel = { loc[0].x, loc[0].y, 0.0D };
/* 1901 */     double[] pixel = this.iDescriptor.worldToPixel(worldPixel);
/*      */ 
/* 1907 */     pixel[1] -= 1.5D * vertRatio * bounds.getHeight();
/* 1908 */     double shift = 0.5D;
/* 1909 */     if (avntxt.getAvnTextType() == IAvnText.AviationTextType.LOW_LEVEL_TURBULENCE) shift = 1.1667D;
/*      */ 
/* 1911 */     if (avntxt.getJustification() == IText.TextJustification.LEFT_JUSTIFY) {
/* 1912 */       pixel[0] += bounds.getWidth() * horizRatio * shift;
/*      */     }
/* 1914 */     else if (avntxt.getJustification() == IText.TextJustification.RIGHT_JUSTIFY) {
/* 1915 */       pixel[0] -= bounds.getWidth() * horizRatio * shift;
/*      */     }
/*      */ 
/* 1918 */     String[] ids = avntxt.getSymbolPatternName().split("\\|");
/*      */ 
/* 1923 */     if (ids.length == 1)
/*      */     {
/* 1925 */       double[] locSym = this.iDescriptor.pixelToWorld(new double[] { pixel[0], pixel[1], 0.0D });
/* 1926 */       Coordinate loc1 = new Coordinate(locSym[0], locSym[1]);
/*      */ 
/* 1931 */       Symbol sym = new Symbol(null, clrs, 1.0F, symSize, Boolean.valueOf(false), loc1, 
/* 1932 */         "Symbol", ids[0]);
/* 1933 */       rlist.addAll(createDisplayElements(sym, paintProps));
/*      */     }
/* 1935 */     else if (ids.length > 1)
/*      */     {
/* 1937 */       double newX = pixel[0] - 0.3333D * bounds.getWidth() * horizRatio;
/*      */ 
/* 1939 */       double[] locSym = this.iDescriptor.pixelToWorld(new double[] { newX, pixel[1], 0.0D });
/* 1940 */       Coordinate loc1 = new Coordinate(locSym[0], locSym[1]);
/*      */ 
/* 1945 */       Symbol sym = new Symbol(null, clrs, 1.0F, symSize, Boolean.valueOf(false), loc1, 
/* 1946 */         "Symbol", ids[0]);
/* 1947 */       rlist.addAll(createDisplayElements(sym, paintProps));
/*      */ 
/* 1949 */       newX = pixel[0] + 0.3333D * bounds.getWidth() * horizRatio;
/*      */ 
/* 1951 */       locSym = this.iDescriptor.pixelToWorld(new double[] { newX, pixel[1], 0.0D });
/* 1952 */       Coordinate loc2 = new Coordinate(locSym[0], locSym[1]);
/*      */ 
/* 1957 */       Symbol sym2 = new Symbol(null, clrs, 1.0F, symSize, Boolean.valueOf(false), loc2, 
/* 1958 */         "Symbol", ids[1]);
/* 1959 */       rlist.addAll(createDisplayElements(sym2, paintProps));
/*      */     }
/*      */ 
/* 1963 */     return rlist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createPressureBox(IAvnText avntxt, PaintProperties paintProps)
/*      */   {
/* 1973 */     ArrayList dlist = new ArrayList();
/*      */ 
/* 1978 */     IFont font = initializeFont(avntxt.getFontName(), avntxt.getFontSize(), avntxt.getStyle());
/* 1979 */     Rectangle2D bounds = this.target.getStringBounds(font, "XyX");
/* 1980 */     font.dispose();
/* 1981 */     double horizRatio = paintProps.getView().getExtent().getWidth() / paintProps.getCanvasBounds().width;
/* 1982 */     double vertRatio = paintProps.getView().getExtent().getHeight() / paintProps.getCanvasBounds().height;
/*      */ 
/* 1987 */     double[] tmp = { avntxt.getPosition().x, avntxt.getPosition().y, 0.0D };
/* 1988 */     double[] center = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/* 1990 */     if (avntxt.getJustification() == IText.TextJustification.LEFT_JUSTIFY) {
/* 1991 */       center[0] += bounds.getWidth() * horizRatio * 0.5D;
/*      */     }
/* 1993 */     else if (avntxt.getJustification() == IText.TextJustification.RIGHT_JUSTIFY) {
/* 1994 */       center[0] -= bounds.getWidth() * horizRatio * 0.5D;
/*      */     }
/* 1996 */     double xoffset = 0.667D * horizRatio * bounds.getWidth();
/* 1997 */     double yoffset = vertRatio * bounds.getHeight();
/* 1998 */     double extra = 2.0D * vertRatio;
/*      */ 
/* 2003 */     double[][] box = null;
/* 2004 */     if (avntxt.getAvnTextType() == IAvnText.AviationTextType.LOW_PRESSURE_BOX) {
/* 2005 */       double[] ul = { center[0] - xoffset, center[1] - yoffset - extra, 0.0D };
/* 2006 */       double[] ur = { center[0] + xoffset, center[1] - yoffset - extra, 0.0D };
/* 2007 */       double[] lr = { center[0] + xoffset, center[1] + yoffset, 0.0D };
/* 2008 */       double[] ll = { center[0] - xoffset, center[1] + yoffset, 0.0D };
/* 2009 */       double[] bottom = { center[0], center[1] + 2.0D * yoffset + 2.0D * extra, 0.0D };
/*      */ 
/* 2011 */       box = new double[][] { bottom, lr, ur, ul, ll, bottom };
/*      */     }
/* 2013 */     else if (avntxt.getAvnTextType() == IAvnText.AviationTextType.HIGH_PRESSURE_BOX) {
/* 2014 */       double[] ul = { center[0] - xoffset, center[1] - yoffset + extra, 0.0D };
/* 2015 */       double[] ur = { center[0] + xoffset, center[1] - yoffset + extra, 0.0D };
/* 2016 */       double[] lr = { center[0] + xoffset, center[1] + yoffset + 3.0D * extra, 0.0D };
/* 2017 */       double[] ll = { center[0] - xoffset, center[1] + yoffset + 3.0D * extra, 0.0D };
/* 2018 */       double[] top = { center[0], center[1] - 2.0D * yoffset - extra, 0.0D };
/*      */ 
/* 2020 */       box = new double[][] { top, ur, lr, ll, ul, top };
/*      */     }
/*      */ 
/* 2026 */     if (box != null) {
/* 2027 */       RGB bg = this.backgroundColor.getColor(IBackgroundColorChangedListener.BGColorMode.EDITOR);
/* 2028 */       IShadedShape fill = this.target.createShadedShape(false, this.iDescriptor, false);
/* 2029 */       fill.addPolygonPixelSpace(toLineString(box), bg);
/* 2030 */       fill.compile();
/* 2031 */       dlist.add(new FillDisplayElement(fill, 1.0F));
/*      */ 
/* 2033 */       IWireframeShape outline = this.target.createWireframeShape(false, this.iDescriptor);
/* 2034 */       outline.addLineSegment(box);
/* 2035 */       outline.compile();
/* 2036 */       dlist.add(new LineDisplayElement(outline, getDisplayColor(avntxt.getTextColor()), 1.0F));
/*      */     }
/*      */ 
/* 2039 */     return dlist;
/*      */   }
/*      */ 
/*      */   private IDisplayable createLineSeparator(IAvnText avntxt, PaintProperties paintProps)
/*      */   {
/* 2051 */     IFont font = initializeFont(avntxt.getFontName(), avntxt.getFontSize(), avntxt.getStyle());
/* 2052 */     Rectangle2D bounds = this.target.getStringBounds(font, "XyX");
/* 2053 */     font.dispose();
/* 2054 */     double horizRatio = paintProps.getView().getExtent().getWidth() / paintProps.getCanvasBounds().width;
/* 2055 */     double vertRatio = paintProps.getView().getExtent().getHeight() / paintProps.getCanvasBounds().height;
/*      */ 
/* 2060 */     double[] tmp = { avntxt.getPosition().x, avntxt.getPosition().y, 0.0D };
/* 2061 */     double[] center = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/* 2063 */     if (avntxt.getJustification() == IText.TextJustification.LEFT_JUSTIFY) {
/* 2064 */       center[0] += bounds.getWidth() * horizRatio * 0.5D;
/*      */     }
/* 2066 */     else if (avntxt.getJustification() == IText.TextJustification.RIGHT_JUSTIFY) {
/* 2067 */       center[0] -= bounds.getWidth() * horizRatio * 0.5D;
/*      */     }
/* 2069 */     double xoffset = 0.667D * horizRatio * bounds.getWidth();
/* 2070 */     double yoffset = 2.0D * vertRatio;
/*      */ 
/* 2075 */     double[] left = { center[0] - xoffset, center[1] + yoffset, 0.0D };
/* 2076 */     double[] right = { center[0] + xoffset, center[1] + yoffset, 0.0D };
/* 2077 */     double[][] seg = { left, right };
/* 2078 */     IWireframeShape line = this.target.createWireframeShape(false, this.iDescriptor);
/* 2079 */     line.addLineSegment(seg);
/* 2080 */     line.compile();
/* 2081 */     return new LineDisplayElement(line, getDisplayColor(avntxt.getTextColor()), 1.0F);
/*      */   }
/*      */ 
/*      */   private IText.DisplayType hasRegularOutline(IAvnText.AviationTextType avnTextType)
/*      */   {
/* 2090 */     switch (avnTextType) {
/*      */     case FREEZING_LEVEL:
/*      */     case LOW_PRESSURE_BOX:
/* 2093 */       return IText.DisplayType.BOX;
/*      */     case HIGH_LEVEL_TURBULENCE:
/*      */     case HIGH_PRESSURE_BOX:
/* 2096 */     case LOW_LEVEL_TURBULENCE: } return IText.DisplayType.NORMAL;
/*      */   }
/*      */ 
/*      */   private boolean hasTextBackgroundMask(IAvnText.AviationTextType avnTextType)
/*      */   {
/* 2106 */     switch (avnTextType) {
/*      */     case FREEZING_LEVEL:
/*      */     case HIGH_PRESSURE_BOX:
/*      */     case LOW_LEVEL_TURBULENCE:
/*      */     case MID_LEVEL_ICING:
/* 2111 */       return true;
/*      */     case HIGH_LEVEL_TURBULENCE:
/*      */     case LOW_PRESSURE_BOX:
/* 2114 */     }return false;
/*      */   }
/*      */ 
/*      */   private String[] createTextString(IAvnText avntxt)
/*      */   {
/* 2126 */     String defaultString = "XXX";
/* 2127 */     String top = addLeadingZero(avntxt.getTopValue());
/* 2128 */     if (top == null) top = defaultString;
/*      */ 
/* 2130 */     String bottom = null;
/* 2131 */     if (avntxt.hasBottomValue()) {
/* 2132 */       bottom = addLeadingZero(avntxt.getBottomValue());
/*      */     }
/* 2134 */     if (bottom == null) bottom = defaultString;
/*      */     String[] ret;
/*      */     String[] ret;
/*      */     String[] ret;
/*      */     String[] ret;
/*      */     String[] ret;
/*      */     String[] ret;
/*      */     String[] ret;
/* 2136 */     switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$display$IAvnText$AviationTextType()[avntxt.getAvnTextType().ordinal()])
/*      */     {
/*      */     case 1:
/* 2139 */       ret = new String[] { top, "L" };
/* 2140 */       break;
/*      */     case 2:
/* 2143 */       ret = new String[] { "H", top };
/* 2144 */       break;
/*      */     case 3:
/* 2147 */       ret = new String[] { top };
/* 2148 */       break;
/*      */     case 7:
/* 2151 */       StringBuilder st = new StringBuilder("0");
/* 2152 */       st.append('°');
/* 2153 */       st.append(": ");
/* 2154 */       st.append(top);
/* 2155 */       ret = new String[] { st.toString() };
/* 2156 */       break;
/*      */     case 4:
/* 2159 */       StringBuilder str = new StringBuilder(top);
/* 2160 */       str.append('/');
/* 2161 */       str.append(bottom);
/* 2162 */       ret = new String[] { str.toString() };
/* 2163 */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 8:
/* 2168 */       ret = new String[] { top, bottom };
/* 2169 */       break;
/*      */     default:
/* 2172 */       ret = new String[] { defaultString };
/*      */     }
/*      */ 
/* 2176 */     return ret;
/*      */   }
/*      */ 
/*      */   private String addLeadingZero(String value)
/*      */   {
/* 2183 */     if (value.length() == 1) return new String("00" + value);
/* 2184 */     if (value.length() == 2) return new String("0" + value);
/* 2185 */     return value;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(IMidCloudText midtxt, PaintProperties paintProps)
/*      */   {
/* 2196 */     if (midtxt.isTwoColumns()) {
/* 2197 */       return createMidCloudText_TwoColumns(midtxt, paintProps);
/*      */     }
/*      */ 
/* 2200 */     return createMidCloudText_OneColumn(midtxt, paintProps);
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createMidCloudText_OneColumn(IMidCloudText midtxt, PaintProperties paintProps)
/*      */   {
/* 2206 */     double turblocation = 0.0D; double icelocation = 0.0D;
/*      */ 
/* 2208 */     ArrayList mlist = new ArrayList();
/*      */ 
/* 2210 */     setScales(paintProps);
/*      */ 
/* 2214 */     List contents = new ArrayList();
/*      */ 
/* 2217 */     contents.addAll(getTokens(midtxt.getCloudAmounts()));
/* 2218 */     contents.addAll(getTokens(midtxt.getCloudTypes()));
/*      */ 
/* 2221 */     if (midtxt.hasTurbulence())
/*      */     {
/* 2223 */       StringTokenizer tok = new StringTokenizer(midtxt.getTurbulenceLevels(), "/");
/*      */ 
/* 2226 */       if (tok.hasMoreTokens()) contents.add(tok.nextToken()); else {
/* 2227 */         contents.add("XXX");
/*      */       }
/*      */ 
/* 2230 */       if (tok.hasMoreTokens()) contents.add(tok.nextToken()); else {
/* 2231 */         contents.add("XXX");
/*      */       }
/*      */ 
/* 2234 */       turblocation = contents.size() - 1.0D;
/*      */     }
/*      */ 
/* 2238 */     if (midtxt.hasIcing())
/*      */     {
/* 2241 */       StringTokenizer tok = new StringTokenizer(midtxt.getIcingLevels(), "/");
/*      */ 
/* 2244 */       if (tok.hasMoreTokens()) contents.add(tok.nextToken()); else {
/* 2245 */         contents.add("XXX");
/*      */       }
/*      */ 
/* 2248 */       if (tok.hasMoreTokens()) contents.add(tok.nextToken()); else
/* 2249 */         contents.add("XXX");
/* 2250 */       icelocation = contents.size() - 1.0D;
/*      */     }
/*      */ 
/* 2254 */     if (midtxt.hasTstorm()) {
/* 2255 */       contents.addAll(getTokens(midtxt.getTstormTypes()));
/* 2256 */       contents.add("CB");
/*      */ 
/* 2258 */       StringTokenizer tok = new StringTokenizer(midtxt.getTstormLevels(), "/");
/*      */ 
/* 2261 */       if (tok.hasMoreTokens()) contents.add(tok.nextToken()); else {
/* 2262 */         contents.add("XXX");
/*      */       }
/*      */ 
/* 2265 */       if (tok.hasMoreTokens()) contents.add(tok.nextToken()); else {
/* 2266 */         contents.add("XXX");
/*      */       }
/*      */     }
/* 2269 */     if (contents.isEmpty()) return mlist;
/*      */     String[] txtstr;
/*      */     String[] txtstr;
/* 2275 */     if (contents.size() == 1) {
/* 2276 */       txtstr = new String[] { (String)contents.get(0) };
/*      */     }
/*      */     else {
/* 2279 */       txtstr = new String[contents.size()];
/* 2280 */       for (int n = 0; n < contents.size(); n++) {
/* 2281 */         if ((midtxt.hasIcing()) || (midtxt.hasTurbulence())) {
/* 2282 */           txtstr[n] = String.format("    %-4.4s\n", new Object[] { contents.get(n) });
/*      */         }
/*      */         else {
/* 2285 */           txtstr[n] = String.format("%-4.4s\n", new Object[] { contents.get(n) });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2293 */     Text txt = new Text(null, midtxt.getFontName(), midtxt.getFontSize(), midtxt.getJustification(), 
/* 2294 */       midtxt.getPosition(), 0.0D, IText.TextRotation.SCREEN_RELATIVE, txtstr, 
/* 2295 */       midtxt.getStyle(), midtxt.getTextColor(), 0, 0, true, IText.DisplayType.BOX, "Text", "General Text");
/*      */ 
/* 2297 */     mlist.addAll(createDisplayElements(txt, paintProps));
/*      */ 
/* 2300 */     if (midtxt.hasTurbulence()) {
/* 2301 */       double voffset = turblocation - contents.size() / 2;
/* 2302 */       mlist.addAll(createMidCloudSymbol(midtxt, voffset, midtxt.getTurbulencePattern(), paintProps));
/*      */     }
/*      */ 
/* 2306 */     if (midtxt.hasIcing()) {
/* 2307 */       double voffset = icelocation - contents.size() / 2;
/* 2308 */       mlist.addAll(createMidCloudSymbol(midtxt, voffset, midtxt.getIcingPattern(), paintProps));
/*      */     }
/*      */ 
/* 2311 */     return mlist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createMidCloudText_TwoColumns(IMidCloudText midtxt, PaintProperties paintProps)
/*      */   {
/* 2316 */     String BLANK = new String(" ");
/* 2317 */     double turblocation = 0.0D; double icelocation = 0.0D;
/*      */ 
/* 2319 */     ArrayList mlist = new ArrayList();
/*      */ 
/* 2321 */     setScales(paintProps);
/*      */ 
/* 2325 */     List contents = new ArrayList();
/*      */ 
/* 2328 */     contents.addAll(getTokens(midtxt.getCloudAmounts()));
/* 2329 */     contents.addAll(getTokens(midtxt.getCloudTypes()));
/*      */ 
/* 2332 */     if (midtxt.hasTurbulence()) {
/* 2333 */       if (contents.size() % 2 == 1) contents.add(BLANK);
/* 2334 */       contents.addAll(getLevels(midtxt.getTurbulenceLevels(), null));
/* 2335 */       turblocation = contents.size() / 2.0D - 1.0D;
/*      */     }
/*      */ 
/* 2339 */     if (midtxt.hasIcing()) {
/* 2340 */       if (contents.size() % 2 == 1) contents.add(BLANK);
/* 2341 */       contents.addAll(getLevels(midtxt.getIcingLevels(), null));
/* 2342 */       icelocation = contents.size() / 2.0D - 1.0D;
/*      */     }
/*      */ 
/* 2346 */     if (midtxt.hasTstorm()) {
/* 2347 */       if (contents.size() % 2 == 1) contents.add(BLANK);
/* 2348 */       contents.addAll(getTokens(midtxt.getTstormTypes()));
/* 2349 */       if (contents.size() % 2 == 1) contents.add(BLANK);
/* 2350 */       contents.addAll(getLevels(midtxt.getTstormLevels(), "CB"));
/*      */     }
/*      */ 
/* 2353 */     if (contents.isEmpty()) return mlist;
/*      */     String[] txtstr;
/*      */     String[] txtstr;
/* 2359 */     if (contents.size() == 1) {
/* 2360 */       txtstr = new String[] { (String)contents.get(0) };
/*      */     }
/*      */     else {
/* 2363 */       if (contents.size() % 2 == 1) contents.add(BLANK);
/* 2364 */       txtstr = new String[contents.size() / 2];
/* 2365 */       Iterator iter = contents.iterator();
/* 2366 */       for (int n = 0; n < contents.size() / 2; n++) {
/* 2367 */         txtstr[n] = String.format("%-4.4s %-4.4s\n", new Object[] { iter.next(), iter.next() });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2374 */     Text txt = new Text(null, midtxt.getFontName(), midtxt.getFontSize(), midtxt.getJustification(), 
/* 2375 */       midtxt.getPosition(), 0.0D, IText.TextRotation.SCREEN_RELATIVE, txtstr, 
/* 2376 */       midtxt.getStyle(), midtxt.getTextColor(), 0, 0, true, IText.DisplayType.BOX, "Text", "General Text");
/*      */ 
/* 2378 */     mlist.addAll(createDisplayElements(txt, paintProps));
/*      */ 
/* 2381 */     if (midtxt.hasTurbulence()) {
/* 2382 */       double voffset = turblocation - contents.size() / 4.0D;
/* 2383 */       mlist.addAll(createMidCloudSymbol(midtxt, voffset, midtxt.getTurbulencePattern(), paintProps));
/*      */     }
/*      */ 
/* 2387 */     if (midtxt.hasIcing()) {
/* 2388 */       double voffset = icelocation - contents.size() / 4.0D;
/* 2389 */       mlist.addAll(createMidCloudSymbol(midtxt, voffset, midtxt.getIcingPattern(), paintProps));
/*      */     }
/*      */ 
/* 2392 */     return mlist;
/*      */   }
/*      */ 
/*      */   private List<String> getLevels(String str, String note)
/*      */   {
/* 2403 */     ArrayList lst = new ArrayList();
/* 2404 */     StringTokenizer tok = new StringTokenizer(str, "/");
/*      */ 
/* 2406 */     if (note == null) lst.add(" "); else {
/* 2407 */       lst.add(note);
/*      */     }
/* 2409 */     if (tok.hasMoreTokens()) lst.add(tok.nextToken()); else {
/* 2410 */       lst.add("XXX");
/*      */     }
/* 2412 */     lst.add(" ");
/*      */ 
/* 2414 */     if (tok.hasMoreTokens()) lst.add(tok.nextToken()); else {
/* 2415 */       lst.add("XXX");
/*      */     }
/* 2417 */     return lst;
/*      */   }
/*      */ 
/*      */   private List<String> getTokens(String str)
/*      */   {
/* 2426 */     ArrayList lst = new ArrayList();
/* 2427 */     if ((str == null) || (str.isEmpty())) return lst;
/*      */ 
/* 2429 */     StringTokenizer tok = new StringTokenizer(str, "|");
/* 2430 */     while (tok.hasMoreTokens()) lst.add(tok.nextToken());
/* 2431 */     return lst;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createMidCloudSymbol(IMidCloudText midtxt, double vertOffset, String symbolPattern, PaintProperties paintProps)
/*      */   {
/* 2442 */     ArrayList rlist = new ArrayList();
/*      */ 
/* 2447 */     IFont font = initializeFont(midtxt.getFontName(), midtxt.getFontSize(), midtxt.getStyle());
/* 2448 */     Rectangle2D bounds = this.target.getStringBounds(font, "Xy");
/* 2449 */     bounds = new Rectangle2D.Double(0.0D, 0.0D, bounds.getWidth() / 2.0D, bounds.getHeight());
/* 2450 */     font.dispose();
/* 2451 */     double vertRatio = paintProps.getView().getExtent().getHeight() / paintProps.getCanvasBounds().height;
/* 2452 */     double horizRatio = paintProps.getView().getExtent().getWidth() / paintProps.getCanvasBounds().width;
/*      */ 
/* 2457 */     Color[] clrs = { getDisplayColor(midtxt.getTextColor()) };
/* 2458 */     double symSize = bounds.getHeight() / 12.0D;
/*      */ 
/* 2463 */     Coordinate[] loc = { midtxt.getPosition() };
/* 2464 */     double[] worldPixel = { loc[0].x, loc[0].y, 0.0D };
/* 2465 */     double[] pixel = this.iDescriptor.worldToPixel(worldPixel);
/*      */ 
/* 2470 */     pixel[1] += vertOffset * vertRatio * bounds.getHeight();
/*      */ 
/* 2472 */     if (midtxt.getJustification() == IText.TextJustification.CENTER) {
/* 2473 */       pixel[0] -= bounds.getWidth() * horizRatio * 2.5D;
/*      */     }
/* 2475 */     else if (midtxt.getJustification() == IText.TextJustification.LEFT_JUSTIFY) {
/* 2476 */       pixel[0] += bounds.getWidth() * horizRatio * 2.0D;
/*      */     }
/* 2478 */     else if (midtxt.getJustification() == IText.TextJustification.RIGHT_JUSTIFY) {
/* 2479 */       pixel[0] -= bounds.getWidth() * horizRatio * 7.0D;
/*      */     }
/*      */ 
/* 2482 */     String[] ids = symbolPattern.split("\\|");
/*      */ 
/* 2487 */     if (ids.length == 1)
/*      */     {
/* 2489 */       double[] locSym = this.iDescriptor.pixelToWorld(new double[] { pixel[0], pixel[1], 0.0D });
/* 2490 */       Coordinate loc1 = new Coordinate(locSym[0], locSym[1]);
/*      */ 
/* 2495 */       Symbol sym = new Symbol(null, clrs, 1.0F, symSize, Boolean.valueOf(false), loc1, 
/* 2496 */         "Symbol", ids[0]);
/* 2497 */       rlist.addAll(createDisplayElements(sym, paintProps));
/*      */     }
/* 2499 */     else if (ids.length > 1)
/*      */     {
/* 2501 */       double newX = pixel[0] - bounds.getWidth() * horizRatio;
/*      */ 
/* 2503 */       double[] locSym = this.iDescriptor.pixelToWorld(new double[] { newX, pixel[1], 0.0D });
/* 2504 */       Coordinate loc1 = new Coordinate(locSym[0], locSym[1]);
/*      */ 
/* 2509 */       Symbol sym = new Symbol(null, clrs, 1.0F, symSize, Boolean.valueOf(false), loc1, 
/* 2510 */         "Symbol", ids[0]);
/* 2511 */       rlist.addAll(createDisplayElements(sym, paintProps));
/*      */ 
/* 2513 */       newX = pixel[0] + bounds.getWidth() * horizRatio;
/*      */ 
/* 2515 */       locSym = this.iDescriptor.pixelToWorld(new double[] { newX, pixel[1], 0.0D });
/* 2516 */       Coordinate loc2 = new Coordinate(locSym[0], locSym[1]);
/*      */ 
/* 2521 */       Symbol sym2 = new Symbol(null, clrs, 1.0F, symSize, Boolean.valueOf(false), loc2, 
/* 2522 */         "Symbol", ids[1]);
/* 2523 */       rlist.addAll(createDisplayElements(sym2, paintProps));
/*      */     }
/*      */ 
/* 2527 */     return rlist;
/*      */   }
/*      */ 
/*      */   private void handleLinePattern(LinePattern pattern, double[][] pts)
/*      */   {
/* 2536 */     handleLinePattern(pattern, pts, ScaleType.SCALE_ALL_SEGMENTS);
/*      */   }
/*      */ 
/*      */   private void handleLinePattern(LinePattern pattern, double[][] pts, ScaleType stype)
/*      */   {
/* 2551 */     double scale = this.elem.getSizeScale();
/* 2552 */     if (scale <= 0.0D) scale = 1.0D;
/* 2553 */     double sfactor = this.deviceScale * scale;
/* 2554 */     Color[] clr = getDisplayColors(this.elem.getColors());
/*      */ 
/* 2559 */     Coordinate[] coords = new Coordinate[pts.length];
/* 2560 */     for (int i = 0; i < pts.length; i++) {
/* 2561 */       coords[i] = new Coordinate(pts[i][0], pts[i][1]);
/*      */     }
/* 2563 */     GeometryFactory gf = new GeometryFactory();
/* 2564 */     LineString ls = gf.createLineString(coords);
/*      */ 
/* 2567 */     double totalDist = ls.getLength();
/*      */ 
/* 2573 */     if ((pattern.hasArrowHead()) && 
/* 2574 */       (pattern.getArrowHeadType() == ArrowHead.ArrowHeadType.FILLED)) {
/* 2575 */       totalDist -= this.arrow.getLength();
/*      */     }
/*      */ 
/* 2583 */     LengthIndexedLine lil = new LengthIndexedLine(ls);
/* 2584 */     LocationIndexedLine lol = new LocationIndexedLine(ls);
/* 2585 */     LengthLocationMap llm = new LengthLocationMap(ls);
/*      */ 
/* 2590 */     double psize = pattern.getLength() * sfactor;
/* 2591 */     double numPatterns = Math.floor(totalDist / psize);
/*      */ 
/* 2598 */     double leftover = totalDist - numPatterns * psize;
/* 2599 */     if (leftover > 0.5D * psize)
/*      */     {
/* 2601 */       numPatterns += 1.0D;
/* 2602 */       leftover -= psize;
/*      */     }
/*      */ 
/* 2606 */     if (stype == ScaleType.SCALE_BLANK_LINE_ONLY)
/* 2607 */       pattern = pattern.scaleBlankLineToLength(totalDist / (numPatterns * sfactor));
/*      */     else {
/* 2609 */       pattern = pattern.scaleToLength(totalDist / (numPatterns * sfactor));
/*      */     }
/*      */ 
/* 2615 */     if (numPatterns < 1.0D) {
/* 2616 */       Coordinate[] ncoords = lil.extractLine(0.0D, totalDist).getCoordinates();
/* 2617 */       double[][] npts = toDouble(ncoords);
/* 2618 */       this.wfs[0].addLineSegment(npts);
/* 2619 */       return;
/*      */     }
/*      */ 
/* 2625 */     double begPat = 0.0D;
/* 2626 */     LinearLocation linloc0 = llm.getLocation(begPat);
/* 2627 */     for (int n = 0; n < (int)Math.floor(numPatterns); n++)
/*      */     {
/* 2629 */       double patlen = pattern.getLength() * sfactor;
/* 2630 */       double endPat = begPat + patlen;
/* 2631 */       LinearLocation linloc1 = llm.getLocation(endPat);
/* 2632 */       LengthIndexedLine sublil = new LengthIndexedLine(lol.extractLine(linloc0, linloc1));
/*      */ 
/* 2637 */       double currDist = 0.0D;
/* 2638 */       for (PatternSegment seg : pattern.getSegments()) {
/* 2639 */         int colorNum = seg.getColorLocation();
/*      */ 
/* 2642 */         if (colorNum >= this.wfs.length) colorNum = 0;
/*      */ 
/* 2645 */         double seglen = seg.getLength() * sfactor;
/*      */ 
/* 2647 */         double endLoc = currDist + seglen;
/*      */ 
/* 2652 */         switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$display$PatternSegment$PatternType()[seg.getPatternType().ordinal()])
/*      */         {
/*      */         case 1:
/* 2658 */           break;
/*      */         case 2:
/* 2664 */           Geometry section = sublil.extractLine(currDist, endLoc);
/* 2665 */           Coordinate[] newcoords = section.getCoordinates();
/*      */ 
/* 2669 */           double[][] newpts = toDouble(newcoords);
/* 2670 */           this.wfs[colorNum].addLineSegment(newpts);
/* 2671 */           break;
/*      */         case 3:
/* 2678 */           double start = 0.0D;
/* 2679 */           double end = 360.0D;
/* 2680 */           ArcPatternApplicator circ = new ArcPatternApplicator(sublil, currDist, endLoc);
/* 2681 */           circ.setArcAttributes(start, end, seg.getNumberInArc());
/* 2682 */           this.wfs[colorNum].addLineSegment(circ.calculateLines());
/* 2683 */           break;
/*      */         case 4:
/* 2690 */           double start = 0.0D;
/* 2691 */           double end = 360.0D;
/* 2692 */           ArcPatternApplicator circf = new ArcPatternApplicator(sublil, currDist, endLoc);
/* 2693 */           circf.setArcAttributes(start, end, seg.getNumberInArc());
/* 2694 */           Coordinate[] carea = circf.calculateFillArea();
/* 2695 */           LineString[] circle = toLineString(carea);
/* 2696 */           this.ss.addPolygonPixelSpace(circle, new RGB(clr[seg.getColorLocation()].getRed(), clr[seg.getColorLocation()].getGreen(), clr[seg.getColorLocation()].getBlue()));
/* 2697 */           break;
/*      */         case 5:
/*      */           double end;
/*      */           double start;
/*      */           double end;
/* 2700 */           if (seg.isReverseSide()) {
/* 2701 */             double start = 0.0D;
/* 2702 */             end = 180.0D;
/*      */           }
/*      */           else {
/* 2705 */             start = 0.0D;
/* 2706 */             end = -180.0D;
/*      */           }
/*      */ 
/* 2712 */           ArcPatternApplicator app180 = new ArcPatternApplicator(sublil, currDist, endLoc);
/* 2713 */           app180.setArcAttributes(start, end, seg.getNumberInArc());
/* 2714 */           this.wfs[colorNum].addLineSegment(app180.calculateLines());
/* 2715 */           break;
/*      */         case 6:
/*      */           double end;
/*      */           double start;
/*      */           double end;
/* 2718 */           if (seg.isReverseSide()) {
/* 2719 */             double start = 0.0D;
/* 2720 */             end = 180.0D;
/*      */           }
/*      */           else {
/* 2723 */             start = 0.0D;
/* 2724 */             end = -180.0D;
/*      */           }
/*      */ 
/* 2731 */           ArcPatternApplicator app = new ArcPatternApplicator(sublil, currDist, endLoc);
/* 2732 */           app.setArcAttributes(start, end, seg.getNumberInArc());
/* 2733 */           app.addSegmentToFill(true);
/* 2734 */           Coordinate[] area = app.calculateFillArea();
/* 2735 */           LineString[] arc = toLineString(area);
/*      */ 
/* 2740 */           this.ss.addPolygonPixelSpace(arc, new RGB(clr[seg.getColorLocation()].getRed(), clr[seg.getColorLocation()].getGreen(), clr[seg.getColorLocation()].getBlue()));
/* 2741 */           this.wfs[colorNum].addLineSegment(app.getSegmentPts());
/* 2742 */           break;
/*      */         case 7:
/*      */           double end;
/*      */           double start;
/*      */           double end;
/* 2745 */           if (seg.isReverseSide()) {
/* 2746 */             double start = 0.0D;
/* 2747 */             end = 180.0D;
/*      */           }
/*      */           else {
/* 2750 */             start = 0.0D;
/* 2751 */             end = -180.0D;
/*      */           }
/*      */ 
/* 2756 */           ArcPatternApplicator app180c = new ArcPatternApplicator(sublil, currDist, endLoc);
/* 2757 */           app180c.setArcAttributes(start, end, seg.getNumberInArc());
/*      */ 
/* 2761 */           this.wfs[colorNum].addLineSegment(app180c.calculateLines());
/* 2762 */           this.wfs[colorNum].addLineSegment(app180c.getSegmentPts());
/* 2763 */           break;
/*      */         case 8:
/*      */           double end;
/*      */           double start;
/*      */           double end;
/* 2766 */           if (seg.isReverseSide()) {
/* 2767 */             double start = 0.0D;
/* 2768 */             end = 90.0D;
/*      */           }
/*      */           else {
/* 2771 */             start = 0.0D;
/* 2772 */             end = -90.0D;
/*      */           }
/*      */ 
/* 2777 */           ArcPatternApplicator app90 = new ArcPatternApplicator(sublil, currDist, endLoc);
/* 2778 */           app90.setArcAttributes(start, end, seg.getNumberInArc());
/*      */ 
/* 2782 */           this.wfs[colorNum].addLineSegment(app90.calculateLines());
/* 2783 */           this.wfs[colorNum].addLineSegment(app90.getSegmentPts());
/* 2784 */           break;
/*      */         case 9:
/*      */           double end;
/*      */           double start;
/*      */           double end;
/* 2787 */           if (seg.isReverseSide()) {
/* 2788 */             double start = -45.0D;
/* 2789 */             end = 225.0D;
/*      */           }
/*      */           else {
/* 2792 */             start = 45.0D;
/* 2793 */             end = -225.0D;
/*      */           }
/*      */ 
/* 2799 */           ArcPatternApplicator app270 = new ArcPatternApplicator(sublil, currDist, endLoc);
/* 2800 */           app270.setArcAttributes(start, end, seg.getNumberInArc());
/* 2801 */           this.wfs[colorNum].addLineSegment(app270.calculateLines());
/* 2802 */           break;
/*      */         case 10:
/*      */           double end;
/*      */           double start;
/*      */           double end;
/* 2805 */           if (seg.isReverseSide()) {
/* 2806 */             double start = -45.0D;
/* 2807 */             end = 225.0D;
/*      */           }
/*      */           else {
/* 2810 */             start = 45.0D;
/* 2811 */             end = -225.0D;
/*      */           }
/*      */ 
/* 2816 */           ArcPatternApplicator app270l = new ArcPatternApplicator(sublil, currDist, endLoc);
/* 2817 */           app270l.setArcAttributes(start, end, seg.getNumberInArc());
/*      */ 
/* 2821 */           this.wfs[colorNum].addLineSegment(app270l.calculateLines());
/* 2822 */           this.wfs[colorNum].addLineSegment(app270l.getSegmentPts());
/* 2823 */           break;
/*      */         case 11:
/* 2830 */           CornerPatternApplicator box = new CornerPatternApplicator(sublil, currDist, endLoc);
/* 2831 */           box.setHeight(seg.getOffsetSize() * sfactor);
/* 2832 */           box.setPatternType(CornerPatternApplicator.CornerPattern.BOX);
/* 2833 */           this.wfs[colorNum].addLineSegment(box.calculateLines());
/* 2834 */           break;
/*      */         case 12:
/* 2841 */           CornerPatternApplicator boxf = new CornerPatternApplicator(sublil, currDist, endLoc);
/* 2842 */           boxf.setHeight(seg.getOffsetSize() * sfactor);
/* 2843 */           boxf.setPatternType(CornerPatternApplicator.CornerPattern.BOX);
/* 2844 */           Coordinate[] boxarea = boxf.calculateFillArea();
/* 2845 */           LineString[] barea = toLineString(boxarea);
/* 2846 */           this.ss.addPolygonPixelSpace(barea, new RGB(clr[seg.getColorLocation()].getRed(), clr[seg.getColorLocation()].getGreen(), clr[seg.getColorLocation()].getBlue()));
/* 2847 */           break;
/*      */         case 13:
/* 2853 */           CornerPatternApplicator ex = new CornerPatternApplicator(sublil, currDist, endLoc);
/* 2854 */           ex.setHeight(seg.getOffsetSize() * sfactor);
/* 2855 */           ex.setPatternType(CornerPatternApplicator.CornerPattern.X_PATTERN);
/* 2856 */           double[][] exes = ex.calculateLines();
/* 2857 */           double[][] slash1 = { exes[0], exes[1] };
/* 2858 */           double[][] slash2 = { exes[2], exes[3] };
/*      */ 
/* 2862 */           this.wfs[colorNum].addLineSegment(slash1);
/* 2863 */           this.wfs[colorNum].addLineSegment(slash2);
/* 2864 */           break;
/*      */         case 14:
/* 2871 */           CornerPatternApplicator ze = new CornerPatternApplicator(sublil, currDist, endLoc);
/* 2872 */           ze.setHeight(seg.getOffsetSize() * sfactor);
/* 2873 */           ze.setPatternType(CornerPatternApplicator.CornerPattern.Z_PATTERN);
/* 2874 */           this.wfs[colorNum].addLineSegment(ze.calculateLines());
/* 2875 */           break;
/*      */         case 15:
/* 2882 */           CornerPatternApplicator dl = new CornerPatternApplicator(sublil, currDist, endLoc);
/* 2883 */           dl.setHeight(seg.getOffsetSize() * sfactor);
/* 2884 */           dl.setPatternType(CornerPatternApplicator.CornerPattern.DOUBLE_LINE);
/* 2885 */           double[][] segs = dl.calculateLines();
/* 2886 */           double[][] top = { segs[0], segs[1] };
/* 2887 */           double[][] bottom = { segs[2], segs[3] };
/*      */ 
/* 2891 */           this.wfs[colorNum].addLineSegment(top);
/* 2892 */           this.wfs[colorNum].addLineSegment(bottom);
/* 2893 */           break;
/*      */         case 16:
/* 2899 */           CornerPatternApplicator tick = new CornerPatternApplicator(sublil, currDist, endLoc);
/* 2900 */           tick.setHeight(seg.getOffsetSize() * sfactor);
/* 2901 */           tick.setPatternType(CornerPatternApplicator.CornerPattern.TICK);
/*      */ 
/* 2906 */           this.wfs[colorNum].addLineSegment(tick.getSegmentPts());
/* 2907 */           this.wfs[colorNum].addLineSegment(tick.calculateLines());
/* 2908 */           break;
/*      */         case 17:
/* 2911 */           double start = -120.0D;
/* 2912 */           double end = 120.0D;
/*      */ 
/* 2916 */           ArcPatternApplicator arrow = new ArcPatternApplicator(sublil, currDist, endLoc);
/* 2917 */           arrow.setArcAttributes(start, end, seg.getNumberInArc());
/*      */ 
/* 2921 */           this.wfs[colorNum].addLineSegment(arrow.calculateLines());
/* 2922 */           this.wfs[colorNum].addLineSegment(arrow.getSegmentPts());
/* 2923 */           break;
/*      */         default:
/* 2929 */           System.out.println("Pattern definition: " + seg.getPatternType().toString() + 
/* 2930 */             " is not found.  Ignoring...");
/*      */         }
/*      */ 
/* 2938 */         currDist = endLoc;
/*      */       }
/*      */ 
/* 2942 */       begPat = endPat;
/* 2943 */       linloc0 = linloc1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private double[][] toDouble(Coordinate[] coords)
/*      */   {
/* 2956 */     double[][] dpts = new double[coords.length][3];
/*      */ 
/* 2958 */     for (int k = 0; k < coords.length; k++) {
/* 2959 */       dpts[k][0] = coords[k].x;
/* 2960 */       dpts[k][1] = coords[k].y;
/*      */     }
/*      */ 
/* 2963 */     return dpts;
/*      */   }
/*      */ 
/*      */   private LineString[] toLineString(Coordinate[] coords)
/*      */   {
/* 2973 */     LineString[] ls = { this.gf.createLineString(coords) };
/* 2974 */     return ls;
/*      */   }
/*      */ 
/*      */   private LineString[] toLineString(double[][] points)
/*      */   {
/* 2984 */     Coordinate[] coords = new Coordinate[points.length];
/* 2985 */     for (int j = 0; j < points.length; j++) {
/* 2986 */       coords[j] = new Coordinate(points[j][0], points[j][1]);
/*      */     }
/*      */ 
/* 2989 */     LineString[] ls = { this.gf.createLineString(coords) };
/* 2990 */     return ls;
/*      */   }
/*      */ 
/*      */   private double[][] ensureClosed(double[][] data)
/*      */   {
/* 3001 */     int n = data.length - 1;
/*      */ 
/* 3006 */     if ((data[0][0] == data[n][0]) && (data[0][1] == data[n][1])) {
/* 3007 */       return data;
/*      */     }
/*      */ 
/* 3013 */     double[][] newdata = new double[data.length + 1][3];
/* 3014 */     for (int i = 0; i < data.length; i++) newdata[i] = data[i];
/* 3015 */     newdata[data.length] = newdata[0];
/* 3016 */     return newdata;
/*      */   }
/*      */ 
/*      */   private FillDisplayElement createFill(double[][] area)
/*      */   {
/* 3030 */     IShadedShape fillarea = this.target.createShadedShape(false, this.iDescriptor, true);
/*      */ 
/* 3036 */     if ((this.elem.getFillPattern() != FillPatternList.FillPattern.TRANSPARENCY) && 
/* 3037 */       (this.elem.getFillPattern() != FillPatternList.FillPattern.SOLID)) {
/* 3038 */       FillPatternList fpl = new FillPatternList();
/* 3039 */       byte[] fpattern = fpl.getFillPattern(this.elem.getFillPattern());
/* 3040 */       fillarea.setFillPattern(fpattern);
/*      */     }
/*      */ 
/* 3046 */     Coordinate[] coords = new Coordinate[area.length];
/* 3047 */     for (int i = 0; i < area.length; i++) {
/* 3048 */       coords[i] = new Coordinate(area[i][0], area[i][1]);
/*      */     }
/*      */ 
/* 3054 */     LineString[] ls = toLineString(coords);
/*      */ 
/* 3059 */     Color[] dspClr = getDisplayColors(this.elem.getColors());
/* 3060 */     Color fillClr = dspClr[0];
/* 3061 */     if ((dspClr.length > 1) && (dspClr[1] != null)) {
/* 3062 */       fillClr = dspClr[1];
/*      */     }
/*      */ 
/* 3065 */     fillarea.addPolygonPixelSpace(ls, new RGB(fillClr.getRed(), 
/* 3066 */       fillClr.getGreen(), 
/* 3067 */       fillClr.getBlue()));
/* 3068 */     fillarea.compile();
/*      */ 
/* 3070 */     float alpha = 1.0F;
/*      */ 
/* 3072 */     if (this.elem.getFillPattern() == FillPatternList.FillPattern.TRANSPARENCY) alpha = 0.5F;
/*      */ 
/* 3077 */     return new FillDisplayElement(fillarea, alpha);
/*      */   }
/*      */ 
/*      */   private void setScales(PaintProperties props)
/*      */   {
/* 3093 */     IExtent pe = props.getView().getExtent();
/* 3094 */     this.deviceScale = (pe.getHeight() / 300.0D);
/*      */ 
/* 3099 */     Rectangle bounds = props.getCanvasBounds();
/* 3100 */     this.screenToExtent = (pe.getHeight() / bounds.height);
/*      */ 
/* 3102 */     this.screenToWorldRatio = (bounds.width / pe.getWidth());
/*      */   }
/*      */ 
/*      */   private double northOffsetAngle(Coordinate loc)
/*      */   {
/* 3112 */     double delta = 0.05D;
/*      */ 
/* 3118 */     double[] south = { loc.x, loc.y - delta, 0.0D };
/* 3119 */     double[] pt1 = this.iDescriptor.worldToPixel(south);
/*      */ 
/* 3121 */     double[] north = { loc.x, loc.y + delta, 0.0D };
/* 3122 */     double[] pt2 = this.iDescriptor.worldToPixel(north);
/*      */ 
/* 3125 */     return -90.0D - Math.toDegrees(Math.atan2(pt2[1] - pt1[1], pt2[0] - pt1[0]));
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createHashMark(IVector vect)
/*      */   {
/* 3134 */     double sfactor = 10.0D * this.deviceScale;
/* 3135 */     double spaceFactor = sfactor * 0.25D;
/* 3136 */     double spacing = 1.0D * spaceFactor;
/* 3137 */     if (vect.getLineWidth() > 3.0D) spacing += 0.25D * spaceFactor * (vect.getLineWidth() - 3.0F);
/*      */ 
/* 3139 */     double scaleSize = sfactor * vect.getSizeScale();
/*      */ 
/* 3144 */     ArrayList slist = new ArrayList();
/* 3145 */     IWireframeShape hash = this.target.createWireframeShape(false, this.iDescriptor);
/*      */ 
/* 3150 */     double[] tmp = { vect.getLocation().x, vect.getLocation().y, 0.0D };
/* 3151 */     double[] start = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/* 3157 */     double angle = northOffsetAngle(vect.getLocation()) + vect.getDirection();
/* 3158 */     double theta = Math.toDegrees(Math.atan(spacing / scaleSize));
/* 3159 */     double dist = 0.5D * Math.sqrt(spacing * spacing + scaleSize * scaleSize);
/*      */ 
/* 3165 */     double dX1 = dist * Math.cos(Math.toRadians(angle - theta));
/* 3166 */     double dY1 = dist * Math.sin(Math.toRadians(angle - theta));
/*      */ 
/* 3168 */     double dX2 = dist * Math.cos(Math.toRadians(angle + theta));
/* 3169 */     double dY2 = dist * Math.sin(Math.toRadians(angle + theta));
/*      */ 
/* 3174 */     hash.addLineSegment(new double[][] { { start[0] + dX1, start[1] - dY1, 0.0D }, 
/* 3175 */       { start[0] - dX2, start[1] + dY2, 0.0D } });
/*      */ 
/* 3177 */     hash.addLineSegment(new double[][] { { start[0] - dX1, start[1] + dY1, 0.0D }, 
/* 3178 */       { start[0] + dX2, start[1] - dY2, 0.0D } });
/*      */ 
/* 3183 */     hash.compile();
/* 3184 */     slist.add(new LineDisplayElement(hash, getDisplayColor(vect.getColor()), vect.getLineWidth()));
/* 3185 */     return slist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createHashMarks(List<IVector> vectors)
/*      */   {
/* 3194 */     double sfactor = 10.0D * this.deviceScale;
/* 3195 */     double spaceFactor = sfactor * 0.25D;
/* 3196 */     double spacing = 1.0D * spaceFactor;
/*      */ 
/* 3202 */     Map hashMarkMap = new HashMap();
/*      */ 
/* 3204 */     float lineWidth = ((IVector)vectors.get(0)).getLineWidth();
/*      */     double scaleSize;
/* 3206 */     for (IVector vect : vectors)
/*      */     {
/* 3208 */       if (vect.getLineWidth() > 3.0D) spacing += 0.25D * spaceFactor * (vect.getLineWidth() - 3.0F);
/*      */ 
/* 3210 */       scaleSize = sfactor * vect.getSizeScale();
/*      */ 
/* 3212 */       Color color = vect.getColor();
/*      */ 
/* 3218 */       IWireframeShape hashMarks = (IWireframeShape)hashMarkMap.get(color);
/* 3219 */       if (hashMarks == null) {
/* 3220 */         hashMarks = this.target.createWireframeShape(false, this.iDescriptor);
/* 3221 */         hashMarkMap.put(color, hashMarks);
/*      */       }
/*      */ 
/* 3227 */       double[] tmp = { vect.getLocation().x, vect.getLocation().y, 0.0D };
/* 3228 */       double[] start = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/* 3234 */       double angle = northOffsetAngle(vect.getLocation()) + vect.getDirection();
/* 3235 */       double theta = Math.toDegrees(Math.atan(spacing / scaleSize));
/* 3236 */       double dist = 0.5D * Math.sqrt(spacing * spacing + scaleSize * scaleSize);
/*      */ 
/* 3242 */       double dX1 = dist * Math.cos(Math.toRadians(angle - theta));
/* 3243 */       double dY1 = dist * Math.sin(Math.toRadians(angle - theta));
/*      */ 
/* 3245 */       double dX2 = dist * Math.cos(Math.toRadians(angle + theta));
/* 3246 */       double dY2 = dist * Math.sin(Math.toRadians(angle + theta));
/*      */ 
/* 3251 */       hashMarks.addLineSegment(new double[][] { { start[0] + dX1, start[1] - dY1, 0.0D }, 
/* 3252 */         { start[0] - dX2, start[1] + dY2, 0.0D } });
/*      */ 
/* 3254 */       hashMarks.addLineSegment(new double[][] { { start[0] - dX1, start[1] + dY1, 0.0D }, 
/* 3255 */         { start[0] + dX2, start[1] - dY2, 0.0D } });
/*      */     }
/*      */ 
/* 3268 */     ArrayList slist = new ArrayList();
/*      */ 
/* 3274 */     for (Color color : hashMarkMap.keySet()) {
/* 3275 */       IWireframeShape hashMarks = (IWireframeShape)hashMarkMap.get(color);
/* 3276 */       hashMarks.compile();
/* 3277 */       slist.add(new LineDisplayElement(hashMarks, color, lineWidth));
/*      */     }
/*      */ 
/* 3280 */     return slist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createArrow(IVector vect)
/*      */   {
/* 3289 */     double sfactor = this.deviceScale * vect.getSizeScale();
/*      */ 
/* 3294 */     ArrayList slist = new ArrayList();
/* 3295 */     IWireframeShape arrow = this.target.createWireframeShape(false, this.iDescriptor);
/*      */ 
/* 3300 */     double[] tmp = { vect.getLocation().x, vect.getLocation().y, 0.0D };
/* 3301 */     double[] start = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/* 3306 */     double speed = 10.0D;
/* 3307 */     if (!vect.hasDirectionOnly()) speed = vect.getSpeed();
/* 3308 */     double arrowLength = sfactor * speed;
/*      */ 
/* 3313 */     double angle = 90.0D - northOffsetAngle(vect.getLocation()) + vect.getDirection();
/*      */ 
/* 3318 */     double[] end = new double[3];
/* 3319 */     start[0] += arrowLength * Math.cos(Math.toRadians(angle));
/* 3320 */     start[1] += arrowLength * Math.sin(Math.toRadians(angle));
/* 3321 */     end[2] = 0.0D;
/*      */ 
/* 3326 */     arrow.addLineSegment(new double[][] { start, end });
/*      */ 
/* 3331 */     double pointAngle = 60.0D;
/* 3332 */     double height = this.deviceScale * vect.getArrowHeadSize() * 2.0D;
/* 3333 */     ArrowHead head = new ArrowHead(new Coordinate(end[0], end[1]), 
/* 3334 */       pointAngle, angle, height, ArrowHead.ArrowHeadType.FILLED);
/* 3335 */     Coordinate[] ahead = head.getArrowHeadShape();
/* 3336 */     Color clr = getDisplayColor(vect.getColor());
/* 3337 */     IShadedShape arrowHead = this.target.createShadedShape(false, this.iDescriptor, false);
/* 3338 */     arrowHead.addPolygonPixelSpace(toLineString(ahead), new RGB(clr.getRed(), 
/* 3339 */       clr.getGreen(), clr.getBlue()));
/*      */ 
/* 3344 */     if (vect.hasBackgroundMask().booleanValue())
/*      */     {
/* 3349 */       RGB bg = this.backgroundColor.getColor(IBackgroundColorChangedListener.BGColorMode.EDITOR);
/* 3350 */       Color bgColor = new Color(bg.red, bg.green, bg.blue);
/*      */ 
/* 3356 */       IWireframeShape mask = this.target.createWireframeShape(false, this.iDescriptor);
/* 3357 */       mask.addLineSegment(new double[][] { start, end });
/* 3358 */       mask.addLineSegment(toDouble(ahead));
/* 3359 */       mask.compile();
/* 3360 */       slist.add(new LineDisplayElement(mask, bgColor, (float)(vect.getLineWidth() + this.deviceScale)));
/*      */     }
/*      */ 
/* 3367 */     Color dspClr = getDisplayColor(vect.getColor());
/*      */ 
/* 3372 */     arrow.compile();
/* 3373 */     slist.add(new LineDisplayElement(arrow, dspClr, vect.getLineWidth()));
/*      */ 
/* 3378 */     arrowHead.compile();
/* 3379 */     FillDisplayElement fde = new FillDisplayElement(arrowHead, vect.getColor().getAlpha());
/* 3380 */     slist.add(fde);
/*      */ 
/* 3382 */     return slist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createArrows(List<IVector> vectors)
/*      */   {
/* 3396 */     Map arrowMap = new HashMap();
/* 3397 */     Map maskMap = new HashMap();
/* 3398 */     Map arrowHeadMap = new HashMap();
/*      */ 
/* 3400 */     float lineWidth = ((IVector)vectors.get(0)).getLineWidth();
/*      */     Color color;
/* 3402 */     for (IVector vect : vectors)
/*      */     {
/* 3406 */       color = getDisplayColor(vect.getColor());
/* 3407 */       Color bgColor = new Color(0, 0, 0);
/* 3408 */       double sfactor = this.deviceScale * vect.getSizeScale();
/*      */ 
/* 3414 */       IWireframeShape arrows = (IWireframeShape)arrowMap.get(color);
/* 3415 */       if (arrows == null) {
/* 3416 */         arrows = this.target.createWireframeShape(false, this.iDescriptor);
/* 3417 */         arrowMap.put(color, arrows);
/*      */       }
/* 3419 */       IWireframeShape masks = null;
/* 3420 */       if (vect.hasBackgroundMask().booleanValue()) {
/* 3421 */         RGB bg = this.backgroundColor.getColor(IBackgroundColorChangedListener.BGColorMode.EDITOR);
/* 3422 */         bgColor = new Color(bg.red, bg.green, bg.blue);
/* 3423 */         masks = (IWireframeShape)maskMap.get(bgColor);
/* 3424 */         if (masks == null) {
/* 3425 */           masks = this.target.createWireframeShape(false, this.iDescriptor);
/* 3426 */           maskMap.put(bgColor, masks);
/*      */         }
/*      */       }
/* 3429 */       IShadedShape arrowHeads = (IShadedShape)arrowHeadMap.get(color);
/* 3430 */       if (arrowHeads == null) {
/* 3431 */         arrowHeads = this.target.createShadedShape(false, this.iDescriptor, false);
/* 3432 */         arrowHeadMap.put(color, arrowHeads);
/*      */       }
/*      */ 
/* 3438 */       double[] tmp = { vect.getLocation().x, vect.getLocation().y, 0.0D };
/* 3439 */       double[] start = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/* 3444 */       double speed = 10.0D;
/* 3445 */       if (!vect.hasDirectionOnly()) speed = vect.getSpeed();
/* 3446 */       double arrowLength = sfactor * speed;
/*      */ 
/* 3451 */       double angle = 90.0D - northOffsetAngle(vect.getLocation()) + vect.getDirection();
/*      */ 
/* 3456 */       double[] end = new double[3];
/* 3457 */       start[0] += arrowLength * Math.cos(Math.toRadians(angle));
/* 3458 */       start[1] += arrowLength * Math.sin(Math.toRadians(angle));
/* 3459 */       end[2] = 0.0D;
/*      */ 
/* 3464 */       arrows.addLineSegment(new double[][] { start, end });
/*      */ 
/* 3469 */       double pointAngle = 60.0D;
/* 3470 */       double height = this.deviceScale * vect.getArrowHeadSize() * 2.0D;
/* 3471 */       ArrowHead head = new ArrowHead(new Coordinate(end[0], end[1]), 
/* 3472 */         pointAngle, angle, height, ArrowHead.ArrowHeadType.FILLED);
/* 3473 */       Coordinate[] ahead = head.getArrowHeadShape();
/* 3474 */       arrowHeads.addPolygonPixelSpace(toLineString(ahead), new RGB(color.getRed(), 
/* 3475 */         color.getGreen(), color.getBlue()));
/*      */ 
/* 3480 */       if (vect.hasBackgroundMask().booleanValue())
/*      */       {
/* 3484 */         masks.addLineSegment(new double[][] { start, end });
/* 3485 */         masks.addLineSegment(toDouble(ahead));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3493 */     ArrayList slist = new ArrayList();
/*      */ 
/* 3499 */     for (Color color : maskMap.keySet()) {
/* 3500 */       IWireframeShape masks = (IWireframeShape)maskMap.get(color);
/* 3501 */       masks.compile();
/* 3502 */       slist.add(new LineDisplayElement(masks, color, (float)(lineWidth + this.deviceScale)));
/*      */     }
/*      */ 
/* 3505 */     for (Color color : arrowMap.keySet()) {
/* 3506 */       IWireframeShape arrows = (IWireframeShape)arrowMap.get(color);
/* 3507 */       arrows.compile();
/* 3508 */       slist.add(new LineDisplayElement(arrows, color, lineWidth));
/*      */     }
/*      */ 
/* 3511 */     for (Color color : arrowHeadMap.keySet()) {
/* 3512 */       IShadedShape arrowHeads = (IShadedShape)arrowHeadMap.get(color);
/* 3513 */       arrowHeads.compile();
/* 3514 */       slist.add(new FillDisplayElement(arrowHeads, color.getAlpha()));
/*      */     }
/*      */ 
/* 3517 */     return slist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createWindBarb(IVector vect)
/*      */   {
/* 3526 */     double sfactor = this.deviceScale * vect.getSizeScale() * 10.0D;
/* 3527 */     IWireframeShape mask = null;
/* 3528 */     Color bgColor = new Color(0, 0, 0);
/*      */ 
/* 3533 */     ArrayList slist = new ArrayList();
/* 3534 */     IWireframeShape barb = this.target.createWireframeShape(false, this.iDescriptor);
/* 3535 */     IShadedShape flags = this.target.createShadedShape(false, this.iDescriptor, false);
/* 3536 */     if (vect.hasBackgroundMask().booleanValue()) {
/* 3537 */       mask = this.target.createWireframeShape(false, this.iDescriptor);
/* 3538 */       RGB bg = this.backgroundColor.getColor(IBackgroundColorChangedListener.BGColorMode.EDITOR);
/* 3539 */       bgColor = new Color(bg.red, bg.green, bg.blue);
/*      */     }
/*      */ 
/* 3545 */     Color dspClr = getDisplayColor(vect.getColor());
/*      */ 
/* 3550 */     double[] tmp = { vect.getLocation().x, vect.getLocation().y, 0.0D };
/* 3551 */     double[] start = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/* 3556 */     if (vect.getSpeed() < 0.5D) {
/* 3557 */       double[][] pts = calculateCircle(start, sfactor * 0.1D);
/* 3558 */       if (vect.hasBackgroundMask().booleanValue()) {
/* 3559 */         mask.addLineSegment(pts);
/* 3560 */         mask.compile();
/* 3561 */         slist.add(new LineDisplayElement(mask, bgColor, vect.getLineWidth() + (float)this.deviceScale));
/*      */       }
/* 3563 */       barb.addLineSegment(pts);
/* 3564 */       barb.compile();
/* 3565 */       slist.add(new LineDisplayElement(barb, dspClr, vect.getLineWidth()));
/* 3566 */       return slist;
/*      */     }
/*      */ 
/* 3573 */     int speed = (int)Math.floor(vect.getSpeed() + 2.5D);
/* 3574 */     int numflags = speed / 50;
/* 3575 */     int remainder = speed % 50;
/* 3576 */     int numbarbs = remainder / 10;
/* 3577 */     remainder %= 10;
/* 3578 */     int halfbarbs = remainder / 5;
/*      */ 
/* 3580 */     double MAX_SEGMENTS = 6.0D;
/* 3581 */     int numsegs = 2 * numflags + numbarbs + halfbarbs;
/* 3582 */     double segmentSpacing = sfactor / MAX_SEGMENTS;
/* 3583 */     double windLength = segmentSpacing * Math.max(MAX_SEGMENTS, numsegs);
/* 3584 */     double barbLength = sfactor / 3.0D;
/*      */ 
/* 3590 */     double angle = -90.0D - northOffsetAngle(vect.getLocation()) + vect.getDirection();
/* 3591 */     double[] end = new double[3];
/* 3592 */     start[0] += windLength * Math.cos(Math.toRadians(angle));
/* 3593 */     start[1] += windLength * Math.sin(Math.toRadians(angle));
/* 3594 */     end[2] = 0.0D;
/* 3595 */     barb.addLineSegment(new double[][] { start, end });
/* 3596 */     if (vect.hasBackgroundMask().booleanValue()) mask.addLineSegment(new double[][] { start, end });
/*      */ 
/* 3602 */     LineString[] ls = toLineString(new Coordinate[] { new Coordinate(start[0], start[1]), 
/* 3603 */       new Coordinate(end[0], end[1]) });
/* 3604 */     LengthIndexedLine lil = new LengthIndexedLine(ls[0]);
/* 3605 */     double currentLoc = lil.getEndIndex();
/*      */ 
/* 3608 */     double BARB_ANGLE = 70.0D;
/* 3609 */     double barbAngle = angle + BARB_ANGLE;
/* 3610 */     if (vect.getLocation().y < 0.0D) barbAngle = angle - BARB_ANGLE;
/* 3611 */     double cosineBarbAngle = Math.cos(Math.toRadians(barbAngle));
/* 3612 */     double sineBarbAngle = Math.sin(Math.toRadians(barbAngle));
/*      */ 
/* 3617 */     for (int j = 0; j < numflags; j++) {
/* 3618 */       Coordinate[] coords = new Coordinate[4];
/* 3619 */       coords[0] = lil.extractPoint(currentLoc);
/* 3620 */       coords[1] = lil.extractPoint(currentLoc - segmentSpacing);
/* 3621 */       double xtip = coords[1].x + barbLength * cosineBarbAngle;
/* 3622 */       double ytip = coords[1].y + barbLength * sineBarbAngle;
/* 3623 */       coords[2] = new Coordinate(xtip, ytip);
/* 3624 */       coords[3] = coords[0];
/* 3625 */       LineString[] oneFlag = toLineString(coords);
/* 3626 */       flags.addPolygonPixelSpace(oneFlag, new RGB(dspClr.getRed(), dspClr.getGreen(), dspClr.getBlue()));
/* 3627 */       if (vect.hasBackgroundMask().booleanValue()) mask.addLineSegment(toDouble(coords));
/* 3628 */       currentLoc -= 2.0D * segmentSpacing;
/*      */     }
/*      */ 
/* 3634 */     for (int j = 0; j < numbarbs; j++) {
/* 3635 */       Coordinate[] coords = new Coordinate[2];
/* 3636 */       coords[0] = lil.extractPoint(currentLoc);
/* 3637 */       double xtip = coords[0].x + barbLength * cosineBarbAngle;
/* 3638 */       double ytip = coords[0].y + barbLength * sineBarbAngle;
/* 3639 */       coords[1] = new Coordinate(xtip, ytip);
/* 3640 */       double[][] pts = toDouble(coords);
/* 3641 */       barb.addLineSegment(pts);
/* 3642 */       if (vect.hasBackgroundMask().booleanValue()) mask.addLineSegment(pts);
/* 3643 */       currentLoc -= segmentSpacing;
/*      */     }
/*      */ 
/* 3649 */     for (int j = 0; j < halfbarbs; j++) {
/* 3650 */       Coordinate[] coords = new Coordinate[2];
/* 3651 */       coords[0] = lil.extractPoint(currentLoc);
/* 3652 */       double xtip = coords[0].x + 0.5D * barbLength * cosineBarbAngle;
/* 3653 */       double ytip = coords[0].y + 0.5D * barbLength * sineBarbAngle;
/* 3654 */       coords[1] = new Coordinate(xtip, ytip);
/* 3655 */       double[][] pts = toDouble(coords);
/* 3656 */       barb.addLineSegment(pts);
/* 3657 */       if (vect.hasBackgroundMask().booleanValue()) mask.addLineSegment(pts);
/* 3658 */       currentLoc -= segmentSpacing;
/*      */     }
/*      */ 
/* 3661 */     if (vect.hasBackgroundMask().booleanValue()) {
/* 3662 */       mask.compile();
/* 3663 */       slist.add(new LineDisplayElement(mask, bgColor, vect.getLineWidth() + (float)this.deviceScale));
/*      */     }
/*      */ 
/* 3669 */     flags.compile();
/* 3670 */     FillDisplayElement fde = new FillDisplayElement(flags, vect.getColor().getAlpha());
/* 3671 */     slist.add(fde);
/*      */ 
/* 3676 */     barb.compile();
/* 3677 */     slist.add(new LineDisplayElement(barb, dspClr, vect.getLineWidth()));
/*      */ 
/* 3679 */     return slist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createWindBarbs(List<IVector> vectors)
/*      */   {
/* 3693 */     Map barbMap = new HashMap();
/* 3694 */     Map maskMap = new HashMap();
/* 3695 */     Map flagMap = new HashMap();
/*      */ 
/* 3697 */     float lineWidth = ((IVector)vectors.get(0)).getLineWidth();
/*      */     Color color;
/* 3699 */     for (IVector vect : vectors) {
/* 3700 */       color = vect.getColor();
/* 3701 */       Color bgColor = new Color(0, 0, 0);
/* 3702 */       double sfactor = this.deviceScale * vect.getSizeScale() * 10.0D;
/*      */ 
/* 3708 */       IWireframeShape barbs = (IWireframeShape)barbMap.get(color);
/* 3709 */       if (barbs == null) {
/* 3710 */         barbs = this.target.createWireframeShape(false, this.iDescriptor);
/* 3711 */         barbMap.put(color, barbs);
/*      */       }
/* 3713 */       IWireframeShape masks = null;
/* 3714 */       if (vect.hasBackgroundMask().booleanValue()) {
/* 3715 */         RGB bg = this.backgroundColor.getColor(IBackgroundColorChangedListener.BGColorMode.EDITOR);
/* 3716 */         bgColor = new Color(bg.red, bg.green, bg.blue);
/* 3717 */         masks = (IWireframeShape)maskMap.get(bgColor);
/* 3718 */         if (masks == null) {
/* 3719 */           masks = this.target.createWireframeShape(false, this.iDescriptor);
/* 3720 */           maskMap.put(bgColor, masks);
/*      */         }
/*      */       }
/* 3723 */       IShadedShape flags = (IShadedShape)flagMap.get(color);
/* 3724 */       if (flags == null) {
/* 3725 */         flags = this.target.createShadedShape(false, this.iDescriptor, false);
/* 3726 */         flagMap.put(color, flags);
/*      */       }
/*      */ 
/* 3732 */       double[] tmp = { vect.getLocation().x, vect.getLocation().y, 0.0D };
/* 3733 */       double[] start = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/* 3738 */       if (vect.getSpeed() < 0.5D) {
/* 3739 */         double[][] pts = calculateCircle(start, sfactor * 0.1D);
/* 3740 */         if (vect.hasBackgroundMask().booleanValue()) {
/* 3741 */           masks.addLineSegment(pts);
/*      */         }
/* 3743 */         barbs.addLineSegment(pts);
/*      */       }
/*      */       else
/*      */       {
/* 3751 */         int speed = (int)Math.floor(vect.getSpeed() + 2.5D);
/* 3752 */         int numflags = speed / 50;
/* 3753 */         int remainder = speed % 50;
/* 3754 */         int numbarbs = remainder / 10;
/* 3755 */         remainder %= 10;
/* 3756 */         int halfbarbs = remainder / 5;
/*      */ 
/* 3758 */         double MAX_SEGMENTS = 6.0D;
/* 3759 */         int numsegs = 2 * numflags + numbarbs + halfbarbs;
/* 3760 */         double segmentSpacing = sfactor / MAX_SEGMENTS;
/* 3761 */         double windLength = segmentSpacing * Math.max(MAX_SEGMENTS, numsegs);
/* 3762 */         double barbLength = sfactor / 3.0D;
/*      */ 
/* 3768 */         double angle = -90.0D - northOffsetAngle(vect.getLocation()) + vect.getDirection();
/* 3769 */         double[] end = new double[3];
/* 3770 */         start[0] += windLength * Math.cos(Math.toRadians(angle));
/* 3771 */         start[1] += windLength * Math.sin(Math.toRadians(angle));
/* 3772 */         end[2] = 0.0D;
/* 3773 */         barbs.addLineSegment(new double[][] { start, end });
/* 3774 */         if (vect.hasBackgroundMask().booleanValue()) masks.addLineSegment(new double[][] { start, end });
/*      */ 
/* 3780 */         LineString[] ls = toLineString(new Coordinate[] { new Coordinate(start[0], start[1]), 
/* 3781 */           new Coordinate(end[0], end[1]) });
/* 3782 */         LengthIndexedLine lil = new LengthIndexedLine(ls[0]);
/* 3783 */         double currentLoc = lil.getEndIndex();
/*      */ 
/* 3786 */         double BARB_ANGLE = 70.0D;
/* 3787 */         double barbAngle = angle + BARB_ANGLE;
/* 3788 */         if (vect.getLocation().y < 0.0D) barbAngle = angle - BARB_ANGLE;
/* 3789 */         double cosineBarbAngle = Math.cos(Math.toRadians(barbAngle));
/* 3790 */         double sineBarbAngle = Math.sin(Math.toRadians(barbAngle));
/*      */ 
/* 3795 */         for (int j = 0; j < numflags; j++) {
/* 3796 */           Coordinate[] coords = new Coordinate[4];
/* 3797 */           coords[0] = lil.extractPoint(currentLoc);
/* 3798 */           coords[1] = lil.extractPoint(currentLoc - segmentSpacing);
/* 3799 */           double xtip = coords[1].x + barbLength * cosineBarbAngle;
/* 3800 */           double ytip = coords[1].y + barbLength * sineBarbAngle;
/* 3801 */           coords[2] = new Coordinate(xtip, ytip);
/* 3802 */           coords[3] = coords[0];
/* 3803 */           LineString[] oneFlag = toLineString(coords);
/* 3804 */           flags.addPolygonPixelSpace(oneFlag, new RGB(color.getRed(), color.getGreen(), color.getBlue()));
/* 3805 */           if (vect.hasBackgroundMask().booleanValue()) masks.addLineSegment(toDouble(coords));
/* 3806 */           currentLoc -= 2.0D * segmentSpacing;
/*      */         }
/*      */ 
/* 3812 */         for (int j = 0; j < numbarbs; j++) {
/* 3813 */           Coordinate[] coords = new Coordinate[2];
/* 3814 */           coords[0] = lil.extractPoint(currentLoc);
/* 3815 */           double xtip = coords[0].x + barbLength * cosineBarbAngle;
/* 3816 */           double ytip = coords[0].y + barbLength * sineBarbAngle;
/* 3817 */           coords[1] = new Coordinate(xtip, ytip);
/* 3818 */           double[][] pts = toDouble(coords);
/* 3819 */           barbs.addLineSegment(pts);
/* 3820 */           if (vect.hasBackgroundMask().booleanValue()) masks.addLineSegment(pts);
/* 3821 */           currentLoc -= segmentSpacing;
/*      */         }
/*      */ 
/* 3827 */         for (int j = 0; j < halfbarbs; j++) {
/* 3828 */           Coordinate[] coords = new Coordinate[2];
/* 3829 */           coords[0] = lil.extractPoint(currentLoc);
/* 3830 */           double xtip = coords[0].x + 0.5D * barbLength * cosineBarbAngle;
/* 3831 */           double ytip = coords[0].y + 0.5D * barbLength * sineBarbAngle;
/* 3832 */           coords[1] = new Coordinate(xtip, ytip);
/* 3833 */           double[][] pts = toDouble(coords);
/* 3834 */           barbs.addLineSegment(pts);
/* 3835 */           if (vect.hasBackgroundMask().booleanValue()) masks.addLineSegment(pts);
/* 3836 */           currentLoc -= segmentSpacing;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3846 */     ArrayList slist = new ArrayList();
/*      */ 
/* 3852 */     for (Color color : barbMap.keySet()) {
/* 3853 */       IWireframeShape barbs = (IWireframeShape)barbMap.get(color);
/* 3854 */       barbs.compile();
/* 3855 */       slist.add(new LineDisplayElement(barbs, color, lineWidth));
/*      */     }
/* 3857 */     for (Color color : maskMap.keySet()) {
/* 3858 */       IWireframeShape masks = (IWireframeShape)maskMap.get(color);
/* 3859 */       masks.compile();
/* 3860 */       slist.add(new LineDisplayElement(masks, color, lineWidth + (float)this.deviceScale));
/*      */     }
/* 3862 */     for (Color color : flagMap.keySet()) {
/* 3863 */       IShadedShape flags = (IShadedShape)flagMap.get(color);
/* 3864 */       flags.compile();
/* 3865 */       slist.add(new FillDisplayElement(flags, color.getAlpha()));
/*      */     }
/*      */ 
/* 3868 */     return slist;
/*      */   }
/*      */ 
/*      */   private double[][] calculateCircle(double[] center, double radius)
/*      */   {
/* 3874 */     int numpts = 16;
/* 3875 */     double[][] arcpts = new double[numpts + 1][3];
/*      */ 
/* 3877 */     double inc = 360.0D / numpts;
/* 3878 */     double angle = 0.0D;
/* 3879 */     for (int j = 0; j < numpts; j++) {
/* 3880 */       arcpts[j][0] = (center[0] + radius * Math.cos(Math.toRadians(angle)));
/* 3881 */       arcpts[j][1] = (center[1] + radius * Math.sin(Math.toRadians(angle)));
/* 3882 */       angle += inc;
/*      */     }
/* 3884 */     arcpts[numpts] = arcpts[0];
/*      */ 
/* 3886 */     return arcpts;
/*      */   }
/*      */ 
/*      */   private IFont initializeFont(String fontName, float fontSize, IText.FontStyle fstyle)
/*      */   {
/* 3895 */     IFont.Style[] styles = null;
/* 3896 */     if (fstyle != null) {
/* 3897 */       switch (fstyle) {
/*      */       case BOLD_ITALIC:
/* 3899 */         styles = new IFont.Style[] { IFont.Style.BOLD };
/* 3900 */         break;
/*      */       case ITALIC:
/* 3902 */         styles = new IFont.Style[] { IFont.Style.ITALIC };
/* 3903 */         break;
/*      */       case REGULAR:
/* 3905 */         styles = new IFont.Style[] { IFont.Style.BOLD, IFont.Style.ITALIC };
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3914 */     IFont font = this.target.initializeFont(fontName, fontSize, styles);
/* 3915 */     font.setSmoothing(false);
/* 3916 */     font.setScaleFont(false);
/*      */ 
/* 3918 */     return font;
/*      */   }
/*      */ 
/*      */   public void setLayerDisplayAttr(Boolean mono, Color clr, Boolean fill)
/*      */   {
/* 3928 */     this.layerMonoColor = mono;
/* 3929 */     this.layerColor = clr;
/* 3930 */     this.layerFilled = fill;
/*      */   }
/*      */ 
/*      */   private Color[] getDisplayColors(Color[] clr)
/*      */   {
/* 3938 */     Color[] newClr = new Color[clr.length];
/*      */ 
/* 3940 */     for (int ii = 0; ii < clr.length; ii++)
/*      */     {
/* 3942 */       if ((this.layerMonoColor.booleanValue()) && (this.layerColor != null)) {
/* 3943 */         newClr[ii] = this.layerColor;
/*      */       }
/*      */       else {
/* 3946 */         newClr[ii] = clr[ii];
/*      */       }
/*      */     }
/*      */ 
/* 3950 */     return newClr;
/*      */   }
/*      */ 
/*      */   private Color getDisplayColor(Color clr)
/*      */   {
/* 3958 */     if ((this.layerMonoColor.booleanValue()) && (this.layerColor != null)) {
/* 3959 */       return this.layerColor;
/*      */     }
/*      */ 
/* 3962 */     return clr;
/*      */   }
/*      */ 
/*      */   private boolean getDisplayFillMode(Boolean filled)
/*      */   {
/* 3973 */     if (this.layerFilled.booleanValue()) {
/* 3974 */       return this.layerFilled.booleanValue();
/*      */     }
/*      */ 
/* 3977 */     return filled.booleanValue();
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(ISigmet isig, PaintProperties paintProps)
/*      */   {
/* 3990 */     if ((isig instanceof Volcano)) {
/* 3991 */       return createDisplayElements((Volcano)isig, paintProps);
/*      */     }
/*      */ 
/* 3994 */     if (((isig instanceof AbstractSigmet)) && ("CCFP_SIGMET".equals(((AbstractSigmet)isig).getPgenType()))) {
/* 3995 */       return createDisplayElements((Sigmet)isig, paintProps);
/*      */     }
/*      */ 
/* 3998 */     return createDisplayElements((AbstractSigmet)isig, paintProps);
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createDisplayElements(AbstractSigmet sigmet, PaintProperties paintProps)
/*      */   {
/* 4010 */     double widthInNautical = sigmet.getWidth() * 1852.0D;
/*      */ 
/* 4015 */     setScales(paintProps);
/*      */ 
/* 4017 */     this.elem = sigmet;
/*      */ 
/* 4019 */     Color[] dspClr = getDisplayColors(this.elem.getColors());
/* 4020 */     Color[] fillClr = dspClr; if (fillClr.length > 1) fillClr[1] = fillClr[0];
/* 4021 */     ArrayList list = new ArrayList();
/* 4022 */     this.wfs = new IWireframeShape[dspClr.length];
/* 4023 */     for (int i = 0; i < dspClr.length; i++) {
/* 4024 */       this.wfs[i] = this.target.createWireframeShape(false, this.iDescriptor);
/*      */     }
/*      */ 
/* 4027 */     Coordinate[] pts = sigmet.getLinePoints();
/*      */ 
/* 4029 */     double[][] pixels = PgenUtil.latlonToPixel(pts, (IMapDescriptor)this.iDescriptor);
/*      */ 
/* 4031 */     if (sigmet.isClosedLine().booleanValue()) {
/* 4032 */       pixels = ensureClosed(pixels);
/*      */     }
/*      */ 
/* 4035 */     double[][] smoothpts = pixels;
/*      */ 
/* 4037 */     LinePattern pattern = new LinePattern("Medium Dashed", false, null);
/* 4038 */     pattern.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 4039 */     pattern.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 4040 */     pattern.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/*      */ 
/* 4042 */     if ((pattern != null) && (pattern.needsLengthUpdate())) {
/* 4043 */       pattern = pattern.updateLength(this.screenToExtent * sigmet.getLineWidth() / (sigmet.getSizeScale() * this.deviceScale));
/*      */     }
/*      */ 
/* 4046 */     this.wfs[0].addLineSegment(smoothpts);
/*      */ 
/* 4048 */     String lineType = sigmet.getType();
/*      */ 
/* 4050 */     if ("Area".equals(lineType))
/*      */     {
/* 4052 */       this.wfs[0].addLineSegment(new Coordinate[] { pts[0], pts[(pts.length - 1)] });
/*      */ 
/* 4054 */       if ((sigmet.getPgenType().equals("VACL_SIGMET")) || (sigmet.getPgenType().equals("CCFP_SIGMET"))) {
/* 4055 */         if (sigmet.getPgenType().equals("VACL_SIGMET")) sigmet.setColors(fillClr);
/* 4056 */         list.add(createFill(smoothpts));
/*      */       }
/*      */     }
/* 4059 */     else if (lineType.contains("Line"))
/*      */     {
/* 4061 */       String[] lineString = lineType.split(":::");
/*      */ 
/* 4063 */       if ("ESOL".equalsIgnoreCase(lineString[1]))
/*      */       {
/* 4065 */         Coordinate[][] sides = SigmetInfo.getSides(pts, widthInNautical);
/* 4066 */         Coordinate[][] sidesWithArcIntsc = SigmetInfo.getSidesWithArcIntsc((IMapDescriptor)this.iDescriptor, pts, sides[0], sides[1]);
/*      */ 
/* 4068 */         double[][] sa1 = PgenUtil.latlonToPixel(sidesWithArcIntsc[0], (IMapDescriptor)this.iDescriptor);
/* 4069 */         double[][] sa2 = PgenUtil.latlonToPixel(sidesWithArcIntsc[1], (IMapDescriptor)this.iDescriptor);
/*      */ 
/* 4071 */         handleLinePattern(pattern, PgenUtil.latlonToPixel(sidesWithArcIntsc[0], (IMapDescriptor)this.iDescriptor));
/* 4072 */         handleLinePattern(pattern, PgenUtil.latlonToPixel(sidesWithArcIntsc[1], (IMapDescriptor)this.iDescriptor));
/*      */ 
/* 4074 */         if (sigmet.getPgenType().equals("VACL_SIGMET")) {
/* 4075 */           sigmet.setColors(fillClr);
/* 4076 */           list.add(createFill(SigmetInfo.getESOLArea(sidesWithArcIntsc[1], sidesWithArcIntsc[0], (IMapDescriptor)this.iDescriptor)));
/*      */         }
/*      */       }
/*      */       else {
/* 4080 */         Coordinate[] sides = SigmetInfo.getSOLCoors(pts, lineString[1], widthInNautical, (IMapDescriptor)this.iDescriptor);
/* 4081 */         handleLinePattern(pattern, PgenUtil.latlonToPixel(sides, (IMapDescriptor)this.iDescriptor));
/*      */       }
/*      */     } else {
/* 4084 */       if ("Isolated".equals(lineType))
/*      */       {
/* 4086 */         IWireframeShape arcpts = this.target.createWireframeShape(false, this.iDescriptor);
/* 4087 */         Coordinate[] locs = sigmet.getLinePoints();
/* 4088 */         ArrayList slist = new ArrayList();
/*      */ 
/* 4090 */         SymbolLocationSet centerSign = new SymbolLocationSet(null, new Color[] { dspClr[0] }, 
/* 4091 */           sigmet.getLineWidth(), 0.5D, Boolean.valueOf(false), locs, "Marker", "PLUS_SIGN");
/*      */ 
/* 4093 */         slist.addAll(createDisplayElements(centerSign, paintProps));
/*      */         try
/*      */         {
/* 4096 */           arcpts.addLineSegment(SigmetInfo.getIsolated(locs[(locs.length - 1)], widthInNautical, (IMapDescriptor)this.iDescriptor)); } catch (Throwable e) {
/* 4097 */           System.out.println("Isolated: " + e.getCause());
/*      */         }
/* 4099 */         arcpts.compile();
/* 4100 */         slist.add(new LineDisplayElement(arcpts, dspClr[0], sigmet.getLineWidth()));
/*      */ 
/* 4102 */         addTopText(sigmet, locs, dspClr, paintProps, slist);
/* 4103 */         return slist;
/* 4104 */       }if (lineType.contains("Text"))
/*      */       {
/* 4106 */         String theTxt = lineType.split(":::")[1];
/* 4107 */         Coordinate[] locs = sigmet.getLinePoints();
/* 4108 */         ArrayList slist = new ArrayList();
/*      */ 
/* 4110 */         Text display = new Text(null, "Courier", 14.0F, IText.TextJustification.CENTER, 
/* 4111 */           locs[0], 0.0D, IText.TextRotation.SCREEN_RELATIVE, sigmet.getDisplayTxt(), 
/* 4112 */           IText.FontStyle.REGULAR, dspClr[0], 0, 0, false, IText.DisplayType.BOX, 
/* 4113 */           "Text", "General Text");
/* 4114 */         slist.addAll(createDisplayElements(display, paintProps));
/*      */ 
/* 4116 */         return slist;
/*      */       }
/*      */     }
/* 4119 */     for (int k = 0; k < this.wfs.length; k++) {
/* 4120 */       this.wfs[k].compile();
/* 4121 */       LineDisplayElement lde = new LineDisplayElement(this.wfs[k], dspClr[k], sigmet.getLineWidth());
/* 4122 */       list.add(lde);
/*      */     }
/*      */ 
/* 4125 */     addTopText(sigmet, pts, dspClr, paintProps, list);
/* 4126 */     return list;
/*      */   }
/*      */ 
/*      */   private void addTopText(AbstractSigmet sigmet, Coordinate[] pts, Color[] dspClr, PaintProperties paintProps, ArrayList<IDisplayable> list)
/*      */   {
/* 4132 */     if (sigmet.isWithTopText())
/*      */     {
/* 4134 */       Text display = new Text(null, "Courier", 14.0F, IText.TextJustification.CENTER, 
/* 4135 */         SnapUtil.getNorthMostPoint(pts), 0.0D, IText.TextRotation.SCREEN_RELATIVE, new String[] { sigmet.getTopText() }, 
/* 4136 */         IText.FontStyle.REGULAR, dspClr[0], 0, 3, false, IText.DisplayType.NORMAL, 
/* 4137 */         "Text", "General Text");
/*      */ 
/* 4139 */       list.addAll(createDisplayElements(display, paintProps));
/*      */     }
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createDisplayElements(Volcano vol, PaintProperties paintProps)
/*      */   {
/* 4151 */     ArrayList slist = new ArrayList();
/*      */ 
/* 4154 */     if (VaaInfo.isNonDrawableVol(vol)) {
/* 4155 */       return slist;
/*      */     }
/* 4157 */     SymbolLocationSet centerSign = new SymbolLocationSet(null, new Color[] { Color.cyan }, 
/* 4158 */       2.0F, 1.0D, Boolean.valueOf(false), vol.getLinePoints(), "Symbol", "PRESENT_WX_201");
/*      */ 
/* 4160 */     slist.addAll(createDisplayElements(centerSign, paintProps));
/*      */ 
/* 4162 */     return slist;
/*      */   }
/*      */ 
/*      */   public ArrayList<IDisplayable> createDisplayElements(IGfa igfa, PaintProperties paintProps)
/*      */   {
/* 4170 */     Gfa gfa = (Gfa)igfa;
/* 4171 */     ArrayList list = createDisplayElements(gfa, paintProps);
/*      */ 
/* 4173 */     if (gfa.getGfaTextCoordinate() != null) {
/* 4174 */       Coordinate loc1 = gfa.getGfaTextCoordinate();
/*      */       Coordinate loc2;
/*      */       Coordinate loc2;
/* 4177 */       if (gfa.isClosedLine().booleanValue()) {
/* 4178 */         loc2 = gfa.getCentroid();
/*      */       } else {
/* 4180 */         GeometryFactory geometryFactory = new GeometryFactory();
/* 4181 */         Coordinate[] a = new Coordinate[gfa.getPoints().size()];
/* 4182 */         CoordinateArraySequence cas = new CoordinateArraySequence((Coordinate[])gfa.getPoints().toArray(a));
/* 4183 */         LineString line = new LineString(cas, geometryFactory);
/* 4184 */         cas = new CoordinateArraySequence(new Coordinate[] { loc1 });
/* 4185 */         Point point = new Point(cas, geometryFactory);
/*      */ 
/* 4187 */         Coordinate[] c = DistanceOp.nearestPoints(line, point);
/* 4188 */         loc2 = c[0];
/*      */       }
/* 4190 */       ArrayList locs = new ArrayList();
/* 4191 */       locs.add(loc1);
/* 4192 */       locs.add(loc2);
/*      */ 
/* 4198 */       String[] txtToDisplay = gfa.getString();
/* 4199 */       Text text = new Text(null, "Courier", 14.0F, IText.TextJustification.CENTER, 
/* 4200 */         loc1, 0.0D, IText.TextRotation.SCREEN_RELATIVE, txtToDisplay, 
/* 4201 */         IText.FontStyle.REGULAR, getDisplayColors(this.elem.getColors())[0], 0, 0, true, IText.DisplayType.BOX, 
/* 4202 */         "", "General Text");
/*      */ 
/* 4204 */       Line line = new Line(null, getDisplayColors(this.elem.getColors()), 1.0F, 1.0D, 
/* 4205 */         false, false, locs, 2, FillPatternList.FillPattern.SOLID, "Lines", "POINTED_ARROW");
/*      */ 
/* 4207 */       list.addAll(createDisplayElements(line, paintProps));
/* 4208 */       list.addAll(createDisplayElements(text, paintProps));
/*      */ 
/* 4211 */       if (gfa.getSymbolType() != null) {
/* 4212 */         double relativePosition = 0.0D;
/* 4213 */         for (int i = 0; i < txtToDisplay.length; i++) {
/* 4214 */           if (txtToDisplay[i].isEmpty())
/*      */           {
/* 4220 */             relativePosition = i + 0.5D - txtToDisplay.length / 2.0D;
/* 4221 */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 4227 */         IFont font = initializeFont("Courier", 14.0F, IText.FontStyle.REGULAR);
/* 4228 */         Rectangle2D bounds = this.target.getStringBounds(font, "Xy");
/* 4229 */         bounds = new Rectangle2D.Double(0.0D, 0.0D, bounds.getWidth() / 2.0D, bounds.getHeight());
/* 4230 */         font.dispose();
/* 4231 */         double vertRatio = paintProps.getView().getExtent().getHeight() / paintProps.getCanvasBounds().height;
/*      */ 
/* 4236 */         double[] worldPixel = { loc1.x, loc1.y, 0.0D };
/* 4237 */         double[] pixel = this.iDescriptor.worldToPixel(worldPixel);
/*      */ 
/* 4240 */         double[] locSym = this.iDescriptor.pixelToWorld(new double[] { pixel[0], 
/* 4241 */           pixel[1] + vertRatio * relativePosition * bounds.getHeight() - 1.0D, 0.0D });
/*      */ 
/* 4243 */         loc1 = new Coordinate(locSym[0], locSym[1]);
/*      */ 
/* 4245 */         Symbol sym = new Symbol(null, getDisplayColors(this.elem.getColors()), 1.5F, 1.0D, Boolean.valueOf(false), loc1, 
/* 4246 */           "Symbol", gfa.getSymbolType());
/* 4247 */         list.addAll(createDisplayElements(sym, paintProps));
/*      */       }
/*      */     }
/*      */ 
/* 4251 */     return list;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> createDisplayElements(Sigmet sigmet, PaintProperties paintProps)
/*      */   {
/*      */     ArrayList list;
/*      */     ArrayList list;
/* 4263 */     if (!"Area".equalsIgnoreCase(sigmet.getType()))
/*      */     {
/* 4265 */       Line line = new Line(null, getDisplayColors(sigmet.getColors()), sigmet.getLineWidth(), 1.0D, false, false, 
/* 4266 */         sigmet.getPoints(), 0, FillPatternList.FillPattern.SOLID, "Lines", sigmet.getPatternName());
/*      */ 
/* 4268 */       list = createDisplayElements(line, paintProps);
/*      */     }
/*      */     else
/*      */     {
/* 4272 */       list = createDisplayElements(sigmet, paintProps);
/*      */ 
/* 4275 */       String spd = sigmet.getEditableAttrPhenomSpeed();
/* 4276 */       if ((spd != null) && (!spd.trim().equals("0")))
/*      */       {
/* 4278 */         String dir = sigmet.getEditableAttrPhenomDirection();
/* 4279 */         Coordinate[] coors = sigmet.getLinePoints();
/*      */ 
/* 4281 */         ArrayList spdCoors = CcfpInfo.getDirMostPts(dir, coors, (IMapDescriptor)this.iDescriptor);
/* 4282 */         if (spdCoors.size() > 1)
/*      */         {
/* 4284 */           Vector vv = new Vector();
/* 4285 */           vv.setPgenCategory("Vector");
/* 4286 */           vv.setPgenType("Arrow");
/* 4287 */           double vDir = vv.vectorDirection((Coordinate)spdCoors.get(0), (Coordinate)spdCoors.get(1));
/*      */ 
/* 4289 */           Vector v = 
/* 4290 */             new Vector(
/* 4291 */             null, 
/* 4292 */             getDisplayColors(sigmet.getColors()), 
/* 4293 */             1.0F, 
/* 4294 */             1.0D, 
/* 4295 */             Boolean.valueOf(false), 
/* 4296 */             (Coordinate)spdCoors.get(0), 
/* 4297 */             IVector.VectorType.ARROW, 
/* 4298 */             10.0D, 
/* 4299 */             vDir, 
/* 4300 */             1.0D, 
/* 4301 */             false, 
/* 4302 */             "Vector", 
/* 4303 */             "Arrow");
/*      */ 
/* 4305 */           Text spdTxt = new Text(null, "Courier", 14.0F, IText.TextJustification.LEFT_JUSTIFY, 
/* 4306 */             getCcfpTxtPts(v), 
/* 4307 */             0.0D, IText.TextRotation.SCREEN_RELATIVE, 
/* 4308 */             new String[] { sigmet.getEditableAttrPhenomSpeed() }, 
/* 4309 */             IText.FontStyle.REGULAR, getDisplayColors(sigmet.getColors())[0], 0, 3, false, IText.DisplayType.NORMAL, 
/* 4310 */             "Text", "General Text");
/*      */ 
/* 4312 */           list.addAll(createDisplayElements(v, paintProps));
/* 4313 */           list.addAll(createDisplayElements(spdTxt, paintProps));
/*      */         }
/*      */       }
/*      */ 
/* 4317 */       if (!CcfpInfo.isTxtArrwExst(sigmet))
/*      */       {
/* 4319 */         CcfpInfo.calAziDist4TxtArrw(sigmet);
/*      */       }
/*      */ 
/* 4324 */       if (CcfpInfo.isTxtArrwExst(sigmet))
/*      */       {
/* 4326 */         String loct = sigmet.getEditableAttrFreeText();
/* 4327 */         if ((loct == null) || (loct.isEmpty()) || (!loct.contains(":::"))) return list;
/* 4328 */         double azi = Double.parseDouble(loct.split(":::")[0]);
/* 4329 */         double dis = Double.parseDouble(loct.split(":::")[1]);
/*      */ 
/* 4331 */         GeodeticCalculator gc = 
/* 4332 */           new GeodeticCalculator(PgenSession.getInstance().getPgenResource().getCoordinateReferenceSystem());
/*      */ 
/* 4335 */         ArrayList locs = new ArrayList();
/*      */ 
/* 4337 */         Coordinate loc2 = CcfpInfo.getSigCentroid2(sigmet, (IMapDescriptor)this.iDescriptor);
/*      */ 
/* 4339 */         Point2D pts = null;
/*      */         try
/*      */         {
/* 4342 */           gc.setStartingGeographicPoint(loc2.x, loc2.y);
/* 4343 */           gc.setDirection(azi, dis);
/* 4344 */           pts = gc.getDestinationGeographicPoint();
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*      */         }
/* 4349 */         if (pts == null) return list;
/*      */ 
/* 4351 */         Coordinate loc1 = new Coordinate(pts.getX(), pts.getY());
/* 4352 */         locs.add(loc1);
/* 4353 */         locs.add(loc2);
/*      */ 
/* 4355 */         Text display = new Text(null, "Courier", 16.0F, IText.TextJustification.LEFT_JUSTIFY, 
/* 4356 */           loc1, 0.0D, IText.TextRotation.SCREEN_RELATIVE, CcfpInfo.getCcftTxt(sigmet), 
/* 4357 */           IText.FontStyle.REGULAR, getDisplayColors(this.elem.getColors())[0], 0, 0, true, IText.DisplayType.BOX, 
/* 4358 */           "Text", "General Text");
/*      */ 
/* 4361 */         if (!CcfpInfo.isPtsInArea(sigmet, loc1)) {
/* 4362 */           Line line = new Line(null, new Color[] { Color.orange }, 1.0F, 1.0D, 
/* 4363 */             false, false, locs, 2, FillPatternList.FillPattern.SOLID, "Lines", "POINTED_ARROW");
/* 4364 */           list.addAll(createDisplayElements(line, paintProps));
/*      */         }
/*      */ 
/* 4367 */         list.addAll(createDisplayElements(display, paintProps));
/*      */       }
/*      */     }
/*      */ 
/* 4371 */     return list;
/*      */   }
/*      */ 
/*      */   private Coordinate getCcfpTxtPts(IVector vect)
/*      */   {
/* 4380 */     double sfactor = this.deviceScale * vect.getSizeScale();
/*      */ 
/* 4382 */     double[] tmp = { vect.getLocation().x, vect.getLocation().y, 0.0D };
/* 4383 */     double[] start = this.iDescriptor.worldToPixel(tmp);
/*      */ 
/* 4385 */     double speed = 9.0D;
/* 4386 */     if (!vect.hasDirectionOnly()) speed = vect.getSpeed();
/* 4387 */     double arrowLength = sfactor * (speed + 4.0D);
/*      */ 
/* 4389 */     double angle = 90.0D - northOffsetAngle(vect.getLocation()) + vect.getDirection();
/*      */ 
/* 4391 */     double[] end = new double[3];
/* 4392 */     start[0] += arrowLength * Math.cos(Math.toRadians(angle));
/* 4393 */     start[1] += arrowLength * Math.sin(Math.toRadians(angle));
/* 4394 */     end[2] = 0.0D;
/*      */ 
/* 4396 */     double[] c = this.iDescriptor.pixelToWorld(end);
/* 4397 */     return new Coordinate(c[0], c[1]);
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> adjustContourLineLabels(IMultiPoint de, PaintProperties paintProps, double[][] smoothpts)
/*      */   {
/* 4407 */     ArrayList dlist = new ArrayList();
/*      */ 
/* 4409 */     if (((de instanceof Line)) && ((((Line)de).getParent() instanceof ContourLine)))
/*      */     {
/* 4411 */       ContourLine cline = (ContourLine)((Line)this.elem).getParent();
/*      */ 
/* 4413 */       boolean lineClosed = cline.getLine().isClosedLine().booleanValue();
/*      */ 
/* 4418 */       double minx = paintProps.getView().getExtent().getMinX();
/* 4419 */       double miny = paintProps.getView().getExtent().getMinY();
/* 4420 */       double maxx = paintProps.getView().getExtent().getMaxX();
/* 4421 */       double maxy = paintProps.getView().getExtent().getMaxY();
/*      */ 
/* 4423 */       double dx = Math.abs(maxx - minx);
/* 4424 */       double dy = Math.abs(maxy - miny);
/* 4425 */       double dd = Math.min(dx, dy);
/*      */ 
/* 4427 */       double ratio = 0.02D;
/* 4428 */       double offset = dd * ratio;
/*      */ 
/* 4430 */       minx += offset;
/* 4431 */       miny += offset;
/* 4432 */       maxx -= offset;
/* 4433 */       maxy -= offset;
/*      */ 
/* 4435 */       double[][] visiblePts = new double[smoothpts.length][smoothpts[0].length];
/* 4436 */       int actualLength = 0;
/*      */ 
/* 4438 */       for (double[] dl : smoothpts) {
/* 4439 */         if ((dl[0] > minx) && (dl[0] < maxx) && (dl[1] > miny) && (dl[1] < maxy)) {
/* 4440 */           visiblePts[actualLength][0] = dl[0];
/* 4441 */           visiblePts[actualLength][1] = dl[1];
/* 4442 */           actualLength++;
/*      */         }
/*      */       }
/*      */ 
/* 4446 */       int numText2Draw = Math.min(actualLength, cline.getNumOfLabels());
/* 4447 */       ArrayList txtPositions = new ArrayList();
/*      */ 
/* 4453 */       if (actualLength < cline.getNumOfLabels())
/*      */       {
/* 4455 */         numText2Draw = Math.min(3, numText2Draw);
/*      */ 
/* 4457 */         if (numText2Draw > 0) {
/* 4458 */           if (numText2Draw == 1) {
/* 4459 */             double xx = visiblePts[(actualLength / 2)][0] - offset / 4.0D;
/* 4460 */             double yy = visiblePts[(actualLength / 2)][1];
/* 4461 */             txtPositions.add(new Coordinate(xx, yy));
/*      */           }
/* 4463 */           else if (numText2Draw == 2) {
/* 4464 */             double xx = visiblePts[0][0] - offset / 4.0D;
/* 4465 */             double yy = visiblePts[0][1];
/* 4466 */             txtPositions.add(new Coordinate(xx, yy));
/*      */ 
/* 4468 */             if (lineClosed) {
/* 4469 */               xx = visiblePts[(actualLength / 2)][0] + offset / 4.0D;
/* 4470 */               yy = visiblePts[(actualLength / 2)][1];
/*      */             }
/*      */             else {
/* 4473 */               xx = visiblePts[(actualLength - 1)][0] + offset / 4.0D;
/* 4474 */               yy = visiblePts[(actualLength - 1)][1];
/*      */             }
/* 4476 */             txtPositions.add(new Coordinate(xx, yy));
/*      */           }
/*      */           else
/*      */           {
/* 4480 */             double xx = visiblePts[0][0] - offset / 4.0D;
/* 4481 */             double yy = visiblePts[0][1];
/* 4482 */             txtPositions.add(new Coordinate(xx, yy));
/*      */ 
/* 4484 */             if (lineClosed) {
/* 4485 */               int intv = actualLength / numText2Draw;
/* 4486 */               xx = visiblePts[intv][0] + offset / 4.0D;
/* 4487 */               yy = visiblePts[intv][1];
/* 4488 */               txtPositions.add(new Coordinate(xx, yy));
/*      */ 
/* 4490 */               xx = visiblePts[(intv * 2)][0] + offset / 4.0D;
/* 4491 */               yy = visiblePts[(intv * 2)][1];
/* 4492 */               txtPositions.add(new Coordinate(xx, yy));
/*      */             }
/*      */             else {
/* 4495 */               xx = visiblePts[(actualLength / 2)][0] + offset / 4.0D;
/* 4496 */               yy = visiblePts[(actualLength / 2)][1];
/* 4497 */               txtPositions.add(new Coordinate(xx, yy));
/*      */ 
/* 4499 */               xx = visiblePts[(actualLength - 1)][0] + offset / 4.0D;
/* 4500 */               yy = visiblePts[(actualLength - 1)][1];
/* 4501 */               txtPositions.add(new Coordinate(xx, yy));
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/* 4508 */       else if (numText2Draw > 0) {
/* 4509 */         if (cline.getNumOfLabels() == 1) {
/* 4510 */           double xx = visiblePts[(actualLength / 2)][0] - offset / 4.0D;
/* 4511 */           double yy = visiblePts[(actualLength / 2)][1];
/* 4512 */           txtPositions.add(new Coordinate(xx, yy));
/*      */         }
/* 4514 */         else if (cline.getNumOfLabels() == 2) {
/* 4515 */           double xx = visiblePts[0][0] - offset / 4.0D;
/* 4516 */           double yy = visiblePts[0][1];
/* 4517 */           txtPositions.add(new Coordinate(xx, yy));
/*      */ 
/* 4519 */           if (lineClosed) {
/* 4520 */             xx = visiblePts[(actualLength / 2)][0] + offset / 4.0D;
/* 4521 */             yy = visiblePts[(actualLength / 2)][1];
/*      */           }
/*      */           else {
/* 4524 */             xx = visiblePts[(actualLength - 1)][0] + offset / 4.0D;
/* 4525 */             yy = visiblePts[(actualLength - 1)][1];
/*      */           }
/* 4527 */           txtPositions.add(new Coordinate(xx, yy));
/*      */         }
/* 4529 */         else if (cline.getNumOfLabels() == 3)
/*      */         {
/* 4531 */           double xx = visiblePts[0][0] - offset / 4.0D;
/* 4532 */           double yy = visiblePts[0][1];
/* 4533 */           txtPositions.add(new Coordinate(xx, yy));
/*      */ 
/* 4535 */           if (lineClosed) {
/* 4536 */             int intv = actualLength / numText2Draw;
/* 4537 */             xx = visiblePts[intv][0] + offset / 4.0D;
/* 4538 */             yy = visiblePts[intv][1];
/* 4539 */             txtPositions.add(new Coordinate(xx, yy));
/*      */ 
/* 4541 */             xx = visiblePts[(intv * 2)][0] + offset / 4.0D;
/* 4542 */             yy = visiblePts[(intv * 2)][1];
/* 4543 */             txtPositions.add(new Coordinate(xx, yy));
/*      */           }
/*      */           else {
/* 4546 */             xx = visiblePts[(actualLength / 2)][0] + offset / 4.0D;
/* 4547 */             yy = visiblePts[(actualLength / 2)][1];
/* 4548 */             txtPositions.add(new Coordinate(xx, yy));
/*      */ 
/* 4550 */             xx = visiblePts[(actualLength - 1)][0] + offset / 4.0D;
/* 4551 */             yy = visiblePts[(actualLength - 1)][1];
/* 4552 */             txtPositions.add(new Coordinate(xx, yy));
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*      */           int interval;
/*      */           int interval;
/* 4557 */           if (lineClosed) {
/* 4558 */             interval = actualLength / numText2Draw;
/*      */           }
/*      */           else {
/* 4561 */             interval = actualLength / (numText2Draw - 1);
/*      */           }
/*      */ 
/* 4564 */           int nadd = numText2Draw - 1;
/* 4565 */           if (lineClosed) nadd = numText2Draw;
/*      */ 
/* 4567 */           for (int jj = 0; jj < nadd; jj++)
/*      */           {
/*      */             double xx;
/*      */             double xx;
/* 4568 */             if (jj == 0) {
/* 4569 */               xx = visiblePts[(jj * interval)][0] - offset / 4.0D;
/*      */             }
/*      */             else {
/* 4572 */               xx = visiblePts[(jj * interval)][0] + offset / 4.0D;
/*      */             }
/* 4574 */             double yy = visiblePts[(jj * interval)][1];
/* 4575 */             txtPositions.add(new Coordinate(xx, yy));
/*      */           }
/*      */ 
/* 4578 */           if (!lineClosed) {
/* 4579 */             double xx = visiblePts[(actualLength - 1)][0] + offset / 4.0D;
/* 4580 */             double yy = visiblePts[(actualLength - 1)][1];
/* 4581 */             txtPositions.add(new Coordinate(xx, yy));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 4592 */       double[] loc = { 0.0D, 0.0D, 0.0D };
/* 4593 */       for (int kk = 0; kk < numText2Draw; kk++) {
/* 4594 */         Text txt = (Text)cline.getLabels().get(kk);
/* 4595 */         loc[0] = ((Coordinate)txtPositions.get(kk)).x;
/* 4596 */         loc[1] = ((Coordinate)txtPositions.get(kk)).y;
/*      */ 
/* 4598 */         double[] tps = this.iDescriptor.pixelToWorld(loc);
/* 4599 */         if ((txt.getAuto() != null) && (txt.getAuto().booleanValue())) {
/* 4600 */           txt.setLocationOnly(new Coordinate(tps[0], tps[1]));
/*      */         }
/*      */ 
/* 4603 */         txt.setParent(null);
/* 4604 */         dlist.addAll(createDisplayElements(txt, paintProps));
/* 4605 */         txt.setParent(cline);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4610 */     return dlist;
/*      */   }
/*      */ 
/*      */   private ArrayList<IDisplayable> adjustContourCircleLabel(IArc arc, PaintProperties paintProps, double[][] smoothpts)
/*      */   {
/* 4620 */     ArrayList dlist = new ArrayList();
/*      */ 
/* 4622 */     AbstractDrawableComponent parent = ((DrawableElement)arc).getParent();
/*      */ 
/* 4624 */     if ((parent instanceof ContourCircle))
/*      */     {
/* 4626 */       Text labelText = ((ContourCircle)parent).getLabel();
/*      */ 
/* 4628 */       if ((labelText.getAuto() != null) && (labelText.getAuto().booleanValue()))
/*      */       {
/* 4632 */         double minx = paintProps.getView().getExtent().getMinX();
/* 4633 */         double miny = paintProps.getView().getExtent().getMinY();
/* 4634 */         double maxx = paintProps.getView().getExtent().getMaxX();
/* 4635 */         double maxy = paintProps.getView().getExtent().getMaxY();
/*      */ 
/* 4637 */         double dx = Math.abs(maxx - minx);
/* 4638 */         double dy = Math.abs(maxy - miny);
/* 4639 */         double dd = Math.min(dx, dy);
/*      */ 
/* 4641 */         double ratio = 0.02D;
/* 4642 */         double offset = dd * ratio;
/*      */ 
/* 4644 */         minx += offset;
/* 4645 */         miny += offset;
/* 4646 */         maxx -= offset;
/* 4647 */         maxy -= offset;
/*      */ 
/* 4649 */         double[][] visiblePts = new double[smoothpts.length][smoothpts[0].length];
/* 4650 */         int actualLength = 0;
/*      */ 
/* 4652 */         for (double[] dl : smoothpts) {
/* 4653 */           if ((dl[0] > minx) && (dl[0] < maxx) && 
/* 4654 */             (dl[1] > miny) && (dl[1] < maxy)) {
/* 4655 */             visiblePts[actualLength][0] = dl[0];
/* 4656 */             visiblePts[actualLength][1] = dl[1];
/* 4657 */             actualLength++;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 4664 */         int pp = Math.max(actualLength / 2, actualLength - 5);
/* 4665 */         double[] loc = this.iDescriptor.pixelToWorld(new double[] { visiblePts[pp][0] - offset / 2.0D, visiblePts[pp][1], 0.0D });
/* 4666 */         Coordinate loc1 = new Coordinate(loc[0], loc[1]);
/* 4667 */         labelText.setLocationOnly(loc1);
/*      */       }
/*      */ 
/* 4673 */       labelText.setParent(null);
/* 4674 */       dlist.addAll(createDisplayElements(labelText, paintProps));
/* 4675 */       labelText.setParent(parent);
/*      */     }
/*      */ 
/* 4679 */     return dlist;
/*      */   }
/*      */ 
/*      */   private void addCcfpSpeed(ArrayList<IDisplayable> list, PaintProperties paintProps, DECollection ccfp)
/*      */   {
/* 4684 */     if ((list == null) || (paintProps == null)) {
/* 4685 */       return;
/*      */     }
/* 4687 */     String[] spdDir = ccfp.getCollectionName().split(":::");
/*      */ 
/* 4689 */     if (!"Area".equals(spdDir[(spdDir.length - 1)])) return;
/*      */ 
/* 4691 */     String spd = spdDir[1];
/* 4692 */     if ((spd != null) && (!spd.trim().equals("0")))
/*      */     {
/* 4694 */       String dir = spdDir[2];
/* 4695 */       Coordinate[] coors = (Coordinate[])ccfp.getPrimaryDE().getPoints().toArray(new Coordinate[0]);
/*      */ 
/* 4697 */       ArrayList spdCoors = CcfpInfo.getDirMostPts(dir, coors, (IMapDescriptor)this.iDescriptor);
/* 4698 */       if (spdCoors.size() > 1)
/*      */       {
/* 4700 */         Vector vv = new Vector();
/* 4701 */         vv.setPgenCategory("Vector");
/* 4702 */         vv.setPgenType("Arrow");
/* 4703 */         double vDir = vv.vectorDirection((Coordinate)spdCoors.get(0), (Coordinate)spdCoors.get(1));
/*      */ 
/* 4705 */         Vector v = 
/* 4706 */           new Vector(
/* 4707 */           null, 
/* 4708 */           getDisplayColors(ccfp.getPrimaryDE().getColors()), 
/* 4709 */           2.0F, 
/* 4710 */           1.5D, 
/* 4711 */           Boolean.valueOf(false), 
/* 4712 */           (Coordinate)spdCoors.get(0), 
/* 4713 */           IVector.VectorType.ARROW, 
/* 4714 */           10.0D, 
/* 4715 */           vDir, 
/* 4716 */           1.0D, 
/* 4717 */           false, 
/* 4718 */           "Vector", 
/* 4719 */           "Arrow");
/*      */ 
/* 4721 */         Text spdTxt = new Text(null, "Courier", 14.0F, IText.TextJustification.CENTER, 
/* 4722 */           getCcfpTxtPts(v), 
/* 4723 */           0.0D, IText.TextRotation.SCREEN_RELATIVE, 
/* 4724 */           new String[] { spd }, 
/* 4725 */           IText.FontStyle.REGULAR, 
/* 4726 */           getDisplayColors(ccfp.getPrimaryDE().getColors())[0], 
/* 4727 */           0, 0, false, IText.DisplayType.NORMAL, "Text", "General Text");
/*      */ 
/* 4729 */         list.addAll(createDisplayElements(v, paintProps));
/* 4730 */         list.addAll(createDisplayElements(spdTxt, paintProps));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private Coordinate calculateDestinationPointMap(Coordinate startPt, double angle, double distance)
/*      */   {
/* 4743 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/* 4744 */     gc.setStartingGeographicPoint(startPt.x, startPt.y);
/* 4745 */     gc.setDirection(angle, distance * 1852.0D);
/*      */ 
/* 4747 */     Point2D pt1 = gc.getDestinationGeographicPoint();
/* 4748 */     return new Coordinate(pt1.getX(), pt1.getY());
/*      */   }
/*      */ 
/*      */   private Coordinate getPointOnArc(Arc arc, double angle)
/*      */   {
/* 4761 */     double[] tmp = { arc.getCenterPoint().x, arc.getCenterPoint().y, 0.0D };
/* 4762 */     double[] center = this.iDescriptor.worldToPixel(tmp);
/* 4763 */     double[] tmp2 = { arc.getCircumferencePoint().x, arc.getCircumferencePoint().y, 0.0D };
/* 4764 */     double[] circum = this.iDescriptor.worldToPixel(tmp2);
/*      */ 
/* 4766 */     double radius = Math.sqrt((center[0] - circum[0]) * (center[0] - circum[0]) + 
/* 4767 */       (center[1] - circum[1]) * (center[1] - circum[1]));
/*      */ 
/* 4772 */     double axisAngle = 90.0D + Math.toDegrees(Math.atan2(circum[1] - center[1], circum[0] - center[0]));
/* 4773 */     angle += axisAngle;
/*      */ 
/* 4775 */     double thisSine = Math.sin(Math.toRadians(angle));
/* 4776 */     double thisCosine = Math.cos(Math.toRadians(angle));
/*      */ 
/* 4778 */     double[] pt = new double[2];
/*      */ 
/* 4781 */     center[0] += radius * thisCosine;
/* 4782 */     center[1] += radius * thisSine;
/*      */ 
/* 4784 */     double[] mapPt = this.iDescriptor.pixelToWorld(pt);
/*      */ 
/* 4786 */     return new Coordinate(mapPt[0], mapPt[1]);
/*      */   }
/*      */ 
/*      */   private Line getWindQuatroLine(Coordinate pt1, Coordinate pt2, Color color)
/*      */   {
/* 4797 */     ArrayList pts = new ArrayList();
/* 4798 */     pts.add(pt1);
/* 4799 */     pts.add(pt2);
/*      */ 
/* 4801 */     return new Line(null, new Color[] { color }, 1.5F, 0.5D, false, 
/* 4802 */       false, pts, 0, null, "Lines", "LINE_SOLID");
/*      */   }
/*      */ 
/*      */   public void reset() {
/* 4806 */     if (this.ss != null) {
/* 4807 */       this.ss.reset();
/*      */     }
/* 4809 */     if (this.sym != null) {
/* 4810 */       this.sym.reset();
/*      */     }
/* 4812 */     if (this.wfs != null)
/* 4813 */       for (IWireframeShape shape : this.wfs)
/* 4814 */         if (shape != null)
/* 4815 */           shape.reset();
/*      */   }
/*      */ 
/*      */   private static enum ScaleType
/*      */   {
/*  144 */     SCALE_ALL_SEGMENTS, SCALE_BLANK_LINE_ONLY;
/*      */   }
/*      */ 
/*      */   class SymbolImageCallback
/*      */     implements IRenderedImageCallback
/*      */   {
/*      */     private String patternName;
/*      */     private double scale;
/*      */     private float lineWidth;
/*      */     private boolean mask;
/*      */     private Color color;
/*      */ 
/*      */     public SymbolImageCallback(String patternName, double scale, float lineWidth, boolean mask, Color color)
/*      */     {
/*  195 */       this.patternName = patternName;
/*  196 */       this.scale = scale;
/*  197 */       this.lineWidth = lineWidth;
/*  198 */       this.mask = mask;
/*  199 */       this.color = color;
/*      */     }
/*      */ 
/*      */     public RenderedImage getImage() throws VizException
/*      */     {
/*  204 */       return SymbolImageUtil.createBufferedImage(this.patternName, this.scale, this.lineWidth, 
/*  205 */         this.mask, this.color);
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.DisplayElementFactory
 * JD-Core Version:    0.6.2
 */