package com.example.springmvc.redirect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/item")
public class ItemController {

    private ItemRepository itemRepository = new ItemRepository();

    /**
     * PRG - Post/Redirect/Get
     */
    @PostMapping("/add")
    public String addItem(Item item){
        return "redirect:/basic/items/" + item.getId();
    }

    /**
     * RedirectAttributes
     */
    @PostMapping("/add-redirect-attr")
    public String addItemRedirectAttr(Item item, RedirectAttributes redirectAttributes) {
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }
}
