package gov.noaa.nws.ncep.ui.pgen.elements;

import com.vividsolutions.jts.geom.Coordinate;

public abstract interface IJetTools
{
  public abstract void snapJet(Jet paramJet);

  public abstract void snapJet(AbstractDrawableComponent paramAbstractDrawableComponent, Jet paramJet);

  public abstract void snapHash(Jet.JetHash paramJetHash, Coordinate paramCoordinate, Jet paramJet);

  public abstract Coordinate latLon2Relative(Coordinate paramCoordinate, Vector paramVector);

  public abstract Coordinate relative2LatLon(Coordinate paramCoordinate, Vector paramVector);

  public abstract java.util.Vector<Integer> checkHashOnJet(Jet paramJet);

  public abstract void addBarbHashFromAnotherJet(Jet paramJet1, Jet paramJet2);
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.IJetTools
 * JD-Core Version:    0.6.2
 */