/*    */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*    */ 
/*    */ public class GfaWording
/*    */ {
/* 27 */   String fromCondsDvlpg = "";
/* 28 */   String fromCondsEndg = "";
/* 29 */   String genOlk = "NO";
/* 30 */   String condsContg = "";
/* 31 */   String otlkCondsDvlpg = "";
/* 32 */   String otlkCondsEndg = "";
/*    */ 
/*    */   public String toString()
/*    */   {
/* 36 */     StringBuilder sb = new StringBuilder(200);
/*    */ 
/* 38 */     sb.append("<fromCondsDvlpg>" + Gfa.nvl(this.fromCondsDvlpg) + "</fromCondsDvlpg>\n");
/* 39 */     sb.append("<fromCondsEndg>" + Gfa.nvl(this.fromCondsEndg) + "</fromCondsEndg>\n");
/* 40 */     sb.append("<genOlk>" + Gfa.nvl(this.genOlk) + "</genOlk>\n");
/* 41 */     sb.append("<condsContg>" + Gfa.nvl(this.condsContg) + "</condsContg>\n");
/* 42 */     sb.append("<otlkCondsDvlpg>" + Gfa.nvl(this.otlkCondsDvlpg) + "</otlkCondsDvlpg>\n");
/* 43 */     sb.append("<otlkCondsEndg>" + Gfa.nvl(this.otlkCondsEndg) + "</otlkCondsEndg>\n");
/*    */ 
/* 45 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 50 */     if (!(obj instanceof GfaWording)) {
/* 51 */       return false;
/*    */     }
/* 53 */     GfaWording w = (GfaWording)obj;
/* 54 */     return (this.fromCondsDvlpg.equals(w.fromCondsDvlpg)) && 
/* 55 */       (this.fromCondsEndg.equals(w.fromCondsEndg)) && 
/* 56 */       (this.genOlk.equals(w.genOlk)) && 
/* 57 */       (this.condsContg.equals(w.condsContg)) && 
/* 58 */       (this.otlkCondsDvlpg.equals(w.otlkCondsDvlpg)) && 
/* 59 */       (this.otlkCondsEndg.equals(w.otlkCondsEndg));
/*    */   }
/*    */ 
/*    */   public String getFromCondsDvlpg() {
/* 63 */     return this.fromCondsDvlpg;
/*    */   }
/*    */ 
/*    */   public String getFromCondsEndg() {
/* 67 */     return this.fromCondsEndg;
/*    */   }
/*    */ 
/*    */   public String getGenOlk() {
/* 71 */     return this.genOlk;
/*    */   }
/*    */ 
/*    */   public String getOtlkCondsDvlpg() {
/* 75 */     return this.otlkCondsDvlpg;
/*    */   }
/*    */ 
/*    */   public String getOtlkCondsEndg() {
/* 79 */     return this.otlkCondsEndg;
/*    */   }
/*    */ 
/*    */   public String getCondsContg() {
/* 83 */     return this.condsContg;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.GfaWording
 * JD-Core Version:    0.6.2
 */