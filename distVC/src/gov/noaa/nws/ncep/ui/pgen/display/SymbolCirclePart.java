/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.adapters.CoordAdapter;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ public class SymbolCirclePart extends SymbolPart
/*     */ {
/*  32 */   private Coordinate[] path = null;
/*  33 */   private boolean pathNeedsUpdate = true;
/*     */ 
/*     */   @XmlJavaTypeAdapter(CoordAdapter.class)
/*     */   @XmlAttribute(name="center")
/*  40 */   private Coordinate center = null;
/*     */ 
/*     */   @XmlAttribute(name="radius")
/*  46 */   private double radius = 0.0D;
/*     */ 
/*     */   @XmlAttribute(name="isFilled")
/*     */   private boolean filled;
/*     */ 
/*     */   public SymbolCirclePart()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SymbolCirclePart(Coordinate center, double radius, boolean filled)
/*     */   {
/*  70 */     this.center = center;
/*  71 */     this.radius = radius;
/*  72 */     this.filled = filled;
/*     */ 
/*  74 */     updatePath();
/*     */   }
/*     */ 
/*     */   public Coordinate[] getPath()
/*     */   {
/*  83 */     if (this.pathNeedsUpdate) updatePath();
/*  84 */     return this.path;
/*     */   }
/*     */ 
/*     */   public boolean isFilled()
/*     */   {
/*  92 */     return this.filled;
/*     */   }
/*     */ 
/*     */   public void setFilled(boolean filled)
/*     */   {
/* 100 */     this.filled = filled;
/*     */   }
/*     */ 
/*     */   public Coordinate getCenter()
/*     */   {
/* 108 */     return this.center;
/*     */   }
/*     */ 
/*     */   public void setCenter(Coordinate center)
/*     */   {
/* 115 */     this.center = center;
/* 116 */     this.pathNeedsUpdate = true;
/*     */   }
/*     */ 
/*     */   public double getRadius()
/*     */   {
/* 123 */     return this.radius;
/*     */   }
/*     */ 
/*     */   public void setRadius(double radius)
/*     */   {
/* 130 */     this.radius = radius;
/* 131 */     this.pathNeedsUpdate = true;
/*     */   }
/*     */ 
/*     */   private void updatePath()
/*     */   {
/* 140 */     if ((this.center == null) || (this.radius == 0.0D)) return;
/*     */ 
/* 143 */     int numpts = 16;
/* 144 */     this.path = new Coordinate[numpts];
/*     */ 
/* 146 */     double increment = 360.0D / numpts;
/* 147 */     double angle = 0.0D;
/* 148 */     for (int j = 0; j < numpts; j++) {
/* 149 */       double x = this.center.x + this.radius * Math.cos(Math.toRadians(angle));
/* 150 */       double y = this.center.y + this.radius * Math.sin(Math.toRadians(angle));
/* 151 */       this.path[j] = new Coordinate(x, y);
/* 152 */       angle += increment;
/*     */     }
/* 154 */     this.pathNeedsUpdate = false;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.SymbolCirclePart
 * JD-Core Version:    0.6.2
 */