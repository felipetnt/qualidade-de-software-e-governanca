package br.com.tinylink.api.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import br.com.tinylink.api.models.Link;
import br.com.tinylink.api.models.Entry;
import br.com.tinylink.api.service.LinkService;
import br.com.tinylink.api.utils.ShortenerUtil;

@RestController
@RequestMapping("/tinylink")
public class ShortenerController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private ShortenerUtil shortenerUtil;

    @GetMapping("/{code}")
    public RedirectView redirect(@PathVariable Integer code) {
        Optional<Link> link = linkService.findLinkByCode(code);

        if (link.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Link não encontrado");
        }

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(link.get().getUrl());
        return redirectView;
    }

    @PostMapping
    public ResponseEntity<String> shortener(@RequestBody Entry entry) {
        String shortUrl = shortenerUtil.shortener(entry.getUrl());
        if (shortUrl == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao encurtar URL");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
    }

    @GetMapping
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("pong");
    }
}