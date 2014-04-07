package gov.noaa.nws.ncep.ui.pgen.tools;

import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;

public abstract interface ILabeledLine
{
  public abstract void setAddingLabelHandler();

  public abstract void resetMouseHandler();

  public abstract LabeledLine getLabeledLine();

  public abstract void setLabeledLine(LabeledLine paramLabeledLine);

  public abstract void setDeleteHandler(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.ILabeledLine
 * JD-Core Version:    0.6.2
 */