package com.example.yazid12rpl02.iface;

public interface ProfileActionListener {
    void onLogout();
    void onProfileUpdated(String updatedName);
    void onProfileUpdateFailed(String message);
}
