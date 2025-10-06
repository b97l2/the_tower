package mia.the_tower.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import mia.the_tower.initialisation.screen.CofferScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ColouredCofferScreen extends HandledScreen<CofferScreenHandler> {
    // vanilla chest background works for 9x3 if we size regions appropriately
    private static final Identifier BG = Identifier.of("minecraft", "textures/gui/container/generic_54.png");

    // derived sizes for 9x3
    private static final int TOP_HEIGHT = 17 + CofferScreenHandler.ROWS_VISIBLE * 18; // 71 for 3 rows
    private static final int BOTTOM_HEIGHT = 96; // player inv panel

    private float scroll = 0.0f; // 0..1

    public ColouredCofferScreen(CofferScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth  = 176;
        this.backgroundHeight = TOP_HEIGHT + BOTTOM_HEIGHT; // 71 + 96 = 167 (close to vanilla 166)
        this.playerInventoryTitleY = this.backgroundHeight - 94; // vanilla convention
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        int x = (this.width  - this.backgroundWidth)  / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // top panel (rows x 18 + 17)
        ctx.drawTexture(RenderLayer::getGuiTextured, BG, x, y,
                0.0f, 0.0f, this.backgroundWidth, TOP_HEIGHT, 256, 256);

        // bottom panel (player inventory)
        ctx.drawTexture(RenderLayer::getGuiTextured, BG, x, y + TOP_HEIGHT,
                0.0f, 126.0f, this.backgroundWidth, BOTTOM_HEIGHT, 256, 256);

        // simple scrollbar on the right edge
        if (hasScrollbar()) {
            int trackX = x + 174;
            int trackY = y + 18;
            int trackH = CofferScreenHandler.ROWS_VISIBLE * 18 - 17; // 37 for 3 rows
            int thumbY = trackY + (int)(scroll * Math.max(trackH, 0));
            // track
            ctx.fill(trackX, trackY, trackX + 2, trackY + CofferScreenHandler.ROWS_VISIBLE * 18, 0xFF000000);
            // thumb
            ctx.fill(trackX - 1, thumbY, trackX + 3, thumbY + 17, 0xFFAAAAAA);
        }
    }

    private boolean hasScrollbar() {
        return handler.getBaseRow() > 0;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
        if (!hasScrollbar()) return false;
        int max = handler.getBaseRow();
        int base = handler.getBaseRow();
        int next = MathHelper.clamp(base - (int)Math.signum(vertical), 0, max);
        if (next != base) {
            this.client.interactionManager.clickButton(this.handler.syncId, next);
            this.scroll = max == 0 ? 0f : (float) next / (float) max;
        }
        return true;
    }

    @Override
    protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) {
        ctx.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 0x404040, false);
        ctx.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 0x404040, false);
    }
}
