package com.gammahollow.spawn;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.gammahollow.spawn.mobs.lush_animals.ArmadilloSpawningRule;
import com.gammahollow.spawn.mobs.lush_animals.BatSpawningRule;
import com.gammahollow.spawn.mobs.lush_animals.ChickenSpawningRule;
import com.gammahollow.spawn.mobs.lush_animals.FoxSpawningRule;
import com.gammahollow.spawn.mobs.lush_animals.FrogSpawningRule;
import com.gammahollow.spawn.mobs.lush_animals.OcelotSpawningRule;
import com.gammahollow.spawn.mobs.lush_animals.ParrotSpawningRule;
import com.gammahollow.spawn.mobs.lush_animals.RabbitSpawningRule;
import com.gammahollow.spawn.mobs.monsters.BoggedSpawningRule;
import com.gammahollow.spawn.mobs.monsters.CaveSpiderSpawningRule;
import com.gammahollow.spawn.mobs.monsters.CreeperSpawningRule;
import com.gammahollow.spawn.mobs.monsters.SkeletonSpawningRule;
import com.gammahollow.spawn.mobs.monsters.SlimeSpawningRule;
import com.gammahollow.spawn.mobs.monsters.SpiderSpawningRule;
import com.gammahollow.spawn.mobs.monsters.StraySpawningRule;
import com.gammahollow.spawn.mobs.monsters.WitchSpawningRule;
import com.gammahollow.spawn.mobs.monsters.WitherSkeletonSpawningRule;
import com.gammahollow.spawn.mobs.monsters.ZombieSpawningRule;
import com.gammahollow.spawn.mobs.reef_animals.AxolotlSpawningRule;
import com.gammahollow.spawn.mobs.reef_animals.CodSpawningRule;
import com.gammahollow.spawn.mobs.reef_animals.DolphinSpawningRule;
import com.gammahollow.spawn.mobs.reef_animals.GlowSquidSpawningRule;
import com.gammahollow.spawn.mobs.reef_animals.PufferfishSpawningRule;
import com.gammahollow.spawn.mobs.reef_animals.SalmonSpawningRule;
import com.gammahollow.spawn.mobs.reef_animals.TropicalFishSpawningRule;
import com.gammahollow.spawn.mobs.reef_animals.TurtleSpawningRule;
import com.gammahollow.util.ModBiomes;
import com.gammahollow.util.ModDimensions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = "gammahollow")
public class GammaSpawner {

    private static final int SPAWN_INTERVAL = 40; // Attempt spawn every 2 seconds
    private static final int MAX_MONSTER_SPAWN_ATTEMPTS_PER_PLAYER = 5; // Max spawn attempts around each player per tick
    private static final int MAX_SPAWN_RANGE = 48; // Max distance from player
    private static final int MIN_SPAWN_RANGE = 16; // Min distance from player

    private static final int REEF_SPAWN_Y_THRESHOLD = 80; // Y level below which we consider it a "reef" biome for special spawns
    private static final int REEF_MAX_HEIGHT = 48; // Max height at which reef mobs can spawn
    private static final int REEF_SPAWN_ATTEMPTS = 5; // Number of attempts to spawn reef mobs per player tick

    private static final int GLOBAL_ANIMAL_CAP_PER_PLAYER = 20; // Global cap for animals per player in their vicinity to prevent overpopulation

    public static final List<AbstractMobSpawningRules> MONSTER_RULES = List.of(
        new BatSpawningRule(),
        new BoggedSpawningRule(),
        new CaveSpiderSpawningRule(),
        new CreeperSpawningRule(),
        new SkeletonSpawningRule(),
        new SlimeSpawningRule(),
        new SpiderSpawningRule(),
        new StraySpawningRule(),
        new WitchSpawningRule(),
        new WitherSkeletonSpawningRule(),
        new ZombieSpawningRule()
    );

    public static final List<AbstractMobSpawningRules> AQUATIC_RULES = List.of(
        new AxolotlSpawningRule(),
        new CodSpawningRule(),
        new DolphinSpawningRule(),
        new GlowSquidSpawningRule(),
        new PufferfishSpawningRule(),
        new SalmonSpawningRule(),
        new TropicalFishSpawningRule(),
        new TurtleSpawningRule()
    );

