/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.LineAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.WatchBoxAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ILine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ 
/*     */ public class PgenWatchStatusLineDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   private WatchBox wb;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  63 */     super.activateTool();
/*     */ 
/*  65 */     ((LineAttrDlg)this.attrDlg).setSmoothLvl(0);
/*  66 */     if ((this.event.getTrigger() instanceof WatchBox)) this.wb = ((WatchBox)this.event.getTrigger());
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  78 */     if (this.mouseHandler == null)
/*     */     {
/*  80 */       this.mouseHandler = new PgenWatchStatusLineDrawingHandler(null);
/*     */     }
/*     */ 
/*  84 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/*  95 */     super.deactivateTool();
/*     */ 
/*  98 */     PgenWatchStatusLineDrawingHandler wsh = (PgenWatchStatusLineDrawingHandler)this.mouseHandler;
/*  99 */     if (wsh != null) wsh.clearPoints();
/*     */   }
/*     */ 
/*     */   private class PgenWatchStatusLineDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/* 113 */     protected ArrayList<Coordinate> points = new ArrayList();
/*     */ 
/*     */     private PgenWatchStatusLineDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 123 */       if (!PgenWatchStatusLineDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 126 */       Coordinate loc = PgenWatchStatusLineDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 127 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 129 */       if (button == 1)
/*     */       {
/* 132 */         this.points.add(loc);
/*     */ 
/* 134 */         return true;
/*     */       }
/*     */ 
/* 137 */       if (button == 3)
/*     */       {
/* 139 */         if (this.points.size() == 0)
/*     */         {
/* 142 */           if (PgenWatchStatusLineDrawingTool.this.attrDlg != null) PgenWatchStatusLineDrawingTool.this.attrDlg.close();
/* 143 */           PgenWatchStatusLineDrawingTool.this.attrDlg = null;
/*     */ 
/* 146 */           PgenUtil.loadWatchBoxModifyTool(PgenWatchStatusLineDrawingTool.this.wb);
/*     */ 
/* 149 */           PgenWatchStatusLineDrawingTool.this.drawingLayer.setSelected(PgenWatchStatusLineDrawingTool.this.wb);
/*     */ 
/* 152 */           WatchBoxAttrDlg wbdlg = WatchBoxAttrDlg.getInstance(null);
/* 153 */           wbdlg.openSpecDlg();
/* 154 */           wbdlg.setDrawableElement(PgenWatchStatusLineDrawingTool.this.wb);
/* 155 */           wbdlg.setMouseHandlerName("Pgen Select");
/* 156 */           wbdlg.setAttrForDlg(PgenWatchStatusLineDrawingTool.this.wb);
/* 157 */           wbdlg.enableButtons();
/* 158 */           wbdlg.setPgenCategory(PgenWatchStatusLineDrawingTool.this.wb.getPgenCategory());
/* 159 */           wbdlg.setPgenType(PgenWatchStatusLineDrawingTool.this.wb.getPgenType());
/* 160 */           wbdlg.setDrawingLayer(PgenWatchStatusLineDrawingTool.this.drawingLayer);
/* 161 */           wbdlg.setMapEditor(PgenWatchStatusLineDrawingTool.this.mapEditor);
/*     */         }
/* 165 */         else if (this.points.size() < 2)
/*     */         {
/* 167 */           PgenWatchStatusLineDrawingTool.this.drawingLayer.removeGhostLine();
/* 168 */           this.points.clear();
/*     */ 
/* 170 */           PgenWatchStatusLineDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */         else
/*     */         {
/* 177 */           Line statusLine = new Line(null, PgenWatchStatusLineDrawingTool.this.attrDlg.getColors(), PgenWatchStatusLineDrawingTool.this.attrDlg.getLineWidth(), 
/* 178 */             1.0D, false, false, this.points, ((ILine)PgenWatchStatusLineDrawingTool.this.attrDlg).getSmoothFactor(), FillPatternList.FillPattern.SOLID, 
/* 179 */             "Lines", "POINTED_ARROW");
/*     */ 
/* 182 */           ((DECollection)PgenWatchStatusLineDrawingTool.this.wb.getParent()).add(statusLine);
/*     */ 
/* 184 */           PgenWatchStatusLineDrawingTool.this.drawingLayer.removeGhostLine();
/* 185 */           this.points.clear();
/* 186 */           PgenWatchStatusLineDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 190 */         return true;
/*     */       }
/*     */ 
/* 193 */       if (button == 2)
/*     */       {
/* 195 */         return true;
/*     */       }
/*     */ 
/* 200 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 213 */       if (!PgenWatchStatusLineDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 216 */       Coordinate loc = PgenWatchStatusLineDrawingTool.this.mapEditor.translateClick(x, y);
/* 217 */       if (loc == null) return false;
/*     */ 
/* 220 */       Line ghostLine = new Line(null, PgenWatchStatusLineDrawingTool.this.attrDlg.getColors(), PgenWatchStatusLineDrawingTool.this.attrDlg.getLineWidth(), 1.0D, false, 
/* 221 */         false, this.points, ((ILine)PgenWatchStatusLineDrawingTool.this.attrDlg).getSmoothFactor(), FillPatternList.FillPattern.SOLID, "Lines", "POINTED_ARROW");
/*     */ 
/* 223 */       if ((this.points != null) && (this.points.size() >= 1))
/*     */       {
/* 225 */         ArrayList ghostPts = new ArrayList(this.points);
/* 226 */         ghostPts.add(loc);
/*     */ 
/* 228 */         ghostLine.setLinePoints(new ArrayList(ghostPts));
/*     */ 
/* 230 */         PgenWatchStatusLineDrawingTool.this.drawingLayer.setGhostLine(ghostLine);
/* 231 */         PgenWatchStatusLineDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 235 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 241 */       if ((!PgenWatchStatusLineDrawingTool.this.drawingLayer.isEditable()) || (this.shiftDown)) return false;
/* 242 */       return true;
/*     */     }
/*     */ 
/*     */     public void clearPoints() {
/* 246 */       this.points.clear();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenWatchStatusLineDrawingTool
 * JD-Core Version:    0.6.2
 */