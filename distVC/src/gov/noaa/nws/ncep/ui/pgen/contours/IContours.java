package gov.noaa.nws.ncep.ui.pgen.contours;

import java.util.Calendar;

public abstract interface IContours
{
  public abstract String getParm();

  public abstract String getLevel();

  public abstract String getForecastHour();

  public abstract Calendar getTime1();

  public abstract Calendar getTime2();

  public abstract String getCint();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.contours.IContours
 * JD-Core Version:    0.6.2
 */