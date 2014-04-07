/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Layer extends DECollection
/*     */ {
/*     */   private boolean onOff;
/*     */   private boolean monoColor;
/*     */   private Color color;
/*     */   private boolean filled;
/*     */   private boolean inUse;
/*     */   private boolean changeMade;
/*     */   private String inputFile;
/*     */   private String outputFile;
/*     */ 
/*     */   public Layer()
/*     */   {
/*  48 */     super("Default");
/*  49 */     this.onOff = true;
/*  50 */     this.monoColor = false;
/*  51 */     this.color = Color.yellow;
/*  52 */     this.filled = false;
/*  53 */     this.inUse = true;
/*  54 */     this.changeMade = false;
/*  55 */     this.inputFile = null;
/*  56 */     this.outputFile = null;
/*     */   }
/*     */ 
/*     */   public Layer(String name, boolean onOff, boolean mode, Color color, boolean fillMode, List<AbstractDrawableComponent> components)
/*     */   {
/*  69 */     super(name);
/*  70 */     this.onOff = onOff;
/*  71 */     this.monoColor = mode;
/*  72 */     this.color = color;
/*  73 */     this.filled = fillMode;
/*  74 */     this.compList = components;
/*  75 */     this.inUse = true;
/*  76 */     this.changeMade = false;
/*  77 */     this.inputFile = null;
/*  78 */     this.outputFile = null;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  85 */     return this.collectionName;
/*     */   }
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/*  92 */     this.collectionName = name;
/*     */   }
/*     */ 
/*     */   public boolean isOnOff()
/*     */   {
/*  99 */     return this.onOff;
/*     */   }
/*     */ 
/*     */   public void setOnOff(boolean onOff)
/*     */   {
/* 106 */     this.onOff = onOff;
/*     */   }
/*     */ 
/*     */   public boolean isMonoColor()
/*     */   {
/* 113 */     return this.monoColor;
/*     */   }
/*     */ 
/*     */   public void setMonoColor(boolean mode)
/*     */   {
/* 120 */     this.monoColor = mode;
/*     */   }
/*     */ 
/*     */   public Color getColor()
/*     */   {
/* 127 */     return this.color;
/*     */   }
/*     */ 
/*     */   public void setColor(Color color)
/*     */   {
/* 134 */     this.color = color;
/*     */   }
/*     */ 
/*     */   public boolean isFilled()
/*     */   {
/* 141 */     return this.filled;
/*     */   }
/*     */ 
/*     */   public void setFilled(boolean fillMode)
/*     */   {
/* 148 */     this.filled = fillMode;
/*     */   }
/*     */ 
/*     */   public void setInUse(boolean isInUse)
/*     */   {
/* 155 */     this.inUse = isInUse;
/*     */   }
/*     */ 
/*     */   public boolean isInUse()
/*     */   {
/* 162 */     return this.inUse;
/*     */   }
/*     */ 
/*     */   public void setChangeMade(boolean changeMade)
/*     */   {
/* 169 */     this.changeMade = changeMade;
/*     */   }
/*     */ 
/*     */   public boolean isChangeMade()
/*     */   {
/* 176 */     return this.changeMade;
/*     */   }
/*     */ 
/*     */   public void setInputFile(String inputFile)
/*     */   {
/* 183 */     this.inputFile = inputFile;
/*     */   }
/*     */ 
/*     */   public String getInputFile()
/*     */   {
/* 190 */     return this.inputFile;
/*     */   }
/*     */ 
/*     */   public void setOutputFile(String outputFile)
/*     */   {
/* 197 */     this.outputFile = outputFile;
/*     */   }
/*     */ 
/*     */   public String getOutputFile()
/*     */   {
/* 204 */     return this.outputFile;
/*     */   }
/*     */ 
/*     */   public List<AbstractDrawableComponent> getDrawables()
/*     */   {
/* 211 */     return this.compList;
/*     */   }
/*     */ 
/*     */   public void setDrawables(List<AbstractDrawableComponent> drawables)
/*     */   {
/* 218 */     for (AbstractDrawableComponent adc : drawables) {
/* 219 */       adc.setParent(this);
/*     */     }
/* 221 */     this.compList = drawables;
/*     */   }
/*     */ 
/*     */   public void removeElement(int index)
/*     */   {
/* 228 */     this.compList.remove(index);
/*     */   }
/*     */ 
/*     */   public void removeAllElements()
/*     */   {
/* 235 */     this.compList.clear();
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent getElement(int index)
/*     */   {
/* 242 */     return (AbstractDrawableComponent)this.compList.get(index);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 249 */     StringBuilder result = new StringBuilder("\n");
/* 250 */     result.append("name:\t\t" + getName() + "\n");
/* 251 */     result.append("onOff:\t\t" + this.onOff + "\n");
/* 252 */     result.append("colorMode:\t\t" + this.monoColor + "\n");
/* 253 */     result.append("color:\t\t" + this.color + "\n");
/* 254 */     result.append("fillMode:\t\t" + this.filled + "\n");
/* 255 */     result.append("inUse:\t\t" + this.inUse + "\n");
/* 256 */     result.append("changeMade:\t\t" + this.changeMade + "\n");
/* 257 */     result.append("inputFile:\t\t" + this.inputFile + "\n");
/* 258 */     result.append("outputFile:\t\t" + this.outputFile + "\n");
/*     */ 
/* 260 */     result.append("\nTotal DrawableElements:\t" + this.compList.size() + "\n");
/*     */ 
/* 262 */     for (int ii = 0; ii < this.compList.size(); ii++) {
/* 263 */       result.append("\nDrawable No.  " + ii + ":\n");
/* 264 */       result.append(getElement(ii));
/* 265 */       result.append("\n");
/*     */     }
/*     */ 
/* 268 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public Layer copy()
/*     */   {
/* 277 */     Layer lyr = new Layer();
/*     */ 
/* 279 */     Iterator iterator = getComponentIterator();
/*     */ 
/* 281 */     while (iterator.hasNext()) {
/* 282 */       lyr.addElement(((AbstractDrawableComponent)iterator.next()).copy());
/*     */     }
/*     */ 
/* 285 */     iterator = lyr.getComponentIterator();
/* 286 */     while (iterator.hasNext()) {
/* 287 */       ((AbstractDrawableComponent)iterator.next()).setParent(lyr);
/*     */     }
/*     */ 
/* 290 */     lyr.setPgenCategory(getPgenCategory());
/* 291 */     lyr.setPgenType(getPgenType());
/* 292 */     lyr.setParent(getParent());
/*     */ 
/* 294 */     lyr.setName(getName());
/* 295 */     lyr.setOnOff(isOnOff());
/*     */ 
/* 297 */     lyr.setMonoColor(isMonoColor());
/* 298 */     lyr.setColor(getColor());
/* 299 */     lyr.setFilled(isFilled());
/* 300 */     lyr.setInUse(isInUse());
/* 301 */     lyr.setChangeMade(isChangeMade());
/* 302 */     lyr.setInputFile(getInputFile());
/* 303 */     lyr.setOutputFile(getOutputFile());
/*     */ 
/* 305 */     return lyr;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.Layer
 * JD-Core Version:    0.6.2
 */