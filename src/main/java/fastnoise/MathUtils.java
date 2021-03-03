/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package fastnoise;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;

import net.iceyleagons.frostedengineering.utils.math.Range;

// 2/26/2020 In the progress of making this a central utility
// 2/26/2020 Implemented Erosion inside here
// 2/26/2020 Refactored class to be MathUtils
// 2/26/2020 Edited to allow long values. (IN THEORY)
// 2/23/2020 Added fastPow.
// 2/22/2020 Removed useless stuff.
public class MathUtils {

    public static MathUtils instance;

    private final Map<NoiseClass, Map.Entry<FastNoise, Optional<FastNoise>>> noiseMap = new HashMap<>();
    private Erosion erosionModel;

    private static long seed = 1337L;
    private static Random random;

    public static MathUtils getInstance(long seed) {
        if (MathUtils.instance == null) {
            random = new Random(seed);
            return MathUtils.instance = new MathUtils(seed);
        }

        return MathUtils.instance;
    }

    public void updateSeed(long seed) {
        noiseMap.forEach((ignore, entry) -> {
            entry.getKey().setSeed(seed);
            if (entry.getValue().isPresent())
                entry.getValue().get().setSeed(seed);
        });
    }

    public static MathUtils getInstance() {
        if (MathUtils.instance == null) {
            random = new Random(seed);
            return MathUtils.instance = new MathUtils();
        }

        return MathUtils.instance;
    }

    public Erosion getErosion(int size, float initialVelocity, float initialVolume, int maxDropletLifetime,
                              float gravity, float evaporationSpeed, float depositSpeed, float erodeSpeed, float minSedimentCapacity,
                              float sedimentCapacityFactor, float inertia, int radius, int iterations) {
        if (this.erosionModel == null)
            return this.erosionModel = new Erosion(size, initialVelocity, initialVolume, maxDropletLifetime, gravity,
                    evaporationSpeed, depositSpeed, erodeSpeed, minSedimentCapacity, sedimentCapacityFactor, inertia,
                    radius, iterations);

        return this.erosionModel;
    }

    public Erosion getErosion() {
        return this.erosionModel;
    }

    public Map.Entry<FastNoise, Optional<FastNoise>> getNoise(NoiseClass noiseClass) {
        setupNoise(noiseClass);

        return noiseMap.get(noiseClass);
    }

    public MathUtils() {
        MathUtils.instance = this;
    }

    public MathUtils(long seed) {
        MathUtils.seed = seed;
        MathUtils.instance = this;
    }

    public enum NoiseClass {
        CAVE, TERRAIN
    }

    public void setupNoise(NoiseClass noiseClass) {
        if (noiseMap.containsKey(noiseClass))
            return;
        switch (noiseClass) {
            default:
            case TERRAIN:
                noiseMap.put(noiseClass, MapEntry.of(new FastNoise().setNoiseType(FastNoise.NoiseType.SIMPLEX),
                        Optional.empty()));
                break;
            case CAVE:
                FastNoise firstNoise = new FastNoise().setNoiseType(FastNoise.NoiseType.FRACTAL_SIMPLEX).setFractalGain(.5f).setFractalLacunarity(2.f)
                        .setFractalType(FastNoise.FractalType.FBM).setFractalOctaves(4);
                FastNoise secondNoise = firstNoise.clone().setSeed(firstNoise.getSeed() + ThreadLocalRandom.current().nextInt(99000) + 1000);
                MapEntry<FastNoise, Optional<FastNoise>> mapEntry1 = MapEntry.of(firstNoise, Optional.of(secondNoise));
                noiseMap.put(noiseClass, mapEntry1);
                break;
        }
    }

    public static class MapEntry<K, V> extends AbstractMap.SimpleEntry<K, V> {
        public MapEntry(K key, V value) {
            super(key, value);
        }

        public static <K, V> MapEntry<K, V> of(K key, V value) {
            return new MapEntry<>(key, value);
        }
    }

    // FastNoise.java
    //
    // MIT License
    //
    // Copyright(c) 2017 Jordan Peck
    //
    // Permission is hereby granted, free of charge, to any person obtaining a copy
    // of this software and associated documentation files(the "Software"), to deal
    // in the Software without restriction, including without limitation the rights
    // to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
    // copies of the Software, and to permit persons to whom the Software is
    // furnished to do so, subject to the following conditions :
    //
    // The above copyright notice and this permission notice shall be included in all
    // copies or substantial portions of the Software.
    //
    // THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    // IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    // FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
    // AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    // LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    // OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    // SOFTWARE.
    //
    // The developer's email is jorzixdan.me2@gzixmail.com (for great email, take
    // off every 'zix'.)
    //
    public static class FastNoise implements Cloneable {
        public enum NoiseType {
            PERLIN, FRACTAL_PERLIN, SIMPLEX, FRACTAL_SIMPLEX, CELLULAR
        }

        public enum Interp {
            LINEAR, HERMITE, QUINTIC
        }

        public enum FractalType {
            FBM, BILLOW, RIGID_MULTI
        }

        public enum CellularDistanceFunction {
            EUCLIDEAN, MANHATTAN, NATURAL
        }

        public enum CellularReturnType {
            CELL_VALUE, NOISE_LOOKUP, DISTANCE, DISTANCE2, DISTANCE2_ADD, DISTANCE2_SUB, DISTANCE2_MUL, DISTANCE2_DIV
        }

        private long m_seed;
        private float m_frequency = (float) 0.01;
        private Interp m_interp = Interp.QUINTIC;
        private NoiseType m_noiseType = NoiseType.SIMPLEX;

        private int m_octaves = 3;
        private float m_lacunarity = (float) 2.0;
        private float m_gain = (float) 0.5;
        private FractalType m_fractalType = FractalType.FBM;

        private float m_fractalBounding;

        private CellularDistanceFunction m_cellularDistanceFunction = CellularDistanceFunction.EUCLIDEAN;
        private CellularReturnType m_cellularReturnType = CellularReturnType.CELL_VALUE;
        private FastNoise m_cellularNoiseLookup = null;

        @Override
        public FastNoise clone() {
            FastNoise cloned = new FastNoise();
            cloned.setSeed(m_seed);
            return cloned;
        }

        public FastNoise() {
            this.m_seed = MathUtils.seed;
            calculateFractalBounding();
        }

        // Returns the seed used by this object
        public long getSeed() {
            return m_seed;
        }

        // Sets seed used for all noise types
        // Default: 1337
        public FastNoise setSeed(long seed) {
            m_seed = seed;
            return this;
        }

        // Sets frequency for all noise types
        // Default: 0.01
        public FastNoise setFrequency(float frequency) {
            m_frequency = frequency;
            return this;
        }

        // Changes the interpolation method used to smooth between noise values
        // Possible interpolation methods (lowest to highest quality) :
        // - Linear
        // - Hermite
        // - Quintic
        // Used in Value, Gradient Noise and Position Perturbing
        // Default: Quintic
        public FastNoise setInterp(Interp interp) {
            m_interp = interp;
            return this;
        }

        // Sets noise return type of GetNoise(...)
        // Default: Simplex
        public FastNoise setNoiseType(NoiseType noiseType) {
            m_noiseType = noiseType;
            return this;
        }

        // Sets octave count for all fractal noise types
        // Default: 3
        public FastNoise setFractalOctaves(int octaves) {
            m_octaves = octaves;
            calculateFractalBounding();
            return this;
        }

        // Sets octave lacunarity for all fractal noise types
        // Default: 2.0
        public FastNoise setFractalLacunarity(float lacunarity) {
            m_lacunarity = lacunarity;
            return this;
        }

        // Sets octave gain for all fractal noise types
        // Default: 0.5
        public FastNoise setFractalGain(float gain) {
            m_gain = gain;
            calculateFractalBounding();
            return this;
        }

        // Sets method for combining octaves in all fractal noise types
        // Default: FBM
        public FastNoise setFractalType(FractalType fractalType) {
            m_fractalType = fractalType;
            return this;
        }

        // Sets return type from cellular noise calculations
        // Note: NoiseLookup requires another FastNoise object be set with SetCellularNoiseLookup() to function
        // Default: CellValue
        public FastNoise setCellularDistanceFunction(CellularDistanceFunction cellularDistanceFunction) {
            m_cellularDistanceFunction = cellularDistanceFunction;
            return this;
        }

        // Sets distance function used in cellular noise calculations
        // Default: Euclidean
        public FastNoise setCellularReturnType(CellularReturnType cellularReturnType) {
            m_cellularReturnType = cellularReturnType;
            return this;
        }

        // Noise used to calculate a cell value if cellular return type is NoiseLookup
        // The lookup value is acquired through GetNoise() so ensure you SetNoiseType() on the noise lookup, value, gradient or simplex is recommended
        public FastNoise setCellularNoiseLookup(FastNoise noise) {
            m_cellularNoiseLookup = noise;
            return this;
        }

        private static final Vector2[] GRAD_2D = {new Vector2(-1, -1), new Vector2(1, -1), new Vector2(-1, 1),
                new Vector2(1, 1), new Vector2(0, -1), new Vector2(-1, 0), new Vector2(0, 1), new Vector2(1, 0),};

        private static final Vector3[] GRAD_3D = {new Vector3(1, 1, 0), new Vector3(-1, 1, 0), new Vector3(1, -1, 0),
                new Vector3(-1, -1, 0), new Vector3(1, 0, 1), new Vector3(-1, 0, 1), new Vector3(1, 0, -1),
                new Vector3(-1, 0, -1), new Vector3(0, 1, 1), new Vector3(0, -1, 1), new Vector3(0, 1, -1),
                new Vector3(0, -1, -1), new Vector3(1, 1, 0), new Vector3(0, -1, 1), new Vector3(-1, 1, 0),
                new Vector3(0, -1, -1),};

