package gov.noaa.nws.ncep.ui.pgen.display;

import com.vividsolutions.jts.geom.Coordinate;

public abstract interface IMultiPoint extends IAttribute
{
  public abstract Coordinate[] getLinePoints();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.IMultiPoint
 * JD-Core Version:    0.6.2
 */