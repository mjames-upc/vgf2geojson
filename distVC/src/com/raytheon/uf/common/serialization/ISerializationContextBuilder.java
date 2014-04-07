package com.raytheon.uf.common.serialization;

import java.io.InputStream;
import java.io.OutputStream;

public abstract interface ISerializationContextBuilder
{
  public abstract ISerializationContext buildSerializationContext(OutputStream paramOutputStream, DynamicSerializationManager paramDynamicSerializationManager);

  public abstract IDeserializationContext buildDeserializationContext(InputStream paramInputStream, DynamicSerializationManager paramDynamicSerializationManager);
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.ISerializationContextBuilder
 * JD-Core Version:    0.6.2
 */