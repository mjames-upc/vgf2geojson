/*     */ package gov.noaa.nws.ncep.ui.pgen.sigmet;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_PART, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.ADD_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.MODIFY})
/*     */ public class ConvSigmet extends Sigmet
/*     */ {
/*     */   public static final String SIGMET_PGEN_CATEGORY = "Sigmet";
/*     */   public static final String SIGMET_PGEN_TYPE = "CONV_SIGMET";
/*     */ 
/*     */   public DrawableElement copy()
/*     */   {
/*  46 */     ConvSigmet newSigmet = new ConvSigmet();
/*  47 */     newSigmet.update(this);
/*     */ 
/*  53 */     ArrayList ptsCopy = new ArrayList();
/*  54 */     for (int i = 0; i < getPoints().size(); i++) {
/*  55 */       ptsCopy.add(new Coordinate((Coordinate)getPoints().get(i)));
/*     */     }
/*  57 */     newSigmet.setPoints(ptsCopy);
/*     */ 
/*  63 */     Color[] colorCopy = new Color[getColors().length];
/*  64 */     for (int i = 0; i < getColors().length; i++) {
/*  65 */       colorCopy[i] = new Color(getColors()[i].getRed(), 
/*  66 */         getColors()[i].getGreen(), 
/*  67 */         getColors()[i].getBlue());
/*     */     }
/*  69 */     newSigmet.setColors(colorCopy);
/*     */ 
/*  74 */     newSigmet.setPgenCategory(new String(getPgenCategory()));
/*  75 */     newSigmet.setPgenType(new String(getPgenType()));
/*  76 */     newSigmet.setParent(getParent());
/*  77 */     newSigmet.setType(getType());
/*  78 */     newSigmet.setWidth(getWidth());
/*     */ 
/*  80 */     newSigmet.setEditableAttrArea(getEditableAttrArea());
/*  81 */     newSigmet.setEditableAttrFromLine(getEditableAttrFromLine());
/*  82 */     newSigmet.setEditableAttrId(getEditableAttrId());
/*  83 */     newSigmet.setEditableAttrSeqNum(getEditableAttrSeqNum());
/*     */ 
/*  86 */     newSigmet.setFillPattern(getFillPattern());
/*     */ 
/*  88 */     newSigmet.setEditableAttrFreeText(getEditableAttrFreeText());
/*  89 */     newSigmet.setEditableAttrFromLine(getEditableAttrFromLine());
/*  90 */     newSigmet.setEditableAttrStartTime(getEditableAttrStartTime());
/*  91 */     newSigmet.setEditableAttrEndTime(getEditableAttrEndTime());
/*  92 */     newSigmet.setEditableAttrPhenom(getEditableAttrPhenom());
/*  93 */     newSigmet.setEditableAttrPhenom2(getEditableAttrPhenom2());
/*  94 */     newSigmet.setEditableAttrPhenomLat(getEditableAttrPhenomLat());
/*  95 */     newSigmet.setEditableAttrPhenomLon(getEditableAttrPhenomLon());
/*  96 */     newSigmet.setEditableAttrPhenomSpeed(getEditableAttrPhenomSpeed());
/*  97 */     newSigmet.setEditableAttrPhenomDirection(getEditableAttrPhenomDirection());
/*     */ 
/*  99 */     newSigmet.setEditableAttrArea(getEditableAttrArea());
/* 100 */     newSigmet.setEditableAttrRemarks(getEditableAttrRemarks());
/* 101 */     newSigmet.setEditableAttrPhenomName(getEditableAttrPhenomName());
/* 102 */     newSigmet.setEditableAttrPhenomPressure(getEditableAttrPhenomPressure());
/* 103 */     newSigmet.setEditableAttrPhenomMaxWind(getEditableAttrPhenomMaxWind());
/* 104 */     newSigmet.setEditableAttrTrend(getEditableAttrTrend());
/* 105 */     newSigmet.setEditableAttrMovement(getEditableAttrMovement());
/* 106 */     newSigmet.setEditableAttrLevel(getEditableAttrLevel());
/* 107 */     newSigmet.setEditableAttrLevelInfo1(getEditableAttrLevelInfo1());
/* 108 */     newSigmet.setEditableAttrLevelInfo2(getEditableAttrLevelInfo2());
/* 109 */     newSigmet.setEditableAttrLevelText1(getEditableAttrLevelText1());
/* 110 */     newSigmet.setEditableAttrLevelText2(getEditableAttrLevelText2());
/* 111 */     newSigmet.setEditableAttrFir(getEditableAttrFir());
/*     */ 
/* 113 */     return newSigmet;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.ConvSigmet
 * JD-Core Version:    0.6.2
 */