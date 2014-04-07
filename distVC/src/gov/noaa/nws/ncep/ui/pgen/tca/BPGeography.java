/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.CoordinateArrayAdapter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ public abstract class BPGeography
/*     */   implements ISerializableObject
/*     */ {
/*     */ 
/*     */   @XmlElement(name="path")
/*     */   @XmlJavaTypeAdapter(CoordinateArrayAdapter.class)
/*     */   private List<Coordinate[]> paths;
/*     */ 
/*     */   @XmlElement(name="landZones")
/*     */   @XmlList
/*     */   private List<String> zones;
/*     */ 
/*     */   public BPGeography()
/*     */   {
/*  52 */     this.paths = new ArrayList();
/*  53 */     this.zones = new ArrayList();
/*     */   }
/*     */ 
/*     */   public List<Coordinate[]> getPaths()
/*     */   {
/*  60 */     return this.paths;
/*     */   }
/*     */ 
/*     */   public void setPaths(List<Coordinate[]> paths)
/*     */   {
/*  67 */     this.paths = paths;
/*     */   }
/*     */ 
/*     */   public abstract List<Breakpoint> getBreakpoints();
/*     */ 
/*     */   public void addPath(Coordinate[] coords)
/*     */   {
/*  81 */     this.paths.add(coords);
/*     */   }
/*     */ 
/*     */   public List<String> getZones()
/*     */   {
/*  88 */     return this.zones;
/*     */   }
/*     */ 
/*     */   public void setZones(List<String> zones)
/*     */   {
/*  95 */     this.zones = zones;
/*     */   }
/*     */ 
/*     */   public void addZones(List<String> zones)
/*     */   {
/* 102 */     this.zones.addAll(zones);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.BPGeography
 * JD-Core Version:    0.6.2
 */