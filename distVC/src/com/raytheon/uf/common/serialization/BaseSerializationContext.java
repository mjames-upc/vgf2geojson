/*    */ package com.raytheon.uf.common.serialization;
/*    */ 
/*    */ public abstract class BaseSerializationContext
/*    */   implements ISerializationContext, IDeserializationContext
/*    */ {
/*    */   protected final DynamicSerializationManager serializationManager;
/*    */ 
/*    */   public BaseSerializationContext(DynamicSerializationManager serializationManager)
/*    */   {
/* 52 */     this.serializationManager = serializationManager;
/*    */   }
/*    */ 
/*    */   public Object readObject()
/*    */     throws SerializationException
/*    */   {
/* 60 */     return this.serializationManager.deserialize(this);
/*    */   }
/*    */ 
/*    */   public void writeObject(Object obj)
/*    */     throws SerializationException
/*    */   {
/* 70 */     this.serializationManager.serialize(this, obj);
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.BaseSerializationContext
 * JD-Core Version:    0.6.2
 */