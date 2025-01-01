package net.chikorita_lover.chicory.api;

import net.chikorita_lover.chicory.impl.NoteBlockSoundRegistryImpl;
import net.fabricmc.fabric.api.util.Block2ObjectMap;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

public interface NoteBlockSoundRegistry extends Block2ObjectMap<RegistryEntry<SoundEvent>> {
    NoteBlockSoundRegistry INSTANCE = new NoteBlockSoundRegistryImpl();
}
