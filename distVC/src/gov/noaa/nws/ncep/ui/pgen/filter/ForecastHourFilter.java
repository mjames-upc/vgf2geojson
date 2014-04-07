/*     */ package gov.noaa.nws.ncep.ui.pgen.filter;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ 
/*     */ public class ForecastHourFilter
/*     */   implements ElementFilter
/*     */ {
/*     */   private String hours;
/*     */ 
/*     */   public ForecastHourFilter(String forecastHours)
/*     */   {
/*  37 */     this.hours = forecastHours;
/*     */   }
/*     */ 
/*     */   public boolean accept(AbstractDrawableComponent adc)
/*     */   {
/*  43 */     String elHours = adc.getForecastHours();
/*     */ 
/*  46 */     boolean singleHr = false;
/*  47 */     int elHH = 0;
/*  48 */     int elMM = 0;
/*  49 */     if ((elHours != null) && (!elHours.isEmpty()) && (!elHours.contains("-"))) {
/*     */       try {
/*  51 */         if (elHours.contains(":"))
/*     */         {
/*  53 */           elHH = Integer.valueOf(elHours.substring(0, elHours.indexOf(":"))).intValue();
/*  54 */           elMM = Integer.valueOf(elHours.substring(elHours.indexOf(":") + 1)).intValue();
/*     */         }
/*     */         else
/*     */         {
/*  58 */           elHH = Integer.valueOf(elHours).intValue();
/*  59 */           elMM = 0;
/*     */         }
/*     */       }
/*     */       catch (NumberFormatException e) {
/*  63 */         return false;
/*     */       }
/*     */ 
/*  66 */       singleHr = true;
/*     */     }
/*     */ 
/*  69 */     if ((elHours == null) || (elHours.isEmpty()))
/*     */     {
/*  71 */       return true;
/*     */     }
/*  73 */     if (elHours.equalsIgnoreCase(this.hours))
/*     */     {
/*  75 */       return true;
/*     */     }
/*  77 */     if ((singleHr) && (elMM == 0) && (this.hours.equalsIgnoreCase(Integer.toString(elHH))))
/*     */     {
/*  79 */       return true;
/*     */     }
/*  81 */     if (this.hours.endsWith("+"))
/*     */     {
/*  83 */       int startHour = 0;
/*     */       try {
/*  85 */         startHour = Integer.valueOf(this.hours.substring(0, this.hours.length() - 1)).intValue();
/*     */       }
/*     */       catch (NumberFormatException e) {
/*  88 */         return false;
/*     */       }
/*     */ 
/*  91 */       int endHour = startHour + 3;
/*     */ 
/*  93 */       if ((singleHr) && (elHH < endHour) && (elHH >= startHour)) {
/*  94 */         return true;
/*     */       }
/*     */     }
/*  97 */     else if (((this.hours.equalsIgnoreCase("AIRM")) || (this.hours.equalsIgnoreCase("OTLK"))) && 
/*  98 */       (elHours.contains("-"))) {
/*  99 */       String endTm = elHours.substring(elHours.indexOf("-") + 1);
/*     */       try
/*     */       {
/* 102 */         int endHr = 0;
/* 103 */         if (endTm.contains(":")) {
/* 104 */           endHr = Integer.valueOf(endTm.substring(0, endTm.indexOf(":"))).intValue();
/*     */         }
/*     */         else {
/* 107 */           endHr = Integer.valueOf(endTm).intValue();
/*     */         }
/*     */ 
/* 110 */         if ((this.hours.equalsIgnoreCase("OTLK")) && (endHr > 6)) {
/* 111 */           return true;
/*     */         }
/* 113 */         if ((this.hours.equalsIgnoreCase("AIRM")) && (endHr <= 6)) {
/* 114 */           return true;
/*     */         }
/*     */       }
/*     */       catch (NumberFormatException e)
/*     */       {
/* 119 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 124 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.filter.ForecastHourFilter
 * JD-Core Version:    0.6.2
 */