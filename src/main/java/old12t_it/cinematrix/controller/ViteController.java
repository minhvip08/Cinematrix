package old12t_it.cinematrix.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ViteController {
    private final ModelAndView indexModelAndView;

    @RequestMapping(value = {
            "/",
            "/{path:^(?!(?:favicon\\.ico|robots\\.txt|firebase-messaging-sw\\.js|logout)$)[\\w\\-]+}",
            // sample need to be removed after development
            "/{path:^(?!(?:api|assets|sample)$)[\\w\\-]*$}/**"},
            method = RequestMethod.GET)
    public ModelAndView index() {
        return indexModelAndView;
    }
}