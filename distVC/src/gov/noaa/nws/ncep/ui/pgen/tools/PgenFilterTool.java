/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*    */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.PgenFilterDlg;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.IWorkbenchWindow;
/*    */ import org.eclipse.ui.PlatformUI;
/*    */ 
/*    */ public class PgenFilterTool extends AbstractPgenTool
/*    */ {
/*    */   protected void activateTool()
/*    */   {
/* 51 */     super.activateTool();
/*    */ 
/* 53 */     PgenFilterDlg filterDlg = PgenFilterDlg.getInstance(
/* 54 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/*    */ 
/* 56 */     filterDlg.setResource(this.drawingLayer, this.mapEditor);
/* 57 */     filterDlg.setBlockOnOpen(false);
/* 58 */     filterDlg.open();
/*    */ 
/* 61 */     PgenUtil.setSelectingMode();
/*    */   }
/*    */ 
/*    */   public IInputHandler getMouseHandler()
/*    */   {
/* 67 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenFilterTool
 * JD-Core Version:    0.6.2
 */