/*    */ package gov.noaa.nws.ncep.ui.pgen.file;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlType(name="", propOrder={"product"})
/*    */ @XmlRootElement(name="Products")
/*    */ public class Products
/*    */ {
/*    */ 
/*    */   @XmlElement(name="Product", required=true)
/*    */   protected List<Product> product;
/*    */ 
/*    */   public List<Product> getProduct()
/*    */   {
/* 72 */     if (this.product == null) {
/* 73 */       this.product = new ArrayList();
/*    */     }
/* 75 */     return this.product;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.Products
 * JD-Core Version:    0.6.2
 */