package gov.noaa.nws.ncep.ui.pgen.tools;

import gov.noaa.nws.ncep.ui.pgen.elements.Jet;

public abstract interface IJetBarb
{
  public abstract void setAddingBarbHandler();

  public abstract void setDeletingBarbHandler();

  public abstract void setAddingHashHandler();

  public abstract void setDeletingHashHandler();

  public abstract void resetMouseHandler();

  public abstract Jet getJet();

  public abstract void setJet(Jet paramJet);
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.IJetBarb
 * JD-Core Version:    0.6.2
 */