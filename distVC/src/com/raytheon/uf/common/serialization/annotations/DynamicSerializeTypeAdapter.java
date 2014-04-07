package com.raytheon.uf.common.serialization.annotations;

import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Documented
public @interface DynamicSerializeTypeAdapter
{
  public abstract Class<? extends ISerializationTypeAdapter<?>> factory();
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.annotations.DynamicSerializeTypeAdapter
 * JD-Core Version:    0.6.2
 */