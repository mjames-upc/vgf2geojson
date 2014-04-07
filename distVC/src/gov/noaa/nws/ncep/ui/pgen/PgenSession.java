/*     */ package gov.noaa.nws.ncep.ui.pgen;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.controls.PgenCommandManager;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ 
/*     */ public class PgenSession
/*     */ {
/*  26 */   private static PgenSession instance = null;
/*     */ 
/*  31 */   private PgenResource pgenResource = null;
/*     */ 
/*  36 */   private PgenPaletteWindow palette = null;
/*     */ 
/*     */   public static synchronized PgenSession getInstance()
/*     */   {
/*  51 */     if (instance == null) instance = new PgenSession();
/*  52 */     return instance;
/*     */   }
/*     */ 
/*     */   public void setResource(PgenResource rsc)
/*     */   {
/*  64 */     removeResource();
/*     */ 
/*  67 */     this.pgenResource = rsc;
/*     */ 
/*  69 */     if (this.pgenResource != null) this.pgenResource.getCommandMgr().addStackListener(this.palette);
/*     */   }
/*     */ 
/*     */   public void removeResource()
/*     */   {
/*  77 */     if (this.pgenResource != null)
/*     */     {
/*  79 */       this.pgenResource.getCommandMgr().removeStackListener(this.palette);
/*     */     }
/*  81 */     this.pgenResource = null;
/*     */ 
/*  86 */     if (this.palette != null) this.palette.disableUndoRedo();
/*     */   }
/*     */ 
/*     */   public PgenResource getPgenResource()
/*     */   {
/*  97 */     if (this.pgenResource == null)
/*     */     {
/*  99 */       PgenResource rsc = PgenUtil.findPgenResource(PgenUtil.getActiveEditor());
/* 100 */       if (rsc != null) {
/* 101 */         this.pgenResource = rsc;
/*     */       }
/*     */       else {
/* 104 */         this.pgenResource = PgenUtil.createNewResource();
/*     */       }
/*     */     }
/*     */ 
/* 108 */     return this.pgenResource;
/*     */   }
/*     */ 
/*     */   public PgenResource getCurrentResource()
/*     */   {
/* 116 */     return this.pgenResource;
/*     */   }
/*     */ 
/*     */   public PgenCommandManager getCommandManager()
/*     */   {
/* 124 */     return this.pgenResource.getCommandMgr();
/*     */   }
/*     */ 
/*     */   public void setPalette(PgenPaletteWindow pal)
/*     */   {
/* 132 */     this.palette = pal;
/*     */ 
/* 134 */     if (this.pgenResource != null) this.pgenResource.getCommandMgr().addStackListener(this.palette);
/*     */   }
/*     */ 
/*     */   public void removePalette()
/*     */   {
/* 142 */     if (this.pgenResource != null) this.pgenResource.getCommandMgr().removeStackListener(this.palette);
/* 143 */     this.palette = null;
/*     */   }
/*     */ 
/*     */   public void disableUndoRedo()
/*     */   {
/* 151 */     if (this.pgenResource != null) getCommandManager().clearStacks();
/*     */ 
/* 153 */     if (this.palette != null)
/* 154 */       this.palette.disableUndoRedo();
/*     */   }
/*     */ 
/*     */   public PgenPaletteWindow getPgenPalette()
/*     */   {
/* 163 */     return this.palette;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.PgenSession
 * JD-Core Version:    0.6.2
 */