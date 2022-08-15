package com.bric.colorpicker.listeners;

public abstract class SecuredListenerWrapper<T> {

    private T listener;

    private boolean changing;

    public void valueChanged() {
        if (!changing) {
            doValueChanged();
        }
        changing = false;
    }

    protected abstract void doValueChanged();

    public void aboutToChangeValue() {
        changing = true;
    }

    public T getListener() {
        return listener;
    }

    public void setListener(T listener) {
        this.listener = listener;
    }
}
