/*    */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*    */ 
/*    */ public class Zone
/*    */ {
/*    */   private static final String ZONE_FORMAT = "%2sZ%03d";
/*    */   private String state;
/*    */   private int number;
/*    */ 
/*    */   public Zone(String zone)
/*    */   {
/* 20 */     this.state = zone.substring(0, 2).toUpperCase();
/* 21 */     this.number = Integer.parseInt(zone.substring(3));
/*    */   }
/*    */ 
/*    */   public String getState()
/*    */   {
/* 29 */     return this.state;
/*    */   }
/*    */ 
/*    */   public int getNumber()
/*    */   {
/* 37 */     return this.number;
/*    */   }
/*    */ 
/*    */   public String getText()
/*    */   {
/* 44 */     return String.format("%2sZ%03d", new Object[] { this.state, Integer.valueOf(this.number) });
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.Zone
 * JD-Core Version:    0.6.2
 */