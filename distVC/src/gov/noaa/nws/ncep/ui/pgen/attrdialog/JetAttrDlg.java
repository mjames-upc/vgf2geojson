/*      */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*      */ 
/*      */ import com.raytheon.uf.viz.core.exception.VizException;
/*      */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*      */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.SpinnerSlider;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IText;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IVector;
/*      */ import gov.noaa.nws.ncep.ui.pgen.display.IVector.VectorType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.IJetTools;
/*      */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.IJetBarb;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenJetDrawingTool;
/*      */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenSelectingTool;
/*      */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import org.eclipse.jface.window.Window;
/*      */ import org.eclipse.swt.graphics.Point;
/*      */ import org.eclipse.swt.graphics.RGB;
/*      */ import org.eclipse.swt.layout.GridData;
/*      */ import org.eclipse.swt.layout.GridLayout;
/*      */ import org.eclipse.swt.layout.RowLayout;
/*      */ import org.eclipse.swt.widgets.Button;
/*      */ import org.eclipse.swt.widgets.Combo;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Event;
/*      */ import org.eclipse.swt.widgets.Group;
/*      */ import org.eclipse.swt.widgets.Label;
/*      */ import org.eclipse.swt.widgets.Listener;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Slider;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ 
/*      */ public class JetAttrDlg extends LineAttrDlg
/*      */ {
/*   77 */   private static JetAttrDlg INSTANCE = null;
/*      */   private IJetBarb jetTool;
/*      */   private BarbAttrDlg barbAttrDlg;
/*      */   private HashAttrDlg hashAttrDlg;
/*      */   private FLAttrDlg flAttrDlg;
/*      */   private BarbDlg barbDlg;
/*      */   private Button addBarbBtn;
/*      */   private Button delBarbBtn;
/*      */   private Button addHashBtn;
/*      */   private Button delHashBtn;
/*      */   private Composite segPane;
/*      */   private Button barbAttrBtn;
/*      */   private Button hashAttrBtn;
/*      */   private Button flAttrBtn;
/*      */   private gov.noaa.nws.ncep.ui.pgen.elements.Vector barbTemplate;
/*      */   private gov.noaa.nws.ncep.ui.pgen.elements.Vector hashTemplate;
/*      */   private gov.noaa.nws.ncep.ui.pgen.elements.Text flTemplate;
/*      */ 
/*      */   private JetAttrDlg(Shell parShell)
/*      */     throws VizException
/*      */   {
/*  162 */     super(parShell);
/*  163 */     this.barbDlg = new BarbDlg(parShell, null);
/*      */   }
/*      */ 
/*      */   public static JetAttrDlg getInstance(Shell parShell)
/*      */   {
/*  176 */     if (INSTANCE == null)
/*      */     {
/*      */       try
/*      */       {
/*  180 */         INSTANCE = new JetAttrDlg(parShell);
/*      */       }
/*      */       catch (VizException e)
/*      */       {
/*  184 */         e.printStackTrace();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  189 */     return INSTANCE;
/*      */   }
/*      */ 
/*      */   protected void initializeComponents()
/*      */   {
/*  199 */     getShell().setText("Jet Attributes");
/*      */ 
/*  202 */     GridLayout mainLayout = new GridLayout(1, false);
/*  203 */     mainLayout.marginHeight = 3;
/*  204 */     mainLayout.marginWidth = 3;
/*  205 */     this.top.setLayout(mainLayout);
/*      */ 
/*  208 */     Composite pane1 = new Composite(this.top, 0);
/*  209 */     GridLayout layout1 = new GridLayout(2, false);
/*  210 */     layout1.marginHeight = 3;
/*  211 */     layout1.marginWidth = 3;
/*  212 */     pane1.setLayout(layout1);
/*      */ 
/*  214 */     Label colorLbl = new Label(pane1, 16384);
/*  215 */     colorLbl.setText("Color:");
/*      */ 
/*  219 */     this.cs = new ColorButtonSelector(pane1);
/*  220 */     this.cs.setColorValue(new RGB(0, 255, 0));
/*      */ 
/*  222 */     Label widthLbl = new Label(pane1, 16384);
/*  223 */     widthLbl.setText("Line Width:");
/*      */ 
/*  225 */     GridLayout gl = new GridLayout(2, false);
/*      */ 
/*  227 */     Group widthGrp = new Group(pane1, 0);
/*  228 */     widthGrp.setLayout(gl);
/*      */ 
/*  230 */     this.widthSpinnerSlider = 
/*  231 */       new SpinnerSlider(widthGrp, 256, 1);
/*  232 */     this.widthSpinnerSlider.setLayoutData(new GridData(180, 30));
/*  233 */     this.widthSpinnerSlider.setMinimum(1);
/*  234 */     this.widthSpinnerSlider.setMaximum(20);
/*  235 */     this.widthSpinnerSlider.setIncrement(1);
/*  236 */     this.widthSpinnerSlider.setPageIncrement(3);
/*  237 */     this.widthSpinnerSlider.setDigits(0);
/*      */ 
/*  241 */     this.patternSizeLbl = new Label(pane1, 16384);
/*  242 */     this.patternSizeLbl.setText("Pattern Size:");
/*      */ 
/*  244 */     Group psGrp = new Group(pane1, 0);
/*  245 */     psGrp.setLayout(gl);
/*      */ 
/*  247 */     this.patternSizeSpinnerSlider = 
/*  248 */       new SpinnerSlider(psGrp, 256, 1);
/*  249 */     this.patternSizeSpinnerSlider.setLayoutData(new GridData(180, 30));
/*  250 */     this.patternSizeSpinnerSlider.setMinimum(1);
/*  251 */     this.patternSizeSpinnerSlider.setMaximum(100);
/*  252 */     this.patternSizeSpinnerSlider.setIncrement(1);
/*  253 */     this.patternSizeSpinnerSlider.setPageIncrement(10);
/*  254 */     this.patternSizeSpinnerSlider.setDigits(1);
/*      */ 
/*  256 */     Label smoothLbl = new Label(pane1, 16384);
/*  257 */     smoothLbl.setText("Smooth Level:");
/*      */ 
/*  259 */     this.smoothLvlCbo = new Combo(pane1, 12);
/*      */ 
/*  261 */     this.smoothLvlCbo.add("0");
/*  262 */     this.smoothLvlCbo.add("1");
/*  263 */     this.smoothLvlCbo.add("2");
/*  264 */     this.smoothLvlCbo.select(2);
/*      */ 
/*  266 */     addSeparator(this.top);
/*      */ 
/*  269 */     Composite pane2 = new Composite(this.top, 0);
/*  270 */     GridLayout layout2 = new GridLayout(2, false);
/*  271 */     layout2.marginHeight = 3;
/*  272 */     layout2.marginWidth = 3;
/*  273 */     pane2.setLayout(layout2);
/*  274 */     GridData gd2 = new GridData(16777216, -1, true, false);
/*  275 */     pane2.setLayoutData(gd2);
/*      */ 
/*  277 */     this.addBarbBtn = new Button(pane2, 8);
/*  278 */     this.addBarbBtn.setText("Add Barb");
/*  279 */     this.delBarbBtn = new Button(pane2, 8);
/*  280 */     this.delBarbBtn.setText("Delete Barb");
/*      */ 
/*  284 */     this.addBarbBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*  288 */         JetAttrDlg.this.barbDlg.close();
/*  289 */         JetAttrDlg.this.barbDlg.open();
/*  290 */         JetAttrDlg.this.jetTool.setAddingBarbHandler();
/*      */ 
/*  293 */         if ((JetAttrDlg.this.jetTool instanceof PgenJetDrawingTool))
/*  294 */           ((PgenJetDrawingTool)JetAttrDlg.this.jetTool).setSelected();
/*      */       }
/*      */     });
/*  299 */     this.delBarbBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*  303 */         JetAttrDlg.this.jetTool.setDeletingBarbHandler();
/*  304 */         JetAttrDlg.this.closeBarbDlg();
/*      */       }
/*      */     });
/*  308 */     addSeparator(this.top);
/*      */ 
/*  311 */     Composite paneHash = new Composite(this.top, 0);
/*  312 */     GridLayout layoutHash = new GridLayout(2, false);
/*  313 */     layoutHash.marginHeight = 3;
/*  314 */     layoutHash.marginWidth = 3;
/*  315 */     paneHash.setLayout(layoutHash);
/*  316 */     GridData gdHash = new GridData(16777216, -1, true, false);
/*  317 */     paneHash.setLayoutData(gdHash);
/*      */ 
/*  319 */     this.addHashBtn = new Button(paneHash, 8);
/*  320 */     this.addHashBtn.setText("Add Hash");
/*  321 */     this.delHashBtn = new Button(paneHash, 8);
/*  322 */     this.delHashBtn.setText("Delete Hash");
/*      */ 
/*  324 */     enableBarbBtns(false);
/*      */ 
/*  326 */     this.addHashBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*  330 */         JetAttrDlg.this.barbDlg.close();
/*  331 */         JetAttrDlg.this.jetTool.setAddingHashHandler();
/*      */ 
/*  334 */         if ((JetAttrDlg.this.jetTool instanceof PgenJetDrawingTool))
/*  335 */           ((PgenJetDrawingTool)JetAttrDlg.this.jetTool).setSelected();
/*      */       }
/*      */     });
/*  340 */     this.delHashBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*  344 */         JetAttrDlg.this.jetTool.setDeletingHashHandler();
/*  345 */         JetAttrDlg.this.closeBarbDlg();
/*      */       }
/*      */     });
/*  349 */     this.segPane = new Composite(this.top, 0);
/*  350 */     GridLayout segGl = new GridLayout(15, false);
/*  351 */     this.segPane.setLayout(segGl);
/*  352 */     this.segPane.pack();
/*      */ 
/*  354 */     addSeparator(this.top);
/*      */ 
/*  356 */     Composite pane3 = new Composite(this.top, 0);
/*  357 */     GridLayout layout3 = new GridLayout(3, false);
/*  358 */     layout3.marginHeight = 3;
/*  359 */     layout3.marginWidth = 3;
/*  360 */     pane3.setLayout(layout3);
/*      */ 
/*  362 */     GridData gd3 = new GridData(16777216, -1, true, false);
/*  363 */     pane3.setLayoutData(gd3);
/*      */ 
/*  365 */     this.barbAttrBtn = new Button(pane3, 8);
/*  366 */     this.barbAttrBtn.setText("Barb Attr");
/*  367 */     this.hashAttrBtn = new Button(pane3, 8);
/*  368 */     this.hashAttrBtn.setText("Hash Attr");
/*  369 */     this.flAttrBtn = new Button(pane3, 8);
/*  370 */     this.flAttrBtn.setText("FL Attr");
/*      */ 
/*  372 */     addSeparator(this.top);
/*      */ 
/*  374 */     this.barbAttrBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */         try
/*      */         {
/*  380 */           if (JetAttrDlg.this.barbAttrDlg == null) {
/*  381 */             JetAttrDlg.this.barbAttrDlg = new JetAttrDlg.BarbAttrDlg(JetAttrDlg.this, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), null, null);
/*      */           }
/*  383 */           JetAttrDlg.this.openAttrDlg(JetAttrDlg.this.barbAttrDlg);
/*  384 */           JetAttrDlg.this.barbAttrDlg.initDlg();
/*  385 */           JetAttrDlg.this.barbAttrDlg.getShell().setText("Jet Barb Attributes");
/*      */ 
/*  388 */           if (JetAttrDlg.this.barbTemplate != null) {
/*  389 */             JetAttrDlg.this.barbAttrDlg.setAttrForDlg(JetAttrDlg.this.barbTemplate);
/*      */           }
/*      */           else
/*      */           {
/*  393 */             IAttribute barbAttr = JetAttrDlg.this.getBarbAttrFromSettings();
/*  394 */             if (barbAttr != null) {
/*  395 */               JetAttrDlg.this.barbAttrDlg.setAttrForDlg(barbAttr);
/*      */             }
/*      */           }
/*  398 */           JetAttrDlg.this.barbAttrDlg.disableWidgets();
/*      */         }
/*      */         catch (VizException e)
/*      */         {
/*  403 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*  409 */     this.hashAttrBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */         try {
/*  414 */           if (JetAttrDlg.this.hashAttrDlg == null) {
/*  415 */             JetAttrDlg.this.hashAttrDlg = new JetAttrDlg.HashAttrDlg(JetAttrDlg.this, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), null);
/*      */           }
/*  417 */           JetAttrDlg.this.openAttrDlg(JetAttrDlg.this.hashAttrDlg);
/*  418 */           JetAttrDlg.this.hashAttrDlg.initDlg();
/*  419 */           JetAttrDlg.this.hashAttrDlg.getShell().setText("Jet Hash Attributes");
/*      */ 
/*  422 */           if (JetAttrDlg.this.hashTemplate != null) {
/*  423 */             JetAttrDlg.this.hashAttrDlg.setAttrForDlg(JetAttrDlg.this.hashTemplate);
/*      */           }
/*      */           else
/*      */           {
/*  427 */             IAttribute hashAttr = JetAttrDlg.this.getHashAttrFromSettings();
/*  428 */             if (hashAttr != null) {
/*  429 */               JetAttrDlg.this.hashAttrDlg.setAttrForDlg(hashAttr);
/*      */             }
/*      */           }
/*      */ 
/*  433 */           JetAttrDlg.this.hashAttrDlg.disableWidgets();
/*      */         }
/*      */         catch (VizException e)
/*      */         {
/*  438 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*  443 */     this.flAttrBtn.addListener(3, new Listener()
/*      */     {
/*      */       public void handleEvent(Event event)
/*      */       {
/*      */         try {
/*  448 */           if (JetAttrDlg.this.flAttrDlg == null) {
/*  449 */             JetAttrDlg.this.flAttrDlg = new JetAttrDlg.FLAttrDlg(JetAttrDlg.this, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), null);
/*      */           }
/*  451 */           JetAttrDlg.this.openAttrDlg(JetAttrDlg.this.flAttrDlg);
/*  452 */           JetAttrDlg.FLAttrDlg.access$1(JetAttrDlg.this.flAttrDlg);
/*      */ 
/*  455 */           if (JetAttrDlg.this.flTemplate != null) {
/*  456 */             JetAttrDlg.this.flAttrDlg.setAttrForDlg(JetAttrDlg.this.flTemplate);
/*      */           }
/*      */           else
/*      */           {
/*  460 */             IAttribute flAttr = JetAttrDlg.this.getFlAttrFromSettings();
/*  461 */             if (flAttr != null) {
/*  462 */               JetAttrDlg.this.flAttrDlg.setAttrForDlg(flAttr);
/*      */             }
/*      */           }
/*      */ 
/*  466 */           JetAttrDlg.FLAttrDlg.access$2(JetAttrDlg.this.flAttrDlg);
/*      */         }
/*      */         catch (VizException e)
/*      */         {
/*  470 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void openAttrDlg(AttrDlg dlg)
/*      */   {
/*  482 */     dlg.setBlockOnOpen(false);
/*  483 */     dlg.setDrawingLayer(this.drawingLayer);
/*  484 */     dlg.setMapEditor(this.mapEditor);
/*  485 */     dlg.open();
/*  486 */     dlg.enableButtons();
/*      */   }
/*      */ 
/*      */   public void enableBarbBtns(boolean flag)
/*      */   {
/*  493 */     this.addBarbBtn.setEnabled(flag);
/*  494 */     this.delBarbBtn.setEnabled(flag);
/*  495 */     this.addHashBtn.setEnabled(flag);
/*  496 */     this.delHashBtn.setEnabled(flag);
/*      */   }
/*      */ 
/*      */   public void enableButtons()
/*      */   {
/*  504 */     super.enableButtons();
/*  505 */     enableBarbBtns(true);
/*      */   }
/*      */ 
/*      */   public boolean close()
/*      */   {
/*  514 */     if (this.barbAttrDlg != null) this.barbAttrDlg.close();
/*  515 */     if (this.hashAttrDlg != null) this.hashAttrDlg.close();
/*  516 */     if (this.flAttrDlg != null) this.flAttrDlg.close();
/*  517 */     this.jetTool = null;
/*  518 */     this.barbDlg.close();
/*  519 */     return super.close();
/*      */   }
/*      */ 
/*      */   public void closeBarbDlg()
/*      */   {
/*  528 */     if (this.barbDlg.getShell() != null) {
/*  529 */       this.barbDlg.close();
/*  530 */       if (this.barbAttrDlg != null) {
/*  531 */         this.barbAttrDlg.close();
/*      */       }
/*  533 */       if (this.flAttrDlg != null) {
/*  534 */         this.flAttrDlg.close();
/*      */       }
/*  536 */       if (this.hashAttrDlg != null) {
/*  537 */         this.hashAttrDlg.close();
/*      */       }
/*  539 */       if ((this.jetTool instanceof PgenJetDrawingTool))
/*  540 */         ((PgenJetDrawingTool)this.jetTool).deSelect();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setJetDrawingTool(IJetBarb tool)
/*      */   {
/*  550 */     this.jetTool = tool;
/*      */   }
/*      */ 
/*      */   public int getBarbSpeed()
/*      */   {
/*  559 */     int ret = 80;
/*      */     try {
/*  561 */       ret = Integer.valueOf(this.barbDlg.speedTxt.getText().trim()).intValue();
/*      */     }
/*      */     catch (Exception e) {
/*  564 */       setBarbSpeed(ret);
/*      */     }
/*      */ 
/*  567 */     if (ret < 80)
/*      */     {
/*  569 */       ret = 80;
/*  570 */       setBarbSpeed(ret);
/*      */     }
/*      */ 
/*  574 */     return ret;
/*      */   }
/*      */ 
/*      */   public void setBarbSpeed(int spd) {
/*  578 */     this.barbDlg.speedTxt.setText(String.valueOf(spd));
/*      */   }
/*      */ 
/*      */   public int getFlightLevel()
/*      */   {
/*  587 */     int ret = 300;
/*      */     try {
/*  589 */       ret = Integer.valueOf(this.barbDlg.levelTxt.getText().trim()).intValue();
/*      */     }
/*      */     catch (Exception e) {
/*  592 */       setFlightLevel(ret);
/*      */     }
/*      */ 
/*  595 */     if (ret < 100)
/*      */     {
/*  597 */       ret *= 10;
/*  598 */       setFlightLevel(ret);
/*      */     }
/*      */ 
/*  602 */     return ret;
/*      */   }
/*      */ 
/*      */   public void setFlightLevel(int level) {
/*  606 */     this.barbDlg.levelTxt.setText(String.valueOf(level));
/*      */   }
/*      */ 
/*      */   public String getFLDepth()
/*      */   {
/*  615 */     int top = 300;
/*  616 */     int bottom = 100;
/*      */     try
/*      */     {
/*  619 */       top = Integer.valueOf(this.barbDlg.topTxt.getText().trim()).intValue();
/*      */     }
/*      */     catch (Exception e) {
/*  622 */       this.barbDlg.topTxt.setText(String.valueOf(top));
/*      */     }
/*      */     try
/*      */     {
/*  626 */       bottom = Integer.valueOf(this.barbDlg.btmTxt.getText().trim()).intValue();
/*      */     }
/*      */     catch (Exception e) {
/*  629 */       this.barbDlg.btmTxt.setText(String.valueOf(bottom));
/*      */     }
/*      */ 
/*  632 */     if (top < 100) {
/*  633 */       top *= 10;
/*  634 */       this.barbDlg.topTxt.setText(String.valueOf(top));
/*      */     }
/*      */ 
/*  637 */     if (bottom < 100) {
/*  638 */       bottom *= 10;
/*  639 */       this.barbDlg.btmTxt.setText(String.valueOf(bottom));
/*      */     }
/*      */ 
/*  642 */     String flDepth = 
/*  643 */       this.barbDlg.topTxt.getText().trim() + "/" + 
/*  644 */       this.barbDlg.btmTxt.getText().trim();
/*  645 */     if (flDepth.length() == 1) flDepth = null;
/*  646 */     return flDepth;
/*      */   }
/*      */ 
/*      */   public void clearFLDepth()
/*      */   {
/*  653 */     this.barbDlg.topTxt.setText("");
/*  654 */     this.barbDlg.btmTxt.setText("");
/*      */   }
/*      */ 
/*      */   public IAttribute getBarbAttr()
/*      */   {
/*  663 */     if ((this.barbAttrDlg != null) && (this.barbAttrDlg.getShell() != null)) return this.barbAttrDlg;
/*  664 */     if (this.barbTemplate != null) return this.barbTemplate;
/*  665 */     return getBarbAttrFromSettings();
/*      */   }
/*      */ 
/*      */   public IAttribute getHashAttr()
/*      */   {
/*  674 */     if ((this.hashAttrDlg != null) && (this.hashAttrDlg.getShell() != null)) return this.hashAttrDlg;
/*  675 */     if (this.hashTemplate != null) return this.hashTemplate;
/*  676 */     return getHashAttrFromSettings();
/*      */   }
/*      */ 
/*      */   public IAttribute getFLAttr()
/*      */   {
/*  685 */     if ((this.flAttrDlg != null) && (this.flAttrDlg.getShell() != null)) return this.flAttrDlg;
/*  686 */     if (this.flTemplate != null) return this.flTemplate;
/*  687 */     return getFlAttrFromSettings();
/*      */   }
/*      */ 
/*      */   public Boolean isClosedLine()
/*      */   {
/*  926 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public Boolean isFilled()
/*      */   {
/*  933 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public java.awt.Color[] getColors()
/*      */   {
/*  942 */     java.awt.Color[] colors = new java.awt.Color[2];
/*      */ 
/*  944 */     colors[0] = new java.awt.Color(this.cs.getColorValue().red, 
/*  945 */       this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*      */ 
/*  947 */     colors[1] = java.awt.Color.green;
/*      */ 
/*  949 */     return colors;
/*      */   }
/*      */ 
/*      */   public void setColor(java.awt.Color[] clr)
/*      */   {
/*  959 */     this.cs.setColorValue(new RGB(clr[0].getRed(), clr[0].getGreen(), clr[0].getBlue()));
/*      */   }
/*      */ 
/*      */   public float getLineWidth()
/*      */   {
/*  967 */     return this.widthSpinnerSlider.getSelection();
/*      */   }
/*      */ 
/*      */   public int getSmoothFactor()
/*      */   {
/*  974 */     return this.smoothLvlCbo.getSelectionIndex();
/*      */   }
/*      */ 
/*      */   public void setAttr(AbstractDrawableComponent adc)
/*      */   {
/*  981 */     if ((adc instanceof Jet))
/*  982 */       super.setAttrForDlg(((Jet)adc).getJetLine());
/*      */   }
/*      */ 
/*      */   private IAttribute getHashAttrFromSettings()
/*      */   {
/*  992 */     AbstractDrawableComponent adc = (AbstractDrawableComponent)AttrSettings.getInstance().getSettings().get("JET");
/*  993 */     if ((adc != null) && ((adc instanceof Jet))) {
/*  994 */       Iterator it = ((Jet)adc).createDEIterator();
/*  995 */       while (it.hasNext()) {
/*  996 */         DrawableElement de = (DrawableElement)it.next();
/*  997 */         if (((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Vector)) && (((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).getVectorType() == IVector.VectorType.HASH_MARK)) {
/*  998 */           return de;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1003 */     return null;
/*      */   }
/*      */ 
/*      */   private IAttribute getBarbAttrFromSettings()
/*      */   {
/* 1012 */     AbstractDrawableComponent adc = (AbstractDrawableComponent)AttrSettings.getInstance().getSettings().get("JET");
/* 1013 */     if ((adc != null) && ((adc instanceof Jet))) {
/* 1014 */       Iterator it = ((Jet)adc).createDEIterator();
/* 1015 */       while (it.hasNext()) {
/* 1016 */         DrawableElement de = (DrawableElement)it.next();
/* 1017 */         if (((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Vector)) && (((gov.noaa.nws.ncep.ui.pgen.elements.Vector)de).getVectorType() == IVector.VectorType.WIND_BARB)) {
/* 1018 */           return de;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1023 */     return null;
/*      */   }
/*      */ 
/*      */   private IAttribute getFlAttrFromSettings()
/*      */   {
/* 1032 */     AbstractDrawableComponent adc = (AbstractDrawableComponent)AttrSettings.getInstance().getSettings().get("JET");
/* 1033 */     if ((adc != null) && ((adc instanceof Jet))) {
/* 1034 */       Iterator it = ((Jet)adc).createDEIterator();
/* 1035 */       while (it.hasNext()) {
/* 1036 */         DrawableElement de = (DrawableElement)it.next();
/* 1037 */         if ((de instanceof gov.noaa.nws.ncep.ui.pgen.elements.Text)) {
/* 1038 */           return de;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1043 */     return null;
/*      */   }
/*      */ 
/*      */   public double getSizeScale()
/*      */   {
/* 1050 */     return this.patternSizeSpinnerSlider.getSelection() / 10.0D;
/*      */   }
/*      */ 
/*      */   public void updateSegmentPane()
/*      */   {
/* 1059 */     clearSegPane();
/* 1060 */     if ((this.jetTool != null) && (this.jetTool.getJet().getSnapTool() != null))
/*      */     {
/* 1062 */       java.util.Vector seg = this.jetTool.getJet().getSnapTool().checkHashOnJet(this.jetTool.getJet());
/*      */ 
/* 1064 */       for (Iterator localIterator = seg.iterator(); localIterator.hasNext(); ) { int ii = ((Integer)localIterator.next()).intValue();
/* 1065 */         Label lbl = new Label(this.segPane, 0);
/* 1066 */         lbl.setText(String.valueOf(ii));
/* 1067 */         if (ii == 0)
/* 1068 */           lbl.setBackground(new org.eclipse.swt.graphics.Color(getShell().getDisplay(), new RGB(0, 255, 0)));
/*      */         else {
/* 1070 */           lbl.setBackground(new org.eclipse.swt.graphics.Color(getShell().getDisplay(), new RGB(255, 0, 0)));
/*      */         }
/*      */       }
/* 1073 */       this.segPane.pack(true);
/* 1074 */       this.segPane.layout();
/* 1075 */       this.top.pack();
/* 1076 */       this.top.layout();
/*      */ 
/* 1078 */       getShell().pack();
/* 1079 */       getShell().layout();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void clearSegPane()
/*      */   {
/* 1088 */     for (Control ctl : this.segPane.getChildren()) {
/* 1089 */       if ((ctl instanceof Label)) {
/* 1090 */         ctl.dispose();
/*      */       }
/*      */     }
/*      */ 
/* 1094 */     this.segPane.pack();
/* 1095 */     this.segPane.layout();
/*      */   }
/*      */ 
/*      */   public void updateBarbTemplate(IVector barb) {
/* 1099 */     if (this.barbTemplate == null) {
/* 1100 */       this.barbTemplate = ((gov.noaa.nws.ncep.ui.pgen.elements.Vector)new DrawableElementFactory().create(DrawableType.VECTOR, 
/* 1101 */         this, "Vector", "Barb", null, null));
/*      */     }
/* 1103 */     this.barbTemplate.update(barb);
/*      */ 
/* 1107 */     this.barbTemplate.setDirection(323.0D);
/*      */   }
/*      */ 
/*      */   public void updateFlTemplate(IText txt) {
/* 1111 */     if (this.flTemplate == null) {
/* 1112 */       this.flTemplate = ((gov.noaa.nws.ncep.ui.pgen.elements.Text)new DrawableElementFactory().create(DrawableType.TEXT, 
/* 1113 */         this, "Text", "General Text", null, null));
/*      */     }
/* 1115 */     this.flTemplate.update(txt);
/*      */ 
/* 1119 */     this.flTemplate.setRotation(289.0D);
/*      */   }
/*      */ 
/*      */   public void updateHashTemplate(IVector hash) {
/* 1123 */     if (this.hashTemplate == null) {
/* 1124 */       this.hashTemplate = ((gov.noaa.nws.ncep.ui.pgen.elements.Vector)new DrawableElementFactory().create(DrawableType.VECTOR, 
/* 1125 */         this, "Vector", "Hash", null, null));
/*      */     }
/* 1127 */     this.hashTemplate.update(hash);
/*      */ 
/* 1131 */     this.hashTemplate.setDirection(291.0D);
/*      */   }
/*      */ 
/*      */   private class BarbAttrDlg extends VectorAttrDlg
/*      */   {
/*      */     private BarbAttrDlg(Shell parShell)
/*      */       throws VizException
/*      */     {
/*  802 */       super();
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/*  808 */       JetAttrDlg.this.barbTemplate = ((gov.noaa.nws.ncep.ui.pgen.elements.Vector)new DrawableElementFactory().create(DrawableType.VECTOR, 
/*  809 */         this, "Vector", "Barb", null, null));
/*      */ 
/*  812 */       if ((JetAttrDlg.this.jetTool instanceof PgenSelectingTool)) {
/*  813 */         ((PgenSelectingTool)JetAttrDlg.this.jetTool).applyBarbAttrOnJet(this, "Barb");
/*      */       }
/*      */ 
/*  816 */       close();
/*      */     }
/*      */ 
/*      */     protected void disableWidgets()
/*      */     {
/*  823 */       this.spdLbl.setEnabled(false);
/*  824 */       this.spdSlider.setEnabled(false);
/*  825 */       this.spdText.setEnabled(false);
/*  826 */       this.dirLbl.setEnabled(false);
/*  827 */       this.dirSlider.setEnabled(false);
/*  828 */       this.dirText.setEnabled(false);
/*      */     }
/*      */ 
/*      */     protected void initDlg()
/*      */     {
/*  835 */       this.sizeText.setText("2");
/*  836 */       this.sizeSlider.setSelection(20);
/*  837 */       this.widthText.setText("2");
/*  838 */       this.widthSlider.setSelection(2);
/*  839 */       this.dirText.setText("270");
/*  840 */       this.dirSlider.setSelection(270);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class BarbDlg extends Window
/*      */   {
/*      */     private org.eclipse.swt.widgets.Text speedTxt;
/*      */     private org.eclipse.swt.widgets.Text levelTxt;
/*      */     private org.eclipse.swt.widgets.Text topTxt;
/*      */     private org.eclipse.swt.widgets.Text btmTxt;
/*      */ 
/*      */     private BarbDlg(Shell parentShell)
/*      */     {
/*  703 */       super();
/*      */     }
/*      */ 
/*      */     protected Control createContents(Composite parent)
/*      */     {
/*  708 */       getShell().setText("Barb Info");
/*      */ 
/*  711 */       GridLayout gl = new GridLayout(3, false);
/*  712 */       gl.marginHeight = 3;
/*  713 */       gl.marginWidth = 3;
/*  714 */       parent.setLayout(gl);
/*      */ 
/*  717 */       Label speedLbl = new Label(parent, 16777216);
/*  718 */       speedLbl.setText("Speed");
/*  719 */       this.speedTxt = new org.eclipse.swt.widgets.Text(parent, 2048);
/*  720 */       this.speedTxt.setText("  100");
/*  721 */       this.speedTxt.addListener(25, new Listener()
/*      */       {
/*      */         public void handleEvent(Event e) {
/*  724 */           e.doit = PgenUtil.validateNumberTextField(e);
/*      */         }
/*      */       });
/*  728 */       Label speedUnit = new Label(parent, 16777216);
/*  729 */       speedUnit.setText("KTS");
/*      */ 
/*  732 */       Label levelLbl = new Label(parent, 16777216);
/*  733 */       levelLbl.setText("Level");
/*  734 */       this.levelTxt = new org.eclipse.swt.widgets.Text(parent, 2048);
/*  735 */       this.levelTxt.setText("  300");
/*  736 */       this.levelTxt.addListener(25, new Listener()
/*      */       {
/*      */         public void handleEvent(Event e) {
/*  739 */           e.doit = PgenUtil.validateNumberTextField(e);
/*      */         }
/*      */       });
/*  742 */       Label levelUnit = new Label(parent, 16777216);
/*  743 */       levelUnit.setText("100 ft");
/*      */ 
/*  746 */       Label topLbl = new Label(parent, 16777216);
/*  747 */       topLbl.setText("Top/Bottom");
/*  748 */       this.topTxt = new org.eclipse.swt.widgets.Text(parent, 2048);
/*  749 */       this.topTxt.addListener(25, new Listener()
/*      */       {
/*      */         public void handleEvent(Event e) {
/*  752 */           e.doit = PgenUtil.validateNumberTextField(e);
/*      */         }
/*      */       });
/*  755 */       Composite btm = new Composite(parent, 0);
/*  756 */       btm.setLayout(new RowLayout(256));
/*      */ 
/*  758 */       Label slashLbl = new Label(btm, 0);
/*  759 */       slashLbl.setText("/");
/*  760 */       this.btmTxt = new org.eclipse.swt.widgets.Text(btm, 2048);
/*  761 */       this.btmTxt.addListener(25, new Listener()
/*      */       {
/*      */         public void handleEvent(Event e) {
/*  764 */           e.doit = PgenUtil.validateNumberTextField(e);
/*      */         }
/*      */       });
/*  768 */       return parent;
/*      */     }
/*      */ 
/*      */     public int open()
/*      */     {
/*  774 */       create();
/*      */ 
/*  776 */       setBlockOnOpen(false);
/*      */ 
/*  778 */       Point loc = JetAttrDlg.INSTANCE.getShell().getLocation();
/*  779 */       getShell().setLocation(loc.x, loc.y + JetAttrDlg.INSTANCE.getShell().getSize().y);
/*      */ 
/*  781 */       return super.open();
/*      */     }
/*      */ 
/*      */     public boolean close()
/*      */     {
/*  788 */       if (JetAttrDlg.this.jetTool != null) JetAttrDlg.this.jetTool.resetMouseHandler();
/*  789 */       return super.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FLAttrDlg extends TextAttrDlg
/*      */   {
/*      */     private FLAttrDlg(Shell parShell)
/*      */       throws VizException
/*      */     {
/*  881 */       super();
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/*  887 */       JetAttrDlg.this.flTemplate = ((gov.noaa.nws.ncep.ui.pgen.elements.Text)new DrawableElementFactory().create(DrawableType.TEXT, 
/*  888 */         this, "Text", "General Text", null, null));
/*      */ 
/*  891 */       if ((JetAttrDlg.this.jetTool instanceof PgenSelectingTool)) {
/*  892 */         ((PgenSelectingTool)JetAttrDlg.this.jetTool).applyFLAttrOnJet(this);
/*      */       }
/*      */ 
/*  895 */       close();
/*      */     }
/*      */ 
/*      */     private void disableWidgets()
/*      */     {
/*  902 */       this.text.setEnabled(false);
/*  903 */       this.textLabel.setEnabled(false);
/*  904 */       this.rotLbl.setEnabled(false);
/*  905 */       this.rotSlider.setEnabled(false);
/*  906 */       this.rotText.setEnabled(false);
/*  907 */       this.screenBtn.setEnabled(false);
/*  908 */       this.northBtn.setEnabled(false);
/*      */     }
/*      */ 
/*      */     private void initDlg()
/*      */     {
/*  916 */       getShell().setText("Jet Flight Level Attributes");
/*  917 */       this.sizeCombo.select(3);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class HashAttrDlg extends JetAttrDlg.BarbAttrDlg
/*      */   {
/*      */     private HashAttrDlg(Shell parShell)
/*      */       throws VizException
/*      */     {
/*  854 */       super(parShell, null);
/*      */     }
/*      */ 
/*      */     public void okPressed()
/*      */     {
/*  860 */       JetAttrDlg.this.hashTemplate = ((gov.noaa.nws.ncep.ui.pgen.elements.Vector)new DrawableElementFactory().create(DrawableType.VECTOR, 
/*  861 */         this, "Vector", "Hash", null, null));
/*      */ 
/*  864 */       if ((JetAttrDlg.this.jetTool instanceof PgenSelectingTool)) {
/*  865 */         ((PgenSelectingTool)JetAttrDlg.this.jetTool).applyBarbAttrOnJet(this, "Hash");
/*      */       }
/*      */ 
/*  868 */       close();
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.JetAttrDlg
 * JD-Core Version:    0.6.2
 */