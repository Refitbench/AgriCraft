package com.infinityraider.infinitylib.network.serialization;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class MessageSerializerSubClass<C extends P, P> implements IMessageSerializer<C> {
    public static final IMessageSerializer<TileEntity> TILE_ENTITY = new MessageSerializerSubClass<>(TileEntity.class);
    public static final IMessageSerializer<Entity> ENTITY = new MessageSerializerSubClass<>(Entity.class);
    public static final IMessageSerializer<Block> BLOCK = new MessageSerializerSubClass<>(Block.class);
    public static final IMessageSerializer<Item> ITEM = new MessageSerializerSubClass<>(Item.class);
    public static final IMessageSerializer<ITextComponent> TEXT = new MessageSerializerSubClass<>(ITextComponent.class);

    private final Class<P> parentClass;

    private IMessageWriter<P> writer;
    private IMessageReader<P> reader;

    private MessageSerializerSubClass(Class<P> parentClass) {
        this.parentClass = parentClass;
    }

    @Override
    public boolean accepts(Class<C> childClass) {
        //this order is really important to prevent infinite loop
        return this.parentClass != childClass
                && this.parentClass.isAssignableFrom(childClass)
                && MessageSerializerStore.getMessageSerializer(this.parentClass).isPresent();
    }

    @Override
    public IMessageWriter<C> getWriter(Class<C> childClass) {
        if(this.writer == null) {
            IMessageSerializer<P> serializer = MessageSerializerStore.getMessageSerializer(this.parentClass).get();
            this.writer = serializer.getWriter(this.parentClass);
        }
        return (buf, data) -> this.writer.writeData(buf, (P) data);
    }

    @Override
    public IMessageReader<C> getReader(Class<C> childClass) {
        if(this.reader == null) {
            IMessageSerializer<P> serializer = MessageSerializerStore.getMessageSerializer(this.parentClass).get();
            this.reader = serializer.getReader(this.parentClass);
        }
        return (buf) -> {
            P obj = this.reader.readData(buf);
            return childClass.isInstance(obj) ? childClass.cast(obj) : null;
        };
    }
}
