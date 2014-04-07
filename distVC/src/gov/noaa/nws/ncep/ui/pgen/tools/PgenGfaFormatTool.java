/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.GfaFormatAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaGenerate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ 
/*     */ public class PgenGfaFormatTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   private GfaGenerate gfaGenerate;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  59 */     super.activateTool();
/*     */ 
/*  61 */     if ((this.attrDlg instanceof GfaFormatAttrDlg)) {
/*  62 */       GfaFormatAttrDlg gfaDlg = (GfaFormatAttrDlg)this.attrDlg;
/*  63 */       gfaDlg.setGfaFormatTool(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  76 */     if (this.mouseHandler == null) {
/*  77 */       this.mouseHandler = new PgenGfaFormatHandler();
/*     */     }
/*     */ 
/*  80 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public StringBuilder generate(PgenResource drawingLayer, ArrayList<Gfa> all, List<String> areas, List<String> categories, String dataURI)
/*     */     throws IOException, JAXBException
/*     */   {
/* 130 */     StringBuilder sb = getGfaGenerate().generate(all, areas, categories, 
/* 131 */       dataURI);
/*     */ 
/* 133 */     return sb;
/*     */   }
/*     */ 
/*     */   private GfaGenerate getGfaGenerate() {
/* 137 */     if (this.gfaGenerate == null) {
/* 138 */       this.gfaGenerate = new GfaGenerate();
/*     */     }
/* 140 */     return this.gfaGenerate;
/*     */   }
/*     */ 
/*     */   public class PgenGfaFormatHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     public PgenGfaFormatHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 100 */       if (!PgenGfaFormatTool.this.isResourceEditable()) {
/* 101 */         return false;
/*     */       }
/* 103 */       if (button == 1)
/*     */       {
/* 105 */         PgenGfaFormatTool.this.mapEditor.refresh();
/*     */ 
/* 107 */         return true;
/*     */       }
/* 109 */       if (button == 3)
/*     */       {
/* 111 */         PgenUtil.setSelectingMode();
/* 112 */         return true;
/*     */       }
/*     */ 
/* 115 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 121 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenGfaFormatTool
 * JD-Core Version:    0.6.2
 */