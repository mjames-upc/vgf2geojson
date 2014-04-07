/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class PgenSpenesDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   protected void activateTool()
/*     */   {
/*  34 */     super.activateTool();
/*  35 */     new Thread() {
/*     */       public void run() {
/*  37 */         PgenStaticDataProvider.getProvider().loadCwaTable();
/*  38 */         PgenStaticDataProvider.getProvider().loadStateTable();
/*  39 */         PgenStaticDataProvider.getProvider().loadRfcTable();
/*     */       }
/*     */     }
/*  41 */     .start();
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/*  52 */     super.deactivateTool();
/*     */ 
/*  54 */     if ((this.mouseHandler instanceof PgenSpenesDrawingHandler)) {
/*  55 */       PgenSpenesDrawingHandler mph = (PgenSpenesDrawingHandler)this.mouseHandler;
/*  56 */       if (mph != null) mph.clearPoints();
/*     */     }
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  66 */     if (this.mouseHandler == null)
/*     */     {
/*  68 */       this.mouseHandler = new PgenSpenesDrawingHandler(null);
/*     */     }
/*     */ 
/*  72 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   private class PgenSpenesDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/*  80 */     private ArrayList<Coordinate> points = new ArrayList();
/*     */     private AbstractDrawableComponent elem;
/*  91 */     private DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/*     */     private PgenSpenesDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 103 */       Coordinate loc = PgenSpenesDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 104 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 106 */       if (button == 1)
/*     */       {
/* 108 */         this.points.add(loc);
/*     */ 
/* 110 */         return true;
/*     */       }
/*     */ 
/* 113 */       if (button == 3)
/*     */       {
/* 115 */         if (this.points.size() == 0)
/*     */         {
/* 117 */           if (PgenSpenesDrawingTool.this.attrDlg != null) PgenSpenesDrawingTool.this.attrDlg.close();
/* 118 */           PgenSpenesDrawingTool.this.attrDlg = null;
/* 119 */           PgenUtil.setSelectingMode();
/*     */         }
/* 122 */         else if (this.points.size() < 2)
/*     */         {
/* 124 */           PgenSpenesDrawingTool.this.drawingLayer.removeGhostLine();
/* 125 */           this.points.clear();
/*     */ 
/* 127 */           PgenSpenesDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */         else
/*     */         {
/* 133 */           this.elem = this.def.create(DrawableType.SPENES, PgenSpenesDrawingTool.this.attrDlg, 
/* 134 */             PgenSpenesDrawingTool.this.pgenCategory, PgenSpenesDrawingTool.this.pgenType, this.points, PgenSpenesDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 136 */           PgenSpenesDrawingTool.this.attrDlg.setDrawableElement((DrawableElement)this.elem);
/*     */ 
/* 140 */           PgenSpenesDrawingTool.this.drawingLayer.addElement(this.elem);
/*     */ 
/* 143 */           PgenSpenesDrawingTool.this.drawingLayer.removeGhostLine();
/* 144 */           this.points.clear();
/*     */ 
/* 146 */           PgenSpenesDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 150 */         return true;
/*     */       }
/*     */ 
/* 153 */       if (button == 2)
/*     */       {
/* 155 */         return true;
/*     */       }
/*     */ 
/* 160 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 174 */       Coordinate loc = PgenSpenesDrawingTool.this.mapEditor.translateClick(x, y);
/* 175 */       if (loc == null) return false;
/*     */ 
/* 178 */       AbstractDrawableComponent ghostline = this.def.create(DrawableType.SPENES, PgenSpenesDrawingTool.this.attrDlg, 
/* 179 */         PgenSpenesDrawingTool.this.pgenCategory, PgenSpenesDrawingTool.this.pgenType, this.points, PgenSpenesDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 181 */       if ((this.points != null) && (this.points.size() >= 1))
/*     */       {
/* 183 */         ArrayList ghostPts = new ArrayList(this.points);
/* 184 */         ghostPts.add(loc);
/* 185 */         Line ln = (Line)ghostline;
/* 186 */         ln.setLinePoints(new ArrayList(ghostPts));
/*     */ 
/* 191 */         PgenSpenesDrawingTool.this.drawingLayer.setGhostLine(ghostline);
/* 192 */         PgenSpenesDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 196 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int aX, int aY, int button)
/*     */     {
/* 202 */       if (this.shiftDown) return false;
/* 203 */       return true;
/*     */     }
/*     */ 
/*     */     private void clearPoints() {
/* 207 */       this.points.clear();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenSpenesDrawingTool
 * JD-Core Version:    0.6.2
 */