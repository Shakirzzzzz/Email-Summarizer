package com.shakir.email_summarizer.Repository;

import com.shakir.email_summarizer.Entity.Email;
import org.springframework.data.repository.CrudRepository;

public interface emailRepository extends CrudRepository<Email,String> {

}
