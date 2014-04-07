package gov.noaa.nws.ncep.ui.pgen.elements.tcm;

import gov.noaa.nws.ncep.ui.pgen.display.ISinglePoint;

public abstract interface ITcmWindQuarter extends ISinglePoint
{
  public abstract double[] getQuarters();

  public abstract int getWindSpeed();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.tcm.ITcmWindQuarter
 * JD-Core Version:    0.6.2
 */