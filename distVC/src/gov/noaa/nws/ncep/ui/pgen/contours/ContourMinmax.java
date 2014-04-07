/*     */ package gov.noaa.nws.ncep.ui.pgen.contours;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ComboSymbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import java.awt.Color;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class ContourMinmax extends DECollection
/*     */ {
/*     */   public ContourMinmax()
/*     */   {
/*  46 */     super("ContourMinmax");
/*     */   }
/*     */ 
/*     */   public ContourMinmax(Coordinate loc, String pgenCat, String pgenType, String[] text)
/*     */   {
/*  54 */     super("ContourMinmax");
/*     */ 
/*  56 */     DrawableElement cmm = null;
/*     */ 
/*  58 */     if (pgenCat.equals("Combo")) {
/*  59 */       cmm = new ComboSymbol(null, new Color[] { Color.green }, 
/*  60 */         2.0F, 1.0D, Boolean.valueOf(true), loc, pgenCat, pgenType);
/*     */     }
/*     */     else {
/*  63 */       cmm = new Symbol(null, new Color[] { Color.green }, 
/*  64 */         2.0F, 2.0D, Boolean.valueOf(true), loc, pgenCat, pgenType);
/*     */     }
/*     */ 
/*  67 */     cmm.setParent(this);
/*     */ 
/*  69 */     add(cmm);
/*     */ 
/*  72 */     Text lbl = new Text(null, "Courier", 14.0F, IText.TextJustification.CENTER, 
/*  73 */       null, 0.0D, IText.TextRotation.SCREEN_RELATIVE, text, 
/*  74 */       IText.FontStyle.REGULAR, Color.GREEN, 0, 0, true, IText.DisplayType.NORMAL, 
/*  75 */       "Text", "General Text");
/*  76 */     Coordinate p = new Coordinate(loc.x, loc.y - 2.5D);
/*  77 */     lbl.setLocation(p);
/*  78 */     lbl.setAuto(Boolean.valueOf(true));
/*  79 */     lbl.setParent(this);
/*     */ 
/*  81 */     add(lbl);
/*     */   }
/*     */ 
/*     */   public ContourMinmax copy()
/*     */   {
/*  92 */     ContourMinmax cmm = new ContourMinmax();
/*     */ 
/*  94 */     Iterator iterator = createDEIterator();
/*     */ 
/*  96 */     while (iterator.hasNext()) {
/*  97 */       DrawableElement de = (DrawableElement)((DrawableElement)iterator.next()).copy();
/*  98 */       de.setParent(cmm);
/*  99 */       cmm.add(de);
/*     */     }
/*     */ 
/* 102 */     return cmm;
/*     */   }
/*     */ 
/*     */   public Text getLabel()
/*     */   {
/* 110 */     Iterator iterator = createDEIterator();
/*     */ 
/* 112 */     Text label = null;
/* 113 */     while (iterator.hasNext()) {
/* 114 */       DrawableElement de = (DrawableElement)iterator.next();
/* 115 */       if ((de instanceof Text)) {
/* 116 */         label = (Text)de;
/* 117 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 121 */     return label;
/*     */   }
/*     */ 
/*     */   public String[] getLabelString()
/*     */   {
/* 128 */     Text label = getLabel();
/* 129 */     if (label != null) {
/* 130 */       return label.getText();
/*     */     }
/*     */ 
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   public void updateLabelString(String[] text)
/*     */   {
/* 143 */     Text label = getLabel();
/* 144 */     if (label != null)
/* 145 */       label.setText(text);
/*     */   }
/*     */ 
/*     */   public DrawableElement getSymbol()
/*     */   {
/* 156 */     Iterator iterator = createDEIterator();
/*     */ 
/* 158 */     DrawableElement csym = null;
/* 159 */     while (iterator.hasNext()) {
/* 160 */       DrawableElement de = (DrawableElement)iterator.next();
/* 161 */       if (((de instanceof Symbol)) || ((de instanceof ComboSymbol))) {
/* 162 */         csym = de;
/* 163 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 167 */     return csym;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.contours.ContourMinmax
 * JD-Core Version:    0.6.2
 */