/*    */ package com.raytheon.uf.common.serialization.adapters;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*    */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*    */ import com.raytheon.uf.common.serialization.SerializationException;
/*    */ import java.util.EnumSet;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class EnumSetAdapter
/*    */   implements ISerializationTypeAdapter<EnumSet>
/*    */ {
/*    */   public EnumSet deserialize(IDeserializationContext deserializer)
/*    */     throws SerializationException
/*    */   {
/* 53 */     int setSize = deserializer.readI32();
/* 54 */     EnumSet rval = null;
/*    */ 
/* 56 */     if (setSize > 0) {
/* 57 */       String enumClassName = deserializer.readString();
/*    */       try
/*    */       {
/* 60 */         Class baseClass = Class.forName(enumClassName);
/* 61 */         if (baseClass.isEnum()) {
/* 62 */           Enum firstEnum = Enum.valueOf(baseClass, deserializer
/* 63 */             .readString());
/* 64 */           rval = EnumSet.of(firstEnum);
/* 65 */           for (int i = 1; i < setSize; i++)
/* 66 */             rval.add(Enum.valueOf(baseClass, deserializer
/* 67 */               .readString()));
/*    */         }
/*    */         else {
/* 70 */           throw new SerializationException(
/* 71 */             "Cannot deserialize EnumSet.  Class [" + 
/* 72 */             enumClassName + "] is not an enum");
/*    */         }
/*    */       } catch (ClassNotFoundException e) {
/* 75 */         throw new SerializationException("Unable to find enum class [" + 
/* 76 */           enumClassName + "]", e);
/*    */       }
/*    */     }
/*    */ 
/* 80 */     return rval;
/*    */   }
/*    */ 
/*    */   public void serialize(ISerializationContext serializer, EnumSet set)
/*    */     throws SerializationException
/*    */   {
/* 86 */     int setSize = set.size();
/* 87 */     serializer.writeI32(set.size());
/* 88 */     if (setSize > 0) {
/* 89 */       Iterator iter = set.iterator();
/* 90 */       Enum firstEnum = (Enum)iter.next();
/* 91 */       serializer.writeString(firstEnum.getClass().getName());
/* 92 */       serializer.writeString(firstEnum.name());
/*    */ 
/* 94 */       while (iter.hasNext())
/* 95 */         serializer.writeString(((Enum)iter.next()).name());
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.adapters.EnumSetAdapter
 * JD-Core Version:    0.6.2
 */