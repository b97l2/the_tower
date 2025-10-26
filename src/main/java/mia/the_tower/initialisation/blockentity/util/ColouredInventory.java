package mia.the_tower.initialisation.blockentity.util;

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.DyeColor;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.*;

public class ColouredInventory implements Inventory{
    private final ServerWorld world;
    private final DyeColor color;
    private final State.Entry entry;

    private ColouredInventory(ServerWorld world, DyeColor color, State.Entry entry) {
        this.world = world;
        this.color = color;
        this.entry = entry;
    }

    public static ColouredInventory get(ServerWorld world, DyeColor color) {
        State state = State.get(world.getServer());
        State.Entry entry = state.getOrCreate(color);
        return new ColouredInventory(world, color, entry);
    }

    public void onContainerAdded() {
        entry.containers = Math.max(0, entry.containers) + 1;

        int oldSize = entry.items.size();
        int newSize = entry.containers * 27;

        if (newSize > oldSize) {
            DefaultedList<ItemStack> resized = DefaultedList.ofSize(newSize, ItemStack.EMPTY);
            for (int i = 0; i < oldSize; i++) {
                resized.set(i, entry.items.get(i));
            }
            entry.items = resized; // replace; do NOT add()
        }
        State.get(world.getServer()).markDirty();
        enforceSizeInvariant();
    }

    public List<ItemStack> onContainerRemoved() {
        List<ItemStack> overflow = new ArrayList<>();
        if (entry.containers <= 0) return overflow;

        int oldSize = entry.items.size();
        entry.containers -= 1;
        int newSize = Math.max(0, entry.containers * 27);

        // collect overflow from truncated tail
        for (int i = newSize; i < oldSize; i++) {
            ItemStack s = entry.items.get(i);
            if (!s.isEmpty()) overflow.add(s);
        }

        // rebuild to new size; do NOT remove()
        if (newSize != oldSize) {
            DefaultedList<ItemStack> resized = DefaultedList.ofSize(newSize, ItemStack.EMPTY);
            for (int i = 0; i < Math.min(newSize, oldSize); i++) {
                resized.set(i, entry.items.get(i));
            }
            entry.items = resized;
        }

        State.get(world.getServer()).markDirty();
        enforceSizeInvariant();
        return overflow;
    }

    // --- Inventory impl ---

    @Override
    public int size() {
        return entry.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : entry.items) if (!s.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return (slot >= 0 && slot < entry.items.size()) ? entry.items.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = Inventories.splitStack(entry.items, slot, amount);
        markDirty();
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = Inventories.removeStack(entry.items, slot);
        markDirty();
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot < 0 || slot >= entry.items.size()) {
            // Out of bounds: refuse rather than silently changing capacity.
            return;
        }
        entry.items.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > stack.getMaxCount()) {
            stack.setCount(stack.getMaxCount());
        }
        markDirty();
    }

    @Override
    public void markDirty() {
        State.get(world.getServer()).markDirty();
    }

    @Override
    public boolean canPlayerUse(net.minecraft.entity.player.PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        Collections.fill(entry.items, ItemStack.EMPTY);
        markDirty();
    }

    //this is to make sure the inventory size is consistent
    private void enforceSizeInvariant() {
        int expected = Math.max(0, entry.containers * 27);
        if (entry.items.size() != expected) {
            DefaultedList<ItemStack> fixed = DefaultedList.ofSize(expected, ItemStack.EMPTY);
            for (int i = 0; i < Math.min(expected, entry.items.size()); i++) fixed.set(i, entry.items.get(i));
            entry.items = fixed;
        }
    }

    // --- Persistent, per-color global state ---

    public static final class State extends PersistentState {
        private static final String KEY = "color_coffer_state";

        private final EnumMap<DyeColor, Entry> byColor = new EnumMap<>(DyeColor.class);

        // NOTE: Type constructor here expects the registry-aware reader. Writer can be null;
        // the manager will use State#writeNbt(nbt, lookup).
        public static final PersistentState.Type<State> TYPE = new PersistentState.Type<>(
                State::new,
                State::fromNbt,
                null
        );

        private State() {}

        //possibly buggy, unexpectedely removes pages from inventory
//        public static State fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
//            State s = new State();
//            for (DyeColor c : DyeColor.values()) {
//                String name = c.getName();
//                if (!nbt.contains(name, NbtElement.COMPOUND_TYPE)) continue;
//                NbtCompound cTag = nbt.getCompound(name);
//
//                Entry e = new Entry();
//                e.containers = cTag.getInt("containers");
//
//                int expectedSize = Math.max(0, e.containers * 27);
//
//                // Load whatever was saved under the standard "Items" list
//                NbtList itemsList = cTag.getList("Items", NbtElement.COMPOUND_TYPE);
//                DefaultedList<ItemStack> temp = DefaultedList.ofSize(itemsList.size(), ItemStack.EMPTY);
//                Inventories.readNbt(cTag, temp, lookup);
//
//                // Reconcile to expected size (containers * 27)
//                e.items = DefaultedList.ofSize(expectedSize, ItemStack.EMPTY);
//                for (int i = 0; i < Math.min(e.items.size(), temp.size()); i++) {
//                    e.items.set(i, temp.get(i));
//                }
//
//                s.byColor.put(c, e);
//            }
//            return s;
//        }

        public static State fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
            State s = new State();
            for (DyeColor c : DyeColor.values()) {
                String name = c.getName();
                if (!nbt.contains(name, NbtElement.COMPOUND_TYPE)) continue;
                NbtCompound cTag = nbt.getCompound(name);

                Entry e = new Entry();
                e.containers = cTag.getInt("containers");

                int expectedSize = Math.max(0, e.containers * 27);

                // Allocate to full capacity so readNbt can place by "Slot" index.
                DefaultedList<ItemStack> temp = DefaultedList.ofSize(expectedSize, ItemStack.EMPTY);
                Inventories.readNbt(cTag, temp, lookup);

                // No extra reconcile/copy step needed; temp already matches capacity.
                e.items = temp;

                s.byColor.put(c, e);
            }
            return s;
        }


        @Override
        public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
            for (Map.Entry<DyeColor, Entry> pair : byColor.entrySet()) {
                DyeColor c = pair.getKey();
                Entry e = pair.getValue();

                NbtCompound cTag = new NbtCompound();
                cTag.putInt("containers", Math.max(0, e.containers));

                // Write under standard "Items" using the registry-aware helper
                Inventories.writeNbt(cTag, e.items, lookup);

                nbt.put(c.getName(), cTag);
            }
            return nbt;
        }

        public Entry getOrCreate(DyeColor color) {
            return byColor.computeIfAbsent(color, c -> new Entry());
        }

        public static State get(MinecraftServer server) {
            ServerWorld overworld = server.getOverworld();
            PersistentStateManager mgr = overworld.getPersistentStateManager();
            return mgr.getOrCreate(TYPE, KEY);
        }

        static final class Entry {
            int containers = 0;
            DefaultedList<ItemStack> items = DefaultedList.ofSize(0, ItemStack.EMPTY);
        }
    }

}
