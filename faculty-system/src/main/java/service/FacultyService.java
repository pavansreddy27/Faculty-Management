package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fm.model.Faculty;
import com.example.fm.repository.FacultyRepository;

@Service
public class FacultyService {
    @Autowired
    private FacultyRepository repo;

    public Faculty saveFaculty(Faculty f) {
        // auto-generate ID logic here
        return repo.save(f);
    }

    public List<Faculty> getAll() {
        return repo.findAll();
    }

    public Faculty getById(String id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
