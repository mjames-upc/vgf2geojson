/*     */ package com.raytheon.uf.common.serialization.adapters;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.GeometryCollection;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.geom.LinearRing;
/*     */ import com.vividsolutions.jts.geom.MultiLineString;
/*     */ import com.vividsolutions.jts.geom.MultiPoint;
/*     */ import com.vividsolutions.jts.geom.MultiPolygon;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import com.vividsolutions.jts.geom.Polygon;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ public class FloatWKBReader
/*     */ {
/*     */   private final GeometryFactory factory;
/*     */ 
/*     */   public FloatWKBReader(GeometryFactory factory)
/*     */   {
/*  66 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */   public Geometry readGeometry(InputStream in) throws IOException {
/*  70 */     return readGeometry(new DataInputStream(in));
/*     */   }
/*     */ 
/*     */   private Geometry readGeometry(DataInput di) throws IOException {
/*  74 */     byte byteOrder = di.readByte();
/*  75 */     if (byteOrder == 1) {
/*  76 */       throw new IOException(
/*  77 */         "WKBF does not currently support little endian");
/*     */     }
/*  79 */     int type = di.readInt();
/*  80 */     switch (type) {
/*     */     case 1:
/*  82 */       return readPoint(di);
/*     */     case 2:
/*  84 */       return readLineString(di);
/*     */     case 3:
/*  86 */       return readPolygon(di);
/*     */     case 4:
/*  88 */       return readMultiPoint(di);
/*     */     case 5:
/*  90 */       return readMultiLineString(di);
/*     */     case 6:
/*  92 */       return readMultiPolygon(di);
/*     */     case 7:
/*  94 */       return readGeometryCollection(di);
/*     */     }
/*     */ 
/*  98 */     throw new IOException("Unknown WKB type " + (type & 0xFF));
/*     */   }
/*     */ 
/*     */   private Point readPoint(DataInput di) throws IOException {
/* 102 */     return this.factory.createPoint(readCoordinate(di));
/*     */   }
/*     */ 
/*     */   private LineString readLineString(DataInput di) throws IOException {
/* 106 */     return this.factory.createLineString(readCoordinates(di));
/*     */   }
/*     */ 
/*     */   private Polygon readPolygon(DataInput di) throws IOException {
/* 110 */     int size = di.readInt();
/* 111 */     LinearRing shell = null;
/* 112 */     LinearRing[] holes = null;
/* 113 */     shell = this.factory.createLinearRing(readCoordinates(di));
/* 114 */     if (size > 1) {
/* 115 */       holes = new LinearRing[size - 1];
/* 116 */       for (int i = 1; i < size; i++) {
/* 117 */         holes[(i - 1)] = this.factory.createLinearRing(readCoordinates(di));
/*     */       }
/*     */     }
/* 120 */     return this.factory.createPolygon(shell, holes);
/*     */   }
/*     */ 
/*     */   private MultiPoint readMultiPoint(DataInput di) throws IOException {
/* 124 */     return this.factory.createMultiPoint((Point[])readMultiGeometry(di, Point.class));
/*     */   }
/*     */ 
/*     */   private MultiLineString readMultiLineString(DataInput di) throws IOException
/*     */   {
/* 129 */     return this.factory.createMultiLineString((LineString[])readMultiGeometry(di, 
/* 130 */       LineString.class));
/*     */   }
/*     */ 
/*     */   private MultiPolygon readMultiPolygon(DataInput di) throws IOException {
/* 134 */     return this.factory.createMultiPolygon((Polygon[])readMultiGeometry(di, Polygon.class));
/*     */   }
/*     */ 
/*     */   private GeometryCollection readGeometryCollection(DataInput di) throws IOException
/*     */   {
/* 139 */     return this.factory.createGeometryCollection(readMultiGeometry(di, 
/* 140 */       Geometry.class));
/*     */   }
/*     */ 
/*     */   private <T extends Geometry> T[] readMultiGeometry(DataInput di, Class<T> geomType) throws IOException
/*     */   {
/* 145 */     int size = di.readInt();
/*     */ 
/* 147 */     Geometry[] geoms = (Geometry[])Array.newInstance(geomType, size);
/* 148 */     for (int i = 0; i < size; i++) {
/* 149 */       Geometry g = readGeometry(di);
/* 150 */       if (geomType.isInstance(g))
/* 151 */         geoms[i] = ((Geometry)geomType.cast(g));
/*     */       else {
/* 153 */         throw new IOException("Expected a " + 
/* 154 */           geomType.getClass().getSimpleName() + 
/* 155 */           " but recieved a " + g.getClass().getSimpleName());
/*     */       }
/*     */     }
/* 158 */     return geoms;
/*     */   }
/*     */ 
/*     */   private Coordinate[] readCoordinates(DataInput di) throws IOException {
/* 162 */     int size = di.readInt();
/* 163 */     Coordinate[] coordinates = new Coordinate[size];
/* 164 */     for (int i = 0; i < size; i++) {
/* 165 */       coordinates[i] = readCoordinate(di);
/*     */     }
/* 167 */     return coordinates;
/*     */   }
/*     */ 
/*     */   private Coordinate readCoordinate(DataInput di) throws IOException {
/* 171 */     return new Coordinate(di.readFloat(), di.readFloat());
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.FloatWKBReader
 * JD-Core Version:    0.6.2
 */