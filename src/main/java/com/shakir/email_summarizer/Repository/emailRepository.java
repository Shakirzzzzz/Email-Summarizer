package com.shakir.email_summarizer.Repository;

import com.shakir.email_summarizer.Entity.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface emailRepository extends CrudRepository<Email,String> {


    @Query("select e from Email e where e.user.id = :id")
    Page<Email> findByUserId(@Param("id") Long id, Pageable pageable);
}
