/*    */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*    */ 
/*    */ import org.eclipse.core.commands.AbstractHandler;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.IWorkbenchWindow;
/*    */ import org.eclipse.ui.PlatformUI;
/*    */ 
/*    */ public class PgenHelpHandler extends AbstractHandler
/*    */ {
/*    */   public Object execute(ExecutionEvent arg0)
/*    */     throws ExecutionException
/*    */   {
/* 40 */     Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/* 41 */     PgenHelpDialog dlg = PgenHelpDialog.getInstance(shell);
/*    */ 
/* 43 */     if (!dlg.isOpen()) {
/* 44 */       dlg.open();
/*    */     }
/*    */ 
/* 47 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.PgenHelpHandler
 * JD-Core Version:    0.6.2
 */