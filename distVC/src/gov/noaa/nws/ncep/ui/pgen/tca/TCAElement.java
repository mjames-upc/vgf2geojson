/*     */ package gov.noaa.nws.ncep.ui.pgen.tca;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TCAElement extends DrawableElement
/*     */   implements ITca
/*     */ {
/*     */   private int stormNumber;
/*     */   private String issueStatus;
/*     */   private String basin;
/*     */   private String advisoryNumber;
/*     */   private String stormName;
/*     */   private String stormType;
/*     */   private Calendar advisoryTime;
/*     */   private String timeZone;
/*     */   private Coordinate textLocation;
/*     */   private ArrayList<TropicalCycloneAdvisory> advisories;
/*     */ 
/*     */   public TCAElement()
/*     */   {
/*  46 */     this.advisories = new ArrayList();
/*  47 */     this.textLocation = new Coordinate(-80.400000000000006D, 25.800000000000001D);
/*     */   }
/*     */ 
/*     */   public ArrayList<TropicalCycloneAdvisory> getAdvisories()
/*     */   {
/*  55 */     return this.advisories;
/*     */   }
/*     */ 
/*     */   public String getAdvisoryNumber()
/*     */   {
/*  63 */     return this.advisoryNumber;
/*     */   }
/*     */ 
/*     */   public String getStormName()
/*     */   {
/*  71 */     return this.stormName;
/*     */   }
/*     */ 
/*     */   public String getStormType()
/*     */   {
/*  79 */     return this.stormType;
/*     */   }
/*     */ 
/*     */   public Coordinate getTextLocation()
/*     */   {
/*  87 */     return this.textLocation;
/*     */   }
/*     */ 
/*     */   public void setPointsOnly(ArrayList<Coordinate> pts)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void update(IAttribute attr)
/*     */   {
/*  98 */     if ((attr instanceof ITca)) {
/*  99 */       ITca tca = (ITca)attr;
/* 100 */       setIssueStatus(tca.getIssuingStatus());
/* 101 */       setStormType(tca.getStormType());
/* 102 */       setBasin(tca.getBasin());
/* 103 */       setStormName(tca.getStormName());
/* 104 */       setStormNumber(tca.getStormNumber());
/* 105 */       setAdvisoryTime(tca.getAdvisoryTime());
/* 106 */       setAdvisoryNumber(tca.getAdvisoryNumber());
/* 107 */       setTimeZone(tca.getTimeZone());
/*     */ 
/* 109 */       setAdvisories(tca.getAdvisories());
/* 110 */       setTextLocation(tca.getTextLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent copy()
/*     */   {
/* 124 */     TCAElement newTca = new TCAElement();
/*     */ 
/* 130 */     newTca.setIssueStatus(new String(getIssuingStatus()));
/* 131 */     newTca.setStormType(new String(getStormType()));
/* 132 */     newTca.setBasin(new String(getBasin()));
/* 133 */     newTca.setStormName(new String(getStormName()));
/* 134 */     newTca.setStormNumber(getStormNumber());
/* 135 */     newTca.setAdvisoryNumber(new String(getAdvisoryNumber()));
/* 136 */     newTca.setTimeZone(new String(getTimeZone()));
/* 137 */     newTca.setTextLocation(new Coordinate(getTextLocation()));
/*     */ 
/* 143 */     Calendar newTime = Calendar.getInstance();
/* 144 */     newTime.setTimeInMillis(getAdvisoryTime().getTimeInMillis());
/* 145 */     newTca.setAdvisoryTime(newTime);
/*     */ 
/* 147 */     for (TropicalCycloneAdvisory adv : getAdvisories()) {
/* 148 */       newTca.addAdvisory(adv.copy());
/*     */     }
/*     */ 
/* 151 */     newTca.setPgenCategory(new String(getPgenCategory()));
/* 152 */     newTca.setPgenType(new String(getPgenType()));
/*     */ 
/* 154 */     newTca.setParent(getParent());
/*     */ 
/* 156 */     return newTca;
/*     */   }
/*     */ 
/*     */   public List<Coordinate> getPoints()
/*     */   {
/* 166 */     List points = new ArrayList();
/*     */ 
/* 168 */     if (this.advisories.isEmpty()) {
/* 169 */       points.add(this.textLocation);
/*     */     }
/*     */     else
/*     */     {
/*     */       Iterator localIterator2;
/* 172 */       for (Iterator localIterator1 = this.advisories.iterator(); localIterator1.hasNext(); 
/* 174 */         localIterator2.hasNext())
/*     */       {
/* 172 */         TropicalCycloneAdvisory tca = (TropicalCycloneAdvisory)localIterator1.next();
/* 173 */         BPGeography segment = tca.getSegment();
/* 174 */         localIterator2 = segment.getBreakpoints().iterator(); continue; Breakpoint bkpt = (Breakpoint)localIterator2.next();
/* 175 */         points.add(bkpt.getLocation());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 180 */     return points;
/*     */   }
/*     */ 
/*     */   public void setColors(Color[] colors)
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getBasin()
/*     */   {
/* 190 */     return this.basin;
/*     */   }
/*     */ 
/*     */   public String getIssuingStatus()
/*     */   {
/* 195 */     return this.issueStatus;
/*     */   }
/*     */ 
/*     */   public int getStormNumber()
/*     */   {
/* 200 */     return this.stormNumber;
/*     */   }
/*     */ 
/*     */   public String getTimeZone()
/*     */   {
/* 205 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */   public String getIssueStatus()
/*     */   {
/* 212 */     return this.issueStatus;
/*     */   }
/*     */ 
/*     */   public void setIssueStatus(String issueStatus)
/*     */   {
/* 219 */     this.issueStatus = issueStatus;
/*     */   }
/*     */ 
/*     */   public Calendar getAdvisoryTime()
/*     */   {
/* 226 */     return this.advisoryTime;
/*     */   }
/*     */ 
/*     */   public void setAdvisoryTime(Calendar advisoryTime)
/*     */   {
/* 233 */     this.advisoryTime = advisoryTime;
/*     */   }
/*     */ 
/*     */   public void setStormNumber(int stormNumber)
/*     */   {
/* 240 */     this.stormNumber = stormNumber;
/*     */   }
/*     */ 
/*     */   public void setBasin(String basin)
/*     */   {
/* 247 */     this.basin = basin;
/*     */   }
/*     */ 
/*     */   public void setAdvisoryNumber(String advisoryNumber)
/*     */   {
/* 254 */     this.advisoryNumber = advisoryNumber;
/*     */   }
/*     */ 
/*     */   public void setStormName(String stormName)
/*     */   {
/* 261 */     this.stormName = stormName;
/*     */   }
/*     */ 
/*     */   public void setStormType(String stormType)
/*     */   {
/* 268 */     this.stormType = stormType;
/*     */   }
/*     */ 
/*     */   public void setTimeZone(String timeZone)
/*     */   {
/* 275 */     this.timeZone = timeZone;
/*     */   }
/*     */ 
/*     */   public void setTextLocation(Coordinate textLocation)
/*     */   {
/* 282 */     this.textLocation = textLocation;
/*     */   }
/*     */ 
/*     */   public void setAdvisories(ArrayList<TropicalCycloneAdvisory> advisories)
/*     */   {
/* 290 */     this.advisories.clear();
/* 291 */     for (TropicalCycloneAdvisory adv : advisories)
/* 292 */       addAdvisory(adv.copy());
/*     */   }
/*     */ 
/*     */   public void addAdvisory(TropicalCycloneAdvisory advisory)
/*     */   {
/* 297 */     this.advisories.add(advisory);
/*     */   }
/*     */ 
/*     */   public void replaceAdvisory(int index, TropicalCycloneAdvisory advisory) {
/* 301 */     this.advisories.set(index, advisory);
/*     */   }
/*     */ 
/*     */   public boolean containsBreakpoint(String name)
/*     */   {
/*     */     Iterator localIterator2;
/* 311 */     for (Iterator localIterator1 = this.advisories.iterator(); localIterator1.hasNext(); 
/* 312 */       localIterator2.hasNext())
/*     */     {
/* 311 */       TropicalCycloneAdvisory adv = (TropicalCycloneAdvisory)localIterator1.next();
/* 312 */       localIterator2 = adv.getSegment().getBreakpoints().iterator(); continue; Breakpoint bkpt = (Breakpoint)localIterator2.next();
/* 313 */       if (bkpt.getName().equals(name)) return true;
/*     */     }
/*     */ 
/* 316 */     return false;
/*     */   }
/*     */ 
/*     */   public TropicalCycloneAdvisory findClosestAdvisory(Coordinate loc)
/*     */   {
/* 324 */     TropicalCycloneAdvisory selectedAdv = null;
/* 325 */     GeometryFactory gf = new GeometryFactory();
/* 326 */     Point pt = gf.createPoint(loc);
/* 327 */     double min = 1.7976931348623157E+308D;
/* 328 */     Geometry geom = null;
/*     */ 
/* 334 */     for (TropicalCycloneAdvisory adv : getAdvisories()) {
/* 335 */       if ((adv.getSegment() instanceof BreakpointPair))
/*     */       {
/* 339 */         Coordinate[] coords = { ((Breakpoint)adv.getSegment().getBreakpoints().get(0)).getLocation(), 
/* 340 */           ((Breakpoint)adv.getSegment().getBreakpoints().get(1)).getLocation() };
/*     */ 
/* 342 */         geom = gf.createLineString(coords);
/*     */       }
/*     */       else
/*     */       {
/* 348 */         geom = gf.createPoint(((Breakpoint)adv.getSegment().getBreakpoints().get(0)).getLocation());
/*     */       }
/*     */ 
/* 351 */       double dist = pt.distance(geom);
/* 352 */       if (dist < min) {
/* 353 */         min = dist;
/* 354 */         selectedAdv = adv;
/*     */       }
/*     */     }
/*     */ 
/* 358 */     return selectedAdv;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tca.TCAElement
 * JD-Core Version:    0.6.2
 */