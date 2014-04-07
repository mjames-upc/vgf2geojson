package gov.noaa.nws.ncep.ui.pgen.display;

public abstract interface ILine extends IMultiPoint
{
  public abstract String getPatternName();

  public abstract int getSmoothFactor();

  public abstract Boolean isClosedLine();

  public abstract Boolean isFilled();

  public abstract FillPatternList.FillPattern getFillPattern();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.ILine
 * JD-Core Version:    0.6.2
 */