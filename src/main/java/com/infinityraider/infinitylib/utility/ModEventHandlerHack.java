package com.infinityraider.infinitylib.utility;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import com.infinityraider.infinitylib.InfinityMod;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


//GO AWAY!!! THERE IS NOTHING TO SEE HERE, ESPECIALLY IF YOU ARE CPW


public class ModEventHandlerHack {
    private static final List<Method> METHODS = getMethods();
    private static final Field FIELD = getField();

    public static void doHack(InfinityMod instance) {
        Loader.instance().getModList().stream()
                .filter(modContainer -> modContainer instanceof FMLModContainer)
                .filter(modContainer -> modContainer.getModId().equals(instance.getModId()))
                .forEach(modContainer -> addModEventHandlerMethods((FMLModContainer) modContainer));
    }

    @SuppressWarnings("unchecked")
    private static void addModEventHandlerMethods(FMLModContainer modContainer) {
        ListMultimap<Class<? extends FMLEvent>, Method> methodMap = getModEventMap(modContainer);
        for(Method method : METHODS) {
            Class<? extends FMLEvent> paramClass = (Class<? extends FMLEvent>) method.getParameterTypes()[0];
            methodMap.put(paramClass, method);
        }
    }

    @SuppressWarnings("unchecked")
    private static ListMultimap<Class<? extends FMLEvent>, Method> getModEventMap(FMLModContainer modContainer) {
        try {
             return (ListMultimap<Class<? extends FMLEvent>, Method>) FIELD.get(modContainer);
        } catch(Exception e) {
            e.printStackTrace();
            //Should not ever happen
            throw new RuntimeException("CAUGHT ERROR WHEN TRYING TO RETRIEVE FMLMODCONTAINER EVENT METHODS MAP");
        }
    }

    private static List<Method> getMethods() {
        List<Method> foundMethods = new ArrayList<>();
        for(Method method : InfinityMod.class.getDeclaredMethods()) {
            for(Annotation a : method.getAnnotations()) {
                if(a.annotationType().equals(Mod.EventHandler.class)) {
                    if(method.getParameterTypes().length == 1 && FMLEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        method.setAccessible(true);
                        foundMethods.add(method);
                    }
                }
            }
        }
        return ImmutableList.copyOf(foundMethods);
    }

    private static Field getField() {
        Field[] fields = FMLModContainer.class.getDeclaredFields();
        for(Field field : fields) {
            if(field.getType() == ListMultimap.class) {
                field.setAccessible(true);
                return field;
            }
        }
        //Should not ever happen
        throw new RuntimeException("COULD NOT RETRIEVE FMLMODCONTAINER EVENT METHODS FIELD");
    }
}
