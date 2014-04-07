/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.CcfpAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.VaaCloudDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.VolcanoVaaAttrDlg;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class AttrDlgFactory
/*     */ {
/*     */   public static AttrDlg createAttrDlg(String pgenCategory, String pgenType, Shell parShell)
/*     */   {
/*  61 */     if (pgenCategory == null) {
/*  62 */       return null;
/*     */     }
/*     */ 
/*  65 */     if ((pgenCategory.equalsIgnoreCase("Lines")) || (
/*  67 */       (pgenType != null) && (pgenType.equalsIgnoreCase("STATUS_LINE"))))
/*     */     {
/*  69 */       return LineAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/*  73 */     if (pgenCategory.equalsIgnoreCase("Front")) {
/*  74 */       return FrontAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/*  77 */     if ((pgenCategory.equalsIgnoreCase("Marker")) || 
/*  78 */       (pgenCategory.equalsIgnoreCase("Symbol")) || 
/*  79 */       (pgenCategory.equalsIgnoreCase("Combo")))
/*     */     {
/*  81 */       if ((pgenType != null) && (pgenType.equalsIgnoreCase("PRESENT_WX_201"))) {
/*  82 */         return VolcanoAttrDlg.getInstance(parShell);
/*     */       }
/*     */ 
/*  85 */       AttrDlg symDlg = LabeledSymbolAttrDlg.getInstance(parShell);
/*  86 */       symDlg.setPgenCategory(pgenCategory);
/*  87 */       symDlg.setPgenType(pgenType);
/*  88 */       return symDlg;
/*     */     }
/*     */ 
/*  93 */     if (pgenCategory.equalsIgnoreCase("Text"))
/*     */     {
/*  95 */       if ((pgenType != null) && (pgenType.equalsIgnoreCase("AVIATION_TEXT"))) {
/*  96 */         return AvnTextAttrDlg.getInstance(parShell);
/*     */       }
/*  98 */       if ((pgenType != null) && (pgenType.equalsIgnoreCase("MID_LEVEL_CLOUD"))) {
/*  99 */         return MidLevelCloudAttrDlg.getInstance(parShell);
/*     */       }
/*     */ 
/* 102 */       return TextAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 107 */     if (pgenCategory.equalsIgnoreCase("Arc"))
/*     */     {
/* 109 */       return ArcAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 112 */     if (pgenCategory.equalsIgnoreCase("Track"))
/*     */     {
/* 114 */       return TrackAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 117 */     if (pgenCategory.equalsIgnoreCase("TRACK_EXTRA_POINTS_INFO"))
/*     */     {
/* 119 */       return TrackExtrapPointInfoDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 122 */     if (pgenCategory.equalsIgnoreCase("Vector"))
/*     */     {
/* 124 */       return VectorAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 127 */     if (pgenCategory.equalsIgnoreCase("Extrap"))
/*     */     {
/* 129 */       return PgenExtrapDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 132 */     if (pgenCategory.equalsIgnoreCase("Interp"))
/*     */     {
/* 134 */       return PgenInterpDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 137 */     if (pgenCategory.equalsIgnoreCase("IncDec"))
/*     */     {
/* 139 */       return IncDecDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 142 */     if (pgenCategory.equalsIgnoreCase("Distance"))
/*     */     {
/* 144 */       return PgenDistanceDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 147 */     if (pgenCategory.equalsIgnoreCase("From"))
/*     */     {
/* 149 */       return FromAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 152 */     if (pgenCategory.equalsIgnoreCase("Cycle"))
/*     */     {
/* 154 */       return CycleDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 157 */     if (pgenCategory.equalsIgnoreCase("Prod_AIRMET"))
/*     */     {
/* 159 */       return GfaFormatAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 162 */     if ((pgenType != null) && (pgenType.equalsIgnoreCase("Jet")))
/*     */     {
/* 164 */       return JetAttrDlg.getInstance(parShell);
/*     */     }
/* 166 */     if ((pgenType != null) && (pgenType.equalsIgnoreCase("Outlook"))) {
/* 167 */       return OutlookAttrDlg.getInstance(parShell);
/*     */     }
/* 169 */     if ((pgenType != null) && (pgenType.equalsIgnoreCase("TCA")))
/*     */     {
/* 171 */       return TcaAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 174 */     if ((pgenType != null) && (pgenType.equalsIgnoreCase("WatchBox")))
/*     */     {
/* 176 */       return WatchBoxAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 179 */     if ((pgenType != null) && (pgenType.equalsIgnoreCase("SPENES"))) {
/* 180 */       return SpenesAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 183 */     if ((pgenType != null) && (pgenType.equalsIgnoreCase("TCM")))
/*     */     {
/* 185 */       return TcmAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 188 */     if ((pgenType != null) && (pgenType.equalsIgnoreCase("Cloud")))
/*     */     {
/* 190 */       return CloudAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 193 */     if ((pgenType != null) && (pgenType.equalsIgnoreCase("Turbulence")))
/*     */     {
/* 195 */       return TurbAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 198 */     if ((pgenType != null) && (pgenType.equalsIgnoreCase("Contours")))
/*     */     {
/* 200 */       return ContoursAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 203 */     if (pgenCategory.equalsIgnoreCase("SIGMET"))
/*     */     {
/* 205 */       if ((pgenType != null) && (pgenType.equalsIgnoreCase("INTL_SIGMET"))) {
/* 206 */         SigmetAttrDlg sigAttrDlg = SigmetAttrDlg.getInstance(parShell);
/* 207 */         sigAttrDlg.setPgenCategory(pgenCategory);
/* 208 */         sigAttrDlg.setPgenType(pgenType);
/* 209 */         return sigAttrDlg;
/* 210 */       }if ((pgenType != null) && (pgenType.equalsIgnoreCase("VOLC_SIGMET"))) {
/* 211 */         VolcanoVaaAttrDlg vaaDlg = VolcanoVaaAttrDlg.getInstance(parShell);
/* 212 */         vaaDlg.setPgenCategory(pgenCategory);
/* 213 */         vaaDlg.setPgenType(pgenType);
/* 214 */         vaaDlg.setFromSelection(true);
/* 215 */         return vaaDlg;
/* 216 */       }if ((pgenType != null) && (pgenType.equalsIgnoreCase("VACL_SIGMET"))) {
/* 217 */         VaaCloudDlg vacDlg = VaaCloudDlg.getInstance(parShell);
/* 218 */         vacDlg.setPgenCategory(pgenCategory);
/* 219 */         vacDlg.setPgenType(pgenType);
/* 220 */         return vacDlg;
/*     */       }
/* 222 */       if ((pgenType != null) && (pgenType.equalsIgnoreCase("CCFP_SIGMET"))) {
/* 223 */         CcfpAttrDlg sigAttrDlg = 
/* 224 */           CcfpAttrDlg.getInstance(parShell);
/* 225 */         sigAttrDlg.setPgenCategory(pgenCategory);
/* 226 */         sigAttrDlg.setPgenType(pgenType);
/* 227 */         return sigAttrDlg;
/*     */       }
/* 229 */       SigmetCommAttrDlg sigAttrDlg = SigmetCommAttrDlg.getInstance(parShell);
/* 230 */       sigAttrDlg.setPgenCategory(pgenCategory);
/* 231 */       sigAttrDlg.setPgenType(pgenType);
/* 232 */       return sigAttrDlg;
/*     */     }
/*     */ 
/* 235 */     if ("GFA".equalsIgnoreCase(pgenType))
/*     */     {
/* 237 */       return GfaAttrDlg.getInstance(parShell);
/*     */     }
/*     */ 
/* 240 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlgFactory
 * JD-Core Version:    0.6.2
 */