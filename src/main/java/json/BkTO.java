package json;

import lombok.Data;

@Data
public class BkTO {
    private String message;
    private Integer code;
    private  String data;
    private Boolean result;
}
