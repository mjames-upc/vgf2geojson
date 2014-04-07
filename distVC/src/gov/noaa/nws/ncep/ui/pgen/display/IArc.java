package gov.noaa.nws.ncep.ui.pgen.display;

import com.vividsolutions.jts.geom.Coordinate;

public abstract interface IArc extends IAttribute
{
  public abstract Coordinate getCenterPoint();

  public abstract Coordinate getCircumferencePoint();

  public abstract double getAxisRatio();

  public abstract double getStartAngle();

  public abstract double getEndAngle();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.IArc
 * JD-Core Version:    0.6.2
 */