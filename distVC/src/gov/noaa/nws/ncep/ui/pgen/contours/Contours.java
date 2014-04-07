/*     */ package gov.noaa.nws.ncep.ui.pgen.contours;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE})
/*     */ public class Contours extends DECollection
/*     */   implements IContours
/*     */ {
/*     */   private String parm;
/*     */   private String level;
/*     */   private String forecastHour;
/*     */   private String cint;
/*     */   private Calendar time1;
/*     */   private Calendar time2;
/*     */ 
/*     */   public Contours()
/*     */   {
/*  57 */     super("Contours");
/*  58 */     setPgenCategory("MET");
/*  59 */     setPgenType("Contours");
/*     */ 
/*  61 */     setParm("");
/*  62 */     setLevel("");
/*  63 */     setForecastHour("");
/*  64 */     setTime1(Calendar.getInstance());
/*  65 */     setTime2(Calendar.getInstance());
/*  66 */     setCint("");
/*     */   }
/*     */ 
/*     */   public Contours(String name)
/*     */   {
/*  71 */     super(name);
/*     */   }
/*     */ 
/*     */   public Contours(IAttribute attr, ArrayList<Coordinate> points)
/*     */   {
/*  76 */     super("Contours");
/*  77 */     setPgenCategory("MET");
/*  78 */     setPgenType("Contours");
/*     */   }
/*     */ 
/*     */   public void setParm(String parm)
/*     */   {
/*  86 */     this.parm = parm;
/*     */   }
/*     */ 
/*     */   public String getParm()
/*     */   {
/*  93 */     return this.parm;
/*     */   }
/*     */ 
/*     */   public String getLevel()
/*     */   {
/* 100 */     return this.level;
/*     */   }
/*     */ 
/*     */   public void setLevel(String level)
/*     */   {
/* 107 */     this.level = level;
/*     */   }
/*     */ 
/*     */   public String getForecastHour()
/*     */   {
/* 114 */     return this.forecastHour;
/*     */   }
/*     */ 
/*     */   public void setForecastHour(String forecastHour)
/*     */   {
/* 121 */     this.forecastHour = forecastHour;
/*     */   }
/*     */ 
/*     */   public String getCint()
/*     */   {
/* 128 */     return this.cint;
/*     */   }
/*     */ 
/*     */   public void setCint(String cint)
/*     */   {
/* 135 */     this.cint = cint;
/*     */   }
/*     */ 
/*     */   public Calendar getTime1()
/*     */   {
/* 142 */     return this.time1;
/*     */   }
/*     */ 
/*     */   public void setTime1(Calendar time)
/*     */   {
/* 149 */     this.time1 = time;
/*     */   }
/*     */ 
/*     */   public Calendar getTime2()
/*     */   {
/* 156 */     return this.time2;
/*     */   }
/*     */ 
/*     */   public void setTime2(Calendar time)
/*     */   {
/* 163 */     this.time2 = time;
/*     */   }
/*     */ 
/*     */   public Contours copy()
/*     */   {
/* 172 */     Contours newContours = new Contours();
/*     */ 
/* 174 */     Iterator iterator = getComponentIterator();
/*     */ 
/* 176 */     while (iterator.hasNext()) {
/* 177 */       AbstractDrawableComponent adc = ((AbstractDrawableComponent)iterator.next()).copy();
/* 178 */       adc.setParent(newContours);
/* 179 */       newContours.add(adc);
/*     */     }
/*     */ 
/* 182 */     newContours.update(this);
/*     */ 
/* 184 */     return newContours;
/*     */   }
/*     */ 
/*     */   public void update(IContours attr)
/*     */   {
/* 192 */     setParm(attr.getParm());
/* 193 */     setLevel(attr.getLevel());
/* 194 */     setForecastHour(attr.getForecastHour());
/* 195 */     setTime1(attr.getTime1());
/* 196 */     setTime2(attr.getTime2());
/* 197 */     setCint(attr.getCint());
/*     */   }
/*     */ 
/*     */   public Contours split(ContourLine cline, int start, int end)
/*     */   {
/* 208 */     Contours newContours = new Contours();
/*     */ 
/* 210 */     Iterator iterator = getComponentIterator();
/*     */ 
/* 212 */     while (iterator.hasNext())
/*     */     {
/* 214 */       AbstractDrawableComponent oldAdc = (AbstractDrawableComponent)iterator.next();
/* 215 */       AbstractDrawableComponent newAdc = oldAdc.copy();
/*     */ 
/* 217 */       if (oldAdc.equals(cline)) {
/* 218 */         ArrayList newLines = ((ContourLine)newAdc).split(start, end);
/* 219 */         for (ContourLine cln : newLines) {
/* 220 */           cln.setParent(newContours);
/* 221 */           newContours.add(cln);
/*     */         }
/*     */       }
/*     */       else {
/* 225 */         newAdc.setParent(newContours);
/* 226 */         newContours.add(newAdc);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 231 */     newContours.update(this);
/*     */ 
/* 233 */     return newContours;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 241 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*     */ 
/* 243 */     result.append("Category:\t" + this.pgenCategory + "\n");
/* 244 */     result.append("Type:\t" + this.pgenType + "\n");
/* 245 */     result.append("Parm:\t" + this.parm + "\n");
/* 246 */     result.append("Level:\t" + this.level + "\n");
/* 247 */     result.append("Cint:\t" + this.cint + "\n");
/* 248 */     result.append("Time1:\t" + this.time1 + "\n");
/* 249 */     result.append("Time2:\t" + this.time2 + "\n");
/*     */ 
/* 251 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public ArrayList<ContourLine> getContourLines()
/*     */   {
/* 259 */     ArrayList lines = new ArrayList();
/*     */ 
/* 261 */     Iterator iterator = getComponentIterator();
/*     */ 
/* 263 */     while (iterator.hasNext()) {
/* 264 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)iterator.next();
/* 265 */       if ((adc instanceof ContourLine)) {
/* 266 */         lines.add((ContourLine)adc);
/*     */       }
/*     */     }
/*     */ 
/* 270 */     return lines;
/*     */   }
/*     */ 
/*     */   public ArrayList<ContourMinmax> getContourMinmaxs()
/*     */   {
/* 279 */     ArrayList cmms = new ArrayList();
/*     */ 
/* 281 */     Iterator iterator = getComponentIterator();
/*     */ 
/* 283 */     while (iterator.hasNext()) {
/* 284 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)iterator.next();
/* 285 */       if ((adc instanceof ContourMinmax)) {
/* 286 */         cmms.add((ContourMinmax)adc);
/*     */       }
/*     */     }
/*     */ 
/* 290 */     return cmms;
/*     */   }
/*     */ 
/*     */   public ArrayList<ContourCircle> getContourCircles()
/*     */   {
/* 299 */     ArrayList cmms = new ArrayList();
/*     */ 
/* 301 */     Iterator iterator = getComponentIterator();
/*     */ 
/* 303 */     while (iterator.hasNext()) {
/* 304 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)iterator.next();
/* 305 */       if ((adc instanceof ContourCircle)) {
/* 306 */         cmms.add((ContourCircle)adc);
/*     */       }
/*     */     }
/*     */ 
/* 310 */     return cmms;
/*     */   }
/*     */ 
/*     */   public Contours createContours(int nContours, int[] nContourPts, double[] latlons, float[] contourValue, Color clr)
/*     */   {
/* 323 */     Contours gridContours = copy();
/* 324 */     gridContours.clear();
/*     */ 
/* 326 */     int tPts = 0;
/* 327 */     ArrayList linePts = new ArrayList();
/* 328 */     for (int mm = 0; mm < nContours; mm++)
/*     */     {
/* 330 */       for (int nn = 0; nn < nContourPts[mm]; nn++) {
/* 331 */         Coordinate point = new Coordinate();
/* 332 */         point.x = latlons[(tPts + nn * 2)];
/* 333 */         point.y = latlons[(tPts + nn * 2 + 1)];
/*     */ 
/* 335 */         linePts.add(point);
/*     */       }
/*     */ 
/* 338 */       tPts += nContourPts[mm] * 2;
/*     */ 
/* 340 */       int nLabels = 2;
/* 341 */       ContourLine cline = new ContourLine(linePts, false, 
/* 342 */         new String[] { contourValue[mm] }, nLabels);
/*     */ 
/* 344 */       cline.setParent(gridContours);
/* 345 */       if (clr != null) cline.getLine().setColors(new Color[] { clr });
/* 346 */       cline.getLine().setLineWidth(2.0F);
/* 347 */       gridContours.add(cline);
/*     */ 
/* 349 */       linePts.clear();
/*     */     }
/*     */ 
/* 352 */     return gridContours;
/*     */   }
/*     */ 
/*     */   public Contours createContours(int nContours, int[] nContourPts, double[] latlons, String[] contourValue, Color clr)
/*     */   {
/* 365 */     Contours gridContours = copy();
/* 366 */     gridContours.clear();
/*     */ 
/* 368 */     int tPts = 0;
/* 369 */     ArrayList linePts = new ArrayList();
/* 370 */     for (int mm = 0; mm < nContours; mm++)
/*     */     {
/* 372 */       for (int nn = 0; nn < nContourPts[mm]; nn++) {
/* 373 */         Coordinate point = new Coordinate();
/* 374 */         point.x = latlons[(tPts + nn * 2)];
/* 375 */         point.y = latlons[(tPts + nn * 2 + 1)];
/*     */ 
/* 377 */         linePts.add(point);
/*     */       }
/*     */ 
/* 380 */       tPts += nContourPts[mm] * 2;
/*     */ 
/* 382 */       int nLabels = 2;
/* 383 */       ContourLine cline = new ContourLine(linePts, false, 
/* 384 */         new String[] { contourValue[mm] }, nLabels);
/*     */ 
/* 386 */       cline.setParent(gridContours);
/* 387 */       if (clr != null) cline.getLine().setColors(new Color[] { clr });
/* 388 */       cline.getLine().setLineWidth(2.0F);
/* 389 */       gridContours.add(cline);
/*     */ 
/* 391 */       linePts.clear();
/*     */     }
/*     */ 
/* 394 */     return gridContours;
/*     */   }
/*     */ 
/*     */   public Contours split(ContourLine cline, Coordinate start, Coordinate end)
/*     */   {
/* 403 */     Contours newContours = new Contours();
/*     */ 
/* 405 */     Iterator iterator = getComponentIterator();
/*     */ 
/* 407 */     while (iterator.hasNext())
/*     */     {
/* 409 */       AbstractDrawableComponent oldAdc = (AbstractDrawableComponent)iterator.next();
/* 410 */       AbstractDrawableComponent newAdc = oldAdc.copy();
/*     */ 
/* 412 */       if (oldAdc.equals(cline)) {
/* 413 */         ArrayList newLines = ((ContourLine)newAdc).split(start, end);
/* 414 */         for (ContourLine cln : newLines) {
/* 415 */           cln.setParent(newContours);
/* 416 */           newContours.add(cln);
/*     */         }
/*     */       }
/*     */       else {
/* 420 */         newAdc.setParent(newContours);
/* 421 */         newContours.add(newAdc);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 426 */     newContours.update(this);
/*     */ 
/* 428 */     return newContours;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.contours.Contours
 * JD-Core Version:    0.6.2
 */