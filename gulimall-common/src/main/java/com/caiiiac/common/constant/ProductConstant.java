package com.caiiiac.common.constant;

public class ProductConstant {
    public enum AttrEnum {
        ATTR_TYPE_BASE(1,"基本属性"),
        ATTR_TYPE_SALE(0,"销售属性");

        private int code;
        private String smg;

        AttrEnum(int code, String smg) {
            this.code = code;
            this.smg = smg;
        }

        public int getCode() {
            return code;
        }

        public String getSmg() {
            return smg;
        }
    }
}
