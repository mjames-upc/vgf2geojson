/*     */ package com.raytheon.uf.common.serialization.jaxb;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.InterningXmlVisitor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.SAXConnector;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor;
/*     */ import java.io.IOException;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.UnmarshallerHandler;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.helpers.AbstractUnmarshallerImpl;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class CustomJAXBUnmarshaller extends AbstractUnmarshallerImpl
/*     */ {
/*  64 */   private static final DefaultHandler dummyHandler = new DefaultHandler();
/*     */   private UnmarshallerImpl delegate;
/*     */   private UnmarshallingContext coordinator;
/*     */ 
/*     */   public CustomJAXBUnmarshaller(UnmarshallerImpl delegate)
/*     */   {
/*  74 */     this.delegate = delegate;
/*  75 */     this.coordinator = new UnmarshallingContext(delegate, null);
/*     */   }
/*     */ 
/*     */   public void setEventHandler(ValidationEventHandler handler)
/*     */     throws JAXBException
/*     */   {
/*  88 */     this.delegate.setEventHandler(handler);
/*     */   }
/*     */ 
/*     */   public ValidationEventHandler getEventHandler()
/*     */     throws JAXBException
/*     */   {
/*  98 */     return this.delegate.getEventHandler();
/*     */   }
/*     */ 
/*     */   public UnmarshallerHandler getUnmarshallerHandler()
/*     */   {
/* 108 */     return this.delegate.getUnmarshallerHandler();
/*     */   }
/*     */ 
/*     */   public Object unmarshal(Node arg0)
/*     */     throws JAXBException
/*     */   {
/* 118 */     return this.delegate.unmarshal(arg0);
/*     */   }
/*     */ 
/*     */   protected Object unmarshal(XMLReader reader, InputSource source)
/*     */     throws JAXBException
/*     */   {
/* 131 */     CustomContentHandler connector = new CustomContentHandler(
/* 132 */       getUnmarshallerHandler(UnmarshallerImpl.needsInterning(reader), 
/* 133 */       null));
/*     */ 
/* 135 */     reader.setContentHandler(connector);
/* 136 */     reader.setErrorHandler(this.coordinator);
/*     */     try
/*     */     {
/* 139 */       reader.parse(source);
/*     */     } catch (IOException e) {
/* 141 */       throw new UnmarshalException(e);
/*     */     } catch (SAXException e) {
/* 143 */       throw createUnmarshalException(e);
/*     */     }
/*     */ 
/* 146 */     Object result = connector.getResult();
/*     */ 
/* 148 */     reader.setContentHandler(dummyHandler);
/* 149 */     reader.setErrorHandler(dummyHandler);
/*     */ 
/* 151 */     return result;
/*     */   }
/*     */ 
/*     */   private SAXConnector getUnmarshallerHandler(boolean intern, JaxBeanInfo expectedType)
/*     */   {
/* 156 */     XmlVisitor h = this.delegate.createUnmarshallerHandler(null, false, 
/* 157 */       expectedType);
/* 158 */     if (intern)
/* 159 */       h = new InterningXmlVisitor(h);
/* 160 */     return new SAXConnector(h, null);
/*     */   }
/*     */ 
/*     */   public Schema getSchema()
/*     */   {
/* 170 */     return this.delegate.getSchema();
/*     */   }
/*     */ 
/*     */   public void setSchema(Schema schema)
/*     */   {
/* 181 */     this.delegate.setSchema(schema);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.jaxb.CustomJAXBUnmarshaller
 * JD-Core Version:    0.6.2
 */