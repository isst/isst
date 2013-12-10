package cn.edu.zju.isst.entity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class ResultHolder {
    private int code;
    private String message;

    public ResultHolder() {
        this.code = 1;
        this.message = "";
    }

    public ResultHolder(int code) {
        this.code = code;
        this.message = "";
    }

    public ResultHolder(String message) {
        this.code = 0;
        this.message = message;
    }

    public ResultHolder(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultHolder(BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (ObjectError oe : result.getAllErrors()) {
                sb.append(oe.getDefaultMessage());
                sb.append("\n");
            }
            this.code = 0;
            this.message = sb.toString();
        } else {
            this.code = 1;
            this.message = "";
        }
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
