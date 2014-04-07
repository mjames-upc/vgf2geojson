/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ISymbol;
/*     */ import java.awt.Color;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE})
/*     */ public class Symbol extends SinglePointElement
/*     */   implements ISymbol
/*     */ {
/*     */   public Symbol()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Symbol(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, Boolean clear, Coordinate location, String pgenCategory, String pgenType)
/*     */   {
/*  56 */     super(range, colors, lineWidth, sizeScale, clear, location, pgenCategory, pgenType);
/*     */   }
/*     */ 
/*     */   public void update(IAttribute attr)
/*     */   {
/*  64 */     super.update(attr);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  71 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/*  73 */     result.append("Category:\t" + this.pgenCategory + "\n");
/*  74 */     result.append("Type:\t" + this.pgenType + "\n");
/*  75 */     result.append("Location:\t" + this.location.x + "\t" + this.location.y + "\n");
/*  76 */     result.append("Color:\t" + this.colors[0] + "\n");
/*  77 */     result.append("LineWidth:\t" + this.lineWidth + "\n");
/*  78 */     result.append("SizeScale:\t" + this.sizeScale + "\n");
/*  79 */     result.append("Clear:\t" + this.clear + "\n");
/*     */ 
/*  81 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/*  94 */     Symbol newSymbol = new Symbol();
/*  95 */     newSymbol.update(this);
/*     */ 
/* 101 */     newSymbol.setColors(new Color[] { new Color(getColors()[0].getRed(), 
/* 102 */       getColors()[0].getGreen(), 
/* 103 */       getColors()[0].getBlue()) });
/* 104 */     newSymbol.setLocation(new Coordinate(getLocation()));
/* 105 */     newSymbol.setPgenCategory(new String(getPgenCategory()));
/* 106 */     newSymbol.setPgenType(new String(getPgenType()));
/* 107 */     newSymbol.setParent(getParent());
/*     */ 
/* 109 */     return newSymbol;
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 115 */     return getPgenType();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Symbol
 * JD-Core Version:    0.6.2
 */