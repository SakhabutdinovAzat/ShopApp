package com.store.services;

import com.store.repositories.ItemRepositories;
import com.store.models.Item;
import com.store.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepositories itemRepositories;

    @Autowired
    public ItemService(ItemRepositories itemRepositories) {
        this.itemRepositories = itemRepositories;
    }

    public List<Item> findAll(String sort){
        return itemRepositories.findAll(Sort.by(sort));
    }

    public List<Item> findAll(int offset, int limit, String sort){
        return itemRepositories.findAll(PageRequest.of(offset,limit, Sort.by(sort))).getContent();
    }

    public Item findOne(int id){
        Optional<Item> optionalItem = itemRepositories.findById(id);
        return optionalItem.orElse(null);
    }

    public List<Item> searchByItemName(String query){
        return itemRepositories.findByNameStartingWith(query);
    }

    @Transactional
    public void save(Item item) {
        itemRepositories.save(item);
    }

    @Transactional
    public void update(int id, Item updateItem) {
        Item itemToBeUpdated = itemRepositories.findById(id).get();

        updateItem.setId(id);
        updateItem.setOwner(itemToBeUpdated.getOwner()); // чтобы не терялась связь при обновлении

        itemRepositories.save(updateItem);
    }

    @Transactional
    public void delete(int id) {
        itemRepositories.deleteById(id);
    }

    public Person getItemOwner(int id) {
        Item item = itemRepositories.findById(id).orElse(null);

        return item.getOwner();
    }

/*    @Transactional
    public void setItemOwner(int id, Item updateItem) {
        updateItem.setOwner(Person);
        itemRepositories.save(updateItem);
    }*/

    @Transactional
    public void release(int id){
        itemRepositories.findById(id).ifPresent(
                item -> {
                    item.setOwner(null);
                    item.setTakenAt(null);
                });
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        itemRepositories.findById(id).ifPresent(
                item -> {
                    item.setOwner(selectedPerson);
                    item.setTakenAt(new Date());
                });
    }

    public List<Item> findByOwner(Person person) {
        return itemRepositories.findByOwner(person);
    }

    public List<Item> findByName(String name) {
        return itemRepositories.findByName(name);
    }
}
