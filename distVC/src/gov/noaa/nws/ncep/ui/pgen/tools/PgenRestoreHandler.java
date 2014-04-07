/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.exception.VizException;
/*    */ import com.raytheon.viz.ui.tools.AbstractTool;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*    */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenRestoreDialog;
/*    */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ 
/*    */ public class PgenRestoreHandler extends AbstractTool
/*    */ {
/*    */   public Object execute(ExecutionEvent event)
/*    */     throws ExecutionException
/*    */   {
/* 45 */     super.execute(event);
/*    */ 
/* 47 */     Shell shell = new Shell(34032);
/*    */ 
/* 50 */     String btnName = event.getParameter("name");
/* 51 */     PgenSession.getInstance().getPgenPalette().setActiveIcon(btnName);
/*    */ 
/* 53 */     PgenRestoreDialog restoreDlg = null;
/*    */ 
/* 55 */     if (restoreDlg == null) {
/*    */       try {
/* 57 */         restoreDlg = new PgenRestoreDialog(shell);
/*    */       }
/*    */       catch (VizException e) {
/* 60 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */ 
/* 64 */     if (restoreDlg != null) restoreDlg.open();
/*    */ 
/* 67 */     PgenSession.getInstance().getPgenPalette().resetIcon(btnName);
/*    */ 
/* 69 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenRestoreHandler
 * JD-Core Version:    0.6.2
 */