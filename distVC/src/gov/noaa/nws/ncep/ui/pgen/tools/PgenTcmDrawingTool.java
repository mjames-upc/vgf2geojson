/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TcmAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.tcm.Tcm;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ 
/*     */ public class PgenTcmDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   protected void activateTool()
/*     */   {
/*  55 */     super.activateTool();
/*     */ 
/*  57 */     DrawableElement elem = null;
/*  58 */     if ((this.event.getTrigger() instanceof Tcm)) elem = (Tcm)this.event.getTrigger();
/*     */ 
/*  60 */     if ((this.attrDlg instanceof TcmAttrDlg)) {
/*  61 */       if (elem != null) this.attrDlg.setAttrForDlg(elem);
/*  62 */       ((TcmAttrDlg)this.attrDlg).setTcm((Tcm)elem);
/*     */     }
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  74 */     if (this.mouseHandler == null)
/*     */     {
/*  76 */       this.mouseHandler = new PgenTCMDrawingHandler();
/*     */     }
/*     */ 
/*  80 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public class PgenTCMDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/*  95 */     private ArrayList<Coordinate> points = new ArrayList();
/*     */     private AbstractDrawableComponent elem;
/* 106 */     private DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/*     */     public PgenTCMDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 116 */       if (!PgenTcmDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 121 */       if (button == 1)
/*     */       {
/* 123 */         return false;
/*     */       }
/*     */ 
/* 126 */       if (button == 3)
/*     */       {
/* 128 */         PgenTcmDrawingTool.this.drawingLayer.removeGhostLine();
/* 129 */         PgenTcmDrawingTool.this.mapEditor.refresh();
/*     */ 
/* 131 */         if (this.points.size() == 0)
/*     */         {
/* 133 */           PgenUtil.setSelectingMode();
/*     */         }
/*     */         else
/*     */         {
/* 138 */           this.points.clear();
/*     */         }
/*     */ 
/* 142 */         return true;
/*     */       }
/*     */ 
/* 145 */       if (button == 2)
/*     */       {
/* 147 */         return false;
/*     */       }
/*     */ 
/* 152 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 167 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 173 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenTcmDrawingTool
 * JD-Core Version:    0.6.2
 */