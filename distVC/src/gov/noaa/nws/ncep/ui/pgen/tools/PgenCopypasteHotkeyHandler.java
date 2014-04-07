/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenClipboard;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.List;
/*     */ import org.eclipse.core.commands.AbstractHandler;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.core.commands.ExecutionException;
/*     */ 
/*     */ public class PgenCopypasteHotkeyHandler extends AbstractHandler
/*     */ {
/*     */   public Object execute(ExecutionEvent event)
/*     */     throws ExecutionException
/*     */   {
/*  52 */     String action = event.getParameter("action");
/*  53 */     if ((action == null) || (action.isEmpty())) {
/*  54 */       return null;
/*     */     }
/*     */ 
/*  57 */     PgenResource pgenRsc = PgenSession.getInstance().getPgenResource();
/*  58 */     if (pgenRsc == null) {
/*  59 */       return null;
/*     */     }
/*     */ 
/*  62 */     Layer activeLyr = pgenRsc.getActiveLayer();
/*  63 */     if (activeLyr == null) {
/*  64 */       return null;
/*     */     }
/*     */ 
/*  67 */     boolean changed = false;
/*  68 */     String curAction = PgenSession.getInstance().getPgenPalette().getCurrentAction();
/*     */ 
/*  79 */     if (action.equalsIgnoreCase("CUT")) {
/*  80 */       if ((curAction != null) && ((curAction.equalsIgnoreCase("Select")) || 
/*  81 */         (curAction.equalsIgnoreCase("MultiSelect"))) && 
/*  82 */         (pgenRsc.getAllSelected() != null) && (!pgenRsc.getAllSelected().isEmpty())) {
/*  83 */         PgenClipboard.getInstance().copy(pgenRsc.getAllSelected());
/*  84 */         pgenRsc.deleteSelectedElements();
/*  85 */         changed = true;
/*     */ 
/*  87 */         if (curAction.equalsIgnoreCase("MultiSelect")) {
/*  88 */           PgenUtil.setMultiSelectMode();
/*     */         }
/*     */         else {
/*  91 */           PgenUtil.setSelectingMode();
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*  96 */     else if (action.equalsIgnoreCase("COPY")) {
/*  97 */       if (pgenRsc.getAllSelected().size() > 0) {
/*  98 */         PgenClipboard.getInstance().copy(pgenRsc.getAllSelected());
/*     */       }
/*     */     }
/* 101 */     else if (action.equalsIgnoreCase("PASTE")) {
/* 102 */       List elms = PgenClipboard.getInstance().getElSelected();
/* 103 */       if ((elms != null) && (elms.size() > 0)) {
/* 104 */         changed = true;
/* 105 */         pgenRsc.addElements(elms);
/*     */       }
/*     */     }
/* 108 */     else if ((action.equalsIgnoreCase("SELECTALL")) && 
/* 109 */       (curAction != null) && ((curAction.equalsIgnoreCase("Select")) || 
/* 110 */       (curAction.equalsIgnoreCase("MultiSelect"))) && 
/* 111 */       (pgenRsc.getActiveLayer().getDrawables().size() > 0))
/*     */     {
/* 113 */       if (curAction.equalsIgnoreCase("MultiSelect")) {
/* 114 */         PgenUtil.setMultiSelectMode();
/*     */       }
/*     */       else {
/* 117 */         PgenUtil.setSelectingMode();
/*     */       }
/*     */ 
/* 120 */       pgenRsc.setSelected(pgenRsc.getActiveLayer().getDrawables());
/*     */     }
/*     */ 
/* 126 */     if (changed) PgenUtil.refresh();
/*     */ 
/* 128 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenCopypasteHotkeyHandler
 * JD-Core Version:    0.6.2
 */