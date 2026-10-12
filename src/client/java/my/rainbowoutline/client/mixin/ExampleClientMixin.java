package my.rainbowoutline.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelRenderer.class)
public class ExampleClientMixin {

    @ModifyArg(
        method = "submitBlockOutline",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;submitHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;IFZ)V"
        ),
        index = 4
    )
    private int rainbowoutline$changeColor(int originalColor) {
        float hue = (System.currentTimeMillis() % 6000L) / 6000.0f;
        int rgb = hsvToRgb(hue, 1.0f, 1.0f);
        int alpha = 230;
        return (alpha << 24) | rgb;
    }

    private static int hsvToRgb(float hue, float saturation, float value) {
        float h = hue * 6.0f;
        int sector = (int) h;
        float f = h - sector;

        float p = value * (1.0f - saturation);
        float q = value * (1.0f - saturation * f);
        float t = value * (1.0f - saturation * (1.0f - f));

        float r, g, b;

        switch (sector % 6) {
            case 0: r = value; g = t; b = p; break;
            case 1: r = q; g = value; b = p; break;
            case 2: r = p; g = value; b = t; break;
            case 3: r = p; g = q; b = value; break;
            case 4: r = t; g = p; b = value; break;
            default: r = value; g = p; b = q; break;
        }

        return (Math.round(r * 255) << 16)
            | (Math.round(g * 255) << 8)
            | Math.round(b * 255);
    }
}
