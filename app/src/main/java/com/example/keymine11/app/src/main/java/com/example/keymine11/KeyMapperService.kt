package com.example.keymine11

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

class KeyMapperService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onKeyEvent(event: KeyEvent): Boolean {
        val isEnabled = getSharedPreferences("11keymine_prefs", Context.MODE_PRIVATE)
            .getBoolean("service_enabled", true)

        if (!isEnabled) {
            return super.onKeyEvent(event)
        }

        val targetKeyCode = when (event.keyCode) {
            KeyEvent.KEYCODE_NUMPAD_DIVIDE -> KeyEvent.KEYCODE_F5
            KeyEvent.KEYCODE_NUMPAD_7      -> KeyEvent.KEYCODE_Q
            KeyEvent.KEYCODE_NUMPAD_8      -> KeyEvent.KEYCODE_W
            KeyEvent.KEYCODE_NUMPAD_9      -> KeyEvent.KEYCODE_E
            KeyEvent.KEYCODE_NUMPAD_SUBTRACT-> KeyEvent.KEYCODE_1
            KeyEvent.KEYCODE_NUMPAD_4      -> KeyEvent.KEYCODE_A
            KeyEvent.KEYCODE_NUMPAD_5      -> KeyEvent.KEYCODE_S
            KeyEvent.KEYCODE_NUMPAD_6      -> KeyEvent.KEYCODE_D
            KeyEvent.KEYCODE_NUMPAD_ADD   -> KeyEvent.KEYCODE_9
            KeyEvent.KEYCODE_NUMPAD_DOT   -> KeyEvent.KEYCODE_SHIFT_LEFT
            KeyEvent.KEYCODE_NUMPAD_ENTER -> KeyEvent.KEYCODE_SPACE
            else -> null
        }

        if (targetKeyCode != null) {
            return true
        }

        return super.onKeyEvent(event)
    }
}
