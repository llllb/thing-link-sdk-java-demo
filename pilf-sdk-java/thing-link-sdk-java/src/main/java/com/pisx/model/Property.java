package com.pisx.model;

/**
 * @Author:boliu
 * @Date:Created in 10:51 AM 2020/10/15
 * @Description:属性数据
 */
public class Property {

    /**
     * 第一列的列名
     */
    private String id ;

    /**
     * 第一列对应的value
     */
    private Object v ;

    /**
     * q boolean
     */
    private Boolean q ;

    /**
     * 时间戳
     */
    private Long t ;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getV() {
        return v;
    }

    public void setV(Object v) {
        this.v = v;
    }

    public Boolean getQ() {
        return q;
    }

    public void setQ(Boolean q) {
        this.q = q;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public Property(String id, Object v, Boolean q, Long t) {
        this.id = id;
        this.v = v;
        this.q = q;
        this.t = t;
    }
}
