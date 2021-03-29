package pl.wsb.restquiz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.wsb.restquiz.entity.NewQuizDto;
import pl.wsb.restquiz.entity.Quiz;
import pl.wsb.restquiz.entity.User;

import javax.validation.Valid;
import java.util.*;

@RestController
public class QuizController {
    final List<Quiz> localRepository = new ArrayList<>();

    public QuizController() {
        localRepository.add( Quiz.builder().question("Test 1")
                .options("1\n2\n\3")
                .answers("1")
                .build());
        localRepository.add( Quiz.builder().question("Test 2")
                .options("1\n2\n\3")
                .answers("2")
                .build());
    }

    @GetMapping("/api/quizzes")
    public List<Quiz> quizzes() {
        return localRepository;
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> quiz(@PathVariable(name="id") int id){
        Optional<Quiz> response;
        if (id < 1 || id > localRepository.size()){
            response = Optional.empty();
        } else {
            response = Optional.of(localRepository.get(id - 1));
        }
        return ResponseEntity.of(response);
    }

    @PostMapping("/api/quizzes")
    @ResponseStatus(HttpStatus.CREATED)
    public Quiz newQuiz(@Valid @RequestBody NewQuizDto newQuizDto){
        Quiz quiz = Quiz.builder()
                .question(newQuizDto.getQuestion())
                .options(getString(newQuizDto.getOptions()))
                .answers(getString(newQuizDto.getAnswers()))
                .build();
        localRepository.add(quiz);
        return quiz;
    }

    @DeleteMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> deleteQuiz(@PathVariable int id){
        Optional<Quiz> response;
        if (id < 1 || id > localRepository.size()){
            response = Optional.empty();
        } else {
            response = Optional.of(localRepository.get(id - 1));
            localRepository.remove(id - 1);
        }
        return ResponseEntity.of(response);
    }

    @PutMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable int id, @Valid @RequestBody NewQuizDto newQuizDto){
        Optional<Quiz> response;
        if (id < 1 || id > localRepository.size()){
            return  ResponseEntity.of(Optional.empty());
        }
        Quiz quiz = localRepository.get(id - 1);
        quiz.setQuestion(newQuizDto.getQuestion());
        quiz.setOptions(getString(newQuizDto.getOptions()));
        quiz.setAnswers(getString(newQuizDto.getAnswers()));
        return ResponseEntity.of(Optional.of(quiz));
    }

    private String getString(String[] s) {
        StringBuffer sb = new StringBuffer();
        for(String option: s){
            sb.append(option+"\n");
        }
        return sb.toString();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
