/*    */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*    */ 
/*    */ public class Basin
/*    */ {
/* 18 */   private static String ATLANTIC = "Atlantic";
/* 19 */   private static String EAST_PACIFIC = "E. Pacific";
/* 20 */   private static String CENTRAL_PACIFIC = "C. Pacific";
/* 21 */   private static String WEST_PACIFIC = "W. Pacific";
/*    */ 
/*    */   public static String getBasinAbbrev(String basin)
/*    */   {
/* 30 */     String abbrev = null;
/*    */ 
/* 32 */     if (basin.equals(ATLANTIC)) {
/* 33 */       abbrev = new String("al");
/*    */     }
/* 35 */     else if (basin.equals(EAST_PACIFIC)) {
/* 36 */       abbrev = new String("ep");
/*    */     }
/* 38 */     else if (basin.equals(CENTRAL_PACIFIC)) {
/* 39 */       abbrev = new String("cp");
/*    */     }
/* 41 */     else if (basin.equals(WEST_PACIFIC)) {
/* 42 */       abbrev = new String("wp");
/*    */     }
/*    */     else {
/* 45 */       abbrev = new String("xx");
/*    */     }
/*    */ 
/* 48 */     return abbrev;
/*    */   }
/*    */ 
/*    */   public static int getBasinNumber(String basin)
/*    */   {
/* 59 */     int num = 0;
/*    */ 
/* 61 */     if ((basin.equals(ATLANTIC)) || (basin.equalsIgnoreCase("al"))) {
/* 62 */       num = 1;
/*    */     }
/* 64 */     else if ((basin.equals(EAST_PACIFIC)) || (basin.equalsIgnoreCase("ep"))) {
/* 65 */       num = 2;
/*    */     }
/* 67 */     else if ((basin.equals(CENTRAL_PACIFIC)) || (basin.equalsIgnoreCase("cp"))) {
/* 68 */       num = 3;
/*    */     }
/* 70 */     else if ((basin.equals(WEST_PACIFIC)) || (basin.equalsIgnoreCase("wp"))) {
/* 71 */       num = 4;
/*    */     }
/*    */ 
/* 74 */     return num;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.Basin
 * JD-Core Version:    0.6.2
 */