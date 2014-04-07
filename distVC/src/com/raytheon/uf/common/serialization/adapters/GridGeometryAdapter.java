/*     */ package com.raytheon.uf.common.serialization.adapters;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*     */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*     */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*     */ import com.raytheon.uf.common.serialization.SerializationException;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import org.geotools.coverage.grid.GeneralGridEnvelope;
/*     */ import org.geotools.coverage.grid.GeneralGridGeometry;
/*     */ import org.geotools.coverage.grid.GridEnvelope2D;
/*     */ import org.geotools.geometry.GeneralEnvelope;
/*     */ import org.geotools.referencing.CRS;
/*     */ import org.opengis.coverage.grid.GridEnvelope;
/*     */ import org.opengis.geometry.Envelope;
/*     */ import org.opengis.referencing.FactoryException;
/*     */ import org.opengis.referencing.crs.CoordinateReferenceSystem;
/*     */ 
/*     */ public class GridGeometryAdapter extends XmlAdapter<GridGeometrySerialized, GeneralGridGeometry>
/*     */   implements ISerializationTypeAdapter<GeneralGridGeometry>
/*     */ {
/*     */   public GridGeometrySerialized marshal(GeneralGridGeometry v)
/*     */     throws Exception
/*     */   {
/*  62 */     GridGeometrySerialized ggs = new GridGeometrySerialized();
/*  63 */     ggs.rangeX = new Integer[] { Integer.valueOf(v.getGridRange().getLow(0)), 
/*  64 */       Integer.valueOf(v.getGridRange().getHigh(0)) };
/*  65 */     ggs.rangeY = new Integer[] { Integer.valueOf(v.getGridRange().getLow(1)), 
/*  66 */       Integer.valueOf(v.getGridRange().getHigh(1)) };
/*     */ 
/*  68 */     ggs.CRS = v.getCoordinateReferenceSystem().toWKT();
/*  69 */     ggs.envelopeMinX = Double.valueOf(v.getEnvelope().getMinimum(0));
/*  70 */     ggs.envelopeMinY = Double.valueOf(v.getEnvelope().getMinimum(1));
/*  71 */     ggs.envelopeMaxX = Double.valueOf(v.getEnvelope().getMaximum(0));
/*  72 */     ggs.envelopeMaxY = Double.valueOf(v.getEnvelope().getMaximum(1));
/*     */ 
/*  74 */     if ((v.getGridRange().getDimension() == 3) && 
/*  75 */       (v.getEnvelope().getDimension() == 3)) {
/*  76 */       ggs.rangeZ = new Integer[] { Integer.valueOf(v.getGridRange().getLow(2)), 
/*  77 */         Integer.valueOf(v.getGridRange().getHigh(2)) };
/*  78 */       ggs.envelopeMinZ = Double.valueOf(v.getEnvelope().getMinimum(2));
/*  79 */       ggs.envelopeMaxZ = Double.valueOf(v.getEnvelope().getMaximum(2));
/*     */     }
/*     */ 
/*  82 */     return ggs;
/*     */   }
/*     */ 
/*     */   public GeneralGridGeometry unmarshal(GridGeometrySerialized v)
/*     */     throws Exception
/*     */   {
/*  88 */     CoordinateReferenceSystem crs = CRS.parseWKT(v.CRS);
/*  89 */     GeneralEnvelope env = new GeneralEnvelope(crs);
/*  90 */     env.setRange(0, v.envelopeMinX.doubleValue(), v.envelopeMaxX.doubleValue());
/*  91 */     env.setRange(1, v.envelopeMinY.doubleValue(), v.envelopeMaxY.doubleValue());
/*     */ 
/*  93 */     GeneralGridGeometry ggg = null;
/*  94 */     int gridX = v.rangeX[1].intValue();
/*  95 */     int gridY = v.rangeY[1].intValue();
/*     */ 
/*  97 */     if ((v.envelopeMinZ == null) && (v.envelopeMaxZ == null) && 
/*  98 */       (v.rangeZ == null)) {
/*  99 */       GridEnvelope2D ge = new GridEnvelope2D(v.rangeX[0].intValue(), v.rangeY[0].intValue(), 
/* 100 */         v.rangeX[1].intValue() - v.rangeX[0].intValue() + 1, v.rangeY[1].intValue() - v.rangeY[0].intValue() + 
/* 101 */         1);
/* 102 */       ggg = new GeneralGridGeometry(ge, env);
/*     */     } else {
/* 104 */       int gridZ = v.rangeZ[1].intValue();
/*     */ 
/* 106 */       env.setRange(2, v.envelopeMinZ.doubleValue(), v.envelopeMaxZ.doubleValue());
/* 107 */       GeneralGridEnvelope gge = new GeneralGridEnvelope(new int[] { 
/* 108 */         gridX / -2, gridY / -2, gridZ / -2 }, new int[] { 
/* 109 */         gridX / 2, gridY / 2, gridZ / 2 }, false);
/* 110 */       ggg = new GeneralGridGeometry(gge, env);
/*     */     }
/*     */ 
/* 113 */     return ggg;
/*     */   }
/*     */ 
/*     */   public void serialize(ISerializationContext serializer, GeneralGridGeometry object)
/*     */     throws SerializationException
/*     */   {
/* 127 */     int numDims = object.getDimension();
/* 128 */     GridEnvelope range = object.getGridRange();
/* 129 */     Envelope env = object.getEnvelope();
/*     */ 
/* 131 */     serializer.writeString(object.getCoordinateReferenceSystem().toWKT());
/* 132 */     serializer.writeI32(numDims);
/* 133 */     for (int i = 0; i < numDims; i++) {
/* 134 */       serializer.writeI32(range.getLow(i));
/* 135 */       serializer.writeI32(range.getHigh(i));
/* 136 */       serializer.writeDouble(env.getMinimum(i));
/* 137 */       serializer.writeDouble(env.getMaximum(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   public GeneralGridGeometry deserialize(IDeserializationContext deserializer)
/*     */     throws SerializationException
/*     */   {
/*     */     try
/*     */     {
/* 152 */       CoordinateReferenceSystem crs = CRS.parseWKT(deserializer
/* 153 */         .readString());
/* 154 */       int numDims = deserializer.readI32();
/* 155 */       GeneralEnvelope env = new GeneralEnvelope(crs);
/* 156 */       int[] lowRange = new int[numDims];
/* 157 */       int[] highRange = new int[numDims];
/* 158 */       for (int i = 0; i < numDims; i++) {
/* 159 */         lowRange[i] = deserializer.readI32();
/* 160 */         highRange[i] = deserializer.readI32();
/* 161 */         env.setRange(i, deserializer.readDouble(), 
/* 162 */           deserializer.readDouble());
/*     */       }
/*     */ 
/* 165 */       return new GeneralGridGeometry(new GeneralGridEnvelope(lowRange, 
/* 166 */         highRange, false), env);
/*     */     } catch (FactoryException e) {
/* 168 */       throw new SerializationException(
/* 169 */         "Error deserializing GeneralGridGeometry, could not read CRS", 
/* 170 */         e);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.GridGeometryAdapter
 * JD-Core Version:    0.6.2
 */