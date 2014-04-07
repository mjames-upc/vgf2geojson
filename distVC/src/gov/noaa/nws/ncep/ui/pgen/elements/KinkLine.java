/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ArrowHead.ArrowHeadType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IKink;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_PART, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_POINT})
/*     */ public class KinkLine extends Line
/*     */   implements IKink
/*     */ {
/*     */   double kinkPosition;
/*     */   ArrowHead.ArrowHeadType arrowHeadType;
/*     */ 
/*     */   public KinkLine()
/*     */   {
/*  47 */     this.kinkPosition = 0.5D;
/*  48 */     this.arrowHeadType = ArrowHead.ArrowHeadType.FILLED;
/*     */   }
/*     */ 
/*     */   public KinkLine(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, boolean closed, boolean filled, ArrayList<Coordinate> linePoints, int smoothFactor, FillPatternList.FillPattern fillPattern, String pgenType, String pgenCategory, double kinkPosition, ArrowHead.ArrowHeadType arrowHeadType)
/*     */   {
/*  74 */     super(range, colors, lineWidth, sizeScale, closed, filled, 
/*  74 */       linePoints, smoothFactor, fillPattern, pgenCategory, pgenType);
/*  75 */     this.kinkPosition = kinkPosition;
/*  76 */     this.arrowHeadType = arrowHeadType;
/*     */   }
/*     */ 
/*     */   public Color getColor()
/*     */   {
/*  84 */     return this.colors[0];
/*     */   }
/*     */ 
/*     */   public void setKinkPosition(double kinkPosition)
/*     */   {
/*  92 */     this.kinkPosition = kinkPosition;
/*     */   }
/*     */ 
/*     */   public double getKinkPosition()
/*     */   {
/* 100 */     return this.kinkPosition;
/*     */   }
/*     */ 
/*     */   public void setArrowHeadType(ArrowHead.ArrowHeadType arrowHeadType)
/*     */   {
/* 108 */     this.arrowHeadType = arrowHeadType;
/*     */   }
/*     */ 
/*     */   public ArrowHead.ArrowHeadType getArrowHeadType()
/*     */   {
/* 116 */     return this.arrowHeadType;
/*     */   }
/*     */ 
/*     */   public Coordinate getStartPoint()
/*     */   {
/* 124 */     if (this.linePoints.size() > 0) {
/* 125 */       return (Coordinate)this.linePoints.get(0);
/*     */     }
/*     */ 
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   public Coordinate getEndPoint()
/*     */   {
/* 137 */     if (this.linePoints.size() > 1) {
/* 138 */       return (Coordinate)this.linePoints.get(this.linePoints.size() - 1);
/*     */     }
/*     */ 
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   public void update(IAttribute iattr)
/*     */   {
/* 151 */     if ((iattr instanceof IKink)) {
/* 152 */       super.update(this.attr);
/* 153 */       IKink attr = (IKink)iattr;
/* 154 */       setKinkPosition(attr.getKinkPosition());
/* 155 */       setArrowHeadType(attr.getArrowHeadType());
/*     */ 
/* 157 */       ArrayList coords = new ArrayList();
/*     */ 
/* 159 */       coords.add(attr.getStartPoint());
/* 160 */       coords.add(attr.getEndPoint());
/* 161 */       setLinePoints(coords);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 169 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/* 171 */     result.append("Category:\t" + this.pgenCategory + "\n");
/* 172 */     result.append("Type:\t" + this.pgenType + "\n");
/* 173 */     result.append("Color:\t" + this.colors[0] + "\n");
/* 174 */     result.append("LineWidth:\t" + this.lineWidth + "\n");
/* 175 */     result.append("SizeScale:\t" + this.sizeScale + "\n");
/* 176 */     result.append("Closed:\t" + this.closed + "\n");
/* 177 */     result.append("Filled:\t" + this.filled + "\n");
/* 178 */     result.append("FillPattern:\t" + this.fillPattern + "\n");
/* 179 */     result.append("KinkPosition:\t" + this.kinkPosition + "\n");
/* 180 */     result.append("ArrowHeadType:\t" + this.arrowHeadType + "\n");
/* 181 */     result.append("Location:\t\n");
/* 182 */     for (Coordinate point : this.linePoints) {
/* 183 */       result.append("\t" + point.x + "\t" + point.y + "\n");
/*     */     }
/*     */ 
/* 186 */     return result.toString();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.KinkLine
 * JD-Core Version:    0.6.2
 */