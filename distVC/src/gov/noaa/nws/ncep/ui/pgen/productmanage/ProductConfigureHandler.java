/*    */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*    */ import org.eclipse.core.commands.AbstractHandler;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.jface.dialogs.MessageDialog;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.IWorkbenchWindow;
/*    */ import org.eclipse.ui.PlatformUI;
/*    */ 
/*    */ public class ProductConfigureHandler extends AbstractHandler
/*    */ {
/*    */   public Object execute(ExecutionEvent arg0)
/*    */     throws ExecutionException
/*    */   {
/* 44 */     if (PgenSession.getInstance().getPgenPalette() == null) {
/* 45 */       MessageDialog confirmDlg = new MessageDialog(
/* 46 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 47 */         "PGEN Palette Needed", null, 
/* 48 */         "Please Activate the PGEN Palette First", 
/* 49 */         4, new String[] { "OK" }, 0);
/*    */ 
/* 51 */       confirmDlg.open();
/*    */ 
/* 53 */       return null;
/*    */     }
/*    */ 
/* 56 */     Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/* 57 */     ProductConfigureDialog dlg = ProductConfigureDialog.getInstance(shell);
/*    */ 
/* 59 */     if (!dlg.isOpen()) {
/* 60 */       dlg.open();
/*    */     }
/*    */ 
/* 63 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureHandler
 * JD-Core Version:    0.6.2
 */