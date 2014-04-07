/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import org.eclipse.jface.resource.CompositeImageDescriptor;
/*    */ import org.eclipse.swt.graphics.ImageData;
/*    */ import org.eclipse.swt.graphics.Point;
/*    */ 
/*    */ public class ImageCombiner extends CompositeImageDescriptor
/*    */ {
/*    */   ImageData image1;
/*    */   ImageData image2;
/*    */ 
/*    */   public ImageCombiner(ImageData image1, ImageData image2)
/*    */   {
/* 31 */     this.image1 = image1;
/* 32 */     this.image2 = image2;
/*    */   }
/*    */ 
/*    */   public void drawComposite()
/*    */   {
/*    */   }
/*    */ 
/*    */   protected void drawCompositeImage(int width, int height)
/*    */   {
/* 48 */     drawImage(this.image1, 0, 0);
/* 49 */     drawImage(this.image2, this.image1.width, 0);
/*    */   }
/*    */ 
/*    */   protected Point getSize()
/*    */   {
/* 59 */     Point pt = new Point(this.image1.width + this.image2.width, this.image1.height);
/* 60 */     return pt;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.ImageCombiner
 * JD-Core Version:    0.6.2
 */