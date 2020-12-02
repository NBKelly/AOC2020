package com.nbkelly.helper;
public interface DebugLogger {
    public void print(int output);
    public void print(Object output);
    public void printf(String line, Object... output);
    public void println(int output);
    public void println(Object output);
    public void println();
    public void ERR();
    public void ERR(Object output);
    public void ERR(int output);
    public void DEBUG();
    public void TEBUG(Object output);
    public void DEBUG(Object output);
    public void TEBUGF(String line, Object... output);
    public void DEBUGF(String line, Object... output);
    public void TEBUG(int output);
    public void DEBUG(int output);
}
