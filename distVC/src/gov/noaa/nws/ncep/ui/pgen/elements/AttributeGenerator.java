/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.common.staticdata.SPCCounty;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ArrowHead.ArrowHeadType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IArc;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAvnText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAvnText.AviationTextType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ICombo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IKink;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IMidCloudText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ISymbolSet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ITrack;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ITrack.ExtraPointTimeDisplayOption;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector.VectorType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IWatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.LinePatternManager;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.SymbolPatternManager;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.TrackPoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*     */ import java.awt.Color;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ 
/*     */ class AttributeGenerator
/*     */   implements IArc, IAvnText, ICombo, IKink, IMidCloudText, ISymbolSet, IText, ITrack, IVector, IWatchBox
/*     */ {
/*     */   private int smoothFactor;
/*     */   private int lpi;
/*     */   private boolean clear;
/*     */   private boolean filled;
/*     */   private boolean closed;
/*     */   private float lineWidth;
/*     */   private double sizeScale;
/*     */   private double kinkPosition;
/*     */   private String type;
/*     */   private String linePattern;
/*     */   private Color[] colors;
/* 238 */   private Color[] colorList = { Color.red, Color.green, Color.blue };
/*     */   private Coordinate location;
/*     */   private Coordinate[] linePoints;
/*     */   private ArrayList<Coordinate> points;
/*     */   private FillPatternList.FillPattern fillPattern;
/*     */   private ArrowHead.ArrowHeadType arrowHeadType;
/*     */   private String lat;
/*     */   private String lon;
/*     */ 
/*     */   public AttributeGenerator(DrawableType deType)
/*     */   {
/* 250 */     this.colors = new Color[10];
/* 251 */     this.points = new ArrayList();
/*     */ 
/* 253 */     Scanner stdin = new Scanner(System.in);
/*     */ 
/* 256 */     System.out.println("Please select a" + deType + " type:");
/* 257 */     for (DrawableType dt : DrawableType.values()) {
/* 258 */       System.out.println(dt.ordinal() + " - " + dt + "\t");
/*     */     }
/*     */ 
/* 261 */     switch (deType)
/*     */     {
/*     */     case ANY:
/* 264 */       break;
/*     */     case ARC:
/* 267 */       break;
/*     */     }
/*     */ 
/* 275 */     System.out.println("Please select a color:\n");
/* 276 */     System.out.println("\t1 = RED\t2 = Green\t3=Blue");
/*     */ 
/* 278 */     int myColor = stdin.nextInt();
/* 279 */     if ((myColor < 0) || (myColor > 2)) {
/* 280 */       myColor = 0;
/*     */     }
/*     */ 
/* 283 */     this.colors[0] = this.colorList[myColor];
/*     */ 
/* 285 */     System.out.println("Please input lineWidth (float):");
/* 286 */     this.lineWidth = stdin.nextFloat();
/* 287 */     System.out.println("Please input sizeScale (double):");
/* 288 */     this.sizeScale = stdin.nextDouble();
/*     */ 
/* 290 */     if (deType == DrawableType.SYMBOL) {
/* 291 */       System.out.println("Please select a SYMBOL type:");
/* 292 */       SymbolPatternManager spl = SymbolPatternManager.getInstance();
/* 293 */       int k = 0;
/* 294 */       for (String lpName : spl.getPatternNames()) {
/* 295 */         System.out.print(k + " - " + lpName + "\t\t");
/* 296 */         if ((k > 0) && (k % 3 == 0)) {
/* 297 */           System.out.println("\n");
/*     */         }
/* 299 */         k++;
/*     */       }
/*     */ 
/* 302 */       this.lpi = stdin.nextInt();
/* 303 */       if ((this.lpi < 0) || (this.lpi >= spl.getPatternNames().length)) {
/* 304 */         this.lpi = 0;
/*     */       }
/*     */ 
/* 307 */       this.type = spl.getPatternNames()[this.lpi];
/*     */ 
/* 309 */       System.out.println("Please input clear flag (0 = false\t1 = true):");
/* 310 */       this.clear = (stdin.nextInt() != 0);
/*     */ 
/* 312 */       System.out.println("Please input location (lat, lon):");
/* 313 */       System.out.println("Please input startPoint (lat, lon):");
/* 314 */       this.lat = stdin.next();
/* 315 */       this.lon = stdin.next();
/* 316 */       this.location = new Coordinate(Double.parseDouble(this.lat), 
/* 317 */         Double.parseDouble(this.lon));
/*     */     }
/* 320 */     else if (deType == DrawableType.LINE)
/*     */     {
/* 322 */       System.out.println("Please select a LINE type:");
/*     */ 
/* 324 */       LinePatternManager lpl = LinePatternManager.getInstance();
/* 325 */       int k = 0;
/* 326 */       for (String lpName : lpl.getPatternNames()) {
/* 327 */         System.out.print(k + " - " + lpName + "\t\t");
/* 328 */         if ((k > 0) && (k % 3 == 0)) {
/* 329 */           System.out.println("\n");
/*     */         }
/* 331 */         k++;
/*     */       }
/*     */ 
/* 334 */       this.lpi = stdin.nextInt();
/* 335 */       if ((this.lpi < 0) || (this.lpi >= lpl.getPatternNames().length)) {
/* 336 */         this.lpi = 0;
/*     */       }
/*     */ 
/* 339 */       this.type = lpl.getPatternNames()[this.lpi];
/* 340 */       this.linePattern = lpl.getPatternNames()[this.lpi];
/*     */ 
/* 342 */       System.out.println("Please input closed flag (0 = false\t1 = true):");
/* 343 */       this.closed = (stdin.nextInt() != 0);
/*     */ 
/* 345 */       System.out.println("Please input filled flag (0 = false\t1 = true):");
/* 346 */       this.filled = (stdin.nextInt() != 0);
/*     */ 
/* 348 */       if (this.filled) {
/* 349 */         System.out.println("Please select a FillPattern:");
/* 350 */         for (FillPatternList.FillPattern fp : FillPatternList.FillPattern.values()) {
/* 351 */           System.out.println(fp.ordinal() + " - " + fp + "\t");
/*     */         }
/*     */ 
/* 354 */         int fpi = stdin.nextInt();
/* 355 */         this.fillPattern = FillPatternList.FillPattern.SOLID;
/* 356 */         for (FillPatternList.FillPattern fp : FillPatternList.FillPattern.values()) {
/* 357 */           if (fp.ordinal() == fpi) {
/* 358 */             this.fillPattern = fp;
/* 359 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 364 */       System.out.println("Please input smoothFactor (0, 1, 2)");
/* 365 */       this.smoothFactor = stdin.nextInt();
/* 366 */       if ((this.smoothFactor < 0) || (this.smoothFactor > 2)) {
/* 367 */         this.smoothFactor = 2;
/*     */       }
/*     */ 
/* 370 */       System.out.println("Please enter points on the LINE:");
/* 371 */       System.out.println("One pair each line: Lat Lon");
/* 372 */       System.out.println("END will end the input");
/*     */ 
/* 374 */       this.points.clear();
/* 375 */       while (stdin.hasNextLine()) {
/* 376 */         if (stdin.hasNext()) {
/* 377 */           this.lat = stdin.next();
/* 378 */           if (this.lat.equals("END"))
/*     */             break;
/* 380 */           this.lon = stdin.next();
/* 381 */           if (this.lon.equals("END"))
/*     */             break;
/* 383 */           this.points.add(new Coordinate(Double.parseDouble(this.lat), 
/* 384 */             Double.parseDouble(this.lon)));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/* 389 */     else if (deType == DrawableType.KINKLINE)
/*     */     {
/* 391 */       System.out.println("Please input kinkPosition (double):");
/* 392 */       this.kinkPosition = stdin.nextFloat();
/*     */ 
/* 394 */       this.points.clear();
/* 395 */       System.out.println("Please input startPoint (lat, lon):");
/* 396 */       this.lat = stdin.next();
/* 397 */       this.lon = stdin.next();
/* 398 */       this.points.add(new Coordinate(Double.parseDouble(this.lat), 
/* 399 */         Double.parseDouble(this.lon)));
/*     */ 
/* 401 */       System.out.println("Please input endPoint (lat, lon):");
/* 402 */       this.lat = stdin.next();
/* 403 */       this.lon = stdin.next();
/* 404 */       this.points.add(new Coordinate(Double.parseDouble(this.lon), 
/* 405 */         Double.parseDouble(this.lat)));
/*     */ 
/* 408 */       System.out.println("Please select an ArrowHeadType:");
/* 409 */       System.out.println("0 - Filled\t1 - open");
/*     */ 
/* 411 */       this.lpi = stdin.nextInt();
/* 412 */       this.arrowHeadType = (this.lpi == 0 ? ArrowHead.ArrowHeadType.FILLED : ArrowHead.ArrowHeadType.OPEN);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 419 */     return this.type;
/*     */   }
/*     */ 
/*     */   public Color[] getColors() {
/* 423 */     return this.colors;
/*     */   }
/*     */ 
/*     */   public float getLineWidth() {
/* 427 */     return this.lineWidth;
/*     */   }
/*     */   public double getSizeScale() {
/* 430 */     return this.sizeScale;
/*     */   }
/*     */ 
/*     */   public Boolean isClear() {
/* 434 */     return Boolean.valueOf(this.clear);
/*     */   }
/*     */   public Coordinate getLocation() {
/* 437 */     return this.location;
/*     */   }
/*     */ 
/*     */   public int getSmoothFactor() {
/* 441 */     return this.smoothFactor;
/*     */   }
/*     */ 
/*     */   public String getLinePattern() {
/* 445 */     return this.linePattern;
/*     */   }
/*     */ 
/*     */   public FillPatternList.FillPattern getFillPattern() {
/* 449 */     return this.fillPattern;
/*     */   }
/*     */ 
/*     */   public Boolean isClosedLine() {
/* 453 */     return Boolean.valueOf(this.closed);
/*     */   }
/*     */ 
/*     */   public Boolean isFilled() {
/* 457 */     return Boolean.valueOf(this.filled);
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLinePoints() {
/* 461 */     this.linePoints = new Coordinate[this.points.size()];
/* 462 */     int k = 0;
/* 463 */     for (Coordinate coord : this.points) {
/* 464 */       this.linePoints[(k++)] = coord;
/*     */     }
/*     */ 
/* 467 */     return this.linePoints;
/*     */   }
/*     */ 
/*     */   public Color getColor() {
/* 471 */     return this.colors[0];
/*     */   }
/*     */ 
/*     */   public Coordinate getStartPoint() {
/* 475 */     return (Coordinate)this.points.get(0);
/*     */   }
/*     */ 
/*     */   public Coordinate getEndPoint() {
/* 479 */     return (Coordinate)this.points.get(1);
/*     */   }
/*     */ 
/*     */   public double getKinkPosition() {
/* 483 */     return this.kinkPosition;
/*     */   }
/*     */ 
/*     */   public ArrowHead.ArrowHeadType getArrowHeadType() {
/* 487 */     return this.arrowHeadType;
/*     */   }
/*     */ 
/*     */   public String getFontName()
/*     */   {
/* 492 */     return null;
/*     */   }
/*     */ 
/*     */   public float getFontSize()
/*     */   {
/* 497 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public IText.TextJustification getJustification()
/*     */   {
/* 502 */     return null;
/*     */   }
/*     */ 
/*     */   public double getRotation()
/*     */   {
/* 507 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public IText.TextRotation getRotationRelativity()
/*     */   {
/* 512 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getString()
/*     */   {
/* 517 */     return null;
/*     */   }
/*     */ 
/*     */   public IText.FontStyle getStyle()
/*     */   {
/* 522 */     return null;
/*     */   }
/*     */ 
/*     */   public int getXOffset()
/*     */   {
/* 527 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getYOffset()
/*     */   {
/* 532 */     return 0;
/*     */   }
/*     */ 
/*     */   public Boolean maskText()
/*     */   {
/* 537 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public IText.DisplayType getDisplayType()
/*     */   {
/* 542 */     return IText.DisplayType.NORMAL;
/*     */   }
/*     */ 
/*     */   public Boolean getHide()
/*     */   {
/* 547 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public Boolean getAuto()
/*     */   {
/* 552 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public Coordinate getCenterPoint()
/*     */   {
/* 557 */     return null;
/*     */   }
/*     */ 
/*     */   public Coordinate getCircumferencePoint()
/*     */   {
/* 562 */     return null;
/*     */   }
/*     */ 
/*     */   public double getAxisRatio()
/*     */   {
/* 567 */     return 1.0D;
/*     */   }
/*     */ 
/*     */   public double getStartAngle()
/*     */   {
/* 572 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double getEndAngle()
/*     */   {
/* 577 */     return 360.0D;
/*     */   }
/*     */ 
/*     */   public IVector.VectorType getVectorType()
/*     */   {
/* 582 */     return null;
/*     */   }
/*     */ 
/*     */   public double getSpeed()
/*     */   {
/* 587 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double getDirection()
/*     */   {
/* 592 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double getArrowHeadSize()
/*     */   {
/* 597 */     return 1.0D;
/*     */   }
/*     */ 
/*     */   public boolean hasDirectionOnly()
/*     */   {
/* 602 */     return false;
/*     */   }
/*     */ 
/*     */   public Boolean hasBackgroundMask()
/*     */   {
/* 607 */     return isClear();
/*     */   }
/*     */ 
/*     */   public Color getExtrapColor()
/*     */   {
/* 612 */     return null;
/*     */   }
/*     */ 
/*     */   public String getExtrapLinePattern()
/*     */   {
/* 617 */     return null;
/*     */   }
/*     */ 
/*     */   public String getExtrapMarker()
/*     */   {
/* 622 */     return null;
/*     */   }
/*     */ 
/*     */   public TrackPoint[] getExtrapPoints()
/*     */   {
/* 627 */     return null;
/*     */   }
/*     */ 
/*     */   public Color getInitialColor()
/*     */   {
/* 632 */     return null;
/*     */   }
/*     */ 
/*     */   public String getInitialLinePattern()
/*     */   {
/* 637 */     return null;
/*     */   }
/*     */ 
/*     */   public String getInitialMarker()
/*     */   {
/* 642 */     return null;
/*     */   }
/*     */ 
/*     */   public TrackPoint[] getInitialPoints()
/*     */   {
/* 647 */     return null;
/*     */   }
/*     */ 
/*     */   public IAvnText.AviationTextType getAvnTextType() {
/* 651 */     return null;
/*     */   }
/*     */ 
/*     */   public String getSymbolPatternName() {
/* 655 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasSymbolPattern() {
/* 659 */     return false;
/*     */   }
/*     */ 
/*     */   public String getTopValue() {
/* 663 */     return null;
/*     */   }
/*     */ 
/*     */   public String getBottomValue() {
/* 667 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasBottomValue() {
/* 671 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean getFillFlag() {
/* 675 */     return false;
/*     */   }
/*     */ 
/*     */   public Color getFillColor() {
/* 679 */     return null;
/*     */   }
/*     */ 
/*     */   public WatchBox.WatchShape getWatchBoxShape() {
/* 683 */     return null;
/*     */   }
/*     */ 
/*     */   public String getWatchSymbolType() {
/* 687 */     return "PLUS_SIGN";
/*     */   }
/*     */ 
/*     */   public float getWatchSymbolWidth() {
/* 691 */     return 1.5F;
/*     */   }
/*     */ 
/*     */   public double getWatchSymbolSize() {
/* 695 */     return 0.7D;
/*     */   }
/*     */ 
/*     */   public String getHazard() {
/* 699 */     return null;
/*     */   }
/*     */ 
/*     */   public String getGfaDesk() {
/* 703 */     return null;
/*     */   }
/*     */ 
/*     */   public String getGfaFcstHr() {
/* 707 */     return null;
/*     */   }
/*     */ 
/*     */   public String getGfaHazard() {
/* 711 */     return null;
/*     */   }
/*     */ 
/*     */   public String getGfaIssueType() {
/* 715 */     return null;
/*     */   }
/*     */ 
/*     */   public String getGfaTag() {
/* 719 */     return null;
/*     */   }
/*     */ 
/*     */   public String getGfaType() {
/* 723 */     return null;
/*     */   }
/*     */ 
/*     */   public HashMap<String, String> getGfaValues() {
/* 727 */     return null;
/*     */   }
/*     */ 
/*     */   public String getGfaArea() {
/* 731 */     return null;
/*     */   }
/*     */ 
/*     */   public String getGfaBeginning() {
/* 735 */     return null;
/*     */   }
/*     */ 
/*     */   public String getGfaEnding() {
/* 739 */     return null;
/*     */   }
/*     */ 
/*     */   public String getGfaStates() {
/* 743 */     return null;
/*     */   }
/*     */ 
/*     */   public int getGfaCycleDay() {
/* 747 */     return PgenCycleTool.getCycleDay();
/*     */   }
/*     */ 
/*     */   public int getGfaCycleHour() {
/* 751 */     return PgenCycleTool.getCycleHour();
/*     */   }
/*     */ 
/*     */   public Station[] getAnchors()
/*     */   {
/* 757 */     return null;
/*     */   }
/*     */ 
/*     */   public List<SPCCounty> getCountyList()
/*     */   {
/* 763 */     return null;
/*     */   }
/*     */ 
/*     */   public int getWatchNumber()
/*     */   {
/* 769 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getIssueFlag()
/*     */   {
/* 775 */     return 0;
/*     */   }
/*     */ 
/*     */   public IText.FontStyle getFontStyle()
/*     */   {
/* 781 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean[] getExtraPointTimeTextDisplayIndicator()
/*     */   {
/* 787 */     return null;
/*     */   }
/*     */ 
/*     */   public ITrack.ExtraPointTimeDisplayOption getExtraPointTimeDisplayOption()
/*     */   {
/* 793 */     return null;
/*     */   }
/*     */ 
/*     */   public Coordinate getPosition()
/*     */   {
/* 799 */     return null;
/*     */   }
/*     */ 
/*     */   public Color getTextColor()
/*     */   {
/* 805 */     return null;
/*     */   }
/*     */ 
/*     */   public Symbol getSymbol()
/*     */   {
/* 811 */     return null;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLocations()
/*     */   {
/* 817 */     return null;
/*     */   }
/*     */ 
/*     */   public String getCloudTypes()
/*     */   {
/* 823 */     return null;
/*     */   }
/*     */ 
/*     */   public String getCloudAmounts()
/*     */   {
/* 829 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasTurbulence()
/*     */   {
/* 835 */     return false;
/*     */   }
/*     */ 
/*     */   public String getTurbulencePattern()
/*     */   {
/* 841 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTurbulenceLevels()
/*     */   {
/* 847 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasIcing()
/*     */   {
/* 853 */     return false;
/*     */   }
/*     */ 
/*     */   public String getIcingPattern()
/*     */   {
/* 859 */     return null;
/*     */   }
/*     */ 
/*     */   public String getIcingLevels()
/*     */   {
/* 865 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasTstorm()
/*     */   {
/* 871 */     return false;
/*     */   }
/*     */ 
/*     */   public String getTstormTypes()
/*     */   {
/* 877 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTstormLevels()
/*     */   {
/* 883 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isTwoColumns()
/*     */   {
/* 889 */     return false;
/*     */   }
/*     */ 
/*     */   public String[] getPatternNames()
/*     */   {
/* 895 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 901 */     return null;
/*     */   }
/*     */ 
/*     */   public Calendar getFirstTimeCalendar()
/*     */   {
/* 907 */     return null;
/*     */   }
/*     */ 
/*     */   public Calendar getSecondTimeCalendar()
/*     */   {
/* 913 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isSetTimeButtonSelected()
/*     */   {
/* 919 */     return false;
/*     */   }
/*     */ 
/*     */   public int getExtraDrawingPointNumber()
/*     */   {
/* 925 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getSkipFactorText()
/*     */   {
/* 931 */     return null;
/*     */   }
/*     */ 
/*     */   public int getFontNameComboSelectedIndex()
/*     */   {
/* 937 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getFontSizeComboSelectedIndex()
/*     */   {
/* 943 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getFontStyleComboSelectedIndex()
/*     */   {
/* 949 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getUnitComboSelectedIndex()
/*     */   {
/* 955 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getRoundComboSelectedIndex()
/*     */   {
/* 961 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getRoundDirComboSelectedIndex()
/*     */   {
/* 967 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getIntervalTimeString()
/*     */   {
/* 973 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.AttributeGenerator
 * JD-Core Version:    0.6.2
 */