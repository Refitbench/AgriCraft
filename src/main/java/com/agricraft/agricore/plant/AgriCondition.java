/*
 */
package com.agricraft.agricore.plant;

import com.agricraft.agricore.core.AgriCore;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author Ryan
 */
public class AgriCondition extends AgriStack {

	protected final int amount;
	protected final int min_x;
	protected final int min_y;
	protected final int min_z;
	protected final int max_x;
	protected final int max_y;
	protected final int max_z;
	
	public AgriCondition() {
		super();
		this.amount = 1;
		this.min_x = 0;
		this.min_y = -2;
		this.min_z = 0;
		this.max_x = 0;
		this.max_y = -2;
		this.max_z = 0;
	}

	public AgriCondition(int amount, int min_x, int min_y, int min_z, int max_x, int max_y, int max_z, String item, int meta, boolean ignoreMeta, boolean useOreDict, String tags, String... ignoreTags) {
		this(amount, min_x, min_y, min_z, max_x, max_y, max_z, item, meta, ignoreMeta, useOreDict, tags, Arrays.asList(ignoreTags));
	}

	public AgriCondition(int amount, int min_x, int min_y, int min_z, int max_x, int max_y, int max_z, String item, int meta, boolean ignoreMeta, boolean useOreDict, String tags, List<String> ignoreTags) {
		super(item, meta, ignoreMeta, useOreDict, tags, ignoreTags);
		this.amount = amount;
		this.min_x = min_x;
		this.min_y = min_y;
		this.min_z = min_z;
		this.max_x = max_x;
		this.max_y = max_y;
		this.max_z = max_z;
	}

	public int getMinX() {
		return min_x;
	}

	public int getMinY() {
		return min_y;
	}

	public int getMinZ() {
		return min_z;
	}

	public int getMaxX() {
		return max_x;
	}

	public int getMaxY() {
		return max_y;
	}

	public int getMaxZ() {
		return max_z;
	}

	@Override
	public boolean validate() {
		if (!super.validate()) {
			AgriCore.getCoreLogger().info("Invalid Condition: Invalid Item: {0}!", item);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Attempts to create a stack of the desired class, wrapped in an Optional.
	 * It uses `amount` from this instance for the stack size, instead of 1.
	 */
	@Override
	public <T> Optional<T> toStack(Class<T> token) {
		return this.toStack(token, amount);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("\nCondition:");
		sb.append("\n\t- ").append(super.toString().replaceAll("\n", "\n\t").trim());
		sb.append("\n\t- Amount: ").append(amount);
		sb.append("\n\t- Min Coord: (").append(min_x).append(", ").append(min_y).append(", ").append(min_z).append(")");
		sb.append("\n\t- Max Coord: (").append(max_x).append(", ").append(max_y).append(", ").append(max_z).append(")");
		return sb.toString();
	}

}
