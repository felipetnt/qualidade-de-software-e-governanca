package br.com.tinylink.api.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.tinylink.api.models.Link;
import br.com.tinylink.api.repository.LinkRepository;

@Service
public class LinkService {

    private static final Logger logger = LoggerFactory.getLogger(LinkService.class);

    @Autowired
    private LinkRepository linkRepository;

    public boolean add(Link link) {
        try {
            linkRepository.save(link);
            logger.info("Link salvo com sucesso: {}", link.getUrl());
            return true;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade ao salvar link (código duplicado): {}", link.getCode(), e);
            return false;
        } catch (Exception e) {
            logger.error("Erro inesperado ao salvar link: {}", link.getUrl(), e);
            return false;
        }
    }

    public List<Link> list() {
        return linkRepository.findAll();
    }

    public Optional<Link> findLinkByCode(Integer code) {
        logger.info("Buscando link pelo código: {}", code);
        return linkRepository.findByCode(code);
    }
}