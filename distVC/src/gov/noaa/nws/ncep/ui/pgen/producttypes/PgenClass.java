/*    */ package gov.noaa.nws.ncep.ui.pgen.producttypes;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlType(name="", propOrder={"pgenObjects"})
/*    */ @XmlRootElement(name="PgenClass")
/*    */ public class PgenClass
/*    */ {
/*    */ 
/*    */   @XmlElement(name="PgenObjects")
/*    */   protected PgenObjects pgenObjects;
/*    */ 
/*    */   @XmlAttribute(name="Name")
/*    */   protected String name;
/*    */ 
/*    */   public PgenObjects getPgenObjects()
/*    */   {
/* 60 */     return this.pgenObjects;
/*    */   }
/*    */ 
/*    */   public void setPgenObjects(PgenObjects value)
/*    */   {
/* 72 */     this.pgenObjects = value;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 84 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String value)
/*    */   {
/* 96 */     this.name = value;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.producttypes.PgenClass
 * JD-Core Version:    0.6.2
 */