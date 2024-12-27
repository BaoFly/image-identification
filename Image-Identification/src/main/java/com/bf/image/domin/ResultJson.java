package com.bf.image.domin;

import com.bf.image.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultJson<T> {
        // 状态码
        private int code;

        // 数据
        private T data;

        // 信息
        private String msg;

        public static <T> ResultJson<T> of(int code, T data, String msg) {
            return new ResultJson<>(code, data, msg);
        }

        public static <T> ResultJson<T> success() {
            return of(CommonConstant.SUCCESS_CODE, null, "Success");
        }
        public static <T> ResultJson<T> success(T data, String msg) {
            return of(CommonConstant.SUCCESS_CODE, data, msg);
        }

        public static <T> ResultJson<T> success(T data) {
            return of(CommonConstant.SUCCESS_CODE, data, "Success");
        }

        public static <T> ResultJson<T> fail(String msg) {
            return of(CommonConstant.FAIL_CODE, null, msg);
        }


}
