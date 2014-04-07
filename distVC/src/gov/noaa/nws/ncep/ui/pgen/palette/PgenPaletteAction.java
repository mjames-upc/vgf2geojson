/*    */ package gov.noaa.nws.ncep.ui.pgen.palette;
/*    */ 
/*    */ import com.raytheon.viz.ui.EditorUtil;
/*    */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*    */ import org.eclipse.core.commands.AbstractHandler;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.swt.widgets.MessageBox;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ import org.eclipse.ui.IViewPart;
/*    */ import org.eclipse.ui.IViewReference;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.IWorkbenchPage;
/*    */ import org.eclipse.ui.IWorkbenchWindow;
/*    */ import org.eclipse.ui.PlatformUI;
/*    */ import org.eclipse.ui.internal.WorkbenchPage;
/*    */ 
/*    */ public class PgenPaletteAction extends AbstractHandler
/*    */ {
/*    */   public Object execute(ExecutionEvent arg0)
/*    */     throws ExecutionException
/*    */   {
/* 39 */     AbstractEditor editor = (AbstractEditor)EditorUtil.getActiveEditor();
/*    */ 
/* 41 */     if ((editor instanceof AbstractEditor)) {
/* 42 */       IWorkbenchPage wpage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
/*    */ 
/* 44 */       IViewPart vpart = wpage.findView("gov.noaa.nws.ncep.ui.PGEN");
/*    */       try
/*    */       {
/* 49 */         if (vpart == null)
/*    */         {
/* 51 */           vpart = wpage.showView("gov.noaa.nws.ncep.ui.PGEN");
/* 52 */           IViewReference pgenViewRef = wpage.findViewReference("gov.noaa.nws.ncep.ui.PGEN");
/* 53 */           if ((pgenViewRef != null) && ((wpage instanceof WorkbenchPage))) {
/* 54 */             ((WorkbenchPage)wpage).detachView(pgenViewRef);
/*    */           }
/*    */ 
/*    */         }
/* 59 */         else if (!wpage.isPartVisible(vpart)) {
/* 60 */           vpart = wpage.showView("gov.noaa.nws.ncep.ui.PGEN");
/* 61 */           IViewReference pgenViewRef = wpage.findViewReference("gov.noaa.nws.ncep.ui.PGEN");
/* 62 */           if ((pgenViewRef != null) && ((wpage instanceof WorkbenchPage))) {
/* 63 */             ((WorkbenchPage)wpage).detachView(pgenViewRef);
/*    */           }
/*    */ 
/*    */         }
/*    */ 
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 71 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 76 */       Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
/* 77 */       MessageBox mb = new MessageBox(shell, 40);
/*    */ 
/* 80 */       mb.setMessage("Pgen is not supported in this editor. Please select a mapEditor for Pgen to use first!");
/* 81 */       mb.open();
/*    */     }
/* 83 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteAction
 * JD-Core Version:    0.6.2
 */