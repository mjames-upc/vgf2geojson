package gov.noaa.nws.ncep.ui.pgen.sigmet;

import gov.noaa.nws.ncep.ui.pgen.display.ILine;
import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;

public abstract interface ICcfp extends ILine
{
  public abstract void copyEditableAttrToAbstractSigmet2(AbstractSigmet paramAbstractSigmet, LabeledLine paramLabeledLine);

  public abstract void copyEditableAttrToAbstractSigmet(AbstractSigmet paramAbstractSigmet);

  public abstract String getCcfpLineType();

  public abstract boolean isAreaType();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.ICcfp
 * JD-Core Version:    0.6.2
 */