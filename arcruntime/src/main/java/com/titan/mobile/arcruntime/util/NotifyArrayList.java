package com.titan.mobile.arcruntime.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zy on 2019/5/22.
 */

public class NotifyArrayList<E> extends ArrayList<E> {

    IListener iListener;

    public NotifyArrayList() {
        super();
    }

    public NotifyArrayList(IListener iListener) {
        super();
        this.iListener = iListener;
    }

    public void setListener(IListener iListener) {
        this.iListener = iListener;
    }

    @Override
    public boolean add(E e) {
        boolean flag = super.add(e);
        if (iListener != null) iListener.itemChange("add");
        return flag;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean flag = super.addAll(index, c);
        if (iListener != null) iListener.itemChange("addAll");
        return flag;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean flag = super.addAll(c);
        if (iListener != null) iListener.itemChange("addAll");
        return flag;
    }

    @Override
    public void clear() {
        super.clear();
        if (iListener != null) iListener.itemChange("clear");
    }

    public void removeAll() {
        super.clear();
        if (iListener != null) iListener.itemChange("removeAll");
    }

    @Override
    public E remove(int index) {
        E flag = super.remove(index);
        if (iListener != null) iListener.itemChange("remove");
        return flag;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = super.removeAll(c);
        if (iListener != null) iListener.itemChange("removeAll");
        return flag;
    }

    public E removeLastItem() {
        int index = size();
        index = index == 0 ? 0 : index - 1;
        return remove(index);
    }

    public E getLastItem() {
        int index = size();
        if (index == 0) return null;
        index = index - 1;
        return get(index);
    }

    public E getFirstItem() {
        return get(0);
    }

    public interface IListener {
        public void itemChange(String flag);
    }
}
