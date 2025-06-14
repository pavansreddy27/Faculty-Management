package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, String> {
}
