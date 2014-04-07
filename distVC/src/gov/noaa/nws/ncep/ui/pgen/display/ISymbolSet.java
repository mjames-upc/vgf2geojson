package gov.noaa.nws.ncep.ui.pgen.display;

import com.vividsolutions.jts.geom.Coordinate;
import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;

public abstract interface ISymbolSet
{
  public abstract Symbol getSymbol();

  public abstract Coordinate[] getLocations();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.ISymbolSet
 * JD-Core Version:    0.6.2
 */