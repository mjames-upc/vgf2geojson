/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TCVEvent
/*     */   implements Comparable<TCVEvent>
/*     */ {
/*     */   private TCVEventType evenType;
/*     */   private UGCGroup ugc;
/*     */   private List<TVtecObject> vtecLines;
/*     */   private List<Breakpoint> breakpoints;
/*     */ 
/*     */   public TCVEvent(TCVEventType evenType)
/*     */   {
/*  37 */     this.evenType = evenType;
/*  38 */     this.ugc = new UGCGroup();
/*  39 */     this.vtecLines = new ArrayList();
/*  40 */     this.breakpoints = new ArrayList();
/*     */   }
/*     */ 
/*     */   public TCVEventType getEvenType()
/*     */   {
/*  49 */     return this.evenType;
/*     */   }
/*     */ 
/*     */   public List<TVtecObject> getVtecLines()
/*     */   {
/*  58 */     return this.vtecLines;
/*     */   }
/*     */ 
/*     */   public List<Breakpoint> getBreakpoints()
/*     */   {
/*  67 */     return this.breakpoints;
/*     */   }
/*     */ 
/*     */   public void addVtecLine(TVtecObject vtec)
/*     */   {
/*  73 */     this.vtecLines.add(vtec);
/*  74 */     Collections.sort(this.vtecLines);
/*     */   }
/*     */ 
/*     */   public UGCGroup getUgc()
/*     */   {
/*  83 */     return this.ugc;
/*     */   }
/*     */ 
/*     */   public void setBreakpoints(List<Breakpoint> breakpoints)
/*     */   {
/*  90 */     this.breakpoints = breakpoints;
/*     */   }
/*     */ 
/*     */   public void addZones(List<String> zones) {
/*  94 */     this.ugc.addZones(zones);
/*     */   }
/*     */ 
/*     */   public void setPurgeTime(Calendar time) {
/*  98 */     this.ugc.setPurgeTime(time);
/*     */   }
/*     */ 
/*     */   public int compareTo(TCVEvent o)
/*     */   {
/* 108 */     TVtecObject thisone = (TVtecObject)getVtecLines().get(0);
/* 109 */     TVtecObject thatone = (TVtecObject)o.getVtecLines().get(0);
/*     */ 
/* 111 */     return thisone.compareTo(thatone);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 123 */     if ((obj instanceof TCVEvent)) {
/* 124 */       TVtecObject thisone = (TVtecObject)getVtecLines().get(0);
/* 125 */       TCVEvent o = (TCVEvent)obj;
/* 126 */       TVtecObject thatone = (TVtecObject)o.getVtecLines().get(0);
/* 127 */       return thisone.equals(thatone);
/*     */     }
/*     */ 
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   public void addBreakpoint(Breakpoint bkpt)
/*     */   {
/* 136 */     this.breakpoints.add(bkpt);
/*     */   }
/*     */ 
/*     */   public static enum TCVEventType
/*     */   {
/*  25 */     LIST, SEGMENT;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.TCVEvent
 * JD-Core Version:    0.6.2
 */