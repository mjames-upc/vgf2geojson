/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*    */ import com.raytheon.uf.viz.core.drawables.IWireframeShape;
/*    */ import com.raytheon.uf.viz.core.drawables.PaintProperties;
/*    */ import com.raytheon.uf.viz.core.exception.VizException;
/*    */ import java.awt.Color;
/*    */ import java.io.PrintStream;
/*    */ import org.eclipse.swt.graphics.RGB;
/*    */ 
/*    */ public class LineDisplayElement
/*    */   implements IDisplayable
/*    */ {
/*    */   private IWireframeShape shape;
/*    */   private Color color;
/*    */   private float lineWidth;
/*    */ 
/*    */   public LineDisplayElement(IWireframeShape shape, Color color, float lineWidth)
/*    */   {
/* 52 */     this.shape = shape;
/* 53 */     this.color = color;
/* 54 */     this.lineWidth = lineWidth;
/*    */   }
/*    */ 
/*    */   public void dispose()
/*    */   {
/* 64 */     this.shape.dispose();
/*    */   }
/*    */ 
/*    */   public void draw(IGraphicsTarget target, PaintProperties paintProps)
/*    */   {
/* 75 */     RGB shapeColor = new RGB(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
/*    */     try {
/* 77 */       target.drawWireframeShape(this.shape, shapeColor, this.lineWidth);
/*    */     }
/*    */     catch (VizException ve) {
/* 80 */       System.out.println("Wireframe Shape not displayable  :(");
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.LineDisplayElement
 * JD-Core Version:    0.6.2
 */