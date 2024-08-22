package br.com.planner.planner.service;

import br.com.planner.planner.domain.link.Link;
import br.com.planner.planner.domain.link.LinkDetails;
import br.com.planner.planner.domain.link.LinkRequestDTO;
import br.com.planner.planner.domain.link.LinkResponseDTO;
import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public LinkResponseDTO registerLink(Trip trip, LinkRequestDTO data) {
        Link link = new Link(data.title(), data.url(), trip);
        this.linkRepository.save(link);
        return new LinkResponseDTO(link.getId());
    }

    public List<LinkDetails> getAllLinkFromId(UUID id) {
        return this.linkRepository.findByTripId(id)
                .stream().map(link -> new LinkDetails(
                        link.getId(),
                        link.getTitle(),
                        link.getUrl())).toList();
    }

    public String deleteLink(UUID id){
        linkRepository.deleteById(id);
        return "Link deletado com sucesso";
    }
}
