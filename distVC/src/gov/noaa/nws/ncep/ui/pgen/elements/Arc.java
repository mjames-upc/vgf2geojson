/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IArc;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE})
/*     */ public class Arc extends Line
/*     */   implements IArc
/*     */ {
/*     */   private double axisRatio;
/*     */   private double startAngle;
/*     */   private double endAngle;
/*     */ 
/*     */   public Arc()
/*     */   {
/*  48 */     this.axisRatio = 1.0D;
/*  49 */     this.startAngle = 0.0D;
/*  50 */     this.endAngle = 360.0D;
/*     */   }
/*     */ 
/*     */   public Arc(Coordinate[] range, Color color, float lineWidth, double sizeScale, boolean closed, boolean filled, int smoothFactor, FillPatternList.FillPattern fillPattern, String pgenType, Coordinate centerPoint, Coordinate circumfencePoint, String pgenCategory, double axisRatio, double startAngle, double endAngle)
/*     */   {
/*  79 */     super(range, new Color[] { color }, lineWidth, sizeScale, closed, filled, 
/*  79 */       new ArrayList(), smoothFactor, fillPattern, pgenCategory, pgenType);
/*     */ 
/*  81 */     this.axisRatio = axisRatio;
/*  82 */     this.startAngle = startAngle;
/*  83 */     this.endAngle = endAngle;
/*     */ 
/*  85 */     setCenterPoint(centerPoint);
/*  86 */     setCircumferencePoint(circumfencePoint);
/*     */   }
/*     */ 
/*     */   public Coordinate getCenterPoint()
/*     */   {
/*  95 */     return (Coordinate)this.linePoints.get(0);
/*     */   }
/*     */ 
/*     */   public Coordinate getCircumferencePoint()
/*     */   {
/* 103 */     return (Coordinate)this.linePoints.get(1);
/*     */   }
/*     */ 
/*     */   public double getAxisRatio()
/*     */   {
/* 111 */     return this.axisRatio;
/*     */   }
/*     */ 
/*     */   public double getStartAngle()
/*     */   {
/* 119 */     return this.startAngle;
/*     */   }
/*     */ 
/*     */   public double getEndAngle()
/*     */   {
/* 127 */     return this.endAngle;
/*     */   }
/*     */ 
/*     */   public void setCenterPoint(Coordinate centerPoint)
/*     */   {
/* 135 */     if (this.linePoints != null) {
/* 136 */       this.linePoints.clear();
/*     */     }
/*     */ 
/* 139 */     this.linePoints.add(0, centerPoint);
/*     */   }
/*     */ 
/*     */   public void setCircumferencePoint(Coordinate circumferencePoint)
/*     */   {
/* 148 */     if ((this.linePoints != null) && (this.linePoints.size() > 1)) {
/* 149 */       this.linePoints.remove(1);
/*     */     }
/*     */ 
/* 152 */     this.linePoints.add(1, circumferencePoint);
/*     */   }
/*     */ 
/*     */   public void setAxisRatio(double axisRatio)
/*     */   {
/* 161 */     if (!new Double(axisRatio).isNaN())
/* 162 */       this.axisRatio = axisRatio;
/*     */   }
/*     */ 
/*     */   public void setStartAngle(double startAngle)
/*     */   {
/* 171 */     if (!new Double(startAngle).isNaN())
/*     */     {
/* 173 */       this.startAngle = startAngle;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setEndAngle(double endAngle)
/*     */   {
/* 182 */     if (!new Double(endAngle).isNaN())
/*     */     {
/* 184 */       this.endAngle = endAngle;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void update(IAttribute iattr)
/*     */   {
/* 194 */     if ((iattr instanceof IArc)) {
/* 195 */       IArc attr = (IArc)iattr;
/* 196 */       setAxisRatio(attr.getAxisRatio());
/* 197 */       setStartAngle(attr.getStartAngle());
/* 198 */       setEndAngle(attr.getEndAngle());
/* 199 */       setColors(attr.getColors());
/* 200 */       setLineWidth(attr.getLineWidth());
/* 201 */       setSizeScale(attr.getSizeScale());
/*     */     }
/*     */   }
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/* 217 */     Arc newArc = new Arc();
/* 218 */     newArc.update(this);
/*     */ 
/* 224 */     ArrayList ptsCopy = new ArrayList();
/* 225 */     for (int i = 0; i < getPoints().size(); i++) {
/* 226 */       ptsCopy.add(new Coordinate((Coordinate)getPoints().get(i)));
/*     */     }
/* 228 */     newArc.setPoints(ptsCopy);
/*     */ 
/* 234 */     Color[] colorCopy = new Color[getColors().length];
/* 235 */     for (int i = 0; i < getColors().length; i++) {
/* 236 */       colorCopy[i] = new Color(getColors()[i].getRed(), 
/* 237 */         getColors()[i].getGreen(), 
/* 238 */         getColors()[i].getBlue());
/*     */     }
/*     */ 
/* 241 */     newArc.setColors(colorCopy);
/*     */ 
/* 246 */     newArc.setPgenCategory(new String(getPgenCategory()));
/* 247 */     newArc.setPgenType(new String(getPgenType()));
/*     */ 
/* 249 */     newArc.setParent(getParent());
/* 250 */     return newArc;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 259 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/* 261 */     result.append("Category:\t" + this.pgenCategory + "\n");
/* 262 */     result.append("Type:\t" + this.pgenType + "\n");
/* 263 */     result.append("Color:\t" + getColors()[0] + "\n");
/* 264 */     result.append("LineWidth:\t" + this.lineWidth + "\n");
/* 265 */     result.append("SizeScale:\t" + this.sizeScale + "\n");
/* 266 */     result.append("Closed:\t" + this.closed + "\n");
/* 267 */     result.append("Filled:\t" + this.filled + "\n");
/* 268 */     result.append("FillPattern:\t" + this.fillPattern + "\n");
/* 269 */     result.append("smoothLevel:\t" + this.smoothFactor + "\n");
/* 270 */     result.append("AxisRatio:\t" + this.axisRatio + "\n");
/* 271 */     result.append("StartAngle:\t" + this.startAngle + "\n");
/* 272 */     result.append("EndAngle:\t" + this.endAngle + "\n");
/* 273 */     if (getCenterPoint() != null) {
/* 274 */       result.append("CenterPoint:\t" + getCenterPoint().y + "\t" + getCenterPoint().x + "\n");
/*     */     }
/*     */     else {
/* 277 */       result.append("CenterPoint:\tnot defined\n");
/*     */     }
/*     */ 
/* 280 */     if (getCircumferencePoint() != null) {
/* 281 */       result.append("CircumfencePoint:\t" + getCircumferencePoint().y + "\t" + 
/* 282 */         getCircumferencePoint().x + "\n");
/*     */     }
/*     */     else {
/* 285 */       result.append("CircumfencePoint:\tnot defined\n");
/*     */     }
/*     */ 
/* 288 */     return result.toString();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Arc
 * JD-Core Version:    0.6.2
 */