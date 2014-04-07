/*     */ package com.raytheon.uf.common.serialization;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.sf.cglib.beans.BeanMap;
/*     */ import net.sf.cglib.beans.BeanMap.Generator;
/*     */ import net.sf.cglib.reflect.FastClass;
/*     */ 
/*     */ public class SerializationCache
/*     */ {
/*  46 */   private static Map<Class<?>, BeanMap.Generator> generators = new HashMap();
/*     */ 
/*  49 */   private static Map<Class<?>, BeanMap> beanMaps = new HashMap();
/*     */ 
/*  52 */   private static Map<String, FastClass> classCache = new HashMap();
/*     */ 
/*     */   public static void returnBeanMap(BeanMap beanMap, Object obj)
/*     */   {
/*  68 */     synchronized (beanMaps) {
/*  69 */       beanMap.setBean(null);
/*  70 */       beanMaps.put(obj.getClass(), beanMap);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static BeanMap getBeanMap(Object obj)
/*     */   {
/*  84 */     BeanMap bm = null;
/*  85 */     synchronized (beanMaps) {
/*  86 */       bm = (BeanMap)beanMaps.remove(obj.getClass());
/*     */     }
/*  88 */     if (bm != null) {
/*  89 */       bm.setBean(obj);
/*  90 */       return bm;
/*     */     }
/*     */ 
/*  93 */     BeanMap.Generator generator = null;
/*  94 */     synchronized (generators) {
/*  95 */       generator = (BeanMap.Generator)generators.remove(obj.getClass());
/*     */     }
/*  97 */     if (generator == null) {
/*  98 */       generator = new BeanMap.Generator();
/*     */     }
/*     */ 
/* 101 */     generator.setClassLoader(SerializationCache.class.getClassLoader());
/* 102 */     generator.setBean(obj);
/* 103 */     bm = generator.create();
/* 104 */     generator.setBean(null);
/*     */ 
/* 106 */     synchronized (generators) {
/* 107 */       generators.put(obj.getClass(), generator);
/*     */     }
/*     */ 
/* 110 */     return bm;
/*     */   }
/*     */ 
/*     */   public static FastClass getFastClass(String name)
/*     */     throws Exception
/*     */   {
/* 122 */     synchronized (classCache) {
/* 123 */       FastClass fc = (FastClass)classCache.get(name);
/* 124 */       if (fc == null) {
/* 125 */         fc = FastClass.create(
/* 126 */           SerializationCache.class.getClassLoader(), 
/* 127 */           Class.forName(name));
/* 128 */         classCache.put(name, fc);
/*     */       }
/* 130 */       return fc;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.SerializationCache
 * JD-Core Version:    0.6.2
 */