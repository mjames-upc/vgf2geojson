/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="", propOrder={"deCollection", "line", "symbol", "text", "avnText", "midCloudText", "arc", "vector", "track", "contours", "tca", "sigmet", "watchBox", "outlook", "gfa", "volcano", "tcm", "spenes"})
/*     */ @XmlRootElement(name="DrawableElement")
/*     */ public class DrawableElement
/*     */ {
/*     */ 
/*     */   @XmlElement(name="DECollection")
/*     */   protected List<DECollection> deCollection;
/*     */ 
/*     */   @XmlElement(name="Line")
/*     */   protected List<Line> line;
/*     */ 
/*     */   @XmlElement(name="Symbol")
/*     */   protected List<Symbol> symbol;
/*     */ 
/*     */   @XmlElement(name="Text")
/*     */   protected List<Text> text;
/*     */ 
/*     */   @XmlElement(name="AvnText")
/*     */   protected List<AvnText> avnText;
/*     */ 
/*     */   @XmlElement(name="MidCloudText")
/*     */   protected List<MidCloudText> midCloudText;
/*     */ 
/*     */   @XmlElement(name="Arc")
/*     */   protected List<Arc> arc;
/*     */ 
/*     */   @XmlElement(name="Vector")
/*     */   protected List<Vector> vector;
/*     */ 
/*     */   @XmlElement(name="Track")
/*     */   protected List<Track> track;
/*     */ 
/*     */   @XmlElement(name="Contours")
/*     */   protected List<Contours> contours;
/*     */ 
/*     */   @XmlElement(name="TCA")
/*     */   protected List<TCA> tca;
/*     */ 
/*     */   @XmlElement(name="Spenes")
/*     */   protected List<Spenes> spenes;
/*     */ 
/*     */   @XmlElement(name="Sigmet")
/*     */   protected List<Sigmet> sigmet;
/*     */ 
/*     */   @XmlElement(name="WatchBox")
/*     */   protected List<WatchBox> watchBox;
/*     */ 
/*     */   @XmlElement(name="Outlook")
/*     */   protected List<Outlook> outlook;
/*     */ 
/*     */   @XmlElement(name="Gfa")
/*     */   protected List<Gfa> gfa;
/*     */ 
/*     */   @XmlElement(name="Volcano")
/*     */   protected List<Volcano> volcano;
/*     */ 
/*     */   @XmlElement(name="TCM")
/*     */   protected List<TCM> tcm;
/*     */ 
/*     */   public List<DECollection> getDECollection()
/*     */   {
/* 137 */     if (this.deCollection == null) {
/* 138 */       this.deCollection = new ArrayList();
/*     */     }
/* 140 */     return this.deCollection;
/*     */   }
/*     */ 
/*     */   public List<Line> getLine()
/*     */   {
/* 166 */     if (this.line == null) {
/* 167 */       this.line = new ArrayList();
/*     */     }
/* 169 */     return this.line;
/*     */   }
/*     */ 
/*     */   public List<Symbol> getSymbol()
/*     */   {
/* 195 */     if (this.symbol == null) {
/* 196 */       this.symbol = new ArrayList();
/*     */     }
/* 198 */     return this.symbol;
/*     */   }
/*     */ 
/*     */   public List<Text> getText()
/*     */   {
/* 224 */     if (this.text == null) {
/* 225 */       this.text = new ArrayList();
/*     */     }
/* 227 */     return this.text;
/*     */   }
/*     */ 
/*     */   public List<AvnText> getAvnText()
/*     */   {
/* 253 */     if (this.avnText == null) {
/* 254 */       this.avnText = new ArrayList();
/*     */     }
/* 256 */     return this.avnText;
/*     */   }
/*     */ 
/*     */   public List<MidCloudText> getMidCloudText()
/*     */   {
/* 282 */     if (this.midCloudText == null) {
/* 283 */       this.midCloudText = new ArrayList();
/*     */     }
/* 285 */     return this.midCloudText;
/*     */   }
/*     */ 
/*     */   public List<Arc> getArc()
/*     */   {
/* 312 */     if (this.arc == null) {
/* 313 */       this.arc = new ArrayList();
/*     */     }
/* 315 */     return this.arc;
/*     */   }
/*     */ 
/*     */   public List<Vector> getVector()
/*     */   {
/* 341 */     if (this.vector == null) {
/* 342 */       this.vector = new ArrayList();
/*     */     }
/* 344 */     return this.vector;
/*     */   }
/*     */ 
/*     */   public List<Track> getTrack()
/*     */   {
/* 370 */     if (this.track == null) {
/* 371 */       this.track = new ArrayList();
/*     */     }
/* 373 */     return this.track;
/*     */   }
/*     */ 
/*     */   public List<Contours> getContours()
/*     */   {
/* 399 */     if (this.contours == null) {
/* 400 */       this.contours = new ArrayList();
/*     */     }
/* 402 */     return this.contours;
/*     */   }
/*     */ 
/*     */   public List<TCA> getTCA()
/*     */   {
/* 428 */     if (this.tca == null) {
/* 429 */       this.tca = new ArrayList();
/*     */     }
/* 431 */     return this.tca;
/*     */   }
/*     */ 
/*     */   public List<Spenes> getSpenes()
/*     */   {
/* 457 */     if (this.spenes == null) {
/* 458 */       this.spenes = new ArrayList();
/*     */     }
/* 460 */     return this.spenes;
/*     */   }
/*     */ 
/*     */   public List<Sigmet> getSigmet()
/*     */   {
/* 486 */     if (this.sigmet == null) {
/* 487 */       this.sigmet = new ArrayList();
/*     */     }
/* 489 */     return this.sigmet;
/*     */   }
/*     */ 
/*     */   public List<WatchBox> getWatchBox()
/*     */   {
/* 515 */     if (this.watchBox == null) {
/* 516 */       this.watchBox = new ArrayList();
/*     */     }
/* 518 */     return this.watchBox;
/*     */   }
/*     */ 
/*     */   public List<Outlook> getOutlook() {
/* 522 */     if (this.outlook == null) {
/* 523 */       this.outlook = new ArrayList();
/*     */     }
/* 525 */     return this.outlook;
/*     */   }
/*     */ 
/*     */   public List<Gfa> getGfa()
/*     */   {
/* 551 */     if (this.gfa == null) {
/* 552 */       this.gfa = new ArrayList();
/*     */     }
/* 554 */     return this.gfa;
/*     */   }
/*     */ 
/*     */   public List<Volcano> getVolcano() {
/* 558 */     if (this.volcano == null) {
/* 559 */       this.volcano = new ArrayList();
/*     */     }
/* 561 */     return this.volcano;
/*     */   }
/*     */ 
/*     */   public List<TCM> getTcm() {
/* 565 */     if (this.tcm == null) {
/* 566 */       this.tcm = new ArrayList();
/*     */     }
/* 568 */     return this.tcm;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.DrawableElement
 * JD-Core Version:    0.6.2
 */