/*    */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ @XmlRootElement(name="waterBreakpoints")
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class WaterBreakpointList
/*    */   implements ISerializableObject
/*    */ {
/*    */ 
/*    */   @XmlElement(name="waterway")
/*    */   private List<WaterBreakpoint> breakpoints;
/*    */ 
/*    */   public List<WaterBreakpoint> getWaterways()
/*    */   {
/* 36 */     return this.breakpoints;
/*    */   }
/*    */ 
/*    */   public void setWaterways(List<WaterBreakpoint> breakpoints)
/*    */   {
/* 43 */     this.breakpoints = breakpoints;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.WaterBreakpointList
 * JD-Core Version:    0.6.2
 */