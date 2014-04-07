/*     */ package gov.noaa.nws.ncep.ui.pgen;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaClip;
/*     */ import java.io.File;
/*     */ import org.eclipse.jface.preference.BooleanFieldEditor;
/*     */ import org.eclipse.jface.preference.ComboFieldEditor;
/*     */ import org.eclipse.jface.preference.DirectoryFieldEditor;
/*     */ import org.eclipse.jface.preference.FieldEditorPreferencePage;
/*     */ import org.eclipse.jface.preference.IPreferenceStore;
/*     */ import org.eclipse.jface.preference.IntegerFieldEditor;
/*     */ import org.eclipse.jface.preference.RadioGroupFieldEditor;
/*     */ import org.eclipse.jface.util.PropertyChangeEvent;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchPreferencePage;
/*     */ 
/*     */ public class PgenPreferences extends FieldEditorPreferencePage
/*     */   implements IWorkbenchPreferencePage
/*     */ {
/*     */   public static final String P_RECOVERY_DIR = "PGEN_RECOVERY_DIR";
/*     */   public static final String V_RECOVERY_DIR = "/tmp";
/*     */   public static final String P_AUTO_FREQ = "PGEN_AUTOSAVE_FREQ";
/*     */   public static final String P_MAX_DIST = "PGEN_MAX_DISTANCE_TO_SELECT";
/*     */   public static final String P_PGEN_MODE = "PGEN_MODE";
/*     */   public static final String P_LAYER_LINK = "PGEN_LAYER_LINK";
/*     */   public static final String P_WORKING_DIR = "PGEN_WORKING_DIR";
/*  64 */   public static final String V_WORKING_DIR = System.getProperty("user.home");
/*     */   public static final String P_OPR_DIR = "PGEN_BASE_DIR";
/*  69 */   public static final String V_OPR_DIR = System.getenv("PGEN_OPR") != null ? 
/*  70 */     System.getenv("PGEN_OPR") : System.getProperty("user.home");
/*     */   public static final String P_COMP_COORD = "PGEN_COMP_COORD";
/*     */   public static final String CED_COMP_COORD = "ced/0;0;0|18.00;-137.00;58.00;-54.00";
/*     */   public static final String STR_COMP_COORD = "str/90;-97;0|19.00;-119.00;47.00;-56.00";
/*     */   private BooleanFieldEditor layerLink;
/*     */ 
/*     */   public PgenPreferences()
/*     */   {
/*  83 */     super(1);
/*  84 */     setPreferenceStore(Activator.getDefault().getPreferenceStore());
/*  85 */     setDescription("Specify PGEN preferences");
/*     */   }
/*     */ 
/*     */   public void createFieldEditors()
/*     */   {
/*  91 */     addField(new DirectoryFieldEditor("PGEN_BASE_DIR", 
/*  92 */       "&PGEN Base Directory:", getFieldEditorParent()));
/*     */ 
/*  94 */     addField(new DirectoryFieldEditor("PGEN_WORKING_DIR", 
/*  95 */       "&PGEN Working Directory:", getFieldEditorParent()));
/*     */ 
/*  97 */     addField(new DirectoryFieldEditor("PGEN_RECOVERY_DIR", 
/*  98 */       "&PGEN Recovery Directory:", getFieldEditorParent()));
/*     */ 
/* 100 */     IntegerFieldEditor freqEd = new IntegerFieldEditor("PGEN_AUTOSAVE_FREQ", 
/* 101 */       "&Auto Save frequency (min):", getFieldEditorParent(), 2);
/* 102 */     freqEd.setValidRange(1, 99);
/* 103 */     freqEd.setValidateStrategy(1);
/* 104 */     addField(freqEd);
/*     */ 
/* 106 */     addField(new IntegerFieldEditor("PGEN_MAX_DISTANCE_TO_SELECT", 
/* 107 */       "&Maximum Distance to be Selected (pixel):", 
/* 108 */       getFieldEditorParent(), 6));
/*     */ 
/* 110 */     String[][] modeOptions = { 
/* 111 */       { 
/* 112 */       "Single PGEN visible on all Map Editors (Legacy NMAP behavior)", 
/* 113 */       PgenUtil.PgenMode.SINGLE.toString() }, 
/* 114 */       { "Separate PGEN Data for each Map Editor", 
/* 115 */       PgenUtil.PgenMode.MULTIPLE.toString() } };
/*     */ 
/* 117 */     RadioGroupFieldEditor modeEditor = new RadioGroupFieldEditor(
/* 118 */       "PGEN_MODE", "&PGEN Mode:", 1, modeOptions, 
/* 119 */       getFieldEditorParent(), true);
/* 120 */     addField(modeEditor);
/*     */ 
/* 122 */     this.layerLink = new BooleanFieldEditor(
/* 123 */       "PGEN_LAYER_LINK", 
/* 124 */       "&Link Pgen Layers with Editor? (option valid in PGEN SINGLE mode only)", 
/* 125 */       0, getFieldEditorParent());
/* 126 */     addField(this.layerLink);
/*     */ 
/* 128 */     ComboFieldEditor projCombo = new ComboFieldEditor("PGEN_COMP_COORD", 
/* 129 */       "&PGEN Computational Coordinate:", new String[][] { 
/* 130 */       { "ced/0;0;0|18.00;-137.00;58.00;-54.00", "ced/0;0;0|18.00;-137.00;58.00;-54.00" }, 
/* 131 */       { "str/90;-97;0|19.00;-119.00;47.00;-56.00", "str/90;-97;0|19.00;-119.00;47.00;-56.00" } }, 
/* 132 */       getFieldEditorParent());
/* 133 */     addField(projCombo);
/*     */   }
/*     */ 
/*     */   public void init(IWorkbench workbench)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent event)
/*     */   {
/* 150 */     if ((event.getSource() instanceof RadioGroupFieldEditor))
/*     */     {
/* 152 */       String value = event.getNewValue().toString();
/* 153 */       if (value.equalsIgnoreCase(PgenUtil.PgenMode.SINGLE.toString()))
/* 154 */         this.layerLink.setEnabled(true, getFieldEditorParent());
/* 155 */       else if (value.equalsIgnoreCase(PgenUtil.PgenMode.MULTIPLE.toString()))
/* 156 */         this.layerLink.setEnabled(false, getFieldEditorParent());
/*     */     }
/* 158 */     else if ((event.getSource() instanceof DirectoryFieldEditor)) {
/* 159 */       String ovalue = event.getOldValue().toString();
/* 160 */       String prefname = ((DirectoryFieldEditor)event.getSource())
/* 161 */         .getPreferenceName();
/* 162 */       String opref = Activator.getDefault().getPreferenceStore()
/* 163 */         .getString(prefname);
/* 164 */       if (ovalue.equals(opref)) {
/* 165 */         String nvalue = event.getNewValue().toString();
/* 166 */         File nfile = new File(nvalue);
/* 167 */         if ((nfile.exists()) && (nfile.isDirectory()) && (nfile.canWrite()))
/* 168 */           Activator.getDefault().getPreferenceStore()
/* 169 */             .setValue(prefname, nvalue);
/*     */       }
/*     */     }
/* 172 */     else if ((event.getSource() instanceof ComboFieldEditor)) {
/* 173 */       GfaClip.getInstance().updateGfaBoundsInGrid();
/* 174 */     } else if ((event.getSource() instanceof IntegerFieldEditor)) {
/* 175 */       IntegerFieldEditor iField = (IntegerFieldEditor)event.getSource();
/* 176 */       if ((iField.getPreferenceName().equals("PGEN_AUTOSAVE_FREQ")) && 
/* 177 */         (!iField.isValid()))
/* 178 */         iField.setStringValue(getPreferenceStore()
/* 179 */           .getDefaultString("PGEN_AUTOSAVE_FREQ"));
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.PgenPreferences
 * JD-Core Version:    0.6.2
 */