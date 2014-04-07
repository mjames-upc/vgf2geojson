/*     */ package gov.noaa.nws.ncep.ui.pgen.graphtogrid;
/*     */ 
/*     */ import com.raytheon.edex.meteoLib.Controller;
/*     */ import com.raytheon.uf.viz.core.map.MapDescriptor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.CoordinateSequence;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LinearRing;
/*     */ import com.vividsolutions.jts.geom.MultiPolygon;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
/*     */ import gov.noaa.nws.ncep.gempak.parameters.core.categorymap.CatMap;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourCircle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourMinmax;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class ContoursToGrid extends GraphToGrid
/*     */ {
/*  72 */   private static float smallestContourValue = -1000000.0F;
/*  73 */   private static float largestContourValue = -999998.0F;
/*     */ 
/*  76 */   private GeometryFactory geometryFactory = new GeometryFactory();
/*     */ 
/*     */   public ContoursToGrid(Contours currentGraph, HashMap<String, String> gridParameters)
/*     */   {
/*  83 */     super(currentGraph, gridParameters);
/*     */   }
/*     */ 
/*     */   public void makeGrid()
/*     */   {
/*  95 */     String proj = getParamValues(this.gridParameters, "PROJ");
/*  96 */     String garea = getParamValues(this.gridParameters, "GRDAREA");
/*  97 */     String kxky = getParamValues(this.gridParameters, "KXKY");
/*     */ 
/*  99 */     String[] nkxky = kxky.split(";");
/*     */ 
/* 101 */     int kx = 63;
/* 102 */     int ky = 28;
/* 103 */     if (nkxky.length > 1) {
/* 104 */       kx = Integer.parseInt(nkxky[0]);
/* 105 */       ky = Integer.parseInt(nkxky[1]);
/*     */     }
/*     */ 
/* 114 */     CoordinateTransform gtrans = new CoordinateTransform(proj, garea, kx, ky);
/*     */ 
/* 119 */     float[] grid = new float[kx * ky];
/* 120 */     float[] hist = new float[kx * ky];
/*     */ 
/* 122 */     for (int kk = 0; kk < kx * ky; kk++) {
/* 123 */       grid[kk] = -9999.0F;
/* 124 */       hist[kk] = 0.0F;
/*     */     }
/*     */ 
/* 127 */     Coordinate[] gridPts = new Coordinate[kx * ky];
/* 128 */     double[] newPt = new double[2];
/*     */ 
/* 130 */     for (int jj = 0; jj < ky; jj++) {
/* 131 */       for (int ii = 0; ii < kx; ii++)
/*     */       {
/* 134 */         newPt[0] = (ii + 1);
/* 135 */         newPt[1] = (jj + 1);
/* 136 */         double[] lonlat = gtrans.gridToWorld(newPt);
/*     */ 
/* 138 */         Coordinate c = new Coordinate(lonlat[0], lonlat[1]);
/*     */ 
/* 140 */         gridPts[(ii + jj * kx)] = c;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 147 */     String bound = getParamValues(this.gridParameters, "BOUNDS");
/* 148 */     Contours bndContours = contoursFromBounds(bound, gtrans, hist, gridPts, kx, ky);
/*     */ 
/* 153 */     String cmapString = getParamValues(this.gridParameters, "CATMAP");
/* 154 */     CatMap cmap = new CatMap(cmapString);
/*     */ 
/* 168 */     Contours extContours = ((Contours)this.currentGraph).copy();
/*     */ 
/* 171 */     checkSpecialBounds(extContours, hist, kx, ky, gtrans, gridPts, cmap);
/*     */ 
/* 173 */     for (ContourLine cline : bndContours.getContourLines()) {
/* 174 */       extContours.add(cline);
/*     */     }
/*     */ 
/* 178 */     ArrayList ccircle = extContours.getContourCircles();
/* 179 */     for (ContourCircle cc : ccircle) {
/* 180 */       ArrayList pts = generateArcPoints((Arc)cc.getCircle(), 20.0D);
/* 181 */       if (pts.size() > 1) {
/* 182 */         ContourLine ncl = new ContourLine(pts, true, cc.getLabelString(), 1);
/* 183 */         extContours.add(ncl);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 189 */     ContoursExtension cntExt = new ContoursExtension(extContours, gtrans, 
/* 190 */       kx, ky, new Color[] { Color.blue }, cmap);
/*     */ 
/* 206 */     String bnds = getParamValues(this.gridParameters, "BOUNDS");
/* 207 */     boolean extend = true;
/*     */ 
/* 217 */     int nlines = cntExt.getNlines();
/*     */ 
/* 219 */     int[] npoints = cntExt.getNpts();
/* 220 */     int[] norigpts = cntExt.getNOrigPts();
/*     */ 
/* 222 */     float[][] flat = cntExt.getFlat();
/* 223 */     float[][] flon = cntExt.getFlon();
/* 224 */     float[][] fi_orig = cntExt.getFiOrig();
/* 225 */     float[][] fj_orig = cntExt.getFjOrig();
/*     */     float[][] fj_ext;
/*     */     int[] nextpts;
/*     */     float[][] fi_ext;
/*     */     float[][] fj_ext;
/* 228 */     if (extend) {
/* 229 */       int[] nextpts = cntExt.getNExtPts();
/* 230 */       float[][] fi_ext = cntExt.getFiExt();
/* 231 */       fj_ext = cntExt.getFjExt();
/*     */     }
/*     */     else {
/* 234 */       nextpts = cntExt.getNOrigPts();
/* 235 */       fi_ext = cntExt.getFiOrig();
/* 236 */       fj_ext = cntExt.getFjOrig();
/*     */     }
/*     */ 
/* 243 */     int ntotal = 0;
/* 244 */     for (int kk = 0; kk < nlines; kk++) {
/* 245 */       ntotal += npoints[kk];
/*     */     }
/*     */ 
/* 248 */     float[] latPts = new float[ntotal];
/* 249 */     float[] lonPts = new float[ntotal];
/* 250 */     int np = 0;
/* 251 */     for (int kk = 0; kk < nlines; kk++) {
/* 252 */       for (int mm = 0; mm < npoints[kk]; mm++) {
/* 253 */         latPts[(np + mm)] = flat[kk][mm];
/* 254 */         lonPts[(np + mm)] = flon[kk][mm];
/*     */       }
/*     */ 
/* 257 */       np += npoints[kk];
/*     */     }
/*     */ 
/* 263 */     ntotal = 0;
/* 264 */     for (int kk = 0; kk < nlines; kk++) {
/* 265 */       ntotal += norigpts[kk];
/*     */     }
/*     */ 
/* 268 */     float[] smthLat = new float[ntotal];
/* 269 */     float[] smthLon = new float[ntotal];
/* 270 */     np = 0;
/* 271 */     for (int kk = 0; kk < nlines; kk++) {
/* 272 */       for (int mm = 0; mm < norigpts[kk]; mm++) {
/* 273 */         smthLat[(np + mm)] = fi_orig[kk][mm];
/* 274 */         smthLon[(np + mm)] = fj_orig[kk][mm];
/*     */       }
/*     */ 
/* 277 */       np += norigpts[kk];
/*     */     }
/*     */ 
/* 283 */     ntotal = 0;
/* 284 */     for (int kk = 0; kk < nlines; kk++) {
/* 285 */       ntotal += nextpts[kk];
/*     */     }
/*     */ 
/* 288 */     float[] extLat = new float[ntotal];
/* 289 */     float[] extLon = new float[ntotal];
/* 290 */     np = 0;
/* 291 */     for (int kk = 0; kk < nlines; kk++) {
/* 292 */       for (int mm = 0; mm < nextpts[kk]; mm++) {
/* 293 */         extLat[(np + mm)] = fi_ext[kk][mm];
/* 294 */         extLon[(np + mm)] = fj_ext[kk][mm];
/*     */       }
/*     */ 
/* 297 */       np += nextpts[kk];
/*     */     }
/*     */ 
/* 306 */     int[] ismth = cntExt.getIsmth();
/* 307 */     int[] iclosed = cntExt.getClosed();
/* 308 */     float[] values = cntExt.getValue();
/* 309 */     float[] linevalues = new float[values.length];
/* 310 */     for (int ii = 0; ii < values.length; ii++) {
/* 311 */       if (values[ii] == 9999.0F) {
/* 312 */         linevalues[ii] = (-values[ii]);
/*     */       }
/*     */       else {
/* 315 */         linevalues[ii] = values[ii];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 322 */     ArrayList cminmax = extContours.getContourMinmaxs();
/* 323 */     int mmnum = cminmax.size();
/* 324 */     double[] mmlonlat = new double[mmnum * 2];
/* 325 */     double[] mmgrid = new double[mmnum * 2];
/* 326 */     float[] mmvalue = new float[mmnum];
/* 327 */     float[] mmlat = new float[mmnum];
/* 328 */     float[] mmlon = new float[mmnum];
/* 329 */     float[] mmfi = new float[mmnum];
/* 330 */     float[] mmfj = new float[mmnum];
/*     */ 
/* 332 */     int ii = 0;
/* 333 */     for (ContourMinmax cmm : cminmax) {
/* 334 */       Coordinate p = ((SinglePointElement)cmm.getSymbol()).getLocation();
/* 335 */       mmlon[ii] = ((float)p.x);
/* 336 */       mmlat[ii] = ((float)p.y);
/*     */ 
/* 338 */       mmlonlat[(ii * 2)] = p.x;
/* 339 */       mmlonlat[(ii * 2 + 1)] = p.y;
/* 340 */       mmvalue[ii] = getValueForLabel(cmap, cmm.getLabelString()[0]);
/* 341 */       ii++;
/*     */     }
/*     */ 
/* 344 */     mmgrid = gtrans.worldToGrid(mmlonlat);
/* 345 */     for (int jj = 0; jj < mmnum; jj++) {
/* 346 */       mmfi[jj] = ((float)mmgrid[(jj * 2)]);
/* 347 */       mmfj[jj] = ((float)mmgrid[(jj * 2 + 1)]);
/*     */     }
/*     */ 
/* 353 */     G2GNativeLibrary g2gNative = G2GNativeLibrary.getInstance();
/*     */ 
/* 355 */     String catmap = getParamValues(this.gridParameters, "CATMAP");
/* 356 */     String gglims = getParamValues(this.gridParameters, "GGLIMS");
/* 357 */     String histgrd = getParamValues(this.gridParameters, "HISTGRD");
/* 358 */     String discrete = getParamValues(this.gridParameters, "DISCRETE");
/* 359 */     String dlines = getParamValues(this.gridParameters, "DLINES");
/* 360 */     String edgeopts = getParamValues(this.gridParameters, "EDGEOPTS");
/*     */ 
/* 366 */     g2gNative.g2g_compute(grid, hist, kx, ky, nlines, 
/* 367 */       npoints, latPts, lonPts, norigpts, smthLat, smthLon, 
/* 368 */       nextpts, extLat, extLon, linevalues, ismth, iclosed, 
/* 369 */       mmnum, mmlat, mmlon, mmfi, mmfj, mmvalue, 
/* 370 */       catmap, histgrd, discrete, dlines, gglims, edgeopts);
/*     */ 
/* 375 */     String cint = getParamValues(this.gridParameters, "CINT");
/* 376 */     contoursFromGrid(grid, kx, ky, cint, gtrans, cmap);
/*     */ 
/* 381 */     String gdoutf = getParamValues(this.gridParameters, "GDOUTF");
/* 382 */     String maxgrd = getParamValues(this.gridParameters, "MAXGRD");
/* 383 */     String gdatim = getParamValues(this.gridParameters, "GDATTIM");
/* 384 */     String gvcord = getParamValues(this.gridParameters, "GVCORD");
/*     */ 
/* 386 */     String ckxky = new String(gtrans.getKx() + ";" + gtrans.getKy());
/* 387 */     Coordinate[] gext = gtrans.getExtent();
/* 388 */     String gdarea = new String(gext[0].y + ";" + gext[0].x + ";" + 
/* 389 */       gext[1].y + ";" + gext[1].x);
/*     */ 
/* 391 */     String gparm = getParamValues(this.gridParameters, "GPARM");
/* 392 */     if ((gparm == null) || (gparm.trim().length() == 0)) {
/* 393 */       gparm = extContours.getParm();
/*     */     }
/*     */ 
/* 396 */     String glevel = getParamValues(this.gridParameters, "GLEVEL");
/* 397 */     if ((glevel == null) || (glevel.trim().length() == 0)) {
/* 398 */       glevel = extContours.getLevel();
/*     */     }
/*     */ 
/* 401 */     if ((gparm == null) || (gparm.trim().length() == 0))
/*     */     {
/* 403 */       return;
/*     */     }
/*     */ 
/* 406 */     if ((glevel == null) || (glevel.trim().length() == 0))
/*     */     {
/* 408 */       return;
/*     */     }
/*     */ 
/* 411 */     gdatim = new String(PgenUtil.calendarToGempakDattim(extContours.getTime1()) + 
/* 412 */       extContours.getForecastHours());
/*     */ 
/* 417 */     String path = getParamValues(this.gridParameters, "PATH");
/* 418 */     if ((path == null) || (path.trim().length() == 0)) {
/* 419 */       path = new String(".");
/*     */     }
/*     */ 
/* 422 */     if (path.trim().startsWith("."))
/*     */     {
/* 424 */       String workingDir = PgenUtil.getWorkingDirectory();
/* 425 */       if (workingDir != null) {
/* 426 */         path = new String(path.trim().replaceFirst(".", workingDir));
/*     */       }
/*     */     }
/*     */ 
/* 430 */     String fullFile = new String(path + "/" + gdoutf);
/*     */ 
/* 435 */     File grdfile = new File(path);
/*     */ 
/* 437 */     Pattern ptn = Pattern.compile("[A-Z]");
/* 438 */     Matcher mt = ptn.matcher(path);
/*     */ 
/* 440 */     String msg = null;
/* 441 */     if (mt.find()) {
/* 442 */       msg = new String("The PATH cannot have upper case in it");
/*     */     }
/* 445 */     else if (!grdfile.exists()) {
/* 446 */       msg = new String("The PATH does not exist, please create it first");
/*     */     }
/*     */ 
/* 450 */     if (msg != null)
/*     */     {
/* 452 */       MessageDialog msgDlg = new MessageDialog(
/* 453 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 454 */         "Warning", null, "Fail to write to " + fullFile + " because:\n\n" + msg, 
/* 455 */         2, new String[] { "OK" }, 0);
/* 456 */       msgDlg.open();
/*     */ 
/* 458 */       return;
/*     */     }
/*     */ 
/* 464 */     String cpyfil = new String(" ");
/* 465 */     String anlyss = new String(" ");
/*     */ 
/* 483 */     g2gNative.g2g_write(grid, hist, histgrd, 
/* 484 */       fullFile, proj, cpyfil, gdarea, anlyss, ckxky, 
/* 485 */       maxgrd, gparm, gdatim, gvcord, glevel);
/*     */   }
/*     */ 
/*     */   private void contoursFromGrid(float[] grid, int kx, int ky, String cint, CoordinateTransform gtrans, CatMap cmap)
/*     */   {
/* 499 */     long[] sz = { kx, ky };
/*     */ 
/* 501 */     int minX = 0;
/* 502 */     int minY = 0;
/* 503 */     int maxX = (int)(sz[0] - 1L);
/* 504 */     int maxY = (int)(sz[1] - 1L);
/*     */ 
/* 506 */     int szX = maxX - minX + 1;
/* 507 */     int szY = maxY - minY + 1;
/*     */ 
/* 509 */     int[] work = new int[10 * grid.length];
/* 510 */     float[] xPoints = new float[10 * grid.length];
/* 511 */     float[] yPoints = new float[10 * grid.length];
/* 512 */     int[] numPoints = new int[1];
/*     */ 
/* 517 */     float gmax = 1.4E-45F;
/* 518 */     float gmin = 3.4028235E+38F;
/*     */ 
/* 520 */     for (int ii = 0; ii < grid.length; ii++) {
/* 521 */       if (grid[ii] > gmax) {
/* 522 */         gmax = grid[ii];
/*     */       }
/*     */ 
/* 525 */       if (grid[ii] < gmin) {
/* 526 */         gmin = grid[ii];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 531 */     ArrayList contourVals = new ArrayList();
/* 532 */     for (ContourLine cline : ((Contours)this.currentGraph).getContourLines()) {
/* 533 */       contourVals.add(cline.getLabelString()[0]);
/*     */     }
/*     */ 
/* 536 */     if (cint != null) {
/* 537 */       float interval = 0.0F;
/* 538 */       if (!cint.contains(";")) {
/*     */         try {
/* 540 */           interval = Math.abs(Float.parseFloat(cint));
/*     */         }
/*     */         catch (Exception localException2)
/*     */         {
/*     */         }
/*     */ 
/* 546 */         if ((interval > 0.0D) && (gmin + interval < gmax))
/*     */         {
/* 548 */           float start = getValueForLabel(cmap, (String)contourVals.get(0));
/* 549 */           while (start > gmin) {
/* 550 */             start -= interval;
/*     */           }
/*     */ 
/* 553 */           contourVals.clear();
/* 554 */           while (start < gmax) {
/* 555 */             contourVals.add(start);
/* 556 */             start += interval;
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 562 */         String[] cintArray = cint.split(";");
/* 563 */         ArrayList cintList = new ArrayList();
/* 564 */         for (String str : cintArray) {
/*     */           try {
/* 566 */             Float.parseFloat(str);
/* 567 */             cintList.add(str);
/*     */           }
/*     */           catch (Exception localException1)
/*     */           {
/*     */           }
/*     */         }
/* 573 */         if (cintList.size() > 0) {
/* 574 */           contourVals.clear();
/* 575 */           contourVals.addAll(cintList);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 580 */     float[] contVals = new float[contourVals.size()];
/* 581 */     for (int ii = 0; ii < contourVals.size(); ii++) {
/* 582 */       contVals[ii] = getValueForLabel(cmap, (String)contourVals.get(ii));
/*     */     }
/*     */ 
/* 588 */     Controller.fortconbuf(grid, work, szX, szX, szY, 1.0F, 0.0F, 
/* 589 */       contVals.length, contVals, xPoints, yPoints, numPoints, 
/* 590 */       smallestContourValue, largestContourValue);
/*     */ 
/* 596 */     int nContours = 0;
/* 597 */     for (int ii = 0; ii < numPoints[0]; ii++) {
/* 598 */       if (xPoints[ii] == -99999.0D)
/*     */       {
/* 600 */         nContours++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 607 */     int[] nContourPts = new int[nContours];
/* 608 */     float[] contourValue = new float[nContours];
/*     */ 
/* 610 */     double[] vals = new double[(numPoints[0] - nContours) * 2];
/* 611 */     int ncnt = 0;
/* 612 */     int npts = 0;
/* 613 */     for (int ii = 0; ii < numPoints[0]; ii++) {
/* 614 */       if (xPoints[ii] == -99999.0D) {
/* 615 */         nContourPts[ncnt] = 0;
/* 616 */         contourValue[ncnt] = ((int)(yPoints[ii] * 10.0F) / 10.0F);
/* 617 */         ncnt++;
/*     */       }
/*     */       else {
/* 620 */         nContourPts[(ncnt - 1)] += 1;
/*     */ 
/* 622 */         vals[npts] = xPoints[ii];
/* 623 */         vals[(npts + 1)] = yPoints[ii];
/*     */ 
/* 625 */         npts += 2;
/*     */       }
/*     */     }
/*     */ 
/* 629 */     double[] latlons = gtrans.gridToWorld(vals);
/*     */ 
/* 634 */     String[] cntStrings = new String[nContours];
/* 635 */     for (int kk = 0; kk < nContours; kk++) {
/* 636 */       cntStrings[kk] = getLabelForValue(cmap, contourValue[kk]);
/*     */     }
/*     */ 
/* 639 */     Contours gridContours = ((Contours)this.currentGraph).createContours(nContours, nContourPts, 
/* 640 */       latlons, cntStrings, Color.green);
/*     */ 
/* 642 */     if (gridContours.getContourLines().size() > 0)
/*     */     {
/* 644 */       String dispOpt = getParamValues(this.gridParameters, "DISPOPT");
/* 645 */       boolean dispAsGhost = true;
/* 646 */       if ((dispOpt != null) && (dispOpt.equalsIgnoreCase("FALSE"))) {
/* 647 */         dispAsGhost = false;
/*     */       }
/*     */ 
/* 650 */       PgenResource drawingLayer = PgenSession.getInstance().getPgenResource();
/* 651 */       if (dispAsGhost) {
/* 652 */         drawingLayer.setGhostLine(gridContours);
/*     */       }
/*     */       else {
/* 655 */         drawingLayer.addElement(gridContours);
/*     */       }
/*     */ 
/* 658 */       PgenUtil.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   private Contours contoursFromBounds(String bnds, CoordinateTransform gtrans, float[] hist, Coordinate[] grdPts, int kx, int ky)
/*     */   {
/* 675 */     ArrayList boundPolys = BoundPolygon.getAllBounds(bnds);
/*     */ 
/* 677 */     Contours bndContours = new Contours();
/*     */ 
/* 682 */     int ks = 0;
/* 683 */     Boolean inout = Boolean.valueOf(true);
/*     */ 
/* 685 */     for (BoundPolygon bpoly : boundPolys)
/*     */     {
/* 687 */       inout = bpoly.getInout();
/*     */       int jj;
/* 688 */       for (Iterator localIterator2 = bpoly.getBoundPolygons().iterator(); localIterator2.hasNext(); 
/* 695 */         jj < ky)
/*     */       {
/* 688 */         MultiPolygon mpoly = (MultiPolygon)localIterator2.next();
/* 689 */         ks++;
/*     */ 
/* 695 */         jj = 0; continue;
/* 696 */         for (int ii = 0; ii < kx; ii++)
/*     */         {
/* 698 */           Point p = this.geometryFactory.createPoint(grdPts[(ii + jj * kx)]);
/*     */ 
/* 700 */           if ((hist[(ii + jj * kx)] != 1.0F) && 
/* 701 */             ((BoundPolygon.pointInPolygon(p, mpoly) ^ inout.booleanValue())))
/* 702 */             hist[(ii + jj * kx)] = 1.0F;
/*     */         }
/* 695 */         jj++;
/*     */       }
/*     */ 
/* 713 */       Contours bContours = bpoly.getBoundsAsContours();
/* 714 */       if (bContours.getContourLines().size() > 0) {
/* 715 */         for (ContourLine cline : bContours.getContourLines()) {
/* 716 */           bndContours.add(cline);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 722 */     return bndContours;
/*     */   }
/*     */ 
/*     */   private void checkSpecialBounds(Contours cnt, float[] hist, int kx, int ky, CoordinateTransform gtrans, Coordinate[] grdPts, CatMap cmap)
/*     */   {
/* 741 */     ArrayList cntline = new ArrayList();
/* 742 */     if (cnt != null) {
/* 743 */       cntline = cnt.getContourLines();
/*     */     }
/*     */ 
/* 747 */     ArrayList polys = new ArrayList();
/* 748 */     boolean[] inout = new boolean[cntline.size()];
/*     */ 
/* 750 */     int nb = 0;
/*     */     String[] label;
/* 751 */     for (ContourLine cline : cntline) {
/* 752 */       Line ln = cline.getLine();
/* 753 */       label = cline.getLabelString();
/*     */ 
/* 755 */       float lblValue = getValueForLabel(cmap, label[0]);
/*     */ 
/* 757 */       if ((ln.isClosedLine().booleanValue()) && ((lblValue == -9999.0F) || 
/* 758 */         (lblValue == 9999.0F))) {
/* 759 */         ArrayList coords = ln.getPoints();
/* 760 */         coords.add(new Coordinate(((Coordinate)coords.get(0)).x, ((Coordinate)coords.get(0)).y));
/*     */ 
/* 762 */         Coordinate[] cd = new Coordinate[coords.size()];
/* 763 */         CoordinateSequence sequence = new CoordinateArraySequence((Coordinate[])coords.toArray(cd));
/* 764 */         LinearRing ring = new LinearRing(sequence, this.geometryFactory);
/* 765 */         Polygon g = new Polygon(ring, null, this.geometryFactory);
/*     */ 
/* 767 */         polys.add(g);
/*     */ 
/* 769 */         if (lblValue == -9999.0F) {
/* 770 */           inout[nb] = true;
/*     */         }
/*     */         else {
/* 773 */           inout[nb] = false;
/*     */         }
/*     */ 
/* 776 */         nb++;
/*     */       }
/*     */     }
/*     */ 
/* 780 */     if (polys.size() > 0)
/*     */     {
/* 786 */       int mb = 0;
/* 787 */       for (Polygon poly : polys)
/*     */       {
/* 793 */         for (int jj = 0; jj < ky; jj++) {
/* 794 */           for (int ii = 0; ii < kx; ii++)
/*     */           {
/* 796 */             Point p = this.geometryFactory.createPoint(grdPts[(ii + jj * kx)]);
/*     */ 
/* 798 */             if (hist[(ii + jj * kx)] != 1.0F) {
/* 799 */               if (((poly.contains(p) ? 0 : 1) ^ inout[mb]) != 0) {
/* 800 */                 hist[(ii + jj * kx)] = 1.0F;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 806 */         mb++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawBoundsAndGrid(Contours cnt, float[] hist, int kx, int ky, Coordinate[] grdPts)
/*     */   {
/* 818 */     PgenResource drawingLayer = PgenSession.getInstance().getPgenResource();
/*     */ 
/* 824 */     DECollection nde = new DECollection();
/* 825 */     nde.setPgenCategory("");
/* 826 */     nde.setPgenType("");
/* 827 */     for (int jj = 0; jj < ky; jj++) {
/* 828 */       for (int ii = 0; ii < kx; ii++)
/*     */       {
/* 830 */         if (hist[(ii + jj * kx)] == 1.0F) {
/* 831 */           nde.add(new Symbol(null, new Color[] { Color.red }, 0.5F, 0.5D, 
/* 832 */             Boolean.valueOf(false), grdPts[(ii + jj * kx)], "Marker", "FILLED_BOX"));
/*     */         }
/*     */         else {
/* 835 */           nde.add(new Symbol(null, new Color[] { Color.blue }, 0.5F, 0.5D, 
/* 836 */             Boolean.valueOf(false), grdPts[(ii + jj * kx)], "Marker", "FILLED_BOX"));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 845 */     if ((cnt != null) && (cnt.getContourLines().size() > 0)) {
/* 846 */       drawingLayer.addElement(cnt);
/*     */     }
/*     */ 
/* 849 */     drawingLayer.addElement(nde);
/*     */   }
/*     */ 
/*     */   private ArrayList<Coordinate> generateArcPoints(Arc arc, double interval)
/*     */   {
/* 861 */     ArrayList points = new ArrayList();
/*     */ 
/* 863 */     PgenResource drawingLayer = PgenSession.getInstance().getPgenResource();
/*     */ 
/* 868 */     double[] tmp = { arc.getCenterPoint().x, arc.getCenterPoint().y, 0.0D };
/* 869 */     double[] center = ((MapDescriptor)drawingLayer.getDescriptor()).worldToPixel(tmp);
/* 870 */     double[] tmp2 = { arc.getCircumferencePoint().x, arc.getCircumferencePoint().y, 0.0D };
/* 871 */     double[] circum = ((MapDescriptor)drawingLayer.getDescriptor()).worldToPixel(tmp2);
/*     */ 
/* 876 */     double axisAngle = Math.toDegrees(Math.atan2(circum[1] - center[1], circum[0] - center[0]));
/* 877 */     double cosineAxis = Math.cos(Math.toRadians(axisAngle));
/* 878 */     double sineAxis = Math.sin(Math.toRadians(axisAngle));
/*     */ 
/* 883 */     double[] diff = { circum[0] - center[0], circum[1] - center[1] };
/* 884 */     double major = Math.sqrt(diff[0] * diff[0] + diff[1] * diff[1]);
/* 885 */     double minor = major * arc.getAxisRatio();
/*     */ 
/* 890 */     double increment = interval;
/* 891 */     double angle = arc.getStartAngle();
/* 892 */     int numpts = (int)(Math.round(arc.getEndAngle() - arc.getStartAngle() + 1.0D) / increment);
/*     */ 
/* 894 */     double[][] path = new double[numpts][3];
/* 895 */     for (int j = 0; j < numpts; j++) {
/* 896 */       double thisSine = Math.sin(Math.toRadians(angle));
/* 897 */       double thisCosine = Math.cos(Math.toRadians(angle));
/*     */ 
/* 905 */       path[j][0] = 
/* 906 */         (center[0] + major * cosineAxis * thisCosine - 
/* 906 */         minor * sineAxis * thisSine);
/* 907 */       path[j][1] = 
/* 908 */         (center[1] + major * sineAxis * thisCosine + 
/* 908 */         minor * cosineAxis * thisSine);
/*     */ 
/* 911 */       double[] pt = ((MapDescriptor)drawingLayer.getDescriptor()).pixelToWorld(
/* 912 */         new double[] { path[j][0], path[j][1], 0.0D });
/*     */ 
/* 914 */       points.add(new Coordinate(pt[0], pt[1]));
/*     */ 
/* 916 */       angle += increment;
/*     */     }
/*     */ 
/* 919 */     return points;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.graphtogrid.ContoursToGrid
 * JD-Core Version:    0.6.2
 */