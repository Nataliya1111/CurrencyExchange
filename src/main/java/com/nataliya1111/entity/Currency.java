package com.nataliya1111.entity;

public class Currency {

    private Long id;
    private String code;
    private String name;
    private String sign;

    public Currency(Long id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.name = fullName;
        this.sign = sign;
    }

    public Currency(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "Currencies{" +
               "id=" + id +
               ", code='" + code + '\'' +
               ", fullName='" + name + '\'' +
               ", sign='" + sign + '\'' +
               '}';
    }

}
