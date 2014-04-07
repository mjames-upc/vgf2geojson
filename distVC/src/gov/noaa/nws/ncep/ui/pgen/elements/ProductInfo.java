/*    */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class ProductInfo
/*    */ {
/*    */   private HashMap<String, String> properties;
/*    */ 
/*    */   public ProductInfo()
/*    */   {
/* 29 */     this.properties = new HashMap();
/*    */   }
/*    */ 
/*    */   public ProductInfo(HashMap<String, String> props) {
/* 33 */     this.properties = props;
/*    */   }
/*    */ 
/*    */   public Object getProperty(String propertyName) {
/* 37 */     return this.properties.get(propertyName);
/*    */   }
/*    */ 
/*    */   public HashMap<String, String> getProperties() {
/* 41 */     return this.properties;
/*    */   }
/*    */ 
/*    */   public void setProperty(String name, String value) {
/* 45 */     this.properties.put(name, value);
/*    */   }
/*    */ 
/*    */   public void setProperties(HashMap<String, String> props) {
/* 49 */     this.properties = new HashMap(props);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.ProductInfo
 * JD-Core Version:    0.6.2
 */