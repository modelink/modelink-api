package com.modelink.reservation.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class AdvertiseAnalyse {

    private Long id;
    /** 保单编号 **/
    private String insuranceNo;
    /** 预约姓名 **/
    private String name;
    /** 投保人性别 **/
    private String gender;
    /** 投保人生日 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;
    /** 投保人年龄 **/
    private Integer age;
    /** 投保人手机号 **/
    private String mobile;
    /** 投保人地址 **/
    private String address;
    /** 投保人省份 **/
    private Integer province;
    /** 投保人城市 **/
    private Integer city;
    /** 预约时间 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date contactTime;
    /** 预约商户（如小米、华夏） **/
    private Long merchantId;
    /** 平台类型（微信、PC、WAP、转介绍） **/
    private Integer platform;
    /** 数据类型（自然流量、SEM） **/
    private Integer dataType;
    /** 渠道入口类型（0-默认） **/
    private Integer sourceType = 0;
    /** 下发时间 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date arrangeTime;
    /** 机构名称 **/
    private String orgName;
    /** 客服名称 **/
    private String tsrName;
    /** 第一天拨打 **/
    private String firstCall;
    /** 第二天拨打 **/
    private String secondCall;
    /** 第三天拨打 **/
    private String threeCall;
    /** 拨打状态 **/
    private String callStatus;
    /** 是否问题数据 **/
    private boolean problem;
    /** 成单日期 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date finishTime;
    /** 缴费方式 **/
    private Integer payType;
    /** 保险额度 **/
    private BigDecimal insuranceAmount;
    /** 保险费用 **/
    private BigDecimal insuranceFee;
    /** 投保数量 **/
    private Integer insuranceCount;
    /** 备注信息 **/
    private String remark;
    /** 创建时间 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 更新时间 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
