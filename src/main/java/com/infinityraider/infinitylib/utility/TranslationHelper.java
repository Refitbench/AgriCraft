package com.infinityraider.infinitylib.utility;

import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@SuppressWarnings("deprecation")
public class TranslationHelper {
    public static String translateToLocal(String text) {
        return I18n.translateToLocal(text);
    }
}
