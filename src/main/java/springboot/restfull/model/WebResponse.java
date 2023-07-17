package springboot.restfull.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {
    // <T> merupakan generic class

    private T data;

    private String errors;

    private PagingResponse paging;
}
