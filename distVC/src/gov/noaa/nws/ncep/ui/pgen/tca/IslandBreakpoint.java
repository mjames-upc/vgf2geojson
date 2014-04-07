/*    */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class IslandBreakpoint extends BPGeography
/*    */   implements ISerializableObject
/*    */ {
/*    */ 
/*    */   @XmlElement
/*    */   private Breakpoint breakpoint;
/*    */ 
/*    */   public Breakpoint getBreakpoint()
/*    */   {
/* 43 */     return this.breakpoint;
/*    */   }
/*    */ 
/*    */   public void setBreakpoint(Breakpoint breakpoint)
/*    */   {
/* 50 */     this.breakpoint = breakpoint;
/*    */   }
/*    */ 
/*    */   public List<Breakpoint> getBreakpoints()
/*    */   {
/* 55 */     List list = new ArrayList();
/* 56 */     list.add(this.breakpoint);
/* 57 */     return list;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.IslandBreakpoint
 * JD-Core Version:    0.6.2
 */