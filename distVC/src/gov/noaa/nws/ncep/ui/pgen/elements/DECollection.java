/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.geotools.referencing.GeodeticCalculator;
/*     */ import org.geotools.referencing.datum.DefaultEllipsoid;
/*     */ 
/*     */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE})
/*     */ public class DECollection extends AbstractDrawableComponent
/*     */ {
/*     */   protected String collectionName;
/*     */   protected List<AbstractDrawableComponent> compList;
/*     */ 
/*     */   public DECollection(String name)
/*     */   {
/*  53 */     this.collectionName = name;
/*  54 */     this.compList = new ArrayList();
/*     */   }
/*     */ 
/*     */   public DECollection() {
/*  58 */     this.collectionName = "collection";
/*  59 */     this.compList = new ArrayList();
/*     */   }
/*     */ 
/*     */   public String getCollectionName()
/*     */   {
/*  66 */     return this.collectionName;
/*     */   }
/*     */ 
/*     */   public Iterator<DrawableElement> createDEIterator()
/*     */   {
/*  76 */     return new DEIterator(this.compList.listIterator());
/*     */   }
/*     */ 
/*     */   public Iterator<AbstractDrawableComponent> getComponentIterator()
/*     */   {
/*  87 */     return this.compList.iterator();
/*     */   }
/*     */ 
/*     */   public ListIterator<AbstractDrawableComponent> getComponentListIterator()
/*     */   {
/*  94 */     return this.compList.listIterator();
/*     */   }
/*     */ 
/*     */   public void addElement(AbstractDrawableComponent adc)
/*     */   {
/* 102 */     add(adc);
/*     */   }
/*     */ 
/*     */   public void add(AbstractDrawableComponent adc)
/*     */   {
/* 110 */     adc.setParent(this);
/* 111 */     this.compList.add(adc);
/*     */   }
/*     */ 
/*     */   public void add(int index, AbstractDrawableComponent adc) {
/* 115 */     adc.setParent(this);
/* 116 */     this.compList.add(index, adc);
/*     */   }
/*     */ 
/*     */   public void add(List<? extends AbstractDrawableComponent> adcList)
/*     */   {
/* 124 */     for (AbstractDrawableComponent adc : adcList) {
/* 125 */       adc.setParent(this);
/*     */     }
/* 127 */     this.compList.addAll(adcList);
/*     */   }
/*     */ 
/*     */   public void remove(AbstractDrawableComponent adc)
/*     */   {
/* 135 */     this.compList.remove(adc);
/*     */   }
/*     */ 
/*     */   public void removeElement(AbstractDrawableComponent comp)
/*     */   {
/* 143 */     remove(comp);
/*     */   }
/*     */ 
/*     */   public List<Coordinate> getPoints()
/*     */   {
/* 151 */     ArrayList points = new ArrayList();
/* 152 */     Iterator iterator = createDEIterator();
/* 153 */     while (iterator.hasNext()) {
/* 154 */       points.addAll(((DrawableElement)iterator.next()).getPoints());
/*     */     }
/* 156 */     return points;
/*     */   }
/*     */ 
/*     */   public DECollection copy()
/*     */   {
/* 164 */     DECollection dec = new DECollection(this.collectionName);
/* 165 */     Iterator iterator = getComponentIterator();
/*     */ 
/* 167 */     while (iterator.hasNext()) {
/* 168 */       dec.addElement(((AbstractDrawableComponent)iterator.next()).copy());
/*     */     }
/*     */ 
/* 171 */     iterator = dec.getComponentIterator();
/* 172 */     while (iterator.hasNext()) {
/* 173 */       ((AbstractDrawableComponent)iterator.next()).setParent(dec);
/*     */     }
/*     */ 
/* 176 */     dec.setPgenCategory(getPgenCategory());
/* 177 */     dec.setPgenType(getPgenType());
/* 178 */     dec.setParent(getParent());
/* 179 */     return dec;
/*     */   }
/*     */ 
/*     */   public void setColors(Color[] colors)
/*     */   {
/* 187 */     Iterator iterator = createDEIterator();
/* 188 */     while (iterator.hasNext())
/* 189 */       ((DrawableElement)iterator.next()).setColors(colors);
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 199 */     return this.compList.isEmpty();
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 206 */     this.compList.clear();
/*     */   }
/*     */ 
/*     */   public DECollection search(AbstractDrawableComponent component)
/*     */   {
/* 216 */     if (this.compList.contains(component)) {
/* 217 */       return this;
/*     */     }
/*     */ 
/* 220 */     for (AbstractDrawableComponent adc : this.compList) {
/* 221 */       if ((adc instanceof DECollection)) {
/* 222 */         DECollection dec = ((DECollection)adc).search(component);
/* 223 */         if (dec != null) {
/* 224 */           return dec;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */   private boolean replaceComponent(AbstractDrawableComponent oldEl, AbstractDrawableComponent newEl)
/*     */   {
/* 240 */     if (this.compList.contains(oldEl)) {
/* 241 */       int idx = this.compList.indexOf(oldEl);
/* 242 */       this.compList.set(idx, newEl);
/* 243 */       newEl.setParent(this);
/* 244 */       return true;
/*     */     }
/*     */ 
/* 247 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean replace(AbstractDrawableComponent oldEl, AbstractDrawableComponent newEl)
/*     */   {
/* 258 */     DECollection dec = search(oldEl);
/* 259 */     if (dec != null) {
/* 260 */       return dec.replaceComponent(oldEl, newEl);
/*     */     }
/*     */ 
/* 263 */     return false;
/*     */   }
/*     */ 
/*     */   public DrawableElement getPrimaryDE()
/*     */   {
/* 271 */     AbstractDrawableComponent el = (AbstractDrawableComponent)this.compList.get(0);
/* 272 */     if ((el instanceof DrawableElement)) {
/* 273 */       return (DrawableElement)el;
/*     */     }
/*     */ 
/* 276 */     return el.getPrimaryDE();
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent getItemAt(int index)
/*     */   {
/* 286 */     return (AbstractDrawableComponent)this.compList.get(index);
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent getNearestComponent(Coordinate point)
/*     */   {
/* 296 */     AbstractDrawableComponent nearestComponent = null;
/* 297 */     double minDistance = -1.0D;
/*     */ 
/* 299 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*     */ 
/* 301 */     gc.setStartingGeographicPoint(point.x, point.y);
/*     */ 
/* 303 */     Iterator iterator = getComponentIterator();
/*     */     Iterator localIterator1;
/* 305 */     for (; iterator.hasNext(); 
/* 308 */       localIterator1.hasNext())
/*     */     {
/* 306 */       AbstractDrawableComponent comp = (AbstractDrawableComponent)iterator.next();
/*     */ 
/* 308 */       localIterator1 = comp.getPoints().iterator(); continue; Coordinate pts = (Coordinate)localIterator1.next();
/*     */ 
/* 310 */       gc.setDestinationGeographicPoint(pts.x, pts.y);
/* 311 */       double dist = gc.getOrthodromicDistance();
/*     */ 
/* 313 */       if ((minDistance < 0.0D) || (dist < minDistance))
/*     */       {
/* 315 */         minDistance = dist;
/* 316 */         nearestComponent = comp;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 323 */     return nearestComponent;
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent getNearestDE(Coordinate point)
/*     */   {
/* 334 */     DrawableElement nearestDE = null;
/* 335 */     double minDistance = -1.0D;
/*     */ 
/* 337 */     GeodeticCalculator gc = new GeodeticCalculator(DefaultEllipsoid.WGS84);
/*     */ 
/* 339 */     gc.setStartingGeographicPoint(point.x, point.y);
/*     */ 
/* 341 */     Iterator iterator = createDEIterator();
/*     */     Iterator localIterator1;
/* 343 */     for (; iterator.hasNext(); 
/* 346 */       localIterator1.hasNext())
/*     */     {
/* 344 */       DrawableElement de = (DrawableElement)iterator.next();
/*     */ 
/* 346 */       localIterator1 = de.getPoints().iterator(); continue; Coordinate pts = (Coordinate)localIterator1.next();
/*     */ 
/* 348 */       gc.setDestinationGeographicPoint(pts.x, pts.y);
/* 349 */       double dist = gc.getOrthodromicDistance();
/*     */ 
/* 351 */       if ((minDistance < 0.0D) || (dist < minDistance))
/*     */       {
/* 353 */         minDistance = dist;
/* 354 */         nearestDE = de;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 361 */     return nearestDE;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 368 */     return getCollectionName();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 376 */     return this.compList.size();
/*     */   }
/*     */ 
/*     */   public boolean contains(AbstractDrawableComponent adc)
/*     */   {
/* 387 */     if (this.compList.contains(adc)) return true;
/*     */ 
/* 389 */     Iterator it = getComponentIterator();
/* 390 */     while (it.hasNext()) {
/* 391 */       AbstractDrawableComponent item = (AbstractDrawableComponent)it.next();
/* 392 */       if (((item instanceof DECollection)) && 
/* 393 */         (((DECollection)item).contains(adc))) return true;
/*     */ 
/*     */     }
/*     */ 
/* 398 */     return false;
/*     */   }
/*     */ 
/*     */   public void setCollectionName(String collectionName) {
/* 402 */     this.collectionName = collectionName;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.DECollection
 * JD-Core Version:    0.6.2
 */