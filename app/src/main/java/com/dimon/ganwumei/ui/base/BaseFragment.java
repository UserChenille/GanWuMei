/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.dimon.ganwumei.ui.base;


import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.dimon.ganwumei.injector.HasComponent;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public abstract  class BaseFragment extends Fragment {

    protected boolean isVisible;

    protected boolean isFirst=true;

    private CompositeSubscription mCompositeSubscription;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible(){
        if(isFirst){
            lazyLoad();
            isFirst=false;
        }

    }

    protected abstract void lazyLoad();

    protected void onInvisible(){}
    /**
     * show Toast Message
     * @param message
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }
}
