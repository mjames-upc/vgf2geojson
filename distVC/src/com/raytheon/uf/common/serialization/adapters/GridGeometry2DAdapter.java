/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ import org.geotools.coverage.grid.GridEnvelope2D;
/*    */ import org.geotools.coverage.grid.GridGeometry2D;
/*    */ import org.geotools.geometry.Envelope2D;
/*    */ import org.geotools.referencing.CRS;
/*    */ import org.opengis.geometry.Envelope;
/*    */ import org.opengis.referencing.crs.CoordinateReferenceSystem;
/*    */ 
/*    */ public class GridGeometry2DAdapter
/*    */   implements ISerializationTypeAdapter<GridGeometry2D>
/*    */ {
/*    */   public GridGeometry2D deserialize(IDeserializationContext deserializer)
/*    */     throws SerializationException
/*    */   {
/*    */     try
/*    */     {
/* 58 */       int x = deserializer.readI32();
/* 59 */       int y = deserializer.readI32();
/* 60 */       int width = deserializer.readI32();
/* 61 */       int height = deserializer.readI32();
/* 62 */       GridEnvelope2D gridRange = new GridEnvelope2D(x, y, width, height);
/*    */ 
/* 64 */       CoordinateReferenceSystem crs = CRS.parseWKT(deserializer
/* 65 */         .readString());
/*    */ 
/* 67 */       double dx = deserializer.readDouble();
/* 68 */       double dy = deserializer.readDouble();
/* 69 */       double dw = deserializer.readDouble();
/* 70 */       double dh = deserializer.readDouble();
/* 71 */       Envelope envelope = new Envelope2D(crs, dx, dy, dw, dh);
/*    */ 
/* 73 */       return new GridGeometry2D(gridRange, envelope);
/*    */     }
/*    */     catch (Throwable e) {
/* 76 */       throw new SerializationException(
/* 77 */         "Error deserializing GridGeomtry2D", e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, GridGeometry2D gridGeom)
/*    */     throws SerializationException
/*    */   {
/* 84 */     serializer.writeI32(gridGeom.getGridRange2D().x);
/* 85 */     serializer.writeI32(gridGeom.getGridRange2D().y);
/* 86 */     serializer.writeI32(gridGeom.getGridRange2D().width);
/* 87 */     serializer.writeI32(gridGeom.getGridRange2D().height);
/*    */ 
/* 89 */     serializer.writeString(gridGeom.getCoordinateReferenceSystem().toWKT());
/* 90 */     serializer.writeDouble(gridGeom.getEnvelope().getMinimum(0));
/* 91 */     serializer.writeDouble(gridGeom.getEnvelope().getMinimum(1));
/* 92 */     serializer.writeDouble(gridGeom.getEnvelope().getSpan(0));
/* 93 */     serializer.writeDouble(gridGeom.getEnvelope().getSpan(1));
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.GridGeometry2DAdapter
 * JD-Core Version:    0.6.2
 */