/*
 */
package com.agricraft.agricore.registry;

import com.agricraft.agricore.plant.AgriMutation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author RlonRyan
 */
public class AgriMutations implements AgriLoadableRegistry<AgriMutation> {

    private final Set<AgriMutation> mutations;

    public AgriMutations() {
        this.mutations = new HashSet<>();
    }

    public boolean hasMutation(AgriMutation mutation) {
        return this.mutations.contains(mutation);
    }

    public boolean hasMutation(String child) {
        for (AgriMutation m : mutations) {
            if (m.isChild(child)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMutation(String parent1, String parent2) {
        for (AgriMutation m : mutations) {
            if (m.isParent(parent1) && m.isParent(parent2)) {
                return true;
            }
        }
        return false;
    }

    public boolean addMutation(AgriMutation mutation) {
        return this.mutations.add(mutation);
    }

    public List<AgriMutation> getMutation(String child) {
        final List<AgriMutation> results = new ArrayList<>();
        this.mutations.forEach((m) -> {
            if (m.isChild(child)) {
                results.add(m);
            }
        });
        return results;
    }

    public List<AgriMutation> getMutation(String parent1, String parent2) {
        final List<AgriMutation> results = new ArrayList<>();
        for (AgriMutation m : mutations) {
            if (m.isParent(parent1) && m.isParent(parent2)) {
                results.add(m);
            }
        }
        return results;
    }

    public Collection<AgriMutation> getAll() {
        return Collections.unmodifiableCollection(this.mutations);
    }

    public void validate() {
        mutations.removeIf((e) -> (!e.validate()));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\nMutations:");
        for (AgriMutation mutation : mutations) {
            sb.append("\n\t- ").append(mutation).append("\n\t");
        }
        return sb.append("\n").toString();
    }

    @Override
    public boolean acceptsElement(String filename) {
        return filename.toLowerCase().endsWith("_mutation.json");
    }

    @Override
    public Class<AgriMutation> getElementClass() {
        return AgriMutation.class;
    }

    @Override
    public void registerElement(AgriMutation element) {
        this.addMutation(element);
    }

    public void clearElements() {
        this.mutations.clear();
    }

    @Override
    public int hashCode() {
        return this.mutations.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AgriMutations other = (AgriMutations) obj;
        return this.mutations.equals(other.mutations);
    }

}
