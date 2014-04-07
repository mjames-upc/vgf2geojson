package gov.noaa.nws.ncep.ui.pgen.display;

import java.awt.Color;

public abstract interface IAttribute
{
  public abstract Color[] getColors();

  public abstract float getLineWidth();

  public abstract double getSizeScale();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.IAttribute
 * JD-Core Version:    0.6.2
 */