package com.infinityraider.agricraft.world;

import com.infinityraider.agricraft.api.v1.misc.IAgriConnectable;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;
import com.infinityraider.agricraft.init.AgriBlocks;
import com.infinityraider.agricraft.tiles.irrigation.TileEntityChannel;
import com.infinityraider.agricraft.tiles.irrigation.TileEntityTank;
import com.infinityraider.infinitylib.utility.WorldHelper;
import net.minecraft.block.*;
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

public class StructureGreenHouseIrrigated extends StructureGreenHouse {

    // Structure dimensions
    private static final int xSize = 17;
    private static final int ySize = 10;
    private static final int zSize = 16;

    private int averageGroundLevel = -1;

    public StructureGreenHouseIrrigated(StructureVillagePieces.Start villagePiece, int type, Random rand, StructureBoundingBox structureBoundingBox, EnumFacing facing) {
        super(villagePiece, type, rand, structureBoundingBox, facing);
    }

    public static StructureGreenHouse buildComponent(StructureVillagePieces.Start villagePiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
        StructureBoundingBox sbb = StructureBoundingBox.getComponentToAddBoundingBox(
                p1, p2, p3, 0, 0, 0, xSize, ySize, zSize, facing
        );
        return canVillageGoDeeper(sbb) && StructureComponent.findIntersecting(pieces, sbb) == null
                ? new StructureGreenHouseIrrigated(villagePiece, p5, random, sbb, facing)
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

            // Make the structure match the ground level instead of being one block higher due to the cobblestone base
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

        // Grass patch
        IBlockState grass = Blocks.GRASS.getDefaultState();
        this.fillWithBlocks(world, boundingBox, 1, 1, 1, 9, 1, 5, grass, grass, false);
        this.fillWithBlocks(world, boundingBox, 10, 1, 1, 13, 1, 2, grass, grass, false);
        this.fillWithBlocks(world, boundingBox, 14, 1, 1, 15, 1, 5, grass, grass, false);

        // Cobblestone foundations
        this.fillWithBlocks(world, boundingBox, 1, 1, 6, 1, 1, 9, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 1, 1, 11, 1, 1, 14, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 15, 1, 6, 15, 1, 9, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 15, 1, 11, 15, 1, 14, cobblestone, cobblestone, false);
        this.setBlockState(world, cobblestone, 2, 1, 6, boundingBox);
        this.setBlockState(world, cobblestone, 8, 1, 6, boundingBox);
        this.setBlockState(world, cobblestone, 14, 1, 6, boundingBox);
        this.setBlockState(world, cobblestone, 2, 1, 14, boundingBox);
        this.setBlockState(world, cobblestone, 8, 1, 14, boundingBox);
        this.setBlockState(world, cobblestone, 14, 1, 14, boundingBox);
        this.fillWithBlocks(world, boundingBox, 10, 1, 3, 10, 1, 5, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 13, 1, 3, 13, 1, 5, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 11, 1, 3, 12, 1, 3, cobblestone, cobblestone, false);

        // Place slabs
        IBlockState stoneSlab = Blocks.DOUBLE_STONE_SLAB.getDefaultState();
        this.setBlockState(world, stoneSlab, 1, 1, 10, boundingBox);
        this.setBlockState(world, stoneSlab, 15, 1, 10, boundingBox);
        this.fillWithBlocks(world, boundingBox, 2, 1, 7, 2, 1, 13, stoneSlab, stoneSlab, false);
        this.fillWithBlocks(world, boundingBox, 8, 1, 7, 8, 1, 13, stoneSlab, stoneSlab, false);
        this.fillWithBlocks(world, boundingBox, 14, 1, 7, 14, 1, 13, stoneSlab, stoneSlab, false);
        this.fillWithBlocks(world, boundingBox, 2, 1, 7, 14, 1, 7, stoneSlab, stoneSlab, false);
        this.fillWithBlocks(world, boundingBox, 2, 1, 13, 14, 1, 13, stoneSlab, stoneSlab, false);
        this.fillWithBlocks(world, boundingBox, 11, 1, 4, 12, 1, 6, stoneSlab, stoneSlab, false);

        // Place water
        IBlockState water = Blocks.WATER.getDefaultState();
        this.fillWithBlocks(world, boundingBox, 3, 1, 6, 7, 1, 6, water, water, false);
        this.fillWithBlocks(world, boundingBox, 9, 1, 6, 10, 1, 6, water, water, false);
        this.fillWithBlocks(world, boundingBox, 3, 1, 14, 7, 1, 14, water, water, false);
        this.fillWithBlocks(world, boundingBox, 9, 1, 14, 13, 1, 14, water, water, false);
        this.setBlockState(world, water, 13, 1, 6, boundingBox);

        // Place farmland
        IBlockState farmland = Blocks.FARMLAND.getDefaultState()
                .withProperty(BlockFarmland.MOISTURE, 7);
        this.fillWithBlocks(world, boundingBox, 3, 1, 8, 7, 1, 12, farmland, farmland, false);
        this.fillWithBlocks(world, boundingBox, 9, 1, 8, 13, 1, 12, farmland, farmland, false);

        // Place standing logs
        IBlockState logYAxis = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
        IBlockState logXAxis = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState()
                .withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X)
        );
        IBlockState logZAxis = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState()
                .withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z)
        );

        this.fillWithBlocks(world, boundingBox, 10, 2, 3, 10, 5, 3, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 13, 2, 3, 13, 5, 3, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 6, 1, 6, 6, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 3, 2, 6, 3, 5, 6, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 6, 2, 6, 6, 5, 6, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 8, 2, 6, 8, 6, 6, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 10, 2, 6, 10, 5, 6, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 13, 2, 6, 13, 5, 6, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 6, 15, 6, 6, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 9, 1, 5, 9, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 9, 15, 5, 9, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 11, 1, 5, 11, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 11, 15, 5, 11, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 14, 1, 6, 14, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 8, 2, 14, 8, 6, 14, logYAxis, logYAxis, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 14, 15, 6, 14, logYAxis, logYAxis, false);
        
        // Logs along x-axis
        this.fillWithBlocks(world, boundingBox, 11, 5, 3, 12, 5, 3, logXAxis, logXAxis, false);
        this.fillWithBlocks(world, boundingBox, 2, 6, 6, 7, 6, 6, logXAxis, logXAxis, false);
        this.fillWithBlocks(world, boundingBox, 9, 6, 6, 14, 6, 6, logXAxis, logXAxis, false);
        this.fillWithBlocks(world, boundingBox, 4, 4, 6, 5, 4, 6, logXAxis, logXAxis, false);
        this.fillWithBlocks(world, boundingBox, 11, 4, 6, 12, 4, 6, logXAxis, logXAxis, false);
        this.fillWithBlocks(world, boundingBox, 2, 6, 14, 7, 6, 14, logXAxis, logXAxis, false);
        this.fillWithBlocks(world, boundingBox, 9, 6, 14, 14, 6, 14, logXAxis, logXAxis, false);
        // Logs along z-axis
        this.fillWithBlocks(world, boundingBox, 1, 6, 7, 1, 6, 13, logZAxis, logZAxis, false);
        this.fillWithBlocks(world, boundingBox, 8, 6, 7, 8, 6, 13, logZAxis, logZAxis, false);
        this.fillWithBlocks(world, boundingBox, 15, 6, 7, 15, 6, 13, logZAxis, logZAxis, false);
        this.fillWithBlocks(world, boundingBox, 10, 5, 4, 10, 5, 5, logZAxis, logZAxis, false);
        this.fillWithBlocks(world, boundingBox, 13, 5, 4, 13, 5, 5, logZAxis, logZAxis, false);
        this.setBlockState(world, logZAxis, 1, 4, 10, boundingBox);
        this.setBlockState(world, logZAxis, 15, 4, 10, boundingBox);

        // Cobblestone walls
        this.fillWithBlocks(world, boundingBox, 4, 2, 6, 5, 2, 6, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 10, 2, 4, 10, 2, 5, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 11, 2, 3, 12, 2, 3, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 13, 2, 4, 13, 2, 5, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 7, 1, 2, 8, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 12, 1, 2, 13, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 7, 15, 2, 8, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 12, 15, 2, 13, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 2, 2, 14, 7, 2, 14, cobblestone, cobblestone, false);
        this.fillWithBlocks(world, boundingBox, 9, 2, 14, 14, 2, 14, cobblestone, cobblestone, false);
        this.setBlockState(world, cobblestone, 2, 2, 6, boundingBox);
        this.setBlockState(world, cobblestone, 7, 2, 6, boundingBox);
        this.setBlockState(world, cobblestone, 9, 2, 6, boundingBox);
        this.setBlockState(world, cobblestone, 14, 2, 6, boundingBox);

        // Place glass
        IBlockState glass = Blocks.GLASS.getDefaultState();
        this.fillWithBlocks(world, boundingBox, 1, 3, 7, 1, 5, 8, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 1, 3, 12, 1, 5, 13, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 2, 3, 6, 2, 5, 6, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 4, 3, 6, 5, 3, 6, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 7, 3, 6, 7, 5, 6, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 9, 3, 6, 9, 5, 6, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 14, 3, 6, 14, 5, 6, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 11, 5, 4, 12, 5, 6, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 15, 3, 7, 15, 5, 8, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 15, 3, 12, 15, 5, 13, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 2, 3, 14, 7, 5, 14, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 9, 3, 14, 14, 5, 14, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 2, 6, 7, 7, 6, 13, glass, glass, false);
        this.fillWithBlocks(world, boundingBox, 9, 6, 7, 14, 6, 13, glass, glass, false);
        this.setBlockState(world, glass, 4, 5, 6, boundingBox);
        this.setBlockState(world, glass, 1, 5, 10, boundingBox);
        this.setBlockState(world, glass, 15, 5, 10, boundingBox);

        // Wooden pillars
        IBlockState planks = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
        this.fillWithBlocks(world, boundingBox, 3, 2, 1, 3, 4, 1, planks, planks, false);
        this.fillWithBlocks(world, boundingBox, 3, 2, 4, 3, 4, 4, planks, planks, false);
        this.fillWithBlocks(world, boundingBox, 6, 2, 1, 6, 4, 1, planks, planks, false);
        this.fillWithBlocks(world, boundingBox, 6, 2, 4, 6, 4, 4, planks, planks, false);

        // Oak stairs - Tank
        IBlockState stairs = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState())
                .withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);

        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH), 3, 4, 3, boundingBox);
        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH), 6, 4, 3, boundingBox);

        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST), 5, 4, 1, boundingBox);
        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST), 5, 4, 4, boundingBox);

        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST), 4, 4, 1, boundingBox);
        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST), 4, 4, 4, boundingBox);

        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH), 3, 4, 2, boundingBox);
        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH), 6, 4, 2, boundingBox);

        // Oak stairs - Seed analyzer
        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH), 11, 4, 3, boundingBox);
        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH), 12, 4, 3, boundingBox);

        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST), 10, 4, 4, boundingBox);
        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST), 10, 4, 5, boundingBox);

        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST), 13, 4, 4, boundingBox);
        this.setBlockState(world, stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST), 13, 4, 5, boundingBox);

        // Place doors (not using #createVillageDoor because it forces them to face NORTH)
        BlockDoor door = this.biomeDoor();
        this.generateDoor(world, boundingBox, rand, 1, 2, 10, EnumFacing.EAST, door);
        this.generateDoor(world, boundingBox, rand, 15, 2, 10, EnumFacing.WEST, door);

        // Place fences
        IBlockState fence = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
        this.fillWithBlocks(world, boundingBox, 1, 2, 1, 2, 2, 1, fence, fence, false);
        this.fillWithBlocks(world, boundingBox, 4, 2, 1, 5, 2, 1, fence, fence, false);
        this.fillWithBlocks(world, boundingBox, 7, 2, 1, 15, 2, 1, fence, fence, false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 2, 1, 2, 5, fence, fence, false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 2, 15, 2, 5, fence, fence, false);
        this.fillWithBlocks(world, boundingBox, 8, 4, 9, 8, 4, 11, fence, fence, false);
        this.setBlockState(world, fence, 8, 5, 9, boundingBox);
        this.setBlockState(world, fence, 8, 5, 11, boundingBox);

        // Place torches
        this.placeTorch(world, EnumFacing.WEST, 0, 4, 6, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 0, 4, 9, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 0, 4, 11, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 0, 4, 14, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 9, 4, 3, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 14, 4, 9, boundingBox);
        this.placeTorch(world, EnumFacing.WEST, 14, 4, 11, boundingBox);

        this.placeTorch(world, EnumFacing.UP, 1, 3, 1, boundingBox);
        this.placeTorch(world, EnumFacing.UP, 15, 3, 1, boundingBox);

        this.placeTorch(world, EnumFacing.SOUTH, 1, 4, 5, boundingBox);
        this.placeTorch(world, EnumFacing.SOUTH, 8, 4, 5, boundingBox);

        this.placeTorch(world, EnumFacing.SOUTH, 10, 4, 2, boundingBox);
        this.placeTorch(world, EnumFacing.SOUTH, 13, 4, 2, boundingBox);
        this.placeTorch(world, EnumFacing.SOUTH, 15, 4, 5, boundingBox);

        this.placeTorch(world, EnumFacing.EAST, 14, 4, 3, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 16, 4, 6, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 16, 4, 9, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 16, 4, 14, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 16, 4, 14, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 2, 4, 9, boundingBox);
        this.placeTorch(world, EnumFacing.EAST, 2, 4, 11, boundingBox);

        // Place crops
        Random random = new Random();
        List<IAgriPlant> plants = this.getPlantPool();

        for(int x = 3; x <= 7; x++) {
            for(int z = 8; z <= 12; z++) {
                this.generateStructureCrop(world, boundingBox, x, 2, z, (z % 2 == 1 && x % 2 == 0) || (x == 5 && z == 10), random, plants);
            }
        }
        for(int x = 9; x <= 13; x++) {
            for(int z = 8; z <= 12; z++) {
                this.generateStructureCrop(world, boundingBox, x, 2, z, (z % 2 == 1 && x % 2 == 0) || (x == 11 && z == 10), random, plants);
            }
        }

        // We can ignore checking if the planks are actually planks (and not sandstone) because it will be done
        // later in #setMaterial, and in that case it will automatically fall back to default oak planks
        Block irrigationMaterialBlock = planks.getBlock();
        int irrigationMaterialMeta = irrigationMaterialBlock.getMetaFromState(planks);

        // Place water tank
        for(int x = 3; x <= 6; x++) {
            for(int y = 5; y <= 8; y++) {
                for(int z = 1; z <= 4; z++) {
                    this.generateStructureWoodenTank(world, boundingBox, x, y, z, irrigationMaterialBlock, irrigationMaterialMeta);
                }
            }
        }

        // Place irrigation channels
        for(int z = 5; z <= 10; z++) {
            this.generateStructureIrrigationChannel(world, boundingBox, 5, 5, z, irrigationMaterialBlock, irrigationMaterialMeta);
        }
        for(int x = 6; x <= 11; x++) {
            this.generateStructureIrrigationChannel(world, boundingBox, x, 5, 10, irrigationMaterialBlock, irrigationMaterialMeta);
        }

        // Place sprinklers
        this.generateStructureSprinkler(world, boundingBox, 5, 4, 10);
        this.generateStructureSprinkler(world, boundingBox, 11, 4, 10);

        // Place seed analyzer
        this.generateStructureSeedAnalyzer(world, boundingBox, 11, 2, 4, EnumFacing.SOUTH);

        // Prevent structure from floating or being blocked on top by terrain
        for(int zz = 0; zz < zSize; ++zz) {
            for(int xx = 0; xx < xSize; ++xx) {
                this.clearCurrentPositionBlocksUpwards(world, xx, ySize - 1, zz, boundingBox);
                this.replaceAirAndLiquidDownwards(world, cobblestone, xx, -1, zz, boundingBox);
            }
        }

        return true;
    }

    protected void generateStructureWoodenTank(World world, StructureBoundingBox boundingBox, int x, int y, int z, Block materialBlock, int materialMeta) {

        int xCoord = this.getXWithOffset(x, z);
        int yCoord = this.getYWithOffset(y);
        int zCoord = this.getZWithOffset(x, z);

        BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);

        if(!boundingBox.isVecInside(pos)) {
            return;
        }

        world.setBlockState(pos, AgriBlocks.getInstance().TANK.getDefaultState());
        TileEntityTank tank = (TileEntityTank) world.getTileEntity(pos);

        if(tank == null) {
            tank = new TileEntityTank();
            world.setTileEntity(pos, tank);
        }

        tank.setMaterial(materialBlock, materialMeta);

        WorldHelper.getTile(world, pos, IAgriConnectable.class)
                .ifPresent(IAgriConnectable::refreshConnections);

        WorldHelper.getTileNeighbors(world, pos, IAgriConnectable.class)
                .forEach(IAgriConnectable::refreshConnections);

    }

    protected void generateStructureIrrigationChannel(World world, StructureBoundingBox boundingBox, int x, int y, int z, Block materialBlock, int materialMeta) {

        int xCoord = this.getXWithOffset(x, z);
        int yCoord = this.getYWithOffset(y);
        int zCoord = this.getZWithOffset(x, z);

        BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);

        if(!boundingBox.isVecInside(pos)) {
            return;
        }

        world.setBlockState(pos, AgriBlocks.getInstance().CHANNEL.getDefaultState());
        TileEntityChannel channel = (TileEntityChannel) world.getTileEntity(pos);

        if(channel == null) {
            return;
        }

        channel.setMaterial(materialBlock, materialMeta);

        WorldHelper.getTile(world, pos, IAgriConnectable.class)
                .ifPresent(IAgriConnectable::refreshConnections);

        WorldHelper.getTileNeighbors(world, pos, IAgriConnectable.class)
                .forEach(IAgriConnectable::refreshConnections);

    }

    protected void generateStructureSprinkler(World world, StructureBoundingBox boundingBox, int x, int y, int z) {

        int xCoord = this.getXWithOffset(x, z);
        int yCoord = this.getYWithOffset(y);
        int zCoord = this.getZWithOffset(x, z);

        BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);

        if(!boundingBox.isVecInside(pos)) {
            return;
        }

        world.setBlockState(pos, AgriBlocks.getInstance().SPRINKLER.getDefaultState());

        WorldHelper.getTile(world, pos.up(), IAgriConnectable.class)
                .ifPresent(IAgriConnectable::refreshConnections);

    }

}
