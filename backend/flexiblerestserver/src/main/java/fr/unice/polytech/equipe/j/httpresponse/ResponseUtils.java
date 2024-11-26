package fr.unice.polytech.equipe.j.httpresponse;

public class ResponseUtils {
    public static String createErrorMessage(String message) {
        return String.format(
                """
                {"errorMessage": %s}
                """, message);
    }

    public static String createErrorMessage(String message, Exception e) {
        return String.format(
                """
                {
                    "errorMessage": %s,
                    "exception": %s,
                }
                """, message, e.toString());
    }
}