    public static final List<AbstractMobSpawningRules> LUSH_RULES = List.of(
        new ArmadilloSpawningRule(),
        new ChickenSpawningRule(),
        new FoxSpawningRule(),
        new FrogSpawningRule(),
        new OcelotSpawningRule(),
        new ParrotSpawningRule(),
        new RabbitSpawningRule()
    );

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        
        // Only run on server, in Gamma, every SPAWN_INTERVAL ticks
        if (level.isClientSide || level.dimension() != ModDimensions.GAMMA_HOLLOW_KEY || level.getGameTime() % SPAWN_INTERVAL != 0) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        for (Player player : serverLevel.players()) {
            attemptSpawnNearPlayer(serverLevel, player);
        }
    }

    private static int getListCountInArea(ServerLevel level, AABB area, List<AbstractMobSpawningRules> rules) {
        int count = 0;
        for (AbstractMobSpawningRules rule : rules) {
            count += rule.getMobCountInArea(level, area);
        }
        return count;
    }

    private static void attemptSpawnNearPlayer(ServerLevel level, Player player) {
        RandomSource random = level.random;
        int offsetX, offsetY, offsetZ;
        BlockPos playerPos = player.blockPosition();
        AABB spawnArea = new AABB(playerPos).inflate(MAX_SPAWN_RANGE);
        
        for (int i = 0; i < MAX_MONSTER_SPAWN_ATTEMPTS_PER_PLAYER; i++) {
            // Pick a random position around the player
            offsetX = random.nextInt(MAX_SPAWN_RANGE * 2) - MAX_SPAWN_RANGE;
            offsetY = random.nextInt(MAX_SPAWN_RANGE * 2) - MAX_SPAWN_RANGE;
            offsetZ = random.nextInt(MAX_SPAWN_RANGE * 2) - MAX_SPAWN_RANGE;
            
            // Ensure it's outside the minimum range
            if (Math.abs(offsetX) < MIN_SPAWN_RANGE && Math.abs(offsetY) < MIN_SPAWN_RANGE && Math.abs(offsetZ) < MIN_SPAWN_RANGE) return;

            BlockPos monsterSpawnPos = playerPos.offset(offsetX, offsetY, offsetZ);
            if (level.isLoaded(monsterSpawnPos)) {
                Holder<Biome> monsterBiome = level.getBiome(monsterSpawnPos);
                spawnFromRules(level, monsterSpawnPos, spawnArea, monsterBiome, random, MONSTER_RULES);
            }
        }

        if (playerPos.getY() < REEF_SPAWN_Y_THRESHOLD) {
            for (int i = 0; i < REEF_SPAWN_ATTEMPTS; i++) {
                offsetX = random.nextInt(MAX_SPAWN_RANGE * 2) - MAX_SPAWN_RANGE;
                offsetZ = random.nextInt(MAX_SPAWN_RANGE * 2) - MAX_SPAWN_RANGE;
                int absoluteY = random.nextInt(REEF_MAX_HEIGHT);
                if (Math.abs(offsetX) < MIN_SPAWN_RANGE && Math.abs(playerPos.getY() - absoluteY) < MIN_SPAWN_RANGE && Math.abs(offsetZ) < MIN_SPAWN_RANGE) continue;
                BlockPos aquaticSpawnPos = playerPos.offset(offsetX, absoluteY, offsetZ);
                if (!level.isLoaded(aquaticSpawnPos)) continue;
                Holder<Biome> aquaticBiome = level.getBiome(aquaticSpawnPos);
                if (spawnFromRules(level, aquaticSpawnPos, spawnArea, aquaticBiome, random, AQUATIC_RULES)) break;
            }
        }

        if (getListCountInArea(level, spawnArea, LUSH_RULES) < GLOBAL_ANIMAL_CAP_PER_PLAYER) {
            offsetX = random.nextInt(MAX_SPAWN_RANGE * 2) - MAX_SPAWN_RANGE;
            offsetY = random.nextInt(MAX_SPAWN_RANGE * 2) - MAX_SPAWN_RANGE;
            offsetZ = random.nextInt(MAX_SPAWN_RANGE * 2) - MAX_SPAWN_RANGE;
            if (Math.abs(offsetX) < MIN_SPAWN_RANGE && Math.abs(offsetY) < MIN_SPAWN_RANGE && Math.abs(offsetZ) < MIN_SPAWN_RANGE) return;
            BlockPos lushSpawnPos = playerPos.offset(offsetX, offsetY, offsetZ);
            if (!level.isLoaded(lushSpawnPos)) return;
            Holder<Biome> lushBiome = level.getBiome(lushSpawnPos);
            spawnFromRules(level, lushSpawnPos, spawnArea, lushBiome, random, LUSH_RULES);
        }
    }

    private static boolean spawnFromRules(ServerLevel level, BlockPos pos, AABB area, Holder<Biome> biome, RandomSource random, List<AbstractMobSpawningRules> ruleList) {
        int totalWeight = 0;
        List<AbstractMobSpawningRules> validRules = new java.util.ArrayList<>();

        for (AbstractMobSpawningRules rule : ruleList) {
            if (rule.canSpawnInBiome(biome)) {
                validRules.add(rule);
                totalWeight += rule.spawnWeight;
            }
        }

        if (totalWeight <= 0) return false;

        int selection = random.nextInt(totalWeight);
        int currentSum = 0;

        for (AbstractMobSpawningRules rule : validRules) {
            currentSum += rule.spawnWeight;
            if (selection < currentSum) {
                int groupSize = rule.getGroupSizeForSpawn(random);
                if (area == null || rule.getMobCapacityInArea(level, area) >= groupSize) {
                    return rule.spawnGroup(level, pos, random, groupSize);
                } else {
                    return false; // Not enough capacity to spawn this group
                }
            }
        }

        throw new IllegalStateException("Should never reach here if totalWeight > 0");
    }
}