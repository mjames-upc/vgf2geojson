/*    */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.IDisplayPaneContainer;
/*    */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*    */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*    */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*    */ import org.eclipse.jface.dialogs.MessageDialog;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.IWorkbenchWindow;
/*    */ import org.eclipse.ui.PlatformUI;
/*    */ 
/*    */ public class PgenDeleteAll extends AbstractPgenTool
/*    */ {
/*    */   protected void activateTool()
/*    */   {
/* 52 */     super.activateTool();
/* 53 */     if (!isResourceEditable()) return;
/*    */ 
/* 58 */     String msg = "Are you sure you want to delete all?";
/*    */ 
/* 60 */     String name = PgenSession.getInstance().getPgenResource().getActiveLayer().getName();
/* 61 */     if (!name.equalsIgnoreCase("Default")) {
/* 62 */       msg = "Are you sure you want to delete all on Layer - " + name + "?";
/*    */     }
/*    */ 
/* 65 */     MessageDialog confirmDlg = new MessageDialog(
/* 66 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 67 */       "Confirm Delete", null, msg, 
/* 68 */       3, new String[] { "OK", "Cancel" }, 0);
/* 69 */     confirmDlg.open();
/*    */ 
/* 71 */     if (confirmDlg.getReturnCode() == 0)
/*    */     {
/* 74 */       this.drawingLayer.removeAllActiveDEs();
/* 75 */       this.editor.refresh();
/*    */     }
/*    */ 
/* 80 */     PgenUtil.setSelectingMode();
/*    */   }
/*    */ 
/*    */   public IInputHandler getMouseHandler()
/*    */   {
/* 86 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenDeleteAll
 * JD-Core Version:    0.6.2
 */