package gov.noaa.nws.ncep.ui.pgen.display;

public abstract interface IMidCloudText extends IText
{
  public abstract String getCloudTypes();

  public abstract String getCloudAmounts();

  public abstract boolean hasTurbulence();

  public abstract String getTurbulencePattern();

  public abstract String getTurbulenceLevels();

  public abstract boolean hasIcing();

  public abstract String getIcingPattern();

  public abstract String getIcingLevels();

  public abstract boolean hasTstorm();

  public abstract String getTstormTypes();

  public abstract String getTstormLevels();

  public abstract boolean isTwoColumns();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.IMidCloudText
 * JD-Core Version:    0.6.2
 */