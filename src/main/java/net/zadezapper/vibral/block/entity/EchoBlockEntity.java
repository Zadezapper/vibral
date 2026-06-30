package net.zadezapper.vibral.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.zadezapper.vibral.Vibral;
import org.jetbrains.annotations.Nullable;

import static net.zadezapper.vibral.block.ModBlocks.ECHO;

public class EchoBlockEntity extends BlockEntity {
    private BlockState storedBlock;
    @Nullable private NbtCompound storedNbt;
    private int restoreTicks;
    private boolean playerPassed;
    private int age;
    private boolean needsSync;

    public boolean getPlayerPassed() {
        return playerPassed;
    }

    public void setPlayerPassed(boolean passed) {
        playerPassed = passed;
        markDirty();
        logExecution(this.world, "setPlayerPassed()");
    }

    public BlockState getStoredBlock() {
        return storedBlock;
    }

    public void setStoredBlock(BlockState state) {
        storedBlock = state;
        markDirty();
        logExecution(this.world, "setStoredBlock()");
    }

    @Nullable public NbtCompound getStoredNbt() {
        return storedNbt;
    }

    public void setStoredNbt(@Nullable NbtCompound nbt) {
        storedNbt = nbt;
        markDirty();
        logExecution(this.world, "setStoredNbt()");
    }

    public int getRestoreTicks() {
        return restoreTicks;
    }

    public int getAge() {
        return age;
    }

    public void setRestoreTicks(int ticks) {
        this.restoreTicks = ticks;
        markDirty();
        logExecution(this.world, "setRestoreTicks()");
    }

    public EchoBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ECHO_BE, pos, state);
        logExecution(this.world, "Constructor");
    }

    public static EchoBlockEntity create(ServerWorld world, BlockPos pos, BlockState originalBlock, @Nullable NbtCompound originalNbt, int restoreTicks) {
        world.setBlockState(pos, ECHO.getDefaultState(), Block.NOTIFY_ALL);
        if (world.getBlockEntity(pos) instanceof EchoBlockEntity echoBlockEntity) {
            echoBlockEntity.setStoredBlock(originalBlock);
            echoBlockEntity.setStoredNbt(originalNbt);
            echoBlockEntity.setRestoreTicks(restoreTicks);

            echoBlockEntity.markDirty();
            world.updateListeners(pos, echoBlockEntity.getCachedState(), echoBlockEntity.getCachedState(), Block.NOTIFY_ALL);
            world.getChunkManager().markForUpdate(pos);
            world.getServer().execute(() ->
                    world.getChunkManager().markForUpdate(pos)
            );
            echoBlockEntity.needsSync = true;
            echoBlockEntity.logExecution(world, "create()");
            return echoBlockEntity;
        }
        return null;
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);

        logExecution(this.world, "addComponents()");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);

        nbt.putInt("Age", age);
        nbt.putInt("RestoreTicks", restoreTicks);
        nbt.putBoolean("PlayerPassed", playerPassed);

        if (storedBlock != null) {
            BlockState.CODEC.encodeStart(
                    registries.getOps(net.minecraft.nbt.NbtOps.INSTANCE),
                    storedBlock
            ).result().ifPresent(encoded ->
                    nbt.put("StoredBlock", encoded));
        }

        if (storedNbt != null) {
            nbt.put("StoredBlockNbt", storedNbt.copy());
        }
        logExecution(this.world, "writeNbt()");

        Vibral.LOGGER.info("writeNbt() output: {}", nbt);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);

        logExecution(this.world, "readComponents()");
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        age = nbt.getInt("Age");
        restoreTicks = nbt.getInt("RestoreTicks");
        playerPassed = nbt.getBoolean("PlayerPassed");
        storedBlock = nbt.contains("StoredBlock") ? BlockState.CODEC.parse(registries.getOps(net.minecraft.nbt.NbtOps.INSTANCE), nbt.get("StoredBlock")).result().orElse(null) : null;
        storedNbt = nbt.contains("StoredBlockNbt") ? nbt.getCompound("StoredBlockNbt") : null;
        logExecution(this.world, "readNbt()");
    }

    public static void tick(World world, BlockPos pos, BlockState state, EchoBlockEntity blockEntity) {
        if (!world.isClient()) {
            if (blockEntity.needsSync) {
                blockEntity.needsSync = false;
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.getChunkManager().markForUpdate(pos);
                }
            }
            boolean playerInside = !world.getEntitiesByClass(
                    Entity.class,
                    new Box(pos),
                    entity -> true
            ).isEmpty();
            if (playerInside && !blockEntity.playerPassed) {
                blockEntity.setPlayerPassed(true);
            }
            blockEntity.age++;
            if (((blockEntity.age >= blockEntity.restoreTicks && blockEntity.playerPassed) || (blockEntity.age >= blockEntity.restoreTicks * 3)) && !playerInside) {
                if (world instanceof ServerWorld serverWorld) {
                    BlockState storedBlock = blockEntity.getStoredBlock();
                    NbtCompound storedNbt = blockEntity.getStoredNbt();
                    if (storedBlock != null) {
                        serverWorld.setBlockState(pos, storedBlock, Block.NOTIFY_ALL);
                    }
                    if (storedNbt != null && serverWorld.getBlockEntity(pos) instanceof BlockEntity restoredBlockEntity) {
                        restoredBlockEntity.read(storedNbt, serverWorld.getRegistryManager());
                        restoredBlockEntity.markDirty();
                    }
                }
            }
            blockEntity.markDirty();
        }
    }

@Nullable
@Override
public Packet<ClientPlayPacketListener> toUpdatePacket() {
    logExecution(this.world, "toUpdatePacket()");
    return BlockEntityUpdateS2CPacket.create(this);
}

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        logExecution(this.world, "toInitialChunkDataNbt");
        return createNbt(registryLookup);
    }

    private void logExecution(World world, String function) {
        /*
        Vibral.LOGGER.info("{} Ran", function);
        Vibral.LOGGER.info("    World Type: {}", world == null ? "null" : world instanceof ServerWorld ? "Server" : "Client");
        Vibral.LOGGER.info("    Environment Type: {}", world == null ? "null" : !world.isClient ? "Server" : "Client");
        Vibral.LOGGER.info("    StoredBlock: {}", storedBlock == null ? "null" : storedBlock.getBlock().getName().getString());
        Vibral.LOGGER.info("    StoredBlockNbt: {}", storedNbt == null ? "null" : storedNbt);
        Vibral.LOGGER.info("    Cached State: {}", getCachedState());
        */
    }
}
