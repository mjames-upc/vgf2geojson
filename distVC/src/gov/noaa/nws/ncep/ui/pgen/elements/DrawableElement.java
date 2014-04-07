/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public abstract class DrawableElement extends AbstractDrawableComponent
/*     */   implements IAttribute
/*     */ {
/*     */   Coordinate[] range;
/*     */ 
/*     */   protected DrawableElement()
/*     */   {
/*  51 */     this.range = null;
/*     */   }
/*     */ 
/*     */   protected DrawableElement(Coordinate[] range, String pgenCategory, String pgenType)
/*     */   {
/*  60 */     this.range = range;
/*  61 */     this.pgenCategory = pgenCategory;
/*  62 */     this.pgenType = pgenType;
/*     */   }
/*     */ 
/*     */   public Coordinate[] getRange()
/*     */   {
/*  71 */     return this.range;
/*     */   }
/*     */ 
/*     */   public void setRange(Coordinate[] range)
/*     */   {
/*  78 */     this.range = range;
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/*  94 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public double getSizeScale()
/*     */   {
/* 102 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public abstract void update(IAttribute paramIAttribute);
/*     */ 
/*     */   public void setPoints(ArrayList<Coordinate> pts)
/*     */   {
/* 115 */     setPointsOnly(pts);
/*     */   }
/*     */ 
/*     */   public abstract void setPointsOnly(ArrayList<Coordinate> paramArrayList);
/*     */ 
/*     */   public Iterator<DrawableElement> createDEIterator()
/*     */   {
/* 125 */     return new SelfIterator(this);
/*     */   }
/*     */ 
/*     */   public DrawableElement getPrimaryDE()
/*     */   {
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 139 */     return getPgenType();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement
 * JD-Core Version:    0.6.2
 */