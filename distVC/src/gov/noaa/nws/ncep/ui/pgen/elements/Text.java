/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import java.awt.Color;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.ROTATE})
/*     */ public class Text extends SinglePointElement
/*     */   implements IText
/*     */ {
/*     */   private String fontName;
/*     */   private float fontSize;
/*     */   private IText.TextJustification justification;
/*     */   private double rotation;
/*     */   private IText.TextRotation rotationRelativity;
/*     */   private String[] text;
/*     */   private IText.FontStyle style;
/*     */   private int xOffset;
/*     */   private int yOffset;
/*     */   private Boolean mask;
/*     */   private IText.DisplayType displayType;
/*     */   private Boolean hide;
/*     */   private Boolean auto;
/*     */ 
/*     */   public Text()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Text(Coordinate[] range, String fontName, float fontSize, IText.TextJustification justification, Coordinate position, double rotation, IText.TextRotation rotationRelativity, String[] text, IText.FontStyle style, Color textColor, int offset, int offset2, boolean mask, IText.DisplayType outline, String pgenCategory, String pgenType)
/*     */   {
/* 125 */     super(range, new Color[] { textColor }, 1.0F, 0.0D, Boolean.valueOf(false), position, pgenCategory, pgenType);
/* 126 */     this.fontName = fontName;
/* 127 */     this.fontSize = fontSize;
/* 128 */     this.justification = justification;
/*     */ 
/* 130 */     this.rotation = rotation;
/* 131 */     this.rotationRelativity = rotationRelativity;
/* 132 */     this.text = text;
/* 133 */     this.style = style;
/*     */ 
/* 135 */     this.xOffset = offset;
/* 136 */     this.yOffset = offset2;
/* 137 */     this.mask = Boolean.valueOf(mask);
/* 138 */     this.displayType = outline;
/*     */ 
/* 140 */     this.hide = Boolean.valueOf(false);
/* 141 */     this.auto = Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public String getFontName()
/*     */   {
/* 149 */     return this.fontName;
/*     */   }
/*     */ 
/*     */   public float getFontSize()
/*     */   {
/* 157 */     return this.fontSize;
/*     */   }
/*     */ 
/*     */   public IText.TextJustification getJustification()
/*     */   {
/* 165 */     return this.justification;
/*     */   }
/*     */ 
/*     */   public Coordinate getPosition()
/*     */   {
/* 173 */     return getLocation();
/*     */   }
/*     */ 
/*     */   public double getRotation()
/*     */   {
/* 181 */     return this.rotation;
/*     */   }
/*     */ 
/*     */   public IText.TextRotation getRotationRelativity()
/*     */   {
/* 189 */     return this.rotationRelativity;
/*     */   }
/*     */ 
/*     */   public String[] getString()
/*     */   {
/* 197 */     return this.text;
/*     */   }
/*     */ 
/*     */   public IText.FontStyle getStyle()
/*     */   {
/* 205 */     return this.style;
/*     */   }
/*     */ 
/*     */   public Color getTextColor()
/*     */   {
/* 213 */     return this.colors[0];
/*     */   }
/*     */ 
/*     */   public int getXOffset()
/*     */   {
/* 221 */     return this.xOffset;
/*     */   }
/*     */ 
/*     */   public int getYOffset()
/*     */   {
/* 229 */     return this.yOffset;
/*     */   }
/*     */ 
/*     */   public Boolean maskText()
/*     */   {
/* 237 */     return this.mask;
/*     */   }
/*     */ 
/*     */   public IText.DisplayType getDisplayType()
/*     */   {
/* 245 */     return this.displayType;
/*     */   }
/*     */ 
/*     */   public void setFontName(String fontName)
/*     */   {
/* 252 */     if (fontName != null)
/* 253 */       this.fontName = fontName;
/*     */   }
/*     */ 
/*     */   public void setFontSize(float fontSize)
/*     */   {
/* 261 */     if (!new Float(fontSize).isNaN())
/* 262 */       this.fontSize = fontSize;
/*     */   }
/*     */ 
/*     */   public void setJustification(IText.TextJustification justification)
/*     */   {
/* 270 */     if (justification != null)
/* 271 */       this.justification = justification;
/*     */   }
/*     */ 
/*     */   public void setRotation(double rotation)
/*     */   {
/* 279 */     if (!new Double(rotation).isNaN())
/* 280 */       this.rotation = rotation;
/*     */   }
/*     */ 
/*     */   public void setRotationRelativity(IText.TextRotation rotationRelativity)
/*     */   {
/* 288 */     if (rotationRelativity != null)
/* 289 */       this.rotationRelativity = rotationRelativity;
/*     */   }
/*     */ 
/*     */   public void setStyle(IText.FontStyle style)
/*     */   {
/* 297 */     if (style != null)
/* 298 */       this.style = style;
/*     */   }
/*     */ 
/*     */   public void setXOffset(int offset)
/*     */   {
/* 306 */     this.xOffset = offset;
/*     */   }
/*     */ 
/*     */   public void setYOffset(int offset)
/*     */   {
/* 313 */     this.yOffset = offset;
/*     */   }
/*     */ 
/*     */   public void setMask(Boolean mask)
/*     */   {
/* 320 */     if (mask != null)
/* 321 */       this.mask = mask;
/*     */   }
/*     */ 
/*     */   public void setDisplayType(IText.DisplayType outline)
/*     */   {
/* 329 */     if (outline != null)
/* 330 */       this.displayType = outline;
/*     */   }
/*     */ 
/*     */   public void setHide(Boolean hide)
/*     */   {
/* 338 */     this.hide = hide;
/*     */   }
/*     */ 
/*     */   public Boolean getHide()
/*     */   {
/* 345 */     return this.hide;
/*     */   }
/*     */ 
/*     */   public void setAuto(Boolean auto)
/*     */   {
/* 352 */     this.auto = auto;
/*     */   }
/*     */ 
/*     */   public Boolean getAuto()
/*     */   {
/* 359 */     return this.auto;
/*     */   }
/*     */ 
/*     */   public String[] getText()
/*     */   {
/* 366 */     return this.text;
/*     */   }
/*     */ 
/*     */   public void setText(String[] text)
/*     */   {
/* 373 */     if (text != null)
/* 374 */       this.text = text;
/*     */   }
/*     */ 
/*     */   public void update(IAttribute iattr)
/*     */   {
/* 383 */     if ((iattr instanceof IText)) {
/* 384 */       super.update(iattr);
/* 385 */       IText attr = (IText)iattr;
/* 386 */       setFontName(attr.getFontName());
/* 387 */       setFontSize(attr.getFontSize());
/* 388 */       setJustification(attr.getJustification());
/* 389 */       setRotation(attr.getRotation());
/* 390 */       setRotationRelativity(attr.getRotationRelativity());
/* 391 */       setStyle(attr.getStyle());
/* 392 */       setXOffset(attr.getXOffset());
/* 393 */       setYOffset(attr.getYOffset());
/* 394 */       setMask(attr.maskText());
/* 395 */       setDisplayType(attr.getDisplayType());
/* 396 */       setHide(attr.getHide());
/* 397 */       setAuto(attr.getAuto());
/* 398 */       setText(attr.getString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/* 412 */     Text newText = new Text();
/* 413 */     newText.update(this);
/*     */ 
/* 419 */     newText.setColors(new Color[] { new Color(getColors()[0].getRed(), 
/* 420 */       getColors()[0].getGreen(), 
/* 421 */       getColors()[0].getBlue()) });
/* 422 */     newText.setLocation(new Coordinate(getLocation()));
/* 423 */     newText.setFontName(new String(getFontName()));
/*     */ 
/* 429 */     String[] textCopy = new String[getText().length];
/* 430 */     for (int i = 0; i < getText().length; i++) {
/* 431 */       textCopy[i] = new String(getText()[i]);
/*     */     }
/* 433 */     newText.setText(textCopy);
/*     */ 
/* 435 */     newText.setPgenCategory(new String(getPgenCategory()));
/* 436 */     newText.setPgenType(new String(getPgenType()));
/*     */ 
/* 438 */     newText.setHide(this.hide);
/* 439 */     newText.setAuto(this.auto);
/*     */ 
/* 441 */     newText.setParent(getParent());
/*     */ 
/* 443 */     return newText;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 450 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/* 452 */     result.append("\nCategory:\t" + this.pgenCategory + "\n");
/* 453 */     result.append("Type:\t" + this.pgenType + "\n");
/* 454 */     if (this.text != null) {
/* 455 */       for (String st : this.text) {
/* 456 */         result.append(st + "\n");
/*     */       }
/*     */     }
/* 459 */     result.append("Color:\t" + this.colors[0] + "\n");
/* 460 */     result.append("FontName:\t" + this.fontName + "\n");
/* 461 */     result.append("FontSize:\t" + this.fontSize + "\n");
/* 462 */     result.append("Justification:\t" + this.justification + "\n");
/* 463 */     result.append("Rotation:\t" + this.rotation + "\n");
/* 464 */     result.append("RotationRelativity:\t" + this.rotationRelativity + "\n");
/* 465 */     result.append("Style:\t" + this.style + "\n");
/* 466 */     result.append("XOffset:\t" + this.xOffset + "\n");
/* 467 */     result.append("YOffset:\t" + this.yOffset + "\n");
/* 468 */     result.append("Mask:\t" + this.mask + "\n");
/* 469 */     result.append("Outline:\t" + this.displayType + "\n");
/* 470 */     result.append("Hide:\t" + this.hide + "\n");
/* 471 */     result.append("Auto:\t" + this.hide + "\n");
/*     */ 
/* 473 */     if (this.location != null) {
/* 474 */       result.append("Position:\t" + this.location.y + "\t" + this.location.x + "\n");
/*     */     }
/*     */     else {
/* 477 */       result.append("Position:\t not defined \n");
/*     */     }
/*     */ 
/* 480 */     return result.toString();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Text
 * JD-Core Version:    0.6.2
 */