/*    */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElements;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ @XmlRootElement(name="coastBreakpoints")
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class CoastBreakpointList
/*    */   implements ISerializableObject
/*    */ {
/*    */ 
/*    */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="coast")})
/*    */   private List<CoastBreakpoint> coasts;
/*    */ 
/*    */   public List<CoastBreakpoint> getCoasts()
/*    */   {
/* 38 */     return this.coasts;
/*    */   }
/*    */ 
/*    */   public void setCoasts(List<CoastBreakpoint> coasts)
/*    */   {
/* 45 */     this.coasts = coasts;
/*    */   }
/*    */ 
/*    */   public List<BreakpointSegment> getCoast(String name)
/*    */   {
/* 50 */     for (CoastBreakpoint coast : this.coasts) {
/* 51 */       if (coast.getName().equals(name)) {
/* 52 */         return coast.getSegments();
/*    */       }
/*    */     }
/*    */ 
/* 56 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.CoastBreakpointList
 * JD-Core Version:    0.6.2
 */