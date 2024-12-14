package net.chikorita_lover.chicory.api;

import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NoteBlockSoundRegistry {
    public static final NoteBlockSoundRegistry INSTANCE = new NoteBlockSoundRegistry();
    private final Map<Block, RegistryEntry<SoundEvent>> registeredEntriesBlock = new HashMap<>();
    private final Map<TagKey<Block>, RegistryEntry<SoundEvent>> registeredEntriesTag = new HashMap<>();
    private volatile Map<Block, RegistryEntry<SoundEvent>> computedEntries = null;

    private NoteBlockSoundRegistry() {
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

    public Optional<RegistryEntry<SoundEvent>> getOptional(Block block) {
        return Optional.ofNullable(this.getEntries().get(block));
    }

    public void add(Block block, SoundEvent sound) {
        this.add(block, Registries.SOUND_EVENT.getEntry(sound));
    }

    public void add(Block block, RegistryEntry<SoundEvent> sound) {
        this.registeredEntriesBlock.put(block, sound);
        this.computedEntries = null;
    }

    public void addTag(TagKey<Block> tag, SoundEvent sound) {
        this.addTag(tag, Registries.SOUND_EVENT.getEntry(sound));
    }

    public void addTag(TagKey<Block> tag, RegistryEntry<SoundEvent> sound) {
        this.registeredEntriesTag.put(tag, sound);
        this.computedEntries = null;
    }
}