        private static final Vector2[] CELL_2D = {new Vector2(-0.4313539279f, 0.1281943404f),
                new Vector2(-0.1733316799f, 0.415278375f), new Vector2(-0.2821957395f, -0.3505218461f),
                new Vector2(-0.2806473808f, 0.3517627718f), new Vector2(0.3125508975f, -0.3237467165f),
                new Vector2(0.3383018443f, -0.2967353402f), new Vector2(-0.4393982022f, -0.09710417025f),
                new Vector2(-0.4460443703f, -0.05953502905f), new Vector2(-0.302223039f, 0.3334085102f),
                new Vector2(-0.212681052f, -0.3965687458f), new Vector2(-0.2991156529f, 0.3361990872f),
                new Vector2(0.2293323691f, 0.3871778202f), new Vector2(0.4475439151f, -0.04695150755f),
                new Vector2(0.1777518f, 0.41340573f), new Vector2(0.1688522499f, -0.4171197882f),
                new Vector2(-0.0976597166f, 0.4392750616f), new Vector2(0.08450188373f, 0.4419948321f),
                new Vector2(-0.4098760448f, -0.1857461384f), new Vector2(0.3476585782f, -0.2857157906f),
                new Vector2(-0.3350670039f, -0.30038326f), new Vector2(0.2298190031f, -0.3868891648f),
                new Vector2(-0.01069924099f, 0.449872789f), new Vector2(-0.4460141246f, -0.05976119672f),
                new Vector2(0.3650293864f, 0.2631606867f), new Vector2(-0.349479423f, 0.2834856838f),
                new Vector2(-0.4122720642f, 0.1803655873f), new Vector2(-0.267327811f, 0.3619887311f),
                new Vector2(0.322124041f, -0.3142230135f), new Vector2(0.2880445931f, -0.3457315612f),
                new Vector2(0.3892170926f, -0.2258540565f), new Vector2(0.4492085018f, -0.02667811596f),
                new Vector2(-0.4497724772f, 0.01430799601f), new Vector2(0.1278175387f, -0.4314657307f),
                new Vector2(-0.03572100503f, 0.4485799926f), new Vector2(-0.4297407068f, -0.1335025276f),
                new Vector2(-0.3217817723f, 0.3145735065f), new Vector2(-0.3057158873f, 0.3302087162f),
                new Vector2(-0.414503978f, 0.1751754899f), new Vector2(-0.3738139881f, 0.2505256519f),
                new Vector2(0.2236891408f, -0.3904653228f), new Vector2(0.002967775577f, -0.4499902136f),
                new Vector2(0.1747128327f, -0.4146991995f), new Vector2(-0.4423772489f, -0.08247647938f),
                new Vector2(-0.2763960987f, -0.355112935f), new Vector2(-0.4019385906f, -0.2023496216f),
                new Vector2(0.3871414161f, -0.2293938184f), new Vector2(-0.430008727f, 0.1326367019f),
                new Vector2(-0.03037574274f, -0.4489736231f), new Vector2(-0.3486181573f, 0.2845441624f),
                new Vector2(0.04553517144f, -0.4476902368f), new Vector2(-0.0375802926f, 0.4484280562f),
                new Vector2(0.3266408905f, 0.3095250049f), new Vector2(0.06540017593f, -0.4452222108f),
                new Vector2(0.03409025829f, 0.448706869f), new Vector2(-0.4449193635f, 0.06742966669f),
                new Vector2(-0.4255936157f, -0.1461850686f), new Vector2(0.449917292f, 0.008627302568f),
                new Vector2(0.05242606404f, 0.4469356864f), new Vector2(-0.4495305179f, -0.02055026661f),
                new Vector2(-0.1204775703f, 0.4335725488f), new Vector2(-0.341986385f, -0.2924813028f),
                new Vector2(0.3865320182f, 0.2304191809f), new Vector2(0.04506097811f, -0.447738214f),
                new Vector2(-0.06283465979f, 0.4455915232f), new Vector2(0.3932600341f, -0.2187385324f),
                new Vector2(0.4472261803f, -0.04988730975f), new Vector2(0.3753571011f, -0.2482076684f),
                new Vector2(-0.273662295f, 0.357223947f), new Vector2(0.1700461538f, 0.4166344988f),
                new Vector2(0.4102692229f, 0.1848760794f), new Vector2(0.323227187f, -0.3130881435f),
                new Vector2(-0.2882310238f, -0.3455761521f), new Vector2(0.2050972664f, 0.4005435199f),
                new Vector2(0.4414085979f, -0.08751256895f), new Vector2(-0.1684700334f, 0.4172743077f),
                new Vector2(-0.003978032396f, 0.4499824166f), new Vector2(-0.2055133639f, 0.4003301853f),
                new Vector2(-0.006095674897f, -0.4499587123f), new Vector2(-0.1196228124f, -0.4338091548f),
                new Vector2(0.3901528491f, -0.2242337048f), new Vector2(0.01723531752f, 0.4496698165f),
                new Vector2(-0.3015070339f, 0.3340561458f), new Vector2(-0.01514262423f, -0.4497451511f),
                new Vector2(-0.4142574071f, -0.1757577897f), new Vector2(-0.1916377265f, -0.4071547394f),
                new Vector2(0.3749248747f, 0.2488600778f), new Vector2(-0.2237774255f, 0.3904147331f),
                new Vector2(-0.4166343106f, -0.1700466149f), new Vector2(0.3619171625f, 0.267424695f),
                new Vector2(0.1891126846f, -0.4083336779f), new Vector2(-0.3127425077f, 0.323561623f),
                new Vector2(-0.3281807787f, 0.307891826f), new Vector2(-0.2294806661f, 0.3870899429f),
                new Vector2(-0.3445266136f, 0.2894847362f), new Vector2(-0.4167095422f, -0.1698621719f),
                new Vector2(-0.257890321f, -0.3687717212f), new Vector2(-0.3612037825f, 0.2683874578f),
                new Vector2(0.2267996491f, 0.3886668486f), new Vector2(0.207157062f, 0.3994821043f),
                new Vector2(0.08355176718f, -0.4421754202f), new Vector2(-0.4312233307f, 0.1286329626f),
                new Vector2(0.3257055497f, 0.3105090899f), new Vector2(0.177701095f, -0.4134275279f),
                new Vector2(-0.445182522f, 0.06566979625f), new Vector2(0.3955143435f, 0.2146355146f),
                new Vector2(-0.4264613988f, 0.1436338239f), new Vector2(-0.3793799665f, -0.2420141339f),
                new Vector2(0.04617599081f, -0.4476245948f), new Vector2(-0.371405428f, -0.2540826796f),
                new Vector2(0.2563570295f, -0.3698392535f), new Vector2(0.03476646309f, 0.4486549822f),
                new Vector2(-0.3065454405f, 0.3294387544f), new Vector2(-0.2256979823f, 0.3893076172f),
                new Vector2(0.4116448463f, -0.1817925206f), new Vector2(-0.2907745828f, -0.3434387019f),
                new Vector2(0.2842278468f, -0.348876097f), new Vector2(0.3114589359f, -0.3247973695f),
                new Vector2(0.4464155859f, -0.0566844308f), new Vector2(-0.3037334033f, -0.3320331606f),
                new Vector2(0.4079607166f, 0.1899159123f), new Vector2(-0.3486948919f, -0.2844501228f),
                new Vector2(0.3264821436f, 0.3096924441f), new Vector2(0.3211142406f, 0.3152548881f),
                new Vector2(0.01183382662f, 0.4498443737f), new Vector2(0.4333844092f, 0.1211526057f),
                new Vector2(0.3118668416f, 0.324405723f), new Vector2(-0.272753471f, 0.3579183483f),
                new Vector2(-0.422228622f, -0.1556373694f), new Vector2(-0.1009700099f, -0.4385260051f),
                new Vector2(-0.2741171231f, -0.3568750521f), new Vector2(-0.1465125133f, 0.4254810025f),
                new Vector2(0.2302279044f, -0.3866459777f), new Vector2(-0.3699435608f, 0.2562064828f),
                new Vector2(0.105700352f, -0.4374099171f), new Vector2(-0.2646713633f, 0.3639355292f),
                new Vector2(0.3521828122f, 0.2801200935f), new Vector2(-0.1864187807f, -0.4095705534f),
                new Vector2(0.1994492955f, -0.4033856449f), new Vector2(0.3937065066f, 0.2179339044f),
                new Vector2(-0.3226158377f, 0.3137180602f), new Vector2(0.3796235338f, 0.2416318948f),
                new Vector2(0.1482921929f, 0.4248640083f), new Vector2(-0.407400394f, 0.1911149365f),
                new Vector2(0.4212853031f, 0.1581729856f), new Vector2(-0.2621297173f, 0.3657704353f),
                new Vector2(-0.2536986953f, -0.3716678248f), new Vector2(-0.2100236383f, 0.3979825013f),
                new Vector2(0.3624152444f, 0.2667493029f), new Vector2(-0.3645038479f, -0.2638881295f),
                new Vector2(0.2318486784f, 0.3856762766f), new Vector2(-0.3260457004f, 0.3101519002f),
                new Vector2(-0.2130045332f, -0.3963950918f), new Vector2(0.3814998766f, -0.2386584257f),
                new Vector2(-0.342977305f, 0.2913186713f), new Vector2(-0.4355865605f, 0.1129794154f),
                new Vector2(-0.2104679605f, 0.3977477059f), new Vector2(0.3348364681f, -0.3006402163f),
                new Vector2(0.3430468811f, 0.2912367377f), new Vector2(-0.2291836801f, -0.3872658529f),
                new Vector2(0.2547707298f, -0.3709337882f), new Vector2(0.4236174945f, -0.151816397f),
                new Vector2(-0.15387742f, 0.4228731957f), new Vector2(-0.4407449312f, 0.09079595574f),
                new Vector2(-0.06805276192f, -0.444824484f), new Vector2(0.4453517192f, -0.06451237284f),
                new Vector2(0.2562464609f, -0.3699158705f), new Vector2(0.3278198355f, -0.3082761026f),
                new Vector2(-0.4122774207f, -0.1803533432f), new Vector2(0.3354090914f, -0.3000012356f),
                new Vector2(0.446632869f, -0.05494615882f), new Vector2(-0.1608953296f, 0.4202531296f),
                new Vector2(-0.09463954939f, 0.4399356268f), new Vector2(-0.02637688324f, -0.4492262904f),
                new Vector2(0.447102804f, -0.05098119915f), new Vector2(-0.4365670908f, 0.1091291678f),
                new Vector2(-0.3959858651f, 0.2137643437f), new Vector2(-0.4240048207f, -0.1507312575f),
                new Vector2(-0.3882794568f, 0.2274622243f), new Vector2(-0.4283652566f, -0.1378521198f),
                new Vector2(0.3303888091f, 0.305521251f), new Vector2(0.3321434919f, -0.3036127481f),
                new Vector2(-0.413021046f, -0.1786438231f), new Vector2(0.08403060337f, -0.4420846725f),
                new Vector2(-0.3822882919f, 0.2373934748f), new Vector2(-0.3712395594f, -0.2543249683f),
                new Vector2(0.4472363971f, -0.04979563372f), new Vector2(-0.4466591209f, 0.05473234629f),
                new Vector2(0.0486272539f, -0.4473649407f), new Vector2(-0.4203101295f, -0.1607463688f),
                new Vector2(0.2205360833f, 0.39225481f), new Vector2(-0.3624900666f, 0.2666476169f),
                new Vector2(-0.4036086833f, -0.1989975647f), new Vector2(0.2152727807f, 0.3951678503f),
                new Vector2(-0.4359392962f, -0.1116106179f), new Vector2(0.4178354266f, 0.1670735057f),
                new Vector2(0.2007630161f, 0.4027334247f), new Vector2(-0.07278067175f, -0.4440754146f),
                new Vector2(0.3644748615f, -0.2639281632f), new Vector2(-0.4317451775f, 0.126870413f),
                new Vector2(-0.297436456f, 0.3376855855f), new Vector2(-0.2998672222f, 0.3355289094f),
                new Vector2(-0.2673674124f, 0.3619594822f), new Vector2(0.2808423357f, 0.3516071423f),
                new Vector2(0.3498946567f, 0.2829730186f), new Vector2(-0.2229685561f, 0.390877248f),
                new Vector2(0.3305823267f, 0.3053118493f), new Vector2(-0.2436681211f, -0.3783197679f),
                new Vector2(-0.03402776529f, 0.4487116125f), new Vector2(-0.319358823f, 0.3170330301f),
                new Vector2(0.4454633477f, -0.06373700535f), new Vector2(0.4483504221f, 0.03849544189f),
                new Vector2(-0.4427358436f, -0.08052932871f), new Vector2(0.05452298565f, 0.4466847255f),
                new Vector2(-0.2812560807f, 0.3512762688f), new Vector2(0.1266696921f, 0.4318041097f),
                new Vector2(-0.3735981243f, 0.2508474468f), new Vector2(0.2959708351f, -0.3389708908f),
                new Vector2(-0.3714377181f, 0.254035473f), new Vector2(-0.404467102f, -0.1972469604f),
                new Vector2(0.1636165687f, -0.419201167f), new Vector2(0.3289185495f, -0.3071035458f),
                new Vector2(-0.2494824991f, -0.3745109914f), new Vector2(0.03283133272f, 0.4488007393f),
                new Vector2(-0.166306057f, -0.4181414777f), new Vector2(-0.106833179f, 0.4371346153f),
                new Vector2(0.06440260376f, -0.4453676062f), new Vector2(-0.4483230967f, 0.03881238203f),
                new Vector2(-0.421377757f, -0.1579265206f), new Vector2(0.05097920662f, -0.4471030312f),
                new Vector2(0.2050584153f, -0.4005634111f), new Vector2(0.4178098529f, -0.167137449f),
                new Vector2(-0.3565189504f, -0.2745801121f), new Vector2(0.4478398129f, 0.04403977727f),
                new Vector2(-0.3399999602f, -0.2947881053f), new Vector2(0.3767121994f, 0.2461461331f),
                new Vector2(-0.3138934434f, 0.3224451987f), new Vector2(-0.1462001792f, -0.4255884251f),
                new Vector2(0.3970290489f, -0.2118205239f), new Vector2(0.4459149305f, -0.06049689889f),
                new Vector2(-0.4104889426f, -0.1843877112f), new Vector2(0.1475103971f, -0.4251360756f),
                new Vector2(0.09258030352f, 0.4403735771f), new Vector2(-0.1589664637f, -0.4209865359f),
                new Vector2(0.2482445008f, 0.3753327428f), new Vector2(0.4383624232f, -0.1016778537f),
                new Vector2(0.06242802956f, 0.4456486745f), new Vector2(0.2846591015f, -0.3485243118f),
                new Vector2(-0.344202744f, -0.2898697484f), new Vector2(0.1198188883f, -0.4337550392f),
                new Vector2(-0.243590703f, 0.3783696201f), new Vector2(0.2958191174f, -0.3391033025f),
                new Vector2(-0.1164007991f, 0.4346847754f), new Vector2(0.1274037151f, -0.4315881062f),
                new Vector2(0.368047306f, 0.2589231171f), new Vector2(0.2451436949f, 0.3773652989f),
                new Vector2(-0.4314509715f, 0.12786735f),};

