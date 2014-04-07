/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*     */ import com.raytheon.uf.common.serialization.adapters.CoordAdapter;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ public class Breakpoint
/*     */   implements ISerializableObject
/*     */ {
/*     */ 
/*     */   @XmlAttribute
/*     */   private String name;
/*     */ 
/*     */   @XmlAttribute
/*     */   private String state;
/*     */ 
/*     */   @XmlAttribute
/*     */   private String country;
/*     */ 
/*     */   @XmlAttribute
/*     */   @XmlJavaTypeAdapter(CoordAdapter.class)
/*     */   private Coordinate location;
/*     */ 
/*     */   @XmlAttribute
/*     */   private boolean official;
/*     */ 
/*     */   public Breakpoint()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Breakpoint(String name, String state, String country, Coordinate location, boolean official)
/*     */   {
/*  66 */     this.name = name;
/*  67 */     this.state = state;
/*  68 */     this.country = country;
/*  69 */     this.location = location;
/*  70 */     this.official = official;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  77 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/*  84 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public Coordinate getLocation()
/*     */   {
/*  91 */     return this.location;
/*     */   }
/*     */ 
/*     */   public void setLocation(Coordinate location)
/*     */   {
/*  98 */     this.location = location;
/*     */   }
/*     */ 
/*     */   public String getState()
/*     */   {
/* 105 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void setState(String state)
/*     */   {
/* 112 */     this.state = state;
/*     */   }
/*     */ 
/*     */   public String getCountry()
/*     */   {
/* 119 */     return this.country;
/*     */   }
/*     */ 
/*     */   public void setCountry(String country)
/*     */   {
/* 126 */     this.country = country;
/*     */   }
/*     */ 
/*     */   public boolean isOfficial()
/*     */   {
/* 133 */     return this.official;
/*     */   }
/*     */ 
/*     */   public void setOfficial(boolean official)
/*     */   {
/* 140 */     this.official = official;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 149 */     boolean retval = false;
/*     */ 
/* 151 */     if (obj == null) return false;
/*     */ 
/* 153 */     if ((obj instanceof Breakpoint)) {
/* 154 */       Breakpoint tmp = (Breakpoint)obj;
/* 155 */       if (getName().equals(tmp.getName())) retval = true;
/*     */     }
/*     */ 
/* 158 */     return retval;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.Breakpoint
 * JD-Core Version:    0.6.2
 */