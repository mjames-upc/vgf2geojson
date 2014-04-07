/*     */ package gov.noaa.nws.ncep.ui.pgen.rsc;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*     */ import com.raytheon.uf.viz.core.drawables.PaintProperties;
/*     */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.AbstractElementContainer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.DefaultElementContainer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.DisplayElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class PgenResourceGhost
/*     */ {
/*     */   public AbstractDrawableComponent component;
/*  56 */   Map<Object, AbstractElementContainer> componentMap = new HashMap();
/*     */ 
/*     */   public void draw(IGraphicsTarget target, PaintProperties paintProps, DisplayElementFactory df, IMapDescriptor descriptor)
/*     */   {
/*  67 */     df.setLayerDisplayAttr(Boolean.valueOf(false), null, Boolean.valueOf(false));
/*  68 */     if (this.component != null) {
/*  69 */       Iterator iterator = this.component
/*  70 */         .createDEIterator();
/*  71 */       int count = 0;
/*  72 */       while (iterator.hasNext()) {
/*  73 */         DrawableElement element = (DrawableElement)iterator.next();
/*  74 */         drawElement(target, paintProps, df, element, descriptor);
/*  75 */         count++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawElement(IGraphicsTarget target, PaintProperties paintProps, DisplayElementFactory df, DrawableElement el, IMapDescriptor descriptor)
/*     */   {
/*  91 */     Object key = createKey(el);
/*  92 */     AbstractElementContainer graphic = (AbstractElementContainer)this.componentMap.get(key);
/*     */ 
/*  94 */     if (graphic == null) {
/*  95 */       graphic = new DefaultElementContainer(el, descriptor, target);
/*  96 */       this.componentMap.put(key, graphic);
/*     */     } else {
/*  98 */       graphic.setElement(el);
/*     */     }
/* 100 */     graphic.draw(target, paintProps, null, true);
/*     */   }
/*     */ 
/*     */   private Object createKey(DrawableElement el) {
/* 104 */     return el.getPgenCategory() + ":" + el.getPgenType();
/*     */   }
/*     */ 
/*     */   public void setGhostLine(AbstractDrawableComponent ghost)
/*     */   {
/* 112 */     this.component = ghost;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.rsc.PgenResourceGhost
 * JD-Core Version:    0.6.2
 */