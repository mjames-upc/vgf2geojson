/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PGenException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ReplaceElementsCommand extends PgenCommand
/*     */ {
/*     */   private DECollection parent;
/*  48 */   private List<AbstractDrawableComponent> oldElements = null;
/*     */ 
/*  53 */   private List<AbstractDrawableComponent> newElements = null;
/*     */ 
/*     */   public ReplaceElementsCommand(DECollection parent, AbstractDrawableComponent oldElement, AbstractDrawableComponent newElement)
/*     */   {
/*  64 */     this.parent = parent;
/*     */ 
/*  66 */     if (this.oldElements == null) {
/*  67 */       this.oldElements = new ArrayList();
/*     */     }
/*     */     else {
/*  70 */       this.oldElements.clear();
/*     */     }
/*     */ 
/*  73 */     this.oldElements.add(oldElement);
/*     */ 
/*  75 */     if (this.newElements == null) {
/*  76 */       this.newElements = new ArrayList();
/*     */     }
/*     */     else {
/*  79 */       this.newElements.clear();
/*     */     }
/*     */ 
/*  82 */     this.newElements.add(newElement);
/*     */   }
/*     */ 
/*     */   public ReplaceElementsCommand(DECollection parent, List<AbstractDrawableComponent> oldElements, List<AbstractDrawableComponent> newElements)
/*     */   {
/*  95 */     this.parent = parent;
/*  96 */     this.oldElements = oldElements;
/*  97 */     this.newElements = newElements;
/*     */   }
/*     */ 
/*     */   public void execute()
/*     */     throws PGenException
/*     */   {
/* 108 */     if (this.parent != null) {
/* 109 */       if (this.oldElements != null) {
/* 110 */         for (AbstractDrawableComponent ade : this.oldElements) {
/* 111 */           this.parent.removeElement(ade);
/*     */         }
/*     */       }
/*     */ 
/* 115 */       if (this.newElements != null) {
/* 116 */         this.parent.add(this.newElements);
/*     */       }
/*     */     }
/* 119 */     else if (this.oldElements.size() == this.newElements.size()) {
/* 120 */       for (int ii = 0; ii < this.oldElements.size(); ii++) {
/* 121 */         AbstractDrawableComponent ade = (AbstractDrawableComponent)this.oldElements.get(ii);
/* 122 */         if ((ade.getParent() != null) && ((ade.getParent() instanceof DECollection))) {
/* 123 */           DECollection dec = (DECollection)ade.getParent();
/* 124 */           dec.removeElement(ade);
/* 125 */           dec.add((AbstractDrawableComponent)this.newElements.get(ii));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void undo()
/*     */     throws PGenException
/*     */   {
/* 138 */     if (this.parent != null) {
/* 139 */       if (this.newElements != null) {
/* 140 */         for (AbstractDrawableComponent ade : this.newElements) {
/* 141 */           this.parent.removeElement(ade);
/*     */         }
/*     */       }
/*     */ 
/* 145 */       if (this.oldElements != null) {
/* 146 */         this.parent.add(this.oldElements);
/*     */       }
/*     */     }
/* 149 */     else if (this.oldElements.size() == this.newElements.size()) {
/* 150 */       for (int ii = 0; ii < this.newElements.size(); ii++) {
/* 151 */         AbstractDrawableComponent ade = (AbstractDrawableComponent)this.newElements.get(ii);
/* 152 */         if ((ade.getParent() != null) && ((ade.getParent() instanceof DECollection))) {
/* 153 */           DECollection dec = (DECollection)ade.getParent();
/* 154 */           dec.removeElement(ade);
/* 155 */           dec.add((AbstractDrawableComponent)this.oldElements.get(ii));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.ReplaceElementsCommand
 * JD-Core Version:    0.6.2
 */