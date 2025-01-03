package net.chikorita_lover.chicory.impl;

import net.chikorita_lover.chicory.api.block.NoteBlockSoundRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public class NoteBlockSoundRegistryImpl implements NoteBlockSoundRegistry {
    private final Map<Block, RegistryEntry<SoundEvent>> registeredEntriesBlock = new HashMap<>();
    private final Map<TagKey<Block>, RegistryEntry<SoundEvent>> registeredEntriesTag = new HashMap<>();
    private volatile Map<Block, RegistryEntry<SoundEvent>> computedEntries = null;

    public NoteBlockSoundRegistryImpl() {
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> this.computedEntries = null);
    }

    private Map<Block, RegistryEntry<SoundEvent>> getEntries() {
        Map<Block, RegistryEntry<SoundEvent>> entries = this.computedEntries;
        if (entries == null) {
            entries = new HashMap<>();
            for (TagKey<Block> tag : this.registeredEntriesTag.keySet()) {
                RegistryEntry<SoundEvent> sound = this.registeredEntriesTag.get(tag);
                for (RegistryEntry<Block> block : Registries.BLOCK.iterateEntries(tag)) {
                    entries.put(block.value(), sound);
                }
            }
            entries.putAll(this.registeredEntriesBlock);
            this.computedEntries = entries;
        }
        return entries;
    }

    @Override
    public RegistryEntry<SoundEvent> get(Block block) {
        return this.getEntries().get(block);
    }

    @Override
    public void add(Block block, RegistryEntry<SoundEvent> sound) {
        this.registeredEntriesBlock.put(block, sound);
        this.computedEntries = null;
    }

    @Override
    public void add(TagKey<Block> tag, RegistryEntry<SoundEvent> sound) {
        this.registeredEntriesTag.put(tag, sound);
        this.computedEntries = null;
    }

    @Override
    public void remove(Block block) {
        this.registeredEntriesBlock.put(block, NoteBlockInstrument.HARP.getSound());
        this.computedEntries = null;
    }

    @Override
    public void remove(TagKey<Block> tag) {
        this.registeredEntriesTag.put(tag, NoteBlockInstrument.HARP.getSound());
        this.computedEntries = null;
    }

    @Override
    public void clear(Block block) {
        this.registeredEntriesBlock.remove(block);
    }

    @Override
    public void clear(TagKey<Block> tag) {
        this.registeredEntriesTag.remove(tag);
    }
}
