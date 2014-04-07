/*     */ package gov.noaa.nws.ncep.ui.pgen.sigmet;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.INTERPOLATE})
/*     */ public class Ccfp extends LabeledLine
/*     */ {
/*  43 */   private Sigmet sigmet = new Sigmet();
/*     */ 
/*  45 */   private String tool = "";
/*     */   private Line areaLine;
/*     */ 
/*     */   public Ccfp(String name)
/*     */   {
/*  50 */     super(name);
/*     */   }
/*     */ 
/*     */   public LabeledLine copy()
/*     */   {
/*  56 */     Ccfp ll = new Ccfp(getName());
/*  57 */     ll.setParent(this.parent);
/*  58 */     ll.setPgenCategory(this.pgenCategory);
/*  59 */     ll.setPgenType(this.pgenType);
/*     */ 
/*  61 */     ll.setSigmet((Sigmet)getSigmet().copy());
/*     */ 
/*  63 */     Iterator it = getComponentIterator();
/*  64 */     while (it.hasNext()) {
/*  65 */       ll.add(((AbstractDrawableComponent)it.next()).copy());
/*     */     }
/*     */ 
/*  68 */     return ll;
/*     */   }
/*     */ 
/*     */   public void setSigmet(Sigmet s) {
/*  72 */     this.sigmet = s;
/*     */   }
/*     */ 
/*     */   public Sigmet getSigmet()
/*     */   {
/*  77 */     this.sigmet.setPoints(getAreaLine().getPoints());
/*  78 */     this.sigmet.setPgenCategory(getPgenCategory());
/*  79 */     this.sigmet.setPgenType(getPgenType());
/*     */ 
/*  81 */     return this.sigmet;
/*     */   }
/*     */ 
/*     */   public void setAreaLine(Line l) {
/*  85 */     this.areaLine = l;
/*     */   }
/*     */ 
/*     */   public Line getAreaLine()
/*     */   {
/*  90 */     return (Line)getLines().get(0);
/*     */   }
/*     */ 
/*     */   public void setAttributes(IAttribute attrDlg)
/*     */   {
/*  95 */     if ((attrDlg instanceof ICcfp))
/*  96 */       ((ICcfp)attrDlg).copyEditableAttrToAbstractSigmet2(this.sigmet, this);
/*     */   }
/*     */ 
/*     */   public void setCollectionName(String name)
/*     */   {
/* 111 */     this.collectionName = name;
/*     */   }
/*     */ 
/*     */   public void setCollectionNameWithSigmet(Sigmet sig)
/*     */   {
/* 116 */     StringBuilder sb = new StringBuilder("CCFP_SIGMET");
/*     */ 
/* 118 */     sb.append(":::");
/* 119 */     sb.append(sig.getEditableAttrPhenomSpeed()).append(":::");
/* 120 */     sb.append(sig.getEditableAttrPhenomDirection()).append(":::");
/* 121 */     sb.append(sig.getEditableAttrStartTime()).append(":::");
/* 122 */     sb.append(sig.getEditableAttrEndTime()).append(":::");
/*     */ 
/* 124 */     sb.append(sig.getEditableAttrPhenom()).append(":::");
/* 125 */     sb.append(sig.getEditableAttrPhenom2()).append(":::");
/* 126 */     sb.append(sig.getEditableAttrPhenomLat()).append(":::");
/* 127 */     sb.append(sig.getEditableAttrPhenomLon()).append(":::");
/* 128 */     sb.append(sig.getType());
/*     */ 
/* 130 */     setCollectionName(sb.toString());
/*     */   }
/*     */ 
/*     */   public boolean isPrimaryDEClosed() {
/* 134 */     DrawableElement de = getPrimaryDE();
/*     */ 
/* 136 */     if ((de != null) && ((de instanceof Line))) {
/* 137 */       return ((Line)de).isClosedLine().booleanValue();
/*     */     }
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   public void setToolName(String n) {
/* 143 */     this.tool = n;
/*     */   }
/*     */ 
/*     */   public void moveText2Last()
/*     */   {
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.Ccfp
 * JD-Core Version:    0.6.2
 */