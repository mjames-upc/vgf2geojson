package com.raytheon.uf.common.serialization.adapters;

import com.raytheon.uf.common.serialization.ISerializableObject;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ReferencedEnvelopeSerialized
  implements ISerializableObject
{

  @XmlAttribute
  public Double minX;

  @XmlAttribute
  public Double maxX;

  @XmlAttribute
  public Double minY;

  @XmlAttribute
  public Double maxY;

  @XmlElement
  public String crs;
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.ReferencedEnvelopeSerialized
 * JD-Core Version:    0.6.2
 */