package com.registrationlogindemo.repository;

import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.StockPrice;
import com.registrationlogindemo.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    /**
     * Repository for interacting with the SQL database for CRUD operations.
     * Since options cannot be edited (financial business logic), only find and delete (when the User deletes their account) are shown
     */
    List<Option> findByOwner(User user);
    void deleteAll();
}
