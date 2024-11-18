package fr.unice.polytech.equipe.j.flexiblerestserver;

import fr.unice.polytech.equipe.j.flexiblerestserver.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.flexiblerestserver.httpresponse.HttpResponse;


@Controller("/tempController")
public class TempRestController {
    private record TempRecord(String name, String psw, String test, String test2){}

    @Route(value= "/tempRoute", method=HttpMethod.GET)
    public HttpResponse tempRoute(String test, String test2) {
        System.out.println("get");
        return new HttpResponse(HttpCode.HTTP_200, "jsp");
    }
}
