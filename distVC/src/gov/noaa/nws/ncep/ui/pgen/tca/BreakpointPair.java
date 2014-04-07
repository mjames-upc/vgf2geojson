/*    */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class BreakpointPair extends BPGeography
/*    */ {
/*    */ 
/*    */   @XmlElement
/*    */   private List<Breakpoint> breakpoints;
/*    */ 
/*    */   public BreakpointPair()
/*    */   {
/* 37 */     this.breakpoints = new ArrayList();
/*    */   }
/*    */ 
/*    */   public BreakpointPair(List<Breakpoint> breakpoints)
/*    */   {
/* 46 */     this.breakpoints = breakpoints;
/*    */   }
/*    */ 
/*    */   public List<Breakpoint> getBreakpoints()
/*    */   {
/* 55 */     return this.breakpoints;
/*    */   }
/*    */ 
/*    */   public void addBreakpoint(Breakpoint bkpt) {
/* 59 */     this.breakpoints.add(bkpt);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.BreakpointPair
 * JD-Core Version:    0.6.2
 */