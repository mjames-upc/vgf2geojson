/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.DrawableImage;
/*     */ import com.raytheon.uf.viz.core.IExtent;
/*     */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*     */ import com.raytheon.uf.viz.core.IView;
/*     */ import com.raytheon.uf.viz.core.PixelCoverage;
/*     */ import com.raytheon.uf.viz.core.drawables.IImage;
/*     */ import com.raytheon.uf.viz.core.drawables.PaintProperties;
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ 
/*     */ public class SymbolSetElement
/*     */   implements IDisplayable
/*     */ {
/*     */   private final IImage raster;
/*     */   private final double[][] locations;
/*     */ 
/*     */   public SymbolSetElement(IImage raster, double[][] locations)
/*     */   {
/*  63 */     this.raster = raster;
/*  64 */     this.locations = locations;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/*  75 */     this.raster.dispose();
/*     */   }
/*     */ 
/*     */   public void draw(IGraphicsTarget target, PaintProperties paintProps)
/*     */   {
/*  87 */     double[] loc = new double[3];
/*     */ 
/*  92 */     double screenToWorldRatio = paintProps.getCanvasBounds().width / 
/*  93 */       paintProps.getView().getExtent().getWidth();
/*  94 */     double scale = 1.0D / screenToWorldRatio;
/*     */ 
/*  99 */     List images = new ArrayList();
/* 100 */     for (int j = 0; j < this.locations.length; j++) {
/* 101 */       loc = this.locations[j];
/* 102 */       PixelCoverage extent = new PixelCoverage(new Coordinate(loc[0], 
/* 103 */         loc[1]), this.raster.getWidth() * scale, this.raster.getHeight() * 
/* 104 */         scale);
/* 105 */       images.add(new DrawableImage(this.raster, extent));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 110 */       target.drawRasters(paintProps, (DrawableImage[])images.toArray(new DrawableImage[0]));
/*     */     } catch (VizException e) {
/* 112 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.SymbolSetElement
 * JD-Core Version:    0.6.2
 */