/*     */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ReduceGfaPoints
/*     */ {
/*     */   List<Coordinate> xyList;
/*     */   List<Integer> reduceFlg;
/*     */   List<Integer> rmFlg;
/*  40 */   List<Integer> orig = new ArrayList();
/*     */   List<Double> areaDiff;
/*     */   int reduceNum;
/*     */   int index;
/*     */   double incrPct;
/*     */   double incrDst;
/*     */   double incrPctOrig;
/*     */   String prefix;
/*     */ 
/*     */   public ReduceGfaPoints(String option)
/*     */   {
/*  52 */     if (option.equalsIgnoreCase("ALG_SIZE"))
/*  53 */       reduceBySize(this.xyList, this.reduceFlg, this.orig, this.reduceNum, this.incrPct, this.incrDst);
/*  54 */     else if (option.equalsIgnoreCase("ALG_PCT_DIST"))
/*  55 */       reduceByPctDist(this.xyList, this.reduceFlg, this.orig, this.incrPct, this.incrPctOrig, this.incrDst, this.prefix);
/*     */     else {
/*  57 */       reduceKeepConcav(this.xyList, this.reduceFlg, this.orig, this.incrPct, this.incrDst, this.prefix);
/*     */     }
/*  59 */     ReduceGfaPointsUtil.getStationTable();
/*     */   }
/*     */ 
/*     */   public static int reduceBySize(List<Coordinate> xyList, List<Integer> reduceFlg, List<Integer> orig, int reduceNum, double incrPct, double incrDst)
/*     */   {
/*  93 */     List resultList = new ArrayList();
/*  94 */     List listIa = new ArrayList();
/*  95 */     List listIb = new ArrayList();
/*  96 */     List rmFlg = new ArrayList();
/*  97 */     List areaDiff = new ArrayList();
/*  98 */     int xySize = xyList.size();
/*  99 */     int ptRemoveIdx = -1;
/* 100 */     double sizeDiff = 0.0D;
/* 101 */     double minDiff = 10000000000.0D;
/*     */ 
/* 103 */     if ((xyList == null) || (xyList.isEmpty())) {
/* 104 */       return -2;
/*     */     }
/* 106 */     xySize = xyList.size();
/*     */ 
/* 108 */     if (reduceNum < 3)
/* 109 */       return -1;
/* 110 */     if (reduceNum >= xySize) {
/* 111 */       return 1;
/*     */     }
/* 113 */     GeometryFactory gf = new GeometryFactory();
/* 114 */     GfaSnap.getInstance().reorderInClockwise(xyList, gf);
/*     */ 
/* 116 */     for (int i = 0; i < xySize; i++) {
/* 117 */       rmFlg.add(Integer.valueOf(1));
/* 118 */       areaDiff.add(Double.valueOf(10000000000.0D));
/*     */     }
/*     */ 
/* 124 */     ArrayList gridList = PgenUtil.latlonToGrid((ArrayList)xyList);
/*     */ 
/* 129 */     while (reduceNum < xySize) {
/* 130 */       minDiff = 10000000000.0D;
/* 131 */       listIb.clear();
/* 132 */       listIa.clear();
/* 133 */       int i = 0;
/*     */ 
/* 135 */       for (i = 0; i < xySize; i++)
/*     */       {
/* 137 */         if ((reduceFlg == null) || (((Integer)reduceFlg.get(i)).intValue() > 0)) {
/* 138 */           resultList = ReduceGfaPointsUtil.findRemovePt(gridList, reduceFlg, rmFlg, areaDiff, i, incrPct, incrDst);
/*     */ 
/* 140 */           sizeDiff = ((Double)areaDiff.get(i)).doubleValue();
/* 141 */           if ((((Integer)rmFlg.get(i)).intValue() >= 0) && (sizeDiff < minDiff))
/*     */           {
/* 143 */             minDiff = sizeDiff;
/* 144 */             ptRemoveIdx = i;
/*     */ 
/* 146 */             if (((Integer)rmFlg.get(i)).intValue() == 1) {
/* 147 */               if ((resultList != null) && (!resultList.isEmpty())) {
/* 148 */                 listIb.add(i, (Coordinate)resultList.get(0));
/* 149 */                 listIa.add(i, (Coordinate)resultList.get(1));
/*     */               }
/*     */               else {
/* 152 */                 listIb.add(null);
/* 153 */                 listIa.add(null);
/*     */               }
/*     */             }
/*     */             else {
/* 157 */               listIb.add(null);
/* 158 */               listIa.add(null);
/*     */             }
/*     */           }
/*     */           else {
/* 162 */             listIb.add(null);
/* 163 */             listIa.add(null);
/*     */           }
/*     */         }
/*     */         else {
/* 167 */           listIb.add(null);
/* 168 */           listIa.add(null);
/* 169 */           rmFlg.set(i, Integer.valueOf(-1));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 176 */       if (ptRemoveIdx == -1)
/*     */       {
/* 180 */         ArrayList temp = PgenUtil.gridToLatlon(gridList);
/*     */ 
/* 182 */         int j = 0;
/* 183 */         for (j = 0; j < temp.size(); j++) {
/* 184 */           xyList.set(j, (Coordinate)temp.get(j));
/*     */         }
/* 186 */         while (xyList.size() > j) {
/* 187 */           xyList.remove(xyList.size() - 1);
/*     */         }
/* 189 */         return 2;
/*     */       }
/*     */ 
/* 192 */       if (((Integer)rmFlg.get(ptRemoveIdx)).intValue() == 1) {
/* 193 */         int ib = (ptRemoveIdx - 1 + xySize) % xySize;
/* 194 */         int ia = (ptRemoveIdx + 1) % xySize;
/* 195 */         gridList.set(ib, (Coordinate)listIb.get(ptRemoveIdx));
/* 196 */         gridList.set(ia, (Coordinate)listIa.get(ptRemoveIdx));
/* 197 */         if (orig != null) {
/* 198 */           orig.set(ib, Integer.valueOf(0));
/* 199 */           orig.set(ia, Integer.valueOf(0));
/*     */         }
/*     */       }
/*     */ 
/* 203 */       if (((Integer)rmFlg.get(ptRemoveIdx)).intValue() >= 0) {
/*     */         try {
/* 205 */           gridList.remove(ptRemoveIdx);
/* 206 */           listIb.remove(ptRemoveIdx);
/* 207 */           listIa.remove(ptRemoveIdx);
/*     */ 
/* 209 */           if (orig != null)
/* 210 */             orig.remove(ptRemoveIdx);
/* 211 */           rmFlg.remove(ptRemoveIdx);
/* 212 */           areaDiff.remove(ptRemoveIdx);
/* 213 */           if (reduceFlg != null) {
/* 214 */             reduceFlg.remove(ptRemoveIdx);
/*     */           }
/* 216 */           xySize--;
/*     */         }
/*     */         catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
/*     */         {
/*     */         }
/*     */       }
/* 222 */       ptRemoveIdx = -1;
/*     */     }
/*     */ 
/* 229 */     ArrayList temp = PgenUtil.gridToLatlon(gridList);
/*     */ 
/* 231 */     int j = 0;
/* 232 */     for (j = 0; j < temp.size(); j++) {
/* 233 */       xyList.set(j, (Coordinate)temp.get(j));
/*     */     }
/* 235 */     while (xyList.size() > j) {
/* 236 */       xyList.remove(xyList.size() - 1);
/*     */     }
/* 238 */     return 0;
/*     */   }
/*     */ 
/*     */   public static int reduceByPctDist(List<Coordinate> xyList, List<Integer> reduceFlg, List<Integer> orig, double incrPct, double incrPctOrig, double incrDst, String prefix)
/*     */   {
/* 273 */     List resultList = new ArrayList();
/* 274 */     List listIa = new ArrayList();
/* 275 */     List listIb = new ArrayList();
/* 276 */     List rmFlg = new ArrayList();
/* 277 */     List areaDiff = new ArrayList();
/*     */ 
/* 279 */     int xySize = xyList.size();
/* 280 */     int ptRemoveIdx = -1;
/* 281 */     double sumSizeDiff = 0.0D;
/* 282 */     double minDiff = 10000000000.0D;
/*     */ 
/* 284 */     if ((xyList == null) || (xyList.isEmpty())) {
/* 285 */       return -2;
/*     */     }
/* 287 */     xySize = xyList.size();
/*     */ 
/* 289 */     if (xySize <= 3) {
/* 290 */       return -2;
/*     */     }
/* 292 */     GeometryFactory gf = new GeometryFactory();
/* 293 */     GfaSnap.getInstance().reorderInClockwise(xyList, gf);
/*     */ 
/* 295 */     for (int i = 0; i < xySize; i++) {
/* 296 */       rmFlg.add(Integer.valueOf(1));
/* 297 */       areaDiff.add(Double.valueOf(10000000000.0D));
/*     */     }
/*     */ 
/* 303 */     ArrayList gridList = PgenUtil.latlonToGrid((ArrayList)xyList);
/*     */ 
/* 305 */     if ((!ReduceGfaPointsUtil.canFormatted(xyList, prefix)) && (xySize > 3))
/*     */     {
/* 309 */       List xyCloseList = new ArrayList();
/* 310 */       for (Coordinate c : gridList) {
/* 311 */         xyCloseList.add(c);
/*     */       }
/*     */ 
/* 314 */       Double xyArea = ReduceGfaPointsUtil.getArea(xyCloseList);
/*     */ 
/* 320 */       for (int i = 0; i < xySize; i++)
/*     */       {
/* 322 */         if ((reduceFlg == null) || (((Integer)reduceFlg.get(i)).intValue() > 0)) {
/* 323 */           resultList = ReduceGfaPointsUtil.findRemovePt(gridList, reduceFlg, rmFlg, areaDiff, i, incrPct, incrDst);
/*     */ 
/* 325 */           if ((resultList != null) && (!resultList.isEmpty())) {
/* 326 */             listIb.add(i, (Coordinate)resultList.get(0));
/* 327 */             listIa.add(i, (Coordinate)resultList.get(1));
/*     */           }
/*     */           else {
/* 330 */             listIb.add(null);
/* 331 */             listIa.add(null);
/*     */           }
/*     */         }
/*     */         else {
/* 335 */           listIb.add(null);
/* 336 */           listIa.add(null);
/* 337 */           rmFlg.set(i, Integer.valueOf(-1));
/*     */         }
/*     */       }
/*     */ 
/* 341 */       sumSizeDiff = 0.0D;
/*     */ 
/* 343 */       ArrayList formatList = PgenUtil.gridToLatlon(gridList);
/* 344 */       while ((!ReduceGfaPointsUtil.canFormatted(formatList, prefix)) && (xySize > 3))
/*     */       {
/* 348 */         minDiff = 10000000000.0D;
/* 349 */         int i = 0;
/*     */ 
/* 351 */         for (i = 0; i < xySize; i++)
/*     */         {
/* 353 */           if (((reduceFlg == null) || (((Integer)reduceFlg.get(i)).intValue() > 0)) && 
/* 354 */             (((Integer)rmFlg.get(i)).intValue() >= 0) && (((Double)areaDiff.get(i)).doubleValue() < minDiff)) {
/* 355 */             minDiff = ((Double)areaDiff.get(i)).doubleValue();
/* 356 */             ptRemoveIdx = i;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 365 */         sumSizeDiff += minDiff;
/* 366 */         int ptFlg = 0;
/*     */ 
/* 374 */         if ((ptRemoveIdx == -1) || (100.0D * sumSizeDiff / xyArea.doubleValue() >= incrPctOrig))
/*     */         {
/* 378 */           ArrayList temp = PgenUtil.gridToLatlon(gridList);
/*     */ 
/* 380 */           int j = 0;
/* 381 */           for (j = 0; j < temp.size(); j++) {
/* 382 */             xyList.set(j, (Coordinate)temp.get(j));
/*     */           }
/* 384 */           while (xyList.size() > j) {
/* 385 */             xyList.remove(xyList.size() - 1);
/*     */           }
/* 387 */           return 2;
/*     */         }
/*     */ 
/* 393 */         if (((Integer)rmFlg.get(ptRemoveIdx)).intValue() == 1) {
/* 394 */           int ib = (ptRemoveIdx - 1 + xySize) % xySize;
/* 395 */           int ia = (ptRemoveIdx + 1) % xySize;
/* 396 */           gridList.set(ib, (Coordinate)listIb.get(ptRemoveIdx));
/* 397 */           gridList.set(ia, (Coordinate)listIa.get(ptRemoveIdx));
/* 398 */           if (orig != null) {
/* 399 */             orig.set(ib, Integer.valueOf(0));
/* 400 */             orig.set(ia, Integer.valueOf(0));
/*     */           }
/* 402 */           ptFlg = ((Integer)rmFlg.get(ptRemoveIdx)).intValue();
/*     */         }
/*     */ 
/* 407 */         if (((Integer)rmFlg.get(ptRemoveIdx)).intValue() >= 0) {
/*     */           try {
/* 409 */             gridList.remove(ptRemoveIdx);
/* 410 */             listIb.remove(ptRemoveIdx);
/* 411 */             listIa.remove(ptRemoveIdx);
/*     */ 
/* 413 */             if (orig != null)
/* 414 */               orig.remove(ptRemoveIdx);
/* 415 */             rmFlg.remove(ptRemoveIdx);
/* 416 */             areaDiff.remove(ptRemoveIdx);
/* 417 */             if (reduceFlg != null) {
/* 418 */               reduceFlg.remove(ptRemoveIdx);
/*     */             }
/* 420 */             xySize--;
/*     */           }
/*     */           catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
/*     */           {
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 430 */         if (ptFlg == 1)
/*     */         {
/* 432 */           int ia = (ptRemoveIdx + xySize) % xySize;
/* 433 */           if ((orig != null) && (((Integer)orig.get(ia)).intValue() == 0) && (
/* 434 */             (reduceFlg == null) || (((Integer)reduceFlg.get(ia)).intValue() == 1)))
/*     */           {
/* 436 */             Coordinate[] snappedA = new Coordinate[1];
/* 437 */             ArrayList transList = PgenUtil.gridToLatlon(gridList);
/*     */ 
/* 439 */             GfaSnap.getInstance().snapPtGFA(ia, ia, null, null, 
/* 440 */               transList, true, true, 3.0D, snappedA);
/*     */ 
/* 442 */             transList.set(ia, snappedA[0]);
/* 443 */             gridList = PgenUtil.latlonToGrid(transList);
/*     */           }
/*     */ 
/* 447 */           int ib = (ptRemoveIdx - 1 + xySize) % xySize;
/* 448 */           if ((orig != null) && (((Integer)orig.get(ib)).intValue() == 0) && (
/* 449 */             (reduceFlg == null) || (((Integer)reduceFlg.get(ib)).intValue() == 1)))
/*     */           {
/* 451 */             Coordinate[] snappedB = new Coordinate[1];
/* 452 */             ArrayList transList = PgenUtil.gridToLatlon(gridList);
/*     */ 
/* 454 */             GfaSnap.getInstance().snapPtGFA(ib, ib, null, null, 
/* 455 */               transList, true, true, 3.0D, snappedB);
/* 456 */             transList.set(ib, snappedB[0]);
/* 457 */             gridList = PgenUtil.latlonToGrid(transList);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 465 */         formatList = PgenUtil.gridToLatlon(gridList);
/* 466 */         if ((!ReduceGfaPointsUtil.canFormatted(formatList, prefix)) && (xySize > 3))
/*     */         {
/* 468 */           int ib = (ptRemoveIdx - 1 + xySize) % xySize;
/* 469 */           if ((reduceFlg == null) || (((Integer)reduceFlg.get(ib)).intValue() > 0)) {
/* 470 */             resultList = ReduceGfaPointsUtil.findRemovePt(gridList, reduceFlg, rmFlg, areaDiff, ib, incrPct, incrDst);
/*     */ 
/* 472 */             if ((resultList != null) && (!resultList.isEmpty())) {
/* 473 */               listIb.set(ib, (Coordinate)resultList.get(0));
/* 474 */               listIa.set(ib, (Coordinate)resultList.get(1));
/*     */             }
/*     */             else {
/* 477 */               listIb.set(ib, null);
/* 478 */               listIa.set(ib, null);
/*     */             }
/*     */           }
/*     */           else {
/* 482 */             listIb.set(ib, null);
/* 483 */             listIa.set(ib, null);
/* 484 */             rmFlg.set(ib, Integer.valueOf(-1));
/*     */           }
/*     */ 
/* 487 */           int ia = (ptRemoveIdx + xySize) % xySize;
/* 488 */           if ((reduceFlg == null) || (((Integer)reduceFlg.get(ia)).intValue() > 0)) {
/* 489 */             resultList = ReduceGfaPointsUtil.findRemovePt(gridList, reduceFlg, rmFlg, areaDiff, ia, incrPct, incrDst);
/*     */ 
/* 491 */             if ((resultList != null) && (!resultList.isEmpty())) {
/* 492 */               listIb.set(ia, (Coordinate)resultList.get(0));
/* 493 */               listIa.set(ia, (Coordinate)resultList.get(1));
/*     */             }
/*     */             else {
/* 496 */               listIb.set(ia, null);
/* 497 */               listIa.set(ia, null);
/*     */             }
/*     */           }
/*     */           else {
/* 501 */             listIb.set(ia, null);
/* 502 */             listIa.set(ia, null);
/* 503 */             rmFlg.set(ia, Integer.valueOf(-1));
/*     */           }
/*     */ 
/* 506 */           if (ptFlg == 1) {
/* 507 */             ib = (ptRemoveIdx - 2 + xySize) % xySize;
/* 508 */             if ((reduceFlg == null) || (((Integer)reduceFlg.get(ib)).intValue() > 0)) {
/* 509 */               resultList = ReduceGfaPointsUtil.findRemovePt(gridList, reduceFlg, rmFlg, areaDiff, ib, incrPct, incrDst);
/*     */ 
/* 511 */               if ((resultList != null) && (!resultList.isEmpty())) {
/* 512 */                 listIb.set(ib, (Coordinate)resultList.get(0));
/* 513 */                 listIa.set(ib, (Coordinate)resultList.get(1));
/*     */               }
/*     */               else {
/* 516 */                 listIb.set(ib, null);
/* 517 */                 listIa.set(ib, null);
/*     */               }
/*     */             } else {
/* 520 */               listIb.set(ib, null);
/* 521 */               listIa.set(ib, null);
/* 522 */               rmFlg.set(ib, Integer.valueOf(-1));
/*     */             }
/*     */ 
/* 525 */             ia = (ptRemoveIdx + 1 + xySize) % xySize;
/* 526 */             if ((reduceFlg == null) || (((Integer)reduceFlg.get(ia)).intValue() > 0)) {
/* 527 */               resultList = ReduceGfaPointsUtil.findRemovePt(gridList, reduceFlg, rmFlg, areaDiff, ia, incrPct, incrDst);
/*     */ 
/* 529 */               if ((resultList != null) && (!resultList.isEmpty())) {
/* 530 */                 listIb.set(ia, (Coordinate)resultList.get(0));
/* 531 */                 listIa.set(ia, (Coordinate)resultList.get(1));
/*     */               }
/*     */               else {
/* 534 */                 listIb.set(ia, null);
/* 535 */                 listIa.set(ia, null);
/*     */               }
/*     */             } else {
/* 538 */               listIb.set(ia, null);
/* 539 */               listIa.set(ia, null);
/* 540 */               rmFlg.set(ia, Integer.valueOf(-1));
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 546 */         ptRemoveIdx = -1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 554 */     ArrayList temp = PgenUtil.gridToLatlon(gridList);
/*     */ 
/* 556 */     int j = 0;
/* 557 */     for (j = 0; j < temp.size(); j++) {
/* 558 */       xyList.set(j, (Coordinate)temp.get(j));
/*     */     }
/* 560 */     while (xyList.size() > j) {
/* 561 */       xyList.remove(xyList.size() - 1);
/*     */     }
/* 563 */     return 0;
/*     */   }
/*     */ 
/*     */   public static int reduceKeepConcav(List<Coordinate> xyList, List<Integer> reduceFlg, List<Integer> orig, double incrPct, double incrDst, String prefix)
/*     */   {
/* 588 */     List resultList = new ArrayList();
/* 589 */     List listIa = new ArrayList();
/* 590 */     List listIb = new ArrayList();
/* 591 */     List rmFlg = new ArrayList();
/* 592 */     List areaDiff = new ArrayList();
/* 593 */     int xySize = xyList.size();
/* 594 */     int ptRemoveIdx = -1;
/* 595 */     double sizeDiff = 0.0D;
/* 596 */     double minDiff = 10000000000.0D;
/*     */ 
/* 598 */     if ((xyList == null) || (xyList.isEmpty())) {
/* 599 */       return -2;
/*     */     }
/* 601 */     xySize = xyList.size();
/*     */ 
/* 603 */     if (xySize <= 3) {
/* 604 */       return -2;
/*     */     }
/* 606 */     GeometryFactory gf = new GeometryFactory();
/* 607 */     GfaSnap.getInstance().reorderInClockwise(xyList, gf);
/*     */ 
/* 609 */     for (int i = 0; i < xySize; i++) {
/* 610 */       rmFlg.add(Integer.valueOf(1));
/* 611 */       areaDiff.add(Double.valueOf(10000000000.0D));
/*     */     }
/*     */ 
/* 617 */     ArrayList gridList = PgenUtil.latlonToGrid((ArrayList)xyList);
/*     */     int j;
/* 619 */     for (; (!ReduceGfaPointsUtil.canFormatted(xyList, prefix)) && (xySize > 3); 
/* 724 */       j < xySize)
/*     */     {
/* 620 */       minDiff = 10000000000.0D;
/* 621 */       listIb.clear();
/* 622 */       listIa.clear();
/* 623 */       int i = 0;
/*     */ 
/* 625 */       for (i = 0; i < xySize; i++)
/*     */       {
/* 627 */         if ((reduceFlg == null) || (((Integer)reduceFlg.get(i)).intValue() > 0)) {
/* 628 */           resultList = ReduceGfaPointsUtil.findRemovePt(gridList, reduceFlg, rmFlg, areaDiff, i, incrPct, incrDst);
/*     */ 
/* 631 */           sizeDiff = ((Double)areaDiff.get(i)).doubleValue();
/* 632 */           if ((((Integer)rmFlg.get(i)).intValue() >= 0) && (sizeDiff < minDiff))
/*     */           {
/* 634 */             minDiff = sizeDiff;
/* 635 */             ptRemoveIdx = i;
/*     */ 
/* 637 */             if (((Integer)rmFlg.get(i)).intValue() == 1) {
/* 638 */               if ((resultList != null) && (!resultList.isEmpty())) {
/* 639 */                 listIb.add(i, (Coordinate)resultList.get(0));
/* 640 */                 listIa.add(i, (Coordinate)resultList.get(1));
/*     */               }
/*     */               else {
/* 643 */                 listIb.add(null);
/* 644 */                 listIa.add(null);
/*     */               }
/*     */             }
/*     */             else {
/* 648 */               listIb.add(null);
/* 649 */               listIa.add(null);
/*     */             }
/*     */           }
/*     */           else {
/* 653 */             listIb.add(null);
/* 654 */             listIa.add(null);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 659 */           listIb.add(null);
/* 660 */           listIa.add(null);
/* 661 */           rmFlg.set(i, Integer.valueOf(-1));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 668 */       if (ptRemoveIdx == -1)
/*     */       {
/* 672 */         ArrayList temp = PgenUtil.gridToLatlon(gridList);
/*     */ 
/* 674 */         int j = 0;
/* 675 */         for (j = 0; j < temp.size(); j++) {
/* 676 */           xyList.set(j, (Coordinate)temp.get(j));
/*     */         }
/* 678 */         while (xyList.size() > j) {
/* 679 */           xyList.remove(xyList.size() - 1);
/*     */         }
/* 681 */         return 2;
/*     */       }
/*     */ 
/* 685 */       if (((Integer)rmFlg.get(ptRemoveIdx)).intValue() == 1) {
/* 686 */         int ib = (ptRemoveIdx - 1 + xySize) % xySize;
/* 687 */         int ia = (ptRemoveIdx + 1) % xySize;
/* 688 */         gridList.set(ib, (Coordinate)listIb.get(ptRemoveIdx));
/* 689 */         gridList.set(ia, (Coordinate)listIa.get(ptRemoveIdx));
/* 690 */         if (orig != null) {
/* 691 */           orig.set(ib, Integer.valueOf(0));
/* 692 */           orig.set(ia, Integer.valueOf(0));
/*     */         }
/*     */       }
/*     */ 
/* 696 */       if (((Integer)rmFlg.get(ptRemoveIdx)).intValue() >= 0) {
/*     */         try {
/* 698 */           gridList.remove(ptRemoveIdx);
/* 699 */           listIb.remove(ptRemoveIdx);
/* 700 */           listIa.remove(ptRemoveIdx);
/*     */ 
/* 702 */           if (orig != null)
/* 703 */             orig.remove(ptRemoveIdx);
/* 704 */           rmFlg.remove(ptRemoveIdx);
/* 705 */           areaDiff.remove(ptRemoveIdx);
/* 706 */           if (reduceFlg != null) {
/* 707 */             reduceFlg.remove(ptRemoveIdx);
/*     */           }
/* 709 */           xySize--;
/*     */         }
/*     */         catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
/*     */         {
/*     */         }
/*     */       }
/* 715 */       ptRemoveIdx = -1;
/*     */ 
/* 724 */       j = 0; continue;
/* 725 */       if ((orig != null) && (((Integer)orig.get(j)).intValue() == 0) && (
/* 726 */         (reduceFlg == null) || (((Integer)reduceFlg.get(j)).intValue() == 1)))
/*     */       {
/* 728 */         Coordinate[] snapped = new Coordinate[1];
/* 729 */         ArrayList transList = PgenUtil.gridToLatlon(gridList);
/*     */ 
/* 731 */         GfaSnap.getInstance().snapPtGFA(j, j, null, null, 
/* 732 */           transList, true, true, 3.0D, snapped);
/*     */ 
/* 734 */         transList.set(j, snapped[0]);
/* 735 */         gridList = PgenUtil.latlonToGrid(transList);
/*     */       }
/* 724 */       j++;
/*     */     }
/*     */ 
/* 743 */     ArrayList temp = PgenUtil.gridToLatlon(gridList);
/*     */ 
/* 745 */     int j = 0;
/* 746 */     for (j = 0; j < temp.size(); j++) {
/* 747 */       xyList.set(j, (Coordinate)temp.get(j));
/*     */     }
/* 749 */     while (xyList.size() > j)
/* 750 */       xyList.remove(xyList.size() - 1);
/* 751 */     return 0;
/*     */   }
/*     */ 
/*     */   public void setXyList(List<Coordinate> xyList)
/*     */   {
/* 759 */     this.xyList = xyList;
/*     */   }
/*     */ 
/*     */   public List<Coordinate> getXyList() {
/* 763 */     return this.xyList;
/*     */   }
/*     */ 
/*     */   public void setReduceFlg(List<Integer> reduceFlg) {
/* 767 */     this.reduceFlg = reduceFlg;
/*     */   }
/*     */ 
/*     */   public List<Integer> getReduceFlg() {
/* 771 */     return this.reduceFlg;
/*     */   }
/*     */ 
/*     */   public void setRmFlg(List<Integer> rmFlg) {
/* 775 */     this.rmFlg = rmFlg;
/*     */   }
/*     */ 
/*     */   public List<Integer> getRmFlg() {
/* 779 */     return this.rmFlg;
/*     */   }
/*     */ 
/*     */   public void setOrig(List<Integer> orig) {
/* 783 */     this.orig = orig;
/*     */   }
/*     */ 
/*     */   public List<Integer> getOrig() {
/* 787 */     return this.orig;
/*     */   }
/*     */ 
/*     */   public void setAreaDiff(List<Double> areaDiff) {
/* 791 */     this.areaDiff = areaDiff;
/*     */   }
/*     */ 
/*     */   public List<Double> getAreaDiff() {
/* 795 */     return this.areaDiff;
/*     */   }
/*     */ 
/*     */   public void setReduceNum(int reduceNum) {
/* 799 */     this.reduceNum = reduceNum;
/*     */   }
/*     */ 
/*     */   public int getReduceNum() {
/* 803 */     return this.reduceNum;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/* 807 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/* 811 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIncrPct(double incrPct) {
/* 815 */     this.incrPct = incrPct;
/*     */   }
/*     */ 
/*     */   public double getIncrPct() {
/* 819 */     return this.incrPct;
/*     */   }
/*     */ 
/*     */   public void setIncrDst(double incrDst) {
/* 823 */     this.incrDst = incrDst;
/*     */   }
/*     */ 
/*     */   public double getIncrDst() {
/* 827 */     return this.incrDst;
/*     */   }
/*     */ 
/*     */   public void setIncrPctOrig(double incrPctOrig) {
/* 831 */     this.incrPctOrig = incrPctOrig;
/*     */   }
/*     */ 
/*     */   public double getIncrPctOrig() {
/* 835 */     return this.incrPctOrig;
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix) {
/* 839 */     this.prefix = prefix;
/*     */   }
/*     */ 
/*     */   public String getPrefix() {
/* 843 */     return this.prefix;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.ReduceGfaPoints
 * JD-Core Version:    0.6.2
 */