/*     */ package com.raytheon.uf.common.serialization.adapters;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.GeometryCollection;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.geom.MultiLineString;
/*     */ import com.vividsolutions.jts.geom.MultiPoint;
/*     */ import com.vividsolutions.jts.geom.MultiPolygon;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class FloatWKBWriter
/*     */ {
/*     */   public void writeGeometry(Geometry geom, OutputStream out)
/*     */     throws IOException
/*     */   {
/*  62 */     writeGeometry(geom, new DataOutputStream(out));
/*     */   }
/*     */ 
/*     */   private void writeGeometry(Geometry geom, DataOutput d) throws IOException
/*     */   {
/*  67 */     d.write(0);
/*  68 */     if ((geom instanceof Point))
/*  69 */       writePoint((Point)geom, d);
/*  70 */     else if ((geom instanceof LineString))
/*  71 */       writeLineString((LineString)geom, d);
/*  72 */     else if ((geom instanceof Polygon))
/*  73 */       writePolygon((Polygon)geom, d);
/*  74 */     else if ((geom instanceof MultiPoint))
/*  75 */       writeMultiPoint((MultiPoint)geom, d);
/*  76 */     else if ((geom instanceof MultiLineString))
/*  77 */       writeMultiLineString((MultiLineString)geom, d);
/*  78 */     else if ((geom instanceof MultiPolygon))
/*  79 */       writeMultiPolygon((MultiPolygon)geom, d);
/*  80 */     else if ((geom instanceof GeometryCollection))
/*  81 */       writeGeometryCollection((GeometryCollection)geom, d);
/*     */     else
/*  83 */       throw new IOException("Unknown Geometry type: " + 
/*  84 */         geom.getClass().getSimpleName());
/*     */   }
/*     */ 
/*     */   private void writePoint(Point p, DataOutput d) throws IOException
/*     */   {
/*  89 */     d.writeInt(1);
/*  90 */     writeCoordinate(p.getCoordinate(), d);
/*     */   }
/*     */ 
/*     */   private void writeLineString(LineString ls, DataOutput d) throws IOException
/*     */   {
/*  95 */     d.writeInt(2);
/*  96 */     writeCoordinates(ls.getCoordinates(), d);
/*     */   }
/*     */ 
/*     */   private void writePolygon(Polygon polygon, DataOutput d) throws IOException {
/* 100 */     d.writeInt(3);
/* 101 */     d.writeInt(polygon.getNumInteriorRing() + 1);
/* 102 */     writeCoordinates(polygon.getExteriorRing().getCoordinates(), d);
/* 103 */     for (int i = 0; i < polygon.getNumInteriorRing(); i++)
/* 104 */       writeCoordinates(polygon.getInteriorRingN(i).getCoordinates(), d);
/*     */   }
/*     */ 
/*     */   private void writeMultiPoint(MultiPoint mp, DataOutput d)
/*     */     throws IOException
/*     */   {
/* 110 */     d.writeInt(4);
/* 111 */     writeMultiGeometry(mp, d);
/*     */   }
/*     */ 
/*     */   private void writeMultiLineString(MultiLineString mls, DataOutput d) throws IOException
/*     */   {
/* 116 */     d.writeInt(5);
/* 117 */     writeMultiGeometry(mls, d);
/*     */   }
/*     */ 
/*     */   private void writeMultiPolygon(MultiPolygon mp, DataOutput d) throws IOException
/*     */   {
/* 122 */     d.writeInt(6);
/* 123 */     writeMultiGeometry(mp, d);
/*     */   }
/*     */ 
/*     */   private void writeGeometryCollection(GeometryCollection gc, DataOutput d) throws IOException
/*     */   {
/* 128 */     d.writeInt(7);
/* 129 */     writeMultiGeometry(gc, d);
/*     */   }
/*     */ 
/*     */   private void writeMultiGeometry(GeometryCollection gc, DataOutput d) throws IOException
/*     */   {
/* 134 */     d.writeInt(gc.getNumGeometries());
/* 135 */     for (int i = 0; i < gc.getNumGeometries(); i++)
/* 136 */       writeGeometry(gc.getGeometryN(i), d);
/*     */   }
/*     */ 
/*     */   private void writeCoordinates(Coordinate[] coordinates, DataOutput d)
/*     */     throws IOException
/*     */   {
/* 142 */     d.writeInt(coordinates.length);
/* 143 */     for (Coordinate c : coordinates)
/* 144 */       writeCoordinate(c, d);
/*     */   }
/*     */ 
/*     */   private void writeCoordinate(Coordinate c, DataOutput d) throws IOException
/*     */   {
/* 149 */     d.writeFloat((float)c.x);
/* 150 */     d.writeFloat((float)c.y);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.FloatWKBWriter
 * JD-Core Version:    0.6.2
 */