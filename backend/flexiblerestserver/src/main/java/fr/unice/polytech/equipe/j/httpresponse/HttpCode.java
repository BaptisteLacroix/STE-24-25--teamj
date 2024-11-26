package fr.unice.polytech.equipe.j.httpresponse;

public enum HttpCode {
    HTTP_200(200),
    HTTP_201(201),
    HTTP_400(400),
    HTTP_404(404),
    HTTP_419(419),
    HTTP_500(500);

    private int code;
    HttpCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
