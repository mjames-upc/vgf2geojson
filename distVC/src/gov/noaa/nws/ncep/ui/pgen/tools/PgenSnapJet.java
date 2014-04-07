/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.IDisplayPane;
/*     */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.linearref.LinearLocation;
/*     */ import com.vividsolutions.jts.linearref.LocationIndexedLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.JetAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.CurveFitter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ISinglePoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector.VectorType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.IJetTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetBarb;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetHash;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class PgenSnapJet
/*     */   implements IJetTools
/*     */ {
/*     */   private IMapDescriptor descriptor;
/*     */   private AbstractEditor mapEditor;
/*     */   private JetAttrDlg jetDlg;
/*     */   private Jet.JetHash hashTmp;
/*     */   private static final double defaultZoomLevel = 0.24D;
/*     */ 
/*     */   public PgenSnapJet(IMapDescriptor des, AbstractEditor editor, JetAttrDlg dlg)
/*     */   {
/*  79 */     this.descriptor = des;
/*  80 */     this.mapEditor = editor;
/*  81 */     this.jetDlg = dlg;
/*     */   }
/*     */ 
/*     */   public void snapJet(Jet aJet)
/*     */   {
/*  91 */     double[][] lineCurvePts = getJetCurve(aJet);
/*     */ 
/*  93 */     Iterator it = aJet.getComponentIterator();
/*     */ 
/*  95 */     while (it.hasNext())
/*     */     {
/*  97 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/*  98 */       if (adc.getName().equalsIgnoreCase("WindInfo")) {
/*  99 */         snapBarbOnJet(adc, lineCurvePts);
/*     */       }
/*     */     }
/*     */ 
/* 103 */     snapAllHashs(aJet);
/*     */ 
/* 105 */     checkHashOnJet(aJet);
/*     */   }
/*     */ 
/*     */   public void snapJet(AbstractDrawableComponent barb, Jet aJet)
/*     */   {
/* 114 */     double[][] lineCurvePts = getJetCurve(aJet);
/* 115 */     snapBarbOnJet(barb, lineCurvePts);
/*     */ 
/* 119 */     checkHashOnJet(aJet);
/*     */   }
/*     */ 
/*     */   private double translateDir(double dir, double lat, double lon, boolean s2m)
/*     */   {
/* 132 */     double[] pt1 = this.descriptor.worldToPixel(new double[] { lon, lat });
/* 133 */     double[] pt2 = this.descriptor.worldToPixel(new double[] { lon, lat + 5.0D });
/*     */ 
/* 135 */     double delx = pt2[0] - pt1[0];
/* 136 */     double dely = pt2[1] - pt1[1];
/*     */     double rv;
/*     */     double rv;
/* 138 */     if ((delx == 0.0D) && (dely == 0.0D)) {
/* 139 */       rv = dir;
/*     */     }
/*     */     else {
/* 142 */       double rot = Math.atan2(delx, -dely);
/*     */       double rv;
/* 143 */       if (s2m) {
/* 144 */         rv = dir - rot * 180.0D / 3.141592653589793D;
/*     */       }
/*     */       else {
/* 147 */         rv = dir + rot * 180.0D / 3.141592653589793D;
/*     */       }
/*     */     }
/*     */ 
/* 151 */     if (rv > 360.0D) {
/* 152 */       rv -= 360.0D;
/*     */     }
/* 154 */     else if (rv < 0.0D) {
/* 155 */       rv += 360.0D;
/*     */     }
/*     */ 
/* 158 */     return rv;
/*     */   }
/*     */ 
/*     */   private double[][] getJetCurve(Jet aJet)
/*     */   {
/* 166 */     double[][] lineScnPts = new double[aJet.getJetLine().getLinePoints().length][2];
/*     */ 
/* 168 */     int ii = 0;
/* 169 */     for (Coordinate loc : aJet.getJetLine().getLinePoints()) {
/* 170 */       double[] pix = { loc.x, loc.y };
/* 171 */       lineScnPts[ii] = this.descriptor.worldToPixel(pix);
/* 172 */       ii++;
/*     */     }
/*     */ 
/* 175 */     return CurveFitter.fitParametricCurve(lineScnPts, 0.5F);
/*     */   }
/*     */ 
/*     */   private int getNearestPtOnCurve(double[][] lineCurvePts, double[] pt)
/*     */   {
/* 183 */     double minDistance = -999.0D;
/* 184 */     int nearestId = 0;
/*     */ 
/* 186 */     for (int ii = 0; ii < lineCurvePts.length; ii++)
/*     */     {
/* 188 */       double dist = Math.sqrt((pt[1] - lineCurvePts[ii][1]) * (
/* 189 */         pt[1] - lineCurvePts[ii][1]) + 
/* 190 */         (pt[0] - lineCurvePts[ii][0]) * (
/* 191 */         pt[0] - lineCurvePts[ii][0]));
/*     */ 
/* 193 */       if ((minDistance < 0.0D) || (dist < minDistance)) {
/* 194 */         minDistance = dist;
/* 195 */         nearestId = ii;
/*     */       }
/*     */     }
/*     */ 
/* 199 */     return nearestId;
/*     */   }
/*     */ 
/*     */   private double getDirectionAtPoint(double[][] lineCurvePts, int id)
/*     */   {
/*     */     double dir;
/* 208 */     if (id == 0)
/*     */     {
/* 210 */       dir = Math.atan2(lineCurvePts[id][1] - lineCurvePts[(id + 1)][1], 
/* 211 */         lineCurvePts[id][0] - lineCurvePts[(id + 1)][0]);
/*     */     }
/*     */     else
/*     */     {
/*     */       double dir;
/* 214 */       if (id == lineCurvePts.length - 1)
/*     */       {
/* 216 */         dir = Math.atan2(lineCurvePts[(id - 1)][1] - lineCurvePts[id][1], 
/* 217 */           lineCurvePts[(id - 1)][0] - lineCurvePts[id][0]);
/*     */       }
/*     */       else
/*     */       {
/* 221 */         double dir1 = Math.atan2(lineCurvePts[(id - 1)][1] - lineCurvePts[id][1], 
/* 222 */           lineCurvePts[(id - 1)][0] - lineCurvePts[id][0]);
/* 223 */         double dir2 = Math.atan2(lineCurvePts[id][1] - lineCurvePts[(id + 1)][1], 
/* 224 */           lineCurvePts[id][0] - lineCurvePts[(id + 1)][0]);
/*     */         double dir;
/* 227 */         if (dir1 * dir2 < 0.0D) {
/* 228 */           dir = 0.5D * (dir2 - dir1);
/*     */         }
/*     */         else {
/* 231 */           dir = 0.5D * (dir1 + dir2);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 237 */     double dir = dir * 180.0D / 3.141592653589793D;
/* 238 */     if (dir < 0.0D) dir += 360.0D;
/*     */ 
/* 242 */     dir += 90.0D;
/* 243 */     if (dir < 0.0D) {
/* 244 */       dir += 360.0D;
/*     */     }
/* 246 */     else if (dir > 360.0D) {
/* 247 */       dir -= 360.0D;
/*     */     }
/*     */ 
/* 250 */     return dir;
/*     */   }
/*     */ 
/*     */   private void updateWind(AbstractDrawableComponent wind, Coordinate loc, double dir, double mapDir)
/*     */   {
/* 260 */     Iterator itde = wind.createDEIterator();
/*     */ 
/* 262 */     while (itde.hasNext()) {
/* 263 */       SinglePointElement spe = (SinglePointElement)itde.next();
/*     */ 
/* 265 */       if ((spe instanceof Jet.JetBarb)) {
/* 266 */         ((Jet.JetBarb)spe).setLocationOnly(loc);
/* 267 */         ((Jet.JetBarb)spe).setDirection(mapDir);
/*     */       }
/* 270 */       else if ((spe instanceof Jet.JetText))
/*     */       {
/* 272 */         double txtDir = dir > 270.0D ? 270.0D - dir + 360.0D : 270.0D - dir;
/*     */ 
/* 275 */         Coordinate txtLoc = latLon2Relative(((Jet.JetText)spe).getLocation(), (gov.noaa.nws.ncep.ui.pgen.elements.Vector)spe.getParent().getPrimaryDE());
/* 276 */         if (txtLoc.x < 0.001D)
/*     */         {
/*     */           double distance;
/*     */           double theta;
/*     */           double distance;
/* 282 */           if ((dir > 0.0D) && (dir < 180.0D))
/*     */           {
/*     */             double distance;
/* 285 */             if (((Text)spe).getText().length > 1) {
/* 286 */               double theta = 35.0D;
/* 287 */               distance = 110.0D;
/*     */             }
/*     */             else {
/* 290 */               double theta = 26.0D;
/* 291 */               distance = 95.0D;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*     */             double distance;
/* 298 */             if (((Text)spe).getText().length > 1) {
/* 299 */               double theta = 33.0D;
/* 300 */               distance = 110.0D;
/*     */             }
/*     */             else
/*     */             {
/* 304 */               theta = 20.0D;
/* 305 */               distance = 95.0D;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 310 */           if (spe.getLocation().y < 0.0D) {
/* 311 */             theta *= -1.0D;
/* 312 */             theta += -20.0D;
/*     */           }
/*     */ 
/* 316 */           ((Jet.JetText)spe).setLocation(relative2LatLon(new Coordinate(distance, theta), 
/* 317 */             (gov.noaa.nws.ncep.ui.pgen.elements.Vector)spe.getParent().getPrimaryDE()));
/*     */         }
/*     */ 
/* 322 */         if ((dir > 0.0D) && (dir < 180.0D)) {
/* 323 */           txtDir += 180.0D;
/* 324 */           if (txtDir > 360.0D) txtDir -= 360.0D;
/*     */ 
/* 326 */           if (spe.getLocation().y < 0.0D) {
/* 327 */             txtDir += 180.0D;
/* 328 */             if (txtDir > 360.0D) txtDir -= 360.0D;
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 335 */         ((Jet.JetText)spe).setRotation(txtDir);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void snapBarbOnJet(AbstractDrawableComponent barb, double[][] lineCurvePts)
/*     */   {
/* 345 */     Coordinate loc = ((ISinglePoint)barb.getPrimaryDE()).getLocation();
/* 346 */     double[] barbPt = this.descriptor.worldToPixel(new double[] { loc.x, loc.y });
/*     */ 
/* 348 */     int nearestId = getNearestPtOnCurve(lineCurvePts, barbPt);
/*     */ 
/* 350 */     double dir = getDirectionAtPoint(lineCurvePts, nearestId);
/* 351 */     double[] pix = this.descriptor.pixelToWorld(lineCurvePts[nearestId]);
/* 352 */     Coordinate nearestPt = new Coordinate(pix[0], pix[1]);
/*     */ 
/* 355 */     double mapDir = translateDir(dir, nearestPt.y, nearestPt.x, true);
/*     */ 
/* 358 */     updateWind(barb, nearestPt, dir, mapDir);
/*     */   }
/*     */ 
/*     */   private void addHashOnJet(Jet aJet, double[][] lineCurvePts)
/*     */   {
/* 369 */     if (aJet.size() < 2) return;
/*     */ 
/* 371 */     double[] speed = new double[aJet.size() + 1];
/* 372 */     int[] index = new int[aJet.size() + 1];
/*     */ 
/* 374 */     speed[0] = 80.0D;
/* 375 */     index[0] = 0;
/* 376 */     speed[1] = 80.0D;
/* 377 */     index[1] = lineCurvePts.length;
/*     */ 
/* 379 */     int counter = 2;
/*     */ 
/* 382 */     Iterator iterator = aJet.createDEIterator();
/* 383 */     while (iterator.hasNext()) {
/* 384 */       DrawableElement de = (DrawableElement)iterator.next();
/* 385 */       if ((de instanceof Jet.JetHash)) {
/* 386 */         this.hashTmp = ((Jet.JetHash)de);
/* 387 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 391 */     aJet.removeAllHash();
/*     */ 
/* 393 */     Iterator it = aJet.createDEIterator();
/* 394 */     int maxSpeedIdx = 0;
/* 395 */     double maxSpeed = -1.0D;
/*     */ 
/* 399 */     while (it.hasNext()) {
/* 400 */       DrawableElement adc = (DrawableElement)it.next();
/* 401 */       if ((adc instanceof Jet.JetBarb)) {
/* 402 */         Coordinate adcLoc = ((Jet.JetBarb)adc).getLocation();
/* 403 */         double[] barbPt = this.descriptor.worldToPixel(new double[] { adcLoc.x, adcLoc.y });
/*     */ 
/* 405 */         int nearestId = getNearestPtOnCurve(lineCurvePts, barbPt);
/* 406 */         index[counter] = nearestId;
/* 407 */         speed[counter] = ((Jet.JetBarb)adc).getSpeed();
/* 408 */         if ((maxSpeed < 0.0D) || (speed[counter] > maxSpeed)) {
/* 409 */           maxSpeed = speed[counter];
/* 410 */           maxSpeedIdx = counter;
/*     */         }
/* 412 */         counter++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 417 */     for (int ii = 0; ii < counter - 1; ii++) {
/* 418 */       for (int jj = 0; jj < counter - 1; jj++) {
/* 419 */         if (index[jj] > index[(jj + 1)]) {
/* 420 */           int hold1 = index[jj];
/* 421 */           index[jj] = index[(jj + 1)];
/* 422 */           index[(jj + 1)] = hold1;
/*     */ 
/* 424 */           double hold2 = speed[jj];
/* 425 */           speed[jj] = speed[(jj + 1)];
/* 426 */           speed[(jj + 1)] = hold2;
/*     */ 
/* 429 */           if (maxSpeedIdx == jj + 1) {
/* 430 */             maxSpeedIdx = jj;
/*     */           }
/* 432 */           else if (maxSpeedIdx == jj) {
/* 433 */             maxSpeedIdx = jj + 1;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 447 */     for (int ii = maxSpeedIdx; ii > 0; ii--) {
/* 448 */       addHash(aJet, lineCurvePts, speed[ii], speed[(ii - 1)], index[ii], index[(ii - 1)]);
/*     */     }
/*     */ 
/* 452 */     for (int ii = maxSpeedIdx; ii < counter - 1; ii++) {
/* 453 */       addHash(aJet, lineCurvePts, speed[ii], speed[(ii + 1)], index[ii], index[(ii + 1)]);
/*     */     }
/*     */ 
/* 456 */     this.hashTmp = null;
/*     */   }
/*     */ 
/*     */   private void addHash(Jet aJet, double[][] lineCurvePts, double speed1, double speed2, int id1, int id2)
/*     */   {
/* 471 */     double delta = 20.0D;
/* 472 */     if (speed1 > speed2) delta = -20.0D;
/*     */ 
/* 474 */     double rate = (id2 - id1) / (speed2 - speed1);
/* 475 */     double speed = speed1;
/*     */ 
/* 478 */     while (Math.abs(speed2 - speed) > Math.abs(delta)) {
/* 479 */       speed += delta;
/* 480 */       int id = (int)((speed - speed1) * rate) + id1;
/* 481 */       Jet.JetHash hash = addHashAtIndex(aJet, lineCurvePts, id);
/*     */ 
/* 483 */       double mapDir = hash.getDirection();
/*     */ 
/* 485 */       if ((this.hashTmp == null) && (this.jetDlg != null)) {
/* 486 */         IAttribute hashAttr = this.jetDlg.getHashAttr();
/*     */ 
/* 488 */         if (hashAttr != null) {
/* 489 */           hash.update(hashAttr);
/*     */         }
/*     */       }
/* 492 */       hash.setDirection(mapDir);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Jet.JetHash addHashAtIndex(Jet aJet, double[][] lineCurvePts, int locId)
/*     */   {
/* 506 */     double[] pix = this.descriptor.pixelToWorld(new double[] { lineCurvePts[locId][0], lineCurvePts[locId][1] });
/* 507 */     Coordinate loc = new Coordinate(pix[0], pix[1]);
/*     */ 
/* 509 */     double dir = getDirectionAtPoint(lineCurvePts, locId);
/*     */ 
/* 511 */     pix = this.descriptor.pixelToWorld(new double[] { lineCurvePts[locId][0], lineCurvePts[locId][1] });
/* 512 */     Coordinate nearestPt = new Coordinate(pix[0], pix[1]);
/*     */ 
/* 514 */     double mapDir = translateDir(dir, nearestPt.y, nearestPt.x, true);
/*     */ 
/* 516 */     mapDir = 180.0D - mapDir;
/* 517 */     if (mapDir < 0.0D) mapDir += 360.0D;
/*     */     Jet.JetHash hash;
/*     */     Jet.JetHash hash;
/* 520 */     if (this.hashTmp != null)
/*     */     {
/*     */       Jet tmp156_155 = aJet; tmp156_155.getClass(); hash = new Jet.JetHash(tmp156_155, null, this.hashTmp.getColors(), 
/* 522 */         this.hashTmp.getLineWidth(), this.hashTmp.getSizeScale(), 
/* 523 */         this.hashTmp.isClear(), loc, IVector.VectorType.HASH_MARK, 
/* 524 */         100.0D, mapDir, this.hashTmp.getArrowHeadSize(), 
/* 525 */         false, "Vector", "Hash");
/*     */     }
/*     */     else
/*     */     {
/*     */       Jet tmp227_226 = aJet; tmp227_226.getClass(); hash = new Jet.JetHash(tmp227_226, null, new Color[] { new Color(0, 255, 0), new Color(255, 0, 0) }, 
/* 529 */         2.0F, 2.0D, Boolean.valueOf(true), loc, IVector.VectorType.HASH_MARK, 
/* 530 */         100.0D, mapDir, 1.0D, false, "Vector", "Hash");
/*     */     }
/*     */ 
/* 533 */     aJet.add(hash);
/* 534 */     return hash;
/*     */   }
/*     */ 
/*     */   public void snapHash(Jet.JetHash hash, Coordinate loc, Jet aJet)
/*     */   {
/* 542 */     double[][] lineCurvePts = getJetCurve(aJet);
/* 543 */     double[] hashPt = this.descriptor.worldToPixel(new double[] { loc.x, loc.y });
/*     */ 
/* 546 */     int nearestId = getNearestPtOnCurve(lineCurvePts, hashPt);
/*     */ 
/* 548 */     double[] nearestPix = this.descriptor.pixelToWorld(new double[] { lineCurvePts[nearestId][0], lineCurvePts[nearestId][1] });
/* 549 */     Coordinate nearestPt = new Coordinate(nearestPix[0], nearestPix[1]);
/*     */ 
/* 552 */     double dir = getDirectionAtPoint(lineCurvePts, nearestId);
/* 553 */     double mapDir = translateDir(dir, nearestPt.y, nearestPt.x, true);
/* 554 */     mapDir = 180.0D - mapDir;
/* 555 */     if (mapDir < 0.0D);
/* 555 */     mapDir += 360.0D;
/*     */ 
/* 557 */     hash.setLocationOnly(nearestPt);
/* 558 */     hash.setDirection(mapDir);
/* 559 */     checkHashOnJet(aJet);
/*     */   }
/*     */ 
/*     */   private void snapAllHashs(Jet aJet)
/*     */   {
/* 565 */     Iterator it = aJet.getComponentIterator();
/* 566 */     while (it.hasNext()) {
/* 567 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 568 */       if ((adc instanceof Jet.JetHash))
/* 569 */         snapHash((Jet.JetHash)adc, ((Jet.JetHash)adc).getLocation(), aJet);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setMapDescriptor(IMapDescriptor mapDescriptor)
/*     */   {
/* 579 */     this.descriptor = mapDescriptor;
/*     */   }
/*     */ 
/*     */   public Coordinate relative2LatLon(Coordinate relativeLoc, gov.noaa.nws.ncep.ui.pgen.elements.Vector barb)
/*     */   {
/* 590 */     double scaleFactor = this.mapEditor.getActiveDisplayPane().getZoomLevel() / 0.24D;
/*     */ 
/* 592 */     double[] pt0 = this.descriptor.worldToPixel(new double[] { ((Coordinate)barb.getPoints().get(0)).x, ((Coordinate)barb.getPoints().get(0)).y });
/*     */ 
/* 594 */     double mdir = barb.getDirection();
/* 595 */     double dir = translateDir(mdir, ((Coordinate)barb.getPoints().get(0)).y, ((Coordinate)barb.getPoints().get(0)).x, false);
/*     */ 
/* 597 */     double deltaX = Math.sin((dir - relativeLoc.y) * 3.141592653589793D / 180.0D) * relativeLoc.x * scaleFactor;
/* 598 */     double deltaY = Math.cos((dir - relativeLoc.y) * 3.141592653589793D / 180.0D) * relativeLoc.x * scaleFactor;
/*     */ 
/* 600 */     double[] pt1 = { pt0[0] + deltaX, pt0[1] - deltaY };
/*     */ 
/* 604 */     double[] world = this.descriptor.pixelToWorld(pt1);
/* 605 */     Coordinate loc = new Coordinate(world[0], world[1], world[2]);
/*     */ 
/* 607 */     return loc;
/*     */   }
/*     */ 
/*     */   public Coordinate latLon2Relative(Coordinate loc, gov.noaa.nws.ncep.ui.pgen.elements.Vector barb)
/*     */   {
/* 617 */     Coordinate polar = new Coordinate();
/*     */ 
/* 621 */     double[] pt0 = this.descriptor.worldToPixel(new double[] { ((Coordinate)barb.getPoints().get(0)).x, ((Coordinate)barb.getPoints().get(0)).y });
/* 622 */     double[] pt1 = this.descriptor.worldToPixel(new double[] { loc.x, loc.y });
/*     */ 
/* 625 */     double deltaX = pt1[0] - pt0[0];
/* 626 */     double deltaY = pt1[1] - pt0[1];
/*     */ 
/* 629 */     polar.x = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
/*     */ 
/* 631 */     double scaleFactor = this.mapEditor.getActiveDisplayPane().getZoomLevel() / 0.24D;
/*     */ 
/* 633 */     polar.x /= scaleFactor;
/*     */ 
/* 636 */     double tmp = Math.atan2(-deltaY, deltaX) * 180.0D / 3.141592653589793D;
/* 637 */     tmp = 90.0D - tmp;
/* 638 */     if (tmp < 0.0D) tmp += 360.0D;
/*     */ 
/* 641 */     double dir = barb.getDirection();
/* 642 */     double sDir = translateDir(dir, ((Coordinate)barb.getPoints().get(0)).y, ((Coordinate)barb.getPoints().get(0)).x, false);
/*     */ 
/* 645 */     polar.y = (sDir - tmp);
/* 646 */     if (polar.y > 180.0D) polar.y -= 360.0D;
/*     */ 
/* 649 */     if ((loc.y < 0.0D) && (polar.y > 0.0D)) polar.y *= -1.0D;
/*     */ 
/* 651 */     return polar;
/*     */   }
/*     */ 
/*     */   public java.util.Vector<Integer> checkHashOnJet(Jet aJet)
/*     */   {
/* 663 */     double[][] lineCurvePts = getJetCurve(aJet);
/* 664 */     java.util.Vector ret = new java.util.Vector();
/*     */ 
/* 666 */     if (aJet.size() < 2) return ret;
/*     */ 
/* 668 */     double[] speed = new double[aJet.size() + 1];
/* 669 */     int[] barbIdx = new int[aJet.size() + 1];
/*     */ 
/* 671 */     int[] hashIdx = new int[aJet.size() + 1];
/* 672 */     for (int ii = 0; ii < hashIdx.length; ii++) {
/* 673 */       hashIdx[ii] = -1;
/*     */     }
/*     */ 
/* 676 */     speed[0] = 80.0D;
/* 677 */     barbIdx[0] = 0;
/* 678 */     speed[1] = 80.0D;
/* 679 */     barbIdx[1] = lineCurvePts.length;
/*     */ 
/* 681 */     int counter = 2;
/*     */ 
/* 695 */     Iterator it = aJet.createDEIterator();
/* 696 */     int maxSpeedIdx = 0;
/* 697 */     double maxSpeed = -1.0D;
/*     */ 
/* 701 */     int kk = 0;
/* 702 */     while (it.hasNext()) {
/* 703 */       DrawableElement adc = (DrawableElement)it.next();
/* 704 */       if ((adc instanceof Jet.JetBarb)) {
/* 705 */         Coordinate adcLoc = ((Jet.JetBarb)adc).getLocation();
/* 706 */         double[] barbPt = this.descriptor.worldToPixel(new double[] { adcLoc.x, adcLoc.y });
/*     */ 
/* 708 */         int nearestId = getNearestPtOnCurve(lineCurvePts, barbPt);
/* 709 */         barbIdx[counter] = nearestId;
/* 710 */         speed[counter] = ((Jet.JetBarb)adc).getSpeed();
/* 711 */         if ((maxSpeed < 0.0D) || (speed[counter] > maxSpeed)) {
/* 712 */           maxSpeed = speed[counter];
/* 713 */           maxSpeedIdx = counter;
/*     */         }
/* 715 */         counter++;
/*     */       }
/* 717 */       else if ((adc instanceof Jet.JetHash)) {
/* 718 */         Coordinate adcLoc = ((Jet.JetHash)adc).getLocation();
/* 719 */         double[] hashPt = this.descriptor.worldToPixel(new double[] { adcLoc.x, adcLoc.y });
/* 720 */         int nearestId = getNearestPtOnCurve(lineCurvePts, hashPt);
/*     */ 
/* 722 */         hashIdx[(kk++)] = nearestId;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 728 */     for (int ii = 0; ii < counter - 1; ii++) {
/* 729 */       for (int jj = 0; jj < counter - 1; jj++) {
/* 730 */         if (barbIdx[jj] > barbIdx[(jj + 1)]) {
/* 731 */           int hold1 = barbIdx[jj];
/* 732 */           barbIdx[jj] = barbIdx[(jj + 1)];
/* 733 */           barbIdx[(jj + 1)] = hold1;
/*     */ 
/* 735 */           double hold2 = speed[jj];
/* 736 */           speed[jj] = speed[(jj + 1)];
/* 737 */           speed[(jj + 1)] = hold2;
/*     */ 
/* 740 */           if (maxSpeedIdx == jj + 1) {
/* 741 */             maxSpeedIdx = jj;
/*     */           }
/* 743 */           else if (maxSpeedIdx == jj) {
/* 744 */             maxSpeedIdx = jj + 1;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 751 */     for (int ii = 0; ii < counter - 1; ii++) {
/* 752 */       double deltaSpeed = Math.abs(speed[(ii + 1)] - speed[ii]);
/* 753 */       int hashesNeeded = (int)(deltaSpeed / 20.0D);
/* 754 */       if ((hashesNeeded > 0) && (deltaSpeed % 20.0D < 0.01D)) hashesNeeded--;
/*     */ 
/* 756 */       int hashesOnJet = 0;
/* 757 */       for (int jj = 0; jj < hashIdx.length; jj++) {
/* 758 */         if ((hashIdx[jj] >= barbIdx[ii]) && (hashIdx[jj] < barbIdx[(ii + 1)])) hashesOnJet++;
/*     */       }
/*     */ 
/* 761 */       ret.add(Integer.valueOf(hashesNeeded - hashesOnJet));
/*     */     }
/*     */ 
/* 765 */     return ret;
/*     */   }
/*     */ 
/*     */   public void addBarbHashFromAnotherJet(Jet jet1, Jet jet2)
/*     */   {
/* 776 */     if (jet2.size() <= 1) return;
/*     */ 
/* 778 */     double[][] newpts = getJetCurve(jet1);
/* 779 */     Coordinate[] coords = new Coordinate[newpts.length];
/*     */ 
/* 781 */     for (int k = 0; k < newpts.length; k++) {
/* 782 */       coords[k] = new Coordinate(newpts[k][0], newpts[k][1]);
/*     */     }
/* 784 */     LineString ls = new GeometryFactory().createLineString(coords);
/* 785 */     LocationIndexedLine lil = new LocationIndexedLine(ls);
/*     */ 
/* 787 */     Iterator it = jet2.createDEIterator();
/*     */ 
/* 789 */     while (it.hasNext()) {
/* 790 */       DrawableElement adc = (DrawableElement)it.next();
/*     */ 
/* 792 */       if (((adc instanceof Jet.JetBarb)) || 
/* 793 */         ((adc instanceof Jet.JetHash)))
/*     */       {
/* 795 */         double[] loc = { ((SinglePointElement)adc).getLocation().x, ((SinglePointElement)adc).getLocation().y };
/* 796 */         double[] pix = this.descriptor.worldToPixel(loc);
/* 797 */         Coordinate screenPt = new Coordinate(pix[0], pix[1]);
/* 798 */         LinearLocation linloc = lil.project(screenPt);
/* 799 */         Coordinate screen2 = lil.extractPoint(linloc);
/*     */ 
/* 801 */         if (screenPt.distance(screen2) < 100.0D)
/* 802 */           if ((adc instanceof Jet.JetBarb))
/*     */           {
/* 804 */             jet1.add(adc.getParent().copy());
/*     */           }
/*     */           else
/* 807 */             jet1.add(adc.copy());
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenSnapJet
 * JD-Core Version:    0.6.2
 */