/*     */ package gov.noaa.nws.ncep.ui.pgen.graphtogrid;
/*     */ 
/*     */ import com.sun.jna.Library;
/*     */ import com.sun.jna.Native;
/*     */ 
/*     */ public class G2GNativeLibrary
/*     */ {
/*     */   private static G2GNativeLibrary instance;
/*  89 */   public G2GNative g2gNative = G2GNative.INSTANCE;
/*     */ 
/*     */   public static G2GNativeLibrary getInstance()
/*     */   {
/*  44 */     if (instance == null) {
/*  45 */       instance = new G2GNativeLibrary();
/*     */     }
/*  47 */     return instance;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  81 */     G2GNative INSTANCE = (G2GNative)Native.loadLibrary("g2g", G2GNative.class);
/*     */   }
/*     */ 
/*     */   public int g2g_compute(float[] grid, float[] hist, int kx, int ky, int nlines, int[] nlatlons, float[] latPts, float[] lonPts, int[] nsmthpts, float[] smthLat, float[] smthLon, int[] nextpts, float[] extLat, float[] extLon, float[] values, int[] ismth, int[] iclosed, int mmnum, float[] mmlat, float[] mmlon, float[] mmfi, float[] mmfj, float[] mmvalue, String catmap, String hstgrd, String discrete, String dlines, String gglimt, String edgeopts)
/*     */   {
/* 107 */     int ier = this.g2gNative.g2g_driver(grid, hist, kx, ky, nlines, 
/* 108 */       nlatlons, latPts, lonPts, 
/* 109 */       nsmthpts, smthLat, smthLon, 
/* 110 */       nextpts, extLat, extLon, 
/* 111 */       values, ismth, iclosed, 
/* 112 */       mmnum, mmlat, mmlon, 
/* 113 */       mmfi, mmfj, mmvalue, 
/* 114 */       catmap, hstgrd, discrete, dlines, 
/* 115 */       gglimt, edgeopts);
/*     */ 
/* 119 */     return ier;
/*     */   }
/*     */ 
/*     */   public int g2g_write(float[] grid, float[] hist, String hstgrd, String gdfile, String proj, String cpyfil, String gdarea, String anlyss, String kxky, String maxgrd, String gparm, String gdatim, String gvcord, String glevel)
/*     */   {
/* 133 */     int ier = this.g2gNative.g2g_writer(grid, hist, hstgrd, 
/* 134 */       gdfile, proj, cpyfil, gdarea, anlyss, kxky, 
/* 135 */       maxgrd, gparm, gdatim, gvcord, glevel);
/*     */ 
/* 139 */     return 0;
/*     */   }
/*     */ 
/*     */   public static abstract interface G2GNative extends Library
/*     */   {
/*  55 */     public static final G2GNative INSTANCE = (G2GNative)Native.loadLibrary("g2g", G2GNative.class);
/*     */ 
/*     */     public abstract int g2g_driver(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4, int[] paramArrayOfInt2, float[] paramArrayOfFloat5, float[] paramArrayOfFloat6, int[] paramArrayOfInt3, float[] paramArrayOfFloat7, float[] paramArrayOfFloat8, float[] paramArrayOfFloat9, int[] paramArrayOfInt4, int[] paramArrayOfInt5, int paramInt4, float[] paramArrayOfFloat10, float[] paramArrayOfFloat11, float[] paramArrayOfFloat12, float[] paramArrayOfFloat13, float[] paramArrayOfFloat14, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6);
/*     */ 
/*     */     public abstract int g2g_writer(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.graphtogrid.G2GNativeLibrary
 * JD-Core Version:    0.6.2
 */