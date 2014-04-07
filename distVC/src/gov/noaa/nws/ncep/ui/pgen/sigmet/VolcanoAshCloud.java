/*    */ package gov.noaa.nws.ncep.ui.pgen.sigmet;
/*    */ 
/*    */ import com.vividsolutions.jts.geom.Coordinate;
/*    */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*    */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*    */ import java.awt.Color;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_PART, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.ADD_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.INTERPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.MODIFY})
/*    */ public class VolcanoAshCloud extends Sigmet
/*    */ {
/*    */   public static final String SIGMET_PGEN_TYPE = "VACL_SIGMET";
/*    */ 
/*    */   public DrawableElement copy()
/*    */   {
/* 48 */     VolcanoAshCloud newSigmet = new VolcanoAshCloud();
/* 49 */     newSigmet.update(this);
/*    */ 
/* 55 */     ArrayList ptsCopy = new ArrayList();
/* 56 */     for (int i = 0; i < getPoints().size(); i++) {
/* 57 */       ptsCopy.add(new Coordinate((Coordinate)getPoints().get(i)));
/*    */     }
/* 59 */     newSigmet.setPoints(ptsCopy);
/*    */ 
/* 65 */     Color[] colorCopy = new Color[getColors().length];
/* 66 */     for (int i = 0; i < getColors().length; i++) {
/* 67 */       colorCopy[i] = new Color(getColors()[i].getRed(), 
/* 68 */         getColors()[i].getGreen(), 
/* 69 */         getColors()[i].getBlue());
/*    */     }
/* 71 */     newSigmet.setColors(colorCopy);
/*    */ 
/* 76 */     newSigmet.setPgenCategory(new String(getPgenCategory()));
/* 77 */     newSigmet.setPgenType(new String(getPgenType()));
/* 78 */     newSigmet.setParent(getParent());
/* 79 */     newSigmet.setType(getType());
/* 80 */     newSigmet.setWidth(getWidth());
/* 81 */     String fhr = getEditableAttrFreeText();
/* 82 */     if ((fhr == null) && (getAttr() != null))
/* 83 */       fhr = ((Sigmet)getAttr()).getEditableAttrFreeText();
/* 84 */     newSigmet.setEditableAttrFreeText(fhr);
/* 85 */     newSigmet.setEditableAttrArea(getEditableAttrArea());
/* 86 */     newSigmet.setEditableAttrFromLine(getEditableAttrFromLine());
/* 87 */     newSigmet.setEditableAttrId(getEditableAttrId());
/* 88 */     newSigmet.setEditableAttrSeqNum(getEditableAttrSeqNum());
/*    */ 
/* 90 */     return newSigmet;
/*    */   }
/*    */ 
/*    */   public FillPatternList.FillPattern getFillPattern()
/*    */   {
/* 95 */     return FillPatternList.FillPattern.FILL_PATTERN_2;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.VolcanoAshCloud
 * JD-Core Version:    0.6.2
 */