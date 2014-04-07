/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.common.dataquery.requests.DbQueryRequest;
/*     */ import com.raytheon.uf.common.dataquery.requests.RequestConstraint;
/*     */ import com.raytheon.uf.common.dataquery.requests.RequestConstraint.ConstraintType;
/*     */ import com.raytheon.uf.common.dataquery.responses.DbQueryResponse;
/*     */ import com.raytheon.uf.viz.core.requests.ThriftClient;
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import com.vividsolutions.jts.geom.GeometryFactory;
/*     */ import com.vividsolutions.jts.geom.Point;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.PgenRecord;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TcaAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.BPGeography;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.Basin;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.Breakpoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.BreakpointFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.BreakpointManager;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.BreakpointPair;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.StormAdvisoryNumber;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.TCAElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.TCVMessage;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tca.TropicalCycloneAdvisory;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ 
/*     */ public class PgenTcaTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   private static final String TCA_TYPE = "TCA";
/*  82 */   private DrawingMode mode = DrawingMode.IDLE;
/*     */ 
/*  84 */   private final double TOL = 0.5D;
/*     */   private static final String tcaFileFormat = "tca_%s%02d%04d_%03d.vgf.xml";
/*     */   private static final String intTcaFileFormat = "tca_%s%02d%04d_%03d%s.vgf.xml";
/*  90 */   private BreakpointManager bmgr = null;
/*     */   private TcaAttrDlg tcaDlg;
/* 100 */   private TCAElement elem = null;
/*     */ 
/* 102 */   private TropicalCycloneAdvisory selectedAdvisory = null;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/* 118 */     super.activateTool();
/*     */ 
/* 120 */     this.bmgr = BreakpointManager.getInstance();
/*     */ 
/* 127 */     this.elem = null;
/* 128 */     if ((this.event.getTrigger() instanceof TCAElement)) {
/* 129 */       this.elem = ((TCAElement)this.event.getTrigger());
/*     */     }
/* 131 */     if ((this.attrDlg instanceof TcaAttrDlg)) {
/* 132 */       this.tcaDlg = ((TcaAttrDlg)this.attrDlg);
/* 133 */       this.tcaDlg.setTcaTool(this);
/* 134 */       if (this.elem != null) {
/* 135 */         this.attrDlg.setAttrForDlg(this.elem);
/*     */       }
/*     */     }
/*     */ 
/* 139 */     this.mode = DrawingMode.IDLE;
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/* 147 */     super.deactivateTool();
/* 148 */     if (this.drawingLayer != null)
/* 149 */       this.drawingLayer.setDefaultPtsSelectedColor();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 160 */     if (this.mouseHandler == null)
/*     */     {
/* 166 */       this.mouseHandler = new PgenTcaHandler();
/*     */     }
/*     */ 
/* 170 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public void setSingleMode()
/*     */   {
/* 638 */     this.mode = DrawingMode.NEW_SINGLE;
/*     */   }
/*     */ 
/*     */   public void setPairMode()
/*     */   {
/* 645 */     this.mode = DrawingMode.NEW_PAIR;
/*     */   }
/*     */ 
/*     */   public void updateTcaElement()
/*     */   {
/* 653 */     if (this.elem == null)
/*     */     {
/* 658 */       DrawableElementFactory def = new DrawableElementFactory();
/* 659 */       Coordinate dummy = new Coordinate(0.0D, 0.0D);
/*     */ 
/* 661 */       this.elem = ((TCAElement)def.create(DrawableType.TCA, this.tcaDlg, 
/* 662 */         this.pgenCategory, this.pgenType, dummy, 
/* 663 */         this.drawingLayer.getActiveLayer()));
/* 664 */       this.drawingLayer.addElement(this.elem);
/*     */     }
/*     */     else
/*     */     {
/* 671 */       TCAElement newElem = (TCAElement)this.elem.copy();
/* 672 */       newElem.update(this.tcaDlg);
/* 673 */       this.drawingLayer.replaceElement(this.elem, newElem);
/* 674 */       this.elem = newElem;
/*     */     }
/* 676 */     this.drawingLayer.setSelected(this.elem);
/*     */ 
/* 678 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   public void advisoryDeleted()
/*     */   {
/* 687 */     deselectAdvisory();
/* 688 */     updateTcaElement();
/*     */   }
/*     */ 
/*     */   public void selectAdvisory(int index)
/*     */   {
/* 696 */     selectAdvisory((TropicalCycloneAdvisory)this.elem.getAdvisories().get(index));
/* 697 */     this.mode = DrawingMode.HIGHLIGHT;
/*     */   }
/*     */ 
/*     */   public void selectAdvisory(TropicalCycloneAdvisory adv)
/*     */   {
/* 705 */     this.selectedAdvisory = adv;
/* 706 */     this.drawingLayer.removePtsSelected();
/* 707 */     this.drawingLayer.setPtsSelectedColor(Color.WHITE);
/* 708 */     List plist = this.elem.getPoints();
/*     */     Iterator localIterator2;
/* 709 */     for (Iterator localIterator1 = adv.getSegment().getBreakpoints().iterator(); localIterator1.hasNext(); 
/* 710 */       localIterator2.hasNext())
/*     */     {
/* 709 */       Breakpoint bp = (Breakpoint)localIterator1.next();
/* 710 */       localIterator2 = plist.iterator(); continue; Coordinate c = (Coordinate)localIterator2.next();
/* 711 */       if (bp.getLocation().equals2D(c))
/* 712 */         this.drawingLayer.addPtSelected(plist.indexOf(c));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deselectAdvisory()
/*     */   {
/* 722 */     this.selectedAdvisory = null;
/* 723 */     this.drawingLayer.removePtsSelected();
/* 724 */     this.drawingLayer.setDefaultPtsSelectedColor();
/* 725 */     this.mode = DrawingMode.IDLE;
/*     */   }
/*     */ 
/*     */   public String saveAdvisory()
/*     */   {
/* 738 */     updateTcaElement();
/*     */ 
/* 740 */     String label = generateFilename();
/*     */ 
/* 742 */     Layer defaultLayer = new Layer();
/* 743 */     defaultLayer.addElement(this.elem);
/* 744 */     ArrayList layerList = new ArrayList();
/* 745 */     layerList.add(defaultLayer);
/*     */ 
/* 747 */     String forecaster = System.getProperty("user.name");
/*     */ 
/* 750 */     ProductTime refTime = new ProductTime(this.elem.getAdvisoryTime());
/*     */ 
/* 752 */     Product defaultProduct = new Product(this.elem.getStormName(), "TCA", 
/* 753 */       forecaster, null, refTime, layerList);
/*     */ 
/* 755 */     defaultProduct.setOutputFile(label);
/* 756 */     defaultProduct.setCenter(PgenUtil.getCurrentOffice());
/*     */     try
/*     */     {
/* 759 */       dataURI = StorageUtils.storeProduct(defaultProduct, true);
/*     */     }
/*     */     catch (PgenStorageException e)
/*     */     {
/*     */       String dataURI;
/* 761 */       StorageUtils.showError(e);
/* 762 */       return null;
/*     */     }
/*     */     String dataURI;
/* 765 */     return dataURI;
/*     */   }
/*     */ 
/*     */   private String generateFilename()
/*     */   {
/* 795 */     String filename = null;
/* 796 */     String advnum = this.elem.getAdvisoryNumber();
/* 797 */     String basin = Basin.getBasinAbbrev(this.elem.getBasin());
/*     */ 
/* 799 */     if (StormAdvisoryNumber.isIntermediate(advnum))
/* 800 */       filename = String.format("tca_%s%02d%04d_%03d%s.vgf.xml", new Object[] { basin, 
/* 801 */         Integer.valueOf(this.elem.getStormNumber()), 
/* 802 */         Integer.valueOf(this.elem.getAdvisoryTime().get(1)), 
/* 803 */         Integer.valueOf(StormAdvisoryNumber.getRegularAdvisory(advnum)), 
/* 804 */         advnum.substring(advnum.length() - 1) });
/*     */     else {
/* 806 */       filename = String.format("tca_%s%02d%04d_%03d.vgf.xml", new Object[] { basin, 
/* 807 */         Integer.valueOf(this.elem.getStormNumber()), 
/* 808 */         Integer.valueOf(this.elem.getAdvisoryTime().get(1)), 
/* 809 */         Integer.valueOf(StormAdvisoryNumber.getRegularAdvisory(advnum)) });
/*     */     }
/* 811 */     return filename;
/*     */   }
/*     */ 
/*     */   public String createTCV()
/*     */   {
/* 826 */     TCAElement prev = getPreviousAdvisory();
/* 827 */     String office = PgenUtil.getCurrentOffice();
/*     */     TCVMessage tcv;
/*     */     TCVMessage tcv;
/* 830 */     if (prev == null)
/* 831 */       tcv = new TCVMessage(office, this.elem);
/*     */     else {
/* 833 */       tcv = new TCVMessage(office, prev, this.elem);
/*     */     }
/*     */ 
/* 836 */     return tcv.createText();
/*     */   }
/*     */ 
/*     */   private TCAElement getPreviousAdvisory()
/*     */   {
/* 846 */     TCAElement previous = null;
/* 847 */     String dataURI = findPreviousActivity();
/* 848 */     if (dataURI == null) {
/* 849 */       return null;
/*     */     }
/*     */ 
/* 853 */     List prds = null;
/*     */     try {
/* 855 */       prds = StorageUtils.retrieveProduct(dataURI);
/*     */     } catch (PgenStorageException e) {
/* 857 */       StorageUtils.showError(e);
/*     */     }
/*     */     Iterator localIterator2;
/* 859 */     for (Iterator localIterator1 = prds.iterator(); localIterator1.hasNext(); 
/* 860 */       localIterator2.hasNext())
/*     */     {
/* 859 */       Product p = (Product)localIterator1.next();
/* 860 */       localIterator2 = p.getLayers().iterator(); continue; Layer l = (Layer)localIterator2.next();
/* 861 */       for (AbstractDrawableComponent de : l.getDrawables()) {
/* 862 */         if ((de instanceof TCAElement)) {
/* 863 */           previous = (TCAElement)de;
/* 864 */           break;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 870 */     return previous;
/*     */   }
/*     */ 
/*     */   private Map<String, String> getTcaActivityMap()
/*     */   {
/* 875 */     Map tcaMap = new HashMap();
/*     */ 
/* 877 */     DbQueryRequest request = new DbQueryRequest();
/* 878 */     request.setEntityClass(PgenRecord.class.getName());
/* 879 */     request.addRequestField("activityLabel");
/* 880 */     request.addRequestField("dataURI");
/* 881 */     request.addConstraint("activityType", new RequestConstraint(
/* 882 */       "TCA", RequestConstraint.ConstraintType.EQUALS));
/*     */     try
/*     */     {
/* 886 */       DbQueryResponse response = (DbQueryResponse)ThriftClient.sendRequest(request);
/* 887 */       for (Map result : response.getResults()) {
/* 888 */         String label = (String)result.get("activityLabel");
/* 889 */         String dataURI = (String)result.get("dataURI");
/* 890 */         tcaMap.put(label, dataURI);
/*     */       }
/*     */     } catch (Exception e) {
/* 893 */       StorageUtils.showError(e);
/*     */     }
/*     */ 
/* 896 */     return tcaMap;
/*     */   }
/*     */ 
/*     */   private String findPreviousActivity()
/*     */   {
/* 906 */     Map tcaMap = getTcaActivityMap();
/*     */ 
/* 909 */     String basin = Basin.getBasinAbbrev(this.elem.getBasin());
/* 910 */     int stormNum = this.elem.getStormNumber();
/* 911 */     int year = this.elem.getAdvisoryTime().get(1);
/* 912 */     String advno = this.elem.getAdvisoryNumber();
/* 913 */     int regnum = StormAdvisoryNumber.getRegularAdvisory(advno);
/*     */ 
/* 915 */     if (!StormAdvisoryNumber.isIntermediate(advno))
/*     */     {
/* 917 */       String label = String.format("tca_%s%02d%04d_%03d%s.vgf.xml", new Object[] { basin, Integer.valueOf(stormNum), Integer.valueOf(year), 
/* 918 */         Integer.valueOf(regnum - 1), "b" });
/* 919 */       if (tcaMap.containsKey(label)) {
/* 920 */         return (String)tcaMap.get(label);
/*     */       }
/* 922 */       label = String.format("tca_%s%02d%04d_%03d%s.vgf.xml", new Object[] { basin, Integer.valueOf(stormNum), Integer.valueOf(year), 
/* 923 */         Integer.valueOf(regnum - 1), "a" });
/* 924 */       if (tcaMap.containsKey(label)) {
/* 925 */         return (String)tcaMap.get(label);
/*     */       }
/* 927 */       label = String.format("tca_%s%02d%04d_%03d.vgf.xml", new Object[] { basin, Integer.valueOf(stormNum), Integer.valueOf(year), 
/* 928 */         Integer.valueOf(regnum - 1) });
/* 929 */       if (tcaMap.containsKey(label))
/* 930 */         return (String)tcaMap.get(label);
/*     */     }
/* 932 */     else if (advno.endsWith("a"))
/*     */     {
/* 934 */       String label = String.format("tca_%s%02d%04d_%03d.vgf.xml", new Object[] { basin, Integer.valueOf(stormNum), Integer.valueOf(year), Integer.valueOf(regnum) });
/* 935 */       if (tcaMap.containsKey(label))
/* 936 */         return (String)tcaMap.get(label);
/*     */     }
/* 938 */     else if (advno.endsWith("b"))
/*     */     {
/* 940 */       String label = String.format("tca_%s%02d%04d_%03d%s.vgf.xml", new Object[] { basin, Integer.valueOf(stormNum), Integer.valueOf(year), 
/* 941 */         Integer.valueOf(regnum), "a" });
/* 942 */       if (tcaMap.containsKey(label)) {
/* 943 */         return (String)tcaMap.get(label);
/*     */       }
/* 945 */       label = String.format("tca_%s%02d%04d_%03d.vgf.xml", new Object[] { basin, Integer.valueOf(stormNum), Integer.valueOf(year), Integer.valueOf(regnum) });
/* 946 */       if (tcaMap.containsKey(label)) {
/* 947 */         return (String)tcaMap.get(label);
/*     */       }
/*     */     }
/*     */ 
/* 951 */     return null;
/*     */   }
/*     */ 
/*     */   private static enum DrawingMode
/*     */   {
/*  79 */     IDLE, HIGHLIGHT, MODIFY, NEW_SINGLE, NEW_PAIR, SELECT_2ND;
/*     */   }
/*     */ 
/*     */   public class PgenTcaHandler extends InputHandlerDefaultImpl
/*     */   {
/* 186 */     private DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/* 188 */     private Symbol DOT = new Symbol(null, new Color[] { Color.WHITE }, 
/* 189 */       1.0F, 7.5D, Boolean.valueOf(false), null, "Marker", "DOT");
/*     */ 
/* 191 */     private Symbol firstPt = null;
/*     */ 
/* 193 */     private Breakpoint bkpt1 = null;
/*     */ 
/* 195 */     private Breakpoint bkpt2 = null;
/*     */     private boolean keepFirst;
/* 199 */     private DECollection ghost = new DECollection();
/*     */ 
/* 201 */     private final String BKPT_TYPE_OFFICIAL = "Official";
/*     */ 
/*     */     public PgenTcaHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 211 */       if (!PgenTcaTool.this.isResourceEditable()) {
/* 212 */         return false;
/*     */       }
/*     */ 
/* 220 */       Coordinate loc = PgenTcaTool.this.mapEditor.translateClick(anX, aY);
/* 221 */       if ((loc == null) || (this.shiftDown)) {
/* 222 */         return false;
/*     */       }
/*     */ 
/* 225 */       String geog = PgenTcaTool.this.tcaDlg.getGeogType();
/*     */ 
/* 232 */       BreakpointFilter filter = new BreakpointFilter();
/* 233 */       if (PgenTcaTool.this.tcaDlg.getBreakpointType().equals("Official")) {
/* 234 */         filter.setOfficialOnly();
/*     */       }
/*     */ 
/* 237 */       if (button == 1)
/*     */       {
/* 239 */         switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenTcaTool$DrawingMode()[PgenTcaTool.this.mode.ordinal()])
/*     */         {
/*     */         case 1:
/* 247 */           if ((PgenTcaTool.this.elem != null) && (!PgenTcaTool.this.elem.getAdvisories().isEmpty())) {
/* 248 */             TropicalCycloneAdvisory adv = PgenTcaTool.this.elem
/* 249 */               .findClosestAdvisory(loc);
/* 250 */             if (adv != null) {
/* 251 */               PgenTcaTool.this.selectAdvisory(adv);
/* 252 */               PgenTcaTool.this.tcaDlg.selectAdvisory(PgenTcaTool.this.elem.getAdvisories().indexOf(
/* 253 */                 adv));
/* 254 */               PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.HIGHLIGHT;
/*     */             }
/*     */           }
/* 257 */           break;
/*     */         case 2:
/* 269 */           if ((PgenTcaTool.this.selectedAdvisory.getSegment() instanceof BreakpointPair)) {
/* 270 */             GeometryFactory gf = new GeometryFactory();
/* 271 */             Point pt1 = gf.createPoint(
/* 272 */               ((Breakpoint)PgenTcaTool.this.selectedAdvisory
/* 272 */               .getSegment().getBreakpoints().get(0))
/* 273 */               .getLocation());
/* 274 */             Point pt2 = gf.createPoint(
/* 275 */               ((Breakpoint)PgenTcaTool.this.selectedAdvisory
/* 275 */               .getSegment().getBreakpoints().get(1))
/* 276 */               .getLocation());
/* 277 */             Point mouse = gf.createPoint(loc);
/* 278 */             double dist1 = mouse.distance(pt1);
/* 279 */             double dist2 = mouse.distance(pt2);
/*     */ 
/* 281 */             if (dist1 < dist2) {
/* 282 */               if (dist1 < 0.5D) {
/* 283 */                 this.bkpt1 = 
/* 284 */                   ((Breakpoint)PgenTcaTool.this.selectedAdvisory.getSegment()
/* 284 */                   .getBreakpoints().get(1));
/* 285 */                 this.keepFirst = false;
/* 286 */                 PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.MODIFY;
/*     */               }
/*     */             }
/* 289 */             else if (dist2 < 0.5D) {
/* 290 */               this.bkpt1 = 
/* 291 */                 ((Breakpoint)PgenTcaTool.this.selectedAdvisory.getSegment()
/* 291 */                 .getBreakpoints().get(0));
/* 292 */               this.keepFirst = true;
/* 293 */               PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.MODIFY;
/*     */             }
/*     */           }
/*     */ 
/* 297 */           break;
/*     */         case 4:
/* 305 */           BPGeography segment = null;
/*     */ 
/* 307 */           if (geog.equals("Islands"))
/*     */           {
/* 309 */             segment = PgenTcaTool.this.bmgr.getNearestIsland(loc);
/* 310 */           } else if (geog.equals("Water"))
/*     */           {
/* 312 */             segment = PgenTcaTool.this.bmgr.getNearestWaterway(loc);
/*     */           }
/*     */ 
/* 315 */           if (segment != null)
/*     */           {
/* 319 */             TropicalCycloneAdvisory adv = new TropicalCycloneAdvisory(
/* 320 */               PgenTcaTool.this.tcaDlg.getSeverity(), PgenTcaTool.this.tcaDlg.getAdvisoryType(), 
/* 321 */               geog, segment);
/* 322 */             PgenTcaTool.this.tcaDlg.addAdvisory(adv);
/* 323 */             PgenTcaTool.this.updateTcaElement();
/*     */           }
/*     */ 
/* 326 */           break;
/*     */         case 5:
/* 334 */           this.bkpt1 = PgenTcaTool.this.bmgr.getNearestBreakpoint(loc, filter);
/* 335 */           if (this.bkpt1 != null)
/*     */           {
/* 337 */             this.firstPt = this.DOT;
/* 338 */             this.firstPt.setLocation(this.bkpt1.getLocation());
/* 339 */             PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.SELECT_2ND;
/* 340 */           }break;
/*     */         case 6:
/* 351 */           String coast = PgenTcaTool.this.bmgr.findCoastName(this.bkpt1);
/* 352 */           boolean isIsland = PgenTcaTool.this.bmgr.isCoastIsland(coast);
/* 353 */           filter.filterCoastName(coast);
/* 354 */           this.bkpt2 = PgenTcaTool.this.bmgr.getNearestBreakpoint(loc, filter);
/* 355 */           if (this.bkpt2 != null)
/*     */           {
/* 357 */             if ((!this.bkpt1.equals(this.bkpt2)) || (isIsland))
/*     */             {
/* 359 */               BreakpointPair bkptPair = PgenTcaTool.this.bmgr.getBreakpointPair(this.bkpt1, 
/* 360 */                 this.bkpt2);
/* 361 */               if (bkptPair != null) {
/* 362 */                 TropicalCycloneAdvisory adv = new TropicalCycloneAdvisory(
/* 363 */                   PgenTcaTool.this.tcaDlg.getSeverity(), PgenTcaTool.this.tcaDlg.getAdvisoryType(), 
/* 364 */                   geog, bkptPair);
/* 365 */                 PgenTcaTool.this.tcaDlg.addAdvisory(adv);
/* 366 */                 PgenTcaTool.this.updateTcaElement();
/*     */               }
/*     */ 
/* 369 */               this.firstPt = null;
/* 370 */               PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.IDLE; } 
/* 371 */           }break;
/*     */         case 3:
/* 380 */           String coast = PgenTcaTool.this.bmgr.findCoastName(this.bkpt1);
/* 381 */           boolean isIsland = PgenTcaTool.this.bmgr.isCoastIsland(coast);
/* 382 */           filter.filterCoastName(coast);
/* 383 */           this.bkpt2 = PgenTcaTool.this.bmgr.getNearestBreakpoint(loc, filter);
/* 384 */           if (this.bkpt2 != null)
/*     */           {
/* 386 */             if ((!this.bkpt1.equals(this.bkpt2)) || (isIsland))
/*     */             {
/* 388 */               BreakpointPair bkptmodPair = null;
/* 389 */               if (this.keepFirst)
/* 390 */                 bkptmodPair = PgenTcaTool.this.bmgr.getBreakpointPair(this.bkpt1, this.bkpt2);
/*     */               else
/* 392 */                 bkptmodPair = PgenTcaTool.this.bmgr.getBreakpointPair(this.bkpt2, this.bkpt1);
/* 393 */               if (bkptmodPair != null) {
/* 394 */                 TropicalCycloneAdvisory adv = new TropicalCycloneAdvisory(
/* 395 */                   PgenTcaTool.this.tcaDlg.getSeverity(), PgenTcaTool.this.tcaDlg.getAdvisoryType(), 
/* 396 */                   geog, bkptmodPair);
/* 397 */                 int idx = PgenTcaTool.this.elem.getAdvisories()
/* 398 */                   .indexOf(PgenTcaTool.this.selectedAdvisory);
/* 399 */                 PgenTcaTool.this.tcaDlg.replaceAdvisory(idx, adv);
/* 400 */                 PgenTcaTool.this.updateTcaElement();
/*     */ 
/* 402 */                 PgenTcaTool.this.selectAdvisory(idx);
/*     */               }
/* 404 */               this.firstPt = null;
/* 405 */               PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.HIGHLIGHT;
/*     */             }
/*     */           }
/*     */           break;
/*     */         }
/* 410 */         return false;
/*     */       }
/*     */ 
/* 414 */       if (button == 3)
/*     */       {
/* 416 */         switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenTcaTool$DrawingMode()[PgenTcaTool.this.mode.ordinal()])
/*     */         {
/*     */         case 1:
/* 422 */           PgenTcaTool.this.drawingLayer.removeSelected();
/* 423 */           PgenUtil.setSelectingMode();
/* 424 */           break;
/*     */         case 2:
/* 430 */           PgenTcaTool.this.tcaDlg.deselectAdvisory();
/* 431 */           PgenTcaTool.this.deselectAdvisory();
/* 432 */           PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.IDLE;
/* 433 */           break;
/*     */         case 3:
/* 436 */           PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.HIGHLIGHT;
/* 437 */           break;
/*     */         case 4:
/* 440 */           PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.IDLE;
/* 441 */           break;
/*     */         case 5:
/* 444 */           PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.IDLE;
/* 445 */           break;
/*     */         case 6:
/* 452 */           this.bkpt1 = null;
/* 453 */           this.firstPt = null;
/* 454 */           PgenTcaTool.this.mode = PgenTcaTool.DrawingMode.NEW_PAIR;
/*     */         }
/*     */ 
/* 458 */         return true;
/*     */       }
/*     */ 
/* 461 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 473 */       if (!PgenTcaTool.this.isResourceEditable()) {
/* 474 */         return false;
/*     */       }
/*     */ 
/* 480 */       Coordinate loc = PgenTcaTool.this.mapEditor.translateClick(x, y);
/* 481 */       if (loc == null) {
/* 482 */         return false;
/*     */       }
/* 484 */       if (PgenTcaTool.this.attrDlg != null)
/*     */       {
/* 486 */         this.ghost.clear();
/*     */ 
/* 489 */         String geog = PgenTcaTool.this.tcaDlg.getGeogType();
/*     */ 
/* 496 */         BreakpointFilter filter = new BreakpointFilter();
/* 497 */         if (PgenTcaTool.this.tcaDlg.getBreakpointType().equals("Official")) {
/* 498 */           filter.setOfficialOnly();
/*     */         }
/* 500 */         switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$tools$PgenTcaTool$DrawingMode()[PgenTcaTool.this.mode.ordinal()])
/*     */         {
/*     */         case 4:
/* 507 */           BPGeography bkptinfo = null;
/* 508 */           if (geog.equals("Islands"))
/* 509 */             bkptinfo = PgenTcaTool.this.bmgr.getNearestIsland(loc);
/* 510 */           else if (geog.equals("Water"))
/* 511 */             bkptinfo = PgenTcaTool.this.bmgr.getNearestWaterway(loc);
/* 512 */           if (bkptinfo != null) {
/* 513 */             String name = ((Breakpoint)bkptinfo.getBreakpoints().get(0))
/* 514 */               .getName();
/* 515 */             this.ghost.add(new Text(null, "Courier", 16.0F, 
/* 516 */               IText.TextJustification.LEFT_JUSTIFY, loc, 0.0D, 
/* 517 */               IText.TextRotation.SCREEN_RELATIVE, 
/* 518 */               new String[] { name }, IText.FontStyle.BOLD, 
/* 519 */               Color.YELLOW, 0, 4, true, IText.DisplayType.NORMAL, 
/* 520 */               "TEXT", "General Text"));
/*     */           }
/* 522 */           break;
/*     */         case 5:
/* 528 */           Breakpoint bkpt = PgenTcaTool.this.bmgr.getNearestBreakpoint(loc, filter);
/* 529 */           if (bkpt != null) {
/* 530 */             String name = bkpt.getName();
/* 531 */             this.ghost.add(new Text(null, "Courier", 16.0F, 
/* 532 */               IText.TextJustification.LEFT_JUSTIFY, loc, 0.0D, 
/* 533 */               IText.TextRotation.SCREEN_RELATIVE, 
/* 534 */               new String[] { name }, IText.FontStyle.BOLD, 
/* 535 */               Color.YELLOW, 0, 1, true, IText.DisplayType.NORMAL, 
/* 536 */               "TEXT", "General Text"));
/*     */           }
/*     */ 
/* 539 */           break;
/*     */         case 6:
/* 550 */           String coast = PgenTcaTool.this.bmgr.findCoastName(this.bkpt1);
/* 551 */           boolean isIsland = PgenTcaTool.this.bmgr.isCoastIsland(coast);
/* 552 */           filter.filterCoastName(coast);
/* 553 */           Breakpoint bkpttmp = PgenTcaTool.this.bmgr.getNearestBreakpoint(loc, filter);
/* 554 */           if ((!this.bkpt1.equals(bkpttmp)) || (isIsland))
/*     */           {
/* 556 */             if (bkpttmp != null) {
/* 557 */               String name = bkpttmp.getName();
/* 558 */               this.ghost.add(new Text(null, "Courier", 16.0F, 
/* 559 */                 IText.TextJustification.LEFT_JUSTIFY, loc, 0.0D, 
/* 560 */                 IText.TextRotation.SCREEN_RELATIVE, 
/* 561 */                 new String[] { name }, IText.FontStyle.BOLD, 
/* 562 */                 Color.YELLOW, 0, 1, true, IText.DisplayType.NORMAL, 
/* 563 */                 "TEXT", "General Text"));
/* 564 */               TCAElement tcaTemp = (TCAElement)this.def.create(
/* 565 */                 DrawableType.TCA, PgenTcaTool.this.tcaDlg, PgenTcaTool.this.pgenCategory, 
/* 566 */                 PgenTcaTool.this.pgenType, loc, PgenTcaTool.this.drawingLayer.getActiveLayer());
/* 567 */               BreakpointPair bp = PgenTcaTool.this.bmgr.getBreakpointPair(this.bkpt1, 
/* 568 */                 bkpttmp);
/* 569 */               TropicalCycloneAdvisory adv = new TropicalCycloneAdvisory(
/* 570 */                 PgenTcaTool.this.tcaDlg.getSeverity(), PgenTcaTool.this.tcaDlg.getAdvisoryType(), 
/* 571 */                 geog, bp);
/* 572 */               tcaTemp.addAdvisory(adv);
/* 573 */               this.ghost.add(tcaTemp);
/*     */             }
/*     */           }
/* 576 */           break;
/*     */         case 3:
/* 587 */           String coast = PgenTcaTool.this.bmgr.findCoastName(this.bkpt1);
/* 588 */           boolean isIsland = PgenTcaTool.this.bmgr.isCoastIsland(coast);
/* 589 */           filter.filterCoastName(coast);
/* 590 */           Breakpoint bkptmod = PgenTcaTool.this.bmgr.getNearestBreakpoint(loc, filter);
/* 591 */           if ((!this.bkpt1.equals(bkptmod)) || (isIsland))
/*     */           {
/* 593 */             if (bkptmod != null) {
/* 594 */               String name = bkptmod.getName();
/* 595 */               this.ghost.add(new Text(null, "Courier", 16.0F, 
/* 596 */                 IText.TextJustification.LEFT_JUSTIFY, loc, 0.0D, 
/* 597 */                 IText.TextRotation.SCREEN_RELATIVE, 
/* 598 */                 new String[] { name }, IText.FontStyle.BOLD, 
/* 599 */                 Color.YELLOW, 0, 1, true, IText.DisplayType.NORMAL, 
/* 600 */                 "TEXT", "General Text"));
/* 601 */               TCAElement tcaTemp = (TCAElement)this.def.create(
/* 602 */                 DrawableType.TCA, PgenTcaTool.this.tcaDlg, PgenTcaTool.this.pgenCategory, 
/* 603 */                 PgenTcaTool.this.pgenType, loc, PgenTcaTool.this.drawingLayer.getActiveLayer());
/* 604 */               BreakpointPair bp = null;
/* 605 */               if (this.keepFirst)
/* 606 */                 bp = PgenTcaTool.this.bmgr.getBreakpointPair(this.bkpt1, bkptmod);
/*     */               else
/* 608 */                 bp = PgenTcaTool.this.bmgr.getBreakpointPair(bkptmod, this.bkpt1);
/* 609 */               TropicalCycloneAdvisory adv = new TropicalCycloneAdvisory(
/* 610 */                 PgenTcaTool.this.tcaDlg.getSeverity(), PgenTcaTool.this.tcaDlg.getAdvisoryType(), 
/* 611 */                 geog, bp);
/* 612 */               int idx = PgenTcaTool.this.elem.getAdvisories()
/* 613 */                 .indexOf(PgenTcaTool.this.selectedAdvisory);
/* 614 */               tcaTemp.replaceAdvisory(idx, adv);
/* 615 */               this.ghost.add(tcaTemp);
/*     */             }
/*     */           }
/*     */ 
/*     */           break;
/*     */         }
/*     */ 
/* 622 */         if (this.firstPt != null)
/* 623 */           this.ghost.add(this.firstPt);
/* 624 */         PgenTcaTool.this.drawingLayer.setGhostLine(this.ghost);
/* 625 */         PgenTcaTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 629 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenTcaTool
 * JD-Core Version:    0.6.2
 */