package br.com.tinylink.api.utils;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.tinylink.api.models.Link;
import br.com.tinylink.api.service.LinkService;

@Component
public class ShortenerUtil {

    @Autowired
    private LinkService linkService;

    public String shortener(String url) {
        try {
            Integer code = generateCode();
            Link newLink = new Link(url, code);

            if (!linkService.add(newLink)) {
                throw new IllegalStateException("Erro ao salvar o link encurtado");
            }

            return "http://localhost:8080/tinylink/" + code;

        } catch (Exception e) {
            System.err.println("Erro ao encurtar link: " + e.getMessage());
            return "Erro ao encurtar link";
        }
    }

    private Integer generateCode() {
        Random generator = new Random();
        Integer code;
        do {
            code = generator.nextInt(999999 - 111111 + 1) + 111111;
        } while (!isValidCode(code));
        return code;
    }

    private Boolean isValidCode(Integer code) {
        return linkService.findLinkByCode(code).isEmpty();
    }
}