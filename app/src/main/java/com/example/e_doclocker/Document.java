package com.example.e_doclocker;

public class Document {
    private String docType,file;
    private byte[] key;
    private String user;

    public Document(String docType, String file, byte[] key) {
        this.docType = docType;
        this.file = file;
        this.key = key;
    }

    public Document(String docType, String file, byte[] key, String user) {
        this.docType = docType;
        this.file = file;
        this.key = key;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }
    public String toString(){
        return docType;
    }
}
