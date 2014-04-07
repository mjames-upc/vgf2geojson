/*     */ package com.raytheon.uf.common.serialization;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.TimeZone;
/*     */ import javax.xml.datatype.DatatypeConfigurationException;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.Duration;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class BuiltInTypeSupport
/*     */ {
/*     */   public static class BigDecimalSerializer
/*     */     implements ISerializationTypeAdapter<BigDecimal>
/*     */   {
/*     */     public BigDecimal deserialize(IDeserializationContext deserializer)
/*     */       throws SerializationException
/*     */     {
/* 219 */       return new BigDecimal(deserializer.readDouble());
/*     */     }
/*     */ 
/*     */     public void serialize(ISerializationContext serializer, BigDecimal object)
/*     */       throws SerializationException
/*     */     {
/* 233 */       serializer.writeDouble(object.doubleValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class BigIntegerSerializer
/*     */     implements ISerializationTypeAdapter<BigInteger>
/*     */   {
/*     */     public BigInteger deserialize(IDeserializationContext deserializer)
/*     */       throws SerializationException
/*     */     {
/* 251 */       return new BigInteger(deserializer.readBinary());
/*     */     }
/*     */ 
/*     */     public void serialize(ISerializationContext serializer, BigInteger object)
/*     */       throws SerializationException
/*     */     {
/* 265 */       serializer.writeBinary(object.toByteArray());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CalendarSerializer
/*     */     implements ISerializationTypeAdapter<Calendar>
/*     */   {
/*     */     public Calendar deserialize(IDeserializationContext arg0)
/*     */       throws SerializationException
/*     */     {
/* 130 */       long t = arg0.readI64();
/* 131 */       Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 132 */       c.setTimeInMillis(t);
/* 133 */       return c;
/*     */     }
/*     */ 
/*     */     public void serialize(ISerializationContext arg0, Calendar arg1)
/*     */       throws SerializationException
/*     */     {
/* 140 */       long t = arg1.getTime().getTime();
/* 141 */       arg0.writeI64(t);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DateSerializer
/*     */     implements ISerializationTypeAdapter<java.util.Date>
/*     */   {
/*     */     public java.util.Date deserialize(IDeserializationContext deserializer)
/*     */       throws SerializationException
/*     */     {
/*  73 */       long t = deserializer.readI64();
/*  74 */       return new java.util.Date(t);
/*     */     }
/*     */ 
/*     */     public void serialize(ISerializationContext serializer, java.util.Date object)
/*     */       throws SerializationException
/*     */     {
/*  80 */       serializer.writeI64(object.getTime());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DurationSerializer
/*     */     implements ISerializationTypeAdapter<Duration>
/*     */   {
/*     */     public Duration deserialize(IDeserializationContext arg0)
/*     */       throws SerializationException
/*     */     {
/*     */       try
/*     */       {
/* 290 */         String durationStr = arg0.readString();
/* 291 */         return DatatypeFactory.newInstance().newDuration(durationStr);
/*     */       } catch (DatatypeConfigurationException e) {
/* 293 */         throw new SerializationException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void serialize(ISerializationContext arg0, Duration arg1)
/*     */       throws SerializationException
/*     */     {
/* 300 */       arg0.writeString(arg1.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class QNameSerializer
/*     */     implements ISerializationTypeAdapter<QName>
/*     */   {
/*     */     public QName deserialize(IDeserializationContext arg0)
/*     */       throws SerializationException
/*     */     {
/* 324 */       return QName.valueOf(arg0.readString());
/*     */     }
/*     */ 
/*     */     public void serialize(ISerializationContext arg0, QName arg1)
/*     */       throws SerializationException
/*     */     {
/* 330 */       arg0.writeString(arg1.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SqlDateSerializer
/*     */     implements ISerializationTypeAdapter<java.sql.Date>
/*     */   {
/*     */     public java.sql.Date deserialize(IDeserializationContext deserializer)
/*     */       throws SerializationException
/*     */     {
/* 190 */       long t = deserializer.readI64();
/* 191 */       return new java.sql.Date(t);
/*     */     }
/*     */ 
/*     */     public void serialize(ISerializationContext serializer, java.sql.Date object)
/*     */       throws SerializationException
/*     */     {
/* 197 */       serializer.writeI64(object.getTime());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ThrowableSerializer
/*     */     implements ISerializationTypeAdapter<Throwable>
/*     */   {
/*     */     public void serialize(ISerializationContext serializer, Throwable object)
/*     */       throws SerializationException
/*     */     {
/* 348 */       serializer.writeObject(ExceptionWrapper.wrapThrowable(object));
/*     */     }
/*     */ 
/*     */     public Throwable deserialize(IDeserializationContext deserializer)
/*     */       throws SerializationException
/*     */     {
/* 354 */       return 
/* 355 */         ExceptionWrapper.unwrapThrowable(
/* 356 */         (SerializableExceptionWrapper)SerializableExceptionWrapper.class
/* 356 */         .cast(deserializer.readObject()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class TimestampSerializer
/*     */     implements ISerializationTypeAdapter<Timestamp>
/*     */   {
/*     */     public Timestamp deserialize(IDeserializationContext deserializer)
/*     */       throws SerializationException
/*     */     {
/*  98 */       long t = deserializer.readI64();
/*  99 */       return new Timestamp(t);
/*     */     }
/*     */ 
/*     */     public void serialize(ISerializationContext serializer, Timestamp object)
/*     */       throws SerializationException
/*     */     {
/* 105 */       serializer.writeI64(object.getTime());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class XMLGregorianCalendarSerializer
/*     */     implements ISerializationTypeAdapter<XMLGregorianCalendar>
/*     */   {
/*     */     public XMLGregorianCalendar deserialize(IDeserializationContext arg0)
/*     */       throws SerializationException
/*     */     {
/*     */       try
/*     */       {
/* 166 */         long t = arg0.readI64();
/* 167 */         GregorianCalendar c = new GregorianCalendar(
/* 168 */           TimeZone.getTimeZone("GMT"));
/* 169 */         c.setTimeInMillis(t);
/* 170 */         return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
/*     */       } catch (DatatypeConfigurationException e) {
/* 172 */         throw new SerializationException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void serialize(ISerializationContext arg0, XMLGregorianCalendar arg1)
/*     */       throws SerializationException
/*     */     {
/* 179 */       long t = arg1.toGregorianCalendar().getTimeInMillis();
/* 180 */       arg0.writeI64(t);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.BuiltInTypeSupport
 * JD-Core Version:    0.6.2
 */