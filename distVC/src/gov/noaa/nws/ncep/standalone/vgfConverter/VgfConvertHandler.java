/*    */ package gov.noaa.nws.ncep.standalone.vgfConverter;
/*    */ 
/*    */ import org.eclipse.core.commands.AbstractHandler;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.ui.IWorkbenchWindow;
/*    */ import org.eclipse.ui.handlers.HandlerUtil;
/*    */ 
/*    */ public class VgfConvertHandler extends AbstractHandler
/*    */ {
/*    */   public Object execute(ExecutionEvent event)
/*    */     throws ExecutionException
/*    */   {
/* 26 */     VgfConvertDialog cd = new VgfConvertDialog(HandlerUtil.getActiveWorkbenchWindow(event).getShell());
/* 27 */     cd.open();
/*    */ 
/* 29 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.standalone.vgfConverter.VgfConvertHandler
 * JD-Core Version:    0.6.2
 */