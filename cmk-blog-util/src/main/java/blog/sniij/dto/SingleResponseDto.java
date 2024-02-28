package blog.sniij.dto;


public class SingleResponseDto<T> {
    private T data;

    public SingleResponseDto(T data) {
        this.data = data;
    }
    public T getData() {
        return data;
    }

}
