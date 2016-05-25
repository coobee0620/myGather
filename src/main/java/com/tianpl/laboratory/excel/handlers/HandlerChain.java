package com.tianpl.laboratory.excel.handlers;

import com.tianpl.laboratory.excel.ExcelExportedAble;
import org.apache.poi.hssf.usermodel.HSSFCell;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tianyu on 15/12/31.
 * @desc 单元格类型处理链.（责任链模式,外部化链）
 *          按照注册顺序遍历处理器（Handler）.
 *          遇到链上第一个返回true的处理器结束遍历,将单元格的填充控制权交给此处理器完成.
 */
public class HandlerChain {
    private static HandlerChain instance = new HandlerChain();

    private List<CellTypeHandler> chain = new ArrayList<>();;

    public static HandlerChain getInstance() {
        return instance;
    }

    private HandlerChain() {
    }

    /**
     * DefaultHandler将始终返回true.保证其始终保持在链的末端
     */
    {
        this.register(new DefaultHandler())
                .register(new DateHandler());
        sort();
    }

    private HandlerChain register(CellTypeHandler handler) {
        if (handler != null) {
            chain.add(handler);
        }
        return this;
    }

    /**
     * 外部注册处理器
     * @param handler cell处理器
     */
    public void registerHandler(CellTypeHandler handler) {
        if (handler != null) {
            chain.add(handler);
            sort();
        }
    }

    /**
     * 降序
     */
    private void sort() {
        Collections.sort(this.chain, (o1, o2) -> o1.getOrder().compareTo(o2.getOrder()));
    }

    public <T extends ExcelExportedAble> void handle(Field field, T obj, HSSFCell cell) {
        for (CellTypeHandler handler : chain) {
            if (handler.handle(field,obj,cell)) {
                break;
            }
        }
    }
}
