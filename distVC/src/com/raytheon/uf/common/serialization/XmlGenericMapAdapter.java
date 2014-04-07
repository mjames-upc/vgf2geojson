/*    */ package com.raytheon.uf.common.serialization;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ public class XmlGenericMapAdapter<K, V> extends XmlAdapter<MapType<K, V>, Map<K, V>>
/*    */ {
/*    */   public Map<K, V> unmarshal(MapType<K, V> serialized)
/*    */     throws Exception
/*    */   {
/* 49 */     HashMap map = new HashMap();
/*    */ 
/* 51 */     for (MapEntryType mapEntryType : serialized.getEntry()) {
/* 52 */       map.put(mapEntryType.getKey(), mapEntryType.getValue());
/*    */     }
/* 54 */     return map;
/*    */   }
/*    */ 
/*    */   public MapType<K, V> marshal(Map<K, V> unserialized) throws Exception
/*    */   {
/* 59 */     if (unserialized == null) {
/* 60 */       return null;
/*    */     }
/*    */ 
/* 63 */     MapType mapType = new MapType();
/*    */ 
/* 65 */     for (Map.Entry entry : unserialized.entrySet()) {
/* 66 */       MapEntryType mapEntryType = new MapEntryType();
/* 67 */       mapEntryType.setKey(entry.getKey());
/* 68 */       mapEntryType.setValue(entry.getValue());
/* 69 */       mapType.getEntry().add(mapEntryType);
/*    */     }
/* 71 */     return mapType;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.XmlGenericMapAdapter
 * JD-Core Version:    0.6.2
 */