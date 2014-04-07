/*    */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*    */ 
/*    */ import org.eclipse.swt.widgets.Button;
/*    */ import org.eclipse.swt.widgets.Control;
/*    */ 
/*    */ public class RadioBtnGroup
/*    */ {
/* 43 */   private Button[] btnGroup = null;
/*    */ 
/*    */   public RadioBtnGroup(Button[] btns)
/*    */   {
/* 62 */     this.btnGroup = btns;
/*    */   }
/*    */ 
/*    */   public void enableBtn(Button btn, Control[] tbEnabled, Control[] tbDisabled)
/*    */   {
/* 72 */     for (Button button : this.btnGroup)
/* 73 */       if (button == btn) {
/* 74 */         button.setSelection(true);
/*    */ 
/* 76 */         if (tbEnabled != null) {
/* 77 */           for (Control cen : tbEnabled) {
/* 78 */             cen.setEnabled(true);
/*    */           }
/*    */         }
/* 81 */         if (tbDisabled != null) {
/* 82 */           for (Control cdis : tbDisabled) {
/* 83 */             cdis.setEnabled(false);
/*    */           }
/*    */         }
/*    */       }
/* 87 */       else if ((button != null) && (!button.isDisposed())) {
/* 88 */         button.setSelection(false);
/*    */       }
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.RadioBtnGroup
 * JD-Core Version:    0.6.2
 */