/*     */ package gov.noaa.nws.ncep.ui.pgen.graphtogrid;
/*     */ 
/*     */ import com.raytheon.uf.common.geospatial.MapUtil;
/*     */ import com.raytheon.uf.viz.core.map.MapDescriptor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.gempak.parameters.core.marshaller.garea.GraphicsAreaCoordinates;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.viz.common.customprojection.CustomProjectionServiceImpl;
/*     */ import java.io.File;
/*     */ import org.geotools.coverage.grid.GeneralGridGeometry;
/*     */ import org.geotools.referencing.operation.DefaultMathTransformFactory;
/*     */ import org.opengis.referencing.crs.CoordinateReferenceSystem;
/*     */ import org.opengis.referencing.operation.MathTransform;
/*     */ 
/*     */ public class CoordinateTransform
/*     */ {
/*  59 */   private final String geogFilePath = PgenStaticDataProvider.getProvider().getGeogFile().getAbsolutePath();
/*     */ 
/*  61 */   private final String stationFilePath = PgenStaticDataProvider.getProvider().getSfcStnFile().getAbsolutePath();
/*     */   private static final int MAXDIM = 2000;
/*     */   private static final int LLMXTG = 1000000;
/*  74 */   private static DefaultMathTransformFactory dmtFactory = new DefaultMathTransformFactory();
/*     */ 
/*  95 */   private String projection = null;
/*  96 */   private String garea = null;
/*  97 */   private int kx = -9999;
/*  98 */   private int ky = -9999;
/*  99 */   private Coordinate[] extent = null;
/*     */ 
/* 105 */   private MathTransform worldToGeneralGrid = null;
/* 106 */   private MathTransform generalGridToWorld = null;
/* 107 */   private double xsize = -9999.0D;
/* 108 */   private double ysize = -9999.0D;
/* 109 */   private double xscale = -9999.0D;
/* 110 */   private double yscale = -9999.0D;
/*     */ 
/*     */   public CoordinateTransform(String proj, String garea, int kx, int ky)
/*     */   {
/* 118 */     this.projection = proj;
/* 119 */     this.garea = garea;
/* 120 */     this.kx = kx;
/* 121 */     this.ky = ky;
/*     */ 
/* 123 */     findTransform();
/*     */   }
/*     */ 
/*     */   public String getProjection()
/*     */   {
/* 131 */     return this.projection;
/*     */   }
/*     */ 
/*     */   public void setProjection(String projection)
/*     */   {
/* 138 */     this.projection = projection;
/* 139 */     findTransform();
/*     */   }
/*     */ 
/*     */   public String getGarea()
/*     */   {
/* 146 */     return this.garea;
/*     */   }
/*     */ 
/*     */   public void setGarea(String garea)
/*     */   {
/* 153 */     this.garea = garea;
/* 154 */     findTransform();
/*     */   }
/*     */ 
/*     */   public int getKx()
/*     */   {
/* 161 */     return this.kx;
/*     */   }
/*     */ 
/*     */   public void setKx(int kx)
/*     */   {
/* 168 */     this.kx = kx;
/* 169 */     findTransform();
/*     */   }
/*     */ 
/*     */   public int getKy()
/*     */   {
/* 176 */     return this.ky;
/*     */   }
/*     */ 
/*     */   public void setKy(int ky)
/*     */   {
/* 183 */     this.ky = ky;
/* 184 */     findTransform();
/*     */   }
/*     */ 
/*     */   public Coordinate[] getExtent()
/*     */   {
/* 191 */     return this.extent;
/*     */   }
/*     */ 
/*     */   private void findTransform()
/*     */   {
/* 205 */     if ((this.projection == null) || (this.projection.length() == 0)) {
/* 206 */       this.projection = "DEF";
/*     */     }
/*     */ 
/* 210 */     if ((this.garea == null) || (this.garea.length() == 0)) {
/* 211 */       this.garea = "US";
/*     */     }
/*     */ 
/* 215 */     if (this.kx <= 0) {
/* 216 */       this.kx = 63;
/*     */     }
/*     */ 
/* 220 */     if (this.ky <= 0) {
/* 221 */       this.ky = 28;
/*     */     }
/*     */ 
/* 225 */     if ((this.kx * this.ky > 1000000) || (this.kx + this.ky > 2000)) {
/* 226 */       this.kx = 63;
/* 227 */       this.ky = 28;
/*     */     }
/*     */ 
/* 237 */     GraphicsAreaCoordinates gareaCoordObj = new GraphicsAreaCoordinates(this.garea);
/*     */ 
/* 239 */     gareaCoordObj.setGeogFileName(this.geogFilePath);
/* 240 */     gareaCoordObj.setStationFileName(this.stationFilePath);
/*     */ 
/* 242 */     boolean tempParseFlag = gareaCoordObj.parseGraphicsAreaString(this.garea);
/* 243 */     gareaCoordObj.setGraphicsAreaStrValid(tempParseFlag);
/*     */     try
/*     */     {
/* 249 */       Coordinate ll = new Coordinate();
/* 250 */       Coordinate ur = new Coordinate();
/*     */ 
/* 252 */       ll.x = gareaCoordObj.getLowerLeftLon();
/* 253 */       ll.y = gareaCoordObj.getLowerLeftLat();
/*     */ 
/* 255 */       ur.x = gareaCoordObj.getUpperRightLon();
/* 256 */       ur.y = gareaCoordObj.getUpperRightLat();
/*     */ 
/* 258 */       this.extent = new Coordinate[2];
/* 259 */       this.extent[0] = ll;
/* 260 */       this.extent[1] = ur;
/*     */       try
/*     */       {
/* 264 */         CustomProjectionServiceImpl customProj = 
/* 265 */           new CustomProjectionServiceImpl(this.projection, this.garea, 
/* 266 */           this.geogFilePath, this.stationFilePath);
/* 267 */         CoordinateReferenceSystem crs = customProj.getCoordinateReferenceSystem();
/* 268 */         GeneralGridGeometry gridGeom = MapDescriptor.createGridGeometry(crs, ll, ur);
/*     */ 
/* 274 */         MathTransform g2c = gridGeom.getGridToCRS();
/* 275 */         MathTransform c2g = gridGeom.getGridToCRS().inverse();
/*     */ 
/* 277 */         MathTransform c2m = MapUtil.getTransformToLatLon(crs);
/* 278 */         MathTransform m2c = MapUtil.getTransformFromLatLon(crs);
/*     */ 
/* 280 */         this.worldToGeneralGrid = dmtFactory.createConcatenatedTransform(m2c, c2g);
/* 281 */         this.generalGridToWorld = dmtFactory.createConcatenatedTransform(g2c, c2m);
/*     */ 
/* 287 */         double[] pts = { ll.x, ll.y, ur.x, ur.y };
/* 288 */         double[] outp = new double[pts.length];
/*     */ 
/* 290 */         this.worldToGeneralGrid.transform(pts, 0, outp, 0, pts.length / 2);
/*     */ 
/* 292 */         this.xsize = Math.abs(outp[2] - outp[0]);
/* 293 */         this.ysize = Math.abs(outp[3] - outp[1]);
/*     */ 
/* 295 */         this.xscale = (this.xsize / (this.kx - 1));
/* 296 */         this.yscale = (this.ysize / (this.ky - 1));
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 300 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 305 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public double[] worldToGrid(double[] lonlat)
/*     */   {
/* 319 */     double[] gridout = null;
/*     */ 
/* 321 */     int numPts = lonlat == null ? 0 : lonlat.length / 2;
/*     */ 
/* 323 */     if ((numPts > 0) && (this.worldToGeneralGrid != null))
/*     */     {
/* 325 */       double[] outp = new double[numPts * 2];
/*     */       try
/*     */       {
/* 331 */         this.worldToGeneralGrid.transform(lonlat, 0, outp, 0, numPts);
/*     */       }
/*     */       catch (Exception e) {
/* 334 */         e.printStackTrace();
/*     */       }
/*     */ 
/* 340 */       if ((outp != null) && (outp.length > 0)) {
/* 341 */         gridout = new double[numPts * 2];
/* 342 */         for (int ii = 0; ii < numPts; ii++) {
/* 343 */           gridout[(ii * 2)] = ((outp[(ii * 2)] + 0.5D) / this.xscale + 1.0D);
/* 344 */           gridout[(ii * 2 + 1)] = ((this.ysize - outp[(ii * 2 + 1)] - 0.5D) / this.yscale + 1.0D);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 350 */     return gridout;
/*     */   }
/*     */ 
/*     */   public double[] gridToWorld(double[] gridin)
/*     */   {
/* 365 */     double[] lonlat = null;
/* 366 */     int numPts = gridin == null ? 0 : gridin.length / 2;
/*     */ 
/* 368 */     if ((numPts > 0) && (this.generalGridToWorld != null))
/*     */     {
/* 374 */       double[] outp = new double[numPts * 2];
/*     */ 
/* 376 */       for (int ii = 0; ii < numPts; ii++) {
/* 377 */         outp[(ii * 2)] = ((gridin[(ii * 2)] - 1.0D) * this.xscale - 0.5D);
/* 378 */         outp[(ii * 2 + 1)] = (this.ysize - (gridin[(ii * 2 + 1)] - 1.0D) * this.yscale - 0.5D);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 386 */         lonlat = new double[numPts * 2];
/* 387 */         this.generalGridToWorld.transform(outp, 0, lonlat, 0, numPts);
/*     */       }
/*     */       catch (Exception e) {
/* 390 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 395 */     return lonlat;
/*     */   }
/*     */ 
/*     */   public double[] worldToGeneralGrid(double[] lonlat)
/*     */   {
/* 406 */     double[] gridout = null;
/*     */ 
/* 408 */     int numPts = lonlat == null ? 0 : lonlat.length / 2;
/*     */ 
/* 410 */     if ((numPts > 0) && (this.worldToGeneralGrid != null))
/*     */     {
/* 412 */       gridout = new double[numPts * 2];
/*     */       try
/*     */       {
/* 418 */         this.worldToGeneralGrid.transform(lonlat, 0, gridout, 0, numPts);
/*     */       }
/*     */       catch (Exception e) {
/* 421 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 426 */     return gridout;
/*     */   }
/*     */ 
/*     */   public double[] generalGridToWorld(double[] gridin)
/*     */   {
/* 440 */     double[] lonlat = null;
/* 441 */     int numPts = gridin == null ? 0 : gridin.length / 2;
/*     */ 
/* 443 */     if ((numPts > 0) && (this.generalGridToWorld != null))
/*     */     {
/*     */       try
/*     */       {
/* 449 */         lonlat = new double[numPts * 2];
/* 450 */         this.generalGridToWorld.transform(gridin, 0, lonlat, 0, numPts);
/*     */       }
/*     */       catch (Exception e) {
/* 453 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 458 */     return lonlat;
/*     */   }
/*     */ 
/*     */   public Coordinate[] worldToGeneralGrid(Coordinate[] lonlat)
/*     */   {
/* 467 */     Coordinate[] gridout = null;
/*     */ 
/* 469 */     int numPts = lonlat == null ? 0 : lonlat.length;
/*     */ 
/* 471 */     double[] ptsin = new double[numPts * 2];
/* 472 */     for (int ii = 0; ii < numPts; ii++) {
/* 473 */       ptsin[(ii * 2)] = lonlat[ii].x;
/* 474 */       ptsin[(ii * 2 + 1)] = lonlat[ii].y;
/*     */     }
/*     */ 
/* 477 */     double[] ptsout = worldToGeneralGrid(ptsin);
/* 478 */     if ((ptsout != null) && (ptsout.length > 0)) {
/* 479 */       gridout = new Coordinate[numPts];
/* 480 */       for (int ii = 0; ii < numPts; ii++) {
/* 481 */         gridout[ii] = new Coordinate(ptsout[(ii * 2)], ptsout[(ii * 2 + 1)]);
/*     */       }
/*     */     }
/*     */ 
/* 485 */     return gridout;
/*     */   }
/*     */ 
/*     */   public Coordinate[] generalGridToWorld(Coordinate[] gridpts)
/*     */   {
/* 494 */     Coordinate[] gridout = null;
/*     */ 
/* 496 */     int numPts = gridpts == null ? 0 : gridpts.length;
/*     */ 
/* 498 */     double[] ptsin = new double[numPts * 2];
/* 499 */     for (int ii = 0; ii < numPts; ii++) {
/* 500 */       ptsin[(ii * 2)] = gridpts[ii].x;
/* 501 */       ptsin[(ii * 2 + 1)] = gridpts[ii].y;
/*     */     }
/*     */ 
/* 504 */     double[] ptsout = generalGridToWorld(ptsin);
/*     */ 
/* 506 */     if ((ptsout != null) && (ptsout.length > 0)) {
/* 507 */       gridout = new Coordinate[numPts];
/* 508 */       for (int ii = 0; ii < numPts; ii++) {
/* 509 */         gridout[ii] = new Coordinate(ptsout[(ii * 2)], ptsout[(ii * 2 + 1)]);
/*     */       }
/*     */     }
/*     */ 
/* 513 */     return gridout;
/*     */   }
/*     */ 
/*     */   public Coordinate[] gridToWorld(Coordinate[] gridpts)
/*     */   {
/* 522 */     Coordinate[] gridout = null;
/*     */ 
/* 524 */     int numPts = gridpts == null ? 0 : gridpts.length;
/*     */ 
/* 526 */     double[] ptsin = new double[numPts * 2];
/* 527 */     for (int ii = 0; ii < numPts; ii++) {
/* 528 */       ptsin[(ii * 2)] = gridpts[ii].x;
/* 529 */       ptsin[(ii * 2 + 1)] = gridpts[ii].y;
/*     */     }
/*     */ 
/* 532 */     double[] ptsout = gridToWorld(ptsin);
/*     */ 
/* 534 */     if ((ptsout != null) && (ptsout.length > 0)) {
/* 535 */       gridout = new Coordinate[numPts];
/* 536 */       for (int ii = 0; ii < numPts; ii++) {
/* 537 */         gridout[ii] = new Coordinate(ptsout[(ii * 2)], ptsout[(ii * 2 + 1)]);
/*     */       }
/*     */     }
/*     */ 
/* 541 */     return gridout;
/*     */   }
/*     */ 
/*     */   public Coordinate[] worldToGrid(Coordinate[] lonlat)
/*     */   {
/* 550 */     Coordinate[] gridout = null;
/*     */ 
/* 552 */     int numPts = lonlat == null ? 0 : lonlat.length;
/*     */ 
/* 554 */     double[] ptsin = new double[numPts * 2];
/* 555 */     for (int ii = 0; ii < numPts; ii++) {
/* 556 */       ptsin[(ii * 2)] = lonlat[ii].x;
/* 557 */       ptsin[(ii * 2 + 1)] = lonlat[ii].y;
/*     */     }
/*     */ 
/* 560 */     double[] ptsout = worldToGrid(ptsin);
/* 561 */     if ((ptsout != null) && (ptsout.length > 0)) {
/* 562 */       gridout = new Coordinate[numPts];
/* 563 */       for (int ii = 0; ii < numPts; ii++) {
/* 564 */         gridout[ii] = new Coordinate(ptsout[(ii * 2)], ptsout[(ii * 2 + 1)]);
/*     */       }
/*     */     }
/*     */ 
/* 568 */     return gridout;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.graphtogrid.CoordinateTransform
 * JD-Core Version:    0.6.2
 */