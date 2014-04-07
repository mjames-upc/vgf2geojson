package com.raytheon.uf.common.serialization;

public abstract interface ISerializationContext
{
  public abstract void writeBool(boolean paramBoolean)
    throws SerializationException;

  public abstract void writeByte(byte paramByte)
    throws SerializationException;

  public abstract void writeI16(short paramShort)
    throws SerializationException;

  public abstract void writeI32(int paramInt)
    throws SerializationException;

  public abstract void writeI64(long paramLong)
    throws SerializationException;

  public abstract void writeDouble(double paramDouble)
    throws SerializationException;

  public abstract void writeDoubleArray(double[] paramArrayOfDouble)
    throws SerializationException;

  public abstract void writeFloat(float paramFloat)
    throws SerializationException;

  public abstract void writeString(String paramString)
    throws SerializationException;

  public abstract void writeBinary(byte[] paramArrayOfByte)
    throws SerializationException;

  public abstract void writeFloatArray(float[] paramArrayOfFloat)
    throws SerializationException;

  public abstract void writeMessageStart(String paramString)
    throws SerializationException;

  public abstract void writeMessageEnd()
    throws SerializationException;

  public abstract void writeObject(Object paramObject)
    throws SerializationException;
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.ISerializationContext
 * JD-Core Version:    0.6.2
 */