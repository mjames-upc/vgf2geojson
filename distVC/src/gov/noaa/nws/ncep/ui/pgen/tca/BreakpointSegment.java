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
/*    */ public class BreakpointSegment extends BPGeography
/*    */   implements ISerializableObject
/*    */ {
/*    */ 
/*    */   @XmlElement
/*    */   private Breakpoint breakpoint;
/*    */ 
/*    */   public Breakpoint getBreakpoint()
/*    */   {
/* 48 */     return this.breakpoint;
/*    */   }
/*    */ 
/*    */   public void setBreakpoint(Breakpoint breakpoint)
/*    */   {
/* 55 */     this.breakpoint = breakpoint;
/*    */   }
/*    */ 
/*    */   public List<Breakpoint> getBreakpoints()
/*    */   {
/* 60 */     List list = new ArrayList();
/* 61 */     list.add(this.breakpoint);
/* 62 */     return list;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.BreakpointSegment
 * JD-Core Version:    0.6.2
 */