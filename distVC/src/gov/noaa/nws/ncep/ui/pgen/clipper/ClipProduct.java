/*     */ package gov.noaa.nws.ncep.ui.pgen.clipper;
/*     */ 
/*     */ import com.raytheon.uf.common.geospatial.MapUtil;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.KinkLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.geotools.geometry.jts.JTS;
/*     */ import org.opengis.referencing.FactoryException;
/*     */ import org.opengis.referencing.crs.CoordinateReferenceSystem;
/*     */ import org.opengis.referencing.operation.MathTransform;
/*     */ import org.opengis.referencing.operation.NoninvertibleTransformException;
/*     */ import org.opengis.referencing.operation.TransformException;
/*     */ 
/*     */ public class ClipProduct
/*     */ {
/*  68 */   private GeometryFactory geometryFactory = new GeometryFactory();
/*     */   private Polygon boundsPoly;
/*  74 */   private final double PRECISION = 1.E-09D;
/*     */ 
/*  79 */   public static boolean keep = true;
/*     */ 
/*     */   public ClipProduct(Polygon bounds, boolean keepInside)
/*     */   {
/*  83 */     this.boundsPoly = bounds;
/*  84 */     keep = keepInside;
/*     */   }
/*     */ 
/*     */   public void clipProduct(Product p)
/*     */   {
/* 107 */     if (this.boundsPoly == null) return;
/*     */ 
/* 109 */     for (Layer layer : p.getLayers()) {
/* 110 */       List before = layer.getDrawables();
/* 111 */       List after = new ArrayList();
/* 112 */       boolean changed = false;
/* 113 */       for (AbstractDrawableComponent adc : before) {
/* 114 */         List c = clipDrawableComponent(adc);
/* 115 */         if ((c != null) && (!c.isEmpty())) {
/* 116 */           after.addAll(c);
/*     */         }
/* 118 */         changed = true;
/*     */       }
/* 120 */       if (changed)
/*     */       {
/* 123 */         layer.setDrawables(after);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<AbstractDrawableComponent> clipDrawableComponents(List<AbstractDrawableComponent> elements)
/*     */   {
/* 130 */     List after = new ArrayList();
/* 131 */     for (AbstractDrawableComponent adc : elements) {
/* 132 */       List c = clipDrawableComponent(adc);
/* 133 */       if ((c != null) && (!c.isEmpty())) {
/* 134 */         after.addAll(c);
/*     */       }
/*     */       else {
/* 137 */         after.add(adc.copy());
/*     */       }
/*     */     }
/* 140 */     return after;
/*     */   }
/*     */ 
/*     */   private List<AbstractDrawableComponent> clipDrawableComponent(AbstractDrawableComponent adc)
/*     */   {
/* 151 */     List ret = null;
/* 152 */     if ((adc instanceof SinglePointElement)) {
/* 153 */       ret = clipSinglePointElement(adc);
/*     */     }
/* 155 */     else if ((adc instanceof MultiPointElement))
/*     */     {
/* 160 */       AbstractDrawableComponent adcOrig = null;
/* 161 */       if ((adc instanceof Arc))
/*     */       {
/* 164 */         adcOrig = adc.copy();
/* 165 */         adc = convertArcToLine((Arc)adc);
/*     */       }
/* 172 */       else if ((adc instanceof Line)) {
/* 173 */         if (((Line)adc).isClosedLine().booleanValue())
/* 174 */           adc.getPoints().add((Coordinate)adc.getPoints().get(0));
/*     */       }
/* 176 */       else if ((adc instanceof Gfa)) {
/* 177 */         if (((Gfa)adc).isClosedLine().booleanValue())
/* 178 */           adc.getPoints().add((Coordinate)adc.getPoints().get(0));
/*     */       }
/* 180 */       else if (((adc instanceof KinkLine)) && 
/* 181 */         (((KinkLine)adc).isClosedLine().booleanValue())) {
/* 182 */         adc.getPoints().add((Coordinate)adc.getPoints().get(0));
/*     */       }
/*     */ 
/* 185 */       ret = clipMultiPointElement(adc);
/*     */ 
/* 187 */       if (ret == null) {
/* 188 */         return null;
/*     */       }
/*     */ 
/* 193 */       for (int i = 0; i < ret.size(); i++) {
/* 194 */         if (((AbstractDrawableComponent)ret.get(i)).getPgenCategory().equalsIgnoreCase("Arc")) {
/* 195 */           Arc newArc = (Arc)((AbstractDrawableComponent)ret.get(i)).getPrimaryDE();
/*     */ 
/* 197 */           Coordinate center = ((Arc)adcOrig).getCenterPoint();
/* 198 */           Coordinate circum = ((Arc)adcOrig).getCircumferencePoint();
/* 199 */           Coordinate startPt = (Coordinate)newArc.getPoints().get(0);
/* 200 */           Coordinate endPt = (Coordinate)newArc.getPoints().get(newArc.getPoints().size() - 1);
/*     */ 
/* 203 */           Coordinate centerProjection = new Coordinate();
/* 204 */           Coordinate circumProjection = new Coordinate();
/* 205 */           Coordinate startPtProjection = new Coordinate();
/* 206 */           Coordinate endPtProjection = new Coordinate();
/*     */ 
/* 208 */           CoordinateReferenceSystem crs = MapUtil.constructStereographic(6371229.0D, 
/* 209 */             6371229.0D, center.y, center.x);
/* 210 */           MathTransform fromLatLon = null;
/* 211 */           MathTransform toLatLon = null;
/*     */           try
/*     */           {
/* 214 */             fromLatLon = MapUtil.getTransformFromLatLon(crs);
/*     */           }
/*     */           catch (FactoryException e1) {
/* 217 */             e1.printStackTrace();
/*     */           }
/*     */ 
/* 220 */           if (fromLatLon != null) {
/*     */             try {
/* 222 */               toLatLon = fromLatLon.inverse();
/*     */             }
/*     */             catch (NoninvertibleTransformException e) {
/* 225 */               e.printStackTrace();
/*     */             }
/*     */           }
/*     */           try
/*     */           {
/* 230 */             JTS.transform(center, centerProjection, fromLatLon);
/* 231 */             JTS.transform(circum, circumProjection, fromLatLon);
/* 232 */             JTS.transform(startPt, startPtProjection, fromLatLon);
/* 233 */             JTS.transform(endPt, endPtProjection, fromLatLon);
/*     */           }
/*     */           catch (TransformException e) {
/* 236 */             e.printStackTrace();
/*     */           }
/*     */ 
/* 243 */           double x0 = centerProjection.x;
/* 244 */           double y0 = centerProjection.y;
/* 245 */           double x1 = circumProjection.x;
/* 246 */           double y1 = circumProjection.y;
/* 247 */           double xs = startPtProjection.x;
/* 248 */           double ys = startPtProjection.y;
/* 249 */           double xe = endPtProjection.x;
/* 250 */           double ye = endPtProjection.y;
/*     */ 
/* 252 */           double radius = Math.sqrt(Math.pow(y1 - y0, 2.0D) + Math.pow(x1 - x0, 2.0D));
/* 253 */           double radius2 = radius * newArc.getAxisRatio();
/*     */ 
/* 256 */           double theta = Math.toDegrees(Math.atan2(y1 - y0, x1 - x0));
/* 257 */           double beta = Math.atan2(y1 - y0, x1 - x0);
/* 258 */           double sinbeta = Math.sin(beta);
/* 259 */           double cosbeta = Math.cos(beta);
/*     */ 
/* 268 */           double cosalpha = 0.0D;
/*     */ 
/* 270 */           cosalpha = (ys * sinbeta + xs * cosbeta) / radius;
/* 271 */           double startAngle = Math.toDegrees(Math.acos(cosalpha));
/* 272 */           cosalpha = (ye * sinbeta + xe * cosbeta) / radius;
/* 273 */           double endAngle = Math.toDegrees(Math.acos(cosalpha));
/*     */ 
/* 276 */           double xTest = radius * Math.cos(Math.toRadians(-startAngle)) * cosbeta - radius2 * Math.sin(Math.toRadians(-startAngle)) * sinbeta;
/* 277 */           double yTest = radius * Math.cos(Math.toRadians(-startAngle)) * sinbeta + radius2 * Math.sin(Math.toRadians(-startAngle)) * cosbeta;
/* 278 */           if ((Math.abs((xs - xTest) / xs) > 0.005D) || (Math.abs((ys - yTest) / ys) > 0.005D)) {
/* 279 */             startAngle = 360.0D - startAngle;
/*     */           }
/* 281 */           xTest = radius * Math.cos(Math.toRadians(-endAngle)) * cosbeta - radius2 * Math.sin(Math.toRadians(-endAngle)) * sinbeta;
/* 282 */           yTest = radius * Math.cos(Math.toRadians(-endAngle)) * sinbeta + radius2 * Math.sin(Math.toRadians(-endAngle)) * cosbeta;
/* 283 */           if ((Math.abs((xe - xTest) / xe) > 0.005D) || (Math.abs((ye - yTest) / ye) > 0.005D)) {
/* 284 */             endAngle = 360.0D - endAngle;
/*     */           }
/*     */ 
/* 287 */           if (Math.abs(endAngle) < 0.001D)
/* 288 */             endAngle += 360.0D;
/* 289 */           if (startAngle >= endAngle) {
/* 290 */             endAngle += 360.0D;
/*     */           }
/*     */ 
/* 313 */           newArc.setCenterPoint(center);
/* 314 */           newArc.setCircumferencePoint(circum);
/* 315 */           newArc.setStartAngle(startAngle);
/* 316 */           newArc.setEndAngle(endAngle);
/* 317 */           ArrayList pts = new ArrayList();
/* 318 */           pts.add(center);
/* 319 */           pts.add(circum);
/* 320 */           newArc.setPoints(pts);
/* 321 */           ret.set(i, newArc);
/*     */         }
/*     */       }
/*     */     }
/* 325 */     else if ((adc instanceof Outlook)) {
/* 326 */       ret = new ArrayList();
/* 327 */       ret.add(clipOutlook((Outlook)adc));
/*     */     }
/* 329 */     return ret;
/*     */   }
/*     */ 
/*     */   private List<AbstractDrawableComponent> clipSinglePointElement(AbstractDrawableComponent elementToClip)
/*     */   {
/* 343 */     Coordinate c = null;
/* 344 */     if ((elementToClip instanceof SinglePointElement))
/* 345 */       c = ((SinglePointElement)elementToClip).getLocation();
/* 346 */     else if ((elementToClip instanceof Arc)) {
/* 347 */       c = ((Arc)elementToClip).getCenterPoint();
/*     */     }
/* 349 */     Point p = this.geometryFactory.createPoint(c);
/* 350 */     if (pointStays(p)) {
/* 351 */       ArrayList ret = new ArrayList();
/* 352 */       ret.add(elementToClip);
/* 353 */       return ret;
/*     */     }
/* 355 */     return null;
/*     */   }
/*     */ 
/*     */   private Arc convertArcToLine(Arc arc)
/*     */   {
/* 370 */     Arc newArc = new Arc();
/*     */     try
/*     */     {
/* 373 */       ArrayList points = new ArrayList();
/*     */ 
/* 375 */       Coordinate c0 = arc.getCenterPoint();
/* 376 */       Coordinate c0world = new Coordinate();
/* 377 */       Coordinate c1 = arc.getCircumferencePoint();
/* 378 */       Coordinate c1world = new Coordinate();
/*     */ 
/* 380 */       CoordinateReferenceSystem crs = MapUtil.constructStereographic(6371229.0D, 
/* 381 */         6371229.0D, c0.y, c0.x);
/* 382 */       MathTransform fromLatLon = MapUtil.getTransformFromLatLon(crs);
/* 383 */       MathTransform toLatLon = fromLatLon.inverse();
/*     */ 
/* 385 */       JTS.transform(c0, c0world, fromLatLon);
/* 386 */       JTS.transform(c1, c1world, fromLatLon);
/*     */ 
/* 388 */       double x0 = c0world.x;
/* 389 */       double y0 = c0world.y;
/* 390 */       double x1 = c1world.x;
/* 391 */       double y1 = c1world.y;
/*     */ 
/* 393 */       double radius = Math.sqrt(Math.pow(y1 - y0, 2.0D) + Math.pow(x1 - x0, 2.0D));
/* 394 */       double radius2 = radius * arc.getAxisRatio();
/*     */ 
/* 397 */       double theta = Math.toDegrees(Math.atan2(y1 - y0, x1 - x0));
/*     */ 
/* 400 */       double a1 = -arc.getStartAngle();
/* 401 */       double a2 = -arc.getEndAngle();
/*     */ 
/* 403 */       if (Math.abs(a1 - a2) < 1.E-05D) a2 -= 360.0D;
/*     */ 
/* 405 */       double beta = Math.atan2(y1 - y0, x1 - x0);
/* 406 */       double sinbeta = Math.sin(beta);
/* 407 */       double cosbeta = Math.cos(beta);
/*     */ 
/* 411 */       for (double angle = a1; angle > a2; angle -= 5.0D)
/*     */       {
/* 413 */         double alpha = Math.toRadians(angle);
/* 414 */         double sinalpha = Math.sin(alpha);
/* 415 */         double cosalpha = Math.cos(alpha);
/*     */ 
/* 417 */         double x = x0 + (radius * cosalpha * cosbeta - radius2 * sinalpha * sinbeta);
/* 418 */         double y = y0 + (radius * cosalpha * sinbeta + radius2 * sinalpha * cosbeta);
/*     */ 
/* 420 */         Coordinate c = new Coordinate(x, y);
/* 421 */         JTS.transform(c, c, toLatLon);
/*     */ 
/* 423 */         points.add(c);
/*     */       }
/*     */ 
/* 427 */       double lastA2x = x0 + (radius * Math.cos(Math.toRadians(a2)) * cosbeta - radius2 * Math.sin(Math.toRadians(a2)) * sinbeta);
/* 428 */       double lastA2y = y0 + (radius * Math.cos(Math.toRadians(a2)) * sinbeta + radius2 * Math.sin(Math.toRadians(a2)) * cosbeta);
/* 429 */       Coordinate c = new Coordinate(lastA2x, lastA2y);
/* 430 */       JTS.transform(c, c, toLatLon);
/* 431 */       points.add(c);
/*     */ 
/* 433 */       arc.setPoints(points);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 449 */     return arc;
/*     */   }
/*     */ 
/*     */   private List<AbstractDrawableComponent> clipMultiPointElement(AbstractDrawableComponent elementToClip)
/*     */   {
/* 463 */     ArrayList pointsActual = ((MultiPointElement)elementToClip).getPoints();
/*     */ 
/* 469 */     ArrayList pointsOnBorder = new ArrayList();
/*     */ 
/* 474 */     ArrayList pointsIncludingIntersections = processMultiPointElementIntersection(pointsActual);
/*     */ 
/* 480 */     ArrayList pointsToLeave = processPointsToLeave(pointsOnBorder, 
/* 481 */       pointsIncludingIntersections);
/*     */ 
/* 483 */     if (!pointsToLeave.isEmpty()) {
/* 484 */       ((MultiPointElement)elementToClip).setPoints(pointsToLeave);
/* 485 */       ArrayList ret = new ArrayList();
/*     */ 
/* 491 */       boolean closed = false;
/* 492 */       if ((elementToClip instanceof Line)) {
/* 493 */         closed = ((Line)elementToClip).isClosedLine().booleanValue();
/*     */       }
/* 498 */       else if ((elementToClip instanceof Gfa)) {
/* 499 */         closed = ((Gfa)elementToClip).isClosedLine().booleanValue();
/*     */       }
/* 501 */       else if ((elementToClip instanceof KinkLine)) {
/* 502 */         closed = ((KinkLine)elementToClip).isClosedLine().booleanValue();
/*     */       }
/*     */ 
/* 505 */       if (isToSplit(elementToClip, pointsOnBorder)) {
/* 506 */         List splitted = split(elementToClip, pointsOnBorder);
/*     */ 
/* 508 */         combineFirstAndLast(closed, splitted);
/* 509 */         ret.addAll(splitted);
/*     */       } else {
/* 511 */         ((MultiPointElement)elementToClip).setClosed(Boolean.valueOf(false));
/* 512 */         ret.add(elementToClip);
/*     */       }
/*     */ 
/* 515 */       return ret;
/*     */     }
/* 517 */     return null;
/*     */   }
/*     */ 
/*     */   private boolean pointStays(Point p)
/*     */   {
/* 535 */     return !(this.boundsPoly.contains(p) ^ keep);
/*     */   }
/*     */ 
/*     */   private ArrayList<Coordinate> processMultiPointElementIntersection(ArrayList<Coordinate> pointsActual)
/*     */   {
/* 546 */     ArrayList pointsIncludingIntersections = new ArrayList();
/* 547 */     Coordinate[] line = new Coordinate[2];
/* 548 */     for (int i = 0; i < pointsActual.size() - 1; i++) {
/* 549 */       line[0] = ((Coordinate)pointsActual.get(i));
/* 550 */       line[1] = ((Coordinate)pointsActual.get(i + 1));
/* 551 */       CoordinateArraySequence cas = new CoordinateArraySequence(line);
/* 552 */       LineString ls = new LineString(cas, this.geometryFactory);
/* 553 */       Geometry intersection = null;
/* 554 */       if (this.boundsPoly.getExteriorRing().intersects(ls)) {
/* 555 */         intersection = this.boundsPoly.intersection(ls);
/*     */       }
/*     */ 
/* 558 */       pointsIncludingIntersections.add((Coordinate)pointsActual.get(i));
/*     */ 
/* 560 */       if (intersection != null) {
/* 561 */         for (Coordinate c : intersection.getCoordinates()) {
/* 562 */           if ((!compareCoordinates(c, line[0])) && (!compareCoordinates(c, line[1])))
/*     */           {
/* 564 */             pointsIncludingIntersections.add(c);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 570 */     pointsIncludingIntersections.add((Coordinate)pointsActual.get(pointsActual.size() - 1));
/* 571 */     return pointsIncludingIntersections;
/*     */   }
/*     */ 
/*     */   private ArrayList<Coordinate> processPointsToLeave(ArrayList<Coordinate> pointsOnBorder, ArrayList<Coordinate> pointsIncludingIntersections)
/*     */   {
/* 585 */     ArrayList pointsToLeave = new ArrayList();
/* 586 */     for (Coordinate c : pointsIncludingIntersections) {
/* 587 */       Point p = this.geometryFactory.createPoint(c);
/*     */ 
/* 590 */       boolean onBorder = this.boundsPoly.getExteriorRing().distance(p) < 1.E-09D;
/* 591 */       if (onBorder) {
/* 592 */         pointsOnBorder.add(c);
/*     */       }
/* 594 */       if ((pointStays(p)) || (onBorder)) {
/* 595 */         pointsToLeave.add(c);
/*     */       }
/*     */     }
/* 598 */     return pointsToLeave;
/*     */   }
/*     */ 
/*     */   private boolean isToSplit(AbstractDrawableComponent adc, ArrayList<Coordinate> pointsOnBorder)
/*     */   {
/* 610 */     if ((adc instanceof MultiPointElement)) {
/* 611 */       ArrayList points = ((MultiPointElement)adc).getPoints();
/* 612 */       for (int i = 0; i < points.size() - 2; i++) {
/* 613 */         if ((pointsOnBorder.contains(points.get(i))) && (pointsOnBorder.contains(points.get(i + 1)))) {
/* 614 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 618 */     return false;
/*     */   }
/*     */ 
/*     */   private List<AbstractDrawableComponent> split(AbstractDrawableComponent adc, ArrayList<Coordinate> pointsOnBorder)
/*     */   {
/* 632 */     ArrayList ret = new ArrayList();
/* 633 */     if ((adc instanceof MultiPointElement)) {
/* 634 */       ArrayList points = ((MultiPointElement)adc).getPoints();
/* 635 */       ArrayList firstElementPoints = new ArrayList();
/* 636 */       ArrayList remainingPoints = new ArrayList();
/* 637 */       int i = 0;
/* 638 */       for (i = 0; i < points.size() - 2; i++) {
/* 639 */         firstElementPoints.add((Coordinate)points.get(i));
/* 640 */         if ((i != 0) && 
/* 641 */           (pointsOnBorder.contains(points.get(i))) && (pointsOnBorder.contains(points.get(i + 1)))) {
/* 642 */           i++;
/* 643 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 647 */       for (; i < points.size(); i++) {
/* 648 */         remainingPoints.add((Coordinate)points.get(i));
/*     */       }
/* 650 */       AbstractDrawableComponent copy = adc.copy();
/* 651 */       ((MultiPointElement)adc).setPoints(firstElementPoints);
/* 652 */       if (firstElementPoints.size() > 1) {
/* 653 */         ((MultiPointElement)adc).setClosed(Boolean.valueOf(false));
/* 654 */         ret.add(adc);
/*     */       }
/*     */ 
/* 657 */       ((MultiPointElement)copy).setPoints(remainingPoints);
/*     */ 
/* 660 */       if ((copy.getPoints().size() > 2) && (isToSplit(copy, pointsOnBorder))) {
/* 661 */         ret.addAll(split(copy, pointsOnBorder));
/*     */       } else {
/* 663 */         ((MultiPointElement)copy).setClosed(Boolean.valueOf(false));
/* 664 */         ret.add(copy);
/*     */       }
/*     */     }
/* 667 */     return ret;
/*     */   }
/*     */ 
/*     */   private void combineFirstAndLast(boolean closed, List<AbstractDrawableComponent> splitted)
/*     */   {
/* 679 */     AbstractDrawableComponent first = (AbstractDrawableComponent)splitted.get(0);
/* 680 */     AbstractDrawableComponent last = (AbstractDrawableComponent)splitted.get(splitted.size() - 1);
/*     */ 
/* 682 */     if (compareCoordinates((Coordinate)first.getPoints().get(0), (Coordinate)last.getPoints().get(last.getPoints().size() - 1))) {
/* 683 */       closed = true;
/*     */     }
/*     */ 
/* 686 */     if ((closed) && (!keep) && (splitted.size() > 1)) {
/* 687 */       splitted.remove(0);
/*     */ 
/* 689 */       last.getPoints().addAll(first.getPoints());
/*     */ 
/* 691 */       ((MultiPointElement)last).setPoints(removeDuplicates(((MultiPointElement)last).getPoints()));
/* 692 */       ((MultiPointElement)last).setClosed(Boolean.valueOf(false));
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean compareCoordinates(Coordinate c1, Coordinate c2)
/*     */   {
/* 708 */     if ((Math.abs(c1.x - c2.x) < 1.E-09D) && (Math.abs(c1.y - c2.y) < 1.E-09D)) {
/* 709 */       return true;
/*     */     }
/* 711 */     return false;
/*     */   }
/*     */ 
/*     */   private <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
/*     */   {
/* 723 */     ArrayList ret = new ArrayList();
/* 724 */     for (Object obj : list) {
/* 725 */       if (!ret.contains(obj)) {
/* 726 */         ret.add(obj);
/*     */       }
/*     */     }
/* 729 */     return ret;
/*     */   }
/*     */ 
/*     */   public Outlook clipOutlook(Outlook otlk)
/*     */   {
/* 734 */     if (this.boundsPoly == null) return otlk;
/*     */ 
/* 736 */     Outlook ret = otlk.copy();
/*     */ 
/* 738 */     List clipped = new ArrayList();
/* 739 */     Iterator it = ret.getComponentIterator();
/* 740 */     while (it.hasNext()) {
/* 741 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 742 */       it.remove();
/* 743 */       if (adc.getName().equals(Outlook.OUTLOOK_LABELED_LINE)) {
/* 744 */         clipped.addAll(clipOutlookLine((DECollection)adc));
/*     */       }
/* 746 */       else if (adc.getName().equals(Outlook.OUTLOOK_LINE_GROUP)) {
/* 747 */         clipped.add(clipOutlookLineGroup((DECollection)adc));
/*     */       }
/*     */     }
/*     */ 
/* 751 */     for (DECollection dec : clipped) {
/* 752 */       ret.add(dec);
/*     */     }
/*     */ 
/* 755 */     return ret;
/*     */   }
/*     */ 
/*     */   private DECollection clipOutlookLineGroup(DECollection dec)
/*     */   {
/* 760 */     DECollection ret = dec.copy();
/* 761 */     ret.clear();
/*     */ 
/* 763 */     Iterator it = dec.getComponentIterator();
/*     */ 
/* 765 */     List clipped = new ArrayList();
/*     */     Iterator localIterator1;
/* 767 */     for (; it.hasNext(); 
/* 777 */       localIterator1.hasNext())
/*     */     {
/* 768 */       clipped.clear();
/*     */ 
/* 770 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 771 */       it.remove();
/*     */ 
/* 773 */       if (adc.getName().equals(Outlook.OUTLOOK_LABELED_LINE)) {
/* 774 */         clipped.addAll(clipOutlookLine((DECollection)adc));
/*     */       }
/*     */ 
/* 777 */       localIterator1 = clipped.iterator(); continue; DECollection otlkLine = (DECollection)localIterator1.next();
/* 778 */       ret.add(otlkLine);
/*     */     }
/*     */ 
/* 782 */     return ret;
/*     */   }
/*     */ 
/*     */   private List<DECollection> clipOutlookLine(DECollection dec) {
/* 786 */     List ret = new ArrayList();
/* 787 */     Iterator it = dec.getComponentIterator();
/*     */ 
/* 789 */     while (it.hasNext()) {
/* 790 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 791 */       if ((adc instanceof Line)) {
/* 792 */         List clippedList = clipDrawableComponent(adc);
/* 793 */         if ((clippedList != null) && (clippedList.size() >= 1)) {
/* 794 */           Iterator it2 = clippedList.iterator();
/* 795 */           while (it2.hasNext()) {
/* 796 */             AbstractDrawableComponent clippedLine = (AbstractDrawableComponent)it2.next();
/* 797 */             if ((clippedLine instanceof Line)) {
/* 798 */               DECollection outlookLine = dec.copy();
/*     */ 
/* 801 */               Iterator it3 = outlookLine.getComponentIterator();
/* 802 */               while (it3.hasNext()) {
/* 803 */                 AbstractDrawableComponent ln = (AbstractDrawableComponent)it3.next();
/* 804 */                 if ((ln instanceof Line)) {
/* 805 */                   it3.remove();
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/* 810 */               outlookLine.add(0, clippedLine);
/* 811 */               ret.add(outlookLine);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 818 */     return ret;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.clipper.ClipProduct
 * JD-Core Version:    0.6.2
 */