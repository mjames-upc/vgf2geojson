/*    */ package gov.noaa.nws.ncep.ui.pgen.palette;
/*    */ 
/*    */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*    */ import org.osgi.framework.BundleContext;
/*    */ 
/*    */ public class Activator extends AbstractUIPlugin
/*    */ {
/*    */   public static final String PLUGIN_ID = "gov.noaa.nws.ncep.ui.pgen";
/*    */   private static Activator plugin;
/*    */ 
/*    */   public void start(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 28 */     super.start(context);
/* 29 */     plugin = this;
/*    */   }
/*    */ 
/*    */   public void stop(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 37 */     plugin = null;
/* 38 */     super.stop(context);
/*    */   }
/*    */ 
/*    */   public static Activator getDefault()
/*    */   {
/* 47 */     return plugin;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.palette.Activator
 * JD-Core Version:    0.6.2
 */