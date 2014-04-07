/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.viz.ui.tools.AbstractTool;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*    */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenCommandManager;
/*    */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ 
/*    */ public class PgenUndoRedoHandler extends AbstractTool
/*    */ {
/*    */   public Object execute(ExecutionEvent event)
/*    */     throws ExecutionException
/*    */   {
/* 34 */     if ((PgenSession.getInstance().getPgenResource() != null) && 
/* 35 */       (PgenSession.getInstance().getPgenResource().isEditable())) {
/* 36 */       if (event.getApplicationContext().equals("Undo")) {
/* 37 */         PgenSession.getInstance().getCommandManager().undo();
/*    */       }
/* 39 */       else if (event.getApplicationContext().equals("Redo")) {
/* 40 */         PgenSession.getInstance().getCommandManager().redo();
/*    */       }
/* 43 */       else if ((event.getParameter("action") != null) && (!event.getParameter("action").isEmpty())) {
/* 44 */         String actionToDo = new String(event.getParameter("action"));
/* 45 */         if (actionToDo.equals("Undo"))
/* 46 */           PgenSession.getInstance().getCommandManager().undo();
/* 47 */         else if (actionToDo.equals("Redo")) {
/* 48 */           PgenSession.getInstance().getCommandManager().redo();
/*    */         }
/*    */       }
/*    */ 
/* 52 */       PgenUtil.refresh();
/*    */ 
/* 55 */       PgenUtil.setSelectingMode();
/*    */     }
/* 57 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenUndoRedoHandler
 * JD-Core Version:    0.6.2
 */