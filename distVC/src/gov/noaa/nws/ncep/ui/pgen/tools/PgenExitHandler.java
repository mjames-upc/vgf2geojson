/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import org.eclipse.core.commands.AbstractHandler;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.ui.IViewPart;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.IWorkbenchPage;
/*    */ import org.eclipse.ui.IWorkbenchWindow;
/*    */ import org.eclipse.ui.PlatformUI;
/*    */ 
/*    */ public class PgenExitHandler extends AbstractHandler
/*    */ {
/*    */   public Object execute(ExecutionEvent event)
/*    */     throws ExecutionException
/*    */   {
/* 37 */     IWorkbenchPage wpage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
/*    */ 
/* 39 */     IViewPart vpart = wpage.findView("gov.noaa.nws.ncep.ui.PGEN");
/*    */ 
/* 41 */     wpage.hideView(vpart);
/*    */ 
/* 43 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenExitHandler
 * JD-Core Version:    0.6.2
 */