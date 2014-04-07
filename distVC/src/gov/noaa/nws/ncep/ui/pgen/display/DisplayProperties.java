/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import java.awt.Color;
/*     */ 
/*     */ public class DisplayProperties
/*     */ {
/*  24 */   private Boolean layerMonoColor = Boolean.valueOf(false);
/*  25 */   private Color layerColor = null;
/*  26 */   private Boolean layerFilled = Boolean.valueOf(false);
/*     */ 
/*     */   public DisplayProperties()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DisplayProperties(Boolean layerMonoColor, Color layerColor, Boolean layerFilled)
/*     */   {
/*  42 */     this.layerMonoColor = layerMonoColor;
/*  43 */     this.layerColor = layerColor;
/*  44 */     this.layerFilled = layerFilled;
/*     */   }
/*     */ 
/*     */   public Boolean getLayerMonoColor()
/*     */   {
/*  50 */     return this.layerMonoColor;
/*     */   }
/*     */ 
/*     */   public void setLayerMonoColor(Boolean layerMonoColor)
/*     */   {
/*  56 */     this.layerMonoColor = layerMonoColor;
/*     */   }
/*     */ 
/*     */   public Color getLayerColor()
/*     */   {
/*  62 */     return this.layerColor;
/*     */   }
/*     */ 
/*     */   public void setLayerColor(Color layerColor)
/*     */   {
/*  68 */     this.layerColor = layerColor;
/*     */   }
/*     */ 
/*     */   public Boolean getLayerFilled()
/*     */   {
/*  74 */     return this.layerFilled;
/*     */   }
/*     */ 
/*     */   public void setLayerFilled(Boolean layerFilled)
/*     */   {
/*  80 */     this.layerFilled = layerFilled;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  91 */     if (obj == null) return false;
/*  92 */     if (!(obj instanceof DisplayProperties)) return false;
/*  93 */     DisplayProperties dp = (DisplayProperties)obj;
/*     */     boolean sameColor;
/*     */     boolean sameColor;
/*  96 */     if ((this.layerColor == null) && (dp.getLayerColor() == null)) {
/*  97 */       sameColor = true;
/*     */     }
/*     */     else
/*     */     {
/*     */       boolean sameColor;
/*  98 */       if ((this.layerColor == null) || (dp.getLayerColor() == null))
/*  99 */         sameColor = false;
/*     */       else
/* 101 */         sameColor = this.layerColor.equals(dp.getLayerColor());
/*     */     }
/* 103 */     if ((sameColor) && 
/* 104 */       (this.layerMonoColor.equals(dp.getLayerMonoColor())) && 
/* 105 */       (this.layerFilled.equals(dp.getLayerFilled()))) return true;
/*     */ 
/* 107 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.DisplayProperties
 * JD-Core Version:    0.6.2
 */