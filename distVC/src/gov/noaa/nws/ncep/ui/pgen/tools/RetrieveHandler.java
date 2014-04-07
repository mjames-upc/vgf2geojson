/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.exception.VizException;
/*    */ import com.raytheon.viz.ui.tools.AbstractTool;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*    */ import gov.noaa.nws.ncep.ui.pgen.controls.RetrieveActivityDialog;
/*    */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ 
/*    */ public class RetrieveHandler extends AbstractTool
/*    */ {
/*    */   public Object execute(ExecutionEvent event)
/*    */     throws ExecutionException
/*    */   {
/* 47 */     Shell shell = new Shell(1264);
/* 48 */     String btnClicked = (String)event.getApplicationContext();
/*    */ 
/* 51 */     String btnName = event.getParameter("name");
/* 52 */     PgenSession.getInstance().getPgenPalette().setActiveIcon(btnName);
/*    */ 
/* 54 */     RetrieveActivityDialog retrieveDlg = null;
/*    */     try
/*    */     {
/* 57 */       retrieveDlg = new RetrieveActivityDialog(shell, btnClicked);
/* 58 */       retrieveDlg.setBlockOnOpen(true);
/*    */     } catch (VizException e) {
/* 60 */       e.printStackTrace();
/*    */     }
/*    */ 
/* 63 */     if (retrieveDlg != null) {
/* 64 */       retrieveDlg.open();
/*    */     }
/*    */ 
/* 68 */     if (PgenSession.getInstance().getPgenPalette() != null) {
/* 69 */       PgenSession.getInstance().getPgenPalette().resetIcon(btnName);
/*    */     }
/*    */ 
/* 72 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.RetrieveHandler
 * JD-Core Version:    0.6.2
 */