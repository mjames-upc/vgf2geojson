/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector.VectorType;
/*     */ import java.awt.Color;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.ROTATE})
/*     */ public class Vector extends SinglePointElement
/*     */   implements IVector
/*     */ {
/*     */   private IVector.VectorType vectorType;
/*     */   private double speed;
/*     */   private double direction;
/*     */   private double arrowHeadSize;
/*     */   private boolean directionOnly;
/*     */ 
/*     */   public Vector()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Vector(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, Boolean clear, Coordinate location, IVector.VectorType vc, double speed, double direction, double arrowHeadSize, boolean directionOnly, String pgenCategory, String pgenType)
/*     */   {
/*  68 */     super(range, colors, lineWidth, sizeScale, clear, location, pgenCategory, pgenType);
/*     */ 
/*  70 */     this.vectorType = vc;
/*  71 */     this.speed = speed;
/*  72 */     this.direction = direction;
/*  73 */     this.arrowHeadSize = arrowHeadSize;
/*  74 */     this.directionOnly = directionOnly;
/*     */   }
/*     */ 
/*     */   public void setVectorType(IVector.VectorType vectorType)
/*     */   {
/*  82 */     this.vectorType = vectorType;
/*     */   }
/*     */ 
/*     */   public IVector.VectorType getVectorType()
/*     */   {
/*  89 */     return this.vectorType;
/*     */   }
/*     */ 
/*     */   public Color getColor()
/*     */   {
/*  96 */     return this.colors[0];
/*     */   }
/*     */ 
/*     */   public void setSpeed(double speed)
/*     */   {
/* 103 */     if (!new Double(speed).isNaN())
/* 104 */       this.speed = speed;
/*     */   }
/*     */ 
/*     */   public double getSpeed()
/*     */   {
/* 112 */     return this.speed;
/*     */   }
/*     */ 
/*     */   public void setDirection(double direction)
/*     */   {
/* 119 */     if (!new Double(direction).isNaN())
/* 120 */       this.direction = direction;
/*     */   }
/*     */ 
/*     */   public double getDirection()
/*     */   {
/* 128 */     return this.direction;
/*     */   }
/*     */ 
/*     */   public void setArrowHeadSize(double arrowHeadSize)
/*     */   {
/* 135 */     if (!new Double(arrowHeadSize).isNaN())
/* 136 */       this.arrowHeadSize = arrowHeadSize;
/*     */   }
/*     */ 
/*     */   public double getArrowHeadSize()
/*     */   {
/* 144 */     return this.arrowHeadSize;
/*     */   }
/*     */ 
/*     */   public void setDirectionOnly(boolean directionOnly)
/*     */   {
/* 151 */     this.directionOnly = directionOnly;
/*     */   }
/*     */ 
/*     */   public boolean hasDirectionOnly()
/*     */   {
/* 158 */     return this.directionOnly;
/*     */   }
/*     */ 
/*     */   public Boolean hasBackgroundMask()
/*     */   {
/* 165 */     return this.clear;
/*     */   }
/*     */ 
/*     */   public void update(IAttribute iattr)
/*     */   {
/* 174 */     if ((iattr instanceof IVector)) {
/* 175 */       super.update(iattr);
/*     */ 
/* 177 */       IVector attr = (IVector)iattr;
/*     */ 
/* 179 */       double spd = attr.getSpeed();
/* 180 */       if (!new Double(spd).isNaN()) {
/* 181 */         this.speed = spd;
/*     */       }
/*     */ 
/* 184 */       setDirection(attr.getDirection());
/* 185 */       setArrowHeadSize(attr.getArrowHeadSize());
/* 186 */       setClear(attr.hasBackgroundMask());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 195 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/* 197 */     result.append("\nCategory:\t" + this.pgenCategory + "\n");
/* 198 */     result.append("Type:\t" + this.pgenType + "\n");
/*     */ 
/* 200 */     result.append("Location:\t" + this.location.y + "\t" + this.location.x + "\n");
/* 201 */     result.append("Color:\t" + this.colors[0] + "\n");
/* 202 */     result.append("LineWidth:\t" + this.lineWidth + "\n");
/* 203 */     result.append("SizeScale:\t" + this.sizeScale + "\n");
/* 204 */     result.append("Clear:\t" + this.clear + "\n");
/* 205 */     result.append("VectorType:\t" + this.vectorType + "\n");
/* 206 */     result.append("Speed:\t" + this.speed + "\n");
/* 207 */     result.append("Direction:\t" + this.direction + "\n");
/* 208 */     result.append("Directional:\t" + this.directionOnly + "\n");
/* 209 */     result.append("ArrowHeadSize:\t" + this.arrowHeadSize + "\n");
/*     */ 
/* 211 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/* 224 */     Vector newVector = new Vector();
/* 225 */     newVector.update(this);
/*     */ 
/* 231 */     newVector.setColors(new Color[] { new Color(getColors()[0].getRed(), 
/* 232 */       getColors()[0].getGreen(), 
/* 233 */       getColors()[0].getBlue()) });
/* 234 */     newVector.setLocation(new Coordinate(getLocation()));
/*     */ 
/* 236 */     newVector.setPgenCategory(new String(getPgenCategory()));
/* 237 */     newVector.setPgenType(new String(getPgenType()));
/*     */ 
/* 239 */     newVector.setVectorType(getVectorType());
/* 240 */     newVector.setParent(getParent());
/*     */ 
/* 242 */     return newVector;
/*     */   }
/*     */ 
/*     */   public static double northRotationAngle(Coordinate point1, Coordinate point2)
/*     */   {
/* 258 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/* 259 */     gc.setStartingGeographicPoint(point1.x, point1.y);
/* 260 */     gc.setDestinationGeographicPoint(point2.x, point2.y);
/*     */ 
/* 262 */     double azimuth = gc.getAzimuth();
/* 263 */     double angle = azimuth;
/* 264 */     if (angle < 0.0D) {
/* 265 */       angle += 360.0D;
/*     */     }
/*     */ 
/* 268 */     return angle;
/*     */   }
/*     */ 
/*     */   public double vectorDirection(Coordinate point1, Coordinate point2)
/*     */   {
/* 288 */     double dir = northRotationAngle(point1, point2);
/*     */ 
/* 290 */     if (this.pgenType.equalsIgnoreCase("Hash"))
/*     */     {
/* 292 */       dir = 90.0D - dir;
/* 293 */       if (dir < 0.0D) dir += 360.0D;
/*     */ 
/*     */     }
/* 296 */     else if (!this.pgenType.equalsIgnoreCase("Barb"))
/*     */     {
/* 301 */       dir += 180.0D;
/* 302 */       if (dir > 360.0D) {
/* 303 */         dir -= 360.0D;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 308 */     double direction = (int)(dir + 3.0D) / 5 * 5;
/*     */ 
/* 310 */     return direction;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Vector
 * JD-Core Version:    0.6.2
 */