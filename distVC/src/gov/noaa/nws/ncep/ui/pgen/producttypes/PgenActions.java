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
/*    */ @XmlType(name="", propOrder={"name"})
/*    */ @XmlRootElement(name="PgenActions")
/*    */ public class PgenActions
/*    */ {
/*    */ 
/*    */   @XmlElement(name="Name")
/*    */   protected List<String> name;
/*    */ 
/*    */   public List<String> getName()
/*    */   {
/* 72 */     if (this.name == null) {
/* 73 */       this.name = new ArrayList();
/*    */     }
/* 75 */     return this.name;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.producttypes.PgenActions
 * JD-Core Version:    0.6.2
 */