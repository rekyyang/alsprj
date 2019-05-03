package zju.edu.als.domain.result;

import java.io.Serializable;

/**
 * Created by zzq on 2016/10/29.
 */
public class Result implements Serializable{
    private static final long serialVersionUID = 1L;
    private boolean success;
    private String message;
    private Integer code;
    private Object data;

    public Result() {
        this.success = true;
    }

    public Result(Object data) {
        this.success = true;
        this.data = data;
        this.code=200;
    }

    public Result(boolean success, Integer code,String message) {
        super();
        this.success = success;
        this.code=code;
        if (message != null) {
            this.message = message;
        }
    }

    public static Result ok() {
        return new Result();
    }

    public static Result ok(Object data) {
        if (data == null) {
            throw new NullPointerException();
        }
        return new Result(data);
    }

    public static Result okMsg(String message) {
        return new Result(true, 200,message);
    }

    public static Result fail(String msg) {
        if (msg == null) {
            msg = "NullPointException";
        }
        return new Result(false,500,msg);
    }
    public static Result fail(Integer code,String msg){
        if (msg == null) {
            msg = "NullPointException";
        }
        return new Result(false,302,msg);
    }

    public static Result fail(Throwable throwable) {
        String s = throwable.getMessage();
        return fail(s);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message != null) {
            this.message = message;
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setAll(boolean success, Integer code, String message) {
        this.success = success;
        this.code=code;
        if (message != null) {
            this.message = message;
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
