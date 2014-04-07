/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import java.io.File;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.HashMap;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ 
/*     */ public class LinePatternManager
/*     */ {
/*  38 */   private static LinePatternManager instance = null;
/*     */   private HashMap<String, LinePattern> patternMap;
/*     */ 
/*     */   protected LinePatternManager()
/*     */   {
/*  50 */     this.patternMap = new HashMap();
/*  51 */     initialize();
/*     */   }
/*     */ 
/*     */   public static synchronized LinePatternManager getInstance()
/*     */   {
/*  61 */     if (instance == null) {
/*  62 */       instance = new LinePatternManager();
/*     */     }
/*  64 */     return instance;
/*     */   }
/*     */ 
/*     */   private void initialize()
/*     */   {
/*  73 */     loadInternal();
/*  74 */     File patterns = PgenStaticDataProvider.getProvider().getStaticFile(
/*  75 */       PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "linePatterns.xml");
/*     */ 
/*  78 */     if ((patterns != null) && (patterns.exists()))
/*  79 */       loadPatternsFromFile(patterns.getAbsolutePath());
/*     */   }
/*     */ 
/*     */   public LinePattern getLinePattern(String key)
/*     */     throws LinePatternException
/*     */   {
/*  91 */     LinePattern pattern = (LinePattern)this.patternMap.get(key);
/*  92 */     if (pattern == null) throw new LinePatternException("Could not find line pattern: " + key);
/*  93 */     return pattern;
/*     */   }
/*     */ 
/*     */   public String[] getPatternNames()
/*     */   {
/* 102 */     String[] names = new String[this.patternMap.size()];
/*     */ 
/* 104 */     int i = 0;
/* 105 */     for (LinePattern lp : this.patternMap.values()) {
/* 106 */       names[(i++)] = lp.getName();
/*     */     }
/* 108 */     return names;
/*     */   }
/*     */ 
/*     */   public String[] getPatternIds()
/*     */   {
/* 118 */     String[] ids = new String[this.patternMap.size()];
/*     */ 
/* 120 */     int i = 0;
/* 121 */     for (String str : this.patternMap.keySet()) {
/* 122 */       ids[(i++)] = str;
/*     */     }
/* 124 */     return ids;
/*     */   }
/*     */ 
/*     */   private void loadInternal()
/*     */   {
/* 136 */     LinePattern lp = new LinePattern("Solid Line", false, null);
/* 137 */     this.patternMap.put("LINE_SOLID", lp);
/*     */ 
/* 139 */     lp = new LinePattern("Line with open arrow head", true, ArrowHead.ArrowHeadType.OPEN);
/* 140 */     this.patternMap.put("POINTED_ARROW", lp);
/*     */ 
/* 142 */     lp = new LinePattern("Line with closed arrow head", true, ArrowHead.ArrowHeadType.FILLED);
/* 143 */     this.patternMap.put("FILLED_ARROW", lp);
/*     */ 
/* 145 */     lp = new LinePattern("Dotted Line", false, null);
/* 146 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 147 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 148 */     this.patternMap.put("LINE_DASHED_2", lp);
/*     */ 
/* 150 */     lp = new LinePattern("Short Dashed", false, null);
/* 151 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 152 */     lp.addSegment(new PatternSegment(3.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 153 */     this.patternMap.put("LINE_DASHED_3", lp);
/*     */ 
/* 155 */     lp = new LinePattern("Medium Dashed", false, null);
/* 156 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 157 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 158 */     this.patternMap.put("LINE_DASHED_4", lp);
/*     */ 
/* 160 */     lp = new LinePattern("Long Dash Short Dash", false, null);
/* 161 */     lp.addSegment(new PatternSegment(6.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 162 */     lp.addSegment(new PatternSegment(3.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 163 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 164 */     lp.addSegment(new PatternSegment(3.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 165 */     this.patternMap.put("LINE_DASHED_5", lp);
/*     */ 
/* 167 */     lp = new LinePattern("Long Dashed", false, null);
/* 168 */     lp.addSegment(new PatternSegment(5.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 169 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 170 */     this.patternMap.put("LINE_DASHED_6", lp);
/*     */ 
/* 172 */     lp = new LinePattern("Long Dash Three Short Dashes", false, null);
/* 173 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 174 */     lp.addSegment(new PatternSegment(3.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 175 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 176 */     lp.addSegment(new PatternSegment(3.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 177 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 178 */     lp.addSegment(new PatternSegment(3.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 179 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 180 */     lp.addSegment(new PatternSegment(3.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 181 */     this.patternMap.put("LINE_DASHED_7", lp);
/*     */ 
/* 183 */     lp = new LinePattern("Long Dash Dot", false, null);
/* 184 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 185 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 186 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 187 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 188 */     this.patternMap.put("LINE_DASHED_8", lp);
/*     */ 
/* 190 */     lp = new LinePattern("Medium Dash Dot Dot Dot", false, null);
/* 191 */     lp.addSegment(new PatternSegment(6.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 192 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 193 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 194 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 195 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 196 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 197 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 198 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 199 */     this.patternMap.put("LINE_DASHED_9", lp);
/*     */ 
/* 201 */     lp = new LinePattern("Long Dash Dot Dot", false, null);
/* 202 */     lp.addSegment(new PatternSegment(15.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 203 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 204 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 205 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 206 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 207 */     lp.addSegment(new PatternSegment(0.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 208 */     this.patternMap.put("LINE_DASHED_10", lp);
/*     */ 
/* 210 */     lp = new LinePattern("Dashed Line with open arrow head", true, ArrowHead.ArrowHeadType.OPEN);
/* 211 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 212 */     lp.addSegment(new PatternSegment(6.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 213 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 214 */     this.patternMap.put("DASHED_ARROW", lp);
/*     */ 
/* 216 */     lp = new LinePattern("Dashed Line with filled arrow head", true, ArrowHead.ArrowHeadType.FILLED);
/* 217 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 218 */     lp.addSegment(new PatternSegment(6.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 219 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 220 */     this.patternMap.put("DASHED_ARROW_FILLED", lp);
/*     */ 
/* 222 */     lp = new LinePattern("Ball-and-Chain", false, null);
/* 223 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 224 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 225 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.CIRCLE, 0, 8, 0, false));
/* 226 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 227 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 228 */     this.patternMap.put("BALL_CHAIN", lp);
/*     */ 
/* 230 */     lp = new LinePattern("ZigZag", false, null);
/* 231 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE, 0, 2, 0, false));
/* 232 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE, 0, 2, 0, true));
/* 233 */     this.patternMap.put("ZIGZAG", lp);
/*     */ 
/* 235 */     lp = new LinePattern("Scallop", false, null);
/* 236 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE, 0, 8, 0, false));
/* 237 */     this.patternMap.put("SCALLOPED", lp);
/*     */ 
/* 239 */     lp = new LinePattern("Alternating Angled Ticks", false, null);
/* 240 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 241 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.ARC_90_DEGREE, 0, 1, 0, false));
/* 242 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 243 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.ARC_90_DEGREE, 0, 1, 0, true));
/* 244 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 245 */     this.patternMap.put("ANGLED_TICKS_ALT", lp);
/*     */ 
/* 247 */     lp = new LinePattern("Filled Circle", false, null);
/* 248 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 249 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 250 */     this.patternMap.put("FILLED_CIRCLES", lp);
/*     */ 
/* 252 */     lp = new LinePattern("Line-Caret-Line", false, null);
/* 253 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 254 */     lp.addSegment(new PatternSegment(6.0D, PatternSegment.PatternType.ARC_270_DEGREE_WITH_LINE, 0, 2, 0, false));
/* 255 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 256 */     this.patternMap.put("LINE_WITH_CARETS", lp);
/*     */ 
/* 258 */     lp = new LinePattern("Line-Caret-Line with spaces", false, null);
/* 259 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 260 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 261 */     lp.addSegment(new PatternSegment(6.0D, PatternSegment.PatternType.ARC_270_DEGREE, 0, 2, 0, false));
/* 262 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 263 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 264 */     this.patternMap.put("LINE_CARET_LINE", lp);
/*     */ 
/* 266 */     lp = new LinePattern("Sine Curve", false, null);
/* 267 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE, 0, 8, 0, false));
/* 268 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE, 0, 8, 0, true));
/* 269 */     this.patternMap.put("SINE_CURVE", lp);
/*     */ 
/* 271 */     lp = new LinePattern("Stationary Front at the surface", false, null);
/* 272 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 273 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 8, 0, true));
/* 274 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 275 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 1, 0, 0, false));
/* 276 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 1, 2, 0, false));
/* 277 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 1, 0, 0, false));
/* 278 */     this.patternMap.put("STATIONARY_FRONT", lp);
/*     */ 
/* 280 */     lp = new LinePattern("Stationary Front Frontogenesis", false, null);
/* 281 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 282 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 8, 0, true));
/* 283 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 284 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 285 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 1, 0, 0, false));
/* 286 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 1, 2, 0, false));
/* 287 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 1, 0, 0, false));
/* 288 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 289 */     this.patternMap.put("STATIONARY_FRONT_FORM", lp);
/*     */ 
/* 291 */     lp = new LinePattern("Stationary Front Frontolysis", false, null);
/* 292 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 293 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 8, 0, true));
/* 294 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 295 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 296 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 297 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 1, 0, 0, false));
/* 298 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 1, 0, 0, false));
/* 299 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 1, 0, 0, false));
/* 300 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 1, 2, 0, false));
/* 301 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 1, 0, 0, false));
/* 302 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 1, 0, 0, false));
/* 303 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 1, 0, 0, false));
/* 304 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 305 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 306 */     this.patternMap.put("STATIONARY_FRONT_DISS", lp);
/*     */ 
/* 308 */     lp = new LinePattern("Warm Front at the surface", false, null);
/* 309 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 310 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 8, 0, false));
/* 311 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 312 */     this.patternMap.put("WARM_FRONT", lp);
/*     */ 
/* 314 */     lp = new LinePattern("Warm Front Frontogenesis", false, null);
/* 315 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 316 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 8, 0, false));
/* 317 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 318 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 319 */     this.patternMap.put("WARM_FRONT_FORM", lp);
/*     */ 
/* 321 */     lp = new LinePattern("Warm Front Frontolysis", false, null);
/* 322 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 323 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 8, 0, false));
/* 324 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 325 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 326 */     lp.addSegment(new PatternSegment(16.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 327 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 328 */     this.patternMap.put("WARM_FRONT_DISS", lp);
/*     */ 
/* 330 */     lp = new LinePattern("Cold Front at the surface", false, null);
/* 331 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 332 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 2, 0, false));
/* 333 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 334 */     this.patternMap.put("COLD_FRONT", lp);
/*     */ 
/* 336 */     lp = new LinePattern("Cold Front Frontogenesis", false, null);
/* 337 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 338 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 2, 0, false));
/* 339 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 340 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 341 */     this.patternMap.put("COLD_FRONT_FORM", lp);
/*     */ 
/* 343 */     lp = new LinePattern("Cold Front Frontolysis", false, null);
/* 344 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 345 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 2, 0, false));
/* 346 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 347 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 348 */     lp.addSegment(new PatternSegment(16.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 349 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 350 */     this.patternMap.put("COLD_FRONT_DISS", lp);
/*     */ 
/* 352 */     lp = new LinePattern("Occluded Front at the surface", false, null);
/* 353 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 354 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 2, 0, false));
/* 355 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 356 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 357 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 8, 0, false));
/* 358 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 359 */     this.patternMap.put("OCCLUDED_FRONT", lp);
/*     */ 
/* 361 */     lp = new LinePattern("Occluded Front Frontogenesis", false, null);
/* 362 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 363 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 2, 0, false));
/* 364 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 365 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 366 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 367 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 8, 0, false));
/* 368 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 369 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 370 */     this.patternMap.put("OCCLUDED_FRONT_FORM", lp);
/*     */ 
/* 372 */     lp = new LinePattern("Occluded Front Frontolysis", false, null);
/* 373 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 374 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 2, 0, false));
/* 375 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 376 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 377 */     lp.addSegment(new PatternSegment(16.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 378 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 379 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 380 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_FILLED, 0, 8, 0, false));
/* 381 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 382 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 383 */     lp.addSegment(new PatternSegment(16.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 384 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 385 */     this.patternMap.put("OCCLUDED_FRONT_DISS", lp);
/*     */ 
/* 387 */     lp = new LinePattern("TROF", false, null);
/* 388 */     lp.addSegment(new PatternSegment(12.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 389 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 390 */     this.patternMap.put("TROF", lp);
/*     */ 
/* 392 */     lp = new LinePattern("Tropical TROF", false, null);
/* 393 */     this.patternMap.put("TROPICAL_TROF", lp);
/*     */ 
/* 395 */     lp = new LinePattern("Dry-Line", false, null);
/* 396 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARC_180_DEGREE_CLOSED, 0, 8, 0, false));
/* 397 */     this.patternMap.put("DRY_LINE", lp);
/*     */ 
/* 399 */     lp = new LinePattern("Instability (Squall) Line", false, null);
/* 400 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 401 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 402 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 403 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 404 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 405 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 406 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 407 */     this.patternMap.put("INSTABILITY", lp);
/*     */ 
/* 409 */     lp = new LinePattern("Box-Circle", false, null);
/* 410 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.BOX, 0, 0, 2, false));
/* 411 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 412 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.CIRCLE, 0, 8, 0, false));
/* 413 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 414 */     this.patternMap.put("BOX_CIRCLE", lp);
/*     */ 
/* 416 */     lp = new LinePattern("Filled Box-Open Box with filled arrow head", true, ArrowHead.ArrowHeadType.FILLED);
/* 417 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BOX_FILLED, 0, 0, 2, false));
/* 418 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BOX, 0, 0, 2, false));
/* 419 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.BOX_FILLED, 0, 0, 2, false));
/* 420 */     this.patternMap.put("FILL_OPEN_BOX", lp);
/*     */ 
/* 422 */     lp = new LinePattern("Line-X-Line", false, null);
/* 423 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 424 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 425 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.X_PATTERN, 0, 0, 2, false));
/* 426 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 427 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 428 */     this.patternMap.put("LINE_X_LINE", lp);
/*     */ 
/* 430 */     lp = new LinePattern("Line-2Xs-Line", false, null);
/* 431 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 432 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 433 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.X_PATTERN, 0, 0, 2, false));
/* 434 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 435 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.X_PATTERN, 0, 0, 2, false));
/* 436 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 437 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 438 */     this.patternMap.put("LINE_XX_LINE", lp);
/*     */ 
/* 440 */     lp = new LinePattern("Filled Circle-X", false, null);
/* 441 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 442 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 443 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.X_PATTERN, 0, 0, 2, false));
/* 444 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 445 */     this.patternMap.put("FILL_CIRCLE_X", lp);
/*     */ 
/* 447 */     lp = new LinePattern("Box-X", false, null);
/* 448 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.BOX, 0, 0, 2, false));
/* 449 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 450 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.X_PATTERN, 0, 0, 2, false));
/* 451 */     lp.addSegment(new PatternSegment(2.0D, PatternSegment.PatternType.BLANK, 0, 0, 0, false));
/* 452 */     this.patternMap.put("BOX_X", lp);
/*     */ 
/* 454 */     lp = new LinePattern("Line-Circle-Line with filled arrow head", true, ArrowHead.ArrowHeadType.FILLED);
/* 455 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 456 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.CIRCLE, 0, 8, 0, false));
/* 457 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 458 */     this.patternMap.put("LINE_CIRCLE_ARROW", lp);
/*     */ 
/* 460 */     lp = new LinePattern("Line-Filled-Circle-Line with filled arrow head", true, ArrowHead.ArrowHeadType.FILLED);
/* 461 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 462 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.CIRCLE_FILLED, 0, 8, 0, false));
/* 463 */     lp.addSegment(new PatternSegment(4.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 464 */     this.patternMap.put("LINE_FILLED_CIRCLE_ARROW", lp);
/*     */ 
/* 466 */     lp = new LinePattern("Double Line", false, null);
/* 467 */     lp.addSegment(new PatternSegment(1.0D, PatternSegment.PatternType.DOUBLE_LINE, 0, 0, 2, false));
/* 468 */     this.patternMap.put("DOUBLE_LINE", lp);
/*     */ 
/* 470 */     lp = new LinePattern("Z-Line", false, null);
/* 471 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.Z_PATTERN, 0, 0, 2, false));
/* 472 */     this.patternMap.put("ZZZ_LINE", lp);
/*     */ 
/* 474 */     lp = new LinePattern("Tick Mark", false, null);
/* 475 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.TICK, 0, 0, 2, false));
/* 476 */     this.patternMap.put("TICK_MARKS", lp);
/*     */ 
/* 478 */     lp = new LinePattern("Streamline-like", false, null);
/* 479 */     lp.addSegment(new PatternSegment(12.0D, PatternSegment.PatternType.LINE, 0, 0, 0, false));
/* 480 */     lp.addSegment(new PatternSegment(8.0D, PatternSegment.PatternType.ARROW_HEAD, 0, 2, 0, false));
/* 481 */     this.patternMap.put("STREAM_LINE", lp);
/*     */   }
/*     */ 
/*     */   public void savePatternsToFile(String filename)
/*     */   {
/* 491 */     PrintWriter writer = null;
/*     */ 
/* 496 */     LinePatternList patternList = new LinePatternList(this.patternMap);
/*     */     try
/*     */     {
/* 503 */       JAXBContext context = JAXBContext.newInstance(new Class[] { LinePatternList.class, LinePatternMapEntry.class });
/* 504 */       Marshaller msh = context.createMarshaller();
/* 505 */       msh.setProperty("jaxb.formatted.output", Boolean.TRUE);
/*     */ 
/* 510 */       File fileOut = new File(filename);
/* 511 */       writer = new PrintWriter(fileOut);
/*     */ 
/* 516 */       msh.marshal(patternList, writer);
/*     */     }
/*     */     catch (Exception e) {
/* 519 */       e.printStackTrace();
/*     */     }
/*     */     finally {
/* 522 */       if (writer != null) writer.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void loadPatternsFromFile(String filename)
/*     */   {
/* 534 */     File fileIn = null;
/*     */     try
/*     */     {
/* 538 */       JAXBContext context = JAXBContext.newInstance(new Class[] { LinePatternList.class });
/* 539 */       Unmarshaller msh = context.createUnmarshaller();
/*     */ 
/* 544 */       fileIn = new File(filename);
/* 545 */       LinePatternList lpl = (LinePatternList)msh.unmarshal(fileIn);
/*     */ 
/* 548 */       for (LinePatternMapEntry entry : lpl.getPatternList()) {
/* 549 */         this.patternMap.put(entry.getPatternId(), entry.getPattern());
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 554 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.LinePatternManager
 * JD-Core Version:    0.6.2
 */