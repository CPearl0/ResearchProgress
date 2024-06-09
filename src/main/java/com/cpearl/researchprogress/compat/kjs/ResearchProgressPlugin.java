package com.cpearl.researchprogress.compat.kjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class ResearchProgressPlugin extends KubeJSPlugin {
    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("ResearchProgress", new ResearchProgressKubeJSBindings());
    }
}
