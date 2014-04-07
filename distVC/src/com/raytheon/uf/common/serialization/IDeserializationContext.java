package com.raytheon.uf.common.serialization;

public abstract interface IDeserializationContext
{
  public abstract boolean readBool()
    throws SerializationException;

  public abstract byte readByte()
    throws SerializationException;

  public abstract short readI16()
    throws SerializationException;

  public abstract int readI32()
    throws SerializationException;

  public abstract long readI64()
    throws SerializationException;

  public abstract double readDouble()
    throws SerializationException;

  public abstract double[] readDoubleArray()
    throws SerializationException;

  public abstract float readFloat()
    throws SerializationException;

  public abstract String readString()
    throws SerializationException;

  public abstract byte[] readBinary()
    throws SerializationException;

  public abstract float[] readFloatArray()
    throws SerializationException;

  public abstract String readMessageStart()
    throws SerializationException;

  public abstract void readMessageEnd()
    throws SerializationException;

  public abstract Object readObject()
    throws SerializationException;
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.IDeserializationContext
 * JD-Core Version:    0.6.2
 */