/*    */ package gov.noaa.nws.ncep.ui.pgen.layering;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*    */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*    */ import org.eclipse.core.commands.AbstractHandler;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ 
/*    */ public class PgenLayeringHandler extends AbstractHandler
/*    */ {
/*    */   public Object execute(ExecutionEvent arg0)
/*    */     throws ExecutionException
/*    */   {
/* 42 */     PgenSession.getInstance().getPgenResource().activateLayering();
/*    */ 
/* 44 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.layering.PgenLayeringHandler
 * JD-Core Version:    0.6.2
 */