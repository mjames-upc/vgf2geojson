package gov.noaa.nws.ncep.ui.pgen.controls;

import gov.noaa.nws.ncep.ui.pgen.PGenException;

public abstract class PgenCommand
{
  public abstract void execute()
    throws PGenException;

  public abstract void undo()
    throws PGenException;
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.PgenCommand
 * JD-Core Version:    0.6.2
 */