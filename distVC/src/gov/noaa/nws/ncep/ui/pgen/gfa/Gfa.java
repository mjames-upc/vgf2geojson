/*      */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*      */ 
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import gov.noaa.nws.ncep.ui.pgen.annotation.ElementOperations;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*      */ import gov.noaa.nws.ncep.viz.common.SnapUtil;
/*      */ import java.awt.Color;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import org.dom4j.Document;
/*      */ import org.dom4j.Node;
/*      */ 
/*      */ @ElementOperations({gov.noaa.nws.ncep.ui.pgen.annotation.Operation.COPY_MOVE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_PART, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.DELETE_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.ADD_POINT, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.MODIFY, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.GFA_FROM, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.INTERPOLATE, gov.noaa.nws.ncep.ui.pgen.annotation.Operation.EXTRAPOLATE})
/*      */ public class Gfa extends Line
/*      */   implements IGfa, Comparable<Gfa>
/*      */ {
/*   62 */   private String hazard = "";
/*   63 */   private String fcstHr = "";
/*   64 */   private String tag = "";
/*   65 */   private String desk = "";
/*   66 */   private String issueType = "";
/*   67 */   private int cycleDay = -1;
/*   68 */   private int cycleHour = -1;
/*   69 */   private String type = "";
/*   70 */   private String area = "";
/*   71 */   private String beginning = "";
/*   72 */   private String ending = "";
/*   73 */   private String states = "";
/*      */   private Coordinate gfaTextCoordinate;
/*   77 */   private HashMap<String, String> values = new HashMap();
/*      */ 
/*   80 */   private ArrayList<Coordinate> notToBeSnapped = new ArrayList();
/*      */   private static final int MIN_WIDTH_GFA_TEXT = 8;
/*      */   public static final String GR = "GR";
/*      */   public static final String FREQUENCY = "Frequency";
/*      */   public static final String CATEGORY = "Category";
/*      */   public static final String FZL_RANGE = "FZL RANGE";
/*      */   public static final String LEVEL = "Level";
/*      */   public static final String INTENSITY = "Intensity";
/*      */   public static final String SPEED = "Speed";
/*      */   public static final String DUE_TO = "DUE TO";
/*      */   public static final String LYR = "LYR";
/*      */   public static final String COVERAGE = "Coverage";
/*      */   public static final String BOTTOM = "Bottom";
/*      */   public static final String TOP = "Top";
/*      */   public static final String TOP_BOTTOM = "Top/Bottom";
/*      */   public static final String FZL_TOP_BOTTOM = "FZL Top/Bottom";
/*      */   public static final String CONTOUR = "Contour";
/*      */   public static final String ENDG_HR = "ENDG_HR";
/*      */   public static final String DVLPG_HR = "DVLPG_HR";
/*      */   public static final String SNAPSHOT_TYPE = "SNAPSHOT_TYPE";
/*      */   public static final String ISSUE_TIME = "ISSUE_TIME";
/*      */   public static final String UNTIL_TIME = "UNTIL_TIME";
/*      */   public static final String OUTLOOK_END_TIME = "OUTLOOK_END_TIME";
/*      */   public static final String AIRMET_TAG = "AIRMET_TAG";
/*      */   public static final String FROM = "FROM";
/*      */   public static final String BOUNDED_BY = "BOUNDED BY";
/*      */   public static final String VOR_TEXT = "FROM LINE";
/*      */   private static final String REDUCE_FLAGS = "REDUCE_FLAGS";
/*      */   public static final String CIG = "CIG";
/*      */   public static final String VIS = "VIS";
/*      */   public static final String MIA = "MIA";
/*      */   public static final String DFW = "DFW";
/*      */   public static final String SFO = "SFO";
/*      */   public static final String BOS = "BOS";
/*      */   public static final String CHI = "CHI";
/*      */   public static final String SLC = "SLC";
/*  129 */   private HashMap<String, Object> attributes = new HashMap();
/*      */ 
/*      */   public Gfa()
/*      */   {
/*      */   }
/*      */ 
/*      */   public Gfa(IAttribute attr, ArrayList<Coordinate> points)
/*      */   {
/*  140 */     if (attr == null) {
/*  141 */       throw new IllegalArgumentException("null IAttribute argument");
/*      */     }
/*  143 */     update(attr);
/*      */ 
/*  145 */     setLinePoints(points);
/*      */   }
/*      */ 
/*      */   public Gfa(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, boolean closed, boolean filled, List<Coordinate> linePoints, Coordinate textCoordinate, int smoothFactor, FillPatternList.FillPattern fillPattern, String pgenCategory, String pgenType, String hazard, String fcstHr, String tag, String desk, String issueType, int cycleDay, int cycleHour, String type, String area, String beginning, String ending, String states)
/*      */   {
/*  154 */     super(range, colors, lineWidth, sizeScale, closed, filled, linePoints, smoothFactor, 
/*  154 */       fillPattern, pgenCategory, pgenType);
/*  155 */     setGfaTextCoordinate(textCoordinate);
/*  156 */     setGfaHazard(hazard);
/*  157 */     setGfaFcstHr(fcstHr);
/*  158 */     setGfaTag(tag);
/*  159 */     setGfaDesk(desk);
/*  160 */     setGfaIssueType(issueType);
/*  161 */     setGfaCycleDay(cycleDay);
/*  162 */     setGfaCycleHour(cycleHour);
/*  163 */     setGfaType(type);
/*  164 */     setGfaArea(area);
/*  165 */     setGfaBeginning(beginning);
/*  166 */     setGfaEnding(ending);
/*  167 */     setGfaStates(states);
/*      */ 
/*  169 */     if ((hazard.equals("ICE")) && (
/*  170 */       (this.values.get("Type") == null) || 
/*  171 */       (((String)this.values.get("Type")).trim().length() == 0)))
/*  172 */       setGfaValue("Type", "ICE");
/*      */   }
/*      */ 
/*      */   public Gfa(ArrayList<Coordinate> linePoints, String hazard, String fcstHr, String tag, String desk, String issueType, String type, HashMap<String, String> values)
/*      */   {
/*  193 */     setPoints(linePoints);
/*  194 */     setGfaHazard(hazard);
/*  195 */     setGfaFcstHr(fcstHr);
/*  196 */     setGfaTag(tag);
/*  197 */     setGfaDesk(desk);
/*  198 */     setGfaIssueType(issueType);
/*  199 */     setGfaType(type);
/*      */ 
/*  202 */     setPgenCategory("MET");
/*  203 */     setPgenType("GFA");
/*  204 */     setFillPattern(FillPatternList.FillPattern.SOLID);
/*  205 */     setRange(null);
/*  206 */     setFilled(Boolean.valueOf(false));
/*  207 */     setColors(GfaInfo.getDefaultColors(hazard, fcstHr));
/*  208 */     setClosed(Boolean.valueOf(true));
/*  209 */     setLineWidth(GfaInfo.getLineWidth(fcstHr));
/*  210 */     setSizeScale(1.0D);
/*  211 */     setSmoothFactor(0);
/*  212 */     setGfaCycleDay(PgenCycleTool.getCycleDay());
/*  213 */     setGfaCycleHour(PgenCycleTool.getCycleHour());
/*  214 */     setGfaTextCoordinate(getCentroid());
/*  215 */     setGfaValues(values);
/*      */ 
/*  217 */     if ((hazard.equals("ICE")) && (
/*  218 */       (values.get("Type") == null) || 
/*  219 */       (((String)values.get("Type")).trim().length() == 0)))
/*  220 */       setGfaValue("Type", "ICE");
/*      */   }
/*      */ 
/*      */   public Gfa copy()
/*      */   {
/*  235 */     Gfa gfa = new Gfa();
/*  236 */     gfa.update(this);
/*      */ 
/*  238 */     gfa.setPgenCategory(new String(getPgenCategory()));
/*  239 */     gfa.setPgenType(new String(getPgenType()));
/*  240 */     gfa.setParent(getParent());
/*      */ 
/*  242 */     gfa.setClosed(isClosedLine());
/*  243 */     gfa.setFilled(isFilled());
/*      */ 
/*  245 */     Color[] colorCopy = new Color[getColors().length];
/*  246 */     for (int i = 0; i < getColors().length; i++) {
/*  247 */       colorCopy[i] = new Color(getColors()[i].getRed(), 
/*  248 */         getColors()[i].getGreen(), 
/*  249 */         getColors()[i].getBlue());
/*      */     }
/*  251 */     gfa.setColors(colorCopy);
/*      */ 
/*  253 */     gfa.setLineWidth(getLineWidth());
/*  254 */     gfa.setSizeScale(getSizeScale());
/*  255 */     gfa.setSmoothFactor(getSmoothFactor());
/*      */ 
/*  257 */     gfa.setFillPattern(getFillPattern());
/*      */ 
/*  259 */     ArrayList ptsCopy = new ArrayList();
/*  260 */     for (int i = 0; i < getPoints().size(); i++) {
/*  261 */       ptsCopy.add(new Coordinate((Coordinate)getPoints().get(i)));
/*      */     }
/*  263 */     gfa.setPoints(ptsCopy);
/*      */ 
/*  265 */     gfa.setGfaHazard(new String(nvl(getGfaHazard())));
/*  266 */     gfa.setGfaTag(new String(nvl(getGfaTag())));
/*  267 */     gfa.setGfaFcstHr(new String(nvl(getGfaFcstHr())));
/*  268 */     gfa.setGfaDesk(new String(nvl(getGfaDesk())));
/*  269 */     gfa.setGfaIssueType(new String(nvl(getGfaIssueType())));
/*  270 */     gfa.setGfaType(new String(nvl(getGfaType())));
/*  271 */     gfa.setGfaArea(new String(nvl(getGfaArea())));
/*  272 */     gfa.setGfaBeginning(new String(nvl(getGfaBeginning())));
/*  273 */     gfa.setGfaEnding(new String(nvl(getGfaEnding())));
/*  274 */     gfa.setGfaStates(new String(nvl(getGfaStates())));
/*      */ 
/*  276 */     gfa.setGfaCycleDay(getGfaCycleDay());
/*  277 */     gfa.setGfaCycleHour(getGfaCycleHour());
/*      */ 
/*  279 */     HashMap gfaValuesCopy = new HashMap();
/*  280 */     for (String str : getGfaValues().keySet()) {
/*  281 */       gfaValuesCopy.put(new String(str), new String(nvl((String)getGfaValues().get(str))));
/*      */     }
/*  283 */     gfa.setGfaValues(gfaValuesCopy);
/*      */ 
/*  285 */     gfa.setGfaTextCoordinate(new Coordinate(getGfaTextCoordinate()));
/*      */ 
/*  287 */     ArrayList notToBeSnappedCopy = new ArrayList();
/*  288 */     for (Coordinate pt : getNotToBeSnapped()) {
/*  289 */       notToBeSnappedCopy.add(new Coordinate(pt));
/*      */     }
/*  291 */     gfa.setNotToBeSnapped(notToBeSnappedCopy);
/*      */ 
/*  293 */     boolean[] reduceFlags = getReduceFlags();
/*  294 */     boolean[] reduceFlagsCopy = new boolean[reduceFlags.length];
/*  295 */     for (int ii = 0; ii < reduceFlags.length; ii++) {
/*  296 */       reduceFlagsCopy[ii] = reduceFlags[ii];
/*      */     }
/*  298 */     gfa.setReduceFlags(reduceFlagsCopy);
/*      */ 
/*  301 */     Calendar cal = (Calendar)getAttribute("ISSUE_TIME", Calendar.class);
/*      */ 
/*  303 */     if (cal != null) {
/*  304 */       Calendar issueTime = Calendar.getInstance();
/*  305 */       issueTime.setTimeInMillis(cal.getTimeInMillis());
/*  306 */       gfa.addAttribute("ISSUE_TIME", issueTime);
/*      */     }
/*      */ 
/*  309 */     cal = (Calendar)getAttribute("UNTIL_TIME", Calendar.class);
/*  310 */     if (cal != null) {
/*  311 */       Calendar untilTime = Calendar.getInstance();
/*  312 */       untilTime.setTimeInMillis(cal.getTimeInMillis());
/*  313 */       gfa.addAttribute("UNTIL_TIME", untilTime);
/*      */     }
/*      */ 
/*  316 */     cal = (Calendar)getAttribute("OUTLOOK_END_TIME", Calendar.class);
/*  317 */     if (cal != null) {
/*  318 */       Calendar otlkEndTime = Calendar.getInstance();
/*  319 */       otlkEndTime.setTimeInMillis(cal.getTimeInMillis());
/*  320 */       gfa.addAttribute("OUTLOOK_END_TIME", otlkEndTime);
/*      */     }
/*      */ 
/*  323 */     if ("ICE".equals(getGfaHazard())) {
/*  324 */       gfa.setGfaValue("Type", new String(nvl(getGfaValue("Type"))));
/*      */     }
/*      */ 
/*  327 */     GfaWording w = (GfaWording)getAttribute("WORDING", GfaWording.class);
/*  328 */     if (w != null) {
/*  329 */       GfaWording wds = new GfaWording();
/*  330 */       wds.condsContg = new String(nvl(w.getCondsContg()));
/*  331 */       wds.fromCondsDvlpg = new String(nvl(w.getFromCondsDvlpg()));
/*  332 */       wds.fromCondsEndg = new String(nvl(w.getFromCondsEndg()));
/*  333 */       wds.genOlk = new String(nvl(w.getGenOlk()));
/*  334 */       wds.otlkCondsDvlpg = new String(nvl(w.getOtlkCondsDvlpg()));
/*  335 */       wds.otlkCondsEndg = new String(nvl(w.getOtlkCondsEndg()));
/*  336 */       gfa.addAttribute("WORDING", wds);
/*      */     }
/*      */ 
/*  339 */     return gfa;
/*      */   }
/*      */ 
/*      */   public void update(IAttribute iattr)
/*      */   {
/*  346 */     if ((iattr instanceof IGfa)) {
/*  347 */       super.update(iattr);
/*      */ 
/*  349 */       IGfa attr = (IGfa)iattr;
/*  350 */       setSizeScale(1.0D);
/*  351 */       setGfaHazard(attr.getGfaHazard());
/*      */ 
/*  353 */       if (!attr.getGfaTag().isEmpty()) {
/*  354 */         setGfaTag(attr.getGfaTag());
/*      */       }
/*      */ 
/*  357 */       if (!attr.getGfaDesk().isEmpty()) {
/*  358 */         setGfaDesk(attr.getGfaDesk());
/*      */       }
/*      */ 
/*  361 */       if (!attr.getGfaIssueType().isEmpty()) {
/*  362 */         setGfaIssueType(attr.getGfaIssueType());
/*      */       }
/*      */ 
/*  365 */       if ((attr.getGfaType() != null) && (!attr.getGfaType().isEmpty())) {
/*  366 */         setGfaType(attr.getGfaType());
/*      */       }
/*      */ 
/*  369 */       if ((attr.getGfaFcstHr() != null) && (!attr.getGfaFcstHr().isEmpty())) {
/*  370 */         setGfaFcstHr(attr.getGfaFcstHr());
/*      */       }
/*      */ 
/*  373 */       if (attr.getGfaValues() != null) {
/*  374 */         HashMap values = attr.getGfaValues();
/*  375 */         if (values.size() != 0)
/*      */         {
/*  377 */           boolean reset = false;
/*  378 */           for (String key : values.keySet()) {
/*  379 */             reset = (values.get(key) != null) && (!((String)values.get(key)).isEmpty());
/*  380 */             if (reset) {
/*      */               break;
/*      */             }
/*      */           }
/*  384 */           if (reset) setGfaValues(attr.getGfaValues());
/*      */         }
/*      */       }
/*      */ 
/*  388 */       setGfaArea(attr.getGfaArea());
/*  389 */       setGfaBeginning(attr.getGfaBeginning());
/*  390 */       setGfaEnding(attr.getGfaEnding());
/*  391 */       setGfaStates(attr.getGfaStates());
/*  392 */       setGfaCycleDay(attr.getGfaCycleDay());
/*  393 */       setGfaCycleHour(attr.getGfaCycleHour());
/*  394 */       setClosed(Boolean.valueOf(!"Open".equalsIgnoreCase((String)this.values.get("Contour"))));
/*      */ 
/*  396 */       if ((attr.getGfaHazard().equals("ICE")) && (
/*  397 */         (attr.getGfaValues().get("Type") == null) || 
/*  398 */         (((String)attr.getGfaValues().get("Type")).trim().length() == 0)))
/*  399 */         setGfaValue("Type", "ICE");
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getPatternName()
/*      */   {
/*  406 */     if ("FZLVL".equals(this.hazard)) {
/*  407 */       if ("SFC".equals(getGfaValue("Level"))) {
/*  408 */         setSizeScale(0.3D);
/*  409 */         return "ZIGZAG";
/*      */       }
/*  411 */       setSizeScale(1.0D);
/*  412 */       return "LINE_DASHED_3";
/*      */     }
/*      */ 
/*  415 */     return "LINE_SOLID";
/*      */   }
/*      */ 
/*      */   public String getSymbolType() {
/*  419 */     if (("TURB".equals(this.hazard)) || 
/*  420 */       ("TURB-HI".equals(this.hazard)) || 
/*  421 */       ("TURB-LO".equals(this.hazard)))
/*  422 */       return "TURBULENCE_4";
/*  423 */     if ("ICE".equals(this.hazard))
/*  424 */       return "ICING_05";
/*  425 */     if (("SFC_WND".equals(this.hazard)) && ("30KT".equalsIgnoreCase(getGfaValue("Speed"))))
/*  426 */       return "SFC_WND_30K";
/*  427 */     if (("SFC_WND".equals(this.hazard)) && ("20KT".equalsIgnoreCase(getGfaValue("Speed"))))
/*  428 */       return "SFC_WND_20K";
/*  429 */     if ("MT_OBSC".equals(this.hazard)) {
/*  430 */       return "MT_OBSC";
/*      */     }
/*      */ 
/*  433 */     return null;
/*      */   }
/*      */ 
/*      */   public String[] getString() {
/*  437 */     ArrayList list = new ArrayList();
/*  438 */     if (!"NRML".equals(this.issueType)) {
/*  439 */       list.add(this.issueType);
/*      */     }
/*  441 */     String f = "";
/*  442 */     if (isSnapshot())
/*      */       try {
/*  444 */         Calendar cal = Calendar.getInstance();
/*  445 */         cal.set(5, this.cycleDay);
/*  446 */         cal.set(11, this.cycleHour);
/*  447 */         cal.set(12, 0);
/*      */ 
/*  449 */         if (this.fcstHr.contains(":")) {
/*  450 */           String[] s = this.fcstHr.split(":");
/*  451 */           int h = Integer.parseInt(s[0].trim());
/*  452 */           int m = Integer.parseInt(s[1].trim());
/*  453 */           cal.add(12, m);
/*      */         } else {
/*  455 */           h = Integer.parseInt(this.fcstHr);
/*      */         }
/*  457 */         cal.add(10, h);
/*  458 */         int h = cal.get(11);
/*  459 */         value = h;
/*  460 */         m = cal.get(12);
/*  461 */         f = this.fcstHr + " " + value + ":" + (m < 10 ? "0" + m : new StringBuilder().append(m).toString());
/*      */       } catch (Exception ignore) {
/*  463 */         f = this.fcstHr;
/*      */       }
/*      */     else {
/*  466 */       f = this.fcstHr;
/*      */     }
/*  468 */     f = f + " " + getGfaTag() + this.desk;
/*  469 */     list.add(f);
/*  470 */     if (("CLD".equals(this.hazard)) && (getGfaValue("Coverage") != null) && (!getGfaValue("Coverage").isEmpty())) {
/*  471 */       list.add(getGfaValue("Coverage"));
/*      */     }
/*      */ 
/*  474 */     if ("MTW".equals(this.hazard)) {
/*  475 */       list.add(getGfaValue("Intensity"));
/*  476 */     } else if ("TS".equals(this.hazard)) {
/*  477 */       if ((getGfaValue("Category") != null) && (!getGfaValue("Category").isEmpty())) list.add(getGfaValue("Category"));
/*  478 */       if ((getGfaValue("Frequency") != null) && (!getGfaValue("Frequency").isEmpty())) list.add(getGfaValue("Frequency"));
/*      */     }
/*      */ 
/*  481 */     String typeToDisplay = this.hazard;
/*  482 */     if ((this.type != null) && (!this.type.isEmpty())) typeToDisplay = typeToDisplay + " " + this.type;
/*  483 */     if (("TS".equals(this.hazard)) && ("true".equals(getGfaValue("GR")))) typeToDisplay = typeToDisplay + " GR";
/*      */ 
/*  485 */     for (Node n : getDisplayTextNodes()) {
/*  486 */       typeToDisplay = typeToDisplay.replaceAll(n.valueOf("@originalText"), 
/*  487 */         n.valueOf("@displayAs").replace(",,", "\n"));
/*      */     }
/*      */ 
/*  490 */     if ("FZLVL".equals(this.hazard))
/*  491 */       typeToDisplay = typeToDisplay.replaceAll("FZLVL", "0°:" + getGfaValue("Level"));
/*  494 */     String[] arrayOfString1;
/*  494 */     int m = (arrayOfString1 = justify(f.length(), typeToDisplay)).length; for (String value = 0; value < m; value++) { String s = arrayOfString1[value];
/*  495 */       list.add(s.trim().replace("ICONHERE", ""));
/*      */     }
/*      */ 
/*  498 */     if ("CLD".equals(this.hazard)) {
/*  499 */       list.add(getGfaValue("Bottom"));
/*  500 */     } else if (("ICE".equals(this.hazard)) || 
/*  501 */       ("TURB".equals(this.hazard)) || 
/*  502 */       ("TURB-HI".equals(this.hazard)) || 
/*  503 */       ("TURB-LO".equals(this.hazard)) || 
/*  504 */       ("MTW".equals(this.hazard)) || 
/*  505 */       ("M_FZLVL".equals(this.hazard)) || 
/*  506 */       ("TS".equals(this.hazard)) || 
/*  507 */       ("CLD_TOPS".equals(this.hazard))) {
/*  508 */       if ((getGfaTop() != null) && (!getGfaTop().isEmpty())) list.add(getGfaTop());
/*  509 */       if ((getGfaBottom() != null) && (!getGfaBottom().isEmpty())) {
/*  510 */         String line = getGfaBottom();
/*  511 */         if (("FZL".equals(line)) && (getGfaValue("FZL Top/Bottom") != null)) {
/*  512 */           line = getGfaValue("FZL Top/Bottom");
/*      */         }
/*  514 */         list.add(line);
/*      */       }
/*      */     } else { "SFC_WND".equals(this.hazard); }
/*      */ 
/*      */ 
/*  519 */     String[] a = new String[list.size()];
/*  520 */     return (String[])list.toArray(a);
/*      */   }
/*      */ 
/*      */   private List<Node> getDisplayTextNodes()
/*      */   {
/*  525 */     String xPath = "/root/displayText/value[@hazard='" + this.hazard + "']|/root/displayText/value[@hazard='']";
/*  526 */     List displayTextNodes = GfaInfo.getDocument().selectNodes(xPath);
/*  527 */     return displayTextNodes;
/*      */   }
/*      */ 
/*      */   public static String[] justify(int width, String str)
/*      */   {
/*  538 */     width = width < 8 ? 8 : width;
/*      */ 
/*  540 */     StringBuffer buf = new StringBuffer(str);
/*      */ 
/*  543 */     int visLoc = buf.indexOf("VIS");
/*  544 */     if ((visLoc > 0) && (buf.indexOf("CIG") >= 0)) {
/*  545 */       buf.setCharAt(visLoc - 1, '\n');
/*      */     }
/*      */ 
/*  549 */     int firstBackSlash = buf.indexOf("/");
/*  550 */     int reset = -1;
/*  551 */     if (firstBackSlash > 0)
/*      */     {
/*  553 */       int jj = firstBackSlash;
/*  554 */       while (jj > 0) {
/*  555 */         if ((buf.charAt(jj) == ' ') || (buf.charAt(jj) == '\n')) {
/*  556 */           reset = jj;
/*  557 */           buf.setCharAt(jj, '\n');
/*  558 */           break;
/*      */         }
/*      */ 
/*  561 */         jj--;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  566 */     int lastDelimeter = -1;
/*  567 */     int lineStart = 0;
/*  568 */     int i = 0;
/*      */ 
/*  570 */     while (i < buf.length()) {
/*  571 */       if ((buf.charAt(i) == ' ') || (buf.charAt(i) == '/')) lastDelimeter = i + 1;
/*  572 */       if (buf.charAt(i) == '\n') {
/*  573 */         lastDelimeter = -1;
/*  574 */         lineStart = i + 1;
/*      */       }
/*      */ 
/*  578 */       if ((visLoc > 0) && (i >= visLoc)) width = 12;
/*  579 */       if ((reset > 0) && (i > reset)) width = 6;
/*      */ 
/*  581 */       if (i > lineStart + width - 1) {
/*  582 */         if (lastDelimeter != -1) {
/*  583 */           buf.insert(lastDelimeter, '\n');
/*  584 */           lineStart = lastDelimeter + 1;
/*  585 */           lastDelimeter = -1;
/*      */         } else {
/*  587 */           buf.insert(i, '\n');
/*  588 */           lineStart = i + 1;
/*      */         }
/*      */       }
/*  591 */       i++;
/*      */     }
/*      */ 
/*  594 */     return buf.toString().split("\n");
/*      */   }
/*      */ 
/*      */   public String getGfaHazard() {
/*  598 */     return this.hazard;
/*      */   }
/*      */ 
/*      */   public void setGfaHazard(String hazard) {
/*  602 */     this.hazard = hazard;
/*      */   }
/*      */ 
/*      */   public String getGfaFcstHr() {
/*  606 */     return this.fcstHr;
/*      */   }
/*      */ 
/*      */   public String getForecastHours()
/*      */   {
/*  611 */     return getGfaFcstHr();
/*      */   }
/*      */ 
/*      */   public void setGfaFcstHr(String fcstHr) {
/*  615 */     this.fcstHr = ((fcstHr == null) || (fcstHr.isEmpty()) ? "0" : fcstHr);
/*      */   }
/*      */ 
/*      */   public String getGfaTag() {
/*  619 */     return this.tag;
/*      */   }
/*      */ 
/*      */   public void setGfaTag(String tag) {
/*  623 */     if (tag != null) tag = tag.replace("*", "");
/*  624 */     this.tag = tag;
/*      */   }
/*      */ 
/*      */   public String getGfaDesk() {
/*  628 */     return this.desk;
/*      */   }
/*      */ 
/*      */   public void setGfaDesk(String desk) {
/*  632 */     this.desk = desk;
/*      */   }
/*      */ 
/*      */   public String getGfaIssueType() {
/*  636 */     return this.issueType;
/*      */   }
/*      */ 
/*      */   public void setGfaIssueType(String issueType) {
/*  640 */     this.issueType = issueType;
/*      */   }
/*      */ 
/*      */   public Coordinate getGfaTextCoordinate() {
/*  644 */     return this.gfaTextCoordinate;
/*      */   }
/*      */ 
/*      */   public void setGfaTextCoordinate(Coordinate gfaTextCoordinate) {
/*  648 */     this.gfaTextCoordinate = gfaTextCoordinate;
/*      */   }
/*      */ 
/*      */   public int getGfaCycleDay() {
/*  652 */     return this.cycleDay;
/*      */   }
/*      */ 
/*      */   public int getGfaCycleHour() {
/*  656 */     return this.cycleHour;
/*      */   }
/*      */ 
/*      */   public void setGfaCycleDay(int day) {
/*  660 */     this.cycleDay = day;
/*      */   }
/*      */ 
/*      */   public void setGfaCycleHour(int hour) {
/*  664 */     this.cycleHour = hour;
/*      */   }
/*      */ 
/*      */   public String getGfaType() {
/*  668 */     return this.type;
/*      */   }
/*      */ 
/*      */   public void setGfaType(String type) {
/*  672 */     this.type = type;
/*      */   }
/*      */ 
/*      */   public String getGfaArea() {
/*  676 */     return this.area;
/*      */   }
/*      */ 
/*      */   public void setGfaArea(String area) {
/*  680 */     this.area = area;
/*      */   }
/*      */ 
/*      */   public String getGfaBeginning() {
/*  684 */     return this.beginning;
/*      */   }
/*      */ 
/*      */   public void setGfaBeginning(String beginning) {
/*  688 */     this.beginning = beginning;
/*      */   }
/*      */ 
/*      */   public String getGfaEnding() {
/*  692 */     return this.ending;
/*      */   }
/*      */ 
/*      */   public void setGfaEnding(String ending) {
/*  696 */     this.ending = ending;
/*      */   }
/*      */ 
/*      */   public String getGfaStates() {
/*  700 */     return this.states;
/*      */   }
/*      */ 
/*      */   public void setGfaStates(String states) {
/*  704 */     this.states = states;
/*      */   }
/*      */ 
/*      */   public String getGfaValue(String key) {
/*  708 */     return (String)this.values.get(key);
/*      */   }
/*      */ 
/*      */   public String getGfaTop() {
/*  712 */     return getGfaValue("Top");
/*      */   }
/*      */ 
/*      */   public String getGfaBottom() {
/*  716 */     return getGfaValue("Bottom");
/*      */   }
/*      */ 
/*      */   public void setGfaValue(String key, String value) {
/*  720 */     if (value == null)
/*      */     {
/*  722 */       return;
/*      */     }
/*  724 */     this.values.put(key, value);
/*      */   }
/*      */ 
/*      */   public HashMap<String, String> getGfaValues() {
/*  728 */     HashMap copy = new HashMap();
/*  729 */     if (this.values == null) return copy;
/*      */ 
/*  731 */     for (String key : this.values.keySet()) {
/*  732 */       copy.put(key, (String)this.values.get(key));
/*      */     }
/*  734 */     return copy;
/*      */   }
/*      */ 
/*      */   public void setGfaValues(HashMap<String, String> values) {
/*  738 */     this.values = values;
/*      */   }
/*      */ 
/*      */   public String valuesToString() {
/*  742 */     StringBuilder sb = new StringBuilder(200);
/*  743 */     if ((this.values == null) || (this.values.isEmpty())) return null;
/*  744 */     for (String key : this.values.keySet()) {
/*  745 */       sb.append("(").append(key).append(",");
/*  746 */       sb.append(((String)this.values.get(key)).toString());
/*  747 */       sb.append(")");
/*      */     }
/*  749 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public boolean isSnapshot() {
/*  753 */     return (this.fcstHr != null) && (this.fcstHr.indexOf("-") == -1);
/*      */   }
/*      */ 
/*      */   public boolean isAirmet() {
/*  757 */     if (isSnapshot()) return false;
/*  758 */     String[] s = nvl(getGfaFcstHr()).split("-");
/*  759 */     int[] hm = getHourMinInt(s[1]);
/*  760 */     return hm[0] <= 6;
/*      */   }
/*      */ 
/*      */   public boolean isOutlook() {
/*  764 */     if (isSnapshot()) return false;
/*  765 */     String[] s = nvl(getGfaFcstHr()).split("-");
/*  766 */     int[] hm0 = getHourMinInt(s[0]);
/*  767 */     int[] hm1 = getHourMinInt(s[1]);
/*  768 */     return (hm0[0] >= 6) && (hm1[0] > 6);
/*      */   }
/*      */ 
/*      */   public boolean isFormat() {
/*  772 */     boolean isFormat = GfaInfo.isFormat(getGfaHazard());
/*  773 */     if ("SFC_WND".equals(getGfaHazard()))
/*      */     {
/*  775 */       isFormat &= "30KT".equalsIgnoreCase(getGfaValue("Speed"));
/*  776 */     }return isFormat;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  783 */     StringBuilder result = new StringBuilder(getClass().getSimpleName());
/*      */ 
/*  785 */     result.append("Category:\t" + this.pgenCategory + "\n");
/*  786 */     result.append("Type:\t" + this.pgenType + "\n");
/*  787 */     result.append("hazard:\t" + this.hazard + "\t\n");
/*  788 */     result.append("fcstHr:\t" + this.fcstHr + "\t\n");
/*  789 */     result.append("tag:\t" + this.tag + "\t\n");
/*  790 */     result.append("desk:\t" + this.desk + "\t\n");
/*  791 */     result.append("issueType:\t" + this.issueType + "\t\n");
/*  792 */     result.append("type:\t" + this.type + "\t\n");
/*      */ 
/*  794 */     result.append("Color:\t" + this.colors[0] + "\n");
/*  795 */     result.append("LineWidth:\t" + this.lineWidth + "\n");
/*  796 */     result.append("SizeScale:\t" + this.sizeScale + "\n");
/*  797 */     result.append("Closed:\t" + this.closed + "\n");
/*  798 */     result.append("Filled:\t" + this.filled + "\n");
/*  799 */     result.append("SmoothFactor:\t" + this.smoothFactor + "\n");
/*  800 */     result.append("FillPattern:\t" + this.fillPattern + "\n");
/*      */ 
/*  802 */     result.append("gfaTextCoordinate:\t\t\n");
/*      */ 
/*  804 */     result.append("Non-null attributes:\t\t\n");
/*  805 */     for (String key : this.attributes.keySet()) {
/*  806 */       Object o = this.attributes.get(key);
/*  807 */       if (o != null) {
/*  808 */         result.append("\t" + key);
/*  809 */         if ((!(o instanceof Gfa)) && (!(o instanceof Collection))) {
/*  810 */           result.append("\t" + o.toString());
/*      */         }
/*  812 */         result.append("\n");
/*      */       }
/*      */     }
/*      */ 
/*  816 */     result.append("Location:\t\t\n");
/*  817 */     for (Coordinate point : this.linePoints) {
/*  818 */       result.append("\t" + point.x + "\t" + point.y + "\n");
/*      */     }
/*      */ 
/*  821 */     return result.toString();
/*      */   }
/*      */ 
/*      */   public void addNotToBeSnapped(Coordinate c)
/*      */   {
/*  830 */     for (Coordinate p : getPoints())
/*  831 */       if (GfaRules.compareCoordinates(c, p)) {
/*  832 */         this.notToBeSnapped.add(c);
/*  833 */         return;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void addNotToBeSnapped(Coordinate[] array)
/*      */   {
/*  845 */     if (array == null) return;
/*  846 */     for (Coordinate c : array)
/*  847 */       addNotToBeSnapped(c);
/*      */   }
/*      */ 
/*      */   public void clearNotToBeSnapped()
/*      */   {
/*  856 */     this.notToBeSnapped.clear();
/*      */   }
/*      */ 
/*      */   public ArrayList<Coordinate> getNotToBeSnapped()
/*      */   {
/*  865 */     return this.notToBeSnapped;
/*      */   }
/*      */ 
/*      */   public void setNotToBeSnapped(ArrayList<Coordinate> notToBeSnapped)
/*      */   {
/*  874 */     this.notToBeSnapped = notToBeSnapped;
/*      */   }
/*      */ 
/*      */   public void snap() {
/*  878 */     if (isSnapshot()) return;
/*  879 */     ArrayList points = getPoints();
/*  880 */     for (Coordinate p : points)
/*  881 */       if (canBeSnapped(p)) {
/*  882 */         List tempList = new ArrayList();
/*  883 */         tempList.add(p);
/*  884 */         tempList = SnapUtil.getSnapWithStation(tempList, SnapUtil.VOR_STATION_LIST, 10, 16, false);
/*  885 */         Coordinate c = (Coordinate)tempList.get(0);
/*  886 */         p.setCoordinate(c);
/*      */       }
/*      */   }
/*      */ 
/*      */   public boolean canBeSnapped(Coordinate point)
/*      */   {
/*  892 */     for (Coordinate no : this.notToBeSnapped) {
/*  893 */       if (GfaRules.compareCoordinates(no, point))
/*      */       {
/*  895 */         return false;
/*      */       }
/*      */     }
/*  898 */     return true;
/*      */   }
/*      */ 
/*      */   public Object getAttribute(String attrName) {
/*  902 */     return getAttribute(attrName, Object.class);
/*      */   }
/*      */ 
/*      */   public <T> T getAttribute(String attrName, Class T)
/*      */   {
/*  907 */     return this.attributes.get(attrName);
/*      */   }
/*      */ 
/*      */   public void addAttribute(String attrName, Object value) {
/*  911 */     this.attributes.put(attrName, value);
/*      */   }
/*      */ 
/*      */   public void removeAttribute(String attrName) {
/*  915 */     this.attributes.remove(attrName);
/*      */   }
/*      */ 
/*      */   public void clearAttributes() {
/*  919 */     this.attributes.clear();
/*      */   }
/*      */ 
/*      */   public boolean[] getReduceFlags()
/*      */   {
/*  930 */     boolean[] flags = (boolean[])this.attributes.get("REDUCE_FLAGS");
/*      */ 
/*  932 */     if (flags == null) {
/*  933 */       flags = new boolean[getLinePoints().length];
/*  934 */       for (int ii = 0; ii < getLinePoints().length; ii++) {
/*  935 */         flags[ii] = true;
/*      */       }
/*      */     }
/*      */ 
/*  939 */     return flags;
/*      */   }
/*      */ 
/*      */   public void setReduceFlags(boolean[] reduceFlgs)
/*      */   {
/*  949 */     this.attributes.put("REDUCE_FLAGS", reduceFlgs);
/*      */   }
/*      */ 
/*      */   public static int[] getHourMinInt(String hourMinStr)
/*      */   {
/*  961 */     if (nvl(hourMinStr).isEmpty()) {
/*  962 */       return null;
/*      */     }
/*  964 */     String[] s = hourMinStr.split(":");
/*  965 */     int[] hm = new int[2];
/*  966 */     hm[0] = Integer.parseInt(s[0]);
/*  967 */     if (s.length > 1) {
/*  968 */       hm[1] = Integer.parseInt(s[1]);
/*      */     }
/*  970 */     return hm;
/*      */   }
/*      */ 
/*      */   public static String nvl(String value) {
/*  974 */     return value == null ? "" : value;
/*      */   }
/*      */ 
/*      */   public int compareTo(Gfa g1)
/*      */   {
/*  979 */     if (this == g1) return 0;
/*  980 */     return 1;
/*      */   }
/*      */ 
/*      */   public Polygon toPolygon()
/*      */   {
/*  989 */     return GfaClip.getInstance().gfaToPolygon(this);
/*      */   }
/*      */ 
/*      */   public String getGfaVorText()
/*      */   {
/*  998 */     return getGfaValue("FROM LINE");
/*      */   }
/*      */ 
/*      */   public void setGfaVorText(String vorText)
/*      */   {
/* 1006 */     setGfaValue("FROM LINE", vorText);
/*      */   }
/*      */ 
/*      */   public static String buildVorText(Gfa gfa)
/*      */   {
/* 1015 */     String s = "";
/*      */ 
/* 1017 */     ArrayList pts = gfa.getPoints();
/*      */ 
/* 1019 */     pts = SnapUtil.getSnapWithStation(pts, SnapUtil.VOR_STATION_LIST, 10, 16, false);
/*      */ 
/* 1021 */     Coordinate[] a = new Coordinate[pts.size()];
/* 1022 */     a = (Coordinate[])pts.toArray(a);
/*      */ 
/* 1024 */     if (gfa.getGfaHazard().equalsIgnoreCase("FZLVL")) {
/* 1025 */       if (gfa.isClosedLine().booleanValue()) {
/* 1026 */         s = SnapUtil.getVORText(a, "-", "Area", -1, true, false, true);
/*      */       }
/*      */       else {
/* 1029 */         s = SnapUtil.getVORText(a, "-", "Line", -1, true, false, true);
/*      */       }
/*      */     }
/* 1032 */     else if (gfa.getGfaHazard().equalsIgnoreCase("LLWS")) {
/* 1033 */       s = SnapUtil.getVORText(a, "-", "Area", -1, true, false, true);
/*      */     }
/*      */     else {
/* 1036 */       s = SnapUtil.getVORText(a, " TO ", "Area", -1, true, false, true);
/*      */     }
/*      */ 
/* 1039 */     return s;
/*      */   }
/*      */ 
/*      */   public boolean isValid()
/*      */   {
/* 1047 */     boolean valid = true;
/*      */ 
/* 1049 */     Polygon pp = toPolygon();
/* 1050 */     if ((pp == null) || (!pp.isValid())) {
/* 1051 */       valid = false;
/*      */     }
/*      */     else {
/* 1054 */       Polygon pg = GfaClip.getInstance().gfaToPolygonInGrid(this);
/* 1055 */       if ((pg == null) || (!pg.isValid())) {
/* 1056 */         valid = false;
/*      */       }
/*      */     }
/*      */ 
/* 1060 */     return valid;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.Gfa
 * JD-Core Version:    0.6.2
 */