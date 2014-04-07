package gov.noaa.nws.ncep.ui.pgen.display;

import gov.noaa.nws.ncep.common.staticdata.SPCCounty;
import gov.noaa.nws.ncep.edex.common.stationTables.Station;
import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox.WatchShape;
import java.awt.Color;
import java.util.List;

public abstract interface IWatchBox extends IMultiPoint
{
  public abstract Color[] getColors();

  public abstract Color getFillColor();

  public abstract WatchBox.WatchShape getWatchBoxShape();

  public abstract Station[] getAnchors();

  public abstract List<SPCCounty> getCountyList();

  public abstract String getWatchSymbolType();

  public abstract float getWatchSymbolWidth();

  public abstract double getWatchSymbolSize();

  public abstract boolean getFillFlag();

  public abstract int getWatchNumber();

  public abstract int getIssueFlag();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.IWatchBox
 * JD-Core Version:    0.6.2
 */