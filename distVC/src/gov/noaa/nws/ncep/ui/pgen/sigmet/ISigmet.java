package gov.noaa.nws.ncep.ui.pgen.sigmet;

import gov.noaa.nws.ncep.ui.pgen.display.ILine;

public abstract interface ISigmet extends ILine
{
  public abstract String getLineType();

  public abstract double getWidth();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.ISigmet
 * JD-Core Version:    0.6.2
 */