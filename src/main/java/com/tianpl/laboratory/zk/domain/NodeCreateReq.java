package com.tianpl.laboratory.zk.domain;

/**
 * Created by tianyu on 16/12/1.
 */
public class NodeCreateReq {
    private String path;
    private byte[] data;
    private boolean isPersistent = false;
    private boolean isSequential = false;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isPersistent() {
        return isPersistent;
    }

    public void setPersistent(boolean persistent) {
        isPersistent = persistent;
    }

    public boolean isSequential() {
        return isSequential;
    }

    public void setSequential(boolean sequential) {
        isSequential = sequential;
    }
}
