/*      */ package gov.noaa.nws.ncep.ui.pgen;
/*      */ 
/*      */ import com.raytheon.uf.common.geospatial.MapUtil;
/*      */ import com.raytheon.uf.common.time.DataTime;
/*      */ import com.raytheon.uf.viz.core.IDisplayPane;
/*      */ import com.raytheon.uf.viz.core.drawables.IDescriptor;
/*      */ import com.raytheon.uf.viz.core.drawables.IDescriptor.FramesInfo;
/*      */ import com.raytheon.uf.viz.core.drawables.IRenderableDisplay;
/*      */ import com.raytheon.uf.viz.core.drawables.ResourcePair;
/*      */ import com.raytheon.uf.viz.core.localization.LocalizationManager;
/*      */ import com.raytheon.uf.viz.core.map.IMapDescriptor;
/*      */ import com.raytheon.uf.viz.core.map.MapDescriptor;
/*      */ import com.raytheon.uf.viz.core.rsc.AbstractVizResource;
/*      */ import com.raytheon.uf.viz.core.rsc.LoadProperties;
/*      */ import com.raytheon.uf.viz.core.rsc.ResourceList;
/*      */ import com.raytheon.viz.ui.EditorUtil;
/*      */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*      */ import com.raytheon.viz.ui.editor.EditorInput;
/*      */ import com.raytheon.viz.ui.editor.ISelectedPanesChangedListener;
/*      */ import com.raytheon.viz.ui.input.InputManager;
/*      */ import com.raytheon.viz.ui.panes.PaneManager;
/*      */ import com.vividsolutions.jts.geom.Coordinate;
/*      */ import com.vividsolutions.jts.geom.CoordinateList;
/*      */ import com.vividsolutions.jts.geom.Geometry;
/*      */ import com.vividsolutions.jts.geom.GeometryFactory;
/*      */ import com.vividsolutions.jts.geom.LineString;
/*      */ import com.vividsolutions.jts.geom.MultiPolygon;
/*      */ import com.vividsolutions.jts.geom.Point;
/*      */ import com.vividsolutions.jts.geom.Polygon;
/*      */ import com.vividsolutions.jts.linearref.LinearLocation;
/*      */ import com.vividsolutions.jts.linearref.LocationIndexedLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductInfo;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.WatchBox;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*      */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*      */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*      */ import gov.noaa.nws.ncep.ui.pgen.graphtogrid.CoordinateTransform;
/*      */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureDialog;
/*      */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*      */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResourceData;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Ccfp;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tca.TCAElement;
/*      */ import gov.noaa.nws.ncep.viz.common.display.INatlCntrsPaneManager;
/*      */ import gov.noaa.nws.ncep.viz.common.display.NcDisplayName;
/*      */ import gov.noaa.nws.ncep.viz.common.display.NcDisplayType;
/*      */ import java.awt.Color;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.PrintStream;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.TimeZone;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerFactory;
/*      */ import javax.xml.transform.dom.DOMSource;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import javax.xml.transform.stream.StreamSource;
/*      */ import org.eclipse.core.commands.Command;
/*      */ import org.eclipse.core.commands.ExecutionEvent;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.jface.preference.IPreferenceStore;
/*      */ import org.eclipse.swt.events.ModifyEvent;
/*      */ import org.eclipse.swt.events.ModifyListener;
/*      */ import org.eclipse.swt.events.VerifyEvent;
/*      */ import org.eclipse.swt.events.VerifyListener;
/*      */ import org.eclipse.swt.layout.FormAttachment;
/*      */ import org.eclipse.swt.layout.FormData;
/*      */ import org.eclipse.swt.layout.FormLayout;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Text;
/*      */ import org.eclipse.ui.IEditorInput;
/*      */ import org.eclipse.ui.IEditorPart;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchPart;
/*      */ import org.eclipse.ui.IWorkbenchPartSite;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ import org.eclipse.ui.commands.ICommandService;
/*      */ import org.geotools.geometry.jts.JTS;
/*      */ import org.opengis.referencing.crs.ProjectedCRS;
/*      */ import org.opengis.referencing.operation.MathTransform;
/*      */ 
/*      */ public class PgenUtil
/*      */ {
/*  166 */   private static PgenResourceData rscData = null;
/*      */ 
/*  172 */   private static CoordinateTransform coordTrans = null;
/*      */   public static final String VIEW_ID = "gov.noaa.nws.ncep.ui.PGEN";
/*      */   public static final float SM2M = 1609.34F;
/*      */   public static final float NM2M = 1852.0F;
/*      */   public static final String RECOVERY_PREFIX = "pgen_session.";
/*      */   public static final String RECOVERY_POSTFIX = ".tmp";
/*      */   public static final String PGEN_PROD_DIR = "prod";
/*      */   public static final String PGEN_XML_DIR = "xml";
/*      */   public static final String PGEN_TEXT_PROD_DIR = "text";
/*      */   public static final String FOUR_ZERO = "0000";
/*      */   public static final String FIVE_ZERO = "00000";
/*      */   public static final String CaveTitle = "CAVE";
/* 1006 */   public static String CURRENT_WORKING_DIRECTORY = System.getProperty("user.home");
/*      */ 
/*      */   public static final PgenResource findPgenResource(AbstractEditor editor)
/*      */   {
/*  214 */     return (PgenResource)findResource(PgenResource.class, editor);
/*      */   }
/*      */ 
/*      */   public static final PgenResource findPgenResourceInPane(IDisplayPane pane)
/*      */   {
/*  224 */     if (pane == null) return null;
/*      */ 
/*  226 */     ResourceList rscList = pane.getDescriptor().getResourceList();
/*      */ 
/*  228 */     for (ResourcePair rp : rscList) {
/*  229 */       AbstractVizResource rsc = rp.getResource();
/*      */ 
/*  231 */       if (rsc.getClass() == PgenResource.class) {
/*  232 */         return (PgenResource)rsc;
/*      */       }
/*      */     }
/*      */ 
/*  236 */     return null;
/*      */   }
/*      */ 
/*      */   public static final void setSelectingMode()
/*      */   {
/*  244 */     setCommandMode("gov.noaa.nws.ncep.ui.pgen.rsc.PgenSelect");
/*      */   }
/*      */ 
/*      */   public static final void setMultiSelectMode()
/*      */   {
/*  253 */     setCommandMode("gov.noaa.nws.ncep.ui.pgen.rsc.PgenMultiSelect");
/*      */   }
/*      */ 
/*      */   public static final void setDelObjMode()
/*      */   {
/*  261 */     setCommandMode("gov.noaa.nws.ncep.ui.pgen.rsc.PgenDeleteObj");
/*      */   }
/*      */ 
/*      */   private static final void setCommandMode(String command) {
/*  265 */     IEditorPart part = EditorUtil.getActiveEditor();
/*      */ 
/*  267 */     if (part == null) return;
/*      */ 
/*  269 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  270 */     Command cmd = service.getCommand(command);
/*      */ 
/*  272 */     if (cmd != null)
/*      */       try
/*      */       {
/*  275 */         HashMap params = new HashMap();
/*  276 */         params.put("editor", part);
/*  277 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, null, null);
/*      */ 
/*  279 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  283 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void setDrawingSymbolMode(String symbolCat, String symbolType, boolean usePrevColor, AbstractDrawableComponent adc)
/*      */   {
/*  296 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  297 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  298 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.rsc.PgenSingleDraw");
/*      */ 
/*  300 */     if (cmd != null)
/*      */       try
/*      */       {
/*  303 */         HashMap params = new HashMap();
/*  304 */         params.put("editor", part);
/*  305 */         params.put("name", symbolType);
/*  306 */         params.put("className", symbolCat);
/*  307 */         params.put("usePrevColor", new Boolean(usePrevColor).toString());
/*      */ 
/*  309 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, adc, null);
/*      */ 
/*  311 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  315 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void setDrawingTextMode(boolean addLabel, boolean usePrevColor, String defaultTxt, AbstractDrawableComponent adc)
/*      */   {
/*  328 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  329 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  330 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.rsc.PgenTextDraw");
/*      */ 
/*  332 */     if (cmd != null)
/*      */       try
/*      */       {
/*  335 */         HashMap params = new HashMap();
/*  336 */         params.put("editor", part);
/*  337 */         params.put("name", "General Text");
/*  338 */         params.put("className", "Text");
/*  339 */         params.put("addLabel", new Boolean(addLabel).toString());
/*  340 */         params.put("usePrevColor", new Boolean(usePrevColor).toString());
/*  341 */         params.put("defaultTxt", defaultTxt);
/*      */ 
/*  343 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, adc, null);
/*      */ 
/*  345 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  349 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void setDrawingGfaTextMode(Gfa lastUsedGfa)
/*      */   {
/*  360 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  361 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  362 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.rsc.PgenGfaDraw");
/*      */ 
/*  364 */     if (cmd != null)
/*      */       try
/*      */       {
/*  367 */         HashMap params = new HashMap();
/*  368 */         params.put("editor", part);
/*  369 */         params.put("name", "GFA");
/*  370 */         params.put("className", "MET");
/*  371 */         StringBuilder sb = new StringBuilder("");
/*      */ 
/*  373 */         for (String s : lastUsedGfa.getString()) {
/*  374 */           sb.append(s).append(",,");
/*      */         }
/*  376 */         if ((lastUsedGfa.getString() != null) && (lastUsedGfa.getString().length > 0)) {
/*  377 */           sb.setLength(sb.length() - 2);
/*      */         }
/*  379 */         params.put("startGfaText", "true");
/*  380 */         params.put("lastUsedGfa", lastUsedGfa);
/*      */ 
/*  382 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, null, null);
/*      */ 
/*  384 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  388 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void setDrawingStatusLineMode(WatchBox wb)
/*      */   {
/*  397 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  398 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  399 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.rsc.PgenWatchStatusLineDraw");
/*      */ 
/*  401 */     if (cmd != null)
/*      */       try
/*      */       {
/*  404 */         HashMap params = new HashMap();
/*  405 */         params.put("editor", part);
/*  406 */         params.put("name", "STATUS_LINE");
/*  407 */         params.put("className", "Watch");
/*      */ 
/*  409 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, wb, null);
/*      */ 
/*  411 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  415 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void setDrawingFrontMode(Line front)
/*      */   {
/*  424 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  425 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  426 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.rsc.PgenMultiDraw");
/*      */ 
/*  428 */     if (cmd != null)
/*      */       try
/*      */       {
/*  431 */         HashMap params = new HashMap();
/*  432 */         params.put("editor", part);
/*  433 */         params.put("name", front.getPgenType());
/*  434 */         params.put("className", front.getPgenCategory());
/*      */ 
/*  436 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, front, null);
/*      */ 
/*  438 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  442 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void loadOutlookSetContTool(Outlook otlk)
/*      */   {
/*  451 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  452 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  453 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.rsc.PgenOutlookSetCont");
/*      */ 
/*  455 */     if (cmd != null)
/*      */       try
/*      */       {
/*  458 */         HashMap params = new HashMap();
/*  459 */         params.put("editor", part);
/*  460 */         params.put("name", "Outlook");
/*  461 */         params.put("className", "");
/*      */ 
/*  463 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, otlk, null);
/*      */ 
/*  465 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  469 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void loadTCATool(DrawableElement de)
/*      */   {
/*  479 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  480 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  481 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.tca");
/*      */ 
/*  483 */     if ((cmd != null) && ((de instanceof TCAElement)))
/*      */       try
/*      */       {
/*  486 */         HashMap params = new HashMap();
/*  487 */         params.put("editor", part);
/*  488 */         params.put("name", de.getPgenType());
/*  489 */         params.put("className", de.getPgenCategory());
/*      */ 
/*  491 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, de, null);
/*      */ 
/*  493 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  497 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void loadWatchBoxModifyTool(DrawableElement de)
/*      */   {
/*  507 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  508 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  509 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.rsc.PgenWatchBoxModify");
/*      */ 
/*  511 */     if ((cmd != null) && ((de instanceof WatchBox)))
/*      */       try
/*      */       {
/*  514 */         HashMap params = new HashMap();
/*  515 */         params.put("editor", part);
/*  516 */         params.put("name", de.getPgenType());
/*  517 */         params.put("className", de.getPgenCategory());
/*      */ 
/*  519 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, de, null);
/*      */ 
/*  521 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  525 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void loadLabeledLineModifyTool(LabeledLine ll)
/*      */   {
/*  535 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  536 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  537 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.rsc.PgenLabeledLineModify");
/*      */ 
/*  539 */     if (cmd != null)
/*      */       try
/*      */       {
/*  542 */         HashMap params = new HashMap();
/*  543 */         params.put("editor", part);
/*  544 */         params.put("name", ll.getPgenType());
/*  545 */         params.put("className", ll.getPgenCategory());
/*      */ 
/*  547 */         if ("CCFP_SIGMET".equals(ll.getPgenType())) {
/*  548 */           params.put("type", ((Ccfp)ll).getSigmet().getType());
/*      */         }
/*      */ 
/*  551 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, ll, null);
/*      */ 
/*  553 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  557 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void loadTcmTool(DrawableElement elem)
/*      */   {
/*  566 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  567 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  568 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.rsc.PgenTCMtool");
/*      */ 
/*  570 */     if (cmd != null)
/*      */       try
/*      */       {
/*  573 */         HashMap params = new HashMap();
/*  574 */         params.put("editor", part);
/*  575 */         params.put("name", elem.getPgenType());
/*  576 */         params.put("className", elem.getPgenCategory());
/*      */ 
/*  578 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, elem, null);
/*      */ 
/*  580 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  584 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void loadOutlookDrawingTool()
/*      */   {
/*  592 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  593 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  594 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.outlookDraw");
/*      */ 
/*  596 */     if (cmd != null)
/*      */       try
/*      */       {
/*  599 */         HashMap params = new HashMap();
/*  600 */         params.put("editor", part);
/*  601 */         params.put("name", "Outlook");
/*  602 */         params.put("className", "");
/*      */ 
/*  604 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, null, null);
/*      */ 
/*  606 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  610 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final PgenResource createNewResource()
/*      */   {
/*  621 */     PgenResource drawingLayer = null;
/*      */ 
/*  623 */     AbstractEditor editor = getActiveEditor();
/*  624 */     if (editor != null) {
/*      */       try {
/*  626 */         switch ($SWITCH_TABLE$gov$noaa$nws$ncep$ui$pgen$PgenUtil$PgenMode()[getPgenMode().ordinal()])
/*      */         {
/*      */         case 1:
/*  632 */           if (rscData == null) {
/*  633 */             rscData = new PgenResourceData();
/*      */           }
/*  635 */           for (IDisplayPane pane : editor.getDisplayPanes()) {
/*  636 */             IDescriptor idesc = pane.getDescriptor();
/*  637 */             if (idesc.getResourceList().size() > 0) {
/*  638 */               drawingLayer = rscData.construct(new LoadProperties(), idesc);
/*      */ 
/*  640 */               idesc.getResourceList().add(drawingLayer);
/*  641 */               idesc.getResourceList().addPreRemoveListener(drawingLayer);
/*  642 */               drawingLayer.init(pane.getTarget());
/*      */             }
/*      */           }
/*  645 */           break;
/*      */         case 2:
/*  650 */           IMapDescriptor desc = (IMapDescriptor)editor.getActiveDisplayPane().getRenderableDisplay().getDescriptor();
/*  651 */           drawingLayer = new PgenResourceData().construct(new LoadProperties(), desc);
/*  652 */           desc.getResourceList().add(drawingLayer);
/*  653 */           desc.getResourceList().addPreRemoveListener(drawingLayer);
/*  654 */           drawingLayer.init(editor.getActiveDisplayPane().getTarget());
/*      */         }
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  659 */         e.printStackTrace();
/*      */       }
/*      */     }
/*  662 */     return drawingLayer;
/*      */   }
/*      */ 
/*      */   public static final void refresh()
/*      */   {
/*  671 */     if (getActiveEditor() != null)
/*  672 */       getActiveEditor().refresh();
/*      */   }
/*      */ 
/*      */   public static final boolean validateNumberTextField(Event event)
/*      */   {
/*  684 */     boolean valid = false;
/*      */ 
/*  689 */     if ((event.widget instanceof Text))
/*      */     {
/*  691 */       Text latLonText = (Text)event.widget;
/*  692 */       StringBuffer str = new StringBuffer(latLonText.getText());
/*      */ 
/*  696 */       if (event.keyCode != 0) {
/*  697 */         str.insert(event.start, event.text);
/*      */       }
/*      */       try
/*      */       {
/*  701 */         Double.valueOf(new String(str));
/*  702 */         valid = true;
/*      */       }
/*      */       catch (NumberFormatException e) {
/*  705 */         valid = false;
/*      */       }
/*      */ 
/*  711 */       if ((!valid) && (((event.start == 0) && ((event.character == '-') || (event.character == '.'))) || (
/*  712 */         (str.length() == 1) && ((str.charAt(0) == '-') || (str.charAt(0) == '.'))))) {
/*  713 */         valid = true;
/*      */       }
/*  715 */       else if ((valid) && (
/*  716 */         (event.character == 'd') || (event.character == 'D') || 
/*  717 */         (event.character == 'f') || (event.character == 'F') || 
/*  718 */         (event.character == 'e') || (event.character == 'E')))
/*      */       {
/*  723 */         valid = false;
/*      */       }
/*      */     }
/*      */ 
/*  727 */     return valid;
/*      */   }
/*      */ 
/*      */   public static boolean validatePositiveInteger(VerifyEvent ve)
/*      */   {
/*  733 */     boolean stat = false;
/*      */ 
/*  735 */     if ((ve.widget instanceof Text)) {
/*  736 */       Text advnum = (Text)ve.widget;
/*  737 */       StringBuffer str = new StringBuffer(advnum.getText());
/*  738 */       str.replace(ve.start, ve.end, ve.text);
/*      */ 
/*  740 */       if (str.toString().isEmpty()) return true;
/*      */       try
/*      */       {
/*  743 */         if (Integer.parseInt(str.toString()) > 0) return true; 
/*      */       }
/*      */       catch (NumberFormatException nfe)
/*      */       {
/*  746 */         return false;
/*      */       }
/*      */ 
/*  749 */       return false;
/*      */     }
/*      */ 
/*  752 */     return stat;
/*      */   }
/*      */ 
/*      */   public static final double[][] latlonToPixel(Coordinate[] pts, IMapDescriptor mapDescriptor)
/*      */   {
/*  761 */     double[] point = new double[3];
/*  762 */     double[][] pixels = new double[pts.length][3];
/*      */ 
/*  764 */     for (int i = 0; i < pts.length; i++) {
/*  765 */       point[0] = pts[i].x;
/*  766 */       point[1] = pts[i].y;
/*  767 */       point[2] = 0.0D;
/*  768 */       pixels[i] = mapDescriptor.worldToPixel(point);
/*      */     }
/*      */ 
/*  771 */     return pixels;
/*      */   }
/*      */ 
/*      */   public static String getCurrentOffice()
/*      */   {
/*  780 */     String wfo = LocalizationManager.getInstance().getCurrentSite();
/*  781 */     if ((wfo.equalsIgnoreCase("none")) || (wfo.isEmpty())) wfo = new String("KNHC");
/*      */ 
/*  783 */     return wfo;
/*      */   }
/*      */ 
/*      */   public static final String getLatLonStringPrepend(Coordinate[] coors, boolean isLineTypeArea)
/*      */   {
/*  792 */     String twoSpace = ":::";
/*  793 */     StringBuilder result = new StringBuilder();
/*  794 */     Coordinate[] arrayOfCoordinate = coors; int j = coors.length; for (int i = 0; i < j; i++) { Coordinate coor = arrayOfCoordinate[i];
/*      */ 
/*  796 */       result.append(coor.y >= 0.0D ? 'N' : 'S');
/*  797 */       int y = (int)Math.abs(coor.y * 100.0D);
/*  798 */       result.append(new DecimalFormat("0000").format(y));
/*      */ 
/*  800 */       result.append(coor.x >= 0.0D ? 'E' : 'W');
/*  801 */       int x = (int)Math.abs(coor.x * 100.0D);
/*  802 */       result.append(new DecimalFormat("00000").format(x));
/*      */ 
/*  804 */       result.append(twoSpace);
/*      */     }
/*  806 */     if (isLineTypeArea) result.append(result.toString().split(twoSpace)[0]);
/*  807 */     return result.toString();
/*      */   }
/*      */ 
/*      */   public static final String getLatLonStringPostpend(Coordinate[] coors, boolean isLineTypeArea)
/*      */   {
/*  816 */     String twoSpace = ":::";
/*  817 */     StringBuilder result = new StringBuilder();
/*  818 */     Coordinate[] arrayOfCoordinate = coors; int j = coors.length; for (int i = 0; i < j; i++) { Coordinate coor = arrayOfCoordinate[i];
/*      */ 
/*  820 */       int y = (int)Math.abs(coor.y * 100.0D);
/*  821 */       result.append(new DecimalFormat("0000").format(y));
/*  822 */       result.append(coor.y >= 0.0D ? 'N' : 'S');
/*      */ 
/*  824 */       int x = (int)Math.abs(coor.x * 100.0D);
/*  825 */       result.append(new DecimalFormat("00000").format(x));
/*  826 */       result.append(coor.x >= 0.0D ? 'E' : 'W');
/*      */ 
/*  828 */       result.append(twoSpace);
/*      */     }
/*  830 */     if (isLineTypeArea) result.append(result.toString().split(twoSpace)[0]);
/*  831 */     return result.toString();
/*      */   }
/*      */ 
/*      */   public static final void loadContoursTool(Contours de)
/*      */   {
/*  839 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  840 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  841 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.contours");
/*      */ 
/*  843 */     if (cmd != null)
/*      */       try
/*      */       {
/*  846 */         HashMap params = new HashMap();
/*  847 */         params.put("editor", part);
/*      */ 
/*  849 */         if (de != null) {
/*  850 */           params.put("name", de.getPgenType());
/*  851 */           params.put("className", de.getPgenCategory());
/*      */         }
/*      */         else {
/*  854 */           params.put("name", "Contours");
/*  855 */           params.put("className", "MET");
/*      */         }
/*      */ 
/*  858 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, de, null);
/*      */ 
/*  860 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  864 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static final void setSelectingMode(AbstractDrawableComponent de)
/*      */   {
/*  873 */     IEditorPart part = EditorUtil.getActiveEditor();
/*  874 */     ICommandService service = (ICommandService)part.getSite().getService(ICommandService.class);
/*  875 */     Command cmd = service.getCommand("gov.noaa.nws.ncep.ui.pgen.rsc.PgenSelect");
/*      */ 
/*  877 */     if (cmd != null)
/*      */       try
/*      */       {
/*  880 */         HashMap params = new HashMap();
/*  881 */         params.put("editor", part);
/*  882 */         ExecutionEvent exec = new ExecutionEvent(cmd, params, de, null);
/*      */ 
/*  884 */         cmd.executeWithChecks(exec);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  888 */         e.printStackTrace();
/*      */       }
/*      */   }
/*      */ 
/*      */   public static String getTempWorkDir()
/*      */   {
/*  898 */     IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
/*  899 */     return prefs.getString("PGEN_RECOVERY_DIR");
/*      */   }
/*      */ 
/*      */   public static PgenMode getPgenMode()
/*      */   {
/*  907 */     IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
/*  908 */     String name = prefs.getString("PGEN_MODE");
/*  909 */     PgenMode mode = PgenMode.valueOf(name);
/*  910 */     return mode;
/*      */   }
/*      */ 
/*      */   public static boolean doesLayerLink()
/*      */   {
/*  917 */     IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
/*  918 */     return prefs.getBoolean("PGEN_LAYER_LINK");
/*      */   }
/*      */ 
/*      */   public static boolean checkFileStatus(String filename)
/*      */   {
/*  927 */     boolean canWrite = false;
/*  928 */     File f = new File(filename);
/*      */ 
/*  930 */     if (f.exists())
/*      */     {
/*  932 */       String msg = "File " + filename + " already exists. Overwrite?";
/*  933 */       MessageDialog confirmDlg = new MessageDialog(
/*  934 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  935 */         "Confirm", null, msg, 
/*  936 */         3, new String[] { "OK", "Cancel" }, 0);
/*  937 */       confirmDlg.open();
/*      */ 
/*  939 */       if (confirmDlg.getReturnCode() == 0)
/*  940 */         canWrite = true;
/*      */     }
/*      */     else {
/*  943 */       canWrite = true;
/*      */     }
/*      */ 
/*  946 */     return canWrite;
/*      */   }
/*      */ 
/*      */   public static String calendarToGempakDattim(Calendar jdattim)
/*      */   {
/*  955 */     String gDattim = null;
/*      */ 
/*  957 */     if (jdattim != null)
/*      */     {
/*  959 */       String gstr = "";
/*      */ 
/*  961 */       int year = jdattim.get(1);
/*  962 */       gstr = gstr + (year - year / 100 * 100);
/*      */ 
/*  964 */       int month = jdattim.get(2) + 1;
/*  965 */       if (month < 10) {
/*  966 */         gstr = gstr + "0";
/*      */       }
/*  968 */       gstr = gstr + month;
/*      */ 
/*  970 */       int day = jdattim.get(5);
/*  971 */       if (day < 10) {
/*  972 */         gstr = gstr + "0";
/*      */       }
/*  974 */       gstr = gstr + day;
/*      */ 
/*  976 */       gstr = gstr + "/";
/*      */ 
/*  978 */       int hour = jdattim.get(11);
/*  979 */       if (hour < 10) {
/*  980 */         gstr = gstr + "0";
/*      */       }
/*  982 */       gstr = gstr + hour;
/*      */ 
/*  984 */       int minute = jdattim.get(12);
/*  985 */       if (minute < 10) {
/*  986 */         gstr = gstr + "0";
/*      */       }
/*  988 */       gstr = gstr + minute;
/*      */ 
/*  990 */       gDattim = new String(gstr);
/*      */     }
/*      */ 
/*  993 */     return gDattim;
/*      */   }
/*      */ 
/*      */   public static String getWorkingDirectory()
/*      */   {
/* 1009 */     return Activator.getDefault().getPreferenceStore().getString("PGEN_WORKING_DIR");
/*      */   }
/*      */ 
/*      */   public static LabeledLine mergeLabels(LabeledLine ll, gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Label testLbl, Coordinate loc, AbstractEditor mapEditor, PgenResource rsc)
/*      */   {
/* 1027 */     gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Label mergeLbl = null;
/*      */ 
/* 1030 */     double[] scnLoc = mapEditor.translateInverseClick(loc);
/*      */ 
/* 1033 */     Layer activeLayer = rsc.getActiveLayer();
/* 1034 */     Iterator it = activeLayer.getComponentIterator();
/* 1035 */     LabeledLine nearestLine = null;
/*      */     LabeledLine lline;
/* 1036 */     while (it.hasNext()) {
/* 1037 */       AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 1038 */       if (((adc instanceof LabeledLine)) && (((LabeledLine)adc).getPgenType().equalsIgnoreCase(ll.getPgenType()))) {
/* 1039 */         lline = (LabeledLine)adc;
/* 1040 */         for (gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.Label lbl : lline.getLabels())
/*      */         {
/*      */           Iterator iterator;
/* 1041 */           if ((Math.abs(lbl.getSpe().getLocation().x - loc.x) >= 0.0001D) || 
/* 1042 */             (Math.abs(lbl.getSpe().getLocation().y - loc.y) >= 0.0001D))
/*      */           {
/* 1050 */             double[] scnPt = mapEditor.translateInverseClick(lbl.getSpe().getLocation());
/* 1051 */             double dist = Math.sqrt((scnLoc[0] - scnPt[0]) * (scnLoc[0] - scnPt[0]) + 
/* 1052 */               (scnLoc[1] - scnPt[1]) * (scnLoc[1] - scnPt[1]));
/*      */ 
/* 1054 */             if (dist < 20.0D)
/*      */             {
/* 1056 */               mergeLbl = lbl;
/* 1057 */               nearestLine = (LabeledLine)adc;
/* 1058 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1063 */       if (mergeLbl != null) {
/*      */         break;
/*      */       }
/*      */     }
/* 1067 */     if ((testLbl != null) && (mergeLbl != null)) {
/* 1068 */       for (Line ln : testLbl.getArrows()) {
/* 1069 */         mergeLbl.addArrow(ln);
/* 1070 */         ln.removePoint(0);
/* 1071 */         ln.addPoint(0, mergeLbl.getSpe().getLocation());
/*      */       }
/*      */ 
/* 1074 */       ll.remove(testLbl);
/*      */     }
/*      */ 
/* 1077 */     if (nearestLine != null) {
/* 1078 */       iterator = nearestLine.getComponentIterator();
/* 1079 */       while (iterator.hasNext()) {
/* 1080 */         ll.add((AbstractDrawableComponent)iterator.next());
/*      */       }
/*      */     }
/*      */ 
/* 1084 */     return nearestLine;
/*      */   }
/*      */ 
/*      */   public static void resetResourceData()
/*      */   {
/* 1091 */     rscData = null;
/*      */   }
/*      */ 
/*      */   public static double getSphPolyArea(Coordinate[] ptsin)
/*      */   {
/* 1125 */     double HalfPi = 1.570796326794897D;
/* 1126 */     double Degree = 57.295779513082323D;
/* 1127 */     double M2NM = 0.000539999979082495D;
/* 1128 */     double RADIUS = 6371200.0D;
/* 1129 */     double GDIFFD = 1.0E-06D;
/*      */ 
/* 1137 */     int npts = ptsin.length;
/* 1138 */     double[] tmplat = new double[npts];
/* 1139 */     double[] tmplon = new double[npts];
/*      */ 
/* 1145 */     for (int jj = 0; jj < npts; jj++) {
/* 1146 */       tmplat[jj] = (ptsin[jj].y / 57.295779513082323D);
/* 1147 */       tmplon[jj] = (ptsin[jj].x / 57.295779513082323D);
/*      */     }
/*      */ 
/* 1154 */     double sum = 0.0D;
/* 1155 */     double Lam2 = 0.0D;
/* 1156 */     double Beta2 = 0.0D;
/* 1157 */     double CosB2 = 0.0D;
/* 1158 */     for (jj = 0; jj < npts; jj++)
/*      */     {
/* 1160 */       int kk = jj + 1;
/*      */       double Lam1;
/*      */       double Beta1;
/*      */       double CosB1;
/* 1161 */       if (jj == 0) {
/* 1162 */         double Lam1 = tmplon[jj]; double Beta1 = tmplat[jj];
/* 1163 */         Lam2 = tmplon[(jj + 1)]; Beta2 = tmplat[(jj + 1)];
/* 1164 */         double CosB1 = Math.cos(Beta1); CosB2 = Math.cos(Beta2);
/*      */       }
/*      */       else {
/* 1167 */         kk = (jj + 1) % npts;
/* 1168 */         Lam1 = Lam2; Beta1 = Beta2;
/* 1169 */         Lam2 = tmplon[kk]; Beta2 = tmplat[kk];
/* 1170 */         CosB1 = CosB2; CosB2 = Math.cos(Beta2);
/*      */       }
/*      */ 
/* 1173 */       if (Math.abs(Lam1 - Lam2) >= 1.0E-06D)
/*      */       {
/* 1175 */         double a = (1.0D - Math.cos(Beta2 - Beta1)) / 2.0D;
/* 1176 */         double b = (1.0D - Math.cos(Lam2 - Lam1)) / 2.0D;
/* 1177 */         double HavA = a + CosB1 * CosB2 * b;
/*      */ 
/* 1179 */         double A = 2.0D * Math.asin(Math.sqrt(HavA));
/* 1180 */         double B = 1.570796326794897D - Beta2;
/* 1181 */         double C = 1.570796326794897D - Beta1;
/* 1182 */         double S = 0.5D * (A + B + C);
/* 1183 */         double T = Math.tan(S / 2.0D) * Math.tan((S - A) / 2.0D) * Math.tan((S - B) / 2.0D) * Math.tan((S - C) / 2.0D);
/*      */ 
/* 1185 */         double excess = Math.abs(4.0D * Math.atan(Math.sqrt(Math.abs(T)))) * 57.295779513082323D;
/* 1186 */         if (Lam2 < Lam1) excess = -excess;
/*      */ 
/* 1188 */         sum += excess;
/*      */       }
/*      */     }
/*      */ 
/* 1192 */     double area = Math.abs(sum);
/*      */ 
/* 1197 */     double radius = 3440.447866730392D;
/*      */ 
/* 1199 */     area *= radius * radius / 57.295779513082323D;
/*      */ 
/* 1201 */     return area;
/*      */   }
/*      */ 
/*      */   public static double getSphPolyArea(Polygon poly)
/*      */   {
/* 1211 */     double extnl_area = getSphPolyArea(poly.getExteriorRing().getCoordinates());
/*      */ 
/* 1213 */     double intnl_area = 0.0D;
/* 1214 */     for (int nn = 0; nn < poly.getNumInteriorRing(); nn++) {
/* 1215 */       intnl_area += getSphPolyArea(poly.getInteriorRingN(nn).getCoordinates());
/* 1216 */       System.out.println("internal??");
/*      */     }
/*      */ 
/* 1219 */     return extnl_area - intnl_area;
/*      */   }
/*      */ 
/*      */   public static double getSphPolyArea(Geometry geom)
/*      */   {
/* 1229 */     double area = 0.0D;
/* 1230 */     if ((geom instanceof Polygon)) {
/* 1231 */       area = getSphPolyArea((Polygon)geom);
/*      */     }
/* 1233 */     else if ((geom instanceof MultiPolygon)) {
/* 1234 */       MultiPolygon mp = (MultiPolygon)geom;
/* 1235 */       for (int nn = 0; nn < mp.getNumGeometries(); nn++) {
/* 1236 */         area += getSphPolyArea((Polygon)mp.getGeometryN(nn));
/*      */       }
/*      */     }
/*      */ 
/* 1240 */     return area;
/*      */   }
/*      */ 
/*      */   public static double getSphPolyAreaInGrid(Polygon poly)
/*      */   {
/* 1254 */     Coordinate[] ring = poly.getExteriorRing().getCoordinates();
/* 1255 */     Coordinate[] ringInMap = gridToLatlon(ring);
/* 1256 */     double extnl_area = getSphPolyArea(ringInMap);
/*      */ 
/* 1258 */     double intnl_area = 0.0D;
/* 1259 */     for (int nn = 0; nn < poly.getNumInteriorRing(); nn++) {
/* 1260 */       ring = poly.getInteriorRingN(nn).getCoordinates();
/* 1261 */       ringInMap = gridToLatlon(ring);
/* 1262 */       intnl_area += getSphPolyArea(ring);
/*      */     }
/*      */ 
/* 1265 */     return extnl_area - intnl_area;
/*      */   }
/*      */ 
/*      */   public static double getSphPolyAreaInGrid(Geometry geom)
/*      */   {
/* 1275 */     double area = 0.0D;
/* 1276 */     if ((geom instanceof Polygon)) {
/* 1277 */       area = getSphPolyAreaInGrid((Polygon)geom);
/* 1278 */     } else if ((geom instanceof MultiPolygon)) {
/* 1279 */       MultiPolygon mp = (MultiPolygon)geom;
/* 1280 */       for (int nn = 0; nn < mp.getNumGeometries(); nn++) {
/* 1281 */         area += getSphPolyAreaInGrid((Polygon)mp.getGeometryN(nn));
/*      */       }
/*      */     }
/*      */ 
/* 1285 */     return area;
/*      */   }
/*      */ 
/*      */   public static double getPolyArea(Polygon poly)
/*      */   {
/* 1299 */     double area = 0.0D;
/*      */     try {
/* 1301 */       ProjectedCRS localProjectionCRS = 
/* 1302 */         MapUtil.constructStereographic(6371229.0D, 
/* 1303 */         6371229.0D, poly.getCentroid()
/* 1304 */         .getY(), poly.getCentroid().getX());
/*      */ 
/* 1306 */       MathTransform mt = MapUtil.getTransformFromLatLon(localProjectionCRS);
/* 1307 */       Geometry newIntersectInLocalProj = JTS.transform(poly, mt);
/* 1308 */       double areaInMeters = newIntersectInLocalProj.getArea();
/*      */ 
/* 1310 */       area = areaInMeters / 1852.0D / 1852.0D;
/*      */     }
/*      */     catch (Exception e) {
/* 1313 */       e.printStackTrace();
/*      */     }
/*      */ 
/* 1316 */     return area;
/*      */   }
/*      */ 
/*      */   public static String getPgenCompCoordProj()
/*      */   {
/* 1324 */     IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
/* 1325 */     String coordStr = prefs.getString("PGEN_COMP_COORD");
/* 1326 */     String[] s = coordStr.split("\\|");
/* 1327 */     return s[0];
/*      */   }
/*      */ 
/*      */   public static String getPgenCompCoordGarea()
/*      */   {
/* 1335 */     IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
/* 1336 */     String coordStr = prefs.getString("PGEN_COMP_COORD");
/* 1337 */     String[] s = coordStr.split("\\|");
/* 1338 */     return s[1];
/*      */   }
/*      */ 
/*      */   public static CoordinateTransform getCompCoord()
/*      */   {
/* 1347 */     if ((coordTrans == null) || 
/* 1348 */       (!coordTrans.getProjection().equals(getPgenCompCoordProj())) || 
/* 1349 */       (!coordTrans.getGarea().equals(getPgenCompCoordGarea())))
/*      */     {
/* 1351 */       coordTrans = new CoordinateTransform(getPgenCompCoordProj(), 
/* 1352 */         getPgenCompCoordGarea(), 800, 600);
/*      */     }
/*      */ 
/* 1355 */     return coordTrans;
/*      */   }
/*      */ 
/*      */   public static Coordinate[] latlonToGrid(Coordinate[] lonlat)
/*      */   {
/* 1368 */     return getCompCoord().worldToGrid(lonlat);
/*      */   }
/*      */ 
/*      */   public static Coordinate[] gridToLatlon(Coordinate[] gridpts)
/*      */   {
/* 1379 */     return getCompCoord().gridToWorld(gridpts);
/*      */   }
/*      */ 
/*      */   public static ArrayList<Coordinate> latlonToGrid(ArrayList<Coordinate> lonlat)
/*      */   {
/* 1390 */     Coordinate[] aa = new Coordinate[lonlat.size()];
/*      */ 
/* 1392 */     aa = latlonToGrid((Coordinate[])lonlat.toArray(aa));
/*      */ 
/* 1394 */     return new ArrayList(Arrays.asList(aa));
/*      */   }
/*      */ 
/*      */   public static ArrayList<Coordinate> gridToLatlon(ArrayList<Coordinate> gridpts)
/*      */   {
/* 1405 */     Coordinate[] aa = new Coordinate[gridpts.size()];
/*      */ 
/* 1407 */     aa = gridToLatlon((Coordinate[])gridpts.toArray(aa));
/*      */ 
/* 1409 */     return new ArrayList(Arrays.asList(aa));
/*      */   }
/*      */ 
/*      */   public static String getPgenOprDirectory()
/*      */   {
/* 1418 */     return Activator.getDefault().getPreferenceStore().getString("PGEN_BASE_DIR");
/*      */   }
/*      */ 
/*      */   public static String getPgenActivityPath()
/*      */   {
/* 1426 */     String pdName = PgenSession.getInstance().getPgenResource().getActiveProduct().getType();
/* 1427 */     ProductType pt = (ProductType)ProductConfigureDialog.getProductTypes().get(pdName);
/* 1428 */     if (pt != null) pdName = pt.getType();
/*      */ 
/* 1432 */     return getPgenOprDirectory() + File.separator + pdName;
/*      */   }
/*      */ 
/*      */   public static String getPgenActivityProdPath()
/*      */   {
/* 1441 */     return getPgenActivityPath() + File.separator + "prod";
/*      */   }
/*      */ 
/*      */   public static String getPgenActivityTextProdPath()
/*      */   {
/* 1451 */     return getPgenActivityProdPath() + File.separator + "text";
/*      */   }
/*      */ 
/*      */   public static String getPgenActivityXmlPath()
/*      */   {
/* 1459 */     return getPgenActivityPath() + File.separator + "xml";
/*      */   }
/*      */ 
/*      */   public static String formatDate(Calendar cal)
/*      */   {
/* 1467 */     StringBuilder dstr = new StringBuilder();
/*      */ 
/* 1469 */     int day = cal.get(5);
/* 1470 */     if (day < 10) {
/* 1471 */       dstr.append("0" + day);
/*      */     }
/*      */     else {
/* 1474 */       dstr.append(day);
/*      */     }
/*      */ 
/* 1477 */     int mon = cal.get(2) + 1;
/* 1478 */     if (mon < 10) {
/* 1479 */       dstr.append("0" + mon);
/*      */     }
/*      */     else {
/* 1482 */       dstr.append(mon);
/*      */     }
/*      */ 
/* 1485 */     dstr.append(cal.get(1));
/*      */ 
/* 1487 */     return dstr.toString();
/*      */   }
/*      */ 
/*      */   public static String parsePgenFileName(String fileName)
/*      */   {
/* 1498 */     String parsedFile = fileName;
/*      */ 
/* 1500 */     if ((fileName != null) && 
/* 1501 */       (fileName.contains(File.separator)))
/*      */     {
/* 1503 */       String[] items = fileName.split(File.separator);
/* 1504 */       StringBuilder stb = new StringBuilder();
/* 1505 */       if (fileName.startsWith(File.separator)) {
/* 1506 */         stb.append(File.separator);
/*      */       }
/*      */ 
/* 1509 */       for (String str : items) {
/* 1510 */         if (str.equals(".")) {
/* 1511 */           stb.append(System.getProperty("user.home"));
/*      */         }
/* 1513 */         else if ((str.startsWith("$")) && (System.getenv(str.substring(1)) != null)) {
/* 1514 */           stb.append(System.getenv(str.substring(1)));
/*      */         }
/*      */         else {
/* 1517 */           stb.append(str);
/*      */         }
/*      */ 
/* 1520 */         stb.append(File.separator);
/*      */       }
/*      */ 
/* 1523 */       parsedFile = stb.toString();
/*      */ 
/* 1525 */       if (!fileName.endsWith(File.separator)) {
/* 1526 */         parsedFile = parsedFile.substring(0, parsedFile.length() - 1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1535 */     return parsedFile;
/*      */   }
/*      */ 
/*      */   public static Coordinate computePoint(Coordinate coor, float dist, float dir)
/*      */   {
/* 1549 */     double PI = 3.14159265D;
/* 1550 */     double HALFPI = 1.570796325D;
/* 1551 */     double TWOPI = 6.2831853D;
/* 1552 */     double DTR = 0.0174532925D;
/* 1553 */     double RTD = 57.295779578552292D;
/* 1554 */     double RADIUS = 6371200.0D;
/* 1555 */     double NM2M = 1852.0D;
/*      */ 
/* 1561 */     double direction = dir * 0.0174532925D;
/* 1562 */     double lat = coor.y * 0.0174532925D;
/* 1563 */     double lon = coor.x * 0.0174532925D;
/* 1564 */     double distance = dist * 1852.0D / 6371200.0D;
/*      */ 
/* 1566 */     double dLat = Math.asin(Math.sin(lat) * Math.cos(distance) + Math.cos(lat) * Math.sin(distance) * Math.cos(direction));
/*      */ 
/* 1572 */     lon -= (int)(lon / 6.2831853D) * 6.2831853D;
/* 1573 */     if (lon < -3.14159265D) lon += 6.2831853D;
/* 1574 */     if (lon > 3.14159265D) lon -= 6.2831853D;
/*      */     double dLon;
/*      */     double dLon;
/* 1581 */     if ((Math.abs(lat - 90.0D) < 1.0E-06D) || (Math.abs(-lat - 90.0D) < 1.0E-06D)) {
/* 1582 */       dLon = lon;
/*      */     }
/*      */     else {
/* 1585 */       dLon = Math.atan2(Math.sin(direction) * Math.sin(distance) * Math.cos(lat), 
/* 1586 */         Math.cos(distance) - Math.sin(lat) * Math.sin(dLat));
/* 1587 */       dLon = lon + dLon + 3.14159265D - 3.14159265D;
/*      */     }
/*      */ 
/* 1594 */     double dLt = dLat - (int)(dLat / 3.14159265D) * 3.14159265D;
/*      */ 
/* 1596 */     if (dLt > 1.570796325D) {
/* 1597 */       dLt = 3.14159265D - dLt;
/* 1598 */       dLon = -dLon;
/*      */     }
/* 1600 */     if (dLt < -1.570796325D) {
/* 1601 */       dLt = -3.14159265D - dLt;
/* 1602 */       dLon = -dLon;
/*      */     }
/*      */ 
/* 1608 */     double dLn = dLon - (int)(dLon / 6.2831853D) * 6.2831853D;
/* 1609 */     if (dLn < -3.14159265D) dLn += 6.2831853D;
/* 1610 */     if (dLn > 3.14159265D) dLn -= 6.2831853D;
/*      */ 
/* 1613 */     Coordinate newCoor = new Coordinate((float)(dLn * 57.295779578552292D), (float)(dLt * 57.295779578552292D));
/* 1614 */     return newCoor;
/*      */   }
/*      */ 
/*      */   public static String applyStyleSheet(DOMSource dSource, String xsltName)
/*      */   {
/* 1625 */     String ret = "";
/* 1626 */     if ((xsltName != null) && (!xsltName.isEmpty())) {
/* 1627 */       File xslt = new File(xsltName);
/* 1628 */       if (xslt.canRead())
/*      */       {
/* 1630 */         ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*      */         try
/*      */         {
/* 1633 */           TransformerFactory tf = TransformerFactory.newInstance();
/* 1634 */           StreamSource myStylesheetSrc = new StreamSource(xslt);
/*      */ 
/* 1636 */           Transformer t = tf.newTransformer(myStylesheetSrc);
/*      */ 
/* 1638 */           t.transform(dSource, new StreamResult(baos));
/*      */ 
/* 1640 */           ret = new String(baos.toByteArray());
/*      */         } catch (Exception e) {
/* 1642 */           System.out.println(e.getMessage());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1647 */     return ret;
/*      */   }
/*      */ 
/*      */   public static String wrap(String str1, int wrapLength, String newLineStr, boolean wrapLongWords)
/*      */   {
/* 1662 */     if (str1 == null) {
/* 1663 */       return "";
/*      */     }
/* 1665 */     if (newLineStr == null) {
/* 1666 */       newLineStr = System.getProperty("line.separator");
/*      */     }
/*      */ 
/* 1669 */     if (wrapLength < 1) {
/* 1670 */       wrapLength = 1;
/*      */     }
/* 1672 */     int inputLineLength1 = str1.length();
/* 1673 */     int offset = 0;
/* 1674 */     StringBuffer wrappedLine = new StringBuffer(inputLineLength1 + 32);
/*      */ 
/* 1676 */     String[] lines = str1.split(newLineStr);
/*      */ 
/* 1678 */     for (int i = 0; i < lines.length; i++) {
/* 1679 */       offset = 0;
/* 1680 */       int inputLineLength = lines[i].length();
/*      */ 
/* 1682 */       if (lines[i].length() < wrapLength)
/*      */       {
/* 1684 */         wrappedLine.append(lines[i]).append(newLineStr);
/*      */       }
/*      */       else {
/* 1687 */         String str = lines[i];
/* 1688 */         while (inputLineLength - offset > wrapLength) {
/* 1689 */           if (str.charAt(offset) == ' ') {
/* 1690 */             offset++;
/*      */           }
/*      */           else {
/* 1693 */             int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);
/*      */ 
/* 1695 */             if (spaceToWrapAt >= offset)
/*      */             {
/* 1697 */               wrappedLine.append(str.substring(offset, spaceToWrapAt));
/* 1698 */               wrappedLine.append(newLineStr);
/* 1699 */               offset = spaceToWrapAt + 1;
/*      */             }
/* 1703 */             else if (wrapLongWords)
/*      */             {
/* 1705 */               wrappedLine.append(str.substring(offset, wrapLength + offset));
/* 1706 */               wrappedLine.append(newLineStr);
/* 1707 */               offset += wrapLength;
/*      */             }
/*      */             else {
/* 1710 */               spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
/* 1711 */               if (spaceToWrapAt >= 0) {
/* 1712 */                 wrappedLine.append(str.substring(offset, spaceToWrapAt));
/* 1713 */                 wrappedLine.append(newLineStr);
/* 1714 */                 offset = spaceToWrapAt + 1;
/*      */               } else {
/* 1716 */                 wrappedLine.append(str.substring(offset));
/* 1717 */                 offset = inputLineLength;
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1724 */         wrappedLine.append(str.substring(offset)).append(newLineStr);
/*      */       }
/*      */     }
/*      */ 
/* 1728 */     return wrappedLine.toString();
/*      */   }
/*      */ 
/*      */   public static final AbstractEditor getActiveEditor()
/*      */   {
/* 1740 */     if ((EditorUtil.getActiveEditor() instanceof AbstractEditor)) {
/* 1741 */       return (AbstractEditor)EditorUtil.getActiveEditor();
/*      */     }
/* 1743 */     return null;
/*      */   }
/*      */ 
/*      */   public static boolean isNatlCntrsEditor(IWorkbenchPart part)
/*      */   {
/* 1751 */     if ((part instanceof AbstractEditor))
/*      */     {
/* 1753 */       IEditorInput edInput = ((AbstractEditor)part).getEditorInput();
/*      */ 
/* 1755 */       if ((edInput instanceof EditorInput))
/*      */       {
/* 1757 */         PaneManager pmngr = ((EditorInput)edInput).getPaneManager();
/*      */ 
/* 1759 */         if ((pmngr instanceof INatlCntrsPaneManager))
/*      */         {
/* 1761 */           NcDisplayType dispType = ((INatlCntrsPaneManager)pmngr).getDisplayType();
/*      */ 
/* 1765 */           if (dispType.equals(NcDisplayType.NMAP_DISPLAY)) {
/* 1766 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1771 */     return false;
/*      */   }
/*      */ 
/*      */   public static final AbstractVizResource findResource(Class<? extends AbstractVizResource> rscClass, AbstractEditor aEdit)
/*      */   {
/* 1780 */     AbstractEditor editor = aEdit != null ? aEdit : getActiveEditor();
/* 1781 */     if (editor == null) {
/* 1782 */       return null;
/*      */     }
/* 1784 */     IRenderableDisplay disp = editor.getActiveDisplayPane()
/* 1785 */       .getRenderableDisplay();
/*      */ 
/* 1787 */     if (disp == null) {
/* 1788 */       return null;
/*      */     }
/* 1790 */     ResourceList rscList = disp.getDescriptor().getResourceList();
/*      */ 
/* 1792 */     for (ResourcePair rp : rscList) {
/* 1793 */       AbstractVizResource rsc = rp.getResource();
/*      */ 
/* 1795 */       if (rsc.getClass() == rscClass) {
/* 1796 */         return rsc;
/*      */       }
/*      */     }
/*      */ 
/* 1800 */     return null;
/*      */   }
/*      */ 
/*      */   public static final void setCaveTitle(String title)
/*      */   {
/* 1807 */     if ((PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) && 
/* 1808 */       (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
/* 1809 */       .getShell() != null))
/* 1810 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
/* 1811 */         .setText("CAVE:" + title);
/*      */   }
/*      */ 
/*      */   public static final void resetCaveTitle()
/*      */   {
/* 1820 */     PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
/* 1821 */       .setText("CAVE");
/*      */   }
/*      */ 
/*      */   public static NcDisplayName getDisplayName(AbstractEditor ed)
/*      */   {
/* 1830 */     if (!isNatlCntrsEditor(ed)) {
/* 1831 */       return null;
/*      */     }
/*      */ 
/* 1834 */     return ((INatlCntrsPaneManager)((EditorInput)ed.getEditorInput()).getPaneManager()).getDisplayName();
/*      */   }
/*      */ 
/*      */   public static int getNumberofPanes(AbstractEditor editor)
/*      */   {
/* 1843 */     return ((EditorInput)editor.getEditorInput()).getPaneManager().getNumberofPanes();
/*      */   }
/*      */ 
/*      */   public static void addSelectedPaneChangedListener(AbstractEditor editor, ISelectedPanesChangedListener listener)
/*      */   {
/* 1855 */     ((EditorInput)editor.getEditorInput()).getPaneManager().addSelectedPaneChangedListener(listener);
/*      */   }
/*      */ 
/*      */   public static void removeSelectedPaneChangedListener(AbstractEditor editor, ISelectedPanesChangedListener listener)
/*      */   {
/* 1867 */     ((EditorInput)editor.getEditorInput()).getPaneManager().removeSelectedPaneChangedListener(listener);
/*      */   }
/*      */ 
/*      */   public static String getCurrentFrameTime()
/*      */   {
/* 1880 */     return getFrameTime(getCurrentFrameCalendar());
/*      */   }
/*      */ 
/*      */   public static Calendar getCurrentFrameCalendar()
/*      */   {
/* 1891 */     Calendar cal = null;
/*      */ 
/* 1893 */     if (EditorUtil.getActiveEditor() != null)
/*      */     {
/* 1895 */       AbstractEditor absEditor = (AbstractEditor)EditorUtil.getActiveEditor();
/*      */ 
/* 1897 */       MapDescriptor mapDescriptor = (MapDescriptor)absEditor.getActiveDisplayPane().getRenderableDisplay().getDescriptor();
/*      */ 
/* 1899 */       if (mapDescriptor != null)
/*      */       {
/* 1901 */         int currentFrame = mapDescriptor.getFramesInfo().getFrameIndex();
/*      */ 
/* 1903 */         DataTime[] frameTimes = mapDescriptor.getFramesInfo().getFrameTimes();
/*      */ 
/* 1905 */         if ((frameTimes != null) && (currentFrame >= 0)) {
/* 1906 */           cal = frameTimes[currentFrame].getValidTime();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1911 */     return cal;
/*      */   }
/*      */ 
/*      */   public static String getFrameTime(Calendar cal)
/*      */   {
/* 1923 */     String frameTimeString = "";
/*      */ 
/* 1925 */     if (cal != null)
/*      */     {
/* 1927 */       SimpleDateFormat FRAME_DATE_FORMAT = new SimpleDateFormat("EEE yyMMdd/HHmm");
/* 1928 */       FRAME_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
/*      */ 
/* 1930 */       String fTimeString = new String(FRAME_DATE_FORMAT.format(cal.getTime()));
/* 1931 */       if ((fTimeString != null) && (fTimeString.trim().length() > 0)) {
/* 1932 */         String[] ftArrays = fTimeString.split(" ");
/* 1933 */         if (ftArrays.length > 1) {
/* 1934 */           frameTimeString = ftArrays[1];
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1939 */     return frameTimeString;
/*      */   }
/*      */ 
/*      */   public static Calendar getNextCalendar(Calendar calIn, String interval)
/*      */   {
/* 1953 */     Calendar nCal = null;
/*      */ 
/* 1955 */     if (calIn != null) {
/* 1956 */       long ftime = calIn.getTimeInMillis();
/* 1957 */       long ntime = 3600000L;
/* 1958 */       if (((interval != null ? 1 : 0) & (interval.trim().length() > 0 ? 1 : 0)) != 0) {
/* 1959 */         String[] intTimes = interval.split(":");
/* 1960 */         if (intTimes.length > 1) {
/* 1961 */           ntime = Integer.parseInt(intTimes[0]) * 60 * 60 * 1000 + 
/* 1962 */             Integer.parseInt(intTimes[1]) * 60 * 1000;
/*      */         }
/*      */         else {
/* 1965 */           ntime = Integer.parseInt(intTimes[0]) * 60 * 1000;
/*      */         }
/*      */       }
/*      */ 
/* 1969 */       nCal = Calendar.getInstance();
/* 1970 */       nCal.setTimeInMillis(ftime + ntime);
/*      */     }
/*      */ 
/* 1973 */     return nCal;
/*      */   }
/*      */ 
/*      */   public static Text setUTCTimeTextField(Composite parent, Text validTime, Calendar cal, Control topWidget, int offset)
/*      */   {
/* 1983 */     validTime.setTextLimit(4);
/* 1984 */     validTime.setText(getInitialTime(cal));
/* 1985 */     validTime.addVerifyListener(new VerifyListener()
/*      */     {
/*      */       public void verifyText(VerifyEvent ve)
/*      */       {
/* 1989 */         char BACKSPACE = '\b';
/* 1990 */         char DELETE = '';
/*      */ 
/* 1992 */         if ((Character.isDigit(ve.character)) || (ve.character == 0) || 
/* 1993 */           (ve.character == '\b') || (ve.character == '')) { ve.doit = true;
/*      */         } else {
/* 1995 */           ve.doit = false;
/* 1996 */           Display.getCurrent().beep();
/*      */         }
/*      */       }
/*      */     });
/* 2000 */     validTime.addModifyListener(new ModifyListener()
/*      */     {
/*      */       public void modifyText(ModifyEvent e)
/*      */       {
/* 2004 */         if (PgenUtil.isTimeValid(PgenUtil.this.getText()))
/* 2005 */           PgenUtil.this.setBackground(Display.getCurrent().getSystemColor(1));
/*      */         else
/* 2007 */           PgenUtil.this.setBackground(Display.getCurrent().getSystemColor(3));
/*      */       }
/*      */     });
/* 2012 */     org.eclipse.swt.widgets.Label utcLabel = new org.eclipse.swt.widgets.Label(parent, 0);
/* 2013 */     utcLabel.setText("UTC");
/* 2014 */     FormData fd = new FormData();
/* 2015 */     parent.setLayout(new FormLayout());
/* 2016 */     if (topWidget != null) fd.top = new FormAttachment(topWidget, offset, 1024);
/* 2017 */     fd.left = new FormAttachment(validTime, 5, 131072);
/* 2018 */     utcLabel.setLayoutData(fd);
/*      */ 
/* 2020 */     return validTime;
/*      */   }
/*      */ 
/*      */   public static boolean isTimeValid(String text)
/*      */   {
/* 2027 */     int time = Integer.parseInt(text);
/* 2028 */     int hour = time / 100;
/* 2029 */     int minute = time % 100;
/*      */ 
/* 2031 */     if ((hour >= 0) && (hour <= 23) && 
/* 2032 */       (minute >= 0) && (minute <= 59)) return true;
/*      */ 
/* 2034 */     return false;
/*      */   }
/*      */ 
/*      */   public static String getInitialTime(Calendar now)
/*      */   {
/* 2043 */     int minute = now.get(12);
/* 2044 */     if (minute >= 15) now.add(11, 1);
/* 2045 */     int hour = now.get(11);
/*      */ 
/* 2047 */     return String.format("%02d00", new Object[] { Integer.valueOf(hour) });
/*      */   }
/*      */ 
/*      */   public static void simulateMouseDown(int x, int y, int button, AbstractEditor mapEditor)
/*      */   {
/* 2058 */     Event me = new Event();
/* 2059 */     me.display = mapEditor.getActiveDisplayPane().getDisplay();
/* 2060 */     me.button = 1;
/* 2061 */     me.type = 3;
/* 2062 */     me.x = x;
/* 2063 */     me.y = y;
/* 2064 */     mapEditor.getMouseManager().handleEvent(me);
/*      */   }
/*      */ 
/*      */   public static boolean isUnmovable(DrawableElement tmpEl)
/*      */   {
/* 2074 */     if ((tmpEl instanceof Volcano)) {
/* 2075 */       return true;
/*      */     }
/* 2077 */     if ((tmpEl instanceof Sigmet)) {
/* 2078 */       Sigmet vaCloud = (Sigmet)tmpEl;
/* 2079 */       String type = vaCloud.getType();
/*      */ 
/* 2081 */       if ((type != null) && ((type.contains("WINDS")) || (type.contains("Isolated")))) {
/* 2082 */         return true;
/*      */       }
/*      */     }
/* 2085 */     return false;
/*      */   }
/*      */ 
/*      */   public static void writePgenFile(String path, String fname, String symName, Color symClr, double size, List<Coordinate> pts)
/*      */   {
/* 2095 */     Product activeProduct = new Product("Default", "Default", "Default", 
/* 2096 */       new ProductInfo(), new ProductTime(), new ArrayList());
/*      */ 
/* 2098 */     Layer activeLayer = new Layer();
/* 2099 */     activeProduct.addLayer(activeLayer);
/*      */ 
/* 2101 */     List productList = new ArrayList();
/* 2102 */     productList.add(activeProduct);
/*      */ 
/* 2104 */     for (Coordinate c : pts) {
/* 2105 */       Symbol cmm = new Symbol(null, new Color[] { symClr }, 
/* 2106 */         1.0F, size, Boolean.valueOf(false), c, "Symbol", symName);
/* 2107 */       activeLayer.add(cmm);
/*      */     }
/*      */ 
/* 2110 */     Products filePrds1 = ProductConverter.convert(productList);
/* 2111 */     String aa = path + "/" + fname;
/* 2112 */     FileTools.write(aa, filePrds1);
/*      */   }
/*      */ 
/*      */   public static List<ArrayList<Coordinate>> deleteLinePart(List<Coordinate> linePoints, boolean closed, Coordinate point1, Coordinate point2)
/*      */   {
/* 2126 */     ArrayList listOfNewLines = new ArrayList();
/*      */ 
/* 2133 */     GeometryFactory gf = new GeometryFactory();
/*      */ 
/* 2139 */     Coordinate[] lps = new Coordinate[linePoints.size()];
/* 2140 */     linePoints.toArray(lps);
/* 2141 */     CoordinateList clist = new CoordinateList(lps);
/* 2142 */     if (closed) clist.closeRing();
/* 2143 */     LineString ls = gf.createLineString(clist.toCoordinateArray());
/* 2144 */     LocationIndexedLine lil = new LocationIndexedLine(ls);
/* 2145 */     LinearLocation loc1 = lil.project(point1);
/* 2146 */     LinearLocation loc2 = lil.project(point2);
/*      */     Coordinate secondPt;
/*      */     LinearLocation firstLoc;
/*      */     LinearLocation secondLoc;
/*      */     Coordinate firstPt;
/*      */     Coordinate secondPt;
/* 2147 */     if (loc1.compareTo(loc2) <= 0) {
/* 2148 */       LinearLocation firstLoc = loc1;
/* 2149 */       LinearLocation secondLoc = loc2;
/* 2150 */       Coordinate firstPt = point1;
/* 2151 */       secondPt = point2;
/*      */     }
/*      */     else {
/* 2154 */       firstLoc = loc2;
/* 2155 */       secondLoc = loc1;
/* 2156 */       firstPt = point2;
/* 2157 */       secondPt = point1;
/*      */     }
/*      */ 
/* 2163 */     if (!closed) {
/* 2164 */       if ((lil.getStartIndex().compareTo(firstLoc) != 0) || 
/* 2165 */         (lil.getEndIndex().getSegmentIndex() != secondLoc.getSegmentIndex()))
/*      */       {
/* 2168 */         if ((lil.getStartIndex().compareTo(firstLoc) == 0) || 
/* 2169 */           (lil.getEndIndex().getSegmentIndex() == secondLoc.getSegmentIndex()))
/*      */         {
/* 2172 */           ArrayList newLine = new ArrayList();
/* 2173 */           if (lil.getStartIndex().compareTo(firstLoc) == 0) {
/* 2174 */             newLine.add(secondPt);
/* 2175 */             newLine.addAll(linePoints.subList(secondLoc.getSegmentIndex() + 1, linePoints.size()));
/*      */           }
/* 2177 */           else if (lil.getEndIndex().getSegmentIndex() == secondLoc.getSegmentIndex()) {
/* 2178 */             newLine.addAll(linePoints.subList(0, firstLoc.getSegmentIndex() + 1));
/* 2179 */             newLine.add(firstPt);
/*      */           }
/*      */ 
/* 2182 */           if (newLine.size() >= 2) listOfNewLines.add(newLine);
/*      */         }
/*      */         else
/*      */         {
/* 2186 */           ArrayList newLine1 = new ArrayList(linePoints.subList(0, firstLoc.getSegmentIndex() + 1));
/* 2187 */           newLine1.add(firstPt);
/*      */ 
/* 2189 */           ArrayList newLine2 = new ArrayList();
/* 2190 */           newLine2.add(secondPt);
/* 2191 */           newLine2.addAll(linePoints.subList(secondLoc.getSegmentIndex() + 1, linePoints.size()));
/*      */ 
/* 2193 */           if (newLine1.size() >= 2) listOfNewLines.add(newLine1);
/* 2194 */           if (newLine2.size() >= 2) listOfNewLines.add(newLine2); 
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2199 */       ArrayList newLine = new ArrayList();
/*      */ 
/* 2201 */       int pointsBetween = secondLoc.getSegmentIndex() - firstLoc.getSegmentIndex();
/*      */ 
/* 2203 */       if (pointsBetween > linePoints.size() - pointsBetween)
/*      */       {
/* 2205 */         newLine.add(firstPt);
/* 2206 */         newLine.addAll(linePoints.subList(firstLoc.getSegmentIndex() + 1, 
/* 2207 */           secondLoc.getSegmentIndex() + 1));
/* 2208 */         newLine.add(secondPt);
/*      */       }
/*      */       else {
/* 2211 */         newLine.add(secondPt);
/* 2212 */         newLine.addAll(linePoints.subList(secondLoc.getSegmentIndex() + 1, linePoints.size()));
/* 2213 */         newLine.addAll(linePoints.subList(0, firstLoc.getSegmentIndex() + 1));
/* 2214 */         newLine.add(firstPt);
/*      */       }
/*      */ 
/* 2217 */       listOfNewLines.add(newLine);
/*      */     }
/*      */ 
/* 2220 */     return listOfNewLines;
/*      */   }
/*      */ 
/*      */   public static enum PgenMode
/*      */   {
/*  164 */     SINGLE, MULTIPLE;
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.PgenUtil
 * JD-Core Version:    0.6.2
 */