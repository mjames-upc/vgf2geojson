/*      */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*      */ 
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.CurveFitter;
/*      */ 
/*      */ public class PgenModifyLine
/*      */ {
/*   38 */   private static double TIE_DIST = 10.0D;
/*   39 */   private static int GHOST_POINTS = 3;
/*   40 */   private static double PRECISION = 0.001D;
/*   41 */   private static double GDIFFD = 1.0E-06D;
/*   42 */   private static float DEV_SCALE = 50.0F;
/*      */ 
/*   47 */   private double[][] originalPts = null;
/*   48 */   private double[][] clickPts = null;
/*   49 */   private boolean closed = false;
/*   50 */   private int smoothLevel = 0;
/*      */ 
/*   55 */   private double[][] modifiedPts = null;
/*   56 */   private double[][] ghostPts = null;
/*   57 */   private int is = 0;
/*   58 */   private int ie = 0;
/*   59 */   private int np = 0;
/*      */ 
/*      */   public PgenModifyLine()
/*      */   {
/*      */   }
/*      */ 
/*      */   public PgenModifyLine(double[][] opt, double[][] cpt, boolean closed, int smthLevel)
/*      */   {
/*   76 */     setOriginalPts(opt);
/*   77 */     setClickPts(cpt);
/*   78 */     setClosed(closed);
/*   79 */     setSmoothLevel(smthLevel);
/*      */ 
/*   81 */     if (closed)
/*   82 */       setOriginalPts(ensureClosed(opt));
/*      */   }
/*      */ 
/*      */   public void setClickPts(double[][] clickPts)
/*      */   {
/*   92 */     this.clickPts = clickPts;
/*      */   }
/*      */ 
/*      */   public double[][] getClickPts()
/*      */   {
/*  100 */     return this.clickPts;
/*      */   }
/*      */ 
/*      */   public void setOriginalPts(double[][] originalPts)
/*      */   {
/*  108 */     this.originalPts = originalPts;
/*      */   }
/*      */ 
/*      */   public double[][] getOriginalPts()
/*      */   {
/*  116 */     return this.originalPts;
/*      */   }
/*      */ 
/*      */   public void setSmoothLevel(int smoothLevel)
/*      */   {
/*  123 */     this.smoothLevel = smoothLevel;
/*      */   }
/*      */ 
/*      */   public int getSmoothLevel()
/*      */   {
/*  130 */     return this.smoothLevel;
/*      */   }
/*      */ 
/*      */   public void setClosed(boolean closed)
/*      */   {
/*  138 */     this.closed = closed;
/*      */   }
/*      */ 
/*      */   public boolean isClosed()
/*      */   {
/*  145 */     return this.closed;
/*      */   }
/*      */ 
/*      */   public double[][] getModifiedPts()
/*      */   {
/*  152 */     return this.modifiedPts;
/*      */   }
/*      */ 
/*      */   public void setModifiedPts(double[][] modifiedPts)
/*      */   {
/*  159 */     this.modifiedPts = modifiedPts;
/*      */   }
/*      */ 
/*      */   public void setGhostPts(double[][] ghostPts)
/*      */   {
/*  166 */     this.ghostPts = ghostPts;
/*      */   }
/*      */ 
/*      */   public double[][] getGhostPts()
/*      */   {
/*  173 */     return this.ghostPts;
/*      */   }
/*      */ 
/*      */   public void PerformModify()
/*      */   {
/*      */     double[][] origPts;
/*      */     double[][] origPts;
/*  185 */     if (this.closed) {
/*  186 */       origPts = ensureClosed(this.originalPts);
/*      */     }
/*      */     else {
/*  189 */       origPts = this.originalPts;
/*      */     }
/*      */ 
/*  196 */     int ipo = origPts.length;
/*  197 */     int ipc = this.clickPts.length;
/*      */ 
/*  199 */     double[] xpo = new double[ipo];
/*  200 */     double[] ypo = new double[ipo];
/*  201 */     double[] xpc = new double[ipc];
/*  202 */     double[] ypc = new double[ipc];
/*      */ 
/*  204 */     for (int ii = 0; ii < ipo; ii++) {
/*  205 */       xpo[ii] = origPts[ii][0];
/*  206 */       ypo[ii] = origPts[ii][1];
/*      */     }
/*      */ 
/*  209 */     for (int ii = 0; ii < ipc; ii++) {
/*  210 */       xpc[ii] = this.clickPts[ii][0];
/*  211 */       ypc[ii] = this.clickPts[ii][1];
/*      */     }
/*      */ 
/*  218 */     setModifiedPts(cv_mdfy(xpo, ypo, xpc, ypc, this.smoothLevel, this.closed));
/*      */ 
/*  224 */     setGhostPts(getModifiedPts());
/*      */   }
/*      */ 
/*      */   private double[][] ensureClosed(double[][] data)
/*      */   {
/*  236 */     int n = data.length - 1;
/*      */ 
/*  241 */     if ((data[0][0] == data[n][0]) && (data[0][1] == data[n][1])) {
/*  242 */       return data;
/*      */     }
/*      */ 
/*  248 */     double[][] newdata = new double[data.length + 1][data[0].length];
/*      */ 
/*  250 */     for (int i = 0; i < data.length; i++) {
/*  251 */       newdata[i] = data[i];
/*      */     }
/*      */ 
/*  254 */     newdata[data.length] = newdata[0];
/*      */ 
/*  256 */     return newdata;
/*      */   }
/*      */ 
/*      */   private double distance(double x1, double y1, double x2, double y2)
/*      */   {
/*  270 */     return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
/*      */   }
/*      */ 
/*      */   private double[][] cv_mdfy(double[] xo, double[] yo, double[] xc, double[] yc, int sm_lvl, boolean closed)
/*      */   {
/*  327 */     boolean modified = false;
/*      */ 
/*  345 */     int npo = xo.length;
/*  346 */     int npc = xc.length;
/*      */ 
/*  348 */     this.is = 0;
/*  349 */     this.ie = (npo - 1);
/*      */ 
/*  351 */     modified = false;
/*  352 */     int op_index = -1;
/*  353 */     int os_index = -1;
/*      */ 
/*  355 */     double[][] newLine = null;
/*      */ 
/*  360 */     double[][] smoothPts = new double[npo][2];
/*  361 */     for (int ii = 0; ii < npo; ii++) {
/*  362 */       smoothPts[ii][0] = xo[ii];
/*  363 */       smoothPts[ii][1] = yo[ii];
/*      */     }
/*      */ 
/*  366 */     if (closed) {
/*  367 */       smoothPts = ensureClosed(smoothPts);
/*      */     }
/*      */ 
/*  370 */     if (sm_lvl > 0)
/*      */     {
/*      */       float density;
/*      */       float density;
/*  374 */       if (sm_lvl == 1) {
/*  375 */         density = DEV_SCALE / 1.0F;
/*      */       }
/*      */       else {
/*  378 */         density = DEV_SCALE / 5.0F;
/*      */       }
/*      */ 
/*  381 */       smoothPts = CurveFitter.fitParametricCurve(smoothPts, density);
/*      */ 
/*  383 */       if (closed) {
/*  384 */         smoothPts = ensureClosed(smoothPts);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  389 */     int sm_pts = smoothPts.length;
/*      */ 
/*  391 */     double[] sm_x = new double[sm_pts];
/*  392 */     double[] sm_y = new double[sm_pts];
/*      */ 
/*  394 */     for (ii = 0; ii < sm_pts; ii++) {
/*  395 */       sm_x[ii] = smoothPts[ii][0];
/*  396 */       sm_y[ii] = smoothPts[ii][1];
/*      */     }
/*      */ 
/*  403 */     double[] xcp = new double[npc];
/*  404 */     double[] ycp = new double[npc];
/*      */ 
/*  406 */     for (ii = 0; ii < npc; ii++) {
/*  407 */       xcp[ii] = xc[ii];
/*  408 */       ycp[ii] = yc[ii];
/*      */     }
/*      */ 
/*  414 */     if ((sm_lvl != 1) && (sm_lvl != 2))
/*      */     {
/*  416 */       op_index = cvm_cptv(xo, yo, xcp[0], ycp[0], closed);
/*  417 */       if (op_index == -1) {
/*  418 */         os_index = cvm_cptl(xo, yo, xcp[0], ycp[0]);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  424 */       op_index = cvm_cptv(sm_x, sm_y, xcp[0], ycp[0], closed);
/*  425 */       if (op_index == -1) {
/*  426 */         os_index = cvm_cptl(sm_x, sm_y, xcp[0], ycp[0]);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  436 */     if ((op_index < 0) && (os_index < 0))
/*      */     {
/*  438 */       this.is = 0;
/*  439 */       this.np = npc;
/*  440 */       this.ie = (this.np - 1);
/*      */ 
/*  442 */       if ((closed) && (npc > 1) && 
/*  443 */         (Math.abs(xc[0] - xc[(npc - 1)]) < PRECISION) && 
/*  444 */         (Math.abs(yc[0] - yc[(npc - 1)]) < PRECISION)) {
/*  445 */         this.np -= 1;
/*      */       }
/*      */ 
/*  448 */       newLine = new double[this.np][2];
/*  449 */       for (ii = 0; ii < this.np; ii++) {
/*  450 */         newLine[ii][0] = xc[ii];
/*  451 */         newLine[ii][1] = yc[ii];
/*      */       }
/*      */ 
/*  454 */       modified = true;
/*      */     }
/*      */ 
/*  464 */     if (!modified)
/*      */     {
/*      */       double fang1;
/*      */       double fang1;
/*  466 */       if (npc > 1) {
/*  467 */         fang1 = cvm_angl(xc[0], yc[0], xc[1], yc[1]);
/*      */       }
/*      */       else {
/*  470 */         fang1 = cvm_angl(xc[0], yc[0], xc[0], yc[0]);
/*      */       }
/*      */ 
/*  473 */       double fang2 = cvm_angl(xc[0], yc[0], xc[(npc - 1)], yc[(npc - 1)]);
/*      */ 
/*  475 */       double dfl = distance(xc[0], yc[0], xc[(npc - 1)], yc[(npc - 1)]);
/*      */ 
/*  477 */       if (((op_index == -1) && (os_index == -1)) || (
/*  478 */         (npc < 2) || (
/*  479 */         (closed) && (dfl <= TIE_DIST) && (
/*  480 */         (npc <= 3) || (Math.abs(fang1 - fang2) < PRECISION)))))
/*      */       {
/*  482 */         this.is = 0;
/*  483 */         this.ie = (npo - 1);
/*  484 */         this.np = npo;
/*      */ 
/*  486 */         newLine = new double[this.np][2];
/*  487 */         for (ii = 0; ii < this.np; ii++) {
/*  488 */           newLine[ii][0] = xo[ii];
/*  489 */           newLine[ii][1] = yo[ii];
/*      */         }
/*      */ 
/*  492 */         modified = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  501 */     if ((!modified) && (closed))
/*      */     {
/*  503 */       int osd = -1;
/*  504 */       int opd = cvm_cptv(xo, yo, xcp[0], ycp[0], closed);
/*  505 */       if (opd >= 0)
/*      */       {
/*  507 */         newLine = cvm_mdfy(xo, yo, xcp, ycp, closed, opd, osd);
/*      */ 
/*  509 */         cvm_index(newLine.length, this.is, this.ie, closed);
/*      */ 
/*  511 */         modified = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  519 */     if ((!modified) && (sm_lvl != 1) && (sm_lvl != 2))
/*      */     {
/*  521 */       newLine = cvm_mdfy(sm_x, sm_y, xcp, ycp, closed, op_index, os_index);
/*      */ 
/*  523 */       cvm_index(newLine.length, this.is, this.ie, closed);
/*      */ 
/*  525 */       modified = true;
/*      */     }
/*      */ 
/*  532 */     if (!modified)
/*      */     {
/*  534 */       double[][] tmpLine = cvm_mdfy(sm_x, sm_y, xcp, ycp, closed, op_index, os_index);
/*      */ 
/*  536 */       newLine = new double[tmpLine.length][2];
/*      */ 
/*  542 */       int nn = 0;
/*  543 */       for (ii = 0; ii < tmpLine.length; ii++) {
/*  544 */         int flag = -1;
/*      */ 
/*  546 */         for (int jj = 0; jj < npc; jj++) {
/*  547 */           if ((flag == -1) && 
/*  548 */             (Math.abs(xcp[jj] - tmpLine[ii][0]) < PRECISION) && 
/*  549 */             (Math.abs(ycp[jj] - tmpLine[ii][1]) < PRECISION))
/*      */           {
/*  551 */             newLine[nn][0] = tmpLine[ii][0];
/*  552 */             newLine[nn][1] = tmpLine[ii][1];
/*      */ 
/*  554 */             nn++;
/*  555 */             flag = 1;
/*      */           }
/*      */         }
/*      */ 
/*  559 */         if (flag == -1) {
/*  560 */           for (jj = 0; jj < npo; jj++)
/*      */           {
/*  562 */             if ((flag == -1) && 
/*  563 */               (Math.abs(xo[jj] - tmpLine[ii][0]) < PRECISION) && 
/*  564 */               (Math.abs(yo[jj] - tmpLine[ii][1]) < PRECISION))
/*      */             {
/*  566 */               newLine[nn][0] = tmpLine[ii][0];
/*  567 */               newLine[nn][1] = tmpLine[ii][1];
/*      */ 
/*  569 */               nn++;
/*  570 */               flag = 1;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  580 */       if (closed)
/*      */       {
/*  582 */         int opd = cvm_cptv(xo, yo, xc[(npc - 1)], yc[(npc - 1)], closed);
/*  583 */         if (opd >= 0) {
/*  584 */           int osd = -1;
/*  585 */           for (ii = 0; ii < nn - 1; ii++) {
/*  586 */             if ((osd == -1) && (
/*  587 */               ((Math.abs(newLine[ii][0] - xc[(npc - 1)]) < PRECISION) && 
/*  588 */               (Math.abs(newLine[ii][1] - yc[(npc - 1)]) < PRECISION) && 
/*  589 */               (Math.abs(newLine[(ii + 1)][0] - xo[opd]) < PRECISION) && 
/*  590 */               (Math.abs(newLine[(ii + 1)][1] - yo[opd]) < PRECISION)) || (
/*  591 */               (Math.abs(newLine[ii][0] - xo[opd]) < PRECISION) && 
/*  592 */               (Math.abs(newLine[ii][1] - yo[opd]) < PRECISION) && 
/*  593 */               (Math.abs(newLine[(ii + 1)][0] - xc[(npc - 1)]) < PRECISION) && 
/*  594 */               (Math.abs(newLine[(ii + 1)][1] - yc[(npc - 1)]) < PRECISION))))
/*      */             {
/*  596 */               osd = ii;
/*      */             }
/*      */           }
/*      */ 
/*  600 */           if (osd >= 0) {
/*  601 */             newLine[osd][0] = xc[(npc - 1)];
/*  602 */             newLine[osd][1] = yc[(npc - 1)];
/*      */ 
/*  604 */             for (ii = osd + 1; ii < nn - 1; ii++) {
/*  605 */               newLine[ii][0] = newLine[(ii + 1)][0];
/*  606 */               newLine[ii][1] = newLine[(ii + 1)][1];
/*      */             }
/*      */ 
/*  609 */             nn--;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  614 */       this.np = nn;
/*      */ 
/*  616 */       while ((this.np > 1) && (distance(newLine[(this.np - 1)][0], newLine[(this.np - 1)][1], 
/*  617 */         newLine[(this.np - 2)][0], newLine[(this.np - 2)][1]) < TIE_DIST)) {
/*  618 */         this.np -= 1;
/*      */       }
/*      */ 
/*  624 */       nn = this.np;
/*  625 */       if (closed) nn = this.np - 1;
/*      */ 
/*  627 */       int flag = -1;
/*  628 */       for (ii = 0; ii < nn; ii++) {
/*  629 */         if ((flag == -1) && 
/*  630 */           (Math.abs(xcp[0] - newLine[ii][0]) < PRECISION) && 
/*  631 */           (Math.abs(ycp[0] - newLine[ii][1]) < PRECISION))
/*      */         {
/*  633 */           this.is = ii;
/*  634 */           flag = 1;
/*      */         }
/*      */       }
/*      */ 
/*  638 */       flag = -1;
/*  639 */       for (ii = this.is + 1; ii < nn; ii++) {
/*  640 */         if ((flag == -1) && 
/*  641 */           (Math.abs(xcp[(npc - 1)] - newLine[ii][0]) < PRECISION) && 
/*  642 */           (Math.abs(ycp[(npc - 1)] - newLine[ii][1]) < PRECISION))
/*      */         {
/*  644 */           this.ie = ii;
/*  645 */           flag = 1;
/*      */         }
/*      */       }
/*      */ 
/*  649 */       cvm_index(this.np, this.is, this.ie, closed);
/*      */     }
/*      */ 
/*  657 */     double[][] modLine = null;
/*  658 */     if ((newLine != null) && (newLine.length > 0))
/*      */     {
/*  660 */       int len = this.np;
/*  661 */       if ((closed) && (Math.max(op_index, os_index) >= 0)) {
/*  662 */         len--;
/*      */       }
/*      */ 
/*  666 */       modLine = new double[len][2];
/*  667 */       for (ii = 0; ii < len; ii++) {
/*  668 */         modLine[ii][0] = newLine[ii][0];
/*  669 */         modLine[ii][1] = newLine[ii][1];
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  674 */     return modLine;
/*      */   }
/*      */ 
/*      */   private int cvm_cptv(double[] xin, double[] yin, double xci, double yci, boolean closed)
/*      */   {
/*  708 */     int opi = -1;
/*  709 */     double min_dist = TIE_DIST;
/*      */ 
/*  711 */     int npi = xin.length;
/*  712 */     int check_points = npi;
/*  713 */     if (closed) check_points = npi - 1;
/*      */ 
/*  715 */     for (int ii = 0; ii < check_points; ii++)
/*      */     {
/*  717 */       double ds = distance(xci, yci, xin[ii], yin[ii]);
/*      */ 
/*  719 */       if (ds <= min_dist) {
/*  720 */         min_dist = ds;
/*  721 */         opi = ii;
/*      */       }
/*      */     }
/*      */ 
/*  725 */     return opi;
/*      */   }
/*      */ 
/*      */   private int cvm_cptl(double[] xin, double[] yin, double xci, double yci)
/*      */   {
/*  759 */     int osi = -1;
/*  760 */     double min_dist = TIE_DIST;
/*  761 */     double d_line = TIE_DIST * 10.0D;
/*      */ 
/*  763 */     int npi = xin.length;
/*      */ 
/*  765 */     for (int ii = 0; ii < npi - 1; ii++) {
/*  766 */       double at = xin[(ii + 1)] - xin[ii];
/*  767 */       double bt = yin[(ii + 1)] - yin[ii];
/*      */ 
/*  769 */       if ((Math.abs(at) < GDIFFD) && (Math.abs(bt) < GDIFFD)) {
/*  770 */         d_line = distance(xci, yci, xin[ii], yin[ii]);
/*      */       }
/*  772 */       else if ((Math.abs(at) < GDIFFD) && (Math.abs(bt) > 0.0D)) {
/*  773 */         d_line = Math.abs(xci - xin[ii]);
/*      */       }
/*  775 */       else if ((Math.abs(bt) < GDIFFD) && (Math.abs(at) > 0.0D)) {
/*  776 */         d_line = Math.abs(yci - yin[ii]);
/*      */       }
/*      */       else {
/*  779 */         double a = 1.0D / at;
/*  780 */         double b = -1.0D / bt;
/*  781 */         double c = yin[ii] / bt - xin[ii] / at;
/*      */ 
/*  783 */         double dd = a * xci + b * yci + c;
/*  784 */         d_line = Math.abs(dd / Math.sqrt(a * a + b * b));
/*      */       }
/*      */ 
/*  787 */       if (d_line <= min_dist)
/*      */       {
/*  789 */         double dleft = distance(xci, yci, xin[ii], yin[ii]);
/*  790 */         double dright = distance(xci, yci, xin[(ii + 1)], yin[(ii + 1)]);
/*      */ 
/*  792 */         double ds = distance(xin[(ii + 1)], yin[(ii + 1)], xin[ii], yin[ii]);
/*      */ 
/*  794 */         double cond1 = dleft * dleft + ds * ds - dright * dright;
/*  795 */         double cond2 = dright * dright + ds * ds - dleft * dleft;
/*      */ 
/*  797 */         if ((cond1 >= 0.0D) && (cond2 >= 0.0D)) {
/*  798 */           min_dist = d_line;
/*  799 */           osi = ii;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  804 */     return osi;
/*      */   }
/*      */ 
/*      */   private double cvm_angl(double xs, double ys, double xe, double ye)
/*      */   {
/*  834 */     double angle = 90.0D;
/*  835 */     double dx = xe - xs;
/*  836 */     double dy = ye - ys;
/*      */ 
/*  838 */     if (Math.abs(dx) > GDIFFD) {
/*  839 */       angle = Math.toDegrees(Math.atan(Math.abs(dy / dx)));
/*      */     }
/*      */ 
/*  842 */     if ((dx > 0.0D) && (dy > 0.0D)) angle = 360.0D - angle;
/*  843 */     if ((dx < 0.0D) && (dy >= 0.0D)) angle += 180.0D;
/*  844 */     if ((dx < 0.0D) && (dy < 0.0D)) angle = 180.0D - angle;
/*      */ 
/*  846 */     return angle;
/*      */   }
/*      */ 
/*      */   private double[][] cvm_mdfy(double[] xo, double[] yo, double[] xc, double[] yc, boolean closed, int opi, int osi)
/*      */   {
/*  884 */     double[][] newLine = null;
/*      */ 
/*  889 */     int npo = xo.length;
/*  890 */     int npc = xc.length;
/*  891 */     double direction = 0.0D;
/*  892 */     int new_npo = npo;
/*  893 */     int epi = -1;
/*  894 */     int esi = -1;
/*      */ 
/*  899 */     if (closed) {
/*  900 */       int to_stop = 0;
/*  901 */       int ii = npo - 1;
/*  902 */       while ((to_stop == 0) && (ii >= npo / 2)) {
/*  903 */         if (distance(xo[ii], yo[ii], xo[(ii - 1)], yo[(ii - 1)]) < PRECISION)
/*  904 */           new_npo--;
/*      */         else {
/*  906 */           to_stop = 1;
/*      */         }
/*  908 */         ii--;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  915 */     epi = cvm_cptv(xo, yo, xc[(npc - 1)], yc[(npc - 1)], closed);
/*  916 */     if (epi == -1) {
/*  917 */       esi = cvm_cptl(xo, yo, xc[(npc - 1)], yc[(npc - 1)]);
/*      */     }
/*      */ 
/*  924 */     int st = Math.max(opi, osi);
/*  925 */     int se = st + 1;
/*  926 */     if ((!closed) && (st == new_npo - 1)) {
/*  927 */       se = st;
/*  928 */       st = se - 1;
/*      */     }
/*      */ 
/*  931 */     double org_angle = cvm_angl(xo[st], yo[st], xo[se], yo[se]);
/*  932 */     double new_angle = cvm_angl(xc[0], yc[0], xc[1], yc[1]);
/*  933 */     direction = Math.abs(new_angle - org_angle);
/*      */ 
/*  938 */     if (!closed) {
/*  939 */       newLine = cvm_opmd(xo, yo, xc, yc, opi, osi, epi, esi, direction);
/*      */     }
/*      */     else {
/*  942 */       newLine = cvm_csmd(xo, yo, xc, yc, opi, osi, epi, esi, direction);
/*      */     }
/*      */ 
/*  948 */     if (newLine == null) {
/*  949 */       this.is = 0;
/*  950 */       this.ie = (npo - 1);
/*  951 */       this.np = npo;
/*      */ 
/*  953 */       newLine = new double[this.np][2];
/*  954 */       for (int ii = 0; ii < this.np; ii++) {
/*  955 */         newLine[ii][0] = xo[ii];
/*  956 */         newLine[ii][1] = yo[ii];
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  961 */     return newLine;
/*      */   }
/*      */ 
/*      */   private void cvm_index(int np, int is, int ie, boolean closed)
/*      */   {
/*  993 */     is -= GHOST_POINTS;
/*  994 */     ie += GHOST_POINTS;
/*      */ 
/* 1003 */     if (np <= 2 * GHOST_POINTS) {
/* 1004 */       is = 0;
/* 1005 */       ie = np - 1;
/*      */     }
/* 1008 */     else if (closed) {
/* 1009 */       if ((is >= 0) && (ie > np - 1)) {
/* 1010 */         ie -= np;
/*      */ 
/* 1012 */         if (ie >= is) {
/* 1013 */           is = 0;
/* 1014 */           ie = np - 1;
/*      */         }
/*      */       }
/* 1017 */       else if ((is < 0) && (ie <= np - 1)) {
/* 1018 */         is += np;
/* 1019 */         if (is <= ie) {
/* 1020 */           is = 0;
/* 1021 */           ie = np - 1;
/*      */         }
/*      */       }
/* 1024 */       else if ((is < 0) && (ie > np - 1)) {
/* 1025 */         is = 0;
/* 1026 */         ie = np - 1;
/*      */       }
/*      */     }
/*      */     else {
/* 1030 */       if (is < 0) is = 0;
/* 1031 */       if (ie > np - 1) ie = np - 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private double[] cvm_swap(double[] xin)
/*      */   {
/* 1056 */     int npi = xin.length;
/* 1057 */     double[] xout = new double[npi];
/*      */ 
/* 1059 */     for (int ii = 0; ii < npi; ii++) {
/* 1060 */       xout[ii] = xin[(npi - ii - 1)];
/*      */     }
/*      */ 
/* 1063 */     return xout;
/*      */   }
/*      */ 
/*      */   private double[][] cvm_opmd(double[] xo, double[] yo, double[] xc, double[] yc, int fin, int fis, int lin, int lis, double drct)
/*      */   {
/* 1104 */     double[][] newLine = null;
/*      */ 
/* 1106 */     int npo = xo.length;
/* 1107 */     int npc = xc.length;
/*      */ 
/* 1109 */     int FCP_d = 0;
/* 1110 */     int F_L = 0;
/* 1111 */     int FCP = Math.max(fin, fis);
/* 1112 */     int LCP = Math.max(lin, lis);
/*      */ 
/* 1114 */     if (fin >= 0) {
/* 1115 */       FCP_d = cvm_drct(xo, yo, xc, yc, FCP, 0);
/* 1116 */       F_L = cvm_drct(xo, yo, xc, yc, FCP, 2);
/*      */     }
/*      */ 
/* 1119 */     if (fis >= 0) {
/* 1120 */       if ((drct > 90.0D) && (drct < 270.0D)) FCP_d = -1;
/*      */ 
/* 1122 */       double dlast = cvm_angl(xc[0], yc[0], xc[(npc - 1)], yc[(npc - 1)]);
/* 1123 */       double dtmp = cvm_angl(xo[FCP], yo[FCP], xo[(FCP + 1)], yo[(FCP + 1)]);
/*      */ 
/* 1125 */       double tmp = Math.abs(dlast - dtmp);
/* 1126 */       if ((tmp > 90.0D) && (tmp < 270.0D)) F_L = -1;
/*      */ 
/*      */     }
/*      */ 
/* 1132 */     if ((LCP >= 0) && (
/* 1133 */       ((FCP < LCP) && (FCP_d == 0)) || 
/* 1134 */       ((FCP > LCP) && (FCP_d == -1)) || 
/* 1135 */       ((FCP == LCP) && (FCP_d == 0) && (F_L == 0)) || (
/* 1136 */       (FCP == LCP) && (FCP_d == -1) && (F_L == -1))))
/*      */     {
/* 1138 */       int isp = Math.min(FCP, LCP);
/* 1139 */       int iep = Math.max(FCP, LCP);
/*      */ 
/* 1141 */       if (((FCP < LCP) && (FCP_d == 0) && (fin >= 0)) || (
/* 1142 */         (FCP == LCP) && (FCP_d == 0) && (F_L == 0) && (fin >= 0))) {
/* 1143 */         isp--;
/*      */       }
/* 1145 */       if (((FCP > LCP) && (FCP_d == -1)) || (
/* 1146 */         (FCP == LCP) && (fis >= 0) && (FCP_d == -1) && (F_L == -1))) {
/* 1147 */         xc = cvm_swap(xc);
/* 1148 */         yc = cvm_swap(yc);
/* 1149 */         if (lin >= 0) isp--;
/*      */       }
/*      */ 
/* 1152 */       this.is = isp;
/* 1153 */       this.ie = (isp + 1 + npc);
/* 1154 */       if (this.is < 0) this.is = 0;
/* 1155 */       this.np = (isp + 1 + npc + npo - iep - 1);
/*      */ 
/* 1157 */       newLine = new double[this.np][3];
/*      */ 
/* 1163 */       for (int ii = 0; ii <= isp; ii++) {
/* 1164 */         newLine[ii][0] = xo[ii];
/* 1165 */         newLine[ii][1] = yo[ii];
/*      */       }
/*      */ 
/* 1168 */       int totalpts = isp + 1;
/*      */ 
/* 1170 */       for (ii = 0; ii < npc; ii++) {
/* 1171 */         newLine[(totalpts + ii)][0] = xc[ii];
/* 1172 */         newLine[(totalpts + ii)][1] = yc[ii];
/*      */       }
/*      */ 
/* 1175 */       totalpts += npc;
/*      */ 
/* 1177 */       for (ii = iep + 1; ii < npo; ii++) {
/* 1178 */         newLine[(totalpts + ii - iep - 1)][0] = xo[ii];
/* 1179 */         newLine[(totalpts + ii - iep - 1)][1] = yo[ii];
/*      */       }
/*      */ 
/*      */     }
/* 1186 */     else if (FCP_d == 0)
/*      */     {
/* 1188 */       int isp = FCP;
/* 1189 */       if (fin >= 0) isp--;
/* 1190 */       this.is = isp;
/* 1191 */       if (this.is < 0) this.is = 0;
/* 1192 */       this.np = (isp + 1 + npc);
/* 1193 */       this.ie = (this.np - 1);
/*      */ 
/* 1195 */       newLine = new double[this.np][2];
/*      */ 
/* 1197 */       for (int ii = 0; ii <= isp; ii++) {
/* 1198 */         newLine[ii][0] = xo[ii];
/* 1199 */         newLine[ii][1] = yo[ii];
/*      */       }
/*      */ 
/* 1202 */       for (ii = 0; ii < npc; ii++)
/*      */       {
/* 1204 */         newLine[(isp + ii + 1)][0] = xc[ii];
/* 1205 */         newLine[(isp + ii + 1)][1] = yc[ii];
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1210 */       xc = cvm_swap(xc);
/* 1211 */       yc = cvm_swap(yc);
/* 1212 */       int iep = FCP;
/* 1213 */       this.is = 0;
/* 1214 */       this.ie = npc;
/* 1215 */       this.np = (npc + npo - iep - 1);
/*      */ 
/* 1217 */       newLine = new double[this.np][2];
/*      */ 
/* 1219 */       for (int ii = 0; ii < npc; ii++) {
/* 1220 */         newLine[ii][0] = xc[ii];
/* 1221 */         newLine[ii][1] = yc[ii];
/*      */       }
/*      */ 
/* 1224 */       for (ii = iep + 1; ii < npo; ii++) {
/* 1225 */         newLine[(npc + ii - iep - 1)][0] = xo[ii];
/* 1226 */         newLine[(npc + ii - iep - 1)][1] = yo[ii];
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1231 */     return newLine;
/*      */   }
/*      */ 
/*      */   private int cvm_drct(double[] xo, double[] yo, double[] xc, double[] yc, int indx, int flag)
/*      */   {
/* 1271 */     int direction = 0;
/* 1272 */     int npo = xo.length;
/* 1273 */     int npc = xc.length;
/*      */ 
/* 1275 */     double angc = cvm_angl(xc[0], yc[0], xc[1], yc[1]);
/* 1276 */     if (flag == 1) {
/* 1277 */       angc = cvm_angl(xo[indx], yo[indx], xc[(npc - 1)], yc[(npc - 1)]);
/*      */     }
/*      */ 
/* 1280 */     if (flag == 2)
/* 1281 */       angc = cvm_angl(xc[0], yc[0], xc[(npc - 1)], yc[(npc - 1)]);
/*      */     double alpha;
/*      */     double alpha;
/* 1285 */     if (indx < npo - 1) {
/* 1286 */       alpha = cvm_angl(xo[indx], yo[indx], xo[(indx + 1)], yo[(indx + 1)]);
/*      */     }
/*      */     else
/* 1289 */       alpha = cvm_angl(xo[(indx - 1)], yo[(indx - 1)], xo[indx], yo[indx]);
/*      */     double beta;
/*      */     double beta;
/* 1292 */     if (indx > 0) {
/* 1293 */       beta = cvm_angl(xo[indx], yo[indx], xo[(indx - 1)], yo[(indx - 1)]);
/*      */     }
/*      */     else
/*      */     {
/* 1298 */       beta = cvm_angl(xo[(indx + 1)], yo[(indx + 1)], xo[indx], yo[indx]);
/*      */     }
/*      */ 
/* 1301 */     double tmp = Math.max(alpha, beta);
/* 1302 */     double ang_from = tmp - Math.abs(beta - alpha) / 2.0D;
/* 1303 */     double ang_to = ang_from + 180.0D;
/*      */ 
/* 1305 */     if (beta >= alpha) {
/* 1306 */       if (ang_to <= 360.0D) {
/* 1307 */         if ((angc > ang_from) && (angc < ang_to))
/* 1308 */           direction = -1;
/*      */       }
/*      */       else
/*      */       {
/* 1312 */         ang_to -= 360.0D;
/* 1313 */         if ((angc <= ang_to) || (angc >= ang_from)) {
/* 1314 */           direction = -1;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/* 1319 */     else if (ang_to <= 360.0D) {
/* 1320 */       if ((angc <= ang_from) || (angc >= ang_to))
/* 1321 */         direction = -1;
/*      */     }
/*      */     else
/*      */     {
/* 1325 */       ang_to -= 360.0D;
/* 1326 */       if ((angc > ang_to) && (angc < ang_from)) {
/* 1327 */         direction = -1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1332 */     return direction;
/*      */   }
/*      */ 
/*      */   private double[][] cvm_csmd(double[] xo, double[] yo, double[] xck, double[] yck, int fin, int fis, int lin, int lis, double drct)
/*      */   {
/* 1374 */     int npo = xo.length;
/* 1375 */     int npc = xck.length;
/*      */ 
/* 1377 */     double[] xc = new double[npc];
/* 1378 */     double[] yc = new double[npc];
/*      */ 
/* 1380 */     for (int ii = 0; ii < npc; ii++) {
/* 1381 */       xc[ii] = xck[ii];
/* 1382 */       yc[ii] = yck[ii];
/*      */     }
/*      */ 
/* 1385 */     double[][] newLine = new double[npc + npo + 2][2];
/*      */ 
/* 1387 */     double min_dist = 10000000000.0D;
/* 1388 */     int LCP_drct = 0;
/* 1389 */     int FCP_drct = 0;
/* 1390 */     int FCP_to_LCP = 0;
/*      */ 
/* 1392 */     if (fin == npo - 1) {
/* 1393 */       fin = 0;
/*      */     }
/*      */ 
/* 1396 */     int FCP = Math.max(fin, fis);
/* 1397 */     int LCP = Math.max(lin, lis);
/*      */ 
/* 1402 */     if (distance(xc[0], yc[0], xc[(npc - 1)], yc[(npc - 1)]) <= TIE_DIST) {
/* 1403 */       this.is = 0;
/* 1404 */       this.np = npc;
/* 1405 */       this.ie = (this.np - 1);
/* 1406 */       for (ii = 0; ii < npc; ii++) {
/* 1407 */         newLine[ii][0] = xc[ii];
/* 1408 */         newLine[ii][1] = yc[ii];
/*      */       }
/*      */ 
/* 1411 */       return newLine;
/*      */     }
/*      */ 
/* 1417 */     if (LCP < 0) {
/* 1418 */       for (ii = 0; ii < npo - 1; ii++) {
/* 1419 */         double ds = distance(xc[(npc - 1)], yc[(npc - 1)], xo[ii], yo[ii]);
/* 1420 */         if (ds <= min_dist) {
/* 1421 */           min_dist = ds;
/* 1422 */           LCP = ii;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1432 */     if (fis >= 0) {
/* 1433 */       if ((drct > 90.0D) && (drct < 270.0D)) FCP_drct = -1;
/*      */ 
/* 1435 */       double dlast = cvm_angl(xc[0], yc[0], xc[(npc - 1)], yc[(npc - 1)]);
/* 1436 */       double dtmp = cvm_angl(xo[FCP], yo[FCP], xo[(FCP + 1)], yo[(FCP + 1)]);
/*      */ 
/* 1438 */       double tmp = Math.abs(dlast - dtmp);
/* 1439 */       if ((tmp > 90.0D) && (tmp < 270.0D)) FCP_to_LCP = -1;
/*      */     }
/*      */ 
/* 1442 */     if (fin >= 0) {
/* 1443 */       FCP_drct = cvm_drct(xo, yo, xc, yc, FCP, 0);
/* 1444 */       FCP_to_LCP = cvm_drct(xo, yo, xc, yc, FCP, 2);
/*      */     }
/*      */ 
/* 1447 */     if ((lin == -1) && (lis == -1)) {
/* 1448 */       LCP_drct = cvm_drct(xo, yo, xc, yc, LCP, 1);
/*      */     }
/*      */ 
/* 1454 */     if ((lin == -1) && (lis == -1))
/*      */     {
/* 1456 */       if (FCP != LCP)
/*      */       {
/* 1458 */         if ((fin >= 0) || ((fis >= 0) && (FCP_drct >= 0)))
/*      */         {
/* 1460 */           if ((FCP_drct >= 0) && (LCP_drct >= 0)) {
/* 1461 */             LCP++;
/* 1462 */             if ((FCP_to_LCP >= 0) && (FCP == LCP)) LCP--;
/*      */           }
/*      */ 
/* 1465 */           if ((FCP_drct < 0) && (LCP_drct < 0)) {
/* 1466 */             if ((FCP == npo - 2) && (LCP == 0)) {
/* 1467 */               LCP = FCP - 1;
/*      */             }
/*      */             else {
/* 1470 */               LCP--;
/* 1471 */               if ((FCP_to_LCP < 0) && (FCP == LCP)) LCP++;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1476 */         if ((fis >= 0) && (FCP_drct < 0)) {
/* 1477 */           if ((LCP_drct < 0) && (FCP_to_LCP < 0)) {
/* 1478 */             FCP++;
/* 1479 */             LCP--;
/*      */           }
/*      */           else
/*      */           {
/* 1485 */             FCP++;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1490 */         if (LCP < 0) LCP = npo - 2;
/* 1491 */         if (FCP > npo - 2) FCP = npo - 2;
/*      */ 
/*      */       }
/* 1495 */       else if (fin >= 0) {
/* 1496 */         if (FCP_to_LCP >= 0) {
/* 1497 */           LCP++;
/*      */         } else {
/* 1499 */           LCP--;
/* 1500 */           if (lin >= 0) FCP++;
/*      */         }
/*      */ 
/* 1503 */         if (LCP < 0) LCP = npo - 2; 
/*      */       }
/*      */       else
/*      */       {
/* 1506 */         if ((FCP_drct >= 0) && (FCP_to_LCP >= 0)) LCP++;
/* 1507 */         if (FCP_drct < 0) {
/* 1508 */           if ((LCP_drct >= 0) && (FCP_to_LCP < 0)) {
/* 1509 */             FCP++;
/* 1510 */           } else if ((LCP_drct < 0) && (FCP_to_LCP < 0)) {
/* 1511 */             FCP++;
/* 1512 */             LCP--;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1523 */     if (FCP == LCP)
/*      */     {
/* 1525 */       if ((FCP_to_LCP < 0) && (FCP_drct < 0)) {
/* 1526 */         xc = cvm_swap(xck);
/* 1527 */         yc = cvm_swap(yck);
/*      */       }
/*      */ 
/* 1533 */       int isp = 0;
/* 1534 */       int iep = npc - 1;
/* 1535 */       this.is = isp;
/* 1536 */       this.np = npc;
/*      */ 
/* 1538 */       for (ii = 0; ii < npc; ii++) {
/* 1539 */         newLine[ii][0] = xc[ii];
/* 1540 */         newLine[ii][1] = yc[ii];
/*      */       }
/*      */ 
/* 1543 */       if ((fis >= 0) && (lin == -1) && (lis == -1))
/*      */       {
/* 1545 */         newLine[npc][0] = xo[FCP];
/* 1546 */         newLine[npc][1] = yo[FCP];
/*      */ 
/* 1548 */         newLine[(npc + 1)][0] = xc[0];
/* 1549 */         newLine[(npc + 1)][1] = yc[0];
/*      */ 
/* 1551 */         this.np += 2;
/* 1552 */         this.ie = (this.np - 1);
/*      */ 
/* 1554 */         double[][] finalLine = new double[this.np][2];
/*      */ 
/* 1556 */         for (int ij = 0; ij < this.np; ij++) {
/* 1557 */           finalLine[ij][0] = newLine[ij][0];
/* 1558 */           finalLine[ij][1] = newLine[ij][1];
/*      */         }
/*      */ 
/* 1561 */         return finalLine;
/*      */       }
/*      */ 
/* 1565 */       if (((fin >= 0) && (lis >= 0) && (FCP_drct < 0)) || 
/* 1566 */         ((fin >= 0) && (lin == -1) && (lis == -1)) || 
/* 1567 */         ((fis >= 0) && (lin >= 0) && (FCP_drct >= 0)) || (
/* 1568 */         (fis >= 0) && (lis >= 0) && (
/* 1569 */         ((FCP_to_LCP < 0) && (FCP_drct >= 0)) || (
/* 1570 */         (FCP_to_LCP >= 0) && (FCP_drct < 0)))))
/*      */       {
/* 1572 */         newLine[npc][0] = xc[0];
/* 1573 */         newLine[npc][1] = yc[0];
/*      */ 
/* 1575 */         this.np += 1;
/* 1576 */         this.ie = (this.np - 1);
/*      */ 
/* 1578 */         double[][] finalLine = new double[this.np][2];
/*      */ 
/* 1580 */         for (int ij = 0; ij < this.np; ij++) {
/* 1581 */           finalLine[ij][0] = newLine[ij][0];
/* 1582 */           finalLine[ij][1] = newLine[ij][1];
/*      */         }
/*      */ 
/* 1585 */         return finalLine;
/*      */       }
/*      */ 
/* 1591 */       isp = FCP;
/* 1592 */       iep = FCP + 1;
/*      */ 
/* 1597 */       this.is = isp;
/* 1598 */       this.np = (isp + 1);
/*      */ 
/* 1600 */       for (ii = 0; ii <= isp; ii++) {
/* 1601 */         newLine[ii][0] = xo[ii];
/* 1602 */         newLine[ii][1] = yo[ii];
/*      */       }
/*      */ 
/* 1608 */       if (((fin >= 0) && (lis >= 0) && (FCP_drct >= 0)) || (
/* 1609 */         (fis >= 0) && (lin >= 0) && (FCP_drct < 0)))
/*      */       {
/* 1611 */         this.np = (this.np + npc - 1);
/*      */ 
/* 1613 */         for (ii = 1; ii < npc; ii++) {
/* 1614 */           newLine[(ii + isp)][0] = xc[ii];
/* 1615 */           newLine[(ii + isp)][1] = yc[ii];
/*      */         }
/*      */       }
/*      */ 
/* 1619 */       if ((fis >= 0) && (lis >= 0) && (
/* 1620 */         ((FCP_to_LCP >= 0) && (FCP_drct >= 0)) || (
/* 1621 */         (FCP_to_LCP < 0) && (FCP_drct < 0))))
/*      */       {
/* 1623 */         this.np += npc;
/* 1624 */         for (ii = 0; ii < npc; ii++) {
/* 1625 */           newLine[(ii + isp + 1)][0] = xc[ii];
/* 1626 */           newLine[(ii + isp + 1)][1] = yc[ii];
/*      */         }
/*      */       }
/*      */ 
/* 1630 */       this.ie = this.np;
/*      */ 
/* 1635 */       for (ii = iep; ii < npo; ii++) {
/* 1636 */         newLine[(ii + this.np - iep)][0] = xo[ii];
/* 1637 */         newLine[(ii + this.np - iep)][1] = yo[ii];
/*      */       }
/*      */ 
/* 1640 */       this.np = (this.np + npo - iep);
/* 1641 */       if (this.ie > this.np - 1) this.ie = (this.np - 1);
/*      */ 
/* 1643 */       double[][] finalLine = new double[this.np][2];
/*      */ 
/* 1645 */       for (int ij = 0; ij < this.np; ij++) {
/* 1646 */         finalLine[ij][0] = newLine[ij][0];
/* 1647 */         finalLine[ij][1] = newLine[ij][1];
/*      */       }
/*      */ 
/* 1650 */       return finalLine;
/*      */     }
/*      */     int isp;
/*      */     int iep;
/* 1658 */     if (FCP_drct >= 0) {
/* 1659 */       int isp = FCP;
/* 1660 */       int iep = LCP;
/* 1661 */       if (lis >= 0) iep = LCP + 1; 
/*      */     }
/*      */     else
/*      */     {
/* 1664 */       isp = LCP;
/* 1665 */       iep = FCP;
/* 1666 */       if ((fis >= 0) && ((lin >= 0) || (lis >= 0))) iep = FCP + 1;
/*      */ 
/* 1668 */       xc = cvm_swap(xck);
/* 1669 */       yc = cvm_swap(yck);
/*      */     }
/*      */ 
/* 1676 */     if (((FCP_drct >= 0) && (FCP < LCP)) || (
/* 1677 */       (FCP_drct < 0) && (FCP > LCP)))
/*      */     {
/* 1682 */       this.is = isp;
/* 1683 */       this.np = (isp + 1);
/*      */ 
/* 1685 */       if ((lin == -1) && (lis == -1) && (isp < 0)) isp = 0;
/*      */ 
/* 1687 */       for (ii = 0; ii <= isp; ii++) {
/* 1688 */         newLine[ii][0] = xo[ii];
/* 1689 */         newLine[ii][1] = yo[ii];
/*      */       }
/*      */ 
/* 1696 */       if (distance(xc[0], yc[0], xo[isp], yo[isp]) <= TIE_DIST) {
/* 1697 */         for (ii = 0; ii < npc; ii++) {
/* 1698 */           newLine[(ii + this.np - 1)][0] = xc[ii];
/* 1699 */           newLine[(ii + this.np - 1)][1] = yc[ii];
/*      */         }
/*      */ 
/* 1702 */         this.np = (this.np + npc - 1);
/*      */       }
/*      */       else
/*      */       {
/* 1706 */         for (ii = 0; ii < npc; ii++) {
/* 1707 */           newLine[(ii + this.np)][0] = xc[ii];
/* 1708 */           newLine[(ii + this.np)][1] = yc[ii];
/*      */         }
/*      */ 
/* 1711 */         this.np += npc;
/*      */       }
/*      */ 
/* 1714 */       this.ie = this.np;
/*      */ 
/* 1719 */       if (distance(xc[(npc - 1)], yc[(npc - 1)], xo[iep], yo[iep]) <= TIE_DIST) {
/* 1720 */         iep++;
/*      */       }
/*      */ 
/* 1723 */       for (ii = iep; ii < npo; ii++) {
/* 1724 */         newLine[(ii + this.np - iep)][0] = xo[ii];
/* 1725 */         newLine[(ii + this.np - iep)][1] = yo[ii];
/*      */       }
/*      */ 
/* 1728 */       this.np = (this.np + npo - iep);
/*      */ 
/* 1730 */       double[][] finalLine = new double[this.np][2];
/* 1731 */       int ij = 0;
/* 1732 */       for (ij = 0; ij < this.np; ij++) {
/* 1733 */         finalLine[ij][0] = newLine[ij][0];
/* 1734 */         finalLine[ij][1] = newLine[ij][1];
/*      */       }
/*      */ 
/* 1737 */       return finalLine;
/*      */     }
/*      */ 
/* 1745 */     this.is = 0;
/* 1746 */     this.np = npc;
/*      */ 
/* 1748 */     for (ii = 0; ii < npc; ii++) {
/* 1749 */       newLine[ii][0] = xc[ii];
/* 1750 */       newLine[ii][1] = yc[ii];
/*      */     }
/*      */ 
/* 1753 */     this.ie = this.np;
/*      */ 
/* 1758 */     if ((fin >= 0) && (lin >= 0)) {
/* 1759 */       this.np += isp - iep - 1;
/* 1760 */       for (ii = iep + 1; ii < isp; ii++) {
/* 1761 */         newLine[(npc + ii - iep - 1)][0] = xo[ii];
/* 1762 */         newLine[(npc + ii - iep - 1)][1] = yo[ii];
/*      */       }
/*      */ 
/* 1765 */       if (distance(xc[0], yc[0], xo[isp], yo[isp]) > TIE_DIST) {
/* 1766 */         newLine[this.np][0] = xo[isp];
/* 1767 */         newLine[this.np][1] = yo[isp];
/*      */ 
/* 1769 */         this.np += 1;
/*      */       }
/*      */ 
/* 1772 */       newLine[this.np][0] = xc[0];
/* 1773 */       newLine[this.np][1] = yc[0];
/*      */ 
/* 1775 */       this.np += 1;
/*      */     }
/* 1778 */     else if (((FCP_drct >= 0) && (FCP < LCP) && (fin >= 0) && (lin == -1)) || (
/* 1779 */       (FCP_drct < 0) && (FCP > LCP) && (fin == -1) && (lin >= 0))) {
/* 1780 */       this.np = (this.np + isp - iep + 1);
/* 1781 */       for (ii = iep; ii <= isp; ii++) {
/* 1782 */         newLine[(npc + ii - iep)][0] = xo[ii];
/* 1783 */         newLine[(npc + ii - iep)][1] = yo[ii];
/*      */       }
/*      */ 
/*      */     }
/* 1787 */     else if (((FCP_drct >= 0) && (FCP < LCP) && (fin == -1) && (lin >= 0)) || (
/* 1788 */       (FCP_drct < 0) && (FCP > LCP) && (fin >= 0) && (lin == -1))) {
/* 1789 */       this.np = (this.np + isp - iep + 1);
/* 1790 */       for (ii = iep + 1; ii <= isp; ii++) {
/* 1791 */         newLine[(npc + ii - iep - 1)][0] = xo[ii];
/* 1792 */         newLine[(npc + ii - iep - 1)][1] = yo[ii];
/*      */       }
/*      */ 
/* 1796 */       newLine[(this.np - 1)][0] = xc[0];
/* 1797 */       newLine[(this.np - 1)][1] = yc[0];
/*      */     }
/*      */     else
/*      */     {
/* 1802 */       double ds = distance(xc[(npc - 1)], yc[(npc - 1)], xo[iep], yo[iep]);
/* 1803 */       if (ds <= TIE_DIST) iep++;
/* 1804 */       this.np = (this.np + isp - iep);
/* 1805 */       for (ii = iep; ii < isp; ii++) {
/* 1806 */         newLine[(npc + ii - iep)][0] = xo[ii];
/* 1807 */         newLine[(npc + ii - iep)][1] = yo[ii];
/*      */       }
/*      */ 
/* 1811 */       if (distance(xc[0], yc[0], xo[isp], yo[isp]) > TIE_DIST) {
/* 1812 */         newLine[this.np][0] = xo[isp];
/* 1813 */         newLine[this.np][1] = yo[isp];
/*      */ 
/* 1815 */         this.np += 1;
/*      */       }
/*      */ 
/* 1819 */       newLine[this.np][0] = xc[0];
/* 1820 */       newLine[this.np][1] = yc[0];
/*      */ 
/* 1822 */       this.np += 1;
/*      */     }
/*      */ 
/* 1827 */     double[][] finalLine = new double[this.np][2];
/* 1828 */     for (int ij = 0; ij < this.np; ij++) {
/* 1829 */       finalLine[ij][0] = newLine[ij][0];
/* 1830 */       finalLine[ij][1] = newLine[ij][1];
/*      */     }
/*      */ 
/* 1833 */     return finalLine;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenModifyLine
 * JD-Core Version:    0.6.2
 */