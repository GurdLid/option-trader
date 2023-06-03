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
    //Possible find by trader id?
    //@Query("SELECT u FROM options u WHERE u.owner.id = :userId")
    //List<Option> getOptionsForUser(long userId);
}
