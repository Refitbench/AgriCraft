# AgriCraft - Maintained by Refitbench

## Refitbench is a suborganization of [CleanroomMC](https://cleanroommc.com) that concentrates on maintaining, developing old open-source mods on 1.12.2.

### Changes made currently:

#### General Changes
- Fixed lighting on blocks in-world and in-GUIs.
- Fixed recipes not differentiated from each other, and being grouped in JEI/HEI together.
- Removed Reflections library which incurred a lot of memory usage in instances.
- Fixed Biomes O' Plenty Farmland/Soil Compatibility
- Proper collision for water tanks

#### Technical Changes
- AIO. No need to download InfinityLib, and packs AgriCore, AgriPlants all in one repository.
- Relocated JOML from `org.joml` => `com.infinityraider.org.joml`
- Code Cleanups

### Notes:
- Generally advise to refresh configurations when switching from the old AgriCraft to this **refitted version**!
  - (And look out for patch notes, some versions may require you to refresh configurations to see the changes)
- If VintageFix is installed, turn off `mixin.mod_opts.agricraft` in their configuration.

Visit the [mod page for more information on mod mechanics](https://www.curseforge.com/minecraft/mc-mods/agricraft)