        private static final Vector3[] CELL_3D = {new Vector3(0.1453787434f, -0.4149781685f, -0.0956981749f),
                new Vector3(-0.01242829687f, -0.1457918398f, -0.4255470325f),
                new Vector3(0.2877979582f, -0.02606483451f, -0.3449535616f),
                new Vector3(-0.07732986802f, 0.2377094325f, 0.3741848704f),
                new Vector3(0.1107205875f, -0.3552302079f, -0.2530858567f),
                new Vector3(0.2755209141f, 0.2640521179f, -0.238463215f),
                new Vector3(0.294168941f, 0.1526064594f, 0.3044271714f),
                new Vector3(0.4000921098f, -0.2034056362f, 0.03244149937f),
                new Vector3(-0.1697304074f, 0.3970864695f, -0.1265461359f),
                new Vector3(-0.1483224484f, -0.3859694688f, 0.1775613147f),
                new Vector3(0.2623596946f, -0.2354852944f, 0.2796677792f),
                new Vector3(-0.2709003183f, 0.3505271138f, -0.07901746678f),
                new Vector3(-0.03516550699f, 0.3885234328f, 0.2243054374f),
                new Vector3(-0.1267712655f, 0.1920044036f, 0.3867342179f),
                new Vector3(0.02952021915f, 0.4409685861f, 0.08470692262f),
                new Vector3(-0.2806854217f, -0.266996757f, 0.2289725438f),
                new Vector3(-0.171159547f, 0.2141185563f, 0.3568720405f),
                new Vector3(0.2113227183f, 0.3902405947f, -0.07453178509f),
                new Vector3(-0.1024352839f, 0.2128044156f, -0.3830421561f),
                new Vector3(-0.3304249877f, -0.1566986703f, 0.2622305365f),
                new Vector3(0.2091111325f, 0.3133278055f, -0.2461670583f),
                new Vector3(0.344678154f, -0.1944240454f, -0.2142341261f),
                new Vector3(0.1984478035f, -0.3214342325f, -0.2445373252f),
                new Vector3(-0.2929008603f, 0.2262915116f, 0.2559320961f),
                new Vector3(-0.1617332831f, 0.006314769776f, -0.4198838754f),
                new Vector3(-0.3582060271f, -0.148303178f, -0.2284613961f),
                new Vector3(-0.1852067326f, -0.3454119342f, -0.2211087107f),
                new Vector3(0.3046301062f, 0.1026310383f, 0.314908508f),
                new Vector3(-0.03816768434f, -0.2551766358f, -0.3686842991f),
                new Vector3(-0.4084952196f, 0.1805950793f, 0.05492788837f),
                new Vector3(-0.02687443361f, -0.2749741471f, 0.3551999201f),
                new Vector3(-0.03801098351f, 0.3277859044f, 0.3059600725f),
                new Vector3(0.2371120802f, 0.2900386767f, -0.2493099024f),
                new Vector3(0.4447660503f, 0.03946930643f, 0.05590469027f),
                new Vector3(0.01985147278f, -0.01503183293f, -0.4493105419f),
                new Vector3(0.4274339143f, 0.03345994256f, -0.1366772882f),
                new Vector3(-0.2072988631f, 0.2871414597f, -0.2776273824f),
                new Vector3(-0.3791240978f, 0.1281177671f, 0.2057929936f),
                new Vector3(-0.2098721267f, -0.1007087278f, -0.3851122467f),
                new Vector3(0.01582798878f, 0.4263894424f, 0.1429738373f),
                new Vector3(-0.1888129464f, -0.3160996813f, -0.2587096108f),
                new Vector3(0.1612988974f, -0.1974805082f, -0.3707885038f),
                new Vector3(-0.08974491322f, 0.229148752f, -0.3767448739f),
                new Vector3(0.07041229526f, 0.4150230285f, -0.1590534329f),
                new Vector3(-0.1082925611f, -0.1586061639f, 0.4069604477f),
                new Vector3(0.2474100658f, -0.3309414609f, 0.1782302128f),
                new Vector3(-0.1068836661f, -0.2701644537f, -0.3436379634f),
                new Vector3(0.2396452163f, 0.06803600538f, -0.3747549496f),
                new Vector3(-0.3063886072f, 0.2597428179f, 0.2028785103f),
                new Vector3(0.1593342891f, -0.3114350249f, -0.2830561951f),
                new Vector3(0.2709690528f, 0.1412648683f, -0.3303331794f),
                new Vector3(-0.1519780427f, 0.3623355133f, 0.2193527988f),
                new Vector3(0.1699773681f, 0.3456012883f, 0.2327390037f),
                new Vector3(-0.1986155616f, 0.3836276443f, -0.1260225743f),
                new Vector3(-0.1887482106f, -0.2050154888f, -0.353330953f),
                new Vector3(0.2659103394f, 0.3015631259f, -0.2021172246f),
                new Vector3(-0.08838976154f, -0.4288819642f, -0.1036702021f),
                new Vector3(-0.04201869311f, 0.3099592485f, 0.3235115047f),
                new Vector3(-0.3230334656f, 0.201549922f, -0.2398478873f),
                new Vector3(0.2612720941f, 0.2759854499f, -0.2409749453f),
                new Vector3(0.385713046f, 0.2193460345f, 0.07491837764f),
                new Vector3(0.07654967953f, 0.3721732183f, 0.241095919f),
                new Vector3(0.4317038818f, -0.02577753072f, 0.1243675091f),
                new Vector3(-0.2890436293f, -0.3418179959f, -0.04598084447f),
                new Vector3(-0.2201947582f, 0.383023377f, -0.08548310451f),
                new Vector3(0.4161322773f, -0.1669634289f, -0.03817251927f),
                new Vector3(0.2204718095f, 0.02654238946f, -0.391391981f),
                new Vector3(-0.1040307469f, 0.3890079625f, -0.2008741118f),
                new Vector3(-0.1432122615f, 0.371614387f, -0.2095065525f),
                new Vector3(0.3978380468f, -0.06206669342f, 0.2009293758f),
                new Vector3(-0.2599274663f, 0.2616724959f, -0.2578084893f),
                new Vector3(0.4032618332f, -0.1124593585f, 0.1650235939f),
                new Vector3(-0.08953470255f, -0.3048244735f, 0.3186935478f),
                new Vector3(0.118937202f, -0.2875221847f, 0.325092195f),
                new Vector3(0.02167047076f, -0.03284630549f, -0.4482761547f),
                new Vector3(-0.3411343612f, 0.2500031105f, 0.1537068389f),
                new Vector3(0.3162964612f, 0.3082064153f, -0.08640228117f),
                new Vector3(0.2355138889f, -0.3439334267f, -0.1695376245f),
                new Vector3(-0.02874541518f, -0.3955933019f, 0.2125550295f),
                new Vector3(-0.2461455173f, 0.02020282325f, -0.3761704803f),
                new Vector3(0.04208029445f, -0.4470439576f, 0.02968078139f),
                new Vector3(0.2727458746f, 0.2288471896f, -0.2752065618f),
                new Vector3(-0.1347522818f, -0.02720848277f, -0.4284874806f),
                new Vector3(0.3829624424f, 0.1231931484f, -0.2016512234f),
                new Vector3(-0.3547613644f, 0.1271702173f, 0.2459107769f),
                new Vector3(0.2305790207f, 0.3063895591f, 0.2354968222f),
                new Vector3(-0.08323845599f, -0.1922245118f, 0.3982726409f),
                new Vector3(0.2993663085f, -0.2619918095f, -0.2103333191f),
                new Vector3(-0.2154865723f, 0.2706747713f, 0.287751117f),
                new Vector3(0.01683355354f, -0.2680655787f, -0.3610505186f),
                new Vector3(0.05240429123f, 0.4335128183f, -0.1087217856f),
                new Vector3(0.00940104872f, -0.4472890582f, 0.04841609928f),
                new Vector3(0.3465688735f, 0.01141914583f, -0.2868093776f),
                new Vector3(-0.3706867948f, -0.2551104378f, 0.003156692623f),
                new Vector3(0.2741169781f, 0.2139972417f, -0.2855959784f),
                new Vector3(0.06413433865f, 0.1708718512f, 0.4113266307f),
                new Vector3(-0.388187972f, -0.03973280434f, -0.2241236325f),
                new Vector3(0.06419469312f, -0.2803682491f, 0.3460819069f),
                new Vector3(-0.1986120739f, -0.3391173584f, 0.2192091725f),
                new Vector3(-0.203203009f, -0.3871641506f, 0.1063600375f),
                new Vector3(-0.1389736354f, -0.2775901578f, -0.3257760473f),
                new Vector3(-0.06555641638f, 0.342253257f, -0.2847192729f),
                new Vector3(-0.2529246486f, -0.2904227915f, 0.2327739768f),
                new Vector3(0.1444476522f, 0.1069184044f, 0.4125570634f),
                new Vector3(-0.3643780054f, -0.2447099973f, -0.09922543227f),
                new Vector3(0.4286142488f, -0.1358496089f, -0.01829506817f),
                new Vector3(0.165872923f, -0.3136808464f, -0.2767498872f),
                new Vector3(0.2219610524f, -0.3658139958f, 0.1393320198f),
                new Vector3(0.04322940318f, -0.3832730794f, 0.2318037215f),
                new Vector3(-0.08481269795f, -0.4404869674f, -0.03574965489f),
                new Vector3(0.1822082075f, -0.3953259299f, 0.1140946023f),
                new Vector3(-0.3269323334f, 0.3036542563f, 0.05838957105f),
                new Vector3(-0.4080485344f, 0.04227858267f, -0.184956522f),
                new Vector3(0.2676025294f, -0.01299671652f, 0.36155217f),
                new Vector3(0.3024892441f, -0.1009990293f, -0.3174892964f),
                new Vector3(0.1448494052f, 0.425921681f, -0.0104580805f),
                new Vector3(0.4198402157f, 0.08062320474f, 0.1404780841f),
                new Vector3(-0.3008872161f, -0.333040905f, -0.03241355801f),
                new Vector3(0.3639310428f, -0.1291284382f, -0.2310412139f),
                new Vector3(0.3295806598f, 0.0184175994f, -0.3058388149f),
                new Vector3(0.2776259487f, -0.2974929052f, -0.1921504723f),
                new Vector3(0.4149000507f, -0.144793182f, -0.09691688386f),
                new Vector3(0.145016715f, -0.0398992945f, 0.4241205002f),
                new Vector3(0.09299023471f, -0.299732164f, -0.3225111565f),
                new Vector3(0.1028907093f, -0.361266869f, 0.247789732f),
                new Vector3(0.2683057049f, -0.07076041213f, -0.3542668666f),
                new Vector3(-0.4227307273f, -0.07933161816f, -0.1323073187f),
                new Vector3(-0.1781224702f, 0.1806857196f, -0.3716517945f),
                new Vector3(0.4390788626f, -0.02841848598f, -0.09435116353f),
                new Vector3(0.2972583585f, 0.2382799621f, -0.2394997452f),
                new Vector3(-0.1707002821f, 0.2215845691f, 0.3525077196f),
                new Vector3(0.3806686614f, 0.1471852559f, -0.1895464869f),
                new Vector3(-0.1751445661f, -0.274887877f, 0.3102596268f),
                new Vector3(-0.2227237566f, -0.2316778837f, 0.3149912482f),
                new Vector3(0.1369633021f, 0.1341343041f, -0.4071228836f),
                new Vector3(-0.3529503428f, -0.2472893463f, -0.129514612f),
                new Vector3(-0.2590744185f, -0.2985577559f, -0.2150435121f),
                new Vector3(-0.3784019401f, 0.2199816631f, -0.1044989934f),
                new Vector3(-0.05635805671f, 0.1485737441f, 0.4210102279f),
                new Vector3(0.3251428613f, 0.09666046873f, -0.2957006485f),
                new Vector3(-0.4190995804f, 0.1406751354f, -0.08405978803f),
                new Vector3(-0.3253150961f, -0.3080335042f, -0.04225456877f),
                new Vector3(0.2857945863f, -0.05796152095f, 0.3427271751f),
                new Vector3(-0.2733604046f, 0.1973770973f, -0.2980207554f),
                new Vector3(0.219003657f, 0.2410037886f, -0.3105713639f),
                new Vector3(0.3182767252f, -0.271342949f, 0.1660509868f),
                new Vector3(-0.03222023115f, -0.3331161506f, -0.300824678f),
                new Vector3(-0.3087780231f, 0.1992794134f, -0.2596995338f),
                new Vector3(-0.06487611647f, -0.4311322747f, 0.1114273361f),
                new Vector3(0.3921171432f, -0.06294284106f, -0.2116183942f),
                new Vector3(-0.1606404506f, -0.358928121f, -0.2187812825f),
                new Vector3(-0.03767771199f, -0.2290351443f, 0.3855169162f),
                new Vector3(0.1394866832f, -0.3602213994f, 0.2308332918f),
                new Vector3(-0.4345093872f, 0.005751117145f, 0.1169124335f),
                new Vector3(-0.1044637494f, 0.4168128432f, -0.1336202785f),
                new Vector3(0.2658727501f, 0.2551943237f, 0.2582393035f),
                new Vector3(0.2051461999f, 0.1975390727f, 0.3484154868f),
                new Vector3(-0.266085566f, 0.23483312f, 0.2766800993f),
                new Vector3(0.07849405464f, -0.3300346342f, -0.2956616708f),
                new Vector3(-0.2160686338f, 0.05376451292f, -0.3910546287f),
                new Vector3(-0.185779186f, 0.2148499206f, 0.3490352499f),
                new Vector3(0.02492421743f, -0.3229954284f, -0.3123343347f),
                new Vector3(-0.120167831f, 0.4017266681f, 0.1633259825f),
                new Vector3(-0.02160084693f, -0.06885389554f, 0.4441762538f),
                new Vector3(0.2597670064f, 0.3096300784f, 0.1978643903f),
                new Vector3(-0.1611553854f, -0.09823036005f, 0.4085091653f),
                new Vector3(-0.3278896792f, 0.1461670309f, 0.2713366126f),
                new Vector3(0.2822734956f, 0.03754421121f, -0.3484423997f),
                new Vector3(0.03169341113f, 0.347405252f, -0.2842624114f),
                new Vector3(0.2202613604f, -0.3460788041f, -0.1849713341f),
                new Vector3(0.2933396046f, 0.3031973659f, 0.1565989581f),
                new Vector3(-0.3194922995f, 0.2453752201f, -0.200538455f),
                new Vector3(-0.3441586045f, -0.1698856132f, -0.2349334659f),
                new Vector3(0.2703645948f, -0.3574277231f, 0.04060059933f),
                new Vector3(0.2298568861f, 0.3744156221f, 0.0973588921f),
                new Vector3(0.09326603877f, -0.3170108894f, 0.3054595587f),
                new Vector3(-0.1116165319f, -0.2985018719f, 0.3177080142f),
                new Vector3(0.2172907365f, -0.3460005203f, -0.1885958001f),
                new Vector3(0.1991339479f, 0.3820341668f, -0.1299829458f),
                new Vector3(-0.0541918155f, -0.2103145071f, 0.39412061f),
                new Vector3(0.08871336998f, 0.2012117383f, 0.3926114802f),
                new Vector3(0.2787673278f, 0.3505404674f, 0.04370535101f),
                new Vector3(-0.322166438f, 0.3067213525f, 0.06804996813f),
                new Vector3(-0.4277366384f, 0.132066775f, 0.04582286686f),
                new Vector3(0.240131882f, -0.1612516055f, 0.344723946f),
                new Vector3(0.1448607981f, -0.2387819045f, 0.3528435224f),
                new Vector3(-0.3837065682f, -0.2206398454f, 0.08116235683f),
                new Vector3(-0.4382627882f, -0.09082753406f, -0.04664855374f),
                new Vector3(-0.37728353f, 0.05445141085f, 0.2391488697f),
                new Vector3(0.1259579313f, 0.348394558f, 0.2554522098f),
                new Vector3(-0.1406285511f, -0.270877371f, -0.3306796947f),
                new Vector3(-0.1580694418f, 0.4162931958f, -0.06491553533f),
                new Vector3(0.2477612106f, -0.2927867412f, -0.2353514536f),
                new Vector3(0.2916132853f, 0.3312535401f, 0.08793624968f),
                new Vector3(0.07365265219f, -0.1666159848f, 0.411478311f),
                new Vector3(-0.26126526f, -0.2422237692f, 0.2748965434f),
                new Vector3(-0.3721862032f, 0.252790166f, 0.008634938242f),
                new Vector3(-0.3691191571f, -0.255281188f, 0.03290232422f),
                new Vector3(0.2278441737f, -0.3358364886f, 0.1944244981f),
                new Vector3(0.363398169f, -0.2310190248f, 0.1306597909f),
                new Vector3(-0.304231482f, -0.2698452035f, 0.1926830856f),
                new Vector3(-0.3199312232f, 0.316332536f, -0.008816977938f),
                new Vector3(0.2874852279f, 0.1642275508f, -0.304764754f),
                new Vector3(-0.1451096801f, 0.3277541114f, -0.2720669462f),
                new Vector3(0.3220090754f, 0.0511344108f, 0.3101538769f),
                new Vector3(-0.1247400865f, -0.04333605335f, -0.4301882115f),
                new Vector3(-0.2829555867f, -0.3056190617f, -0.1703910946f),
                new Vector3(0.1069384374f, 0.3491024667f, -0.2630430352f),
                new Vector3(-0.1420661144f, -0.3055376754f, -0.2982682484f),
                new Vector3(-0.250548338f, 0.3156466809f, -0.2002316239f),
                new Vector3(0.3265787872f, 0.1871229129f, 0.2466400438f),
                new Vector3(0.07646097258f, -0.3026690852f, 0.324106687f),
                new Vector3(0.3451771584f, 0.2757120714f, -0.0856480183f),
                new Vector3(0.298137964f, 0.2852657134f, 0.179547284f),
                new Vector3(0.2812250376f, 0.3466716415f, 0.05684409612f),
                new Vector3(0.4390345476f, -0.09790429955f, -0.01278335452f),
                new Vector3(0.2148373234f, 0.1850172527f, 0.3494474791f),
                new Vector3(0.2595421179f, -0.07946825393f, 0.3589187731f),
                new Vector3(0.3182823114f, -0.307355516f, -0.08203022006f),
                new Vector3(-0.4089859285f, -0.04647718411f, 0.1818526372f),
                new Vector3(-0.2826749061f, 0.07417482322f, 0.3421885344f),
                new Vector3(0.3483864637f, 0.225442246f, -0.1740766085f),
                new Vector3(-0.3226415069f, -0.1420585388f, -0.2796816575f),
                new Vector3(0.4330734858f, -0.118868561f, -0.02859407492f),
                new Vector3(-0.08717822568f, -0.3909896417f, -0.2050050172f),
                new Vector3(-0.2149678299f, 0.3939973956f, -0.03247898316f),
                new Vector3(-0.2687330705f, 0.322686276f, -0.1617284888f),
                new Vector3(0.2105665099f, -0.1961317136f, -0.3459683451f),
                new Vector3(0.4361845915f, -0.1105517485f, 0.004616608544f),
                new Vector3(0.05333333359f, -0.313639498f, -0.3182543336f),
                new Vector3(-0.05986216652f, 0.1361029153f, -0.4247264031f),
                new Vector3(0.3664988455f, 0.2550543014f, -0.05590974511f),
                new Vector3(-0.2341015558f, -0.182405731f, 0.3382670703f),
                new Vector3(-0.04730947785f, -0.4222150243f, -0.1483114513f),
                new Vector3(-0.2391566239f, -0.2577696514f, -0.2808182972f),
                new Vector3(-0.1242081035f, 0.4256953395f, -0.07652336246f),
                new Vector3(0.2614832715f, -0.3650179274f, 0.02980623099f),
                new Vector3(-0.2728794681f, -0.3499628774f, 0.07458404908f),
                new Vector3(0.007892900508f, -0.1672771315f, 0.4176793787f),
                new Vector3(-0.01730330376f, 0.2978486637f, -0.3368779738f),
                new Vector3(0.2054835762f, -0.3252600376f, -0.2334146693f),
                new Vector3(-0.3231994983f, 0.1564282844f, -0.2712420987f),
                new Vector3(-0.2669545963f, 0.2599343665f, -0.2523278991f),
                new Vector3(-0.05554372779f, 0.3170813944f, -0.3144428146f),
                new Vector3(-0.2083935713f, -0.310922837f, -0.2497981362f),
                new Vector3(0.06989323478f, -0.3156141536f, 0.3130537363f),
                new Vector3(0.3847566193f, -0.1605309138f, -0.1693876312f),
                new Vector3(-0.3026215288f, -0.3001537679f, -0.1443188342f),
                new Vector3(0.3450735512f, 0.08611519592f, 0.2756962409f),
                new Vector3(0.1814473292f, -0.2788782453f, -0.3029914042f),
                new Vector3(-0.03855010448f, 0.09795110726f, 0.4375151083f),
                new Vector3(0.3533670318f, 0.2665752752f, 0.08105160988f),
                new Vector3(-0.007945601311f, 0.140359426f, -0.4274764309f),
                new Vector3(0.4063099273f, -0.1491768253f, -0.1231199324f),
                new Vector3(-0.2016773589f, 0.008816271194f, -0.4021797064f),
                new Vector3(-0.07527055435f, -0.425643481f, -0.1251477955f),};

