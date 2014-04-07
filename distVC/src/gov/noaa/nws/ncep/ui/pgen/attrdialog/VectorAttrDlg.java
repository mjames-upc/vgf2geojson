/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IVector.VectorType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Vector;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.swt.events.KeyAdapter;
/*     */ import org.eclipse.swt.events.KeyEvent;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.events.SelectionListener;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Slider;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class VectorAttrDlg extends AttrDlg
/*     */   implements IVector
/*     */ {
/*  66 */   static VectorAttrDlg INSTANCE = null;
/*     */ 
/*  68 */   private Composite top = null;
/*     */   private Label colorLbl;
/*  71 */   private ColorButtonSelector cs = null;
/*     */   private Label clearLbl;
/*  74 */   private Button clearBtn1 = null;
/*  75 */   private Button clearBtn2 = null;
/*     */   protected Label dirLbl;
/*  78 */   protected Slider dirSlider = null;
/*  79 */   protected Text dirText = null;
/*     */ 
/*  81 */   protected Label spdLbl = null;
/*  82 */   protected Slider spdSlider = null;
/*  83 */   protected Text spdText = null;
/*     */   private Label sizeLbl;
/*  86 */   protected Slider sizeSlider = null;
/*  87 */   protected Text sizeText = null;
/*     */   private Label widthLbl;
/*  90 */   protected Slider widthSlider = null;
/*  91 */   protected Text widthText = null;
/*     */ 
/*  93 */   private Label arwHeadSizeLbl = null;
/*  94 */   private Slider arwHeadSizeSlider = null;
/*  95 */   private Text arwHeadSizeText = null;
/*     */   protected Button[] chkBox;
/*     */ 
/*     */   protected VectorAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 107 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static VectorAttrDlg getInstance(Shell parShell)
/*     */   {
/* 120 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/* 124 */         INSTANCE = new VectorAttrDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/* 128 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 133 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 143 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 146 */     GridLayout mainLayout = new GridLayout(3, false);
/* 147 */     mainLayout.marginHeight = 3;
/* 148 */     mainLayout.marginWidth = 3;
/* 149 */     this.top.setLayout(mainLayout);
/*     */ 
/* 151 */     initializeComponents();
/*     */ 
/* 153 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 163 */     getShell().setText("Vector Attributes");
/* 164 */     this.chkBox = new Button[7];
/*     */ 
/* 166 */     createColorAttr();
/* 167 */     createClearAttr();
/* 168 */     createDirectionAttr();
/* 169 */     createSpeedAttr();
/* 170 */     createSizeAttr();
/* 171 */     createWidthAttr();
/* 172 */     createHeadSizeAttr();
/*     */ 
/* 174 */     addSeparator(this.top.getParent());
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 182 */     if (this.chkBox[ChkBox.COLOR.ordinal()].getSelection())
/*     */     {
/* 185 */       Color[] colors = new Color[1];
/*     */ 
/* 187 */       colors[0] = new Color(this.cs.getColorValue().red, 
/* 188 */         this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*     */ 
/* 190 */       return colors;
/*     */     }
/*     */ 
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   private void setColor(Color clr)
/*     */   {
/* 203 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   public IVector.VectorType getVectorType()
/*     */   {
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */   public double getDirection()
/*     */   {
/* 221 */     if (this.chkBox[ChkBox.DIRECTION.ordinal()].getSelection()) {
/* 222 */       return this.dirSlider.getSelection();
/*     */     }
/*     */ 
/* 225 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public double getSpeed()
/*     */   {
/* 234 */     if (this.chkBox[ChkBox.SPEED.ordinal()].getSelection()) {
/* 235 */       return Double.valueOf(this.spdText.getText()).doubleValue();
/*     */     }
/*     */ 
/* 238 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 247 */     if (this.chkBox[ChkBox.WIDTH.ordinal()].getSelection()) {
/* 248 */       return this.widthSlider.getSelection();
/*     */     }
/*     */ 
/* 251 */     return (0.0F / 0.0F);
/*     */   }
/*     */ 
/*     */   public double getSizeScale()
/*     */   {
/* 260 */     if (this.chkBox[ChkBox.SIZE.ordinal()].getSelection()) {
/* 261 */       return this.sizeSlider.getSelection() / 10.0D;
/*     */     }
/*     */ 
/* 264 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public double getArrowHeadSize()
/*     */   {
/* 273 */     if (this.chkBox[ChkBox.HEADSIZE.ordinal()].getSelection()) {
/* 274 */       return this.arwHeadSizeSlider.getSelection() / 10.0D;
/*     */     }
/*     */ 
/* 277 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public boolean hasDirectionOnly()
/*     */   {
/* 287 */     return false;
/*     */   }
/*     */ 
/*     */   public Boolean isClear()
/*     */   {
/* 295 */     if (this.chkBox[ChkBox.CLEAR.ordinal()].getSelection()) {
/* 296 */       return Boolean.valueOf(this.clearBtn1.getSelection());
/*     */     }
/*     */ 
/* 299 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean hasBackgroundMask()
/*     */   {
/* 308 */     return isClear();
/*     */   }
/*     */ 
/*     */   public void setSpeed(double spd)
/*     */   {
/* 315 */     this.spdSlider.setSelection((int)spd);
/* 316 */     this.spdText.setText((int)spd);
/*     */   }
/*     */ 
/*     */   public void setDirection(double dir)
/*     */   {
/* 323 */     this.dirSlider.setSelection((int)dir);
/* 324 */     this.dirText.setText((int)dir);
/*     */   }
/*     */ 
/*     */   public void setLineWidth(float width)
/*     */   {
/* 332 */     this.widthSlider.setSelection((int)width);
/* 333 */     this.widthText.setText(width);
/*     */   }
/*     */ 
/*     */   public void setSizeScale(double size)
/*     */   {
/* 341 */     this.sizeSlider.setSelection((int)(size * 10.0D));
/* 342 */     this.sizeText.setText(size);
/*     */   }
/*     */ 
/*     */   public void setArrowHeadSize(double ahs)
/*     */   {
/* 350 */     this.arwHeadSizeSlider.setSelection((int)(ahs * 10.0D));
/* 351 */     this.arwHeadSizeText.setText(ahs);
/*     */   }
/*     */ 
/*     */   public void setClear(boolean clr)
/*     */   {
/* 358 */     this.clearBtn1.setSelection(clr);
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/* 367 */     HashMap attr = new HashMap();
/*     */ 
/* 369 */     attr.put("speed", Double.valueOf(getSpeed()));
/* 370 */     attr.put("direction", Double.valueOf(getDirection()));
/* 371 */     attr.put("arrowHeadSize", Double.valueOf(getArrowHeadSize()));
/* 372 */     attr.put("sizeScale", Double.valueOf(getSizeScale()));
/* 373 */     attr.put("lineWidth", Float.valueOf(getLineWidth()));
/* 374 */     attr.put("clear", isClear());
/* 375 */     attr.put("color", getColor());
/*     */ 
/* 377 */     return attr;
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute iattr)
/*     */   {
/* 384 */     if ((iattr instanceof IVector)) {
/* 385 */       IVector attr = (IVector)iattr;
/* 386 */       adjustAttrForDlg(((Vector)attr).getPgenType());
/*     */ 
/* 388 */       Color clr = attr.getColors()[0];
/* 389 */       if (clr != null) setColor(clr);
/*     */ 
/* 391 */       setSpeed(attr.getSpeed());
/* 392 */       setClear(attr.isClear().booleanValue());
/* 393 */       setDirection(attr.getDirection());
/* 394 */       setSizeScale(attr.getSizeScale());
/* 395 */       setLineWidth(attr.getLineWidth());
/* 396 */       setArrowHeadSize(attr.getArrowHeadSize());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void adjustAttrForDlg(String pgenType)
/*     */   {
/* 406 */     if (pgenType.equalsIgnoreCase("Barb"))
/*     */     {
/* 408 */       getShell().setText("Wind Barb Attributes");
/*     */ 
/* 410 */       this.arwHeadSizeLbl.setEnabled(false);
/* 411 */       this.arwHeadSizeSlider.setEnabled(false);
/* 412 */       this.arwHeadSizeText.setEnabled(false);
/*     */ 
/* 414 */       this.spdLbl.setEnabled(true);
/* 415 */       this.spdSlider.setEnabled(true);
/* 416 */       this.spdText.setEnabled(true);
/*     */ 
/* 418 */       this.spdSlider.setValues(100, 0, 405, 5, 5, 5);
/* 419 */       this.spdText.setText("100");
/*     */     }
/* 422 */     else if (pgenType.equalsIgnoreCase("Hash"))
/*     */     {
/* 424 */       getShell().setText("Hash Attributes");
/*     */ 
/* 426 */       this.spdLbl.setEnabled(false);
/* 427 */       this.spdSlider.setEnabled(false);
/* 428 */       this.spdText.setEnabled(false);
/*     */ 
/* 430 */       this.arwHeadSizeLbl.setEnabled(false);
/* 431 */       this.arwHeadSizeSlider.setEnabled(false);
/* 432 */       this.arwHeadSizeText.setEnabled(false);
/*     */     }
/* 434 */     else if (pgenType.equalsIgnoreCase("directional"))
/*     */     {
/* 436 */       getShell().setText("Directional Arrow Attributes");
/*     */ 
/* 438 */       this.spdLbl.setEnabled(false);
/* 439 */       this.spdSlider.setEnabled(false);
/* 440 */       this.spdText.setEnabled(false);
/*     */     }
/*     */     else
/*     */     {
/* 445 */       getShell().setText("Wind Arrow Attributes");
/*     */ 
/* 447 */       this.spdLbl.setEnabled(true);
/* 448 */       this.spdSlider.setEnabled(true);
/* 449 */       this.spdText.setEnabled(true);
/*     */ 
/* 451 */       this.spdSlider.setValues(10, 0, 401, 1, 1, 1);
/*     */ 
/* 453 */       this.arwHeadSizeLbl.setEnabled(true);
/* 454 */       this.arwHeadSizeSlider.setEnabled(true);
/* 455 */       this.arwHeadSizeText.setEnabled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createColorAttr()
/*     */   {
/* 464 */     this.chkBox[ChkBox.COLOR.ordinal()] = new Button(this.top, 32);
/* 465 */     this.chkBox[ChkBox.COLOR.ordinal()].setLayoutData(new GridData(16, 28));
/* 466 */     this.chkBox[ChkBox.COLOR.ordinal()].addSelectionListener(new SelectionListener()
/*     */     {
/*     */       public void widgetDefaultSelected(SelectionEvent e)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 475 */         Button btn = (Button)e.widget;
/* 476 */         if (btn.getSelection()) {
/* 477 */           VectorAttrDlg.this.colorLbl.setEnabled(true);
/*     */         }
/*     */         else
/* 480 */           VectorAttrDlg.this.colorLbl.setEnabled(false);
/*     */       }
/*     */     });
/* 486 */     this.colorLbl = new Label(this.top, 16384);
/* 487 */     this.colorLbl.setText("Color:");
/*     */ 
/* 489 */     this.cs = new ColorButtonSelector(this.top);
/* 490 */     this.cs.setColorValue(new RGB(0, 255, 0));
/*     */   }
/*     */ 
/*     */   private void createClearAttr()
/*     */   {
/* 497 */     this.chkBox[ChkBox.CLEAR.ordinal()] = new Button(this.top, 32);
/* 498 */     this.chkBox[ChkBox.CLEAR.ordinal()].setLayoutData(new GridData(16, 28));
/* 499 */     this.chkBox[ChkBox.CLEAR.ordinal()].addSelectionListener(new SelectionListener()
/*     */     {
/*     */       public void widgetDefaultSelected(SelectionEvent e)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 508 */         Button btn = (Button)e.widget;
/* 509 */         if (btn.getSelection()) {
/* 510 */           VectorAttrDlg.this.clearLbl.setEnabled(true);
/* 511 */           VectorAttrDlg.this.clearBtn1.setEnabled(true);
/* 512 */           VectorAttrDlg.this.clearBtn2.setEnabled(true);
/*     */         }
/*     */         else {
/* 515 */           VectorAttrDlg.this.clearLbl.setEnabled(false);
/* 516 */           VectorAttrDlg.this.clearBtn1.setEnabled(false);
/* 517 */           VectorAttrDlg.this.clearBtn2.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 523 */     this.clearLbl = new Label(this.top, 16384);
/* 524 */     this.clearLbl.setText("Clear:");
/*     */ 
/* 526 */     Group clearGroup = new Group(this.top, 0);
/* 527 */     GridLayout gl = new GridLayout(2, false);
/* 528 */     clearGroup.setLayout(gl);
/*     */ 
/* 530 */     this.clearBtn1 = new Button(clearGroup, 16);
/* 531 */     this.clearBtn1.setText("On");
/* 532 */     this.clearBtn1.setSelection(true);
/*     */ 
/* 534 */     this.clearBtn2 = new Button(clearGroup, 16);
/* 535 */     this.clearBtn2.setText("Off");
/*     */   }
/*     */ 
/*     */   private void createDirectionAttr()
/*     */   {
/* 542 */     this.chkBox[ChkBox.DIRECTION.ordinal()] = new Button(this.top, 32);
/* 543 */     this.chkBox[ChkBox.DIRECTION.ordinal()].setLayoutData(new GridData(16, 28));
/* 544 */     this.chkBox[ChkBox.DIRECTION.ordinal()].addSelectionListener(new SelectionListener()
/*     */     {
/*     */       public void widgetDefaultSelected(SelectionEvent e)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 553 */         Button btn = (Button)e.widget;
/* 554 */         if (btn.getSelection()) {
/* 555 */           VectorAttrDlg.this.dirLbl.setEnabled(true);
/* 556 */           VectorAttrDlg.this.dirText.setEnabled(true);
/* 557 */           VectorAttrDlg.this.dirSlider.setEnabled(true);
/*     */         }
/*     */         else {
/* 560 */           VectorAttrDlg.this.dirLbl.setEnabled(false);
/* 561 */           VectorAttrDlg.this.dirText.setEnabled(false);
/* 562 */           VectorAttrDlg.this.dirSlider.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 567 */     this.dirLbl = new Label(this.top, 16384);
/* 568 */     this.dirLbl.setText("Direction:");
/* 569 */     GridLayout gl = new GridLayout(2, false);
/*     */ 
/* 571 */     Group dirGroup = new Group(this.top, 0);
/* 572 */     dirGroup.setLayout(gl);
/*     */ 
/* 574 */     this.dirSlider = new Slider(dirGroup, 256);
/* 575 */     this.dirSlider.setValues(360, 0, 365, 5, 5, 5);
/* 576 */     this.dirSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 578 */         VectorAttrDlg.this.dirText.setText(VectorAttrDlg.this.dirSlider.getSelection());
/*     */       }
/*     */     });
/* 582 */     this.dirText = new Text(dirGroup, 2052);
/* 583 */     this.dirText.setLayoutData(new GridData(25, 10));
/* 584 */     this.dirText.setEditable(true);
/* 585 */     this.dirText.setText("360");
/* 586 */     this.dirText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 588 */         int value = 0;
/*     */         try {
/* 590 */           value = Integer.parseInt(VectorAttrDlg.this.dirText.getText());
/* 591 */           if ((value >= 0) && (value < 361)) {
/* 592 */             VectorAttrDlg.this.dirSlider.setSelection(value / 5 * 5);
/* 593 */             VectorAttrDlg.this.dirText.setToolTipText("");
/*     */           }
/*     */           else {
/* 596 */             VectorAttrDlg.this.dirText.setToolTipText("Only integer values between 0 and 360 are accepted.");
/*     */           }
/*     */         } catch (NumberFormatException e1) {
/* 599 */           VectorAttrDlg.this.dirText.setToolTipText("Only integer values between 0 and 360 are accepted.");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createSpeedAttr()
/*     */   {
/* 610 */     this.chkBox[ChkBox.SPEED.ordinal()] = new Button(this.top, 32);
/* 611 */     this.chkBox[ChkBox.SPEED.ordinal()].setLayoutData(new GridData(16, 28));
/* 612 */     this.chkBox[ChkBox.SPEED.ordinal()].addSelectionListener(new SelectionListener()
/*     */     {
/*     */       public void widgetDefaultSelected(SelectionEvent e)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 621 */         Button btn = (Button)e.widget;
/* 622 */         if (btn.getSelection()) {
/* 623 */           VectorAttrDlg.this.spdLbl.setEnabled(true);
/* 624 */           VectorAttrDlg.this.spdText.setEnabled(true);
/* 625 */           VectorAttrDlg.this.spdSlider.setEnabled(true);
/*     */         }
/*     */         else {
/* 628 */           VectorAttrDlg.this.spdLbl.setEnabled(false);
/* 629 */           VectorAttrDlg.this.spdText.setEnabled(false);
/* 630 */           VectorAttrDlg.this.spdSlider.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 636 */     this.spdLbl = new Label(this.top, 16384);
/* 637 */     this.spdLbl.setText("Speed:");
/* 638 */     GridLayout gl = new GridLayout(2, false);
/*     */ 
/* 640 */     Group spdGroup = new Group(this.top, 0);
/* 641 */     spdGroup.setLayout(gl);
/*     */ 
/* 643 */     this.spdSlider = new Slider(spdGroup, 256);
/* 644 */     this.spdSlider.setValues(10, 0, 401, 1, 1, 1);
/* 645 */     this.spdSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 647 */         int thumb = VectorAttrDlg.this.spdSlider.getThumb();
/* 648 */         VectorAttrDlg.this.spdText.setText(VectorAttrDlg.this.spdSlider.getSelection() / thumb * thumb);
/*     */       }
/*     */     });
/* 652 */     this.spdText = new Text(spdGroup, 2052);
/* 653 */     this.spdText.setLayoutData(new GridData(25, 10));
/* 654 */     this.spdText.setEditable(true);
/* 655 */     this.spdText.setText("10");
/* 656 */     this.spdText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 658 */         int value = 0;
/*     */         try {
/* 660 */           value = Integer.parseInt(VectorAttrDlg.this.spdText.getText());
/* 661 */           if ((value >= 0) && (value < 401)) {
/* 662 */             VectorAttrDlg.this.spdSlider.setSelection(value);
/* 663 */             VectorAttrDlg.this.spdText.setToolTipText("");
/*     */           }
/*     */           else {
/* 666 */             VectorAttrDlg.this.spdText.setToolTipText("Only integer values between 0 and 400 are accepted.");
/*     */           }
/*     */         } catch (NumberFormatException e1) {
/* 669 */           VectorAttrDlg.this.spdText.setToolTipText("Only integer values between 0 and 400 are accepted.");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createSizeAttr()
/*     */   {
/* 679 */     this.chkBox[ChkBox.SIZE.ordinal()] = new Button(this.top, 32);
/* 680 */     this.chkBox[ChkBox.SIZE.ordinal()].setLayoutData(new GridData(16, 28));
/* 681 */     this.chkBox[ChkBox.SIZE.ordinal()].addSelectionListener(new SelectionListener()
/*     */     {
/*     */       public void widgetDefaultSelected(SelectionEvent e)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 690 */         Button btn = (Button)e.widget;
/* 691 */         if (btn.getSelection()) {
/* 692 */           VectorAttrDlg.this.sizeLbl.setEnabled(true);
/* 693 */           VectorAttrDlg.this.sizeText.setEnabled(true);
/* 694 */           VectorAttrDlg.this.sizeSlider.setEnabled(true);
/*     */         }
/*     */         else {
/* 697 */           VectorAttrDlg.this.sizeLbl.setEnabled(false);
/* 698 */           VectorAttrDlg.this.sizeText.setEnabled(false);
/* 699 */           VectorAttrDlg.this.sizeSlider.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 706 */     this.sizeLbl = new Label(this.top, 16384);
/* 707 */     this.sizeLbl.setText("Size:");
/* 708 */     GridLayout gl = new GridLayout(2, false);
/*     */ 
/* 710 */     Group sizeGroup = new Group(this.top, 0);
/* 711 */     sizeGroup.setLayout(gl);
/*     */ 
/* 713 */     this.sizeSlider = new Slider(sizeGroup, 256);
/* 714 */     this.sizeSlider.setValues(10, 1, 101, 1, 1, 1);
/* 715 */     this.sizeSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 717 */         VectorAttrDlg.this.sizeText.setText(VectorAttrDlg.this.sizeSlider.getSelection() / 10.0D);
/*     */       }
/*     */     });
/* 721 */     this.sizeText = new Text(sizeGroup, 2052);
/* 722 */     this.sizeText.setLayoutData(new GridData(25, 10));
/* 723 */     this.sizeText.setEditable(true);
/* 724 */     this.sizeText.setText("1.0");
/* 725 */     this.sizeText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 727 */         double value = 0.0D;
/*     */         try {
/* 729 */           value = Double.parseDouble(VectorAttrDlg.this.sizeText.getText());
/* 730 */           if ((value >= 0.1D) && (value < 10.0D)) {
/* 731 */             VectorAttrDlg.this.sizeSlider.setSelection((int)(value * 10.0D));
/* 732 */             VectorAttrDlg.this.sizeText.setToolTipText("");
/*     */           }
/*     */           else {
/* 735 */             VectorAttrDlg.this.sizeText.setToolTipText("Only values between 0.1 and 10.0 are accepted.");
/*     */           }
/*     */         } catch (NumberFormatException e1) {
/* 738 */           VectorAttrDlg.this.sizeText.setToolTipText("Only values between 0.1 and 10.0 are accepted.");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createWidthAttr()
/*     */   {
/* 748 */     this.chkBox[ChkBox.WIDTH.ordinal()] = new Button(this.top, 32);
/* 749 */     this.chkBox[ChkBox.WIDTH.ordinal()].setLayoutData(new GridData(16, 28));
/* 750 */     this.chkBox[ChkBox.WIDTH.ordinal()].addSelectionListener(new SelectionListener()
/*     */     {
/*     */       public void widgetDefaultSelected(SelectionEvent e)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 759 */         Button btn = (Button)e.widget;
/* 760 */         if (btn.getSelection()) {
/* 761 */           VectorAttrDlg.this.widthLbl.setEnabled(true);
/* 762 */           VectorAttrDlg.this.widthText.setEnabled(true);
/* 763 */           VectorAttrDlg.this.widthSlider.setEnabled(true);
/*     */         }
/*     */         else {
/* 766 */           VectorAttrDlg.this.widthLbl.setEnabled(false);
/* 767 */           VectorAttrDlg.this.widthText.setEnabled(false);
/* 768 */           VectorAttrDlg.this.widthSlider.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 775 */     this.widthLbl = new Label(this.top, 16384);
/* 776 */     this.widthLbl.setText("Width:");
/*     */ 
/* 778 */     GridLayout gl = new GridLayout(2, false);
/*     */ 
/* 780 */     Group widthGroup = new Group(this.top, 0);
/* 781 */     widthGroup.setLayout(gl);
/*     */ 
/* 783 */     this.widthSlider = new Slider(widthGroup, 256);
/* 784 */     this.widthSlider.setValues(2, 1, 11, 1, 1, 1);
/* 785 */     this.widthSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 787 */         VectorAttrDlg.this.widthText.setText(VectorAttrDlg.this.widthSlider.getSelection());
/*     */       }
/*     */     });
/* 791 */     this.widthText = new Text(widthGroup, 2052);
/* 792 */     this.widthText.setLayoutData(new GridData(25, 10));
/* 793 */     this.widthText.setEditable(true);
/* 794 */     this.widthText.setText("2");
/* 795 */     this.widthText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 797 */         int value = 0;
/*     */         try {
/* 799 */           value = Integer.parseInt(VectorAttrDlg.this.widthText.getText());
/* 800 */           if ((value >= 1) && (value < 11)) {
/* 801 */             VectorAttrDlg.this.widthSlider.setSelection(value);
/* 802 */             VectorAttrDlg.this.widthText.setToolTipText("");
/*     */           }
/*     */           else {
/* 805 */             VectorAttrDlg.this.widthText.setToolTipText("Only values between 1.0 and 10.0 are accepted.");
/*     */           }
/*     */         } catch (NumberFormatException e1) {
/* 808 */           VectorAttrDlg.this.widthText.setToolTipText("Only values between 1.0 and 10.0 are accepted.");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void createHeadSizeAttr()
/*     */   {
/* 818 */     this.chkBox[ChkBox.HEADSIZE.ordinal()] = new Button(this.top, 32);
/* 819 */     this.chkBox[ChkBox.HEADSIZE.ordinal()].setLayoutData(new GridData(16, 28));
/* 820 */     this.chkBox[ChkBox.HEADSIZE.ordinal()].addSelectionListener(new SelectionListener()
/*     */     {
/*     */       public void widgetDefaultSelected(SelectionEvent e)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void widgetSelected(SelectionEvent e)
/*     */       {
/* 829 */         Button btn = (Button)e.widget;
/* 830 */         if (btn.getSelection()) {
/* 831 */           VectorAttrDlg.this.arwHeadSizeLbl.setEnabled(true);
/* 832 */           VectorAttrDlg.this.arwHeadSizeText.setEnabled(true);
/* 833 */           VectorAttrDlg.this.arwHeadSizeSlider.setEnabled(true);
/*     */         }
/*     */         else {
/* 836 */           VectorAttrDlg.this.arwHeadSizeLbl.setEnabled(false);
/* 837 */           VectorAttrDlg.this.arwHeadSizeText.setEnabled(false);
/* 838 */           VectorAttrDlg.this.arwHeadSizeSlider.setEnabled(false);
/*     */         }
/*     */       }
/*     */     });
/* 844 */     this.arwHeadSizeLbl = new Label(this.top, 16384);
/* 845 */     this.arwHeadSizeLbl.setText("Head Size:");
/* 846 */     GridLayout gl = new GridLayout(2, false);
/*     */ 
/* 848 */     Group arwHeadSizeGroup = new Group(this.top, 0);
/* 849 */     arwHeadSizeGroup.setLayout(gl);
/*     */ 
/* 851 */     this.arwHeadSizeSlider = new Slider(arwHeadSizeGroup, 256);
/* 852 */     this.arwHeadSizeSlider.setValues(10, 1, 101, 1, 1, 1);
/* 853 */     this.arwHeadSizeSlider.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 855 */         VectorAttrDlg.this.arwHeadSizeText.setText(VectorAttrDlg.this.arwHeadSizeSlider.getSelection() / 10.0D);
/*     */       }
/*     */     });
/* 859 */     this.arwHeadSizeText = new Text(arwHeadSizeGroup, 2052);
/* 860 */     this.arwHeadSizeText.setLayoutData(new GridData(25, 10));
/* 861 */     this.arwHeadSizeText.setEditable(true);
/* 862 */     this.arwHeadSizeText.setText("1.0");
/* 863 */     this.arwHeadSizeText.addKeyListener(new KeyAdapter() {
/*     */       public void keyReleased(KeyEvent e) {
/* 865 */         double value = 0.0D;
/*     */         try {
/* 867 */           value = Double.parseDouble(VectorAttrDlg.this.arwHeadSizeText.getText());
/* 868 */           if ((value >= 0.1D) && (value < 10.0D)) {
/* 869 */             VectorAttrDlg.this.arwHeadSizeSlider.setSelection((int)(value * 10.0D));
/* 870 */             VectorAttrDlg.this.arwHeadSizeText.setToolTipText("");
/*     */           }
/*     */           else {
/* 873 */             VectorAttrDlg.this.arwHeadSizeText.setToolTipText("Only values between 0.1 and 10.0 are accepted.");
/*     */           }
/*     */         } catch (NumberFormatException e1) {
/* 876 */           VectorAttrDlg.this.arwHeadSizeText.setToolTipText("Only values between 0.1 and 10.0 are accepted.");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 886 */     create();
/*     */ 
/* 889 */     if (PgenSession.getInstance().getPgenPalette().getCurrentAction()
/* 889 */       .equalsIgnoreCase("MultiSelect")) {
/* 890 */       enableChkBoxes(true);
/* 891 */       enableAllWidgets(false);
/*     */     }
/*     */     else {
/* 894 */       enableChkBoxes(false);
/*     */     }
/*     */ 
/* 897 */     int rt = super.open();
/* 898 */     Point shellSizeInPoint = getShell().getSize();
/* 899 */     shellSizeInPoint.x += 20;
/* 900 */     getShell().setSize(shellSizeInPoint);
/* 901 */     return rt;
/*     */   }
/*     */ 
/*     */   private void enableChkBoxes(boolean flag)
/*     */   {
/* 910 */     if (!flag) {
/* 911 */       setAllChkBoxes();
/*     */     }
/* 913 */     for (ChkBox chk : ChkBox.values())
/* 914 */       this.chkBox[chk.ordinal()].setVisible(flag);
/*     */   }
/*     */ 
/*     */   private void enableAllWidgets(boolean flag)
/*     */   {
/* 925 */     this.colorLbl.setEnabled(flag);
/*     */ 
/* 927 */     this.clearLbl.setEnabled(flag);
/* 928 */     this.clearBtn1.setEnabled(flag);
/* 929 */     this.clearBtn2.setEnabled(flag);
/*     */ 
/* 931 */     this.widthLbl.setEnabled(flag);
/* 932 */     this.widthText.setEnabled(flag);
/* 933 */     this.widthSlider.setEnabled(flag);
/*     */ 
/* 935 */     this.sizeLbl.setEnabled(flag);
/* 936 */     this.sizeText.setEnabled(flag);
/* 937 */     this.sizeSlider.setEnabled(flag);
/*     */ 
/* 939 */     this.dirLbl.setEnabled(flag);
/* 940 */     this.dirText.setEnabled(flag);
/* 941 */     this.dirSlider.setEnabled(flag);
/*     */ 
/* 943 */     this.spdLbl.setEnabled(flag);
/* 944 */     this.spdText.setEnabled(flag);
/* 945 */     this.spdSlider.setEnabled(flag);
/*     */ 
/* 947 */     this.arwHeadSizeLbl.setEnabled(flag);
/* 948 */     this.arwHeadSizeText.setEnabled(flag);
/* 949 */     this.arwHeadSizeSlider.setEnabled(flag);
/*     */   }
/*     */ 
/*     */   private void setAllChkBoxes()
/*     */   {
/* 957 */     for (ChkBox chk : ChkBox.values())
/* 958 */       this.chkBox[chk.ordinal()].setSelection(true);
/*     */   }
/*     */ 
/*     */   public Coordinate getLocation()
/*     */   {
/* 964 */     return null;
/*     */   }
/*     */ 
/*     */   public Color getColor()
/*     */   {
/* 969 */     return getColors()[0];
/*     */   }
/*     */ 
/*     */   protected static enum ChkBox
/*     */   {
/*  64 */     COLOR, CLEAR, DIRECTION, SPEED, SIZE, WIDTH, HEADSIZE;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.VectorAttrDlg
 * JD-Core Version:    0.6.2
 */