/*
 * Simple PAIR class to emulate the one found in C++.
 * If you know of a better way to do this, let me know.
 */
package com.example.FYP.aardvark_project.Common;

public class mPair<F, S> {
    public F first;
    public S second;

    public mPair (F f, S s) {
        this.first = f;
        this.second = s;
    }
}
