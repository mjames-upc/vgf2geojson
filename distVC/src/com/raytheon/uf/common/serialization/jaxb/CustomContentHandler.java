/*     */ package com.raytheon.uf.common.serialization.jaxb;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.SAXConnector;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class CustomContentHandler
/*     */   implements ContentHandler
/*     */ {
/*     */   private SAXConnector connector;
/*     */ 
/*     */   public CustomContentHandler(SAXConnector connector)
/*     */   {
/*  57 */     this.connector = connector;
/*     */   }
/*     */ 
/*     */   public final void characters(char[] arg0, int arg1, int arg2)
/*     */   {
/*  68 */     this.connector.characters(arg0, arg1, arg2);
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/*  76 */     this.connector.endDocument();
/*     */   }
/*     */ 
/*     */   public void endElement(String arg0, String arg1, String arg2)
/*     */     throws SAXException
/*     */   {
/*  89 */     this.connector.endElement(arg0, arg1, arg2);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String arg0)
/*     */     throws SAXException
/*     */   {
/*  98 */     this.connector.endPrefixMapping(arg0);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 107 */     return this.connector.equals(obj);
/*     */   }
/*     */ 
/*     */   public Object getResult()
/*     */     throws JAXBException, IllegalStateException
/*     */   {
/* 117 */     return this.connector.getResult();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 125 */     return this.connector.hashCode();
/*     */   }
/*     */ 
/*     */   public final void ignorableWhitespace(char[] arg0, int arg1, int arg2)
/*     */   {
/* 136 */     this.connector.ignorableWhitespace(arg0, arg1, arg2);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String arg0, String arg1)
/*     */   {
/* 146 */     this.connector.processingInstruction(arg0, arg1);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator arg0)
/*     */   {
/* 154 */     this.connector.setDocumentLocator(arg0);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String arg0)
/*     */   {
/* 162 */     this.connector.skippedEntity(arg0);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 170 */     this.connector.startDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(String arg0, String arg1, String arg2, Attributes arg3)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 185 */       this.connector.startElement(arg0, arg1, arg2, arg3);
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String arg0, String arg1)
/*     */     throws SAXException
/*     */   {
/* 201 */     this.connector.startPrefixMapping(arg0, arg1);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.jaxb.CustomContentHandler
 * JD-Core Version:    0.6.2
 */