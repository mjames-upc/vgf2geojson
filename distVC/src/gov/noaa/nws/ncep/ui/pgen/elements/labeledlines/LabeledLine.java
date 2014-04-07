/*     */ package gov.noaa.nws.ncep.ui.pgen.elements.labeledlines;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class LabeledLine extends DECollection
/*     */ {
/*     */   public LabeledLine(String name)
/*     */   {
/*  46 */     super(name);
/*     */   }
/*     */ 
/*     */   public LabeledLine(String name, Line ln, Label lbl)
/*     */   {
/*  56 */     super(name);
/*  57 */     if (ln != null) add(ln);
/*  58 */     if (lbl != null) add(lbl);
/*     */   }
/*     */ 
/*     */   public void addLine(Line ln)
/*     */   {
/*  66 */     if (ln != null) add(ln);
/*     */   }
/*     */ 
/*     */   public void rmLine(Line ln)
/*     */   {
/*  74 */     Iterator it = getComponentIterator();
/*  75 */     while (it.hasNext())
/*  76 */       if (it.next() == ln) {
/*  77 */         it.remove();
/*  78 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void addLabel(Label lbl)
/*     */   {
/*  88 */     if (lbl != null) add(lbl);
/*     */   }
/*     */ 
/*     */   public void rmLabel(Label lbl)
/*     */   {
/*  96 */     Iterator it = getComponentIterator();
/*  97 */     while (it.hasNext())
/*  98 */       if (it.next() == lbl) {
/*  99 */         it.remove();
/* 100 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public List<Line> getLines()
/*     */   {
/* 110 */     ArrayList lines = new ArrayList();
/* 111 */     Iterator it = getComponentIterator();
/* 112 */     while (it.hasNext()) {
/* 113 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 114 */       if ((adc instanceof Line)) {
/* 115 */         lines.add((Line)adc);
/*     */       }
/*     */     }
/* 118 */     return lines;
/*     */   }
/*     */ 
/*     */   public List<Label> getLabels()
/*     */   {
/* 126 */     ArrayList labels = new ArrayList();
/* 127 */     Iterator it = getComponentIterator();
/* 128 */     while (it.hasNext()) {
/* 129 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 130 */       if ((adc instanceof Label)) {
/* 131 */         labels.add((Label)adc);
/*     */       }
/*     */     }
/* 134 */     return labels;
/*     */   }
/*     */ 
/*     */   public LabeledLine copy()
/*     */   {
/* 141 */     LabeledLine ll = new LabeledLine(getName());
/* 142 */     ll.setParent(this.parent);
/* 143 */     ll.setPgenCategory(this.pgenCategory);
/* 144 */     ll.setPgenType(this.pgenType);
/*     */ 
/* 146 */     Iterator it = getComponentIterator();
/* 147 */     while (it.hasNext()) {
/* 148 */       ll.add(((AbstractDrawableComponent)it.next()).copy());
/*     */     }
/*     */ 
/* 151 */     return ll;
/*     */   }
/*     */ 
/*     */   public DrawableElement getPrimaryDE()
/*     */   {
/* 157 */     for (AbstractDrawableComponent adc : this.compList) {
/* 158 */       if ((adc instanceof Line)) return (Line)adc;
/*     */     }
/*     */ 
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   public Label getLabelAt(Coordinate loc) {
/* 165 */     Iterator it = getComponentIterator();
/* 166 */     while (it.hasNext()) {
/* 167 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 168 */       if ((adc instanceof Label)) {
/* 169 */         Label lbl = (Label)adc;
/* 170 */         if ((Math.abs(lbl.getSpe().getLocation().x - loc.x) < 0.0001D) && 
/* 171 */           (Math.abs(lbl.getSpe().getLocation().y - loc.y) < 0.0001D))
/*     */         {
/* 174 */           return lbl;
/*     */         }
/*     */       }
/*     */     }
/* 178 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine
 * JD-Core Version:    0.6.2
 */