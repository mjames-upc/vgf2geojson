/*    */ package gov.noaa.nws.ncep.ui.pgen;
/*    */ 
/*    */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*    */ import org.osgi.framework.Bundle;
/*    */ import org.osgi.framework.BundleContext;
/*    */ import org.osgi.framework.ServiceReference;
/*    */ 
/*    */ public class PgenStaticDataProvider
/*    */ {
/*    */   private static IStaticDataProvider provider;
/*    */ 
/*    */   public static IStaticDataProvider getProvider()
/*    */   {
/* 12 */     if (provider == null)
/*    */     {
/* 14 */       ServiceReference ref = Activator.getDefault().getBundle().getBundleContext().getServiceReference(
/* 15 */         IStaticDataProvider.class.getName());
/* 16 */       IStaticDataProvider isdp = null;
/* 17 */       if ((ref != null) && 
/* 18 */         ((isdp = (IStaticDataProvider)Activator.getDefault().getBundle().getBundleContext().getService(ref)) != null))
/* 19 */         provider = isdp;
/*    */       else {
/* 21 */         throw new PGenRuntimeException("PGEN static data provider: NULL!");
/*    */       }
/*    */     }
/* 24 */     return provider;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider
 * JD-Core Version:    0.6.2
 */