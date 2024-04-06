package com.kunminx.linkage.bean;

public class CustomGroupedItem<T> extends BaseGroupedItem<CustomGroupedItem.ItemInfo<T>> {

    public CustomGroupedItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public CustomGroupedItem(ItemInfo<T> item) {
        super(item);
    }

    public static class ItemInfo<T> extends BaseGroupedItem.ItemInfo {
        private T t;

        public ItemInfo(String title, String group, String route, T t) {
            super(title, group, route);
            this.t = t;
        }

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }
    }
}
