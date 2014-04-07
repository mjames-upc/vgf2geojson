/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ public class CurveFitter
/*     */ {
/*     */   public static double[][] fitParametricCurve(double[][] pts, float density)
/*     */   {
/*  32 */     int n = pts.length - 1;
/*  33 */     int[] npts = new int[n];
/*     */ 
/*  40 */     if (n < 2) return pts;
/*     */ 
/*  45 */     int sumpts = 0;
/*  46 */     for (int i = 0; i < n; i++) {
/*  47 */       double xdiff = pts[(i + 1)][0] - pts[i][0];
/*  48 */       double ydiff = pts[(i + 1)][1] - pts[i][1];
/*  49 */       double chord = Math.sqrt(xdiff * xdiff + ydiff * ydiff);
/*  50 */       npts[i] = ((int)Math.floor(chord / density) + 1);
/*  51 */       sumpts += npts[i];
/*     */     }
/*     */ 
/*  57 */     double[] first = new double[3];
/*  58 */     double[] last = new double[3];
/*  59 */     if ((pts[0][0] == pts[n][0]) && (pts[0][1] == pts[n][1])) {
/*  60 */       first[0] = pts[(n - 1)][0];
/*  61 */       first[1] = pts[(n - 1)][1];
/*  62 */       last[0] = pts[1][0];
/*  63 */       last[1] = pts[1][1];
/*     */     }
/*     */     else {
/*  66 */       first[0] = ((5.0D * pts[0][0] - 4.0D * pts[1][0] + pts[2][0]) / 2.0D);
/*  67 */       first[1] = ((5.0D * pts[0][1] - 4.0D * pts[1][1] + pts[2][1]) / 2.0D);
/*  68 */       last[0] = ((5.0D * pts[n][0] - 4.0D * pts[(n - 1)][0] + pts[(n - 2)][0]) / 2.0D);
/*  69 */       last[1] = ((5.0D * pts[n][1] - 4.0D * pts[(n - 1)][1] + pts[(n - 2)][1]) / 2.0D);
/*     */     }
/*     */ 
/*  75 */     double[][] tmp = new double[pts.length + 2][3];
/*  76 */     tmp[0] = first;
/*  77 */     for (int j = 0; j < pts.length; j++) tmp[(j + 1)] = pts[j];
/*  78 */     tmp[(pts.length + 1)] = last;
/*     */ 
/*  83 */     int i = 0;
/*  84 */     double[][] newpts = new double[sumpts + 1][3];
/*  85 */     newpts[(i++)] = pts[0];
/*  86 */     for (int k = 1; k <= n; k++) {
/*  87 */       for (int j = 0; j < npts[(k - 1)]; j++)
/*     */       {
/*  89 */         double t = (j + 1) / npts[(k - 1)];
/*  90 */         double[] out = new double[3];
/*  91 */         out[0] = 
/*  93 */           (tmp[k][0] + 0.5D * (tmp[(k + 1)][0] - tmp[(k - 1)][0]) * t - 
/*  92 */           0.5D * (tmp[(k + 2)][0] - 4.0D * tmp[(k + 1)][0] + 5.0D * tmp[k][0] - 2.0D * tmp[(k - 1)][0]) * (t * t) + 
/*  93 */           0.5D * (tmp[(k + 2)][0] - 3.0D * tmp[(k + 1)][0] + 3.0D * tmp[k][0] - tmp[(k - 1)][0]) * (t * t * t));
/*  94 */         out[1] = 
/*  96 */           (tmp[k][1] + 0.5D * (tmp[(k + 1)][1] - tmp[(k - 1)][1]) * t - 
/*  95 */           0.5D * (tmp[(k + 2)][1] - 4.0D * tmp[(k + 1)][1] + 5.0D * tmp[k][1] - 2.0D * tmp[(k - 1)][1]) * (t * t) + 
/*  96 */           0.5D * (tmp[(k + 2)][1] - 3.0D * tmp[(k + 1)][1] + 3.0D * tmp[k][1] - tmp[(k - 1)][1]) * (t * t * t));
/*  97 */         out[2] = 0.0D;
/*     */ 
/* 100 */         newpts[(i++)] = out;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 105 */     return newpts;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.CurveFitter
 * JD-Core Version:    0.6.2
 */