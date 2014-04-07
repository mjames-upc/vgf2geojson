/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.ISinglePoint;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo.ProductInfo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Volcano;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenVolcanoCreateTool;
/*     */ import java.awt.Color;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.eclipse.swt.events.KeyAdapter;
/*     */ import org.eclipse.swt.events.KeyEvent;
/*     */ import org.eclipse.swt.events.ModifyEvent;
/*     */ import org.eclipse.swt.events.ModifyListener;
/*     */ import org.eclipse.swt.events.VerifyEvent;
/*     */ import org.eclipse.swt.events.VerifyListener;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Menu;
/*     */ import org.eclipse.swt.widgets.MenuItem;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.swt.widgets.ToolBar;
/*     */ import org.eclipse.swt.widgets.ToolItem;
/*     */ 
/*     */ public class VolcanoCreateDlg extends AttrDlg
/*     */   implements ISinglePoint
/*     */ {
/*     */   private static VolcanoCreateDlg INSTANCE;
/*  78 */   protected Composite top = null;
/*     */ 
/*  83 */   Text txtVol = null;
/*     */ 
/*  88 */   Text txtNum = null;
/*     */ 
/*  93 */   Text txtLoc = null;
/*     */ 
/*  98 */   Text txtEle = null;
/*     */ 
/* 103 */   Text txtArea = null;
/*     */ 
/* 108 */   Button btnVol = null;
/*     */ 
/* 114 */   Button btnLay = null;
/*     */ 
/* 119 */   private Station stn = null;
/*     */ 
/* 124 */   private PgenVolcanoCreateTool tool = null;
/*     */ 
/* 129 */   private Volcano elem = null;
/*     */ 
/* 134 */   private boolean setStnFlag = false;
/*     */ 
/* 140 */   private boolean isManualInput = false;
/*     */ 
/*     */   public VolcanoCreateDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 150 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static VolcanoCreateDlg getInstance(Shell parShell)
/*     */   {
/* 161 */     if (INSTANCE == null) {
/*     */       try
/*     */       {
/* 164 */         INSTANCE = new VolcanoCreateDlg(parShell);
/*     */       } catch (VizException e) {
/* 166 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 171 */     return INSTANCE;
/*     */   }
/*     */   public HashMap<String, Object> getAttrFromDlg() {
/* 174 */     return new HashMap();
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute ia)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 183 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 185 */     getShell().setText("VAA Volcano Create");
/*     */ 
/* 187 */     GridLayout mainLayout = new GridLayout(8, false);
/* 188 */     mainLayout.marginHeight = 3;
/* 189 */     mainLayout.marginWidth = 3;
/* 190 */     this.top.setLayout(mainLayout);
/*     */ 
/* 192 */     Group top1 = new Group(this.top, 16384);
/* 193 */     top1.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/* 194 */     top1.setLayout(new GridLayout(8, false));
/* 195 */     createArea1(top1);
/*     */ 
/* 197 */     Group top2 = new Group(this.top, 16384);
/* 198 */     top2.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/* 199 */     top2.setLayout(new GridLayout(8, false));
/* 200 */     createArea2(top2);
/*     */ 
/* 202 */     Group top3 = new Group(this.top, 16384);
/* 203 */     top3.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/* 204 */     top3.setLayout(new GridLayout(8, false));
/* 205 */     createArea3(top3);
/*     */ 
/* 207 */     Group top4 = new Group(this.top, 16384);
/* 208 */     top4.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/* 209 */     top4.setLayout(new GridLayout(8, false));
/* 210 */     createArea4(top4);
/*     */ 
/* 212 */     Group top5 = new Group(this.top, 16384);
/* 213 */     top5.setLayoutData(new GridData(4, 16777216, true, true, 8, 1));
/* 214 */     top5.setLayout(new GridLayout(8, false));
/* 215 */     createArea5(top5);
/*     */ 
/* 220 */     new TextFieldListener(
/* 221 */       new Text[] { this.txtArea, this.txtEle, this.txtLoc, this.txtNum, this.txtVol }, 
/* 222 */       new Control[] { this.btnVol });
/*     */ 
/* 225 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void createArea1(Group top2)
/*     */   {
/* 234 */     Label lblVol = new Label(top2, 16384);
/* 235 */     lblVol.setText("Volcano: ");
/*     */ 
/* 237 */     this.txtVol = new Text(top2, 18440);
/* 238 */     this.txtVol.setLayoutData(new GridData(4, 16777216, true, false, 3, 1));
/*     */ 
/* 240 */     Shell shell = getShell();
/*     */ 
/* 242 */     final ToolBar tb = new ToolBar(top2, 256);
/* 243 */     final ToolItem ti = new ToolItem(tb, 4);
/*     */ 
/* 245 */     final Menu mu = new Menu(shell, 8);
/*     */ 
/* 247 */     for (int i = 0; i < SigmetInfo.VOL_NAME_BUCKET_ARRAY.length; i++) {
/* 248 */       if (i == 0) {
/* 249 */         MenuItem mi1 = new MenuItem(mu, 8);
/* 250 */         mi1.setText(SigmetInfo.VOL_NAME_BUCKET_ARRAY[i]);
/*     */ 
/* 252 */         mi1.addListener(13, new Listener() {
/*     */           public void handleEvent(Event e) {
/* 254 */             VolcanoCreateDlg.this.resetTxts();
/* 255 */             VolcanoCreateDlg.this.setTxtsEditable(true);
/*     */           } } );
/*     */       }
/*     */       else {
/* 259 */         MenuItem mi1 = new MenuItem(mu, 64);
/* 260 */         mi1.setText(SigmetInfo.VOL_NAME_BUCKET_ARRAY[i]);
/* 261 */         Menu mi1Menu = new Menu(shell, 4);
/* 262 */         mi1.setMenu(mi1Menu);
/*     */ 
/* 264 */         List list = (List)SigmetInfo.VOLCANO_BUCKET_MAP.get(SigmetInfo.VOL_NAME_BUCKET_ARRAY[i]);
/* 265 */         int size = list.size();
/* 266 */         for (int j = 0; j < size; j++) {
/* 267 */           final MenuItem mi1MenuMi1 = new MenuItem(mi1Menu, 8);
/* 268 */           mi1MenuMi1.setText((String)list.get(j));
/* 269 */           mi1MenuMi1.addListener(13, new Listener() {
/*     */             public void handleEvent(Event e) {
/* 271 */               VolcanoCreateDlg.this.resetTxts();
/* 272 */               VolcanoCreateDlg.this.setTxtsEditable(false);
/*     */ 
/* 274 */               VolcanoCreateDlg.this.setTextToTxts(mi1MenuMi1.getText());
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 281 */     ti.addListener(13, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/* 285 */         Rectangle bounds = ti.getBounds();
/* 286 */         Point point = tb.toDisplay(bounds.x, bounds.y + bounds.height);
/* 287 */         mu.setLocation(point);
/* 288 */         mu.setVisible(true);
/*     */       }
/*     */     });
/* 294 */     Label lblNum = new Label(top2, 16384);
/* 295 */     lblNum.setText("   Number: ");
/*     */ 
/* 297 */     this.txtNum = new Text(top2, 2056);
/*     */   }
/*     */ 
/*     */   private void createArea2(Group top)
/*     */   {
/* 307 */     Label lblLoc = new Label(top, 16384);
/* 308 */     lblLoc.setText("Location ( e.g. N3900W07700 ):  ");
/*     */ 
/* 310 */     this.txtLoc = new Text(top, 2056);
/* 311 */     this.txtLoc.setLayoutData(new GridData(4, 16777216, true, false, 3, 1));
/*     */   }
/*     */ 
/*     */   private void createArea3(Group top)
/*     */   {
/* 321 */     Label lblArea = new Label(top, 16384);
/* 322 */     lblArea.setText("Area: ");
/*     */ 
/* 324 */     this.txtArea = new Text(top, 2056);
/* 325 */     this.txtArea.setLayoutData(new GridData(4, 16777216, true, false, 2, 1));
/*     */ 
/* 327 */     Label lblEle = new Label(top, 16384);
/* 328 */     lblEle.setText("     Elevation: ");
/*     */ 
/* 330 */     this.txtEle = new Text(top, 2056);
/*     */ 
/* 332 */     Label lblFt = new Label(top, 16384);
/* 333 */     lblFt.setText(" ft");
/*     */   }
/*     */ 
/*     */   private void createArea4(Group top)
/*     */   {
/* 342 */     Label lblDummy = new Label(top, 16384);
/* 343 */     Label lblDummy2 = new Label(top, 16384);
/* 344 */     Label lblDummy3 = new Label(top, 16384);
/*     */ 
/* 346 */     this.btnVol = new Button(top, 8);
/* 347 */     this.btnVol.setText("Create VAA Volcano");
/*     */ 
/* 349 */     this.btnVol.addListener(13, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 351 */         if ((VolcanoCreateDlg.this.txtVol != null) && (VolcanoCreateDlg.this.txtVol.getText() != null) && (VolcanoCreateDlg.this.txtVol.getText().length() > 0)) {
/* 352 */           VolcanoCreateDlg.this.createVol();
/* 353 */           VolcanoCreateDlg.this.cancelPressed();
/*     */         }
/*     */       }
/*     */     });
/* 358 */     this.btnLay = new Button(top, 8);
/* 359 */     this.btnLay.setText("Create Volcano in Layers");
/*     */ 
/* 361 */     this.btnLay.addListener(13, new Listener()
/*     */     {
/*     */       public void handleEvent(Event e)
/*     */       {
/*     */       }
/*     */     });
/* 371 */     Button btnCan = new Button(top, 8);
/* 372 */     btnCan.setText("Cancel");
/* 373 */     btnCan.addListener(13, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 375 */         VolcanoCreateDlg.this.cancelPressed();
/*     */       }
/*     */     });
/* 381 */     this.btnVol.setEnabled(false);
/* 382 */     this.btnLay.setEnabled(false);
/*     */   }
/*     */ 
/*     */   private void createArea5(Group top)
/*     */   {
/* 391 */     Label lblDummy = new Label(top, 16384);
/* 392 */     Label lblDummy2 = new Label(top, 16384);
/* 393 */     Label lblDummy3 = new Label(top, 16384);
/*     */ 
/* 395 */     Label lblPro = new Label(top, 16384);
/* 396 */     lblPro.setText(" Special Products:  ");
/*     */ 
/* 398 */     final Combo comboType = new Combo(top, 8);
/* 399 */     comboType.setItems(VaaInfo.ProductInfo.getProduct(VaaInfo.LOCS[1]));
/* 400 */     comboType.select(0);
/*     */ 
/* 402 */     Button btnGo = new Button(top, 8);
/* 403 */     btnGo.setText(" Go....... ");
/* 404 */     btnGo.addListener(13, new Listener() {
/*     */       public void handleEvent(Event e) {
/* 406 */         VolcanoVaaAttrDlg vaDlg = VolcanoVaaAttrDlg.getInstance(VolcanoCreateDlg.this.getParentShell());
/*     */ 
/* 408 */         vaDlg.setFromSelection(false);
/* 409 */         Volcano vol = new Volcano();
/* 410 */         vol.setPgenCategory("SIGMET");
/* 411 */         vol.setPgenType("VOLC_SIGMET");
/* 412 */         vol.setProduct(comboType.getText().trim());
/* 413 */         VaaInfo.setVolcanoFields(vol, comboType.getText().trim(), false);
/*     */ 
/* 415 */         VolcanoCreateDlg.this.drawingLayer.addElement(vol);
/* 416 */         VaaInfo.VOL_PROD_MAP.put(vol, VolcanoCreateDlg.this.drawingLayer.getActiveProduct());
/* 417 */         vaDlg.setVolcano(vol);
/* 418 */         VolcanoCreateDlg.this.cancelPressed();
/* 419 */         vaDlg.open();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void setTxtsEditable(boolean flag)
/*     */   {
/* 435 */     this.txtVol.setEditable(flag);
/* 436 */     this.txtNum.setEditable(flag);
/* 437 */     this.txtLoc.setEditable(flag);
/* 438 */     this.txtArea.setEditable(flag);
/* 439 */     this.txtEle.setEditable(flag);
/* 440 */     this.isManualInput = flag;
/* 441 */     this.setStnFlag = flag;
/*     */   }
/*     */ 
/*     */   private void resetTxts()
/*     */   {
/* 448 */     this.txtVol.setText("");
/* 449 */     this.txtNum.setText("");
/* 450 */     this.txtLoc.setText("");
/* 451 */     this.txtArea.setText("");
/* 452 */     this.txtEle.setText("");
/*     */   }
/*     */ 
/*     */   private void setTextToTxts(String volName)
/*     */   {
/* 460 */     this.txtVol.setText(volName);
/*     */ 
/* 462 */     List list = SigmetInfo.VOLCANO_STATION_LIST;
/*     */ 
/* 466 */     for (Station station : list) {
/* 467 */       if ((volName != null) && (volName.equals(station.getStnname()))) {
/* 468 */         this.stn = station;
/*     */       }
/*     */     }
/* 471 */     this.txtNum.setText(this.stn.getStid());
/* 472 */     this.txtLoc.setText(getLocText(this.stn));
/* 473 */     this.txtArea.setText(this.stn.getLocation());
/*     */ 
/* 475 */     this.txtEle.setText(VaaInfo.getFootTxtFromMeter(this.stn.getElevation().intValue(), 1));
/*     */   }
/*     */ 
/*     */   private String getLocText(Station stn)
/*     */   {
/* 485 */     String s = PgenUtil.getLatLonStringPrepend(new Coordinate[] { new Coordinate(stn.getLongitude().floatValue(), stn.getLatitude().floatValue()) }, false);
/* 486 */     return s.replaceAll(":::", "");
/*     */   }
/*     */ 
/*     */   public void createVol()
/*     */   {
/* 494 */     setStation(this.setStnFlag);
/*     */ 
/* 496 */     DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/* 498 */     this.elem = ((Volcano)def.create(DrawableType.VAA, this, 
/* 499 */       this.pgenCategory, this.pgenType, new Coordinate(this.stn.getLongitude().floatValue(), 
/* 500 */       this.stn.getLatitude().floatValue()), this.drawingLayer.getActiveLayer()));
/*     */ 
/* 502 */     if (this.elem != null)
/*     */     {
/* 504 */       this.elem.setProduct(VaaInfo.DEFAULT_PRODUCT);
/*     */ 
/* 506 */       this.elem.setArea(this.stn.getLocation());
/* 507 */       this.elem.setElev(getElevText(this.stn));
/* 508 */       this.elem.setTxtLoc(getLocText(this.stn));
/* 509 */       this.elem.setNumber(this.stn.getStid());
/* 510 */       this.elem.setName(this.stn.getStnname());
/*     */ 
/* 526 */       this.drawingLayer.addElement(this.elem);
/* 527 */       VaaInfo.VOL_PROD_MAP.put(this.elem, this.drawingLayer.getActiveProduct());
/* 528 */       this.mapEditor.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCreateTool(PgenVolcanoCreateTool tool)
/*     */   {
/* 534 */     this.tool = tool;
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 539 */     setReturnCode(1);
/* 540 */     close();
/* 541 */     PgenUtil.setSelectingMode();
/*     */   }
/*     */ 
/*     */   private void setStation(boolean flag)
/*     */   {
/* 549 */     if (flag) {
/* 550 */       this.stn = new Station();
/*     */ 
/* 552 */       this.stn.setStnname(this.txtVol.getText().trim());
/* 553 */       this.stn.setStid(this.txtNum.getText().trim());
/*     */ 
/* 555 */       String latlon = this.txtLoc.getText().trim();
/* 556 */       Float lat = VaaInfo.getLatLonFromTxt(latlon, true);
/* 557 */       this.stn.setLatitude(lat);
/* 558 */       Float lon = VaaInfo.getLatLonFromTxt(latlon, false);
/* 559 */       this.stn.setLongitude(lon);
/*     */ 
/* 561 */       this.stn.setElevation(Integer.valueOf(VaaInfo.getMeterIntFromFoot(this.txtEle.getText().trim())));
/*     */ 
/* 563 */       this.stn.setLocation(this.txtArea.getText().trim());
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getElevText(Station stn)
/*     */   {
/* 657 */     StringBuilder sb = new StringBuilder();
/*     */ 
/* 659 */     double dMeter = 0.0D;
/* 660 */     String sMeter = stn.getElevation();
/*     */     try {
/* 662 */       dMeter = Double.parseDouble(sMeter);
/*     */     } catch (Exception localException) {
/*     */     }
/* 665 */     String feet = VaaInfo.getFootTxtFromMeter(dMeter, 0);
/* 666 */     sb.append(feet).append("  ft (");
/* 667 */     sb.append(sMeter).append("  m)");
/*     */ 
/* 669 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 674 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean isClear()
/*     */   {
/* 679 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public Coordinate getLocation()
/*     */   {
/* 684 */     return null;
/*     */   }
/*     */ 
/*     */   class TextFieldListener extends KeyAdapter
/*     */     implements ModifyListener, VerifyListener
/*     */   {
/*     */     private Text[] texts;
/*     */     private Control[] controls;
/*     */ 
/*     */     public TextFieldListener(Text[] txts, Control[] ctrls)
/*     */     {
/* 586 */       this.texts = txts;
/* 587 */       this.controls = ctrls;
/*     */       Text text;
/* 589 */       for (text : txts) {
/* 590 */         text.addModifyListener(this);
/* 591 */         text.addVerifyListener(this);
/* 592 */         text.addKeyListener(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void keyPressed(KeyEvent e)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void modifyText(ModifyEvent e)
/*     */     {
/* 610 */       for (Text txt : this.texts)
/*     */       {
/* 613 */         if ((txt.getText() == null) || (txt.getText().length() == 0)) {
/* 614 */           for (Control cont : this.controls) cont.setEnabled(false);
/* 615 */           return;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 623 */       if (VolcanoCreateDlg.this.isManualInput)
/*     */       {
/* 625 */         if ((VaaInfo.isValidLatLon(VolcanoCreateDlg.this.txtLoc.getText().trim())) && 
/* 626 */           (VaaInfo.isValidElev(VolcanoCreateDlg.this.txtEle.getText().trim())))
/* 627 */           for (Control cont : this.controls) cont.setEnabled(true);
/*     */         else {
/* 629 */           for (Control cont : this.controls) cont.setEnabled(false);
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 638 */         for (Control cont : this.controls) cont.setEnabled(true);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void verifyText(VerifyEvent e)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.VolcanoCreateDlg
 * JD-Core Version:    0.6.2
 */