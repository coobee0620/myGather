package com.tianpl.demo;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by tianyu on 16/10/10.
 */
//@Component
public class MyJavaBean {
    @Value("测试一下啦")
    private String desc;
    @Value("这是备注信息啦啦啦")
    private String remark;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
