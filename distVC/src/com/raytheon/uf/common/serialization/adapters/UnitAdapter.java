/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ import java.text.ParseException;
/*    */ import javax.measure.unit.Unit;
/*    */ import javax.measure.unit.UnitFormat;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ public class UnitAdapter extends XmlAdapter<String, Unit<?>>
/*    */   implements ISerializationTypeAdapter<Unit<?>>
/*    */ {
/*    */   public String marshal(Unit<?> v)
/*    */     throws Exception
/*    */   {
/* 53 */     if (v == null) {
/* 54 */       return "";
/*    */     }
/* 56 */     return v.toString();
/*    */   }
/*    */ 
/*    */   public Unit<?> unmarshal(String unit)
/*    */     throws Exception
/*    */   {
/* 62 */     Unit retVal = Unit.ONE;
/*    */ 
/* 64 */     if ((unit != null) && 
/* 65 */       (!unit.equals(""))) {
/* 66 */       retVal = (Unit)UnitFormat.getUCUMInstance().parseObject(
/* 67 */         unit);
/*    */     }
/*    */ 
/* 70 */     return retVal;
/*    */   }
/*    */ 
/*    */   public Unit<?> deserialize(IDeserializationContext deserializer)
/*    */     throws SerializationException
/*    */   {
/* 76 */     Unit retVal = Unit.ONE;
/* 77 */     String str = deserializer.readString();
/* 78 */     if ((str != null) && 
/* 79 */       (!str.equals(""))) {
/*    */       try {
/* 81 */         retVal = (Unit)UnitFormat.getUCUMInstance()
/* 82 */           .parseObject(str);
/*    */       } catch (ParseException e) {
/* 84 */         throw new SerializationException(
/* 85 */           "Error parsing unit from string " + str, e);
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 90 */     return retVal;
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, Unit<?> object)
/*    */     throws SerializationException
/*    */   {
/* 96 */     serializer.writeString(object.toString());
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.UnitAdapter
 * JD-Core Version:    0.6.2
 */