package com.example.huoda.myapplication.record;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.example.huoda.myapplication.R;
import com.example.huoda.myapplication.Style;


public class RecordVoiceBtnStyle extends Style {

    private String voiceBtnText;
    private String tapDownText;
    private Drawable voiceBtnBg;
    private Drawable mic_1;
    private Drawable mic_2;
    private Drawable mic_3;
    private Drawable mic_4;
    private Drawable mic_5;
    private Drawable cancelRecord;

    public static cn.jiguang.imui.chatinput.record.RecordVoiceBtnStyle parse(Context context, AttributeSet attrs) {
        cn.jiguang.imui.chatinput.record.RecordVoiceBtnStyle style = new cn.jiguang.imui.chatinput.record.RecordVoiceBtnStyle(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, cn.jiguang.imui.chatinput.R.styleable.RecordVoiceButton);
        style.voiceBtnText = typedArray.getString(cn.jiguang.imui.chatinput.R.styleable.RecordVoiceButton_voiceBtnText);
        style.tapDownText = typedArray.getString(cn.jiguang.imui.chatinput.R.styleable.RecordVoiceButton_tapDownText);
        style.voiceBtnBg = typedArray.getDrawable(cn.jiguang.imui.chatinput.R.styleable.RecordVoiceButton_recordVoiceBtnBg);
        style.mic_1 = typedArray.getDrawable(R.styleable.RecordVoiceButton_mic_1);
        style.mic_2 = typedArray.getDrawable(cn.jiguang.imui.chatinput.R.styleable.RecordVoiceButton_mic_2);
        style.mic_3 = typedArray.getDrawable(cn.jiguang.imui.chatinput.R.styleable.RecordVoiceButton_mic_3);
        style.mic_4 = typedArray.getDrawable(cn.jiguang.imui.chatinput.R.styleable.RecordVoiceButton_mic_4);
        style.mic_5 = typedArray.getDrawable(cn.jiguang.imui.chatinput.R.styleable.RecordVoiceButton_mic_5);
        style.cancelRecord = typedArray.getDrawable(cn.jiguang.imui.chatinput.R.styleable.RecordVoiceButton_cancelRecord);

        typedArray.recycle();
        return style;
    }

    private RecordVoiceBtnStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String getVoiceBtnText() {
        return this.voiceBtnText;
    }

    public String getTapDownText() {
        return this.tapDownText;
    }

    public Drawable getVoiceBtnBg() {
        return this.voiceBtnBg;
    }

    public Drawable getMic_1() {
        return this.mic_1;
    }

    public Drawable getMic_2() {
        return this.mic_2;
    }

    public Drawable getMic_3() {
        return this.mic_3;
    }

    public Drawable getMic_4() {
        return this.mic_4;
    }

    public Drawable getMic_5() {
        return this.mic_5;
    }

    public Drawable getCancelRecord() {
        return this.cancelRecord;
    }
}
