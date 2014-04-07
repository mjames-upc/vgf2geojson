/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ import com.raytheon.viz.texteditor.util.VtecObject;
/*     */ import com.raytheon.viz.texteditor.util.VtecUtil;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class TVtecObject extends VtecObject
/*     */   implements Comparable<TVtecObject>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String vtecCreateFormatTCV = "/%s.%s.%s.%s.%s.%04d.%s-000000T0000Z/";
/*     */   private static final String COMPARE_FORMAT = "%s.%s.%s";
/*  37 */   private static final HashMap<String, Integer> priority = new HashMap();
/*     */ 
/*     */   static
/*     */   {
/*  43 */     priority.put("NEW.HU.W", new Integer(0));
/*  44 */     priority.put("NEW.HU.A", new Integer(1));
/*  45 */     priority.put("NEW.TR.W", new Integer(2));
/*  46 */     priority.put("NEW.TR.A", new Integer(3));
/*  47 */     priority.put("CON.HU.W", new Integer(4));
/*  48 */     priority.put("CON.HU.A", new Integer(5));
/*  49 */     priority.put("CON.TR.W", new Integer(6));
/*  50 */     priority.put("CON.TR.A", new Integer(7));
/*  51 */     priority.put("CAN.HU.W", new Integer(8));
/*  52 */     priority.put("CAN.HU.A", new Integer(9));
/*  53 */     priority.put("CAN.TR.W", new Integer(10));
/*  54 */     priority.put("CAN.TR.A", new Integer(11));
/*     */   }
/*     */ 
/*     */   public TVtecObject()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TVtecObject(String vtec)
/*     */   {
/*  68 */     super(vtec);
/*     */   }
/*     */ 
/*     */   public TVtecObject(String product, String action, String office, String phenomenon, String significance, int sequence, Calendar startTime, Calendar endTime)
/*     */   {
/*  84 */     super(action, office, phenomenon, significance, sequence);
/*  85 */     setProduct(product);
/*  86 */     setStartTime(startTime);
/*  87 */     setEndTime(endTime);
/*     */   }
/*     */ 
/*     */   public TVtecObject(String office, String phenomenon, String significance, int sequence)
/*     */   {
/*  98 */     super(office, phenomenon, significance, sequence);
/*     */   }
/*     */ 
/*     */   public String getVtecString()
/*     */   {
/*     */     String vtecStr;
/*     */     String vtecStr;
/* 109 */     if (getEndTime() == null)
/* 110 */       vtecStr = String.format("/%s.%s.%s.%s.%s.%04d.%s-000000T0000Z/", new Object[] { getProduct(), getAction(), 
/* 111 */         getOffice(), getPhenomena(), getSignificance(), getSequence(), 
/* 112 */         VtecUtil.formatVtecTime(getStartTime()) });
/*     */     else {
/* 114 */       vtecStr = super.getVtecString();
/*     */     }
/* 116 */     return vtecStr;
/*     */   }
/*     */ 
/*     */   public int compareTo(TVtecObject o)
/*     */   {
/* 126 */     if (o == null) throw new NullPointerException("TVtecObject: Can't compare null");
/*     */ 
/* 128 */     int thisPriority = getPriority(this);
/* 129 */     int thatPriority = getPriority(o);
/*     */ 
/* 132 */     if (thisPriority < thatPriority) {
/* 133 */       return -1;
/*     */     }
/* 135 */     if (thisPriority > thatPriority) {
/* 136 */       return 1;
/*     */     }
/*     */ 
/* 139 */     return 0;
/*     */   }
/*     */ 
/*     */   private int getPriority(TVtecObject o)
/*     */   {
/* 149 */     String str = String.format("%s.%s.%s", new Object[] { o.getAction(), o.getPhenomena(), o.getSignificance() });
/*     */ 
/* 151 */     if (priority.containsKey(str)) {
/* 152 */       return ((Integer)priority.get(str)).intValue();
/*     */     }
/* 154 */     return 100;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 164 */     if (obj == null) throw new NullPointerException("TVtecObject: Can't compare null");
/*     */ 
/* 166 */     if ((obj instanceof TVtecObject)) {
/* 167 */       TVtecObject vtec = (TVtecObject)obj;
/* 168 */       if (getPriority(this) == getPriority(vtec)) return true;
/*     */     }
/*     */ 
/* 171 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.TVtecObject
 * JD-Core Version:    0.6.2
 */