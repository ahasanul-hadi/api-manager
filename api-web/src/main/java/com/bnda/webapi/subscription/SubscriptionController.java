package com.bnda.webapi.subscription;

import com.bnda.webapi.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("")
    public String subscribe(SubscriptionDTO subscriptionDTO) throws ApplicationException {
        subscriptionService.subscribe(subscriptionDTO);

        subscriptionService.flushGateway();

        return "redirect:/consumers/subscriptions/"+subscriptionDTO.getConsumerID();
    }

    @GetMapping("/delete")
    public String delete(SubscriptionDTO subscriptionDTO) throws ApplicationException {
        subscriptionService.subscribe(subscriptionDTO);

        subscriptionService.flushGateway();

        return "redirect:/consumers/subscriptions/"+subscriptionDTO.getConsumerID();
    }


    @GetMapping("/delete/consumer/{id}/subscriptions/{subID}")
    public String deleteSubscription(@PathVariable("id") Long id, @PathVariable("subID") Long subID, final RedirectAttributes redirectAttributes){
       subscriptionService.deleteById(subID);
        subscriptionService.flushGateway();
        redirectAttributes.addFlashAttribute("type", "success");
        redirectAttributes.addFlashAttribute("message", "A publisher was deleted!");
        return "redirect:/consumers/subscriptions/"+id;
    }


}
