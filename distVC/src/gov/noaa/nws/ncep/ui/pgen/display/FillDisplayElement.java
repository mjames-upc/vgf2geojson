/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import com.raytheon.uf.viz.core.IGraphicsTarget;
/*    */ import com.raytheon.uf.viz.core.drawables.IShadedShape;
/*    */ import com.raytheon.uf.viz.core.drawables.PaintProperties;
/*    */ import com.raytheon.uf.viz.core.exception.VizException;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class FillDisplayElement
/*    */   implements IDisplayable
/*    */ {
/*    */   private IShadedShape shape;
/*    */   private float alpha;
/*    */ 
/*    */   public FillDisplayElement(IShadedShape shape, float alpha)
/*    */   {
/* 42 */     this.shape = shape;
/* 43 */     this.alpha = alpha;
/*    */   }
/*    */ 
/*    */   public void dispose()
/*    */   {
/* 53 */     this.shape.dispose();
/*    */   }
/*    */ 
/*    */   public void draw(IGraphicsTarget target, PaintProperties paintProps)
/*    */   {
/*    */     try
/*    */     {
/* 66 */       target.drawShadedShape(this.shape, this.alpha);
/*    */     }
/*    */     catch (VizException ve) {
/* 69 */       System.out.println("Shaded Shape not displayable  :(");
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.FillDisplayElement
 * JD-Core Version:    0.6.2
 */