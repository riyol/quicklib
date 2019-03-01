package com.riyol.databinding.recyclerview.adatper;

public interface ItemType {
    default int getItemType() {
        return 0;
    }
}
