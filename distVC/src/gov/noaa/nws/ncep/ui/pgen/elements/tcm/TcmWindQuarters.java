/*    */ package gov.noaa.nws.ncep.ui.pgen.elements.tcm;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*    */ import com.vividsolutions.jts.geom.Coordinate;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlElements;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class TcmWindQuarters extends SinglePointElement
/*    */   implements ITcmWindQuarter, ISerializableObject
/*    */ {
/*    */ 
/*    */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="quatro", type=java.lang.Double.class)})
/*    */   private double[] quatro;
/*    */ 
/*    */   @XmlAttribute
/*    */   private int windSpeed;
/*    */ 
/*    */   public TcmWindQuarters()
/*    */   {
/*    */   }
/*    */ 
/*    */   public TcmWindQuarters(Coordinate loc, int spd, double q1, double q2, double q3, double q4)
/*    */   {
/* 51 */     this.quatro = new double[4];
/* 52 */     this.quatro[0] = q1;
/* 53 */     this.quatro[1] = q2;
/* 54 */     this.quatro[2] = q3;
/* 55 */     this.quatro[3] = q4;
/* 56 */     setLocation(loc);
/* 57 */     this.windSpeed = spd;
/*    */   }
/*    */ 
/*    */   public AbstractDrawableComponent copy()
/*    */   {
/* 63 */     TcmWindQuarters newQuatros = 
/* 64 */       new TcmWindQuarters(new Coordinate(getLocation().x, getLocation().y), 
/* 65 */       getWindSpeed(), 
/* 66 */       this.quatro[0], this.quatro[1], this.quatro[2], this.quatro[3]);
/*    */ 
/* 68 */     return newQuatros;
/*    */   }
/*    */ 
/*    */   public double[] getQuarters() {
/* 72 */     return this.quatro;
/*    */   }
/*    */ 
/*    */   public void setWindSpeed(int windSpeed) {
/* 76 */     this.windSpeed = windSpeed;
/*    */   }
/*    */ 
/*    */   public int getWindSpeed() {
/* 80 */     return this.windSpeed;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.tcm.TcmWindQuarters
 * JD-Core Version:    0.6.2
 */