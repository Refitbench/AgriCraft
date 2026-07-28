package com.infinityraider.agricraft.world;

import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;
import com.infinityraider.agricraft.api.v1.seed.AgriSeed;
import com.infinityraider.agricraft.api.v1.stat.IAgriStat;
import com.infinityraider.agricraft.init.AgriBlocks;
import com.infinityraider.agricraft.reference.AgriCraftConfig;
import com.infinityraider.agricraft.tiles.TileEntityCrop;
import com.infinityraider.agricraft.tiles.analyzer.TileEntitySeedAnalyzer;
import com.infinityraider.agricraft.utility.WorldGenerationHelper;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static net.minecraft.block.BlockLog.LOG_AXIS;

public class StructureGreenHouse extends StructureVillagePieces.House1 {

    // Structure dimensions
    private static final int xSize = 17;
    private static final int ySize = 8;
    private static final int zSize = 11;

    private int averageGroundLevel = -1;

    public StructureGreenHouse() {}

    public StructureGreenHouse(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox sbb, EnumFacing facing) {
        super(start, type, rand, sbb, facing);
        this.setCoordBaseMode(facing);
        this.boundingBox = sbb;
    }

    public static StructureGreenHouse buildComponent(StructureVillagePieces.Start villagePiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
        StructureBoundingBox sbb = StructureBoundingBox.getComponentToAddBoundingBox(
                p1, p2, p3, 0, 0, 0, xSize, ySize, zSize, facing
        );
        return canVillageGoDeeper(sbb) && StructureComponent.findIntersecting(pieces, sbb) == null
                ? new StructureGreenHouse(villagePiece, p5, random, sbb, facing)
                : null;
    }

    @Override
    public boolean addComponentParts(World world, Random rand, StructureBoundingBox boundingBox) {

        // Level off ground
        if(this.averageGroundLevel < 0) {
            this.averageGroundLevel = this.getAverageGroundLevel(world, boundingBox);
            if(this.averageGroundLevel < 0) {
                return true;
            }
            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + ySize - 1, 0);
            this.boundingBox.offset(0, -1, 0);
        }

        // Fill with air
        this.fillWithAir(world, boundingBox, 0, 0, 0, xSize - 1, ySize, zSize - 1);

        // Cobblestone base
        IBlockState cobblestone = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
        this.fillWithBlocks(world, boundingBox, 0, 0, 0, xSize - 1, 0, zSize - 1, cobblestone, cobblestone, false);

        // Ring of gravel
        IBlockState gravel = this.getBiomeSpecificBlockState(Blocks.GRAVEL.getDefaultState());
        this.fillWithBlocks(world, boundingBox, 0, 1, 0, xSize - 1, 1, 0, gravel, gravel, false);
        this.fillWithBlocks(world, boundingBox, 0, 1, 0, 0, 1, zSize - 1, gravel, gravel, false);
        this.fillWithBlocks(world, boundingBox, 0, 1, zSize - 1, xSize - 1, 1, zSize - 1, gravel, gravel, false);
        this.fillWithBlocks(world, boundingBox, xSize - 1, 1, 0, xSize - 1, 1, zSize - 1, gravel, gravel, false);

        // Cobblestone foundations
        this.fillWithBlocks(world, boundingBox, 1, 1, 1, 1, 1, 4, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 1, 1, 6, 1, 1, 9, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 15, 1, 1, 15, 1, 4, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 15, 1, 6, 15, 1, 9, cobblestone, cobblestone, false);
        this.setBlockState(world, cobblestone,2, 1, 1, boundingBox);
        this.setBlockState(world, cobblestone,8, 1, 1, boundingBox);
        this.setBlockState(world, cobblestone,14, 1, 5, boundingBox);
        this.setBlockState(world, cobblestone,2, 1, 9, boundingBox);
        this.setBlockState(world, cobblestone,8, 1, 9, boundingBox);
        this.setBlockState(world, cobblestone,14, 1, 9, boundingBox);

        // Place slabs
        IBlockState stoneSlab = Blocks.DOUBLE_STONE_SLAB.getDefaultState();
        this.setBlockState(world, stoneSlab, 1, 1, 5, boundingBox);
        this.setBlockState(world, stoneSlab, 15, 1, 5, boundingBox);

        this.fillWithBlocks(world, boundingBox, 2, 1, 2, 2, 1, 8, stoneSlab, stoneSlab, false);
        this.fillWithBlocks(world, boundingBox, 8, 1, 2, 8, 1, 8, stoneSlab, stoneSlab, false);
        this.fillWithBlocks(world, boundingBox, 14, 1, 2, 14, 1, 8, stoneSlab, stoneSlab, false);
        this.fillWithBlocks(world, boundingBox, 2, 1, 2, 14, 1, 2, stoneSlab, stoneSlab, false);
        this.fillWithBlocks(world, boundingBox, 2, 1, 8, 14, 1, 8, stoneSlab, stoneSlab, false);

