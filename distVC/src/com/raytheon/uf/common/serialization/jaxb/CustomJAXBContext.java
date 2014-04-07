/*    */ package com.raytheon.uf.common.serialization.jaxb;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*    */ import javax.xml.bind.JAXBContext;
/*    */ import javax.xml.bind.JAXBException;
/*    */ import javax.xml.bind.Marshaller;
/*    */ import javax.xml.bind.Unmarshaller;
/*    */ import javax.xml.bind.Validator;
/*    */ 
/*    */ public class CustomJAXBContext extends JAXBContext
/*    */ {
/*    */   private JAXBContextImpl delegate;
/*    */ 
/*    */   public CustomJAXBContext(JAXBContextImpl delegate)
/*    */   {
/* 56 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */   public Marshaller createMarshaller()
/*    */     throws JAXBException
/*    */   {
/* 66 */     return this.delegate.createMarshaller();
/*    */   }
/*    */ 
/*    */   public Unmarshaller createUnmarshaller()
/*    */     throws JAXBException
/*    */   {
/* 76 */     return new CustomJAXBUnmarshaller(this.delegate.createUnmarshaller());
/*    */   }
/*    */ 
/*    */   public Validator createValidator()
/*    */     throws JAXBException
/*    */   {
/* 86 */     return this.delegate.createValidator();
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.jaxb.CustomJAXBContext
 * JD-Core Version:    0.6.2
 */