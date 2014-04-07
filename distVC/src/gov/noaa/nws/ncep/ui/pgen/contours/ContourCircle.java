/*     */ package gov.noaa.nws.ncep.ui.pgen.contours;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Arc;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import java.awt.Color;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class ContourCircle extends DECollection
/*     */ {
/*     */   public ContourCircle()
/*     */   {
/*  46 */     super("ContourCircle");
/*     */   }
/*     */ 
/*     */   public ContourCircle(Coordinate center, Coordinate circum, String[] text, boolean hide)
/*     */   {
/*  54 */     super("ContourCircle");
/*     */ 
/*  56 */     DrawableElement cArc = null;
/*     */ 
/*  58 */     cArc = new Arc(null, Color.red, 3.0F, 1.0D, false, false, 2, FillPatternList.FillPattern.SOLID, 
/*  59 */       "Circle", center, circum, "Arc", 1.0D, 0.0D, 360.0D);
/*     */ 
/*  61 */     cArc.setParent(this);
/*     */ 
/*  63 */     add(cArc);
/*     */ 
/*  66 */     Text lbl = new Text(null, "Courier", 14.0F, IText.TextJustification.CENTER, 
/*  67 */       null, 0.0D, IText.TextRotation.SCREEN_RELATIVE, text, 
/*  68 */       IText.FontStyle.REGULAR, Color.GREEN, 0, 0, true, IText.DisplayType.NORMAL, 
/*  69 */       "Text", "General Text");
/*     */ 
/*  71 */     lbl.setLocation(circum);
/*     */ 
/*  73 */     lbl.setAuto(Boolean.valueOf(true));
/*     */ 
/*  75 */     lbl.setParent(this);
/*     */ 
/*  77 */     lbl.setHide(Boolean.valueOf(hide));
/*     */ 
/*  79 */     add(lbl);
/*     */   }
/*     */ 
/*     */   public ContourCircle copy()
/*     */   {
/*  90 */     ContourCircle cmm = new ContourCircle();
/*     */ 
/*  92 */     Iterator iterator = createDEIterator();
/*     */ 
/*  94 */     while (iterator.hasNext()) {
/*  95 */       DrawableElement de = (DrawableElement)((DrawableElement)iterator.next()).copy();
/*  96 */       de.setParent(cmm);
/*  97 */       cmm.add(de);
/*     */     }
/*     */ 
/* 100 */     return cmm;
/*     */   }
/*     */ 
/*     */   public Text getLabel()
/*     */   {
/* 108 */     Iterator iterator = createDEIterator();
/*     */ 
/* 110 */     Text label = null;
/* 111 */     while (iterator.hasNext()) {
/* 112 */       DrawableElement de = (DrawableElement)iterator.next();
/* 113 */       if ((de instanceof Text)) {
/* 114 */         label = (Text)de;
/* 115 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 119 */     return label;
/*     */   }
/*     */ 
/*     */   public String[] getLabelString()
/*     */   {
/* 126 */     Text label = getLabel();
/* 127 */     if (label != null) {
/* 128 */       return label.getText();
/*     */     }
/*     */ 
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */   public void updateLabelString(String[] text)
/*     */   {
/* 141 */     Text label = getLabel();
/* 142 */     if (label != null)
/* 143 */       label.setText(text);
/*     */   }
/*     */ 
/*     */   public DrawableElement getCircle()
/*     */   {
/* 154 */     Iterator iterator = createDEIterator();
/*     */ 
/* 156 */     DrawableElement csym = null;
/* 157 */     while (iterator.hasNext()) {
/* 158 */       DrawableElement de = (DrawableElement)iterator.next();
/* 159 */       if ((de instanceof Arc)) {
/* 160 */         csym = de;
/* 161 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 165 */     return csym;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.contours.ContourCircle
 * JD-Core Version:    0.6.2
 */