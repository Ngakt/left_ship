package com.example.huoda.myapplication.messages;

import android.content.Context;
import android.view.View;

import com.example.huoda.myapplication.commons.ImageLoader;
import com.example.huoda.myapplication.commons.ViewHolder;
import com.example.huoda.myapplication.commons.models.IMessage;
import com.example.huoda.myapplication.messages.MsgListAdapter;

public abstract class BaseMessageViewHolder<MESSAGE extends IMessage>
        extends ViewHolder<MESSAGE> {

    protected Context mContext;

    protected float mDensity;
    protected int mPosition;
    protected boolean mIsSelected;
    protected ImageLoader mImageLoader;

    protected com.example.huoda.myapplication.messages.MsgListAdapter.OnMsgLongClickListener<MESSAGE> mMsgLongClickListener;
    protected com.example.huoda.myapplication.messages.MsgListAdapter.OnMsgClickListener<MESSAGE> mMsgClickListener;
    protected MsgListAdapter.OnAvatarClickListener<MESSAGE> mAvatarClickListener;

    public BaseMessageViewHolder(View itemView) {
        super(itemView);
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}