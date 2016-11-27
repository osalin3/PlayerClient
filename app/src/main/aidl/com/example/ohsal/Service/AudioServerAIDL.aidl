// AudioServerAIDL.aidl
package com.example.ohsal.Service;

// Declare any non-default types here with import statements

interface AudioServerAIDL {
     String[] getClips();
     boolean playClip(String clip);
     boolean stop();
}

