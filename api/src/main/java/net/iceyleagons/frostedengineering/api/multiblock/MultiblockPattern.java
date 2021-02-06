package net.iceyleagons.frostedengineering.api.multiblock;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TOTHTOMI
 */
public class MultiblockPattern {

    private int registeredParts = 0;
    private final MultiblockPart[] multiblockParts;
    @Getter
    private final MultiblockPart core;
    @Getter
    private final int width;
    @Getter
    private final int depth;
    @Getter
    private final int height;
    @Getter
    private final Material[] materials;
    private final List<Material> materialList;

    public MultiblockPattern(int width, int depth, int height, Material core, Material[] materials) {
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.materials = materials;
        this.materialList = new ArrayList<>(Arrays.asList(materials));

        int size = (width * depth * height);
        this.multiblockParts = new MultiblockPart[size];

        this.core = new MultiblockPart(core, 0, 0, 0);
        this.core.setMaster(true);

    }

    public void addPart(int relX, int relY, int relZ, Material material) {
        multiblockParts[registeredParts++] = new MultiblockPart(material, relX, relZ, relY);
    }

    public boolean isValid(Block block) {
        if (!materialList.contains(block.getType())) return false;

        Location core = getNearbyCoreLocation(block.getLocation());
        if (core == null) return false;

        for (MultiblockPart toCheck : multiblockParts) {
            if (toCheck == null) continue;
            Location toCheckLocation = core.clone().add(toCheck.getRelativeX(), toCheck.getRelativeY(), toCheck.getRelativeZ());
            if (toCheckLocation.getBlock().getType() != toCheck.getMaterial()) return false;
        }
        return true;
    }

    public Location getNearbyCoreLocation(Location location) {
        for (int y = -height+1; y < height; y++) {
            for (int x = -width + 1; x < width; x++) {
                for (int z = -depth + 1; z < depth; z++) {
                    Block block = location.clone().add(x, y, z).getBlock();
                    if (block.getType() == core.getMaterial()) return block.getLocation();
                }
            }
        }
        return null;
    }

    public static class PatternBuilder {

        private final Map<String, Material> materialMap = new HashMap<>();
        private final List<String[][]> layers = new ArrayList<>();
        private String coreKey;

        public void addLayer(String[]... layer) {
            layers.add(layer);
        }

        public void addMaterial(String key, Material material) {
            materialMap.put(key, material);
        }

        public void addCore(String key, Material material) {
            this.coreKey = key;
            materialMap.put(key, material);
        }

        public MultiblockPattern build() {
            if (coreKey == null || !materialMap.containsKey(coreKey)) throw new IllegalStateException("Core material is not set up!");

            int[] coreCoords = findCore();
            int[] dimension = getDimensions();
            if (coreCoords == null) throw new IllegalStateException("Core material is not set in shape!");

            int width = dimension[0];
            int depth = dimension[1];
            MultiblockPattern multiblockPattern = new MultiblockPattern(width, depth, layers.size(), materialMap.get(coreKey), materialMap.values().toArray(new Material[0]));

            int coreLayer = coreCoords[0];
            int coreX = coreCoords[1];
            int coreZ = coreCoords[2];

            for (int height = 0; height < layers.size(); height++) {
                int distanceFromCoreY = height-coreLayer;

                String[][] layer = layers.get(height);
                for (int x = 0; x < layer.length; x++) {
                    int distanceFromCoreX = x-coreX;
                    String[] column = layer[x];
                    for (int z = 0; z < column.length; z++) {
                        int distanceFromCoreZ = z-coreZ;

                        multiblockPattern.addPart(distanceFromCoreX, distanceFromCoreY, distanceFromCoreZ, materialMap.get(column[z]));
                    }
                }

            }

            return multiblockPattern;
        }

        private int[] getDimensions() {
            int width = 0;
            int depth = 0;


            for (String[][] layer : layers) {
                for (int j = 0; j < layer.length; j++) {
                    if (width < j + 1) width = j + 1;
                    for (int k = 0; k < layer[j].length; k++)
                        if (depth < k + 1) depth = k + 1;
                }
            }
            return new int[]{width, depth};
        }

        private int[] findCore() {
            for (int i = 0; i < layers.size(); i++) {
                String[][] layer = layers.get(i);
                for (int j = 0; j < layer.length; j++) {
                    String[] column = layer[j];
                    for (int k = 0; k < column.length; k++) {
                        if (column[k].equals(coreKey)) return new int[]{i, j, k};
                    }
                }
            }
            return null;
        }
    }
}