        private static float interpHermiteFunc(float t) {
            return t * t * (3 - 2 * t);
        }

        private static float interpQuinticFunc(float t) {
            return t * t * t * (t * (t * 6 - 15) + 10);
        }

        private void calculateFractalBounding() {
            float amp = m_gain;
            float ampFractal = 1;
            for (int i = 1; i < m_octaves; i++) {
                ampFractal += amp;
                amp *= m_gain;
            }
            m_fractalBounding = 1 / ampFractal;
        }

        // Hashing
        private final static int X_PRIME = 1619;
        private final static int Y_PRIME = 31337;
        private final static int Z_PRIME = 6971;
        private final static int W_PRIME = 1013;

        private static long hash2D(long seed, int x, int y) {
            long hash = seed;
            hash ^= X_PRIME * x;
            hash ^= Y_PRIME * y;

            hash = hash * hash * hash * 60493;
            hash = (hash >> 13) ^ hash;

            return hash;
        }

        private static long hash3D(long seed, int x, int y, int z) {
            long hash = seed;
            hash ^= X_PRIME * x;
            hash ^= Y_PRIME * y;
            hash ^= Z_PRIME * z;

            hash = hash * hash * hash * 60493;
            hash = (hash >> 13) ^ hash;

            return hash;
        }

        private static float valCoord2D(long seed, int x, int y) {
            long n = seed;
            n ^= X_PRIME * x;
            n ^= Y_PRIME * y;

            return (n * n * n * 60493) / (float) 2147483648.0;
        }

        private static float valCoord3D(long seed, int x, int y, int z) {
            long n = seed;
            n ^= X_PRIME * x;
            n ^= Y_PRIME * y;
            n ^= Z_PRIME * z;

            return (n * n * n * 60493) / (float) 2147483648.0;
        }

        private static float gradCoord2D(long seed, int x, int y, float xd, float yd) {
            int hash = toInteger(seed);
            hash ^= X_PRIME * x;
            hash ^= Y_PRIME * y;

            hash = hash * hash * hash * 60493;
            hash = (hash >> 13) ^ hash;

            Vector2 g = GRAD_2D[hash & 7];

            return xd * g.x + yd * g.y;
        }

        private static float gradCoord3D(long seed, int x, int y, int z, float xd, float yd, float zd) {
            int hash = toInteger(seed);
            hash ^= X_PRIME * x;
            hash ^= Y_PRIME * y;
            hash ^= Z_PRIME * z;

            hash = hash * hash * hash * 60493;
            hash = (hash >> 13) ^ hash;

            Vector3 g = GRAD_3D[hash & 15];

            return xd * g.x + yd * g.y + zd * g.z;
        }

        private static float gradCoord4D(long seed, int x, int y, int z, int w, float xd, float yd, float zd,
                                         float wd) {
            int hash = toInteger(seed);
            hash ^= X_PRIME * x;
            hash ^= Y_PRIME * y;
            hash ^= Z_PRIME * z;
            hash ^= W_PRIME * w;

            hash = hash * hash * hash * 60493;
            hash = (hash >> 13) ^ hash;

            hash &= 31;
            float a = yd, b = zd, c = wd; // X,Y,Z
            switch (hash >> 3) { // OR, DEPENDING ON HIGH ORDER 2 BITS:
                case 1:
                    a = wd;
                    b = xd;
                    c = yd;
                    break; // W,X,Y
                case 2:
                    a = zd;
                    b = wd;
                    c = xd;
                    break; // Z,W,X
                case 3:
                    a = yd;
                    b = zd;
                    c = wd;
                    break; // Y,Z,W
            }
            return ((hash & 4) == 0 ? -a : a) + ((hash & 2) == 0 ? -b : b) + ((hash & 1) == 0 ? -c : c);
        }

