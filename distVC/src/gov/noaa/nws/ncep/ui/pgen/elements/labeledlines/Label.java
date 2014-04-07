/*     */ package gov.noaa.nws.ncep.ui.pgen.elements.labeledlines;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Label extends DECollection
/*     */ {
/*     */   SinglePointElement spe;
/*     */ 
/*     */   public Label()
/*     */   {
/*  46 */     super("Label");
/*     */   }
/*     */ 
/*     */   public Label(SinglePointElement spe)
/*     */   {
/*  54 */     super("Label");
/*  55 */     this.spe = spe;
/*  56 */     add(spe);
/*     */   }
/*     */ 
/*     */   public SinglePointElement getSpe()
/*     */   {
/*  64 */     return this.spe;
/*     */   }
/*     */ 
/*     */   public void setSpe(SinglePointElement spe)
/*     */   {
/*  72 */     if (spe != null) {
/*  73 */       if (this.spe != null) {
/*  74 */         remove(this.spe);
/*     */       }
/*     */ 
/*  77 */       this.spe = spe;
/*  78 */       add(spe);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addArrow(Line arrow)
/*     */   {
/*  87 */     if (arrow != null) add(arrow);
/*     */   }
/*     */ 
/*     */   public List<Line> getArrows()
/*     */   {
/*  95 */     List arrows = new ArrayList();
/*  96 */     Iterator it = getComponentIterator();
/*  97 */     while (it.hasNext()) {
/*  98 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/*  99 */       if ((adc instanceof Line)) {
/* 100 */         arrows.add((Line)adc);
/*     */       }
/*     */     }
/*     */ 
/* 104 */     return arrows;
/*     */   }
/*     */ 
/*     */   public Label copy()
/*     */   {
/* 111 */     Label lbl = new Label((SinglePointElement)this.spe.copy());
/* 112 */     lbl.setPgenCategory(this.pgenCategory);
/* 113 */     lbl.setPgenType(this.pgenType);
/* 114 */     lbl.setParent(this.parent);
/*     */ 
/* 116 */     for (Line ln : getArrows()) {
/* 117 */       lbl.addArrow((Line)ln.copy());
/*     */     }
/*     */ 
/* 120 */     return lbl;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Label
 * JD-Core Version:    0.6.2
 */