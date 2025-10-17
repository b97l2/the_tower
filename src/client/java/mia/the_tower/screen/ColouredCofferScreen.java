package mia.the_tower.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import mia.the_tower.initialisation.screen.CofferScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class
ColouredCofferScreen extends HandledScreen<CofferScreenHandler> {
    // vanilla chest background works for 9x3 if we size regions appropriately
    private static final Identifier BG = Identifier.of("the_tower", "textures/gui/container/coffer_screen.png");
    private static final Identifier TEX_PREV = Identifier.of("the_tower", "textures/gui/container/pagination_button_coloured_coffer_left.png");
    private static final Identifier TEX_NEXT = Identifier.of("the_tower", "textures/gui/container/pagination_button_coloured_coffer_right.png");

    private IconButton prevBtn;
    private IconButton nextBtn;

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
    protected void init() {
        super.init();

        // 12x12 buttons beside the grid; adjust positions as you like
        prevBtn = new IconButton(0, 0, 12, 12, TEX_PREV, () ->
                this.client.interactionManager.clickButton(this.handler.syncId, 0) // page--
        );
        nextBtn = new IconButton(0, 0, 12, 12, TEX_NEXT, () ->
                this.client.interactionManager.clickButton(this.handler.syncId, 1) // page++
        );

        this.addDrawableChild(prevBtn);
        this.addDrawableChild(nextBtn);

        placeButtons();
    }

    @Override
    protected void handledScreenTick() {
        int page  = this.handler.getPage();
        int total = this.handler.getTotalPagesSynced();

        if (prevBtn != null) prevBtn.active = page > 0;
        if (nextBtn != null) nextBtn.active = page < total - 1;
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        ctx.drawTexture(RenderLayer::getGuiTextured, BG, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }


    @Override
    protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) {
        //these are to draw titles, but I don't want this for this block
//        ctx.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 0x404040, false);
//        ctx.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 0x404040, false);

        //this is for page indicator
        String p = (handler.getPage() + 1) + " of " + handler.getTotalPagesSynced();
        int w = this.textRenderer.getWidth(p);
        ctx.drawText(this.textRenderer, p, this.backgroundWidth/2 - w/2, this.backgroundHeight/2 - 10, 0x404040, false);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        //this.renderBackground(ctx);                 // 1) dim the world behind
        super.render(ctx, mouseX, mouseY, delta);   // 2) draw slots/items/etc.
        this.drawMouseoverTooltip(ctx, mouseX, mouseY); // 3) draw item tooltips last
    }

    private static final class IconButton extends PressableWidget {
        private final Identifier texture;
        private final Runnable onClick;

        IconButton(int x, int y, int w, int h, Identifier texture, Runnable onClick) {
            super(x, y, w, h, Text.empty());
            this.texture = texture;
            this.onClick = onClick;
        }

        @Override
        public void onPress() {
            if (onClick != null) onClick.run();
        }

        @Override
        protected void renderWidget(DrawContext ctx, int mouseX, int mouseY, float delta) {
            int state = !this.active ? 2 : (this.isHovered() ? 1 : 0); // 0=normal,1=hover,2=disabled
            float u = 0.0f;
            float v = state * this.height;
            int sheetW = this.width;   // set to full sheet width (or 256 if using an atlas-style sheet)
            int sheetH = this.height * 3;

            ctx.drawTexture(RenderLayer::getGuiTextured, texture, this.getX(), this.getY(),
                    u, v, this.width, this.height, sheetW, sheetH);
        }

        @Override
        protected void appendClickableNarrations(net.minecraft.client.gui.screen.narration.NarrationMessageBuilder b) {
            // Optional narration; leave empty for icon-only
        }
    }

 //this is to keep buttons in the right place despite resolution changes
    private static final int BTN_W = 12, BTN_H = 12;
    private static final int BTN_MARGIN_RIGHT = 47;   // distance from right edge of the container
    private static final int BTN_TOP_OFFSET   = 71;  // distance from container top to row 1
    private static final int BTN_GAP          = 58;   // gap between prev/next

    private void placeButtons() {
        // anchor to the right edge of the container, not magic numbers
        int right = this.x + this.backgroundWidth;          // container right x
        int nextX = right - BTN_MARGIN_RIGHT - BTN_W;       // rightmost button
        int prevX = nextX - BTN_GAP - BTN_W;
        int y     = this.y + BTN_TOP_OFFSET;

        if (prevBtn != null) prevBtn.setPosition(prevX, y);
        if (nextBtn != null) nextBtn.setPosition(nextX, y);
    }

}
