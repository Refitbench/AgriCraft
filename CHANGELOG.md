# Changelog

## [2.18.0] - 2026-05-31

### Added
- Configuration for how water pads are created, that can be used to circumvent overlapping mod interactions
- Configuration for bonemealing AgriCraft crops
- Language keys for seeds, crops, soils, tooltips and player messages

### Changed
- Maximum crop stat allowed value now increased to max byte

### Fixed
- Bonemealing logic
- Right-clicking logic, now streamlined and optimized

## [2.17.3] - 2026-05-17

### Fixed
- Ambiguity introduced when reobfuscation (IItemHandler <-> IInventory: getStackInSlot)

## [2.17.2] - 2026-03-23

### Fixed
- Fixed normal irrigation channel recipe
- Fixed normal/full irrigation channel conversion recipes not properly copying NBT

## [2.17.1] - 2026-03-16

### Fixed
- Check if a feature is enabled before registering it's recipes

## [2.17.0] - 2026-02-17

### Changed
- Optimized suppression of model errors

### Fixed
- Collision not added on the bottom side of tanks, now it is only ignored when there is another tank below it

### Added
- WAILA/HWYLA support for crop sticks
- TerraFirmaCraft and TFC Rocks+ native soil support

## [2.16.0] - 2026-02-16

### Fixed
- Ambient Occlusion on some selected blocks

### Added
- Proper collision for water tanks

## [2.15.0] - 2026-02-12

### Fixed
- Description not showing up in mod information
- Biomes O' Plenty Compatibility. (PLEASE REFRESH AGRICRAFT CONFIGS!)

## [2.14.0] - 2026-02-12

### Changed
- Cleaning up wood type registrations

### Fixed
- Some recipes still being duplicates

## [2.13.0] - 2026-02-10

### Changed
- Combined InfinityLib, AgriCore, AgriPlants, AgriCraft altogether

### Fixed
- Lighting on blocks in-world and in-GUIs.
- Recipes not differentiated from each other, and being grouped in JEI/HEI together.

### Removed
- Reflections library which incurred a lot of memory usage in instances.