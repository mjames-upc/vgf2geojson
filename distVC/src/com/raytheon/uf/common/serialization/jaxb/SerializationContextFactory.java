/*    */ package com.raytheon.uf.common.serialization.jaxb;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.annotation.RuntimeInlineAnnotationReader;
/*    */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*    */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.JAXBContextBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.xml.bind.JAXBContext;
/*    */ import javax.xml.bind.JAXBException;
/*    */ 
/*    */ public class SerializationContextFactory
/*    */ {
/*    */   public static JAXBContext createContext(Class[] classes, Map props)
/*    */   {
/* 59 */     System.setProperty(JAXBContextImpl.class.getName() + ".fastBoot", 
/* 60 */       "true");
/* 61 */     JAXBContextImpl.JAXBContextBuilder builder = new JAXBContextImpl.JAXBContextBuilder();
/* 62 */     builder.setAnnotationReader(new RuntimeInlineAnnotationReader());
/* 63 */     builder.setClasses(classes);
/* 64 */     builder.setSubclassReplacements(new HashMap());
/* 65 */     builder.setDefaultNsUri("");
/* 66 */     builder.setTypeRefs(new ArrayList());
/*    */     try
/*    */     {
/* 71 */       return new CustomJAXBContext(new JAXBContextImpl(builder));
/*    */     } catch (JAXBException e) {
/* 73 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.jaxb.SerializationContextFactory
 * JD-Core Version:    0.6.2
 */