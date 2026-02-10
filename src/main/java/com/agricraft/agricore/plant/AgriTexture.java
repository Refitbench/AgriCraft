/*
 */
package com.agricraft.agricore.plant;

import com.agricraft.agricore.core.AgriCore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author RlonRyan
 */
public class AgriTexture {

    private AgriRenderType render_type = AgriRenderType.HASH;
    private String seed_texture = "minecraft:items/seeds_wheat";

    private final String[] plant_textures = new String[]{
        "minecraft:blocks/wheat_stage_0",
        "minecraft:blocks/wheat_stage_1",
        "minecraft:blocks/wheat_stage_2",
        "minecraft:blocks/wheat_stage_3",
        "minecraft:blocks/wheat_stage_4",
        "minecraft:blocks/wheat_stage_5",
        "minecraft:blocks/wheat_stage_6",
        "minecraft:blocks/wheat_stage_7"
    };

    // GSON Tricker
    public AgriTexture() {
    }

    public AgriTexture(AgriRenderType render_type, String seed_texture, String[] plant_textures) {

        this.render_type = render_type;
        this.seed_texture = seed_texture;

        System.arraycopy(plant_textures, 0, this.plant_textures, 0, plant_textures.length < this.plant_textures.length ? plant_textures.length : this.plant_textures.length);

        // Distribute the textures.
        String last = "NO TEXTURE!";
        for (int i = 0; i < this.plant_textures.length; i++) {
            if (this.plant_textures[i] == null) {
                this.plant_textures[i] = last;
            } else {
                last = this.plant_textures[i];
            }
        }

    }

    public AgriRenderType getRenderType() {
        return render_type;
    }

    public String getSeedTexture() {
        return seed_texture;
    }
    
    public int getGrowthStages() {
        return plant_textures.length;
    }

    public List<String> getAllTextures() {
        final List<String> tex = new ArrayList<>(plant_textures.length + 1);
        tex.addAll(Arrays.asList(plant_textures));
        tex.add(seed_texture);
        return tex;
    }

    public String[] getPlantTextures() {
        return Arrays.copyOf(plant_textures, plant_textures.length);
    }

    public String getPlantTexture(int stage) {
        return this.plant_textures[stage % this.plant_textures.length];
    }

    public boolean validate() {
        if (!AgriCore.getValidator().isValidTexture(seed_texture)) {
            AgriCore.getCoreLogger().debug("Invalid AgriTexture! Invalid Seed Texture: \"{0}\"!", seed_texture);
            return false;
        }
        for (String texture : plant_textures) {
            if (!AgriCore.getValidator().isValidTexture(texture)) {
                AgriCore.getCoreLogger().debug("Invalid AgriTexture! Invalid Plant Texture: \"{0}\"!", texture);
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("Icon Set:\n");
        sb.append("\t- Seed Texture: ").append(this.seed_texture).append("\n");
        sb.append("\t- Render Type: ").append(this.render_type).append("\n");
        sb.append("\t- Supported Number of Growth Stages:").append(this.plant_textures.length).append("\n");
        sb.append("\t- Plant Textures:\n");
        for (int i = 0; i < this.plant_textures.length; i++) {
            sb.append("\t\t- ").append(i).append(" : ").append(this.plant_textures[i]).append("\n");
        }
        return sb.toString();
    }

}
