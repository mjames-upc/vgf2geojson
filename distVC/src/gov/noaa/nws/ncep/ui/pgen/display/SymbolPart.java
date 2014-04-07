package gov.noaa.nws.ncep.ui.pgen.display;

import com.vividsolutions.jts.geom.Coordinate;

public abstract class SymbolPart
{
  public abstract Coordinate[] getPath();

  public abstract boolean isFilled();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.SymbolPart
 * JD-Core Version:    0.6.2
 */