        public float getNoise(float x, float y, float z) {
            x *= m_frequency;
            y *= m_frequency;
            z *= m_frequency;

            switch (m_noiseType) {
                case PERLIN:
                    return singlePerlin(m_seed, x, y, z);
                case FRACTAL_PERLIN:
                    switch (m_fractalType) {
                        case FBM:
                            return singlePerlinFractalFBM(x, y, z);
                        case BILLOW:
                            return singlePerlinFractalBillow(x, y, z);
                        case RIGID_MULTI:
                            return singlePerlinFractalRigidMulti(x, y, z);
                        default:
                            return 0;
                    }
                case SIMPLEX:
                    return singleSimplex(m_seed, x, y, z);
                case FRACTAL_SIMPLEX:
                    switch (m_fractalType) {
                        case FBM:
                            return singleSimplexFractalFBM(x, y, z);
                        case BILLOW:
                            return singleSimplexFractalBillow(x, y, z);
                        case RIGID_MULTI:
                            return singleSimplexFractalRigidMulti(x, y, z);
                        default:
                            return 0;
                    }
                case CELLULAR:
                    switch (m_cellularReturnType) {
                        case CELL_VALUE:
                        case NOISE_LOOKUP:
                        case DISTANCE:
                            return singleCellular(x, y, z);
                        default:
                            return singleCellular2Edge(x, y, z);
                    }
                default:
                    return 0;
            }
        }

        public float getNoise(float x, float y) {
            x *= m_frequency;
            y *= m_frequency;

            switch (m_noiseType) {
                case PERLIN:
                    return singlePerlin(m_seed, x, y);
                case FRACTAL_PERLIN:
                    switch (m_fractalType) {
                        case FBM:
                            return singlePerlinFractalFBM(x, y);
                        case BILLOW:
                            return singlePerlinFractalBillow(x, y);
                        case RIGID_MULTI:
                            return singlePerlinFractalRigidMulti(x, y);
                        default:
                            return 0;
                    }
                case SIMPLEX:
                    return singleSimplex(m_seed, x, y);
                case FRACTAL_SIMPLEX:
                    switch (m_fractalType) {
                        case FBM:
                            return singleSimplexFractalFBM(x, y);
                        case BILLOW:
                            return singleSimplexFractalBillow(x, y);
                        case RIGID_MULTI:
                            return singleSimplexFractalRigidMulti(x, y);
                        default:
                            return 0;
                    }
                case CELLULAR:
                    switch (m_cellularReturnType) {
                        case CELL_VALUE:
                        case NOISE_LOOKUP:
                        case DISTANCE:
                            return singleCellular(x, y);
                        default:
                            return singleCellular2Edge(x, y);
                    }
                default:
                    return 0;
            }
        }

        // Gradient Noise
        public float getPerlinFractal(float x, float y, float z) {
            x *= m_frequency;
            y *= m_frequency;
            z *= m_frequency;

            switch (m_fractalType) {
                case FBM:
                    return singlePerlinFractalFBM(x, y, z);
                case BILLOW:
                    return singlePerlinFractalBillow(x, y, z);
                case RIGID_MULTI:
                    return singlePerlinFractalRigidMulti(x, y, z);
                default:
                    return 0;
            }
        }

        private float singlePerlinFractalFBM(float x, float y, float z) {
            long seed = m_seed;
            float sum = singlePerlin(seed, x, y, z);
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;
                z *= m_lacunarity;

                amp *= m_gain;
                sum += singlePerlin(++seed, x, y, z) * amp;
            }

            return sum * m_fractalBounding;
        }

        private float singlePerlinFractalBillow(float x, float y, float z) {
            long seed = m_seed;
            float sum = Math.abs(singlePerlin(seed, x, y, z)) * 2 - 1;
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;
                z *= m_lacunarity;

                amp *= m_gain;
                sum += (Math.abs(singlePerlin(++seed, x, y, z)) * 2 - 1) * amp;
            }