        // Place water
        IBlockState water = Blocks.WATER.getDefaultState();
        this.fillWithBlocks(world, boundingBox, 3, 1, 1, 7, 1, 1, water, water, false);
        this.fillWithBlocks(world, boundingBox, 9, 1, 1, 10, 1, 1, water, water, false);
        this.fillWithBlocks(world, boundingBox, 3, 1, 9, 7, 1, 9, water, water, false);
        this.fillWithBlocks(world, boundingBox, 9, 1, 9, 13, 1, 9, water, water, false);
        this.setBlockState(world, water, 13, 1, 6, boundingBox);

        // Place farmland
        IBlockState farmland = Blocks.FARMLAND.getDefaultState()
                .withProperty(BlockFarmland.MOISTURE, 7);
        this.fillWithBlocks(world, boundingBox, 3, 1, 3, 7, 1, 7, farmland, farmland, false);
        this.fillWithBlocks(world, boundingBox, 9, 1, 3, 13, 1, 7, farmland, farmland, false);

        // Place standing logs
        IBlockState log = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
        this.fillWithBlocks(world, boundingBox, 1, 2, 1, 1, 6, 1, log, log, false);
        this.fillWithBlocks(world, boundingBox, 8, 2, 1, 8, 6, 1, log, log, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 1, 15, 6, 1, log, log, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 4, 1, 5, 4, log, log, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 4, 15, 5, 4, log, log, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 6, 1, 5, 6, log, log, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 6, 15, 5, 6, log, log, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 9, 1, 6, 9, log, log, false);
        this.fillWithBlocks(world, boundingBox, 8, 2, 9, 8, 6, 9, log, log, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 9, 15, 6, 9, log, log, false);

        IBlockState logXAxis = log;
        IBlockState logZAxis = log;

        if(log.getBlock() instanceof BlockLog) {
            logXAxis = logXAxis.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
            logZAxis = logZAxis.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
        }

        // Logs along x-axis
        this.fillWithBlocks(world, boundingBox, 2, 6, 1, 7, 6, 1, logXAxis, logXAxis, false);
        this.fillWithBlocks(world, boundingBox, 9, 6, 1, 14, 6, 1, logXAxis, logXAxis, false);
        this.fillWithBlocks(world, boundingBox, 2, 6, 9, 7, 6, 9, logXAxis, logXAxis, false);
        this.fillWithBlocks(world, boundingBox, 9, 6, 9, 14, 6, 9, logXAxis, logXAxis, false);
        // Logs along z-axis
        this.fillWithBlocks(world, boundingBox, 1, 6, 2, 1, 6, 8, logZAxis, logZAxis, false);
        this.fillWithBlocks(world, boundingBox, 8, 6, 2, 8, 6, 8, logZAxis, logZAxis, false);
        this.fillWithBlocks(world, boundingBox, 15, 6, 2, 15, 6, 8, logZAxis, logZAxis, false);
        this.setBlockState(world, logZAxis, 1, 4, 5, boundingBox); // ???
        this.setBlockState(world, logZAxis, 15, 4, 5, boundingBox);

        // Cobblestone walls
        this.fillWithBlocks(world, boundingBox, 2, 2, 1, 7, 2, 1, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 9, 2, 1, 14, 2, 1, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 2, 1, 2, 3, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 7, 1, 2, 8, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 2, 15, 2, 3, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 7, 15, 2, 8, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 2, 2, 9, 7, 2, 9, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 9, 2, 9, 14, 2, 9, cobblestone, cobblestone, false);

        // Place glass
        IBlockState glass = Blocks.GLASS.getDefaultState();
        this.fillWithBlocks(world, boundingBox, 1, 3, 2, 1, 5, 3, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 1, 3, 7, 1, 5, 8, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 15, 3, 2, 15, 5, 3, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 15, 3, 7, 15, 5, 8, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 2, 3, 1, 7, 5, 1, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 9, 3, 1, 14, 5, 1, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 2, 3, 9, 7, 5, 9, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 9, 3, 9, 14, 5, 9, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 2, 6, 2, 7, 6, 8, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 9, 6, 2, 14, 6, 8, glass, glass, false);
        this.setBlockState(world, glass, 1, 5, 5, boundingBox);
        this.setBlockState(world, glass, 15, 5, 5, boundingBox);

