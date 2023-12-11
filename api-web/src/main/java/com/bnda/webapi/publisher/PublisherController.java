package com.bnda.webapi.publisher;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/publishers")
@RequiredArgsConstructor
@Slf4j
public class PublisherController {

    private final PublisherService publisherService;

    @PostMapping("")
    public String addPublisher( @ModelAttribute("dto") Publisher dto, BindingResult result, final RedirectAttributes redirectAttributes){

        log.info("publisher:"+dto.toString());
        publisherService.save(dto);

        redirectAttributes.addFlashAttribute("type", "success");
        redirectAttributes.addFlashAttribute("message", "A new publisher has been added!");

        return "redirect:/publishers";
    }

    @GetMapping("")
    public String getPublishers(ModelMap model){
        model.addAttribute("publishers",publisherService.findAll());
        model.addAttribute("dto",new Publisher());
        return "/publisher/publisher";
    }


    @GetMapping("/endpoints/{id}")
    public String getEndpoints(@PathVariable("id") Long publisherID, ModelMap model){
        model.addAttribute("publisher",publisherService.findById(publisherID).orElseThrow());
        model.addAttribute("endPointDTO", new EndPointDTO());
        return "/publisher/endpoints";
    }


    @PostMapping("/endpoints")
    public String saveEndpoints(EndPointDTO endPointDTO, ModelMap model){
        log.info("save endPoint:"+endPointDTO.toString());
        publisherService.saveEndPoint(endPointDTO);
        return "redirect:/publishers/endpoints/"+endPointDTO.getPublisherID();
    }

    @GetMapping("/delete/{id}")
    public String deletePublisher(@PathVariable("id") Long id, final RedirectAttributes redirectAttributes){
        publisherService.deleteById(id);
        redirectAttributes.addFlashAttribute("type", "success");
        redirectAttributes.addFlashAttribute("message", "A publisher was deleted!");
        return "redirect:/publishers";
    }

    @GetMapping("/delete/publisher/{id}/end-points/{endPoint}")
    public String deleteEndPoint(@PathVariable("id") Long id, @PathVariable("endPoint") Long endPointID, final RedirectAttributes redirectAttributes){
        publisherService.deleteEndPoint(endPointID);
        redirectAttributes.addFlashAttribute("type", "success");
        redirectAttributes.addFlashAttribute("message", "A publisher was deleted!");
        return "redirect:/publishers/endpoints/"+id;
    }

}
