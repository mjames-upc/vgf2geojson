/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.CONNECT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.FLIP, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_PART, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.MODIFY, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.INTERPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.ADD_POINT})
/*     */ public class Line extends MultiPointElement
/*     */   implements ILine
/*     */ {
/*  47 */   private boolean flipSide = false;
/*     */ 
/*     */   public Line()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Line(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, boolean closed, boolean filled, List<Coordinate> linePoints, int smoothFactor, FillPatternList.FillPattern fillPattern, String pgenCategory, String pgenType)
/*     */   {
/*  74 */     super(range, colors, lineWidth, sizeScale, closed, filled, 
/*  74 */       linePoints, smoothFactor, fillPattern, pgenCategory, pgenType);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  81 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/*  83 */     result.append("Category:\t" + this.pgenCategory + "\n");
/*  84 */     result.append("Type:\t" + this.pgenType + "\n");
/*  85 */     result.append("Color:\t" + this.colors[0] + "\n");
/*  86 */     result.append("LineWidth:\t" + this.lineWidth + "\n");
/*  87 */     result.append("SizeScale:\t" + this.sizeScale + "\n");
/*  88 */     result.append("Closed:\t" + this.closed + "\n");
/*  89 */     result.append("Filled:\t" + this.filled + "\n");
/*  90 */     result.append("SmoothFactor:\t" + this.smoothFactor + "\n");
/*  91 */     result.append("FillPattern:\t" + this.fillPattern + "\n");
/*  92 */     result.append("Location:\t\n");
/*  93 */     for (Coordinate point : this.linePoints) {
/*  94 */       result.append("\t" + point.x + "\t" + point.y + "\n");
/*     */     }
/*     */ 
/*  97 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public void update(IAttribute iattr)
/*     */   {
/* 105 */     if ((iattr instanceof ILine)) {
/* 106 */       ILine attr = (ILine)iattr;
/* 107 */       super.update(attr);
/* 108 */       setClosed(attr.isClosedLine());
/* 109 */       setFilled(attr.isFilled());
/* 110 */       setSmoothFactor(attr.getSmoothFactor());
/* 111 */       if ((attr.isFilled() != null) && (attr.isFilled().booleanValue())) setFillPattern(attr.getFillPattern());
/*     */     }
/*     */   }
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/* 125 */     Line newLine = new Line();
/*     */ 
/* 130 */     newLine.setPgenCategory(new String(getPgenCategory()));
/* 131 */     newLine.setPgenType(new String(getPgenType()));
/* 132 */     newLine.setParent(getParent());
/*     */ 
/* 134 */     newLine.update(this);
/*     */ 
/* 140 */     ArrayList ptsCopy = new ArrayList();
/* 141 */     for (int i = 0; i < getPoints().size(); i++) {
/* 142 */       ptsCopy.add(new Coordinate((Coordinate)getPoints().get(i)));
/*     */     }
/* 144 */     newLine.setPoints(ptsCopy);
/*     */ 
/* 150 */     Color[] colorCopy = new Color[getColors().length];
/* 151 */     for (int i = 0; i < getColors().length; i++) {
/* 152 */       colorCopy[i] = new Color(getColors()[i].getRed(), 
/* 153 */         getColors()[i].getGreen(), 
/* 154 */         getColors()[i].getBlue());
/*     */     }
/* 156 */     newLine.setColors(colorCopy);
/* 157 */     newLine.setFlipSide(this.flipSide);
/*     */ 
/* 159 */     return newLine;
/*     */   }
/*     */ 
/*     */   public boolean isFlipSide()
/*     */   {
/* 167 */     return this.flipSide;
/*     */   }
/*     */ 
/*     */   public void setFlipSide(boolean flipSide)
/*     */   {
/* 174 */     this.flipSide = flipSide;
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 182 */     return getPgenType();
/*     */   }
/*     */ 
/*     */   public int getSmoothFactor()
/*     */   {
/* 192 */     return this.smoothFactor;
/*     */   }
/*     */ 
/*     */   public Boolean isClosedLine()
/*     */   {
/* 201 */     return Boolean.valueOf(this.closed);
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 210 */     return Boolean.valueOf(this.filled);
/*     */   }
/*     */ 
/*     */   public FillPatternList.FillPattern getFillPattern()
/*     */   {
/* 219 */     FillPatternList.FillPattern fp = FillPatternList.FillPattern.SOLID;
/*     */ 
/* 221 */     if (this.fillPattern != null) {
/* 222 */       fp = this.fillPattern;
/*     */     }
/*     */ 
/* 225 */     return fp;
/*     */   }
/*     */ 
/*     */   public Coordinate getEastMostPoint()
/*     */   {
/* 234 */     Coordinate eastMostPt = (Coordinate)getPoints().get(0);
/*     */ 
/* 236 */     for (Coordinate pt : getPoints()) {
/* 237 */       if (pt.x > eastMostPt.x) {
/* 238 */         eastMostPt = pt;
/*     */       }
/*     */     }
/*     */ 
/* 242 */     return eastMostPt;
/*     */   }
/*     */ 
/*     */   public Coordinate getCentroid()
/*     */   {
/* 251 */     if (getPoints().size() < 2) {
/* 252 */       return null;
/*     */     }
/* 254 */     GeometryFactory factory = new GeometryFactory();
/* 255 */     Coordinate[] a = new Coordinate[getPoints().size() + 1];
/* 256 */     getPoints().toArray(a);
/* 257 */     a[(a.length - 1)] = a[0];
/* 258 */     LineString g = factory.createLineString(a);
/* 259 */     Point p = g.getCentroid();
/* 260 */     return p.getCoordinate();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Line
 * JD-Core Version:    0.6.2
 */