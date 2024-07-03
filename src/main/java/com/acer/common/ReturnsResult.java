package com.acer.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 统一返回对象
 * @author acer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnsResult {
    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否成功
     */
    private String msg;

    /**
     * 返回值
     */
    private Object data;

    public ReturnsResult(Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }


    /**
     * 只返回数据
     * @author zhj
     * @date 17:26 2022/12/2
     * @param result 返回值
     * @return com.acer.common.ApiRestResponse<T>
     **/
    public static ReturnsResult successData(Object result) {
        ReturnsResult apiRestResponse = new ReturnsResult();
        apiRestResponse.setMsg("成功");
        apiRestResponse.setData(result);
        return apiRestResponse;
    }

    /**
     * 请求成功带返回值
     * @author acer
     * @date 11:54 2023/5/20
     * @param status 状态
     * @param msg 是否成功
     * @param result 返回值中的data信息
     * @return com.acer.common.ReturnsResult
    **/
    public static ReturnsResult success(Integer status, String msg, Object result) {
        ReturnsResult response = new ReturnsResult();
        response.setStatus(status);
        response.setMsg(msg);
        response.setData(result);
        return response;
    }

    /**
     * 失败时返回
     * @param code 状态码
     * @param msg  错误信息
     * @return ApiRestResponse对象
     */
    public static ReturnsResult error(Integer code, String msg) {
        return new ReturnsResult(code, msg);
    }
    /**
     * 失败时返回
     * @param msg  错误信息
     * @return ApiRestResponse对象
     */
    public static ReturnsResult error(String msg) {
        return new ReturnsResult(400, msg);
    }

}


