/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*     */ import com.raytheon.uf.viz.core.drawables.PaintProperties;
/*     */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.ITcm;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.IGfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.ISigmet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.ITca;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class AbstractElementContainer
/*     */ {
/*     */   protected DrawableElement element;
/*     */   protected IMapDescriptor mapDescriptor;
/*     */   protected IGraphicsTarget target;
/*     */   protected DisplayElementFactory def;
/*  58 */   protected List<IDisplayable> displayEls = null;
/*     */ 
/*     */   public AbstractElementContainer(DrawableElement element, IMapDescriptor mapDescriptor, IGraphicsTarget target)
/*     */   {
/*  65 */     this.element = element;
/*  66 */     this.mapDescriptor = mapDescriptor;
/*  67 */     this.target = target;
/*  68 */     this.def = new DisplayElementFactory(target, mapDescriptor);
/*     */   }
/*     */ 
/*     */   public void setMapDescriptor(IMapDescriptor mapDescriptor)
/*     */   {
/*  76 */     this.mapDescriptor = mapDescriptor;
/*  77 */     this.def = new DisplayElementFactory(this.target, mapDescriptor);
/*  78 */     dispose();
/*  79 */     this.displayEls = null;
/*     */   }
/*     */ 
/*     */   public abstract void draw(IGraphicsTarget paramIGraphicsTarget, PaintProperties paramPaintProperties, DisplayProperties paramDisplayProperties);
/*     */ 
/*     */   public abstract void draw(IGraphicsTarget paramIGraphicsTarget, PaintProperties paramPaintProperties, DisplayProperties paramDisplayProperties, boolean paramBoolean);
/*     */ 
/*     */   protected void createDisplayables(PaintProperties paintProps)
/*     */   {
/* 110 */     if ((this.displayEls != null) && (!this.displayEls.isEmpty())) reset();
/* 111 */     if ((this.element instanceof IAvnText)) {
/* 112 */       this.displayEls = this.def.createDisplayElements((IAvnText)this.element, paintProps);
/*     */     }
/* 114 */     else if ((this.element instanceof IMidCloudText)) {
/* 115 */       this.displayEls = this.def.createDisplayElements((IMidCloudText)this.element, paintProps);
/*     */     }
/* 117 */     else if ((this.element instanceof IText)) {
/* 118 */       this.displayEls = this.def.createDisplayElements((IText)this.element, paintProps);
/*     */     }
/* 120 */     else if ((this.element instanceof IVector)) {
/* 121 */       this.displayEls = this.def.createDisplayElements((IVector)this.element, paintProps);
/*     */     }
/* 123 */     else if ((this.element instanceof ICombo)) {
/* 124 */       this.displayEls = this.def.createDisplayElements((ICombo)this.element, paintProps);
/*     */     }
/* 126 */     else if ((this.element instanceof ITca)) {
/* 127 */       this.displayEls = this.def.createDisplayElements((ITca)this.element, paintProps);
/*     */     }
/* 129 */     else if ((this.element instanceof ISigmet)) {
/* 130 */       this.displayEls = this.def.createDisplayElements((ISigmet)this.element, paintProps);
/*     */     }
/* 132 */     else if ((this.element instanceof ISymbol)) {
/* 133 */       this.displayEls = this.def.createDisplayElements((ISymbol)this.element, paintProps);
/*     */     }
/* 135 */     else if ((this.element instanceof ITrack)) {
/* 136 */       this.displayEls = this.def.createDisplayElements((ITrack)this.element, paintProps);
/*     */     }
/* 138 */     else if ((this.element instanceof IWatchBox)) {
/* 139 */       this.displayEls = this.def.createDisplayElements((IWatchBox)this.element, paintProps);
/*     */     }
/* 141 */     else if ((this.element instanceof ITcm)) {
/* 142 */       this.displayEls = this.def.createDisplayElements((ITcm)this.element, paintProps);
/*     */     }
/* 144 */     else if ((this.element instanceof IMultiPoint))
/* 145 */       if ((this.element instanceof IKink)) {
/* 146 */         this.displayEls = this.def.createDisplayElements((IKink)this.element, paintProps);
/*     */       }
/* 148 */       else if ((this.element instanceof IArc)) {
/* 149 */         this.displayEls = this.def.createDisplayElements((IArc)this.element, paintProps);
/*     */       }
/* 151 */       else if ((this.element instanceof IGfa)) {
/* 152 */         this.displayEls = this.def.createDisplayElements((IGfa)this.element, paintProps);
/*     */       }
/* 154 */       else if ((this.element instanceof ILine))
/* 155 */         this.displayEls = this.def.createDisplayElements((ILine)this.element, paintProps, true);
/*     */   }
/*     */ 
/*     */   private void reset()
/*     */   {
/* 161 */     this.def.reset();
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 169 */     if (this.displayEls == null) return;
/*     */ 
/* 171 */     for (IDisplayable each : this.displayEls) {
/* 172 */       each.dispose();
/*     */     }
/* 174 */     this.displayEls.clear();
/*     */   }
/*     */ 
/*     */   public void setElement(DrawableElement el) {
/* 178 */     this.element = el;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.AbstractElementContainer
 * JD-Core Version:    0.6.2
 */