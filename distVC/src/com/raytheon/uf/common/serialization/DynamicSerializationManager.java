/*     */ package com.raytheon.uf.common.serialization;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.adapters.BufferAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.ByteBufferAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.CoordAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.EnumSetAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.FloatBufferAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.GeometryTypeAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.GridGeometry2DAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.GridGeometryAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.JTSEnvelopeAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.PointAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.ReferencedEnvelopeAdapter;
/*     */ import com.raytheon.uf.common.serialization.adapters.StackTraceElementAdapter;
/*     */ import com.raytheon.uf.common.serialization.annotations.DynamicSerialize;
/*     */ import com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement;
/*     */ import com.raytheon.uf.common.serialization.annotations.DynamicSerializeTypeAdapter;
/*     */ import com.raytheon.uf.common.serialization.thrift.ThriftSerializationContext;
/*     */ import com.raytheon.uf.common.serialization.thrift.ThriftSerializationContextBuilder;
/*     */ import com.raytheon.uf.common.util.ByteArrayOutputStreamPool;
/*     */ import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Envelope;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import java.awt.Point;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.xml.datatype.Duration;
/*     */ import javax.xml.namespace.QName;
/*     */ import net.sf.cglib.beans.BeanMap;
/*     */ import org.geotools.coverage.grid.GeneralGridGeometry;
/*     */ import org.geotools.coverage.grid.GridGeometry2D;
/*     */ import org.geotools.geometry.jts.ReferencedEnvelope;
/*     */ 
/*     */ public class DynamicSerializationManager
/*     */ {
/* 105 */   private static Map<SerializationType, DynamicSerializationManager> instanceMap = new HashMap();
/*     */   private ISerializationContextBuilder builder;
/* 120 */   private static Map<String, SerializationMetadata> serializedAttributes = new ConcurrentHashMap();
/*     */ 
/* 122 */   private static final SerializationMetadata NO_METADATA = new SerializationMetadata();
/*     */ 
/*     */   static
/*     */   {
/* 127 */     registerAdapter(GregorianCalendar.class, new BuiltInTypeSupport.CalendarSerializer());
/* 128 */     registerAdapter(XMLGregorianCalendarImpl.class, 
/* 129 */       new BuiltInTypeSupport.XMLGregorianCalendarSerializer());
/* 130 */     registerAdapter(java.util.Date.class, new BuiltInTypeSupport.DateSerializer());
/* 131 */     registerAdapter(Timestamp.class, new BuiltInTypeSupport.TimestampSerializer());
/* 132 */     registerAdapter(java.sql.Date.class, 
/* 133 */       new BuiltInTypeSupport.SqlDateSerializer());
/* 134 */     registerAdapter(Point.class, new PointAdapter());
/* 135 */     registerAdapter(Coordinate.class, new CoordAdapter());
/* 136 */     registerAdapter(BigDecimal.class, 
/* 137 */       new BuiltInTypeSupport.BigDecimalSerializer());
/* 138 */     registerAdapter(BigInteger.class, 
/* 139 */       new BuiltInTypeSupport.BigIntegerSerializer());
/* 140 */     registerAdapter(Geometry.class, new GeometryTypeAdapter());
/* 141 */     registerAdapter(Envelope.class, new JTSEnvelopeAdapter());
/* 142 */     registerAdapter(GridGeometry2D.class, new GridGeometry2DAdapter());
/* 143 */     registerAdapter(GeneralGridGeometry.class, new GridGeometryAdapter());
/* 144 */     registerAdapter(ReferencedEnvelope.class, 
/* 145 */       new ReferencedEnvelopeAdapter());
/* 146 */     registerAdapter(EnumSet.class, new EnumSetAdapter());
/* 147 */     registerAdapter(StackTraceElement.class, new StackTraceElementAdapter());
/* 148 */     registerAdapter(Duration.class, 
/* 149 */       new BuiltInTypeSupport.DurationSerializer());
/* 150 */     registerAdapter(QName.class, new BuiltInTypeSupport.QNameSerializer());
/* 151 */     registerAdapter(Throwable.class, 
/* 152 */       new BuiltInTypeSupport.ThrowableSerializer());
/*     */ 
/* 154 */     registerAdapter(ByteBuffer.class, new ByteBufferAdapter());
/* 155 */     registerAdapter(FloatBuffer.class, new FloatBufferAdapter());
/* 156 */     registerAdapter(Buffer.class, new BufferAdapter());
/*     */   }
/*     */ 
/*     */   public byte[] serialize(Object obj)
/*     */     throws SerializationException
/*     */   {
/* 177 */     ByteArrayOutputStream baos = ByteArrayOutputStreamPool.getInstance()
/* 178 */       .getStream();
/*     */     try
/*     */     {
/* 181 */       ISerializationContext ctx = this.builder.buildSerializationContext(
/* 182 */         baos, this);
/* 183 */       ctx.writeMessageStart("dynamicSerialize");
/* 184 */       serialize(ctx, obj);
/* 185 */       ctx.writeMessageEnd();
/* 186 */       return baos.toByteArray();
/*     */     } finally {
/* 188 */       if (baos != null)
/*     */         try
/*     */         {
/* 191 */           baos.close();
/*     */         }
/*     */         catch (IOException localIOException1)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void serialize(Object obj, OutputStream os)
/*     */     throws SerializationException
/*     */   {
/* 210 */     ISerializationContext ctx = this.builder.buildSerializationContext(os, 
/* 211 */       this);
/* 212 */     ctx.writeMessageStart("dynamicSerialize");
/* 213 */     serialize(ctx, obj);
/* 214 */     ctx.writeMessageEnd();
/*     */   }
/*     */ 
/*     */   public void serialize(ISerializationContext ctx, Object obj)
/*     */     throws SerializationException
/*     */   {
/* 231 */     BeanMap beanMap = null;
/*     */ 
/* 233 */     if ((obj != null) && (!obj.getClass().isArray()))
/* 234 */       beanMap = SerializationCache.getBeanMap(obj);
/*     */     try
/*     */     {
/* 237 */       SerializationMetadata metadata = null;
/* 238 */       if (obj != null) {
/* 239 */         metadata = getSerializationMetadata(obj.getClass().getName());
/*     */       }
/*     */ 
/* 242 */       ((ThriftSerializationContext)ctx).serializeMessage(obj, beanMap, 
/* 243 */         metadata);
/*     */     } finally {
/* 245 */       if (beanMap != null)
/* 246 */         SerializationCache.returnBeanMap(beanMap, obj);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object deserialize(InputStream istream)
/*     */     throws SerializationException
/*     */   {
/* 260 */     IDeserializationContext ctx = this.builder.buildDeserializationContext(
/* 261 */       istream, this);
/* 262 */     ctx.readMessageStart();
/* 263 */     Object obj = deserialize(ctx);
/* 264 */     ctx.readMessageEnd();
/* 265 */     return obj;
/*     */   }
/*     */ 
/*     */   public Object deserialize(IDeserializationContext ctx)
/*     */     throws SerializationException
/*     */   {
/* 280 */     return ((ThriftSerializationContext)ctx).deserializeMessage();
/*     */   }
/*     */ 
/*     */   public static <T> void registerAdapter(Class<? extends T> clazz, ISerializationTypeAdapter<T> adapter)
/*     */   {
/* 285 */     SerializationMetadata md = new SerializationMetadata();
/* 286 */     md.serializationFactory = adapter;
/* 287 */     md.adapterStructName = clazz.getName();
/* 288 */     if (serializedAttributes.containsKey(md.adapterStructName)) {
/* 289 */       throw new RuntimeException(
/* 290 */         "Could not create serialization metadata for class: " + 
/* 291 */         clazz + ", metadata already exists");
/*     */     }
/* 293 */     serializedAttributes.put(md.adapterStructName, md);
/*     */   }
/*     */ 
/*     */   public static SerializationMetadata inspect(Class<?> c)
/*     */   {
/* 311 */     SerializationMetadata attribs = (SerializationMetadata)serializedAttributes.get(c.getName());
/* 312 */     if (attribs != null) {
/* 313 */       return attribs;
/*     */     }
/*     */ 
/* 316 */     attribs = new SerializationMetadata();
/* 317 */     attribs.serializedAttributes = new ArrayList();
/* 318 */     attribs.attributesWithFactories = new HashMap();
/*     */ 
/* 320 */     DynamicSerializeTypeAdapter serializeAdapterTag = 
/* 321 */       (DynamicSerializeTypeAdapter)c
/* 321 */       .getAnnotation(DynamicSerializeTypeAdapter.class);
/*     */ 
/* 324 */     if (serializeAdapterTag != null) {
/* 325 */       Class factoryTag = serializeAdapterTag.factory();
/*     */       try {
/* 327 */         attribs.serializationFactory = 
/* 328 */           ((ISerializationTypeAdapter)factoryTag
/* 328 */           .newInstance());
/* 329 */         attribs.adapterStructName = c.getName();
/*     */       } catch (Exception e) {
/* 331 */         throw new RuntimeException("Factory could not be constructed: " + 
/* 332 */           factoryTag, e);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 337 */     if (attribs.serializationFactory == null) {
/* 338 */       Class superClazz = c.getSuperclass();
/* 339 */       while ((superClazz != null) && (attribs.serializationFactory == null)) {
/* 340 */         SerializationMetadata superMd = inspect(superClazz);
/* 341 */         if ((superMd != null) && (superMd.serializationFactory != null)) {
/* 342 */           attribs.serializationFactory = superMd.serializationFactory;
/* 343 */           attribs.adapterStructName = superMd.adapterStructName;
/*     */         }
/* 345 */         superClazz = superClazz.getSuperclass();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 351 */     DynamicSerialize serializeTag = (DynamicSerialize)c.getAnnotation(DynamicSerialize.class);
/* 352 */     if ((serializeTag == null) && (attribs.serializationFactory == null))
/* 353 */       return null;
/*     */     Field[] fields;
/* 356 */     if (attribs.serializationFactory == null)
/*     */     {
/* 358 */       Class clazz = c;
/* 359 */       Set getters = new HashSet();
/* 360 */       setters = new HashSet();
/* 361 */       while ((clazz != null) && (clazz != Object.class))
/*     */       {
/* 366 */         getters.clear();
/* 367 */         setters.clear();
/* 368 */         methods = c.getMethods();
/*     */         String name;
/* 369 */         for (Method m : methods) {
/* 370 */           name = m.getName();
/* 371 */           if (name.startsWith("get")) {
/* 372 */             name = name.substring(3);
/* 373 */             getters.add(name.toLowerCase());
/* 374 */           } else if (name.startsWith("is")) {
/* 375 */             name = name.substring(2);
/* 376 */             getters.add(name.toLowerCase());
/* 377 */           } else if (name.startsWith("set")) {
/* 378 */             name = name.substring(3);
/* 379 */             setters.add(name.toLowerCase());
/*     */           }
/*     */         }
/*     */ 
/* 383 */         fields = clazz.getDeclaredFields();
/* 384 */         for (Field field : fields)
/*     */         {
/* 386 */           int modifier = field.getModifiers();
/* 387 */           if (!Modifier.isFinal(modifier))
/*     */           {
/* 391 */             DynamicSerializeElement annotation = 
/* 392 */               (DynamicSerializeElement)field
/* 392 */               .getAnnotation(DynamicSerializeElement.class);
/* 393 */             if (annotation != null) {
/* 394 */               String fieldName = field.getName();
/*     */ 
/* 396 */               attribs.serializedAttributes.add(field.getName());
/* 397 */               if (serializeAdapterTag == null) {
/* 398 */                 serializeAdapterTag = 
/* 399 */                   (DynamicSerializeTypeAdapter)field.getType()
/* 399 */                   .getAnnotation(
/* 400 */                   DynamicSerializeTypeAdapter.class);
/*     */               }
/* 402 */               if (serializeAdapterTag != null) {
/*     */                 try {
/* 404 */                   attribs.attributesWithFactories.put(fieldName, 
/* 406 */                     (ISerializationTypeAdapter)serializeAdapterTag.factory()
/* 406 */                     .newInstance());
/*     */                 } catch (Exception e) {
/* 408 */                   throw new RuntimeException(
/* 409 */                     "Factory could not be instantiated", e);
/*     */                 }
/*     */               }
/*     */ 
/* 413 */               boolean foundGetter = false;
/* 414 */               boolean foundSetter = false;
/* 415 */               String lower = fieldName.toLowerCase();
/*     */ 
/* 417 */               if (getters.contains(lower)) {
/* 418 */                 foundGetter = true;
/*     */               }
/*     */ 
/* 421 */               if (setters.contains(lower)) {
/* 422 */                 foundSetter = true;
/*     */               }
/*     */ 
/* 425 */               if ((!foundGetter) || (!foundSetter)) {
/* 426 */                 String missing = "";
/* 427 */                 if ((!foundGetter) && (!foundSetter))
/* 428 */                   missing = "Getter and Setter";
/* 429 */                 else if (!foundGetter)
/* 430 */                   missing = "Getter";
/* 431 */                 else if (!foundSetter) {
/* 432 */                   missing = "Setter";
/*     */                 }
/*     */ 
/* 435 */                 throw new RuntimeException("Required " + missing + 
/* 436 */                   " on " + clazz.getName() + ":" + 
/* 437 */                   field.getName() + " is missing");
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 442 */         clazz = clazz.getSuperclass();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 447 */     Collections.sort(attribs.serializedAttributes);
/* 448 */     serializedAttributes.put(c.getName(), attribs);
/*     */ 
/* 451 */     Class[] innerClzs = c.getClasses();
/* 452 */     Method[] methods = (fields = innerClzs).length; for (Set setters = 0; setters < methods; setters++) { Class innerClz = fields[setters];
/* 453 */       inspect(innerClz);
/*     */     }
/*     */ 
/* 456 */     return attribs;
/*     */   }
/*     */ 
/*     */   public static synchronized DynamicSerializationManager getManager(SerializationType type)
/*     */   {
/* 461 */     DynamicSerializationManager mgr = (DynamicSerializationManager)instanceMap.get(type);
/* 462 */     if (mgr == null) {
/* 463 */       mgr = new DynamicSerializationManager(type);
/* 464 */       instanceMap.put(type, mgr);
/*     */     }
/*     */ 
/* 467 */     return mgr;
/*     */   }
/*     */ 
/*     */   private DynamicSerializationManager(SerializationType type) {
/* 471 */     if (type == SerializationType.Thrift)
/* 472 */       this.builder = new ThriftSerializationContextBuilder();
/*     */   }
/*     */ 
/*     */   public SerializationMetadata getSerializationMetadata(String name)
/*     */   {
/* 486 */     SerializationMetadata sm = (SerializationMetadata)serializedAttributes.get(name);
/* 487 */     if (sm == null) {
/*     */       try {
/* 489 */         sm = inspect(Class.forName(name, true, getClass()
/* 490 */           .getClassLoader()));
/* 491 */         if (sm == null)
/* 492 */           serializedAttributes.put(name, NO_METADATA);
/*     */       }
/*     */       catch (ClassNotFoundException e) {
/* 495 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 499 */     if (sm == NO_METADATA) {
/* 500 */       return null;
/*     */     }
/* 502 */     return sm;
/*     */   }
/*     */ 
/*     */   public static enum EnclosureType
/*     */   {
/* 160 */     FIELD, COLLECTION; } 
/*     */   public static class SerializationMetadata { public List<String> serializedAttributes;
/*     */     public ISerializationTypeAdapter<?> serializationFactory;
/*     */     public Map<String, ISerializationTypeAdapter<?>> attributesWithFactories;
/*     */     public String adapterStructName; } 
/* 164 */   public static enum SerializationType { Thrift; }
/*     */ 
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.DynamicSerializationManager
 * JD-Core Version:    0.6.2
 */