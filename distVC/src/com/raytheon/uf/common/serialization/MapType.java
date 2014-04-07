/*    */ package com.raytheon.uf.common.serialization;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public class MapType<K, V>
/*    */ {
/* 44 */   private List<MapEntryType<K, V>> entry = new ArrayList();
/*    */ 
/*    */   public MapType() {
/*    */   }
/*    */ 
/*    */   public MapType(Map<K, V> map) {
/* 50 */     for (Map.Entry mapEntry : map.entrySet())
/* 51 */       this.entry.add(new MapEntryType(mapEntry));
/*    */   }
/*    */ 
/*    */   public List<MapEntryType<K, V>> getEntry()
/*    */   {
/* 56 */     return this.entry;
/*    */   }
/*    */ 
/*    */   public void setEntry(List<MapEntryType<K, V>> entry) {
/* 60 */     this.entry = entry;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.MapType
 * JD-Core Version:    0.6.2
 */