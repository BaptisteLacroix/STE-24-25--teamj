package fr.unice.polytech.equipe.j.flexiblerestserver;

@Controller("/tempController")
public class TempRestController {
    private record TempRecord(String name, String psw, String test, String test2){}

    @Route(value= "/tempRoute", method=HttpMethod.GET)
    public TempRecord tempRoute(String test, String test2) {
        return new TempRecord("test", "1,2,3", test, test2);
    }
}
