/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ public class StormAdvisoryNumber
/*     */ {
/*     */   public static boolean isValid(String advisory)
/*     */   {
/*  27 */     String adv = advisory.toLowerCase();
/*     */     String number;
/*     */     String number;
/*  33 */     if ((adv.endsWith("a")) || (adv.endsWith("b"))) {
/*  34 */       number = advisory.substring(0, advisory.length() - 1);
/*     */     }
/*     */     else {
/*  37 */       number = advisory;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  44 */       if (Integer.parseInt(number) > 0) return true; 
/*     */     }
/*     */     catch (NumberFormatException nfe)
/*     */     {
/*  47 */       return false;
/*     */     }
/*     */ 
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isIntermediate(String advisory)
/*     */   {
/*  60 */     String adv = advisory.toLowerCase();
/*     */ 
/*  66 */     if ((adv.endsWith("a")) || (adv.endsWith("b"))) {
/*  67 */       return true;
/*     */     }
/*     */ 
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */   public static int getRegularAdvisory(String advisory)
/*     */   {
/*  83 */     String adv = advisory.toLowerCase();
/*     */     String number;
/*     */     String number;
/*  89 */     if ((adv.endsWith("a")) || (adv.endsWith("b"))) {
/*  90 */       number = advisory.substring(0, advisory.length() - 1);
/*     */     }
/*     */     else {
/*  93 */       number = advisory;
/*     */     }
/*     */ 
/*     */     int num;
/*     */     try
/*     */     {
/* 100 */       num = Integer.parseInt(number);
/*     */     }
/*     */     catch (NumberFormatException nfe)
/*     */     {
/*     */       int num;
/* 103 */       num = 0;
/*     */     }
/*     */ 
/* 106 */     return num;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.StormAdvisoryNumber
 * JD-Core Version:    0.6.2
 */