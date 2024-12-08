package fr.unice.polytech.equipe.j.utils;

import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;

@Controller("/api/database")
public class HealthController {
    @Route(value = "/health", method = HttpMethod.GET)
    public HttpResponse checkHealth() {
        System.out.println("API is live");
        return new HttpResponse(HttpCode.HTTP_200, "API is live");
    }
}
