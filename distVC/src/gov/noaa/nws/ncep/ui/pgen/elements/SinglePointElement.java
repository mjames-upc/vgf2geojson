/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ISinglePoint;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public abstract class SinglePointElement extends DrawableElement
/*     */   implements ISinglePoint
/*     */ {
/*     */   Color[] colors;
/*     */   float lineWidth;
/*     */   double sizeScale;
/*     */   Boolean clear;
/*     */   Coordinate location;
/*     */ 
/*     */   protected SinglePointElement()
/*     */   {
/*  47 */     this.colors = new Color[] { Color.red };
/*  48 */     this.lineWidth = 1.0F;
/*  49 */     this.sizeScale = 1.0D;
/*  50 */     this.clear = Boolean.valueOf(false);
/*  51 */     this.location = null;
/*     */   }
/*     */ 
/*     */   protected SinglePointElement(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, Boolean clear, Coordinate location, String pgenCategory, String pgenType)
/*     */   {
/*  66 */     super(range, pgenCategory, pgenType);
/*  67 */     this.colors = colors;
/*  68 */     this.lineWidth = lineWidth;
/*  69 */     this.sizeScale = sizeScale;
/*  70 */     this.clear = clear;
/*  71 */     this.location = location;
/*     */   }
/*     */ 
/*     */   public Coordinate getLocation()
/*     */   {
/*  80 */     return this.location;
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/*  89 */     return this.colors;
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/*  98 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */   public Boolean isClear()
/*     */   {
/* 107 */     return this.clear;
/*     */   }
/*     */ 
/*     */   public double getSizeScale()
/*     */   {
/* 116 */     return this.sizeScale;
/*     */   }
/*     */ 
/*     */   public void setLocation(Coordinate location)
/*     */   {
/* 123 */     setLocationOnly(location);
/*     */   }
/*     */ 
/*     */   public void setLocationOnly(Coordinate location) {
/* 127 */     this.location = location;
/*     */   }
/*     */ 
/*     */   public void setColors(Color[] colors)
/*     */   {
/* 133 */     if (colors != null)
/* 134 */       this.colors = colors;
/*     */   }
/*     */ 
/*     */   public void setLineWidth(float lineWidth)
/*     */   {
/* 142 */     if (!new Float(lineWidth).isNaN())
/* 143 */       this.lineWidth = lineWidth;
/*     */   }
/*     */ 
/*     */   public void setClear(Boolean clear)
/*     */   {
/* 151 */     if (clear != null)
/* 152 */       this.clear = clear;
/*     */   }
/*     */ 
/*     */   public void setSizeScale(double sizeScale)
/*     */   {
/* 160 */     if (!new Double(sizeScale).isNaN())
/* 161 */       this.sizeScale = sizeScale;
/*     */   }
/*     */ 
/*     */   public void update(IAttribute iattr)
/*     */   {
/* 169 */     if ((iattr instanceof ISinglePoint)) {
/* 170 */       ISinglePoint attr = (ISinglePoint)iattr;
/* 171 */       setClear(attr.isClear());
/* 172 */       setColors(attr.getColors());
/* 173 */       setLineWidth(attr.getLineWidth());
/* 174 */       setSizeScale(attr.getSizeScale());
/*     */     }
/*     */   }
/*     */ 
/*     */   public ArrayList<Coordinate> getPoints()
/*     */   {
/* 180 */     ArrayList pts = new ArrayList();
/* 181 */     pts.add(getLocation());
/* 182 */     return pts;
/*     */   }
/*     */ 
/*     */   public void setPointsOnly(ArrayList<Coordinate> pts)
/*     */   {
/* 188 */     setLocationOnly((Coordinate)pts.get(0));
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 193 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement
 * JD-Core Version:    0.6.2
 */