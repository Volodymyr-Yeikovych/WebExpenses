package pl.edu.s28201.webExpenses.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
@Slf4j
public class ErrorMappingController implements ErrorController {

    @GetMapping
    public String handleError(
            HttpServletRequest request,
            Model model
    ) {
        log.info("GET: /error");

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        String errorMessage = "An unexpected error occurred.";
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            errorMessage = "Error " + statusCode + ": " + (message != null ? message.toString() : "An unexpected error occurred.");
        }

        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}
