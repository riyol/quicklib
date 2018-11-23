package com.riyol.navigate;

public class Navigate {
    private Object data;
    private ActionEnum action;
    private Object subAction;

    private Navigate() {
    }

    public <T> T getData() {
        return (T) data;
    }

    public ActionEnum getAction() {
        return action;
    }

    public <T> void setData(T data) {
        this.data = data;
    }

    public <T> T getSubAction() {
        return (T) subAction;
    }

    public <T> void setSubAction(T subAction) {
        this.subAction = subAction;
    }

    public static Navigate create(ActionEnum action) {
        Navigate navigate = new Navigate();
        navigate.action = action;
        return navigate;
    }

    public static <T> Navigate create(ActionEnum action, T subAction) {
        Navigate navigate = new Navigate();
        navigate.action = action;
        navigate.subAction = subAction;
        return navigate;
    }

    public static <T> Navigate createWithData(ActionEnum action, T data) {
        Navigate navigate = new Navigate();
        navigate.action = action;
        navigate.data = data;
        return navigate;
    }

    public static <T, SubAction> Navigate createWithData(ActionEnum action, SubAction subAction, T data) {
        Navigate navigate = new Navigate();
        navigate.action = action;
        navigate.data = data;
        navigate.subAction = subAction;
        return navigate;
    }
}
