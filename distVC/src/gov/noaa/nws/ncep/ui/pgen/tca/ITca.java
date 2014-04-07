package gov.noaa.nws.ncep.ui.pgen.tca;

import com.vividsolutions.jts.geom.Coordinate;
import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
import java.util.ArrayList;
import java.util.Calendar;

public abstract interface ITca extends IAttribute
{
  public abstract String getStormName();

  public abstract String getStormType();

  public abstract String getBasin();

  public abstract int getStormNumber();

  public abstract String getAdvisoryNumber();

  public abstract Calendar getAdvisoryTime();

  public abstract String getTimeZone();

  public abstract ArrayList<TropicalCycloneAdvisory> getAdvisories();

  public abstract String getIssuingStatus();

  public abstract Coordinate getTextLocation();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.ITca
 * JD-Core Version:    0.6.2
 */