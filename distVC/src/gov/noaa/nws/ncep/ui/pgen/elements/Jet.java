/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector.VectorType;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.CONNECT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.FLIP, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_PART, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.ADD_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.MODIFY})
/*     */ public class Jet extends DECollection
/*     */ {
/*     */   private IJetTools snapTool;
/*     */ 
/*     */   public Jet()
/*     */   {
/*  49 */     super("Jet");
/*     */ 
/*  53 */     JetLine jetLine = new JetLine(null);
/*  54 */     add(jetLine);
/*     */   }
/*     */ 
/*     */   public Jet(IAttribute attr, ArrayList<Coordinate> points)
/*     */   {
/*  59 */     super("Jet");
/*     */ 
/*  63 */     JetLine jetLine = new JetLine(null);
/*  64 */     jetLine.update(attr);
/*     */ 
/*  66 */     jetLine.setLinePoints(points);
/*     */ 
/*  69 */     add(jetLine);
/*     */   }
/*     */ 
/*     */   public IJetTools getSnapTool()
/*     */   {
/*  78 */     return this.snapTool;
/*     */   }
/*     */ 
/*     */   public void setSnapTool(IJetTools snapTool)
/*     */   {
/*  87 */     if (this.snapTool == null) {
/*  88 */       this.snapTool = snapTool;
/*     */ 
/*  90 */       Iterator it = createDEIterator();
/*  91 */       while (it.hasNext()) {
/*  92 */         DrawableElement de = (DrawableElement)it.next();
/*  93 */         if (((de instanceof JetText)) && 
/*  94 */           (((JetText)de).getLatLonFlag()))
/*     */         {
/* 100 */           ((JetText)de).setLocation(((JetText)de).getLoc());
/* 101 */           ((JetText)de).setLatLonFlag(false);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 108 */       this.snapTool = snapTool;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addBarb(DECollection dec)
/*     */   {
/* 118 */     add(dec);
/*     */ 
/* 120 */     if (this.snapTool != null)
/* 121 */       this.snapTool.snapJet(dec, this);
/*     */   }
/*     */ 
/*     */   public void addHash(JetHash hash)
/*     */   {
/* 127 */     add(hash);
/*     */ 
/* 129 */     if (this.snapTool != null)
/* 130 */       this.snapTool.snapHash(hash, hash.getLocation(), this);
/*     */   }
/*     */ 
/*     */   public JetLine getJetLine()
/*     */   {
/* 140 */     return (JetLine)getPrimaryDE();
/*     */   }
/*     */ 
/*     */   public void removeAllHash()
/*     */   {
/* 145 */     Iterator it = this.compList.iterator();
/* 146 */     while (it.hasNext()) {
/* 147 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 148 */       if (((adc instanceof Vector)) && 
/* 149 */         (((Vector)adc).getVectorType() == IVector.VectorType.HASH_MARK))
/* 150 */         it.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Jet copy()
/*     */   {
/* 161 */     Jet newJet = new Jet();
/*     */ 
/* 163 */     newJet.remove(newJet.getJetLine());
/*     */ 
/* 165 */     newJet.setSnapTool(this.snapTool);
/*     */ 
/* 168 */     Iterator iterator = getComponentIterator();
/*     */ 
/* 170 */     while (iterator.hasNext()) {
/* 171 */       newJet.addElement(((AbstractDrawableComponent)iterator.next()).copy());
/*     */     }
/*     */ 
/* 174 */     newJet.setPgenCategory(getPgenCategory());
/* 175 */     newJet.setPgenType(getPgenType());
/* 176 */     newJet.setParent(getParent());
/*     */ 
/* 178 */     return newJet;
/*     */   }
/*     */ 
/*     */   public class JetBarb extends Vector
/*     */   {
/*     */     private JetBarb()
/*     */     {
/*     */     }
/*     */ 
/*     */     public JetBarb(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, Boolean clear, Coordinate location, IVector.VectorType vc, double speed, double direction, double arrowHeadSize, boolean directionOnly, String pgenCategory, String pgenType)
/*     */     {
/* 276 */       super(colors, lineWidth, sizeScale, clear, location, vc, speed, 
/* 276 */         direction, arrowHeadSize, directionOnly, pgenCategory, pgenType);
/*     */     }
/*     */ 
/*     */     public void setLocation(Coordinate location)
/*     */     {
/* 286 */       if (this.parent != null)
/*     */       {
/* 288 */         if (Jet.this.snapTool != null) {
/* 289 */           Jet.this.snapTool.snapJet(this.parent, (Jet)this.parent.getParent());
/*     */         }
/*     */         else
/* 292 */           super.setLocation(location);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setSpeedOnly(double spd)
/*     */     {
/* 298 */       super.setSpeed(spd);
/*     */     }
/*     */ 
/*     */     public void setSpeed(double spd)
/*     */     {
/* 304 */       super.setSpeed(spd);
/* 305 */       if (this.parent != null)
/*     */       {
/* 307 */         if ((Jet.this.snapTool != null) && 
/* 308 */           ((this.parent.getParent() instanceof Jet)))
/* 309 */           Jet.this.snapTool.snapJet(this.parent, (Jet)this.parent.getParent());
/*     */       }
/*     */     }
/*     */ 
/*     */     public JetBarb copy()
/*     */     {
/* 321 */       Vector newBarb = (Vector)super.copy();
/* 322 */       JetBarb newJetBarb = new JetBarb(Jet.this);
/*     */ 
/* 324 */       newJetBarb.setParent(this.parent);
/*     */ 
/* 326 */       newJetBarb.update(newBarb);
/* 327 */       newJetBarb.setLocationOnly(newBarb.getLocation());
/*     */ 
/* 329 */       newJetBarb.setPgenCategory(newBarb.getPgenCategory());
/* 330 */       newJetBarb.setPgenType(newBarb.getPgenType());
/*     */ 
/* 332 */       newJetBarb.setVectorType(newBarb.getVectorType());
/*     */ 
/* 334 */       return newJetBarb;
/*     */     }
/*     */ 
/*     */     public Jet.JetText getFlightLvl()
/*     */     {
/* 344 */       DECollection windInfo = (DECollection)this.parent;
/* 345 */       Iterator it = windInfo.getComponentIterator();
/*     */ 
/* 347 */       Jet.JetText jt = null;
/* 348 */       while (it.hasNext()) {
/* 349 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 350 */         if ((adc instanceof Jet.JetText)) {
/* 351 */           jt = (Jet.JetText)adc;
/*     */         }
/*     */       }
/*     */ 
/* 355 */       return jt;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class JetHash extends Vector
/*     */   {
/*     */     private JetHash()
/*     */     {
/* 369 */       setParent(Jet.this);
/*     */     }
/*     */ 
/*     */     public JetHash(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, Boolean clear, Coordinate location, IVector.VectorType vc, double speed, double direction, double arrowHeadSize, boolean directionOnly, String pgenCategory, String pgenType)
/*     */     {
/* 395 */       super(colors, lineWidth, sizeScale, clear, location, vc, speed, 
/* 395 */         direction, arrowHeadSize, directionOnly, pgenCategory, pgenType);
/*     */ 
/* 397 */       setParent(Jet.this);
/*     */     }
/*     */ 
/*     */     public void setLocation(Coordinate location)
/*     */     {
/* 407 */       if (((Jet)this.parent).search(this) != null)
/*     */       {
/* 409 */         if (Jet.this.snapTool != null) {
/* 410 */           Jet.this.snapTool.snapHash(this, location, (Jet)this.parent);
/*     */         }
/*     */         else
/* 413 */           super.setLocation(location);
/*     */       }
/*     */     }
/*     */ 
/*     */     public JetHash copy()
/*     */     {
/* 424 */       Vector newHash = (Vector)super.copy();
/* 425 */       JetHash newJetHash = new JetHash(Jet.this);
/*     */ 
/* 427 */       newJetHash.setParent(this.parent);
/*     */ 
/* 429 */       newJetHash.update(newHash);
/* 430 */       newJetHash.setLocationOnly(newHash.getLocation());
/*     */ 
/* 432 */       newJetHash.setPgenCategory(newHash.getPgenCategory());
/* 433 */       newJetHash.setPgenType(newHash.getPgenType());
/*     */ 
/* 435 */       newJetHash.setVectorType(newHash.getVectorType());
/*     */ 
/* 437 */       return newJetHash;
/*     */     }
/*     */   }
/*     */ 
/*     */   @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.CONNECT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_PART, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.MODIFY})
/*     */   public class JetLine extends Line
/*     */   {
/*     */     private JetLine()
/*     */     {
/* 195 */       setPgenCategory("Lines");
/* 196 */       setPgenType("FILLED_ARROW");
/* 197 */       setParent(Jet.this);
/*     */     }
/*     */ 
/*     */     public JetLine(Color[] range, float colors, double lineWidth, boolean arg6, boolean closed, ArrayList<Coordinate> filled, int linePoints, FillPatternList.FillPattern smoothFactor, String fillPattern, String pgenCategory)
/*     */     {
/* 206 */       super(colors, lineWidth, sizeScale, closed, filled, 
/* 206 */         linePoints, smoothFactor, fillPattern, pgenCategory, pgenType);
/*     */     }
/*     */ 
/*     */     public void setPoints(ArrayList<Coordinate> pts)
/*     */     {
/* 214 */       setPointsOnly(pts);
/* 215 */       if (Jet.this.snapTool != null)
/* 216 */         Jet.this.snapTool.snapJet((Jet)this.parent);
/*     */     }
/*     */ 
/*     */     public JetLine copy()
/*     */     {
/* 226 */       Line newLine = (Line)super.copy();
/* 227 */       JetLine newJetLine = new JetLine(Jet.this);
/* 228 */       newJetLine.setParent(this.parent);
/*     */ 
/* 230 */       newJetLine.update(newLine);
/* 231 */       newJetLine.setLinePoints(newLine.getPoints());
/*     */ 
/* 233 */       return newJetLine;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class JetText extends Text
/*     */   {
/*     */     private boolean latLonFlag;
/*     */ 
/*     */     public JetText()
/*     */     {
/*     */     }
/*     */ 
/*     */     public JetText(Coordinate[] range, String fontName, float fontSize, IText.TextJustification justification, Coordinate position, double rotation, IText.TextRotation rotationRelativity, String[] text, IText.FontStyle style, Color textColor, int offset, int offset2, boolean mask, IText.DisplayType outline, String pgenCategory, String pgenType)
/*     */     {
/* 476 */       super(fontName, fontSize, justification, position, 
/* 475 */         rotation, rotationRelativity, text, style, textColor, 
/* 476 */         offset, offset2, mask, outline, pgenCategory, pgenType);
/*     */     }
/*     */ 
/*     */     public void setLocationOnly(Coordinate loc)
/*     */     {
/* 484 */       if (Jet.this.snapTool != null) {
/* 485 */         super.setLocationOnly(Jet.this.snapTool.latLon2Relative(loc, (Vector)getParent().getPrimaryDE()));
/*     */       }
/*     */       else
/* 488 */         super.setLocationOnly(loc);
/*     */     }
/*     */ 
/*     */     public Coordinate getLocation()
/*     */     {
/* 498 */       if (Jet.this.snapTool != null) {
/* 499 */         return Jet.this.snapTool.relative2LatLon(super.getLocation(), (Vector)getParent().getPrimaryDE());
/*     */       }
/*     */ 
/* 502 */       return super.getLocation();
/*     */     }
/*     */ 
/*     */     private Coordinate getLoc()
/*     */     {
/* 511 */       return this.location;
/*     */     }
/*     */ 
/*     */     public JetText copy()
/*     */     {
/* 520 */       JetText newText = new JetText(Jet.this);
/* 521 */       newText.update(this);
/* 522 */       newText.setParent(getParent());
/*     */ 
/* 528 */       newText.setColors(new Color[] { new Color(getColors()[0].getRed(), 
/* 529 */         getColors()[0].getGreen(), 
/* 530 */         getColors()[0].getBlue()) });
/* 531 */       newText.setLocation(new Coordinate(getLocation()));
/* 532 */       newText.setFontName(new String(getFontName()));
/*     */ 
/* 538 */       String[] textCopy = new String[getText().length];
/* 539 */       for (int i = 0; i < getText().length; i++) {
/* 540 */         textCopy[i] = new String(getText()[i]);
/*     */       }
/* 542 */       newText.setText(textCopy);
/*     */ 
/* 544 */       newText.setPgenCategory(new String(getPgenCategory()));
/* 545 */       newText.setPgenType(new String(getPgenType()));
/*     */ 
/* 548 */       return newText;
/*     */     }
/*     */ 
/*     */     public void setLatLonFlag(boolean latLonFlag)
/*     */     {
/* 555 */       this.latLonFlag = latLonFlag;
/*     */     }
/*     */ 
/*     */     public boolean getLatLonFlag()
/*     */     {
/* 562 */       return this.latLonFlag;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Jet
 * JD-Core Version:    0.6.2
 */