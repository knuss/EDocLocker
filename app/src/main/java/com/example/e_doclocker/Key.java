package com.example.e_doclocker;

public class Key {
    String nic;
    byte[] key;
    String ts;

    public Key(String nic, byte[] key, String ts) {
        this.nic = nic;
        this.key = key;
        this.ts = ts;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
