/*    */ package gov.noaa.nws.ncep.ui.pgen.file;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlType(name="ColorType", propOrder={"color"})
/*    */ public class ColorType
/*    */ {
/*    */ 
/*    */   @XmlElement(name="Color", required=true)
/*    */   protected Color color;
/*    */ 
/*    */   public Color getColor()
/*    */   {
/* 53 */     return this.color;
/*    */   }
/*    */ 
/*    */   public void setColor(Color value)
/*    */   {
/* 65 */     this.color = value;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.ColorType
 * JD-Core Version:    0.6.2
 */