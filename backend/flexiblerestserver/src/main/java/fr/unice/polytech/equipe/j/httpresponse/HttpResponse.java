package fr.unice.polytech.equipe.j.httpresponse;

public class HttpResponse<T>{
    private HttpCode code;
    private T content;
    public HttpResponse(HttpCode code, T content) {
        this.code = code;
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public int getCode() {
        return this.code.getCode();
    }
}
