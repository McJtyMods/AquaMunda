package mcjty.aquamunda.multiblock;

public interface IMultiBlockFactory<T extends IMultiBlock> {

    T create();

    boolean isSameType(IMultiBlock mb);
}
