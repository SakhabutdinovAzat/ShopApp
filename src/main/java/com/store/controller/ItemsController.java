package com.store.controller;

import com.store.models.Item;
import com.store.models.Person;
import com.store.services.ItemService;
import com.store.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/items")
public class ItemsController {

    private final ItemService itemService;
    private final PeopleService peopleService;

    @Autowired
    public ItemsController(ItemService itemService, PeopleService peopleService) {
        this.itemService = itemService;
        this.peopleService = peopleService;
    }

    //                        @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
    //                        @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(3) int limit,
    //                        @RequestParam(value = "sort", defaultValue = "name") String sort)

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                        @RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit,
                        @RequestParam(value = "sort", defaultValue = "name", required = false) String sort) {

        if (offset == 0 || limit == 0)
            model.addAttribute("items", itemService.findAll(sort));
        else
            model.addAttribute("items", itemService.findAll(offset, limit, sort));

        return "items/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("person") Person person,
                       Model model) {
        model.addAttribute("item", itemService.findOne(id));

        Person itemOwner = itemService.getItemOwner(id);

        if (itemOwner != null)
            model.addAttribute("owner", itemOwner);
        else
            model.addAttribute("people", peopleService.findAll());

        return "items/show";
    }

    @GetMapping("/new")
    public String newItem(Model model) {
        model.addAttribute("item", new Item());

        return "items/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult) {
        /*        bookValidator.validate(book, bindingResult);*/
        if (bindingResult.hasErrors())
            return "items/new";

        itemService.save(item);
        return "redirect:/items";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("item", itemService.findOne(id));

        return "items/edit";
    }


    @PatchMapping("/{id}")
    public String update(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "items/edit";
        }

        itemService.update(id, item);

        return "redirect:/items";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        itemService.delete(id);

        return "redirect:/items";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        itemService.release(id);

        return "redirect:/items/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        itemService.assign(id, selectedPerson);

        return "redirect:/items/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "items/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("items", itemService.searchByItemName(query));

        return "items/search";
    }
}
