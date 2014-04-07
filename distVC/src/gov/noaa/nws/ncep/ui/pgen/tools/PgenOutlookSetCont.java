/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.OutlookAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ 
/*     */ public class PgenOutlookSetCont extends AbstractPgenDrawingTool
/*     */ {
/*     */   private Outlook otlk;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  56 */     super.activateTool();
/*     */ 
/*  58 */     if ((this.event.getTrigger() instanceof Outlook)) this.otlk = ((Outlook)this.event.getTrigger());
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  70 */     if (this.mouseHandler == null)
/*     */     {
/*  72 */       this.mouseHandler = new PgenOutlookSetContHandler();
/*     */     }
/*     */ 
/*  76 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/*  87 */     super.deactivateTool();
/*     */   }
/*     */ 
/*     */   private class PgenOutlookSetContHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     DECollection dec;
/*     */ 
/*     */     public PgenOutlookSetContHandler()
/*     */     {
/* 102 */       this.dec = null;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 113 */       if ((!PgenOutlookSetCont.this.isResourceEditable()) || (PgenOutlookSetCont.this.otlk == null)) return false;
/*     */ 
/* 116 */       Coordinate loc = PgenOutlookSetCont.this.mapEditor.translateClick(anX, aY);
/* 117 */       if (loc == null) return false;
/*     */ 
/* 119 */       if (button == 1)
/*     */       {
/* 121 */         Line ln = PgenOutlookSetCont.this.otlk.getNearestLine(loc);
/*     */ 
/* 124 */         if ((ln != null) && ((ln.getParent().getParent() == this.dec) || (ln.getParent().getParent().equals(PgenOutlookSetCont.this.otlk))))
/*     */         {
/* 127 */           if (ln.getParent().getParent() == this.dec) {
/* 128 */             PgenOutlookSetCont.this.drawingLayer.removeSelected(ln);
/*     */ 
/* 130 */             PgenOutlookSetCont.this.otlk.rmLineFromGroup(ln, this.dec);
/* 131 */             if (!PgenOutlookSetCont.this.otlk.contains(this.dec)) this.dec = null;
/*     */           }
/*     */           else
/*     */           {
/* 135 */             PgenOutlookSetCont.this.drawingLayer.addSelected(ln);
/*     */ 
/* 137 */             if (this.dec == null) {
/* 138 */               this.dec = new DECollection(Outlook.OUTLOOK_LINE_GROUP);
/* 139 */               PgenOutlookSetCont.this.otlk.add(this.dec);
/*     */             }
/* 141 */             PgenOutlookSetCont.this.otlk.addLineToGroup(ln, this.dec);
/*     */           }
/*     */ 
/* 144 */           PgenOutlookSetCont.this.drawingLayer.removeGhostLine();
/*     */         }
/* 147 */         else if ((ln != null) && (ln.getParent().getParent().getName().equalsIgnoreCase(Outlook.OUTLOOK_LINE_GROUP)) && (this.dec == null))
/*     */         {
/* 149 */           this.dec = ((DECollection)ln.getParent().getParent());
/* 150 */           PgenOutlookSetCont.this.drawingLayer.setSelected(ln);
/*     */         }
/* 152 */         else if ((ln != null) && (ln.getParent().getParent().getName().equalsIgnoreCase(Outlook.OUTLOOK_LINE_GROUP)) && (this.dec != null))
/*     */         {
/* 154 */           PgenOutlookSetCont.this.otlk.addLineToGroup(ln, this.dec);
/* 155 */           PgenOutlookSetCont.this.drawingLayer.setSelected(ln);
/*     */         }
/*     */ 
/* 158 */         ((OutlookAttrDlg)PgenOutlookSetCont.this.attrDlg).showContLines(PgenOutlookSetCont.this.otlk);
/* 159 */         PgenOutlookSetCont.this.mapEditor.refresh();
/* 160 */         return true;
/*     */       }
/*     */ 
/* 163 */       if (button == 3)
/*     */       {
/* 165 */         PgenOutlookSetCont.this.drawingLayer.removeSelected();
/* 166 */         PgenUtil.loadOutlookDrawingTool();
/* 167 */         this.dec = null;
/*     */ 
/* 169 */         return true;
/*     */       }
/*     */ 
/* 174 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 182 */       if (!PgenOutlookSetCont.this.isResourceEditable()) return false;
/* 183 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenOutlookSetCont
 * JD-Core Version:    0.6.2
 */