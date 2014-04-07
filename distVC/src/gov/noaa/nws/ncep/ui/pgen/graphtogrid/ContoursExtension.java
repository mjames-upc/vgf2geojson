/*     */ package gov.noaa.nws.ncep.ui.pgen.graphtogrid;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*     */ import com.raytheon.uf.viz.core.map.MapDescriptor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.gempak.parameters.core.categorymap.CatMap;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.CurveFitter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class ContoursExtension
/*     */ {
/*     */   private Contours contour;
/*     */   private CoordinateTransform gtrans;
/*     */   private int kx;
/*     */   private int ky;
/*     */   private Color[] extColors;
/*     */   private CatMap catmap;
/*  60 */   private int nlines = 0;
/*  61 */   private int[] npts = null;
/*  62 */   private float[][] flat = null;
/*  63 */   private float[][] flon = null;
/*  64 */   private int[] nOrigPts = null;
/*  65 */   private float[][] fi_orig = null;
/*  66 */   private float[][] fj_orig = null;
/*  67 */   private int[] nExtPts = null;
/*  68 */   private float[][] fi_ext = null;
/*  69 */   private float[][] fj_ext = null;
/*     */ 
/*  71 */   private float[] value = null;
/*  72 */   private int[] closed = null;
/*  73 */   private int[] ismth = null;
/*     */ 
/*  76 */   private ArrayList<AbstractDrawableComponent> extLines = null;
/*     */ 
/*     */   public ContoursExtension(Contours currentGraph, CoordinateTransform gtrans, int kx, int ky, Color[] extClr, CatMap cmap)
/*     */   {
/*  85 */     this.contour = currentGraph;
/*  86 */     this.gtrans = gtrans;
/*  87 */     this.extColors = extClr;
/*  88 */     setExtent(kx, ky);
/*     */ 
/*  90 */     setCatmap(cmap);
/*     */ 
/*  92 */     computeExtentions();
/*     */   }
/*     */ 
/*     */   public void setContour(Contours contour)
/*     */   {
/*  99 */     this.contour = contour;
/*     */   }
/*     */ 
/*     */   public void setGtrans(CoordinateTransform gtrans)
/*     */   {
/* 106 */     this.gtrans = gtrans;
/*     */   }
/*     */ 
/*     */   public void setKx(int kx)
/*     */   {
/* 113 */     this.kx = kx;
/*     */   }
/*     */ 
/*     */   public void setKy(int ky)
/*     */   {
/* 120 */     this.ky = ky;
/*     */   }
/*     */ 
/*     */   public void setExtColors(Color[] extColors)
/*     */   {
/* 127 */     this.extColors = extColors;
/*     */   }
/*     */ 
/*     */   public CatMap getCatmap()
/*     */   {
/* 135 */     return this.catmap;
/*     */   }
/*     */ 
/*     */   public void setCatmap(CatMap catmap)
/*     */   {
/* 142 */     this.catmap = catmap;
/*     */   }
/*     */ 
/*     */   public void setExtent(int kx, int ky)
/*     */   {
/* 149 */     this.kx = kx;
/* 150 */     this.ky = ky;
/*     */   }
/*     */ 
/*     */   public ArrayList<AbstractDrawableComponent> getExtLines()
/*     */   {
/* 158 */     return this.extLines;
/*     */   }
/*     */ 
/*     */   public int getNlines()
/*     */   {
/* 165 */     return this.nlines;
/*     */   }
/*     */ 
/*     */   public int[] getNpts()
/*     */   {
/* 172 */     return this.npts;
/*     */   }
/*     */ 
/*     */   public float[][] getFlat()
/*     */   {
/* 179 */     return this.flat;
/*     */   }
/*     */ 
/*     */   public float[][] getFlon()
/*     */   {
/* 186 */     return this.flon;
/*     */   }
/*     */ 
/*     */   public void setNOrigPts(int[] nOrigPts)
/*     */   {
/* 193 */     this.nOrigPts = nOrigPts;
/*     */   }
/*     */ 
/*     */   public int[] getNOrigPts()
/*     */   {
/* 200 */     return this.nOrigPts;
/*     */   }
/*     */ 
/*     */   public float[][] getFiOrig()
/*     */   {
/* 207 */     return this.fi_orig;
/*     */   }
/*     */ 
/*     */   public float[][] getFjOrig()
/*     */   {
/* 214 */     return this.fj_orig;
/*     */   }
/*     */ 
/*     */   public void setNExtPts(int[] nExtPts)
/*     */   {
/* 221 */     this.nExtPts = nExtPts;
/*     */   }
/*     */ 
/*     */   public int[] getNExtPts()
/*     */   {
/* 228 */     return this.nExtPts;
/*     */   }
/*     */ 
/*     */   public float[][] getFiExt()
/*     */   {
/* 235 */     return this.fi_ext;
/*     */   }
/*     */ 
/*     */   public float[][] getFjExt()
/*     */   {
/* 242 */     return this.fj_ext;
/*     */   }
/*     */ 
/*     */   public float[] getValue()
/*     */   {
/* 248 */     return this.value;
/*     */   }
/*     */ 
/*     */   public int[] getClosed()
/*     */   {
/* 255 */     return this.closed;
/*     */   }
/*     */ 
/*     */   public int[] getIsmth()
/*     */   {
/* 262 */     return this.ismth;
/*     */   }
/*     */ 
/*     */   private void computeExtentions()
/*     */   {
/* 270 */     ArrayList clines = this.contour.getContourLines();
/*     */ 
/* 272 */     this.nlines = clines.size();
/* 273 */     this.npts = new int[this.nlines];
/* 274 */     this.nOrigPts = new int[this.nlines];
/* 275 */     this.nExtPts = new int[this.nlines];
/* 276 */     this.value = new float[this.nlines];
/* 277 */     this.closed = new int[this.nlines];
/* 278 */     this.ismth = new int[this.nlines];
/* 279 */     this.flat = new float[this.nlines][];
/* 280 */     this.flon = new float[this.nlines][];
/* 281 */     this.fi_orig = new float[this.nlines][];
/* 282 */     this.fj_orig = new float[this.nlines][];
/* 283 */     this.fi_ext = new float[this.nlines][];
/* 284 */     this.fj_ext = new float[this.nlines][];
/*     */ 
/* 286 */     this.extLines = new ArrayList();
/*     */ 
/* 288 */     int ii = 0;
/* 289 */     for (ContourLine cline : clines)
/*     */     {
/* 292 */       Line line = ensureClosed(cline.getLine());
/*     */ 
/* 294 */       Coordinate[] linePts = line.getLinePoints();
/* 295 */       this.npts[ii] = linePts.length;
/*     */ 
/* 297 */       this.flat[ii] = new float[this.npts[ii]];
/* 298 */       this.flon[ii] = new float[this.npts[ii]];
/*     */ 
/* 300 */       for (int jj = 0; jj < this.npts[ii]; jj++) {
/* 301 */         this.flat[ii][jj] = ((float)linePts[jj].y);
/* 302 */         this.flon[ii][jj] = ((float)linePts[jj].x);
/*     */       }
/*     */ 
/* 310 */       this.ismth[ii] = line.getSmoothFactor();
/* 311 */       this.closed[ii] = (line.isClosedLine().booleanValue() ? 1 : 0);
/* 312 */       this.value[ii] = GraphToGrid.getValueForLabel(this.catmap, cline.getLabelString()[0]);
/*     */ 
/* 317 */       smoothLine(line, ii);
/*     */ 
/* 322 */       computeLineExtentions(line, ii);
/*     */ 
/* 324 */       ii++;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void smoothLine(Line ln, int index)
/*     */   {
/* 335 */     if (ln != null)
/*     */     {
/* 337 */       PgenResource drawingLayer = PgenSession.getInstance().getPgenResource();
/*     */ 
/* 342 */       Coordinate[] pts = ln.getLinePoints();
/*     */ 
/* 350 */       double[][] pixels = PgenUtil.latlonToPixel(pts, (IMapDescriptor)drawingLayer.getDescriptor());
/*     */       double[][] smoothpts;
/*     */       double[][] smoothpts;
/* 355 */       if (ln.getSmoothFactor() > 0) {
/* 356 */         float devScale = 50.0F;
/*     */         float density;
/*     */         float density;
/* 357 */         if (ln.getSmoothFactor() == 1) {
/* 358 */           density = devScale / 1.0F;
/*     */         }
/*     */         else {
/* 361 */           density = devScale / 5.0F;
/*     */         }
/*     */ 
/* 364 */         smoothpts = CurveFitter.fitParametricCurve(pixels, density);
/*     */       }
/*     */       else {
/* 367 */         smoothpts = pixels;
/*     */       }
/*     */ 
/* 373 */       int origPts = smoothpts.length;
/*     */ 
/* 375 */       this.nOrigPts[index] = origPts;
/*     */ 
/* 377 */       this.fi_orig[index] = new float[origPts];
/* 378 */       this.fj_orig[index] = new float[origPts];
/*     */ 
/* 381 */       double[] ptsIn = new double[origPts * 2];
/*     */ 
/* 383 */       for (int kk = 0; kk < smoothpts.length; kk++)
/*     */       {
/* 385 */         double[] pt = ((MapDescriptor)drawingLayer.getDescriptor()).pixelToWorld(
/* 386 */           new double[] { smoothpts[kk][0], smoothpts[kk][1], 0.0D });
/*     */ 
/* 388 */         ptsIn[(kk * 2)] = pt[0];
/* 389 */         ptsIn[(kk * 2 + 1)] = pt[1];
/*     */       }
/*     */ 
/* 393 */       double[] outp = this.gtrans.worldToGrid(ptsIn);
/*     */ 
/* 395 */       for (int jj = 0; jj < origPts; jj++) {
/* 396 */         this.fi_orig[index][jj] = ((float)outp[(jj * 2 + 1)]);
/* 397 */         this.fj_orig[index][jj] = ((float)outp[(jj * 2)]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void computeLineExtentions(Line ln, int index)
/*     */   {
/* 410 */     if (ln != null)
/*     */     {
/* 415 */       Coordinate p0 = null;
/* 416 */       Coordinate p1 = null;
/* 417 */       double[] newPt = null;
/* 418 */       double[] newPt2 = null;
/*     */ 
/* 420 */       if (!ln.isClosedLine().booleanValue())
/*     */       {
/* 422 */         newPt = extendLine(this.fj_orig[index][0], this.fi_orig[index][0], 
/* 423 */           this.fj_orig[index][1], this.fi_orig[index][1], 
/* 424 */           this.kx, this.ky);
/*     */ 
/* 426 */         if (newPt != null)
/*     */         {
/* 428 */           double[] lonlat = this.gtrans.gridToWorld(newPt);
/*     */ 
/* 430 */           p0 = new Coordinate(lonlat[0], lonlat[1]);
/*     */ 
/* 432 */           Line firstSeg = (Line)ln.copy();
/*     */ 
/* 434 */           ArrayList twoPts = new ArrayList();
/* 435 */           twoPts.add(p0);
/* 436 */           twoPts.add(ln.getLinePoints()[0]);
/*     */ 
/* 438 */           firstSeg.setPointsOnly(twoPts);
/* 439 */           if (this.extColors != null) firstSeg.setColors(this.extColors);
/*     */ 
/* 441 */           this.extLines.add(firstSeg);
/*     */         }
/*     */ 
/* 448 */         int np = this.fi_orig[index].length;
/* 449 */         newPt2 = extendLine(this.fj_orig[index][(np - 1)], this.fi_orig[index][(np - 1)], 
/* 450 */           this.fj_orig[index][(np - 2)], this.fi_orig[index][(np - 2)], 
/* 451 */           this.kx, this.ky);
/*     */ 
/* 453 */         if (newPt2 != null)
/*     */         {
/* 455 */           double[] lonlat = this.gtrans.gridToWorld(newPt2);
/*     */ 
/* 457 */           p1 = new Coordinate(lonlat[0], lonlat[1]);
/*     */ 
/* 459 */           Line secondSeg = (Line)ln.copy();
/*     */ 
/* 461 */           ArrayList seg2 = new ArrayList();
/* 462 */           seg2.add(p1);
/*     */ 
/* 464 */           int np1 = ln.getLinePoints().length;
/* 465 */           seg2.add(ln.getLinePoints()[(np1 - 1)]);
/*     */ 
/* 467 */           secondSeg.setPointsOnly(seg2);
/* 468 */           if (this.extColors != null) secondSeg.setColors(this.extColors);
/*     */ 
/* 470 */           this.extLines.add(secondSeg);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 478 */       int extPts = this.fi_orig[index].length;
/* 479 */       if (p0 != null) extPts++;
/* 480 */       if (p1 != null) extPts++;
/*     */ 
/* 482 */       this.nExtPts[index] = extPts;
/*     */ 
/* 484 */       this.fi_ext[index] = new float[extPts];
/* 485 */       this.fj_ext[index] = new float[extPts];
/*     */ 
/* 487 */       int npp = 0;
/* 488 */       if (newPt != null) {
/* 489 */         this.fi_ext[index][npp] = ((float)newPt[1]);
/* 490 */         this.fj_ext[index][npp] = ((float)newPt[0]);
/* 491 */         npp++;
/*     */       }
/*     */ 
/* 494 */       for (int kk = 0; kk < this.fi_orig[index].length; kk++) {
/* 495 */         this.fi_ext[index][npp] = this.fi_orig[index][kk];
/* 496 */         this.fj_ext[index][npp] = this.fj_orig[index][kk];
/* 497 */         npp++;
/*     */       }
/*     */ 
/* 500 */       if (newPt2 != null) {
/* 501 */         this.fi_ext[index][npp] = ((float)newPt2[1]);
/* 502 */         this.fj_ext[index][npp] = ((float)newPt2[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private double[] extendLine(float px1, float py1, float px2, float py2, int kx, int ky)
/*     */   {
/* 519 */     double[] extPts = null;
/* 520 */     int minx = 1;
/* 521 */     int miny = 1;
/* 522 */     int maxx = kx;
/* 523 */     int maxy = ky;
/*     */ 
/* 526 */     if ((px1 >= minx) && (px1 <= maxx) && (py1 >= miny) && (py1 <= maxy))
/*     */     {
/* 530 */       extPts = new double[2];
/*     */       float ynew;
/*     */       float xnew;
/*     */       float ynew;
/* 532 */       if (px2 == px1) {
/* 533 */         float xnew = px1;
/*     */         float ynew;
/* 535 */         if (px1 < px2) {
/* 536 */           ynew = miny - 1;
/*     */         }
/*     */         else {
/* 539 */           ynew = maxy + 1;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 544 */         float m = (py2 - py1) / (px2 - px1);
/* 545 */         float b = py1 - m * px1;
/*     */         float xnew;
/* 547 */         if (px1 < px2) {
/* 548 */           xnew = minx - 1;
/*     */         }
/*     */         else {
/* 551 */           xnew = maxx + 1;
/*     */         }
/*     */ 
/* 554 */         ynew = m * xnew + b;
/*     */ 
/* 556 */         if (ynew > maxy) {
/* 557 */           ynew = maxy + 1;
/* 558 */           xnew = (ynew - b) / m;
/*     */         }
/* 560 */         else if (ynew < miny) {
/* 561 */           ynew = miny - 1;
/* 562 */           xnew = (ynew - b) / m;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 567 */       extPts[0] = xnew;
/* 568 */       extPts[1] = ynew;
/*     */     }
/*     */ 
/* 571 */     return extPts;
/*     */   }
/*     */ 
/*     */   private Line ensureClosed(Line ln)
/*     */   {
/* 583 */     Line newLine = (Line)ln.copy();
/*     */ 
/* 585 */     if (ln.isClosedLine().booleanValue()) {
/* 586 */       ArrayList pts = newLine.getPoints();
/* 587 */       int nn = pts.size() - 1;
/* 588 */       if ((((Coordinate)pts.get(0)).x != ((Coordinate)pts.get(nn)).x) || 
/* 589 */         (((Coordinate)pts.get(0)).y != ((Coordinate)pts.get(nn)).y))
/*     */       {
/* 591 */         pts.add((Coordinate)pts.get(0));
/*     */       }
/*     */     }
/*     */ 
/* 595 */     return newLine;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.graphtogrid.ContoursExtension
 * JD-Core Version:    0.6.2
 */