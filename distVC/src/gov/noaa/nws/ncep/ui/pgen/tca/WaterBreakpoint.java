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
/*    */ public class WaterBreakpoint extends BPGeography
/*    */   implements ISerializableObject
/*    */ {
/*    */ 
/*    */   @XmlElement
/*    */   private Breakpoint breakpoint;
/*    */ 
/*    */   public Breakpoint getBreakpoint()
/*    */   {
/* 42 */     return this.breakpoint;
/*    */   }
/*    */ 
/*    */   public void setBreakpoint(Breakpoint breakpoint)
/*    */   {
/* 49 */     this.breakpoint = breakpoint;
/*    */   }
/*    */ 
/*    */   public List<Breakpoint> getBreakpoints()
/*    */   {
/* 54 */     List list = new ArrayList();
/* 55 */     list.add(this.breakpoint);
/* 56 */     return list;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.WaterBreakpoint
 * JD-Core Version:    0.6.2
 */