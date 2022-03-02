package testing;

import jdk.incubator.foreign.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static jdk.incubator.foreign.CLinker.*;

public class Testing {
    static {
        System.loadLibrary("java_entt_registry");
    }

    static GroupLayout point = MemoryLayout.structLayout(
            C_FLOAT.withName("x"),
            C_FLOAT.withName("y")
    );

    static SymbolLookup lookup = SymbolLookup.loaderLookup();
    static MemoryAddress init = lookup.lookup("init").get();
    static MemoryAddress create = lookup.lookup("create").get();
    static MemoryAddress update = lookup.lookup("update").get();

    public static void main(String[] args) throws Throwable {
        MethodHandle cinit = CLinker.getInstance().downcallHandle(
                init,
                MethodType.methodType(void.class),
                FunctionDescriptor.ofVoid()
        );
        MethodHandle ccreate = CLinker.getInstance().downcallHandle(
                create,
                MethodType.methodType(MemorySegment.class, MemoryAddress.class),
                FunctionDescriptor.of(point, C_POINTER)
        );
        MethodHandle cupdate = CLinker.getInstance().downcallHandle(
                update,
                MethodType.methodType(int.class),
                FunctionDescriptor.of(C_INT)
        );
        MemorySegment seg;
        SegmentAllocator allocator = SegmentAllocator.arenaAllocator(ResourceScope.newSharedScope());
        seg = allocator.allocate(point);
        var VHx = point.varHandle(float.class, MemoryLayout.PathElement.groupElement("x"));
        var VHy = point.varHandle(float.class, MemoryLayout.PathElement.groupElement("y"));
        VHx.set(seg, 32.2f);
        VHy.set(seg, 23.2f);
        var entities = 1;
        var st = System.nanoTime();
        for (int i = 0; i < entities; i++) {
            seg = (MemorySegment) ccreate.invoke(allocator, seg.address());
        }
        var en = System.nanoTime();

        System.out.println("[INFO] Time taken: " + (en - st) * 0.000001f + "ms");
    }
}
