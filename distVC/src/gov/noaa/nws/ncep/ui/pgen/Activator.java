/*    */ package gov.noaa.nws.ncep.ui.pgen;
/*    */ 
/*    */ import org.eclipse.jface.preference.IPreferenceStore;
/*    */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*    */ import org.osgi.framework.BundleContext;
/*    */ 
/*    */ public class Activator extends AbstractUIPlugin
/*    */ {
/*    */   public static final String PLUGIN_ID = "gov.noaa.nws.ncep.ui.pgen";
/*    */   private static Activator plugin;
/* 20 */   private IPreferenceStore myprefs = null;
/*    */ 
/*    */   public void start(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 33 */     super.start(context);
/* 34 */     plugin = this;
/*    */   }
/*    */ 
/*    */   public void stop(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 42 */     plugin = null;
/* 43 */     super.stop(context);
/*    */   }
/*    */ 
/*    */   public static Activator getDefault()
/*    */   {
/* 52 */     return plugin;
/*    */   }
/*    */ 
/*    */   public IPreferenceStore getPreferenceStore()
/*    */   {
/* 64 */     if (this.myprefs == null) {
/* 65 */       this.myprefs = super.getPreferenceStore();
/* 66 */       this.myprefs.setDefault("PGEN_BASE_DIR", PgenPreferences.V_OPR_DIR);
/* 67 */       this.myprefs.setDefault("PGEN_WORKING_DIR", PgenPreferences.V_WORKING_DIR);
/* 68 */       this.myprefs.setDefault("PGEN_RECOVERY_DIR", "/tmp");
/* 69 */       this.myprefs.setDefault("PGEN_AUTOSAVE_FREQ", 5);
/* 70 */       this.myprefs.setDefault("PGEN_MAX_DISTANCE_TO_SELECT", 30);
/* 71 */       this.myprefs.setDefault("PGEN_MODE", PgenUtil.PgenMode.SINGLE.toString());
/* 72 */       this.myprefs.setDefault("PGEN_LAYER_LINK", false);
/* 73 */       this.myprefs.setDefault("PGEN_COMP_COORD", "ced/0;0;0|18.00;-137.00;58.00;-54.00");
/*    */     }
/*    */ 
/* 76 */     return this.myprefs;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.Activator
 * JD-Core Version:    0.6.2
 */