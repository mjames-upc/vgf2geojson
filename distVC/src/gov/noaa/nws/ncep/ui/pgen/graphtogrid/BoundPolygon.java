/*     */ package gov.noaa.nws.ncep.ui.pgen.graphtogrid;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.MultiPolygon;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BoundPolygon
/*     */ {
/*     */   private String bnds;
/*     */   public String boundsTableAlias;
/*     */   private String columnName;
/*     */   private String columnValue;
/*     */   private Boolean inout;
/*     */   private ArrayList<MultiPolygon> multiPolygons;
/*     */ 
/*     */   public BoundPolygon(String bnds)
/*     */   {
/*  81 */     this.bnds = bnds;
/*     */ 
/*  83 */     this.multiPolygons = new ArrayList();
/*  84 */     this.inout = Boolean.valueOf(true);
/*     */ 
/*  86 */     retrieveBounds();
/*     */   }
/*     */ 
/*     */   public String getBoundsTableAlias()
/*     */   {
/*  93 */     return this.boundsTableAlias;
/*     */   }
/*     */ 
/*     */   public String getColumnName()
/*     */   {
/* 100 */     return this.columnName;
/*     */   }
/*     */ 
/*     */   public String getColumnValue()
/*     */   {
/* 107 */     return this.columnValue;
/*     */   }
/*     */ 
/*     */   public Boolean getInout()
/*     */   {
/* 114 */     return this.inout;
/*     */   }
/*     */ 
/*     */   public ArrayList<MultiPolygon> getBoundPolygons()
/*     */   {
/* 121 */     return this.multiPolygons;
/*     */   }
/*     */ 
/*     */   private void retrieveBounds()
/*     */   {
/* 132 */     parseBounds();
/*     */ 
/* 134 */     this.multiPolygons = PgenStaticDataProvider.getProvider().getG2GBounds(this.boundsTableAlias, this.columnName, this.columnValue);
/*     */   }
/*     */ 
/*     */   private void parseBounds()
/*     */   {
/* 146 */     String[] bndStr = this.bnds.split("\\|");
/*     */ 
/* 149 */     this.inout = Boolean.valueOf(true);
/*     */ 
/* 151 */     if (bndStr != null) {
/* 152 */       if (bndStr.length > 0) {
/* 153 */         this.boundsTableAlias = bndStr[0];
/*     */       }
/*     */ 
/* 158 */       if (bndStr.length > 1) {
/* 159 */         int inds = bndStr[1].indexOf("<");
/* 160 */         int inde = bndStr[1].indexOf(">");
/*     */ 
/* 162 */         if ((inds >= 0) && (inde >= 0) && (inde > inds)) {
/* 163 */           this.columnName = bndStr[1].substring(bndStr[1].indexOf("<") + 1, bndStr[1].indexOf(">"));
/* 164 */           this.columnValue = bndStr[1].substring(bndStr[1].indexOf(">") + 1, bndStr[1].length());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 171 */       if ((bndStr.length > 2) && 
/* 172 */         (bndStr[2].trim().charAt(0) != 'T') && 
/* 173 */         (bndStr[2].trim().charAt(0) != 't'))
/* 174 */         this.inout = Boolean.valueOf(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean pointInPolygon(Point p, MultiPolygon polys)
/*     */   {
/* 193 */     return !polys.contains(p);
/*     */   }
/*     */ 
/*     */   public Contours getBoundsAsContours()
/*     */   {
/* 204 */     Contours boundContours = new Contours();
/*     */ 
/* 206 */     int nn = 0; int mm = 0;
/* 207 */     for (MultiPolygon mpoly : this.multiPolygons)
/*     */     {
/* 209 */       String cvalue = "-9999.0";
/* 210 */       if (!this.inout.booleanValue()) cvalue = "9999.0";
/*     */ 
/* 212 */       ArrayList linePts = new ArrayList();
/* 213 */       for (int ii = 0; ii < mpoly.getNumGeometries(); ii++)
/*     */       {
/* 215 */         Geometry g = mpoly.getGeometryN(ii);
/* 216 */         Coordinate[] cg = g.getCoordinates();
/*     */ 
/* 218 */         for (Coordinate pp : cg) {
/* 219 */           linePts.add(pp);
/*     */         }
/*     */ 
/* 222 */         ContourLine cline = new ContourLine(linePts, true, 
/* 223 */           new String[] { cvalue }, 1);
/*     */ 
/* 225 */         cline.setParent(boundContours);
/* 226 */         cline.getLine().setColors(new Color[] { Color.green });
/* 227 */         cline.getLine().setLineWidth(4.0F);
/* 228 */         cline.getLine().setSmoothFactor(0);
/* 229 */         boundContours.add(cline);
/*     */ 
/* 231 */         linePts.clear();
/*     */ 
/* 233 */         mm++;
/*     */       }
/*     */ 
/* 236 */       nn++;
/*     */     }
/*     */ 
/* 242 */     return boundContours;
/*     */   }
/*     */ 
/*     */   public static ArrayList<BoundPolygon> getAllBounds(String bndStr)
/*     */   {
/* 254 */     ArrayList allBounds = new ArrayList();
/*     */ 
/* 256 */     String[] bndArray = bndStr.split("\\+");
/*     */ 
/* 259 */     for (String str : bndArray) {
/* 260 */       if (str.trim().length() > 0) {
/* 261 */         BoundPolygon one = new BoundPolygon(str);
/* 262 */         if (one.getBoundPolygons().size() > 0) {
/* 263 */           allBounds.add(one);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 268 */     return allBounds;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.graphtogrid.BoundPolygon
 * JD-Core Version:    0.6.2
 */