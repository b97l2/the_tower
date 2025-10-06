package mia.the_tower.initialisation.screen;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;

public class CofferScreenHandler  extends ScreenHandler {
    public static final int COLS = 9;
    public static final int ROWS_VISIBLE = 3;              // 27 visible slots
    public static final int WINDOW_SLOTS = COLS * ROWS_VISIBLE;

    private final ScreenHandlerContext context;
    private final Block sourceBlock;

    // Server only (null client-side)
    private final Inventory backing;
    private final WindowedInventory window;

    // Properties synced to client: [0] totalRows, [1] baseRow
    private final PropertyDelegate props = new ArrayPropertyDelegate(2);

    private int baseRow = 0;

    /* ---------- client constructor (registered factory) ---------- */
    public CofferScreenHandler(int syncId, PlayerInventory playerInv) {
        super(screen_init.COFFER, syncId);
        this.context    = ScreenHandlerContext.EMPTY;
        this.sourceBlock= null;
        this.backing    = null;
        this.window     = new WindowedInventory(new SimpleInventory(WINDOW_SLOTS), 0, WINDOW_SLOTS);

        this.addProperties(props); // values will come from server
        layoutSlots(playerInv);
    }

    /* ---------- server constructor (from BE factory) ---------- */
    public CofferScreenHandler(int syncId, PlayerInventory playerInv, Inventory backing,
                               ScreenHandlerContext ctx, Block srcBlock) {
        super(screen_init.COFFER, syncId);
        this.context    = ctx;
        this.sourceBlock= srcBlock;
        this.backing    = backing;
        this.window     = new WindowedInventory(backing, 0, WINDOW_SLOTS);

        // initial sync
        props.set(0, totalRows());
        props.set(1, baseRow);
        this.addProperties(props);

        layoutSlots(playerInv);
        backing.onOpen(playerInv.player);
        clampAndApplyBaseRow();
    }

    /* ---------- layout ---------- */
    private void layoutSlots(PlayerInventory playerInv) {
        // Window: 9x3 at (8,18)
        int left = 8, top = 18;
        for (int r = 0; r < ROWS_VISIBLE; r++) {
            for (int c = 0; c < COLS; c++) {
                int windowIndex = c + r * COLS;
                this.addSlot(new CofferSlot(this, window, windowIndex,
                        left + c * 18, top + r * 18));
            }
        }
        // Player inv at (8,84) and hotbar at (8,142) like vanilla 9x3 chest
        addPlayerInventory(playerInv, 8, 84);
    }

    private void addPlayerInventory(PlayerInventory inv, int left, int top) {
        // 3 rows
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 9; c++) {
                this.addSlot(new Slot(inv, c + (r + 1) * 9, left + c * 18, top + r * 18));
            }
        }
        // hotbar
        for (int c = 0; c < 9; c++) {
            this.addSlot(new Slot(inv, c, left + c * 18, top + 58));
        }
    }

    /* ---------- windowing + scrolling ---------- */
    private int totalRows() {
        if (backing == null) return ROWS_VISIBLE; // client will use synced prop
        int rows = Math.max(0, (int)Math.ceil(backing.size() / (double)COLS));
        return Math.max(rows, ROWS_VISIBLE);
    }

    private void clampAndApplyBaseRow() {
        int maxBase = Math.max(0, getSyncedTotalRows() - ROWS_VISIBLE);
        baseRow = MathHelper.clamp(baseRow, 0, maxBase);
        window.setBaseIndex(baseRow * COLS);
        props.set(0, getSyncedTotalRows()); // keep total rows fresh for client
        props.set(1, baseRow);
        this.sendContentUpdates();
    }

    /** Called from client (HandledScreen) via clickButton(syncId, newBaseRow). */
    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        this.baseRow = id;        // id is target baseRow
        clampAndApplyBaseRow();
        return true;
    }

    /* ---------- access for the client screen ---------- */
    public int getBaseRow() {
        // server uses field; client uses prop
        return this.backing != null ? this.baseRow : props.get(1);
    }

    public int getSyncedTotalRows() {
        // server: compute live; client: from prop
        return this.backing != null ? totalRows() : Math.max(props.get(0), ROWS_VISIBLE);
    }

    public int getMaxBaseRowSynced() {
        return Math.max(0, getSyncedTotalRows() - ROWS_VISIBLE);
    }

    /* ---------- required overrides ---------- */
    @Override
    public boolean canUse(PlayerEntity player) {
        if (sourceBlock == null) return true;
        return canUse(context, player, sourceBlock);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack stack = slot.getStack();
            original = stack.copy();

            int cofferStart = 0;
            int cofferEnd   = WINDOW_SLOTS; // [0,27)
            int invStart    = WINDOW_SLOTS;
            int invEnd      = invStart + 27;
            int hotStart    = invEnd;
            int hotEnd      = hotStart + 9;

            if (index < cofferEnd) {
                if (!this.insertItem(stack, invStart, hotEnd, true)) return ItemStack.EMPTY;
            } else {
                // Only insert into enabled coffer slots
                if (!this.insertItem(stack, cofferStart, cofferEnd, false)) return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
        }
        return original;
    }

    /* ---------- inner: 27-slot window proxy ---------- */
    private static final class WindowedInventory implements Inventory {
        private final Inventory backing;
        private int baseIndex;    // changes on scroll
        private final int size;   // fixed 27

        WindowedInventory(Inventory backing, int baseIndex, int size) {
            this.backing = backing;
            this.baseIndex = Math.max(0, baseIndex);
            this.size = size;
        }

        void setBaseIndex(int base) { this.baseIndex = Math.max(0, base); }
        private int map(int i) { return baseIndex + i; }

        @Override public int size() { return size; }
        @Override public boolean isEmpty() {
            for (int i = 0; i < size; i++) if (!getStack(i).isEmpty()) return false;
            return true;
        }
        @Override public ItemStack getStack(int slot) {
            int idx = map(slot);
            return (idx >= 0 && idx < backing.size()) ? backing.getStack(idx) : ItemStack.EMPTY;
        }
        @Override public ItemStack removeStack(int slot, int amount) {
            int idx = map(slot);
            return (idx >= 0 && idx < backing.size()) ? backing.removeStack(idx, amount) : ItemStack.EMPTY;
        }
        @Override public ItemStack removeStack(int slot) {
            int idx = map(slot);
            return (idx >= 0 && idx < backing.size()) ? backing.removeStack(idx) : ItemStack.EMPTY;
        }
        @Override public void setStack(int slot, ItemStack stack) {
            int idx = map(slot);
            if (idx >= 0 && idx < backing.size()) backing.setStack(idx, stack);
        }
        @Override public void markDirty() { backing.markDirty(); }
        @Override public boolean canPlayerUse(PlayerEntity player) { return backing.canPlayerUse(player); }
        @Override public void clear() {
            for (int i = 0; i < size; i++) setStack(i, ItemStack.EMPTY);
        }
    }

    /* ---------- inner: slot that disables when mapped index is out of range ---------- */
    private static final class CofferSlot extends Slot {
        private final CofferScreenHandler handler;

        CofferSlot(CofferScreenHandler handler, Inventory window, int index, int x, int y) {
            super(window, index, x, y);
            this.handler = handler;
        }

        private boolean mapsToValidIndex() {
            int mapped = handler.getBaseRow() * COLS + this.getIndex(); // global index in backing
            return mapped < handler.getSyncedTotalRows() * COLS;
        }

        @Override public boolean isEnabled()   { return mapsToValidIndex(); }
        @Override public boolean canInsert(ItemStack stack) { return mapsToValidIndex() && super.canInsert(stack); }
    }
}
