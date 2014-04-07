package com.raytheon.uf.common.serialization.adapters;

import com.raytheon.uf.common.serialization.ISerializableObject;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class GridGeometrySerialized
  implements ISerializableObject
{

  @XmlAttribute
  public Integer[] rangeX;

  @XmlAttribute
  public Integer[] rangeY;

  @XmlAttribute(required=false)
  public Integer[] rangeZ;

  @XmlAttribute
  public Double envelopeMinX;

  @XmlAttribute
  public Double envelopeMaxX;

  @XmlAttribute
  public Double envelopeMinY;

  @XmlAttribute
  public Double envelopeMaxY;

  @XmlAttribute(required=false)
  public Double envelopeMinZ;

  @XmlAttribute(required=false)
  public Double envelopeMaxZ;

  @XmlElement
  public String CRS;
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.GridGeometrySerialized
 * JD-Core Version:    0.6.2
 */