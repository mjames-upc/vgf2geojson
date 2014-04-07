/*    */ package gov.noaa.nws.ncep.ui.pgen.producttypes;
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
/*    */ @XmlType(name="", propOrder={"productType"})
/*    */ @XmlRootElement(name="ProductTypes")
/*    */ public class ProductTypes
/*    */ {
/*    */ 
/*    */   @XmlElement(name="ProductType")
/*    */   protected List<ProductType> productType;
/*    */ 
/*    */   public List<ProductType> getProductType()
/*    */   {
/* 72 */     if (this.productType == null) {
/* 73 */       this.productType = new ArrayList();
/*    */     }
/* 75 */     return this.productType;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.producttypes.ProductTypes
 * JD-Core Version:    0.6.2
 */