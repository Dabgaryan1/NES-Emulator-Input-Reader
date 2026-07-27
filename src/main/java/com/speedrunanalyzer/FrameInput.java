package com.speedrunanalyzer;

//FrameInput class to represent the input state of a NES controller at a specific frame
public class FrameInput {
    //frame number
    private final long frame;

    //fields for nes controller buttons
    private final boolean u;    //up button
    private final boolean d;    //down button
    private final boolean l;    //left button
    private final boolean r;    //right button
    private final boolean a;    //a button
    private final boolean b;    //b button
    private final boolean start;    //start button
    private final boolean select;   //select button

    //constructor
    public FrameInput(long frame,
                      boolean u,
                      boolean d,
                      boolean l,
                      boolean r,
                      boolean a,
                      boolean b,
                      boolean start,
                      boolean select) {
        this.frame = frame;
        this.u = u;
        this.d = d;
        this.l = l;
        this.r = r;
        this.a = a;
        this.b = b;
        this.start = start;
        this.select = select;
    }

    //getters
    public long getFrame() {
        return frame;
    }
    public boolean isU() {
        return u;
    }
    public boolean isD() {
        return d;
    }
    public boolean isL() {
        return l;
    }
    public boolean isR() {
        return r;
    }
    public boolean isA() {
        return a;
    }
    public boolean isB() {
        return b;
    }
    public boolean isStart() {
        return start;
    }
    public boolean isSelect() {
        return select;
    }
}