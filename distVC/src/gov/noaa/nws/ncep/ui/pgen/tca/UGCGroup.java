/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class UGCGroup
/*     */ {
/*     */   private static final String ZONE_NUM_FMT = "%03d-";
/*     */   private static final String DATE_TIME_FMT = "%02d%02d00-";
/*     */   private static final int MAX_LINE_LENGTH = 66;
/*     */   private static final char DASH = '-';
/*     */   private static final char NEW_LINE = '\n';
/*     */   private Calendar purgeTime;
/*     */   private TreeMap<String, TreeSet<Integer>> zoneGroups;
/*     */ 
/*     */   public UGCGroup()
/*     */   {
/*  41 */     this.zoneGroups = new TreeMap();
/*     */   }
/*     */ 
/*     */   public Calendar getPurgeTime()
/*     */   {
/*  48 */     return this.purgeTime;
/*     */   }
/*     */ 
/*     */   public void setPurgeTime(Calendar purgeTime)
/*     */   {
/*  55 */     this.purgeTime = purgeTime;
/*     */   }
/*     */ 
/*     */   public void addZone(String zone)
/*     */   {
/*  67 */     Zone z = new Zone(zone);
/*  68 */     String state = z.getState();
/*  69 */     Integer num = new Integer(z.getNumber());
/*     */ 
/*  74 */     if (this.zoneGroups.containsKey(state)) {
/*  75 */       ((TreeSet)this.zoneGroups.get(state)).add(num);
/*     */     }
/*     */     else
/*     */     {
/*  81 */       TreeSet newgroup = new TreeSet();
/*  82 */       newgroup.add(num);
/*  83 */       this.zoneGroups.put(state, newgroup);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addZones(List<String> zones)
/*     */   {
/*  95 */     for (String zone : zones)
/*  96 */       addZone(zone);
/*     */   }
/*     */ 
/*     */   public String createUGCString()
/*     */   {
/* 105 */     StringBuilder sb = new StringBuilder();
/*     */     Iterator localIterator2;
/* 107 */     for (Iterator localIterator1 = this.zoneGroups.keySet().iterator(); localIterator1.hasNext(); 
/* 109 */       localIterator2.hasNext())
/*     */     {
/* 107 */       String state = (String)localIterator1.next();
/* 108 */       sb.append(state + "Z");
/* 109 */       localIterator2 = ((TreeSet)this.zoneGroups.get(state)).iterator(); continue; Integer num = (Integer)localIterator2.next();
/* 110 */       sb.append(String.format("%03d-", new Object[] { Integer.valueOf(num.intValue()) }));
/*     */     }
/*     */ 
/* 113 */     sb.append(String.format("%02d%02d00-", new Object[] { Integer.valueOf(this.purgeTime.get(5)), 
/* 114 */       Integer.valueOf(this.purgeTime.get(11)) }));
/*     */ 
/* 120 */     int index = 65;
/* 121 */     while (index < sb.length()) {
/* 122 */       int idx = index;
/* 123 */       boolean done = false;
/*     */       do {
/* 125 */         idx--;
/* 126 */         if (sb.charAt(idx) == '-') {
/* 127 */           sb.insert(idx + 1, '\n');
/* 128 */           done = true;
/*     */         }
/*     */       }
/* 130 */       while (!done);
/* 131 */       index = idx + 66;
/*     */     }
/*     */ 
/* 134 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public List<String> getZones()
/*     */   {
/* 145 */     ArrayList lst = new ArrayList();
/*     */     Iterator localIterator2;
/* 147 */     for (Iterator localIterator1 = this.zoneGroups.keySet().iterator(); localIterator1.hasNext(); 
/* 148 */       localIterator2.hasNext())
/*     */     {
/* 147 */       String state = (String)localIterator1.next();
/* 148 */       localIterator2 = ((TreeSet)this.zoneGroups.get(state)).iterator(); continue; Integer num = (Integer)localIterator2.next();
/* 149 */       StringBuilder sb = new StringBuilder(state + "Z");
/* 150 */       sb.append(String.format("%03d", new Object[] { Integer.valueOf(num.intValue()) }));
/* 151 */       lst.add(sb.toString());
/*     */     }
/*     */ 
/* 155 */     return lst;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.UGCGroup
 * JD-Core Version:    0.6.2
 */