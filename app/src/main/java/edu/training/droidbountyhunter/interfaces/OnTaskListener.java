package edu.training.droidbountyhunter.interfaces;

public interface OnTaskListener {
    void OnTaskCompleted(String response);
    void OnTaskError(int errorCode, String message);
}
