import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mccore.rand.seed.StructureSeed;
import com.seedfinding.mccore.state.Dimension;
import com.seedfinding.mccore.util.pos.CPos;
import com.seedfinding.mccore.util.pos.RPos;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcfeature.structure.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int MAX_OFFSET = 4;
    public static MCVersion VERSION = MCVersion.v1_19;
    public static BuriedTreasure BURIEDTRESURE = new BuriedTreasure(VERSION);
    public static PillagerOutpost PILLAGEROUTPOST = new PillagerOutpost(VERSION);
    public static Shipwreck SHIPWRECK = new Shipwreck(VERSION);
    public static SwampHut SWAMPHUT = new SwampHut(VERSION);
    public static RuinedPortal RUINEDPORTAL = new RuinedPortal(Dimension.OVERWORLD, VERSION);
    public static Monument MONUMENT = new Monument(VERSION);


    public static List<CPos> buriedTreasures = new ArrayList<CPos>() {{
        add(new CPos(305, -20));
        add(new CPos(178, -100));
        add(new CPos(145, -136));
    }};

    public static List<CPos> pillagerOutposts = new ArrayList<CPos>() {{
        add(new CPos(164, 83));
    }};

    public static List<CPos> shipwrecks = new ArrayList<CPos>() {{
        add(new CPos(275, -57));
        add(new CPos(254, -19));
        add(new CPos(-41, -81));
    }};

    public static List<CPos> swampHuts = new ArrayList<CPos>() {{
        add(new CPos(206, -137));
    }};

    public static List<CPos> ruinedPortals = new ArrayList<CPos>() {{
    }};

    public static List<CPos> monuments = new ArrayList<CPos>() {{
    }};

    public static void main(String[] args) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter("maching_structure_seeds.txt", false));

        StructureSeed.iterator()
                .asStream()
                .parallel()
                .filter(Main::check)
                .forEach(x -> {
                    try {
                        writer.write(x + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
        ;
        writer.flush();
        writer.close();
        ;
    }

    public static boolean check(long seed) {
        ChunkRand rand = new ChunkRand();

        if (!checkStructure(seed, rand, BURIEDTRESURE, buriedTreasures, MAX_OFFSET)) return false;
        if (!checkStructure(seed, rand, PILLAGEROUTPOST, pillagerOutposts, MAX_OFFSET)) return false;
        if (!checkStructure(seed, rand, SHIPWRECK, shipwrecks, MAX_OFFSET)) return false;
        if (!checkStructure(seed, rand, SWAMPHUT, swampHuts, MAX_OFFSET)) return false;
        if (!checkStructure(seed, rand, RUINEDPORTAL, ruinedPortals, MAX_OFFSET)) return false;
        if (!checkStructure(seed, rand, MONUMENT, monuments, MAX_OFFSET)) return false;

        return true;
    }

    public static boolean checkStructure(long seed, ChunkRand rand, RegionStructure structureType, List<CPos> structurePositions, int maxOffset) {
        for (CPos cpos : structurePositions) {
            RPos rPos = cpos.toRegionPos(structureType.getSpacing());
            CPos pos = structureType.getInRegion(seed, rPos.getX(), rPos.getZ(), rand);
            if (pos == null || distance(cpos, pos) > maxOffset) {
                return false;
            }
        }
        return true;
    }

    public static int distance(CPos a, CPos b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getZ() - b.getZ());
    }


}
