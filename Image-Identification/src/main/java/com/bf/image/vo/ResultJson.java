package com.bf.image.vo;

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
            return of(200, null, "Success");
        }

        public static <T> ResultJson<T> success(T data) {
            return of(200, data, "Success");
        }

        public static <T> ResultJson<T> fail(int code, String msg) {
            return of(code, null, msg);
        }


}
