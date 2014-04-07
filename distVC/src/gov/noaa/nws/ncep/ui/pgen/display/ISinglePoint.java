package gov.noaa.nws.ncep.ui.pgen.display;

import com.vividsolutions.jts.geom.Coordinate;

public abstract interface ISinglePoint extends IAttribute
{
  public abstract Coordinate getLocation();

  public abstract Boolean isClear();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.ISinglePoint
 * JD-Core Version:    0.6.2
 */