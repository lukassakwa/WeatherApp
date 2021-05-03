package com.olivier.weatherapp.presenter;

public abstract class BasePresenter<T> {

    protected T view;

    public void attach(T view){
        this.view = view;
    }

    public void detach(T view){
        this.view = null;
    }

    public boolean isViewAttach(){
        return this.view != null;
    }

}
