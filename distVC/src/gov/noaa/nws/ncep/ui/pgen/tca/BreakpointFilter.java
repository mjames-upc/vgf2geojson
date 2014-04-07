/*    */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*    */ 
/*    */ public class BreakpointFilter
/*    */ {
/*    */   private boolean official;
/*    */   private String coastName;
/*    */   private BreakpointManager bmgr;
/*    */ 
/*    */   public BreakpointFilter()
/*    */   {
/* 28 */     this.bmgr = BreakpointManager.getInstance();
/* 29 */     this.official = false;
/* 30 */     this.coastName = null;
/*    */   }
/*    */ 
/*    */   public void setOfficialOnly()
/*    */   {
/* 37 */     this.official = true;
/*    */   }
/*    */ 
/*    */   public void filterCoastName(String name)
/*    */   {
/* 45 */     this.coastName = name;
/*    */   }
/*    */ 
/*    */   public boolean isAccepted(Breakpoint bkpt)
/*    */   {
/* 55 */     if ((this.official) && (!bkpt.isOfficial())) {
/* 56 */       return false;
/*    */     }
/*    */ 
/* 59 */     if ((this.coastName != null) && 
/* 60 */       (!this.bmgr.findCoastName(bkpt).equals(this.coastName))) {
/* 61 */       return false;
/*    */     }
/*    */ 
/* 65 */     return true;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.BreakpointFilter
 * JD-Core Version:    0.6.2
 */