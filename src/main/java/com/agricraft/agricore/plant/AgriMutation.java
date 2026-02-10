/*
 */
package com.agricraft.agricore.plant;

import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.json.AgriSerializable;
import java.util.Random;

/**
 *
 * @author RlonRyan
 */
public class AgriMutation implements AgriSerializable {

    private String path;
    
    private final boolean enabled;

    private final double chance;

    private final String child;

    private final String parent1;
    private final String parent2;

    public AgriMutation() {
        this.enabled = false;
        this.path = "default/default_mutation.json";
        this.chance = 0;
        this.child = "carrot_plant";
        this.parent1 = "wheat_plant";
        this.parent2 = "potato_plant";
    }

    public AgriMutation(double chance, String child, String parent1, String parent2, String path, boolean enabled) {
        this.enabled = enabled;
        this.path = path;
        this.chance = chance;
        this.child = child;
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    public boolean isChild(String child) {
        return this.child.equals(child);
    }

    public boolean isParent(String parent) {
        return this.parent1.equals(parent) || this.parent2.equals(parent);
    }

    public double getChance() {
        return chance;
    }

    public AgriPlant getChild() {
        return AgriCore.getPlants().getPlant(child);
    }

    public AgriPlant getParent1() {
        return AgriCore.getPlants().getPlant(parent1);
    }

    public AgriPlant getParent2() {
        return AgriCore.getPlants().getPlant(parent2);
    }

    public boolean randomMutate(final Random rand) {
        return (chance > rand.nextDouble());
    }

    public boolean validate() {
        if (!this.enabled) {
            AgriCore.getCoreLogger().info("Disabled Mutation: {0}", this);
            return false;
        } else if (!AgriCore.getPlants().hasPlant(child)) {
            AgriCore.getCoreLogger().info("Invalid Mutation: Invalid Child: \"{0}\"!\n{1}", child, this);
            return false;
        } else if (!AgriCore.getPlants().hasPlant(parent1)) {
            AgriCore.getCoreLogger().info("Invalid Mutation: Invalid Parent 1: \"{0}\"!\n{1}", parent1, this);
            return false;
        } else if (!AgriCore.getPlants().hasPlant(parent2)) {
            AgriCore.getCoreLogger().info("Invalid Mutation: Invalid Parent 2: \"{0}\"!\n{1}", parent2, this);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nMutation:\n");
        sb.append("\t- Parents:\n");
        sb.append("\t\t- Parent 1: ").append(parent1).append("\n");
        sb.append("\t\t- Parent 2: ").append(parent2).append("\n");
        sb.append("\t- Child: ").append(child).append("\n");
        sb.append("\t- Chance: ").append(chance).append("\n");
        return sb.toString();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

}
