package gov.noaa.nws.ncep.ui.pgen.display;

import com.vividsolutions.jts.geom.Coordinate;
import java.awt.Color;

public abstract interface IKink extends IAttribute
{
  public abstract Coordinate getStartPoint();

  public abstract Coordinate getEndPoint();

  public abstract Color getColor();

  public abstract double getKinkPosition();

  public abstract ArrowHead.ArrowHeadType getArrowHeadType();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.IKink
 * JD-Core Version:    0.6.2
 */