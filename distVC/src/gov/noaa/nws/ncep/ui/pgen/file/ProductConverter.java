/*      */ package gov.noaa.nws.ncep.ui.pgen.file;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.common.staticdata.SPCCounty;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.IStationField.StationField;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*      */ import gov.noaa.nws.ncep.edex.common.stationTables.StationTable;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
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
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaRules;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaWording;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Ccfp;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo.ProductInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.TCAElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenSnapJet;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
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
/*  145 */     List prd = new ArrayList();
/*      */ 
/*  147 */     for (Product fPrd : filePrds.getProduct()) {
/*  148 */       gov.noaa.nws.ncep.ui.pgen.elements.Product p = new gov.noaa.nws.ncep.ui.pgen.elements.Product();
/*      */ 
/*  150 */       p.setName(fPrd.getName());
/*  151 */       p.setForecaster(fPrd.getForecaster());
/*      */ 
/*  153 */       if (fPrd.isOnOff() != null) {
/*  154 */         p.setOnOff(fPrd.isOnOff().booleanValue());
/*      */       }
/*      */ 
/*  157 */       if (fPrd.getType() != null) {
/*  158 */         p.setType(fPrd.getType());
/*      */       }
/*      */ 
/*  161 */       if (fPrd.getCenter() != null) {
/*  162 */         p.setCenter(fPrd.getCenter());
/*      */       }
/*      */ 
/*  165 */       if (fPrd.isSaveLayers() != null) {
/*  166 */         p.setSaveLayers(fPrd.isSaveLayers().booleanValue());
/*      */       }
/*      */       else {
/*  169 */         p.setSaveLayers(false);
/*      */       }
/*      */ 
/*  172 */       p.setUseFile(false);
/*  173 */       p.setInputFile(null);
/*      */ 
/*  175 */       String outFile = fPrd.getOutputFile();
/*  176 */       if (outFile != null) {
/*  177 */         p.setOutputFile(outFile);
/*      */       }
/*      */       else {
/*  180 */         p.setOutputFile(null);
/*      */       }
/*      */ 
/*  183 */       p.setLayers(convertFileLayers(fPrd.getLayer()));
/*      */ 
/*  185 */       prd.add(p);
/*      */     }
/*      */ 
/*  188 */     return prd;
/*      */   }
/*      */ 
/*      */   private static List<gov.noaa.nws.ncep.ui.pgen.elements.Layer> convertFileLayers(List<Layer> flayers)
/*      */   {
/*  197 */     List layers = new ArrayList();
/*      */ 
/*  199 */     for (Layer fLayer : flayers)
/*      */     {
/*  201 */       gov.noaa.nws.ncep.ui.pgen.elements.Layer lyr = new gov.noaa.nws.ncep.ui.pgen.elements.Layer();
/*  202 */       lyr.setName(fLayer.getName());
/*      */ 
/*  204 */       lyr.setColor(new java.awt.Color(fLayer.getColor().getRed(), fLayer.getColor().getGreen(), 
/*  205 */         fLayer.getColor().getBlue(), fLayer.getColor().getAlpha().intValue()));
/*      */ 
/*  207 */       if (fLayer.isOnOff() != null) {
/*  208 */         lyr.setOnOff(fLayer.isOnOff().booleanValue());
/*  209 */         lyr.setMonoColor(fLayer.isMonoColor().booleanValue());
/*  210 */         lyr.setFilled(fLayer.isFilled().booleanValue());
/*      */       }
/*      */       else {
/*  213 */         lyr.setOnOff(true);
/*  214 */         lyr.setMonoColor(false);
/*  215 */         lyr.setFilled(false);
/*      */       }
/*      */ 
/*  218 */       lyr.setInputFile(null);
/*  219 */       lyr.setOutputFile(null);
/*      */ 
/*  221 */       lyr.setDrawables(convert(fLayer.getDrawableElement()));
/*      */ 
/*  223 */       layers.add(lyr);
/*      */     }
/*      */ 
/*  226 */     return layers;
/*      */   }
/*      */ 
/*      */   private static List<AbstractDrawableComponent> convert(DrawableElement elem)
/*      */   {
/*  236 */     List des = new ArrayList();
/*      */     Object line;
/*  238 */     if (!elem.getLine().isEmpty())
/*      */     {
/*  240 */       for (Line fLine : elem.getLine())
/*      */       {
/*  242 */         java.awt.Color[] clr = new java.awt.Color[fLine.getColor().size()];
/*  243 */         int nn = 0;
/*  244 */         for (Color fColor : fLine.getColor()) {
/*  245 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  246 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  249 */         ArrayList linePoints = new ArrayList();
/*  250 */         nn = 0;
/*  251 */         for (Point pt : fLine.getPoint()) {
/*  252 */           linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */         }
/*      */ 
/*  255 */         line = new gov.noaa.nws.ncep.ui.pgen.elements.Line(null, clr, 
/*  256 */           fLine.getLineWidth().floatValue(), fLine.getSizeScale().doubleValue(), fLine.isClosed().booleanValue(), 
/*  257 */           fLine.isFilled().booleanValue(), linePoints, 
/*  258 */           fLine.getSmoothFactor().intValue(), 
/*  259 */           FillPatternList.FillPattern.valueOf(fLine.getFillPattern()), 
/*  260 */           fLine.getPgenCategory(), fLine.getPgenType());
/*      */ 
/*  262 */         ((gov.noaa.nws.ncep.ui.pgen.elements.Line)line).setFlipSide(fLine.isFlipSide());
/*      */ 
/*  264 */         des.add(line);
/*      */       }
/*      */     }
/*      */     Object symbol;
/*  269 */     if (!elem.getSymbol().isEmpty())
/*      */     {
/*  271 */       for (Symbol fSymbol : elem.getSymbol())
/*      */       {
/*  273 */         java.awt.Color[] clr = new java.awt.Color[fSymbol.getColor().size()];
/*  274 */         int nn = 0;
/*  275 */         for (line = fSymbol.getColor().iterator(); ((Iterator)line).hasNext(); ) { Color fColor = (Color)((Iterator)line).next();
/*  276 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  277 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  280 */         Point loc = fSymbol.getPoint();
/*      */ 
/*  282 */         if (fSymbol.pgenCategory.equals("Combo")) {
/*  283 */           ComboSymbol symbol = new ComboSymbol(null, clr, 
/*  284 */             fSymbol.getLineWidth().floatValue(), fSymbol.getSizeScale().doubleValue(), 
/*  285 */             fSymbol.isClear(), 
/*  286 */             new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  287 */             fSymbol.getPgenCategory(), fSymbol.getPgenType());
/*  288 */           des.add(symbol);
/*      */         }
/*      */         else {
/*  291 */           symbol = new gov.noaa.nws.ncep.ui.pgen.elements.Symbol(null, clr, 
/*  292 */             fSymbol.getLineWidth().floatValue(), fSymbol.getSizeScale().doubleValue(), 
/*  293 */             fSymbol.isClear(), 
/*  294 */             new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  295 */             fSymbol.getPgenCategory(), fSymbol.getPgenType());
/*  296 */           des.add(symbol);
/*      */         }
/*      */       }
/*      */     }
/*      */     Object st;
/*      */     int nline;
/*  303 */     if (!elem.getText().isEmpty())
/*      */     {
/*  305 */       for (Text fText : elem.getText())
/*      */       {
/*  307 */         java.awt.Color[] clr = new java.awt.Color[fText.getColor().size()];
/*  308 */         int nn = 0;
/*  309 */         for (symbol = fText.getColor().iterator(); ((Iterator)symbol).hasNext(); ) { Color fColor = (Color)((Iterator)symbol).next();
/*  310 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  311 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  314 */         Point loc = fText.getPoint();
/*      */ 
/*  316 */         st = new String[fText.getTextLine().size()];
/*      */ 
/*  318 */         nline = 0;
/*  319 */         for (String str : fText.getTextLine()) {
/*  320 */           st[(nline++)] = str;
/*      */         }
/*      */ 
/*  323 */         gov.noaa.nws.ncep.ui.pgen.elements.Text text = new gov.noaa.nws.ncep.ui.pgen.elements.Text(null, fText.getFontName(), 
/*  324 */           fText.getFontSize().floatValue(), 
/*  325 */           IText.TextJustification.valueOf(fText.getJustification()), 
/*  326 */           new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  327 */           fText.getRotation().doubleValue(), 
/*  328 */           IText.TextRotation.valueOf(fText.getRotationRelativity()), 
/*  329 */           (String[])st, 
/*  330 */           IText.FontStyle.valueOf(fText.getStyle()), clr[0], 0, 0, 
/*  331 */           fText.isMask().booleanValue(), 
/*  332 */           IText.DisplayType.valueOf(fText.getDisplayType()), 
/*  333 */           fText.getPgenCategory(), fText.getPgenType());
/*      */ 
/*  335 */         if (fText.getXOffset() != null) {
/*  336 */           text.setXOffset(fText.getXOffset().intValue());
/*      */         }
/*      */ 
/*  339 */         if (fText.getYOffset() != null) {
/*  340 */           text.setYOffset(fText.getYOffset().intValue());
/*      */         }
/*      */ 
/*  343 */         if (fText.isHide() != null) {
/*  344 */           text.setHide(fText.isHide());
/*      */         }
/*      */ 
/*  347 */         if (fText.isAuto() != null) {
/*  348 */           text.setAuto(fText.isAuto());
/*      */         }
/*      */ 
/*  351 */         des.add(text);
/*      */       }
/*      */     }
/*      */     Object text;
/*  356 */     if (!elem.getAvnText().isEmpty())
/*      */     {
/*  358 */       for (AvnText aText : elem.getAvnText())
/*      */       {
/*  360 */         java.awt.Color[] clr = new java.awt.Color[aText.getColor().size()];
/*  361 */         int nn = 0;
/*  362 */         for (st = aText.getColor().iterator(); ((Iterator)st).hasNext(); ) { Color fColor = (Color)((Iterator)st).next();
/*  363 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  364 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  367 */         Point loc = aText.getPoint();
/*      */ 
/*  369 */         text = new gov.noaa.nws.ncep.ui.pgen.elements.AvnText(null, aText.getFontName(), 
/*  370 */           aText.getFontSize().floatValue(), 
/*  371 */           IText.TextJustification.valueOf(aText.getJustification()), 
/*  372 */           new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  373 */           IAvnText.AviationTextType.valueOf(aText.getAvnTextType()), 
/*  374 */           aText.getTopValue(), aText.getBottomValue(), 
/*  375 */           IText.FontStyle.valueOf(aText.getStyle()), clr[0], 
/*  376 */           aText.getSymbolPatternName(), aText.getPgenCategory(), aText.getPgenType());
/*      */ 
/*  378 */         des.add(text);
/*      */       }
/*      */     }
/*      */     Object text;
/*  383 */     if (!elem.getMidCloudText().isEmpty())
/*      */     {
/*  385 */       for (MidCloudText mText : elem.getMidCloudText())
/*      */       {
/*  387 */         java.awt.Color[] clr = new java.awt.Color[mText.getColor().size()];
/*  388 */         int nn = 0;
/*  389 */         for (text = mText.getColor().iterator(); ((Iterator)text).hasNext(); ) { Color fColor = (Color)((Iterator)text).next();
/*  390 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  391 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  394 */         Point loc = mText.getPoint();
/*      */ 
/*  396 */         text = new gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText(null, mText.getFontName(), 
/*  397 */           mText.getFontSize().floatValue(), 
/*  398 */           IText.TextJustification.valueOf(mText.getJustification()), 
/*  399 */           new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  400 */           mText.getCloudTypes(), mText.getCloudAmounts(), 
/*  401 */           mText.getTurbulenceType(), mText.getTurbulenceLevels(), 
/*  402 */           mText.getIcingType(), mText.getIcingLevels(), 
/*  403 */           mText.getTstormTypes(), mText.getTstormLevels(), 
/*  404 */           IText.FontStyle.valueOf(mText.getStyle()), clr[0], 
/*  405 */           mText.getPgenCategory(), mText.getPgenType());
/*      */ 
/*  407 */         des.add(text);
/*      */       }
/*      */     }
/*      */     Object arc;
/*  412 */     if (!elem.getArc().isEmpty())
/*      */     {
/*  414 */       for (Arc fArc : elem.getArc())
/*      */       {
/*  416 */         java.awt.Color[] clr = new java.awt.Color[fArc.getColor().size()];
/*  417 */         int nn = 0;
/*  418 */         for (text = fArc.getColor().iterator(); ((Iterator)text).hasNext(); ) { Color fColor = (Color)((Iterator)text).next();
/*  419 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  420 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  424 */         ArrayList linePoints = new ArrayList();
/*  425 */         for (Point pt : fArc.getPoint()) {
/*  426 */           linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */         }
/*      */ 
/*  429 */         arc = new gov.noaa.nws.ncep.ui.pgen.elements.Arc(null, clr[0], 
/*  430 */           fArc.getLineWidth().floatValue(), fArc.getSizeScale().doubleValue(), fArc.isClosed().booleanValue(), 
/*  431 */           fArc.isFilled().booleanValue(), fArc.getSmoothFactor().intValue(), 
/*  432 */           FillPatternList.FillPattern.valueOf(fArc.getFillPattern()), fArc.getPgenType(), 
/*  433 */           (Coordinate)linePoints.get(0), (Coordinate)linePoints.get(1), 
/*  434 */           fArc.getPgenCategory(), fArc.getAxisRatio().doubleValue(), 
/*  435 */           fArc.getStartAngle().doubleValue(), fArc.getEndAngle().doubleValue());
/*      */ 
/*  437 */         des.add(arc);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  443 */     des.addAll(TrackConverter.getTrackElementListByTrackBeanList(elem.getTrack()));
/*      */     Object vtype;
/*      */     Object pgenType;
/*  445 */     if (!elem.getVector().isEmpty())
/*      */     {
/*  447 */       for (Vector fVector : elem.getVector())
/*      */       {
/*  449 */         java.awt.Color[] clr = new java.awt.Color[fVector.getColor().size()];
/*  450 */         int nn = 0;
/*  451 */         for (arc = fVector.getColor().iterator(); ((Iterator)arc).hasNext(); ) { Color fColor = (Color)((Iterator)arc).next();
/*  452 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  453 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  456 */         Point loc = fVector.getPoint();
/*      */ 
/*  458 */         vtype = null;
/*  459 */         pgenType = fVector.getPgenType();
/*  460 */         if (((String)pgenType).equalsIgnoreCase("Hash")) {
/*  461 */           vtype = IVector.VectorType.HASH_MARK;
/*      */         }
/*  463 */         else if (((String)pgenType).equalsIgnoreCase("Barb")) {
/*  464 */           vtype = IVector.VectorType.WIND_BARB;
/*      */         }
/*      */         else {
/*  467 */           vtype = IVector.VectorType.ARROW;
/*      */         }
/*      */ 
/*  470 */         gov.noaa.nws.ncep.ui.pgen.elements.Vector vector = new gov.noaa.nws.ncep.ui.pgen.elements.Vector(null, clr, 
/*  471 */           fVector.getLineWidth().floatValue(), fVector.getSizeScale().doubleValue(), 
/*  472 */           fVector.isClear(), new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/*  473 */           (IVector.VectorType)vtype, fVector.getSpeed().doubleValue(), fVector.getDirection().doubleValue(), 
/*  474 */           fVector.getArrowHeadSize().doubleValue(), fVector.isDirectionOnly().booleanValue(), 
/*  475 */           fVector.getPgenCategory(), fVector.getPgenType());
/*      */ 
/*  477 */         des.add(vector);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  482 */     if (!elem.getTCA().isEmpty())
/*      */     {
/*  484 */       for (TCA ftca : elem.getTCA())
/*      */       {
/*  507 */         TCAElement tca = new TCAElement();
/*  508 */         tca.setPgenType(ftca.getPgenType());
/*  509 */         tca.setPgenCategory(ftca.getPgenCategory());
/*      */ 
/*  511 */         tca.setStormNumber(ftca.getStormNumber().intValue());
/*  512 */         tca.setStormName(ftca.getStormName());
/*  513 */         tca.setBasin(ftca.getBasin());
/*  514 */         tca.setIssueStatus(ftca.getIssueStatus());
/*  515 */         tca.setStormType(ftca.getStormType());
/*  516 */         tca.setAdvisoryNumber(ftca.getAdvisoryNumber());
/*  517 */         tca.setTimeZone(ftca.getTimeZone());
/*  518 */         tca.setTextLocation(ftca.getTextLocation());
/*      */ 
/*  520 */         Calendar advTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*  521 */         XMLGregorianCalendar xmlCal = ftca.getAdvisoryTime();
/*  522 */         advTime.set(xmlCal.getYear(), xmlCal.getMonth() - 1, xmlCal.getDay(), 
/*  523 */           xmlCal.getHour(), xmlCal.getMinute(), xmlCal.getSecond());
/*  524 */         tca.setAdvisoryTime(advTime);
/*      */ 
/*  526 */         tca.setAdvisories(ftca.getAdvisories());
/*      */ 
/*  528 */         des.add(tca);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  533 */     if (!elem.getSpenes().isEmpty())
/*      */     {
/*  535 */       for (Spenes fspenes : elem.getSpenes()) {
/*  536 */         des.add(convertXML2Spenes(fspenes));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  541 */     if (!elem.getDECollection().isEmpty())
/*      */     {
/*  543 */       for (DECollection fdec : elem.getDECollection()) {
/*  544 */         String cname = fdec.getCollectionName();
/*  545 */         if (cname.equalsIgnoreCase("jet")) {
/*  546 */           Jet jet = convertXML2Jet(fdec);
/*  547 */           if (jet != null) {
/*  548 */             des.add(jet);
/*      */           }
/*      */         }
/*  551 */         else if (cname.equalsIgnoreCase("Cloud")) {
/*  552 */           des.add(convertXML2Cloud(fdec));
/*      */         }
/*  554 */         else if (cname.equalsIgnoreCase("Turbulence")) {
/*  555 */           des.add(convertXML2Turb(fdec));
/*  556 */         } else if (cname.contains("CCFP_SIGMET")) { des.add(convertXML2Ccfp(fdec));
/*      */         } else {
/*  558 */           gov.noaa.nws.ncep.ui.pgen.elements.DECollection dec = new gov.noaa.nws.ncep.ui.pgen.elements.DECollection(cname);
/*  559 */           dec.setPgenCategory(fdec.getPgenCategory());
/*  560 */           dec.setPgenType(fdec.getPgenType());
/*  561 */           dec.add(convert(fdec.getDrawableElement()));
/*  562 */           des.add(dec);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  567 */     if (!elem.getWatchBox().isEmpty()) {
/*  568 */       for (WatchBox fwb : elem.getWatchBox()) {
/*  569 */         des.add(convertXML2WatchBox(fwb));
/*      */       }
/*      */     }
/*      */ 
/*  573 */     if (!elem.getContours().isEmpty())
/*      */     {
/*  575 */       for (Contours fdec : elem.getContours()) {
/*  576 */         des.add(convertXML2Contours(fdec));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  581 */     if (!elem.getOutlook().isEmpty())
/*      */     {
/*  583 */       for (Outlook fotlk : elem.getOutlook())
/*  584 */         des.add(convertXML2Outlook(fotlk));
/*      */     }
/*      */     Object sigmet;
/*  589 */     if (!elem.getSigmet().isEmpty())
/*      */     {
/*  591 */       for (Sigmet fSig : elem.getSigmet())
/*      */       {
/*  593 */         java.awt.Color[] clr = new java.awt.Color[fSig.getColor().size()];
/*  594 */         int nn = 0;
/*  595 */         for (vtype = fSig.getColor().iterator(); ((Iterator)vtype).hasNext(); ) { Color fColor = (Color)((Iterator)vtype).next();
/*  596 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/*  597 */             fColor.getBlue(), fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  600 */         ArrayList sigmetPoints = new ArrayList();
/*  601 */         nn = 0;
/*  602 */         for (pgenType = fSig.getPoint().iterator(); ((Iterator)pgenType).hasNext(); ) { Point pt = (Point)((Iterator)pgenType).next();
/*  603 */           sigmetPoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */         }
/*      */ 
/*  606 */         sigmet = new gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet(null, clr, 
/*  607 */           fSig.getLineWidth().floatValue(), fSig.getSizeScale().doubleValue(), fSig.isClosed().booleanValue(), 
/*  608 */           fSig.isFilled().booleanValue(), sigmetPoints, 
/*  609 */           fSig.getSmoothFactor().intValue(), 
/*  610 */           FillPatternList.FillPattern.valueOf(fSig.getFillPattern()), 
/*  611 */           fSig.getPgenCategory(), fSig.getPgenType(), 
/*  612 */           fSig.getType(), fSig.getWidth().doubleValue(), 
/*  614 */           fSig.getEditableAttrArea(), 
/*  615 */           fSig.getEditableAttrIssueOffice(), 
/*  616 */           fSig.getEditableAttrStatus(), 
/*  617 */           fSig.getEditableAttrId(), 
/*  618 */           fSig.getEditableAttrSeqNum(), 
/*  619 */           fSig.getEditableAttrStartTime(), 
/*  620 */           fSig.getEditableAttrEndTime(), 
/*  621 */           fSig.getEditableAttrRemarks(), 
/*  622 */           fSig.getEditableAttrPhenom(), 
/*  623 */           fSig.getEditableAttrPhenom2(), 
/*  624 */           fSig.getEditableAttrPhenomName(), 
/*  625 */           fSig.getEditableAttrPhenomLat(), 
/*  626 */           fSig.getEditableAttrPhenomLon(), 
/*  627 */           fSig.getEditableAttrPhenomPressure(), 
/*  628 */           fSig.getEditableAttrPhenomMaxWind(), 
/*  629 */           fSig.getEditableAttrFreeText(), 
/*  630 */           fSig.getEditableAttrTrend(), 
/*  631 */           fSig.getEditableAttrMovement(), 
/*  632 */           fSig.getEditableAttrPhenomSpeed(), 
/*  633 */           fSig.getEditableAttrPhenomDirection(), 
/*  634 */           fSig.getEditableAttrLevel(), 
/*  635 */           fSig.getEditableAttrLevelInfo1(), 
/*  636 */           fSig.getEditableAttrLevelInfo2(), 
/*  637 */           fSig.getEditableAttrLevelText1(), 
/*  638 */           fSig.getEditableAttrLevelText2(), 
/*  639 */           fSig.getEditableAttrFromLine(), 
/*  640 */           fSig.getEditableAttrFir());
/*      */ 
/*  642 */         des.add(sigmet);
/*      */       }
/*      */     }
/*      */ 
/*  646 */     if (!elem.getGfa().isEmpty())
/*      */     {
/*  648 */       for (Gfa fgfa : elem.getGfa())
/*      */       {
/*  650 */         java.awt.Color[] clr = new java.awt.Color[fgfa.getColor().size()];
/*  651 */         int nn = 0;
/*  652 */         for (sigmet = fgfa.getColor().iterator(); ((Iterator)sigmet).hasNext(); ) { Color fColor = (Color)((Iterator)sigmet).next();
/*  653 */           clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), fColor.getBlue(), 
/*  654 */             fColor.getAlpha().intValue());
/*      */         }
/*      */ 
/*  657 */         ArrayList linePoints = new ArrayList();
/*  658 */         nn = 0;
/*  659 */         for (pgenType = fgfa.getPoint().iterator(); ((Iterator)pgenType).hasNext(); ) { Point pt = (Point)((Iterator)pgenType).next();
/*  660 */           linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */         }
/*      */ 
/*  663 */         Coordinate gfaTextCoordinate = new Coordinate(fgfa.getLonText().doubleValue(), fgfa.getLatText().doubleValue());
/*      */ 
/*  665 */         String haz = nvl(fgfa.getHazard());
/*  666 */         if (haz.equalsIgnoreCase("IFR")) {
/*  667 */           haz = new String("C&V");
/*      */         }
/*      */ 
/*  670 */         gov.noaa.nws.ncep.ui.pgen.gfa.Gfa gfa = new gov.noaa.nws.ncep.ui.pgen.gfa.Gfa(null, clr, fgfa.getLineWidth().floatValue(), fgfa.getSizeScale().doubleValue(), fgfa
/*  671 */           .isClosed().booleanValue(), fgfa.isFilled().booleanValue(), linePoints, gfaTextCoordinate, fgfa
/*  672 */           .getSmoothFactor().intValue(), FillPatternList.FillPattern.valueOf(fgfa.getFillPattern()), fgfa
/*  673 */           .getPgenCategory(), fgfa.getPgenType(), haz, fgfa.getFcstHr(), 
/*  674 */           fgfa.getTag(), fgfa.getDesk(), fgfa.getIssueType(), fgfa.getCycleDay().intValue(), 
/*  675 */           fgfa.getCycleHour().intValue(), fgfa.getType(), 
/*  676 */           fgfa.getArea(), fgfa.getBeginning(), fgfa.getEnding(), fgfa.getStates());
/*      */ 
/*  678 */         gfa.setGfaValue("GR", fgfa.getGr());
/*  679 */         gfa.setGfaValue("Frequency", fgfa.getFrequency());
/*  680 */         gfa.setGfaValue("Category", fgfa.getTsCategory());
/*  681 */         gfa.setGfaValue("FZL RANGE", fgfa.getFzlRange());
/*  682 */         gfa.setGfaValue("Level", fgfa.getLevel());
/*  683 */         gfa.setGfaValue("Intensity", fgfa.getIntensity());
/*  684 */         gfa.setGfaValue("Speed", fgfa.getSpeed());
/*  685 */         gfa.setGfaValue("DUE TO", fgfa.getDueTo());
/*  686 */         gfa.setGfaValue("LYR", fgfa.getLyr());
/*  687 */         gfa.setGfaValue("Coverage", fgfa.getCoverage());
/*  688 */         gfa.setGfaValue("Bottom", fgfa.getBottom());
/*  689 */         gfa.setGfaValue("Top", fgfa.getTop());
/*  690 */         if ((fgfa.getTop() != null) && (fgfa.getBottom() != null)) {
/*  691 */           gfa.setGfaValue("Top/Bottom", fgfa.getTop() + "/" + fgfa.getBottom());
/*      */         }
/*  693 */         gfa.setGfaValue("FZL Top/Bottom", fgfa.getFzlTopBottom());
/*  694 */         gfa.setGfaValue("Contour", fgfa.getContour());
/*  695 */         if ("ICE".equals(gfa.getGfaHazard())) {
/*  696 */           gfa.setGfaType("");
/*  697 */           gfa.setGfaValue("Type", fgfa.getType());
/*      */         }
/*      */ 
/*  700 */         String cig = fgfa.getCig();
/*  701 */         if (cig != null) {
/*  702 */           gfa.setGfaValue("CIG", fgfa.getCig());
/*      */         }
/*      */         else {
/*  705 */           gfa.setGfaValue("CIG", "");
/*      */         }
/*      */ 
/*  708 */         String vis = fgfa.getVis();
/*  709 */         if (vis != null) {
/*  710 */           gfa.setGfaValue("VIS", fgfa.getVis());
/*      */         }
/*      */         else {
/*  713 */           gfa.setGfaValue("VIS", "");
/*      */         }
/*      */ 
/*  716 */         String airmetTag = fgfa.getAirmetTag();
/*  717 */         if (airmetTag != null) {
/*  718 */           gfa.setGfaValue("AIRMET_TAG", airmetTag);
/*      */         }
/*      */         else {
/*  721 */           String prefix = "";
/*  722 */           if (gfa.getGfaHazard().equals("TURB-HI")) {
/*  723 */             prefix = "H";
/*      */           }
/*  725 */           else if (gfa.getGfaHazard().equals("TURB-LO")) {
/*  726 */             prefix = "L";
/*      */           }
/*      */ 
/*  729 */           gfa.setGfaValue("AIRMET_TAG", new String(prefix + gfa.getGfaTag() + gfa.getGfaDesk()));
/*      */         }
/*      */ 
/*  733 */         String timeStr = fgfa.getIssueTime();
/*  734 */         if ((timeStr != null) && (timeStr.trim().length() >= 6)) {
/*  735 */           Calendar issueTimeCal = Calendar.getInstance();
/*  736 */           int day = Integer.parseInt(timeStr.substring(0, 2));
/*  737 */           int hour = Integer.parseInt(timeStr.substring(2, 4));
/*  738 */           int min = Integer.parseInt(timeStr.substring(4));
/*  739 */           issueTimeCal.set(5, day);
/*  740 */           issueTimeCal.set(11, hour);
/*  741 */           issueTimeCal.set(12, min);
/*  742 */           issueTimeCal.set(13, 0);
/*  743 */           gfa.addAttribute("ISSUE_TIME", issueTimeCal);
/*      */         }
/*      */ 
/*  746 */         String timeStr2 = fgfa.getUntilTime();
/*  747 */         if ((timeStr2 != null) && (timeStr2.trim().length() >= 6)) {
/*  748 */           Calendar untilTimeCal = Calendar.getInstance();
/*  749 */           int day = Integer.parseInt(timeStr.substring(0, 2));
/*  750 */           int hour = Integer.parseInt(timeStr.substring(2, 4));
/*  751 */           int min = Integer.parseInt(timeStr.substring(4));
/*  752 */           untilTimeCal.set(5, day);
/*  753 */           untilTimeCal.set(11, hour);
/*  754 */           untilTimeCal.set(12, min);
/*  755 */           untilTimeCal.set(13, 0);
/*  756 */           gfa.addAttribute("UNTIL_TIME", untilTimeCal);
/*      */         }
/*      */ 
/*  759 */         String timeStr3 = fgfa.getOutlookEndTime();
/*  760 */         if ((timeStr3 != null) && (timeStr3.trim().length() >= 6)) {
/*  761 */           Calendar otlkEndTimeCal = Calendar.getInstance();
/*  762 */           int day = Integer.parseInt(timeStr.substring(0, 2));
/*  763 */           int hour = Integer.parseInt(timeStr.substring(2, 4));
/*  764 */           int min = Integer.parseInt(timeStr.substring(4));
/*  765 */           otlkEndTimeCal.set(5, day);
/*  766 */           otlkEndTimeCal.set(11, hour);
/*  767 */           otlkEndTimeCal.set(12, min);
/*  768 */           otlkEndTimeCal.set(13, 0);
/*  769 */           gfa.addAttribute("OUTLOOK_END_TIME", otlkEndTimeCal);
/*      */         }
/*      */ 
/*  772 */         des.add(gfa);
/*      */       }
/*      */     }
/*      */ 
/*  776 */     if (!elem.getVolcano().isEmpty())
/*      */     {
/*  778 */       for (Volcano fVol : elem.getVolcano()) {
/*  779 */         des.add(convertXML2Volcano(fVol));
/*      */       }
/*      */     }
/*      */ 
/*  783 */     if (!elem.getTcm().isEmpty())
/*      */     {
/*  785 */       for (TCM ftcm : elem.getTcm()) {
/*  786 */         des.add(convertXML2Tcm(ftcm));
/*      */       }
/*      */     }
/*      */ 
/*  790 */     return des;
/*      */   }
/*      */ 
/*      */   public static Products convert(List<gov.noaa.nws.ncep.ui.pgen.elements.Product> prds)
/*      */   {
/*  800 */     Products fprds = new Products();
/*      */ 
/*  802 */     for (gov.noaa.nws.ncep.ui.pgen.elements.Product prd : prds)
/*      */     {
/*  804 */       Product p = 
/*  805 */         new Product();
/*      */ 
/*  807 */       p.setName(prd.getName());
/*  808 */       p.setType(prd.getType());
/*  809 */       p.setForecaster(prd.getForecaster());
/*  810 */       p.setCenter(prd.getCenter());
/*      */ 
/*  812 */       String outFile = prd.getOutputFile();
/*  813 */       if (outFile != null) {
/*  814 */         p.setOutputFile(outFile);
/*      */       }
/*      */       else {
/*  817 */         p.setOutputFile(null);
/*      */       }
/*      */ 
/*  820 */       p.setInputFile(null);
/*  821 */       p.setUseFile(Boolean.valueOf(false));
/*  822 */       p.setOnOff(Boolean.valueOf(prd.isOnOff()));
/*  823 */       p.setSaveLayers(Boolean.valueOf(prd.isSaveLayers()));
/*      */ 
/*  825 */       p.getLayer().addAll(convertLayers(prd.getLayers()));
/*      */ 
/*  827 */       fprds.getProduct().add(p);
/*      */     }
/*      */ 
/*  831 */     return fprds;
/*      */   }
/*      */ 
/*      */   private static List<Layer> convertLayers(List<gov.noaa.nws.ncep.ui.pgen.elements.Layer> layers)
/*      */   {
/*  839 */     List flyrs = 
/*  840 */       new ArrayList();
/*      */ 
/*  842 */     for (gov.noaa.nws.ncep.ui.pgen.elements.Layer lyr : layers) {
/*  843 */       Layer l = 
/*  844 */         new Layer();
/*      */ 
/*  846 */       if (lyr.getName() != null) {
/*  847 */         l.setName(lyr.getName());
/*      */       }
/*      */ 
/*  850 */       Color clr = 
/*  851 */         new Color();
/*      */ 
/*  853 */       if (lyr.getColor() != null)
/*      */       {
/*  855 */         clr.setRed(lyr.getColor().getRed());
/*  856 */         clr.setGreen(lyr.getColor().getGreen());
/*  857 */         clr.setBlue(lyr.getColor().getBlue());
/*  858 */         clr.setAlpha(Integer.valueOf(lyr.getColor().getAlpha()));
/*      */       }
/*      */       else
/*      */       {
/*  863 */         clr.setRed(0);
/*  864 */         clr.setGreen(255);
/*  865 */         clr.setBlue(255);
/*  866 */         clr.setAlpha(Integer.valueOf(255));
/*      */       }
/*      */ 
/*  869 */       l.setColor(clr);
/*      */ 
/*  871 */       l.setOnOff(Boolean.valueOf(lyr.isOnOff()));
/*  872 */       l.setMonoColor(Boolean.valueOf(lyr.isMonoColor()));
/*  873 */       l.setFilled(Boolean.valueOf(lyr.isFilled()));
/*  874 */       l.setInputFile(null);
/*  875 */       l.setOutputFile(null);
/*      */ 
/*  877 */       l.setDrawableElement(convertDEs(lyr.getDrawables()));
/*      */ 
/*  879 */       flyrs.add(l);
/*      */     }
/*      */ 
/*  882 */     return flyrs;
/*      */   }
/*      */ 
/*      */   private static DrawableElement convertDEs(List<AbstractDrawableComponent> des)
/*      */   {
/*  891 */     DrawableElement fde = 
/*  892 */       new DrawableElement();
/*      */ 
/*  894 */     for (AbstractDrawableComponent adc : des) {
/*  895 */       if ((adc instanceof gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement)) {
/*  896 */         gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement de = (gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement)adc;
/*      */         String haz;
/*      */         Calendar cal;
/*      */         Object sdf;
/*  897 */         if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Line))
/*      */         {
/*  899 */           if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Arc)) {
/*  900 */             Arc arc = 
/*  901 */               new Arc();
/*      */ 
/*  903 */             for (java.awt.Color clr : de.getColors())
/*      */             {
/*  905 */               Color fclr = 
/*  906 */                 new Color();
/*      */ 
/*  908 */               fclr.setRed(clr.getRed());
/*  909 */               fclr.setGreen(clr.getGreen());
/*  910 */               fclr.setBlue(clr.getBlue());
/*  911 */               fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/*  913 */               arc.getColor().add(fclr);
/*      */             }
/*      */ 
/*  916 */             for (Coordinate crd : ((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getLinePoints())
/*      */             {
/*  918 */               Point fpt = new Point();
/*  919 */               fpt.setLat(Double.valueOf(crd.y));
/*  920 */               fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/*  922 */               arc.getPoint().add(fpt);
/*      */             }
/*      */ 
/*  925 */             arc.setPgenCategory(de.getPgenCategory());
/*  926 */             arc.setLineWidth(Float.valueOf(de.getLineWidth()));
/*  927 */             arc.setSizeScale(Double.valueOf(de.getSizeScale()));
/*  928 */             arc.setSmoothFactor(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getSmoothFactor()));
/*  929 */             arc.setClosed(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).isClosedLine());
/*  930 */             arc.setFilled(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).isFilled());
/*  931 */             arc.setPgenType(de.getPgenType());
/*      */ 
/*  933 */             arc.setFillPattern(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getFillPattern().name());
/*  934 */             arc.setAxisRatio(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getAxisRatio()));
/*  935 */             arc.setStartAngle(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getStartAngle()));
/*  936 */             arc.setEndAngle(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Arc)de).getEndAngle()));
/*      */ 
/*  938 */             fde.getArc().add(arc);
/*      */           }
/*  941 */           else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)) {
/*  942 */             Gfa fgfa = 
/*  943 */               new Gfa();
/*      */ 
/*  945 */             for (java.awt.Color clr : de.getColors())
/*      */             {
/*  947 */               Color fclr = 
/*  948 */                 new Color();
/*      */ 
/*  950 */               fclr.setRed(clr.getRed());
/*  951 */               fclr.setGreen(clr.getGreen());
/*  952 */               fclr.setBlue(clr.getBlue());
/*  953 */               fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/*  955 */               fgfa.getColor().add(fclr);
/*      */             }
/*      */ 
/*  958 */             for (Coordinate crd : ((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).getLinePoints())
/*      */             {
/*  960 */               Point fpt = new Point();
/*  961 */               fpt.setLat(Double.valueOf(crd.y));
/*  962 */               fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/*  964 */               fgfa.getPoint().add(fpt);
/*      */             }
/*      */ 
/*  967 */             fgfa.setPgenCategory(de.getPgenCategory());
/*  968 */             fgfa.setLineWidth(Float.valueOf(de.getLineWidth()));
/*  969 */             fgfa.setSizeScale(Double.valueOf(de.getSizeScale()));
/*  970 */             fgfa.setSmoothFactor(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).getSmoothFactor()));
/*  971 */             fgfa.setClosed(((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).isClosedLine());
/*  972 */             fgfa.setFilled(((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).isFilled());
/*  973 */             fgfa.setPgenType(de.getPgenType());
/*  974 */             gov.noaa.nws.ncep.ui.pgen.gfa.Gfa e = (gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de;
/*  975 */             if (e.getGfaTextCoordinate() != null) {
/*  976 */               fgfa.setLatText(Double.valueOf(e.getGfaTextCoordinate().y));
/*  977 */               fgfa.setLonText(Double.valueOf(e.getGfaTextCoordinate().x));
/*      */             }
/*      */ 
/*  980 */             haz = nvl(e.getGfaHazard());
/*  981 */             if (haz.equalsIgnoreCase("C&V")) {
/*  982 */               haz = new String("IFR");
/*      */             }
/*  984 */             fgfa.setHazard(haz);
/*      */ 
/*  986 */             fgfa.setFcstHr(nvl(e.getGfaFcstHr()));
/*  987 */             fgfa.setTag(nvl(e.getGfaTag()));
/*  988 */             fgfa.setDesk(nvl(e.getGfaDesk()));
/*  989 */             fgfa.setIssueType(nvl(e.getGfaIssueType()));
/*  990 */             fgfa.setCycleDay(Integer.valueOf(e.getGfaCycleDay()));
/*  991 */             fgfa.setCycleHour(Integer.valueOf(e.getGfaCycleHour()));
/*  992 */             fgfa.setType(nvl(e.getGfaType()));
/*  993 */             fgfa.setArea(nvl(e.getGfaArea()));
/*  994 */             fgfa.setBeginning(nvl(e.getGfaBeginning()));
/*  995 */             fgfa.setEnding(nvl(e.getGfaEnding()));
/*  996 */             fgfa.setStates(nvl(e.getGfaStates()));
/*  997 */             fgfa.setGr(nvl(e.getGfaValue("GR")));
/*  998 */             fgfa.setFrequency(nvl(e.getGfaValue("Frequency")));
/*  999 */             fgfa.setTsCategory(nvl(e.getGfaValue("Category")));
/* 1000 */             fgfa.setFzlRange(nvl(e.getGfaValue("FZL RANGE")));
/* 1001 */             fgfa.setLevel(nvl(e.getGfaValue("Level")));
/* 1002 */             fgfa.setIntensity(nvl(e.getGfaValue("Intensity")));
/* 1003 */             fgfa.setSpeed(nvl(e.getGfaValue("Speed")));
/* 1004 */             fgfa.setDueTo(nvl(e.getGfaValue("DUE TO")));
/* 1005 */             fgfa.setLyr(nvl(e.getGfaValue("LYR")));
/* 1006 */             fgfa.setCoverage(nvl(e.getGfaValue("Coverage")));
/* 1007 */             fgfa.setBottom(nvl(e.getGfaBottom()));
/* 1008 */             fgfa.setTop(nvl(e.getGfaTop()));
/* 1009 */             fgfa.setFzlTopBottom(nvl(e.getGfaValue("FZL Top/Bottom")));
/* 1010 */             fgfa.setContour(nvl(e.getGfaValue("Contour")));
/* 1011 */             fgfa.setIsOutlook(Boolean.valueOf(e.isOutlook()));
/* 1012 */             if ("ICE".equals(e.getGfaHazard())) {
/* 1013 */               fgfa.setType(nvl(e.getGfaValue("Type")));
/*      */             }
/*      */ 
/* 1016 */             fgfa.setCig(nvl(e.getGfaValue("CIG")));
/* 1017 */             fgfa.setVis(nvl(e.getGfaValue("VIS")));
/*      */ 
/* 1019 */             if (e.getGfaValue("AIRMET_TAG") != null) {
/* 1020 */               fgfa.setAirmetTag(e.getGfaValue("AIRMET_TAG"));
/*      */             }
/*      */             else {
/* 1023 */               String prefix = "";
/* 1024 */               if (e.getGfaHazard().equals("TURB-HI")) {
/* 1025 */                 prefix = "H";
/*      */               }
/* 1027 */               else if (e.getGfaHazard().equals("TURB-LO")) {
/* 1028 */                 prefix = "L";
/*      */               }
/*      */ 
/* 1031 */               fgfa.setAirmetTag(new String(prefix + e.getGfaTag() + e.getGfaDesk()));
/*      */             }
/*      */ 
/* 1034 */             cal = (Calendar)e.getAttribute("ISSUE_TIME", Calendar.class);
/* 1035 */             sdf = new SimpleDateFormat("ddHHmm");
/* 1036 */             if (cal != null) {
/* 1037 */               fgfa.setIssueTime(((SimpleDateFormat)sdf).format(cal.getTime()));
/*      */             }
/*      */ 
/* 1040 */             cal = (Calendar)e.getAttribute("UNTIL_TIME", Calendar.class);
/* 1041 */             if (cal != null) {
/* 1042 */               fgfa.setUntilTime(((SimpleDateFormat)sdf).format(cal.getTime()));
/*      */             }
/*      */ 
/* 1045 */             cal = (Calendar)e.getAttribute("OUTLOOK_END_TIME", Calendar.class);
/* 1046 */             if (cal != null) {
/* 1047 */               fgfa.setOutlookEndTime(((SimpleDateFormat)sdf).format(cal.getTime()));
/*      */             }
/*      */ 
/* 1050 */             if (e.getAttribute("WORDING") != null) {
/* 1051 */               GfaWording w = (GfaWording)e.getAttribute("WORDING", GfaWording.class);
/* 1052 */               fgfa.setFromCondsDvlpg(GfaRules.replacePlusWithCycle(w
/* 1053 */                 .getFromCondsDvlpg(), e.getGfaCycleHour()));
/* 1054 */               fgfa.setFromCondsEndg(GfaRules.replacePlusWithCycle(w
/* 1055 */                 .getFromCondsEndg(), e.getGfaCycleHour()));
/* 1056 */               fgfa.setCondsContg(GfaRules.replacePlusWithCycle(w.getCondsContg(), e
/* 1057 */                 .getGfaCycleHour()));
/* 1058 */               fgfa.setOtlkCondsDvlpg(GfaRules.replacePlusWithCycle(w
/* 1059 */                 .getOtlkCondsDvlpg(), e.getGfaCycleHour()));
/* 1060 */               fgfa.setOtlkCondsEndg(GfaRules.replacePlusWithCycle(w
/* 1061 */                 .getOtlkCondsEndg(), e.getGfaCycleHour()));
/*      */             }
/*      */ 
/* 1065 */             fgfa.setTextVor(nvl(((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).getGfaVorText()));
/*      */ 
/* 1067 */             fgfa.setFillPattern(nvl(((gov.noaa.nws.ncep.ui.pgen.gfa.Gfa)de).getFillPattern().name()));
/*      */ 
/* 1069 */             fde.getGfa().add(fgfa);
/*      */           }
/* 1071 */           else if ((de instanceof Track)) {
/* 1072 */             fde.getTrack().add(TrackConverter.getTrackBeanByTrackElement((Track)de));
/* 1073 */           } else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet))
/*      */           {
/* 1075 */             Sigmet sigmet = 
/* 1076 */               new Sigmet();
/*      */ 
/* 1078 */             String str8 = (sdf = de.getColors()).length; for (String str1 = 0; str1 < str8; str1++) { java.awt.Color clr = sdf[str1];
/*      */ 
/* 1080 */               Color fclr = 
/* 1081 */                 new Color();
/*      */ 
/* 1083 */               fclr.setRed(clr.getRed());
/* 1084 */               fclr.setGreen(clr.getGreen());
/* 1085 */               fclr.setBlue(clr.getBlue());
/* 1086 */               fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/* 1088 */               sigmet.getColor().add(fclr);
/*      */             }
/*      */ 
/* 1091 */             String str9 = (sdf = ((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getLinePoints()).length; for (String str2 = 0; str2 < str9; str2++) { Coordinate crd = sdf[str2];
/*      */ 
/* 1093 */               Point fpt = new Point();
/* 1094 */               fpt.setLat(Double.valueOf(crd.y));
/* 1095 */               fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/* 1097 */               sigmet.getPoint().add(fpt);
/*      */             }
/*      */ 
/* 1100 */             sigmet.setPgenCategory(de.getPgenCategory());
/* 1101 */             sigmet.setLineWidth(Float.valueOf(de.getLineWidth()));
/* 1102 */             sigmet.setSizeScale(Double.valueOf(de.getSizeScale()));
/* 1103 */             sigmet.setSmoothFactor(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getSmoothFactor()));
/* 1104 */             sigmet.setClosed(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).isClosedLine());
/* 1105 */             sigmet.setFilled(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).isFilled());
/* 1106 */             sigmet.setPgenType(de.getPgenType());
/* 1107 */             sigmet.setFillPattern(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getFillPattern().name());
/*      */ 
/* 1109 */             sigmet.setType(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getType());
/* 1110 */             sigmet.setWidth(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getWidth()));
/*      */ 
/* 1112 */             sigmet.setEditableAttrArea(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrArea());
/* 1113 */             sigmet.setEditableAttrIssueOffice(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrIssueOffice());
/* 1114 */             sigmet.setEditableAttrStatus(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrStatus());
/* 1115 */             sigmet.setEditableAttrId(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrId());
/* 1116 */             sigmet.setEditableAttrSeqNum(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrSeqNum());
/* 1117 */             sigmet.setEditableAttrStartTime(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrStartTime());
/* 1118 */             sigmet.setEditableAttrEndTime(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrEndTime());
/* 1119 */             sigmet.setEditableAttrRemarks(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrRemarks());
/* 1120 */             sigmet.setEditableAttrPhenom(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenom());
/* 1121 */             sigmet.setEditableAttrPhenom2(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenom2());
/* 1122 */             sigmet.setEditableAttrPhenomName(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomName());
/* 1123 */             sigmet.setEditableAttrPhenomLat(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomLat());
/* 1124 */             sigmet.setEditableAttrPhenomLon(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomLon());
/* 1125 */             sigmet.setEditableAttrPhenomPressure(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomPressure());
/* 1126 */             sigmet.setEditableAttrPhenomMaxWind(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomMaxWind());
/* 1127 */             sigmet.setEditableAttrFreeText(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrFreeText());
/* 1128 */             sigmet.setEditableAttrTrend(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrTrend());
/* 1129 */             sigmet.setEditableAttrMovement(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrMovement());
/* 1130 */             sigmet.setEditableAttrPhenomSpeed(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomSpeed());
/* 1131 */             sigmet.setEditableAttrPhenomDirection(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrPhenomDirection());
/* 1132 */             sigmet.setEditableAttrLevel(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrLevel());
/* 1133 */             sigmet.setEditableAttrLevelInfo1(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrLevelInfo1());
/* 1134 */             sigmet.setEditableAttrLevelInfo2(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrLevelInfo2());
/* 1135 */             sigmet.setEditableAttrLevelText1(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrLevelText1());
/* 1136 */             sigmet.setEditableAttrLevelText2(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrLevelText2());
/* 1137 */             sigmet.setEditableAttrFromLine(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrFromLine());
/* 1138 */             sigmet.setEditableAttrFir(((gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet)de).getEditableAttrFir());
/*      */ 
/* 1140 */             fde.getSigmet().add(sigmet);
/*      */           }
/* 1143 */           else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Spenes)) {
/* 1144 */             fde.getSpenes().add(convertSpenes2XML((gov.noaa.nws.ncep.ui.pgen.elements.Spenes)de));
/*      */           }
/*      */           else {
/* 1147 */             Line line = 
/* 1148 */               new Line();
/*      */ 
/* 1150 */             String str10 = (sdf = de.getColors()).length; for (String str3 = 0; str3 < str10; str3++) { java.awt.Color clr = sdf[str3];
/*      */ 
/* 1152 */               Color fclr = 
/* 1153 */                 new Color();
/*      */ 
/* 1155 */               fclr.setRed(clr.getRed());
/* 1156 */               fclr.setGreen(clr.getGreen());
/* 1157 */               fclr.setBlue(clr.getBlue());
/* 1158 */               fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/* 1160 */               line.getColor().add(fclr);
/*      */             }
/*      */ 
/* 1163 */             String str11 = (sdf = ((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).getLinePoints()).length; for (String str4 = 0; str4 < str11; str4++) { Coordinate crd = sdf[str4];
/*      */ 
/* 1165 */               Point fpt = new Point();
/* 1166 */               fpt.setLat(Double.valueOf(crd.y));
/* 1167 */               fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/* 1169 */               line.getPoint().add(fpt);
/*      */             }
/*      */ 
/* 1172 */             line.setPgenCategory(de.getPgenCategory());
/* 1173 */             line.setLineWidth(Float.valueOf(de.getLineWidth()));
/* 1174 */             line.setSizeScale(Double.valueOf(de.getSizeScale()));
/* 1175 */             line.setSmoothFactor(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).getSmoothFactor()));
/* 1176 */             line.setClosed(((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).isClosedLine());
/* 1177 */             line.setFilled(((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).isFilled());
/* 1178 */             line.setPgenType(de.getPgenType());
/*      */ 
/* 1180 */             line.setFillPattern(((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).getFillPattern().name());
/* 1181 */             line.setFlipSide(((gov.noaa.nws.ncep.ui.pgen.elements.Line)de).isFlipSide());
/*      */ 
/* 1183 */             fde.getLine().add(line);
/*      */           }
/*      */ 
/*      */         }
/* 1187 */         else if (((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Symbol)) || ((de instanceof ComboSymbol)))
/*      */         {
/* 1189 */           Symbol symbol = 
/* 1190 */             new Symbol();
/*      */ 
/* 1192 */           String str12 = (sdf = de.getColors()).length; for (String str5 = 0; str5 < str12; str5++) { java.awt.Color clr = sdf[str5];
/*      */ 
/* 1194 */             Color fclr = 
/* 1195 */               new Color();
/*      */ 
/* 1197 */             fclr.setRed(clr.getRed());
/* 1198 */             fclr.setGreen(clr.getGreen());
/* 1199 */             fclr.setBlue(clr.getBlue());
/* 1200 */             fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/* 1202 */             symbol.getColor().add(fclr);
/*      */           }
/*      */ 
/* 1205 */           Point fpt = new Point();
/* 1206 */           fpt.setLat(Double.valueOf(((ISinglePoint)de).getLocation().y));
/* 1207 */           fpt.setLon(Double.valueOf(((ISinglePoint)de).getLocation().x));
/* 1208 */           symbol.setPoint(fpt);
/*      */ 
/* 1210 */           symbol.setPgenType(de.getPgenType());
/* 1211 */           symbol.setPgenCategory(de.getPgenCategory());
/* 1212 */           symbol.setLineWidth(Float.valueOf(de.getLineWidth()));
/* 1213 */           symbol.setSizeScale(Double.valueOf(de.getSizeScale()));
/* 1214 */           symbol.setClear(((ISinglePoint)de).isClear());
/*      */ 
/* 1216 */           fde.getSymbol().add(symbol);
/*      */         }
/*      */         else
/*      */         {
/*      */           Color fclr;
/* 1218 */           if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.AvnText))
/*      */           {
/* 1220 */             AvnText atext = 
/* 1221 */               new AvnText();
/*      */ 
/* 1223 */             String str13 = (sdf = de.getColors()).length; for (String str6 = 0; str6 < str13; str6++) { java.awt.Color clr = sdf[str6];
/*      */ 
/* 1225 */               fclr = 
/* 1226 */                 new Color();
/*      */ 
/* 1228 */               fclr.setRed(clr.getRed());
/* 1229 */               fclr.setGreen(clr.getGreen());
/* 1230 */               fclr.setBlue(clr.getBlue());
/* 1231 */               fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 1232 */               atext.getColor().add(fclr);
/*      */             }
/*      */ 
/* 1235 */             Point fpt = new Point();
/* 1236 */             fpt.setLat(Double.valueOf(((ISinglePoint)de).getLocation().y));
/* 1237 */             fpt.setLon(Double.valueOf(((ISinglePoint)de).getLocation().x));
/* 1238 */             atext.setPoint(fpt);
/*      */ 
/* 1240 */             atext.setAvnTextType(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getAvnTextType().name());
/* 1241 */             atext.setTopValue(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getTopValue());
/* 1242 */             atext.setBottomValue(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getBottomValue());
/* 1243 */             atext.setJustification(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getJustification().name());
/* 1244 */             atext.setStyle(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getStyle().name());
/* 1245 */             atext.setFontName(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getFontName());
/* 1246 */             atext.setFontSize(Float.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getFontSize()));
/* 1247 */             atext.setSymbolPatternName(((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)de).getSymbolPatternName());
/* 1248 */             atext.setPgenType(de.getPgenType());
/* 1249 */             atext.setPgenCategory(de.getPgenCategory());
/*      */ 
/* 1251 */             fde.getAvnText().add(atext);
/*      */           }
/*      */           else
/*      */           {
/*      */             Object localObject4;
/*      */             Point fpt;
/* 1253 */             if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText))
/*      */             {
/* 1255 */               gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText mcde = (gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText)de;
/*      */ 
/* 1257 */               MidCloudText mtext = 
/* 1258 */                 new MidCloudText();
/*      */ 
/* 1260 */               localObject4 = (fclr = de.getColors()).length; for (Object localObject1 = 0; localObject1 < localObject4; localObject1++) { java.awt.Color clr = fclr[localObject1];
/*      */ 
/* 1262 */                 Color fclr = 
/* 1263 */                   new Color();
/*      */ 
/* 1265 */                 fclr.setRed(clr.getRed());
/* 1266 */                 fclr.setGreen(clr.getGreen());
/* 1267 */                 fclr.setBlue(clr.getBlue());
/* 1268 */                 fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 1269 */                 mtext.getColor().add(fclr);
/*      */               }
/*      */ 
/* 1272 */               fpt = new Point();
/* 1273 */               fpt.setLat(Double.valueOf(((ISinglePoint)de).getLocation().y));
/* 1274 */               fpt.setLon(Double.valueOf(((ISinglePoint)de).getLocation().x));
/* 1275 */               mtext.setPoint(fpt);
/*      */ 
/* 1277 */               mtext.setCloudTypes(mcde.getCloudTypes());
/* 1278 */               mtext.setCloudAmounts(mcde.getCloudAmounts());
/* 1279 */               mtext.setTurbulenceType(mcde.getTurbulencePattern());
/* 1280 */               mtext.setTurbulenceLevels(mcde.getTurbulenceLevels());
/* 1281 */               mtext.setIcingType(mcde.getIcingPattern());
/* 1282 */               mtext.setIcingLevels(mcde.getIcingLevels());
/* 1283 */               mtext.setTstormTypes(mcde.getTstormTypes());
/* 1284 */               mtext.setTstormLevels(mcde.getTstormLevels());
/*      */ 
/* 1286 */               mtext.setJustification(mcde.getJustification().name());
/* 1287 */               mtext.setStyle(mcde.getStyle().name());
/* 1288 */               mtext.setFontName(mcde.getFontName());
/* 1289 */               mtext.setFontSize(Float.valueOf(mcde.getFontSize()));
/* 1290 */               mtext.setPgenType(mcde.getPgenType());
/* 1291 */               mtext.setPgenCategory(mcde.getPgenCategory());
/*      */ 
/* 1293 */               fde.getMidCloudText().add(mtext);
/*      */             }
/*      */             else
/*      */             {
/*      */               Object localObject5;
/*      */               String st;
/* 1295 */               if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text))
/*      */               {
/* 1297 */                 Text text = 
/* 1298 */                   new Text();
/*      */ 
/* 1300 */                 Point localPoint2 = (localObject4 = de.getColors()).length;
/*      */                 Color fclr;
/* 1300 */                 for (Point localPoint1 = 0; localPoint1 < localPoint2; localPoint1++) { java.awt.Color clr = localObject4[localPoint1];
/*      */ 
/* 1302 */                   fclr = 
/* 1303 */                     new Color();
/*      */ 
/* 1305 */                   fclr.setRed(clr.getRed());
/* 1306 */                   fclr.setGreen(clr.getGreen());
/* 1307 */                   fclr.setBlue(clr.getBlue());
/* 1308 */                   fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 1309 */                   text.getColor().add(fclr);
/*      */                 }
/*      */ 
/* 1312 */                 Point fpt = new Point();
/* 1313 */                 fpt.setLat(Double.valueOf(((ISinglePoint)de).getLocation().y));
/* 1314 */                 fpt.setLon(Double.valueOf(((ISinglePoint)de).getLocation().x));
/* 1315 */                 text.setPoint(fpt);
/*      */ 
/* 1317 */                 localObject5 = (fclr = ((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getString()).length; for (Object localObject2 = 0; localObject2 < localObject5; localObject2++) { st = fclr[localObject2];
/* 1318 */                   text.getTextLine().add(new String(st));
/*      */                 }
/*      */ 
/* 1321 */                 text.setXOffset(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getXOffset()));
/* 1322 */                 text.setYOffset(Integer.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getYOffset()));
/* 1323 */                 text.setDisplayType(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getDisplayType().name());
/* 1324 */                 text.setMask(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).maskText());
/* 1325 */                 text.setRotationRelativity(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getRotationRelativity().name());
/* 1326 */                 text.setRotation(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getRotation()));
/* 1327 */                 text.setJustification(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getJustification().name());
/* 1328 */                 text.setStyle(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getStyle().name());
/* 1329 */                 text.setFontName(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getFontName());
/* 1330 */                 text.setFontSize(Float.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getFontSize()));
/* 1331 */                 text.setPgenType(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getPgenType());
/* 1332 */                 text.setPgenCategory(de.getPgenCategory());
/*      */ 
/* 1334 */                 text.setHide(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getHide());
/* 1335 */                 text.setAuto(((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getAuto());
/*      */ 
/* 1337 */                 fde.getText().add(text);
/*      */               }
/* 1340 */               else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Vector))
/*      */               {
/* 1342 */                 Vector vector = 
/* 1343 */                   new Vector();
/*      */ 
/* 1345 */                 String str14 = (localObject5 = de.getColors()).length; for (String str7 = 0; str7 < str14; str7++) { java.awt.Color clr = localObject5[str7];
/*      */ 
/* 1347 */                   Color fclr = 
/* 1348 */                     new Color();
/*      */ 
/* 1350 */                   fclr.setRed(clr.getRed());
/* 1351 */                   fclr.setGreen(clr.getGreen());
/* 1352 */                   fclr.setBlue(clr.getBlue());
/* 1353 */                   fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/*      */ 
/* 1355 */                   vector.getColor().add(fclr);
/*      */                 }
/*      */ 
/* 1358 */                 Point fpt = new Point();
/* 1359 */                 fpt.setLat(Double.valueOf(((ISinglePoint)de).getLocation().y));
/* 1360 */                 fpt.setLon(Double.valueOf(((ISinglePoint)de).getLocation().x));
/* 1361 */                 vector.setPoint(fpt);
/*      */ 
/* 1363 */                 vector.setPgenType(de.getPgenType());
/* 1364 */                 vector.setPgenCategory(de.getPgenCategory());
/*      */ 
/* 1366 */                 vector.setLineWidth(Float.valueOf(de.getLineWidth()));
/* 1367 */                 vector.setSizeScale(Double.valueOf(de.getSizeScale()));
/* 1368 */                 vector.setClear(((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).isClear());
/*      */ 
/* 1370 */                 vector.setDirection(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).getDirection()));
/* 1371 */                 vector.setSpeed(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).getSpeed()));
/* 1372 */                 vector.setArrowHeadSize(Double.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).getArrowHeadSize()));
/* 1373 */                 vector.setDirectionOnly(Boolean.valueOf(((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).hasDirectionOnly()));
/*      */ 
/* 1375 */                 fde.getVector().add(vector);
/*      */               }
/* 1378 */               else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.WatchBox)) {
/* 1379 */                 fde.getWatchBox().add(convertWatchBox2XML((gov.noaa.nws.ncep.ui.pgen.elements.WatchBox)de));
/*      */               }
/* 1381 */               else if ((de instanceof Tcm)) {
/* 1382 */                 fde.getTcm().add(convertTcm2XML((Tcm)de));
/*      */               }
/* 1384 */               else if ((de instanceof TCAElement))
/*      */               {
/* 1386 */                 TCAElement tcaEl = (TCAElement)de;
/*      */ 
/* 1388 */                 TCA tca = 
/* 1389 */                   new TCA();
/*      */ 
/* 1409 */                 tca.setPgenType(tcaEl.getPgenType());
/* 1410 */                 tca.setPgenCategory(tcaEl.getPgenCategory());
/*      */ 
/* 1412 */                 tca.setStormNumber(Integer.valueOf(tcaEl.getStormNumber()));
/* 1413 */                 tca.setStormName(tcaEl.getStormName());
/* 1414 */                 tca.setBasin(tcaEl.getBasin());
/* 1415 */                 tca.setIssueStatus(tcaEl.getIssueStatus());
/* 1416 */                 tca.setStormType(tcaEl.getStormType());
/* 1417 */                 tca.setAdvisoryNumber(tcaEl.getAdvisoryNumber());
/*      */ 
/* 1419 */                 tca.setTimeZone(tcaEl.getTimeZone());
/* 1420 */                 tca.setTextLocation(tcaEl.getTextLocation());
/*      */ 
/* 1422 */                 Calendar advTime = tcaEl.getAdvisoryTime();
/* 1423 */                 XMLGregorianCalendar xmlCal = null;
/*      */                 try {
/* 1425 */                   xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 1426 */                     advTime.get(1), 
/* 1427 */                     advTime.get(2) + 1, 
/* 1428 */                     advTime.get(5), 
/* 1429 */                     advTime.get(11), 
/* 1430 */                     advTime.get(12), 
/* 1431 */                     advTime.get(13), 
/* 1432 */                     advTime.get(14), 
/* 1433 */                     0);
/*      */                 }
/*      */                 catch (DatatypeConfigurationException e) {
/* 1436 */                   e.printStackTrace();
/*      */                 }
/* 1438 */                 tca.setAdvisoryTime(xmlCal);
/*      */ 
/* 1440 */                 tca.setAdvisories(tcaEl.getAdvisories());
/*      */ 
/* 1442 */                 fde.getTCA().add(tca);
/*      */               }
/* 1444 */               else if ((de instanceof gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano)) {
/* 1445 */                 fde.getVolcano().add(convertVolcano2XML((gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano)de)); } 
/*      */             }
/*      */           }
/*      */         } } else if ((adc instanceof gov.noaa.nws.ncep.ui.pgen.elements.DECollection))
/*      */       {
/* 1450 */         if (adc.getName().equalsIgnoreCase("Contours")) {
/* 1451 */           fde.getContours().add(convertContours2XML((gov.noaa.nws.ncep.ui.pgen.contours.Contours)adc));
/*      */         }
/* 1453 */         else if (adc.getName().equalsIgnoreCase("Outlook")) {
/* 1454 */           fde.getOutlook().add(convertOutlook2XML((gov.noaa.nws.ncep.ui.pgen.elements.Outlook)adc));
/*      */         }
/*      */         else {
/* 1457 */           fde.getDECollection().add(convertDECollection2XML((gov.noaa.nws.ncep.ui.pgen.elements.DECollection)adc));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1462 */     return fde;
/*      */   }
/*      */ 
/*      */   private static DECollection convertDECollection2XML(gov.noaa.nws.ncep.ui.pgen.elements.DECollection dec)
/*      */   {
/* 1472 */     DECollection fdec = 
/* 1473 */       new DECollection();
/*      */ 
/* 1475 */     String cName = dec.getCollectionName();
/* 1476 */     if (cName != null) {
/* 1477 */       fdec.setCollectionName(cName);
/*      */     }
/*      */ 
/* 1480 */     fdec.setPgenType(dec.getPgenType());
/* 1481 */     fdec.setPgenCategory(dec.getPgenCategory());
/*      */ 
/* 1483 */     List componentList = new ArrayList();
/* 1484 */     Iterator it = dec.getComponentIterator();
/*      */ 
/* 1486 */     while (it.hasNext()) {
/* 1487 */       componentList.add((AbstractDrawableComponent)it.next());
/*      */     }
/*      */ 
/* 1490 */     fdec.setDrawableElement(convertDEs(componentList));
/*      */ 
/* 1492 */     return fdec;
/*      */   }
/*      */ 
/*      */   private static Jet convertXML2Jet(DECollection dec)
/*      */   {
/* 1502 */     Jet jet = new Jet();
/* 1503 */     jet.setPgenCategory(dec.getPgenCategory());
/* 1504 */     jet.setPgenType(dec.getPgenType());
/*      */ 
/* 1506 */     jet.remove(jet.getJetLine());
/* 1507 */     DrawableElement elem = dec.getDrawableElement();
/*      */ 
/* 1509 */     if (elem.getLine() != null)
/* 1510 */       jet.add(convertJetLine((Line)elem.getLine().get(0), jet));
/*      */     else {
/* 1512 */       return null;
/*      */     }
/* 1514 */     if (elem.getVector() != null) {
/* 1515 */       for (Vector fVector : elem.getVector()) {
/* 1516 */         jet.add(convertJetHash(fVector, jet));
/*      */       }
/*      */     }
/*      */ 
/* 1520 */     if (elem.getDECollection() != null) {
/* 1521 */       for (DECollection fdec : elem.getDECollection()) {
/* 1522 */         if (fdec.getCollectionName().equalsIgnoreCase("WindInfo")) {
/* 1523 */           gov.noaa.nws.ncep.ui.pgen.elements.DECollection wind = new gov.noaa.nws.ncep.ui.pgen.elements.DECollection("WindInfo");
/*      */ 
/* 1525 */           DrawableElement de = fdec.getDrawableElement();
/* 1526 */           if (de.getVector() != null) {
/* 1527 */             wind.add(convertJetBarb((Vector)de.getVector().get(0), jet));
/*      */           }
/*      */ 
/* 1530 */           if (de.getText() != null)
/*      */           {
/* 1533 */             ((Text)de.getText().get(0)).getPoint().setLat(Double.valueOf(0.0D));
/* 1534 */             ((Text)de.getText().get(0)).getPoint().setLon(Double.valueOf(0.0D));
/* 1535 */             wind.add(convertJetFL((Text)de.getText().get(0), jet));
/*      */           }
/*      */ 
/* 1538 */           jet.add(wind);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1545 */     if (PgenSession.getInstance() != null) {
/* 1546 */       PgenSnapJet st = new PgenSnapJet((IMapDescriptor)PgenSession.getInstance().getPgenResource().getDescriptor(), 
/* 1547 */         PgenUtil.getActiveEditor(), null);
/* 1548 */       jet.setSnapTool(st);
/* 1549 */       st.snapJet(jet);
/*      */     }
/*      */ 
/* 1552 */     return jet;
/*      */   }
/*      */ 
/*      */   private static Jet.JetLine convertJetLine(Line jetLine, Jet jet)
/*      */   {
/* 1561 */     java.awt.Color[] clr = new java.awt.Color[jetLine.getColor().size()];
/* 1562 */     int nn = 0;
/* 1563 */     for (Color fColor : jetLine.getColor()) {
/* 1564 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 1565 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/*      */ 
/* 1568 */     ArrayList linePoints = new ArrayList();
/* 1569 */     nn = 0;
/* 1570 */     for (Point pt : jetLine.getPoint())
/* 1571 */       linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */     Jet tmp168_167 = jet; tmp168_167.getClass(); Jet.JetLine newLine = new Jet.JetLine(tmp168_167, null, clr, 
/* 1575 */       jetLine.getLineWidth().floatValue(), jetLine.getSizeScale().doubleValue(), jetLine.isClosed().booleanValue(), 
/* 1576 */       jetLine.isFilled().booleanValue(), linePoints, 
/* 1577 */       jetLine.getSmoothFactor().intValue(), 
/* 1578 */       FillPatternList.FillPattern.valueOf(jetLine.getFillPattern()), 
/* 1579 */       jetLine.getPgenCategory(), jetLine.getPgenType());
/*      */ 
/* 1581 */     newLine.setParent(jet);
/*      */ 
/* 1583 */     return newLine;
/*      */   }
/*      */ 
/*      */   private static Jet.JetHash convertJetHash(Vector fVector, Jet jet)
/*      */   {
/* 1593 */     java.awt.Color[] clr = new java.awt.Color[fVector.getColor().size()];
/* 1594 */     int nn = 0;
/* 1595 */     for (Color fColor : fVector.getColor()) {
/* 1596 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 1597 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/*      */ 
/* 1600 */     Point loc = fVector.getPoint();
/*      */ 
/* 1602 */     IVector.VectorType vtype = null;
/* 1603 */     String pgenType = fVector.getPgenType();
/*      */ 
/* 1605 */     if (pgenType.equalsIgnoreCase("Hash"))
/* 1606 */       vtype = IVector.VectorType.HASH_MARK;
/*      */     Jet tmp123_122 = jet; tmp123_122.getClass(); Jet.JetHash hash = new Jet.JetHash(tmp123_122, null, clr, 
/* 1609 */       fVector.getLineWidth().floatValue(), fVector.getSizeScale().doubleValue(), 
/* 1610 */       fVector.isClear(), new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 1611 */       vtype, fVector.getSpeed().doubleValue(), fVector.getDirection().doubleValue(), 
/* 1612 */       fVector.getArrowHeadSize().doubleValue(), fVector.isDirectionOnly().booleanValue(), 
/* 1613 */       fVector.getPgenCategory(), fVector.getPgenType());
/* 1614 */     hash.setParent(jet);
/*      */ 
/* 1616 */     return hash;
/*      */   }
/*      */ 
/*      */   private static Jet.JetBarb convertJetBarb(Vector fVector, Jet jet)
/*      */   {
/* 1626 */     java.awt.Color[] clr = new java.awt.Color[fVector.getColor().size()];
/* 1627 */     int nn = 0;
/* 1628 */     for (Color fColor : fVector.getColor()) {
/* 1629 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 1630 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/*      */ 
/* 1633 */     Point loc = fVector.getPoint();
/*      */ 
/* 1635 */     IVector.VectorType vtype = null;
/* 1636 */     String pgenType = fVector.getPgenType();
/*      */ 
/* 1638 */     if (pgenType.equalsIgnoreCase("Barb"))
/* 1639 */       vtype = IVector.VectorType.WIND_BARB;
/*      */     Jet tmp123_122 = jet; tmp123_122.getClass(); Jet.JetBarb barb = new Jet.JetBarb(tmp123_122, null, clr, 
/* 1642 */       fVector.getLineWidth().floatValue(), fVector.getSizeScale().doubleValue(), 
/* 1643 */       fVector.isClear(), new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 1644 */       vtype, fVector.getSpeed().doubleValue(), fVector.getDirection().doubleValue(), 
/* 1645 */       fVector.getArrowHeadSize().doubleValue(), fVector.isDirectionOnly().booleanValue(), 
/* 1646 */       fVector.getPgenCategory(), fVector.getPgenType());
/* 1647 */     barb.setParent(jet);
/* 1648 */     return barb;
/*      */   }
/*      */ 
/*      */   private static Jet.JetText convertJetFL(Text fText, Jet aJet)
/*      */   {
/* 1657 */     java.awt.Color[] clr = new java.awt.Color[fText.getColor().size()];
/* 1658 */     int nn = 0;
/* 1659 */     for (Color fColor : fText.getColor()) {
/* 1660 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 1661 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/*      */ 
/* 1664 */     Point loc = fText.getPoint();
/*      */ 
/* 1666 */     String[] st = new String[fText.getTextLine().size()];
/*      */ 
/* 1668 */     int nline = 0;
/* 1669 */     for (String str : fText.getTextLine())
/* 1670 */       st[(nline++)] = str;
/*      */     Jet tmp161_160 = aJet; tmp161_160.getClass(); Jet.JetText text = new Jet.JetText(tmp161_160, null, fText.getFontName(), 
/* 1674 */       fText.getFontSize().floatValue(), 
/* 1675 */       IText.TextJustification.valueOf(fText.getJustification()), 
/* 1676 */       new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 1677 */       fText.getRotation().doubleValue(), 
/* 1678 */       IText.TextRotation.valueOf(fText.getRotationRelativity()), 
/* 1679 */       st, 
/* 1680 */       IText.FontStyle.valueOf(fText.getStyle()), clr[0], 0, 0, 
/* 1681 */       fText.isMask().booleanValue(), 
/* 1682 */       IText.DisplayType.valueOf(fText.getDisplayType()), 
/* 1683 */       fText.getPgenCategory(), fText.getPgenType());
/*      */ 
/* 1685 */     text.setLatLonFlag(false);
/*      */ 
/* 1687 */     return text;
/*      */   }
/*      */ 
/*      */   private static Contours convertContours2XML(gov.noaa.nws.ncep.ui.pgen.contours.Contours cnt)
/*      */   {
/* 1697 */     Contours contours = 
/* 1698 */       new Contours();
/*      */ 
/* 1700 */     contours.setCollectionName("Contours");
/* 1701 */     contours.setPgenType("Contours");
/* 1702 */     contours.setPgenCategory("MET");
/*      */ 
/* 1704 */     contours.setParm(cnt.getParm());
/* 1705 */     contours.setLevel(cnt.getLevel());
/* 1706 */     contours.setCint(cnt.getCint());
/* 1707 */     if (cnt.getForecastHour() != null) {
/* 1708 */       contours.setForecastHour(cnt.getForecastHour());
/*      */     }
/*      */ 
/* 1711 */     Calendar cntTime = cnt.getTime1();
/* 1712 */     XMLGregorianCalendar xmlCal = null;
/*      */     try {
/* 1714 */       xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 1715 */         cntTime.get(1), 
/* 1716 */         cntTime.get(2) + 1, 
/* 1717 */         cntTime.get(5), 
/* 1718 */         cntTime.get(11), 
/* 1719 */         cntTime.get(12), 
/* 1720 */         cntTime.get(13), 
/* 1721 */         cntTime.get(14), 
/* 1722 */         0);
/*      */     } catch (DatatypeConfigurationException e) {
/* 1724 */       e.printStackTrace();
/*      */     }
/*      */ 
/* 1727 */     contours.setTime1(xmlCal);
/*      */ 
/* 1729 */     Calendar cntTime2 = cnt.getTime2();
/* 1730 */     XMLGregorianCalendar xmlCal2 = null;
/*      */     try {
/* 1732 */       xmlCal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 1733 */         cntTime2.get(1), 
/* 1734 */         cntTime2.get(2) + 1, 
/* 1735 */         cntTime2.get(5), 
/* 1736 */         cntTime2.get(11), 
/* 1737 */         cntTime2.get(12), 
/* 1738 */         cntTime2.get(13), 
/* 1739 */         cntTime2.get(14), 
/* 1740 */         0);
/*      */     } catch (DatatypeConfigurationException e) {
/* 1742 */       e.printStackTrace();
/*      */     }
/*      */ 
/* 1745 */     contours.setTime2(xmlCal2);
/*      */ 
/* 1747 */     Iterator it = cnt.getComponentIterator();
/* 1748 */     int ii = 0;
/* 1749 */     while (it.hasNext())
/*      */     {
/* 1751 */       AbstractDrawableComponent next = (AbstractDrawableComponent)it.next();
/*      */ 
/* 1753 */       if ((next instanceof gov.noaa.nws.ncep.ui.pgen.elements.DECollection)) {
/* 1754 */         contours.getDECollection().add(
/* 1755 */           convertDECollection2XML((gov.noaa.nws.ncep.ui.pgen.elements.DECollection)next));
/*      */       }
/*      */ 
/* 1758 */       ii++;
/*      */     }
/*      */ 
/* 1761 */     return contours;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.contours.Contours convertXML2Contours(Contours cnt)
/*      */   {
/* 1772 */     gov.noaa.nws.ncep.ui.pgen.contours.Contours contours = new gov.noaa.nws.ncep.ui.pgen.contours.Contours("Contours");
/*      */ 
/* 1774 */     contours.setPgenType(cnt.getPgenType());
/* 1775 */     contours.setPgenCategory(cnt.getPgenCategory());
/*      */ 
/* 1777 */     contours.setParm(cnt.getParm());
/* 1778 */     contours.setLevel(cnt.getLevel());
/* 1779 */     contours.setCint(cnt.getCint());
/*      */ 
/* 1781 */     if (cnt.getForecastHour() != null) {
/* 1782 */       contours.setForecastHour(cnt.getForecastHour());
/*      */     }
/*      */ 
/* 1785 */     Calendar cntTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 1786 */     XMLGregorianCalendar xmlCal = cnt.getTime1();
/* 1787 */     if (xmlCal != null)
/* 1788 */       cntTime.set(xmlCal.getYear(), xmlCal.getMonth() - 1, xmlCal.getDay(), 
/* 1789 */         xmlCal.getHour(), xmlCal.getMinute(), xmlCal.getSecond());
/* 1790 */     contours.setTime1(cntTime);
/*      */ 
/* 1792 */     Calendar cntTime2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 1793 */     XMLGregorianCalendar xmlCal2 = cnt.getTime2();
/* 1794 */     if (xmlCal2 != null)
/* 1795 */       cntTime2.set(xmlCal2.getYear(), xmlCal2.getMonth() - 1, xmlCal2.getDay(), 
/* 1796 */         xmlCal2.getHour(), xmlCal2.getMinute(), xmlCal2.getSecond());
/* 1797 */     contours.setTime2(cntTime2);
/*      */ 
/* 1799 */     for (DECollection fdec : cnt.getDECollection())
/*      */     {
/*      */       int numOfLabels;
/* 1801 */       if (fdec.getCollectionName().equals("ContourLine")) {
/* 1802 */         ContourLine contourLine = new ContourLine();
/*      */ 
/* 1804 */         List delist = convert(fdec.getDrawableElement());
/* 1805 */         String[] labelString = null;
/* 1806 */         numOfLabels = 0;
/*      */ 
/* 1808 */         for (AbstractDrawableComponent de : delist)
/*      */         {
/* 1810 */           de.setParent(contourLine);
/* 1811 */           contourLine.add(de);
/*      */ 
/* 1813 */           if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text)) {
/* 1814 */             numOfLabels++;
/*      */ 
/* 1816 */             if (labelString == null) {
/* 1817 */               labelString = ((gov.noaa.nws.ncep.ui.pgen.elements.Text)de).getText();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1824 */         contourLine.setNumOfLabels(numOfLabels);
/* 1825 */         contourLine.setLabelString(labelString);
/*      */ 
/* 1827 */         contourLine.setParent(contours);
/*      */ 
/* 1829 */         contours.add(contourLine);
/*      */       }
/* 1831 */       else if (fdec.getCollectionName().equals("ContourMinmax"))
/*      */       {
/* 1833 */         ContourMinmax contourMinmax = new ContourMinmax();
/*      */ 
/* 1835 */         List delist = convert(fdec.getDrawableElement());
/* 1836 */         for (AbstractDrawableComponent de : delist)
/*      */         {
/* 1838 */           de.setParent(contourMinmax);
/* 1839 */           contourMinmax.add(de);
/*      */         }
/*      */ 
/* 1843 */         contourMinmax.setParent(contours);
/*      */ 
/* 1845 */         contours.add(contourMinmax);
/*      */       }
/* 1847 */       else if (fdec.getCollectionName().equals("ContourCircle"))
/*      */       {
/* 1849 */         ContourCircle contourCircle = new ContourCircle();
/*      */ 
/* 1851 */         List delist = convert(fdec.getDrawableElement());
/* 1852 */         for (AbstractDrawableComponent de : delist)
/*      */         {
/* 1854 */           de.setParent(contourCircle);
/* 1855 */           contourCircle.add(de);
/*      */         }
/*      */ 
/* 1859 */         contourCircle.setParent(contours);
/*      */ 
/* 1861 */         contours.add(contourCircle);
/*      */       }
/*      */     }
/*      */ 
/* 1865 */     return contours;
/*      */   }
/*      */ 
/*      */   private static Spenes convertSpenes2XML(gov.noaa.nws.ncep.ui.pgen.elements.Spenes spenes)
/*      */   {
/* 1875 */     Spenes fspenes = 
/* 1876 */       new Spenes();
/*      */ 
/* 1878 */     fspenes.setPgenCategory(spenes.getPgenCategory());
/* 1879 */     fspenes.setPgenType(spenes.getPgenType());
/* 1880 */     fspenes.setLineWidth(Float.valueOf(spenes.getLineWidth()));
/* 1881 */     fspenes.setSmoothLevel(Integer.valueOf(spenes.getSmoothFactor()));
/* 1882 */     fspenes.setInitDateTime(spenes.getInitDateTime());
/* 1883 */     fspenes.setStateZ000(spenes.getStateZ000());
/* 1884 */     fspenes.setLatestDataUsed(spenes.getLatestDataUsed());
/* 1885 */     fspenes.setObsHr(spenes.getObsHr());
/* 1886 */     fspenes.setForecasters(spenes.getForecasters());
/* 1887 */     fspenes.setLocation(spenes.getLocation());
/* 1888 */     fspenes.setAttnWFOs(spenes.getAttnWFOs());
/* 1889 */     fspenes.setAttnRFCs(spenes.getAttnRFCs());
/* 1890 */     fspenes.setEvent(spenes.getEvent());
/* 1891 */     fspenes.setSatAnalysisTrends(spenes.getSatAnalysisTrend());
/* 1892 */     fspenes.setShortTermBegin(spenes.getShortTermBegin());
/* 1893 */     fspenes.setShortTermEnd(spenes.getShortTermEnd());
/* 1894 */     fspenes.setOutLookLevel(spenes.getOutlookLevel());
/* 1895 */     fspenes.setAddlInfo(spenes.getAddlInfo());
/* 1896 */     fspenes.setLatlon(spenes.getLatLon());
/*      */ 
/* 1899 */     for (Coordinate crd : spenes.getLinePoints())
/*      */     {
/* 1901 */       fpt = new Point();
/* 1902 */       fpt.setLat(Double.valueOf(crd.y));
/* 1903 */       fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/* 1905 */       fspenes.getPoint().add(fpt);
/*      */     }
/* 1907 */     Point fpt = new Point();
/* 1908 */     Coordinate crd = new Coordinate();
/* 1909 */     crd = spenes.getLinePoints()[0];
/* 1910 */     fpt.setLat(Double.valueOf(crd.y));
/* 1911 */     fpt.setLon(Double.valueOf(crd.x));
/* 1912 */     fspenes.getPoint().add(fpt);
/*      */     java.awt.Color[] arrayOfColor;
/* 1916 */     Point fpt = (arrayOfColor = spenes.getColors()).length; for (Point localPoint1 = 0; localPoint1 < fpt; localPoint1++) { java.awt.Color clr = arrayOfColor[localPoint1];
/*      */ 
/* 1918 */       Color fclr = 
/* 1919 */         new Color();
/*      */ 
/* 1921 */       fclr.setRed(clr.getRed());
/* 1922 */       fclr.setGreen(clr.getGreen());
/* 1923 */       fclr.setBlue(clr.getBlue());
/* 1924 */       fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 1925 */       fspenes.getColor().add(fclr);
/*      */     }
/*      */ 
/* 1928 */     return fspenes;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.elements.Spenes convertXML2Spenes(Spenes fspenes)
/*      */   {
/* 1937 */     gov.noaa.nws.ncep.ui.pgen.elements.Spenes sp = new gov.noaa.nws.ncep.ui.pgen.elements.Spenes();
/*      */ 
/* 1939 */     sp.setPgenCategory(fspenes.getPgenCategory());
/* 1940 */     sp.setPgenType(fspenes.getPgenType());
/* 1941 */     sp.setLineWidth(fspenes.getLineWidth().floatValue());
/* 1942 */     sp.setSmoothFactor(fspenes.getSmoothLevel().intValue());
/* 1943 */     sp.setInitDateTime(fspenes.getInitDateTime());
/* 1944 */     sp.setLatestData(fspenes.getLatestDataUsed());
/* 1945 */     sp.setObsHr(fspenes.getObsHr());
/* 1946 */     sp.setForecasters(fspenes.getForecasters());
/* 1947 */     sp.setStateZ000(fspenes.getStateZ0000());
/* 1948 */     sp.setLocation(fspenes.getLocation());
/* 1949 */     sp.setAttnWFOs(fspenes.getAttnWFOs());
/* 1950 */     sp.setAttnRFCs(fspenes.getAttnRFCs());
/* 1951 */     sp.setEvent(fspenes.getEvent());
/* 1952 */     sp.setSatAnalysisTrend(fspenes.getSatAnalysisTrends());
/* 1953 */     sp.setShortTermBegin(fspenes.getShortTermBegin());
/* 1954 */     sp.setShortTermEnd(fspenes.getShortTermEnd());
/* 1955 */     sp.setOutlookLevel(fspenes.getOutLookLevel());
/* 1956 */     sp.setAddlInfo(fspenes.getAddlInfo());
/* 1957 */     sp.setLatLon(fspenes.getLatlon());
/*      */ 
/* 1959 */     java.awt.Color[] clr = new java.awt.Color[fspenes.getColor().size()];
/* 1960 */     int nn = 0;
/* 1961 */     for (Color fColor : fspenes.getColor()) {
/* 1962 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 1963 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/* 1965 */     sp.setColors(clr);
/*      */ 
/* 1968 */     ArrayList linePoints = new ArrayList();
/* 1969 */     nn = 0;
/* 1970 */     for (Point pt : fspenes.getPoint()) {
/* 1971 */       linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */     }
/* 1973 */     sp.setPointsOnly(linePoints);
/*      */ 
/* 1977 */     return sp;
/*      */   }
/*      */ 
/*      */   private static WatchBox convertWatchBox2XML(gov.noaa.nws.ncep.ui.pgen.elements.WatchBox wb)
/*      */   {
/* 1988 */     WatchBox fwb = 
/* 1989 */       new WatchBox();
/*      */ 
/* 1991 */     fwb.setPgenCategory(wb.getPgenCategory());
/* 1992 */     fwb.setPgenType(wb.getPgenType());
/* 1993 */     fwb.setBoxShape(wb.getBoxShape());
/* 1994 */     fwb.setFillFlag(Boolean.valueOf(wb.getFillFlag()));
/* 1995 */     fwb.setSymbolSize(Double.valueOf(wb.getWatchSymbolSize()));
/* 1996 */     fwb.setSymbolWidth(Float.valueOf(wb.getWatchSymbolWidth()));
/* 1997 */     fwb.setSymbolType(wb.getWatchSymbolType());
/*      */ 
/* 2000 */     fwb.setIssueStatus(wb.getIssueStatus());
/*      */ 
/* 2002 */     Calendar issueTime = wb.getIssueTime();
/* 2003 */     if (issueTime != null) {
/* 2004 */       XMLGregorianCalendar xmlIssueCal = null;
/*      */       try {
/* 2006 */         xmlIssueCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2007 */           issueTime.get(1), 
/* 2008 */           issueTime.get(2) + 1, 
/* 2009 */           issueTime.get(5), 
/* 2010 */           issueTime.get(11), 
/* 2011 */           issueTime.get(12), 
/* 2012 */           issueTime.get(13), 
/* 2013 */           issueTime.get(14), 
/* 2014 */           0);
/*      */       }
/*      */       catch (DatatypeConfigurationException e) {
/* 2017 */         e.printStackTrace();
/*      */       }
/* 2019 */       fwb.setIssueTime(xmlIssueCal);
/*      */     }
/*      */ 
/* 2022 */     Calendar expTime = wb.getExpTime();
/* 2023 */     if (expTime != null) {
/* 2024 */       XMLGregorianCalendar xmlExpCal = null;
/*      */       try {
/* 2026 */         xmlExpCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2027 */           expTime.get(1), 
/* 2028 */           expTime.get(2) + 1, 
/* 2029 */           expTime.get(5), 
/* 2030 */           expTime.get(11), 
/* 2031 */           expTime.get(12), 
/* 2032 */           expTime.get(13), 
/* 2033 */           expTime.get(14), 
/* 2034 */           0);
/*      */       }
/*      */       catch (DatatypeConfigurationException e) {
/* 2037 */         e.printStackTrace();
/*      */       }
/* 2039 */       fwb.setExpTime(xmlExpCal);
/*      */     }
/*      */ 
/* 2042 */     fwb.setSeverity(wb.getSeverity());
/* 2043 */     fwb.setTimeZone(wb.getTimeZone());
/* 2044 */     fwb.setHailSize(Float.valueOf(wb.getHailSize()));
/* 2045 */     fwb.setGust(Integer.valueOf(wb.getGust()));
/* 2046 */     fwb.setTop(Integer.valueOf(wb.getTop()));
/* 2047 */     fwb.setMoveDir(Integer.valueOf(wb.getMoveDir()));
/* 2048 */     fwb.setMoveSpeed(Integer.valueOf(wb.getMoveSpeed()));
/* 2049 */     fwb.setAdjAreas(wb.getAdjAreas());
/* 2050 */     fwb.setReplWatch(Integer.valueOf(wb.getReplWatch()));
/* 2051 */     fwb.setContWatch(wb.getContWatch());
/* 2052 */     fwb.setIssueFlag(Integer.valueOf(wb.getIssueFlag()));
/* 2053 */     fwb.setWatchType(wb.getWatchType());
/* 2054 */     fwb.setForecaster(wb.getForecaster());
/* 2055 */     fwb.setWatchNumber(Integer.valueOf(wb.getWatchNumber()));
/* 2056 */     fwb.setEndPointAnc(wb.getEndPointAnc());
/* 2057 */     fwb.setEndPointVor(wb.getEndPointVor());
/* 2058 */     fwb.setHalfWidthNm(Integer.valueOf(wb.getHalfWidthNm()));
/* 2059 */     fwb.setHalfWidthSm(Integer.valueOf(wb.getHalfWidthSm()));
/* 2060 */     fwb.setWatchAreaNm(Integer.valueOf(wb.getWathcAreaNm()));
/*      */     java.awt.Color[] arrayOfColor;
/* 2063 */     DatatypeConfigurationException localDatatypeConfigurationException1 = (arrayOfColor = wb.getColors()).length;
/*      */     Color fclr;
/* 2063 */     for (e = 0; e < localDatatypeConfigurationException1; e++) { java.awt.Color clr = arrayOfColor[e];
/*      */ 
/* 2065 */       fclr = 
/* 2066 */         new Color();
/*      */ 
/* 2068 */       fclr.setRed(clr.getRed());
/* 2069 */       fclr.setGreen(clr.getGreen());
/* 2070 */       fclr.setBlue(clr.getBlue());
/* 2071 */       fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 2072 */       fwb.getColor().add(fclr);
/*      */     }
/*      */ 
/* 2076 */     java.awt.Color fColor = wb.getFillColor();
/*      */     ColorType ct;
/* 2077 */     if (fColor != null) {
/* 2078 */       Color fill = 
/* 2079 */         new Color();
/* 2080 */       fill.setRed(fColor.getRed());
/* 2081 */       fill.setGreen(fColor.getGreen());
/* 2082 */       fill.setBlue(fColor.getBlue());
/* 2083 */       fill.setAlpha(Integer.valueOf(fColor.getAlpha()));
/* 2084 */       ct = new ColorType();
/* 2085 */       ct.setColor(fill);
/* 2086 */       fwb.setFillColor(ct);
/*      */     }
/*      */ 
/* 2090 */     ColorType localColorType2 = (fclr = wb.getLinePoints()).length; for (ColorType localColorType1 = 0; localColorType1 < localColorType2; localColorType1++) { Coordinate crd = fclr[localColorType1];
/*      */ 
/* 2092 */       Point fpt = new Point();
/* 2093 */       fpt.setLat(Double.valueOf(crd.y));
/* 2094 */       fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/* 2096 */       fwb.getPoint().add(fpt);
/*      */     }
/*      */ 
/* 2100 */     ColorType localColorType3 = (fclr = wb.getAnchors()).length; for (Object localObject1 = 0; localObject1 < localColorType3; localObject1++) { Station stn = fclr[localObject1];
/* 2101 */       fwb.getAnchorPoints().add(stn.getStid() + " " + stn.getState() + " " + stn.getStnname());
/*      */     }
/*      */ 
/* 2105 */     for (localObject1 = wb.getCountyList().iterator(); ((Iterator)localObject1).hasNext(); ) { SPCCounty cnty = (SPCCounty)((Iterator)localObject1).next();
/*      */ 
/* 2107 */       cntyName = "";
/* 2108 */       if (cnty.getName() != null) {
/* 2109 */         cntyName = cnty.getName().replaceAll("City of ", "").replaceAll(" City", "");
/*      */       }
/*      */ 
/* 2112 */       fwb.getCounties().add(String.format("%1$-7s%2$-5s%3$-12s%4$6.2f%5$8.2f%6$7s%7$5s %8$s", new Object[] { 
/* 2113 */         cnty.getUgcId(), cnty.getState(), cntyName, 
/* 2114 */         Double.valueOf(cnty.getCentriod().y), Double.valueOf(cnty.getCentriod().x), cnty.getFips(), 
/* 2115 */         cnty.getWfo(), cnty.getZoneName().toUpperCase() }));
/*      */     }
/*      */ 
/* 2119 */     for (localObject1 = wb.getStates().iterator(); ((Iterator)localObject1).hasNext(); ) { String str = (String)((Iterator)localObject1).next();
/* 2120 */       fwb.getStates().add(str);
/*      */     }
/*      */ 
/* 2124 */     String wfoStr = "";
/* 2125 */     for (Object cntyName = wb.getWFOs().iterator(); ((Iterator)cntyName).hasNext(); ) { String str = (String)((Iterator)cntyName).next();
/* 2126 */       wfoStr = wfoStr + str + "...";
/*      */     }
/* 2128 */     fwb.setWfos(wfoStr);
/*      */ 
/* 2131 */     if (wb.getStatusHistory() != null) {
/* 2132 */       for (cntyName = wb.getStatusHistory().iterator(); ((Iterator)cntyName).hasNext(); ) { WatchBox.WatchStatus ws = (WatchBox.WatchStatus)((Iterator)cntyName).next();
/*      */ 
/* 2134 */         WatchBox.Status fws = new WatchBox.Status();
/*      */ 
/* 2136 */         fws.setFromLine(ws.getFromLine());
/* 2137 */         fws.setStatusForecaster(ws.getStatusForecaster());
/* 2138 */         fws.setMesoDiscussionNumber(Integer.valueOf(ws.getDiscussion()));
/*      */ 
/* 2140 */         Calendar statusValidTime = ws.getStatusValidTime();
/* 2141 */         if (statusValidTime != null) {
/* 2142 */           XMLGregorianCalendar xmlCal = null;
/*      */           try {
/* 2144 */             xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2145 */               statusValidTime.get(1), 
/* 2146 */               statusValidTime.get(2) + 1, 
/* 2147 */               statusValidTime.get(5), 
/* 2148 */               statusValidTime.get(11), 
/* 2149 */               statusValidTime.get(12), 
/* 2150 */               statusValidTime.get(13), 
/* 2151 */               statusValidTime.get(14), 
/* 2152 */               0);
/*      */           }
/*      */           catch (DatatypeConfigurationException e)
/*      */           {
/* 2156 */             e.printStackTrace();
/*      */           }
/* 2158 */           fws.setStatusValidTime(xmlCal);
/*      */         }
/*      */ 
/* 2161 */         Calendar statusExpTime = ws.getStatusValidTime();
/* 2162 */         if (statusExpTime != null) {
/* 2163 */           XMLGregorianCalendar xmlCal = null;
/*      */           try {
/* 2165 */             xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2166 */               statusExpTime.get(1), 
/* 2167 */               statusExpTime.get(2) + 1, 
/* 2168 */               statusExpTime.get(5), 
/* 2169 */               statusExpTime.get(11), 
/* 2170 */               statusExpTime.get(12), 
/* 2171 */               statusExpTime.get(13), 
/* 2172 */               statusExpTime.get(14), 
/* 2173 */               0);
/*      */           }
/*      */           catch (DatatypeConfigurationException e)
/*      */           {
/* 2177 */             e.printStackTrace();
/*      */           }
/* 2179 */           fws.setStatusExpTime(xmlCal);
/*      */         }
/*      */ 
/* 2182 */         fwb.getStatus().add(fws);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2229 */     return fwb;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.elements.WatchBox convertXML2WatchBox(WatchBox fwb)
/*      */   {
/* 2238 */     gov.noaa.nws.ncep.ui.pgen.elements.WatchBox wb = new gov.noaa.nws.ncep.ui.pgen.elements.WatchBox();
/*      */ 
/* 2240 */     wb.setPgenCategory(fwb.getPgenCategory());
/* 2241 */     wb.setPgenType(fwb.getPgenType());
/* 2242 */     wb.setWatchBoxShape(fwb.getBoxShape());
/* 2243 */     wb.setFillFlag(fwb.isFillFlag().booleanValue());
/* 2244 */     wb.setWatchSymbolSize(fwb.getSymbolSize().doubleValue());
/* 2245 */     wb.setWatchSymbolWidth(fwb.getSymbolWidth().floatValue());
/* 2246 */     wb.setWatchSymbolType(fwb.getSymbolType());
/* 2247 */     wb.setWatchType(fwb.getWatchType());
/*      */ 
/* 2250 */     wb.setIssueStatus(fwb.getIssueStatus());
/*      */ 
/* 2252 */     Calendar issueTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2253 */     XMLGregorianCalendar xmlIssueCal = fwb.getIssueTime();
/* 2254 */     if (xmlIssueCal != null) {
/* 2255 */       issueTime.set(xmlIssueCal.getYear(), xmlIssueCal.getMonth() - 1, xmlIssueCal.getDay(), 
/* 2256 */         xmlIssueCal.getHour(), xmlIssueCal.getMinute(), xmlIssueCal.getSecond());
/* 2257 */       wb.setIssueTime(issueTime);
/*      */     }
/*      */ 
/* 2260 */     Calendar expTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2261 */     XMLGregorianCalendar xmlExpCal = fwb.getExpTime();
/* 2262 */     if (xmlExpCal != null) {
/* 2263 */       expTime.set(xmlExpCal.getYear(), xmlExpCal.getMonth() - 1, xmlExpCal.getDay(), 
/* 2264 */         xmlExpCal.getHour(), xmlExpCal.getMinute(), xmlExpCal.getSecond());
/* 2265 */       wb.setExpTime(expTime);
/*      */     }
/*      */ 
/* 2268 */     wb.setSeverity(fwb.getSeverity());
/* 2269 */     wb.setTimeZone(fwb.getTimeZone());
/*      */ 
/* 2271 */     if (fwb.getHailSize() != null) {
/* 2272 */       wb.setHailSize(fwb.getHailSize().floatValue());
/*      */     }
/* 2274 */     if (fwb.getGust() != null) {
/* 2275 */       wb.setGust(fwb.getGust().intValue());
/*      */     }
/* 2277 */     if (fwb.getTop() != null) {
/* 2278 */       wb.setTop(fwb.getTop().intValue());
/*      */     }
/* 2280 */     if (fwb.getMoveDir() != null) {
/* 2281 */       wb.setMoveDir(fwb.getMoveDir().intValue());
/*      */     }
/* 2283 */     if (fwb.getMoveSpeed() != null) {
/* 2284 */       wb.setMoveSpeed(fwb.getMoveSpeed().intValue());
/*      */     }
/* 2286 */     wb.setAdjAreas(fwb.getAdjAreas());
/*      */ 
/* 2288 */     if (fwb.getReplWatch() != null) {
/* 2289 */       wb.setReplWatch(fwb.getReplWatch().intValue());
/*      */     }
/* 2291 */     if (fwb.getContWatch() != null) {
/* 2292 */       wb.setContWatch(fwb.getContWatch());
/*      */     }
/* 2294 */     if (fwb.getIssueFlag() != null) {
/* 2295 */       wb.setIssueFlag(fwb.getIssueFlag().intValue());
/*      */     }
/* 2297 */     wb.setForecaster(fwb.getForecaster());
/*      */ 
/* 2299 */     if (fwb.getWatchNumber() != null) {
/* 2300 */       wb.setWatchNumber(fwb.getWatchNumber().intValue());
/*      */     }
/* 2302 */     wb.setEndPointAnc(fwb.getEndPointAnc());
/* 2303 */     wb.setEndPointVor(fwb.getEndPointVor());
/*      */ 
/* 2305 */     if (fwb.getWatchAreaNm() != null) {
/* 2306 */       wb.setWathcAreaNm(fwb.getWatchAreaNm().intValue());
/*      */     }
/* 2308 */     if (fwb.getHalfWidthNm() != null) {
/* 2309 */       wb.setHalfWidthNm(fwb.getHalfWidthNm().intValue());
/*      */     }
/* 2311 */     if (fwb.getHalfWidthSm() != null) {
/* 2312 */       wb.setHalfWidthSm(fwb.getHalfWidthSm().intValue());
/*      */     }
/*      */ 
/* 2316 */     java.awt.Color[] clr = new java.awt.Color[fwb.getColor().size()];
/* 2317 */     int nn = 0;
/* 2318 */     for (Color fColor : fwb.getColor()) {
/* 2319 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2320 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/* 2322 */     wb.setColors(clr);
/*      */ 
/* 2325 */     ArrayList linePoints = new ArrayList();
/* 2326 */     nn = 0;
/* 2327 */     for (Point pt : fwb.getPoint()) {
/* 2328 */       linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */     }
/* 2330 */     wb.setPointsOnly(linePoints);
/*      */ 
/* 2333 */     if (fwb.getFillColor() != null) {
/* 2334 */       Color fColor = fwb.getFillColor().getColor();
/* 2335 */       if (fColor != null) {
/* 2336 */         java.awt.Color fill = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2337 */           fColor.getBlue(), fColor.getAlpha().intValue());
/* 2338 */         wb.setFillColor(fill);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2343 */     Station[] anchors = new Station[fwb.getAnchorPoints().size()];
/* 2344 */     nn = 0;
/* 2345 */     for (String str : fwb.getAnchorPoints()) {
/* 2346 */       anchors[(nn++)] = PgenStaticDataProvider.getProvider().getAnchorTbl().getStation(IStationField.StationField.STID, str.substring(0, 3));
/*      */     }
/* 2348 */     wb.setAnchors(anchors[0], anchors[1]);
/*      */ 
/* 2351 */     if (!fwb.getCounties().isEmpty()) {
/* 2352 */       for (SPCCounty cnty : PgenStaticDataProvider.getProvider().getSPCCounties()) {
/* 2353 */         if (cnty.getUgcId() != null) {
/* 2354 */           Iterator it = fwb.getCounties().iterator();
/* 2355 */           while (it.hasNext()) {
/* 2356 */             String str = ((String)it.next()).trim();
/* 2357 */             if (str.split(" ").length == 1) {
/* 2358 */               if ((!cnty.getFips().isEmpty()) && (str.equals(cnty.getFips()))) {
/* 2359 */                 wb.addCounty(cnty);
/* 2360 */                 it.remove();
/* 2361 */                 break;
/*      */               }
/*      */ 
/*      */             }
/* 2365 */             else if ((!cnty.getUgcId().isEmpty()) && (str.contains(cnty.getUgcId()))) {
/* 2366 */               wb.addCounty(cnty);
/* 2367 */               it.remove();
/* 2368 */               break;
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2378 */     for (WatchBox.Status fws : fwb.getStatus())
/*      */     {
/* 2380 */       Calendar statusValidTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2381 */       XMLGregorianCalendar xmlStatusValidCal = fws.getStatusValidTime();
/* 2382 */       if (xmlStatusValidCal != null) {
/* 2383 */         statusValidTime.set(xmlStatusValidCal.getYear(), xmlStatusValidCal.getMonth() - 1, xmlStatusValidCal.getDay(), 
/* 2384 */           xmlStatusValidCal.getHour(), xmlStatusValidCal.getMinute(), xmlStatusValidCal.getSecond());
/*      */       }
/*      */ 
/* 2387 */       Calendar statusExpTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2388 */       XMLGregorianCalendar xmlStatusExpCal = fws.getStatusExpTime();
/* 2389 */       if (xmlStatusExpCal != null) {
/* 2390 */         statusExpTime.set(xmlStatusExpCal.getYear(), xmlStatusExpCal.getMonth() - 1, xmlStatusExpCal.getDay(), 
/* 2391 */           xmlStatusExpCal.getHour(), xmlStatusExpCal.getMinute(), xmlStatusExpCal.getSecond());
/*      */       }
/*      */ 
/* 2394 */       wb.addStatus(fws.getFromLine(), fws.getMesoDiscussionNumber().intValue(), 
/* 2395 */         statusValidTime, statusExpTime, fws.getStatusForecaster());
/*      */     }
/*      */ 
/* 2399 */     return wb;
/*      */   }
/*      */ 
/*      */   private static Outlook convertOutlook2XML(gov.noaa.nws.ncep.ui.pgen.elements.Outlook otlk)
/*      */   {
/* 2410 */     Outlook fotlk = 
/* 2411 */       new Outlook();
/*      */ 
/* 2413 */     fotlk.setDays(otlk.getDays());
/* 2414 */     fotlk.setForecaster(otlk.getForecaster());
/* 2415 */     fotlk.setName(otlk.getName());
/* 2416 */     fotlk.setPgenType(otlk.getPgenType());
/* 2417 */     fotlk.setPgenCategory(otlk.getPgenCategory());
/* 2418 */     fotlk.setOutlookType(otlk.getOutlookType());
/* 2419 */     fotlk.setLineInfo(otlk.getLineInfo());
/*      */ 
/* 2421 */     XMLGregorianCalendar xmlCal = null;
/*      */ 
/* 2423 */     if (otlk.getParm() != null) {
/* 2424 */       fotlk.setParm(otlk.getParm());
/* 2425 */       fotlk.setLevel(otlk.getLevel());
/* 2426 */       fotlk.setCint(otlk.getCint());
/*      */ 
/* 2428 */       Calendar cntTime = otlk.getTime1();
/*      */       try
/*      */       {
/* 2431 */         xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2432 */           cntTime.get(1), 
/* 2433 */           cntTime.get(2) + 1, 
/* 2434 */           cntTime.get(5), 
/* 2435 */           cntTime.get(11), 
/* 2436 */           cntTime.get(12), 
/* 2437 */           cntTime.get(13), 
/* 2438 */           cntTime.get(14), 
/* 2439 */           0);
/*      */       } catch (DatatypeConfigurationException e) {
/* 2441 */         e.printStackTrace();
/*      */       }
/*      */ 
/* 2444 */       fotlk.setTime(xmlCal);
/*      */     }
/*      */ 
/* 2448 */     Calendar issueTime = otlk.getIssueTime();
/* 2449 */     if (issueTime != null) {
/* 2450 */       xmlCal = null;
/*      */       try {
/* 2452 */         xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2453 */           issueTime.get(1), 
/* 2454 */           issueTime.get(2) + 1, 
/* 2455 */           issueTime.get(5), 
/* 2456 */           issueTime.get(11), 
/* 2457 */           issueTime.get(12), 
/* 2458 */           issueTime.get(13), 
/* 2459 */           0, 
/* 2460 */           0);
/*      */       }
/*      */       catch (DatatypeConfigurationException e)
/*      */       {
/* 2464 */         e.printStackTrace();
/*      */       }
/* 2466 */       fotlk.setIssueTime(xmlCal);
/*      */     }
/*      */ 
/* 2469 */     Calendar expTime = otlk.getExpirationTime();
/* 2470 */     if (expTime != null) {
/* 2471 */       xmlCal = null;
/*      */       try {
/* 2473 */         xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 2474 */           expTime.get(1), 
/* 2475 */           expTime.get(2) + 1, 
/* 2476 */           expTime.get(5), 
/* 2477 */           expTime.get(11), 
/* 2478 */           expTime.get(12), 
/* 2479 */           expTime.get(13), 
/* 2480 */           0, 
/* 2481 */           0);
/*      */       }
/*      */       catch (DatatypeConfigurationException e)
/*      */       {
/* 2485 */         e.printStackTrace();
/*      */       }
/* 2487 */       fotlk.setExpTime(xmlCal);
/*      */     }
/*      */ 
/* 2490 */     Iterator it = otlk.getComponentIterator();
/* 2491 */     while (it.hasNext())
/*      */     {
/* 2493 */       AbstractDrawableComponent next = (AbstractDrawableComponent)it.next();
/*      */ 
/* 2495 */       if ((next instanceof gov.noaa.nws.ncep.ui.pgen.elements.DECollection)) {
/* 2496 */         fotlk.getDECollection().add(
/* 2497 */           convertDECollection2XML((gov.noaa.nws.ncep.ui.pgen.elements.DECollection)next));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2502 */     return fotlk;
/*      */   }
/*      */ 
/*      */   private static gov.noaa.nws.ncep.ui.pgen.elements.Outlook convertXML2Outlook(Outlook fotlk)
/*      */   {
/* 2513 */     DrawableElementFactory def = new DrawableElementFactory();
/* 2514 */     gov.noaa.nws.ncep.ui.pgen.elements.Outlook otlk = def.createOutlook(null, null, null, null);
/*      */ 
/* 2516 */     otlk.setPgenType(fotlk.getPgenType());
/* 2517 */     otlk.setPgenCategory(fotlk.getPgenCategory());
/* 2518 */     otlk.setDays(fotlk.getDays());
/* 2519 */     otlk.setForecaster(fotlk.getForecaster());
/* 2520 */     otlk.setOutlookType(fotlk.getOutlookType());
/* 2521 */     otlk.setLineInfo(fotlk.getLineInfo());
/*      */ 
/* 2523 */     if (fotlk.getParm() != null) {
/* 2524 */       otlk.setParm(fotlk.getParm());
/* 2525 */       otlk.setLevel(fotlk.getLevel());
/* 2526 */       otlk.setCint(fotlk.getCint());
/*      */ 
/* 2528 */       Calendar cntTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2529 */       XMLGregorianCalendar xmlCal = fotlk.getTime();
/* 2530 */       cntTime.set(xmlCal.getYear(), xmlCal.getMonth() - 1, xmlCal.getDay(), 
/* 2531 */         xmlCal.getHour(), xmlCal.getMinute(), xmlCal.getSecond());
/* 2532 */       otlk.setTime1(cntTime);
/*      */     }
/*      */ 
/* 2535 */     Calendar issueTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2536 */     issueTime.set(14, 0);
/* 2537 */     XMLGregorianCalendar xmlIssueCal = fotlk.getIssueTime();
/* 2538 */     if (xmlIssueCal != null) {
/* 2539 */       issueTime.set(xmlIssueCal.getYear(), xmlIssueCal.getMonth() - 1, xmlIssueCal.getDay(), 
/* 2540 */         xmlIssueCal.getHour(), xmlIssueCal.getMinute(), xmlIssueCal.getSecond());
/* 2541 */       otlk.setIssueTime(issueTime);
/*      */     }
/*      */ 
/* 2544 */     Calendar expTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 2545 */     issueTime.set(14, 0);
/*      */ 
/* 2547 */     XMLGregorianCalendar xmlExpCal = fotlk.getExpTime();
/* 2548 */     if (xmlExpCal != null) {
/* 2549 */       expTime.set(xmlExpCal.getYear(), xmlExpCal.getMonth() - 1, xmlExpCal.getDay(), 
/* 2550 */         xmlExpCal.getHour(), xmlExpCal.getMinute(), xmlExpCal.getSecond());
/* 2551 */       otlk.setExpirationTime(expTime);
/*      */     }
/*      */ 
/* 2554 */     for (DECollection fdec : fotlk.getDECollection())
/*      */     {
/*      */       AbstractDrawableComponent de;
/* 2556 */       if (fdec.getCollectionName().equalsIgnoreCase(gov.noaa.nws.ncep.ui.pgen.elements.Outlook.OUTLOOK_LABELED_LINE)) {
/* 2557 */         gov.noaa.nws.ncep.ui.pgen.elements.DECollection dec = new gov.noaa.nws.ncep.ui.pgen.elements.DECollection(gov.noaa.nws.ncep.ui.pgen.elements.Outlook.OUTLOOK_LABELED_LINE);
/* 2558 */         List delist = convert(fdec.getDrawableElement());
/* 2559 */         for (Iterator localIterator2 = delist.iterator(); localIterator2.hasNext(); ) { de = (AbstractDrawableComponent)localIterator2.next();
/* 2560 */           de.setParent(dec);
/* 2561 */           dec.add(de);
/*      */         }
/* 2563 */         dec.setParent(otlk);
/* 2564 */         otlk.add(dec);
/*      */       }
/* 2568 */       else if (fdec.getCollectionName().equalsIgnoreCase(gov.noaa.nws.ncep.ui.pgen.elements.Outlook.OUTLOOK_LINE_GROUP)) {
/* 2569 */         gov.noaa.nws.ncep.ui.pgen.elements.DECollection grp = new gov.noaa.nws.ncep.ui.pgen.elements.DECollection(gov.noaa.nws.ncep.ui.pgen.elements.Outlook.OUTLOOK_LINE_GROUP);
/*      */ 
/* 2571 */         for (DECollection labeledLine : fdec.getDrawableElement().getDECollection()) {
/* 2572 */           gov.noaa.nws.ncep.ui.pgen.elements.DECollection lblLine = new gov.noaa.nws.ncep.ui.pgen.elements.DECollection(gov.noaa.nws.ncep.ui.pgen.elements.Outlook.OUTLOOK_LABELED_LINE);
/* 2573 */           List des = convert(labeledLine.getDrawableElement());
/* 2574 */           for (AbstractDrawableComponent de : des) {
/* 2575 */             de.setParent(lblLine);
/* 2576 */             lblLine.add(de);
/*      */           }
/* 2578 */           lblLine.setParent(grp);
/* 2579 */           grp.add(lblLine);
/*      */         }
/* 2581 */         grp.setParent(otlk);
/* 2582 */         otlk.add(grp);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2587 */     return otlk;
/*      */   }
/*      */ 
/*      */   public static Properties load(File propsFile)
/*      */   {
/* 2594 */     Properties props = new Properties();
/*      */     try {
/* 2596 */       FileInputStream fis = new FileInputStream(propsFile);
/* 2597 */       props.load(fis);
/*      */ 
/* 2599 */       fis.close();
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/* 2603 */     return props;
/*      */   }
/*      */ 
/*      */   public static gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano convertXML2Volcano(Volcano fVol)
/*      */   {
/* 2617 */     gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano vol = new gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano();
/*      */ 
/* 2619 */     vol.setPgenCategory(fVol.getPgenCategory());
/* 2620 */     vol.setPgenType(fVol.getPgenType());
/* 2621 */     vol.setSizeScale(fVol.getSizeScale().doubleValue());
/* 2622 */     vol.setLineWidth(fVol.getLineWidth().floatValue());
/* 2623 */     vol.setClear(fVol.isClear());
/*      */ 
/* 2625 */     java.awt.Color[] clr = new java.awt.Color[fVol.getColor().size()];
/* 2626 */     int nn = 0;
/* 2627 */     for (Color fColor : fVol.getColor()) {
/* 2628 */       clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2629 */         fColor.getBlue(), fColor.getAlpha().intValue());
/*      */     }
/* 2631 */     vol.setColors(clr); String fvp = fVol.getProduct() == null ? null : fVol.getProduct();
/* 2632 */     boolean isNoneDrawable = Arrays.asList(VaaInfo.ProductInfo.getProduct(gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo.LOCS[1])).contains(fvp);
/* 2633 */     ArrayList volPoints = new ArrayList();
/* 2634 */     nn = 0;
/* 2635 */     for (Point pt : fVol.getPoint()) {
/* 2636 */       volPoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */     }
/* 2638 */     if (!isNoneDrawable) vol.setPoints(volPoints);
/*      */ 
/* 2640 */     vol.setName(fVol.getName());
/* 2641 */     vol.setNumber(fVol.getNumber());
/* 2642 */     vol.setTxtLoc(fVol.getTxtLoc());
/* 2643 */     vol.setArea(fVol.getArea());
/* 2644 */     vol.setElev(fVol.getElev());
/*      */ 
/* 2646 */     vol.setOrigStnVAAC(fVol.getOrigStnVAAC());
/* 2647 */     vol.setWmoId(fVol.getWmoId());
/* 2648 */     vol.setHdrNum(fVol.getHdrNum());
/* 2649 */     vol.setProduct(fVol.getProduct());
/* 2650 */     vol.setYear(fVol.getYear());
/* 2651 */     vol.setAdvNum(fVol.getAdvNum());
/* 2652 */     vol.setCorr(fVol.getCorr());
/*      */ 
/* 2654 */     vol.setInfoSource(fVol.getInfoSource());
/* 2655 */     vol.setAddInfoSource(fVol.getAddInfoSource());
/* 2656 */     vol.setAviColorCode(fVol.getAviColorCode());
/* 2657 */     vol.setErupDetails(fVol.getErupDetails());
/*      */ 
/* 2659 */     vol.setObsAshDate(fVol.getObsAshDate());
/* 2660 */     vol.setObsAshTime(fVol.getObsAshTime());
/* 2661 */     vol.setNil(fVol.getNil());
/*      */ 
/* 2663 */     vol.setObsFcstAshCloudInfo(fVol.getObsFcstAshCloudInfo());
/* 2664 */     vol.setObsFcstAshCloudInfo6(fVol.getObsFcstAshCloudInfo6());
/* 2665 */     vol.setObsFcstAshCloudInfo12(fVol.getObsFcstAshCloudInfo12());
/* 2666 */     vol.setObsFcstAshCloudInfo18(fVol.getObsFcstAshCloudInfo18());
/*      */ 
/* 2668 */     vol.setRemarks(fVol.getRemarks());
/* 2669 */     vol.setNextAdv(fVol.getNextAdv());
/* 2670 */     vol.setForecasters(fVol.getForecasters());
/*      */ 
/* 2672 */     return vol;
/*      */   }
/*      */ 
/*      */   public static Volcano convertVolcano2XML(gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano vol)
/*      */   {
/* 2685 */     Volcano fVol = 
/* 2686 */       new Volcano();
/* 2687 */     String vp = vol.getProduct() == null ? null : vol.getProduct().trim();
/* 2688 */     boolean isNoneDrawable = Arrays.asList(VaaInfo.ProductInfo.getProduct(gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo.LOCS[1])).contains(vp);
/*      */ 
/* 2691 */     for (java.awt.Color clr : vol.getColors())
/*      */     {
/* 2693 */       Color fclr = 
/* 2694 */         new Color();
/*      */ 
/* 2696 */       fclr.setRed(clr.getRed());
/* 2697 */       fclr.setGreen(clr.getGreen());
/* 2698 */       fclr.setBlue(clr.getBlue());
/* 2699 */       fclr.setAlpha(Integer.valueOf(clr.getAlpha()));
/* 2700 */       fVol.getColor().add(fclr);
/*      */     }
/*      */ 
/* 2703 */     for (Coordinate crd : vol.getLinePoints()) {
/* 2704 */       if (isNoneDrawable) break;
/* 2705 */       Point fpt = new Point();
/* 2706 */       fpt.setLat(Double.valueOf(crd.y));
/* 2707 */       fpt.setLon(Double.valueOf(crd.x));
/*      */ 
/* 2709 */       fVol.getPoint().add(fpt);
/*      */     }
/*      */ 
/* 2712 */     fVol.setPgenCategory(vol.getPgenCategory());
/* 2713 */     fVol.setPgenType(vol.getPgenType());
/* 2714 */     fVol.setSizeScale(Double.valueOf(vol.getSizeScale()));
/* 2715 */     fVol.setLineWidth(Float.valueOf(vol.getLineWidth()));
/* 2716 */     fVol.setClear(vol.isClear());
/*      */ 
/* 2718 */     fVol.setName(vol.getName());
/* 2719 */     fVol.setNumber(vol.getNumber());
/* 2720 */     fVol.setTxtLoc(vol.getTxtLoc());
/* 2721 */     fVol.setArea(vol.getArea());
/* 2722 */     fVol.setElev(vol.getElev());
/*      */ 
/* 2724 */     fVol.setOrigStnVAAC(vol.getOrigStnVAAC());
/* 2725 */     fVol.setWmoId(vol.getWmoId());
/* 2726 */     fVol.setHdrNum(vol.getHdrNum());
/* 2727 */     fVol.setProduct(vol.getProduct());
/* 2728 */     fVol.setYear(vol.getYear());
/* 2729 */     fVol.setAdvNum(vol.getAdvNum());
/* 2730 */     fVol.setCorr(vol.getCorr());
/*      */ 
/* 2732 */     fVol.setInfoSource(vol.getInfoSource());
/* 2733 */     fVol.setAddInfoSource(vol.getAddInfoSource());
/* 2734 */     fVol.setAviColorCode(vol.getAviColorCode());
/* 2735 */     fVol.setErupDetails(vol.getErupDetails());
/*      */ 
/* 2737 */     fVol.setObsAshDate(vol.getObsAshDate());
/* 2738 */     fVol.setObsAshTime(vol.getObsAshTime());
/* 2739 */     fVol.setNil(vol.getNil());
/*      */ 
/* 2741 */     fVol.setObsFcstAshCloudInfo(vol.getObsFcstAshCloudInfo());
/* 2742 */     fVol.setObsFcstAshCloudInfo6(vol.getObsFcstAshCloudInfo6());
/* 2743 */     fVol.setObsFcstAshCloudInfo12(vol.getObsFcstAshCloudInfo12());
/* 2744 */     fVol.setObsFcstAshCloudInfo18(vol.getObsFcstAshCloudInfo18());
/*      */ 
/* 2746 */     fVol.setRemarks(vol.getRemarks());
/* 2747 */     fVol.setNextAdv(vol.getNextAdv());
/* 2748 */     fVol.setForecasters(vol.getForecasters());
/*      */ 
/* 2751 */     return fVol;
/*      */   }
/*      */ 
/*      */   private static String nvl(String str) {
/* 2755 */     return "".equals(str) ? null : str;
/*      */   }
/*      */ 
/*      */   private static LabeledLine convertXML2LabeledLine(DECollection dec, LabeledLine ll)
/*      */   {
/* 2765 */     if (ll != null) {
/* 2766 */       ll.setPgenCategory(dec.getPgenCategory());
/* 2767 */       ll.setPgenType(dec.getPgenType());
/*      */ 
/* 2769 */       DrawableElement elem = dec
/* 2770 */         .getDrawableElement();
/*      */       Iterator localIterator1;
/*      */       java.awt.Color[] clr;
/* 2772 */       if (elem.getDECollection() != null)
/*      */       {
/* 2775 */         localIterator1 = elem
/* 2775 */           .getDECollection().iterator();
/*      */ 
/* 2774 */         while (localIterator1.hasNext()) {
/* 2775 */           DECollection lblDec = (DECollection)localIterator1.next();
/* 2776 */           if (lblDec.getCollectionName().equalsIgnoreCase("Label")) {
/* 2777 */             Label lbl = new Label();
/* 2778 */             lbl.setParent(ll);
/*      */ 
/* 2780 */             DrawableElement lblDe = lblDec
/* 2781 */               .getDrawableElement();
/*      */             Object text;
/* 2783 */             if (lblDe.getMidCloudText() != null)
/*      */             {
/* 2786 */               for (MidCloudText aText : lblDe.getMidCloudText())
/*      */               {
/* 2788 */                 java.awt.Color[] clr = new java.awt.Color[aText.getColor().size()];
/* 2789 */                 int nn = 0;
/*      */ 
/* 2791 */                 Iterator localIterator3 = aText
/* 2791 */                   .getColor().iterator();
/*      */ 
/* 2790 */                 while (localIterator3.hasNext()) {
/* 2791 */                   Color fColor = (Color)localIterator3.next();
/* 2792 */                   clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2793 */                     fColor.getBlue(), fColor.getAlpha().intValue());
/*      */                 }
/*      */ 
/* 2796 */                 Point loc = aText.getPoint();
/*      */ 
/* 2798 */                 text = new gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText(null, aText.getFontName(), 
/* 2799 */                   aText.getFontSize().floatValue(), 
/* 2800 */                   IText.TextJustification.valueOf(aText.getJustification()), 
/* 2801 */                   new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 2802 */                   aText.getCloudTypes(), aText.getCloudAmounts(), 
/* 2803 */                   aText.getTurbulenceType(), aText.getTurbulenceLevels(), 
/* 2804 */                   aText.getIcingType(), aText.getIcingLevels(), 
/* 2805 */                   aText.getTstormTypes(), aText.getTstormLevels(), 
/* 2806 */                   IText.FontStyle.valueOf(aText.getStyle()), clr[0], aText.getPgenCategory(), 
/* 2807 */                   aText.getPgenType());
/*      */ 
/* 2810 */                 ((gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText)text).setParent(lbl);
/* 2811 */                 lbl.setSpe((SinglePointElement)text);
/*      */               }
/*      */             }
/*      */             Object text;
/* 2815 */             if (lblDe.getAvnText() != null)
/*      */             {
/* 2818 */               for (AvnText aText : lblDe.getAvnText())
/*      */               {
/* 2820 */                 java.awt.Color[] clr = new java.awt.Color[aText.getColor().size()];
/* 2821 */                 int nn = 0;
/*      */ 
/* 2823 */                 text = aText
/* 2823 */                   .getColor().iterator();
/*      */ 
/* 2822 */                 while (((Iterator)text).hasNext()) {
/* 2823 */                   Color fColor = (Color)((Iterator)text).next();
/* 2824 */                   clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2825 */                     fColor.getBlue(), fColor.getAlpha().intValue());
/*      */                 }
/*      */ 
/* 2828 */                 Point loc = aText.getPoint();
/*      */ 
/* 2830 */                 text = new gov.noaa.nws.ncep.ui.pgen.elements.AvnText(null, aText.getFontName(), 
/* 2831 */                   aText.getFontSize().floatValue(), 
/* 2832 */                   IText.TextJustification.valueOf(aText.getJustification()), 
/* 2833 */                   new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 2834 */                   IAvnText.AviationTextType.valueOf(aText.getAvnTextType()), 
/* 2835 */                   aText.getTopValue(), 
/* 2836 */                   aText.getBottomValue(), 
/* 2837 */                   IText.FontStyle.valueOf(aText.getStyle()), 
/* 2838 */                   clr[0], 
/* 2839 */                   aText.getSymbolPatternName(), 
/* 2840 */                   aText.getPgenCategory(), 
/* 2841 */                   aText.getPgenType());
/*      */ 
/* 2844 */                 ((gov.noaa.nws.ncep.ui.pgen.elements.AvnText)text).setParent(lbl);
/* 2845 */                 lbl.setSpe((SinglePointElement)text);
/*      */               }
/*      */             }
/* 2848 */             if (lblDe.getText() != null) handleCcfpText(lblDe, lbl);
/*      */ 
/* 2850 */             if (lblDe.getLine() != null)
/*      */             {
/* 2853 */               ??? = lblDe
/* 2853 */                 .getLine().iterator();
/*      */ 
/* 2852 */               while (???.hasNext()) {
/* 2853 */                 Line arrowLine = (Line)???.next();
/*      */ 
/* 2856 */                 clr = new java.awt.Color[arrowLine.getColor().size()];
/* 2857 */                 int nn = 0;
/*      */ 
/* 2859 */                 text = arrowLine
/* 2859 */                   .getColor().iterator();
/*      */ 
/* 2858 */                 while (((Iterator)text).hasNext()) {
/* 2859 */                   Color fColor = (Color)((Iterator)text).next();
/* 2860 */                   clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor
/* 2861 */                     .getGreen(), fColor.getBlue(), fColor
/* 2862 */                     .getAlpha().intValue());
/*      */                 }
/*      */ 
/* 2865 */                 ArrayList linePoints = new ArrayList();
/* 2866 */                 nn = 0;
/* 2867 */                 for (Point pt : arrowLine.getPoint()) {
/* 2868 */                   linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt
/* 2869 */                     .getLat().doubleValue()));
/*      */                 }
/*      */ 
/* 2872 */                 gov.noaa.nws.ncep.ui.pgen.elements.Line line = new gov.noaa.nws.ncep.ui.pgen.elements.Line(null, clr, arrowLine
/* 2873 */                   .getLineWidth().floatValue(), arrowLine.getSizeScale().doubleValue(), 
/* 2874 */                   arrowLine.isClosed().booleanValue(), arrowLine.isFilled().booleanValue(), 
/* 2875 */                   linePoints, arrowLine.getSmoothFactor().intValue(), 
/* 2876 */                   FillPatternList.FillPattern.valueOf(arrowLine
/* 2877 */                   .getFillPattern()), arrowLine
/* 2878 */                   .getPgenCategory(), arrowLine
/* 2879 */                   .getPgenType());
/*      */ 
/* 2881 */                 line.setParent(lbl);
/* 2882 */                 lbl.addArrow(line);
/*      */               }
/*      */             }
/*      */ 
/* 2886 */             ll.addLabel(lbl);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2893 */       if (elem.getLine() != null)
/*      */       {
/* 2895 */         for (Line fLine : elem.getLine())
/*      */         {
/* 2898 */           java.awt.Color[] clr = new java.awt.Color[fLine.getColor().size()];
/* 2899 */           int nn = 0;
/*      */ 
/* 2901 */           ??? = fLine
/* 2901 */             .getColor().iterator();
/*      */ 
/* 2900 */           while (???.hasNext()) {
/* 2901 */             Color fColor = (Color)???.next();
/* 2902 */             clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2903 */               fColor.getBlue(), fColor.getAlpha().intValue());
/*      */           }
/*      */ 
/* 2906 */           ArrayList linePoints = new ArrayList();
/* 2907 */           nn = 0;
/* 2908 */           for (Point pt : fLine.getPoint()) {
/* 2909 */             linePoints.add(new Coordinate(pt.getLon().doubleValue(), pt.getLat().doubleValue()));
/*      */           }
/*      */ 
/* 2912 */           gov.noaa.nws.ncep.ui.pgen.elements.Line line = new gov.noaa.nws.ncep.ui.pgen.elements.Line(null, clr, fLine.getLineWidth().floatValue(), fLine
/* 2913 */             .getSizeScale().doubleValue(), fLine.isClosed().booleanValue(), fLine.isFilled().booleanValue(), 
/* 2914 */             linePoints, fLine.getSmoothFactor().intValue(), 
/* 2915 */             FillPatternList.FillPattern.valueOf(fLine.getFillPattern()), fLine
/* 2916 */             .getPgenCategory(), fLine.getPgenType());
/*      */ 
/* 2918 */           line.setParent(ll);
/* 2919 */           ll.addLine(line);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2925 */     return ll;
/*      */   }
/*      */ 
/*      */   private static Cloud convertXML2Cloud(DECollection dec)
/*      */   {
/* 2936 */     Cloud cloud = new Cloud("Cloud");
/* 2937 */     return (Cloud)convertXML2LabeledLine(dec, cloud);
/*      */   }
/*      */ 
/*      */   private static Turbulence convertXML2Turb(DECollection dec)
/*      */   {
/* 2948 */     Turbulence turb = new Turbulence("Turbulence");
/* 2949 */     return (Turbulence)convertXML2LabeledLine(dec, turb);
/*      */   }
/*      */ 
/*      */   private static Ccfp convertXML2Ccfp(DECollection dec)
/*      */   {
/* 2955 */     String[] attr = dec.getCollectionName().split(":::");
/* 2956 */     gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet sig = new gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet();
/* 2957 */     sig.setEditableAttrPhenomSpeed(attr[1]);
/* 2958 */     sig.setEditableAttrPhenomDirection(attr[2]);
/* 2959 */     sig.setEditableAttrStartTime(attr[3]);
/* 2960 */     sig.setEditableAttrEndTime(attr[4]);
/* 2961 */     sig.setEditableAttrPhenom(attr[5]);
/* 2962 */     sig.setEditableAttrPhenom2(attr[6]);
/* 2963 */     sig.setEditableAttrPhenomLat(attr[7]);
/* 2964 */     sig.setEditableAttrPhenomLon(attr[8]);
/* 2965 */     sig.setType(attr[(attr.length - 1)]);
/*      */ 
/* 2967 */     Ccfp ccfp = new Ccfp(dec.getCollectionName());
/* 2968 */     ccfp.setSigmet(sig);
/* 2969 */     return (Ccfp)convertXML2LabeledLine(dec, ccfp);
/*      */   }
/*      */ 
/*      */   private static void handleCcfpText(DrawableElement lblDe, Label lbl)
/*      */   {
/* 2974 */     for (Text aText : lblDe.getText())
/*      */     {
/* 2976 */       java.awt.Color[] clr = new java.awt.Color[aText.getColor().size()];
/* 2977 */       int nn = 0;
/*      */ 
/* 2979 */       Iterator localIterator2 = aText
/* 2979 */         .getColor().iterator();
/*      */ 
/* 2978 */       while (localIterator2.hasNext()) {
/* 2979 */         Color fColor = (Color)localIterator2.next();
/* 2980 */         clr[(nn++)] = new java.awt.Color(fColor.getRed(), fColor.getGreen(), 
/* 2981 */           fColor.getBlue(), fColor.getAlpha().intValue());
/*      */       }
/*      */ 
/* 2984 */       Point loc = aText.getPoint();
/*      */ 
/* 2986 */       gov.noaa.nws.ncep.ui.pgen.elements.Text text = new gov.noaa.nws.ncep.ui.pgen.elements.Text(null, aText.getFontName(), aText.getFontSize().floatValue(), 
/* 2987 */         IText.TextJustification.valueOf(aText.getJustification()), new Coordinate(loc.getLon().doubleValue(), loc.getLat().doubleValue()), 
/* 2988 */         0.0D, IText.TextRotation.SCREEN_RELATIVE, (String[])aText.getTextLine().toArray(new String[0]), 
/* 2989 */         IText.FontStyle.valueOf(aText.getStyle()), clr[0], 0, 0, 
/* 2990 */         true, IText.DisplayType.BOX, aText.getPgenCategory(), aText.getPgenType());
/*      */ 
/* 2992 */       text.setParent(lbl);
/* 2993 */       lbl.setSpe(text);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Tcm convertXML2Tcm(TCM fileTcm)
/*      */   {
/* 3005 */     Tcm pgenTcm = new Tcm();
/*      */ 
/* 3007 */     pgenTcm.setPgenType(fileTcm.getPgenType());
/* 3008 */     pgenTcm.setPgenCategory(fileTcm.getPgenCategory());
/*      */ 
/* 3010 */     pgenTcm.setAdvisoryNumber(fileTcm.getAdvisoryNumber().intValue());
/* 3011 */     pgenTcm.setBasin(fileTcm.getBasin());
/* 3012 */     pgenTcm.setCentralPressure(fileTcm.getCentralPressure().intValue());
/* 3013 */     pgenTcm.setCorrection(fileTcm.getCorrection().booleanValue());
/* 3014 */     pgenTcm.setEyeSize(fileTcm.getEyeSize().intValue());
/* 3015 */     pgenTcm.setStormName(fileTcm.getStormName());
/* 3016 */     pgenTcm.setStormNumber(fileTcm.getStormNumber().intValue());
/* 3017 */     pgenTcm.setStormType(fileTcm.getStormType());
/*      */ 
/* 3019 */     Calendar tcmTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 3020 */     XMLGregorianCalendar xmlCal = fileTcm.getAdvisoryTime();
/* 3021 */     tcmTime.set(xmlCal.getYear(), xmlCal.getMonth() - 1, xmlCal.getDay(), 
/* 3022 */       xmlCal.getHour(), xmlCal.getMinute(), xmlCal.getSecond());
/* 3023 */     pgenTcm.setTime(tcmTime);
/*      */ 
/* 3025 */     pgenTcm.setWaveQuatro(fileTcm.getTcmWaves());
/* 3026 */     pgenTcm.setTcmFcst(fileTcm.getTcmFcst());
/*      */ 
/* 3028 */     return pgenTcm;
/*      */   }
/*      */ 
/*      */   private static TCM convertTcm2XML(Tcm pgenTcm)
/*      */   {
/* 3039 */     TCM fileTcm = 
/* 3040 */       new TCM();
/*      */ 
/* 3043 */     fileTcm.setAdvisoryNumber(Integer.valueOf(pgenTcm.getAdvisoryNumber()));
/*      */ 
/* 3045 */     XMLGregorianCalendar xmlCal = null;
/* 3046 */     Calendar tcmTime = pgenTcm.getAdvisoryTime();
/*      */     try
/*      */     {
/* 3049 */       xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
/* 3050 */         tcmTime.get(1), 
/* 3051 */         tcmTime.get(2) + 1, 
/* 3052 */         tcmTime.get(5), 
/* 3053 */         tcmTime.get(11), 
/* 3054 */         tcmTime.get(12), 
/* 3055 */         tcmTime.get(13), 
/* 3056 */         tcmTime.get(14), 
/* 3057 */         0);
/*      */     } catch (DatatypeConfigurationException e) {
/* 3059 */       e.printStackTrace();
/*      */     }
/*      */ 
/* 3062 */     fileTcm.setAdvisoryTime(xmlCal);
/*      */ 
/* 3064 */     fileTcm.setCentralPressure(Integer.valueOf(pgenTcm.getCentralPressure()));
/* 3065 */     fileTcm.setCorrection(Boolean.valueOf(pgenTcm.isCorrection()));
/* 3066 */     fileTcm.setEyeSize(Integer.valueOf(pgenTcm.getEyeSize()));
/* 3067 */     fileTcm.setPositionAccuracy(Integer.valueOf(pgenTcm.getPositionAccuracy()));
/*      */ 
/* 3069 */     fileTcm.setBasin(pgenTcm.getBasin());
/* 3070 */     fileTcm.setPgenCategory(pgenTcm.getPgenCategory());
/* 3071 */     fileTcm.setPgenType(pgenTcm.getPgenType());
/* 3072 */     fileTcm.setStormName(pgenTcm.getStormName());
/* 3073 */     fileTcm.setStormNumber(Integer.valueOf(pgenTcm.getStormNumber()));
/* 3074 */     fileTcm.setStormType(pgenTcm.getStormType());
/*      */ 
/* 3076 */     fileTcm.setTcmWaves(pgenTcm.getWaveQuarters());
/* 3077 */     fileTcm.setTcmFcst(pgenTcm.getTcmFcst());
/*      */ 
/* 3079 */     return fileTcm;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.ProductConverter
 * JD-Core Version:    0.6.2
 */