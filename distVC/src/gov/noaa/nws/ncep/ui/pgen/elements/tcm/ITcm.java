package gov.noaa.nws.ncep.ui.pgen.elements.tcm;

import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
import java.util.Calendar;
import java.util.List;

public abstract interface ITcm extends IAttribute
{
  public abstract double[][] getWindRadius();

  public abstract TcmWindQuarters getWaveQuarters();

  public abstract List<TcmFcst> getTcmFcst();

  public abstract String getStormName();

  public abstract Calendar getAdvisoryTime();

  public abstract int getCentralPressure();

  public abstract int getFcstHr();

  public abstract String getStormType();

  public abstract int getStormNumber();

  public abstract int getAdvisoryNumber();

  public abstract String getBasin();

  public abstract int getEyeSize();

  public abstract int getPositionAccuracy();

  public abstract boolean isCorrection();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.tcm.ITcm
 * JD-Core Version:    0.6.2
 */