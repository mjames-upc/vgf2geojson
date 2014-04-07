/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.exception.VizException;
/*    */ import com.raytheon.viz.ui.tools.AbstractTool;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*    */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenFileManageDialog1;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*    */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*    */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*    */ import java.util.List;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ 
/*    */ public class PgenFileManageHandler extends AbstractTool
/*    */ {
/*    */   public Object execute(ExecutionEvent event)
/*    */     throws ExecutionException
/*    */   {
/* 48 */     Shell shell = new Shell(1264);
/* 49 */     String btnClicked = (String)event.getApplicationContext();
/*    */ 
/* 52 */     String btnName = event.getParameter("name");
/* 53 */     PgenSession.getInstance().getPgenPalette().setActiveIcon(btnName);
/*    */ 
/* 55 */     String curFile = PgenSession.getInstance().getPgenResource().getActiveProduct().getOutputFile();
/*    */ 
/* 57 */     if ((curFile != null) && (btnClicked.equalsIgnoreCase("Save"))) {
/* 58 */       PgenSession.getInstance().getPgenResource().saveCurrentProduct(curFile);
/*    */     }
/* 60 */     else if ((curFile != null) && (btnClicked.equalsIgnoreCase("Save All"))) {
/* 61 */       if (PgenSession.getInstance().getPgenResource().getProducts().size() > 1) {
/* 62 */         PgenSession.getInstance().getPgenResource().saveAllProducts();
/*    */       }
/*    */       else {
/* 65 */         PgenSession.getInstance().getPgenResource().saveCurrentProduct(curFile);
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 70 */       PgenFileManageDialog1 file_dlg = null;
/*    */ 
/* 72 */       if (file_dlg == null) {
/*    */         try {
/* 74 */           file_dlg = new PgenFileManageDialog1(shell, btnClicked);
/* 75 */           file_dlg.setBlockOnOpen(true);
/*    */         }
/*    */         catch (VizException e) {
/* 78 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */ 
/* 82 */       if (file_dlg != null) file_dlg.open();
/*    */ 
/*    */     }
/*    */ 
/* 86 */     if (PgenSession.getInstance().getPgenPalette() != null) {
/* 87 */       PgenSession.getInstance().getPgenPalette().resetIcon(btnName);
/*    */     }
/*    */ 
/* 90 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenFileManageHandler
 * JD-Core Version:    0.6.2
 */