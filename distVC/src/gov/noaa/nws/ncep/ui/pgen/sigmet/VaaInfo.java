/*      */ package gov.noaa.nws.ncep.ui.pgen.sigmet;
/*      */ 
/*      */ import com.raytheon.uf.common.dataquery.requests.DbQueryRequest;
/*      */ import com.raytheon.uf.common.dataquery.requests.DbQueryRequest.OrderMode;
/*      */ import com.raytheon.uf.common.dataquery.requests.RequestConstraint;
/*      */ import com.raytheon.uf.common.dataquery.requests.RequestConstraint.ConstraintType;
/*      */ import com.raytheon.uf.common.dataquery.responses.DbQueryResponse;
/*      */ import com.raytheon.uf.viz.core.requests.ThriftClient;
/*      */ import gov.noaa.nws.ncep.common.dataplugin.pgen.PgenRecord;
/*      */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*      */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Method;
/*      */ import java.text.NumberFormat;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.TimeZone;
/*      */ import java.util.TreeMap;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.xml.bind.JAXBContext;
/*      */ import javax.xml.bind.Marshaller;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerFactory;
/*      */ import javax.xml.transform.dom.DOMSource;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import javax.xml.transform.stream.StreamSource;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ 
/*      */ public class VaaInfo
/*      */ {
/*      */   public static final String VOLCANO_PRODUCT_NAME = "VOLCANO";
/*      */   public static final String PGEN_CATEGORY = "SIGMET";
/*      */   public static final String PGEN_TYPE_VOLCANO = "VOLC_SIGMET";
/*      */   public static final String PGEN_TYEP_CLOUD = "VACL_SIGMET";
/*  107 */   public static final Map<String, ArrayList<String>> VAA_INFO_MAP = new HashMap();
/*      */ 
/*  112 */   public static final String[] LOCS = { "LOC_EDIT", "LOC_CREATE" };
/*      */ 
/*  117 */   public static final String[] LAYERS = { "VOLCANO", "OBS", 
/*  118 */     "F06", "F12", "F18" };
/*      */   public static final String OTHERSFCST_DIALOG = "dialog";
/*      */   public static final String OTHERSFCST_DISPLAY = "display";
/*      */   public static final String OTHERSFCST_TEXT = "text";
/*  131 */   public static final Map<Volcano, gov.noaa.nws.ncep.ui.pgen.elements.Product> VOL_PROD_MAP = new HashMap();
/*      */   public static final String SEPERATER = ":::";
/*      */   public static final String NONE_DRAWABLE_MSG = "THIS IS A TEXT PRODUCT THAT CANNOT BE DISPLAYED!";
/*      */   public static final String VA_NOT_IDENTIFIABLE = "VA NOT IDENTIFIABLE FROM SATELLITE DATA";
/*      */   public static final String TYPE_TEXT = "Text";
/*  198 */   public static final String DEFAULT_PRODUCT = ProductInfo.getProduct(LOCS[0])[0];
/*      */ 
/*  161 */   public static int CURRENT_LAYER_INDEX = 1;
/*      */ 
/*  168 */   public static Map<String, String[]> VAA_INFO_SINGLE_MAP = new HashMap();
/*  169 */   public static Map<String, String[]> VAA_INFO_PATH_MAP = new HashMap();
/*  170 */   public static Map<String, String[]> VAA_INFO_STN_MAP = new HashMap();
/*  171 */   public static Map<String, String[]> VAA_INFO_OTHERSFCST_MAP = new HashMap();
/*  172 */   public static Map<String, String[]> VAA_INFO_WORDING_MAP = new HashMap();
/*      */ 
/*  180 */   public static final Map<String, Method> ENTRY_VOLSETTER_MAP = new HashMap();
/*      */   public static final String FILE_EXTENSION_XML = "xml";
/*      */   public static final String FILE_EXTENSION_TXT = "txt";
/*      */ 
/*      */   static
/*      */   {
/*  193 */     parseVaaFile();
/*  194 */     initVaaMaps();
/*      */ 
/*  196 */     initEntryVolSetterMap();
/*      */   }
/*      */ 
/*      */   public static void initEntryVolSetterMap()
/*      */   {
/*      */     try
/*      */     {
/*  215 */       ENTRY_VOLSETTER_MAP.put("<VOLCANO>", 
/*  216 */         Volcano.class.getMethod("setName", new Class[] { String.class }));
/*  217 */       ENTRY_VOLSETTER_MAP.put("<NUMBER>", 
/*  218 */         Volcano.class.getMethod("setNumber", new Class[] { String.class }));
/*  219 */       ENTRY_VOLSETTER_MAP.put("<LOCATION>", 
/*  220 */         Volcano.class.getMethod("setTxtLoc", new Class[] { String.class }));
/*  221 */       ENTRY_VOLSETTER_MAP.put("<AREA>", 
/*  222 */         Volcano.class.getMethod("setArea", new Class[] { String.class }));
/*  223 */       ENTRY_VOLSETTER_MAP.put("<SUMMIT ELEVATION>", 
/*  224 */         Volcano.class.getMethod("setElev", new Class[] { String.class }));
/*  225 */       ENTRY_VOLSETTER_MAP.put("<ADVISORY NUMBER>", 
/*  226 */         Volcano.class.getMethod("setAdvNum", new Class[] { String.class }));
/*      */ 
/*  235 */       ENTRY_VOLSETTER_MAP.put("<OBS ASH CLOUD>", Volcano.class.getMethod(
/*  236 */         "setObsFcstAshCloudInfo", new Class[] { String.class }));
/*  237 */       ENTRY_VOLSETTER_MAP.put("<FCST ASH CLOUD +6H>", Volcano.class
/*  238 */         .getMethod("setObsFcstAshCloudInfo6", new Class[] { String.class }));
/*  239 */       ENTRY_VOLSETTER_MAP.put("<FCST ASH CLOUD +12H>", Volcano.class
/*  240 */         .getMethod("setObsFcstAshCloudInfo12", new Class[] { String.class }));
/*  241 */       ENTRY_VOLSETTER_MAP.put("<FCST ASH CLOUD +18H>", Volcano.class
/*  242 */         .getMethod("setObsFcstAshCloudInfo18", new Class[] { String.class }));
/*      */ 
/*  249 */       ENTRY_VOLSETTER_MAP.put("<REMARKS>", 
/*  250 */         Volcano.class.getMethod("setExtraRemarks", new Class[] { String.class }));
/*  251 */       ENTRY_VOLSETTER_MAP
/*  252 */         .put("<INFORMATION SOURCE>", Volcano.class.getMethod(
/*  253 */         "setExtraInfoSource", new Class[] { String.class }));
/*  254 */       ENTRY_VOLSETTER_MAP.put("<ERUPTION DETAILS>", Volcano.class
/*  255 */         .getMethod("setExtraErupDetails", new Class[] { String.class }));
/*  256 */       ENTRY_VOLSETTER_MAP.put("<NEXT ADVISORY>", 
/*  257 */         Volcano.class.getMethod("setExtraNextAdv", new Class[] { String.class }));
/*      */     }
/*      */     catch (NoSuchMethodException e) {
/*  260 */       System.out.println("___initializing1: " + e.getMessage());
/*      */     } catch (SecurityException e) {
/*  262 */       System.out.println("___initializing2: " + e.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void initVaaMaps()
/*      */   {
/*  273 */     List singleList = new ArrayList();
/*      */ 
/*  275 */     for (String s : VAA_INFO_MAP.keySet()) {
/*  276 */       ArrayList ss = (ArrayList)VAA_INFO_MAP.get(s);
/*      */ 
/*  278 */       if ("others-fcst".equals(s.trim())) {
/*  279 */         initOthersFcstMap(ss);
/*      */       }
/*  281 */       for (String sss : ss) {
/*  282 */         String[] sa = sss.split(":::");
/*      */ 
/*  284 */         if ("header-information".equals(s.trim()))
/*  285 */           initStnMap(sa);
/*  286 */         else if ("path".equals(s.trim()))
/*  287 */           initPathMap(sa);
/*  288 */         else if ("format".equals(s.trim())) {
/*  289 */           initProductMap(sa);
/*      */         }
/*  292 */         else if ("wording".equals(s.trim()))
/*  293 */           initWordingMap(sa);
/*      */         else {
/*  295 */           for (String ssss : sa) {
/*  296 */             String[] values = ssss.split("=");
/*      */ 
/*  298 */             if ((sa.length <= 1) && (!"path".equalsIgnoreCase(s.trim())))
/*      */             {
/*  300 */               if (values.length > 1) {
/*  301 */                 singleList.add(values[1]);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  307 */       VAA_INFO_SINGLE_MAP.put(s.trim(), 
/*  308 */         (String[])singleList.toArray(new String[0]));
/*  309 */       singleList.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void initStnMap(String[] s)
/*      */   {
/*  322 */     String stn = ""; String vaac = ""; String id = ""; String hdr = "";
/*  323 */     String[] arrayOfString1 = s; int j = s.length; for (int i = 0; i < j; i++) { String ss = arrayOfString1[i];
/*  324 */       String[] sss = ss.split("=");
/*  325 */       if (sss.length > 1) {
/*  326 */         if ("orig-stn".equals(sss[0].trim()))
/*  327 */           stn = sss[1];
/*  328 */         else if ("vaac".equals(sss[0].trim()))
/*  329 */           vaac = sss[1];
/*  330 */         else if ("wmoid".equals(sss[0].trim()))
/*  331 */           id = sss[1];
/*      */         else {
/*  333 */           hdr = sss[1];
/*      */         }
/*      */       }
/*      */     }
/*  337 */     VAA_INFO_STN_MAP.put(stn + vaac, new String[] { id, hdr });
/*      */   }
/*      */ 
/*      */   public static void initOthersFcstMap(ArrayList<String> sss)
/*      */   {
/*  350 */     LinkedHashSet current = new LinkedHashSet(); LinkedHashSet older = new LinkedHashSet(); LinkedHashSet oldest = new LinkedHashSet();
/*      */ 
/*  352 */     for (int i = 0; i < sss.size(); i++) {
/*  353 */       String[] ss = ((String)sss.get(i)).split(":::");
/*  354 */       for (String value : ss) {
/*  355 */         String[] s = value.split("=");
/*  356 */         if ("dialog".equals(s[0].trim()))
/*  357 */           current.add(s[1]);
/*  358 */         else if ("display".equals(s[0].trim()))
/*  359 */           older.add(s[1]);
/*      */         else {
/*  361 */           oldest.add(s[1]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  366 */     VAA_INFO_OTHERSFCST_MAP.put("dialog", 
/*  367 */       (String[])current.toArray(new String[0]));
/*  368 */     VAA_INFO_OTHERSFCST_MAP.put("display", 
/*  369 */       (String[])older.toArray(new String[0]));
/*  370 */     VAA_INFO_OTHERSFCST_MAP.put("text", 
/*  371 */       (String[])oldest.toArray(new String[0]));
/*      */   }
/*      */ 
/*      */   public static void initPathMap(String[] s)
/*      */   {
/*  381 */     String[] sss = null;
/*  382 */     String[] arrayOfString1 = s; int j = s.length; for (int i = 0; i < j; i++) { String ss = arrayOfString1[i];
/*  383 */       sss = ss.split("=");
/*      */     }
/*      */ 
/*  386 */     if (sss.length > 1)
/*  387 */       VAA_INFO_PATH_MAP.put(sss[0], new String[] { sss[1] });
/*      */   }
/*      */ 
/*      */   public static void initProductMap(String[] s)
/*      */   {
/*  398 */     String loc = ""; String prod = ""; String entry = "";
/*  399 */     String[] arrayOfString1 = s; int j = s.length; for (int i = 0; i < j; i++) { String ss = arrayOfString1[i];
/*  400 */       String[] sss = ss.split("=");
/*  401 */       if (sss.length > 1) {
/*  402 */         if ("location".equals(sss[0].trim()))
/*  403 */           loc = sss[1];
/*  404 */         else if ("product".equals(sss[0].trim()))
/*  405 */           prod = sss[1];
/*      */         else {
/*  407 */           entry = sss[1];
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  412 */     ProductInfo.setLocProdEntry(loc, prod, entry);
/*      */   }
/*      */ 
/*      */   public static void initWordingMap(String[] s)
/*      */   {
/*  422 */     String tag = ""; String neww = ""; String old = "";
/*  423 */     String[] arrayOfString1 = s; int j = s.length; for (int i = 0; i < j; i++) { String ss = arrayOfString1[i];
/*  424 */       String[] sss = ss.split("=");
/*  425 */       if (sss.length > 1) {
/*  426 */         if ("tag".equals(sss[0].trim()))
/*  427 */           tag = sss[1];
/*  428 */         else if ("new-wording".equals(sss[0].trim()))
/*  429 */           neww = sss[1];
/*      */         else {
/*  431 */           old = sss[1];
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  436 */     VAA_INFO_WORDING_MAP.put(tag, new String[] { neww, old });
/*      */   }
/*      */ 
/*      */   public static void parseVaaFile()
/*      */   {
/*  445 */     Document doc = null;
/*      */ 
/*  447 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*      */     try
/*      */     {
/*  450 */       DocumentBuilder builder = factory.newDocumentBuilder();
/*  451 */       File vaaFile = PgenStaticDataProvider.getProvider().getStaticFile(
/*  452 */         PgenStaticDataProvider.getProvider()
/*  453 */         .getPgenLocalizationRoot() + "vaa.xml");
/*      */ 
/*  455 */       doc = builder.parse(vaaFile.getAbsoluteFile());
/*      */     } catch (Exception e) {
/*  457 */       System.out.println("-----------" + e.getMessage());
/*      */     }
/*      */ 
/*  460 */     NodeList nlist = doc.getElementsByTagNameNS("*", "*");
/*      */ 
/*  462 */     for (int i = 0; i < nlist.getLength(); i++)
/*      */     {
/*  464 */       Node nElem = nlist.item(i);
/*      */ 
/*  466 */       String elemName = nElem.getNodeName().trim();
/*      */ 
/*  468 */       NamedNodeMap nnMap = nElem.getAttributes();
/*      */ 
/*  470 */       ArrayList listOld = null;
/*      */ 
/*  472 */       StringBuilder sb = new StringBuilder();
/*      */ 
/*  474 */       for (int j = 0; j < nnMap.getLength(); j++)
/*      */       {
/*  476 */         Node nAttr = nnMap.item(j);
/*      */ 
/*  478 */         handleAttrValue(sb, nAttr.getNodeName(), nAttr.getNodeValue());
/*      */       }
/*      */ 
/*  482 */       listOld = (ArrayList)VAA_INFO_MAP.get(elemName);
/*      */ 
/*  484 */       if (listOld != null) {
/*  485 */         listOld.add(getListString(sb));
/*      */       }
/*      */       else {
/*  488 */         ArrayList list = new ArrayList();
/*  489 */         list.add(getListString(sb));
/*  490 */         VAA_INFO_MAP.put(elemName, list);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void handleAttrValue(StringBuilder sb, String name, String value)
/*      */   {
/*  509 */     sb.append(name);
/*  510 */     sb.append("=");
/*  511 */     sb.append(value);
/*  512 */     sb.append(":::");
/*      */   }
/*      */ 
/*      */   public static String getListString(StringBuilder sb)
/*      */   {
/*  524 */     String s = sb.toString();
/*  525 */     int index = s.lastIndexOf(":::");
/*      */ 
/*  527 */     return index > 0 ? s.substring(0, s.lastIndexOf(":::")) : s;
/*      */   }
/*      */ 
/*      */   public static String getFootTxtFromMeter(double meter, int maxFracDigits)
/*      */   {
/*  539 */     NumberFormat nf = NumberFormat.getInstance();
/*  540 */     nf.setMaximumFractionDigits(maxFracDigits);
/*      */ 
/*  542 */     return nf.format(meter * 3.281D).replaceFirst(",", "");
/*      */   }
/*      */ 
/*      */   public static int getMeterIntFromFoot(String feet)
/*      */   {
/*  554 */     int iFeet = -999999;
/*      */     try
/*      */     {
/*  557 */       iFeet = Integer.parseInt(feet);
/*      */     } catch (Exception e) {
/*  559 */       iFeet = 0;
/*      */     }
/*      */ 
/*  562 */     return (int)Math.round(iFeet / 3.281D);
/*      */   }
/*      */ 
/*      */   public static String getDateTime(String tFormat)
/*      */   {
/*  574 */     SimpleDateFormat sdf = new SimpleDateFormat(tFormat);
/*  575 */     sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
/*      */ 
/*  577 */     return sdf.format(new Date());
/*      */   }
/*      */ 
/*      */   public static boolean isValidLatLon(String latlon)
/*      */   {
/*  589 */     if ((latlon == null) || (latlon.length() != 11)) {
/*  590 */       return false;
/*      */     }
/*  592 */     if ((latlon.charAt(0) != 'n') && (latlon.charAt(0) != 'N') && 
/*  593 */       (latlon.charAt(0) != 's') && (latlon.charAt(0) != 'S')) {
/*  594 */       return false;
/*      */     }
/*  596 */     if ((latlon.charAt(5) != 'w') && (latlon.charAt(5) != 'W') && 
/*  597 */       (latlon.charAt(5) != 'e') && (latlon.charAt(5) != 'E')) {
/*  598 */       return false;
/*      */     }
/*  600 */     String lat = latlon.substring(1, 5); String lon = latlon.substring(6);
/*  601 */     int latInt = -999999; int lonInt = -999999;
/*      */     try {
/*  603 */       latInt = Integer.parseInt(lat);
/*  604 */       lonInt = Integer.parseInt(lon);
/*      */     } catch (Exception e) {
/*  606 */       return false;
/*      */     }
/*      */ 
/*  609 */     if ((latInt < 0) || (latInt > 9000) || (lonInt < 0) || (lonInt > 18000)) {
/*  610 */       return false;
/*      */     }
/*  612 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isValidElev(String elev)
/*      */   {
/*  624 */     double elevInt = -999999.0D;
/*      */     try
/*      */     {
/*  627 */       elevInt = Double.parseDouble(elev);
/*      */     } catch (Exception e) {
/*  629 */       return false;
/*      */     }
/*      */ 
/*  637 */     if ((elevInt < -35840.0D) || (elevInt > 27889.0D)) {
/*  638 */       return false;
/*      */     }
/*  640 */     return true;
/*      */   }
/*      */ 
/*      */   public static Float getLatLonFromTxt(String latlon, boolean isLat)
/*      */   {
/*  653 */     String ns = latlon.substring(0, 1); String ew = latlon.substring(5, 6);
/*      */ 
/*  655 */     float nsDivider = ns.equalsIgnoreCase("N") ? 100 : -100;
/*  656 */     float ewDivider = ew.equalsIgnoreCase("E") ? 100 : -100;
/*      */ 
/*  658 */     String lat = latlon.substring(1, 5); String lon = latlon.substring(6);
/*  659 */     int latInt = -999999; int lonInt = -999999;
/*      */     try
/*      */     {
/*  662 */       latInt = Integer.parseInt(lat);
/*  663 */       lonInt = Integer.parseInt(lon);
/*      */     } catch (Exception e) {
/*  665 */       System.out.println("-----" + e.getMessage());
/*      */     }
/*      */ 
/*  668 */     return new Float(isLat ? latInt / nsDivider : lonInt / ewDivider);
/*      */   }
/*      */ 
/*      */   public static String getLatestAdvNo(String vol, String fFormat)
/*      */   {
/*  683 */     String dir = PgenUtil.getPgenActivityTextProdPath();
/*  684 */     File f = new File(dir);
/*      */ 
/*  687 */     String volFilePrefix = vol;
/*  688 */     final String fileExt = fFormat;
/*      */ 
/*  691 */     File[] files = f.listFiles(new FilenameFilter()
/*      */     {
/*      */       public boolean accept(File dir, String name)
/*      */       {
/*  700 */         String connector = "_";
/*      */ 
/*  702 */         if ((name == null) || (name.length() == 0))
/*  703 */           return false;
/*  704 */         if (!name.startsWith(VaaInfo.this))
/*  705 */           return false;
/*  706 */         if (!name.endsWith("." + fileExt))
/*  707 */           return false;
/*  708 */         if (!name.contains(connector)) {
/*  709 */           return false;
/*      */         }
/*  711 */         String[] f = name.split("\\." + fileExt)[0].split(connector);
/*  712 */         if (f.length < 3) {
/*  713 */           return false;
/*      */         }
/*  715 */         StringBuilder sb = new StringBuilder(VaaInfo.this);
/*  716 */         sb.append(connector).append(f[(f.length - 2)]);
/*  717 */         sb.append(connector).append(f[(f.length - 1)]);
/*  718 */         sb.append("." + fileExt);
/*      */ 
/*  720 */         if (!name.equals(sb.toString())) {
/*  721 */           return false;
/*      */         }
/*  723 */         String pDate = "[0-9]{8}"; String pTime = "[0-9]{4}";
/*      */ 
/*  725 */         return (Pattern.matches(pDate, f[(f.length - 2)])) && 
/*  726 */           (Pattern.matches(pTime, f[(f.length - 1)]));
/*      */       }
/*      */     });
/*  731 */     if ((files == null) || (files.length == 0)) {
/*  732 */       return "000";
/*      */     }
/*      */ 
/*  735 */     TreeMap dmap = new TreeMap();
/*      */ 
/*  737 */     for (File file : files)
/*      */     {
/*  740 */       String[] ftime = file.getName().split("\\." + fFormat)[0]
/*  741 */         .split("_");
/*  742 */       Date date = null;
/*      */       try {
/*  744 */         date = getTime(ftime[(ftime.length - 2)], ftime[(ftime.length - 1)]);
/*      */       } catch (NumberFormatException e) {
/*  746 */         System.out.println("---get time of the file failed: " + 
/*  747 */           e.getMessage());
/*      */       }
/*  749 */       if (date != null) {
/*  750 */         dmap.put(date, file);
/*      */       }
/*      */     }
/*      */ 
/*  754 */     File latestFile = (File)dmap.get(dmap.lastKey());
/*      */ 
/*  756 */     return "txt".equals(fileExt) ? getAdvNoFrmTxtFile(latestFile) : 
/*  757 */       getLatestAdvNoFrmXMLFile(latestFile);
/*      */   }
/*      */ 
/*      */   public static String getLatestAdvNo(String volname) {
/*  761 */     Volcano vol = getLatestProductFor(volname);
/*  762 */     if (vol == null)
/*  763 */       return "000";
/*  764 */     System.out.println("Got Volnum = " + vol.getAdvNum());
/*  765 */     return vol.getAdvNum();
/*      */   }
/*      */ 
/*      */   private static Volcano getLatestProductFor(String volname) {
/*  769 */     String dataURI = null;
/*  770 */     DbQueryRequest request = new DbQueryRequest();
/*  771 */     request.setEntityClass(PgenRecord.class.getName());
/*  772 */     request.addRequestField("dataURI");
/*  773 */     request.addConstraint("activityName", new RequestConstraint(
/*  774 */       volname, RequestConstraint.ConstraintType.EQUALS));
/*      */ 
/*  776 */     request.setOrderByField("dataTime.refTime", DbQueryRequest.OrderMode.ASC);
/*      */     try
/*      */     {
/*  780 */       DbQueryResponse response = (DbQueryResponse)ThriftClient.sendRequest(request);
/*  781 */       for (Map result : response.getResults()) {
/*  782 */         dataURI = (String)result.get("dataURI");
/*  783 */         System.out.println(dataURI);
/*      */       }
/*      */     } catch (Exception e) {
/*  786 */       StorageUtils.showError(e);
/*  787 */       return null;
/*      */     }
/*      */     DbQueryResponse response;
/*  790 */     if (dataURI == null) {
/*  791 */       return null;
/*      */     }
/*      */     try
/*      */     {
/*  795 */       prods = StorageUtils.retrieveProduct(dataURI);
/*      */     }
/*      */     catch (PgenStorageException e)
/*      */     {
/*      */       List prods;
/*  797 */       StorageUtils.showError(e);
/*  798 */       return null;
/*      */     }
/*      */     List prods;
/*      */     Iterator localIterator3;
/*  801 */     for (Iterator localIterator2 = prods.iterator(); localIterator2.hasNext(); 
/*  802 */       localIterator3.hasNext())
/*      */     {
/*  801 */       gov.noaa.nws.ncep.ui.pgen.elements.Product p = (gov.noaa.nws.ncep.ui.pgen.elements.Product)localIterator2.next();
/*  802 */       localIterator3 = p.getLayers().iterator(); continue; gov.noaa.nws.ncep.ui.pgen.elements.Layer l = (gov.noaa.nws.ncep.ui.pgen.elements.Layer)localIterator3.next();
/*  803 */       for (AbstractDrawableComponent adc : l.getDrawables()) {
/*  804 */         if ((adc instanceof Volcano)) {
/*  805 */           return (Volcano)adc;
/*      */         }
/*      */       }
/*      */     }
/*  809 */     return null;
/*      */   }
/*      */ 
/*      */   public static Date getTime(String date, String hourmin)
/*      */     throws NumberFormatException
/*      */   {
/*  821 */     return new Date(Integer.parseInt(date.substring(0, 4)), 
/*  822 */       Integer.parseInt(date.substring(4, 6)), Integer.parseInt(date
/*  823 */       .substring(6)), Integer.parseInt(hourmin
/*  824 */       .substring(0, 2)), Integer.parseInt(hourmin.substring(
/*  825 */       2, 4)));
/*      */   }
/*      */ 
/*      */   public static String getAdvNoFrmTxtFile(File f)
/*      */   {
/*  836 */     BufferedReader in = null;
/*      */     try {
/*  838 */       in = new BufferedReader(new FileReader(f));
/*      */     } catch (Exception e) {
/*  840 */       return "000";
/*      */     }
/*      */ 
/*  843 */     if (in == null) {
/*  844 */       return "000";
/*      */     }
/*  846 */     String line = null; String advNo = null;
/*      */     try
/*      */     {
/*  849 */       while ((line = in.readLine()) != null)
/*  850 */         if (line.contains("ADVISORY NR"))
/*  851 */           advNo = line.split("/")[1];
/*      */     }
/*      */     catch (Exception e) {
/*  854 */       return "000";
/*      */     }
/*      */ 
/*  857 */     return advNo == null ? "000" : advNo;
/*      */   }
/*      */ 
/*      */   public static String[] getFhrTimes(String date, String time)
/*      */   {
/*  871 */     int d = 0; int t = 0; int hour = 0; int min = 0;
/*  872 */     boolean wrongDate = false; boolean wrongTime = false;
/*      */ 
/*  875 */     if ((date == null) || (date.length() == 0)) {
/*  876 */       wrongDate = true;
/*      */     } else {
/*      */       try {
/*  879 */         d = Math.abs(Integer.parseInt(date));
/*      */       } catch (Exception e) {
/*  881 */         wrongDate = true;
/*      */       }
/*      */ 
/*  884 */       if ((d == 0) || (d > 31)) {
/*  885 */         wrongDate = true;
/*      */       }
/*      */     }
/*      */ 
/*  889 */     if ((time == null) || (time.length() == 0)) {
/*  890 */       wrongTime = true;
/*      */     } else {
/*      */       try {
/*  893 */         t = Math.abs(Integer.parseInt(time));
/*      */       } catch (Exception e) {
/*  895 */         wrongTime = true;
/*      */       }
/*      */ 
/*  898 */       if (t > 2359) {
/*  899 */         wrongTime = true;
/*      */       }
/*  901 */       hour = t / 100;
/*  902 */       min = t % 100;
/*      */ 
/*  905 */       if (hour == 0) {
/*  906 */         hour = min;
/*  907 */         min = 0;
/*      */       }
/*      */ 
/*  910 */       if (hour > 23) {
/*  911 */         wrongTime = true;
/*      */       }
/*      */     }
/*  914 */     SimpleDateFormat sdf = new SimpleDateFormat();
/*      */ 
/*  917 */     if ((wrongDate) || (wrongTime)) {
/*  918 */       sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
/*      */     }
/*  920 */     Calendar c = Calendar.getInstance();
/*      */ 
/*  923 */     if ((wrongDate) || (wrongTime)) {
/*  924 */       d = c.get(5);
/*  925 */       hour = c.get(11);
/*  926 */       min = c.get(12);
/*      */     }
/*      */ 
/*  930 */     if (min > 44) {
/*  931 */       hour++;
/*      */     }
/*      */ 
/*  934 */     sdf.applyPattern((min > 14) && (min < 45) ? "dd/HH30" : "dd/HH00");
/*      */ 
/*  936 */     c.set(5, d);
/*  937 */     c.set(11, hour);
/*      */ 
/*  940 */     Calendar c6 = (Calendar)c.clone(); Calendar c12 = (Calendar)c.clone();
/*  941 */     Calendar c18 = (Calendar)c
/*  941 */       .clone();
/*  942 */     c6.add(10, 6);
/*  943 */     c12.add(10, 12);
/*  944 */     c18.add(10, 18);
/*      */ 
/*  946 */     return new String[] { sdf.format(c6.getTime()) + "Z", 
/*  947 */       sdf.format(c12.getTime()) + "Z", 
/*  948 */       sdf.format(c18.getTime()) + "Z" };
/*      */   }
/*      */ 
/*      */   public static String[] getVaaEntryParts(String entry)
/*      */   {
/* 1028 */     String[] entryValue = { "", "" };
/*      */ 
/* 1030 */     int index = -1;
/*      */ 
/* 1032 */     index = entry.indexOf(">");
/*      */ 
/* 1034 */     if (index >= 0) {
/* 1035 */       entryValue[0] = entry.substring(0, index + 1).trim();
/* 1036 */       entryValue[1] = entry.substring(index + 1).trim();
/*      */     }
/*      */ 
/* 1039 */     return entryValue;
/*      */   }
/*      */ 
/*      */   public static void setVolcanoFields(Volcano vol, String prodType, boolean fromSelection)
/*      */   {
/* 1050 */     if ((vol == null) || (prodType == null) || (prodType.length() == 0)) {
/* 1051 */       return;
/*      */     }
/* 1053 */     ArrayList entries = ProductInfo.getEntry(prodType);
/* 1054 */     if (entries == null) {
/* 1055 */       return;
/*      */     }
/*      */ 
/* 1059 */     int index = -1;
/* 1060 */     String part = "";
/*      */ 
/* 1062 */     for (String line : entries)
/* 1063 */       if ((line != null) && (line.length() != 0))
/*      */       {
/* 1066 */         index = line.indexOf(">");
/* 1067 */         part = line.substring(index + 1);
/*      */ 
/* 1069 */         if ((part != null) && (part.length() != 0))
/*      */         {
/* 1076 */           String[] ev = getVaaEntryParts(line);
/* 1077 */           Method m = (Method)ENTRY_VOLSETTER_MAP.get(ev[0]);
/*      */           try
/*      */           {
/* 1081 */             if ((m != null) && (ev[1] != null))
/* 1082 */               m.invoke(vol, new Object[] { ev[1] });
/*      */           }
/*      */           catch (Exception e) {
/* 1085 */             System.out.println("--- java reflection method call failed: " + 
/* 1086 */               e.getMessage());
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private static void setVolFieldsFrmSelect(Volcano vol, String prodType, ArrayList<String> entries)
/*      */   {
/* 1108 */     int index = -1;
/* 1109 */     String part = "";
/* 1110 */     for (String line : entries)
/* 1111 */       if ((line != null) && (line.length() != 0))
/*      */       {
/* 1114 */         index = line.indexOf(">");
/* 1115 */         part = line.substring(index + 1).trim();
/*      */ 
/* 1119 */         if (line.contains("<INFORMATION SOURCE>")) {
/* 1120 */           vol.setInfoSource(part);
/*      */         }
/* 1122 */         if (line.contains("<ERUPTION DETAILS>")) {
/* 1123 */           vol.setErupDetails(part);
/*      */         }
/*      */ 
/* 1128 */         if (line.contains("<OBS ASH CLOUD>")) {
/* 1129 */           vol.setObsFcstAshCloudInfo(part);
/*      */         }
/* 1131 */         if (line.contains("<FCST ASH CLOUD +6H>")) {
/* 1132 */           vol.setObsFcstAshCloudInfo6(part);
/*      */         }
/* 1134 */         if (line.contains("<FCST ASH CLOUD +12H>")) {
/* 1135 */           vol.setObsFcstAshCloudInfo6(part);
/*      */         }
/* 1137 */         if (line.contains("<FCST ASH CLOUD +18H>")) {
/* 1138 */           vol.setObsFcstAshCloudInfo6(part);
/*      */         }
/* 1140 */         if (line.contains("<REMARKS>")) {
/* 1141 */           vol.setRemarks(part);
/*      */         }
/* 1143 */         if (line.contains("<NEXT ADVISORY>"))
/* 1144 */           vol.setNextAdv(part);
/*      */       }
/*      */   }
/*      */ 
/*      */   public static gov.noaa.nws.ncep.ui.pgen.file.Volcano getXMLVolFrmDrawableVol(Volcano dVol)
/*      */   {
/* 1160 */     return 
/* 1161 */       ProductConverter.convertVolcano2XML(dVol);
/*      */   }
/*      */ 
/*      */   public static String getTxtFromXML(DOMSource dSource, File xsltF)
/*      */   {
/* 1177 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*      */     try
/*      */     {
/* 1180 */       TransformerFactory tf = TransformerFactory.newInstance();
/* 1181 */       StreamSource myStylesheetSrc = new StreamSource(xsltF);
/*      */ 
/* 1183 */       Transformer t = tf.newTransformer(myStylesheetSrc);
/*      */ 
/* 1185 */       t.transform(dSource, new StreamResult(baos));
/*      */     }
/*      */     catch (Exception e) {
/* 1188 */       System.out.println(e.getMessage());
/*      */     }
/*      */ 
/* 1191 */     return new String(baos.toByteArray());
/*      */   }
/*      */ 
/*      */   public static DOMSource getVolcanoDOMSource(gov.noaa.nws.ncep.ui.pgen.file.Volcano fVol, String packageName)
/*      */   {
/* 1207 */     Document doc = null;
/*      */     try
/*      */     {
/* 1210 */       JAXBContext jc = JAXBContext.newInstance(packageName);
/* 1211 */       Marshaller m = jc.createMarshaller();
/*      */ 
/* 1213 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 1214 */       dbf.setNamespaceAware(true);
/* 1215 */       DocumentBuilder db = dbf.newDocumentBuilder();
/*      */ 
/* 1217 */       doc = db.newDocument();
/*      */ 
/* 1219 */       m.marshal(fVol, doc);
/*      */     }
/*      */     catch (Exception e) {
/* 1222 */       e.printStackTrace();
/*      */     }
/*      */ 
/* 1225 */     return new DOMSource(doc);
/*      */   }
/*      */ 
/*      */   public static boolean isNoneDrawableTxt(Products prods)
/*      */   {
/* 1240 */     boolean flag = false;
/*      */     Iterator localIterator2;
/* 1242 */     for (Iterator localIterator1 = prods.getProduct().iterator(); localIterator1.hasNext(); 
/* 1243 */       localIterator2.hasNext())
/*      */     {
/* 1242 */       gov.noaa.nws.ncep.ui.pgen.file.Product p = (gov.noaa.nws.ncep.ui.pgen.file.Product)localIterator1.next();
/* 1243 */       localIterator2 = p.getLayer().iterator(); continue; gov.noaa.nws.ncep.ui.pgen.file.Layer l = (gov.noaa.nws.ncep.ui.pgen.file.Layer)localIterator2.next();
/* 1244 */       if ((p.isOnOff() != null) && (l.isOnOff() != null) && (p.isOnOff().booleanValue()) && 
/* 1245 */         (l.isOnOff().booleanValue())) {
/* 1246 */         DrawableElement de = l
/* 1247 */           .getDrawableElement();
/*      */ 
/* 1249 */         List list = de
/* 1250 */           .getVolcano();
/* 1251 */         for (gov.noaa.nws.ncep.ui.pgen.file.Volcano v : list) {
/* 1252 */           String vp = v.getProduct() == null ? null : v
/* 1253 */             .getProduct().trim();
/* 1254 */           flag = Arrays.asList(ProductInfo.getProduct(LOCS[1]))
/* 1255 */             .contains(vp);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1261 */     return flag;
/*      */   }
/*      */ 
/*      */   public static void openMsgDlg(String msg)
/*      */   {
/* 1272 */     MessageDialog confirmDlg = new MessageDialog(
/* 1273 */       PlatformUI.getWorkbench()
/* 1274 */       .getActiveWorkbenchWindow().getShell(), "Warning", 
/* 1275 */       null, msg, 4, 
/* 1276 */       new String[] { "OK" }, 0);
/* 1277 */     confirmDlg.open();
/*      */   }
/*      */ 
/*      */   public static String getAshCloudInfo(String hour)
/*      */   {
/* 1288 */     StringBuilder sb = new StringBuilder();
/* 1289 */     List list = null;
/*      */ 
/* 1294 */     gov.noaa.nws.ncep.ui.pgen.elements.Product volProd = null;
/*      */ 
/* 1299 */     volProd = PgenSession.getInstance().getPgenResource()
/* 1300 */       .getActiveProduct();
/* 1301 */     List lyrList = volProd == null ? null : volProd.getLayers();
/* 1302 */     if (!"VOLCANO".equalsIgnoreCase(volProd.getName())) {
/* 1303 */       return sb.toString();
/*      */     }
/*      */ 
/* 1311 */     if (lyrList == null) {
/* 1312 */       return sb.toString();
/*      */     }
/* 1314 */     boolean isObsWithNotSeen = false;
/* 1315 */     ArrayList obsWithNotSeen = new ArrayList();
/*      */ 
/* 1317 */     for (gov.noaa.nws.ncep.ui.pgen.elements.Layer lyr : lyrList) {
/* 1318 */       list = lyr.getDrawables();
/*      */ 
/* 1320 */       for (AbstractDrawableComponent adc : list) {
/* 1321 */         if ("VACL_SIGMET".equals(adc.getPgenType())) {
/* 1322 */           Sigmet vac = (Sigmet)adc;
/*      */ 
/* 1325 */           String fhr = vac.getEditableAttrFreeText();
/* 1326 */           String fromLine = 
/* 1327 */             PgenUtil.getLatLonStringPrepend(vac.getLinePoints(), vac
/* 1328 */             .getType().contains("Area"));
/*      */ 
/* 1332 */           if (fhr == null) {
/* 1333 */             return sb.toString();
/*      */           }
/* 1335 */           if (fhr.substring(0, 3).contains(hour)) {
/* 1336 */             String txt = getParsedTxt(fhr, fromLine, vac.getType(), 
/* 1337 */               Integer.toString((int)vac.getWidth()));
/*      */ 
/* 1341 */             if ((!LAYERS[2].equals(hour)) && 
/* 1342 */               (!LAYERS[3].equals(hour)) && 
/* 1343 */               (!LAYERS[4].equals(hour)))
/*      */             {
/* 1345 */               obsWithNotSeen.add(txt);
/*      */ 
/* 1347 */               if (vac.getType() != null)
/*      */               {
/* 1349 */                 if (vac.getType().contains(
/* 1349 */                   "VA NOT IDENTIFIABLE FROM SATELLITE DATA"))
/* 1350 */                   isObsWithNotSeen = true;
/*      */               }
/*      */             }
/* 1353 */             sb.append(txt);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1358 */       if (isObsWithNotSeen) {
/* 1359 */         return (String)obsWithNotSeen.get(0);
/*      */       }
/*      */     }
/*      */ 
/* 1363 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   private static String getParsedTxt(String fhr, String fline, String type, String lineWidth)
/*      */   {
/* 1374 */     if ((type != null) && (type.contains("Text"))) {
/* 1375 */       return getFcstTxtFromTextType(type);
/*      */     }
/* 1377 */     String divider = " - ";
/*      */ 
/* 1379 */     String[] txtFhr = fhr.split(":::");
/* 1380 */     String[] txtFline = fline
/* 1380 */       .split(":::");
/*      */ 
/* 1382 */     StringBuilder sb = new StringBuilder();
/*      */ 
/* 1386 */     if ((txtFhr.length > 1) && (txtFhr[1].contains("SFC")))
/* 1387 */       sb.append(txtFhr[1]).append("/FL");
/*      */     else {
/* 1389 */       sb.append("FL").append(txtFhr[1]).append("/");
/*      */     }
/* 1391 */     if ((txtFhr.length >= 3) && (txtFhr[2] != null) && (txtFhr[2].length() != 0) && 
/* 1392 */       (!txtFhr[2].equals(" ")))
/*      */     {
/* 1395 */       sb.append(txtFhr[2]);
/*      */     }
/* 1397 */     sb.append(" ");
/*      */ 
/* 1399 */     if (type.contains("Line")) {
/* 1400 */       sb.append(lineWidth);
/* 1401 */       sb.append("NM WID LINE BTN ");
/*      */     }
/*      */ 
/* 1405 */     for (String s : txtFline) {
/* 1406 */       if (s.length() > 6) {
/* 1407 */         String wORe = s.substring(5, 6);
/* 1408 */         String replaced = s.replace(wORe, " " + wORe);
/* 1409 */         sb.append(replaced).append(divider);
/*      */       }
/*      */     }
/*      */ 
/* 1413 */     int last = sb.lastIndexOf("-");
/* 1414 */     if ((last > 0) && (last < sb.length())) {
/* 1415 */       sb.delete(last, last + 1);
/*      */     }
/*      */ 
/* 1419 */     if ((fhr.contains("F06")) || (fhr.contains("F12")) || (fhr.contains("F18"))) {
/* 1420 */       return " ";
/*      */     }
/*      */ 
/* 1424 */     sb.append("MOV").append(" ");
/*      */ 
/* 1426 */     if ((txtFhr.length >= 4) && (txtFhr[3] != null) && (txtFhr[3].length() != 0) && 
/* 1427 */       (!txtFhr[3].equals(" ")))
/*      */     {
/* 1430 */       sb.append(txtFhr[3]).append(" ");
/*      */     }
/* 1432 */     if ((txtFhr.length < 1) || (txtFhr[(txtFhr.length - 1)] == null) || 
/* 1433 */       (txtFhr[(txtFhr.length - 1)].length() == 0) || 
/* 1434 */       (txtFhr[(txtFhr.length - 1)].equals(" ")))
/* 1435 */       sb.append("0KT");
/*      */     else {
/* 1437 */       sb.append(txtFhr[(txtFhr.length - 1)]).append("KT");
/*      */     }
/* 1439 */     sb.append(" ");
/* 1440 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public static int getLayerIdx()
/*      */   {
/* 1449 */     gov.noaa.nws.ncep.ui.pgen.elements.Layer lyr = PgenSession.getInstance().getPgenResource()
/* 1450 */       .getActiveLayer();
/* 1451 */     for (int i = 0; i < LAYERS.length; i++) {
/* 1452 */       if (LAYERS[i].equalsIgnoreCase("OBS")) {
/* 1453 */         if ((LAYERS[i].equalsIgnoreCase(lyr.getName())) || 
/* 1454 */           (lyr.getName().equalsIgnoreCase("F00"))) {
/* 1455 */           return i;
/*      */         }
/*      */       }
/* 1458 */       else if (LAYERS[i].equalsIgnoreCase(lyr.getName())) {
/* 1459 */         return i;
/*      */       }
/*      */     }
/*      */ 
/* 1463 */     return CURRENT_LAYER_INDEX;
/*      */   }
/*      */ 
/*      */   public static void setLayerIdx(int current_layer_index) {
/* 1467 */     CURRENT_LAYER_INDEX = current_layer_index;
/*      */   }
/*      */ 
/*      */   public static boolean isNonDrawableVol(Volcano v)
/*      */   {
/* 1477 */     String vp = v.getProduct() == null ? null : v.getProduct().trim();
/* 1478 */     return Arrays.asList(ProductInfo.getProduct(LOCS[1])).contains(vp);
/*      */   }
/*      */ 
/*      */   public static String getLatestAdvNoFrmXMLFile(File f)
/*      */   {
/* 1489 */     String ano = null;
/*      */ 
/* 1491 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 1492 */     dbf.setNamespaceAware(true);
/*      */ 
/* 1494 */     DocumentBuilder db = null;
/*      */     try
/*      */     {
/* 1497 */       db = dbf.newDocumentBuilder();
/*      */     } catch (Exception e) {
/* 1499 */       System.out.println("---DocumentBuilder create failed: " + 
/* 1500 */         e.getMessage());
/*      */     }
/*      */ 
/* 1503 */     Document doc = null;
/*      */     try
/*      */     {
/* 1506 */       doc = db.parse(f);
/*      */     } catch (Exception e) {
/* 1508 */       System.out.println("---DocumentBuilder create failed: " + 
/* 1509 */         e.getMessage());
/*      */     }
/*      */ 
/* 1512 */     NodeList nList = doc.getElementsByTagName("Volcano");
/*      */ 
/* 1514 */     for (int i = 0; i < nList.getLength(); i++) {
/* 1515 */       Node n = nList.item(i);
/* 1516 */       NamedNodeMap nnMap = n.getAttributes();
/* 1517 */       Node advNo = nnMap.getNamedItem("advNum");
/* 1518 */       if (advNo != null) {
/* 1519 */         ano = advNo.getNodeValue();
/*      */       }
/*      */     }
/* 1522 */     return (ano == null) || (ano.length() == 0) ? "000" : ano;
/*      */   }
/*      */ 
/*      */   public static int getFcstItemIndexFromTxt(String txt)
/*      */   {
/* 1533 */     int index = 0;
/* 1534 */     if ((txt == null) || (txt.length() == 0)) {
/* 1535 */       return index;
/*      */     }
/* 1537 */     String[] values = 
/* 1538 */       (String[])VAA_INFO_OTHERSFCST_MAP
/* 1538 */       .get("display");
/*      */ 
/* 1540 */     String keyWord = "ASH";
/*      */ 
/* 1543 */     for (int i = 0; i < values.length; i++) {
/* 1544 */       if (values[i].equals(txt)) {
/* 1545 */         index = i;
/* 1546 */         break;
/*      */       }
/* 1548 */       if ((!values[i].equals(txt)) && (values[i].contains(keyWord)) && 
/* 1549 */         (txt.contains(keyWord)))
/*      */       {
/* 1551 */         if ((txt.substring(txt.indexOf(keyWord)).equals(
/* 1551 */           values[i].substring(values[i].indexOf(keyWord)))) && 
/* 1552 */           (txt.indexOf(keyWord) > 0) && 
/* 1553 */           (values[i].indexOf(keyWord) > 0)) {
/* 1554 */           index = i;
/*      */         }
/*      */       }
/*      */     }
/* 1558 */     return index;
/*      */   }
/*      */ 
/*      */   public static String getFcstTxtFromTextType(String type)
/*      */   {
/* 1570 */     String empty = ""; String oneSpace = " ";
/*      */ 
/* 1572 */     if ((type == null) || (type.length() == 0)) {
/* 1573 */       return empty;
/*      */     }
/*      */ 
/* 1576 */     String[] words = type.split(":::");
/* 1577 */     if (words.length < 2) {
/* 1578 */       return empty;
/*      */     }
/*      */ 
/* 1581 */     String disTxt = words[1];
/*      */ 
/* 1585 */     if ((disTxt != null) && (disTxt.contains("VA NOT IDENTIFIABLE FROM SATELLITE DATA"))) {
/* 1586 */       if (words.length < 3) {
/* 1587 */         return empty;
/*      */       }
/* 1589 */       return words[1] + oneSpace + words[2];
/*      */     }
/*      */ 
/* 1594 */     int index = getFcstItemIndexFromTxt(disTxt);
/* 1595 */     String[] texts = 
/* 1596 */       (String[])VAA_INFO_OTHERSFCST_MAP
/* 1596 */       .get("text");
/* 1597 */     if ((index < 0) || (index > texts.length - 1)) {
/* 1598 */       return empty;
/*      */     }
/*      */ 
/* 1601 */     String txt = texts[index];
/*      */ 
/* 1603 */     if (txt != null)
/*      */     {
/* 1605 */       if (txt.contains("SFC/FL")) {
/* 1606 */         return disTxt;
/*      */       }
/*      */ 
/* 1612 */       int in1 = txt.indexOf('{'); int in2 = txt.indexOf('}');
/*      */ 
/* 1615 */       String parsableIndex = "0";
/*      */ 
/* 1621 */       if ((in1 >= 0) && (in2 > in1) && (in2 <= txt.length() - 1))
/*      */       {
/* 1623 */         StringBuilder sb = new StringBuilder(txt.substring(0, in1));
/*      */ 
/* 1626 */         sb.append(disTxt.split(oneSpace)[0]);
/*      */ 
/* 1628 */         sb.append(txt.substring(in2 + 1));
/* 1629 */         sb.append(oneSpace);
/*      */ 
/* 1631 */         return sb.toString();
/*      */       }
/*      */ 
/* 1634 */       return txt + oneSpace;
/*      */     }
/*      */ 
/* 1638 */     return empty;
/*      */   }
/*      */ 
/*      */   public static boolean isNoneDrawableTxt(List<gov.noaa.nws.ncep.ui.pgen.elements.Product> prods)
/*      */   {
/*      */     Iterator localIterator2;
/* 1642 */     for (Iterator localIterator1 = prods.iterator(); localIterator1.hasNext(); 
/* 1643 */       localIterator2.hasNext())
/*      */     {
/* 1642 */       gov.noaa.nws.ncep.ui.pgen.elements.Product p = (gov.noaa.nws.ncep.ui.pgen.elements.Product)localIterator1.next();
/* 1643 */       localIterator2 = p.getLayers().iterator(); continue; gov.noaa.nws.ncep.ui.pgen.elements.Layer l = (gov.noaa.nws.ncep.ui.pgen.elements.Layer)localIterator2.next();
/* 1644 */       for (AbstractDrawableComponent adc : l.getDrawables()) {
/* 1645 */         if ((adc instanceof Volcano)) {
/* 1646 */           return isNonDrawableVol((Volcano)adc);
/*      */         }
/*      */       }
/*      */     }
/* 1650 */     return false;
/*      */   }
/*      */ 
/*      */   public static class ProductInfo
/*      */   {
/*  959 */     private static final Map<String, LinkedHashSet<String>> LOC_PROD_MAP = new HashMap();
/*      */ 
/*  961 */     private static final Map<String, ArrayList<String>> PROD_ENTRY_MAP = new HashMap();
/*      */ 
/*      */     private static void setLocProdEntry(String loc, String prod, String entry)
/*      */     {
/*  971 */       LinkedHashSet prods = (LinkedHashSet)LOC_PROD_MAP.get(loc);
/*      */ 
/*  973 */       if (prods == null) {
/*  974 */         LinkedHashSet prodSet = new LinkedHashSet();
/*  975 */         prodSet.add(prod);
/*  976 */         LOC_PROD_MAP.put(loc, prodSet);
/*      */       } else {
/*  978 */         prods.add(prod);
/*      */       }
/*      */ 
/*  983 */       ArrayList entries = (ArrayList)PROD_ENTRY_MAP.get(prod);
/*      */ 
/*  985 */       if (entries == null) {
/*  986 */         ArrayList entryList = new ArrayList();
/*  987 */         entryList.add(entry);
/*  988 */         PROD_ENTRY_MAP.put(prod, entryList);
/*      */       } else {
/*  990 */         entries.add(entry);
/*      */       }
/*      */     }
/*      */ 
/*      */     public static String[] getProduct(String loc)
/*      */     {
/* 1006 */       return (String[])((LinkedHashSet)LOC_PROD_MAP.get(loc)).toArray(new String[0]);
/*      */     }
/*      */ 
/*      */     public static ArrayList<String> getEntry(String prod)
/*      */     {
/* 1017 */       return (ArrayList)PROD_ENTRY_MAP.get(prod);
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo
 * JD-Core Version:    0.6.2
 */