package net.iceyleagons.worldgenerator.generator.populators;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.schematic.Schematic;
import net.iceyleagons.icicle.schematic.SchematicLoader;
import net.iceyleagons.worldgenerator.WorldGenerator;
import net.iceyleagons.worldgenerator.biome.Biome;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.generator.FrostedChunkGenerator;
import net.iceyleagons.worldgenerator.trees.StandardClusters;
import net.iceyleagons.worldgenerator.trees.Trees;
import net.lingala.zip4j.ZipFile;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreePopulator extends BlockPopulator {
    private final int seed;
    private final BiomeManager biomeManager;
    private final int treesPerChunk;

    private static final boolean experimental = true;

    public TreePopulator(int seed, int treesPerChunk, BiomeManager biomeManager) {
        this.seed = seed;
        this.biomeManager = biomeManager;
        this.treesPerChunk = treesPerChunk;

        init();
    }

    @SneakyThrows
    private void init() {
        File dir = new File(WorldGenerator.MAIN.getDataFolder(), "trees\\");
        dir.mkdirs();

        File updated = new File(dir.getParentFile(), "updated.txt");
        if (updated.exists()) {
            String text = new String(Files.readAllBytes(Paths.get(updated.toURI())), StandardCharsets.UTF_8);

            long updatedTime = Long.parseLong(text);

            if (!(System.currentTimeMillis() - updatedTime > 172_800_000)) { // or 2 days in millis, can also be expressed as 1.728e+8
                shuffle();

                return;
            } else updated.delete();
        }

        // http://0x0.st/-HKD.zip

        URL url = new URL("http://0x0.st/-HKD.zip");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream is = connection.getInputStream()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(new File(dir.getParentFile(), "trees.zip"))) {
                int read;
                while ((read = is.read()) != -1)
                    fileOutputStream.write(read);
            }
        }

        ZipFile zfile = new ZipFile(new File(dir.getParentFile(), "trees.zip"));
        zfile.extractAll(dir.getParent());

        new File(dir.getParentFile(), "trees.zip").delete();
        try (FileWriter fileWriter = new FileWriter(updated)) {
            fileWriter.write(String.valueOf(System.currentTimeMillis()));
        }

        shuffle();
    }

    private void shuffle() {
        for (Biome biome : this.biomeManager.biomeList) {
            String string = "trees/" + biome.getBiomeName() + "/";
            List<Schematic> clipboards = new ArrayList<>();

            for (File file : new File(WorldGenerator.MAIN.getDataFolder(), string).listFiles())
                clipboards.add(SchematicLoader.loadSchematic(file).join());

            File settingsFile = new File(new File(WorldGenerator.MAIN.getDataFolder(), string), "settings.txt");
            if (settingsFile.exists() && experimental) {

            }

            biome.getTreeList().addAll(clipboards);
        }
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        int chunkX = chunk.getX() * 16;
        int chunkZ = chunk.getZ() * 16;
        for (int i = 0; i < this.treesPerChunk; ++i) {
            Location location;
            int z;
            int x = chunkX + random.nextInt(16);
            int y = world.getHighestBlockYAt(x, z = chunkZ + random.nextInt(16));

            Material mat;
            if (y < 98 || (mat = (location = new Location(world, x, y, z)).clone().add(0.0, -1.0, 0.0).getBlock().getType()) == Material.WATER
                    || mat == Material.LILY_PAD || mat == Material.LAVA || mat == Material.ICE || mat.name().contains("LEAVES") || mat.name().contains("LOG"))
                continue;
            Biome biome = biomeManager.getBiome(x, z);

            if (random.nextDouble() > biome.getTreeChance())
                return;

            if (!FrostedChunkGenerator.canCave(location.getBlockX(), location.getBlockY(), location.getBlockZ(), seed))
                return;

            if (FrostedChunkGenerator.getRiverDepth(location.getBlockX(), location.getBlockZ(), seed) != 0)
                return;

            populateWithRandomTree(location, biome, random);
        }
    }

    private void populateWithRandomTree(Location location, Biome biome, Random random) {
        if (random.nextDouble() > 0.9 && experimental)
            Trees.create(location, Trees.TreeConstructor.Builder.create(StandardClusters.OAK).build()).place();
        else {
            List<Schematic> list = biome.getTreeList();
            if (list.size() == 0)
                return;

            int chosenTree = random.nextInt(list.size());
            Schematic schematic = list.get(chosenTree);

            schematic.paste(location, -2, true, false, false);
        }
    }
}
