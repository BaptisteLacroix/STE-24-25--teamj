package fr.unice.polytech.equipe.j.httpresponse;

public enum HttpCode {
    HTTP_200(200),
    HTTP_201(201),
    HTTP_400(400),
    HTTP_403(403),
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
    // Get the enum value from the integer code
    public static HttpCode fromCode(int code) {
        for (HttpCode httpCode : HttpCode.values()) {
            if (httpCode.getCode() == code) {
                return httpCode;
            }
        }
        return null;
    }
}
