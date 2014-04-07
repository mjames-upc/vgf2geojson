/*    */ package gov.noaa.nws.ncep.ui.pgen.action;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*    */ import com.raytheon.viz.ui.perspectives.AbstractVizPerspectiveManager;
/*    */ import com.raytheon.viz.ui.perspectives.VizPerspectiveListener;
/*    */ import com.raytheon.viz.ui.tools.AbstractModalTool;
/*    */ import com.raytheon.viz.ui.tools.ModalToolManager;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*    */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*    */ import gov.noaa.nws.ncep.ui.pgen.tools.AbstractPgenDrawingTool;
/*    */ import gov.noaa.nws.ncep.ui.pgen.tools.AbstractPgenTool;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import org.eclipse.core.runtime.IConfigurationElement;
/*    */ import org.eclipse.jface.action.Action;
/*    */ 
/*    */ public class PgenAction extends Action
/*    */ {
/*    */   private String actionName;
/*    */ 
/*    */   public PgenAction(String name)
/*    */   {
/* 47 */     super(name);
/* 48 */     this.actionName = name;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 58 */     doPgenAction(this.actionName);
/*    */   }
/*    */ 
/*    */   public String getText()
/*    */   {
/* 68 */     return this.actionName;
/*    */   }
/*    */ 
/*    */   private void doPgenAction(String actionName)
/*    */   {
/*    */     try
/*    */     {
/* 79 */       AbstractPgenDrawingTool pgenTool = null;
/* 80 */       AbstractVizPerspectiveManager mgr = VizPerspectiveListener.getCurrentPerspectiveManager();
/* 81 */       Iterator localIterator = mgr.getToolManager().getSelectedModalTools().iterator(); if (localIterator.hasNext()) { AbstractModalTool tool = (AbstractModalTool)localIterator.next();
/* 82 */         if ((tool instanceof AbstractPgenDrawingTool)) {
/* 83 */           pgenTool = (AbstractPgenDrawingTool)tool;
/*    */         }
/*    */ 
/*    */       }
/*    */ 
/* 88 */       if (pgenTool != null) {
/* 89 */         IConfigurationElement ice = (IConfigurationElement)PgenSession.getInstance().getPgenPalette().getItemMap().get(actionName);
/* 90 */         Object obj = Class.forName(ice.getAttribute("actionHandlerClass"))
/* 91 */           .getConstructor(new Class[] { 
/* 91 */           AbstractPgenTool.class }).newInstance(new Object[] { pgenTool });
/*    */ 
/* 93 */         if ((obj != null) && ((obj instanceof IInputHandler)))
/* 94 */           pgenTool.setHandler((IInputHandler)obj);
/*    */       }
/*    */     }
/*    */     catch (Exception e) {
/* 98 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.action.PgenAction
 * JD-Core Version:    0.6.2
 */