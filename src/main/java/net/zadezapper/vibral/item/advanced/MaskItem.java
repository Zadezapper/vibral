package net.zadezapper.vibral.item.advanced;

/* import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.util.Formatting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.world.World;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.message.SentMessage;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import xyz.amymialee.amarite.Amarite; */
import net.minecraft.item.Item;

public class MaskItem extends /* Trinket */ Item {
    public MaskItem(Settings settings) {
        super(settings);
    }
    /*
    public final Identifier maskTexture;
    private final int color;

    public MaskItem(Identifier texture, int color, FabricItemSettings settings) {
        super(settings);
        this.maskTexture = texture;
        this.color = color;
    }

    public static boolean isWearingMask(LivingEntity livingEntity) {
        return getWornMask(livingEntity) != ItemStack.field_8037;
    }

    public static ItemStack getWornMask(LivingEntity livingEntity) {
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(livingEntity);
        if (component.isPresent()) {
            Iterator var2 = ((TrinketComponent)component.get()).getAllEquipped().iterator();

            while(var2.hasNext()) {
                ServerPlayerEntity<SlotReference, ItemStack> pair = (ServerPlayerEntity)var2.next();
                if (((ItemStack)pair.method_15441()).method_7909() instanceof MaskItem) {
                    return (ItemStack)pair.method_15441();
                }
            }
        }

        return ItemStack.field_8037;
    }

    public static int getOffset(ItemStack stack) {
        return stack.method_7969() == null ? 0 : stack.method_7969().method_10550("offset");
    }

    public static void incrementOffset(ItemStack stack) {
        NbtCompound compound = stack.method_7948();
        compound.method_10569("offset", Amarite.clampLoop(compound.method_10550("offset") + 1, -2, 3));
    }

    public SentMessage method_7864(ItemStack stack) {
        return Amarite.withColor(super.method_7864(stack), this.color);
    }

    public void method_7851(ItemStack stack, @Nullable World world, List<SentMessage> tooltip, ItemUsageContext context) {
        tooltip.add(SentMessage.method_43471("item.amarite.mask.desc").method_27692(Formatting.field_1080));
        tooltip.add(SentMessage.method_43471("item.amarite.mask.desc.offset1").method_27692(Formatting.field_1080));
        if (stack.method_7969() != null) {
            tooltip.add(SentMessage.method_43469("item.amarite.mask.desc.offset2", new Object[]{stack.method_7969().method_10550("offset")}).method_27692(Formatting.field_1080));
        }

        super.method_7851(stack, world, tooltip, context);
    }
     */
}