/*     */ package com.raytheon.uf.common.serialization.adapters;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*     */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*     */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*     */ import com.raytheon.uf.common.serialization.SerializationException;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import org.geotools.geometry.jts.ReferencedEnvelope;
/*     */ import org.geotools.referencing.CRS;
/*     */ import org.opengis.referencing.FactoryException;
/*     */ import org.opengis.referencing.crs.CoordinateReferenceSystem;
/*     */ 
/*     */ public class ReferencedEnvelopeAdapter extends XmlAdapter<ReferencedEnvelopeSerialized, ReferencedEnvelope>
/*     */   implements ISerializationTypeAdapter<ReferencedEnvelope>
/*     */ {
/*     */   public ReferencedEnvelope deserialize(IDeserializationContext deserializer)
/*     */     throws SerializationException
/*     */   {
/*     */     try
/*     */     {
/*  60 */       CoordinateReferenceSystem crs = CRS.parseWKT(deserializer.readString());
/*     */ 
/*  62 */       return new ReferencedEnvelope(deserializer.readDouble(), 
/*  63 */         deserializer.readDouble(), deserializer.readDouble(), 
/*  64 */         deserializer.readDouble(), crs);
/*     */     } catch (FactoryException e) {
/*  66 */       throw new SerializationException(
/*  67 */         "Error deserializing ReferencedEnvelope, could not read CRS", 
/*  68 */         e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void serialize(ISerializationContext serializer, ReferencedEnvelope object)
/*     */     throws SerializationException
/*     */   {
/*  75 */     serializer.writeString(object.getCoordinateReferenceSystem().toWKT());
/*  76 */     serializer.writeDouble(object.getMinX());
/*  77 */     serializer.writeDouble(object.getMaxX());
/*  78 */     serializer.writeDouble(object.getMinY());
/*  79 */     serializer.writeDouble(object.getMaxY());
/*     */   }
/*     */ 
/*     */   public ReferencedEnvelope unmarshal(ReferencedEnvelopeSerialized v)
/*     */     throws Exception
/*     */   {
/*  85 */     return new ReferencedEnvelope(v.minX.doubleValue(), v.maxX.doubleValue(), v.minY.doubleValue(), v.maxY.doubleValue(), 
/*  86 */       CRS.parseWKT(v.crs));
/*     */   }
/*     */ 
/*     */   public ReferencedEnvelopeSerialized marshal(ReferencedEnvelope v)
/*     */     throws Exception
/*     */   {
/*  92 */     if (v == null) {
/*  93 */       return null;
/*     */     }
/*  95 */     ReferencedEnvelopeSerialized r = new ReferencedEnvelopeSerialized();
/*  96 */     r.crs = v.getCoordinateReferenceSystem().toWKT();
/*  97 */     r.minX = Double.valueOf(v.getMinX());
/*  98 */     r.maxX = Double.valueOf(v.getMaxX());
/*  99 */     r.minY = Double.valueOf(v.getMinY());
/* 100 */     r.maxY = Double.valueOf(v.getMaxY());
/* 101 */     return r;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.ReferencedEnvelopeAdapter
 * JD-Core Version:    0.6.2
 */