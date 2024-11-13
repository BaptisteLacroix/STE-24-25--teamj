package fr.unice.polytech.equipe.j.flexiblerestserver;

@Controller("/tempController")
public class TempRestController {
    private record TempRecord(String name, String psw, String test, String test2){}

    @Route(value= "/tempRoute", method=HttpMethod.GET)
    public TempRecord tempRoute(String test, String test2) {
        System.out.println("get");
        return new TempRecord("test", "1,2,3", test, test2);
    }

    @Route(value= "/tempRoute", method=HttpMethod.POST)
    public TempRecord tempRoutePost(String test, String test2) {
        System.out.println("post");
        return new TempRecord("test", "1,2,3", test, test2);
    }

    @Route(value= "/tempRoute", method=HttpMethod.PUT)
    public TempRecord tempRoutePut(String test, String test2) {
        System.out.println("put");
        return new TempRecord("test", "1,2,3", test, test2);
    }
}
