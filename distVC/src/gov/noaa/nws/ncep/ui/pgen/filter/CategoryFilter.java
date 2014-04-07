/*     */ package gov.noaa.nws.ncep.ui.pgen.filter;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetBarb;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetHash;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Spenes;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.Tcm;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.TCAElement;
/*     */ 
/*     */ public class CategoryFilter
/*     */   implements ElementFilter
/*     */ {
/*     */   String category;
/*     */ 
/*     */   public CategoryFilter(String category)
/*     */   {
/*  37 */     this.category = category;
/*     */   }
/*     */ 
/*     */   public boolean accept(AbstractDrawableComponent adc)
/*     */   {
/*  44 */     if (this.category.equalsIgnoreCase("MET")) {
/*  45 */       if ((adc instanceof DECollection)) {
/*  46 */         if ((((DECollection)adc).getCollectionName().equalsIgnoreCase("Watch")) || 
/*  47 */           (((DECollection)adc).getCollectionName().equalsIgnoreCase("Outlook")) || 
/*  48 */           ((adc instanceof LabeledLine)) || 
/*  49 */           ((adc instanceof Jet)) || 
/*  50 */           ((adc instanceof Contours)))
/*  51 */           return true;
/*     */       }
/*     */       else {
/*  54 */         if (((adc instanceof Jet.JetHash)) || 
/*  55 */           ((adc instanceof Jet.JetBarb)) || 
/*  56 */           ((adc instanceof Jet.JetText)))
/*     */         {
/*  58 */           return false;
/*     */         }
/*  60 */         if (((adc instanceof Text)) && 
/*  61 */           (adc.getParent().getParent() != null) && 
/*  62 */           ((adc.getParent().getParent() instanceof Outlook))) {
/*  63 */           return true;
/*     */         }
/*  65 */         if ((adc.getParent().getPgenCategory() != null) && 
/*  66 */           (adc.getParent().getPgenCategory().equalsIgnoreCase("MET")))
/*     */         {
/*  68 */           return true;
/*     */         }
/*     */ 
/*  71 */         if ((adc.getParent().getParent() != null) && (adc.getParent().getParent().getPgenCategory() != null) && 
/*  72 */           (adc.getParent().getParent().getPgenCategory().equalsIgnoreCase("MET")))
/*     */         {
/*  74 */           return true;
/*     */         }
/*  76 */         if (((adc instanceof Gfa)) || 
/*  77 */           ((adc instanceof TCAElement)) || 
/*  78 */           ((adc instanceof Tcm)) || 
/*  79 */           ((adc instanceof Spenes)))
/*  80 */           return true;
/*     */       }
/*     */     }
/*     */     else {
/*  84 */       if (this.category.equalsIgnoreCase("SIGMET")) {
/*  85 */         return true;
/*     */       }
/*  87 */       if ((adc.getParent() != null) && (
/*  88 */         ((adc.getParent() instanceof LabeledLine)) || 
/*  89 */         ((adc.getParent().getParent() instanceof LabeledLine))))
/*     */       {
/*  91 */         if (this.category.equalsIgnoreCase("ANY")) {
/*  92 */           return true;
/*     */         }
/*     */ 
/*  95 */         return false;
/*     */       }
/*     */ 
/*  98 */       if ((this.category.equalsIgnoreCase("any")) || 
/*  99 */         (adc.getPgenCategory().equalsIgnoreCase(this.category))) {
/* 100 */         if (this.category.equalsIgnoreCase("Lines")) {
/* 101 */           if (((adc.getParent() instanceof Jet)) || 
/* 102 */             ((adc.getParent().getParent() instanceof Jet)) || 
/* 103 */             ((adc.getParent() instanceof Outlook)) || 
/* 104 */             ((adc.getParent().getParent() instanceof Outlook)) || 
/* 105 */             ((adc.getParent().getParent() instanceof Contours)))
/*     */           {
/* 107 */             return false;
/*     */           }
/*     */ 
/*     */         }
/* 111 */         else if ((this.category.equalsIgnoreCase("Symbol")) || (this.category.equalsIgnoreCase("Arc"))) {
/* 112 */           if ((adc.getParent().getParent() instanceof Contours)) {
/* 113 */             return false;
/*     */           }
/*     */         }
/* 116 */         else if ((this.category.equalsIgnoreCase("Text")) && 
/* 117 */           (adc.getParent().getParent() != null) && 
/* 118 */           ((adc.getParent().getParent() instanceof Contours))) {
/* 119 */           if ((adc.getParent().getParent() instanceof Outlook)) {
/* 120 */             return true;
/*     */           }
/*     */ 
/* 123 */           return false;
/*     */         }
/*     */ 
/* 128 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   public String getCategory() {
/* 136 */     return this.category;
/*     */   }
/*     */ 
/*     */   public void setCategory(String category) {
/* 140 */     this.category = category;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.filter.CategoryFilter
 * JD-Core Version:    0.6.2
 */