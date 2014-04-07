/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Stack;
/*     */ 
/*     */ public class DEIterator
/*     */   implements Iterator<DrawableElement>
/*     */ {
/*  32 */   private Stack<ListIterator<AbstractDrawableComponent>> stack = new Stack();
/*     */ 
/*     */   public DEIterator(ListIterator<AbstractDrawableComponent> iterator)
/*     */   {
/*  40 */     this.stack.push(iterator);
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*  46 */     boolean status = false;
/*     */ 
/*  48 */     if (!this.stack.empty())
/*     */     {
/*  50 */       ListIterator iterator = (ListIterator)this.stack.peek();
/*  51 */       if (!iterator.hasNext()) {
/*  52 */         this.stack.pop();
/*  53 */         return hasNext();
/*     */       }
/*     */ 
/*  57 */       int pIdx = iterator.previousIndex();
/*  58 */       while (iterator.hasNext()) {
/*  59 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)iterator.next();
/*  60 */         if ((adc instanceof DrawableElement)) {
/*  61 */           status = true;
/*     */         }
/*  64 */         else if ((adc instanceof DECollection)) {
/*  65 */           status = hasDE((DECollection)adc);
/*  66 */           if (status) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*  71 */       while (iterator.hasPrevious()) {
/*  72 */         if (iterator.previousIndex() == pIdx) break;
/*  73 */         iterator.previous();
/*     */       }
/*     */ 
/*  78 */       if ((!status) && (!this.stack.isEmpty())) {
/*  79 */         this.stack.pop();
/*  80 */         return hasNext();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  86 */     return status;
/*     */   }
/*     */ 
/*     */   public DrawableElement next()
/*     */   {
/*  91 */     if (hasNext()) {
/*  92 */       return (DrawableElement)getNext();
/*     */     }
/*     */ 
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   private AbstractDrawableComponent getNext()
/*     */   {
/* 100 */     Iterator iterator = (Iterator)this.stack.peek();
/* 101 */     AbstractDrawableComponent component = (AbstractDrawableComponent)iterator.next();
/* 102 */     if ((component instanceof DECollection)) {
/* 103 */       if (hasDE((DECollection)component)) {
/* 104 */         this.stack.push(((DECollection)component).getComponentListIterator());
/*     */       }
/*     */ 
/* 108 */       return getNext();
/*     */     }
/*     */ 
/* 111 */     return component;
/*     */   }
/*     */ 
/*     */   public void remove()
/*     */   {
/*     */   }
/*     */ 
/*     */   private boolean hasDE(DECollection dec)
/*     */   {
/* 126 */     boolean status = false;
/* 127 */     Iterator it = dec.getComponentIterator();
/* 128 */     while (it.hasNext()) {
/* 129 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 130 */       if ((adc instanceof DrawableElement)) {
/* 131 */         status = true;
/*     */       }
/* 134 */       else if ((adc instanceof DECollection)) {
/* 135 */         status = hasDE((DECollection)adc);
/* 136 */         if (status)
/*     */           break;
/*     */       }
/*     */     }
/* 140 */     return status;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.DEIterator
 * JD-Core Version:    0.6.2
 */