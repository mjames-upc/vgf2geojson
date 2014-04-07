/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import java.awt.Color;
/*     */ import java.util.Calendar;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class AbstractDrawableComponent
/*     */ {
/*     */   protected String pgenCategory;
/*     */   protected String pgenType;
/*     */   protected AbstractDrawableComponent parent;
/*     */   protected Calendar startTime;
/*     */   protected Calendar endtime;
/*     */ 
/*     */   public abstract void setColors(Color[] paramArrayOfColor);
/*     */ 
/*     */   public abstract List<Coordinate> getPoints();
/*     */ 
/*     */   public abstract Iterator<DrawableElement> createDEIterator();
/*     */ 
/*     */   public abstract AbstractDrawableComponent copy();
/*     */ 
/*     */   public abstract DrawableElement getPrimaryDE();
/*     */ 
/*     */   public abstract String getName();
/*     */ 
/*     */   public String getPgenCategory()
/*     */   {
/*  55 */     return this.pgenCategory;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String pgenCategory)
/*     */   {
/*  62 */     this.pgenCategory = pgenCategory;
/*     */   }
/*     */ 
/*     */   public String getPgenType()
/*     */   {
/*  69 */     return this.pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenType(String pgenType)
/*     */   {
/*  76 */     this.pgenType = pgenType;
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent getParent() {
/*  80 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setParent(AbstractDrawableComponent parent) {
/*  84 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   public String getForecastHours() {
/*  92 */     return "";
/*     */   }
/*     */   public Calendar getStartTime() {
/*  95 */     return this.startTime;
/*     */   }
/*     */   public void setStartTime(Calendar startTime) {
/*  98 */     this.startTime = startTime;
/*     */   }
/*     */   public Calendar getEndtime() {
/* 101 */     return this.endtime;
/*     */   }
/*     */   public void setEndtime(Calendar endtime) {
/* 104 */     this.endtime = endtime;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent
 * JD-Core Version:    0.6.2
 */