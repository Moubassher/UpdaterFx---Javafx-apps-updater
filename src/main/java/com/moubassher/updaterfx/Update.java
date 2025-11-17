package com.moubassher.updaterfx;

import java.util.List;

public class Update {
    public final boolean available;
    public final List<String> added;
    public final List<String> changed;
    public final List<String> removed;
    private String remoteUrl;
    public Update(boolean available,
                  List<String> added,
                  List<String> changed,
                  List<String> removed) {
        this.available = available;
        this.added = added;
        this.changed = changed;
        this.removed = removed;
    }


}
