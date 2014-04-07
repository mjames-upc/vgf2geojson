/*     */ package com.raytheon.uf.common.serialization;
/*     */ 
/*     */ import com.raytheon.uf.common.util.ServiceLoaderUtil;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ 
/*     */ public final class SerializationUtil
/*     */ {
/*     */   private static volatile JAXBManager jaxbManager;
/*     */ 
/*     */   public static JAXBManager getJaxbManager()
/*     */     throws JAXBException
/*     */   {
/*  75 */     JAXBManager result = jaxbManager;
/*  76 */     if (result == null) {
/*  77 */       synchronized (SerializationUtil.class) {
/*  78 */         result = jaxbManager;
/*  79 */         if (result == null)
/*     */         {
/*  83 */           List jaxbClasses = 
/*  84 */             ((IJaxbableClassesLocator)ServiceLoaderUtil.load(SerializationUtil.class, 
/*  85 */             IJaxbableClassesLocator.class, 
/*  86 */             SerializableManager.getInstance()))
/*  87 */             .getJaxbables();
/*  88 */           jaxbManager = result = new JAXBManager(
/*  89 */             (Class[])jaxbClasses.toArray(new Class[jaxbClasses.size()]));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  94 */     return result;
/*     */   }
/*     */ 
/*     */   public static JAXBContext getJaxbContext() throws JAXBException {
/*  98 */     return getJaxbManager().getJaxbContext();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static Object unmarshalFromXml(String xml)
/*     */     throws JAXBException
/*     */   {
/* 115 */     return unmarshalFromXml(Object.class, xml);
/*     */   }
/*     */ 
/*     */   public static <T> T unmarshalFromXml(Class<T> clazz, String xml)
/*     */     throws JAXBException
/*     */   {
/* 132 */     return clazz.cast(getJaxbManager().unmarshalFromXml(xml));
/*     */   }
/*     */ 
/*     */   public static String marshalToXml(Object obj)
/*     */     throws JAXBException
/*     */   {
/* 145 */     return getJaxbManager().marshalToXml(obj);
/*     */   }
/*     */ 
/*     */   public static void jaxbMarshalToXmlFile(Object obj, String filePath)
/*     */     throws SerializationException
/*     */   {
/*     */     try
/*     */     {
/* 161 */       getJaxbManager().jaxbMarshalToXmlFile(obj, filePath);
/*     */     } catch (JAXBException e) {
/* 163 */       throw new SerializationException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static Object jaxbUnmarshalFromXmlFile(String filePath)
/*     */     throws SerializationException
/*     */   {
/* 182 */     return jaxbUnmarshalFromXmlFile(Object.class, filePath);
/*     */   }
/*     */ 
/*     */   public static <T> T jaxbUnmarshalFromXmlFile(Class<T> clazz, String filePath)
/*     */     throws SerializationException
/*     */   {
/*     */     try
/*     */     {
/* 199 */       return clazz.cast(getJaxbManager().jaxbUnmarshalFromXmlFile(
/* 200 */         filePath));
/*     */     } catch (JAXBException e) {
/* 202 */       throw new SerializationException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static Object jaxbUnmarshalFromXmlFile(File file)
/*     */     throws SerializationException
/*     */   {
/* 220 */     return jaxbUnmarshalFromXmlFile(Object.class, file);
/*     */   }
/*     */ 
/*     */   public static <T> T jaxbUnmarshalFromXmlFile(Class<T> clazz, File file)
/*     */     throws SerializationException
/*     */   {
/*     */     try
/*     */     {
/* 238 */       return clazz.cast(getJaxbManager().jaxbUnmarshalFromXmlFile(file));
/*     */     } catch (Exception e) {
/* 240 */       throw new SerializationException(e.getLocalizedMessage(), e);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static Object jaxbUnmarshalFromInputStream(InputStream is)
/*     */     throws SerializationException
/*     */   {
/* 259 */     return jaxbUnmarshalFromInputStream(Object.class, is);
/*     */   }
/*     */ 
/*     */   public static <T> T jaxbUnmarshalFromInputStream(Class<T> clazz, InputStream is)
/*     */     throws SerializationException
/*     */   {
/*     */     try
/*     */     {
/* 277 */       return clazz
/* 278 */         .cast(getJaxbManager().jaxbUnmarshalFromInputStream(is));
/*     */     } catch (Exception e) {
/* 280 */       throw new SerializationException(e.getLocalizedMessage(), e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static byte[] transformToThrift(Object obj)
/*     */     throws SerializationException
/*     */   {
/* 297 */     DynamicSerializationManager dsm = 
/* 298 */       DynamicSerializationManager.getManager(DynamicSerializationManager.SerializationType.Thrift);
/* 299 */     return dsm.serialize(obj);
/*     */   }
/*     */ 
/*     */   public static void transformToThriftUsingStream(Object obj, OutputStream os)
/*     */     throws SerializationException
/*     */   {
/* 317 */     DynamicSerializationManager dsm = 
/* 318 */       DynamicSerializationManager.getManager(DynamicSerializationManager.SerializationType.Thrift);
/* 319 */     dsm.serialize(obj, os);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static Object transformFromThrift(byte[] bytes)
/*     */     throws SerializationException
/*     */   {
/* 337 */     return transformFromThrift(Object.class, bytes);
/*     */   }
/*     */ 
/*     */   public static <T> T transformFromThrift(Class<T> clazz, byte[] bytes)
/*     */     throws SerializationException
/*     */   {
/* 352 */     DynamicSerializationManager dsm = 
/* 353 */       DynamicSerializationManager.getManager(DynamicSerializationManager.SerializationType.Thrift);
/* 354 */     ByteArrayInputStream bais = null;
/*     */     try {
/* 356 */       bais = new ByteArrayInputStream(bytes);
/* 357 */       return clazz.cast(dsm.deserialize(bais));
/*     */     } catch (ClassCastException cce) {
/* 359 */       throw new SerializationException(cce);
/*     */     } finally {
/* 361 */       if (bais != null)
/*     */         try {
/* 363 */           bais.close();
/*     */         }
/*     */         catch (IOException localIOException1)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static <T> T transformFromThrift(Class<T> clazz, InputStream is)
/*     */     throws SerializationException
/*     */   {
/* 383 */     DynamicSerializationManager dsm = 
/* 384 */       DynamicSerializationManager.getManager(DynamicSerializationManager.SerializationType.Thrift);
/*     */     try {
/* 386 */       return clazz.cast(dsm.deserialize(is));
/*     */     } catch (ClassCastException cce) {
/* 388 */       throw new SerializationException(cce);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.SerializationUtil
 * JD-Core Version:    0.6.2
 */