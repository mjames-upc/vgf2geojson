/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ 
/*     */ public class PgenCommandManager
/*     */ {
/*     */   private Stack<PgenCommand> undo;
/*     */   private Stack<PgenCommand> redo;
/*     */   private Set<CommandStackListener> listeners;
/*     */ 
/*     */   public PgenCommandManager()
/*     */   {
/*  44 */     this.undo = new Stack();
/*  45 */     this.redo = new Stack();
/*  46 */     this.listeners = new HashSet();
/*     */   }
/*     */ 
/*     */   public void addCommand(PgenCommand command)
/*     */   {
/*     */     try
/*     */     {
/*  60 */       command.execute();
/*     */ 
/*  65 */       this.undo.push(command);
/*  66 */       if (!this.redo.isEmpty())
/*  67 */         this.redo.clear();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  71 */       e.printStackTrace();
/*     */     }
/*     */     finally
/*     */     {
/*  75 */       stacksChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void undo()
/*     */   {
/*  87 */     if (this.undo.isEmpty()) return;
/*     */ 
/*  92 */     PgenCommand cmd = (PgenCommand)this.undo.pop();
/*     */     try
/*     */     {
/*  98 */       cmd.undo();
/*  99 */       this.redo.push(cmd);
/*     */     }
/*     */     catch (Exception e) {
/* 102 */       e.printStackTrace();
/*     */     }
/*     */     finally
/*     */     {
/* 106 */       stacksChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void redo()
/*     */   {
/* 117 */     if (this.redo.isEmpty()) return;
/*     */ 
/* 122 */     PgenCommand cmd = (PgenCommand)this.redo.pop();
/*     */     try
/*     */     {
/* 128 */       cmd.execute();
/* 129 */       this.undo.push(cmd);
/*     */     }
/*     */     catch (Exception e) {
/* 132 */       e.printStackTrace();
/*     */     }
/*     */     finally
/*     */     {
/* 136 */       stacksChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flushStacks()
/*     */   {
/* 145 */     this.undo.clear();
/* 146 */     this.redo.clear();
/* 147 */     this.listeners.clear();
/*     */   }
/*     */ 
/*     */   public void clearStacks()
/*     */   {
/* 154 */     this.undo.clear();
/* 155 */     this.redo.clear();
/*     */   }
/*     */ 
/*     */   public void addStackListener(CommandStackListener clisten)
/*     */   {
/* 163 */     this.listeners.add(clisten);
/*     */ 
/* 165 */     clisten.stacksUpdated(this.undo.size(), this.redo.size());
/*     */   }
/*     */ 
/*     */   public void removeStackListener(CommandStackListener clisten)
/*     */   {
/* 173 */     this.listeners.remove(clisten);
/*     */   }
/*     */ 
/*     */   private void stacksChanged()
/*     */   {
/* 181 */     for (CommandStackListener clist : this.listeners)
/* 182 */       clist.stacksUpdated(this.undo.size(), this.redo.size());
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.PgenCommandManager
 * JD-Core Version:    0.6.2
 */