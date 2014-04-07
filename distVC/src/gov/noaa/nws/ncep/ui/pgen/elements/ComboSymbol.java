/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ICombo;
/*     */ import java.awt.Color;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE})
/*     */ public class ComboSymbol extends SinglePointElement
/*     */   implements ICombo
/*     */ {
/*     */   public ComboSymbol()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ComboSymbol(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, Boolean clear, Coordinate location, String pgenCategory, String pgenType)
/*     */   {
/*  54 */     super(range, colors, lineWidth, sizeScale, clear, location, pgenCategory, pgenType);
/*     */   }
/*     */ 
/*     */   public void update(IAttribute attr)
/*     */   {
/*  62 */     super.update(attr);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  69 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/*  71 */     result.append("Category:\t" + this.pgenCategory + "\n");
/*  72 */     result.append("Type:\t" + this.pgenType + "\n");
/*  73 */     result.append("Location:\t" + this.location.x + "\t" + this.location.y + "\n");
/*  74 */     result.append("Color:\t" + this.colors[0] + "\n");
/*  75 */     result.append("LineWidth:\t" + this.lineWidth + "\n");
/*  76 */     result.append("SizeScale:\t" + this.sizeScale + "\n");
/*  77 */     result.append("Clear:\t" + this.clear + "\n");
/*     */ 
/*  79 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/*  92 */     ComboSymbol newSymbol = new ComboSymbol();
/*  93 */     newSymbol.update(this);
/*     */ 
/*  99 */     newSymbol.setColors(new Color[] { new Color(getColors()[0].getRed(), 
/* 100 */       getColors()[0].getGreen(), 
/* 101 */       getColors()[0].getBlue()) });
/* 102 */     newSymbol.setLocation(new Coordinate(getLocation()));
/* 103 */     newSymbol.setPgenCategory(new String(getPgenCategory()));
/* 104 */     newSymbol.setPgenType(new String(getPgenType()));
/*     */ 
/* 106 */     return newSymbol;
/*     */   }
/*     */ 
/*     */   public String[] getPatternNames()
/*     */   {
/* 118 */     String type = getPgenType();
/* 119 */     return type.split("\\|");
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.ComboSymbol
 * JD-Core Version:    0.6.2
 */