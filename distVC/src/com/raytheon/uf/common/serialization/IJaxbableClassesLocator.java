package com.raytheon.uf.common.serialization;

import java.util.List;

public abstract interface IJaxbableClassesLocator
{
  public abstract List<Class<ISerializableObject>> getJaxbables();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.IJaxbableClassesLocator
 * JD-Core Version:    0.6.2
 */