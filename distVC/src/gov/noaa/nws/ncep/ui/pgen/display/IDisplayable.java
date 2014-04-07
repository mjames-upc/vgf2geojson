package gov.noaa.nws.ncep.ui.pgen.display;

import com.raytheon.uf.viz.core.IGraphicsTarget;
import com.raytheon.uf.viz.core.drawables.PaintProperties;

public abstract interface IDisplayable
{
  public abstract void draw(IGraphicsTarget paramIGraphicsTarget, PaintProperties paramPaintProperties);

  public abstract void dispose();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.IDisplayable
 * JD-Core Version:    0.6.2
 */