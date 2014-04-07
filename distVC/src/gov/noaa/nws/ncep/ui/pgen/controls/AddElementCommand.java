/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PGenException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import java.util.List;
/*     */ 
/*     */ public class AddElementCommand extends PgenCommand
/*     */ {
/*     */   private List<Product> list;
/*     */   private Product product;
/*     */   private Layer layer;
/*     */   private AbstractDrawableComponent element;
/*     */   private boolean layerExisted;
/*     */   private boolean productExisted;
/*     */ 
/*     */   public AddElementCommand(List<Product> list, Product product, Layer layer, AbstractDrawableComponent element)
/*     */   {
/*  65 */     this.list = list;
/*  66 */     this.product = product;
/*  67 */     this.layer = layer;
/*  68 */     this.element = element;
/*     */   }
/*     */ 
/*     */   public void execute()
/*     */     throws PGenException
/*     */   {
/*  83 */     if (this.list == null) throw new PGenException("AddElementCommand must be given a non null Product List");
/*  84 */     if (this.product == null) throw new PGenException("AddElementCommand must be given a non null Product");
/*  85 */     if (this.layer == null) throw new PGenException("AddElementCommand must be given a non null Layer");
/*  86 */     if ((this.element == null) || (this.element.isEmpty())) throw new PGenException("AddElementCommand must be given a non null Element");
/*     */ 
/*  91 */     if (this.list.contains(this.product)) {
/*  92 */       this.productExisted = true;
/*     */     }
/*     */     else {
/*  95 */       this.productExisted = false;
/*  96 */       this.list.add(this.product);
/*     */     }
/*     */ 
/* 102 */     if (this.product.contains(this.layer)) {
/* 103 */       this.layerExisted = true;
/*     */     }
/*     */     else {
/* 106 */       this.layerExisted = false;
/* 107 */       this.product.addLayer(this.layer);
/*     */     }
/*     */ 
/* 113 */     this.layer.addElement(this.element);
/*     */   }
/*     */ 
/*     */   public void undo()
/*     */     throws PGenException
/*     */   {
/* 128 */     this.layer.removeElement(this.element);
/*     */ 
/* 133 */     if ((this.layer.isEmpty()) && (!this.layerExisted)) this.product.removeLayer(this.layer);
/*     */ 
/* 138 */     if ((this.product.isEmpty()) && (!this.productExisted)) this.list.remove(this.product);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.AddElementCommand
 * JD-Core Version:    0.6.2
 */