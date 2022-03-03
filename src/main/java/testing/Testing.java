package testing;

import jdk.incubator.foreign.*;
import testing.entt.Entt;
import testing.util.Timer;

import static testing.entt.Entt.*;

public class Testing {
    private static long toMB(long mem) {
        return (long)((mem / 1024) * 0.00097656f);
    }
    public static void main(String[] args) {
        var memStart = Runtime.getRuntime().freeMemory();
        var entt = new Entt();
        var entities = 1000000;

        Timer.start("Initialize SegmentAllocator");
        SegmentAllocator allocator = SegmentAllocator.arenaAllocator(ResourceScope.newSharedScope());
        Timer.end();

        Timer.start("Allocate segment for " + entities + " points.");
        var layout = MemoryLayout.sequenceLayout(entities, point);
        var seg = allocator.allocate(layout);
        Timer.end();

        Timer.start("Fill segment with float values");
        seg.elements(point).forEach((s) -> {
            var VHx = point.varHandle(float.class, MemoryLayout.PathElement.groupElement("x"));
            var VHy = point.varHandle(float.class, MemoryLayout.PathElement.groupElement("y"));
            VHx.set(s, 0.0f);
            VHy.set(s, 0.0f);
        });
        Timer.end();

        Timer.start("register position - native code");
        entt.registerPosition(seg);
        Timer.end();

        Timer.start("register all positions - native code");
        entt.registerPositions(seg, entities);
        Timer.end();

        Timer.start("Update all positions - native code");
        entt.update();
        Timer.end();

        Timer.start("Copy positions to arr and get arr pointer - native code");
        var addr = entt.getPositions(layout);
        Timer.end();

        Timer.start("MemoryAddress.asSegment()");
        var newSeg = addr.asSegment(entt.getCapasity() * point.byteSize(), seg.scope());
        Timer.end();

        Timer.start("Copy positions from segment to float array(MemorySegmaent.toFloatArray())");
        float[] pointsFromCpp = newSeg.toFloatArray();
        Timer.end();

        for (int i = 0; i < pointsFromCpp.length / 2; i+=2) {
            System.out.println("(" + (int)(i * 0.5f) + ") x: " + pointsFromCpp[i] + " y: " + pointsFromCpp[i+1]);
        }

        // RESULTS
        System.out.println("Number of point structs: " + entities);
        for (var el :
                Timer.getResults()) {
            System.out.println(el);
        }
    }
}
