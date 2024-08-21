package org.office.manager.repository;

import org.office.manager.entity.Contact;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer> {

	@Query("from Contact as c where c.user.id =:id")
	public Page<Contact> findContactsByUser(@Param("id") int id, PageRequest pg);

}
