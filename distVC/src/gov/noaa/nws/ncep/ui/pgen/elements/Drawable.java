/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IMultiPoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ISinglePoint;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class Drawable
/*     */   implements IMultiPoint, ISinglePoint
/*     */ {
/*     */   private List<Coordinate> points;
/*     */   private HashMap<String, Object> properties;
/*     */ 
/*     */   public Drawable()
/*     */   {
/*  43 */     this.points = new ArrayList();
/*  44 */     this.properties = new HashMap();
/*     */   }
/*     */ 
/*     */   public Drawable(List<Coordinate> ipoints, HashMap<String, Object> prop)
/*     */   {
/*  51 */     if (ipoints == null) {
/*  52 */       this.points = new ArrayList();
/*     */     }
/*     */     else {
/*  55 */       this.points = ipoints;
/*     */     }
/*     */ 
/*  58 */     if (prop == null) {
/*  59 */       this.properties = new HashMap();
/*     */     }
/*     */     else
/*  62 */       this.properties = prop;
/*     */   }
/*     */ 
/*     */   public List<Coordinate> getPoints()
/*     */   {
/*  71 */     return this.points;
/*     */   }
/*     */ 
/*     */   public void setPoints(List<Coordinate> points)
/*     */   {
/*  78 */     this.points = points;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String propertyName)
/*     */   {
/*  85 */     return this.properties.get(propertyName);
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getProperties()
/*     */   {
/*  92 */     return this.properties;
/*     */   }
/*     */ 
/*     */   public void setProperties(HashMap<String, Object> prop)
/*     */   {
/*  99 */     this.properties = prop;
/*     */   }
/*     */ 
/*     */   public boolean matches(HashMap<String, Object> searchProperty)
/*     */   {
/* 106 */     for (Iterator ii = searchProperty.keySet().iterator(); ii.hasNext(); )
/*     */     {
/* 108 */       String propertyName = (String)ii.next();
/*     */ 
/* 111 */       if (!this.properties.get(propertyName).equals(
/* 111 */         searchProperty.get(propertyName))) {
/* 112 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   public void addPoint(int index, Coordinate point)
/*     */   {
/* 123 */     this.points.add(index, point);
/*     */   }
/*     */ 
/*     */   public void appendPoint(Coordinate point)
/*     */   {
/* 130 */     this.points.add(point);
/*     */   }
/*     */ 
/*     */   public void removePoint(int index)
/*     */   {
/* 137 */     this.points.remove(index);
/*     */   }
/*     */ 
/*     */   public Coordinate getPoint(int index)
/*     */   {
/* 144 */     return (Coordinate)this.points.get(index);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 151 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/* 153 */     result.append("\n");
/*     */ 
/* 155 */     for (Iterator ii = this.properties.keySet().iterator(); ii.hasNext(); )
/*     */     {
/* 157 */       String propertyName = (String)ii.next();
/* 158 */       result.append(propertyName);
/* 159 */       result.append(":\t");
/* 160 */       result.append(this.properties.get(propertyName));
/* 161 */       result.append("\n");
/*     */     }
/*     */ 
/* 164 */     result.append("\nPoints:\t");
/* 165 */     result.append(this.points.size());
/* 166 */     result.append("\n");
/*     */ 
/* 168 */     for (int ii = 0; ii < this.points.size(); ii++) {
/* 169 */       Coordinate onePoint = getPoint(ii);
/* 170 */       result.append(onePoint.x);
/* 171 */       result.append("\t");
/* 172 */       result.append(onePoint.y);
/* 173 */       result.append("\n");
/*     */     }
/*     */ 
/* 176 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 181 */     return (String)getProperty("Type");
/*     */   }
/*     */ 
/*     */   public Coordinate[] getLinePoints()
/*     */   {
/* 187 */     Coordinate[] a = new Coordinate[this.points.size()];
/* 188 */     this.points.toArray(a);
/* 189 */     return a;
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 195 */     Color[] colors = new Color[2];
/*     */ 
/* 197 */     colors[0] = ((Color)getProperty("color"));
/* 198 */     colors[1] = Color.red;
/*     */ 
/* 200 */     return colors;
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 206 */     return Float.parseFloat(((String)getProperty("lineWidth")).trim());
/*     */   }
/*     */ 
/*     */   public String getLinePattern()
/*     */   {
/* 212 */     return (String)getProperty("linePattern");
/*     */   }
/*     */ 
/*     */   public int getSmoothFactor()
/*     */   {
/* 218 */     return Integer.parseInt(((String)getProperty("smoothFactor")).trim());
/*     */   }
/*     */ 
/*     */   public Boolean isClosedLine()
/*     */   {
/* 224 */     return Boolean.valueOf(Boolean.parseBoolean(((String)getProperty("closed")).trim()));
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 230 */     return Boolean.valueOf(Boolean.parseBoolean(((String)getProperty("filled")).trim()));
/*     */   }
/*     */ 
/*     */   public FillPatternList.FillPattern getFillPattern()
/*     */   {
/* 236 */     return (FillPatternList.FillPattern)getProperty("fillPattern");
/*     */   }
/*     */ 
/*     */   public double getSizeScale()
/*     */   {
/* 242 */     return Double.parseDouble(((String)getProperty("sizeScale")).trim());
/*     */   }
/*     */ 
/*     */   public Coordinate getLocation()
/*     */   {
/* 248 */     return (Coordinate)this.points.get(0);
/*     */   }
/*     */ 
/*     */   public Boolean isClear()
/*     */   {
/* 254 */     return Boolean.valueOf(Boolean.parseBoolean(((String)getProperty("clear")).trim()));
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Drawable
 * JD-Core Version:    0.6.2
 */