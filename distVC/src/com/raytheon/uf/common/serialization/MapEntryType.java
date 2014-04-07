/*    */ package com.raytheon.uf.common.serialization;
/*    */ 
/*    */ import java.util.Map.Entry;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class MapEntryType<K, V>
/*    */ {
/*    */ 
/*    */   @XmlElement
/*    */   private K key;
/*    */ 
/*    */   @XmlElement
/*    */   private V value;
/*    */ 
/*    */   public MapEntryType()
/*    */   {
/*    */   }
/*    */ 
/*    */   public MapEntryType(Map.Entry<K, V> e)
/*    */   {
/* 58 */     this.key = e.getKey();
/* 59 */     this.value = e.getValue();
/*    */   }
/*    */ 
/*    */   public K getKey() {
/* 63 */     return this.key;
/*    */   }
/*    */ 
/*    */   public void setKey(K key) {
/* 67 */     this.key = key;
/*    */   }
/*    */ 
/*    */   public V getValue() {
/* 71 */     return this.value;
/*    */   }
/*    */ 
/*    */   public void setValue(V value) {
/* 75 */     this.value = value;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.MapEntryType
 * JD-Core Version:    0.6.2
 */