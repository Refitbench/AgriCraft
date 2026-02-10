/*
 */
package com.agricraft.agricore.util;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author RlonRyan
 */
@FunctionalInterface
public interface AgriConverter {

    <T> Optional<T> toStack(Class<T> token, String element, int meta, int amount, String tags, boolean ignoreMeta, boolean useOreDict, List<String> ignoreTags);

}
