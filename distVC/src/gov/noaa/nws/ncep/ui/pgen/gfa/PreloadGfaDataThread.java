/*    */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.localization.LocalizationManager;
/*    */ import gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo;
/*    */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*    */ 
/*    */ public class PreloadGfaDataThread extends Thread
/*    */ {
/*    */   public static boolean loaded;
/*    */   public boolean started;
/* 43 */   private String[] toLoad = { LocalizationManager.class.getName(), PgenCycleTool.class.getName(), SigmetInfo.class.getName(), GfaClip.class.getName() };
/*    */ 
/*    */   public void run()
/*    */   {
/* 51 */     if ((this.started) || (loaded)) return;
/*    */ 
/* 53 */     this.started = true;
/*    */ 
/* 55 */     long time = System.currentTimeMillis();
/*    */ 
/* 57 */     ClassLoader loader = getClass().getClassLoader();
/*    */     try {
/* 59 */       for (String c : this.toLoad) {
/* 60 */         loader.loadClass(c);
/*    */       }
/*    */ 
/* 63 */       LocalizationManager.getBaseDir();
/* 64 */       GfaInfo.getDocument();
/* 65 */       new SigmetInfo();
/*    */ 
/* 68 */       GfaClip.getInstance().loadGfaBounds();
/* 69 */       ReduceGfaPointsUtil.getStationTable();
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 73 */       e.printStackTrace();
/*    */     }
/*    */ 
/* 79 */     loaded = true;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.PreloadGfaDataThread
 * JD-Core Version:    0.6.2
 */