/*    */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*    */ 
/*    */ import com.vividsolutions.jts.geom.Coordinate;
/*    */ import gov.noaa.nws.ncep.ui.pgen.display.ISymbolSet;
/*    */ import java.awt.Color;
/*    */ 
/*    */ public class SymbolLocationSet
/*    */   implements ISymbolSet
/*    */ {
/*    */   private Symbol symbol;
/*    */   private Coordinate[] locations;
/*    */ 
/*    */   public SymbolLocationSet(Symbol symbol, Coordinate[] locations)
/*    */   {
/* 51 */     this.symbol = symbol;
/* 52 */     this.locations = locations;
/*    */   }
/*    */ 
/*    */   public SymbolLocationSet(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, Boolean hasMask, Coordinate[] locations, String pgenCategory, String pgenType)
/*    */   {
/* 70 */     this.symbol = new Symbol(range, colors, lineWidth, sizeScale, hasMask, locations[0], pgenCategory, pgenType);
/* 71 */     this.locations = locations;
/*    */   }
/*    */ 
/*    */   public Coordinate[] getLocations()
/*    */   {
/* 79 */     return this.locations;
/*    */   }
/*    */ 
/*    */   public Symbol getSymbol()
/*    */   {
/* 87 */     return this.symbol;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.SymbolLocationSet
 * JD-Core Version:    0.6.2
 */