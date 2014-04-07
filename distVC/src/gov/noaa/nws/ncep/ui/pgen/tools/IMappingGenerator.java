package gov.noaa.nws.ncep.ui.pgen.tools;

import com.vividsolutions.jts.geom.Coordinate;
import java.util.LinkedHashMap;

public abstract interface IMappingGenerator
{
  public abstract LinkedHashMap<Coordinate, Coordinate> generateMappingPoints();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.IMappingGenerator
 * JD-Core Version:    0.6.2
 */