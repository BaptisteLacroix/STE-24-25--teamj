package fr.unice.polytech.equipe.j;

import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;


@Controller("/tempController")
public class TempRestController {
    private record TempRecord(String name, String psw, String test, String test2){}

    @Route(value= "/tempRoute", method=HttpMethod.GET)
    public HttpResponse tempRoute(String test, String test2) {
        System.out.println("get");
        return new HttpResponse(HttpCode.HTTP_200, "jsp");
    }
}
