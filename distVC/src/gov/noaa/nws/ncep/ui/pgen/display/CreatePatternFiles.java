/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class CreatePatternFiles
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 22 */     createSymbolPatternFile("/tmp/symbolPatterns.xml");
/*    */   }
/*    */ 
/*    */   private static void createLinePatternFile(String fname)
/*    */   {
/* 28 */     LinePatternManager lpmgr = LinePatternManager.getInstance();
/* 29 */     System.out.println("SAG" + lpmgr.getPatternIds().length);
/*    */     try
/*    */     {
/* 34 */       lpmgr.savePatternsToFile(fname);
/* 35 */       lpmgr.loadPatternsFromFile(fname);
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 50 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   private static void createSymbolPatternFile(String fname)
/*    */   {
/* 57 */     SymbolPatternManager spmgr = SymbolPatternManager.getInstance();
/* 58 */     System.out.println("SAG" + spmgr.getPatternIds().length);
/*    */     try
/*    */     {
/* 74 */       spmgr.savePatternsToFile(fname);
/* 75 */       spmgr.loadPatternsFromFile(fname);
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 79 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.CreatePatternFiles
 * JD-Core Version:    0.6.2
 */