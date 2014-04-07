/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PGenException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DeleteSelectedElementsCommand extends PgenCommand
/*     */ {
/*     */   private List<Product> prodList;
/*     */   private List<AbstractDrawableComponent> elSelected;
/*     */   private List<AbstractDrawableComponent> saveEl;
/*     */   private HashMap<AbstractDrawableComponent, DECollection> elMap;
/*     */ 
/*     */   public DeleteSelectedElementsCommand(List<Product> list, List<AbstractDrawableComponent> elements)
/*     */   {
/*  67 */     this.prodList = list;
/*  68 */     this.elSelected = elements;
/*  69 */     this.saveEl = new ArrayList(elements);
/*  70 */     this.elMap = new HashMap();
/*     */   }
/*     */ 
/*     */   public void execute()
/*     */     throws PGenException
/*     */   {
/*     */     Iterator localIterator2;
/*  81 */     for (Iterator localIterator1 = this.saveEl.iterator(); localIterator1.hasNext(); 
/*  83 */       localIterator2.hasNext())
/*     */     {
/*  81 */       AbstractDrawableComponent comp = (AbstractDrawableComponent)localIterator1.next();
/*     */ 
/*  83 */       localIterator2 = this.prodList.iterator(); continue; Product prod = (Product)localIterator2.next();
/*     */ 
/*  85 */       for (Layer layer : prod.getLayers())
/*     */       {
/*  87 */         DECollection dec = layer.search(comp);
/*  88 */         if (dec != null)
/*     */         {
/*  90 */           dec.removeElement(comp);
/*  91 */           this.elMap.put(comp, dec);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  97 */     this.elSelected.clear();
/*     */   }
/*     */ 
/*     */   public void undo()
/*     */     throws PGenException
/*     */   {
/* 107 */     for (AbstractDrawableComponent comp : this.saveEl)
/*     */     {
/* 109 */       DECollection dec = (DECollection)this.elMap.get(comp);
/*     */ 
/* 111 */       if (dec != null) {
/* 112 */         dec.addElement(comp);
/*     */       }
/*     */       else
/* 115 */         throw new PGenException("Coulnd't find the collection when restoring objects!");
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.DeleteSelectedElementsCommand
 * JD-Core Version:    0.6.2
 */