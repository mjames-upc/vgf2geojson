package com.raytheon.uf.common.serialization;

public abstract interface ISerializationTypeAdapter<T>
{
  public abstract void serialize(ISerializationContext paramISerializationContext, T paramT)
    throws SerializationException;

  public abstract T deserialize(IDeserializationContext paramIDeserializationContext)
    throws SerializationException;
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.ISerializationTypeAdapter
 * JD-Core Version:    0.6.2
 */