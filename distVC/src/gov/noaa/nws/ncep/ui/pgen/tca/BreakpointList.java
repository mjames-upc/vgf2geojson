/*    */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class BreakpointList extends BPGeography
/*    */ {
/*    */ 
/*    */   @XmlElement
/*    */   private List<Breakpoint> breakpoints;
/*    */ 
/*    */   public BreakpointList()
/*    */   {
/* 38 */     this.breakpoints = new ArrayList();
/*    */   }
/*    */ 
/*    */   public List<Breakpoint> getBreakpoints()
/*    */   {
/* 47 */     return this.breakpoints;
/*    */   }
/*    */ 
/*    */   public void addBreakpoint(Breakpoint bkpt) {
/* 51 */     this.breakpoints.add(bkpt);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.BreakpointList
 * JD-Core Version:    0.6.2
 */