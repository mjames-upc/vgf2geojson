/*     */ package com.raytheon.uf.common.serialization;
/*     */ 
/*     */ import com.raytheon.uf.common.status.IUFStatusHandler;
/*     */ import com.raytheon.uf.common.status.UFStatus;
/*     */ import com.raytheon.uf.common.status.UFStatus.Priority;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ 
/*     */ public class JAXBManager
/*     */ {
/*     */   private static final int QUEUE_SIZE = 10;
/*     */   private final JAXBContext jaxbContext;
/* 109 */   private final Queue<Unmarshaller> unmarshallers = new ConcurrentLinkedQueue();
/*     */ 
/* 111 */   private final Queue<Marshaller> marshallers = new ConcurrentLinkedQueue();
/*     */ 
/*     */   public JAXBManager(Class<?>[] clazz) throws JAXBException {
/* 114 */     this.jaxbContext = JAXBContext.newInstance(clazz);
/*     */   }
/*     */ 
/*     */   public JAXBContext getJaxbContext() throws JAXBException {
/* 118 */     return this.jaxbContext;
/*     */   }
/*     */ 
/*     */   private Unmarshaller getUnmarshaller() throws JAXBException {
/* 122 */     Unmarshaller m = (Unmarshaller)this.unmarshallers.poll();
/* 123 */     if (m == null) {
/* 124 */       m = getJaxbContext().createUnmarshaller();
/*     */ 
/* 126 */       m.setEventHandler(new MaintainEventsValidationHandler(null));
/*     */     }
/*     */     else
/*     */     {
/* 131 */       ValidationEventHandler h = m.getEventHandler();
/* 132 */       if ((h instanceof MaintainEventsValidationHandler)) {
/* 133 */         MaintainEventsValidationHandler sh = (MaintainEventsValidationHandler)h;
/* 134 */         sh.clearEvents();
/*     */       }
/*     */     }
/*     */ 
/* 138 */     return m;
/*     */   }
/*     */ 
/*     */   private Marshaller getMarshaller() throws JAXBException {
/* 142 */     Marshaller m = (Marshaller)this.marshallers.poll();
/* 143 */     if (m == null) {
/* 144 */       m = getJaxbContext().createMarshaller();
/*     */     }
/*     */ 
/* 147 */     return m;
/*     */   }
/*     */ 
/*     */   public Object unmarshalFromXml(String xml)
/*     */     throws JAXBException
/*     */   {
/* 160 */     Unmarshaller msh = null;
/*     */     try {
/* 162 */       msh = getUnmarshaller();
/* 163 */       StringReader reader = new StringReader(xml);
/* 164 */       Object obj = msh.unmarshal(reader);
/* 165 */       return obj;
/*     */     } finally {
/* 167 */       handleEvents(msh, null);
/* 168 */       if ((msh != null) && (this.unmarshallers.size() < 10))
/* 169 */         this.unmarshallers.add(msh);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void handleEvents(Unmarshaller msh, String name)
/*     */   {
/*     */     try
/*     */     {
/* 180 */       ValidationEventHandler h = msh.getEventHandler();
/* 181 */       if ((h instanceof MaintainEventsValidationHandler)) {
/* 182 */         boolean allInfo = true;
/* 183 */         MaintainEventsValidationHandler mh = (MaintainEventsValidationHandler)h;
/* 184 */         for (ValidationEvent event : mh.getEvents()) {
/* 185 */           if (event.getSeverity() == 2)
/*     */           {
/* 189 */             allInfo = false;
/* 190 */             break;
/*     */           }
/*     */         }
/* 193 */         for (ValidationEvent event : mh.getEvents()) {
/* 194 */           UFStatus.Priority p = UFStatus.Priority.INFO;
/* 195 */           if (!allInfo) {
/* 196 */             switch (event.getSeverity()) {
/*     */             case 2:
/* 198 */               p = UFStatus.Priority.SIGNIFICANT;
/* 199 */               break;
/*     */             case 1:
/* 201 */               p = UFStatus.Priority.PROBLEM;
/* 202 */               break;
/*     */             case 0:
/* 204 */               p = UFStatus.Priority.WARN;
/*     */             }
/*     */           }
/*     */ 
/* 208 */           UFStatus.getHandler().handle(
/* 209 */             p, 
/* 210 */             (name != null ? name : "") + ": " + 
/* 211 */             event.getMessage() + " on line " + 
/* 212 */             event.getLocator().getLineNumber() + 
/* 213 */             " column " + 
/* 214 */             event.getLocator().getColumnNumber(), 
/* 215 */             event.getLinkedException());
/*     */         }
/* 217 */         mh.clearEvents();
/*     */       }
/*     */     }
/*     */     catch (JAXBException localJAXBException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public String marshalToXml(Object obj)
/*     */     throws JAXBException
/*     */   {
/* 234 */     return marshalToXml(obj, true);
/*     */   }
/*     */ 
/*     */   public String marshalToXml(Object obj, boolean formatedOutput)
/*     */     throws JAXBException
/*     */   {
/* 250 */     Marshaller msh = getMarshaller();
/*     */     try {
/* 252 */       StringWriter writer = new StringWriter();
/* 253 */       msh.setProperty("jaxb.formatted.output", new Boolean(
/* 254 */         formatedOutput));
/* 255 */       msh.marshal(obj, writer);
/* 256 */       return writer.toString();
/*     */     } finally {
/* 258 */       if ((msh != null) && (this.marshallers.size() < 10))
/* 259 */         this.marshallers.add(msh);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void jaxbMarshalToXmlFile(Object obj, String filePath)
/*     */     throws SerializationException
/*     */   {
/* 276 */     jaxbMarshalToXmlFile(obj, filePath, true);
/*     */   }
/*     */ 
/*     */   public void jaxbMarshalToXmlFile(Object obj, String filePath, boolean formattedOutput)
/*     */     throws SerializationException
/*     */   {
/*     */     try
/*     */     {
/* 294 */       jaxbMarshalToStream(obj, new FileOutputStream(new File(filePath)), 
/* 295 */         formattedOutput);
/*     */     } catch (SerializationException e) {
/* 297 */       throw e;
/*     */     } catch (Exception e) {
/* 299 */       throw new SerializationException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void jaxbMarshalToStream(Object obj, OutputStream out)
/*     */     throws SerializationException
/*     */   {
/* 313 */     jaxbMarshalToStream(obj, out, true);
/*     */   }
/*     */ 
/*     */   public void jaxbMarshalToStream(Object obj, OutputStream out, boolean formattedOutput)
/*     */     throws SerializationException
/*     */   {
/* 328 */     Marshaller msh = null;
/*     */     try {
/* 330 */       msh = getMarshaller();
/* 331 */       msh.setProperty("jaxb.formatted.output", new Boolean(
/* 332 */         formattedOutput));
/* 333 */       msh.marshal(obj, out);
/*     */     } catch (Exception e) {
/* 335 */       throw new SerializationException(e);
/*     */     } finally {
/* 337 */       if ((msh != null) && (this.marshallers.size() < 10)) {
/* 338 */         this.marshallers.add(msh);
/*     */       }
/* 340 */       if (out != null)
/*     */         try {
/* 342 */           out.close();
/*     */         }
/*     */         catch (IOException localIOException)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object jaxbUnmarshalFromXmlFile(String filePath)
/*     */     throws SerializationException
/*     */   {
/* 360 */     return jaxbUnmarshalFromXmlFile(new File(filePath));
/*     */   }
/*     */ 
/*     */   public Object jaxbUnmarshalFromXmlFile(File file)
/*     */     throws SerializationException
/*     */   {
/* 373 */     FileReader reader = null;
/* 374 */     Unmarshaller msh = null;
/*     */     try {
/* 376 */       msh = getUnmarshaller();
/* 377 */       reader = new FileReader(file);
/* 378 */       Object obj = msh.unmarshal(reader);
/* 379 */       return obj;
/*     */     } catch (Exception e) {
/* 381 */       throw new SerializationException(e.getLocalizedMessage(), e);
/*     */     } finally {
/* 383 */       if (msh != null) {
/* 384 */         handleEvents(msh, file.getName());
/*     */       }
/* 386 */       if ((msh != null) && (this.unmarshallers.size() < 10)) {
/* 387 */         this.unmarshallers.add(msh);
/*     */       }
/* 389 */       if (reader != null)
/*     */         try {
/* 391 */           reader.close();
/*     */         }
/*     */         catch (IOException localIOException1)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object jaxbUnmarshalFromInputStream(InputStream is)
/*     */     throws SerializationException
/*     */   {
/* 410 */     Unmarshaller msh = null;
/*     */     try {
/* 412 */       msh = getUnmarshaller();
/* 413 */       Object obj = msh.unmarshal(is);
/* 414 */       return obj;
/*     */     } catch (Exception e) {
/* 416 */       throw new SerializationException(e.getLocalizedMessage(), e);
/*     */     } finally {
/* 418 */       if (msh != null) {
/* 419 */         handleEvents(msh, null);
/*     */       }
/* 421 */       if ((msh != null) && (this.unmarshallers.size() < 10)) {
/* 422 */         this.unmarshallers.add(msh);
/*     */       }
/* 424 */       if (is != null)
/*     */         try {
/* 426 */           is.close();
/*     */         }
/*     */         catch (IOException localIOException1)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MaintainEventsValidationHandler
/*     */     implements ValidationEventHandler
/*     */   {
/*  85 */     private final ArrayList<ValidationEvent> events = new ArrayList(
/*  86 */       0);
/*     */ 
/*     */     public boolean handleEvent(ValidationEvent event)
/*     */     {
/*  90 */       this.events.add(event);
/*  91 */       return true;
/*     */     }
/*     */ 
/*     */     public ArrayList<ValidationEvent> getEvents() {
/*  95 */       synchronized (this.events) {
/*  96 */         return new ArrayList(this.events);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void clearEvents() {
/* 101 */       synchronized (this.events) {
/* 102 */         this.events.clear();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.JAXBManager
 * JD-Core Version:    0.6.2
 */