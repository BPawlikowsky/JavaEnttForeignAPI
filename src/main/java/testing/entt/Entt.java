package testing.entt;

import jdk.incubator.foreign.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static jdk.incubator.foreign.CLinker.*;


public class Entt {

    static {
        System.loadLibrary("java_entt_registry");
    }

    public static GroupLayout point = MemoryLayout.structLayout(
            C_FLOAT.withName("x"),
            C_FLOAT.withName("y")
    );

    private final MethodHandle init, create, update;

    public Entt() {
        SymbolLookup lookup = SymbolLookup.loaderLookup();
        MemoryAddress initAddr = lookup.lookup("init").get();
        init = CLinker.getInstance().downcallHandle(
                initAddr,
                MethodType.methodType(void.class),
                FunctionDescriptor.ofVoid()
        );

        MemoryAddress createAddr = lookup.lookup("create").get();
        create = CLinker.getInstance().downcallHandle(
                createAddr,
                MethodType.methodType(void.class, MemoryAddress.class),
                FunctionDescriptor.ofVoid(C_POINTER)
        );

        MemoryAddress updateAddr = lookup.lookup("update").get();
        update = CLinker.getInstance().downcallHandle(
                updateAddr,
                MethodType.methodType(int.class),
                FunctionDescriptor.of(C_INT)
        );
    }

    public void init() {
        try {
            init.invoke();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void create(MemorySegment segment) {
        try {
            create.invokeExact(segment.address());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            var result = update.invoke();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
