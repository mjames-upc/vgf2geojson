/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.Geometry;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.MultiPolygon;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.common.staticdata.SPCCounty;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchBoxAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchInfoDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.List;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.geotools.factory.CommonFactoryFinder;
/*     */ import org.geotools.factory.GeoTools;
/*     */ import org.geotools.feature.FeatureCollection;
/*     */ import org.geotools.feature.FeatureCollections;
/*     */ import org.geotools.feature.FeatureIterator;
/*     */ import org.geotools.feature.simple.SimpleFeatureBuilder;
/*     */ import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
/*     */ import org.geotools.referencing.crs.DefaultGeographicCRS;
/*     */ import org.opengis.feature.simple.SimpleFeature;
/*     */ import org.opengis.feature.simple.SimpleFeatureType;
/*     */ import org.opengis.filter.Filter;
/*     */ import org.opengis.filter.FilterFactory2;
/*     */ 
/*     */ public class PgenWatchBoxAddDelCntyHandler extends InputHandlerDefaultImpl
/*     */ {
/*     */   private AbstractEditor mapEditor;
/*     */   private PgenResource drawingLayer;
/*     */   private WatchBox wb;
/*     */   private PgenWatchBoxModifyTool wbTool;
/*     */   static FeatureCollection<SimpleFeatureType, SimpleFeature> counties;
/*     */ 
/*     */   public PgenWatchBoxAddDelCntyHandler(AbstractEditor mapEditor, PgenResource drawingLayer, WatchBox wb, PgenWatchBoxModifyTool tool)
/*     */   {
/*  75 */     this.mapEditor = mapEditor;
/*  76 */     this.drawingLayer = drawingLayer;
/*  77 */     this.wb = wb;
/*  78 */     this.wbTool = tool;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/*  91 */     if ((!this.drawingLayer.isEditable()) || (this.shiftDown)) return false;
/*     */ 
/*  94 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/*  95 */     if (loc == null) return false;
/*     */ 
/*  97 */     if (button == 1)
/*     */     {
/* 100 */       if (counties == null) {
/* 101 */         createCountyFeatureCollection();
/*     */       }
/*     */ 
/* 104 */       GeometryFactory gf = new GeometryFactory();
/* 105 */       Point click = gf.createPoint(loc);
/*     */ 
/* 108 */       FeatureCollection fc = counties.subCollection(createFilter(click));
/*     */ 
/* 110 */       FeatureIterator featureIterator = fc.features();
/*     */ 
/* 113 */       String ugc = null;
/* 114 */       while (featureIterator.hasNext())
/*     */       {
/* 116 */         SimpleFeature f = (SimpleFeature)featureIterator.next();
/* 117 */         MultiPolygon mp = (MultiPolygon)f.getAttribute("Location");
/*     */ 
/* 119 */         if (mp.contains(click)) {
/* 120 */           ugc = f.getID();
/* 121 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 125 */       boolean gotCnty = false;
/* 126 */       SPCCounty county = null;
/*     */ 
/* 129 */       if (ugc != null)
/*     */       {
/* 131 */         List cntyTbl = PgenStaticDataProvider.getProvider().getSPCCounties();
/*     */ 
/* 133 */         for (SPCCounty cnty : cntyTbl) {
/* 134 */           if (ugc.equalsIgnoreCase(cnty.getUgcId())) {
/* 135 */             gotCnty = true;
/* 136 */             county = cnty;
/* 137 */             break;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 143 */       boolean inList = false;
/*     */ 
/* 147 */       if (gotCnty)
/*     */       {
/* 149 */         for (SPCCounty cnty : this.wb.getCountyList()) {
/* 150 */           if (ugc.equalsIgnoreCase(cnty.getUgcId())) {
/* 151 */             inList = true;
/* 152 */             break;
/*     */           }
/*     */         }
/*     */ 
/* 156 */         WatchBox newWb = (WatchBox)this.wb.copy();
/* 157 */         newWb.setCountyList(this.wb.getCountyList());
/*     */ 
/* 159 */         if (inList) {
/* 160 */           if (((WatchBoxAttrDlg)this.wbTool.attrDlg).getWatchInfoDlg().isClusteringOn()) {
/* 161 */             newWb.rmClstCnty(county);
/*     */           }
/*     */           else {
/* 164 */             newWb.removeCounty(county);
/*     */           }
/*     */ 
/*     */         }
/* 168 */         else if (((WatchBoxAttrDlg)this.wbTool.attrDlg).getWatchInfoDlg().isClusteringOn()) {
/* 169 */           newWb.addClstCnty(county);
/*     */         }
/*     */         else {
/* 172 */           newWb.addCounty(county);
/*     */         }
/*     */ 
/* 176 */         this.drawingLayer.replaceElement(this.wb, newWb);
/* 177 */         this.drawingLayer.setSelected(newWb);
/*     */ 
/* 179 */         this.wb = newWb;
/*     */ 
/* 181 */         ((WatchBoxAttrDlg)this.wbTool.attrDlg).setWatchBox(newWb);
/* 182 */         ((WatchBoxAttrDlg)this.wbTool.attrDlg).getWatchInfoDlg().clearCwaPane();
/* 183 */         ((WatchBoxAttrDlg)this.wbTool.attrDlg).getWatchInfoDlg().createCWAs(newWb.getWFOs());
/* 184 */         ((WatchBoxAttrDlg)this.wbTool.attrDlg).getWatchInfoDlg().setCwaBtns();
/* 185 */         ((WatchBoxAttrDlg)this.wbTool.attrDlg).getWatchInfoDlg().setStatesWFOs();
/*     */ 
/* 187 */         ((WatchBoxAttrDlg)this.wbTool.attrDlg).getWatchInfoDlg().enableAllButtons(false);
/*     */ 
/* 189 */         this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 192 */       return false;
/*     */     }
/*     */ 
/* 195 */     if (button == 3)
/*     */     {
/* 197 */       this.wbTool.resetMouseHandler();
/* 198 */       ((WatchBoxAttrDlg)this.wbTool.attrDlg).getWatchInfoDlg().enableAllButtons(true);
/* 199 */       ((WatchBoxAttrDlg)this.wbTool.attrDlg).enableDspBtn(true);
/* 200 */       ((WatchBoxAttrDlg)this.wbTool.attrDlg).buttonBar.setEnabled(true);
/*     */ 
/* 202 */       return true;
/*     */     }
/*     */ 
/* 207 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */   {
/* 215 */     if ((!this.drawingLayer.isEditable()) || (this.shiftDown)) return false;
/* 216 */     return true;
/*     */   }
/*     */ 
/*     */   private void createCountyFeatureCollection()
/*     */   {
/* 224 */     counties = FeatureCollections.newCollection();
/*     */ 
/* 227 */     SimpleFeatureTypeBuilder builder2 = new SimpleFeatureTypeBuilder();
/* 228 */     builder2.setName("cntyGeometry");
/* 229 */     builder2.setCRS(DefaultGeographicCRS.WGS84);
/* 230 */     builder2.add("Location", MultiPolygon.class);
/* 231 */     builder2.add("cntyName", String.class);
/*     */ 
/* 233 */     SimpleFeatureType POLY = builder2.buildFeatureType();
/*     */ 
/* 235 */     SimpleFeatureBuilder fbuild = new SimpleFeatureBuilder(POLY);
/*     */ 
/* 238 */     List cntyTbl = PgenStaticDataProvider.getProvider().getSPCCounties();
/* 239 */     for (SPCCounty cnty : cntyTbl)
/* 240 */       if ((cnty.getShape() != null) && (cnty.getName() != null)) {
/* 241 */         Geometry countyGeo = cnty.getShape();
/* 242 */         if (countyGeo != null)
/*     */         {
/* 244 */           fbuild.add(countyGeo);
/* 245 */           fbuild.add(cnty.getName());
/*     */ 
/* 247 */           SimpleFeature feature = fbuild.buildFeature(cnty.getUgcId());
/* 248 */           counties.add(feature);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private Filter createFilter(Point loc)
/*     */   {
/* 262 */     FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(
/* 263 */       GeoTools.getDefaultHints());
/*     */ 
/* 265 */     Filter filter = ff.contains(ff.property(
/* 266 */       "Location"), ff.literal(loc));
/*     */ 
/* 268 */     return filter;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenWatchBoxAddDelCntyHandler
 * JD-Core Version:    0.6.2
 */