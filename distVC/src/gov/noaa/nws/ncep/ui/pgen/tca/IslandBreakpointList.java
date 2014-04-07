/*    */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ @XmlRootElement(name="islandBreakpoints")
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class IslandBreakpointList
/*    */   implements ISerializableObject
/*    */ {
/*    */ 
/*    */   @XmlElement(name="island")
/*    */   private List<IslandBreakpoint> islands;
/*    */ 
/*    */   public List<IslandBreakpoint> getIslands()
/*    */   {
/* 37 */     return this.islands;
/*    */   }
/*    */ 
/*    */   public void setIslands(List<IslandBreakpoint> islands)
/*    */   {
/* 44 */     this.islands = islands;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.IslandBreakpointList
 * JD-Core Version:    0.6.2
 */