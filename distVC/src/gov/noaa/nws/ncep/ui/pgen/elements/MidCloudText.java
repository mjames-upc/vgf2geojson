/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IMidCloudText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import java.awt.Color;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE})
/*     */ public class MidCloudText extends Text
/*     */   implements IMidCloudText
/*     */ {
/*     */   private String cloudTypes;
/*     */   private String turbulencePattern;
/*     */   private String turbulenceLevels;
/*     */   private String icingPattern;
/*     */   private String icingLevels;
/*     */   private String tstormTypes;
/*     */   private String tstormLevels;
/*  36 */   private boolean twoColumns = true;
/*     */ 
/*     */   public MidCloudText()
/*     */   {
/*     */   }
/*     */ 
/*     */   public MidCloudText(Coordinate[] range, String fontName, float fontSize, IText.TextJustification justification, Coordinate position, String cloudTypes, String cloudAmounts, String turbulencePattern, String turbulenceLevels, String icingPattern, String icingLevels, String tstormTypes, String tstormLevels, IText.FontStyle style, Color textColor, String pgenCategory, String pgenType)
/*     */   {
/*  50 */     super(range, fontName, fontSize, justification, position, 0.0D, IText.TextRotation.SCREEN_RELATIVE, null, 
/*  50 */       style, textColor, 0, 0, false, IText.DisplayType.NORMAL, pgenCategory, pgenType);
/*     */ 
/*  52 */     this.cloudTypes = cloudTypes;
/*     */ 
/*  54 */     this.turbulencePattern = turbulencePattern;
/*  55 */     this.turbulenceLevels = turbulenceLevels;
/*  56 */     this.icingPattern = icingPattern;
/*  57 */     this.icingLevels = icingLevels;
/*  58 */     this.tstormTypes = tstormTypes;
/*  59 */     this.tstormLevels = tstormLevels;
/*     */   }
/*     */ 
/*     */   public String getCloudTypes()
/*     */   {
/*  67 */     return this.cloudTypes;
/*     */   }
/*     */ 
/*     */   public void setCloudTypes(String cloudTypes)
/*     */   {
/*  74 */     this.cloudTypes = cloudTypes;
/*     */   }
/*     */ 
/*     */   public String getCloudAmounts()
/*     */   {
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTurbulencePattern()
/*     */   {
/*  96 */     return this.turbulencePattern;
/*     */   }
/*     */ 
/*     */   public void setTurbulencePattern(String turbulencePattern)
/*     */   {
/* 103 */     this.turbulencePattern = turbulencePattern;
/*     */   }
/*     */ 
/*     */   public String getTurbulenceLevels()
/*     */   {
/* 110 */     return this.turbulenceLevels;
/*     */   }
/*     */ 
/*     */   public void setTurbulenceLevels(String turbulenceLevels)
/*     */   {
/* 117 */     this.turbulenceLevels = turbulenceLevels;
/*     */   }
/*     */ 
/*     */   public String getIcingPattern()
/*     */   {
/* 124 */     return this.icingPattern;
/*     */   }
/*     */ 
/*     */   public void setIcingPattern(String icingPattern)
/*     */   {
/* 131 */     this.icingPattern = icingPattern;
/*     */   }
/*     */ 
/*     */   public String getIcingLevels()
/*     */   {
/* 138 */     return this.icingLevels;
/*     */   }
/*     */ 
/*     */   public void setIcingLevels(String icingLevels)
/*     */   {
/* 145 */     this.icingLevels = icingLevels;
/*     */   }
/*     */ 
/*     */   public String getTstormTypes()
/*     */   {
/* 152 */     return this.tstormTypes;
/*     */   }
/*     */ 
/*     */   public void setTstormTypes(String tstormTypes)
/*     */   {
/* 159 */     this.tstormTypes = tstormTypes;
/*     */   }
/*     */ 
/*     */   public String getTstormLevels()
/*     */   {
/* 166 */     return this.tstormLevels;
/*     */   }
/*     */ 
/*     */   public void setTstormLevels(String tstormLevels)
/*     */   {
/* 173 */     this.tstormLevels = tstormLevels;
/*     */   }
/*     */ 
/*     */   public void update(IAttribute attr)
/*     */   {
/* 181 */     super.update(attr);
/*     */ 
/* 183 */     if ((attr instanceof IMidCloudText)) {
/* 184 */       IMidCloudText mid = (IMidCloudText)attr;
/* 185 */       if (mid.getCloudTypes() != null) setCloudTypes(mid.getCloudTypes());
/*     */ 
/* 187 */       if (mid.getTurbulencePattern() != null) setTurbulencePattern(mid.getTurbulencePattern());
/* 188 */       if (mid.getTurbulenceLevels() != null) setTurbulenceLevels(mid.getTurbulenceLevels());
/* 189 */       if (mid.getIcingPattern() != null) setIcingPattern(mid.getIcingPattern());
/* 190 */       if (mid.getIcingLevels() != null) setIcingLevels(mid.getIcingLevels());
/* 191 */       if (mid.getTstormTypes() != null) setTstormTypes(mid.getTstormTypes());
/* 192 */       if (mid.getTstormLevels() != null) setTstormLevels(mid.getTstormLevels());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 200 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/* 202 */     result.append("\nCategory:\t" + this.pgenCategory + "\n");
/* 203 */     result.append("Type:\t" + this.pgenType + "\n");
/* 204 */     result.append("Cloud Types:\t" + this.cloudTypes + "\n");
/*     */ 
/* 206 */     result.append("Turb Pattern:\t" + this.turbulencePattern + "\n");
/* 207 */     result.append("Turb Levels:\t" + this.turbulenceLevels + "\n");
/* 208 */     result.append("Icing Pattern:\t" + this.icingPattern + "\n");
/* 209 */     result.append("Icing Levels:\t" + this.icingLevels + "\n");
/* 210 */     result.append("Tstorm Types:\t" + this.tstormTypes + "\n");
/* 211 */     result.append("Tstorm Levels:\t" + this.tstormLevels + "\n");
/* 212 */     result.append("Color:\t" + this.colors[0] + "\n");
/* 213 */     result.append("FontName:\t" + getFontName() + "\n");
/* 214 */     result.append("FontSize:\t" + getFontSize() + "\n");
/* 215 */     result.append("Justification:\t" + getJustification() + "\n");
/* 216 */     result.append("Style:\t" + getStyle() + "\n");
/*     */ 
/* 218 */     result.append("Position:\t" + this.location.y + "\t" + this.location.x + "\n");
/*     */ 
/* 220 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/* 233 */     MidCloudText newText = new MidCloudText();
/* 234 */     newText.update(this);
/*     */ 
/* 240 */     newText.setColors(new Color[] { new Color(getColors()[0].getRed(), 
/* 241 */       getColors()[0].getGreen(), 
/* 242 */       getColors()[0].getBlue()) });
/* 243 */     newText.setLocation(new Coordinate(getLocation()));
/* 244 */     newText.setFontName(new String(getFontName()));
/*     */ 
/* 246 */     newText.setCloudTypes(new String(getCloudTypes()));
/*     */ 
/* 248 */     newText.setTurbulencePattern(new String(getTurbulencePattern()));
/* 249 */     newText.setTurbulenceLevels(new String(getTurbulenceLevels()));
/* 250 */     newText.setIcingPattern(new String(getIcingPattern()));
/* 251 */     newText.setIcingLevels(new String(getIcingLevels()));
/* 252 */     newText.setTstormTypes(new String(getTstormTypes()));
/* 253 */     newText.setTstormLevels(new String(getTstormLevels()));
/*     */ 
/* 255 */     newText.setPgenCategory(new String(getPgenCategory()));
/* 256 */     newText.setPgenType(new String(getPgenType()));
/* 257 */     newText.setParent(getParent());
/*     */ 
/* 259 */     newText.setTwoColumns(isTwoColumns());
/*     */ 
/* 261 */     return newText;
/*     */   }
/*     */ 
/*     */   public boolean hasIcing()
/*     */   {
/* 266 */     if ((this.icingLevels == null) || (this.icingLevels.isEmpty())) return false;
/* 267 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean hasTstorm()
/*     */   {
/* 272 */     if ((this.tstormTypes == null) || (this.tstormTypes.isEmpty())) return false;
/* 273 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean hasTurbulence()
/*     */   {
/* 278 */     if ((this.turbulenceLevels == null) || (this.turbulenceLevels.isEmpty())) return false;
/* 279 */     return true;
/*     */   }
/*     */ 
/*     */   public void setTwoColumns(boolean twoColumns) {
/* 283 */     this.twoColumns = twoColumns;
/*     */   }
/*     */ 
/*     */   public boolean isTwoColumns() {
/* 287 */     return this.twoColumns;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.MidCloudText
 * JD-Core Version:    0.6.2
 */