/*     */ package gov.noaa.nws.ncep.ui.pgen.display;
/*     */ 
/*     */ import com.raytheon.viz.ui.color.BackgroundColor;
/*     */ import com.raytheon.viz.ui.color.IBackgroundColorChangedListener.BGColorMode;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.Path2D.Double;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ComponentColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.PrintStream;
/*     */ import org.eclipse.swt.graphics.Device;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ import org.eclipse.swt.graphics.ImageData;
/*     */ import org.eclipse.swt.graphics.PaletteData;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ 
/*     */ public class SymbolImageUtil
/*     */ {
/*     */   public static final int INITIAL_IMAGE_SIZE = 12;
/*     */ 
/*     */   public static Image createIcon(Device device, String symbolId)
/*     */   {
/*  52 */     Image icon = null;
/*     */ 
/*  54 */     String[] ids = symbolId.split("\\|");
/*     */ 
/*  56 */     if (ids.length == 1)
/*     */     {
/*  60 */       BufferedImage img = createBufferedImage(symbolId);
/*  61 */       if (img != null) {
/*  62 */         ImageData idata = convertToSWT(img);
/*  63 */         icon = new Image(device, idata);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  70 */       BufferedImage img1 = createBufferedImage(ids[0]);
/*  71 */       BufferedImage img2 = createBufferedImage(ids[1]);
/*  72 */       if ((img1 != null) && (img2 != null)) {
/*  73 */         ImageData idata1 = convertToSWT(img1);
/*  74 */         ImageData idata2 = convertToSWT(img2);
/*  75 */         ImageCombiner cid = new ImageCombiner(idata1, idata2);
/*  76 */         icon = new Image(device, cid.getImageData());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  81 */     return icon;
/*     */   }
/*     */ 
/*     */   public static BufferedImage createBufferedImage(String symbolId)
/*     */   {
/*  91 */     return createBufferedImage(symbolId, 2.0D, 1.0F, false, new Color(0, 0, 0));
/*     */   }
/*     */ 
/*     */   public static BufferedImage createBufferedImage(String symbolId, double scale, float strokeWidth, boolean backgroundMask, Color symColor)
/*     */   {
/* 109 */     SymbolPattern pattern = null;
/* 110 */     SymbolPatternManager spl = SymbolPatternManager.getInstance();
/*     */     try {
/* 112 */       pattern = spl.getSymbolPattern(symbolId);
/*     */     }
/*     */     catch (SymbolPatternException spe) {
/* 115 */       System.out.println(spe.getMessage());
/* 116 */       return null;
/*     */     }
/*     */ 
/* 122 */     double sfactor = scale;
/* 123 */     int imageSize = 12 * (int)Math.ceil(sfactor);
/* 124 */     double center = imageSize * 0.5D;
/* 125 */     BufferedImage image = new BufferedImage(imageSize, imageSize, 6);
/*     */ 
/* 130 */     Graphics2D g2d = image.createGraphics();
/* 131 */     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 132 */     g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */     Path2D.Double mask;
/* 138 */     if (backgroundMask)
/*     */     {
/* 140 */       RGB bg = BackgroundColor.getActivePerspectiveInstance().getColor(IBackgroundColorChangedListener.BGColorMode.EDITOR);
/* 141 */       Color bgColor = new Color(bg.red, bg.green, bg.blue);
/* 142 */       g2d.setColor(bgColor);
/* 143 */       g2d.setStroke(new BasicStroke(strokeWidth + 5.0F, 1, 1));
/* 144 */       mask = new Path2D.Double();
/* 145 */       for (SymbolPart spart : pattern.getParts()) {
/* 146 */         Coordinate[] coords = spart.getPath();
/* 147 */         mask.reset();
/* 148 */         mask.moveTo(center + sfactor * coords[0].x, center - sfactor * coords[0].y);
/* 149 */         for (int i = 1; i < coords.length; i++) {
/* 150 */           mask.lineTo(center + sfactor * coords[i].x, center - sfactor * coords[i].y);
/*     */         }
/* 152 */         g2d.draw(mask);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 157 */     g2d.setColor(symColor);
/* 158 */     g2d.setPaint(symColor);
/* 159 */     g2d.setStroke(new BasicStroke(strokeWidth, 1, 1));
/*     */ 
/* 164 */     Path2D.Double path = new Path2D.Double();
/* 165 */     for (SymbolPart spart : pattern.getParts()) {
/* 166 */       Coordinate[] coords = spart.getPath();
/* 167 */       path.reset();
/* 168 */       path.moveTo(center + sfactor * coords[0].x, center - sfactor * coords[0].y);
/* 169 */       for (int i = 1; i < coords.length; i++) {
/* 170 */         path.lineTo(center + sfactor * coords[i].x, center - sfactor * coords[i].y);
/*     */       }
/* 172 */       if (spart.isFilled())
/*     */       {
/* 174 */         g2d.fill(path);
/*     */       }
/*     */       else {
/* 177 */         g2d.draw(path);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 182 */     image.flush();
/* 183 */     g2d.dispose();
/*     */ 
/* 185 */     return image;
/*     */   }
/*     */ 
/*     */   public static ImageData convertToSWT(BufferedImage bufferedImage)
/*     */   {
/* 193 */     if ((bufferedImage.getColorModel() instanceof ComponentColorModel)) {
/* 194 */       ComponentColorModel colorModel = (ComponentColorModel)bufferedImage
/* 195 */         .getColorModel();
/*     */ 
/* 197 */       PaletteData palette = new PaletteData(255, 65280, 16711680);
/* 198 */       ImageData data = new ImageData(bufferedImage.getWidth(), 
/* 199 */         bufferedImage.getHeight(), colorModel.getPixelSize(), 
/* 200 */         palette);
/* 201 */       if (colorModel.getTransparency() == 1) data.transparentPixel = 255;
/* 202 */       WritableRaster raster = bufferedImage.getRaster();
/* 203 */       int[] pixelArray = new int[4];
/* 204 */       for (int y = 0; y < data.height; y++) {
/* 205 */         for (int x = 0; x < data.width; x++) {
/* 206 */           raster.getPixel(x, y, pixelArray);
/* 207 */           int pixel = palette.getPixel(new RGB(pixelArray[0], 
/* 208 */             pixelArray[1], pixelArray[2]));
/* 209 */           data.setPixel(x, y, pixel);
/* 210 */           if (colorModel.getTransparency() == 3) data.setAlpha(x, y, pixelArray[3]);
/*     */         }
/*     */       }
/* 213 */       return data;
/* 214 */     }if ((bufferedImage.getColorModel() instanceof IndexColorModel)) {
/* 215 */       IndexColorModel colorModel = (IndexColorModel)bufferedImage
/* 216 */         .getColorModel();
/* 217 */       int size = colorModel.getMapSize();
/* 218 */       byte[] reds = new byte[size];
/* 219 */       byte[] greens = new byte[size];
/* 220 */       byte[] blues = new byte[size];
/* 221 */       colorModel.getReds(reds);
/* 222 */       colorModel.getGreens(greens);
/* 223 */       colorModel.getBlues(blues);
/* 224 */       RGB[] rgbs = new RGB[size];
/* 225 */       for (int i = 0; i < rgbs.length; i++) {
/* 226 */         rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, 
/* 227 */           blues[i] & 0xFF);
/*     */       }
/* 229 */       PaletteData palette = new PaletteData(rgbs);
/* 230 */       ImageData data = new ImageData(bufferedImage.getWidth(), 
/* 231 */         bufferedImage.getHeight(), colorModel.getPixelSize(), 
/* 232 */         palette);
/* 233 */       data.transparentPixel = colorModel.getTransparentPixel();
/* 234 */       WritableRaster raster = bufferedImage.getRaster();
/* 235 */       int[] pixelArray = new int[1];
/* 236 */       for (int y = 0; y < data.height; y++) {
/* 237 */         for (int x = 0; x < data.width; x++) {
/* 238 */           raster.getPixel(x, y, pixelArray);
/* 239 */           data.setPixel(x, y, pixelArray[0]);
/*     */         }
/*     */       }
/* 242 */       return data;
/*     */     }
/* 244 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.SymbolImageUtil
 * JD-Core Version:    0.6.2
 */