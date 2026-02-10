/*
 */
package com.agricraft.agricore.registry;

import com.agricraft.agricore.plant.AgriSoil;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RlonRyan
 */
public class AgriSoils implements AgriLoadableRegistry<AgriSoil> {

    private final Map<String, AgriSoil> soils;

    public AgriSoils() {
        this.soils = new HashMap<>();
    }

    public boolean hasSoil(String id) {
        return this.soils.containsKey(id);
    }

    public boolean addSoil(AgriSoil plant) {
        return this.soils.putIfAbsent(plant.getId(), plant) == null;
    }

    public AgriSoil getSoil(String id) {
        return this.soils.get(id);
    }

    public Collection<AgriSoil> getAll() {
        return Collections.unmodifiableCollection(this.soils.values());
    }

    public void validate() {
        soils.entrySet().removeIf((e) -> (!e.getValue().validate()));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\nSoils:");
        for (AgriSoil plant : soils.values()) {
            sb.append("\n\t- Soil: ");
            sb.append(plant.toString().replaceAll("\n", "\n\t").trim());
        }
        return sb.append("\n").toString();
    }

    @Override
    public boolean acceptsElement(String filename) {
        return filename.toLowerCase().endsWith("_soil.json");
    }

    @Override
    public Class<AgriSoil> getElementClass() {
        return AgriSoil.class;
    }

    @Override
    public void registerElement(AgriSoil element) {
        this.addSoil(element);
    }

    @Override
    public void clearElements() {
        this.soils.clear();
    }

    @Override
    public int hashCode() {
        return this.soils.hashCode();
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
        final AgriSoils other = (AgriSoils) obj;
        return this.soils.equals(other.soils);
    }

}
