package gov.noaa.nws.ncep.ui.pgen.gfa;

import gov.noaa.nws.ncep.ui.pgen.display.ILine;
import java.util.HashMap;

public abstract interface IGfa extends ILine
{
  public abstract String getGfaHazard();

  public abstract String getGfaFcstHr();

  public abstract String getGfaTag();

  public abstract String getGfaDesk();

  public abstract String getGfaIssueType();

  public abstract String getGfaType();

  public abstract String getSymbolType();

  public abstract String getGfaArea();

  public abstract String getGfaBeginning();

  public abstract String getGfaEnding();

  public abstract String getGfaStates();

  public abstract int getGfaCycleDay();

  public abstract int getGfaCycleHour();

  public abstract HashMap<String, String> getGfaValues();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.IGfa
 * JD-Core Version:    0.6.2
 */