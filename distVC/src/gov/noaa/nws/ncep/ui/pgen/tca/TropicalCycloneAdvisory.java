/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlElements;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ 
/*     */ @XmlType(name="", propOrder={"severity", "advisoryType", "geographyType", "segment"})
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ public class TropicalCycloneAdvisory
/*     */   implements ISerializableObject
/*     */ {
/*     */ 
/*     */   @XmlElement
/*     */   private String severity;
/*     */ 
/*     */   @XmlElement
/*     */   private String advisoryType;
/*     */ 
/*     */   @XmlElement
/*     */   private String geographyType;
/*     */ 
/*     */   @XmlElements({@XmlElement(name="coast", type=BreakpointPair.class), @XmlElement(name="island", type=IslandBreakpoint.class), @XmlElement(name="waterway", type=WaterBreakpoint.class)})
/*     */   private BPGeography segment;
/*     */ 
/*     */   public TropicalCycloneAdvisory()
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getSeverity()
/*     */   {
/*  76 */     return this.severity;
/*     */   }
/*     */ 
/*     */   public TropicalCycloneAdvisory(String severity, String advisoryType, String geographyType, BPGeography segment)
/*     */   {
/*  88 */     this.severity = severity;
/*  89 */     this.advisoryType = advisoryType;
/*  90 */     this.geographyType = geographyType;
/*  91 */     this.segment = segment;
/*     */   }
/*     */ 
/*     */   public void setSeverity(String severity)
/*     */   {
/*  98 */     this.severity = severity;
/*     */   }
/*     */ 
/*     */   public String getAdvisoryType()
/*     */   {
/* 105 */     return this.advisoryType;
/*     */   }
/*     */ 
/*     */   public void setAdvisoryType(String advisoryType)
/*     */   {
/* 112 */     this.advisoryType = advisoryType;
/*     */   }
/*     */ 
/*     */   public String getGeographyType()
/*     */   {
/* 119 */     return this.geographyType;
/*     */   }
/*     */ 
/*     */   public void setGeographyType(String geographyType)
/*     */   {
/* 126 */     this.geographyType = geographyType;
/*     */   }
/*     */ 
/*     */   public BPGeography getSegment()
/*     */   {
/* 133 */     return this.segment;
/*     */   }
/*     */ 
/*     */   public void setSegment(BPGeography segment)
/*     */   {
/* 140 */     this.segment = segment;
/*     */   }
/*     */ 
/*     */   public TropicalCycloneAdvisory copy()
/*     */   {
/* 162 */     String sevType = new String(getSeverity());
/* 163 */     String advType = new String(getAdvisoryType());
/* 164 */     String geogType = new String(getGeographyType());
/* 165 */     return new TropicalCycloneAdvisory(sevType, advType, geogType, getSegment());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 175 */     if (obj == null) return false;
/*     */ 
/* 177 */     if ((obj instanceof TropicalCycloneAdvisory)) {
/* 178 */       TropicalCycloneAdvisory tca = (TropicalCycloneAdvisory)obj;
/*     */ 
/* 180 */       if (!this.geographyType.equals(tca.getGeographyType())) return false;
/*     */ 
/* 182 */       if (!this.advisoryType.equals(tca.getAdvisoryType())) return false;
/*     */ 
/* 184 */       if (!this.severity.equals(tca.getSeverity())) return false;
/*     */ 
/* 186 */       List thislist = this.segment.getBreakpoints();
/* 187 */       List thatlist = tca.getSegment().getBreakpoints();
/* 188 */       if (thislist.size() != thatlist.size()) return false;
/*     */ 
/* 190 */       for (int j = 0; j < thislist.size(); j++)
/* 191 */         if (!((Breakpoint)thislist.get(j)).equals(thatlist.get(j))) return false;
/*     */     }
/*     */     else
/*     */     {
/* 195 */       return false;
/*     */     }
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean overlaps(TropicalCycloneAdvisory tca)
/*     */   {
/* 208 */     BreakpointManager bm = BreakpointManager.getInstance();
/*     */ 
/* 210 */     if (!this.advisoryType.equals(tca.getAdvisoryType())) return false;
/*     */ 
/* 212 */     if (!this.severity.equals(tca.getSeverity())) return false;
/*     */ 
/* 214 */     if ((!(this.segment instanceof BreakpointPair)) || 
/* 215 */       (!(tca.getSegment() instanceof BreakpointPair))) return false;
/*     */ 
/* 217 */     return bm.pairsOverlap((BreakpointPair)this.segment, (BreakpointPair)tca.getSegment());
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.TropicalCycloneAdvisory
 * JD-Core Version:    0.6.2
 */