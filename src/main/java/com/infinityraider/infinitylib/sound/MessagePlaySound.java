package com.infinityraider.infinitylib.sound;

import com.google.common.collect.ImmutableList;
import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.infinitylib.network.serialization.ByteBufUtil;
import com.infinityraider.infinitylib.network.serialization.IMessageReader;
import com.infinityraider.infinitylib.network.serialization.IMessageSerializer;
import com.infinityraider.infinitylib.network.serialization.IMessageWriter;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MessagePlaySound extends MessageBase<IMessage> {
    private Type type;
    private Entity entity;
    private Vec3d position;
    private String uuid;
    private SoundEvent sound;
    private SoundCategory category;
    private float volume;
    private float pitch;
    private boolean repeat;
    private int repeatDelay;

    public MessagePlaySound() {
        super();
    }

    public MessagePlaySound(Entity entity, SoundTaskServer task) {
        this();
        this.type = Type.ENTITY;
        this.entity = entity;
        this.uuid = task.getUUID();
        this.sound = task.getSound();
        this.category = task.getCategory();
        this.volume = task.getVolume();
        this.pitch = task.getPitch();
        this.repeat = task.repeat();
        this.repeatDelay = task.repeatDelay();
    }

    public MessagePlaySound(Vec3d position, SoundTaskServer task) {
        this();
        this.type = Type.POSITION;
        this.position = position;
        this.uuid = task.getUUID();
        this.sound = task.getSound();
        this.category = task.getCategory();
        this.volume = task.getVolume();
        this.pitch = task.getPitch();
        this.repeat = task.repeat();
        this.repeatDelay = task.repeatDelay();
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.checkData() && this.sound != null && this.category != null && ctx.side == Side.CLIENT) {
            ModSoundHandler.getInstance().onSoundMessage(this);
        }
    }

    protected boolean checkData() {
        if(this.type != null) {
            switch (this.type) {
                case ENTITY:
                    return this.entity != null;
                case POSITION:
                    return this.position != null;
            }
        }
        return false;
    }

    public Type getType() {
        return this.type;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Vec3d getPosition() {
        return this.position;
    }

    @SideOnly(Side.CLIENT)
    public SoundTaskClient getModSound() {
        return new SoundTaskClient(this.uuid, this.sound, this.category, this.volume, this.pitch)
                .setVolume(this.volume)
                .setPitch(this.pitch)
                .setRepeat(this.repeat)
                .setRepeatDelay(this.repeatDelay);
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    protected List<IMessageSerializer> getNecessarySerializers() {
        return ImmutableList.of(
                new IMessageSerializer<SoundEvent>() {
                    @Override
                    public boolean accepts(Class<SoundEvent> clazz) {
                        return clazz.isAssignableFrom(SoundEvent.class);
                    }

                    @Override
                    public IMessageWriter<SoundEvent> getWriter(Class<SoundEvent> clazz) {
                        return (buf, data) -> ByteBufUtil.writeString(buf, data.getSoundName().toString());
                    }

                    @Override
                    public IMessageReader<SoundEvent> getReader(Class<SoundEvent> clazz) {
                        return (data) -> (SoundEvent.REGISTRY.getObject(new ResourceLocation(ByteBufUtil.readString(data))));
                    }
                });
    }

    public enum Type {
        ENTITY,
        POSITION
    }
}