        // Place doors (not using #createVillageDoor because it forces them to face NORTH)
        BlockDoor door = this.biomeDoor();
        this.generateDoor(world, boundingBox, rand, 1, 2, 5, EnumFacing.EAST, door);
        this.generateDoor(world, boundingBox, rand, 15, 2, 5, EnumFacing.WEST, door);

        // Place torches
        this.placeTorch(world, EnumFacing.WEST, 0, 4, 1, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 0, 4, 4, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 0, 4, 6, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 0, 4, 9, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 14, 4, 4, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 14, 4, 6, boundingBox);

        this.placeTorch(world, EnumFacing.SOUTH, 1, 4, 0, boundingBox);
        this.placeTorch(world, EnumFacing.SOUTH, 8, 4, 0, boundingBox);
        this.placeTorch(world, EnumFacing.SOUTH, 15, 4, 0, boundingBox);
        this.placeTorch(world, EnumFacing.SOUTH, 8, 4, 8, boundingBox);

        this.placeTorch(world, EnumFacing.EAST, 16, 4, 1, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 16, 4, 4, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 16, 4, 6, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 16, 4, 9, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 2, 4, 4, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 2, 4, 6, boundingBox);

        this.placeTorch(world, EnumFacing.NORTH, 1, 4, 10, boundingBox);
        this.placeTorch(world, EnumFacing.NORTH, 8, 4, 10, boundingBox);
        this.placeTorch(world, EnumFacing.NORTH, 15, 4, 10, boundingBox);
        this.placeTorch(world, EnumFacing.NORTH, 8, 4, 2, boundingBox);

        // Place crops
        Random random = new Random();
        List<IAgriPlant> plants = this.getPlantPool();

        for(int x = 3; x <= 7; x++) {
            for(int z = 3; z <= 7; z++) {
                this.generateStructureCrop(world, boundingBox, x, 2, z, (z % 2 == 0 && x % 2 == 0) || (x == 5 && z == 5), random, plants);
            }
        }
        for(int x = 9; x <= 13; x++) {
            for(int z = 3; z <= 7; z++) {
                this.generateStructureCrop(world, boundingBox, x, 2, z, (z % 2 == 0 && x % 2 == 0) || (x == 11 && z == 5), random, plants);
            }
        }

        // Prevent structure from floating or being blocked on top by terrain
        for(int zz = 0; zz < zSize; ++zz) {
            for(int xx = 0; xx < xSize; ++xx) {
                this.clearCurrentPositionBlocksUpwards(world, xx, ySize - 1, zz, boundingBox);
                this.replaceAirAndLiquidDownwards(world, cobblestone, xx, -1, zz, boundingBox);
            }
        }

        // TODO: Spawn Villager

        return true;
    }

    protected void generateStructureCrop(World world, StructureBoundingBox boundingBox, int x, int y, int z, boolean crossCrop, Random random, List<IAgriPlant> plants) {

        int xCoord = this.getXWithOffset(x, z);
        int yCoord = this.getYWithOffset(y);
        int zCoord = this.getZWithOffset(x, z);

        BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);

        if(!boundingBox.isVecInside(pos)) {
            return;
        }

        world.setBlockState(pos, AgriBlocks.getInstance().CROP.getDefaultState(), 2);
        TileEntityCrop crop = (TileEntityCrop) world.getTileEntity(pos);

        if(crop == null) {
            return;
        }

        if(crossCrop) {
            crop.setCrossCrop(true);
        } else {
            if(!plants.isEmpty()) {
                IAgriStat randomStat = WorldGenerationHelper.getRandomStat(random);
                AgriSeed seed = WorldGenerationHelper.getRandomSeed(random, false, plants)
                        .withStat(randomStat);
                crop.setSeed(seed);
            }
        }

    }

    protected void generateStructureSeedAnalyzer(World world, StructureBoundingBox boundingBox, int x, int y, int z, EnumFacing facing) {

        int xCoord = this.getXWithOffset(x, z);
        int yCoord = this.getYWithOffset(y);
        int zCoord = this.getZWithOffset(x, z);

        BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);

        if(!boundingBox.isVecInside(pos)) {
            return;
        }

        world.setBlockState(pos, AgriBlocks.getInstance().SEED_ANALYZER.getDefaultState());
        TileEntitySeedAnalyzer analyzer = (TileEntitySeedAnalyzer) world.getTileEntity(pos);

        if(analyzer != null && facing != null) {
            analyzer.setOrientation(facing);
        }

    }

    protected List<IAgriPlant> getPlantPool() {
        return AgriApi.getPlantRegistry().stream()
                .filter(plant -> plant.getTier() <= AgriCraftConfig.greenhousePlantTierLimit)
                .collect(Collectors.toList());
    }

}
