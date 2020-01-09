package com.mandob.base.error;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiFieldError {
    private String field;
    private String message;

    public static ApiFieldError of(String field, String message) {
        return new ApiFieldError(field, message);
    }
}
