/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.CoordinateList;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.LineString;
/*     */ import com.vividsolutions.jts.linearref.LinearLocation;
/*     */ import com.vividsolutions.jts.linearref.LocationIndexedLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PGenException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.IJetTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.MultiPointElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DeletePartCommand extends PgenCommand
/*     */ {
/*     */   private List<Product> list;
/*     */   private DECollection parent;
/*     */   private AbstractDrawableComponent element;
/*  69 */   private AbstractDrawableComponent element1 = null;
/*  70 */   private AbstractDrawableComponent element2 = null;
/*     */   private Coordinate firstPt;
/*     */   private Coordinate secondPt;
/*     */   LocationIndexedLine lil;
/*     */   private LinearLocation firstLoc;
/*     */   private LinearLocation secondLoc;
/*     */   private boolean removeAll;
/*     */   private boolean removeOneEnd;
/*     */   private boolean removeMiddle;
/*     */ 
/*     */   public DeletePartCommand(List<Product> list, Line element, Coordinate point1, Coordinate point2)
/*     */   {
/*  96 */     this.list = list;
/*  97 */     this.element = element;
/*     */ 
/*  99 */     GeometryFactory gf = new GeometryFactory();
/*     */ 
/* 105 */     CoordinateList clist = new CoordinateList(element.getLinePoints());
/* 106 */     if (element.isClosedLine().booleanValue()) clist.closeRing();
/* 107 */     LineString ls = gf.createLineString(clist.toCoordinateArray());
/* 108 */     this.lil = new LocationIndexedLine(ls);
/* 109 */     LinearLocation loc1 = this.lil.project(point1);
/* 110 */     LinearLocation loc2 = this.lil.project(point2);
/* 111 */     if (loc1.compareTo(loc2) <= 0) {
/* 112 */       this.firstLoc = loc1;
/* 113 */       this.secondLoc = loc2;
/* 114 */       this.firstPt = point1;
/* 115 */       this.secondPt = point2;
/*     */     }
/*     */     else {
/* 118 */       this.firstLoc = loc2;
/* 119 */       this.secondLoc = loc1;
/* 120 */       this.firstPt = point2;
/* 121 */       this.secondPt = point1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void execute()
/*     */     throws PGenException
/*     */   {
/* 141 */     this.parent = ((DECollection)this.element.getParent());
/* 142 */     if (((Line)this.element).isClosedLine().booleanValue())
/*     */     {
/* 144 */       deleteClosedPart();
/*     */     }
/*     */     else
/*     */     {
/* 149 */       deleteOpenPart();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void undo()
/*     */     throws PGenException
/*     */   {
/* 169 */     if ((!(this.element instanceof Jet)) && (((Line)this.element).isClosedLine().booleanValue())) {
/* 170 */       undeleteClosedPart();
/*     */     }
/*     */     else
/* 173 */       undeleteOpenPart();
/*     */   }
/*     */ 
/*     */   private void deleteOpenPart()
/*     */   {
/* 183 */     List points = this.element.getPoints();
/*     */ 
/* 185 */     if ((this.lil.getStartIndex().compareTo(this.firstLoc) == 0) && 
/* 186 */       (this.lil.getEndIndex().getSegmentIndex() == this.secondLoc.getSegmentIndex()))
/*     */     {
/* 191 */       this.removeAll = true;
/* 192 */       this.parent.removeElement(this.element);
/*     */     }
/* 195 */     else if ((this.lil.getStartIndex().compareTo(this.firstLoc) == 0) || 
/* 196 */       (this.lil.getEndIndex().getSegmentIndex() == this.secondLoc.getSegmentIndex()))
/*     */     {
/* 201 */       this.removeOneEnd = true;
/* 202 */       this.element1 = ((MultiPointElement)this.element.copy());
/* 203 */       ArrayList newPts = new ArrayList();
/*     */ 
/* 205 */       if (this.lil.getStartIndex().compareTo(this.firstLoc) == 0) {
/* 206 */         newPts.add(this.secondPt);
/* 207 */         newPts.addAll(points.subList(this.secondLoc.getSegmentIndex() + 1, points.size()));
/*     */       }
/* 209 */       else if (this.lil.getEndIndex().getSegmentIndex() == this.secondLoc.getSegmentIndex()) {
/* 210 */         newPts.addAll(points.subList(0, this.firstLoc.getSegmentIndex() + 1));
/* 211 */         newPts.add(this.firstPt);
/*     */       }
/*     */ 
/* 214 */       ((MultiPointElement)this.element1).setPoints(newPts);
/*     */ 
/* 216 */       this.parent.addElement(this.element1);
/* 217 */       this.parent.removeElement(this.element);
/*     */     }
/*     */     else
/*     */     {
/* 223 */       this.removeMiddle = true;
/*     */ 
/* 225 */       if ((this.element1 == null) && (this.element2 == null)) {
/* 226 */         this.element1 = ((MultiPointElement)this.element.copy());
/* 227 */         ArrayList new1 = new ArrayList(points.subList(0, this.firstLoc.getSegmentIndex() + 1));
/* 228 */         new1.add(this.firstPt);
/* 229 */         ((MultiPointElement)this.element1).setPoints(new1);
/*     */ 
/* 231 */         this.element2 = ((MultiPointElement)this.element.copy());
/* 232 */         ArrayList new2 = new ArrayList();
/* 233 */         new2.add(this.secondPt);
/* 234 */         new2.addAll(points.subList(this.secondLoc.getSegmentIndex() + 1, points.size()));
/* 235 */         ((MultiPointElement)this.element2).setPoints(new2);
/*     */       }
/*     */ 
/* 238 */       if ((this.parent instanceof Jet)) {
/* 239 */         Jet jet1 = new Jet();
/* 240 */         jet1.clear();
/* 241 */         jet1.addElement(this.element1);
/* 242 */         jet1.setPgenCategory(this.parent.getPgenCategory());
/* 243 */         jet1.setPgenType(this.parent.getPgenType());
/*     */ 
/* 245 */         jet1.setSnapTool(((Jet)this.parent).getSnapTool());
/*     */ 
/* 247 */         Jet jet2 = new Jet();
/* 248 */         jet2.clear();
/* 249 */         jet2.addElement(this.element2);
/* 250 */         jet2.setSnapTool(((Jet)this.parent).getSnapTool());
/* 251 */         jet2.setPgenCategory(this.parent.getPgenCategory());
/* 252 */         jet2.setPgenType(this.parent.getPgenType());
/*     */ 
/* 254 */         jet1.getSnapTool().addBarbHashFromAnotherJet(jet1, (Jet)this.parent);
/* 255 */         jet2.getSnapTool().addBarbHashFromAnotherJet(jet2, (Jet)this.parent);
/*     */ 
/* 257 */         jet1.getSnapTool().snapJet(jet1);
/* 258 */         jet2.getSnapTool().snapJet(jet2);
/*     */ 
/* 260 */         this.element = this.parent;
/* 261 */         this.parent = ((DECollection)this.parent.getParent());
/* 262 */         this.element1 = jet1;
/* 263 */         this.element2 = jet2;
/*     */       }
/*     */ 
/* 266 */       this.parent.addElement(this.element1);
/* 267 */       this.parent.addElement(this.element2);
/* 268 */       this.parent.removeElement(this.element);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void deleteClosedPart()
/*     */   {
/* 278 */     List points = this.element.getPoints();
/* 279 */     int pointsBetween = this.secondLoc.getSegmentIndex() - this.firstLoc.getSegmentIndex();
/*     */ 
/* 281 */     if (pointsBetween > points.size() - pointsBetween)
/*     */     {
/* 284 */       this.element1 = ((MultiPointElement)this.element.copy());
/* 285 */       if ((this.element1 instanceof Gfa)) {
/* 286 */         ((MultiPointElement)this.element1).setClosed(Boolean.valueOf(true));
/*     */       }
/*     */       else {
/* 289 */         ((MultiPointElement)this.element1).setClosed(Boolean.valueOf(false));
/*     */       }
/* 291 */       this.element1.getPoints().clear();
/* 292 */       this.element1.getPoints().add(this.firstPt);
/* 293 */       this.element1.getPoints().addAll(points.subList(this.firstLoc.getSegmentIndex() + 1, this.secondLoc.getSegmentIndex() + 1));
/* 294 */       this.element1.getPoints().add(this.secondPt);
/*     */ 
/* 296 */       this.parent.addElement(this.element1);
/* 297 */       this.parent.removeElement(this.element);
/*     */     }
/*     */     else
/*     */     {
/* 302 */       this.element1 = ((MultiPointElement)this.element.copy());
/* 303 */       if ((this.element1 instanceof Gfa)) {
/* 304 */         ((MultiPointElement)this.element1).setClosed(Boolean.valueOf(true));
/*     */       }
/*     */       else {
/* 307 */         ((MultiPointElement)this.element1).setClosed(Boolean.valueOf(false));
/*     */       }
/* 309 */       this.element1.getPoints().clear();
/* 310 */       this.element1.getPoints().add(this.secondPt);
/* 311 */       this.element1.getPoints().addAll(points.subList(this.secondLoc.getSegmentIndex() + 1, points.size()));
/* 312 */       this.element1.getPoints().addAll(points.subList(0, this.firstLoc.getSegmentIndex() + 1));
/* 313 */       this.element1.getPoints().add(this.firstPt);
/*     */ 
/* 315 */       this.parent.addElement(this.element1);
/* 316 */       this.parent.removeElement(this.element);
/*     */     }
/*     */ 
/* 320 */     if ((this.element1 instanceof Gfa))
/* 321 */       ((Gfa)this.element1).setGfaVorText(Gfa.buildVorText((Gfa)this.element1));
/*     */   }
/*     */ 
/*     */   private void undeleteOpenPart()
/*     */   {
/* 332 */     if (this.removeAll)
/*     */     {
/* 334 */       this.parent.addElement(this.element);
/* 335 */       this.removeAll = false;
/*     */     }
/* 338 */     else if (this.removeOneEnd)
/*     */     {
/* 340 */       this.parent.addElement(this.element);
/* 341 */       this.parent.removeElement(this.element1);
/* 342 */       this.removeOneEnd = false;
/*     */     }
/* 345 */     else if (this.removeMiddle)
/*     */     {
/* 347 */       this.parent.removeElement(this.element1);
/* 348 */       this.parent.removeElement(this.element2);
/* 349 */       this.parent.addElement(this.element);
/*     */ 
/* 351 */       if ((this.element instanceof Jet)) {
/* 352 */         this.parent = ((Jet)this.element);
/* 353 */         this.element = ((Jet)this.parent).getJetLine();
/* 354 */         this.element1 = ((Jet)this.element1).getJetLine();
/* 355 */         this.element2 = ((Jet)this.element2).getJetLine();
/*     */       }
/*     */ 
/* 358 */       this.removeMiddle = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void undeleteClosedPart()
/*     */   {
/* 368 */     this.parent.removeElement(this.element1);
/* 369 */     this.parent.addElement(this.element);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.DeletePartCommand
 * JD-Core Version:    0.6.2
 */