/*    */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlElements;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class CoastBreakpoint
/*    */   implements ISerializableObject
/*    */ {
/*    */ 
/*    */   @XmlAttribute
/*    */   private String name;
/*    */ 
/*    */   @XmlAttribute
/*    */   private boolean island;
/*    */ 
/*    */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="segment")})
/*    */   private List<BreakpointSegment> segments;
/*    */ 
/*    */   public String getName()
/*    */   {
/* 54 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name)
/*    */   {
/* 61 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public List<BreakpointSegment> getSegments()
/*    */   {
/* 68 */     return this.segments;
/*    */   }
/*    */ 
/*    */   public void setSegments(List<BreakpointSegment> segments)
/*    */   {
/* 75 */     this.segments = segments;
/*    */   }
/*    */ 
/*    */   public boolean isIsland()
/*    */   {
/* 82 */     return this.island;
/*    */   }
/*    */ 
/*    */   public void setIsland(boolean island)
/*    */   {
/* 89 */     this.island = island;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.CoastBreakpoint
 * JD-Core Version:    0.6.2
 */