package com.reggie.Utils;


import com.google.gson.*;
import com.montnets.mwgate.common.GlobalParams;
import com.montnets.mwgate.common.Message;
import com.montnets.mwgate.smsutil.ConfigManager;
import com.montnets.mwgate.smsutil.SmsSendConn;

import java.util.Date;

public class SmsUtils {
    private static SmsSendConn smsSendConn = null;
    private static String userid = "E10GU4";
    static{


        GlobalParams globalParams = new GlobalParams();
        // 设置请求路径
        globalParams.setRequestPath("/sms/v2/std/");
        // 设置是否需要日志 1:需要日志;0:不需要日志
        globalParams.setNeedLog(1);
        // 设置全局参数
        ConfigManager.setGlobalParams(globalParams);

        // 设置用户账号信息
        setAccountInfo();

        // 是否保持长连接
        boolean isKeepAlive = true;
        // 实例化短信处理对象
        smsSendConn = new SmsSendConn(isKeepAlive);
        // 单条发送

    }

    public static void singleSend(String phoneNum,String code,String time) {


        try {
            // 参数类
            Message message = new Message();
            // 设置用户账号 指定用户账号发送，需要填写用户账号，不指定用户账号发送，无需填写用户账号
            message.setUserid(userid);
            // 设置手机号码 此处只能设置一个手机号码
            message.setMobile(phoneNum);
            // 设置内容
            message.setContent("您的验证码是 "+code+"，在 "+time+" 分钟内有效。如非本人操作请忽略本短信。");
            // 用户自定义流水编号
            message.setCustid(String.valueOf(new Date().getTime()));
            // 自定义扩展数据
            message.setExdata("187563");
            // 业务类型
            message.setSvrtype("SMS001");

            // 返回的流水号
            StringBuffer returnValue = new StringBuffer();
            // 返回值
            int result = -310099;
            // 发送短信
            result = smsSendConn.singleSend(message, returnValue);
            // result为0:成功
            if (result == 0) {
                System.out.println("单条发送提交成功！");
                System.out.println(returnValue.toString());
            }
            // result为非0：失败
            else {
                System.out.println("单条发送提交失败,错误码：" + result);
            }
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
        }
    }
    public static void setAccountInfo() {
        // 设置用户账号信息

        // 用户账号
        String userid = "E10GU4";
        // 密码
        String password = "cP64aP";
        // 发送优先级
        int priority = 1;
        // 主IP信息
        String ipAddress1 = "api01.monyun.cn:7901";

        // 备用IP1信息
        String ipAddress2 = "api02.monyun.cn:7901";
        // 备用IP2信息
        String ipAddress3 = null;
        // 备用IP3信息
        String ipAddress4 = null;
        // 返回值
        int result = -310007;
        try {
            // 设置用户账号信息
            result = ConfigManager.setAccountInfo(userid, password, priority,
                    ipAddress1, ipAddress2, ipAddress3, ipAddress4);
            // 判断返回结果，0设置成功，否则失败
            if (result == 0) {
                System.out.println("设置用户账号信息成功！");
            } else {
                System.out.println("设置用户账号信息失败，错误码：" + result);
            }
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
        }
    }
}
