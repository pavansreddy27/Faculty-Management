package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.Faculty;
import service.FacultyService;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "http://localhost:3002")
public class FacultyController {
    @Autowired
    private FacultyService service;

    @PostMapping
    public Faculty add(@RequestBody Faculty f) {
        return service.saveFaculty(f);
    }

    @GetMapping
    public List<Faculty> list() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Faculty get(@PathVariable String id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Faculty update(@PathVariable String id, @RequestBody Faculty f) {
        f.setId(id);
        return service.saveFaculty(f);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteById(id);
    }
}
