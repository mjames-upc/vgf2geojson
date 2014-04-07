/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAvnText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAvnText.AviationTextType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import java.awt.Color;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE})
/*     */ public class AvnText extends Text
/*     */   implements IAvnText
/*     */ {
/*     */   private IAvnText.AviationTextType avnTextType;
/*  29 */   private String symbolPatternName = null;
/*  30 */   private String topValue = null;
/*  31 */   private String bottomValue = null;
/*     */ 
/*     */   public AvnText()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AvnText(Coordinate[] range, String fontName, float fontSize, IText.TextJustification justification, Coordinate position, IAvnText.AviationTextType textType, String topValue, String bottomValue, IText.FontStyle style, Color textColor, String symbolPatternName, String pgenCategory, String pgenType)
/*     */   {
/*  44 */     super(range, fontName, fontSize, justification, position, 0.0D, IText.TextRotation.SCREEN_RELATIVE, null, 
/*  44 */       style, textColor, 0, 0, false, IText.DisplayType.NORMAL, pgenCategory, pgenType);
/*     */ 
/*  46 */     this.avnTextType = textType;
/*  47 */     this.topValue = topValue;
/*  48 */     this.bottomValue = bottomValue;
/*  49 */     this.symbolPatternName = symbolPatternName;
/*     */   }
/*     */ 
/*     */   public IAvnText.AviationTextType getAvnTextType()
/*     */   {
/*  58 */     return this.avnTextType;
/*     */   }
/*     */ 
/*     */   public String getBottomValue()
/*     */   {
/*  66 */     return this.bottomValue;
/*     */   }
/*     */ 
/*     */   public String getSymbolPatternName()
/*     */   {
/*  74 */     return this.symbolPatternName;
/*     */   }
/*     */ 
/*     */   public String getTopValue()
/*     */   {
/*  82 */     return this.topValue;
/*     */   }
/*     */ 
/*     */   public boolean hasBottomValue()
/*     */   {
/*  90 */     if (this.bottomValue == null) {
/*  91 */       return false;
/*     */     }
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   public void setAvnTextType(IAvnText.AviationTextType avnTextType)
/*     */   {
/* 100 */     if (avnTextType != null)
/* 101 */       this.avnTextType = avnTextType;
/*     */   }
/*     */ 
/*     */   public void setSymbolPatternName(String symbolPatternName)
/*     */   {
/* 109 */     if (symbolPatternName != null)
/* 110 */       this.symbolPatternName = symbolPatternName;
/*     */   }
/*     */ 
/*     */   public void setTopValue(String topValue)
/*     */   {
/* 118 */     if (topValue != null)
/* 119 */       this.topValue = topValue;
/*     */   }
/*     */ 
/*     */   public void setBottomValue(String bottomValue)
/*     */   {
/* 127 */     if (bottomValue != null)
/* 128 */       this.bottomValue = bottomValue;
/*     */   }
/*     */ 
/*     */   public boolean hasSymbolPattern()
/*     */   {
/* 137 */     if (this.symbolPatternName == null) {
/* 138 */       return false;
/*     */     }
/* 140 */     return true;
/*     */   }
/*     */ 
/*     */   public void update(IAttribute iattr)
/*     */   {
/* 148 */     if ((iattr instanceof IAvnText)) {
/* 149 */       super.update(iattr);
/* 150 */       IAvnText attr = (IAvnText)iattr;
/* 151 */       setAvnTextType(attr.getAvnTextType());
/* 152 */       setTopValue(attr.getTopValue());
/* 153 */       setBottomValue(attr.getBottomValue());
/* 154 */       setSymbolPatternName(attr.getSymbolPatternName());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 162 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/* 164 */     result.append("\nCategory:\t" + this.pgenCategory + "\n");
/* 165 */     result.append("Type:\t" + this.pgenType + "\n");
/* 166 */     result.append("Text Type:\t" + this.avnTextType + "\n");
/* 167 */     result.append("Top Value:\t" + this.topValue + "\n");
/* 168 */     result.append("Bottom Value:\t" + this.bottomValue + "\n");
/* 169 */     result.append("Symbol Pattern:\t" + this.symbolPatternName + "\n");
/* 170 */     result.append("Color:\t" + this.colors[0] + "\n");
/* 171 */     result.append("FontName:\t" + getFontName() + "\n");
/* 172 */     result.append("FontSize:\t" + getFontSize() + "\n");
/* 173 */     result.append("Justification:\t" + getJustification() + "\n");
/* 174 */     result.append("Style:\t" + getStyle() + "\n");
/*     */ 
/* 176 */     result.append("Position:\t" + this.location.y + "\t" + this.location.x + "\n");
/*     */ 
/* 178 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/* 191 */     AvnText newText = new AvnText();
/* 192 */     newText.update(this);
/*     */ 
/* 198 */     newText.setColors(new Color[] { new Color(getColors()[0].getRed(), 
/* 199 */       getColors()[0].getGreen(), 
/* 200 */       getColors()[0].getBlue()) });
/* 201 */     newText.setLocation(new Coordinate(getLocation()));
/* 202 */     newText.setFontName(new String(getFontName()));
/*     */ 
/* 204 */     newText.setSymbolPatternName(new String(getSymbolPatternName()));
/* 205 */     newText.setTopValue(new String(getTopValue()));
/* 206 */     newText.setBottomValue(new String(getBottomValue()));
/*     */ 
/* 208 */     newText.setPgenCategory(new String(getPgenCategory()));
/* 209 */     newText.setPgenType(new String(getPgenType()));
/* 210 */     newText.setParent(getParent());
/*     */ 
/* 212 */     return newText;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.AvnText
 * JD-Core Version:    0.6.2
 */