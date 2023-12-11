package com.bnda.webapi.consumer;

import com.bnda.webapi.exception.ApplicationException;
import com.bnda.webapi.publisher.EndPointDTO;
import com.bnda.webapi.publisher.Publisher;
import com.bnda.webapi.publisher.PublisherService;
import com.bnda.webapi.subscription.SubscriptionDTO;
import com.bnda.webapi.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/consumers")
public class ConsumerController {

    private final ConsumerService consumerService;
    private final PublisherService publisherService;
    private final SubscriptionService subscriptionService;

    @GetMapping("")
    public String getConsumers(Model model){
        model.addAttribute("consumers", consumerService.findAll());
        model.addAttribute("consumer", new Consumer());
        return "consumer/consumers";
    }

    @PostMapping("")
    public String addConsumer(Consumer consumer, final RedirectAttributes redirectAttributes){

        consumerService.save(consumer);

        redirectAttributes.addFlashAttribute("type", "success");
        redirectAttributes.addFlashAttribute("message", "A new Consumer has been added!");

        return "redirect:/consumers";
    }


    @GetMapping("/subscriptions/{id}")
    public String getSubscriptions(@PathVariable("id") Long consumerID, ModelMap model) throws ApplicationException {
        model.addAttribute("consumer",consumerService.findById(consumerID));
        model.addAttribute("publishers",publisherService.findAll());
        model.addAttribute("endPoints",publisherService.endPointRepository.findAll());
        model.addAttribute("subscriptionDTO", new SubscriptionDTO());
        return "/consumer/mySubscriptions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttributes){
        consumerService.deleteById(id);
        redirectAttributes.addFlashAttribute("type", "success");
        redirectAttributes.addFlashAttribute("message", "A consumer was deleted!");

        subscriptionService.flushGateway();

        return "redirect:/consumers";
    }

}
