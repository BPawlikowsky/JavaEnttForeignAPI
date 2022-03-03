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

    private final MethodHandle init, createPos, createBulk, update, printAll, getPositions, getCapasity;

    public Entt() {
        SymbolLookup lookup = SymbolLookup.loaderLookup();
        MemoryAddress address = lookup.lookup("init").get();
        init = CLinker.getInstance().downcallHandle(
                address,
                MethodType.methodType(void.class),
                FunctionDescriptor.ofVoid()
        );

        address = lookup.lookup("registerPosition").get();
        createPos = CLinker.getInstance().downcallHandle(
                address,
                MethodType.methodType(void.class, MemoryAddress.class),
                FunctionDescriptor.ofVoid(C_POINTER)
        );

        address = lookup.lookup("registerPositions").get();
        createBulk = CLinker.getInstance().downcallHandle(
                address,
                MethodType.methodType(void.class, MemoryAddress.class, int.class),
                FunctionDescriptor.ofVoid(C_POINTER, C_INT)
        );

        address = lookup.lookup("update").get();
        update = CLinker.getInstance().downcallHandle(
                address,
                MethodType.methodType(int.class),
                FunctionDescriptor.of(C_INT)
        );

        address = lookup.lookup("printAllEntities").get();
        printAll = CLinker.getInstance().downcallHandle(
                address,
                MethodType.methodType(void.class),
                FunctionDescriptor.ofVoid()
        );

        address = lookup.lookup("getPositions").get();
        getPositions = CLinker.getInstance().downcallHandle(
                address,
                MethodType.methodType(MemoryAddress.class),
                FunctionDescriptor.of(C_POINTER)
        );

        address = lookup.lookup("getCapacity").get();
        getCapasity = CLinker.getInstance().downcallHandle(
                address,
                MethodType.methodType(long.class),
                FunctionDescriptor.of(C_LONG)
        );
    }

    public void init() {
        try {
            init.invoke();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void registerPosition(MemorySegment segment) {
        try {
            createPos.invokeExact(segment.address());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void registerPositions(MemorySegment segment, int length) {
        try {
            createBulk.invokeExact(segment.address(), length);
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

    public void printAll() {
        try {
            var result = printAll.invoke();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public MemoryAddress getPositions(MemoryLayout layout) {
        MemoryAddress result;
        try {
            result = (MemoryAddress) getPositions.invoke();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public long getCapasity() {
        try {
            return (long)getCapasity.invoke();
        } catch (Throwable e) {
            e.printStackTrace();
            return -1;
        }
    }
}
