/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import java.io.File;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.HashMap;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ 
/*     */ public class SymbolPatternManager
/*     */ {
/*  38 */   private static SymbolPatternManager instance = null;
/*     */   private HashMap<String, SymbolPattern> patternMap;
/*     */ 
/*     */   protected SymbolPatternManager()
/*     */   {
/*  50 */     this.patternMap = new HashMap();
/*  51 */     initialize();
/*     */   }
/*     */ 
/*     */   public static synchronized SymbolPatternManager getInstance()
/*     */   {
/*  61 */     if (instance == null) {
/*  62 */       instance = new SymbolPatternManager();
/*     */     }
/*  64 */     return instance;
/*     */   }
/*     */ 
/*     */   private void initialize()
/*     */   {
/*  73 */     loadInternal();
/*  74 */     File patterns = PgenStaticDataProvider.getProvider().getStaticFile(
/*  75 */       PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "symbolPatterns.xml");
/*  76 */     if ((patterns != null) && (patterns.exists()))
/*  77 */       loadPatternsFromFile(patterns.getAbsolutePath());
/*     */   }
/*     */ 
/*     */   public SymbolPattern getSymbolPattern(String key)
/*     */     throws SymbolPatternException
/*     */   {
/*  89 */     SymbolPattern pattern = (SymbolPattern)this.patternMap.get(key);
/*  90 */     if (pattern == null) throw new SymbolPatternException("Could not find symbol pattern: " + key);
/*  91 */     return pattern;
/*     */   }
/*     */ 
/*     */   public String[] getPatternNames()
/*     */   {
/* 100 */     String[] names = new String[this.patternMap.size()];
/*     */ 
/* 102 */     int i = 0;
/* 103 */     for (SymbolPattern sp : this.patternMap.values()) {
/* 104 */       names[(i++)] = sp.getName();
/*     */     }
/* 106 */     return names;
/*     */   }
/*     */ 
/*     */   public String[] getPatternIds()
/*     */   {
/* 115 */     String[] ids = new String[this.patternMap.size()];
/*     */ 
/* 117 */     int i = 0;
/* 118 */     for (String str : this.patternMap.keySet()) {
/* 119 */       ids[(i++)] = str;
/*     */     }
/* 121 */     return ids;
/*     */   }
/*     */ 
/*     */   private void loadInternal()
/*     */   {
/* 136 */     SymbolPattern sp = new SymbolPattern("Plus Sign");
/* 137 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 0.0D), new Coordinate(3.0D, 0.0D) });
/* 138 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 3.0D), new Coordinate(0.0D, -3.0D) });
/* 139 */     this.patternMap.put("PLUS_SIGN", sp);
/*     */ 
/* 141 */     sp = new SymbolPattern("Octagon");
/* 142 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -3.0D), new Coordinate(1.0D, -3.0D), 
/* 143 */       new Coordinate(3.0D, -1.0D), new Coordinate(3.0D, 1.0D), 
/* 144 */       new Coordinate(1.0D, 3.0D), new Coordinate(-1.0D, 3.0D), 
/* 145 */       new Coordinate(-3.0D, 1.0D), new Coordinate(-3.0D, -1.0D), 
/* 146 */       new Coordinate(-1.0D, -3.0D) });
/* 147 */     this.patternMap.put("OCTAGON", sp);
/*     */ 
/* 149 */     sp = new SymbolPattern("Triangle");
/* 150 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -3.0D), new Coordinate(3.0D, -3.0D), 
/* 151 */       new Coordinate(0.0D, 3.0D), new Coordinate(-3.0D, -3.0D) });
/* 152 */     this.patternMap.put("TRIANGLE", sp);
/*     */ 
/* 154 */     sp = new SymbolPattern("Box");
/* 155 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -3.0D), new Coordinate(3.0D, -3.0D), 
/* 156 */       new Coordinate(3.0D, 3.0D), new Coordinate(-3.0D, 3.0D), 
/* 157 */       new Coordinate(-3.0D, -3.0D) });
/* 158 */     this.patternMap.put("BOX", sp);
/*     */ 
/* 160 */     sp = new SymbolPattern("Small X");
/* 161 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, -2.0D), new Coordinate(2.0D, 2.0D) });
/* 162 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, -2.0D), new Coordinate(-2.0D, 2.0D) });
/* 163 */     this.patternMap.put("SMALL_X", sp);
/*     */ 
/* 165 */     sp = new SymbolPattern("Diamond");
/* 166 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, -3.0D), new Coordinate(-3.0D, 0.0D), 
/* 167 */       new Coordinate(0.0D, 3.0D), new Coordinate(3.0D, 0.0D), 
/* 168 */       new Coordinate(0.0D, -3.0D) });
/* 169 */     this.patternMap.put("DIAMOND", sp);
/*     */ 
/* 171 */     sp = new SymbolPattern("Up Arrow");
/* 172 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, -3.0D), new Coordinate(0.0D, 0.0D), 
/* 173 */       new Coordinate(-3.0D, 0.0D), new Coordinate(0.0D, 3.0D), 
/* 174 */       new Coordinate(3.0D, 0.0D), new Coordinate(0.0D, 0.0D) });
/* 175 */     this.patternMap.put("UP_ARROW", sp);
/*     */ 
/* 177 */     sp = new SymbolPattern("Bar X");
/* 178 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -3.0D), new Coordinate(3.0D, 3.0D), 
/* 179 */       new Coordinate(-3.0D, 3.0D), new Coordinate(3.0D, -3.0D) });
/* 180 */     this.patternMap.put("X_WITH_TOP_BAR", sp);
/*     */ 
/* 182 */     sp = new SymbolPattern("Letter Z");
/* 183 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 3.0D), new Coordinate(3.0D, 3.0D), 
/* 184 */       new Coordinate(-3.0D, -3.0D), new Coordinate(3.0D, -3.0D) });
/* 185 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 0.0D), new Coordinate(2.0D, 0.0D) });
/* 186 */     this.patternMap.put("Z_WITH_BAR", sp);
/*     */ 
/* 188 */     sp = new SymbolPattern("Letter Y");
/* 189 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 3.0D), new Coordinate(0.0D, 0.0D), 
/* 190 */       new Coordinate(0.0D, -3.0D) });
/* 191 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 0.0D), new Coordinate(3.0D, 3.0D) });
/* 192 */     this.patternMap.put("Y", sp);
/*     */ 
/* 194 */     sp = new SymbolPattern("Box X");
/* 195 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -1.0D), new Coordinate(1.0D, -1.0D), 
/* 196 */       new Coordinate(1.0D, 1.0D), new Coordinate(-1.0D, 1.0D), 
/* 197 */       new Coordinate(-1.0D, -1.0D), new Coordinate(-3.0D, -3.0D) });
/* 198 */     sp.addPath(new Coordinate[] { new Coordinate(1.0D, -1.0D), new Coordinate(3.0D, -3.0D) });
/* 199 */     sp.addPath(new Coordinate[] { new Coordinate(1.0D, 1.0D), new Coordinate(3.0D, 3.0D) });
/* 200 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 1.0D), new Coordinate(-3.0D, 3.0D) });
/* 201 */     this.patternMap.put("BOX_WITH_DIAGONALS", sp);
/*     */ 
/* 203 */     sp = new SymbolPattern("Asterisk");
/* 204 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, -2.0D), new Coordinate(2.0D, 2.0D) });
/* 205 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, -2.0D), new Coordinate(-2.0D, 2.0D) });
/* 206 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 0.0D), new Coordinate(3.0D, 0.0D) });
/* 207 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, -3.0D), new Coordinate(0.0D, 3.0D) });
/* 208 */     this.patternMap.put("ASTERISK", sp);
/*     */ 
/* 210 */     sp = new SymbolPattern("Hourglass");
/* 211 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -3.0D), new Coordinate(3.0D, 3.0D), 
/* 212 */       new Coordinate(-3.0D, 3.0D), new Coordinate(3.0D, -3.0D), 
/* 213 */       new Coordinate(-3.0D, -3.0D) });
/* 214 */     this.patternMap.put("HOURGLASS_X", sp);
/*     */ 
/* 216 */     sp = new SymbolPattern("Star");
/* 217 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 3.0D), new Coordinate(-1.0D, 1.0D), 
/* 218 */       new Coordinate(-3.0D, 1.0D), new Coordinate(-1.0D, 0.0D), 
/* 219 */       new Coordinate(-2.0D, -2.0D), new Coordinate(0.0D, -1.0D), 
/* 220 */       new Coordinate(2.0D, -2.0D), new Coordinate(1.0D, 0.0D), 
/* 221 */       new Coordinate(3.0D, 1.0D), new Coordinate(1.0D, 1.0D), 
/* 222 */       new Coordinate(0.0D, 3.0D) });
/* 223 */     this.patternMap.put("STAR", sp);
/*     */ 
/* 225 */     sp = new SymbolPattern("Dot");
/* 226 */     sp.addPath(new Coordinate[] { new Coordinate(0.5D, 0.0D), new Coordinate(0.35D, 0.35D), 
/* 227 */       new Coordinate(0.0D, 0.5D), new Coordinate(-0.35D, 0.35D), 
/* 228 */       new Coordinate(-0.5D, 0.0D), new Coordinate(-0.35D, -0.35D), 
/* 229 */       new Coordinate(0.0D, -0.5D), new Coordinate(0.35D, -0.35D), 
/* 230 */       new Coordinate(0.5D, 0.0D) }, true);
/* 231 */     this.patternMap.put("DOT", sp);
/*     */ 
/* 233 */     sp = new SymbolPattern("Large X");
/* 234 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -3.0D), new Coordinate(3.0D, 3.0D) });
/* 235 */     sp.addPath(new Coordinate[] { new Coordinate(3.0D, -3.0D), new Coordinate(-3.0D, 3.0D) });
/* 236 */     this.patternMap.put("LARGE_X", sp);
/*     */ 
/* 238 */     sp = new SymbolPattern("Filled Octagon");
/* 239 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -3.0D), new Coordinate(1.0D, -3.0D), 
/* 240 */       new Coordinate(3.0D, -1.0D), new Coordinate(3.0D, 1.0D), 
/* 241 */       new Coordinate(1.0D, 3.0D), new Coordinate(-1.0D, 3.0D), 
/* 242 */       new Coordinate(-3.0D, 1.0D), new Coordinate(-3.0D, -1.0D), 
/* 243 */       new Coordinate(-1.0D, -3.0D) }, true);
/* 244 */     this.patternMap.put("FILLED_OCTAGON", sp);
/*     */ 
/* 246 */     sp = new SymbolPattern("Filled Triangle");
/* 247 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -3.0D), new Coordinate(3.0D, -3.0D), 
/* 248 */       new Coordinate(0.0D, 3.0D), new Coordinate(-3.0D, -3.0D) }, true);
/* 249 */     this.patternMap.put("FILLED_TRIANGLE", sp);
/*     */ 
/* 251 */     sp = new SymbolPattern("Filled Box");
/* 252 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -3.0D), new Coordinate(3.0D, -3.0D), 
/* 253 */       new Coordinate(3.0D, 3.0D), new Coordinate(-3.0D, 3.0D), 
/* 254 */       new Coordinate(-3.0D, -3.0D) }, true);
/* 255 */     this.patternMap.put("FILLED_BOX", sp);
/*     */ 
/* 257 */     sp = new SymbolPattern("Filled Diamond");
/* 258 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, -3.0D), new Coordinate(-3.0D, 0.0D), 
/* 259 */       new Coordinate(0.0D, 3.0D), new Coordinate(3.0D, 0.0D), 
/* 260 */       new Coordinate(0.0D, -3.0D) }, true);
/* 261 */     this.patternMap.put("FILLED_DIAMOND", sp);
/*     */ 
/* 263 */     sp = new SymbolPattern("Filled Star");
/* 264 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 3.0D), new Coordinate(-1.0D, 1.0D), 
/* 265 */       new Coordinate(-3.0D, 1.0D), new Coordinate(-1.0D, 0.0D), 
/* 266 */       new Coordinate(-2.0D, -2.0D), new Coordinate(0.0D, -1.0D), 
/* 267 */       new Coordinate(2.0D, -2.0D), new Coordinate(1.0D, 0.0D), 
/* 268 */       new Coordinate(3.0D, 1.0D), new Coordinate(1.0D, 1.0D), 
/* 269 */       new Coordinate(0.0D, 3.0D) }, true);
/* 270 */     this.patternMap.put("FILLED_STAR", sp);
/*     */ 
/* 272 */     sp = new SymbolPattern("Minus Sign");
/* 273 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 0.0D), new Coordinate(3.0D, 0.0D) });
/* 274 */     this.patternMap.put("MINUS_SIGN", sp);
/*     */ 
/* 279 */     sp = new SymbolPattern("Haze");
/* 280 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -1.0D), new Coordinate(-3.0D, 0.0D), 
/* 281 */       new Coordinate(-2.0D, 1.0D), new Coordinate(-1.0D, 1.0D), 
/* 282 */       new Coordinate(2.0D, -2.0D), new Coordinate(3.0D, -2.0D), 
/* 283 */       new Coordinate(4.0D, -1.0D), new Coordinate(4.0D, 0.0D), 
/* 284 */       new Coordinate(3.0D, 1.0D), new Coordinate(2.0D, 1.0D), 
/* 285 */       new Coordinate(-1.0D, -2.0D), new Coordinate(-2.0D, -2.0D), 
/* 286 */       new Coordinate(-3.0D, -1.0D) });
/* 287 */     this.patternMap.put("PRESENT_WX_005", sp);
/*     */ 
/* 289 */     sp = new SymbolPattern("Light Fog");
/* 290 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -1.0D), new Coordinate(3.0D, -1.0D) });
/* 291 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 1.0D), new Coordinate(3.0D, 1.0D) });
/* 292 */     this.patternMap.put("PRESENT_WX_010", sp);
/*     */ 
/* 294 */     sp = new SymbolPattern("Fog, Sky not discernible");
/* 295 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -2.0D), new Coordinate(3.0D, -2.0D) });
/* 296 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 0.0D), new Coordinate(3.0D, 0.0D) });
/* 297 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 2.0D), new Coordinate(3.0D, 2.0D) });
/* 298 */     this.patternMap.put("PRESENT_WX_045", sp);
/*     */ 
/* 300 */     sp = new SymbolPattern("Continuous drizzle, slight at observation time");
/* 301 */     sp.addDot(new Coordinate(-2.0D, 1.0D), 0.75D);
/* 302 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, 1.0D), new Coordinate(-2.0D, 0.0D), 
/* 303 */       new Coordinate(-3.0D, -1.0D) });
/* 304 */     sp.addDot(new Coordinate(2.0D, 1.0D), 0.75D);
/* 305 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, 1.0D), new Coordinate(2.0D, 0.0D), 
/* 306 */       new Coordinate(1.0D, -1.0D) });
/* 307 */     this.patternMap.put("PRESENT_WX_051", sp);
/*     */ 
/* 309 */     sp = new SymbolPattern("Slight freezing drizzle");
/* 310 */     sp.addDot(new Coordinate(-3.0D, 1.0D), 0.75D);
/* 311 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 1.0D), new Coordinate(-3.0D, -0.0D), 
/* 312 */       new Coordinate(-4.0D, -1.0D) });
/* 313 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, -2.0D), new Coordinate(-5.0D, -1.0D), 
/* 314 */       new Coordinate(-5.0D, 0.0D), new Coordinate(-5.0D, 1.0D), 
/* 315 */       new Coordinate(-4.0D, 2.0D), new Coordinate(-2.0D, 2.0D), 
/* 316 */       new Coordinate(2.0D, -2.0D), new Coordinate(4.0D, -2.0D), 
/* 317 */       new Coordinate(5.0D, -1.0D), new Coordinate(5.0D, 1.0D), 
/* 318 */       new Coordinate(4.0D, 2.0D) });
/* 319 */     this.patternMap.put("PRESENT_WX_056", sp);
/*     */ 
/* 321 */     sp = new SymbolPattern("Continuous rain");
/* 322 */     sp.addDot(new Coordinate(-2.0D, 0.0D), 1.1D);
/* 323 */     sp.addDot(new Coordinate(2.0D, 0.0D), 1.1D);
/* 324 */     this.patternMap.put("PRESENT_WX_061", sp);
/*     */ 
/* 326 */     sp = new SymbolPattern("Continuous moderate rain");
/* 327 */     sp.addDot(new Coordinate(-2.0D, -1.0D), 1.1D);
/* 328 */     sp.addDot(new Coordinate(2.0D, -1.0D), 1.1D);
/* 329 */     sp.addDot(new Coordinate(0.0D, 2.0D), 1.1D);
/* 330 */     this.patternMap.put("PRESENT_WX_063", sp);
/*     */ 
/* 332 */     sp = new SymbolPattern("Continuous heavy rain");
/* 333 */     sp.addDot(new Coordinate(0.0D, -3.0D), 1.1D);
/* 334 */     sp.addDot(new Coordinate(-2.0D, 0.0D), 1.1D);
/* 335 */     sp.addDot(new Coordinate(2.0D, 0.0D), 1.1D);
/* 336 */     sp.addDot(new Coordinate(0.0D, 3.0D), 1.1D);
/* 337 */     this.patternMap.put("PRESENT_WX_065", sp);
/*     */ 
/* 339 */     sp = new SymbolPattern("Slight freezing rain");
/* 340 */     sp.addDot(new Coordinate(-3.0D, 0.0D), 1.1D);
/* 341 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, -2.0D), new Coordinate(-5.0D, -1.0D), 
/* 342 */       new Coordinate(-5.0D, 0.0D), new Coordinate(-5.0D, 1.0D), 
/* 343 */       new Coordinate(-4.0D, 2.0D), new Coordinate(-2.0D, 2.0D), 
/* 344 */       new Coordinate(2.0D, -2.0D), new Coordinate(4.0D, -2.0D), 
/* 345 */       new Coordinate(5.0D, -1.0D), new Coordinate(5.0D, 1.0D), 
/* 346 */       new Coordinate(4.0D, 2.0D) });
/* 347 */     this.patternMap.put("PRESENT_WX_066", sp);
/*     */ 
/* 349 */     sp = new SymbolPattern("Continuous Light Snow");
/* 350 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, -1.0D), new Coordinate(-2.0D, 1.0D) });
/* 351 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-2.0D, -1.0D) });
/* 352 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, 0.0D), new Coordinate(-1.0D, 0.0D) });
/* 353 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, -1.0D), new Coordinate(4.0D, 1.0D) });
/* 354 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, 1.0D), new Coordinate(4.0D, -1.0D) });
/* 355 */     sp.addPath(new Coordinate[] { new Coordinate(1.0D, 0.0D), new Coordinate(5.0D, 0.0D) });
/* 356 */     this.patternMap.put("PRESENT_WX_071", sp);
/*     */ 
/* 358 */     sp = new SymbolPattern("Moderate Snow");
/* 359 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 0.0D), new Coordinate(-2.0D, -2.0D) });
/* 360 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, -2.0D), new Coordinate(-2.0D, 0.0D) });
/* 361 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, -1.0D), new Coordinate(-1.0D, -1.0D) });
/* 362 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, 0.0D), new Coordinate(4.0D, -2.0D) });
/* 363 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, -2.0D), new Coordinate(4.0D, 0.0D) });
/* 364 */     sp.addPath(new Coordinate[] { new Coordinate(1.0D, -1.0D), new Coordinate(5.0D, -1.0D) });
/* 365 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 3.0D), new Coordinate(1.0D, 1.0D) });
/* 366 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 1.0D), new Coordinate(1.0D, 3.0D) });
/* 367 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, 2.0D), new Coordinate(2.0D, 2.0D) });
/* 368 */     this.patternMap.put("PRESENT_WX_073", sp);
/*     */ 
/* 370 */     sp = new SymbolPattern("Continuous Heavy Snow");
/* 371 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 4.0D), new Coordinate(1.0D, 2.0D) });
/* 372 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 2.0D), new Coordinate(1.0D, 4.0D) });
/* 373 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, 3.0D), new Coordinate(2.0D, 3.0D) });
/* 374 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-2.0D, -1.0D) });
/* 375 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, -1.0D), new Coordinate(-2.0D, 1.0D) });
/* 376 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, 0.0D), new Coordinate(-1.0D, 0.0D) });
/* 377 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, 1.0D), new Coordinate(4.0D, -1.0D) });
/* 378 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, -1.0D), new Coordinate(4.0D, 1.0D) });
/* 379 */     sp.addPath(new Coordinate[] { new Coordinate(1.0D, 0.0D), new Coordinate(5.0D, 0.0D) });
/* 380 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -2.0D), new Coordinate(1.0D, -4.0D) });
/* 381 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -2.0D) });
/* 382 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, -3.0D), new Coordinate(2.0D, -3.0D) });
/* 383 */     this.patternMap.put("PRESENT_WX_075", sp);
/*     */ 
/* 385 */     sp = new SymbolPattern("Ice pellets");
/* 386 */     sp.addDot(new Coordinate(0.0D, 0.0D), 0.75D);
/* 387 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -2.0D), new Coordinate(0.0D, 3.0D), 
/* 388 */       new Coordinate(3.0D, -2.0D), new Coordinate(-3.0D, -2.0D) });
/* 389 */     this.patternMap.put("PRESENT_WX_079", sp);
/*     */ 
/* 391 */     sp = new SymbolPattern("Slight rain shower");
/* 392 */     sp.addDot(new Coordinate(0.0D, 3.0D), 0.75D);
/* 393 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 1.0D), new Coordinate(0.0D, -5.0D), 
/* 394 */       new Coordinate(3.0D, 1.0D), new Coordinate(-3.0D, 1.0D) });
/* 395 */     this.patternMap.put("PRESENT_WX_080", sp);
/*     */ 
/* 397 */     sp = new SymbolPattern("Slight Snow Showers");
/* 398 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 1.0D), new Coordinate(0.0D, -5.0D), 
/* 399 */       new Coordinate(3.0D, 1.0D), new Coordinate(-3.0D, 1.0D) });
/* 400 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 3.0D), new Coordinate(1.0D, 5.0D) });
/* 401 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 5.0D), new Coordinate(1.0D, 3.0D) });
/* 402 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, 4.0D), new Coordinate(2.0D, 4.0D) });
/* 403 */     this.patternMap.put("PRESENT_WX_085", sp);
/*     */ 
/* 405 */     sp = new SymbolPattern("Moderate Hail Showers");
/* 406 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 1.0D), new Coordinate(0.0D, -5.0D), 
/* 407 */       new Coordinate(3.0D, 1.0D), new Coordinate(-3.0D, 1.0D) }, false);
/* 408 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 2.0D), new Coordinate(0.0D, 4.0D), 
/* 409 */       new Coordinate(3.0D, 2.0D), new Coordinate(-3.0D, 2.0D) }, false);
/* 410 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, -1.0D), new Coordinate(2.0D, -1.0D) }, false);
/* 411 */     this.patternMap.put("PRESENT_WX_088", sp);
/*     */ 
/* 413 */     sp = new SymbolPattern("Slight Shower of Hail");
/* 414 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 1.0D), new Coordinate(0.0D, -5.0D), 
/* 415 */       new Coordinate(3.0D, 1.0D), new Coordinate(-3.0D, 1.0D) }, false);
/* 416 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 2.0D), new Coordinate(0.0D, 4.0D), 
/* 417 */       new Coordinate(3.0D, 2.0D), new Coordinate(-3.0D, 2.0D) }, true);
/* 418 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 4.0D), new Coordinate(0.0D, 2.0D) }, false);
/* 419 */     this.patternMap.put("PRESENT_WX_089", sp);
/*     */ 
/* 421 */     sp = new SymbolPattern("Slight or mod thinderstorm with rain");
/* 422 */     sp.addDot(new Coordinate(0.0D, 3.0D), 1.15D);
/* 423 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, 1.0D), new Coordinate(2.0D, 1.0D), 
/* 424 */       new Coordinate(0.0D, -1.0D), new Coordinate(3.0D, -4.0D), 
/* 425 */       new Coordinate(3.0D, -3.0D), new Coordinate(3.0D, -4.0D), 
/* 426 */       new Coordinate(2.0D, -4.0D) });
/* 427 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -4.0D), new Coordinate(-1.0D, 1.0D) }, false);
/* 428 */     this.patternMap.put("PRESENT_WX_095", sp);
/*     */ 
/* 430 */     sp = new SymbolPattern("Slight or Mod Thunderstorm with Snow");
/* 431 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, 1.0D), new Coordinate(2.0D, 1.0D), 
/* 432 */       new Coordinate(0.0D, -1.0D), new Coordinate(3.0D, -4.0D), 
/* 433 */       new Coordinate(3.0D, -3.0D), new Coordinate(3.0D, -4.0D), 
/* 434 */       new Coordinate(2.0D, -4.0D) });
/* 435 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -4.0D), new Coordinate(-1.0D, 1.0D) });
/* 436 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 2.0D), new Coordinate(1.0D, 4.0D) });
/* 437 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 4.0D), new Coordinate(1.0D, 2.0D) });
/* 438 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, 3.0D), new Coordinate(2.0D, 3.0D) });
/* 439 */     this.patternMap.put("PRESENT_WX_105", sp);
/*     */ 
/* 441 */     sp = new SymbolPattern("Volcanic activity");
/* 442 */     sp.addDot(new Coordinate(0.0D, -4.0D), 0.75D);
/* 443 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -4.0D), new Coordinate(-4.0D, -4.0D), 
/* 444 */       new Coordinate(-2.0D, 1.0D), new Coordinate(2.0D, 1.0D), 
/* 445 */       new Coordinate(4.0D, -4.0D), new Coordinate(1.0D, -4.0D) });
/* 446 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 1.0D), new Coordinate(-2.0D, 4.0D) }, false);
/* 447 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 1.0D), new Coordinate(0.0D, 4.0D) }, false);
/* 448 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 1.0D), new Coordinate(2.0D, 4.0D) }, false);
/* 449 */     this.patternMap.put("PRESENT_WX_201", sp);
/*     */ 
/* 454 */     sp = new SymbolPattern("High Pressure H");
/* 455 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 4.0D), new Coordinate(-2.0D, 4.0D), 
/* 456 */       new Coordinate(-2.0D, 1.0D), new Coordinate(2.0D, 1.0D), 
/* 457 */       new Coordinate(2.0D, 4.0D), new Coordinate(4.0D, 4.0D), 
/* 458 */       new Coordinate(4.0D, -5.0D), new Coordinate(2.0D, -5.0D), 
/* 459 */       new Coordinate(2.0D, -1.0D), new Coordinate(-2.0D, -1.0D), 
/* 460 */       new Coordinate(-2.0D, -5.0D), new Coordinate(-4.0D, -5.0D), 
/* 461 */       new Coordinate(-4.0D, 4.0D) }, true);
/* 462 */     this.patternMap.put("HIGH_PRESSURE_H", sp);
/*     */ 
/* 464 */     sp = new SymbolPattern("Low Pressure L");
/* 465 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 4.0D), new Coordinate(-2.0D, 4.0D), 
/* 466 */       new Coordinate(-2.0D, -3.0D), new Coordinate(4.0D, -3.0D), 
/* 467 */       new Coordinate(4.0D, -5.0D), new Coordinate(-4.0D, -5.0D), 
/* 468 */       new Coordinate(-4.0D, 4.0D) }, true);
/* 469 */     this.patternMap.put("LOW_PRESSURE_L", sp);
/*     */ 
/* 471 */     sp = new SymbolPattern("Tropical Storm (Northern Hemisphere)");
/* 472 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, 5.0D), new Coordinate(1.0D, 5.0D), 
/* 473 */       new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 2.0D), 
/* 474 */       new Coordinate(-2.0D, 0.0D), new Coordinate(-1.0D, 2.0D), 
/* 475 */       new Coordinate(0.0D, 4.0D), new Coordinate(2.0D, 5.0D) }, true);
/* 476 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, -5.0D), new Coordinate(-1.0D, -5.0D), 
/* 477 */       new Coordinate(1.0D, -4.0D), new Coordinate(2.0D, -2.0D), 
/* 478 */       new Coordinate(2.0D, 0.0D), new Coordinate(1.0D, -2.0D), 
/* 479 */       new Coordinate(0.0D, -4.0D), new Coordinate(-2.0D, -5.0D) }, true);
/* 480 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 2.0D), new Coordinate(-2.0D, 1.0D), 
/* 481 */       new Coordinate(-2.0D, 0.0D), new Coordinate(-1.0D, -2.0D), 
/* 482 */       new Coordinate(1.0D, -2.0D), new Coordinate(2.0D, -1.0D), 
/* 483 */       new Coordinate(2.0D, 0.0D), new Coordinate(1.0D, 2.0D), 
/* 484 */       new Coordinate(-1.0D, 2.0D) }, false);
/* 485 */     this.patternMap.put("TROPICAL_STORM_NH", sp);
/*     */ 
/* 487 */     sp = new SymbolPattern("Hurricane (Northern Hemisphere)");
/* 488 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, 5.0D), new Coordinate(1.0D, 5.0D), 
/* 489 */       new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 2.0D), 
/* 490 */       new Coordinate(-2.0D, 0.0D), new Coordinate(-1.0D, -2.0D), 
/* 491 */       new Coordinate(1.0D, -2.0D), new Coordinate(0.0D, -4.0D), 
/* 492 */       new Coordinate(-2.0D, -5.0D), new Coordinate(-1.0D, -5.0D), 
/* 493 */       new Coordinate(1.0D, -4.0D), new Coordinate(2.0D, -2.0D), 
/* 494 */       new Coordinate(2.0D, 0.0D), new Coordinate(1.0D, 2.0D), 
/* 495 */       new Coordinate(-1.0D, 2.0D), new Coordinate(0.0D, 4.0D), 
/* 496 */       new Coordinate(2.0D, 5.0D) }, true);
/* 497 */     this.patternMap.put("HURRICANE_NH", sp);
/*     */ 
/* 499 */     sp = new SymbolPattern("Tropical Storm (Southern Hemisphere)");
/* 500 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, -5.0D), new Coordinate(0.0D, -5.0D), 
/* 501 */       new Coordinate(-2.0D, -4.0D), new Coordinate(-3.0D, -2.0D), 
/* 502 */       new Coordinate(-3.0D, 0.0D), new Coordinate(-2.0D, -2.0D), 
/* 503 */       new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -5.0D) }, true);
/* 504 */     sp.addPath(new Coordinate[] { new Coordinate(1.0D, 0.0D), new Coordinate(0.0D, 2.0D), 
/* 505 */       new Coordinate(-1.0D, 4.0D), new Coordinate(-3.0D, 5.0D), 
/* 506 */       new Coordinate(-2.0D, 5.0D), new Coordinate(0.0D, 4.0D), 
/* 507 */       new Coordinate(1.0D, 2.0D), new Coordinate(1.0D, 0.0D) }, true);
/* 508 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, -2.0D), new Coordinate(-2.0D, -2.0D), 
/* 509 */       new Coordinate(-3.0D, -1.0D), new Coordinate(-3.0D, 0.0D), 
/* 510 */       new Coordinate(-2.0D, 2.0D), new Coordinate(0.0D, 2.0D), 
/* 511 */       new Coordinate(1.0D, 1.0D), new Coordinate(0.0D, -2.0D), 
/* 512 */       new Coordinate(-2.0D, -2.0D) }, false);
/* 513 */     this.patternMap.put("TROPICAL_STORM_SH", sp);
/*     */ 
/* 515 */     sp = new SymbolPattern("Hurricane (Southern Hemisphere)");
/* 516 */     sp.addPath(new Coordinate[] { new Coordinate(1.0D, -5.0D), new Coordinate(0.0D, -5.0D), 
/* 517 */       new Coordinate(-2.0D, -4.0D), new Coordinate(-3.0D, -2.0D), 
/* 518 */       new Coordinate(-3.0D, 0.0D), new Coordinate(-2.0D, 2.0D), 
/* 519 */       new Coordinate(0.0D, 2.0D), new Coordinate(-1.0D, 4.0D), 
/* 520 */       new Coordinate(-3.0D, 5.0D), new Coordinate(-2.0D, 5.0D), 
/* 521 */       new Coordinate(0.0D, 4.0D), new Coordinate(1.0D, 2.0D), 
/* 522 */       new Coordinate(1.0D, 0.0D), new Coordinate(0.0D, -2.0D), 
/* 523 */       new Coordinate(-2.0D, -2.0D), new Coordinate(-1.0D, -4.0D), 
/* 524 */       new Coordinate(1.0D, -5.0D) }, true);
/* 525 */     this.patternMap.put("HURRICANE_SH", sp);
/*     */ 
/* 527 */     sp = new SymbolPattern("Storm Center");
/* 528 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, -5.0D), new Coordinate(-5.0D, 5.0D), 
/* 529 */       new Coordinate(5.0D, 5.0D), new Coordinate(5.0D, -5.0D), 
/* 530 */       new Coordinate(-5.0D, -5.0D), new Coordinate(5.0D, 5.0D) }, false);
/* 531 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -5.0D), new Coordinate(-5.0D, 5.0D) });
/* 532 */     this.patternMap.put("STORM_CENTER", sp);
/*     */ 
/* 534 */     sp = new SymbolPattern("Tropical Depression");
/* 535 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, 1.0D), new Coordinate(-5.0D, -1.0D), 
/* 536 */       new Coordinate(-4.0D, -3.0D), new Coordinate(-3.0D, -4.0D), 
/* 537 */       new Coordinate(-1.0D, -5.0D), new Coordinate(1.0D, -5.0D), 
/* 538 */       new Coordinate(3.0D, -4.0D), new Coordinate(4.0D, -3.0D), 
/* 539 */       new Coordinate(5.0D, -1.0D), new Coordinate(5.0D, 1.0D), 
/* 540 */       new Coordinate(4.0D, 3.0D), new Coordinate(3.0D, 4.0D), 
/* 541 */       new Coordinate(1.0D, 5.0D), new Coordinate(-1.0D, 5.0D), 
/* 542 */       new Coordinate(-3.0D, 4.0D), new Coordinate(-4.0D, 3.0D), 
/* 543 */       new Coordinate(-5.0D, 1.0D) }, false);
/* 544 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -3.0D), new Coordinate(3.0D, 3.0D) });
/* 545 */     sp.addPath(new Coordinate[] { new Coordinate(3.0D, -3.0D), new Coordinate(-3.0D, 3.0D) });
/* 546 */     this.patternMap.put("TROPICAL_DEPRESSION", sp);
/*     */ 
/* 548 */     sp = new SymbolPattern("Tropical Cyclone");
/* 549 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, 5.0D), new Coordinate(5.0D, 5.0D) });
/* 550 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 5.0D), new Coordinate(0.0D, -5.0D) });
/* 551 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -4.0D), new Coordinate(4.0D, -5.0D), 
/* 552 */       new Coordinate(3.0D, -5.0D), new Coordinate(2.0D, -4.0D), 
/* 553 */       new Coordinate(2.0D, -2.0D), new Coordinate(3.0D, -1.0D), 
/* 554 */       new Coordinate(4.0D, -1.0D), new Coordinate(5.0D, -2.0D) }, false);
/* 555 */     this.patternMap.put("TROPICAL_CYCLONE", sp);
/*     */ 
/* 557 */     sp = new SymbolPattern("Flame");
/* 558 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, -5.0D), new Coordinate(0.0D, -4.0D), 
/* 559 */       new Coordinate(1.0D, -5.0D), new Coordinate(2.0D, -3.0D), 
/* 560 */       new Coordinate(4.0D, -2.0D), new Coordinate(5.0D, -1.0D), 
/* 561 */       new Coordinate(5.0D, 0.0D), new Coordinate(4.0D, 2.0D), 
/* 562 */       new Coordinate(4.0D, 1.0D), new Coordinate(2.0D, -1.0D), 
/* 563 */       new Coordinate(2.0D, 1.0D), new Coordinate(1.0D, 5.0D), 
/* 564 */       new Coordinate(0.0D, 3.0D), new Coordinate(-1.0D, 4.0D), 
/* 565 */       new Coordinate(-1.0D, 3.0D), new Coordinate(-2.0D, 1.0D), 
/* 566 */       new Coordinate(-2.0D, 0.0D), new Coordinate(-1.0D, -2.0D), 
/* 567 */       new Coordinate(-1.0D, -1.0D), new Coordinate(1.0D, 0.0D), 
/* 568 */       new Coordinate(0.0D, -1.0D), new Coordinate(0.0D, -3.0D), 
/* 569 */       new Coordinate(-3.0D, -2.0D), new Coordinate(-4.0D, -1.0D), 
/* 570 */       new Coordinate(-5.0D, 2.0D), new Coordinate(-5.0D, 0.0D), 
/* 571 */       new Coordinate(-4.0D, -2.0D), new Coordinate(-5.0D, -1.0D), 
/* 572 */       new Coordinate(-5.0D, -2.0D), new Coordinate(-4.0D, -3.0D), 
/* 573 */       new Coordinate(-5.0D, -3.0D), new Coordinate(0.0D, -5.0D) }, true);
/* 574 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 2.0D), new Coordinate(-3.0D, 0.0D), 
/* 575 */       new Coordinate(-3.0D, 2.0D), new Coordinate(-2.0D, 3.0D), 
/* 576 */       new Coordinate(-2.0D, 4.0D), new Coordinate(-3.0D, 3.0D), 
/* 577 */       new Coordinate(-3.0D, 5.0D), new Coordinate(-4.0D, 2.0D), 
/* 578 */       new Coordinate(-4.0D, 0.0D), new Coordinate(-3.0D, -1.0D), 
/* 579 */       new Coordinate(-4.0D, 2.0D) }, true);
/* 580 */     this.patternMap.put("FLAME", sp);
/*     */ 
/* 582 */     sp = new SymbolPattern("X Cross");
/* 583 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, -5.0D), new Coordinate(5.0D, 5.0D) });
/* 584 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -5.0D), new Coordinate(-5.0D, 5.0D) });
/* 585 */     this.patternMap.put("X_CROSS", sp);
/*     */ 
/* 587 */     sp = new SymbolPattern("LowX (outline)");
/* 588 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, -2.0D), new Coordinate(2.0D, 2.0D) });
/* 589 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, 2.0D), new Coordinate(2.0D, -2.0D) });
/* 590 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, 5.0D), new Coordinate(-3.0D, 5.0D), 
/* 591 */       new Coordinate(-3.0D, -3.0D), new Coordinate(2.0D, -3.0D), 
/* 592 */       new Coordinate(2.0D, -5.0D), new Coordinate(-5.0D, -5.0D), 
/* 593 */       new Coordinate(-5.0D, 5.0D) }, false);
/* 594 */     this.patternMap.put("LOW_X_OUTLINE", sp);
/*     */ 
/* 596 */     sp = new SymbolPattern("LowX (filled)");
/* 597 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, -2.0D), new Coordinate(2.0D, 2.0D) });
/* 598 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, 2.0D), new Coordinate(2.0D, -2.0D) });
/* 599 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, 5.0D), new Coordinate(-3.0D, 5.0D), 
/* 600 */       new Coordinate(-3.0D, -3.0D), new Coordinate(2.0D, -3.0D), 
/* 601 */       new Coordinate(2.0D, -5.0D), new Coordinate(-5.0D, -5.0D), 
/* 602 */       new Coordinate(-5.0D, 5.0D) }, true);
/* 603 */     this.patternMap.put("LOW_X_FILLED", sp);
/*     */ 
/* 605 */     sp = new SymbolPattern("Tropical Storm NH");
/* 606 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, -2.0D), new Coordinate(-3.0D, -3.0D), 
/* 607 */       new Coordinate(-1.0D, -2.0D), new Coordinate(0.0D, 0.0D), 
/* 608 */       new Coordinate(3.0D, 0.0D), new Coordinate(4.0D, -1.0D), 
/* 609 */       new Coordinate(5.0D, -3.0D) }, false);
/* 610 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 0.0D), new Coordinate(-1.0D, 1.0D), 
/* 611 */       new Coordinate(-1.0D, 4.0D), new Coordinate(0.0D, 5.0D) }, false);
/* 612 */     this.patternMap.put("TROPICAL_STORM_NH_WPAC", sp);
/*     */ 
/* 614 */     sp = new SymbolPattern("Tropical Storm SH");
/* 615 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -2.0D), new Coordinate(3.0D, -3.0D), 
/* 616 */       new Coordinate(1.0D, -2.0D), new Coordinate(0.0D, 0.0D), 
/* 617 */       new Coordinate(-3.0D, 0.0D), new Coordinate(-4.0D, -1.0D), 
/* 618 */       new Coordinate(-5.0D, -3.0D) }, false);
/* 619 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 0.0D), new Coordinate(1.0D, 1.0D), 
/* 620 */       new Coordinate(1.0D, 4.0D), new Coordinate(0.0D, 5.0D) }, false);
/* 621 */     this.patternMap.put("TROPICAL_STORM_SH_WPAC", sp);
/*     */ 
/* 623 */     sp = new SymbolPattern("Nuclear Fallout");
/* 624 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, 1.0D), new Coordinate(-3.0D, 5.0D), 
/* 625 */       new Coordinate(-1.0D, 1.0D), new Coordinate(-5.0D, 1.0D) }, true);
/* 626 */     sp.addPath(new Coordinate[] { new Coordinate(1.0D, 1.0D), new Coordinate(3.0D, 5.0D), 
/* 627 */       new Coordinate(5.0D, 1.0D), new Coordinate(1.0D, 1.0D) }, true);
/* 628 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, -5.0D), new Coordinate(0.0D, -1.0D), 
/* 629 */       new Coordinate(2.0D, -5.0D), new Coordinate(-2.0D, -5.0D) }, true);
/* 630 */     sp.addDot(new Coordinate(0.0D, 0.0D), 0.7D);
/* 631 */     this.patternMap.put("NUCLEAR_FALLOUT", sp);
/*     */ 
/* 633 */     sp = new SymbolPattern("Letter A filled");
/* 634 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -5.0D), new Coordinate(3.0D, -5.0D), 
/* 635 */       new Coordinate(2.0D, -2.0D), new Coordinate(-2.0D, -2.0D), 
/* 636 */       new Coordinate(-1.0D, 0.0D), new Coordinate(1.0D, 0.0D), 
/* 637 */       new Coordinate(0.0D, 3.0D), new Coordinate(-1.0D, 0.0D), 
/* 638 */       new Coordinate(-2.0D, -2.0D), new Coordinate(-3.0D, -5.0D), 
/* 639 */       new Coordinate(-5.0D, -5.0D), new Coordinate(-1.0D, 5.0D), 
/* 640 */       new Coordinate(1.0D, 5.0D), new Coordinate(5.0D, -5.0D) }, true);
/* 641 */     this.patternMap.put("LETTER_A_FILLED", sp);
/*     */ 
/* 643 */     sp = new SymbolPattern("Letter C");
/* 644 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -2.0D), new Coordinate(4.0D, -4.0D), 
/* 645 */       new Coordinate(2.0D, -5.0D), new Coordinate(-2.0D, -5.0D), 
/* 646 */       new Coordinate(-4.0D, -4.0D), new Coordinate(-5.0D, -2.0D), 
/* 647 */       new Coordinate(-5.0D, 2.0D), new Coordinate(-4.0D, 4.0D), 
/* 648 */       new Coordinate(-2.0D, 5.0D), new Coordinate(2.0D, 5.0D), 
/* 649 */       new Coordinate(4.0D, 4.0D), new Coordinate(5.0D, 2.0D), 
/* 650 */       new Coordinate(3.0D, 2.0D), new Coordinate(2.0D, 3.0D), 
/* 651 */       new Coordinate(-2.0D, 3.0D), new Coordinate(-3.0D, 2.0D), 
/* 652 */       new Coordinate(-3.0D, -2.0D), new Coordinate(-2.0D, -3.0D), 
/* 653 */       new Coordinate(2.0D, -3.0D), new Coordinate(3.0D, -2.0D), 
/* 654 */       new Coordinate(5.0D, -2.0D) }, false);
/* 655 */     this.patternMap.put("LETTER_C", sp);
/*     */ 
/* 657 */     sp = new SymbolPattern("Letter C filled");
/* 658 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -2.0D), new Coordinate(4.0D, -4.0D), 
/* 659 */       new Coordinate(2.0D, -5.0D), new Coordinate(-2.0D, -5.0D), 
/* 660 */       new Coordinate(-4.0D, -4.0D), new Coordinate(-5.0D, -2.0D), 
/* 661 */       new Coordinate(-5.0D, 2.0D), new Coordinate(-4.0D, 4.0D), 
/* 662 */       new Coordinate(-2.0D, 5.0D), new Coordinate(2.0D, 5.0D), 
/* 663 */       new Coordinate(4.0D, 4.0D), new Coordinate(5.0D, 2.0D), 
/* 664 */       new Coordinate(3.0D, 2.0D), new Coordinate(2.0D, 3.0D), 
/* 665 */       new Coordinate(-2.0D, 3.0D), new Coordinate(-3.0D, 2.0D), 
/* 666 */       new Coordinate(-3.0D, -2.0D), new Coordinate(-2.0D, -3.0D), 
/* 667 */       new Coordinate(2.0D, -3.0D), new Coordinate(3.0D, -2.0D), 
/* 668 */       new Coordinate(5.0D, -2.0D) }, true);
/* 669 */     this.patternMap.put("LETTER_C_FILLED", sp);
/*     */ 
/* 671 */     sp = new SymbolPattern("Letter X");
/* 672 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -5.0D), new Coordinate(3.0D, -5.0D), 
/* 673 */       new Coordinate(0.0D, -1.0D), new Coordinate(-3.0D, -5.0D), 
/* 674 */       new Coordinate(-5.0D, -5.0D), new Coordinate(-1.0D, 0.0D), 
/* 675 */       new Coordinate(-5.0D, 5.0D), new Coordinate(-3.0D, 5.0D), 
/* 676 */       new Coordinate(0.0D, 1.0D), new Coordinate(3.0D, 5.0D), 
/* 677 */       new Coordinate(5.0D, 5.0D), new Coordinate(1.0D, 0.0D), 
/* 678 */       new Coordinate(5.0D, -5.0D) }, false);
/* 679 */     this.patternMap.put("LETTER_X", sp);
/*     */ 
/* 681 */     sp = new SymbolPattern("Letter X filled");
/* 682 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -5.0D), new Coordinate(3.0D, -5.0D), 
/* 683 */       new Coordinate(0.0D, -1.0D), new Coordinate(-3.0D, -5.0D), 
/* 684 */       new Coordinate(-5.0D, -5.0D), new Coordinate(-1.0D, 0.0D), 
/* 685 */       new Coordinate(-5.0D, 5.0D), new Coordinate(-3.0D, 5.0D), 
/* 686 */       new Coordinate(0.0D, 1.0D), new Coordinate(3.0D, 5.0D), 
/* 687 */       new Coordinate(5.0D, 5.0D), new Coordinate(1.0D, 0.0D), 
/* 688 */       new Coordinate(5.0D, -5.0D) }, true);
/* 689 */     this.patternMap.put("LETTER_X_FILLED", sp);
/*     */ 
/* 691 */     sp = new SymbolPattern("Letter N");
/* 692 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -5.0D), new Coordinate(3.0D, -5.0D), 
/* 693 */       new Coordinate(-3.0D, 2.0D), new Coordinate(-3.0D, -5.0D), 
/* 694 */       new Coordinate(-5.0D, -5.0D), new Coordinate(-5.0D, 5.0D), 
/* 695 */       new Coordinate(-3.0D, 5.0D), new Coordinate(3.0D, -2.0D), 
/* 696 */       new Coordinate(3.0D, 5.0D), new Coordinate(5.0D, 5.0D), 
/* 697 */       new Coordinate(5.0D, -5.0D) }, false);
/* 698 */     this.patternMap.put("LETTER_N", sp);
/*     */ 
/* 700 */     sp = new SymbolPattern("Letter N filled");
/* 701 */     sp.addPath(new Coordinate[] { new Coordinate(5.0D, -5.0D), new Coordinate(3.0D, -5.0D), 
/* 702 */       new Coordinate(-3.0D, 2.0D), new Coordinate(-3.0D, -5.0D), 
/* 703 */       new Coordinate(-5.0D, -5.0D), new Coordinate(-5.0D, 5.0D), 
/* 704 */       new Coordinate(-3.0D, 5.0D), new Coordinate(3.0D, -2.0D), 
/* 705 */       new Coordinate(3.0D, 5.0D), new Coordinate(5.0D, 5.0D), 
/* 706 */       new Coordinate(5.0D, -5.0D) }, true);
/* 707 */     this.patternMap.put("LETTER_N_FILLED", sp);
/*     */ 
/* 709 */     sp = new SymbolPattern("Thirty knot wind barb");
/* 710 */     sp.addPath(new Coordinate[] { new Coordinate(3.0D, 4.0D), new Coordinate(5.0D, 4.0D), 
/* 711 */       new Coordinate(5.0D, 0.0D), new Coordinate(3.0D, 0.0D), 
/* 712 */       new Coordinate(3.0D, 4.0D) }, false);
/* 713 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 4.0D), new Coordinate(1.0D, 4.0D), 
/* 714 */       new Coordinate(1.0D, 0.0D), new Coordinate(-1.0D, 0.0D) }, false);
/* 715 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, -2.0D), new Coordinate(-4.0D, -5.0D), 
/* 716 */       new Coordinate(4.0D, -5.0D) }, false);
/* 717 */     sp.addPath(new Coordinate[] { new Coordinate(1.0D, 2.0D), new Coordinate(0.0D, 2.0D) });
/* 718 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, -2.0D), new Coordinate(-2.0D, -5.0D) });
/* 719 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -2.0D), new Coordinate(0.0D, -5.0D) });
/* 720 */     this.patternMap.put("30_KT_BARB", sp);
/*     */ 
/* 725 */     sp = new SymbolPattern("Light superstructure icing");
/* 726 */     sp.addPath(new Coordinate[] { new Coordinate(2.0D, 2.0D), new Coordinate(2.0D, 0.0D), 
/* 727 */       new Coordinate(1.0D, -1.0D), new Coordinate(-1.0D, -1.0D), 
/* 728 */       new Coordinate(-2.0D, 0.0D), new Coordinate(-2.0D, 2.0D), 
/* 729 */       new Coordinate(2.0D, 2.0D) }, false);
/* 730 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 4.0D), new Coordinate(0.0D, -4.0D) });
/* 731 */     this.patternMap.put("ICING_09", sp);
/*     */ 
/* 733 */     sp = new SymbolPattern("Heavy superstructure icing");
/* 734 */     sp.addPath(new Coordinate[] { new Coordinate(3.0D, 2.0D), new Coordinate(3.0D, 0.0D), 
/* 735 */       new Coordinate(2.0D, -1.0D), new Coordinate(-2.0D, -1.0D), 
/* 736 */       new Coordinate(-3.0D, 0.0D), new Coordinate(-3.0D, 2.0D), 
/* 737 */       new Coordinate(3.0D, 2.0D) }, false);
/* 738 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, 4.0D), new Coordinate(-1.0D, -4.0D) });
/* 739 */     sp.addPath(new Coordinate[] { new Coordinate(1.0D, 4.0D), new Coordinate(1.0D, -4.0D) });
/* 740 */     this.patternMap.put("ICING_10", sp);
/*     */ 
/* 745 */     sp = new SymbolPattern("Thunderstorm");
/* 746 */     sp.addPath(new Coordinate[] { new Coordinate(-2.0D, 1.0D), new Coordinate(2.0D, 1.0D), 
/* 747 */       new Coordinate(0.0D, -1.0D), new Coordinate(3.0D, -4.0D), 
/* 748 */       new Coordinate(3.0D, -3.0D), new Coordinate(3.0D, -4.0D), 
/* 749 */       new Coordinate(2.0D, -4.0D) }, false);
/* 750 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -4.0D), new Coordinate(-1.0D, 1.0D) });
/* 751 */     this.patternMap.put("PAST_WX_09", sp);
/*     */ 
/* 756 */     sp = new SymbolPattern("No cloud");
/*     */ 
/* 763 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 764 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 765 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 766 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 767 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 768 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, false);
/* 769 */     this.patternMap.put("SKY_COVER_00", sp);
/*     */ 
/* 771 */     sp = new SymbolPattern("One-tenth or less");
/* 772 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 773 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 774 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 775 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 776 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 777 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, false);
/* 778 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 2.0D), new Coordinate(0.0D, -2.0D) });
/* 779 */     this.patternMap.put("SKY_COVER_01", sp);
/*     */ 
/* 781 */     sp = new SymbolPattern("Two-tenths to three-tenths");
/* 782 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 783 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 784 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 785 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 786 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 787 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, false);
/* 788 */     sp.addPath(new Coordinate[] { new Coordinate(4.0D, 0.0D), new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), 
/* 789 */       new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), new Coordinate(1.0D, 4.0D), new Coordinate(0.0D, 4.0D), 
/* 790 */       new Coordinate(0.0D, 0.0D), new Coordinate(4.0D, 0.0D) }, true);
/* 791 */     this.patternMap.put("SKY_COVER_02", sp);
/*     */ 
/* 793 */     sp = new SymbolPattern("Four-tenths");
/* 794 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 795 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 796 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 797 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 798 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 799 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, false);
/* 800 */     sp.addPath(new Coordinate[] { new Coordinate(4.0D, 0.0D), new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), 
/* 801 */       new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), new Coordinate(1.0D, 4.0D), new Coordinate(0.0D, 4.0D), 
/* 802 */       new Coordinate(0.0D, 0.0D), new Coordinate(4.0D, 0.0D) }, true);
/* 803 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 0.0D), new Coordinate(0.0D, -4.0D) });
/* 804 */     this.patternMap.put("SKY_COVER_03", sp);
/*     */ 
/* 806 */     sp = new SymbolPattern("Five-tenths");
/* 807 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 808 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 809 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 810 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 811 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 812 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, false);
/* 813 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 814 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 815 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 816 */       new Coordinate(1.0D, 4.0D), new Coordinate(0.0D, 4.0D), new Coordinate(0.0D, -4.0D) }, true);
/* 817 */     this.patternMap.put("SKY_COVER_04", sp);
/*     */ 
/* 819 */     sp = new SymbolPattern("Six-tenths");
/* 820 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 821 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 822 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 823 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 824 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 825 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, false);
/* 826 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 827 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 828 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 829 */       new Coordinate(1.0D, 4.0D), new Coordinate(0.0D, 4.0D), new Coordinate(0.0D, -4.0D) }, true);
/* 830 */     sp.addPath(new Coordinate[] { new Coordinate(0.0D, 0.0D), new Coordinate(-4.0D, 0.0D) });
/* 831 */     this.patternMap.put("SKY_COVER_05", sp);
/*     */ 
/* 833 */     sp = new SymbolPattern("Seven-tenths to eight-tenths");
/* 834 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 835 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 836 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 837 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 838 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 839 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, false);
/* 840 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 0.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 841 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 842 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 843 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 844 */       new Coordinate(1.0D, 4.0D), new Coordinate(0.0D, 4.0D), new Coordinate(0.0D, 0.0D), new Coordinate(-4.0D, 0.0D) }, true);
/* 845 */     this.patternMap.put("SKY_COVER_06", sp);
/*     */ 
/* 847 */     sp = new SymbolPattern("Nine-tenths or overcast with opening");
/* 848 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 849 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 850 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 851 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 852 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 853 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, false);
/* 854 */     sp.addPath(new Coordinate[] { new Coordinate(0.5D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 855 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 856 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 857 */       new Coordinate(1.0D, 4.0D), new Coordinate(0.5D, 4.0D), new Coordinate(0.5D, -4.0D) }, true);
/* 858 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 859 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(-0.5D, -4.0D), 
/* 860 */       new Coordinate(-0.5D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 861 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, true);
/* 862 */     this.patternMap.put("SKY_COVER_07", sp);
/*     */ 
/* 864 */     sp = new SymbolPattern("Completely overcast");
/* 865 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 866 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 867 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 868 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 869 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 870 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, true);
/* 871 */     this.patternMap.put("SKY_COVER_08", sp);
/*     */ 
/* 873 */     sp = new SymbolPattern("Sky obscured");
/* 874 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 875 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 876 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 877 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 878 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 879 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, false);
/* 880 */     sp.addPath(new Coordinate[] { new Coordinate(-2.9D, -2.9D), new Coordinate(2.9D, 2.9D) });
/* 881 */     sp.addPath(new Coordinate[] { new Coordinate(2.9D, -2.9D), new Coordinate(-2.9D, 2.9D) });
/* 882 */     this.patternMap.put("SKY_COVER_09", sp);
/*     */ 
/* 884 */     sp = new SymbolPattern("Missing cloud");
/* 885 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 1.0D), new Coordinate(-4.0D, -1.0D), new Coordinate(-3.6D, -2.0D), 
/* 886 */       new Coordinate(-2.9D, -2.9D), new Coordinate(-2.0D, -3.6D), new Coordinate(-1.0D, -4.0D), new Coordinate(1.0D, -4.0D), 
/* 887 */       new Coordinate(2.0D, -3.6D), new Coordinate(2.9D, -2.9D), new Coordinate(3.6D, -2.0D), new Coordinate(4.0D, -1.0D), 
/* 888 */       new Coordinate(4.0D, 1.0D), new Coordinate(3.6D, 2.0D), new Coordinate(2.9D, 2.9D), new Coordinate(2.0D, 3.6D), 
/* 889 */       new Coordinate(1.0D, 4.0D), new Coordinate(-1.0D, 4.0D), new Coordinate(-2.0D, 3.6D), new Coordinate(-2.9D, 2.9D), 
/* 890 */       new Coordinate(-3.6D, 2.0D), new Coordinate(-4.0D, 1.0D) }, false);
/* 891 */     sp.addPath(new Coordinate[] { new Coordinate(-1.0D, -3.0D), new Coordinate(-1.0D, 3.0D), new Coordinate(0.0D, 0.0D), 
/* 892 */       new Coordinate(1.0D, 3.0D), new Coordinate(1.0D, -3.0D) });
/* 893 */     this.patternMap.put("SKY_COVER_10", sp);
/*     */ 
/* 898 */     sp = new SymbolPattern("Rising then falling");
/* 899 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, -3.0D), new Coordinate(1.0D, 3.0D), 
/* 900 */       new Coordinate(4.0D, 0.0D) }, false);
/* 901 */     this.patternMap.put("PRESSURE_TENDENCY_00", sp);
/*     */ 
/* 903 */     sp = new SymbolPattern("Rising then steady");
/* 904 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, -3.0D), new Coordinate(1.0D, 3.0D), 
/* 905 */       new Coordinate(4.0D, 3.0D) }, false);
/* 906 */     this.patternMap.put("PRESSURE_TENDENCY_01", sp);
/*     */ 
/* 908 */     sp = new SymbolPattern("Rising steadily");
/* 909 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, -3.0D), new Coordinate(1.0D, 3.0D) }, false);
/* 910 */     this.patternMap.put("PRESSURE_TENDENCY_02", sp);
/*     */ 
/* 912 */     sp = new SymbolPattern("Falling or steady then rising");
/* 913 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 0.0D), new Coordinate(-1.0D, -3.0D), 
/* 914 */       new Coordinate(5.0D, 3.0D) }, false);
/* 915 */     this.patternMap.put("PRESSURE_TENDENCY_03", sp);
/*     */ 
/* 917 */     sp = new SymbolPattern("Steady");
/* 918 */     sp.addPath(new Coordinate[] { new Coordinate(-3.0D, 0.0D), new Coordinate(3.0D, 0.0D), 
/* 919 */       new Coordinate(4.0D, 0.0D) }, false);
/* 920 */     this.patternMap.put("PRESSURE_TENDENCY_04", sp);
/*     */ 
/* 922 */     sp = new SymbolPattern("Falling then rising");
/* 923 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, 3.0D), new Coordinate(1.0D, -3.0D), 
/* 924 */       new Coordinate(4.0D, 0.0D) }, false);
/* 925 */     this.patternMap.put("PRESSURE_TENDENCY_05", sp);
/*     */ 
/* 927 */     sp = new SymbolPattern("Falling then steady");
/* 928 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, 3.0D), new Coordinate(1.0D, -3.0D), 
/* 929 */       new Coordinate(4.0D, -3.0D) }, false);
/* 930 */     this.patternMap.put("PRESSURE_TENDENCY_06", sp);
/*     */ 
/* 932 */     sp = new SymbolPattern("Falling steadily");
/* 933 */     sp.addPath(new Coordinate[] { new Coordinate(-5.0D, 3.0D), new Coordinate(1.0D, -3.0D) }, false);
/* 934 */     this.patternMap.put("PRESSURE_TENDENCY_07", sp);
/*     */ 
/* 936 */     sp = new SymbolPattern("Steady or rising then falling");
/* 937 */     sp.addPath(new Coordinate[] { new Coordinate(-4.0D, 0.0D), new Coordinate(-1.0D, 3.0D), 
/* 938 */       new Coordinate(5.0D, -3.0D) }, false);
/* 939 */     this.patternMap.put("PRESSURE_TENDENCY_08", sp);
/*     */   }
/*     */ 
/*     */   public void savePatternsToFile(String filename)
/*     */   {
/* 948 */     PrintWriter writer = null;
/*     */ 
/* 953 */     SymbolPatternList patternList = new SymbolPatternList(this.patternMap);
/*     */     try
/*     */     {
/* 960 */       JAXBContext context = JAXBContext.newInstance(new Class[] { SymbolPatternList.class, SymbolPatternMapEntry.class });
/* 961 */       Marshaller msh = context.createMarshaller();
/* 962 */       msh.setProperty("jaxb.formatted.output", Boolean.TRUE);
/*     */ 
/* 967 */       File fileOut = new File(filename);
/* 968 */       writer = new PrintWriter(fileOut);
/*     */ 
/* 973 */       msh.marshal(patternList, writer);
/*     */     }
/*     */     catch (Exception e) {
/* 976 */       e.printStackTrace();
/*     */     }
/*     */     finally {
/* 979 */       if (writer != null) writer.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void loadPatternsFromFile(String filename)
/*     */   {
/* 990 */     File fileIn = null;
/* 991 */     SymbolPatternList spl = null;
/*     */     try
/*     */     {
/* 995 */       JAXBContext context = JAXBContext.newInstance(new Class[] { SymbolPatternList.class });
/* 996 */       Unmarshaller msh = context.createUnmarshaller();
/*     */ 
/* 1001 */       fileIn = new File(filename);
/* 1002 */       spl = (SymbolPatternList)msh.unmarshal(fileIn);
/*     */ 
/* 1005 */       for (SymbolPatternMapEntry entry : spl.getPatternList()) {
/* 1006 */         this.patternMap.put(entry.getPatternId(), entry.getPattern());
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 1011 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.SymbolPatternManager
 * JD-Core Version:    0.6.2
 */