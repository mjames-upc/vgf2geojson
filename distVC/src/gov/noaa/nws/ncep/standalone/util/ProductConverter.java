/*      */ package gov.noaa.nws.ncep.standalone.util;
/*      */ 
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.LineString;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import gov.noaa.nws.ncep.common.staticdata.SPCCounty;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.IStationField.StationField;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.StationTable;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourCircle;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourMinmax;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAvnText.AviationTextType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.ISinglePoint;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IVector.VectorType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ComboSymbol;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetBarb;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetHash;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetText;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Track;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox.WatchStatus;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Cloud;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Label;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Turbulence;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.Tcm;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ColorType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Point;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.TCA;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.TCM;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.TrackConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.WatchBox.Hole;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.WatchBox.Outline;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.WatchBox.Status;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaRules;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaWording;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Ccfp;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo.ProductInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.TCAElement;
/*      */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.net.URL;
/*      */ import java.security.CodeSource;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import java.util.TimeZone;
/*      */ import javax.xml.datatype.DatatypeConfigurationException;
/*      */ import javax.xml.datatype.DatatypeFactory;
/*      */ import javax.xml.datatype.XMLGregorianCalendar;
/*      */ 
/*      */ public class ProductConverter
/*      */ {
/*      */   public static List<gov.noaa.nws.ncep.ui.pgen.elements.Product> convert(Products filePrds)
/*      */   {
/*  125 */     List prd = new ArrayList();
/*      */ 
/*  127 */     for (gov.noaa.nws.ncep.ui.pgen.file.Product fPrd : filePrds.getProduct()) {
/*  128 */       gov.noaa.nws.ncep.ui.pgen.elements.Product p = new gov.noaa.nws.ncep.ui.pgen.elements.Product();
/*      */ 
/*  130 */       p.setName(fPrd.getName());
/*  131 */       p.setForecaster(fPrd.getForecaster());
/*      */ 
/*  133 */       if (fPrd.isOnOff() != null) {
/*  134 */         p.setOnOff(fPrd.isOnOff().booleanValue());
/*      */       }
/*      */ 
/*  137 */       if (fPrd.getType() != null) {
/*  138 */         p.setType(fPrd.getType());
/*      */       }
/*      */ 
/*  141 */       if (fPrd.getCenter() != null) {
/*  142 */         p.setCenter(fPrd.getCenter());
/*      */       }
/*      */ 
/*  145 */       if (fPrd.isSaveLayers() != null) {
/*  146 */         p.setSaveLayers(fPrd.isSaveLayers().booleanValue());
/*      */       }
/*      */       else {
/*  149 */         p.setSaveLayers(false);
/*      */       }
/*      */ 
/*  152 */       p.setUseFile(false);
/*  153 */       p.setInputFile(null);
/*      */ 
/*  155 */       String outFile = fPrd.getOutputFile();
/*  156 */       if (outFile != null) {
/*  157 */         p.setOutputFile(outFile);
/*      */       }
/*      */       else {
/*  160 */         p.setOutputFile(null);
/*      */       }
/*      */ 
/*  163 */       p.setLayers(convertFileLayers(fPrd.getLayer()));
/*      */ 
/*  165 */       prd.add(p);
/*      */     }
/*      */ 
/*  168 */     return prd;
/*      */   }
/*      */ 
/*      */   private static List<gov.noaa.nws.ncep.ui.pgen.elements.Layer> convertFileLayers(List<gov.noaa.nws.ncep.ui.pgen.file.Layer> flayers)
/*      */   {
/*  177 */     List layers = new ArrayList();
/*      */ 
/*  179 */     for (gov.noaa.nws.ncep.ui.pgen.file.Layer fLayer : flayers)
/*      */     {
/*  181 */       gov.noaa.nws.ncep.ui.pgen.elements.Layer lyr = new gov.noaa.nws.ncep.ui.pgen.elements.Layer();
/*  182 */       lyr.setName(fLayer.getName());
/*      */ 
/*  184 */       lyr.setColor(new java.awt.Color(fLayer.getColor().getRed(), fLayer.getColor().getGreen(), 
/*  185 */         fLayer.getColor().getBlue(), fLayer.getColor().getAlpha().intValue()));
/*      */ 
/*  187 */       if (fLayer.isOnOff() != null) {
/*  188 */         lyr.setOnOff(fLayer.isOnOff().booleanValue());
/*  189 */         lyr.setMonoColor(fLayer.isMonoColor().booleanValue());
/*  190 */         lyr.setFilled(fLayer.isFilled().booleanValue());
/*      */       }
/*      */       else {
/*  193 */         lyr.setOnOff(true);
/*  194 */         lyr.setMonoColor(false);
/*  195 */         lyr.setFilled(false);
/*      */       }
/*      */ 
/*  198 */       lyr.setInputFile(null);
/*  199 */       lyr.setOutputFile(null);
/*      */ 
/*  201 */       lyr.setDrawables(convert(fLayer.getDrawableElement()));
/*      */ 
/*  203 */       layers.add(lyr);
/*      */     }
/*      */ 
/*  206 */     return layers;
/*      */   }
/*      */ 
/*      */   private static List<AbstractDrawableComponent> convert(gov.noaa.nws.ncep.ui.pgen.file.DrawableElement elem)
/*      */   {
/*  216 */     List des = new ArrayList();
/*      */     Object line;
/*  218 */     if (!elem.getLine().isEmpty())
/*      */     {
/*  220 */       for (gov.noaa.nws.ncep.ui.pgen.file.Line fLine : elem.getLine())
/*      */       {
/*  222 */         java.awt.Color[] clr = new java.awt.Color[fLine.getColor().size()];
/*  223 */         int nn = 0;
/*  224 */         for (gov.noaa.nws.ncep.ui.pgen.file.Color fColor : fLine.getColor()) {
/*  225 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  226 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  229 */         ArrayList linePoints = new ArrayList();
/*  230 */         nn = 0;
/*  231 */         for (Point pt : fLine.getPoint()) {
/*  232 */           linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */         }
/*      */ 
/*  235 */         line = new gov.noaa.nws.ncep.ui.pgen.elements.Line(null, clr, 
/*  236 */           fLine.getLineWidth().floatValue(), fLine.getSizeScale().doubleValue(), fLine.isClosed().booleanValue(), 
/*  237 */           fLine.isFilled().booleanValue(), linePoints, 
/*  238 */           fLine.getSmoothFactor().intValue(), 
/*  239 */           FillPatternList.FillPattern.valueOf(fLine.getFillPattern()), 
/*  240 */           fLine.getPgenCategory(), fLine.getPgenType());
/*      */ 
/*  242 */         ((gov.noaa.nws.ncep.ui.pgen.elements.Line)line).setFlipSide(fLine.isFlipSide());
/*      */ 
/*  244 */         des.add(line);
/*      */       }
/*      */     }
/*      */     Object symbol;
/*  249 */     if (!elem.getSymbol().isEmpty())
/*      */     {
/*  251 */       for (gov.noaa.nws.ncep.ui.pgen.file.Symbol fSymbol : elem.getSymbol())
/*      */       {
/*  253 */         java.awt.Color[] clr = new java.awt.Color[fSymbol.getColor().size()];
/*  254 */         int nn = 0;
/*  255 */         for (line = fSymbol.getColor().iterator(); ((Iterator)line).hasNext(); ) { gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)((Iterator)line).next();
/*  256 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  257 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  260 */         Point loc = fSymbol.getPoint();
/*      */ 
/*  262 */         if (fSymbol.getPgenCategory().equals("Combo")) {
/*  263 */           ComboSymbol symbol = new ComboSymbol(null, clr, 
/*  264 */             fSymbol.getLineWidth().floatValue(), fSymbol.getSizeScale().doubleValue(), 
/*  265 */             fSymbol.isClear(), 
/*  266 */             new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  267 */             fSymbol.getPgenCategory(), fSymbol.getPgenType());
/*  268 */           des.add(symbol);
/*      */         }
/*      */         else {
/*  271 */           symbol = new gov.noaa.nws.ncep.ui.pgen.elements.Symbol(null, clr, 
/*  272 */             fSymbol.getLineWidth().floatValue(), fSymbol.getSizeScale().doubleValue(), 
/*  273 */             fSymbol.isClear(), 
/*  274 */             new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  275 */             fSymbol.getPgenCategory(), fSymbol.getPgenType());
/*  276 */           des.add(symbol);
/*      */         }
/*      */       }
/*      */     }
/*      */     Object st;
/*      */     int nline;
/*  283 */     if (!elem.getText().isEmpty())
/*      */     {
/*  285 */       for (gov.noaa.nws.ncep.ui.pgen.file.Text fText : elem.getText())
/*      */       {
/*  287 */         java.awt.Color[] clr = new java.awt.Color[fText.getColor().size()];
/*  288 */         int nn = 0;
/*  289 */         for (symbol = fText.getColor().iterator(); ((Iterator)symbol).hasNext(); ) { gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)((Iterator)symbol).next();
/*  290 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  291 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  294 */         Point loc = fText.getPoint();
/*      */ 
/*  296 */         st = new String[fText.getTextLine().size()];
/*      */ 
/*  298 */         nline = 0;
/*  299 */         for (String str : fText.getTextLine()) {
/*  300 */           st[(nline++)] = str;
/*      */         }
/*      */ 
/*  303 */         gov.noaa.nws.ncep.ui.pgen.elements.Text text = new gov.noaa.nws.ncep.ui.pgen.elements.Text(null, fText.getFontName(), 
/*  304 */           fText.getFontSize().floatValue(), 
/*  305 */           IText.TextJustification.valueOf(fText.getJustification()), 
/*  306 */           new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  307 */           fText.getRotation().doubleValue(), 
/*  308 */           IText.TextRotation.valueOf(fText.getRotationRelativity()), 
/*  309 */           (String[])st, 
/*  310 */           IText.FontStyle.valueOf(fText.getStyle()), clr[0], 0, 0, 
/*  311 */           fText.isMask().booleanValue(), 
/*  312 */           IText.DisplayType.valueOf(fText.getDisplayType()), 
/*  313 */           fText.getPgenCategory(), fText.getPgenType());
/*      */ 
/*  315 */         if (fText.getXOffset() != null) {
/*  316 */           text.setXOffset(fText.getXOffset().intValue());
/*      */         }
/*      */ 
/*  319 */         if (fText.getYOffset() != null) {
/*  320 */           text.setYOffset(fText.getYOffset().intValue());
/*      */         }
/*      */ 
/*  323 */         if (fText.isHide() != null) {
/*  324 */           text.setHide(fText.isHide());
/*      */         }
/*      */ 
/*  327 */         if (fText.isAuto() != null) {
/*  328 */           text.setAuto(fText.isAuto());
/*      */         }
/*      */ 
/*  331 */         des.add(text);
/*      */       }
/*      */     }
/*      */     Object text;
/*  336 */     if (!elem.getAvnText().isEmpty())
/*      */     {
/*  338 */       for (gov.noaa.nws.ncep.ui.pgen.file.AvnText aText : elem.getAvnText())
/*      */       {
/*  340 */         java.awt.Color[] clr = new java.awt.Color[aText.getColor().size()];
/*  341 */         int nn = 0;
/*  342 */         for (st = aText.getColor().iterator(); ((Iterator)st).hasNext(); ) { gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)((Iterator)st).next();
/*  343 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  344 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  347 */         Point loc = aText.getPoint();
/*      */ 
/*  349 */         text = new gov.noaa.nws.ncep.ui.pgen.elements.AvnText(null, aText.getFontName(), 
/*  350 */           aText.getFontSize().floatValue(), 
/*  351 */           IText.TextJustification.valueOf(aText.getJustification()), 
/*  352 */           new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  353 */           IAvnText.AviationTextType.valueOf(aText.getAvnTextType()), 
/*  354 */           aText.getTopValue(), aText.getBottomValue(), 
/*  355 */           IText.FontStyle.valueOf(aText.getStyle()), clr[0], 
/*  356 */           aText.getSymbolPatternName(), aText.getPgenCategory(), aText.getPgenType());
/*      */ 
/*  358 */         des.add(text);
/*      */       }
/*      */     }
/*      */     Object text;
/*  363 */     if (!elem.getMidCloudText().isEmpty())
/*      */     {
/*  365 */       for (gov.noaa.nws.ncep.ui.pgen.file.MidCloudText mText : elem.getMidCloudText())
/*      */       {
/*  367 */         java.awt.Color[] clr = new java.awt.Color[mText.getColor().size()];
/*  368 */         int nn = 0;
/*  369 */         for (text = mText.getColor().iterator(); ((Iterator)text).hasNext(); ) { gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)((Iterator)text).next();
/*  370 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  371 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  374 */         Point loc = mText.getPoint();
/*      */ 
/*  376 */         text = new gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText(null, mText.getFontName(), 
/*  377 */           mText.getFontSize().floatValue(), 
/*  378 */           IText.TextJustification.valueOf(mText.getJustification()), 
/*  379 */           new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  380 */           mText.getCloudTypes(), mText.getCloudAmounts(), 
/*  381 */           mText.getTurbulenceType(), mText.getTurbulenceLevels(), 
/*  382 */           mText.getIcingType(), mText.getIcingLevels(), 
/*  383 */           mText.getTstormTypes(), mText.getTstormLevels(), 
/*  384 */           IText.FontStyle.valueOf(mText.getStyle()), clr[0], 
/*  385 */           mText.getPgenCategory(), mText.getPgenType());
/*      */ 
/*  387 */         des.add(text);
/*      */       }
/*      */     }
/*      */     Object arc;
/*  392 */     if (!elem.getArc().isEmpty())
/*      */     {
/*  394 */       for (gov.noaa.nws.ncep.ui.pgen.file.Arc fArc : elem.getArc())
/*      */       {
/*  396 */         java.awt.Color[] clr = new java.awt.Color[fArc.getColor().size()];
/*  397 */         int nn = 0;
/*  398 */         for (text = fArc.getColor().iterator(); ((Iterator)text).hasNext(); ) { gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)((Iterator)text).next();
/*  399 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  400 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  404 */         ArrayList linePoints = new ArrayList();
/*  405 */         for (Point pt : fArc.getPoint()) {
/*  406 */           linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */         }
/*      */ 
/*  409 */         arc = new gov.noaa.nws.ncep.ui.pgen.elements.Arc(null, clr[0], 
/*  410 */           fArc.getLineWidth().floatValue(), fArc.getSizeScale().doubleValue(), fArc.isClosed().booleanValue(), 
/*  411 */           fArc.isFilled().booleanValue(), fArc.getSmoothFactor().intValue(), 
/*  412 */           FillPatternList.FillPattern.valueOf(fArc.getFillPattern()), fArc.getPgenType(), 
/*  413 */           (Coordinate)linePoints.get(0), (Coordinate)linePoints.get(1), 
/*  414 */           fArc.getPgenCategory(), fArc.getAxisRatio().doubleValue(), 
/*  415 */           fArc.getStartAngle().doubleValue(), fArc.getEndAngle().doubleValue());
/*      */ 
/*  417 */         des.add(arc);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  423 */     des.addAll(TrackConverter.getTrackElementListByTrackBeanList(elem.getTrack()));
/*      */     Object vtype;
/*      */     Object pgenType;
/*  425 */     if (!elem.getVector().isEmpty())
/*      */     {
/*  427 */       for (gov.noaa.nws.ncep.ui.pgen.file.Vector fVector : elem.getVector())
/*      */       {
/*  429 */         java.awt.Color[] clr = new java.awt.Color[fVector.getColor().size()];
/*  430 */         int nn = 0;
/*  431 */         for (arc = fVector.getColor().iterator(); ((Iterator)arc).hasNext(); ) { gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)((Iterator)arc).next();
/*  432 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  433 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  436 */         Point loc = fVector.getPoint();
/*      */ 
/*  438 */         vtype = null;
/*  439 */         pgenType = fVector.getPgenType();
/*  440 */         if (((String)pgenType).equalsIgnoreCase("Hash")) {
/*  441 */           vtype = IVector.VectorType.HASH_MARK;
/*      */         }
/*  443 */         else if (((String)pgenType).equalsIgnoreCase("Barb")) {
/*  444 */           vtype = IVector.VectorType.WIND_BARB;
/*      */         }
/*      */         else {
/*  447 */           vtype = IVector.VectorType.ARROW;
/*      */         }
/*      */ 
/*  450 */         gov.noaa.nws.ncep.ui.pgen.elements.Vector vector = new gov.noaa.nws.ncep.ui.pgen.elements.Vector(null, clr, 
/*  451 */           fVector.getLineWidth().floatValue(), fVector.getSizeScale().doubleValue(), 
/*  452 */           fVector.isClear(), new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  453 */           (IVector.VectorType)vtype, fVector.getSpeed().doubleValue(), fVector.getDirection().doubleValue(), 
/*  454 */           fVector.getArrowHeadSize().doubleValue(), fVector.isDirectionOnly().booleanValue(), 
/*  455 */           fVector.getPgenCategory(), fVector.getPgenType());
/*      */ 
/*  457 */         des.add(vector);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  462 */     if (!elem.getTCA().isEmpty())
/*      */     {
/*  464 */       for (TCA ftca : elem.getTCA())
/*      */       {
/*  487 */         TCAElement tca = new TCAElement();
/*  488 */         tca.setPgenType(ftca.getPgenType());
/*  489 */         tca.setPgenCategory(ftca.getPgenCategory());
/*      */ 
/*  491 */         tca.setStormNumber(ftca.getStormNumber().intValue());
/*  492 */         tca.setStormName(ftca.getStormName());
/*  493 */         tca.setBasin(ftca.getBasin());
/*  494 */         tca.setIssueStatus(ftca.getIssueStatus());
/*  495 */         tca.setStormType(ftca.getStormType());
/*  496 */         tca.setAdvisoryNumber(ftca.getAdvisoryNumber());
/*  497 */         tca.setTimeZone(ftca.getTimeZone());
/*  498 */         tca.setTextLocation(ftca.getTextLocation());
/*      */ 
/*  500 */         Calendar advTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*  501 */         XMLGregorianCalendar xmlCal = ftca.getAdvisoryTime();
/*  502 */         advTime.set(xmlCal.getYear(), xmlCal.getMonth() - 1, xmlCal.getDay(), 
/*  503 */           xmlCal.getHour(), xmlCal.getMinute(), xmlCal.getSecond());
/*  504 */         tca.setAdvisoryTime(advTime);
/*      */ 
/*  506 */         tca.setAdvisories(ftca.getAdvisories());
/*      */ 
/*  508 */         des.add(tca);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  513 */     if (!elem.getDECollection().isEmpty())
/*      */     {
/*  515 */       for (gov.noaa.nws.ncep.ui.pgen.file.DECollection fdec : elem.getDECollection()) {
/*  516 */         String cname = fdec.getCollectionName();
/*  517 */         if (cname.equalsIgnoreCase("jet")) {
/*  518 */           Jet jet = convertXML2Jet(fdec);
/*  519 */           if (jet != null) {
/*  520 */             des.add(jet);
/*      */           }
/*      */         }
/*  523 */         else if (cname.equalsIgnoreCase("Cloud")) {
/*  524 */           des.add(convertXML2Cloud(fdec));
/*      */         }
/*  526 */         else if (cname.equalsIgnoreCase("Turbulence")) {
/*  527 */           des.add(convertXML2Turb(fdec));
/*      */         }
/*  529 */         else if (cname.contains("CCFP_SIGMET")) {
/*  530 */           des.add(convertXML2Ccfp(fdec));
/*      */         }
/*      */         else {
/*  533 */           gov.noaa.nws.ncep.ui.pgen.elements.DECollection dec = new gov.noaa.nws.ncep.ui.pgen.elements.DECollection(cname);
/*  534 */           dec.setPgenCategory(fdec.getPgenCategory());
/*  535 */           dec.setPgenType(fdec.getPgenType());
/*  536 */           dec.add(convert(fdec.getDrawableElement()));
/*  537 */           des.add(dec);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  542 */     if (!elem.getWatchBox().isEmpty()) {
/*  543 */       for (gov.noaa.nws.ncep.ui.pgen.file.WatchBox fwb : elem.getWatchBox()) {
/*  544 */         des.add(convertXML2WatchBox(fwb));
/*      */       }
/*      */     }
/*      */ 
/*  548 */     if (!elem.getContours().isEmpty())
/*      */     {
/*  550 */       for (gov.noaa.nws.ncep.ui.pgen.file.Contours fdec : elem.getContours()) {
/*  551 */         des.add(convertXML2Contours(fdec));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  556 */     if (!elem.getOutlook().isEmpty())
/*      */     {
/*  558 */       for (gov.noaa.nws.ncep.ui.pgen.file.Outlook fotlk : elem.getOutlook())
/*  559 */         des.add(convertXML2Outlook(fotlk));
/*      */     }
/*      */     Object sigmet;
/*  564 */     if (!elem.getSigmet().isEmpty())
/*      */     {
/*  566 */       for (gov.noaa.nws.ncep.ui.pgen.file.Sigmet fSig : elem.getSigmet())
/*      */       {
/*  568 */         java.awt.Color[] clr = new java.awt.Color[fSig.getColor().size()];
/*  569 */         int nn = 0;
/*  570 */         for (vtype = fSig.getColor().iterator(); ((Iterator)vtype).hasNext(); ) { gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)((Iterator)vtype).next();
/*  571 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  572 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  575 */         ArrayList sigmetPoints = new ArrayList();
/*  576 */         nn = 0;
/*  577 */         for (pgenType = fSig.getPoint().iterator(); ((Iterator)pgenType).hasNext(); ) { Point pt = (Point)((Iterator)pgenType).next();
/*  578 */           sigmetPoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */         }
/*      */ 
/*  581 */         sigmet = new gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet(null, clr, 
/*  582 */           fSig.getLineWidth().floatValue(), fSig.getSizeScale().doubleValue(), fSig.isClosed().booleanValue(), 
/*  583 */           fSig.isFilled().booleanValue(), sigmetPoints, 
/*  584 */           fSig.getSmoothFactor().intValue(), 
/*  585 */           FillPatternList.FillPattern.valueOf(fSig.getFillPattern()), 
/*  586 */           fSig.getPgenCategory(), fSig.getPgenType(), 
/*  587 */           fSig.getType(), fSig.getWidth().doubleValue(), 
/*  589 */           fSig.getEditableAttrArea(), 
/*  590 */           fSig.getEditableAttrIssueOffice(), 
/*  591 */           fSig.getEditableAttrStatus(), 
/*  592 */           fSig.getEditableAttrId(), 
/*  593 */           fSig.getEditableAttrSeqNum(), 
/*  594 */           fSig.getEditableAttrStartTime(), 
/*  595 */           fSig.getEditableAttrEndTime(), 
/*  596 */           fSig.getEditableAttrRemarks(), 
/*  597 */           fSig.getEditableAttrPhenom(), 
/*  598 */           fSig.getEditableAttrPhenom2(), 
/*  599 */           fSig.getEditableAttrPhenomName(), 
/*  600 */           fSig.getEditableAttrPhenomLat(), 
/*  601 */           fSig.getEditableAttrPhenomLon(), 
/*  602 */           fSig.getEditableAttrPhenomPressure(), 
/*  603 */           fSig.getEditableAttrPhenomMaxWind(), 
/*  604 */           fSig.getEditableAttrFreeText(), 
/*  605 */           fSig.getEditableAttrTrend(), 
/*  606 */           fSig.getEditableAttrMovement(), 
/*  607 */           fSig.getEditableAttrPhenomSpeed(), 
/*  608 */           fSig.getEditableAttrPhenomDirection(), 
/*  609 */           fSig.getEditableAttrLevel(), 
/*  610 */           fSig.getEditableAttrLevelInfo1(), 
/*  611 */           fSig.getEditableAttrLevelInfo2(), 
/*  612 */           fSig.getEditableAttrLevelText1(), 
/*  613 */           fSig.getEditableAttrLevelText2(), 
/*  614 */           fSig.getEditableAttrFromLine(), 
/*  615 */           fSig.getEditableAttrFir());
/*      */ 
/*  617 */         des.add(sigmet);
/*      */       }
/*      */     }
/*      */ 
/*  621 */     if (!elem.getGfa().isEmpty())
/*      */     {
/*  623 */       for (gov.noaa.nws.ncep.ui.pgen.file.Gfa fgfa : elem.getGfa())
/*      */       {
/*  625 */         java.awt.Color[] clr = new java.awt.Color[fgfa.getColor().size()];
/*  626 */         int nn = 0;
/*  627 */         for (sigmet = fgfa.getColor().iterator(); ((Iterator)sigmet).hasNext(); ) { gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)((Iterator)sigmet).next();
/*  628 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), fColor.getBlue(), 
/*  629 */             fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  632 */         ArrayList linePoints = new ArrayList();
/*  633 */         nn = 0;
/*  634 */         for (pgenType = fgfa.getPoint().iterator(); ((Iterator)pgenType).hasNext(); ) { Point pt = (Point)((Iterator)pgenType).next();
/*  635 */           linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */         }
/*      */ 
/*  638 */         Coordinate gfaTextCoordinate = new Coordinate(fgfa.getLonText().doubleValue(), fgfa.getLatText().doubleValue());
/*      */ 
/*  640 */         gov.noaa.nws.ncep.ui.pgen.gfa.Gfa gfa = new gov.noaa.nws.ncep.ui.pgen.gfa.Gfa(null, clr, fgfa.getLineWidth().floatValue(), fgfa.getSizeScale().doubleValue(), fgfa
/*  641 */           .isClosed().booleanValue(), fgfa.isFilled().booleanValue(), linePoints, gfaTextCoordinate, fgfa
/*  642 */           .getSmoothFactor().intValue(), FillPatternList.FillPattern.valueOf(fgfa.getFillPattern()), fgfa
/*  643 */           .getPgenCategory(), fgfa.getPgenType(), fgfa.getHazard(), fgfa.getFcstHr(), 
/*  644 */           fgfa.getTag(), fgfa.getDesk(), fgfa.getIssueType(), fgfa.getCycleDay().intValue(), fgfa.getCycleHour().intValue(), fgfa.getType(), 
/*  645 */           fgfa.getArea(), fgfa.getBeginning(), fgfa.getEnding(), fgfa.getStates());
/*      */ 
/*  647 */         gfa.setGfaValue("GR", fgfa.getGr());
/*  648 */         gfa.setGfaValue("Frequency", fgfa.getFrequency());
/*  649 */         gfa.setGfaValue("Category", fgfa.getTsCategory());
/*  650 */         gfa.setGfaValue("FZL RANGE", fgfa.getFzlRange());
/*  651 */         gfa.setGfaValue("Level", fgfa.getLevel());
/*  652 */         gfa.setGfaValue("Intensity", fgfa.getIntensity());
/*  653 */         gfa.setGfaValue("Speed", fgfa.getSpeed());
/*  654 */         gfa.setGfaValue("DUE TO", fgfa.getDueTo());
/*  655 */         gfa.setGfaValue("LYR", fgfa.getLyr());
/*  656 */         gfa.setGfaValue("Coverage", fgfa.getCoverage());
/*  657 */         gfa.setGfaValue("Bottom", fgfa.getBottom());
/*  658 */         gfa.setGfaValue("Top", fgfa.getTop());
/*  659 */         if ((fgfa.getTop() != null) && (fgfa.getBottom() != null)) {
/*  660 */           gfa.setGfaValue("Top/Bottom", fgfa.getTop() + "/" + fgfa.getBottom());
/*      */         }
/*  662 */         gfa.setGfaValue("FZL Top/Bottom", fgfa.getFzlTopBottom());
/*  663 */         gfa.setGfaValue("Contour", fgfa.getContour());
/*  664 */         if ("ICE".equals(gfa.getGfaHazard())) {
/*  665 */           gfa.setGfaType("");
/*  666 */           gfa.setGfaValue("Type", fgfa.getType());
/*      */         }
/*  668 */         des.add(gfa);
/*      */       }
/*      */     }
/*      */ 
/*  672 */     if (!elem.getVolcano().isEmpty())
/*      */     {
/*  674 */       for (gov.noaa.nws.ncep.ui.pgen.file.Volcano fVol : elem.getVolcano()) {
/*  675 */         des.add(convertXML2Volcano(fVol));
/*      */       }
/*      */     }
/*      */ 
/*  679 */     if (!elem.getTcm().isEmpty())
/*      */     {
/*  681 */       for (TCM ftcm : elem.getTcm()) {
/*  682 */         des.add(convertXML2Tcm(ftcm));
/*      */       }
/*      */     }
/*      */ 
/*  686 */     return des;
/*      */   }
/*      */ 
/*      */   public static Products convert(List<gov.noaa.nws.ncep.ui.pgen.elements.Product> prds)
/*      */   {
/*  696 */     Products fprds = new Products();
/*      */ 
/*  698 */     for (gov.noaa.nws.ncep.ui.pgen.elements.Product prd : prds)
/*      */     {
/*  700 */       gov.noaa.nws.ncep.ui.pgen.file.Product p = 
/*  701 */         new gov.noaa.nws.ncep.ui.pgen.file.Product();
/*      */ 
/*  703 */       p.setName(prd.getName());
/*  704 */       p.setType(prd.getType());
/*  705 */       p.setForecaster(prd.getForecaster());
/*  706 */       p.setCenter(prd.getCenter());
/*      */ 
/*  708 */       String outFile = prd.getOutputFile();
/*  709 */       if (outFile != null) {
/*  710 */         p.setOutputFile(outFile);
/*      */       }
/*      */       else {
/*  713 */         p.setOutputFile(null);
/*      */       }
/*      */ 
/*  716 */       p.setInputFile(null);
/*  717 */       p.setUseFile(Boolean.valueOf(false));
/*  718 */       p.setOnOff(Boolean.valueOf(prd.isOnOff()));
/*  719 */       p.setSaveLayers(Boolean.valueOf(prd.isSaveLayers()));
/*      */ 
/*  721 */       p.getLayer().addAll(convertLayers(prd.getLayers()));
/*      */ 
/*  723 */       fprds.getProduct().add(p);
/*      */     }
/*      */ 
/*  727 */     return fprds;
/*      */   }
/*      */ 
/*      */   private static List<gov.noaa.nws.ncep.ui.pgen.file.Layer> convertLayers(List<gov.noaa.nws.ncep.ui.pgen.elements.Layer> layers)
/*      */   {
/*  735 */     List flyrs = 
/*  736 */       new ArrayList();
/*      */ 
/*  738 */     for (gov.noaa.nws.ncep.ui.pgen.elements.Layer lyr : layers) {
/*  739 */       gov.noaa.nws.ncep.ui.pgen.file.Layer l = 
/*  740 */         new gov.noaa.nws.ncep.ui.pgen.file.Layer();
/*      */ 
/*  742 */       if (lyr.getName() != null) {
/*  743 */         l.setName(lyr.getName());
/*      */       }
/*      */ 
/*  746 */       gov.noaa.nws.ncep.ui.pgen.file.Color clr = 
/*  747 */         new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/*  749 */       if (lyr.getColor() != null)
/*      */       {
/*  751 */         clr.setRed(lyr.getColor().getRed());
/*  752 */         clr.setGreen(lyr.getColor().getGreen());
/*  753 */         clr.setBlue(lyr.getColor().getBlue());
/*  754 */         clr.setAlpha(Integer.valueOf(lyr.getColor().getAlpha()));
/*      */       }
/*      */       else
/*      */       {
/*  759 */         clr.setRed(0);
/*  760 */         clr.setGreen(255);
/*  761 */         clr.setBlue(255);
/*  762 */         clr.setAlpha(Integer.valueOf(255));
/*      */       }
/*      */ 
/*  765 */       l.setColor(clr);
/*      */ 
/*  767 */       l.setOnOff(Boolean.valueOf(lyr.isOnOff()));
/*  768 */       l.setMonoColor(Boolean.valueOf(lyr.isMonoColor()));
/*  769 */       l.setFilled(Boolean.valueOf(lyr.isFilled()));
/*  770 */       l.setInputFile(null);
/*  771 */       l.setOutputFile(null);
/*      */ 
/*  789 */       l.setDrawableElement(convertDEs(lyr.getDrawables()));
/*      */ 
/*  791 */       flyrs.add(l);
/*      */     }
/*      */ 
/*  794 */     return flyrs;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.file.DrawableElement convertDEs(List<AbstractDrawableComponent> des)
/*      */   {
/*  803 */     gov.noaa.nws.ncep.ui.pgen.file.DrawableElement fde = 
/*  804 */       new gov.noaa.nws.ncep.ui.pgen.file.DrawableElement();
/*      */ 
/*  806 */     for (AbstractDrawableComponent adc : des) {
/*  807 */       if ((adc instanceof gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement)) {
/*  808 */         gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement de = (gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement)adc;
/*      */         Calendar cal;
/*      */         SimpleDateFormat sdf;
/*      */         Object pts;
/*  809 */         if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Line))
/*      */         {
/*  811 */           if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Arc)) {
/*  812 */             gov.noaa.nws.ncep.ui.pgen.file.Arc arc = 
/*  813 */               new gov.noaa.nws.ncep.ui.pgen.file.Arc();
/*      */ 
/*  815 */             for (java.awt.Color clr : de.getColors())
/*      */             {
/*  817 */               gov.noaa.nws.ncep.ui.pgen.file.Color fclr = 
/*  818 */                 new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/*  820 */               fclr.setRed(clr.getRed());
/*  821 */               fclr.setGreen(clr.getGreen());
/*  822 */               fclr.setBlue(clr.getBlue());
/*  823 */               fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/*  825 */               arc.getColor().add(fclr);
/*      */             }
/*      */ 
/*  828 */             for (Coordinate crd : ((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getLinePoints())
/*      */             {
/*  830 */               Point fpt = new Point();
/*  831 */               fpt.setLat(Double.valueOf(crd.y));
/*  832 */               fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/*  834 */               arc.getPoint().add(fpt);
/*      */             }
/*      */ 
/*  837 */             arc.setPgenCategory(de.getPgenCategory());
/*  838 */             arc.setLineWidth(Float.valueOf(de.getLineWidth()));
/*  839 */             arc.setSizeScale(Double.valueOf(de.getSizeScale()));
/*  840 */             arc.setSmoothFactor(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getSmoothFactor()));
/*  841 */             arc.setClosed(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).isClosedLine());
/*  842 */             arc.setFilled(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).isFilled());
/*  843 */             arc.setPgenType(de.getPgenType());
/*      */ 
/*  845 */             arc.setFillPattern(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getFillPattern().name());
/*  846 */             arc.setAxisRatio(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getAxisRatio()));
/*  847 */             arc.setStartAngle(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getStartAngle()));
/*  848 */             arc.setEndAngle(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getEndAngle()));
/*      */ 
/*  850 */             fde.getArc().add(arc);
/*      */           }
/*  853 */           else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)) {
/*  854 */             gov.noaa.nws.ncep.ui.pgen.file.Gfa fgfa = 
/*  855 */               new gov.noaa.nws.ncep.ui.pgen.file.Gfa();
/*      */ 
/*  857 */             for (java.awt.Color clr : de.getColors())
/*      */             {
/*  859 */               gov.noaa.nws.ncep.ui.pgen.file.Color fclr = 
/*  860 */                 new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/*  862 */               fclr.setRed(clr.getRed());
/*  863 */               fclr.setGreen(clr.getGreen());
/*  864 */               fclr.setBlue(clr.getBlue());
/*  865 */               fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/*  867 */               fgfa.getColor().add(fclr);
/*      */             }
/*      */ 
/*  870 */             for (Coordinate crd : ((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).getLinePoints())
/*      */             {
/*  872 */               Point fpt = new Point();
/*  873 */               fpt.setLat(Double.valueOf(crd.y));
/*  874 */               fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/*  876 */               fgfa.getPoint().add(fpt);
/*      */             }
/*      */ 
/*  879 */             fgfa.setPgenCategory(de.getPgenCategory());
/*  880 */             fgfa.setLineWidth(Float.valueOf(de.getLineWidth()));
/*  881 */             fgfa.setSizeScale(Double.valueOf(de.getSizeScale()));
/*  882 */             fgfa.setSmoothFactor(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).getSmoothFactor()));
/*  883 */             fgfa.setClosed(((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).isClosedLine());
/*  884 */             fgfa.setFilled(((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).isFilled());
/*  885 */             fgfa.setPgenType(de.getPgenType());
/*  886 */             gov.noaa.nws.ncep.ui.pgen.gfa.Gfa e = (gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de;
/*  887 */             if (e.getGfaTextCoordinate() != null) {
/*  888 */               fgfa.setLatText(Double.valueOf(e.getGfaTextCoordinate().y));
/*  889 */               fgfa.setLonText(Double.valueOf(e.getGfaTextCoordinate().x));
/*      */             }
/*  891 */             fgfa.setHazard(nvl(e.getGfaHazard()));
/*  892 */             fgfa.setFcstHr(nvl(e.getGfaFcstHr()));
/*  893 */             fgfa.setTag(nvl(e.getGfaTag()));
/*  894 */             fgfa.setDesk(nvl(e.getGfaDesk()));
/*  895 */             fgfa.setIssueType(nvl(e.getGfaIssueType()));
/*  896 */             fgfa.setCycleDay(Integer.valueOf(e.getGfaCycleDay()));
/*  897 */             fgfa.setCycleHour(Integer.valueOf(e.getGfaCycleHour()));
/*  898 */             fgfa.setType(nvl(e.getGfaType()));
/*  899 */             fgfa.setArea(nvl(e.getGfaArea()));
/*  900 */             fgfa.setBeginning(nvl(e.getGfaBeginning()));
/*  901 */             fgfa.setEnding(nvl(e.getGfaEnding()));
/*  902 */             fgfa.setStates(nvl(e.getGfaStates()));
/*  903 */             fgfa.setGr(nvl(e.getGfaValue("GR")));
/*  904 */             fgfa.setFrequency(nvl(e.getGfaValue("Frequency")));
/*  905 */             fgfa.setTsCategory(nvl(e.getGfaValue("Category")));
/*  906 */             fgfa.setFzlRange(nvl(e.getGfaValue("FZL RANGE")));
/*  907 */             fgfa.setLevel(nvl(e.getGfaValue("Level")));
/*  908 */             fgfa.setIntensity(nvl(e.getGfaValue("Intensity")));
/*  909 */             fgfa.setSpeed(nvl(e.getGfaValue("Speed")));
/*  910 */             fgfa.setDueTo(nvl(e.getGfaValue("DUE TO")));
/*  911 */             fgfa.setLyr(nvl(e.getGfaValue("LYR")));
/*  912 */             fgfa.setCoverage(nvl(e.getGfaValue("Coverage")));
/*  913 */             fgfa.setBottom(nvl(e.getGfaBottom()));
/*  914 */             fgfa.setTop(nvl(e.getGfaTop()));
/*  915 */             fgfa.setFzlTopBottom(nvl(e.getGfaValue("FZL Top/Bottom")));
/*  916 */             fgfa.setContour(nvl(e.getGfaValue("Contour")));
/*  917 */             fgfa.setIsOutlook(Boolean.valueOf(e.isOutlook()));
/*  918 */             if ("ICE".equals(e.getGfaHazard())) {
/*  919 */               fgfa.setType(nvl(e.getGfaValue("Type")));
/*      */             }
/*  921 */             cal = (Calendar)e.getAttribute("ISSUE_TIME", Calendar.class);
/*  922 */             sdf = new SimpleDateFormat("ddHHmm");
/*  923 */             if (cal != null) {
/*  924 */               fgfa.setIssueTime(sdf.format(cal.getTime()));
/*      */             }
/*  926 */             cal = (Calendar)e.getAttribute("UNTIL_TIME", Calendar.class);
/*  927 */             if (cal != null) {
/*  928 */               fgfa.setUntilTime(sdf.format(cal.getTime()));
/*      */             }
/*  930 */             if (e.getAttribute("WORDING") != null) {
/*  931 */               GfaWording w = (GfaWording)e.getAttribute("WORDING", GfaWording.class);
/*  932 */               fgfa.setFromCondsDvlpg(GfaRules.replacePlusWithCycle(w
/*  933 */                 .getFromCondsDvlpg(), e.getGfaCycleHour()));
/*  934 */               fgfa.setFromCondsEndg(GfaRules.replacePlusWithCycle(w
/*  935 */                 .getFromCondsEndg(), e.getGfaCycleHour()));
/*  936 */               fgfa.setCondsContg(GfaRules.replacePlusWithCycle(w.getCondsContg(), e
/*  937 */                 .getGfaCycleHour()));
/*  938 */               fgfa.setOtlkCondsDvlpg(GfaRules.replacePlusWithCycle(w
/*  939 */                 .getOtlkCondsDvlpg(), e.getGfaCycleHour()));
/*  940 */               fgfa.setOtlkCondsEndg(GfaRules.replacePlusWithCycle(w
/*  941 */                 .getOtlkCondsEndg(), e.getGfaCycleHour()));
/*      */             }
/*      */ 
/*  944 */             pts = e.getPoints();
/*  945 */             pts = SnapUtil.getSnapWithStation((List)pts, SnapUtil.VOR_STATION_LIST, 10, 16, false);
/*  946 */             Coordinate[] a = new Coordinate[((ArrayList)pts).size()];
/*  947 */             a = (Coordinate[])((ArrayList)pts).toArray(a);
/*  948 */             String s = "";
/*  949 */             if (fgfa.getHazard().equalsIgnoreCase("FZLVL")) {
/*  950 */               if (fgfa.isClosed().booleanValue()) {
/*  951 */                 s = SnapUtil.getVORText(a, "-", "Area", -1, true, false, true);
/*      */               }
/*      */               else {
/*  954 */                 s = SnapUtil.getVORText(a, "-", "Line", -1, true, false, true);
/*      */               }
/*      */             }
/*  957 */             else if (fgfa.getHazard().equalsIgnoreCase("LLWS")) {
/*  958 */               s = SnapUtil.getVORText(a, "-", "Area", -1, true, false, true);
/*      */             }
/*      */             else {
/*  961 */               s = SnapUtil.getVORText(a, " TO ", "Area", -1, true, false, true);
/*      */             }
/*  963 */             fgfa.setTextVor(s);
/*      */ 
/*  965 */             fgfa.setFillPattern(nvl(((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).getFillPattern().name()));
/*      */ 
/*  967 */             fde.getGfa().add(fgfa);
/*  968 */           } else if ((de instanceof Track)) {
/*  969 */             fde.getTrack().add(TrackConverter.getTrackBeanByTrackElement((Track)de));
/*  970 */           } else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet))
/*      */           {
/*  972 */             gov.noaa.nws.ncep.ui.pgen.file.Sigmet sigmet = 
/*  973 */               new gov.noaa.nws.ncep.ui.pgen.file.Sigmet();
/*      */ 
/*  975 */             Calendar localCalendar7 = (pts = de.getColors()).length; for (Calendar localCalendar1 = 0; localCalendar1 < localCalendar7; localCalendar1++) { java.awt.Color clr = pts[localCalendar1];
/*      */ 
/*  977 */               gov.noaa.nws.ncep.ui.pgen.file.Color fclr = 
/*  978 */                 new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/*  980 */               fclr.setRed(clr.getRed());
/*  981 */               fclr.setGreen(clr.getGreen());
/*  982 */               fclr.setBlue(clr.getBlue());
/*  983 */               fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/*  985 */               sigmet.getColor().add(fclr);
/*      */             }
/*      */ 
/*  988 */             Calendar localCalendar8 = (pts = ((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getLinePoints()).length; for (Calendar localCalendar2 = 0; localCalendar2 < localCalendar8; localCalendar2++) { Coordinate crd = pts[localCalendar2];
/*      */ 
/*  990 */               Point fpt = new Point();
/*  991 */               fpt.setLat(Double.valueOf(crd.y));
/*  992 */               fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/*  994 */               sigmet.getPoint().add(fpt);
/*      */             }
/*      */ 
/*  997 */             sigmet.setPgenCategory(de.getPgenCategory());
/*  998 */             sigmet.setLineWidth(Float.valueOf(de.getLineWidth()));
/*  999 */             sigmet.setSizeScale(Double.valueOf(de.getSizeScale()));
/* 1000 */             sigmet.setSmoothFactor(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getSmoothFactor()));
/* 1001 */             sigmet.setClosed(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).isClosedLine());
/* 1002 */             sigmet.setFilled(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).isFilled());
/* 1003 */             sigmet.setPgenType(de.getPgenType());
/* 1004 */             sigmet.setFillPattern(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getFillPattern().name());
/*      */ 
/* 1006 */             sigmet.setType(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getType());
/* 1007 */             sigmet.setWidth(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getWidth()));
/*      */ 
/* 1009 */             sigmet.setEditableAttrArea(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrArea());
/* 1010 */             sigmet.setEditableAttrIssueOffice(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrIssueOffice());
/* 1011 */             sigmet.setEditableAttrStatus(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrStatus());
/* 1012 */             sigmet.setEditableAttrId(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrId());
/* 1013 */             sigmet.setEditableAttrSeqNum(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrSeqNum());
/* 1014 */             sigmet.setEditableAttrStartTime(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrStartTime());
/* 1015 */             sigmet.setEditableAttrEndTime(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrEndTime());
/* 1016 */             sigmet.setEditableAttrRemarks(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrRemarks());
/* 1017 */             sigmet.setEditableAttrPhenom(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenom());
/* 1018 */             sigmet.setEditableAttrPhenom2(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenom2());
/* 1019 */             sigmet.setEditableAttrPhenomName(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomName());
/* 1020 */             sigmet.setEditableAttrPhenomLat(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomLat());
/* 1021 */             sigmet.setEditableAttrPhenomLon(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomLon());
/* 1022 */             sigmet.setEditableAttrPhenomPressure(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomPressure());
/* 1023 */             sigmet.setEditableAttrPhenomMaxWind(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomMaxWind());
/* 1024 */             sigmet.setEditableAttrFreeText(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrFreeText());
/* 1025 */             sigmet.setEditableAttrTrend(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrTrend());
/* 1026 */             sigmet.setEditableAttrMovement(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrMovement());
/* 1027 */             sigmet.setEditableAttrPhenomSpeed(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomSpeed());
/* 1028 */             sigmet.setEditableAttrPhenomDirection(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomDirection());
/* 1029 */             sigmet.setEditableAttrLevel(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrLevel());
/* 1030 */             sigmet.setEditableAttrLevelInfo1(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrLevelInfo1());
/* 1031 */             sigmet.setEditableAttrLevelInfo2(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrLevelInfo2());
/* 1032 */             sigmet.setEditableAttrLevelText1(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrLevelText1());
/* 1033 */             sigmet.setEditableAttrLevelText2(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrLevelText2());
/* 1034 */             sigmet.setEditableAttrFromLine(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrFromLine());
/* 1035 */             sigmet.setEditableAttrFir(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrFir());
/*      */ 
/* 1037 */             fde.getSigmet().add(sigmet);
/*      */           }
/*      */           else
/*      */           {
/* 1041 */             gov.noaa.nws.ncep.ui.pgen.file.Line line = 
/* 1042 */               new gov.noaa.nws.ncep.ui.pgen.file.Line();
/*      */ 
/* 1044 */             Calendar localCalendar9 = (pts = de.getColors()).length; for (Calendar localCalendar3 = 0; localCalendar3 < localCalendar9; localCalendar3++) { java.awt.Color clr = pts[localCalendar3];
/*      */ 
/* 1046 */               gov.noaa.nws.ncep.ui.pgen.file.Color fclr = 
/* 1047 */                 new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/* 1049 */               fclr.setRed(clr.getRed());
/* 1050 */               fclr.setGreen(clr.getGreen());
/* 1051 */               fclr.setBlue(clr.getBlue());
/* 1052 */               fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/* 1054 */               line.getColor().add(fclr);
/*      */             }
/*      */ 
/* 1057 */             Calendar localCalendar10 = (pts = ((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).getLinePoints()).length; for (Calendar localCalendar4 = 0; localCalendar4 < localCalendar10; localCalendar4++) { Coordinate crd = pts[localCalendar4];
/*      */ 
/* 1059 */               Point fpt = new Point();
/* 1060 */               fpt.setLat(Double.valueOf(crd.y));
/* 1061 */               fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/* 1063 */               line.getPoint().add(fpt);
/*      */             }
/*      */ 
/* 1066 */             line.setPgenCategory(de.getPgenCategory());
/* 1067 */             line.setLineWidth(Float.valueOf(de.getLineWidth()));
/* 1068 */             line.setSizeScale(Double.valueOf(de.getSizeScale()));
/* 1069 */             line.setSmoothFactor(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).getSmoothFactor()));
/* 1070 */             line.setClosed(((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).isClosedLine());
/* 1071 */             line.setFilled(((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).isFilled());
/* 1072 */             line.setPgenType(de.getPgenType());
/*      */ 
/* 1074 */             line.setFillPattern(((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).getFillPattern().name());
/* 1075 */             line.setFlipSide(((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).isFlipSide());
/*      */ 
/* 1077 */             fde.getLine().add(line);
/*      */           }
/*      */ 
/*      */         }
/* 1081 */         else if (((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Symbol)) || ((de instanceof ComboSymbol)))
/*      */         {
/* 1083 */           gov.noaa.nws.ncep.ui.pgen.file.Symbol symbol = 
/* 1084 */             new gov.noaa.nws.ncep.ui.pgen.file.Symbol();
/*      */ 
/* 1086 */           Calendar localCalendar11 = (pts = de.getColors()).length; for (Calendar localCalendar5 = 0; localCalendar5 < localCalendar11; localCalendar5++) { java.awt.Color clr = pts[localCalendar5];
/*      */ 
/* 1088 */             gov.noaa.nws.ncep.ui.pgen.file.Color fclr = 
/* 1089 */               new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/* 1091 */             fclr.setRed(clr.getRed());
/* 1092 */             fclr.setGreen(clr.getGreen());
/* 1093 */             fclr.setBlue(clr.getBlue());
/* 1094 */             fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/* 1096 */             symbol.getColor().add(fclr);
/*      */           }
/*      */ 
/* 1099 */           Point fpt = new Point();
/* 1100 */           fpt.setLat(Double.valueOf(((ISinglePoint)de).getLocation().y));
/* 1101 */           fpt.setLon(Double.valueOf(((ISinglePoint)de).getLocation().x));
/* 1102 */           symbol.setPoint(fpt);
/*      */ 
/* 1104 */           symbol.setPgenType(de.getPgenType());
/* 1105 */           symbol.setPgenCategory(de.getPgenCategory());
/* 1106 */           symbol.setLineWidth(Float.valueOf(de.getLineWidth()));
/* 1107 */           symbol.setSizeScale(Double.valueOf(de.getSizeScale()));
/* 1108 */           symbol.setClear(((ISinglePoint)de).isClear());
/*      */ 
/* 1110 */           fde.getSymbol().add(symbol);
/*      */         }
/*      */         else
/*      */         {
/*      */           gov.noaa.nws.ncep.ui.pgen.file.Color fclr;
/* 1112 */           if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.AvnText))
/*      */           {
/* 1114 */             gov.noaa.nws.ncep.ui.pgen.file.AvnText atext = 
/* 1115 */               new gov.noaa.nws.ncep.ui.pgen.file.AvnText();
/*      */ 
/* 1117 */             Calendar localCalendar12 = (pts = de.getColors()).length; for (Calendar localCalendar6 = 0; localCalendar6 < localCalendar12; localCalendar6++) { java.awt.Color clr = pts[localCalendar6];
/*      */ 
/* 1119 */               fclr = 
/* 1120 */                 new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/* 1122 */               fclr.setRed(clr.getRed());
/* 1123 */               fclr.setGreen(clr.getGreen());
/* 1124 */               fclr.setBlue(clr.getBlue());
/* 1125 */               fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 1126 */               atext.getColor().add(fclr);
/*      */             }
/*      */ 
/* 1129 */             Point fpt = new Point();
/* 1130 */             fpt.setLat(Double.valueOf(((ISinglePoint)de).getLocation().y));
/* 1131 */             fpt.setLon(Double.valueOf(((ISinglePoint)de).getLocation().x));
/* 1132 */             atext.setPoint(fpt);
/*      */ 
/* 1134 */             atext.setAvnTextType(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getAvnTextType().name());
/* 1135 */             atext.setTopValue(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getTopValue());
/* 1136 */             atext.setBottomValue(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getBottomValue());
/* 1137 */             atext.setJustification(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getJustification().name());
/* 1138 */             atext.setStyle(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getStyle().name());
/* 1139 */             atext.setFontName(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getFontName());
/* 1140 */             atext.setFontSize(Float.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getFontSize()));
/* 1141 */             atext.setSymbolPatternName(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getSymbolPatternName());
/* 1142 */             atext.setPgenType(de.getPgenType());
/* 1143 */             atext.setPgenCategory(de.getPgenCategory());
/*      */ 
/* 1145 */             fde.getAvnText().add(atext);
/*      */           }
/*      */           else
/*      */           {
/*      */             Object localObject4;
/*      */             Point fpt;
/* 1147 */             if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText))
/*      */             {
/* 1149 */               gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText mcde = (gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText)de;
/*      */ 
/* 1151 */               gov.noaa.nws.ncep.ui.pgen.file.MidCloudText mtext = 
/* 1152 */                 new gov.noaa.nws.ncep.ui.pgen.file.MidCloudText();
/*      */ 
/* 1154 */               localObject4 = (fclr = de.getColors()).length; for (Object localObject1 = 0; localObject1 < localObject4; localObject1++) { java.awt.Color clr = fclr[localObject1];
/*      */ 
/* 1156 */                 gov.noaa.nws.ncep.ui.pgen.file.Color fclr = 
/* 1157 */                   new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/* 1159 */                 fclr.setRed(clr.getRed());
/* 1160 */                 fclr.setGreen(clr.getGreen());
/* 1161 */                 fclr.setBlue(clr.getBlue());
/* 1162 */                 fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 1163 */                 mtext.getColor().add(fclr);
/*      */               }
/*      */ 
/* 1166 */               fpt = new Point();
/* 1167 */               fpt.setLat(Double.valueOf(((ISinglePoint)de).getLocation().y));
/* 1168 */               fpt.setLon(Double.valueOf(((ISinglePoint)de).getLocation().x));
/* 1169 */               mtext.setPoint(fpt);
/*      */ 
/* 1171 */               mtext.setCloudTypes(mcde.getCloudTypes());
/* 1172 */               mtext.setCloudAmounts(mcde.getCloudAmounts());
/* 1173 */               mtext.setTurbulenceType(mcde.getTurbulencePattern());
/* 1174 */               mtext.setTurbulenceLevels(mcde.getTurbulenceLevels());
/* 1175 */               mtext.setIcingType(mcde.getIcingPattern());
/* 1176 */               mtext.setIcingLevels(mcde.getIcingLevels());
/* 1177 */               mtext.setTstormTypes(mcde.getTstormTypes());
/* 1178 */               mtext.setTstormLevels(mcde.getTstormLevels());
/*      */ 
/* 1180 */               mtext.setJustification(mcde.getJustification().name());
/* 1181 */               mtext.setStyle(mcde.getStyle().name());
/* 1182 */               mtext.setFontName(mcde.getFontName());
/* 1183 */               mtext.setFontSize(Float.valueOf(mcde.getFontSize()));
/* 1184 */               mtext.setPgenType(mcde.getPgenType());
/* 1185 */               mtext.setPgenCategory(mcde.getPgenCategory());
/*      */ 
/* 1187 */               fde.getMidCloudText().add(mtext);
/*      */             }
/*      */             else
/*      */             {
/*      */               Object localObject5;
/*      */               String st;
/* 1189 */               if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text))
/*      */               {
/* 1191 */                 gov.noaa.nws.ncep.ui.pgen.file.Text text = 
/* 1192 */                   new gov.noaa.nws.ncep.ui.pgen.file.Text();
/*      */ 
/* 1194 */                 Point localPoint2 = (localObject4 = de.getColors()).length;
/*      */                 gov.noaa.nws.ncep.ui.pgen.file.Color fclr;
/* 1194 */                 for (Point localPoint1 = 0; localPoint1 < localPoint2; localPoint1++) { java.awt.Color clr = localObject4[localPoint1];
/*      */ 
/* 1196 */                   fclr = 
/* 1197 */                     new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/* 1199 */                   fclr.setRed(clr.getRed());
/* 1200 */                   fclr.setGreen(clr.getGreen());
/* 1201 */                   fclr.setBlue(clr.getBlue());
/* 1202 */                   fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 1203 */                   text.getColor().add(fclr);
/*      */                 }
/*      */ 
/* 1206 */                 Point fpt = new Point();
/* 1207 */                 fpt.setLat(Double.valueOf(((ISinglePoint)de).getLocation().y));
/* 1208 */                 fpt.setLon(Double.valueOf(((ISinglePoint)de).getLocation().x));
/* 1209 */                 text.setPoint(fpt);
/*      */ 
/* 1211 */                 localObject5 = (fclr = ((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getString()).length; for (Object localObject2 = 0; localObject2 < localObject5; localObject2++) { st = fclr[localObject2];
/* 1212 */                   text.getTextLine().add(new String(st));
/*      */                 }
/*      */ 
/* 1215 */                 text.setXOffset(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getXOffset()));
/* 1216 */                 text.setYOffset(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getYOffset()));
/* 1217 */                 text.setDisplayType(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getDisplayType().name());
/* 1218 */                 text.setMask(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).maskText());
/* 1219 */                 text.setRotationRelativity(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getRotationRelativity().name());
/* 1220 */                 text.setRotation(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getRotation()));
/* 1221 */                 text.setJustification(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getJustification().name());
/* 1222 */                 text.setStyle(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getStyle().name());
/* 1223 */                 text.setFontName(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getFontName());
/* 1224 */                 text.setFontSize(Float.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getFontSize()));
/* 1225 */                 text.setPgenType(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getPgenType());
/* 1226 */                 text.setPgenCategory(de.getPgenCategory());
/*      */ 
/* 1228 */                 text.setHide(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getHide());
/* 1229 */                 text.setAuto(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getAuto());
/*      */ 
/* 1231 */                 fde.getText().add(text);
/*      */               }
/* 1234 */               else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Vector))
/*      */               {
/* 1236 */                 gov.noaa.nws.ncep.ui.pgen.file.Vector vector = 
/* 1237 */                   new gov.noaa.nws.ncep.ui.pgen.file.Vector();
/*      */ 
/* 1239 */                 String str2 = (localObject5 = de.getColors()).length; for (String str1 = 0; str1 < str2; str1++) { java.awt.Color clr = localObject5[str1];
/*      */ 
/* 1241 */                   gov.noaa.nws.ncep.ui.pgen.file.Color fclr = 
/* 1242 */                     new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/* 1244 */                   fclr.setRed(clr.getRed());
/* 1245 */                   fclr.setGreen(clr.getGreen());
/* 1246 */                   fclr.setBlue(clr.getBlue());
/* 1247 */                   fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/* 1249 */                   vector.getColor().add(fclr);
/*      */                 }
/*      */ 
/* 1252 */                 Point fpt = new Point();
/* 1253 */                 fpt.setLat(Double.valueOf(((ISinglePoint)de).getLocation().y));
/* 1254 */                 fpt.setLon(Double.valueOf(((ISinglePoint)de).getLocation().x));
/* 1255 */                 vector.setPoint(fpt);
/*      */ 
/* 1257 */                 vector.setPgenType(de.getPgenType());
/* 1258 */                 vector.setPgenCategory(de.getPgenCategory());
/*      */ 
/* 1260 */                 vector.setLineWidth(Float.valueOf(de.getLineWidth()));
/* 1261 */                 vector.setSizeScale(Double.valueOf(de.getSizeScale()));
/* 1262 */                 vector.setClear(((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).isClear());
/*      */ 
/* 1264 */                 vector.setDirection(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).getDirection()));
/* 1265 */                 vector.setSpeed(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).getSpeed()));
/* 1266 */                 vector.setArrowHeadSize(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).getArrowHeadSize()));
/* 1267 */                 vector.setDirectionOnly(Boolean.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).hasDirectionOnly()));
/*      */ 
/* 1269 */                 fde.getVector().add(vector);
/*      */               }
/* 1272 */               else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.WatchBox)) {
/* 1273 */                 fde.getWatchBox().add(convertWatchBox2XML((gov.noaa.nws.ncep.ui.pgen.elements.WatchBox)de));
/*      */               }
/* 1275 */               else if ((de instanceof Tcm)) {
/* 1276 */                 fde.getTcm().add(convertTcm2XML((Tcm)de));
/*      */               }
/* 1278 */               else if ((de instanceof TCAElement))
/*      */               {
/* 1280 */                 TCAElement tcaEl = (TCAElement)de;
/*      */ 
/* 1282 */                 TCA tca = 
/* 1283 */                   new TCA();
/*      */ 
/* 1303 */                 tca.setPgenType(tcaEl.getPgenType());
/* 1304 */                 tca.setPgenCategory(tcaEl.getPgenCategory());
/*      */ 
/* 1306 */                 tca.setStormNumber(Integer.valueOf(tcaEl.getStormNumber()));
/* 1307 */                 tca.setStormName(tcaEl.getStormName());
/* 1308 */                 tca.setBasin(tcaEl.getBasin());
/* 1309 */                 tca.setIssueStatus(tcaEl.getIssueStatus());
/* 1310 */                 tca.setStormType(tcaEl.getStormType());
/* 1311 */                 tca.setAdvisoryNumber(tcaEl.getAdvisoryNumber());
/*      */ 
/* 1313 */                 tca.setTimeZone(tcaEl.getTimeZone());
/* 1314 */                 tca.setTextLocation(tcaEl.getTextLocation());
/*      */ 
/* 1316 */                 Calendar advTime = tcaEl.getAdvisoryTime();
/* 1317 */                 XMLGregorianCalendar xmlCal = null;
/*      */                 try {
/* 1319 */                   xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 1320 */                     advTime.get(1), 
/* 1321 */                     advTime.get(2) + 1, 
/* 1322 */                     advTime.get(5), 
/* 1323 */                     advTime.get(11), 
/* 1324 */                     advTime.get(12), 
/* 1325 */                     advTime.get(13), 
/* 1326 */                     advTime.get(14), 
/* 1327 */                     0);
/*      */                 }
/*      */                 catch (DatatypeConfigurationException e) {
/* 1330 */                   e.printStackTrace();
/*      */                 }
/* 1332 */                 tca.setAdvisoryTime(xmlCal);
/*      */ 
/* 1334 */                 tca.setAdvisories(tcaEl.getAdvisories());
/*      */ 
/* 1336 */                 fde.getTCA().add(tca);
/*      */               }
/* 1338 */               else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano)) {
/* 1339 */                 fde.getVolcano().add(convertVolcano2XML((gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano)de)); } 
/*      */             }
/*      */           }
/*      */         } } else if ((adc instanceof gov.noaa.nws.ncep.ui.pgen.elements.DECollection))
/*      */       {
/* 1344 */         if (adc.getName().equalsIgnoreCase("Contours")) {
/* 1345 */           fde.getContours().add(convertContours2XML((gov.noaa.nws.ncep.ui.pgen.contours.Contours)adc));
/*      */         }
/* 1347 */         else if (adc.getName().equalsIgnoreCase("Outlook")) {
/* 1348 */           fde.getOutlook().add(convertOutlook2XML((gov.noaa.nws.ncep.ui.pgen.elements.Outlook)adc));
/*      */         }
/*      */         else {
/* 1351 */           fde.getDECollection().add(convertDECollection2XML((gov.noaa.nws.ncep.ui.pgen.elements.DECollection)adc));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1356 */     return fde;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.file.DECollection convertDECollection2XML(gov.noaa.nws.ncep.ui.pgen.elements.DECollection dec)
/*      */   {
/* 1366 */     gov.noaa.nws.ncep.ui.pgen.file.DECollection fdec = 
/* 1367 */       new gov.noaa.nws.ncep.ui.pgen.file.DECollection();
/*      */ 
/* 1369 */     String cName = dec.getCollectionName();
/* 1370 */     if (cName != null) {
/* 1371 */       fdec.setCollectionName(cName);
/*      */     }
/*      */ 
/* 1374 */     fdec.setPgenType(dec.getPgenType());
/* 1375 */     fdec.setPgenCategory(dec.getPgenCategory());
/*      */ 
/* 1377 */     List componentList = new ArrayList();
/* 1378 */     Iterator it = dec.getComponentIterator();
/*      */ 
/* 1380 */     while (it.hasNext()) {
/* 1381 */       componentList.add((AbstractDrawableComponent)it.next());
/*      */     }
/*      */ 
/* 1384 */     fdec.setDrawableElement(convertDEs(componentList));
/*      */ 
/* 1386 */     return fdec;
/*      */   }
/*      */ 
/*      */   private static Jet convertXML2Jet(gov.noaa.nws.ncep.ui.pgen.file.DECollection dec)
/*      */   {
/* 1396 */     Jet jet = new Jet();
/* 1397 */     jet.setPgenCategory(dec.getPgenCategory());
/* 1398 */     jet.setPgenType(dec.getPgenType());
/*      */ 
/* 1400 */     jet.remove(jet.getJetLine());
/* 1401 */     gov.noaa.nws.ncep.ui.pgen.file.DrawableElement elem = dec.getDrawableElement();
/*      */ 
/* 1403 */     if (elem.getLine() != null)
/* 1404 */       jet.add(convertJetLine((gov.noaa.nws.ncep.ui.pgen.file.Line)elem.getLine().get(0), jet));
/*      */     else {
/* 1406 */       return null;
/*      */     }
/* 1408 */     if (elem.getVector() != null) {
/* 1409 */       for (gov.noaa.nws.ncep.ui.pgen.file.Vector fVector : elem.getVector()) {
/* 1410 */         jet.add(convertJetHash(fVector, jet));
/*      */       }
/*      */     }
/*      */ 
/* 1414 */     if (elem.getDECollection() != null) {
/* 1415 */       for (gov.noaa.nws.ncep.ui.pgen.file.DECollection fdec : elem.getDECollection()) {
/* 1416 */         if (fdec.getCollectionName().equalsIgnoreCase("WindInfo")) {
/* 1417 */           gov.noaa.nws.ncep.ui.pgen.elements.DECollection wind = new gov.noaa.nws.ncep.ui.pgen.elements.DECollection("WindInfo");
/*      */ 
/* 1419 */           gov.noaa.nws.ncep.ui.pgen.file.DrawableElement de = fdec.getDrawableElement();
/* 1420 */           if (de.getVector() != null) {
/* 1421 */             wind.add(convertJetBarb((gov.noaa.nws.ncep.ui.pgen.file.Vector)de.getVector().get(0), jet));
/*      */           }
/*      */ 
/* 1424 */           if (de.getText() != null) {
/* 1425 */             wind.add(convertJetFL((gov.noaa.nws.ncep.ui.pgen.file.Text)de.getText().get(0), jet));
/*      */           }
/*      */ 
/* 1428 */           jet.add(wind);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1434 */     return jet;
/*      */   }
/*      */ 
/*      */   private static Jet.JetLine convertJetLine(gov.noaa.nws.ncep.ui.pgen.file.Line jetLine, Jet jet)
/*      */   {
/* 1443 */     java.awt.Color[] clr = new java.awt.Color[jetLine.getColor().size()];
/* 1444 */     int nn = 0;
/* 1445 */     for (gov.noaa.nws.ncep.ui.pgen.file.Color fColor : jetLine.getColor()) {
/* 1446 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 1447 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/*      */ 
/* 1450 */     ArrayList linePoints = new ArrayList();
/* 1451 */     nn = 0;
/* 1452 */     for (Point pt : jetLine.getPoint())
/* 1453 */       linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */     Jet tmp168_167 = jet; tmp168_167.getClass(); Jet.JetLine newLine = new Jet.JetLine(tmp168_167, null, clr, 
/* 1457 */       jetLine.getLineWidth().floatValue(), jetLine.getSizeScale().doubleValue(), jetLine.isClosed().booleanValue(), 
/* 1458 */       jetLine.isFilled().booleanValue(), linePoints, 
/* 1459 */       jetLine.getSmoothFactor().intValue(), 
/* 1460 */       FillPatternList.FillPattern.valueOf(jetLine.getFillPattern()), 
/* 1461 */       jetLine.getPgenCategory(), jetLine.getPgenType());
/*      */ 
/* 1463 */     newLine.setParent(jet);
/*      */ 
/* 1465 */     return newLine;
/*      */   }
/*      */ 
/*      */   private static Jet.JetHash convertJetHash(gov.noaa.nws.ncep.ui.pgen.file.Vector fVector, Jet jet)
/*      */   {
/* 1475 */     java.awt.Color[] clr = new java.awt.Color[fVector.getColor().size()];
/* 1476 */     int nn = 0;
/* 1477 */     for (gov.noaa.nws.ncep.ui.pgen.file.Color fColor : fVector.getColor()) {
/* 1478 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 1479 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/*      */ 
/* 1482 */     Point loc = fVector.getPoint();
/*      */ 
/* 1484 */     IVector.VectorType vtype = null;
/* 1485 */     String pgenType = fVector.getPgenType();
/*      */ 
/* 1487 */     if (pgenType.equalsIgnoreCase("Hash"))
/* 1488 */       vtype = IVector.VectorType.HASH_MARK;
/*      */     Jet tmp123_122 = jet; tmp123_122.getClass(); Jet.JetHash hash = new Jet.JetHash(tmp123_122, null, clr, 
/* 1491 */       fVector.getLineWidth().floatValue(), fVector.getSizeScale().doubleValue(), 
/* 1492 */       fVector.isClear(), new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 1493 */       vtype, fVector.getSpeed().doubleValue(), fVector.getDirection().doubleValue(), 
/* 1494 */       fVector.getArrowHeadSize().doubleValue(), fVector.isDirectionOnly().booleanValue(), 
/* 1495 */       fVector.getPgenCategory(), fVector.getPgenType());
/* 1496 */     hash.setParent(jet);
/*      */ 
/* 1498 */     return hash;
/*      */   }
/*      */ 
/*      */   private static Jet.JetBarb convertJetBarb(gov.noaa.nws.ncep.ui.pgen.file.Vector fVector, Jet jet)
/*      */   {
/* 1508 */     java.awt.Color[] clr = new java.awt.Color[fVector.getColor().size()];
/* 1509 */     int nn = 0;
/* 1510 */     for (gov.noaa.nws.ncep.ui.pgen.file.Color fColor : fVector.getColor()) {
/* 1511 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 1512 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/*      */ 
/* 1515 */     Point loc = fVector.getPoint();
/*      */ 
/* 1517 */     IVector.VectorType vtype = null;
/* 1518 */     String pgenType = fVector.getPgenType();
/*      */ 
/* 1520 */     if (pgenType.equalsIgnoreCase("Barb"))
/* 1521 */       vtype = IVector.VectorType.WIND_BARB;
/*      */     Jet tmp123_122 = jet; tmp123_122.getClass(); Jet.JetBarb barb = new Jet.JetBarb(tmp123_122, null, clr, 
/* 1524 */       fVector.getLineWidth().floatValue(), fVector.getSizeScale().doubleValue(), 
/* 1525 */       fVector.isClear(), new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 1526 */       vtype, fVector.getSpeed().doubleValue(), fVector.getDirection().doubleValue(), 
/* 1527 */       fVector.getArrowHeadSize().doubleValue(), fVector.isDirectionOnly().booleanValue(), 
/* 1528 */       fVector.getPgenCategory(), fVector.getPgenType());
/* 1529 */     barb.setParent(jet);
/* 1530 */     return barb;
/*      */   }
/*      */ 
/*      */   private static Jet.JetText convertJetFL(gov.noaa.nws.ncep.ui.pgen.file.Text fText, Jet aJet)
/*      */   {
/* 1539 */     java.awt.Color[] clr = new java.awt.Color[fText.getColor().size()];
/* 1540 */     int nn = 0;
/* 1541 */     for (gov.noaa.nws.ncep.ui.pgen.file.Color fColor : fText.getColor()) {
/* 1542 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 1543 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/*      */ 
/* 1546 */     Point loc = fText.getPoint();
/*      */ 
/* 1548 */     String[] st = new String[fText.getTextLine().size()];
/*      */ 
/* 1550 */     int nline = 0;
/* 1551 */     for (String str : fText.getTextLine())
/* 1552 */       st[(nline++)] = str;
/*      */     Jet tmp161_160 = aJet; tmp161_160.getClass(); Jet.JetText text = new Jet.JetText(tmp161_160, null, fText.getFontName(), 
/* 1556 */       fText.getFontSize().floatValue(), 
/* 1557 */       IText.TextJustification.valueOf(fText.getJustification()), 
/* 1558 */       new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 1559 */       fText.getRotation().doubleValue(), 
/* 1560 */       IText.TextRotation.valueOf(fText.getRotationRelativity()), 
/* 1561 */       st, 
/* 1562 */       IText.FontStyle.valueOf(fText.getStyle()), clr[0], 0, 0, 
/* 1563 */       fText.isMask().booleanValue(), 
/* 1564 */       IText.DisplayType.valueOf(fText.getDisplayType()), 
/* 1565 */       fText.getPgenCategory(), fText.getPgenType());
/*      */ 
/* 1567 */     text.setLatLonFlag(true);
/*      */ 
/* 1569 */     return text;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.file.Contours convertContours2XML(gov.noaa.nws.ncep.ui.pgen.contours.Contours cnt)
/*      */   {
/* 1579 */     gov.noaa.nws.ncep.ui.pgen.file.Contours contours = 
/* 1580 */       new gov.noaa.nws.ncep.ui.pgen.file.Contours();
/*      */ 
/* 1582 */     contours.setCollectionName("Contours");
/* 1583 */     contours.setPgenType("Contours");
/* 1584 */     contours.setPgenCategory("MET");
/*      */ 
/* 1586 */     contours.setParm(cnt.getParm());
/* 1587 */     contours.setLevel(cnt.getLevel());
/* 1588 */     contours.setCint(cnt.getCint());
/* 1589 */     if (cnt.getForecastHour() != null) {
/* 1590 */       contours.setForecastHour(cnt.getForecastHour());
/*      */     }
/*      */ 
/* 1593 */     Calendar cntTime = cnt.getTime1();
/* 1594 */     XMLGregorianCalendar xmlCal = null;
/*      */     try {
/* 1596 */       xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 1597 */         cntTime.get(1), 
/* 1598 */         cntTime.get(2) + 1, 
/* 1599 */         cntTime.get(5), 
/* 1600 */         cntTime.get(11), 
/* 1601 */         cntTime.get(12), 
/* 1602 */         cntTime.get(13), 
/* 1603 */         cntTime.get(14), 
/* 1604 */         0);
/*      */     } catch (DatatypeConfigurationException e) {
/* 1606 */       e.printStackTrace();
/*      */     }
/*      */ 
/* 1609 */     contours.setTime1(xmlCal);
/*      */ 
/* 1611 */     Calendar cntTime2 = cnt.getTime2();
/* 1612 */     XMLGregorianCalendar xmlCal2 = null;
/*      */     try {
/* 1614 */       xmlCal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 1615 */         cntTime2.get(1), 
/* 1616 */         cntTime2.get(2) + 1, 
/* 1617 */         cntTime2.get(5), 
/* 1618 */         cntTime2.get(11), 
/* 1619 */         cntTime2.get(12), 
/* 1620 */         cntTime2.get(13), 
/* 1621 */         cntTime2.get(14), 
/* 1622 */         0);
/*      */     } catch (DatatypeConfigurationException e) {
/* 1624 */       e.printStackTrace();
/*      */     }
/*      */ 
/* 1627 */     contours.setTime2(xmlCal2);
/*      */ 
/* 1629 */     Iterator it = cnt.getComponentIterator();
/* 1630 */     int ii = 0;
/* 1631 */     while (it.hasNext())
/*      */     {
/* 1633 */       AbstractDrawableComponent next = (AbstractDrawableComponent)it.next();
/*      */ 
/* 1635 */       if ((next instanceof gov.noaa.nws.ncep.ui.pgen.elements.DECollection)) {
/* 1636 */         contours.getDECollection().add(
/* 1637 */           convertDECollection2XML((gov.noaa.nws.ncep.ui.pgen.elements.DECollection)next));
/*      */       }
/*      */ 
/* 1640 */       ii++;
/*      */     }
/*      */ 
/* 1643 */     return contours;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.contours.Contours convertXML2Contours(gov.noaa.nws.ncep.ui.pgen.file.Contours cnt)
/*      */   {
/* 1654 */     gov.noaa.nws.ncep.ui.pgen.contours.Contours contours = new gov.noaa.nws.ncep.ui.pgen.contours.Contours("Contours");
/*      */ 
/* 1656 */     contours.setPgenType(cnt.getPgenType());
/* 1657 */     contours.setPgenCategory(cnt.getPgenCategory());
/*      */ 
/* 1659 */     contours.setParm(cnt.getParm());
/* 1660 */     contours.setLevel(cnt.getLevel());
/* 1661 */     contours.setCint(cnt.getCint());
/*      */ 
/* 1663 */     if (cnt.getForecastHour() != null) {
/* 1664 */       contours.setForecastHour(cnt.getForecastHour());
/*      */     }
/*      */ 
/* 1667 */     Calendar cntTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 1668 */     XMLGregorianCalendar xmlCal = cnt.getTime1();
/* 1669 */     if (xmlCal != null)
/* 1670 */       cntTime.set(xmlCal.getYear(), xmlCal.getMonth() - 1, xmlCal.getDay(), 
/* 1671 */         xmlCal.getHour(), xmlCal.getMinute(), xmlCal.getSecond());
/* 1672 */     contours.setTime1(cntTime);
/*      */ 
/* 1674 */     Calendar cntTime2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 1675 */     XMLGregorianCalendar xmlCal2 = cnt.getTime2();
/* 1676 */     if (xmlCal2 != null)
/* 1677 */       cntTime2.set(xmlCal2.getYear(), xmlCal2.getMonth() - 1, xmlCal2.getDay(), 
/* 1678 */         xmlCal2.getHour(), xmlCal2.getMinute(), xmlCal2.getSecond());
/* 1679 */     contours.setTime2(cntTime2);
/*      */ 
/* 1681 */     for (gov.noaa.nws.ncep.ui.pgen.file.DECollection fdec : cnt.getDECollection())
/*      */     {
/*      */       int numOfLabels;
/* 1683 */       if (fdec.getCollectionName().equals("ContourLine")) {
/* 1684 */         ContourLine contourLine = new ContourLine();
/*      */ 
/* 1686 */         List delist = convert(fdec.getDrawableElement());
/* 1687 */         String[] labelString = null;
/* 1688 */         numOfLabels = 0;
/*      */ 
/* 1690 */         for (AbstractDrawableComponent de : delist)
/*      */         {
/* 1692 */           de.setParent(contourLine);
/* 1693 */           contourLine.add(de);
/*      */ 
/* 1695 */           if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text)) {
/* 1696 */             numOfLabels++;
/*      */ 
/* 1698 */             if (labelString == null) {
/* 1699 */               labelString = ((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getText();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1706 */         contourLine.setNumOfLabels(numOfLabels);
/* 1707 */         contourLine.setLabelString(labelString);
/*      */ 
/* 1709 */         contourLine.setParent(contours);
/*      */ 
/* 1711 */         contours.add(contourLine);
/*      */       }
/* 1713 */       else if (fdec.getCollectionName().equals("ContourMinmax"))
/*      */       {
/* 1715 */         ContourMinmax contourMinmax = new ContourMinmax();
/*      */ 
/* 1717 */         List delist = convert(fdec.getDrawableElement());
/* 1718 */         for (AbstractDrawableComponent de : delist)
/*      */         {
/* 1720 */           de.setParent(contourMinmax);
/* 1721 */           contourMinmax.add(de);
/*      */         }
/*      */ 
/* 1725 */         contourMinmax.setParent(contours);
/*      */ 
/* 1727 */         contours.add(contourMinmax);
/*      */       }
/* 1729 */       else if (fdec.getCollectionName().equals("ContourCircle"))
/*      */       {
/* 1731 */         ContourCircle contourCircle = new ContourCircle();
/*      */ 
/* 1733 */         List delist = convert(fdec.getDrawableElement());
/* 1734 */         for (AbstractDrawableComponent de : delist)
/*      */         {
/* 1736 */           de.setParent(contourCircle);
/* 1737 */           contourCircle.add(de);
/*      */         }
/*      */ 
/* 1741 */         contourCircle.setParent(contours);
/*      */ 
/* 1743 */         contours.add(contourCircle);
/*      */       }
/*      */     }
/*      */ 
/* 1747 */     return contours;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.file.WatchBox convertWatchBox2XML(gov.noaa.nws.ncep.ui.pgen.elements.WatchBox wb)
/*      */   {
/* 1757 */     gov.noaa.nws.ncep.ui.pgen.file.WatchBox fwb = 
/* 1758 */       new gov.noaa.nws.ncep.ui.pgen.file.WatchBox();
/*      */ 
/* 1760 */     fwb.setPgenCategory(wb.getPgenCategory());
/* 1761 */     fwb.setPgenType(wb.getPgenType());
/* 1762 */     fwb.setBoxShape(wb.getBoxShape());
/* 1763 */     fwb.setFillFlag(Boolean.valueOf(wb.getFillFlag()));
/* 1764 */     fwb.setSymbolSize(Double.valueOf(wb.getWatchSymbolSize()));
/* 1765 */     fwb.setSymbolWidth(Float.valueOf(wb.getWatchSymbolWidth()));
/* 1766 */     fwb.setSymbolType(wb.getWatchSymbolType());
/*      */ 
/* 1769 */     fwb.setIssueStatus(wb.getIssueStatus());
/*      */ 
/* 1771 */     Calendar issueTime = wb.getIssueTime();
/* 1772 */     if (issueTime != null) {
/* 1773 */       XMLGregorianCalendar xmlIssueCal = null;
/*      */       try {
/* 1775 */         xmlIssueCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 1776 */           issueTime.get(1), 
/* 1777 */           issueTime.get(2) + 1, 
/* 1778 */           issueTime.get(5), 
/* 1779 */           issueTime.get(11), 
/* 1780 */           issueTime.get(12), 
/* 1781 */           issueTime.get(13), 
/* 1782 */           issueTime.get(14), 
/* 1783 */           0);
/*      */       }
/*      */       catch (DatatypeConfigurationException e) {
/* 1786 */         e.printStackTrace();
/*      */       }
/* 1788 */       fwb.setIssueTime(xmlIssueCal);
/*      */     }
/*      */ 
/* 1791 */     Calendar expTime = wb.getExpTime();
/* 1792 */     if (expTime != null) {
/* 1793 */       XMLGregorianCalendar xmlExpCal = null;
/*      */       try {
/* 1795 */         xmlExpCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 1796 */           expTime.get(1), 
/* 1797 */           expTime.get(2) + 1, 
/* 1798 */           expTime.get(5), 
/* 1799 */           expTime.get(11), 
/* 1800 */           expTime.get(12), 
/* 1801 */           expTime.get(13), 
/* 1802 */           expTime.get(14), 
/* 1803 */           0);
/*      */       }
/*      */       catch (DatatypeConfigurationException e) {
/* 1806 */         e.printStackTrace();
/*      */       }
/* 1808 */       fwb.setExpTime(xmlExpCal);
/*      */     }
/*      */ 
/* 1811 */     fwb.setSeverity(wb.getSeverity());
/* 1812 */     fwb.setTimeZone(wb.getTimeZone());
/* 1813 */     fwb.setHailSize(Float.valueOf(wb.getHailSize()));
/* 1814 */     fwb.setGust(Integer.valueOf(wb.getGust()));
/* 1815 */     fwb.setTop(Integer.valueOf(wb.getTop()));
/* 1816 */     fwb.setMoveDir(Integer.valueOf(wb.getMoveDir()));
/* 1817 */     fwb.setMoveSpeed(Integer.valueOf(wb.getMoveSpeed()));
/* 1818 */     fwb.setAdjAreas(wb.getAdjAreas());
/* 1819 */     fwb.setReplWatch(Integer.valueOf(wb.getReplWatch()));
/* 1820 */     fwb.setContWatch(wb.getContWatch());
/* 1821 */     fwb.setIssueFlag(Integer.valueOf(wb.getIssueFlag()));
/* 1822 */     fwb.setWatchType(wb.getWatchType());
/* 1823 */     fwb.setForecaster(wb.getForecaster());
/* 1824 */     fwb.setWatchNumber(Integer.valueOf(wb.getWatchNumber()));
/* 1825 */     fwb.setEndPointAnc(wb.getEndPointAnc());
/* 1826 */     fwb.setEndPointVor(wb.getEndPointVor());
/* 1827 */     fwb.setHalfWidthNm(Integer.valueOf(wb.getHalfWidthNm()));
/* 1828 */     fwb.setHalfWidthSm(Integer.valueOf(wb.getHalfWidthSm()));
/* 1829 */     fwb.setWatchAreaNm(Integer.valueOf(wb.getWathcAreaNm()));
/*      */     java.awt.Color[] arrayOfColor;
/* 1832 */     DatatypeConfigurationException localDatatypeConfigurationException1 = (arrayOfColor = wb.getColors()).length;
/*      */     gov.noaa.nws.ncep.ui.pgen.file.Color fclr;
/* 1832 */     for (e = 0; e < localDatatypeConfigurationException1; e++) { java.awt.Color clr = arrayOfColor[e];
/*      */ 
/* 1834 */       fclr = 
/* 1835 */         new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/* 1837 */       fclr.setRed(clr.getRed());
/* 1838 */       fclr.setGreen(clr.getGreen());
/* 1839 */       fclr.setBlue(clr.getBlue());
/* 1840 */       fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 1841 */       fwb.getColor().add(fclr);
/*      */     }
/*      */ 
/* 1845 */     java.awt.Color fColor = wb.getFillColor();
/*      */     ColorType ct;
/* 1846 */     if (fColor != null) {
/* 1847 */       gov.noaa.nws.ncep.ui.pgen.file.Color fill = 
/* 1848 */         new gov.noaa.nws.ncep.ui.pgen.file.Color();
/* 1849 */       fill.setRed(fColor.getRed());
/* 1850 */       fill.setGreen(fColor.getGreen());
/* 1851 */       fill.setBlue(fColor.getBlue());
/* 1852 */       fill.setAlpha(Integer.valueOf(fColor.getAlpha()));
/* 1853 */       ct = new ColorType();
/* 1854 */       ct.setColor(fill);
/* 1855 */       fwb.setFillColor(ct);
/*      */     }
/*      */ 
/* 1859 */     ColorType localColorType2 = (fclr = wb.getLinePoints()).length; for (ColorType localColorType1 = 0; localColorType1 < localColorType2; localColorType1++) { Coordinate crd = fclr[localColorType1];
/*      */ 
/* 1861 */       Point fpt = new Point();
/* 1862 */       fpt.setLat(Double.valueOf(crd.y));
/* 1863 */       fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/* 1865 */       fwb.getPoint().add(fpt);
/*      */     }
/*      */ 
/* 1869 */     ColorType localColorType3 = (fclr = wb.getAnchors()).length; for (Object localObject1 = 0; localObject1 < localColorType3; localObject1++) { Station stn = fclr[localObject1];
/* 1870 */       fwb.getAnchorPoints().add(stn.getStid() + " " + stn.getState() + " " + stn.getStnname());
/*      */     }
/*      */ 
/* 1874 */     for (localObject1 = wb.getCountyList().iterator(); ((Iterator)localObject1).hasNext(); ) { SPCCounty cnty = (SPCCounty)((Iterator)localObject1).next();
/*      */ 
/* 1876 */       cntyName = "";
/* 1877 */       if (cnty.getName() != null) {
/* 1878 */         cntyName = cnty.getName().replaceAll("City of ", "").replaceAll(" City", "");
/*      */       }
/*      */ 
/* 1881 */       fwb.getCounties().add(String.format("%1$-7s%2$-5s%3$-12s%4$6.2f%5$8.2f%6$7s%7$5s %8$s", new Object[] { 
/* 1882 */         cnty.getUgcId(), cnty.getState(), cntyName, 
/* 1883 */         Double.valueOf(cnty.getCentriod().y), Double.valueOf(cnty.getCentriod().x), cnty.getFips(), 
/* 1884 */         cnty.getWfo(), cnty.getZoneName().toUpperCase() }));
/*      */     }
/*      */ 
/* 1888 */     for (localObject1 = wb.getStates().iterator(); ((Iterator)localObject1).hasNext(); ) { String str = (String)((Iterator)localObject1).next();
/* 1889 */       fwb.getStates().add(str);
/*      */     }
/*      */ 
/* 1893 */     String wfoStr = "";
/* 1894 */     for (Object cntyName = wb.getWFOs().iterator(); ((Iterator)cntyName).hasNext(); ) { String str = (String)((Iterator)cntyName).next();
/* 1895 */       wfoStr = wfoStr + str + "...";
/*      */     }
/* 1897 */     fwb.setWfos(wfoStr);
/*      */ 
/* 1900 */     if (wb.getStatusHistory() != null) {
/* 1901 */       for (cntyName = wb.getStatusHistory().iterator(); ((Iterator)cntyName).hasNext(); ) { WatchBox.WatchStatus ws = (WatchBox.WatchStatus)((Iterator)cntyName).next();
/*      */ 
/* 1903 */         WatchBox.Status fws = new WatchBox.Status();
/*      */ 
/* 1905 */         fws.setFromLine(ws.getFromLine());
/* 1906 */         fws.setStatusForecaster(ws.getStatusForecaster());
/* 1907 */         fws.setMesoDiscussionNumber(Integer.valueOf(ws.getDiscussion()));
/*      */ 
/* 1909 */         Calendar statusValidTime = ws.getStatusValidTime();
/* 1910 */         if (statusValidTime != null) {
/* 1911 */           XMLGregorianCalendar xmlCal = null;
/*      */           try {
/* 1913 */             xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 1914 */               statusValidTime.get(1), 
/* 1915 */               statusValidTime.get(2) + 1, 
/* 1916 */               statusValidTime.get(5), 
/* 1917 */               statusValidTime.get(11), 
/* 1918 */               statusValidTime.get(12), 
/* 1919 */               statusValidTime.get(13), 
/* 1920 */               statusValidTime.get(14), 
/* 1921 */               0);
/*      */           }
/*      */           catch (DatatypeConfigurationException e)
/*      */           {
/* 1925 */             e.printStackTrace();
/*      */           }
/* 1927 */           fws.setStatusValidTime(xmlCal);
/*      */         }
/*      */ 
/* 1930 */         Calendar statusExpTime = ws.getStatusValidTime();
/* 1931 */         if (statusExpTime != null) {
/* 1932 */           XMLGregorianCalendar xmlCal = null;
/*      */           try {
/* 1934 */             xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 1935 */               statusExpTime.get(1), 
/* 1936 */               statusExpTime.get(2) + 1, 
/* 1937 */               statusExpTime.get(5), 
/* 1938 */               statusExpTime.get(11), 
/* 1939 */               statusExpTime.get(12), 
/* 1940 */               statusExpTime.get(13), 
/* 1941 */               statusExpTime.get(14), 
/* 1942 */               0);
/*      */           }
/*      */           catch (DatatypeConfigurationException e)
/*      */           {
/* 1946 */             e.printStackTrace();
/*      */           }
/* 1948 */           fws.setStatusExpTime(xmlCal);
/*      */         }
/*      */ 
/* 1951 */         fwb.getStatus().add(fws);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1957 */     Geometry union = wb.getCountyUnion();
/*      */ 
/* 1959 */     if (union != null)
/*      */     {
/* 1961 */       for (int ii = 0; ii < union.getNumGeometries(); ii++)
/*      */       {
/* 1964 */         Polygon poly = (Polygon)union.getGeometryN(ii);
/*      */ 
/* 1966 */         LineString outside = poly.getExteriorRing();
/*      */ 
/* 1968 */         WatchBox.Outline ol = new WatchBox.Outline();
/*      */         Coordinate[] arrayOfCoordinate1;
/* 1970 */         DatatypeConfigurationException localDatatypeConfigurationException2 = (arrayOfCoordinate1 = outside.getCoordinates()).length;
/*      */         Point fpt;
/* 1970 */         for (e = 0; e < localDatatypeConfigurationException2; e++) { Coordinate pt = arrayOfCoordinate1[e];
/* 1971 */           fpt = new Point();
/* 1972 */           fpt.setLat(Double.valueOf(pt.y));
/* 1973 */           fpt.setLon(Double.valueOf(pt.x));
/* 1974 */           ol.getPoint().add(fpt);
/*      */         }
/*      */ 
/* 1977 */         fwb.getOutline().add(ol);
/*      */ 
/* 1980 */         for (int jj = 0; jj < poly.getNumInteriorRing(); jj++)
/*      */         {
/* 1982 */           LineString ls = poly.getInteriorRingN(jj);
/* 1983 */           WatchBox.Hole hole = new WatchBox.Hole();
/*      */           Coordinate[] arrayOfCoordinate2;
/* 1985 */           Point localPoint1 = (arrayOfCoordinate2 = ls.getCoordinates()).length; for (fpt = 0; fpt < localPoint1; fpt++) { Coordinate pt = arrayOfCoordinate2[fpt];
/* 1986 */             Point fpt = new Point();
/* 1987 */             fpt.setLat(Double.valueOf(pt.y));
/* 1988 */             fpt.setLon(Double.valueOf(pt.x));
/* 1989 */             hole.getPoint().add(fpt);
/*      */           }
/*      */ 
/* 1992 */           fwb.getHole().add(hole);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1998 */     return fwb;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.elements.WatchBox convertXML2WatchBox(gov.noaa.nws.ncep.ui.pgen.file.WatchBox fwb)
/*      */   {
/* 2007 */     gov.noaa.nws.ncep.ui.pgen.elements.WatchBox wb = new gov.noaa.nws.ncep.ui.pgen.elements.WatchBox();
/*      */ 
/* 2009 */     wb.setPgenCategory(fwb.getPgenCategory());
/* 2010 */     wb.setPgenType(fwb.getPgenType());
/* 2011 */     wb.setWatchBoxShape(fwb.getBoxShape());
/* 2012 */     wb.setFillFlag(fwb.isFillFlag().booleanValue());
/* 2013 */     wb.setWatchSymbolSize(fwb.getSymbolSize().doubleValue());
/* 2014 */     wb.setWatchSymbolWidth(fwb.getSymbolWidth().floatValue());
/* 2015 */     wb.setWatchSymbolType(fwb.getSymbolType());
/* 2016 */     wb.setWatchType(fwb.getWatchType());
/*      */ 
/* 2019 */     wb.setIssueStatus(fwb.getIssueStatus());
/*      */ 
/* 2021 */     Calendar issueTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2022 */     XMLGregorianCalendar xmlIssueCal = fwb.getIssueTime();
/* 2023 */     if (xmlIssueCal != null) {
/* 2024 */       issueTime.set(xmlIssueCal.getYear(), xmlIssueCal.getMonth() - 1, xmlIssueCal.getDay(), 
/* 2025 */         xmlIssueCal.getHour(), xmlIssueCal.getMinute(), xmlIssueCal.getSecond());
/* 2026 */       wb.setIssueTime(issueTime);
/*      */     }
/*      */ 
/* 2029 */     Calendar expTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2030 */     XMLGregorianCalendar xmlExpCal = fwb.getExpTime();
/* 2031 */     if (xmlExpCal != null) {
/* 2032 */       expTime.set(xmlExpCal.getYear(), xmlExpCal.getMonth() - 1, xmlExpCal.getDay(), 
/* 2033 */         xmlExpCal.getHour(), xmlExpCal.getMinute(), xmlExpCal.getSecond());
/* 2034 */       wb.setExpTime(expTime);
/*      */     }
/*      */ 
/* 2037 */     wb.setSeverity(fwb.getSeverity());
/* 2038 */     wb.setTimeZone(fwb.getTimeZone());
/*      */ 
/* 2040 */     if (fwb.getHailSize() != null) {
/* 2041 */       wb.setHailSize(fwb.getHailSize().floatValue());
/*      */     }
/* 2043 */     if (fwb.getGust() != null) {
/* 2044 */       wb.setGust(fwb.getGust().intValue());
/*      */     }
/* 2046 */     if (fwb.getTop() != null) {
/* 2047 */       wb.setTop(fwb.getTop().intValue());
/*      */     }
/* 2049 */     if (fwb.getMoveDir() != null) {
/* 2050 */       wb.setMoveDir(fwb.getMoveDir().intValue());
/*      */     }
/* 2052 */     if (fwb.getMoveSpeed() != null) {
/* 2053 */       wb.setMoveSpeed(fwb.getMoveSpeed().intValue());
/*      */     }
/* 2055 */     wb.setAdjAreas(fwb.getAdjAreas());
/*      */ 
/* 2057 */     if (fwb.getReplWatch() != null) {
/* 2058 */       wb.setReplWatch(fwb.getReplWatch().intValue());
/*      */     }
/* 2060 */     if (fwb.getContWatch() != null) {
/* 2061 */       wb.setContWatch(fwb.getContWatch());
/*      */     }
/* 2063 */     if (fwb.getIssueFlag() != null) {
/* 2064 */       wb.setIssueFlag(fwb.getIssueFlag().intValue());
/*      */     }
/* 2066 */     wb.setForecaster(fwb.getForecaster());
/*      */ 
/* 2068 */     if (fwb.getWatchNumber() != null) {
/* 2069 */       wb.setWatchNumber(fwb.getWatchNumber().intValue());
/*      */     }
/* 2071 */     wb.setEndPointAnc(fwb.getEndPointAnc());
/* 2072 */     wb.setEndPointVor(fwb.getEndPointVor());
/*      */ 
/* 2074 */     if (fwb.getWatchAreaNm() != null) {
/* 2075 */       wb.setWathcAreaNm(fwb.getWatchAreaNm().intValue());
/*      */     }
/* 2077 */     if (fwb.getHalfWidthNm() != null) {
/* 2078 */       wb.setHalfWidthNm(fwb.getHalfWidthNm().intValue());
/*      */     }
/* 2080 */     if (fwb.getHalfWidthSm() != null) {
/* 2081 */       wb.setHalfWidthSm(fwb.getHalfWidthSm().intValue());
/*      */     }
/*      */ 
/* 2085 */     java.awt.Color[] clr = new java.awt.Color[fwb.getColor().size()];
/* 2086 */     int nn = 0;
/* 2087 */     for (gov.noaa.nws.ncep.ui.pgen.file.Color fColor : fwb.getColor()) {
/* 2088 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2089 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/* 2091 */     wb.setColors(clr);
/*      */ 
/* 2094 */     ArrayList linePoints = new ArrayList();
/* 2095 */     nn = 0;
/* 2096 */     for (Point pt : fwb.getPoint()) {
/* 2097 */       linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */     }
/* 2099 */     wb.setPointsOnly(linePoints);
/*      */ 
/* 2102 */     if (fwb.getFillColor() != null) {
/* 2103 */       gov.noaa.nws.ncep.ui.pgen.file.Color fColor = fwb.getFillColor().getColor();
/* 2104 */       if (fColor != null) {
/* 2105 */         java.awt.Color fill = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2106 */           fColor.getBlue(), fColor.getAlpha().intValue());
/* 2107 */         wb.setFillColor(fill);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2114 */     String fileDir = ProductConverter.class.getProtectionDomain().getCodeSource().getLocation().toString();
/*      */ 
/* 2119 */     if (fileDir.endsWith(".jar"))
/* 2120 */       fileDir = fileDir.substring(5, fileDir.lastIndexOf("/") + 1);
/*      */     else {
/* 2122 */       fileDir = fileDir.substring(5);
/*      */     }
/*      */ 
/* 2125 */     Station[] anchors = new Station[fwb.getAnchorPoints().size()];
/* 2126 */     nn = 0;
/* 2127 */     for (String str : fwb.getAnchorPoints())
/*      */     {
/* 2129 */       if (!str.equalsIgnoreCase(""))
/* 2130 */         anchors[(nn++)] = new StationTable(fileDir + "table/spcwatch.xml").getStation(IStationField.StationField.STID, str.substring(0, 3));
/*      */     }
/* 2132 */     wb.setAnchors(anchors[0], anchors[1]);
/*      */ 
/* 2135 */     Station[] county = new Station[fwb.getCounties().size()];
/* 2136 */     int n = 0;
/*      */ 
/* 2138 */     for (String str : fwb.getCounties())
/*      */     {
/* 2140 */       String[] substr = str.split("\\s+");
/* 2141 */       if (substr.length > 1) {
/* 2142 */         str = substr[5];
/*      */       }
/* 2144 */       county[n] = new StationTable(fileDir + "table/mzcntys.xml").getStation(IStationField.StationField.STNM, str);
/* 2145 */       if (county[n] != null)
/*      */       {
/* 2147 */         SPCCounty wbCounty = new SPCCounty(
/* 2148 */           county[n].getStnnum(), 
/* 2149 */           county[n].getStnname(), 
/* 2150 */           county[n].getWfo(), 
/* 2151 */           "", 
/* 2152 */           county[n].getState(), 
/* 2153 */           county[n].getCountry(), 
/* 2154 */           "", 
/* 2155 */           new Coordinate(county[n].getLongitude().floatValue(), county[n].getLatitude().floatValue()), 
/* 2156 */           null, 
/* 2157 */           false);
/*      */ 
/* 2160 */         wb.addCounty(wbCounty);
/*      */       }
/* 2162 */       n++;
/*      */     }
/*      */ 
/* 2200 */     for (WatchBox.Status fws : fwb.getStatus())
/*      */     {
/* 2202 */       Calendar statusValidTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2203 */       XMLGregorianCalendar xmlStatusValidCal = fws.getStatusValidTime();
/* 2204 */       if (xmlStatusValidCal != null) {
/* 2205 */         statusValidTime.set(xmlStatusValidCal.getYear(), xmlStatusValidCal.getMonth() - 1, xmlStatusValidCal.getDay(), 
/* 2206 */           xmlStatusValidCal.getHour(), xmlStatusValidCal.getMinute(), xmlStatusValidCal.getSecond());
/*      */       }
/*      */ 
/* 2209 */       Calendar statusExpTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2210 */       XMLGregorianCalendar xmlStatusExpCal = fws.getStatusExpTime();
/* 2211 */       if (xmlStatusExpCal != null) {
/* 2212 */         statusExpTime.set(xmlStatusExpCal.getYear(), xmlStatusExpCal.getMonth() - 1, xmlStatusExpCal.getDay(), 
/* 2213 */           xmlStatusExpCal.getHour(), xmlStatusExpCal.getMinute(), xmlStatusExpCal.getSecond());
/*      */       }
/*      */ 
/* 2216 */       wb.addStatus(fws.getFromLine(), fws.getMesoDiscussionNumber().intValue(), 
/* 2217 */         statusValidTime, statusExpTime, fws.getStatusForecaster());
/*      */     }
/*      */ 
/* 2221 */     return wb;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.file.Outlook convertOutlook2XML(gov.noaa.nws.ncep.ui.pgen.elements.Outlook otlk)
/*      */   {
/* 2232 */     gov.noaa.nws.ncep.ui.pgen.file.Outlook fotlk = 
/* 2233 */       new gov.noaa.nws.ncep.ui.pgen.file.Outlook();
/*      */ 
/* 2235 */     fotlk.setDays(otlk.getDays());
/* 2236 */     fotlk.setForecaster(otlk.getForecaster());
/* 2237 */     fotlk.setName(otlk.getName());
/* 2238 */     fotlk.setPgenType(otlk.getPgenType());
/* 2239 */     fotlk.setPgenCategory(otlk.getPgenCategory());
/* 2240 */     fotlk.setOutlookType(otlk.getOutlookType());
/* 2241 */     fotlk.setLineInfo(otlk.getLineInfo());
/*      */ 
/* 2243 */     XMLGregorianCalendar xmlCal = null;
/*      */ 
/* 2245 */     if (otlk.getParm() != null) {
/* 2246 */       fotlk.setParm(otlk.getParm());
/* 2247 */       fotlk.setLevel(otlk.getLevel());
/* 2248 */       fotlk.setCint(otlk.getCint());
/*      */ 
/* 2250 */       Calendar cntTime = otlk.getTime1();
/*      */       try
/*      */       {
/* 2253 */         xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2254 */           cntTime.get(1), 
/* 2255 */           cntTime.get(2) + 1, 
/* 2256 */           cntTime.get(5), 
/* 2257 */           cntTime.get(11), 
/* 2258 */           cntTime.get(12), 
/* 2259 */           cntTime.get(13), 
/* 2260 */           cntTime.get(14), 
/* 2261 */           0);
/*      */       } catch (DatatypeConfigurationException e) {
/* 2263 */         e.printStackTrace();
/*      */       }
/*      */ 
/* 2266 */       fotlk.setTime(xmlCal);
/*      */     }
/*      */ 
/* 2270 */     Calendar issueTime = otlk.getIssueTime();
/* 2271 */     if (issueTime != null) {
/* 2272 */       xmlCal = null;
/*      */       try {
/* 2274 */         xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2275 */           issueTime.get(1), 
/* 2276 */           issueTime.get(2) + 1, 
/* 2277 */           issueTime.get(5), 
/* 2278 */           issueTime.get(11), 
/* 2279 */           issueTime.get(12), 
/* 2280 */           issueTime.get(13), 
/* 2281 */           0, 
/* 2282 */           0);
/*      */       }
/*      */       catch (DatatypeConfigurationException e)
/*      */       {
/* 2286 */         e.printStackTrace();
/*      */       }
/* 2288 */       fotlk.setIssueTime(xmlCal);
/*      */     }
/*      */ 
/* 2291 */     Calendar expTime = otlk.getExpirationTime();
/* 2292 */     if (expTime != null) {
/* 2293 */       xmlCal = null;
/*      */       try {
/* 2295 */         xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2296 */           expTime.get(1), 
/* 2297 */           expTime.get(2) + 1, 
/* 2298 */           expTime.get(5), 
/* 2299 */           expTime.get(11), 
/* 2300 */           expTime.get(12), 
/* 2301 */           expTime.get(13), 
/* 2302 */           0, 
/* 2303 */           0);
/*      */       }
/*      */       catch (DatatypeConfigurationException e)
/*      */       {
/* 2307 */         e.printStackTrace();
/*      */       }
/* 2309 */       fotlk.setExpTime(xmlCal);
/*      */     }
/*      */ 
/* 2312 */     Iterator it = otlk.getComponentIterator();
/* 2313 */     while (it.hasNext())
/*      */     {
/* 2315 */       AbstractDrawableComponent next = (AbstractDrawableComponent)it.next();
/*      */ 
/* 2317 */       if ((next instanceof gov.noaa.nws.ncep.ui.pgen.elements.DECollection)) {
/* 2318 */         fotlk.getDECollection().add(
/* 2319 */           convertDECollection2XML((gov.noaa.nws.ncep.ui.pgen.elements.DECollection)next));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2324 */     return fotlk;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.elements.Outlook convertXML2Outlook(gov.noaa.nws.ncep.ui.pgen.file.Outlook fotlk)
/*      */   {
/* 2335 */     DrawableElementFactory def = new DrawableElementFactory();
/* 2336 */     gov.noaa.nws.ncep.ui.pgen.elements.Outlook otlk = def.createOutlook(null, null, null, null);
/*      */ 
/* 2338 */     otlk.setPgenType(fotlk.getPgenType());
/* 2339 */     otlk.setPgenCategory(fotlk.getPgenCategory());
/* 2340 */     otlk.setDays(fotlk.getDays());
/* 2341 */     otlk.setForecaster(fotlk.getForecaster());
/* 2342 */     otlk.setOutlookType(fotlk.getOutlookType());
/* 2343 */     otlk.setLineInfo(fotlk.getLineInfo());
/*      */ 
/* 2345 */     if (fotlk.getParm() != null) {
/* 2346 */       otlk.setParm(fotlk.getParm());
/* 2347 */       otlk.setLevel(fotlk.getLevel());
/* 2348 */       otlk.setCint(fotlk.getCint());
/*      */ 
/* 2350 */       Calendar cntTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2351 */       XMLGregorianCalendar xmlCal = fotlk.getTime();
/* 2352 */       cntTime.set(xmlCal.getYear(), xmlCal.getMonth() - 1, xmlCal.getDay(), 
/* 2353 */         xmlCal.getHour(), xmlCal.getMinute(), xmlCal.getSecond());
/* 2354 */       otlk.setTime1(cntTime);
/*      */     }
/*      */ 
/* 2357 */     Calendar issueTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2358 */     issueTime.set(14, 0);
/* 2359 */     XMLGregorianCalendar xmlIssueCal = fotlk.getIssueTime();
/* 2360 */     if (xmlIssueCal != null) {
/* 2361 */       issueTime.set(xmlIssueCal.getYear(), xmlIssueCal.getMonth() - 1, xmlIssueCal.getDay(), 
/* 2362 */         xmlIssueCal.getHour(), xmlIssueCal.getMinute(), xmlIssueCal.getSecond());
/* 2363 */       otlk.setIssueTime(issueTime);
/*      */     }
/*      */ 
/* 2366 */     Calendar expTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2367 */     issueTime.set(14, 0);
/*      */ 
/* 2369 */     XMLGregorianCalendar xmlExpCal = fotlk.getExpTime();
/* 2370 */     if (xmlExpCal != null) {
/* 2371 */       expTime.set(xmlExpCal.getYear(), xmlExpCal.getMonth() - 1, xmlExpCal.getDay(), 
/* 2372 */         xmlExpCal.getHour(), xmlExpCal.getMinute(), xmlExpCal.getSecond());
/* 2373 */       otlk.setExpirationTime(expTime);
/*      */     }
/*      */ 
/* 2376 */     for (gov.noaa.nws.ncep.ui.pgen.file.DECollection fdec : fotlk.getDECollection())
/*      */     {
/*      */       AbstractDrawableComponent de;
/* 2378 */       if (fdec.getCollectionName().equalsIgnoreCase(gov.noaa.nws.ncep.ui.pgen.elements.Outlook.OUTLOOK_LABELED_LINE)) {
/* 2379 */         gov.noaa.nws.ncep.ui.pgen.elements.DECollection dec = new gov.noaa.nws.ncep.ui.pgen.elements.DECollection(gov.noaa.nws.ncep.ui.pgen.elements.Outlook.OUTLOOK_LABELED_LINE);
/* 2380 */         List delist = convert(fdec.getDrawableElement());
/* 2381 */         for (Iterator localIterator2 = delist.iterator(); localIterator2.hasNext(); ) { de = (AbstractDrawableComponent)localIterator2.next();
/* 2382 */           de.setParent(dec);
/* 2383 */           dec.add(de);
/*      */         }
/* 2385 */         dec.setParent(otlk);
/* 2386 */         otlk.add(dec);
/*      */       }
/* 2390 */       else if (fdec.getCollectionName().equalsIgnoreCase(gov.noaa.nws.ncep.ui.pgen.elements.Outlook.OUTLOOK_LINE_GROUP)) {
/* 2391 */         gov.noaa.nws.ncep.ui.pgen.elements.DECollection grp = new gov.noaa.nws.ncep.ui.pgen.elements.DECollection(gov.noaa.nws.ncep.ui.pgen.elements.Outlook.OUTLOOK_LINE_GROUP);
/*      */ 
/* 2393 */         for (gov.noaa.nws.ncep.ui.pgen.file.DECollection labeledLine : fdec.getDrawableElement().getDECollection()) {
/* 2394 */           gov.noaa.nws.ncep.ui.pgen.elements.DECollection lblLine = new gov.noaa.nws.ncep.ui.pgen.elements.DECollection(gov.noaa.nws.ncep.ui.pgen.elements.Outlook.OUTLOOK_LABELED_LINE);
/* 2395 */           List des = convert(labeledLine.getDrawableElement());
/* 2396 */           for (AbstractDrawableComponent de : des) {
/* 2397 */             de.setParent(lblLine);
/* 2398 */             lblLine.add(de);
/*      */           }
/* 2400 */           lblLine.setParent(grp);
/* 2401 */           grp.add(lblLine);
/*      */         }
/* 2403 */         grp.setParent(otlk);
/* 2404 */         otlk.add(grp);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2409 */     return otlk;
/*      */   }
/*      */ 
/*      */   public static Properties load(File propsFile)
/*      */   {
/* 2416 */     Properties props = new Properties();
/*      */     try {
/* 2418 */       FileInputStream fis = new FileInputStream(propsFile);
/* 2419 */       props.load(fis);
/*      */ 
/* 2421 */       fis.close();
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/* 2425 */     return props;
/*      */   }
/*      */ 
/*      */   public static gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano convertXML2Volcano(gov.noaa.nws.ncep.ui.pgen.file.Volcano fVol)
/*      */   {
/* 2439 */     gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano vol = new gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano();
/*      */ 
/* 2441 */     vol.setPgenCategory(fVol.getPgenCategory());
/* 2442 */     vol.setPgenType(fVol.getPgenType());
/* 2443 */     vol.setSizeScale(fVol.getSizeScale().doubleValue());
/* 2444 */     vol.setLineWidth(fVol.getLineWidth().floatValue());
/* 2445 */     vol.setClear(fVol.isClear());
/*      */ 
/* 2447 */     java.awt.Color[] clr = new java.awt.Color[fVol.getColor().size()];
/* 2448 */     int nn = 0;
/* 2449 */     for (gov.noaa.nws.ncep.ui.pgen.file.Color fColor : fVol.getColor()) {
/* 2450 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2451 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/* 2453 */     vol.setColors(clr);
/*      */ 
/* 2458 */     boolean isNoneDrawable = false;
/*      */ 
/* 2463 */     ArrayList volPoints = new ArrayList();
/* 2464 */     nn = 0;
/* 2465 */     for (Point pt : fVol.getPoint()) {
/* 2466 */       volPoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */     }
/* 2468 */     if (!isNoneDrawable) vol.setPoints(volPoints);
/*      */ 
/* 2470 */     vol.setName(fVol.getName());
/* 2471 */     vol.setNumber(fVol.getNumber());
/* 2472 */     vol.setTxtLoc(fVol.getTxtLoc());
/* 2473 */     vol.setArea(fVol.getArea());
/* 2474 */     vol.setElev(fVol.getElev());
/*      */ 
/* 2476 */     vol.setOrigStnVAAC(fVol.getOrigStnVAAC());
/* 2477 */     vol.setWmoId(fVol.getWmoId());
/* 2478 */     vol.setHdrNum(fVol.getHdrNum());
/* 2479 */     vol.setProduct(fVol.getProduct());
/* 2480 */     vol.setYear(fVol.getYear());
/* 2481 */     vol.setAdvNum(fVol.getAdvNum());
/* 2482 */     vol.setCorr(fVol.getCorr());
/*      */ 
/* 2484 */     vol.setInfoSource(fVol.getInfoSource());
/* 2485 */     vol.setAddInfoSource(fVol.getAddInfoSource());
/* 2486 */     vol.setAviColorCode(fVol.getAviColorCode());
/* 2487 */     vol.setErupDetails(fVol.getErupDetails());
/*      */ 
/* 2489 */     vol.setObsAshDate(fVol.getObsAshDate());
/* 2490 */     vol.setObsAshTime(fVol.getObsAshTime());
/* 2491 */     vol.setNil(fVol.getNil());
/*      */ 
/* 2493 */     vol.setObsFcstAshCloudInfo(fVol.getObsFcstAshCloudInfo());
/* 2494 */     vol.setObsFcstAshCloudInfo6(fVol.getObsFcstAshCloudInfo6());
/* 2495 */     vol.setObsFcstAshCloudInfo12(fVol.getObsFcstAshCloudInfo12());
/* 2496 */     vol.setObsFcstAshCloudInfo18(fVol.getObsFcstAshCloudInfo18());
/*      */ 
/* 2498 */     vol.setRemarks(fVol.getRemarks());
/* 2499 */     vol.setNextAdv(fVol.getNextAdv());
/* 2500 */     vol.setForecasters(fVol.getForecasters());
/*      */ 
/* 2502 */     return vol;
/*      */   }
/*      */ 
/*      */   public static gov.noaa.nws.ncep.ui.pgen.file.Volcano convertVolcano2XML(gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano vol)
/*      */   {
/* 2515 */     gov.noaa.nws.ncep.ui.pgen.file.Volcano fVol = 
/* 2516 */       new gov.noaa.nws.ncep.ui.pgen.file.Volcano();
/* 2517 */     String vp = vol.getProduct() == null ? null : vol.getProduct().trim();
/* 2518 */     boolean isNoneDrawable = Arrays.asList(VaaInfo.ProductInfo.getProduct(gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo.LOCS[1])).contains(vp);
/*      */ 
/* 2521 */     for (java.awt.Color clr : vol.getColors())
/*      */     {
/* 2523 */       gov.noaa.nws.ncep.ui.pgen.file.Color fclr = 
/* 2524 */         new gov.noaa.nws.ncep.ui.pgen.file.Color();
/*      */ 
/* 2526 */       fclr.setRed(clr.getRed());
/* 2527 */       fclr.setGreen(clr.getGreen());
/* 2528 */       fclr.setBlue(clr.getBlue());
/* 2529 */       fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 2530 */       fVol.getColor().add(fclr);
/*      */     }
/*      */ 
/* 2533 */     for (Coordinate crd : vol.getLinePoints()) {
/* 2534 */       if (isNoneDrawable) break;
/* 2535 */       Point fpt = new Point();
/* 2536 */       fpt.setLat(Double.valueOf(crd.y));
/* 2537 */       fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/* 2539 */       fVol.getPoint().add(fpt);
/*      */     }
/*      */ 
/* 2542 */     fVol.setPgenCategory(vol.getPgenCategory());
/* 2543 */     fVol.setPgenType(vol.getPgenType());
/* 2544 */     fVol.setSizeScale(Double.valueOf(vol.getSizeScale()));
/* 2545 */     fVol.setLineWidth(Float.valueOf(vol.getLineWidth()));
/* 2546 */     fVol.setClear(vol.isClear());
/*      */ 
/* 2548 */     fVol.setName(vol.getName());
/* 2549 */     fVol.setNumber(vol.getNumber());
/* 2550 */     fVol.setTxtLoc(vol.getTxtLoc());
/* 2551 */     fVol.setArea(vol.getArea());
/* 2552 */     fVol.setElev(vol.getElev());
/*      */ 
/* 2554 */     fVol.setOrigStnVAAC(vol.getOrigStnVAAC());
/* 2555 */     fVol.setWmoId(vol.getWmoId());
/* 2556 */     fVol.setHdrNum(vol.getHdrNum());
/* 2557 */     fVol.setProduct(vol.getProduct());
/* 2558 */     fVol.setYear(vol.getYear());
/* 2559 */     fVol.setAdvNum(vol.getAdvNum());
/* 2560 */     fVol.setCorr(vol.getCorr());
/*      */ 
/* 2562 */     fVol.setInfoSource(vol.getInfoSource());
/* 2563 */     fVol.setAddInfoSource(vol.getAddInfoSource());
/* 2564 */     fVol.setAviColorCode(vol.getAviColorCode());
/* 2565 */     fVol.setErupDetails(vol.getErupDetails());
/*      */ 
/* 2567 */     fVol.setObsAshDate(vol.getObsAshDate());
/* 2568 */     fVol.setObsAshTime(vol.getObsAshTime());
/* 2569 */     fVol.setNil(vol.getNil());
/*      */ 
/* 2571 */     fVol.setObsFcstAshCloudInfo(vol.getObsFcstAshCloudInfo());
/* 2572 */     fVol.setObsFcstAshCloudInfo6(vol.getObsFcstAshCloudInfo6());
/* 2573 */     fVol.setObsFcstAshCloudInfo12(vol.getObsFcstAshCloudInfo12());
/* 2574 */     fVol.setObsFcstAshCloudInfo18(vol.getObsFcstAshCloudInfo18());
/*      */ 
/* 2576 */     fVol.setRemarks(vol.getRemarks());
/* 2577 */     fVol.setNextAdv(vol.getNextAdv());
/* 2578 */     fVol.setForecasters(vol.getForecasters());
/*      */ 
/* 2581 */     return fVol;
/*      */   }
/*      */ 
/*      */   private static String nvl(String str) {
/* 2585 */     return "".equals(str) ? null : str;
/*      */   }
/*      */ 
/*      */   private static LabeledLine convertXML2LabeledLine(gov.noaa.nws.ncep.ui.pgen.file.DECollection dec, LabeledLine ll)
/*      */   {
/* 2595 */     if (ll != null) {
/* 2596 */       ll.setPgenCategory(dec.getPgenCategory());
/* 2597 */       ll.setPgenType(dec.getPgenType());
/*      */ 
/* 2599 */       gov.noaa.nws.ncep.ui.pgen.file.DrawableElement elem = dec
/* 2600 */         .getDrawableElement();
/*      */       Iterator localIterator1;
/*      */       Iterator localIterator2;
/*      */       java.awt.Color[] clr;
/* 2602 */       if (elem.getDECollection() != null)
/*      */       {
/* 2605 */         localIterator1 = elem
/* 2605 */           .getDECollection().iterator();
/*      */ 
/* 2604 */         while (localIterator1.hasNext()) {
/* 2605 */           gov.noaa.nws.ncep.ui.pgen.file.DECollection lblDec = (gov.noaa.nws.ncep.ui.pgen.file.DECollection)localIterator1.next();
/* 2606 */           if (lblDec.getCollectionName().equalsIgnoreCase("Label")) {
/* 2607 */             Label lbl = new Label();
/* 2608 */             lbl.setParent(ll);
/*      */ 
/* 2610 */             gov.noaa.nws.ncep.ui.pgen.file.DrawableElement lblDe = lblDec
/* 2611 */               .getDrawableElement();
/*      */             Object line;
/* 2613 */             if (lblDe.getLine() != null)
/*      */             {
/* 2616 */               localIterator2 = lblDe
/* 2616 */                 .getLine().iterator();
/*      */ 
/* 2615 */               while (localIterator2.hasNext()) {
/* 2616 */                 gov.noaa.nws.ncep.ui.pgen.file.Line arrowLine = (gov.noaa.nws.ncep.ui.pgen.file.Line)localIterator2.next();
/*      */ 
/* 2619 */                 java.awt.Color[] clr = new java.awt.Color[arrowLine.getColor().size()];
/* 2620 */                 int nn = 0;
/*      */ 
/* 2622 */                 Iterator localIterator3 = arrowLine
/* 2622 */                   .getColor().iterator();
/*      */ 
/* 2621 */                 while (localIterator3.hasNext()) {
/* 2622 */                   gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)localIterator3.next();
/* 2623 */                   clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor
/* 2624 */                     .getGreen(), fColor.getBlue(), fColor
/* 2625 */                     .getAlpha().intValue());
/*      */                 }
/*      */ 
/* 2628 */                 ArrayList linePoints = new ArrayList();
/* 2629 */                 nn = 0;
/* 2630 */                 for (Point pt : arrowLine.getPoint()) {
/* 2631 */                   linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt
/* 2632 */                     .getLat().doubleValue()));
/*      */                 }
/*      */ 
/* 2635 */                 line = new gov.noaa.nws.ncep.ui.pgen.elements.Line(null, clr, arrowLine
/* 2636 */                   .getLineWidth().floatValue(), arrowLine.getSizeScale().doubleValue(), 
/* 2637 */                   arrowLine.isClosed().booleanValue(), arrowLine.isFilled().booleanValue(), 
/* 2638 */                   linePoints, arrowLine.getSmoothFactor().intValue(), 
/* 2639 */                   FillPatternList.FillPattern.valueOf(arrowLine
/* 2640 */                   .getFillPattern()), arrowLine
/* 2641 */                   .getPgenCategory(), arrowLine
/* 2642 */                   .getPgenType());
/*      */ 
/* 2644 */                 ((gov.noaa.nws.ncep.ui.pgen.elements.Line)line).setParent(lbl);
/* 2645 */                 lbl.addArrow((gov.noaa.nws.ncep.ui.pgen.elements.Line)line);
/*      */               }
/*      */             }
/*      */             Object text;
/* 2649 */             if (lblDe.getMidCloudText() != null)
/*      */             {
/* 2652 */               for (gov.noaa.nws.ncep.ui.pgen.file.MidCloudText aText : lblDe.getMidCloudText())
/*      */               {
/* 2654 */                 java.awt.Color[] clr = new java.awt.Color[aText.getColor().size()];
/* 2655 */                 int nn = 0;
/*      */ 
/* 2657 */                 line = aText
/* 2657 */                   .getColor().iterator();
/*      */ 
/* 2656 */                 while (((Iterator)line).hasNext()) {
/* 2657 */                   gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)((Iterator)line).next();
/* 2658 */                   clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2659 */                     fColor.getBlue(), fColor.getAlpha().intValue());
/*      */                 }
/*      */ 
/* 2662 */                 Point loc = aText.getPoint();
/*      */ 
/* 2664 */                 text = new gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText(null, aText.getFontName(), 
/* 2665 */                   aText.getFontSize().floatValue(), 
/* 2666 */                   IText.TextJustification.valueOf(aText.getJustification()), 
/* 2667 */                   new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 2668 */                   aText.getCloudTypes(), aText.getCloudAmounts(), 
/* 2669 */                   aText.getTurbulenceType(), aText.getTurbulenceLevels(), 
/* 2670 */                   aText.getIcingType(), aText.getIcingLevels(), 
/* 2671 */                   aText.getTstormTypes(), aText.getTstormLevels(), 
/* 2672 */                   IText.FontStyle.valueOf(aText.getStyle()), clr[0], aText.getPgenCategory(), 
/* 2673 */                   aText.getPgenType());
/*      */ 
/* 2676 */                 ((gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText)text).setParent(lbl);
/* 2677 */                 lbl.setSpe((SinglePointElement)text);
/*      */               }
/*      */             }
/*      */ 
/* 2681 */             if (lblDe.getAvnText() != null)
/*      */             {
/* 2684 */               for (gov.noaa.nws.ncep.ui.pgen.file.AvnText aText : lblDe.getAvnText())
/*      */               {
/* 2686 */                 clr = new java.awt.Color[aText.getColor().size()];
/* 2687 */                 int nn = 0;
/*      */ 
/* 2689 */                 text = aText
/* 2689 */                   .getColor().iterator();
/*      */ 
/* 2688 */                 while (((Iterator)text).hasNext()) {
/* 2689 */                   gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)((Iterator)text).next();
/* 2690 */                   clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2691 */                     fColor.getBlue(), fColor.getAlpha().intValue());
/*      */                 }
/*      */ 
/* 2694 */                 Point loc = aText.getPoint();
/*      */ 
/* 2696 */                 gov.noaa.nws.ncep.ui.pgen.elements.AvnText text = new gov.noaa.nws.ncep.ui.pgen.elements.AvnText(null, aText.getFontName(), 
/* 2697 */                   aText.getFontSize().floatValue(), 
/* 2698 */                   IText.TextJustification.valueOf(aText.getJustification()), 
/* 2699 */                   new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 2700 */                   IAvnText.AviationTextType.valueOf(aText.getAvnTextType()), 
/* 2701 */                   aText.getTopValue(), 
/* 2702 */                   aText.getBottomValue(), 
/* 2703 */                   IText.FontStyle.valueOf(aText.getStyle()), 
/* 2704 */                   clr[0], 
/* 2705 */                   aText.getSymbolPatternName(), 
/* 2706 */                   aText.getPgenCategory(), 
/* 2707 */                   aText.getPgenType());
/*      */ 
/* 2710 */                 text.setParent(lbl);
/* 2711 */                 lbl.setSpe(text);
/*      */               }
/*      */             }
/* 2714 */             if (lblDe.getText() != null) handleCcfpText(lblDe, lbl);
/* 2715 */             ll.addLabel(lbl);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2722 */       if (elem.getLine() != null)
/*      */       {
/* 2724 */         for (gov.noaa.nws.ncep.ui.pgen.file.Line fLine : elem.getLine())
/*      */         {
/* 2727 */           java.awt.Color[] clr = new java.awt.Color[fLine.getColor().size()];
/* 2728 */           int nn = 0;
/*      */ 
/* 2730 */           localIterator2 = fLine
/* 2730 */             .getColor().iterator();
/*      */ 
/* 2729 */           while (localIterator2.hasNext()) {
/* 2730 */             gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)localIterator2.next();
/* 2731 */             clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2732 */               fColor.getBlue(), fColor.getAlpha().intValue());
/*      */           }
/*      */ 
/* 2735 */           ArrayList linePoints = new ArrayList();
/* 2736 */           nn = 0;
/* 2737 */           for (Point pt : fLine.getPoint()) {
/* 2738 */             linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */           }
/*      */ 
/* 2741 */           gov.noaa.nws.ncep.ui.pgen.elements.Line line = new gov.noaa.nws.ncep.ui.pgen.elements.Line(null, clr, fLine.getLineWidth().floatValue(), fLine
/* 2742 */             .getSizeScale().doubleValue(), fLine.isClosed().booleanValue(), fLine.isFilled().booleanValue(), 
/* 2743 */             linePoints, fLine.getSmoothFactor().intValue(), 
/* 2744 */             FillPatternList.FillPattern.valueOf(fLine.getFillPattern()), fLine
/* 2745 */             .getPgenCategory(), fLine.getPgenType());
/*      */ 
/* 2747 */           line.setParent(ll);
/* 2748 */           ll.addLine(line);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2754 */     return ll;
/*      */   }
/*      */ 
/*      */   private static Cloud convertXML2Cloud(gov.noaa.nws.ncep.ui.pgen.file.DECollection dec)
/*      */   {
/* 2765 */     Cloud cloud = new Cloud("Cloud");
/* 2766 */     return (Cloud)convertXML2LabeledLine(dec, cloud);
/*      */   }
/*      */ 
/*      */   private static Turbulence convertXML2Turb(gov.noaa.nws.ncep.ui.pgen.file.DECollection dec)
/*      */   {
/* 2777 */     Turbulence turb = new Turbulence("Turbulence");
/* 2778 */     return (Turbulence)convertXML2LabeledLine(dec, turb);
/*      */   }
/*      */ 
/*      */   private static Ccfp convertXML2Ccfp(gov.noaa.nws.ncep.ui.pgen.file.DECollection dec)
/*      */   {
/* 2784 */     String[] attr = dec.getCollectionName().split(":::");
/* 2785 */     gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet sig = new gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet();
/* 2786 */     sig.setEditableAttrPhenomSpeed(attr[1]);
/* 2787 */     sig.setEditableAttrPhenomDirection(attr[2]);
/* 2788 */     sig.setEditableAttrStartTime(attr[3]);
/* 2789 */     sig.setEditableAttrEndTime(attr[4]);
/* 2790 */     sig.setEditableAttrPhenom(attr[5]);
/* 2791 */     sig.setEditableAttrPhenom2(attr[6]);
/* 2792 */     sig.setEditableAttrPhenomLat(attr[7]);
/* 2793 */     sig.setEditableAttrPhenomLon(attr[8]);
/* 2794 */     sig.setType(attr[(attr.length - 1)]);
/*      */ 
/* 2796 */     Ccfp ccfp = new Ccfp(dec.getCollectionName());
/* 2797 */     ccfp.setSigmet(sig);
/* 2798 */     return (Ccfp)convertXML2LabeledLine(dec, ccfp);
/*      */   }
/*      */ 
/*      */   private static void handleCcfpText(gov.noaa.nws.ncep.ui.pgen.file.DrawableElement lblDe, Label lbl)
/*      */   {
/* 2803 */     for (gov.noaa.nws.ncep.ui.pgen.file.Text aText : lblDe.getText())
/*      */     {
/* 2805 */       java.awt.Color[] clr = new java.awt.Color[aText.getColor().size()];
/* 2806 */       int nn = 0;
/*      */ 
/* 2808 */       Iterator localIterator2 = aText
/* 2808 */         .getColor().iterator();
/*      */ 
/* 2807 */       while (localIterator2.hasNext()) {
/* 2808 */         gov.noaa.nws.ncep.ui.pgen.file.Color fColor = (gov.noaa.nws.ncep.ui.pgen.file.Color)localIterator2.next();
/* 2809 */         clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2810 */           fColor.getBlue(), fColor.getAlpha().intValue());
/*      */       }
/*      */ 
/* 2813 */       Point loc = aText.getPoint();
/*      */ 
/* 2815 */       gov.noaa.nws.ncep.ui.pgen.elements.Text text = new gov.noaa.nws.ncep.ui.pgen.elements.Text(null, aText.getFontName(), aText.getFontSize().floatValue(), 
/* 2816 */         IText.TextJustification.valueOf(aText.getJustification()), new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 2817 */         0.0D, IText.TextRotation.SCREEN_RELATIVE, (String[])aText.getTextLine().toArray(new String[0]), 
/* 2818 */         IText.FontStyle.valueOf(aText.getStyle()), clr[0], 0, 0, 
/* 2819 */         true, IText.DisplayType.BOX, aText.getPgenCategory(), aText.getPgenType());
/*      */ 
/* 2821 */       text.setParent(lbl);
/* 2822 */       lbl.setSpe(text);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Tcm convertXML2Tcm(TCM fileTcm)
/*      */   {
/* 2834 */     Tcm pgenTcm = new Tcm();
/*      */ 
/* 2836 */     pgenTcm.setPgenType(fileTcm.getPgenType());
/* 2837 */     pgenTcm.setPgenCategory(fileTcm.getPgenCategory());
/*      */ 
/* 2839 */     pgenTcm.setAdvisoryNumber(fileTcm.getAdvisoryNumber().intValue());
/* 2840 */     pgenTcm.setBasin(fileTcm.getBasin());
/* 2841 */     pgenTcm.setCentralPressure(fileTcm.getCentralPressure().intValue());
/* 2842 */     pgenTcm.setCorrection(fileTcm.getCorrection().booleanValue());
/* 2843 */     pgenTcm.setEyeSize(fileTcm.getEyeSize().intValue());
/* 2844 */     pgenTcm.setStormName(fileTcm.getStormName());
/* 2845 */     pgenTcm.setStormNumber(fileTcm.getStormNumber().intValue());
/* 2846 */     pgenTcm.setStormType(fileTcm.getStormType());
/*      */ 
/* 2848 */     Calendar tcmTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2849 */     XMLGregorianCalendar xmlCal = fileTcm.getAdvisoryTime();
/* 2850 */     tcmTime.set(xmlCal.getYear(), xmlCal.getMonth() - 1, xmlCal.getDay(), 
/* 2851 */       xmlCal.getHour(), xmlCal.getMinute(), xmlCal.getSecond());
/* 2852 */     pgenTcm.setTime(tcmTime);
/*      */ 
/* 2854 */     pgenTcm.setWaveQuatro(fileTcm.getTcmWaves());
/* 2855 */     pgenTcm.setTcmFcst(fileTcm.getTcmFcst());
/*      */ 
/* 2857 */     return pgenTcm;
/*      */   }
/*      */ 
/*      */   private static TCM convertTcm2XML(Tcm pgenTcm)
/*      */   {
/* 2868 */     TCM fileTcm = 
/* 2869 */       new TCM();
/*      */ 
/* 2872 */     fileTcm.setAdvisoryNumber(Integer.valueOf(pgenTcm.getAdvisoryNumber()));
/*      */ 
/* 2874 */     XMLGregorianCalendar xmlCal = null;
/* 2875 */     Calendar tcmTime = pgenTcm.getAdvisoryTime();
/*      */     try
/*      */     {
/* 2878 */       xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2879 */         tcmTime.get(1), 
/* 2880 */         tcmTime.get(2) + 1, 
/* 2881 */         tcmTime.get(5), 
/* 2882 */         tcmTime.get(11), 
/* 2883 */         tcmTime.get(12), 
/* 2884 */         tcmTime.get(13), 
/* 2885 */         tcmTime.get(14), 
/* 2886 */         0);
/*      */     } catch (DatatypeConfigurationException e) {
/* 2888 */       e.printStackTrace();
/*      */     }
/*      */ 
/* 2891 */     fileTcm.setAdvisoryTime(xmlCal);
/*      */ 
/* 2893 */     fileTcm.setCentralPressure(Integer.valueOf(pgenTcm.getCentralPressure()));
/* 2894 */     fileTcm.setCorrection(Boolean.valueOf(pgenTcm.isCorrection()));
/* 2895 */     fileTcm.setEyeSize(Integer.valueOf(pgenTcm.getEyeSize()));
/* 2896 */     fileTcm.setPositionAccuracy(Integer.valueOf(pgenTcm.getPositionAccuracy()));
/*      */ 
/* 2898 */     fileTcm.setBasin(pgenTcm.getBasin());
/* 2899 */     fileTcm.setPgenCategory(pgenTcm.getPgenCategory());
/* 2900 */     fileTcm.setPgenType(pgenTcm.getPgenType());
/* 2901 */     fileTcm.setStormName(pgenTcm.getStormName());
/* 2902 */     fileTcm.setStormNumber(Integer.valueOf(pgenTcm.getStormNumber()));
/* 2903 */     fileTcm.setStormType(pgenTcm.getStormType());
/*      */ 
/* 2905 */     fileTcm.setTcmWaves(pgenTcm.getWaveQuarters());
/* 2906 */     fileTcm.setTcmFcst(pgenTcm.getTcmFcst());
/*      */ 
/* 2908 */     return fileTcm;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.standalone.util.ProductConverter
 * JD-Core Version:    0.6.2
 */