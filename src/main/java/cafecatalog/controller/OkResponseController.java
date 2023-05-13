package cafecatalog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class OkResponseController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String statusOk() {
        return "Status OK.";
    }
}
