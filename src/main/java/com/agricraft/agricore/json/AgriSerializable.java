/*
 */
package com.agricraft.agricore.json;

/**
 * Marker interface for classes that can be serialized by AgriCore. Classes that
 * are annotated with this annotation must have a string "path" field.
 *
 * @author RlonRyan
 */
public interface AgriSerializable {

    boolean isEnabled();

    String getPath();

    void setPath(String path);

}
