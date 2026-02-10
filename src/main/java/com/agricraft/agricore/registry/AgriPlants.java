/*
 */
package com.agricraft.agricore.registry;

import com.agricraft.agricore.plant.AgriPlant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RlonRyan
 */
public class AgriPlants implements AgriLoadableRegistry<AgriPlant> {

    private final Map<String, AgriPlant> plants;

    public AgriPlants() {
        this.plants = new HashMap<>();
    }

    public boolean hasPlant(String id) {
        return this.plants.containsKey(id);
    }

    public boolean addPlant(AgriPlant plant) {
        return this.plants.putIfAbsent(plant.getId(), plant) == null;
    }

    public AgriPlant getPlant(String id) {
        return this.plants.get(id);
    }

    public Collection<AgriPlant> getAll() {
        return Collections.unmodifiableCollection(this.plants.values());
    }

    public void validate() {
        plants.entrySet().removeIf((e) -> (!e.getValue().validate()));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\nPlants:");
        for (AgriPlant plant : plants.values()) {
            sb.append("\n\t- Plant: ");
            sb.append(plant.toString().replaceAll("\n", "\n\t").trim());
        }
        return sb.append("\n").toString();
    }

    @Override
    public boolean acceptsElement(String filename) {
        return filename.toLowerCase().endsWith("_plant.json");
    }

    @Override
    public Class<AgriPlant> getElementClass() {
        return AgriPlant.class;
    }

    @Override
    public void registerElement(AgriPlant element) {
        this.addPlant(element);
    }

    @Override
    public void clearElements() {
        this.plants.clear();
    }

    @Override
    public int hashCode() {
        return this.plants.hashCode();
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
        final AgriPlants other = (AgriPlants) obj;
        return this.plants.equals(other.plants);
    }

}
