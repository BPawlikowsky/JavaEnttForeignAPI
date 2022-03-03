package testing;

import jdk.incubator.foreign.*;
import testing.entt.Entt;
import testing.util.Timer;

import static testing.entt.Entt.*;

public class Testing {
    public static void main(String[] args) {
        var entt = new Entt();

        Timer.start("Initialize SegmentAllocator");
        SegmentAllocator allocator = SegmentAllocator.arenaAllocator(ResourceScope.newSharedScope());
        Timer.end();
        Timer.start("Allocate memory");
        var entities = 100;
        var layout = MemoryLayout.sequenceLayout(entities, point);
        var seg = allocator.allocate(layout);
        Timer.end();
        Timer.start("Fill memory");
        seg.elements(point).forEach((s) -> {
                    var VHx = point.varHandle(float.class, MemoryLayout.PathElement.groupElement("x"));
                    var VHy = point.varHandle(float.class, MemoryLayout.PathElement.groupElement("y"));
            VHx.set(s, 32.2f);
            VHy.set(s, 23.2f);
            Timer.start("Create function");
            entt.create(s);
            Timer.end();
        });
        Timer.end();

        Timer.start("Update function");
        entt.update();
        Timer.end();

        Timer.start("Copy memory to array");
        float[] pointsFromCpp = seg.toFloatArray();
        Timer.end();

        for (var el :
                Timer.getResults()) {
            System.out.println(el);
        }

        for (int i = 0; i < pointsFromCpp.length; i+=2) {
            System.out.println("(" + (int)(i * 0.5f) + ") x: " + pointsFromCpp[i] + " y: " + pointsFromCpp[i+1]);
        }

    }
}
