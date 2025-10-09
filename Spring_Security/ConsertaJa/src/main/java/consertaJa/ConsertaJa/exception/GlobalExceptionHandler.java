package consertaJa.ConsertaJa.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdNaoEncontradoException.class)
    public ModelAndView handleNotFound(IdNaoEncontradoException ex, HttpServletRequest request) {
        return baseModelAndView("error/404", HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        return baseModelAndView("error/400", HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntime(RuntimeException ex, HttpServletRequest request) {
        return baseModelAndView("error/400", HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletRequest request) {
        return baseModelAndView("error/500", HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    private ModelAndView baseModelAndView(String view, HttpStatus status, Exception ex, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(view);
        mv.setStatus(status);
        mv.addObject("status", status.value());
        mv.addObject("error", status.getReasonPhrase());
        mv.addObject("message", ex.getMessage());
        mv.addObject("path", request.getRequestURI());
        mv.addObject("timestamp", java.time.LocalDateTime.now());
        return mv;
    }
}

