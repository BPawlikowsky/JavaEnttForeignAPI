package testing;

import jdk.incubator.foreign.*;
import testing.entt.Entt;

import static testing.entt.Entt.*;

public class Testing {
    public static void main(String[] args) {
        var entt = new Entt();
        SegmentAllocator allocator = SegmentAllocator.arenaAllocator(ResourceScope.newSharedScope());
        var entities = 100;
        var layout = MemoryLayout.sequenceLayout(entities, point);
        var seg = allocator.allocate(layout);
        var st = System.nanoTime();
        seg.elements(point).forEach((s) -> {
                    var VHx = point.varHandle(float.class, MemoryLayout.PathElement.groupElement("x"));
                    var VHy = point.varHandle(float.class, MemoryLayout.PathElement.groupElement("y"));
            VHx.set(s, 32.2f);
            VHy.set(s, 23.2f);
            entt.create(s);
        });
        var en = System.nanoTime();

        entt.update();

        seg.elements(point).forEach((s) -> {
            var VHx = point.varHandle(float.class, MemoryLayout.PathElement.groupElement("x"));
            var VHy = point.varHandle(float.class, MemoryLayout.PathElement.groupElement("y"));
            System.out.println(VHx.get(s) + " " + VHy.get(s) + s.address());
        });

        System.out.println("[INFO] Time taken: " + (en - st) * 0.000001f + "ms");
    }
}
