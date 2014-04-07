/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourMinmax;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE})
/*     */ public class Outlook extends Contours
/*     */ {
/*  54 */   public static String OUTLOOK_LINE_GROUP = "OutlookLineGroup";
/*     */ 
/*  57 */   public static String OUTLOOK_LABELED_LINE = "OutlookLine";
/*     */   private String outlookType;
/*     */   private String forecaster;
/*     */   private String days;
/*     */   private Calendar issueTime;
/*     */   private Calendar expirationTime;
/*     */   private String lineInfo;
/*     */   private boolean linesSorted;
/*     */ 
/*     */   protected Outlook(String name)
/*     */   {
/*  75 */     super(name);
/*  76 */     this.linesSorted = false;
/*     */   }
/*     */ 
/*     */   public void reorderLines()
/*     */   {
/*  86 */     if (!this.linesSorted) {
/*  87 */       orderLines();
/*  88 */       this.linesSorted = true;
/*     */     }
/*     */     else {
/*  91 */       Iterator it = this.compList.iterator();
/*  92 */       while (it.hasNext()) {
/*  93 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/*  94 */         if (adc.getName().equalsIgnoreCase(OUTLOOK_LINE_GROUP)) {
/*  95 */           ListIterator listIt = ((DECollection)adc)
/*  96 */             .getComponentListIterator();
/*  97 */           while (listIt.nextIndex() < ((DECollection)adc).size()) {
/*  98 */             listIt.next();
/*     */           }
/*     */ 
/* 101 */           DECollection lastLn = null;
/* 102 */           while ((listIt.hasPrevious()) && (lastLn == null)) {
/* 103 */             AbstractDrawableComponent adc1 = (AbstractDrawableComponent)listIt.previous();
/*     */ 
/* 105 */             if (adc1.getName().equalsIgnoreCase(
/* 105 */               OUTLOOK_LABELED_LINE)) {
/* 106 */               Iterator it1 = ((DECollection)adc1)
/* 107 */                 .getComponentIterator();
/* 108 */               while (it1.hasNext()) {
/* 109 */                 AbstractDrawableComponent de = (AbstractDrawableComponent)it1.next();
/* 110 */                 if ((de instanceof Line)) {
/* 111 */                   lastLn = (DECollection)adc1;
/* 112 */                   break;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 118 */           if (lastLn != null) {
/* 119 */             ((DECollection)adc).remove(lastLn);
/* 120 */             ((DECollection)adc).add(0, lastLn);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void orderLines()
/*     */   {
/* 140 */     Iterator it = this.compList.iterator();
/* 141 */     while (it.hasNext()) {
/* 142 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 143 */       if (adc.getName().equalsIgnoreCase(OUTLOOK_LINE_GROUP)) {
/* 144 */         ArrayList grp = new ArrayList();
/* 145 */         Iterator itGrp = ((DECollection)adc)
/* 146 */           .getComponentIterator();
/*     */         Iterator itLabeledLine;
/* 147 */         for (; itGrp.hasNext(); 
/* 151 */           itLabeledLine.hasNext())
/*     */         {
/* 148 */           AbstractDrawableComponent labeledLine = (AbstractDrawableComponent)itGrp.next();
/* 149 */           itLabeledLine = ((DECollection)labeledLine)
/* 150 */             .getComponentIterator();
/* 151 */           continue;
/* 152 */           AbstractDrawableComponent de = (AbstractDrawableComponent)itLabeledLine.next();
/* 153 */           if ((de instanceof Line)) {
/* 154 */             grp.add((DECollection)labeledLine);
/*     */ 
/* 156 */             itGrp.remove();
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 161 */         Collections.sort(grp, new EastMost(null));
/* 162 */         ((DECollection)adc).add(grp);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Outlook copy()
/*     */   {
/* 219 */     Outlook newOtlk = new Outlook(getName());
/* 220 */     Iterator iterator = getComponentIterator();
/*     */ 
/* 222 */     while (iterator.hasNext()) {
/* 223 */       newOtlk.addElement(((AbstractDrawableComponent)iterator.next()).copy());
/*     */     }
/*     */ 
/* 226 */     iterator = newOtlk.getComponentIterator();
/* 227 */     while (iterator.hasNext()) {
/* 228 */       ((AbstractDrawableComponent)iterator.next()).setParent(newOtlk);
/*     */     }
/*     */ 
/* 231 */     newOtlk.setParm(getParm());
/* 232 */     newOtlk.setLevel(getLevel());
/* 233 */     newOtlk.setTime1(getTime1());
/* 234 */     newOtlk.setTime2(getTime2());
/* 235 */     newOtlk.setCint(getCint());
/*     */ 
/* 237 */     newOtlk.setPgenCategory(getPgenCategory());
/* 238 */     newOtlk.setPgenType(getPgenType());
/* 239 */     newOtlk.setParent(getParent());
/* 240 */     newOtlk.setOutlookType(getOutlookType());
/* 241 */     newOtlk.setDays(getDays());
/* 242 */     newOtlk.setIssueTime(getIssueTime());
/* 243 */     newOtlk.setExpirationTime(getExpirationTime());
/* 244 */     newOtlk.setForecaster(getForecaster());
/*     */ 
/* 246 */     return newOtlk;
/*     */   }
/*     */ 
/*     */   public void removeLine(Line ln)
/*     */   {
/* 255 */     DECollection dec = (DECollection)ln.getParent();
/*     */ 
/* 257 */     if (dec.getParent().equals(this)) {
/* 258 */       remove(dec);
/* 259 */     } else if (dec.getParent().getParent().equals(this)) {
/* 260 */       DECollection grp = (DECollection)dec.getParent();
/* 261 */       grp.remove(dec);
/* 262 */       if (grp.size() == 0)
/* 263 */         remove(grp);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getDEs()
/*     */   {
/* 274 */     Iterator it = createDEIterator();
/* 275 */     int num = 0;
/* 276 */     while (it.hasNext()) {
/* 277 */       it.next();
/* 278 */       num++;
/*     */     }
/* 280 */     return num;
/*     */   }
/*     */ 
/*     */   public void setOutlookType(String outlookType)
/*     */   {
/* 285 */     this.outlookType = outlookType;
/*     */   }
/*     */ 
/*     */   public String getOutlookType() {
/* 289 */     return this.outlookType;
/*     */   }
/*     */ 
/*     */   public void setForecaster(String forecaster) {
/* 293 */     this.forecaster = forecaster;
/*     */   }
/*     */ 
/*     */   public String getForecaster() {
/* 297 */     return this.forecaster;
/*     */   }
/*     */ 
/*     */   public void setDays(String days) {
/* 301 */     this.days = days;
/*     */   }
/*     */ 
/*     */   public String getDays() {
/* 305 */     return this.days;
/*     */   }
/*     */ 
/*     */   public void setIssueTime(Calendar issueTime) {
/* 309 */     this.issueTime = issueTime;
/*     */   }
/*     */ 
/*     */   public Calendar getIssueTime() {
/* 313 */     return this.issueTime;
/*     */   }
/*     */ 
/*     */   public void setExpirationTime(Calendar expirationTime) {
/* 317 */     this.expirationTime = expirationTime;
/*     */   }
/*     */ 
/*     */   public Calendar getExpirationTime() {
/* 321 */     return this.expirationTime;
/*     */   }
/*     */ 
/*     */   public void setLineInfo(String lineInfo) {
/* 325 */     this.lineInfo = lineInfo;
/*     */   }
/*     */ 
/*     */   public String getLineInfo() {
/* 329 */     return this.lineInfo;
/*     */   }
/*     */ 
/*     */   public void saveToFile(String filename)
/*     */   {
/* 339 */     Layer defaultLayer = new Layer();
/* 340 */     defaultLayer.addElement(this);
/*     */ 
/* 342 */     Product defaultProduct = new Product();
/* 343 */     defaultProduct.addLayer(defaultLayer);
/*     */ 
/* 345 */     ArrayList prds = new ArrayList();
/* 346 */     prds.add(defaultProduct);
/* 347 */     Products filePrds = ProductConverter.convert(prds);
/*     */ 
/* 349 */     FileTools.write(filename, filePrds);
/*     */   }
/*     */ 
/*     */   public void addLineToGroup(Line ln, DECollection grp)
/*     */   {
/* 361 */     if ((ln != null) && (contains(ln)) && (
/* 362 */       (grp == null) || (contains(grp))))
/*     */     {
/* 364 */       if (ln.getParent().getParent().equals(this)) {
/* 365 */         if (grp == null) {
/* 366 */           grp = new DECollection(OUTLOOK_LINE_GROUP);
/* 367 */           remove(ln.getParent());
/* 368 */           add(grp);
/* 369 */           grp.add(ln.getParent());
/*     */         } else {
/* 371 */           remove(ln.getParent());
/* 372 */           grp.add(ln.getParent());
/*     */         }
/*     */       }
/* 375 */       else if ((ln.getParent().getParent().getName()
/* 375 */         .equalsIgnoreCase(OUTLOOK_LINE_GROUP)) && 
/* 376 */         (grp != null))
/*     */       {
/* 382 */         Iterator it = ((DECollection)ln.getParent()
/* 383 */           .getParent()).createDEIterator();
/* 384 */         int lns = 0;
/* 385 */         while (it.hasNext()) {
/* 386 */           DrawableElement de = (DrawableElement)it.next();
/* 387 */           if ((de instanceof Line)) {
/* 388 */             lns++;
/*     */           }
/*     */         }
/*     */ 
/* 392 */         if (lns == 1) {
/* 393 */           remove(ln.getParent().getParent());
/* 394 */           grp.add(ln.getParent());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rmLineFromGroup(Line ln, DECollection grp)
/*     */   {
/* 407 */     if ((ln != null) && (contains(ln)) && 
/* 408 */       (grp != null) && (contains(grp)) && 
/* 409 */       (ln.getParent().getParent() == grp))
/*     */     {
/* 411 */       grp.remove(ln.getParent());
/*     */ 
/* 414 */       if (grp.size() == 0) {
/* 415 */         remove(grp);
/*     */       }
/*     */ 
/* 419 */       add(ln.getParent());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Line getNearestLine(Coordinate loc)
/*     */   {
/* 434 */     Line nearestLine = null;
/* 435 */     double minDistance = -1.0D;
/*     */ 
/* 437 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*     */ 
/* 439 */     gc.setStartingGeographicPoint(loc.x, loc.y);
/*     */ 
/* 441 */     Iterator iterator = createDEIterator();
/* 442 */     while (iterator.hasNext())
/*     */     {
/* 444 */       DrawableElement element = (DrawableElement)iterator.next();
/* 445 */       if ((element instanceof Line))
/*     */       {
/* 447 */         for (Coordinate pts : element.getPoints())
/*     */         {
/* 449 */           gc.setDestinationGeographicPoint(pts.x, pts.y);
/*     */ 
/* 451 */           double dist = 0.0D;
/*     */           try
/*     */           {
/* 454 */             dist = gc.getOrthodromicDistance();
/*     */           } catch (IllegalStateException ise) {
/* 456 */             ise.printStackTrace();
/*     */           }
/*     */           catch (ArithmeticException ae) {
/* 459 */             ae.printStackTrace();
/* 460 */             continue;
/*     */           }
/*     */ 
/* 463 */           if ((minDistance < 0.0D) || (dist < minDistance))
/*     */           {
/* 465 */             minDistance = dist;
/* 466 */             nearestLine = (Line)element;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 474 */     return nearestLine;
/*     */   }
/*     */ 
/*     */   public ArrayList<ContourLine> getContourLines()
/*     */   {
/* 482 */     ArrayList lines = new ArrayList();
/*     */ 
/* 484 */     findContourLines(this, lines);
/*     */ 
/* 486 */     return lines;
/*     */   }
/*     */ 
/*     */   public ArrayList<ContourMinmax> getContourMinmaxs()
/*     */   {
/* 495 */     return new ArrayList();
/*     */   }
/*     */ 
/*     */   public Outlook createContours(int nContours, int[] nContourPts, double[] latlons, float[] contourValue, Color clr)
/*     */   {
/* 508 */     Outlook gridContours = copy();
/*     */ 
/* 510 */     int tPts = 0;
/*     */ 
/* 512 */     ArrayList cline = getContourLines();
/* 513 */     Line lineTemp = ((ContourLine)cline.get(0)).getLine();
/* 514 */     Text txtTemp = (Text)((ContourLine)cline.get(0)).getLabels().get(0);
/*     */ 
/* 516 */     gridContours.clear();
/*     */ 
/* 518 */     for (int mm = 0; mm < nContours; mm++)
/*     */     {
/* 520 */       ArrayList linePts = new ArrayList();
/*     */ 
/* 522 */       for (int nn = 0; nn < nContourPts[mm]; nn++) {
/* 523 */         Coordinate point = new Coordinate();
/* 524 */         point.x = latlons[(tPts + nn * 2)];
/* 525 */         point.y = latlons[(tPts + nn * 2 + 1)];
/*     */ 
/* 527 */         linePts.add(point);
/*     */       }
/*     */ 
/* 530 */       Collections.reverse(linePts);
/*     */ 
/* 532 */       tPts += nContourPts[mm] * 2;
/*     */ 
/* 534 */       Line oneLine = (Line)lineTemp.copy();
/* 535 */       oneLine.setPointsOnly(linePts);
/* 536 */       if (clr != null) {
/* 537 */         oneLine.setColors(new Color[] { clr });
/*     */       }
/* 539 */       DECollection dec = new DECollection(OUTLOOK_LABELED_LINE);
/* 540 */       oneLine.setParent(dec);
/* 541 */       dec.add(oneLine);
/*     */ 
/* 543 */       int nLabels = 1;
/* 544 */       Coordinate cdt = (Coordinate)linePts.get(linePts.size() - 1);
/* 545 */       for (int ii = 0; ii < nLabels; ii++) {
/* 546 */         Text oneText = (Text)txtTemp.copy();
/* 547 */         oneText.setText(new String[] { contourValue[mm] });
/*     */ 
/* 549 */         oneText.setLocationOnly(new Coordinate(cdt.x + 2.0D, cdt.y + 1.0D));
/* 550 */         if (clr != null)
/* 551 */           oneText.setColors(new Color[] { clr });
/* 552 */         oneText.setParent(dec);
/* 553 */         dec.add(oneText);
/*     */       }
/*     */ 
/* 556 */       dec.setParent(gridContours);
/* 557 */       gridContours.add(dec);
/*     */     }
/*     */ 
/* 561 */     return gridContours;
/*     */   }
/*     */ 
/*     */   public Outlook createContours(int nContours, int[] nContourPts, double[] latlons, String[] contourValue, Color clr)
/*     */   {
/* 574 */     Outlook gridContours = copy();
/*     */ 
/* 576 */     int tPts = 0;
/*     */ 
/* 578 */     ArrayList cline = getContourLines();
/* 579 */     Line lineTemp = ((ContourLine)cline.get(0)).getLine();
/* 580 */     Text txtTemp = (Text)((ContourLine)cline.get(0)).getLabels().get(0);
/*     */ 
/* 582 */     gridContours.clear();
/*     */ 
/* 584 */     for (int mm = 0; mm < nContours; mm++)
/*     */     {
/* 586 */       ArrayList linePts = new ArrayList();
/*     */ 
/* 588 */       for (int nn = 0; nn < nContourPts[mm]; nn++) {
/* 589 */         Coordinate point = new Coordinate();
/* 590 */         point.x = latlons[(tPts + nn * 2)];
/* 591 */         point.y = latlons[(tPts + nn * 2 + 1)];
/*     */ 
/* 593 */         linePts.add(point);
/*     */       }
/*     */ 
/* 596 */       Collections.reverse(linePts);
/*     */ 
/* 598 */       tPts += nContourPts[mm] * 2;
/*     */ 
/* 600 */       Line oneLine = (Line)lineTemp.copy();
/* 601 */       oneLine.setPointsOnly(linePts);
/* 602 */       if (clr != null) {
/* 603 */         oneLine.setColors(new Color[] { clr });
/*     */       }
/* 605 */       DECollection dec = new DECollection(OUTLOOK_LABELED_LINE);
/* 606 */       oneLine.setParent(dec);
/* 607 */       dec.add(oneLine);
/*     */ 
/* 609 */       int nLabels = 1;
/* 610 */       Coordinate cdt = (Coordinate)linePts.get(linePts.size() - 1);
/* 611 */       for (int ii = 0; ii < nLabels; ii++) {
/* 612 */         Text oneText = (Text)txtTemp.copy();
/* 613 */         oneText.setText(new String[] { contourValue[mm] });
/*     */ 
/* 615 */         oneText.setLocationOnly(new Coordinate(cdt.x + 2.0D, cdt.y + 1.0D));
/* 616 */         if (clr != null)
/* 617 */           oneText.setColors(new Color[] { clr });
/* 618 */         oneText.setParent(dec);
/* 619 */         dec.add(oneText);
/*     */       }
/*     */ 
/* 622 */       dec.setParent(gridContours);
/* 623 */       gridContours.add(dec);
/*     */     }
/*     */ 
/* 627 */     return gridContours;
/*     */   }
/*     */ 
/*     */   private void findContourLines(DECollection dec, ArrayList<ContourLine> lines)
/*     */   {
/* 637 */     if (lines == null) {
/* 638 */       lines = new ArrayList();
/*     */     }
/*     */ 
/* 641 */     Iterator iterator = dec
/* 642 */       .getComponentIterator();
/*     */ 
/* 644 */     while (iterator.hasNext())
/*     */     {
/* 646 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)iterator.next();
/*     */ 
/* 648 */       if (adc.getName().equals(OUTLOOK_LINE_GROUP)) {
/* 649 */         findContourLines((DECollection)adc, lines);
/* 650 */       } else if (adc.getName().equals(OUTLOOK_LABELED_LINE))
/*     */       {
/* 652 */         Iterator deit = adc.createDEIterator();
/* 653 */         Line ln = null;
/* 654 */         Text txt = null;
/* 655 */         while (deit.hasNext()) {
/* 656 */           DrawableElement de = (DrawableElement)deit.next();
/* 657 */           if ((de instanceof Line))
/* 658 */             ln = (Line)de;
/* 659 */           else if ((de instanceof Text)) {
/* 660 */             txt = (Text)de;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 665 */         ContourLine cline = new ContourLine(ln, txt, 1);
/* 666 */         cline.setParent(this);
/* 667 */         lines.add(cline);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class EastMost
/*     */     implements Comparator<DECollection>
/*     */   {
/*     */     private EastMost()
/*     */     {
/*     */     }
/*     */ 
/*     */     public int compare(DECollection labeledLn1, DECollection labeledLn2)
/*     */     {
/* 180 */       Iterator itLabeledLine1 = labeledLn1
/* 181 */         .getComponentIterator();
/* 182 */       Line ln1 = null;
/* 183 */       while (itLabeledLine1.hasNext()) {
/* 184 */         AbstractDrawableComponent de = (AbstractDrawableComponent)itLabeledLine1.next();
/* 185 */         if ((de instanceof Line)) {
/* 186 */           ln1 = (Line)de;
/*     */         }
/*     */       }
/*     */ 
/* 190 */       Iterator itLabeledLine2 = labeledLn2
/* 191 */         .getComponentIterator();
/* 192 */       Line ln2 = null;
/* 193 */       while (itLabeledLine1.hasNext()) {
/* 194 */         AbstractDrawableComponent de = (AbstractDrawableComponent)itLabeledLine2.next();
/* 195 */         if ((de instanceof Line)) {
/* 196 */           ln2 = (Line)de;
/*     */         }
/*     */       }
/*     */ 
/* 200 */       if ((ln1 == null) || (ln2 == null)) {
/* 201 */         return 0;
/*     */       }
/* 203 */       if (((Coordinate)ln1.getPoints().get(0)).x > ((Coordinate)ln2.getPoints().get(0)).x)
/* 204 */         return -1;
/* 205 */       if (((Coordinate)ln1.getPoints().get(0)).x < ((Coordinate)ln2.getPoints().get(0)).x) {
/* 206 */         return 1;
/*     */       }
/* 208 */       return 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Outlook
 * JD-Core Version:    0.6.2
 */