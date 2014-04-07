/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.DrawableString;
/*     */ import com.raytheon.uf.viz.core.IExtent;
/*     */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*     */ import com.raytheon.uf.viz.core.drawables.IFont;
/*     */ import com.raytheon.uf.viz.core.drawables.PaintProperties;
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.color.BackgroundColor;
/*     */ import com.raytheon.viz.ui.color.IBackgroundColorChangedListener.BGColorMode;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ 
/*     */ public class TextDisplayElement
/*     */   implements IDisplayable
/*     */ {
/*     */   private boolean mask;
/*     */   private IExtent box;
/*     */   private IText.DisplayType displayType;
/*     */   private DrawableString dstring;
/*     */ 
/*     */   public TextDisplayElement(DrawableString dstring, boolean mask, IText.DisplayType dType, IExtent box)
/*     */   {
/*  81 */     this.mask = mask;
/*  82 */     this.box = box;
/*     */ 
/*  85 */     this.displayType = dType;
/*  86 */     this.dstring = dstring;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 123 */     this.dstring.font.dispose();
/*     */   }
/*     */ 
/*     */   public void draw(IGraphicsTarget target, PaintProperties paintProps)
/*     */   {
/*     */     try
/*     */     {
/* 138 */       if (this.mask) {
/* 139 */         RGB bg = BackgroundColor.getActivePerspectiveInstance().getColor(IBackgroundColorChangedListener.BGColorMode.EDITOR);
/* 140 */         target.drawShadedRect(this.box, bg, 1.0D, null);
/*     */       }
/*     */ 
/* 143 */       switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$display$IText$DisplayType()[this.displayType.ordinal()])
/*     */       {
/*     */       case 2:
/* 146 */         target.drawRect(this.box, this.dstring.getColors()[0], 1.0F, 1.0D);
/* 147 */         break;
/*     */       case 4:
/* 150 */         target.drawLine(this.box.getMinX(), this.box.getMinY(), 0.0D, this.box.getMaxX(), this.box.getMinY(), 
/* 151 */           0.0D, this.dstring.getColors()[0], 1.0F);
/* 152 */         break;
/*     */       case 3:
/* 155 */         target.drawLine(this.box.getMinX(), this.box.getMaxY(), 0.0D, this.box.getMaxX(), this.box.getMaxY(), 
/* 156 */           0.0D, this.dstring.getColors()[0], 1.0F);
/*     */       }
/*     */ 
/* 161 */       target.drawStrings(new DrawableString[] { this.dstring });
/*     */     }
/*     */     catch (VizException ve)
/*     */     {
/* 176 */       ve.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.TextDisplayElement
 * JD-Core Version:    0.6.2
 */