            return sum * m_fractalBounding;
        }

        private float singlePerlinFractalRigidMulti(float x, float y, float z) {
            long seed = m_seed;
            float sum = 1 - Math.abs(singlePerlin(seed, x, y, z));
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;
                z *= m_lacunarity;

                amp *= m_gain;
                sum -= (1 - Math.abs(singlePerlin(++seed, x, y, z))) * amp;
            }

            return sum;
        }

        public float getPerlin(float x, float y, float z) {
            return singlePerlin(m_seed, x * m_frequency, y * m_frequency, z * m_frequency);
        }

        private float singlePerlin(long seed, float x, float y, float z) {
            int x0 = fastFloor(x);
            int y0 = fastFloor(y);
            int z0 = fastFloor(z);
            int x1 = x0 + 1;
            int y1 = y0 + 1;
            int z1 = z0 + 1;

            float xs, ys, zs;
            switch (m_interp) {
                default:
                case LINEAR:
                    xs = x - x0;
                    ys = y - y0;
                    zs = z - z0;
                    break;
                case HERMITE:
                    xs = interpHermiteFunc(x - x0);
                    ys = interpHermiteFunc(y - y0);
                    zs = interpHermiteFunc(z - z0);
                    break;
                case QUINTIC:
                    xs = interpQuinticFunc(x - x0);
                    ys = interpQuinticFunc(y - y0);
                    zs = interpQuinticFunc(z - z0);
                    break;
            }

            float xd0 = x - x0;
            float yd0 = y - y0;
            float zd0 = z - z0;
            float xd1 = xd0 - 1;
            float yd1 = yd0 - 1;
            float zd1 = zd0 - 1;

            float xf00 = lerp(gradCoord3D(seed, x0, y0, z0, xd0, yd0, zd0),
                    gradCoord3D(seed, x1, y0, z0, xd1, yd0, zd0), xs);
            float xf10 = lerp(gradCoord3D(seed, x0, y1, z0, xd0, yd1, zd0),
                    gradCoord3D(seed, x1, y1, z0, xd1, yd1, zd0), xs);
            float xf01 = lerp(gradCoord3D(seed, x0, y0, z1, xd0, yd0, zd1),
                    gradCoord3D(seed, x1, y0, z1, xd1, yd0, zd1), xs);
            float xf11 = lerp(gradCoord3D(seed, x0, y1, z1, xd0, yd1, zd1),
                    gradCoord3D(seed, x1, y1, z1, xd1, yd1, zd1), xs);

            float yf0 = lerp(xf00, xf10, ys);
            float yf1 = lerp(xf01, xf11, ys);

            return lerp(yf0, yf1, zs);
        }

        public float getPerlinFractal(float x, float y) {
            x *= m_frequency;
            y *= m_frequency;

            switch (m_fractalType) {
                case FBM:
                    return singlePerlinFractalFBM(x, y);
                case BILLOW:
                    return singlePerlinFractalBillow(x, y);
                case RIGID_MULTI:
                    return singlePerlinFractalRigidMulti(x, y);
                default:
                    return 0;
            }
        }

        private float singlePerlinFractalFBM(float x, float y) {
            long seed = m_seed;
            float sum = singlePerlin(seed, x, y);
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;

                amp *= m_gain;
                sum += singlePerlin(++seed, x, y) * amp;
            }

            return sum * m_fractalBounding;
        }

        private float singlePerlinFractalBillow(float x, float y) {
            long seed = m_seed;
            float sum = Math.abs(singlePerlin(seed, x, y)) * 2 - 1;
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;

                amp *= m_gain;
                sum += (Math.abs(singlePerlin(++seed, x, y)) * 2 - 1) * amp;
            }

            return sum * m_fractalBounding;
        }

        private float singlePerlinFractalRigidMulti(float x, float y) {
            long seed = m_seed;
            float sum = 1 - Math.abs(singlePerlin(seed, x, y));
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;

                amp *= m_gain;
                sum -= (1 - Math.abs(singlePerlin(++seed, x, y))) * amp;
            }

            return sum;
        }

        public float getPerlin(float x, float y) {
            return singlePerlin(m_seed, x * m_frequency, y * m_frequency);
        }

        private float singlePerlin(long seed, float x, float y) {
            int x0 = fastFloor(x);
            int y0 = fastFloor(y);
            int x1 = x0 + 1;
            int y1 = y0 + 1;

            float xs, ys;
            switch (m_interp) {
                default:
                case LINEAR:
                    xs = x - x0;
                    ys = y - y0;
                    break;
                case HERMITE:
                    xs = interpHermiteFunc(x - x0);
                    ys = interpHermiteFunc(y - y0);
                    break;
                case QUINTIC:
                    xs = interpQuinticFunc(x - x0);
                    ys = interpQuinticFunc(y - y0);
                    break;
            }

            float xd0 = x - x0;
            float yd0 = y - y0;
            float xd1 = xd0 - 1;
            float yd1 = yd0 - 1;

            float xf0 = lerp(gradCoord2D(seed, x0, y0, xd0, yd0), gradCoord2D(seed, x1, y0, xd1, yd0), xs);
            float xf1 = lerp(gradCoord2D(seed, x0, y1, xd0, yd1), gradCoord2D(seed, x1, y1, xd1, yd1), xs);

            return lerp(xf0, xf1, ys);
        }

        // Simplex Noise
        public float getSimplexFractal(float x, float y, float z) {
            x *= m_frequency;
            y *= m_frequency;
            z *= m_frequency;

            switch (m_fractalType) {
                case FBM:
                    return singleSimplexFractalFBM(x, y, z);
                case BILLOW:
                    return singleSimplexFractalBillow(x, y, z);
                case RIGID_MULTI:
                    return singleSimplexFractalRigidMulti(x, y, z);
                default:
                    return 0;
            }
        }

        private float singleSimplexFractalFBM(float x, float y, float z) {
            long seed = m_seed;
            float sum = singleSimplex(seed, x, y, z);
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;
                z *= m_lacunarity;

                amp *= m_gain;
                sum += singleSimplex(++seed, x, y, z) * amp;
            }

            return sum * m_fractalBounding;
        }

        private float singleSimplexFractalBillow(float x, float y, float z) {
            long seed = m_seed;
            float sum = Math.abs(singleSimplex(seed, x, y, z)) * 2 - 1;
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;
                z *= m_lacunarity;

                amp *= m_gain;
                sum += (Math.abs(singleSimplex(++seed, x, y, z)) * 2 - 1) * amp;
            }

            return sum * m_fractalBounding;
        }

        private float singleSimplexFractalRigidMulti(float x, float y, float z) {
            long seed = m_seed;
            float sum = 1 - Math.abs(singleSimplex(seed, x, y, z));
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;
                z *= m_lacunarity;

                amp *= m_gain;
                sum -= (1 - Math.abs(singleSimplex(++seed, x, y, z))) * amp;
            }

            return sum;
        }

        public float getSimplex(float x, float y, float z) {
            return singleSimplex(m_seed, x * m_frequency, y * m_frequency, z * m_frequency);
        }

        private final static float F3 = (float) (1.0 / 3.0);
        private final static float G3 = (float) (1.0 / 6.0);
        private final static float G33 = G3 * 3 - 1;

        private float singleSimplex(long seed, float x, float y, float z) {
            float t = (x + y + z) * F3;
            int i = fastFloor(x + t);
            int j = fastFloor(y + t);
            int k = fastFloor(z + t);

            t = (i + j + k) * G3;
            float x0 = x - (i - t);
            float y0 = y - (j - t);
            float z0 = z - (k - t);

            int i1, j1, k1;
            int i2, j2, k2;

            if (x0 >= y0) {
                if (y0 >= z0) {
                    i1 = 1;
                    j1 = 0;
                    k1 = 0;
                    i2 = 1;
                    j2 = 1;
                    k2 = 0;
                } else if (x0 >= z0) {
                    i1 = 1;
                    j1 = 0;
                    k1 = 0;
                    i2 = 1;
                    j2 = 0;
                    k2 = 1;
                } else // x0 < z0
                {
                    i1 = 0;
                    j1 = 0;
                    k1 = 1;
                    i2 = 1;
                    j2 = 0;
                    k2 = 1;
                }
            } else // x0 < y0
            {
                if (y0 < z0) {
                    i1 = 0;
                    j1 = 0;
                    k1 = 1;
                    i2 = 0;
                    j2 = 1;
                    k2 = 1;
                } else if (x0 < z0) {
                    i1 = 0;
                    j1 = 1;
                    k1 = 0;
                    i2 = 0;
                    j2 = 1;
                    k2 = 1;
                } else // x0 >= z0
                {
                    i1 = 0;
                    j1 = 1;
                    k1 = 0;
                    i2 = 1;
                    j2 = 1;
                    k2 = 0;
                }
            }

            float x1 = x0 - i1 + G3;
            float y1 = y0 - j1 + G3;
            float z1 = z0 - k1 + G3;
            float x2 = x0 - i2 + F3;
            float y2 = y0 - j2 + F3;
            float z2 = z0 - k2 + F3;
            float x3 = x0 + G33;
            float y3 = y0 + G33;
            float z3 = z0 + G33;

            float n0, n1, n2, n3;

            t = (float) 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
            if (t < 0)
                n0 = 0;
            else {
                t *= t;
                n0 = t * t * gradCoord3D(seed, i, j, k, x0, y0, z0);
            }

            t = (float) 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
            if (t < 0)
                n1 = 0;
            else {
                t *= t;
                n1 = t * t * gradCoord3D(seed, i + i1, j + j1, k + k1, x1, y1, z1);
            }

            t = (float) 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
            if (t < 0)
                n2 = 0;
            else {
                t *= t;
                n2 = t * t * gradCoord3D(seed, i + i2, j + j2, k + k2, x2, y2, z2);
            }

            t = (float) 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
            if (t < 0)
                n3 = 0;
            else {
                t *= t;
                n3 = t * t * gradCoord3D(seed, i + 1, j + 1, k + 1, x3, y3, z3);
            }

            return 32 * (n0 + n1 + n2 + n3);
        }

        public float getSimplexFractal(float x, float y) {
            x *= m_frequency;
            y *= m_frequency;

            switch (m_fractalType) {
                case FBM:
                    return singleSimplexFractalFBM(x, y);
                case BILLOW:
                    return singleSimplexFractalBillow(x, y);
                case RIGID_MULTI:
                    return singleSimplexFractalRigidMulti(x, y);
                default:
                    return 0;
            }
        }

        private float singleSimplexFractalFBM(float x, float y) {
            long seed = m_seed;
            float sum = singleSimplex(seed, x, y);
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;

                amp *= m_gain;
                sum += singleSimplex(++seed, x, y) * amp;
            }

            return sum * m_fractalBounding;
        }

        private float singleSimplexFractalBillow(float x, float y) {
            long seed = m_seed;
            float sum = Math.abs(singleSimplex(seed, x, y)) * 2 - 1;
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;

                amp *= m_gain;
                sum += (Math.abs(singleSimplex(++seed, x, y)) * 2 - 1) * amp;
            }

            return sum * m_fractalBounding;
        }

        private float singleSimplexFractalRigidMulti(float x, float y) {
            long seed = m_seed;
            float sum = 1 - Math.abs(singleSimplex(seed, x, y));
            float amp = 1;

            for (int i = 1; i < m_octaves; i++) {
                x *= m_lacunarity;
                y *= m_lacunarity;

                amp *= m_gain;
                sum -= (1 - Math.abs(singleSimplex(++seed, x, y))) * amp;
            }

            return sum;
        }

        public float getSimplex(float x, float y) {
            return singleSimplex(m_seed, x * m_frequency, y * m_frequency);
        }

        private final static float F2 = (float) (1.0 / 2.0);
        private final static float G2 = (float) (1.0 / 4.0);

        private float singleSimplex(long seed, float x, float y) {
            float t = (x + y) * F2;
            int i = fastFloor(x + t);
            int j = fastFloor(y + t);

            t = (i + j) * G2;
            float X0 = i - t;
            float Y0 = j - t;

            float x0 = x - X0;
            float y0 = y - Y0;

            int i1, j1;
            if (x0 > y0) {
                i1 = 1;
                j1 = 0;
            } else {
                i1 = 0;
                j1 = 1;
            }

            float x1 = x0 - i1 + G2;
            float y1 = y0 - j1 + G2;
            float x2 = x0 - 1 + F2;
            float y2 = y0 - 1 + F2;

            float n0, n1, n2;

            t = (float) 0.5 - x0 * x0 - y0 * y0;
            if (t < 0)
                n0 = 0;
            else {
                t *= t;
                n0 = t * t * gradCoord2D(seed, i, j, x0, y0);
            }

            t = (float) 0.5 - x1 * x1 - y1 * y1;
            if (t < 0)
                n1 = 0;
            else {
                t *= t;
                n1 = t * t * gradCoord2D(seed, i + i1, j + j1, x1, y1);
            }

            t = (float) 0.5 - x2 * x2 - y2 * y2;
            if (t < 0)
                n2 = 0;
            else {
                t *= t;
                n2 = t * t * gradCoord2D(seed, i + 1, j + 1, x2, y2);
            }

            return 50 * (n0 + n1 + n2);
        }

        public float getSimplex(float x, float y, float z, float w) {
            return singleSimplex(m_seed, x * m_frequency, y * m_frequency, z * m_frequency, w * m_frequency);
        }

        private static final byte[] SIMPLEX_4D = {0, 1, 2, 3, 0, 1, 3, 2, 0, 0, 0, 0, 0, 2, 3, 1, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 1, 2, 3, 0, 0, 2, 1, 3, 0, 0, 0, 0, 0, 3, 1, 2, 0, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 1, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 1, 2, 0, 3, 0, 0, 0, 0, 1, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 0, 1, 2, 3, 1, 0,
                1, 0, 2, 3, 1, 0, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 3, 1, 0, 0, 0, 0, 2, 1, 3, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 3, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1, 2, 3, 0, 2, 1, 0, 0, 0, 0, 3, 1, 2, 0, 2, 1, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 2, 0, 0, 0, 0, 3, 2, 0, 1, 3, 2, 1, 0};

        private final static float F4 = (float) ((2.23606797 - 1.0) / 4.0);
        private final static float G4 = (float) ((5.0 - 2.23606797) / 20.0);

        private float singleSimplex(long seed, float x, float y, float z, float w) {
            float n0, n1, n2, n3, n4;
            float t = (x + y + z + w) * F4;
            int i = fastFloor(x + t);
            int j = fastFloor(y + t);
            int k = fastFloor(z + t);
            int l = fastFloor(w + t);
            t = (i + j + k + l) * G4;
            float X0 = i - t;
            float Y0 = j - t;
            float Z0 = k - t;
            float W0 = l - t;
            float x0 = x - X0;
            float y0 = y - Y0;
            float z0 = z - Z0;
            float w0 = w - W0;

            int c = (x0 > y0) ? 32 : 0;
            c += (x0 > z0) ? 16 : 0;
            c += (y0 > z0) ? 8 : 0;
            c += (x0 > w0) ? 4 : 0;
            c += (y0 > w0) ? 2 : 0;
            c += (z0 > w0) ? 1 : 0;
            c <<= 2;

            int i1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
            int i2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
            int i3 = SIMPLEX_4D[c++] >= 1 ? 1 : 0;
            int j1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
            int j2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
            int j3 = SIMPLEX_4D[c++] >= 1 ? 1 : 0;
            int k1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
            int k2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
            int k3 = SIMPLEX_4D[c++] >= 1 ? 1 : 0;
            int l1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
            int l2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
            int l3 = SIMPLEX_4D[c] >= 1 ? 1 : 0;

            float x1 = x0 - i1 + G4;
            float y1 = y0 - j1 + G4;
            float z1 = z0 - k1 + G4;
            float w1 = w0 - l1 + G4;
            float x2 = x0 - i2 + 2 * G4;
            float y2 = y0 - j2 + 2 * G4;
            float z2 = z0 - k2 + 2 * G4;
            float w2 = w0 - l2 + 2 * G4;
            float x3 = x0 - i3 + 3 * G4;
            float y3 = y0 - j3 + 3 * G4;
            float z3 = z0 - k3 + 3 * G4;
            float w3 = w0 - l3 + 3 * G4;
            float x4 = x0 - 1 + 4 * G4;
            float y4 = y0 - 1 + 4 * G4;
            float z4 = z0 - 1 + 4 * G4;
            float w4 = w0 - 1 + 4 * G4;

            t = (float) 0.6 - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
            if (t < 0)
                n0 = 0;
            else {
                t *= t;
                n0 = t * t * gradCoord4D(seed, i, j, k, l, x0, y0, z0, w0);
            }
            t = (float) 0.6 - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
            if (t < 0)
                n1 = 0;
            else {
                t *= t;
                n1 = t * t * gradCoord4D(seed, i + i1, j + j1, k + k1, l + l1, x1, y1, z1, w1);
            }
            t = (float) 0.6 - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
            if (t < 0)
                n2 = 0;
            else {
                t *= t;
                n2 = t * t * gradCoord4D(seed, i + i2, j + j2, k + k2, l + l2, x2, y2, z2, w2);
            }
            t = (float) 0.6 - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
            if (t < 0)
                n3 = 0;
            else {
                t *= t;
                n3 = t * t * gradCoord4D(seed, i + i3, j + j3, k + k3, l + l3, x3, y3, z3, w3);
            }
            t = (float) 0.6 - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
            if (t < 0)
                n4 = 0;
            else {
                t *= t;
                n4 = t * t * gradCoord4D(seed, i + 1, j + 1, k + 1, l + 1, x4, y4, z4, w4);
            }

            return 27 * (n0 + n1 + n2 + n3 + n4);
        }

        // Cellular Noise
        public float getCellular(float x, float y, float z) {
            x *= m_frequency;
            y *= m_frequency;
            z *= m_frequency;

            switch (m_cellularReturnType) {
                case CELL_VALUE:
                case NOISE_LOOKUP:
                case DISTANCE:
                    return singleCellular(x, y, z);
                default:
                    return singleCellular2Edge(x, y, z);
            }
        }

        private float singleCellular(float x, float y, float z) {
            int xr = fastRound(x);
            int yr = fastRound(y);
            int zr = fastRound(z);

            float distance = 999999;
            int xc = 0, yc = 0, zc = 0;

            switch (m_cellularDistanceFunction) {
                case EUCLIDEAN:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            for (int zi = zr - 1; zi <= zr + 1; zi++) {
                                Vector3 vec = CELL_3D[toInteger(hash3D(m_seed, xi, yi, zi) & 255)];

                                float vecX = xi - x + vec.x;
                                float vecY = yi - y + vec.y;
                                float vecZ = zi - z + vec.z;

                                float newDistance = vecX * vecX + vecY * vecY + vecZ * vecZ;

                                if (newDistance < distance) {
                                    distance = newDistance;
                                    xc = xi;
                                    yc = yi;
                                    zc = zi;
                                }
                            }
                        }
                    }
                    break;
                case MANHATTAN:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            for (int zi = zr - 1; zi <= zr + 1; zi++) {
                                Vector3 vec = CELL_3D[toInteger(hash3D(m_seed, xi, yi, zi)) & 255];

                                float vecX = xi - x + vec.x;
                                float vecY = yi - y + vec.y;
                                float vecZ = zi - z + vec.z;

                                float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ);

                                if (newDistance < distance) {
                                    distance = newDistance;
                                    xc = xi;
                                    yc = yi;
                                    zc = zi;
                                }
                            }
                        }
                    }
                    break;
                case NATURAL:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            for (int zi = zr - 1; zi <= zr + 1; zi++) {
                                Vector3 vec = CELL_3D[toInteger(hash3D(m_seed, xi, yi, zi)) & 255];

                                float vecX = xi - x + vec.x;
                                float vecY = yi - y + vec.y;
                                float vecZ = zi - z + vec.z;

                                float newDistance = (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ))
                                        + (vecX * vecX + vecY * vecY + vecZ * vecZ);

                                if (newDistance < distance) {
                                    distance = newDistance;
                                    xc = xi;
                                    yc = yi;
                                    zc = zi;
                                }
                            }
                        }
                    }
                    break;
            }

            switch (m_cellularReturnType) {
                case CELL_VALUE:
                    return valCoord3D(0, xc, yc, zc);

                case NOISE_LOOKUP:
                    Vector3 vec = CELL_3D[toInteger(hash3D(m_seed, xc, yc, zc)) & 255];
                    return m_cellularNoiseLookup.getNoise(xc + vec.x, yc + vec.y, zc + vec.z);

                case DISTANCE:
                    return distance - 1;
                default:
                    return 0;
            }
        }

        private float singleCellular2Edge(float x, float y, float z) {
            int xr = fastRound(x);
            int yr = fastRound(y);
            int zr = fastRound(z);

            float distance = 999999;
            float distance2 = 999999;

            switch (m_cellularDistanceFunction) {
                case EUCLIDEAN:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            for (int zi = zr - 1; zi <= zr + 1; zi++) {
                                Vector3 vec = CELL_3D[toInteger(hash3D(m_seed, xi, yi, zi)) & 255];

                                float vecX = xi - x + vec.x;
                                float vecY = yi - y + vec.y;
                                float vecZ = zi - z + vec.z;

                                float newDistance = vecX * vecX + vecY * vecY + vecZ * vecZ;

                                distance2 = Math.max(Math.min(distance2, newDistance), distance);
                                distance = Math.min(distance, newDistance);
                            }
                        }
                    }
                    break;
                case MANHATTAN:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            for (int zi = zr - 1; zi <= zr + 1; zi++) {
                                Vector3 vec = CELL_3D[toInteger(hash3D(m_seed, xi, yi, zi)) & 255];

                                float vecX = xi - x + vec.x;
                                float vecY = yi - y + vec.y;
                                float vecZ = zi - z + vec.z;

                                float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ);

                                distance2 = Math.max(Math.min(distance2, newDistance), distance);
                                distance = Math.min(distance, newDistance);
                            }
                        }
                    }
                    break;
                case NATURAL:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            for (int zi = zr - 1; zi <= zr + 1; zi++) {
                                Vector3 vec = CELL_3D[toInteger(hash3D(m_seed, xi, yi, zi)) & 255];

                                float vecX = xi - x + vec.x;
                                float vecY = yi - y + vec.y;
                                float vecZ = zi - z + vec.z;

                                float newDistance = (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ))
                                        + (vecX * vecX + vecY * vecY + vecZ * vecZ);

                                distance2 = Math.max(Math.min(distance2, newDistance), distance);
                                distance = Math.min(distance, newDistance);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

            switch (m_cellularReturnType) {
                case DISTANCE2:
                    return distance2 - 1;
                case DISTANCE2_ADD:
                    return distance2 + distance - 1;
                case DISTANCE2_SUB:
                    return distance2 - distance - 1;
                case DISTANCE2_MUL:
                    return distance2 * distance - 1;
                case DISTANCE2_DIV:
                    return distance / distance2 - 1;
                default:
                    return 0;
            }
        }

        public float getCellular(float x, float y) {
            x *= m_frequency;
            y *= m_frequency;

            switch (m_cellularReturnType) {
                case CELL_VALUE:
                case NOISE_LOOKUP:
                case DISTANCE:
                    return singleCellular(x, y);
                default:
                    return singleCellular2Edge(x, y);
            }
        }

        private float singleCellular(float x, float y) {
            int xr = fastRound(x);
            int yr = fastRound(y);

            float distance = 999999;
            int xc = 0, yc = 0;

            switch (m_cellularDistanceFunction) {
                default:
                case EUCLIDEAN:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            Vector2 vec = CELL_2D[toInteger(hash2D(m_seed, xi, yi)) & 255];

                            float vecX = xi - x + vec.x;
                            float vecY = yi - y + vec.y;

                            float newDistance = vecX * vecX + vecY * vecY;

                            if (newDistance < distance) {
                                distance = newDistance;
                                xc = xi;
                                yc = yi;
                            }
                        }
                    }
                    break;
                case MANHATTAN:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            Vector2 vec = CELL_2D[toInteger(hash2D(m_seed, xi, yi)) & 255];

                            float vecX = xi - x + vec.x;
                            float vecY = yi - y + vec.y;

                            float newDistance = (Math.abs(vecX) + Math.abs(vecY));

                            if (newDistance < distance) {
                                distance = newDistance;
                                xc = xi;
                                yc = yi;
                            }
                        }
                    }
                    break;
                case NATURAL:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            Vector2 vec = CELL_2D[toInteger(hash2D(m_seed, xi, yi)) & 255];

                            float vecX = xi - x + vec.x;
                            float vecY = yi - y + vec.y;

                            float newDistance = (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);

                            if (newDistance < distance) {
                                distance = newDistance;
                                xc = xi;
                                yc = yi;
                            }
                        }
                    }
                    break;
            }

            switch (m_cellularReturnType) {
                case CELL_VALUE:
                    return valCoord2D(0, xc, yc);

                case NOISE_LOOKUP:
                    Vector2 vec = CELL_2D[toInteger(hash2D(m_seed, xc, yc)) & 255];
                    return m_cellularNoiseLookup.getNoise(xc + vec.x, yc + vec.y);

                case DISTANCE:
                    return distance - 1;
                default:
                    return 0;
            }
        }

        private float singleCellular2Edge(float x, float y) {
            int xr = fastRound(x);
            int yr = fastRound(y);

            float distance = 999999;
            float distance2 = 999999;

            switch (m_cellularDistanceFunction) {
                default:
                case EUCLIDEAN:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            Vector2 vec = CELL_2D[toInteger(hash2D(m_seed, xi, yi)) & 255];

                            float vecX = xi - x + vec.x;
                            float vecY = yi - y + vec.y;

                            float newDistance = vecX * vecX + vecY * vecY;

                            distance2 = Math.max(Math.min(distance2, newDistance), distance);
                            distance = Math.min(distance, newDistance);
                        }
                    }
                    break;
                case MANHATTAN:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            Vector2 vec = CELL_2D[toInteger(hash2D(m_seed, xi, yi)) & 255];

                            float vecX = xi - x + vec.x;
                            float vecY = yi - y + vec.y;

                            float newDistance = Math.abs(vecX) + Math.abs(vecY);

                            distance2 = Math.max(Math.min(distance2, newDistance), distance);
                            distance = Math.min(distance, newDistance);
                        }
                    }
                    break;
                case NATURAL:
                    for (int xi = xr - 1; xi <= xr + 1; xi++) {
                        for (int yi = yr - 1; yi <= yr + 1; yi++) {
                            Vector2 vec = CELL_2D[toInteger(hash2D(m_seed, xi, yi)) & 255];

                            float vecX = xi - x + vec.x;
                            float vecY = yi - y + vec.y;

                            float newDistance = (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);

                            distance2 = Math.max(Math.min(distance2, newDistance), distance);
                            distance = Math.min(distance, newDistance);
                        }
                    }
                    break;
            }

            switch (m_cellularReturnType) {
                case DISTANCE2:
                    return distance2 - 1;
                case DISTANCE2_ADD:
                    return distance2 + distance - 1;
                case DISTANCE2_SUB:
                    return distance2 - distance - 1;
                case DISTANCE2_MUL:
                    return distance2 * distance - 1;
                case DISTANCE2_DIV:
                    return distance / distance2 - 1;
                default:
                    return 0;
            }
        }
    }

    public class Erosion {

        private final long seed;
        private final int numIterations;
        private final int radius;
        private final float inertia;
        private final float sedimentCapacityFactor;
        private final float minSedimentCapacity;
        private final float erosionSpeed;
        private final float depositSpeed;
        private final float evaporateSpeed;
        private final float gravity;
        private final int maxDropletLifetime;
        private final float initialWaterVolume;
        private final float initialVelocity;
        private final int mapSize;

        public Erosion(int size, float initialVelocity, float initialVolume, int maxDropletLifetime, float gravity,
                       float evaporationSpeed, float depositSpeed, float erodeSpeed, float minSedimentCapacity,
                       float sedimentCapacityFactor, float inertia, int radius, int iterations) {
            this.seed = MathUtils.seed;
            this.mapSize = size;
            this.initialVelocity = initialVelocity;
            this.initialWaterVolume = initialVolume;
            this.maxDropletLifetime = maxDropletLifetime;
            this.gravity = gravity;
            this.evaporateSpeed = evaporationSpeed;
            this.depositSpeed = depositSpeed;
            this.erosionSpeed = erodeSpeed;
            this.minSedimentCapacity = minSedimentCapacity;
            this.sedimentCapacityFactor = sedimentCapacityFactor;
            this.inertia = inertia;
            this.radius = radius;
            this.numIterations = iterations;
        }

        public float[][] erode(float[][] map) {
            // Calculate the max value for both axis of the heightmap
            final float xMax = mapSize - 1.f;
            final float yMax = mapSize - 1.f;

            Thread threadA = new Thread(() -> {
                final Random r = new Random(seed);

                for (int iteration = 0; iteration < numIterations; iteration++) {
                    Droplet droplet;
                    try {
                        droplet = new Droplet(r.nextInt((int) (radius - (xMax - radius))) + (xMax - radius),
                                r.nextInt((int) (radius - (yMax - radius))) + (yMax - radius), 0f, 0f, 0, 0, 0f, 0f,
                                initialVelocity, initialWaterVolume, 0f);
                        // Make new droplet, with random position
                    } catch (IllegalArgumentException ignore) {
                        // Ignore. This should actually happen.

                        droplet = new Droplet(0, 0, 0f, 0f, 0, 0, 0f, 0f, initialVelocity, initialWaterVolume, 0f);
                        // Make new droplet, at 0, 0
                    }

                    for (int lifetime = 0; lifetime < maxDropletLifetime; lifetime++) {
                        // Cast position floats to ints
                        droplet.node = new Node((int) droplet.position.x, (int) droplet.position.y);

                        // Calculate droplet's offset inside the cell (0,0) = at NW node, (1,1) = at SE node
                        droplet.cellOffset.x = droplet.position.x - droplet.node.x;
                        droplet.cellOffset.y = droplet.position.y - droplet.node.y;

                        // Calculate droplet's height and direction of flow with bilinear interpolation of surrounding heights
                        HeightAndGradient heightAndGradient = calculateHeightAndGradient(map, droplet);

                        // Update the droplet's direction and position (move position 1 unit regardless of speed)
                        droplet.direction.x = (droplet.direction.x * inertia
                                - heightAndGradient.gradient.x * (1 - inertia));
                        droplet.direction.y = (droplet.direction.y * inertia
                                - heightAndGradient.gradient.y * (1 - inertia));

                        // Normalize direction
                        float len = (float) Math.sqrt(droplet.direction.x * droplet.direction.x
                                + droplet.direction.y * droplet.direction.y);
                        if (len != 0) {
                            droplet.direction.x /= len;
                            droplet.direction.y /= len;
                        }
                        droplet.position.x += droplet.direction.x;
                        droplet.position.y += droplet.direction.y;

                        // Stop simulating droplet if it's not moving or has flowed over edge of map
                        if ((droplet.direction.x == 0 && droplet.direction.y == 0) || droplet.position.x < 0
                                || droplet.position.x >= xMax || droplet.position.y < 0
                                || droplet.position.y >= yMax || droplet.node.x < 0 || droplet.node.y < 0) {
                            break;
                        }

                        // Find the droplet's new height and calculate the deltaHeight
                        float newHeight = calculateHeightAndGradient(map, droplet).height;
                        float oldHeight = heightAndGradient.height;
                        float deltaHeight = newHeight - oldHeight;

                        // Calculate the droplet's sediment capacity (higher when moving fast down a slope and contains lots of water)
                        final float sedimentCapacity = Math.max(
                                -deltaHeight * droplet.velocity * droplet.water * sedimentCapacityFactor,
                                minSedimentCapacity);

                        // If carrying more sediment than capacity, or if flowing uphill:
                        if (droplet.sediment > sedimentCapacity || deltaHeight > 0) {
                            deposit(map, droplet, deltaHeight, sedimentCapacity);
                        } else {
                            erode(map, droplet, deltaHeight, sedimentCapacity);
                        }

                        // Update droplet's speed and water content
                        droplet.velocity = (float) Math
                                .sqrt(Math.abs(droplet.velocity * droplet.velocity + deltaHeight * gravity));
                        droplet.water *= (1 - evaporateSpeed);

                    }
                }
            });
            threadA.start();

            try {
                threadA.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return map;
        }

        private HeightAndGradient calculateHeightAndGradient(float[][] map, Droplet droplet) {
            int nodeX = (int) droplet.position.x;
            int nodeY = (int) droplet.position.y;

            // Calculate droplet's offset inside the cell (0,0) = at NW node, (1,1) = at SE node
            float offsetX = droplet.position.x - nodeX;
            float offsetY = droplet.position.y - nodeY;

            // Get heights of the four nodes of this cell
            float heightNW = map[nodeX][nodeY];
            float heightNE = map[nodeX + 1][nodeY];
            float heightSW = map[nodeX][nodeY + 1];
            float heightSE = map[nodeX + 1][nodeY + 1];

            // Calculate droplet's direction of flow with bilinear interpolation of height difference along the edges
            float gradientX = (heightNE - heightNW) * (1 - offsetY) + (heightSE - heightSW) * offsetY;
            float gradientY = (heightSW - heightNW) * (1 - offsetX) + (heightSE - heightNE) * offsetX;

            // Calculate height with bilinear interpolation of the heights of the nodes of the cell
            float height = heightNW * (1 - offsetX) * (1 - offsetY) + heightNE * offsetX * (1 - offsetY)
                    + heightSW * (1 - offsetX) * offsetY + heightSE * offsetX * offsetY;

            return new HeightAndGradient(height, gradientX, gradientY);
        }

        private void deposit(float[][] map, Droplet droplet, float deltaHeight, float sedimentCapacity) {
            // If moving uphill (deltaHeight > 0) try fill up to the current height, otherwise deposit a fraction of the excess sediment
            float amountToDeposit = (deltaHeight > 0) ? Math.min(deltaHeight, droplet.sediment)
                    : (droplet.sediment - sedimentCapacity) * depositSpeed;
            droplet.sediment -= amountToDeposit;

            // Add the sediment to the four nodes of the current cell using bilinear interpolation
            // Deposition is not distributed over a radius (like erosion) so that it can fill small pits
            map[droplet.node.x][droplet.node.y] += amountToDeposit * (1 - droplet.cellOffset.x)
                    * (1 - droplet.cellOffset.y);
            map[droplet.node.x + 1][droplet.node.y] += amountToDeposit * droplet.cellOffset.x
                    * (1 - droplet.cellOffset.y);
            map[droplet.node.x][droplet.node.y + 1] += amountToDeposit * (1 - droplet.cellOffset.x)
                    * droplet.cellOffset.y;
            map[droplet.node.x + 1][droplet.node.y + 1] += amountToDeposit * droplet.cellOffset.x
                    * droplet.cellOffset.y;

        }

        private void erode(float[][] map, Droplet droplet, float deltaHeight, float sedimentCapacity) {
            // Erode a fraction of the droplet's current carry capacity.
            // Clamp the erosion to the change in height so that it doesn't dig a hole in the terrain behind the droplet
            float amountToErode = Math.min((sedimentCapacity - droplet.sediment) * erosionSpeed, -deltaHeight);

            Brush brush = new Brush(Math.max(0, (int) droplet.position.x - radius),
                    Math.max(0, (int) droplet.position.y - radius),
                    Math.min(mapSize - 1, (int) droplet.position.x + radius),
                    Math.min(mapSize - 1, (int) droplet.position.y + radius), 0f);

            // Calculate the weights of each point on the brush
            for (int x = brush.start.x; x <= brush.end.x; x++) {
                for (int y = brush.start.y; y <= brush.end.y; y++) {
                    float sqrDst = (x - (int) droplet.position.x) * (x - (int) droplet.position.x)
                            + (y - (int) droplet.position.y) * (y - (int) droplet.position.y);
                    if (sqrDst <= radius * radius) {
                        float weight = (float) (1 - Math.sqrt(sqrDst) / radius);
                        brush.weightSum += weight;
                        brush.weights[x - brush.start.x][y - brush.start.y] = weight;
                    }
                }
            }

            // Erode the map proporsional to the weights in the brush
            for (int x = brush.start.x; x <= brush.end.x; x++) {
                for (int y = brush.start.y; y <= brush.end.y; y++) {
                    brush.weights[x - brush.start.x][y - brush.start.y] /= brush.weightSum;
                    float weighedErodeAmount = amountToErode * brush.weights[x - brush.start.x][y - brush.start.y];
                    float deltaSediment = Math.min(map[x][y], weighedErodeAmount);
                    map[x][y] -= deltaSediment;
                    droplet.sediment += deltaSediment;
                }
            }
        }

        private class Node {
            public final int x;
            public final int y;

            private Node(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }

        private class HeightAndGradient {
            public float height;
            public final Vector2 gradient;

            private HeightAndGradient(float height, Vector2 gradient) {
                this.height = height;
                this.gradient = gradient;
            }

            private HeightAndGradient(float height, float x, float y) {
                this(height, new Vector2(x, y));
            }
        }

        private class Droplet {
            public final Vector2 position;
            public final Vector2 direction;
            public Node node;
            public final Vector2 cellOffset;
            public float velocity;
            public float sediment;
            public float water;

            private Droplet(Vector2 position, Vector2 direction, Node node, Vector2 cellOffset, float velocity,
                            float sediment, float water) {
                this.position = position;
                this.direction = direction;
                this.node = node;
                this.cellOffset = cellOffset;
                this.velocity = velocity;
                this.sediment = sediment;
                this.water = water;
            }

            private Droplet(float x, float y, float yaw, float pitch, int startX, int startY, float cellX, float cellY,
                            float velocity, float sediment, float water) {
                this(new Vector2(x, y), new Vector2(yaw, pitch), new Node(startX, startY), new Vector2(cellX, cellY),
                        velocity, sediment, water);
            }
        }

        private class Brush {
            public final Node start;
            public final Node end;
            public float[][] weights = new float[mapSize][mapSize];
            public float weightSum;

            private Brush(Node start, Node end, float weightSum) {
                this.start = start;
                this.end = end;
                this.weightSum = weightSum;
            }

            private Brush(int x1, int y1, int x2, int y2, float weightSum) {
                this(new Node(x1, y1), new Node(x2, y2), weightSum);
            }
        }
    }

    public static ArrayList<Vector3> brasenhamLine(final Vector3 pos1, final Vector3 pos2) {
        final ArrayList<Vector3> output = new ArrayList<>();
        int x1 = (int) pos1.x;
        int y1 = (int) pos1.y;
        int z1 = (int) pos1.z;
        int x2 = (int) pos2.x;
        int y2 = (int) pos2.y;
        int z2 = (int) pos2.z;
        output.add(new Vector3(x1, y1, z1));
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dz = Math.abs(z2 - z1);

        int xs = x2 > x1 ? 1 : -1;
        int ys = y2 > y1 ? 1 : -1;
        int zs = z2 > z1 ? 1 : -1;

        if (dx >= dy && dx >= dz) {
            int p1 = 2 * dy - dx;
            int p2 = 2 * dz - dx;
            while (x1 != x2) {
                x1 += xs;
                if (p1 >= 0) {
                    y1 += ys;
                    p1 -= 2 * dx;
                }
                if (p2 >= 0) {
                    z1 += zs;
                    p2 -= 2 * dx;
                }
                p1 += 2 * dy;
                p2 += 2 * dz;
                output.add(new Vector3(x1, y1, z1));
            }
        } else if (dy >= dx && dy >= dz) {
            int p1 = 2 * dx - dy;
            int p2 = 2 * dz - dy;
            while (y1 != y2) {
                y1 += ys;
                if (p1 >= 0) {
                    x1 += xs;
                    p1 -= 2 * dy;
                }
                if (p2 >= 0) {
                    z1 += zs;
                    p2 -= 2 * dy;
                }
                p1 += 2 * dx;
                p2 += 2 * dz;
                output.add(new Vector3(x1, y1, z1));
            }
        } else {
            int p1 = 2 * dy - dz;
            int p2 = 2 * dx - dz;
            while (z1 != z2) {
                z1 += zs;
                if (p1 >= 0) {
                    y1 += ys;
                    p1 -= 2 * dz;
                }
                if (p2 >= 0) {
                    x1 += xs;
                    p2 -= 2 * dz;
                }
                p1 += 2 * dy;
                p2 += 2 * dx;
                output.add(new Vector3(x1, y1, z1));
            }
        }
        return output;
    }

    public static class Vector2 {
        public float x, y;

        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public double distance(Vector2 otherPoint) {
            double d1 = this.x - otherPoint.x;
            double d2 = this.y - otherPoint.y;
            return Math.sqrt(d1 * d1 + d2 * d2);
        }
    }

    public enum RotationAxis {
        X, Y, Z
    }

    @Data
    public static class Rotation {

        public float x, y, z;
        float rX, rY, rZ;

        public Vector3 realVector() {
            return new Vector3(x, y, z);
        }

        public Vector3 toVector() {
            return new Vector3(rX, rY, rZ);
        }

        public Rotation(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;

            recalculateValues();
        }

        public void addRotation(float rotation, RotationAxis axis) {
            switch (axis) {
                default:
                case X:
                    this.x += rotation;
                    break;
                case Y:
                    this.y += rotation;
                    break;
                case Z:
                    this.z += rotation;
                    break;
            }

            recalculateValues();
        }

        void recalculateValues() {
            if (x < -180 || x > 180)
                x = -(x) + (x % 180 * 2);
            if (y < -180 || y > 180)
                y = -(y) + (y % 180 * 2);
            if (z < -180 || z > 180)
                z = -(z) + (z % 180 * 2);

            rX = map(x);
            rY = map(y);
            rZ = map(z);
        }
    }

    /**
     * Maps values to fit in the range of 1 to -1.
     *
     * @param value the value. (between -180 and 180)
     * @return mapped value.
     */
    static float map(float value) {
        return (value + 180.f) * 2.f / (180.f + 180.f);
    }

    @Data
    @RequiredArgsConstructor
    public static class Vector3 {
        @NonNull
        public float x, y, z;

        public double distance(Vector3 otherPoint) {
            double d1 = this.x - otherPoint.x;
            double d2 = this.y - otherPoint.y;
            double d3 = this.y - otherPoint.y;
            return Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
        }

        public static Vector3 add(final Vector3 vec1, final Vector3 vec2) {
            return new Vector3(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
        }

        public static Vector3 sub(final Vector3 vec1, final Vector3 vec2) {
            return new Vector3(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
        }

        public static Vector3 div(final Vector3 vec1, final int n) {
            return new Vector3(vec1.x / n, vec1.y / n, vec1.z / n);
        }

        public static Vector3 mul(final Vector3 vec1, final int n) {
            return new Vector3(vec1.x * n, vec1.y * n, vec1.z * n);
        }

        public static Vector3 mul(final Vector3 vec1, final Vector3 vec2) {
            return new Vector3(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
        }

        public static Vector3 normalize(Vector3 vec1) {
            final double length = length(vec1);
            return new Vector3((float) (vec1.x / length), (float) (vec1.y / length), (float) (vec1.z / length));
        }

        public static double length(final Vector3 vec1) {
            return Math.sqrt(Math.pow(vec1.x, 2.0) + Math.pow(vec1.y, 2.0) + Math.pow(vec1.z, 2.0));
        }

        public static double distance(Vector3 vec1, Vector3 vec2) {
            return Math.sqrt(Math.pow(vec2.x - vec1.x, 2.0) + Math.pow(vec2.y - vec1.y, 2.0) + Math.pow(vec2.z - vec1.z, 2.0));
        }
    }

    public static float fastPow(final double base, final double exponent) {
        return (float) Double.longBitsToDouble(
                (long) (exponent * (Double.doubleToLongBits(base) - 4606921280493453312L)) + 4606921280493453312L);
    }

    public static int toInteger(final long l) {
        try {
            return Math.toIntExact(l);
        } catch (ArithmeticException ex) {
            if (l > Integer.MAX_VALUE)
                return Integer.MAX_VALUE;
            else
                return Integer.MIN_VALUE;
        }
    }

    public static int toInteger(final Float f) {
        return f.intValue();
    }

    public static int fastFloor(float f) {
        return (f >= 0 ? (int) f : (int) f - 1);
    }

    public static int fastRound(float f) {
        return (f >= 0) ? (int) (f + (float) 0.5) : (int) (f - (float) 0.5);
    }

    private static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    public static Vector getRandomPerpVector(Vector paramVector) {
        Vector vector = new Vector(randomDouble(-1.0D, 1.0D), randomDouble(-1.0D, 1.0D), randomDouble(-1.0D, 1.0D));
        return vector.subtract(paramVector.clone().multiply(vector.dot(paramVector) / paramVector.dot(paramVector)));
    }

    public static Vector getPerpVector(Vector paramVector1, Vector paramVector2) {
        double d1 = paramVector1.getY() * paramVector2.getZ() - paramVector1.getZ() * paramVector2.getY();
        double d2 = paramVector1.getZ() * paramVector2.getX() - paramVector1.getX() * paramVector2.getZ();
        double d3 = paramVector1.getX() * paramVector2.getY() - paramVector1.getY() * paramVector2.getX();
        return new Vector(d1, d2, d3);
    }

    public static double randomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static long randomLong() {
        return random.nextLong();
    }

    public static double map(double paramDouble, Range range1, Range range2) {
        double d = range1.asDouble();
        if (d != 0.0D) {
            double d1 = range2.asDouble();
            return (paramDouble - range1.getMin()) * d1 / d + range2.getMin();
        }
        return paramDouble;
    }

    public static double getMagnitude(Vector paramVector) {
        return Math.sqrt(paramVector.getX() * paramVector.getX() + paramVector.getY() * paramVector.getY()
                + paramVector.getZ() * paramVector.getZ());
    }

    public static Vector setMagnitude(Vector paramVector, double paramDouble) {
        double d = Math.sqrt(paramDouble * paramDouble / (paramVector.getX() * paramVector.getX()
                + paramVector.getY() * paramVector.getY() + paramVector.getZ() * paramVector.getZ()));
        return paramVector.clone().multiply(d);
    }

    public static Vector getRotatedVector(Vector object, Vector angleVector, double angle) {
        return object.clone().rotateAroundAxis(angleVector, angle);
    }

}
