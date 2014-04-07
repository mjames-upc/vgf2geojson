/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.exception.VizException;
/*    */ import com.raytheon.viz.ui.tools.AbstractTool;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*    */ import gov.noaa.nws.ncep.ui.pgen.controls.StoreActivityDialog;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*    */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*    */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*    */ import java.util.List;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.IWorkbenchWindow;
/*    */ import org.eclipse.ui.PlatformUI;
/*    */ 
/*    */ public class StoreManageHandler extends AbstractTool
/*    */ {
/*    */   public Object execute(ExecutionEvent event)
/*    */     throws ExecutionException
/*    */   {
/* 47 */     String btnClicked = (String)event.getApplicationContext();
/*    */ 
/* 50 */     String btnName = event.getParameter("name");
/* 51 */     PgenSession.getInstance().getPgenPalette().setActiveIcon(btnName);
/*    */ 
/* 53 */     String curFile = PgenSession.getInstance().getPgenResource()
/* 54 */       .getActiveProduct().getOutputFile();
/*    */ 
/* 56 */     if ((curFile != null) && (btnClicked.equalsIgnoreCase("Save"))) {
/* 57 */       PgenSession.getInstance().getPgenResource()
/* 58 */         .storeCurrentProduct(curFile);
/* 59 */     } else if ((curFile != null) && (btnClicked.equalsIgnoreCase("Save All")))
/*    */     {
/* 61 */       if (PgenSession.getInstance().getPgenResource().getProducts()
/* 61 */         .size() > 1)
/* 62 */         PgenSession.getInstance().getPgenResource().storeAllProducts();
/*    */       else
/* 64 */         PgenSession.getInstance().getPgenResource()
/* 65 */           .storeCurrentProduct(curFile);
/*    */     }
/*    */     else
/*    */     {
/* 69 */       Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
/* 70 */         .getShell();
/* 71 */       StoreActivityDialog storeDialog = null;
/*    */ 
/* 73 */       if (storeDialog == null) {
/*    */         try {
/* 75 */           storeDialog = new StoreActivityDialog(shell, btnClicked);
/* 76 */           storeDialog.setBlockOnOpen(true);
/*    */         } catch (VizException e) {
/* 78 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */ 
/* 82 */       if (storeDialog != null) {
/* 83 */         storeDialog.open();
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 88 */     if (PgenSession.getInstance().getPgenPalette() != null) {
/* 89 */       PgenSession.getInstance().getPgenPalette().resetIcon(btnName);
/*    */     }
/*    */ 
/* 92 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.StoreManageHandler
 * JD-Core Version:    0.6.2
 */