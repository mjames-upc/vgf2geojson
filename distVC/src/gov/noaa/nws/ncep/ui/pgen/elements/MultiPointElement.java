/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LinearRing;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IMultiPoint;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class MultiPointElement extends DrawableElement
/*     */   implements IMultiPoint
/*     */ {
/*     */   protected Color[] colors;
/*     */   protected float lineWidth;
/*     */   protected double sizeScale;
/*     */   protected boolean closed;
/*     */   protected boolean filled;
/*     */   protected int smoothFactor;
/*     */   protected FillPatternList.FillPattern fillPattern;
/*     */   protected ArrayList<Coordinate> linePoints;
/*     */   IAttribute attr;
/*     */ 
/*     */   protected MultiPointElement()
/*     */   {
/*  59 */     this.colors = new Color[] { Color.red };
/*  60 */     this.lineWidth = 1.0F;
/*  61 */     this.sizeScale = 1.0D;
/*  62 */     this.closed = false;
/*  63 */     this.filled = false;
/*  64 */     this.linePoints = null;
/*  65 */     this.smoothFactor = 2;
/*  66 */     this.fillPattern = FillPatternList.FillPattern.SOLID;
/*     */   }
/*     */ 
/*     */   protected MultiPointElement(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, boolean closed, boolean filled, List<Coordinate> linePoints, int smoothFactor, FillPatternList.FillPattern fillPattern, String pgenCategory, String pgenType)
/*     */   {
/*  85 */     super(range, pgenCategory, pgenType);
/*  86 */     this.colors = colors;
/*  87 */     this.lineWidth = lineWidth;
/*  88 */     this.sizeScale = sizeScale;
/*  89 */     this.closed = closed;
/*  90 */     this.filled = filled;
/*  91 */     this.smoothFactor = smoothFactor;
/*     */ 
/*  93 */     this.linePoints = new ArrayList();
/*     */ 
/*  95 */     if (linePoints != null) {
/*  96 */       this.linePoints.addAll(linePoints);
/*     */     }
/*     */ 
/*  99 */     FillPatternList.FillPattern fp = FillPatternList.FillPattern.SOLID;
/* 100 */     if (fillPattern != null) {
/* 101 */       this.fillPattern = fillPattern;
/*     */     }
/*     */     else
/* 104 */       this.fillPattern = fp;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLinePoints()
/*     */   {
/* 116 */     Coordinate[] a = new Coordinate[this.linePoints.size()];
/* 117 */     this.linePoints.toArray(a);
/* 118 */     return a;
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 128 */     return this.colors;
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 137 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */   public double getSizeScale()
/*     */   {
/* 146 */     return this.sizeScale;
/*     */   }
/*     */ 
/*     */   public void setLinePoints(List<Coordinate> linePoints)
/*     */   {
/* 153 */     this.linePoints = new ArrayList(linePoints);
/*     */   }
/*     */ 
/*     */   public void setColors(Color[] colors)
/*     */   {
/* 160 */     if (colors != null)
/* 161 */       if ((getPgenType() != null) && (getPgenType().contains("FRONT")))
/*     */       {
/* 163 */         if (getPgenType().contains("STATIONARY_FRONT"))
/*     */         {
/* 165 */           if (this.colors.length > colors.length)
/*     */           {
/* 167 */             for (int ii = 0; ii < colors.length; ii++) {
/* 168 */               this.colors[ii] = colors[ii];
/*     */             }
/*     */           }
/*     */           else {
/* 172 */             this.colors = colors;
/*     */           }
/*     */ 
/*     */         }
/* 177 */         else if (this.colors.length < colors.length)
/*     */         {
/* 179 */           for (int ii = 0; ii < this.colors.length; ii++) {
/* 180 */             this.colors[ii] = colors[ii];
/*     */           }
/*     */         }
/*     */         else {
/* 184 */           this.colors = colors;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 190 */         this.colors = colors;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void setLineWidth(float lineWidth)
/*     */   {
/* 199 */     if (!new Float(lineWidth).isNaN())
/* 200 */       this.lineWidth = lineWidth;
/*     */   }
/*     */ 
/*     */   public void setSmoothFactor(int smoothFactor)
/*     */   {
/* 208 */     if (smoothFactor >= 0)
/* 209 */       this.smoothFactor = smoothFactor;
/*     */   }
/*     */ 
/*     */   public void setFilled(Boolean filled)
/*     */   {
/* 217 */     if (filled != null)
/* 218 */       this.filled = filled.booleanValue();
/*     */   }
/*     */ 
/*     */   public void setClosed(Boolean closed)
/*     */   {
/* 226 */     if (closed != null)
/* 227 */       this.closed = closed.booleanValue();
/*     */   }
/*     */ 
/*     */   public void setSizeScale(double sizeScale)
/*     */   {
/* 235 */     if (!new Double(sizeScale).isNaN())
/* 236 */       this.sizeScale = sizeScale;
/*     */   }
/*     */ 
/*     */   public void setFillPattern(FillPatternList.FillPattern fillPattern)
/*     */   {
/* 243 */     this.fillPattern = fillPattern;
/*     */   }
/*     */ 
/*     */   public void update(IAttribute iattr)
/*     */   {
/* 250 */     if ((iattr instanceof IMultiPoint)) {
/* 251 */       IMultiPoint attr = (IMultiPoint)iattr;
/*     */ 
/* 253 */       setColors(attr.getColors());
/* 254 */       setLineWidth(attr.getLineWidth());
/* 255 */       setSizeScale(attr.getSizeScale());
/*     */ 
/* 257 */       setAttr(attr);
/*     */     }
/*     */   }
/*     */ 
/*     */   public IAttribute getAttr()
/*     */   {
/* 266 */     return this.attr;
/*     */   }
/*     */ 
/*     */   public void setAttr(IAttribute attr) {
/* 270 */     this.attr = attr;
/*     */   }
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/* 279 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public ArrayList<Coordinate> getPoints()
/*     */   {
/* 284 */     return this.linePoints;
/*     */   }
/*     */ 
/*     */   public void setPointsOnly(ArrayList<Coordinate> pts)
/*     */   {
/* 290 */     this.linePoints = pts;
/*     */   }
/*     */ 
/*     */   public void addPoint(int index, Coordinate point)
/*     */   {
/* 296 */     this.linePoints.add(index, point);
/*     */   }
/*     */ 
/*     */   public void removePoint(int index)
/*     */   {
/* 302 */     this.linePoints.remove(index);
/*     */   }
/*     */ 
/*     */   public Polygon toJTSPolygon()
/*     */   {
/* 316 */     GeometryFactory geometryFactory = new GeometryFactory();
/*     */ 
/* 318 */     Coordinate[] coords = new Coordinate[this.linePoints.size() + 1];
/*     */ 
/* 320 */     for (int ii = 0; ii < this.linePoints.size(); ii++) {
/* 321 */       coords[ii] = ((Coordinate)this.linePoints.get(ii));
/*     */     }
/* 323 */     coords[(coords.length - 1)] = coords[0];
/*     */ 
/* 325 */     CoordinateArraySequence cas = new CoordinateArraySequence(coords);
/* 326 */     LinearRing ring = new LinearRing(cas, geometryFactory);
/*     */ 
/* 328 */     Polygon polygon = new Polygon(ring, null, geometryFactory);
/*     */ 
/* 330 */     return polygon;
/*     */   }
/*     */ 
/*     */   public boolean getFilled()
/*     */   {
/* 337 */     return this.filled;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement
 * JD-Core Version:    0.6.2
 */