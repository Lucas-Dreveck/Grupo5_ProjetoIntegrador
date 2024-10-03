package com.ambientese.grupo5.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ambientese.grupo5.DTO.QuestionRegistration;
import com.ambientese.grupo5.Model.QuestionModel;
import com.ambientese.grupo5.Model.Enums.PillarEnum;
import com.ambientese.grupo5.Repository.AnswerRepository;
import com.ambientese.grupo5.Repository.QuestionRepository;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public List<QuestionModel> listQuestions() {
        return questionRepository.findAll();
    }

    public List<QuestionModel> listQuestionsPerPillar(PillarEnum pillar) {
        return questionRepository.findByPillar(pillar);
    }

    public List<QuestionRegistration> allPagedQuestionsWithFilter(String name, int page, int size) {
        List<QuestionModel> questions;
        if (name != null && !name.isEmpty()) {
            questions = questionRepository.findAllByDescriptionStartingWithIgnoreCaseOrderByDescriptionAsc(name);

            if (questions.isEmpty()) {
                questions = questionRepository.findAllOrderByDescriptionAsc().stream()
                        .filter(question -> question.getPillar().toString().toLowerCase().startsWith(name.toLowerCase()))
                        .collect(Collectors.toList());
            }
        } else {
            questions = questionRepository.findAll();
        }

        List<QuestionRegistration> result = pageQuestions(questions, page, size);

        return result;
    }

    private List<QuestionRegistration> pageQuestions(List<QuestionModel> questions, int page, int size) {
        int total = questions.size();
        int start = Math.min(page * size, total);
        int end = Math.min((page + 1) * size, total);

        return questions.subList(start, end).stream()
                .map(question -> new QuestionRegistration(
                        question.getId(),
                        question.getDescription(),
                        question.getPillar(),
                        end == total
                ))
                .collect(Collectors.toList());
    }

    public QuestionModel createQuestion(String description, PillarEnum pillar) {
        QuestionModel newQuestion = new QuestionModel();
        newQuestion.setDescription(description);
        newQuestion.setPillar(pillar);
        return questionRepository.save(newQuestion);
    }

    public QuestionModel updateQuestion(long id, String newDescription, PillarEnum newPillar) {
        QuestionModel existingQuestion = questionRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Pergunta não encontrada"));

        existingQuestion.setDescription(newDescription);
        existingQuestion.setPillar(newPillar);

        return questionRepository.save(existingQuestion);
    }

    public ResponseEntity<String> deleteQuestion(long id) {
        QuestionModel question = questionRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("Pergunta não encontrada"));

        if (answerRepository.existsByQuestionId(question.getId())) {
            return ResponseEntity.badRequest().body("Erro ao deletar, a pergunta está sendo utilizadas em avaliações.");
        } else {
            questionRepository.delete(question);
            return ResponseEntity.noContent().build();
        }
    }
}
